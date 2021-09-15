/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mimvc.controlador;

import mimvc.modelo.ModeloPersona;
import mimvc.vista.VistaPersona;
import mimvc.vista.VistaPrincipal;

/**
 *
 * @author Patricio
 */
public class ControlPrincipal {
    private VistaPrincipal vista;

    public ControlPrincipal(VistaPrincipal vista) {
        this.vista = vista;
        vista.setVisible(true);
    }
    
    public void iniciaControl(){
        vista.getMnuManPersonas().addActionListener(l->mantPersonas());
        vista.getTlbManPersonas().addActionListener(l->mantPersonas());
    
    }
    
    public void mantPersonas(){
    
        ModeloPersona m=new ModeloPersona();
        VistaPersona v = new VistaPersona();
        vista.getDktPrincipal().add(v);
        ControlPersona c=new ControlPersona(m, v);
        c.iniciaControl();
    }
    
    
}
