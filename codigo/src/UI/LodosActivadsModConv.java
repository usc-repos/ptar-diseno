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

public class LodosActivadsModConv extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("Lodos Activados Modalidad Conv.");
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    public Map<String, Pregunta> map = new TreeMap<>();
    PopupMenuListener listener;
    private Bod bod = new Bod("");
    private Listener_Popup Lpopup;
    Util util = new Util();
    Pregunta pg;
    private boolean eSave = true;

    public LodosActivadsModConv(Bod bod) {
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
        bod.WinLodosActivadsConvenc = true;//Bandera La ventana se ha abierto
        btn_guardar.setText("Guardar");
        btn_close.setText("Cerrar");
        try {
            // - - - - - - Cargar el titulo de la sección  
            rs = db.ResultadoSelect("obtenerseccion", "12");

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
            // - - - - - - Pregunta 02 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MCH
            rs = db.ResultadoSelect("datospregunta", "MCH");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MCH", pg);

            lbl_MCH_titulo1.setText(pg.tit1);
            lbl_MCH_titulo2.setText(rs.getString("titulo2"));
            txt_MCH_Pregunta.setText(rs.getString("valorpordefecto"));
            lbl_MCH_desc.setText(pg.desc);

            txt_MCH_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("MCH");
                }
            });

            AsignarPopupBtn(btn_MCH_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 280);
            // - - - - - - Pregunta 03 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MAR
            rs = db.ResultadoSelect("datospregunta", "MAR");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MAR", pg);

            lbl_MAR_titulo1.setText(pg.tit1);
            lbl_MAR_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 04 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MPS
            rs = db.ResultadoSelect("datospregunta", "MPS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MPS", pg);

            lbl_MPS_titulo1.setText(pg.tit1);
            lbl_MPS_titulo2.setText(rs.getString("titulo2"));
            txt_MPS_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_MPS_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("MPS");
                }
            });

            AsignarPopupBtn(btn_MPS_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 460, 170);
            // - - - - - - Pregunta 05 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MTR
            rs = db.ResultadoSelect("datospregunta", "MTR");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MTR", pg);

            lbl_MTR_titulo1.setText(pg.tit1);
            lbl_MTR_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_MTR_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 480, 150);
            // - - - - - - Pregunta 06 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MGC
            rs = db.ResultadoSelect("datospregunta", "MGC");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MGC", pg);

            lbl_MGC_titulo1.setText(pg.tit1);

            String[] t2 = rs.getString("titulo2").split("\\|");
            lbl_MGC_titulo2.setText(t2[0]);
            lbl_MGC_titulo3.setText(t2[1]);
            lbl_MGC_titulo4.setText(t2[2]);
            lbl_MGC_desc.setText(pg.desc);

            AsignarPopupBtn(btn_MGC_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 390, 440);
            // - - - - - - Pregunta 07 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MGA
            rs = db.ResultadoSelect("datospregunta", "MGA");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MGA", pg);

            lbl_MGA_titulo1.setText(pg.tit1);
            lbl_MGA_titulo2.setText(rs.getString("titulo2"));

            txt_MGA_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("MGA");
                }
            });
            // - - - - - - Pregunta 08 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MGL
            rs = db.ResultadoSelect("datospregunta", "MGL");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MGL", pg);

            lbl_MGL_titulo1.setText(pg.tit1);
            lbl_MGL_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 09 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MDB
            rs = db.ResultadoSelect("datospregunta", "MDB");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MDB", pg);

            lbl_MDB_titulo1.setText(pg.tit1);
            lbl_MDB_titulo2.setText(rs.getString("titulo2"));
            txt_MDB_Pregunta.setText(rs.getString("valorpordefecto"));
            lbl_MDB_desc.setText(pg.desc);

            txt_MDB_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("MDB");
                }
            });
            AsignarPopupBtn(btn_MDB_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 490, 320);
            // - - - - - - Pregunta 10 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MSE
            rs = db.ResultadoSelect("datospregunta", "MSE");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MSE", pg);

            lbl_MSE_titulo1.setText(pg.tit1);
            lbl_MSE_titulo2.setText(rs.getString("titulo2"));
            txt_MSE_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_MSE_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("MSE");
                }
            });

            AsignarPopupBtn(btn_MSE_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 540, 260);
            // - - - - - - Pregunta 11 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MRD
            rs = db.ResultadoSelect("datospregunta", "MRD");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MRD", pg);

            lbl_MRD_titulo1.setText(pg.tit1);
            lbl_MRD_titulo2.setText(rs.getString("titulo2"));
            txt_MRD_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_MRD_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("MRD");
                }
            });
            // - - - - - - Pregunta 12 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MST
            rs = db.ResultadoSelect("datospregunta", "MST");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MST", pg);

            lbl_MST_titulo1.setText(pg.tit1);
            lbl_MST_titulo2.setText(rs.getString("titulo2"));
            txt_MST_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_MST_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("MST");
                }
            });

            AsignarPopupBtn(btn_MST_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 500, 240);
            // - - - - - - Pregunta 13 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MSR
            rs = db.ResultadoSelect("datospregunta", "MSR");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MSR", pg);

            lbl_MSR_titulo1.setText(pg.tit1);
            lbl_MSR_titulo2.setText(rs.getString("titulo2"));
            txt_MSR_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_MSR_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("MSR");
                }
            });
            AsignarPopupBtn(btn_MSR_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 490, 150);
            // - - - - - - Pregunta 14 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MCC
            rs = db.ResultadoSelect("datospregunta", "MCC");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MCC", pg);

            lbl_MCC_titulo1.setText(pg.tit1);
            lbl_MCC_titulo2.setText(rs.getString("titulo2"));
            txt_MCC_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_MCC_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("MCC");
                }
            });

            AsignarPopupBtn(btn_MCC_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 580, 230);
            // - - - - - - Pregunta 15 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MTE
            rs = db.ResultadoSelect("datospregunta", "MTE");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MTE", pg);

            lbl_MTE_titulo1.setText(pg.tit1);
            lbl_MTE_titulo2.setText(rs.getString("titulo2"));
            txt_MTE_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_MTE_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("MTE");
                }
            });

            AsignarPopupBtn(btn_MTE_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 490, 170);
            // - - - - - - Pregunta 16 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MEL
            rs = db.ResultadoSelect("datospregunta", "MEL");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MEL", pg);

            lbl_MEL_titulo1.setText(pg.tit1);
            lbl_MEL_titulo2.setText(rs.getString("titulo2"));
            txt_MEL_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_MEL_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("MEL");
                }
            });

            AsignarPopupBtn(btn_MEL_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 450, 180);
            // - - - - - - Pregunta 17 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MCE
            rs = db.ResultadoSelect("datospregunta", "MCE");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MCE", pg);

            lbl_MCE_titulo1.setText(pg.tit1);
            lbl_MCE_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_MCE_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 480, 270);
            // - - - - - - Pregunta 18 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MDP
            rs = db.ResultadoSelect("datospregunta", "MDP");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MDP", pg);

            lbl_MDP_titulo1.setText(pg.tit1);
            lbl_MDP_titulo2.setText(rs.getString("titulo2"));
            lbl_MDP_desc.setText(pg.desc);

            AsignarPopupBtn(btn_MDP_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 460, 180);
            // - - - - - - Pregunta 19 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MDS
            rs = db.ResultadoSelect("datospregunta", "MDS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MDS", pg);

            lbl_MDS_titulo1.setText(pg.tit1);
            lbl_MDS_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 20 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MES
            rs = db.ResultadoSelect("datospregunta", "MES");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MES", pg);

            lbl_MES_titulo1.setText(pg.tit1);
            lbl_MES_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 21 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MED
            rs = db.ResultadoSelect("datospregunta", "MED");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MED", pg);

            lbl_MED_titulo1.setText(pg.tit1);
            lbl_MED_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 22 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MPL
            rs = db.ResultadoSelect("datospregunta", "MPL");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MPL", pg);

            lbl_MPL_titulo1.setText(pg.tit1);
            lbl_MPL_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_MPL_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 560, 560);
            // - - - - - - Pregunta 23 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MLT
            rs = db.ResultadoSelect("datospregunta", "MLT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MLT", pg);

            lbl_MLT_titulo1.setText(pg.tit1);
            lbl_MLT_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 24 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MVL
            rs = db.ResultadoSelect("datospregunta", "MVL");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MVL", pg);

            lbl_MVL_titulo1.setText(pg.tit1);
            lbl_MVL_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_MVL_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 460, 280);
            // - - - - - - Pregunta 25 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MVT
            rs = db.ResultadoSelect("datospregunta", "MVT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MVT", pg);

            lbl_MVT_titulo1.setText(pg.tit1);
            lbl_MVT_titulo2.setText(rs.getString("titulo2"));
            lbl_MVT_desc.setText(pg.desc);

            AsignarPopupBtn(btn_MVT_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 320, 340);
            // - - - - - - Pregunta 26 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MVH
            rs = db.ResultadoSelect("datospregunta", "MVH");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MVH", pg);

            lbl_MVH_titulo1.setText(pg.tit1);
            lbl_MVH_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_MVH_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 350, 170);
            // - - - - - - Pregunta 27 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MPT
            rs = db.ResultadoSelect("datospregunta", "MPT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MPT", pg);

            lbl_MPT_titulo1.setText(pg.tit1);
            lbl_MPT_titulo2.setText(rs.getString("titulo2"));
            txt_MPT_Pregunta.setText(rs.getString("valorpordefecto"));
            lbl_MPT_desc.setText(pg.desc);

            txt_MPT_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("MPT");
                }
            });

            AsignarPopupBtn(btn_MPT_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 440, 160);
            AsignarPopupBtn(btn_MPT_ayuda1, rs.getString("ayuda2"), rs.getString("ayudalink"), 500, 150);
            // - - - - - - Pregunta 28 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MAS
            rs = db.ResultadoSelect("datospregunta", "MAS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MAS", pg);

            lbl_MAS_titulo1.setText(pg.tit1);
            lbl_MAS_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 28A Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MWT
            rs = db.ResultadoSelect("datospregunta", "MWT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MWT", pg);

            lbl_MWT_titulo1.setText(pg.tit1);
            lbl_MWT_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 28 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MHT
            rs = db.ResultadoSelect("datospregunta", "MHT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MHT", pg);

            lbl_MHT_titulo1.setText(pg.tit1);
            lbl_MHT_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 29 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MAM
            rs = db.ResultadoSelect("datospregunta", "MAM");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MAM", pg);

            lbl_MAM_titulo1.setText(pg.tit1);
            lbl_MAM_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_MAM_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 460, 420);
            // - - - - - - Pregunta 30 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MRM
            rs = db.ResultadoSelect("datospregunta", "MRM");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MRM", pg);

            lbl_MRM_titulo1.setText(pg.tit1);
            lbl_MRM_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_MRM_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 360, 220);
            // - - - - - - Pregunta 31 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MEC
            rs = db.ResultadoSelect("datospregunta", "MEC");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MEC", pg);

            lbl_MEC_titulo1.setText(pg.tit1);
            lbl_MEC_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_MEC_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 360, 250);
            // - - - - - - Pregunta 32 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MFS
            rs = db.ResultadoSelect("datospregunta", "MFS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MFS", pg);

            lbl_MFS_titulo1.setText(pg.tit1);
            lbl_MFS_titulo2.setText(rs.getString("titulo2"));
            txt_MFS_Pregunta.setText(rs.getString("valorpordefecto"));
            lbl_MFS_desc.setText(pg.desc);

            txt_MFS_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("MFS");
                }
            });

            AsignarPopupBtn(btn_MFS_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 540, 320);
            // - - - - - - Pregunta 33 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MSA
            rs = db.ResultadoSelect("datospregunta", "MSA");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MSA", pg);

            lbl_MSA_titulo1.setText(pg.tit1);
            lbl_MSA_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_MSA_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 320, 240);
            // - - - - - - Pregunta 34 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MMS
            rs = db.ResultadoSelect("datospregunta", "MMS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MMS", pg);

            lbl_MMS_titulo1.setText(pg.tit1);
            lbl_MMS_titulo2.setText(rs.getString("titulo2"));
            lbl_MMS_desc.setText(pg.desc);

            AsignarPopupBtn(btn_MMS_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 490, 280);
            // - - - - - - Pregunta 35 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MHS
            rs = db.ResultadoSelect("datospregunta", "MHS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MHS", pg);

            lbl_MHS_titulo1.setText(pg.tit1);
            lbl_MHS_titulo2.setText(rs.getString("titulo2"));
            txt_MHS_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_MHS_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("MHS");
                }
            });

            AsignarPopupBtn(btn_MHS_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 430, 150);
            // - - - - - - Pregunta 36 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MSD
            rs = db.ResultadoSelect("datospregunta", "MSD");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MSD", pg);

            lbl_MSD_titulo1.setText(pg.tit1);
            lbl_MSD_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 37 Cuestionario 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MAT
            rs = db.ResultadoSelect("datospregunta", "MAT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MAT", pg);

            lbl_MAT_titulo1.setText(pg.tit1);
            lbl_MAT_titulo2.setText(rs.getString("titulo2"));

            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - Si ya han sido llenado correctamente estos datos se obtienen de Bod
            if (bod.EditLodosActivadsConvenc) {
                txt_MCH_Pregunta.setText(bod.getVarFormateadaPorNombre("MCH", map.get("MCH").deci)); //Trae la variable por nombre y la formatea por cantidad de decimales
                txt_MAR_Pregunta.setText(bod.getVarFormateadaPorNombre("MAR", map.get("MAR").deci));
                txt_MPS_Pregunta.setText(bod.getVarFormateadaPorNombre("MPS", map.get("MPS").deci));
                txt_MTR_Pregunta.setText(bod.getVarFormateadaPorNombre("MTR", map.get("MTR").deci));
                txt_MGC_Pregunta.setText(bod.getVarFormateadaPorNombre("MGC", map.get("MGC").deci));
                txt_MGA_Pregunta.setText(bod.getVarFormateadaPorNombre("MGA", map.get("MGA").deci));
                txt_MGL_Pregunta.setText(bod.getVarFormateadaPorNombre("MGL", map.get("MGL").deci));
                txt_MDB_Pregunta.setText(bod.getVarFormateadaPorNombre("MDB", map.get("MDB").deci));
                txt_MSE_Pregunta.setText(bod.getVarFormateadaPorNombre("MSE", map.get("MSE").deci));
                txt_MRD_Pregunta.setText(bod.getVarFormateadaPorNombre("MRD", map.get("MRD").deci));
                txt_MST_Pregunta.setText(bod.getVarFormateadaPorNombre("MST", map.get("MST").deci));
                txt_MSR_Pregunta.setText(bod.getVarFormateadaPorNombre("MSR", map.get("MSR").deci));
                txt_MCC_Pregunta.setText(bod.getVarFormateadaPorNombre("MCC", map.get("MCC").deci));
                txt_MTE_Pregunta.setText(bod.getVarFormateadaPorNombre("MTE", map.get("MTE").deci));
                txt_MEL_Pregunta.setText(bod.getVarFormateadaPorNombre("MEL", map.get("MEL").deci));
                txt_MCE_Pregunta.setText(bod.getVarFormateadaPorNombre("MCE", map.get("MCE").deci));
                txt_MDP_Pregunta.setText(bod.getVarFormateadaPorNombre("MDP", map.get("MDP").deci));
                txt_MDS_Pregunta.setText(bod.getVarFormateadaPorNombre("MDS", map.get("MDS").deci));
                txt_MES_Pregunta.setText(bod.getVarFormateadaPorNombre("MES", map.get("MES").deci));
                txt_MED_Pregunta.setText(bod.getVarFormateadaPorNombre("MED", map.get("MED").deci));
                txt_MPL_Pregunta.setText(bod.getVarFormateadaPorNombre("MPL", map.get("MPL").deci));
                txt_MLT_Pregunta.setText(bod.getVarFormateadaPorNombre("MLT", map.get("MLT").deci));
                txt_MVL_Pregunta.setText(bod.getVarFormateadaPorNombre("MVL", map.get("MVL").deci));
                txt_MVT_Pregunta.setText(bod.getVarFormateadaPorNombre("MVT", map.get("MVT").deci));
                txt_MVH_Pregunta.setText(bod.getVarFormateadaPorNombre("MVH", map.get("MVH").deci));
                txt_MPT_Pregunta.setText(bod.getVarFormateadaPorNombre("MPT", map.get("MPT").deci));
                txt_MAS_Pregunta.setText(bod.getVarFormateadaPorNombre("MAS", map.get("MAS").deci));
                txt_MWT_Pregunta.setText(bod.getVarFormateadaPorNombre("MWT", map.get("MWT").deci));
                txt_MHT_Pregunta.setText(bod.getVarFormateadaPorNombre("MHT", map.get("MHT").deci));
                txt_MAM_Pregunta.setText(bod.getVarFormateadaPorNombre("MAM", map.get("MAM").deci));
                txt_MRM_Pregunta.setText(bod.getVarFormateadaPorNombre("MRM", map.get("MRM").deci));
                txt_MEC_Pregunta.setText(bod.getVarFormateadaPorNombre("MEC", map.get("MEC").deci));
                txt_MFS_Pregunta.setText(bod.getVarFormateadaPorNombre("MFS", map.get("MFS").deci));
                txt_MSA_Pregunta.setText(bod.getVarFormateadaPorNombre("MSA", map.get("MSA").deci));
                txt_MMS_Pregunta.setText(bod.getVarFormateadaPorNombre("MMS", map.get("MMS").deci));
                txt_MHS_Pregunta.setText(bod.getVarFormateadaPorNombre("MHS", map.get("MHS").deci));
                txt_MSD_Pregunta.setText(bod.getVarFormateadaPorNombre("MSD", map.get("MSD").deci));
                txt_MAT_Pregunta.setText(bod.getVarFormateadaPorNombre("MAT", map.get("MAT").deci));                
                
            } else {
                ejecutarFunciones("MCH");
                ejecutarFunciones("MPS");
                ejecutarFunciones("MDB");
                ejecutarFunciones("MSE");
                ejecutarFunciones("MRD");
                ejecutarFunciones("MST");
                ejecutarFunciones("MSR");
                ejecutarFunciones("MCC");
                ejecutarFunciones("MTE");
                ejecutarFunciones("MEL");
                ejecutarFunciones("MPT");
                ejecutarFunciones("MFS");
                ejecutarFunciones("MHS");
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
        lbl_MAM_error.setText("");
        lbl_MAR_error.setText("");
        lbl_MAS_error.setText("");
        lbl_MAT_error.setText("");
        lbl_MCC_error.setText("");
        lbl_MCE_error.setText("");
        lbl_MCH_error.setText("");
        lbl_MDB_error.setText("");
        lbl_MDP_error.setText("");
        lbl_MDS_error.setText("");
        lbl_MEC_error.setText("");
        lbl_MED_error.setText("");
        lbl_MEL_error.setText("");
        lbl_MES_error.setText("");
        lbl_MFS_error.setText("");
        lbl_MGA_error.setText("");
        lbl_MGC_error.setText("");
        lbl_MGL_error.setText("");
        lbl_MHS_error.setText("");
        lbl_MHT_error.setText("");
        lbl_MLT_error.setText("");
        lbl_MMS_error.setText("");
        lbl_MPL_error.setText("");
        lbl_MPS_error.setText("");
        lbl_MPT_error.setText("");
        lbl_MRD_error.setText("");
        lbl_MRM_error.setText("");
        lbl_MSA_error.setText("");
        lbl_MSD_error.setText("");
        lbl_MSE_error.setText("");
        lbl_MSR_error.setText("");
        lbl_MST_error.setText("");
        lbl_MTE_error.setText("");
        lbl_MTR_error.setText("");
        lbl_MVH_error.setText("");
        lbl_MVL_error.setText("");
        lbl_MVT_error.setText("");
        lbl_MWT_error.setText("");
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

        if (!CalcularMCH()) {
            lbl_save_error.setText(map.get("MCH").erro);
            lbl_save_error2.setText(map.get("MCH").tit1 + " . " + map.get("MCH").desc);
            return false;
        }

        if (!CalcularMAR()) {
            lbl_save_error.setText(map.get("MAR").erro);
            lbl_save_error2.setText(map.get("MAR").tit1 + " . " + map.get("MAR").desc);
            return false;
        }

        if (!CalcularMPS()) {
            lbl_save_error.setText(map.get("MPS").erro);
            lbl_save_error2.setText(map.get("MPS").tit1 + " . " + map.get("MPS").desc);
            return false;
        }

        if (!CalcularMTR()) {
            lbl_save_error.setText(map.get("MTR").erro);
            lbl_save_error2.setText(map.get("MTR").tit1 + " . " + map.get("MTR").desc);
            return false;
        }

        if (!CalcularMGC()) {
            lbl_save_error.setText(map.get("MGC").erro);
            lbl_save_error2.setText(map.get("MGC").tit1 + " . " + map.get("MGC").desc);
            return false;
        }

        if (!CalcularMGA()) {
            lbl_save_error.setText(map.get("MGA").erro);
            lbl_save_error2.setText(map.get("MGA").tit1 + " . " + map.get("MGA").desc);
            return false;
        }

        if (!CalcularMGL()) {
            lbl_save_error.setText(map.get("MGL").erro);
            lbl_save_error2.setText(map.get("MGL").tit1 + " . " + map.get("MGL").desc);
            return false;
        }

        if (!CalcularMDB()) {
            lbl_save_error.setText(map.get("MDB").erro);
            lbl_save_error2.setText(map.get("MDB").tit1 + " . " + map.get("MDB").desc);
            return false;
        }

        if (!CalcularMSE()) {
            lbl_save_error.setText(map.get("MSE").erro);
            lbl_save_error2.setText(map.get("MSE").tit1 + " . " + map.get("MSE").desc);
            return false;
        }

        if (!CalcularMRD()) {
            lbl_save_error.setText(map.get("MRD").erro);
            lbl_save_error2.setText(map.get("MRD").tit1 + " . " + map.get("MRD").desc);
            return false;
        }

        if (!CalcularMST()) {
            lbl_save_error.setText(map.get("MST").erro);
            lbl_save_error2.setText(map.get("MST").tit1 + " . " + map.get("MST").desc);
            return false;
        }

        if (!CalcularMSR()) {
            lbl_save_error.setText(map.get("MSR").erro);
            lbl_save_error2.setText(map.get("MSR").tit1 + " . " + map.get("MSR").desc);
            return false;
        }

        if (!CalcularMCC()) {
            lbl_save_error.setText(map.get("MCC").erro);
            lbl_save_error2.setText(map.get("MCC").tit1 + " . " + map.get("MCC").desc);
            return false;
        }

        if (!CalcularMTE()) {
            lbl_save_error.setText(map.get("MTE").erro);
            lbl_save_error2.setText(map.get("MTE").tit1 + " . " + map.get("MTE").desc);
            return false;
        }

        if (!CalcularMEL()) {
            lbl_save_error.setText(map.get("MEL").erro);
            lbl_save_error2.setText(map.get("MEL").tit1 + " . " + map.get("MEL").desc);
            return false;
        }

        if (!CalcularMCE()) {
            lbl_save_error.setText(map.get("MCE").erro);
            lbl_save_error2.setText(map.get("MCE").tit1 + " . " + map.get("MCE").desc);
            return false;
        }

        if (!CalcularMDP()) {
            lbl_save_error.setText(map.get("MDP").erro);
            lbl_save_error2.setText(map.get("MDP").tit1 + " . " + map.get("MDP").desc);
            return false;
        }

        if (!CalcularMDS()) {
            lbl_save_error.setText(map.get("MDS").erro);
            lbl_save_error2.setText(map.get("MDS").tit1 + " . " + map.get("MDS").desc);
            return false;
        }

        if (!CalcularMES()) {
            lbl_save_error.setText(map.get("MES").erro);
            lbl_save_error2.setText(map.get("MES").tit1 + " . " + map.get("MES").desc);
            return false;
        }

        if (!CalcularMED()) {
            lbl_save_error.setText(map.get("MED").erro);
            lbl_save_error2.setText(map.get("MED").tit1 + " . " + map.get("MED").desc);
            return false;
        }

        if (!CalcularMPL()) {
            lbl_save_error.setText(map.get("MPL").erro);
            lbl_save_error2.setText(map.get("MPL").tit1 + " . " + map.get("MPL").desc);
            return false;
        }

        if (!CalcularMLT()) {
            lbl_save_error.setText(map.get("MLT").erro);
            lbl_save_error2.setText(map.get("MLT").tit1 + " . " + map.get("MLT").desc);
            return false;
        }

        if (!CalcularMVL()) {
            lbl_save_error.setText(map.get("MVL").erro);
            lbl_save_error2.setText(map.get("MVL").tit1 + " . " + map.get("MVL").desc);
            return false;
        }

        if (!CalcularMVT()) {
            lbl_save_error.setText(map.get("MVT").erro);
            lbl_save_error2.setText(map.get("MVT").tit1 + " . " + map.get("MVT").desc);
            return false;
        }

        if (!CalcularMVH()) {
            lbl_save_error.setText(map.get("MVH").erro);
            lbl_save_error2.setText(map.get("MVH").tit1 + " . " + map.get("MVH").desc);
            return false;
        }

        if (CalcularMVHC()) {
            return false;
        }

        if (!CalcularMPT()) {
            lbl_save_error.setText(map.get("MPT").erro);
            lbl_save_error2.setText(map.get("MPT").tit1 + " . " + map.get("MPT").desc);
            return false;
        }

        if (!CalcularMAS()) {
            lbl_save_error.setText(map.get("MAS").erro);
            lbl_save_error2.setText(map.get("MAS").tit1 + " . " + map.get("MAS").desc);
            return false;
        }

        if (!CalcularMWT()) {
            lbl_save_error.setText(map.get("MWT").erro);
            lbl_save_error2.setText(map.get("MWT").tit1 + " . " + map.get("MWT").desc);
            return false;
        }

        if (!CalcularMHT()) {
            lbl_save_error.setText(map.get("MHT").erro);
            lbl_save_error2.setText(map.get("MHT").tit1 + " . " + map.get("MHT").desc);
            return false;
        }

        if (!CalcularMAM()) {
            lbl_save_error.setText(map.get("MAM").erro);
            lbl_save_error2.setText(map.get("MAM").tit1 + " . " + map.get("MAM").desc);
            return false;
        }

        if (!CalcularMRM()) {
            lbl_save_error.setText(map.get("MRM").erro);
            lbl_save_error2.setText(map.get("MRM").tit1 + " . " + map.get("MRM").desc);
            return false;
        }

        if (!CalcularMEC()) {
            lbl_save_error.setText(map.get("MEC").erro);
            lbl_save_error2.setText(map.get("MEC").tit1 + " . " + map.get("MEC").desc);
            return false;
        }

        if (!CalcularMFS()) {
            lbl_save_error.setText(map.get("MFS").erro);
            lbl_save_error2.setText(map.get("MFS").tit1 + " . " + map.get("MFS").desc);
            return false;
        }

        if (!CalcularMSA()) {
            lbl_save_error.setText(map.get("MSA").erro);
            lbl_save_error2.setText(map.get("MSA").tit1 + " . " + map.get("MSA").desc);
            return false;
        }

        if (!CalcularMMS()) {
            lbl_save_error.setText(map.get("MMS").erro);
            lbl_save_error2.setText(map.get("MMS").tit1 + " . " + map.get("MMS").desc);
            return false;
        }

        if (!CalcularMHS()) {
            lbl_save_error.setText(map.get("MHS").erro);
            lbl_save_error2.setText(map.get("MHS").tit1 + " . " + map.get("MHS").desc);
            return false;
        }

        if (!CalcularMSD()) {
            lbl_save_error.setText(map.get("MSD").erro);
            lbl_save_error2.setText(map.get("MSD").tit1 + " . " + map.get("MSD").desc);
            return false;
        }

        if (!CalcularMAT()) {
            lbl_save_error.setText(map.get("MAT").erro);
            lbl_save_error2.setText(map.get("MAT").tit1 + " . " + map.get("MAT").desc);
            return false;
        }
        Main main = (Main) this.getTopLevelAncestor();
        main.cancela = false;
        main.vbod = this.bod;
        bod.EditLodosActivadsConvenc = true;

        if (!bod.exportarProyecto(true)) {
            bod.EditLodosActivadsConvenc = false;
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

    private boolean CalcularMCH() {

        try {
            lbl_MCH_error.setText("");

            if (bod.setVarDob(txt_MCH_Pregunta.getText(), "MCH", map.get("MCH").rmin, map.get("MCH").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularMCH " + ex.getMessage());
        }
        lbl_MCH_error.setText(map.get("MCH").erro);
        return false;
    }

    private boolean CalcularMAR() {

        try {
            lbl_MAR_error.setText("");
            double x = bod.calcularMAR();

            if (bod.setVarDob(x, "MAR", map.get("MAR").rmin, map.get("MAR").rmax)) {
                txt_MAR_Pregunta.setText(bod.getVarFormateadaPorNombre("MAR", map.get("MAR").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMAR " + ex.getMessage());
        }
        lbl_MAR_error.setText(map.get("MAR").erro);
        return false;
    }

    private boolean CalcularMPS() {

        try {
            lbl_MPS_error.setText("");

            if (bod.setVarDob(txt_MPS_Pregunta.getText(), "MPS", map.get("MPS").rmin, map.get("MPS").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularMPS " + ex.getMessage());
        }
        lbl_MPS_error.setText(map.get("MPS").erro);
        return false;
    }

    private boolean CalcularMTR() {

        try {
            lbl_MTR_error.setText("");
            double x = bod.calcularMTR();

            if (bod.setVarDob(x, "MTR", map.get("MTR").rmin, map.get("MTR").rmax)) {
                txt_MTR_Pregunta.setText(bod.getVarFormateadaPorNombre("MTR", map.get("MTR").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMTR " + ex.getMessage());
        }
        lbl_MTR_error.setText(map.get("MTR").erro);
        return false;
    }

    private boolean CalcularMGC() {

        try {
            lbl_MGC_error.setText("");
            double x = bod.calcularMGC();

            if (bod.setVarDob(x, "MGC", map.get("MGC").rmin, map.get("MGC").rmax)) {
                txt_MGC_Pregunta.setText(bod.getVarFormateadaPorNombre("MGC", map.get("MGC").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMGC " + ex.getMessage());
        }
        lbl_MGC_error.setText(map.get("MGC").erro);
        return false;
    }

    private boolean CalcularMGA() {

        try {
            lbl_MGA_error.setText("");

            if (txt_MGA_Pregunta.getText().trim().equals("") || txt_MGA_Pregunta.getText().trim().equals("0")) { //Opcional
                bod.setVarDob(0, "MGA", 0, 1);
                lbl_MGL_error.setText("");
                return true;
            }

            if (bod.setVarDob(txt_MGA_Pregunta.getText(), "MGA", map.get("MGA").rmin, map.get("MGA").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularMGA " + ex.getMessage());
        }
        lbl_MGA_error.setText(map.get("MGA").erro);
        return false;
    }

    private boolean CalcularMGL() {

        if (txt_MGA_Pregunta.getText().trim().equals("") || txt_MGA_Pregunta.getText().trim().equals("0")) { //Opcional
            bod.setVarDob(0, "MGL", 0, 1);
            lbl_MGL_error.setText("");
            return true;
        }
        
        try {
            lbl_MGL_error.setText("");
            double x = bod.calcularMGL();

            if (bod.setVarDob(x, "MGL", map.get("MGL").rmin, map.get("MGL").rmax)) {
                txt_MGL_Pregunta.setText(bod.getVarFormateadaPorNombre("MGL", map.get("MGL").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMGL " + ex.getMessage());
        }
        lbl_MGL_error.setText(map.get("MGL").erro);
        return false;
    }

    private boolean CalcularMDB() {

        try {
            lbl_MDB_error.setText("");

            if (bod.setVarDob(txt_MDB_Pregunta.getText(), "MDB", map.get("MDB").rmin, map.get("MDB").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularMDB " + ex.getMessage());
        }
        lbl_MDB_error.setText(map.get("MDB").erro);
        return false;
    }

    private boolean CalcularMSE() {

        try {
            lbl_MSE_error.setText("");

            if (bod.setVarDob(txt_MSE_Pregunta.getText(), "MSE", map.get("MSE").rmin, map.get("MSE").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularMSE " + ex.getMessage());
        }
        lbl_MSE_error.setText(map.get("MSE").erro);
        return false;
    }

    private boolean CalcularMRD() {

        try {
            lbl_MRD_error.setText("");

            if (bod.setVarDob(txt_MRD_Pregunta.getText(), "MRD", map.get("MRD").rmin, map.get("MRD").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularMRD " + ex.getMessage());
        }
        lbl_MRD_error.setText(map.get("MRD").erro);
        return false;
    }

    private boolean CalcularMST() {

        try {
            lbl_MST_error.setText("");

            if (bod.setVarDob(txt_MST_Pregunta.getText(), "MST", map.get("MST").rmin, map.get("MST").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularMST " + ex.getMessage());
        }
        lbl_MST_error.setText(map.get("MST").erro);
        return false;
    }

    private boolean CalcularMSR() {

        try {
            lbl_MSR_error.setText("");

            if (bod.setVarDob(txt_MSR_Pregunta.getText(), "MSR", map.get("MSR").rmin, map.get("MSR").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularMSR " + ex.getMessage());
        }
        lbl_MSR_error.setText(map.get("MSR").erro);
        return false;
    }

    private boolean CalcularMCC() {

        try {
            lbl_MCC_error.setText("");

            if (bod.setVarDob(txt_MCC_Pregunta.getText(), "MCC", map.get("MCC").rmin, map.get("MCC").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularMCC " + ex.getMessage());
        }
        lbl_MCC_error.setText(map.get("MCC").erro);
        return false;
    }

    private boolean CalcularMTE() {

        try {
            lbl_MTE_error.setText("");

            if (bod.setVarDob(txt_MTE_Pregunta.getText(), "MTE", map.get("MTE").rmin, map.get("MTE").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularMTE " + ex.getMessage());
        }
        lbl_MTE_error.setText(map.get("MTE").erro);
        return false;
    }

    private boolean CalcularMEL() {

        try {
            lbl_MEL_error.setText("");

            if (bod.setVarDob(txt_MEL_Pregunta.getText(), "MEL", map.get("MEL").rmin, map.get("MEL").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularMEL " + ex.getMessage());
        }
        lbl_MEL_error.setText(map.get("MEL").erro);
        return false;
    }

    private boolean CalcularMCE() {

        try {
            lbl_MCE_error.setText("");
            double x = bod.calcularMCE();

            if (bod.setVarDob(x, "MCE", map.get("MCE").rmin, map.get("MCE").rmax)) {
                txt_MCE_Pregunta.setText(bod.getVarFormateadaPorNombre("MCE", map.get("MCE").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMCE " + ex.getMessage());
        }
        lbl_MCE_error.setText(map.get("MCE").erro);
        return false;
    }

    private boolean CalcularMDP() {

        try {
            lbl_MDP_error.setText("");
            double x = bod.calcularMDP();

            if (bod.setVarDob(x, "MDP", map.get("MDP").rmin, map.get("MDP").rmax)) {
                txt_MDP_Pregunta.setText(bod.getVarFormateadaPorNombre("MDP", map.get("MDP").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMDP " + ex.getMessage());
        }
        lbl_MDP_error.setText(map.get("MDP").erro);
        return false;
    }

    private boolean CalcularMDS() {

        try {
            lbl_MDS_error.setText("");
            double x = bod.calcularMDS();

            if (bod.setVarDob(x, "MDS", map.get("MDS").rmin, map.get("MDS").rmax)) {
                txt_MDS_Pregunta.setText(bod.getVarFormateadaPorNombre("MDS", map.get("MDS").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMDS " + ex.getMessage());
        }
        lbl_MDS_error.setText(map.get("MDS").erro);
        return false;
    }

    private boolean CalcularMES() {

        try {
            lbl_MES_error.setText("");
            double x = bod.calcularMES();

            if (bod.setVarDob(x, "MES", map.get("MES").rmin, map.get("MES").rmax)) {
                txt_MES_Pregunta.setText(bod.getVarFormateadaPorNombre("MES", map.get("MES").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMES " + ex.getMessage());
        }
        lbl_MES_error.setText(map.get("MES").erro);
        return false;
    }

    private boolean CalcularMED() {

        try {
            lbl_MED_error.setText("");
            double x = bod.calcularMED();

            if (bod.setVarDob(x, "MED", map.get("MED").rmin, map.get("MED").rmax)) {
                txt_MED_Pregunta.setText(bod.getVarFormateadaPorNombre("MED", map.get("MED").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMED " + ex.getMessage());
        }
        lbl_MED_error.setText(map.get("MED").erro);
        return false;
    }

    private boolean CalcularMPL() {

        try {
            lbl_MPL_error.setText("");
            double x = bod.calcularMPL();

            if (bod.setVarDob(x, "MPL", map.get("MPL").rmin, map.get("MPL").rmax)) {
                txt_MPL_Pregunta.setText(bod.getVarFormateadaPorNombre("MPL", map.get("MPL").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMPL " + ex.getMessage());
        }
        lbl_MPL_error.setText(map.get("MPL").erro);
        return false;
    }

    private boolean CalcularMLT() {

        try {
            lbl_MLT_error.setText("");
            double x = bod.calcularMLT();

            if (bod.setVarDob(x, "MLT", map.get("MLT").rmin, map.get("MLT").rmax)) {
                txt_MLT_Pregunta.setText(bod.getVarFormateadaPorNombre("MLT", map.get("MLT").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMLT " + ex.getMessage());
        }
        lbl_MLT_error.setText(map.get("MLT").erro);
        return false;
    }

    private boolean CalcularMVL() {

        try {
            lbl_MVL_error.setText("");
            double x = bod.calcularMVL();

            if (bod.setVarDob(x, "MVL", map.get("MVL").rmin, map.get("MVL").rmax)) {
                txt_MVL_Pregunta.setText(bod.getVarFormateadaPorNombre("MVL", map.get("MVL").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMVL " + ex.getMessage());
        }
        lbl_MVL_error.setText(map.get("MVL").erro);
        return false;
    }

    private boolean CalcularMVT() {

        try {
            lbl_MVT_error.setText("");
            double x = bod.calcularMVT();

            if (bod.setVarDob(x, "MVT", map.get("MVT").rmin, map.get("MVT").rmax)) {
                txt_MVT_Pregunta.setText(bod.getVarFormateadaPorNombre("MVT", map.get("MVT").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMVT " + ex.getMessage());
        }
        lbl_MVT_error.setText(map.get("MVT").erro);
        return false;
    }

    private boolean CalcularMVH() {

        try {
            lbl_MVH_error.setText("");
            double x = bod.calcularMVH();

            if (bod.setVarDob(x, "MVH", map.get("MVH").rmin, map.get("MVH").rmax)) {
                txt_MVH_Pregunta.setText(bod.getVarFormateadaPorNombre("MVH", map.get("MVH").deci));
                //CalcularMVHC();
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMVH " + ex.getMessage());
        }
        lbl_MVH_error.setText(map.get("MVH").erro);
        return false;
    }

    private boolean CalcularMVHC() {

        try {
            String x = bod.calcularMVHC();

            if (!x.trim().isEmpty()) {
                int n = util.Mensaje(x, "yesno");
                if (n == JOptionPane.YES_OPTION) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMVHC " + ex.getMessage());
        }
        return false;
    }

    private boolean CalcularMPT() {

        try {
            lbl_MPT_error.setText("");

            if (bod.setVarDob(txt_MPT_Pregunta.getText(), "MPT", map.get("MPT").rmin, map.get("MPT").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularMPT " + ex.getMessage());
        }
        lbl_MPT_error.setText(map.get("MPT").erro);
        return false;
    }

    private boolean CalcularMAS() {

        try {
            lbl_MAS_error.setText("");
            double x = bod.calcularMAS();

            if (bod.setVarDob(x, "MAS", map.get("MAS").rmin, map.get("MAS").rmax)) {
                txt_MAS_Pregunta.setText(bod.getVarFormateadaPorNombre("MAS", map.get("MAS").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMAS " + ex.getMessage());
        }
        lbl_MAS_error.setText(map.get("MAS").erro);
        return false;
    }

    private boolean CalcularMWT() {

        try {
            lbl_MWT_error.setText("");
            double x = bod.calcularMWT();

            if (bod.setVarDob(x, "MWT", map.get("MWT").rmin, map.get("MWT").rmax)) {
                txt_MWT_Pregunta.setText(bod.getVarFormateadaPorNombre("MWT", map.get("MWT").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMWT " + ex.getMessage());
        }
        lbl_MWT_error.setText(map.get("MWT").erro);
        return false;
    }

    private boolean CalcularMHT() {

        try {
            lbl_MHT_error.setText("");
            double x = bod.calcularMHT();

            if (bod.setVarDob(x, "MHT", map.get("MHT").rmin, map.get("MHT").rmax)) {
                txt_MHT_Pregunta.setText(bod.getVarFormateadaPorNombre("MHT", map.get("MHT").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMHT " + ex.getMessage());
        }
        lbl_MHT_error.setText(map.get("MHT").erro);
        return false;
    }

    private boolean CalcularMAM() {

        try {
            lbl_MAM_error.setText("");
            double x = bod.calcularMAM();

            if (bod.setVarDob(x, "MAM", map.get("MAM").rmin, map.get("MAM").rmax)) {
                txt_MAM_Pregunta.setText(bod.getVarFormateadaPorNombre("MAM", map.get("MAM").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMAM " + ex.getMessage());
        }
        lbl_MAM_error.setText(map.get("MAM").erro);
        return false;
    }

    private boolean CalcularMRM() {

        try {
            lbl_MRM_error.setText("");
            double x = bod.calcularMRM();

            if (bod.setVarDob(x, "MRM", map.get("MRM").rmin, map.get("MRM").rmax)) {
                txt_MRM_Pregunta.setText(bod.getVarFormateadaPorNombre("MRM", map.get("MRM").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMRM " + ex.getMessage());
        }
        lbl_MRM_error.setText(map.get("MRM").erro);
        return false;
    }

    private boolean CalcularMEC() {

        try {
            lbl_MEC_error.setText("");
            double x = bod.calcularMEC();

            if (bod.setVarDob(x, "MEC", map.get("MEC").rmin, map.get("MEC").rmax)) {
                txt_MEC_Pregunta.setText(bod.getVarFormateadaPorNombre("MEC", map.get("MEC").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMEC " + ex.getMessage());
        }
        lbl_MEC_error.setText(map.get("MEC").erro);
        return false;
    }

    private boolean CalcularMFS() {

        try {
            lbl_MFS_error.setText("");

            if (bod.setVarDob(txt_MFS_Pregunta.getText(), "MFS", map.get("MFS").rmin, map.get("MFS").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularMFS " + ex.getMessage());
        }
        lbl_MFS_error.setText(map.get("MFS").erro);
        return false;
    }

    private boolean CalcularMSA() {

        try {
            lbl_MSA_error.setText("");
            double x = bod.calcularMSA();

            if (bod.setVarDob(x, "MSA", map.get("MSA").rmin, map.get("MSA").rmax)) {
                txt_MSA_Pregunta.setText(bod.getVarFormateadaPorNombre("MSA", map.get("MSA").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMSA " + ex.getMessage());
        }
        lbl_MSA_error.setText(map.get("MSA").erro);
        return false;
    }

    private boolean CalcularMMS() {

        try {
            lbl_MMS_error.setText("");
            double x = bod.calcularMMS();

            if (bod.setVarDob(x, "MMS", map.get("MMS").rmin, map.get("MMS").rmax)) {
                txt_MMS_Pregunta.setText(bod.getVarFormateadaPorNombre("MMS", map.get("MMS").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMMS " + ex.getMessage());
        }
        lbl_MMS_error.setText(map.get("MMS").erro);
        return false;
    }

    private boolean CalcularMHS() {

        try {
            lbl_MHS_error.setText("");

            if (bod.setVarDob(txt_MHS_Pregunta.getText(), "MHS", map.get("MHS").rmin, map.get("MHS").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularMHS " + ex.getMessage());
        }
        lbl_MHS_error.setText(map.get("MHS").erro);
        return false;
    }

    private boolean CalcularMSD() {

        try {
            lbl_MSD_error.setText("");
            double x = bod.calcularMSD();

            if (bod.setVarDob(x, "MSD", map.get("MSD").rmin, map.get("MSD").rmax)) {
                txt_MSD_Pregunta.setText(bod.getVarFormateadaPorNombre("MSD", map.get("MSD").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMSD " + ex.getMessage());
        }
        lbl_MSD_error.setText(map.get("MSD").erro);
        return false;
    }

    private boolean CalcularMAT() {

        try {
            lbl_MAT_error.setText("");
            double x = bod.calcularMAT();

            if (bod.setVarDob(x, "MAT", map.get("MAT").rmin, map.get("MAT").rmax)) {
                txt_MAT_Pregunta.setText(bod.getVarFormateadaPorNombre("MAT", map.get("MAT").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularMAT " + ex.getMessage());
        }
        lbl_MAT_error.setText(map.get("MAT").erro);
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
            case "MCH":
                CalcularMCH();
                ejecutarFunciones("MAR");
                break;

            case "MAR":
                CalcularMAR();
                ejecutarFunciones("MTR");
                CalcularMGC();
                ejecutarFunciones("MGL");
                ejecutarFunciones("MAT");
                break;

            case "MPS":
                CalcularMPS();
                ejecutarFunciones("MTR");

                break;
            case "MTR":
                CalcularMTR();
                break;

            case "MGA":
                if (CalcularMGA()) {
                    ejecutarFunciones("MGL");
                }
                break;

            case "MGL":
                CalcularMGL();
                break;

            case "MDB":
                CalcularMDB();
                ejecutarFunciones("MDS");
                ejecutarFunciones("MED");
                break;

            case "MSE":
                CalcularMSE();
                ejecutarFunciones("MDP");
                break;

            case "MRD":
                CalcularMRD();
                ejecutarFunciones("MDP");
                break;

            case "MST":
                CalcularMST();
                ejecutarFunciones("MVT");
                ejecutarFunciones("MRM");
                break;

            case "MSR":
                CalcularMSR();
                ejecutarFunciones("MRM");
                break;

            case "MCC":
                CalcularMCC();
                ejecutarFunciones("MCE");
                ejecutarFunciones("MVT");
                break;

            case "MTE":
                CalcularMTE();
                ejecutarFunciones("MCE");
                ejecutarFunciones("MVT");
                break;

            case "MEL":
                CalcularMEL();
                ejecutarFunciones("MCE");
                ejecutarFunciones("MVT");
                break;

            case "MCE":
                CalcularMCE();
                ejecutarFunciones("MPL");
                break;

            case "MDP":
                CalcularMDP();
                ejecutarFunciones("MDS");
                break;

            case "MDS":
                CalcularMDS();
                ejecutarFunciones("MES");
                ejecutarFunciones("MPL");
                ejecutarFunciones("MVT");
                break;

            case "MES":
                CalcularMES();
                break;

            case "MED":
                CalcularMED();
                break;

            case "MPL":
                CalcularMPL();
                ejecutarFunciones("MLT");
                break;

            case "MLT":
                CalcularMLT();
                ejecutarFunciones("MVL");
                break;

            case "MVL":
                CalcularMVL();
                break;

            case "MVT":
                CalcularMVT();
                ejecutarFunciones("MVH");
                ejecutarFunciones("MAS");
                ejecutarFunciones("MAM");
                ejecutarFunciones("MEC");
                break;

            case "MVH":
                CalcularMVH();
                break;

            case "MPT":
                CalcularMPT();
                ejecutarFunciones("MAS");
                break;

            case "MAS":
                CalcularMAS();
                ejecutarFunciones("MAT");
                CalcularMWT();
                CalcularMHT();
                break;

            case "MAM":
                CalcularMAM();
                break;

            case "MRM":
                CalcularMRM();
                ejecutarFunciones("MMS");
                break;

            case "MEC":
                CalcularMEC();
                break;

            case "MFS":
                CalcularMFS();
                ejecutarFunciones("MSA");
                break;

            case "MSA":
                CalcularMSA();
                ejecutarFunciones("MAT");
                CalcularMSD();
                break;

            case "MMS":
                CalcularMMS();
                break;

            case "MHS":
                CalcularMHS();
                break;

            case "MAT":
                CalcularMAT();
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
        bod.WinLodosActivadsConvenc = false;
        Main main = (Main) this.getTopLevelAncestor();
        main.bod.WinLodosActivadsConvenc = false;
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
        lbl_MCH_desc = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
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
        lbl_MCH_titulo2 = new javax.swing.JLabel();
        txt_MCH_Pregunta = new javax.swing.JTextField();
        lbl_MCH_titulo1 = new javax.swing.JLabel();
        btn_MCH_ayuda = new javax.swing.JButton();
        lbl_MAR_error = new javax.swing.JLabel();
        lbl_MAR_titulo2 = new javax.swing.JLabel();
        txt_MAR_Pregunta = new javax.swing.JTextField();
        lbl_MAR_titulo1 = new javax.swing.JLabel();
        lbl_MPS_error = new javax.swing.JLabel();
        lbl_MPS_titulo2 = new javax.swing.JLabel();
        txt_MPS_Pregunta = new javax.swing.JTextField();
        lbl_MPS_titulo1 = new javax.swing.JLabel();
        btn_MPS_ayuda = new javax.swing.JButton();
        lbl_MTR_error = new javax.swing.JLabel();
        lbl_MTR_titulo2 = new javax.swing.JLabel();
        txt_MTR_Pregunta = new javax.swing.JTextField();
        lbl_MTR_titulo1 = new javax.swing.JLabel();
        btn_MTR_ayuda = new javax.swing.JButton();
        lbl_MGC_error = new javax.swing.JLabel();
        lbl_MGC_titulo2 = new javax.swing.JLabel();
        txt_MGC_Pregunta = new javax.swing.JTextField();
        lbl_MGC_titulo1 = new javax.swing.JLabel();
        btn_MGC_ayuda = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JSeparator();
        lbl_MGA_error = new javax.swing.JLabel();
        lbl_MGA_titulo2 = new javax.swing.JLabel();
        txt_MGA_Pregunta = new javax.swing.JTextField();
        lbl_MGA_titulo1 = new javax.swing.JLabel();
        lbl_MGL_error = new javax.swing.JLabel();
        lbl_MGL_titulo2 = new javax.swing.JLabel();
        txt_MGL_Pregunta = new javax.swing.JTextField();
        lbl_MGL_titulo1 = new javax.swing.JLabel();
        lbl_MDB_error = new javax.swing.JLabel();
        lbl_MDB_titulo2 = new javax.swing.JLabel();
        txt_MDB_Pregunta = new javax.swing.JTextField();
        lbl_MDB_titulo1 = new javax.swing.JLabel();
        lbl_MSE_error = new javax.swing.JLabel();
        lbl_MSE_titulo2 = new javax.swing.JLabel();
        txt_MSE_Pregunta = new javax.swing.JTextField();
        lbl_MSE_titulo1 = new javax.swing.JLabel();
        btn_MSE_ayuda = new javax.swing.JButton();
        lbl_MRD_error = new javax.swing.JLabel();
        lbl_MRD_titulo2 = new javax.swing.JLabel();
        txt_MRD_Pregunta = new javax.swing.JTextField();
        lbl_MRD_titulo1 = new javax.swing.JLabel();
        btn_MDB_ayuda = new javax.swing.JButton();
        lbl_MST_error = new javax.swing.JLabel();
        lbl_MST_titulo2 = new javax.swing.JLabel();
        txt_MST_Pregunta = new javax.swing.JTextField();
        lbl_MST_titulo1 = new javax.swing.JLabel();
        lbl_MSR_error = new javax.swing.JLabel();
        lbl_MSR_titulo2 = new javax.swing.JLabel();
        txt_MSR_Pregunta = new javax.swing.JTextField();
        lbl_MSR_titulo1 = new javax.swing.JLabel();
        btn_MST_ayuda = new javax.swing.JButton();
        lbl_MCC_error = new javax.swing.JLabel();
        lbl_MCC_titulo2 = new javax.swing.JLabel();
        txt_MCC_Pregunta = new javax.swing.JTextField();
        lbl_MCC_titulo1 = new javax.swing.JLabel();
        btn_MCC_ayuda = new javax.swing.JButton();
        lbl_MTE_error = new javax.swing.JLabel();
        lbl_MTE_titulo2 = new javax.swing.JLabel();
        txt_MTE_Pregunta = new javax.swing.JTextField();
        lbl_MTE_titulo1 = new javax.swing.JLabel();
        btn_MTE_ayuda = new javax.swing.JButton();
        jSeparator12 = new javax.swing.JSeparator();
        lbl_MEL_error = new javax.swing.JLabel();
        lbl_MEL_titulo2 = new javax.swing.JLabel();
        txt_MEL_Pregunta = new javax.swing.JTextField();
        lbl_MEL_titulo1 = new javax.swing.JLabel();
        btn_MEL_ayuda = new javax.swing.JButton();
        lbl_MCE_error = new javax.swing.JLabel();
        lbl_MCE_titulo2 = new javax.swing.JLabel();
        txt_MCE_Pregunta = new javax.swing.JTextField();
        lbl_MCE_titulo1 = new javax.swing.JLabel();
        btn_MCE_ayuda = new javax.swing.JButton();
        lbl_MDP_desc = new javax.swing.JLabel();
        lbl_MDP_error = new javax.swing.JLabel();
        lbl_MDP_titulo2 = new javax.swing.JLabel();
        txt_MDP_Pregunta = new javax.swing.JTextField();
        lbl_MDP_titulo1 = new javax.swing.JLabel();
        btn_MDP_ayuda = new javax.swing.JButton();
        lbl_MDS_error = new javax.swing.JLabel();
        lbl_MDS_titulo2 = new javax.swing.JLabel();
        txt_MDS_Pregunta = new javax.swing.JTextField();
        lbl_MDS_titulo1 = new javax.swing.JLabel();
        jSeparator14 = new javax.swing.JSeparator();
        lbl_MES_error = new javax.swing.JLabel();
        lbl_MES_titulo2 = new javax.swing.JLabel();
        txt_MES_Pregunta = new javax.swing.JTextField();
        lbl_MES_titulo1 = new javax.swing.JLabel();
        lbl_MED_error = new javax.swing.JLabel();
        lbl_MED_titulo2 = new javax.swing.JLabel();
        txt_MED_Pregunta = new javax.swing.JTextField();
        lbl_MED_titulo1 = new javax.swing.JLabel();
        jSeparator15 = new javax.swing.JSeparator();
        lbl_MPL_error = new javax.swing.JLabel();
        lbl_MPL_titulo2 = new javax.swing.JLabel();
        txt_MPL_Pregunta = new javax.swing.JTextField();
        lbl_MPL_titulo1 = new javax.swing.JLabel();
        btn_MPL_ayuda = new javax.swing.JButton();
        lbl_MLT_error = new javax.swing.JLabel();
        lbl_MLT_titulo2 = new javax.swing.JLabel();
        txt_MLT_Pregunta = new javax.swing.JTextField();
        lbl_MLT_titulo1 = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        lbl_MVL_error = new javax.swing.JLabel();
        lbl_MVL_titulo2 = new javax.swing.JLabel();
        txt_MVL_Pregunta = new javax.swing.JTextField();
        lbl_MVL_titulo1 = new javax.swing.JLabel();
        lbl_MVT_error = new javax.swing.JLabel();
        lbl_MVT_titulo2 = new javax.swing.JLabel();
        txt_MVT_Pregunta = new javax.swing.JTextField();
        lbl_MVT_titulo1 = new javax.swing.JLabel();
        btn_MVT_ayuda = new javax.swing.JButton();
        lbl_MVH_error = new javax.swing.JLabel();
        lbl_MVH_titulo2 = new javax.swing.JLabel();
        txt_MVH_Pregunta = new javax.swing.JTextField();
        lbl_MVH_titulo1 = new javax.swing.JLabel();
        btn_MVH_ayuda = new javax.swing.JButton();
        lbl_MPT_error = new javax.swing.JLabel();
        lbl_MPT_titulo2 = new javax.swing.JLabel();
        txt_MPT_Pregunta = new javax.swing.JTextField();
        lbl_MPT_titulo1 = new javax.swing.JLabel();
        lbl_MAS_error = new javax.swing.JLabel();
        lbl_MAS_titulo2 = new javax.swing.JLabel();
        txt_MAS_Pregunta = new javax.swing.JTextField();
        lbl_MAS_titulo1 = new javax.swing.JLabel();
        btn_MPT_ayuda = new javax.swing.JButton();
        jSeparator16 = new javax.swing.JSeparator();
        lbl_MPT_desc = new javax.swing.JLabel();
        jSeparator17 = new javax.swing.JSeparator();
        lbl_MRM_error = new javax.swing.JLabel();
        lbl_MRM_titulo2 = new javax.swing.JLabel();
        txt_MRM_Pregunta = new javax.swing.JTextField();
        lbl_MRM_titulo1 = new javax.swing.JLabel();
        lbl_MEC_error = new javax.swing.JLabel();
        lbl_MEC_titulo2 = new javax.swing.JLabel();
        txt_MEC_Pregunta = new javax.swing.JTextField();
        lbl_MEC_titulo1 = new javax.swing.JLabel();
        btn_MEC_ayuda = new javax.swing.JButton();
        lbl_MFS_error = new javax.swing.JLabel();
        lbl_MFS_titulo2 = new javax.swing.JLabel();
        txt_MFS_Pregunta = new javax.swing.JTextField();
        lbl_MFS_titulo1 = new javax.swing.JLabel();
        btn_MFS_ayuda = new javax.swing.JButton();
        lbl_MSA_error = new javax.swing.JLabel();
        lbl_MSA_titulo2 = new javax.swing.JLabel();
        txt_MSA_Pregunta = new javax.swing.JTextField();
        lbl_MSA_titulo1 = new javax.swing.JLabel();
        btn_MSA_ayuda = new javax.swing.JButton();
        lbl_MAM_error = new javax.swing.JLabel();
        lbl_MAM_titulo2 = new javax.swing.JLabel();
        txt_MAM_Pregunta = new javax.swing.JTextField();
        lbl_MAM_titulo1 = new javax.swing.JLabel();
        btn_MRM_ayuda = new javax.swing.JButton();
        jSeparator18 = new javax.swing.JSeparator();
        lbl_MMS_error = new javax.swing.JLabel();
        lbl_MMS_titulo2 = new javax.swing.JLabel();
        txt_MMS_Pregunta = new javax.swing.JTextField();
        lbl_MMS_titulo1 = new javax.swing.JLabel();
        btn_MMS_ayuda = new javax.swing.JButton();
        lbl_MHS_error = new javax.swing.JLabel();
        lbl_MHS_titulo2 = new javax.swing.JLabel();
        txt_MHS_Pregunta = new javax.swing.JTextField();
        lbl_MHS_titulo1 = new javax.swing.JLabel();
        btn_MHS_ayuda = new javax.swing.JButton();
        lbl_MMS_desc = new javax.swing.JLabel();
        lbl_MAT_error = new javax.swing.JLabel();
        lbl_MAT_titulo2 = new javax.swing.JLabel();
        txt_MAT_Pregunta = new javax.swing.JTextField();
        lbl_MAT_titulo1 = new javax.swing.JLabel();
        btn_MVL_ayuda = new javax.swing.JButton();
        lbl_MDB_desc = new javax.swing.JLabel();
        lbl_MGC_desc = new javax.swing.JLabel();
        btn_MSR_ayuda = new javax.swing.JButton();
        jSeparator19 = new javax.swing.JSeparator();
        lbl_MVT_desc = new javax.swing.JLabel();
        btn_MPT_ayuda1 = new javax.swing.JButton();
        btn_MAM_ayuda = new javax.swing.JButton();
        lbl_MFS_desc = new javax.swing.JLabel();
        lbl_MCH_error = new javax.swing.JLabel();
        lbl_MSD_error = new javax.swing.JLabel();
        lbl_MSD_titulo2 = new javax.swing.JLabel();
        txt_MSD_Pregunta = new javax.swing.JTextField();
        lbl_MSD_titulo1 = new javax.swing.JLabel();
        lbl_MWT_error = new javax.swing.JLabel();
        lbl_MWT_titulo2 = new javax.swing.JLabel();
        txt_MWT_Pregunta = new javax.swing.JTextField();
        lbl_MWT_titulo1 = new javax.swing.JLabel();
        lbl_MHT_error = new javax.swing.JLabel();
        lbl_MHT_titulo2 = new javax.swing.JLabel();
        txt_MHT_Pregunta = new javax.swing.JTextField();
        lbl_MHT_titulo1 = new javax.swing.JLabel();
        lbl_MGC_titulo3 = new javax.swing.JLabel();
        lbl_MGC_titulo4 = new javax.swing.JLabel();
        btn_close2 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1024, 2370));

        jsp_LodosActivadosModConv.setPreferredSize(new java.awt.Dimension(1024, 768));

        jp_Componentes.setPreferredSize(new java.awt.Dimension(1024, 2370));
        jp_Componentes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jp_Componentes.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 38, 1024, -1));

        lbl_titulo1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_titulo1.setForeground(new java.awt.Color(51, 51, 51));
        lbl_titulo1.setText("Titulo");
        jp_Componentes.add(lbl_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 300, 30));

        lbl_titulo2.setForeground(new java.awt.Color(0, 51, 102));
        lbl_titulo2.setText("Desc");
        jp_Componentes.add(lbl_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 0, 600, 30));
        jp_Componentes.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 168, 1024, 2));

        btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Button_Salva_Guardar_21.png"))); // NOI18N
        btn_guardar.setText("save");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2290, 120, -1));

        lbl_MCH_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_MCH_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_MCH_desc.setText("Desc");
        lbl_MCH_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MCH_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 610, 30));
        jp_Componentes.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 391, 1024, 2));

        lbl_Q2C_titulo2.setText("Titulo");
        lbl_Q2C_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2C_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 50, 80, 25));
        jp_Componentes.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 467, 1024, 0));
        jp_Componentes.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 2280, 1024, -1));

        btn_close.setText("Salir");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2330, 120, 30));

        lbl_save_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error.setText(" . . .");
        jp_Componentes.add(lbl_save_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 2290, 400, 35));

        lbl_save_error2.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error2.setText(" . . .");
        jp_Componentes.add(lbl_save_error2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 2330, 400, 35));

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

        lbl_MCH_titulo2.setText("Titulo");
        lbl_MCH_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MCH_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 230, 80, 25));

        txt_MCH_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_MCH_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 230, 130, 25));

        lbl_MCH_titulo1.setText("Titulo");
        lbl_MCH_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MCH_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 300, 30));

        btn_MCH_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MCH_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MCH_ayuda.setContentAreaFilled(false);
        btn_MCH_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MCH_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 230, 25, 25));

        lbl_MAR_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MAR_error.setText(" . . .");
        jp_Componentes.add(lbl_MAR_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 270, 340, 35));

        lbl_MAR_titulo2.setText("Titulo");
        lbl_MAR_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MAR_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 270, 80, 25));

        txt_MAR_Pregunta.setEditable(false);
        jp_Componentes.add(txt_MAR_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 270, 130, 25));

        lbl_MAR_titulo1.setText("Titulo");
        lbl_MAR_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MAR_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 300, 30));

        lbl_MPS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MPS_error.setText(" . . .");
        jp_Componentes.add(lbl_MPS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 310, 340, 35));

        lbl_MPS_titulo2.setText("Titulo");
        lbl_MPS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MPS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 310, 80, 25));

        txt_MPS_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_MPS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 310, 130, 25));

        lbl_MPS_titulo1.setText("Titulo");
        lbl_MPS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MPS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 300, 30));

        btn_MPS_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MPS_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MPS_ayuda.setContentAreaFilled(false);
        btn_MPS_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MPS_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 310, 25, 25));

        lbl_MTR_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MTR_error.setText(" . . .");
        jp_Componentes.add(lbl_MTR_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 350, 340, 35));

        lbl_MTR_titulo2.setText("Titulo");
        lbl_MTR_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MTR_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 350, 80, 25));

        txt_MTR_Pregunta.setEditable(false);
        jp_Componentes.add(txt_MTR_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 350, 130, 25));

        lbl_MTR_titulo1.setText("Titulo");
        lbl_MTR_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MTR_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 300, 30));

        btn_MTR_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MTR_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MTR_ayuda.setContentAreaFilled(false);
        btn_MTR_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MTR_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 350, 25, 25));

        lbl_MGC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MGC_error.setText(" . . .");
        jp_Componentes.add(lbl_MGC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 470, 340, 35));

        lbl_MGC_titulo2.setText("Titulo");
        lbl_MGC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MGC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 470, 80, 25));

        txt_MGC_Pregunta.setEditable(false);
        jp_Componentes.add(txt_MGC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 470, 130, 25));

        lbl_MGC_titulo1.setText("Titulo");
        lbl_MGC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MGC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 470, 300, 30));

        btn_MGC_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MGC_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MGC_ayuda.setContentAreaFilled(false);
        btn_MGC_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MGC_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 470, 25, 25));
        jp_Componentes.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 630, 1024, 2));

        lbl_MGA_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MGA_error.setText(" . . .");
        jp_Componentes.add(lbl_MGA_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 540, 340, 35));

        lbl_MGA_titulo2.setText("Titulo");
        lbl_MGA_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MGA_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 540, 80, 25));

        txt_MGA_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_MGA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 540, 130, 25));

        lbl_MGA_titulo1.setText("Titulo");
        lbl_MGA_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MGA_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 540, 300, 30));

        lbl_MGL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MGL_error.setText(" . . .");
        jp_Componentes.add(lbl_MGL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 580, 340, 35));

        lbl_MGL_titulo2.setText("Titulo");
        lbl_MGL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MGL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 580, 80, 25));

        txt_MGL_Pregunta.setEditable(false);
        jp_Componentes.add(txt_MGL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 580, 130, 25));

        lbl_MGL_titulo1.setText("Titulo");
        lbl_MGL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MGL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 580, 300, 30));

        lbl_MDB_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MDB_error.setText(" . . .");
        jp_Componentes.add(lbl_MDB_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 680, 340, 35));

        lbl_MDB_titulo2.setText("Titulo");
        lbl_MDB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MDB_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 680, 80, 25));

        txt_MDB_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_MDB_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 680, 130, 25));

        lbl_MDB_titulo1.setText("Titulo");
        lbl_MDB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MDB_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 680, 300, 30));

        lbl_MSE_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MSE_error.setText(" . . .");
        jp_Componentes.add(lbl_MSE_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 720, 340, 35));

        lbl_MSE_titulo2.setText("Titulo");
        lbl_MSE_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MSE_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 720, 80, 25));

        txt_MSE_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_MSE_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 720, 130, 25));

        lbl_MSE_titulo1.setText("Titulo");
        lbl_MSE_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MSE_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 720, 300, 30));

        btn_MSE_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MSE_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MSE_ayuda.setContentAreaFilled(false);
        btn_MSE_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MSE_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 720, 25, 25));

        lbl_MRD_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MRD_error.setText(" . . .");
        jp_Componentes.add(lbl_MRD_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 760, 340, 35));

        lbl_MRD_titulo2.setText("Titulo");
        lbl_MRD_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MRD_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 760, 80, 25));

        txt_MRD_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_MRD_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 760, 130, 25));

        lbl_MRD_titulo1.setText("Titulo");
        lbl_MRD_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MRD_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 760, 300, 30));

        btn_MDB_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MDB_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MDB_ayuda.setContentAreaFilled(false);
        btn_MDB_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MDB_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 680, 25, 25));

        lbl_MST_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MST_error.setText(" . . .");
        jp_Componentes.add(lbl_MST_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 800, 340, 35));

        lbl_MST_titulo2.setText("Titulo");
        lbl_MST_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MST_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 800, 80, 25));

        txt_MST_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_MST_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 800, 130, 25));

        lbl_MST_titulo1.setText("Titulo");
        lbl_MST_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MST_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 800, 300, 30));

        lbl_MSR_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MSR_error.setText(" . . .");
        jp_Componentes.add(lbl_MSR_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 840, 340, 35));

        lbl_MSR_titulo2.setText("Titulo");
        lbl_MSR_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MSR_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 840, 80, 25));

        txt_MSR_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_MSR_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 840, 130, 25));

        lbl_MSR_titulo1.setText("Titulo");
        lbl_MSR_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MSR_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 840, 300, 30));

        btn_MST_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MST_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MST_ayuda.setContentAreaFilled(false);
        btn_MST_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MST_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 800, 25, 25));

        lbl_MCC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MCC_error.setText(" . . .");
        jp_Componentes.add(lbl_MCC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 880, 340, 35));

        lbl_MCC_titulo2.setText("Titulo");
        lbl_MCC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MCC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 880, 80, 25));

        txt_MCC_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_MCC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 880, 130, 25));

        lbl_MCC_titulo1.setText("Titulo");
        lbl_MCC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MCC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 880, 300, 30));

        btn_MCC_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MCC_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MCC_ayuda.setContentAreaFilled(false);
        btn_MCC_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MCC_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 880, 25, 25));

        lbl_MTE_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MTE_error.setText(" . . .");
        jp_Componentes.add(lbl_MTE_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 920, 340, 35));

        lbl_MTE_titulo2.setText("Titulo");
        lbl_MTE_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MTE_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 920, 80, 25));

        txt_MTE_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_MTE_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 920, 130, 25));

        lbl_MTE_titulo1.setText("Titulo");
        lbl_MTE_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MTE_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 920, 300, 30));

        btn_MTE_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MTE_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MTE_ayuda.setContentAreaFilled(false);
        btn_MTE_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MTE_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 920, 25, 25));
        jp_Componentes.add(jSeparator12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1040, 1024, 2));

        lbl_MEL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MEL_error.setText(" . . .");
        jp_Componentes.add(lbl_MEL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 960, 340, 35));

        lbl_MEL_titulo2.setText("Titulo");
        lbl_MEL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MEL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 960, 80, 25));

        txt_MEL_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_MEL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 960, 130, 25));

        lbl_MEL_titulo1.setText("Titulo");
        lbl_MEL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MEL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 960, 300, 30));

        btn_MEL_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MEL_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MEL_ayuda.setContentAreaFilled(false);
        btn_MEL_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MEL_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 960, 25, 25));

        lbl_MCE_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MCE_error.setText(" . . .");
        jp_Componentes.add(lbl_MCE_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1000, 340, 35));

        lbl_MCE_titulo2.setText("Titulo");
        lbl_MCE_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MCE_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1000, 80, 25));

        txt_MCE_Pregunta.setEditable(false);
        jp_Componentes.add(txt_MCE_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1000, 130, 25));

        lbl_MCE_titulo1.setText("Titulo");
        lbl_MCE_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MCE_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1000, 300, 30));

        btn_MCE_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MCE_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MCE_ayuda.setContentAreaFilled(false);
        btn_MCE_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MCE_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1000, 25, 25));

        lbl_MDP_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_MDP_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_MDP_desc.setText("Desc");
        lbl_MDP_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MDP_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1050, 610, 30));

        lbl_MDP_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MDP_error.setText(" . . .");
        jp_Componentes.add(lbl_MDP_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1090, 340, 35));

        lbl_MDP_titulo2.setText("Titulo");
        lbl_MDP_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MDP_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1090, 80, 25));

        txt_MDP_Pregunta.setEditable(false);
        jp_Componentes.add(txt_MDP_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1090, 130, 25));

        lbl_MDP_titulo1.setText("Titulo");
        lbl_MDP_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MDP_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1090, 300, 30));

        btn_MDP_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MDP_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MDP_ayuda.setContentAreaFilled(false);
        btn_MDP_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MDP_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1090, 25, 25));

        lbl_MDS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MDS_error.setText(" . . .");
        jp_Componentes.add(lbl_MDS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1130, 340, 35));

        lbl_MDS_titulo2.setText("Titulo");
        lbl_MDS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MDS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1130, 80, 25));

        txt_MDS_Pregunta.setEditable(false);
        jp_Componentes.add(txt_MDS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1130, 130, 25));

        lbl_MDS_titulo1.setText("Titulo");
        lbl_MDS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MDS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1130, 300, 30));
        jp_Componentes.add(jSeparator14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1250, 1024, 2));

        lbl_MES_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MES_error.setText(" . . .");
        jp_Componentes.add(lbl_MES_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1170, 340, 35));

        lbl_MES_titulo2.setText("Titulo");
        lbl_MES_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MES_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1170, 80, 25));

        txt_MES_Pregunta.setEditable(false);
        jp_Componentes.add(txt_MES_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1170, 130, 25));

        lbl_MES_titulo1.setText("Titulo");
        lbl_MES_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MES_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1170, 300, 30));

        lbl_MED_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MED_error.setText(" . . .");
        jp_Componentes.add(lbl_MED_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1210, 340, 35));

        lbl_MED_titulo2.setText("Titulo");
        lbl_MED_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MED_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1210, 80, 25));

        txt_MED_Pregunta.setEditable(false);
        jp_Componentes.add(txt_MED_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1210, 130, 25));

        lbl_MED_titulo1.setText("Titulo");
        lbl_MED_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MED_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1210, 300, 30));
        jp_Componentes.add(jSeparator15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1360, 1024, -1));

        lbl_MPL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MPL_error.setText(" . . .");
        jp_Componentes.add(lbl_MPL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1270, 340, 35));

        lbl_MPL_titulo2.setText("Titulo");
        lbl_MPL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MPL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1270, 80, 25));

        txt_MPL_Pregunta.setEditable(false);
        jp_Componentes.add(txt_MPL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1270, 130, 25));

        lbl_MPL_titulo1.setText("Titulo");
        lbl_MPL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MPL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1270, 300, 30));

        btn_MPL_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MPL_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MPL_ayuda.setContentAreaFilled(false);
        btn_MPL_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MPL_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1270, 25, 25));

        lbl_MLT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MLT_error.setText(" . . .");
        jp_Componentes.add(lbl_MLT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1310, 340, 35));

        lbl_MLT_titulo2.setText("Titulo");
        lbl_MLT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MLT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1310, 80, 25));

        txt_MLT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_MLT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1310, 130, 25));

        lbl_MLT_titulo1.setText("Titulo");
        lbl_MLT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MLT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1310, 300, 30));
        jp_Componentes.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1570, 1024, -1));

        lbl_MVL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MVL_error.setText(" . . .");
        jp_Componentes.add(lbl_MVL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1380, 340, 35));

        lbl_MVL_titulo2.setText("Titulo");
        lbl_MVL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MVL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1380, 80, 25));

        txt_MVL_Pregunta.setEditable(false);
        jp_Componentes.add(txt_MVL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1380, 130, 25));

        lbl_MVL_titulo1.setText("Titulo");
        lbl_MVL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MVL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1380, 300, 30));

        lbl_MVT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MVT_error.setText(" . . .");
        jp_Componentes.add(lbl_MVT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1480, 340, 35));

        lbl_MVT_titulo2.setText("Titulo");
        lbl_MVT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MVT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1480, 80, 25));

        txt_MVT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_MVT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1480, 130, 25));

        lbl_MVT_titulo1.setText("Titulo");
        lbl_MVT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MVT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1480, 300, 30));

        btn_MVT_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MVT_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MVT_ayuda.setContentAreaFilled(false);
        btn_MVT_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MVT_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1480, 25, 25));

        lbl_MVH_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MVH_error.setText(" . . .");
        jp_Componentes.add(lbl_MVH_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1520, 340, 35));

        lbl_MVH_titulo2.setText("Titulo");
        lbl_MVH_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MVH_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1520, 80, 25));

        txt_MVH_Pregunta.setEditable(false);
        jp_Componentes.add(txt_MVH_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1520, 130, 25));

        lbl_MVH_titulo1.setText("Titulo");
        lbl_MVH_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MVH_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1520, 300, 30));

        btn_MVH_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MVH_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MVH_ayuda.setContentAreaFilled(false);
        btn_MVH_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MVH_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1520, 25, 25));

        lbl_MPT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MPT_error.setText(" . . .");
        jp_Componentes.add(lbl_MPT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1620, 340, 35));

        lbl_MPT_titulo2.setText("Titulo");
        lbl_MPT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MPT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1620, 80, 25));

        txt_MPT_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_MPT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1620, 130, 25));

        lbl_MPT_titulo1.setText("Titulo");
        lbl_MPT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MPT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1620, 300, 30));

        lbl_MAS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MAS_error.setText(" . . .");
        jp_Componentes.add(lbl_MAS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1660, 340, 35));

        lbl_MAS_titulo2.setText("Titulo");
        lbl_MAS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MAS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1660, 80, 25));

        txt_MAS_Pregunta.setEditable(false);
        jp_Componentes.add(txt_MAS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1660, 130, 25));

        lbl_MAS_titulo1.setText("Titulo");
        lbl_MAS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MAS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1660, 300, 30));

        btn_MPT_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MPT_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MPT_ayuda.setContentAreaFilled(false);
        btn_MPT_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MPT_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 1580, 25, 25));
        jp_Componentes.add(jSeparator16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1780, 1024, 2));

        lbl_MPT_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_MPT_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_MPT_desc.setText("Desc");
        lbl_MPT_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MPT_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1580, 610, 30));
        jp_Componentes.add(jSeparator17, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1920, 1024, 2));

        lbl_MRM_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MRM_error.setText(" . . .");
        jp_Componentes.add(lbl_MRM_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1840, 340, 35));

        lbl_MRM_titulo2.setText("Titulo");
        lbl_MRM_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MRM_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1840, 80, 25));

        txt_MRM_Pregunta.setEditable(false);
        jp_Componentes.add(txt_MRM_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1840, 130, 25));

        lbl_MRM_titulo1.setText("Titulo");
        lbl_MRM_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MRM_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1840, 300, 30));

        lbl_MEC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MEC_error.setText(" . . .");
        jp_Componentes.add(lbl_MEC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1880, 340, 35));

        lbl_MEC_titulo2.setText("Titulo");
        lbl_MEC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MEC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1880, 80, 25));

        txt_MEC_Pregunta.setEditable(false);
        jp_Componentes.add(txt_MEC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1880, 130, 25));

        lbl_MEC_titulo1.setText("Titulo");
        lbl_MEC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MEC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1880, 300, 30));

        btn_MEC_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MEC_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MEC_ayuda.setContentAreaFilled(false);
        btn_MEC_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MEC_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1880, 25, 25));

        lbl_MFS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MFS_error.setText(" . . .");
        jp_Componentes.add(lbl_MFS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1970, 340, 35));

        lbl_MFS_titulo2.setText("Titulo");
        lbl_MFS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MFS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1970, 80, 25));

        txt_MFS_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_MFS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1970, 130, 25));

        lbl_MFS_titulo1.setText("Titulo");
        lbl_MFS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MFS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1970, 300, 30));

        btn_MFS_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MFS_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MFS_ayuda.setContentAreaFilled(false);
        btn_MFS_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MFS_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1970, 25, 25));

        lbl_MSA_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MSA_error.setText(" . . .");
        jp_Componentes.add(lbl_MSA_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 2010, 340, 35));

        lbl_MSA_titulo2.setText("Titulo");
        lbl_MSA_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MSA_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 2010, 80, 25));

        txt_MSA_Pregunta.setEditable(false);
        jp_Componentes.add(txt_MSA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 2010, 130, 25));

        lbl_MSA_titulo1.setText("Titulo");
        lbl_MSA_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MSA_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2010, 300, 30));

        btn_MSA_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MSA_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MSA_ayuda.setContentAreaFilled(false);
        btn_MSA_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MSA_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 2010, 25, 25));

        lbl_MAM_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MAM_error.setText(" . . .");
        jp_Componentes.add(lbl_MAM_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1800, 340, 35));

        lbl_MAM_titulo2.setText("Titulo");
        lbl_MAM_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MAM_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1800, 80, 25));

        txt_MAM_Pregunta.setEditable(false);
        jp_Componentes.add(txt_MAM_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1800, 130, 25));

        lbl_MAM_titulo1.setText("Titulo");
        lbl_MAM_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MAM_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1800, 300, 30));

        btn_MRM_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MRM_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MRM_ayuda.setContentAreaFilled(false);
        btn_MRM_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MRM_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1840, 25, 25));
        jp_Componentes.add(jSeparator18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 2050, 1024, 2));

        lbl_MMS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MMS_error.setText(" . . .");
        jp_Componentes.add(lbl_MMS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 2110, 340, 35));

        lbl_MMS_titulo2.setText("Titulo");
        lbl_MMS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MMS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 2110, 80, 25));

        txt_MMS_Pregunta.setEditable(false);
        jp_Componentes.add(txt_MMS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 2110, 130, 25));

        lbl_MMS_titulo1.setText("Titulo");
        lbl_MMS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MMS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2110, 300, 30));

        btn_MMS_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MMS_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MMS_ayuda.setContentAreaFilled(false);
        btn_MMS_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MMS_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 2110, 25, 25));

        lbl_MHS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MHS_error.setText(" . . .");
        jp_Componentes.add(lbl_MHS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 2150, 340, 35));

        lbl_MHS_titulo2.setText("Titulo");
        lbl_MHS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MHS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 2150, 80, 25));

        txt_MHS_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_MHS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 2150, 130, 25));

        lbl_MHS_titulo1.setText("Titulo");
        lbl_MHS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MHS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2150, 300, 30));

        btn_MHS_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MHS_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MHS_ayuda.setContentAreaFilled(false);
        btn_MHS_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MHS_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 2150, 25, 25));

        lbl_MMS_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_MMS_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_MMS_desc.setText("Desc");
        lbl_MMS_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MMS_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2070, 610, 30));

        lbl_MAT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MAT_error.setText(" . . .");
        jp_Componentes.add(lbl_MAT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 2230, 340, 35));

        lbl_MAT_titulo2.setText("Titulo");
        lbl_MAT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MAT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 2230, 80, 25));

        txt_MAT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_MAT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 2230, 130, 25));

        lbl_MAT_titulo1.setText("Titulo");
        lbl_MAT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MAT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2230, 300, 30));

        btn_MVL_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MVL_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MVL_ayuda.setContentAreaFilled(false);
        btn_MVL_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MVL_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1380, 25, 25));

        lbl_MDB_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_MDB_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_MDB_desc.setText("Desc");
        lbl_MDB_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MDB_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 640, 610, 30));

        lbl_MGC_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_MGC_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_MGC_desc.setText("Desc");
        lbl_MGC_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MGC_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 610, 30));

        btn_MSR_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MSR_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MSR_ayuda.setContentAreaFilled(false);
        btn_MSR_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MSR_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 840, 25, 25));
        jp_Componentes.add(jSeparator19, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1430, 1024, 2));

        lbl_MVT_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_MVT_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_MVT_desc.setText("Desc");
        lbl_MVT_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MVT_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1440, 610, 30));

        btn_MPT_ayuda1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MPT_ayuda1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MPT_ayuda1.setContentAreaFilled(false);
        btn_MPT_ayuda1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MPT_ayuda1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1620, 25, 25));

        btn_MAM_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MAM_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MAM_ayuda.setContentAreaFilled(false);
        btn_MAM_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MAM_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1800, 25, 25));

        lbl_MFS_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_MFS_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_MFS_desc.setText("Desc");
        lbl_MFS_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MFS_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1930, 610, 30));

        lbl_MCH_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MCH_error.setText(" . . .");
        jp_Componentes.add(lbl_MCH_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 230, 340, 35));

        lbl_MSD_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MSD_error.setText(" . . .");
        jp_Componentes.add(lbl_MSD_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 2190, 340, 35));

        lbl_MSD_titulo2.setText("Titulo");
        lbl_MSD_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MSD_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 2190, 80, 25));

        txt_MSD_Pregunta.setEditable(false);
        jp_Componentes.add(txt_MSD_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 2190, 130, 25));

        lbl_MSD_titulo1.setText("Titulo");
        lbl_MSD_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MSD_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2190, 300, 30));

        lbl_MWT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MWT_error.setText(" . . .");
        jp_Componentes.add(lbl_MWT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1700, 340, 35));

        lbl_MWT_titulo2.setText("Titulo");
        lbl_MWT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MWT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1700, 80, 25));

        txt_MWT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_MWT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1700, 130, 25));

        lbl_MWT_titulo1.setText("Titulo");
        lbl_MWT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MWT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1700, 300, 30));

        lbl_MHT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_MHT_error.setText(" . . .");
        jp_Componentes.add(lbl_MHT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1740, 340, 35));

        lbl_MHT_titulo2.setText("Titulo");
        lbl_MHT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MHT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1740, 80, 25));

        txt_MHT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_MHT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1740, 130, 25));

        lbl_MHT_titulo1.setText("Titulo");
        lbl_MHT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MHT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1740, 300, 30));

        lbl_MGC_titulo3.setText("Titulo");
        lbl_MGC_titulo3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MGC_titulo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, 300, 30));

        lbl_MGC_titulo4.setText("Titulo");
        lbl_MGC_titulo4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MGC_titulo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 510, 300, 30));

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jsp_LodosActivadosModConv, javax.swing.GroupLayout.DEFAULT_SIZE, 2399, Short.MAX_VALUE)
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
    private javax.swing.JButton btn_MAM_ayuda;
    private javax.swing.JButton btn_MCC_ayuda;
    private javax.swing.JButton btn_MCE_ayuda;
    private javax.swing.JButton btn_MCH_ayuda;
    private javax.swing.JButton btn_MDB_ayuda;
    private javax.swing.JButton btn_MDP_ayuda;
    private javax.swing.JButton btn_MEC_ayuda;
    private javax.swing.JButton btn_MEL_ayuda;
    private javax.swing.JButton btn_MFS_ayuda;
    private javax.swing.JButton btn_MGC_ayuda;
    private javax.swing.JButton btn_MHS_ayuda;
    private javax.swing.JButton btn_MMS_ayuda;
    private javax.swing.JButton btn_MPL_ayuda;
    private javax.swing.JButton btn_MPS_ayuda;
    private javax.swing.JButton btn_MPT_ayuda;
    private javax.swing.JButton btn_MPT_ayuda1;
    private javax.swing.JButton btn_MRM_ayuda;
    private javax.swing.JButton btn_MSA_ayuda;
    private javax.swing.JButton btn_MSE_ayuda;
    private javax.swing.JButton btn_MSR_ayuda;
    private javax.swing.JButton btn_MST_ayuda;
    private javax.swing.JButton btn_MTE_ayuda;
    private javax.swing.JButton btn_MTR_ayuda;
    private javax.swing.JButton btn_MVH_ayuda;
    private javax.swing.JButton btn_MVL_ayuda;
    private javax.swing.JButton btn_MVT_ayuda;
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
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JPanel jp_Componentes;
    private javax.swing.JScrollPane jsp_LodosActivadosModConv;
    private javax.swing.JLabel lbl_CABQ_titulo1;
    private javax.swing.JLabel lbl_CABQ_titulo2;
    private javax.swing.JLabel lbl_CVS_titulo1;
    private javax.swing.JLabel lbl_CVS_titulo2;
    private javax.swing.JLabel lbl_MAM_error;
    private javax.swing.JLabel lbl_MAM_titulo1;
    private javax.swing.JLabel lbl_MAM_titulo2;
    private javax.swing.JLabel lbl_MAR_error;
    private javax.swing.JLabel lbl_MAR_titulo1;
    private javax.swing.JLabel lbl_MAR_titulo2;
    private javax.swing.JLabel lbl_MAS_error;
    private javax.swing.JLabel lbl_MAS_titulo1;
    private javax.swing.JLabel lbl_MAS_titulo2;
    private javax.swing.JLabel lbl_MAT_error;
    private javax.swing.JLabel lbl_MAT_titulo1;
    private javax.swing.JLabel lbl_MAT_titulo2;
    private javax.swing.JLabel lbl_MCC_error;
    private javax.swing.JLabel lbl_MCC_titulo1;
    private javax.swing.JLabel lbl_MCC_titulo2;
    private javax.swing.JLabel lbl_MCE_error;
    private javax.swing.JLabel lbl_MCE_titulo1;
    private javax.swing.JLabel lbl_MCE_titulo2;
    private javax.swing.JLabel lbl_MCH_desc;
    private javax.swing.JLabel lbl_MCH_error;
    private javax.swing.JLabel lbl_MCH_titulo1;
    private javax.swing.JLabel lbl_MCH_titulo2;
    private javax.swing.JLabel lbl_MDB_desc;
    private javax.swing.JLabel lbl_MDB_error;
    private javax.swing.JLabel lbl_MDB_titulo1;
    private javax.swing.JLabel lbl_MDB_titulo2;
    private javax.swing.JLabel lbl_MDP_desc;
    private javax.swing.JLabel lbl_MDP_error;
    private javax.swing.JLabel lbl_MDP_titulo1;
    private javax.swing.JLabel lbl_MDP_titulo2;
    private javax.swing.JLabel lbl_MDS_error;
    private javax.swing.JLabel lbl_MDS_titulo1;
    private javax.swing.JLabel lbl_MDS_titulo2;
    private javax.swing.JLabel lbl_MEC_error;
    private javax.swing.JLabel lbl_MEC_titulo1;
    private javax.swing.JLabel lbl_MEC_titulo2;
    private javax.swing.JLabel lbl_MED_error;
    private javax.swing.JLabel lbl_MED_titulo1;
    private javax.swing.JLabel lbl_MED_titulo2;
    private javax.swing.JLabel lbl_MEL_error;
    private javax.swing.JLabel lbl_MEL_titulo1;
    private javax.swing.JLabel lbl_MEL_titulo2;
    private javax.swing.JLabel lbl_MES_error;
    private javax.swing.JLabel lbl_MES_titulo1;
    private javax.swing.JLabel lbl_MES_titulo2;
    private javax.swing.JLabel lbl_MFS_desc;
    private javax.swing.JLabel lbl_MFS_error;
    private javax.swing.JLabel lbl_MFS_titulo1;
    private javax.swing.JLabel lbl_MFS_titulo2;
    private javax.swing.JLabel lbl_MGA_error;
    private javax.swing.JLabel lbl_MGA_titulo1;
    private javax.swing.JLabel lbl_MGA_titulo2;
    private javax.swing.JLabel lbl_MGC_desc;
    private javax.swing.JLabel lbl_MGC_error;
    private javax.swing.JLabel lbl_MGC_titulo1;
    private javax.swing.JLabel lbl_MGC_titulo2;
    private javax.swing.JLabel lbl_MGC_titulo3;
    private javax.swing.JLabel lbl_MGC_titulo4;
    private javax.swing.JLabel lbl_MGL_error;
    private javax.swing.JLabel lbl_MGL_titulo1;
    private javax.swing.JLabel lbl_MGL_titulo2;
    private javax.swing.JLabel lbl_MHS_error;
    private javax.swing.JLabel lbl_MHS_titulo1;
    private javax.swing.JLabel lbl_MHS_titulo2;
    private javax.swing.JLabel lbl_MHT_error;
    private javax.swing.JLabel lbl_MHT_titulo1;
    private javax.swing.JLabel lbl_MHT_titulo2;
    private javax.swing.JLabel lbl_MLT_error;
    private javax.swing.JLabel lbl_MLT_titulo1;
    private javax.swing.JLabel lbl_MLT_titulo2;
    private javax.swing.JLabel lbl_MMS_desc;
    private javax.swing.JLabel lbl_MMS_error;
    private javax.swing.JLabel lbl_MMS_titulo1;
    private javax.swing.JLabel lbl_MMS_titulo2;
    private javax.swing.JLabel lbl_MPL_error;
    private javax.swing.JLabel lbl_MPL_titulo1;
    private javax.swing.JLabel lbl_MPL_titulo2;
    private javax.swing.JLabel lbl_MPS_error;
    private javax.swing.JLabel lbl_MPS_titulo1;
    private javax.swing.JLabel lbl_MPS_titulo2;
    private javax.swing.JLabel lbl_MPT_desc;
    private javax.swing.JLabel lbl_MPT_error;
    private javax.swing.JLabel lbl_MPT_titulo1;
    private javax.swing.JLabel lbl_MPT_titulo2;
    private javax.swing.JLabel lbl_MRD_error;
    private javax.swing.JLabel lbl_MRD_titulo1;
    private javax.swing.JLabel lbl_MRD_titulo2;
    private javax.swing.JLabel lbl_MRM_error;
    private javax.swing.JLabel lbl_MRM_titulo1;
    private javax.swing.JLabel lbl_MRM_titulo2;
    private javax.swing.JLabel lbl_MSA_error;
    private javax.swing.JLabel lbl_MSA_titulo1;
    private javax.swing.JLabel lbl_MSA_titulo2;
    private javax.swing.JLabel lbl_MSD_error;
    private javax.swing.JLabel lbl_MSD_titulo1;
    private javax.swing.JLabel lbl_MSD_titulo2;
    private javax.swing.JLabel lbl_MSE_error;
    private javax.swing.JLabel lbl_MSE_titulo1;
    private javax.swing.JLabel lbl_MSE_titulo2;
    private javax.swing.JLabel lbl_MSR_error;
    private javax.swing.JLabel lbl_MSR_titulo1;
    private javax.swing.JLabel lbl_MSR_titulo2;
    private javax.swing.JLabel lbl_MST_error;
    private javax.swing.JLabel lbl_MST_titulo1;
    private javax.swing.JLabel lbl_MST_titulo2;
    private javax.swing.JLabel lbl_MTE_error;
    private javax.swing.JLabel lbl_MTE_titulo1;
    private javax.swing.JLabel lbl_MTE_titulo2;
    private javax.swing.JLabel lbl_MTR_error;
    private javax.swing.JLabel lbl_MTR_titulo1;
    private javax.swing.JLabel lbl_MTR_titulo2;
    private javax.swing.JLabel lbl_MVH_error;
    private javax.swing.JLabel lbl_MVH_titulo1;
    private javax.swing.JLabel lbl_MVH_titulo2;
    private javax.swing.JLabel lbl_MVL_error;
    private javax.swing.JLabel lbl_MVL_titulo1;
    private javax.swing.JLabel lbl_MVL_titulo2;
    private javax.swing.JLabel lbl_MVT_desc;
    private javax.swing.JLabel lbl_MVT_error;
    private javax.swing.JLabel lbl_MVT_titulo1;
    private javax.swing.JLabel lbl_MVT_titulo2;
    private javax.swing.JLabel lbl_MWT_error;
    private javax.swing.JLabel lbl_MWT_titulo1;
    private javax.swing.JLabel lbl_MWT_titulo2;
    private javax.swing.JLabel lbl_Q2C_titulo1;
    private javax.swing.JLabel lbl_Q2C_titulo2;
    private javax.swing.JLabel lbl_save_error;
    private javax.swing.JLabel lbl_save_error2;
    private javax.swing.JLabel lbl_titulo1;
    private javax.swing.JLabel lbl_titulo2;
    private javax.swing.JTextField txt_CABQ_Pregunta;
    private javax.swing.JTextField txt_CVS_Pregunta;
    private javax.swing.JTextField txt_MAM_Pregunta;
    private javax.swing.JTextField txt_MAR_Pregunta;
    private javax.swing.JTextField txt_MAS_Pregunta;
    private javax.swing.JTextField txt_MAT_Pregunta;
    private javax.swing.JTextField txt_MCC_Pregunta;
    private javax.swing.JTextField txt_MCE_Pregunta;
    private javax.swing.JTextField txt_MCH_Pregunta;
    private javax.swing.JTextField txt_MDB_Pregunta;
    private javax.swing.JTextField txt_MDP_Pregunta;
    private javax.swing.JTextField txt_MDS_Pregunta;
    private javax.swing.JTextField txt_MEC_Pregunta;
    private javax.swing.JTextField txt_MED_Pregunta;
    private javax.swing.JTextField txt_MEL_Pregunta;
    private javax.swing.JTextField txt_MES_Pregunta;
    private javax.swing.JTextField txt_MFS_Pregunta;
    private javax.swing.JTextField txt_MGA_Pregunta;
    private javax.swing.JTextField txt_MGC_Pregunta;
    private javax.swing.JTextField txt_MGL_Pregunta;
    private javax.swing.JTextField txt_MHS_Pregunta;
    private javax.swing.JTextField txt_MHT_Pregunta;
    private javax.swing.JTextField txt_MLT_Pregunta;
    private javax.swing.JTextField txt_MMS_Pregunta;
    private javax.swing.JTextField txt_MPL_Pregunta;
    private javax.swing.JTextField txt_MPS_Pregunta;
    private javax.swing.JTextField txt_MPT_Pregunta;
    private javax.swing.JTextField txt_MRD_Pregunta;
    private javax.swing.JTextField txt_MRM_Pregunta;
    private javax.swing.JTextField txt_MSA_Pregunta;
    private javax.swing.JTextField txt_MSD_Pregunta;
    private javax.swing.JTextField txt_MSE_Pregunta;
    private javax.swing.JTextField txt_MSR_Pregunta;
    private javax.swing.JTextField txt_MST_Pregunta;
    private javax.swing.JTextField txt_MTE_Pregunta;
    private javax.swing.JTextField txt_MTR_Pregunta;
    private javax.swing.JTextField txt_MVH_Pregunta;
    private javax.swing.JTextField txt_MVL_Pregunta;
    private javax.swing.JTextField txt_MVT_Pregunta;
    private javax.swing.JTextField txt_MWT_Pregunta;
    private javax.swing.JTextField txt_Q2C_Pregunta;
    // End of variables declaration//GEN-END:variables
}