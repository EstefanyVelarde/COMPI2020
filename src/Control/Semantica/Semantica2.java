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
    }
    
    String edo;
    
    public void zona(int PS, int LT) {
        switch(PS) {
            case 1010: case 1011: case 1012: // IF, ELIF, WHILE
                System.out.println("\n@ " + PS); sem1.printStacks();
                
                saveLast();
                
                if(oper.getTipo().equals("B"))
                    edo = "Acepta";
                else {
                    setError(760, line, valorReal);
                }
                
                setRegla(PS, topePila, valorReal, line, edo);
                
                sem1.emptyStacks();
            break;
            
            case 1140: // RETURN
                System.out.println("\n@ " + PS); sem1.printStacks();
                
                Operando dato = sem1.operStack.removeLast();
                
                ambito.cambioReturn(ambito.lastFuncionId, dato.getTipo());
                
                setRegla(1140, dato);
            break;
            
            case 1150: // NO RETURN ?
                System.out.println("\n@ " + PS); sem1.printStacks();
                
                if(ambito.lastFuncionId != null)
                    setRegla(1150, "fun", ambito.lastFuncionId, 
                            ambito.lastFuncionToken.getLinea(), "Acepta");
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

            valorReal = oper.getToken().getLexema();
            
            topePila = getTipo(oper.getTipo());

            line = oper.getToken().getLinea();
        }
        
    }
    
    
    // REGLAS
    public void regla1031(Token token, int num1, int num2) {
        if(num1 > num2)
            setError(1031, 763, "decimal", num2 + "", token.getLinea());
        else
            setRegla(1031, "decimal", num2 + "", token.getLinea(), "Acepta");
        
        printReglas();
    }
    
    
    public void regla1031(Token token, int num1, int num2, int num3) {
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
        
        printReglas();
    }
    
    public void regla1081(int line) {
        setRegla(1081, "id", "TV", line, "Acepta");
    }
    
    public void regla1082(Operando dato) {
        if(dato.isArr() || dato.isRange() || dato.getTipo().equals("A") 
                || dato.getTipo().equals("T") || dato.getTipo().equals("L") 
                || dato.getTipo().equals("DIC") || dato.getTipo().equals("C"))
            setRegla(1082, dato.getTipo(), dato.getLex(), dato.getToken().getLinea(), "Acepta");
        else
            setError(1082, 772, dato.getTipo(), dato.getLex(), dato.getToken().getLinea());
    }
    
    public void regla1090(Operando dato) {
        if(dato.getSimbolos() != null) {
            String clase = dato.getSimbolos()[1];
        
            switch(clase) {
                case "var": case "par": case "lista": case "arreglo": 
                case "diccionario": case "rango": case "tupla": 
                    setRegla(1090, "Identificador", dato.getLex(), 
                            dato.getToken().getLinea(), "Acepta"); break;
                default: 
                    setError(1090, 773,  "Identificador", dato.getLex(), 
                            dato.getToken().getLinea());
            }
        } else
            setError(1090, 773,  "Identificador", dato.getLex(), 
                    dato.getToken().getLinea());
        printReglas();
    }
    
    public void regla1110(Operando dato) {
        if(dato.getSimbolos() != null) {
            String clase = dato.getSimbolos()[1];
            
            if(dato.isFun()) {
                if(dato.getSimbolos()[7] == null)
                    setRegla(1110, clase, dato.getLex(), 
                            dato.getToken().getLinea(), "Acepta");
                else
                    setError(1110, 775,  clase, dato.getLex(), 
                            dato.getToken().getLinea());
            }
        } else 
            if(dato.isFun())
                setError(1110, 775,  dato.getTipo(), dato.getLex(), 
                            dato.getToken().getLinea());
        
            
        printReglas();
    }
    
    public void regla1120(Operando dato) {
        if(dato.getSimbolos() != null) {
            String clase = dato.getSimbolos()[1];
            
            if(dato.isFun()) {
                if(dato.getSimbolos()[7] != null)
                    setRegla(1120, clase, dato.getLex(), 
                            dato.getToken().getLinea(), "Acepta");
                else
                    setError(1120, 776,  clase, dato.getLex(), 
                            dato.getToken().getLinea());
            }
        } else 
            if(dato.isFun())
                setError(1120, 776,  dato.getTipo(), dato.getLex(), 
                            dato.getToken().getLinea());
        
            
        printReglas();
    }
    
    public void setFun(Operando dato) {
        dato.setFun(true);
    }
    
    public void regla1130(String[] idsimbolos, Token token) {
        if(idsimbolos == null) {
            setError(1130, 777, "Identificador", token.getLexema(), token.getLinea());
        } else
            setRegla(1130, "Identificador", token.getLexema(), token.getLinea(), "Acepta");
        
        printReglas();
    }
    
    public void regla1160(Token token, int num1, int num2, int num3) {
        if(num1 > num2) { // Rango negativo
            if(num3 < 0)
                setRegla(1161, "decimal", num3 + "", token.getLinea(), "Acepta");
            else
                setError(1161, 778, "decimal", num3 + "", token.getLinea());
        } else { // Rango positivo
            if(num3 > 0)
                setRegla(1160, "decimal", num3 + "", token.getLinea(), "Acepta");
            else
                setError(1160, 779, "decimal", num3 + "", token.getLinea());
            
        }
        
        printReglas();
    }
    
    public void regla1170(Operando dato, String temp) {
        if(dato.getSimbolos() != null) {
            int id = dato.getIdsimbolos();
            String clase = dato.getSimbolos()[1];
            String tipo = dato.getTipo();

            if(tipo.equals("N") && clase.equals("par")) {
                dato.setTipo(temp);

                setRegla(1170, dato);

                ambito.cambioValor(id, getTipo(temp));
            }
        }  
        
        printReglas();
    }
    
    public void setRegla(int id) {
        listaReglas.add(new Regla(id, topePila, valorReal, line, edo, amb));
            
        
    }
    
    public void setRegla(int id, String topePila, String valorReal, int line, String edo) {
        listaReglas.add(new Regla(id, topePila, valorReal, line, edo, ambito.ambStack.peekLast()));
            
    }
    
    public void setRegla(int regla, Operando dato) {
        setRegla(regla, getTipo(dato.getTipo()), dato.getLex(), 
                dato.getToken().getLinea(), "Acepta");
        
    }
    
    
    // ERRORES
    public void setError(int error) {
        this.edo = "ERROR";
        
        ambito.errores.add(new Error(error, line, valorReal, desc[error-760], "Semántica 2"));
    }
    
    public void setError(int error, int line, String lexema) {
        this.edo = "ERROR";
        
        ambito.errores.add(new Error(error, line, lexema, desc[error-760], "Semántica 2"));
    }
    
    public void setError(int regla, int error, Operando dato) {
        setRegla(regla, getTipo(dato.getTipo()), dato.getLex(), 
                dato.getToken().getLinea(), "ERROR");
        setError(error, dato.getToken().getLinea(), dato.getLex());
    }
    
    public void setError(int regla, int error, String topePila, 
            String valorReal, int line) {
        setRegla(regla, topePila, valorReal,  line, "ERROR");
        setError(error, line, valorReal);
    }
   
    String desc[] = { 
        "La EXP tiene que ser Booleana",
        "ID debe ser mismo tipo que EXP",
        "La cant. de dimensiones utilizadas debe ser igual a la declarada",
        "La organizacion de los datos de inicializacion de una lista tipo arr deben tener un orden",
        "La EXP debe ser entera",
        "Fuera de limite",
        "Los diccionarios pueden recibir el tipo de dato que crearon como llave",
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
        System.out.println("\n/////////////////////////////////");
        System.out.println("REGLA\tTOPE\tVALOR\tLINEA\tEDO\tAMB");
        
        for (Regla regla : listaReglas) {
            System.out.println(regla.getId() + "\t" + regla.getTopePila() + "\t" + regla.getValorReal() +
                    "\t" + regla.getLinea() + "\t" + regla.getEdo() +
                    "\t" + regla.getAmbito());
        }
        
    }
    
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
            default: tipo = tipoSimbolos;
        }
        
        return tipo;
    }
    
    public String getTemp(String tipoSimbolos) {
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
            default: tipo = tipoSimbolos;
        }
        
        return tipo;
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
