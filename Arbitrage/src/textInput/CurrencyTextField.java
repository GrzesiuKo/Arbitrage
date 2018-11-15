package textInput;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class CurrencyTextField extends TextField {
    int limit;
    String currentText;

    public CurrencyTextField() {
        limit = 3;
        this.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                verify();
            }
        });
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
