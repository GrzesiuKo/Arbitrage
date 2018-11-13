package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

public class FileProcessor {
    Map<String, Currency> currencies;
    private int currentLineNumber = -1;
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

        while (result != false && scanner.hasNext()) {
            currentLine = scanner.nextLine();
            currentLineNumber++;
            result = checkLine(currentLine, currentLineNumber);
        }

        return false;
    }

    private boolean checkLine(String line, int lineNumber) {
        boolean result = true;

        if (lineNumber == 0) {
            result = line.contains("#");

        } else if (line.contains("#")) {
            currentFilePart++;

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
        boolean result = true;

        return result;
    }

    private boolean checkOfferLine(String line) {
        boolean result = true;

        return result;
    }

    private void addCurrency(String line) {

    }

    private void addOffer(String line) {

    }
}
