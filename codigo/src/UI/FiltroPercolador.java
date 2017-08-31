package UI;

import BO.Bod;
import Componentes.Pregunta;
import DB.Dao;
import java.io.File;
import Componentes.Util;
import org.apache.log4j.Logger;
import Componentes.Configuracion;
import Componentes.Listener_Popup;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.event.PopupMenuListener;

public class FiltroPercolador extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("Filtro Percolador"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.ObtenerKey("rutabasedatos") + File.separator + conf.ObtenerKey("nombrebasedatos"));
    public Map<String, Pregunta> map = new TreeMap<>();
    PopupMenuListener listener;
    private Bod bod = new Bod("");
    private Listener_Popup Lpopup;
    Util util = new Util();
    Pregunta pg;

    public FiltroPercolador(Bod bod) {
        this.bod = bod;
        initComponents();
        bod.WinFiltroPercolador = true;
        InicializarComponentes();
    }

    /**
     * Inicializa todos los elementos (jlabels,jtextbox,etc.) con sus
     * respectivos valores y textos desde la base de datos.
     */
    public void InicializarComponentes() {

        ResultSet rs;
        bod.WinFiltroPercolador= true;//Bandera La ventana se ha abierto
        btn_guardar.setText("Guardar");
        btn_close.setText("Cerrar");
        String p;
        try {

            // - - - - - - Cargar el titulo de la sección  
            rs = db.ResultadoSelect("obtenerseccion", "11");

            lbl_titulo1.setText(rs.getString("Nombre"));
            lbl_titulo2.setText(rs.getString("Mensaje"));
            // - - - - - - Dato 1 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q2C
            rs = db.ResultadoSelect("datospregunta", "Q2C");

            String[] titulo2 = rs.getString("titulo2").split("\\|"); //Q2C en el titulo 2 tiene 2 posibles textos

            lbl_Q2C_titulo1.setText(rs.getString("titulo1"));
            lbl_Q2C_titulo2.setText(titulo2[1].trim());
            txt_Q2C_Pregunta.setText(bod.getVarFormateada(bod.calcularQ2Cm3(), rs.getInt("decimales"))); // Q2C en m³/dia
            // - - - - - - Dato 2 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q3C
            rs = db.ResultadoSelect("datospregunta", "Q3C");

            String[] titulo3 = rs.getString("titulo2").split("\\|"); //Q3C en el titulo 2 tiene 2 posibles textos

            lbl_Q3C_titulo1.setText(rs.getString("titulo1"));
            lbl_Q3C_titulo2.setText(titulo3[1].trim());
            txt_Q3C_Pregunta.setText(bod.getVarFormateada(bod.calcularQ3Cm3(), rs.getInt("decimales"))); // Q3C en m³/dia
            // - - - - - - Dato 3 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UEC
            rs = db.ResultadoSelect("datospregunta", "UEC");

            lbl_UEC_titulo1.setText(rs.getString("titulo1"));
            lbl_UEC_titulo2.setText(rs.getString("titulo2"));
            txt_UEC_Pregunta.setText(bod.getVarFormateadaPorNombre("UEC", rs.getInt("decimales")));
            // - - - - - - Pregunta 01 Cuestionario 11 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PCO
            rs = db.ResultadoSelect("datospregunta", "PCO");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PCO", pg);

            lbl_PCO_titulo1.setText(pg.tit1);
            lbl_PCO_titulo2.setText(rs.getString("titulo2"));
            txt_PCO_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_PCO_Pregunta.addFocusListener(new FocusAdapter() { //Listener que se dispara cuando la casilla pierde focus
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("PCO");
                }
            });
            AsignarPopupBtn(btn_PCO_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 440, 330);
            // - - - - - - Pregunta 02 Cuestionario 11 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PVM
            rs = db.ResultadoSelect("datospregunta", "PVM");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PVM", pg);

            lbl_PVM_titulo1.setText(pg.tit1);
            lbl_PVM_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_PVM_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 430, 320);
            // - - - - - - Pregunta 03 Cuestionario 11 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PPM
            rs = db.ResultadoSelect("datospregunta", "PPM");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PPM", pg);

            lbl_PPM_titulo1.setText(pg.tit1);
            lbl_PPM_titulo2.setText(rs.getString("titulo2"));
            txt_PPM_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_PPM_Pregunta.addFocusListener(new FocusAdapter() { //Listener que se dispara cuando la casilla pierde focus
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("PPM");
                }
            });
            AsignarPopupBtn(btn_PPM_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 380, 200);
            // - - - - - - Pregunta 04 Cuestionario 11 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PAS
            rs = db.ResultadoSelect("datospregunta", "PAS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PAS", pg);

            lbl_PAS_titulo.setText(pg.tit1);
            lbl_PAS_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 05 Cuestionario 11 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PH2
            rs = db.ResultadoSelect("datospregunta", "PH2");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PH2", pg);

            lbl_PH2_titulo1.setText(pg.tit1);
            lbl_PH2_titulo2.setText(rs.getString("titulo2"));
            lbl_PH2_desc.setText(pg.desc);
            // - - - - - - Pregunta 06 Cuestionario 11 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PH3
            rs = db.ResultadoSelect("datospregunta", "PH3");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PH3", pg);

            lbl_PH3_titulo1.setText(pg.tit1);
            lbl_PH3_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_PH3_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 440, 400);
            // - - - - - - Pregunta 07 Cuestionario 11 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PFU
            rs = db.ResultadoSelect("datospregunta", "PFU");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PFU", pg);

            lbl_PFU_titulo1.setText(pg.tit1);
            lbl_PFU_titulo2.setText(rs.getString("titulo2"));
            txt_PFU_Pregunta.setText(rs.getString("valorpordefecto"));
            lbl_PFU_desc.setText(pg.desc);

            txt_PFU_Pregunta.addFocusListener(new FocusAdapter() { //Listener que se dispara cuando la casilla pierde focus
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("PFU");
                }
            });
            // - - - - - - Pregunta 08 Cuestionario 11 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PFA
            rs = db.ResultadoSelect("datospregunta", "PFA");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PFA", pg);

            lbl_PFA_titulo1.setText(pg.tit1);
            lbl_PFA_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 09 Cuestionario 11 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PFD
            rs = db.ResultadoSelect("datospregunta", "PFD");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PFD", pg);

            lbl_PFD_titulo1.setText(pg.tit1);
            lbl_PFD_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 10 Cuestionario 11 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PER
            rs = db.ResultadoSelect("datospregunta", "PER");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PER", pg);

            lbl_PER_titulo1.setText(pg.tit1);
            lbl_PER_titulo2.setText(rs.getString("titulo2"));
            txt_PER_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_PER_Pregunta.addFocusListener(new FocusAdapter() { //Listener que se dispara cuando la casilla pierde focus
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("PER");
                }
            });
            AsignarPopupBtn(btn_PER_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 180);
            // - - - - - - Pregunta 11 Cuestionario 11 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PCD
            rs = db.ResultadoSelect("datospregunta", "PCD");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PCD", pg);

            lbl_PCD_titulo1.setText(pg.tit1);
            lbl_PCD_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 12 Cuestionario 11 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PEL
            rs = db.ResultadoSelect("datospregunta", "PEL");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PEL", pg);

            lbl_PEL_titulo1.setText(pg.tit1);
            lbl_PEL_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_PEL_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 460, 480);
            // - - - - - - Pregunta 13 Cuestionario 11 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PVL
            rs = db.ResultadoSelect("datospregunta", "PVL");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PVL", pg);

            lbl_PVL_titulo1.setText(pg.tit1);
            lbl_PVL_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 14 Cuestionario 11 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PSS
            rs = db.ResultadoSelect("datospregunta", "PSS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PSS", pg);

            lbl_PSS_titulo1.setText(pg.tit1);
            lbl_PSS_titulo2.setText(rs.getString("titulo2"));
            txt_PSS_Pregunta.setText(rs.getString("valorpordefecto"));
            lbl_PSS_desc.setText(pg.desc);

            txt_PSS_Pregunta.addFocusListener(new FocusAdapter() { //Listener que se dispara cuando la casilla pierde focus
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("PSS");
                }
            });
            AsignarPopupBtn(btn_PSS_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 360, 200);
            // - - - - - - Pregunta 15 Cuestionario 11 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PSA
            rs = db.ResultadoSelect("datospregunta", "PSA");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PSA", pg);

            lbl_PSA_titulo1.setText(pg.tit1);
            lbl_PSA_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 16 Cuestionario 11 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PPS
            rs = db.ResultadoSelect("datospregunta", "PPS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PPS", pg);

            lbl_PPS_titulo1.setText(pg.tit1);
            lbl_PPS_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_PPS_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 360);
            // - - - - - - Pregunta 17 Cuestionario 11 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PNU
            rs = db.ResultadoSelect("datospregunta", "PNU");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PNU", pg);

            lbl_PNU_titulo1.setText(pg.tit1);
            lbl_PNU_titulo2.setText(rs.getString("titulo2"));
            txt_PNU_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_PNU_Pregunta.addFocusListener(new FocusAdapter() { //Listener que se dispara cuando la casilla pierde focus
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("PNU");
                }
            });
            // - - - - - - Pregunta 18 Cuestionario 11 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PAX
            rs = db.ResultadoSelect("datospregunta", "PAX");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PAX", pg);

            lbl_PAX_titulo1.setText(pg.tit1);
            lbl_PAX_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 19 Cuestionario 11 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PDX
            rs = db.ResultadoSelect("datospregunta", "PDX");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PDX", pg);

            lbl_PDX_titulo1.setText(pg.tit1);
            lbl_PDX_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 20 Cuestionario 11 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PAT
            rs = db.ResultadoSelect("datospregunta", "PAT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PAT", pg);

            lbl_PAT_titulo1.setText(pg.tit1);
            lbl_PAT_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - Si ya han sido llenado correctamente estos datos se obtienen de Bod
            if (bod.EditFiltroPercolador) {
                txt_PCO_Pregunta.setText(bod.getVarFormateadaPorNombre("UDQ", map.get("UDQ").deci)); //Trae la variable por nombre y la formatea por cantidad de decimales

//ALTRAER DATOS LOS METODOS SOBRESSCRIBEN LOS DATOS???
            } else {
                ejecutarFunciones("PCO");
                ejecutarFunciones("PPM");
                ejecutarFunciones("PFU");
                ejecutarFunciones("PER");
                ejecutarFunciones("PSS");
                ejecutarFunciones("PNU");
            }

        } catch (Exception ex) {
            log.error("Error en InicializarComponentes " + ex.getMessage()   + " " + ex.getCause()  );
            ErrorGeneral(); // Ha ocurrido algun error en los datos o conexión, se debe revisar log.
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }

    /**
     * Deshabilita todos los componentes y muestra una advertencia al usuario
     */
    private void ErrorGeneral() {
        util.Mensaje("Ha ocurrido un error, revise el log de errores \n cierre la ventana", "error");

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
        lbl_save_error2.setText("");

        if (!CalcularPCO()) {
            lbl_save_error.setText(map.get("PCO").erro);
            lbl_save_error2.setText(map.get("PCO").tit1 + " . " + map.get("PCO").desc);
            return false;
        }

        if (!CalcularPVM()) {
            lbl_save_error.setText(map.get("PVM").erro);
            lbl_save_error2.setText(map.get("PVM").tit1 + " . " + map.get("PVM").desc);
            return false;
        }

        if (!CalcularPPM()) {
            lbl_save_error.setText(map.get("PPM").erro);
            lbl_save_error2.setText(map.get("PPM").tit1 + " . " + map.get("PPM").desc);
            return false;
        }

        if (!CalcularPAS()) {
            lbl_save_error.setText(map.get("PAS").erro);
            lbl_save_error2.setText(map.get("PAS").tit1 + " . " + map.get("PAS").desc);
            return false;
        }

        if (!CalcularPH2()) {
            lbl_save_error.setText(map.get("PH2").erro);
            lbl_save_error2.setText(map.get("PH2").tit1 + " . " + map.get("PH2").desc);
            return false;
        }

        if (!CalcularPH3()) {
            lbl_save_error.setText(map.get("PH3").erro);
            lbl_save_error2.setText(map.get("PH3").tit1 + " . " + map.get("PH3").desc);
            return false;
        }

        if (!CalcularPFU()) {
            lbl_save_error.setText(map.get("PFU").erro);
            lbl_save_error2.setText(map.get("PFU").tit1 + " . " + map.get("PFU").desc);
            return false;
        }

        if (!CalcularPFA()) {
            lbl_save_error.setText(map.get("PFA").erro);
            lbl_save_error2.setText(map.get("PFA").tit1 + " . " + map.get("PFA").desc);
            return false;
        }

        if (!CalcularPFD()) {
            lbl_save_error.setText(map.get("PFD").erro);
            lbl_save_error2.setText(map.get("PFD").tit1 + " . " + map.get("PFD").desc);
            return false;
        }

        if (!CalcularPER()) {
            lbl_save_error.setText(map.get("PER").erro);
            lbl_save_error2.setText(map.get("PER").tit1 + " . " + map.get("PER").desc);
            return false;
        }

        if (!CalcularPCD()) {
            lbl_save_error.setText(map.get("PCD").erro);
            lbl_save_error2.setText(map.get("PCD").tit1 + " . " + map.get("PCD").desc);
            return false;
        }

        if (!CalcularPEL()) {
            lbl_save_error.setText(map.get("PEL").erro);
            lbl_save_error2.setText(map.get("PEL").tit1 + " . " + map.get("PEL").desc);
            return false;
        }

        if (!CalcularPVL()) {
            lbl_save_error.setText(map.get("PVL").erro);
            lbl_save_error2.setText(map.get("PVL").tit1 + " . " + map.get("PVL").desc);
            return false;
        }

        if (!CalcularPSS()) {
            lbl_save_error.setText(map.get("PSS").erro);
            lbl_save_error2.setText(map.get("PSS").tit1 + " . " + map.get("PSS").desc);
            return false;
        }

        if (!CalcularPSA()) {
            lbl_save_error.setText(map.get("PSA").erro);
            lbl_save_error2.setText(map.get("PSA").tit1 + " . " + map.get("PSA").desc);
            return false;
        }

        if (!CalcularPPS()) {
            lbl_save_error.setText(map.get("PPS").erro);
            lbl_save_error2.setText(map.get("PPS").tit1 + " . " + map.get("PPS").desc);
            return false;
        }

        if (!CalcularPNU()) {
            lbl_save_error.setText(map.get("PNU").erro);
            lbl_save_error2.setText(map.get("PNU").tit1 + " . " + map.get("PNU").desc);
            return false;
        }

        if (!CalcularPAX()) {
            lbl_save_error.setText(map.get("PAX").erro);
            lbl_save_error2.setText(map.get("PAX").tit1 + " . " + map.get("PAX").desc);
            return false;
        }

        if (!CalcularPDX()) {
            lbl_save_error.setText(map.get("PDX").erro);
            lbl_save_error2.setText(map.get("PDX").tit1 + " . " + map.get("PDX").desc);
            return false;
        }

        if (!CalcularPAT()) {
            lbl_save_error.setText(map.get("PAT").erro);
            lbl_save_error2.setText(map.get("PAT").tit1 + " . " + map.get("PAT").desc);
            return false;
        }

        // bod.EditReactorUasb = true;
        //        if (!bod.GuardarUpdateBod()) {
        //            bod.EditReactorUasb = false;
        //            return false;
        //        }
        //
        //        Main main = (Main) this.getTopLevelAncestor(); // Trata de ir  al contenedor principal 
        //        main.ComprobarCambio(2);//Comprobar cambios que afecten datos posteriores, si este guardar es de una edición.

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

    private boolean CalcularPCO() {

        try {
            lbl_PCO_error.setText("");

            if (bod.setVarDob(txt_PCO_Pregunta.getText(), "PCO", map.get("PCO").rmin, map.get("PCO").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularPCO " + ex.getMessage());
        }
        lbl_PCO_error.setText(map.get("PCO").erro);
        return false;
    }

    private boolean CalcularPVM() {

        try {
            lbl_PVM_error.setText("");
            double x = bod.calcularPVM();

            if (bod.setVarDob(x, "PVM", map.get("PVM").rmin, map.get("PVM").rmax)) {
                txt_PVM_Pregunta.setText(bod.getVarFormateadaPorNombre("PVM", map.get("PVM").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularPVM " + ex.getMessage());
        }
        lbl_PVM_error.setText(map.get("PVM").erro);
        return false;
    }

    private boolean CalcularPPM() {

        try {
            lbl_PPM_error.setText("");

            if (bod.setVarDob(txt_PPM_Pregunta.getText(), "PPM", map.get("PPM").rmin, map.get("PPM").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularPPM " + ex.getMessage());
        }
        lbl_PPM_error.setText(map.get("PPM").erro);
        return false;
    }

    private boolean CalcularPAS() {

        try {
            lbl_PAS_error.setText("");
            double x = bod.calcularPAS();

            if (bod.setVarDob(x, "PAS", map.get("PAS").rmin, map.get("PAS").rmax)) {
                txt_PAS_Pregunta.setText(bod.getVarFormateadaPorNombre("PAS", map.get("PAS").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularPAS " + ex.getMessage());
        }
        lbl_PAS_error.setText(map.get("PAS").erro);
        return false;
    }

    private boolean CalcularPH2() {

        try {
            lbl_PH2_error.setText("");
            double x = bod.calcularPH2();

            if (bod.setVarDob(x, "PH2", map.get("PH2").rmin, map.get("PH2").rmax)) {
                txt_PH2_Pregunta.setText(bod.getVarFormateadaPorNombre("PH2", map.get("PH2").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularPH2 " + ex.getMessage());
        }
        lbl_PH2_error.setText(map.get("PH2").erro);
        return false;
    }

    /**
     * *
     * Si qs para Qmed menor a 15, remcomendar al usuario incrementar el valor
     * de Cv y si es > 18 recomendar disminuir Cv
     */
    private void CalcularPH2C() {

        String x = lbl_PH2_error.getText();
        if (x.contains("</HTML>")) {
            lbl_PH2_error.setForeground(Color.BLACK);
            lbl_PH2_error.setText(x.replace("</HTML>", "<br/><div style=''color:#000000;''>" + bod.calcularPH2C() + "</div></HTML>"));
            return;
        }
        lbl_PH2_error.setText(x + " " + bod.calcularPH2C());
    }

    private boolean CalcularPH3() {

        try {
            lbl_PH3_error.setText("");
            double x = bod.calcularPH3();

            if (bod.setVarDob(x, "PH3", map.get("PH3").rmin, map.get("PH3").rmax)) {
                txt_PH3_Pregunta.setText(bod.getVarFormateadaPorNombre("PH3", map.get("PH3").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularPH3 " + ex.getMessage());
        }
        lbl_PH3_error.setText(map.get("PH3").erro);
        return false;
    }

    private boolean CalcularPFU() {

        try {
            lbl_PFU_error.setText("");

            if (bod.setVarInt(txt_PFU_Pregunta.getText(), "PFU", (int) map.get("PFU").rmin, (int) map.get("PFU").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularPFU " + ex.getMessage());
        }
        lbl_PFU_error.setText(map.get("PFU").erro);
        return false;
    }

    private boolean CalcularPFA() {

        try {
            lbl_PFA_error.setText("");
            double x = bod.calcularPFA();

            if (bod.setVarDob(x, "PFA", map.get("PFA").rmin, map.get("PFA").rmax)) {
                txt_PFA_Pregunta.setText(bod.getVarFormateadaPorNombre("PFA", map.get("PFA").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularPFA " + ex.getMessage());
        }
        lbl_PFA_error.setText(map.get("PFA").erro);
        return false;
    }

    private boolean CalcularPFD() {

        try {
            lbl_PFD_error.setText("");
            double x = bod.calcularPFD();

            if (bod.setVarDob(x, "PFD", map.get("PFD").rmin, map.get("PFD").rmax)) {
                txt_PFD_Pregunta.setText(bod.getVarFormateadaPorNombre("PFD", map.get("PFD").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularPFD " + ex.getMessage());
        }
        lbl_PFD_error.setText(map.get("PFD").erro);
        return false;
    }

    private boolean CalcularPER() {

        try {
            lbl_PER_error.setText("");

            if (bod.setVarDob(txt_PER_Pregunta.getText(), "PER", map.get("PER").rmin, map.get("PER").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularPER " + ex.getMessage());
        }
        lbl_PER_error.setText(map.get("PER").erro);
        return false;
    }

    private boolean CalcularPCD() {

        try {
            lbl_PCD_error.setText("");
            double x = bod.calcularPCD();

            if (bod.setVarDob(x, "PCD", map.get("PCD").rmin, map.get("PCD").rmax)) {
                txt_PCD_Pregunta.setText(bod.getVarFormateadaPorNombre("PCD", map.get("PCD").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularPCD " + ex.getMessage());
        }
        lbl_PCD_error.setText(map.get("PCD").erro);
        return false;
    }

    private boolean CalcularPEL() {

        try {
            lbl_PEL_error.setText("");
            double x = bod.calcularPEL();

            if (bod.setVarDob(x, "PEL", map.get("PEL").rmin, map.get("PEL").rmax)) {
                txt_PEL_Pregunta.setText(bod.getVarFormateadaPorNombre("PEL", map.get("PEL").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularPEL " + ex.getMessage());
        }
        lbl_PEL_error.setText(map.get("PEL").erro);
        return false;
    }

    private boolean CalcularPVL() {

        try {
            lbl_PVL_error.setText("");
            double x = bod.calcularPVL();

            if (bod.setVarDob(x, "PVL", map.get("PVL").rmin, map.get("PVL").rmax)) {
                txt_PVL_Pregunta.setText(bod.getVarFormateadaPorNombre("PVL", map.get("PVL").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularPVL " + ex.getMessage());
        }
        lbl_PVL_error.setText(map.get("PVL").erro);
        return false;
    }

    private boolean CalcularPSS() {

        try {
            lbl_PSS_error.setText("");

            if (bod.setVarInt(txt_PSS_Pregunta.getText(), "PSS", (int) map.get("PSS").rmin, (int) map.get("PSS").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularPSS " + ex.getMessage());
        }
        lbl_PSS_error.setText(map.get("PSS").erro);
        return false;
    }

    private boolean CalcularPSA() {

        try {
            lbl_PSA_error.setText("");
            double x = bod.calcularPSA();

            if (bod.setVarDob(x, "PSA", map.get("PSA").rmin, map.get("PSA").rmax)) {
                txt_PSA_Pregunta.setText(bod.getVarFormateadaPorNombre("PSA", map.get("PSA").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularPSA " + ex.getMessage());
        }
        lbl_PSA_error.setText(map.get("PSA").erro);
        return false;
    }

    private boolean CalcularPPS() {

        try {
            lbl_PPS_error.setText("");
            double x = bod.calcularPPS();

            if (bod.setVarDob(x, "PPS", map.get("PPS").rmin, map.get("PPS").rmax)) {
                txt_PPS_Pregunta.setText(bod.getVarFormateadaPorNombre("PPS", map.get("PPS").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularPPS " + ex.getMessage());
        }
        lbl_PPS_error.setText(map.get("PPS").erro);
        return false;
    }

    private boolean CalcularPNU() {

        try {
            lbl_PNU_error.setText("");

            if (bod.setVarInt(txt_PNU_Pregunta.getText(), "PNU", (int) map.get("PNU").rmin, (int) map.get("PNU").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularPNU " + ex.getMessage());
        }
        lbl_PNU_error.setText(map.get("PNU").erro);
        return false;
    }

    private boolean CalcularPAX() {

        try {
            lbl_PAX_error.setText("");
            double x = bod.calcularPAX();

            if (bod.setVarDob(x, "PAX", map.get("PAX").rmin, map.get("PAX").rmax)) {
                txt_PAX_Pregunta.setText(bod.getVarFormateadaPorNombre("PAX", map.get("PAX").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularPAX " + ex.getMessage());
        }
        lbl_PAX_error.setText(map.get("PAX").erro);
        return false;
    }

    private boolean CalcularPDX() {

        try {
            lbl_PDX_error.setText("");
            double x = bod.calcularPDX();

            if (bod.setVarDob(x, "PDX", map.get("PDX").rmin, map.get("PDX").rmax)) {
                txt_PDX_Pregunta.setText(bod.getVarFormateadaPorNombre("PDX", map.get("PDX").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularPDX " + ex.getMessage());
        }
        lbl_PDX_error.setText(map.get("PDX").erro);
        return false;
    }

    private boolean CalcularPAT() {

        try {
            lbl_PAT_error.setText("");
            double x = bod.calcularPAT();

            if (bod.setVarDob(x, "PAT", map.get("PAT").rmin, map.get("PAT").rmax)) {
                txt_PAT_Pregunta.setText(bod.getVarFormateadaPorNombre("PAT", map.get("PAT").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularPAT " + ex.getMessage());
        }
        lbl_PAT_error.setText(map.get("PAT").erro);
        return false;
    }

    /**
     * *
     * Ejecuta ciertas funciones que a su vez desencadenan otras funciones
     *
     * @param func : Nombre de la variable de la función
     */
    private void ejecutarFunciones(String func) {
        switch (func) {
            case "PCO":
                if (CalcularPCO()) {
                    ejecutarFunciones("PVM");
                }
                break;
            case "PVM":
                if (CalcularPVM()) {
                    ejecutarFunciones("PAS");
                }
                break;
            case "PPM":
                if (CalcularPPM()) {
                    ejecutarFunciones("PAS");
                }
                break;
            case "PAS":
                if (CalcularPAS()) {
                    CalcularPH2();
                    CalcularPH2C();
                    CalcularPH3();
                    ejecutarFunciones("PFA");
                    CalcularPAT();
                }
                break;
            case "PFU":
                if (CalcularPFU()) {
                    ejecutarFunciones("PFA");
                }
                break;
            case "PFA":
                if (CalcularPFA()) {
                    CalcularPFD();
                }
                break;
            case "PER":
                if (CalcularPER()) {
                    ejecutarFunciones("PCD");
                }
                break;
            case "PCD":
                if (CalcularPCD()) {
                    ejecutarFunciones("PEL");
                }
                break;
            case "PEL":
                if (CalcularPEL()) {
                    CalcularPVL();
                }
                break;
            case "PSS":
                if (CalcularPSS()) {
                    ejecutarFunciones("PSA");
                    CalcularPPS();
                }
                break;
            case "PSA":
                if (CalcularPSA()) {
                    ejecutarFunciones("PAX");
                    CalcularPAT();
                }
                break;
            case "PNU":
                if (CalcularPNU()) {
                    ejecutarFunciones("PAX");
                }
                break;
            case "PAX":
                if (CalcularPAX()) {
                    CalcularPDX();
                }
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jsp_FiltroPercolador = new javax.swing.JScrollPane();
        jp_Componentes = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        lbl_titulo1 = new javax.swing.JLabel();
        lbl_titulo2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        btn_guardar = new javax.swing.JButton();
        lbl_PH2_desc = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        lbl_Q2C_titulo2 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        btn_close = new javax.swing.JButton();
        lbl_save_error = new javax.swing.JLabel();
        lbl_save_error2 = new javax.swing.JLabel();
        txt_Q2C_Pregunta = new javax.swing.JTextField();
        lbl_Q2C_titulo1 = new javax.swing.JLabel();
        lbl_Q3C_titulo2 = new javax.swing.JLabel();
        txt_Q3C_Pregunta = new javax.swing.JTextField();
        lbl_Q3C_titulo1 = new javax.swing.JLabel();
        lbl_UEC_titulo2 = new javax.swing.JLabel();
        txt_UEC_Pregunta = new javax.swing.JTextField();
        lbl_UEC_titulo1 = new javax.swing.JLabel();
        lbl_PCO_error = new javax.swing.JLabel();
        lbl_PCO_titulo2 = new javax.swing.JLabel();
        txt_PCO_Pregunta = new javax.swing.JTextField();
        lbl_PCO_titulo1 = new javax.swing.JLabel();
        btn_PCO_ayuda = new javax.swing.JButton();
        lbl_PVM_error = new javax.swing.JLabel();
        lbl_PVM_titulo2 = new javax.swing.JLabel();
        txt_PVM_Pregunta = new javax.swing.JTextField();
        lbl_PVM_titulo1 = new javax.swing.JLabel();
        btn_PVM_ayuda = new javax.swing.JButton();
        lbl_PPM_error = new javax.swing.JLabel();
        lbl_PPM_titulo2 = new javax.swing.JLabel();
        txt_PPM_Pregunta = new javax.swing.JTextField();
        lbl_PPM_titulo1 = new javax.swing.JLabel();
        btn_PPM_ayuda = new javax.swing.JButton();
        lbl_PAS_error = new javax.swing.JLabel();
        lbl_PAS_titulo2 = new javax.swing.JLabel();
        txt_PAS_Pregunta = new javax.swing.JTextField();
        lbl_PAS_titulo = new javax.swing.JLabel();
        lbl_PH2_error = new javax.swing.JLabel();
        lbl_PH2_titulo2 = new javax.swing.JLabel();
        txt_PH2_Pregunta = new javax.swing.JTextField();
        lbl_PH2_titulo1 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        lbl_PH3_error = new javax.swing.JLabel();
        lbl_PH3_titulo2 = new javax.swing.JLabel();
        txt_PH3_Pregunta = new javax.swing.JTextField();
        lbl_PH3_titulo1 = new javax.swing.JLabel();
        btn_PH3_ayuda = new javax.swing.JButton();
        lbl_PFU_error = new javax.swing.JLabel();
        lbl_PFU_titulo2 = new javax.swing.JLabel();
        txt_PFU_Pregunta = new javax.swing.JTextField();
        lbl_PFU_titulo1 = new javax.swing.JLabel();
        lbl_PFA_error = new javax.swing.JLabel();
        lbl_PFA_titulo2 = new javax.swing.JLabel();
        txt_PFA_Pregunta = new javax.swing.JTextField();
        lbl_PFA_titulo1 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        lbl_PFD_error = new javax.swing.JLabel();
        lbl_PFD_titulo2 = new javax.swing.JLabel();
        txt_PFD_Pregunta = new javax.swing.JTextField();
        lbl_PFD_titulo1 = new javax.swing.JLabel();
        lbl_PER_error = new javax.swing.JLabel();
        lbl_PER_titulo2 = new javax.swing.JLabel();
        txt_PER_Pregunta = new javax.swing.JTextField();
        lbl_PER_titulo1 = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        lbl_PCD_error = new javax.swing.JLabel();
        lbl_PCD_titulo2 = new javax.swing.JLabel();
        txt_PCD_Pregunta = new javax.swing.JTextField();
        lbl_PCD_titulo1 = new javax.swing.JLabel();
        btn_PER_ayuda = new javax.swing.JButton();
        lbl_PEL_error = new javax.swing.JLabel();
        lbl_PEL_titulo2 = new javax.swing.JLabel();
        txt_PEL_Pregunta = new javax.swing.JTextField();
        lbl_PEL_titulo1 = new javax.swing.JLabel();
        btn_PEL_ayuda = new javax.swing.JButton();
        lbl_PVL_error = new javax.swing.JLabel();
        lbl_PVL_titulo2 = new javax.swing.JLabel();
        txt_PVL_Pregunta = new javax.swing.JTextField();
        lbl_PVL_titulo1 = new javax.swing.JLabel();
        lbl_PSS_desc = new javax.swing.JLabel();
        lbl_PSS_error = new javax.swing.JLabel();
        lbl_PSS_titulo2 = new javax.swing.JLabel();
        txt_PSS_Pregunta = new javax.swing.JTextField();
        lbl_PSS_titulo1 = new javax.swing.JLabel();
        btn_PSS_ayuda = new javax.swing.JButton();
        lbl_PSA_error = new javax.swing.JLabel();
        lbl_PSA_titulo2 = new javax.swing.JLabel();
        txt_PSA_Pregunta = new javax.swing.JTextField();
        lbl_PSA_titulo1 = new javax.swing.JLabel();
        jSeparator13 = new javax.swing.JSeparator();
        lbl_PPS_error = new javax.swing.JLabel();
        lbl_PPS_titulo2 = new javax.swing.JLabel();
        txt_PPS_Pregunta = new javax.swing.JTextField();
        lbl_PPS_titulo1 = new javax.swing.JLabel();
        btn_PPS_ayuda = new javax.swing.JButton();
        lbl_PNU_error = new javax.swing.JLabel();
        lbl_PNU_titulo2 = new javax.swing.JLabel();
        txt_PNU_Pregunta = new javax.swing.JTextField();
        lbl_PNU_titulo1 = new javax.swing.JLabel();
        jSeparator14 = new javax.swing.JSeparator();
        lbl_PAT_error = new javax.swing.JLabel();
        lbl_PAT_titulo2 = new javax.swing.JLabel();
        txt_PAT_Pregunta = new javax.swing.JTextField();
        lbl_PAT_titulo1 = new javax.swing.JLabel();
        lbl_PFU_desc = new javax.swing.JLabel();
        lbl_PAX_error = new javax.swing.JLabel();
        lbl_PAX_titulo2 = new javax.swing.JLabel();
        txt_PAX_Pregunta = new javax.swing.JTextField();
        lbl_PAX_titulo1 = new javax.swing.JLabel();
        lbl_PDX_error = new javax.swing.JLabel();
        lbl_PDX_titulo2 = new javax.swing.JLabel();
        txt_PDX_Pregunta = new javax.swing.JTextField();
        lbl_PDX_titulo1 = new javax.swing.JLabel();
        btn_close2 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1024, 2000));

        jsp_FiltroPercolador.setPreferredSize(new java.awt.Dimension(1024, 768));

        jp_Componentes.setPreferredSize(new java.awt.Dimension(1024, 1400));
        jp_Componentes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jp_Componentes.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 38, 1024, -1));

        lbl_titulo1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_titulo1.setForeground(new java.awt.Color(51, 51, 51));
        lbl_titulo1.setText("Titulo");
        jp_Componentes.add(lbl_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 300, 30));

        lbl_titulo2.setForeground(new java.awt.Color(0, 51, 102));
        lbl_titulo2.setText("Desc");
        jp_Componentes.add(lbl_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 0, 600, 30));
        jp_Componentes.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 1024, -1));

        btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Button_Salva_Guardar_21.png"))); // NOI18N
        btn_guardar.setText("save");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1200, 120, -1));

        lbl_PH2_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_PH2_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_PH2_desc.setText("Desc");
        lbl_PH2_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PH2_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 610, 30));
        jp_Componentes.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 343, 1024, 2));

        lbl_Q2C_titulo2.setText("Titulo");
        lbl_Q2C_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2C_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 50, 80, 25));
        jp_Componentes.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 467, 1024, 0));

        btn_close.setText("Salir");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1240, 120, 30));

        lbl_save_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error.setText(" . . .");
        jp_Componentes.add(lbl_save_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 1200, 400, 35));

        lbl_save_error2.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error2.setText(" . . .");
        jp_Componentes.add(lbl_save_error2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 1240, 400, 35));

        txt_Q2C_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q2C_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 50, 130, 25));

        lbl_Q2C_titulo1.setText("Titulo");
        lbl_Q2C_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2C_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 300, 30));

        lbl_Q3C_titulo2.setText("Titulo");
        lbl_Q3C_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q3C_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 90, 80, 25));

        txt_Q3C_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q3C_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 90, 130, 25));

        lbl_Q3C_titulo1.setText("Titulo");
        lbl_Q3C_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q3C_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 300, 30));

        lbl_UEC_titulo2.setText("Titulo");
        lbl_UEC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UEC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 130, 80, 25));

        txt_UEC_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UEC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 130, 130, 25));

        lbl_UEC_titulo1.setText("Titulo");
        lbl_UEC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UEC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 300, 30));

        lbl_PCO_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PCO_error.setText(" . . .");
        jp_Componentes.add(lbl_PCO_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 180, 340, 35));

        lbl_PCO_titulo2.setText("Titulo");
        lbl_PCO_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PCO_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 180, 80, 25));
        jp_Componentes.add(txt_PCO_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 180, 130, 25));

        lbl_PCO_titulo1.setText("Titulo");
        lbl_PCO_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PCO_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 300, 30));

        btn_PCO_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_PCO_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_PCO_ayuda.setContentAreaFilled(false);
        btn_PCO_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_PCO_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 180, 25, 25));

        lbl_PVM_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PVM_error.setText(" . . .");
        jp_Componentes.add(lbl_PVM_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 220, 340, 35));

        lbl_PVM_titulo2.setText("Titulo");
        lbl_PVM_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PVM_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 220, 80, 25));

        txt_PVM_Pregunta.setEditable(false);
        jp_Componentes.add(txt_PVM_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 220, 130, 25));

        lbl_PVM_titulo1.setText("Titulo");
        lbl_PVM_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PVM_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 300, 30));

        btn_PVM_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_PVM_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_PVM_ayuda.setContentAreaFilled(false);
        btn_PVM_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_PVM_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 220, 25, 25));

        lbl_PPM_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PPM_error.setText(" . . .");
        jp_Componentes.add(lbl_PPM_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 260, 340, 35));

        lbl_PPM_titulo2.setText("Titulo");
        lbl_PPM_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PPM_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 260, 80, 25));
        jp_Componentes.add(txt_PPM_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 260, 130, 25));

        lbl_PPM_titulo1.setText("Titulo");
        lbl_PPM_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PPM_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 300, 30));

        btn_PPM_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_PPM_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_PPM_ayuda.setContentAreaFilled(false);
        btn_PPM_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_PPM_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 260, 25, 25));

        lbl_PAS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PAS_error.setText(" . . .");
        jp_Componentes.add(lbl_PAS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 300, 340, 35));

        lbl_PAS_titulo2.setText("Titulo");
        lbl_PAS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PAS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 300, 80, 25));

        txt_PAS_Pregunta.setEditable(false);
        jp_Componentes.add(txt_PAS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 300, 130, 25));

        lbl_PAS_titulo.setText("Titulo");
        lbl_PAS_titulo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PAS_titulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 300, 30));

        lbl_PH2_error.setText(" . . .");
        jp_Componentes.add(lbl_PH2_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 390, 340, 35));

        lbl_PH2_titulo2.setText("Titulo");
        lbl_PH2_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PH2_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 390, 80, 25));

        txt_PH2_Pregunta.setEditable(false);
        jp_Componentes.add(txt_PH2_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 390, 130, 25));

        lbl_PH2_titulo1.setText("Titulo");
        lbl_PH2_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PH2_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 300, 30));
        jp_Componentes.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 472, 1024, 2));
        jSeparator8.getAccessibleContext().setAccessibleName("");

        lbl_PH3_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PH3_error.setText(" . . .");
        jp_Componentes.add(lbl_PH3_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 430, 340, 35));

        lbl_PH3_titulo2.setText("Titulo");
        lbl_PH3_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PH3_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 430, 80, 25));

        txt_PH3_Pregunta.setEditable(false);
        jp_Componentes.add(txt_PH3_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 430, 130, 25));

        lbl_PH3_titulo1.setText("Titulo");
        lbl_PH3_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PH3_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 300, 30));

        btn_PH3_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_PH3_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_PH3_ayuda.setContentAreaFilled(false);
        btn_PH3_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_PH3_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 430, 25, 25));

        lbl_PFU_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PFU_error.setText(" . . .");
        jp_Componentes.add(lbl_PFU_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 520, 340, 35));

        lbl_PFU_titulo2.setText("Titulo");
        lbl_PFU_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PFU_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 520, 80, 25));
        jp_Componentes.add(txt_PFU_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 520, 130, 25));

        lbl_PFU_titulo1.setText("Titulo");
        lbl_PFU_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PFU_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, 300, 30));

        lbl_PFA_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PFA_error.setText(" . . .");
        jp_Componentes.add(lbl_PFA_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 560, 340, 35));

        lbl_PFA_titulo2.setText("Titulo");
        lbl_PFA_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PFA_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 560, 80, 25));

        txt_PFA_Pregunta.setEditable(false);
        jp_Componentes.add(txt_PFA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 560, 130, 25));

        lbl_PFA_titulo1.setText("Titulo");
        lbl_PFA_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PFA_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 560, 300, 30));
        jp_Componentes.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 642, 1024, 2));

        lbl_PFD_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PFD_error.setText(" . . .");
        jp_Componentes.add(lbl_PFD_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 600, 340, 35));

        lbl_PFD_titulo2.setText("Titulo");
        lbl_PFD_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PFD_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 600, 80, 25));

        txt_PFD_Pregunta.setEditable(false);
        jp_Componentes.add(txt_PFD_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 600, 130, 25));

        lbl_PFD_titulo1.setText("Titulo");
        lbl_PFD_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PFD_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 600, 300, 30));

        lbl_PER_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PER_error.setText(" . . .");
        jp_Componentes.add(lbl_PER_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 660, 340, 35));

        lbl_PER_titulo2.setText("Titulo");
        lbl_PER_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PER_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 660, 80, 25));
        jp_Componentes.add(txt_PER_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 660, 130, 25));

        lbl_PER_titulo1.setText("Titulo");
        lbl_PER_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PER_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 660, 300, 30));
        jp_Componentes.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 822, 1024, 2));

        lbl_PCD_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PCD_error.setText(" . . .");
        jp_Componentes.add(lbl_PCD_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 700, 340, 35));

        lbl_PCD_titulo2.setText("Titulo");
        lbl_PCD_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PCD_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 700, 80, 25));

        txt_PCD_Pregunta.setEditable(false);
        jp_Componentes.add(txt_PCD_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 700, 130, 25));

        lbl_PCD_titulo1.setText("Titulo");
        lbl_PCD_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PCD_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 700, 300, 30));

        btn_PER_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_PER_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_PER_ayuda.setContentAreaFilled(false);
        btn_PER_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_PER_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 660, 25, 25));

        lbl_PEL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PEL_error.setText(" . . .");
        jp_Componentes.add(lbl_PEL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 740, 340, 35));

        lbl_PEL_titulo2.setText("Titulo");
        lbl_PEL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PEL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 740, 80, 25));

        txt_PEL_Pregunta.setEditable(false);
        jp_Componentes.add(txt_PEL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 740, 130, 25));

        lbl_PEL_titulo1.setText("Titulo");
        lbl_PEL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PEL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 740, 300, 30));

        btn_PEL_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_PEL_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_PEL_ayuda.setContentAreaFilled(false);
        btn_PEL_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_PEL_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 740, 25, 25));

        lbl_PVL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PVL_error.setText(" . . .");
        jp_Componentes.add(lbl_PVL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 780, 340, 35));

        lbl_PVL_titulo2.setText("Titulo");
        lbl_PVL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PVL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 780, 80, 25));

        txt_PVL_Pregunta.setEditable(false);
        jp_Componentes.add(txt_PVL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 780, 130, 25));

        lbl_PVL_titulo1.setText("Titulo");
        lbl_PVL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PVL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 780, 300, 30));

        lbl_PSS_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_PSS_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_PSS_desc.setText("Desc");
        lbl_PSS_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PSS_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 840, 610, 30));

        lbl_PSS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PSS_error.setText(" . . .");
        jp_Componentes.add(lbl_PSS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 880, 340, 35));

        lbl_PSS_titulo2.setText("Titulo");
        lbl_PSS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PSS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 880, 80, 25));
        jp_Componentes.add(txt_PSS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 880, 130, 25));

        lbl_PSS_titulo1.setText("Titulo");
        lbl_PSS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PSS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 880, 300, 30));

        btn_PSS_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_PSS_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_PSS_ayuda.setContentAreaFilled(false);
        btn_PSS_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_PSS_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 880, 25, 25));

        lbl_PSA_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PSA_error.setText(" . . .");
        jp_Componentes.add(lbl_PSA_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 920, 340, 35));

        lbl_PSA_titulo2.setText("Titulo");
        lbl_PSA_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PSA_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 920, 80, 25));

        txt_PSA_Pregunta.setEditable(false);
        jp_Componentes.add(txt_PSA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 920, 130, 25));

        lbl_PSA_titulo1.setText("Titulo");
        lbl_PSA_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PSA_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 920, 300, 30));
        jp_Componentes.add(jSeparator13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1120, 1024, -1));

        lbl_PPS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PPS_error.setText(" . . .");
        jp_Componentes.add(lbl_PPS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 960, 340, 35));

        lbl_PPS_titulo2.setText("Titulo");
        lbl_PPS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PPS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 960, 80, 25));

        txt_PPS_Pregunta.setEditable(false);
        jp_Componentes.add(txt_PPS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 960, 130, 25));

        lbl_PPS_titulo1.setText("Titulo");
        lbl_PPS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PPS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 960, 300, 30));

        btn_PPS_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_PPS_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_PPS_ayuda.setContentAreaFilled(false);
        btn_PPS_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_PPS_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 960, 25, 25));

        lbl_PNU_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PNU_error.setText(" . . .");
        jp_Componentes.add(lbl_PNU_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1000, 340, 35));

        lbl_PNU_titulo2.setText("Titulo");
        lbl_PNU_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PNU_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1000, 80, 25));
        jp_Componentes.add(txt_PNU_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1000, 130, 25));

        lbl_PNU_titulo1.setText("Titulo");
        lbl_PNU_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PNU_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1000, 300, 30));
        jp_Componentes.add(jSeparator14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1180, 1024, -1));

        lbl_PAT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PAT_error.setText(" . . .");
        jp_Componentes.add(lbl_PAT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1140, 340, 35));

        lbl_PAT_titulo2.setText("Titulo");
        lbl_PAT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PAT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1140, 80, 25));

        txt_PAT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_PAT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1140, 130, 25));

        lbl_PAT_titulo1.setText("Titulo");
        lbl_PAT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PAT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1140, 300, 30));

        lbl_PFU_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_PFU_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_PFU_desc.setText("Desc");
        lbl_PFU_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PFU_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 480, 610, 30));

        lbl_PAX_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PAX_error.setText(" . . .");
        jp_Componentes.add(lbl_PAX_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1040, 340, 35));

        lbl_PAX_titulo2.setText("Titulo");
        lbl_PAX_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PAX_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1040, 80, 25));

        txt_PAX_Pregunta.setEditable(false);
        jp_Componentes.add(txt_PAX_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1040, 130, 25));

        lbl_PAX_titulo1.setText("Titulo");
        lbl_PAX_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PAX_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1040, 300, 30));

        lbl_PDX_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PDX_error.setText(" . . .");
        jp_Componentes.add(lbl_PDX_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1080, 340, 35));

        lbl_PDX_titulo2.setText("Titulo");
        lbl_PDX_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PDX_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1080, 80, 25));

        txt_PDX_Pregunta.setEditable(false);
        jp_Componentes.add(txt_PDX_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1080, 130, 25));

        lbl_PDX_titulo1.setText("Titulo");
        lbl_PDX_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PDX_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1080, 300, 30));

        jsp_FiltroPercolador.setViewportView(jp_Componentes);
        jp_Componentes.getAccessibleContext().setAccessibleName("");

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
            .addComponent(jsp_FiltroPercolador, javax.swing.GroupLayout.PREFERRED_SIZE, 1008, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_close2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btn_close2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jsp_FiltroPercolador, javax.swing.GroupLayout.DEFAULT_SIZE, 1926, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        try {
            if (bod.idproyecto > 0 && Guardar()) {
                util.Mensaje("Datos guardados", "ok");
                Main main = (Main) this.getTopLevelAncestor(); // Trata de ir  al contenedor principal y activa o desactivar botones
                main.comprobarBotones();
                main.CargarDatosFondo();
            }
        } catch (Exception ex) {
            log.error("Error en acción boton guardar() " + ex.getMessage());
        }
    }//GEN-LAST:event_btn_guardarActionPerformed

    private void btn_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_closeActionPerformed
        bod.WinFiltroPercolador= false;
        this.dispose();
    }//GEN-LAST:event_btn_closeActionPerformed

    private void btn_close2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_close2ActionPerformed
        // TODO add your handling code here:
        bod.WinFiltroPercolador = false;
        this.dispose();
    }//GEN-LAST:event_btn_close2ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_PCO_ayuda;
    private javax.swing.JButton btn_PEL_ayuda;
    private javax.swing.JButton btn_PER_ayuda;
    private javax.swing.JButton btn_PH3_ayuda;
    private javax.swing.JButton btn_PPM_ayuda;
    private javax.swing.JButton btn_PPS_ayuda;
    private javax.swing.JButton btn_PSS_ayuda;
    private javax.swing.JButton btn_PVM_ayuda;
    private javax.swing.JButton btn_close;
    private javax.swing.JButton btn_close2;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JPanel jp_Componentes;
    private javax.swing.JScrollPane jsp_FiltroPercolador;
    private javax.swing.JLabel lbl_PAS_error;
    private javax.swing.JLabel lbl_PAS_titulo;
    private javax.swing.JLabel lbl_PAS_titulo2;
    private javax.swing.JLabel lbl_PAT_error;
    private javax.swing.JLabel lbl_PAT_titulo1;
    private javax.swing.JLabel lbl_PAT_titulo2;
    private javax.swing.JLabel lbl_PAX_error;
    private javax.swing.JLabel lbl_PAX_titulo1;
    private javax.swing.JLabel lbl_PAX_titulo2;
    private javax.swing.JLabel lbl_PCD_error;
    private javax.swing.JLabel lbl_PCD_titulo1;
    private javax.swing.JLabel lbl_PCD_titulo2;
    private javax.swing.JLabel lbl_PCO_error;
    private javax.swing.JLabel lbl_PCO_titulo1;
    private javax.swing.JLabel lbl_PCO_titulo2;
    private javax.swing.JLabel lbl_PDX_error;
    private javax.swing.JLabel lbl_PDX_titulo1;
    private javax.swing.JLabel lbl_PDX_titulo2;
    private javax.swing.JLabel lbl_PEL_error;
    private javax.swing.JLabel lbl_PEL_titulo1;
    private javax.swing.JLabel lbl_PEL_titulo2;
    private javax.swing.JLabel lbl_PER_error;
    private javax.swing.JLabel lbl_PER_titulo1;
    private javax.swing.JLabel lbl_PER_titulo2;
    private javax.swing.JLabel lbl_PFA_error;
    private javax.swing.JLabel lbl_PFA_titulo1;
    private javax.swing.JLabel lbl_PFA_titulo2;
    private javax.swing.JLabel lbl_PFD_error;
    private javax.swing.JLabel lbl_PFD_titulo1;
    private javax.swing.JLabel lbl_PFD_titulo2;
    private javax.swing.JLabel lbl_PFU_desc;
    private javax.swing.JLabel lbl_PFU_error;
    private javax.swing.JLabel lbl_PFU_titulo1;
    private javax.swing.JLabel lbl_PFU_titulo2;
    private javax.swing.JLabel lbl_PH2_desc;
    private javax.swing.JLabel lbl_PH2_error;
    private javax.swing.JLabel lbl_PH2_titulo1;
    private javax.swing.JLabel lbl_PH2_titulo2;
    private javax.swing.JLabel lbl_PH3_error;
    private javax.swing.JLabel lbl_PH3_titulo1;
    private javax.swing.JLabel lbl_PH3_titulo2;
    private javax.swing.JLabel lbl_PNU_error;
    private javax.swing.JLabel lbl_PNU_titulo1;
    private javax.swing.JLabel lbl_PNU_titulo2;
    private javax.swing.JLabel lbl_PPM_error;
    private javax.swing.JLabel lbl_PPM_titulo1;
    private javax.swing.JLabel lbl_PPM_titulo2;
    private javax.swing.JLabel lbl_PPS_error;
    private javax.swing.JLabel lbl_PPS_titulo1;
    private javax.swing.JLabel lbl_PPS_titulo2;
    private javax.swing.JLabel lbl_PSA_error;
    private javax.swing.JLabel lbl_PSA_titulo1;
    private javax.swing.JLabel lbl_PSA_titulo2;
    private javax.swing.JLabel lbl_PSS_desc;
    private javax.swing.JLabel lbl_PSS_error;
    private javax.swing.JLabel lbl_PSS_titulo1;
    private javax.swing.JLabel lbl_PSS_titulo2;
    private javax.swing.JLabel lbl_PVL_error;
    private javax.swing.JLabel lbl_PVL_titulo1;
    private javax.swing.JLabel lbl_PVL_titulo2;
    private javax.swing.JLabel lbl_PVM_error;
    private javax.swing.JLabel lbl_PVM_titulo1;
    private javax.swing.JLabel lbl_PVM_titulo2;
    private javax.swing.JLabel lbl_Q2C_titulo1;
    private javax.swing.JLabel lbl_Q2C_titulo2;
    private javax.swing.JLabel lbl_Q3C_titulo1;
    private javax.swing.JLabel lbl_Q3C_titulo2;
    private javax.swing.JLabel lbl_UEC_titulo1;
    private javax.swing.JLabel lbl_UEC_titulo2;
    private javax.swing.JLabel lbl_save_error;
    private javax.swing.JLabel lbl_save_error2;
    private javax.swing.JLabel lbl_titulo1;
    private javax.swing.JLabel lbl_titulo2;
    private javax.swing.JTextField txt_PAS_Pregunta;
    private javax.swing.JTextField txt_PAT_Pregunta;
    private javax.swing.JTextField txt_PAX_Pregunta;
    private javax.swing.JTextField txt_PCD_Pregunta;
    private javax.swing.JTextField txt_PCO_Pregunta;
    private javax.swing.JTextField txt_PDX_Pregunta;
    private javax.swing.JTextField txt_PEL_Pregunta;
    private javax.swing.JTextField txt_PER_Pregunta;
    private javax.swing.JTextField txt_PFA_Pregunta;
    private javax.swing.JTextField txt_PFD_Pregunta;
    private javax.swing.JTextField txt_PFU_Pregunta;
    private javax.swing.JTextField txt_PH2_Pregunta;
    private javax.swing.JTextField txt_PH3_Pregunta;
    private javax.swing.JTextField txt_PNU_Pregunta;
    private javax.swing.JTextField txt_PPM_Pregunta;
    private javax.swing.JTextField txt_PPS_Pregunta;
    private javax.swing.JTextField txt_PSA_Pregunta;
    private javax.swing.JTextField txt_PSS_Pregunta;
    private javax.swing.JTextField txt_PVL_Pregunta;
    private javax.swing.JTextField txt_PVM_Pregunta;
    private javax.swing.JTextField txt_Q2C_Pregunta;
    private javax.swing.JTextField txt_Q3C_Pregunta;
    private javax.swing.JTextField txt_UEC_Pregunta;
    // End of variables declaration//GEN-END:variables
}
