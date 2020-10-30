package Control.Semantica;

import Control.Ambito.Ambito;
import Model.Operando;
import Model.Token;
import Model.Error;
import java.util.LinkedList;

public class Semantica1 {
    MatrizSemantica matriz;
    
    ContSemantica contador;
    
    Ambito amb; // Para acceder a conexion, tokens y errores
    
    Semantica2 sem2;
    
    LinkedList<Operando> operStack;
    
    LinkedList<Token> opStack;
    
    public Semantica1(Ambito ambito) {
        this.amb = ambito;
        
        matriz = new MatrizSemantica();
        
        contador = new ContSemantica();
        
        operStack =  new LinkedList();
        
        opStack = new LinkedList();
        
        lexemaAsign = "";
    }
    
    Token token;
    
    String[] idsimbolos; // [0] tipo [1] clase [2] idsimbolos
    
    public void checar(int LT) {
        if(identificador(LT)) {
            token = amb.tokens.peekFirst();
            
            setIdentificador();
            
            if(asignacion) 
                lexemaAsign += " " + token.getLexema();
            
            
        } else {
            //if(asignacion) {
                token = amb.tokens.peekFirst();
                
                if(operando(LT)) {
                    if(negativo) {
                        operStack.offer(new Operando(new Token(token.getToken(), 
                                token.getLinea(), "-"+token.getLexema()), getTipo(LT)));

                        negativo = false;
                    } else
                        operStack.offer(new Operando(token, getTipo(LT)));

                    lexemaAsign += " " + token.getLexema();
                } else 
                    if(operador(LT)) {
                        opStack.offer(token);

                        lexemaAsign += " " + token.getLexema();
                    }
                        
           // }
        }
    }
    
    // @ ZONA

    Operando oper1, oper2; 

    String tipoOper1, tipoOper2, asign;
    
    String lexemaAsign, lexemaOper, lexOp;

    String tempTipo;

    Token op;

    int opToken, line;  
    
    boolean asignacion, negativo;
    
    public void zona(int PS) {
        if(!amb.declaracion) {// SI ESTA EN EJECUCION
            
                switch(PS) {
                    case 850: // VERIF. OPERACIÓN
                        System.out.println("\n@ 850"); printStacks();

                        saveLast();

                        tempTipo = matriz.getType(tipoOper1, tipoOper2, opToken);

                        if (tempTipo.equals("error")) {

                            setError(lexemaOper);

                            tempTipo = "V"; // Marcamos Variant
                        }

                        operStack.offer(new Operando(new Token(lexemaOper, line), tempTipo)); // Guardamos temp

                        System.out.println("\n++ SE CREO TEMP\n"); printStacks();

                        contador.addTemp(tempTipo);
                    break;
                    
                    case 851: 
                        asignacion = true;

                        lexemaAsign = token.getLexema();
                    break;
                  
                    case 852: // VERIF. ASIGNACIÓN
                        if(asignacion) {
                            System.out.println("\n@ 852"); printStacks();

                            saveLast();

                            if(oper1.getIdsimbolos() != -1) {

                                tempTipo = matriz.getType(tipoOper1, tipoOper2, opToken);

                                if (tempTipo.equals("error")) {
                                    setError(lexemaAsign);
                                    
                                    sem2.setError(761, token.getLinea(), lexemaAsign); // SEM2, REGLA 2: ID = EXP
                                } else
                                    sem2.edo = "Acepta";
                                
                                System.out.println("\nASIGN VERIFICADA " + tempTipo); printStacks();

                                contador.addAsing(asign, line);
                            } else
                                sem2.edo = null; // SEM2, PARA NO MARCAR ASIGN ARR = 5

                            asignacion = false;

                            emptyStacks();
                        } else
                            sem2.edo = null; // SEM2, PARA NO MARCAR ASIGN ARR = 5
                        
                        lexemaAsign = "";
                    break;

                    case 853:
                        negativo = true;
                    break;
                }
                    
        }
    }
    
    // Guarda datos para verif. asignacion de operacion normal
    public void saveLast() {
        oper2 = operStack.removeLast();
        oper1 = operStack.removeLast(); 
        
        op = opStack.removeLast();
        
        tipoOper1 = oper1.getTipo();
        tipoOper2 = oper2.getTipo();
        
        opToken = op.getToken();
        
        lexOp = op.getLexema();
        
        lexemaOper = oper1.getToken().getLexema() + " " + op.getLexema()
                + " " + oper2.getToken().getLexema();
        
        // Contador
        asign = oper1.getToken().getLexema() + " -> T" + oper2.getTipo();
        
        line = oper1.getToken().getLinea();
    }
    
    
    public String getTipo(String tipoSimbolos) {
        String tipo = null;
        
        switch(tipoSimbolos) {
            case "boolean":     tipo = "B";  break;
            case "caracter":    tipo = "CH"; break;
            case "cadena":      tipo = "C";  break;
            case "decimal":     tipo = "D";  break;
            case "flotante":    tipo = "F";  break;
            case "complejo":    tipo = "CM"; break;
            case "binario":     tipo = "DB"; break;
            case "hexadecimal": tipo = "DH"; break;
            case "octal":       tipo = "DO"; break;
            case "none":        tipo = "N";  break;
            case "struct": 
                switch(idsimbolos[1]) {
                    case "tupla":       tipo = "T";   break;
                    case "lista":       tipo = "L";   break;
                    case "arreglo":     tipo = "A";   break;
                    case "diccionario": tipo = "DIC"; break;
                }
            break;
            
            default: tipo = "X";
        }
        
        return tipo;
    }
    
    public String getTipo(int LT) {
        String tipo = null;
        
        switch(LT) {
            case -54: 
            case -55: tipo = "B";  break;   // Boolean
            case -56: tipo = "N";  break;   // None
            case -40: tipo = "C";  break;   // Cadena
            case -41: tipo = "CH"; break;   // Caracter
            case -45: tipo = "D";  break;   // Decimal
            case -46: tipo = "F";  break;    // Float
            case -47: tipo = "CM"; break;   // Complejo
            case -48: tipo = "DB"; break;   // Binario
            case -49: tipo = "DH"; break;   // Hexadecimal
            case -50: tipo = "DO"; break;   // Octal
            default:  tipo = "X";
            
        }
        
        return tipo;
    }
    
    
    public boolean identificador(int LT) {
        return LT == -44;
    }
    
    public void setIdentificador() {
        idsimbolos = amb.getIdSimbolos(token.getLexema());

        if(idsimbolos != null)
            operStack.offer(new Operando(token, getTipo(idsimbolos[0]), 
                    Integer.parseInt(idsimbolos[2]), idsimbolos));
        else 
            operStack.offer(new Operando(token, "V")); // Guardamos temp variant

    }
    
    public boolean operando(int LT) {
        if(LT >= -50 && LT <= -40) // C, CH, D, DF, CM, DB, DH, DO
            return true;
        
        if(LT >= -56 && LT <= -54) // none, true, false
            return true;
        
        return false;
    }
    
    public boolean operador(int LT) {
        if(negativo)
            return false;
        
        if(LT >= -37 && LT <= -7) 
                return true;
        
        if(LT >= -60 && LT <= -57) // is, isnot, in, innot
                return true;
        
        if(LT == -51) // ##
            return true;
        
        return false;
    }
    
    // ERRORES
    public void setError(String lexema) {
        amb.errores.add(new Error(750, token.getLinea(), lexema, "Incompatibilidad de tipos", "Semántica 1"));
        
        contador.addError();
    }

    // CONTADOR
    public ContSemantica getContador() {
        return contador;
    }
    
    
    // STACKS 
    public void emptyStacks() {
        operStack =  new LinkedList();
        
        opStack = new LinkedList();
        
    }
    
    public void printStacks() {
        System.out.println("\n*------------------------\n");
        System.out.println(" OPERSTACK:");
        
        for(Operando o : operStack) {
            if(o.getToken() != null)
                System.out.println(" "+o.getToken().getLexema()+" "+o.getTipo());
            else
                System.out.println(" T"+o.getTipo());
        }
        
        System.out.println("\n OPSTACK:");
        for(Token t : opStack) 
            System.out.println(" "+t.getLexema());
        
        System.out.println("\n------------------------*\n");
    }
    
    public void addSem2(Semantica2 sem2) {
        this.sem2 = sem2;
    }
}
