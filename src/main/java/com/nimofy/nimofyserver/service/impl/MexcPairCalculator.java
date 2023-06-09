package com.nimofy.nimofyserver.service.impl;

import com.nimofy.nimofyserver.dto.mexc.MexcResponseDTO;
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
public class MexcPairCalculator implements PairCalculator {

    @Value("${exchange.api.mexc}")
    private String mexcApiUrl;

    private final RestTemplate restTemplate;

    @Override
    public double calculatePrice(String symbol) {
        URI apiUrl = buildApiUrl(symbol);
        MexcResponseDTO mexcResponseDTO = restTemplate.getForObject(apiUrl, MexcResponseDTO.class);
        System.out.println(mexcResponseDTO);
        Objects.requireNonNull(mexcResponseDTO);
        return mexcResponseDTO.price();
    }
    private URI buildApiUrl(String symbol) {
        return UriComponentsBuilder.fromHttpUrl(mexcApiUrl)
                .queryParam("symbol", symbol)
                .build().toUri();
    }
}