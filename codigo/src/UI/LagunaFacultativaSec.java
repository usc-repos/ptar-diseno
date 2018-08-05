package UI;

import BO.Bod;
import DB.Dao;
import Componentes.Util;
import org.apache.log4j.Logger;
import Componentes.Configuracion;
import Componentes.Listener_Popup;
import Componentes.Pregunta;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class LagunaFacultativaSec extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("Laguna Facultativa Sec."); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    public Map<String, Pregunta> map = new TreeMap<>();
    private Bod bod = new Bod("");
    private Listener_Popup Lpopup;
    Util util = new Util();
    Pregunta pg;
    private boolean eSave = true;

    public LagunaFacultativaSec(Bod bod) {
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
        bod.WinLagunaFacultativaSec = true;//Bandera La ventana se ha abierto

        try {//Todo: implementar donde esta error
            btn_guardar.setText("Guardar");
            btn_close.setText("Cerrar");

            // - - - - - - Titulo de la sección            
            rs = db.ResultadoSelect("obtenerseccion", "6", null);

            lbl_titulo1.setText(rs.getString("Nombre")); //Título de este jpane
            lbl_titulo2.setText(rs.getString("Mensaje"));

            // - - - - - - Pregunta 45 Cuestionario 3 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q2C Calculo de Caudales de Diseno
            rs = db.ResultadoSelect("datospregunta", "Q2C");

            String[] titulo2 = rs.getString("titulo2").split("\\|"); //Q2C en el titulo 2 tiene 2 posibles textos

            lbl_Q2C_titulo1.setText(rs.getString("titulo1"));
            lbl_Q2C_titulo2.setText(titulo2[1].trim());
            txt_Q2C_Pregunta.setText(bod.getVarFormateada(bod.calcularQ2Cm3Dia(), rs.getInt("decimales"))); // Q2C en m³/dia
            // - - - - - - Pregunta 6 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LAE
            rs = db.ResultadoSelect("datospregunta", "LAE");

            lbl_LAE_titulo1.setText(rs.getString("titulo1"));
            lbl_LAE_titulo2.setText(rs.getString("titulo2"));
            txt_LAE_Pregunta.setText(bod.getVarFormateada(bod.getVarDob("LAE"), rs.getInt("decimales")));
            // - - - - - - Pregunta 1 Cuestionario 4 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - CAT
            rs = db.ResultadoSelect("datospregunta", "CAT");

            lbl_CAT_titulo1.setText(rs.getString("titulo1"));
            lbl_CAT_titulo2.setText(rs.getString("titulo2"));
            txt_CAT_Pregunta.setText(bod.getVarFormateada(bod.getVarDob("CAT"), rs.getInt("decimales")));
            // - - - - - - Pregunta 4 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FTE
            rs = db.ResultadoSelect("datospregunta", "FTE");

            pg = new Pregunta(); //Objeto que será creado con datos, básicos de rangos y mensajes
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("FTE", pg);

            lbl_FTE_titulo1.setText(pg.tit1);
            lbl_FTE_titulo2.setText(rs.getString("titulo2"));
            txt_FTE_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_FTE_Pregunta.addFocusListener(new FocusAdapter() { //Listener que se dispara cuando la casilla pierde focus
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("FTE");
                }
            });

            AsignarPopupBtn(btn_FTE_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 450, 50);
            // - - - - - - Pregunta 5 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FCO
            rs = db.ResultadoSelect("datospregunta", "FCO");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("FCO", pg);

            lbl_FCO_titulo1.setText(pg.tit1);
            lbl_FCO_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_FCO_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 440, 360);
            // - - - - - - Pregunta 6 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FCA
            rs = db.ResultadoSelect("datospregunta", "FCA");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("FCA", pg);

            lbl_FCA_titulo1.setText(pg.tit1);
            lbl_FCA_titulo2.setText(rs.getString("titulo2"));
            txt_FCA_Pregunta.setText(rs.getString("valorpordefecto"));
            lbl_FCA_desc.setText(pg.desc);

            txt_FCA_Pregunta.addFocusListener(new FocusAdapter() { //Listener que se dispara cuando la casilla pierde focus
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("FCA");
                }
            });

            AsignarPopupBtn(btn_FCA_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 490, 310);
            // - - - - - - Pregunta 7 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FAS
            rs = db.ResultadoSelect("datospregunta", "FAS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("FAS", pg);

            lbl_FAS_titulo1.setText(pg.tit1);
            lbl_FAS_titulo2.setText(rs.getString("titulo2"));
            
            AsignarPopupBtn(btn_FAS_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 390, 260);
            // - - - - - - Pregunta 8 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FTR
            rs = db.ResultadoSelect("datospregunta", "FTR");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("FTR", pg);

            lbl_FTR_titulo1.setText(pg.tit1);
            lbl_FTR_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_FTR_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 460, 420);
            // - - - - - - Pregunta 9 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FED
            rs = db.ResultadoSelect("datospregunta", "FED");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("FED", pg);

            lbl_FED_titulo1.setText(pg.tit1);
            lbl_FED_titulo2.setText(rs.getString("titulo2"));
            
            AsignarPopupBtn(btn_FED_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 280);
            // - - - - - - Pregunta 10 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FUV
            rs = db.ResultadoSelect("datospregunta", "FUV");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("FUV", pg);

            lbl_FUV_titulo1.setText(pg.tit1);
            lbl_FUV_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 11 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FSA
            rs = db.ResultadoSelect("datospregunta", "FSA");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("FSA", pg);

            lbl_FSA_titulo1.setText(pg.tit1);
            lbl_FSA_titulo2.setText(rs.getString("titulo2"));
            lbl_FSA_desc.setText(pg.desc);

            AsignarPopupBtn(btn_FSA_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 380, 150);
            // - - - - - - Pregunta 12 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FSL
            rs = db.ResultadoSelect("datospregunta", "FSL");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("FSL", pg);

            lbl_FSL_titulo1.setText(pg.tit1);
            lbl_FSL_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 13 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FAP
            rs = db.ResultadoSelect("datospregunta", "FAP");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("FAP", pg);

            lbl_FAP_titulo1.setText(pg.tit1);
            lbl_FAP_titulo2.setText(rs.getString("titulo2"));
            txt_FAP_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_FAP_Pregunta.addFocusListener(new FocusAdapter() { //Listener que se dispara cuando la casilla pierde focus
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("FAP");
                }
            });

            AsignarPopupBtn(btn_FAP_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 310, 160);
            // - - - - - - Pregunta 14 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FAI
            rs = db.ResultadoSelect("datospregunta", "FAI");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("FAI", pg);

            lbl_FAI_titulo1.setText(pg.tit1);
            lbl_FAI_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_FAI_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 250, 150);
            // - - - - - - Pregunta 15 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FAB
            rs = db.ResultadoSelect("datospregunta", "FAB");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("FAB", pg);

            lbl_FAB_titulo1.setText(pg.tit1);
            lbl_FAB_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_FAB_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 350, 220);
            // - - - - - - Pregunta 16 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FLA
            rs = db.ResultadoSelect("datospregunta", "FLA");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("FLA", pg);

            lbl_FLA_titulo1.setText(pg.tit1);
            lbl_FLA_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
            // Si Bod cargó datos de Laguna Facultativa secundaria desde la BD, porque Ya estaba editada, se proceden a llenar casillas con datos
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -          
            if (bod.EditLagunaFacultativaSec) {

                txt_FTE_Pregunta.setText(bod.getVarFormateadaPorNombre("FTE", map.get("FTE").deci)); //Trae la variable por nombre y la formatea por cantidad de decimales
                txt_FCO_Pregunta.setText(bod.getVarFormateadaPorNombre("FCO", map.get("FCO").deci));
                txt_FCA_Pregunta.setText(bod.getVarFormateadaPorNombre("FCA", map.get("FCA").deci));
                txt_FAS_Pregunta.setText(bod.getVarFormateadaPorNombre("FAS", map.get("FAS").deci));
                txt_FTR_Pregunta.setText(bod.getVarFormateadaPorNombre("FTR", map.get("FTR").deci));
                txt_FED_Pregunta.setText(bod.getVarFormateadaPorNombre("FED", map.get("FED").deci));
                txt_FUV_Pregunta.setText(bod.getVarFormateadaPorNombre("FUV", map.get("FUV").deci));
                txt_FSA_Pregunta.setText(bod.getVarFormateadaPorNombre("FSA", map.get("FSA").deci));
                txt_FSL_Pregunta.setText(bod.getVarFormateadaPorNombre("FSL", map.get("FSL").deci));
                txt_FAP_Pregunta.setText(bod.getVarFormateadaPorNombre("FAP", map.get("FAP").deci));
                txt_FAI_Pregunta.setText(bod.getVarFormateadaPorNombre("FAI", map.get("FAI").deci));
                txt_FAB_Pregunta.setText(bod.getVarFormateadaPorNombre("FAB", map.get("FAB").deci));
                txt_FLA_Pregunta.setText(bod.getVarFormateadaPorNombre("FLA", map.get("FLA").deci));
            } else {// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -si no realiza otras tareas de cálculos que se deben hacer al cargar

                ejecutarFunciones("FTE");
                ejecutarFunciones("FCA");
                ejecutarFunciones("FAP");
                ejecutarFunciones("FAB");
                ejecutarFunciones("FLA");
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

    private void borradoMsgError() {
        lbl_CAT_error.setText("");
        lbl_FAB_error.setText("");
        lbl_FAI_error.setText("");
        lbl_FAP_error.setText("");
        lbl_FAS_error.setText("");
        lbl_FCA_error.setText("");
        lbl_FCO_error.setText("");
        lbl_FED_error.setText("");
        lbl_FLA_error.setText("");
        lbl_FSA_error.setText("");
        lbl_FSL_error.setText("");
        lbl_FTE_error.setText("");
        lbl_FTR_error.setText("");
        lbl_FUV_error.setText("");
        lbl_LAE_error.setText("");
        lbl_Q2C_error.setText("");
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

        if (!CalcularFTE()) {
            lbl_save_error.setText(map.get("FTE").erro);
            lbl_save_desc.setText(map.get("FTE").desc);
            lbl_save_error.setText(map.get("FTE").tit1);
            return false;
        }

        if (!CalcularFCO()) {
            lbl_save_error.setText(map.get("FCO").erro);
            lbl_save_desc.setText(map.get("FCO").desc);
            lbl_save_titulo1.setText(map.get("FCO").tit1);
            return false;
        }

        if (!CalcularFCA()) {
            lbl_save_error.setText(map.get("FCA").erro);
            lbl_save_desc.setText(map.get("FCA").desc);
            lbl_save_titulo1.setText(map.get("FCA").tit1);
            return false;
        }

        if (!CalcularFAS()) {
            lbl_save_error.setText(map.get("FAS").erro);
            lbl_save_desc.setText(map.get("FAS").desc);
            lbl_save_titulo1.setText(map.get("FAS").tit1);
            return false;
        }

        if (!CalcularFTR()) {
            lbl_save_error.setText(map.get("FTR").erro);
            lbl_save_desc.setText(map.get("FTR").desc);
            lbl_save_titulo1.setText(map.get("FTR").tit1);
            return false;
        }

        if (!CalcularFED()) {
            lbl_save_error.setText(map.get("FED").erro);
            lbl_save_desc.setText(map.get("FED").desc);
            lbl_save_titulo1.setText(map.get("FED").tit1);
            return false;
        }

        if (!CalcularFUV()) {
            lbl_save_error.setText(map.get("FUV").erro);
            lbl_save_desc.setText(map.get("FUV").desc);
            lbl_save_titulo1.setText(map.get("FUV").tit1);
            return false;
        }

        if (!CalcularFSA()) {
            lbl_save_error.setText(map.get("FSA").erro);
            lbl_save_desc.setText(map.get("FSA").desc);
            lbl_save_titulo1.setText(map.get("FSA").tit1);
            return false;
        }

        if (!CalcularFSL()) {
            lbl_save_error.setText(map.get("FSL").erro);
            lbl_save_desc.setText(map.get("FSL").desc);
            lbl_save_titulo1.setText(map.get("FSL").tit1);
            return false;
        }

        if (!CalcularFAP()) {
            lbl_save_error.setText(map.get("FAP").erro);
            lbl_save_desc.setText(map.get("FAP").desc);
            lbl_save_titulo1.setText(map.get("FAP").tit1);
            return false;
        }

        if (!CalcularFAI()) {
            lbl_save_error.setText(map.get("FAI").erro);
            lbl_save_desc.setText(map.get("FAI").desc);
            lbl_save_titulo1.setText(map.get("FAI").tit1);
            return false;
        }

        if (!CalcularFAB()) {
            lbl_save_error.setText(map.get("FAB").erro);
            lbl_save_desc.setText(map.get("FAB").desc);
            lbl_save_titulo1.setText(map.get("FAB").tit1);
            return false;
        }

        if (!CalcularFLA()) {
            lbl_save_error.setText(map.get("FLA").erro);
            lbl_save_desc.setText(map.get("FLA").desc);
            lbl_save_titulo1.setText(map.get("FLA").tit1);
            return false;
        }

        Main main = (Main) this.getTopLevelAncestor();
        main.cancela = false;
        main.vbod = this.bod;
        bod.EditLagunaFacultativaSec = true;

        if (!bod.exportarProyecto(true)) {
            bod.EditLagunaFacultativaSec = false;
            main.cancela = false;
            main.vbod = null;
            return false;
        }
        main.vbod = null;
        main.bod = this.bod;
        eSave = true;
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

    private boolean CalcularFTE() {

        try {
            lbl_FTE_error.setText("");

            if (bod.setVarDob(txt_FTE_Pregunta.getText(), "FTE", map.get("FTE").rmin, map.get("FTE").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularFTE " + ex.getMessage());
        }
        lbl_FTE_error.setText(map.get("FTE").erro);
        return false;
    }

    private boolean CalcularFCO() {

        try {
            lbl_FCO_error.setText("");
            double x = bod.calcularFCO();

            if (bod.setVarDob(x, "FCO", map.get("FCO").rmin, map.get("FCO").rmax)) {
                txt_FCO_Pregunta.setText(bod.getVarFormateadaPorNombre("FCO", map.get("FCO").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularFCO " + ex.getMessage());
        }
        lbl_FCO_error.setText(map.get("FCO").erro);
        return false;
    }

    private boolean CalcularFCA() {

        try {
            lbl_FCA_error.setText("");

            if (bod.setVarDob(txt_FCA_Pregunta.getText(), "FCA", map.get("FCA").rmin, map.get("FCA").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularFCA " + ex.getMessage());
        }
        lbl_FCA_error.setText(map.get("FCA").erro);
        return false;
    }

    private boolean CalcularFAS() {

        try {
            lbl_FAS_error.setText("");
            double x = bod.calcularFAS();

            if (bod.setVarDob(x, "FAS", map.get("FAS").rmin, map.get("FAS").rmax)) {
                txt_FAS_Pregunta.setText(bod.getVarFormateadaPorNombre("FAS", map.get("FAS").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularFAS " + ex.getMessage());
        }
        lbl_FAS_error.setText(map.get("FAS").erro);
        return false;
    }

    private boolean CalcularFTR() {

        try {
            lbl_FTR_error.setText("");
            double x = bod.calcularFTR();

            if (bod.setVarDob(x, "FTR", map.get("FTR").rmin, map.get("FTR").rmax)) {
                txt_FTR_Pregunta.setText(bod.getVarFormateadaPorNombre("FTR", map.get("FTR").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularFTR " + ex.getMessage());
        }
        lbl_FTR_error.setText(map.get("FTR").erro);
        return false;
    }

    private boolean CalcularFED() {

        try {
            lbl_FED_error.setText("");
            double x = bod.calcularFED();

            if (bod.setVarDob(x, "FED", map.get("FED").rmin, map.get("FED").rmax)) {
                txt_FED_Pregunta.setText(bod.getVarFormateadaPorNombre("FED", map.get("FED").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularFED " + ex.getMessage());
        }
        lbl_FED_error.setText(map.get("FED").erro);
        return false;
    }

    private boolean CalcularFUV() {

        try {
            lbl_FUV_error.setText("");
            double x = bod.calcularFUV();

            if (bod.setVarDob(x, "FUV", map.get("FUV").rmin, map.get("FUV").rmax)) {
                txt_FUV_Pregunta.setText(bod.getVarFormateadaPorNombre("FUV", map.get("FUV").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularFUV " + ex.getMessage());
        }
        lbl_FUV_error.setText(map.get("FUV").erro);
        return false;
    }

    private boolean CalcularFSA() {

        try {
            lbl_FSA_error.setText("");
            double x = bod.calcularFSA();

            if (bod.setVarDob(x, "FSA", map.get("FSA").rmin, map.get("FSA").rmax)) {
                txt_FSA_Pregunta.setText(bod.getVarFormateadaPorNombre("FSA", map.get("FSA").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularFSA " + ex.getMessage());
        }
        lbl_FSA_error.setText(map.get("FSA").erro);
        return false;
    }

    private boolean CalcularFSL() {

        try {
            lbl_FSL_error.setText("");
            double x = bod.calcularFSL();

            if (bod.setVarDob(x, "FSL", map.get("FSL").rmin, map.get("FSL").rmax)) {
                txt_FSL_Pregunta.setText(bod.getVarFormateadaPorNombre("FSL", map.get("FSL").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularFSL " + ex.getMessage());
        }
        lbl_FSL_error.setText(map.get("FSL").erro);
        return false;
    }

    private boolean CalcularFAP() {

        try {
            lbl_FAP_error.setText("");

            if (bod.setVarInt(txt_FAP_Pregunta.getText(), "FAP", (int) map.get("FAP").rmin, (int) map.get("FAP").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularFAP " + ex.getMessage());
        }
        lbl_FAP_error.setText(map.get("FAP").erro);
        return false;
    }

    private boolean CalcularFAI() {

        try {
            lbl_FAI_error.setText("");
            double x = bod.calcularFAI();

            if (bod.setVarDob(x, "FAI", map.get("FAI").rmin, map.get("FAI").rmax)) {
                txt_FAI_Pregunta.setText(bod.getVarFormateadaPorNombre("FAI", map.get("FAI").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularFAI " + ex.getMessage());
        }
        lbl_FAI_error.setText(map.get("FAI").erro);
        return false;
    }

    private boolean CalcularFAB() {

        try {
            lbl_FAB_error.setText("");
            String x = bod.calcularFAB();

            if (bod.setVarDob(x, "FAB", map.get("FAB").rmin, map.get("FAB").rmax)) {
                txt_FAB_Pregunta.setText(bod.getVarFormateadaPorNombre("FAB", map.get("FAB").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularFAB " + ex.getMessage());
        }
        lbl_FAB_error.setText(map.get("FAB").erro);
        return false;
    }

    private boolean CalcularFLA() {

        try {
            lbl_FLA_error.setText("");
            double x = bod.calcularFLA();

            if (bod.setVarDob(x, "FLA", map.get("FLA").rmin, map.get("FLA").rmax)) {
                txt_FLA_Pregunta.setText(bod.getVarFormateadaPorNombre("FLA", map.get("FLA").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularFLA " + ex.getMessage());
        }
        lbl_FLA_error.setText(map.get("FLA").erro);
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

            case "FTE":
                CalcularFTE();
                ejecutarFunciones("FCO");
                break;

            case "FCO":
                CalcularFCO();
                ejecutarFunciones("FAS");
                ejecutarFunciones("FUV");
                break;

            case "FCA":
                CalcularFCA();
                ejecutarFunciones("FTR");
                ejecutarFunciones("FUV");
                break;

            case "FAS":
                CalcularFAS();
                ejecutarFunciones("FTR");
                ejecutarFunciones("FSA");
                ejecutarFunciones("FAB");
                ejecutarFunciones("FUV");
                break;

            case "FTR":
                CalcularFTR();
                ejecutarFunciones("FED");
                break;

            case "FED":
                CalcularFED();
                break;

            case "FUV":
                CalcularFUV();
                break;

            case "FSA":
                CalcularFSA();
                ejecutarFunciones("FSL");
                break;

            case "FSL":
                CalcularFSL();
                break;

            case "FAP":
                CalcularFAP();
                ejecutarFunciones("FAI");
                break;

            case "FAI":
                CalcularFAI();
                break;

            case "FAB":
                CalcularFAB();
                break;

            case "FLA":
                CalcularFLA();
                break;
        }
    }

    private void cerrar() {
        if (!eSave) {
            int n = util.Mensaje("¿Desea cerrar? \n Se perderan los cambios realizados?", "yesno");
            if (n == JOptionPane.NO_OPTION) {
                return;
            }
        }
        bod.WinLagunaFacultativaSec = false;
        Main main = (Main) this.getTopLevelAncestor();
        main.bod.WinLagunaFacultativaSec = false;
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jsp_LagunaFacultativasec = new javax.swing.JScrollPane();
        jp_Componentes = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        lbl_titulo1 = new javax.swing.JLabel();
        lbl_titulo2 = new javax.swing.JLabel();
        btn_guardar = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        txt_Q2C_Pregunta = new javax.swing.JTextField();
        lbl_Q2C_titulo1 = new javax.swing.JLabel();
        lbl_Q2C_titulo2 = new javax.swing.JLabel();
        lbl_LAE_error = new javax.swing.JLabel();
        lbl_LAE_titulo1 = new javax.swing.JLabel();
        txt_LAE_Pregunta = new javax.swing.JTextField();
        lbl_LAE_titulo2 = new javax.swing.JLabel();
        lbl_Q2C_error = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        btn_close = new javax.swing.JButton();
        lbl_save_error = new javax.swing.JLabel();
        lbl_save_desc = new javax.swing.JLabel();
        lbl_CAT_titulo1 = new javax.swing.JLabel();
        txt_CAT_Pregunta = new javax.swing.JTextField();
        lbl_CAT_titulo2 = new javax.swing.JLabel();
        lbl_CAT_error = new javax.swing.JLabel();
        txt_FTE_Pregunta = new javax.swing.JTextField();
        lbl_FTE_titulo1 = new javax.swing.JLabel();
        lbl_FTE_error = new javax.swing.JLabel();
        lbl_FTE_titulo2 = new javax.swing.JLabel();
        lbl_FCA_error = new javax.swing.JLabel();
        lbl_FCA_titulo2 = new javax.swing.JLabel();
        txt_FCA_Pregunta = new javax.swing.JTextField();
        lbl_FCA_titulo1 = new javax.swing.JLabel();
        lbl_FCA_desc = new javax.swing.JLabel();
        lbl_FAS_error = new javax.swing.JLabel();
        lbl_FAS_titulo2 = new javax.swing.JLabel();
        txt_FAS_Pregunta = new javax.swing.JTextField();
        lbl_FAS_titulo1 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        txt_FED_Pregunta = new javax.swing.JTextField();
        lbl_FED_titulo1 = new javax.swing.JLabel();
        lbl_FUV_error = new javax.swing.JLabel();
        lbl_FUV_titulo2 = new javax.swing.JLabel();
        lbl_FED_error = new javax.swing.JLabel();
        txt_FUV_Pregunta = new javax.swing.JTextField();
        lbl_FUV_titulo1 = new javax.swing.JLabel();
        lbl_FED_titulo2 = new javax.swing.JLabel();
        lbl_FTR_titulo1 = new javax.swing.JLabel();
        txt_FTR_Pregunta = new javax.swing.JTextField();
        lbl_FTR_titulo2 = new javax.swing.JLabel();
        lbl_FTR_error = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        lbl_FSA_desc = new javax.swing.JLabel();
        lbl_FSA_titulo1 = new javax.swing.JLabel();
        lbl_FSA_error = new javax.swing.JLabel();
        lbl_FSA_titulo2 = new javax.swing.JLabel();
        txt_FSA_Pregunta = new javax.swing.JTextField();
        lbl_FSL_titulo1 = new javax.swing.JLabel();
        lbl_FSL_error = new javax.swing.JLabel();
        lbl_FSL_titulo2 = new javax.swing.JLabel();
        txt_FSL_Pregunta = new javax.swing.JTextField();
        lbl_FCO_titulo1 = new javax.swing.JLabel();
        txt_FCO_Pregunta = new javax.swing.JTextField();
        lbl_FCO_titulo2 = new javax.swing.JLabel();
        lbl_FCO_error = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        txt_FAB_Pregunta = new javax.swing.JTextField();
        lbl_FAB_error = new javax.swing.JLabel();
        lbl_FAB_titulo1 = new javax.swing.JLabel();
        lbl_FAP_titulo2 = new javax.swing.JLabel();
        lbl_FAB_titulo2 = new javax.swing.JLabel();
        txt_FAI_Pregunta = new javax.swing.JTextField();
        lbl_FAP_titulo1 = new javax.swing.JLabel();
        lbl_FAI_error = new javax.swing.JLabel();
        lbl_FAI_titulo2 = new javax.swing.JLabel();
        txt_FAP_Pregunta = new javax.swing.JTextField();
        lbl_FAI_titulo1 = new javax.swing.JLabel();
        lbl_FAP_error = new javax.swing.JLabel();
        btn_FTE_ayuda = new javax.swing.JButton();
        btn_FAB_ayuda = new javax.swing.JButton();
        btn_FCO_ayuda = new javax.swing.JButton();
        btn_FCA_ayuda = new javax.swing.JButton();
        btn_FTR_ayuda = new javax.swing.JButton();
        btn_FSA_ayuda = new javax.swing.JButton();
        btn_FAP_ayuda = new javax.swing.JButton();
        btn_FAI_ayuda = new javax.swing.JButton();
        lbl_save_titulo1 = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        lbl_FLA_titulo2 = new javax.swing.JLabel();
        lbl_FLA_titulo1 = new javax.swing.JLabel();
        txt_FLA_Pregunta = new javax.swing.JTextField();
        lbl_FLA_error = new javax.swing.JLabel();
        btn_FAS_ayuda = new javax.swing.JButton();
        btn_FED_ayuda = new javax.swing.JButton();
        btn_close2 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1024, 768));

        jsp_LagunaFacultativasec.setPreferredSize(new java.awt.Dimension(1024, 768));

        jp_Componentes.setPreferredSize(new java.awt.Dimension(1024, 1020));
        jp_Componentes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jp_Componentes.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 1024, 10));

        lbl_titulo1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_titulo1.setForeground(new java.awt.Color(51, 51, 51));
        lbl_titulo1.setText("Titulo");
        jp_Componentes.add(lbl_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 300, 30));

        lbl_titulo2.setForeground(new java.awt.Color(0, 51, 102));
        lbl_titulo2.setText("Desc");
        jp_Componentes.add(lbl_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 0, 600, 30));

        btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Button_Salva_Guardar_21.png"))); // NOI18N
        btn_guardar.setText("save");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 860, 120, -1));
        jp_Componentes.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 252, 1024, 2));

        txt_Q2C_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q2C_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 50, 130, 25));

        lbl_Q2C_titulo1.setText("Titulo");
        lbl_Q2C_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2C_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 300, 30));

        lbl_Q2C_titulo2.setText("Titulo");
        lbl_Q2C_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2C_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 50, 80, 25));

        lbl_LAE_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_LAE_error.setText(" . . .");
        jp_Componentes.add(lbl_LAE_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 90, 340, 35));

        lbl_LAE_titulo1.setText("Titulo");
        lbl_LAE_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LAE_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 300, 30));

        txt_LAE_Pregunta.setEditable(false);
        jp_Componentes.add(txt_LAE_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 90, 130, 25));

        lbl_LAE_titulo2.setText("Titulo");
        lbl_LAE_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LAE_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 90, 80, 25));

        lbl_Q2C_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_Q2C_error.setText(" . . .");
        jp_Componentes.add(lbl_Q2C_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 50, 340, 35));
        jp_Componentes.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 522, 1024, 2));

        btn_close.setText("Salir");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 900, 120, 30));

        lbl_save_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error.setText(" . . .");
        jp_Componentes.add(lbl_save_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 860, 500, 35));

        lbl_save_desc.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_desc.setText(" . . .");
        jp_Componentes.add(lbl_save_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 900, 610, 35));

        lbl_CAT_titulo1.setText("Titulo");
        lbl_CAT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 300, 30));

        txt_CAT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_CAT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 130, 130, 25));

        lbl_CAT_titulo2.setText("Titulo");
        lbl_CAT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 130, 80, 25));

        lbl_CAT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_CAT_error.setText(" . . .");
        jp_Componentes.add(lbl_CAT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 130, 340, 35));

        txt_FTE_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_FTE_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 170, 130, 25));

        lbl_FTE_titulo1.setText("Titulo");
        lbl_FTE_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FTE_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 300, 30));

        lbl_FTE_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FTE_error.setText(" . . .");
        jp_Componentes.add(lbl_FTE_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 170, 340, 35));

        lbl_FTE_titulo2.setText("Titulo");
        lbl_FTE_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FTE_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 170, 80, 25));

        lbl_FCA_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FCA_error.setText(" . . .");
        jp_Componentes.add(lbl_FCA_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 290, 340, 35));

        lbl_FCA_titulo2.setText("Titulo");
        lbl_FCA_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FCA_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 300, 80, 25));

        txt_FCA_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_FCA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 300, 130, 25));

        lbl_FCA_titulo1.setText("Titulo");
        lbl_FCA_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FCA_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 300, 30));

        lbl_FCA_desc.setText("Desc");
        lbl_FCA_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FCA_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 600, 30));

        lbl_FAS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FAS_error.setText(" . . .");
        jp_Componentes.add(lbl_FAS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 330, 340, 35));

        lbl_FAS_titulo2.setText("Titulo");
        lbl_FAS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FAS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 340, 80, 25));

        txt_FAS_Pregunta.setEditable(false);
        jp_Componentes.add(txt_FAS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 340, 130, 25));

        lbl_FAS_titulo1.setText("Titulo");
        lbl_FAS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FAS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 300, 30));
        jp_Componentes.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 382, 1014, 2));

        txt_FED_Pregunta.setEditable(false);
        jp_Componentes.add(txt_FED_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 440, 130, 25));

        lbl_FED_titulo1.setText("Titulo");
        lbl_FED_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FED_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, 300, 30));

        lbl_FUV_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FUV_error.setText(" . . .");
        jp_Componentes.add(lbl_FUV_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 470, 340, 35));

        lbl_FUV_titulo2.setText("Titulo");
        lbl_FUV_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FUV_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 480, 80, 25));

        lbl_FED_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FED_error.setText(" . . .");
        jp_Componentes.add(lbl_FED_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 430, 340, 35));

        txt_FUV_Pregunta.setEditable(false);
        jp_Componentes.add(txt_FUV_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 480, 130, 25));

        lbl_FUV_titulo1.setText("Titulo");
        lbl_FUV_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FUV_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 480, 300, 30));

        lbl_FED_titulo2.setText("Titulo");
        lbl_FED_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FED_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 440, 80, 25));

        lbl_FTR_titulo1.setText("Titulo");
        lbl_FTR_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FTR_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 300, 30));

        txt_FTR_Pregunta.setEditable(false);
        jp_Componentes.add(txt_FTR_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 400, 130, 25));

        lbl_FTR_titulo2.setText("Titulo");
        lbl_FTR_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FTR_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 400, 80, 25));

        lbl_FTR_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FTR_error.setText(" . . .");
        jp_Componentes.add(lbl_FTR_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 390, 340, 35));
        jp_Componentes.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, 650, 1110, 2));

        lbl_FSA_desc.setText("Desc");
        lbl_FSA_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FSA_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 600, 30));

        lbl_FSA_titulo1.setText("Titulo");
        lbl_FSA_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FSA_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 570, 300, 30));

        lbl_FSA_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FSA_error.setText(" . . .");
        jp_Componentes.add(lbl_FSA_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 560, 340, 35));

        lbl_FSA_titulo2.setText("Titulo");
        lbl_FSA_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FSA_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 570, 80, 25));

        txt_FSA_Pregunta.setEditable(false);
        jp_Componentes.add(txt_FSA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 570, 130, 25));

        lbl_FSL_titulo1.setText("Titulo");
        lbl_FSL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FSL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 610, 300, 30));

        lbl_FSL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FSL_error.setText(" . . .");
        jp_Componentes.add(lbl_FSL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 600, 340, 35));

        lbl_FSL_titulo2.setText("Titulo");
        lbl_FSL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FSL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 610, 80, 25));

        txt_FSL_Pregunta.setEditable(false);
        jp_Componentes.add(txt_FSL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 610, 130, 25));

        lbl_FCO_titulo1.setText("Titulo");
        lbl_FCO_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FCO_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 300, 30));

        txt_FCO_Pregunta.setEditable(false);
        jp_Componentes.add(txt_FCO_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 210, 130, 25));

        lbl_FCO_titulo2.setText("Titulo");
        lbl_FCO_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FCO_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 210, 80, 25));

        lbl_FCO_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FCO_error.setText(" . . .");
        jp_Componentes.add(lbl_FCO_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 210, 340, 35));
        jp_Componentes.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 790, 1084, 2));

        txt_FAB_Pregunta.setEditable(false);
        jp_Componentes.add(txt_FAB_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 750, 130, 25));

        lbl_FAB_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FAB_error.setText(" . . .");
        jp_Componentes.add(lbl_FAB_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 740, 340, 35));

        lbl_FAB_titulo1.setText("Titulo");
        lbl_FAB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FAB_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 740, 300, 30));

        lbl_FAP_titulo2.setText("Titulo");
        lbl_FAP_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FAP_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 670, 80, 25));

        lbl_FAB_titulo2.setText("Titulo");
        lbl_FAB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FAB_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 750, 80, 25));

        txt_FAI_Pregunta.setEditable(false);
        jp_Componentes.add(txt_FAI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 710, 130, 25));

        lbl_FAP_titulo1.setText("Titulo");
        lbl_FAP_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FAP_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 660, 300, 30));

        lbl_FAI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FAI_error.setText(" . . .");
        jp_Componentes.add(lbl_FAI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 700, 340, 35));

        lbl_FAI_titulo2.setText("Titulo");
        lbl_FAI_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FAI_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 710, 80, 25));

        txt_FAP_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_FAP_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 670, 130, 25));

        lbl_FAI_titulo1.setText("Titulo");
        lbl_FAI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FAI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 700, 300, 30));

        lbl_FAP_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FAP_error.setText(" . . .");
        jp_Componentes.add(lbl_FAP_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 660, 340, 35));

        btn_FTE_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_FTE_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_FTE_ayuda.setContentAreaFilled(false);
        btn_FTE_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_FTE_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 170, 25, 25));

        btn_FAB_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_FAB_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_FAB_ayuda.setContentAreaFilled(false);
        btn_FAB_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_FAB_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 750, 25, 25));

        btn_FCO_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_FCO_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_FCO_ayuda.setContentAreaFilled(false);
        btn_FCO_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_FCO_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 210, 25, 25));

        btn_FCA_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_FCA_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_FCA_ayuda.setContentAreaFilled(false);
        btn_FCA_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_FCA_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 300, 25, 25));

        btn_FTR_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_FTR_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_FTR_ayuda.setContentAreaFilled(false);
        btn_FTR_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_FTR_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 400, 25, 25));

        btn_FSA_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_FSA_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_FSA_ayuda.setContentAreaFilled(false);
        btn_FSA_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_FSA_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 530, 25, 25));

        btn_FAP_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_FAP_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_FAP_ayuda.setContentAreaFilled(false);
        btn_FAP_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_FAP_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 670, 25, 25));

        btn_FAI_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_FAI_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_FAI_ayuda.setContentAreaFilled(false);
        btn_FAI_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_FAI_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 710, 25, 25));

        lbl_save_titulo1.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_titulo1.setText(" . . .");
        jp_Componentes.add(lbl_save_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 860, 300, 35));
        jp_Componentes.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 850, 1110, 2));

        lbl_FLA_titulo2.setText("Titulo");
        lbl_FLA_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FLA_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 810, 80, 25));

        lbl_FLA_titulo1.setText("Titulo");
        lbl_FLA_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FLA_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 800, 300, 30));

        txt_FLA_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_FLA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 810, 130, 25));

        lbl_FLA_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FLA_error.setText(" . . .");
        jp_Componentes.add(lbl_FLA_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 800, 340, 35));

        btn_FAS_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_FAS_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_FAS_ayuda.setContentAreaFilled(false);
        btn_FAS_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_FAS_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 340, 25, 25));

        btn_FED_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_FED_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_FED_ayuda.setContentAreaFilled(false);
        btn_FED_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_FED_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 440, 25, 25));

        jsp_LagunaFacultativasec.setViewportView(jp_Componentes);

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
            .addComponent(jsp_LagunaFacultativasec, javax.swing.GroupLayout.PREFERRED_SIZE, 1008, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_close2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btn_close2)
                .addGap(2, 2, 2)
                .addComponent(jsp_LagunaFacultativasec, javax.swing.GroupLayout.DEFAULT_SIZE, 964, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_FAB_ayuda;
    private javax.swing.JButton btn_FAI_ayuda;
    private javax.swing.JButton btn_FAP_ayuda;
    private javax.swing.JButton btn_FAS_ayuda;
    private javax.swing.JButton btn_FCA_ayuda;
    private javax.swing.JButton btn_FCO_ayuda;
    private javax.swing.JButton btn_FED_ayuda;
    private javax.swing.JButton btn_FSA_ayuda;
    private javax.swing.JButton btn_FTE_ayuda;
    private javax.swing.JButton btn_FTR_ayuda;
    private javax.swing.JButton btn_close;
    private javax.swing.JButton btn_close2;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JPanel jp_Componentes;
    private javax.swing.JScrollPane jsp_LagunaFacultativasec;
    private javax.swing.JLabel lbl_CAT_error;
    private javax.swing.JLabel lbl_CAT_titulo1;
    private javax.swing.JLabel lbl_CAT_titulo2;
    private javax.swing.JLabel lbl_FAB_error;
    private javax.swing.JLabel lbl_FAB_titulo1;
    private javax.swing.JLabel lbl_FAB_titulo2;
    private javax.swing.JLabel lbl_FAI_error;
    private javax.swing.JLabel lbl_FAI_titulo1;
    private javax.swing.JLabel lbl_FAI_titulo2;
    private javax.swing.JLabel lbl_FAP_error;
    private javax.swing.JLabel lbl_FAP_titulo1;
    private javax.swing.JLabel lbl_FAP_titulo2;
    private javax.swing.JLabel lbl_FAS_error;
    private javax.swing.JLabel lbl_FAS_titulo1;
    private javax.swing.JLabel lbl_FAS_titulo2;
    private javax.swing.JLabel lbl_FCA_desc;
    private javax.swing.JLabel lbl_FCA_error;
    private javax.swing.JLabel lbl_FCA_titulo1;
    private javax.swing.JLabel lbl_FCA_titulo2;
    private javax.swing.JLabel lbl_FCO_error;
    private javax.swing.JLabel lbl_FCO_titulo1;
    private javax.swing.JLabel lbl_FCO_titulo2;
    private javax.swing.JLabel lbl_FED_error;
    private javax.swing.JLabel lbl_FED_titulo1;
    private javax.swing.JLabel lbl_FED_titulo2;
    private javax.swing.JLabel lbl_FLA_error;
    private javax.swing.JLabel lbl_FLA_titulo1;
    private javax.swing.JLabel lbl_FLA_titulo2;
    private javax.swing.JLabel lbl_FSA_desc;
    private javax.swing.JLabel lbl_FSA_error;
    private javax.swing.JLabel lbl_FSA_titulo1;
    private javax.swing.JLabel lbl_FSA_titulo2;
    private javax.swing.JLabel lbl_FSL_error;
    private javax.swing.JLabel lbl_FSL_titulo1;
    private javax.swing.JLabel lbl_FSL_titulo2;
    private javax.swing.JLabel lbl_FTE_error;
    private javax.swing.JLabel lbl_FTE_titulo1;
    private javax.swing.JLabel lbl_FTE_titulo2;
    private javax.swing.JLabel lbl_FTR_error;
    private javax.swing.JLabel lbl_FTR_titulo1;
    private javax.swing.JLabel lbl_FTR_titulo2;
    private javax.swing.JLabel lbl_FUV_error;
    private javax.swing.JLabel lbl_FUV_titulo1;
    private javax.swing.JLabel lbl_FUV_titulo2;
    private javax.swing.JLabel lbl_LAE_error;
    private javax.swing.JLabel lbl_LAE_titulo1;
    private javax.swing.JLabel lbl_LAE_titulo2;
    private javax.swing.JLabel lbl_Q2C_error;
    private javax.swing.JLabel lbl_Q2C_titulo1;
    private javax.swing.JLabel lbl_Q2C_titulo2;
    private javax.swing.JLabel lbl_save_desc;
    private javax.swing.JLabel lbl_save_error;
    private javax.swing.JLabel lbl_save_titulo1;
    private javax.swing.JLabel lbl_titulo1;
    private javax.swing.JLabel lbl_titulo2;
    private javax.swing.JTextField txt_CAT_Pregunta;
    private javax.swing.JTextField txt_FAB_Pregunta;
    private javax.swing.JTextField txt_FAI_Pregunta;
    private javax.swing.JTextField txt_FAP_Pregunta;
    private javax.swing.JTextField txt_FAS_Pregunta;
    private javax.swing.JTextField txt_FCA_Pregunta;
    private javax.swing.JTextField txt_FCO_Pregunta;
    private javax.swing.JTextField txt_FED_Pregunta;
    private javax.swing.JTextField txt_FLA_Pregunta;
    private javax.swing.JTextField txt_FSA_Pregunta;
    private javax.swing.JTextField txt_FSL_Pregunta;
    private javax.swing.JTextField txt_FTE_Pregunta;
    private javax.swing.JTextField txt_FTR_Pregunta;
    private javax.swing.JTextField txt_FUV_Pregunta;
    private javax.swing.JTextField txt_LAE_Pregunta;
    private javax.swing.JTextField txt_Q2C_Pregunta;
    // End of variables declaration//GEN-END:variables
}
