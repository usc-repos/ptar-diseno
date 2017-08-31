package UI;

import BO.Bod;
import DB.Dao;
import java.io.File;
import Componentes.Util;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.apache.log4j.Logger;
import Componentes.Validaciones;
import Componentes.Configuracion;
import Componentes.Listener_Popup;
import Componentes.Listener_Texto;
import java.awt.Color;
import java.awt.Component;
import java.sql.ResultSet;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;

public class Desarenador extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("Desarenador"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.ObtenerKey("rutabasedatos") + File.separator + conf.ObtenerKey("nombrebasedatos"));
    private Validaciones validar = new Validaciones();
    private Bod bod = new Bod("");
    private Listener_Popup Lpopup;
    private Listener_Texto Ltexto;
    Util util = new Util();
    //---------------------------- Variables generales
    private ButtonGroup btnGrupLLA = new ButtonGroup();
    private boolean esErrorDAN = true;
    private String listaDAN;
    private String listaDCK;
    private String listaDPA;
    private double numDAN = 0;
    //---------------------------- Variables guardar msg error
    private String errorDAN;
    private String errorDCK;
    private String errorDCN;
    private String errorDP1;
    private String errorDP2;
    private String errorDP3;
    private String errorDRZ;
    private String errorDAH;
    private String errorDAB;
    private String errorDVF;
    private String errorDLD;
    private String errorDAL;
    private String errorDEM;
    private String errorDFL;
    private String errorDUP;
    //---------------------------- Variables guardar rangos 
    private double minDAN;
    private double maxDAN;
    private double minDCK;
    private double maxDCK;
    private double minDCN;
    private double maxDCN;
    private double minDP1;
    private double maxDP1;
    private double minDP2;
    private double maxDP2;
    private double minDP3;
    private double maxDP3;
    private double minDRZ;
    private double maxDRZ;
    private double minDAH;
    private double maxDAH;
    private double minDAB;
    private double maxDAB;
    private double minDVF;
    private double maxDVF;
    private double minDLD;
    private double maxDLD;
    private double minDAL;
    private double maxDAL;
    private double minDEM;
    private double maxDEM;
    private double minDUP;
    private double maxDUP;
    private int minDFL;
    private int maxDFL;

    public Desarenador(Bod bod) {
        this.bod = bod;
        initComponents();
        bod.WinDesarenador = true;
        InicializarComponentes();
    }

    /**
     * Inicializa todos los elementos (jlabels,jtextbox,etc.) con sus
     * respectivos valores y textos desde al base de datos.
     */
    public void InicializarComponentes() {

        ResultSet rs;
        bod.WinDesarenador = true;//Bandera La ventana se ha abierto

        try {//Todo: implementar donde esta error
            btn_guardar.setText("Guardar");
            btn_close.setText("Cerrar");

            // - - - - - - Titulo de la sección            
            rs = db.ResultadoSelect("obtenerseccion", "9", null);

            lbl_titulo1.setText(rs.getString("Nombre")); //Título de este jpane
            lbl_titulo2.setText(rs.getString("Mensaje"));

            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q2C
            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q2K"); //Aqui se obtiene solo el titulo 2 de Q2K em metroscubicos

            String titulo2 = rs.getString("titulo2").replace("d", "s");

            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q2C");

            lbl_Q2C_titulo1.setText(rs.getString("descripcion") + rs.getString("titulo1"));
            lbl_Q2C_titulo2.setText(titulo2);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q2C
            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q1C");

            lbl_Q1C_titulo1.setText(rs.getString("descripcion") + rs.getString("titulo1"));
            lbl_Q1C_titulo2.setText(titulo2);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q3C
            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q3C");

            lbl_Q3C_titulo1.setText(rs.getString("descripcion") + rs.getString("titulo1"));
            lbl_Q3C_titulo2.setText(titulo2);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DAN  
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DAN");

            minDAN = rs.getDouble("rangomin");
            maxDAN = rs.getDouble("rangomax");
            errorDAN = rs.getString("errormsg");
            listaDAN = rs.getString("valorpordefecto");
            lbl_DAN_desc.setText(rs.getString("descripcion"));
            lbl_DAN_titulo1.setText(rs.getString("titulo1"));
            lbl_DAN_titulo2.setText(rs.getString("titulo2"));
            AsignarPopup(lbl_DAN_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 280, 480);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DCK  
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DCK");

            minDCK = rs.getDouble("rangomin");
            maxDCK = rs.getDouble("rangomax");
            errorDCK = rs.getString("errormsg");
            listaDCK = rs.getString("valorpordefecto"); //Aqui se guardo la tabla de ayuda, se recupera
            lbl_DCK_desc.setText(rs.getString("descripcion"));
            lbl_DCK_titulo1.setText(rs.getString("titulo1"));
            lbl_DCK_titulo2.setText(rs.getString("titulo2"));
            AsignarPopup(lbl_DCK_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 380, 490);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DCN  
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DCN");

            minDCN = rs.getDouble("rangomin");
            maxDCN = rs.getDouble("rangomax");
            errorDCN = rs.getString("errormsg");
            lbl_DCN_titulo1.setText(rs.getString("titulo1"));
            lbl_DCN_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DP1  
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DP1");

            minDP1 = rs.getDouble("rangomin");
            maxDP1 = rs.getDouble("rangomax");
            errorDP1 = rs.getString("errormsg");
            lbl_DP1_titulo1.setText(rs.getString("titulo1"));
            lbl_DP1_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_DP1_Pregunta, minDP1, maxDP1, lbl_DP1_error, errorDP1);
            AsignarPopup(lbl_DP1_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 490, 500);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DP2  
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DP2");

            minDP2 = rs.getDouble("rangomin");
            maxDP2 = rs.getDouble("rangomax");
            errorDP2 = rs.getString("errormsg");
            lbl_DP2_titulo1.setText(rs.getString("titulo1"));
            lbl_DP2_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_DP2_Pregunta, minDP2, maxDP2, lbl_DP2_error, errorDP2);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DP3  
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DP3");

            minDP3 = rs.getDouble("rangomin");
            maxDP3 = rs.getDouble("rangomax");
            errorDP3 = rs.getString("errormsg");
            lbl_DP3_titulo1.setText(rs.getString("titulo1"));
            lbl_DP3_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_DP3_Pregunta, minDP3, maxDP3, lbl_DP3_error, errorDP3);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DRZ  
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DRZ");

            minDRZ = rs.getDouble("rangomin");
            maxDRZ = rs.getDouble("rangomax");
            errorDRZ = rs.getString("errormsg");
            lbl_DRZ_titulo1.setText(rs.getString("titulo1"));
            lbl_DRZ_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_DRZ_Pregunta, minDRZ, maxDRZ, lbl_DRZ_error, errorDRZ);
            AsignarPopup(lbl_DRZ_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 380, 320);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DPA  
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DPA");

            listaDPA = rs.getString("errormsg"); //Aqui se guardo la tabla de ayuda, se recupera
            lbl_DPA_desc.setText(rs.getString("descripcion"));
            lbl_DPA_titulo1.setText(rs.getString("titulo1"));
            lbl_DPA_titulo2.setText(rs.getString("titulo2"));
            AsignarPopup(lbl_DPA_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 490, 500);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DPB 
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DPB");

            lbl_DPB_titulo1.setText(rs.getString("titulo1"));
            lbl_DPB_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DPC 
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DPC");

            lbl_DPC_titulo1.setText(rs.getString("titulo1"));
            lbl_DPC_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DPD 
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DPD");

            lbl_DPD_titulo1.setText(rs.getString("titulo1"));
            lbl_DPD_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DPE 
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DPE");

            lbl_DPE_titulo1.setText(rs.getString("titulo1"));
            lbl_DPE_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DPF 
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DPF");

            lbl_DPF_titulo1.setText(rs.getString("titulo1"));
            lbl_DPF_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DPG 
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DPG");

            lbl_DPG_titulo1.setText(rs.getString("titulo1"));
            lbl_DPG_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DPK 
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DPK");

            lbl_DPK_titulo1.setText(rs.getString("titulo1"));
            lbl_DPK_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DPN 
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DPN");

            lbl_DPN_titulo1.setText(rs.getString("titulo1"));
            lbl_DPN_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DAH  
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DAH");

            minDAH = rs.getDouble("rangomin");
            maxDAH = rs.getDouble("rangomax");
            errorDAH = rs.getString("errormsg");
            lbl_DAH_titulo1.setText(rs.getString("titulo1"));
            lbl_DAH_titulo2.setText(rs.getString("titulo2"));
            lbl_DAH_desc.setText(rs.getString("descripcion"));
            AsignarTextDoble(txt_DAH_Pregunta, minDAH, maxDAH, lbl_DAH_error, errorDAH);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DAB  
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DAB");

            minDAB = rs.getDouble("rangomin");
            maxDAB = rs.getDouble("rangomax");
            errorDAB = rs.getString("errormsg");
            lbl_DAB_titulo1.setText(rs.getString("titulo1"));
            lbl_DAB_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_DAB_Pregunta, minDAB, maxDAB, lbl_DAB_error, errorDAB);
            AsignarPopup(lbl_DAB_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 440, 300);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DVF 
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DVF");

            minDVF = rs.getDouble("rangomin");
            maxDVF = rs.getDouble("rangomax");
            errorDVF = rs.getString("errormsg");
            lbl_DVF_titulo1.setText(rs.getString("titulo1"));
            lbl_DVF_titulo2.setText(rs.getString("titulo2"));
            txt_DVF_Pregunta.setText(rs.getString("valorpordefecto"));
            AsignarTextDoble(txt_DVF_Pregunta, minDVF, maxDVF, lbl_DVF_error, errorDVF);
            AsignarPopup(lbl_DVF_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 410, 210);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DLD 
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DLD");

            minDLD = rs.getDouble("rangomin");
            maxDLD = rs.getDouble("rangomax");
            errorDLD = rs.getString("errormsg");
            lbl_DLD_titulo1.setText(rs.getString("titulo1"));
            lbl_DLD_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_DLD_Pregunta, minDLD, maxDLD, lbl_DLD_error, errorDLD);
            AsignarPopup(lbl_DLD_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 410, 210);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DAL 
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DAL");

            minDAL = rs.getDouble("rangomin");
            maxDAL = rs.getDouble("rangomax");
            errorDAL = rs.getString("errormsg");
            lbl_DAL_titulo1.setText(rs.getString("titulo1"));
            lbl_DAL_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_DAL_Pregunta, minDAL, maxDAL, lbl_DAL_error, errorDAL);
            AsignarPopup(lbl_DAL_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 390, 250);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DEM 
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DEM");

            minDEM = rs.getDouble("rangomin");
            maxDEM = rs.getDouble("rangomax");
            errorDEM = rs.getString("errormsg");
            lbl_DEM_titulo1.setText(rs.getString("titulo1"));
            lbl_DEM_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_DEM_Pregunta, minDEM, maxDEM, lbl_DEM_error, errorDEM);
            AsignarPopup(lbl_DEM_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 390, 270);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DFL 
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DFL");

            minDFL = rs.getInt("rangomin");
            maxDFL = rs.getInt("rangomax");
            errorDFL = rs.getString("errormsg");
            lbl_DFL_titulo1.setText(rs.getString("titulo1"));
            lbl_DFL_titulo2.setText(rs.getString("titulo2"));
            txt_DFL_Pregunta.setText(rs.getString("valorpordefecto"));
            AsignarTextEntero(txt_DFL_Pregunta, minDFL, maxDFL, lbl_DFL_error, errorDFL);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DUP 
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DUP");

            minDUP = rs.getDouble("rangomin");
            maxDUP = rs.getDouble("rangomax");
            errorDUP = rs.getString("errormsg");
            lbl_DUP_titulo1.setText(rs.getString("titulo1"));
            lbl_DUP_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_DUP_Pregunta, minDUP, maxDUP, lbl_DUP_error, errorDUP);
            AsignarPopup(lbl_DUP_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 390, 340);
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
            // Si Bod cargó datos de Laguna Anaerobia desde la BD, porque Ya estaba editada, se proceden a llenar casillas con datos
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -          
            if (bod.EditDesarenador) {
                txt_DAN_Pregunta.setText(bod.getDANs());
                txt_DCK_Pregunta.setText(bod.getDCKs());
                txt_DCN_Pregunta.setText(bod.getDCNs());
                txt_DP1_Pregunta.setText(bod.getDP1s());
                txt_DP2_Pregunta.setText(bod.getDP2s());
                txt_DP3_Pregunta.setText(bod.getDP3s());
                txt_DRZ_Pregunta.setText(bod.getDRZs());
                txt_DPA_Pregunta.setText(bod.getDPAs());
                txt_DPB_Pregunta.setText(bod.getDPBs());
                txt_DPC_Pregunta.setText(bod.getDPCs());
                txt_DPD_Pregunta.setText(bod.getDPDs());
                txt_DPE_Pregunta.setText(bod.getDPEs());
                txt_DPF_Pregunta.setText(bod.getDPFs());
                txt_DPG_Pregunta.setText(bod.getDPGs());
                txt_DPK_Pregunta.setText(bod.getDPKs());
                txt_DPN_Pregunta.setText(bod.getDPNs());
                txt_DAH_Pregunta.setText(bod.getDAHs());
                txt_DAB_Pregunta.setText(bod.getDABs());
                txt_DVF_Pregunta.setText(bod.getDVFs());
                txt_DLD_Pregunta.setText(bod.getDLDs());
                txt_DAL_Pregunta.setText(bod.getDALs());
                txt_DEM_Pregunta.setText(bod.getDEMs());
                txt_DFL_Pregunta.setText(bod.getDFL() + "");
                txt_DUP_Pregunta.setText(bod.getDUPs());
            } else {// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -si no otras tareas de cálculos que se deben hacer al cargar
               
                calcularDEM();
                calcularDVF();
                calcularDFL();
            }

            txt_Q1C_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(bod.calcularQ1C_m3())), "#.####"));
            txt_Q2C_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(bod.calcularQ2C_m3())), "#.####"));
            txt_Q3C_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(bod.calcularQ3C_m3())), "#.####"));
            lbl_DAN_error.setText("");

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
     * Comprueba todos los campos de esta ventana versus la clase de lógica de
     * datos. Hasta que esten correctos todos permite guardar.
     *
     * @return
     */
    private boolean Guardar() {

        try {
            ResultSet rs;
            lbl_save_error.setText("");
            lbl_save_error2.setText("");

            // - - - - - - Pregunta 1  - - - - - - - - - DAN // Ancho nominal (W)
            if (!calcularDAN()) {
                rs = db.ResultadoSelect("obtenernombretitulo", "DAN", null);
                lbl_save_error2.setText("Punto 1, Error, " + rs.getString("titulo1"));
                lbl_save_error.setText(errorDAN);
                return false;
            }
            // - - - - - - Pregunta 2  - - - - - - - - - DCK DCN //Coeficiente K y Coeficiente n. .Profundidad de la lámina de agua (H) para Qmax, Qmed y Qmin.             
            if (!calcularDCKDCN()) {

                rs = db.ResultadoSelect("obtenernombretitulo", "DCK", null);
                lbl_save_error2.setText("Punto 2-3, Error, " + rs.getString("titulo1"));
                lbl_save_error.setText("error NaN");
                return false;
            }
            // - - - - - - Pregunta 4  - - - - - - - - - DP1 // Hmax. Profundidad de la lámina de agua (H) para Qmax, Qmed y Qmin.
            if (!calcularDP1()) {
                rs = db.ResultadoSelect("obtenernombretitulo", "DP1", null);
                lbl_save_error2.setText("Punto 4, Error, " + rs.getString("titulo1"));
                lbl_save_error.setText(errorDP1);
                return false;
            }
            // - - - - - - Pregunta 5  - - - - - - - - - DP2 // Hmed. Profundidad de la lámina de agua (H) para Qmax, Qmed y Qmin.
            if (!calcularDP2()) {
                rs = db.ResultadoSelect("obtenernombretitulo", "DP2", null);
                lbl_save_error2.setText("Punto 5, Error, " + rs.getString("titulo1"));
                lbl_save_error.setText(errorDP2);
                return false;
            }
            // - - - - - - Pregunta 6  - - - - - - - - - DP3 // Hmin. Profundidad de la lámina de agua (H) para Qmax, Qmed y Qmin.
            if (!calcularDP3()) {
                rs = db.ResultadoSelect("obtenernombretitulo", "DP3", null);
                lbl_save_error2.setText("Punto 6, Error, " + rs.getString("titulo1"));
                lbl_save_error.setText(errorDP3);
                return false;
            }
            // - - - - - - Pregunta 7  - - - - - - - - - DRZ //Resalto (Z). Profundidad de la lámina de agua (H) para Qmax, Qmed y Qmin.
            if (!calcularDRZ()) {
                rs = db.ResultadoSelect("obtenernombretitulo", "DRZ", null);
                lbl_save_error2.setText("Punto 7, Error, " + rs.getString("titulo1"));
                lbl_save_error.setText(errorDRZ);
                return false;
            }
            // - - - - - - Pregunta 8  - - - - - - - - - DPA ...B,C,D,E,F,G,K,N //Dimensiones del medidor Parshall. A,B,C,D,E,F,G,K,N
            if (!calcularDPABCDEFGKN()) {

                rs = db.ResultadoSelect("obtenernombretitulo", "DPA", null);
                lbl_save_error2.setText("Punto 8 - 16, Error, " + rs.getString("titulo1"));
                lbl_save_error.setText("error");
                return false;
            }
            // - - - - - - Pregunta 17 - - - - - - - - - DAH // Altura máxima de lámina de agua en el desarenador (H)
            if (!calcularDAH()) {
                rs = db.ResultadoSelect("obtenernombretitulo", "DAH", null);
                lbl_save_error2.setText("Punto 17, Error, " + rs.getString("titulo1"));
                lbl_save_error.setText(errorDAH);
                return false;
            }
            //¡ debe ir antes de la pregunta 18 para usar en DAB!
            // - - - - - - Pregunta 19 - - - - - - - - - DVF // Velocidad del flujo (V) 
            if (!calcularDVF()) {
                rs = db.ResultadoSelect("obtenernombretitulo", "DVF", null);
                lbl_save_error2.setText("Punto 19, Error, " + rs.getString("titulo1"));
                lbl_save_error.setText(errorDVF);
                return false;
            }
            // - - - - - - Pregunta 18 - - - - - - - - - DAB // Ancho del desarenador (b)
            if (!calcularDAB()) {
                rs = db.ResultadoSelect("obtenernombretitulo", "DAB", null);
                lbl_save_error2.setText("Punto 18, Error, " + rs.getString("titulo1"));
                lbl_save_error.setText(errorDAB);
                return false;
            }
            // - - - - - - Pregunta 20- - - - - - - - - DLD // Longitud del desarenador (L)
            if (!calcularDLD()) {
                rs = db.ResultadoSelect("obtenernombretitulo", "DLD", null);
                lbl_save_error2.setText("Punto 20, Error, " + rs.getString("titulo1"));
                lbl_save_error.setText(errorDLD);
                return false;
            }
            // - - - - - - Pregunta 21 - - - - - - - - - DAL // Área longitudinal del desarenador (A)
            if (!calcularDAL()) {
                rs = db.ResultadoSelect("obtenernombretitulo", "DAL", null);
                lbl_save_error2.setText("Punto 21, Error, " + rs.getString("titulo1"));
                lbl_save_error.setText(errorDAL);
                return false;
            }
            // - - - - - - Pregunta 22 - - - - - - - - - DEM // Estimación de material retenido (q)
            if (!calcularDEM()) {
                rs = db.ResultadoSelect("obtenernombretitulo", "DEM", null);
                lbl_save_error2.setText("Punto 22, Error, " + rs.getString("titulo1"));
                lbl_save_error.setText(errorDEM);
                return false;
            }
            // - - - - - - Pregunta 23 - - - - - - - - - DFL // Frecuencia de limpieza (t)
            if (!calcularDFL()) {
                rs = db.ResultadoSelect("obtenernombretitulo", "DFL", null);
                lbl_save_error2.setText("Punto 23, Error, " + rs.getString("titulo1"));
                lbl_save_error.setText(errorDFL);
                return false;
            }
            // - - - - - - Pregunta 24 - - - - - - - - - DUP // Profundidad útil depósito inferior de arena (p)
            if (!calcularDUP()) {
                rs = db.ResultadoSelect("obtenernombretitulo", "DUP", null);
                lbl_save_error2.setText("Punto 24, Error, " + rs.getString("titulo1"));
                lbl_save_error.setText(errorDUP);
                return false;
            }

            bod.EditDesarenador = true;

            if (!bod.GuardarUpdateBod()) {
                bod.EditDesarenador = false;
                return false;
            }
//            Esto solo lo deben hacer quienes afecten datos de los que depndan otros y Desarenador no genera datos que afecten posteriores 
//            Main main = (Main) this.getTopLevelAncestor(); // Trata de ir  al contenedor principal 
//            main.ComprobarCambio(6);//Comprobar cambios que afecten datos posteriores, si este guardar es de una edición.

            return true;
        } catch (Exception ex) {
            log.error("Error en Guardar() " + ex.getMessage());
            util.Mensaje("Error en Guardar, revise el log", "error");
            return false;
        } finally {
            db.close(); //Se cierra la conexiòn de guardar()        
        }
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

    private void AsignarTextDoble(JTextField jtxtf, double min, double max, JLabel jlbl, String ErrorMsg) {
        Ltexto = new Listener_Texto();
        Ltexto.AgregarAlertaTextoDoble(jtxtf, min, max, jlbl, ErrorMsg);
    }

    private void AsignarTextEntero(JTextField jtxtf, int min, int max, JLabel jlbl, String ErrorMsg) {
        Ltexto = new Listener_Texto();
        Ltexto.AgregarAlertaTextoEntero(jtxtf, min, max, jlbl, ErrorMsg);
    }

    public void calcularOtros() {
        calcularDCKDCN();
        calcularDP1();
        calcularDP2();
        calcularDP3();
        calcularDRZ();
        calcularDPABCDEFGKN();
        calcularDAH();
        calcularDAB();
        calcularDLD();
        calcularDAL();
        calcularDUP();
    }

    /**
     * *
     * Calcular temporalmente DAN ingresado por el usuario , el cual es
     * comparado contra la tabla
     */
    private boolean calcularDAN() {

        try {
            String err = errorDAN;

            if (bod.setDAN(txt_DAN_Pregunta.getText().trim(), minDAN, maxDAN)) {

                String msg = bod.calcularDAN(listaDAN, Double.parseDouble(txt_DAN_Pregunta.getText().trim()));
                String[] arr = msg.split(";");

                if (arr[0].trim().equals("")) { //la posicion 0 vacia indica: sin error                     
                    esErrorDAN = false;
                    lbl_DAN_error.setForeground(new Color(0, 137, 148));
                    lbl_DAN_error.setText(arr[1]);
                    numDAN = Double.parseDouble(arr[2].trim()); //Este número es el usado por las otras tablas (cm o m), no el ingresado por user
                    calcularOtros();
                    return true;
                } else {
                    err = arr[0];
                }
            }
            esErrorDAN = true;
            lbl_DAN_error.setForeground(new Color(151, 0, 51));
            lbl_DAN_error.setText(err);
            numDAN = 0;
        } catch (Exception ex) {
            log.error("Error en calcularDAN " + ex.getMessage());
            lbl_DAN_error.setText("Error de calculo");//TODO: mejorar msg       
        }
        return false;
    }

    private boolean calcularDCKDCN() {

        try {
            if (!esErrorDAN) {

                String[] a = bod.calcularDCKDCN(listaDCK, numDAN).split(";");

                if (!a[0].trim().equals("") || a[0] != null) {

                    if (bod.setDCK(a[0], minDCK, maxDCK)) {

                        txt_DCK_Pregunta.setText(bod.getDCKs());
                        lbl_DCK_error.setText("");

                        if (bod.setDCN(a[1], minDCN, maxDCN)) {

                            txt_DCN_Pregunta.setText(bod.getDCNs());
                            lbl_DCN_error.setText("");
                            return true;
                        }
                    }
                }
            } else {
                lbl_DCK_error.setText("");//TODO: mejorar msg
                lbl_DCN_error.setText("");
                txt_DCK_Pregunta.setText("");
                txt_DCN_Pregunta.setText("");
            }
        } catch (Exception ex) {
            log.error("Error en calcularDCK " + ex.getMessage());
            lbl_DCK_error.setText("Error de calculo");//TODO: mejorar msg
            lbl_DCN_error.setText("Error de calculo");
        }
        return false;
    }

    /**
     * Esto se hace para calcular automática y temporalmente DP1 ya que debe
     * aparecer cuando el usuario ingrese DAN
     */
    private boolean calcularDP1() {

        try {
            if (!esErrorDAN) {

                if (bod.setDP1(bod.calcularDP1(0, 0), minDP1, maxDP1)) {
                    txt_DP1_Pregunta.setText(bod.getDP1s());
                    return true;
                }
                txt_DP1_Pregunta.setText("");
                lbl_DP1_error.setText(errorDP1);
            } else {
                txt_DP1_Pregunta.setText("");
                lbl_DP1_error.setText(errorDP1);
            }
        } catch (Exception ex) {
            log.error("Error en calcularDP1() " + ex.getMessage());
            lbl_DP1_error.setText("Error de calculo");//TODO: mejorar msg
        }
        return false;
    }

    /**
     * Esto se hace para calcular automática y temporalmente DP2 ya que debe
     * aparecer cuando el usuario ingrese DAN
     */
    private boolean calcularDP2() {

        try {
            if (!esErrorDAN) {

                if (bod.setDP2(bod.calcularDP2(0, 0), minDP2, maxDP2)) {
                    txt_DP2_Pregunta.setText(bod.getDP2s());
                    return true;
                }
                txt_DP2_Pregunta.setText("");
                lbl_DP2_error.setText(errorDP2);
            } else {
                txt_DP2_Pregunta.setText("");
                lbl_DP2_error.setText("");
            }
        } catch (Exception ex) {
            log.error("Error en calcularDP2() " + ex.getMessage());
            lbl_DP2_error.setText("Error de calculo");//TODO: mejorar msg
        }
        return false;
    }

    /**
     * Esto se hace para calcular automática y temporalmente DP3 ya que debe
     * aparecer cuando el usuario ingrese DAN
     */
    private boolean calcularDP3() {

        try {
            if (!esErrorDAN) {

                if (bod.setDP3(bod.calcularDP3(0, 0), minDP3, maxDP3)) {
                    txt_DP3_Pregunta.setText(bod.getDP3s());
                    return true;
                }
                txt_DP3_Pregunta.setText("");
                lbl_DP3_error.setText(errorDP3);
            }
            txt_DP3_Pregunta.setText("");
            lbl_DP3_error.setText("");
        } catch (Exception ex) {
            log.error("Error en calcularDP3() " + ex.getMessage());
            lbl_DP3_error.setText("Error de calculo");//TODO: mejorar msg
        }
        return false;
    }

    /**
     * Esto se hace para calcular automática y temporalmente DRZ ya que debe
     * aparecer cuando el usuario ingrese DAN
     */
    private boolean calcularDRZ() {

        try {
            if (!esErrorDAN) {

                if (bod.setDRZ(bod.calcularDRZ(0, 0), minDRZ, maxDRZ)) {

                    txt_DRZ_Pregunta.setText(bod.getDRZs());
                    lbl_DRZ_error.setText("");
                    return true;
                }
                txt_DRZ_Pregunta.setText("");
                lbl_DRZ_error.setText(errorDRZ);
            } else {
                txt_DRZ_Pregunta.setText("");
                lbl_DRZ_error.setText("");//TODO: mejorar msg 
            }
        } catch (Exception ex) {
            log.error("Error en calcularDP1DP2DP3 " + ex.getMessage());
            lbl_DRZ_error.setText("Error de calculo");//TODO: mejorar msg
        }
        return false;
    }

    /**
     * Esto se hace para calcular automática y temporalmente DPA,
     * DPB,DPC,DPD,DPE,DPF,DPG,DPK,DPN ya que deben aparecer cuando el usuario
     * ingrese DAN
     */
    private boolean calcularDPABCDEFGKN() {
        try {
            if (!esErrorDAN) {
                String[] a = bod.calcularDPABCDEFGKN(listaDPA, numDAN).split(";");

                if (!a[0].trim().equals("") || a[0] != null) {
                    bod.setDPA(Double.parseDouble(a[0]));
                    bod.setDPB(Double.parseDouble(a[1]));
                    bod.setDPC(Double.parseDouble(a[2]));
                    bod.setDPD(Double.parseDouble(a[3]));
                    bod.setDPE(Double.parseDouble(a[4]));
                    bod.setDPF(Double.parseDouble(a[5]));
                    bod.setDPG(Double.parseDouble(a[6]));
                    bod.setDPK(Double.parseDouble(a[7]));
                    bod.setDPN(Double.parseDouble(a[8]));

                    txt_DPA_Pregunta.setText(bod.getDPAs());
                    txt_DPB_Pregunta.setText(bod.getDPBs());
                    txt_DPC_Pregunta.setText(bod.getDPCs());
                    txt_DPD_Pregunta.setText(bod.getDPDs());
                    txt_DPE_Pregunta.setText(bod.getDPEs());
                    txt_DPF_Pregunta.setText(bod.getDPFs());
                    txt_DPG_Pregunta.setText(bod.getDPGs());
                    txt_DPK_Pregunta.setText(bod.getDPKs());
                    txt_DPN_Pregunta.setText(bod.getDPNs());
                    return true;
                }
            }
        } catch (Exception ex) {
            log.error("Error en calcularDPABCDEFGKN " + ex.getMessage());
            lbl_DPAN_error.setText("Error de calculo");//TODO: mejorar msg
        }
        txt_DPA_Pregunta.setText("");
        txt_DPB_Pregunta.setText("");
        txt_DPC_Pregunta.setText("");
        txt_DPD_Pregunta.setText("");
        txt_DPE_Pregunta.setText("");
        txt_DPF_Pregunta.setText("");
        txt_DPG_Pregunta.setText("");
        txt_DPK_Pregunta.setText("");
        txt_DPN_Pregunta.setText("");
        return false;
    }

    /**
     * Esto se hace para calcular automática y temporalmente DAH ya que debe
     * aparecer porque sus datos dependen del ingreso de DAN
     */
    private boolean calcularDAH() {
        try {
            if (!esErrorDAN) {

                if (bod.setDAH(bod.calcularDAH(0, 0), minDAH, maxDAH)) {

                    txt_DAH_Pregunta.setText(bod.getDAHs());
                    lbl_DAH_error.setText("");
                    return true;
                }
                txt_DAH_Pregunta.setText("");
                lbl_DAH_error.setText(errorDAH);
            } else {
                txt_DAH_Pregunta.setText("");
                lbl_DAH_error.setText("");//TODO: mejorar msg 
            }
        } catch (Exception ex) {
            log.error("Error en calcularDAH " + ex.getMessage());
            lbl_DAH_error.setText("Error de calculo");//TODO: mejorar msg
        }
        return false;
    }

    /**
     * Esto se hace para calcular automática y temporalmente DVF ya que debe
     * aparecer porque de sus datos depende DAB
     */
    private boolean calcularDVF() {
        try {
            if (bod.setDVF(txt_DVF_Pregunta.getText().trim(), minDVF, maxDVF)) {

                //txt_DVF_Pregunta.setText(bod.getDVFs()); //No necesario
                lbl_DVF_error.setText("");
                return true;
            }
            // txt_DVF_Pregunta.setText("");  //No necesario
            lbl_DVF_error.setText(errorDVF);
        } catch (Exception ex) {
            log.error("Error en calcularDVF " + ex.getMessage());
            lbl_DVF_error.setText("Error de calculo");//TODO: mejorar msg
        }
        return false;
    }

    /**
     * Esto se hace para calcular automática y temporalmente DAB ya que debe
     * aparecer porque sus datos dependen del ingreso de DAN y DVF
     */
    private boolean calcularDAB() {
        try {
            if (!esErrorDAN) {

                if (bod.setDAB(bod.calcularDAB(0, 0), minDAB, maxDAB)) {

                    txt_DAB_Pregunta.setText(bod.getDABs());
                    lbl_DAB_error.setText("");
                    return true;
                }
                txt_DAB_Pregunta.setText("");
                lbl_DAB_error.setText(errorDAB);
            } else {
                txt_DAB_Pregunta.setText("");
                lbl_DAB_error.setText("");//TODO: mejorar msg 
            }
        } catch (Exception ex) {
            log.error("Error en calcularDAB " + ex.getMessage());
            lbl_DAB_error.setText("Error de calculo");//TODO: mejorar msg
        }
        return false;
    }

    /**
     * Esto se hace para calcular automática y temporalmente DLD ya que debe
     * aparecer porque sus datos dependen del ingreso de DAH y datos anteriores
     */
    private boolean calcularDLD() {
        try {
            if (!esErrorDAN) {

                if (bod.setDLD(bod.calcularDLD(0), minDLD, maxDLD)) {

                    txt_DLD_Pregunta.setText(bod.getDLDs());
                    lbl_DLD_error.setText("");
                    return true;
                }
                txt_DLD_Pregunta.setText("");
                lbl_DLD_error.setText(errorDLD);
            } else {
                txt_DLD_Pregunta.setText("");
                lbl_DLD_error.setText("");//TODO: mejorar msg 
            }
        } catch (Exception ex) {
            log.error("Error en calcularDLD " + ex.getMessage());
            lbl_DLD_error.setText("Error de calculo");//TODO: mejorar msg
        }
        return false;
    }

    /**
     * Esto se hace para calcular automática y temporalmente DAL ya que debe
     * aparecer porque sus datos dependen del ingreso de DAN
     */
    private boolean calcularDAL() {
        try {
            if (!esErrorDAN) {

                if (bod.setDAL(bod.calcularDAL(0, 0), minDAL, maxDAL)) {

                    txt_DAL_Pregunta.setText(bod.getDALs());
                    lbl_DAL_error.setText("");
                    return true;
                }
                txt_DAL_Pregunta.setText("");
                lbl_DAL_error.setText(errorDAL);
            } else {
                txt_DAL_Pregunta.setText("");
                lbl_DAL_error.setText("");//TODO: mejorar msg 
            }
        } catch (Exception ex) {
            log.error("Error en calcularDAL " + ex.getMessage());
            lbl_DAL_error.setText("Error de calculo");//TODO: mejorar msg
        }
        return false;
    }

    /**
     * Esto se hace para calcular automática y temporalmente DEM
     */
    private boolean calcularDEM() {
        try {
            if (bod.setDEM(bod.calcularDEM(), minDEM, maxDEM)) {

                txt_DEM_Pregunta.setText(bod.getDEMs());
                lbl_DEM_error.setText("");
                return true;
            }
            txt_DEM_Pregunta.setText("");
            lbl_DEM_error.setText(errorDEM);

        } catch (Exception ex) {
            log.error("Error en calcularDEM " + ex.getMessage());
            lbl_DEM_error.setText("Error de calculo");//TODO: mejorar msg
        }
        return false;
    }

    /**
     * Esto se hace para calcular automática y temporalmente DFL
     */
    private boolean calcularDFL() {
        try {
           // if (!esErrorDAN) {

                if (bod.setDFL(txt_DFL_Pregunta.getText().trim(), minDFL, maxDFL)) {

                    lbl_DFL_error.setText("");
                    return true;
                }
                //txt_DFL_Pregunta.setText("");  //No necesario
                lbl_DFL_error.setText(errorDFL);
//            } else {
//                txt_DFL_Pregunta.setText("");
//                lbl_DFL_error.setText("");//TODO: mejorar msg 
//            }
        } catch (Exception ex) {
            log.error("Error en calcularDFL " + ex.getMessage());
            lbl_DFL_error.setText("Error de calculo");//TODO: mejorar msg
        }
        return false;
    }

    /**
     * Esto se hace para calcular automática y temporalmente DUP ya que debe
     * aparecer porque sus datos dependen del ingreso de DEM, DFL , DAL y datos
     * anteriores
     */
    private boolean calcularDUP() {
        try {
//            if (!esErrorDAN) {

                if (bod.setDUP(bod.calcularDUP(0, 0, 0), minDUP, maxDUP)) {

                    txt_DUP_Pregunta.setText(bod.getDUPs());
                    lbl_DUP_error.setText("");
                    return true;
                }
                txt_DUP_Pregunta.setText("");
                lbl_DUP_error.setText(errorDUP);
//            } else {
//                txt_DUP_Pregunta.setText("");
//                lbl_DUP_error.setText("");//TODO: mejorar msg 
//            }
        } catch (Exception ex) {
            log.error("Error en calcularDUP " + ex.getMessage());
            lbl_DUP_error.setText("Error de calculo");//TODO: mejorar msg
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jsp_Desarenador = new javax.swing.JScrollPane();
        jp_Componentes = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        lbl_titulo1 = new javax.swing.JLabel();
        lbl_titulo2 = new javax.swing.JLabel();
        btn_guardar = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        lbl_Q3C_titulo1 = new javax.swing.JLabel();
        lbl_Q1C_titulo1 = new javax.swing.JLabel();
        txt_Q1C_Pregunta = new javax.swing.JTextField();
        lbl_Q1C_titulo2 = new javax.swing.JLabel();
        btn_close = new javax.swing.JButton();
        lbl_save_error = new javax.swing.JLabel();
        lbl_save_error2 = new javax.swing.JLabel();
        lbl_DAN_error = new javax.swing.JLabel();
        lbl_DAN_titulo2 = new javax.swing.JLabel();
        txt_DAN_Pregunta = new javax.swing.JTextField();
        lbl_DAN_titulo1 = new javax.swing.JLabel();
        txt_DPB_Pregunta = new javax.swing.JTextField();
        lbl_DPB_titulo1 = new javax.swing.JLabel();
        lbl_DAN_ayuda = new javax.swing.JLabel();
        lbl_DPB_titulo2 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        lbl_DAN_desc = new javax.swing.JLabel();
        lbl_DCN_titulo1 = new javax.swing.JLabel();
        lbl_DCN_error = new javax.swing.JLabel();
        lbl_DCN_titulo2 = new javax.swing.JLabel();
        txt_DCN_Pregunta = new javax.swing.JTextField();
        lbl_DP1_titulo1 = new javax.swing.JLabel();
        lbl_DP1_error = new javax.swing.JLabel();
        lbl_DP1_titulo2 = new javax.swing.JLabel();
        txt_DP1_Pregunta = new javax.swing.JTextField();
        lbl_DAH_titulo2 = new javax.swing.JLabel();
        lbl_DAH_error = new javax.swing.JLabel();
        lbl_DAH_titulo1 = new javax.swing.JLabel();
        txt_DAH_Pregunta = new javax.swing.JTextField();
        lbl_DPA_titulo2 = new javax.swing.JLabel();
        lbl_DPA_titulo1 = new javax.swing.JLabel();
        txt_DPA_Pregunta = new javax.swing.JTextField();
        lbl_REB_ayuda = new javax.swing.JLabel();
        lbl_DCK_error = new javax.swing.JLabel();
        txt_DCK_Pregunta = new javax.swing.JTextField();
        lbl_DCK_titulo1 = new javax.swing.JLabel();
        lbl_DCK_titulo2 = new javax.swing.JLabel();
        lbl_RAT_ayuda = new javax.swing.JLabel();
        lbl_DPA_desc = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        lbl_DPA_ayuda = new javax.swing.JLabel();
        lbl_DAB_error = new javax.swing.JLabel();
        lbl_DAB_titulo2 = new javax.swing.JLabel();
        txt_DAB_Pregunta = new javax.swing.JTextField();
        lbl_DAB_titulo1 = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        lbl_Q2C_titulo1 = new javax.swing.JLabel();
        txt_Q2C_Pregunta = new javax.swing.JTextField();
        lbl_Q2C_titulo2 = new javax.swing.JLabel();
        lbl_DCK_desc = new javax.swing.JLabel();
        lbl_REB_ayuda1 = new javax.swing.JLabel();
        txt_DRZ_Pregunta = new javax.swing.JTextField();
        lbl_DRZ_error = new javax.swing.JLabel();
        lbl_DRZ_titulo2 = new javax.swing.JLabel();
        txt_DP3_Pregunta = new javax.swing.JTextField();
        lbl_DRZ_titulo1 = new javax.swing.JLabel();
        lbl_DP3_error = new javax.swing.JLabel();
        lbl_DP3_titulo2 = new javax.swing.JLabel();
        lbl_DP3_titulo1 = new javax.swing.JLabel();
        lbl_DP2_titulo2 = new javax.swing.JLabel();
        lbl_DP2_error = new javax.swing.JLabel();
        lbl_DP2_titulo1 = new javax.swing.JLabel();
        txt_DP2_Pregunta = new javax.swing.JTextField();
        lbl_DCK_ayuda = new javax.swing.JLabel();
        lbl_DP1_ayuda = new javax.swing.JLabel();
        lbl_DRZ_ayuda = new javax.swing.JLabel();
        txt_DPC_Pregunta = new javax.swing.JTextField();
        lbl_DPC_titulo2 = new javax.swing.JLabel();
        lbl_DPC_titulo1 = new javax.swing.JLabel();
        lbl_DPD_titulo2 = new javax.swing.JLabel();
        txt_DPD_Pregunta = new javax.swing.JTextField();
        lbl_DPD_titulo1 = new javax.swing.JLabel();
        lbl_DPF_titulo2 = new javax.swing.JLabel();
        txt_DPF_Pregunta = new javax.swing.JTextField();
        lbl_DPF_titulo1 = new javax.swing.JLabel();
        txt_DPE_Pregunta = new javax.swing.JTextField();
        lbl_DPE_titulo2 = new javax.swing.JLabel();
        lbl_DPE_titulo1 = new javax.swing.JLabel();
        lbl_DPK_titulo2 = new javax.swing.JLabel();
        lbl_DPK_titulo1 = new javax.swing.JLabel();
        lbl_DPN_titulo1 = new javax.swing.JLabel();
        txt_DPK_Pregunta = new javax.swing.JTextField();
        lbl_DPN_titulo2 = new javax.swing.JLabel();
        txt_DPN_Pregunta = new javax.swing.JTextField();
        txt_DPG_Pregunta = new javax.swing.JTextField();
        lbl_DPG_titulo2 = new javax.swing.JLabel();
        lbl_DPG_titulo1 = new javax.swing.JLabel();
        lbl_RAT_ayuda1 = new javax.swing.JLabel();
        lbl_DAH_desc = new javax.swing.JLabel();
        lbl_DVF_titulo2 = new javax.swing.JLabel();
        lbl_DVF_error = new javax.swing.JLabel();
        lbl_DVF_titulo1 = new javax.swing.JLabel();
        txt_DVF_Pregunta = new javax.swing.JTextField();
        lbl_DLD_titulo1 = new javax.swing.JLabel();
        txt_DLD_Pregunta = new javax.swing.JTextField();
        lbl_DLD_titulo2 = new javax.swing.JLabel();
        lbl_DLD_error = new javax.swing.JLabel();
        lbl_DAL_titulo2 = new javax.swing.JLabel();
        lbl_DAL_error = new javax.swing.JLabel();
        lbl_DAL_titulo1 = new javax.swing.JLabel();
        txt_DAL_Pregunta = new javax.swing.JTextField();
        lbl_DFL_error = new javax.swing.JLabel();
        lbl_DFL_titulo1 = new javax.swing.JLabel();
        lbl_DFL_titulo2 = new javax.swing.JLabel();
        txt_DUP_Pregunta = new javax.swing.JTextField();
        lbl_DUP_titulo1 = new javax.swing.JLabel();
        txt_DFL_Pregunta = new javax.swing.JTextField();
        lbl_DEM_titulo1 = new javax.swing.JLabel();
        txt_DEM_Pregunta = new javax.swing.JTextField();
        lbl_DUP_error = new javax.swing.JLabel();
        lbl_DUP_titulo2 = new javax.swing.JLabel();
        lbl_DEM_titulo2 = new javax.swing.JLabel();
        lbl_DEM_error = new javax.swing.JLabel();
        lbl_DAB_ayuda = new javax.swing.JLabel();
        lbl_DVF_ayuda = new javax.swing.JLabel();
        lbl_DLD_ayuda = new javax.swing.JLabel();
        lbl_DAL_ayuda = new javax.swing.JLabel();
        lbl_DEM_ayuda = new javax.swing.JLabel();
        lbl_DUP_ayuda = new javax.swing.JLabel();
        txt_Q3C_Pregunta = new javax.swing.JTextField();
        lbl_Q3C_titulo2 = new javax.swing.JLabel();
        lbl_DPAN_error = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1024, 1500));

        jsp_Desarenador.setPreferredSize(new java.awt.Dimension(1024, 1200));

        jp_Componentes.setPreferredSize(new java.awt.Dimension(1024, 1400));

        lbl_titulo1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_titulo1.setForeground(new java.awt.Color(51, 51, 51));
        lbl_titulo1.setText("Titulo");

        lbl_titulo2.setForeground(new java.awt.Color(0, 51, 102));
        lbl_titulo2.setText("Desc");

        btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Button_Salva_Guardar_21.png"))); // NOI18N
        btn_guardar.setText("save");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });

        lbl_Q3C_titulo1.setText("Titulo");
        lbl_Q3C_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_Q1C_titulo1.setText("Titulo");
        lbl_Q1C_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_Q1C_Pregunta.setEditable(false);

        lbl_Q1C_titulo2.setText("Titulo");
        lbl_Q1C_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        btn_close.setText("Salir");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });

        lbl_save_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error.setText(" . . .");

        lbl_save_error2.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error2.setText(" . . .");

        lbl_DAN_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DAN_error.setText(" . . .");

        lbl_DAN_titulo2.setText("Titulo");
        lbl_DAN_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_DAN_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_DAN_PreguntaKeyReleased(evt);
            }
        });

        lbl_DAN_titulo1.setText("Titulo");
        lbl_DAN_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_DPB_Pregunta.setEditable(false);

        lbl_DPB_titulo1.setText("Titulo");
        lbl_DPB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DAN_ayuda.setBackground(new java.awt.Color(153, 195, 115));
        lbl_DAN_ayuda.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_DAN_ayuda.setForeground(new java.awt.Color(0, 102, 102));
        lbl_DAN_ayuda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_DAN_ayuda.setText("?");
        lbl_DAN_ayuda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 51), 2));
        lbl_DAN_ayuda.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbl_DAN_ayuda.setFocusable(false);
        lbl_DAN_ayuda.setOpaque(true);
        lbl_DAN_ayuda.setRequestFocusEnabled(false);

        lbl_DPB_titulo2.setText("Titulo");
        lbl_DPB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DAN_desc.setText("Desc");
        lbl_DAN_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DCN_titulo1.setText("Titulo");
        lbl_DCN_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DCN_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DCN_error.setText(" . . .");

        lbl_DCN_titulo2.setText("Titulo");
        lbl_DCN_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_DCN_Pregunta.setEditable(false);

        lbl_DP1_titulo1.setText("Titulo");
        lbl_DP1_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DP1_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DP1_error.setText(" . . .");

        lbl_DP1_titulo2.setText("Titulo");
        lbl_DP1_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_DP1_Pregunta.setEditable(false);

        lbl_DAH_titulo2.setText("Titulo");
        lbl_DAH_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DAH_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DAH_error.setText(" . . .");

        lbl_DAH_titulo1.setText("Titulo");
        lbl_DAH_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_DAH_Pregunta.setEditable(false);

        lbl_DPA_titulo2.setText("Titulo");
        lbl_DPA_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DPA_titulo1.setText("Titulo");
        lbl_DPA_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_DPA_Pregunta.setEditable(false);

        lbl_REB_ayuda.setBackground(new java.awt.Color(153, 195, 115));
        lbl_REB_ayuda.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_REB_ayuda.setForeground(new java.awt.Color(0, 102, 102));
        lbl_REB_ayuda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_REB_ayuda.setText("?");
        lbl_REB_ayuda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 51), 2));
        lbl_REB_ayuda.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbl_REB_ayuda.setFocusable(false);
        lbl_REB_ayuda.setOpaque(true);
        lbl_REB_ayuda.setRequestFocusEnabled(false);

        lbl_DCK_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DCK_error.setText(" . . .");

        txt_DCK_Pregunta.setEditable(false);

        lbl_DCK_titulo1.setText("Titulo");
        lbl_DCK_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DCK_titulo2.setText("Titulo");
        lbl_DCK_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_RAT_ayuda.setBackground(new java.awt.Color(153, 195, 115));
        lbl_RAT_ayuda.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_RAT_ayuda.setForeground(new java.awt.Color(0, 102, 102));
        lbl_RAT_ayuda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_RAT_ayuda.setText("?");
        lbl_RAT_ayuda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 51), 2));
        lbl_RAT_ayuda.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbl_RAT_ayuda.setFocusable(false);
        lbl_RAT_ayuda.setOpaque(true);
        lbl_RAT_ayuda.setRequestFocusEnabled(false);

        lbl_DPA_desc.setText("Desc");
        lbl_DPA_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DPA_ayuda.setBackground(new java.awt.Color(153, 195, 115));
        lbl_DPA_ayuda.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_DPA_ayuda.setForeground(new java.awt.Color(0, 102, 102));
        lbl_DPA_ayuda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_DPA_ayuda.setText("?");
        lbl_DPA_ayuda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 51), 2));
        lbl_DPA_ayuda.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbl_DPA_ayuda.setFocusable(false);
        lbl_DPA_ayuda.setOpaque(true);
        lbl_DPA_ayuda.setRequestFocusEnabled(false);

        lbl_DAB_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DAB_error.setText(" . . .");

        lbl_DAB_titulo2.setText("Titulo");
        lbl_DAB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_DAB_Pregunta.setEditable(false);

        lbl_DAB_titulo1.setText("Titulo");
        lbl_DAB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_Q2C_titulo1.setText("Titulo");
        lbl_Q2C_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_Q2C_Pregunta.setEditable(false);

        lbl_Q2C_titulo2.setText("Titulo");
        lbl_Q2C_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DCK_desc.setText("Desc");
        lbl_DCK_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_REB_ayuda1.setBackground(new java.awt.Color(153, 195, 115));
        lbl_REB_ayuda1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_REB_ayuda1.setForeground(new java.awt.Color(0, 102, 102));
        lbl_REB_ayuda1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_REB_ayuda1.setText("?");
        lbl_REB_ayuda1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 51), 2));
        lbl_REB_ayuda1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbl_REB_ayuda1.setFocusable(false);
        lbl_REB_ayuda1.setOpaque(true);
        lbl_REB_ayuda1.setRequestFocusEnabled(false);

        txt_DRZ_Pregunta.setEditable(false);

        lbl_DRZ_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DRZ_error.setText(" . . .");

        lbl_DRZ_titulo2.setText("Titulo");
        lbl_DRZ_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_DP3_Pregunta.setEditable(false);

        lbl_DRZ_titulo1.setText("Titulo");
        lbl_DRZ_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DP3_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DP3_error.setText(" . . .");

        lbl_DP3_titulo2.setText("Titulo");
        lbl_DP3_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DP3_titulo1.setText("Titulo");
        lbl_DP3_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DP2_titulo2.setText("Titulo");
        lbl_DP2_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DP2_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DP2_error.setText(" . . .");

        lbl_DP2_titulo1.setText("Titulo");
        lbl_DP2_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_DP2_Pregunta.setEditable(false);

        lbl_DCK_ayuda.setBackground(new java.awt.Color(153, 195, 115));
        lbl_DCK_ayuda.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_DCK_ayuda.setForeground(new java.awt.Color(0, 102, 102));
        lbl_DCK_ayuda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_DCK_ayuda.setText("?");
        lbl_DCK_ayuda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 51), 2));
        lbl_DCK_ayuda.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbl_DCK_ayuda.setFocusable(false);
        lbl_DCK_ayuda.setOpaque(true);
        lbl_DCK_ayuda.setRequestFocusEnabled(false);

        lbl_DP1_ayuda.setBackground(new java.awt.Color(153, 195, 115));
        lbl_DP1_ayuda.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_DP1_ayuda.setForeground(new java.awt.Color(0, 102, 102));
        lbl_DP1_ayuda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_DP1_ayuda.setText("?");
        lbl_DP1_ayuda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 51), 2));
        lbl_DP1_ayuda.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbl_DP1_ayuda.setFocusable(false);
        lbl_DP1_ayuda.setOpaque(true);
        lbl_DP1_ayuda.setRequestFocusEnabled(false);

        lbl_DRZ_ayuda.setBackground(new java.awt.Color(153, 195, 115));
        lbl_DRZ_ayuda.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_DRZ_ayuda.setForeground(new java.awt.Color(0, 102, 102));
        lbl_DRZ_ayuda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_DRZ_ayuda.setText("?");
        lbl_DRZ_ayuda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 51), 2));
        lbl_DRZ_ayuda.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbl_DRZ_ayuda.setFocusable(false);
        lbl_DRZ_ayuda.setOpaque(true);
        lbl_DRZ_ayuda.setRequestFocusEnabled(false);

        txt_DPC_Pregunta.setEditable(false);

        lbl_DPC_titulo2.setText("Titulo");
        lbl_DPC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DPC_titulo1.setText("Titulo");
        lbl_DPC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DPD_titulo2.setText("Titulo");
        lbl_DPD_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_DPD_Pregunta.setEditable(false);

        lbl_DPD_titulo1.setText("Titulo");
        lbl_DPD_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DPF_titulo2.setText("Titulo");
        lbl_DPF_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_DPF_Pregunta.setEditable(false);

        lbl_DPF_titulo1.setText("Titulo");
        lbl_DPF_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_DPE_Pregunta.setEditable(false);

        lbl_DPE_titulo2.setText("Titulo");
        lbl_DPE_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DPE_titulo1.setText("Titulo");
        lbl_DPE_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DPK_titulo2.setText("Titulo");
        lbl_DPK_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DPK_titulo1.setText("Titulo");
        lbl_DPK_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DPN_titulo1.setText("Titulo");
        lbl_DPN_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_DPK_Pregunta.setEditable(false);

        lbl_DPN_titulo2.setText("Titulo");
        lbl_DPN_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_DPN_Pregunta.setEditable(false);

        txt_DPG_Pregunta.setEditable(false);

        lbl_DPG_titulo2.setText("Titulo");
        lbl_DPG_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DPG_titulo1.setText("Titulo");
        lbl_DPG_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_RAT_ayuda1.setBackground(new java.awt.Color(153, 195, 115));
        lbl_RAT_ayuda1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_RAT_ayuda1.setForeground(new java.awt.Color(0, 102, 102));
        lbl_RAT_ayuda1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_RAT_ayuda1.setText("?");
        lbl_RAT_ayuda1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 51), 2));
        lbl_RAT_ayuda1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbl_RAT_ayuda1.setFocusable(false);
        lbl_RAT_ayuda1.setOpaque(true);
        lbl_RAT_ayuda1.setRequestFocusEnabled(false);

        lbl_DAH_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_DAH_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_DAH_desc.setText("Desc");
        lbl_DAH_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DVF_titulo2.setText("Titulo");
        lbl_DVF_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DVF_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DVF_error.setText(" . . .");

        lbl_DVF_titulo1.setText("Titulo");
        lbl_DVF_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_DVF_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_DVF_PreguntaKeyReleased(evt);
            }
        });

        lbl_DLD_titulo1.setText("Titulo");
        lbl_DLD_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_DLD_Pregunta.setEditable(false);

        lbl_DLD_titulo2.setText("Titulo");
        lbl_DLD_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DLD_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DLD_error.setText(" . . .");

        lbl_DAL_titulo2.setText("Titulo");
        lbl_DAL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DAL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DAL_error.setText(" . . .");

        lbl_DAL_titulo1.setText("Titulo");
        lbl_DAL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_DAL_Pregunta.setEditable(false);

        lbl_DFL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DFL_error.setText(" . . .");

        lbl_DFL_titulo1.setText("Titulo");
        lbl_DFL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DFL_titulo2.setText("Titulo");
        lbl_DFL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_DUP_Pregunta.setEditable(false);

        lbl_DUP_titulo1.setText("Titulo");
        lbl_DUP_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_DFL_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_DFL_PreguntaKeyReleased(evt);
            }
        });

        lbl_DEM_titulo1.setText("Titulo");
        lbl_DEM_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_DEM_Pregunta.setEditable(false);

        lbl_DUP_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DUP_error.setText(" . . .");

        lbl_DUP_titulo2.setText("Titulo");
        lbl_DUP_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DEM_titulo2.setText("Titulo");
        lbl_DEM_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DEM_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DEM_error.setText(" . . .");

        lbl_DAB_ayuda.setBackground(new java.awt.Color(153, 195, 115));
        lbl_DAB_ayuda.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_DAB_ayuda.setForeground(new java.awt.Color(0, 102, 102));
        lbl_DAB_ayuda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_DAB_ayuda.setText("?");
        lbl_DAB_ayuda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 51), 2));
        lbl_DAB_ayuda.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbl_DAB_ayuda.setFocusable(false);
        lbl_DAB_ayuda.setOpaque(true);
        lbl_DAB_ayuda.setRequestFocusEnabled(false);

        lbl_DVF_ayuda.setBackground(new java.awt.Color(153, 195, 115));
        lbl_DVF_ayuda.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_DVF_ayuda.setForeground(new java.awt.Color(0, 102, 102));
        lbl_DVF_ayuda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_DVF_ayuda.setText("?");
        lbl_DVF_ayuda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 51), 2));
        lbl_DVF_ayuda.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbl_DVF_ayuda.setFocusable(false);
        lbl_DVF_ayuda.setOpaque(true);
        lbl_DVF_ayuda.setRequestFocusEnabled(false);

        lbl_DLD_ayuda.setBackground(new java.awt.Color(153, 195, 115));
        lbl_DLD_ayuda.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_DLD_ayuda.setForeground(new java.awt.Color(0, 102, 102));
        lbl_DLD_ayuda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_DLD_ayuda.setText("?");
        lbl_DLD_ayuda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 51), 2));
        lbl_DLD_ayuda.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbl_DLD_ayuda.setFocusable(false);
        lbl_DLD_ayuda.setOpaque(true);
        lbl_DLD_ayuda.setRequestFocusEnabled(false);

        lbl_DAL_ayuda.setBackground(new java.awt.Color(153, 195, 115));
        lbl_DAL_ayuda.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_DAL_ayuda.setForeground(new java.awt.Color(0, 102, 102));
        lbl_DAL_ayuda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_DAL_ayuda.setText("?");
        lbl_DAL_ayuda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 51), 2));
        lbl_DAL_ayuda.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbl_DAL_ayuda.setFocusable(false);
        lbl_DAL_ayuda.setOpaque(true);
        lbl_DAL_ayuda.setRequestFocusEnabled(false);

        lbl_DEM_ayuda.setBackground(new java.awt.Color(153, 195, 115));
        lbl_DEM_ayuda.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_DEM_ayuda.setForeground(new java.awt.Color(0, 102, 102));
        lbl_DEM_ayuda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_DEM_ayuda.setText("?");
        lbl_DEM_ayuda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 51), 2));
        lbl_DEM_ayuda.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbl_DEM_ayuda.setFocusable(false);
        lbl_DEM_ayuda.setOpaque(true);
        lbl_DEM_ayuda.setRequestFocusEnabled(false);

        lbl_DUP_ayuda.setBackground(new java.awt.Color(153, 195, 115));
        lbl_DUP_ayuda.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_DUP_ayuda.setForeground(new java.awt.Color(0, 102, 102));
        lbl_DUP_ayuda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_DUP_ayuda.setText("?");
        lbl_DUP_ayuda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 51), 2));
        lbl_DUP_ayuda.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbl_DUP_ayuda.setFocusable(false);
        lbl_DUP_ayuda.setOpaque(true);
        lbl_DUP_ayuda.setRequestFocusEnabled(false);

        txt_Q3C_Pregunta.setEditable(false);

        lbl_Q3C_titulo2.setText("Titulo");
        lbl_Q3C_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_DPAN_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DPAN_error.setText(" . . .");

        javax.swing.GroupLayout jp_ComponentesLayout = new javax.swing.GroupLayout(jp_Componentes);
        jp_Componentes.setLayout(jp_ComponentesLayout);
        jp_ComponentesLayout.setHorizontalGroup(
            jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_ComponentesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_DAN_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(214, 214, 214))
            .addComponent(jSeparator9)
            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addComponent(lbl_DCK_desc, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_DCK_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(302, 302, 302))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addComponent(lbl_DPA_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(txt_DPA_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(lbl_DPA_titulo2))
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addComponent(lbl_DPB_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(txt_DPB_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(lbl_DPB_titulo2)))
                        .addGap(44, 44, 44)
                        .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addComponent(lbl_DPD_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(txt_DPD_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(lbl_DPD_titulo2))
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addComponent(lbl_DPE_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(txt_DPE_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(lbl_DPE_titulo2))
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addComponent(lbl_DPF_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(txt_DPF_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(lbl_DPF_titulo2)))
                        .addGap(62, 62, 62)
                        .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addComponent(lbl_DPG_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(txt_DPG_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(lbl_DPG_titulo2))
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addComponent(lbl_DPK_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(txt_DPK_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(lbl_DPK_titulo2))
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addComponent(lbl_DPN_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(txt_DPN_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(lbl_DPN_titulo2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_RAT_ayuda1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_RAT_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addComponent(lbl_DPA_desc, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_DPA_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(297, 297, 297))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_ComponentesLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 827, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbl_DP1_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(204, 204, 204))
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addComponent(lbl_DCN_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(txt_DCN_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(lbl_DCN_titulo2)
                                .addGap(18, 18, 18)
                                .addComponent(lbl_DCN_error, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addComponent(lbl_DP1_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(txt_DP1_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(lbl_DP1_titulo2)
                                .addGap(18, 18, 18)
                                .addComponent(lbl_DP1_error, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                        .addComponent(lbl_DRZ_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addComponent(txt_DRZ_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(4, 4, 4)
                                        .addComponent(lbl_DRZ_titulo2)
                                        .addGap(18, 18, 18)
                                        .addComponent(lbl_DRZ_error, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbl_DRZ_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                        .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                                .addComponent(lbl_DP3_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10)
                                                .addComponent(txt_DP3_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(4, 4, 4)
                                                .addComponent(lbl_DP3_titulo2)
                                                .addGap(18, 18, 18)
                                                .addComponent(lbl_DP3_error, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                                .addComponent(lbl_DP2_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10)
                                                .addComponent(txt_DP2_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(4, 4, 4)
                                                .addComponent(lbl_DP2_titulo2)
                                                .addGap(18, 18, 18)
                                                .addComponent(lbl_DP2_error, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(359, 359, 359)))
                                .addGap(104, 104, 104)
                                .addComponent(lbl_REB_ayuda1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addComponent(lbl_DCK_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(txt_DCK_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(lbl_DCK_titulo2)
                                .addGap(18, 18, 18)
                                .addComponent(lbl_DCK_error, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbl_REB_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addComponent(lbl_DPC_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(txt_DPC_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(lbl_DPC_titulo2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_ComponentesLayout.createSequentialGroup()
                        .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator10, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator11, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                        .addComponent(btn_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(38, 38, 38)
                                        .addComponent(lbl_save_error, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                        .addComponent(btn_close, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(38, 38, 38)
                                        .addComponent(lbl_save_error2, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                        .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                                        .addComponent(lbl_DAB_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(10, 10, 10)
                                                        .addComponent(txt_DAB_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(4, 4, 4)
                                                        .addComponent(lbl_DAB_titulo2)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(lbl_DAB_error, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(lbl_DAB_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                                        .addComponent(lbl_DLD_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(10, 10, 10)
                                                        .addComponent(txt_DLD_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(4, 4, 4)
                                                        .addComponent(lbl_DLD_titulo2)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(lbl_DLD_error, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(0, 0, Short.MAX_VALUE)))
                                                .addGap(49, 49, 49))
                                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                                .addComponent(lbl_DAL_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10)
                                                .addComponent(txt_DAL_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(4, 4, 4)
                                                .addComponent(lbl_DAL_titulo2)
                                                .addGap(18, 18, 18)
                                                .addComponent(lbl_DAL_error, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(185, 185, 185)
                                                .addComponent(lbl_DAL_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addComponent(lbl_DLD_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(119, 119, 119))
                                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                        .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                                .addComponent(lbl_DAH_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10)
                                                .addComponent(txt_DAH_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(4, 4, 4)
                                                .addComponent(lbl_DAH_titulo2)
                                                .addGap(18, 18, 18)
                                                .addComponent(lbl_DAH_error, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(lbl_DAH_desc, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                                .addComponent(lbl_DVF_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10)
                                                .addComponent(txt_DVF_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(4, 4, 4)
                                                .addComponent(lbl_DVF_titulo2)
                                                .addGap(18, 18, 18)
                                                .addComponent(lbl_DVF_error, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(121, 121, 121)
                                                .addComponent(lbl_DVF_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 342, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_DEM_ayuda, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_ComponentesLayout.createSequentialGroup()
                                        .addComponent(lbl_DEM_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addComponent(txt_DEM_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(4, 4, 4)
                                        .addComponent(lbl_DEM_titulo2)
                                        .addGap(18, 18, 18)
                                        .addComponent(lbl_DEM_error, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                        .addComponent(lbl_DFL_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addComponent(txt_DFL_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(4, 4, 4)
                                        .addComponent(lbl_DFL_titulo2)
                                        .addGap(18, 18, 18)
                                        .addComponent(lbl_DFL_error, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                        .addComponent(lbl_DUP_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addComponent(txt_DUP_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(4, 4, 4)
                                        .addComponent(lbl_DUP_titulo2)
                                        .addGap(18, 18, 18)
                                        .addComponent(lbl_DUP_error, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(275, 275, 275)
                                .addComponent(lbl_DUP_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_Q3C_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(txt_Q3C_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_Q3C_titulo2))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(lbl_titulo1)
                        .addGap(18, 18, 18)
                        .addComponent(lbl_titulo2, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 1061, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addComponent(lbl_Q1C_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(txt_Q1C_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(lbl_Q1C_titulo2))
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addComponent(lbl_Q2C_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(txt_Q2C_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(lbl_Q2C_titulo2))))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbl_DAN_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(txt_DAN_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(lbl_DAN_titulo2)
                        .addGap(18, 18, 18)
                        .addComponent(lbl_DAN_error, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbl_DAN_desc, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbl_DPAN_error, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jp_ComponentesLayout.setVerticalGroup(
            jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_titulo1)
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lbl_titulo2)))
                .addGap(2, 2, 2)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_Q3C_titulo1)
                    .addComponent(txt_Q3C_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_Q3C_titulo2))
                .addGap(20, 20, 20)
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_Q1C_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_Q1C_titulo1)
                            .addComponent(lbl_Q1C_titulo2))))
                .addGap(18, 18, 18)
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_Q2C_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_Q2C_titulo1)
                            .addComponent(lbl_Q2C_titulo2))))
                .addGap(18, 18, 18)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_DAN_desc)
                    .addComponent(lbl_DAN_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_DAN_titulo1)
                    .addComponent(txt_DAN_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_DAN_titulo2)
                    .addComponent(lbl_DAN_error, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_DCK_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_DCK_desc))
                .addGap(22, 22, 22)
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_DCK_titulo1))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(txt_DCK_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_DCK_titulo2))
                    .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_DCK_error, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_REB_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_DCN_titulo1))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(txt_DCN_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_DCN_titulo2))
                    .addComponent(lbl_DCN_error, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_DP1_titulo1))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(txt_DP1_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_DP1_titulo2))
                    .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_DP1_error, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_DP1_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_REB_ayuda1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lbl_DP2_titulo1))
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(txt_DP2_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lbl_DP2_titulo2))
                            .addComponent(lbl_DP2_error, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lbl_DP3_titulo1))
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(txt_DP3_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lbl_DP3_titulo2))
                            .addComponent(lbl_DP3_error, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lbl_DRZ_titulo1))
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(txt_DRZ_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lbl_DRZ_titulo2))
                            .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lbl_DRZ_error, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbl_DRZ_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_DPA_desc)
                    .addComponent(lbl_DPA_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lbl_DPA_titulo1))
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(txt_DPA_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lbl_DPA_titulo2))
                            .addComponent(lbl_RAT_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(lbl_DPB_titulo1))
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(txt_DPB_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(lbl_DPB_titulo2)))
                        .addGap(18, 18, 18)
                        .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_DPC_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_DPC_titulo1)
                                    .addComponent(lbl_DPC_titulo2)))))
                    .addComponent(lbl_RAT_ayuda1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_DPD_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbl_DPD_titulo1)
                                            .addComponent(lbl_DPD_titulo2))))
                                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addComponent(lbl_DPE_titulo1))
                                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(txt_DPE_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addComponent(lbl_DPE_titulo2)))
                                .addGap(18, 18, 18)
                                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_DPF_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbl_DPF_titulo1)
                                            .addComponent(lbl_DPF_titulo2)))))
                            .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_DPG_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbl_DPG_titulo1)
                                            .addComponent(lbl_DPG_titulo2))))
                                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addComponent(lbl_DPK_titulo1))
                                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(txt_DPK_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addComponent(lbl_DPK_titulo2)))
                                .addGap(18, 18, 18)
                                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_DPN_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbl_DPN_titulo1)
                                            .addComponent(lbl_DPN_titulo2))))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_DPAN_error, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_DAH_desc)
                .addGap(12, 12, 12)
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_DAH_titulo1))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(txt_DAH_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_DAH_titulo2))
                    .addComponent(lbl_DAH_error, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_DAB_titulo1))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(txt_DAB_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_DAB_titulo2))
                    .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_DAB_error, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_DAB_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_DVF_titulo1))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(txt_DVF_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_DVF_titulo2))
                    .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_DVF_error, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_DVF_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_DLD_titulo1))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(txt_DLD_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_DLD_titulo2))
                    .addComponent(lbl_DLD_error, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_ComponentesLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_DLD_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_DAL_titulo1))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(txt_DAL_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_DAL_titulo2))
                    .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_DAL_error, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_DAL_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_DEM_titulo1))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(txt_DEM_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_DEM_titulo2))
                    .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_DEM_error, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_DEM_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(11, 11, 11)
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_DFL_titulo1))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(txt_DFL_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_DFL_titulo2))
                    .addComponent(lbl_DFL_error, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_DUP_titulo1))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(txt_DUP_Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_DUP_titulo2))
                    .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_DUP_error, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_DUP_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(btn_guardar))
                    .addComponent(lbl_save_error, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jp_ComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_ComponentesLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(btn_close, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lbl_save_error2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(213, Short.MAX_VALUE))
        );

        jsp_Desarenador.setViewportView(jp_Componentes);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jsp_Desarenador, javax.swing.GroupLayout.DEFAULT_SIZE, 1018, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jsp_Desarenador, javax.swing.GroupLayout.DEFAULT_SIZE, 1474, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_closeActionPerformed
        bod.WinDesarenador = false;
        if (!bod.EditDesarenador) {
            bod.setNullsDesarenador(); //Borrar variables usadas
        }
        this.dispose();
    }//GEN-LAST:event_btn_closeActionPerformed

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        try {
            if (bod.idproyecto > 0 && Guardar()) {
                util.Mensaje("Datos guardados", "ok");//Todo:mensajes en db
                Main main = (Main) this.getTopLevelAncestor(); // Trata de ir  al contenedor principal y activa o desactivar botones
                main.comprobarBotones();
                main.CargarDatosFondo();
            }
        } catch (Exception ex) {
            log.error("Error en acción boton guardar() " + ex.getMessage());
        }
    }//GEN-LAST:event_btn_guardarActionPerformed

    private void txt_DAN_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_DAN_PreguntaKeyReleased
        // Calcular temporalmente a DAN:
        calcularDAN();
    }//GEN-LAST:event_txt_DAN_PreguntaKeyReleased

    private void txt_DVF_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_DVF_PreguntaKeyReleased
        // Calcular temporalmente a DAB:
        if (calcularDVF()) {
            calcularDAB();
        }
    }//GEN-LAST:event_txt_DVF_PreguntaKeyReleased

    private void txt_DFL_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_DFL_PreguntaKeyReleased
        // Calcular temporalmente a DUP:
        if (calcularDFL()) {
            calcularDUP();
        }
    }//GEN-LAST:event_txt_DFL_PreguntaKeyReleased
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_close;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JPanel jp_Componentes;
    private javax.swing.JScrollPane jsp_Desarenador;
    private javax.swing.JLabel lbl_DAB_ayuda;
    private javax.swing.JLabel lbl_DAB_error;
    private javax.swing.JLabel lbl_DAB_titulo1;
    private javax.swing.JLabel lbl_DAB_titulo2;
    private javax.swing.JLabel lbl_DAH_desc;
    private javax.swing.JLabel lbl_DAH_error;
    private javax.swing.JLabel lbl_DAH_titulo1;
    private javax.swing.JLabel lbl_DAH_titulo2;
    private javax.swing.JLabel lbl_DAL_ayuda;
    private javax.swing.JLabel lbl_DAL_error;
    private javax.swing.JLabel lbl_DAL_titulo1;
    private javax.swing.JLabel lbl_DAL_titulo2;
    private javax.swing.JLabel lbl_DAN_ayuda;
    private javax.swing.JLabel lbl_DAN_desc;
    private javax.swing.JLabel lbl_DAN_error;
    private javax.swing.JLabel lbl_DAN_titulo1;
    private javax.swing.JLabel lbl_DAN_titulo2;
    private javax.swing.JLabel lbl_DCK_ayuda;
    private javax.swing.JLabel lbl_DCK_desc;
    private javax.swing.JLabel lbl_DCK_error;
    private javax.swing.JLabel lbl_DCK_titulo1;
    private javax.swing.JLabel lbl_DCK_titulo2;
    private javax.swing.JLabel lbl_DCN_error;
    private javax.swing.JLabel lbl_DCN_titulo1;
    private javax.swing.JLabel lbl_DCN_titulo2;
    private javax.swing.JLabel lbl_DEM_ayuda;
    private javax.swing.JLabel lbl_DEM_error;
    private javax.swing.JLabel lbl_DEM_titulo1;
    private javax.swing.JLabel lbl_DEM_titulo2;
    private javax.swing.JLabel lbl_DFL_error;
    private javax.swing.JLabel lbl_DFL_titulo1;
    private javax.swing.JLabel lbl_DFL_titulo2;
    private javax.swing.JLabel lbl_DLD_ayuda;
    private javax.swing.JLabel lbl_DLD_error;
    private javax.swing.JLabel lbl_DLD_titulo1;
    private javax.swing.JLabel lbl_DLD_titulo2;
    private javax.swing.JLabel lbl_DP1_ayuda;
    private javax.swing.JLabel lbl_DP1_error;
    private javax.swing.JLabel lbl_DP1_titulo1;
    private javax.swing.JLabel lbl_DP1_titulo2;
    private javax.swing.JLabel lbl_DP2_error;
    private javax.swing.JLabel lbl_DP2_titulo1;
    private javax.swing.JLabel lbl_DP2_titulo2;
    private javax.swing.JLabel lbl_DP3_error;
    private javax.swing.JLabel lbl_DP3_titulo1;
    private javax.swing.JLabel lbl_DP3_titulo2;
    private javax.swing.JLabel lbl_DPAN_error;
    private javax.swing.JLabel lbl_DPA_ayuda;
    private javax.swing.JLabel lbl_DPA_desc;
    private javax.swing.JLabel lbl_DPA_titulo1;
    private javax.swing.JLabel lbl_DPA_titulo2;
    private javax.swing.JLabel lbl_DPB_titulo1;
    private javax.swing.JLabel lbl_DPB_titulo2;
    private javax.swing.JLabel lbl_DPC_titulo1;
    private javax.swing.JLabel lbl_DPC_titulo2;
    private javax.swing.JLabel lbl_DPD_titulo1;
    private javax.swing.JLabel lbl_DPD_titulo2;
    private javax.swing.JLabel lbl_DPE_titulo1;
    private javax.swing.JLabel lbl_DPE_titulo2;
    private javax.swing.JLabel lbl_DPF_titulo1;
    private javax.swing.JLabel lbl_DPF_titulo2;
    private javax.swing.JLabel lbl_DPG_titulo1;
    private javax.swing.JLabel lbl_DPG_titulo2;
    private javax.swing.JLabel lbl_DPK_titulo1;
    private javax.swing.JLabel lbl_DPK_titulo2;
    private javax.swing.JLabel lbl_DPN_titulo1;
    private javax.swing.JLabel lbl_DPN_titulo2;
    private javax.swing.JLabel lbl_DRZ_ayuda;
    private javax.swing.JLabel lbl_DRZ_error;
    private javax.swing.JLabel lbl_DRZ_titulo1;
    private javax.swing.JLabel lbl_DRZ_titulo2;
    private javax.swing.JLabel lbl_DUP_ayuda;
    private javax.swing.JLabel lbl_DUP_error;
    private javax.swing.JLabel lbl_DUP_titulo1;
    private javax.swing.JLabel lbl_DUP_titulo2;
    private javax.swing.JLabel lbl_DVF_ayuda;
    private javax.swing.JLabel lbl_DVF_error;
    private javax.swing.JLabel lbl_DVF_titulo1;
    private javax.swing.JLabel lbl_DVF_titulo2;
    private javax.swing.JLabel lbl_Q1C_titulo1;
    private javax.swing.JLabel lbl_Q1C_titulo2;
    private javax.swing.JLabel lbl_Q2C_titulo1;
    private javax.swing.JLabel lbl_Q2C_titulo2;
    private javax.swing.JLabel lbl_Q3C_titulo1;
    private javax.swing.JLabel lbl_Q3C_titulo2;
    private javax.swing.JLabel lbl_RAT_ayuda;
    private javax.swing.JLabel lbl_RAT_ayuda1;
    private javax.swing.JLabel lbl_REB_ayuda;
    private javax.swing.JLabel lbl_REB_ayuda1;
    private javax.swing.JLabel lbl_save_error;
    private javax.swing.JLabel lbl_save_error2;
    private javax.swing.JLabel lbl_titulo1;
    private javax.swing.JLabel lbl_titulo2;
    private javax.swing.JTextField txt_DAB_Pregunta;
    private javax.swing.JTextField txt_DAH_Pregunta;
    private javax.swing.JTextField txt_DAL_Pregunta;
    private javax.swing.JTextField txt_DAN_Pregunta;
    private javax.swing.JTextField txt_DCK_Pregunta;
    private javax.swing.JTextField txt_DCN_Pregunta;
    private javax.swing.JTextField txt_DEM_Pregunta;
    private javax.swing.JTextField txt_DFL_Pregunta;
    private javax.swing.JTextField txt_DLD_Pregunta;
    private javax.swing.JTextField txt_DP1_Pregunta;
    private javax.swing.JTextField txt_DP2_Pregunta;
    private javax.swing.JTextField txt_DP3_Pregunta;
    private javax.swing.JTextField txt_DPA_Pregunta;
    private javax.swing.JTextField txt_DPB_Pregunta;
    private javax.swing.JTextField txt_DPC_Pregunta;
    private javax.swing.JTextField txt_DPD_Pregunta;
    private javax.swing.JTextField txt_DPE_Pregunta;
    private javax.swing.JTextField txt_DPF_Pregunta;
    private javax.swing.JTextField txt_DPG_Pregunta;
    private javax.swing.JTextField txt_DPK_Pregunta;
    private javax.swing.JTextField txt_DPN_Pregunta;
    private javax.swing.JTextField txt_DRZ_Pregunta;
    private javax.swing.JTextField txt_DUP_Pregunta;
    private javax.swing.JTextField txt_DVF_Pregunta;
    private javax.swing.JTextField txt_Q1C_Pregunta;
    private javax.swing.JTextField txt_Q2C_Pregunta;
    private javax.swing.JTextField txt_Q3C_Pregunta;
    // End of variables declaration//GEN-END:variables
}
