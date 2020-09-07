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
    
    int contAmb, flag;
    
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
        
        ambStack = new LinkedList();
        
        contAmb = -1;
        
        this.crear();
    }
        
    public void checar(int PS, int LT) {
        if(LT == -44) { // Si es ID
            if(declaracion) {
                if(existeID()) {
                    setError(700);
                } else {
                    addSimbolos(tokens.peekFirst(), LT);
                }
            } else { // Esta en ejecucion
                if(!existeID()) {
                    setError(701);
                }
            }
        }
    }
    
    public void cambiarZona(int PS, int LT) {
        switch(PS) {
            case 800: declaracion = false; break;
            case 801: declaracion = true;  break;
            case 802: crear();  break;
            case 803: cerrar(); break;
            case 804: addSimbolos(PS); break;
        }
        
        prodStack.removeLast();
    }
    
    public void crear() {
        ambStack.add(++contAmb);
        
        System.out.println("++ AMBITO \n\t" + ambStack.toString());
    }
    
    public void cerrar() {
        ambStack.removeLast();
        System.out.println("-- AMBITO \n\t" + ambStack.toString());
    }
    
    // ERROR
    public void setError(int error) {
        Token token = tokens.peekFirst();
        
        errores.add(new Error(error, token.getLinea(), token.getLexema(), desc[error - 700], "Ámbito"));
    }
    
    String desc[] = { 
        "Variable duplicada",
        "Variable no declarada"
    };
  
    // BASE DE DATOS
    public boolean existeID() {
        String id = tokens.peekFirst().getLexema();
        
        try {
            Iterator it = ambStack.iterator();
            
            while(it.hasNext()) {
                rs = stmt.executeQuery(existeID + id + "' AND ambito =" + ambStack.peekLast());

                if(rs.next() && !declaracion)
                    return true;
                
                it.next();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Ambito.class.getName()).log(Level.SEVERE, null, ex);
        }
         
            
        return false;
    }
    
    public void addSimbolos(Token token, int LT) {
        switch(LT) {
            case -44: // GUARDA ID
                try {
                    String sql = "INSERT INTO simbolos (id, ambito) values ('"
                            + token.getLexema() + "', "+ ambStack.peekLast() + ");";
                    System.out.println(sql);
                    stmt.executeUpdate(sql);
                } catch (SQLException ex) {
                    Logger.getLogger(Ambito.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            break;
        }
    }
    
    public void addSimbolos(int PS) {
        switch(PS) {
            case 804: // ES FUNCION
                
                try {
                    stmt.executeUpdate(update + "fun" + last);
                } catch (SQLException ex) {
                    Logger.getLogger(Ambito.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            break;
        }
    }
    
    Connection con;
    
    Statement stmt;
    
    ResultSet rs;
    
    String resetTable = "DELETE FROM simbolos;";
    
    String resetAI = "ALTER TABLE simbolos AUTO_INCREMENT = 1;";
    
    String existeID = "SELECT id FROM simbolos WHERE id = '";
    
    String addSimbolos = "INSERT INTO simbolos (id) values ('x');";
    
    String update = "UPDATE simbolos SET clase = '";
    
    String last = "' ORDER by idsimbolos desc limit 1";
    
    // PILAS
    public void initAmbito(LinkedList<Integer> prodStack, LinkedList<Token> tokens, 
            LinkedList<Error> errores) {
        
        this.prodStack = prodStack;
        
        this.tokens = tokens;
        
        this.errores = errores;
    }
}
