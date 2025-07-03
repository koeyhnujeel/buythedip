package com.zunza.buythedip.cryptocurrency.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zunza.buythedip.cryptocurrency.client.BinanceClient;
import com.zunza.buythedip.cryptocurrency.client.CoinMarketCapClient;
import com.zunza.buythedip.cryptocurrency.dto.binance.ExchangeInfoDto;
import com.zunza.buythedip.cryptocurrency.dto.binance.SymbolDto;
import com.zunza.buythedip.cryptocurrency.dto.cmc.CryptoCurrencyMetadataDto;
import com.zunza.buythedip.cryptocurrency.dto.cmc.MetadataDetailDto;
import com.zunza.buythedip.cryptocurrency.dto.cmc.UrlsDto;
import com.zunza.buythedip.cryptocurrency.entity.Cryptocurrency;
import com.zunza.buythedip.cryptocurrency.entity.CryptocurrencyMetadata;
import com.zunza.buythedip.cryptocurrency.repository.CryptocurrencyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class dataInit {

	private final BinanceClient binanceClient;
	private final CoinMarketCapClient cmcClient;
	private final CryptocurrencyRepository cryptoCurrencyRepository;

	// @PostConstruct
	private void cryptoCurrencyDataInit() {
		ExchangeInfoDto exchangeInfo = binanceClient.getExchangeInfo();
		List<SymbolDto> filteredSymbols = filterUsdtPairsAndTrading(exchangeInfo.getSymbols());

		log.info(String.valueOf(filteredSymbols.size()));
		filteredSymbols.forEach(symbol ->
			log.info("{} / {}", symbol.getBaseAsset(), symbol.getStatus())
			);

		List<String> baseAssetList = filteredSymbols.stream()
			.map(SymbolDto::getBaseAsset)
			.toList();

		List<List<String>> partitionList = partitionList(baseAssetList, 100);
		List<CryptoCurrencyMetadataDto> result = new ArrayList<>();

		for (List<String> list : partitionList) {
			String symbolParam = String.join(",", list);
			CryptoCurrencyMetadataDto metadata = cmcClient.getCryptoCurrencyMetadata(symbolParam);

			result.add(metadata);
		}

		List<Cryptocurrency> cryptocurrencyList = new ArrayList<>();

		for (CryptoCurrencyMetadataDto metadata : result) {
			for (String s : metadata.getData().keySet()) {

				List<MetadataDetailDto> metadataDetailDtos = metadata.getData().get(s);
				if (metadataDetailDtos == null || metadataDetailDtos.isEmpty()) continue;

				MetadataDetailDto detail = metadataDetailDtos.get(0);
				if (detail == null) continue;

				UrlsDto urls = detail.getUrls();
				CryptocurrencyMetadata cryptoCurrencyMetadata = CryptocurrencyMetadata.of(
					detail.getLogo(), detail.getDescription(), urls.getWebsite(),
					urls.getTwitter(), urls.getExplorer(), detail.getTagNames());

				Cryptocurrency cryptoCurrency = Cryptocurrency.of(detail.getName(), detail.getSymbol(), "TRADING", cryptoCurrencyMetadata);
				cryptocurrencyList.add(cryptoCurrency);
			}
		}

		cryptoCurrencyRepository.saveAll(cryptocurrencyList);
		log.info("==============COMPLETE===================");
	}

	private List<SymbolDto> filterUsdtPairsAndTrading(List<SymbolDto> symbols) {
		return symbols.stream()
			.filter(symbol -> symbol.getQuoteAsset().equals("USDT"))
			.filter(symbol -> symbol.getStatus().equals("TRADING"))
			.toList();
	}

	private List<List<String>> partitionList(List<String> list, int size) {
		List<List<String>> result = new ArrayList<>();
		for (int i = 0; i < list.size(); i += size) {
			result.add(list.subList(i, Math.min(i + size, list.size())));
		}
		return result;
	}
}
