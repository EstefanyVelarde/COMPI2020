package Control;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContAmbito {
    Connection con;
    
    Statement stmt;
    
    ResultSet rs;
    
    Integer count;
    
    int[][] contador;
    
    int[] total;
    
    int dec, bin, oct, hex, flot, cad, car, comp, bool, none, arr, tup, list, ran, dic, struct;
    
    int contRow;
    
    public ContAmbito(Connection con, Statement stmt) {
        this.con = con;
        this.stmt = stmt;
    }
    
    public void setContador(int contAmb) {
        this.contador = new int[contAmb + 1][18];
        this.total = new int[18];
        
        for (int i = 0; i < contAmb + 1; i++) {
            contador[i][0] = i; // Ambito
            
            for (int j = 1; j < 17; j++) {
                contador[i][j] = getValue(i, j);
                
                total[j] += contador[i][j];
                
                contRow += contador[i][j];
            }
            
            contador[i][17] = contRow;
            total[17] += contRow;
            contRow = 0;
        }
    }
    
    String select = "SELECT COUNT(*) AS 'count' FROM simbolos WHERE amb = ";
    String type[] = {
        " AND (tipo = 'decimal' AND (clase = 'var' OR clase = 'par'));",
        " AND (tipo = 'binario' AND (clase = 'var' OR clase = 'par'));",
        " AND (tipo = 'octal' AND (clase = 'var' OR clase = 'par'));",
        " AND (tipo = 'hexadecimal' AND (clase = 'var' OR clase = 'par'));",
        " AND (tipo = 'flotante' AND (clase = 'var' OR clase = 'par'));",
        " AND (tipo = 'cadena' AND (clase = 'var' OR clase = 'par'))",
        " AND (tipo = 'caracter' AND (clase = 'var' OR clase = 'par'));",
        " AND (tipo = 'complejo' AND (clase = 'var' OR clase = 'par'));",
        " AND (tipo = 'boolean' AND (clase = 'var' OR clase = 'par'));",
        " AND (tipo = 'none' AND (clase = 'var' OR clase = 'par' OR clase = 'fun') AND error is null);",
        " AND clase = 'arreglo';",
        " AND clase = 'tupla';",
        " AND clase = 'lista';",
        " AND clase = 'rango';",
        " AND clase = 'diccionario';",
        " AND (clase = 'datoLista' OR clase = 'datoTupla' OR clase = 'datoDic');",
    };
    
    public int getValue(int amb, int column) {
        String sql = select + amb + type[column - 1];
        
        try {
            rs = stmt.executeQuery(sql);
            rs.next();
            count = rs.getInt("count");
            rs.close();
            return (count == null) ? 0 : count;
        } catch (SQLException ex) {
            Logger.getLogger(Ambito.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
    }
    
    String simbolos = "SELECT idsimbolos, "
            + "ifnull(concat('#', error, ' ', id), id) as id, "
            + "tipo, clase, amb, tArr, tipoLista, dimArr, valor, "
            + "noPar, llave, tpArr FROM simbolos";
    
    public ResultSet getSimbolos() throws SQLException {
        return stmt.executeQuery(simbolos);
    }
    
    public int[][] getContador() {
        return contador;
    }
    
    public int[] getTotal() {
        
        return total;
    }
}
