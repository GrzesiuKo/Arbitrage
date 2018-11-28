package data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileProcessorTest {
    FileProcessor fileProcessor;

    @BeforeEach
    void setUp() {
        fileProcessor = new FileProcessor(null);
    }

    @Test
    void checkFileAndMakeGraphForGoodData() {
        //Given
        boolean result;
        String graphString;
        String resultString;
        //When
        result = fileProcessor.checkFileAndMakeGraph(new File("src/testFiles/DobreDane.txt"));

        graphString = graphToString(fileProcessor.getGraph());
        resultString = "CHF: PLN \n" +
                "ABC: XYZ \n" +
                "EUR: LSD DHL \n" +
                "PLN: EUR USD \n" +
                "USD: CHF \n" +
                "XYZ: \n" +
                "DHL: LSD \n" +
                "LSD: PLN CHF \n";
        //Then
        assertEquals(true, result);
        assertEquals(resultString, graphString);
    }

    @Test
    void checkFileAndMakeGraphForInvalidData() {
        //Given
        boolean result;
        //When
        result = fileProcessor.checkFileAndMakeGraph(new File("src/testFiles/ZleDane.txt"));
        //Then
        assertEquals(false, result);
    }

    private String graphToString(Graph graph) {
        ArrayList<Currency> currencies;
        StringBuilder sb;

        sb = new StringBuilder();
        currencies = graph.toArrayList();

        for (Currency c : currencies) {
            sb.append(c.getShortName() + ": ");
            for (Offer o : c.getExchanges()) {
                sb.append(o.getCurrency().getShortName() + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
