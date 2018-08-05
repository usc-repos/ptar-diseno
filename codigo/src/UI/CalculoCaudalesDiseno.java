package UI;

import BO.Bod;
import DB.Dao;
import Componentes.Util;
import org.apache.log4j.Logger;
import Componentes.Configuracion;
import Componentes.Listener_Popup;
import Componentes.Pregunta2;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class CalculoCaudalesDiseno extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("DatosEntrada"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    public Map<String, Pregunta2> map = new TreeMap<>();
    private Bod bod = new Bod("");
    private Listener_Popup Lpopup;
    Util util = new Util();
    Pregunta2 pg;
    ButtonGroup buttongroup1 = new ButtonGroup();
    ButtonGroup buttongroup3 = new ButtonGroup();
    ButtonGroup btnGrupQCD = new ButtonGroup();
    private boolean eSave = true; //solo para advertir perdida de datos al cerrar la ventana
    private boolean esGuia = false; //para saber si hubo un guardado, se usa al cerrar esta ventana...

    public CalculoCaudalesDiseno(Bod bod) {
        this.bod = bod;
        initComponents();
        bod.WinCalculoCaudalesDiseno = true;//Bandera La ventana se ha abierto
        InicializarComponentes();
    }

    /**
     * Inicializa todos los elementos (jlabels,jtextbox,etc.) con sus
     * respectivos valores y textos desde al base de datos.
     */
    public void InicializarComponentes() {

        ResultSet rs;

        try {
            btn_guardar.setText("Guardar");
            btn_close.setText("Cerrar");

            // - - - - - - Cargar el titulo de la sección  
            rs = db.ResultadoSelect("obtenerseccion", "3");

            lbl_titulo1.setText(rs.getString("Nombre"));
            lbl_titulo2.setText(rs.getString("Mensaje"));
            // - - - - - - Pregunta 1  - - - - - - - - - - - - - - - - - - - -  - - - - - - - - - - - CAR
            rs = db.ResultadoSelect("datospregunta", "CAR");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("CAR", pg);

            lbl_CAR_titulo1.setText(pg.tit1);
            lbl_CAR_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_CAR_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 480, 320);
            // - - - - - - Pregunta 2 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - - - KAR
            rs = db.ResultadoSelect("datospregunta", "KAR");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("KAR", pg);

            lbl_KAR_titulo1.setText(pg.tit1);
            lbl_KAR_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 3 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QIF         
            rs = db.ResultadoSelect("datospregunta", "QIF");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("QIF", pg);

            rbtn_QIF_Pregunta1.setText(pg.tit1);
            rbtn_QIF_Pregunta2.setText(pg.tit2);
            lbl_QIF_desc.setText(pg.desc);

            buttongroup1.add(rbtn_QIF_Pregunta1);
            buttongroup1.add(rbtn_QIF_Pregunta2);

            rbtn_QIF_Pregunta1.addActionListener(new ActionListener() { //Listener para el radiobutton
                @Override
                public void actionPerformed(ActionEvent e) {
                    ejecutarFunciones("QIF");
                }
            });

            rbtn_QIF_Pregunta2.addActionListener(new ActionListener() { //Listener para el radiobutton
                @Override
                public void actionPerformed(ActionEvent e) {
                    ejecutarFunciones("QIF");
                }
            });

            AsignarPopupBtn(btn_QIF_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 550, 370);
            // - - - - - - Pregunta 5 (Opción 1) - - - - - - - - - - - - - - - - - - - - - - - - - - - - QEL
            rs = db.ResultadoSelect("datospregunta", "QEL");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("QEL", pg);

            txt_QEL_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("QEL");
                }
            });
            // - - - - - - Pregunta 7 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QET
            rs = db.ResultadoSelect("datospregunta", "QET");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("QET", pg);

            txt_QET_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_QET_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("QET");
                }
            });

            AsignarPopupBtn(btn_QET_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 250);
            // - - - - - - Pregunta 9 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QEC
            rs = db.ResultadoSelect("datospregunta", "QEC");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("QEC", pg);

            // txt_QEC_Pregunta.setClickable(true);
            txt_QEC_Pregunta.setToolTipText("500-8000");
            // - - - - - - Pregunta 11 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QEK
            rs = db.ResultadoSelect("datospregunta", "QEK");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("QEK", pg);
            // - - - - - - Pregunta 6 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QAA
            rs = db.ResultadoSelect("datospregunta", "QAA");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("QAA", pg);

            txt_QAA_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("QAA");
                }
            });
            // - - - - - - Pregunta 8 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QAI
            rs = db.ResultadoSelect("datospregunta", "QAI");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("QAI", pg);

            txt_QAI_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_QAI_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("QAI");
                }
            });

            AsignarPopupBtn(btn_QAI_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 530, 450);
            // - - - - - - Pregunta 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QAC
            rs = db.ResultadoSelect("datospregunta", "QAC");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("QAC", pg);
            // - - - - - - Pregunta 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QAK
            rs = db.ResultadoSelect("datospregunta", "QAK");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("QAK", pg);
            // - - - - - - Pregunta 1X - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QCD
            rs = db.ResultadoSelect("datospregunta", "QCD");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("QCD", pg);

            btnGrupQCD.add(rbtn_QCD_Pregunta1);
            btnGrupQCD.add(rbtn_QCD_Pregunta2);
            rbtn_QCD_Pregunta1.setText(pg.tit1);
            rbtn_QCD_Pregunta2.setText(pg.tit2);
            lbl_QCD_desc.setText(pg.desc);

            rbtn_QCD_Pregunta1.addActionListener(new ActionListener() { //Listener para el radiobutton SI
                @Override
                public void actionPerformed(ActionEvent e) {
                    ejecutarFunciones("QCD");
                }
            });
            rbtn_QCD_Pregunta2.addActionListener(new ActionListener() { //Listener para el radiobutton NO
                @Override
                public void actionPerformed(ActionEvent e) {
                    ejecutarFunciones("QCD");
                }
            });
            // - - - - - - Pregunta 14 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QCA
            rs = db.ResultadoSelect("datospregunta", "QCA");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("QCA", pg);

            lbl_QCA_titulo1.setText(pg.tit1);
            lbl_QCA_titulo2.setText(rs.getString("titulo2"));

            txt_QCA_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("QCA");
                }
            });

            AsignarPopupBtn(btn_QCA_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 480, 220);
            // - - - - - - Pregunta 15 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QCM
            rs = db.ResultadoSelect("datospregunta", "QCM");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("QCM", pg);

            lbl_QCM_titulo1.setText(pg.tit1);
            lbl_QCM_titulo2.setText(rs.getString("titulo2"));
            txt_QCM_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_QCM_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("QCM");
                }
            });
            // - - - - - - Pregunta 16 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QCC
            rs = db.ResultadoSelect("datospregunta", "QCC");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("QCC", pg);

            lbl_QCC_titulo1.setText(pg.tit1);
            lbl_QCC_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 17 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QCK
            rs = db.ResultadoSelect("datospregunta", "QCK");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("QCK", pg);

            lbl_QCK_titulo1.setText(pg.tit1);
            lbl_QCK_titulo2.setText(rs.getString("titulo2"));

            // - - - - - - Pregunta 22 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - IOA
            rs = db.ResultadoSelect("datospregunta", "IOA");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("IOA", pg);

            chbx_IOA_Pregunta.setText(pg.tit1);
            lbl_IOA_desc.setText(pg.desc);

            chbx_IOA_Pregunta.addItemListener(new ItemListener() { //Listener para el checkbox 
                @Override
                public void itemStateChanged(ItemEvent e) {
                    ejecutarFunciones("IOA");
                }
            });

            AsignarPopupBtn(btn_IOA_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 490, 460);
            // - - - - - - Pregunta 22 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - IRM  
            rs = db.ResultadoSelect("datospregunta", "IRM");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("IRM", pg);

            rbtn_IRM_Pregunta1.setText(pg.tit1);
            rbtn_IRM_Pregunta2.setText(pg.tit2);
            lbl_IRM_desc.setText(pg.desc);

            buttongroup3.add(rbtn_IRM_Pregunta1);
            buttongroup3.add(rbtn_IRM_Pregunta2);


            rbtn_IRM_Pregunta1.addActionListener(new ActionListener() { //Listener para el radiobutton // Si!
                @Override
                public void actionPerformed(ActionEvent e) {
                    ejecutarFunciones("IRM");//OtrosAportesIndustrial();
                }
            });
            rbtn_IRM_Pregunta2.addActionListener(new ActionListener() { //Listener para el radiobutton //No!
                @Override
                public void actionPerformed(ActionEvent e) {
                    ejecutarFunciones("IRM");
                }
            });
            // - - - - - - Pregunta 23 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - IAI
            rs = db.ResultadoSelect("datospregunta", "IAI");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("IAI", pg);

            lbl_IAI_IQ2_titulo1.setText(pg.tit1);
            lbl_IAI_IQ2_titulo2.setText(rs.getString("titulo2"));

            txt_IAI_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("IAI");
                }
            });
            // - - - - - - Pregunta 24 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - IPI
            rs = db.ResultadoSelect("datospregunta", "IPI");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("IPI", pg);

            lbl_IPI_IK2_titulo1.setText(pg.tit1);
            lbl_IPI_IK2_titulo2.setText(rs.getString("titulo2"));

            txt_IPI_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("IPI");
                }
            });

            AsignarPopupBtn(btn_IPI_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 450, 340);
            // - - - - - - Pregunta 25 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - IQI
            rs = db.ResultadoSelect("datospregunta", "IQI");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("IQI", pg);

            lbl_IQI_titulo1.setText(pg.tit1);
            lbl_IQI_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 26 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - IKI
            rs = db.ResultadoSelect("datospregunta", "IKI");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("IKI", pg);

            lbl_IKI_titulo1.setText(pg.tit1);
            lbl_IKI_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 27 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - IQ2
            rs = db.ResultadoSelect("datospregunta", "IQ2");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("IQ2", pg);

            lbl_IAI_IQ2_titulo1.setText(pg.tit1);
            lbl_IAI_IQ2_titulo2.setText(rs.getString("titulo2"));

            txt_IQ2_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("IQ2");
                }
            });
            // - - - - - - Pregunta 28 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - IK2
            rs = db.ResultadoSelect("datospregunta", "IK2");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("IK2", pg);

            lbl_IPI_IK2_titulo1.setText(pg.tit1);
            lbl_IPI_IK2_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 35 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - COA
            rs = db.ResultadoSelect("datospregunta", "COA");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("COA", pg);

            chbx_COA_Pregunta.setText(pg.tit1);

            chbx_COA_Pregunta.addItemListener(new ItemListener() { //Listener para el checkbox
                @Override
                public void itemStateChanged(ItemEvent e) {
                    ejecutarFunciones("COA");
                }
            });

            AsignarPopupBtn(btn_COA_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 580, 250);
            // - - - - - - Pregunta 36 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - CAC
            rs = db.ResultadoSelect("datospregunta", "CAC");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("CAC", pg);

            lbl_CAC_titulo1.setText(pg.tit1);
            lbl_CAC_titulo2.setText(rs.getString("titulo2"));

            txt_CAC_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("CAC");
                }
            });

            // - - - - - - Pregunta 37 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - CPC
            rs = db.ResultadoSelect("datospregunta", "CPC");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("CPC", pg);

            lbl_CPC_titulo1.setText(pg.tit1);
            lbl_CPC_titulo2.setText(rs.getString("titulo2"));
            txt_CPC_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_CPC_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("CPC");
                }
            });
            // - - - - - - Pregunta 38 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - CQC
            rs = db.ResultadoSelect("datospregunta", "CQC");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("CQC", pg);

            lbl_CQC_titulo1.setText(pg.tit1);
            lbl_CQC_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 39 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - CKC
            rs = db.ResultadoSelect("datospregunta", "CKC");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("CKC", pg);

            lbl_CKC_titulo1.setText(pg.tit1);
            lbl_CKC_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 40 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - YOA
            rs = db.ResultadoSelect("datospregunta", "YOA");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("YOA", pg);

            chbx_YOA_Pregunta.setText(pg.tit1);

            chbx_YOA_Pregunta.addItemListener(new ItemListener() { //Listener para el checkbox Institucional
                @Override
                public void itemStateChanged(ItemEvent e) {
                    ejecutarFunciones("YOA");
                }
            });

            AsignarPopupBtn(btn_YOA_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 540, 280);
            // - - - - - - Pregunta 41 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - YAI
            rs = db.ResultadoSelect("datospregunta", "YAI");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("YAI", pg);

            lbl_YAI_titulo1.setText(pg.tit1);
            lbl_YAI_titulo2.setText(rs.getString("titulo2"));

            txt_YAI_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("YAI");
                }
            });
            // - - - - - - Pregunta 42 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - YPI
            rs = db.ResultadoSelect("datospregunta", "YPI");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("YPI", pg);

            lbl_YPI_titulo1.setText(pg.tit1);
            lbl_YPI_titulo2.setText(rs.getString("titulo2"));
            txt_YPI_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_YPI_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("YPI");
                }
            });

            AsignarPopupBtn(btn_YPI_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 320, 360);
            // - - - - - - Pregunta 43 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - YQI
            rs = db.ResultadoSelect("datospregunta", "YQI");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("YQI", pg);

            lbl_YQI_titulo1.setText(pg.tit1);
            lbl_YQI_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 44 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - YKI
            rs = db.ResultadoSelect("datospregunta", "YKI");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("YKI", pg);

            lbl_YKI_titulo1.setText(pg.tit1);
            lbl_YKI_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 45 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q2C 
            rs = db.ResultadoSelect("datospregunta", "Q2C");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("Q2C", pg);

            lbl_Q2C_titulo1.setText(pg.tit1);

            String[] titulo2 = rs.getString("titulo2").split("\\|"); //Q2C en el titulo 2 tiene 2 posibles textos

            lbl_Q2C_titulo2.setText(titulo2[0].trim());
            lbl_Q2K_titulo2.setText(titulo2[1].trim());
            lbl_Q2C_desc.setText(pg.desc);

            AsignarPopupBtn(btn_Q2C_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 560, 610);
            // - - - - - - Pregunta 46 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q2K
            rs = db.ResultadoSelect("datospregunta", "Q2K");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("Q2K", pg);

            lbl_Q2K_titulo1.setText(pg.tit1);
            //lbl_Q2K_titulo2.setText(rs.getString("titulo2"));//Arriba se coloca 
            // - - - - - - Pregunta 47 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q3M
            rs = db.ResultadoSelect("datospregunta", "Q3M");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("Q3M", pg);

            lbl_Q3M_titulo1.setText(pg.tit1);
            lbl_Q3M_titulo2.setText(rs.getString("titulo2"));
            txt_Q3M_Pregunta.setText(rs.getString("valorpordefecto"));
            lbl_Q3M_desc.setText(pg.desc);

            AsignarPopupBtn(btn_Q3M_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 550, 490);
            // - - - - - - Pregunta 48 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q3R
            rs = db.ResultadoSelect("datospregunta", "Q3R");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("Q3R", pg);

            AsignarPopupBtn(btn_Q3R_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 720, 550);
            //AsignarPopupBtn(btn_Q3R_ayuda2, rs.getString("ayuda2"), "", 370, 250);
            // - - - - - - Pregunta 49 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q3C
            rs = db.ResultadoSelect("datospregunta", "Q3C");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("Q3C", pg);

            lbl_Q3C_titulo1.setText(pg.tit1);

            String[] titulo3 = rs.getString("titulo2").split("\\|"); //Q3C en el titulo 2 tiene 2 posibles textos

            lbl_Q3C_titulo2.setText(titulo3[0].trim());
            lbl_Q3K_titulo2.setText(titulo3[1].trim());
            // - - - - - - Pregunta 51 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q3K
            rs = db.ResultadoSelect("datospregunta", "Q3K");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("Q3K", pg);

            lbl_Q3K_titulo1.setText(pg.tit1);
            //lbl_Q3K_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 51 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q1H
            rs = db.ResultadoSelect("datospregunta", "Q1H");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("Q1H", pg);

            lbl_Q1H_titulo1.setText(pg.tit1);
            lbl_Q1H_titulo2.setText(rs.getString("titulo2"));
            txt_Q1H_Pregunta.setText(rs.getString("valorpordefecto"));
            lbl_Q1H_desc.setText(pg.desc);

            txt_Q1H_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("Q1H");
                }
            });

            AsignarPopupBtn(btn_Q1H_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 520, 360);
            // - - - - - - Pregunta 52 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q1C
            rs = db.ResultadoSelect("datospregunta", "Q1C");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("Q1C", pg);

            lbl_Q1C_titulo1.setText(pg.tit1);

            String[] titulo1 = rs.getString("titulo2").split("\\|"); //Q3C en el titulo 2 tiene 2 posibles textos

            lbl_Q1C_titulo2.setText(titulo1[0].trim());
            lbl_Q1K_titulo2.setText(titulo1[1].trim());

            AsignarPopupBtn(btn_Q1C_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 280);
            // - - - - - - Pregunta 52 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q1K
            rs = db.ResultadoSelect("datospregunta", "Q1K");

            pg = new Pregunta2();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.tit2 = rs.getString("titulo2");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("Q1K", pg);

            lbl_Q1K_titulo1.setText(pg.tit1);
            // lbl_Q1K_titulo2.setText(rs.getString("titulo2"));

            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
            // Si se ha cargado un proyecto
            // Si Bod cargó datos de Calculo de Caudales de Diseno desde la BD, porque estaba editada, se procede a llenar casillas con datos
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

            if (bod.EditCalculoCaudalesDiseno) {

                txt_CAR_Pregunta.setText(bod.getVarFormateadaPorNombre("CAR", map.get("CAR").deci));
                txt_KAR_Pregunta.setText(bod.getVarFormateadaPorNombre("KAR", map.get("KAR").deci));

                if (bod.getVarInt("QIF") < 1) { //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Caudal de infiltración (Qinf)
                    rbtn_QIF_Pregunta1.setSelected(true);
                    txt_QEL_Pregunta.setText(bod.getVarFormateadaPorNombre("QEL", map.get("QEL").deci));
                    txt_QET_Pregunta.setText(bod.getVarFormateadaPorNombre("QET", map.get("QET").deci));
                    txt_QEC_Pregunta.setText(bod.getVarFormateadaPorNombre("QEC", map.get("QEC").deci));
                    txt_QEK_Pregunta.setText(bod.getVarFormateadaPorNombre("QEK", map.get("QEK").deci));
                    CalcularQAI();//ver *b
                } else {
                    rbtn_QIF_Pregunta2.setSelected(true);
                    txt_QAA_Pregunta.setText(bod.getVarFormateadaPorNombre("QAA", map.get("QAA").deci));
                    txt_QAI_Pregunta.setText(bod.getVarFormateadaPorNombre("QAI", map.get("QAI").deci));
                    txt_QAC_Pregunta.setText(bod.getVarFormateadaPorNombre("QAC", map.get("QAC").deci));
                    txt_QAK_Pregunta.setText(bod.getVarFormateadaPorNombre("QAK", map.get("QAK").deci));
                    CalcularQET();//ver *b
                }
                CalcularQIF();

                if (bod.getVarInt("QCD") < 1) { //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Caudal por conexiones erradas (QCE)
                    rbtn_QCD_Pregunta2.setSelected(true);
                    verCaudalConexionesErradas(false);
                    CalcularQCM();//ver *b
                } else {//Si
                    verCaudalConexionesErradas(true);
                    rbtn_QCD_Pregunta1.setSelected(true);
                    txt_QCA_Pregunta.setText(bod.getVarFormateadaPorNombre("QCA", map.get("QCA").deci));
                    txt_QCM_Pregunta.setText(bod.getVarFormateadaPorNombre("QCM", map.get("QCM").deci));
                    txt_QCC_Pregunta.setText(bod.getVarFormateadaPorNombre("QCC", map.get("QCC").deci));
                    txt_QCK_Pregunta.setText(bod.getVarFormateadaPorNombre("QCK", map.get("QCK").deci));
                }

                if (bod.getVarInt("IOA") > 0) { //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Otros Aportes Industrial

                    chbx_IOA_Pregunta.setSelected(true);

                    if (bod.getVarInt("IRM") < 1) {

                        rbtn_IRM_Pregunta2.setSelected(true); //Opcion 'No' = IAI,IPI,IQI,IKI
                        txt_IAI_Pregunta.setText(bod.getVarFormateadaPorNombre("IAI", map.get("IAI").deci));
                        txt_IPI_Pregunta.setText(bod.getVarFormateadaPorNombre("IPI", map.get("IPI").deci));
                        txt_IQI_Pregunta.setText(bod.getVarFormateadaPorNombre("IQI", map.get("IQI").deci));
                        txt_IKI_Pregunta.setText(bod.getVarFormateadaPorNombre("IKI", map.get("IKI").deci));
                    } else {
                        rbtn_IRM_Pregunta1.setSelected(true);
                        txt_IQ2_Pregunta.setText(bod.getVarFormateadaPorNombre("IQ2", map.get("IQ2").deci));
                        txt_IK2_Pregunta.setText(bod.getVarFormateadaPorNombre("IK2", map.get("IK2").deci));
                    }
                    CalcularIOA();
                } else {
                    verOtrosAportesIndustrial(true, true);//ocultar todo
                }

                if (bod.getVarInt("COA") > 0) { //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Otros Aportes Comercial

                    chbx_COA_Pregunta.setSelected(true);
                    verOtrosAportesComercial(false);
                    txt_CAC_Pregunta.setText(bod.getVarFormateadaPorNombre("CAC", map.get("CAC").deci));
                    txt_CPC_Pregunta.setText(bod.getVarFormateadaPorNombre("CPC", map.get("CPC").deci));
                    txt_CQC_Pregunta.setText(bod.getVarFormateadaPorNombre("CQC", map.get("CQC").deci));
                    txt_CKC_Pregunta.setText(bod.getVarFormateadaPorNombre("CKC", map.get("CKC").deci));
                } else {
                    CalcularCPC();//ver *b
                    chbx_COA_Pregunta.setSelected(false);
                    verOtrosAportesComercial(true); //ocultar todo
                }

                if (bod.getVarInt("YOA") > 0) { //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Otros Aportes Institucional
                    chbx_YOA_Pregunta.setSelected(true);
                    verOtrosAportesInstitucional(false);
                    txt_YAI_Pregunta.setText(bod.getVarFormateadaPorNombre("YAI", map.get("YAI").deci));
                    txt_YPI_Pregunta.setText(bod.getVarFormateadaPorNombre("YPI", map.get("YPI").deci));
                    txt_YQI_Pregunta.setText(bod.getVarFormateadaPorNombre("YQI", map.get("YQI").deci));
                    txt_YKI_Pregunta.setText(bod.getVarFormateadaPorNombre("YKI", map.get("YKI").deci));
                } else {
                    CalcularYPI();//ver *b
                    chbx_YOA_Pregunta.setSelected(false);
                    verOtrosAportesInstitucional(true); //ocultar todo
                }

                txt_Q2C_Pregunta.setText(bod.getVarFormateadaPorNombre("Q2C", map.get("Q2C").deci)); //- - - - - - - Caudal medio diario 
                txt_Q2K_Pregunta.setText(bod.getVarFormateadaPorNombre("Q2K", map.get("Q2K").deci));

                txt_Q3M_Pregunta.setText(bod.getVarFormateadaPorNombre("Q3M", map.get("Q3M").deci)); //- - - - - - - Caudal max diario
                txt_Q3C_Pregunta.setText(bod.getVarFormateadaPorNombre("Q3C", map.get("Q3C").deci));
                txt_Q3K_Pregunta.setText(bod.getVarFormateadaPorNombre("Q3K", map.get("Q3K").deci));

                txt_Q1H_Pregunta.setText(bod.getVarFormateadaPorNombre("Q1H", map.get("Q1H").deci)); //- - - - - - - Caudal min diario
                txt_Q1C_Pregunta.setText(bod.getVarFormateadaPorNombre("Q1C", map.get("Q1C").deci));
                txt_Q1K_Pregunta.setText(bod.getVarFormateadaPorNombre("Q1K", map.get("Q1K").deci));

            } else {//Como no hay datos precargados de la BD, Se procede a hacer otros calculos necesarios que deben mostrarse al cargar la pagina

                CalcularQET();//(*b) las que tienen valores por defecto  en algunos cálculos estan vacías pues no existen en el bod. 
                CalcularQAI();//(si se colocan abajo del else, o arriba en la carga, puede que los valores existentes se sobrescriban o sean mas cortos)
                CalcularQCM();
                CalcularCPC();
                CalcularYPI();

                CalcularQ1H();

                ejecutarFunciones("CAR");
                ejecutarFunciones("KAR");

                txt_QEL_Pregunta.setVisible(false);//- - - - - - - - - - - - - - Ocultar Caudal de Infiltración
                txt_QET_Pregunta.setVisible(false);
                btn_QET_ayuda.setVisible(false);
                txt_QEC_Pregunta.setVisible(false);
                txt_QEK_Pregunta.setVisible(false);

                txt_QAA_Pregunta.setVisible(false);
                txt_QAI_Pregunta.setVisible(false);
                btn_QAI_ayuda.setVisible(false);
                txt_QAC_Pregunta.setVisible(false);
                txt_QAK_Pregunta.setVisible(false);

                lbl_QEL_QAA_error.setVisible(false);
                lbl_QET_QAI_error.setVisible(false);
                lbl_QEC_QAC_error.setVisible(false);
                lbl_QEK_QAK_error.setVisible(false);

                lbl_QEL_QAA_titulo1.setVisible(false);
                lbl_QEL_QAA_titulo2.setVisible(false);
                lbl_QET_QAI_titulo1.setVisible(false);
                lbl_QET_QAI_titulo2.setVisible(false);
                lbl_QEC_QAC_titulo1.setVisible(false);
                lbl_QEC_QAC_titulo2.setVisible(false);
                lbl_QEK_QAK_titulo1.setVisible(false);
                lbl_QEK_QAK_titulo2.setVisible(false);

                verCaudalConexionesErradas(false);
                verOtrosAportesIndustrial(true, true);//ocultar todo
                verOtrosAportesComercial(true); //- - - - - - - - - - - - - - - - Otros Aportes Comercial
                verOtrosAportesInstitucional(true); //- - - - - - - - - - - - - -  Otros Aportes Institucional 

                borradoMsgError();
            }
            CalcularQ3M();
            eSave = true;
        } catch (Exception ex) {
            log.error("Error en InicializarComponentes " + ex.getMessage());
            ErrorGeneral(); // Ha ocurrido algun error en los datos o conexión, se debe revisar log.
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }

    private void borradoMsgError() {
        lbl_CAC_error.setText("");
        lbl_CAR_error.setText("");
        lbl_CKC_error.setText("");
        lbl_CPC_error.setText("");
        lbl_CQC_error.setText("");
        lbl_IAI_IQ2_error.setText("");
        lbl_IKI_error.setText("");
        lbl_IPI_IK2_error.setText("");
        lbl_IQI_error.setText("");
        lbl_KAR_error.setText("");
        lbl_Q1C_error.setText("");
        lbl_Q1H_error.setText("");
        lbl_Q1K_error.setText("");
        lbl_Q2C_error.setText("");
        lbl_Q2K_error.setText("");
        lbl_Q3C_error.setText("");
        lbl_Q3K_error.setText("");
        lbl_Q3M_error.setText("");
        lbl_QCA_error.setText("");
        lbl_QCC_error.setText("");
        lbl_QCD_error.setText("");
        lbl_QCK_error.setText("");
        lbl_QCM_error.setText("");
        lbl_QEC_QAC_error.setText("");
        lbl_QEK_QAK_error.setText("");
        lbl_QEL_QAA_error.setText("");
        lbl_QET_QAI_error.setText("");
        lbl_YAI_error.setText("");
        lbl_YKI_error.setText("");
        lbl_YPI_error.setText("");
        lbl_YQI_error.setText("");
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
     * Comprueba todos los campos por guardar de esta ventana versus la clase de
     * lógica de negocio. Hasta que esten correctos todos se permite guardar.
     *
     * @return
     */
    private boolean Guardar() {

        String escoger = "Debe completar los campos de: \n";
        lbl_save_error.setText("");
        lbl_save_desc.setText("");

        if (!CalcularCAR()) {
            lbl_save_error.setText(map.get("CAR").erro);
            lbl_save_desc.setText(map.get("CAR").desc);
            lbl_save_titulo1.setText(map.get("CAR").tit1);
            return false;
        }

        if (!CalcularKAR()) {
            lbl_save_error.setText(map.get("KAR").erro);
            lbl_save_desc.setText(map.get("KAR").desc);
            lbl_save_titulo1.setText(map.get("KAR").tit1);
            return false;
        }

        if (!CalcularQIF()) {
            lbl_save_error.setText(map.get("QIF").erro);
            lbl_save_desc.setText(map.get("QIF").desc);
            lbl_save_titulo1.setText(map.get("QIF").tit1);
            return false;
        }

        boolean escogerCaudalInfiltrac = false;

        if (rbtn_QIF_Pregunta1.isSelected()) {
            escogerCaudalInfiltrac = true;

            if (!CalcularQEL()) {
                lbl_save_error.setText(map.get("QEL").erro);
                lbl_save_desc.setText(map.get("QEL").desc);
                lbl_save_titulo1.setText(map.get("QEL").tit1);
                return false;
            }


            if (!CalcularQET()) {
                lbl_save_error.setText(map.get("QET").erro);
                lbl_save_desc.setText(map.get("QET").desc);
                lbl_save_titulo1.setText(map.get("QET").tit1);
                return false;
            }

            if (!CalcularQEC()) {
                lbl_save_error.setText(map.get("QEC").erro);
                lbl_save_desc.setText(map.get("QEC").desc);
                lbl_save_titulo1.setText(map.get("QEC").tit1);
                return false;
            }

            if (!CalcularQEK()) {
                lbl_save_error.setText(map.get("QEK").erro);
                lbl_save_desc.setText(map.get("QEK").desc);
                lbl_save_titulo1.setText(map.get("QEK").tit1);
                return false;
            }
        }

        if (rbtn_QIF_Pregunta2.isSelected()) {
            escogerCaudalInfiltrac = true;
            if (!CalcularQAA()) {
                lbl_save_error.setText(map.get("QAA").erro);
                lbl_save_desc.setText(map.get("QAA").desc);
                lbl_save_titulo1.setText(map.get("QAA").tit1);
                return false;
            }

            if (!CalcularQAI()) {
                lbl_save_error.setText(map.get("QAI").erro);
                lbl_save_desc.setText(map.get("QAI").desc);
                lbl_save_titulo1.setText(map.get("QAI").tit1);
                return false;
            }

            if (!CalcularQAC()) {
                lbl_save_error.setText(map.get("QAC").erro);
                lbl_save_desc.setText(map.get("QAC").desc);
                lbl_save_titulo1.setText(map.get("QAC").tit1);
                return false;
            }

            if (!CalcularQAK()) {
                lbl_save_error.setText(map.get("QAK").erro);
                lbl_save_desc.setText(map.get("QAK").desc);
                lbl_save_titulo1.setText(map.get("QAK").tit1);
                return false;
            }
        }

        if (!escogerCaudalInfiltrac) {
            lbl_save_desc.setText(map.get("QIF").desc);
            return false;
        }

        if (!CalcularQCD()) {
            lbl_save_error.setText(map.get("QCD").erro);
            lbl_save_desc.setText(map.get("QCD").desc);
            lbl_save_titulo1.setText(map.get("QCD").tit1);
            return false;
        }

        boolean escogerCaudalConexionesErradas = false;

        if (rbtn_QCD_Pregunta1.isSelected()) {

            escogerCaudalConexionesErradas = true;

            if (!CalcularQCA()) {
                lbl_save_error.setText(map.get("QCA").erro);
                lbl_save_desc.setText(map.get("QCA").desc);
                lbl_save_titulo1.setText(map.get("QCA").tit1);
                return false;
            }

            if (!CalcularQCM()) {
                lbl_save_error.setText(map.get("QCM").erro);
                lbl_save_desc.setText(map.get("QCM").desc);
                lbl_save_titulo1.setText(map.get("QCM").tit1);
                return false;
            }

            if (!CalcularQCC()) {
                lbl_save_error.setText(map.get("QCC").erro);
                lbl_save_desc.setText(map.get("QCC").desc);
                lbl_save_titulo1.setText(map.get("QCC").tit1);
                return false;
            }

            if (!CalcularQCK()) {
                lbl_save_error.setText(map.get("QCK").erro);
                lbl_save_desc.setText(map.get("QCK").desc);
                lbl_save_titulo1.setText(map.get("QCK").tit1);
                return false;
            }
        }

        boolean escogerOtrosAportesIndustrial = false;

        if (!CalcularIOA()) {
            lbl_save_error.setText(map.get("IOA").erro);
            lbl_save_desc.setText(map.get("IOA").desc);
            lbl_save_titulo1.setText(map.get("IOA").tit1);
            return false;
        }

        if (chbx_IOA_Pregunta.isSelected()) {

            if (!rbtn_IRM_Pregunta1.isSelected() && !rbtn_IRM_Pregunta2.isSelected()) {
                lbl_save_error.setText(escoger + map.get("IOA").tit1);
                return false;
            }

            if (!CalcularIRM()) {
                lbl_save_error.setText(map.get("IRM").erro);
                lbl_save_desc.setText(map.get("IRM").desc);
                lbl_save_titulo1.setText(map.get("IRM").tit1);
                return false;
            }

            if (rbtn_IRM_Pregunta2.isSelected()) { //No = 0 = IAI,IPI.....

                escogerOtrosAportesIndustrial = true;

                if (!CalcularIAI()) {
                    lbl_save_error.setText(map.get("IAI").erro);
                    lbl_save_desc.setText(map.get("IAI").desc);
                    lbl_save_titulo1.setText(map.get("IAI").tit1);
                    return false;
                }

                if (!CalcularIPI()) {
                    lbl_save_error.setText(map.get("IPI").erro);
                    lbl_save_desc.setText(map.get("IPI").desc);
                    lbl_save_titulo1.setText(map.get("IPI").tit1);
                    return false;
                }

                if (!CalcularIQI()) {
                    lbl_save_error.setText(map.get("IQI").erro);
                    lbl_save_desc.setText(map.get("IQI").desc);
                    lbl_save_titulo1.setText(map.get("IQI").tit1);
                    return false;
                }

                if (!CalcularIKI()) {
                    lbl_save_error.setText(map.get("IKI").erro);
                    lbl_save_desc.setText(map.get("IKI").desc);
                    lbl_save_titulo1.setText(map.get("IKI").tit1);
                    return false;
                }
            }

            if (rbtn_IRM_Pregunta1.isSelected()) { //Si = 0 = IQ2,IK2.....

                escogerOtrosAportesIndustrial = true;

                if (!CalcularIQ2()) {
                    lbl_save_error.setText(map.get("IQ2").erro);
                    lbl_save_desc.setText(map.get("IQ2").desc);
                    lbl_save_titulo1.setText(map.get("IQ2").tit1);
                    return false;
                }

                if (!CalcularIK2()) {
                    lbl_save_error.setText(map.get("IK2").erro);
                    lbl_save_desc.setText(map.get("IK2").desc);
                    lbl_save_titulo1.setText(map.get("IK2").tit1);
                    return false;
                }
            }
        }

        if (!CalcularCOA()) {
            lbl_save_error.setText(map.get("COA").erro);
            lbl_save_desc.setText(map.get("COA").desc);
            lbl_save_titulo1.setText(map.get("COA").tit1);
            return false;
        }

        boolean escogerOtrosAportesComercial = false;

        if (chbx_COA_Pregunta.isSelected()) {

            escogerOtrosAportesComercial = true;

            if (!CalcularCAC()) {
                lbl_save_error.setText(map.get("CAC").erro);
                lbl_save_desc.setText(map.get("CAC").desc);
                lbl_save_titulo1.setText(map.get("CAC").tit1);
                return false;
            }

            if (!CalcularCPC()) {
                lbl_save_error.setText(map.get("CPC").erro);
                lbl_save_desc.setText(map.get("CPC").desc);
                lbl_save_titulo1.setText(map.get("CPC").tit1);
                return false;
            }

            if (!CalcularCQC()) {
                lbl_save_error.setText(map.get("CQC").erro);
                lbl_save_desc.setText(map.get("CQC").desc);
                lbl_save_titulo1.setText(map.get("CQC").tit1);
                return false;
            }

            if (!CalcularCKC()) {
                lbl_save_error.setText(map.get("CKC").erro);
                lbl_save_desc.setText(map.get("CKC").desc);
                lbl_save_titulo1.setText(map.get("CKC").tit1);
                return false;
            }
        }

        boolean escogerOtrosAportesInstitucional = false;

        if (chbx_YOA_Pregunta.isSelected()) {

            escogerOtrosAportesInstitucional = true;

            if (!CalcularYOA()) {
                lbl_save_error.setText(map.get("YOA").erro);
                lbl_save_desc.setText(map.get("YOA").desc);
                lbl_save_titulo1.setText(map.get("YOA").tit1);
                return false;
            }

            if (!CalcularYAI()) {
                lbl_save_error.setText(map.get("YAI").erro);
                lbl_save_desc.setText(map.get("YAI").desc);
                lbl_save_titulo1.setText(map.get("YAI").tit1);
                return false;
            }

            if (!CalcularYPI()) {
                lbl_save_error.setText(map.get("YPI").erro);
                lbl_save_desc.setText(map.get("YPI").desc);
                lbl_save_titulo1.setText(map.get("YPI").tit1);
                return false;
            }

            if (!CalcularYQI()) {
                lbl_save_error.setText(map.get("YQI").erro);
                lbl_save_desc.setText(map.get("YQI").desc);
                lbl_save_titulo1.setText(map.get("YQI").tit1);
                return false;
            }

            if (!CalcularYKI()) {
                lbl_save_error.setText(map.get("YKI").erro);
                lbl_save_desc.setText(map.get("YKI").desc);
                lbl_save_titulo1.setText(map.get("YKI").tit1);
                return false;
            }
        }

        if (!CalcularQ2C()) {
            lbl_save_error.setText(map.get("Q2C").erro);
            lbl_save_desc.setText(map.get("Q2C").desc);
            lbl_save_titulo1.setText(map.get("Q2C").tit1);
            return false;
        }

        if (!CalcularQ2K()) {
            lbl_save_error.setText(map.get("Q2K").erro);
            lbl_save_desc.setText(map.get("Q2K").desc);
            lbl_save_titulo1.setText(map.get("Q2K").tit1);
            return false;
        }

//        if (!CalcularQ3M()) {
//            lbl_save_error.setText(map.get("Q3M").erro);
//            lbl_save_desc.setText(map.get("Q3M").desc);
//            lbl_save_titulo1.setText(map.get("Q3M").tit1);
//            return false;
//        }
//
//        if (!CalcularQ3R()) {
//            lbl_save_error.setText(map.get("Q3R").erro);
//            lbl_save_desc.setText(map.get("Q3R").desc);
//            lbl_save_titulo1.setText(map.get("Q3R").tit1);
//            return false;
//        }

        if (!CalcularQ3C()) {
            lbl_save_error.setText(map.get("Q3C").erro);
            lbl_save_desc.setText(map.get("Q3C").desc);
            lbl_save_titulo1.setText(map.get("Q3C").tit1);
            return false;
        }

        if (!CalcularQ3K()) {
            lbl_save_error.setText(map.get("Q3K").erro);
            lbl_save_desc.setText(map.get("Q3K").desc);
            lbl_save_titulo1.setText(map.get("Q3K").tit1);
            return false;
        }

        if (!CalcularQ1H()) {
            lbl_save_error.setText(map.get("Q1H").erro);
            lbl_save_desc.setText(map.get("Q1H").desc);
            lbl_save_titulo1.setText(map.get("Q1H").tit1);
            return false;
        }

        if (!CalcularQ1C()) {
            lbl_save_error.setText(map.get("Q1C").erro);
            lbl_save_desc.setText(map.get("Q1C").desc);
            lbl_save_titulo1.setText(map.get("Q1C").tit1);
            return false;
        }

        if (!CalcularQ1K()) {
            lbl_save_error.setText(map.get("Q1K").erro);
            lbl_save_desc.setText(map.get("Q1K").desc);
            lbl_save_titulo1.setText(map.get("Q1K").tit1);
            return false;
        }
        //--------------------------------------------------------------------- Mensajes sobre opcionales no usados

        escoger = "";

        if (!escogerCaudalConexionesErradas) {
            escoger += "* " + map.get("QCA").desc + "\n";
        }
        if (!escogerOtrosAportesIndustrial) {
            escoger += "* " + map.get("IOA").tit1 + "\n";
        }

        if (!escogerOtrosAportesComercial) {
            escoger += "* " + map.get("COA").tit1 + "\n";
        }

        if (!escogerOtrosAportesInstitucional) {
            escoger += "* " + map.get("YOA").tit1 + "\n";
        }

        if (!escoger.isEmpty()) {
            escoger = "¿Desea guardar sin completar los campos de: \n" + escoger + " ?";

            int n = util.Mensaje(escoger, "yesno");

            if (n == JOptionPane.NO_OPTION) {
                return false;
            }
        }
        //---------------------------------------------------------------------

        bod.EditCalculoCaudalesDiseno = true;//Se cambia para tener la posibilidad de ser guardado en true
        Main main = (Main) this.getTopLevelAncestor(); // Trata de ir  al contenedor principal 
        main.cancela = false;
        main.vbod = this.bod;  //Uso el bod temporal(vbod) para guardar en caso de fallo, en caso de èxito mas abajo se lo paso la verdadero bod

        if (!main.ComprobarCambio(4, true)) {//Comprobar cambios que afecten datos posteriores, si este guardar es de una edición.
            if (!main.cancela) {
                bod.EditCalculoCaudalesDiseno = false;
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
    //---------------------------------------------------------------------------------------------------------------------- * * *

    private boolean CalcularCAR() {

        try {
            lbl_CAR_error.setText("");
            double x = bod.calcularCAR();

            if (bod.setVarDob(x, "CAR", map.get("CAR").rmin, map.get("CAR").rmax)) {
                txt_CAR_Pregunta.setText(bod.getVarFormateadaPorNombre("CAR", map.get("CAR").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularCAR " + ex.getMessage());
        }
        lbl_CAR_error.setText(map.get("CAR").erro);
        return false;
    }

    private boolean CalcularKAR() {

        try {
            lbl_KAR_error.setText("");
            double x = bod.calcularKAR();

            if (bod.setVarDob(x, "KAR", map.get("KAR").rmin, map.get("KAR").rmax)) {
                txt_KAR_Pregunta.setText(bod.getVarFormateadaPorNombre("KAR", map.get("KAR").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularKAR " + ex.getMessage());
        }
        lbl_KAR_error.setText(map.get("KAR").erro);
        return false;
    }

    private boolean CalcularQIF() {

        try {

            if (rbtn_QIF_Pregunta1.isSelected()) { // 0 = Extensión de la red de alc...
                if (bod.setVarInt(0, "QIF", (int) 0, 1)) {
                    verCaudalInfiltración(true);
                    return true;
                }
            }
            if (rbtn_QIF_Pregunta2.isSelected()) { // 1 = Área del alcantarillado inf...
                if (bod.setVarInt(1, "QIF", (int) 0, 1)) {
                    verCaudalInfiltración(false);
                    return true;
                }
            }

        } catch (Exception ex) {
            log.error("Error: CalcularQIF " + ex.getMessage());
        }
        return false;
    }

    private boolean CalcularQEL() {

        try {
            lbl_QEL_QAA_error.setText("");

            if (bod.setVarDob(txt_QEL_Pregunta.getText(), "QEL", map.get("QEL").rmin, map.get("QEL").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularQEL " + ex.getMessage());
        }
        lbl_QEL_QAA_error.setText(map.get("QEL").erro);
        return false;
    }

    private boolean CalcularQET() {

        try {
            lbl_QET_QAI_error.setText("");

            if (bod.setVarDob(txt_QET_Pregunta.getText(), "QET", map.get("QET").rmin, map.get("QET").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularQET " + ex.getMessage());
        }
        lbl_QET_QAI_error.setText(map.get("QET").erro);
        return false;
    }

    private boolean CalcularQEC() {

        try {
            lbl_QEC_QAC_error.setText("");
            double x = bod.calcularQEC();

            if (bod.setVarDob(x, "QEC", map.get("QEC").rmin, map.get("QEC").rmax)) {
                txt_QEC_Pregunta.setText(bod.getVarFormateadaPorNombre("QEC", map.get("QEC").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularQEC " + ex.getMessage());
        }
        lbl_QEC_QAC_error.setText(map.get("QEC").erro);
        return false;
    }

    private boolean CalcularQEK() {

        try {
            lbl_QEK_QAK_error.setText("");
            double x = bod.calcularQEK();

            if (bod.setVarDob(x, "QEK", map.get("QEK").rmin, map.get("QEK").rmax)) {
                txt_QEK_Pregunta.setText(bod.getVarFormateadaPorNombre("QEK", map.get("QEK").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularQEK " + ex.getMessage());
        }
        lbl_QEK_QAK_error.setText(map.get("QEK").erro);
        return false;
    }

    private boolean CalcularQAA() {

        try {
            lbl_QEL_QAA_error.setText("");

            if (bod.setVarDob(txt_QAA_Pregunta.getText(), "QAA", map.get("QAA").rmin, map.get("QAA").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularQAA " + ex.getMessage());
        }
        lbl_QEL_QAA_error.setText(map.get("QAA").erro);
        return false;
    }

    private boolean CalcularQAI() {

        try {
            lbl_QET_QAI_error.setText("");

            if (bod.setVarDob(txt_QAI_Pregunta.getText(), "QAI", map.get("QAI").rmin, map.get("QAI").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularQAI " + ex.getMessage());
        }
        lbl_QET_QAI_error.setText(map.get("QAI").erro);
        return false;
    }

    private boolean CalcularQAC() {

        try {
            lbl_QEC_QAC_error.setText("");
            double x = bod.calcularQAC();

            if (bod.setVarDob(x, "QAC", map.get("QAC").rmin, map.get("QAC").rmax)) {
                txt_QAC_Pregunta.setText(bod.getVarFormateadaPorNombre("QAC", map.get("QAC").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularQAC " + ex.getMessage());
        }
        lbl_QEC_QAC_error.setText(map.get("QAC").erro);
        return false;
    }

    private boolean CalcularQAK() {

        try {
            lbl_QEK_QAK_error.setText("");
            double x = bod.calcularQAK();

            if (bod.setVarDob(x, "QAK", map.get("QAK").rmin, map.get("QAK").rmax)) {
                txt_QAK_Pregunta.setText(bod.getVarFormateadaPorNombre("QAK", map.get("QAK").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularQAK " + ex.getMessage());
        }
        lbl_QEK_QAK_error.setText(map.get("QAK").erro);
        return false;
    }

    private boolean CalcularQCD() {

        try {

            if (rbtn_QCD_Pregunta1.isSelected()) { //si = 1
                if (bod.setVarInt(1, "QCD", (int) 0, 1)) {
                    verCaudalConexionesErradas(true);
                    return true;
                }
            }
            if (rbtn_QCD_Pregunta2.isSelected()) { //No = 0
                if (bod.setVarInt(0, "QCD", (int) 0, 1)) {
                    verCaudalConexionesErradas(false);
                    return true;
                }
            }

        } catch (Exception ex) {
            log.error("Error: CalcularQCD " + ex.getMessage());
        }
        return false;
    }

    private boolean CalcularQCA() {

        try {
            lbl_QCA_error.setText("");

            if (bod.setVarDob(txt_QCA_Pregunta.getText(), "QCA", map.get("QCA").rmin, map.get("QCA").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularQCA " + ex.getMessage());
        }
        lbl_QCA_error.setText(map.get("QCA").erro);
        return false;
    }

    private boolean CalcularQCM() {

        try {
            lbl_QCM_error.setText("");

            if (bod.setVarDob(txt_QCM_Pregunta.getText(), "QCM", map.get("QCM").rmin, map.get("QCM").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularQCM " + ex.getMessage());
        }
        lbl_QCM_error.setText(map.get("QCM").erro);
        return false;
    }

    private boolean CalcularQCC() {

        try {
            lbl_QCC_error.setText("");
            double x = bod.calcularQCC();

            if (bod.setVarDob(x, "QCC", map.get("QCC").rmin, map.get("QCC").rmax)) {
                txt_QCC_Pregunta.setText(bod.getVarFormateadaPorNombre("QCC", map.get("QCC").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularQCC " + ex.getMessage());
        }
        lbl_QCC_error.setText(map.get("QCC").erro);
        return false;
    }

    private boolean CalcularQCK() {

        try {
            lbl_QCK_error.setText("");
            double x = bod.calcularQCK();

            if (bod.setVarDob(x, "QCK", map.get("QCK").rmin, map.get("QCK").rmax)) {
                txt_QCK_Pregunta.setText(bod.getVarFormateadaPorNombre("QCK", map.get("QCK").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularQCK " + ex.getMessage());
        }
        lbl_QCK_error.setText(map.get("QCK").erro);
        return false;
    }

    private boolean CalcularIOA() {

        try {
            if (chbx_IOA_Pregunta.isSelected()) {
                if (bod.setVarInt(1, "IOA", (int) 0, 1)) {
                    verOtrosAportesIndustrial(true, false);//Muestra
                    CalcularIRM();
                    return true;
                }
            } else {
                if (bod.setVarInt(0, "IOA", (int) 0, 1)) {
                    verOtrosAportesIndustrial(true, true);//ocultar todo
                    return true;
                }
            }
        } catch (Exception ex) {
            log.error("Error: CalcularIOA " + ex.getMessage());
        }
        return false;
    }

    private boolean CalcularIRM() {

        try {
            if (rbtn_IRM_Pregunta1.isSelected()) { //si = 1 = IQ2,IK2 
                if (bod.setVarInt(1, "IRM", (int) 0, 1)) {
                    verOtrosAportesIndustrial(false, false);
                    return true;
                }
            }
            if (rbtn_IRM_Pregunta2.isSelected()) { //No = 0 = IAI,IPI.....
                if (bod.setVarInt(0, "IRM", (int) 0, 1)) {
                    verOtrosAportesIndustrial(true, false);
                    return true;
                }
            }

        } catch (Exception ex) {
            log.error("Error: CalcularIRM " + ex.getMessage());
        }
        return false;
    }

    private boolean CalcularIAI() {

        try {
            lbl_IAI_IQ2_error.setText("");

            if (bod.setVarDob(txt_IAI_Pregunta.getText(), "IAI", map.get("IAI").rmin, map.get("IAI").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularIAI " + ex.getMessage());
        }
        lbl_IAI_IQ2_error.setText(map.get("IAI").erro);
        return false;
    }

    private boolean CalcularIPI() {

        try {
            lbl_IPI_IK2_error.setText("");

            if (bod.setVarDob(txt_IPI_Pregunta.getText(), "IPI", map.get("IPI").rmin, map.get("IPI").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularIPI " + ex.getMessage());
        }
        lbl_IPI_IK2_error.setText(map.get("IPI").erro);
        return false;
    }

    private boolean CalcularIQI() {

        try {
            lbl_IQI_error.setText("");
            double x = bod.calcularIQI();

            if (bod.setVarDob(x, "IQI", map.get("IQI").rmin, map.get("IQI").rmax)) {
                txt_IQI_Pregunta.setText(bod.getVarFormateadaPorNombre("IQI", map.get("IQI").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularIQI " + ex.getMessage());
        }
        lbl_IQI_error.setText(map.get("IQI").erro);
        return false;
    }

    private boolean CalcularIKI() {

        try {
            lbl_IKI_error.setText("");
            double x = bod.calcularIKI();

            if (bod.setVarDob(x, "IKI", map.get("IKI").rmin, map.get("IKI").rmax)) {
                txt_IKI_Pregunta.setText(bod.getVarFormateadaPorNombre("IKI", map.get("IKI").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularIKI " + ex.getMessage());
        }
        lbl_IKI_error.setText(map.get("IKI").erro);
        return false;
    }

    private boolean CalcularIQ2() {

        try {
            lbl_IAI_IQ2_error.setText("");

            if (bod.setVarDob(txt_IQ2_Pregunta.getText(), "IQ2", map.get("IQ2").rmin, map.get("IQ2").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularIQ2 " + ex.getMessage());
        }
        lbl_IAI_IQ2_error.setText(map.get("IQ2").erro);
        return false;
    }

    private boolean CalcularIK2() {

        try {
            lbl_IPI_IK2_error.setText("");
            double x = bod.calcularIK2();

            if (bod.setVarDob(x, "IK2", map.get("IK2").rmin, map.get("IK2").rmax)) {
                txt_IK2_Pregunta.setText(bod.getVarFormateadaPorNombre("IK2", map.get("IK2").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularIK2 " + ex.getMessage());
        }
        lbl_IPI_IK2_error.setText(map.get("IK2").erro);
        return false;
    }

    private boolean CalcularCOA() {

        try {
            if (chbx_COA_Pregunta.isSelected()) {
                if (bod.setVarInt(1, "COA", (int) 0, 1)) {
                    verOtrosAportesComercial(false);
                    return true;
                }
            } else {
                if (bod.setVarInt(0, "COA", (int) 0, 1)) {
                    verOtrosAportesComercial(true);
                    return true;
                }
            }
        } catch (Exception ex) {
            log.error("Error: CalcularCOA " + ex.getMessage());
        }
        return false;
    }

    private boolean CalcularCAC() {

        try {
            lbl_CAC_error.setText("");

            if (bod.setVarDob(txt_CAC_Pregunta.getText(), "CAC", map.get("CAC").rmin, map.get("CAC").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularCAC " + ex.getMessage());
        }
        lbl_CAC_error.setText(map.get("CAC").erro);
        return false;
    }

    private boolean CalcularCPC() {

        try {
            lbl_CPC_error.setText("");

            if (bod.setVarDob(txt_CPC_Pregunta.getText(), "CPC", map.get("CPC").rmin, map.get("CPC").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularCPC " + ex.getMessage());
        }
        lbl_CPC_error.setText(map.get("CPC").erro);
        return false;
    }

    private boolean CalcularCQC() {

        try {
            lbl_CQC_error.setText("");
            double x = bod.calcularCQC();

            if (bod.setVarDob(x, "CQC", map.get("CQC").rmin, map.get("CQC").rmax)) {
                txt_CQC_Pregunta.setText(bod.getVarFormateadaPorNombre("CQC", map.get("CQC").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularCQC " + ex.getMessage());
        }
        lbl_CQC_error.setText(map.get("CQC").erro);
        return false;
    }

    private boolean CalcularCKC() {

        try {
            lbl_CKC_error.setText("");
            double x = bod.calcularCKC();

            if (bod.setVarDob(x, "CKC", map.get("CKC").rmin, map.get("CKC").rmax)) {
                txt_CKC_Pregunta.setText(bod.getVarFormateadaPorNombre("CKC", map.get("CKC").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularCKC " + ex.getMessage());
        }
        lbl_CKC_error.setText(map.get("CKC").erro);
        return false;
    }

    private boolean CalcularYOA() {

        try {
            if (chbx_YOA_Pregunta.isSelected()) {
                if (bod.setVarInt(1, "YOA", (int) 0, 1)) {
                    verOtrosAportesInstitucional(false);
                    return true;
                }
            } else {
                if (bod.setVarInt(0, "YOA", (int) 0, 1)) {
                    verOtrosAportesInstitucional(true);
                    return true;
                }
            }
        } catch (Exception ex) {
            log.error("Error: CalcularYOA " + ex.getMessage());
        }
        return false;
    }

    private boolean CalcularYAI() {

        try {
            lbl_YAI_error.setText("");

            if (bod.setVarDob(txt_YAI_Pregunta.getText(), "YAI", map.get("YAI").rmin, map.get("YAI").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularYAI " + ex.getMessage());
        }
        lbl_YAI_error.setText(map.get("YAI").erro);
        return false;
    }

    private boolean CalcularYPI() {

        try {
            lbl_YPI_error.setText("");

            if (bod.setVarDob(txt_YPI_Pregunta.getText(), "YPI", map.get("YPI").rmin, map.get("YPI").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularYPI " + ex.getMessage());
        }
        lbl_YPI_error.setText(map.get("YPI").erro);
        return false;
    }

    private boolean CalcularYQI() {

        try {
            lbl_YQI_error.setText("");
            double x = bod.calcularYQI();

            if (bod.setVarDob(x, "YQI", map.get("YQI").rmin, map.get("YQI").rmax)) {
                txt_YQI_Pregunta.setText(bod.getVarFormateadaPorNombre("YQI", map.get("YQI").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularYQI " + ex.getMessage());
        }
        lbl_YQI_error.setText(map.get("YQI").erro);
        return false;
    }

    private boolean CalcularYKI() {

        try {
            lbl_YKI_error.setText("");
            double x = bod.calcularYKI();

            if (bod.setVarDob(x, "YKI", map.get("YKI").rmin, map.get("YKI").rmax)) {
                txt_YKI_Pregunta.setText(bod.getVarFormateadaPorNombre("YKI", map.get("YKI").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularYKI " + ex.getMessage());
        }
        lbl_YKI_error.setText(map.get("YKI").erro);
        return false;
    }

    private boolean CalcularQ2C() {

        try {
            lbl_Q2C_error.setText("");
            double x = bod.calcularQ2C();

            if (bod.setVarDob(x, "Q2C", map.get("Q2C").rmin, map.get("Q2C").rmax)) {
                txt_Q2C_Pregunta.setText(bod.getVarFormateadaPorNombre("Q2C", map.get("Q2C").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularQ2C " + ex.getMessage());
        }
        lbl_Q2C_error.setText(map.get("Q2C").erro);
        return false;
    }

    private boolean CalcularQ2K() {

        try {
            lbl_Q2K_error.setText("");
            double x = bod.calcularQ2Cm3Dia();

            if (bod.setVarDob(x, "Q2K", map.get("Q2K").rmin, map.get("Q2K").rmax)) {
                txt_Q2K_Pregunta.setText(bod.getVarFormateadaPorNombre("Q2K", map.get("Q2K").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularQ2K " + ex.getMessage());
        }
        lbl_Q2K_error.setText(map.get("Q2K").erro);
        return false;
    }

    private boolean CalcularQ3M() {

        try {
            lbl_Q3M_error.setText("");

            if (bod.setVarDob(txt_Q3M_Pregunta.getText(), "Q3M", map.get("Q3M").rmin, map.get("Q3M").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularQ3M " + ex.getMessage());
        }
        lbl_Q3M_error.setText(map.get("Q3M").erro);
        return false;
    }

    private boolean CalcularQ3C() {

        try {
            lbl_Q3C_error.setText("");
            double x = bod.calcularQ3C();

            if (bod.setVarDob(x, "Q3C", map.get("Q3C").rmin, map.get("Q3C").rmax)) {
                txt_Q3C_Pregunta.setText(bod.getVarFormateadaPorNombre("Q3C", map.get("Q3C").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularQ3C " + ex.getMessage());
        }
        lbl_Q3C_error.setText(map.get("Q3C").erro);
        return false;
    }

    private boolean CalcularQ3K() {

        try {
            lbl_Q3K_error.setText("");
            double x = bod.calcularQ3Cm3Dia();

            if (bod.setVarDob(x, "Q3K", map.get("Q3K").rmin, map.get("Q3K").rmax)) {
                txt_Q3K_Pregunta.setText(bod.getVarFormateadaPorNombre("Q3K", map.get("Q3K").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularQ3K " + ex.getMessage());
        }
        lbl_Q3K_error.setText(map.get("Q3K").erro);
        return false;
    }

    private boolean CalcularQ1H() {

        try {
            lbl_Q1H_error.setText("");

            if (bod.setVarDob(txt_Q1H_Pregunta.getText(), "Q1H", map.get("Q1H").rmin, map.get("Q1H").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularQ1H " + ex.getMessage());
        }
        lbl_Q1H_error.setText(map.get("Q1H").erro);
        return false;
    }

    private boolean CalcularQ1C() {

        try {
            lbl_Q1C_error.setText("");
            double x = bod.calcularQ1C();

            if (bod.setVarDob(x, "Q1C", map.get("Q1C").rmin, map.get("Q1C").rmax)) {
                txt_Q1C_Pregunta.setText(bod.getVarFormateadaPorNombre("Q1C", map.get("Q1C").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularQ1C " + ex.getMessage());
        }
        lbl_Q1C_error.setText(map.get("Q1C").erro);
        return false;
    }

    private boolean CalcularQ1K() {

        try {
            lbl_Q1K_error.setText("");
            double x = bod.calcularQ1Cm3Dia();

            if (bod.setVarDob(x, "Q1K", map.get("Q1K").rmin, map.get("Q1K").rmax)) {
                txt_Q1K_Pregunta.setText(bod.getVarFormateadaPorNombre("Q1K", map.get("Q1K").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularQ1K " + ex.getMessage());
        }
        lbl_Q1K_error.setText(map.get("Q1K").erro);
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
            case "CAR":
                CalcularCAR();
                break;

            case "KAR":
                CalcularKAR();
                break;

            case "QIF":
                CalcularQIF();
                ejecutarFunciones("Q2C");
                break;

            case "QEL":
                CalcularQEL();
                ejecutarFunciones("QEC");
                break;

            case "QET":
                CalcularQET();
                ejecutarFunciones("QEC");
                ejecutarFunciones("QEK");
                break;

            case "QEC":
                CalcularQEC();
                ejecutarFunciones("QEK");
                break;

            case "QEK":
                CalcularQEK();
                break;

            case "QAA":
                CalcularQAA();
                ejecutarFunciones("QAC");
                ejecutarFunciones("QAK");
                break;

            case "QAI":
                CalcularQAI();
                ejecutarFunciones("QAC");
                ejecutarFunciones("QAK");
                break;

            case "QAC":
                CalcularQAC();
                break;

            case "QAK":
                CalcularQAK();
                break;

            case "QCD":
                CalcularQCD();
                ejecutarFunciones("Q2C");
                break;

            case "QCA":
                CalcularQCA();
                ejecutarFunciones("QCC");
                ejecutarFunciones("QCK");
                break;

            case "QCM":
                CalcularQCM();
                ejecutarFunciones("QCC");
                ejecutarFunciones("QCK");
                break;

            case "QCC":
                CalcularQCC();
                ejecutarFunciones("Q2C");
                break;

            case "QCK":
                CalcularQCK();
                break;

            case "IOA":
                CalcularIOA();
                break;

            case "IRM":
                CalcularIRM();
                ejecutarFunciones("Q2C");
                break;

            case "IAI":
                CalcularIAI();
                ejecutarFunciones("IQI");
                break;

            case "IPI":
                CalcularIPI();
                ejecutarFunciones("IQI");
                ejecutarFunciones("IKI");
                break;

            case "IQI":
                CalcularIQI();
                ejecutarFunciones("IKI");
                ejecutarFunciones("Q2C");
                break;

            case "IKI":
                CalcularIKI();
                break;

            case "IQ2":
                CalcularIQ2();
                ejecutarFunciones("IK2");
                ejecutarFunciones("Q2C");
                break;

            case "IK2":
                CalcularIK2();
                break;

            case "COA":
                CalcularCOA();
                ejecutarFunciones("Q2C");
                break;

            case "CAC":
                CalcularCAC();
                ejecutarFunciones("CQC");
                ejecutarFunciones("CKC");
                break;

            case "CPC":
                CalcularCPC();
                ejecutarFunciones("CQC");
                ejecutarFunciones("CKC");
                break;

            case "CQC":
                CalcularCQC();
                ejecutarFunciones("Q2C");
                break;

            case "CKC":
                CalcularCKC();
                break;

            case "YOA":
                CalcularYOA();
                ejecutarFunciones("Q2C");
                break;

            case "YAI":
                CalcularYAI();
                ejecutarFunciones("YQI");
                ejecutarFunciones("YKI");
                break;

            case "YPI":
                CalcularYPI();
                ejecutarFunciones("YQI");
                ejecutarFunciones("YKI");
                break;

            case "YQI":
                CalcularYQI();
                ejecutarFunciones("Q2C");
                break;

            case "YKI":
                CalcularYKI();
                break;

            case "Q2C":
                CalcularQ2C();
                ejecutarFunciones("Q2K");
                ejecutarFunciones("Q3C");
                ejecutarFunciones("Q1C");
                break;

            case "Q2K":
                CalcularQ2K();
                break;

            case "Q3M":
                CalcularQ3M();
                break;

            case "Q3C":
                CalcularQ3C();
                ejecutarFunciones("Q3K");
                break;

            case "Q3K":
                CalcularQ3K();
                break;

            case "Q1H":
                CalcularQ1H();
                ejecutarFunciones("Q1C");
                break;

            case "Q1C":
                CalcularQ1C();
                ejecutarFunciones("Q1K");
                break;

            case "Q1K":
                CalcularQ1K();
                break;
        }
    }

    /**
     * *
     * Oculta,visualiza o habilita las opciones de los radiobuttons de Caudal de
     * infiltración (Qinf) Según escoja el usuario: Extensión de la red de
     * alcantarillado o Área servida
     */
    private void verCaudalInfiltración(boolean esCero) { //QIF = 0 QIF Extensión de la red de alcantarillado, QIF = 1 Área del alcantarillado...

        lbl_QEL_QAA_error.setText("");
        lbl_QET_QAI_error.setText("");
        lbl_QEC_QAC_error.setText("");
        lbl_QEK_QAK_error.setText("");

        txt_QEL_Pregunta.setVisible(esCero);
        txt_QET_Pregunta.setVisible(esCero);
        btn_QET_ayuda.setVisible(esCero);
        txt_QEC_Pregunta.setVisible(esCero);
        txt_QEK_Pregunta.setVisible(esCero);

        txt_QAA_Pregunta.setVisible(!esCero);
        txt_QAI_Pregunta.setVisible(!esCero);
        btn_QAI_ayuda.setVisible(!esCero);
        txt_QAC_Pregunta.setVisible(!esCero);
        txt_QAK_Pregunta.setVisible(!esCero);

        lbl_QEL_QAA_titulo1.setVisible(true);
        lbl_QEL_QAA_titulo2.setVisible(true);
        lbl_QET_QAI_titulo1.setVisible(true);
        lbl_QET_QAI_titulo2.setVisible(true);
        lbl_QEC_QAC_titulo1.setVisible(true);
        lbl_QEC_QAC_titulo2.setVisible(true);
        lbl_QEK_QAK_titulo1.setVisible(true);
        lbl_QEK_QAK_titulo2.setVisible(true);

        if (esCero) {
            lbl_QEL_QAA_titulo1.setText(map.get("QEL").tit1);
            lbl_QEL_QAA_titulo2.setText(map.get("QEL").tit2);

            lbl_QET_QAI_titulo1.setText(map.get("QET").tit1);
            lbl_QET_QAI_titulo2.setText(map.get("QET").tit2);

            lbl_QEC_QAC_titulo1.setText(map.get("QEC").tit1);
            lbl_QEC_QAC_titulo2.setText(map.get("QEC").tit2);

            lbl_QEK_QAK_titulo1.setText(map.get("QEK").tit1);
            lbl_QEK_QAK_titulo2.setText(map.get("QEK").tit2);
        } else {
            lbl_QEL_QAA_titulo1.setText(map.get("QAA").tit1);
            lbl_QEL_QAA_titulo2.setText(map.get("QAA").tit2);

            lbl_QET_QAI_titulo1.setText(map.get("QAI").tit1);
            lbl_QET_QAI_titulo2.setText(map.get("QAI").tit2);

            lbl_QEC_QAC_titulo1.setText(map.get("QAC").tit1);
            lbl_QEC_QAC_titulo2.setText(map.get("QAC").tit2);

            lbl_QEK_QAK_titulo1.setText(map.get("QAK").tit1);
            lbl_QEK_QAK_titulo2.setText(map.get("QAK").tit2);
        }
    }

    /**
     * Oculta,visualiza o habilita las opciones de los radiobuttons de Caudal de
     * Conexiones Erradas (QCE) Si el usuario desea usarla opcionalmente
     */
    private void verCaudalConexionesErradas(boolean esCero) { //QCD = 0 No usar, QCD = 1 Usar caudal conex....

        lbl_QCA_titulo1.setVisible(esCero);
        txt_QCA_Pregunta.setVisible(esCero);
        lbl_QCA_titulo2.setVisible(esCero);
        lbl_QCA_error.setVisible(esCero);
        lbl_QCM_titulo1.setVisible(esCero);
        txt_QCM_Pregunta.setVisible(esCero);
        lbl_QCM_titulo2.setVisible(esCero);
        lbl_QCM_error.setVisible(esCero);
        lbl_QCC_titulo1.setVisible(esCero);
        txt_QCC_Pregunta.setVisible(esCero);
        lbl_QCC_titulo2.setVisible(esCero);
        lbl_QCC_error.setVisible(esCero);
        lbl_QCK_titulo1.setVisible(esCero);
        txt_QCK_Pregunta.setVisible(esCero);
        lbl_QCK_titulo2.setVisible(esCero);
        lbl_QCK_error.setVisible(esCero);
    }

    /**
     * Oculta,visualiza o habilita las opciones de los radiobuttons de Otros
     * Aportes tipo: Industrial de la pregunta ¿Cuenta con registros de medición
     * de caudales de origen industrial?
     */
    private void verOtrosAportesIndustrial(boolean esCero, boolean ocultarTodo) {

        lbl_IAI_IQ2_error.setText("");
        lbl_IPI_IK2_error.setText("");
        lbl_IQI_error.setText("");
        lbl_IKI_error.setText("");

        rbtn_IRM_Pregunta1.setVisible(!ocultarTodo);
        rbtn_IRM_Pregunta2.setVisible(!ocultarTodo);
        lbl_IRM_desc.setVisible(!ocultarTodo);

        if (!rbtn_IRM_Pregunta1.isSelected() && !rbtn_IRM_Pregunta2.isSelected()) {
            esCero = true;
            ocultarTodo = true;
        }

        if (ocultarTodo) {
            esCero = !esCero;
        }

        txt_IAI_Pregunta.setVisible(esCero);
        txt_IPI_Pregunta.setVisible(esCero);
        btn_IPI_ayuda.setVisible(esCero);
        txt_IQI_Pregunta.setVisible(esCero);
        txt_IKI_Pregunta.setVisible(esCero);

        if (ocultarTodo) {
            esCero = !esCero;
        }

        txt_IQ2_Pregunta.setVisible(!esCero);
        txt_IK2_Pregunta.setVisible(!esCero);

        lbl_IAI_IQ2_titulo1.setVisible(!ocultarTodo);
        lbl_IAI_IQ2_titulo2.setVisible(!ocultarTodo);
        lbl_IPI_IK2_titulo1.setVisible(!ocultarTodo);
        lbl_IPI_IK2_titulo2.setVisible(!ocultarTodo);
        lbl_IQI_titulo1.setVisible(!ocultarTodo);
        lbl_IQI_titulo2.setVisible(!ocultarTodo);
        lbl_IKI_titulo1.setVisible(!ocultarTodo);
        lbl_IKI_titulo2.setVisible(!ocultarTodo);


        if (esCero) { //No = IAI,IPI,IQI,IKI

            lbl_IAI_IQ2_titulo1.setText(map.get("IAI").tit1);
            lbl_IAI_IQ2_titulo2.setText(map.get("IAI").tit2);

            lbl_IPI_IK2_titulo1.setText(map.get("IPI").tit1);
            lbl_IPI_IK2_titulo2.setText(map.get("IPI").tit2);

            lbl_IQI_titulo1.setText(map.get("IQI").tit1);
            lbl_IQI_titulo2.setText(map.get("IQI").tit2);

            lbl_IKI_titulo1.setText(map.get("IKI").tit1);
            lbl_IKI_titulo2.setText(map.get("IKI").tit2);
        } else { //Si = IQ2,IK2 

            lbl_IAI_IQ2_titulo1.setText(map.get("IQ2").tit1);
            lbl_IAI_IQ2_titulo2.setText(map.get("IQ2").tit2);

            lbl_IPI_IK2_titulo1.setText(map.get("IK2").tit1);
            lbl_IPI_IK2_titulo2.setText(map.get("IK2").tit2);

            lbl_IQI_titulo1.setText("");
            lbl_IQI_titulo2.setText("");

            lbl_IKI_titulo1.setText("");
            lbl_IKI_titulo2.setText("");
        }
    }

    private void verOtrosAportesComercial(boolean esCero) {

        lbl_CAC_titulo1.setVisible(!esCero);
        txt_CAC_Pregunta.setVisible(!esCero);
        lbl_CAC_titulo2.setVisible(!esCero);
        lbl_CAC_error.setVisible(!esCero);
        lbl_CPC_titulo1.setVisible(!esCero);
        txt_CPC_Pregunta.setVisible(!esCero);
        lbl_CPC_titulo2.setVisible(!esCero);
        lbl_CPC_error.setVisible(!esCero);
        lbl_CQC_titulo1.setVisible(!esCero);
        txt_CQC_Pregunta.setVisible(!esCero);
        lbl_CQC_titulo2.setVisible(!esCero);
        lbl_CQC_error.setVisible(!esCero);
        lbl_CKC_titulo1.setVisible(!esCero);
        txt_CKC_Pregunta.setVisible(!esCero);
        lbl_CKC_titulo2.setVisible(!esCero);
        lbl_CKC_error.setVisible(!esCero);
    }

    private void verOtrosAportesInstitucional(boolean esCero) {

        lbl_YAI_titulo1.setVisible(!esCero);
        lbl_YAI_titulo2.setVisible(!esCero);
        txt_YAI_Pregunta.setVisible(!esCero);
        lbl_YAI_error.setVisible(!esCero);
        lbl_YPI_titulo1.setVisible(!esCero);
        lbl_YPI_titulo2.setVisible(!esCero);
        txt_YPI_Pregunta.setVisible(!esCero);
        lbl_YPI_error.setVisible(!esCero);
        btn_YPI_ayuda.setVisible(!esCero);
        lbl_YQI_titulo1.setVisible(!esCero);
        lbl_YQI_titulo2.setVisible(!esCero);
        txt_YQI_Pregunta.setVisible(!esCero);
        lbl_YQI_error.setVisible(!esCero);
        lbl_YKI_titulo1.setVisible(!esCero);
        lbl_YKI_titulo2.setVisible(!esCero);
        txt_YKI_Pregunta.setVisible(!esCero);
        lbl_YKI_error.setVisible(!esCero);
    }

    private void AsignarPopupBtn(JButton lbl, String mensaje, String uri, int dx, int dy) {
        Lpopup = new Listener_Popup();
        Lpopup.AgregarMensajePopupBtn(lbl, mensaje, uri, dx, dy);
    }

    private void cerrar() {
        if (!eSave) {
            int n = util.Mensaje("¿Desea cerrar? \n Se perderan los cambios realizados?", "yesno");
            if (n == JOptionPane.NO_OPTION) {
                return;
            }
        }
        bod.WinCalculoCaudalesDiseno = false;
        Main main = (Main) this.getTopLevelAncestor();
        main.bod.WinCalculoCaudalesDiseno = false;
        if(esGuia) main.guiaUsuario2();
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jsp_CalculoCaudalesDiseno = new javax.swing.JScrollPane();
        jp_Componentes = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        lbl_titulo1 = new javax.swing.JLabel();
        lbl_CAR_titulo1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        txt_CAR_Pregunta = new javax.swing.JTextField();
        lbl_titulo2 = new javax.swing.JLabel();
        lbl_CAR_titulo2 = new javax.swing.JLabel();
        lbl_QIF_desc = new javax.swing.JLabel();
        txt_QAA_Pregunta = new javax.swing.JTextField();
        lbl_QEL_QAA_titulo1 = new javax.swing.JLabel();
        lbl_QEL_QAA_titulo2 = new javax.swing.JLabel();
        lbl_KAR_error = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        lbl_QEL_QAA_error = new javax.swing.JLabel();
        txt_KAR_Pregunta = new javax.swing.JTextField();
        lbl_KAR_titulo2 = new javax.swing.JLabel();
        lbl_CAR_error = new javax.swing.JLabel();
        lbl_KAR_titulo1 = new javax.swing.JLabel();
        rbtn_QIF_Pregunta2 = new javax.swing.JRadioButton();
        lbl_QET_QAI_error = new javax.swing.JLabel();
        lbl_QET_QAI_titulo2 = new javax.swing.JLabel();
        lbl_QET_QAI_titulo1 = new javax.swing.JLabel();
        txt_QAI_Pregunta = new javax.swing.JTextField();
        lbl_QEC_QAC_error = new javax.swing.JLabel();
        lbl_QEC_QAC_titulo2 = new javax.swing.JLabel();
        lbl_QEC_QAC_titulo1 = new javax.swing.JLabel();
        txt_QAC_Pregunta = new javax.swing.JTextField();
        lbl_QEK_QAK_error = new javax.swing.JLabel();
        lbl_QEK_QAK_titulo2 = new javax.swing.JLabel();
        lbl_QEK_QAK_titulo1 = new javax.swing.JLabel();
        txt_QEL_Pregunta = new javax.swing.JTextField();
        lbl_QCA_error = new javax.swing.JLabel();
        lbl_QCA_titulo2 = new javax.swing.JLabel();
        lbl_QCA_titulo1 = new javax.swing.JLabel();
        txt_QCA_Pregunta = new javax.swing.JTextField();
        lbl_QCK_titulo1 = new javax.swing.JLabel();
        lbl_QCM_error = new javax.swing.JLabel();
        lbl_QCM_titulo2 = new javax.swing.JLabel();
        txt_QCK_Pregunta = new javax.swing.JTextField();
        lbl_QCK_titulo2 = new javax.swing.JLabel();
        lbl_QCK_error = new javax.swing.JLabel();
        txt_QCC_Pregunta = new javax.swing.JTextField();
        lbl_QCC_titulo1 = new javax.swing.JLabel();
        lbl_QCC_titulo2 = new javax.swing.JLabel();
        lbl_QCC_error = new javax.swing.JLabel();
        lbl_QCM_titulo1 = new javax.swing.JLabel();
        txt_QCM_Pregunta = new javax.swing.JTextField();
        lbl_QCD_desc = new javax.swing.JLabel();
        rbtn_QIF_Pregunta1 = new javax.swing.JRadioButton();
        jSeparator4 = new javax.swing.JSeparator();
        rbtn_IRM_Pregunta1 = new javax.swing.JRadioButton();
        lbl_IOA_desc = new javax.swing.JLabel();
        rbtn_IRM_Pregunta2 = new javax.swing.JRadioButton();
        lbl_IAI_IQ2_error = new javax.swing.JLabel();
        txt_IPI_Pregunta = new javax.swing.JTextField();
        lbl_IPI_IK2_titulo1 = new javax.swing.JLabel();
        lbl_IQI_error = new javax.swing.JLabel();
        lbl_IQI_titulo2 = new javax.swing.JLabel();
        lbl_IPI_IK2_titulo2 = new javax.swing.JLabel();
        lbl_IPI_IK2_error = new javax.swing.JLabel();
        lbl_IAI_IQ2_titulo1 = new javax.swing.JLabel();
        lbl_IAI_IQ2_titulo2 = new javax.swing.JLabel();
        lbl_IKI_titulo1 = new javax.swing.JLabel();
        txt_IAI_Pregunta = new javax.swing.JTextField();
        lbl_IKI_titulo2 = new javax.swing.JLabel();
        lbl_IKI_error = new javax.swing.JLabel();
        txt_IQI_Pregunta = new javax.swing.JTextField();
        lbl_IQI_titulo1 = new javax.swing.JLabel();
        txt_IKI_Pregunta = new javax.swing.JTextField();
        txt_IK2_Pregunta = new javax.swing.JTextField();
        txt_IQ2_Pregunta = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        lbl_CAC_error = new javax.swing.JLabel();
        chbx_COA_Pregunta = new javax.swing.JCheckBox();
        txt_CPC_Pregunta = new javax.swing.JTextField();
        lbl_CPC_titulo1 = new javax.swing.JLabel();
        lbl_CQC_error = new javax.swing.JLabel();
        lbl_CQC_titulo2 = new javax.swing.JLabel();
        lbl_CPC_titulo2 = new javax.swing.JLabel();
        lbl_CPC_error = new javax.swing.JLabel();
        lbl_CAC_titulo1 = new javax.swing.JLabel();
        lbl_CAC_titulo2 = new javax.swing.JLabel();
        lbl_CKC_titulo1 = new javax.swing.JLabel();
        txt_CAC_Pregunta = new javax.swing.JTextField();
        lbl_CKC_titulo2 = new javax.swing.JLabel();
        lbl_CKC_error = new javax.swing.JLabel();
        txt_CQC_Pregunta = new javax.swing.JTextField();
        lbl_CQC_titulo1 = new javax.swing.JLabel();
        txt_CKC_Pregunta = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();
        txt_YPI_Pregunta = new javax.swing.JTextField();
        lbl_YPI_titulo1 = new javax.swing.JLabel();
        lbl_YQI_error = new javax.swing.JLabel();
        lbl_YAI_error = new javax.swing.JLabel();
        chbx_IOA_Pregunta = new javax.swing.JCheckBox();
        lbl_YQI_titulo2 = new javax.swing.JLabel();
        lbl_YKI_titulo1 = new javax.swing.JLabel();
        txt_YAI_Pregunta = new javax.swing.JTextField();
        lbl_YAI_titulo1 = new javax.swing.JLabel();
        lbl_YAI_titulo2 = new javax.swing.JLabel();
        txt_YQI_Pregunta = new javax.swing.JTextField();
        lbl_YQI_titulo1 = new javax.swing.JLabel();
        lbl_YKI_titulo2 = new javax.swing.JLabel();
        lbl_YKI_error = new javax.swing.JLabel();
        lbl_YPI_error = new javax.swing.JLabel();
        lbl_YPI_titulo2 = new javax.swing.JLabel();
        txt_YKI_Pregunta = new javax.swing.JTextField();
        jSeparator8 = new javax.swing.JSeparator();
        lbl_Q2C_titulo1 = new javax.swing.JLabel();
        txt_Q2C_Pregunta = new javax.swing.JTextField();
        lbl_Q2K_titulo2 = new javax.swing.JLabel();
        lbl_Q2K_error = new javax.swing.JLabel();
        txt_Q2K_Pregunta = new javax.swing.JTextField();
        lbl_Q2K_titulo1 = new javax.swing.JLabel();
        lbl_Q2C_error = new javax.swing.JLabel();
        lbl_Q2C_titulo2 = new javax.swing.JLabel();
        lbl_Q2C_desc = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        lbl_Q3M_desc = new javax.swing.JLabel();
        txt_Q3M_Pregunta = new javax.swing.JTextField();
        lbl_Q3M_titulo1 = new javax.swing.JLabel();
        lbl_Q3M_titulo2 = new javax.swing.JLabel();
        lbl_Q3M_error = new javax.swing.JLabel();
        txt_Q3K_Pregunta = new javax.swing.JTextField();
        lbl_Q3C_titulo2 = new javax.swing.JLabel();
        lbl_Q3C_error = new javax.swing.JLabel();
        txt_Q3C_Pregunta = new javax.swing.JTextField();
        lbl_Q3C_titulo1 = new javax.swing.JLabel();
        lbl_Q3K_titulo2 = new javax.swing.JLabel();
        lbl_Q3K_error = new javax.swing.JLabel();
        lbl_Q3K_titulo1 = new javax.swing.JLabel();
        lbl_Q1H_desc = new javax.swing.JLabel();
        lbl_Q1C_titulo1 = new javax.swing.JLabel();
        txt_Q1C_Pregunta = new javax.swing.JTextField();
        lbl_Q1C_error = new javax.swing.JLabel();
        lbl_Q1C_titulo2 = new javax.swing.JLabel();
        lbl_Q1H_titulo1 = new javax.swing.JLabel();
        txt_Q1H_Pregunta = new javax.swing.JTextField();
        txt_Q1K_Pregunta = new javax.swing.JTextField();
        lbl_Q1K_titulo2 = new javax.swing.JLabel();
        lbl_Q1K_error = new javax.swing.JLabel();
        lbl_Q1H_error = new javax.swing.JLabel();
        lbl_Q1H_titulo2 = new javax.swing.JLabel();
        lbl_Q1K_titulo1 = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        btn_guardar = new javax.swing.JButton();
        btn_close = new javax.swing.JButton();
        lbl_save_error = new javax.swing.JLabel();
        lbl_save_desc = new javax.swing.JLabel();
        txt_QAK_Pregunta = new javax.swing.JTextField();
        txt_QEK_Pregunta = new javax.swing.JTextField();
        txt_QEC_Pregunta = new javax.swing.JTextField();
        txt_QET_Pregunta = new javax.swing.JTextField();
        lbl_IRM_desc = new javax.swing.JLabel();
        rbtn_QCD_Pregunta1 = new javax.swing.JRadioButton();
        rbtn_QCD_Pregunta2 = new javax.swing.JRadioButton();
        lbl_QCD_error = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        btn_CAR_ayuda = new javax.swing.JButton();
        btn_QAI_ayuda = new javax.swing.JButton();
        btn_QET_ayuda = new javax.swing.JButton();
        btn_QCA_ayuda = new javax.swing.JButton();
        btn_IOA_ayuda = new javax.swing.JButton();
        btn_IPI_ayuda = new javax.swing.JButton();
        btn_Q3R_ayuda = new javax.swing.JButton();
        btn_YPI_ayuda = new javax.swing.JButton();
        btn_YOA_ayuda = new javax.swing.JButton();
        btn_COA_ayuda = new javax.swing.JButton();
        btn_Q2C_ayuda = new javax.swing.JButton();
        btn_Q1H_ayuda = new javax.swing.JButton();
        btn_Q3M_ayuda = new javax.swing.JButton();
        btn_Q1C_ayuda = new javax.swing.JButton();
        btn_QIF_ayuda = new javax.swing.JButton();
        lbl_save_titulo1 = new javax.swing.JLabel();
        chbx_YOA_Pregunta = new javax.swing.JCheckBox();
        btn_close2 = new javax.swing.JButton();

        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(1024, 1524));

        jsp_CalculoCaudalesDiseno.setPreferredSize(new java.awt.Dimension(1024, 2500));

        jp_Componentes.setName(""); // NOI18N
        jp_Componentes.setOpaque(false);
        jp_Componentes.setPreferredSize(new java.awt.Dimension(1024, 2500));
        jp_Componentes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jp_Componentes.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 1093, 2));

        lbl_titulo1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_titulo1.setForeground(new java.awt.Color(51, 51, 51));
        lbl_titulo1.setText("Titulo");
        jp_Componentes.add(lbl_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 300, 60));

        lbl_CAR_titulo1.setText("Titulo");
        lbl_CAR_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAR_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 300, 30));
        jp_Componentes.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1093, -1));

        txt_CAR_Pregunta.setEditable(false);
        jp_Componentes.add(txt_CAR_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 120, 130, 25));

        lbl_titulo2.setForeground(new java.awt.Color(0, 51, 102));
        lbl_titulo2.setText("Desc");
        jp_Componentes.add(lbl_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 0, 650, 85));

        lbl_CAR_titulo2.setText("Titulo");
        lbl_CAR_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAR_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 120, 80, 25));

        lbl_QIF_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_QIF_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_QIF_desc.setText("Desc");
        lbl_QIF_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QIF_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 610, 30));

        txt_QAA_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_QAA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 290, 130, 25));

        lbl_QEL_QAA_titulo1.setText("Titulo");
        lbl_QEL_QAA_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QEL_QAA_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 300, 30));

        lbl_QEL_QAA_titulo2.setText("Titulo");
        lbl_QEL_QAA_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QEL_QAA_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 290, 80, 25));

        lbl_KAR_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_KAR_error.setText(" . . .");
        jp_Componentes.add(lbl_KAR_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 150, 340, 35));
        jp_Componentes.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 450, 1093, 2));

        lbl_QEL_QAA_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_QEL_QAA_error.setText(" . . .");
        jp_Componentes.add(lbl_QEL_QAA_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 280, 280, 35));

        txt_KAR_Pregunta.setEditable(false);
        jp_Componentes.add(txt_KAR_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 160, 130, 25));

        lbl_KAR_titulo2.setText("Titulo");
        lbl_KAR_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_KAR_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 160, 80, 25));

        lbl_CAR_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_CAR_error.setText(" . . .");
        jp_Componentes.add(lbl_CAR_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 110, 340, 35));

        lbl_KAR_titulo1.setText("Titulo");
        lbl_KAR_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_KAR_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 300, 30));

        rbtn_QIF_Pregunta2.setText("Titulo");
        jp_Componentes.add(rbtn_QIF_Pregunta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 240, 440, 30));

        lbl_QET_QAI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_QET_QAI_error.setText(" . . .");
        jp_Componentes.add(lbl_QET_QAI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 320, 280, 35));

        lbl_QET_QAI_titulo2.setText("Titulo");
        lbl_QET_QAI_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QET_QAI_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 330, 80, 25));

        lbl_QET_QAI_titulo1.setText("Titulo");
        lbl_QET_QAI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QET_QAI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 300, 30));

        txt_QAI_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_QAI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 330, 130, 25));

        lbl_QEC_QAC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_QEC_QAC_error.setText(" . . .");
        jp_Componentes.add(lbl_QEC_QAC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 360, 280, 35));

        lbl_QEC_QAC_titulo2.setText("Titulo");
        lbl_QEC_QAC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QEC_QAC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 370, 80, 25));

        lbl_QEC_QAC_titulo1.setText("Titulo");
        lbl_QEC_QAC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QEC_QAC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 300, 30));

        txt_QAC_Pregunta.setEditable(false);
        jp_Componentes.add(txt_QAC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 370, 130, 25));

        lbl_QEK_QAK_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_QEK_QAK_error.setText(" . . .");
        jp_Componentes.add(lbl_QEK_QAK_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 400, 280, 35));

        lbl_QEK_QAK_titulo2.setText("Titulo");
        lbl_QEK_QAK_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QEK_QAK_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 410, 80, 25));

        lbl_QEK_QAK_titulo1.setText("Titulo");
        lbl_QEK_QAK_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QEK_QAK_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, 300, 30));

        txt_QEL_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_QEL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 290, 130, 25));

        lbl_QCA_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_QCA_error.setText(" . . .");
        jp_Componentes.add(lbl_QCA_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 500, 340, 35));

        lbl_QCA_titulo2.setText("Titulo");
        lbl_QCA_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QCA_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 500, 80, 25));

        lbl_QCA_titulo1.setText("Titulo");
        lbl_QCA_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QCA_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 500, 300, 30));

        txt_QCA_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_QCA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 500, 130, 25));

        lbl_QCK_titulo1.setText("Titulo");
        lbl_QCK_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QCK_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 620, 300, 30));

        lbl_QCM_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_QCM_error.setText(" . . .");
        jp_Componentes.add(lbl_QCM_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 540, 340, 35));

        lbl_QCM_titulo2.setText("Titulo");
        lbl_QCM_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QCM_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 540, 80, 25));

        txt_QCK_Pregunta.setEditable(false);
        jp_Componentes.add(txt_QCK_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 620, 130, 25));

        lbl_QCK_titulo2.setText("Titulo");
        lbl_QCK_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QCK_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 620, 80, 25));

        lbl_QCK_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_QCK_error.setText(" . . .");
        jp_Componentes.add(lbl_QCK_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 620, 340, 35));

        txt_QCC_Pregunta.setEditable(false);
        jp_Componentes.add(txt_QCC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 580, 130, 25));

        lbl_QCC_titulo1.setText("Titulo");
        lbl_QCC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QCC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 580, 300, 30));

        lbl_QCC_titulo2.setText("Titulo");
        lbl_QCC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QCC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 580, 80, 25));

        lbl_QCC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_QCC_error.setText(" . . .");
        jp_Componentes.add(lbl_QCC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 570, 340, 35));

        lbl_QCM_titulo1.setText("Titulo");
        lbl_QCM_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QCM_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 540, 300, 30));

        txt_QCM_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_QCM_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 540, 130, 25));

        lbl_QCD_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_QCD_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_QCD_desc.setText("Desc");
        lbl_QCD_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QCD_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 300, 30));

        rbtn_QIF_Pregunta1.setText("Titulo");
        jp_Componentes.add(rbtn_QIF_Pregunta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 440, 30));
        jp_Componentes.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 660, 1093, -1));

        rbtn_IRM_Pregunta1.setText("Titulo");
        jp_Componentes.add(rbtn_IRM_Pregunta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 750, 80, 25));

        lbl_IOA_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_IOA_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_IOA_desc.setText("Desc");
        lbl_IOA_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IOA_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 670, 610, 30));

        rbtn_IRM_Pregunta2.setText("Titulo");
        jp_Componentes.add(rbtn_IRM_Pregunta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 750, 80, 25));

        lbl_IAI_IQ2_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_IAI_IQ2_error.setText(" . . .");
        jp_Componentes.add(lbl_IAI_IQ2_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 780, 280, 35));

        txt_IPI_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_IPI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 830, 130, 25));

        lbl_IPI_IK2_titulo1.setText("Titulo");
        lbl_IPI_IK2_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IPI_IK2_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 830, 300, 25));

        lbl_IQI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_IQI_error.setText(" . . .");
        jp_Componentes.add(lbl_IQI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 860, 280, 35));

        lbl_IQI_titulo2.setText("Titulo");
        lbl_IQI_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IQI_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 870, 80, 25));

        lbl_IPI_IK2_titulo2.setText("Titulo");
        lbl_IPI_IK2_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IPI_IK2_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 830, 80, 25));

        lbl_IPI_IK2_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_IPI_IK2_error.setText(" . . .");
        jp_Componentes.add(lbl_IPI_IK2_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 820, 280, 35));

        lbl_IAI_IQ2_titulo1.setText("Titulo");
        lbl_IAI_IQ2_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IAI_IQ2_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 790, 300, 30));

        lbl_IAI_IQ2_titulo2.setText("Titulo");
        lbl_IAI_IQ2_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IAI_IQ2_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 790, 80, 25));

        lbl_IKI_titulo1.setText("Titulo");
        lbl_IKI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IKI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 910, 300, 30));

        txt_IAI_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_IAI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 790, 130, 25));

        lbl_IKI_titulo2.setText("Titulo");
        lbl_IKI_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IKI_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 910, 80, 25));

        lbl_IKI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_IKI_error.setText(" . . .");
        jp_Componentes.add(lbl_IKI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 900, 280, 35));

        txt_IQI_Pregunta.setEditable(false);
        jp_Componentes.add(txt_IQI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 870, 130, 25));

        lbl_IQI_titulo1.setText("Titulo");
        lbl_IQI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IQI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 870, 300, 30));

        txt_IKI_Pregunta.setEditable(false);
        jp_Componentes.add(txt_IKI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 910, 130, 25));

        txt_IK2_Pregunta.setEditable(false);
        jp_Componentes.add(txt_IK2_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 830, 130, 25));

        txt_IQ2_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_IQ2_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 790, 130, 25));
        jp_Componentes.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 950, 1093, -1));

        lbl_CAC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_CAC_error.setText(" . . .");
        jp_Componentes.add(lbl_CAC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 990, 340, 35));

        chbx_COA_Pregunta.setText("Titulo");
        jp_Componentes.add(chbx_COA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 960, 300, 30));

        txt_CPC_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_CPC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1040, 130, 25));

        lbl_CPC_titulo1.setText("Titulo");
        lbl_CPC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CPC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1040, 300, 30));

        lbl_CQC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_CQC_error.setText(" . . .");
        jp_Componentes.add(lbl_CQC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1070, 340, 35));

        lbl_CQC_titulo2.setText("Titulo");
        lbl_CQC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CQC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1080, 80, 25));

        lbl_CPC_titulo2.setText("Titulo");
        lbl_CPC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CPC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1040, 80, 25));

        lbl_CPC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_CPC_error.setText(" . . .");
        jp_Componentes.add(lbl_CPC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1030, 340, 35));

        lbl_CAC_titulo1.setText("Titulo");
        lbl_CAC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1000, 300, 30));

        lbl_CAC_titulo2.setText("Titulo");
        lbl_CAC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1000, 80, 25));

        lbl_CKC_titulo1.setText("Titulo");
        lbl_CKC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CKC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1120, 300, 30));

        txt_CAC_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_CAC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1000, 130, 25));

        lbl_CKC_titulo2.setText("Titulo");
        lbl_CKC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CKC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1120, 80, 25));

        lbl_CKC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_CKC_error.setText(" . . .");
        jp_Componentes.add(lbl_CKC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1110, 340, 35));

        txt_CQC_Pregunta.setEditable(false);
        jp_Componentes.add(txt_CQC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1080, 130, 25));

        lbl_CQC_titulo1.setText("Titulo");
        lbl_CQC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CQC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1080, 300, 30));

        txt_CKC_Pregunta.setEditable(false);
        jp_Componentes.add(txt_CKC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1120, 130, 25));
        jp_Componentes.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1160, 1083, 2));

        txt_YPI_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_YPI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1260, 130, 25));

        lbl_YPI_titulo1.setText("Titulo");
        lbl_YPI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_YPI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1260, 300, 30));

        lbl_YQI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_YQI_error.setText(" . . .");
        jp_Componentes.add(lbl_YQI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1290, 200, 35));

        lbl_YAI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_YAI_error.setText(" . . .");
        jp_Componentes.add(lbl_YAI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1210, 200, 35));

        chbx_IOA_Pregunta.setText("Titulo");
        jp_Componentes.add(chbx_IOA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 710, 300, 30));

        lbl_YQI_titulo2.setText("Titulo");
        lbl_YQI_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_YQI_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1300, 80, 25));

        lbl_YKI_titulo1.setText("Titulo");
        lbl_YKI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_YKI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1340, 300, 30));

        txt_YAI_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_YAI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1220, 130, 25));

        lbl_YAI_titulo1.setText("Titulo");
        lbl_YAI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_YAI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1210, 300, 30));

        lbl_YAI_titulo2.setText("Titulo");
        lbl_YAI_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_YAI_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1220, 80, 25));

        txt_YQI_Pregunta.setEditable(false);
        jp_Componentes.add(txt_YQI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1300, 130, 25));

        lbl_YQI_titulo1.setText("Titulo");
        lbl_YQI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_YQI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1300, 300, 30));

        lbl_YKI_titulo2.setText("Titulo");
        lbl_YKI_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_YKI_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1340, 80, 25));

        lbl_YKI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_YKI_error.setText(" . . .");
        jp_Componentes.add(lbl_YKI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1330, 200, 35));

        lbl_YPI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_YPI_error.setText(" . . .");
        jp_Componentes.add(lbl_YPI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1250, 200, 35));

        lbl_YPI_titulo2.setText("Titulo");
        lbl_YPI_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_YPI_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1260, 80, 25));

        txt_YKI_Pregunta.setEditable(false);
        jp_Componentes.add(txt_YKI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1340, 130, 25));
        jp_Componentes.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1380, 1093, 2));

        lbl_Q2C_titulo1.setText("Titulo");
        lbl_Q2C_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2C_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1430, 300, 30));

        txt_Q2C_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q2C_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1430, 130, 25));

        lbl_Q2K_titulo2.setText("Titulo");
        lbl_Q2K_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2K_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1470, 80, 25));

        lbl_Q2K_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_Q2K_error.setText(" . . .");
        jp_Componentes.add(lbl_Q2K_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1460, 340, 35));

        txt_Q2K_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q2K_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1470, 130, 25));

        lbl_Q2K_titulo1.setText("Titulo");
        lbl_Q2K_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2K_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1470, 300, 30));

        lbl_Q2C_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_Q2C_error.setText(" . . .");
        jp_Componentes.add(lbl_Q2C_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1420, 340, 35));

        lbl_Q2C_titulo2.setText("Titulo");
        lbl_Q2C_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2C_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1430, 80, 25));

        lbl_Q2C_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_Q2C_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_Q2C_desc.setText("Desc");
        lbl_Q2C_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2C_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1390, 610, 30));
        jp_Componentes.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1510, 1093, 2));

        lbl_Q3M_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_Q3M_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_Q3M_desc.setText("Desc");
        lbl_Q3M_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q3M_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1520, 610, 30));

        txt_Q3M_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q3M_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1560, 130, 25));

        lbl_Q3M_titulo1.setText("Titulo");
        lbl_Q3M_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q3M_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1560, 270, 30));

        lbl_Q3M_titulo2.setText("Titulo");
        lbl_Q3M_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q3M_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1560, 80, 25));

        lbl_Q3M_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_Q3M_error.setText(" . . .");
        jp_Componentes.add(lbl_Q3M_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1550, 200, 35));

        txt_Q3K_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q3K_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1640, 130, 25));

        lbl_Q3C_titulo2.setText("Titulo");
        lbl_Q3C_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q3C_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1600, 80, 25));

        lbl_Q3C_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_Q3C_error.setText(" . . .");
        jp_Componentes.add(lbl_Q3C_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1590, 200, 35));

        txt_Q3C_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q3C_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1600, 130, 25));

        lbl_Q3C_titulo1.setText("Titulo");
        lbl_Q3C_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q3C_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1600, 300, 30));

        lbl_Q3K_titulo2.setText("Titulo");
        lbl_Q3K_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q3K_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1640, 80, 25));

        lbl_Q3K_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_Q3K_error.setText(" . . .");
        jp_Componentes.add(lbl_Q3K_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1630, 200, 35));

        lbl_Q3K_titulo1.setText("Titulo");
        lbl_Q3K_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q3K_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1640, 300, 30));

        lbl_Q1H_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_Q1H_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_Q1H_desc.setText("Desc");
        lbl_Q1H_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q1H_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1700, 610, 30));

        lbl_Q1C_titulo1.setText("Titulo");
        lbl_Q1C_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q1C_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1780, 300, 30));

        txt_Q1C_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q1C_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1780, 130, 25));

        lbl_Q1C_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_Q1C_error.setText(" . . .");
        jp_Componentes.add(lbl_Q1C_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1770, 200, 35));

        lbl_Q1C_titulo2.setText("Titulo");
        lbl_Q1C_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q1C_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1780, 80, 25));

        lbl_Q1H_titulo1.setText("Titulo");
        lbl_Q1H_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q1H_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1740, 300, 30));

        txt_Q1H_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_Q1H_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1740, 130, 25));

        txt_Q1K_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q1K_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1820, 130, 25));

        lbl_Q1K_titulo2.setText("Titulo");
        lbl_Q1K_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q1K_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1820, 80, 25));

        lbl_Q1K_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_Q1K_error.setText(" . . .");
        jp_Componentes.add(lbl_Q1K_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1810, 200, 35));

        lbl_Q1H_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_Q1H_error.setText(" . . .");
        jp_Componentes.add(lbl_Q1H_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1730, 200, 35));

        lbl_Q1H_titulo2.setText("Titulo");
        lbl_Q1H_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q1H_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1740, 80, 25));

        lbl_Q1K_titulo1.setText("Titulo");
        lbl_Q1K_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q1K_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1820, 300, 30));
        jp_Componentes.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1860, 1083, 2));

        btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Button_Salva_Guardar_21.png"))); // NOI18N
        btn_guardar.setText("cerrar");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1870, 120, -1));

        btn_close.setText("Salir");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1910, 120, 30));

        lbl_save_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error.setText(" . . .");
        jp_Componentes.add(lbl_save_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 1870, 500, 35));

        lbl_save_desc.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_desc.setText(" . . .");
        jp_Componentes.add(lbl_save_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 1910, 610, 35));

        txt_QAK_Pregunta.setEditable(false);
        jp_Componentes.add(txt_QAK_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 410, 130, 25));

        txt_QEK_Pregunta.setEditable(false);
        jp_Componentes.add(txt_QEK_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 410, 130, 25));

        txt_QEC_Pregunta.setEditable(false);
        jp_Componentes.add(txt_QEC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 370, 130, 25));

        txt_QET_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_QET_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 330, 130, 25));

        lbl_IRM_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_IRM_desc.setText("Desc");
        lbl_IRM_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IRM_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 750, 340, 30));

        rbtn_QCD_Pregunta1.setText("Titulo");
        jp_Componentes.add(rbtn_QCD_Pregunta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 460, 80, 25));

        rbtn_QCD_Pregunta2.setText("Titulo");
        jp_Componentes.add(rbtn_QCD_Pregunta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 460, 80, 25));

        lbl_QCD_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_QCD_error.setText(" . . .");
        jp_Componentes.add(lbl_QCD_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 460, 340, 35));
        jp_Componentes.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1680, 1093, 2));

        btn_CAR_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_CAR_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_CAR_ayuda.setContentAreaFilled(false);
        btn_CAR_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_CAR_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 120, 25, 25));

        btn_QAI_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_QAI_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_QAI_ayuda.setContentAreaFilled(false);
        btn_QAI_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_QAI_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 330, 25, 25));

        btn_QET_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_QET_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_QET_ayuda.setContentAreaFilled(false);
        btn_QET_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_QET_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 330, 25, 25));

        btn_QCA_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_QCA_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_QCA_ayuda.setContentAreaFilled(false);
        btn_QCA_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_QCA_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 460, 25, 25));

        btn_IOA_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_IOA_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_IOA_ayuda.setContentAreaFilled(false);
        btn_IOA_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_IOA_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 670, 30, 25));

        btn_IPI_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_IPI_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_IPI_ayuda.setContentAreaFilled(false);
        btn_IPI_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_IPI_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 830, 30, 25));

        btn_Q3R_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_Q3R_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_Q3R_ayuda.setContentAreaFilled(false);
        btn_Q3R_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_Q3R_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 1560, 30, 25));

        btn_YPI_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_YPI_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_YPI_ayuda.setContentAreaFilled(false);
        btn_YPI_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_YPI_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 1260, 30, 25));

        btn_YOA_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_YOA_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_YOA_ayuda.setContentAreaFilled(false);
        btn_YOA_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_YOA_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 1170, 30, 25));

        btn_COA_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_COA_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_COA_ayuda.setContentAreaFilled(false);
        btn_COA_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_COA_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 960, 30, 25));

        btn_Q2C_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_Q2C_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_Q2C_ayuda.setContentAreaFilled(false);
        btn_Q2C_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_Q2C_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 1430, 30, 25));

        btn_Q1H_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_Q1H_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_Q1H_ayuda.setContentAreaFilled(false);
        btn_Q1H_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_Q1H_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 1700, 30, 25));

        btn_Q3M_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_Q3M_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_Q3M_ayuda.setContentAreaFilled(false);
        btn_Q3M_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_Q3M_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 1520, 30, 25));

        btn_Q1C_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_Q1C_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_Q1C_ayuda.setContentAreaFilled(false);
        btn_Q1C_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_Q1C_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 1740, 30, 25));

        btn_QIF_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_QIF_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_QIF_ayuda.setContentAreaFilled(false);
        btn_QIF_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_QIF_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 210, 25, 25));

        lbl_save_titulo1.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_titulo1.setText(" . . .");
        jp_Componentes.add(lbl_save_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 1870, 300, 35));

        chbx_YOA_Pregunta.setText("Titulo");
        jp_Componentes.add(chbx_YOA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1170, 300, 30));

        jsp_CalculoCaudalesDiseno.setViewportView(jp_Componentes);
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
            .addComponent(jsp_CalculoCaudalesDiseno, javax.swing.GroupLayout.PREFERRED_SIZE, 1008, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_close2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btn_close2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jsp_CalculoCaudalesDiseno, javax.swing.GroupLayout.DEFAULT_SIZE, 1972, Short.MAX_VALUE)
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
    private javax.swing.JButton btn_CAR_ayuda;
    private javax.swing.JButton btn_COA_ayuda;
    private javax.swing.JButton btn_IOA_ayuda;
    private javax.swing.JButton btn_IPI_ayuda;
    private javax.swing.JButton btn_Q1C_ayuda;
    private javax.swing.JButton btn_Q1H_ayuda;
    private javax.swing.JButton btn_Q2C_ayuda;
    private javax.swing.JButton btn_Q3M_ayuda;
    private javax.swing.JButton btn_Q3R_ayuda;
    private javax.swing.JButton btn_QAI_ayuda;
    private javax.swing.JButton btn_QCA_ayuda;
    private javax.swing.JButton btn_QET_ayuda;
    private javax.swing.JButton btn_QIF_ayuda;
    private javax.swing.JButton btn_YOA_ayuda;
    private javax.swing.JButton btn_YPI_ayuda;
    private javax.swing.JButton btn_close;
    private javax.swing.JButton btn_close2;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JCheckBox chbx_COA_Pregunta;
    private javax.swing.JCheckBox chbx_IOA_Pregunta;
    private javax.swing.JCheckBox chbx_YOA_Pregunta;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JPanel jp_Componentes;
    private javax.swing.JScrollPane jsp_CalculoCaudalesDiseno;
    private javax.swing.JLabel lbl_CAC_error;
    private javax.swing.JLabel lbl_CAC_titulo1;
    private javax.swing.JLabel lbl_CAC_titulo2;
    private javax.swing.JLabel lbl_CAR_error;
    private javax.swing.JLabel lbl_CAR_titulo1;
    private javax.swing.JLabel lbl_CAR_titulo2;
    private javax.swing.JLabel lbl_CKC_error;
    private javax.swing.JLabel lbl_CKC_titulo1;
    private javax.swing.JLabel lbl_CKC_titulo2;
    private javax.swing.JLabel lbl_CPC_error;
    private javax.swing.JLabel lbl_CPC_titulo1;
    private javax.swing.JLabel lbl_CPC_titulo2;
    private javax.swing.JLabel lbl_CQC_error;
    private javax.swing.JLabel lbl_CQC_titulo1;
    private javax.swing.JLabel lbl_CQC_titulo2;
    private javax.swing.JLabel lbl_IAI_IQ2_error;
    private javax.swing.JLabel lbl_IAI_IQ2_titulo1;
    private javax.swing.JLabel lbl_IAI_IQ2_titulo2;
    private javax.swing.JLabel lbl_IKI_error;
    private javax.swing.JLabel lbl_IKI_titulo1;
    private javax.swing.JLabel lbl_IKI_titulo2;
    private javax.swing.JLabel lbl_IOA_desc;
    private javax.swing.JLabel lbl_IPI_IK2_error;
    private javax.swing.JLabel lbl_IPI_IK2_titulo1;
    private javax.swing.JLabel lbl_IPI_IK2_titulo2;
    private javax.swing.JLabel lbl_IQI_error;
    private javax.swing.JLabel lbl_IQI_titulo1;
    private javax.swing.JLabel lbl_IQI_titulo2;
    private javax.swing.JLabel lbl_IRM_desc;
    private javax.swing.JLabel lbl_KAR_error;
    private javax.swing.JLabel lbl_KAR_titulo1;
    private javax.swing.JLabel lbl_KAR_titulo2;
    private javax.swing.JLabel lbl_Q1C_error;
    private javax.swing.JLabel lbl_Q1C_titulo1;
    private javax.swing.JLabel lbl_Q1C_titulo2;
    private javax.swing.JLabel lbl_Q1H_desc;
    private javax.swing.JLabel lbl_Q1H_error;
    private javax.swing.JLabel lbl_Q1H_titulo1;
    private javax.swing.JLabel lbl_Q1H_titulo2;
    private javax.swing.JLabel lbl_Q1K_error;
    private javax.swing.JLabel lbl_Q1K_titulo1;
    private javax.swing.JLabel lbl_Q1K_titulo2;
    private javax.swing.JLabel lbl_Q2C_desc;
    private javax.swing.JLabel lbl_Q2C_error;
    private javax.swing.JLabel lbl_Q2C_titulo1;
    private javax.swing.JLabel lbl_Q2C_titulo2;
    private javax.swing.JLabel lbl_Q2K_error;
    private javax.swing.JLabel lbl_Q2K_titulo1;
    private javax.swing.JLabel lbl_Q2K_titulo2;
    private javax.swing.JLabel lbl_Q3C_error;
    private javax.swing.JLabel lbl_Q3C_titulo1;
    private javax.swing.JLabel lbl_Q3C_titulo2;
    private javax.swing.JLabel lbl_Q3K_error;
    private javax.swing.JLabel lbl_Q3K_titulo1;
    private javax.swing.JLabel lbl_Q3K_titulo2;
    private javax.swing.JLabel lbl_Q3M_desc;
    private javax.swing.JLabel lbl_Q3M_error;
    private javax.swing.JLabel lbl_Q3M_titulo1;
    private javax.swing.JLabel lbl_Q3M_titulo2;
    private javax.swing.JLabel lbl_QCA_error;
    private javax.swing.JLabel lbl_QCA_titulo1;
    private javax.swing.JLabel lbl_QCA_titulo2;
    private javax.swing.JLabel lbl_QCC_error;
    private javax.swing.JLabel lbl_QCC_titulo1;
    private javax.swing.JLabel lbl_QCC_titulo2;
    private javax.swing.JLabel lbl_QCD_desc;
    private javax.swing.JLabel lbl_QCD_error;
    private javax.swing.JLabel lbl_QCK_error;
    private javax.swing.JLabel lbl_QCK_titulo1;
    private javax.swing.JLabel lbl_QCK_titulo2;
    private javax.swing.JLabel lbl_QCM_error;
    private javax.swing.JLabel lbl_QCM_titulo1;
    private javax.swing.JLabel lbl_QCM_titulo2;
    private javax.swing.JLabel lbl_QEC_QAC_error;
    private javax.swing.JLabel lbl_QEC_QAC_titulo1;
    private javax.swing.JLabel lbl_QEC_QAC_titulo2;
    private javax.swing.JLabel lbl_QEK_QAK_error;
    private javax.swing.JLabel lbl_QEK_QAK_titulo1;
    private javax.swing.JLabel lbl_QEK_QAK_titulo2;
    private javax.swing.JLabel lbl_QEL_QAA_error;
    private javax.swing.JLabel lbl_QEL_QAA_titulo1;
    private javax.swing.JLabel lbl_QEL_QAA_titulo2;
    private javax.swing.JLabel lbl_QET_QAI_error;
    private javax.swing.JLabel lbl_QET_QAI_titulo1;
    private javax.swing.JLabel lbl_QET_QAI_titulo2;
    private javax.swing.JLabel lbl_QIF_desc;
    private javax.swing.JLabel lbl_YAI_error;
    private javax.swing.JLabel lbl_YAI_titulo1;
    private javax.swing.JLabel lbl_YAI_titulo2;
    private javax.swing.JLabel lbl_YKI_error;
    private javax.swing.JLabel lbl_YKI_titulo1;
    private javax.swing.JLabel lbl_YKI_titulo2;
    private javax.swing.JLabel lbl_YPI_error;
    private javax.swing.JLabel lbl_YPI_titulo1;
    private javax.swing.JLabel lbl_YPI_titulo2;
    private javax.swing.JLabel lbl_YQI_error;
    private javax.swing.JLabel lbl_YQI_titulo1;
    private javax.swing.JLabel lbl_YQI_titulo2;
    private javax.swing.JLabel lbl_save_desc;
    private javax.swing.JLabel lbl_save_error;
    private javax.swing.JLabel lbl_save_titulo1;
    private javax.swing.JLabel lbl_titulo1;
    private javax.swing.JLabel lbl_titulo2;
    private javax.swing.JRadioButton rbtn_IRM_Pregunta1;
    private javax.swing.JRadioButton rbtn_IRM_Pregunta2;
    private javax.swing.JRadioButton rbtn_QCD_Pregunta1;
    private javax.swing.JRadioButton rbtn_QCD_Pregunta2;
    private javax.swing.JRadioButton rbtn_QIF_Pregunta1;
    private javax.swing.JRadioButton rbtn_QIF_Pregunta2;
    private javax.swing.JTextField txt_CAC_Pregunta;
    private javax.swing.JTextField txt_CAR_Pregunta;
    private javax.swing.JTextField txt_CKC_Pregunta;
    private javax.swing.JTextField txt_CPC_Pregunta;
    private javax.swing.JTextField txt_CQC_Pregunta;
    private javax.swing.JTextField txt_IAI_Pregunta;
    private javax.swing.JTextField txt_IK2_Pregunta;
    private javax.swing.JTextField txt_IKI_Pregunta;
    private javax.swing.JTextField txt_IPI_Pregunta;
    private javax.swing.JTextField txt_IQ2_Pregunta;
    private javax.swing.JTextField txt_IQI_Pregunta;
    private javax.swing.JTextField txt_KAR_Pregunta;
    private javax.swing.JTextField txt_Q1C_Pregunta;
    private javax.swing.JTextField txt_Q1H_Pregunta;
    private javax.swing.JTextField txt_Q1K_Pregunta;
    private javax.swing.JTextField txt_Q2C_Pregunta;
    private javax.swing.JTextField txt_Q2K_Pregunta;
    private javax.swing.JTextField txt_Q3C_Pregunta;
    private javax.swing.JTextField txt_Q3K_Pregunta;
    private javax.swing.JTextField txt_Q3M_Pregunta;
    private javax.swing.JTextField txt_QAA_Pregunta;
    private javax.swing.JTextField txt_QAC_Pregunta;
    private javax.swing.JTextField txt_QAI_Pregunta;
    private javax.swing.JTextField txt_QAK_Pregunta;
    private javax.swing.JTextField txt_QCA_Pregunta;
    private javax.swing.JTextField txt_QCC_Pregunta;
    private javax.swing.JTextField txt_QCK_Pregunta;
    private javax.swing.JTextField txt_QCM_Pregunta;
    private javax.swing.JTextField txt_QEC_Pregunta;
    private javax.swing.JTextField txt_QEK_Pregunta;
    private javax.swing.JTextField txt_QEL_Pregunta;
    private javax.swing.JTextField txt_QET_Pregunta;
    private javax.swing.JTextField txt_YAI_Pregunta;
    private javax.swing.JTextField txt_YKI_Pregunta;
    private javax.swing.JTextField txt_YPI_Pregunta;
    private javax.swing.JTextField txt_YQI_Pregunta;
    // End of variables declaration//GEN-END:variables
}
