package textInput;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class CreditTextField extends TextField {
    int charLimit;

    public CreditTextField() {
        charLimit = 13;
        this.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                verify();
            }
        });
    }

    @Override
    public void replaceText(int startIndex, int endIndex, String textToReplace) {
        String currentText = getText();
        String newText;
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(currentText).append(textToReplace);
        newText = stringBuilder.toString();

        System.out.println(newText);


        if(newText.matches("(\\d+)(,|\\.)?(\\d){0,2}?") && newText.length()<charLimit-2){
            super.replaceText(startIndex, endIndex, textToReplace);
        } else if((containsDigitsCommaAndDigits(newText) && newText.length()<=charLimit) || textToReplace.isEmpty()){

            super.replaceText(startIndex, endIndex, textToReplace);
        }
    }

    private boolean containsDigitsCommaAndDigits(String text) {
        if (text.length() < charLimit - 1) {
            return text.matches("(\\d+)(,|\\.)(\\d){0,2}");
        } else {
            return text.matches("(\\d+)(,|\\.)(\\d){1,2}");
        }
    }

    private boolean consistsOfDigitsOnly(String text) {
        return text.matches("(\\d)*");
    }

    public void verify() {
        if (getText().length() > charLimit) {
            setText(getText().substring(0, charLimit));
        }
    }
}
