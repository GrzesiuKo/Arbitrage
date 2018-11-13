package textInput;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class CurrencyNameTextField extends TextField {
    int limit;
    String currentText;

    public CurrencyNameTextField() {
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
        if (textToReplace.matches("[A-Z]") || textToReplace.isEmpty()) {
            super.replaceText(startIndex, endIndex, textToReplace);
        }
    }

    public void verify() {
        if (getText().length() <= limit) {
            currentText = getText();
        }else{
            setText(currentText);
        }
    }
}
