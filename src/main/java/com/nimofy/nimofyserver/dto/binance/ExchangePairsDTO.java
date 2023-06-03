package com.nimofy.nimofyserver.dto.binance;

import java.util.List;

public record ExchangePairsDTO(List<TradingPairDTO> symbols) {}