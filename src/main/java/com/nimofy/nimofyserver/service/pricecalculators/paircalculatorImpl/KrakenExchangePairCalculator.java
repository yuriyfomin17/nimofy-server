package com.nimofy.nimofyserver.service.pricecalculators.paircalculatorImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.nimofy.nimofyserver.dto.exchanges.Exchange;
import com.nimofy.nimofyserver.service.pricecalculators.ExchangePairCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class KrakenExchangePairCalculator implements ExchangePairCalculator {

    private final Exchange exchange = Exchange.KRAKEN;


    @Value("${exchange.api.kraken}")
    private String krakenApiUrl;

    private final RestTemplate restTemplate;

    @Override
    public double calculatePrice(String symbol) {
        if (Objects.isNull(symbol)) return 0;
        try {

            URI apiUrl = buildApiUrl(symbol);
            JsonNode response = restTemplate.getForObject(apiUrl, JsonNode.class);
            List<Double> prices = getPrice(symbol, response);
            return prices.get(0);
        } catch (Exception e) {
            log.error("Exchange:{}, Symbol:{}, Message:{}", exchange, symbol, e.getMessage());
            return 0;
        }
    }

    private static List<Double> getPrice(String symbol, JsonNode response) {
        Objects.requireNonNull(response);
        var priceNode = response.get("result").get(symbol).get("c");
        return StreamSupport.stream(priceNode.spliterator(), false)
                .map(JsonNode::asDouble)
                .toList();
    }

    private URI buildApiUrl(String symbol) {
        return UriComponentsBuilder.fromHttpUrl(krakenApiUrl)
                .queryParam("pair", symbol)
                .build().toUri();
    }

    @Override
    public Exchange getExchange() {
        return exchange;
    }
}