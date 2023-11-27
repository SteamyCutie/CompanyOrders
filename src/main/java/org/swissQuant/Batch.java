package org.swissQuant;

//import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Batch {

//    private Date date;
    private String date;
    private Integer quantity;
    private Double price;
    private String currency;

    public Double getBatchValueInUSD() {
        Double rate;
        if (currency.equals("EUR")) {
            rate = 1.09;
        } else if (currency.equals("CHF")) {
            rate = 1.14;
        } else {
            rate = 1.0;
        }
        return quantity * price * rate;
    }
}
