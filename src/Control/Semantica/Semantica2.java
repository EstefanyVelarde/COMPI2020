package Control.Semantica;

import Control.Ambito.Ambito;
import Model.Operando;
import Model.Regla;
import Model.Error;
import Model.Token;
import java.util.LinkedList;


public class Semantica2 {
    Semantica1 sem1;
    
    Ambito ambito; // Para acceder a conexion, tokens y errores
     
    LinkedList<Regla> listaReglas;
    
    public Semantica2(Ambito ambito, Semantica1 sem1) {
        this.ambito = ambito;
        
        this.sem1 = sem1;
        
        listaReglas = new LinkedList();
        
        this.edo = "Acepta";
    }
    
    public void checar(int PS, int LT) {
//        if(!sem1.operStack.isEmpty()) {
//            System.out.println("\nCHECAR SEM2");
//            System.out.println("PS: " + getTope(PS));
//            sem1.printStacks();
//        }
    }
    
    String edo;
    
    public void zona(int PS, int LT) {
        switch(PS) {
            case 1010: case 1011: case 1012: // IF, ELIF, WHILE
                System.out.println("\n@ " + PS); sem1.printStacks();
                
                saveLast(LT);
                
                if(oper.getTipo().equals("B"))
                    edo = "Acepta";
                else
                    setError(760);
                
                
                setRegla(PS);
                
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
//            case 1020: // ASIGN
//                
//                System.out.println("\n@ " + PS); sem1.printStacks();
//                
//                saveLast(LT);
//                
//                if(!sem1.lexOp.equals("=")) // ASIGN COMP
//                    PS = 1021;
//                
//                if(edo != null)
//                    setRegla(PS); // El edo se manejo en @ 852 de Sem1
//            break;
            
        }
    }
      
    Operando oper; 

    String topePila, valorReal, lexOper;
    
    int line, amb, tipoToken;
    
    public void saveLast(int LT) {
        
//        
//        System.out.println("\n - SAVELAST:");
        
        topePila = sem1.token.getLexema();
        
        amb = ambito.ambStack.peekLast();
        
        if(sem1.operStack.isEmpty()) { // OPERSTACK EMPTY (SUCEDIO ASIGN U OPERACION)
            valorReal = sem1.oper1.getToken().getLexema();
            
            line = sem1.line;
            
        } else {
            sem1.printStacks();

            oper = sem1.operStack.peekLast(); 

            valorReal = oper.getToken().getLexema();

            line = oper.getToken().getLinea();
        }
        
//        
//        System.out.println("\n    - TopePila:\t" + topePila);
//        System.out.println("    - ValorReal:\t" + valorReal);
//        System.out.println("    - Line:\t" + line);
//        System.out.println("    - Edo:\t" + edo);
//        System.out.println("    - Amb:\t" + amb);
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
    
    public void regla1090(Operando dato) {
        if(dato.getSimbolos() != null) {
            String clase = dato.getSimbolos()[1];
        
            switch(clase) {
                case "var": case "par": case "lista": case "arreglo": 
                case "diccionario": case "rango": 
                    setRegla(1090, clase, dato.getLex(), 
                            dato.getToken().getLinea(), "Acepta"); break;
                default: 
                    setError(1090, 773,  clase, dato.getLex(), 
                            dato.getToken().getLinea());
            }
        } else
            setError(1090, 773,  "TV", dato.getLex(), 
                    dato.getToken().getLinea());
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
    
    public void regla1130(String[] idsimbolos, Token token) {
        if(idsimbolos == null) {
            setError(1130, 777, "TV", token.getLexema(), token.getLinea());
        } else
            setRegla(1130, "TV", token.getLexema(), token.getLinea(), "Acepta");
        
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
        "ID debe ser declarado antes de ser utilizado"
        
        
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
 }
