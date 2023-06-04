package com.nimofy.nimofyserver.service.impl;

import com.nimofy.nimofyserver.dto.kucoin.KucoinResponseDTO;
import com.nimofy.nimofyserver.service.Exchange;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class KucoinExchange implements Exchange {
    @Value("${exchange.api.kucoin}")
    private String kucoinApiUrl;
    private final RestTemplate restTemplate;

    @Override
    public double calculatePrice(String symbol) {
        URI apiUrl = buildApiUrl(symbol);
        KucoinResponseDTO kucoinResponseDTO = restTemplate.getForObject(apiUrl, KucoinResponseDTO.class);
        Objects.requireNonNull(kucoinResponseDTO);
        System.out.println(kucoinResponseDTO);
        return kucoinResponseDTO.data().price();
    }

    private URI buildApiUrl(String symbol) {
        String[] arr = symbol.split("USDT");
        String newSymbol = arr[0] + "-USDT";
        return UriComponentsBuilder.fromHttpUrl(kucoinApiUrl)
                .queryParam("symbol", newSymbol)
                .build().toUri();
    }
}
