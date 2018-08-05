package UI;

import BO.Bod;
import Componentes.Pregunta;
import DB.Dao;
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

public class LodosActivadsModConvUASB extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("Lodos Activados Modalidad Conv. UASB");
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    public Map<String, Pregunta> map = new TreeMap<>();
    PopupMenuListener listener;
    private Bod bod = new Bod("");
    private Listener_Popup Lpopup;
    Util util = new Util();
    Pregunta pg;
    private boolean eSave = true;

    public LodosActivadsModConvUASB(Bod bod) {
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
        bod.WinLodosActivadsConvencUASB = true;//Bandera La ventana se ha abierto
        btn_guardar.setText("Guardar");
        btn_close.setText("Cerrar");
        try {
            // - - - - - - Cargar el titulo de la sección  
            rs = db.ResultadoSelect("obtenerseccion", "14");

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
                txt_CABQ_Pregunta.setText(bod.getVarFormateada(bod.getVarDob("UEQ"), rs.getInt("decimales")));
            } else {
                rs = db.ResultadoSelect("datospregunta", "CAQ");

                lbl_CABQ_titulo1.setText(rs.getString("titulo1"));
                lbl_CABQ_titulo2.setText(rs.getString("titulo2"));
                txt_CABQ_Pregunta.setText(bod.getVarFormateada(bod.getVarDob("UEC"), rs.getInt("decimales")));
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
            // - - - - - - Pregunta 09 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NDB
            rs = db.ResultadoSelect("datospregunta", "NDB");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NDB", pg);

            lbl_NDB_titulo1.setText(pg.tit1);
            lbl_NDB_titulo2.setText(rs.getString("titulo2"));
            txt_NDB_Pregunta.setText(rs.getString("valorpordefecto"));
            lbl_NDB_desc.setText(pg.desc);

            txt_NDB_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("NDB");
                }
            });
            AsignarPopupBtn(btn_NDB_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 490, 320);
            // - - - - - - Pregunta 10 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NSE
            rs = db.ResultadoSelect("datospregunta", "NSE");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NSE", pg);

            lbl_NSE_titulo1.setText(pg.tit1);
            lbl_NSE_titulo2.setText(rs.getString("titulo2"));
            txt_NSE_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_NSE_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("NSE");
                }
            });

            AsignarPopupBtn(btn_NSE_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 540, 260);
            // - - - - - - Pregunta 11 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NRD
            rs = db.ResultadoSelect("datospregunta", "NRD");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NRD", pg);

            lbl_NRD_titulo1.setText(pg.tit1);
            lbl_NRD_titulo2.setText(rs.getString("titulo2"));
            txt_NRD_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_NRD_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("NRD");
                }
            });
            // - - - - - - Pregunta 12 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NST
            rs = db.ResultadoSelect("datospregunta", "NST");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NST", pg);

            lbl_NST_titulo1.setText(pg.tit1);
            lbl_NST_titulo2.setText(rs.getString("titulo2"));
            txt_NST_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_NST_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("NST");
                }
            });

            AsignarPopupBtn(btn_NST_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 500, 240);
            // - - - - - - Pregunta 13 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NSR
            rs = db.ResultadoSelect("datospregunta", "NSR");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NSR", pg);

            lbl_NSR_titulo1.setText(pg.tit1);
            lbl_NSR_titulo2.setText(rs.getString("titulo2"));
            txt_NSR_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_NSR_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("NSR");
                }
            });
            AsignarPopupBtn(btn_NSR_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 490, 190);
            // - - - - - - Pregunta 14 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NCC
            rs = db.ResultadoSelect("datospregunta", "NCC");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NCC", pg);

            lbl_NCC_titulo1.setText(pg.tit1);
            lbl_NCC_titulo2.setText(rs.getString("titulo2"));
            txt_NCC_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_NCC_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("NCC");
                }
            });

            AsignarPopupBtn(btn_NCC_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 580, 230);
            // - - - - - - Pregunta 15 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NTE
            rs = db.ResultadoSelect("datospregunta", "NTE");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NTE", pg);

            lbl_NTE_titulo1.setText(pg.tit1);
            lbl_NTE_titulo2.setText(rs.getString("titulo2"));
            txt_NTE_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_NTE_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("NTE");
                }
            });

            AsignarPopupBtn(btn_NTE_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 490, 170);
            // - - - - - - Pregunta 16 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NEL
            rs = db.ResultadoSelect("datospregunta", "NEL");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NEL", pg);

            lbl_NEL_titulo1.setText(pg.tit1);
            lbl_NEL_titulo2.setText(rs.getString("titulo2"));
            txt_NEL_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_NEL_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("NEL");
                }
            });

            AsignarPopupBtn(btn_NEL_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 450, 180);
            // - - - - - - Pregunta 17 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NCE
            rs = db.ResultadoSelect("datospregunta", "NCE");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NCE", pg);

            lbl_NCE_titulo1.setText(pg.tit1);
            lbl_NCE_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_NCE_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 480, 270);
            // - - - - - - Pregunta 18 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NDP
            rs = db.ResultadoSelect("datospregunta", "NDP");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NDP", pg);

            lbl_NDP_titulo1.setText(pg.tit1);
            lbl_NDP_titulo2.setText(rs.getString("titulo2"));
            lbl_NDP_desc.setText(pg.desc);

            AsignarPopupBtn(btn_NDP_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 460, 180);
            // - - - - - - Pregunta 19 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NDS
            rs = db.ResultadoSelect("datospregunta", "NDS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NDS", pg);

            lbl_NDS_titulo1.setText(pg.tit1);
            lbl_NDS_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 20 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NES
            rs = db.ResultadoSelect("datospregunta", "NES");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NES", pg);

            lbl_NES_titulo1.setText(pg.tit1);
            lbl_NES_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 21 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NED
            rs = db.ResultadoSelect("datospregunta", "NED");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NED", pg);

            lbl_NED_titulo1.setText(pg.tit1);
            lbl_NED_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 22 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NPL
            rs = db.ResultadoSelect("datospregunta", "NPL");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NPL", pg);

            lbl_NPL_titulo1.setText(pg.tit1);
            lbl_NPL_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_NPL_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 560, 560);
            // - - - - - - Pregunta 23 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NLT
            rs = db.ResultadoSelect("datospregunta", "NLT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NLT", pg);

            lbl_NLT_titulo1.setText(pg.tit1);
            lbl_NLT_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 24 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NVL
            rs = db.ResultadoSelect("datospregunta", "NVL");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NVL", pg);

            lbl_NVL_titulo1.setText(pg.tit1);
            lbl_NVL_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_NVL_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 460, 280);
            // - - - - - - Pregunta 25 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NVT
            rs = db.ResultadoSelect("datospregunta", "NVT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NVT", pg);

            lbl_NVT_titulo1.setText(pg.tit1);
            lbl_NVT_titulo2.setText(rs.getString("titulo2"));
            lbl_NVT_desc.setText(pg.desc);

            AsignarPopupBtn(btn_NVT_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 320, 340);
            // - - - - - - Pregunta 26 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NVH
            rs = db.ResultadoSelect("datospregunta", "NVH");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NVH", pg);

            lbl_NVH_titulo1.setText(pg.tit1);
            lbl_NVH_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_NVH_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 220);
            // - - - - - - Pregunta 27 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NPT
            rs = db.ResultadoSelect("datospregunta", "NPT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NPT", pg);

            lbl_NPT_titulo1.setText(pg.tit1);
            lbl_NPT_titulo2.setText(rs.getString("titulo2"));
            txt_NPT_Pregunta.setText(rs.getString("valorpordefecto"));
            lbl_NPT_desc.setText(pg.desc);

            txt_NPT_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("NPT");
                }
            });

            AsignarPopupBtn(btn_NPT_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 440, 160);
            AsignarPopupBtn(btn_NPT_ayuda1, rs.getString("ayuda2"), rs.getString("ayudalink"), 500, 150);
            // - - - - - - Pregunta 28 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NAS
            rs = db.ResultadoSelect("datospregunta", "NAS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NAS", pg);

            lbl_NAS_titulo1.setText(pg.tit1);
            lbl_NAS_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 28 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NWT
            rs = db.ResultadoSelect("datospregunta", "NWT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NWT", pg);

            lbl_NWT_titulo1.setText(pg.tit1);
            lbl_NWT_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 28 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NHT
            rs = db.ResultadoSelect("datospregunta", "NHT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NHT", pg);

            lbl_NHT_titulo1.setText(pg.tit1);
            lbl_NHT_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 29 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NAM
            rs = db.ResultadoSelect("datospregunta", "NAM");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NAM", pg);

            lbl_NAM_titulo1.setText(pg.tit1);
            lbl_NAM_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_NAM_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 460, 420);
            // - - - - - - Pregunta 30 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NRM
            rs = db.ResultadoSelect("datospregunta", "NRM");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NRM", pg);

            lbl_NRM_titulo1.setText(pg.tit1);
            lbl_NRM_titulo2.setText(rs.getString("titulo2"));
            txt_NRM_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_NRM_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("NRM");
                }
            });

            AsignarPopupBtn(btn_NRM_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 480, 200);
            // - - - - - - Pregunta 31 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NEC
            rs = db.ResultadoSelect("datospregunta", "NEC");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NEC", pg);

            lbl_NEC_titulo1.setText(pg.tit1);
            lbl_NEC_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_NEC_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 360, 250);
            // - - - - - - Pregunta 32 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NFS
            rs = db.ResultadoSelect("datospregunta", "NFS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NFS", pg);

            lbl_NFS_titulo1.setText(pg.tit1);
            lbl_NFS_titulo2.setText(rs.getString("titulo2"));
            txt_NFS_Pregunta.setText(rs.getString("valorpordefecto"));
            lbl_NFS_desc.setText(pg.desc);

            txt_NFS_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("NFS");
                }
            });

            AsignarPopupBtn(btn_NFS_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 460, 220);
            // - - - - - - Pregunta 33 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NSA
            rs = db.ResultadoSelect("datospregunta", "NSA");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NSA", pg);

            lbl_NSA_titulo1.setText(pg.tit1);
            lbl_NSA_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_NSA_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 320, 240);
            // - - - - - - Pregunta 34 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NMS
            rs = db.ResultadoSelect("datospregunta", "NMS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NMS", pg);

            lbl_NMS_titulo1.setText(pg.tit1);
            lbl_NMS_titulo2.setText(rs.getString("titulo2"));
            lbl_NMS_desc.setText(pg.desc);

            AsignarPopupBtn(btn_NMS_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 490, 280);
            // - - - - - - Pregunta 35 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NHS
            rs = db.ResultadoSelect("datospregunta", "NHS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NHS", pg);

            lbl_NHS_titulo1.setText(pg.tit1);
            lbl_NHS_titulo2.setText(rs.getString("titulo2"));
            txt_NHS_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_NHS_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("NHS");
                }
            });

            AsignarPopupBtn(btn_NHS_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 430, 150);
            // - - - - - - Pregunta 36 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NSD
            rs = db.ResultadoSelect("datospregunta", "NSD");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NSD", pg);

            lbl_NSD_titulo1.setText(pg.tit1);
            lbl_NSD_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 36 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NAT
            rs = db.ResultadoSelect("datospregunta", "NAT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NAT", pg);

            lbl_NAT_titulo1.setText(pg.tit1);
            lbl_NAT_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - Si ya han sido llenado correctamente estos datos se obtienen de Bod
            if (bod.EditLodosActivadsConvencUASB) {
                txt_NDB_Pregunta.setText(bod.getVarFormateadaPorNombre("NDB", map.get("NDB").deci));
                txt_NSE_Pregunta.setText(bod.getVarFormateadaPorNombre("NSE", map.get("NSE").deci));
                txt_NRD_Pregunta.setText(bod.getVarFormateadaPorNombre("NRD", map.get("NRD").deci));
                txt_NST_Pregunta.setText(bod.getVarFormateadaPorNombre("NST", map.get("NST").deci));
                txt_NSR_Pregunta.setText(bod.getVarFormateadaPorNombre("NSR", map.get("NSR").deci));
                txt_NCC_Pregunta.setText(bod.getVarFormateadaPorNombre("NCC", map.get("NCC").deci));
                txt_NTE_Pregunta.setText(bod.getVarFormateadaPorNombre("NTE", map.get("NTE").deci));
                txt_NEL_Pregunta.setText(bod.getVarFormateadaPorNombre("NEL", map.get("NEL").deci));
                txt_NCE_Pregunta.setText(bod.getVarFormateadaPorNombre("NCE", map.get("NCE").deci));
                txt_NDP_Pregunta.setText(bod.getVarFormateadaPorNombre("NDP", map.get("NDP").deci));
                txt_NDS_Pregunta.setText(bod.getVarFormateadaPorNombre("NDS", map.get("NDS").deci));
                txt_NES_Pregunta.setText(bod.getVarFormateadaPorNombre("NES", map.get("NES").deci));
                txt_NED_Pregunta.setText(bod.getVarFormateadaPorNombre("NED", map.get("NED").deci));
                txt_NPL_Pregunta.setText(bod.getVarFormateadaPorNombre("NPL", map.get("NPL").deci));
                txt_NLT_Pregunta.setText(bod.getVarFormateadaPorNombre("NLT", map.get("NLT").deci));
                txt_NVL_Pregunta.setText(bod.getVarFormateadaPorNombre("NVL", map.get("NVL").deci));
                txt_NVT_Pregunta.setText(bod.getVarFormateadaPorNombre("NVT", map.get("NVT").deci));
                txt_NVH_Pregunta.setText(bod.getVarFormateadaPorNombre("NVH", map.get("NVH").deci));
                txt_NPT_Pregunta.setText(bod.getVarFormateadaPorNombre("NPT", map.get("NPT").deci));
                txt_NAS_Pregunta.setText(bod.getVarFormateadaPorNombre("NAS", map.get("NAS").deci));
                txt_NWT_Pregunta.setText(bod.getVarFormateadaPorNombre("NWT", map.get("NWT").deci));
                txt_NHT_Pregunta.setText(bod.getVarFormateadaPorNombre("NHT", map.get("NHT").deci));
                txt_NAM_Pregunta.setText(bod.getVarFormateadaPorNombre("NAM", map.get("NAM").deci));
                txt_NRM_Pregunta.setText(bod.getVarFormateadaPorNombre("NRM", map.get("NRM").deci));
                txt_NEC_Pregunta.setText(bod.getVarFormateadaPorNombre("NEC", map.get("NEC").deci));
                txt_NFS_Pregunta.setText(bod.getVarFormateadaPorNombre("NFS", map.get("NFS").deci));
                txt_NSA_Pregunta.setText(bod.getVarFormateadaPorNombre("NSA", map.get("NSA").deci));
                txt_NMS_Pregunta.setText(bod.getVarFormateadaPorNombre("NMS", map.get("NMS").deci));
                txt_NHS_Pregunta.setText(bod.getVarFormateadaPorNombre("NHS", map.get("NHS").deci));
                txt_NSD_Pregunta.setText(bod.getVarFormateadaPorNombre("NSD", map.get("NSD").deci));
                txt_NAT_Pregunta.setText(bod.getVarFormateadaPorNombre("NAT", map.get("NAT").deci));
            } else {
                ejecutarFunciones("NDB");
                ejecutarFunciones("NSE");
                ejecutarFunciones("NRD");
                ejecutarFunciones("NST");
                ejecutarFunciones("NSR");
                ejecutarFunciones("NCC");
                ejecutarFunciones("NTE");
                ejecutarFunciones("NEL");
                ejecutarFunciones("NPT");
                ejecutarFunciones("NRM");
                ejecutarFunciones("NFS");
                ejecutarFunciones("NHS");
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
        lbl_NAM_error.setText("");
        lbl_NAS_error.setText("");
        lbl_NAT_error.setText("");
        lbl_NCC_error.setText("");
        lbl_NCE_error.setText("");
        lbl_NDB_error.setText("");
        lbl_NDP_error.setText("");
        lbl_NDS_error.setText("");
        lbl_NEC_error.setText("");
        lbl_NED_error.setText("");
        lbl_NEL_error.setText("");
        lbl_NES_error.setText("");
        lbl_NFS_error.setText("");
        lbl_NHS_error.setText("");
        lbl_NHT_error.setText("");
        lbl_NLT_error.setText("");
        lbl_NMS_error.setText("");
        lbl_NPL_error.setText("");
        lbl_NPT_error.setText("");
        lbl_NRD_error.setText("");
        lbl_NRM_error.setText("");
        lbl_NSA_error.setText("");
        lbl_NSD_error.setText("");
        lbl_NSE_error.setText("");
        lbl_NSR_error.setText("");
        lbl_NST_error.setText("");
        lbl_NTE_error.setText("");
        lbl_NVH_error.setText("");
        lbl_NVL_error.setText("");
        lbl_NVT_error.setText("");
        lbl_NWT_error.setText("");
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

        if (!CalcularNDB()) {
            lbl_save_error.setText(map.get("NDB").erro);
            lbl_save_error2.setText(map.get("NDB").tit1 + " . " + map.get("NDB").desc);
            return false;
        }

        if (!CalcularNSE()) {
            lbl_save_error.setText(map.get("NSE").erro);
            lbl_save_error2.setText(map.get("NSE").tit1 + " . " + map.get("NSE").desc);
            return false;
        }

        if (!CalcularNRD()) {
            lbl_save_error.setText(map.get("NRD").erro);
            lbl_save_error2.setText(map.get("NRD").tit1 + " . " + map.get("NRD").desc);
            return false;
        }

        if (!CalcularNST()) {
            lbl_save_error.setText(map.get("NST").erro);
            lbl_save_error2.setText(map.get("NST").tit1 + " . " + map.get("NST").desc);
            return false;
        }

        if (!CalcularNSR()) {
            lbl_save_error.setText(map.get("NSR").erro);
            lbl_save_error2.setText(map.get("NSR").tit1 + " . " + map.get("NSR").desc);
            return false;
        }

        if (!CalcularNCC()) {
            lbl_save_error.setText(map.get("NCC").erro);
            lbl_save_error2.setText(map.get("NCC").tit1 + " . " + map.get("NCC").desc);
            return false;
        }

        if (!CalcularNTE()) {
            lbl_save_error.setText(map.get("NTE").erro);
            lbl_save_error2.setText(map.get("NTE").tit1 + " . " + map.get("NTE").desc);
            return false;
        }

        if (!CalcularNEL()) {
            lbl_save_error.setText(map.get("NEL").erro);
            lbl_save_error2.setText(map.get("NEL").tit1 + " . " + map.get("NEL").desc);
            return false;
        }

        if (!CalcularNCE()) {
            lbl_save_error.setText(map.get("NCE").erro);
            lbl_save_error2.setText(map.get("NCE").tit1 + " . " + map.get("NCE").desc);
            return false;
        }

        if (!CalcularNDP()) {
            lbl_save_error.setText(map.get("NDP").erro);
            lbl_save_error2.setText(map.get("NDP").tit1 + " . " + map.get("NDP").desc);
            return false;
        }

        if (!CalcularNDS()) {
            lbl_save_error.setText(map.get("NDS").erro);
            lbl_save_error2.setText(map.get("NDS").tit1 + " . " + map.get("NDS").desc);
            return false;
        }

        if (!CalcularNES()) {
            lbl_save_error.setText(map.get("NES").erro);
            lbl_save_error2.setText(map.get("NES").tit1 + " . " + map.get("NES").desc);
            return false;
        }

        if (!CalcularNED()) {
            lbl_save_error.setText(map.get("NED").erro);
            lbl_save_error2.setText(map.get("NED").tit1 + " . " + map.get("NED").desc);
            return false;
        }

        if (!CalcularNPL()) {
            lbl_save_error.setText(map.get("NPL").erro);
            lbl_save_error2.setText(map.get("NPL").tit1 + " . " + map.get("NPL").desc);
            return false;
        }

        if (!CalcularNLT()) {
            lbl_save_error.setText(map.get("NLT").erro);
            lbl_save_error2.setText(map.get("NLT").tit1 + " . " + map.get("NLT").desc);
            return false;
        }

        if (!CalcularNVL()) {
            lbl_save_error.setText(map.get("NVL").erro);
            lbl_save_error2.setText(map.get("NVL").tit1 + " . " + map.get("NVL").desc);
            return false;
        }

        if (!CalcularNVT()) {
            lbl_save_error.setText(map.get("NVT").erro);
            lbl_save_error2.setText(map.get("NVT").tit1 + " . " + map.get("NVT").desc);
            return false;
        }

        if (!CalcularNVH()) {
            lbl_save_error.setText(map.get("NVH").erro);
            lbl_save_error2.setText(map.get("NVH").tit1 + " . " + map.get("NVH").desc);
            return false;
        }

        if (CalcularNVHC()) {
            return false;
        }

        if (!CalcularNPT()) {
            lbl_save_error.setText(map.get("NPT").erro);
            lbl_save_error2.setText(map.get("NPT").tit1 + " . " + map.get("NPT").desc);
            return false;
        }

        if (!CalcularNAS()) {
            lbl_save_error.setText(map.get("NAS").erro);
            lbl_save_error2.setText(map.get("NAS").tit1 + " . " + map.get("NAS").desc);
            return false;
        }

        if (!CalcularNWT()) {
            lbl_save_error.setText(map.get("NWT").erro);
            lbl_save_error2.setText(map.get("NWT").tit1 + " . " + map.get("NWT").desc);
            return false;
        }

        if (!CalcularNHT()) {
            lbl_save_error.setText(map.get("NHT").erro);
            lbl_save_error2.setText(map.get("NHT").tit1 + " . " + map.get("NHT").desc);
            return false;
        }

        if (!CalcularNAM()) {
            lbl_save_error.setText(map.get("NAM").erro);
            lbl_save_error2.setText(map.get("NAM").tit1 + " . " + map.get("NAM").desc);
            return false;
        }

        if (!CalcularNRM()) {
            lbl_save_error.setText(map.get("NRM").erro);
            lbl_save_error2.setText(map.get("NRM").tit1 + " . " + map.get("NRM").desc);
            return false;
        }

        if (!CalcularNEC()) {
            lbl_save_error.setText(map.get("NEC").erro);
            lbl_save_error2.setText(map.get("NEC").tit1 + " . " + map.get("NEC").desc);
            return false;
        }

        if (!CalcularNFS()) {
            lbl_save_error.setText(map.get("NFS").erro);
            lbl_save_error2.setText(map.get("NFS").tit1 + " . " + map.get("NFS").desc);
            return false;
        }

        if (!CalcularNSA()) {
            lbl_save_error.setText(map.get("NSA").erro);
            lbl_save_error2.setText(map.get("NSA").tit1 + " . " + map.get("NSA").desc);
            return false;
        }

        if (!CalcularNMS()) {
            lbl_save_error.setText(map.get("NMS").erro);
            lbl_save_error2.setText(map.get("NMS").tit1 + " . " + map.get("NMS").desc);
            return false;
        }

        if (!CalcularNHS()) {
            lbl_save_error.setText(map.get("NHS").erro);
            lbl_save_error2.setText(map.get("NHS").tit1 + " . " + map.get("NHS").desc);
            return false;
        }

        if (!CalcularNSD()) {
            lbl_save_error.setText(map.get("NSD").erro);
            lbl_save_error2.setText(map.get("NSD").tit1 + " . " + map.get("NSD").desc);
            return false;
        }

        if (!CalcularNAT()) {
            lbl_save_error.setText(map.get("NAT").erro);
            lbl_save_error2.setText(map.get("NAT").tit1 + " . " + map.get("NAT").desc);
            return false;
        }
        Main main = (Main) this.getTopLevelAncestor();
        main.cancela = false;
        main.vbod = this.bod;
        bod.EditLodosActivadsConvencUASB = true;

        if (!bod.exportarProyecto(true)) {
            bod.EditLodosActivadsConvencUASB = false;
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

    private boolean CalcularNDB() {

        try {
            lbl_NDB_error.setText("");

            if (bod.setVarDob(txt_NDB_Pregunta.getText(), "NDB", map.get("NDB").rmin, map.get("NDB").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularNDB " + ex.getMessage());
        }
        lbl_NDB_error.setText(map.get("NDB").erro);
        return false;
    }

    private boolean CalcularNSE() {

        try {
            lbl_NSE_error.setText("");

            if (bod.setVarDob(txt_NSE_Pregunta.getText(), "NSE", map.get("NSE").rmin, map.get("NSE").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularNSE " + ex.getMessage());
        }
        lbl_NSE_error.setText(map.get("NSE").erro);
        return false;
    }

    private boolean CalcularNRD() {

        try {
            lbl_NRD_error.setText("");

            if (bod.setVarDob(txt_NRD_Pregunta.getText(), "NRD", map.get("NRD").rmin, map.get("NRD").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularNRD " + ex.getMessage());
        }
        lbl_NRD_error.setText(map.get("NRD").erro);
        return false;
    }

    private boolean CalcularNST() {

        try {
            lbl_NST_error.setText("");

            if (bod.setVarDob(txt_NST_Pregunta.getText(), "NST", map.get("NST").rmin, map.get("NST").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularNST " + ex.getMessage());
        }
        lbl_NST_error.setText(map.get("NST").erro);
        return false;
    }

    private boolean CalcularNSR() {

        try {
            lbl_NSR_error.setText("");

            if (bod.setVarDob(txt_NSR_Pregunta.getText(), "NSR", map.get("NSR").rmin, map.get("NSR").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularNSR " + ex.getMessage());
        }
        lbl_NSR_error.setText(map.get("NSR").erro);
        return false;
    }

    private boolean CalcularNCC() {

        try {
            lbl_NCC_error.setText("");

            if (bod.setVarDob(txt_NCC_Pregunta.getText(), "NCC", map.get("NCC").rmin, map.get("NCC").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularNCC " + ex.getMessage());
        }
        lbl_NCC_error.setText(map.get("NCC").erro);
        return false;
    }

    private boolean CalcularNTE() {

        try {
            lbl_NTE_error.setText("");

            if (bod.setVarDob(txt_NTE_Pregunta.getText(), "NTE", map.get("NTE").rmin, map.get("NTE").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularNTE " + ex.getMessage());
        }
        lbl_NTE_error.setText(map.get("NTE").erro);
        return false;
    }

    private boolean CalcularNEL() {

        try {
            lbl_NEL_error.setText("");

            if (bod.setVarDob(txt_NEL_Pregunta.getText(), "NEL", map.get("NEL").rmin, map.get("NEL").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularNEL " + ex.getMessage());
        }
        lbl_NEL_error.setText(map.get("NEL").erro);
        return false;
    }

    private boolean CalcularNCE() {

        try {
            lbl_NCE_error.setText("");
            double x = bod.calcularNCE();

            if (bod.setVarDob(x, "NCE", map.get("NCE").rmin, map.get("NCE").rmax)) {
                txt_NCE_Pregunta.setText(bod.getVarFormateadaPorNombre("NCE", map.get("NCE").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularNCE " + ex.getMessage());
        }
        lbl_NCE_error.setText(map.get("NCE").erro);
        return false;
    }

    private boolean CalcularNDP() {

        try {
            lbl_NDP_error.setText("");
            double x = bod.calcularNDP();

            if (bod.setVarDob(x, "NDP", map.get("NDP").rmin, map.get("NDP").rmax)) {
                txt_NDP_Pregunta.setText(bod.getVarFormateadaPorNombre("NDP", map.get("NDP").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularNDP " + ex.getMessage());
        }
        lbl_NDP_error.setText(map.get("NDP").erro);
        return false;
    }

    private boolean CalcularNDS() {

        try {
            lbl_NDS_error.setText("");
            double x = bod.calcularNDS();

            if (bod.setVarDob(x, "NDS", map.get("NDS").rmin, map.get("NDS").rmax)) {
                txt_NDS_Pregunta.setText(bod.getVarFormateadaPorNombre("NDS", map.get("NDS").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularNDS " + ex.getMessage());
        }
        lbl_NDS_error.setText(map.get("NDS").erro);
        return false;
    }

    private boolean CalcularNES() {

        try {
            lbl_NES_error.setText("");
            double x = bod.calcularNES();

            if (bod.setVarDob(x, "NES", map.get("NES").rmin, map.get("NES").rmax)) {
                txt_NES_Pregunta.setText(bod.getVarFormateadaPorNombre("NES", map.get("NES").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularNES " + ex.getMessage());
        }
        lbl_NES_error.setText(map.get("NES").erro);
        return false;
    }

    private boolean CalcularNED() {

        try {
            lbl_NED_error.setText("");
            double x = bod.calcularNED();

            if (bod.setVarDob(x, "NED", map.get("NED").rmin, map.get("NED").rmax)) {
                txt_NED_Pregunta.setText(bod.getVarFormateadaPorNombre("NED", map.get("NED").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularNED " + ex.getMessage());
        }
        lbl_NED_error.setText(map.get("NED").erro);
        return false;
    }

    private boolean CalcularNPL() {

        try {
            lbl_NPL_error.setText("");
            double x = bod.calcularNPL();

            if (bod.setVarDob(x, "NPL", map.get("NPL").rmin, map.get("NPL").rmax)) {
                txt_NPL_Pregunta.setText(bod.getVarFormateadaPorNombre("NPL", map.get("NPL").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularNPL " + ex.getMessage());
        }
        lbl_NPL_error.setText(map.get("NPL").erro);
        return false;
    }

    private boolean CalcularNLT() {

        try {
            lbl_NLT_error.setText("");
            double x = bod.calcularNLT();

            if (bod.setVarDob(x, "NLT", map.get("NLT").rmin, map.get("NLT").rmax)) {
                txt_NLT_Pregunta.setText(bod.getVarFormateadaPorNombre("NLT", map.get("NLT").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularNLT " + ex.getMessage());
        }
        lbl_NLT_error.setText(map.get("NLT").erro);
        return false;
    }

    private boolean CalcularNVL() {

        try {
            lbl_NVL_error.setText("");
            double x = bod.calcularNVL();

            if (bod.setVarDob(x, "NVL", map.get("NVL").rmin, map.get("NVL").rmax)) {
                txt_NVL_Pregunta.setText(bod.getVarFormateadaPorNombre("NVL", map.get("NVL").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularNVL " + ex.getMessage());
        }
        lbl_NVL_error.setText(map.get("NVL").erro);
        return false;
    }

    private boolean CalcularNVT() {

        try {
            lbl_NVT_error.setText("");
            double x = bod.calcularNVT();

            if (bod.setVarDob(x, "NVT", map.get("NVT").rmin, map.get("NVT").rmax)) {
                txt_NVT_Pregunta.setText(bod.getVarFormateadaPorNombre("NVT", map.get("NVT").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularNVT " + ex.getMessage());
        }
        lbl_NVT_error.setText(map.get("NVT").erro);
        return false;
    }

    private boolean CalcularNVH() {

        try {
            lbl_NVH_error.setText("");
            double x = bod.calcularNVH();

            if (bod.setVarDob(x, "NVH", map.get("NVH").rmin, map.get("NVH").rmax)) {
                txt_NVH_Pregunta.setText(bod.getVarFormateadaPorNombre("NVH", map.get("NVH").deci));
                // CalcularNVHC(); //Muestra el mensaje de advertencia
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularNVH " + ex.getMessage());
        }
        lbl_NVH_error.setText(map.get("NVH").erro);
        return false;
    }

    private boolean CalcularNVHC() {

        try {
            String x = bod.calcularNVHC();

            if (!x.trim().isEmpty()) {
                int n = util.Mensaje(x, "yesno");
                if (n == JOptionPane.YES_OPTION) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception ex) {
            log.error("Error: CalcularNVHC " + ex.getMessage());
        }
        return false;
    }

    private boolean CalcularNPT() {

        try {
            lbl_NPT_error.setText("");

            if (bod.setVarDob(txt_NPT_Pregunta.getText(), "NPT", map.get("NPT").rmin, map.get("NPT").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularNPT " + ex.getMessage());
        }
        lbl_NPT_error.setText(map.get("NPT").erro);
        return false;
    }

    private boolean CalcularNAS() {

        try {
            lbl_NAS_error.setText("");
            double x = bod.calcularNAS();

            if (bod.setVarDob(x, "NAS", map.get("NAS").rmin, map.get("NAS").rmax)) {
                txt_NAS_Pregunta.setText(bod.getVarFormateadaPorNombre("NAS", map.get("NAS").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularNAS " + ex.getMessage());
        }
        lbl_NAS_error.setText(map.get("NAS").erro);
        return false;
    }

    private boolean CalcularNWT() {

        try {
            lbl_NWT_error.setText("");
            double x = bod.calcularNWT();

            if (bod.setVarDob(x, "NWT", map.get("NWT").rmin, map.get("NWT").rmax)) {
                txt_NWT_Pregunta.setText(bod.getVarFormateadaPorNombre("NWT", map.get("NWT").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularNWT " + ex.getMessage());
        }
        lbl_NWT_error.setText(map.get("NWT").erro);
        return false;
    }

    private boolean CalcularNHT() {

        try {
            lbl_NHT_error.setText("");
            double x = bod.calcularNHT();

            if (bod.setVarDob(x, "NHT", map.get("NHT").rmin, map.get("NHT").rmax)) {
                txt_NHT_Pregunta.setText(bod.getVarFormateadaPorNombre("NHT", map.get("NHT").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularNHT " + ex.getMessage());
        }
        lbl_NHT_error.setText(map.get("NHT").erro);
        return false;
    }

    private boolean CalcularNAM() {

        try {
            lbl_NAM_error.setText("");
            double x = bod.calcularNAM();

            if (bod.setVarDob(x, "NAM", map.get("NAM").rmin, map.get("NAM").rmax)) {
                txt_NAM_Pregunta.setText(bod.getVarFormateadaPorNombre("NAM", map.get("NAM").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularNAM " + ex.getMessage());
        }
        lbl_NAM_error.setText(map.get("NAM").erro);
        return false;
    }

    private boolean CalcularNRM() {

        try {
            lbl_NRM_error.setText("");

            if (bod.setVarDob(txt_NRM_Pregunta.getText(), "NRM", map.get("NRM").rmin, map.get("NRM").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularNRM " + ex.getMessage());
        }
        lbl_NRM_error.setText(map.get("NRM").erro);
        return false;
    }

    private boolean CalcularNEC() {

        try {
            lbl_NEC_error.setText("");
            double x = bod.calcularNEC();

            if (bod.setVarDob(x, "NEC", map.get("NEC").rmin, map.get("NEC").rmax)) {
                txt_NEC_Pregunta.setText(bod.getVarFormateadaPorNombre("NEC", map.get("NEC").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularNEC " + ex.getMessage());
        }
        lbl_NEC_error.setText(map.get("NEC").erro);
        return false;
    }

    private boolean CalcularNFS() {

        try {
            lbl_NFS_error.setText("");

            if (bod.setVarDob(txt_NFS_Pregunta.getText(), "NFS", map.get("NFS").rmin, map.get("NFS").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularNFS " + ex.getMessage());
        }
        lbl_NFS_error.setText(map.get("NFS").erro);
        return false;
    }

    private boolean CalcularNSA() {

        try {
            lbl_NSA_error.setText("");
            double x = bod.calcularNSA();

            if (bod.setVarDob(x, "NSA", map.get("NSA").rmin, map.get("NSA").rmax)) {
                txt_NSA_Pregunta.setText(bod.getVarFormateadaPorNombre("NSA", map.get("NSA").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularNSA " + ex.getMessage());
        }
        lbl_NSA_error.setText(map.get("NSA").erro);
        return false;
    }

    private boolean CalcularNMS() {

        try {
            lbl_NMS_error.setText("");
            double x = bod.calcularNMS();

            if (bod.setVarDob(x, "NMS", map.get("NMS").rmin, map.get("NMS").rmax)) {
                txt_NMS_Pregunta.setText(bod.getVarFormateadaPorNombre("NMS", map.get("NMS").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularNMS " + ex.getMessage());
        }
        lbl_NMS_error.setText(map.get("NMS").erro);
        return false;
    }

    private boolean CalcularNHS() {

        try {
            lbl_NHS_error.setText("");

            if (bod.setVarDob(txt_NHS_Pregunta.getText(), "NHS", map.get("NHS").rmin, map.get("NHS").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularNHS " + ex.getMessage());
        }
        lbl_NHS_error.setText(map.get("NHS").erro);
        return false;
    }

    private boolean CalcularNSD() {

        try {
            lbl_NSD_error.setText("");
            double x = bod.calcularNSD();

            if (bod.setVarDob(x, "NSD", map.get("NSD").rmin, map.get("NSD").rmax)) {
                txt_NSD_Pregunta.setText(bod.getVarFormateadaPorNombre("NSD", map.get("NSD").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularNSD " + ex.getMessage());
        }
        lbl_NSD_error.setText(map.get("NSD").erro);
        return false;
    }

    private boolean CalcularNAT() {

        try {
            lbl_NAT_error.setText("");
            double x = bod.calcularNAT();

            if (bod.setVarDob(x, "NAT", map.get("NAT").rmin, map.get("NAT").rmax)) {
                txt_NAT_Pregunta.setText(bod.getVarFormateadaPorNombre("NAT", map.get("NAT").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularNAT " + ex.getMessage());
        }
        lbl_NAT_error.setText(map.get("NAT").erro);
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
            case "NDB":
                CalcularNDB();
                ejecutarFunciones("NDS");
                ejecutarFunciones("NED");
                break;

            case "NSE":
                CalcularNSE();
                ejecutarFunciones("NDP");
                break;

            case "NRD":
                CalcularNRD();
                ejecutarFunciones("NDP");
                break;

            case "NST":
                CalcularNST();
                ejecutarFunciones("NVT");
                ejecutarFunciones("NRM");
                break;

            case "NSR":
                CalcularNSR();
                ejecutarFunciones("NRM");
                break;

            case "NCC":
                CalcularNCC();
                ejecutarFunciones("NCE");
                ejecutarFunciones("NVT");
                break;

            case "NTE":
                CalcularNTE();
                ejecutarFunciones("NCE");
                ejecutarFunciones("NVT");
                break;

            case "NEL":
                CalcularNEL();
                ejecutarFunciones("NCE");
                ejecutarFunciones("NVT");
                break;

            case "NCE":
                CalcularNCE();
                ejecutarFunciones("NPL");
                break;

            case "NDP":
                CalcularNDP();
                ejecutarFunciones("NDS");
                break;

            case "NDS":
                CalcularNDS();
                ejecutarFunciones("NES");
                ejecutarFunciones("NPL");
                ejecutarFunciones("NVT");
                ejecutarFunciones("NAM");
                break;

            case "NES":
                CalcularNES();
                break;

            case "NED":
                CalcularNED();
                break;

            case "NPL":
                CalcularNPL();
                ejecutarFunciones("NLT");
                break;

            case "NLT":
                CalcularNLT();
                ejecutarFunciones("NVL");

                break;
            case "NVL":
                CalcularNVL();
                break;

            case "NVT":
                CalcularNVT();
                ejecutarFunciones("NVH");
                ejecutarFunciones("NAS");
                ejecutarFunciones("NAM");
                ejecutarFunciones("NEC");
                break;

            case "NVH":
                CalcularNVH();
                break;

            case "NPT":
                CalcularNPT();
                ejecutarFunciones("NAS");
                break;

            case "NAS":
                CalcularNAS();
                CalcularNWT();
                CalcularNHT();
                ejecutarFunciones("NAT");
                break;

            case "NAM":
                CalcularNAM();
                break;

            case "NRM":
                CalcularNRM();
                ejecutarFunciones("NMS");
                break;

            case "NEC":
                CalcularNEC();
                break;

            case "NFS":
                CalcularNFS();
                ejecutarFunciones("NSA");
                break;

            case "NSA":
                CalcularNSA();
                ejecutarFunciones("NAT");
                CalcularNSD();
                break;

            case "NMS":
                CalcularNMS();
                break;

            case "NHS":
                CalcularNHS();
                break;

            case "NAT":
                CalcularNAT();
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
        bod.WinLodosActivadsConvencUASB = false;
        Main main = (Main) this.getTopLevelAncestor();
        main.bod.WinLodosActivadsConvencUASB = false;
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jsp_LodosActivadosModConv = new javax.swing.JScrollPane();
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
        lbl_NDB_error = new javax.swing.JLabel();
        lbl_NDB_titulo2 = new javax.swing.JLabel();
        txt_NDB_Pregunta = new javax.swing.JTextField();
        lbl_NDB_titulo1 = new javax.swing.JLabel();
        lbl_NSE_error = new javax.swing.JLabel();
        lbl_NSE_titulo2 = new javax.swing.JLabel();
        txt_NSE_Pregunta = new javax.swing.JTextField();
        lbl_NSE_titulo1 = new javax.swing.JLabel();
        btn_NSE_ayuda = new javax.swing.JButton();
        lbl_NRD_error = new javax.swing.JLabel();
        lbl_NRD_titulo2 = new javax.swing.JLabel();
        txt_NRD_Pregunta = new javax.swing.JTextField();
        lbl_NRD_titulo1 = new javax.swing.JLabel();
        btn_NDB_ayuda = new javax.swing.JButton();
        lbl_NST_error = new javax.swing.JLabel();
        lbl_NST_titulo2 = new javax.swing.JLabel();
        txt_NST_Pregunta = new javax.swing.JTextField();
        lbl_NST_titulo1 = new javax.swing.JLabel();
        lbl_NSR_error = new javax.swing.JLabel();
        lbl_NSR_titulo2 = new javax.swing.JLabel();
        txt_NSR_Pregunta = new javax.swing.JTextField();
        lbl_NSR_titulo1 = new javax.swing.JLabel();
        btn_NST_ayuda = new javax.swing.JButton();
        lbl_NCC_error = new javax.swing.JLabel();
        lbl_NCC_titulo2 = new javax.swing.JLabel();
        txt_NCC_Pregunta = new javax.swing.JTextField();
        lbl_NCC_titulo1 = new javax.swing.JLabel();
        btn_NCC_ayuda = new javax.swing.JButton();
        lbl_NTE_error = new javax.swing.JLabel();
        lbl_NTE_titulo2 = new javax.swing.JLabel();
        txt_NTE_Pregunta = new javax.swing.JTextField();
        lbl_NTE_titulo1 = new javax.swing.JLabel();
        btn_NTE_ayuda = new javax.swing.JButton();
        jSeparator12 = new javax.swing.JSeparator();
        lbl_NEL_error = new javax.swing.JLabel();
        lbl_NEL_titulo2 = new javax.swing.JLabel();
        txt_NEL_Pregunta = new javax.swing.JTextField();
        lbl_NEL_titulo1 = new javax.swing.JLabel();
        btn_NEL_ayuda = new javax.swing.JButton();
        lbl_NCE_error = new javax.swing.JLabel();
        lbl_NCE_titulo2 = new javax.swing.JLabel();
        txt_NCE_Pregunta = new javax.swing.JTextField();
        lbl_NCE_titulo1 = new javax.swing.JLabel();
        btn_NCE_ayuda = new javax.swing.JButton();
        lbl_NDP_desc = new javax.swing.JLabel();
        lbl_NDP_error = new javax.swing.JLabel();
        lbl_NDP_titulo2 = new javax.swing.JLabel();
        txt_NDP_Pregunta = new javax.swing.JTextField();
        lbl_NDP_titulo1 = new javax.swing.JLabel();
        btn_NDP_ayuda = new javax.swing.JButton();
        lbl_NDS_error = new javax.swing.JLabel();
        lbl_NDS_titulo2 = new javax.swing.JLabel();
        txt_NDS_Pregunta = new javax.swing.JTextField();
        lbl_NDS_titulo1 = new javax.swing.JLabel();
        jSeparator14 = new javax.swing.JSeparator();
        lbl_NES_error = new javax.swing.JLabel();
        lbl_NES_titulo2 = new javax.swing.JLabel();
        txt_NES_Pregunta = new javax.swing.JTextField();
        lbl_NES_titulo1 = new javax.swing.JLabel();
        lbl_NED_error = new javax.swing.JLabel();
        lbl_NED_titulo2 = new javax.swing.JLabel();
        txt_NED_Pregunta = new javax.swing.JTextField();
        lbl_NED_titulo1 = new javax.swing.JLabel();
        jSeparator15 = new javax.swing.JSeparator();
        lbl_NPL_error = new javax.swing.JLabel();
        lbl_NPL_titulo2 = new javax.swing.JLabel();
        txt_NPL_Pregunta = new javax.swing.JTextField();
        lbl_NPL_titulo1 = new javax.swing.JLabel();
        btn_NPL_ayuda = new javax.swing.JButton();
        lbl_NLT_error = new javax.swing.JLabel();
        lbl_NLT_titulo2 = new javax.swing.JLabel();
        txt_NLT_Pregunta = new javax.swing.JTextField();
        lbl_NLT_titulo1 = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        lbl_NVL_error = new javax.swing.JLabel();
        lbl_NVL_titulo2 = new javax.swing.JLabel();
        txt_NVL_Pregunta = new javax.swing.JTextField();
        lbl_NVL_titulo1 = new javax.swing.JLabel();
        lbl_NVT_error = new javax.swing.JLabel();
        lbl_NVT_titulo2 = new javax.swing.JLabel();
        txt_NVT_Pregunta = new javax.swing.JTextField();
        lbl_NVT_titulo1 = new javax.swing.JLabel();
        btn_NVT_ayuda = new javax.swing.JButton();
        lbl_NVH_error = new javax.swing.JLabel();
        lbl_NVH_titulo2 = new javax.swing.JLabel();
        txt_NVH_Pregunta = new javax.swing.JTextField();
        lbl_NVH_titulo1 = new javax.swing.JLabel();
        btn_NVH_ayuda = new javax.swing.JButton();
        lbl_NPT_error = new javax.swing.JLabel();
        lbl_NPT_titulo2 = new javax.swing.JLabel();
        txt_NPT_Pregunta = new javax.swing.JTextField();
        lbl_NPT_titulo1 = new javax.swing.JLabel();
        lbl_NAS_error = new javax.swing.JLabel();
        lbl_NAS_titulo2 = new javax.swing.JLabel();
        txt_NAS_Pregunta = new javax.swing.JTextField();
        lbl_NAS_titulo1 = new javax.swing.JLabel();
        btn_NPT_ayuda = new javax.swing.JButton();
        jSeparator16 = new javax.swing.JSeparator();
        lbl_NPT_desc = new javax.swing.JLabel();
        jSeparator17 = new javax.swing.JSeparator();
        lbl_NRM_error = new javax.swing.JLabel();
        lbl_NRM_titulo2 = new javax.swing.JLabel();
        txt_NRM_Pregunta = new javax.swing.JTextField();
        lbl_NRM_titulo1 = new javax.swing.JLabel();
        lbl_NEC_error = new javax.swing.JLabel();
        lbl_NEC_titulo2 = new javax.swing.JLabel();
        txt_NEC_Pregunta = new javax.swing.JTextField();
        lbl_NEC_titulo1 = new javax.swing.JLabel();
        btn_NEC_ayuda = new javax.swing.JButton();
        lbl_NFS_error = new javax.swing.JLabel();
        lbl_NFS_titulo2 = new javax.swing.JLabel();
        txt_NFS_Pregunta = new javax.swing.JTextField();
        lbl_NFS_titulo1 = new javax.swing.JLabel();
        btn_NFS_ayuda = new javax.swing.JButton();
        lbl_NSA_error = new javax.swing.JLabel();
        lbl_NSA_titulo2 = new javax.swing.JLabel();
        txt_NSA_Pregunta = new javax.swing.JTextField();
        lbl_NSA_titulo1 = new javax.swing.JLabel();
        btn_NSA_ayuda = new javax.swing.JButton();
        lbl_NAM_error = new javax.swing.JLabel();
        lbl_NAM_titulo2 = new javax.swing.JLabel();
        txt_NAM_Pregunta = new javax.swing.JTextField();
        lbl_NAM_titulo1 = new javax.swing.JLabel();
        btn_NRM_ayuda = new javax.swing.JButton();
        jSeparator18 = new javax.swing.JSeparator();
        lbl_NMS_error = new javax.swing.JLabel();
        lbl_NMS_titulo2 = new javax.swing.JLabel();
        txt_NMS_Pregunta = new javax.swing.JTextField();
        lbl_NMS_titulo1 = new javax.swing.JLabel();
        btn_NMS_ayuda = new javax.swing.JButton();
        lbl_NHS_error = new javax.swing.JLabel();
        lbl_NHS_titulo2 = new javax.swing.JLabel();
        txt_NHS_Pregunta = new javax.swing.JTextField();
        lbl_NHS_titulo1 = new javax.swing.JLabel();
        btn_NHS_ayuda = new javax.swing.JButton();
        lbl_NMS_desc = new javax.swing.JLabel();
        lbl_NAT_error = new javax.swing.JLabel();
        lbl_NAT_titulo2 = new javax.swing.JLabel();
        txt_NAT_Pregunta = new javax.swing.JTextField();
        lbl_NAT_titulo1 = new javax.swing.JLabel();
        btn_NVL_ayuda = new javax.swing.JButton();
        lbl_NDB_desc = new javax.swing.JLabel();
        btn_NSR_ayuda = new javax.swing.JButton();
        jSeparator19 = new javax.swing.JSeparator();
        lbl_NVT_desc = new javax.swing.JLabel();
        btn_NPT_ayuda1 = new javax.swing.JButton();
        btn_NAM_ayuda = new javax.swing.JButton();
        lbl_NFS_desc = new javax.swing.JLabel();
        lbl_NWT_error = new javax.swing.JLabel();
        lbl_NWT_titulo2 = new javax.swing.JLabel();
        txt_NWT_Pregunta = new javax.swing.JTextField();
        lbl_NWT_titulo1 = new javax.swing.JLabel();
        lbl_NHT_titulo1 = new javax.swing.JLabel();
        txt_NHT_Pregunta = new javax.swing.JTextField();
        lbl_NHT_titulo2 = new javax.swing.JLabel();
        lbl_NHT_error = new javax.swing.JLabel();
        lbl_NSD_error = new javax.swing.JLabel();
        lbl_NSD_titulo2 = new javax.swing.JLabel();
        txt_NSD_Pregunta = new javax.swing.JTextField();
        lbl_NSD_titulo1 = new javax.swing.JLabel();
        btn_close2 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1024, 2300));

        jsp_LodosActivadosModConv.setPreferredSize(new java.awt.Dimension(1024, 768));

        jp_Componentes.setPreferredSize(new java.awt.Dimension(1024, 1900));
        jp_Componentes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jp_Componentes.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 38, 1024, -1));

        lbl_titulo1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_titulo1.setForeground(new java.awt.Color(51, 51, 51));
        lbl_titulo1.setText("Titulo");
        jp_Componentes.add(lbl_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 340, 30));

        lbl_titulo2.setForeground(new java.awt.Color(0, 51, 102));
        lbl_titulo2.setText("Desc");
        jp_Componentes.add(lbl_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 0, 600, 30));
        jp_Componentes.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 168, 1024, 2));

        btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Button_Salva_Guardar_21.png"))); // NOI18N
        btn_guardar.setText("save");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1830, 120, -1));

        lbl_Q2C_titulo2.setText("Titulo");
        lbl_Q2C_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2C_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 50, 80, 25));
        jp_Componentes.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 467, 1024, 0));
        jp_Componentes.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1820, 1024, -1));

        btn_close.setText("Salir");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1870, 120, 30));

        lbl_save_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error.setText(" . . .");
        jp_Componentes.add(lbl_save_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 1830, 400, 35));

        lbl_save_error2.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error2.setText(" . . .");
        jp_Componentes.add(lbl_save_error2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 1870, 400, 35));

        txt_Q2C_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q2C_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 50, 130, 25));

        lbl_Q2C_titulo1.setText("Titulo");
        lbl_Q2C_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2C_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 300, 30));

        lbl_CABQ_titulo2.setText("Titulo");
        lbl_CABQ_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CABQ_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 90, 80, 25));

        txt_CABQ_Pregunta.setEditable(false);
        jp_Componentes.add(txt_CABQ_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 90, 130, 25));

        lbl_CABQ_titulo1.setText("Titulo");
        lbl_CABQ_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CABQ_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 300, 30));

        lbl_CVS_titulo2.setText("Titulo");
        lbl_CVS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CVS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 130, 80, 25));

        txt_CVS_Pregunta.setEditable(false);
        jp_Componentes.add(txt_CVS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 130, 130, 25));

        lbl_CVS_titulo1.setText("Titulo");
        lbl_CVS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CVS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 300, 30));

        lbl_NDB_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NDB_error.setText(" . . .");
        jp_Componentes.add(lbl_NDB_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 220, 340, 35));

        lbl_NDB_titulo2.setText("Titulo");
        lbl_NDB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NDB_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 220, 80, 25));

        txt_NDB_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_NDB_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 220, 130, 25));

        lbl_NDB_titulo1.setText("Titulo");
        lbl_NDB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NDB_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 300, 30));

        lbl_NSE_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NSE_error.setText(" . . .");
        jp_Componentes.add(lbl_NSE_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 260, 340, 35));

        lbl_NSE_titulo2.setText("Titulo");
        lbl_NSE_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NSE_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 260, 80, 25));

        txt_NSE_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_NSE_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 260, 130, 25));

        lbl_NSE_titulo1.setText("Titulo");
        lbl_NSE_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NSE_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 300, 30));

        btn_NSE_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_NSE_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_NSE_ayuda.setContentAreaFilled(false);
        btn_NSE_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_NSE_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 260, 25, 25));

        lbl_NRD_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NRD_error.setText(" . . .");
        jp_Componentes.add(lbl_NRD_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 300, 340, 35));

        lbl_NRD_titulo2.setText("Titulo");
        lbl_NRD_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NRD_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 300, 80, 25));

        txt_NRD_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_NRD_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 300, 130, 25));

        lbl_NRD_titulo1.setText("Titulo");
        lbl_NRD_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NRD_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 300, 30));

        btn_NDB_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_NDB_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_NDB_ayuda.setContentAreaFilled(false);
        btn_NDB_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_NDB_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 220, 25, 25));

        lbl_NST_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NST_error.setText(" . . .");
        jp_Componentes.add(lbl_NST_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 340, 340, 35));

        lbl_NST_titulo2.setText("Titulo");
        lbl_NST_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NST_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 340, 80, 25));

        txt_NST_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_NST_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 340, 130, 25));

        lbl_NST_titulo1.setText("Titulo");
        lbl_NST_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NST_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 300, 30));

        lbl_NSR_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NSR_error.setText(" . . .");
        jp_Componentes.add(lbl_NSR_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 380, 340, 35));

        lbl_NSR_titulo2.setText("Titulo");
        lbl_NSR_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NSR_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 380, 80, 25));

        txt_NSR_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_NSR_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 380, 130, 25));

        lbl_NSR_titulo1.setText("Titulo");
        lbl_NSR_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NSR_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 300, 30));

        btn_NST_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_NST_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_NST_ayuda.setContentAreaFilled(false);
        btn_NST_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_NST_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 340, 25, 25));

        lbl_NCC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NCC_error.setText(" . . .");
        jp_Componentes.add(lbl_NCC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 420, 340, 35));

        lbl_NCC_titulo2.setText("Titulo");
        lbl_NCC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NCC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 420, 80, 25));

        txt_NCC_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_NCC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 420, 130, 25));

        lbl_NCC_titulo1.setText("Titulo");
        lbl_NCC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NCC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 420, 300, 30));

        btn_NCC_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_NCC_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_NCC_ayuda.setContentAreaFilled(false);
        btn_NCC_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_NCC_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 420, 25, 25));

        lbl_NTE_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NTE_error.setText(" . . .");
        jp_Componentes.add(lbl_NTE_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 460, 340, 35));

        lbl_NTE_titulo2.setText("Titulo");
        lbl_NTE_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NTE_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 460, 80, 25));

        txt_NTE_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_NTE_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 460, 130, 25));

        lbl_NTE_titulo1.setText("Titulo");
        lbl_NTE_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NTE_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 300, 30));

        btn_NTE_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_NTE_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_NTE_ayuda.setContentAreaFilled(false);
        btn_NTE_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_NTE_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 460, 25, 25));
        jp_Componentes.add(jSeparator12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 580, 1024, 2));

        lbl_NEL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NEL_error.setText(" . . .");
        jp_Componentes.add(lbl_NEL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 500, 340, 35));

        lbl_NEL_titulo2.setText("Titulo");
        lbl_NEL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NEL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 500, 80, 25));

        txt_NEL_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_NEL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 500, 130, 25));

        lbl_NEL_titulo1.setText("Titulo");
        lbl_NEL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NEL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 500, 300, 30));

        btn_NEL_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_NEL_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_NEL_ayuda.setContentAreaFilled(false);
        btn_NEL_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_NEL_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 500, 25, 25));

        lbl_NCE_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NCE_error.setText(" . . .");
        jp_Componentes.add(lbl_NCE_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 540, 340, 35));

        lbl_NCE_titulo2.setText("Titulo");
        lbl_NCE_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NCE_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 540, 80, 25));

        txt_NCE_Pregunta.setEditable(false);
        jp_Componentes.add(txt_NCE_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 540, 130, 25));

        lbl_NCE_titulo1.setText("Titulo");
        lbl_NCE_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NCE_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 540, 300, 30));

        btn_NCE_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_NCE_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_NCE_ayuda.setContentAreaFilled(false);
        btn_NCE_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_NCE_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 540, 25, 25));

        lbl_NDP_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_NDP_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_NDP_desc.setText("Desc");
        lbl_NDP_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NDP_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 590, 610, 30));

        lbl_NDP_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NDP_error.setText(" . . .");
        jp_Componentes.add(lbl_NDP_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 630, 340, 35));

        lbl_NDP_titulo2.setText("Titulo");
        lbl_NDP_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NDP_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 630, 80, 25));

        txt_NDP_Pregunta.setEditable(false);
        jp_Componentes.add(txt_NDP_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 630, 130, 25));

        lbl_NDP_titulo1.setText("Titulo");
        lbl_NDP_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NDP_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 630, 300, 30));

        btn_NDP_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_NDP_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_NDP_ayuda.setContentAreaFilled(false);
        btn_NDP_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_NDP_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 630, 25, 25));

        lbl_NDS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NDS_error.setText(" . . .");
        jp_Componentes.add(lbl_NDS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 670, 340, 35));

        lbl_NDS_titulo2.setText("Titulo");
        lbl_NDS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NDS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 670, 80, 25));

        txt_NDS_Pregunta.setEditable(false);
        jp_Componentes.add(txt_NDS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 670, 130, 25));

        lbl_NDS_titulo1.setText("Titulo");
        lbl_NDS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NDS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 670, 300, 30));
        jp_Componentes.add(jSeparator14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 790, 1024, 2));

        lbl_NES_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NES_error.setText(" . . .");
        jp_Componentes.add(lbl_NES_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 710, 340, 35));

        lbl_NES_titulo2.setText("Titulo");
        lbl_NES_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NES_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 710, 80, 25));

        txt_NES_Pregunta.setEditable(false);
        jp_Componentes.add(txt_NES_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 710, 130, 25));

        lbl_NES_titulo1.setText("Titulo");
        lbl_NES_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NES_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 710, 300, 30));

        lbl_NED_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NED_error.setText(" . . .");
        jp_Componentes.add(lbl_NED_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 750, 340, 35));

        lbl_NED_titulo2.setText("Titulo");
        lbl_NED_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NED_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 750, 80, 25));

        txt_NED_Pregunta.setEditable(false);
        jp_Componentes.add(txt_NED_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 750, 130, 25));

        lbl_NED_titulo1.setText("Titulo");
        lbl_NED_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NED_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 750, 300, 30));
        jp_Componentes.add(jSeparator15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 900, 1024, -1));

        lbl_NPL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NPL_error.setText(" . . .");
        jp_Componentes.add(lbl_NPL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 810, 340, 35));

        lbl_NPL_titulo2.setText("Titulo");
        lbl_NPL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NPL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 810, 80, 25));

        txt_NPL_Pregunta.setEditable(false);
        jp_Componentes.add(txt_NPL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 810, 130, 25));

        lbl_NPL_titulo1.setText("Titulo");
        lbl_NPL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NPL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 810, 300, 30));

        btn_NPL_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_NPL_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_NPL_ayuda.setContentAreaFilled(false);
        btn_NPL_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_NPL_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 810, 25, 25));

        lbl_NLT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NLT_error.setText(" . . .");
        jp_Componentes.add(lbl_NLT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 850, 340, 35));

        lbl_NLT_titulo2.setText("Titulo");
        lbl_NLT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NLT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 850, 80, 25));

        txt_NLT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_NLT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 850, 130, 25));

        lbl_NLT_titulo1.setText("Titulo");
        lbl_NLT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NLT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 850, 300, 30));
        jp_Componentes.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1110, 1024, -1));

        lbl_NVL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NVL_error.setText(" . . .");
        jp_Componentes.add(lbl_NVL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 920, 340, 35));

        lbl_NVL_titulo2.setText("Titulo");
        lbl_NVL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NVL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 920, 80, 25));

        txt_NVL_Pregunta.setEditable(false);
        jp_Componentes.add(txt_NVL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 920, 130, 25));

        lbl_NVL_titulo1.setText("Titulo");
        lbl_NVL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NVL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 920, 300, 30));

        lbl_NVT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NVT_error.setText(" . . .");
        jp_Componentes.add(lbl_NVT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1020, 340, 35));

        lbl_NVT_titulo2.setText("Titulo");
        lbl_NVT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NVT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1020, 80, 25));

        txt_NVT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_NVT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1020, 130, 25));

        lbl_NVT_titulo1.setText("Titulo");
        lbl_NVT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NVT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1020, 300, 30));

        btn_NVT_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_NVT_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_NVT_ayuda.setContentAreaFilled(false);
        btn_NVT_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_NVT_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1020, 25, 25));

        lbl_NVH_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NVH_error.setText(" . . .");
        jp_Componentes.add(lbl_NVH_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1060, 340, 35));

        lbl_NVH_titulo2.setText("Titulo");
        lbl_NVH_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NVH_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1060, 80, 25));

        txt_NVH_Pregunta.setEditable(false);
        jp_Componentes.add(txt_NVH_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1060, 130, 25));

        lbl_NVH_titulo1.setText("Titulo");
        lbl_NVH_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NVH_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1060, 300, 30));

        btn_NVH_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_NVH_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_NVH_ayuda.setContentAreaFilled(false);
        btn_NVH_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_NVH_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1060, 25, 25));

        lbl_NPT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NPT_error.setText(" . . .");
        jp_Componentes.add(lbl_NPT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1160, 340, 35));

        lbl_NPT_titulo2.setText("Titulo");
        lbl_NPT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NPT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1160, 80, 25));

        txt_NPT_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_NPT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1160, 130, 25));

        lbl_NPT_titulo1.setText("Titulo");
        lbl_NPT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NPT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1160, 300, 30));

        lbl_NAS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NAS_error.setText(" . . .");
        jp_Componentes.add(lbl_NAS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1200, 340, 35));

        lbl_NAS_titulo2.setText("Titulo");
        lbl_NAS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NAS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1200, 80, 25));

        txt_NAS_Pregunta.setEditable(false);
        jp_Componentes.add(txt_NAS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1200, 130, 25));

        lbl_NAS_titulo1.setText("Titulo");
        lbl_NAS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NAS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1200, 300, 30));

        btn_NPT_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_NPT_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_NPT_ayuda.setContentAreaFilled(false);
        btn_NPT_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_NPT_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 1120, 25, 25));
        jp_Componentes.add(jSeparator16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1320, 1024, 2));

        lbl_NPT_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_NPT_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_NPT_desc.setText("Desc");
        lbl_NPT_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NPT_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1120, 610, 30));
        jp_Componentes.add(jSeparator17, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1460, 1024, 2));

        lbl_NRM_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NRM_error.setText(" . . .");
        jp_Componentes.add(lbl_NRM_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 1380, 340, 35));

        lbl_NRM_titulo2.setText("Titulo");
        lbl_NRM_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NRM_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1380, 80, 25));

        txt_NRM_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_NRM_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1380, 130, 25));

        lbl_NRM_titulo1.setText("Titulo");
        lbl_NRM_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NRM_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1380, 300, 30));

        lbl_NEC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NEC_error.setText(" . . .");
        jp_Componentes.add(lbl_NEC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 1420, 340, 35));

        lbl_NEC_titulo2.setText("Titulo");
        lbl_NEC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NEC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1420, 80, 25));

        txt_NEC_Pregunta.setEditable(false);
        jp_Componentes.add(txt_NEC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1420, 130, 25));

        lbl_NEC_titulo1.setText("Titulo");
        lbl_NEC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NEC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1420, 300, 30));

        btn_NEC_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_NEC_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_NEC_ayuda.setContentAreaFilled(false);
        btn_NEC_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_NEC_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1420, 25, 25));

        lbl_NFS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NFS_error.setText(" . . .");
        jp_Componentes.add(lbl_NFS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1510, 340, 35));

        lbl_NFS_titulo2.setText("Titulo");
        lbl_NFS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NFS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1510, 80, 25));

        txt_NFS_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_NFS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1510, 130, 25));

        lbl_NFS_titulo1.setText("Titulo");
        lbl_NFS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NFS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1510, 300, 30));

        btn_NFS_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_NFS_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_NFS_ayuda.setContentAreaFilled(false);
        btn_NFS_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_NFS_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1510, 25, 25));

        lbl_NSA_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NSA_error.setText(" . . .");
        jp_Componentes.add(lbl_NSA_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1550, 340, 35));

        lbl_NSA_titulo2.setText("Titulo");
        lbl_NSA_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NSA_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1550, 80, 25));

        txt_NSA_Pregunta.setEditable(false);
        jp_Componentes.add(txt_NSA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1550, 130, 25));

        lbl_NSA_titulo1.setText("Titulo");
        lbl_NSA_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NSA_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1550, 300, 30));

        btn_NSA_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_NSA_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_NSA_ayuda.setContentAreaFilled(false);
        btn_NSA_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_NSA_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1550, 25, 25));

        lbl_NAM_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NAM_error.setText(" . . .");
        jp_Componentes.add(lbl_NAM_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 1340, 340, 35));

        lbl_NAM_titulo2.setText("Titulo");
        lbl_NAM_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NAM_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1340, 110, 25));

        txt_NAM_Pregunta.setEditable(false);
        jp_Componentes.add(txt_NAM_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1340, 130, 25));

        lbl_NAM_titulo1.setText("Titulo");
        lbl_NAM_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NAM_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1340, 300, 30));

        btn_NRM_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_NRM_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_NRM_ayuda.setContentAreaFilled(false);
        btn_NRM_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_NRM_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1380, 25, 25));
        jp_Componentes.add(jSeparator18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1590, 1024, 2));

        lbl_NMS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NMS_error.setText(" . . .");
        jp_Componentes.add(lbl_NMS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1650, 340, 35));

        lbl_NMS_titulo2.setText("Titulo");
        lbl_NMS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NMS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1650, 80, 25));

        txt_NMS_Pregunta.setEditable(false);
        jp_Componentes.add(txt_NMS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1650, 130, 25));

        lbl_NMS_titulo1.setText("Titulo");
        lbl_NMS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NMS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1650, 300, 30));

        btn_NMS_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_NMS_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_NMS_ayuda.setContentAreaFilled(false);
        btn_NMS_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_NMS_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1650, 25, 25));

        lbl_NHS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NHS_error.setText(" . . .");
        jp_Componentes.add(lbl_NHS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1690, 340, 35));

        lbl_NHS_titulo2.setText("Titulo");
        lbl_NHS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NHS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1690, 80, 25));

        txt_NHS_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_NHS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1690, 130, 25));

        lbl_NHS_titulo1.setText("Titulo");
        lbl_NHS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NHS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1690, 300, 30));

        btn_NHS_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_NHS_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_NHS_ayuda.setContentAreaFilled(false);
        btn_NHS_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_NHS_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1690, 25, 25));

        lbl_NMS_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_NMS_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_NMS_desc.setText("Desc");
        lbl_NMS_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NMS_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1610, 610, 30));

        lbl_NAT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NAT_error.setText(" . . .");
        jp_Componentes.add(lbl_NAT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1770, 340, 35));

        lbl_NAT_titulo2.setText("Titulo");
        lbl_NAT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NAT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1770, 80, 25));

        txt_NAT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_NAT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1770, 130, 25));

        lbl_NAT_titulo1.setText("Titulo");
        lbl_NAT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NAT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1770, 300, 30));

        btn_NVL_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_NVL_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_NVL_ayuda.setContentAreaFilled(false);
        btn_NVL_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_NVL_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 920, 25, 25));

        lbl_NDB_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_NDB_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_NDB_desc.setText("Desc");
        lbl_NDB_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NDB_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 610, 30));

        btn_NSR_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_NSR_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_NSR_ayuda.setContentAreaFilled(false);
        btn_NSR_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_NSR_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 380, 25, 25));
        jp_Componentes.add(jSeparator19, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 970, 1024, 2));

        lbl_NVT_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_NVT_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_NVT_desc.setText("Desc");
        lbl_NVT_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NVT_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 980, 610, 30));

        btn_NPT_ayuda1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_NPT_ayuda1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_NPT_ayuda1.setContentAreaFilled(false);
        btn_NPT_ayuda1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_NPT_ayuda1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1160, 25, 25));

        btn_NAM_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_NAM_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_NAM_ayuda.setContentAreaFilled(false);
        btn_NAM_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_NAM_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1340, 25, 25));

        lbl_NFS_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_NFS_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_NFS_desc.setText("Desc");
        lbl_NFS_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NFS_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1470, 610, 30));

        lbl_NWT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NWT_error.setText(" . . .");
        jp_Componentes.add(lbl_NWT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1240, 340, 35));

        lbl_NWT_titulo2.setText("Titulo");
        lbl_NWT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NWT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1240, 80, 25));

        txt_NWT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_NWT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1240, 130, 25));

        lbl_NWT_titulo1.setText("Titulo");
        lbl_NWT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NWT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1240, 300, 30));

        lbl_NHT_titulo1.setText("Titulo");
        lbl_NHT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NHT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1280, 300, 30));

        txt_NHT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_NHT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1280, 130, 25));

        lbl_NHT_titulo2.setText("Titulo");
        lbl_NHT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NHT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1280, 80, 25));

        lbl_NHT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NHT_error.setText(" . . .");
        jp_Componentes.add(lbl_NHT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1280, 340, 35));

        lbl_NSD_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NSD_error.setText(" . . .");
        jp_Componentes.add(lbl_NSD_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1730, 340, 35));

        lbl_NSD_titulo2.setText("Titulo");
        lbl_NSD_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NSD_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1730, 80, 25));

        txt_NSD_Pregunta.setEditable(false);
        jp_Componentes.add(txt_NSD_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1730, 130, 25));

        lbl_NSD_titulo1.setText("Titulo");
        lbl_NSD_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NSD_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1730, 300, 30));

        jsp_LodosActivadosModConv.setViewportView(jp_Componentes);
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
            .addComponent(jsp_LodosActivadosModConv, javax.swing.GroupLayout.PREFERRED_SIZE, 1008, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_close2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btn_close2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(jsp_LodosActivadosModConv, javax.swing.GroupLayout.DEFAULT_SIZE, 2205, Short.MAX_VALUE)
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
    private javax.swing.JButton btn_NAM_ayuda;
    private javax.swing.JButton btn_NCC_ayuda;
    private javax.swing.JButton btn_NCE_ayuda;
    private javax.swing.JButton btn_NDB_ayuda;
    private javax.swing.JButton btn_NDP_ayuda;
    private javax.swing.JButton btn_NEC_ayuda;
    private javax.swing.JButton btn_NEL_ayuda;
    private javax.swing.JButton btn_NFS_ayuda;
    private javax.swing.JButton btn_NHS_ayuda;
    private javax.swing.JButton btn_NMS_ayuda;
    private javax.swing.JButton btn_NPL_ayuda;
    private javax.swing.JButton btn_NPT_ayuda;
    private javax.swing.JButton btn_NPT_ayuda1;
    private javax.swing.JButton btn_NRM_ayuda;
    private javax.swing.JButton btn_NSA_ayuda;
    private javax.swing.JButton btn_NSE_ayuda;
    private javax.swing.JButton btn_NSR_ayuda;
    private javax.swing.JButton btn_NST_ayuda;
    private javax.swing.JButton btn_NTE_ayuda;
    private javax.swing.JButton btn_NVH_ayuda;
    private javax.swing.JButton btn_NVL_ayuda;
    private javax.swing.JButton btn_NVT_ayuda;
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
    private javax.swing.JScrollPane jsp_LodosActivadosModConv;
    private javax.swing.JLabel lbl_CABQ_titulo1;
    private javax.swing.JLabel lbl_CABQ_titulo2;
    private javax.swing.JLabel lbl_CVS_titulo1;
    private javax.swing.JLabel lbl_CVS_titulo2;
    private javax.swing.JLabel lbl_NAM_error;
    private javax.swing.JLabel lbl_NAM_titulo1;
    private javax.swing.JLabel lbl_NAM_titulo2;
    private javax.swing.JLabel lbl_NAS_error;
    private javax.swing.JLabel lbl_NAS_titulo1;
    private javax.swing.JLabel lbl_NAS_titulo2;
    private javax.swing.JLabel lbl_NAT_error;
    private javax.swing.JLabel lbl_NAT_titulo1;
    private javax.swing.JLabel lbl_NAT_titulo2;
    private javax.swing.JLabel lbl_NCC_error;
    private javax.swing.JLabel lbl_NCC_titulo1;
    private javax.swing.JLabel lbl_NCC_titulo2;
    private javax.swing.JLabel lbl_NCE_error;
    private javax.swing.JLabel lbl_NCE_titulo1;
    private javax.swing.JLabel lbl_NCE_titulo2;
    private javax.swing.JLabel lbl_NDB_desc;
    private javax.swing.JLabel lbl_NDB_error;
    private javax.swing.JLabel lbl_NDB_titulo1;
    private javax.swing.JLabel lbl_NDB_titulo2;
    private javax.swing.JLabel lbl_NDP_desc;
    private javax.swing.JLabel lbl_NDP_error;
    private javax.swing.JLabel lbl_NDP_titulo1;
    private javax.swing.JLabel lbl_NDP_titulo2;
    private javax.swing.JLabel lbl_NDS_error;
    private javax.swing.JLabel lbl_NDS_titulo1;
    private javax.swing.JLabel lbl_NDS_titulo2;
    private javax.swing.JLabel lbl_NEC_error;
    private javax.swing.JLabel lbl_NEC_titulo1;
    private javax.swing.JLabel lbl_NEC_titulo2;
    private javax.swing.JLabel lbl_NED_error;
    private javax.swing.JLabel lbl_NED_titulo1;
    private javax.swing.JLabel lbl_NED_titulo2;
    private javax.swing.JLabel lbl_NEL_error;
    private javax.swing.JLabel lbl_NEL_titulo1;
    private javax.swing.JLabel lbl_NEL_titulo2;
    private javax.swing.JLabel lbl_NES_error;
    private javax.swing.JLabel lbl_NES_titulo1;
    private javax.swing.JLabel lbl_NES_titulo2;
    private javax.swing.JLabel lbl_NFS_desc;
    private javax.swing.JLabel lbl_NFS_error;
    private javax.swing.JLabel lbl_NFS_titulo1;
    private javax.swing.JLabel lbl_NFS_titulo2;
    private javax.swing.JLabel lbl_NHS_error;
    private javax.swing.JLabel lbl_NHS_titulo1;
    private javax.swing.JLabel lbl_NHS_titulo2;
    private javax.swing.JLabel lbl_NHT_error;
    private javax.swing.JLabel lbl_NHT_titulo1;
    private javax.swing.JLabel lbl_NHT_titulo2;
    private javax.swing.JLabel lbl_NLT_error;
    private javax.swing.JLabel lbl_NLT_titulo1;
    private javax.swing.JLabel lbl_NLT_titulo2;
    private javax.swing.JLabel lbl_NMS_desc;
    private javax.swing.JLabel lbl_NMS_error;
    private javax.swing.JLabel lbl_NMS_titulo1;
    private javax.swing.JLabel lbl_NMS_titulo2;
    private javax.swing.JLabel lbl_NPL_error;
    private javax.swing.JLabel lbl_NPL_titulo1;
    private javax.swing.JLabel lbl_NPL_titulo2;
    private javax.swing.JLabel lbl_NPT_desc;
    private javax.swing.JLabel lbl_NPT_error;
    private javax.swing.JLabel lbl_NPT_titulo1;
    private javax.swing.JLabel lbl_NPT_titulo2;
    private javax.swing.JLabel lbl_NRD_error;
    private javax.swing.JLabel lbl_NRD_titulo1;
    private javax.swing.JLabel lbl_NRD_titulo2;
    private javax.swing.JLabel lbl_NRM_error;
    private javax.swing.JLabel lbl_NRM_titulo1;
    private javax.swing.JLabel lbl_NRM_titulo2;
    private javax.swing.JLabel lbl_NSA_error;
    private javax.swing.JLabel lbl_NSA_titulo1;
    private javax.swing.JLabel lbl_NSA_titulo2;
    private javax.swing.JLabel lbl_NSD_error;
    private javax.swing.JLabel lbl_NSD_titulo1;
    private javax.swing.JLabel lbl_NSD_titulo2;
    private javax.swing.JLabel lbl_NSE_error;
    private javax.swing.JLabel lbl_NSE_titulo1;
    private javax.swing.JLabel lbl_NSE_titulo2;
    private javax.swing.JLabel lbl_NSR_error;
    private javax.swing.JLabel lbl_NSR_titulo1;
    private javax.swing.JLabel lbl_NSR_titulo2;
    private javax.swing.JLabel lbl_NST_error;
    private javax.swing.JLabel lbl_NST_titulo1;
    private javax.swing.JLabel lbl_NST_titulo2;
    private javax.swing.JLabel lbl_NTE_error;
    private javax.swing.JLabel lbl_NTE_titulo1;
    private javax.swing.JLabel lbl_NTE_titulo2;
    private javax.swing.JLabel lbl_NVH_error;
    private javax.swing.JLabel lbl_NVH_titulo1;
    private javax.swing.JLabel lbl_NVH_titulo2;
    private javax.swing.JLabel lbl_NVL_error;
    private javax.swing.JLabel lbl_NVL_titulo1;
    private javax.swing.JLabel lbl_NVL_titulo2;
    private javax.swing.JLabel lbl_NVT_desc;
    private javax.swing.JLabel lbl_NVT_error;
    private javax.swing.JLabel lbl_NVT_titulo1;
    private javax.swing.JLabel lbl_NVT_titulo2;
    private javax.swing.JLabel lbl_NWT_error;
    private javax.swing.JLabel lbl_NWT_titulo1;
    private javax.swing.JLabel lbl_NWT_titulo2;
    private javax.swing.JLabel lbl_Q2C_titulo1;
    private javax.swing.JLabel lbl_Q2C_titulo2;
    private javax.swing.JLabel lbl_save_error;
    private javax.swing.JLabel lbl_save_error2;
    private javax.swing.JLabel lbl_titulo1;
    private javax.swing.JLabel lbl_titulo2;
    private javax.swing.JTextField txt_CABQ_Pregunta;
    private javax.swing.JTextField txt_CVS_Pregunta;
    private javax.swing.JTextField txt_NAM_Pregunta;
    private javax.swing.JTextField txt_NAS_Pregunta;
    private javax.swing.JTextField txt_NAT_Pregunta;
    private javax.swing.JTextField txt_NCC_Pregunta;
    private javax.swing.JTextField txt_NCE_Pregunta;
    private javax.swing.JTextField txt_NDB_Pregunta;
    private javax.swing.JTextField txt_NDP_Pregunta;
    private javax.swing.JTextField txt_NDS_Pregunta;
    private javax.swing.JTextField txt_NEC_Pregunta;
    private javax.swing.JTextField txt_NED_Pregunta;
    private javax.swing.JTextField txt_NEL_Pregunta;
    private javax.swing.JTextField txt_NES_Pregunta;
    private javax.swing.JTextField txt_NFS_Pregunta;
    private javax.swing.JTextField txt_NHS_Pregunta;
    private javax.swing.JTextField txt_NHT_Pregunta;
    private javax.swing.JTextField txt_NLT_Pregunta;
    private javax.swing.JTextField txt_NMS_Pregunta;
    private javax.swing.JTextField txt_NPL_Pregunta;
    private javax.swing.JTextField txt_NPT_Pregunta;
    private javax.swing.JTextField txt_NRD_Pregunta;
    private javax.swing.JTextField txt_NRM_Pregunta;
    private javax.swing.JTextField txt_NSA_Pregunta;
    private javax.swing.JTextField txt_NSD_Pregunta;
    private javax.swing.JTextField txt_NSE_Pregunta;
    private javax.swing.JTextField txt_NSR_Pregunta;
    private javax.swing.JTextField txt_NST_Pregunta;
    private javax.swing.JTextField txt_NTE_Pregunta;
    private javax.swing.JTextField txt_NVH_Pregunta;
    private javax.swing.JTextField txt_NVL_Pregunta;
    private javax.swing.JTextField txt_NVT_Pregunta;
    private javax.swing.JTextField txt_NWT_Pregunta;
    private javax.swing.JTextField txt_Q2C_Pregunta;
    // End of variables declaration//GEN-END:variables
}