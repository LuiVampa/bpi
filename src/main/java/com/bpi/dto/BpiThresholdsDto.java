package com.bpi.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Grebenkov.Andrey on 14.10.2019
 */
@Data
public class BpiThresholdsDto {
    BigDecimal lowestRate;
    BigDecimal highestRate;
}
