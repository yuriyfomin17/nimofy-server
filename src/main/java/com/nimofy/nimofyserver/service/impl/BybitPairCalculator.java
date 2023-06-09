package com.nimofy.nimofyserver.service.impl;

import com.nimofy.nimofyserver.dto.bybit.BybitResponseDTO;
import com.nimofy.nimofyserver.service.PairCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BybitPairCalculator implements PairCalculator {

    @Value("${exchange.api.bybit}")
    private String bybitApiUrl;

    private final RestTemplate restTemplate;

    @Override
    public double calculatePrice(String symbol) {
        URI apiUrl = buildApiUrl(symbol);
        BybitResponseDTO bybitResponse = restTemplate.getForObject(apiUrl, BybitResponseDTO.class);
        Objects.requireNonNull(bybitResponse);
        System.out.println(bybitResponse);
        return bybitResponse.result().list().get(0).indexPrice();
    }

    private URI buildApiUrl(String symbol) {
        return UriComponentsBuilder.fromHttpUrl(bybitApiUrl)
                .queryParam("symbol", symbol)
                .build().toUri();
    }
}