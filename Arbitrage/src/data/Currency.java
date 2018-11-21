package data;

import analyzing.Path;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Currency {
    private String shortName;
    private String fullName;

    private double exchangedMoney;
    private ArrayList<Offer> exchanges;
    private ArrayList<Currency> visited;

    public Currency(String shortName, String fullName) {
        this.shortName = shortName;
        this.fullName = fullName;
        exchangedMoney = -1;
        exchanges = new ArrayList<>();
        visited = new ArrayList<>();
    }

    public boolean hasOffer(Offer offer) {
        boolean result;

        if (offer == null) {
            return false;
        } else {
            result = false;
        }

        for (Offer o : exchanges) {
            result = offer.getCurrency().equals(o.getCurrency());
            if (result == true){
                return true;
            }
        }
        return result;
    }

    public boolean hasVisited(Currency currency) {
        boolean visited;

        if (currency == null) {
            return false;
        } else {
            visited = false;
        }

        for (Currency c : this.visited) {
            visited = currency.equals(c);
            if (visited == true){
                return true;
            }
        }
        return visited;
    }

    public void addOffer(Offer offer) {
        exchanges.add(offer);
    }

    public void addVisited(Currency currency) {
        visited.add(currency);
    }

    public String getShortName() {
        return shortName;
    }

    public double getExchangedMoney() {
        return exchangedMoney;
    }

    public void setExchangedMoney(double exchangedMoney) {
        this.exchangedMoney = exchangedMoney;
    }

    public ArrayList<Offer> getExchanges() {
        return exchanges;
    }

    public ArrayList<Currency> getVisited() {
        return visited;
    }

    public void setVisited(ArrayList<Currency> visited) {
        this.visited = visited;
    }

    public Path toPath() {
        String pathWay;
        Path path;

        path = new Path();
        StringBuilder sb = new StringBuilder();

        for (Currency c : visited) {
            sb.append(c.getShortName()).append("->");
        }
        if (visited.size()>1) {
            sb.delete(sb.toString().length() - 2, sb.toString().length());
            pathWay = sb.toString();
            path.setResultingPath(pathWay);
            path.setResultingCredit(exchangedMoney);
        }else{
            return null;
        }

        return path;
    }

    public boolean equals(Currency currency) {
        String shortName = currency.getShortName();

        return getShortName().equals(shortName);
    }
}
