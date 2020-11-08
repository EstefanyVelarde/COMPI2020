package Control.Semantica;

import Control.Ambito.Ambito;
import Control.Arreglos;
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

    Operando oper1, oper2; 

    String tipoOper1, tipoOper2, asign;
    
    String lexemaAsign, lexemaOper, lexOp;

    String tempTipo;

    Token op;

    int opToken, line;  
    
    public boolean asignacion, negativo, isArr, isFun, isRang, isFor;
    
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
                break;

                
                // OPERACION
                case 850:
                    printZone(PS);

                    saveLast();

                    tempTipo = matriz.getType(tipoOper1, tipoOper2, opToken);

                    saveTemp();
                    
                    System.out.println("\n++ SE CREO TEMP\n"); printStacks();

                    
                    contador.addTemp(tempTipo);
                    
                    contador.addAsing(asign, line); // SE AGREGO EN S2 (?)
                break;
                

                // ASIGNACION
                case 851: 
                    asignacion = true;
                break;
                
                case 852: // VERIF. ASIGNACIÓN
                    if(asignacion) {
                        printZone(PS);

                        saveLast();
                        
                        tempTipo = matriz.getType(tipoOper1, tipoOper2, opToken);
                        
                        if (tempTipo.equals("error")) 
                            setError(lexemaAsign);
                        
                        sem2.regla1090(oper1); // TIPOS VALIDOS
                        
                        sem2.regla1020(oper1, oper2, lexOp, tempTipo); // ASIGNACION
                        
                        sem2.regla1170(oper1, tipoOper2); // CAMBIO DE VALOR
                        
                        System.out.println("\nASIGN VERIFICADA " + tempTipo); printStacks();
                        
                        contador.addAsing(asign, line);

                        asignacion = false;

                        emptyStacks();
                    }

                    lexemaAsign = "";
                break;

                
                // NEGATIVO 
                case 853:
                    printZone(PS);
                    
                    negativo = true;
                break;
                
                
                // ARREGLO []
                case 860: case 810:
                    printZone(PS);
                    
                    isArr = true; 
                    
                    arr.setIdSimbolos(idsimbolos);
                break;

                case 861: case 811:
                    printZone(PS);
                    
                    isArr = false; 
                break;
                
                
                // EST ;
                case 862: 
                    printZone(PS);
                    
                    sem2.printReglas();
                break;
                
                
                // FOR VALUE
                case 863:
                    printZone(PS);
                    
                    isFor = true;
                break;
                
                case 864: 
                    printZone(PS);
                    
                    sem2.regla1082(operStack.removeLast());
                    
                    isFor = false;
                break;
                
                
            }  
        }
    }
    
    public void printZone(int PS) {
        System.out.println("\n@ " + PS); printStacks();
    }
    
    // Guarda datos para verif. asignacion de operacion normal
    public void saveLast() {
        if(operStack.size() > 1) {
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
                        oper2 = operStack.removeLast();
                        oper1 = operStack.removeLast(); 

                        op = opStack.removeLast();
                    }
                }
            }

            tipoOper1 = oper1.getTipo();

            if(tipoOper1.equals("A"))
                tipoOper1 = getTipo(oper1.getSimbolos()[5]);

            tipoOper2 = oper2.getTipo();

            opToken = op.getToken();

            lexOp = op.getLexema();

            lexemaOper = oper1.getToken().getLexema() + " " + op.getLexema()
                    + " " + oper2.getToken().getLexema();
            
            if(asignacion)
                asign = oper1.getToken().getLexema() + " -> T" + oper2.getTipo();

            line = oper1.getToken().getLinea();
        } else {
            // CREAR VARIANTS?
        }
    }
    
    public void saveTemp() {
        if (tempTipo.equals("error")) {
            setError(lexemaOper);

            tempTipo = "V"; // Marcamos Variant
        }

        if(isArr) {
            arr.operStack.offer(new Operando(new Token(lexemaOper, line), tempTipo, true)); // Guardamos temp

        } else {
            if(isFun) {
                fun.operStack.offer(new Operando(new Token(lexemaOper, line), tempTipo, true)); // Guardamos temp
            } else {
                operStack.offer(new Operando(new Token(lexemaOper, line), tempTipo, true)); // Guardamos temp
            }
        }
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
                        operStack.offer(new Operando(token, getTipo(idsimbolos[0]), 
                            Integer.parseInt(idsimbolos[2]), idsimbolos));
                    } else {
                        operStack.offer(new Operando(token, "V")); // Guardamos temp variant
                    }
                }
            }
        }
    }
    
   
    // OPER & OP
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
            case "none":        tipo = "N";  break;
            case "struct": 
                switch(idsimbolos[1]) {
                    case "tupla":       tipo = "T";   break;
                    case "lista":       tipo = "L";   break;
                    case "arreglo":     tipo = "A";   break;
                    case "diccionario": tipo = "DIC"; break;
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

        System.out.println("\n*--------------empty----------*\n");
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
        fun.sem2 = sem2;
        rang.sem2 = sem2;
    }
}
