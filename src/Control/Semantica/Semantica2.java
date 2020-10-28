package Control.Semantica;

import Control.Ambito.Ambito;
import Model.Operando;
import Model.Regla;
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
    }
    
    public void checar(int PS) {
        if(!sem1.operStack.isEmpty()) {
            System.out.println("\nCHECAR SEM2");
            System.out.println("PS: " + getTope(PS));
            sem1.printStacks();
        }
    }
        
    public void zona(int PS) {
        switch(PS) {
            case 1010: // IF    
                System.out.println("\n@ 1010"); sem1.printStacks();
                if(!sem1.opStack.isEmpty()) {
                    
                    
                    saveRegla(PS, "ERROR");
                    
                    sem1.emptyStacks();
                    
                    return;
                }
                
                if(sem1.operStack.isEmpty()) {
                    
                    saveRegla(PS, "ERROR");
                    
                    return;
                }
                
                Operando oper = sem1.operStack.peekLast();
                
                if(oper.getTipo().equals("B")){
                    
                    saveRegla(PS, "Acepta");
                } else
                    saveRegla(PS, "ERROR");
                
                sem1.emptyStacks();
            break;
            
        }
    }
    
    Operando oper1, oper2; 

    String valorReal, tipoOper2;
    
    Token op;

    int line, amb;
    
    public void saveRegla(int id, String edo) {
        
        if(sem1.operStack.isEmpty()) {
            System.out.println("!!!!!!!!!!!!!!!!!!!! OPERSTACK EMPTY");
        } else {
            oper1 = sem1.operStack.removeLast(); 
        
            valorReal = oper1.getToken().getLexema();

            line = oper1.getToken().getLinea();

            amb = ambito.ambStack.getLast();

            listaReglas.add(new Regla(id, "-", valorReal, line, edo, amb));
            
            printReglas();
        }
        
    }
    
    public String getTope(int PS) {
        String tope = null;
        
        switch(PS) {
            case -11: 
            case -12: tope = "Boolean";  break;   // Boolean
            default:  tope = PS + "";
            
        }
        
        return tope;
    }
    
    public void printReglas() {
        System.out.println("/////////////////////////////////");
        System.out.println("REGLA\tTOPE\tVALOR\tLINEA\tEDO\tAMB");
        
        for (Regla regla : listaReglas) {
            System.out.println(regla.getId() + "\t" + regla.getTopePila() + "\t" + regla.getValorReal() +
                    "\t" + regla.getLinea() + "\t" + regla.getEdo() +
                    "\t" + regla.getAmbito());
        }
        
    }
 }
