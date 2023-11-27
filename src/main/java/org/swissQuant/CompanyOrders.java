package org.swissQuant;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
//import java.text.DateFormat;
//import java.text.ParseException;
import java.util.*;

public class CompanyOrders {

    private static final Config COMPANY_CONFIG = new Config();

    private static Double getTotalValue(List<Batch> batchList) {
        return batchList.stream().reduce(0.0, (sub, batch) -> sub + batch.getBatchValueInUSD(), Double::sum) / COMPANY_CONFIG.RATE;
    }

    private static Integer getTotalQuantity(List<Batch> batchList) {
        return batchList.stream().reduce(0, (sub, batch) -> sub + batch.getQuantity(), Integer::sum);
    }

    public static void main(String[] args) {
        Map<String, List<Batch>> products = new HashMap<>();

        for (String filename : args) {
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String line;
                br.readLine();
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
//                    Date date = DateFormat.getDateInstance().parse(parts[0]);
                    String date = parts[0];
                    Integer quantity = Integer.parseInt(parts[1]);
                    String name = parts[2];
                    double price = Double.parseDouble(parts[3]);
                    String currency = parts[4];

                    Batch batch = new Batch(date, quantity, price, currency);
                    List<Batch> batchList;
                    if (products.containsKey(name)) {
                        batchList = products.get(name);
                    } else {
                        batchList = new ArrayList<>();
                    }
                    batchList.add(batch);

                    products.put(name, batchList);
                }
//            } catch (IOException | ParseException e) {
            } catch (IOException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        Map<String, List<Batch>> sortedProducts = new TreeMap<>((p1, p2) -> getTotalValue(products.get(p2)).compareTo(getTotalValue(products.get(p1))));
        sortedProducts.putAll(products);

        System.out.println("Product\t\tTotal Quantity\t\tCurrency\t\tValue");
        int count = 0;
        for (Map.Entry<String, List<Batch>> entry : sortedProducts.entrySet()) {
            if (count >= COMPANY_CONFIG.LIMIT) {
                break;
            }
            System.out.println(entry.getKey() + "\t\t" + getTotalQuantity(entry.getValue()) + "\t\t" + COMPANY_CONFIG.CURRENCY + "\t\t" + COMPANY_CONFIG.decimalFormat.format(getTotalValue(entry.getValue())));
            count++;
        }
    }

}