package org.swissQuant;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Config {
    public final String CURRENCY = "CHF";
    public final Double RATE = 1.14;
    public final int LIMIT = 10;
    public final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public Config() {
        decimalFormat.setRoundingMode(RoundingMode.UP);
    }
}
