package data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileProcessorTest {
    FileProcessor fileProcessor;

    @BeforeEach
    void setUp() {
        fileProcessor = new FileProcessor(null);
    }

    @Test
    void checkFileAndMakeGraphForGoodData() {
        boolean result;

        result = fileProcessor.checkFileAndMakeGraph(new File("src/testFiles/DobreDane.txt"));

        assertEquals(true, result);
    }
}