package Control.Semantica;

import Control.Ambito.Ambito;
import Control.Arreglos;
import Model.Operando;
import Model.Token;
import Model.Error;
import java.util.LinkedList;

public class Semantica1 {
    MatrizSemantica matriz;
    
    ContSemantica contador;
    
    public Ambito amb; // Para acceder a conexion, tokens y errores
    
    Semantica2 sem2;
    
    Arreglos arr;
    
    LinkedList<Operando> operStack;
    
    LinkedList<Token> opStack;
    
    public Semantica1(Ambito ambito) {
        this.amb = ambito;
        
        this.arr = new Arreglos(this);
        
        matriz = new MatrizSemantica();
        
        contador = new ContSemantica();
        
        operStack =  new LinkedList();
        
        opStack = new LinkedList();
        
        lexemaAsign = "";
    }
    
    Token token;
    
    String[] idsimbolos; // [0] tipo [1] clase [2] idsimbolos [3] tArr [4] dimArr [5] tipoLista
    
    public void checar(int LT) {
        if(identificador(LT)) {
            token = amb.tokens.peekFirst();
            
            setIdentificador();
            
            if(asignacion) 
                lexemaAsign += " " + token.getLexema();
            
        } else {
            if(isArr) {
                token = amb.tokens.peekFirst();

                if(operando(LT)) {
                    arr.setOper(token, LT);
                } else 
                    if(operador(LT))
                        arr.setOp(token);
            } else {
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
                        
           } 
            
        }
    }
    
    // @ ZONA

    Operando oper1, oper2; 

    String tipoOper1, tipoOper2, asign;
    
    String lexemaAsign, lexemaOper, lexOp;

    String tempTipo;

    Token op;

    int opToken, line;  
    
    public boolean asignacion, negativo, isArr, isFun;
    
    public void zona(int PS) {
        if(!amb.declaracion) {// SI ESTA EN EJECUCION
            if(isArr)
                arr.zona(PS);
            
            switch(PS) {
                case 844: 
                    System.out.println("\n@ " + PS); printStacks();

                    lexemaAsign = token.getLexema(); 
                break;
                
                case 845: 
                    System.out.println("\n@ " + PS); printStacks();
                    
                    isFun = true;
                break;
                
                case 846: 
                    System.out.println("\n@ " + PS); printStacks();
                    
                    isFun = false;
                break;

                case 850: // VERIF. OPERACIÓN
                    System.out.println("\n@ 850"); printStacks();

                    saveLast();

                    tempTipo = matriz.getType(tipoOper1, tipoOper2, opToken);

                    if (tempTipo.equals("error")) {

                        setError(lexemaOper);

                        tempTipo = "V"; // Marcamos Variant
                    }

                    if(isArr) {
                        arr.operStack.offer(new Operando(new Token(lexemaOper, line), tempTipo)); // Guardamos temp

                        System.out.println("\n++ SE CREO TEMP\n"); arr.printStacks();
                    } else {
                        operStack.offer(new Operando(new Token(lexemaOper, line), tempTipo)); // Guardamos temp

                        System.out.println("\n++ SE CREO TEMP\n"); printStacks();
                    }

                    contador.addTemp(tempTipo);
                break;

                case 851: 
                    asignacion = true;
                break;

                case 852: // VERIF. ASIGNACIÓN
                    if(asignacion) {
                        System.out.println("\n@ 852"); printStacks();

                        saveLast();
                        
                        sem2.regla1090(oper1); // TIPOS VALIDOS
                        
                        if(tipoOper1.equals("N"))
                            tempTipo = matriz.getType("V", tipoOper2, opToken);
                        else
                            tempTipo = matriz.getType(tipoOper1, tipoOper2, opToken);
                        
                        sem2.regla1170(oper1, tipoOper2); // CAMBIO DE VALOR
                        
                        if (tempTipo.equals("error")) {
                            setError(lexemaAsign);
                            
                            sem2.edo = "ERROR";
                            
                            if(lexOp.equals("="))
                                sem2.setError(1020, 761, oper1);
                            else
                                sem2.setError(1021, 761, oper1);
                                
                        } else
                            if(lexOp.equals("="))
                                sem2.setRegla(1020, oper1);
                            else
                                sem2.setRegla(1021, oper1);
                        

                        System.out.println("\nASIGN VERIFICADA " + tempTipo); printStacks();
                        
                        contador.addAsing(asign, line);

                        asignacion = false;

                        emptyStacks();
                    }

                    lexemaAsign = "";
                break;

                case 853:
                    negativo = true;
                break;

                case 860: 
                    System.out.println("\n@ " + PS); arr.printStacks();
                    isArr = true; 
                    
                    arr.setIdSimbolos(idsimbolos);
                break;

                case 861: 
                    System.out.println("\n@ " + PS); arr.printStacks();
                    isArr = false; 
                break;
            }  
        }
    }
    
    // Guarda datos para verif. asignacion de operacion normal
    public void saveLast() {
        if(isArr) {
            oper2 = arr.operStack.removeLast();
            oper1 = arr.operStack.removeLast(); 

            op = arr.opStack.removeLast();
        } else {
            oper2 = operStack.removeLast();
            oper1 = operStack.removeLast(); 

            op = opStack.removeLast();
        }
        
        tipoOper1 = oper1.getTipo();
        
        if(tipoOper1.equals("A"))
            tipoOper1 = getTipo(oper1.getSimbolos()[5]);
        
        tipoOper2 = oper2.getTipo();

        opToken = op.getToken();

        lexOp = op.getLexema();

        lexemaOper = oper1.getToken().getLexema() + " " + op.getLexema()
                + " " + oper2.getToken().getLexema();
        
        if(asignacion) {
            // Contador
            asign = oper1.getToken().getLexema() + " -> T" + oper2.getTipo();

            line = oper1.getToken().getLinea();
        }
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
        idsimbolos = amb.getIdSimbolos(token.getLexema()); // [0] tipo [1] clase [2] idsimbolos [3] tArr [4] dimArr [5] tipoLista
        
        if(isArr) {
            arr.setIdentificador(idsimbolos, token);
        } else {
            if(idsimbolos != null) { 
                operStack.offer(new Operando(token, getTipo(idsimbolos[0]), 
                    Integer.parseInt(idsimbolos[2]), idsimbolos));
            } else {
                operStack.offer(new Operando(token, "V")); // Guardamos temp variant
            }
        }
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
        arr.sem2 = sem2;
    }
}
