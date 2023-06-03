package com.nimofy.nimofyserver.service.impl;

import com.nimofy.nimofyserver.dto.bittrex.BittrexDTO;
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
public class BittrexExchange implements Exchange {
    @Value("${exchange.api.bittrex}")
    private String bittrexApiUrl;
    private final RestTemplate restTemplate;
    @Override
    public double calculatePrice(String symbol) {
        URI apiUrl = buildApiUrl(symbol);
        BittrexDTO bittrexDTO = restTemplate.getForObject(apiUrl, BittrexDTO.class);
        Objects.requireNonNull(bittrexDTO);
        System.out.println(bittrexDTO);
        return bittrexDTO.lastTradeRate();
    }
    private URI buildApiUrl(String symbol){
        String[] arr = symbol.split("USDT");
        String newSymbol = arr[0] + "-USDT";
        String url = bittrexApiUrl + "/" + newSymbol + "/ticker";
        return UriComponentsBuilder.fromHttpUrl(url)
                .build()
                .toUri();
    }
}
