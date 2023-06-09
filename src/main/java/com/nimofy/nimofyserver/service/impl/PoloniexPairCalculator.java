package com.nimofy.nimofyserver.service.impl;

import com.nimofy.nimofyserver.dto.poloniex.PoloniexDTO;
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
public class PoloniexPairCalculator implements PairCalculator {

    @Value("${exchange.api.poloniex}")
    private String poloniexApiUrl;

    private final RestTemplate restTemplate;

    @Override
    public double calculatePrice(String symbol) {
        URI apiUrl = buildApiUrl(symbol);
        PoloniexDTO poloniexDTO = restTemplate.getForObject(apiUrl, PoloniexDTO.class);
        Objects.requireNonNull(poloniexDTO);
        System.out.println(poloniexDTO);
        return poloniexDTO.price();
    }

    private URI buildApiUrl(String symbol) {
        String[] arr = symbol.split("USDT");
        String newSymbol = arr[0] + "_USDT";
        String url = poloniexApiUrl + "/" + newSymbol + "/price";
        return UriComponentsBuilder.fromHttpUrl(url)
                .build()
                .toUri();
    }
}