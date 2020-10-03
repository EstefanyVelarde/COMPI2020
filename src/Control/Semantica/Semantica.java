package Control.Semantica;

import Control.Ambito.Ambito;
import Model.Operando;
import Model.Token;
import java.sql.Connection;
import java.util.Hashtable;
import java.util.LinkedList;

public class Semantica {
    MatrizSemantica matriz;
    
    Ambito ambito;
    
    LinkedList<Operando> operStack;
    
    LinkedList<Token> opStack;
    
    // TEMPORAL TIPO - CONTADOR
    Hashtable<String, Integer> contTemp; 
    
    public Semantica(Ambito ambito) {
        this.ambito = ambito;
        
        matriz = new MatrizSemantica();
        
        operStack =  new LinkedList();
        
        opStack = new LinkedList();
        
        contTemp = new Hashtable();
    }
    
    public void checar(int PS, int LT) {
        if(LT == -44) { // Si es ID
            
        } 
    }
    
    public void zona(int PS) {
        switch(PS) {
            case 820: break;
            case 821: break;
        }
        
    }
    
    
    
    
}
