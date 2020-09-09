package Control;

import Model.Token;
import Model.Error;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Ambito {
    LinkedList<Integer> prodStack; // Sintaxis prodStack
    
    LinkedList<Token> tokens;
    
    LinkedList<Error> errores;
    
    LinkedList<Integer> ambStack; // Pila de ambitos
    
    boolean declaracion;
    
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
    
    public void checar(int PS, int LT) {
        if(LT == -44) { // Si es ID
            if(declaracion) {
                if(existeID()) {
                    setError(700);
                } else {
                    addSimbolos(tokens.peekFirst());
                }
            } else { // Esta en ejecucion
                if(!existeID()) {
                    setError(701);
                }
            }
        } else 
            if(key != -1)
                key(LT);
    }
    
    public boolean existeID() {
        id = tokens.peekFirst().getLexema();
        String sql;
        
        try {
            if(key == 806 || key == 876) { // Si es parametro, for id: solo se busca en ese ambito
                sql = existeID + id + "' AND amb =" + ambStack.peekLast();
                System.out.println("\n ++++ KEY : "+ key + "  SQL: "+ sql);
                return (rs = stmt.executeQuery(sql)).next();
            } else {
                Iterator it = ambStack.iterator();
            
                while(it.hasNext()) {
                    int amb = (int) it.next();   
                    
                    sql = existeID + id + "' AND amb =" + amb;
                    
                    System.out.println("\n ++++ KEY : "+ key + "  SQL: "+ sql);

                    if((rs = stmt.executeQuery(sql)).next()) {
                        if(key == 804) // Si es fun guardar clase para marcar error
                            clase = rs.getString(4);
                        return true;
                    }
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Ambito.class.getName()).log(Level.SEVERE, null, ex);
        }
         
            
        return false;
    }
    
    public void zona(int PS, int LT) {
        switch(PS) {
            case 800: declaracion = false;  break;  // Zona de ejecucion
            case 801: declaracion = true;   break;  // Zona de declaracion
            
            case 802: ambStack.add(++contAmb);  break;  // Crear ambito
            case 803: ambStack.removeLast();    break;  // Cerrar ambito
            
            case 807: tpArr = ambStack.peekLast() + ""; addSimbolos(PS); break;  // Fin parametros
        }
        
        if(declaracion) {
            switch(PS) {
                case 804: tipo = "real"; clase = "fun"; key = PS;   break;  // Funcion
                
                case 805: clase = "var"; key = PS;   break;  // Variable
                
                case 806: tipo = "none"; clase = "par"; noPar++;  key = PS;   break;  // Parametro
               
                case 808: tipo = "struct"; clase = "tupla"; key = PS; addSimbolos(808); break; // Tupla
                case 809: key = -1; break; // Fin tupla
                
                case 810: tipo = "struct"; clase = "lista"; key = PS; addSimbolos(808); ambStack.add(++contAmb); break; // Lista
                case 811: key = -1; ambStack.removeLast(); if(tipoLista != null) delSimbolos(811); addSimbolos(811); break; // Fin lista
                
                case 812: tipo = "struct"; clase = "rango"; key = PS; addSimbolos(808); break; // Rango
                case 813: key = -1; break; // Fin rango
                
                case 814: tipo = "struct"; clase = "diccionario"; key = PS; addSimbolos(808); break; // Diccionario
                case 815: key = -1; break; // Fin diccionario
                
                case 876: tipo = "decimal"; clase = "var"; key = PS; break; // para FOR id 
                case 877: key = -1; break; // Fin FOR id
            }
        }
        
        
        prodStack.removeLast();
    }
    
    public void key(int LT) {
        switch(key) {
            case 805: var(LT);          break;
            case 808: tupla(LT);        break;
            case 810: lista(LT);        break;
            case 812: rango(LT);        break;
            case 814: diccionario(LT);  break;
        }
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
            clase = "datoLista"; 
        } else {
            if(!tipo.equals(this.tipo)) {// Checar si no son iguales para marcar sacarlos
                tipoLista = null;
                this.tipo = tipo;
            }
        }
    }
    
    // RANGO
    public void rango(int LT) {
        
    }
    
    // DICCIONARIO
    public void diccionario(int LT) {
        
    }
    
    // COLUMNAS SIMBOLOS
    String id, tipo, clase, tipoLista, valor, tpArr;
    
    int amb, tArr, dimArr, noPar, llave;
    
    public void addSimbolos(Token token) {
        String sql = "";
        
        switch(key) {
            case 804: // Funcion
                tpArr = (ambStack.peekLast() + 1) + ""; // Define el ambito que creara
                
                if(!clase.equals("fun")) {
                    id = "#"+id;
                    clase = "fun";
                }
                
                sql = "INSERT INTO simbolos (id, tipo, clase, amb, tpArr) VALUES ('" 
                    + id + "', '" + tipo + "', '" + clase + "', " + ambStack.peekLast() + ", '" + tpArr + "');";
                
                tpArr = id; // Guarda tpArr para parametros
                
                key = -1;
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
        System.out.println(sql);
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Ambito.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            
            case 810: // INSERT DATO
                sql = "INSERT INTO simbolos (tipo, clase, amb, noPar, tpArr) VALUES ('" 
                    + tipo + "', '"+ clase + "', "+ ambStack.peekLast() + ", " + tArr + ", '" + tpArr + "');";
            break;
            
            case 811: // UPDATE LAST LISTA: tipoLista, tArr
                sql = update + "tipoLista = '" + tipoLista + "', tArr = '" + tArr + "' WHERE clase = 'lista' "+ last;
                tArr = 0;
            break;
        }
        System.out.println(sql);
        
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Ambito.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void delSimbolos(int PS) {
        String sql = "";
        
        switch(PS) {
            case 811: // DELETE WHERE clase LAST LIMIT tArr
                sql = delete + "clase = '" + clase + "' " + lastLimit + tArr + ";";
            break;
        }
        System.out.println(sql);
        
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Ambito.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
    
    // ERROR
    public void setError(int error) {
        Token token = tokens.peekFirst();
        
        if(key == 804 && !clase.equals("fun")) // Agregamos #funcion
            addSimbolos(tokens.peekLast());
        
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
}
