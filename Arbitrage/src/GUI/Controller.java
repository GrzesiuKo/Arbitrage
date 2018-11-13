package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import textInput.CurrencyNameTextField;

import java.awt.*;
import java.io.File;

public class Controller {
    private Stage stage;

    @FXML
    CheckBox dataCheckBox;

    @FXML
    CheckBox fromCheckBox;

    @FXML
    CheckBox toCheckBox;

    @FXML
    CheckBox creditCheckBox;

    @FXML
    CurrencyNameTextField toTextField;

    @FXML
    CurrencyNameTextField fromTextField;

    @FXML
    TextArea resultsTextArea;

    public void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);

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
