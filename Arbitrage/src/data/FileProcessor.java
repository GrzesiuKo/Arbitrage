package data;

import javafx.scene.control.TextArea;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class FileProcessor {
    data.Graph graph;
    int currentFilePart = 0;
    String repeatedCurrency;
    boolean isRepeatedCurrency;
    TextArea messages;
    private int currentLineNumber = 0;

    public FileProcessor(TextArea communication) {
        graph = new data.Graph();
        messages = communication;
        isRepeatedCurrency = false;
    }

    public boolean checkFileAndMakeGraph(File file) {
        Scanner scanner;
        boolean result;
        String currentLine;

        if (file != null) {
            try {
                scanner = new Scanner(file);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            initializeFileProcessor(messages);
            result = true;
        } else {
            return false;
        }

        if (scanner.hasNextLine()) {
            while (result != false && scanner.hasNextLine()) {
                currentLine = scanner.nextLine();
                currentLineNumber++;
                result = checkLineAndAddNode(currentLine, currentLineNumber);
                reportCurrencyRepetition(isRepeatedCurrency);
            }
        } else {
            result = false;
        }

        return result;
    }

    private void initializeFileProcessor(TextArea communication) {
        graph = new Graph();
        messages = communication;
        isRepeatedCurrency = false;
    }

    private boolean checkLineAndAddNode(String line, int lineNumber) {
        boolean result;
        int hashCharIndex;

        hashCharIndex = line.indexOf("#");

        if (lineNumber == 1) {
            result = hashCharIndex == 0 || hashCharIndex == 1;
        } else if (hashCharIndex == 0 || hashCharIndex == 1) {
            currentFilePart++;
            result = true;

        } else if (lineNumber > 0 && currentFilePart == 0) {
            result = checkCurrencyLine(line);

            if (result) {
                addCurrency(line);
            }

        } else if (lineNumber > 0 && currentFilePart == 1) {
            result = checkOfferLine(line);

            if (result) {
                addOffer(line);
            }

        } else {
            return false;
        }


        return result;
    }

    private boolean checkCurrencyLine(String line) {
        boolean result;
        Scanner scanner;
        Pattern currencyLine;

        scanner = new Scanner(line);
        scanner.useDelimiter(Pattern.compile("$"));
        currencyLine = Pattern.compile("(\\d)+\\s[A-Z]{3}.*");

        result = scanner.hasNext(currencyLine);

        return result;
    }

    private boolean checkOfferLine(String line) {
        boolean result;
        Scanner scanner;
        Pattern offerLine;

        scanner = new Scanner(line);
        scanner.useDelimiter(Pattern.compile("$"));
        offerLine = Pattern.compile("(\\d)+\\s[A-Z]{3}\\s[A-Z]{3}\\s(\\d+)((,|\\.)(\\d+))?\\s(PROC|STAŁA)\\s\\d+((,|\\.)(\\d+))?\\s*");

        result = scanner.hasNext(offerLine);

        return result;
    }

    private void addCurrency(String line) {
        String shortcut;
        String fullName;
        Scanner scanner;
        Currency currency;

        scanner = new Scanner(line);
        scanner.next();
        shortcut = scanner.next();
        scanner.useDelimiter("");
        scanner.next(" ");
        scanner.useDelimiter("$");
        fullName = scanner.next();
        currency = new Currency(shortcut, fullName);

        if (graph.hasCurrency(shortcut)) {
            repeatedCurrency = shortcut;
            isRepeatedCurrency = true;
        } else {
            graph.addCurrency(shortcut, currency);
        }
    }

    private void addOffer(String line) {
        String from;
        String to;
        double rate;
        double charge;
        double percent;
        Scanner scanner;
        Offer offer;

        if (line != null) {
            scanner = new Scanner(line);
            charge = 0;
            percent = 0;
        } else {
            return;
        }

        scanner.next();
        from = scanner.next();
        to = scanner.next();
        rate = getDoubleFromString(scanner.next());

        if (isPercent(scanner.next())) {
            percent = getDoubleFromString(scanner.next());
        } else {
            charge = getDoubleFromString(scanner.next());
        }

        if (graph.hasCurrency(from) && graph.hasCurrency(to)) {

            offer = initializeOffer(to, rate, percent, charge);
            graph.connectOfferWithCurrency(offer, from);
        }
    }

    public int getCurrentLineNumber() {
        return currentLineNumber;
    }

    public void reportCurrencyRepetition(boolean isToReport) {
        if (isToReport) {
            messages.setText(messages.getText() + "\nRepeated declaration of: " + repeatedCurrency);
            isRepeatedCurrency = false;
        }
    }

    private boolean isPercent(String name) {
        return name.matches("PROC");
    }

    private double getDoubleFromString(String text) {
        Scanner scanner;
        if (text.matches("\\d*(\\.(\\d+))?")) {
            return Double.parseDouble(text);
        } else if (text.matches("\\d*(,(\\d+))?")) {
            scanner = new Scanner(text);
            return scanner.nextDouble();
        } else {
            return -1;
        }
    }

    private Offer initializeOffer(String to, double rate, double percentCharge, double standingCharge) {
        Offer offer;
        Currency currency;

        if (graph.hasCurrency(to)) {
            currency = graph.getCurrency(to);
            offer = new Offer(currency, rate, percentCharge, standingCharge);
        } else {
            offer = null;
        }
        return offer;
    }

    public void showGraph() {
        ArrayList<Currency> currencies;
        currencies = graph.toArrayList();

        for (Currency c : currencies) {
            messages.setText(messages.getText() + "\n" + c.getShortName() + ": ");
            for (Offer o : c.getExchanges()) {
                messages.setText(messages.getText() + o.getCurrency().getShortName() + " ");
            }
        }
    }

    public Graph getGraph() {
        return graph;
    }
}
