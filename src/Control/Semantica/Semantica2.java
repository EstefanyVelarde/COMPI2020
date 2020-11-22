package Control.Semantica;

import Control.Ambito.Ambito;
import Model.Operando;
import Model.Regla;
import Model.Error;
import Model.Token;
import java.util.LinkedList;


public class Semantica2 {
    Semantica1 sem1;
    
    ContSemantica2 contador;
    
    Ambito ambito; // Para acceder a conexion, tokens y errores
     
    LinkedList<Regla> listaReglas;
    
    public Semantica2(Ambito ambito, Semantica1 sem1) {
        this.ambito = ambito;
        
        this.sem1 = sem1;
        
        contador = new ContSemantica2();
        
        listaReglas = new LinkedList();
        
        this.edo = "Acepta";
        
        tempCont = new int[15];
    }
    
    String edo;
    
    public void zona(int PS) {
        switch(PS) {
            case 1009: // IF, ELIF, WHILE
                sem1.isIf = true;
            break;
            case 1010: case 1011: case 1012: // IF, ELIF, WHILE
                sem1.printZone(PS);
                
                regla1010(PS);
                
                sem1.isIf = false;
                
                printReglas();
            break;
            
            case 1140: // RETURN
                sem1.printZone(PS);
                
                regla1140();
                
                
                printReglas();
            break;
            
            case 1150: // NO RETURN ?
                sem1.printZone(PS);
                
                if(ambito.lastFuncionId != null)
                    setRegla(1150, "fun", ambito.lastFuncionId, 
                            ambito.lastFuncionToken.getLinea(), "Acepta");
                
                printReglas();
            break;
            
        }
        
    }
      
    Operando oper; 

    String topePila, valorReal, lexOper;
    
    int line, amb, tipoToken;
    
    public void saveLast() {
        
        amb = ambito.ambStack.peekLast();
        
        if(sem1.operStack.isEmpty()) { // OPERSTACK EMPTY (SUCEDIO ASIGN U OPERACION)
            valorReal = sem1.oper1.getToken().getLexema();
            
            topePila = getTipo(sem1.oper1.getTipo());
            
            line = sem1.line;
            
        } else {
            sem1.printStacks();

            oper = sem1.operStack.peekLast(); 
            
            valorReal = getValorReal(oper);
            
            topePila = getTipo(oper.getTipo());

            line = oper.getToken().getLinea();
        }
        
    }
    
    public String getValorReal(Operando oper) {
        String valorReal = "";
        
        if(oper.isTemp())
                valorReal = getTemp(oper.getTipo(), oper.getNoTemp());
            else
                valorReal = oper.getLex();
        
        return valorReal;
    }
    
    public String getValorReal(Operando oper, String tempTipo) {
        String valorReal = "";
        
        if(oper.isTemp())
                valorReal = valorReal = getTemp(tempTipo);
            else
                valorReal = oper.getLex();
        
        return valorReal;
    }
    
    public String getTemp(String tipo) {
        String temp =  "T" + tipo;
        
        return temp + getNoTemp(tipo);
    }
    
    public String getTemp(String tipo, int noTemp) {
        String temp =  "T" + tipo + noTemp;
        
        return temp;
    }
    
    
    // REGLAS
    public void regla1010(int PS) {
        saveLast();
                
        if(oper != null) {
            if(oper.getTipo().equals("B"))
                edo = "Acepta";
            else {
                setError(760, line, valorReal);
            }

            setRegla(PS, topePila, valorReal, line, edo);
        }
        sem1.emptyStacks();
    }
    
    public void regla1020(Operando oper1, Operando oper2, String lexOp, String tempTipo) { // ASIGNACION
        int regla = 1020;
        
        if(oper1 != null && oper2 != null) {
            if (tempTipo.equals("error")) {
                if(!lexOp.equals("="))
                    regla = 1021;

                topePila = getTipo(oper1.getTipo());

                valorReal = getValorReal(oper2, tempTipo);

                line = oper1.getToken().getLinea();

                setError(regla, 761, topePila, valorReal, line);
            } else {
                if(!lexOp.equals("="))
                    regla = 1021;

                topePila = getTipo(oper1.getTipo());

                valorReal = getValorReal(oper2, tempTipo);

                line = oper1.getToken().getLinea();

                setRegla(regla, topePila, valorReal, line);
            }
        }
    }
    
    public void regla1031(Token token, int num1, int num2) {
        if(token != null) {
            if(num1 > num2)
                setError(1031, 763, "decimal", num2 + "", token.getLinea());
            else
                setRegla(1031, "decimal", num2 + "", token.getLinea(), "Acepta");
        }
        printReglas();
    }
    
    
    public void regla1031(Token token, int num1, int num2, int num3) {
        if(token != null) {
            if(num1 > num2) {
                if(num3 < 0)
                    setRegla(1031, "decimal", num3 + "", token.getLinea(), "Acepta");
                else
                    setError(1031, 763, "decimal", num3 + "", token.getLinea());
            } else {
                if(num3 > 0)
                    setRegla(1031, "decimal", num3 + "", token.getLinea(), "Acepta");
                else
                    setError(1031, 763, "decimal", num3 + "", token.getLinea());

            }
        }
        printReglas();
    }
    
    public void regla1060(Token token) {
        if(token != null) 
            setRegla(1060, getTipo(sem1.getTipo(token.getToken())), token.getLexema(), token.getLinea(), "Acepta");
    }
    
    public void regla1080(Token token) {
        if(token != null) 
            setRegla(1080, "id", token.getLexema(), token.getLinea(), "Acepta");
    }
    
    public void regla1081(Token token) {
        if(token != null) 
            setRegla(1081, "id", token.getLexema(), token.getLinea(), "Acepta");
    }
    
    public void regla1082(Operando dato) {
        if(dato != null) {
            if(dato.isArr() || dato.isRange() || dato.getTipo().equals("A") 
                || dato.getTipo().equals("T") || dato.getTipo().equals("L") 
                || dato.getTipo().equals("DIC") || dato.getTipo().equals("C")
                     || dato.getTipo().equals("V"))
                setRegla(1082, getTipo(dato.getTipo()), dato.getLex(), 
                        dato.getToken().getLinea(), "Acepta");
            else
                setError(1082, 772, getTipo(dato.getTipo()), dato.getLex(), 
                        dato.getToken().getLinea());
        }
        
    }
    
    public void regla1090(Operando dato) {
        if(dato != null) {
            if(dato.getSimbolos() != null) {
                String clase = dato.getSimbolos()[1];

                switch(clase) {
                    case "var": case "par": case "lista": case "arreglo": 
                    case "diccionario": case "rango": case "tupla": 
                        setRegla(1090, clase, dato.getLex(), 
                                dato.getToken().getLinea(), "Acepta"); break;
                    default: 
                        setError(1090, 773,  "id", dato.getLex(), 
                                dato.getToken().getLinea());
                }
            } else
                setError(1090, 773,  "id", dato.getLex(), 
                        dato.getToken().getLinea());
        }
        printReglas();
        
    }
    
    public void regla1110(Operando dato) {
        
        if(dato != null) {
            if(dato.getSimbolos() != null) {
                String tipo = dato.getSimbolos()[0];

                if(dato.isFun()) {
                    if(tipo.equals("none")) // TIPO == NONE
                        setRegla(1110, tipo, dato.getLex(), 
                                dato.getToken().getLinea(), "Acepta");
                    else
                        setError(1110, 775,  tipo, dato.getLex(), 
                                dato.getToken().getLinea());
                }
            } else 
                if(dato.isFun()) // SI NO ESTA DECLARADA
                    setError(1110, 775,  dato.getTipo(), dato.getLex(), 
                                dato.getToken().getLinea());

        }
        printReglas();
    }
    
    public void regla1120(Operando dato) {
        if(dato != null) {
            if(dato.getSimbolos() != null) {
                String tipo = dato.getSimbolos()[0];

                if(dato.isFun()) {
                    if(!tipo.equals("none")) // TIPO != NONE
                        setRegla(1120, tipo, dato.getLex(), 
                                dato.getToken().getLinea());
                    else
                        setError(1120, 776,  tipo, dato.getLex(), 
                                dato.getToken().getLinea());
                }
            } else 
                if(dato.isFun())
                    setError(1120, 776,  dato.getTipo(), dato.getLex(), 
                                dato.getToken().getLinea());

        }
        printReglas();
    }
    
    public void setFun(Operando dato) {
        if(dato != null) 
            dato.setFun(true);
    }
    
    public void regla1130(String[] idsimbolos, Token token) {
        
        if(token != null) {
            if(idsimbolos == null) {
                setError(1130, 777, "id", token.getLexema(), token.getLinea());
            } else
                setRegla(1130, "id", token.getLexema(), token.getLinea(), "Acepta");
        }
        printReglas();
    }
    
    public void regla1140() {
        Operando dato = sem1.getLastOper(null);
                
        if(dato != null) {
            ambito.cambioReturn(ambito.lastFuncionId, getTipo(dato.getTipo()));

            setRegla(1140, dato);
        }
    }
    
    public void regla1160(Token token, int num1, int num2, int num3) {
        
        if(token != null) {
            if(num1 > num2) { // Rango debe ser negativo
                if(num3 < 0)
                    setRegla(1161, "decimal", num3 + "", token.getLinea(), "Acepta");
                else
                    setError(1161, 779, "decimal", num3 + "", token.getLinea());
            } else { // Rango positivo
                if(num3 > 0)
                    setRegla(1160, "decimal", num3 + "", token.getLinea(), "Acepta");
                else
                    setError(1160, 778, "decimal", num3 + "", token.getLinea());

            }
        }
        printReglas();
    }
    
    public void regla1170(Operando dato, String temp) {
        if(dato != null) {
            if(dato.getSimbolos() != null) {
                int id = dato.getIdsimbolos();
                String clase = dato.getSimbolos()[1];
                String tipo = dato.getTipo();

                if(clase.equals("par")) {
                    dato.setTipo(temp);

                    setRegla(1170, dato);

                    ambito.cambioValor(id, getTipo(temp));
                }
            }  
        }
        printReglas();
    }
    
    
    // SET REGLAS
    
    public void setRegla(int id) {
        listaReglas.add(new Regla(id, topePila, valorReal, line, edo, amb));
            
        
    }
    
    public void setRegla(int id, String topePila, String valorReal, int line, String edo) {
        listaReglas.add(new Regla(id, topePila, valorReal, line, edo, ambito.ambStack.peekLast()));
            
    }
    
    public void setRegla(int id, String topePila, String valorReal, int line) {
        listaReglas.add(new Regla(id, topePila, valorReal, line, "Acepta", ambito.ambStack.peekLast()));
            
    }
    
    public void setRegla(int regla, Operando dato) {
        if(dato != null) 
            setRegla(regla, getTipo(dato.getTipo()), dato.getLex(), 
                dato.getToken().getLinea(), "Acepta");
        
    }
    
    
    // SET ERRORES
    public void setError(int error) {
        this.edo = "Error";
        
        ambito.errores.add(new Error(error, line, valorReal, desc[error-760], "Semántica 2"));
    }
    
    public void setError(int error, int line, String lexema) {
        this.edo = "Error";
        
        ambito.errores.add(new Error(error, line, lexema, desc[error-760], "Semántica 2"));
    }
    
    public void setError(int regla, int error, Operando dato) {
        
        if(dato != null) {
            setRegla(regla, getTipo(dato.getTipo()), dato.getLex(), 
                    dato.getToken().getLinea(), "Error");
            setError(error, dato.getToken().getLinea(), dato.getLex());
        }
    }
    
    public void setError(int regla, int error, String topePila, 
            String valorReal, int line) {
        setRegla(regla, topePila, valorReal,  line, "Error");
        setError(error, line, valorReal);
    }
   
    String desc[] = { 
        "La EXP tiene que ser Booleana",
        "ID debe ser mismo tipo que EXP",
        "La cant. de dimensiones utilizadas debe ser igual a la declarada",
        "La organizacion de los datos de inicializacion de una lista tipo arr deben tener un orden",
        "La EXP debe ser entera",
        "Fuera de limite",
        "Diferente valor de llave",
        "Los diccionarios no tienen posiciones negativas",
        "Fuera de dimension",
        "Las tuplas no tienen posiciones negativas",
        "for",
        "id for",
        "Valor invalido",
        "ID debe ser var, par, arr, list, dicc, rang para recibir valor",
        "La cant. de parametros debe ser igual a la declarada",
        "ID debe ser clase procedimiento",
        "ID debe ser clase funcion",
        "ID debe ser declarado antes de ser utilizado",
        "Se esperaba rango positivo",
        "Se esperaba rango negativo"
    };
    
    
    // LISTA REGLAS
    public void printReglas() {
        System.out.println("\n//////////////// SEM 2 /////////////////");
        System.out.println("REGLA\tTOPE\tVALOR\tLINEA\tEDO\tAMB");
        
        for (Regla regla : listaReglas) {
            System.out.println(regla.getId() + "\t" + regla.getTopePila() + "\t" + regla.getValorReal() +
                    "\t" + regla.getLinea() + "\t" + regla.getEdo() +
                    "\t" + regla.getAmbito());
        }
        
    }
    
    
    // TIPOS 
    public String getTipo(String tipoSimbolos) {
        String tipo = null;
        
        switch(tipoSimbolos) {
            case "B":   tipo = "boolean";       break;
            case "CH":  tipo = "caracter";      break;
            case "C":   tipo = "cadena";        break;
            case "D":   tipo = "decimal";       break;
            case "F":   tipo = "flotante";      break;
            case "CM":  tipo = "complejo";      break;
            case "DB":  tipo = "binario";       break;
            case "DH":  tipo = "hexadecimal";   break;
            case "DO":  tipo = "octal";         break;
            case "N":   tipo = "none";          break;
            case "T":   tipo = "tupla";         break;
            case "L":   tipo = "lista";         break;
            case "A":   tipo = "arreglo";       break;
            case "DIC": tipo = "diccionario";   break;
            case "V":   tipo = "variant";       break;
                
            default: tipo = tipoSimbolos;
        }
        
        return tipo;
    }
    
    int[] tempCont;
    
    public int getNoTemp(String tipo) {
        int no = 0;
        
        switch(tipo) {
            case "B":   no = 1 + tempCont[0]++; break;
            case "CH":  no = 1 + tempCont[1]++; break;
            case "C":   no = 1 + tempCont[2]++; break;
            case "D":  System.out.println("\n\nNOTEMP DDDDD\n\n"); no = 1 + tempCont[3]++; break;
            case "F":   no = 1 + tempCont[4]++; break;
            case "CM":  no = 1 + tempCont[5]++; break;
            case "DB":  no = 1 + tempCont[6]++; break;
            case "DH":  no = 1 + tempCont[7]++; break;
            case "DO":  no = 1 + tempCont[8]++; break;
            case "N":   no = 1 + tempCont[9]++; break;
            case "T":   no = 1 + tempCont[10]++; break;
            case "L":   no = 1 + tempCont[11]++; break;
            case "A":   no = 1 + tempCont[12]++; break;
            case "DIC": no = 1 + tempCont[13]++; break;
            case "error": case "V": 
                        no = 1 + tempCont[14]++; break;
        }
        
        return no;
    }
    
    
    // CONTADOR
    public ContSemantica2 getContador() {
        return contador;
    }
    
    public void setContador(int contAmb) {
        contador.listaReglas = this.listaReglas;
        
        contador.setAparicion();
        
        contador.setAmb(contAmb);
    }
 }
