package com.zunza.buythedip.session;

import lombok.Getter;

@Getter
public enum RedisKeyPrefix {
    SUBSCRIBER_COUNT("subscribers:count:"),
    SESSION_SUBSCRIPTIONS("session:subs:"),
    SUBSCRIPTION_DESTINATION("sub:dest:");

    private final String prefix;

    RedisKeyPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getKey(String suffix) {
        return prefix + suffix;
    }
    
    public String getKey(String... suffixes) {
        return prefix + String.join(":", suffixes);
    }
}
