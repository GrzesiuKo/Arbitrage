package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Graph {
    Map<String, Currency> currencies;
    Currency root;

    public Graph() {
        currencies = new HashMap<>();
    }

    public boolean hasCurrency(String shortName) {
        return currencies.containsKey(shortName);
    }

    public void addCurrency(String shortName, Currency currency) {
        currencies.put(shortName, currency);
    }

    public Currency getCurrency(String key) {
        return currencies.get(key);
    }

    public void connectOfferWithCurrency(Offer offer, String currencyShortName) {
        Currency currency;

        if (hasCurrency(currencyShortName)) {
            currency = getCurrency(currencyShortName);
        } else {
            return;
        }

        if (currency.hasOffer(offer)) {
            return;
        } else {
            currency.addOffer(offer);
        }
    }

    public ArrayList<Currency> toArrayList() {
        ArrayList<Currency> values;

        if (currencies != null) {
            values = new ArrayList<>();
        } else {
            return null;
        }

        for (String s : currencies.keySet()) {
            values.add(currencies.get(s));
        }

        return values;
    }

    public Currency getRoot() {
        return root;
    }

    public void setRoot(Currency root) {
        this.root = root;
    }

    public void prepareForNextPathFinding() {
        ArrayList<Currency> currencies;

        currencies = toArrayList();

        for (Currency c : currencies) {
            c.setExchangedMoney(-1);
            c.setVisited(new ArrayList<>());
        }
    }

}
