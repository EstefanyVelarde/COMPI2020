package Control.Semantica;

import java.util.LinkedList;

public class ContSemantica {
    LinkedList<Asign> asigns;
    
    int[] temp;
    
    int errors;
    
    public ContSemantica() {
        asigns =  new LinkedList();
        
        resetValues();
    }
    
    public void addTemp(String temp) {
        int pos = getPos(temp);
        
        this.temp[pos]++;
    }
    
    public int getPos(String temp) {
        int pos = -1;
        
        switch(temp) {
            case "D":   pos = 0;  break;
            case "DO":  pos = 1;  break;
            case "DB":  pos = 2;  break;
            case "DH":  pos = 3;  break;
            case "F":   pos = 4;  break;
            case "C":   pos = 5;  break;
            case "CH":  pos = 6;  break;
            case "CM":  pos = 7;  break;
            case "B":   pos = 8;  break;
            case "T":   pos = 9;  break;
            case "L":   pos = 10; break;
            case "A":   pos = 11; break;
            case "DIC": pos = 12; break;
            case "V":   pos = 13; break;
            
        }
        
        return pos;
    }
    
    public void addAsing(String lex, int line) {
        asigns.add(new Asign(lex, temp, line, errors));
        
        System.out.println("\nASIGN");
        System.out.print(line + "\t");
        
        for(int t : temp)
            System.out.print(t + "\t");
        
        
        System.out.print(lex + "\t" + errors);
        
        resetValues();
    }
    
    public void addError() {
        errors++;
    }
    
    void resetValues() {
        temp = new int[14];
        errors = 0;
    }
    
}

class Asign {
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
