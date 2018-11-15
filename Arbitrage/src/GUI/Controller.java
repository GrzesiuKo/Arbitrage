package GUI;

import data.FileProcessor;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import textInput.CreditTextField;
import textInput.CurrencyTextField;

import java.io.File;

public class Controller {
    private Stage stage;
    private FileProcessor fileProcessor;

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
        fileProcessor = new FileProcessor();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);

        if(fileProcessor.checkFile(file)){
            resultTextArea.setText("Poprawny plik. Obecna linia to: "+fileProcessor.getCurrentLineNumber());
            dataCheckBox.setSelected(true);
        }else{
            resultTextArea.setText("Niepoprawny plik. Błąd w linii: "+fileProcessor.getCurrentLineNumber());
        }

    }

    public void calculateExchange() {
        dataCheckBox.setSelected(true);
    }

    public void calculateArbitrage() {

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
