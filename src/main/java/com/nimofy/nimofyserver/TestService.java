package com.nimofy.nimofybot;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TestService {
    private final RestTemplate restTemplate;

    @EventListener(ContextRefreshedEvent.class)
    public void printAllPairs(){
        String url = "https://api.binance.com/api/v3/exchangeInfo";
        ExchangePairs exchangePairs = restTemplate.getForObject(url, ExchangePairs.class);
        Objects.requireNonNull(exchangePairs);
        List<TradingPair> tradingPairs = exchangePairs.symbols().stream()
                .filter(tradingPair -> tradingPair.symbol().contains("USDT") && tradingPair.status().equals("TRADING") && tradingPair.symbol().contains("RVN"))
                .toList();
        System.out.println(tradingPairs);
        System.out.println("List size:" + tradingPairs.size());
    }
}