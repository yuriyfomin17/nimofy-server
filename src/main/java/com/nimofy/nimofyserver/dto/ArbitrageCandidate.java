package com.nimofy.nimofyserver.dto;

import com.nimofy.nimofyserver.dto.exchanges.Exchange;
import lombok.Builder;

@Builder
public record ArbitrageCandidate(

        Exchange exchange,

        String tradingPair,

        double price
) {
    @Override
    public String toString() {
        return "{" +
                "exchange=" + exchange +
                ", tradingPair='" + tradingPair + '\'' +
                ", price=" + price +
                '}';
    }
}