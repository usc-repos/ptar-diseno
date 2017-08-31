package UI;

import BO.Bod;
import DB.Dao;
import java.io.File;
import Componentes.Util;
import javax.swing.JLabel;
import java.text.DateFormat;
import javax.swing.JTextField;
import org.apache.log4j.Logger;
import Componentes.Validaciones;
import Componentes.Configuracion;
import Componentes.Listener_Popup;
import Componentes.Listener_Texto;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.sql.ResultSet;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CalculoCaudalesDiseno extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();
    //private DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
    private static Logger log = Logger.getLogger("DatosEntrada"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.ObtenerKey("rutabasedatos") + File.separator + conf.ObtenerKey("nombrebasedatos"));
    private Validaciones validar = new Validaciones();
    private Bod bod = new Bod("");
    private Listener_Popup Lpopup;
    private Listener_Texto Ltexto;
    Util util = new Util();
    boolean flag_QIF = false; //Guarda el estado del radiobutton
    int flag_QCE_QNL = -1; //Guarda el estado del radiobutton QCE
    boolean flag_IRM_y = false;
    boolean flag_IRM_n = false;
    boolean flag_bajo = false;
    ButtonGroup buttongroup1 = new ButtonGroup();
    ButtonGroup btnGrupQCEQNL = new ButtonGroup();
    ButtonGroup buttongroup3 = new ButtonGroup();
    ButtonGroup btnGrupQCD = new ButtonGroup();
    String titulo1_QEL;
    String titulo2_QEL;
    String titulo1_QAA;
    String titulo2_QAA;
    String titulo1_QET;
    String titulo2_QET;
    String titulo1_QAI;
    String titulo2_QAI;
    String titulo1_QEC;
    String titulo2_QEC;
    String titulo1_QAC;
    String titulo2_QAC;
    String titulo1_QEK;
    String titulo2_QEK;
    String titulo1_QAK;
    String titulo2_QAK;
    String coeficienteICM; //Guardaran los datos de los coeficientes si el usuario no decide editarlos
    String coeficienteICC;
    String valordefectosQ3M;

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

            // - - - - - - Titulo de la sección            
            rs = db.ResultadoSelect("obtenerseccion", "3", null);

            lbl_titulo1.setText(rs.getString("Nombre")); //Título de este jpane
            lbl_titulo2.setText(rs.getString("Mensaje"));

            // - - - - - - Pregunta 1  - - - - - - - - - - - - - - - - - - - -  - - - - - - - - - - - CAR
            rs = db.ResultadoSelect("obtenerpregunta", "3", "CAR");

            bod.minCAR = rs.getDouble("rangomin");
            bod.maxCAR = rs.getDouble("rangomax");
            bod.errorCAR = rs.getString("errormsg");
            lbl_CAR_titulo1.setText(rs.getString("titulo1"));
            lbl_CAR_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_CAR_Pregunta, bod.minCAR, bod.maxCAR, lbl_CAR_error, bod.errorCAR);
            // - - - - - - Pregunta 2 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - - - KAR
            rs = db.ResultadoSelect("obtenerpregunta", "3", "KAR");

            bod.minKAR = rs.getDouble("rangomin");
            bod.maxKAR = rs.getDouble("rangomax");
            bod.errorKAR = rs.getString("errormsg");
            lbl_KAR_titulo1.setText(rs.getString("titulo1"));
            lbl_KAR_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_KAR_Pregunta, bod.minKAR, bod.maxKAR, lbl_KAR_error, bod.errorKAR);
            // - - - - - - Pregunta 3 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QIF         
            rs = db.ResultadoSelect("obtenerpregunta", "3", "QIF");

            buttongroup1.add(rbtn_QIF_Pregunta);
            lbl_QIF_desc.setText(rs.getString("descripcion"));
            rbtn_QIF_Pregunta.setText(rs.getString("titulo1"));
            AsignarPopupBtn(btn_QIF_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 520, 420);

            rbtn_QIF_Pregunta.addActionListener(new ActionListener() { //Listener para el radiobutton
                @Override
                public void actionPerformed(ActionEvent e) {
                    flag_QIF = true;
                    ComprobarCaudalInfiltración();
                }
            });
            // - - - - - - Pregunta 4 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QIA
            rs = db.ResultadoSelect("obtenerpregunta", "3", "QIA");

            buttongroup1.add(rbtn_QIA_Pregunta);
            rbtn_QIA_Pregunta.setText(rs.getString("titulo1"));

            rbtn_QIA_Pregunta.addActionListener(new ActionListener() { //Listener para el radiobutton
                @Override
                public void actionPerformed(ActionEvent e) {
                    flag_QIF = false;
                    ComprobarCaudalInfiltración();
                }
            });
            // - - - - - - Pregunta 5 (Opción 1) - - - - - - - - - - - - - - - - - - - - - - - - - - - - QEL
            rs = db.ResultadoSelect("obtenerpregunta", "3", "QEL");

            bod.minQEL = rs.getDouble("rangomin");
            bod.maxQEL = rs.getDouble("rangomax");
            titulo1_QEL = rs.getString("titulo1");//Variables para cambiar titulos segun la seleccion de QIA/QIF
            titulo2_QEL = rs.getString("titulo2");
            bod.errorQEL = rs.getString("errormsg");
            AsignarTextDoble(txt_QEL_Pregunta, bod.minQEL, bod.maxQEL, lbl_QEL_QAA_error, bod.errorQEL);
            // - - - - - - Pregunta 6 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QAA
            rs = db.ResultadoSelect("obtenerpregunta", "3", "QAA");

            bod.minQAA = rs.getDouble("rangomin");
            bod.maxQAA = rs.getDouble("rangomax");
            titulo1_QAA = rs.getString("titulo1");//Variables para cambiar titulos segun la seleccion de QIA/QIF
            titulo2_QAA = rs.getString("titulo2");
            bod.errorQAA = rs.getString("errormsg");
            AsignarTextDoble(txt_QAA_Pregunta, bod.minQAA, bod.maxQAA, lbl_QEL_QAA_error, bod.errorQAA);
            // - - - - - - Pregunta 7 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QET
            rs = db.ResultadoSelect("obtenerpregunta", "3", "QET");

            bod.minQET = rs.getDouble("rangomin");
            bod.maxQET = rs.getDouble("rangomax");
            titulo1_QET = rs.getString("titulo1");//Variables para cambiar titulos segun la seleccion de QIA/QIF
            titulo2_QET = rs.getString("titulo2");
            bod.errorQET = rs.getString("errormsg");
            txt_QET_Pregunta.setText(rs.getString("valorpordefecto"));
            AsignarPopupBtn(btn_QET_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 350, 350);
            AsignarTextDoble(txt_QET_Pregunta, bod.minQET, bod.maxQET, lbl_QET_QAI_error, bod.errorQET);
            // - - - - - - Pregunta 8 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QAI
            rs = db.ResultadoSelect("obtenerpregunta", "3", "QAI");

            bod.minQAI = rs.getDouble("rangomin");
            bod.maxQAI = rs.getDouble("rangomax");
            titulo1_QAI = rs.getString("titulo1");//Variables para cambiar titulos segun la seleccion de QIA/QIF
            titulo2_QAI = rs.getString("titulo2");
            bod.errorQAI = rs.getString("errormsg");
            AsignarPopupBtn(btn_QAI_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 390);
            AsignarTextDoble(txt_QAI_Pregunta, bod.minQAI, bod.maxQAI, lbl_QET_QAI_error, bod.errorQAI);
            // - - - - - - Pregunta 9 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QEC
            rs = db.ResultadoSelect("obtenerpregunta", "3", "QEC");

            bod.minQEC = rs.getDouble("rangomin");
            bod.maxQEC = rs.getDouble("rangomax");
            titulo1_QEC = rs.getString("titulo1");//Variables para cambiar titulos segun la seleccion de QIA/QIF
            titulo2_QEC = rs.getString("titulo2");
            bod.errorQEC = rs.getString("errormsg");
            AsignarTextDoble(txt_QEC_Pregunta, bod.minQEC, bod.maxQEC, lbl_QEC_QAC_error, bod.errorQEC);

            ListenerCampoTxt(txt_QEC_Pregunta); //Se le agrega un listener a este campo no editable, por si recibe valores para calcular caudales min.,max.,med.
            // - - - - - - Pregunta 10 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QAC
            rs = db.ResultadoSelect("obtenerpregunta", "3", "QAC");

            bod.minQAC = rs.getDouble("rangomin");
            bod.maxQAC = rs.getDouble("rangomax");
            titulo1_QAC = rs.getString("titulo1");//Variables para cambiar titulos segun la seleccion de QIA/QIF
            titulo2_QAC = rs.getString("titulo2");
            bod.errorQAC = rs.getString("errormsg");
            AsignarTextDoble(txt_QAC_Pregunta, bod.minQAC, bod.maxQAC, lbl_QEC_QAC_error, bod.errorQAC);

            ListenerCampoTxt(txt_QAC_Pregunta); //Se le agrega un listener a este campo no editable, por si recibe valores para calcular caudales min.,max.,med.
            // - - - - - - Pregunta 11 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QEK
            rs = db.ResultadoSelect("obtenerpregunta", "3", "QEK");

            bod.minQEK = rs.getDouble("rangomin");
            bod.maxQEK = rs.getDouble("rangomax");
            titulo1_QEK = rs.getString("titulo1");//Variables para cambiar titulos segun la seleccion de QIA/QIF
            titulo2_QEK = rs.getString("titulo2");
            bod.errorQEK = rs.getString("errormsg");
            AsignarTextDoble(txt_QEK_Pregunta, bod.minQEK, bod.maxQEK, lbl_QEK_QAK_error, bod.errorQEK);
            // - - - - - - Pregunta 12 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QAK
            rs = db.ResultadoSelect("obtenerpregunta", "3", "QAK");

            bod.minQAK = rs.getDouble("rangomin");
            bod.maxQAK = rs.getDouble("rangomax");
            titulo1_QAK = rs.getString("titulo1");//Variables para cambiar titulos segun la seleccion de QIA/QIF
            titulo2_QAK = rs.getString("titulo2");
            bod.errorQAK = rs.getString("errormsg");
            AsignarTextDoble(txt_QAK_Pregunta, bod.minQAK, bod.maxQAK, lbl_QEK_QAK_error, bod.errorQAK);
            // - - - - - - Pregunta 13 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QCE
            rs = db.ResultadoSelect("obtenerpregunta", "3", "QCE");

            btnGrupQCEQNL.add(rbtn_QCE_Pregunta);
            lbl_QCE_desc.setText(rs.getString("descripcion"));
            rbtn_QCE_Pregunta.setText(rs.getString("titulo1"));
            AsignarPopupBtn(btn_QCE_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 450, 350);

            rbtn_QCE_Pregunta.addActionListener(new ActionListener() { //Listener para el radiobutton
                @Override
                public void actionPerformed(ActionEvent e) {
                    flag_QCE_QNL = 1;
                    CaudalConexionesErradas();
                }
            });
            // - - - - - - Pregunta 1X - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QCD
            rs = db.ResultadoSelect("obtenerpregunta", "3", "QCD");

            btnGrupQCD.add(rbtn_QCD_Pregunta1);
            btnGrupQCD.add(rbtn_QCD_Pregunta2);
            bod.errorQCD = rs.getString("errormsg");
            rbtn_QCD_Pregunta1.setText(rs.getString("titulo1"));//Variables para cambiar titulos segun la seleccion de QIA/QIF
            rbtn_QCD_Pregunta2.setText(rs.getString("titulo2"));

            rbtn_QCD_Pregunta1.addActionListener(new ActionListener() { //Listener para el radiobutton
                @Override
                public void actionPerformed(ActionEvent e) {
                    flag_QCE_QNL = -1;
                    CaudalConexionesErradas();
                }
            });
            rbtn_QCD_Pregunta2.addActionListener(new ActionListener() { //Listener para el radiobutton
                @Override
                public void actionPerformed(ActionEvent e) {
                    flag_QCE_QNL = 0;
                    CaudalConexionesErradas();
                }
            });
            // - - - - - - Pregunta 14 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QCA
            rs = db.ResultadoSelect("obtenerpregunta", "3", "QCA");

            bod.minQCA = rs.getDouble("rangomin");
            bod.maxQCA = rs.getDouble("rangomax");
            bod.errorQCA = rs.getString("errormsg");
            lbl_QCA_titulo1.setText(rs.getString("titulo1"));
            lbl_QCA_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_QCA_Pregunta, bod.minQCA, bod.maxQCA, lbl_QCA_error, bod.errorQCA);
            // - - - - - - Pregunta 15 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QCM
            rs = db.ResultadoSelect("obtenerpregunta", "3", "QCM");

            bod.minQCM = rs.getDouble("rangomin");
            bod.maxQCM = rs.getDouble("rangomax");
            bod.errorQCM = rs.getString("errormsg");
            lbl_QCM_titulo1.setText(rs.getString("titulo1"));
            lbl_QCM_titulo2.setText(rs.getString("titulo2"));
            AsignarPopupBtn(btn_QCM_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 440, 460);
            AsignarTextDoble(txt_QCM_Pregunta, bod.minQCM, bod.maxQCM, lbl_QCM_error, bod.errorQCM);
            // - - - - - - Pregunta 16 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QCC
            rs = db.ResultadoSelect("obtenerpregunta", "3", "QCC");

            bod.minQCC = rs.getDouble("rangomin");
            bod.maxQCC = rs.getDouble("rangomax");
            bod.errorQCC = rs.getString("errormsg");
            lbl_QCC_titulo1.setText(rs.getString("titulo1"));
            lbl_QCC_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_QCC_Pregunta, bod.minQCC, bod.maxQCC, lbl_QCC_error, bod.errorQCC);

            ListenerCampoTxt(txt_QCC_Pregunta); //Se le agrega un listener a este campo no editable, por si recibe valores para calcular caudales min.,max.,med.
            // - - - - - - Pregunta 17 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QCK
            rs = db.ResultadoSelect("obtenerpregunta", "3", "QCK");

            bod.minQCK = rs.getDouble("rangomin");
            bod.maxQCK = rs.getDouble("rangomax");
            bod.errorQCK = rs.getString("errormsg");
            lbl_QCK_titulo1.setText(rs.getString("titulo1"));
            lbl_QCK_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_QCK_Pregunta, bod.minQCK, bod.maxQCK, lbl_QCK_error, bod.errorQCK);
            // - - - - - - Pregunta 18 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QNL
            rs = db.ResultadoSelect("obtenerpregunta", "3", "QNL");

            btnGrupQCEQNL.add(rbtn_QNL_Pregunta);
            rbtn_QNL_Pregunta.setText(rs.getString("titulo1"));
            AsignarPopupBtn(btn_QNL_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 450, 220);

            rbtn_QNL_Pregunta.addActionListener(new ActionListener() { //Listener para el radiobutton
                @Override
                public void actionPerformed(ActionEvent e) {
                    flag_QCE_QNL = 2;
                    CaudalConexionesErradas();
                }
            });
            // - - - - - - Pregunta 19 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QNC
            rs = db.ResultadoSelect("obtenerpregunta", "3", "QNC");

            bod.minQNC = rs.getDouble("rangomin");
            bod.maxQNC = rs.getDouble("rangomax");
            bod.errorQNC = rs.getString("errormsg");
            lbl_QNC_titulo1.setText(rs.getString("titulo1"));
            lbl_QNC_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_QNC_Pregunta, bod.minQNC, bod.maxQNC, lbl_QNC_error, bod.errorQNC);

            ListenerCampoTxt(txt_QNC_Pregunta); //Se le agrega un listener a este campo no editable, por si recibe valores para calcular caudales min.,max.,med.
            // - - - - - - Pregunta 20 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - QNK
            rs = db.ResultadoSelect("obtenerpregunta", "3", "QNK");

            bod.minQNK = rs.getDouble("rangomin");
            bod.maxQNK = rs.getDouble("rangomax");
            bod.errorQNK = rs.getString("errormsg");
            lbl_QNK_titulo1.setText(rs.getString("titulo1"));
            lbl_QNK_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_QNK_Pregunta, bod.minQNK, bod.maxQNK, lbl_QNK_error, bod.errorQNK);
            // - - - - - - Pregunta 21 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - IOA
            rs = db.ResultadoSelect("obtenerpregunta", "3", "IOA");

            lbl_IOA_desc.setText(rs.getString("descripcion"));
            chbx_IOA_pregunta.setText(rs.getString("titulo1"));
            AsignarPopupBtn(btn_IOA_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 470, 400);

            chbx_IOA_pregunta.addItemListener(new ItemListener() { //Listener para el checkbox
                public void itemStateChanged(ItemEvent e) {
                    OtrosAportesIndustrial();
                }
            });
            // - - - - - - Pregunta 22 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - IRM
            rs = db.ResultadoSelect("obtenerpregunta", "3", "IRM");

            buttongroup3.add(rbtn_IRM_Pregunta1);
            buttongroup3.add(rbtn_IRM_Pregunta2);
            lbl_IRM_desc.setText(rs.getString("descripcion"));
            rbtn_IRM_Pregunta1.setText(rs.getString("titulo1"));
            rbtn_IRM_Pregunta2.setText(rs.getString("titulo2"));

            rbtn_IRM_Pregunta1.addActionListener(new ActionListener() { //Listener para el radiobutton // Si!
                @Override
                public void actionPerformed(ActionEvent e) {
                    flag_IRM_y = true;
                    flag_IRM_n = false;
                    OtrosAportesIndustrial();
                }
            });
            rbtn_IRM_Pregunta2.addActionListener(new ActionListener() { //Listener para el radiobutton //No!
                @Override
                public void actionPerformed(ActionEvent e) {
                    flag_IRM_y = false;
                    flag_IRM_n = true;
                    OtrosAportesIndustrial();
                }
            });
            // - - - - - - Pregunta 23 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - IAI
            rs = db.ResultadoSelect("obtenerpregunta", "3", "IAI");

            bod.minIAI = rs.getInt("rangomin");
            bod.maxIAI = rs.getInt("rangomax");
            bod.errorIAI = rs.getString("errormsg");
            lbl_IAI_titulo1.setText(rs.getString("titulo1"));
            lbl_IAI_titulo2.setText(rs.getString("titulo2"));
            AsignarTextEntero(txt_IAI_Pregunta, bod.minIAI, bod.maxIAI, lbl_IAI_error, bod.errorIAI);
            // - - - - - - Pregunta 24 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - IPI
            rs = db.ResultadoSelect("obtenerpregunta", "3", "IPI");

            bod.minIPI = rs.getDouble("rangomin");
            bod.maxIPI = rs.getDouble("rangomax");
            bod.errorIPI = rs.getString("errormsg");
            lbl_IPI_titulo1.setText(rs.getString("titulo1"));
            lbl_IPI_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_IPI_Pregunta, bod.minIPI, bod.maxIPI, lbl_IPI_error, bod.errorIPI);
            AsignarPopupBtn(btn_IPI_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 450, 320);
            // - - - - - - Pregunta 25 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - IQI
            rs = db.ResultadoSelect("obtenerpregunta", "3", "IQI");

            bod.minIQI = rs.getDouble("rangomin");
            bod.maxIQI = rs.getDouble("rangomax");
            bod.errorIQI = rs.getString("errormsg");
            lbl_IQI_titulo1.setText(rs.getString("titulo1"));
            lbl_IQI_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_IQI_Pregunta, bod.minIQI, bod.maxIQI, lbl_IQI_error, bod.errorIQI);

            ListenerCampoTxt(txt_IQI_Pregunta); //Se le agrega un listener a este campo no editable, por si recibe valores para calcular caudales min.,max.,med.
            // - - - - - - Pregunta 26 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - IKI
            rs = db.ResultadoSelect("obtenerpregunta", "3", "IKI");

            bod.minIKI = rs.getDouble("rangomin");
            bod.maxIKI = rs.getDouble("rangomax");
            bod.errorIKI = rs.getString("errormsg");
            lbl_IKI_titulo1.setText(rs.getString("titulo1"));
            lbl_IKI_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_IKI_Pregunta, bod.minIKI, bod.maxIKI, lbl_IKI_error, bod.errorIKI);
            // - - - - - - Pregunta 27 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - IQ2
            rs = db.ResultadoSelect("obtenerpregunta", "3", "IQ2");

            bod.minIQ2 = rs.getDouble("rangomin");
            bod.maxIQ2 = rs.getDouble("rangomax");
            bod.errorIQ2 = rs.getString("errormsg");
            lbl_IQ2_titulo1.setText(rs.getString("titulo1"));
            lbl_IQ2_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_IQ2_Pregunta, bod.minIQ2, bod.maxIQ2, lbl_IQ2_error, bod.errorIQ2);

            ListenerCampoTxt(txt_IQ2_Pregunta); //Se le agrega un listener a este campo no editable, por si recibe valores para calcular caudales min.,max.,med.
            // - - - - - - Pregunta 28 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - IK2
            rs = db.ResultadoSelect("obtenerpregunta", "3", "IK2");

            bod.minIK2 = rs.getDouble("rangomin");
            bod.maxIK2 = rs.getDouble("rangomax");
            bod.errorIK2 = rs.getString("errormsg");
            lbl_IK2_titulo1.setText(rs.getString("titulo1"));
            lbl_IK2_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_IK2_Pregunta, bod.minIK2, bod.maxIK2, lbl_IK2_error, bod.errorIK2);
            // - - - - - - Pregunta 29 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ICO
            rs = db.ResultadoSelect("obtenerpregunta", "3", "ICO");

            chbx_ICO_Pregunta.setText(rs.getString("titulo1"));

            chbx_ICO_Pregunta.addItemListener(new ItemListener() { //Listener para el checkbox //Habilita edicion de coeficientes
                public void itemStateChanged(ItemEvent e) {
                    EdicionCoeficientes();
                }
            });
            // - - - - - - Pregunta 29 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - IQ3
            rs = db.ResultadoSelect("obtenerpregunta", "3", "IQ3");

            bod.minIQ3 = rs.getDouble("rangomin");
            bod.maxIQ3 = rs.getDouble("rangomax");
            bod.errorIQ3 = rs.getString("errormsg");
            lbl_IQ3_titulo1.setText(rs.getString("titulo1"));
            lbl_IQ3_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_IQ3_Pregunta, bod.minIQ3, bod.maxIQ3, lbl_IQ3_error, bod.errorIQ3);
            // - - - - - - Pregunta 30 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - IK3
            rs = db.ResultadoSelect("obtenerpregunta", "3", "IK3");

            bod.minIK3 = rs.getDouble("rangomin");
            bod.maxIK3 = rs.getDouble("rangomax");
            bod.errorIK3 = rs.getString("errormsg");
            lbl_IK3_titulo1.setText(rs.getString("titulo1"));
            lbl_IK3_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_IK3_Pregunta, bod.minIK3, bod.maxIK3, lbl_IK3_error, bod.errorIK3);
            // - - - - - - Pregunta 31 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ICM
            rs = db.ResultadoSelect("obtenerpregunta", "3", "ICM");

            bod.minICM = rs.getDouble("rangomin");
            bod.maxICM = rs.getDouble("rangomax");
            bod.errorICM = rs.getString("errormsg");
            lbl_ICM_titulo1.setText(rs.getString("titulo1"));
            lbl_ICM_titulo2.setText(rs.getString("titulo2"));
            coeficienteICM = rs.getString("valorpordefecto");
            txt_ICM_Pregunta.setText(coeficienteICM);
            AsignarTextDoble(txt_ICM_Pregunta, bod.minICM, bod.maxICM, lbl_ICM_error, bod.errorICM);
            AsignarPopupBtn(btn_ICM_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 190);
            // - - - - - - Pregunta 32 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - IQ1
            rs = db.ResultadoSelect("obtenerpregunta", "3", "IQ1");

            bod.minIQ1 = rs.getDouble("rangomin");
            bod.maxIQ1 = rs.getDouble("rangomax");
            bod.errorIQ1 = rs.getString("errormsg");
            lbl_IQ1_titulo1.setText(rs.getString("titulo1"));
            lbl_IQ1_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_IQ1_Pregunta, bod.minIQ1, bod.maxIQ1, lbl_IQ1_error, bod.errorIQ1);
            // - - - - - - Pregunta 33 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - IK1
            rs = db.ResultadoSelect("obtenerpregunta", "3", "IK1");

            bod.minIK1 = rs.getDouble("rangomin");
            bod.maxIK1 = rs.getDouble("rangomax");
            bod.errorIK1 = rs.getString("errormsg");
            lbl_IK1_titulo1.setText(rs.getString("titulo1"));
            lbl_IK1_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_IK1_Pregunta, bod.minIK1, bod.maxIK1, lbl_IK1_error, bod.errorIK1);
            // - - - - - - Pregunta 34 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ICC
            rs = db.ResultadoSelect("obtenerpregunta", "3", "ICC");

            bod.minICC = rs.getDouble("rangomin");
            bod.maxICC = rs.getDouble("rangomax");
            bod.errorICC = rs.getString("errormsg");
            lbl_ICC_titulo1.setText(rs.getString("titulo1"));
            lbl_ICC_titulo2.setText(rs.getString("titulo2"));
            coeficienteICC = rs.getString("valorpordefecto");
            txt_ICC_Pregunta.setText(coeficienteICC);
            AsignarTextDoble(txt_ICC_Pregunta, bod.minICC, bod.maxICC, lbl_ICC_error, bod.errorICC);
            AsignarPopupBtn(btn_ICC_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 390, 150);
            // - - - - - - Pregunta 35 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - COA
            rs = db.ResultadoSelect("obtenerpregunta", "3", "COA");

            chbx_COA_Pregunta.setText(rs.getString("titulo1"));
            AsignarPopupBtn(btn_COA_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 500, 320);

            chbx_COA_Pregunta.addItemListener(new ItemListener() { //Listener para el checkbox
                public void itemStateChanged(ItemEvent e) {
                    OtrosAportesComercial();
                }
            });
            // - - - - - - Pregunta 36 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - CAC
            rs = db.ResultadoSelect("obtenerpregunta", "3", "CAC");

            bod.minCAC = rs.getDouble("rangomin");
            bod.maxCAC = rs.getDouble("rangomax");
            bod.errorCAC = rs.getString("errormsg");
            lbl_CAC_titulo1.setText(rs.getString("titulo1"));
            lbl_CAC_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_CAC_Pregunta, bod.minCAC, bod.maxCAC, lbl_CAC_error, bod.errorCAC);
            // - - - - - - Pregunta 37 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - CPC
            rs = db.ResultadoSelect("obtenerpregunta", "3", "CPC");

            bod.minCPC = rs.getDouble("rangomin");
            bod.maxCPC = rs.getDouble("rangomax");
            bod.errorCPC = rs.getString("errormsg");
            lbl_CPC_titulo1.setText(rs.getString("titulo1"));
            lbl_CPC_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_CPC_Pregunta, bod.minCPC, bod.maxCPC, lbl_CPC_error, bod.errorCPC);
            AsignarPopupBtn(btn_CPC_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 320, 330);
            // - - - - - - Pregunta 38 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - CQC
            rs = db.ResultadoSelect("obtenerpregunta", "3", "CQC");

            bod.minCQC = rs.getDouble("rangomin");
            bod.maxCQC = rs.getDouble("rangomax");
            bod.errorCQC = rs.getString("errormsg");
            lbl_CQC_titulo1.setText(rs.getString("titulo1"));
            lbl_CQC_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_CQC_Pregunta, bod.minCQC, bod.maxCQC, lbl_CQC_error, bod.errorCQC);

            ListenerCampoTxt(txt_CQC_Pregunta); //Se le agrega un listener a este campo no editable, por si recibe valores para calcular caudales min.,max.,med.
            // - - - - - - Pregunta 39 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - CKC
            rs = db.ResultadoSelect("obtenerpregunta", "3", "CKC");

            bod.minCKC = rs.getDouble("rangomin");
            bod.maxCKC = rs.getDouble("rangomax");
            bod.errorCKC = rs.getString("errormsg");
            lbl_CKC_titulo1.setText(rs.getString("titulo1"));
            lbl_CKC_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_CKC_Pregunta, bod.minCKC, bod.maxCKC, lbl_CKC_error, bod.errorCKC);
            // - - - - - - Pregunta 40 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - YOA
            rs = db.ResultadoSelect("obtenerpregunta", "3", "YOA");

            chbx_YOA_Pregunta.setText(rs.getString("titulo1"));
            AsignarPopupBtn(btn_YOA_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 420, 330);

            chbx_YOA_Pregunta.addItemListener(new ItemListener() { //Listener para el checkbox Institucional
                public void itemStateChanged(ItemEvent e) {
                    OtrosAportesInstitucional();
                }
            });
            // - - - - - - Pregunta 41 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - YAI
            rs = db.ResultadoSelect("obtenerpregunta", "3", "YAI");

            bod.minYAI = rs.getDouble("rangomin");
            bod.maxYAI = rs.getDouble("rangomax");
            bod.errorYAI = rs.getString("errormsg");
            lbl_YAI_titulo1.setText(rs.getString("titulo1"));
            lbl_YAI_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_YAI_Pregunta, bod.minYAI, bod.maxYAI, lbl_YAI_error, bod.errorYAI);
            // - - - - - - Pregunta 42 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - YPI
            rs = db.ResultadoSelect("obtenerpregunta", "3", "YPI");

            bod.minYPI = rs.getDouble("rangomin");
            bod.maxYPI = rs.getDouble("rangomax");
            bod.errorYPI = rs.getString("errormsg");
            lbl_YPI_titulo1.setText(rs.getString("titulo1"));
            lbl_YPI_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_YPI_Pregunta, bod.minYPI, bod.maxYPI, lbl_YPI_error, bod.errorYPI);
            AsignarPopupBtn(btn_YPI_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 320, 360);
            // - - - - - - Pregunta 43 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - YQI
            rs = db.ResultadoSelect("obtenerpregunta", "3", "YQI");

            bod.minYQI = rs.getDouble("rangomin");
            bod.maxYQI = rs.getDouble("rangomax");
            bod.errorYQI = rs.getString("errormsg");
            lbl_YQI_titulo1.setText(rs.getString("titulo1"));
            lbl_YQI_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_YQI_Pregunta, bod.minYQI, bod.maxYQI, lbl_YQI_error, bod.errorYQI);

            ListenerCampoTxt(txt_YQI_Pregunta); //Se le agrega un listener a este campo no editable, por si recibe valores para calcular caudales min.,max.,med.
            // - - - - - - Pregunta 44 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - YKI
            rs = db.ResultadoSelect("obtenerpregunta", "3", "YKI");

            bod.minYKI = rs.getDouble("rangomin");
            bod.maxYKI = rs.getDouble("rangomax");
            bod.errorYKI = rs.getString("errormsg");
            lbl_YKI_titulo1.setText(rs.getString("titulo1"));
            lbl_YKI_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_YKI_Pregunta, bod.minYKI, bod.maxYKI, lbl_YKI_error, bod.errorYKI);
            // - - - - - - Pregunta 45 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q2C
            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q2C"); //El cálculo se Q2C se da al guardar!

            bod.minQ2C = rs.getDouble("rangomin");
            bod.maxQ2C = rs.getDouble("rangomax");
            bod.errorQ2C = rs.getString("errormsg");
            lbl_Q2C_titulo1.setText(rs.getString("titulo1"));
            lbl_Q2C_titulo2.setText(rs.getString("titulo2"));
            lbl_Q2C_desc.setText(rs.getString("descripcion"));
            AsignarTextDoble(txt_Q2C_Pregunta, bod.minQ2C, bod.maxQ2C, lbl_Q2C_error, bod.errorQ2C);
            AsignarPopupBtn(btn_Q2C_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 490, 410);
            // - - - - - - Pregunta 46 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q2K
            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q2K"); //El cálculo se Q2K se da al guardar!

            bod.minQ2K = rs.getDouble("rangomin");
            bod.maxQ2K = rs.getDouble("rangomax");
            bod.errorQ2K = rs.getString("errormsg");
            lbl_Q2K_titulo1.setText(rs.getString("titulo1"));
            lbl_Q2K_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_Q2K_Pregunta, bod.minQ2K, bod.maxQ2K, lbl_Q2K_error, bod.errorQ2K);
            // - - - - - - Pregunta 47 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q3M
            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q3M");

            bod.minQ3M = rs.getDouble("rangomin");
            bod.maxQ3M = rs.getDouble("rangomax");
            bod.errorQ3M = rs.getString("errormsg");
            lbl_Q3M_titulo1.setText(rs.getString("titulo1"));
            lbl_Q3M_titulo2.setText(rs.getString("titulo2"));
            lbl_Q3M_desc.setText(rs.getString("descripcion"));
            valordefectosQ3M = rs.getString("valorpordefecto");
            txt_Q3M_Pregunta.setText(valordefectosQ3M);
            AsignarTextDoble(txt_Q3M_Pregunta, bod.minQ3M, bod.maxQ3M, lbl_Q3M_error, bod.errorQ3M);
            AsignarPopupBtn(btn_Q3M_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 490, 410);

            ListenerCampoTxt(txt_Q3M_Pregunta); //Se le agrega un listener a este campo no editable, por si recibe valores para calcular caudales min.,max.,med.
            // - - - - - - Pregunta 48 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q3R
            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q3R");

            bod.errorQ3R = rs.getString("errormsg");
            chbx_Q3R_Pregunta.setText(rs.getString("titulo1"));
            AsignarPopupBtn(btn_Q3R_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 560, 550);

            chbx_Q3R_Pregunta.addItemListener(new ItemListener() { //Listener para el checkbox Institucional
                public void itemStateChanged(ItemEvent e) {
                    CaudalMaximoDiario();
                }
            });
            // - - - - - - Pregunta 49 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q3C
            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q3C");

            bod.minQ3C = rs.getDouble("rangomin");
            bod.maxQ3C = rs.getDouble("rangomax");
            bod.errorQ3C = rs.getString("errormsg");
            lbl_Q3C_titulo1.setText(rs.getString("titulo1"));
            lbl_Q3C_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_Q3C_Pregunta, bod.minQ3C, bod.maxQ3C, lbl_Q3C_error, bod.errorQ3C);
            // - - - - - - Pregunta 50 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q3K
            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q3K");

            bod.minQ3K = rs.getDouble("rangomin");
            bod.maxQ3K = rs.getDouble("rangomax");
            bod.errorQ3K = rs.getString("errormsg");
            lbl_Q3K_titulo1.setText(rs.getString("titulo1"));
            lbl_Q3K_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_Q3K_Pregunta, bod.minQ3K, bod.maxQ3K, lbl_Q3K_error, bod.errorQ3K);
            // - - - - - - Pregunta 51 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q1H
            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q1H");

            bod.minQ1H = rs.getDouble("rangomin");
            bod.maxQ1H = rs.getDouble("rangomax");
            bod.errorQ1H = rs.getString("errormsg");
            lbl_Q1H_titulo1.setText(rs.getString("titulo1"));
            lbl_Q1H_titulo2.setText(rs.getString("titulo2"));
            lbl_Q1H_desc.setText(rs.getString("descripcion"));
            txt_Q1H_Pregunta.setText(rs.getString("valorpordefecto"));
            AsignarTextDoble(txt_Q1H_Pregunta, bod.minQ1H, bod.maxQ1H, lbl_Q1H_error, bod.errorQ1H);
            AsignarPopupBtn(btn_Q1H_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 420, 340);

            ListenerCampoTxt(txt_Q1H_Pregunta); //Se le agrega un listener a este campo no editable, por si recibe valores para calcular caudales min.,max.,med.
            // - - - - - - Pregunta 52 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q1C
            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q1C");

            bod.minQ1C = rs.getDouble("rangomin");
            bod.maxQ1C = rs.getDouble("rangomax");
            bod.errorQ1C = rs.getString("errormsg");
            lbl_Q1C_titulo1.setText(rs.getString("titulo1"));
            lbl_Q1C_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_Q1C_Pregunta, bod.minQ1C, bod.maxQ1C, lbl_Q1C_error, bod.errorQ1C);
            AsignarPopupBtn(btn_Q1C_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 280);
            // - - - - - - Pregunta 53 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q1K
            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q1K");

            bod.minQ1K = rs.getDouble("rangomin");
            bod.maxQ1K = rs.getDouble("rangomax");
            bod.errorQ1K = rs.getString("errormsg");
            lbl_Q1K_titulo1.setText(rs.getString("titulo1"));
            lbl_Q1K_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_Q1K_Pregunta, bod.minQ1K, bod.maxQ1K, lbl_Q1K_error, bod.errorQ1K);

            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
            // Si Bod cargó datos de Calculo de Caudales de Diseno desde la BD, porque estaba editada, se procede a llenar casillas con datos
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

            flag_bajo = bod.getNCR().trim().toUpperCase().equals("BAJO"); // REGLA: Si el nivel de complejidad es bajo, deben aparecer las dos opciones de cálculo, de lo contrario aparece por defecto el Área Servida.

            if (bod.EditCalculoCaudalesDiseno) {
                txt_CAR_Pregunta.setText(bod.getCAR());
                txt_KAR_Pregunta.setText(bod.getKAR());

                if (bod.getQIF() > 0) {
                    flag_QIF = true;
                    rbtn_QIF_Pregunta.setSelected(true);
                    txt_QEL_Pregunta.setText(bod.getQEL());
                    txt_QET_Pregunta.setText(bod.getQET());
                    txt_QEC_Pregunta.setText(bod.getQEC());
                    txt_QEK_Pregunta.setText(bod.getQEK());
                }

                if (bod.getQIA() > 0) {
                    flag_QIF = false;
                    rbtn_QIA_Pregunta.setSelected(true);
                    txt_QAA_Pregunta.setText(bod.getQAA());
                    txt_QAI_Pregunta.setText(bod.getQAI());
                    txt_QAC_Pregunta.setText(bod.getQAC());
                    txt_QAK_Pregunta.setText(bod.getQAK());
                }
                ComprobarCaudalInfiltración();
                //--------------------------------------------------------------------------------- Llenar: Caudal por conexiones erradas (QCE)               

                flag_QCE_QNL = 0; //Ocultar todo 

                if (bod.getQCD() > 0) {

                    rbtn_QCD_Pregunta1.setSelected(true);

                    if (!flag_bajo && bod.getQNL() > 0) { //Error no puede QNL estar editado //Regla (ver arriba)
                        lbl_QCD_error.setText("Error!"); //Todo: evitar texto quemado
                    }

                    txt_QCA_Pregunta.setText(bod.getQCA());
                    txt_QCM_Pregunta.setText(bod.getQCM());

                    if (bod.getQCE() > 0) {
                        flag_QCE_QNL = 1;
                        rbtn_QCE_Pregunta.setSelected(true);
                        txt_QCC_Pregunta.setText(bod.getQCC());
                        txt_QCK_Pregunta.setText(bod.getQCK());
                    }

                    if (bod.getQNL() > 0) {
                        flag_QCE_QNL = 2;
                        rbtn_QNL_Pregunta.setSelected(true);
                        txt_QNC_Pregunta.setText(bod.getQNC());
                        txt_QNK_Pregunta.setText(bod.getQNK());
                    }

                } else {
                    rbtn_QCD_Pregunta2.setSelected(true);
                }
                CaudalConexionesErradas();
                //--------------------------------------------------------------------------------- Llenar: Otros aportes industrial
                if (bod.getIOA() > 0) {//7800
                    chbx_IOA_pregunta.setSelected(true);

                    if (bod.getIRM() > 0) { //Opcion 'Si'
                        rbtn_IRM_Pregunta1.setSelected(true);
                        flag_IRM_y = true;
                        flag_IRM_n = false;
                        OtrosAportesIndustrial(); //flag_IRM = false, check en 'No'                        
                        txt_IQ2_Pregunta.setText(bod.getIQ2());
                        txt_IK2_Pregunta.setText(bod.getIK2());

                        if (bod.getICO() > 0) {
                            txt_ICM_Pregunta.setEditable(true);
                            txt_ICC_Pregunta.setEditable(true);
                        }
                        txt_IQ3_Pregunta.setText(bod.getIQ3());
                        txt_IK3_Pregunta.setText(bod.getIK3());
                        txt_IQ1_Pregunta.setText(bod.getIQ1());
                        txt_IK1_Pregunta.setText(bod.getIK1());
                        txt_ICM_Pregunta.setText(bod.getICM());
                        txt_ICC_Pregunta.setText(bod.getICC());
                    } else { //Opcion 'No'
                        rbtn_IRM_Pregunta2.setSelected(true);
                        flag_IRM_y = false;
                        flag_IRM_n = true;
                        OtrosAportesIndustrial();
                        txt_IAI_Pregunta.setText(bod.getIAI());
                        txt_IPI_Pregunta.setText(bod.getIPI());
                        txt_IQI_Pregunta.setText(bod.getIQI());
                        txt_IKI_Pregunta.setText(bod.getIKI());
                    }
                } else {
                    chbx_IOA_pregunta.setSelected(false);
                    OtrosAportesIndustrial();
                }
                if (bod.getICO() > 0) { //Si esta habilitada la opcion de cambiar coeficientes
                    txt_ICM_Pregunta.setText(bod.getICM());
                    txt_ICC_Pregunta.setText(bod.getICC());
                }
                //--------------------------------------------------------------------------------- Llenar: Otros aportes Comercial
                if (bod.getCOA() > 0) {
                    chbx_COA_Pregunta.setSelected(true);
                    txt_CAC_Pregunta.setText(bod.getCAC());
                    txt_CPC_Pregunta.setText(bod.getCPC());
                    txt_CQC_Pregunta.setText(bod.getCQC());
                    txt_CKC_Pregunta.setText(bod.getCKC());
                } else {
                    chbx_COA_Pregunta.setSelected(false);
                }
                OtrosAportesComercial();
                //--------------------------------------------------------------------------------- Llenar: Otros aportes Institucional
                if (bod.getYOA() > 0) { //Si esta habilitada la opcion de Otros Aportes Institucional
                    chbx_YOA_Pregunta.setSelected(true);
                    txt_YAI_Pregunta.setText(bod.getYAI());
                    txt_YPI_Pregunta.setText(bod.getYPI());
                    txt_YQI_Pregunta.setText(bod.getYQI());
                    txt_YKI_Pregunta.setText(bod.getYKI());
                } else {
                    chbx_YOA_Pregunta.setSelected(false);
                }
                OtrosAportesInstitucional();
                //--------------------------------------------------------------------------------- Caudal medio diario (solo se calcula en 'Guardar')
                txt_Q2C_Pregunta.setText(bod.getQ2C());
                txt_Q2K_Pregunta.setText(bod.getQ2K());
                //--------------------------------------------------------------------------------- Caudal max diario  
                txt_Q3M_Pregunta.setText(bod.getQ3M());

                if (bod.getQ3R() > 0) {
                    chbx_Q3R_Pregunta.setSelected(true);
                }
                txt_Q3C_Pregunta.setText(bod.getQ3Cs());
                txt_Q3K_Pregunta.setText(bod.getQ3K());
                //--------------------------------------------------------------------------------- Caudal min diario
                txt_Q1H_Pregunta.setText(bod.getQ1H());
                txt_Q1C_Pregunta.setText(bod.getQ1C());
                txt_Q1K_Pregunta.setText(bod.getQ1K());

            } else {//Como no hay datos precargados de la BD, Se procede a hacer otros calculos necesarios que deben mostrarse al cargar la pagina

                txt_CAR_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(bod.CalcularCAR())), "#.##"));//CAR ,se genera pero no se comprueba
                txt_KAR_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(bod.CalcularKAR())), "#.##"));//KAR ,se genera pero no se comprueba

                ComprobarCaudalInfiltración(); //Oculta algunos y aqui abajo coulto los demas

                txt_QAA_Pregunta.setVisible(false);
                txt_QAI_Pregunta.setVisible(false);
                btn_QAI_ayuda.setVisible(false);
                lbl_QEL_QAA_error.setVisible(false);
                lbl_QET_QAI_error.setVisible(false);
                lbl_QEL_QAA_titulo1.setVisible(false);
                lbl_QEL_QAA_titulo2.setVisible(false);
                lbl_QET_QAI_titulo1.setVisible(false);
                lbl_QET_QAI_titulo2.setVisible(false);//----

                txt_QAA_Pregunta.setVisible(false); //Se deshabilita hasta que no se haya escogido algun radiobutton
                txt_QAI_Pregunta.setVisible(false);
                flag_IRM_n = false;
                flag_IRM_y = false;

                flag_QCE_QNL = 0; //Caudal por conexiones erradas Ocultar todo  
                CaudalConexionesErradas();//----

                OtrosAportesIndustrial(); //flag_IRM_y y flag_IRM_n = false  Ocultar componentes de 'No' y 'Si'
                OtrosAportesComercial(); //Como es la primera vez, oculta todos sus relativos
                OtrosAportesInstitucional();
            }

        } catch (Exception ex) {
            log.error("Error en InicializarComponentes " + ex.getMessage());
            ErrorGeneral(); // Ha ocurrido algun error en los datos o conexión, se debe revisar log.
        } finally {
            db.close(); //Se cierra la conexiòn
        }
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

        lbl_save_error.setText("");
        lbl_save_error2.setText("");

        if (!bod.setCAR(bod.CalcularCAR(), bod.minCAR, bod.maxCAR)) {
            lbl_save_error2.setText("Punto 1, Se deben corregir los datos anteriores (proyección poblacional)");
            lbl_save_error.setText(bod.errorCAR);
            return false;
        }

        if (!bod.setKAR(bod.CalcularKAR(), bod.minKAR, bod.maxKAR)) {
            lbl_save_error2.setText("Punto 2, Se deben corregir los datos anteriores (proyección poblacional)");
            lbl_save_error.setText(bod.errorKAR);
            return false;
        }

        if (!rbtn_QIF_Pregunta.isSelected() && !rbtn_QIA_Pregunta.isSelected()) {
            lbl_save_error2.setText("Caudal de infiltración (Qinf), debe escoger una opción"); //Todo: los mensajes de error se deben tomar de la bd de QIF o QIA            
            return false;
        }

        if (rbtn_QIF_Pregunta.isSelected()) {

            bod.setQIF(1);
            bod.setQIA(0);

            if (!bod.setQEL(txt_QEL_Pregunta.getText(), bod.minQEL, bod.maxQEL)) {
                lbl_save_error2.setText("Caudal de infiltración (Qinf), Punto 1");
                lbl_save_error.setText(bod.errorQEL);
                return false;
            }

            if (!bod.setQET(txt_QET_Pregunta.getText(), bod.minQET, bod.maxQET)) {
                lbl_save_error2.setText("Caudal de infiltración (Qinf), Punto 2");
                lbl_save_error.setText(bod.errorQET);
                return false;
            }

            if (!bod.setQEC(txt_QEC_Pregunta.getText(), bod.minQEC, bod.maxQEC)) {
                lbl_save_error2.setText("Caudal de infiltración (Qinf), Punto 3");
                lbl_save_error.setText(bod.errorQEC);
                return false;
            }

            if (!bod.setQEK(txt_QEK_Pregunta.getText(), bod.minQEK, bod.maxQEK)) {
                lbl_save_error2.setText("Caudal de infiltración (Qinf), Punto 4");
                lbl_save_error.setText(bod.errorQEK);
                return false;
            }
        } else { //Else = rbtn_QIA_Pregunta.isSelected

            bod.setQIF(0);
            bod.setQIA(1);

            if (!bod.setQAA(txt_QAA_Pregunta.getText(), bod.minQAA, bod.maxQAA)) {
                lbl_save_error2.setText("Caudal de infiltración (Qinf), Punto 1");
                lbl_save_error.setText(bod.errorQAA);
                return false;
            }

            if (!bod.setQAI(txt_QAI_Pregunta.getText(), bod.minQAI, bod.maxQAI)) {
                lbl_save_error2.setText("Caudal de infiltración (Qinf), Punto 2");
                lbl_save_error.setText(bod.errorQAI);
                return false;
            }

            if (!bod.setQAC(txt_QAC_Pregunta.getText(), bod.minQAC, bod.maxQAC)) {
                lbl_save_error2.setText("Caudal de infiltración (Qinf), Punto 3");
                lbl_save_error.setText(bod.errorQAC);
                return false;
            }

            if (!bod.setQAK(txt_QAK_Pregunta.getText(), bod.minQAK, bod.maxQAK)) {
                lbl_save_error2.setText("Caudal de infiltración (Qinf), Punto 4");
                lbl_save_error.setText(bod.errorQAK);
                return false;
            }
        }
        //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - INICIA Caudal por conexiones erradas  

        if (!rbtn_QCD_Pregunta1.isSelected() && !rbtn_QCD_Pregunta2.isSelected()) {
            lbl_save_error2.setText("Caudal por conexiones erradas (Qce), debe escoger si/no");
            return false;
        }

        if (rbtn_QCD_Pregunta1.isSelected()) {

            bod.setQCD(1);

            if (!rbtn_QCE_Pregunta.isSelected() && !rbtn_QNL_Pregunta.isSelected()) {
                lbl_save_error2.setText("Caudal por conexiones erradas (Qce), debe escoger una opción"); //Todo: los mensajes de error se deben tomar de la bd de QIF o QIA            
                return false;
            }

            if (rbtn_QCE_Pregunta.isSelected()) {

                bod.setQCE(1);
                bod.setQNL(0);

                if (!bod.setQCA(txt_QCA_Pregunta.getText(), bod.minQCA, bod.maxQCA)) {
                    lbl_save_error2.setText("Punto 5");
                    lbl_save_error.setText(bod.errorQCA);
                    return false;
                }

                if (!bod.setQCM(txt_QCM_Pregunta.getText(), bod.minQCM, bod.maxQCM)) {
                    lbl_save_error2.setText("Punto 6");
                    lbl_save_error.setText(bod.errorQCM);
                    return false;
                }

                if (!bod.setQCC(txt_QCC_Pregunta.getText(), bod.minQCC, bod.maxQCC)) {
                    lbl_save_error2.setText("Punto 7");
                    lbl_save_error.setText(bod.errorQCC);
                    return false;
                }

                if (!bod.setQCK(txt_QCK_Pregunta.getText(), bod.minQCK, bod.maxQCK)) {
                    lbl_save_error2.setText("Punto 8");
                    lbl_save_error.setText(bod.errorQCK);
                    return false;
                }
            } else {//else = rbtn_QNL_Pregunta.isSelected()
                bod.setQCE(0);
                bod.setQNL(1);

                if (!bod.setQNC(txt_QNC_Pregunta.getText(), bod.minQNC, bod.maxQNC)) {
                    lbl_save_error2.setText("Punto 7");
                    lbl_save_error.setText(bod.errorQNC);
                    return false;
                }

                if (!bod.setQNK(txt_QNK_Pregunta.getText(), bod.minQNK, bod.maxQNK)) {
                    lbl_save_error2.setText("Punto 8");
                    lbl_save_error.setText(bod.errorQNK);
                    return false;
                }
            }
        } else {
            bod.setQCD(0);
            bod.setQCE(0);
            bod.setQNL(0);
            bod.setQCA("0", 0, 1);
            bod.setQCM("0", 0, 1);
            bod.setQCC("0", 0, 1);
            bod.setQCK("0", 0, 1);
            bod.setQNC("0", 0, 1);
            bod.setQNK("0", 0, 1);
        }//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - * FIN Caudal por conexiones erradas

        if (chbx_IOA_pregunta.isSelected()) { //Industrial  ,11      7800

            bod.setIOA(1);

            if (!rbtn_IRM_Pregunta1.isSelected() && !rbtn_IRM_Pregunta2.isSelected()) {
                lbl_save_error2.setText("Otros aportes Industrial, debe escoger opción s/n Otros aportes...Industrial"); //Todo: los mensajes de error se deben tomar de la bd de QIF o QIA            
                return false;
            }

            if (rbtn_IRM_Pregunta2.isSelected()) { //No

                bod.setIRM(0);

                if (!bod.setIAI(txt_IAI_Pregunta.getText(), bod.minIAI, bod.maxIAI)) {
                    lbl_save_error2.setText("Otros aportes Industrial, Punto 1");
                    lbl_save_error.setText(bod.errorIAI);
                    return false;
                }

                if (!bod.setIPI(txt_IPI_Pregunta.getText(), bod.minIPI, bod.maxIPI)) {
                    lbl_save_error2.setText("Otros aportes Industrial, Punto 2");
                    lbl_save_error.setText(bod.errorIPI);
                    return false;
                }

                if (!bod.setIQI(txt_IQI_Pregunta.getText(), bod.minIQI, bod.maxIQI)) {
                    lbl_save_error2.setText("Otros aportes Industrial, Punto 3");//Todo, remplazar   'punto'
                    lbl_save_error.setText(bod.errorIQI);
                    return false;
                }

                if (!bod.setIKI(txt_IKI_Pregunta.getText(), bod.minIKI, bod.maxIKI)) {
                    lbl_save_error2.setText("Otros aportes Industrial, Punto 4");
                    lbl_save_error.setText(bod.errorIKI);
                    return false;
                }

            } else {//Si rbtn_IRM_Pregunta1.isSelected()

                bod.setIRM(1);

                if (!bod.setIQ2(txt_IQ2_Pregunta.getText(), bod.minIQ2, bod.maxIQ2)) {
                    lbl_save_error2.setText("Otros aportes Industrial, Punto 1");
                    lbl_save_error.setText(bod.errorIQ2);
                    return false;
                }

                if (!bod.setIK2(txt_IK2_Pregunta.getText(), bod.minIK2, bod.maxIK2)) {
                    lbl_save_error2.setText("Otros aportes Industrial, Punto 2");
                    lbl_save_error.setText(bod.errorIK2);
                    return false;
                }

                if (chbx_ICO_Pregunta.isSelected()) {
                    bod.setICO(1);
                } else {
                    bod.setICO(0);
                }

                if (!bod.setIQ3(txt_IQ3_Pregunta.getText(), bod.minIQ3, bod.maxIQ3)) {
                    lbl_save_error2.setText("Otros aportes Industrial, Punto 3");
                    lbl_save_error.setText(bod.errorIQ3);
                    return false;
                }

                if (!bod.setIK3(txt_IK3_Pregunta.getText(), bod.minIK3, bod.maxIK3)) {
                    lbl_save_error2.setText("Otros aportes Industrial, Punto 4");
                    lbl_save_error.setText(bod.errorIK3);
                    return false;
                }

                if (!bod.setICM(txt_ICM_Pregunta.getText(), bod.minICM, bod.maxICM)) {
                    lbl_save_error2.setText("Otros aportes Industrial, Punto 5");
                    lbl_save_error.setText(bod.errorICM);
                    return false;
                }

                if (!bod.setIQ1(txt_IQ1_Pregunta.getText(), bod.minIQ1, bod.maxIQ1)) {
                    lbl_save_error2.setText("Otros aportes Industrial, Punto 6");
                    lbl_save_error.setText(bod.errorIQ1);
                    return false;
                }

                if (!bod.setIK1(txt_IK1_Pregunta.getText(), bod.minIK1, bod.maxIK1)) {
                    lbl_save_error2.setText("Otros aportes Industrial, Punto 7");
                    lbl_save_error.setText(bod.errorIK1);
                    return false;
                }

                if (!bod.setICC(txt_ICC_Pregunta.getText(), bod.minICC, bod.maxICC)) {
                    lbl_save_error2.setText("Otros aportes Industrial, Punto 8");
                    lbl_save_error.setText(bod.errorICC);
                    return false;
                }
            }
        } else {//todo se coloca en cero, pues no es obligatoria

            bod.setIOA(0);
            bod.setIRM(0);
            bod.setIAI("0", 0, 1);
            bod.setIPI("0", 0, 1);
            bod.setIQI("0", 0, 1);
            bod.setIKI("0", 0, 1);
            bod.setIQ2("0", 0, 1);
            bod.setIK2("0", 0, 1);
            bod.setICO(0);
            bod.setIQ3("0", 0, 1);
            bod.setIK3("0", 0, 1);
            bod.setICM("0", 0, 1);
            bod.setIQ1("0", 0, 1);
            bod.setIK1("0", 0, 1);
            bod.setICC("0", 0, 1);
        }

        if (chbx_COA_Pregunta.isSelected()) { //Otros aportes Comercial, 
            bod.setCOA(1);
            if (!bod.setCAC(txt_CAC_Pregunta.getText(), bod.minCAC, bod.maxCAC)) {
                lbl_save_error2.setText("Otros aportes Comercial, Punto 1");
                lbl_save_error.setText(bod.errorCAC);
                return false;
            }

            if (!bod.setCPC(txt_CPC_Pregunta.getText(), bod.minCPC, bod.maxCPC)) {
                lbl_save_error2.setText("Otros aportes Comercial, Punto 2");
                lbl_save_error.setText(bod.errorCPC);
                return false;
            }

            if (!bod.setCQC(txt_CQC_Pregunta.getText(), bod.minCQC, bod.maxCQC)) {
                lbl_save_error2.setText("Otros aportes Comercial, Punto 3");
                lbl_save_error.setText(bod.errorCQC);
                return false;
            }

            if (!bod.setCKC(txt_CKC_Pregunta.getText(), bod.minCKC, bod.maxCKC)) {
                lbl_save_error2.setText("Otros aportes Comercial, Punto 4");
                lbl_save_error.setText(bod.errorCKC);
                return false;
            }
        } else {
            bod.setCOA(0);
            bod.setCAC("0", 0, 1);
            bod.setCPC("0", 0, 1);
            bod.setCQC("0", 0, 1);
            bod.setCKC("0", 0, 1);
        }

        if (chbx_YOA_Pregunta.isSelected()) { //Otros aportes Institucional, 
            bod.setYOA(1);

            if (!bod.setYAI(txt_YAI_Pregunta.getText(), bod.minYAI, bod.maxYAI)) {
                lbl_save_error2.setText("Otros aportes Institucional, Punto 1");
                lbl_save_error.setText(bod.errorYAI);
                return false;
            }

            if (!bod.setYPI(txt_YPI_Pregunta.getText(), bod.minYPI, bod.maxYPI)) {
                lbl_save_error2.setText("Otros aportes Institucional, Punto 2");
                lbl_save_error.setText(bod.errorYPI);
                return false;
            }

            if (!bod.setYQI(txt_YQI_Pregunta.getText(), bod.minYQI, bod.maxYQI)) {
                lbl_save_error2.setText("Otros aportes Institucional, Punto 3");
                lbl_save_error.setText(bod.errorYQI);
                return false;
            }

            if (!bod.setYKI(txt_YKI_Pregunta.getText(), bod.minYKI, bod.maxYKI)) {
                lbl_save_error2.setText("Otros aportes Institucional, Punto 4");
                lbl_save_error.setText(bod.errorYKI);
                return false;
            }
        } else {
            bod.setYOA(0);
            bod.setYAI("0", 0, 1);
            bod.setYPI("0", 0, 1);
            bod.setYQI("0", 0, 1);
            bod.setYKI("0", 0, 1);
        }
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - INICIO Caudal medio diario  :TODO  al guardar los datos cambian en unos pequños decimales ya que uso datos del bod
        if (!bod.setQ2C(bod.CalcularQ2C(0, 0, 0, 0, 0, 0), bod.minQ2C, bod.maxQ2C)) {//Caudal medio diario  
            lbl_save_error2.setText("Caudal medio diario, Punto 1, Debe corregir los datos (ver ayuda)");
            lbl_save_error.setText(bod.errorQ2C);
            return false;
        } else {
            txt_Q2C_Pregunta.setText(bod.getQ2C()); //Ax: wut
        }

        if (!bod.setQ2K(bod.CalcularQ2K(0, 0, 0, 0, 0, 0), bod.minQ2K, bod.maxQ2K)) {//Caudal medio diario
            lbl_save_error2.setText("Caudal medio diario, Punto 2, Debe corregir los datos (ver ayuda)");
            lbl_save_error.setText(bod.errorQ2K);
            return false;
        } else {
            txt_Q2K_Pregunta.setText(bod.getQ2K());
        }

        if (!bod.setQ3M(txt_Q3M_Pregunta.getText(), bod.minQ3M, bod.maxQ3M)) { //Caudal máximo diario
            lbl_save_error2.setText("Caudal máximo diario, Punto 1");
            lbl_save_error.setText(bod.errorQ3M);
            return false;
        }

        if (chbx_Q3R_Pregunta.isSelected()) {
            bod.setQ3R(1);
        } else {
            bod.setQ3R(0);
        }

        if (!bod.setQ3C(bod.CalcularQ3C(0, 0, 0, 0, 0, 0, 0), bod.minQ3C, bod.maxQ3C)) {//Caudal máximo diario
            lbl_save_error2.setText("Caudal máximo diario, Punto 2, Debe corregir los datos (ver ayuda)");
            lbl_save_error.setText(bod.errorQ3C);
            return false;
        } else {
            txt_Q3C_Pregunta.setText(bod.getQ3Cs());
        }

        if (!bod.setQ3K(bod.CalcularQ3K(0, 0, 0, 0, 0, 0, 0), bod.minQ3K, bod.maxQ3K)) {//Caudal máximo diario
            lbl_save_error2.setText("Caudal máximo diario, Punto 3, Debe corregir los datos (ver ayuda)");
            lbl_save_error.setText(bod.errorQ3K);
            return false;
        } else {
            txt_Q3K_Pregunta.setText(bod.getQ3K());
        }

        if (!bod.setQ1H(txt_Q1H_Pregunta.getText(), bod.minQ1H, bod.maxQ1H)) { //Caudal mínimo diario
            lbl_save_error2.setText("Caudal mínimo diario, Punto 1");
            lbl_save_error.setText(bod.errorQ1H);
            return false;
        }

        if (!bod.setQ1C(bod.CalcularQ1C(0, 0, 0, 0, 0, 0), bod.minQ1C, bod.maxQ1C)) {//Caudal mínimo diario
            lbl_save_error2.setText("Caudal mínimo diario, Punto 2, Debe corregir los datos (ver ayuda)");
            lbl_save_error.setText(bod.errorQ1C);
            return false;
        } else {
            txt_Q1C_Pregunta.setText(bod.getQ1C());
        }

        if (!bod.setQ1K(bod.CalcularQ1K(0, 0, 0, 0, 0, 0), bod.minQ1K, bod.maxQ1K)) {//Caudal mínimo diario
            lbl_save_error2.setText("Caudal mínimo diario, Punto 3, Debe corregir los datos (ver ayuda)");
            lbl_save_error.setText(bod.errorQ1K);
            return false;
        } else {
            txt_Q1K_Pregunta.setText(bod.getQ1K());
        }

        if (!chbx_YOA_Pregunta.isSelected() || !chbx_IOA_pregunta.isSelected() || !chbx_COA_Pregunta.isSelected()) { //Mensaje a usuario advertir: llenar opcionalmente Aportes Industrial, Comercial e Institucional 
            String temp = "";

            if (!chbx_IOA_pregunta.isSelected()) {
                temp = "Industrial\n";
            }
            if (!chbx_COA_Pregunta.isSelected()) {
                temp += "Comercial\n";
            }
            if (!chbx_YOA_Pregunta.isSelected()) {
                temp += "Institucional";
            }

            int n = util.Mensaje("¿Desea Guardar sin llenar los aportes: \n" + temp + " ?", "yesno");
            if (n != JOptionPane.YES_OPTION) {
                return false;
            }
        }

        bod.EditCalculoCaudalesDiseno = true;

        if (!bod.GuardarUpdateBod()) {
            bod.EditCalculoCaudalesDiseno = false;
            return false;
        }

        Main main = (Main) this.getTopLevelAncestor(); // Trata de ir  al contenedor principal 
        main.ComprobarCambio(4);//Comprobar cambios que afecten datos posteriores, si este guardar es de una edición.

        return true;
    }

    /**
     * *
     * Oculta,visualiza o habilita las opciones de los radiobuttons de Caudal de
     * infiltración (Qinf) Según escoja el usuario: Extensión de la red de
     * alcantarillado o Área servida
     */
    private void ComprobarCaudalInfiltración() {

        lbl_QEL_QAA_error.setVisible(true);
        lbl_QET_QAI_error.setVisible(true);
        lbl_QEL_QAA_titulo1.setVisible(true);
        lbl_QEL_QAA_titulo2.setVisible(true);
        lbl_QET_QAI_titulo1.setVisible(true);
        lbl_QET_QAI_titulo2.setVisible(true);

        lbl_QEL_QAA_error.setText("");
        lbl_QET_QAI_error.setText("");
        lbl_QEC_QAC_error.setText("");
        lbl_QEK_QAK_error.setText("");

        if (flag_QIF) {
            txt_QAA_Pregunta.setVisible(false);
            txt_QEL_Pregunta.setVisible(true);
            lbl_QEL_QAA_titulo1.setText(titulo1_QEL);
            lbl_QEL_QAA_titulo2.setText(titulo2_QEL);

            btn_QAI_ayuda.setVisible(false);//-------
            btn_QET_ayuda.setVisible(true);
            txt_QAI_Pregunta.setVisible(false);
            txt_QET_Pregunta.setVisible(true);
            lbl_QET_QAI_titulo1.setText(titulo1_QET);
            lbl_QET_QAI_titulo2.setText(titulo2_QET);

            txt_QAC_Pregunta.setVisible(false);//-------
            txt_QEC_Pregunta.setVisible(true);
            lbl_QEC_QAC_titulo1.setText(titulo1_QEC);
            lbl_QEC_QAC_titulo2.setText(titulo2_QEC);

            txt_QAK_Pregunta.setVisible(false);//-------
            txt_QEK_Pregunta.setVisible(true);
            lbl_QEK_QAK_titulo1.setText(titulo1_QEK);
            lbl_QEK_QAK_titulo2.setText(titulo2_QEK);
            return;
        }

        txt_QAA_Pregunta.setVisible(true);
        txt_QEL_Pregunta.setVisible(false);
        lbl_QEL_QAA_titulo1.setText(titulo1_QAA); //Todo cambiar esto y dejar labels para c/u
        lbl_QEL_QAA_titulo2.setText(titulo2_QAA);

        btn_QAI_ayuda.setVisible(true);//-------------
        btn_QET_ayuda.setVisible(false);
        txt_QAI_Pregunta.setVisible(true);
        txt_QET_Pregunta.setVisible(false);
        lbl_QET_QAI_titulo1.setText(titulo1_QAI);
        lbl_QET_QAI_titulo2.setText(titulo2_QAI);

        txt_QAC_Pregunta.setVisible(true);//----------
        txt_QEC_Pregunta.setVisible(false);
        lbl_QEC_QAC_titulo1.setText(titulo1_QAC);
        lbl_QEC_QAC_titulo2.setText(titulo2_QAC);

        txt_QAK_Pregunta.setVisible(true);//----------
        txt_QEK_Pregunta.setVisible(false);
        lbl_QEK_QAK_titulo1.setText(titulo1_QAK);
        lbl_QEK_QAK_titulo2.setText(titulo2_QAK);
    }

    /**
     * Oculta,visualiza o habilita las opciones de los radiobuttons de Caudal de
     * Conexiones Erradas (QCE) Según escoja el usuario: Área servida o Nivel de
     * complejidad , si este último es visible (regla1)
     */
    private void CaudalConexionesErradas() {

        switch (flag_QCE_QNL) { //TODO. ESTE FLAG DEBE SER PARAMETRO
            case -1:
                if (!flag_bajo) { //Regla 
                    flag_QCE_QNL = 1; //Visible Área servida                     
                    rbtn_QCE_Pregunta.setSelected(true);
                    rbtn_QCE_Pregunta.setVisible(true);
                    CaudalConexionesErradas();
                } else {
                    rbtn_QCE_Pregunta.setVisible(true);
                    rbtn_QNL_Pregunta.setVisible(true);
                    btn_QNL_ayuda.setVisible(true);
                }
                lbl_QCA_titulo1.setVisible(true);
                txt_QCA_Pregunta.setVisible(true);
                lbl_QCA_titulo2.setVisible(true);
                lbl_QCA_error.setVisible(true);
                lbl_QCM_titulo1.setVisible(true);
                txt_QCM_Pregunta.setVisible(true);
                lbl_QCM_titulo2.setVisible(true);
                lbl_QCM_error.setVisible(true);
                btn_QCM_ayuda.setVisible(true);
                break;
            case 0: //Ocultar todo     
                btnGrupQCEQNL.clearSelection();
                lbl_QCA_titulo1.setVisible(false);
                txt_QCA_Pregunta.setVisible(false);
                lbl_QCA_titulo2.setVisible(false);
                lbl_QCA_error.setVisible(false);
                lbl_QCM_titulo1.setVisible(false);
                txt_QCM_Pregunta.setVisible(false);
                lbl_QCM_titulo2.setVisible(false);
                lbl_QCM_error.setVisible(false);
                btn_QCM_ayuda.setVisible(false);
                rbtn_QCE_Pregunta.setVisible(false);
                rbtn_QNL_Pregunta.setVisible(false);
                btn_QNL_ayuda.setVisible(false);
                lbl_QNC_titulo1.setVisible(false);
                txt_QNC_Pregunta.setVisible(false);
                lbl_QNC_titulo2.setVisible(false);
                lbl_QNC_error.setVisible(false);
                lbl_QNK_titulo1.setVisible(false);
                txt_QNK_Pregunta.setVisible(false);
                lbl_QNK_titulo2.setVisible(false);
                lbl_QNK_error.setVisible(false);
                lbl_QCC_titulo1.setVisible(false);
                txt_QCC_Pregunta.setVisible(false);
                lbl_QCC_titulo2.setVisible(false);
                lbl_QCC_error.setVisible(false);
                lbl_QCK_titulo1.setVisible(false);
                txt_QCK_Pregunta.setVisible(false);
                lbl_QCK_titulo2.setVisible(false);
                lbl_QCK_error.setVisible(false);
                break;
            case 1: //Visible Área servida
                txt_QCA_Pregunta.setEditable(true);
                txt_QCM_Pregunta.setEditable(true);
                lbl_QNC_titulo1.setVisible(false);
                txt_QNC_Pregunta.setVisible(false);
                lbl_QNC_titulo2.setVisible(false);
                lbl_QNC_error.setVisible(false);
                lbl_QNK_titulo1.setVisible(false);
                txt_QNK_Pregunta.setVisible(false);
                lbl_QNK_titulo2.setVisible(false);
                lbl_QNK_error.setVisible(false);
                lbl_QCC_titulo1.setVisible(true);
                txt_QCC_Pregunta.setVisible(true);
                lbl_QCC_titulo2.setVisible(true);
                lbl_QCC_error.setVisible(true);
                lbl_QCK_titulo1.setVisible(true);
                txt_QCK_Pregunta.setVisible(true);
                lbl_QCK_titulo2.setVisible(true);
                lbl_QCK_error.setVisible(true);
                calcularQCCQCK();
                break;
            case 2:
                txt_QCA_Pregunta.setEditable(true);
                txt_QCM_Pregunta.setEditable(true);
                lbl_QNC_titulo1.setVisible(true);
                txt_QNC_Pregunta.setVisible(true);
                lbl_QNC_titulo2.setVisible(true);
                lbl_QNC_error.setVisible(true);
                lbl_QNK_titulo1.setVisible(true);
                txt_QNK_Pregunta.setVisible(true);
                lbl_QNK_titulo2.setVisible(true);
                lbl_QNK_error.setVisible(true);
                lbl_QCC_titulo1.setVisible(false);
                txt_QCC_Pregunta.setVisible(false);
                lbl_QCC_titulo2.setVisible(false);
                lbl_QCC_error.setVisible(false);
                lbl_QCK_titulo1.setVisible(false);
                txt_QCK_Pregunta.setVisible(false);
                lbl_QCK_titulo2.setVisible(false);
                lbl_QCK_error.setVisible(false);
                calcularQNCQNK();
                break;
        }
    }

    /**
     * Oculta,visualiza o habilita las opciones de los radiobuttons de Otros
     * Aportes tipo: Industrial de la pregunta ¿Cuenta con registros de medición
     * de caudales de origen industrial?
     */
    private void OtrosAportesIndustrial() {

        if (chbx_IOA_pregunta.isSelected()) {
            rbtn_IRM_Pregunta1.setVisible(true);
            rbtn_IRM_Pregunta2.setVisible(true);
            lbl_IRM_desc.setVisible(true);
        } else {
            rbtn_IRM_Pregunta1.setVisible(false);
            rbtn_IRM_Pregunta2.setVisible(false);
            lbl_IRM_desc.setVisible(false);
            buttongroup3.clearSelection();//Borra el rbtn seleccionado
            flag_IRM_y = false;
            flag_IRM_n = false;
        }
        //SINO DESAH TODO

        if (!flag_IRM_y) {
            lbl_IQ2_titulo1.setVisible(false);
            lbl_IQ2_titulo2.setVisible(false);
            txt_IQ2_Pregunta.setVisible(false);
            lbl_IQ2_error.setVisible(false);
            lbl_IK2_titulo1.setVisible(false);
            lbl_IK2_titulo2.setVisible(false);
            txt_IK2_Pregunta.setVisible(false);
            lbl_IK2_error.setVisible(false);
            chbx_ICO_Pregunta.setVisible(false);
            lbl_IQ3_titulo1.setVisible(false);
            lbl_IQ3_titulo2.setVisible(false);
            txt_IQ3_Pregunta.setVisible(false);
            lbl_IQ3_error.setVisible(false);
            lbl_IK3_titulo1.setVisible(false);
            lbl_IK3_titulo2.setVisible(false);
            txt_IK3_Pregunta.setVisible(false);
            lbl_IK3_error.setVisible(false);
            lbl_ICM_titulo1.setVisible(false);
            lbl_ICM_titulo2.setVisible(false);
            txt_ICM_Pregunta.setVisible(false);
            btn_ICM_ayuda.setVisible(false);
            lbl_ICM_error.setVisible(false);
            lbl_IQ1_titulo1.setVisible(false);
            lbl_IQ1_titulo2.setVisible(false);
            txt_IQ1_Pregunta.setVisible(false);
            lbl_IQ1_error.setVisible(false);
            lbl_IK1_titulo1.setVisible(false);
            lbl_IK1_titulo2.setVisible(false);
            txt_IK1_Pregunta.setVisible(false);
            lbl_IK1_error.setVisible(false);
            lbl_ICC_titulo1.setVisible(false);
            lbl_ICC_titulo2.setVisible(false);
            txt_ICC_Pregunta.setVisible(false);
            txt_ICC_Pregunta.setVisible(false);
            btn_ICC_ayuda.setVisible(false);
            lbl_ICC_error.setVisible(false);
        } else {
            lbl_IQ2_titulo1.setVisible(true);
            lbl_IQ2_titulo2.setVisible(true);
            txt_IQ2_Pregunta.setVisible(true);
            lbl_IQ2_error.setVisible(true);
            lbl_IK2_titulo1.setVisible(true);
            lbl_IK2_titulo2.setVisible(true);
            txt_IK2_Pregunta.setVisible(true);
            lbl_IK2_error.setVisible(true);
            chbx_ICO_Pregunta.setVisible(true);
            lbl_IQ3_titulo1.setVisible(true);
            lbl_IQ3_titulo2.setVisible(true);
            txt_IQ3_Pregunta.setVisible(true);
            lbl_IQ3_error.setVisible(true);
            lbl_IK3_titulo1.setVisible(true);
            lbl_IK3_titulo2.setVisible(true);
            txt_IK3_Pregunta.setVisible(true);
            lbl_IK3_error.setVisible(true);
            lbl_ICM_titulo1.setVisible(true);
            lbl_ICM_titulo2.setVisible(true);
            txt_ICM_Pregunta.setVisible(true);
            btn_ICM_ayuda.setVisible(true);
            lbl_ICM_error.setVisible(true);
            lbl_IQ1_titulo1.setVisible(true);
            lbl_IQ1_titulo2.setVisible(true);
            txt_IQ1_Pregunta.setVisible(true);
            lbl_IQ1_error.setVisible(true);
            lbl_IK1_titulo1.setVisible(true);
            lbl_IK1_titulo2.setVisible(true);
            txt_IK1_Pregunta.setVisible(true);
            lbl_IK1_error.setVisible(true);
            lbl_ICC_titulo1.setVisible(true);
            lbl_ICC_titulo2.setVisible(true);
            txt_ICC_Pregunta.setVisible(true);
            txt_ICC_Pregunta.setVisible(true);
            btn_ICC_ayuda.setVisible(true);
            lbl_ICC_error.setVisible(true);
        }

        if (!flag_IRM_n) {
            lbl_IAI_error.setVisible(false);
            lbl_IAI_titulo1.setVisible(false);
            lbl_IAI_titulo2.setVisible(false);
            txt_IAI_Pregunta.setVisible(false);

            lbl_IPI_error.setVisible(false);
            lbl_IPI_titulo1.setVisible(false);
            lbl_IPI_titulo2.setVisible(false);
            txt_IPI_Pregunta.setVisible(false);
            btn_IPI_ayuda.setVisible(false);

            lbl_IQI_error.setVisible(false);
            lbl_IQI_titulo1.setVisible(false);
            lbl_IQI_titulo2.setVisible(false);
            txt_IQI_Pregunta.setVisible(false);

            lbl_IKI_error.setVisible(false);
            lbl_IKI_titulo1.setVisible(false);
            lbl_IKI_titulo2.setVisible(false);
            txt_IKI_Pregunta.setVisible(false);
        } else {
            lbl_IAI_error.setVisible(true);
            lbl_IAI_titulo1.setVisible(true);
            lbl_IAI_titulo2.setVisible(true);
            txt_IAI_Pregunta.setVisible(true);

            lbl_IPI_error.setVisible(true);
            lbl_IPI_titulo1.setVisible(true);
            lbl_IPI_titulo2.setVisible(true);
            txt_IPI_Pregunta.setVisible(true);
            btn_IPI_ayuda.setVisible(true);

            lbl_IQI_error.setVisible(true);
            lbl_IQI_titulo1.setVisible(true);
            lbl_IQI_titulo2.setVisible(true);
            txt_IQI_Pregunta.setVisible(true);

            lbl_IKI_error.setVisible(true);
            lbl_IKI_titulo1.setVisible(true);
            lbl_IKI_titulo2.setVisible(true);
            txt_IKI_Pregunta.setVisible(true);
        }
    }

    /**
     * Función del Checkbox que controla la edición de coeficientes ICM e ICC
     * habilitandolos o no.
     */
    private void EdicionCoeficientes() {
        if (chbx_ICO_Pregunta.isSelected()) {
            txt_ICC_Pregunta.setEditable(true);
            txt_ICM_Pregunta.setEditable(true);
        } else {
            txt_ICC_Pregunta.setEditable(false);
            txt_ICM_Pregunta.setEditable(false);
            txt_ICM_Pregunta.setText(coeficienteICM);
            txt_ICC_Pregunta.setText(coeficienteICC);
            calcularIK3();
            calcularIK1();
        }
    }

    private void OtrosAportesComercial() {

        if (chbx_COA_Pregunta.isSelected()) {
            lbl_CAC_titulo1.setVisible(true);
            txt_CAC_Pregunta.setVisible(true);
            lbl_CAC_titulo2.setVisible(true);
            lbl_CAC_error.setVisible(true);
            lbl_CPC_titulo1.setVisible(true);
            txt_CPC_Pregunta.setVisible(true);
            lbl_CPC_titulo2.setVisible(true);
            lbl_CPC_error.setVisible(true);
            btn_CPC_ayuda.setVisible(true);
            lbl_CQC_titulo1.setVisible(true);
            txt_CQC_Pregunta.setVisible(true);
            lbl_CQC_titulo2.setVisible(true);
            lbl_CQC_error.setVisible(true);
            lbl_CKC_titulo1.setVisible(true);
            txt_CKC_Pregunta.setVisible(true);
            lbl_CKC_titulo2.setVisible(true);
            lbl_CKC_error.setVisible(true);
        } else {
            lbl_CAC_titulo1.setVisible(false);
            txt_CAC_Pregunta.setVisible(false);
            lbl_CAC_titulo2.setVisible(false);
            lbl_CAC_error.setVisible(false);
            lbl_CPC_titulo1.setVisible(false);
            txt_CPC_Pregunta.setVisible(false);
            lbl_CPC_titulo2.setVisible(false);
            lbl_CPC_error.setVisible(false);
            btn_CPC_ayuda.setVisible(false);
            lbl_CQC_titulo1.setVisible(false);
            txt_CQC_Pregunta.setVisible(false);
            lbl_CQC_titulo2.setVisible(false);
            lbl_CQC_error.setVisible(false);
            lbl_CKC_titulo1.setVisible(false);
            txt_CKC_Pregunta.setVisible(false);
            lbl_CKC_titulo2.setVisible(false);
            lbl_CKC_error.setVisible(false);
        }
    }

    private void OtrosAportesInstitucional() {

        if (chbx_YOA_Pregunta.isSelected()) {
            lbl_YAI_titulo1.setVisible(true);
            lbl_YAI_titulo2.setVisible(true);
            txt_YAI_Pregunta.setVisible(true);
            lbl_YAI_error.setVisible(true);
            lbl_YPI_titulo1.setVisible(true);
            lbl_YPI_titulo2.setVisible(true);
            txt_YPI_Pregunta.setVisible(true);
            lbl_YPI_error.setVisible(true);
            btn_YPI_ayuda.setVisible(true);
            lbl_YQI_titulo1.setVisible(true);
            lbl_YQI_titulo2.setVisible(true);
            txt_YQI_Pregunta.setVisible(true);
            lbl_YQI_error.setVisible(true);
            lbl_YKI_titulo1.setVisible(true);
            lbl_YKI_titulo2.setVisible(true);
            txt_YKI_Pregunta.setVisible(true);
            lbl_YKI_error.setVisible(true);
        } else {
            lbl_YAI_titulo1.setVisible(false);
            lbl_YAI_titulo2.setVisible(false);
            txt_YAI_Pregunta.setVisible(false);
            lbl_YAI_error.setVisible(false);
            lbl_YPI_titulo1.setVisible(false);
            lbl_YPI_titulo2.setVisible(false);
            txt_YPI_Pregunta.setVisible(false);
            lbl_YPI_error.setVisible(false);
            btn_YPI_ayuda.setVisible(false);
            lbl_YQI_titulo1.setVisible(false);
            lbl_YQI_titulo2.setVisible(false);
            txt_YQI_Pregunta.setVisible(false);
            lbl_YQI_error.setVisible(false);
            lbl_YKI_titulo1.setVisible(false);
            lbl_YKI_titulo2.setVisible(false);
            txt_YKI_Pregunta.setVisible(false);
            lbl_YKI_error.setVisible(false);
        }
    }

    private void CaudalMaximoDiario() {

        if (chbx_Q3R_Pregunta.isSelected()) {
            txt_Q3M_Pregunta.setEditable(false);
            calcularQ3M();
        } else {
            txt_Q3M_Pregunta.setEditable(true);
            txt_Q3M_Pregunta.setText(valordefectosQ3M);
        }
    }

    /**
     * *
     * Esto se hace para calcular automática y temporalmente QEC y QEK(Qinf L/s
     * m3/d) Ya que deben aparecer cuando el usuario ingrese QET QEL
     */
    private void calcularQECQEK() {

        if (validar.EsDobleEntre(txt_QEL_Pregunta.getText(), bod.minQEL, bod.maxQEL) && validar.EsDobleEntre(txt_QET_Pregunta.getText(), bod.minQET, bod.maxQET)) {
            String x = bod.CalcularQEC(Double.parseDouble(txt_QEL_Pregunta.getText().trim()), Double.parseDouble(txt_QET_Pregunta.getText().trim()));
            txt_QEC_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.##"));

            String y = bod.CalcularQEK(Double.parseDouble(txt_QEL_Pregunta.getText().trim()), Double.parseDouble(txt_QET_Pregunta.getText().trim()));
            txt_QEK_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(y)), "#.##"));
        } else {
            txt_QEC_Pregunta.setText("");
            txt_QEK_Pregunta.setText("");
        }
    }

    /**
     * *
     * Esto se hace para calcular automática y temporalmente QAC y QAK(Qinf L/s
     * m3/d) Ya que deben aparecer cuando el usuario ingrese QAA QAI
     */
    private void calcularQACQAK() {

        if (validar.EsDobleEntre(txt_QAA_Pregunta.getText(), bod.minQAA, bod.maxQAA) && validar.EsDobleEntre(txt_QAI_Pregunta.getText(), bod.minQAI, bod.maxQAI)) {
            String x = bod.CalcularQAC(Double.parseDouble(txt_QAA_Pregunta.getText().trim()), Double.parseDouble(txt_QAI_Pregunta.getText().trim()));
            txt_QAC_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.##"));

            String y = bod.CalcularQAK(Double.parseDouble(txt_QAA_Pregunta.getText().trim()), Double.parseDouble(txt_QAI_Pregunta.getText().trim()));
            txt_QAK_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(y)), "#.##"));
        } else {
            txt_QAC_Pregunta.setText("");
            txt_QAK_Pregunta.setText("");
        }
    }

    /**
     * *
     * Esto se hace para calcular automática y temporalmente QCC y QCK(Qinf L/s
     * m3/d) Ya que deben aparecer cuando el usuario ingrese QCA QCM
     */
    private void calcularQCCQCK() { //-_-

        if (validar.EsDobleEntre(txt_QCA_Pregunta.getText(), bod.minQCA, bod.maxQCA) && validar.EsDobleEntre(txt_QCM_Pregunta.getText(), bod.minQCM, bod.maxQCM)) {
            String x = bod.CalcularQCC(Double.parseDouble(txt_QCA_Pregunta.getText().trim()), Double.parseDouble(txt_QCM_Pregunta.getText().trim()));
            txt_QCC_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.##"));

            String y = bod.CalcularQCK(Double.parseDouble(txt_QCA_Pregunta.getText().trim()), Double.parseDouble(txt_QCM_Pregunta.getText().trim()));
            txt_QCK_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(y)), "#.##"));
        } else {
            txt_QCC_Pregunta.setText("");
            txt_QCK_Pregunta.setText("");
        }
    }

    /**
     * Esto se hace para calcular automática y temporalmente QNC y QNK(Qinf L/s
     * m3/d) Ya que deben aparecer cuando el usuario ingrese QCA QCM
     */
    private void calcularQNCQNK() {

        if (validar.EsDobleEntre(txt_QCA_Pregunta.getText(), bod.minQCA, bod.maxQCA) && validar.EsDobleEntre(txt_QCM_Pregunta.getText(), bod.minQCM, bod.maxQCM)) {
            String x = bod.CalcularQNC();
            txt_QNC_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.##"));

            String y = bod.CalcularQNK();
            txt_QNK_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(y)), "#.##"));
        } else {
            txt_QNC_Pregunta.setText("");
            txt_QNK_Pregunta.setText("");
        }
    }

    /**
     * Esto se hace para calcular automática y temporalmente IQI (Qi) y
     * IKI(m3/d) Ya que deben aparecer cuando el usuario ingrese IAI e IPI
     */
    private void calcularIQIIKI() {
        if (validar.EsDobleEntre(txt_IAI_Pregunta.getText(), bod.minIAI, bod.maxIAI) && validar.EsDobleEntre(txt_IPI_Pregunta.getText(), bod.minIPI, bod.maxIPI)) {
            String x = bod.CalcularIQI(Double.parseDouble(txt_IAI_Pregunta.getText().trim()), Double.parseDouble(txt_IPI_Pregunta.getText().trim()));
            txt_IQI_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.##"));

            String y = bod.CalcularIKI(Double.parseDouble(txt_IAI_Pregunta.getText().trim()), Double.parseDouble(txt_IPI_Pregunta.getText().trim()));
            txt_IKI_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(y)), "#.##"));
        } else {
            txt_IQI_Pregunta.setText("");
            txt_IKI_Pregunta.setText("");
        }
    }

    /**
     * Esto se hace para calcular automática y temporalmente IK2 Ya que deben
     * aparecer cuando el usuario ingrese IQ2
     */
    private void calcularIK2() {
        if (validar.EsDobleEntre(txt_IQ2_Pregunta.getText(), bod.minIQ2, bod.maxIQ2)) {
            String x = bod.CalcularIK2(Double.parseDouble(txt_IQ2_Pregunta.getText().trim()));
            txt_IK2_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.##"));
        } else {
            txt_IK2_Pregunta.setText("");
        }
    }

    /**
     * Esto se hace para calcular automática y temporalmente IK3 Ya que deben
     * aparecer cuando el usuario ingrese IQ3 y (opcionalmente)ICM
     */
    private void calcularIK3() {
        if (validar.EsDobleEntre(txt_ICM_Pregunta.getText(), bod.minICM, bod.maxICM) && validar.EsDobleEntre(txt_IQ3_Pregunta.getText(), bod.minIQ3, bod.maxIQ3)) {
            String x = bod.CalcularIK3(Double.parseDouble(txt_ICM_Pregunta.getText().trim()), Double.parseDouble(txt_IQ3_Pregunta.getText().trim()));
            txt_IK3_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.##"));
        } else {
            txt_IK3_Pregunta.setText("");//Todo (maybe) esto provoca dsiparo de error, aunque no afecta nada
        }
    }

    /**
     * Esto se hace para calcular automática y temporalmente IK1 Ya que deben
     * aparecer cuando el usuario ingrese IQ1 y (opcionalmente)ICC
     */
    private void calcularIK1() {
        if (validar.EsDobleEntre(txt_ICC_Pregunta.getText(), bod.minICC, bod.maxICC) && validar.EsDobleEntre(txt_IQ1_Pregunta.getText(), bod.minIQ1, bod.maxIQ1)) {
            String x = bod.CalcularIK1(Double.parseDouble(txt_ICC_Pregunta.getText().trim()), Double.parseDouble(txt_IQ1_Pregunta.getText().trim()));
            txt_IK1_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.##"));
        } else {
            txt_IK1_Pregunta.setText("");
        }
    }

    private void calcularCQCCKC() {
        if (validar.EsDobleEntre(txt_CAC_Pregunta.getText(), bod.minCAC, bod.maxCAC) && validar.EsDobleEntre(txt_CPC_Pregunta.getText(), bod.minCPC, bod.maxCPC)) {
            String x = bod.CalcularCQC(Double.parseDouble(txt_CAC_Pregunta.getText().trim()), Double.parseDouble(txt_CPC_Pregunta.getText().trim()));
            txt_CQC_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.##"));

            String y = bod.CalcularCKC(Double.parseDouble(txt_CAC_Pregunta.getText().trim()), Double.parseDouble(txt_CPC_Pregunta.getText().trim()));
            txt_CKC_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(y)), "#.##"));
        } else {
            txt_CQC_Pregunta.setText("");
            txt_CKC_Pregunta.setText("");
        }
    }

    private void calcularYQIYKI() {
        if (validar.EsDobleEntre(txt_YAI_Pregunta.getText(), bod.minYAI, bod.maxYAI) && validar.EsDobleEntre(txt_YPI_Pregunta.getText(), bod.minYPI, bod.maxYPI)) {
            String x = bod.CalcularYQI(Double.parseDouble(txt_YAI_Pregunta.getText().trim()), Double.parseDouble(txt_YPI_Pregunta.getText().trim()));
            txt_YQI_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.##"));

            String y = bod.CalcularYKI(Double.parseDouble(txt_YAI_Pregunta.getText().trim()), Double.parseDouble(txt_YPI_Pregunta.getText().trim()));
            txt_YKI_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(y)), "#.##"));
        } else {
            txt_YQI_Pregunta.setText("");
            txt_YKI_Pregunta.setText("");
        }
    }

    private void calcularQ3M() { //Se calcula a l guardar, tal vez esto no sirva?

        String x = bod.calcularQ3M();

        if (validar.EsDobleEntre(x, bod.minQ3M, bod.maxQ3M)) {
            txt_Q3M_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.##"));
            lbl_Q3M_error.setText("");
        } else {
            txt_Q3M_Pregunta.setText("");
            lbl_Q3M_error.setText(bod.errorQ3M);
        }
    }

    /**
     * *
     * Esto se hace para calcular automática y temporalmente Q2C y Q2K Ya que
     * deben aparecer cuando el usuario ingrese los datos relacionados. Los
     * datos relacionados no son comprobados ya que vienen de otros cálculos
     */
    private void calcularQ2CQ2K() {

        double C = 0;
        double Qinf = 0;
        double Qce = 0;
        double Qi = 0;
        double Qc = 0;
        double Qin = 0;

        try {
            C = validar.EsNumero(txt_CAR_Pregunta.getText());//Contribución de aguas residuales (C)

            if (rbtn_QIF_Pregunta.isSelected()) {
                Qinf = validar.EsNumero(txt_QEC_Pregunta.getText());
            }
            if (rbtn_QIA_Pregunta.isSelected()) {
                Qinf = validar.EsNumero(txt_QAC_Pregunta.getText());
            }

            if (rbtn_QCD_Pregunta1.isSelected()) {

                if (rbtn_QCE_Pregunta.isSelected()) {
                    Qce = validar.EsNumero(txt_QCC_Pregunta.getText());
                }

                if (rbtn_QNL_Pregunta.isSelected()) {
                    Qce = validar.EsNumero(txt_QNC_Pregunta.getText());
                }
            }

            if (chbx_IOA_pregunta.isSelected()) {

                if (rbtn_IRM_Pregunta1.isSelected()) {//si
                    Qi = validar.EsNumero(txt_IQ2_Pregunta.getText());
                }

                if (rbtn_IRM_Pregunta2.isSelected()) {//no
                    Qi = validar.EsNumero(txt_IQI_Pregunta.getText());
                }
            }

            if (chbx_COA_Pregunta.isSelected()) {
                Qc = validar.EsNumero(txt_CQC_Pregunta.getText());
            }

            if (chbx_YOA_Pregunta.isSelected()) {
                Qin = validar.EsNumero(txt_YQI_Pregunta.getText());
            }

            String sQ2C = bod.CalcularQ2C(C, Qinf, Qce, Qi, Qc, Qin);

            if (!validar.EsDobleEntre(sQ2C, bod.minQ2C, bod.maxQ2C)) {//Caudal medio diario  
                lbl_Q2C_error.setText(bod.errorQ2C);

            } else {
                txt_Q2C_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(sQ2C)), "#.##"));
            }

            String sQ2K = bod.CalcularQ2K(C, Qinf, Qce, Qi, Qc, Qin); //posible null

            if (!validar.EsDobleEntre(sQ2K, bod.minQ2K, bod.maxQ2K)) {//Caudal medio diario  
                lbl_Q2K_error.setText(bod.errorQ2K);

            } else {
                txt_Q2K_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(sQ2K)), "#.##"));
            }
        } catch (Exception ex) {
            txt_Q2C_Pregunta.setText("Ha ocurrido un error al calcular");
            txt_Q2K_Pregunta.setText("Ha ocurrido un error al calcular");
            log.error("Error en calcularQ2CQ2K() " + ex.getMessage());
        }
    }

    /**
     * *
     * Esto se hace para calcular automática y temporalmente Q3C y Q3K (Caudal
     * máximo diario) Ya que deben aparecer cuando el usuario ingrese los datos
     * relacionados. Los datos relacionados no son comprobados ya que vienen de
     * otros cálculos
     */
    private void calcularQ3CQ3K() {

        double C = 0;
        double K = 0;
        double Qinf = 0;
        double Qce = 0;
        double Qi = 0;
        double Qc = 0;
        double Qin = 0;

        try {
            C = validar.EsNumero(txt_CAR_Pregunta.getText());//Contribución de aguas residuales (C)

            if (!validar.EsDobleEntre(txt_Q3M_Pregunta.getText(), bod.minQ3M, bod.maxQ3M)) {
                txt_Q3C_Pregunta.setText("");
                txt_Q3K_Pregunta.setText("");
                return;
            } else {
                K = validar.EsNumero(txt_Q3M_Pregunta.getText());
            }

            if (rbtn_QIF_Pregunta.isSelected()) {
                Qinf = validar.EsNumero(txt_QEC_Pregunta.getText());
            }
            if (rbtn_QIA_Pregunta.isSelected()) {
                Qinf = validar.EsNumero(txt_QAC_Pregunta.getText());
            }

            if (rbtn_QCD_Pregunta1.isSelected()) {

                if (rbtn_QCE_Pregunta.isSelected()) {
                    Qce = validar.EsNumero(txt_QCC_Pregunta.getText());
                }

                if (rbtn_QNL_Pregunta.isSelected()) {
                    Qce = validar.EsNumero(txt_QNC_Pregunta.getText());
                }
            }

            if (chbx_IOA_pregunta.isSelected()) {

                if (rbtn_IRM_Pregunta1.isSelected()) {//si
                    Qi = validar.EsNumero(txt_IQ3_Pregunta.getText());
                }

                if (rbtn_IRM_Pregunta2.isSelected()) {//no
                    Qi = validar.EsNumero(txt_IQI_Pregunta.getText());
                }
            }

            if (chbx_COA_Pregunta.isSelected()) {
                Qc = validar.EsNumero(txt_CQC_Pregunta.getText());
            }

            if (chbx_YOA_Pregunta.isSelected()) {
                Qin = validar.EsNumero(txt_YQI_Pregunta.getText());
            }

            String sQ3C = bod.CalcularQ3C(C, K, Qinf, Qce, Qi, Qc, Qin);

            if (!validar.EsDobleEntre(sQ3C, bod.minQ3C, bod.maxQ3C)) {//Caudal medio diario  
                lbl_Q3C_error.setText(bod.errorQ3C);

            } else {
                txt_Q3C_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(sQ3C)), "#.##"));
            }

            String sQ3K = bod.CalcularQ3K(C, K, Qinf, Qce, Qi, Qc, Qin); //posible null

            if (!validar.EsDobleEntre(sQ3K, bod.minQ3K, bod.maxQ3K)) {//Caudal medio diario  
                lbl_Q3K_error.setText(bod.errorQ3K);

            } else {
                txt_Q3K_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(sQ3K)), "#.##"));
            }
        } catch (Exception ex) {
            txt_Q3C_Pregunta.setText("Ha ocurrido un error al calcular");
            txt_Q3K_Pregunta.setText("Ha ocurrido un error al calcular");
            log.error("Error en calcularQ3CQ3K() " + ex.getMessage());
        }
    }

    /**
     * *
     * Esto se hace para calcular automática y temporalmente Q1C y Q1K (Caudal
     * mínimo diario) Ya que deben aparecer cuando el usuario ingrese los datos
     * relacionados. Los datos relacionados no son comprobados ya que vienen de
     * otros cálculos
     */
    private void calcularQ1CQ1K() {

        double C = 0;
        double K3 = 0;
        double Qinf = 0;
        double Qi = 0;
        double Qc = 0;
        double Qin = 0;

        try {
            C = validar.EsNumero(txt_CAR_Pregunta.getText());//Contribución de aguas residuales (C)

            if (!validar.EsDobleEntre(txt_Q1H_Pregunta.getText(), bod.minQ1H, bod.maxQ1H)) {
                txt_Q1C_Pregunta.setText("");
                txt_Q1K_Pregunta.setText("");
                return;
            } else {
                K3 = validar.EsNumero(txt_Q1H_Pregunta.getText());
            }

            if (rbtn_QIF_Pregunta.isSelected()) {
                Qinf = validar.EsNumero(txt_QEC_Pregunta.getText());
            }
            if (rbtn_QIA_Pregunta.isSelected()) {
                Qinf = validar.EsNumero(txt_QAC_Pregunta.getText());
            }

            if (chbx_IOA_pregunta.isSelected()) {

                if (rbtn_IRM_Pregunta1.isSelected()) {//si
                    Qi = validar.EsNumero(txt_IQ1_Pregunta.getText());
                }

                if (rbtn_IRM_Pregunta2.isSelected()) {//no
                    Qi = validar.EsNumero(txt_IQI_Pregunta.getText());
                }
            }

            if (chbx_COA_Pregunta.isSelected()) {
                Qc = validar.EsNumero(txt_CQC_Pregunta.getText());
            }

            if (chbx_YOA_Pregunta.isSelected()) {
                Qin = validar.EsNumero(txt_YQI_Pregunta.getText());
            }

            String sQ1C = bod.CalcularQ1C(C, K3, Qinf, Qi, Qc, Qin);

            if (!validar.EsDobleEntre(sQ1C, bod.minQ1C, bod.maxQ1C)) {//Caudal medio diario  
                lbl_Q1C_error.setText(bod.errorQ1C);

            } else {
                txt_Q1C_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(sQ1C)), "#.##"));
            }

            String sQ1K = bod.CalcularQ1K(C, K3, Qinf, Qi, Qc, Qin); //posible null

            if (!validar.EsDobleEntre(sQ1K, bod.minQ1K, bod.maxQ1K)) {//Caudal medio diario  
                lbl_Q1K_error.setText(bod.errorQ1K);

            } else {
                txt_Q1K_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(sQ1K)), "#.##"));
            }
        } catch (Exception ex) {
            txt_Q1C_Pregunta.setText("Ha ocurrido un error al calcular");
            txt_Q1K_Pregunta.setText("Ha ocurrido un error al calcular");
            log.error("Error en calcularQ1CQ1K() " + ex.getMessage());
        }
    }

    /**
     * *
     * Agrega un listener a los campos necesarios para calcular las 3 ultimas
     * partes (Caudal medio, máx y min. diarios)
     *
     * @param jt recibe el campo de texto que se le agregará el listener
     */
    private void ListenerCampoTxt(JTextField jtfield) {

        jtfield.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
            }

            public void removeUpdate(DocumentEvent e) {
            }

            public void insertUpdate(DocumentEvent e) {
                calcularQ2CQ2K();
                calcularQ3CQ3K();
                calcularQ1CQ1K();
            }
        });
    }

    /**
     * Permite asignar y crear un mensaje de ayuda (popup) mediante la clase
     * Listener_Popup --> MensajePopup
     *
     * @param lbl Label a quien se asigna
     * @param mensaje del popup
     * @param uri dirección url al hacer clic en el popup
     */
    private void AsignarPopup(JLabel lbl, String mensaje, String uri, int dx, int dy) {
        Lpopup = new Listener_Popup();
        Lpopup.AgregarMensajePopup(lbl, mensaje, uri, dx, dy);
    }
    
        private void AsignarPopupBtn(JButton lbl, String mensaje, String uri, int dx, int dy) {
        Lpopup = new Listener_Popup();
        Lpopup.AgregarMensajePopupBtn(lbl, mensaje, uri, dx, dy);
    }

    private void AsignarTextDoble(JTextField jtxtf, double min, double max, JLabel jlbl, String ErrorMsg) {
        Ltexto = new Listener_Texto();
        Ltexto.AgregarAlertaTextoDoble(jtxtf, min, max, jlbl, ErrorMsg);
    }

    private void AsignarTextEntero(JTextField jtxtf, int min, int max, JLabel jlbl, String ErrorMsg) {
        Ltexto = new Listener_Texto();
        Ltexto.AgregarAlertaTextoEntero(jtxtf, min, max, jlbl, ErrorMsg);
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
        rbtn_QIA_Pregunta = new javax.swing.JRadioButton();
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
        lbl_QCE_desc = new javax.swing.JLabel();
        rbtn_QIF_Pregunta = new javax.swing.JRadioButton();
        lbl_QNC_titulo1 = new javax.swing.JLabel();
        lbl_QNC_error = new javax.swing.JLabel();
        lbl_QNC_titulo2 = new javax.swing.JLabel();
        lbl_QNK_titulo1 = new javax.swing.JLabel();
        txt_QNK_Pregunta = new javax.swing.JTextField();
        lbl_QNK_titulo2 = new javax.swing.JLabel();
        lbl_QNK_error = new javax.swing.JLabel();
        txt_QNC_Pregunta = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        rbtn_IRM_Pregunta1 = new javax.swing.JRadioButton();
        lbl_IOA_desc = new javax.swing.JLabel();
        chbx_IOA_pregunta = new javax.swing.JCheckBox();
        rbtn_IRM_Pregunta2 = new javax.swing.JRadioButton();
        lbl_IAI_error = new javax.swing.JLabel();
        txt_IPI_Pregunta = new javax.swing.JTextField();
        lbl_IPI_titulo1 = new javax.swing.JLabel();
        lbl_IQI_error = new javax.swing.JLabel();
        lbl_IQI_titulo2 = new javax.swing.JLabel();
        lbl_IPI_titulo2 = new javax.swing.JLabel();
        lbl_IPI_error = new javax.swing.JLabel();
        lbl_IAI_titulo1 = new javax.swing.JLabel();
        lbl_IAI_titulo2 = new javax.swing.JLabel();
        lbl_IKI_titulo1 = new javax.swing.JLabel();
        txt_IAI_Pregunta = new javax.swing.JTextField();
        lbl_IKI_titulo2 = new javax.swing.JLabel();
        lbl_IKI_error = new javax.swing.JLabel();
        txt_IQI_Pregunta = new javax.swing.JTextField();
        lbl_IQI_titulo1 = new javax.swing.JLabel();
        txt_IKI_Pregunta = new javax.swing.JTextField();
        lbl_IQ2_error = new javax.swing.JLabel();
        txt_IK2_Pregunta = new javax.swing.JTextField();
        lbl_IK2_titulo1 = new javax.swing.JLabel();
        txt_IQ3_Pregunta = new javax.swing.JTextField();
        txt_IK3_Pregunta = new javax.swing.JTextField();
        lbl_IQ3_titulo1 = new javax.swing.JLabel();
        lbl_IK3_error = new javax.swing.JLabel();
        lbl_IK3_titulo2 = new javax.swing.JLabel();
        txt_IQ2_Pregunta = new javax.swing.JTextField();
        lbl_IK3_titulo1 = new javax.swing.JLabel();
        lbl_IQ2_titulo2 = new javax.swing.JLabel();
        lbl_IQ2_titulo1 = new javax.swing.JLabel();
        lbl_IK2_error = new javax.swing.JLabel();
        lbl_IK2_titulo2 = new javax.swing.JLabel();
        lbl_IQ3_titulo2 = new javax.swing.JLabel();
        lbl_IQ3_error = new javax.swing.JLabel();
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
        chbx_YOA_Pregunta = new javax.swing.JCheckBox();
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
        lbl_save_error2 = new javax.swing.JLabel();
        txt_QAK_Pregunta = new javax.swing.JTextField();
        txt_QEK_Pregunta = new javax.swing.JTextField();
        txt_QEC_Pregunta = new javax.swing.JTextField();
        txt_QET_Pregunta = new javax.swing.JTextField();
        rbtn_QCE_Pregunta = new javax.swing.JRadioButton();
        rbtn_QNL_Pregunta = new javax.swing.JRadioButton();
        lbl_IRM_desc = new javax.swing.JLabel();
        lbl_IK1_error = new javax.swing.JLabel();
        lbl_IK1_titulo2 = new javax.swing.JLabel();
        lbl_IQ1_titulo2 = new javax.swing.JLabel();
        lbl_IQ1_error = new javax.swing.JLabel();
        lbl_ICC_titulo1 = new javax.swing.JLabel();
        lbl_ICM_titulo1 = new javax.swing.JLabel();
        lbl_ICM_titulo2 = new javax.swing.JLabel();
        txt_ICM_Pregunta = new javax.swing.JTextField();
        lbl_ICC_titulo2 = new javax.swing.JLabel();
        lbl_ICC_error = new javax.swing.JLabel();
        lbl_IK1_titulo1 = new javax.swing.JLabel();
        txt_ICC_Pregunta = new javax.swing.JTextField();
        txt_IK1_Pregunta = new javax.swing.JTextField();
        lbl_IQ1_titulo1 = new javax.swing.JLabel();
        txt_IQ1_Pregunta = new javax.swing.JTextField();
        lbl_ICM_error = new javax.swing.JLabel();
        chbx_ICO_Pregunta = new javax.swing.JCheckBox();
        chbx_Q3R_Pregunta = new javax.swing.JCheckBox();
        rbtn_QCD_Pregunta1 = new javax.swing.JRadioButton();
        rbtn_QCD_Pregunta2 = new javax.swing.JRadioButton();
        lbl_QCD_error = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        btn_QIF_ayuda = new javax.swing.JButton();
        btn_QAI_ayuda = new javax.swing.JButton();
        btn_QET_ayuda = new javax.swing.JButton();
        btn_QCE_ayuda = new javax.swing.JButton();
        btn_QCM_ayuda = new javax.swing.JButton();
        btn_QNL_ayuda = new javax.swing.JButton();
        btn_IOA_ayuda = new javax.swing.JButton();
        btn_IPI_ayuda = new javax.swing.JButton();
        btn_ICC_ayuda = new javax.swing.JButton();
        btn_ICM_ayuda = new javax.swing.JButton();
        btn_Q3R_ayuda = new javax.swing.JButton();
        btn_YPI_ayuda = new javax.swing.JButton();
        btn_YOA_ayuda = new javax.swing.JButton();
        btn_CPC_ayuda = new javax.swing.JButton();
        btn_COA_ayuda = new javax.swing.JButton();
        btn_Q2C_ayuda = new javax.swing.JButton();
        btn_Q1H_ayuda = new javax.swing.JButton();
        btn_Q3M_ayuda = new javax.swing.JButton();
        btn_Q1C_ayuda = new javax.swing.JButton();
        btn_close2 = new javax.swing.JButton();

        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(1024, 1524));

        jsp_CalculoCaudalesDiseno.setPreferredSize(new java.awt.Dimension(1024, 2500));

        jp_Componentes.setName(""); // NOI18N
        jp_Componentes.setOpaque(false);
        jp_Componentes.setPreferredSize(new java.awt.Dimension(1024, 2700));
        jp_Componentes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jp_Componentes.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 1093, -1));

        lbl_titulo1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_titulo1.setForeground(new java.awt.Color(51, 51, 51));
        lbl_titulo1.setText("Titulo");
        jp_Componentes.add(lbl_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 300, 30));

        lbl_CAR_titulo1.setText("Titulo");
        lbl_CAR_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAR_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 300, 30));
        jp_Componentes.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 139, 1093, -1));

        txt_CAR_Pregunta.setEditable(false);
        jp_Componentes.add(txt_CAR_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 50, 130, 25));

        lbl_titulo2.setForeground(new java.awt.Color(0, 51, 102));
        lbl_titulo2.setText("Desc");
        jp_Componentes.add(lbl_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 0, 600, 30));

        lbl_CAR_titulo2.setText("Titulo");
        lbl_CAR_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAR_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 50, 80, 25));

        lbl_QIF_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_QIF_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_QIF_desc.setText("Desc");
        lbl_QIF_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QIF_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 610, 30));

        txt_QAA_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_QAA_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_QAA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 260, 130, 25));

        lbl_QEL_QAA_titulo1.setText("Titulo");
        lbl_QEL_QAA_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QEL_QAA_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 300, 30));

        lbl_QEL_QAA_titulo2.setText("Titulo");
        lbl_QEL_QAA_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QEL_QAA_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 260, 80, 25));

        lbl_KAR_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_KAR_error.setText(" . . .");
        jp_Componentes.add(lbl_KAR_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 90, 340, 35));
        jp_Componentes.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 420, 1093, 2));

        lbl_QEL_QAA_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_QEL_QAA_error.setText(" . . .");
        jp_Componentes.add(lbl_QEL_QAA_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 250, 280, 35));

        txt_KAR_Pregunta.setEditable(false);
        jp_Componentes.add(txt_KAR_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 100, 130, 25));

        lbl_KAR_titulo2.setText("Titulo");
        lbl_KAR_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_KAR_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 100, 80, 25));

        lbl_CAR_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_CAR_error.setText(" . . .");
        jp_Componentes.add(lbl_CAR_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 50, 340, 35));

        lbl_KAR_titulo1.setText("Titulo");
        lbl_KAR_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_KAR_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 300, 30));

        rbtn_QIA_Pregunta.setText("Titulo");
        jp_Componentes.add(rbtn_QIA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 300, 30));

        lbl_QET_QAI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_QET_QAI_error.setText(" . . .");
        jp_Componentes.add(lbl_QET_QAI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 290, 280, 35));

        lbl_QET_QAI_titulo2.setText("Titulo");
        lbl_QET_QAI_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QET_QAI_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 300, 80, 25));

        lbl_QET_QAI_titulo1.setText("Titulo");
        lbl_QET_QAI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QET_QAI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 300, 30));

        txt_QAI_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_QAI_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_QAI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 300, 130, 25));

        lbl_QEC_QAC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_QEC_QAC_error.setText(" . . .");
        jp_Componentes.add(lbl_QEC_QAC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 330, 280, 35));

        lbl_QEC_QAC_titulo2.setText("Titulo");
        lbl_QEC_QAC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QEC_QAC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 340, 80, 25));

        lbl_QEC_QAC_titulo1.setText("Titulo");
        lbl_QEC_QAC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QEC_QAC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 300, 30));

        txt_QAC_Pregunta.setEditable(false);
        jp_Componentes.add(txt_QAC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 340, 130, 25));

        lbl_QEK_QAK_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_QEK_QAK_error.setText(" . . .");
        jp_Componentes.add(lbl_QEK_QAK_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 370, 280, 35));

        lbl_QEK_QAK_titulo2.setText("Titulo");
        lbl_QEK_QAK_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QEK_QAK_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 380, 80, 25));

        lbl_QEK_QAK_titulo1.setText("Titulo");
        lbl_QEK_QAK_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QEK_QAK_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 300, 30));

        txt_QEL_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_QEL_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_QEL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 260, 130, 25));

        lbl_QCA_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_QCA_error.setText(" . . .");
        jp_Componentes.add(lbl_QCA_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 470, 340, 35));

        lbl_QCA_titulo2.setText("Titulo");
        lbl_QCA_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QCA_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 470, 80, 25));

        lbl_QCA_titulo1.setText("Titulo");
        lbl_QCA_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QCA_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 470, 300, 30));

        txt_QCA_Pregunta.setEditable(false);
        txt_QCA_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_QCA_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_QCA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 470, 130, 25));

        lbl_QCK_titulo1.setText("Titulo");
        lbl_QCK_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QCK_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 630, 300, 30));

        lbl_QCM_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_QCM_error.setText(" . . .");
        jp_Componentes.add(lbl_QCM_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 510, 340, 35));

        lbl_QCM_titulo2.setText("Titulo");
        lbl_QCM_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QCM_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 510, 80, 25));

        txt_QCK_Pregunta.setEditable(false);
        jp_Componentes.add(txt_QCK_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 630, 130, 25));

        lbl_QCK_titulo2.setText("Titulo");
        lbl_QCK_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QCK_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 630, 80, 25));

        lbl_QCK_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_QCK_error.setText(" . . .");
        jp_Componentes.add(lbl_QCK_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 630, 340, 35));

        txt_QCC_Pregunta.setEditable(false);
        jp_Componentes.add(txt_QCC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 590, 130, 25));

        lbl_QCC_titulo1.setText("Titulo");
        lbl_QCC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QCC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 590, 300, 30));

        lbl_QCC_titulo2.setText("Titulo");
        lbl_QCC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QCC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 590, 80, 25));

        lbl_QCC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_QCC_error.setText(" . . .");
        jp_Componentes.add(lbl_QCC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 580, 340, 35));

        lbl_QCM_titulo1.setText("Titulo");
        lbl_QCM_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QCM_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 510, 300, 30));

        txt_QCM_Pregunta.setEditable(false);
        txt_QCM_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_QCM_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_QCM_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 510, 130, 25));

        lbl_QCE_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_QCE_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_QCE_desc.setText("Desc");
        lbl_QCE_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QCE_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 300, 30));

        rbtn_QIF_Pregunta.setText("Titulo");
        jp_Componentes.add(rbtn_QIF_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 300, 30));

        lbl_QNC_titulo1.setText("Titulo");
        lbl_QNC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QNC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 710, 300, 30));

        lbl_QNC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_QNC_error.setText(" . . .");
        jp_Componentes.add(lbl_QNC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 700, 340, 35));

        lbl_QNC_titulo2.setText("Titulo");
        lbl_QNC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QNC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 710, 80, 25));

        lbl_QNK_titulo1.setText("Titulo");
        lbl_QNK_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QNK_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 750, 300, 30));

        txt_QNK_Pregunta.setEditable(false);
        jp_Componentes.add(txt_QNK_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 750, 130, 25));

        lbl_QNK_titulo2.setText("Titulo");
        lbl_QNK_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_QNK_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 750, 80, 25));

        lbl_QNK_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_QNK_error.setText(" . . .");
        jp_Componentes.add(lbl_QNK_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 740, 340, 35));

        txt_QNC_Pregunta.setEditable(false);
        jp_Componentes.add(txt_QNC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 710, 130, 25));
        jp_Componentes.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 791, 1093, -1));

        rbtn_IRM_Pregunta1.setText("Titulo");
        jp_Componentes.add(rbtn_IRM_Pregunta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 880, 80, 25));

        lbl_IOA_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_IOA_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_IOA_desc.setText("Desc");
        lbl_IOA_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IOA_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 800, 610, 30));

        chbx_IOA_pregunta.setText("Titulo");
        jp_Componentes.add(chbx_IOA_pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 840, 300, 30));

        rbtn_IRM_Pregunta2.setText("Titulo");
        jp_Componentes.add(rbtn_IRM_Pregunta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 880, 80, 25));

        lbl_IAI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_IAI_error.setText(" . . .");
        jp_Componentes.add(lbl_IAI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 910, 340, 35));

        txt_IPI_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_IPI_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_IPI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 960, 130, 25));

        lbl_IPI_titulo1.setText("Titulo");
        lbl_IPI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IPI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 960, 300, 25));

        lbl_IQI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_IQI_error.setText(" . . .");
        jp_Componentes.add(lbl_IQI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 990, 340, 35));

        lbl_IQI_titulo2.setText("Titulo");
        lbl_IQI_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IQI_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1000, 80, 25));

        lbl_IPI_titulo2.setText("Titulo");
        lbl_IPI_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IPI_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 960, 80, 25));

        lbl_IPI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_IPI_error.setText(" . . .");
        jp_Componentes.add(lbl_IPI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 950, 340, 35));

        lbl_IAI_titulo1.setText("Titulo");
        lbl_IAI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IAI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 910, 300, 30));

        lbl_IAI_titulo2.setText("Titulo");
        lbl_IAI_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IAI_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 920, 80, 25));

        lbl_IKI_titulo1.setText("Titulo");
        lbl_IKI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IKI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1040, 300, 30));

        txt_IAI_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_IAI_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_IAI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 920, 130, 25));

        lbl_IKI_titulo2.setText("Titulo");
        lbl_IKI_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IKI_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1040, 80, 25));

        lbl_IKI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_IKI_error.setText(" . . .");
        jp_Componentes.add(lbl_IKI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1030, 340, 35));

        txt_IQI_Pregunta.setEditable(false);
        jp_Componentes.add(txt_IQI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1000, 130, 25));

        lbl_IQI_titulo1.setText("Titulo");
        lbl_IQI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IQI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1000, 300, 30));

        txt_IKI_Pregunta.setEditable(false);
        jp_Componentes.add(txt_IKI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1040, 130, 25));

        lbl_IQ2_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_IQ2_error.setText(" . . .");
        jp_Componentes.add(lbl_IQ2_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1070, 340, 35));

        txt_IK2_Pregunta.setEditable(false);
        jp_Componentes.add(txt_IK2_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1120, 130, 25));

        lbl_IK2_titulo1.setText("Titulo");
        lbl_IK2_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IK2_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1120, 300, 30));

        txt_IQ3_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_IQ3_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_IQ3_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1160, 130, 25));

        txt_IK3_Pregunta.setEditable(false);
        jp_Componentes.add(txt_IK3_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1200, 130, 25));

        lbl_IQ3_titulo1.setText("Titulo");
        lbl_IQ3_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IQ3_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1160, 300, 30));

        lbl_IK3_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_IK3_error.setText(" . . .");
        jp_Componentes.add(lbl_IK3_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1190, 340, 35));

        lbl_IK3_titulo2.setText("Titulo");
        lbl_IK3_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IK3_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1200, 80, 25));

        txt_IQ2_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_IQ2_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_IQ2_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1080, 130, 25));

        lbl_IK3_titulo1.setText("Titulo");
        lbl_IK3_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IK3_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1200, 300, 30));

        lbl_IQ2_titulo2.setText("Titulo");
        lbl_IQ2_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IQ2_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1080, 80, 25));

        lbl_IQ2_titulo1.setText("Titulo");
        lbl_IQ2_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IQ2_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1080, 300, 30));

        lbl_IK2_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_IK2_error.setText(" . . .");
        jp_Componentes.add(lbl_IK2_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1110, 340, 35));

        lbl_IK2_titulo2.setText("Titulo");
        lbl_IK2_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IK2_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1120, 80, 25));

        lbl_IQ3_titulo2.setText("Titulo");
        lbl_IQ3_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IQ3_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1160, 80, 25));

        lbl_IQ3_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_IQ3_error.setText(" . . .");
        jp_Componentes.add(lbl_IQ3_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1150, 240, 35));
        jp_Componentes.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1404, 1093, -1));

        lbl_CAC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_CAC_error.setText(" . . .");
        jp_Componentes.add(lbl_CAC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1440, 340, 35));

        chbx_COA_Pregunta.setText("Titulo");
        jp_Componentes.add(chbx_COA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1410, 300, 30));

        txt_CPC_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_CPC_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_CPC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1490, 130, 25));

        lbl_CPC_titulo1.setText("Titulo");
        lbl_CPC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CPC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1490, 300, 30));

        lbl_CQC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_CQC_error.setText(" . . .");
        jp_Componentes.add(lbl_CQC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1520, 340, 35));

        lbl_CQC_titulo2.setText("Titulo");
        lbl_CQC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CQC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1530, 80, 25));

        lbl_CPC_titulo2.setText("Titulo");
        lbl_CPC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CPC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1490, 80, 25));

        lbl_CPC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_CPC_error.setText(" . . .");
        jp_Componentes.add(lbl_CPC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1480, 340, 35));

        lbl_CAC_titulo1.setText("Titulo");
        lbl_CAC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1450, 300, 30));

        lbl_CAC_titulo2.setText("Titulo");
        lbl_CAC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1450, 80, 25));

        lbl_CKC_titulo1.setText("Titulo");
        lbl_CKC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CKC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1570, 300, 30));

        txt_CAC_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_CAC_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_CAC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1450, 130, 25));

        lbl_CKC_titulo2.setText("Titulo");
        lbl_CKC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CKC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1570, 80, 25));

        lbl_CKC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_CKC_error.setText(" . . .");
        jp_Componentes.add(lbl_CKC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1560, 340, 35));

        txt_CQC_Pregunta.setEditable(false);
        jp_Componentes.add(txt_CQC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1530, 130, 25));

        lbl_CQC_titulo1.setText("Titulo");
        lbl_CQC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CQC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1530, 300, 30));

        txt_CKC_Pregunta.setEditable(false);
        jp_Componentes.add(txt_CKC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1570, 130, 25));
        jp_Componentes.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1612, 1083, 2));

        txt_YPI_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_YPI_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_YPI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1710, 130, 25));

        lbl_YPI_titulo1.setText("Titulo");
        lbl_YPI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_YPI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1710, 300, 30));

        lbl_YQI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_YQI_error.setText(" . . .");
        jp_Componentes.add(lbl_YQI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1740, 200, 35));

        lbl_YAI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_YAI_error.setText(" . . .");
        jp_Componentes.add(lbl_YAI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1660, 200, 35));

        chbx_YOA_Pregunta.setText("Titulo");
        jp_Componentes.add(chbx_YOA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1620, 300, 30));

        lbl_YQI_titulo2.setText("Titulo");
        lbl_YQI_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_YQI_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1750, 80, 25));

        lbl_YKI_titulo1.setText("Titulo");
        lbl_YKI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_YKI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1790, 300, 30));

        txt_YAI_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_YAI_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_YAI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1670, 130, 25));

        lbl_YAI_titulo1.setText("Titulo");
        lbl_YAI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_YAI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1660, 300, 30));

        lbl_YAI_titulo2.setText("Titulo");
        lbl_YAI_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_YAI_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1670, 80, 25));

        txt_YQI_Pregunta.setEditable(false);
        jp_Componentes.add(txt_YQI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1750, 130, 25));

        lbl_YQI_titulo1.setText("Titulo");
        lbl_YQI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_YQI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1750, 300, 30));

        lbl_YKI_titulo2.setText("Titulo");
        lbl_YKI_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_YKI_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1790, 80, 25));

        lbl_YKI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_YKI_error.setText(" . . .");
        jp_Componentes.add(lbl_YKI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1780, 200, 35));

        lbl_YPI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_YPI_error.setText(" . . .");
        jp_Componentes.add(lbl_YPI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1700, 200, 35));

        lbl_YPI_titulo2.setText("Titulo");
        lbl_YPI_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_YPI_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1710, 80, 25));

        txt_YKI_Pregunta.setEditable(false);
        jp_Componentes.add(txt_YKI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1790, 130, 25));
        jp_Componentes.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1830, 1093, 2));

        lbl_Q2C_titulo1.setText("Titulo");
        lbl_Q2C_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2C_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1880, 300, 30));

        txt_Q2C_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q2C_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1880, 130, 25));

        lbl_Q2K_titulo2.setText("Titulo");
        lbl_Q2K_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2K_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1920, 80, 25));

        lbl_Q2K_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_Q2K_error.setText(" . . .");
        jp_Componentes.add(lbl_Q2K_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1910, 340, 35));

        txt_Q2K_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q2K_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1920, 130, 25));

        lbl_Q2K_titulo1.setText("Titulo");
        lbl_Q2K_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2K_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1920, 300, 30));

        lbl_Q2C_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_Q2C_error.setText(" . . .");
        jp_Componentes.add(lbl_Q2C_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1870, 340, 35));

        lbl_Q2C_titulo2.setText("Titulo");
        lbl_Q2C_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2C_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1880, 80, 25));

        lbl_Q2C_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_Q2C_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_Q2C_desc.setText("Desc");
        lbl_Q2C_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2C_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1840, 610, 30));
        jp_Componentes.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1958, 1093, 2));

        lbl_Q3M_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_Q3M_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_Q3M_desc.setText("Desc");
        lbl_Q3M_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q3M_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1970, 610, 30));
        jp_Componentes.add(txt_Q3M_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 2010, 130, 25));

        lbl_Q3M_titulo1.setText("Titulo");
        lbl_Q3M_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q3M_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2010, 300, 30));

        lbl_Q3M_titulo2.setText("Titulo");
        lbl_Q3M_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q3M_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 2010, 80, 25));

        lbl_Q3M_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_Q3M_error.setText(" . . .");
        jp_Componentes.add(lbl_Q3M_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 2000, 200, 35));

        txt_Q3K_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q3K_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 2140, 130, 25));

        lbl_Q3C_titulo2.setText("Titulo");
        lbl_Q3C_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q3C_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 2090, 80, 25));

        lbl_Q3C_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_Q3C_error.setText(" . . .");
        jp_Componentes.add(lbl_Q3C_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 2080, 200, 35));

        txt_Q3C_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q3C_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 2090, 130, 25));

        lbl_Q3C_titulo1.setText("Titulo");
        lbl_Q3C_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q3C_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2090, 300, 30));

        lbl_Q3K_titulo2.setText("Titulo");
        lbl_Q3K_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q3K_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 2140, 80, 25));

        lbl_Q3K_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_Q3K_error.setText(" . . .");
        jp_Componentes.add(lbl_Q3K_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 2130, 200, 35));

        lbl_Q3K_titulo1.setText("Titulo");
        lbl_Q3K_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q3K_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2140, 300, 30));

        lbl_Q1H_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_Q1H_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_Q1H_desc.setText("Desc");
        lbl_Q1H_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q1H_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2190, 610, 30));

        lbl_Q1C_titulo1.setText("Titulo");
        lbl_Q1C_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q1C_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2270, 300, 30));

        txt_Q1C_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q1C_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 2270, 130, 25));

        lbl_Q1C_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_Q1C_error.setText(" . . .");
        jp_Componentes.add(lbl_Q1C_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 2260, 200, 35));

        lbl_Q1C_titulo2.setText("Titulo");
        lbl_Q1C_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q1C_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 2270, 80, 25));

        lbl_Q1H_titulo1.setText("Titulo");
        lbl_Q1H_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q1H_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2230, 300, 30));
        jp_Componentes.add(txt_Q1H_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 2230, 130, 25));

        txt_Q1K_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q1K_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 2310, 130, 25));

        lbl_Q1K_titulo2.setText("Titulo");
        lbl_Q1K_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q1K_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 2310, 80, 25));

        lbl_Q1K_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_Q1K_error.setText(" . . .");
        jp_Componentes.add(lbl_Q1K_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 2300, 200, 35));

        lbl_Q1H_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_Q1H_error.setText(" . . .");
        jp_Componentes.add(lbl_Q1H_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 2220, 200, 35));

        lbl_Q1H_titulo2.setText("Titulo");
        lbl_Q1H_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q1H_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 2230, 80, 25));

        lbl_Q1K_titulo1.setText("Titulo");
        lbl_Q1K_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q1K_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2310, 300, 30));
        jp_Componentes.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 2348, 1083, 2));

        btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Button_Salva_Guardar_21.png"))); // NOI18N
        btn_guardar.setText("cerrar");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2360, 120, -1));

        btn_close.setText("Salir");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2400, 120, 30));

        lbl_save_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error.setText(" . . .");
        jp_Componentes.add(lbl_save_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 2360, 520, 35));

        lbl_save_error2.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error2.setText(" . . .");
        jp_Componentes.add(lbl_save_error2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 2400, 520, 35));

        txt_QAK_Pregunta.setEditable(false);
        jp_Componentes.add(txt_QAK_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 380, 130, 25));

        txt_QEK_Pregunta.setEditable(false);
        jp_Componentes.add(txt_QEK_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 380, 130, 25));

        txt_QEC_Pregunta.setEditable(false);
        jp_Componentes.add(txt_QEC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 340, 130, 25));

        txt_QET_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_QET_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_QET_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 300, 130, 25));

        rbtn_QCE_Pregunta.setText("Titulo");
        jp_Componentes.add(rbtn_QCE_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 550, 300, 30));

        rbtn_QNL_Pregunta.setText("Titulo");
        jp_Componentes.add(rbtn_QNL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 670, 300, 30));

        lbl_IRM_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_IRM_desc.setText("Desc");
        lbl_IRM_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IRM_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 880, 340, 30));

        lbl_IK1_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_IK1_error.setText(" . . .");
        jp_Componentes.add(lbl_IK1_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1310, 340, 35));

        lbl_IK1_titulo2.setText("Titulo");
        lbl_IK1_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IK1_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1320, 80, 25));

        lbl_IQ1_titulo2.setText("Titulo");
        lbl_IQ1_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IQ1_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1280, 80, 25));

        lbl_IQ1_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_IQ1_error.setText(" . . .");
        jp_Componentes.add(lbl_IQ1_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1270, 340, 35));

        lbl_ICC_titulo1.setText("Titulo");
        lbl_ICC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_ICC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1370, 300, 30));

        lbl_ICM_titulo1.setText("Titulo");
        lbl_ICM_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_ICM_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1240, 300, 30));

        lbl_ICM_titulo2.setText("Titulo");
        lbl_ICM_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_ICM_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1240, 80, 25));

        txt_ICM_Pregunta.setEditable(false);
        txt_ICM_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_ICM_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_ICM_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1240, 130, 25));

        lbl_ICC_titulo2.setText("Titulo");
        lbl_ICC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_ICC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1370, 80, 25));

        lbl_ICC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_ICC_error.setText(" . . .");
        jp_Componentes.add(lbl_ICC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1360, 340, 35));

        lbl_IK1_titulo1.setText("Titulo");
        lbl_IK1_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IK1_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1320, 300, 30));

        txt_ICC_Pregunta.setEditable(false);
        txt_ICC_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_ICC_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_ICC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1360, 130, 25));

        txt_IK1_Pregunta.setEditable(false);
        jp_Componentes.add(txt_IK1_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1320, 130, 25));

        lbl_IQ1_titulo1.setText("Titulo");
        lbl_IQ1_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_IQ1_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1280, 300, 30));

        txt_IQ1_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_IQ1_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_IQ1_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1280, 130, 25));

        lbl_ICM_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_ICM_error.setText(" . . .");
        jp_Componentes.add(lbl_ICM_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1230, 340, 35));

        chbx_ICO_Pregunta.setText("Titulo");
        jp_Componentes.add(chbx_ICO_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 1160, 180, 25));

        chbx_Q3R_Pregunta.setText("Titulo");
        jp_Componentes.add(chbx_Q3R_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2050, 300, 30));

        rbtn_QCD_Pregunta1.setText("Titulo");
        jp_Componentes.add(rbtn_QCD_Pregunta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 430, 80, 25));

        rbtn_QCD_Pregunta2.setText("Titulo");
        jp_Componentes.add(rbtn_QCD_Pregunta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 430, 80, 25));

        lbl_QCD_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_QCD_error.setText(" . . .");
        jp_Componentes.add(lbl_QCD_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 430, 340, 35));
        jp_Componentes.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 2186, 1093, 2));

        btn_QIF_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_QIF_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_QIF_ayuda.setContentAreaFilled(false);
        btn_QIF_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_QIF_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 150, 25, 25));

        btn_QAI_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_QAI_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_QAI_ayuda.setContentAreaFilled(false);
        btn_QAI_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_QAI_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 300, 25, 25));

        btn_QET_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_QET_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_QET_ayuda.setContentAreaFilled(false);
        btn_QET_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_QET_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 300, 25, 25));

        btn_QCE_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_QCE_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_QCE_ayuda.setContentAreaFilled(false);
        btn_QCE_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_QCE_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 430, 25, 25));

        btn_QCM_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_QCM_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_QCM_ayuda.setContentAreaFilled(false);
        btn_QCM_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_QCM_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 510, 25, 25));

        btn_QNL_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_QNL_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_QNL_ayuda.setContentAreaFilled(false);
        btn_QNL_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_QNL_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 670, 25, 25));

        btn_IOA_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_IOA_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_IOA_ayuda.setContentAreaFilled(false);
        btn_IOA_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_IOA_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 840, 30, 25));

        btn_IPI_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_IPI_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_IPI_ayuda.setContentAreaFilled(false);
        btn_IPI_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_IPI_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 960, 30, 25));

        btn_ICC_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_ICC_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_ICC_ayuda.setContentAreaFilled(false);
        btn_ICC_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_ICC_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 1360, 30, 25));

        btn_ICM_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_ICM_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_ICM_ayuda.setContentAreaFilled(false);
        btn_ICM_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_ICM_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 1240, 30, 25));

        btn_Q3R_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_Q3R_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_Q3R_ayuda.setContentAreaFilled(false);
        btn_Q3R_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_Q3R_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 2010, 30, 25));

        btn_YPI_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_YPI_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_YPI_ayuda.setContentAreaFilled(false);
        btn_YPI_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_YPI_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 1710, 30, 25));

        btn_YOA_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_YOA_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_YOA_ayuda.setContentAreaFilled(false);
        btn_YOA_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_YOA_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 1620, 30, 25));

        btn_CPC_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_CPC_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_CPC_ayuda.setContentAreaFilled(false);
        btn_CPC_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_CPC_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 1490, 30, 25));

        btn_COA_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_COA_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_COA_ayuda.setContentAreaFilled(false);
        btn_COA_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_COA_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 1410, 30, 25));

        btn_Q2C_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_Q2C_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_Q2C_ayuda.setContentAreaFilled(false);
        btn_Q2C_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_Q2C_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 1880, 30, 25));

        btn_Q1H_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_Q1H_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_Q1H_ayuda.setContentAreaFilled(false);
        btn_Q1H_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_Q1H_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 2190, 30, 25));

        btn_Q3M_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_Q3M_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_Q3M_ayuda.setContentAreaFilled(false);
        btn_Q3M_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_Q3M_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 1970, 30, 25));

        btn_Q1C_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_Q1C_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_Q1C_ayuda.setContentAreaFilled(false);
        btn_Q1C_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_Q1C_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 2230, 30, 25));

        jsp_CalculoCaudalesDiseno.setViewportView(jp_Componentes);

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
            .addComponent(jsp_CalculoCaudalesDiseno, javax.swing.GroupLayout.DEFAULT_SIZE, 1008, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_close2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btn_close2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jsp_CalculoCaudalesDiseno, javax.swing.GroupLayout.DEFAULT_SIZE, 1451, Short.MAX_VALUE)
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
        bod.WinCalculoCaudalesDiseno = false;
        this.dispose();
    }//GEN-LAST:event_btn_closeActionPerformed

    private void txt_IAI_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_IAI_PreguntaKeyReleased
        //Listener para calcular automática y temporalmente IQI e IKI //Todo: este metodo release funciona mejor que los otros, cambiar los otros
        calcularIQIIKI();
    }//GEN-LAST:event_txt_IAI_PreguntaKeyReleased

    private void txt_IPI_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_IPI_PreguntaKeyReleased
        //Listener para calcular automática y temporalmente IQI e IKI //Todo: este metodo release funciona mejor que los otros, cambiar los otros
        calcularIQIIKI();
    }//GEN-LAST:event_txt_IPI_PreguntaKeyReleased

    private void txt_IQ2_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_IQ2_PreguntaKeyReleased
        //Listener para calcular automática y temporalmente IK2
        calcularIK2();
    }//GEN-LAST:event_txt_IQ2_PreguntaKeyReleased

    private void txt_IQ3_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_IQ3_PreguntaKeyReleased
        //Listener para calcular automática y temporalmente IK3
        calcularIK3();
    }//GEN-LAST:event_txt_IQ3_PreguntaKeyReleased

    private void txt_ICM_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_ICM_PreguntaKeyReleased
        //Listener para calcular automática y temporalmente IK3
        calcularIK3();
    }//GEN-LAST:event_txt_ICM_PreguntaKeyReleased

    private void txt_IQ1_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_IQ1_PreguntaKeyReleased
        //Listener para calcular automática y temporalmente IK1
        calcularIK1();
    }//GEN-LAST:event_txt_IQ1_PreguntaKeyReleased

    private void txt_ICC_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_ICC_PreguntaKeyReleased
        //Listener para calcular automática y temporalmente IK1
        calcularIK1();
    }//GEN-LAST:event_txt_ICC_PreguntaKeyReleased

    private void txt_CAC_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_CAC_PreguntaKeyReleased
        //Listener para calcular automática y temporalmente CQC y CKC
        calcularCQCCKC();
    }//GEN-LAST:event_txt_CAC_PreguntaKeyReleased

    private void txt_CPC_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_CPC_PreguntaKeyReleased
        //Listener para calcular automática y temporalmente CQC y CKC
        calcularCQCCKC();
    }//GEN-LAST:event_txt_CPC_PreguntaKeyReleased

    private void txt_YAI_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_YAI_PreguntaKeyReleased
        //Listener para calcular automática y temporalmente YQI y YKI
        calcularYQIYKI();
    }//GEN-LAST:event_txt_YAI_PreguntaKeyReleased

    private void txt_YPI_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_YPI_PreguntaKeyReleased
        //Listener para calcular automática y temporalmente YQI y YKI
        calcularYQIYKI();
    }//GEN-LAST:event_txt_YPI_PreguntaKeyReleased

    private void txt_QET_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_QET_PreguntaKeyReleased
        //Listener para calcular automática y temporalmente QEC y QEK 
        calcularQECQEK();
    }//GEN-LAST:event_txt_QET_PreguntaKeyReleased

    private void txt_QEL_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_QEL_PreguntaKeyReleased
        //Listener para calcular automática y temporalmente QEC y QEK 
        calcularQECQEK();
    }//GEN-LAST:event_txt_QEL_PreguntaKeyReleased

    private void txt_QAA_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_QAA_PreguntaKeyReleased
        //Listener para calcular automática y temporalmente QAC y QAK
        calcularQACQAK();
    }//GEN-LAST:event_txt_QAA_PreguntaKeyReleased

    private void txt_QAI_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_QAI_PreguntaKeyReleased
        //Listener para calcular automática y temporalmente QAC y QAK
        calcularQACQAK();
    }//GEN-LAST:event_txt_QAI_PreguntaKeyReleased

    private void txt_QCA_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_QCA_PreguntaKeyReleased
        //Listener para calcular automática y temporalmente QCC y QCK
        if (rbtn_QCE_Pregunta.isSelected()) {
            calcularQCCQCK();
        }
        if (rbtn_QNL_Pregunta.isSelected()) {
            calcularQNCQNK();
        }
    }//GEN-LAST:event_txt_QCA_PreguntaKeyReleased

    private void txt_QCM_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_QCM_PreguntaKeyReleased
        //Listener para calcular automática y temporalmente QCC y QCK
        if (rbtn_QCE_Pregunta.isSelected()) {
            calcularQCCQCK();
        }
        if (rbtn_QNL_Pregunta.isSelected()) {
            calcularQNCQNK();
        }
    }//GEN-LAST:event_txt_QCM_PreguntaKeyReleased

    private void btn_close2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_close2ActionPerformed
        bod.WinCalculoCaudalesDiseno = false;
        this.dispose();
    }//GEN-LAST:event_btn_close2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_COA_ayuda;
    private javax.swing.JButton btn_CPC_ayuda;
    private javax.swing.JButton btn_ICC_ayuda;
    private javax.swing.JButton btn_ICM_ayuda;
    private javax.swing.JButton btn_IOA_ayuda;
    private javax.swing.JButton btn_IPI_ayuda;
    private javax.swing.JButton btn_Q1C_ayuda;
    private javax.swing.JButton btn_Q1H_ayuda;
    private javax.swing.JButton btn_Q2C_ayuda;
    private javax.swing.JButton btn_Q3M_ayuda;
    private javax.swing.JButton btn_Q3R_ayuda;
    private javax.swing.JButton btn_QAI_ayuda;
    private javax.swing.JButton btn_QCE_ayuda;
    private javax.swing.JButton btn_QCM_ayuda;
    private javax.swing.JButton btn_QET_ayuda;
    private javax.swing.JButton btn_QIF_ayuda;
    private javax.swing.JButton btn_QNL_ayuda;
    private javax.swing.JButton btn_YOA_ayuda;
    private javax.swing.JButton btn_YPI_ayuda;
    private javax.swing.JButton btn_close;
    private javax.swing.JButton btn_close2;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JCheckBox chbx_COA_Pregunta;
    private javax.swing.JCheckBox chbx_ICO_Pregunta;
    private javax.swing.JCheckBox chbx_IOA_pregunta;
    private javax.swing.JCheckBox chbx_Q3R_Pregunta;
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
    private javax.swing.JLabel lbl_IAI_error;
    private javax.swing.JLabel lbl_IAI_titulo1;
    private javax.swing.JLabel lbl_IAI_titulo2;
    private javax.swing.JLabel lbl_ICC_error;
    private javax.swing.JLabel lbl_ICC_titulo1;
    private javax.swing.JLabel lbl_ICC_titulo2;
    private javax.swing.JLabel lbl_ICM_error;
    private javax.swing.JLabel lbl_ICM_titulo1;
    private javax.swing.JLabel lbl_ICM_titulo2;
    private javax.swing.JLabel lbl_IK1_error;
    private javax.swing.JLabel lbl_IK1_titulo1;
    private javax.swing.JLabel lbl_IK1_titulo2;
    private javax.swing.JLabel lbl_IK2_error;
    private javax.swing.JLabel lbl_IK2_titulo1;
    private javax.swing.JLabel lbl_IK2_titulo2;
    private javax.swing.JLabel lbl_IK3_error;
    private javax.swing.JLabel lbl_IK3_titulo1;
    private javax.swing.JLabel lbl_IK3_titulo2;
    private javax.swing.JLabel lbl_IKI_error;
    private javax.swing.JLabel lbl_IKI_titulo1;
    private javax.swing.JLabel lbl_IKI_titulo2;
    private javax.swing.JLabel lbl_IOA_desc;
    private javax.swing.JLabel lbl_IPI_error;
    private javax.swing.JLabel lbl_IPI_titulo1;
    private javax.swing.JLabel lbl_IPI_titulo2;
    private javax.swing.JLabel lbl_IQ1_error;
    private javax.swing.JLabel lbl_IQ1_titulo1;
    private javax.swing.JLabel lbl_IQ1_titulo2;
    private javax.swing.JLabel lbl_IQ2_error;
    private javax.swing.JLabel lbl_IQ2_titulo1;
    private javax.swing.JLabel lbl_IQ2_titulo2;
    private javax.swing.JLabel lbl_IQ3_error;
    private javax.swing.JLabel lbl_IQ3_titulo1;
    private javax.swing.JLabel lbl_IQ3_titulo2;
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
    private javax.swing.JLabel lbl_QCD_error;
    private javax.swing.JLabel lbl_QCE_desc;
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
    private javax.swing.JLabel lbl_QNC_error;
    private javax.swing.JLabel lbl_QNC_titulo1;
    private javax.swing.JLabel lbl_QNC_titulo2;
    private javax.swing.JLabel lbl_QNK_error;
    private javax.swing.JLabel lbl_QNK_titulo1;
    private javax.swing.JLabel lbl_QNK_titulo2;
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
    private javax.swing.JLabel lbl_save_error;
    private javax.swing.JLabel lbl_save_error2;
    private javax.swing.JLabel lbl_titulo1;
    private javax.swing.JLabel lbl_titulo2;
    private javax.swing.JRadioButton rbtn_IRM_Pregunta1;
    private javax.swing.JRadioButton rbtn_IRM_Pregunta2;
    private javax.swing.JRadioButton rbtn_QCD_Pregunta1;
    private javax.swing.JRadioButton rbtn_QCD_Pregunta2;
    private javax.swing.JRadioButton rbtn_QCE_Pregunta;
    private javax.swing.JRadioButton rbtn_QIA_Pregunta;
    private javax.swing.JRadioButton rbtn_QIF_Pregunta;
    private javax.swing.JRadioButton rbtn_QNL_Pregunta;
    private javax.swing.JTextField txt_CAC_Pregunta;
    private javax.swing.JTextField txt_CAR_Pregunta;
    private javax.swing.JTextField txt_CKC_Pregunta;
    private javax.swing.JTextField txt_CPC_Pregunta;
    private javax.swing.JTextField txt_CQC_Pregunta;
    private javax.swing.JTextField txt_IAI_Pregunta;
    private javax.swing.JTextField txt_ICC_Pregunta;
    private javax.swing.JTextField txt_ICM_Pregunta;
    private javax.swing.JTextField txt_IK1_Pregunta;
    private javax.swing.JTextField txt_IK2_Pregunta;
    private javax.swing.JTextField txt_IK3_Pregunta;
    private javax.swing.JTextField txt_IKI_Pregunta;
    private javax.swing.JTextField txt_IPI_Pregunta;
    private javax.swing.JTextField txt_IQ1_Pregunta;
    private javax.swing.JTextField txt_IQ2_Pregunta;
    private javax.swing.JTextField txt_IQ3_Pregunta;
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
    private javax.swing.JTextField txt_QNC_Pregunta;
    private javax.swing.JTextField txt_QNK_Pregunta;
    private javax.swing.JTextField txt_YAI_Pregunta;
    private javax.swing.JTextField txt_YKI_Pregunta;
    private javax.swing.JTextField txt_YPI_Pregunta;
    private javax.swing.JTextField txt_YQI_Pregunta;
    // End of variables declaration//GEN-END:variables
}
