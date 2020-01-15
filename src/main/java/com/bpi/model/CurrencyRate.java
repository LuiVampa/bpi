package com.bpi.model;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Map;

/**
 * Created by Grebenkov.Andrey on 14.10.2019
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyRate {
    private Map<String, Bpi> bpi;
}
