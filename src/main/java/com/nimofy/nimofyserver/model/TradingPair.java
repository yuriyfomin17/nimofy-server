package com.nimofy.nimofyserver.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "trading_pairs")
public class TradingPair {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true, name = "binance_trading_pair_name")
    private String binanceTradingPairName;

    @Column(unique = true, name = "bitfinex_trading_pair_name")
    private String bitfinexTradingPairName;

    @Column(unique = true, name = "bitmart_trading_pair_name")
    private String bitmartTradingPairName;

    @Column(unique = true, name = "bittrex_trading_pair_name")
    private String bittrexTradingPairName;

    @Column(unique = true, name = "bybit_trading_pair_name")
    private String bybitTradingPairName;

    @Column(unique = true, name = "huobi_trading_pair_name")
    private String huobiTradingPairName;

    @Column(unique = true, name = "kucoin_trading_pair_name")
    private String kucoinTradingPairName;

    @Column(unique = true, name = "mexc_trading_pair_name")
    private String mexcTradingPairName;

    @Column(unique = true, name = "poloniex_trading_pair_name")
    private String poloniexTradingPairName;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TradingPair tradingPair)) return false;
        return id != null && id.equals(tradingPair.getId());
    }

    public TradingPair() {}
}