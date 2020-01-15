package com.bpi;

/**
 * Created by Grebenkov.Andrey on 14.10.2019
 */
public class Constants {
    public static final String LINE_SEPARATOR = System.lineSeparator();
    public static final String COMMAND_SEPARATOR = "--------------------------------------------------";
    public static final String HEADER =
            "Enter 'currencies' to view supported currencies." + LINE_SEPARATOR
                    + "Enter 'q' to quite" + LINE_SEPARATOR
                    + "Enter the currency code to find out its exchange rate (EUR, USD, etc.):";
    public static final String COMMAND_NOT_SUPPORTED_MSG = "This command is not supported - %s.";
    public static final String CURRENCY_NOT_SUPPORTED_MSG = "This currency is not supported - %s.";
    public static final String QUITE = "q";
    public static final String CURRENCIES = "currencies";
    public static final String NO_DATA_MSG = "Couldn't load data. See logs for more details.";
}
