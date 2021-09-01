/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mimvc.modelo;

import java.awt.Image;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricio
 */
public class ModeloPersona extends Persona{
   private ConexionPG con=new ConexionPG();
    public ModeloPersona() {
    }

    public ModeloPersona(String idPersona, String nombres, String apellidos, Date fechaNacimiento, Image foto) {
        super(idPersona, nombres, apellidos, fechaNacimiento, foto);
    }
    
    public List<Persona> listarPersonas(){
        
           String sql="select * from persona"; //Campos de la base de datos.
           ResultSet rs= con.consulta(sql);
           List<Persona> lista=new ArrayList<Persona>();
       try {
           while(rs.next()){
             Persona p= new Persona();
             p.setIdPersona(rs.getString("idpersona"));//campos de la BD
             p.setNombres(rs.getString("nombres"));//campos de la BD
             p.setApellidos(rs.getString("apellidos"));//campos de la BD
             lista.add(p);
           }
           //IMPORTANTISIMO CERRAR CONEXION.
           rs.close();
           return lista;
       } catch (SQLException ex) {
           Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
           return null;
       }
            
            
    }
    public List<Persona> listarPersonas(String aguja){
           //2000-05-14
           String sql="select * from persona where "; //Campos de la base de datos.
           sql+=" UPPER(idpersona) like UPPER('%"+ aguja + "%') ";
           sql+="OR UPPER(nombres) like UPPER('%"+ aguja + "%') ";
           sql+="OR UPPER(apellidos) like UPPER('%"+ aguja + "%') ";
           ResultSet rs= con.consulta(sql);
           List<Persona> lista=new ArrayList<Persona>();
       try {
           while(rs.next()){
             Persona p= new Persona();
             p.setIdPersona(rs.getString("idpersona"));//campos de la BD
             p.setNombres(rs.getString("nombres"));//campos de la BD
             p.setApellidos(rs.getString("apellidos"));//campos de la BD
             lista.add(p);
           }
           //IMPORTANTISIMO CERRAR CONEXION.
           rs.close();
           return lista;
       } catch (SQLException ex) {
           Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
           return null;
       }
            
            
    }
    public boolean grabar(){
        String sql;
        sql="INSERT INTO persona (idpersona,nombres,apellidos) ";
        sql+=" VALUES ('"+getIdPersona()+"','"+getNombres()+"','"+getApellidos()+"')";
        return con.accion(sql);
    }
}
