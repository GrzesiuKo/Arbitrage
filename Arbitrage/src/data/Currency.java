package data;

import java.util.ArrayList;

public class Currency {
    private String shortName;
    private String fullName;
    private double charge;
    private double cost;
    private double exchangedMoney;
    private Currency previous;
    private ArrayList<Offer> exchanges;

    public Currency(String shortName, String fullName) {
        this.shortName = shortName;
        this.fullName = fullName;
        exchanges = new ArrayList<>();
    }

    public boolean hasOffer(Offer offer){
        boolean result;

        if (offer == null){
            return false;
        }else{
            result = false;
        }

        for (Offer o: exchanges) {
            result = offer.getCurrency().equals(o.getCurrency());
        }

        return result;
    }

    public void addOffer(Offer offer){
        exchanges.add(offer);
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getExchangedMoney() {
        return exchangedMoney;
    }

    public void setExchangedMoney(double exchangedMoney) {
        this.exchangedMoney = exchangedMoney;
    }

    public Currency getPrevious() {
        return previous;
    }

    public void setPrevious(Currency previous) {
        this.previous = previous;
    }

    public ArrayList<Offer> getExchanges() {
        return exchanges;
    }
}
