package Componentes;

import Componentes.Util;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import org.apache.log4j.Logger;

public class Listener_Popup {

    private static Logger log = Logger.getLogger("AgregarMensajePopup");
    public JPopupMenu jPop;
    public Util util = new Util();

    /**
     * Añade un mensaje tipo popup con hipervínculo a un label de ayuda para JLabel
     *
     * @param jlbl Recibe el label el cual se le asignará el popup.
     * @param msg Mensaje del popup.
     * @param uri url que el popup abrirá.
     */
    public void AgregarMensajePopup(final JLabel jlbl, String msg, final String uri,final int dx,final int dy) {//Todo: las dimensiones hay que darlas debido a un bug de thread propio de jpopups
        jPop = new JPopupMenu(); //Contendrà el label popup 
        jPop.setPreferredSize(new Dimension(dx, dy));
        //final JLabel label = new JLabel(msg); //Antes se usaba label pero no deja copiar texto, en prueba jtxtpane

        final JTextPane label = new JTextPane();
        label.setContentType("text/html"); 
        label.setText(msg);// label.setEditable(false); //label.setBackground(null); // label.setBorder(null); // remove the border
        //label.setCursor(new Cursor(Cursor.HAND_CURSOR)); //Cambiar el puntero mouse de flecha a mano.

        label.addMouseListener(new MouseAdapter() { //Añade eventos de raton en el label del popup ayuda.
            public void mouseClicked(MouseEvent e) {
                try {
                    if (!uri.equals("")) {
                        util.AbrirUri(uri);
                    }
                } catch (Exception ex) {
                    log.error("Error mouseClicked " + ex.getMessage());
                    //todo: implementar exepcion
                }
            }
        });

        jlbl.addMouseListener(new MouseAdapter() { //Añade eventos de raton en el label del popup ayuda.
            public void mouseEntered(MouseEvent e) {
                label.removeAll();
                jPop.add(label);
                jPop.show(jlbl, e.getX(), e.getY());
            }
        });
    }
    
        /**
     * Añade un mensaje tipo popup con hipervínculo a un label de ayuda para JButton 
     *
     * @param jlbl Recibe el label el cual se le asignará el popup.
     * @param msg Mensaje del popup.
     * @param uri url que el popup abrirá.
     */
    public void AgregarMensajePopupBtn(final JButton jlbl, String msg, final String uri,final int dx,final int dy) {//Todo: las dimensiones hay que darlas debido a un bug de thread propio de jpopups
        jPop = new JPopupMenu(); //Contendrà el label popup 
        jPop.setPreferredSize(new Dimension(dx, dy));
        //final JLabel label = new JLabel(msg); //Antes se usaba label pero no deja copiar texto, en prueba jtxtpane

        final JTextPane label = new JTextPane();
        label.setContentType("text/html"); 
        label.setText(msg);// label.setEditable(false); //label.setBackground(null); // label.setBorder(null); // remove the border
        //label.setCursor(new Cursor(Cursor.HAND_CURSOR)); //Cambiar el puntero mouse de flecha a mano.

        label.addMouseListener(new MouseAdapter() { //Añade eventos de raton en el label del popup ayuda.
            public void mouseClicked(MouseEvent e) {
                try {
                    if (!uri.equals("")) {
                        util.AbrirUri(uri);
                    }
                } catch (Exception ex) {
                    log.error("Error mouseClicked " + ex.getMessage());
                    //todo: implementar exepcion
                }
            }
        });

        jlbl.addMouseListener(new MouseAdapter() { //Añade eventos de raton en el label del popup ayuda.
            public void mouseClicked(MouseEvent e) {
                label.removeAll();
                jPop.add(label);
                jPop.show(jlbl, e.getX(), e.getY());
            }
        });
    }
}
