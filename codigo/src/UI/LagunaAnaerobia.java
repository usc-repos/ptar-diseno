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
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class LagunaAnaerobia extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();     
    private static Logger log = Logger.getLogger("Laguna_Anaerobia"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.ObtenerKey("rutabasedatos") + File.separator + conf.ObtenerKey("nombrebasedatos"));
    private Validaciones validar = new Validaciones();
    private Bod bod = new Bod("");
    ButtonGroup btnGrupLLA = new ButtonGroup();
    private Listener_Popup Lpopup;
    private Listener_Texto Ltexto;
    Util util = new Util();

    public LagunaAnaerobia(Bod bod) {
        this.bod = bod;
        initComponents();
        bod.WinLagunaAnaerobia = true;
        InicializarComponentes();
    }

    /**
     * Inicializa todos los elementos (jlabels,jtextbox,etc.) con sus
     * respectivos valores y textos desde al base de datos.
     */
    public void InicializarComponentes() {

        ResultSet rs;
        bod.WinLagunaAnaerobia = true;//Bandera La ventana se ha abierto

        try {//Todo: implementar donde esta error
            btn_guardar.setText("Guardar");
            btn_close.setText("Cerrar");

            // - - - - - - Titulo de la sección            
            rs = db.ResultadoSelect("obtenerseccion", "5", null);

            lbl_titulo1.setText(rs.getString("Nombre")); //Título de este jpane
            lbl_titulo2.setText(rs.getString("Mensaje"));

            // - - - - - - Pregunta 45 Cuestionario 3 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q2C Calculo de Caudales de Diseno
            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q2C");

            lbl_Q2C_titulo1.setText(rs.getString("titulo1"));
            lbl_Q2C_titulo2.setText(rs.getString("titulo2"));
            txt_Q2C_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(bod.getQ2C())), "#.#"));//Si estoy en esta pantalla ya debe existir q2c
            // - - - - - - Pregunta 2 Cuestionario 4 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - CAB
            rs = db.ResultadoSelect("obtenerpregunta", "4", "CAB");

            lbl_CAB_titulo1.setText(rs.getString("titulo1"));
            lbl_CAB_titulo2.setText(rs.getString("titulo2"));
            txt_CAB_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(bod.getCAB())), "#.#"));
            // - - - - - - Pregunta 1 Cuestionario 4 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - CAT
            rs = db.ResultadoSelect("obtenerpregunta", "4", "CAT");

            lbl_CAT_titulo1.setText(rs.getString("titulo1"));
            lbl_CAT_titulo2.setText(rs.getString("titulo2"));
            txt_CAT_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(bod.getCAT())), "#.#"));
            // - - - - - - Pregunta 4 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LCO
            rs = db.ResultadoSelect("obtenerpregunta", "5", "LCO"); //Todo: no es necesario 'AsignarTextDoble' y otros, verificar en transcurso

            bod.minLCO = rs.getDouble("rangomin");
            bod.maxLCO = rs.getDouble("rangomax");
            bod.errorLCO = rs.getString("errormsg");
            lbl_LCO_titulo1.setText(rs.getString("titulo1"));
            lbl_LCO_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_LCO_Pregunta, bod.minLCO, bod.maxLCO, lbl_LCO_error, bod.errorLCO);
            AsignarPopupBtn(btn_LCO_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 440, 490);
            // - - - - - - Pregunta 5 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LCE
            rs = db.ResultadoSelect("obtenerpregunta", "5", "LCE");

            bod.minLCE = rs.getDouble("rangomin");
            bod.maxLCE = rs.getDouble("rangomax");
            bod.errorLCE = rs.getString("errormsg");
            lbl_LCE_titulo1.setText(rs.getString("titulo1"));
            lbl_LCE_titulo2.setText(rs.getString("titulo2"));
            lbl_LCE_desc.setText(rs.getString("descripcion"));
            AsignarTextDoble(txt_LCE_Pregunta, bod.minLCE, bod.maxLCE, lbl_LCE_error, bod.errorLCE);
            // - - - - - - Pregunta 6 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LAE
            rs = db.ResultadoSelect("obtenerpregunta", "5", "LAE");

            bod.minLAE = rs.getDouble("rangomin");
            bod.maxLAE = rs.getDouble("rangomax");
            bod.errorLAE = rs.getString("errormsg");
            lbl_LAE_titulo1.setText(rs.getString("titulo1"));
            lbl_LAE_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_LAE_Pregunta, bod.minLAE, bod.maxLAE, lbl_LAE_error, bod.errorLAE);
            // - - - - - - Pregunta 7 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LVU
            rs = db.ResultadoSelect("obtenerpregunta", "5", "LVU");

            bod.minLVU = rs.getDouble("rangomin");
            bod.maxLVU = rs.getDouble("rangomax");
            bod.errorLVU = rs.getString("errormsg");
            lbl_LVU_titulo1.setText(rs.getString("titulo1"));
            lbl_LVU_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_LVU_Pregunta, bod.minLVU, bod.maxLVU, lbl_LVU_error, bod.errorLVU);
            // - - - - - - Pregunta 8 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LTH
            rs = db.ResultadoSelect("obtenerpregunta", "5", "LTH");

            bod.minLTH = rs.getDouble("rangomin");
            bod.maxLTH = rs.getDouble("rangomax");
            bod.errorLTH = rs.getString("errormsg");
            lbl_LTH_titulo1.setText(rs.getString("titulo1"));
            lbl_LTH_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_LTH_Pregunta, bod.minLTH, bod.maxLTH, lbl_LTH_error, bod.errorLTH);
            AsignarPopupBtn(btn_LTH_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 410, 150);
            // - - - - - - Pregunta 9 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LVR
            rs = db.ResultadoSelect("obtenerpregunta", "5", "LVR");

            bod.minLVR = rs.getDouble("rangomin");
            bod.maxLVR = rs.getDouble("rangomax");
            bod.errorLVR = rs.getString("errormsg");
            lbl_LVR_titulo1.setText(rs.getString("titulo1"));
            lbl_LVR_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_LVR_Pregunta, bod.minLVR, bod.maxLVR, lbl_LVR_error, bod.errorLVR);
            // - - - - - - Pregunta 10 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LAU
            rs = db.ResultadoSelect("obtenerpregunta", "5", "LAU");

            bod.minLAU = rs.getDouble("rangomin");
            bod.maxLAU = rs.getDouble("rangomax");
            bod.errorLAU = rs.getString("errormsg");
            lbl_LAU_titulo1.setText(rs.getString("titulo1"));
            lbl_LAU_titulo2.setText(rs.getString("titulo2"));
            lbl_LAU_desc.setText(rs.getString("descripcion"));
            txt_LAU_Pregunta.setText(rs.getString("valorpordefecto"));
            AsignarTextDoble(txt_LAU_Pregunta, bod.minLAU, bod.maxLAU, lbl_LAU_error, bod.errorLAU);
            AsignarPopupBtn(btn_LAU_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 470, 240);
            // - - - - - - Pregunta 11 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LAS
            rs = db.ResultadoSelect("obtenerpregunta", "5", "LAS");

            bod.minLAS = rs.getDouble("rangomin");
            bod.maxLAS = rs.getDouble("rangomax");
            bod.errorLAS = rs.getString("errormsg");
            lbl_LAS_titulo1.setText(rs.getString("titulo1"));
            lbl_LAS_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_LAS_Pregunta, bod.minLAS, bod.maxLAS, lbl_LAS_error, bod.errorLAS);
            ListenerCampoTxt(txt_LAS_Pregunta); //Se le agrega un listener a este campo no editable, por si recibe valores para calcular LAA LAL LAB
            // - - - - - - Pregunta 12 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LLA
            rs = db.ResultadoSelect("obtenerpregunta", "5", "LLA");

            btnGrupLLA.add(rbtn_LLA_Pregunta1);
            btnGrupLLA.add(rbtn_LLA_Pregunta2);
            lbl_LLA_desc.setText(rs.getString("descripcion"));
            rbtn_LLA_Pregunta1.setText(rs.getString("titulo1"));
            rbtn_LLA_Pregunta2.setText(rs.getString("titulo2"));
            AsignarPopupBtn(btn_LLA_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 470, 120);

            rbtn_LLA_Pregunta1.addActionListener(new ActionListener() { //Listener para el radiobutton
                @Override
                public void actionPerformed(ActionEvent e) {
                    calcularLAALAL();
                }
            });

            rbtn_LLA_Pregunta2.addActionListener(new ActionListener() { //Listener para el radiobutton
                @Override
                public void actionPerformed(ActionEvent e) {
                    calcularLAALAL();
                }
            });
            // - - - - - - Pregunta 13 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LAA
            rs = db.ResultadoSelect("obtenerpregunta", "5", "LAA");

            bod.minLAA = rs.getDouble("rangomin");
            bod.maxLAA = rs.getDouble("rangomax");
            bod.errorLAA = rs.getString("errormsg");
            lbl_LAA_titulo1.setText(rs.getString("titulo1"));
            lbl_LAA_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_LAA_Pregunta, bod.minLAA, bod.maxLAA, lbl_LAA_error, bod.errorLAA);
            // - - - - - - Pregunta 14 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LAL
            rs = db.ResultadoSelect("obtenerpregunta", "5", "LAL");

            bod.minLAL = rs.getDouble("rangomin");
            bod.maxLAL = rs.getDouble("rangomax");
            bod.errorLAL = rs.getString("errormsg");
            lbl_LAL_titulo1.setText(rs.getString("titulo1"));
            lbl_LAL_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_LAL_Pregunta, bod.minLAL, bod.maxLAL, lbl_LAL_error, bod.errorLAL);
            // - - - - - - Pregunta 15 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LAP
            rs = db.ResultadoSelect("obtenerpregunta", "5", "LAP");

            bod.minLAP = rs.getInt("rangomin");
            bod.maxLAP = rs.getInt("rangomax");
            bod.errorLAP = rs.getString("errormsg");
            lbl_LAP_titulo1.setText(rs.getString("titulo1"));
            lbl_LAP_titulo2.setText(rs.getString("titulo2"));
            txt_LAP_Pregunta.setText(rs.getString("valorpordefecto"));
            AsignarTextEntero(txt_LAP_Pregunta, bod.minLAP, bod.maxLAP, lbl_LAP_error, bod.errorLAP);
            AsignarPopupBtn(btn_LAP_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 310, 160);
            // - - - - - - Pregunta 16 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LAI
            rs = db.ResultadoSelect("obtenerpregunta", "5", "LAI");

            bod.minLAI = rs.getDouble("rangomin");
            bod.maxLAI = rs.getDouble("rangomax");
            bod.errorLAI = rs.getString("errormsg");
            lbl_LAI_titulo1.setText(rs.getString("titulo1"));
            lbl_LAI_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_LAI_Pregunta, bod.minLAI, bod.maxLAI, lbl_LAI_error, bod.errorLAI);
            AsignarPopupBtn(btn_LAI_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 250, 150);
            // - - - - - - Pregunta 17 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LAB
            rs = db.ResultadoSelect("obtenerpregunta", "5", "LAB");

            bod.minLAB = rs.getDouble("rangomin");
            bod.maxLAB = rs.getDouble("rangomax");
            bod.errorLAB = rs.getString("errormsg");
            lbl_LAB_titulo1.setText(rs.getString("titulo1"));
            lbl_LAB_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_LAB_Pregunta, bod.minLAB, bod.maxLAB, lbl_LAB_error, bod.errorLAB);
            AsignarPopupBtn(btn_LAB_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 350, 220);
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
            // Si Bod cargó datos de Laguna Anaerobia desde la BD, porque Ya estaba editada, se proceden a llenar casillas con datos
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -          
            if (bod.EditLagunaAnaerobia) {

                txt_LCO_Pregunta.setText(bod.getLCO());
                txt_LCE_Pregunta.setText(bod.getLCE());
                txt_LAE_Pregunta.setText(bod.getLAE());
                txt_LVU_Pregunta.setText(bod.getLVU());
                txt_LTH_Pregunta.setText(bod.getLTH());
                txt_LVR_Pregunta.setText(bod.getLVR());
                txt_LAU_Pregunta.setText(bod.getLAUs());
                txt_LAS_Pregunta.setText(bod.getLAS());

                if (bod.getLLA() > 0) { //1:3
                    rbtn_LLA_Pregunta1.setSelected(true);
                } else { //2:3
                    rbtn_LLA_Pregunta2.setSelected(true);
                }
                txt_LAA_Pregunta.setText(bod.getLAAs());
                txt_LAL_Pregunta.setText(bod.getLALs());

                txt_LAP_Pregunta.setText(bod.getLAP() + "");
                txt_LAI_Pregunta.setText(bod.getLAIs());
                txt_LAB_Pregunta.setText(bod.getLABs());

            } else {// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -si no otras tareas de cálculos que se deben hacer al cargar
                String x = bod.CalcularLCO(0);
                txt_LCO_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.#"));

                x = bod.CalcularLCE(0);
                txt_LCE_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.#"));

                x = bod.CalcularLAE(Double.parseDouble(txt_LCE_Pregunta.getText()));
                txt_LAE_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.#"));

                x = bod.CalcularLVU(Double.parseDouble(txt_LCO_Pregunta.getText()));
                txt_LVU_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.#"));

                x = bod.CalcularLTH(Double.parseDouble(txt_LVU_Pregunta.getText()));
                txt_LTH_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.#"));

                x = bod.CalcularLVR(Double.parseDouble(txt_LTH_Pregunta.getText()));
                txt_LVR_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.#"));

                calcularLAS();
                calcularLAI();
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

        if (!bod.setLCO(bod.CalcularLCO(0), bod.minLCO, bod.maxLCO)) {// Carga Orgánica Volumétrica (COV LA) g/m³*Día 
            lbl_save_error2.setText("Punto 1, Carga Orgánica Volumétrica, Se deben corregir los datos anteriores dependientes");
            lbl_save_error.setText(bod.errorLCO);
            return false;
        }

        if (!bod.setLCE(bod.CalcularLCE(0), bod.minLCE, bod.maxLCE)) {// Eficiencia de DBO5 estimada %LA Calculo de Eficiencia y Efluente de DBO5
            lbl_save_error2.setText("Punto 2, Eficiencia de DBO5 estimada, Se deben corregir los datos anteriores dependientes");
            lbl_save_error.setText(bod.errorLCE);
            return false;
        }

        //en este punto el dato bod LCE ya existe, por esto calcular...(0)
        if (!bod.setLAE(bod.CalcularLAE(0), bod.minLAE, bod.maxLAE)) {// DBO5 en el efluente de la laguna Anaerobia (SLA) mg/L  
            lbl_save_error2.setText("Punto 3, DBO5 en el efluente de la laguna Anaerobia, Se deben corregir los datos anteriores dependientes");
            lbl_save_error.setText(bod.errorLAE);
            return false;
        }

        //en este punto el dato bod LCO ya existe, por esto calcular...(0)
        if (!bod.setLVU(bod.CalcularLVU(0), bod.minLVU, bod.maxLVU)) {// Volumen Útil (VLA) m³ 
            lbl_save_error2.setText("Punto 4, Volumen Útil, Se deben corregir los datos anteriores dependientes");
            lbl_save_error.setText(bod.errorLVU);
            return false;
        }

        //en este punto el dato bod LVU ya existe, por esto calcular...(0)        
        if (!bod.setLTH(bod.CalcularLTH(0), bod.minLTH, bod.maxLTH)) {// Tiempo de retención hidráulico (TRLA) días 
            lbl_save_error2.setText("Punto 5, Tiempo de retención hidráulico, Se deben corregir los datos anteriores dependientes");
            lbl_save_error.setText(bod.errorLTH);
            return false;
        }

        //en este punto el dato bod LTH ya existe, por esto calcular...(0)
        if (!bod.setLVR(bod.CalcularLVR(0), bod.minLVR, bod.maxLVR)) {// Volumen Útil Recalculado (VLA) m³ 
            lbl_save_error2.setText("Punto 6, Volumen Útil Recalculado, Se deben corregir los datos anteriores dependientes");
            lbl_save_error.setText(bod.errorLVR);
            return false;
        }

        if (!bod.setLAU(txt_LAU_Pregunta.getText(), bod.minLAU, bod.maxLAU)) {// Altura Útil (hLA) m Calculo de Área Superficial 
            lbl_save_error2.setText("Punto 7, Altura Útil");
            lbl_save_error.setText(bod.errorLAU);
            return false;
        }

        //en este punto el dato bod LAU y LVR ya existen, por esto calcular...(0,0)
        if (!bod.setLAS(bod.CalcularLAS(0, 0), bod.minLAS, bod.maxLAS)) {// Área Superficial m²
            lbl_save_error2.setText("Punto 8, Área Superficial, Se deben corregir los datos dependientes");
            lbl_save_error.setText(bod.errorLAS);
            return false;
        }

        if (!rbtn_LLA_Pregunta1.isSelected() && !rbtn_LLA_Pregunta2.isSelected()) {// 1:3 2:3 Relación Largo: Ancho
            lbl_save_error2.setText("Punto 9, Debe escoger una opción Relación Largo: Ancho ");
            return false;
        }

        if (rbtn_LLA_Pregunta1.isSelected()) {// 1:3

            bod.setLLA(1);

            //en este punto el dato bod LAS ya existe, por esto calcular...(0)
            if (!bod.setLAA(bod.CalcularLAA_1_3(0), bod.minLAA, bod.maxLAA)) {// Ancho (aLA) m
                lbl_save_error2.setText("Punto 10, Ancho, Se deben corregir los datos dependientes");
                lbl_save_error.setText(bod.errorLAA);
                return false;
            }

            //en este punto el dato bod LAA ya existe, por esto calcular...(0)
            if (!bod.setLAL(bod.CalcularLAL_1_3(0), bod.minLAL, bod.maxLAL)) {// Largo (LLA) m
                lbl_save_error2.setText("Punto 11, Largo, Se deben corregir los datos dependientes");
                lbl_save_error.setText(bod.errorLAL);
                return false;
            }
        } else {//rbtn_LLA_Pregunta2.isSelected() 2:3

            bod.setLLA(0);

            //en este punto el dato bod LAS ya existe, por esto calcular...(0)
            if (!bod.setLAA(bod.CalcularLAA_2_3(0), bod.minLAA, bod.maxLAA)) {// Ancho (aLA) m
                lbl_save_error2.setText("Punto 10, Ancho, Se deben corregir los datos dependientes");
                lbl_save_error.setText(bod.errorLAA);
                return false;
            }

            //en este punto el dato bod LAA ya existe, por esto calcular...(0)
            if (!bod.setLAL(bod.CalcularLAL_2_3(0), bod.minLAL, bod.maxLAL)) {// Largo (LLA) m
                lbl_save_error2.setText("Punto 11, Largo, Se deben corregir los datos dependientes");
                lbl_save_error.setText(bod.errorLAL);
                return false;
            }
        }

        if (!bod.setLAP(txt_LAP_Pregunta.getText(), bod.minLAP, bod.maxLAP)) {// Pendiente n
            lbl_save_error2.setText("Punto 12, Pendiente n");
            lbl_save_error.setText(bod.errorLAP);
            return false;
        }

        //en este punto el dato bod LAP ya existe, por esto calcular...(0)
        if (!bod.setLAI(bod.calcularLAI(0), bod.minLAI, bod.maxLAI)) {// Ángulo de inclinación de la pendiente α
            lbl_save_error2.setText("Punto 13, Ángulo de inclinación de la pendiente α");
            lbl_save_error.setText(bod.errorLAI);
            return false;
        }

        //en este punto el dato bod LAS ya existe, por esto calcular...(0)
        if (!bod.setLAB(bod.calcularLAB(0), bod.minLAB, bod.maxLAB)) {// Ángulo de inclinación de la pendiente α
            lbl_save_error2.setText("Punto 13, Ángulo de inclinación de la pendiente α");
            lbl_save_error.setText(bod.errorLAB);
            return false;
        }

        bod.EditLagunaAnaerobia = true;

        if (!bod.GuardarUpdateBod()) {
            bod.EditLagunaAnaerobia = false;
            return false;
        }
        
        return true;
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
     * Esto se hace para calcular automática y temporalmente LAS ya que debe
     * aparecer cuando el usuario ingrese LAU y exista LVR
     */
    private void calcularLAS() {

        String x = txt_LAU_Pregunta.getText().trim();
        if (validar.EsDobleEntre(x, bod.minLAU, bod.maxLAU)) {
            x = bod.CalcularLAS(Double.parseDouble(x), Double.parseDouble(txt_LVR_Pregunta.getText().trim()));
            txt_LAS_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.#"));
        } else {
            txt_LAS_Pregunta.setText("");
        }
    }

    /**
     * Esto se hace para calcular automática y temporalmente LAA y LAL ya que
     * deben aparecer cuando el usuario checkee LLA
     */
    private void calcularLAALAL() {

        if (rbtn_LLA_Pregunta1.isSelected()) { // 1:3

            String x = txt_LAS_Pregunta.getText().trim();

            if (!x.equals("")) {

                x = bod.CalcularLAA_1_3(Double.parseDouble(x));
                txt_LAA_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.#"));

                x = bod.CalcularLAL_1_3(Double.parseDouble(x));
                txt_LAL_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.#"));
            } else {
                txt_LAA_Pregunta.setText("");
                txt_LAL_Pregunta.setText("");
            }
        }

        if (rbtn_LLA_Pregunta2.isSelected()) { // 2:3

            String x = txt_LAS_Pregunta.getText().trim();

            if (!x.equals("")) {

                x = bod.CalcularLAA_2_3(Double.parseDouble(x));
                txt_LAA_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.#"));

                x = bod.CalcularLAL_2_3(Double.parseDouble(x));
                txt_LAL_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.#"));
            } else {
                txt_LAA_Pregunta.setText("");
                txt_LAL_Pregunta.setText("");
            }
        }
    }

    private void calcularLAI() {

        String x = txt_LAP_Pregunta.getText().trim();
        if (validar.EsEnteroEntre(x, bod.minLAP, bod.maxLAP)) {
            x = bod.calcularLAI(Integer.parseInt(x));
            txt_LAI_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.#"));
        } else {
            txt_LAI_Pregunta.setText("");
        }
    }

    private void calcularLAB() {

        String x = txt_LAS_Pregunta.getText().trim();
        if (validar.EsDobleEntre(x, bod.minLAS, bod.maxLAS)) {
            x = bod.calcularLAB(Double.parseDouble(x));
            txt_LAB_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.#"));
        } else {
            txt_LAB_Pregunta.setText("");
        }
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
                calcularLAALAL();
                calcularLAB();//Sucede cuando el àrea superficial LAS es calculada, se debe calcular temporalmente LAB Borde libre
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jsp_LagunaAnaerobia = new javax.swing.JScrollPane();
        jp_Componentes = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        lbl_titulo1 = new javax.swing.JLabel();
        lbl_titulo2 = new javax.swing.JLabel();
        btn_guardar = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        txt_Q2C_Pregunta = new javax.swing.JTextField();
        lbl_Q2C_titulo1 = new javax.swing.JLabel();
        lbl_Q2C_titulo2 = new javax.swing.JLabel();
        lbl_CAB_titulo1 = new javax.swing.JLabel();
        txt_CAB_Pregunta = new javax.swing.JTextField();
        lbl_CAB_titulo2 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        btn_close = new javax.swing.JButton();
        lbl_save_error = new javax.swing.JLabel();
        lbl_save_error2 = new javax.swing.JLabel();
        lbl_CAT_titulo1 = new javax.swing.JLabel();
        txt_CAT_Pregunta = new javax.swing.JTextField();
        lbl_CAT_titulo2 = new javax.swing.JLabel();
        txt_LCO_Pregunta = new javax.swing.JTextField();
        lbl_LCO_titulo1 = new javax.swing.JLabel();
        lbl_LCO_titulo2 = new javax.swing.JLabel();
        lbl_LCE_error = new javax.swing.JLabel();
        lbl_LCE_titulo2 = new javax.swing.JLabel();
        txt_LCE_Pregunta = new javax.swing.JTextField();
        lbl_LCE_titulo1 = new javax.swing.JLabel();
        lbl_LCE_desc = new javax.swing.JLabel();
        lbl_LAE_error = new javax.swing.JLabel();
        lbl_LAE_titulo2 = new javax.swing.JLabel();
        txt_LAE_Pregunta = new javax.swing.JTextField();
        lbl_LAE_titulo1 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        txt_LVR_Pregunta = new javax.swing.JTextField();
        lbl_LVR_titulo1 = new javax.swing.JLabel();
        lbl_LAU_error = new javax.swing.JLabel();
        lbl_LAU_titulo2 = new javax.swing.JLabel();
        lbl_LVR_error = new javax.swing.JLabel();
        txt_LAU_Pregunta = new javax.swing.JTextField();
        lbl_LAU_titulo1 = new javax.swing.JLabel();
        lbl_LVR_titulo2 = new javax.swing.JLabel();
        lbl_LVU_titulo1 = new javax.swing.JLabel();
        lbl_LVU_titulo2 = new javax.swing.JLabel();
        txt_LVU_Pregunta = new javax.swing.JTextField();
        lbl_LTH_titulo1 = new javax.swing.JLabel();
        txt_LTH_Pregunta = new javax.swing.JTextField();
        lbl_LTH_titulo2 = new javax.swing.JLabel();
        lbl_LVU_error = new javax.swing.JLabel();
        lbl_LTH_error = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        lbl_LLA_desc = new javax.swing.JLabel();
        rbtn_LLA_Pregunta2 = new javax.swing.JRadioButton();
        rbtn_LLA_Pregunta1 = new javax.swing.JRadioButton();
        lbl_LAA_titulo1 = new javax.swing.JLabel();
        lbl_LAA_error = new javax.swing.JLabel();
        lbl_LAA_titulo2 = new javax.swing.JLabel();
        txt_LAA_Pregunta = new javax.swing.JTextField();
        lbl_LAL_titulo1 = new javax.swing.JLabel();
        lbl_LAL_error = new javax.swing.JLabel();
        lbl_LAL_titulo2 = new javax.swing.JLabel();
        txt_LAL_Pregunta = new javax.swing.JTextField();
        lbl_LCO_error = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        lbl_LAS_titulo2 = new javax.swing.JLabel();
        lbl_LAS_error = new javax.swing.JLabel();
        lbl_LAS_titulo1 = new javax.swing.JLabel();
        txt_LAS_Pregunta = new javax.swing.JTextField();
        lbl_LAU_desc = new javax.swing.JLabel();
        txt_LAI_Pregunta = new javax.swing.JTextField();
        lbl_LAP_titulo1 = new javax.swing.JLabel();
        lbl_LAI_error = new javax.swing.JLabel();
        lbl_LAI_titulo2 = new javax.swing.JLabel();
        txt_LAP_Pregunta = new javax.swing.JTextField();
        lbl_LAI_titulo1 = new javax.swing.JLabel();
        lbl_LAP_error = new javax.swing.JLabel();
        lbl_LAP_titulo2 = new javax.swing.JLabel();
        lbl_LAB_titulo2 = new javax.swing.JLabel();
        lbl_LAB_error = new javax.swing.JLabel();
        lbl_LAB_titulo1 = new javax.swing.JLabel();
        txt_LAB_Pregunta = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        btn_LAB_ayuda = new javax.swing.JButton();
        btn_LCO_ayuda = new javax.swing.JButton();
        btn_LTH_ayuda = new javax.swing.JButton();
        btn_LAU_ayuda = new javax.swing.JButton();
        btn_LLA_ayuda = new javax.swing.JButton();
        btn_LAP_ayuda = new javax.swing.JButton();
        btn_LAI_ayuda = new javax.swing.JButton();
        btn_close2 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1024, 768));

        jsp_LagunaAnaerobia.setPreferredSize(new java.awt.Dimension(1024, 768));

        jp_Componentes.setPreferredSize(new java.awt.Dimension(1024, 1200));
        jp_Componentes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jp_Componentes.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 41, 1061, 2));

        lbl_titulo1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_titulo1.setForeground(new java.awt.Color(51, 51, 51));
        lbl_titulo1.setText("Titulo");
        jp_Componentes.add(lbl_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 300, 30));

        lbl_titulo2.setForeground(new java.awt.Color(0, 51, 102));
        lbl_titulo2.setText("Desc");
        jp_Componentes.add(lbl_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(337, 2, 600, 30));

        btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Button_Salva_Guardar_21.png"))); // NOI18N
        btn_guardar.setText("save");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 950, 120, -1));
        jp_Componentes.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 1071, 2));

        txt_Q2C_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q2C_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 50, 130, 25));

        lbl_Q2C_titulo1.setText("Titulo");
        lbl_Q2C_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2C_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 300, 30));

        lbl_Q2C_titulo2.setText("Titulo");
        lbl_Q2C_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2C_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 50, 80, 25));

        lbl_CAB_titulo1.setText("Titulo");
        lbl_CAB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAB_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 300, 30));

        txt_CAB_Pregunta.setEditable(false);
        jp_Componentes.add(txt_CAB_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 90, 130, 25));

        lbl_CAB_titulo2.setText("Titulo");
        lbl_CAB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAB_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 90, 80, 25));
        jp_Componentes.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 625, 1071, 2));

        btn_close.setText("Salir");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 990, 120, 30));

        lbl_save_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error.setText(" . . .");
        jp_Componentes.add(lbl_save_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 950, 400, 35));

        lbl_save_error2.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error2.setText(" . . .");
        jp_Componentes.add(lbl_save_error2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 990, 400, 35));

        lbl_CAT_titulo1.setText("Titulo");
        lbl_CAT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 300, 30));

        txt_CAT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_CAT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 130, 130, 25));

        lbl_CAT_titulo2.setText("Titulo");
        lbl_CAT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 130, 80, 25));

        txt_LCO_Pregunta.setEditable(false);
        jp_Componentes.add(txt_LCO_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 170, 130, 25));

        lbl_LCO_titulo1.setText("Titulo");
        lbl_LCO_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LCO_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 300, 30));

        lbl_LCO_titulo2.setText("Titulo");
        lbl_LCO_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LCO_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 170, 80, 25));

        lbl_LCE_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_LCE_error.setText(" . . .");
        jp_Componentes.add(lbl_LCE_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 260, 340, 35));

        lbl_LCE_titulo2.setText("Titulo");
        lbl_LCE_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LCE_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 270, 80, 25));

        txt_LCE_Pregunta.setEditable(false);
        jp_Componentes.add(txt_LCE_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 270, 130, 25));

        lbl_LCE_titulo1.setText("Titulo");
        lbl_LCE_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LCE_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 300, 30));

        lbl_LCE_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_LCE_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_LCE_desc.setText("Desc");
        lbl_LCE_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LCE_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 600, 30));

        lbl_LAE_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_LAE_error.setText(" . . .");
        jp_Componentes.add(lbl_LAE_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 300, 340, 35));

        lbl_LAE_titulo2.setText("Titulo");
        lbl_LAE_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LAE_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 310, 80, 25));

        txt_LAE_Pregunta.setEditable(false);
        jp_Componentes.add(txt_LAE_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 310, 130, 25));

        lbl_LAE_titulo1.setText("Titulo");
        lbl_LAE_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LAE_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 300, 30));
        jp_Componentes.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 355, 1071, 2));

        txt_LVR_Pregunta.setEditable(false);
        jp_Componentes.add(txt_LVR_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 450, 130, 25));

        lbl_LVR_titulo1.setText("Titulo");
        lbl_LVR_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LVR_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, 300, 30));

        lbl_LAU_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_LAU_error.setText(" . . .");
        jp_Componentes.add(lbl_LAU_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 530, 340, 35));

        lbl_LAU_titulo2.setText("Titulo");
        lbl_LAU_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LAU_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 540, 80, 25));

        lbl_LVR_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_LVR_error.setText(" . . .");
        jp_Componentes.add(lbl_LVR_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 440, 340, 35));

        txt_LAU_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_LAU_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_LAU_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 540, 130, 25));

        lbl_LAU_titulo1.setText("Titulo");
        lbl_LAU_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LAU_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 540, 300, 30));

        lbl_LVR_titulo2.setText("Titulo");
        lbl_LVR_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LVR_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 450, 80, 25));

        lbl_LVU_titulo1.setText("Titulo");
        lbl_LVU_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LVU_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 300, 30));

        lbl_LVU_titulo2.setText("Titulo");
        lbl_LVU_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LVU_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 370, 80, 25));

        txt_LVU_Pregunta.setEditable(false);
        jp_Componentes.add(txt_LVU_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 370, 130, 25));

        lbl_LTH_titulo1.setText("Titulo");
        lbl_LTH_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LTH_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 300, 30));

        txt_LTH_Pregunta.setEditable(false);
        jp_Componentes.add(txt_LTH_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 410, 130, 25));

        lbl_LTH_titulo2.setText("Titulo");
        lbl_LTH_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LTH_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 410, 80, 25));

        lbl_LVU_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_LVU_error.setText(" . . .");
        jp_Componentes.add(lbl_LVU_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 360, 340, 35));

        lbl_LTH_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_LTH_error.setText(" . . .");
        jp_Componentes.add(lbl_LTH_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 400, 340, 35));
        jp_Componentes.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 786, 1071, 2));

        lbl_LLA_desc.setText("Desc");
        lbl_LLA_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LLA_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 630, 600, 30));

        rbtn_LLA_Pregunta2.setText("Titulo");
        jp_Componentes.add(rbtn_LLA_Pregunta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 670, 80, 25));

        rbtn_LLA_Pregunta1.setText("Titulo");
        jp_Componentes.add(rbtn_LLA_Pregunta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 670, 80, 25));

        lbl_LAA_titulo1.setText("Titulo");
        lbl_LAA_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LAA_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 700, 300, 30));

        lbl_LAA_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_LAA_error.setText(" . . .");
        jp_Componentes.add(lbl_LAA_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 690, 340, 35));

        lbl_LAA_titulo2.setText("Titulo");
        lbl_LAA_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LAA_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 700, 80, 25));

        txt_LAA_Pregunta.setEditable(false);
        jp_Componentes.add(txt_LAA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 700, 130, 25));

        lbl_LAL_titulo1.setText("Titulo");
        lbl_LAL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LAL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 740, 300, 30));

        lbl_LAL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_LAL_error.setText(" . . .");
        jp_Componentes.add(lbl_LAL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 730, 340, 35));

        lbl_LAL_titulo2.setText("Titulo");
        lbl_LAL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LAL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 740, 80, 25));

        txt_LAL_Pregunta.setEditable(false);
        jp_Componentes.add(txt_LAL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 740, 130, 25));

        lbl_LCO_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_LCO_error.setText(" . . .");
        jp_Componentes.add(lbl_LCO_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 170, 340, 35));
        jp_Componentes.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 496, 1071, 2));

        lbl_LAS_titulo2.setText("Titulo");
        lbl_LAS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LAS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 580, 80, 25));

        lbl_LAS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_LAS_error.setText(" . . .");
        jp_Componentes.add(lbl_LAS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 570, 340, 35));

        lbl_LAS_titulo1.setText("Titulo");
        lbl_LAS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LAS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 580, 300, 30));

        txt_LAS_Pregunta.setEditable(false);
        jp_Componentes.add(txt_LAS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 580, 130, 25));

        lbl_LAU_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_LAU_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_LAU_desc.setText("Desc");
        lbl_LAU_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LAU_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 500, 600, 30));

        txt_LAI_Pregunta.setEditable(false);
        jp_Componentes.add(txt_LAI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 840, 130, 25));

        lbl_LAP_titulo1.setText("Titulo");
        lbl_LAP_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LAP_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 800, 300, 30));

        lbl_LAI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_LAI_error.setText(" . . .");
        jp_Componentes.add(lbl_LAI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 830, 340, 35));

        lbl_LAI_titulo2.setText("Titulo");
        lbl_LAI_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LAI_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 840, 80, 25));

        txt_LAP_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_LAP_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_LAP_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 800, 130, 25));

        lbl_LAI_titulo1.setText("Titulo");
        lbl_LAI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LAI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 840, 300, 30));

        lbl_LAP_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_LAP_error.setText(" . . .");
        jp_Componentes.add(lbl_LAP_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 790, 340, 35));

        lbl_LAP_titulo2.setText("Titulo");
        lbl_LAP_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LAP_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 800, 80, 25));

        lbl_LAB_titulo2.setText("Titulo");
        lbl_LAB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LAB_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 880, 80, 25));

        lbl_LAB_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_LAB_error.setText(" . . .");
        jp_Componentes.add(lbl_LAB_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 870, 340, 35));

        lbl_LAB_titulo1.setText("Titulo");
        lbl_LAB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_LAB_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 880, 300, 30));

        txt_LAB_Pregunta.setEditable(false);
        jp_Componentes.add(txt_LAB_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 880, 130, 25));
        jp_Componentes.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 930, 1071, -1));

        btn_LAB_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_LAB_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_LAB_ayuda.setContentAreaFilled(false);
        btn_LAB_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_LAB_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 880, 25, 25));

        btn_LCO_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_LCO_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_LCO_ayuda.setContentAreaFilled(false);
        btn_LCO_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_LCO_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 170, 25, 25));

        btn_LTH_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_LTH_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_LTH_ayuda.setContentAreaFilled(false);
        btn_LTH_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_LTH_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 410, 25, 25));

        btn_LAU_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_LAU_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_LAU_ayuda.setContentAreaFilled(false);
        btn_LAU_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_LAU_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 540, 25, 25));

        btn_LLA_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_LLA_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_LLA_ayuda.setContentAreaFilled(false);
        btn_LLA_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_LLA_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 640, 25, 25));

        btn_LAP_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_LAP_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_LAP_ayuda.setContentAreaFilled(false);
        btn_LAP_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_LAP_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 800, 25, 25));

        btn_LAI_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_LAI_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_LAI_ayuda.setContentAreaFilled(false);
        btn_LAI_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_LAI_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 840, 25, 25));

        jsp_LagunaAnaerobia.setViewportView(jp_Componentes);

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
            .addComponent(jsp_LagunaAnaerobia, javax.swing.GroupLayout.DEFAULT_SIZE, 1008, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_close2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(btn_close2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jsp_LagunaAnaerobia, javax.swing.GroupLayout.DEFAULT_SIZE, 1234, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void btn_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_closeActionPerformed
        bod.WinLagunaAnaerobia = false;
        this.dispose();
    }//GEN-LAST:event_btn_closeActionPerformed

    private void txt_LAU_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_LAU_PreguntaKeyReleased
        //Listener para calcular automática y temporalmente LAS
        calcularLAS();
    }//GEN-LAST:event_txt_LAU_PreguntaKeyReleased

    private void txt_LAP_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_LAP_PreguntaKeyReleased
        //Listener para calcular automática y temporalmente LAI
        calcularLAI();
    }//GEN-LAST:event_txt_LAP_PreguntaKeyReleased

    private void btn_close2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_close2ActionPerformed
        bod.WinLagunaAnaerobia = false;
        this.dispose();
    }//GEN-LAST:event_btn_close2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_LAB_ayuda;
    private javax.swing.JButton btn_LAI_ayuda;
    private javax.swing.JButton btn_LAP_ayuda;
    private javax.swing.JButton btn_LAU_ayuda;
    private javax.swing.JButton btn_LCO_ayuda;
    private javax.swing.JButton btn_LLA_ayuda;
    private javax.swing.JButton btn_LTH_ayuda;
    private javax.swing.JButton btn_close;
    private javax.swing.JButton btn_close2;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JPanel jp_Componentes;
    private javax.swing.JScrollPane jsp_LagunaAnaerobia;
    private javax.swing.JLabel lbl_CAB_titulo1;
    private javax.swing.JLabel lbl_CAB_titulo2;
    private javax.swing.JLabel lbl_CAT_titulo1;
    private javax.swing.JLabel lbl_CAT_titulo2;
    private javax.swing.JLabel lbl_LAA_error;
    private javax.swing.JLabel lbl_LAA_titulo1;
    private javax.swing.JLabel lbl_LAA_titulo2;
    private javax.swing.JLabel lbl_LAB_error;
    private javax.swing.JLabel lbl_LAB_titulo1;
    private javax.swing.JLabel lbl_LAB_titulo2;
    private javax.swing.JLabel lbl_LAE_error;
    private javax.swing.JLabel lbl_LAE_titulo1;
    private javax.swing.JLabel lbl_LAE_titulo2;
    private javax.swing.JLabel lbl_LAI_error;
    private javax.swing.JLabel lbl_LAI_titulo1;
    private javax.swing.JLabel lbl_LAI_titulo2;
    private javax.swing.JLabel lbl_LAL_error;
    private javax.swing.JLabel lbl_LAL_titulo1;
    private javax.swing.JLabel lbl_LAL_titulo2;
    private javax.swing.JLabel lbl_LAP_error;
    private javax.swing.JLabel lbl_LAP_titulo1;
    private javax.swing.JLabel lbl_LAP_titulo2;
    private javax.swing.JLabel lbl_LAS_error;
    private javax.swing.JLabel lbl_LAS_titulo1;
    private javax.swing.JLabel lbl_LAS_titulo2;
    private javax.swing.JLabel lbl_LAU_desc;
    private javax.swing.JLabel lbl_LAU_error;
    private javax.swing.JLabel lbl_LAU_titulo1;
    private javax.swing.JLabel lbl_LAU_titulo2;
    private javax.swing.JLabel lbl_LCE_desc;
    private javax.swing.JLabel lbl_LCE_error;
    private javax.swing.JLabel lbl_LCE_titulo1;
    private javax.swing.JLabel lbl_LCE_titulo2;
    private javax.swing.JLabel lbl_LCO_error;
    private javax.swing.JLabel lbl_LCO_titulo1;
    private javax.swing.JLabel lbl_LCO_titulo2;
    private javax.swing.JLabel lbl_LLA_desc;
    private javax.swing.JLabel lbl_LTH_error;
    private javax.swing.JLabel lbl_LTH_titulo1;
    private javax.swing.JLabel lbl_LTH_titulo2;
    private javax.swing.JLabel lbl_LVR_error;
    private javax.swing.JLabel lbl_LVR_titulo1;
    private javax.swing.JLabel lbl_LVR_titulo2;
    private javax.swing.JLabel lbl_LVU_error;
    private javax.swing.JLabel lbl_LVU_titulo1;
    private javax.swing.JLabel lbl_LVU_titulo2;
    private javax.swing.JLabel lbl_Q2C_titulo1;
    private javax.swing.JLabel lbl_Q2C_titulo2;
    private javax.swing.JLabel lbl_save_error;
    private javax.swing.JLabel lbl_save_error2;
    private javax.swing.JLabel lbl_titulo1;
    private javax.swing.JLabel lbl_titulo2;
    private javax.swing.JRadioButton rbtn_LLA_Pregunta1;
    private javax.swing.JRadioButton rbtn_LLA_Pregunta2;
    private javax.swing.JTextField txt_CAB_Pregunta;
    private javax.swing.JTextField txt_CAT_Pregunta;
    private javax.swing.JTextField txt_LAA_Pregunta;
    private javax.swing.JTextField txt_LAB_Pregunta;
    private javax.swing.JTextField txt_LAE_Pregunta;
    private javax.swing.JTextField txt_LAI_Pregunta;
    private javax.swing.JTextField txt_LAL_Pregunta;
    private javax.swing.JTextField txt_LAP_Pregunta;
    private javax.swing.JTextField txt_LAS_Pregunta;
    private javax.swing.JTextField txt_LAU_Pregunta;
    private javax.swing.JTextField txt_LCE_Pregunta;
    private javax.swing.JTextField txt_LCO_Pregunta;
    private javax.swing.JTextField txt_LTH_Pregunta;
    private javax.swing.JTextField txt_LVR_Pregunta;
    private javax.swing.JTextField txt_LVU_Pregunta;
    private javax.swing.JTextField txt_Q2C_Pregunta;
    // End of variables declaration//GEN-END:variables
}
