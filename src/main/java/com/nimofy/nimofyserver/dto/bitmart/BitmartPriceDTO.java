package com.nimofy.nimofyserver.dto.bitmart;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BitmartPriceDTO(@JsonProperty("best_ask") double bestAsk) {
}
