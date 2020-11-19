package Control.Semantica;

import Control.Ambito.Ambito;
import Model.Funcion;
import Model.Operando;
import Model.Regla;
import Model.Token;
import java.util.LinkedList;


public class Semantica3 {
    Ambito ambito; // Para acceder a conexion, tokens y errores
    
    Semantica1 sem1;
     
    LinkedList<Regla> listaReglas;
    
    LinkedList<Funcion> funStack;
    
    public Semantica3(Ambito ambito, Semantica1 sem1) {
        this.ambito = ambito;
        
        this.sem1 = sem1;
        
        listaReglas = new LinkedList();
        
        funStack = new LinkedList();
        
        tempCont = new int[15];
    }
    
    
    public Token token;

    public void checar(int LT) {
        if(isFuncion(LT)) {
            token = ambito.tokens.peekFirst();
            
            funStack.offer(new Funcion(token));
            
            System.out.println("\n++ ISFUN\n");
            this.printStacks();
        } else {
            if(isFunlist(LT)) {
                token = ambito.tokens.peekFirst();

                funStack.offer(new Funcion(token, sem1.peekLastOper(null)));
                
                System.out.println("\n++ ISFUNLIST\n");
                this.printStacks();
            }
        }
    }
    
    public void zona(int PS) {
        switch(PS) {
            case 869: // GET FUNLIST ID
                this.printZone(PS);
                
            break;
        }
        
        if(!funStack.isEmpty()) {
            switch(PS) {
                
                // EST ;
                case 862: 
                    printZone(PS);
                    
                    this.printReglas();
                    
                    this.emptyStacks();
                break;
                
                case 870: // FUN_PAR1
                    this.printZone(PS);
                    
                    Operando par1 = sem1.getLastOper(null);
                    
                    if(!funStack.isEmpty()) {
                        
                        Funcion lastFun = funStack.peekLast();
                        
                        lastFun.setPar(par1);

                        //se deberia verificar regla dependiendo fun y par1
                        verificarReglas(lastFun, par1, lastFun.getParStackSize());
                    
                        if(par1.isTemp())
                            System.out.println("++ PAR1: " + "T" + par1.getTipo());
                        else
                            System.out.println("++ PAR1: " + par1.getLex());
                    }
                    
                    
                    sem1.printStacks();
                    
                    this.printStacks();
                break;
                
                case 871: // FUN_PAR2
                    this.printZone(PS);
                    
                    Operando par2 = sem1.getLastOper(null);
                    
                    if(!funStack.isEmpty()) {
                        
                        Funcion lastFun = funStack.peekLast();
                        
                        lastFun.setPar(par2);

                        //se deberia verificar regla dependiendo fun y par2
                        verificarReglas(lastFun, par2, lastFun.getParStackSize());
                    
                        if(par2.isTemp())
                            System.out.println("++ PAR2: " + "T" + par2.getTipo());
                        else
                            System.out.println("++ PAR2: " + par2.getLex());
                    }
                    
                    
                    sem1.printStacks();
                    
                    this.printStacks();
                break;
                
                case 872: // END FUN                   
                    this.printZone(PS);
                    
                    if(!funStack.isEmpty()) {
                        Funcion lastFun = funStack.removeLast();
                        
                        if(checarNoPar(lastFun)) { // verificamos num parametros de la funcion
                            
                            // se marca el tipo que regresa la funcion y se agrega a sem1 operandos
                            String tipo = getTipoSalida(lastFun); 
                            
                            if(tipo != null) { // Si es SALIDA
                                Token funToken = lastFun.getToken();
                                
                                Operando temp = saveTemp(funToken, tipo);
                                
                                verificarReglas(lastFun, temp);
                            } else { // es sort, reverse o extend, remove, insert
                                
                            } 
                            
                        } else { // Set error 2020 TV num parametros incorrectos
                            
                        }
                    }
                   
                           
                    sem1.printStacks();
                    
                    this.printStacks();
                break; 
                
                case 873: // END FUNLIST               
                    this.printZone(PS);
                    
                    if(!funStack.isEmpty()) {
                        Funcion lastFun = funStack.removeLast();
                        
                        Operando lastId = lastFun.getIdFunlist();
                        
                        if(lastId != null) { // Sacamos el id
                            lastId.setTemp(true);

                            // se marca el tipo que regresa la funlist

                        } else {
                            // set error y variant? o solo variant?
                        }
                        
                    }
                    
                    sem1.printStacks();
                    
                    this.printStacks();
                break; 
            }
        }
    }
    
    public void verificarReglas(Funcion lastFun, Operando temp) {
        if(lastFun != null) {
            Token funToken = lastFun.getToken();
            
            String funLex = funToken.getLexema();
            
            switch(funLex) {
                case "findall":  // 2008
//                case "sample":
                    topePila = getTopePila(lastFun);
                    
                    setRegla(2008, funLex, temp, "Acepta");
                break;
                
                case "replace": // 2009
                    topePila = getTopePila(lastFun);
                    
                    setRegla(2009, funLex, temp, "Acepta");
                break;
                
                case "count": // 2010, 2017
                    topePila = getTopePila(lastFun);
                    
                    setRegla(2010, funLex, temp, "Acepta");
                    
                    // checar uso
                    
                break;
            }
        }
        
        this.printReglas();
    }
    
    public void verificarReglas(Funcion lastFun, Operando par, int noPar) {
        if(lastFun != null && par != null) {
            Token funToken = lastFun.getToken();
            
            String funLex = funToken.getLexema();
            
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
                            
                        case "findall": // 2002
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
    
    public boolean checarNoPar(Funcion fun) {
        
        if(fun != null) {
            Token funToken = fun.getToken();
            
            if(funToken != null) {
                String funLex = funToken.getLexema();
                
                int noPar = fun.getParStackSize();

                switch(funLex) {
                    case "findall": case "sample": case "randrange": case "insert": 
                        if(noPar == 2)
                            return true;
                    break;
                    
                    case "replace": 
                        if(noPar == 3)
                            return true;
                    break;
                    
                    case "len": case "choice": case "mean": case "variance": 
                    case "sum": case "count": case "index": case "append": 
                    case "extend": case "remove": 
                        if(noPar == 1)
                            return true;
                    break;
                    
                    case "sort": case "reverse": 
                        if(noPar == 0)
                            return true;
                    break;
                    
                    
                    case "pop":
                        if(noPar == 0 || noPar == 1)
                            return true;
                    break;
                }
            }
        }
        
        return false;
    }
    
    
    public String getTipoSalida(Funcion fun) {
        String tipo = null;
        
        if(fun != null) {
            Token funToken = fun.getToken();
            
            if(funToken != null) {
                String funLex = funToken.getLexema();

                switch(funLex) {
                    case "findall": case "sample": 
                        tipo = "L";
                    break;
                    
                    case "replace": 
                        tipo = "C";
                    break;
                    
                    case "len": case "randrange": case "count": case "index":  
                        tipo = "D";
                    break;
                    
                    case "choice": 
                        Operando par = fun.getLastPar();
                        
                        String parTipo = par.getTipo();
                        
                        switch(parTipo) {
                            case "V": case "L": case "A":  case "T": case "DIC":
                                tipo = "V"; 
                            break;
                            case "C": tipo = "CH"; break;
                            case "R": tipo = "D"; break;
                        }
                    break;
                    
                    case "pop": 
                        tipo = "V"; 
                    break;
                    
                    case "mean": case "variance": 
                        tipo = "DH";
                    break;
                    
                    case "sum": 
                        tipo = "V";
                    break;
                    
                    case "append": 
                        tipo = "V";
                    break;
                    
                    case "sort": case "reverse": 
                        tipo = "L";
                    break;
                }
            }
        }
        
        return tipo;
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
    
    public Operando saveTemp(Token funToken, String tipo) {
        String funLex = funToken.getLexema();

        int line = funToken.getLinea();

        return sem1.saveTemp(funLex, line, tipo); 
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
                case "findall": case "replace": 
                    topePila = "id, cadena";   break;
            }
        else
            if(noPar == 2) 
                switch(fun) {
                    case "replace": topePila = "id, cadena";   break;
                    case "findall":   topePila = "id, cadena, lista";   break;
                }
        
        return topePila;
    }
    
    public String getTopePila(Funcion fun) {
        String tipo = null;
        
        if(fun != null) {
            Token funToken = fun.getToken();
            
            if(funToken != null) {
                String funLex = funToken.getLexema();

                switch(funLex) {
                    case "findall": case "sample": 
                        tipo = "lista";
                    break;
                    
                    case "replace": 
                        tipo = "cadena";
                    break;
                    
                    case "len": case "randrange": case "count": case "index":  
                        tipo = "decimal";
                    break;
                    
                    case "choice": 
                        Operando par = fun.getLastPar();
                        
                        String parTipo = par.getTipo();
                        
                        switch(parTipo) {
                            case "V": case "L": case "A":  case "T": case "DIC":
                                tipo = "decimal, float, hex, octal, binario, "
                                        + "complejo, cadena"; 
                            break;
                            case "C": tipo = "char"; break;
                            case "R": tipo = "decimal"; break;
                        }
                    break;
                    
                    case "pop": 
                        tipo = "decimal, float, hex, octal, binario, "
                                        + "complejo, cadena"; 
                    break;
                    
                    case "mean": case "variance": 
                        tipo = "hex";
                    break;
                    
                    case "sum": 
                        tipo = "decimal, float, hex, octal, binario";
                    break;
                    
                    case "append": 
                        tipo = "cadena, lista, dicc";
                    break;
                    
                    case "sort": case "reverse": 
                        tipo = "lista";
                    break;
                }
            }
        }
        
        return tipo;
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
        return (LT <= -71 && LT >= -76) || LT == -78 || LT == -80 || LT == -81;
    }
    
    
    public boolean isFunlist(int LT) {
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

        System.out.println("\n*-------------- sem3 empty stacks----------*\n");
    }
    
    public void printStacks() {
        System.out.println("\n*------------------------\n");
        
        System.out.println(" FUNSTACK:");
        for(Funcion f : funStack)  {
            System.out.print("\n ++");
            System.out.print(" "+f.getToken().getLexema());
            
            if(f.isFunlist)
                System.out.print(" "+f.getIdFunlist().getLex());
            
            System.out.println("\n\tPARSTACK:");
        
            for(Operando o : f.getParStack()) {
                if(!o.isTemp())
                    System.out.println("\t "+o.getToken().getLexema()+" "+o.getTipo());
                else
                    System.out.println("\t T"+o.getTipo()+o.getNoTemp());
            }
            System.out.println("");
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
