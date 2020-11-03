package Control;

import Control.Semantica.Semantica1;
import Control.Semantica.Semantica2;
import Model.Operando;
import Model.Token;
import java.util.LinkedList;

public class Arreglos {
    Semantica1 sem1;
    
    public Semantica2 sem2;
    
    public LinkedList<Operando> operStack;
    
    public LinkedList<Token> opStack;
    
    public String[] idsimbolos; // [0] tipo [1] clase [2] idsimbolos [3] tArr [4] dimArr [5] tipoLista [6] noPar [7] funcion
    
    int[] tArr;
    
    public boolean tupla;
    
    int nIntervalo, dimArr;

    public Arreglos(Semantica1 sem1) {
        this.sem1 = sem1;
        
        operStack =  new LinkedList();
        
        opStack = new LinkedList();
    }
    
    public void zona(int PS) {
        switch(PS) {
            case 823: // ,
                System.out.println("\nARR @ " + PS); printStacks();
                checarArrIntervalo();
                printStacks();
            break;

            case 824: // :
                System.out.println("\nARR @ " + PS); printStacks();
                nIntervalo++;
            break;
            
            case 861:
                System.out.println("\nARR @ " + PS); printStacks();
                checarArrIntervalo();
                printStacks();
                
                checarArrStack();
            break;
            
            case 811:
                System.out.println("\nARR @ " + PS); printStacks();
                checarArrIntervalo();
                printStacks();
                
                Operando oper = operStack.removeLast();
                sem1.setOper(new Operando(oper.getToken(), "D"));
                
                sem1.operStack.peekLast().setArr(true);
            break;
        }
    }
    
    public void checarArrStack() {
        if(idsimbolos == null) {
            while(!operStack.isEmpty()) {
                Operando dato = operStack.removeFirst();
                
                setError(1030, 762, dato);
            }
        } else
            switch(idsimbolos[1]) {
                case "arreglo": arreglo(); break;
                case "diccionario": diccionario(); break;
                case "tupla": tupla(); break;

            }
        
        printStacks();
    }
    
    public void arreglo() {
        int dim = 0;
        
        while(!operStack.isEmpty()) {
            Operando dato = operStack.removeFirst();
            
            if(regla1030(dato, dim)) {
                if(regla1040(dato)) {
                    regla1050(dato, dim);
                }
            }
            
            dim++;
        }
            
        
    }
    
    public void diccionario() {
        
    }
    
    public void tupla() {
        int dim = 0;
        
        dimArr = 1;
        
        while(!operStack.isEmpty()) {
            Operando dato = operStack.removeFirst();
            
            if(regla1030(dato, dim)) { // DIM
                if(regla1040(dato)) { // ENT
                    if(regla1050(dato, dim)) { // LIM
                        regla1070(dato, Integer.parseInt(dato.getLex()) + 1);
                        regla1071(dato, dim);
                    }
                }
            }
            
            dim++;
        }
    }
    
    
    // SEMANTICA 2
    public boolean regla1030(Operando dato, int dim) {
        if(dim < dimArr) {
           setRegla(1030, dato);
           
           return true;
        } else 
            setError(1030, 762, dato);
        
        return false;
    }
    
    public boolean regla1040(Operando dato) {
        if(dato.getTipo().equals("D")) {
           setRegla(1040, dato);
           
           return true;
        } else 
           setError(1040, 764, dato);
        
        return false;
    }
    
    public boolean regla1050(Operando dato, int dim) {
        if(tArr != null) {
            if(isInteger(dato.getLex())) { // Si no es temp
                int num = Integer.parseInt(dato.getLex());
                if(dim < tArr.length) {
                    if(num < tArr[dim]) { // Si esta dentro del limite
                        setRegla(1050, dato);

                        return true;
                    } 
                } else {
                    setError(1050, 765, dato);

                    return false;
                }
            } else {
                setRegla(1050, dato);

                return false;
            }
            
        } else 
           setError(1050, 765, dato);
        
        return false;
    }
    
    public void regla1070(Operando dato, int noPar) {
        Operando tupla = sem1.operStack.peekLast();
        
        if(tupla.getSimbolos() != null) {
            tupla.setTipo(sem1.getTipo(sem1.amb.getTipoDato(noPar, tupla.getLex())));
        } else
            tupla.setTipo("V");
        
        setRegla(1070, tupla);
    }
    
    public void regla1071(Operando dato, int dim) {
        if(dato.getTipo().equals("D")) { // Si es decimal
            if(isInteger(dato.getLex())) { // Si no es temp
                int num = Integer.parseInt(dato.getLex());

                if(num < 0) { // Si es negativo da ERROR 1071
                    setError(1071, 769, dato);
                } else {
                    setRegla(1071, dato);
                }
            } else
                setRegla(1071, dato);
        } 
        
    }
    
    public void setRegla(int regla, Operando dato) {
        sem2.setRegla(regla, sem2.getTipo(dato.getTipo()), dato.getLex(), 
                dato.getToken().getLinea(), "Acepta");
        
    }
    
    public void setError(int regla, int error, Operando dato) {
        sem2.setRegla(regla, dato.getTipo(), dato.getLex(), 
                dato.getToken().getLinea(), "ERROR");
        sem2.setError(error, dato.getToken().getLinea(), dato.getLex());
    }
    
    // INTERVALOS
    public void checarArrIntervalo() {
        if(nIntervalo != 0)
            setArrIntervalo();
    }
    
    public void setArrIntervalo() {
        int size = operStack.size();

        if(size > 1) {
            Operando oper1, oper2, oper3;
            
            oper3 = operStack.removeLast();
            oper2 = operStack.removeLast();
            
            int num1, num2, num3;

            num3 = Integer.parseInt(oper3.getLex());
            num2 = Integer.parseInt(oper2.getLex());

            if(nIntervalo == 1) { // x : x
                if(num2 > num3) 
                    num1 = num2 - num3;
                else
                    num1 = num3 - num2; // Podria ir ERROR 1031
            
                sem2.regla1031(oper2.getToken(), num2, num3);
            } else { // x : x : x
                oper1 = operStack.removeLast();
                num1 = Integer.parseInt(oper1.getLex());
                
                
                sem2.regla1031(oper1.getToken(), num1, num2, num3);
                
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
            }
            
                
            operStack.add(new Operando(new Token(num1 + "", oper3.getToken().getLinea()), "D"));
        }
        
        nIntervalo = 0;
    }
    
    // ARRSTACKS
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
        System.out.println(" ARR - OPERSTACK:");
        
        for(Operando o : operStack) {
            if(o.getToken() != null)
                System.out.println(" "+o.getToken().getLexema()+" "+o.getTipo());
            else
                System.out.println(" T"+o.getTipo());
        }
        
        System.out.println("\n ARR - OPSTACK:");
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
    
    // IDSIMBOLOS
    public void setIdSimbolos(String[] idsimbolos) {
        this.idsimbolos = idsimbolos;
        
        if(idsimbolos != null) {
            if(idsimbolos[4] != null)
                dimArr = Integer.parseInt(idsimbolos[4]);
            
            checarTArr();
        }
    }
    
    // TARR
    public void checarTArr() {
        if(idsimbolos[3] != null) {
            String tArr = idsimbolos[3];

            this.tArr = new int[tArr.length()];

            int i = 0;

            if(tArr.contains(";") && tArr.contains(",")) { // 2;3,4
                for (int j = 2; j < tArr.length(); j++) {
                    if(tArr.charAt(j) != ',') {
                        this.tArr[i] = Integer.parseInt(tArr.charAt(j) + "");

                        i++;
                    }
                }
            } else {
                if(tArr.contains(";")) { // 2;3;4
                    for (int j = 0; j < tArr.length(); j++) {
                        if(tArr.charAt(j) != ';'){
                            this.tArr[i] = Integer.parseInt(tArr.charAt(j) + "");

                            i++;
                        }
                    }
                } else // [1]
                    this.tArr[0] = Integer.parseInt(idsimbolos[3]);
            }
        }
        
    }
    
}
