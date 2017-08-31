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
import java.text.SimpleDateFormat;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class LagunaFacultativaSec extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();
    //private DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
    private static Logger log = Logger.getLogger("Laguna Facultativa Sec."); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.ObtenerKey("rutabasedatos") + File.separator + conf.ObtenerKey("nombrebasedatos"));
    private Validaciones validar = new Validaciones();
    private Bod bod = new Bod("");
    private Listener_Popup Lpopup;
    private Listener_Texto Ltexto;
    Util util = new Util();

    public LagunaFacultativaSec(Bod bod) {
        this.bod = bod;
        initComponents();
        bod.WinLagunaFacultativaSec = true;
        InicializarComponentes();
    }

    /**
     * Inicializa todos los elementos (jlabels,jtextbox,etc.) con sus
     * respectivos valores y textos desde al base de datos.
     */
    public void InicializarComponentes() {

        ResultSet rs;
        bod.WinLagunaFacultativaSec = true;//Bandera La ventana se ha abierto

        try {//Todo: implementar donde esta error
            btn_guardar.setText("Guardar");
            btn_close.setText("Cerrar");

            // - - - - - - Titulo de la sección            
            rs = db.ResultadoSelect("obtenerseccion", "6", null);

            lbl_titulo1.setText(rs.getString("Nombre")); //Título de este jpane
            lbl_titulo2.setText(rs.getString("Mensaje"));

            // - - - - - - Pregunta 45 Cuestionario 3 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q2C Calculo de Caudales de Diseno
            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q2C");

            lbl_Q2C_titulo1.setText(rs.getString("titulo1"));
            lbl_Q2C_titulo2.setText(rs.getString("titulo2"));
            txt_Q2C_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(bod.getQ2C())), "#.#"));//Si estoy en esta pantalla ya debe existir q2c
            // - - - - - - Pregunta 6 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LAE
            rs = db.ResultadoSelect("obtenerpregunta", "5", "LAE");

            lbl_LAE_titulo1.setText(rs.getString("titulo1"));
            lbl_LAE_titulo2.setText(rs.getString("titulo2"));
            txt_LAE_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(bod.getLAE())), "#.#"));
            // - - - - - - Pregunta 1 Cuestionario 4 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - CAT
            rs = db.ResultadoSelect("obtenerpregunta", "4", "CAT");

            lbl_CAT_titulo1.setText(rs.getString("titulo1"));
            lbl_CAT_titulo2.setText(rs.getString("titulo2"));
            txt_CAT_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(bod.getCAT())), "#.#"));
            // - - - - - - Pregunta 4 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FTE
            rs = db.ResultadoSelect("obtenerpregunta", "6", "FTE");

            bod.minFTE = rs.getDouble("rangomin");
            bod.maxFTE = rs.getDouble("rangomax");
            bod.errorFTE = rs.getString("errormsg");
            lbl_FTE_titulo1.setText(rs.getString("titulo1"));
            lbl_FTE_titulo2.setText(rs.getString("titulo2"));
            txt_FTE_Pregunta.setText(rs.getString("valorpordefecto"));
            AsignarTextDoble(txt_FTE_Pregunta, bod.minFTE, bod.maxFTE, lbl_FTE_error, bod.errorFTE);
            AsignarPopupBtn(btn_FTE_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 450, 50);
            // - - - - - - Pregunta 5 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FTE
            rs = db.ResultadoSelect("obtenerpregunta", "6", "FCO");

            bod.minFCO = rs.getDouble("rangomin");
            bod.maxFCO = rs.getDouble("rangomax");
            bod.errorFCO = rs.getString("errormsg");
            lbl_FCO_titulo1.setText(rs.getString("titulo1"));
            lbl_FCO_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_FCO_Pregunta, bod.minFCO, bod.maxFCO, lbl_FCO_error, bod.errorFCO);
            AsignarPopupBtn(btn_FCO_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 420, 400);
            // - - - - - - Pregunta 6 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FCA
            rs = db.ResultadoSelect("obtenerpregunta", "6", "FCA");

            bod.minFCA = rs.getDouble("rangomin");
            bod.maxFCA = rs.getDouble("rangomax");
            bod.errorFCA = rs.getString("errormsg");
            lbl_FCA_titulo1.setText(rs.getString("titulo1"));
            lbl_FCA_titulo2.setText(rs.getString("titulo2"));
            lbl_FCA_desc.setText(rs.getString("descripcion"));
            txt_FCA_Pregunta.setText(rs.getString("valorpordefecto"));
            AsignarTextDoble(txt_FCA_Pregunta, bod.minFCA, bod.maxFCA, lbl_FCA_error, bod.errorFCA);
            AsignarPopupBtn(btn_FCA_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 380, 310);
            // - - - - - - Pregunta 7 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FAS
            rs = db.ResultadoSelect("obtenerpregunta", "6", "FAS");

            bod.minFAS = rs.getDouble("rangomin");
            bod.maxFAS = rs.getDouble("rangomax");
            bod.errorFAS = rs.getString("errormsg");
            lbl_FAS_titulo1.setText(rs.getString("titulo1"));
            lbl_FAS_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_FAS_Pregunta, bod.minFAS, bod.maxFAS, lbl_FAS_error, bod.errorFAS);
            ListenerCampoTxt(txt_FAS_Pregunta); //Se le agrega un listener a este campo no editable, por si recibe valores para calcular FAB
            // - - - - - - Pregunta 8 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FTR
            rs = db.ResultadoSelect("obtenerpregunta", "6", "FTR");

            bod.minFTR = rs.getDouble("rangomin");
            bod.maxFTR = rs.getDouble("rangomax");
            bod.errorFTR = rs.getString("errormsg");
            lbl_FTR_titulo1.setText(rs.getString("titulo1"));
            lbl_FTR_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_FTR_Pregunta, bod.minFTR, bod.maxFTR, lbl_FTR_error, bod.errorFTR);
            AsignarPopupBtn(btn_FTR_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 330);
            // - - - - - - Pregunta 9 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FED
            rs = db.ResultadoSelect("obtenerpregunta", "6", "FED");

            bod.minFED = rs.getDouble("rangomin");
            bod.maxFED = rs.getDouble("rangomax");
            bod.errorFED = rs.getString("errormsg");
            lbl_FED_titulo1.setText(rs.getString("titulo1"));
            lbl_FED_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_FED_Pregunta, bod.minFED, bod.maxFED, lbl_FED_error, bod.errorFED);
            // - - - - - - Pregunta 10 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FUV
            rs = db.ResultadoSelect("obtenerpregunta", "6", "FUV");

            bod.minFUV = rs.getDouble("rangomin");
            bod.maxFUV = rs.getDouble("rangomax");
            bod.errorFUV = rs.getString("errormsg");
            lbl_FUV_titulo1.setText(rs.getString("titulo1"));
            lbl_FUV_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_FUV_Pregunta, bod.minFUV, bod.maxFUV, lbl_FUV_error, bod.errorFUV);
            // - - - - - - Pregunta 11 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FSA
            rs = db.ResultadoSelect("obtenerpregunta", "6", "FSA");

            bod.minFSA = rs.getDouble("rangomin");
            bod.maxFSA = rs.getDouble("rangomax");
            bod.errorFSA = rs.getString("errormsg");
            lbl_FSA_titulo1.setText(rs.getString("titulo1"));
            lbl_FSA_titulo2.setText(rs.getString("titulo2"));
            lbl_FSA_desc.setText(rs.getString("descripcion"));
            AsignarTextDoble(txt_FSA_Pregunta, bod.minFSA, bod.maxFSA, lbl_FSA_error, bod.errorFSA);
            AsignarPopupBtn(btn_FSA_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 380, 150);
            // - - - - - - Pregunta 12 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FSL
            rs = db.ResultadoSelect("obtenerpregunta", "6", "FSL");

            bod.minFSL = rs.getDouble("rangomin");
            bod.maxFSL = rs.getDouble("rangomax");
            bod.errorFSL = rs.getString("errormsg");
            lbl_FSL_titulo1.setText(rs.getString("titulo1"));
            lbl_FSL_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_FSL_Pregunta, bod.minFSL, bod.maxFSL, lbl_FSL_error, bod.errorFSL);
            // - - - - - - Pregunta 13 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FAP
            rs = db.ResultadoSelect("obtenerpregunta", "6", "FAP");

            bod.minFAP = rs.getInt("rangomin");
            bod.maxFAP = rs.getInt("rangomax");
            bod.errorFAP = rs.getString("errormsg");
            lbl_FAP_titulo1.setText(rs.getString("titulo1"));
            lbl_FAP_titulo2.setText(rs.getString("titulo2"));
            txt_FAP_Pregunta.setText(rs.getString("valorpordefecto"));
            AsignarTextEntero(txt_FAP_Pregunta, bod.minFAP, bod.maxFAP, lbl_FAP_error, bod.errorFAP);
            AsignarPopupBtn(btn_FAP_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 310, 160);
            // - - - - - - Pregunta 14 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FAI
            rs = db.ResultadoSelect("obtenerpregunta", "6", "FAI");

            bod.minFAI = rs.getDouble("rangomin");
            bod.maxFAI = rs.getDouble("rangomax");
            bod.errorFAI = rs.getString("errormsg");
            lbl_FAI_titulo1.setText(rs.getString("titulo1"));
            lbl_FAI_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_FAI_Pregunta, bod.minFAI, bod.maxFAI, lbl_FAI_error, bod.errorFAI);
            AsignarPopupBtn(btn_FAI_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 250, 150);
            // - - - - - - Pregunta 15 Cuestionario 6 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - FAB
            rs = db.ResultadoSelect("obtenerpregunta", "6", "FAB");

            bod.minFAB = rs.getDouble("rangomin");
            bod.maxFAB = rs.getDouble("rangomax");
            bod.errorFAB = rs.getString("errormsg");
            lbl_FAB_titulo1.setText(rs.getString("titulo1"));
            lbl_FAB_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_FAB_Pregunta, bod.minFAB, bod.maxFAB, lbl_FAB_error, bod.errorFAB);
            AsignarPopupBtn(btn_FAB_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 350, 220);
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
            // Si Bod cargó datos de Laguna Facultativa secundaria desde la BD, porque Ya estaba editada, se proceden a llenar casillas con datos
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -          
            if (bod.EditLagunaFacultativaSec) {

                txt_FTE_Pregunta.setText(bod.getFTE());
                txt_FCO_Pregunta.setText(bod.getFCO());
                txt_FCA_Pregunta.setText(bod.getFCAs());
                txt_FAS_Pregunta.setText(bod.getFAS());
                txt_FTR_Pregunta.setText(bod.getFTR());
                txt_FED_Pregunta.setText(bod.getFED());
                txt_FUV_Pregunta.setText(bod.getFUV());
                txt_FSA_Pregunta.setText(bod.getFSAs());
                txt_FSL_Pregunta.setText(bod.getFSLs());

                txt_FAP_Pregunta.setText(bod.getFAP() + "");
                txt_FAI_Pregunta.setText(bod.getFAIs());
                txt_FAB_Pregunta.setText(bod.getFABs());
            } else {// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -si no realiza otras tareas de cálculos que se deben hacer al cargar

                String x = bod.CalcularFCO();
                txt_FCO_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.#"));

                x = bod.CalcularFAS(Double.parseDouble(txt_FCO_Pregunta.getText()));
                txt_FAS_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.#"));

                CalcularFTR();

                x = bod.CalcularFED(Double.parseDouble(txt_FTR_Pregunta.getText()));
                txt_FED_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.#"));

                x = bod.CalcularFUV(Double.parseDouble(txt_FCO_Pregunta.getText()));
                txt_FUV_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.#"));

                x = bod.CalcularFSA(Double.parseDouble(txt_FAS_Pregunta.getText()));
                txt_FSA_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.#"));

                x = bod.CalcularFSL(Double.parseDouble(txt_FSA_Pregunta.getText()));
                txt_FSL_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.#"));

                calcularFAI();
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
     * Comprueba todos los campos de esta ventana versus la clase de lógica de
     * datos. Hasta que esten correctos todos permite guardar.
     *
     * @return
     */
    private boolean Guardar() {

        lbl_save_error.setText("");
        lbl_save_error2.setText("");

        if (!bod.setFTE(txt_FTE_Pregunta.getText().trim(), bod.minFTE, bod.maxFTE)) {// Tasa de evaporación (e) mm/Día
            lbl_save_error2.setText("Punto 4, Tasa de evaporación ");
            lbl_save_error.setText(bod.errorFTE);
            return false;
        }

        if (!bod.setFCO(bod.CalcularFCO(), bod.minFCO, bod.maxFCO)) {// Carga Orgánica Volumétrica (COVLF) g/m³*Día 
            lbl_save_error2.setText("Punto 5, Carga Orgánica Volumétrica, Se deben corregir los datos anteriores dependientes");
            lbl_save_error.setText(bod.errorFCO);
            return false;
        }

        if (!bod.setFCA(txt_FCA_Pregunta.getText().trim(), bod.minFCA, bod.maxFCA)) {// Altura Útil (hLF) m Cálculo de Área Superficial
            lbl_save_error2.setText("Punto 6, Altura Útil");
            lbl_save_error.setText(bod.errorFCA);
            return false;
        }

        //en este punto el dato bod FCO ya existe, por esto calcular...(0)
        if (!bod.setFAS(bod.CalcularFAS(0), bod.minFAS, bod.maxFAS)) {// Área Superficial (ASLF) m²
            lbl_save_error2.setText("Punto 7, Área Superficial, Se deben corregir los datos anteriores dependientes");
            lbl_save_error.setText(bod.errorFAS);
            return false;
        }

        //en este punto el dato bod FAS,FCA,FTE, ya existen, por esto calcular...(0,0,0)
        if (!bod.setFTR(bod.CalcularFTR(0, 0, 0), bod.minFTR, bod.maxFTR)) {// Tiempo de retención hidráulico (TRLF) días
            lbl_save_error2.setText("Punto 8, Tiempo de retención hidráulico, Se deben corregir los datos anteriores dependientes");
            lbl_save_error.setText(bod.errorFTR);
            return false;
        }

        //en este punto el dato bod FTR ya existe, por esto calcular...(0)
        if (!bod.setFED(bod.CalcularFED(0), bod.minFED, bod.maxFED)) {// Eficiencia de DBO5 estimada %LF 
            lbl_save_error2.setText("Punto 9, Eficiencia de DBO5 estimada, Se deben corregir los datos anteriores dependientes");
            lbl_save_error.setText(bod.errorFED);
            return false;
        }

        //en este punto el dato bod FCO ya existe, por esto calcular...(0)
        if (!bod.setFUV(bod.CalcularFUV(0), bod.minFUV, bod.maxFUV)) {// Volumen Útil (VLA) m³ 
            lbl_save_error2.setText("Punto 10, Volumen Útil, Se deben corregir los datos anteriores dependientes");
            lbl_save_error.setText(bod.errorFUV);
            return false;
        }

        //en este punto el dato bod FAS ya existe, por esto calcular...(0)
        if (!bod.setFSA(bod.CalcularFSA(0), bod.minFSA, bod.maxFSA)) {// Ancho (aLF) m Relación Largo: Ancho 
            lbl_save_error2.setText("Punto 11, Ancho, Se deben corregir los datos anteriores dependientes");
            lbl_save_error.setText(bod.errorFSA);
            return false;
        }

        //en este punto el dato bod FSA ya existe, por esto calcular...(0)
        if (!bod.setFSL(bod.CalcularFSL(0), bod.minFSL, bod.maxFSL)) {// Ancho (aLF) m Relación Largo: Ancho 
            lbl_save_error2.setText("Punto 11, Largo, Se deben corregir los datos anteriores dependientes");
            lbl_save_error.setText(bod.errorFSL);
            return false;
        }

        if (!bod.setFAP(txt_FAP_Pregunta.getText(), bod.minFAP, bod.maxFAP)) {// Pendiente n
            lbl_save_error2.setText("Punto 12, Pendiente n");
            lbl_save_error.setText(bod.errorFAP);
            return false;
        }

        //en este punto el dato bod FAP ya existe, por esto calcular...(0)
        if (!bod.setFAI(bod.calcularFAI(0), bod.minFAI, bod.maxFAI)) {// Ángulo de inclinación de la pendiente α
            lbl_save_error2.setText("Punto 13, Ángulo de inclinación de la pendiente α");
            lbl_save_error.setText(bod.errorFAI);
            return false;
        }

        //en este punto el dato bod FAS ya existe, por esto calcular...(0)
        if (!bod.setFAB(bod.calcularFAB(0), bod.minFAB, bod.maxFAB)) {// Ángulo de inclinación de la pendiente α
            lbl_save_error2.setText("Punto 13, Ángulo de inclinación de la pendiente α");
            lbl_save_error.setText(bod.errorFAB);
            return false;
        }

        bod.EditLagunaFacultativaSec = true;

        if (!bod.GuardarUpdateBod()) {
            bod.EditLagunaFacultativaSec = false;
            return false;
        }
        return true;
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

    /**
     * *
     * Agrega un listener a LAS para calcular LAA y LAL
     *
     * @param jt recibe el campo de texto que se le agregaqra el listener
     */
    private void ListenerCampoTxt(JTextField jtfield) {

        jtfield.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
            }

            public void removeUpdate(DocumentEvent e) {
            }

            public void insertUpdate(DocumentEvent e) {
                calcularFAB();//Sucede cuando el àrea superficial FAS es calculada, se debe calcular temporalmente FAB Borde libre
            }
        });
    }

    private void CalcularFTR() { //no se nota el cambio inmediato debido a la regla

        String x = bod.CalcularFTR(Double.parseDouble(txt_FAS_Pregunta.getText()), Double.parseDouble(txt_FCA_Pregunta.getText()), Double.parseDouble(txt_FTE_Pregunta.getText()));
        txt_FTR_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.#"));
    }

    private void calcularFAI() {

        String x = txt_FAP_Pregunta.getText().trim();
        if (validar.EsEnteroEntre(x, bod.minFAP, bod.maxFAP)) {
            x = bod.calcularFAI(Integer.parseInt(x));
            txt_FAI_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.#"));
        } else {
            txt_FAI_Pregunta.setText("");
        }
    }

    private void calcularFAB() {

        String x = txt_FAS_Pregunta.getText().trim();
        if (validar.EsDobleEntre(x, bod.minFAS, bod.maxFAS)) {
            x = bod.calcularFAB(Double.parseDouble(x));
            txt_FAB_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.#"));
        } else {
            txt_FAB_Pregunta.setText("");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jsp_LagunaFacultativasec = new javax.swing.JScrollPane();
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
        lbl_save_error2 = new javax.swing.JLabel();
        lbl_CAT_titulo1 = new javax.swing.JLabel();
        txt_CAT_Pregunta = new javax.swing.JTextField();
        lbl_CAT_titulo2 = new javax.swing.JLabel();
        lbl_CAT_error = new javax.swing.JLabel();
        txt_FTE_Pregunta = new javax.swing.JTextField();
        lbl_FTE_titulo1 = new javax.swing.JLabel();
        lbl_FTE_error = new javax.swing.JLabel();
        lbl_FTE_titulo2 = new javax.swing.JLabel();
        lbl_FCA_error = new javax.swing.JLabel();
        lbl_FCA_titulo2 = new javax.swing.JLabel();
        txt_FCA_Pregunta = new javax.swing.JTextField();
        lbl_FCA_titulo1 = new javax.swing.JLabel();
        lbl_FCA_desc = new javax.swing.JLabel();
        lbl_FAS_error = new javax.swing.JLabel();
        lbl_FAS_titulo2 = new javax.swing.JLabel();
        txt_FAS_Pregunta = new javax.swing.JTextField();
        lbl_FAS_titulo1 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        txt_FED_Pregunta = new javax.swing.JTextField();
        lbl_FED_titulo1 = new javax.swing.JLabel();
        lbl_FUV_error = new javax.swing.JLabel();
        lbl_FUV_titulo2 = new javax.swing.JLabel();
        lbl_FED_error = new javax.swing.JLabel();
        txt_FUV_Pregunta = new javax.swing.JTextField();
        lbl_FUV_titulo1 = new javax.swing.JLabel();
        lbl_FED_titulo2 = new javax.swing.JLabel();
        lbl_FTR_titulo1 = new javax.swing.JLabel();
        txt_FTR_Pregunta = new javax.swing.JTextField();
        lbl_FTR_titulo2 = new javax.swing.JLabel();
        lbl_FTR_error = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        lbl_FSA_desc = new javax.swing.JLabel();
        lbl_FSA_titulo1 = new javax.swing.JLabel();
        lbl_FSA_error = new javax.swing.JLabel();
        lbl_FSA_titulo2 = new javax.swing.JLabel();
        txt_FSA_Pregunta = new javax.swing.JTextField();
        lbl_FSL_titulo1 = new javax.swing.JLabel();
        lbl_FSL_error = new javax.swing.JLabel();
        lbl_FSL_titulo2 = new javax.swing.JLabel();
        txt_FSL_Pregunta = new javax.swing.JTextField();
        lbl_FCO_titulo1 = new javax.swing.JLabel();
        txt_FCO_Pregunta = new javax.swing.JTextField();
        lbl_FCO_titulo2 = new javax.swing.JLabel();
        lbl_FCO_error = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        txt_FAB_Pregunta = new javax.swing.JTextField();
        lbl_FAB_error = new javax.swing.JLabel();
        lbl_FAB_titulo1 = new javax.swing.JLabel();
        lbl_FAP_titulo2 = new javax.swing.JLabel();
        lbl_FAB_titulo2 = new javax.swing.JLabel();
        txt_FAI_Pregunta = new javax.swing.JTextField();
        lbl_FAP_titulo1 = new javax.swing.JLabel();
        lbl_FAI_error = new javax.swing.JLabel();
        lbl_FAI_titulo2 = new javax.swing.JLabel();
        txt_FAP_Pregunta = new javax.swing.JTextField();
        lbl_FAI_titulo1 = new javax.swing.JLabel();
        lbl_FAP_error = new javax.swing.JLabel();
        btn_FTE_ayuda = new javax.swing.JButton();
        btn_FAB_ayuda = new javax.swing.JButton();
        btn_FCO_ayuda = new javax.swing.JButton();
        btn_FCA_ayuda = new javax.swing.JButton();
        btn_FTR_ayuda = new javax.swing.JButton();
        btn_FSA_ayuda = new javax.swing.JButton();
        btn_FAP_ayuda = new javax.swing.JButton();
        btn_FAI_ayuda = new javax.swing.JButton();
        btn_close2 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1024, 768));

        jsp_LagunaFacultativasec.setPreferredSize(new java.awt.Dimension(1024, 768));

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
        jp_Componentes.add(btn_guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 810, 120, -1));
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
        jp_Componentes.add(btn_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 850, 120, 30));

        lbl_save_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error.setText(" . . .");
        jp_Componentes.add(lbl_save_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 800, 400, 35));

        lbl_save_error2.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error2.setText(" . . .");
        jp_Componentes.add(lbl_save_error2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 850, 400, 35));

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

        txt_FTE_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_FTE_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_FTE_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 170, 130, 25));

        lbl_FTE_titulo1.setText("Titulo");
        lbl_FTE_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FTE_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 300, 30));

        lbl_FTE_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FTE_error.setText(" . . .");
        jp_Componentes.add(lbl_FTE_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 170, 340, 35));

        lbl_FTE_titulo2.setText("Titulo");
        lbl_FTE_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FTE_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 170, 80, 25));

        lbl_FCA_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FCA_error.setText(" . . .");
        jp_Componentes.add(lbl_FCA_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 290, 340, 35));

        lbl_FCA_titulo2.setText("Titulo");
        lbl_FCA_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FCA_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 300, 80, 25));

        txt_FCA_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_FCA_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_FCA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 300, 130, 25));

        lbl_FCA_titulo1.setText("Titulo");
        lbl_FCA_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FCA_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 300, 30));

        lbl_FCA_desc.setText("Desc");
        lbl_FCA_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FCA_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 600, 30));

        lbl_FAS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FAS_error.setText(" . . .");
        jp_Componentes.add(lbl_FAS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 330, 340, 35));

        lbl_FAS_titulo2.setText("Titulo");
        lbl_FAS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FAS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 340, 80, 25));

        txt_FAS_Pregunta.setEditable(false);
        jp_Componentes.add(txt_FAS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 340, 130, 25));

        lbl_FAS_titulo1.setText("Titulo");
        lbl_FAS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FAS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 300, 30));
        jp_Componentes.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 382, 1014, 2));

        txt_FED_Pregunta.setEditable(false);
        jp_Componentes.add(txt_FED_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 440, 130, 25));

        lbl_FED_titulo1.setText("Titulo");
        lbl_FED_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FED_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, 300, 30));

        lbl_FUV_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FUV_error.setText(" . . .");
        jp_Componentes.add(lbl_FUV_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 470, 340, 35));

        lbl_FUV_titulo2.setText("Titulo");
        lbl_FUV_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FUV_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 480, 80, 25));

        lbl_FED_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FED_error.setText(" . . .");
        jp_Componentes.add(lbl_FED_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 430, 340, 35));

        txt_FUV_Pregunta.setEditable(false);
        jp_Componentes.add(txt_FUV_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 480, 130, 25));

        lbl_FUV_titulo1.setText("Titulo");
        lbl_FUV_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FUV_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 480, 300, 30));

        lbl_FED_titulo2.setText("Titulo");
        lbl_FED_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FED_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 440, 80, 25));

        lbl_FTR_titulo1.setText("Titulo");
        lbl_FTR_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FTR_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 300, 30));

        txt_FTR_Pregunta.setEditable(false);
        jp_Componentes.add(txt_FTR_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 400, 130, 25));

        lbl_FTR_titulo2.setText("Titulo");
        lbl_FTR_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FTR_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 400, 80, 25));

        lbl_FTR_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FTR_error.setText(" . . .");
        jp_Componentes.add(lbl_FTR_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 390, 340, 35));
        jp_Componentes.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, 650, 1110, 2));

        lbl_FSA_desc.setText("Desc");
        lbl_FSA_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FSA_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 600, 30));

        lbl_FSA_titulo1.setText("Titulo");
        lbl_FSA_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FSA_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 570, 300, 30));

        lbl_FSA_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FSA_error.setText(" . . .");
        jp_Componentes.add(lbl_FSA_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 560, 340, 35));

        lbl_FSA_titulo2.setText("Titulo");
        lbl_FSA_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FSA_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 570, 80, 25));

        txt_FSA_Pregunta.setEditable(false);
        jp_Componentes.add(txt_FSA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 570, 130, 25));

        lbl_FSL_titulo1.setText("Titulo");
        lbl_FSL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FSL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 610, 300, 30));

        lbl_FSL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FSL_error.setText(" . . .");
        jp_Componentes.add(lbl_FSL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 600, 340, 35));

        lbl_FSL_titulo2.setText("Titulo");
        lbl_FSL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FSL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 610, 80, 25));

        txt_FSL_Pregunta.setEditable(false);
        jp_Componentes.add(txt_FSL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 610, 130, 25));

        lbl_FCO_titulo1.setText("Titulo");
        lbl_FCO_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FCO_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 300, 30));

        txt_FCO_Pregunta.setEditable(false);
        jp_Componentes.add(txt_FCO_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 210, 130, 25));

        lbl_FCO_titulo2.setText("Titulo");
        lbl_FCO_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FCO_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 210, 80, 25));

        lbl_FCO_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FCO_error.setText(" . . .");
        jp_Componentes.add(lbl_FCO_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 210, 340, 35));
        jp_Componentes.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 790, 1084, 2));

        txt_FAB_Pregunta.setEditable(false);
        jp_Componentes.add(txt_FAB_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 750, 130, 25));

        lbl_FAB_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FAB_error.setText(" . . .");
        jp_Componentes.add(lbl_FAB_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 740, 340, 35));

        lbl_FAB_titulo1.setText("Titulo");
        lbl_FAB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FAB_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 740, 300, 30));

        lbl_FAP_titulo2.setText("Titulo");
        lbl_FAP_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FAP_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 670, 80, 25));

        lbl_FAB_titulo2.setText("Titulo");
        lbl_FAB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FAB_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 750, 80, 25));

        txt_FAI_Pregunta.setEditable(false);
        jp_Componentes.add(txt_FAI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 710, 130, 25));

        lbl_FAP_titulo1.setText("Titulo");
        lbl_FAP_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FAP_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 660, 300, 30));

        lbl_FAI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FAI_error.setText(" . . .");
        jp_Componentes.add(lbl_FAI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 700, 340, 35));

        lbl_FAI_titulo2.setText("Titulo");
        lbl_FAI_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FAI_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 710, 80, 25));

        txt_FAP_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_FAP_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_FAP_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 670, 130, 25));

        lbl_FAI_titulo1.setText("Titulo");
        lbl_FAI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FAI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 700, 300, 30));

        lbl_FAP_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FAP_error.setText(" . . .");
        jp_Componentes.add(lbl_FAP_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 660, 340, 35));

        btn_FTE_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_FTE_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_FTE_ayuda.setContentAreaFilled(false);
        btn_FTE_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_FTE_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 170, 25, 25));

        btn_FAB_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_FAB_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_FAB_ayuda.setContentAreaFilled(false);
        btn_FAB_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_FAB_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 750, 25, 25));

        btn_FCO_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_FCO_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_FCO_ayuda.setContentAreaFilled(false);
        btn_FCO_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_FCO_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 210, 25, 25));

        btn_FCA_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_FCA_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_FCA_ayuda.setContentAreaFilled(false);
        btn_FCA_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_FCA_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 300, 25, 25));

        btn_FTR_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_FTR_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_FTR_ayuda.setContentAreaFilled(false);
        btn_FTR_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_FTR_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 400, 25, 25));

        btn_FSA_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_FSA_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_FSA_ayuda.setContentAreaFilled(false);
        btn_FSA_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_FSA_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 530, 25, 25));

        btn_FAP_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_FAP_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_FAP_ayuda.setContentAreaFilled(false);
        btn_FAP_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_FAP_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 670, 25, 25));

        btn_FAI_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_FAI_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_FAI_ayuda.setContentAreaFilled(false);
        btn_FAI_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_FAI_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 710, 25, 25));

        jsp_LagunaFacultativasec.setViewportView(jp_Componentes);

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
            .addComponent(jsp_LagunaFacultativasec, javax.swing.GroupLayout.DEFAULT_SIZE, 1008, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_close2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btn_close2)
                .addGap(2, 2, 2)
                .addComponent(jsp_LagunaFacultativasec, javax.swing.GroupLayout.DEFAULT_SIZE, 1025, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        bod.WinLagunaFacultativaSec = false;
        this.dispose();
    }//GEN-LAST:event_btn_closeActionPerformed

    private void txt_FTE_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_FTE_PreguntaKeyReleased
        // Calcular FTR temporalmente
        CalcularFTR();
    }//GEN-LAST:event_txt_FTE_PreguntaKeyReleased

    private void txt_FCA_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_FCA_PreguntaKeyReleased
        // Calcular FTR temporalmente
        CalcularFTR();
    }//GEN-LAST:event_txt_FCA_PreguntaKeyReleased

    private void txt_FAP_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_FAP_PreguntaKeyReleased
        //Listener para calcular automática y temporalmente LAI
        calcularFAI();
    }//GEN-LAST:event_txt_FAP_PreguntaKeyReleased

    private void btn_close2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_close2ActionPerformed
        bod.WinLagunaFacultativaSec = false;
        this.dispose();
    }//GEN-LAST:event_btn_close2ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_FAB_ayuda;
    private javax.swing.JButton btn_FAI_ayuda;
    private javax.swing.JButton btn_FAP_ayuda;
    private javax.swing.JButton btn_FCA_ayuda;
    private javax.swing.JButton btn_FCO_ayuda;
    private javax.swing.JButton btn_FSA_ayuda;
    private javax.swing.JButton btn_FTE_ayuda;
    private javax.swing.JButton btn_FTR_ayuda;
    private javax.swing.JButton btn_close;
    private javax.swing.JButton btn_close2;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JPanel jp_Componentes;
    private javax.swing.JScrollPane jsp_LagunaFacultativasec;
    private javax.swing.JLabel lbl_CAT_error;
    private javax.swing.JLabel lbl_CAT_titulo1;
    private javax.swing.JLabel lbl_CAT_titulo2;
    private javax.swing.JLabel lbl_FAB_error;
    private javax.swing.JLabel lbl_FAB_titulo1;
    private javax.swing.JLabel lbl_FAB_titulo2;
    private javax.swing.JLabel lbl_FAI_error;
    private javax.swing.JLabel lbl_FAI_titulo1;
    private javax.swing.JLabel lbl_FAI_titulo2;
    private javax.swing.JLabel lbl_FAP_error;
    private javax.swing.JLabel lbl_FAP_titulo1;
    private javax.swing.JLabel lbl_FAP_titulo2;
    private javax.swing.JLabel lbl_FAS_error;
    private javax.swing.JLabel lbl_FAS_titulo1;
    private javax.swing.JLabel lbl_FAS_titulo2;
    private javax.swing.JLabel lbl_FCA_desc;
    private javax.swing.JLabel lbl_FCA_error;
    private javax.swing.JLabel lbl_FCA_titulo1;
    private javax.swing.JLabel lbl_FCA_titulo2;
    private javax.swing.JLabel lbl_FCO_error;
    private javax.swing.JLabel lbl_FCO_titulo1;
    private javax.swing.JLabel lbl_FCO_titulo2;
    private javax.swing.JLabel lbl_FED_error;
    private javax.swing.JLabel lbl_FED_titulo1;
    private javax.swing.JLabel lbl_FED_titulo2;
    private javax.swing.JLabel lbl_FSA_desc;
    private javax.swing.JLabel lbl_FSA_error;
    private javax.swing.JLabel lbl_FSA_titulo1;
    private javax.swing.JLabel lbl_FSA_titulo2;
    private javax.swing.JLabel lbl_FSL_error;
    private javax.swing.JLabel lbl_FSL_titulo1;
    private javax.swing.JLabel lbl_FSL_titulo2;
    private javax.swing.JLabel lbl_FTE_error;
    private javax.swing.JLabel lbl_FTE_titulo1;
    private javax.swing.JLabel lbl_FTE_titulo2;
    private javax.swing.JLabel lbl_FTR_error;
    private javax.swing.JLabel lbl_FTR_titulo1;
    private javax.swing.JLabel lbl_FTR_titulo2;
    private javax.swing.JLabel lbl_FUV_error;
    private javax.swing.JLabel lbl_FUV_titulo1;
    private javax.swing.JLabel lbl_FUV_titulo2;
    private javax.swing.JLabel lbl_LAE_error;
    private javax.swing.JLabel lbl_LAE_titulo1;
    private javax.swing.JLabel lbl_LAE_titulo2;
    private javax.swing.JLabel lbl_Q2C_error;
    private javax.swing.JLabel lbl_Q2C_titulo1;
    private javax.swing.JLabel lbl_Q2C_titulo2;
    private javax.swing.JLabel lbl_save_error;
    private javax.swing.JLabel lbl_save_error2;
    private javax.swing.JLabel lbl_titulo1;
    private javax.swing.JLabel lbl_titulo2;
    private javax.swing.JTextField txt_CAT_Pregunta;
    private javax.swing.JTextField txt_FAB_Pregunta;
    private javax.swing.JTextField txt_FAI_Pregunta;
    private javax.swing.JTextField txt_FAP_Pregunta;
    private javax.swing.JTextField txt_FAS_Pregunta;
    private javax.swing.JTextField txt_FCA_Pregunta;
    private javax.swing.JTextField txt_FCO_Pregunta;
    private javax.swing.JTextField txt_FED_Pregunta;
    private javax.swing.JTextField txt_FSA_Pregunta;
    private javax.swing.JTextField txt_FSL_Pregunta;
    private javax.swing.JTextField txt_FTE_Pregunta;
    private javax.swing.JTextField txt_FTR_Pregunta;
    private javax.swing.JTextField txt_FUV_Pregunta;
    private javax.swing.JTextField txt_LAE_Pregunta;
    private javax.swing.JTextField txt_Q2C_Pregunta;
    // End of variables declaration//GEN-END:variables
}
