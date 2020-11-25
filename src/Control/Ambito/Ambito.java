package Control.Ambito;

import Control.Conexion;
import Control.Cuadruplos.Cuadruplos;
import Control.Semantica.Semantica1;
import Control.Semantica.Semantica2;
import Model.Diccionario;
import Model.Token;
import Model.Error;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Ambito {
    public Semantica1 sem1;
    
    public Semantica2 sem2;
    
    public Cuadruplos cuad;
    
    public LinkedList<Integer> prodStack; // Sintaxis prodStack
    
    public LinkedList<Token> tokens;
    
    public LinkedList<Error> errores;
    
    public LinkedList<Integer> ambStack; // Pila de ambitos
    
    public LinkedList<Integer> keys; // Pila de keys
    
    public Token token;
    
    public boolean declaracion, negativo;
    
    public int contAmb;
    
    public Integer key;
    
    public Ambito() {
        this.con = (new Conexion()).Conectar();
        
        try {
            stmt = con.createStatement();
            
            stmt.executeUpdate(resetTable);
            stmt.executeUpdate(resetAI);
        } catch (SQLException ex) {
            Logger.getLogger(Ambito.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        declaracion = true; 
        
        contAmb = 0; 
        
        key = -1;
        
        ambStack = new LinkedList();
        
        ambStack.add(contAmb); // Crea ambito 0
        
        
        keys = new LinkedList();
    }
    
    public void checar(int LT) {
        token = tokens.peekFirst();
        
        if(identificador(LT)) {
            if(declaracion) {
                if(existeID()) 
                    setError(700);
                else 
                    addSimbolos(token);
            } else  // Esta en ejecucion
                if(!existeID()) 
                    setError(701);
        } else 
            if(!keys.isEmpty())
                key(LT);
    }
    
    
    
    public int peekLastAmb() {
        return this.ambStack.peekLast();
    }
    
    public boolean existeID() {
        id = tokens.peekFirst().getLexema();
        
        String sql;
        
        try {
            if(declaracion) {
                sql = existeID + id + "' AND amb =" + ambStack.peekLast() + " " + last;
                
                if((rs = stmt.executeQuery(sql)).next()) {
                    error = rs.getInt("error");
                    
                    return true;
                }
                
                return false;
            } else {
                for (int i = 0; i < ambStack.size(); i++) {
                    int amb = ambStack.get(i);   
                    
                    sql = existeID + id + "' AND amb =" + amb;

                    if((rs = stmt.executeQuery(sql)).next()) {
                        return true;
                    }
                    rs.close();
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Ambito.class.getName()).log(Level.SEVERE, null, ex);
        }
         
            
        return false;
    }
    
    public void zona(int PS) {
        switch(PS) {
            case 800: declaracion = false;  break;  // Zona de ejecucion
            case 801: declaracion = true;   break;  // Zona de declaracion
            
            case 802: ambStack.add(++contAmb);  break;  // Crear ambito
            case 803: ambStack.removeLast();    break;  // Cerrar ambito
        }
        
        if(declaracion) {
            switch(PS) {
                case 804: tipo = "none"; clase = "fun"; keys.offer(PS);  break;  // Funcion
                
                
                case 805: clase = "var"; keys.offer(PS);   break;  // Variable
                
                
                case 806: tipo = "none"; clase = "par"; noPar++;  keys.offer(PS);   break;  // Parametro
                case 807: tpArr = ambStack.peekLast() + ""; addSimbolos(PS); break;  // Fin parametros

                
                case 808: tipo = "struct"; clase = "tupla"; keys.offer(PS); addSimbolos(808); ambStack.add(++contAmb); clase = "datoTupla"; break; // Tupla
                case 809: keys.removeLast(); ambStack.removeLast(); addSimbolos(809); break; // Fin tupla
                
                
                case 810: initLista(); keys.offer(PS); break; // Lista
                case 822: delArrAmbito(); checarArrIntervalo(); arrStack.add(";"); break; // ;
                case 823: checarArrIntervalo(); comaArr = true; break; // ,
                case 824: delArrAmbito(); nIntervalo++; keys.offer(PS); break; // :
                case 811: checarArrIntervalo(); if(isArreglo) { setArreglo(); } else ambStack.removeLast(); addSimbolos(822); keys.removeLast(); break; // Fin lista
                
                
                case 812: tipo = "struct"; clase = "rango"; tArrRango = ""; dimArrRango = ""; keys.offer(PS); addSimbolos(808); break; // Rango
                case 813: case 814: case 815: keys.offer(PS); break;
                case 816: keys.removeLast(); break; // Fin rango
                
                
                case 817: tipo = "struct"; clase = "diccionario"; keys.offer(PS); 
                          addDiccionarioStack(); ambStack.add(++contAmb); 
                          clase = "datoDic"; valor = ""; llave = ""; 
                          break; // Diccionario
                case 818:  checarLlaveDicc(); break; // :
                case 8181:  checarValorDicc(); break; // VALOR EN DATODIC
                case 819: keys.removeLast(); ambStack.removeLast(); checarDimDicc(); 
                          diccionarioStack.removeLast(); break; // Fin diccionario
                
                
                case 820: tipo = "decimal"; clase = "var"; keys.offer(PS); break; // para FOR id 
                case 821: sem2.regla1080(token); sem2.regla1081(token); keys.removeLast(); break; // Fin FOR id
                
                
                case 853: negativo = true; break; // - Decimal
            }
        }
        
    }
    
    public void key(int LT) {
        printKeys();
        
        key = keys.peekLast();
        
        switch(key) {
            case 813: case 814: 
            case 815: rango(LT);        break;
            case 805: var(LT);          break;
            case 808: tupla(LT);        break;
            case 824: case 822: case 823: 
            case 810: lista(LT);        break;
            case 817: 
            case 818: diccionario(LT);  break;
        }
    }
    
    public boolean identificador(int LT) {
        return LT == -44;
    }
    
    
    // VARIABLE
    public void var(int LT) {
        tipo = getTipo(LT); 
        
        if(tipo != null) {
            addSimbolos(805);
            
            keys.removeLast();
        } else {
            if(LT == -38) // ; 
                if(!keys.isEmpty())
                    if(keys.peekLast().equals(805))
                        keys.removeLast();
        }
    }
    
    
    // TUPLA
    public void tupla(int LT) {
        String tipo = getTipo(LT);
        
        if(tipo != null) {
            tArr++;
            
            this.tipo = tipo;

            addSimbolos(810);
        }
    }
    
    
    // LISTA, ARREGLO
    LinkedList<String> arrStack;
    
    String tArrDim;
    
    boolean isArreglo, comaArr;
    
    int nIntervalo;
    
    public void lista(int LT) {
        String tipo = getTipo(LT);
                    
        if(tipo != null) {
            tArr++;

            tipoLista(tipo);

            addSimbolos(810);


            if(negativo) { 
                arrStack.add("-" + token.getLexema());

                negativo = false;
            } else
                arrStack.add(token.getLexema());
        }
    }
    
    public void tipoLista(String tipo) {
        if(tArr == 1) { // Si es el primer datoLista se guarda
            this.tipo = tipo;
            
            tipoLista = tipo;
            
            isArreglo = true;
        } else {
            if(!tipo.equals(this.tipo)) {// Checar si no son iguales para marcar que es lista
                tipoLista = null;  
                
                this.tipo = tipo;
                
                isArreglo = false;
            }
        }
    }
    
    public boolean arreglo() {
        return tipoLista != null;
    }
    
    public void setArreglo() {
        
        delSimbolos(811); // Elimina los datoLista
        
        String dato = null, lastDato = null;
        
        int cont = 0;
        
        if(arrStack.size() == 1) {
            dato = arrStack.removeLast();
            
            if(!isInteger(dato))
                dato = 1 + "";
        }
            
        while(!arrStack.isEmpty()) {
            dato = arrStack.removeLast();
            
            switch(dato) {
                case ";": 
                    dimArr++;
                    
                    if(comaArr) {
                        setTArr(cont); // tam
                    } else {
                        setTArr(lastDato); // Dim
                    }
                    
                    cont = 0;
                break;
                
                case ":":
                    dato = arrStack.removeLast();
                    
                    cont += Integer.parseInt(dato);
                break;
                
                default: cont++;
            }
            
            lastDato = dato;
        }
        
        if(comaArr) {
            setTArr(cont); // tam
            
            if(dimArr > 1)
                tArrDim = dimArr + ";" + tArrDim;
        } else
            setTArr(dato); // dim
        
        addSimbolos(812); // Cambia clase = arreglo y tipoLista
    }
    
    boolean arrAmb;
    
    public void delArrAmbito() {
        if(!arrAmb) {
            // Como es un arreglo eliminamos ambito creado para listas
            ambStack.removeLast();
            contAmb--; 
            
            arrAmb = true;
        }
    }
    
    public void setTArr(int dato) { // Se agrega tam
        if(tArrDim.isEmpty())
            tArrDim = dato + "";
        else
            tArrDim = dato + "," + tArrDim;
    }

    public void setTArr(String dato) { // Se agrega dim
        if(tArrDim.length() > 0)
            tArrDim = dato + ";" + tArrDim;
        else
            tArrDim = dato;
    }
    
    public void checarArrIntervalo() {
        if(key == 824) 
            setArrIntervalo();
        
    }
    
    public void setArrIntervalo() {
        int size = arrStack.size();

        if(size > 1) {
            String a1, a2, a3;
            int num1 = 0, num2, num3;
            
            if(nIntervalo == 1) {// x : x
                a3 = arrStack.removeLast();
                a2 = arrStack.removeLast();
                
                if(isInteger(a3)) {
                    if(isInteger(a2)) { // Si los dos son INT
                        num3 = getInteger(a3);
                        num2 = getInteger(a2);


                        if(num2 > num3) 
                            num1 = num2 - num3;
                        else
                            num1 = num3 - num2; // error?

                        sem2.regla1031(token, num2, num3);
                    } 
                }
            } else {
                if(size > 2)
                    if(nIntervalo == 2) { // x : x : x

                        a3 = arrStack.removeLast();
                        a2 = arrStack.removeLast();
                        a1 = arrStack.removeLast();

                        if(isInteger(a3)) {
                            if(isInteger(a2)) { // Si los dos son INT
                                if(isInteger(a1)) { // Si los tres son INT

                                    num3 = getInteger(a3);
                                    num2 = getInteger(a2);
                                    num1 = getInteger(a1);

                                    sem2.regla1031(token, num1, num2, num3);


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
                            }
                        }
                    }
            }
            
            arrStack.add(num1 + "");
            arrStack.add(":");
        }
        
        nIntervalo = 0;
        
        
        while(keys.peekLast() == 824) {
            keys.removeLast();
        }
    }
    
    public void initLista() {
        tipo = "struct"; 
        clase = "lista"; 
                         
        dimArr = 1;
        
        addSimbolos(808); 
        
        arrAmb = false;
        
        // datoLista
        ambStack.add(++contAmb); 
        clase = "datoLista"; 
        
        // tArr
        arrStack = new LinkedList(); 
        nIntervalo = 0;
        comaArr = false;
        tArrDim = "";
    }
    
    
    
    
    // RANGO
    public void rango(int LT) {
        switch(key) {
            case 813: 
                if(LT == -8) { // - Decimal
                    tArrRango = tokens.peekFirst().getLexema();
                } else {
                    tArrRango += tokens.peekFirst().getLexema() + ", ";
                }
                
                keys.removeLast();
            break;
            case 814: tArrRango += tokens.peekFirst().getLexema(); if(LT != -8) { 
                keys.removeLast(); addSimbolos(814); }break;
            case 815: dimArrRango += tokens.peekFirst().getLexema(); if(LT != -8) { 
                keys.removeLast(); addSimbolos(815); } break;
        }
    }
    
    
    
    // DICCIONARIOfdv
    public LinkedList<Diccionario> diccionarioStack;
    Token llaveT;
    
    public void diccionario(int LT) {   
        String tipo;
        
        Token token = tokens.peekFirst();
        
        switch(key) {
            case 817: 
                tipo = getTipo(LT);
        
                if(tipo != null) {
                    
                    if(negativo) {
                        llave = "-" + token.getLexema();
                        negativo = false;
                    } else
                        llave = token.getLexema();
                    
                    llaveT = token;
                    System.out.println("llave t " + llaveT.getLexema());
                    
                    this.tipo = tipo;
                }
            break;
            
        }
        
    }
    
//    public void diccionario(int LT) {
//        String tipo = getTipo(LT);
//        
//        if(tipo != null) {
//            if(tipoDicc == null)
//                tipoDicc = tipo;
//            
//            tArr++;
//            
//            lastDicc = tokens.peekFirst();
//            
//            id = lastDicc.getLexema();
//            
//            if(LT == -40 || LT == -41)
//                id = "\\" + id.substring(0, id.length()-1) + "\\" + id.charAt(id.length() -1);
//                    
//            
//            this.tipo = tipo;
//            
//            addSimbolos(key);
//        } else
//            if(LT == -8) {
//                if(key == 817)
//                    valor = "-";
//                else 
//                    llave = "-";
//            }
//                
//        
//    }
    
    public void checarLlaveDicc() {
        
        if(notNull(diccionarioStack)) {
            if(!diccionarioStack.isEmpty()) {
                Diccionario lastDicc = diccionarioStack.peekLast();
                        
                lastDicc.tArr++;

                noPar = lastDicc.tArr;
                
                tpArr = lastDicc.getId();
                
                if(notNull(lastDicc.llaveT))
                    sem2.regla1060(lastDicc, llaveT);
                else
                    lastDicc.llaveT = llaveT;
            }
        }
            
        
        addSimbolos(817);
                
        
//        if(notNull(tipoDicc))
//            if(tipoDicc.equals(getTipo(lastDicc.getToken())))
//                sem2.regla1060(lastDicc);
//            else
//                sem2.setError(1060, 766, sem2.getTipo(sem1.getTipo(lastDicc.getToken())), 
//                        lastDicc.getLexema(),lastDicc.getLinea() );
//        
//        sem2.printReglas();



    }
    
    public void checarValorDicc() {
        if(llave != null) {
            valor = llave;

            addSimbolos(818); // update valor last dato
        }
    }
    
    public void checarDimDicc() {
        if(notNull(diccionarioStack)) {
            if(!diccionarioStack.isEmpty()) {
                Diccionario lastDicc = diccionarioStack.peekLast();

                tArr = lastDicc.tArr;

                dimArr = lastDicc.dimArr;

                id = lastDicc.getId();
            }
        }
        
        addSimbolos(819);
    }
    
    public void addDiccionarioStack() {
        String[] simbolos;
        Diccionario newDicc, firstDicc;
        
        if(!notNull(diccionarioStack) || diccionarioStack.isEmpty()) { // dic = @ {}
            
            addSimbolos(808);
            
            simbolos = getLastIdSimbolos();
            
            newDicc = new Diccionario(simbolos);
            
            diccionarioStack = new LinkedList();
            
            diccionarioStack.offer(newDicc);
            
            newDicc.dimArr = 1;
            
            newDicc.tArr= 0;

        } else { // dic = @ { 1 : @ { } }
            firstDicc = diccionarioStack.peekFirst();
            
            firstDicc.dimArr = 2;
            
            simbolos = getLastIdSimbolos(); // [0] idsimbolos [1] id [2] tipo [3] clase [4] amb [5] llave [6] tpArr
            
            simbolos[1] = simbolos[6] + "_" + simbolos[5];
            
            newDicc = new Diccionario(simbolos);
            
            newDicc.dimArr = 1;
            
            newDicc.tArr= 0;
            
            diccionarioStack.offer(newDicc);
            
            id = simbolos[1];
            
            valor = id;
            
            addSimbolos(818); // update valor last dato
           
            addSimbolos(8181); // insert dicc
            
        }
            
        
        printDiccionarioStack();
    }
    
    
    
    public String getTipo(int LT) {
        String tipo = null;
        
        switch(LT) {
            case -54: 
            case -55: tipo = "boolean"; break;
            case -40: tipo = "cadena"; break;
            case -41: tipo = "caracter";  break;
            case -45: tipo = "decimal"; break;
            case -46: tipo = "flotante"; break;
            case -47: tipo = "complejo";  break;
            case -48: tipo = "binario";   break;
            case -49: tipo = "hexadecimal";  break;
            case -50: tipo = "octal"; break;
            case -56: tipo = "none";  break;
        }
        
        return tipo;
    }
    
    
    // COLUMNAS SIMBOLOS
    public String id, tipo, clase, tipoLista, valor, tpArr, llave, tArrRango, dimArrRango;
    
    public int amb, tArr, noPar, dimArr, error;
    
    public void addSimbolos(Token token) {
        String sql;
        
        key = keys.peekLast();
        
        switch(key) {
            case 804: // Funcion
                tpArr = (ambStack.peekLast() + 1) + ""; // Define el ambito que creara
                
                if(clase.equals("error")) { // Se define el núm. del error
                    clase = "fun";
                    
                    error++;
                    
                    sql = "INSERT INTO simbolos (id, tipo, clase, amb, tpArr, error) VALUES ('" 
                    + id + "', '" + tipo + "', '" + clase + "', " + ambStack.peekLast() + ", '" + tpArr + "', '" + error + "');";
                    
                } else
                    sql = "INSERT INTO simbolos (id, tipo, clase, amb, tpArr) VALUES ('" 
                        + id + "', '" + tipo + "', '" + clase + "', " + ambStack.peekLast() + ", '" + tpArr + "');";
                
                tpArr = id; // Guarda tpArr para parametros
                
                keys.removeLast();
                
                error = 0;
                
                lastFuncionId = id;
                
                lastFuncionToken = token;
            break; 
            
            case 806: // Parametro
                sql = "INSERT INTO simbolos (id, tipo, clase, amb, noPar, tpArr) VALUES ('" 
                    + token.getLexema() + "', '"+ tipo + "', '"+ clase + "', "+ ambStack.peekLast() + ", "+ noPar + ", '"+ tpArr + "');";
                
                keys.removeLast();
            break;
            
            default:
                sql = "INSERT INTO simbolos (id, tipo, clase, amb) VALUES ('" 
                    + token.getLexema() + "', '"+ tipo + "', '"+ clase + "', "+ ambStack.peekLast() + ");";
                
                
                tpArr = token.getLexema(); // Guarda tpArr para datos
        }
        
        executeUpdate(sql);
    }
    
    public void addSimbolos(int PS) {
        String sql = "";
        
        switch(PS) {
            case 805: // UPDATE LAST: tipo
                sql = update + "tipo = '" + tipo + "' " + last;
                
            break;
            
            case 807: // UPDATE LAST FUN: noPar
                sql = update + "noPar = '" + noPar + "', tpArr = '" + tpArr +"' WHERE clase = 'fun' "+ last;
                
                noPar = 0;
            break;
            
            case 808: // UPDATE LAST: tipo, clase
                sql = update + "tipo = '" + tipo + "', clase = '" + clase + "' " + last;
            break;
            
            case 809: // UPDATE LAST TUPLA: tArr
                sql = update + "tArr = '" + tArr + "' WHERE clase = 'tupla' "+ last;
                
                tArr = 0;
            break;
            
            case 810: // INSERT DATO
                sql = "INSERT INTO simbolos (tipo, clase, amb, noPar, tpArr) VALUES ('" 
                    + tipo + "', '"+ clase + "', "+ ambStack.peekLast() + ", " + tArr + ", '" + tpArr + "');";
            break;
            
            case 811: // UPDATE LAST LISTA: tArr
                sql = update + " tArr = '" + tArr + "' WHERE clase = 'lista' "+ last;
                
                tipoLista = null;
                
                tArr = 0;
            break;
            
            case 822: // UPDATE LAST LISTA O ARREGLO: tArr, dimArr
                if(tArrDim.isEmpty())
                    tArrDim = tArr + "";
                
                sql = update + " tArr = '" + tArrDim + "', dimArr = '" + dimArr + "' WHERE clase = 'lista' OR clase = 'arreglo' "+ last;
                
                tArr = 0;
                
                dimArr = 0;
                
                tArrDim = null;
            break;
            
            case 812: // UPDATE LAST LISTA: clase, tipoLista
                sql = update + "clase = 'arreglo', " + "tipoLista = '" + tipoLista + "' " + " WHERE clase = 'lista' "+ last;
                
                tipoLista = null;
            break;
            
            case 814: // UPDATE LAST RANGO: tArr
                sql = update + "tArr = '" + tArrRango + "' WHERE clase = 'rango' "+ last;
                
                tArrRango = null;
            break;
            
            case 815: // UPDATE LAST RANGO: dimArr
                sql = update + "dimArr = '" + dimArrRango + "' WHERE clase = 'rango' "+ last;
                
                dimArrRango = null;
            break;
            
            case 817: // INSERT DATODIC
                
                sql = "INSERT INTO simbolos (tipo, clase, amb, llave, noPar, tpArr) VALUES ('" 
                    + tipo + "', '"+ clase + "', "+ ambStack.peekLast() + ", '" + llave + "', " + noPar + ", '" + tpArr + "');";
                
                llave = "";
                
//                System.out.println("\nSQL 817: " + sql + "\n");
                
            break;
            
            case 818: // UPDATE LAST DATODIC: valor
                
                sql = update + "valor = '" + valor + "' WHERE clase = 'datoDic' "+ last;
                
                valor = "";
                
            break;
            
            case 8181: // INSERT DICC
                sql = "INSERT INTO simbolos (id, tipo, clase, amb) VALUES ('" 
                    + id + "', '"+ tipo + "', '"+ clase + "', "+ ambStack.peekLast() + ");";
            break;
            
            case 819: // UPDATE DICCIONARIO: tArr, dimArr
                sql = update + "tArr = '" + tArr + "', dimArr  = '" + dimArr + "' WHERE id = '"+ id +"' "+ last;
                tArr = 0;
                dimArr = 0;
                valor = null; 
                llave = null;
                id = null;
            break;
        }
        
        executeUpdate(sql);
    }
    
    public void delSimbolos(int PS) {
        String sql = "";
        
        switch(PS) {
            case 811: // DELETE WHERE clase LAST LIMIT tArr (Elimina datoLista)
                sql = delete + "clase = '" + clase + "' " + lastLimit + tArr + ";";
                delArrAmbito();
            break;
        }
        
        executeUpdate(sql);
    }
    
    Connection con;
    
    Statement stmt;
    
    ResultSet rs;
    
    String resetTable = "DELETE FROM simbolos;";
    
    String resetAI = "ALTER TABLE simbolos AUTO_INCREMENT = 1;";
    
    String existeID = "SELECT * FROM simbolos WHERE id = '";
    
    String update = "UPDATE simbolos SET ";
    
    String last = "ORDER by idsimbolos desc limit 1";
    
    String delete = "DELETE FROM simbolos WHERE ";
    
    String lastLimit = "ORDER by idsimbolos desc limit ";
    
    public void executeUpdate(String sql) {
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Ambito.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    // ERROR
    public void setError(int error) {
        
        if(key == 804 && error == 700) { // Agregamos #funcion
            clase = "error";
            addSimbolos(tokens.peekLast());
        } else 
            if(key == 806)
                noPar--;
        
        errores.add(new Error(error, token.getLinea(), token.getLexema(), desc[error - 700], "Ámbito"));
        
        if(!keys.isEmpty())
            keys.removeLast();
    }
    
    String desc[] = { 
        "Variable duplicada",
        "Variable no declarada"
    };
    
    // PASAR STACKS
    public void initAmbito(LinkedList<Integer> prodStack, LinkedList<Token> tokens, 
            LinkedList<Error> errores) {
        
        this.prodStack = prodStack;
        
        this.tokens = tokens;
        
        this.errores = errores;
    }
    
    // CONTADOR
    ContAmbito contAmbito;
    
    public void setContador() {
        contAmbito = new ContAmbito(this.con, this.stmt);
        contAmbito.setContador(contAmb);
    }
    
    public ContAmbito getContador() {
        return contAmbito;
    }
    
    // DICCCCCC
    String getLastIdSimbolos = "SELECT idsimbolos, id, tipo, clase, amb, llave, tpArr FROM simbolos " + last;
    
    public String[] getLastIdSimbolos(){
        String[] idsimbolos = null; // [0] idsimbolos [1] id [2] tipo [3] clase [4] amb [5] llave [6] tpArr
        
        System.out.println("\n** OBTENIENDO LAST");
        try {
            if((rs = stmt.executeQuery(getLastIdSimbolos)).next()) {
                idsimbolos = new String[7];

                idsimbolos[0] = rs.getString(1); // idsimbolos

                System.out.println("\nLASTIDSIMBOLOS");
                System.out.println("idsimbolos = " + idsimbolos[0]);

                idsimbolos[1] = rs.getString(2); // id
                System.out.println("id = " + idsimbolos[1]);

                idsimbolos[2] = rs.getString(3) + ""; // tipo
                System.out.println("tipo = " + idsimbolos[2]);

                idsimbolos[3] = rs.getString(4) + ""; // clase
                System.out.print("clase = " + idsimbolos[3]);

                idsimbolos[4] = rs.getString(5) + ""; // amb
                System.out.println(", amb = " + idsimbolos[4]);
                
                idsimbolos[5] = rs.getString(6) + ""; // llave
                System.out.println("llave = " + idsimbolos[5]);
                
                idsimbolos[6] = rs.getString(7) + ""; // tpArr
                System.out.println("tpArr = " + idsimbolos[6]);
            }

            rs.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(Ambito.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return idsimbolos;
    }
    
    
    
    // PARA SEMANTICA
    String getIdSimbolos = "SELECT tipo, clase, idsimbolos, tArr, dimArr, tipoLista, noPar, funcion FROM simbolos where (clase = 'var' OR clase = 'fun' OR clase = 'par' OR tipo = 'struct') && id = '";
    
    public String[] getIdSimbolos(String id){
        String[] idsimbolos = null; // [0] tipo [1] clase [2] idsimbolos [3] tArr [4] dimArr [5] tipoLista [6] noPar
        String sql;
        System.out.println("\n** BUSCANDO ID " + id);
        try {
            
            for (int i = 0; i < ambStack.size(); i++) {
                int amb = ambStack.get(i);   

                sql = getIdSimbolos + id + "' AND amb =" + amb;

                if((rs = stmt.executeQuery(sql)).next()) {
                    idsimbolos = new String[7];
                
                    idsimbolos[0] = rs.getString(1); // Tipo
                    
                    System.out.println("\nLASTIDSIMBOLOS");
                    System.out.println("TIPO = " + idsimbolos[0]);
                    
                    idsimbolos[1] = rs.getString(2); // Clase
                    System.out.println("CLASE = " + idsimbolos[1]);

                    idsimbolos[2] = rs.getInt(3) + ""; // Idsimbolos
                    System.out.println("ID = " + idsimbolos[2]);
                    
                    idsimbolos[3] = rs.getString(4) + ""; // tArr
                    System.out.print("tArr = " + idsimbolos[3]);
                    
                    idsimbolos[4] = rs.getInt(5) + ""; // dimArr
                    System.out.println(", dimArr = " + idsimbolos[4]);
                    
                    idsimbolos[5] = rs.getString(6); // tipoLista
                    System.out.println("tipoLista = " + idsimbolos[5]);
                    
                    idsimbolos[6] = rs.getInt(7) + ""; // noPar
                    System.out.println(" noPar = " + idsimbolos[6]);
                }
                
                rs.close();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Ambito.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return idsimbolos;
    }
    
    public void cambioValor(int id, String tipo) {
        String sql = "";
        
        sql = update + "tipo = '" + tipo + "' WHERE idsimbolos= '"+ id +"';";

        executeUpdate(sql);
    }
    
    public String lastFuncionId;
    public Token lastFuncionToken;
    
    public void cambioReturn(String id, String tipo) {
        String sql = "";
        
        sql = update + "tipo = '" + tipo + "' WHERE id= '"+ id + "' " + last + ";";
        
        executeUpdate(sql);
        
        lastFuncionId = null;
        
        lastFuncionToken = null;
        
    }
    
    public String getTipoDato(int noPar, String tpArr) {
        String tipo = "V";
        
        String sql = "SELECT tipo FROM simbolos WHERE noPar ='" + noPar + "' && tpArr = '" + tpArr + "';";
        
        try {
            System.out.println(sql);
            
            if((rs = stmt.executeQuery(sql)).next())
                tipo = rs.getString(1);
            
            return tipo;
            
        } catch (SQLException ex) {
            
            System.out.println(sql);
            Logger.getLogger(Ambito.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tipo;
    }
    
    public String getTipoValor(String id, String llave) {
        String tipo = "V";
        
        String sql = "SELECT valor FROM simbolos WHERE id ='" + id + "' && llave = '" + llave + "';";
        
        try {
            System.out.println(sql);
            
            if((rs = stmt.executeQuery(sql)).next())
                tipo = rs.getString(1);
            
            return tipo;
            
        } catch (SQLException ex) {
            
            System.out.println(sql);
            Logger.getLogger(Ambito.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tipo;
    }
    
    public String getTipoDatoTupla(int noPar, String tpArr) {
        String tipo = "V";
        
        String sql = "SELECT tipo FROM simbolos WHERE clase = 'datoTupla' &&"
                + " noPar ='" + noPar + "' && tpArr = '" + tpArr + "';";
//        
//        System.out.println("\nSQLEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
//        System.out.println(sql);
        
        try {
            
            if((rs = stmt.executeQuery(sql)).next())
                tipo = rs.getString(1);
            
            return tipo;
        } catch (SQLException ex) {
            
            Logger.getLogger(Ambito.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tipo;
    }
    
    public String getTipoDatoLista(int noPar, String tpArr) {
        String tipo = "V";
        
        String sql = "SELECT tipo FROM simbolos WHERE clase = 'datoLista' &&"
                + " noPar ='" + noPar + "' && tpArr = '" + tpArr + "';";
        
//        System.out.println("\nSQLEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
//        System.out.println(sql);
        
        try {
            
            if((rs = stmt.executeQuery(sql)).next())
                tipo = rs.getString(1);
            
            return tipo;
        } catch (SQLException ex) {
            
            Logger.getLogger(Ambito.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tipo;
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
    
    public void printKeys() {
        System.out.println("\n*------------------------ T: " + token.getLexema() + "\n");
        System.out.println(" KEYSTACK:");
        
        for(Integer o : keys) {
                System.out.println(o);
        }
        
        
        System.out.println("\n------------------------*\n");
    }
    
    public void printDiccionarioStack() {
//        System.out.println("\n*------------------------ T: " + token.getLexema() + "\n");
//        System.out.println(" DICCSTACK:");
//        
//        for(Diccionario o : diccionarioStack) {
//            System.out.println(o.getId());
//        }
//        
//        
//        System.out.println("\n------------------------*\n");
    }
}
