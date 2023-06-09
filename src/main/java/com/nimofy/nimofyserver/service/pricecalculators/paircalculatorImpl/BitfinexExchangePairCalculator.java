package com.nimofy.nimofyserver.service.pricecalculators.paircalculatorImpl;

import com.nimofy.nimofyserver.dto.exchanges.Exchange;
import com.nimofy.nimofyserver.service.pricecalculators.ExchangePairCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class BitfinexExchangePairCalculator implements ExchangePairCalculator {

    private final Exchange exchange = Exchange.BITFINEX;

    @Value("${exchange.api.bitfinex}")
    private String bitfinexApiUrl;

    private final RestTemplate restTemplate;

    @Override
    public double calculatePrice(String symbol) {
        if (Objects.isNull(symbol)) return 0;
        try {
            URI apiUrl = buildApiUrl(symbol);
            ParameterizedTypeReference<List<Double>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<List<Double>> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, null, responseType);
            List<Double> pairInfo = responseEntity.getBody();
            Objects.requireNonNull(pairInfo);
            return pairInfo.get(6);
        } catch (Exception e) {
            log.error("Exchange:{}, Symbol:{}, Message:{}", exchange, symbol, e.getMessage());
            return 0;
        }
    }

    private URI buildApiUrl(String symbol) {
        String url = bitfinexApiUrl + symbol;
        return UriComponentsBuilder.fromHttpUrl(url)
                .build()
                .toUri();
    }

    @Override
    public Exchange getExchange() {
        return exchange;
    }
}