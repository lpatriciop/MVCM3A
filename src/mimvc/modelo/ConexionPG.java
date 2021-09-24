/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mimvc.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricio
 */
public class ConexionPG {
//DATOS DE CONEXION A SU BASE LOCAL.
    String cadenaConexion="jdbc:postgresql://localhost:5432/mvc";
    String usuarioBD="postgres";
    String contrasBD="ista";
    
    Connection con;
    public ConexionPG() {
    
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionPG.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
           con= DriverManager.getConnection(cadenaConexion, usuarioBD, contrasBD);
        } catch (SQLException ex) {
            Logger.getLogger(ConexionPG.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public ResultSet consulta(String sqlc){
    
        try {
            Statement st= con.createStatement();
            ResultSet rs= st.executeQuery(sqlc);
            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(ConexionPG.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public boolean accion(String sqla){
        try {
            Statement st= con.createStatement();
            boolean rb=st.execute(sqla);
            st.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ConexionPG.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
    }

    public Connection getCon() {
        return con;
    }

  
    
}
