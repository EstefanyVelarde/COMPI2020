package Control.Cuadruplos;

import Model.Cuadruplo;
import java.util.LinkedList;

public class ContCuadruplos {
    
    public LinkedList<Cuadruplo> cuadruplos;
    
    public int[][] cont;
    
    public String[] header = {
        "TD",
        "TDO",
        "TDB",
        "TDH",
        "TF",
        "TC",
        "TCH",
        "TCM",
        "TB",
        "TT",
        "TL",
        "TL-D",
        "TL-DO",
        "TL-DB",
        "TL-DH",
        "TL-F",
        "TL-C",
        "TL-CH",
        "TL-CM",
        "TL-B",
        "TL-DIC",
        "TV",
        "Tfor",
        "Tforb",
        "lista",
        "dicc",
        "tupla",
        "call",
        "=",
        "op-rel",
        "op-log",
        "op-arit",
        "JF",
        "JMP",
        "valor",
        "IF-E",
        "FOR-E",
        "WHI-E",
        "def",
        "PPAL"
    };
            

   
    public void setContador(int contAmb) {
        cont = new int[contAmb+2][header.length]; 
        // 0 - 23 temps
        // 24 - 34 acciones
        // 35 - 39 temps
        
        for (int i = 0; i < cuadruplos.size(); i++) {
            for (int j = 0; j < 24; j++) { // TEMPS
                Cuadruplo actual = cuadruplos.get(i);
                
                String res = actual.getRes();
                
                int amb = actual.amb;
                
                if(res.contains(header[j])) {
                    //System.out.println(res +  " " + actual.amb);
                    
                    cont[amb][j]++;
                    
                    cont[contAmb+1][j]++;
                    
                    break;
                }
            }
            
            for (int j = 24; j < 35; j++) { // ACC
                Cuadruplo actual = cuadruplos.get(i);

                String acc = actual.getAccion();

                int amb = actual.amb;


                if(isOperador(acc)) {
                    acc = getTipoOp(acc);
                } 
                
                if(acc.contains(header[j])) {
                    //System.out.println(acc +  " " + actual.amb);

                    cont[amb][j]++;

                    cont[contAmb+1][j]++;

                    break;
                }
            }
            
            for (int j = 35; j < 40; j++) { // ETI
                Cuadruplo actual = cuadruplos.get(i);
                
                String eti = actual.getEtiqueta();
                
                int amb = actual.amb;
                
                if(eti.contains(header[j])) {
                    //System.out.println(eti +  " " + actual.amb);
                    
                    cont[amb][j]++;
                    
                    cont[contAmb+1][j]++;
                    
                    break;
                }
            }
        }
    }
    
    public boolean isOperador(String x) {
        switch(x) {
            case "<": case "<=": case ">": case ">=": case "==": case "!=":
            case "is": case "isnot": case "in": case "innot":
                return true;
            
            
            case "&&": case "&": case "|": case "!": case "||":
                return true;
                
            case "+": case "-": case "/": case "//": case "**":
                return true;
        }
        
        return false;
    }
    
    public String getTipoOp(String x) {
        switch(x) {
            case "<": case "<=": case ">": case ">=": case "==": case "!=":
            case "is": case "isnot": case "in": case "innot":
                return "op-rel";
            
            
            case "&&": case "&": case "|": case "!": case "||":
                return "op-log";
                
            case "+": case "-": case "/": case "//": case "**":
                return "op-arit";
        }
        
        return "op-rel";
    }
}
