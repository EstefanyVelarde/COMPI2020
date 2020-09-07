package Control;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Pruebas {
    public static void main(String[] args) throws SQLException {
       Conexion con = new Conexion();
       
       Connection c= con.Conectar();
       
        String existeID = "SELECT id FROM simbolos WHERE id = 'x';";
        String add = "INSERT INTO simbolos (id) values ('x');";
        String delete = "DELETE FROM simbolos;";
        Statement stmt = c.createStatement();
        
        stmt.executeUpdate(delete);
        ResultSet rs = stmt.executeQuery(existeID);
        
        System.out.println("RS " + rs.next());
    }
}
