package Control;

import java.util.LinkedList;

public class Ambito {
    Conexion con;
    
    LinkedList<Integer> ambStack;
    
    boolean ejecucion, declaracion;
    
    public Ambito(Conexion con) {
        this.con = con;
        
        ambStack = new LinkedList();
    }
    
    public void checar(int PS, int LT) {
        
        if(!ejecucion(PS, LT)) 
            declaracion(PS, LT);
    }
    
    public boolean ejecucion(int PS, int LT) {
        if(PS == 800) {
            ejecucion = true;
            System.out.println("++ ZONA EJECUCION \n\tPS: " + PS +"\n\tLT: " + LT);
            return true;
        }
            
        if(PS == 801) {
            ejecucion = false;
            System.out.println("-- ZONA EJECUCION \n\tPS: " + PS +"\n\tLT: " + LT);
            return true;
        }
        
        return false;
    }
    
    public boolean declaracion(int PS, int LT) {
        if(PS == 802) {
            declaracion = true;
            
            nuevo();
            
            System.out.println("++ ZONA DECLARACION \n\tPS: " + PS +"\n\tLT: " + LT);
            return true;
        }
            
        if(PS == 803) {
            declaracion = false;
            
            viejo();
            
            System.out.println("-- ZONA DECLARACION \n\tPS: " + PS +"\n\tLT: " + LT);
            return true;
        }
        
        return false;
    }
    
    public void nuevo() {
        if(ambStack.isEmpty()) {
            ambStack.add(0);
        } else {
            int last = ambStack.peekLast() + 1;
            
            ambStack.add(last);
        }
        
        System.out.println("++ AMBITO \n\t" + ambStack.toString());
    }
    
    public void viejo() {
        ambStack.removeLast();
        System.out.println("-- AMBITO \n\t" + ambStack.toString());
    }
    
}
