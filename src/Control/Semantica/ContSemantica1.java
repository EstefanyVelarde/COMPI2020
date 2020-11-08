package Control.Semantica;

import Model.Asign;
import java.util.LinkedList;

public class ContSemantica1 {
    public LinkedList<Asign> asigns;
    
    int[] temp;
    
    int[] tempTotal;
    
    int errors, errorsTotal;
    
    public ContSemantica1() {
        asigns =  new LinkedList();
        
        resetValues();
        
        tempTotal = new int[14];
        errorsTotal = 0;
    }
    
    public void addTemp(String temp) {
        int pos = getPos(temp);
        
        if(pos > 0) {
            this.temp[pos]++;
            tempTotal[pos]++;
        }
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
        
        System.out.println("");
        resetValues();
    }
    
    public void addError() {
        errors++;
        errorsTotal++;
    }
    
    void resetValues() {
        temp = new int[14];
        errors = 0;
    }

    public LinkedList<Asign> getAsigns() {
        return asigns;
    }

    public int[] getTempTotal() {
        return tempTotal;
    }

    public int getErrorsTotal() {
        return errorsTotal;
    }

}


