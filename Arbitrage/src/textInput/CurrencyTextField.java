package textInput;

import javafx.scene.control.TextField;

public class CurrencyTextField extends TextField {
    int limit;
    String currentText;

    public CurrencyTextField() {
        limit = 3;
        this.setOnKeyTyped(event -> verify());
    }

    @Override
    public void replaceText(int startIndex, int endIndex, String textToReplace) {


        if (textToReplace.matches("[a-zA-Z]") || textToReplace.isEmpty()) {
            super.replaceText(startIndex, endIndex, textToReplace.toUpperCase());
        }
    }

    public void verify() {
        if (getText().length() <= limit) {
            currentText = getText();
        } else {
            setText(currentText);
        }
    }
}
