package com.bpi.dao.impl;


import com.bpi.dto.CurrencyRateDto;
import com.bpi.model.BpiHistory;
import com.bpi.model.Currency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * Created by Grebenkov.Andrey on 14.10.2019
 */
public class CoinDeskBpiDaoTest {
    private static final String USD = "USD";
    private static final Integer RATE_PERIOD = 31;
    private final CoinDeskBpiDao bpiDao = new CoinDeskBpiDao();

    @Test
    void getSupportedCurrenciesTest() {
        final Currency currency = new Currency();
        currency.setCurrency(USD);
        currency.setCountry("United States Dollar");

        final Currency[] supportedCurrencies = bpiDao.getSupportedCurrencies();

        Assertions.assertTrue(supportedCurrencies.length > 0);
        Assertions.assertTrue(Arrays.asList(supportedCurrencies).contains(currency));
    }

    @Test
    void getBpiHistoryTest() {
        final BpiHistory bpiHistory = bpiDao.getBpiHistory(USD);
        Assertions.assertEquals(RATE_PERIOD, bpiHistory.getBpi().size());
    }

    @Test
    void getRateByCurrencyTest() {
        final CurrencyRateDto usd = bpiDao.getRateByCurrency(USD);
        Assertions.assertFalse(usd.toString().isEmpty());
    }
}