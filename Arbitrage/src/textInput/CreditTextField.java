package textInput;

import javafx.scene.control.TextField;

public class CreditTextField extends TextField {
    int limit;

    public CreditTextField(){
        limit = Integer.MAX_VALUE;
    }

    @Override
    public void replaceText(int startIndex, int endIndex, String textToReplace){
        if (textToReplace.matches("[0-9]") || textToReplace.isEmpty()) {
            super.replaceText(startIndex, endIndex, textToReplace);
        }
    }

    public void verify() {
        if (getText().length() > limit) {
            setText(getText().substring(0, limit));
        }
    }
}
