package com.nimofy.nimofyserver.service.pricecalculators.paircalculatorImpl;

import com.nimofy.nimofyserver.dto.exchanges.Exchange;
import com.nimofy.nimofyserver.dto.exchanges.bittrex.BittrexDTO;
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
public class BittrexExchangePairCalculator implements ExchangePairCalculator {

    private final Exchange exchange = Exchange.BITTREX;

    @Value("${exchange.api.bittrex}")
    private String bittrexApiUrl;

    private final RestTemplate restTemplate;

    @Override
    public double calculatePrice(String symbol) {
        if (Objects.isNull(symbol)) return 0;
        try {
            URI apiUrl = buildApiUrl(symbol);
            BittrexDTO bittrexDTO = restTemplate.getForObject(apiUrl, BittrexDTO.class);
            Objects.requireNonNull(bittrexDTO);
            return bittrexDTO.lastTradeRate();
        } catch (Exception e){
            log.error("Exchange:{}, Symbol:{}, Message:{}", exchange, symbol, e.getMessage());
            return 0;
        }
    }
    private URI buildApiUrl(String symbol){
        String url = bittrexApiUrl + "/" + symbol + "/ticker";
        return UriComponentsBuilder.fromHttpUrl(url)
                .build()
                .toUri();
    }

    @Override
    public Exchange getExchange() {
        return exchange;
    }
}