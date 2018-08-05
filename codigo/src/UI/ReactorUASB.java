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

public class ReactorUASB extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("Reactor UASB"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    // private Validaciones validar = new Validaciones();
    public Map<String, Pregunta> map = new TreeMap<>();
    PopupMenuListener listener;
    private Bod bod = new Bod("");
    private Listener_Popup Lpopup;
    Util util = new Util();
    Pregunta pg;
    private boolean eSave = true;
    private boolean esGuia = false; 

    public ReactorUASB(Bod bod) {
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
        bod.WinReactorUasb = true;//Bandera La ventana se ha abierto
        btn_guardar.setText("Guardar");
        btn_close.setText("Cerrar");
        String p;
        try {

            // - - - - - - Cargar el titulo de la sección  
            rs = db.ResultadoSelect("obtenerseccion", "10");

            lbl_titulo1.setText(rs.getString("Nombre"));
            lbl_titulo2.setText(rs.getString("Mensaje"));
            // - - - - - - Dato 1 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q2C
            rs = db.ResultadoSelect("datospregunta", "Q2C");

            String[] titulo2 = rs.getString("titulo2").split("\\|"); //Q2C en el titulo 2 tiene 2 posibles textos

            lbl_Q2C_titulo1.setText(rs.getString("titulo1"));
            lbl_Q2C_titulo2.setText(titulo2[1].trim());
            txt_Q2C_Pregunta.setText(bod.getVarFormateada(bod.calcularQ2Cm3Dia(), rs.getInt("decimales"))); // Q2C en m³/dia
            // - - - - - - Dato 2 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q3C
            rs = db.ResultadoSelect("datospregunta", "Q3C");

            String[] titulo3 = rs.getString("titulo2").split("\\|"); //Q3C en el titulo 2 tiene 2 posibles textos

            lbl_Q3C_titulo1.setText(rs.getString("titulo1"));
            lbl_Q3C_titulo2.setText(titulo3[1].trim());
            txt_Q3C_Pregunta.setText(bod.getVarFormateada(bod.calcularQ3Cm3Dia(), rs.getInt("decimales"))); // Q3C en m³/dia
            // - - - - - - Dato 3 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Si CAB no fue establecido, se usa CAQ, se pueden mostrar los 2
            rs = db.ResultadoSelect("datospregunta", "CAB");

            if (bod.getVarDob("CAB") != 0) {
                txt_CAB_Pregunta.setText(bod.getVarFormateada(bod.getVarDob("CAB"), rs.getInt("decimales")));
                lbl_CAB_titulo1.setText(rs.getString("titulo1"));
                lbl_CAB_titulo2.setText(rs.getString("titulo2"));
            } else {
                lbl_CAB_titulo1.setText(rs.getString("titulo1"));
                lbl_CAB_titulo2.setText("");
                txt_CAB_Pregunta.setEnabled(false);
                lbl_CAB_titulo1.setEnabled(false);
                lbl_CAB_titulo2.setEnabled(false);
            }

            rs = db.ResultadoSelect("datospregunta", "CAQ");
            txt_CAQ_Pregunta.setText(bod.getVarFormateada(bod.getVarDob("CAQ"), rs.getInt("decimales")));

            lbl_CAQ_titulo1.setText(rs.getString("titulo1"));
            lbl_CAQ_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Dato 4 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - CAT
            rs = db.ResultadoSelect("datospregunta", "CAT");

            lbl_CAT_titulo1.setText(rs.getString("titulo1"));
            lbl_CAT_titulo2.setText(rs.getString("titulo2"));
            txt_CAT_Pregunta.setText(bod.getVarFormateadaPorNombre("CAT", rs.getInt("decimales")));
            // - - - - - - Pregunta 01 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UDQ
            rs = db.ResultadoSelect("datospregunta", "UDQ");

            pg = new Pregunta(); //Objeto que será creado con datos, básicos de rangos y mensajes
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UDQ", pg);

            lbl_UDQ_titulo1.setText(pg.tit1);
            lbl_UDQ_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_UDQ_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 500, 360);
            // - - - - - - Pregunta 02 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UTH
            rs = db.ResultadoSelect("datospregunta", "UTH");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UTH", pg);

            lbl_UTH_titulo1.setText(pg.tit1);
            lbl_UTH_titulo2.setText(rs.getString("titulo2"));

            txt_UTH_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_UTH_Pregunta.addFocusListener(new FocusAdapter() { //Listener que se dispara cuando la casilla pierde focus
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("UTH");
                }
            });
            AsignarPopupBtn(btn_UTH_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 500, 360);
            // - - - - - - Pregunta 03 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UVR
            rs = db.ResultadoSelect("datospregunta", "UVR");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UVR", pg);

            lbl_UVR_titulo1.setText(pg.tit1);
            lbl_UVR_titulo2.setText(rs.getString("titulo2"));

            //ListenerCampoTxt(txt_UVR_Pregunta);
            AsignarPopupBtn(btn_UVR_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 500, 360);
            // - - - - - - Pregunta 04 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - URV 
            rs = db.ResultadoSelect("datospregunta", "URV");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("URV", pg);

            lbl_URV_titulo1.setText(pg.tit1);
            lbl_URV_titulo2.setText(rs.getString("titulo2"));

            txt_URV_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_URV_Pregunta.addFocusListener(new FocusAdapter() { //Listener que se dispara cuando la casilla pierde focus
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("URV");
                }
            });
            // - - - - - - Pregunta 04A Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UNR
            rs = db.ResultadoSelect("datospregunta", "UNR");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UNR", pg);

            lbl_UNR_titulo1.setText(pg.tit1);
            lbl_UNR_titulo2.setText(rs.getString("titulo2"));

            txt_UNR_Pregunta.setText(rs.getString("valorpordefecto"));

            AsignarPopupBtn(btn_UNR_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 500, 360);
            // - - - - - - Pregunta 05 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UVU
            rs = db.ResultadoSelect("datospregunta", "UVU");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UVU", pg);

            lbl_UVU_titulo1.setText(pg.tit1);
            lbl_UVU_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_UVU_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 500, 360);
            // - - - - - - Pregunta 06 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UHR
            rs = db.ResultadoSelect("datospregunta", "UHR");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UHR", pg);

            lbl_UHR_titulo1.setText(pg.tit1);
            lbl_UHR_titulo2.setText(rs.getString("titulo2"));

            txt_UHR_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_UHR_Pregunta.addFocusListener(new FocusAdapter() { //Listener que se dispara cuando la casilla pierde focus
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("UHR");
                }
            });
            AsignarPopupBtn(btn_UHR_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 450, 560);
            // - - - - - - Pregunta 07 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UHS
            rs = db.ResultadoSelect("datospregunta", "UHS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UHS", pg);

            lbl_UHS_titulo1.setText(pg.tit1);
            lbl_UHS_titulo2.setText(rs.getString("titulo2"));

            txt_UHS_Pregunta.setText(rs.getString("valorpordefecto"));
            // - - - - - - Pregunta 08 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UHD
            rs = db.ResultadoSelect("datospregunta", "UHD");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UHD", pg);

            lbl_UHD_titulo1.setText(pg.tit1);
            lbl_UHD_titulo2.setText(rs.getString("titulo2"));

            txt_UHD_Pregunta.setText(rs.getString("valorpordefecto"));
            // - - - - - - Pregunta 09 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UAR
            rs = db.ResultadoSelect("datospregunta", "UAR");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UAR", pg);

            lbl_UAR_titulo1.setText(pg.tit1);
            lbl_UAR_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_UAR_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 450, 320);
            // - - - - - - Pregunta 10 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UAA
            rs = db.ResultadoSelect("datospregunta", "UAA");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UAA", pg);

            lbl_UAA_titulo1.setText(pg.tit1);
            lbl_UAA_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_UAA_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 300, 220);
            // - - - - - - Pregunta 11 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UAL
            rs = db.ResultadoSelect("datospregunta", "UAL");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UAL", pg);

            lbl_UAL_titulo1.setText(pg.tit1);
            lbl_UAL_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 12 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UAT
            rs = db.ResultadoSelect("datospregunta", "UAT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UAT", pg);

            lbl_UAT_titulo1.setText(pg.tit1);
            lbl_UAT_titulo2.setText(rs.getString("titulo2"));
            lbl_UAT_desc.setText(pg.desc);

            AsignarPopupBtn(btn_UAT_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 300, 320);
            // - - - - - - Pregunta 13 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UVT
            rs = db.ResultadoSelect("datospregunta", "UVT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UVT", pg);

            lbl_UVT_titulo1.setText(pg.tit1);
            lbl_UVT_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_UVT_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 300, 260);
            // - - - - - - Pregunta 14 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - URH
            rs = db.ResultadoSelect("datospregunta", "URH");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("URH", pg);

            lbl_URH_titulo1.setText(pg.tit1);
            lbl_URH_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_URH_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 500, 280);
            // - - - - - - Pregunta 15 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UCH
            rs = db.ResultadoSelect("datospregunta", "UCH");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UCH", pg);

            lbl_UCH_titulo1.setText(pg.tit1);
            lbl_UCH_titulo2.setText(rs.getString("titulo2"));
            lbl_UCH_desc.setText(pg.desc);

            AsignarPopupBtn(btn_UCH_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 500, 360);
            // - - - - - - Pregunta 16 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UCO
            rs = db.ResultadoSelect("datospregunta", "UCO");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UCO", pg);

            lbl_UCO_titulo1.setText(pg.tit1);
            lbl_UCO_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_UCO_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 450, 490);
            // - - - - - - Pregunta 17 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UVN
            rs = db.ResultadoSelect("datospregunta", "UVN");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UVN", pg);

            lbl_UVN_titulo1.setText(pg.tit1);
            lbl_UVN_titulo2.setText(rs.getString("titulo2"));
            lbl_UVN_desc.setText(pg.desc);

            AsignarPopupBtn(btn_UVN_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 570, 650);
            // - - - - - - Pregunta 18 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UVM
            rs = db.ResultadoSelect("datospregunta", "UVM");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UVM", pg);

            lbl_UVM_titulo1.setText(pg.tit1);
            lbl_UVM_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 19 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UAD
            rs = db.ResultadoSelect("datospregunta", "UAD");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UAD", pg);

            lbl_UAD_titulo1.setText(pg.tit1);
            lbl_UAD_titulo2.setText(rs.getString("titulo2"));
            lbl_UAD_desc.setText(pg.desc);

            txt_UAD_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_UAD_Pregunta.addFocusListener(new FocusAdapter() { //Listener que se dispara cuando la casilla pierde focus
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("UAD");
                }
            });
            AsignarPopupBtn(btn_UAD_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 520, 600);
            // - - - - - - Pregunta 20 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UNT
            rs = db.ResultadoSelect("datospregunta", "UNT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UNT", pg);

            lbl_UNT_titulo1.setText(pg.tit1);
            lbl_UNT_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_UNT_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 350, 280);
            // - - - - - - Pregunta 21 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - (Si CAB no fue establecido = UER, si CAB existe = URQ)

            if (bod.getVarDob("CAB") != 0) {
                rs = db.ResultadoSelect("datospregunta", "URQ");

                pg = new Pregunta();
                pg.deci = rs.getInt("decimales");
                pg.tit1 = rs.getString("titulo1");
                pg.rmin = rs.getDouble("rangomin");
                pg.rmax = rs.getDouble("rangomax");
                pg.erro = rs.getString("errormsg");
                pg.desc = rs.getString("descripcion");
                map.put("URQ", pg);

                lbl_URQ_titulo1.setText(pg.tit1);
                lbl_URQ_titulo2.setText(rs.getString("titulo2"));

                AsignarPopupBtn(btn_URQ_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 400);
            } else {
                lbl_URQ_titulo1.setText("");
                lbl_URQ_titulo2.setText("");
                lbl_URQ_titulo1.setEnabled(false);
                lbl_URQ_titulo2.setEnabled(false);
                btn_URQ_ayuda.setEnabled(false);
            }

            rs = db.ResultadoSelect("datospregunta", "UER");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UER", pg);

            lbl_UER_titulo1.setText(pg.tit1);
            lbl_UER_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_UER_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 400);

            // - - - - - - Pregunta 22 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - (Si CAB no fue establecido = UEC, si CAB existe = UEQ)
            if (bod.getVarDob("CAB") != 0) {
                rs = db.ResultadoSelect("datospregunta", "UEQ");

                pg = new Pregunta();
                pg.deci = rs.getInt("decimales");
                pg.tit1 = rs.getString("titulo1");
                pg.rmin = rs.getDouble("rangomin");
                pg.rmax = rs.getDouble("rangomax");
                pg.erro = rs.getString("errormsg");
                pg.desc = rs.getString("descripcion");
                map.put("UEQ", pg);

                lbl_UEQ_titulo1.setText(pg.tit1);
                lbl_UEQ_titulo2.setText(rs.getString("titulo2"));

                AsignarPopupBtn(btn_UEQ_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 300);
            } else {
                lbl_UEQ_titulo1.setText("");
                lbl_UEQ_titulo2.setText("");
                lbl_UEQ_titulo1.setEnabled(false);
                lbl_UEQ_titulo2.setEnabled(false);
                btn_UEQ_ayuda.setEnabled(false);
            }
            rs = db.ResultadoSelect("datospregunta", "UEC");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UEC", pg);

            lbl_UEC_titulo1.setText(pg.tit1);
            lbl_UEC_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_UEC_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 300);
            // - - - - - - Pregunta 26 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UCP
            rs = db.ResultadoSelect("datospregunta", "UCP");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UCP", pg);

            lbl_UCP_titulo1.setText(pg.tit1);
            lbl_UCP_titulo2.setText(rs.getString("titulo2"));

            txt_UCP_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_UCP_Pregunta.addFocusListener(new FocusAdapter() { //Listener que se dispara cuando la casilla pierde focus
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("UCP");
                }
            });
            // - - - - - - Pregunta 27 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UPL
            rs = db.ResultadoSelect("datospregunta", "UPL");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UPL", pg);

            lbl_UPL_titulo1.setText(pg.tit1);
            lbl_UPL_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_UPL_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 380, 300);
            // - - - - - - Pregunta 28 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UVL
            rs = db.ResultadoSelect("datospregunta", "UVL");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UVL", pg);

            lbl_UVL_titulo1.setText(pg.tit1);
            lbl_UVL_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_UVL_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 380, 300);
            // - - - - - - Pregunta 29 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - USM
            rs = db.ResultadoSelect("datospregunta", "USM");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("USM", pg);

            lbl_USM_titulo1.setText(pg.tit1);
            lbl_USM_titulo2.setText(rs.getString("titulo2"));
            lbl_USM_desc.setText(pg.desc);

            txt_USM_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_USM_Pregunta.addFocusListener(new FocusAdapter() { //Listener que se dispara cuando la casilla pierde focus
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("USM");
                }
            });
            // - - - - - - Pregunta 30 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UDM
            rs = db.ResultadoSelect("datospregunta", "UDM");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UDM", pg);

            lbl_UDM_titulo1.setText(pg.tit1);
            lbl_UDM_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_UDM_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 440, 320);
            // - - - - - - Pregunta 31 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UCT
            rs = db.ResultadoSelect("datospregunta", "UCT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UCT", pg);

            lbl_UCT_titulo1.setText(pg.tit1);
            lbl_UCT_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_UCT_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 320);
            // - - - - - - Pregunta 32 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UPM
            rs = db.ResultadoSelect("datospregunta", "UPM");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UPM", pg);

            lbl_UPM_titulo1.setText(pg.tit1);
            lbl_UPM_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_UPM_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 380, 300);
            // - - - - - - Pregunta 33 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UCM
            rs = db.ResultadoSelect("datospregunta", "UCM");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UCM", pg);

            lbl_UCM_titulo1.setText(pg.tit1);
            lbl_UCM_titulo2.setText(rs.getString("titulo2"));

            txt_UCM_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_UCM_Pregunta.addFocusListener(new FocusAdapter() { //Listener que se dispara cuando la casilla pierde focus
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("UCM");
                }
            });
            AsignarPopupBtn(btn_UCM_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 350, 220);
            // - - - - - - Pregunta 34 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UVB
            rs = db.ResultadoSelect("datospregunta", "UVB");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UVB", pg);

            lbl_UVB_titulo1.setText(pg.tit1);
            lbl_UVB_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_UVB_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 380, 330);
            // - - - - - - Pregunta 35 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UCB
            rs = db.ResultadoSelect("datospregunta", "UCB");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UCB", pg);

            lbl_UCB_titulo1.setText(pg.tit1);
            lbl_UCB_titulo2.setText(rs.getString("titulo2"));
            lbl_UCB_desc.setText(pg.desc);

            txt_UCB_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_UCB_Pregunta.addFocusListener(new FocusAdapter() { //Listener que se dispara cuando la casilla pierde focus
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("UCB");
                }
            });
            AsignarPopupBtn(btn_UCB_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 320);
            // - - - - - - Pregunta 36 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UEB
            rs = db.ResultadoSelect("datospregunta", "UEB");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UEB", pg);

            lbl_UEB_titulo1.setText(pg.tit1);

            String[] tt = rs.getString("titulo2").split("\\|"); //tiene 2 'titulo2'
            lbl_UEB_titulo2.setText(tt[0]);
            lbl_UEB_titulo3.setText(tt[1]);

            AsignarPopupBtn(btn_UEB_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 260);
            // - - - - - - Pregunta 37 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - URE
            rs = db.ResultadoSelect("datospregunta", "URE");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("URE", pg);

            lbl_URE_titulo1.setText(pg.tit1);
            lbl_URE_titulo2.setText(rs.getString("titulo2"));

            txt_URE_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_URE_Pregunta.addFocusListener(new FocusAdapter() { //Listener que se dispara cuando la casilla pierde focus
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("URE");
                }
            });
            AsignarPopupBtn(btn_URE_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 350, 180);
            // - - - - - - Pregunta 38 Cuestionario 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UPE
            rs = db.ResultadoSelect("datospregunta", "UPE");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UPE", pg);

            lbl_UPE_titulo1.setText(pg.tit1);

            String[] ti = rs.getString("titulo2").split("\\|");
            lbl_UPE_titulo2.setText(ti[0]);
            lbl_UPE_titulo3.setText(ti[1]);

            AsignarPopupBtn(btn_UPE_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 380, 280);
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - Si ya han sido llenado correctamente estos datos se obtienen de Bod
            if (bod.EditReactorUasb) {
                txt_UDQ_Pregunta.setText(bod.getVarFormateadaPorNombre("UDQ", map.get("UDQ").deci)); //Trae la variable por nombre y la formatea por cantidad de decimales
                txt_UTH_Pregunta.setText(bod.getVarFormateadaPorNombre("UTH", map.get("UTH").deci));
                txt_UVR_Pregunta.setText(bod.getVarFormateadaPorNombre("UVR", map.get("UVR").deci));
                txt_UNR_Pregunta.setText(bod.getVarFormateadaPorNombre("UNR", map.get("UNR").deci));
                txt_URV_Pregunta.setText(bod.getVarFormateadaPorNombre("URV", map.get("URV").deci));
                txt_UVU_Pregunta.setText(bod.getVarFormateadaPorNombre("UVU", map.get("UVU").deci));
                txt_UHR_Pregunta.setText(bod.getVarFormateadaPorNombre("UHR", map.get("UHR").deci));
                txt_UHS_Pregunta.setText(bod.getVarFormateadaPorNombre("UHS", map.get("UHS").deci));
                txt_UHD_Pregunta.setText(bod.getVarFormateadaPorNombre("UHD", map.get("UHD").deci));
                txt_UAR_Pregunta.setText(bod.getVarFormateadaPorNombre("UAR", map.get("UAR").deci));
                txt_UAA_Pregunta.setText(bod.getVarFormateadaPorNombre("UAA", map.get("UAA").deci));
                txt_UAL_Pregunta.setText(bod.getVarFormateadaPorNombre("UAL", map.get("UAL").deci));
                txt_UAT_Pregunta.setText(bod.getVarFormateadaPorNombre("UAT", map.get("UAT").deci));
                txt_UVT_Pregunta.setText(bod.getVarFormateadaPorNombre("UVT", map.get("UVT").deci));
                txt_URH_Pregunta.setText(bod.getVarFormateadaPorNombre("URH", map.get("URH").deci));
                txt_UCH_Pregunta.setText(bod.getVarFormateadaPorNombre("UCH", map.get("UCH").deci));
                txt_UCO_Pregunta.setText(bod.getVarFormateadaPorNombre("UCO", map.get("UCO").deci));
                txt_UVN_Pregunta.setText(bod.getVarFormateadaPorNombre("UVN", map.get("UVN").deci));
                txt_UVM_Pregunta.setText(bod.getVarFormateadaPorNombre("UVM", map.get("UVM").deci));
                txt_UAD_Pregunta.setText(bod.getVarFormateadaPorNombre("UAD", map.get("UAD").deci));
                txt_UNT_Pregunta.setText(bod.getVarFormateadaPorNombre("UNT", map.get("UNT").deci));

                if (bod.getVarDob("CAB") != 0) {
                    txt_URQ_Pregunta.setText(bod.getVarFormateadaPorNombre("URQ", map.get("URQ").deci));
                    txt_UEQ_Pregunta.setText(bod.getVarFormateadaPorNombre("UEQ", map.get("UEQ").deci));
                }
                txt_UER_Pregunta.setText(bod.getVarFormateadaPorNombre("UER", map.get("UER").deci));
                txt_UEC_Pregunta.setText(bod.getVarFormateadaPorNombre("UEC", map.get("UEC").deci));
                
                txt_UCP_Pregunta.setText(bod.getVarFormateadaPorNombre("UCP", map.get("UCP").deci));
                txt_UPL_Pregunta.setText(bod.getVarFormateadaPorNombre("UPL", map.get("UPL").deci));
                txt_UVL_Pregunta.setText(bod.getVarFormateadaPorNombre("UVL", map.get("UVL").deci));
                txt_USM_Pregunta.setText(bod.getVarFormateadaPorNombre("USM", map.get("USM").deci));
                txt_UDM_Pregunta.setText(bod.getVarFormateadaPorNombre("UDM", map.get("UDM").deci));
                txt_UCT_Pregunta.setText(bod.getVarFormateadaPorNombre("UCT", map.get("UCT").deci));
                txt_UPM_Pregunta.setText(bod.getVarFormateadaPorNombre("UPM", map.get("UPM").deci));
                txt_UCM_Pregunta.setText(bod.getVarFormateadaPorNombre("UCM", map.get("UCM").deci));
                txt_UVB_Pregunta.setText(bod.getVarFormateadaPorNombre("UVB", map.get("UVB").deci));
                txt_UCB_Pregunta.setText(bod.getVarFormateadaPorNombre("UCB", map.get("UCB").deci));
                txt_UEB_Pregunta.setText(bod.getVarFormateadaPorNombre("UEB", map.get("UEB").deci));
                txt_URE_Pregunta.setText(bod.getVarFormateadaPorNombre("URE", map.get("URE").deci));
                txt_UPE_Pregunta.setText(bod.getVarFormateadaPorNombre("UPE", map.get("UPE").deci));

                CalcularUEBK();//Se obtiene una variable no existe en Bod
                CalcularUPEK();//Se obtiene una variable no existe en Bod
            } else {
                CalcularUDQ();
                CalcularUNR();
                CalcularUHR_UHS_UHD();

                ejecutarFunciones("UTH");
                ejecutarFunciones("URV");
                ejecutarFunciones("UAD");
                ejecutarFunciones("UER");
                ejecutarFunciones("UEC");           
                ejecutarFunciones("UCP");
                ejecutarFunciones("USM");
                ejecutarFunciones("UCT");
                ejecutarFunciones("UCM");
                ejecutarFunciones("UCB");
                ejecutarFunciones("URE");
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
        lbl_UAA_error.setText("");
        lbl_UAD_error.setText("");
        lbl_UAL_error.setText("");
        lbl_UAR_error.setText("");
        lbl_UAT_error.setText("");
        lbl_UCB_error.setText("");
        lbl_UCH_error.setText("");
        lbl_UCM_error.setText("");
        lbl_UCO_error.setText("");
        lbl_UCP_error.setText("");
        lbl_UCT_error.setText("");
        lbl_UDM_error.setText("");
        lbl_UDQ_error.setText("");
        lbl_UEB_error.setText("");
        lbl_UEB_error2.setText("");
        lbl_UEC_error.setText("");
        lbl_UEQ_error.setText("");
        lbl_UER_error.setText("");
        lbl_UHD_error.setText("");
        lbl_UHR_error.setText("");
        lbl_UHS_error.setText("");
        lbl_UNR_error.setText("");
        lbl_UNT_error.setText("");
        lbl_UPE_error.setText("");
        lbl_UPE_error2.setText("");
        lbl_UPL_error.setText("");
        lbl_UPM_error.setText("");
        lbl_URE_error.setText("");
        lbl_URH_error.setText("");
        lbl_URQ_error.setText("");
        lbl_USM_error.setText("");
        lbl_UTH_error.setText("");
        lbl_UVB_error.setText("");
        lbl_UVL_error.setText("");
        lbl_UVM_error.setText("");
        lbl_UVN_error.setText("");
        lbl_UVR_error.setText("");
        lbl_UVT_error.setText("");
        lbl_UVU_error.setText("");
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
        lbl_save_desc.setText("");

        if (!CalcularUDQ()) {
            lbl_save_error.setText(map.get("UDQ").erro);
            lbl_save_desc.setText(map.get("UDQ").desc);
            lbl_save_titulo1.setText(map.get("UDQ").tit1);
            return false;
        }

        if (!CalcularUTH()) {
            lbl_save_error.setText(map.get("UTH").erro);
            lbl_save_desc.setText(map.get("UTH").desc);
            lbl_save_titulo1.setText(map.get("UTH").tit1);
            return false;
        }

        if (!CalcularUVR()) {
            lbl_save_error.setText(map.get("UVR").erro);
            lbl_save_desc.setText(map.get("UVR").desc);
            lbl_save_titulo1.setText(map.get("UVR").tit1);
            return false;
        }

        if (!CalcularURV()) {
            lbl_save_error.setText(map.get("URV").erro);
            lbl_save_desc.setText(map.get("URV").desc);
            lbl_save_titulo1.setText(map.get("URV").tit1);
            return false;
        }

        if (!CalcularUNR()) {
            lbl_save_error.setText(map.get("UNR").erro);
            lbl_save_desc.setText(map.get("UNR").desc);
            lbl_save_titulo1.setText(map.get("UNR").tit1);
            return false;
        }

        if (!CalcularUVU()) {
            lbl_save_error.setText(map.get("UVU").erro);
            lbl_save_desc.setText(map.get("UVU").desc);
            lbl_save_titulo1.setText(map.get("UVU").tit1);
            return false;
        }

        if (!CalcularUHR_UHS_UHD()) {
            lbl_save_error.setText(map.get("UHR").erro);
            lbl_save_desc.setText(map.get("UHR").desc);
            lbl_save_titulo1.setText(map.get("UHR").tit1);
            return false;
        }

        if (!CalcularUAR()) {
            lbl_save_error.setText(map.get("UAR").erro);
            lbl_save_desc.setText(map.get("UAR").desc);
            lbl_save_titulo1.setText(map.get("UAR").tit1);
            return false;
        }

        if (!CalcularUAA()) {
            lbl_save_error.setText(map.get("UAA").erro);
            lbl_save_desc.setText(map.get("UAA").desc);
            lbl_save_titulo1.setText(map.get("UAA").tit1);
            return false;
        }

        if (!CalcularUAL()) {
            lbl_save_error.setText(map.get("UAL").erro);
            lbl_save_desc.setText(map.get("UAL").desc);
            lbl_save_titulo1.setText(map.get("UAL").tit1);
            return false;
        }

        if (!CalcularUAT()) {
            lbl_save_error.setText(map.get("UAT").erro);
            lbl_save_desc.setText(map.get("UAT").desc);
            lbl_save_titulo1.setText(map.get("UAT").tit1);
            return false;
        }

        if (!CalcularUVT()) {
            lbl_save_error.setText(map.get("UVT").erro);
            lbl_save_desc.setText(map.get("UVT").desc);
            lbl_save_titulo1.setText(map.get("UVT").tit1);
            return false;
        }

        if (!CalcularURH()) {
            lbl_save_error.setText(map.get("URH").erro);
            lbl_save_desc.setText(map.get("URH").desc);
            lbl_save_titulo1.setText(map.get("URH").tit1);
            return false;
        }

        if (!CalcularUCH()) {
            lbl_save_error.setText(map.get("UCH").erro);
            lbl_save_desc.setText(map.get("UCH").desc);
            lbl_save_titulo1.setText(map.get("UCH").tit1);
            return false;
        }

        if (!CalcularUCO()) {
            lbl_save_error.setText(map.get("UCO").erro);
            lbl_save_desc.setText(map.get("UCO").desc);
            lbl_save_titulo1.setText(map.get("UCO").tit1);
            return false;
        }

        if (!CalcularUVN()) {
            lbl_save_error.setText(map.get("UVN").erro);
            lbl_save_desc.setText(map.get("UVN").desc);
            lbl_save_titulo1.setText(map.get("UVN").tit1);
            return false;
        }

        if (!CalcularUVM()) {
            lbl_save_error.setText(map.get("UVM").erro);
            lbl_save_desc.setText(map.get("UVM").desc);
            lbl_save_titulo1.setText(map.get("UVM").tit1);
            return false;
        }

        if (!CalcularUAD()) {
            lbl_save_error.setText(map.get("UAD").erro);
            lbl_save_desc.setText(map.get("UAD").desc);
            lbl_save_titulo1.setText(map.get("UAD").tit1);
            return false;
        }

        if (!CalcularUNT()) {
            lbl_save_error.setText(map.get("UNT").erro);
            lbl_save_desc.setText(map.get("UNT").desc);
            lbl_save_titulo1.setText(map.get("UNT").tit1);
            return false;
        }

        if (!CalcularURQ()) { //todo: la otra variable se setea en cero?
            if (bod.getVarDob("CAB") != 0) {
                lbl_save_error.setText(map.get("URQ").erro);
                lbl_save_desc.setText(map.get("URQ").desc);
                lbl_save_titulo1.setText(map.get("URQ").tit1);
            }
            return false;
        }

        if (!CalcularUER()) { //todo: la otra variable se setea en cero?          
            lbl_save_error.setText(map.get("UER").erro);
            lbl_save_desc.setText(map.get("UER").desc);
            lbl_save_titulo1.setText(map.get("UER").tit1);
            return false;
        }

        if (!CalcularUEQ()) {
            if (bod.getVarDob("CAB") != 0) {
                lbl_save_error.setText(map.get("UEQ").erro);
                lbl_save_desc.setText(map.get("UEQ").desc);
                lbl_save_titulo1.setText(map.get("UEQ").tit1);
            }
            return false;
        }

        if (!CalcularUEC()) {
            lbl_save_error.setText(map.get("UEC").erro);
            lbl_save_desc.setText(map.get("UEC").desc);
            lbl_save_titulo1.setText(map.get("UEC").tit1);
            return false;
        }

        if (!CalcularUCP()) {
            lbl_save_error.setText(map.get("UCP").erro);
            lbl_save_desc.setText(map.get("UCP").desc);
            lbl_save_titulo1.setText(map.get("UCP").tit1);
            return false;
        }

        if (!CalcularUPL()) {
            lbl_save_error.setText(map.get("UPL").erro);
            lbl_save_desc.setText(map.get("UPL").desc);
            lbl_save_titulo1.setText(map.get("UPL").tit1);
            return false;
        }

        if (!CalcularUVL()) {
            lbl_save_error.setText(map.get("UVL").erro);
            lbl_save_desc.setText(map.get("UVL").desc);
            lbl_save_titulo1.setText(map.get("UVL").tit1);
            return false;
        }

        if (!CalcularUSM()) {
            lbl_save_error.setText(map.get("USM").erro);
            lbl_save_desc.setText(map.get("USM").desc);
            lbl_save_titulo1.setText(map.get("USM").tit1);
            return false;
        }

        if (!CalcularUDM()) {
            lbl_save_error.setText(map.get("UDM").erro);
            lbl_save_desc.setText(map.get("UDM").desc);
            lbl_save_titulo1.setText(map.get("UDM").tit1);
            return false;
        }

        if (!CalcularUCT()) {
            lbl_save_error.setText(map.get("UCT").erro);
            lbl_save_desc.setText(map.get("UCT").desc);
            lbl_save_titulo1.setText(map.get("UCT").tit1);
            return false;
        }

        if (!CalcularUPM()) {
            lbl_save_error.setText(map.get("UPM").erro);
            lbl_save_desc.setText(map.get("UPM").desc);
            lbl_save_titulo1.setText(map.get("UPM").tit1);
            return false;
        }

        if (!CalcularUCM()) {
            lbl_save_error.setText(map.get("UCM").erro);
            lbl_save_desc.setText(map.get("UCM").desc);
            lbl_save_titulo1.setText(map.get("UCM").tit1);
            return false;
        }

        if (!CalcularUVB()) {
            lbl_save_error.setText(map.get("UVB").erro);
            lbl_save_desc.setText(map.get("UVB").desc);
            lbl_save_titulo1.setText(map.get("UVB").tit1);
            return false;
        }

        if (!CalcularUCB()) {
            lbl_save_error.setText(map.get("UCB").erro);
            lbl_save_desc.setText(map.get("UCB").desc);
            lbl_save_titulo1.setText(map.get("UCB").tit1);
            return false;
        }

        if (!CalcularUEB_UEBK()) {
            lbl_save_error.setText(map.get("UEB").erro);
            lbl_save_desc.setText(map.get("UEB").desc);
            lbl_save_titulo1.setText(map.get("UEB").tit1);
            return false;
        }

        if (!CalcularURE()) {
            lbl_save_error.setText(map.get("URE").erro);
            lbl_save_desc.setText(map.get("URE").desc);
            lbl_save_titulo1.setText(map.get("URE").tit1);
            return false;
        }

        if (!CalcularUPE_UPEK()) {
            lbl_save_error.setText(map.get("UPE").erro);
            lbl_save_desc.setText(map.get("UPE").desc);
            lbl_save_titulo1.setText(map.get("UPE").tit1);
            return false;
        }

        bod.EditReactorUasb = true;
        Main main = (Main) this.getTopLevelAncestor(); // Trata de ir  al contenedor principal 
        main.cancela = false;
        main.vbod = this.bod;

        if (!main.ComprobarCambio(7, true)) {//Comprobar cambios que afecten datos posteriores, si este guardar es de una edición.
            if (!main.cancela) {
                bod.EditReactorUasb = false;
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

    private boolean CalcularUDQ() {

        try {
            double x = bod.calcularUDQ();
            lbl_UDQ_error.setText("");

            if (bod.setVarDob(x, "UDQ", map.get("UDQ").rmin, map.get("UDQ").rmax)) { //Setear variable por nombre, solo si está entre sus rangos
                txt_UDQ_Pregunta.setText(bod.getVarFormateadaPorNombre("UDQ", map.get("UDQ").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUDQ " + ex.getMessage());
        }
        lbl_UDQ_error.setText(map.get("UDQ").erro);
        return false;
    }

    /**
     * Se comprueba primero (1) si el numero de la casilla está entre rangos ,
     * luego (2) si el numero esta entre los rangos de la tabla
     *
     * @return
     */
    private boolean CalcularUTH() {

        try {
            lbl_UTH_error.setText("");
            double x = Double.parseDouble(txt_UTH_Pregunta.getText());

            if (bod.setVarDob(x, "UTH", map.get("UTH").rmin, map.get("UTH").rmax)) { //1
                txt_UTH_Pregunta.setText(bod.getVarFormateadaPorNombre("UTH", map.get("UTH").deci));

                String temp = bod.calcularUTH(x);

                if (!temp.isEmpty()) {  //Se hace un cálculo de sugerencia
                    lbl_UTH_error.setText("Sugerido " + temp);//todo esp                    
                }
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUTH " + ex.getMessage());
        }
        lbl_UTH_error.setText(map.get("UTH").erro);
        return false;
    }

    private boolean CalcularUVR() {

        try {
            lbl_UVR_error.setText("");
            double x = (int) bod.calcularUVR(); //Se quitan decimales para UVR

            if (bod.setVarDob(x, "UVR", map.get("UVR").rmin, map.get("UVR").rmax)) {
                txt_UVR_Pregunta.setText(bod.getVarFormateadaPorNombre("UVR", map.get("UVR").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUVR " + ex.getMessage());
        }
        lbl_UVR_error.setText(map.get("UVR").erro);
        return false;
    }

    private boolean CalcularURV() {

        try {
            lbl_URV_error.setText("");

            if (bod.setVarDob(txt_URV_Pregunta.getText(), "URV", map.get("URV").rmin, map.get("URV").rmax)) {
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularURV " + ex.getMessage());
        }
        lbl_URV_error.setText(map.get("URV").erro);
        return false;
    }

    private boolean CalcularUNR() {

        try {
            lbl_UNR_error.setText("");
            int x = (int) Math.ceil(bod.calcularUNR());//aqui UNR debe ser formateada 'ceilling' a entero para ser seteada

            if (bod.setVarInt(x, "UNR", (int) map.get("UNR").rmin, (int) map.get("UNR").rmax)) {
                txt_UNR_Pregunta.setText(String.valueOf(x));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUNR " + ex.getMessage());
        }
        lbl_UNR_error.setText(map.get("UNR").erro);
        return false;
    }

    private boolean CalcularUVU() {

        try {
            lbl_UVU_error.setText("");
            double x = bod.calcularUVU();

            if (bod.setVarDob(x, "UVU", map.get("UVU").rmin, map.get("UVU").rmax)) {
                txt_UVU_Pregunta.setText(bod.getVarFormateadaPorNombre("UVU", map.get("UVU").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUVU " + ex.getMessage());
        }
        lbl_UVU_error.setText(map.get("UVU").erro);
        return false;
    }

    private boolean CalcularUHR_UHS_UHD() {

        try {
            lbl_UHR_error.setText("");
            lbl_UHS_error.setText("");
            lbl_UHD_error.setText("");
            double x = Double.parseDouble(txt_UHR_Pregunta.getText());

            if (bod.setVarDob(x, "UHR", map.get("UHR").rmin, map.get("UHR").rmax)) {

                String y = bod.calcularUHR(x);

                if (y.length() > 4) {
                    String[] z = y.split("\\|");
                    if (!bod.setVarDob(z[0], "UHR", map.get("UHR").rmin, map.get("UHR").rmax)) {
                        lbl_UHR_error.setText(map.get("UHR").erro);
                        return false;
                    };
                    if (!bod.setVarDob(z[1], "UHS", map.get("UHS").rmin, map.get("UHS").rmax)) {
                        lbl_UHS_error.setText(map.get("UHS").erro);
                        return false;
                    };
                    if (!bod.setVarDob(z[2], "UHD", map.get("UHD").rmin, map.get("UHD").rmax)) {
                        lbl_UHD_error.setText(map.get("UHD").erro);
                        return false;
                    };
                    txt_UHR_Pregunta.setText(z[0]);
                    txt_UHS_Pregunta.setText(z[1]);
                    txt_UHD_Pregunta.setText(z[2]);
                    return true;
                }
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUHR_UHS_UHD " + ex.getMessage());
        }
        lbl_UHR_error.setText(map.get("UHR").erro);
        return false;
    }

    private boolean CalcularUAR() {

        try {
            lbl_UAR_error.setText("");
            double x = bod.calcularUAR();

            if (bod.setVarDob(x, "UAR", map.get("UAR").rmin, map.get("UAR").rmax)) {
                txt_UAR_Pregunta.setText(bod.getVarFormateadaPorNombre("UAR", map.get("UAR").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUAR " + ex.getMessage());
        }
        lbl_UAR_error.setText(map.get("UAR").erro);
        return false;
    }

    private boolean CalcularUAL() {

        try {
            lbl_UAL_error.setText("");
            double x = bod.calcularUAL();

            if (bod.setVarDob(x, "UAL", map.get("UAL").rmin, map.get("UAL").rmax)) {
                txt_UAL_Pregunta.setText(bod.getVarFormateadaPorNombre("UAL", map.get("UAL").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUAL " + ex.getMessage());
        }
        lbl_UAL_error.setText(map.get("UAL").erro);
        return false;
    }

    private boolean CalcularUAA() {

        try {
            lbl_UAA_error.setText("");
            double x = bod.calcularUAA();

            if (bod.setVarDob(x, "UAA", map.get("UAA").rmin, map.get("UAA").rmax)) {
                txt_UAA_Pregunta.setText(bod.getVarFormateadaPorNombre("UAA", map.get("UAA").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUAA " + ex.getMessage());
        }
        lbl_UAA_error.setText(map.get("UAA").erro);
        return false;
    }

    private boolean CalcularUAT() {

        try {
            lbl_UAT_error.setText("");
            double x = bod.calcularUAT();

            if (bod.setVarDob(x, "UAT", map.get("UAT").rmin, map.get("UAT").rmax)) {
                txt_UAT_Pregunta.setText(bod.getVarFormateadaPorNombre("UAT", map.get("UAT").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUAT " + ex.getMessage());
        }
        lbl_UAT_error.setText(map.get("UAT").erro);
        return false;
    }

    private boolean CalcularUVT() {

        try {
            lbl_UVT_error.setText("");
            double x = bod.calcularUVT();

            if (bod.setVarDob(x, "UVT", map.get("UVT").rmin, map.get("UVT").rmax)) {
                txt_UVT_Pregunta.setText(bod.getVarFormateadaPorNombre("UVT", map.get("UVT").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUVT " + ex.getMessage());
        }
        lbl_UVT_error.setText(map.get("UVT").erro);
        return false;
    }

    private boolean CalcularURH() {

        try {
            lbl_URH_error.setText("");
            double x = bod.calcularURH();

            if (bod.setVarDob(x, "URH", map.get("URH").rmin, map.get("URH").rmax)) {
                txt_URH_Pregunta.setText(bod.getVarFormateadaPorNombre("URH", map.get("URH").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularURH " + ex.getMessage());
        }
        lbl_URH_error.setText(map.get("URH").erro);
        return false;
    }

    private boolean CalcularUCH() {

        try {
            lbl_UCH_error.setText("");
            double x = bod.calcularUCH();

            if (bod.setVarDob(x, "UCH", map.get("UCH").rmin, map.get("UCH").rmax)) {
                txt_UCH_Pregunta.setText(bod.getVarFormateadaPorNombre("UCH", map.get("UCH").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUCH " + ex.getMessage());
        }
        lbl_UCH_error.setText(map.get("UCH").erro);
        return false;
    }

    private boolean CalcularUCO() {

        try {
            lbl_UCO_error.setText("");
            double x = bod.calcularUCO();

            if (bod.setVarDob(x, "UCO", map.get("UCO").rmin, map.get("UCO").rmax)) {
                txt_UCO_Pregunta.setText(bod.getVarFormateadaPorNombre("UCO", map.get("UCO").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUCO " + ex.getMessage());
        }
        lbl_UCO_error.setText(map.get("UCO").erro);
        return false;
    }

    private boolean CalcularUVN() {

        try {
            lbl_UVN_error.setText("");
            double x = bod.calcularUVN();

            if (bod.setVarDob(x, "UVN", map.get("UVN").rmin, map.get("UVN").rmax)) {
                txt_UVN_Pregunta.setText(bod.getVarFormateadaPorNombre("UVN", map.get("UVN").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUVN " + ex.getMessage());
        }
        lbl_UVN_error.setText(map.get("UVN").erro);
        return false;
    }

    private boolean CalcularUVM() {

        try {
            lbl_UVM_error.setText("");
            double x = bod.calcularUVM();

            if (bod.setVarDob(x, "UVM", map.get("UVM").rmin, map.get("UVM").rmax)) {
                txt_UVM_Pregunta.setText(bod.getVarFormateadaPorNombre("UVM", map.get("UVM").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUVM " + ex.getMessage());
        }
        lbl_UVM_error.setText(map.get("UVM").erro);
        return false;
    }

    private boolean CalcularUAD() {

        try {
            lbl_UAD_error.setText("");

            if (bod.setVarDob(txt_UAD_Pregunta.getText(), "UAD", map.get("UAD").rmin, map.get("UAD").rmax)) {
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUAD " + ex.getMessage());
        }
        lbl_UAD_error.setText(map.get("UAD").erro);
        return false;
    }

    private boolean CalcularUNT() {

        try {
            lbl_UNT_error.setText("");
            double x = Math.ceil(bod.calcularUNT()); //Debe redondearse por arriba

            if (bod.setVarDob(x, "UNT", map.get("UNT").rmin, map.get("UNT").rmax)) {
                txt_UNT_Pregunta.setText(bod.getVarFormateadaPorNombre("UNT", map.get("UNT").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUNT " + ex.getMessage());
        }
        lbl_UNT_error.setText(map.get("UNT").erro);
        return false;
    }

    private boolean CalcularURQ() {

        if (bod.getVarDob("CAB") != 0) {
            try {
                lbl_URQ_error.setText("");
                double x = bod.calcularURQ();

                if (bod.setVarDob(x, "URQ", map.get("URQ").rmin, map.get("URQ").rmax)) {
                    txt_URQ_Pregunta.setText(bod.getVarFormateadaPorNombre("URQ", map.get("URQ").deci));
                    return true;
                }
            } catch (Exception ex) {
                log.error("Error: CalcularURQ " + ex.getMessage());
            }
            lbl_URQ_error.setText(map.get("URQ").erro);
            return false;
        }
        return true;//true por el if
    }

    private boolean CalcularUER() {

        try {
            lbl_UER_error.setText("");
            double x = bod.calcularUER();

            if (bod.setVarDob(x, "UER", map.get("UER").rmin, map.get("UER").rmax)) {
                txt_UER_Pregunta.setText(bod.getVarFormateadaPorNombre("UER", map.get("UER").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUER " + ex.getMessage());
        }
        lbl_UER_error.setText(map.get("UER").erro);
        return false;
    }

    private boolean CalcularUEQ() {

        if (bod.getVarDob("CAB") != 0) {
            try {
                lbl_UEQ_error.setText("");
                double x = bod.calcularUEQ();

                if (bod.setVarDob(x, "UEQ", map.get("UEQ").rmin, map.get("UEQ").rmax)) {
                    txt_UEQ_Pregunta.setText(bod.getVarFormateadaPorNombre("UEQ", map.get("UEQ").deci));
                    return true;
                }
            } catch (Exception ex) {
                log.error("Error: CalcularUEQ " + ex.getMessage());
            }
            lbl_UEQ_error.setText(map.get("UEQ").erro);
        }
        return true;//true por el if
    }

    private boolean CalcularUEC() {

        try {
            lbl_UEC_error.setText("");
            double x = bod.calcularUEC();

            if (bod.setVarDob(x, "UEC", map.get("UEC").rmin, map.get("UEC").rmax)) {
                txt_UEC_Pregunta.setText(bod.getVarFormateadaPorNombre("UEC", map.get("UEC").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUEC " + ex.getMessage());
        }
        lbl_UEC_error.setText(map.get("UEC").erro);

        return false;
    }

    private boolean CalcularUCP() {

        try {
            lbl_UCP_error.setText("");

            if (bod.setVarDob(txt_UCP_Pregunta.getText(), "UCP", map.get("UCP").rmin, map.get("UCP").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularUCP " + ex.getMessage());
        }
        lbl_UCP_error.setText(map.get("UCP").erro);
        return false;
    }

    private boolean CalcularUPL() {

        try {
            lbl_UPL_error.setText("");
            double x = bod.calcularUPL();

            if (bod.setVarDob(x, "UPL", map.get("UPL").rmin, map.get("UPL").rmax)) {
                txt_UPL_Pregunta.setText(bod.getVarFormateadaPorNombre("UPL", map.get("UPL").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUPL " + ex.getMessage());
        }
        lbl_UPL_error.setText(map.get("UPL").erro);
        return false;
    }

    private boolean CalcularUVL() {

        try {
            lbl_UVL_error.setText("");
            double x = bod.calcularUVL();

            if (bod.setVarDob(x, "UVL", map.get("UVL").rmin, map.get("UVL").rmax)) {
                txt_UVL_Pregunta.setText(bod.getVarFormateadaPorNombre("UVL", map.get("UVL").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUVL " + ex.getMessage());
        }
        lbl_UVL_error.setText(map.get("UVL").erro);
        return false;
    }

    private boolean CalcularUSM() {

        try {
            lbl_USM_error.setText("");

            if (bod.setVarDob(txt_USM_Pregunta.getText(), "USM", map.get("USM").rmin, map.get("USM").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularUSM " + ex.getMessage());
        }
        lbl_USM_error.setText(map.get("USM").erro);
        return false;
    }

    private boolean CalcularUDM() {

        try {
            lbl_UDM_error.setText("");
            double x = bod.calcularUDM();

            if (bod.setVarDob(x, "UDM", map.get("UDM").rmin, map.get("UDM").rmax)) {
                txt_UDM_Pregunta.setText(bod.getVarFormateadaPorNombre("UDM", map.get("UDM").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUDM " + ex.getMessage());
        }
        lbl_UDM_error.setText(map.get("UDM").erro);
        return false;
    }

    private boolean CalcularUCT() {

        try {
            lbl_UCT_error.setText("");
            double x = bod.calcularUCT();

            if (bod.setVarDob(x, "UCT", map.get("UCT").rmin, map.get("UCT").rmax)) {
                txt_UCT_Pregunta.setText(bod.getVarFormateadaPorNombre("UCT", map.get("UCT").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUCT " + ex.getMessage());
        }
        lbl_UCT_error.setText(map.get("UCT").erro);
        return false;
    }

    private boolean CalcularUPM() {

        try {
            lbl_UPM_error.setText("");
            double x = bod.calcularUPM();

            if (bod.setVarDob(x, "UPM", map.get("UPM").rmin, map.get("UPM").rmax)) {
                txt_UPM_Pregunta.setText(bod.getVarFormateadaPorNombre("UPM", map.get("UPM").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUPM " + ex.getMessage());
        }
        lbl_UPM_error.setText(map.get("UPM").erro);
        return false;
    }

    private boolean CalcularUCM() {

        try {
            lbl_UCM_error.setText("");

            if (bod.setVarDob(txt_UCM_Pregunta.getText(), "UCM", map.get("UCM").rmin, map.get("UCM").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularUCM " + ex.getMessage());
        }
        lbl_UCM_error.setText(map.get("UCM").erro);
        return false;
    }

    private boolean CalcularUVB() {

        try {
            lbl_UVB_error.setText("");
            double x = bod.calcularUVB();

            if (bod.setVarDob(x, "UVB", map.get("UVB").rmin, map.get("UVB").rmax)) {
                txt_UVB_Pregunta.setText(bod.getVarFormateadaPorNombre("UVB", map.get("UVB").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUVB " + ex.getMessage());
        }
        lbl_UVB_error.setText(map.get("UVB").erro);
        return false;
    }

    private boolean CalcularUCB() {

        try {
            lbl_UCB_error.setText("");

            if (bod.setVarDob(txt_UCB_Pregunta.getText(), "UCB", map.get("UCB").rmin, map.get("UCB").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularUCB " + ex.getMessage());
        }
        lbl_UCB_error.setText(map.get("UCB").erro);
        return false;
    }

    private boolean CalcularUEB_UEBK() {

        try {
            lbl_UEB_error.setText("");
            txt_UEB_Pregunta2.setText("");
            double x = bod.calcularUEB();

            if (bod.setVarDob(x, "UEB", map.get("UEB").rmin, map.get("UEB").rmax)) {

                txt_UEB_Pregunta.setText(bod.getVarFormateadaPorNombre("UEB", map.get("UEB").deci));
                CalcularUEBK();
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUEB " + ex.getMessage());
        }
        lbl_UEB_error.setText(map.get("UEB").erro);
        return false;
    }

    private void CalcularUEBK() {

        try {
            double x = bod.calcularUEBK();//Se procede a calcular este valor en otra medida --> kWh/d
            txt_UEB_Pregunta2.setText(bod.getVarFormateada(x, map.get("UEB").deci));
            return;
        } catch (Exception ex) {
            log.error("Error: CalcularUEBK " + ex.getMessage());
        }
        lbl_UEB_error2.setText(map.get("UEB").erro);
    }

    private boolean CalcularURE() {

        try {
            lbl_URE_error.setText("");

            if (bod.setVarDob(txt_URE_Pregunta.getText(), "URE", map.get("URE").rmin, map.get("URE").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularURE " + ex.getMessage());
        }
        lbl_URE_error.setText(map.get("URE").erro);
        return false;
    }

    private boolean CalcularUPE_UPEK() {

        try {
            lbl_UPE_error.setText("");
            txt_UPE_Pregunta2.setText("");
            double x = bod.calcularUPE();

            if (bod.setVarDob(x, "UPE", map.get("UPE").rmin, map.get("UPE").rmax)) {
                txt_UPE_Pregunta.setText(bod.getVarFormateadaPorNombre("UPE", map.get("UPE").deci));
                CalcularUPEK();
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularUPE " + ex.getMessage());
        }
        lbl_UPE_error.setText(map.get("UPE").erro);
        return false;
    }

    private void CalcularUPEK() {

        try {
            double x = bod.calcularUPEK();//Se procede a calcular este valor en otra medida --> Hp
            txt_UPE_Pregunta2.setText(bod.getVarFormateada(x, map.get("UPE").deci));
            return;

        } catch (Exception ex) {
            log.error("Error: CalcularUPE " + ex.getMessage());
        }
        lbl_UPE_error2.setText(map.get("UPE").erro);
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
            case "UTH":
                CalcularUTH();
                ejecutarFunciones("UVR");
                break;

            case "UVR": 
                CalcularUVR();
                ejecutarFunciones("UNR");
                break;
                
            case "URV":
                CalcularURV();
                ejecutarFunciones("UNR");
                break;

            case "UNR":
                CalcularUNR();
                CalcularUVU();
                ejecutarFunciones("UAR");
                break;

            case "UHR":
                CalcularUHR_UHS_UHD();
                ejecutarFunciones("UAR");
                break;

            case "UAR":
                CalcularUAR();
                CalcularUAA();
                ejecutarFunciones("UAL");
                ejecutarFunciones("UAT");
                break;

            case "UAL":
                CalcularUAL(); 
                break;

            case "UAT":
                CalcularUAT();
                ejecutarFunciones("UVT");
                CalcularUVN();
                CalcularUVM();
                ejecutarFunciones("UNT");
                break;

            case "UVT":
                CalcularUVT();
                ejecutarFunciones("URH");
                CalcularUCH();
                CalcularUCO();
                break;

            case "URH":
                CalcularURH();
                ejecutarFunciones("UER");
                ejecutarFunciones("URQ");
                break;

            case "UAD":
                CalcularUAD();
                ejecutarFunciones("UNT");
                break;

            case "UNT":
                CalcularUNT();
                break;

            case "UER":
                CalcularUER();
                ejecutarFunciones("UEC");
                ejecutarFunciones("UEQ");
                break;

            case "UEC":
                CalcularUEC();
                //ejecutarFunciones("UDM");
                break;

            case "URQ":
                CalcularURQ();
                break;

            case "UEQ":
                CalcularUEQ();
                ejecutarFunciones("UDM");
                break;

            case "UCP":
                CalcularUCP();
                ejecutarFunciones("UPL");
                break;

            case "UPL":
                CalcularUPL();
                CalcularUVL();
                break;

            case "USM":
                CalcularUSM();
                ejecutarFunciones("UDM");
                break;

            case "UDM":
                CalcularUDM();
                ejecutarFunciones("UPM");
                break;

            case "UCT":
                CalcularUCT();
                ejecutarFunciones("UPM");
                break;

            case "UPM":
                CalcularUPM();
                ejecutarFunciones("UVB");

                break;
            case "UCM":
                CalcularUCM();
                ejecutarFunciones("UVB");
                break;

            case "UVB":
                CalcularUVB();
                ejecutarFunciones("UEB");
                break;

            case "UCB":
                CalcularUCB();
                ejecutarFunciones("UEB");
                break;

            case "UEB":
                CalcularUEB_UEBK();
                CalcularUPE_UPEK();
                break;

            case "URE":
                CalcularURE();
                CalcularUPE_UPEK();
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
        bod.WinReactorUasb = false;
        Main main = (Main) this.getTopLevelAncestor();
        main.bod.WinReactorUasb = false;
        if(esGuia) main.guiaUsuario2();
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jsp_ReactorUasb = new javax.swing.JScrollPane();
        jp_Componentes = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        lbl_titulo1 = new javax.swing.JLabel();
        lbl_titulo2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        btn_guardar = new javax.swing.JButton();
        lbl_UAT_desc = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        lbl_Q2C_titulo2 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        btn_close = new javax.swing.JButton();
        lbl_save_error = new javax.swing.JLabel();
        lbl_save_desc = new javax.swing.JLabel();
        txt_Q2C_Pregunta = new javax.swing.JTextField();
        lbl_Q2C_titulo1 = new javax.swing.JLabel();
        lbl_Q3C_titulo2 = new javax.swing.JLabel();
        txt_Q3C_Pregunta = new javax.swing.JTextField();
        lbl_Q3C_titulo1 = new javax.swing.JLabel();
        lbl_CAQ_titulo2 = new javax.swing.JLabel();
        txt_CAQ_Pregunta = new javax.swing.JTextField();
        lbl_CAQ_titulo1 = new javax.swing.JLabel();
        lbl_CAT_titulo2 = new javax.swing.JLabel();
        txt_CAT_Pregunta = new javax.swing.JTextField();
        lbl_CAT_titulo1 = new javax.swing.JLabel();
        lbl_UDQ_error = new javax.swing.JLabel();
        lbl_UDQ_titulo2 = new javax.swing.JLabel();
        txt_UDQ_Pregunta = new javax.swing.JTextField();
        lbl_UDQ_titulo1 = new javax.swing.JLabel();
        btn_UDQ_ayuda = new javax.swing.JButton();
        lbl_UTH_error = new javax.swing.JLabel();
        lbl_UTH_titulo2 = new javax.swing.JLabel();
        txt_UTH_Pregunta = new javax.swing.JTextField();
        lbl_UTH_titulo1 = new javax.swing.JLabel();
        btn_UTH_ayuda = new javax.swing.JButton();
        lbl_UVR_error = new javax.swing.JLabel();
        lbl_UVR_titulo2 = new javax.swing.JLabel();
        txt_UVR_Pregunta = new javax.swing.JTextField();
        lbl_UVR_titulo1 = new javax.swing.JLabel();
        btn_UVR_ayuda = new javax.swing.JButton();
        lbl_UNR_error = new javax.swing.JLabel();
        lbl_UNR_titulo2 = new javax.swing.JLabel();
        txt_UNR_Pregunta = new javax.swing.JTextField();
        lbl_UNR_titulo1 = new javax.swing.JLabel();
        btn_UNR_ayuda = new javax.swing.JButton();
        lbl_UVU_error = new javax.swing.JLabel();
        lbl_UVU_titulo2 = new javax.swing.JLabel();
        txt_UVU_Pregunta = new javax.swing.JTextField();
        lbl_UVU_titulo1 = new javax.swing.JLabel();
        btn_UVU_ayuda = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JSeparator();
        lbl_UHR_error = new javax.swing.JLabel();
        lbl_UHR_titulo2 = new javax.swing.JLabel();
        txt_UHR_Pregunta = new javax.swing.JTextField();
        lbl_UHR_titulo1 = new javax.swing.JLabel();
        btn_UHR_ayuda = new javax.swing.JButton();
        lbl_UHS_error = new javax.swing.JLabel();
        lbl_UHS_titulo2 = new javax.swing.JLabel();
        txt_UHS_Pregunta = new javax.swing.JTextField();
        lbl_UHS_titulo1 = new javax.swing.JLabel();
        lbl_UHD_error = new javax.swing.JLabel();
        lbl_UHD_titulo2 = new javax.swing.JLabel();
        txt_UHD_Pregunta = new javax.swing.JTextField();
        lbl_UHD_titulo1 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        lbl_UAR_error = new javax.swing.JLabel();
        lbl_UAR_titulo2 = new javax.swing.JLabel();
        txt_UAR_Pregunta = new javax.swing.JTextField();
        lbl_UAR_titulo1 = new javax.swing.JLabel();
        btn_UAR_ayuda = new javax.swing.JButton();
        lbl_UAA_error = new javax.swing.JLabel();
        lbl_UAA_titulo2 = new javax.swing.JLabel();
        txt_UAA_Pregunta = new javax.swing.JTextField();
        lbl_UAA_titulo1 = new javax.swing.JLabel();
        btn_UAA_ayuda = new javax.swing.JButton();
        lbl_UAL_error = new javax.swing.JLabel();
        lbl_UAL_titulo2 = new javax.swing.JLabel();
        txt_UAL_Pregunta = new javax.swing.JTextField();
        lbl_UAL_titulo1 = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        lbl_UAT_error = new javax.swing.JLabel();
        lbl_UAT_titulo2 = new javax.swing.JLabel();
        txt_UAT_Pregunta = new javax.swing.JTextField();
        lbl_UAT_titulo1 = new javax.swing.JLabel();
        btn_UAT_ayuda = new javax.swing.JButton();
        lbl_UVT_error = new javax.swing.JLabel();
        lbl_UVT_titulo2 = new javax.swing.JLabel();
        txt_UVT_Pregunta = new javax.swing.JTextField();
        lbl_UVT_titulo1 = new javax.swing.JLabel();
        btn_UVT_ayuda = new javax.swing.JButton();
        lbl_URH_error = new javax.swing.JLabel();
        lbl_URH_titulo2 = new javax.swing.JLabel();
        txt_URH_Pregunta = new javax.swing.JTextField();
        lbl_URH_titulo1 = new javax.swing.JLabel();
        btn_URH_ayuda = new javax.swing.JButton();
        lbl_UEB_error = new javax.swing.JLabel();
        lbl_UEB_titulo2 = new javax.swing.JLabel();
        txt_UEB_Pregunta = new javax.swing.JTextField();
        lbl_UEB_titulo1 = new javax.swing.JLabel();
        btn_UEB_ayuda = new javax.swing.JButton();
        lbl_UEB_error2 = new javax.swing.JLabel();
        lbl_UEB_titulo3 = new javax.swing.JLabel();
        txt_UEB_Pregunta2 = new javax.swing.JTextField();
        lbl_URE_error = new javax.swing.JLabel();
        lbl_URE_titulo2 = new javax.swing.JLabel();
        txt_URE_Pregunta = new javax.swing.JTextField();
        lbl_URE_titulo1 = new javax.swing.JLabel();
        btn_URE_ayuda = new javax.swing.JButton();
        lbl_UCH_desc = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        lbl_UCH_error = new javax.swing.JLabel();
        lbl_UCH_titulo2 = new javax.swing.JLabel();
        txt_UCH_Pregunta = new javax.swing.JTextField();
        lbl_UCH_titulo1 = new javax.swing.JLabel();
        btn_UCH_ayuda = new javax.swing.JButton();
        lbl_UCO_error = new javax.swing.JLabel();
        lbl_UCO_titulo2 = new javax.swing.JLabel();
        txt_UCO_Pregunta = new javax.swing.JTextField();
        lbl_UCO_titulo1 = new javax.swing.JLabel();
        btn_UCO_ayuda = new javax.swing.JButton();
        lbl_UVN_desc = new javax.swing.JLabel();
        jSeparator13 = new javax.swing.JSeparator();
        lbl_UVN_error = new javax.swing.JLabel();
        lbl_UVN_titulo2 = new javax.swing.JLabel();
        txt_UVN_Pregunta = new javax.swing.JTextField();
        lbl_UVN_titulo1 = new javax.swing.JLabel();
        btn_UVN_ayuda = new javax.swing.JButton();
        lbl_UVM_error = new javax.swing.JLabel();
        lbl_UVM_titulo2 = new javax.swing.JLabel();
        txt_UVM_Pregunta = new javax.swing.JTextField();
        lbl_UVM_titulo1 = new javax.swing.JLabel();
        lbl_UAD_desc = new javax.swing.JLabel();
        jSeparator14 = new javax.swing.JSeparator();
        lbl_UAD_error = new javax.swing.JLabel();
        lbl_UAD_titulo2 = new javax.swing.JLabel();
        txt_UAD_Pregunta = new javax.swing.JTextField();
        lbl_UAD_titulo1 = new javax.swing.JLabel();
        btn_UAD_ayuda = new javax.swing.JButton();
        lbl_UNT_error = new javax.swing.JLabel();
        lbl_UNT_titulo2 = new javax.swing.JLabel();
        txt_UNT_Pregunta = new javax.swing.JTextField();
        lbl_UNT_titulo1 = new javax.swing.JLabel();
        btn_UNT_ayuda = new javax.swing.JButton();
        jSeparator15 = new javax.swing.JSeparator();
        lbl_UER_error = new javax.swing.JLabel();
        lbl_UER_titulo2 = new javax.swing.JLabel();
        txt_UER_Pregunta = new javax.swing.JTextField();
        lbl_UER_titulo1 = new javax.swing.JLabel();
        btn_UER_ayuda = new javax.swing.JButton();
        lbl_URQ_error = new javax.swing.JLabel();
        lbl_URQ_titulo2 = new javax.swing.JLabel();
        txt_URQ_Pregunta = new javax.swing.JTextField();
        lbl_URQ_titulo1 = new javax.swing.JLabel();
        lbl_UCP_error = new javax.swing.JLabel();
        lbl_UCP_titulo2 = new javax.swing.JLabel();
        txt_UCP_Pregunta = new javax.swing.JTextField();
        lbl_UCP_titulo1 = new javax.swing.JLabel();
        lbl_UPL_error = new javax.swing.JLabel();
        lbl_UPL_titulo2 = new javax.swing.JLabel();
        txt_UPL_Pregunta = new javax.swing.JTextField();
        lbl_UPL_titulo1 = new javax.swing.JLabel();
        btn_UPL_ayuda = new javax.swing.JButton();
        jSeparator16 = new javax.swing.JSeparator();
        lbl_USM_desc = new javax.swing.JLabel();
        jSeparator17 = new javax.swing.JSeparator();
        lbl_USM_error = new javax.swing.JLabel();
        lbl_USM_titulo2 = new javax.swing.JLabel();
        txt_USM_Pregunta = new javax.swing.JTextField();
        lbl_USM_titulo1 = new javax.swing.JLabel();
        lbl_UDM_error = new javax.swing.JLabel();
        lbl_UDM_titulo2 = new javax.swing.JLabel();
        txt_UDM_Pregunta = new javax.swing.JTextField();
        lbl_UDM_titulo1 = new javax.swing.JLabel();
        btn_UDM_ayuda = new javax.swing.JButton();
        lbl_UCT_error = new javax.swing.JLabel();
        lbl_UCT_titulo2 = new javax.swing.JLabel();
        txt_UCT_Pregunta = new javax.swing.JTextField();
        lbl_UCT_titulo1 = new javax.swing.JLabel();
        btn_UCT_ayuda = new javax.swing.JButton();
        lbl_UPM_error = new javax.swing.JLabel();
        lbl_UPM_titulo2 = new javax.swing.JLabel();
        txt_UPM_Pregunta = new javax.swing.JTextField();
        lbl_UPM_titulo1 = new javax.swing.JLabel();
        btn_UPM_ayuda = new javax.swing.JButton();
        lbl_UVL_error = new javax.swing.JLabel();
        lbl_UVL_titulo2 = new javax.swing.JLabel();
        txt_UVL_Pregunta = new javax.swing.JTextField();
        lbl_UVL_titulo1 = new javax.swing.JLabel();
        btn_UVL_ayuda = new javax.swing.JButton();
        jSeparator18 = new javax.swing.JSeparator();
        lbl_UCM_error = new javax.swing.JLabel();
        lbl_UCM_titulo2 = new javax.swing.JLabel();
        txt_UCM_Pregunta = new javax.swing.JTextField();
        lbl_UCM_titulo1 = new javax.swing.JLabel();
        btn_UCM_ayuda = new javax.swing.JButton();
        lbl_UVB_error = new javax.swing.JLabel();
        lbl_UVB_titulo2 = new javax.swing.JLabel();
        txt_UVB_Pregunta = new javax.swing.JTextField();
        lbl_UVB_titulo1 = new javax.swing.JLabel();
        btn_UVB_ayuda = new javax.swing.JButton();
        lbl_UCB_desc = new javax.swing.JLabel();
        lbl_UCB_error = new javax.swing.JLabel();
        lbl_UCB_titulo2 = new javax.swing.JLabel();
        txt_UCB_Pregunta = new javax.swing.JTextField();
        lbl_UCB_titulo1 = new javax.swing.JLabel();
        btn_UCB_ayuda = new javax.swing.JButton();
        lbl_UPE_error = new javax.swing.JLabel();
        lbl_UPE_titulo2 = new javax.swing.JLabel();
        txt_UPE_Pregunta = new javax.swing.JTextField();
        lbl_UPE_titulo1 = new javax.swing.JLabel();
        btn_UPE_ayuda = new javax.swing.JButton();
        lbl_UPE_error2 = new javax.swing.JLabel();
        lbl_UPE_titulo3 = new javax.swing.JLabel();
        txt_UPE_Pregunta2 = new javax.swing.JTextField();
        lbl_save_titulo1 = new javax.swing.JLabel();
        lbl_UEC_error = new javax.swing.JLabel();
        lbl_UEC_titulo2 = new javax.swing.JLabel();
        txt_UEC_Pregunta = new javax.swing.JTextField();
        lbl_UEC_titulo1 = new javax.swing.JLabel();
        btn_UEC_ayuda = new javax.swing.JButton();
        lbl_UEQ_error = new javax.swing.JLabel();
        lbl_UEQ_titulo2 = new javax.swing.JLabel();
        txt_UEQ_Pregunta = new javax.swing.JTextField();
        lbl_UEQ_titulo1 = new javax.swing.JLabel();
        lbl_CAB_titulo2 = new javax.swing.JLabel();
        txt_CAB_Pregunta = new javax.swing.JTextField();
        lbl_CAB_titulo1 = new javax.swing.JLabel();
        btn_URQ_ayuda = new javax.swing.JButton();
        btn_UEQ_ayuda = new javax.swing.JButton();
        txt_URV_Pregunta = new javax.swing.JTextField();
        lbl_URV_titulo2 = new javax.swing.JLabel();
        lbl_URV_error = new javax.swing.JLabel();
        lbl_URV_titulo1 = new javax.swing.JLabel();
        btn_close2 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1024, 2524));

        jsp_ReactorUasb.setPreferredSize(new java.awt.Dimension(1024, 768));

        jp_Componentes.setPreferredSize(new java.awt.Dimension(1024, 2600));
        jp_Componentes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jp_Componentes.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 38, 1024, -1));

        lbl_titulo1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_titulo1.setForeground(new java.awt.Color(51, 51, 51));
        lbl_titulo1.setText("Titulo");
        jp_Componentes.add(lbl_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 300, 30));

        lbl_titulo2.setForeground(new java.awt.Color(0, 51, 102));
        lbl_titulo2.setText("Desc");
        jp_Componentes.add(lbl_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 0, 600, 30));
        jp_Componentes.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 1024, -1));

        btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Button_Salva_Guardar_21.png"))); // NOI18N
        btn_guardar.setText("save");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2350, 120, -1));

        lbl_UAT_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_UAT_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_UAT_desc.setText("Desc");
        lbl_UAT_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UAT_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 820, 610, 30));
        jp_Componentes.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 520, 1024, -1));

        lbl_Q2C_titulo2.setText("Titulo");
        lbl_Q2C_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2C_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 50, 80, 25));
        jp_Componentes.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 467, 1024, 0));
        jp_Componentes.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 2330, 1024, -1));

        btn_close.setText("Salir");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2390, 120, 30));

        lbl_save_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error.setText(" . . .");
        jp_Componentes.add(lbl_save_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 2350, 500, 35));

        lbl_save_desc.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_desc.setText(" . . .");
        jp_Componentes.add(lbl_save_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 2390, 610, 35));

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

        lbl_CAQ_titulo2.setText("Titulo");
        lbl_CAQ_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAQ_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 130, 80, 25));

        txt_CAQ_Pregunta.setEditable(false);
        jp_Componentes.add(txt_CAQ_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 130, 130, 25));

        lbl_CAQ_titulo1.setText("Titulo");
        lbl_CAQ_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAQ_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 300, 30));

        lbl_CAT_titulo2.setText("Titulo");
        lbl_CAT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 220, 80, 25));

        txt_CAT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_CAT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 220, 130, 25));

        lbl_CAT_titulo1.setText("Titulo");
        lbl_CAT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 300, 30));

        lbl_UDQ_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UDQ_error.setText(" . . .");
        jp_Componentes.add(lbl_UDQ_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 280, 340, 35));

        lbl_UDQ_titulo2.setText("Titulo");
        lbl_UDQ_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UDQ_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 280, 80, 25));

        txt_UDQ_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UDQ_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 280, 130, 25));

        lbl_UDQ_titulo1.setText("Titulo");
        lbl_UDQ_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UDQ_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 300, 30));

        btn_UDQ_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UDQ_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UDQ_ayuda.setContentAreaFilled(false);
        btn_UDQ_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UDQ_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 280, 25, 25));

        lbl_UTH_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UTH_error.setText(" . . .");
        jp_Componentes.add(lbl_UTH_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 320, 340, 35));

        lbl_UTH_titulo2.setText("Titulo");
        lbl_UTH_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UTH_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 320, 80, 25));

        txt_UTH_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_UTH_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 320, 130, 25));

        lbl_UTH_titulo1.setText("Titulo");
        lbl_UTH_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UTH_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 300, 30));

        btn_UTH_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UTH_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UTH_ayuda.setContentAreaFilled(false);
        btn_UTH_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UTH_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 320, 25, 25));

        lbl_UVR_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UVR_error.setText(" . . .");
        jp_Componentes.add(lbl_UVR_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 360, 340, 35));

        lbl_UVR_titulo2.setText("Titulo");
        lbl_UVR_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UVR_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 360, 80, 25));

        txt_UVR_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UVR_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 360, 130, 25));

        lbl_UVR_titulo1.setText("Titulo");
        lbl_UVR_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UVR_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 300, 30));

        btn_UVR_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UVR_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UVR_ayuda.setContentAreaFilled(false);
        btn_UVR_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UVR_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 360, 25, 25));

        lbl_UNR_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UNR_error.setText(" . . .");
        jp_Componentes.add(lbl_UNR_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 440, 340, 35));

        lbl_UNR_titulo2.setText("Titulo");
        lbl_UNR_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UNR_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 440, 80, 25));

        txt_UNR_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UNR_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 440, 130, 25));

        lbl_UNR_titulo1.setText("Titulo");
        lbl_UNR_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UNR_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, 300, 30));

        btn_UNR_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UNR_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UNR_ayuda.setContentAreaFilled(false);
        btn_UNR_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UNR_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 440, 25, 25));

        lbl_UVU_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UVU_error.setText(" . . .");
        jp_Componentes.add(lbl_UVU_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 480, 340, 35));

        lbl_UVU_titulo2.setText("Titulo");
        lbl_UVU_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UVU_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 480, 80, 25));

        txt_UVU_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UVU_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 480, 130, 25));

        lbl_UVU_titulo1.setText("Titulo");
        lbl_UVU_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UVU_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 480, 300, 30));

        btn_UVU_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UVU_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UVU_ayuda.setContentAreaFilled(false);
        btn_UVU_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UVU_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 480, 25, 25));
        jp_Componentes.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 670, 1024, -1));

        lbl_UHR_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UHR_error.setText(" . . .");
        jp_Componentes.add(lbl_UHR_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 540, 340, 35));

        lbl_UHR_titulo2.setText("Titulo");
        lbl_UHR_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UHR_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 540, 80, 25));

        txt_UHR_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_UHR_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 540, 130, 25));

        lbl_UHR_titulo1.setText("Titulo");
        lbl_UHR_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UHR_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 540, 300, 30));

        btn_UHR_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UHR_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UHR_ayuda.setContentAreaFilled(false);
        btn_UHR_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UHR_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 540, 25, 25));

        lbl_UHS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UHS_error.setText(" . . .");
        jp_Componentes.add(lbl_UHS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 580, 340, 35));

        lbl_UHS_titulo2.setText("Titulo");
        lbl_UHS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UHS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 580, 80, 25));

        txt_UHS_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UHS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 580, 130, 25));

        lbl_UHS_titulo1.setText("Titulo");
        lbl_UHS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UHS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 580, 300, 30));

        lbl_UHD_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UHD_error.setText(" . . .");
        jp_Componentes.add(lbl_UHD_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 620, 340, 35));

        lbl_UHD_titulo2.setText("Titulo");
        lbl_UHD_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UHD_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 620, 80, 25));

        txt_UHD_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UHD_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 620, 130, 25));

        lbl_UHD_titulo1.setText("Titulo");
        lbl_UHD_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UHD_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 620, 300, 30));
        jp_Componentes.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 810, 1024, -1));

        lbl_UAR_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UAR_error.setText(" . . .");
        jp_Componentes.add(lbl_UAR_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 690, 340, 35));

        lbl_UAR_titulo2.setText("Titulo");
        lbl_UAR_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UAR_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 690, 80, 25));

        txt_UAR_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UAR_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 690, 130, 25));

        lbl_UAR_titulo1.setText("Titulo");
        lbl_UAR_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UAR_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 690, 300, 30));

        btn_UAR_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UAR_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UAR_ayuda.setContentAreaFilled(false);
        btn_UAR_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UAR_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 690, 25, 25));

        lbl_UAA_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UAA_error.setText(" . . .");
        jp_Componentes.add(lbl_UAA_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 730, 340, 35));

        lbl_UAA_titulo2.setText("Titulo");
        lbl_UAA_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UAA_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 730, 80, 25));

        txt_UAA_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UAA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 730, 130, 25));

        lbl_UAA_titulo1.setText("Titulo");
        lbl_UAA_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UAA_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 730, 300, 30));

        btn_UAA_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UAA_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UAA_ayuda.setContentAreaFilled(false);
        btn_UAA_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UAA_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 730, 25, 25));

        lbl_UAL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UAL_error.setText(" . . .");
        jp_Componentes.add(lbl_UAL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 770, 340, 35));

        lbl_UAL_titulo2.setText("Titulo");
        lbl_UAL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UAL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 770, 80, 25));

        txt_UAL_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UAL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 770, 130, 25));

        lbl_UAL_titulo1.setText("Titulo");
        lbl_UAL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UAL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 770, 300, 30));
        jp_Componentes.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 990, 1024, -1));

        lbl_UAT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UAT_error.setText(" . . .");
        jp_Componentes.add(lbl_UAT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 860, 340, 35));

        lbl_UAT_titulo2.setText("Titulo");
        lbl_UAT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UAT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 860, 80, 25));

        txt_UAT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UAT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 860, 130, 25));

        lbl_UAT_titulo1.setText("Titulo");
        lbl_UAT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UAT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 860, 300, 30));

        btn_UAT_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UAT_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UAT_ayuda.setContentAreaFilled(false);
        btn_UAT_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UAT_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 860, 25, 25));

        lbl_UVT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UVT_error.setText(" . . .");
        jp_Componentes.add(lbl_UVT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 900, 340, 35));

        lbl_UVT_titulo2.setText("Titulo");
        lbl_UVT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UVT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 900, 80, 25));

        txt_UVT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UVT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 900, 130, 25));

        lbl_UVT_titulo1.setText("Titulo");
        lbl_UVT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UVT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 900, 300, 30));

        btn_UVT_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UVT_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UVT_ayuda.setContentAreaFilled(false);
        btn_UVT_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UVT_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 900, 25, 25));

        lbl_URH_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_URH_error.setText(" . . .");
        jp_Componentes.add(lbl_URH_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 940, 340, 35));

        lbl_URH_titulo2.setText("Titulo");
        lbl_URH_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_URH_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 940, 80, 25));

        txt_URH_Pregunta.setEditable(false);
        jp_Componentes.add(txt_URH_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 940, 130, 25));

        lbl_URH_titulo1.setText("Titulo");
        lbl_URH_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_URH_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 940, 300, 30));

        btn_URH_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_URH_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_URH_ayuda.setContentAreaFilled(false);
        btn_URH_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_URH_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 940, 25, 25));

        lbl_UEB_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UEB_error.setText(" . . .");
        jp_Componentes.add(lbl_UEB_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 2120, 340, 35));

        lbl_UEB_titulo2.setText("Titulo");
        lbl_UEB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UEB_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 2120, 80, 25));

        txt_UEB_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UEB_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 2120, 130, 25));

        lbl_UEB_titulo1.setText("Titulo");
        lbl_UEB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UEB_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2120, 300, 30));

        btn_UEB_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UEB_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UEB_ayuda.setContentAreaFilled(false);
        btn_UEB_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UEB_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 2120, 25, 25));

        lbl_UEB_error2.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UEB_error2.setText(" . . .");
        jp_Componentes.add(lbl_UEB_error2, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 2160, 340, 35));

        lbl_UEB_titulo3.setText("Titulo");
        lbl_UEB_titulo3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UEB_titulo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 2160, 80, 25));

        txt_UEB_Pregunta2.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_UEB_Pregunta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 2160, 130, 25));

        lbl_URE_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_URE_error.setText(" . . .");
        jp_Componentes.add(lbl_URE_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 2200, 340, 35));

        lbl_URE_titulo2.setText("Titulo");
        lbl_URE_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_URE_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 2200, 80, 25));

        txt_URE_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_URE_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 2200, 130, 25));

        lbl_URE_titulo1.setText("Titulo");
        lbl_URE_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_URE_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2200, 300, 30));

        btn_URE_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_URE_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_URE_ayuda.setContentAreaFilled(false);
        btn_URE_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_URE_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 2200, 25, 25));

        lbl_UCH_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_UCH_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_UCH_desc.setText("Desc");
        lbl_UCH_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UCH_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1000, 610, 30));
        jp_Componentes.add(jSeparator12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1120, 1024, -1));

        lbl_UCH_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UCH_error.setText(" . . .");
        jp_Componentes.add(lbl_UCH_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 1040, 340, 35));

        lbl_UCH_titulo2.setText("Titulo");
        lbl_UCH_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UCH_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1040, 80, 25));

        txt_UCH_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UCH_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1040, 130, 25));

        lbl_UCH_titulo1.setText("Titulo");
        lbl_UCH_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UCH_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1040, 300, 30));

        btn_UCH_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UCH_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UCH_ayuda.setContentAreaFilled(false);
        btn_UCH_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UCH_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1040, 25, 25));

        lbl_UCO_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UCO_error.setText(" . . .");
        jp_Componentes.add(lbl_UCO_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 1080, 340, 35));

        lbl_UCO_titulo2.setText("Titulo");
        lbl_UCO_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UCO_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1080, 100, 25));

        txt_UCO_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UCO_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1080, 130, 25));

        lbl_UCO_titulo1.setText("Titulo");
        lbl_UCO_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UCO_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1080, 300, 30));

        btn_UCO_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UCO_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UCO_ayuda.setContentAreaFilled(false);
        btn_UCO_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UCO_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1080, 25, 25));

        lbl_UVN_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_UVN_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_UVN_desc.setText("Desc");
        lbl_UVN_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UVN_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1130, 610, 30));
        jp_Componentes.add(jSeparator13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1260, 1024, -1));

        lbl_UVN_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UVN_error.setText(" . . .");
        jp_Componentes.add(lbl_UVN_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1170, 340, 35));

        lbl_UVN_titulo2.setText("Titulo");
        lbl_UVN_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UVN_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1170, 80, 25));

        txt_UVN_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UVN_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1170, 130, 25));

        lbl_UVN_titulo1.setText("Titulo");
        lbl_UVN_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UVN_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1170, 300, 30));

        btn_UVN_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UVN_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UVN_ayuda.setContentAreaFilled(false);
        btn_UVN_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UVN_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1170, 25, 25));

        lbl_UVM_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UVM_error.setText(" . . .");
        jp_Componentes.add(lbl_UVM_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1210, 340, 35));

        lbl_UVM_titulo2.setText("Titulo");
        lbl_UVM_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UVM_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1210, 80, 25));

        txt_UVM_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UVM_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1210, 130, 25));

        lbl_UVM_titulo1.setText("Titulo");
        lbl_UVM_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UVM_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1210, 300, 30));

        lbl_UAD_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_UAD_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_UAD_desc.setText("Desc");
        lbl_UAD_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UAD_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1270, 610, 30));
        jp_Componentes.add(jSeparator14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1390, 1024, -1));

        lbl_UAD_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UAD_error.setText(" . . .");
        jp_Componentes.add(lbl_UAD_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1310, 340, 35));

        lbl_UAD_titulo2.setText("Titulo");
        lbl_UAD_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UAD_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1310, 80, 25));

        txt_UAD_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_UAD_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1310, 130, 25));

        lbl_UAD_titulo1.setText("Titulo");
        lbl_UAD_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UAD_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1310, 300, 30));

        btn_UAD_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UAD_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UAD_ayuda.setContentAreaFilled(false);
        btn_UAD_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UAD_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1310, 25, 25));

        lbl_UNT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UNT_error.setText(" . . .");
        jp_Componentes.add(lbl_UNT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1350, 340, 35));

        lbl_UNT_titulo2.setText("Titulo");
        lbl_UNT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UNT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1350, 80, 25));

        txt_UNT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UNT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1350, 130, 25));

        lbl_UNT_titulo1.setText("Titulo");
        lbl_UNT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UNT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1350, 300, 30));

        btn_UNT_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UNT_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UNT_ayuda.setContentAreaFilled(false);
        btn_UNT_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UNT_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1350, 25, 25));
        jp_Componentes.add(jSeparator15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1570, 1024, -1));

        lbl_UER_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UER_error.setText(" . . .");
        jp_Componentes.add(lbl_UER_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1410, 340, 35));

        lbl_UER_titulo2.setText("Titulo");
        lbl_UER_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UER_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1410, 80, 25));

        txt_UER_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UER_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1410, 130, 25));

        lbl_UER_titulo1.setText("Titulo");
        lbl_UER_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UER_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1410, 300, 30));

        btn_UER_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UER_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UER_ayuda.setContentAreaFilled(false);
        btn_UER_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UER_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1410, 25, 25));

        lbl_URQ_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_URQ_error.setText(" . . .");
        jp_Componentes.add(lbl_URQ_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1450, 340, 35));

        lbl_URQ_titulo2.setText("Titulo");
        lbl_URQ_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_URQ_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1450, 80, 25));

        txt_URQ_Pregunta.setEditable(false);
        jp_Componentes.add(txt_URQ_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1450, 130, 25));

        lbl_URQ_titulo1.setText("Titulo");
        lbl_URQ_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_URQ_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1450, 300, 30));

        lbl_UCP_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UCP_error.setText(" . . .");
        jp_Componentes.add(lbl_UCP_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1580, 340, 35));

        lbl_UCP_titulo2.setText("Titulo");
        lbl_UCP_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UCP_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1580, 80, 25));

        txt_UCP_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_UCP_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1580, 130, 25));

        lbl_UCP_titulo1.setText("Titulo");
        lbl_UCP_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UCP_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1580, 300, 30));

        lbl_UPL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UPL_error.setText(" . . .");
        jp_Componentes.add(lbl_UPL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1620, 340, 35));

        lbl_UPL_titulo2.setText("Titulo");
        lbl_UPL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UPL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1620, 80, 25));

        txt_UPL_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UPL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1620, 130, 25));

        lbl_UPL_titulo1.setText("Titulo");
        lbl_UPL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UPL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1620, 300, 30));

        btn_UPL_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UPL_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UPL_ayuda.setContentAreaFilled(false);
        btn_UPL_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UPL_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1620, 25, 25));
        jp_Componentes.add(jSeparator16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1710, 1024, -1));

        lbl_USM_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_USM_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_USM_desc.setText("Desc");
        lbl_USM_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_USM_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1720, 610, 30));
        jp_Componentes.add(jSeparator17, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1930, 1024, -1));

        lbl_USM_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_USM_error.setText(" . . .");
        jp_Componentes.add(lbl_USM_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 1760, 340, 35));

        lbl_USM_titulo2.setText("Titulo");
        lbl_USM_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_USM_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1760, 110, 25));

        txt_USM_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_USM_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1760, 130, 25));

        lbl_USM_titulo1.setText("Titulo");
        lbl_USM_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_USM_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1760, 300, 30));

        lbl_UDM_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UDM_error.setText(" . . .");
        jp_Componentes.add(lbl_UDM_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 1800, 340, 35));

        lbl_UDM_titulo2.setText("Titulo");
        lbl_UDM_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UDM_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1800, 80, 25));

        txt_UDM_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UDM_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1800, 130, 25));

        lbl_UDM_titulo1.setText("Titulo");
        lbl_UDM_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UDM_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1800, 300, 30));

        btn_UDM_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UDM_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UDM_ayuda.setContentAreaFilled(false);
        btn_UDM_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UDM_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1800, 25, 25));

        lbl_UCT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UCT_error.setText(" . . .");
        jp_Componentes.add(lbl_UCT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 1840, 340, 35));

        lbl_UCT_titulo2.setText("Titulo");
        lbl_UCT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UCT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1840, 80, 25));

        txt_UCT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UCT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1840, 130, 25));

        lbl_UCT_titulo1.setText("Titulo");
        lbl_UCT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UCT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1840, 300, 40));

        btn_UCT_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UCT_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UCT_ayuda.setContentAreaFilled(false);
        btn_UCT_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UCT_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1840, 25, 25));

        lbl_UPM_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UPM_error.setText(" . . .");
        jp_Componentes.add(lbl_UPM_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 1880, 340, 35));

        lbl_UPM_titulo2.setText("Titulo");
        lbl_UPM_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UPM_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1880, 80, 25));

        txt_UPM_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UPM_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1880, 130, 25));

        lbl_UPM_titulo1.setText("Titulo");
        lbl_UPM_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UPM_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1880, 300, 30));

        btn_UPM_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UPM_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UPM_ayuda.setContentAreaFilled(false);
        btn_UPM_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UPM_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1880, 25, 25));

        lbl_UVL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UVL_error.setText(" . . .");
        jp_Componentes.add(lbl_UVL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1660, 340, 35));

        lbl_UVL_titulo2.setText("Titulo");
        lbl_UVL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UVL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1660, 80, 25));

        txt_UVL_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UVL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1660, 130, 25));

        lbl_UVL_titulo1.setText("Titulo");
        lbl_UVL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UVL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1660, 300, 30));

        btn_UVL_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UVL_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UVL_ayuda.setContentAreaFilled(false);
        btn_UVL_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UVL_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1660, 25, 25));
        jp_Componentes.add(jSeparator18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 2030, 1024, -1));

        lbl_UCM_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UCM_error.setText(" . . .");
        jp_Componentes.add(lbl_UCM_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1940, 340, 35));

        lbl_UCM_titulo2.setText("Titulo");
        lbl_UCM_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UCM_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1940, 80, 25));

        txt_UCM_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_UCM_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1940, 130, 25));

        lbl_UCM_titulo1.setText("Titulo");
        lbl_UCM_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UCM_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1940, 300, 30));

        btn_UCM_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UCM_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UCM_ayuda.setContentAreaFilled(false);
        btn_UCM_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UCM_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1940, 25, 25));

        lbl_UVB_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UVB_error.setText(" . . .");
        jp_Componentes.add(lbl_UVB_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1980, 340, 35));

        lbl_UVB_titulo2.setText("Titulo");
        lbl_UVB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UVB_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1980, 80, 25));

        txt_UVB_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UVB_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1980, 130, 25));

        lbl_UVB_titulo1.setText("Titulo");
        lbl_UVB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UVB_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1980, 300, 30));

        btn_UVB_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UVB_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UVB_ayuda.setContentAreaFilled(false);
        btn_UVB_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UVB_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1980, 25, 25));

        lbl_UCB_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_UCB_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_UCB_desc.setText("Desc");
        lbl_UCB_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UCB_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2040, 610, 30));

        lbl_UCB_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UCB_error.setText(" . . .");
        jp_Componentes.add(lbl_UCB_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 2080, 340, 35));

        lbl_UCB_titulo2.setText("Titulo");
        lbl_UCB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UCB_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 2080, 80, 25));

        txt_UCB_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UCB_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 2080, 130, 25));

        lbl_UCB_titulo1.setText("Titulo");
        lbl_UCB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UCB_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2080, 300, 30));

        btn_UCB_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UCB_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UCB_ayuda.setContentAreaFilled(false);
        btn_UCB_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UCB_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 2080, 25, 25));

        lbl_UPE_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UPE_error.setText(" . . .");
        jp_Componentes.add(lbl_UPE_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 2240, 340, 35));

        lbl_UPE_titulo2.setText("Titulo");
        lbl_UPE_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UPE_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 2240, 80, 25));

        txt_UPE_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UPE_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 2240, 130, 25));

        lbl_UPE_titulo1.setText("Titulo");
        lbl_UPE_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UPE_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2240, 300, 30));

        btn_UPE_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UPE_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UPE_ayuda.setContentAreaFilled(false);
        btn_UPE_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UPE_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 2240, 25, 25));

        lbl_UPE_error2.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UPE_error2.setText(" . . .");
        jp_Componentes.add(lbl_UPE_error2, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 2280, 340, 35));

        lbl_UPE_titulo3.setText("Titulo");
        lbl_UPE_titulo3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UPE_titulo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 2280, 80, 25));

        txt_UPE_Pregunta2.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_UPE_Pregunta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 2280, 130, 25));

        lbl_save_titulo1.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_titulo1.setText(" . . .");
        jp_Componentes.add(lbl_save_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 2350, 300, 35));

        lbl_UEC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UEC_error.setText(" . . .");
        jp_Componentes.add(lbl_UEC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1490, 340, 35));

        lbl_UEC_titulo2.setText("Titulo");
        lbl_UEC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UEC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1490, 80, 25));

        txt_UEC_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UEC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1490, 130, 25));

        lbl_UEC_titulo1.setText("Titulo");
        lbl_UEC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UEC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1490, 300, 30));

        btn_UEC_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UEC_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UEC_ayuda.setContentAreaFilled(false);
        btn_UEC_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UEC_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1490, 25, 25));

        lbl_UEQ_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_UEQ_error.setText(" . . .");
        jp_Componentes.add(lbl_UEQ_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1530, 340, 35));

        lbl_UEQ_titulo2.setText("Titulo");
        lbl_UEQ_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UEQ_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1530, 80, 25));

        txt_UEQ_Pregunta.setEditable(false);
        jp_Componentes.add(txt_UEQ_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1530, 130, 25));

        lbl_UEQ_titulo1.setText("Titulo");
        lbl_UEQ_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_UEQ_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1530, 300, 30));

        lbl_CAB_titulo2.setText("Titulo");
        lbl_CAB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAB_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 170, 80, 25));

        txt_CAB_Pregunta.setEditable(false);
        jp_Componentes.add(txt_CAB_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 170, 130, 25));

        lbl_CAB_titulo1.setText("Titulo");
        lbl_CAB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAB_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 300, 30));

        btn_URQ_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_URQ_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_URQ_ayuda.setContentAreaFilled(false);
        btn_URQ_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_URQ_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1450, 25, 25));

        btn_UEQ_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_UEQ_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_UEQ_ayuda.setContentAreaFilled(false);
        btn_UEQ_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_UEQ_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1530, 25, 25));

        txt_URV_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_URV_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 400, 130, 25));

        lbl_URV_titulo2.setText("Titulo");
        lbl_URV_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_URV_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 400, 80, 25));

        lbl_URV_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_URV_error.setText(" . . .");
        jp_Componentes.add(lbl_URV_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 400, 340, 35));

        lbl_URV_titulo1.setText("Titulo");
        lbl_URV_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_URV_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 300, 30));

        jsp_ReactorUasb.setViewportView(jp_Componentes);
        jp_Componentes.getAccessibleContext().setAccessibleName("");
        jp_Componentes.getAccessibleContext().setAccessibleDescription("");

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
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jsp_ReactorUasb, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 1008, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_close2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btn_close2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jsp_ReactorUasb, javax.swing.GroupLayout.DEFAULT_SIZE, 2450, Short.MAX_VALUE)
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
    private javax.swing.JButton btn_UAA_ayuda;
    private javax.swing.JButton btn_UAD_ayuda;
    private javax.swing.JButton btn_UAR_ayuda;
    private javax.swing.JButton btn_UAT_ayuda;
    private javax.swing.JButton btn_UCB_ayuda;
    private javax.swing.JButton btn_UCH_ayuda;
    private javax.swing.JButton btn_UCM_ayuda;
    private javax.swing.JButton btn_UCO_ayuda;
    private javax.swing.JButton btn_UCT_ayuda;
    private javax.swing.JButton btn_UDM_ayuda;
    private javax.swing.JButton btn_UDQ_ayuda;
    private javax.swing.JButton btn_UEB_ayuda;
    private javax.swing.JButton btn_UEC_ayuda;
    private javax.swing.JButton btn_UEQ_ayuda;
    private javax.swing.JButton btn_UER_ayuda;
    private javax.swing.JButton btn_UHR_ayuda;
    private javax.swing.JButton btn_UNR_ayuda;
    private javax.swing.JButton btn_UNT_ayuda;
    private javax.swing.JButton btn_UPE_ayuda;
    private javax.swing.JButton btn_UPL_ayuda;
    private javax.swing.JButton btn_UPM_ayuda;
    private javax.swing.JButton btn_URE_ayuda;
    private javax.swing.JButton btn_URH_ayuda;
    private javax.swing.JButton btn_URQ_ayuda;
    private javax.swing.JButton btn_UTH_ayuda;
    private javax.swing.JButton btn_UVB_ayuda;
    private javax.swing.JButton btn_UVL_ayuda;
    private javax.swing.JButton btn_UVN_ayuda;
    private javax.swing.JButton btn_UVR_ayuda;
    private javax.swing.JButton btn_UVT_ayuda;
    private javax.swing.JButton btn_UVU_ayuda;
    private javax.swing.JButton btn_close;
    private javax.swing.JButton btn_close2;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JPanel jp_Componentes;
    private javax.swing.JScrollPane jsp_ReactorUasb;
    private javax.swing.JLabel lbl_CAB_titulo1;
    private javax.swing.JLabel lbl_CAB_titulo2;
    private javax.swing.JLabel lbl_CAQ_titulo1;
    private javax.swing.JLabel lbl_CAQ_titulo2;
    private javax.swing.JLabel lbl_CAT_titulo1;
    private javax.swing.JLabel lbl_CAT_titulo2;
    private javax.swing.JLabel lbl_Q2C_titulo1;
    private javax.swing.JLabel lbl_Q2C_titulo2;
    private javax.swing.JLabel lbl_Q3C_titulo1;
    private javax.swing.JLabel lbl_Q3C_titulo2;
    private javax.swing.JLabel lbl_UAA_error;
    private javax.swing.JLabel lbl_UAA_titulo1;
    private javax.swing.JLabel lbl_UAA_titulo2;
    private javax.swing.JLabel lbl_UAD_desc;
    private javax.swing.JLabel lbl_UAD_error;
    private javax.swing.JLabel lbl_UAD_titulo1;
    private javax.swing.JLabel lbl_UAD_titulo2;
    private javax.swing.JLabel lbl_UAL_error;
    private javax.swing.JLabel lbl_UAL_titulo1;
    private javax.swing.JLabel lbl_UAL_titulo2;
    private javax.swing.JLabel lbl_UAR_error;
    private javax.swing.JLabel lbl_UAR_titulo1;
    private javax.swing.JLabel lbl_UAR_titulo2;
    private javax.swing.JLabel lbl_UAT_desc;
    private javax.swing.JLabel lbl_UAT_error;
    private javax.swing.JLabel lbl_UAT_titulo1;
    private javax.swing.JLabel lbl_UAT_titulo2;
    private javax.swing.JLabel lbl_UCB_desc;
    private javax.swing.JLabel lbl_UCB_error;
    private javax.swing.JLabel lbl_UCB_titulo1;
    private javax.swing.JLabel lbl_UCB_titulo2;
    private javax.swing.JLabel lbl_UCH_desc;
    private javax.swing.JLabel lbl_UCH_error;
    private javax.swing.JLabel lbl_UCH_titulo1;
    private javax.swing.JLabel lbl_UCH_titulo2;
    private javax.swing.JLabel lbl_UCM_error;
    private javax.swing.JLabel lbl_UCM_titulo1;
    private javax.swing.JLabel lbl_UCM_titulo2;
    private javax.swing.JLabel lbl_UCO_error;
    private javax.swing.JLabel lbl_UCO_titulo1;
    private javax.swing.JLabel lbl_UCO_titulo2;
    private javax.swing.JLabel lbl_UCP_error;
    private javax.swing.JLabel lbl_UCP_titulo1;
    private javax.swing.JLabel lbl_UCP_titulo2;
    private javax.swing.JLabel lbl_UCT_error;
    private javax.swing.JLabel lbl_UCT_titulo1;
    private javax.swing.JLabel lbl_UCT_titulo2;
    private javax.swing.JLabel lbl_UDM_error;
    private javax.swing.JLabel lbl_UDM_titulo1;
    private javax.swing.JLabel lbl_UDM_titulo2;
    private javax.swing.JLabel lbl_UDQ_error;
    private javax.swing.JLabel lbl_UDQ_titulo1;
    private javax.swing.JLabel lbl_UDQ_titulo2;
    private javax.swing.JLabel lbl_UEB_error;
    private javax.swing.JLabel lbl_UEB_error2;
    private javax.swing.JLabel lbl_UEB_titulo1;
    private javax.swing.JLabel lbl_UEB_titulo2;
    private javax.swing.JLabel lbl_UEB_titulo3;
    private javax.swing.JLabel lbl_UEC_error;
    private javax.swing.JLabel lbl_UEC_titulo1;
    private javax.swing.JLabel lbl_UEC_titulo2;
    private javax.swing.JLabel lbl_UEQ_error;
    private javax.swing.JLabel lbl_UEQ_titulo1;
    private javax.swing.JLabel lbl_UEQ_titulo2;
    private javax.swing.JLabel lbl_UER_error;
    private javax.swing.JLabel lbl_UER_titulo1;
    private javax.swing.JLabel lbl_UER_titulo2;
    private javax.swing.JLabel lbl_UHD_error;
    private javax.swing.JLabel lbl_UHD_titulo1;
    private javax.swing.JLabel lbl_UHD_titulo2;
    private javax.swing.JLabel lbl_UHR_error;
    private javax.swing.JLabel lbl_UHR_titulo1;
    private javax.swing.JLabel lbl_UHR_titulo2;
    private javax.swing.JLabel lbl_UHS_error;
    private javax.swing.JLabel lbl_UHS_titulo1;
    private javax.swing.JLabel lbl_UHS_titulo2;
    private javax.swing.JLabel lbl_UNR_error;
    private javax.swing.JLabel lbl_UNR_titulo1;
    private javax.swing.JLabel lbl_UNR_titulo2;
    private javax.swing.JLabel lbl_UNT_error;
    private javax.swing.JLabel lbl_UNT_titulo1;
    private javax.swing.JLabel lbl_UNT_titulo2;
    private javax.swing.JLabel lbl_UPE_error;
    private javax.swing.JLabel lbl_UPE_error2;
    private javax.swing.JLabel lbl_UPE_titulo1;
    private javax.swing.JLabel lbl_UPE_titulo2;
    private javax.swing.JLabel lbl_UPE_titulo3;
    private javax.swing.JLabel lbl_UPL_error;
    private javax.swing.JLabel lbl_UPL_titulo1;
    private javax.swing.JLabel lbl_UPL_titulo2;
    private javax.swing.JLabel lbl_UPM_error;
    private javax.swing.JLabel lbl_UPM_titulo1;
    private javax.swing.JLabel lbl_UPM_titulo2;
    private javax.swing.JLabel lbl_URE_error;
    private javax.swing.JLabel lbl_URE_titulo1;
    private javax.swing.JLabel lbl_URE_titulo2;
    private javax.swing.JLabel lbl_URH_error;
    private javax.swing.JLabel lbl_URH_titulo1;
    private javax.swing.JLabel lbl_URH_titulo2;
    private javax.swing.JLabel lbl_URQ_error;
    private javax.swing.JLabel lbl_URQ_titulo1;
    private javax.swing.JLabel lbl_URQ_titulo2;
    private javax.swing.JLabel lbl_URV_error;
    private javax.swing.JLabel lbl_URV_titulo1;
    private javax.swing.JLabel lbl_URV_titulo2;
    private javax.swing.JLabel lbl_USM_desc;
    private javax.swing.JLabel lbl_USM_error;
    private javax.swing.JLabel lbl_USM_titulo1;
    private javax.swing.JLabel lbl_USM_titulo2;
    private javax.swing.JLabel lbl_UTH_error;
    private javax.swing.JLabel lbl_UTH_titulo1;
    private javax.swing.JLabel lbl_UTH_titulo2;
    private javax.swing.JLabel lbl_UVB_error;
    private javax.swing.JLabel lbl_UVB_titulo1;
    private javax.swing.JLabel lbl_UVB_titulo2;
    private javax.swing.JLabel lbl_UVL_error;
    private javax.swing.JLabel lbl_UVL_titulo1;
    private javax.swing.JLabel lbl_UVL_titulo2;
    private javax.swing.JLabel lbl_UVM_error;
    private javax.swing.JLabel lbl_UVM_titulo1;
    private javax.swing.JLabel lbl_UVM_titulo2;
    private javax.swing.JLabel lbl_UVN_desc;
    private javax.swing.JLabel lbl_UVN_error;
    private javax.swing.JLabel lbl_UVN_titulo1;
    private javax.swing.JLabel lbl_UVN_titulo2;
    private javax.swing.JLabel lbl_UVR_error;
    private javax.swing.JLabel lbl_UVR_titulo1;
    private javax.swing.JLabel lbl_UVR_titulo2;
    private javax.swing.JLabel lbl_UVT_error;
    private javax.swing.JLabel lbl_UVT_titulo1;
    private javax.swing.JLabel lbl_UVT_titulo2;
    private javax.swing.JLabel lbl_UVU_error;
    private javax.swing.JLabel lbl_UVU_titulo1;
    private javax.swing.JLabel lbl_UVU_titulo2;
    private javax.swing.JLabel lbl_save_desc;
    private javax.swing.JLabel lbl_save_error;
    private javax.swing.JLabel lbl_save_titulo1;
    private javax.swing.JLabel lbl_titulo1;
    private javax.swing.JLabel lbl_titulo2;
    private javax.swing.JTextField txt_CAB_Pregunta;
    private javax.swing.JTextField txt_CAQ_Pregunta;
    private javax.swing.JTextField txt_CAT_Pregunta;
    private javax.swing.JTextField txt_Q2C_Pregunta;
    private javax.swing.JTextField txt_Q3C_Pregunta;
    private javax.swing.JTextField txt_UAA_Pregunta;
    private javax.swing.JTextField txt_UAD_Pregunta;
    private javax.swing.JTextField txt_UAL_Pregunta;
    private javax.swing.JTextField txt_UAR_Pregunta;
    private javax.swing.JTextField txt_UAT_Pregunta;
    private javax.swing.JTextField txt_UCB_Pregunta;
    private javax.swing.JTextField txt_UCH_Pregunta;
    private javax.swing.JTextField txt_UCM_Pregunta;
    private javax.swing.JTextField txt_UCO_Pregunta;
    private javax.swing.JTextField txt_UCP_Pregunta;
    private javax.swing.JTextField txt_UCT_Pregunta;
    private javax.swing.JTextField txt_UDM_Pregunta;
    private javax.swing.JTextField txt_UDQ_Pregunta;
    private javax.swing.JTextField txt_UEB_Pregunta;
    private javax.swing.JTextField txt_UEB_Pregunta2;
    private javax.swing.JTextField txt_UEC_Pregunta;
    private javax.swing.JTextField txt_UEQ_Pregunta;
    private javax.swing.JTextField txt_UER_Pregunta;
    private javax.swing.JTextField txt_UHD_Pregunta;
    private javax.swing.JTextField txt_UHR_Pregunta;
    private javax.swing.JTextField txt_UHS_Pregunta;
    private javax.swing.JTextField txt_UNR_Pregunta;
    private javax.swing.JTextField txt_UNT_Pregunta;
    private javax.swing.JTextField txt_UPE_Pregunta;
    private javax.swing.JTextField txt_UPE_Pregunta2;
    private javax.swing.JTextField txt_UPL_Pregunta;
    private javax.swing.JTextField txt_UPM_Pregunta;
    private javax.swing.JTextField txt_URE_Pregunta;
    private javax.swing.JTextField txt_URH_Pregunta;
    private javax.swing.JTextField txt_URQ_Pregunta;
    private javax.swing.JTextField txt_URV_Pregunta;
    private javax.swing.JTextField txt_USM_Pregunta;
    private javax.swing.JTextField txt_UTH_Pregunta;
    private javax.swing.JTextField txt_UVB_Pregunta;
    private javax.swing.JTextField txt_UVL_Pregunta;
    private javax.swing.JTextField txt_UVM_Pregunta;
    private javax.swing.JTextField txt_UVN_Pregunta;
    private javax.swing.JTextField txt_UVR_Pregunta;
    private javax.swing.JTextField txt_UVT_Pregunta;
    private javax.swing.JTextField txt_UVU_Pregunta;
    // End of variables declaration//GEN-END:variables
}
