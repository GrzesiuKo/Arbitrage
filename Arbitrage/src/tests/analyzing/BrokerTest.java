package analyzing;

import data.FileProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BrokerTest {
    Broker broker;
    FileProcessor fileProcessor;

    @BeforeEach
    void setUp() {
        fileProcessor = new FileProcessor(null);
        fileProcessor.checkFileAndMakeGraph(new File("src/testFiles/DobreDane.txt"));
        broker = new Broker(fileProcessor.getGraph());
    }

    @Test
    void earnByArbitrageNegativeCredit() {
        //Given
        Path expected = null;
        Path result;
        //When
        result = broker.earnByArbitrage(-1);
        //Then
        assertEquals(expected, result);
    }

    @Test
    void earnByArbitragePositiveCreditIfThereIsArbitrage() {
        //Given
        String expected = "CHF->PLN->EUR->DHL->LSD->CHF\n263984900,00";
        Path path;
        //When
        path = broker.earnByArbitrage(1000);
        //Then
        assertEquals(expected, path.toString());
    }

    @Test
    void earnByArbitragePositiveCreditIfThereIsNoArbitrage() {
        //Given
        Path path;
        //When
        fileProcessor.checkFileAndMakeGraph(new File("src/testFiles/BrakArbitrage.txt"));
        broker = new Broker(fileProcessor.getGraph());
        path = broker.earnByArbitrage(1000);
        //Then
        assertEquals(null, path);
    }

    @Test
    void exchangeIfThereIsExchange() {
        //Given
        String expected = "EUR->DHL->LSD->CHF->PLN\n89909300,00";
        Path path;
        //When
        path = broker.exchange(1000, "EUR", "PLN");
        //Then
        assertEquals(expected, path.toString());
    }

    @Test
    void exchangeIfThereIsNoExchange() {
        //Given
        Path path;
        //When
        path = broker.exchange(1000, "PLN", "ABC");
        //Then
        assertEquals(null, path);
    }

    @Test
    void exchangeIfThereIsInvalidCurrencyName() {
        //Given
        Path path;
        //When
        path = broker.exchange(1000, "Hopla", "ABC");
        //Then
        assertEquals(null, path);
    }

}