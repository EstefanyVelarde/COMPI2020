package Control;

import Control.Semantica.Semantica1;
import Control.Semantica.Semantica2;
import Model.Operando;
import Model.Token;
import java.util.LinkedList;

public class Funciones {
    Semantica1 sem1;
    
    public Semantica2 sem2;
    
    public LinkedList<Operando> operStack;
    
    public LinkedList<Token> opStack;
    
    public String[] idsimbolos; // [0] tipo [1] clase [2] idsimbolos [3] tArr [4] dimArr
    
    int nIntervalo;
    
    public Funciones(Semantica1 sem1) {
        this.sem1 = sem1;
        
        operStack =  new LinkedList();
        
        opStack = new LinkedList();
    }
    
    public void zona(int PS) {
        switch(PS) {
            case 823: // ,
                System.out.println("\nFUN @ " + PS); printStacks();
                printStacks();
            break;

            case 824: // :
                System.out.println("\nFUN @ " + PS); printStacks();
                nIntervalo++;
            break;
            
            case 846: 
                System.out.println("\nFUN @ " + PS); printStacks();
                printStacks();
                
                checarFunStack();
            break;
        }
    }
    
    public void checarFunStack() {
        if(idsimbolos == null) {
            while(!operStack.isEmpty()) {
                Operando dato = operStack.removeFirst();
                
                setError(1100, 774, dato);
            }
        } else {
            Operando dato = null;
            int par = 0, noPar = Integer.parseInt(idsimbolos[6]);
            while(!operStack.isEmpty()) {
                dato = operStack.removeFirst();
                
                if(par < Integer.parseInt(idsimbolos[6])) {
                    par++;
                    setRegla(1100, dato);
                } else
                    setError(1100, 774, dato);
            }
            
            if(par < noPar){
                setError(1100, 774, dato);
            }
            
        }
            
        
        printStacks();
    }
    
    // FUNSTACKS
    public void setIdentificador(String[] idsimbolos, Token token) {
        if(idsimbolos != null) {
                operStack.offer(new Operando(token, sem1.getTipo(idsimbolos[0]), 
                Integer.parseInt(idsimbolos[2]), idsimbolos));
        } else {
            operStack.offer(new Operando(token, "V")); // Guardamos temp variant
        }
    }
    
    public void setOper(Token token, int LT) {
        if(sem1.negativo) {
            operStack.offer(new Operando(new Token(LT, 
                    token.getLinea(), "-"+token.getLexema()), sem1.getTipo(LT)));

            sem1.negativo = false;
        } else
            operStack.offer(new Operando(token, sem1.getTipo(LT)));
    }
    
    public void setOp(Token token) {
        opStack.offer(token);
    }
    
    public void printStacks() {
        System.out.println("\n*------------------------\n");
        System.out.println(" FUN - OPERSTACK:");
        
        for(Operando o : operStack) {
            if(o.getToken() != null)
                System.out.println(" "+o.getToken().getLexema()+" "+o.getTipo());
            else
                System.out.println(" T"+o.getTipo());
        }
        
        System.out.println("\n FUN - OPSTACK:");
        for(Token t : opStack) 
            System.out.println(" "+t.getLexema());
        
        System.out.println("\n------------------------*\n");
    }
    
    // REGLAS
    public void setRegla(int regla, Operando dato) {
        sem2.setRegla(regla, dato.getTipo(), dato.getLex(), 
                dato.getToken().getLinea(), "Acepta");
        
    }
    
    public void setError(int regla, int error, Operando dato) {
        sem2.setRegla(regla, dato.getTipo(), dato.getLex(), 
                dato.getToken().getLinea(), "ERROR");
        sem2.setError(error, dato.getToken().getLinea(), dato.getLex());
    }
    
    
}
