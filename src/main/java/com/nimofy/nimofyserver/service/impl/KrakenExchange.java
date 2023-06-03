package com.nimofy.nimofyserver.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.nimofy.nimofyserver.service.Exchange;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class KrakenExchange implements Exchange {
    @Value("${exchange.api.kraken}")
    private String krakenApiUrl;
    private final RestTemplate restTemplate;
    @Override
    public double calculatePrice(String symbol) {
        URI apiUrl = buildApiUrl(symbol);
        JsonNode response = restTemplate.getForObject(apiUrl, JsonNode.class);
        String newSymbol = symbol.equals("BTCUSDT") ? "XBTUSDT": symbol;
        System.out.println(response);
        Objects.requireNonNull(response);
        List<JsonNode> doubles = StreamSupport.stream(response.spliterator(), false)
                .map(node -> node.get("result"))
                .toList();
        System.out.println(doubles);
        return 0.0;
    }
    private URI buildApiUrl(String symbol) {
        return UriComponentsBuilder.fromHttpUrl(krakenApiUrl)
                .queryParam("pair", symbol)
                .build().toUri();
    }
}
