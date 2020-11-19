package Model;

import java.util.LinkedList;

public class Funcion {
    Token token;
    
    Operando idFunlist;
    
    LinkedList<Operando> parStack;
    
    public boolean isFun, isFunlist;

    public Funcion(Token token) {
        this.token = token;
        
        parStack = new LinkedList();
        
        isFun = true;
    }

    public Funcion(Token token, Operando idFunlist) {
        this.token = token;
        
        this.idFunlist = idFunlist;
        
        parStack = new LinkedList();
        
        isFunlist = true;
    }
    
    
    public void setPar(Operando par) {
        parStack.offer(par);
    }
    
    public Operando getLastPar() {
        if(parStack.isEmpty())
            return null;
        else
            return parStack.removeLast();
    }
    
    public Operando peekLastPar(Operando par) {
        if(parStack.isEmpty())
            return null;
        else
            return parStack.peekLast();
    }
    
    public int getParStackSize() {
        return parStack.size();
    }
    
    
    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Operando getIdFunlist() {
        return idFunlist;
    }

    public void setIdFunlist(Operando idFunlist) {
        this.idFunlist = idFunlist;
    }
    
    public LinkedList<Operando> getParStack() {
        return parStack;
    }

    public void setParStack(LinkedList<Operando> parStack) {
        this.parStack = parStack;
    }
    
    
}
