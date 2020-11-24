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
                        
                        if(regla2021(lastFun, par1)) { // verificamos num parametros de la funcion

                            //se deberia verificar regla dependiendo fun y par1
                            verificarReglasEntrada(lastFun, par1, lastFun.getNoPar());

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
                        
                        if(regla2021(lastFun, par2)) { // verificamos num parametros de la funcion

                            //se deberia verificar regla dependiendo fun y par2
                            verificarReglasEntrada(lastFun, par2, lastFun.getNoPar());

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
                        
                        // se verifican las reglas AQUI directo
                        verificarReglasSalida(lastFun);
                    }
                    
                    sem1.printStacks();
                    
                    this.printStacks();
                break; 
                
                case 873: // END FUNLIST               
                    this.printZone(PS);
                    
                    if(!funStack.isEmpty()) {
                        Funcion lastFun = funStack.removeLast();
                        
                        // se verifican las reglas AQUI directo
                        verificarReglasSalida(lastFun);
                    }
                    
                    sem1.printStacks();
                    
                    this.printStacks();
                break; 
            }
        }
    }
    
    public void verificarReglasEntrada(Funcion lastFun, Operando par, int noPar) { // REGLAS ENTRADA
        if(notNull(lastFun) && notNull(par)) {
            String funLex = lastFun.getFunLex();
            
            topePila = getTopePilaEntrada(funLex, noPar);
            
            switch(noPar) {
                case 1:
                    switch(funLex) {
                        case "findall": // 2001
                        case "replace":
                            regla2001(lastFun, par, noPar);
                        break;

                        case "len": // 2003
                        case "sample":
                        case "choice":
                        case "count":
                            regla2003(lastFun, par, noPar);
                        break;

                        case "randrange": // 2004
                        case "pop":
                        case "insert":
                            regla2004(lastFun, par, noPar);
                        break;

                        case "extend": // 2005
                            regla2005(lastFun, par, noPar);
                        break;

                        case "mean": // 2006
                        case "variance":
                        case "sum":
                            regla2006(lastFun, par, noPar);
                        break;
                        
                        case "print": // 2007
                            regla2007(lastFun, par, noPar);
                        break;

                        case "index": // 2008
                        case "append":
                        case "remove":
                            regla2008(lastFun, par, noPar);
                        break;
                    }
                break;
                
                case 2:
                    switch(funLex) {
                        case "replace": // 2001
                            regla2001(lastFun, par, noPar);
                        break;
                            
                        case "findall": // 2002
                            regla2002(lastFun, par, noPar);
                        break;
                        
                        case "randrange": // 2004
                        case "sample":
                            regla2004(lastFun, par, noPar);
                        break;
                        
                        case "print": // 2007
                            regla2007(lastFun, par, noPar);
                        break;
                        
                        case "insert": // 2008
                            regla2008(lastFun, par, noPar);
                        break;
                    }
                break;
                
                case 3:
                    switch(funLex) {
                        case "replace": // 2001
                            regla2001(lastFun, par, noPar);
                        break;
                        
                        case "print": // 2007
                            regla2007(lastFun, par, noPar);
                        break;
                    }
                break;
                
                default:
                    switch(funLex) {
                        case "print": // 2007
                            regla2007(lastFun, par, noPar);
                        break;
                    }
            }
        }
        
        this.printReglas();
    }
    
    
    public void verificarReglasSalida(Funcion lastFun) { // REGLAS SALIDA
        if(lastFun != null) {
            Token funToken = lastFun.getToken();
            
            String funLex = funToken.getLexema();
            
            String tipo = "V";
            
            verificarParametros(lastFun, funLex);
            
            Operando temp = null;
            
            switch(funLex) {
                case "findall": case "sample": // 2009
                    if(lastFun.isError()){
                        tipo = "V";  // se marca Variant
                        
                        saveTemp(funToken, tipo); // se agrega a sem1 operandos

                        temp = createTemp(funToken, tipo);
                    } else {
                        tipo = "L";
                        
                        saveTemp(funToken, tipo); // se agrega a sem1 operandos
                        
                        temp = createTemp(funToken, tipo);
                    }
                    
                    temp = saveTemp(funToken, tipo); // se agrega a sem1 operandos
                    
                    regla2009(lastFun, temp, funLex); // Marcamos regla de salida
                    
                    verificarReglasUso(lastFun, temp, funLex);
                break;
                
                case "replace": // 2010
                    if(lastFun.isError())
                        tipo = "V";  // se marca Variant
                    else
                        tipo = "C";
                    
                    temp = saveTemp(funToken, tipo); // se agrega a sem1 operandos
                    
                    regla2010(lastFun, temp, funLex);
                    
                    verificarReglasUso(lastFun, temp, funLex);
                break;
                
                case "len": case "randrange": case "count": case "index": // 2011
                    if(lastFun.isError()) {
                        tipo = "V";  // se marca Variant
                        
                        saveTemp(funToken, tipo); // se agrega a sem1 operandos

                        temp = createTemp(funToken, tipo);
                    } else {
                        tipo = "D";
                        
                        saveTemp(funToken, tipo); // se agrega a sem1 operandos
                        
                        switch(funLex) {
                            case "len": case "randrange": case "index":
                                temp = createTemp(funToken, tipo); break;
                            case "count": 
                                if(isIdentificador(lastFun.getIdFunlist())) 
                                    temp = lastFun.getIdFunlist(); 
                                else
                                    temp = createTemp(funToken, tipo);
                            break;
                            
                            
                        }
                    }
                    
                    
                    regla2011(lastFun, temp, funLex);
                    
                    switch(funLex) {
                        case "count": case "index": 
                            if(isIdentificador(lastFun.getIdFunlist())) 
                                temp = lastFun.getIdFunlist(); break;
                    }
                    
                    
                    verificarReglasUso(lastFun, temp, funLex);
                    
                break;
                
                case "choice": case "pop": // 2012
                    if(lastFun.isError()) {
                        tipo = "V";  // se marca Variant
                        
                        saveTemp(funToken, tipo); // se agrega a sem1 operandos

                        temp = createTemp(funToken, tipo);
                    } else {
                        if(isIdentificador(lastFun.peekLastPar())) {
                            tipo = lastFun.peekLastPar().getTipo();
                            
                            saveTemp(funToken, tipo); // se agrega a sem1 operandos
                            
                            temp = lastFun.peekLastPar();
                        } else {
                            
                            if(isIdentificador(lastFun.getIdFunlist())) {
                                Operando id = lastFun.getIdFunlist();
                                Operando par = lastFun.peekLastPar();
                                
                                if(notNull(par)) {
                                    if(id.getSimbolos()[1].equals("arreglo")) { // obtener tipo de tipoLista
                                        tipo = getTipo(id.getSimbolos()[5], id.getSimbolos());

                                        saveTemp(funToken, tipo); // se agrega a sem1 operandos
                                        temp = createTemp(funToken, tipo);
                                    } else {
                                        
                                        if(id.getSimbolos()[0].equals("struct")) {
                                            if(isInteger(par.getLex())) {
                                                // obtener tipo de la posicion
                                                tipo = ambito.getTipoDatoLista(getInteger(par.getLex()), id.getLex());

                                                tipo = getTipo(tipo, id.getSimbolos());

                                                temp = createTemp(funToken, tipo);
                                                
                                            } else {
                                                
                                                //devolver el ultimo
                                                
                                                tipo = ambito.getTipoDatoLista(1, id.getLex());

                                                tipo = getTipo(tipo, id.getSimbolos());

                                                temp = createTemp(funToken, tipo);
                                            }
                                        } else {
                                    
                                            tipo = "V";  // se marca Variant

                                            saveTemp(funToken, tipo); // se agrega a sem1 operandos

                                            temp = createTemp(funToken, tipo);
                                        }
                                    }
                                    
                                } else {
                                    //devolver el ultimo
                                    
                                    tipo = "V";  // se marca Variant

                                    saveTemp(funToken, tipo); // se agrega a sem1 operandos

                                    temp = createTemp(funToken, tipo);
                                }
                            } else {
                                tipo = "V";  // se marca Variant

                                saveTemp(funToken, tipo); // se agrega a sem1 operandos

                                temp = createTemp(funToken, tipo);
                                
                            }
                        }
                    }
                        
                    
                    regla2012(lastFun, temp, funLex);
                    
                    
                    switch(funLex) {
                        case "pop": case "choice":
                            if(isIdentificador(lastFun.getIdFunlist())) 
                                temp = lastFun.getIdFunlist(); break;
                    }
                    
                    verificarReglasUso(lastFun, temp, funLex);
                break;
                
                
                case "mean": case "variance": // 2013
                    if(lastFun.isError()) {
                        tipo = "V";  // se marca Variant
                        
                        saveTemp(funToken, tipo); // se agrega a sem1 operandos
                        
                        temp = createTemp(funToken, tipo);
                    } else {
                        tipo = "F";
                        
                        saveTemp(funToken, tipo); // se agrega a sem1 operandos

                        temp = createTemp(funToken, tipo);
                    }
                    
                    regla2013(lastFun, temp, funLex);
                    
                    
                    verificarReglasUso(lastFun, temp, funLex);
                break;
                
                case "sum": // 2014
                    if(lastFun.isError()) {
                        tipo = "V";  // se marca Variant
                        
                        saveTemp(funToken, tipo); // se agrega a sem1 operandos
                        
                        temp = createTemp(funToken, tipo);
                    } else {
                        // Obtener tipoLista
                        if(isIdentificador(lastFun.peekLastPar())) {
                            String[] idsimbolos = lastFun.peekLastPar().getSimbolos();
                        
                            tipo = getTipo(idsimbolos[5], idsimbolos);
                            
                            saveTemp(funToken, tipo); // se agrega a sem1 operandos
                            
                            temp = createTemp(funToken, tipo);
                        } else {
                            tipo = "V";  // se marca Variant
                            
                            saveTemp(funToken, tipo); // se agrega a sem1 operandos
                        
                            temp = createTemp(funToken, tipo);
                        }
                    }
                        
                    regla2014(lastFun, temp, funLex);
                    
                    verificarReglasUso(lastFun, temp, funLex);
                break;
                
                case "append": // 2015
                    if(lastFun.isError()) {
                        tipo = "V";  // se marca Variant
                        
                        saveTemp(funToken, tipo); // se agrega a sem1 operandos
                        
                        temp = createTemp(funToken, tipo);
                    } else {
                        // Obtener tipoLista
                        if(isIdentificador(lastFun.getIdFunlist())) {
                            String[] idsimbolos = lastFun.getIdFunlist().getSimbolos();
                        
                            tipo = getTipo(idsimbolos[0], idsimbolos);
                            
                            saveTemp(funToken, tipo); // se agrega a sem1 operandos
                            
                            temp = lastFun.getIdFunlist();
                        } else {
                            tipo = "V";  // se marca Variant
                        
                            saveTemp(funToken, tipo); // se agrega a sem1 operandos
                            
                            temp = createTemp(funToken, tipo);
                        }
                    }
                        
                    regla2015(lastFun, temp, funLex);
                    
                    verificarReglasUso(lastFun, temp, funLex);
                break;
                
                case "sort": case "reverse": // 2016
                    if(lastFun.isError()) {
                        tipo = "V";  // se marca Variant
                        
                        saveTemp(funToken, tipo); // se agrega a sem1 operandos
                        
                        temp = createTemp(funToken, tipo);
                    } else {
                        tipo = "L";
                        
                        saveTemp(funToken, tipo); // se agrega a sem1 operandos
                        
                        if(isIdentificador(lastFun.getIdFunlist()))
                            temp = lastFun.getIdFunlist();
                        else
                            temp = createTemp(funToken, tipo);
                    }
                    
                    regla2016(lastFun, temp, funLex);
                    
                    
                    verificarReglasUso(lastFun, temp, funLex);
                break;
                
                case "extend": 
                case "remove":
                case "insert":
                    if(isIdentificador(lastFun.getIdFunlist()))
                            temp = lastFun.getIdFunlist();
                        else
                            temp = createTemp(funToken, "V");
                    
                    
                    //saveTemp(funToken, "N"); // se agrega a sem1 operandos
                    
                    verificarReglasUso(lastFun, temp, funLex);
                break;
                
            }
        }
        
        this.printReglas();
    }
    
    public void verificarReglasUso(Funcion lastFun, Operando temp, String funLex) { // REGLAS USO
        if(lastFun != null) {
            
            switch(funLex) {
                case "sort": case "reverse": case "extend": // 2017
                    regla2017(lastFun, temp, funLex);
                break;
                
                case "count": case "index": // 2018
                    regla2018(lastFun, temp, funLex);
                break;
                
                case "append": // 2019
                    regla2019(lastFun, temp, funLex);
                break;
                
                case "pop": case "remove": case "insert": // 2020
                    regla2020(lastFun, temp, funLex);
                break;
            }
        }
        
        this.printReglas();
    }
    
    
    public void verificarParametros(Funcion fun, String funLex) {
        if(checarNoPar(fun, "menor")) {// checamos si hicieron falta parametros
            fun.setErrorParFaltante(true);
            
            setErrorParFaltante(fun, funLex);

            fun.setError(true);
        } 
    }

    // REGLAS ENTRADA
    public void regla2001(Funcion fun, Operando par, int noPar) {
        int idRegla = 2001;
        
        String funLex_par = fun.getFunLex() + "_par" + noPar;
        
        if(fun.isErrorParFaltante()) {
            setError(fun, idRegla, funLex_par, createTemp(fun.getToken(), fun.getFunLex()));
        } else {
            if(isVariant(par) || isCadena(par)) 
                setRegla(idRegla, funLex_par, par, "Acepta");
            else
                setError(fun, idRegla, funLex_par, par);
        }
    }
    
    public void regla2002(Funcion fun, Operando par, int noPar) {
        int idRegla = 2002;
        
        String funLex_par = fun.getFunLex() + "_par" + noPar;
        
        if(fun.isErrorParFaltante()) {
            setError(fun, idRegla, funLex_par, createTemp(fun.getToken(), fun.getFunLex()));
        } else {
            
            if(isVariant(par) || isCadena(par)) {
                 setRegla(idRegla, funLex_par, par, "Acepta");
            } else {
                if(isIdLista(par))
                    setRegla(idRegla, funLex_par, par, "Acepta");
                else
                    setError(fun, idRegla, funLex_par, par);
            }
        }
    }
    
    public void regla2003(Funcion fun, Operando par, int noPar) {
        int idRegla = 2003;
        
        String funLex_par = fun.getFunLex() + "_par" + noPar;
        
        if(fun.isErrorParFaltante()) {
            setError(fun, idRegla, funLex_par, createTemp(fun.getToken(), fun.getFunLex()));
        } else {
            if(isIdLista(par) || isIdCadena(par) || isIdDicc(par) || 
                    isIdTupla(par) || isIdRango(par)) {
                setRegla(idRegla, funLex_par, par, "Acepta");
            } else
                setError(fun, idRegla, funLex_par, par);
        }
    }
    
    public void regla2004(Funcion fun, Operando par, int noPar) {
        int idRegla = 2004;
        
        String funLex_par = fun.getFunLex() + "_par" + noPar;
        
        if(fun.isErrorParFaltante()) {
            setError(fun, idRegla, funLex_par, createTemp(fun.getToken(), fun.getFunLex()));
        } else {
            if(isVariant(par) || isDecimal(par))
                setRegla(idRegla, funLex_par, par, "Acepta");
            else
                setError(fun, idRegla, funLex_par, par);
        }
    }
    
    public void regla2005(Funcion fun, Operando par, int noPar) {
        int idRegla = 2005;
        
        String funLex_par = fun.getFunLex() + "_par" + noPar;
        
        if(fun.isErrorParFaltante()) {
            setError(fun, idRegla, funLex_par, createTemp(fun.getToken(), fun.getFunLex()));
        } else {
            if(isVariant(par)) 
                setRegla(idRegla, funLex_par, par, "Acepta");
            else
                if(isIdLista(par))
                     setRegla(idRegla, funLex_par, par, "Acepta");
                else
                    setError(fun, idRegla, funLex_par, par);
        }
    }
    
    public void regla2006(Funcion fun, Operando par, int noPar) {
        int idRegla = 2006;
        
        String funLex_par = fun.getFunLex() + "_par" + noPar;
        
        if(fun.isErrorParFaltante()) {
            setError(fun, idRegla, funLex_par, createTemp(fun.getToken(), fun.getFunLex()));
        } else {
            if(isVariant(par)) 
                setRegla(idRegla, funLex_par, par, "Acepta");
            else
                if(isIdListaNumerica(par))
                     setRegla(idRegla, funLex_par, par, "Acepta");
                else
                    setError(fun, idRegla, funLex_par, par);
        }
    }
    
    public void regla2007(Funcion fun, Operando par, int noPar) {
        int idRegla = 2007;
        
        String funLex_par = fun.getFunLex() + "_par" + noPar;
        
        
        setRegla(idRegla, funLex_par, par, "Acepta");
        
//        if(fun.isErrorParFaltante()) {
//            setError(fun, idRegla, funLex_par, createTemp(fun.getToken(), fun.getFunLex()));
//        } else {
//             if(isVariant(par))
//                setRegla(idRegla, funLex_par, par, "Acepta");
//             else
//                 if(isConstanteEntero(par) || isCadena(par) || isChar(par) ||
//                         isIdentificador(par))
//                    setRegla(idRegla, funLex_par, par, "Acepta");
//                 else
//                    setError(fun, idRegla, funLex_par, par);
//        }
    }
    
    public void regla2008(Funcion fun, Operando par, int noPar) {
        int idRegla = 2008;
        
        String funLex_par = fun.getFunLex() + "_par" + noPar;
        
        if(fun.isErrorParFaltante()) {
            setError(fun, idRegla, funLex_par, createTemp(fun.getToken(), fun.getFunLex()));
        } else {
        
            if(isVariant(par))
                setRegla(idRegla, funLex_par, par, "Acepta");
             else
                 if(isConstanteEntero(par) || isCadena(par) || isChar(par))
                    setRegla(idRegla, funLex_par, par, "Acepta");
                 else
                    setError(fun, idRegla, funLex_par, par);
        }
    }
    
    
    // REGLAS SALIDA
    public void regla2009(Funcion fun, Operando valorReal, String funLex) {
        int idRegla = 2009;
        
        topePila = getTopePilaSalida(fun);

        setRegla(idRegla, funLex, valorReal, "Acepta");
    }
    
    public void regla2010(Funcion fun, Operando temp, String funLex) {
        int idRegla = 2010;
        
        topePila = getTopePilaSalida(fun);

        setRegla(idRegla, funLex, temp, "Acepta");
    }
    
    public void regla2011(Funcion fun, Operando temp, String funLex) {
        int idRegla = 2011;
        
        topePila = getTopePilaSalida(fun);

        setRegla(idRegla, funLex, temp, "Acepta");
    }
    
    public void regla2012(Funcion fun, Operando temp, String funLex) {
        int idRegla = 2012;
        
        topePila = getTopePilaSalida(fun);

        setRegla(idRegla, funLex, temp, "Acepta");
    }
    
    public void regla2013(Funcion fun, Operando temp, String funLex) {
        int idRegla = 2013;
        
        topePila = getTopePilaSalida(fun);

        setRegla(idRegla, funLex, temp, "Acepta");
    }
    
    public void regla2014(Funcion fun, Operando temp, String funLex) {
        int idRegla = 2014;
        
        topePila = getTopePilaSalida(fun);

        setRegla(idRegla, funLex, temp, "Acepta");
    }
    
    public void regla2015(Funcion fun, Operando temp, String funLex) {
        int idRegla = 2015;
        
        topePila = getTopePilaSalida(fun);

        setRegla(idRegla, funLex, fun.getIdFunlist(), "Acepta");
    }
    
    public void regla2016(Funcion fun, Operando temp, String funLex) {
        int idRegla = 2016;
        
        topePila = getTopePilaSalida(fun);

        setRegla(idRegla, funLex, temp, "Acepta");
    }
    
    // USO
    public void regla2017(Funcion fun, Operando temp, String funLex) {
        int idRegla = 2017;
        
        topePila = getTopePilaUso(fun);
        
        Operando id = fun.getIdFunlist();
        
        if(isVariant(id)) {
            setRegla(idRegla, funLex, id, "Acepta");
        } else {
            if(isIdLista(id)) {
                setRegla(idRegla, funLex, id, "Acepta");
            } else
                setError(fun, idRegla, funLex, id);
        }
    }
    
    public void regla2018(Funcion fun, Operando temp, String funLex) {
        int idRegla = 2018;
        
        topePila = getTopePilaUso(fun);

        Operando id = fun.getIdFunlist();
        
        if(isVariant(id)) {
            setRegla(idRegla, funLex, id, "Acepta");
        } else {
            if(isIdLista(id) || isIdCadena(id) || isIdDicc(id) || isIdListaNumerica(id)) {
                setRegla(idRegla, funLex, id, "Acepta");
            } else
                setError(fun, idRegla, funLex, id);
        }
    }
    
    public void regla2019(Funcion fun, Operando temp, String funLex) {
        int idRegla = 2019;
        
        topePila = getTopePilaUso(fun);
        
        Operando id = fun.getIdFunlist();

        if(isVariant(id)) {
            setRegla(idRegla, funLex, id, "Acepta");
        } else {
            if(isIdLista(id) || isIdCadena(id) || isIdListaNumerica(id)) {
                setRegla(idRegla, funLex, id, "Acepta");
            } else
                setError(fun, idRegla, funLex, id);
        }
    }
    
    public void regla2020(Funcion fun, Operando temp, String funLex) {
        int idRegla = 2020;
        
        topePila = getTopePilaUso(fun);

        Operando id = fun.getIdFunlist();

        if(isVariant(id)) {
            setRegla(idRegla, funLex, id, "Acepta");
        } else {
            
            if(isIdLista(id) || isIdListaNumerica(id)) {
                setRegla(idRegla, funLex, id, "Acepta");
            } else
                setError(fun, idRegla, funLex, id);
        }
    }
    
    public boolean regla2021(Funcion fun, Operando par) {
        int idRegla = 2021;
        
        String funLex = fun.getFunLex();
        
        if(notNull(fun) && notNull(par)) {
            if(checarNoPar(fun, "mayor")) {
                
                topePila = fun.getFunLex();
                        
                setError(fun, idRegla, funLex, par);
                
                fun.setError(true);
                
                return false;
            } else {
                return true;
            }
        }
        
        return false;
    }

    // PARAMETROS
    public boolean checarNoPar(Funcion fun, String tipo) {
        
        if(fun != null) {
            String funLex = fun.getFunLex();
            
            int noPar = fun.getNoPar();
            
            switch(funLex) {
                case "findall": case "sample": case "randrange": case "insert": 
                    switch(tipo) {
                        case "mayor":
                            if(noPar > 2)
                                return true;
                        break;
                        
                        case "igual":
                            if(noPar == 2)
                                return true;
                        break;
                        
                        case "menor":
                            if(noPar < 2)
                                return true;
                        break;
                    }
                break;

                case "replace": 
                    switch(tipo) {
                        case "mayor":
                            if(noPar > 3)
                                return true;
                        break;
                        
                        case "igual":
                            if(noPar == 3)
                                return true;
                        break;
                        
                        case "menor":
                            if(noPar < 3)
                                return true;
                        break;
                    }
                break;

                case "len": case "choice": case "mean": case "variance": 
                case "sum": case "count": case "index": case "append": 
                case "extend": case "remove": 
                    switch(tipo) {
                        case "mayor":
                            if(noPar > 1)
                                return true;
                        break;
                        
                        case "igual":
                            if(noPar == 1)
                                return true;
                        break;
                        
                        case "menor":
                            if(noPar < 1)
                                return true;
                        break;
                    }
                break;

                case "sort": case "reverse": 
                    switch(tipo) {
                        case "mayor":
                            if(noPar > 0)
                                return true;
                        break;
                        
                        case "igual":
                            if(noPar == 0)
                                return true;
                        break;
                    }
                break;


                case "pop":
                    switch(tipo) {
                        case "mayor":
                            if(noPar > 1)
                                return true;
                        break;
                        
                        case "igual":
                            if(noPar == 0 || noPar == 1)
                                return true;
                        break;
                    }
                break;
                
                case "print":
                    switch(tipo) {
                        case "igual":
                                return true;
                    }
                break;
            }
        }
        
        return false;
    }
    
    public void setErrorParFaltante(Funcion fun, String funLex){
        int noPar = fun.getNoPar();
        
        switch(funLex) {
            case "findall": // 2001, 2002
                
                if(noPar < 1) 
                    regla2001(fun, null, 1);
                
                regla2002(fun, null, 2);
            break;
            
            case "replace": // 2001, 2001, 2001
                if(noPar < 1)
                    regla2001(fun, null, 1);
                
                if(noPar < 2)
                    regla2001(fun, null, 2);
                
                regla2001(fun, null, 3);
            break;
            
            case "len": case "choice": case "count": // 2003
                regla2003(fun, null, noPar);
            break;
            
            case "sample": // 2003, 2004
                if(noPar < 1)
                    regla2003(fun, null, 1);
                
                regla2004(fun, null, 2);
            break;
            
            case "randrange": // 2004, 2004
                if(noPar < 1)
                    regla2004(fun, null, 1);
                
                regla2004(fun, null, 2);
            break;
            
            case "insert": // 2004, 2008
                if(noPar < 1)
                    regla2004(fun, null, 1);
                
                regla2008(fun, null, 2);
            break;
            
            case "extend": // 2005
                regla2005(fun, null, 1);
            break;
            
            case "mean": case "variance": case "sum": // 2006
                regla2006(fun, null, 1);
            break;
            
            case "index": case "append": case "remove": // 2008
                regla2008(fun, null, 1);
            break;
        }
    }
    
    
    
    Operando oper; 

    String funcion, topePila, valorReal, lexOper, edo;
    
    int line, amb, tipoToken;
    
    int[] tempCont;
    
    public String getValorReal(Operando oper) {
        String valorReal = "";
        
        if(notNull(oper))
            if(oper.isTemp()) {
                if(oper.getNoTemp() == 0) 
                    oper.setNoTemp(getNoTemp(oper.getTipo()));
                    
                valorReal = getTemp(oper.getTipo(), oper.getNoTemp());
            } else
                valorReal = oper.getLex();
        
        return valorReal;
    }
    
    public String getTopePilaEntrada(String fun, int noPar) { // REGLAS ENTRADA
        String topePila = "";
        
        switch(noPar) {
            case 1:
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
                        
                    case "print":
                        topePila = "decimal, float, hex, oct, bin, complejo, cadena, char, id";   break;
                }
            break;
               
            case 2:
                switch(fun) {
                    case "replace": topePila = "id, cadena";   break;
                    
                    case "findall":   topePila = "id, cadena, lista";   break;
                    
                    case "sample": case "randrange":
                        topePila = "decimal, id";   break;
                        
                    case "insert":
                        topePila = "decimal, float, hex, oct, bin, complejo, cadena, char";   break;
                        
                    case "print":
                        topePila = "decimal, float, hex, oct, bin, complejo, cadena, char, id";   break;
                }
            break;
            
            case 3:
                switch(fun) {
                    case "replace": topePila = "id, cadena";   break;
                    
                    case "print":
                        topePila = "decimal, float, hex, oct, bin, complejo, cadena, char, id";   break;
                }
            break;
            
            default:
                switch(fun) {
                    case "print":
                        topePila = "decimal, float, hex, oct, bin, complejo, cadena, char, id";   break;
                }
        }
        
        return topePila;
    }
    
    public String getTopePilaSalida(Funcion fun) { // REGLAS SALIDA
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
                        if(notNull(par)) {
                        String parTipo = par.getTipo();
                        
                        switch(parTipo) {
                            
                            case "C": tipo = "char"; break;
                            case "R": tipo = "decimal"; break;
                            default:
                            tipo = "decimal, float, hex, octal, binario, "
                                        + "complejo, cadena"; 
                        }
                        }
                    break;
                    
                    case "pop": 
                        tipo = "decimal, float, hex, octal, binario, "
                                        + "complejo, cadena"; 
                    break;
                    
                    case "mean": case "variance": 
                        tipo = "float";
                    break;
                    
                    case "sum": 
                        tipo = "decimal, float, hex, octal, binario, complejo";
                    break;
                    
                    case "append": 
                        tipo = "cadena, lista, dicc";
                    break;
                    
                    case "sort": case "reverse": case "extend":
                        tipo = "lista"; 
                    break;
                    
                    case "remove":
                        tipo = "idlista";
                        break;
                }
            }
        }
        
        return tipo;
    }
    
    public String getTopePilaUso(Funcion fun) { // REGLAS USO
        String tipo = "";
        
        if(fun != null) {
            Token funToken = fun.getToken();
            
            if(funToken != null) {
                String funLex = funToken.getLexema();

                switch(funLex) {
                    case "sort": case "reverse": case "extend": // 2016
                        tipo = "idlista"; 
                    break;
                    
                    case "index": case "count": // 2017
                        tipo = "idlista, idcadena, idlistanumerica"; 
                    break;
                    
                    case "append": // 2018
                        tipo = "idlista, idcadena, iddicc, idlistanumerica"; 
                    break;
                    
                    case "pop": case "remove": case "insert": // 2019
                        tipo = "idlista, idlistanumerica"; 
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
    
    public Operando createTemp(Token funToken, String tempTipo) {
        String funLex = funToken.getLexema();

        int line = funToken.getLinea();
        
        Operando newOper = new Operando(new Token(funLex, line), 
                    tempTipo, true, this.getNoTemp(tempTipo));
        
        return newOper;
    }
    
    public void setTemp(Operando dato, String tipo) {
        dato.setTipo(tipo);
        
        dato.setTemp(true);
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
    
    public String getTipo(String tipoSimbolos, String[] idsimbolos) {
        String tipo = null;
        
        switch(tipoSimbolos) {
            case "boolean":     tipo = "B";  break;
            case "caracter":    tipo = "CH"; break;
            case "cadena":      tipo = "C";  break;
            case "decimal":     tipo = "D";  break;
            case "flotante":    tipo = "F";  break;
            case "complejo":    tipo = "CM"; break;
            case "binario":     tipo = "DB"; break;
            case "hexadecimal": tipo = "DH"; break;
            case "octal":       tipo = "DO"; break;
            case "none":        tipo = "V"; break; // None
            case "struct": 
                if(idsimbolos != null)
                    switch(idsimbolos[1]) {
                        case "tupla":       tipo = "T";   break;
                        case "lista":       tipo = "L";   break;
                        case "arreglo":     tipo = "A";   break;
                        case "diccionario": tipo = "DIC"; break;
                        case "rango":       tipo = "R"; break;
                    }
                else
                    tipo = "V";
            break;
            
            default: tipo = "V";
        }
        
        return tipo;
    }
    
    // SET REGLAS
    public void setRegla(int id, String funcion, Operando dato, String edo) {
        amb = ambito.ambStack.peekLast();
        
        valorReal = this.getValorReal(dato);
        
        line = dato.getToken().getLinea();
        
        listaReglas.add(new Regla(id, funcion, topePila, valorReal, line, edo, amb));
    }
    
    // SET ERRORES
    public void setError(Funcion fun, int error, String funcion, Operando dato) {
        if(notNull(dato)) {
            if(!isIdentificador(dato))
                setTemp(dato, dato.getTipo());
         
            ambito.errores.add(new Model.Error(error-2001+780, line, dato.getLex(), desc[error-2001], "Semántica 3"));
        } else
            ambito.errores.add(new Model.Error(error-2001+780, line, fun.getFunLex(), desc[error-2001], "Semántica 3"));
    
        
        setRegla(error, funcion, dato, "Error");

        fun.setError(true);
    }
    
    String desc[] = { 
        "Paramertro debe ser tipo cadena, id o idcadena",                                       //2001
        "Paramertro debe ser tipo cadena, id, idlista o  idcadena",                             //2002
        "Parametro debe ser tipo idlista, idcadena, iddicc, idtup, o idrang",                   //2003
        "Parametro debe ser tipo decimal o id",                                                 //2004
        "Parametro debe ser tipo idlista",                                                      //2005
        "Parametro debe ser tipo idlista num",                                                  //2006
        "Parametro debe ser tipo decimal, float, hex, oct, bin, complejo, cadena, char o id",   //2007
        "Parametro debe ser tipo decimal, float, hex, oct, bin, complejo, cadena o char",       //2008
        "", //2009
        "", //2010
        "", //2011
        "", //2012
        "", //2013
        "", //2014
        "", //2015
        "", //2016
        "Debe ser idlista",                                                 //2017
        "Debe ser idlista, idcadena o idlistanumerica",                     //2018
        "Debe ser idlista, idcadena, iddiccionario o idlistanumerica",      //2019
        "Debe ser idlista o idlistanumerica",                               //2020
        "Parametro excedido",                                               //2021
    };
    
    // FUNCION
    public boolean isFuncion(int LT) {
        return (LT <= -71 && LT >= -76) || LT == -78 || LT == -80 || LT == -81 || LT == -82;
    }
    
    
    public boolean isFunlist(int LT) {
        return (LT <= -61 && LT >= -69);
    }
    
    // LISTA REGLAS
    public void printReglas() {
        System.out.println("\n//////////////////////// SEM 3 /////////////////////////");
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
    
    public boolean isVariant(Operando oper) {
        if(notNull(oper))
            if(oper.getTipo().equals("V"))
                return true;
        
        return false;
    }
    
    public boolean isDecimal(Operando oper) {
        if(notNull(oper))
            if(oper.getTipo().equals("D"))
                return true;
        
        return false;
    }
    
    public boolean isFloat(Operando oper) {
        if(notNull(oper))
            if(oper.getTipo().equals("F"))
                return true;
        
        return false;
    }
    
    public boolean isConstanteEntero(Operando oper) {
        if(notNull(oper)) {
            String tipo = oper.getTipo();
            
            if(tipo.equals("D") || tipo.equals("F") || tipo.equals("DO") || tipo.equals("DH") || 
                    tipo.equals("DB") || 
                    tipo.equals("CM"))
                return true;
        }
        
        return false;
    }
    
    public boolean isCadena(Operando oper) {
        if(notNull(oper))
            if(oper.getTipo().equals("C"))
                return true;
        
        return false;
    }
    
    public boolean isChar(Operando oper) {
        if(notNull(oper))
            if(oper.getTipo().equals("CH"))
                return true;
        
        return false;
    }
    
    public boolean isIdentificador(Operando oper) {
        if(notNull(oper))
            if(notNull(oper.getSimbolos()))
                return true;
        
        return false;
    }
    
    
    public boolean isIdLista(Operando oper) {
        if(isIdentificador(oper))
            if(oper.getTipo().equals("L") || oper.getTipo().equals("A")) 
                return true;
        
        return false;
    }
    
    public boolean isIdCadena(Operando oper) {
        if(isIdentificador(oper))
            if(oper.getTipo().equals("C")) 
                return true;
        
        return false;
    }
    
    public boolean isIdDicc(Operando oper) {
        if(isIdentificador(oper))
            if(oper.getTipo().equals("DIC")) 
                return true;
        
        return false;
    }
    
    public boolean isIdTupla(Operando oper) {
        if(isIdentificador(oper))
            if(oper.getTipo().equals("T")) 
                return true;
        
        return false;
    }
    
    public boolean isIdRango(Operando oper) {
        if(isIdentificador(oper))
            if(oper.getTipo().equals("R")) 
                return true;
        
        return false;
    }
    
    public boolean isIdListaNumerica(Operando oper) {
        if(isIdentificador(oper))
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
    
    public int getInteger(String num) {
        if(isInteger(num))
            return Integer.parseInt(num);
        else
            return -1;
    }
    
    public boolean notNull(Object dato) {
        return dato != null;
    }
    
    
    // CONTADOR
    ContSemantica3 contador;
    
    public ContSemantica3 getContador() {
        return contador;
    }
    
    public void setContador(int contAmb) {
        contador.listaReglas = this.listaReglas;
        
        contador.setContSalida();
    }
}
