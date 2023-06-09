package com.nimofy.nimofyserver.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.nimofy.nimofyserver.service.PairCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class KrakenPairCalculator implements PairCalculator {

    @Value("${exchange.api.kraken}")
    private String krakenApiUrl;

    private final RestTemplate restTemplate;

    @Override
    public double calculatePrice(String symbol) {
        URI apiUrl = buildApiUrl(symbol);
        JsonNode response = restTemplate.getForObject(apiUrl, JsonNode.class);
        List<Double> prices = getPrice(symbol, response);
        return prices.get(0);
    }

    private static List<Double> getPrice(String symbol, JsonNode response) {
        String newSymbol = symbol.equals("BTCUSDT") ? "XBTUSDT" : symbol;
        Objects.requireNonNull(response);
        System.out.println(response);
        var priceNode = response.get("result").get(newSymbol).get("c");
        return StreamSupport.stream(priceNode.spliterator(), false)
                .map(JsonNode::asDouble)
                .toList();
    }

    private URI buildApiUrl(String symbol) {
        return UriComponentsBuilder.fromHttpUrl(krakenApiUrl)
                .queryParam("pair", symbol)
                .build().toUri();
    }
}