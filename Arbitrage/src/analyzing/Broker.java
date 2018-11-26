package analyzing;

import data.Currency;
import data.Graph;
import data.Offer;

import java.util.ArrayList;

public class Broker {
    private Graph graph;

    private boolean isArbitrage;
    private double credit;

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
            this.credit = credit;
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

        if (currencies != null && currencies.size() > 0 && credit >= 0) {
            path = null;
            isArbitrage = false;
            graph.prepareForNextPathFinding();
            this.credit = credit;
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
            hasLoopEnded = false;
        }

        while (iterationNumber < currencies.size() - 1) {
            hasChanged = false;
            hasLoopEnded = false;
            Currency c;
            System.out.println("Początek iteracji: " + iterationNumber);
            while (!hasLoopEnded) {
                c = currencies.get(loopIterator.getIterator());
                System.out.println("Sprawdzamy walute " + c.getShortName());
                System.out.println("Jej money to: " + c.getExchangedMoney());
                if (!c.equals(root) && c.getExchangedMoney() >= 0) {

                    if (updateNodes(c)) {
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
            System.out.println("Koniec iteracji: " + iterationNumber);
            iterationNumber++;
        }

        System.out.println("Koniec iteracji: " + iterationNumber);
    }

    private boolean updateNodes(Currency source) {
        Currency destination;
        boolean hasChanged;

        hasChanged = false;

        for (Offer o : source.getExchanges()) {
            destination = o.getCurrency();

            System.out.println("\tWchodzimy do sąsiada: " + destination.getShortName());

            if (checkIfICanUpdateNode(source, destination)) {
                hasChanged = updateExchangedMoney(source, o);
                if (hasChanged) {
                    System.out.println("\t\tZaktualizowano: " + destination.getShortName() + " " + destination.getExchangedMoney());
                }
            } else if (destination.equals(graph.getRoot())) {
                updateExchangedMoney(source, o);
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

        result = ((start * rate) * ((100 - percentCharge)) / 100) - standingCharge;
        if (destination.getExchangedMoney() < result) {
            destination.setExchangedMoney(result);
            visited = new ArrayList<>(source.getVisited());
            destination.setVisited(visited);
            destination.addVisited(destination);
            return true;
        }
        return false;
    }

    private boolean checkIfICanUpdateNode(Currency source, Currency destination){
        boolean hasAlreadyVisited;
        boolean itWasLastVisited;
        boolean isItGoingToLoop;

        hasAlreadyVisited = destination.hasVisited(source);
        itWasLastVisited = destination.getLastVisited() == source;
        isItGoingToLoop = source.hasVisited(destination);

        return (!hasAlreadyVisited || itWasLastVisited) && !isItGoingToLoop;
    }

}
