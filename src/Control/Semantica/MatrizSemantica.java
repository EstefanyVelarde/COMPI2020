package Control.Semantica;

import java.util.Hashtable;

public class MatrizSemantica {
    Hashtable<String, String> suma, resta, multi, div, 
            res, opRel, opIdent, opBool, opBits, opExp, asign;
    
    String error;
    
    public MatrizSemantica() {
        error = "error";
        
        initSuma();
        initResta();
        initMulti();
        initDiv();
        initRes();
        
        initOpRel();
        initOpIdent();
        initOpBool();
        initOpBits();
        initOpExp();
                
        initAsign();
    }
    
    public String getType(String type1, String type2, int op) {
        
        if(asignacion(op)) { // Asignaciones
            if(type1.equals(type2))
                return type1;
            
            if(type1.equals("V") || type2.equals("V"))
                return type1;
            
            return isValid(type1, type2);
        }
       
        if(type1.equals("V")) // Variant toma el tipo de dato con el que se compara
            if(type2.equals("N")) 
                return error;
            else
                return type2;
        else 
            if(type2.equals("V"))
                return type1;
        
        Hashtable<String, String> matriz = null;
        
        switch(op) {
            case -7:    matriz = suma;      break;
            case -8:    matriz = resta;     break;
            case -9:    matriz = multi;     break;
            case -10:   matriz = opExp;     break;
            case -11:   matriz = div;       break;
            case -12: case -13: 
                        matriz = res;       break;
            case -16: case -17: case -18: case -25: 
            case -26: case -27: case -51: 
                        matriz = opBool;    break;
            case -28: case -29: 
                        matriz = opBits;    break;
            
            default:
                if(op >= -24 && op <= -19)
                    matriz = opRel;
                else
                    if(op >= -60 && op <= -57)
                        matriz = opIdent;
                    
        }
        
        String type = isValid(matriz, type1, type2);
        
        return type == null ? error : type;
    }
    
    // ASIGNACION
    public String isValid(String type1, String type2) { // Para asignaciones
        String keyType1 = type1 + "-" + type2;
        
        return asign.containsKey(keyType1) ? asign.get(keyType1) : error;
    }
    
    public boolean asignacion(int LT) {
        if(LT >= -37 && LT <= -30)
            return true;
        
        return false;
    }
    
    private void initAsign() {
        asign = new Hashtable();
        
        // DECIMAL
        asign.put("D-CH", "D");
        
        // FLOAT
        asign.put("F-CH", "F");
        asign.put("F-D", "F");
    }
    
    // OPERACION
    public String isValid(Hashtable<String, String> matriz, String type1, String type2) {
        String keyType1 = type1 + "-" + type2;
        
        return matriz.get(keyType1);
    }
    
    
    private void initSuma() {
        suma = new Hashtable();
        
        // DECIMAL
        suma.put("D-D", "D");
        suma.put("D-F", "F");
        suma.put("D-C", "C");
        suma.put("D-CH", "CH");
        suma.put("D-CM", "CM");
        
        // OCTAL
        suma.put("DO-DO", "DO");
        suma.put("DO-C", "C");
        suma.put("DO-CH", "C");
        
        // BINARIO
        suma.put("DB-DB", "DB");
        suma.put("DB-C", "C");
        suma.put("DB-CH", "C");
        
        // HEXADECIMAL
        suma.put("DH-DH", "DH");
        suma.put("DH-C", "C");
        suma.put("DH-CH", "C");
        
        // FLOAT
        suma.put("F-D", "F");
        suma.put("F-F", "F");
        suma.put("F-C", "C");
        suma.put("F-CH", "C");
        suma.put("F-CM", "CM");
        
        // CADENA
        suma.put("C-D", "C");
        suma.put("C-DO", "C");
        suma.put("C-DB", "C");
        suma.put("C-DH", "C");
        suma.put("C-F", "C");
        suma.put("C-C", "C");
        suma.put("C-CH", "C");
        suma.put("C-CM", "C");
        suma.put("C-B", "C");
        
        // CHAR
        suma.put("CH-D", "CH");
        suma.put("CH-DO", "C");
        suma.put("CH-DB", "C");
        suma.put("CH-DH", "C");
        suma.put("CH-F", "C");
        suma.put("CH-C", "C");
        suma.put("CH-CH", "C");
        
        // COMPLEJA
        suma.put("CM-D", "CM");
        suma.put("CM-F", "CM");
        suma.put("CM-C", "C");
        suma.put("CM-CM", "CM");
        
        // BOOL
        suma.put("B-C", "C");
        
        // TUPLA
        suma.put("T-T", "T");
        suma.put("T-L", "L");
        suma.put("T-A", "L");
        
        // LISTA
        suma.put("L-T", "L");
        suma.put("L-L", "L");
        suma.put("L-A", "L");
        
        // ARREGLO
        suma.put("A-T", "L");
        suma.put("A-L", "L");
        suma.put("A-A", "A");
    }
    
    private void initResta() {
        resta = new Hashtable();
        
        // DECIMAL
        resta.put("D-D", "D");
        resta.put("D-F", "F");
        resta.put("D-CH", "D");
        resta.put("D-CM", "CM");
        
        // OCTAL
        resta.put("DO-DO", "DO");
        
        // BINARIO
        resta.put("DB-DB", "DB");
        
        // HEXADECIMAL
        resta.put("DH-DH", "DH");
        
        // FLOAT
        resta.put("F-D", "F");
        resta.put("F-F", "F");
        resta.put("F-CM", "CM");
        
        // CHAR
        resta.put("CH-D", "D");
        resta.put("CH-CH", "D");
        
        // COMPLEJA
        resta.put("CM-D", "CM");
        resta.put("CM-F", "CM");
        resta.put("CM-CM", "CM");
    }
    
    private void initMulti() {
        multi = new Hashtable();
        
        // DECIMAL
        multi.put("D-D", "D");
        multi.put("D-F", "F");
        multi.put("D-C", "C");
        multi.put("D-CH", "C");
        multi.put("D-CM", "CM");
        multi.put("D-T", "T");
        multi.put("D-L", "L");
        multi.put("D-A", "A");
        
        // OCTAL
        multi.put("DO-DO", "DO");
        multi.put("DO-C", "C");
        
        // BINARIO
        multi.put("DB-DB", "DB");
        multi.put("DB-C", "C");
        
        // HEXADECIMAL
        multi.put("DH-DH", "DH");
        multi.put("DH-C", "C");
        
        // FLOAT
        multi.put("F-D", "F");
        multi.put("F-F", "F");
        multi.put("F-CM", "CM");
        
        // CADENA
        multi.put("C-D", "C");
        multi.put("C-DO", "C");
        multi.put("C-DB", "C");
        multi.put("C-DH", "C");
        multi.put("C-T", "T");
        multi.put("C-L", "L");
        multi.put("C-A", "A");
        
        // CHAR
        multi.put("CH-D", "C");
        
        // COMPLEJA
        multi.put("CM-D", "CM");
        multi.put("CM-F", "CM");
        multi.put("CM-CM", "CM");
        
        // TUPLA
        multi.put("T-D", "T");
        multi.put("T-F", "T");
        multi.put("T-T", "T");
        multi.put("T-L", "L");
        multi.put("T-A", "L");
        
        // LISTA
        multi.put("L-D", "L");
        multi.put("L-F", "L");
        multi.put("L-T", "L");
        multi.put("L-L", "L");
        multi.put("L-A", "L");
        
        // ARREGLO
        multi.put("A-D", "A");
        multi.put("A-F", "A");
        multi.put("A-T", "L");
        multi.put("A-L", "L");
        multi.put("A-A", "A");
    }
    
    private void initDiv() {
        div = new Hashtable();
        
        // DECIMAL
        div.put("D-D", "F");
        div.put("D-F", "F");
        div.put("D-CM", "CM");
        
        // OCTAL
        div.put("DO-DO", "DO");
        
        // BINARIO
        div.put("DB-DB", "DB");
        
        // HEXADECIMAL
        div.put("DH-DH", "DH");
        
        // FLOAT
        div.put("F-D", "F");
        div.put("F-F", "F");
        div.put("F-CM", "CM");
        
        // COMPLEJA
        div.put("CM-D", "CM");
        div.put("CM-F", "CM");
        div.put("CM-CM", "CM");
    }
    
    private void initRes() {
        res = new Hashtable();
        
        // DECIMAL
        res.put("D-D", "D");
        res.put("D-CM", "CM");
        
        // OCTAL
        res.put("DO-DO", "DO");
        
        // BINARIO
        res.put("DB-DB", "DB");
        
        // HEXADECIMAL
        res.put("DH-DH", "DH");
        
        // COMPLEJA
        res.put("CM-D", "CM");
        res.put("CM-CM", "CM");
    }
    
    private void initOpRel() {
        opRel = new Hashtable();
        
        // DECIMAL
        opRel.put("D-D", "B");
        opRel.put("D-F", "B");
        opRel.put("D-C", "B");
        opRel.put("D-CH", "B");
        opRel.put("D-CM", "B");
        opRel.put("D-T", "B");
        
        // OCTAL
        opRel.put("DO-DO", "B");
        opRel.put("DO-C", "B");
        
        // BINARIO
        opRel.put("DB-DB", "B");
        opRel.put("DB-C", "B");
        
        // HEXADECIMAL
        opRel.put("DH-DH", "B");
        opRel.put("DH-C", "B");
        
        // FLOAT
        opRel.put("F-D", "B");
        opRel.put("F-F", "B");
        opRel.put("F-CM", "B");
        
        // CADENA
        opRel.put("C-D", "B");
        opRel.put("C-DO", "B");
        opRel.put("C-DB", "B");
        opRel.put("C-DH", "B");
        opRel.put("C-C", "B");
        
        // CHAR
        opRel.put("CH-D", "B");
        opRel.put("CH-CH", "B");
        
        // COMPLEJA
        opRel.put("CM-D", "B");
        opRel.put("CM-F", "B");
        opRel.put("CM-CM", "B");
        
        // TUPLA
        opRel.put("T-T", "B");
        
        // LISTA
        opRel.put("L-L", "B");
        
        // ARREGLO
        opRel.put("A-A", "B");
    }
    
    private void initOpIdent() {
        opIdent = new Hashtable();
        
        // TUPLA
        opIdent.put("T-T", "B");
        
        // LISTA
        opIdent.put("L-L", "B");
        
        // ARREGLO
        opIdent.put("A-A", "B");
    }
    
    private void initOpBool() {
        opBool = new Hashtable();
        
        // BOOL
        opBool.put("B-B", "B");
    }
    
    private void initOpBits() {
        opBits = new Hashtable();
        
        // DECIMAL
        opBits.put("D-D", "D");
        opBits.put("D-DO", "DO");
        opBits.put("D-DB", "DB");
        opBits.put("D-DH", "DH");
        
        // OCTAL
        opBits.put("DO-D", "DO");
        opBits.put("DO-DO", "DO");
        opBits.put("DO-DB", "DB");
        opBits.put("DO-DH", "DH");
        
        // BINARIO
        opBits.put("DB-D", "DB");
        opBits.put("DB-DO", "DB");
        opBits.put("DB-DB", "DB");
        opBits.put("DB-DH", "DH");
        
        // HEXADECIMAL
        opBits.put("DH-D", "DH");
        opBits.put("DH-DO", "DH");
        opBits.put("DH-DB", "DH");
        opBits.put("DH-DH", "DH");
        
        // CHAR
        opBits.put("CH-D", "CH");
        opBits.put("CH-DO", "CH");
        opBits.put("CH-DB", "CH");
        opBits.put("CH-DH", "CH");
    }
    
    private void initOpExp() {
        opExp = new Hashtable();
        
        // DECIMAL
        opExp.put("D-D", "D");
        opExp.put("DO-D", "DO");
        opExp.put("DB-D", "DB");
        opExp.put("DH-D", "DH");
        opExp.put("CM-D", "CM");
    }
}

/* ABREVIACIONES
    D = Decimal
    DO = Octal
    DB = Binario
    DH = Hexadecimal
    F = Flotantes
    C = Cadena
    CH = Char
    CM = Compleja
    B = Boolean
    T = Tuplas
    L = Listas
    A = Arreglos
    DIC = Diccionarios
    V = Variant
*/