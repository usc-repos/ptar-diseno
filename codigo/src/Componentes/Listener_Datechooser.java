package Componentes;

import datechooser.beans.DateChooserCombo;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;

public class Listener_Datechooser {

    private Validaciones validardates = new Validaciones();
    DateFormat dateformt = new SimpleDateFormat("dd/MM/yyyy");

    public void AgregarCambioFecha(final DateChooserCombo datechscombo, final Date min, final Date max, final JLabel jlbl, final String ErrorMsg) {

        datechscombo.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                try {
                    if (!validardates.EsFechaEntre(dateformt.parse(dateformt.format(datechscombo.getSelectedDate().getTime())), min, max)) {
                        jlbl.setText(ErrorMsg);
                    }
                    else{jlbl.setText("");}
                } catch (Exception ex) {
                     //todo: implementar exepcion
                }
            }
        });
    }
}
