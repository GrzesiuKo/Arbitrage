package analyzing;

import data.Currency;
import data.Graph;
import data.Offer;

import java.util.ArrayList;

public class Broker {
    private FinancialAnalyst analyst;
    private double finalValue;
    private Graph graph;

    public Broker(Graph graph) {
        this.graph = graph;
    }

    public Path earnByArbitrage(ArrayList<Currency> currencies, double credit) {
        Path path;
        boolean isArbitrage;
        int rootIndex;

        if (currencies != null && currencies.size() > 0 && credit >= 0) {
            path = null;
            isArbitrage = false;
            rootIndex = 0;
            graph.prepareForNextPathFinding();
        } else {
            return null;
        }

        while (!isArbitrage && rootIndex < currencies.size()) {
            graph.setRoot(currencies.get(rootIndex));
            System.out.println(credit);
            graph.getRoot().setExchangedMoney(credit);
            graph.getRoot().addVisited(graph.getRoot());
            configureGraph(currencies, rootIndex);
            if (graph.getRoot().getExchangedMoney() > credit) {
                isArbitrage = true;

            } else {
                rootIndex++;
            }
        }

        if (isArbitrage == true) {
            path = graph.getRoot().toPath();
        }

        return path;
    }

    public Path exchange(double credit, String from, String to) {
        Path path = null;

        return path;
    }

    private void configureGraph(ArrayList<Currency> currencies, int rootIndex) {
        Currency current;
        int iterationNumber;
        LoopIterator loopIterator;
        boolean hasChanged;
        boolean change;

        if (currencies == null || rootIndex < 0) {
            return;
        } else {
            current = null;
            iterationNumber = 0;
            loopIterator = new LoopIterator(rootIndex, currencies.size());
            hasChanged = false;
            change = false;
        }
        while (iterationNumber < currencies.size() - 1) {
            current = currencies.get(loopIterator.getIterator());

            if (current.getExchangedMoney()<0){
                loopIterator.next();

            }else {
                change = updateNodes(current);
                if (change) {
                    hasChanged = true;
                }
                loopIterator.next();
            }
            if (loopIterator.getIterator() == rootIndex) {
                iterationNumber++;
                loopIterator.next();
                if (!hasChanged) {
                    break;
                }
            }
        }
    }

    private boolean updateNodes(Currency current) {
        Currency node;
        boolean hasChanged;

        hasChanged = false;

        for (Offer o : current.getExchanges()) {
            node = o.getCurrency();

            if (!node.hasVisited(current)) {
                System.out.println("Zmieniamy node "+node.getShortName());
                hasChanged = updateExchangedMoney(current, o);
            } else if (node == graph.getRoot()) {
                // hasChanged = updateExchangedMoney(current, o);

            }
        }
        return hasChanged;
    }

    private boolean updateExchangedMoney(Currency source, Offer offer) {
        double result;
        double start;
        double rate;
        double percentCharge;
        double standingCharge;
        Currency destinationCurrency;

        start = source.getExchangedMoney();
        rate = offer.getRate();
        percentCharge = offer.getPercentCharge();
        standingCharge = offer.getStandingCharge();
        destinationCurrency = offer.getCurrency();

        if (percentCharge >= 100) {
            return false;
        }

        result = (start * rate) * (100 - percentCharge) / 100 - standingCharge;

        if (destinationCurrency.getExchangedMoney() < result) {
            destinationCurrency.setExchangedMoney(result);
            destinationCurrency.setVisited(source.getVisited());
            destinationCurrency.addVisited(destinationCurrency);
            return true;
        }
        return false;
    }
}
