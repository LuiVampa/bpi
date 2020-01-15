package com.bpi.dao;

import com.bpi.dto.CurrencyRateDto;
import com.bpi.model.BpiHistory;
import com.bpi.model.Currency;

/**
 * Created by Grebenkov.Andrey on 14.10.2019
 *
 * This interface provides methods for obtaining currency rate data.
 */
public interface BpiDao {

    /**
     * Returns the currencies which rate can be showed.
     *
     * @return an array with currency info (currency code and currency country).
     */
    Currency[] getSupportedCurrencies();

    /**
     * Returns the currency rates by code for last 30 days.
     *
     * @param currencyCode USD, EUR, etc.
     * @return the currency rate history.
     */
    BpiHistory getBpiHistory(String currencyCode);

    /**
     * Returns the currency rate by code.
     *
     * @param currencyCode USD, EUR, etc.
     * @return info about certain currency by code (current/lowest/highest rate, description and code).
     */
    CurrencyRateDto getRateByCurrency(String currencyCode);
}
