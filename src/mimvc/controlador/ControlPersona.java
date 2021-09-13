/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mimvc.controlador;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.xml.ws.Holder;
import mimvc.modelo.ModeloPersona;
import mimvc.modelo.Persona;
import mimvc.vista.VistaPersona;
import sun.swing.table.DefaultTableCellHeaderRenderer;

/**
 *
 * @author Patricio
 */
public class ControlPersona {
    private ModeloPersona modelo;
    private VistaPersona vista;

    public ControlPersona(ModeloPersona modelo, VistaPersona vista) {
        this.modelo = modelo;
        this.vista = vista;
        //SOLAMENTE INICIALIZAR ELEMENTOS.
        vista.setTitle("CRUD PERSONAS");
        vista.getLblMensajes().setText("Bienvienidos Sistema 1.0");
        vista.setVisible(true);
        cargaLista("");
    }
    
    public void iniciaControl(){
     KeyListener kl= new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
             //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyPressed(KeyEvent e) {
             //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyReleased(KeyEvent e) {
                cargaLista(vista.getTxtBuscar().getText());
            //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
     vista.getBntListar().addActionListener(l->cargaLista(""));
     vista.getBntCrear().addActionListener(l->cargaDialogo(1));
     vista.getBtnEditar().addActionListener(l->cargaDialogo(2));
     vista.getBtnAceptar().addActionListener(l->grabaPersona());
     vista.getBtnExaminar().addActionListener(l->examinarFoto());
     //KeyListener
     vista.getTxtBuscar().addKeyListener(kl);
     
    }
    
//    private void cargaLista(){
//    //Carga datos a la vista.
//        DefaultTableModel tablaMd;
//        tablaMd=(DefaultTableModel)vista.getTblPersonas().getModel();
//        tablaMd.setNumRows(0);
//        List<Persona> lista=modelo.listarPersonas("");
//        lista.stream().forEach(per->{
//         String[] fila={per.getIdPersona(),per.getNombres(),per.getApellidos()};
//         tablaMd.addRow(fila);
//        });
//        
//    }
    private void cargaLista(String aguja){
    //Carga datos a la vista.
    
        vista.getTblPersonas().setDefaultRenderer(Object.class, new ImagenTabla());
        vista.getTblPersonas().setRowHeight(100);
        DefaultTableCellRenderer render = new DefaultTableCellHeaderRenderer();
        DefaultTableModel tablaMd;
        tablaMd=(DefaultTableModel)vista.getTblPersonas().getModel();
        tablaMd.setNumRows(0);
        List<Persona> lista=modelo.listarPersonas(aguja);
        int ncols=tablaMd.getColumnCount();
        Holder<Integer> i = new Holder<>(0);
        lista.stream().forEach(per->{
           tablaMd.addRow(new Object[ncols]);
           vista.getTblPersonas().setValueAt(per.getIdPersona(), i.value, 0);
           vista.getTblPersonas().setValueAt(per.getNombres(), i.value, 1);
           
           
           Image img =per.getFoto();
           
           if(img!=null){
             Image nimg=img.getScaledInstance(100, 100,  Image.SCALE_SMOOTH);
             ImageIcon icon = new ImageIcon(nimg);
             render.setIcon(icon);
             vista.getTblPersonas().setValueAt(new JLabel(icon), i.value, 3);
           }else{
            vista.getTblPersonas().setValueAt(null, i.value, 3);
           }
           
           i.value++; 
//         String[] fila={per.getIdPersona(),per.getNombres(),per.getApellidos()};
//         tablaMd.addRow(fila);
        });
        
    }
    private void cargaDialogo(int origen){
        
      if(origen==1){
          vista.getDlgPersona().setTitle("CREAR PERSONA");
      }else{
          vista.getDlgPersona().setTitle("EDITAR PERSONA");
      }
      vista.getDlgPersona().setSize(500,300);
      vista.getDlgPersona().setLocationRelativeTo(vista);
      vista.getDlgPersona().setVisible(true);
    }
    private void grabaPersona(){
        String idpersona=vista.getTxtID().getText();
        String nombres=vista.getTxtNombres().getText();
        String apellidos=vista.getTxtApellidos().getText();
        
//        ModeloPersona persona=new ModeloPersona();
        modelo.setIdPersona(idpersona);
        modelo.setNombres(nombres);
        modelo.setApellidos(apellidos);
        
        if(modelo.grabar()){
            JOptionPane.showMessageDialog(vista, "Persona Creada Satisfactoriamente");
            cargaLista("");
        }else{
            JOptionPane.showMessageDialog(vista, "ERROR");
        }
        
    }
    private void examinarFoto(){
        JFileChooser jfc=new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if(jfc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
            
            try {
                Image imagen=ImageIO.read(jfc.getSelectedFile()).getScaledInstance(
                        vista.getLblFoto().getWidth(),
                        vista.getLblFoto().getHeight(),
                        Image.SCALE_DEFAULT);
                Icon icon =new ImageIcon(imagen);
                vista.getLblFoto().setIcon(icon);
                vista.getLblFoto().updateUI();
            } catch (IOException ex) {
                Logger.getLogger(ControlPersona.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
