package com.nimofy.nimofyserver.service.impl;

import com.nimofy.nimofyserver.dto.huobi.HuobiResponseDTO;
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
public class HuobiExchange implements Exchange {
    @Value("${exchange.api.huobi}")
    private String huobiApiUrl;
    private final RestTemplate restTemplate;

    @Override
    public double calculatePrice(String symbol) {
        URI apiUrl = buildApiUrl(symbol);
        HuobiResponseDTO huobiResponseDTO = restTemplate.getForObject(apiUrl, HuobiResponseDTO.class);
        Objects.requireNonNull(huobiResponseDTO);
        System.out.println(huobiResponseDTO);
        return huobiResponseDTO.tick().data().get(0).price();
    }

    private URI buildApiUrl(String symbol) {
        String[] arr = symbol.split("USDT");
        String newSymbol = arr[0] + "USDT";
        return UriComponentsBuilder.fromHttpUrl(huobiApiUrl)
                .queryParam("symbol", newSymbol.toLowerCase())
                .build()
                .toUri();
    }
}
