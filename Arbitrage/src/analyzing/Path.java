package analyzing;

import java.text.DecimalFormat;

public class Path {
    private double resultingCredit;
    private String resultingPath;

    public void setResultingPath(String resultingPath) {
        this.resultingPath = resultingPath;
    }

    public void setResultingCredit(double resultingCredit) {
        this.resultingCredit = resultingCredit;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(resultingPath).append("\n").append(new DecimalFormat("0.00").format(resultingCredit));
        return sb.toString();
    }
}
