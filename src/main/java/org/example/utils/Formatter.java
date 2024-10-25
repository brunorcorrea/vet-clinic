package org.example.utils;

import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;

public class Formatter {

    private Formatter() {
    }

    public static NumberFormatter createDecimalNumberFormatter() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);

        NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
        numberFormatter.setValueClass(Double.class);
        numberFormatter.setAllowsInvalid(true);
        numberFormatter.setMinimum(0.0);
        return numberFormatter;
    }

    public static NumberFormatter createIntegerNumberFormatter() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);

        NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
        numberFormatter.setAllowsInvalid(true);
        numberFormatter.setMinimum(0);
        return numberFormatter;
    }
}
