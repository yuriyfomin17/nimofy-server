package com.nimofy.nimofyserver.service.pricecalculators.paircalculatorImpl;

import com.nimofy.nimofyserver.dto.exchanges.Exchange;
import com.nimofy.nimofyserver.dto.exchanges.mexc.MexcResponseDTO;
import com.nimofy.nimofyserver.service.pricecalculators.ExchangePairCalculator;
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
public class MexcExchangePairCalculator implements ExchangePairCalculator {

    private  final Exchange exchange = Exchange.MEXC;

    @Value("${exchange.api.mexc}")
    private String mexcApiUrl;

    private final RestTemplate restTemplate;

    @Override
    public double calculatePrice(String symbol) {
        if (Objects.isNull(symbol)) return 0;
        try {
            URI apiUrl = buildApiUrl(symbol);
            MexcResponseDTO mexcResponseDTO = restTemplate.getForObject(apiUrl, MexcResponseDTO.class);
            Objects.requireNonNull(mexcResponseDTO);
            return mexcResponseDTO.price();
        } catch (Exception e){
            log.error("Exchange:{}, Symbol:{}, Message:{}", exchange, symbol, e.getMessage());
            return 0;
        }
    }
    private URI buildApiUrl(String symbol) {
        return UriComponentsBuilder.fromHttpUrl(mexcApiUrl)
                .queryParam("symbol", symbol)
                .build().toUri();
    }

    @Override
    public Exchange getExchange() {
        return exchange;
    }
}