package com.nimofy.nimofyserver.dto.exchanges;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Exchange {

    BINANCE("binance", "https://www.binance.com"),

    BITFINEX("bitfinex", "https://www.bitfinex.com"),

    BITMART("bitmart", "https://www.bitmart.com"),

    BITTREX("bittrex", "https://global.bittrex.com"),

    BYBIT("bybit", "https://www.bybit.com"),

    KRAKEN("kraken", "https://www.kraken.com"),

    HUOBI("huobi", "https://www.huobi.com"),

    KUCOIN("kucoin", "https://www.kucoin.com"),

    MEXC("mexc", "https://www.mexc.com"),

    POLONIEX("poloniex","https://poloniex.com");

    private final String value;

    private final String url;

    @Override
    public String toString() {
        return value;
    }

    public String getUrl() {
        return url;
    }
}