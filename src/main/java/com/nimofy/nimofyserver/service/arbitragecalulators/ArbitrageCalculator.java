package com.nimofy.nimofyserver.service.arbitragecalulators;

import com.nimofy.nimofyserver.dto.ArbitrageCandidate;
import com.nimofy.nimofyserver.dto.ArbitrageWindow;
import com.nimofy.nimofyserver.dto.exchanges.Exchange;
import com.nimofy.nimofyserver.model.TradingPair;
import com.nimofy.nimofyserver.repository.TradingPairRepo;
import com.nimofy.nimofyserver.service.pricecalculators.ExchangePairCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArbitrageCalculator {

    private final TradingPairRepo tradingPairRepo;

    private final List<ExchangePairCalculator> exchangePairCalculators;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;


    public List<ArbitrageWindow> calculateArbitrageWindow() {
        List<TradingPair> tradingPairs = tradingPairRepo.findAllOrderedById();
        return tradingPairs.stream()
                .map(this::handleTradingPair)
                .filter(Objects::nonNull)
                .toList();
    }

    private ArbitrageWindow handleTradingPair(TradingPair tradingPair) {
        List<CompletableFuture<ArbitrageCandidate>> completableFutures = exchangePairCalculators.stream()
                .map(exchangePairCalculator -> CompletableFuture.supplyAsync(() -> handleExchangePairCalculator(tradingPair, exchangePairCalculator), threadPoolTaskExecutor))
                .toList();
        List<ArbitrageCandidate> arbitrageCandidateList = completableFutures.stream().map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(ArbitrageCandidate::price, Double::compare))
                .toList();
        return getArbitrageWindow(tradingPair, arbitrageCandidateList);
    }

    private ArbitrageCandidate handleExchangePairCalculator(TradingPair tradingPair, ExchangePairCalculator exchangePairCalculator) {
        try {
            ArbitrageCandidate arbitrageCandidate = getArbitrageCandidate(exchangePairCalculator, tradingPair);
            if (arbitrageCandidate.price() > 0) {
                return arbitrageCandidate;
            }
            return null;
        } catch (Exception e) {
            log.error("Exchange:{}, Pair:{}, Error:{} ", exchangePairCalculator.getExchange().toString().toUpperCase(), tradingPair.getBinanceTradingPairName(), e.getMessage());
            return null;
        }
    }

    private ArbitrageWindow getArbitrageWindow(TradingPair tradingPair, List<ArbitrageCandidate> validCandidates) {
        if (validCandidates.size() > 1) {
            ArbitrageCandidate lowerValueArbitrageCandidate = validCandidates.get(0);
            ArbitrageCandidate higherValueArbitrageCandidate = validCandidates.get(validCandidates.size() - 1);
            double lowerPrice = lowerValueArbitrageCandidate.price();
            double higherPrice = higherValueArbitrageCandidate.price();
            double percentage = (higherPrice - lowerPrice) * 100 / lowerPrice;

            BigDecimal roundedNumber = new BigDecimal(percentage).setScale(2, RoundingMode.HALF_UP);
            double roundedPercentage = roundedNumber.doubleValue();

            if (percentage >= 0.5) {
                log.info("Arbitrage Detected ✅, TradingPair:{}, Percentage:{}%, Buy Exchange:{}, SellExchange:{}",
                        tradingPair.getBinanceTradingPairName(),
                        roundedPercentage,
                        lowerValueArbitrageCandidate.exchange(),
                        higherValueArbitrageCandidate.exchange()
                );
                return ArbitrageWindow.builder()
                        .percentage(roundedPercentage)
                        .buyExchange(lowerValueArbitrageCandidate.exchange())
                        .sellExchange(higherValueArbitrageCandidate.exchange())
                        .tradingPair(tradingPair.getBinanceTradingPairName())
                        .build();
            }
        }
        log.info("Trading Pair {} ❌", tradingPair.getBinanceTradingPairName());
        return null;
    }

    private ArbitrageCandidate getArbitrageCandidate(ExchangePairCalculator exchangePairCalculator, TradingPair tradingPair) {
        Exchange exchange = exchangePairCalculator.getExchange();
        String unifiedSymbol = tradingPair.getBinanceTradingPairName();
        return switch (exchange) {
            case BINANCE ->
                    new ArbitrageCandidate(Exchange.BINANCE, unifiedSymbol, exchangePairCalculator.calculatePrice(tradingPair.getBinanceTradingPairName()));
            case BITFINEX ->
                    new ArbitrageCandidate(Exchange.BITFINEX, unifiedSymbol, exchangePairCalculator.calculatePrice(tradingPair.getBitfinexTradingPairName()));
            case BITMART ->
                    new ArbitrageCandidate(Exchange.BITMART, unifiedSymbol, exchangePairCalculator.calculatePrice(tradingPair.getBitmartTradingPairName()));
            case BITTREX ->
                    new ArbitrageCandidate(Exchange.BITTREX, unifiedSymbol, exchangePairCalculator.calculatePrice(tradingPair.getBittrexTradingPairName()));
            case BYBIT ->
                    new ArbitrageCandidate(Exchange.BYBIT, unifiedSymbol, exchangePairCalculator.calculatePrice(tradingPair.getBybitTradingPairName()));
            case KRAKEN ->
                    new ArbitrageCandidate(Exchange.KRAKEN, unifiedSymbol, exchangePairCalculator.calculatePrice(tradingPair.getKrakenTradingPairName()));
            case HUOBI ->
                    new ArbitrageCandidate(Exchange.HUOBI, unifiedSymbol, exchangePairCalculator.calculatePrice(tradingPair.getHuobiTradingPairName()));
            case KUCOIN ->
                    new ArbitrageCandidate(Exchange.KUCOIN, unifiedSymbol, exchangePairCalculator.calculatePrice(tradingPair.getKucoinTradingPairName()));
            case MEXC ->
                    new ArbitrageCandidate(Exchange.MEXC, unifiedSymbol, exchangePairCalculator.calculatePrice(tradingPair.getMexcTradingPairName()));
            case POLONIEX ->
                    new ArbitrageCandidate(Exchange.POLONIEX, unifiedSymbol, exchangePairCalculator.calculatePrice(tradingPair.getPoloniexTradingPairName()));
        };
    }
}