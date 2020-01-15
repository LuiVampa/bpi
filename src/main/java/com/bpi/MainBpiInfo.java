package com.bpi;

import com.bpi.dao.BpiDao;
import com.bpi.dao.impl.CoinDeskBpiDao;
import com.bpi.dto.CurrencyRateDto;
import com.bpi.model.Currency;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

import static com.bpi.Constants.COMMAND_NOT_SUPPORTED_MSG;
import static com.bpi.Constants.COMMAND_SEPARATOR;
import static com.bpi.Constants.CURRENCIES;
import static com.bpi.Constants.CURRENCY_NOT_SUPPORTED_MSG;
import static com.bpi.Constants.HEADER;
import static com.bpi.Constants.LINE_SEPARATOR;
import static com.bpi.Constants.NO_DATA_MSG;
import static com.bpi.Constants.QUITE;

/**
 * Created by Grebenkov.Andrey on 14.10.2019
 */
public class MainBpiInfo {

    private static BpiDao BPI_DAO = new CoinDeskBpiDao();

    public static void main(String[] args) {
        printHeader();
        Scanner sc = new Scanner(System.in);
        String requestLine = sc.nextLine();

        while (!QUITE.equals(requestLine)) {
            String result;
            if (isCorrectRequestLine(requestLine)) {
                if ("currencies".equals(requestLine.toLowerCase())) {
                    System.out.println("Supported currencies:");
                    result = Arrays.stream(BPI_DAO.getSupportedCurrencies())
                                   .map(Currency::toString)
                                   .collect(Collectors.joining(LINE_SEPARATOR));
                } else {
                    final CurrencyRateDto rateByCurrency = BPI_DAO.getRateByCurrency(requestLine.toUpperCase());
                    result = rateByCurrency == null
                            ? String.format(CURRENCY_NOT_SUPPORTED_MSG, requestLine)
                            : rateByCurrency.toString();
                }
            } else {
                result = String.format(COMMAND_NOT_SUPPORTED_MSG, requestLine);
            }
            showResult(result);
            printHeader();
            requestLine = sc.nextLine();
        }
        sc.close();
    }

    private static void showResult(String result) {
        if (result == null) {
            System.out.println(NO_DATA_MSG);
        } else {
            System.out.println(result);
        }
    }

    private static boolean isCorrectRequestLine(String requestLine) {
        return requestLine != null &&
                (CURRENCIES.equalsIgnoreCase(requestLine) || requestLine.length() == 3);
    }

    private static void printHeader() {
        System.out.println(COMMAND_SEPARATOR);
        System.out.println(HEADER);
    }
}