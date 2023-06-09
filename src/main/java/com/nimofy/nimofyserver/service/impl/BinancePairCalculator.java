package com.nimofy.nimofyserver.service.impl;

import com.nimofy.nimofyserver.dto.binance.BinanceResponseDTO;
import com.nimofy.nimofyserver.service.PairCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class BinancePairCalculator implements PairCalculator {

    @Value("${exchange.api.binance}")
    private String binanceApiUrl;

    private final RestTemplate restTemplate;

    @Override
    public double calculatePrice(String symbol) {
        URI apiUrl = buildApiUrl(symbol);
        BinanceResponseDTO binanceResponseDTO = restTemplate.getForObject(apiUrl, BinanceResponseDTO.class);
        Objects.requireNonNull(binanceResponseDTO);
        log.info("Binance Response {}", binanceResponseDTO);
        return binanceResponseDTO.price();
    }

    private URI buildApiUrl(String symbol) {
        return UriComponentsBuilder.fromHttpUrl(binanceApiUrl)
                .queryParam("symbol", symbol)
                .build().toUri();
    }
}