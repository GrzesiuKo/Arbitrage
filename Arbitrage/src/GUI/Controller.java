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
    private Stage stage;
    private FileProcessor fileProcessor;
    private Graph graph;

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
        } else {
            resultTextArea.setText(resultTextArea.getText() + "\nNiepoprawny plik. Błąd w linii: " + fileProcessor.getCurrentLineNumber());
        }
        graph = fileProcessor.getGraph();
        fileProcessor.showGraph();
        resultTextArea.setText(resultTextArea.getText() + "\nFinished file reading.");
    }

    public void calculateExchange() {
        Path exchangePath;
        Broker broker;
        String from;
        String to;
        double credit;

        exchangePath = null;
        broker = new Broker(graph);
        from = fromTextField.getText();
        to = toTextField.getText();
        credit = creditExchangeTextField.getDoubleFromString();

        exchangePath = broker.exchange(credit, from, to);

        resultTextArea.setText("Exchange Path:\n"+exchangePath);
    }

    public void calculateArbitrage() {
        Path arbitragePath;
        Broker broker;
        double credit;

        arbitragePath = null;
        broker = new Broker(graph);
        credit = creditArbitrageTextField.getDoubleFromString();

        arbitragePath = broker.earnByArbitrage(graph.toArrayList(),credit);

        resultTextArea.setText("Arbitrage Path:\n"+arbitragePath.toString());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
