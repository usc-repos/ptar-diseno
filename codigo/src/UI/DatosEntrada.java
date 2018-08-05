package UI;

import BO.Bod;
import DB.Dao;
import java.io.File;
import Componentes.Util;
import org.apache.log4j.Logger;
import Componentes.Configuracion;
import Componentes.Listener_Popup;
import Componentes.Pregunta;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class DatosEntrada extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("DatosEntrada"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    public Map<String, Pregunta> map = new TreeMap<>();
    PopupMenuListener listener;
    private Bod bod = new Bod("");
    private Listener_Popup Lpopup;
    Util util = new Util();
    private boolean eSave = true; //solo para advertir perdida de datos al cerrar la ventana
    private boolean esGuia = false; //para saber si hubo un guardado, se usa al cerrar esta ventana...
    Pregunta pg;

    public DatosEntrada(Bod bod) {
        this.bod = bod;
        initComponents();
        InicializarComponentes();
    }

    /**
     * Inicializa todos los elementos (jlabels,jtextbox,etc.) con sus
     * respectivos valores y textos desde al base de datos.
     */
    public void InicializarComponentes() {

        ResultSet rs;
        bod.WinDatosDeEntrada = true;//Bandera, la ventana se ha abierto
        final int year = Calendar.getInstance().get(Calendar.YEAR);
        btn_guardar.setText("Guardar");
        btn_close.setText("Cerrar");

        try {//Todo: implementar posición de posible error
            //-------------------------------------------------------------- Titulo de la sección     
            rs = db.ResultadoSelect("obtenerseccion", "1", null);

            lbl_titulo1.setText(rs.getString("Nombre")); //Título de este jpane
            lbl_titulo2.setText(rs.getString("Mensaje"));
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
            //Inicialemnte coloca el item seleccionado en el año actual, mas abajo se coloca otra si existe

            listener = new PopupMenuListener() {
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

                    JComboBox box = (JComboBox) e.getSource();
                    if (box.getSelectedItem().toString().equals("") || box.getSelectedItem().toString().equals(year + "")) {
                        box.setSelectedItem(year + "");
                    }
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent e) {
                }
            };

            // - - - - - - Pregunta 01 Cuestionario 01 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - TFA 
            rs = db.ResultadoSelect("datospregunta", "TFA");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("TFA", pg);

            lbl_TFA_titulo1.setText(pg.tit1);
            ddm_TFA.addItem(""); //Añade un campo vacio a la lista desplegable fecha

            if (pg.rmax <= year) {//por si se usa años despues
                pg.rmax += 30;
            }

            for (int annios = (int) pg.rmin; annios <= pg.rmax; annios++) {//Añade años a la lista desplegable fecha
                ddm_TFA.addItem(annios + "");
            }
            ddm_TFA.addPopupMenuListener(listener); //otros listeners en este y demas dropdownlist, son agregados abajo como eventos de ItemStateChanged
            AsignarPopupBtn(btn_TFA_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 390, 100);
            // - - - - - - Pregunta 02 Cuestionario 01 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - TML 
            rs = db.ResultadoSelect("datospregunta", "TML");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("TML", pg);

            lbl_TML_titulo1.setText(pg.tit1);
            lbl_TML_titulo2.setText(rs.getString("titulo2"));

            txt_TML_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("TML");
                }
            });

            AsignarPopupBtn(btn_TML_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 300, 110);
            // - - - - - - Pregunta 03 Cuestionario 01 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - TCA 
            rs = db.ResultadoSelect("datospregunta", "TCA");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("TCA", pg);

            lbl_TCA_titulo1.setText(pg.tit1);
            lbl_TCA_titulo2.setText(rs.getString("titulo2"));
            txt_TCA_Pregunta.setText(rs.getString("valorpordefecto"));
            lbl_TCA_desc.setText(pg.desc);

            txt_TCA_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("TCA");
                }
            });

            AsignarPopupBtn(btn_TCA_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 420, 110);
            // - - - - - - Pregunta 04 Cuestionario 01 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - TCR 
            rs = db.ResultadoSelect("datospregunta", "TCR");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("TCR", pg);

            lbl_TCR_titulo1.setText(pg.tit1);
            lbl_TCR_titulo2.setText(rs.getString("titulo2"));

            txt_TCR_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("TCR");
                }
            });
            // - - - - - - Pregunta 05 Cuestionario 01 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - TCI 
            rs = db.ResultadoSelect("datospregunta", "TCI");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("TCI", pg);

            lbl_TCI_titulo1.setText(pg.tit1);
            ddm_TCI.addItem("");

            if (pg.rmax <= year) {
                pg.rmax = pg.rmax + 30;
            }

            for (int annios = (int) pg.rmin; annios <= pg.rmax; annios++) {//Añade años a la lista desplegable fecha
                ddm_TCI.addItem(annios + "");
            }

            ddm_TCI.addPopupMenuListener(listener); //otros listeners en este y demas dropdownlist, son agregados abajo como eventos de ItemStateChanged
            // - - - - - - Pregunta 06 Cuestionario 01 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - TUC 
            rs = db.ResultadoSelect("datospregunta", "TUC");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("TUC", pg);

            lbl_TUC_titulo1.setText(pg.tit1);
            ddm_TUC.addItem("");

            if (pg.rmax <= year) {
                pg.rmax = pg.rmax + 30;
            }

            for (int annios = (int) pg.rmin; annios <= pg.rmax; annios++) {//Añade años a la lista desplegable fecha
                ddm_TUC.addItem(annios + "");
            }

            ddm_TUC.addPopupMenuListener(listener);
            // - - - - - - Pregunta 07 Cuestionario 01 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - TDI 
            rs = db.ResultadoSelect("datospregunta", "TDI");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("TDI", pg);

            lbl_TDI_titulo1.setText(pg.tit1);
            lbl_TDI_titulo2.setText(rs.getString("titulo2"));
            txt_TDI_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_TDI_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("TDI");
                }
            });

            AsignarPopupBtn(btn_TDI_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 470, 250);
            // - - - - - - Pregunta 08 Cuestionario 01 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - TRE 
            rs = db.ResultadoSelect("datospregunta", "TRE");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("TRE", pg);

            lbl_TRE_titulo1.setText(pg.tit1);
            lbl_TRE_titulo2.setText(rs.getString("titulo2"));
            txt_TRE_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_TRE_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("TRE");
                }
            });

            AsignarPopupBtn(btn_TRE_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 460, 200);
            // - - - - - - Pregunta 09 Cuestionario 01 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - TDP 
            rs = db.ResultadoSelect("datospregunta", "TDP");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("TDP", pg);

            lbl_TDP_titulo1.setText(pg.tit1);
            lbl_TDP_titulo2.setText(rs.getString("titulo2"));
            txt_TDP_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_TDP_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("TDP");
                }
            });

            AsignarPopupBtn(btn_TDP_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 450, 350);

            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - Si ya han sido llenado correctamente estos datos se obtienen de Bod
            if (bod.EditDatosDeEntrada) {
                ddm_TFA.setSelectedItem(bod.getVarInt("TFA") + "");
                txt_TML_Pregunta.setText(bod.getVarFormateadaPorNombre("TML", map.get("TML").deci));
                txt_TCA_Pregunta.setText(bod.getVarFormateadaPorNombre("TCA", map.get("TCA").deci));
                txt_TCR_Pregunta.setText(bod.getVarFormateadaPorNombre("TCR", map.get("TCR").deci));
                ddm_TCI.setSelectedItem(bod.getVarInt("TCI") + "");
                ddm_TUC.setSelectedItem(bod.getVarInt("TUC") + "");
                txt_TDI_Pregunta.setText(bod.getVarFormateadaPorNombre("TDI", map.get("TDI").deci));
                txt_TRE_Pregunta.setText(bod.getVarFormateadaPorNombre("TRE", map.get("TRE").deci));
                txt_TDP_Pregunta.setText(bod.getVarFormateadaPorNombre("TDP", map.get("TDP").deci));
            } else {
                borradoMsgError();
            }
            
            eSave = true;

        } catch (Exception ex) {
            log.error("Error en InicializarComponentes " + ex.getMessage());
            ErrorGeneral(); // Ha ocurrido algun error en los datos o conexión, se debe revisar log.
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }

    private void borradoMsgError () {
        lbl_TCA_error.setText("");
        lbl_TCI_error.setText("");
        lbl_TCR_error.setText("");
        lbl_TDI_error.setText("");
        lbl_TDP_error.setText("");
        lbl_TFA_error.setText("");
        lbl_TML_error.setText("");
        lbl_TRE_error.setText("");
        lbl_TUC_error.setText("");
    }

    /**
     * Deshabilita todos los componentes y muestra una advertencia al usuario
     */
    private void ErrorGeneral() {
        JOptionPane.showMessageDialog(null, "Ha ocurrido un error, revise el log de errores \n cierre la ventana", "Error al cargar componentes", JOptionPane.ERROR_MESSAGE);//Todo: 'intl'

        Component[] comp = jp_Componentes.getComponents();//Deshabilitar todos los Componentes del Jpane
        for (Component component : comp) {
            component.setEnabled(false);
        }
        btn_close.setEnabled(true);
    }

    /**
     * Comprueba todos los campos de esta ventana versus la clase de lógica de
     * datos. Hasta que esten correctos todos permite guardar.
     *
     * @return
     */
    private boolean Guardar() {

        lbl_save_error.setText("");
        lbl_save_desc.setText("");

        if (!CalcularTFA()) {
            lbl_save_error.setText(map.get("TFA").erro);
            lbl_save_desc.setText(map.get("TFA").desc);
            lbl_save_titulo1.setText(map.get("TFA").tit1);
            return false;
        }

        if (!CalcularTML()) {
            lbl_save_error.setText(map.get("TML").erro);
            lbl_save_desc.setText(map.get("TML").desc);
            lbl_save_titulo1.setText(map.get("TML").tit1);
            return false;
        }

        if (!CalcularTCA()) {
            lbl_save_error.setText(map.get("TCA").erro);
            lbl_save_desc.setText(map.get("TCA").desc);
            lbl_save_titulo1.setText(map.get("TCA").tit1);
            return false;
        }

        if (!CalcularTCR()) {
            lbl_save_error.setText(map.get("TCR").erro);
            lbl_save_desc.setText(map.get("TCR").desc);
            lbl_save_titulo1.setText(map.get("TCR").tit1);
            return false;
        }

        if (!CalcularTCI()) {
            lbl_save_error.setText(map.get("TCI").erro);
            lbl_save_desc.setText(map.get("TCI").desc);
            lbl_save_titulo1.setText(map.get("TCI").tit1);
            return false;
        }

        if (!CalcularTUC()) {
            lbl_save_error.setText(map.get("TUC").erro);
            lbl_save_desc.setText(map.get("TUC").desc);
            lbl_save_titulo1.setText(map.get("TUC").tit1);
            return false;
        }

        if (!CalcularTDI()) {
            lbl_save_error.setText(map.get("TDI").erro);
            lbl_save_desc.setText(map.get("TDI").desc);
            lbl_save_titulo1.setText(map.get("TDI").tit1);
            return false;
        }

        if (!CalcularTRE()) {
            lbl_save_error.setText(map.get("TRE").erro);
            lbl_save_desc.setText(map.get("TRE").desc);
            lbl_save_titulo1.setText(map.get("TRE").tit1);
            return false;
        }

        if (!CalcularTDP()) {
            lbl_save_error.setText(map.get("TDP").erro);
            lbl_save_desc.setText(map.get("TDP").desc);
            lbl_save_titulo1.setText(map.get("TDP").tit1);
            return false;
        }

        bod.EditDatosDeEntrada = true;//Se cambia para tener la posibilidad de ser guardado en true
        Main main = (Main) this.getTopLevelAncestor(); // Trata de ir  al contenedor principal 
        main.cancela = false;
        main.vbod = this.bod;

        if (!main.ComprobarCambio(2, true)) {//Comprobar cambios que afecten datos posteriores, si este guardar es de una edición.
            if (!main.cancela) {
                bod.EditDatosDeEntrada = false;
            }
            main.cancela = false;
            main.vbod = null;
            return false;
        }
        lbl_save_error.setText("");
        lbl_save_desc.setText("");
        lbl_save_titulo1.setText("");

        main.vbod = null;
        main.bod = this.bod;
        main.guiaUsuario();
        eSave = true;
        esGuia = true;
        return true;
    }

    /**
     * Permite asignar y crear un mensaje de ayuda (popup) mediante la clase
     * Listener_Popup --> MensajePopup
     *
     * @param btn botón a quien se asigna
     * @param mensaje del popup
     * @param uri dirección url al hacer clic en el popup
     * @param dx ancho
     * @param dy alto
     */
    private void AsignarPopupBtn(JButton btn, String mensaje, String uri, int dx, int dy) {
        Lpopup = new Listener_Popup();
        Lpopup.AgregarMensajePopupBtn(btn, mensaje, uri, dx, dy);
    }

    private boolean CalcularTFA() {

        try {
            lbl_TFA_error.setText("");

            if (!comprobarFchTucTci()) {
                return false;
            }

            if (bod.setVarInt(ddm_TFA.getSelectedItem().toString(), "TFA", (int) map.get("TFA").rmin, (int) map.get("TFA").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularTFA " + ex.getMessage());
        }
        lbl_TFA_error.setText(map.get("TFA").erro);
        return false;
    }

    private boolean CalcularTML() {

        try {
            lbl_TML_error.setText("");

            if (bod.setVarDob(txt_TML_Pregunta.getText(), "TML", map.get("TML").rmin, map.get("TML").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularTML " + ex.getMessage());
        }
        lbl_TML_error.setText(map.get("TML").erro);
        return false;
    }

    private boolean CalcularTCA() {

        try {
            lbl_TCA_error.setText("");

            if (bod.setVarInt(txt_TCA_Pregunta.getText(), "TCA", (int) map.get("TCA").rmin, (int) map.get("TCA").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularTCA " + ex.getMessage());
        }
        lbl_TCA_error.setText(map.get("TCA").erro);
        return false;
    }

    private boolean CalcularTCR() {

        try {
            lbl_TCR_error.setText("");

            if (bod.setVarInt(txt_TCR_Pregunta.getText(), "TCR", (int) map.get("TCR").rmin, (int) map.get("TCR").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularTCR " + ex.getMessage());
        }
        lbl_TCR_error.setText(map.get("TCR").erro);
        return false;
    }

    private boolean CalcularTCI() {

        try {
            lbl_TCI_error.setText("");

            if (!comprobarFchTucTci()) {
                return false;
            }

            if (bod.setVarInt(ddm_TCI.getSelectedItem().toString(), "TCI", (int) map.get("TCI").rmin, (int) map.get("TCI").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularTCI " + ex.getMessage());
        }
        lbl_TCI_error.setText(map.get("TCI").erro);
        return false;
    }

    private boolean CalcularTUC() {

        try {
            lbl_TUC_error.setText("");

            if (!comprobarFchTucTci()) {
                return false;
            }

            if (bod.setVarInt(ddm_TUC.getSelectedItem().toString(), "TUC", (int) map.get("TUC").rmin, (int) map.get("TUC").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularTUC " + ex.getMessage());
        }
        lbl_TUC_error.setText(map.get("TUC").erro);
        return false;
    }

    private boolean CalcularTDI() {

        try {
            lbl_TDI_error.setText("");

            if (bod.setVarInt(txt_TDI_Pregunta.getText(), "TDI", (int) map.get("TDI").rmin, (int) map.get("TDI").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularTDI " + ex.getMessage());
        }
        lbl_TDI_error.setText(map.get("TDI").erro);
        return false;
    }

    private boolean CalcularTRE() {

        try {
            lbl_TRE_error.setText("");

            if (bod.setVarDob(txt_TRE_Pregunta.getText(), "TRE", map.get("TRE").rmin, map.get("TRE").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularTRE " + ex.getMessage());
        }
        lbl_TRE_error.setText(map.get("TRE").erro);
        return false;
    }

    private boolean CalcularTDP() {

        try {
            lbl_TDP_error.setText("");

            if (bod.setVarInt(txt_TDP_Pregunta.getText(), "TDP", (int) map.get("TDP").rmin, (int) map.get("TDP").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularTDP " + ex.getMessage());
        }
        lbl_TDP_error.setText(map.get("TDP").erro);
        return false;
    }

    /**
     * *
     * Ejecuta ciertas funciones que a su vez desencadenan otras funciones
     *
     * @param func : Nombre de la variable de la función
     */
    private void ejecutarFunciones(String func) {
        eSave = false;
        switch (func) {

            case "TFA":
                CalcularTFA();
                break;

            case "TML":
                CalcularTML();
                break;

            case "TCA":
                CalcularTCA();
                break;

            case "TCR":
                CalcularTCR();
                break;

            case "TCI":
                CalcularTCI();
                break;

            case "TUC":
                CalcularTUC();
                break;

            case "TDI":
                CalcularTDI();
                break;

            case "TRE":
                CalcularTRE();
                break;

            case "TDP":
                CalcularTDP();
                break;
        }
    }

    private boolean comprobarFchTucTci() {

        try {
            lbl_TFA_error.setText("");
            lbl_TCI_error.setText("");
            lbl_TUC_error.setText("");

            int itci = 0, ituc = 0, ifch = 0;
            String fch = ddm_TFA.getSelectedItem().toString();
            String tci = ddm_TCI.getSelectedItem().toString();
            String tuc = ddm_TUC.getSelectedItem().toString();

            if (!tci.trim().equals("")) {
                itci = Integer.parseInt(tci);
            }
            if (!tuc.trim().equals("")) {
                ituc = Integer.parseInt(tuc);
            }
            if (!fch.trim().equals("")) {
                ifch = Integer.parseInt(fch);
            }

            if (ifch > 0 && ituc > 0 && (ituc > ifch)) {  // TUC menor o igual al año actual
                lbl_TUC_error.setText(map.get("TUC").erro);
                return false;
            }

            if (ifch > 0 && itci > 0 && (itci >= ifch)) {
                lbl_TCI_error.setText(map.get("TCI").erro);
                return false;
            }

            if (ituc > 0 && itci > 0 && (itci >= ituc)) { //Tuc debe ser mayor que Tci
                lbl_TCI_error.setText(map.get("TCI").erro);
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    private void cerrar() {
        if (!eSave) {
            int n = util.Mensaje("¿Desea cerrar? \n Se perderan los cambios realizados?", "yesno");
            if (n == JOptionPane.NO_OPTION) {
                return;
            }
        }
        bod.WinDatosDeEntrada = false;
        Main main = (Main) this.getTopLevelAncestor();
        main.bod.WinDatosDeEntrada = false;
        if(esGuia) main.guiaUsuario2();
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jsp_DatosEntrada = new javax.swing.JScrollPane();
        jp_Componentes = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        lbl_titulo1 = new javax.swing.JLabel();
        lbl_TFA_titulo1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        lbl_titulo2 = new javax.swing.JLabel();
        lbl_TFA_error = new javax.swing.JLabel();
        btn_guardar = new javax.swing.JButton();
        lbl_TML_titulo1 = new javax.swing.JLabel();
        lbl_TML_titulo2 = new javax.swing.JLabel();
        lbl_TML_error = new javax.swing.JLabel();
        lbl_TCA_desc = new javax.swing.JLabel();
        lbl_TCA_titulo1 = new javax.swing.JLabel();
        txt_TCA_Pregunta = new javax.swing.JTextField();
        lbl_TCA_titulo2 = new javax.swing.JLabel();
        lbl_TCR_titulo1 = new javax.swing.JLabel();
        txt_TCR_Pregunta = new javax.swing.JTextField();
        lbl_TCR_titulo2 = new javax.swing.JLabel();
        lbl_TCA_error = new javax.swing.JLabel();
        lbl_TCR_error = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        lbl_TCI_titulo1 = new javax.swing.JLabel();
        lbl_TUC_titulo1 = new javax.swing.JLabel();
        lbl_TCI_error = new javax.swing.JLabel();
        lbl_TUC_error = new javax.swing.JLabel();
        txt_TDI_Pregunta = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        lbl_TDI_titulo1 = new javax.swing.JLabel();
        lbl_TDI_titulo2 = new javax.swing.JLabel();
        lbl_TRE_error = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        lbl_TRE_titulo1 = new javax.swing.JLabel();
        txt_TRE_Pregunta = new javax.swing.JTextField();
        lbl_TRE_titulo2 = new javax.swing.JLabel();
        lbl_TDI_error = new javax.swing.JLabel();
        lbl_TDP_titulo1 = new javax.swing.JLabel();
        txt_TDP_Pregunta = new javax.swing.JTextField();
        lbl_TDP_titulo2 = new javax.swing.JLabel();
        lbl_TDP_error = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        btn_close = new javax.swing.JButton();
        lbl_save_error = new javax.swing.JLabel();
        lbl_save_desc = new javax.swing.JLabel();
        ddm_TFA = new javax.swing.JComboBox();
        ddm_TCI = new javax.swing.JComboBox();
        ddm_TUC = new javax.swing.JComboBox();
        btn_TDI_ayuda = new javax.swing.JButton();
        btn_TFA_ayuda = new javax.swing.JButton();
        btn_TML_ayuda = new javax.swing.JButton();
        btn_TCA_ayuda = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JSeparator();
        btn_TRE_ayuda = new javax.swing.JButton();
        btn_TDP_ayuda = new javax.swing.JButton();
        lbl_save_titulo1 = new javax.swing.JLabel();
        txt_TML_Pregunta = new javax.swing.JTextField();
        btn_close2 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1024, 768));

        jsp_DatosEntrada.setPreferredSize(new java.awt.Dimension(1024, 768));

        jp_Componentes.setPreferredSize(new java.awt.Dimension(1024, 768));
        jp_Componentes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jp_Componentes.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 38, 1024, -1));

        lbl_titulo1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_titulo1.setForeground(new java.awt.Color(51, 51, 51));
        lbl_titulo1.setText("Titulo");
        jp_Componentes.add(lbl_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 300, 30));

        lbl_TFA_titulo1.setText("Titulo");
        lbl_TFA_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_TFA_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 300, 30));
        jp_Componentes.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 92, 1024, -1));
        jp_Componentes.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 152, 1024, -1));

        lbl_titulo2.setForeground(new java.awt.Color(0, 51, 102));
        lbl_titulo2.setText("Desc");
        jp_Componentes.add(lbl_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 4, 600, 30));

        lbl_TFA_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_TFA_error.setText(" . . .");
        jp_Componentes.add(lbl_TFA_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 50, 340, 35));

        btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Button_Salva_Guardar_21.png"))); // NOI18N
        btn_guardar.setText("save");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 590, 120, -1));

        lbl_TML_titulo1.setText("Titulo");
        lbl_TML_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_TML_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 300, 30));

        lbl_TML_titulo2.setText("Titulo");
        lbl_TML_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_TML_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 110, 80, 25));

        lbl_TML_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_TML_error.setText(" . . .");
        jp_Componentes.add(lbl_TML_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 100, 340, 35));

        lbl_TCA_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_TCA_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_TCA_desc.setText("Desc");
        lbl_TCA_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_TCA_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 610, 30));

        lbl_TCA_titulo1.setText("Titulo");
        lbl_TCA_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_TCA_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 220, 30));

        txt_TCA_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_TCA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 210, 130, 25));

        lbl_TCA_titulo2.setText("Titulo");
        lbl_TCA_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_TCA_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 210, 60, 25));

        lbl_TCR_titulo1.setText("Titulo");
        lbl_TCR_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_TCR_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 220, 30));

        txt_TCR_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_TCR_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 300, 130, 25));

        lbl_TCR_titulo2.setText("Titulo");
        lbl_TCR_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_TCR_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 300, 60, 25));

        lbl_TCA_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_TCA_error.setText(" . . .");
        jp_Componentes.add(lbl_TCA_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 250, 340, 35));

        lbl_TCR_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_TCR_error.setText(" . . .");
        jp_Componentes.add(lbl_TCR_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, 340, 35));
        jp_Componentes.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, 1024, 2));

        lbl_TCI_titulo1.setText("Titulo");
        lbl_TCI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_TCI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 210, 220, 30));

        lbl_TUC_titulo1.setText("Titulo");
        lbl_TUC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_TUC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 300, 220, 30));

        lbl_TCI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_TCI_error.setText(" . . .");
        jp_Componentes.add(lbl_TCI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 250, 340, 35));

        lbl_TUC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_TUC_error.setText(" . . .");
        jp_Componentes.add(lbl_TUC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 340, 340, 35));

        txt_TDI_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_TDI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 400, 130, 25));
        jp_Componentes.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 440, 1024, -1));

        lbl_TDI_titulo1.setText("Titulo");
        lbl_TDI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_TDI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 300, 30));

        lbl_TDI_titulo2.setText("Titulo");
        lbl_TDI_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_TDI_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 400, 80, 25));

        lbl_TRE_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_TRE_error.setText(" . . .");
        jp_Componentes.add(lbl_TRE_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 460, 340, 35));
        jp_Componentes.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 500, 1024, -1));

        lbl_TRE_titulo1.setText("Titulo");
        lbl_TRE_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_TRE_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 300, 30));

        txt_TRE_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_TRE_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 460, 130, 25));

        lbl_TRE_titulo2.setText("Titulo");
        lbl_TRE_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_TRE_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 460, 80, 25));

        lbl_TDI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_TDI_error.setText(" . . .");
        jp_Componentes.add(lbl_TDI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 400, 340, 35));

        lbl_TDP_titulo1.setText("Titulo");
        lbl_TDP_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_TDP_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, 300, 30));

        txt_TDP_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_TDP_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 520, 130, 25));

        lbl_TDP_titulo2.setText("Titulo");
        lbl_TDP_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_TDP_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 520, 80, 25));

        lbl_TDP_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_TDP_error.setText(" . . .");
        jp_Componentes.add(lbl_TDP_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 520, 340, 35));
        jp_Componentes.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 562, 1024, -1));

        btn_close.setText("Salir");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 640, 120, 30));

        lbl_save_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error.setText(" . . .");
        jp_Componentes.add(lbl_save_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 590, 400, 35));

        lbl_save_desc.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_desc.setText(" . . .");
        jp_Componentes.add(lbl_save_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 640, 610, 35));

        ddm_TFA.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ddm_TFAItemStateChanged(evt);
            }
        });
        jp_Componentes.add(ddm_TFA, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 50, 130, 30));
        ddm_TFA.getAccessibleContext().setAccessibleName("");

        ddm_TCI.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ddm_TCIItemStateChanged(evt);
            }
        });
        jp_Componentes.add(ddm_TCI, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 210, 130, 30));

        ddm_TUC.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ddm_TUCItemStateChanged(evt);
            }
        });
        jp_Componentes.add(ddm_TUC, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 300, 130, 30));

        btn_TDI_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_TDI_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_TDI_ayuda.setContentAreaFilled(false);
        btn_TDI_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_TDI_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 400, 25, 25));

        btn_TFA_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_TFA_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_TFA_ayuda.setContentAreaFilled(false);
        btn_TFA_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_TFA_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 50, 25, 25));

        btn_TML_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_TML_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_TML_ayuda.setContentAreaFilled(false);
        btn_TML_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_TML_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 110, 25, 25));

        btn_TCA_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_TCA_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_TCA_ayuda.setContentAreaFilled(false);
        btn_TCA_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_TCA_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 160, 25, 25));
        jp_Componentes.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 294, 580, 2));

        btn_TRE_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_TRE_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_TRE_ayuda.setContentAreaFilled(false);
        btn_TRE_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_TRE_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 460, 25, 25));

        btn_TDP_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_TDP_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_TDP_ayuda.setContentAreaFilled(false);
        btn_TDP_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_TDP_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 520, 25, 25));

        lbl_save_titulo1.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_titulo1.setText(" . . .");
        jp_Componentes.add(lbl_save_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 590, 300, 35));

        txt_TML_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_TML_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 110, 130, 25));

        jsp_DatosEntrada.setViewportView(jp_Componentes);

        btn_close2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/cerrar.png"))); // NOI18N
        btn_close2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_close2.setContentAreaFilled(false);
        btn_close2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_close2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jsp_DatosEntrada, javax.swing.GroupLayout.DEFAULT_SIZE, 1008, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_close2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(btn_close2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jsp_DatosEntrada, javax.swing.GroupLayout.DEFAULT_SIZE, 706, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        try {
            if (!bod.pathProyecto.isEmpty()) {
                Guardar();
                Main main = (Main) this.getTopLevelAncestor(); // Trata de ir  al contenedor principal y activa o desactivar botones
                main.comprobarBotones();
                main.CargarDatosFondo();
            }
        } catch (Exception ex) {
            log.error("Error en acción boton guardar() " + ex.getMessage());
        }
    }//GEN-LAST:event_btn_guardarActionPerformed

    private void btn_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_closeActionPerformed
        cerrar();
    }//GEN-LAST:event_btn_closeActionPerformed

    private void btn_close2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_close2ActionPerformed
        cerrar();
    }//GEN-LAST:event_btn_close2ActionPerformed

    private void ddm_TCIItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ddm_TCIItemStateChanged
        ejecutarFunciones("TCI");
    }//GEN-LAST:event_ddm_TCIItemStateChanged

    private void ddm_TUCItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ddm_TUCItemStateChanged
        ejecutarFunciones("TUC");
    }//GEN-LAST:event_ddm_TUCItemStateChanged

    private void ddm_TFAItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ddm_TFAItemStateChanged
        ejecutarFunciones("TFA");
    }//GEN-LAST:event_ddm_TFAItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_TCA_ayuda;
    private javax.swing.JButton btn_TDI_ayuda;
    private javax.swing.JButton btn_TDP_ayuda;
    private javax.swing.JButton btn_TFA_ayuda;
    private javax.swing.JButton btn_TML_ayuda;
    private javax.swing.JButton btn_TRE_ayuda;
    private javax.swing.JButton btn_close;
    private javax.swing.JButton btn_close2;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JComboBox ddm_TCI;
    private javax.swing.JComboBox ddm_TFA;
    private javax.swing.JComboBox ddm_TUC;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JPanel jp_Componentes;
    private javax.swing.JScrollPane jsp_DatosEntrada;
    private javax.swing.JLabel lbl_TCA_desc;
    private javax.swing.JLabel lbl_TCA_error;
    private javax.swing.JLabel lbl_TCA_titulo1;
    private javax.swing.JLabel lbl_TCA_titulo2;
    private javax.swing.JLabel lbl_TCI_error;
    private javax.swing.JLabel lbl_TCI_titulo1;
    private javax.swing.JLabel lbl_TCR_error;
    private javax.swing.JLabel lbl_TCR_titulo1;
    private javax.swing.JLabel lbl_TCR_titulo2;
    private javax.swing.JLabel lbl_TDI_error;
    private javax.swing.JLabel lbl_TDI_titulo1;
    private javax.swing.JLabel lbl_TDI_titulo2;
    private javax.swing.JLabel lbl_TDP_error;
    private javax.swing.JLabel lbl_TDP_titulo1;
    private javax.swing.JLabel lbl_TDP_titulo2;
    private javax.swing.JLabel lbl_TFA_error;
    private javax.swing.JLabel lbl_TFA_titulo1;
    private javax.swing.JLabel lbl_TML_error;
    private javax.swing.JLabel lbl_TML_titulo1;
    private javax.swing.JLabel lbl_TML_titulo2;
    private javax.swing.JLabel lbl_TRE_error;
    private javax.swing.JLabel lbl_TRE_titulo1;
    private javax.swing.JLabel lbl_TRE_titulo2;
    private javax.swing.JLabel lbl_TUC_error;
    private javax.swing.JLabel lbl_TUC_titulo1;
    private javax.swing.JLabel lbl_save_desc;
    private javax.swing.JLabel lbl_save_error;
    private javax.swing.JLabel lbl_save_titulo1;
    private javax.swing.JLabel lbl_titulo1;
    private javax.swing.JLabel lbl_titulo2;
    private javax.swing.JTextField txt_TCA_Pregunta;
    private javax.swing.JTextField txt_TCR_Pregunta;
    private javax.swing.JTextField txt_TDI_Pregunta;
    private javax.swing.JTextField txt_TDP_Pregunta;
    private javax.swing.JTextField txt_TML_Pregunta;
    private javax.swing.JTextField txt_TRE_Pregunta;
    // End of variables declaration//GEN-END:variables
}
