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
    
    int[] tempCont;
    
    public Cuadruplos() {
        
        cuadruplos = new LinkedList();
        
        funciones = new LinkedList();
        
        tempCont = new int[15];
    }
    
    public void zona(int PS) {
       
        switch(PS) {
            case 800: // main
                printZone(PS);
                
                if(funciones.isEmpty())
                    pushMain();
                break;
                
            
            case 801: // end main
                printZone(PS);
                
                if(funciones.isEmpty())
                    popMain();
                
                break;
            
            case 8041: // def fun
                printZone(PS);
                
                pushFuncion(amb.lastFuncionToken);
                
                break;
                
            case 1804: // enddef
                printZone(PS);
                
                popFuncion();
                
                break;
        }
    }
    
    public void pushLista(Operando lista, Operando pos) {
        if(noErrores()) {
            this.cuadruplos.offer(new Cuadruplo("", "lista", lista.getLex(), "", ""));

            printCuadruplos();
        }
    }
    
    public void pushOperacion(Token op, Operando oper1, Operando oper2, String tempTipo) {
        if(noErrores()) {
            this.cuadruplos.offer(new Cuadruplo("", op.getLexema(), oper1.getLex(), oper2.getLex(), "T"+tempTipo+getNoTemp(tempTipo)));

            printCuadruplos();
        }
    }
    
    public void pushFuncion(Token token) {
        if(noErrores()) {
            this.cuadruplos.offer(new Cuadruplo("def", token.getLexema(), "", "", ""));

            printCuadruplos();

            funciones.add(token);

            printFunciones();
        }
    }
    
    public void popFuncion() {
        if(noErrores()) {
            if(!funciones.isEmpty()) {
            Token token = funciones.removeLast();

            this.cuadruplos.offer(new Cuadruplo("enddef", token.getLexema(), "", "", ""));
            }
            printCuadruplos();


            printFunciones();
        }
    }
    
    public void pushMain() {
        if(noErrores()) {
            this.cuadruplos.offer(new Cuadruplo("PPAL", "Main:", "", "", ""));

            printCuadruplos();

        }
    }
    
    public void popMain() {
        if(noErrores()) {
            this.cuadruplos.offer(new Cuadruplo("ENDMAIN",  "", "", "", ""));
            
            printCuadruplos();
        }
    }
    
    
    // PRINTS
    public void printZone(int PS) {
        System.out.println("\n CUAD @ " + PS);  printCuadruplos(); printFunciones();
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
        System.out.println("\n*------------- CUAD -----------\n");
        System.out.println(" FUNCIONES:");
        
        for(Token o : funciones) {
            System.out.println(o.getLexema());
        }
        
        
        System.out.println("\n------------------------*\n");
    }
    
    // ISSSSS
    
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
    
    public boolean isIdentificador(Operando oper) {
        if(notNull(oper))
            if(notNull(oper.getSimbolos()))
                return true;
        
        return false;
    }
    
    public boolean noErrores() {
        return amb.errores.isEmpty();
    }
    
    public int getNoTemp(String tipo) {
        int no = 0;
        
        switch(tipo) {
            case "B":   no = 1 + tempCont[0]++; break;
            case "CH":  no = 1 + tempCont[1]++; break;
            case "C":   no = 1 + tempCont[2]++; break;
            case "D":   no = 1 + tempCont[3]++; break;
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
    
}
