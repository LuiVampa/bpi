package com.bpi.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

import static com.bpi.Constants.LINE_SEPARATOR;

/**
 * Created by Grebenkov.Andrey on 14.10.2019
 */
@Data
@Builder
public class CurrencyRateDto {
    BigDecimal currentRate;
    BigDecimal lowestRate;
    BigDecimal highestRate;
    String description;
    String code;

    @Override
    public String toString() {
        return "Code: " + code + LINE_SEPARATOR +
                "Description: " + description + LINE_SEPARATOR +
                "Current rate: " + currentRate + LINE_SEPARATOR +
                "Highest rate in the last 30 days: " + highestRate + LINE_SEPARATOR +
                "Lowest rate in the last 30 days: " + lowestRate;
    }
}
