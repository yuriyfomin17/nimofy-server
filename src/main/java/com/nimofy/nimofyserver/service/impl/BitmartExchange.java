package com.nimofy.nimofyserver.service.impl;

import com.nimofy.nimofyserver.dto.bitmart.BitmartDTO;
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
public class BitmartExchange implements Exchange {
    @Value("${exchange.api.bitmart}")
    private String bitmartApiUrl;
    private final RestTemplate restTemplate;

    @Override
    public double calculatePrice(String symbol) {
        URI apiUrl = buildApiUrl(symbol);
        System.out.println(apiUrl);
        BitmartDTO bitmartDTO = restTemplate.getForObject(apiUrl, BitmartDTO.class);
        Objects.requireNonNull(bitmartDTO);
        System.out.println(bitmartDTO);
        return bitmartDTO.data().bestAsk();
    }

    private URI buildApiUrl(String symbol) {
        String[] arr = symbol.split("USDT");
        String newSymbol = arr[0] + "_USDT";
        return UriComponentsBuilder.fromHttpUrl(bitmartApiUrl)
                .queryParam("symbol", newSymbol)
                .build()
                .toUri();
    }
}
