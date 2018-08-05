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

public class LagunaFacultativaSecUASB extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("Laguna Facultativa Sec. UASB"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    public Map<String, Pregunta> map = new TreeMap<>();
    private Bod bod = new Bod("");
    private Listener_Popup Lpopup;
    Util util = new Util();
    Pregunta pg;
    private boolean eSave = true;

    public LagunaFacultativaSecUASB(Bod bod) {
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
        bod.WinLagunaFacultativaUASB = true;//Bandera La ventana se ha abierto

        try {//Todo: implementar donde esta error
            btn_guardar.setText("Guardar");
            btn_close.setText("Cerrar");

            // - - - - - - Titulo de la sección            
            rs = db.ResultadoSelect("obtenerseccion", "15", null);

            lbl_titulo1.setText(rs.getString("Nombre")); //Título de este jpane
            lbl_titulo2.setText(rs.getString("Mensaje"));

            // - - - - - - Pregunta 45 Cuestionario 3 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q2C Calculo de Caudales de Diseno
            rs = db.ResultadoSelect("datospregunta", "Q2C");

            String[] titulo2 = rs.getString("titulo2").split("\\|"); //Q2C en el titulo 2 tiene 2 posibles textos

            lbl_Q2C_titulo1.setText(rs.getString("titulo1"));
            lbl_Q2C_titulo2.setText(titulo2[1].trim());
            txt_Q2C_Pregunta.setText(bod.getVarFormateada(bod.calcularQ2Cm3Dia(), rs.getInt("decimales"))); // Q2C en m³/dia
            // - - - - - - Pregunta 6 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - UEQ(DBO) o UEC(DQO)
            if (bod.getVarDob("CAB") != 0) {
                rs = db.ResultadoSelect("datospregunta", "UEQ");

                lbl_LAE_titulo1.setText(rs.getString("titulo1"));
                lbl_LAE_titulo2.setText(rs.getString("titulo2"));
                txt_LAE_Pregunta.setText(bod.getVarFormateada(bod.getVarDob("UEQ"), rs.getInt("decimales")));
            } else {
                rs = db.ResultadoSelect("datospregunta", "UEC");

                lbl_LAE_titulo1.setText(rs.getString("titulo1"));
                lbl_LAE_titulo2.setText(rs.getString("titulo2"));
                txt_LAE_Pregunta.setText(bod.getVarFormateada(bod.getVarDob("UEC"), rs.getInt("decimales")));
            }
            // - - - - - - Pregunta 1 Cuestionario 4 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - CAT
            rs = db.ResultadoSelect("datospregunta", "CAT");

            lbl_CAT_titulo1.setText(rs.getString("titulo1"));
            lbl_CAT_titulo2.setText(rs.getString("titulo2"));
            txt_CAT_Pregunta.setText(bod.getVarFormateada(bod.getVarDob("CAT"), rs.getInt("decimales")));
            // - - - - - - Pregunta 4 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - GTE
            rs = db.ResultadoSelect("datospregunta", "GTE");

            pg = new Pregunta(); //Objeto que será creado con datos, básicos de rangos y mensajes
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("GTE", pg);

            lbl_GTE_titulo1.setText(pg.tit1);
            lbl_GTE_titulo2.setText(rs.getString("titulo2"));
            txt_GTE_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_GTE_Pregunta.addFocusListener(new FocusAdapter() { //Listener que se dispara cuando la casilla pierde focus
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("GTE");
                }
            });

            AsignarPopupBtn(btn_GTE_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 450, 50);
            // - - - - - - Pregunta 5 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - GCO
            rs = db.ResultadoSelect("datospregunta", "GCO");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("GCO", pg);

            lbl_GCO_titulo1.setText(pg.tit1);
            lbl_GCO_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_GCO_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 440, 360);
            // - - - - - - Pregunta 6 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - GCA
            rs = db.ResultadoSelect("datospregunta", "GCA");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("GCA", pg);

            lbl_GCA_titulo1.setText(pg.tit1);
            lbl_GCA_titulo2.setText(rs.getString("titulo2"));
            txt_GCA_Pregunta.setText(rs.getString("valorpordefecto"));
            lbl_GCA_desc.setText(pg.desc);

            txt_GCA_Pregunta.addFocusListener(new FocusAdapter() { //Listener que se dispara cuando la casilla pierde focus
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("GCA");
                }
            });

            AsignarPopupBtn(btn_GCA_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 490, 310);
            // - - - - - - Pregunta 7 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - GAS
            rs = db.ResultadoSelect("datospregunta", "GAS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("GAS", pg);

            lbl_GAS_titulo1.setText(pg.tit1);
            lbl_GAS_titulo2.setText(rs.getString("titulo2"));
            
            AsignarPopupBtn(btn_GAS_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 390, 260);
            // - - - - - - Pregunta 8 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - GTR
            rs = db.ResultadoSelect("datospregunta", "GTR");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("GTR", pg);

            lbl_GTR_titulo1.setText(pg.tit1);
            lbl_GTR_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_GTR_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 460, 420);
            // - - - - - - Pregunta 9 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - GED
            rs = db.ResultadoSelect("datospregunta", "GED");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("GED", pg);

            lbl_GED_titulo1.setText(pg.tit1);
            lbl_GED_titulo2.setText(rs.getString("titulo2"));
            
            AsignarPopupBtn(btn_GED_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 280);
            // - - - - - - Pregunta 10 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - GUV
            rs = db.ResultadoSelect("datospregunta", "GUV");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("GUV", pg);

            lbl_GUV_titulo1.setText(pg.tit1);
            lbl_GUV_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 11 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - GSA
            rs = db.ResultadoSelect("datospregunta", "GSA");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("GSA", pg);

            lbl_GSA_titulo1.setText(pg.tit1);
            lbl_GSA_titulo2.setText(rs.getString("titulo2"));
            lbl_GSA_desc.setText(pg.desc);

            AsignarPopupBtn(btn_GSA_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 380, 150);
            // - - - - - - Pregunta 12 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - GSL
            rs = db.ResultadoSelect("datospregunta", "GSL");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("GSL", pg);

            lbl_GSL_titulo1.setText(pg.tit1);
            lbl_GSL_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 13 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - GAP
            rs = db.ResultadoSelect("datospregunta", "GAP");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("GAP", pg);

            lbl_GAP_titulo1.setText(pg.tit1);
            lbl_GAP_titulo2.setText(rs.getString("titulo2"));
            txt_GAP_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_GAP_Pregunta.addFocusListener(new FocusAdapter() { //Listener que se dispara cuando la casilla pierde focus
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("GAP");
                }
            });

            AsignarPopupBtn(btn_GAP_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 310, 160);
            // - - - - - - Pregunta 14 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - GAI
            rs = db.ResultadoSelect("datospregunta", "GAI");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("GAI", pg);

            lbl_GAI_titulo1.setText(pg.tit1);
            lbl_GAI_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_GAI_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 250, 150);
            // - - - - - - Pregunta 15 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - GAB
            rs = db.ResultadoSelect("datospregunta", "GAB");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("GAB", pg);

            lbl_GAB_titulo1.setText(pg.tit1);
            lbl_GAB_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_GAB_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 350, 220);
            // - - - - - - Pregunta 15 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - GUA
            rs = db.ResultadoSelect("datospregunta", "GUA");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("GUA", pg);

            lbl_GUA_titulo1.setText(pg.tit1);
            lbl_GUA_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
            // Si Bod cargó datos de Laguna Facultativa secundaria desde la BD, porque Ya estaba editada, se proceden a llenar casillas con datos
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -          
            if (bod.EditLagunaFacultativaUASB) {

                txt_GTE_Pregunta.setText(bod.getVarFormateadaPorNombre("GTE", map.get("GTE").deci)); //Trae la variable por nombre y la formatea por cantidad de decimales
                txt_GCO_Pregunta.setText(bod.getVarFormateadaPorNombre("GCO", map.get("GCO").deci));
                txt_GCA_Pregunta.setText(bod.getVarFormateadaPorNombre("GCA", map.get("GCA").deci));
                txt_GAS_Pregunta.setText(bod.getVarFormateadaPorNombre("GAS", map.get("GAS").deci));
                txt_GTR_Pregunta.setText(bod.getVarFormateadaPorNombre("GTR", map.get("GTR").deci));
                txt_GED_Pregunta.setText(bod.getVarFormateadaPorNombre("GED", map.get("GED").deci));
                txt_GUV_Pregunta.setText(bod.getVarFormateadaPorNombre("GUV", map.get("GUV").deci));
                txt_GSA_Pregunta.setText(bod.getVarFormateadaPorNombre("GSA", map.get("GSA").deci));
                txt_GSL_Pregunta.setText(bod.getVarFormateadaPorNombre("GSL", map.get("GSL").deci));
                txt_GAP_Pregunta.setText(bod.getVarFormateadaPorNombre("GAP", map.get("GAP").deci));
                txt_GAI_Pregunta.setText(bod.getVarFormateadaPorNombre("GAI", map.get("GAI").deci));
                txt_GAB_Pregunta.setText(bod.getVarFormateadaPorNombre("GAB", map.get("GAB").deci));
                txt_GUA_Pregunta.setText(bod.getVarFormateadaPorNombre("GUA", map.get("GUA").deci));
            } else {// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -si no realiza otras tareas de cálculos que se deben hacer al cargar

                ejecutarFunciones("GTE");
                ejecutarFunciones("GCA");
                ejecutarFunciones("GAP");
                ejecutarFunciones("GAB");
                ejecutarFunciones("GUA");
                borradoMsgError();
            }
            ejecutarFunciones("GUA");
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
        lbl_GAB_error.setText("");
        lbl_GAI_error.setText("");
        lbl_GAP_error.setText("");
        lbl_GAS_error.setText("");
        lbl_GCA_error.setText("");
        lbl_GCO_error.setText("");
        lbl_GED_error.setText("");
        lbl_GSA_error.setText("");
        lbl_GSL_error.setText("");
        lbl_GTE_error.setText("");
        lbl_GTR_error.setText("");
        lbl_GUA_error.setText("");
        lbl_GUV_error.setText("");
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

        if (!CalcularGTE()) {
            lbl_save_error.setText(map.get("GTE").erro);
            lbl_save_desc.setText(map.get("GTE").desc);
            lbl_save_error.setText(map.get("GTE").tit1);
            return false;
        }

        if (!CalcularGCO()) {
            lbl_save_error.setText(map.get("GCO").erro);
            lbl_save_desc.setText(map.get("GCO").desc);
            lbl_save_titulo1.setText(map.get("GCO").tit1);
            return false;
        }

        if (!CalcularGCA()) {
            lbl_save_error.setText(map.get("GCA").erro);
            lbl_save_desc.setText(map.get("GCA").desc);
            lbl_save_titulo1.setText(map.get("GCA").tit1);
            return false;
        }

        if (!CalcularGAS()) {
            lbl_save_error.setText(map.get("GAS").erro);
            lbl_save_desc.setText(map.get("GAS").desc);
            lbl_save_titulo1.setText(map.get("GAS").tit1);
            return false;
        }

        if (!CalcularGTR()) {
            lbl_save_error.setText(map.get("GTR").erro);
            lbl_save_desc.setText(map.get("GTR").desc);
            lbl_save_titulo1.setText(map.get("GTR").tit1);
            return false;
        }

        if (!CalcularGED()) {
            lbl_save_error.setText(map.get("GED").erro);
            lbl_save_desc.setText(map.get("GED").desc);
            lbl_save_titulo1.setText(map.get("GED").tit1);
            return false;
        }

        if (!CalcularGUV()) {
            lbl_save_error.setText(map.get("GUV").erro);
            lbl_save_desc.setText(map.get("GUV").desc);
            lbl_save_titulo1.setText(map.get("GUV").tit1);
            return false;
        }

        if (!CalcularGSA()) {
            lbl_save_error.setText(map.get("GSA").erro);
            lbl_save_desc.setText(map.get("GSA").desc);
            lbl_save_titulo1.setText(map.get("GSA").tit1);
            return false;
        }

        if (!CalcularGSL()) {
            lbl_save_error.setText(map.get("GSL").erro);
            lbl_save_desc.setText(map.get("GSL").desc);
            lbl_save_titulo1.setText(map.get("GSL").tit1);
            return false;
        }

        if (!CalcularGAP()) {
            lbl_save_error.setText(map.get("GAP").erro);
            lbl_save_desc.setText(map.get("GAP").desc);
            lbl_save_titulo1.setText(map.get("GAP").tit1);
            return false;
        }

        if (!CalcularGAI()) {
            lbl_save_error.setText(map.get("GAI").erro);
            lbl_save_desc.setText(map.get("GAI").desc);
            lbl_save_titulo1.setText(map.get("GAI").tit1);
            return false;
        }

        if (!CalcularGAB()) {
            lbl_save_error.setText(map.get("GAB").erro);
            lbl_save_desc.setText(map.get("GAB").desc);
            lbl_save_titulo1.setText(map.get("GAB").tit1);
            return false;
        }

        if (!CalcularGUA()) {
            lbl_save_error.setText(map.get("GUA").erro);
            lbl_save_desc.setText(map.get("GUA").desc);
            lbl_save_titulo1.setText(map.get("GUA").tit1);
            return false;
        }

        Main main = (Main) this.getTopLevelAncestor();
        main.cancela = false;
        main.vbod = this.bod;
        bod.EditLagunaFacultativaUASB = true;

        if (!bod.exportarProyecto(true)) {
            bod.EditLagunaFacultativaUASB = false;
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

    private boolean CalcularGTE() {

        try {
            lbl_GTE_error.setText("");

            if (bod.setVarDob(txt_GTE_Pregunta.getText(), "GTE", map.get("GTE").rmin, map.get("GTE").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularGTE " + ex.getMessage());
        }
        lbl_GTE_error.setText(map.get("GTE").erro);
        return false;
    }

    private boolean CalcularGCO() {

        try {
            lbl_GCO_error.setText("");
            double x = bod.calcularGCO();

            if (bod.setVarDob(x, "GCO", map.get("GCO").rmin, map.get("GCO").rmax)) {
                txt_GCO_Pregunta.setText(bod.getVarFormateadaPorNombre("GCO", map.get("GCO").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularGCO " + ex.getMessage());
        }
        lbl_GCO_error.setText(map.get("GCO").erro);
        return false;
    }

    private boolean CalcularGCA() {

        try {
            lbl_GCA_error.setText("");

            if (bod.setVarDob(txt_GCA_Pregunta.getText(), "GCA", map.get("GCA").rmin, map.get("GCA").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularGCA " + ex.getMessage());
        }
        lbl_GCA_error.setText(map.get("GCA").erro);
        return false;
    }

    private boolean CalcularGAS() {

        try {
            lbl_GAS_error.setText("");
            double x = bod.calcularGAS();

            if (bod.setVarDob(x, "GAS", map.get("GAS").rmin, map.get("GAS").rmax)) {
                txt_GAS_Pregunta.setText(bod.getVarFormateadaPorNombre("GAS", map.get("GAS").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularGAS " + ex.getMessage());
        }
        lbl_GAS_error.setText(map.get("GAS").erro);
        return false;
    }

    private boolean CalcularGTR() {

        try {
            lbl_GTR_error.setText("");
            double x = bod.calcularGTR();

            if (bod.setVarDob(x, "GTR", map.get("GTR").rmin, map.get("GTR").rmax)) {
                txt_GTR_Pregunta.setText(bod.getVarFormateadaPorNombre("GTR", map.get("GTR").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularGTR " + ex.getMessage());
        }
        lbl_GTR_error.setText(map.get("GTR").erro);
        return false;
    }

    private boolean CalcularGED() {

        try {
            lbl_GED_error.setText("");
            double x = bod.calcularGED();

            if (bod.setVarDob(x, "GED", map.get("GED").rmin, map.get("GED").rmax)) {
                txt_GED_Pregunta.setText(bod.getVarFormateadaPorNombre("GED", map.get("GED").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularGED " + ex.getMessage());
        }
        lbl_GED_error.setText(map.get("GED").erro);
        return false;
    }

    private boolean CalcularGUV() {

        try {
            lbl_GUV_error.setText("");
            double x = bod.calcularGUV();

            if (bod.setVarDob(x, "GUV", map.get("GUV").rmin, map.get("GUV").rmax)) {
                txt_GUV_Pregunta.setText(bod.getVarFormateadaPorNombre("GUV", map.get("GUV").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularGUV " + ex.getMessage());
        }
        lbl_GUV_error.setText(map.get("GUV").erro);
        return false;
    }

    private boolean CalcularGSA() {

        try {
            lbl_GSA_error.setText("");
            double x = bod.calcularGSA();

            if (bod.setVarDob(x, "GSA", map.get("GSA").rmin, map.get("GSA").rmax)) {
                txt_GSA_Pregunta.setText(bod.getVarFormateadaPorNombre("GSA", map.get("GSA").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularGSA " + ex.getMessage());
        }
        lbl_GSA_error.setText(map.get("GSA").erro);
        return false;
    }

    private boolean CalcularGSL() {

        try {
            lbl_GSL_error.setText("");
            double x = bod.calcularGSL();

            if (bod.setVarDob(x, "GSL", map.get("GSL").rmin, map.get("GSL").rmax)) {
                txt_GSL_Pregunta.setText(bod.getVarFormateadaPorNombre("GSL", map.get("GSL").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularGSL " + ex.getMessage());
        }
        lbl_GSL_error.setText(map.get("GSL").erro);
        return false;
    }

    private boolean CalcularGAP() {

        try {
            lbl_GAP_error.setText("");

            if (bod.setVarInt(txt_GAP_Pregunta.getText(), "GAP", (int) map.get("GAP").rmin, (int) map.get("GAP").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularGAP " + ex.getMessage());
        }
        lbl_GAP_error.setText(map.get("GAP").erro);
        return false;
    }

    private boolean CalcularGAI() {

        try {
            lbl_GAI_error.setText("");
            double x = bod.calcularGAI();

            if (bod.setVarDob(x, "GAI", map.get("GAI").rmin, map.get("GAI").rmax)) {
                txt_GAI_Pregunta.setText(bod.getVarFormateadaPorNombre("GAI", map.get("GAI").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularGAI " + ex.getMessage());
        }
        lbl_GAI_error.setText(map.get("GAI").erro);
        return false;
    }

    private boolean CalcularGAB() {

        try {
            lbl_GAB_error.setText("");
            String x = bod.calcularGAB();

            if (bod.setVarDob(x, "GAB", map.get("GAB").rmin, map.get("GAB").rmax)) {
                txt_GAB_Pregunta.setText(bod.getVarFormateadaPorNombre("GAB", map.get("GAB").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularGAB " + ex.getMessage());
        }
        lbl_GAB_error.setText(map.get("GAB").erro);
        return false;
    }

    private boolean CalcularGUA() {

        try {
            lbl_GUA_error.setText("");
            double x = bod.calcularGUA();

            if (bod.setVarDob(x, "GUA", map.get("GUA").rmin, map.get("GUA").rmax)) {
                txt_GUA_Pregunta.setText(bod.getVarFormateadaPorNombre("GUA", map.get("GUA").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularGUA " + ex.getMessage());
        }
        lbl_GUA_error.setText(map.get("GUA").erro);
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
            case "GTE":
                CalcularGTE();
                ejecutarFunciones("GCO");
                break;

            case "GCO":
                CalcularGCO();
                ejecutarFunciones("GAS");
               // ejecutarFunciones("GUV");
                break;

            case "GCA":
                CalcularGCA();
                ejecutarFunciones("GTR");
                ejecutarFunciones("GUV");
                break;

            case "GAS":
                CalcularGAS();
                ejecutarFunciones("GTR");
                ejecutarFunciones("GSA");
                ejecutarFunciones("GAB");
                ejecutarFunciones("GUV");
                break;

            case "GTR":
                CalcularGTR();
                ejecutarFunciones("GED");
                break;

            case "GED":
                CalcularGED();
                break;

            case "GUV":
                CalcularGUV();
                break;

            case "GSA":
                CalcularGSA();
                ejecutarFunciones("GSL");
                break;

            case "GSL":
                CalcularGSL();
                break;

            case "GAP":
                CalcularGAP();
                ejecutarFunciones("GAI");
                break;

            case "GAI":
                CalcularGAI();
                break;
            case "GAB":
                CalcularGAB();
                break;

            case "GUA":
                CalcularGUA();
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
        bod.WinLagunaFacultativaUASB = false;
        Main main = (Main) this.getTopLevelAncestor();
        main.bod.WinLagunaFacultativaUASB = false;
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jsp_LagunaFacultativasecUasb = new javax.swing.JScrollPane();
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
        txt_GTE_Pregunta = new javax.swing.JTextField();
        lbl_GTE_titulo1 = new javax.swing.JLabel();
        lbl_GTE_error = new javax.swing.JLabel();
        lbl_GTE_titulo2 = new javax.swing.JLabel();
        lbl_GCA_error = new javax.swing.JLabel();
        lbl_GCA_titulo2 = new javax.swing.JLabel();
        txt_GCA_Pregunta = new javax.swing.JTextField();
        lbl_GCA_titulo1 = new javax.swing.JLabel();
        lbl_GCA_desc = new javax.swing.JLabel();
        lbl_GAS_error = new javax.swing.JLabel();
        lbl_GAS_titulo2 = new javax.swing.JLabel();
        txt_GAS_Pregunta = new javax.swing.JTextField();
        lbl_GAS_titulo1 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        txt_GED_Pregunta = new javax.swing.JTextField();
        lbl_GED_titulo1 = new javax.swing.JLabel();
        lbl_GUV_error = new javax.swing.JLabel();
        lbl_GUV_titulo2 = new javax.swing.JLabel();
        lbl_GED_error = new javax.swing.JLabel();
        txt_GUV_Pregunta = new javax.swing.JTextField();
        lbl_GUV_titulo1 = new javax.swing.JLabel();
        lbl_GED_titulo2 = new javax.swing.JLabel();
        lbl_GTR_titulo1 = new javax.swing.JLabel();
        txt_GTR_Pregunta = new javax.swing.JTextField();
        lbl_GTR_titulo2 = new javax.swing.JLabel();
        lbl_GTR_error = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        lbl_GSA_desc = new javax.swing.JLabel();
        lbl_GSA_titulo1 = new javax.swing.JLabel();
        lbl_GSA_error = new javax.swing.JLabel();
        lbl_GSA_titulo2 = new javax.swing.JLabel();
        txt_GSA_Pregunta = new javax.swing.JTextField();
        lbl_GSL_titulo1 = new javax.swing.JLabel();
        lbl_GSL_error = new javax.swing.JLabel();
        lbl_GSL_titulo2 = new javax.swing.JLabel();
        txt_GSL_Pregunta = new javax.swing.JTextField();
        lbl_GCO_titulo1 = new javax.swing.JLabel();
        txt_GCO_Pregunta = new javax.swing.JTextField();
        lbl_GCO_titulo2 = new javax.swing.JLabel();
        lbl_GCO_error = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        txt_GAB_Pregunta = new javax.swing.JTextField();
        lbl_GAB_error = new javax.swing.JLabel();
        lbl_GAB_titulo1 = new javax.swing.JLabel();
        lbl_GAP_titulo2 = new javax.swing.JLabel();
        lbl_GAB_titulo2 = new javax.swing.JLabel();
        txt_GAI_Pregunta = new javax.swing.JTextField();
        lbl_GAP_titulo1 = new javax.swing.JLabel();
        lbl_GAI_error = new javax.swing.JLabel();
        lbl_GAI_titulo2 = new javax.swing.JLabel();
        txt_GAP_Pregunta = new javax.swing.JTextField();
        lbl_GAI_titulo1 = new javax.swing.JLabel();
        lbl_GAP_error = new javax.swing.JLabel();
        btn_GTE_ayuda = new javax.swing.JButton();
        btn_GAB_ayuda = new javax.swing.JButton();
        btn_GCO_ayuda = new javax.swing.JButton();
        btn_GCA_ayuda = new javax.swing.JButton();
        btn_GTR_ayuda = new javax.swing.JButton();
        btn_GSA_ayuda = new javax.swing.JButton();
        btn_GAP_ayuda = new javax.swing.JButton();
        btn_GAI_ayuda = new javax.swing.JButton();
        lbl_save_titulo1 = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        lbl_GUA_titulo2 = new javax.swing.JLabel();
        lbl_GUA_titulo1 = new javax.swing.JLabel();
        txt_GUA_Pregunta = new javax.swing.JTextField();
        lbl_GUA_error = new javax.swing.JLabel();
        btn_GAS_ayuda = new javax.swing.JButton();
        btn_GED_ayuda = new javax.swing.JButton();
        btn_close2 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1024, 768));

        jsp_LagunaFacultativasecUasb.setPreferredSize(new java.awt.Dimension(1024, 768));

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

        txt_GTE_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_GTE_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 170, 130, 25));

        lbl_GTE_titulo1.setText("Titulo");
        lbl_GTE_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GTE_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 300, 30));

        lbl_GTE_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_GTE_error.setText(" . . .");
        jp_Componentes.add(lbl_GTE_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 170, 340, 35));

        lbl_GTE_titulo2.setText("Titulo");
        lbl_GTE_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GTE_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 170, 80, 25));

        lbl_GCA_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_GCA_error.setText(" . . .");
        jp_Componentes.add(lbl_GCA_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 290, 340, 35));

        lbl_GCA_titulo2.setText("Titulo");
        lbl_GCA_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GCA_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 300, 80, 25));

        txt_GCA_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_GCA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 300, 130, 25));

        lbl_GCA_titulo1.setText("Titulo");
        lbl_GCA_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GCA_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 300, 30));

        lbl_GCA_desc.setText("Desc");
        lbl_GCA_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GCA_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 600, 30));

        lbl_GAS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_GAS_error.setText(" . . .");
        jp_Componentes.add(lbl_GAS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 330, 340, 35));

        lbl_GAS_titulo2.setText("Titulo");
        lbl_GAS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GAS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 340, 80, 25));

        txt_GAS_Pregunta.setEditable(false);
        jp_Componentes.add(txt_GAS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 340, 130, 25));

        lbl_GAS_titulo1.setText("Titulo");
        lbl_GAS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GAS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 300, 30));
        jp_Componentes.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 382, 1014, 2));

        txt_GED_Pregunta.setEditable(false);
        jp_Componentes.add(txt_GED_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 440, 130, 25));

        lbl_GED_titulo1.setText("Titulo");
        lbl_GED_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GED_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, 300, 30));

        lbl_GUV_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_GUV_error.setText(" . . .");
        jp_Componentes.add(lbl_GUV_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 470, 340, 35));

        lbl_GUV_titulo2.setText("Titulo");
        lbl_GUV_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GUV_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 480, 80, 25));

        lbl_GED_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_GED_error.setText(" . . .");
        jp_Componentes.add(lbl_GED_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 430, 340, 35));

        txt_GUV_Pregunta.setEditable(false);
        jp_Componentes.add(txt_GUV_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 480, 130, 25));

        lbl_GUV_titulo1.setText("Titulo");
        lbl_GUV_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GUV_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 480, 300, 30));

        lbl_GED_titulo2.setText("Titulo");
        lbl_GED_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GED_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 440, 80, 25));

        lbl_GTR_titulo1.setText("Titulo");
        lbl_GTR_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GTR_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 300, 30));

        txt_GTR_Pregunta.setEditable(false);
        jp_Componentes.add(txt_GTR_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 400, 130, 25));

        lbl_GTR_titulo2.setText("Titulo");
        lbl_GTR_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GTR_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 400, 80, 25));

        lbl_GTR_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_GTR_error.setText(" . . .");
        jp_Componentes.add(lbl_GTR_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 390, 340, 35));
        jp_Componentes.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, 650, 1110, 2));

        lbl_GSA_desc.setText("Desc");
        lbl_GSA_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GSA_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 600, 30));

        lbl_GSA_titulo1.setText("Titulo");
        lbl_GSA_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GSA_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 570, 300, 30));

        lbl_GSA_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_GSA_error.setText(" . . .");
        jp_Componentes.add(lbl_GSA_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 560, 340, 35));

        lbl_GSA_titulo2.setText("Titulo");
        lbl_GSA_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GSA_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 570, 80, 25));

        txt_GSA_Pregunta.setEditable(false);
        jp_Componentes.add(txt_GSA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 570, 130, 25));

        lbl_GSL_titulo1.setText("Titulo");
        lbl_GSL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GSL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 610, 300, 30));

        lbl_GSL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_GSL_error.setText(" . . .");
        jp_Componentes.add(lbl_GSL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 600, 340, 35));

        lbl_GSL_titulo2.setText("Titulo");
        lbl_GSL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GSL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 610, 80, 25));

        txt_GSL_Pregunta.setEditable(false);
        jp_Componentes.add(txt_GSL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 610, 130, 25));

        lbl_GCO_titulo1.setText("Titulo");
        lbl_GCO_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GCO_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 300, 30));

        txt_GCO_Pregunta.setEditable(false);
        jp_Componentes.add(txt_GCO_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 210, 130, 25));

        lbl_GCO_titulo2.setText("Titulo");
        lbl_GCO_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GCO_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 210, 80, 25));

        lbl_GCO_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_GCO_error.setText(" . . .");
        jp_Componentes.add(lbl_GCO_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 210, 340, 35));
        jp_Componentes.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 790, 1084, 2));

        txt_GAB_Pregunta.setEditable(false);
        jp_Componentes.add(txt_GAB_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 750, 130, 25));

        lbl_GAB_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_GAB_error.setText(" . . .");
        jp_Componentes.add(lbl_GAB_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 740, 340, 35));

        lbl_GAB_titulo1.setText("Titulo");
        lbl_GAB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GAB_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 740, 300, 30));

        lbl_GAP_titulo2.setText("Titulo");
        lbl_GAP_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GAP_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 670, 80, 25));

        lbl_GAB_titulo2.setText("Titulo");
        lbl_GAB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GAB_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 750, 80, 25));

        txt_GAI_Pregunta.setEditable(false);
        jp_Componentes.add(txt_GAI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 710, 130, 25));

        lbl_GAP_titulo1.setText("Titulo");
        lbl_GAP_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GAP_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 660, 300, 30));

        lbl_GAI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_GAI_error.setText(" . . .");
        jp_Componentes.add(lbl_GAI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 700, 340, 35));

        lbl_GAI_titulo2.setText("Titulo");
        lbl_GAI_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GAI_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 710, 80, 25));

        txt_GAP_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_GAP_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 670, 130, 25));

        lbl_GAI_titulo1.setText("Titulo");
        lbl_GAI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GAI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 700, 300, 30));

        lbl_GAP_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_GAP_error.setText(" . . .");
        jp_Componentes.add(lbl_GAP_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 660, 340, 35));

        btn_GTE_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_GTE_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_GTE_ayuda.setContentAreaFilled(false);
        btn_GTE_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_GTE_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 170, 25, 25));

        btn_GAB_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_GAB_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_GAB_ayuda.setContentAreaFilled(false);
        btn_GAB_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_GAB_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 750, 25, 25));

        btn_GCO_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_GCO_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_GCO_ayuda.setContentAreaFilled(false);
        btn_GCO_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_GCO_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 210, 25, 25));

        btn_GCA_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_GCA_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_GCA_ayuda.setContentAreaFilled(false);
        btn_GCA_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_GCA_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 300, 25, 25));

        btn_GTR_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_GTR_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_GTR_ayuda.setContentAreaFilled(false);
        btn_GTR_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_GTR_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 400, 25, 25));

        btn_GSA_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_GSA_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_GSA_ayuda.setContentAreaFilled(false);
        btn_GSA_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_GSA_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 530, 25, 25));

        btn_GAP_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_GAP_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_GAP_ayuda.setContentAreaFilled(false);
        btn_GAP_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_GAP_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 670, 25, 25));

        btn_GAI_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_GAI_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_GAI_ayuda.setContentAreaFilled(false);
        btn_GAI_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_GAI_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 710, 25, 25));

        lbl_save_titulo1.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_titulo1.setText(" . . .");
        jp_Componentes.add(lbl_save_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 860, 300, 35));
        jp_Componentes.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 850, 1110, 10));

        lbl_GUA_titulo2.setText("Titulo");
        lbl_GUA_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GUA_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 810, 80, 25));

        lbl_GUA_titulo1.setText("Titulo");
        lbl_GUA_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_GUA_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 800, 300, 30));

        txt_GUA_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_GUA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 810, 130, 25));

        lbl_GUA_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_GUA_error.setText(" . . .");
        jp_Componentes.add(lbl_GUA_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 800, 340, 35));

        btn_GAS_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_GAS_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_GAS_ayuda.setContentAreaFilled(false);
        btn_GAS_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_GAS_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 340, 20, 25));

        btn_GED_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_GED_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_GED_ayuda.setContentAreaFilled(false);
        btn_GED_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_GED_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 440, 25, 25));

        jsp_LagunaFacultativasecUasb.setViewportView(jp_Componentes);

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
            .addComponent(jsp_LagunaFacultativasecUasb, javax.swing.GroupLayout.PREFERRED_SIZE, 1008, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_close2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btn_close2)
                .addGap(2, 2, 2)
                .addComponent(jsp_LagunaFacultativasecUasb, javax.swing.GroupLayout.DEFAULT_SIZE, 953, Short.MAX_VALUE)
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
    private javax.swing.JButton btn_GAB_ayuda;
    private javax.swing.JButton btn_GAI_ayuda;
    private javax.swing.JButton btn_GAP_ayuda;
    private javax.swing.JButton btn_GAS_ayuda;
    private javax.swing.JButton btn_GCA_ayuda;
    private javax.swing.JButton btn_GCO_ayuda;
    private javax.swing.JButton btn_GED_ayuda;
    private javax.swing.JButton btn_GSA_ayuda;
    private javax.swing.JButton btn_GTE_ayuda;
    private javax.swing.JButton btn_GTR_ayuda;
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
    private javax.swing.JScrollPane jsp_LagunaFacultativasecUasb;
    private javax.swing.JLabel lbl_CAT_error;
    private javax.swing.JLabel lbl_CAT_titulo1;
    private javax.swing.JLabel lbl_CAT_titulo2;
    private javax.swing.JLabel lbl_GAB_error;
    private javax.swing.JLabel lbl_GAB_titulo1;
    private javax.swing.JLabel lbl_GAB_titulo2;
    private javax.swing.JLabel lbl_GAI_error;
    private javax.swing.JLabel lbl_GAI_titulo1;
    private javax.swing.JLabel lbl_GAI_titulo2;
    private javax.swing.JLabel lbl_GAP_error;
    private javax.swing.JLabel lbl_GAP_titulo1;
    private javax.swing.JLabel lbl_GAP_titulo2;
    private javax.swing.JLabel lbl_GAS_error;
    private javax.swing.JLabel lbl_GAS_titulo1;
    private javax.swing.JLabel lbl_GAS_titulo2;
    private javax.swing.JLabel lbl_GCA_desc;
    private javax.swing.JLabel lbl_GCA_error;
    private javax.swing.JLabel lbl_GCA_titulo1;
    private javax.swing.JLabel lbl_GCA_titulo2;
    private javax.swing.JLabel lbl_GCO_error;
    private javax.swing.JLabel lbl_GCO_titulo1;
    private javax.swing.JLabel lbl_GCO_titulo2;
    private javax.swing.JLabel lbl_GED_error;
    private javax.swing.JLabel lbl_GED_titulo1;
    private javax.swing.JLabel lbl_GED_titulo2;
    private javax.swing.JLabel lbl_GSA_desc;
    private javax.swing.JLabel lbl_GSA_error;
    private javax.swing.JLabel lbl_GSA_titulo1;
    private javax.swing.JLabel lbl_GSA_titulo2;
    private javax.swing.JLabel lbl_GSL_error;
    private javax.swing.JLabel lbl_GSL_titulo1;
    private javax.swing.JLabel lbl_GSL_titulo2;
    private javax.swing.JLabel lbl_GTE_error;
    private javax.swing.JLabel lbl_GTE_titulo1;
    private javax.swing.JLabel lbl_GTE_titulo2;
    private javax.swing.JLabel lbl_GTR_error;
    private javax.swing.JLabel lbl_GTR_titulo1;
    private javax.swing.JLabel lbl_GTR_titulo2;
    private javax.swing.JLabel lbl_GUA_error;
    private javax.swing.JLabel lbl_GUA_titulo1;
    private javax.swing.JLabel lbl_GUA_titulo2;
    private javax.swing.JLabel lbl_GUV_error;
    private javax.swing.JLabel lbl_GUV_titulo1;
    private javax.swing.JLabel lbl_GUV_titulo2;
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
    private javax.swing.JTextField txt_GAB_Pregunta;
    private javax.swing.JTextField txt_GAI_Pregunta;
    private javax.swing.JTextField txt_GAP_Pregunta;
    private javax.swing.JTextField txt_GAS_Pregunta;
    private javax.swing.JTextField txt_GCA_Pregunta;
    private javax.swing.JTextField txt_GCO_Pregunta;
    private javax.swing.JTextField txt_GED_Pregunta;
    private javax.swing.JTextField txt_GSA_Pregunta;
    private javax.swing.JTextField txt_GSL_Pregunta;
    private javax.swing.JTextField txt_GTE_Pregunta;
    private javax.swing.JTextField txt_GTR_Pregunta;
    private javax.swing.JTextField txt_GUA_Pregunta;
    private javax.swing.JTextField txt_GUV_Pregunta;
    private javax.swing.JTextField txt_LAE_Pregunta;
    private javax.swing.JTextField txt_Q2C_Pregunta;
    // End of variables declaration//GEN-END:variables
}