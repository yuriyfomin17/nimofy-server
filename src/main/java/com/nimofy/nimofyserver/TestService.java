package com.nimofy.nimofyserver;

import com.nimofy.nimofyserver.dto.ArbitrageWindow;
import com.nimofy.nimofyserver.service.arbitragecalulators.ArbitrageCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestService {
    private final ArbitrageCalculator arbitrageCalculator;

    @EventListener(ContextRefreshedEvent.class)
    public void testMethod(){
        List<ArbitrageWindow> arbitrageWindows = arbitrageCalculator.calculateArbitrageWindow();
        log.info("[ARBITRAGE_WINDOWS] -> {}", arbitrageWindows);
    }
}