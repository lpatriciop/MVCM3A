/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mimvc.modelo;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.postgresql.util.Base64;

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
    
//    public List<Persona> listarPersonas(){
//        
//           String sql="select * from persona"; //Campos de la base de datos.
//           ResultSet rs= con.consulta(sql);
//           List<Persona> lista=new ArrayList<Persona>();
//       try {
//           while(rs.next()){
//             Persona p= new Persona();
//             p.setIdPersona(rs.getString("idpersona"));//campos de la BD
//             p.setNombres(rs.getString("nombres"));//campos de la BD
//             p.setApellidos(rs.getString("apellidos"));//campos de la BD
//             lista.add(p);
//           }
//           //IMPORTANTISIMO CERRAR CONEXION.
//           rs.close();
//           return lista;
//       } catch (SQLException ex) {
//           Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
//           return null;
//       }
//            
//            
//    }
    public List<Persona> listarPersonas(String aguja) 
    {
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
             byte[] bf=rs.getBytes("foto");
             if(bf!=null){
               bf=Base64.decode(bf,0,bf.length);
               try{
               p.setFoto(obtenerImagen(bf));
               }catch(IOException ex){
                 p.setFoto(null);
                 Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
               }
             }else{
                 p.setFoto(null);
             }
             
                      
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
        String foto64=null;
        
        BufferedImage img= imgBimage(getFoto());
        ByteArrayOutputStream bos= new ByteArrayOutputStream();
        
        try{
                ImageIO.write(img, "PNG", bos);
                byte[] imgb=bos.toByteArray();
                foto64=Base64.encodeBytes(imgb);
        } catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        String sql;
        sql="INSERT INTO persona (idpersona,nombres,apellidos,foto) ";
        sql+=" VALUES ('"+getIdPersona()+"','"+getNombres()+"','"+getApellidos()+"','"+foto64+"')";
        // INSERT INTO persona (idpersona,nombres,apellidos) VALUES ('010101','JUAN','PEREZ','$@%@#@$%$#@')
        return con.accion(sql);
    }
    
    private BufferedImage imgBimage(Image img){
        //Compruebo que no ya un buferrimage
           if( img instanceof BufferedImage) {
                return (BufferedImage)img;
        }
           BufferedImage bi= new BufferedImage(
                   img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
           Graphics2D bGR=bi.createGraphics();
           bGR.drawImage(img, 0, 0,null);
           bGR.dispose();
           return bi;
    }
    
    private Image obtenerImagen(byte[] bytes) throws IOException{
    
        ByteArrayInputStream bis=new ByteArrayInputStream(bytes);
        Iterator it = ImageIO.getImageReadersByFormatName("png");
        ImageReader reader = (ImageReader)it.next();
        Object source = bis;
        
        ImageInputStream iis=ImageIO.createImageInputStream(source);
        reader.setInput(iis,true);
        
        ImageReadParam param = reader.getDefaultReadParam();
        param.setSourceSubsampling(1, 1, 0, 0);
        
        return reader.read(0,param);
        
    }
}
