package com.bpi.model;

import lombok.Data;

/**
 * Created by Grebenkov.Andrey on 14.10.2019
 */
@Data
public class Currency {
    private String currency;
    private String country;

    @Override
    public String toString() {
        return "Currency: " + currency + ", Country: " + country;
    }
}
