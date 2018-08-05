package UI;

import BO.Bod;
import Componentes.Pregunta;
import DB.Dao;
import java.io.File;
import Componentes.Util;
import org.apache.log4j.Logger;
import Componentes.Configuracion;
import Componentes.Listener_Popup;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.event.PopupMenuListener;

public class LodosActivModAirExten extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("Lodos Activados Modalidad Aireacion Extendida");
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    public Map<String, Pregunta> map = new TreeMap<>();
    PopupMenuListener listener;
    private Bod bod = new Bod("");
    private Listener_Popup Lpopup;
    Util util = new Util();
    Pregunta pg;
    private boolean eSave = true;

    public LodosActivModAirExten(Bod bod) {
        this.bod = bod;
        initComponents();
        InicializarComponentes();
    }

    /**
     * Inicializa todos los elementos (jlabels,jtextbox,etc.) con sus
     * respectivos valores y textos desde la base de datos.
     */
    public void InicializarComponentes() {

        ResultSet rs;
        bod.WinLodosActivadsAireacExt = true;//Bandera La ventana se ha abierto
        btn_guardar.setText("Guardar");
        btn_close.setText("Cerrar");
        try {
            // - - - - - - Cargar el titulo de la sección  
            rs = db.ResultadoSelect("obtenerseccion", "13");

            lbl_titulo1.setText(rs.getString("Nombre"));
            lbl_titulo2.setText(rs.getString("Mensaje"));
            // - - - - - - Dato 1 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q2C en m³/dia
            rs = db.ResultadoSelect("datospregunta", "Q2C");

            String[] titulo2 = rs.getString("titulo2").split("\\|"); //Q2C en el titulo 2 tiene 2 posibles textos

            lbl_Q2C_titulo1.setText(rs.getString("titulo1"));
            lbl_Q2C_titulo2.setText(titulo2[1].trim());
            txt_Q2C_Pregunta.setText(bod.getVarFormateada(bod.calcularQ2Cm3Dia(), rs.getInt("decimales")));
            // - - - - - - Dato 2 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Si CAB no fue establecido, se usa CAQ
            if (bod.getVarDob("CAB") != 0) {
                rs = db.ResultadoSelect("datospregunta", "CAB");

                lbl_CABQ_titulo1.setText(rs.getString("titulo1"));
                lbl_CABQ_titulo2.setText(rs.getString("titulo2"));
                txt_CABQ_Pregunta.setText(bod.getVarFormateada(bod.getVarDob("CAB"), rs.getInt("decimales")));
            } else {
                rs = db.ResultadoSelect("datospregunta", "CAQ");

                lbl_CABQ_titulo1.setText(rs.getString("titulo1"));
                lbl_CABQ_titulo2.setText(rs.getString("titulo2"));
                txt_CABQ_Pregunta.setText(bod.getVarFormateada(bod.getVarDob("CAQ"), rs.getInt("decimales")));
            }
            // - - - - - - Pregunta 01 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - CVS
            rs = db.ResultadoSelect("datospregunta", "CVS");

            pg = new Pregunta(); // Objeto que será creado con datos, básicos de rangos y mensajes
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("CVS", pg);

            lbl_CVS_titulo1.setText(pg.tit1);
            lbl_CVS_titulo2.setText(rs.getString("titulo2"));
            txt_CVS_Pregunta.setText(rs.getString("valorpordefecto"));

            // - - - - - - Pregunta 09 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EDB
            rs = db.ResultadoSelect("datospregunta", "EDB");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EDB", pg);

            lbl_EDB_titulo1.setText(pg.tit1);
            lbl_EDB_titulo2.setText(rs.getString("titulo2"));
            txt_EDB_Pregunta.setText(rs.getString("valorpordefecto"));
            lbl_EDB_desc.setText(pg.desc);

            txt_EDB_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("EDB");
                }
            });
            AsignarPopupBtn(btn_EDB_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 490, 320);
            // - - - - - - Pregunta 10 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ESE
            rs = db.ResultadoSelect("datospregunta", "ESE");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("ESE", pg);

            lbl_ESE_titulo1.setText(pg.tit1);
            lbl_ESE_titulo2.setText(rs.getString("titulo2"));
            txt_ESE_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_ESE_Pregunta.addFocusListener(new FocusAdapter() {
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("ESE");
                }
            });

            AsignarPopupBtn(btn_ESE_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 540, 260);
            // - - - - - - Pregunta 11 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ERD
            rs = db.ResultadoSelect("datospregunta", "ERD");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("ERD", pg);

            lbl_ERD_titulo1.setText(pg.tit1);
            lbl_ERD_titulo2.setText(rs.getString("titulo2"));
            txt_ERD_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_ERD_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("ERD");
                }
            });
            // - - - - - - Pregunta 12 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EST
            rs = db.ResultadoSelect("datospregunta", "EST");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EST", pg);

            lbl_EST_titulo1.setText(pg.tit1);
            lbl_EST_titulo2.setText(rs.getString("titulo2"));
            txt_EST_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_EST_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("EST");
                }
            });

            AsignarPopupBtn(btn_EST_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 500, 240);
            // - - - - - - Pregunta 13 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ESR
            rs = db.ResultadoSelect("datospregunta", "ESR");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("ESR", pg);

            lbl_ESR_titulo1.setText(pg.tit1);
            lbl_ESR_titulo2.setText(rs.getString("titulo2"));
            txt_ESR_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_ESR_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("ESR");
                }
            });
            AsignarPopupBtn(btn_ESR_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 490, 150);
            // - - - - - - Pregunta 14 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ECC
            rs = db.ResultadoSelect("datospregunta", "ECC");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("ECC", pg);

            lbl_ECC_titulo1.setText(pg.tit1);
            lbl_ECC_titulo2.setText(rs.getString("titulo2"));
            txt_ECC_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_ECC_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("ECC");
                }
            });

            AsignarPopupBtn(btn_ECC_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 580, 230);
            // - - - - - - Pregunta 15 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ETE
            rs = db.ResultadoSelect("datospregunta", "ETE");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("ETE", pg);

            lbl_ETE_titulo1.setText(pg.tit1);
            lbl_ETE_titulo2.setText(rs.getString("titulo2"));
            txt_ETE_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_ETE_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("ETE");
                }
            });

            AsignarPopupBtn(btn_ETE_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 490, 170);
            // - - - - - - Pregunta 16 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EEL
            rs = db.ResultadoSelect("datospregunta", "EEL");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EEL", pg);

            lbl_EEL_titulo1.setText(pg.tit1);
            lbl_EEL_titulo2.setText(rs.getString("titulo2"));
            txt_EEL_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_EEL_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("EEL");
                }
            });

            AsignarPopupBtn(btn_EEL_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 450, 180);
            // - - - - - - Pregunta 17 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ECE
            rs = db.ResultadoSelect("datospregunta", "ECE");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("ECE", pg);

            lbl_ECE_titulo1.setText(pg.tit1);
            lbl_ECE_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_ECE_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 480, 270);
            // - - - - - - Pregunta 18 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EDP
            rs = db.ResultadoSelect("datospregunta", "EDP");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EDP", pg);

            lbl_EDP_titulo1.setText(pg.tit1);
            lbl_EDP_titulo2.setText(rs.getString("titulo2"));
            lbl_EDP_desc.setText(pg.desc);

            AsignarPopupBtn(btn_EDP_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 460, 180);
            // - - - - - - Pregunta 19 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EDS
            rs = db.ResultadoSelect("datospregunta", "EDS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EDS", pg);

            lbl_EDS_titulo1.setText(pg.tit1);
            lbl_EDS_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 20 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EES
            rs = db.ResultadoSelect("datospregunta", "EES");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EES", pg);

            lbl_EES_titulo1.setText(pg.tit1);
            lbl_EES_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 21 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EED
            rs = db.ResultadoSelect("datospregunta", "EED");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EED", pg);

            lbl_EED_titulo1.setText(pg.tit1);
            lbl_EED_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 22 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EPL
            rs = db.ResultadoSelect("datospregunta", "EPL");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EPL", pg);

            lbl_EPL_titulo1.setText(pg.tit1);
            lbl_EPL_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_EPL_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 560, 560);
            // - - - - - - Pregunta 23 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ELT
            rs = db.ResultadoSelect("datospregunta", "ELT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("ELT", pg);

            lbl_ELT_titulo1.setText(pg.tit1);
            lbl_ELT_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 24 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EVL
            rs = db.ResultadoSelect("datospregunta", "EVL");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EVL", pg);

            lbl_EVL_titulo1.setText(pg.tit1);
            lbl_EVL_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_EVL_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 460, 280);
            // - - - - - - Pregunta 25 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EVT
            rs = db.ResultadoSelect("datospregunta", "EVT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EVT", pg);

            lbl_EVT_titulo1.setText(pg.tit1);
            lbl_EVT_titulo2.setText(rs.getString("titulo2"));
            lbl_EVT_desc.setText(pg.desc);

            AsignarPopupBtn(btn_EVT_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 320, 340);
            // - - - - - - Pregunta 26 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EVH
            rs = db.ResultadoSelect("datospregunta", "EVH");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EVH", pg);

            lbl_EVH_titulo1.setText(pg.tit1);
            lbl_EVH_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_EVH_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 350, 170);
            // - - - - - - Pregunta 27 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EPT
            rs = db.ResultadoSelect("datospregunta", "EPT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EPT", pg);

            lbl_EPT_titulo1.setText(pg.tit1);
            lbl_EPT_titulo2.setText(rs.getString("titulo2"));
            txt_EPT_Pregunta.setText(rs.getString("valorpordefecto"));
            lbl_EPT_desc.setText(pg.desc);

            txt_EPT_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("EPT");
                }
            });

            AsignarPopupBtn(btn_EPT_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 440, 160);
            AsignarPopupBtn(btn_EPT_ayuda1, rs.getString("ayuda2"), rs.getString("ayudalink"), 500, 150);
            // - - - - - - Pregunta 28 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EAS
            rs = db.ResultadoSelect("datospregunta", "EAS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EAS", pg);

            lbl_EAS_titulo1.setText(pg.tit1);
            lbl_EAS_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 28A Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EWT
            rs = db.ResultadoSelect("datospregunta", "EWT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EWT", pg);

            lbl_EWT_titulo1.setText(pg.tit1);
            lbl_EWT_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 28B Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EHT
            rs = db.ResultadoSelect("datospregunta", "EHT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EHT", pg);

            lbl_EHT_titulo1.setText(pg.tit1);
            lbl_EHT_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 29 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EAM
            rs = db.ResultadoSelect("datospregunta", "EAM");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EAM", pg);

            lbl_EAM_titulo1.setText(pg.tit1);
            lbl_EAM_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_EAM_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 470, 420);
            // - - - - - - Pregunta 30 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ERM
            rs = db.ResultadoSelect("datospregunta", "ERM");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("ERM", pg);

            lbl_ERM_titulo1.setText(pg.tit1);
            lbl_ERM_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_ERM_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 360, 220);
            // - - - - - - Pregunta 31 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EEC
            rs = db.ResultadoSelect("datospregunta", "EEC");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EEC", pg);

            lbl_EEC_titulo1.setText(pg.tit1);
            lbl_EEC_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_EEC_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 360, 250);
            // - - - - - - Pregunta 32 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EFS
            rs = db.ResultadoSelect("datospregunta", "EFS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EFS", pg);

            lbl_EFS_titulo1.setText(pg.tit1);
            lbl_EFS_titulo2.setText(rs.getString("titulo2"));
            txt_EFS_Pregunta.setText(rs.getString("valorpordefecto"));
            lbl_EFS_desc.setText(pg.desc);

            txt_EFS_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("EFS");
                }
            });

            AsignarPopupBtn(btn_EFS_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 600, 220);
            // - - - - - - Pregunta 33 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ESA
            rs = db.ResultadoSelect("datospregunta", "ESA");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("ESA", pg);

            lbl_ESA_titulo1.setText(pg.tit1);
            lbl_ESA_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_ESA_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 320, 240);
            // - - - - - - Pregunta 34 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EMS
            rs = db.ResultadoSelect("datospregunta", "EMS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EMS", pg);

            lbl_EMS_titulo1.setText(pg.tit1);
            lbl_EMS_titulo2.setText(rs.getString("titulo2"));
            lbl_EMS_desc.setText(pg.desc);

            AsignarPopupBtn(btn_EMS_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 490, 280);
            // - - - - - - Pregunta 35 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EHS
            rs = db.ResultadoSelect("datospregunta", "EHS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EHS", pg);

            lbl_EHS_titulo1.setText(pg.tit1);
            lbl_EHS_titulo2.setText(rs.getString("titulo2"));
            txt_EHS_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_EHS_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("EHS");
                }
            });

            AsignarPopupBtn(btn_EHS_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 430, 150);
            // - - - - - - Pregunta 35B Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ESD
            rs = db.ResultadoSelect("datospregunta", "ESD");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("ESD", pg);

            lbl_ESD_titulo1.setText(pg.tit1);
            lbl_ESD_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 36 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EAT
            rs = db.ResultadoSelect("datospregunta", "EAT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EAT", pg);

            lbl_EAT_titulo1.setText(pg.tit1);
            lbl_EAT_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - Si ya han sido llenado correctamente estos datos se obtienen de Bod
            if (bod.EditLodosActivadsAireacExt) {

                txt_EDB_Pregunta.setText(bod.getVarFormateadaPorNombre("EDB", map.get("EDB").deci));
                txt_ESE_Pregunta.setText(bod.getVarFormateadaPorNombre("ESE", map.get("ESE").deci));
                txt_ERD_Pregunta.setText(bod.getVarFormateadaPorNombre("ERD", map.get("ERD").deci));
                txt_EST_Pregunta.setText(bod.getVarFormateadaPorNombre("EST", map.get("EST").deci));
                txt_ESR_Pregunta.setText(bod.getVarFormateadaPorNombre("ESR", map.get("ESR").deci));
                txt_ECC_Pregunta.setText(bod.getVarFormateadaPorNombre("ECC", map.get("ECC").deci));
                txt_ETE_Pregunta.setText(bod.getVarFormateadaPorNombre("ETE", map.get("ETE").deci));
                txt_EEL_Pregunta.setText(bod.getVarFormateadaPorNombre("EEL", map.get("EEL").deci));
                txt_ECE_Pregunta.setText(bod.getVarFormateadaPorNombre("ECE", map.get("ECE").deci));
                txt_EDP_Pregunta.setText(bod.getVarFormateadaPorNombre("EDP", map.get("EDP").deci));
                txt_EDS_Pregunta.setText(bod.getVarFormateadaPorNombre("EDS", map.get("EDS").deci));
                txt_EES_Pregunta.setText(bod.getVarFormateadaPorNombre("EES", map.get("EES").deci));
                txt_EED_Pregunta.setText(bod.getVarFormateadaPorNombre("EED", map.get("EED").deci));
                txt_EPL_Pregunta.setText(bod.getVarFormateadaPorNombre("EPL", map.get("EPL").deci));
                txt_ELT_Pregunta.setText(bod.getVarFormateadaPorNombre("ELT", map.get("ELT").deci));
                txt_EVL_Pregunta.setText(bod.getVarFormateadaPorNombre("EVL", map.get("EVL").deci));
                txt_EVT_Pregunta.setText(bod.getVarFormateadaPorNombre("EVT", map.get("EVT").deci));
                txt_EVH_Pregunta.setText(bod.getVarFormateadaPorNombre("EVH", map.get("EVH").deci));
                txt_EPT_Pregunta.setText(bod.getVarFormateadaPorNombre("EPT", map.get("EPT").deci));
                txt_EAS_Pregunta.setText(bod.getVarFormateadaPorNombre("EAS", map.get("EAS").deci));
                txt_EWT_Pregunta.setText(bod.getVarFormateadaPorNombre("EWT", map.get("EWT").deci));
                txt_EHT_Pregunta.setText(bod.getVarFormateadaPorNombre("EHT", map.get("EHT").deci));
                txt_EAM_Pregunta.setText(bod.getVarFormateadaPorNombre("EAM", map.get("EAM").deci));
                txt_ERM_Pregunta.setText(bod.getVarFormateadaPorNombre("ERM", map.get("ERM").deci));
                txt_EEC_Pregunta.setText(bod.getVarFormateadaPorNombre("EEC", map.get("EEC").deci));
                txt_EFS_Pregunta.setText(bod.getVarFormateadaPorNombre("EFS", map.get("EFS").deci));
                txt_ESA_Pregunta.setText(bod.getVarFormateadaPorNombre("ESA", map.get("ESA").deci));
                txt_EMS_Pregunta.setText(bod.getVarFormateadaPorNombre("EMS", map.get("EMS").deci));
                txt_EHS_Pregunta.setText(bod.getVarFormateadaPorNombre("EHS", map.get("EHS").deci));
                txt_ESD_Pregunta.setText(bod.getVarFormateadaPorNombre("ESD", map.get("ESD").deci));
                txt_EAT_Pregunta.setText(bod.getVarFormateadaPorNombre("EAT", map.get("EAT").deci));

            } else {
                ejecutarFunciones("MCH");
                ejecutarFunciones("MPS");
                ejecutarFunciones("EDB");
                ejecutarFunciones("ESE");
                ejecutarFunciones("ERD");
                ejecutarFunciones("EST");
                ejecutarFunciones("ESR");
                ejecutarFunciones("ECC");
                ejecutarFunciones("ETE");
                ejecutarFunciones("EEL");
                ejecutarFunciones("EPT");
                ejecutarFunciones("EFS");
                ejecutarFunciones("EHS");
                borradoMsgError();
            }
            eSave = true;
        } catch (Exception ex) {
            log.error("Error en InicializarComponentes " + ex.getMessage() + " " + ex.getCause());
            ErrorGeneral(); // Ha ocurrido algun error en los datos o conexión, se debe revisar log.            
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }

    private void borradoMsgError() {
        lbl_EAM_error.setText("");
        lbl_EAS_error.setText("");
        lbl_EAT_error.setText("");
        lbl_ECC_error.setText("");
        lbl_ECE_error.setText("");
        lbl_EDB_error.setText("");
        lbl_EDP_error.setText("");
        lbl_EDS_error.setText("");
        lbl_EEC_error.setText("");
        lbl_EED_error.setText("");
        lbl_EEL_error.setText("");
        lbl_EES_error.setText("");
        lbl_EFS_error.setText("");
        lbl_EHS_error.setText("");
        lbl_EHT_error.setText("");
        lbl_ELT_error.setText("");
        lbl_EMS_error.setText("");
        lbl_EPL_error.setText("");
        lbl_EPT_error.setText("");
        lbl_ERD_error.setText("");
        lbl_ERM_error.setText("");
        lbl_ESA_error.setText("");
        lbl_ESD_error.setText("");
        lbl_ESE_error.setText("");
        lbl_ESR_error.setText("");
        lbl_EST_error.setText("");
        lbl_ETE_error.setText("");
        lbl_EVH_error.setText("");
        lbl_EVL_error.setText("");
        lbl_EVT_error.setText("");
        lbl_EWT_error.setText("");
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

        if (!CalcularEDB()) {
            lbl_save_error.setText(map.get("EDB").erro);
            lbl_save_error2.setText(map.get("EDB").tit1 + " . " + map.get("EDB").desc);
            return false;
        }

        if (!CalcularESE()) {
            lbl_save_error.setText(map.get("ESE").erro);
            lbl_save_error2.setText(map.get("ESE").tit1 + " . " + map.get("ESE").desc);
            return false;
        }

        if (!CalcularERD()) {
            lbl_save_error.setText(map.get("ERD").erro);
            lbl_save_error2.setText(map.get("ERD").tit1 + " . " + map.get("ERD").desc);
            return false;
        }

        if (!CalcularEST()) {
            lbl_save_error.setText(map.get("EST").erro);
            lbl_save_error2.setText(map.get("EST").tit1 + " . " + map.get("EST").desc);
            return false;
        }

        if (!CalcularESR()) {
            lbl_save_error.setText(map.get("ESR").erro);
            lbl_save_error2.setText(map.get("ESR").tit1 + " . " + map.get("ESR").desc);
            return false;
        }

        if (!CalcularECC()) {
            lbl_save_error.setText(map.get("ECC").erro);
            lbl_save_error2.setText(map.get("ECC").tit1 + " . " + map.get("ECC").desc);
            return false;
        }

        if (!CalcularETE()) {
            lbl_save_error.setText(map.get("ETE").erro);
            lbl_save_error2.setText(map.get("ETE").tit1 + " . " + map.get("ETE").desc);
            return false;
        }

        if (!CalcularEEL()) {
            lbl_save_error.setText(map.get("EEL").erro);
            lbl_save_error2.setText(map.get("EEL").tit1 + " . " + map.get("EEL").desc);
            return false;
        }

        if (!CalcularECE()) {
            lbl_save_error.setText(map.get("ECE").erro);
            lbl_save_error2.setText(map.get("ECE").tit1 + " . " + map.get("ECE").desc);
            return false;
        }

        if (!CalcularEDP()) {
            lbl_save_error.setText(map.get("EDP").erro);
            lbl_save_error2.setText(map.get("EDP").tit1 + " . " + map.get("EDP").desc);
            return false;
        }

        if (!CalcularEDS()) {
            lbl_save_error.setText(map.get("EDS").erro);
            lbl_save_error2.setText(map.get("EDS").tit1 + " . " + map.get("EDS").desc);
            return false;
        }

        if (!CalcularEES()) {
            lbl_save_error.setText(map.get("EES").erro);
            lbl_save_error2.setText(map.get("EES").tit1 + " . " + map.get("EES").desc);
            return false;
        }

        if (!CalcularEED()) {
            lbl_save_error.setText(map.get("EED").erro);
            lbl_save_error2.setText(map.get("EED").tit1 + " . " + map.get("EED").desc);
            return false;
        }

        if (!CalcularEPL()) {
            lbl_save_error.setText(map.get("EPL").erro);
            lbl_save_error2.setText(map.get("EPL").tit1 + " . " + map.get("EPL").desc);
            return false;
        }

        if (!CalcularELT()) {
            lbl_save_error.setText(map.get("ELT").erro);
            lbl_save_error2.setText(map.get("ELT").tit1 + " . " + map.get("ELT").desc);
            return false;
        }

        if (!CalcularEVL()) {
            lbl_save_error.setText(map.get("EVL").erro);
            lbl_save_error2.setText(map.get("EVL").tit1 + " . " + map.get("EVL").desc);
            return false;
        }

        if (!CalcularEVT()) {
            lbl_save_error.setText(map.get("EVT").erro);
            lbl_save_error2.setText(map.get("EVT").tit1 + " . " + map.get("EVT").desc);
            return false;
        }

        if (!CalcularEVH()) {
            lbl_save_error.setText(map.get("EVH").erro);
            lbl_save_error2.setText(map.get("EVH").tit1 + " . " + map.get("EVH").desc);
            return false;
        }  
        
        if (CalcularEVHC()) {
            return false;
        }

        if (!CalcularEPT()) {
            lbl_save_error.setText(map.get("EPT").erro);
            lbl_save_error2.setText(map.get("EPT").tit1 + " . " + map.get("EPT").desc);
            return false;
        }

        if (!CalcularEAS()) {
            lbl_save_error.setText(map.get("EAS").erro);
            lbl_save_error2.setText(map.get("EAS").tit1 + " . " + map.get("EAS").desc);
            return false;
        }

        if (!CalcularEWT()) {
            lbl_save_error.setText(map.get("EWT").erro);
            lbl_save_error2.setText(map.get("EWT").tit1 + " . " + map.get("EWT").desc);
            return false;
        }

        if (!CalcularEHT()) {
            lbl_save_error.setText(map.get("EHT").erro);
            lbl_save_error2.setText(map.get("EHT").tit1 + " . " + map.get("EHT").desc);
            return false;
        }

        if (!CalcularEAM()) {
            lbl_save_error.setText(map.get("EAM").erro);
            lbl_save_error2.setText(map.get("EAM").tit1 + " . " + map.get("EAM").desc);
            return false;
        }

        if (!CalcularERM()) {
            lbl_save_error.setText(map.get("ERM").erro);
            lbl_save_error2.setText(map.get("ERM").tit1 + " . " + map.get("ERM").desc);
            return false;
        }

        if (!CalcularEEC()) {
            lbl_save_error.setText(map.get("EEC").erro);
            lbl_save_error2.setText(map.get("EEC").tit1 + " . " + map.get("EEC").desc);
            return false;
        }

        if (!CalcularEFS()) {
            lbl_save_error.setText(map.get("EFS").erro);
            lbl_save_error2.setText(map.get("EFS").tit1 + " . " + map.get("EFS").desc);
            return false;
        }

        if (!CalcularESA()) {
            lbl_save_error.setText(map.get("ESA").erro);
            lbl_save_error2.setText(map.get("ESA").tit1 + " . " + map.get("ESA").desc);
            return false;
        }

        if (!CalcularEMS()) {
            lbl_save_error.setText(map.get("EMS").erro);
            lbl_save_error2.setText(map.get("EMS").tit1 + " . " + map.get("EMS").desc);
            return false;
        }

        if (!CalcularEHS()) {
            lbl_save_error.setText(map.get("EHS").erro);
            lbl_save_error2.setText(map.get("EHS").tit1 + " . " + map.get("EHS").desc);
            return false;
        }

        if (!CalcularESD()) {
            lbl_save_error.setText(map.get("ESD").erro);
            lbl_save_error2.setText(map.get("ESD").tit1 + " . " + map.get("ESD").desc);
            return false;
        }

        if (!CalcularEAT()) {
            lbl_save_error.setText(map.get("EAT").erro);
            lbl_save_error2.setText(map.get("EAT").tit1 + " . " + map.get("EAT").desc);
            return false;
        }

        bod.EditLodosActivadsAireacExt = true;
        Main main = (Main) this.getTopLevelAncestor();
        main.vbod = this.bod;

        if (!bod.exportarProyecto(true)) {
            bod.EditLodosActivadsAireacExt = false;
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

    private boolean CalcularEDB() {

        try {
            lbl_EDB_error.setText("");

            if (bod.setVarDob(txt_EDB_Pregunta.getText(), "EDB", map.get("EDB").rmin, map.get("EDB").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularEDB " + ex.getMessage());
        }
        lbl_EDB_error.setText(map.get("EDB").erro);
        return false;
    }

    private boolean CalcularESE() {

        try {
            lbl_ESE_error.setText("");

            if (bod.setVarDob(txt_ESE_Pregunta.getText(), "ESE", map.get("ESE").rmin, map.get("ESE").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularESE " + ex.getMessage());
        }
        lbl_ESE_error.setText(map.get("ESE").erro);
        return false;
    }

    private boolean CalcularERD() {

        try {
            lbl_ERD_error.setText("");

            if (bod.setVarDob(txt_ERD_Pregunta.getText(), "ERD", map.get("ERD").rmin, map.get("ERD").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularERD " + ex.getMessage());
        }
        lbl_ERD_error.setText(map.get("ERD").erro);
        return false;
    }

    private boolean CalcularEST() {

        try {
            lbl_EST_error.setText("");

            if (bod.setVarDob(txt_EST_Pregunta.getText(), "EST", map.get("EST").rmin, map.get("EST").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularEST " + ex.getMessage());
        }
        lbl_EST_error.setText(map.get("EST").erro);
        return false;
    }

    private boolean CalcularESR() {

        try {
            lbl_ESR_error.setText("");

            if (bod.setVarDob(txt_ESR_Pregunta.getText(), "ESR", map.get("ESR").rmin, map.get("ESR").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularESR " + ex.getMessage());
        }
        lbl_ESR_error.setText(map.get("ESR").erro);
        return false;
    }

    private boolean CalcularECC() {

        try {
            lbl_ECC_error.setText("");

            if (bod.setVarDob(txt_ECC_Pregunta.getText(), "ECC", map.get("ECC").rmin, map.get("ECC").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularECC " + ex.getMessage());
        }
        lbl_ECC_error.setText(map.get("ECC").erro);
        return false;
    }

    private boolean CalcularETE() {

        try {
            lbl_ETE_error.setText("");

            if (bod.setVarDob(txt_ETE_Pregunta.getText(), "ETE", map.get("ETE").rmin, map.get("ETE").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularETE " + ex.getMessage());
        }
        lbl_ETE_error.setText(map.get("ETE").erro);
        return false;
    }

    private boolean CalcularEEL() {

        try {
            lbl_EEL_error.setText("");

            if (bod.setVarDob(txt_EEL_Pregunta.getText(), "EEL", map.get("EEL").rmin, map.get("EEL").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularEEL " + ex.getMessage());
        }
        lbl_EEL_error.setText(map.get("EEL").erro);
        return false;
    }

    private boolean CalcularECE() {

        try {
            lbl_ECE_error.setText("");
            double x = bod.calcularECE();

            if (bod.setVarDob(x, "ECE", map.get("ECE").rmin, map.get("ECE").rmax)) {
                txt_ECE_Pregunta.setText(bod.getVarFormateadaPorNombre("ECE", map.get("ECE").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularECE " + ex.getMessage());
        }
        lbl_ECE_error.setText(map.get("ECE").erro);
        return false;
    }

    private boolean CalcularEDP() {

        try {
            lbl_EDP_error.setText("");
            double x = bod.calcularEDP();

            if (bod.setVarDob(x, "EDP", map.get("EDP").rmin, map.get("EDP").rmax)) {
                txt_EDP_Pregunta.setText(bod.getVarFormateadaPorNombre("EDP", map.get("EDP").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularEDP " + ex.getMessage());
        }
        lbl_EDP_error.setText(map.get("EDP").erro);
        return false;
    }

    private boolean CalcularEDS() {

        try {
            lbl_EDS_error.setText("");
            double x = bod.calcularEDS();

            if (bod.setVarDob(x, "EDS", map.get("EDS").rmin, map.get("EDS").rmax)) {
                txt_EDS_Pregunta.setText(bod.getVarFormateadaPorNombre("EDS", map.get("EDS").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularEDS " + ex.getMessage());
        }
        lbl_EDS_error.setText(map.get("EDS").erro);
        return false;
    }

    private boolean CalcularEES() {

        try {
            lbl_EES_error.setText("");
            double x = bod.calcularEES();

            if (bod.setVarDob(x, "EES", map.get("EES").rmin, map.get("EES").rmax)) {
                txt_EES_Pregunta.setText(bod.getVarFormateadaPorNombre("EES", map.get("EES").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularEES " + ex.getMessage());
        }
        lbl_EES_error.setText(map.get("EES").erro);
        return false;
    }

    private boolean CalcularEED() {

        try {
            lbl_EED_error.setText("");
            double x = bod.calcularEED();

            if (bod.setVarDob(x, "EED", map.get("EED").rmin, map.get("EED").rmax)) {
                txt_EED_Pregunta.setText(bod.getVarFormateadaPorNombre("EED", map.get("EED").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularEED " + ex.getMessage());
        }
        lbl_EED_error.setText(map.get("EED").erro);
        return false;
    }

    private boolean CalcularEPL() {

        try {
            lbl_EPL_error.setText("");
            double x = bod.calcularEPL();

            if (bod.setVarDob(x, "EPL", map.get("EPL").rmin, map.get("EPL").rmax)) {
                txt_EPL_Pregunta.setText(bod.getVarFormateadaPorNombre("EPL", map.get("EPL").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularEPL " + ex.getMessage());
        }
        lbl_EPL_error.setText(map.get("EPL").erro);
        return false;
    }

    private boolean CalcularELT() {

        try {
            lbl_ELT_error.setText("");
            double x = bod.calcularELT();

            if (bod.setVarDob(x, "ELT", map.get("ELT").rmin, map.get("ELT").rmax)) {
                txt_ELT_Pregunta.setText(bod.getVarFormateadaPorNombre("ELT", map.get("ELT").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularELT " + ex.getMessage());
        }
        lbl_ELT_error.setText(map.get("ELT").erro);
        return false;
    }

    private boolean CalcularEVL() {

        try {
            lbl_EVL_error.setText("");
            double x = bod.calcularEVL();

            if (bod.setVarDob(x, "EVL", map.get("EVL").rmin, map.get("EVL").rmax)) {
                txt_EVL_Pregunta.setText(bod.getVarFormateadaPorNombre("EVL", map.get("EVL").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularEVL " + ex.getMessage());
        }
        lbl_EVL_error.setText(map.get("EVL").erro);
        return false;
    }

    private boolean CalcularEVT() {

        try {
            lbl_EVT_error.setText("");
            double x = bod.calcularEVT();

            if (bod.setVarDob(x, "EVT", map.get("EVT").rmin, map.get("EVT").rmax)) {
                txt_EVT_Pregunta.setText(bod.getVarFormateadaPorNombre("EVT", map.get("EVT").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularEVT " + ex.getMessage());
        }
        lbl_EVT_error.setText(map.get("EVT").erro);
        return false;
    }

    private boolean CalcularEVH() {

        try {
            lbl_EVH_error.setText("");
            double x = bod.calcularEVH();

            if (bod.setVarDob(x, "EVH", map.get("EVH").rmin, map.get("EVH").rmax)) {
                txt_EVH_Pregunta.setText(bod.getVarFormateadaPorNombre("EVH", map.get("EVH").deci));
                // CalcularEVHC();
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularEVH " + ex.getMessage());
        }
        lbl_EVH_error.setText(map.get("EVH").erro);
        return false;
    }

    private boolean CalcularEVHC() {

        try {
            String x = bod.calcularEVHC();

            if (!x.trim().isEmpty()) {
                int n = util.Mensaje(x, "yesno");
                if (n == JOptionPane.YES_OPTION) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception ex) {
            log.error("Error: CalcularEVHC " + ex.getMessage());
        }
        return false;
    }

    private boolean CalcularEPT() {

        try {
            lbl_EPT_error.setText("");

            if (bod.setVarDob(txt_EPT_Pregunta.getText(), "EPT", map.get("EPT").rmin, map.get("EPT").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularEPT " + ex.getMessage());
        }
        lbl_EPT_error.setText(map.get("EPT").erro);
        return false;
    }

    private boolean CalcularEAS() {

        try {
            lbl_EAS_error.setText("");
            double x = bod.calcularEAS();

            if (bod.setVarDob(x, "EAS", map.get("EAS").rmin, map.get("EAS").rmax)) {
                txt_EAS_Pregunta.setText(bod.getVarFormateadaPorNombre("EAS", map.get("EAS").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularEAS " + ex.getMessage());
        }
        lbl_EAS_error.setText(map.get("EAS").erro);
        return false;
    }

    private boolean CalcularEWT() {

        try {
            lbl_EWT_error.setText("");
            double x = bod.calcularEWT();

            if (bod.setVarDob(x, "EWT", map.get("EWT").rmin, map.get("EWT").rmax)) {
                txt_EWT_Pregunta.setText(bod.getVarFormateadaPorNombre("EWT", map.get("EWT").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularEWT " + ex.getMessage());
        }
        lbl_EWT_error.setText(map.get("EWT").erro);
        return false;
    }

    private boolean CalcularEHT() {

        try {
            lbl_EHT_error.setText("");
            double x = bod.calcularEHT();

            if (bod.setVarDob(x, "EHT", map.get("EHT").rmin, map.get("EHT").rmax)) {
                txt_EHT_Pregunta.setText(bod.getVarFormateadaPorNombre("EHT", map.get("EHT").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularEHT " + ex.getMessage());
        }
        lbl_EHT_error.setText(map.get("EHT").erro);
        return false;
    }

    private boolean CalcularEAM() {

        try {
            lbl_EAM_error.setText("");
            double x = bod.calcularEAM();

            if (bod.setVarDob(x, "EAM", map.get("EAM").rmin, map.get("EAM").rmax)) {
                txt_EAM_Pregunta.setText(bod.getVarFormateadaPorNombre("EAM", map.get("EAM").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularEAM " + ex.getMessage());
        }
        lbl_EAM_error.setText(map.get("EAM").erro);
        return false;
    }

    private boolean CalcularERM() {

        try {
            lbl_ERM_error.setText("");
            double x = bod.calcularERM();

            if (bod.setVarDob(x, "ERM", map.get("ERM").rmin, map.get("ERM").rmax)) {
                txt_ERM_Pregunta.setText(bod.getVarFormateadaPorNombre("ERM", map.get("ERM").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularERM " + ex.getMessage());
        }
        lbl_ERM_error.setText(map.get("ERM").erro);
        return false;
    }

    private boolean CalcularEEC() {

        try {
            lbl_EEC_error.setText("");
            double x = bod.calcularEEC();

            if (bod.setVarDob(x, "EEC", map.get("EEC").rmin, map.get("EEC").rmax)) {
                txt_EEC_Pregunta.setText(bod.getVarFormateadaPorNombre("EEC", map.get("EEC").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularEEC " + ex.getMessage());
        }
        lbl_EEC_error.setText(map.get("EEC").erro);
        return false;
    }

    private boolean CalcularEFS() {

        try {
            lbl_EFS_error.setText("");

            if (bod.setVarDob(txt_EFS_Pregunta.getText(), "EFS", map.get("EFS").rmin, map.get("EFS").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularEFS " + ex.getMessage());
        }
        lbl_EFS_error.setText(map.get("EFS").erro);
        return false;
    }

    private boolean CalcularESA() {

        try {
            lbl_ESA_error.setText("");
            double x = bod.calcularESA();

            if (bod.setVarDob(x, "ESA", map.get("ESA").rmin, map.get("ESA").rmax)) {
                txt_ESA_Pregunta.setText(bod.getVarFormateadaPorNombre("ESA", map.get("ESA").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularESA " + ex.getMessage());
        }
        lbl_ESA_error.setText(map.get("ESA").erro);
        return false;
    }

    private boolean CalcularEMS() {

        try {
            lbl_EMS_error.setText("");
            double x = bod.calcularEMS();

            if (bod.setVarDob(x, "EMS", map.get("EMS").rmin, map.get("EMS").rmax)) {
                txt_EMS_Pregunta.setText(bod.getVarFormateadaPorNombre("EMS", map.get("EMS").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularEMS " + ex.getMessage());
        }
        lbl_EMS_error.setText(map.get("EMS").erro);
        return false;
    }

    private boolean CalcularEHS() {

        try {
            lbl_EHS_error.setText("");

            if (bod.setVarDob(txt_EHS_Pregunta.getText(), "EHS", map.get("EHS").rmin, map.get("EHS").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularEHS " + ex.getMessage());
        }
        lbl_EHS_error.setText(map.get("EHS").erro);
        return false;
    }

    private boolean CalcularESD() {

        try {
            lbl_ESD_error.setText("");
            double x = bod.calcularESD();

            if (bod.setVarDob(x, "ESD", map.get("ESD").rmin, map.get("ESD").rmax)) {
                txt_ESD_Pregunta.setText(bod.getVarFormateadaPorNombre("ESD", map.get("ESD").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularESD " + ex.getMessage());
        }
        lbl_ESD_error.setText(map.get("ESD").erro);
        return false;
    }

    private boolean CalcularEAT() {

        try {
            lbl_EAT_error.setText("");
            double x = bod.calcularEAT();

            if (bod.setVarDob(x, "EAT", map.get("EAT").rmin, map.get("EAT").rmax)) {
                txt_EAT_Pregunta.setText(bod.getVarFormateadaPorNombre("EAT", map.get("EAT").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularEAT " + ex.getMessage());
        }
        lbl_EAT_error.setText(map.get("EAT").erro);
        return false;
    }

    /**
     * Ejecuta ciertas funciones por parametro de nombre, la funcion que se
     * ejecuta desencadena otras funciones y a su vez estas pueden hacer lo
     * mismo
     *
     * @param func : Nombre de la variable de la función
     */
    private void ejecutarFunciones(String func) {
        eSave = false;
        switch (func) {
            case "EDB":
                CalcularEDB();
                ejecutarFunciones("EDS");
                ejecutarFunciones("EED");
                break;

            case "ESE":
                CalcularESE();
                ejecutarFunciones("EDP");
                break;

            case "ERD":
                CalcularERD();
                ejecutarFunciones("EDP");
                break;

            case "EST":
                CalcularEST();
                ejecutarFunciones("EVT");
                ejecutarFunciones("ERM");
                break;

            case "ESR":
                CalcularESR();
                ejecutarFunciones("ERM");
                break;

            case "ECC":
                CalcularECC();
                ejecutarFunciones("ECE");
                ejecutarFunciones("EVT");
                break;

            case "ETE":
                CalcularETE();
                ejecutarFunciones("ECE");
                ejecutarFunciones("EVT");
                break;

            case "EEL":
                CalcularEEL();
                ejecutarFunciones("ECE");
                ejecutarFunciones("EVT");

                break;
            case "ECE":
                CalcularECE();
                ejecutarFunciones("EPL");
                break;

            case "EDP":
                CalcularEDP();
                ejecutarFunciones("EDS");
                break;

            case "EDS":
                CalcularEDS();
                ejecutarFunciones("EES");
                ejecutarFunciones("EPL");
                ejecutarFunciones("EVT");
                break;

            case "EES":
                CalcularEES();

                break;
            case "EED":
                CalcularEED();
                break;

            case "EPL":
                CalcularEPL();
                ejecutarFunciones("ELT");
                break;

            case "ELT":
                CalcularELT();
                ejecutarFunciones("EVL");
                break;

            case "EVL":
                CalcularEVL();
                break;

            case "EVT":
                CalcularEVT();
                ejecutarFunciones("EVH");
                ejecutarFunciones("EAS");
                ejecutarFunciones("EAM");
                ejecutarFunciones("EEC");
                break;

            case "EVH":
                CalcularEVH();
                break;

            case "EPT":
                CalcularEPT();
                ejecutarFunciones("EAS");
                break;

            case "EAS":
                CalcularEAS();
                ejecutarFunciones("EAT");
                CalcularEWT();
                CalcularEHT();
                break;

            case "EAM":
                CalcularEAM();
                break;

            case "ERM":
                CalcularERM();
                ejecutarFunciones("EMS");
                break;

            case "EEC":
                CalcularEEC();
                break;

            case "EFS":
                CalcularEFS();
                ejecutarFunciones("ESA");
                break;

            case "ESA":
                CalcularESA();
                ejecutarFunciones("EAT");
                CalcularESD();
                break;

            case "EMS":
                CalcularEMS();
                break;

            case "EHS":
                CalcularEHS();
                break;

            case "EAT":
                CalcularEAT();
                break;

            default:
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
        bod.WinLodosActivadsAireacExt = false;
        Main main = (Main) this.getTopLevelAncestor();
        main.bod.WinLodosActivadsAireacExt = false;
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jsp_LodosActivadsAireacExt = new javax.swing.JScrollPane();
        jp_Componentes = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        lbl_titulo1 = new javax.swing.JLabel();
        lbl_titulo2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        btn_guardar = new javax.swing.JButton();
        lbl_Q2C_titulo2 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        btn_close = new javax.swing.JButton();
        lbl_save_error = new javax.swing.JLabel();
        lbl_save_error2 = new javax.swing.JLabel();
        txt_Q2C_Pregunta = new javax.swing.JTextField();
        lbl_Q2C_titulo1 = new javax.swing.JLabel();
        lbl_CABQ_titulo2 = new javax.swing.JLabel();
        txt_CABQ_Pregunta = new javax.swing.JTextField();
        lbl_CABQ_titulo1 = new javax.swing.JLabel();
        lbl_CVS_titulo2 = new javax.swing.JLabel();
        txt_CVS_Pregunta = new javax.swing.JTextField();
        lbl_CVS_titulo1 = new javax.swing.JLabel();
        lbl_EDB_error = new javax.swing.JLabel();
        lbl_EDB_titulo2 = new javax.swing.JLabel();
        txt_EDB_Pregunta = new javax.swing.JTextField();
        lbl_EDB_titulo1 = new javax.swing.JLabel();
        lbl_ESE_error = new javax.swing.JLabel();
        lbl_ESE_titulo2 = new javax.swing.JLabel();
        txt_ESE_Pregunta = new javax.swing.JTextField();
        lbl_ESE_titulo1 = new javax.swing.JLabel();
        btn_ESE_ayuda = new javax.swing.JButton();
        lbl_ERD_error = new javax.swing.JLabel();
        lbl_ERD_titulo2 = new javax.swing.JLabel();
        txt_ERD_Pregunta = new javax.swing.JTextField();
        lbl_ERD_titulo1 = new javax.swing.JLabel();
        btn_EDB_ayuda = new javax.swing.JButton();
        lbl_EST_error = new javax.swing.JLabel();
        lbl_EST_titulo2 = new javax.swing.JLabel();
        txt_EST_Pregunta = new javax.swing.JTextField();
        lbl_EST_titulo1 = new javax.swing.JLabel();
        lbl_ESR_error = new javax.swing.JLabel();
        lbl_ESR_titulo2 = new javax.swing.JLabel();
        txt_ESR_Pregunta = new javax.swing.JTextField();
        lbl_ESR_titulo1 = new javax.swing.JLabel();
        btn_EST_ayuda = new javax.swing.JButton();
        lbl_ECC_error = new javax.swing.JLabel();
        lbl_ECC_titulo2 = new javax.swing.JLabel();
        txt_ECC_Pregunta = new javax.swing.JTextField();
        lbl_ECC_titulo1 = new javax.swing.JLabel();
        btn_ECC_ayuda = new javax.swing.JButton();
        lbl_ETE_error = new javax.swing.JLabel();
        lbl_ETE_titulo2 = new javax.swing.JLabel();
        txt_ETE_Pregunta = new javax.swing.JTextField();
        lbl_ETE_titulo1 = new javax.swing.JLabel();
        btn_ETE_ayuda = new javax.swing.JButton();
        jSeparator12 = new javax.swing.JSeparator();
        lbl_EEL_error = new javax.swing.JLabel();
        lbl_EEL_titulo2 = new javax.swing.JLabel();
        txt_EEL_Pregunta = new javax.swing.JTextField();
        lbl_EEL_titulo1 = new javax.swing.JLabel();
        btn_EEL_ayuda = new javax.swing.JButton();
        lbl_ECE_error = new javax.swing.JLabel();
        lbl_ECE_titulo2 = new javax.swing.JLabel();
        txt_ECE_Pregunta = new javax.swing.JTextField();
        lbl_ECE_titulo1 = new javax.swing.JLabel();
        btn_ECE_ayuda = new javax.swing.JButton();
        lbl_EDP_desc = new javax.swing.JLabel();
        lbl_EDP_error = new javax.swing.JLabel();
        lbl_EDP_titulo2 = new javax.swing.JLabel();
        txt_EDP_Pregunta = new javax.swing.JTextField();
        lbl_EDP_titulo1 = new javax.swing.JLabel();
        btn_EDP_ayuda = new javax.swing.JButton();
        lbl_EDS_error = new javax.swing.JLabel();
        lbl_EDS_titulo2 = new javax.swing.JLabel();
        txt_EDS_Pregunta = new javax.swing.JTextField();
        lbl_EDS_titulo1 = new javax.swing.JLabel();
        jSeparator14 = new javax.swing.JSeparator();
        lbl_EES_error = new javax.swing.JLabel();
        lbl_EES_titulo2 = new javax.swing.JLabel();
        txt_EES_Pregunta = new javax.swing.JTextField();
        lbl_EES_titulo1 = new javax.swing.JLabel();
        lbl_EED_error = new javax.swing.JLabel();
        lbl_EED_titulo2 = new javax.swing.JLabel();
        txt_EED_Pregunta = new javax.swing.JTextField();
        lbl_EED_titulo1 = new javax.swing.JLabel();
        jSeparator15 = new javax.swing.JSeparator();
        lbl_EPL_error = new javax.swing.JLabel();
        lbl_EPL_titulo2 = new javax.swing.JLabel();
        txt_EPL_Pregunta = new javax.swing.JTextField();
        lbl_EPL_titulo1 = new javax.swing.JLabel();
        btn_EPL_ayuda = new javax.swing.JButton();
        lbl_ELT_error = new javax.swing.JLabel();
        lbl_ELT_titulo2 = new javax.swing.JLabel();
        txt_ELT_Pregunta = new javax.swing.JTextField();
        lbl_ELT_titulo1 = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        lbl_EVL_error = new javax.swing.JLabel();
        lbl_EVL_titulo2 = new javax.swing.JLabel();
        txt_EVL_Pregunta = new javax.swing.JTextField();
        lbl_EVL_titulo1 = new javax.swing.JLabel();
        lbl_EVT_error = new javax.swing.JLabel();
        lbl_EVT_titulo2 = new javax.swing.JLabel();
        txt_EVT_Pregunta = new javax.swing.JTextField();
        lbl_EVT_titulo1 = new javax.swing.JLabel();
        btn_EVT_ayuda = new javax.swing.JButton();
        lbl_EVH_error = new javax.swing.JLabel();
        lbl_EVH_titulo2 = new javax.swing.JLabel();
        txt_EVH_Pregunta = new javax.swing.JTextField();
        lbl_EVH_titulo1 = new javax.swing.JLabel();
        btn_EVH_ayuda = new javax.swing.JButton();
        lbl_EPT_error = new javax.swing.JLabel();
        lbl_EPT_titulo2 = new javax.swing.JLabel();
        txt_EPT_Pregunta = new javax.swing.JTextField();
        lbl_EPT_titulo1 = new javax.swing.JLabel();
        lbl_EAS_error = new javax.swing.JLabel();
        lbl_EAS_titulo2 = new javax.swing.JLabel();
        txt_EAS_Pregunta = new javax.swing.JTextField();
        lbl_EAS_titulo1 = new javax.swing.JLabel();
        btn_EPT_ayuda = new javax.swing.JButton();
        jSeparator16 = new javax.swing.JSeparator();
        lbl_EPT_desc = new javax.swing.JLabel();
        jSeparator17 = new javax.swing.JSeparator();
        lbl_ERM_error = new javax.swing.JLabel();
        lbl_ERM_titulo2 = new javax.swing.JLabel();
        txt_ERM_Pregunta = new javax.swing.JTextField();
        lbl_ERM_titulo1 = new javax.swing.JLabel();
        lbl_EEC_error = new javax.swing.JLabel();
        lbl_EEC_titulo2 = new javax.swing.JLabel();
        txt_EEC_Pregunta = new javax.swing.JTextField();
        lbl_EEC_titulo1 = new javax.swing.JLabel();
        btn_EEC_ayuda = new javax.swing.JButton();
        lbl_EFS_error = new javax.swing.JLabel();
        lbl_EFS_titulo2 = new javax.swing.JLabel();
        txt_EFS_Pregunta = new javax.swing.JTextField();
        lbl_EFS_titulo1 = new javax.swing.JLabel();
        btn_EFS_ayuda = new javax.swing.JButton();
        lbl_ESA_error = new javax.swing.JLabel();
        lbl_ESA_titulo2 = new javax.swing.JLabel();
        txt_ESA_Pregunta = new javax.swing.JTextField();
        lbl_ESA_titulo1 = new javax.swing.JLabel();
        btn_ESA_ayuda = new javax.swing.JButton();
        lbl_EAM_error = new javax.swing.JLabel();
        lbl_EAM_titulo2 = new javax.swing.JLabel();
        txt_EAM_Pregunta = new javax.swing.JTextField();
        lbl_EAM_titulo1 = new javax.swing.JLabel();
        btn_ERM_ayuda = new javax.swing.JButton();
        jSeparator18 = new javax.swing.JSeparator();
        lbl_EMS_error = new javax.swing.JLabel();
        lbl_EMS_titulo2 = new javax.swing.JLabel();
        txt_EMS_Pregunta = new javax.swing.JTextField();
        lbl_EMS_titulo1 = new javax.swing.JLabel();
        btn_EMS_ayuda = new javax.swing.JButton();
        lbl_EHS_error = new javax.swing.JLabel();
        lbl_EHS_titulo2 = new javax.swing.JLabel();
        txt_EHS_Pregunta = new javax.swing.JTextField();
        lbl_EHS_titulo1 = new javax.swing.JLabel();
        btn_EHS_ayuda = new javax.swing.JButton();
        lbl_EMS_desc = new javax.swing.JLabel();
        lbl_EAT_error = new javax.swing.JLabel();
        lbl_EAT_titulo2 = new javax.swing.JLabel();
        txt_EAT_Pregunta = new javax.swing.JTextField();
        lbl_EAT_titulo1 = new javax.swing.JLabel();
        btn_EVL_ayuda = new javax.swing.JButton();
        lbl_EDB_desc = new javax.swing.JLabel();
        btn_ESR_ayuda = new javax.swing.JButton();
        jSeparator19 = new javax.swing.JSeparator();
        lbl_EVT_desc = new javax.swing.JLabel();
        btn_EPT_ayuda1 = new javax.swing.JButton();
        btn_EAM_ayuda = new javax.swing.JButton();
        lbl_EFS_desc = new javax.swing.JLabel();
        lbl_EWT_error = new javax.swing.JLabel();
        lbl_EWT_titulo2 = new javax.swing.JLabel();
        txt_EWT_Pregunta = new javax.swing.JTextField();
        lbl_EWT_titulo1 = new javax.swing.JLabel();
        lbl_EHT_error = new javax.swing.JLabel();
        lbl_EHT_titulo2 = new javax.swing.JLabel();
        txt_EHT_Pregunta = new javax.swing.JTextField();
        lbl_EHT_titulo1 = new javax.swing.JLabel();
        lbl_ESD_error = new javax.swing.JLabel();
        lbl_ESD_titulo2 = new javax.swing.JLabel();
        txt_ESD_Pregunta = new javax.swing.JTextField();
        lbl_ESD_titulo1 = new javax.swing.JLabel();
        btn_close2 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1024, 2300));

        jsp_LodosActivadsAireacExt.setPreferredSize(new java.awt.Dimension(1024, 768));

        jp_Componentes.setPreferredSize(new java.awt.Dimension(1024, 2000));
        jp_Componentes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jp_Componentes.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 1024, 2));

        lbl_titulo1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_titulo1.setForeground(new java.awt.Color(51, 51, 51));
        lbl_titulo1.setText("Titulo");
        jp_Componentes.add(lbl_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 300, 30));

        lbl_titulo2.setForeground(new java.awt.Color(0, 51, 102));
        lbl_titulo2.setText("Desc");
        jp_Componentes.add(lbl_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, 640, 125));
        jp_Componentes.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 282, 1024, 2));

        btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Button_Salva_Guardar_21.png"))); // NOI18N
        btn_guardar.setText("save");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1940, 120, -1));

        lbl_Q2C_titulo2.setText("Titulo");
        lbl_Q2C_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2C_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 160, 80, 25));
        jp_Componentes.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 467, 1024, 0));
        jp_Componentes.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1930, 1024, -1));

        btn_close.setText("Salir");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1980, 120, 30));

        lbl_save_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error.setText(" . . .");
        jp_Componentes.add(lbl_save_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 1940, 400, 35));

        lbl_save_error2.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error2.setText(" . . .");
        jp_Componentes.add(lbl_save_error2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 1980, 400, 35));

        txt_Q2C_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q2C_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 160, 130, 25));

        lbl_Q2C_titulo1.setText("Titulo");
        lbl_Q2C_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2C_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 300, 30));

        lbl_CABQ_titulo2.setText("Titulo");
        lbl_CABQ_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CABQ_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 200, 80, 25));

        txt_CABQ_Pregunta.setEditable(false);
        jp_Componentes.add(txt_CABQ_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 200, 130, 25));

        lbl_CABQ_titulo1.setText("Titulo");
        lbl_CABQ_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CABQ_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 300, 30));

        lbl_CVS_titulo2.setText("Titulo");
        lbl_CVS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CVS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 240, 80, 25));

        txt_CVS_Pregunta.setEditable(false);
        jp_Componentes.add(txt_CVS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 240, 130, 25));

        lbl_CVS_titulo1.setText("Titulo");
        lbl_CVS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CVS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 300, 30));

        lbl_EDB_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_EDB_error.setText(" . . .");
        jp_Componentes.add(lbl_EDB_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 330, 340, 35));

        lbl_EDB_titulo2.setText("Titulo");
        lbl_EDB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EDB_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 330, 80, 25));

        txt_EDB_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_EDB_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 330, 130, 25));

        lbl_EDB_titulo1.setText("Titulo");
        lbl_EDB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EDB_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 300, 30));

        lbl_ESE_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_ESE_error.setText(" . . .");
        jp_Componentes.add(lbl_ESE_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 370, 340, 35));

        lbl_ESE_titulo2.setText("Titulo");
        lbl_ESE_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_ESE_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 370, 80, 25));

        txt_ESE_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_ESE_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 370, 130, 25));

        lbl_ESE_titulo1.setText("Titulo");
        lbl_ESE_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_ESE_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 300, 30));

        btn_ESE_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_ESE_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_ESE_ayuda.setContentAreaFilled(false);
        btn_ESE_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_ESE_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 370, 25, 25));

        lbl_ERD_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_ERD_error.setText(" . . .");
        jp_Componentes.add(lbl_ERD_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 410, 340, 35));

        lbl_ERD_titulo2.setText("Titulo");
        lbl_ERD_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_ERD_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 410, 80, 25));

        txt_ERD_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_ERD_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 410, 130, 25));

        lbl_ERD_titulo1.setText("Titulo");
        lbl_ERD_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_ERD_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, 300, 30));

        btn_EDB_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_EDB_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_EDB_ayuda.setContentAreaFilled(false);
        btn_EDB_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_EDB_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 330, 25, 25));

        lbl_EST_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_EST_error.setText(" . . .");
        jp_Componentes.add(lbl_EST_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 450, 340, 35));

        lbl_EST_titulo2.setText("Titulo");
        lbl_EST_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EST_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 450, 80, 25));

        txt_EST_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_EST_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 450, 130, 25));

        lbl_EST_titulo1.setText("Titulo");
        lbl_EST_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EST_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, 300, 30));

        lbl_ESR_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_ESR_error.setText(" . . .");
        jp_Componentes.add(lbl_ESR_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 490, 340, 35));

        lbl_ESR_titulo2.setText("Titulo");
        lbl_ESR_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_ESR_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 490, 80, 25));

        txt_ESR_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_ESR_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 490, 130, 25));

        lbl_ESR_titulo1.setText("Titulo");
        lbl_ESR_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_ESR_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 490, 300, 30));

        btn_EST_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_EST_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_EST_ayuda.setContentAreaFilled(false);
        btn_EST_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_EST_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 450, 25, 25));

        lbl_ECC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_ECC_error.setText(" . . .");
        jp_Componentes.add(lbl_ECC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 530, 340, 35));

        lbl_ECC_titulo2.setText("Titulo");
        lbl_ECC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_ECC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 530, 80, 25));

        txt_ECC_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_ECC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 530, 130, 25));

        lbl_ECC_titulo1.setText("Titulo");
        lbl_ECC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_ECC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 300, 30));

        btn_ECC_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_ECC_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_ECC_ayuda.setContentAreaFilled(false);
        btn_ECC_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_ECC_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 530, 25, 25));

        lbl_ETE_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_ETE_error.setText(" . . .");
        jp_Componentes.add(lbl_ETE_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 570, 340, 35));

        lbl_ETE_titulo2.setText("Titulo");
        lbl_ETE_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_ETE_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 570, 80, 25));

        txt_ETE_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_ETE_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 570, 130, 25));

        lbl_ETE_titulo1.setText("Titulo");
        lbl_ETE_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_ETE_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 570, 300, 30));

        btn_ETE_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_ETE_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_ETE_ayuda.setContentAreaFilled(false);
        btn_ETE_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_ETE_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 570, 25, 25));
        jp_Componentes.add(jSeparator12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 690, 1024, 2));

        lbl_EEL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_EEL_error.setText(" . . .");
        jp_Componentes.add(lbl_EEL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 610, 340, 35));

        lbl_EEL_titulo2.setText("Titulo");
        lbl_EEL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EEL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 610, 80, 25));

        txt_EEL_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_EEL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 610, 130, 25));

        lbl_EEL_titulo1.setText("Titulo");
        lbl_EEL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EEL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 610, 300, 30));

        btn_EEL_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_EEL_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_EEL_ayuda.setContentAreaFilled(false);
        btn_EEL_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_EEL_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 610, 25, 25));

        lbl_ECE_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_ECE_error.setText(" . . .");
        jp_Componentes.add(lbl_ECE_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 650, 340, 35));

        lbl_ECE_titulo2.setText("Titulo");
        lbl_ECE_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_ECE_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 650, 80, 25));

        txt_ECE_Pregunta.setEditable(false);
        jp_Componentes.add(txt_ECE_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 650, 130, 25));

        lbl_ECE_titulo1.setText("Titulo");
        lbl_ECE_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_ECE_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 650, 300, 30));

        btn_ECE_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_ECE_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_ECE_ayuda.setContentAreaFilled(false);
        btn_ECE_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_ECE_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 650, 25, 25));

        lbl_EDP_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_EDP_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_EDP_desc.setText("Desc");
        lbl_EDP_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EDP_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 700, 610, 30));

        lbl_EDP_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_EDP_error.setText(" . . .");
        jp_Componentes.add(lbl_EDP_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 740, 340, 35));

        lbl_EDP_titulo2.setText("Titulo");
        lbl_EDP_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EDP_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 740, 80, 25));

        txt_EDP_Pregunta.setEditable(false);
        jp_Componentes.add(txt_EDP_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 740, 130, 25));

        lbl_EDP_titulo1.setText("Titulo");
        lbl_EDP_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EDP_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 740, 300, 30));

        btn_EDP_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_EDP_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_EDP_ayuda.setContentAreaFilled(false);
        btn_EDP_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_EDP_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 740, 25, 25));

        lbl_EDS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_EDS_error.setText(" . . .");
        jp_Componentes.add(lbl_EDS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 780, 340, 35));

        lbl_EDS_titulo2.setText("Titulo");
        lbl_EDS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EDS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 780, 80, 25));

        txt_EDS_Pregunta.setEditable(false);
        jp_Componentes.add(txt_EDS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 780, 130, 25));

        lbl_EDS_titulo1.setText("Titulo");
        lbl_EDS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EDS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 780, 300, 30));
        jp_Componentes.add(jSeparator14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 900, 1024, 2));

        lbl_EES_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_EES_error.setText(" . . .");
        jp_Componentes.add(lbl_EES_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 820, 340, 35));

        lbl_EES_titulo2.setText("Titulo");
        lbl_EES_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EES_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 820, 80, 25));

        txt_EES_Pregunta.setEditable(false);
        jp_Componentes.add(txt_EES_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 820, 130, 25));

        lbl_EES_titulo1.setText("Titulo");
        lbl_EES_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EES_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 820, 300, 30));

        lbl_EED_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_EED_error.setText(" . . .");
        jp_Componentes.add(lbl_EED_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 860, 340, 35));

        lbl_EED_titulo2.setText("Titulo");
        lbl_EED_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EED_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 860, 80, 25));

        txt_EED_Pregunta.setEditable(false);
        jp_Componentes.add(txt_EED_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 860, 130, 25));

        lbl_EED_titulo1.setText("Titulo");
        lbl_EED_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EED_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 860, 300, 30));
        jp_Componentes.add(jSeparator15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1010, 1024, -1));

        lbl_EPL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_EPL_error.setText(" . . .");
        jp_Componentes.add(lbl_EPL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 920, 340, 35));

        lbl_EPL_titulo2.setText("Titulo");
        lbl_EPL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EPL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 920, 80, 25));

        txt_EPL_Pregunta.setEditable(false);
        jp_Componentes.add(txt_EPL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 920, 130, 25));

        lbl_EPL_titulo1.setText("Titulo");
        lbl_EPL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EPL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 920, 300, 30));

        btn_EPL_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_EPL_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_EPL_ayuda.setContentAreaFilled(false);
        btn_EPL_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_EPL_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 920, 25, 25));

        lbl_ELT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_ELT_error.setText(" . . .");
        jp_Componentes.add(lbl_ELT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 960, 340, 35));

        lbl_ELT_titulo2.setText("Titulo");
        lbl_ELT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_ELT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 960, 80, 25));

        txt_ELT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_ELT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 960, 130, 25));

        lbl_ELT_titulo1.setText("Titulo");
        lbl_ELT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_ELT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 960, 300, 30));
        jp_Componentes.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1220, 1024, -1));

        lbl_EVL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_EVL_error.setText(" . . .");
        jp_Componentes.add(lbl_EVL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1030, 340, 35));

        lbl_EVL_titulo2.setText("Titulo");
        lbl_EVL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EVL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1030, 80, 25));

        txt_EVL_Pregunta.setEditable(false);
        jp_Componentes.add(txt_EVL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1030, 130, 25));

        lbl_EVL_titulo1.setText("Titulo");
        lbl_EVL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EVL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1030, 300, 30));

        lbl_EVT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_EVT_error.setText(" . . .");
        jp_Componentes.add(lbl_EVT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1130, 340, 35));

        lbl_EVT_titulo2.setText("Titulo");
        lbl_EVT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EVT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1130, 80, 25));

        txt_EVT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_EVT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1130, 130, 25));

        lbl_EVT_titulo1.setText("Titulo");
        lbl_EVT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EVT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1130, 300, 30));

        btn_EVT_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_EVT_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_EVT_ayuda.setContentAreaFilled(false);
        btn_EVT_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_EVT_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1130, 25, 25));

        lbl_EVH_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_EVH_error.setText(" . . .");
        jp_Componentes.add(lbl_EVH_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1170, 400, 35));

        lbl_EVH_titulo2.setText("Titulo");
        lbl_EVH_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EVH_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1170, 80, 25));

        txt_EVH_Pregunta.setEditable(false);
        jp_Componentes.add(txt_EVH_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1170, 130, 25));

        lbl_EVH_titulo1.setText("Titulo");
        lbl_EVH_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EVH_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1170, 300, 30));

        btn_EVH_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_EVH_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_EVH_ayuda.setContentAreaFilled(false);
        btn_EVH_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_EVH_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1170, 25, 25));

        lbl_EPT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_EPT_error.setText(" . . .");
        jp_Componentes.add(lbl_EPT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1270, 340, 35));

        lbl_EPT_titulo2.setText("Titulo");
        lbl_EPT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EPT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1270, 80, 25));

        txt_EPT_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_EPT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1270, 130, 25));

        lbl_EPT_titulo1.setText("Titulo");
        lbl_EPT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EPT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1270, 300, 30));

        lbl_EAS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_EAS_error.setText(" . . .");
        jp_Componentes.add(lbl_EAS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1310, 340, 35));

        lbl_EAS_titulo2.setText("Titulo");
        lbl_EAS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EAS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1310, 80, 25));

        txt_EAS_Pregunta.setEditable(false);
        jp_Componentes.add(txt_EAS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1310, 130, 25));

        lbl_EAS_titulo1.setText("Titulo");
        lbl_EAS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EAS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1310, 300, 30));

        btn_EPT_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_EPT_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_EPT_ayuda.setContentAreaFilled(false);
        btn_EPT_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_EPT_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 1230, 25, 25));
        jp_Componentes.add(jSeparator16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1430, 1024, 2));

        lbl_EPT_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_EPT_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_EPT_desc.setText("Desc");
        lbl_EPT_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EPT_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1230, 610, 30));
        jp_Componentes.add(jSeparator17, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1570, 1024, 2));

        lbl_ERM_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_ERM_error.setText(" . . .");
        jp_Componentes.add(lbl_ERM_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1490, 340, 35));

        lbl_ERM_titulo2.setText("Titulo");
        lbl_ERM_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_ERM_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1490, 80, 25));

        txt_ERM_Pregunta.setEditable(false);
        jp_Componentes.add(txt_ERM_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1490, 130, 25));

        lbl_ERM_titulo1.setText("Titulo");
        lbl_ERM_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_ERM_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1490, 300, 30));

        lbl_EEC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_EEC_error.setText(" . . .");
        jp_Componentes.add(lbl_EEC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1530, 340, 35));

        lbl_EEC_titulo2.setText("Titulo");
        lbl_EEC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EEC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1530, 80, 25));

        txt_EEC_Pregunta.setEditable(false);
        jp_Componentes.add(txt_EEC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1530, 130, 25));

        lbl_EEC_titulo1.setText("Titulo");
        lbl_EEC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EEC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1530, 300, 30));

        btn_EEC_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_EEC_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_EEC_ayuda.setContentAreaFilled(false);
        btn_EEC_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_EEC_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1530, 25, 25));

        lbl_EFS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_EFS_error.setText(" . . .");
        jp_Componentes.add(lbl_EFS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1620, 340, 35));

        lbl_EFS_titulo2.setText("Titulo");
        lbl_EFS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EFS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1620, 80, 25));

        txt_EFS_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_EFS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1620, 130, 25));

        lbl_EFS_titulo1.setText("Titulo");
        lbl_EFS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EFS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1620, 300, 30));

        btn_EFS_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_EFS_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_EFS_ayuda.setContentAreaFilled(false);
        btn_EFS_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_EFS_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1620, 25, 25));

        lbl_ESA_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_ESA_error.setText(" . . .");
        jp_Componentes.add(lbl_ESA_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1660, 340, 35));

        lbl_ESA_titulo2.setText("Titulo");
        lbl_ESA_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_ESA_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1660, 80, 25));

        txt_ESA_Pregunta.setEditable(false);
        jp_Componentes.add(txt_ESA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1660, 130, 25));

        lbl_ESA_titulo1.setText("Titulo");
        lbl_ESA_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_ESA_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1660, 300, 30));

        btn_ESA_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_ESA_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_ESA_ayuda.setContentAreaFilled(false);
        btn_ESA_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_ESA_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1660, 25, 25));

        lbl_EAM_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_EAM_error.setText(" . . .");
        jp_Componentes.add(lbl_EAM_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1450, 340, 35));

        lbl_EAM_titulo2.setText("Titulo");
        lbl_EAM_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EAM_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1450, 80, 25));

        txt_EAM_Pregunta.setEditable(false);
        jp_Componentes.add(txt_EAM_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1450, 130, 25));

        lbl_EAM_titulo1.setText("Titulo");
        lbl_EAM_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EAM_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1450, 300, 30));

        btn_ERM_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_ERM_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_ERM_ayuda.setContentAreaFilled(false);
        btn_ERM_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_ERM_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1490, 25, 25));
        jp_Componentes.add(jSeparator18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1700, 1024, 2));

        lbl_EMS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_EMS_error.setText(" . . .");
        jp_Componentes.add(lbl_EMS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1760, 340, 35));

        lbl_EMS_titulo2.setText("Titulo");
        lbl_EMS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EMS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1760, 80, 25));

        txt_EMS_Pregunta.setEditable(false);
        jp_Componentes.add(txt_EMS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1760, 130, 25));

        lbl_EMS_titulo1.setText("Titulo");
        lbl_EMS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EMS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1760, 300, 30));

        btn_EMS_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_EMS_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_EMS_ayuda.setContentAreaFilled(false);
        btn_EMS_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_EMS_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1760, 25, 25));

        lbl_EHS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_EHS_error.setText(" . . .");
        jp_Componentes.add(lbl_EHS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1800, 340, 35));

        lbl_EHS_titulo2.setText("Titulo");
        lbl_EHS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EHS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1800, 80, 25));

        txt_EHS_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_EHS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1800, 130, 25));

        lbl_EHS_titulo1.setText("Titulo");
        lbl_EHS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EHS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1800, 300, 30));

        btn_EHS_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_EHS_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_EHS_ayuda.setContentAreaFilled(false);
        btn_EHS_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_EHS_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1800, 25, 25));

        lbl_EMS_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_EMS_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_EMS_desc.setText("Desc");
        lbl_EMS_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EMS_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1720, 610, 30));

        lbl_EAT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_EAT_error.setText(" . . .");
        jp_Componentes.add(lbl_EAT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1880, 340, 35));

        lbl_EAT_titulo2.setText("Titulo");
        lbl_EAT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EAT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1880, 80, 25));

        txt_EAT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_EAT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1880, 130, 25));

        lbl_EAT_titulo1.setText("Titulo");
        lbl_EAT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EAT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1880, 300, 30));

        btn_EVL_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_EVL_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_EVL_ayuda.setContentAreaFilled(false);
        btn_EVL_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_EVL_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1030, 25, 25));

        lbl_EDB_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_EDB_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_EDB_desc.setText("Desc");
        lbl_EDB_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EDB_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 610, 30));

        btn_ESR_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_ESR_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_ESR_ayuda.setContentAreaFilled(false);
        btn_ESR_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_ESR_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 490, 25, 25));
        jp_Componentes.add(jSeparator19, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1080, 1024, 2));

        lbl_EVT_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_EVT_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_EVT_desc.setText("Desc");
        lbl_EVT_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EVT_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1090, 610, 30));

        btn_EPT_ayuda1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_EPT_ayuda1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_EPT_ayuda1.setContentAreaFilled(false);
        btn_EPT_ayuda1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_EPT_ayuda1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1270, 25, 25));

        btn_EAM_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_EAM_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_EAM_ayuda.setContentAreaFilled(false);
        btn_EAM_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_EAM_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1450, 25, 25));

        lbl_EFS_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_EFS_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_EFS_desc.setText("Desc");
        lbl_EFS_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EFS_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1580, 610, 30));

        lbl_EWT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_EWT_error.setText(" . . .");
        jp_Componentes.add(lbl_EWT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1350, 340, 35));

        lbl_EWT_titulo2.setText("Titulo");
        lbl_EWT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EWT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1350, 80, 25));

        txt_EWT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_EWT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1350, 130, 25));

        lbl_EWT_titulo1.setText("Titulo");
        lbl_EWT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EWT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1350, 300, 30));

        lbl_EHT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_EHT_error.setText(" . . .");
        jp_Componentes.add(lbl_EHT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1390, 340, 35));

        lbl_EHT_titulo2.setText("Titulo");
        lbl_EHT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EHT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1390, 80, 25));

        txt_EHT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_EHT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1390, 130, 25));

        lbl_EHT_titulo1.setText("Titulo");
        lbl_EHT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_EHT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1390, 300, 30));

        lbl_ESD_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_ESD_error.setText(" . . .");
        jp_Componentes.add(lbl_ESD_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1840, 340, 35));

        lbl_ESD_titulo2.setText("Titulo");
        lbl_ESD_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_ESD_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1840, 80, 25));

        txt_ESD_Pregunta.setEditable(false);
        jp_Componentes.add(txt_ESD_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1840, 130, 25));

        lbl_ESD_titulo1.setText("Titulo");
        lbl_ESD_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_ESD_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1840, 300, 30));

        jsp_LodosActivadsAireacExt.setViewportView(jp_Componentes);
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
            .addComponent(jsp_LodosActivadsAireacExt, javax.swing.GroupLayout.PREFERRED_SIZE, 1008, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_close2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btn_close2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jsp_LodosActivadsAireacExt, javax.swing.GroupLayout.DEFAULT_SIZE, 2226, Short.MAX_VALUE)
                .addContainerGap())
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
    private javax.swing.JButton btn_EAM_ayuda;
    private javax.swing.JButton btn_ECC_ayuda;
    private javax.swing.JButton btn_ECE_ayuda;
    private javax.swing.JButton btn_EDB_ayuda;
    private javax.swing.JButton btn_EDP_ayuda;
    private javax.swing.JButton btn_EEC_ayuda;
    private javax.swing.JButton btn_EEL_ayuda;
    private javax.swing.JButton btn_EFS_ayuda;
    private javax.swing.JButton btn_EHS_ayuda;
    private javax.swing.JButton btn_EMS_ayuda;
    private javax.swing.JButton btn_EPL_ayuda;
    private javax.swing.JButton btn_EPT_ayuda;
    private javax.swing.JButton btn_EPT_ayuda1;
    private javax.swing.JButton btn_ERM_ayuda;
    private javax.swing.JButton btn_ESA_ayuda;
    private javax.swing.JButton btn_ESE_ayuda;
    private javax.swing.JButton btn_ESR_ayuda;
    private javax.swing.JButton btn_EST_ayuda;
    private javax.swing.JButton btn_ETE_ayuda;
    private javax.swing.JButton btn_EVH_ayuda;
    private javax.swing.JButton btn_EVL_ayuda;
    private javax.swing.JButton btn_EVT_ayuda;
    private javax.swing.JButton btn_close;
    private javax.swing.JButton btn_close2;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JPanel jp_Componentes;
    private javax.swing.JScrollPane jsp_LodosActivadsAireacExt;
    private javax.swing.JLabel lbl_CABQ_titulo1;
    private javax.swing.JLabel lbl_CABQ_titulo2;
    private javax.swing.JLabel lbl_CVS_titulo1;
    private javax.swing.JLabel lbl_CVS_titulo2;
    private javax.swing.JLabel lbl_EAM_error;
    private javax.swing.JLabel lbl_EAM_titulo1;
    private javax.swing.JLabel lbl_EAM_titulo2;
    private javax.swing.JLabel lbl_EAS_error;
    private javax.swing.JLabel lbl_EAS_titulo1;
    private javax.swing.JLabel lbl_EAS_titulo2;
    private javax.swing.JLabel lbl_EAT_error;
    private javax.swing.JLabel lbl_EAT_titulo1;
    private javax.swing.JLabel lbl_EAT_titulo2;
    private javax.swing.JLabel lbl_ECC_error;
    private javax.swing.JLabel lbl_ECC_titulo1;
    private javax.swing.JLabel lbl_ECC_titulo2;
    private javax.swing.JLabel lbl_ECE_error;
    private javax.swing.JLabel lbl_ECE_titulo1;
    private javax.swing.JLabel lbl_ECE_titulo2;
    private javax.swing.JLabel lbl_EDB_desc;
    private javax.swing.JLabel lbl_EDB_error;
    private javax.swing.JLabel lbl_EDB_titulo1;
    private javax.swing.JLabel lbl_EDB_titulo2;
    private javax.swing.JLabel lbl_EDP_desc;
    private javax.swing.JLabel lbl_EDP_error;
    private javax.swing.JLabel lbl_EDP_titulo1;
    private javax.swing.JLabel lbl_EDP_titulo2;
    private javax.swing.JLabel lbl_EDS_error;
    private javax.swing.JLabel lbl_EDS_titulo1;
    private javax.swing.JLabel lbl_EDS_titulo2;
    private javax.swing.JLabel lbl_EEC_error;
    private javax.swing.JLabel lbl_EEC_titulo1;
    private javax.swing.JLabel lbl_EEC_titulo2;
    private javax.swing.JLabel lbl_EED_error;
    private javax.swing.JLabel lbl_EED_titulo1;
    private javax.swing.JLabel lbl_EED_titulo2;
    private javax.swing.JLabel lbl_EEL_error;
    private javax.swing.JLabel lbl_EEL_titulo1;
    private javax.swing.JLabel lbl_EEL_titulo2;
    private javax.swing.JLabel lbl_EES_error;
    private javax.swing.JLabel lbl_EES_titulo1;
    private javax.swing.JLabel lbl_EES_titulo2;
    private javax.swing.JLabel lbl_EFS_desc;
    private javax.swing.JLabel lbl_EFS_error;
    private javax.swing.JLabel lbl_EFS_titulo1;
    private javax.swing.JLabel lbl_EFS_titulo2;
    private javax.swing.JLabel lbl_EHS_error;
    private javax.swing.JLabel lbl_EHS_titulo1;
    private javax.swing.JLabel lbl_EHS_titulo2;
    private javax.swing.JLabel lbl_EHT_error;
    private javax.swing.JLabel lbl_EHT_titulo1;
    private javax.swing.JLabel lbl_EHT_titulo2;
    private javax.swing.JLabel lbl_ELT_error;
    private javax.swing.JLabel lbl_ELT_titulo1;
    private javax.swing.JLabel lbl_ELT_titulo2;
    private javax.swing.JLabel lbl_EMS_desc;
    private javax.swing.JLabel lbl_EMS_error;
    private javax.swing.JLabel lbl_EMS_titulo1;
    private javax.swing.JLabel lbl_EMS_titulo2;
    private javax.swing.JLabel lbl_EPL_error;
    private javax.swing.JLabel lbl_EPL_titulo1;
    private javax.swing.JLabel lbl_EPL_titulo2;
    private javax.swing.JLabel lbl_EPT_desc;
    private javax.swing.JLabel lbl_EPT_error;
    private javax.swing.JLabel lbl_EPT_titulo1;
    private javax.swing.JLabel lbl_EPT_titulo2;
    private javax.swing.JLabel lbl_ERD_error;
    private javax.swing.JLabel lbl_ERD_titulo1;
    private javax.swing.JLabel lbl_ERD_titulo2;
    private javax.swing.JLabel lbl_ERM_error;
    private javax.swing.JLabel lbl_ERM_titulo1;
    private javax.swing.JLabel lbl_ERM_titulo2;
    private javax.swing.JLabel lbl_ESA_error;
    private javax.swing.JLabel lbl_ESA_titulo1;
    private javax.swing.JLabel lbl_ESA_titulo2;
    private javax.swing.JLabel lbl_ESD_error;
    private javax.swing.JLabel lbl_ESD_titulo1;
    private javax.swing.JLabel lbl_ESD_titulo2;
    private javax.swing.JLabel lbl_ESE_error;
    private javax.swing.JLabel lbl_ESE_titulo1;
    private javax.swing.JLabel lbl_ESE_titulo2;
    private javax.swing.JLabel lbl_ESR_error;
    private javax.swing.JLabel lbl_ESR_titulo1;
    private javax.swing.JLabel lbl_ESR_titulo2;
    private javax.swing.JLabel lbl_EST_error;
    private javax.swing.JLabel lbl_EST_titulo1;
    private javax.swing.JLabel lbl_EST_titulo2;
    private javax.swing.JLabel lbl_ETE_error;
    private javax.swing.JLabel lbl_ETE_titulo1;
    private javax.swing.JLabel lbl_ETE_titulo2;
    private javax.swing.JLabel lbl_EVH_error;
    private javax.swing.JLabel lbl_EVH_titulo1;
    private javax.swing.JLabel lbl_EVH_titulo2;
    private javax.swing.JLabel lbl_EVL_error;
    private javax.swing.JLabel lbl_EVL_titulo1;
    private javax.swing.JLabel lbl_EVL_titulo2;
    private javax.swing.JLabel lbl_EVT_desc;
    private javax.swing.JLabel lbl_EVT_error;
    private javax.swing.JLabel lbl_EVT_titulo1;
    private javax.swing.JLabel lbl_EVT_titulo2;
    private javax.swing.JLabel lbl_EWT_error;
    private javax.swing.JLabel lbl_EWT_titulo1;
    private javax.swing.JLabel lbl_EWT_titulo2;
    private javax.swing.JLabel lbl_Q2C_titulo1;
    private javax.swing.JLabel lbl_Q2C_titulo2;
    private javax.swing.JLabel lbl_save_error;
    private javax.swing.JLabel lbl_save_error2;
    private javax.swing.JLabel lbl_titulo1;
    private javax.swing.JLabel lbl_titulo2;
    private javax.swing.JTextField txt_CABQ_Pregunta;
    private javax.swing.JTextField txt_CVS_Pregunta;
    private javax.swing.JTextField txt_EAM_Pregunta;
    private javax.swing.JTextField txt_EAS_Pregunta;
    private javax.swing.JTextField txt_EAT_Pregunta;
    private javax.swing.JTextField txt_ECC_Pregunta;
    private javax.swing.JTextField txt_ECE_Pregunta;
    private javax.swing.JTextField txt_EDB_Pregunta;
    private javax.swing.JTextField txt_EDP_Pregunta;
    private javax.swing.JTextField txt_EDS_Pregunta;
    private javax.swing.JTextField txt_EEC_Pregunta;
    private javax.swing.JTextField txt_EED_Pregunta;
    private javax.swing.JTextField txt_EEL_Pregunta;
    private javax.swing.JTextField txt_EES_Pregunta;
    private javax.swing.JTextField txt_EFS_Pregunta;
    private javax.swing.JTextField txt_EHS_Pregunta;
    private javax.swing.JTextField txt_EHT_Pregunta;
    private javax.swing.JTextField txt_ELT_Pregunta;
    private javax.swing.JTextField txt_EMS_Pregunta;
    private javax.swing.JTextField txt_EPL_Pregunta;
    private javax.swing.JTextField txt_EPT_Pregunta;
    private javax.swing.JTextField txt_ERD_Pregunta;
    private javax.swing.JTextField txt_ERM_Pregunta;
    private javax.swing.JTextField txt_ESA_Pregunta;
    private javax.swing.JTextField txt_ESD_Pregunta;
    private javax.swing.JTextField txt_ESE_Pregunta;
    private javax.swing.JTextField txt_ESR_Pregunta;
    private javax.swing.JTextField txt_EST_Pregunta;
    private javax.swing.JTextField txt_ETE_Pregunta;
    private javax.swing.JTextField txt_EVH_Pregunta;
    private javax.swing.JTextField txt_EVL_Pregunta;
    private javax.swing.JTextField txt_EVT_Pregunta;
    private javax.swing.JTextField txt_EWT_Pregunta;
    private javax.swing.JTextField txt_Q2C_Pregunta;
    // End of variables declaration//GEN-END:variables
}