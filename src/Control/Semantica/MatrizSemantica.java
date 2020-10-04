package Control.Semantica;

import java.util.Hashtable;

public class MatrizSemantica {
    Hashtable<String, String> suma, resta, multi, div, 
            res, opRel, opIdent, opBits;
    
    String error;
    
    public MatrizSemantica() {
        error = "V";
        
        initSuma();
    }
    
    public String getType(String type1, String type2, int op) {
        if(asignacion(op)) {
            if(type1.equals(type2))
                return type1;
            else
                return error;
        }
        
        Hashtable<String, String> matriz = null;
        
        switch(op) {
            case -7: matriz = suma; break;
        }
        
        String type = getType(matriz, type1, type2);
        
        return type == null ? error : type;
    }

    public String getType(Hashtable<String, String> matriz, String type1, String type2) {
        String keyType1 = type1 + "-" + type2;
        String keyType2 = type2 + "-" + type1;
        
        return matriz.containsKey(keyType1) ? matriz.get(keyType1) : matriz.get(keyType2);
    }
    
    public boolean asignacion(int LT) {
        if(LT >= -37 && LT <= -30)
            return true;
        
        return false;
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
        suma.put("F-F", "F");
        suma.put("F-C", "C");
        suma.put("F-CH", "C");
        suma.put("F-CM", "CM");
        
        // CADENA
        suma.put("C-CH", "C");
        suma.put("C-CM", "C");
        suma.put("C-B", "C");
        
        // CHAR
        suma.put("CH-CH", "C");
        
        // COMPLEJA
        suma.put("CM-CM", "CM");
        
        // TUPLA
        suma.put("T-T", "T");
        suma.put("T-L", "L");
        suma.put("T-A", "L");
        
        // LISTA
        suma.put("L-L", "L");
        suma.put("L-A", "L");
        
        // ARREGLO
        suma.put("A-A", "A");
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