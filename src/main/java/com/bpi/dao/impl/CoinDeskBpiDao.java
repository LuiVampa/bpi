package com.bpi.dao.impl;

import com.bpi.dao.BpiDao;
import com.bpi.dto.BpiThresholdsDto;
import com.bpi.dto.CurrencyRateDto;
import com.bpi.model.BpiHistory;
import com.bpi.model.Currency;
import com.bpi.model.CurrencyRate;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Grebenkov.Andrey on 14.10.2019
 */
@Slf4j
public class CoinDeskBpiDao implements BpiDao {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String BPI_HISTORY_URL = "https://api.coindesk.com/v1/bpi/historical/close.json?currency=%s";
    private static final String CURRENT_PRICE_BY_CODE_URL = "https://api.coindesk.com/v1/bpi/currentprice/%s.json";
    private static final String SUPPORTED_CURRENCIES_URL = "https://api.coindesk.com/v1/bpi/supported-currencies.json";

    private static LocalDate currentDate = LocalDate.now();
    private Map<String, BpiThresholdsDto> bpiHistory;
    private List<String> supportedCurrencies;

    {
        bpiHistory = new HashMap<>();
        supportedCurrencies = Arrays.stream(getSupportedCurrencies())
                                    .map(Currency::getCurrency)
                                    .collect(Collectors.toList());
    }

    @Override
    public Currency[] getSupportedCurrencies() {
        return getBpiInfo(SUPPORTED_CURRENCIES_URL, Currency[].class);
    }

    @Override
    public BpiHistory getBpiHistory(String currencyCode) {
        return supportedCurrencies.contains(currencyCode)
                ? getBpiInfo(String.format(BPI_HISTORY_URL, currencyCode), BpiHistory.class)
                : null;
    }

    @Override
    public CurrencyRateDto getRateByCurrency(String currencyCode) {
        if (!supportedCurrencies.contains(currencyCode)) {
            return null;
        }
        BpiThresholdsDto thresholds = Optional.ofNullable(getCachedHistory(currencyCode))
                                              .orElse(loadThresholds(currencyCode));
        if (thresholds == null) {
            return null;
        }
        return Optional.ofNullable(getBpiInfo(
                String.format(CURRENT_PRICE_BY_CODE_URL, currencyCode),
                CurrencyRate.class
        ))
                       .map(CurrencyRate::getBpi)
                       .map(bpiInfo -> bpiInfo.get(currencyCode))
                       .map(bpi -> CurrencyRateDto.builder()
                                                  .code(bpi.getCode())
                                                  .description(bpi.getDescription())
                                                  .currentRate(bpi.getRate())
                                                  .lowestRate(thresholds.getLowestRate())
                                                  .highestRate(thresholds.getHighestRate())
                                                  .build())
                       .orElse(null);
    }

    private BpiThresholdsDto getCachedHistory(String currencyCode) {
        final LocalDate now = LocalDate.now();
        if (now.isAfter(currentDate)) {
            currentDate = now;
            bpiHistory = new HashMap<>();
        }
        return bpiHistory.get(currencyCode);
    }

    private BpiThresholdsDto loadThresholds(String currencyCode) {
        final BpiThresholdsDto bpiThresholdsDto = createBpiThresholdsDto(currencyCode);
        if (bpiThresholdsDto == null) {
            return null;
        }
        bpiHistory.put(currencyCode, bpiThresholdsDto);
        return bpiThresholdsDto;
    }

    private BpiThresholdsDto createBpiThresholdsDto(String currencyCode) {
        final BpiHistory bpiHistoryByCode = getBpiHistory(currencyCode);
        if (bpiHistoryByCode == null) {
            return null;
        }
        final BpiThresholdsDto bpiThresholdsDto = new BpiThresholdsDto();
        bpiThresholdsDto.setHighestRate(bpiHistoryByCode.getBpi()
                                                        .values()
                                                        .stream()
                                                        .max((BigDecimal::compareTo))
                                                        .orElse(null));
        bpiThresholdsDto.setLowestRate(bpiHistoryByCode.getBpi()
                                                       .values()
                                                       .stream()
                                                       .min((BigDecimal::compareTo))
                                                       .orElse(null));
        return bpiThresholdsDto;
    }

    private <T> T getBpiInfo(String requestUrl, Class<T> clazz) {
        final URLConnection connection = createConnection(requestUrl);
        final String jsonStr = getJson(connection);
        return getDataFromJson(jsonStr, clazz);
    }

    private <T> T getDataFromJson(String jsonStr, Class<T> clazz) {
        if (jsonStr == null) {
            return null;
        }
        T result = null;

        try {
            result = OBJECT_MAPPER.readValue(jsonStr, clazz);
        } catch (IOException e) {
            log.error("Can't read value from json string - {}", jsonStr, e);
        }
        return result;
    }


    private URLConnection createConnection(String requestUrl) {
        URLConnection connection = null;
        try {
            URL url = new URL(requestUrl);
            connection = url.openConnection();
        } catch (MalformedURLException e) {
            log.error("Malformed URL has occurred - {}", requestUrl, e);
        } catch (IOException e) {
            log.error("Unable to establish connection by url - {}", requestUrl, e);
        }
        return connection;
    }

    private String getJson(URLConnection connection) {
        if (connection == null) {
            return null;
        }
        String json = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            json = br.lines().collect(Collectors.joining());
        } catch (IOException e) {
            log.error("Unable to establish connection - {}", connection.getURL().getUserInfo(), e);
        }
        return json;
    }
}