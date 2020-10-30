package Control.Semantica;

import Control.Ambito.Ambito;
import Model.Operando;
import Model.Regla;
import Model.Error;
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
            
            case 1020: // ASIGN
                
                System.out.println("\n@ " + PS); sem1.printStacks();
                
                saveLast(LT);
                
                if(!sem1.lexOp.equals("=")) // ASIGN COMP
                    PS = 1021;
                
                if(edo != null)
                    setRegla(PS); // El edo se manejo en @ 852 de Sem1
            break;
            
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
    public void setRegla(int id) {
        listaReglas.add(new Regla(id, topePila, valorReal, line, edo, amb));
            
        printReglas();
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
    
   
    String desc[] = { 
        "La EXP tiene que ser Booleana",
        "ID debe ser mismo tipo que EXP",
        "La cant. de dimensiones utilizadas debe ser igual a la declarada"
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
 }
