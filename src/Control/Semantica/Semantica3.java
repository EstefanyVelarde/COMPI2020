package Control.Semantica;

import Control.Ambito.Ambito;
import Model.Operando;
import Model.Regla;
import Model.Token;
import java.util.LinkedList;


public class Semantica3 {
    Ambito ambito; // Para acceder a conexion, tokens y errores
    
    Semantica1 sem1;
     
    LinkedList<Regla> listaReglas;
    
    LinkedList<Token> funStack;
    
    LinkedList<Operando> idFunListStack;
    
    LinkedList<Operando> parStack;
    
    public Semantica3(Ambito ambito, Semantica1 sem1) {
        this.ambito = ambito;
        
        this.sem1 = sem1;
        
        listaReglas = new LinkedList();
        
        funStack = new LinkedList();
        
        idFunListStack = new LinkedList();
        
        parStack = new LinkedList();
        
        tempCont = new int[15];
    }
    
    
    public Token lastFun;

    public void checar(int LT) {
        if(isFuncion(LT) || isFunList(LT)) {
            lastFun = ambito.tokens.peekFirst();
            
            funStack.offer(lastFun);
            
            this.printStacks();
        }
    }
    
    public void zona(int PS) {
        switch(PS) {
            case 869: // GET FUNLIST ID
                idFunListStack.offer(sem1.peekLastOper(null));
                
                this.printZone(PS);
                
            break;
        }
        
        if(!funStack.isEmpty()) {
            switch(PS) {
                case 870: // FUN_PAR1
                    this.printZone(PS);
                    
                    Operando par1 = sem1.getLastOper(null);

                    parStack.offer(par1);
                    
                    //se deberia verificar regla dependiendo fun y par1
                    verificarReglas(funStack.peekLast(), par1, parStack.size());
                    
                    if(par1.isTemp())
                        System.out.println("++ PAR1: " + "T" + par1.getTipo());
                    else
                        System.out.println("++ PAR1: " + par1.getLex());
                    
                    sem1.printStacks();
                break;
                
                case 871: // FUN_PAR2
                    this.printZone(PS);
                    
                    Operando par2 = sem1.getLastOper(null);
                    
                    parStack.offer(par2);
                    
                    //se deberia verificar regla dependiendo fun y par2
                    verificarReglas(funStack.peekLast(), par2, parStack.size());
                    
                    if(par2.isTemp())
                        System.out.println("++ PAR2: " + "T" + par2.getTipo());
                    else
                        System.out.println("++ PAR2: " + par2.getLex());
                    
                    
                    sem1.printStacks();
                break;
                
                case 872: // END FUN                   
                    this.printZone(PS);
                    
                    lastFun = funStack.removeLast();
                    
                    if(parStack.size() == 2 || parStack.size() == 1) {

                        // se marca el tipo que regresa la funcion y se agrega a sem1 operandos
                        verificarReglas(lastFun);
                    } else {
                        // Set error y variant? o solo variant?
                    }
                           
                    sem1.printStacks();
                    
                    this.emptyStacks();
                break; 
                
                case 873: // END FUNLIST               
                    this.printZone(PS);
                    
                    lastFun = funStack.removeLast();
                    
                    if(isFunList(lastFun.getToken())) { // Si era FUNLIST
                        if(!idFunListStack.isEmpty()) {
                            Operando lastId = idFunListStack.removeLast(); // Sacamos el id
                            
                            lastId.setTemp(true);
                            
                            // se marca el tipo que regresa la funlist
                            
                        } else {
                            // set error y variant? o solo variant?
                        }
                    }
                        
                    sem1.printStacks();
                    
                    this.emptyStacks();
                break; 
            }
        }
    }
    
    public void verificarReglas(Token lastFun) {
        if(lastFun != null) {
            String funLex = lastFun.getLexema();
            
            switch(funLex) {
                case "findall": // 2008
//                    topePila = getTopePila(funLex);
//                    
//                    Operando dato = null;
//                    
//                    for(Operando o : parStack) {
//                        if(isVariant(o)) {
//                            dato = o;
//                            break;
//                        }
//                    }
//                    
//                    if(dato == null)
//                        this.setTemp(parStack.peekLast(), "L");
//                    
//                    setRegla(2001, funLex, par, "Acepta");
//                    
//                    if(par.getTipo().equals("C") || isIdentificador(par)
//                            || isVariant(par))
//                        setRegla(2001, funLex, par, "Acepta");
//                    else 
//                        setError(2001, funLex, par);

// TENDRE QUE HACER UN STACK DE FUNCIONES Y SUS PARSTACK
                break;
            }
        }
        
        this.printReglas();
    }
    
    public void verificarReglas(Token lastFun, Operando par, int noPar) {
        if(lastFun != null && par != null) {
            String funLex = lastFun.getLexema();
            if(noPar == 1) {
                switch(funLex) {
                    case "findall": // 2001
                    case "replace":
                        topePila = getTopePila(funLex, noPar);
                        
                        if(par.getTipo().equals("C") || isIdentificador(par)
                                || isVariant(par))
                            setRegla(2001, funLex, par, "Acepta");
                        else 
                            setError(2001, funLex, par);
                        
                            
                    break;
                }
                
            } else {
                if(noPar == 2) {
                    switch(funLex) {
                        case "replace": // 2001
                            topePila = getTopePila(funLex, noPar);
                            
                            if(par.getTipo().equals("C") || isIdentificador(par)
                                    || isVariant(par))
                                setRegla(2001, funLex, par, "Acepta");
                            else 
                                setError(2001, funLex, par);

                            
                        break;
                            
                        case "findall":
                            topePila = getTopePila(funLex, noPar);
                            
                            if(par.getTipo().equals("C") 
                                    || (par.getTipo().equals("L") 
                                    && par.getSimbolos() != null)
                                    || isIdentificador(par)
                                    || isVariant(par))
                                setRegla(2002, funLex, par, "Acepta");
                            else 
                                setError(2002, funLex, par);
                                
                        break;
                    }

                }
            }
        }
        
        this.printReglas();
    }
    
    public boolean isIdentificador(Operando oper) {
        if(oper != null)
            if(oper.getSimbolos() != null)
                return true;
        
        return false;
    }
    
    public boolean isVariant(Operando oper) {
        if(oper != null)
            if(oper.getTipo().equals("V"))
                return true;
        
        return false;
    }
    
    public void setTemp(Operando dato, String tipo) {
        dato.setTipo(tipo);
        
        int noTemp = this.getNoTemp(tipo);
        
        dato.setNoTemp(noTemp);
        
        dato.setTemp(true);
    }
    
    Operando oper; 

    String funcion, topePila, valorReal, lexOper, edo;
    
    int line, amb, tipoToken;
    
    int[] tempCont;
    
    public String getValorReal(Operando oper) {
        String valorReal = "";
        
        if(oper.isTemp())
                valorReal = getTemp(oper.getTipo(), oper.getNoTemp());
            else
                valorReal = oper.getLex();
        
        return valorReal;
    }
    
    public String getTopePila(String fun, int noPar) {
        String topePila = "";
        
        if(noPar == 1) 
            switch(fun) {
                case "findall":   topePila = "id, cadena";   break;
            }
        else
            if(noPar == 2) 
                switch(fun) {
                    case "findall":   topePila = "id, cadena, lista";   break;
                }
        
        return topePila;
    }
    
    public String getTopePila(String fun) {
        String topePila = "";
        
        switch(fun) {
            case "findall":   topePila = "lista";   break;
        }
        
        return topePila;
    }
    
    public String getTemp(String tipo) {
        String temp =  "T" + tipo;
        
        return temp + getNoTemp(tipo);
    }
    
    public String getTemp(String tipo, int noTemp) {
        String temp =  "T" + tipo + noTemp;
        
        return temp;
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
    
    // SET REGLAS
    public void setRegla(int id, String funcion, Operando dato, String edo) {
        amb = ambito.ambStack.peekLast();
        
        valorReal = this.getValorReal(dato);
        
        line = dato.getToken().getLinea();
        
        listaReglas.add(new Regla(id, funcion, topePila, valorReal, line, edo, amb));
    }
    
    // SET ERRORES
    public void setError(int error, String funcion, Operando dato) {
        setRegla(2001, funcion, dato, "Error");
        
        ambito.errores.add(new Model.Error(error-2001+780, line, valorReal, desc[error-2001], "Semántica 3"));
    
        
        setTemp(dato, "V");
    }
    
    String desc[] = { 
        "Paramertro debe ser tipo cadena, id o idcadena",
        "Paramertro debe ser tipo cadena, id, idlista o  idcadena",
    };
    
    // FUNCION
    public boolean isFuncion(int LT) {
        return (LT <= -71 && LT >= -76) || LT == -78 || LT == -81;
    }
    
    
    public boolean isFunList(int LT) {
        return (LT <= -61 && LT >= -69);
    }
    
    // LISTA REGLAS
    public void printReglas() {
        System.out.println("\n//////////////// SEM 3 /////////////////");
        System.out.println("REGLA\tFUNCION\tTOPEPILA\tVALORREAL\tLINEA\tEDO\tAMB");
        
        for (Regla regla : this.listaReglas) {
            System.out.println(regla.getId() + "\t" + regla.getFuncion() + "\t" + regla.getTopePila() + "\t" + regla.getValorReal() +
                    "\t" + regla.getLinea() + "\t" + regla.getEdo() +
                    "\t" + regla.getAmbito());
        }
        System.out.println("");
    }
    
    // STACKS 
    public void printZone(int PS) {
        System.out.println("\n SEM3 @ " + PS); printStacks();
    }
    
    public void emptyStacks() {
        funStack =  new LinkedList();
        
        idFunListStack = new LinkedList();
        
        parStack = new LinkedList();

        System.out.println("\n*-------------- sem3 empty stacks----------*\n");
    }
    
    public void printStacks() {
        System.out.println("\n*------------------------\n");
        
        System.out.println(" FUNSTACK:");
        for(Token t : funStack) 
            System.out.println(" "+t.getLexema());
        
        System.out.println("\n IDFUNLISTSTACK:");
        
        for(Operando o : idFunListStack) {
            if(!o.isTemp())
                System.out.println(" "+o.getToken().getLexema()+" "+o.getTipo());
            else
                System.out.println(" T"+o.getTipo());
        }
        
        System.out.println("\n PARSTACK:");
        
        for(Operando o : parStack) {
            if(!o.isTemp())
                System.out.println(" "+o.getToken().getLexema()+" "+o.getTipo());
            else
                System.out.println(" T"+o.getTipo()+o.getNoTemp());
        }
        
        
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
}
