package GUI;

import analyzing.Broker;
import analyzing.Path;
import data.FileProcessor;
import data.Graph;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import textInput.CreditTextField;
import textInput.CurrencyTextField;

import java.io.File;

public class Controller {
    @FXML
    CheckBox dataCheckBox;
    @FXML
    CheckBox fromCheckBox;
    @FXML
    CheckBox toCheckBox;
    @FXML
    CheckBox creditCheckBox;
    @FXML
    CurrencyTextField toTextField;
    @FXML
    CurrencyTextField fromTextField;
    @FXML
    TextArea resultTextArea;
    @FXML
    CreditTextField creditExchangeTextField;
    @FXML
    CreditTextField creditArbitrageTextField;
    private Stage stage;
    private FileProcessor fileProcessor;
    private Graph graph;

    public void chooseFile() {
        FileChooser fileChooser;

        fileChooser = new FileChooser();
        fileProcessor = new FileProcessor(resultTextArea);

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);

        resultTextArea.setText("Reading the file...");
        if (fileProcessor.checkFileAndMakeGraph(file)) {
            resultTextArea.setText(resultTextArea.getText() + "\nPoprawny plik. Obecna linia to: " + fileProcessor.getCurrentLineNumber());
            dataCheckBox.setSelected(true);

            graph = fileProcessor.getGraph();
            fileProcessor.showGraph();
        } else if (fileProcessor.getCurrentLineNumber() == 0) {
            resultTextArea.setText(resultTextArea.getText() + "\nIncorrect ﬁle. Make sure it's ANSI encoding.");
        } else {
            resultTextArea.setText(resultTextArea.getText() + "\nIncorrect ﬁle. Line " + fileProcessor.getCurrentLineNumber() + " is wrong. Compare it with the ﬁle pattern.");
        }

        resultTextArea.setText(resultTextArea.getText() + "\nFinished file reading.");
    }

    public void calculateExchange() {
        Path exchangePath;
        Broker broker;
        String from;
        String to;
        double credit;

        if (graph == null) {
            resultTextArea.setText("There was no ﬁle given.");
            return;
        }

        broker = new Broker(graph);
        from = fromTextField.getText();
        to = toTextField.getText();
        credit = creditExchangeTextField.getDoubleFromString();

        if (!graph.hasCurrency(from)) {
            resultTextArea.setText("There is no such currency like the one given in FROM field.");
            return;
        }

        if (!graph.hasCurrency(to)) {
            resultTextArea.setText("There is no such currency like the one given in TO field.");
            return;
        }

        if (!from.isEmpty() && !to.isEmpty()) {
            exchangePath = broker.exchange(credit, from, to);
            if (exchangePath != null) {
                resultTextArea.setText("Exchange Path:\n" + exchangePath);
            } else {
                resultTextArea.setText("No profitable exchange between those currencies possible.");
            }
        } else {
            resultTextArea.setText("Fill the missing information.");
        }
        graph.writeOutCurrencies();
    }

    public void calculateArbitrage() {
        Path arbitragePath;
        Broker broker;
        double credit;

        if (graph == null) {
            resultTextArea.setText("No file was given.");
            return;
        }

        broker = new Broker(graph);
        credit = creditArbitrageTextField.getDoubleFromString();
        if (credit != -1) {
            arbitragePath = broker.earnByArbitrage(credit);

            if (arbitragePath != null) {
                resultTextArea.setText("Arbitrage Path:\n" + arbitragePath.toString());
            } else {
                resultTextArea.setText("No arbitrage found.");
            }
        } else {
            resultTextArea.setText("Fill the missing information.");
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
