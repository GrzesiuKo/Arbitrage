package data;

public class Offer {
    Currency currency;
    double rate;
    double percentCharge;
    double standingCharge;

    public Offer(Currency currency, double rate, double percentCharge, double standingCharge) {
        this.currency = currency;
        this.rate = rate;
        this.percentCharge = percentCharge;
        this.standingCharge = standingCharge;
    }


    public Currency getCurrency() {
        return currency;
    }

    public double getRate() {
        return rate;
    }

    public double getPercentCharge() {
        return percentCharge;
    }

    public double getStandingCharge() {
        return standingCharge;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(currency.getShortName()).append("\n").append(rate).append("\n").append(percentCharge).append("\n").append(standingCharge).append("\n");
        return sb.toString();
    }
}
