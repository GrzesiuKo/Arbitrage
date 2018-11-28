package analyzing;

import data.Currency;
import data.Graph;
import data.Offer;

import java.util.ArrayList;

public class Broker {
    private Graph graph;

    private boolean isArbitrage;

    public Broker(Graph graph) {
        this.graph = graph;
    }

    public Path earnByArbitrage(double credit) {
        Path path;
        int rootIndex;
        Currency root;
        ArrayList<Currency> currencies;

        currencies = graph.toArrayList();

        if (currencies != null && currencies.size() > 0 && credit >= 0) {
            path = null;
            isArbitrage = false;
            rootIndex = 0;
            graph.prepareForNextPathFinding();
        } else {
            return null;
        }

        while (!isArbitrage && rootIndex < currencies.size()) {
            graph.prepareForNextPathFinding();
            root = currencies.get(rootIndex);
            graph.setRoot(root);
            root.setExchangedMoney(credit);
            root.addVisited(root);
            updateNodes(root);

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
        Path path;
        int rootIndex;
        Currency root;
        Currency result;
        ArrayList<Currency> currencies;

        currencies = graph.toArrayList();

        if (graph.getRoot() == graph.getCurrency(from)) {

            return graph.getCurrency(to).toPath();

        } else if (currencies != null && currencies.size() > 0 && credit >= 0) {

            isArbitrage = false;
            graph.prepareForNextPathFinding();

            root = graph.getCurrency(from);
            root.setExchangedMoney(credit);
            root.addVisited(root);
            updateNodes(root);
            graph.setRoot(root);

            rootIndex = currencies.indexOf(root);
        } else {
            return null;
        }

        configureGraph(currencies, rootIndex);

        result = graph.getCurrency(to);

        if (result.getExchangedMoney() == 0) {
            path = null;
        } else {
            path = result.toPath();
        }
        return path;
    }

    private void configureGraph(ArrayList<Currency> currencies, int rootIndex) {
        int iterationNumber;
        boolean hasChanged;
        Currency root;
        LoopIterator loopIterator;
        boolean hasLoopEnded;

        if (currencies == null || rootIndex < 0) {
            return;
        } else {
            iterationNumber = 0;
            root = currencies.get(rootIndex);
            loopIterator = new LoopIterator(rootIndex, currencies.size());
        }

        while (iterationNumber < currencies.size() - 1) {
            hasChanged = false;
            hasLoopEnded = false;
            Currency current;

            while (!hasLoopEnded) {
                current = currencies.get(loopIterator.getIterator());

                if (!current.equals(root) && current.getExchangedMoney() >= 0) {
                    if (updateNodes(current)) {
                        hasChanged = true;
                    }
                }

                if (isArbitrage) {
                    break;
                }

                loopIterator.next();
                hasLoopEnded = loopIterator.hasLoopEnded();
            }

            if (!hasChanged || isArbitrage) {
                break;
            }
            iterationNumber++;
        }
    }

    private boolean updateNodes(Currency source) {
        Currency destination;
        boolean hasChanged;

        hasChanged = false;

        for (Offer o : source.getExchanges()) {
            destination = o.getCurrency();

            if (checkIfICanUpdateNode(source, destination)) {
                hasChanged = updateExchangedMoney(source, o);
            } else if (destination.equals(graph.getRoot())) {
                updateExchangedMoney(source, o);
            }
        }
        return hasChanged;
    }

    private boolean checkIfICanUpdateNode(Currency source, Currency destination) {
        boolean itWasAlreadyVisited;
        boolean itWasLastVisited;
        boolean itIsGoingToLoop;

        itWasAlreadyVisited = destination.hasVisited(source);
        itWasLastVisited = destination.getLastVisited() == source;
        itIsGoingToLoop = source.hasVisited(destination);

        return (!itWasAlreadyVisited || itWasLastVisited) && !itIsGoingToLoop;
    }

    private boolean updateExchangedMoney(Currency source, Offer offer) {
        double result;
        double start;
        double rate;
        double percentCharge;
        double standingCharge;
        Currency destination;
        ArrayList<Currency> visited;

        start = source.getExchangedMoney();
        rate = offer.getRate();
        percentCharge = offer.getPercentCharge();
        standingCharge = offer.getStandingCharge();
        destination = offer.getCurrency();

        if (percentCharge >= 100) {
            return false;
        }

        result = start * rate;
        result *= (100 - percentCharge) / 100;
        result -= standingCharge;

        if (destination.getExchangedMoney() < result) {
            destination.setExchangedMoney(result);
            visited = new ArrayList<>(source.getVisited());
            destination.setVisited(visited);
            destination.addVisited(destination);
            return true;
        }
        return false;
    }


}
