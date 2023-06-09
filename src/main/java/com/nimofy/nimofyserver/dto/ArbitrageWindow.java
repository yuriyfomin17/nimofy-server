package com.nimofy.nimofyserver.dto;

import com.nimofy.nimofyserver.dto.exchanges.Exchange;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ArbitrageWindow(
        Exchange buyExchange,

        Exchange sellExchange,

        String tradingPair,

        double percentage,

        LocalDateTime createdAt

) {
}