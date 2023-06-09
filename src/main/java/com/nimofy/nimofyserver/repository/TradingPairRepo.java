package com.nimofy.nimofyserver.repository;

import com.nimofy.nimofyserver.model.TradingPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TradingPairRepo extends JpaRepository<TradingPair, Long> {
    @Query("select tp from TradingPair tp order by tp.id asc ")
    List<TradingPair> findAllOrderedById();
}