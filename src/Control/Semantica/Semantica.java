package Control.Semantica;

import Control.Ambito.Ambito;
import Model.Operando;
import Model.Token;
import java.util.Hashtable;
import java.util.LinkedList;

public class Semantica {
    MatrizSemantica matriz;
    
    Ambito amb; // Para acceder a conexion, tokens y errores
    
    LinkedList<Operando> operStack;
    
    LinkedList<Token> opStack;
    
    // TEMPORAL TIPO - CONTADOR
    Hashtable<String, Integer> contTemp; 
    
    public Semantica(Ambito ambito) {
        this.amb = ambito;
        
        matriz = new MatrizSemantica();
        
        operStack =  new LinkedList();
        
        opStack = new LinkedList();
        
        contTemp = new Hashtable();
    }
    
    Token token;
    
    String[] idsimbolos; // [0] tipo [1] clase [2] idsimbolos
    
    public void checar(int LT) {
        token = amb.tokens.peekFirst();
        
        if(identificador(LT)) {
            idsimbolos = amb.getIdSimbolos(token.getLexema());
            
            if(idsimbolos != null)
                operStack.offer(new Operando(token, getTipo(idsimbolos[0]), Integer.parseInt(idsimbolos[2])));
            else
                operStack.offer(new Operando("V")); // Guardamos temp variant
        } else 
            if(operando(LT))
                operStack.offer(new Operando(token, getTipo(LT)));
            else 
                if(operador(LT)) 
                    opStack.offer(token);
    }
    
    // @ ZONA

    Operando oper1, oper2; 

    String tipoOper1, tipoOper2;

    String tempTipo;

    Token op;

    int opToken;    
    
    public void zona(int PS) {
        

        switch(PS) {
            case 850: 
                System.out.println("\n@ 850"); printStacks();
                
                saveLast();
                
                tempTipo = matriz.getType(tipoOper1, tipoOper2, opToken);
                
                operStack.offer(new Operando(tempTipo)); // Guardamos temp
                
                if (tempTipo == "V")
                    setError(tipoOper1, tipoOper2);
                   
                System.out.println("\nSE CREO TEMP"); printStacks();
            break;
            
            case 851: 
                System.out.println("\n@ 851"); printStacks();
                
                saveLast();
                
                tempTipo = matriz.getType(tipoOper1, tipoOper2, opToken);
                
                if (tempTipo == "V")
                    setError(tipoOper1, tipoOper2);
                
                System.out.println("\nASIGN VERIFICADA"); printStacks();
            break;
        }
        
    }
    
    public void saveLast() {
        oper1 = operStack.removeLast();
        oper2 = operStack.removeLast(); 
        
        op = opStack.removeLast();
        
        tipoOper1 = oper1.getTipo();
        tipoOper2 = oper2.getTipo();
        
        opToken = op.getToken();
    }
    
    public void printStacks() {
        
        System.out.println("OPERSTACK:");
        
        for(Operando o : operStack) {
            if(o.getToken() != null)
                System.out.println(o.getToken().getLexema()+" "+o.getTipo());
            else
                System.out.println("T"+o.getTipo());
        }
        
        System.out.println("\nOPSTACK:");
        for(Token t : opStack) 
            System.out.println(t.getLexema());
    }
    
    
    public String getTipo(String tipoSimbolos) {
        String tipo = null;
        
        switch(tipoSimbolos) {
            case "boolean":     tipo = "B";  break;
            case "caracter":    tipo = "C";  break;
            case "cadena":      tipo = "CH"; break;
            case "decimal":     tipo = "D";  break;
            case "flotante":    tipo = "DF"; break;
            case "complejo":    tipo = "CM"; break;
            case "binario":     tipo = "DB"; break;
            case "hexadecimal": tipo = "DH"; break;
            case "octal":       tipo = "DO"; break;
            case "struct": 
                switch(idsimbolos[1]) {
                    case "tupla":       tipo = "T";   break;
                    case "lista":       tipo = "L";   break;
                    case "arreglo":     tipo = "A";   break;
                    case "diccionario": tipo = "DIC"; break;
                }
            break;
        }
        
        return tipo;
    }
    
    public String getTipo(int LT) {
        String tipo = null;
        
        switch(LT) {
            case -54: 
            case -55: tipo = "B";  break;   // Boolean
            case -40: tipo = "C";  break;   // Cadena
            case -41: tipo = "CH"; break;   // Caracter
            case -45: tipo = "D";  break;   // Decimal
            case -46: tipo = "DF"; break;   // Float
            case -47: tipo = "CM"; break;   // Complejo
            case -48: tipo = "DB"; break;   // Binario
            case -49: tipo = "DH"; break;   // Hexadecimal
            case -50: tipo = "DO"; break;   // Octal
        }
        
        return tipo;
    }
    
    
    public boolean identificador(int LT) {
        return LT == -44;
    }
    
    public boolean operando(int LT) {
        if(LT >= -50 && LT <= -40) // C, CH, D, DF, CM, DB, DH, DO
            return true;
        
        if(LT >= -55 && LT <= -54) // true, false
            return true;
        
        return false;
    }
    
    public boolean operador(int LT) {
        if(LT >= -37 && LT <= -7)
            return true;
        
        return false;
    }
    
    // ERRORES
    String desc = "Incompatibilidad de tipos ";
    
    public void setError(String tipo1, String tipo2) {
        Token token = amb.tokens.peekFirst();
        
        amb.errores.add(new Model.Error(750, token.getLinea(), token.getLexema(), desc + tipo1 + " - " + tipo2, "Semántica"));
    }
    
}
