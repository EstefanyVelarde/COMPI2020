package Control.Cuadruplos;

import Control.Ambito.Ambito;
import Control.Semantica.Semantica1;
import Control.Semantica.Semantica2;
import Control.Semantica.Semantica3;
import Model.Cuadruplo;
import Model.Operando;
import Model.Token;
import java.util.LinkedList;

public class Cuadruplos {
    public Ambito amb;
    
    public Semantica1 sem1;
    
    public Semantica2 sem2;
    
    public Semantica3 sem3;
    
    public LinkedList<Cuadruplo> cuadruplos;
    
    public LinkedList<Token> funciones;
    
    public Cuadruplos() {
        
        cuadruplos = new LinkedList();
        
        funciones = new LinkedList();
        
    }
    
    public void zona(int PS) {
       
        switch(PS) {
            case 8041: // def fun
                
                pushFuncion(amb.lastFuncionToken);
                
                break;
                
            case 1804: // enddef
                
                popFuncion();
                
                break;
        }
    }
    
    public void setCuadruplo(Operando oper) {
    
    }
    
    public void setCuadruplo(Token token) {
        
    }
    
    public void pushFuncion(Token token) {
        this.cuadruplos.offer(new Cuadruplo("def", token.getLexema(), "", "", ""));
        
        printCuadruplos();
        
        funciones.add(token);
        
        printFunciones();
    }
    
    public void popFuncion() {
        if(!funciones.isEmpty()) {
        Token token = funciones.removeLast();
        
        this.cuadruplos.offer(new Cuadruplo("enddef", token.getLexema(), "", "", ""));
        }
        printCuadruplos();
        
        
        printFunciones();
    }
    
    public void printCuadruplos() {
        System.out.println("\n//////////////////////// CUADRUPLOS /////////////////////////");
        System.out.println("ETIQ\tACCION\tARG1\tARG2\tRESULTADO");
        
        for (Cuadruplo c : cuadruplos) {
            System.out.println(c.getEtiqueta() + "\t" + c.getAccion() + "\t" + c.getArg1() + "\t" + c.getArg2() +
                    "\t" + c.getRes());
        }
        System.out.println("");
    }
    
    public void printFunciones() {
        System.out.println("\n*------------------------\n");
        System.out.println(" FUNCIONES:");
        
        for(Token o : funciones) {
            System.out.println(o.getLexema());
        }
        
        
        System.out.println("\n------------------------*\n");
    }
    
}
