package data;

import javafx.scene.control.TextArea;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class FileProcessor {
    Map<String, Currency> currencies;
    private int currentLineNumber = 0;
    int currentFilePart = 0;
    String repeatedCurrency;
    boolean isRepeatedCurrency;
    TextArea messages;

    public FileProcessor(TextArea communication){
        currencies = new HashMap<>();
        messages = communication;
        isRepeatedCurrency = false;
    }

    public boolean checkFile(File file) {
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
            result = true;
        } else {
            return false;
        }

        if (scanner.hasNextLine()) {
            while (result != false && scanner.hasNextLine()) {
                currentLine = scanner.nextLine();
                currentLineNumber++;
                result = checkLine(currentLine, currentLineNumber);
                reportCurrencyRepetition(isRepeatedCurrency);
            }
        } else {
            result = false;
        }

        return result;
    }

    private boolean checkLine(String line, int lineNumber) {
        boolean result;

        if (lineNumber == 1) {
            result = line.startsWith("#");

        } else if (line.startsWith("#")) {
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

        if (currencies.containsKey(shortcut)){
            repeatedCurrency = shortcut;
            isRepeatedCurrency = true;
        }else{
            currencies.put(shortcut, currency);
        }
    }

    private void addOffer(String line) {

    }

    public int getCurrentLineNumber() {
        return currentLineNumber;
    }

    public void reportCurrencyRepetition(boolean isToReport){
        if (isToReport){
            messages.setText(messages.getText()+"\nRepeated declaration of: "+repeatedCurrency);
            isRepeatedCurrency = false;
        }
    }
}
