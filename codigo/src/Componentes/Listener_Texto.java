package Componentes;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Listener_Texto {

    private Validaciones validar = new Validaciones();

    public void AgregarAlertaTextoDoble(final JTextField jtxtf, final double min, final double max, final JLabel jlbl, final String ErrorMsg) {

        jtxtf.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                cambio();
            }

            public void removeUpdate(DocumentEvent e) {
                cambio();
            }

            public void insertUpdate(DocumentEvent e) {
                cambio();                
            }

            public void cambio() {
                
                if (validar.EsDobleEntre(jtxtf.getText(), min, max)) {
                    jlbl.setText("");                
                } else {
                    jlbl.setText(ErrorMsg);
                }
            }
        });
    }    
    
        public void AgregarAlertaTextoEntero(final JTextField jtxtf, final int min, final int max, final JLabel jlbl, final String ErrorMsg) {

        jtxtf.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                cambio();
            }

            public void removeUpdate(DocumentEvent e) {
                cambio();
            }

            public void insertUpdate(DocumentEvent e) {
                cambio();                
            }

            public void cambio() {
                
                if (validar.EsEnteroEntre(jtxtf.getText(), min, max)) {
                    jlbl.setText("");                
                } else {
                    jlbl.setText(ErrorMsg);
                }
            }
        });
    }
}
