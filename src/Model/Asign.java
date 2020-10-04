package Model;

public class Asign {
    String asign;
    int[] temp;
    int line, errors;

    public Asign(String asign, int[] temp, int line, int errors) {
        this.asign = asign;
        this.temp = temp;
        this.line = line;
        this.errors = errors;
    }
    
    public String getAsign() {
        return asign;
    }

    public void setAsign(String asign) {
        this.asign = asign;
    }

    public int[] getTemp() {
        return temp;
    }

    public void setTemp(int[] temp) {
        this.temp = temp;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }
    
    
}