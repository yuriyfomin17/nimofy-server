package com.nimofy.nimofyserver;

import com.nimofy.nimofyserver.service.impl.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

//    private final RestTemplate restTemplate;
//    private final TradingPairRepo tradingPairRepo;
    private final BinanceExchange binanceExchange;
    private final BybitExchange bybitExchange;
    private final PoloniexExchange poloniexExchange;
    private final BittrexExchange bittrexExchange;
    private final BitmartExchange bitmartExchange;
    private final BitfinexExchange bitfinexExchange;
    private final KrakenExchange krakenExchange;
    private final KucoinExchange kucoinExchange;
    private final HuobiExchange huobiExchange;

    private final MexcExchange mexcExchange;

    @EventListener(ContextRefreshedEvent.class)
    public void printAllPairs() {
        binanceExchange.calculatePrice("BTCUSDT");
        bybitExchange.calculatePrice("BTCUSDT");
        poloniexExchange.calculatePrice("BTCUSDT");
        bittrexExchange.calculatePrice("BTCUSDT");
        bitmartExchange.calculatePrice("BTCUSDT");
        bitfinexExchange.calculatePrice("BTCUSDT");
        krakenExchange.calculatePrice("BTCUSDT");
        kucoinExchange.calculatePrice("BTCUSDT");
        huobiExchange.calculatePrice("BTCUSDT");
        mexcExchange.calculatePrice("BTCUSDT");

//        String url = "https://api.binance.com/api/v3/exchangeInfo";
//        ExchangePairsDTO exchangePairsDTO = restTemplate.getForObject(url, ExchangePairsDTO.class);
//        Objects.requireNonNull(exchangePairsDTO);
//        List<TradingPairDTO> tradingPairDTOS = exchangePairsDTO.symbols().stream()
//                .filter(tradingPairDTO -> tradingPairDTO.symbol().contains("USDT") && tradingPairDTO.status().equals("TRADING"))
//                .toList();
//        List<TradingPair> tradingPairList = tradingPairDTOS.stream()
//                .map(this::getTradingPair)
//                .toList();
//        tradingPairRepo.saveAll(tradingPairList);

    }

//    private TradingPair getTradingPair(TradingPairDTO tradingPairDTO) {
//        TradingPair tradingPair = new TradingPair();
//        tradingPair.setTradingPairName(tradingPairDTO.symbol());
//        return tradingPair;
//    }
}