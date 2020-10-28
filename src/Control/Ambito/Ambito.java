package Control.Ambito;

import Control.Conexion;
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
    public LinkedList<Integer> prodStack; // Sintaxis prodStack
    
    public LinkedList<Token> tokens;
    
    public LinkedList<Error> errores;
    
    public LinkedList<Integer> ambStack; // Pila de ambitos
    
    public boolean declaracion;
    
    int contAmb, key;
    
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
    }
    
    public void checar(int LT) {
        if(identificador(LT)) {
            if(declaracion) {
                if(existeID()) 
                    setError(700);
                else 
                    addSimbolos(tokens.peekFirst());
            } else  // Esta en ejecucion
                if(!existeID()) 
                    setError(701);
        } else 
            if(key != -1)
                key(LT);
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
                case 804: tipo = "none"; clase = "fun"; key = PS;   break;  // Funcion
                
                case 805: clase = "var"; key = PS;   break;  // Variable
                
                case 806: tipo = "none"; clase = "par"; noPar++;  key = PS;   break;  // Parametro
                case 807: tpArr = ambStack.peekLast() + ""; addSimbolos(PS); break;  // Fin parametros

                case 808: tipo = "struct"; clase = "tupla"; key = PS; addSimbolos(808); ambStack.add(++contAmb); clase = "datoTupla"; break; // Tupla
                case 809: key = -1; ambStack.removeLast(); addSimbolos(809); break; // Fin tupla
                
                case 810: tipo = "struct"; clase = "lista"; key = PS; addSimbolos(808); ambStack.add(++contAmb); clase = "datoLista"; break; // Lista
                case 811: key = -1; ambStack.removeLast(); if(tipoLista != null) { delSimbolos(811); addSimbolos(812); } addSimbolos(811); break; // Fin lista
                
                case 812: tipo = "struct"; clase = "rango"; tArrRango = ""; dimArrRango = ""; key = PS; addSimbolos(808); break; // Rango
                case 813: case 814: case 815: key = PS; break;
                case 816: key = -1; break; // Fin rango
                
                case 817: tipo = "struct"; clase = "diccionario"; key = PS; addSimbolos(808); ambStack.add(++contAmb); clase = "datoDic"; valor = ""; llave = ""; break; // Diccionario
                case 818: key = PS; break;
                case 819: key = -1; ambStack.removeLast(); addSimbolos(819); break; // Fin diccionario
                
                case 820: tipo = "decimal"; clase = "var"; key = PS; break; // para FOR id 
                case 821: key = -1; break; // Fin FOR id
            }
        }
        
    }
    
    public void key(int LT) {
        switch(key) {
            case 813: case 814: 
            case 815: rango(LT);        break;
            case 805: var(LT);          break;
            case 808: tupla(LT);        break;
            case 810: lista(LT);        break;
            case 817: 
            case 818: diccionario(LT);  break;
        }
    }
    
    public boolean identificador(int LT) {
        return LT == -44;
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
    
    // VARIABLE
    public void var(int LT) {
        tipo = getTipo(LT); 
        
        if(tipo != null)
            addSimbolos(805);
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
    
    // LISTA
    public void lista(int LT) {
        String tipo = getTipo(LT);
        
        if(tipo != null) {
            tArr++;
            
            tipoLista(tipo);
            
            addSimbolos(810);
        }
    }
    
    public void tipoLista(String tipo) {
        if(tArr == 1) { // Si es el primer datoLista se guarda
            this.tipo = tipo;
            tipoLista = tipo;
        } else {
            if(!tipo.equals(this.tipo)) {// Checar si no son iguales para marcar sacarlos
                tipoLista = null;
                this.tipo = tipo;
            }
        }
    }
    
    // RANGO
    public void rango(int LT) {
        switch(key) {
            case 813: 
                if(LT == -8) {
                    tArrRango = tokens.peekFirst().getLexema();
                } else {
                    tArrRango += tokens.peekFirst().getLexema() + ", ";
                    key = -1;
                }
            break;
            case 814: tArrRango += tokens.peekFirst().getLexema(); if(LT != -8) { key = -1; addSimbolos(814); }break;
            case 815: dimArrRango += tokens.peekFirst().getLexema(); if(LT != -8) { key = -1; addSimbolos(815); } break;
        }
    }
    
    
    
    // DICCIONARIO
    public void diccionario(int LT) {
        String tipo = getTipo(LT);
        
        if(tipo != null) {
            tArr++;
            
            id = tokens.peekFirst().getLexema();
            
            if(LT == -40 || LT == -41)
                id = "\\" + id.substring(0, id.length()-1) + "\\" + id.charAt(id.length() -1);
                    
            
            this.tipo = tipo;
            
            addSimbolos(key);
        } else
            if(LT == -8) {
                if(key == 817)
                    valor = "-";
                else 
                    llave = "-";
            }
                
        
    }
    
    // COLUMNAS SIMBOLOS
    String id, tipo, clase, tipoLista, valor, tpArr, llave, tArrRango, dimArrRango;
    
    int amb, tArr, noPar, dimArr, error;
    
    public void addSimbolos(Token token) {
        String sql;
        
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
                
                key = -1;
                
                error = 0;
            break; 
            
            case 806: // Parametro
                sql = "INSERT INTO simbolos (id, tipo, clase, amb, noPar, tpArr) VALUES ('" 
                    + token.getLexema() + "', '"+ tipo + "', '"+ clase + "', "+ ambStack.peekLast() + ", "+ noPar + ", '"+ tpArr + "');";
                
                key = -1;
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
                
                key = 0;
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
            
            case 811: // UPDATE LAST LISTA O ARREGLO: tArr
                sql = update + " tArr = '" + tArr + "' WHERE clase = 'lista' OR clase = 'arreglo' "+ last;
                
                tipoLista = null;
                
                tArr = 0;
            break;
            
            case 812: // UPDATE LAST LISTA: clase arreglo
                sql = update + "clase = 'arreglo', " + "tipoLista = '" + tipoLista + "' "  +" WHERE clase = 'lista' "+ last;
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
                valor += id;
                
                sql = "INSERT INTO simbolos (tipo, clase, amb, valor, noPar, tpArr) VALUES ('" 
                    + tipo + "', '"+ clase + "', "+ ambStack.peekLast() + ", '" + valor + "', " + tArr + ", '" + tpArr + "');";
                
                valor = "";
            break;
            
            case 818: // UPDATE LAST DATODIC: llave
                llave += id;
                
                sql = update + "llave = '" + llave + "' WHERE clase = 'datoDic' "+ last;
                
                llave = "";
                
                key = 817;
                
                tArr--;
            break;
            
            case 819: // UPDATE LAST DICCIONARIO: tArr, dimArr
                sql = update + "tArr = '" + tArr + "', dimArr  = '1' WHERE clase = 'diccionario' "+ last;
                tArr = 0;
                valor = null; 
                llave = null;
            break;
        }
        
        executeUpdate(sql);
    }
    
    public void delSimbolos(int PS) {
        String sql = "";
        
        switch(PS) {
            case 811: // DELETE WHERE clase LAST LIMIT tArr
                sql = delete + "clase = '" + clase + "' " + lastLimit + tArr + ";";
                
                contAmb--; // Como es un arreglo eliminamos ambito del contador
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
        Token token = tokens.peekFirst();
        
        if(key == 804 && error == 700) { // Agregamos #funcion
            clase = "error";
            addSimbolos(tokens.peekLast());
        } else 
            if(key == 806)
                noPar--;
        
        errores.add(new Error(error, token.getLinea(), token.getLexema(), desc[error - 700], "Ámbito"));
        
        key = -1;
    }
    
    String desc[] = { 
        "Variable duplicada",
        "Variable no declarada"
    };
    
    // PASAR PILAS
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
    
    // PARA SEMANTICA
    String getIdSimbolos = "SELECT tipo, clase, idsimbolos FROM simbolos where (clase = 'var' OR clase = 'par' OR tipo = 'struct') && id = '";
    
    public String[] getIdSimbolos(String id){
        String[] idsimbolos = null;
        String sql;
        System.out.println("\n** BUSCANDO ID " + id);
        try {
            
            for (int i = 0; i < ambStack.size(); i++) {
                int amb = ambStack.get(i);   

                sql = getIdSimbolos + id + "' AND amb =" + amb;

                if((rs = stmt.executeQuery(sql)).next()) {
                    idsimbolos = new String[3];
                
                    idsimbolos[0] = rs.getString(1); // Tipo
                    
                    System.out.println("\nLASTIDSIMBOLOS");
                    System.out.println("TIPO = " + idsimbolos[0]);
                    
                    idsimbolos[1] = rs.getString(2); // Clase
                    System.out.println("CLASE = " + idsimbolos[1]);

                    idsimbolos[2] = rs.getInt(3) + ""; // Idsimbolos
                    System.out.println("ID = " + idsimbolos[2]);
                }
                
                rs.close();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Ambito.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return idsimbolos;
    }
}
