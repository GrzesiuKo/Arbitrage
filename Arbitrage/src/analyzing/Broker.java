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
            System.out.println("Kolejna waluta jako root: " + currencies.get(rootIndex).getShortName());
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

        path = result.toPath();

        return path;
    }

    private void configureGraph(ArrayList<Currency> currencies, int rootIndex) {
        int iterationNumber;
        boolean hasChanged;
        Currency root;

        if (currencies == null || rootIndex < 0) {
            return;
        } else {
            iterationNumber = 0;
            hasChanged = false;
            root = currencies.get(rootIndex);
        }
        while (iterationNumber < currencies.size() - 1) {
            for (Currency c : currencies) {
                if (!c.equals(root) && c.getExchangedMoney() >= 0) {
                    System.out.println("Na plusie, bierzemy jego sąsiadów: " + c.getShortName());
                    hasChanged = updateNodes(c);
                }
                if (isArbitrage){
                    break;
                }
            }
            if (!hasChanged || isArbitrage) {
                break;
            }
            iterationNumber++;
        }
    }

    private boolean updateNodes(Currency current) {
        Currency node;
        boolean hasChanged;

        hasChanged = false;

        for (Offer o : current.getExchanges()) {
            node = o.getCurrency();

            if (!node.hasVisited(current)) {
                System.out.println("\tWchodzimy z "+current.getShortName()+" do node " + node.getShortName());

                System.out.println("\t\t1. Scieżka do : "+current.getShortName()+" to\n" + current.toPath().toString());
                hasChanged = updateExchangedMoney(current, o);

                System.out.println("\t\tNode money: " + node.getExchangedMoney());
                System.out.println("\t\t2.Scieżka do : "+current.getShortName()+" to\n" + current.toPath().toString());
                System.out.println("\t\tScieżka do : "+node.getShortName()+" to\n" + node.toPath().toString());
            }else if(node.equals(graph.getRoot())){
                updateExchangedMoney(current, o);
                if (graph.getRoot().getExchangedMoney() > credit){
                    isArbitrage = true;
                    return true;
                }
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
System.out.println("LICZE");
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
