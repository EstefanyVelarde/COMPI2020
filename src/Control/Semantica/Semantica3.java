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
        
        contador = new ContSemantica3();
        
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
                        
                        if(regla2020(lastFun, par1)) { // verificamos num parametros de la funcion

                            //se deberia verificar regla dependiendo fun y par1
                            verificarReglas(lastFun, par1, lastFun.getParStackSize());

                            if(par1.isTemp())
                                System.out.println("++ PAR1: " + "T" + par1.getTipo());
                            else
                                System.out.println("++ PAR1: " + par1.getLex());
                        }
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
                        
                        if(regla2020(lastFun, par2)) { // verificamos num parametros de la funcion

                            //se deberia verificar regla dependiendo fun y par2
                            verificarReglas(lastFun, par2, lastFun.getParStackSize());

                            if(par2.isTemp())
                                System.out.println("++ PAR2: " + "T" + par2.getTipo());
                            else
                                System.out.println("++ PAR2: " + par2.getLex());
                        }
                    }
                    
                    sem1.printStacks();
                    
                    this.printStacks();
                break;
                
                case 872: // END FUN                   
                    this.printZone(PS);
                    
                    if(!funStack.isEmpty()) {
                        Funcion lastFun = funStack.removeLast();
                        
                        if(checarNoPar(lastFun)) { // verificamos num parametros de la funcion
                            
                            String tipo = getTipoSalida(lastFun); // se marca el tipo que regresa la funcion
                            
                            if(tipo != null) { // Si es SALIDA
                                Token funToken = lastFun.getToken();
                                
                                Operando temp = saveTemp(funToken, tipo);  // se agrega a sem1 operandos
                                
                                verificarReglas(lastFun, temp);
                            } else { // es sort, reverse o extend, remove, insert
                                
                            } 
                            
                        } else { // Set error 2020 TV num parametros incorrectos ?? q faltan parametros
                            
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

                            if(checarNoPar(lastFun)) { // verificamos num parametros de la funcion 
                                
                                String tipo = getTipoSalida(lastFun); // se marca el tipo que regresa la funcion

                                if(tipo != null) { // Si es SALIDA
                                    Token funToken = lastFun.getToken();

                                    Operando temp = saveTemp(funToken, tipo); // se agrega a sem1 operandos

                                    verificarReglas(lastFun, temp);
                                } else { // es sort, reverse o extend, remove, insert

                                } 
                            } else { // Set error 2020 TV num parametros incorrectos ?? q faltan parametros
                                
                            }
                        } else { // set error y variant? o solo variant?
                            
                        }
                    }
                    
                    sem1.printStacks();
                    
                    this.printStacks();
                break; 
            }
        }
    }
    
    public void verificarReglas(Funcion lastFun, Operando par, int noPar) { // REGLAS ENTRADA
        if(notNull(lastFun) && notNull(par)) {
            Token funToken = lastFun.getToken();
            
            String funLex = funToken.getLexema();
            
            String funLex_par = funLex + "_par" + noPar;
            
            if(noPar == 1) {
                topePila = getTopePila(funLex, noPar);
                
                switch(funLex) {
                    case "findall": // 2001
                    case "replace":
                        regla2001(par, funLex_par);
                    break;
                    
                    case "len": // 2003
                    case "sample":
                    case "choice":
                    case "count":
                        regla2003(par, funLex_par);
                    break;
                    
                    case "randrange": // 2004
                    case "pop":
                    case "insert":
                        regla2004(par, funLex_par);
                    break;
                    
                    case "extend": // 2005
                        regla2005(par, funLex_par);
                    break;
                    
                    case "mean": // 2006
                    case "variance":
                    case "sum":
                        regla2006(par, funLex_par);
                    break;
                    
                    case "index": // 2007
                    case "append":
                    case "remove":
                        regla2007(par, funLex_par);
                    break;
                }
                
            } else {
                if(noPar == 2) {
                    topePila = getTopePila(funLex, noPar);
                    
                    switch(funLex) {
                        case "replace": // 2001
                            regla2001(par, funLex_par);
                        break;
                            
                        case "findall": // 2002
                            regla2002(par, funLex_par);
                        break;
                        
                        case "randrange": // 2004
                        case "sample":
                            regla2004(par, funLex_par);
                        break;
                        
                        case "insert": // 2007
                            regla2007(par, funLex_par);
                        break;
                    }

                }
            }
        }
        
        this.printReglas();
    }
    
    
    public void verificarReglas(Funcion lastFun, Operando temp) { // REGLAS SALIDA & USO
        if(lastFun != null) {
            Token funToken = lastFun.getToken();
            
            String funLex = funToken.getLexema();
            
            switch(funLex) {
                case "findall":  // 2008
                case "sample":
                    regla2008(lastFun, temp, funLex);
                break;
                
                case "replace": // 2009
                    regla2009(lastFun, temp, funLex);
                break;
                
                case "len": case "randrange": case "index": // 2010
                    regla2010(lastFun, temp, funLex);
                break;
                
                case "choice": case "pop": // 2011
                    regla2011(lastFun, temp, funLex);
                break;
                
                case "mean": case "variance": // 2012
                    regla2012(lastFun, temp, funLex);
                break;
                
                case "sum": // 2013
                    regla2013(lastFun, temp, funLex);
                break;
                
                case "append": // 2014, 2018
                    regla2014(lastFun, temp, funLex);
                    regla2018(lastFun, temp, funLex);
                break;
                
                case "sort": case "reverse": // 2015, 2016
                    regla2015(lastFun, temp, funLex);
                    regla2016(lastFun, temp, funLex);
                break;
                
                case "extend": // 2016
                    regla2016(lastFun, temp, funLex);
                break;
                
                case "count": // 2010, 2017
                    regla2010(lastFun, temp, funLex);
                    regla2017(lastFun, temp, funLex);
                break;
                
                case "remove": case "insert": // 2019
                    regla2019(lastFun, temp, funLex);
                break;
                
                
            }
        }
        
        this.printReglas();
    }
    
    // REGLAS
    public void regla2001(Operando par, String funLex) {
        int idRegla = 2001;
        
        if(isVariant(par)) 
            setRegla(idRegla, funLex, par, "Acepta");
        else
            if(par.getTipo().equals("C") || isIdentificador(par))
                setRegla(idRegla, funLex, par, "Acepta");
            else 
                setError(idRegla, funLex, par);
    }
    
    public void regla2002(Operando par, String funLex) {
        int idRegla = 2002;
        
        if(par.getTipo().equals("C") 
                || (par.getTipo().equals("L") 
                && par.getSimbolos() != null)
                || isIdentificador(par)
                || isVariant(par))
            setRegla(idRegla, funLex, par, "Acepta");
        else 
            setError(idRegla, funLex, par);
    }
    
    public void regla2003(Operando par, String funLex) {
        int idRegla = 2003;
        
        if(isIdentificador(par)) {
            if(par.getTipo().equals("L") || 
                    par.getTipo().equals("C") || 
                    par.getTipo().equals("DIC") || 
                    par.getTipo().equals("T") || 
                    par.getTipo().equals("R") || isVariant(par))
                setRegla(idRegla, funLex, par, "Acepta");
            else
                setError(idRegla, funLex, par);
        } else
            setError(idRegla, funLex, par);
    }
    
    public void regla2004(Operando par, String funLex) {
        int idRegla = 2004;
        
        if(isVariant(par))
            setRegla(idRegla, funLex, par, "Acepta");
        else
            if(isIdentificador(par) || par.getTipo().equals("D"))
                    setRegla(idRegla, funLex, par, "Acepta");
            else
                setError(idRegla, funLex, par);
    }
    
    public void regla2005(Operando par, String funLex) {
        int idRegla = 2005;
        
        if(isIdentificador(par)) {
            if(par.getTipo().equals("L"))
                setRegla(idRegla, funLex, par, "Acepta");
            else
                if(isVariant(par)) 
                    setRegla(idRegla, funLex, par, "Acepta");
                else
                    setError(idRegla, funLex, par);
        } else
            setError(idRegla, funLex, par);
    }
    
    public void regla2006(Operando par, String funLex) {
        int idRegla = 2006;
        
        if(isIdentificador(par)) {
            if(isVariant(par)) 
                setRegla(idRegla, funLex, par, "Acepta");
            else 
                if(isListaNumerica(par)) 
                    setRegla(idRegla, funLex, par, "Acepta");
                else
                    setError(idRegla, funLex, par);
        } else
            setError(idRegla, funLex, par);
    }
    
    public void regla2007(Operando par, String funLex) {
        int idRegla = 2007;
        
        if(isConstanteEntero(par) || isVariant(par) ||
                par.getTipo().equals("CM") || par.getTipo().equals("C") || 
                par.getTipo().equals("CH"))
            
                setRegla(idRegla, funLex, par, "Acepta");
        else
            setError(idRegla, funLex, par);
    }
    
    public void regla2008(Funcion fun, Operando temp, String funLex) {
        int idRegla = 2008;
        
        topePila = getTopePila(fun);

        setRegla(idRegla, funLex, temp, "Acepta");
    }
    
    public void regla2009(Funcion fun, Operando temp, String funLex) {
        int idRegla = 2009;
        
        topePila = getTopePila(fun);

        setRegla(idRegla, funLex, temp, "Acepta");
    }
    
    public void regla2010(Funcion fun, Operando temp, String funLex) {
        int idRegla = 2010;
        
        topePila = getTopePila(fun);

        setRegla(idRegla, funLex, temp, "Acepta");
    }
    
    public void regla2011(Funcion fun, Operando temp, String funLex) {
        int idRegla = 2011;
        
        topePila = getTopePila(fun);

        setRegla(idRegla, funLex, temp, "Acepta");
    }
    
    public void regla2012(Funcion fun, Operando temp, String funLex) {
        int idRegla = 2012;
        
        topePila = getTopePila(fun);

        setRegla(idRegla, funLex, temp, "Acepta");
    }
    
    public void regla2013(Funcion fun, Operando temp, String funLex) {
        int idRegla = 2013;
        
        topePila = getTopePila(fun);

        setRegla(idRegla, funLex, temp, "Acepta");
    }
    
    public void regla2014(Funcion fun, Operando temp, String funLex) {
        int idRegla = 2014;
        
        topePila = getTopePila(fun);

        setRegla(idRegla, funLex, fun.getIdFunlist(), "Acepta");
    }
    
    public void regla2015(Funcion fun, Operando temp, String funLex) {
        int idRegla = 2015;
        
        topePila = getTopePila(fun);

        setRegla(idRegla, funLex, temp, "Acepta");
    }
    
    public void regla2016(Funcion fun, Operando temp, String funLex) {
        int idRegla = 2016;
        
        topePila = getTopePila(fun);

        setRegla(idRegla, funLex, temp, "Acepta");
    }
    
    public void regla2017(Funcion fun, Operando temp, String funLex) {
        int idRegla = 2017;
        
        topePila = getTopePila(fun);

        setRegla(idRegla, funLex, temp, "Acepta");
    }
    
    public void regla2018(Funcion fun, Operando temp, String funLex) {
        int idRegla = 2018;
        
        topePila = getTopePila(fun);

        setRegla(idRegla, funLex, temp, "Acepta");
    }
    
    public void regla2019(Funcion fun, Operando temp, String funLex) {
        int idRegla = 2019;
        
        topePila = getTopePila(fun);

        setRegla(idRegla, funLex, temp, "Acepta");
    }
    
    public boolean regla2020(Funcion fun, Operando par) {
        int idRegla = 2020;
        
        if(notNull(fun) && notNull(par))
            if(!checarNoPar(fun)) {
                String funLex = fun.getToken().getLexema();
                
                setError(idRegla, funLex, par);
                
                return false;
            } else
                return true;
        
        return false;
    }
    
    // PARAMETROS
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
    
    public String getTopePila(String fun, int noPar) { // REGLAS ENTRADA
        String topePila = "";
        
        if(noPar == 1) 
            switch(fun) {
                case "findall": case "replace": 
                    topePila = "id, cadena";   break;
                    
                case "len": case "sample":  case "choice":  case "count": 
                    topePila = "idlista, idcadena, iddicc, idtupla, idrang";   break;
                    
                case "randrange": case "pop":  case "insert":
                    topePila = "decimal, id";   break;
                    
                case "extend":
                    topePila = "idlista";   break;
                    
                case "mean": case "variance":  case "sum":
                    topePila = "idlistanum";   break;
                    
                case "index": case "append":  case "remove":
                    topePila = "decimal, float, hex, oct, bin, complejo, cadena, char";   break;
            }
        else
            if(noPar == 2) 
                switch(fun) {
                    case "replace": topePila = "id, cadena";   break;
                    
                    case "findall":   topePila = "id, cadena, lista";   break;
                    
                    case "sample": case "randrange":
                        topePila = "decimal, id";   break;
                        
                    case "insert":
                        topePila = "decimal, float, hex, oct, bin, complejo, cadena, char";   break;
                }
        
        return topePila;
    }
    
    public String getTopePila(Funcion fun) { // REGLAS SALIDA
        String tipo = "";
        
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
                        Operando par = fun.peekLastPar();
                        
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
    
    public String getTipoSalida(Funcion fun) {
        Operando par = null;
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
                        par = fun.peekLastPar();
                        
                        if(par != null) {
                            String parTipo = par.getTipo();

                            switch(parTipo) {
                                case "V": case "L": case "A":  case "T": case "DIC":
                                    tipo = "V"; 
                                break;
                                case "C": tipo = "CH"; break;
                                case "R": tipo = "D"; break;
                            }
                        } else
                            tipo = "V";
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
                        Operando idFunlist = fun.getIdFunlist();
                        
                        if(idFunlist != null) {
                            String tipoIdFunlist = idFunlist.getTipo();
                            
                            switch(tipoIdFunlist) {
                                case "L": tipo = "L";
                                
                                default: tipo = "V";
                            }
                        } else 
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
    
    
    public Operando saveTemp(Token funToken, String tipo) {
        String funLex = funToken.getLexema();

        int line = funToken.getLinea();

        return sem1.saveTemp(funLex, line, tipo); 
    }
    
    public void setTemp(Operando dato, String tipo) {
        dato.setTipo(tipo);
        
        int noTemp = this.getNoTemp(tipo);
        
        dato.setNoTemp(noTemp);
        
        dato.setTemp(true);
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
        "Paramertro debe ser tipo cadena, id o idcadena",                                   //2001
        "Paramertro debe ser tipo cadena, id, idlista o  idcadena",                         //2002
        "Parametro debe ser tipo idlista, idcadena, iddicc, idtup, o idrang",               //2003
        "Parametro debe ser tipo decimal o id",                                             //2004
        "Parametro debe ser tipo idlista",                                                  //2005
        "Parametro debe ser tipo idlista num",                                              //2006
        "Parametro debe ser tipo decimal, float, hex, oct, bin, complejo, cadena o char",   //2007
        "", //2008
        "", //2009
        "", //2010
        "", //2011
        "", //2012
        "", //2013
        "", //2014
        "", //2015
        "", //2016
        "", //2017
        "", //2018
        "", //2019
        "Parametro excedido",  //2020
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
    
    public boolean isIdentificador(Operando oper) {
        if(notNull(oper))
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
    
    public boolean isConstanteEntero(Operando oper) {
        if(oper != null) {
            String tipo = oper.getTipo();
            
            if(tipo.equals("D") || tipo.equals("DO") || tipo.equals("DH") || 
                    tipo.equals("DB"))
                return true;
        }
        
        return false;
    }
    
    public boolean isListaNumerica(Operando oper) {
        if(oper != null && oper.getSimbolos() != null)
            if(oper.getTipo().equals("A")) 
                    if(oper.getSimbolos()[5].equals("decimal")) 
                        return true;
        
        return false;
    }
    
    public boolean isInteger(String numero){
        try{
            Integer.parseInt(numero);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
    
    public boolean notNull(Operando dato) {
        return dato != null;
    }
    
    public boolean notNull(Funcion dato) {
        return dato != null;
    }
    
    // CONTADOR
    ContSemantica3 contador;
    
    public ContSemantica3 getContador() {
        return contador;
    }
    
    public void setContador(int contAmb) {
        contador.listaReglas = this.listaReglas;
        
        contador.setAparicion();
        
        contador.setAmb(contAmb);
    }
}
