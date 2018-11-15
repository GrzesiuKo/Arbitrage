package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class FileProcessor {
    Map<String, Currency> currencies;
    private int currentLineNumber = 0;
    int currentFilePart = 0;


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

    }

    private void addOffer(String line) {

    }

    public int getCurrentLineNumber() {
        return currentLineNumber;
    }
}
