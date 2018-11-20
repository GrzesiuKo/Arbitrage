package analyzing;

import java.text.DecimalFormat;
import java.util.Stack;

public class Path {
    private double charges;
    private double resultingCredit;
    private String resultingPath;



    private void add(String node){

    }

    private String makePath(Stack<String> nodes){
        String result = null;

        return result;
    }

    public void setResultingPath(String resultingPath) {
        this.resultingPath = resultingPath;
    }

    public String getResultingPath() {
        return resultingPath;
    }

    public void setResultingCredit(double resultingCredit) {
        this.resultingCredit = resultingCredit;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(resultingPath).append("\n").append(new DecimalFormat("#.00").format(resultingCredit));
        return sb.toString();
    }
}
