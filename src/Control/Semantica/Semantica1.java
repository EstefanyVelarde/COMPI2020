package Control.Semantica;

import Control.Ambito.Ambito;
import Control.Arreglos;
import Control.Cuadruplos.Cuadruplos;
import Control.Funciones;
import Control.Rangos;
import Model.Operando;
import Model.Token;
import Model.Error;
import java.util.LinkedList;

public class Semantica1 {
    MatrizSemantica matriz;
    
    ContSemantica1 contador;
    
    public Ambito amb; // Para acceder a conexion, tokens y errores
    
    Semantica2 sem2;
    
    public Cuadruplos cuad;
    
    Arreglos arr;
    
    Funciones fun;
    
    Rangos rang;
    
    public LinkedList<Operando> operStack;
    
    LinkedList<Token> opStack;
    
    public Semantica1(Ambito ambito) {
        this.amb = ambito;
        
        this.arr = new Arreglos(this);
        
        this.fun = new Funciones(this);
        
        this.rang = new Rangos(this);
        
        matriz = new MatrizSemantica();
        
        contador = new ContSemantica1();
        
        operStack =  new LinkedList();
        
        opStack = new LinkedList();
        
        lexemaAsign = "";
    }
    
    public Token token;
    
    String[] idsimbolos; // [0] tipo [1] clase [2] idsimbolos [3] tArr [4] dimArr [5] tipoLista
    
    public void checar(int LT) {
        if(identificador(LT)) {
            token = amb.tokens.peekFirst();
            
            setIdentificador();
            
            if(asignacion) 
                lexemaAsign += " " + token.getLexema();
            
        } else {
            token = amb.tokens.peekFirst();
            
            // OPERANDO || OPERADOR
            if(operando(LT)) {
                setOper(token, LT);
            } else {
                if(operador(LT)) {
                    setOp(token);
                } 
            }
            
        }
    }
    
    
    // @ ZONA

    public Operando oper1, oper2; 

    public String tipoOper1, tipoOper2, asign;
    
    public String lexemaAsign, lexemaOper, lexOp;

    public String tempTipo;

    public Token op;

    int opToken, line;  
    
    public boolean asignacion, negativo;
    
    public boolean isIf, isArr, isFun, isRang, isFor, isFunNoExplicada;
    
    public void zona(int PS) {
        if(!amb.declaracion) {// SI ESTA EN EJECUCION
            if(isArr)
                arr.zona(PS);
            
            if(isFun)
                fun.zona(PS);
            
            if(isRang)
                rang.zona(PS);
            
            switch(PS) {
                // RANGO
                case 812:
                    printZone(PS);
                    
                    isRang = true;
                break;
                
                case 816:
                    printZone(PS);
                    
                    isRang = false;
                break;
                
                
                // ID
                case 844:
                    printZone(PS);

                    lexemaAsign = token.getLexema(); 
                break;
                
                
                // FUN()
                case 845:
                    printZone(PS);
                    
                    isFun = true;
                    
                    sem2.setFun(operStack.peekLast());
                    
                    fun.idsimbolos = idsimbolos;
                break;
                
                case 846: 
                    printZone(PS);
                    
                    isFun = false;
                    
                    isFunNoExplicada = false;
                break;

                
                // OPERACION
                case 850:
                    printZone(PS);

                    saveLast();
                    
                    if(notNull(oper1) && notNull(oper1) && notNull(op)) {
                        tempTipo = matriz.getType(tipoOper1, tipoOper2, opToken);

                        Operando tempOper = saveTemp();
                        
                        cuad.pushOperacion(op, oper1, oper2, tempTipo, tempOper);

                        System.out.println("\n++ SE CREO TEMP\n"); printStacks();

                        contador.addTemp(tempTipo);

                        if(isIf || isArr || isFun || isRang || isFor)
                            contador.addAsing(asign, line); // SE AGREGO EN S2 (?)

                        System.out.println("\nOPERACION VERIFICADA " + tempTipo);
                    }
                break;
                

                // ASIGNACION
                case 851: 
                    asignacion = true;
                    
                    System.out.println("\n\n   INICIO ASIGNACIOOOOON    \n\n");
                    
                    sem2.regla1090(operStack.peekLast()); // TIPOS VALIDOS
                break;
                
                case 852: // VERIF. ASIGNACIÓN
                    if(asignacion) {
                        printZone(PS);
                        
                        saveLast();
                        
                        if(notNull(oper1) && notNull(oper1) && notNull(op)) {
                            tempTipo = matriz.getType(tipoOper1, tipoOper2, opToken);

                            if (tempTipo.equals("error")) 
                                setError(lexemaAsign);

                            sem2.regla1020(oper1, oper2, lexOp, tempTipo); // ASIGNACION

                            sem2.regla1170(oper1, tipoOper2); // CAMBIO DE VALOR

                            System.out.println("\nASIGN VERIFICADA " + tempTipo); 

                            System.out.println("\nASIGN VERIFICADA " + tipoOper1 +" " +  tipoOper2); 
                            printStacks();

                            contador.addAsing(asign, line);
                        
                        }
                        emptyStacks();
                    }

                    lexemaAsign = "";
                    
                    asignacion = false;
                    
                    System.out.println("\n\n   fin ASIGNACIOOOOON    \n\n");
                    
                break;

                
                // NEGATIVO 
                case 853:
                    printZone(PS);
                    
                    negativo = true;
                break;
                
                
                // ARREGLO []
                case 860: case 810:
                    arr.printZone(PS);
                    
                    isArr = true; 
                    
                    arr.setIdSimbolos(idsimbolos);
                    
                    arr.zona(PS);
                break;

                case 861: case 811:
                    arr.printZone(PS);
                    
                    isArr = false; 
                break;
                
                
                // EST ;
                case 862: 
                    printZone(PS);
                    
                    sem2.printReglas();
                    
                    asignacion = false;
                    this.emptyStacks();
                break;
                
                
                // FOR VALUE
                case 863:
                    printZone(PS);
                    
                    isFor = true;
                break;
                
                case 864: 
                    printZone(PS);
                    
                    Operando forVal = operStack.removeLast();
                    
                    sem2.regla1082(forVal);
                    
                    cuad.setForVal(forVal);
                    
                    isFor = false;
                break;
                
                
            }  
        }
    }
    
    public void printZone(int PS) {
        System.out.println("\n SEM1 @ " + PS); printStacks();
    }
    
    // Guarda datos para verif. asignacion de operacion normal
    public void saveLast() {
        
            if(isArr) {
                oper2 = arr.operStack.removeLast();
                oper1 = arr.operStack.removeLast(); 
                
                op = arr.opStack.removeLast();
            } else {
                if(isFun) {

                    oper2 = fun.operStack.removeLast();
                    oper1 = fun.operStack.removeLast(); 

                    op = fun.opStack.removeLast();

                } else {

                    if(isRang) {

                        oper2 = rang.operStack.removeLast();
                        oper1 = rang.operStack.removeLast(); 

                        op = rang.opStack.removeLast();

                    } else {
                        if(operStack.size() > 1) {
                            oper2 = operStack.removeLast();
                            oper1 = operStack.removeLast(); 
                            
                            if(opStack.size() > 0)
                                op = opStack.removeLast();
                            else {
                                op = null;
                            }
                        } else {
                            oper2 = null;

                            oper1 = null;
                            
                        }
                    }
                }
            }
            
            
            if(notNull(oper1) && notNull(oper1) && notNull(op)) {
                tipoOper1 = oper1.getTipo();

                tipoOper2 = oper2.getTipo();

                opToken = op.getToken();

                lexOp = op.getLexema();

                lexemaOper = oper1.getToken().getLexema() + " " + op.getLexema()
                        + " " + oper2.getToken().getLexema();

                if(asignacion)
                    asign = oper1.getToken().getLexema() + " -> T" + oper2.getTipo();
                else 
                    asign = "";

                line = oper1.getToken().getLinea();
            }
    }
    
    
    
    public Operando saveTemp() {
        Operando tempOper;
        
        if (tempTipo.equals("error")) {
            setError(lexemaOper);

            tempTipo = "V"; // Marcamos Variant
        }
        
        
        tempOper = new Operando(new Token(lexemaOper, line), 
                    tempTipo, true, sem2.getNoTemp(tempTipo));

        if(isArr) {
            
            arr.operStack.offer(tempOper); // Guardamos temp

        } else {
            if(isFun) {
                fun.operStack.offer(tempOper); // Guardamos temp
            } else {
                operStack.offer(tempOper); // Guardamos temp
            }
        }
        
        return tempOper;
    }
    
    public Operando saveTemp(String lexemaOper, int line, String tempTipo) {
        if (tempTipo.equals("error")) {
            setError(lexemaOper);

            tempTipo = "V"; // Marcamos Variant
        }
        
        Operando newOper = new Operando(new Token(lexemaOper, line), 
                    tempTipo, true, sem2.getNoTemp(tempTipo));

        if(isArr) {
            arr.operStack.offer(newOper); // Guardamos temp

        } else {
            if(isFun) {
                fun.operStack.offer(newOper); // Guardamos temp
            } else {
                operStack.offer(newOper); // Guardamos temp
            }
        }
        
        return newOper;
    }
    
    // IDENTIFICADOR
    public boolean identificador(int LT) {
        return LT == -44;
    }
    
    public void setIdentificador() {
        idsimbolos = amb.getIdSimbolos(token.getLexema()); // [0] tipo [1] clase [2] idsimbolos [3] tArr [4] dimArr [5] tipoLista
        
        sem2.regla1130(idsimbolos, token);
        
        if(isArr) {
            arr.setIdentificador(idsimbolos, token);
        } else {
            if(isFun) {
                fun.setIdentificador(idsimbolos, token);
            } else {
                if(isRang) {
                    rang.setIdentificador(idsimbolos, token);
                } else {
                    if(idsimbolos != null) { 
                        int idSimbolos = Integer.parseInt(idsimbolos[2]);
                        
                        operStack.offer(new Operando(token, getTipo(idsimbolos[0]), 
                            idSimbolos, idsimbolos));
                    } else {
                        operStack.offer(new Operando(token, "V")); // Guardamos temp variant
                    }
                }
            }
        }
    }
    
   
    // OPER & OP
    public Operando getLastOper(Token op) {
        Operando oper;
        
        if(operStack.size() > 0) 
            oper = operStack.removeLast();
        else {
            Token tokenTemp;
            
            if(op != null)
                tokenTemp = new Token(op.getLexema(), op.getLinea());
            else
                tokenTemp = new Token("V", 1);
            
            oper =  new Operando(tokenTemp, "V", true, sem2.getNoTemp("V"));
            
        }
        
        return oper;
    }
    
    public Operando peekLastOper(Token op) {
        Operando oper;
        
        if(operStack.size() > 0) 
            oper = operStack.peekLast();
        else {
            Token tokenTemp;
            
            if(op != null)
                tokenTemp = new Token(op.getLexema(), op.getLinea());
            else
                tokenTemp = new Token("V", 1);
            
            oper =  new Operando(tokenTemp, "V", true, sem2.getNoTemp("V"));
            
        }
        
        return oper;
    }
    
    public Token getLastOp() {
        Token op;
        
        if(opStack.size() > 0) 
            op = opStack.removeLast();
        else
            op = new Token("V", 1);
        
        return op;
    }
    
    public void setOper(Token token, int LT) {
        if(isArr)
            arr.setOper(token, LT);
        else {
            if(isFun)
                fun.setOper(token, LT);
            else {
                if(isRang)
                    rang.setOper(token, LT);
                else {
                    if(negativo) {
                        operStack.offer(new Operando(new Token(token.getToken(), 
                                token.getLinea(), "-"+token.getLexema()), getTipo(LT)));

                        negativo = false;
                    } else
                        operStack.offer(new Operando(token, getTipo(LT)));

                        lexemaAsign += " " + token.getLexema();
                }
            }
        }
    }
    
    public void setOper(Operando oper) {
        operStack.offer(oper);

        lexemaAsign += " " + token.getLexema();
    }
    
    public void setOp(Token token) {
        if(isArr)
            arr.setOp(token);
        else {
            if(isFun)
                fun.setOp(token);
            else {
                if(isRang)
                    rang.setOp(token);
                else {
                    opStack.offer(token);

                    lexemaAsign += " " + token.getLexema();
                }
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
    
    
    
    // TIPOS
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
            case "none":        tipo = "N"; contador.addTemp("V"); break; // None
            //case "none":        tipo = "V"; contador.addTemp(tipo); break; // None
            case "struct": 
                switch(idsimbolos[1]) {
                    case "tupla":       tipo = "T";   break;
                    case "lista":       tipo = "L";   break;
                    case "arreglo":     tipo = "A";   break;
                    case "diccionario": tipo = "DIC"; break;
                    case "rango":       tipo = "R"; break;
                }
            break;
            
            default: tipo = tipoSimbolos;
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
            case -46: tipo = "F";  break;   // Float
            case -47: tipo = "CM"; break;   // Complejo
            case -48: tipo = "DB"; break;   // Binario
            case -49: tipo = "DH"; break;   // Hexadecimal
            case -50: tipo = "DO"; break;   // Octal
            case -56: tipo = "V"; contador.addTemp(tipo); break; // None
            default:  tipo = "X";
            
        }
        
        return tipo;
    }
    
    // ERRORES
    public void setError(String lexema) {
        amb.errores.add(new Error(750, token.getLinea(), lexema, "Incompatibilidad de tipos", "Semántica 1"));
        
        contador.addError();
    }

    // CONTADOR
    public ContSemantica1 getContador() {
        return contador;
    }
    
    
    // STACKS 
    public void emptyStacks() {
        operStack =  new LinkedList();
        
        opStack = new LinkedList();

        System.out.println("\n*-------------- sem1 empty stacks ----------*\n");
    }
    
    public void printStacks() {
        System.out.println("\n*------------------------\n");
        System.out.println(" OPERSTACK:");
        
        for(Operando o : operStack) {
            if(!o.isTemp())
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
        fun.sem2 = sem2;
        rang.sem2 = sem2;
    }
    
    public boolean isInteger(String numero){
        try{
            Integer.parseInt(numero);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
    
    public int getInteger(String num) {
        if(isInteger(num))
            return Integer.parseInt(num);
        else
            return -1;
    }
    
    public boolean notNull(Object dato) {
        return dato != null;
    }
}
