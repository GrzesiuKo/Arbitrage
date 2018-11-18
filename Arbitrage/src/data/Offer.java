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
}
