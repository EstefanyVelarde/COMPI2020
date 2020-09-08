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
        String id = tokens.peekFirst().getLexema();
        
        try {
            Iterator it = ambStack.iterator();
            
            while(it.hasNext()) {
                int amb = (int) it.next();
                rs = stmt.executeQuery(existeID + id + "' AND amb =" + amb);

                if(rs.next() && !declaracion)
                    return true;
                
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
            case 804: tipo = "real"; clase = "fun"; key = PS;   break;  // Funcion
            case 805: clase = "var"; key = PS;                  break;  // Variable
            case 806: tipo = "none"; clase = "par"; noPar++;  key = PS;   break;  // Parametro
            case 807: tpArr = ambStack.peekLast() + ""; addSimbolos(807); break;  // Fin parametros
            case 808: case 809: case 810:
            case 811: tipo = "struct"; key = PS; break;   // LIST-TUP-RANGOS
        }
        
        prodStack.removeLast();
    }
    
    public void key(int LT) {
        switch(key) {
            case 805: var(LT);          break;
            case 808: tupla(LT);        break;
            case 809: lista(LT);        break;
            case 810: rango(LT);        break;
            case 811: diccionario(LT);  break;
        }
    }
    
    
    
    // VARIABLE
    public void var(int LT) {
        switch(LT) {
            case -54: 
            case -55: tipo = "boolean"; addSimbolos(805); break;
            case -40: tipo = "cad";     addSimbolos(805); break;
            case -41: tipo = "char";    addSimbolos(805); break;
            case -45: tipo = "int";     addSimbolos(805); break;
            case -46: tipo = "float";   addSimbolos(805); break;
            case -47: tipo = "complex"; addSimbolos(805); break;
            case -48: tipo = "bin";     addSimbolos(805); break;
            case -49: tipo = "hexa";    addSimbolos(805); break;
            case -50: tipo = "oct";     addSimbolos(805); break;
            case -56: tipo = "none";    addSimbolos(805); break;
        }
    }
    
    // TUPLA
    public void tupla(int LT) {
        
    }
    
    // LISTA
    public void lista(int LT) {
        
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
                
                sql = "INSERT INTO simbolos (id, tipo, clase, amb, tpArr) VALUES ('" 
                    + token.getLexema() + "', '" + tipo + "', '" + clase + "', " + ambStack.peekLast() + ", '" + tpArr + "');";
                
                tpArr = token.getLexema(); // Guarda tpArr para parametros
                
                key = -1;
            break; 
            
            case 806: // Parametro
                sql = "INSERT INTO simbolos (id, tipo, clase, amb, noPar, tpArr) VALUES ('" 
                    + token.getLexema() + "', '"+ tipo + "', '"+ clase + "', "+ ambStack.peekLast() + ", "+ noPar + ", '"+ tpArr + "');";
                
                key = -1;
            break;
            
            default:
                sql = "INSERT INTO simbolos (id, clase, amb) VALUES ('" 
                    + token.getLexema() + "', '"+ clase + "', "+ ambStack.peekLast() + ");";
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
        }
        
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
    
    String existeID = "SELECT id FROM simbolos WHERE id = '";
    
    String update = "UPDATE simbolos SET ";
    
    String last = "ORDER by idsimbolos desc limit 1";
    
    
    // ERROR
    public void setError(int error) {
        Token token = tokens.peekFirst();
        
        errores.add(new Error(error, token.getLinea(), token.getLexema(), desc[error - 700], "Ámbito"));
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
