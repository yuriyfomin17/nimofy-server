package com.nimofy.nimofyserver.service.impl;

import com.nimofy.nimofyserver.dto.bitfinex.BitfinexDTO;
import com.nimofy.nimofyserver.service.Exchange;
import lombok.RequiredArgsConstructor;
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
public class BitfinexExchange implements Exchange {

    @Value("${exchange.api.bitfinex}")
    private String bitfinexApiUrl;
    private final RestTemplate restTemplate;
    @Override
    public double calculatePrice(String symbol) {
        URI apiUrl = buildApiUrl(symbol);
        System.out.println(apiUrl);
        ParameterizedTypeReference<List<Double>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<Double>> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, null, responseType);
        List<Double> pairInfo = responseEntity.getBody();
        System.out.println(pairInfo);
        Objects.requireNonNull(pairInfo);
        return pairInfo.get(6);
    }

    private URI buildApiUrl(String symbol){
        String[] arr = symbol.split("USDT");
        String url = bitfinexApiUrl + "t"  + arr[0] + "USD";
        return UriComponentsBuilder.fromHttpUrl(url)
                .build()
                .toUri();
    }
}
