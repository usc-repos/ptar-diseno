package UI;

import BO.Bod;
import DB.Dao;
import Componentes.Util;
import org.apache.log4j.Logger;
import Componentes.Configuracion;
import Componentes.Listener_Popup;
import Componentes.Pregunta;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class Rejillas extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("Rejillas"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    private Bod bod = new Bod("");
    public Map<String, Pregunta> map = new TreeMap<>();
    ButtonGroup btnGrupLLA = new ButtonGroup();
    private Listener_Popup Lpopup;
    Util util = new Util();
    Pregunta pg;
    ButtonGroup btngroupRBI = new ButtonGroup();
    private String[] rangosRBI;
    private boolean eSave = true;
    private boolean esGuia = false;

    public Rejillas(Bod bod) {
        this.bod = bod;
        initComponents();
        bod.WinRejillas = true;
        InicializarComponentes();
    }

    /**
     * Inicializa todos los elementos (jlabels,jtextbox,etc.) con sus
     * respectivos valores y textos desde al base de datos.
     */
    public void InicializarComponentes() {

        ResultSet rs;
        bod.WinRejillas = true;//Bandera La ventana se ha abierto

        try {
            btn_guardar.setText("Guardar");
            btn_close.setText("Cerrar");

            // - - - - - - Titulo de la sección            
            rs = db.ResultadoSelect("obtenerseccion", "8", null);

            lbl_titulo1.setText(rs.getString("Nombre")); //Título de este jpane
            lbl_titulo2.setText(rs.getString("Mensaje"));
            // - - - - - - Dato 1 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q2C
            rs = db.ResultadoSelect("datospregunta", "Q2C");

            String[] titulo2 = rs.getString("titulo2").split("\\|"); //el titulo 2 tiene 2 posibles textos

            lbl_Q2C_titulo1.setText(rs.getString("titulo1"));
            lbl_Q2C_titulo2.setText(titulo2[2].trim());
            txt_Q2C_Pregunta.setText(bod.getVarFormateada(bod.calcularQ2Cm3Seg(), rs.getInt("decimales")));
            // - - - - - - Dato 2 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q3C
            rs = db.ResultadoSelect("datospregunta", "Q3C");

            String[] titulo3 = rs.getString("titulo2").split("\\|"); //el titulo 2 tiene 2 posibles textos

            lbl_Q3C_titulo1.setText(rs.getString("titulo1"));
            lbl_Q3C_titulo2.setText(titulo3[2].trim());
            txt_Q3C_Pregunta.setText(bod.getVarFormateada(bod.calcularQ3Cm3Seg(), rs.getInt("decimales")));

            // - - - - - - Pregunta 1 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RPL
            rs = db.ResultadoSelect("datospregunta", "RPL");

            AsignarPopupBtn(btn_Q2C_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 370, 150);//Mensaje ayuda del anterior

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RPL", pg);

            lbl_RPL_titulo1.setText(pg.tit1);
            lbl_RPL_titulo2.setText(rs.getString("titulo2"));

            txt_RPL_Pregunta.addFocusListener(new FocusAdapter() { //Listener que se dispara cuando la casilla pierde focus
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("RPL");
                }
            });
            // - - - - - - Pregunta 2 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RTR
            rs = db.ResultadoSelect("datospregunta", "RTR");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RTR", pg);

            lbl_RTR_titulo1.setText(pg.tit1);
            lbl_RTR_titulo2.setText(rs.getString("titulo2"));
            lbl_RTR_desc.setText(pg.desc);


            AsignarPopupBtn(btn_RTR_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 360, 480);

            lbl_RNB_titulo3.setText(rs.getString("titulo2"));//Campo que usa datos de RTR

            int pos;
            try {
                pos = Integer.parseInt(rs.getString("valorpordefecto").trim());
            } catch (Exception e) {
                pos = 0;
            }

            //En la ecuación están los rangos del ddm
            rs = db.ResultadoSelect("obtenerecuacion", "selecctiporejillartr", "RTR");

            String[] equ = rs.getString("ecuacion").split("\\|");

            for (String elem : equ) {
                ddm_RTR_pregunta.addItem(elem);
            }
            ddm_RTR_pregunta.setSelectedIndex(pos);

            ddm_RTR_pregunta.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ejecutarFunciones("RTR");
                }
            });

            // - - - - - - Pregunta 3 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - REB
            rs = db.ResultadoSelect("datospregunta", "REB");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("REB", pg);

            lbl_REB_titulo1.setText(pg.tit1);
            lbl_REB_titulo2.setText(rs.getString("titulo2"));
            AsignarPopupBtn(btn_REB_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 420, 250);
            // - - - - - - Pregunta 4 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - REL
            rs = db.ResultadoSelect("datospregunta", "REL");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("REL", pg);

            lbl_REL_titulo1.setText(pg.tit1);
            lbl_REL_titulo2.setText(rs.getString("titulo2"));
            txt_REL_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_REL_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("REL");
                }
            });

            AsignarPopupBtn(btn_REL_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 450, 450);

            lbl_RNE_titulo3.setText(rs.getString("titulo2")); //usa resultados de REL
            // - - - - - - Pregunta 5 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RIB
            rs = db.ResultadoSelect("datospregunta", "RIB");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RIB", pg);

            lbl_RIB_titulo1.setText(pg.tit1);
            lbl_RIB_titulo2.setText(rs.getString("titulo2"));
            txt_RIB_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_RIB_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("RIB");
                }
            });

            //En la ecuación están los rangos 
            rs = db.ResultadoSelect("obtenerecuacion", "inclinacionbarrasrib", "RIB");

            rangosRBI = rs.getString("ecuacion").split(","); //Se guardan los rangos que cambian dependiendo de RBI

            // - - - - - - Pregunta 6 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RBI 
            rs = db.ResultadoSelect("datospregunta", "RBI");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RBI", pg);

            lbl_RBI_pregunta1.setText(pg.tit1);
            lbl_RBI_pregunta2.setText(rs.getString("titulo2"));
            lbl_RBI_desc.setText(pg.desc);

            btngroupRBI.add(lbl_RBI_pregunta1);
            btngroupRBI.add(lbl_RBI_pregunta2);

            lbl_RBI_pregunta1.addActionListener(new ActionListener() { //Listener para el radiobutton
                @Override
                public void actionPerformed(ActionEvent e) {
                    ejecutarFunciones("RBI");
                    txt_RIB_Pregunta.setEnabled(true);
                }
            });

            lbl_RBI_pregunta2.addActionListener(new ActionListener() { //Listener para el radiobutton
                @Override
                public void actionPerformed(ActionEvent e) {
                    ejecutarFunciones("RBI");
                    txt_RIB_Pregunta.setEnabled(true);
                }
            });

            AsignarPopupBtn(btn_RBI_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 450, 250);
            // - - - - - - Pregunta 7 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RPM
            rs = db.ResultadoSelect("datospregunta", "RPM");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RPM", pg);

            lbl_RPM_titulo1.setText(pg.tit1);
            lbl_RPM_titulo2.setText(rs.getString("titulo2"));
            txt_RPM_Pregunta.setText(rs.getString("valorpordefecto"));
            lbl_RPM_desc.setText(pg.desc);

            txt_RPM_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("RPM");
                }
            });

            AsignarPopupBtn(btn_RPM_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 300);
            // - - - - - - Pregunta 8 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RPN
            rs = db.ResultadoSelect("datospregunta", "RPN");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RPN", pg);

            lbl_RPN_titulo1.setText(pg.tit1);
            lbl_RPN_titulo2.setText(rs.getString("titulo2"));
            txt_RPN_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_RPN_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("RPN");
                }
            });
            // - - - - - - Pregunta 9 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RAU
            rs = db.ResultadoSelect("datospregunta", "RAU");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RAU", pg);

            lbl_RAU_titulo1.setText(pg.tit1);
            lbl_RAU_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_RAU_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 360, 260);
            // - - - - - - Pregunta 10 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RER
            rs = db.ResultadoSelect("datospregunta", "RER");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RER", pg);

            lbl_RER_titulo1.setText(pg.tit1);
            lbl_RER_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_RER_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 360, 260);
            // - - - - - - Pregunta 11 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RAT
            rs = db.ResultadoSelect("datospregunta", "RAT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RAT", pg);

            lbl_RAT_titulo1.setText(pg.tit1);
            lbl_RAT_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_RAT_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 350, 280);
            // - - - - - - Pregunta 12 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RAR
            rs = db.ResultadoSelect("datospregunta", "RAR");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RAR", pg);

            lbl_RAR_titulo1.setText(pg.tit1);
            lbl_RAR_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_RAR_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 360, 300);

            lbl_RCP_titulo4.setText(rs.getString("titulo2")); //Este componente y otros usan resultados y cosas de RAR
            lbl_RVM_titulo4.setText(rs.getString("titulo2")); //Este componente y otros usan resultados y cosas de RAR
            lbl_RCP_titulo3.setText(" ");
            lbl_RVM_titulo3.setText(" ");

            // - - - - - - Pregunta 13 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RCP
            rs = db.ResultadoSelect("datospregunta", "RCP");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RCP", pg);

            lbl_RCP_titulo1.setText(pg.tit1);
            lbl_RCP_titulo2.setText(rs.getString("titulo2"));
            lbl_RCP_desc.setText(pg.desc);

            AsignarPopupBtn(btn_RCP_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 380, 420);
            // - - - - - - Pregunta 14 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RCM
            rs = db.ResultadoSelect("datospregunta", "RCM");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RCM", pg);

            lbl_RCM_titulo1.setText(pg.tit1);
            lbl_RCM_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 15 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RLC
            rs = db.ResultadoSelect("datospregunta", "RLC");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RLC", pg);

            lbl_RLC_titulo1.setText(pg.tit1);
            lbl_RLC_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_RLC_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 360, 300);
            // - - - - - - Pregunta 16 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RVM
            rs = db.ResultadoSelect("datospregunta", "RVM");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RVM", pg);

            lbl_RVM_titulo1.setText(pg.tit1);
            lbl_RVM_titulo2.setText(rs.getString("titulo2"));
            lbl_RVM_desc.setText(pg.desc);

            AsignarPopupBtn(btn_RVM_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 360, 340);
            // - - - - - - Pregunta 17 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RVN
            rs = db.ResultadoSelect("datospregunta", "RVN");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RVN", pg);

            lbl_RVN_titulo1.setText(pg.tit1);
            lbl_RVN_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 18 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RNB
            rs = db.ResultadoSelect("datospregunta", "RNB");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RNB", pg);

            lbl_RNB_titulo1.setText(pg.tit1);
            lbl_RNB_titulo2.setText(rs.getString("titulo2"));
            lbl_RNB_desc.setText(pg.desc);

            AsignarPopupBtn(btn_RNB_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 360, 370);
            AsignarPopupBtn(btn_RNB_ayuda2, rs.getString("ayuda2"), "", 260, 100);
            // - - - - - - Pregunta 19 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RNE
            rs = db.ResultadoSelect("datospregunta", "RNE");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RNE", pg);

            lbl_RNE_titulo1.setText(pg.tit1);
            lbl_RNE_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 20 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RPC
            rs = db.ResultadoSelect("datospregunta", "RPC");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RPC", pg);

            lbl_RPC_titulo1.setText(pg.tit1);
            lbl_RPC_titulo2.setText(rs.getString("titulo2"));
            lbl_RPC_desc.setText(pg.desc);

            AsignarPopupBtn(btn_RPC_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 450, 470);
            // - - - - - - Pregunta 21 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RPS
            rs = db.ResultadoSelect("datospregunta", "RPS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RPS", pg);

            lbl_RPS_titulo1.setText(pg.tit1);
            lbl_RPS_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 22 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RPB
            rs = db.ResultadoSelect("datospregunta", "RPB");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RPB", pg);

            lbl_RPB_titulo1.setText(pg.tit1);
            lbl_RPB_titulo2.setText(rs.getString("titulo2"));
            txt_RPB_Pregunta.setText(rs.getString("valorpordefecto"));
            lbl_RPB_desc.setText(pg.desc);

            txt_RPB_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("RPB");
                }
            });

            String imagen = "";
            if (!bod.Generalpath.equals("")) { //todo: comprobar slash en linux
                imagen = bod.Generalpath + "/Images/Barras.png";
                imagen = rs.getString("ayuda").replace("[imagen]", imagen).replace("\\", "/");
            }

            AsignarPopupBtn(btn_RPB_ayuda, imagen, rs.getString("ayudalink"), 360, 600);
            // - - - - - - Pregunta 23 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RPH
            rs = db.ResultadoSelect("datospregunta", "RPH");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RPH", pg);

            lbl_RPH_titulo1.setText(pg.tit1);
            lbl_RPH_titulo2.setText(rs.getString("titulo2"));

            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
            // Si Bod cargó datos de Laguna Anaerobia desde la BD, porque Ya estaba editada, se proceden a llenar casillas con datos
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -          
            if (bod.EditRejillas) {

                txt_RPL_Pregunta.setText(bod.getVarFormateadaPorNombre("RPL", map.get("RPL").deci)); //Trae la variable por nombre y la formatea por cantidad de decimales
                ddm_RTR_pregunta.setSelectedIndex(bod.getVarInt("RTR"));
                txt_REB_Pregunta.setText(bod.getVarFormateadaPorNombre("REB", map.get("REB").deci));

                txt_REL_Pregunta.setText(bod.getVarFormateadaPorNombre("REL", map.get("REL").deci));
                txt_RNE_Pregunta2.setText(txt_REL_Pregunta.getText());

                txt_RIB_Pregunta.setText(bod.getVarFormateadaPorNombre("RIB", map.get("RIB").deci));
                txt_RIB_Pregunta.setEnabled(true);

                if (bod.getVarInt("RBI") > 0) {// 1 = mecánico
                    lbl_RBI_pregunta1.setSelected(false);
                    lbl_RBI_pregunta2.setSelected(true);
                } else {
                    lbl_RBI_pregunta1.setSelected(true);
                    lbl_RBI_pregunta2.setSelected(false);
                }
                CalcularRBI();
                txt_RPM_Pregunta.setText(bod.getVarFormateadaPorNombre("RPM", map.get("RPM").deci));
                txt_RPN_Pregunta.setText(bod.getVarFormateadaPorNombre("RPN", map.get("RPN").deci));
                txt_RAU_Pregunta.setText(bod.getVarFormateadaPorNombre("RAU", map.get("RAU").deci));
                txt_RER_Pregunta.setText(bod.getVarFormateadaPorNombre("RER", map.get("RER").deci));
                txt_RAT_Pregunta.setText(bod.getVarFormateadaPorNombre("RAT", map.get("RAT").deci));

                txt_RAR_Pregunta.setText(bod.getVarFormateadaPorNombre("RAR", map.get("RAR").deci));
                lbl_RCP_titulo3.setText(txt_RAR_Pregunta.getText());
                lbl_RVM_titulo3.setText(txt_RAR_Pregunta.getText());

                txt_RCP_Pregunta.setText(bod.getVarFormateadaPorNombre("RCP", map.get("RCP").deci));
                txt_RCM_Pregunta.setText(bod.getVarFormateadaPorNombre("RCM", map.get("RCM").deci));
                txt_RLC_Pregunta.setText(bod.getVarFormateadaPorNombre("RLC", map.get("RLC").deci));
                txt_RVM_Pregunta.setText(bod.getVarFormateadaPorNombre("RVM", map.get("RVM").deci));
                txt_RVN_Pregunta.setText(bod.getVarFormateadaPorNombre("RVN", map.get("RVN").deci));
                txt_RNB_Pregunta.setText(bod.getVarFormateadaPorNombre("RNB", map.get("RNB").deci));
                txt_RNE_Pregunta.setText(bod.getVarFormateadaPorNombre("RNE", map.get("RNE").deci));
                txt_RPC_Pregunta.setText(bod.getVarFormateadaPorNombre("RPC", map.get("RPC").deci));
                txt_RPS_Pregunta.setText(bod.getVarFormateadaPorNombre("RPS", map.get("RPS").deci));
                txt_RPB_Pregunta.setText(bod.getVarFormateadaPorNombre("RPB", map.get("RPB").deci));
                txt_RPH_Pregunta.setText(bod.getVarFormateadaPorNombre("RPH", map.get("RPH").deci));

            } else {// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -si no otras tareas de cálculos que se deben hacer al cargar

                CalcularRIB();//(*b) las que tienen valores por defecto  en algunos cálculos estan vacías pues no existen en el bod.               

                ejecutarFunciones("RTR");
                ejecutarFunciones("REL");
                ejecutarFunciones("RPM");
                ejecutarFunciones("RPN");
                ejecutarFunciones("RPB");
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
        lbl_RAR_error.setText("");
        lbl_RAT_error.setText("");
        lbl_RAU_error.setText("");
        lbl_RCM_error.setText("");
        lbl_RCP_error.setText("");
        lbl_REB_error.setText("");
        lbl_REL_error.setText("");
        lbl_RER_error.setText("");
        lbl_RIB_error.setText("");
        lbl_RLC_error.setText("");
        lbl_RNB_error.setText("");
        lbl_RNE_error.setText("");
        lbl_RPB_error.setText("");
        lbl_RPC_error.setText("");
        lbl_RPH_error.setText("");
        lbl_RPL_error.setText("");
        lbl_RPM_error.setText("");
        lbl_RPN_error.setText("");
        lbl_RPS_error.setText("");
        lbl_RVM_error.setText("");
        lbl_RVN_error.setText("");
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
    private boolean Guardar() { //Si se modifica esta clase se debe modificar su respectivo en 'Componentes' CX_

        lbl_save_error.setText("");
        lbl_save_desc.setText("");

        if (!CalcularRPL()) {
            lbl_save_error.setText(map.get("RPL").erro);
            lbl_save_desc.setText(map.get("RPL").desc);
            lbl_save_titulo1.setText(map.get("RPL").tit1);
            return false;
        }

        if (!CalcularRTR()) {
            lbl_save_error.setText(map.get("RTR").erro);
            lbl_save_desc.setText(map.get("RTR").desc);
            lbl_save_titulo1.setText(map.get("RTR").tit1);
            return false;
        }

        if (!CalcularREB()) {
            lbl_save_error.setText(map.get("REB").erro);
            lbl_save_desc.setText(map.get("REB").desc);
            lbl_save_titulo1.setText(map.get("REB").tit1);
            return false;
        }

        if (!CalcularREL()) {
            lbl_save_error.setText(map.get("REL").erro);
            lbl_save_desc.setText(map.get("REL").desc);
            lbl_save_titulo1.setText(map.get("REL").tit1);
            return false;
        }

        if (!CalcularRIB()) {
            lbl_save_error.setText(map.get("RIB").erro);
            lbl_save_desc.setText(map.get("RIB").desc);
            lbl_save_titulo1.setText(map.get("RIB").tit1);
            return false;
        }

        if (!CalcularRBI()) {
            lbl_save_error.setText(map.get("RBI").erro);
            lbl_save_desc.setText(map.get("RBI").desc);
            lbl_save_titulo1.setText(map.get("RBI").tit1);
            return false;
        }

        if (!CalcularRPM()) {
            lbl_save_error.setText(map.get("RPM").erro);
            lbl_save_desc.setText(map.get("RPM").desc);
            lbl_save_titulo1.setText(map.get("RPM").tit1);
            return false;
        }

        if (!CalcularRPN()) {
            lbl_save_error.setText(map.get("RPN").erro);
            lbl_save_desc.setText(map.get("RPN").desc);
            lbl_save_titulo1.setText(map.get("RPN").tit1);
            return false;
        }

        if (!CalcularRAU()) {
            lbl_save_error.setText(map.get("RAU").erro);
            lbl_save_desc.setText(map.get("RAU").desc);
            lbl_save_titulo1.setText(map.get("RAU").tit1);
            return false;
        }

        if (!CalcularRER()) {
            lbl_save_error.setText(map.get("RER").erro);
            lbl_save_desc.setText(map.get("RER").desc);
            lbl_save_titulo1.setText(map.get("RER").tit1);
            return false;
        }

        if (!CalcularRAT()) {
            lbl_save_error.setText(map.get("RAT").erro);
            lbl_save_desc.setText(map.get("RAT").desc);
            lbl_save_titulo1.setText(map.get("RAT").tit1);
            return false;
        }

        if (!CalcularRAR()) {
            lbl_save_error.setText(map.get("RAR").erro);
            lbl_save_desc.setText(map.get("RAR").desc);
            lbl_save_titulo1.setText(map.get("RAR").tit1);
            return false;
        }

        if (!CalcularRCP()) {
            lbl_save_error.setText(map.get("RCP").erro);
            lbl_save_desc.setText(map.get("RCP").desc);
            lbl_save_titulo1.setText(map.get("RCP").tit1);
            return false;
        }

        if (!CalcularRCM()) {
            lbl_save_error.setText(map.get("RCM").erro);
            lbl_save_desc.setText(map.get("RCM").desc);
            lbl_save_titulo1.setText(map.get("RCM").tit1);
            return false;
        }

        if (!CalcularRLC()) {
            lbl_save_error.setText(map.get("RLC").erro);
            lbl_save_desc.setText(map.get("RLC").desc);
            lbl_save_titulo1.setText(map.get("RLC").tit1);
            return false;
        }

        if (!CalcularRVM()) {
            lbl_save_error.setText(map.get("RVM").erro);
            lbl_save_desc.setText(map.get("RVM").desc);
            lbl_save_titulo1.setText(map.get("RVM").tit1);
            return false;
        }

        if (!CalcularRVN()) {
            lbl_save_error.setText(map.get("RVN").erro);
            lbl_save_desc.setText(map.get("RVN").desc);
            lbl_save_titulo1.setText(map.get("RVN").tit1);
            return false;
        }

        if (!CalcularRNB()) {
            lbl_save_error.setText(map.get("RNB").erro);
            lbl_save_desc.setText(map.get("RNB").desc);
            lbl_save_titulo1.setText(map.get("RNB").tit1);
            return false;
        }

        if (bod.getVarInt("RNB") <= 2) {//Todo: de la bd
            util.Mensaje("Número de barras (Nb) ≤ 2 barras. Se recomienda verificar los valores de \"h\" y \"a\",\n o considerar otras opciones de sistemas compactos de tratamiento preliminar", "ok");
        }

        if (!CalcularRNE()) {
            lbl_save_error.setText(map.get("RNE").erro);
            lbl_save_desc.setText(map.get("RNE").desc);
            lbl_save_titulo1.setText(map.get("RNE").tit1);
            return false;
        }

        if (!CalcularRPC()) {
            lbl_save_error.setText(map.get("RPC").erro);
            lbl_save_desc.setText(map.get("RPC").desc);
            lbl_save_titulo1.setText(map.get("RPC").tit1);
            return false;
        }

        if (!CalcularRPS()) {
            lbl_save_error.setText(map.get("RPS").erro);
            lbl_save_desc.setText(map.get("RPS").desc);
            lbl_save_titulo1.setText(map.get("RPS").tit1);
            return false;
        }

        if (!CalcularRPB()) {
            lbl_save_error.setText(map.get("RPB").erro);
            lbl_save_desc.setText(map.get("RPB").desc);
            lbl_save_titulo1.setText(map.get("RPB").tit1);
            return false;
        }

        if (!CalcularRPH()) {
            lbl_save_error.setText(map.get("RPH").erro);
            lbl_save_desc.setText(map.get("RPH").desc);
            lbl_save_titulo1.setText(map.get("RPH").tit1);
            return false;
        }

        bod.EditRejillas = true;
        Main main = (Main) this.getTopLevelAncestor(); // Trata de ir  al contenedor principal 
        main.cancela = false;
        main.rejilladesarena = true;
        main.vbod = this.bod;

        if (!main.ComprobarCambio(5, true)) {//Comprobar cambios que afecten datos posteriores, si este guardar es de una edición.

            if (!main.cancela) {
                bod.EditRejillas = false;
                main.bod.EditRejillas = false;
            }
            main.rejilladesarena = false;
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
    private void AsignarPopupBtn(JButton lbl, String mensaje, String uri, int dx, int dy) {
        Lpopup = new Listener_Popup();
        Lpopup.AgregarMensajePopupBtn(lbl, mensaje, uri, dx, dy);
    }

    private boolean CalcularRPL() {

        try {
            lbl_RPL_error.setText("");

            if (bod.setVarDob(txt_RPL_Pregunta.getText(), "RPL", map.get("RPL").rmin, map.get("RPL").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularRPL " + ex.getMessage());
        }
        lbl_RPL_error.setText(map.get("RPL").erro);
        return false;
    }

    private boolean CalcularRTR() {

        try {
            int x = ddm_RTR_pregunta.getSelectedIndex();

            if (bod.setVarInt(x, "RTR", 0, 1000)) { //Números arbitrarios, indican la cantidad de items del menú
                txt_RNB_Pregunta2.setText(ddm_RTR_pregunta.getSelectedItem().toString());
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularRTR " + ex.getMessage());
        }
        return false;
    }

    private boolean CalcularREB() {

        try {
            lbl_REB_error.setText("");

            String[] items = ddm_RTR_pregunta.getSelectedItem().toString().split(",");
            double x = Double.parseDouble(items[1].substring(0, 5).toLowerCase().replace("x", "").trim());

            if (bod.setVarDob(x, "REB", map.get("REB").rmin, map.get("REB").rmax)) {
                txt_REB_Pregunta.setText(bod.getVarFormateadaPorNombre("REB", map.get("REB").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularREB " + ex.getMessage());
        }
        lbl_REB_error.setText(map.get("REB").erro);
        return false;
    }

    private boolean CalcularREL() {

        try {
            lbl_REL_error.setText("");

            if (bod.setVarInt(txt_REL_Pregunta.getText(), "REL", (int) map.get("REL").rmin, (int) map.get("REL").rmax)) {
                txt_RNE_Pregunta2.setText(txt_REL_Pregunta.getText());
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularREL " + ex.getMessage());
        }
        lbl_REL_error.setText(map.get("REL").erro);
        return false;
    }

    private boolean CalcularRIB() {

        try {
            lbl_RIB_error.setText("");

            if (bod.setVarInt(txt_RIB_Pregunta.getText(), "RIB", (int) map.get("RIB").rmin, (int) map.get("RIB").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularRIB " + ex.getMessage());
        }
        lbl_RIB_error.setText(map.get("RIB").erro);
        return false;
    }

    private void CambiaRangoRIB(int z) {

        if (z == 0) { //Rango de 45-60
            String[] x = rangosRBI[0].split("\\|");
            map.get("RIB").rmin = Integer.parseInt(x[1]);
            map.get("RIB").rmax = Integer.parseInt(x[2]);
        } else { //Rango de 60-90
            String[] x = rangosRBI[1].split("\\|");
            map.get("RIB").rmin = Integer.parseInt(x[1]);
            map.get("RIB").rmax = Integer.parseInt(x[2]);
        }
    }

    private boolean CalcularRBI() {

        try {
            int x;

            if (lbl_RBI_pregunta2.isSelected()) { //Manual = 0, Mecánico = 1 
                x = 1;
            } else if (lbl_RBI_pregunta1.isSelected()) {
                x = 0;
            } else {
                return false;
            }

            CambiaRangoRIB(x);//Según lo elegido ajusta los rangos para RIB

            if (bod.setVarInt(x, "RBI", 0, 1)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularRBI " + ex.getMessage());
        }
        return false;
    }

    private boolean CalcularRPM() {

        try {
            lbl_RPM_error.setText("");

            if (bod.setVarDob(txt_RPM_Pregunta.getText(), "RPM", map.get("RPM").rmin, map.get("RPM").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularRPM " + ex.getMessage());
        }
        lbl_RPM_error.setText(map.get("RPM").erro);
        return false;
    }

    private boolean CalcularRPN() {

        try {
            lbl_RPN_error.setText("");

            if (bod.setVarDob(txt_RPN_Pregunta.getText(), "RPN", map.get("RPN").rmin, map.get("RPN").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularRPN " + ex.getMessage());
        }
        lbl_RPN_error.setText(map.get("RPN").erro);
        return false;
    }

    private boolean CalcularRAU() {

        try {
            lbl_RAU_error.setText("");
            double x = bod.calcularRAU();

            if (bod.setVarDob(x, "RAU", map.get("RAU").rmin, map.get("RAU").rmax)) {
                txt_RAU_Pregunta.setText(bod.getVarFormateadaPorNombre("RAU", map.get("RAU").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularRAU " + ex.getMessage());
        }
        lbl_RAU_error.setText(map.get("RAU").erro);
        return false;
    }

    private boolean CalcularRER() {

        try {
            lbl_RER_error.setText("");
            double x = bod.calcularRER();

            if (bod.setVarDob(x, "RER", map.get("RER").rmin, map.get("RER").rmax)) {
                txt_RER_Pregunta.setText(bod.getVarFormateadaPorNombre("RER", map.get("RER").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularRER " + ex.getMessage());
        }
        lbl_RER_error.setText(map.get("RER").erro);
        return false;
    }

    private boolean CalcularRAT() {

        try {
            lbl_RAT_error.setText("");
            double x = bod.calcularRAT();

            if (bod.setVarDob(x, "RAT", map.get("RAT").rmin, map.get("RAT").rmax)) {
                txt_RAT_Pregunta.setText(bod.getVarFormateadaPorNombre("RAT", map.get("RAT").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularRAT " + ex.getMessage());
        }
        lbl_RAT_error.setText(map.get("RAT").erro);
        return false;
    }

    private boolean CalcularRAR() {

        try {
            lbl_RAR_error.setText("");
            double x = bod.calcularRAR();

            if (bod.setVarDob(x, "RAR", map.get("RAR").rmin, map.get("RAR").rmax)) {
                txt_RAR_Pregunta.setText(bod.getVarFormateadaPorNombre("RAR", map.get("RAR").deci));
                lbl_RCP_titulo3.setText(txt_RAR_Pregunta.getText());
                lbl_RVM_titulo3.setText(txt_RAR_Pregunta.getText());
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularRAR " + ex.getMessage());
        }
        lbl_RAR_error.setText(map.get("RAR").erro);
        return false;
    }

    private boolean CalcularRCP() {

        try {
            lbl_RCP_error.setText("");
            double x = bod.calcularRCP();

            if (bod.setVarDob(x, "RCP", map.get("RCP").rmin, map.get("RCP").rmax)) {
                txt_RCP_Pregunta.setText(bod.getVarFormateadaPorNombre("RCP", map.get("RCP").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularRCP " + ex.getMessage());
        }
        lbl_RCP_error.setText(map.get("RCP").erro);
        return false;
    }

    private boolean CalcularRCM() {

        try {
            lbl_RCM_error.setText("");
            double x = bod.calcularRCM();

            if (bod.setVarDob(x, "RCM", map.get("RCM").rmin, map.get("RCM").rmax)) {
                txt_RCM_Pregunta.setText(bod.getVarFormateadaPorNombre("RCM", map.get("RCM").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularRCM " + ex.getMessage());
        }
        lbl_RCM_error.setText(map.get("RCM").erro);
        return false;
    }

    private boolean CalcularRLC() {

        try {
            lbl_RLC_error.setText("");
            double x = bod.calcularRLC();

            if (bod.setVarDob(x, "RLC", map.get("RLC").rmin, map.get("RLC").rmax)) {
                txt_RLC_Pregunta.setText(bod.getVarFormateadaPorNombre("RLC", map.get("RLC").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularRLC " + ex.getMessage());
        }
        lbl_RLC_error.setText(map.get("RLC").erro);
        return false;
    }

    private boolean CalcularRVM() {

        try {
            lbl_RVM_error.setText("");
            double x = bod.calcularRVM();

            if (bod.setVarDob(x, "RVM", map.get("RVM").rmin, map.get("RVM").rmax)) {
                txt_RVM_Pregunta.setText(bod.getVarFormateadaPorNombre("RVM", map.get("RVM").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularRVM " + ex.getMessage());
        }
        lbl_RVM_error.setText(map.get("RVM").erro);
        return false;
    }

    private boolean CalcularRVN() {

        try {
            lbl_RVN_error.setText("");
            double x = bod.calcularRVN();

            if (bod.setVarDob(x, "RVN", map.get("RVN").rmin, map.get("RVN").rmax)) {
                txt_RVN_Pregunta.setText(bod.getVarFormateadaPorNombre("RVN", map.get("RVN").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularRVN " + ex.getMessage());
        }
        lbl_RVN_error.setText(map.get("RVN").erro);
        return false;
    }

    private boolean CalcularRNB() {

        try {
            lbl_RNB_error.setText("");
            int x = bod.calcularRNB();

            if (x < 1) {
                x = 1;
            }

            if (bod.setVarInt(x, "RNB", (int) map.get("RNB").rmin, (int) map.get("RNB").rmax)) {
                txt_RNB_Pregunta.setText(bod.getVarFormateadaPorNombre("RNB", map.get("RNB").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularRNB " + ex.getMessage());
        }
        lbl_RNB_error.setText(map.get("RNB").erro);
        return false;
    }

    private boolean CalcularRNE() {

        try {
            lbl_RNE_error.setText("");
            double x = bod.calcularRNE();

            if (bod.setVarDob(x, "RNE", map.get("RNE").rmin, map.get("RNE").rmax)) {
                txt_RNE_Pregunta.setText(bod.getVarFormateadaPorNombre("RNE", map.get("RNE").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularRNE " + ex.getMessage());
        }
        lbl_RNE_error.setText(map.get("RNE").erro);
        return false;
    }

    private boolean CalcularRPC() {

        try {
            lbl_RPC_error.setText("");
            double x = bod.calcularRPC();
            txt_RPC_Pregunta.setText(bod.getVarFormateada(x, 4)); //todo: temporal

            if (bod.setVarDob(x, "RPC", map.get("RPC").rmin, map.get("RPC").rmax)) {
                txt_RPC_Pregunta.setText(bod.getVarFormateadaPorNombre("RPC", map.get("RPC").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularRPC " + ex.getMessage());
        }
        lbl_RPC_error.setText(map.get("RPC").erro);
        return false;
    }

    private boolean CalcularRPS() {

        try {
            lbl_RPS_error.setText("");
            double x = bod.calcularRPS();

            if (bod.setVarDob(x, "RPS", map.get("RPS").rmin, map.get("RPS").rmax)) {
                txt_RPS_Pregunta.setText(bod.getVarFormateadaPorNombre("RPS", map.get("RPS").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularRPS " + ex.getMessage());
        }
        lbl_RPS_error.setText(map.get("RPS").erro);
        return false;
    }

    private boolean CalcularRPB() {

        try {
            lbl_RPB_error.setText("");

            if (bod.setVarDob(txt_RPB_Pregunta.getText(), "RPB", map.get("RPB").rmin, map.get("RPB").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularRPB " + ex.getMessage());
        }
        lbl_RPB_error.setText(map.get("RPB").erro);
        return false;
    }

    private boolean CalcularRPH() {

        try {
            lbl_RPH_error.setText("");
            double x = bod.calcularRPH();

            if (bod.setVarDob(x, "RPH", map.get("RPH").rmin, map.get("RPH").rmax)) {
                txt_RPH_Pregunta.setText(bod.getVarFormateadaPorNombre("RPH", map.get("RPH").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularRPH " + ex.getMessage());
        }
        lbl_RPH_error.setText(map.get("RPH").erro);
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
            case "RPL":
                CalcularRPL();
                ejecutarFunciones("RAR");
                break;

            case "RTR":
                CalcularRTR();
                ejecutarFunciones("REB");
                break;

            case "REB":
                CalcularREB();
                ejecutarFunciones("RER");
                ejecutarFunciones("RNB");
                ejecutarFunciones("RPH");
                break;

            case "REL":
                CalcularREL();
                ejecutarFunciones("RER");
                ejecutarFunciones("RNB");
                ejecutarFunciones("RPH");
                break;

            case "RIB":
                CalcularRIB();
                ejecutarFunciones("RPH");
                break;

            case "RBI":
                CalcularRBI();
                ejecutarFunciones("RIB");
                break;

            case "RPM":
                CalcularRPM();
                ejecutarFunciones("RAU");
                break;

            case "RPN":
                CalcularRPN();
                break;

            case "RAU":
                CalcularRAU();
                ejecutarFunciones("RAT");
                break;

            case "RER":
                CalcularRER();
                ejecutarFunciones("RAT");
                ejecutarFunciones("RCP");
                break;

            case "RAT":
                CalcularRAT();
                ejecutarFunciones("RAR");
                ejecutarFunciones("RLC");
                break;

            case "RAR":
                CalcularRAR();
                ejecutarFunciones("RCP");
                ejecutarFunciones("RVM");
                ejecutarFunciones("RVN");
                ejecutarFunciones("RNB");
                break;

            case "RCP":
                CalcularRCP();
                CalcularRCM();
                ejecutarFunciones("RPC");
                ejecutarFunciones("RPH");
                break;

            case "RLC":
                CalcularRLC();
                break;

            case "RVM":
                CalcularRVM();
                ejecutarFunciones("RPC");
                break;

            case "RVN":
                CalcularRVN();
                break;

            case "RNB":
                CalcularRNB();
                CalcularRNE();
                break;

            case "RPC":
                CalcularRPC();
                CalcularRPS();
                break;

            case "RPB":
                CalcularRPB();
                ejecutarFunciones("RPH");
                break;

            case "RPH":
                CalcularRPH();
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
        bod.WinRejillas = false;
        Main main = (Main) this.getTopLevelAncestor();
        main.bod.WinRejillas = false;
        if (esGuia) {
            main.guiaUsuario2();
        }
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jsp_Rejillas = new javax.swing.JScrollPane();
        jp_Componentes = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        lbl_titulo1 = new javax.swing.JLabel();
        btn_guardar = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        txt_Q2C_Pregunta = new javax.swing.JTextField();
        lbl_Q2C_titulo1 = new javax.swing.JLabel();
        lbl_Q2C_titulo2 = new javax.swing.JLabel();
        lbl_Q3C_titulo1 = new javax.swing.JLabel();
        txt_Q3C_Pregunta = new javax.swing.JTextField();
        lbl_Q3C_titulo2 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        btn_close = new javax.swing.JButton();
        lbl_save_error = new javax.swing.JLabel();
        lbl_save_desc = new javax.swing.JLabel();
        lbl_RPL_error = new javax.swing.JLabel();
        lbl_RPL_titulo2 = new javax.swing.JLabel();
        txt_RPL_Pregunta = new javax.swing.JTextField();
        lbl_RPL_titulo1 = new javax.swing.JLabel();
        lbl_RTR_titulo2 = new javax.swing.JLabel();
        lbl_RTR_titulo1 = new javax.swing.JLabel();
        txt_RAR_Pregunta = new javax.swing.JTextField();
        lbl_RAR_titulo1 = new javax.swing.JLabel();
        lbl_RLC_error = new javax.swing.JLabel();
        lbl_RLC_titulo2 = new javax.swing.JLabel();
        lbl_RAR_error = new javax.swing.JLabel();
        txt_RLC_Pregunta = new javax.swing.JTextField();
        lbl_RLC_titulo1 = new javax.swing.JLabel();
        lbl_RAR_titulo2 = new javax.swing.JLabel();
        lbl_REL_titulo1 = new javax.swing.JLabel();
        lbl_REL_titulo2 = new javax.swing.JLabel();
        txt_REL_Pregunta = new javax.swing.JTextField();
        lbl_RIB_titulo1 = new javax.swing.JLabel();
        txt_RIB_Pregunta = new javax.swing.JTextField();
        lbl_RIB_titulo2 = new javax.swing.JLabel();
        lbl_REL_error = new javax.swing.JLabel();
        lbl_RIB_error = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        lbl_RPM_desc = new javax.swing.JLabel();
        lbl_RBI_pregunta1 = new javax.swing.JRadioButton();
        lbl_RBI_pregunta2 = new javax.swing.JRadioButton();
        lbl_RPM_titulo1 = new javax.swing.JLabel();
        lbl_RPM_error = new javax.swing.JLabel();
        lbl_RPM_titulo2 = new javax.swing.JLabel();
        txt_RPM_Pregunta = new javax.swing.JTextField();
        lbl_RPN_titulo1 = new javax.swing.JLabel();
        lbl_RPN_error = new javax.swing.JLabel();
        lbl_RPN_titulo2 = new javax.swing.JLabel();
        txt_RPN_Pregunta = new javax.swing.JTextField();
        lbl_RVM_titulo2 = new javax.swing.JLabel();
        lbl_RVM_error = new javax.swing.JLabel();
        lbl_RVM_titulo1 = new javax.swing.JLabel();
        txt_RVM_Pregunta = new javax.swing.JTextField();
        txt_RER_Pregunta = new javax.swing.JTextField();
        lbl_RAU_titulo1 = new javax.swing.JLabel();
        lbl_RER_error = new javax.swing.JLabel();
        lbl_RER_titulo2 = new javax.swing.JLabel();
        txt_RAU_Pregunta = new javax.swing.JTextField();
        lbl_RER_titulo1 = new javax.swing.JLabel();
        lbl_RAU_error = new javax.swing.JLabel();
        lbl_RAU_titulo2 = new javax.swing.JLabel();
        lbl_RAT_titulo2 = new javax.swing.JLabel();
        lbl_RAT_error = new javax.swing.JLabel();
        lbl_RAT_titulo1 = new javax.swing.JLabel();
        txt_RAT_Pregunta = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        ddm_RTR_pregunta = new javax.swing.JComboBox();
        lbl_REB_error = new javax.swing.JLabel();
        txt_REB_Pregunta = new javax.swing.JTextField();
        lbl_REB_titulo1 = new javax.swing.JLabel();
        lbl_REB_titulo2 = new javax.swing.JLabel();
        lbl_RBI_desc = new javax.swing.JLabel();
        lbl_RCP_desc = new javax.swing.JLabel();
        lbl_RCP_titulo4 = new javax.swing.JLabel();
        lbl_RCP_titulo3 = new javax.swing.JLabel();
        txt_RCP_Pregunta = new javax.swing.JTextField();
        lbl_RCM_titulo1 = new javax.swing.JLabel();
        txt_RCM_Pregunta = new javax.swing.JTextField();
        lbl_RCM_titulo2 = new javax.swing.JLabel();
        lbl_RCM_error = new javax.swing.JLabel();
        lbl_RCP_titulo2 = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        lbl_RCP_error = new javax.swing.JLabel();
        lbl_RCP_titulo1 = new javax.swing.JLabel();
        lbl_RVN_error = new javax.swing.JLabel();
        lbl_RVN_titulo2 = new javax.swing.JLabel();
        txt_RVN_Pregunta = new javax.swing.JTextField();
        lbl_RVN_titulo1 = new javax.swing.JLabel();
        lbl_RVM_desc = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        lbl_RVM_titulo3 = new javax.swing.JLabel();
        lbl_RNB_desc = new javax.swing.JLabel();
        lbl_RNE_titulo1 = new javax.swing.JLabel();
        txt_RNE_Pregunta = new javax.swing.JTextField();
        lbl_RNE_titulo2 = new javax.swing.JLabel();
        lbl_RNE_error = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        lbl_RNB_error = new javax.swing.JLabel();
        lbl_RNB_titulo2 = new javax.swing.JLabel();
        lbl_RNB_titulo1 = new javax.swing.JLabel();
        txt_RNB_Pregunta = new javax.swing.JTextField();
        jSeparator8 = new javax.swing.JSeparator();
        lbl_RPC_error = new javax.swing.JLabel();
        lbl_RPS_titulo2 = new javax.swing.JLabel();
        lbl_RPS_error = new javax.swing.JLabel();
        lbl_RPS_titulo1 = new javax.swing.JLabel();
        txt_RPS_Pregunta = new javax.swing.JTextField();
        lbl_RPC_desc = new javax.swing.JLabel();
        lbl_RPC_titulo2 = new javax.swing.JLabel();
        lbl_RPC_titulo1 = new javax.swing.JLabel();
        txt_RPC_Pregunta = new javax.swing.JTextField();
        lbl_RPH_titulo1 = new javax.swing.JLabel();
        txt_RPH_Pregunta = new javax.swing.JTextField();
        lbl_RPB_desc = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        lbl_RPB_error = new javax.swing.JLabel();
        lbl_RPH_titulo2 = new javax.swing.JLabel();
        lbl_RPH_error = new javax.swing.JLabel();
        lbl_RPB_titulo2 = new javax.swing.JLabel();
        txt_RPB_Pregunta = new javax.swing.JTextField();
        lbl_RPB_titulo1 = new javax.swing.JLabel();
        lbl_RTR_desc = new javax.swing.JLabel();
        lbl_RVM_titulo4 = new javax.swing.JLabel();
        txt_RNB_Pregunta2 = new javax.swing.JTextField();
        txt_RNE_Pregunta2 = new javax.swing.JTextField();
        lbl_RNB_titulo3 = new javax.swing.JLabel();
        lbl_RNE_titulo3 = new javax.swing.JLabel();
        btn_Q2C_ayuda = new javax.swing.JButton();
        lbl_titulo2 = new javax.swing.JLabel();
        btn_RAT_ayuda = new javax.swing.JButton();
        btn_RTR_ayuda = new javax.swing.JButton();
        btn_REB_ayuda = new javax.swing.JButton();
        btn_REL_ayuda = new javax.swing.JButton();
        btn_RBI_ayuda = new javax.swing.JButton();
        btn_RPM_ayuda = new javax.swing.JButton();
        btn_RER_ayuda = new javax.swing.JButton();
        btn_RAR_ayuda = new javax.swing.JButton();
        btn_RAU_ayuda = new javax.swing.JButton();
        btn_RVM_ayuda = new javax.swing.JButton();
        btn_RLC_ayuda = new javax.swing.JButton();
        btn_RNB_ayuda = new javax.swing.JButton();
        btn_RCP_ayuda = new javax.swing.JButton();
        btn_RPC_ayuda = new javax.swing.JButton();
        btn_RPB_ayuda = new javax.swing.JButton();
        lbl_save_titulo1 = new javax.swing.JLabel();
        btn_RNB_ayuda2 = new javax.swing.JButton();
        btn_close2 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1024, 1524));

        jsp_Rejillas.setPreferredSize(new java.awt.Dimension(1024, 1400));

        jp_Componentes.setPreferredSize(new java.awt.Dimension(1024, 1650));
        jp_Componentes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jp_Componentes.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 38, 1061, -1));

        lbl_titulo1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_titulo1.setForeground(new java.awt.Color(51, 51, 51));
        lbl_titulo1.setText("Titulo");
        jp_Componentes.add(lbl_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 300, 30));

        btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Button_Salva_Guardar_21.png"))); // NOI18N
        btn_guardar.setText("save");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1450, 120, -1));
        jp_Componentes.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 127, 1061, -1));

        txt_Q2C_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q2C_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 50, 130, 25));

        lbl_Q2C_titulo1.setText("Titulo");
        lbl_Q2C_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2C_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 47, 300, 30));

        lbl_Q2C_titulo2.setText("Titulo");
        lbl_Q2C_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2C_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 60, 80, 25));

        lbl_Q3C_titulo1.setText("Titulo");
        lbl_Q3C_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q3C_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 92, 300, 30));

        txt_Q3C_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q3C_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 90, 130, 25));

        lbl_Q3C_titulo2.setText("Titulo");
        lbl_Q3C_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q3C_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 90, 80, 25));
        jp_Componentes.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 412, 1071, 2));

        btn_close.setText("Salir");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1490, 120, 30));

        lbl_save_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error.setText(" . . .");
        jp_Componentes.add(lbl_save_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 1450, 500, 35));

        lbl_save_desc.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_desc.setText(" . . .");
        jp_Componentes.add(lbl_save_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 1490, 610, 35));

        lbl_RPL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RPL_error.setText(" . . .");
        jp_Componentes.add(lbl_RPL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 130, 340, 35));

        lbl_RPL_titulo2.setText("Titulo");
        lbl_RPL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 140, 80, 25));

        txt_RPL_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_RPL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 140, 130, 25));

        lbl_RPL_titulo1.setText("Titulo");
        lbl_RPL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 300, 30));

        lbl_RTR_titulo2.setText("Titulo");
        lbl_RTR_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RTR_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 210, 80, 25));

        lbl_RTR_titulo1.setText("Titulo");
        lbl_RTR_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RTR_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 300, 30));

        txt_RAR_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RAR_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 680, 130, 25));

        lbl_RAR_titulo1.setText("Titulo");
        lbl_RAR_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RAR_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 670, 300, 30));

        lbl_RLC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RLC_error.setText(" . . .");
        jp_Componentes.add(lbl_RLC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 850, 340, 35));

        lbl_RLC_titulo2.setText("Titulo");
        lbl_RLC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RLC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 860, 80, 25));

        lbl_RAR_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RAR_error.setText(" . . .");
        jp_Componentes.add(lbl_RAR_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 670, 340, 35));

        txt_RLC_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RLC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 860, 130, 25));

        lbl_RLC_titulo1.setText("Titulo");
        lbl_RLC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RLC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 860, 300, 30));

        lbl_RAR_titulo2.setText("Titulo");
        lbl_RAR_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RAR_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 680, 80, 25));

        lbl_REL_titulo1.setText("Titulo");
        lbl_REL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_REL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 300, 30));

        lbl_REL_titulo2.setText("Titulo");
        lbl_REL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_REL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 290, 80, 25));

        txt_REL_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_REL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 290, 130, 25));

        lbl_RIB_titulo1.setText("Titulo");
        lbl_RIB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RIB_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 300, 30));

        txt_RIB_Pregunta.setEnabled(false);
        jp_Componentes.add(txt_RIB_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 330, 130, 25));

        lbl_RIB_titulo2.setText("Titulo");
        lbl_RIB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RIB_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 330, 80, 25));

        lbl_REL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_REL_error.setText(" . . .");
        jp_Componentes.add(lbl_REL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 280, 340, 35));

        lbl_RIB_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RIB_error.setText(" . . .");
        jp_Componentes.add(lbl_RIB_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 320, 340, 35));
        jp_Componentes.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 543, 1071, 2));

        lbl_RPM_desc.setText("Desc");
        lbl_RPM_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPM_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 420, 610, 30));

        lbl_RBI_pregunta1.setText("Titulo");
        jp_Componentes.add(lbl_RBI_pregunta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 370, 80, 25));

        lbl_RBI_pregunta2.setText("Titulo");
        jp_Componentes.add(lbl_RBI_pregunta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 370, 80, 25));

        lbl_RPM_titulo1.setText("Titulo");
        lbl_RPM_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPM_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 300, 30));

        lbl_RPM_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RPM_error.setText(" . . .");
        jp_Componentes.add(lbl_RPM_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 460, 340, 35));

        lbl_RPM_titulo2.setText("Titulo");
        lbl_RPM_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPM_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 460, 80, 25));

        txt_RPM_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_RPM_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 460, 130, 25));

        lbl_RPN_titulo1.setText("Titulo");
        lbl_RPN_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPN_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 500, 300, 30));

        lbl_RPN_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RPN_error.setText(" . . .");
        jp_Componentes.add(lbl_RPN_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 500, 340, 35));

        lbl_RPN_titulo2.setText("Titulo");
        lbl_RPN_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPN_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 500, 80, 25));

        txt_RPN_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_RPN_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 500, 130, 25));

        lbl_RVM_titulo2.setText("Titulo");
        lbl_RVM_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RVM_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 970, 80, 25));

        lbl_RVM_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RVM_error.setText(" . . .");
        jp_Componentes.add(lbl_RVM_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 960, 340, 35));

        lbl_RVM_titulo1.setText("Titulo");
        lbl_RVM_titulo1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lbl_RVM_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RVM_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 970, 300, 30));

        txt_RVM_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RVM_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 970, 130, 25));

        txt_RER_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RER_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 600, 130, 25));

        lbl_RAU_titulo1.setText("Titulo");
        lbl_RAU_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RAU_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 550, 300, 30));

        lbl_RER_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RER_error.setText(" . . .");
        jp_Componentes.add(lbl_RER_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 590, 340, 35));

        lbl_RER_titulo2.setText("Titulo");
        lbl_RER_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RER_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 600, 80, 25));

        txt_RAU_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RAU_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 560, 130, 25));

        lbl_RER_titulo1.setText("Titulo");
        lbl_RER_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RER_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 590, 300, 30));

        lbl_RAU_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RAU_error.setText(" . . .");
        jp_Componentes.add(lbl_RAU_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 550, 340, 35));

        lbl_RAU_titulo2.setText("Titulo");
        lbl_RAU_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RAU_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 560, 80, 25));

        lbl_RAT_titulo2.setText("Titulo");
        lbl_RAT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RAT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 640, 80, 25));

        lbl_RAT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RAT_error.setText(" . . .");
        jp_Componentes.add(lbl_RAT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 630, 340, 35));

        lbl_RAT_titulo1.setText("Titulo");
        lbl_RAT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RAT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 630, 300, 30));

        txt_RAT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RAT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 640, 130, 25));
        jp_Componentes.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1050, 1051, 2));

        jp_Componentes.add(ddm_RTR_pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 210, 130, 25));

        lbl_REB_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_REB_error.setText(" . . .");
        jp_Componentes.add(lbl_REB_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 240, 340, 35));

        txt_REB_Pregunta.setEditable(false);
        jp_Componentes.add(txt_REB_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 250, 130, 25));

        lbl_REB_titulo1.setText("Titulo");
        lbl_REB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_REB_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 300, 30));

        lbl_REB_titulo2.setText("Titulo");
        lbl_REB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_REB_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 250, 80, 25));

        lbl_RBI_desc.setText("Titulo");
        lbl_RBI_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RBI_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 300, 30));

        lbl_RCP_desc.setText("Desc");
        lbl_RCP_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RCP_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 730, 387, 30));

        lbl_RCP_titulo4.setText("Titulo");
        lbl_RCP_titulo4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RCP_titulo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 730, 80, 25));

        lbl_RCP_titulo3.setText("Titulo");
        lbl_RCP_titulo3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RCP_titulo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 730, 80, 25));

        txt_RCP_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RCP_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 780, 130, 25));

        lbl_RCM_titulo1.setText("Titulo");
        lbl_RCM_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RCM_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 820, 300, 30));

        txt_RCM_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RCM_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 820, 130, 25));

        lbl_RCM_titulo2.setText("Titulo");
        lbl_RCM_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RCM_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 820, 80, 25));

        lbl_RCM_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RCM_error.setText(" . . .");
        jp_Componentes.add(lbl_RCM_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 810, 340, 35));

        lbl_RCP_titulo2.setText("Titulo");
        lbl_RCP_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RCP_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 780, 80, 25));
        jp_Componentes.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 902, 1061, 2));

        lbl_RCP_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RCP_error.setText(" . . .");
        jp_Componentes.add(lbl_RCP_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 770, 340, 35));

        lbl_RCP_titulo1.setText("Titulo");
        lbl_RCP_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RCP_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 770, 300, 30));

        lbl_RVN_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RVN_error.setText(" . . .");
        jp_Componentes.add(lbl_RVN_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1000, 340, 35));

        lbl_RVN_titulo2.setText("Titulo");
        lbl_RVN_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RVN_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1010, 80, 25));

        txt_RVN_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RVN_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1010, 130, 25));

        lbl_RVN_titulo1.setText("Titulo");
        lbl_RVN_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RVN_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1010, 300, 30));

        lbl_RVM_desc.setText("Desc");
        lbl_RVM_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RVM_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 910, 386, 50));
        jp_Componentes.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 722, 1061, 2));

        lbl_RVM_titulo3.setText("Titulo");
        lbl_RVM_titulo3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RVM_titulo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 910, 80, 25));

        lbl_RNB_desc.setText("Desc");
        lbl_RNB_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RNB_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1060, 610, 30));

        lbl_RNE_titulo1.setText("Titulo");
        lbl_RNE_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RNE_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1130, 250, 30));

        txt_RNE_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RNE_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 1140, 130, 25));

        lbl_RNE_titulo2.setText("Titulo");
        lbl_RNE_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RNE_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 1140, 80, 25));

        lbl_RNE_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RNE_error.setText(" . . .");
        jp_Componentes.add(lbl_RNE_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 1130, 270, 35));
        jp_Componentes.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1180, 1061, 2));

        lbl_RNB_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RNB_error.setText(" . . .");
        jp_Componentes.add(lbl_RNB_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 1080, 270, 35));

        lbl_RNB_titulo2.setText("Titulo");
        lbl_RNB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RNB_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 1090, 80, 25));

        lbl_RNB_titulo1.setText("Titulo");
        lbl_RNB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RNB_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1090, 230, 30));

        txt_RNB_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RNB_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 1090, 130, 25));
        jp_Componentes.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1310, 1061, 2));

        lbl_RPC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RPC_error.setText(" . . .");
        jp_Componentes.add(lbl_RPC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1220, 340, 35));

        lbl_RPS_titulo2.setText("Titulo");
        lbl_RPS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1270, 80, 25));

        lbl_RPS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RPS_error.setText(" . . .");
        jp_Componentes.add(lbl_RPS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1260, 340, 35));

        lbl_RPS_titulo1.setText("Titulo");
        lbl_RPS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1270, 300, 30));

        txt_RPS_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RPS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1270, 130, 25));

        lbl_RPC_desc.setText("Desc");
        lbl_RPC_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPC_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1190, 610, 30));

        lbl_RPC_titulo2.setText("Titulo");
        lbl_RPC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1230, 80, 25));

        lbl_RPC_titulo1.setText("Titulo");
        lbl_RPC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1230, 300, 30));

        txt_RPC_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RPC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1230, 130, 25));

        lbl_RPH_titulo1.setText("Titulo");
        lbl_RPH_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPH_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1390, 300, 30));

        txt_RPH_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RPH_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1390, 130, 25));

        lbl_RPB_desc.setText("Desc");
        lbl_RPB_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPB_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1320, 610, 30));
        jp_Componentes.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1430, 1061, 2));

        lbl_RPB_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RPB_error.setText(" . . .");
        jp_Componentes.add(lbl_RPB_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1350, 340, 35));

        lbl_RPH_titulo2.setText("Titulo");
        lbl_RPH_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPH_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1390, 80, 25));

        lbl_RPH_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RPH_error.setText(" . . .");
        jp_Componentes.add(lbl_RPH_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1390, 340, 35));

        lbl_RPB_titulo2.setText("Titulo");
        lbl_RPB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPB_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1350, 80, 25));

        txt_RPB_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_RPB_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1350, 130, 25));

        lbl_RPB_titulo1.setText("Titulo");
        lbl_RPB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPB_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1350, 300, 30));

        lbl_RTR_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_RTR_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_RTR_desc.setText("Desc");
        lbl_RTR_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RTR_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 610, 30));

        lbl_RVM_titulo4.setText("Titulo");
        lbl_RVM_titulo4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RVM_titulo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 910, 80, 25));

        txt_RNB_Pregunta2.setEditable(false);
        jp_Componentes.add(txt_RNB_Pregunta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 1090, 130, 25));

        txt_RNE_Pregunta2.setEditable(false);
        jp_Componentes.add(txt_RNE_Pregunta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 1140, 130, 25));

        lbl_RNB_titulo3.setText("Titulo");
        lbl_RNB_titulo3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RNB_titulo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 1090, 80, 25));

        lbl_RNE_titulo3.setText("Titulo");
        lbl_RNE_titulo3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RNE_titulo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 1140, 80, 25));

        btn_Q2C_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_Q2C_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_Q2C_ayuda.setContentAreaFilled(false);
        btn_Q2C_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_Q2C_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 50, 25, 25));

        lbl_titulo2.setForeground(new java.awt.Color(0, 51, 102));
        lbl_titulo2.setText("Desc");
        jp_Componentes.add(lbl_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 0, 600, 30));

        btn_RAT_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RAT_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RAT_ayuda.setContentAreaFilled(false);
        btn_RAT_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RAT_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 640, 25, 25));

        btn_RTR_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RTR_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RTR_ayuda.setContentAreaFilled(false);
        btn_RTR_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RTR_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 210, 25, 25));

        btn_REB_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_REB_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_REB_ayuda.setContentAreaFilled(false);
        btn_REB_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_REB_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 250, 25, 25));

        btn_REL_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_REL_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_REL_ayuda.setContentAreaFilled(false);
        btn_REL_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_REL_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 290, 25, 25));

        btn_RBI_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RBI_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RBI_ayuda.setContentAreaFilled(false);
        btn_RBI_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RBI_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 330, 25, 25));

        btn_RPM_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RPM_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RPM_ayuda.setContentAreaFilled(false);
        btn_RPM_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RPM_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 420, 25, 25));

        btn_RER_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RER_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RER_ayuda.setContentAreaFilled(false);
        btn_RER_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RER_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 600, 25, 25));

        btn_RAR_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RAR_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RAR_ayuda.setContentAreaFilled(false);
        btn_RAR_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RAR_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 680, 25, 25));

        btn_RAU_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RAU_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RAU_ayuda.setContentAreaFilled(false);
        btn_RAU_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RAU_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 560, 25, 25));

        btn_RVM_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RVM_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RVM_ayuda.setContentAreaFilled(false);
        btn_RVM_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RVM_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 910, 25, -1));

        btn_RLC_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RLC_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RLC_ayuda.setContentAreaFilled(false);
        btn_RLC_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RLC_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 860, 25, -1));

        btn_RNB_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RNB_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RNB_ayuda.setContentAreaFilled(false);
        btn_RNB_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RNB_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 1060, 25, -1));

        btn_RCP_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RCP_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RCP_ayuda.setContentAreaFilled(false);
        btn_RCP_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RCP_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 780, 25, -1));

        btn_RPC_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RPC_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RPC_ayuda.setContentAreaFilled(false);
        btn_RPC_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RPC_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 1190, 25, -1));

        btn_RPB_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RPB_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RPB_ayuda.setContentAreaFilled(false);
        btn_RPB_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RPB_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 1320, 25, -1));

        lbl_save_titulo1.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_titulo1.setText(" . . .");
        jp_Componentes.add(lbl_save_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 1450, 300, 35));

        btn_RNB_ayuda2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RNB_ayuda2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RNB_ayuda2.setContentAreaFilled(false);
        btn_RNB_ayuda2.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RNB_ayuda2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 1090, 25, -1));

        jsp_Rejillas.setViewportView(jp_Componentes);

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
            .addComponent(jsp_Rejillas, javax.swing.GroupLayout.PREFERRED_SIZE, 1008, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_close2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btn_close2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jsp_Rejillas, javax.swing.GroupLayout.DEFAULT_SIZE, 1554, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_closeActionPerformed
        cerrar();
    }//GEN-LAST:event_btn_closeActionPerformed

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

    private void btn_close2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_close2ActionPerformed
        cerrar();
    }//GEN-LAST:event_btn_close2ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Q2C_ayuda;
    private javax.swing.JButton btn_RAR_ayuda;
    private javax.swing.JButton btn_RAT_ayuda;
    private javax.swing.JButton btn_RAU_ayuda;
    private javax.swing.JButton btn_RBI_ayuda;
    private javax.swing.JButton btn_RCP_ayuda;
    private javax.swing.JButton btn_REB_ayuda;
    private javax.swing.JButton btn_REL_ayuda;
    private javax.swing.JButton btn_RER_ayuda;
    private javax.swing.JButton btn_RLC_ayuda;
    private javax.swing.JButton btn_RNB_ayuda;
    private javax.swing.JButton btn_RNB_ayuda2;
    private javax.swing.JButton btn_RPB_ayuda;
    private javax.swing.JButton btn_RPC_ayuda;
    private javax.swing.JButton btn_RPM_ayuda;
    private javax.swing.JButton btn_RTR_ayuda;
    private javax.swing.JButton btn_RVM_ayuda;
    private javax.swing.JButton btn_close;
    private javax.swing.JButton btn_close2;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JComboBox ddm_RTR_pregunta;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JPanel jp_Componentes;
    private javax.swing.JScrollPane jsp_Rejillas;
    private javax.swing.JLabel lbl_Q2C_titulo1;
    private javax.swing.JLabel lbl_Q2C_titulo2;
    private javax.swing.JLabel lbl_Q3C_titulo1;
    private javax.swing.JLabel lbl_Q3C_titulo2;
    private javax.swing.JLabel lbl_RAR_error;
    private javax.swing.JLabel lbl_RAR_titulo1;
    private javax.swing.JLabel lbl_RAR_titulo2;
    private javax.swing.JLabel lbl_RAT_error;
    private javax.swing.JLabel lbl_RAT_titulo1;
    private javax.swing.JLabel lbl_RAT_titulo2;
    private javax.swing.JLabel lbl_RAU_error;
    private javax.swing.JLabel lbl_RAU_titulo1;
    private javax.swing.JLabel lbl_RAU_titulo2;
    private javax.swing.JLabel lbl_RBI_desc;
    private javax.swing.JRadioButton lbl_RBI_pregunta1;
    private javax.swing.JRadioButton lbl_RBI_pregunta2;
    private javax.swing.JLabel lbl_RCM_error;
    private javax.swing.JLabel lbl_RCM_titulo1;
    private javax.swing.JLabel lbl_RCM_titulo2;
    private javax.swing.JLabel lbl_RCP_desc;
    private javax.swing.JLabel lbl_RCP_error;
    private javax.swing.JLabel lbl_RCP_titulo1;
    private javax.swing.JLabel lbl_RCP_titulo2;
    private javax.swing.JLabel lbl_RCP_titulo3;
    private javax.swing.JLabel lbl_RCP_titulo4;
    private javax.swing.JLabel lbl_REB_error;
    private javax.swing.JLabel lbl_REB_titulo1;
    private javax.swing.JLabel lbl_REB_titulo2;
    private javax.swing.JLabel lbl_REL_error;
    private javax.swing.JLabel lbl_REL_titulo1;
    private javax.swing.JLabel lbl_REL_titulo2;
    private javax.swing.JLabel lbl_RER_error;
    private javax.swing.JLabel lbl_RER_titulo1;
    private javax.swing.JLabel lbl_RER_titulo2;
    private javax.swing.JLabel lbl_RIB_error;
    private javax.swing.JLabel lbl_RIB_titulo1;
    private javax.swing.JLabel lbl_RIB_titulo2;
    private javax.swing.JLabel lbl_RLC_error;
    private javax.swing.JLabel lbl_RLC_titulo1;
    private javax.swing.JLabel lbl_RLC_titulo2;
    private javax.swing.JLabel lbl_RNB_desc;
    private javax.swing.JLabel lbl_RNB_error;
    private javax.swing.JLabel lbl_RNB_titulo1;
    private javax.swing.JLabel lbl_RNB_titulo2;
    private javax.swing.JLabel lbl_RNB_titulo3;
    private javax.swing.JLabel lbl_RNE_error;
    private javax.swing.JLabel lbl_RNE_titulo1;
    private javax.swing.JLabel lbl_RNE_titulo2;
    private javax.swing.JLabel lbl_RNE_titulo3;
    private javax.swing.JLabel lbl_RPB_desc;
    private javax.swing.JLabel lbl_RPB_error;
    private javax.swing.JLabel lbl_RPB_titulo1;
    private javax.swing.JLabel lbl_RPB_titulo2;
    private javax.swing.JLabel lbl_RPC_desc;
    private javax.swing.JLabel lbl_RPC_error;
    private javax.swing.JLabel lbl_RPC_titulo1;
    private javax.swing.JLabel lbl_RPC_titulo2;
    private javax.swing.JLabel lbl_RPH_error;
    private javax.swing.JLabel lbl_RPH_titulo1;
    private javax.swing.JLabel lbl_RPH_titulo2;
    private javax.swing.JLabel lbl_RPL_error;
    private javax.swing.JLabel lbl_RPL_titulo1;
    private javax.swing.JLabel lbl_RPL_titulo2;
    private javax.swing.JLabel lbl_RPM_desc;
    private javax.swing.JLabel lbl_RPM_error;
    private javax.swing.JLabel lbl_RPM_titulo1;
    private javax.swing.JLabel lbl_RPM_titulo2;
    private javax.swing.JLabel lbl_RPN_error;
    private javax.swing.JLabel lbl_RPN_titulo1;
    private javax.swing.JLabel lbl_RPN_titulo2;
    private javax.swing.JLabel lbl_RPS_error;
    private javax.swing.JLabel lbl_RPS_titulo1;
    private javax.swing.JLabel lbl_RPS_titulo2;
    private javax.swing.JLabel lbl_RTR_desc;
    private javax.swing.JLabel lbl_RTR_titulo1;
    private javax.swing.JLabel lbl_RTR_titulo2;
    private javax.swing.JLabel lbl_RVM_desc;
    private javax.swing.JLabel lbl_RVM_error;
    private javax.swing.JLabel lbl_RVM_titulo1;
    private javax.swing.JLabel lbl_RVM_titulo2;
    private javax.swing.JLabel lbl_RVM_titulo3;
    private javax.swing.JLabel lbl_RVM_titulo4;
    private javax.swing.JLabel lbl_RVN_error;
    private javax.swing.JLabel lbl_RVN_titulo1;
    private javax.swing.JLabel lbl_RVN_titulo2;
    private javax.swing.JLabel lbl_save_desc;
    private javax.swing.JLabel lbl_save_error;
    private javax.swing.JLabel lbl_save_titulo1;
    private javax.swing.JLabel lbl_titulo1;
    private javax.swing.JLabel lbl_titulo2;
    private javax.swing.JTextField txt_Q2C_Pregunta;
    private javax.swing.JTextField txt_Q3C_Pregunta;
    private javax.swing.JTextField txt_RAR_Pregunta;
    private javax.swing.JTextField txt_RAT_Pregunta;
    private javax.swing.JTextField txt_RAU_Pregunta;
    private javax.swing.JTextField txt_RCM_Pregunta;
    private javax.swing.JTextField txt_RCP_Pregunta;
    private javax.swing.JTextField txt_REB_Pregunta;
    private javax.swing.JTextField txt_REL_Pregunta;
    private javax.swing.JTextField txt_RER_Pregunta;
    private javax.swing.JTextField txt_RIB_Pregunta;
    private javax.swing.JTextField txt_RLC_Pregunta;
    private javax.swing.JTextField txt_RNB_Pregunta;
    private javax.swing.JTextField txt_RNB_Pregunta2;
    private javax.swing.JTextField txt_RNE_Pregunta;
    private javax.swing.JTextField txt_RNE_Pregunta2;
    private javax.swing.JTextField txt_RPB_Pregunta;
    private javax.swing.JTextField txt_RPC_Pregunta;
    private javax.swing.JTextField txt_RPH_Pregunta;
    private javax.swing.JTextField txt_RPL_Pregunta;
    private javax.swing.JTextField txt_RPM_Pregunta;
    private javax.swing.JTextField txt_RPN_Pregunta;
    private javax.swing.JTextField txt_RPS_Pregunta;
    private javax.swing.JTextField txt_RVM_Pregunta;
    private javax.swing.JTextField txt_RVN_Pregunta;
    // End of variables declaration//GEN-END:variables
}
