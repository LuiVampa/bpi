package com.bpi.model;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by Grebenkov.Andrey on 14.10.2019
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Bpi {
    private String code;
    private String description;
    @JsonProperty("rate_float")
    private BigDecimal rate;
}
