package Control;

import Control.Semantica.Semantica1;
import Control.Semantica.Semantica2;
import Model.Operando;
import Model.Token;
import java.util.LinkedList;


public class Rangos {
    Semantica1 sem1;
    
    public Semantica2 sem2;
    
    public LinkedList<Operando> operStack;
    
    public LinkedList<Token> opStack;
    
    public String[] idsimbolos; // [0] tipo [1] clase [2] idsimbolos [3] tArr [4] dimArr
    
    int nIntervalo;
    
    public Rangos(Semantica1 sem1) {
        this.sem1 = sem1;
        
        operStack =  new LinkedList();
        
        opStack = new LinkedList();
    }
    
    public void zona(int PS) {
        switch(PS) {
            
            case 816: 
                System.out.println("\nRANG @ " + PS); printStacks();
                printStacks();
                
                setArrIntervalo();
            break;
        }
    }
    
    // INTERVALOS
    
    public void setArrIntervalo() {
        int size = operStack.size();

        if(size > 1) {
            Operando oper1, oper2, oper3;
            
            oper3 = this.getLastOper();
            oper2 = this.getLastOper();
            
            if(isInteger(oper3.getLex())) {
               if(isInteger(oper2.getLex())) { // Si los dos son INT
                    int num1 = 0, num2, num3;

                    num3 = Integer.parseInt(oper3.getLex());
                    num2 = Integer.parseInt(oper2.getLex());

                    if(size == 3) { // x , x , x
                        oper1 = this.getLastOper();
                        num1 = Integer.parseInt(oper1.getLex());

                        sem2.regla1160(oper1.getToken(), num1, num2, num3);

                        if(num1 > num2) 
                            num1 = (num1 - num2);
                        else
                            num1 = (num2 - num1);


                        if(num3 < 0) // Si es dividendo es negativo
                            num3 *= -1;

                        if((num1 % num3) != 0) 
                            num1 = num1 / num3 + 1;
                        else 
                            num1 /= num3;

                        Token token= new Token(-45, oper3.getToken().getLinea(), num1 + "");

                        sem1.operStack.offer(new Operando(token, sem1.getTipo(-45)));

                        sem1.lexemaAsign += " " + token.getLexema();
                        
                        sem1.operStack.peekLast().setRange(true);
                    }
                }
            }
            
            
        }
    }
    
    // RANGSTACKS
    public Operando getLastOper() {
        Operando oper;
        
        if(operStack.size() > 0) 
            oper = operStack.removeLast();
        else {
            Token tokenTemp = sem1.token;
            
            if(tokenTemp == null)
                tokenTemp = new Token("0", -45);
                                
            oper =  new Operando(tokenTemp, "V", true, sem2.getNoTemp("V"));
        }
        
        return oper;
    }
    
    public Operando getFirstOper() {
        Operando oper;
        
        if(operStack.size() > 0) 
            oper = operStack.removeFirst();
        else {
            Token tokenTemp = sem1.token;
            
            if(tokenTemp == null)
                tokenTemp = new Token("0", -45);
                                
            oper =  new Operando(tokenTemp, "V", true, sem2.getNoTemp("V"));
        }
        
        return oper;
    }
    
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
        System.out.println(" RANG - OPERSTACK:");
        
        for(Operando o : operStack) {
            if(!o.isTemp())
                System.out.println(" "+o.getToken().getLexema()+" "+o.getTipo());
            else
                System.out.println(" T"+o.getTipo());
        }
        
        System.out.println("\n RANG - OPSTACK:");
        for(Token t : opStack) 
            System.out.println(" "+t.getLexema());
        
        System.out.println("\n------------------------*\n");
    }
    
    public boolean isInteger(String numero){
        try{
            Integer.parseInt(numero);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
    
    // REGLAS
    public void setRegla(int regla, Operando dato) {
        if(dato != null) {
            sem2.setRegla(regla, dato.getTipo(), dato.getLex(), 
                    dato.getToken().getLinea(), "Acepta");
        }
        
    }
    
    public void setError(int regla, int error, Operando dato) {
        if(dato != null) {
            sem2.setRegla(regla, dato.getTipo(), dato.getLex(), 
                    dato.getToken().getLinea(), "ERROR");
            sem2.setError(error, dato.getToken().getLinea(), dato.getLex());
        }
    }
    
}
