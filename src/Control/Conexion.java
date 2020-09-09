package Control;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
    Connection con = null;
    
    public Connection Conectar() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/a16130288?useSSL=false","root","root");
            System.out.println("++ Conectado a BD");
            
        } catch (Exception e) {
            System.out.println("-- No conectado a BD \n\tERROR: " + e);
        }
        
        return con;
    }
}
