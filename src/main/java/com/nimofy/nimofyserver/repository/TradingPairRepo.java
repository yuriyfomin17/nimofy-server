package com.nimofy.nimofyserver.repository;

import com.nimofy.nimofyserver.model.TradingPair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradingPairRepo extends JpaRepository<TradingPair, Long> {}