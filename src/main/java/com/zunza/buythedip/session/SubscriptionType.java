package com.zunza.buythedip.session;

import lombok.Getter;

@Getter
public enum SubscriptionType {
    TRADE("/topic/crypto/trade", "trade"),
    KLINE("/topic/crypto/kline", "kline"),
    CHAT("/topic/chat", "chat");

    private final String destinationPrefix;
    private final String managerKey;

    SubscriptionType(String destinationPrefix, String managerKey) {
        this.destinationPrefix = destinationPrefix;
        this.managerKey = managerKey;
    }

    public static SubscriptionType from(String destination) {
        if (destination == null) {
            return CHAT;
        }
        for (SubscriptionType type : values()) {
            if (destination.startsWith(type.destinationPrefix)) {
                return type;
            }
        }
        return CHAT;
    }
}
