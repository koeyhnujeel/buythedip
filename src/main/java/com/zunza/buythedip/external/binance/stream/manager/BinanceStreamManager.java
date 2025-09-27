package com.zunza.buythedip.external.binance.stream.manager;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.crypto.repository.CryptoRepository;
import com.zunza.buythedip.crypto.service.CryptoService;
import com.zunza.buythedip.external.binance.stream.handler.KlineStreamHandler;
import com.zunza.buythedip.external.binance.stream.handler.TickerStreamHandler;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BinanceStreamManager {
	private final RedissonClient redissonClient;
	private final ObjectMapper objectMapper;
	private final WebSocketClient webSocketClient;
	private final CryptoService cryptoService;
	private final CryptoRepository cryptoRepository;

	private static final String LEADER_LOCK_KEY = "BINANCE:STREAM:LEADER:LOCK";
	private static final String STREAM_URL = "wss://data-stream.binance.vision/stream";
	private static final String TICKER_STREAM_SUFFIX = "usdt@miniTicker";
	private static final String KLINE_STREAM_SUFFIX = "usdt@kline_";
	private static final int CHUNK_SIZE = 203;

	private RLock lock;
	private volatile boolean isLeader = false;
	private volatile long lastConnectionTime = 0L;
	private final List<WebSocketSession> activeSessions = new CopyOnWriteArrayList<>();
	private final String hostname = System.getenv("HOSTNAME");

	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReadyEvent(ApplicationReadyEvent event) {
		Thread thread = new Thread(this::tryToBecomeLeader);
		thread.setDaemon(true);
		thread.start();
	}

	@PreDestroy
	public void destroy() {
		if (lock != null && lock.isHeldByCurrentThread()) {
			lock.unlock();
			log.info("애플리케이션 종료... 락을 해제했습니다.");
		}
	}

	public void tryToBecomeLeader() {
		lock = redissonClient.getLock(LEADER_LOCK_KEY);

		try {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					lock.lock();
					isLeader = true;
					log.info("[{}] 리더로 지정... 스트림 연결을 시작합니다.", hostname);
					stopStreams();
					startStreams();

					while (lock.isLocked() && lock.isHeldByCurrentThread()) {
						Thread.sleep(5000);
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				} finally {
					if (lock.isHeldByCurrentThread()) {
						log.warn("락을 해제합니다.");
						lock.unlock();
					}
					isLeader = false;
					log.warn("[{}] 리더 해제... 스트림 연결을 종료합니다.", hostname);
					stopStreams();
				}
			}
		} catch (Exception e) {
			log.error("예기치 않은 오류 발생", e);
		}
	}

	@Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
	public void scheduledReconnect() {
		if (!isLeader) {
			return;
		}

		if (lastConnectionTime == 0L) {
			return;
		}

		long uptime = System.currentTimeMillis() - lastConnectionTime;
		long tenHoursInMillis = 10 * 60 * 60 * 1000L;

		if (uptime > tenHoursInMillis) {
			log.info("연결 유지 시간이 10시간을 초과했습니다 ({}분 경과). 재연결을 시작합니다.", TimeUnit.MILLISECONDS.toMinutes(uptime));

			stopStreams();
			startStreams();
		}
	}

	private void startStreams() {
		List<String> symbols = cryptoRepository.findAll().stream()
			.map(crypto -> crypto.getSymbol().toLowerCase())
			.toList();

		if (symbols.isEmpty()) {
			log.warn("스트림을 시작할 심볼이 없습니다.");
			return;
		}

		startTickerStream(symbols);
		startKlineStream(symbols);
		this.lastConnectionTime = System.currentTimeMillis();
		log.info("스트림 연결이 완료되었습니다. 마지막 연결 시간: {}", new Date(this.lastConnectionTime));
	}

	private void stopStreams() {
		log.info("모든 활성 웹소켓 세션({})을 종료합니다.", activeSessions.size());
		activeSessions.forEach(session -> {
			try {
				if (session.isOpen()) {
					session.close();
				}
			} catch (Exception e) {
				log.warn("웹소켓 세션 종료 중 오류 발생: {}", session.getId(), e);
			}
		});
		activeSessions.clear();
		log.info("스트림 연결 해제가 완료되었습니다. 마지막 연결 해제 시간: {}", new Date(System.currentTimeMillis()));
	}

	private void startTickerStream(List<String> symbols) {
		List<String> params = symbols.stream()
			.map(symbol -> symbol + TICKER_STREAM_SUFFIX)
			.toList();

		for (int i = 0; i < params.size(); i += CHUNK_SIZE) {
			List<String> chunk = params.subList(i, Math.min(i + CHUNK_SIZE, params.size()));
			TickerStreamHandler handler = new TickerStreamHandler(chunk, objectMapper, cryptoService);
			webSocketClient.execute(handler, STREAM_URL)
				.thenAccept(activeSessions::add);

			log.info("Ticker 스트림 연결 시작... {}개의 심볼", chunk.size());
		}
	}

	private void startKlineStream(List<String> symbols) {
		List<String> klineIntervals = List.of("1m", "3m", "5m", "15m", "30m", "1h", "4h", "1d", "1w", "1M");

		klineIntervals.forEach(interval -> {
			List<String> params = symbols.stream()
				.map(symbol -> symbol + KLINE_STREAM_SUFFIX + interval)
				.toList();

			for (int i = 0; i < params.size(); i += CHUNK_SIZE) {
				List<String> chunk = params.subList(i, Math.min(i + CHUNK_SIZE, params.size()));
				KlineStreamHandler handler = new KlineStreamHandler(chunk, objectMapper, cryptoService);
				webSocketClient.execute(handler, STREAM_URL)
					.thenAccept(activeSessions::add);

				log.info("Kline 스트림 연결 시작... {}개의 심볼 {} 인터벌", chunk.size(), interval);
			}
		});
	}
}
