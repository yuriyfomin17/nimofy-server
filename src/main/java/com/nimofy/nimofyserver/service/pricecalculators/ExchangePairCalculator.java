package com.nimofy.nimofyserver.service.pricecalculators;

import com.nimofy.nimofyserver.dto.exchanges.Exchange;

public interface ExchangePairCalculator {

    double calculatePrice(String symbol);

    Exchange getExchange();

}