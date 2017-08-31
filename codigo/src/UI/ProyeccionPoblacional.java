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

public class ProyeccionPoblacional extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("ProyeccionPoblacional"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.ObtenerKey("rutabasedatos") + File.separator + conf.ObtenerKey("nombrebasedatos"));
    private Validaciones validar = new Validaciones();
    private Listener_Popup Lpopup;
    private Listener_Texto Ltexto;
    private Bod bod = new Bod("");
    boolean flag_MEA = false; //Guarda el estado del radiobutton
    Util util = new Util();

    public ProyeccionPoblacional(Bod bod) {
        this.bod = bod;
        initComponents();
        bod.WinProyeccionPoblacional = true;
        InicializarComponentes();
    }

    /**
     * Inicializa todos los elementos (jlabels,jtextbox,etc.) con sus
     * respectivos valores y textos traidos desde al base de datos.
     */
    public void InicializarComponentes() {

        ResultSet rs;
        ButtonGroup buttongroup = new ButtonGroup();
        lbl_.setVisible(false);

        try {
            btn_Guardar.setText("Guardar");
            btn_close.setText("Cerrar");
            //- - - - - - - - - - - - - - - - - - - Titulo de la sección- - - - - - - - - - - - - - - - - - - - - - - - - - - 
            rs = Resultado("obtenerseccion", "2", null);

            lbl_titulo1_ProyeccionPoblacional.setText(rs.getString("Nombre"));
            lbl_titulo2_ProyeccionPoblacional.setText(rs.getString("Mensaje"));
            // - - - - - - Pregunta 01 Cuestionario 02 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MEA
            rs = Resultado("obtenerpregunta", "2", "MEA");

            buttongroup.add(rbtn_MEA_Pregunta);
            lbl_MEA_desc.setText(rs.getString("descripcion"));
            rbtn_MEA_Pregunta.setText(rs.getString("titulo1"));
            AsignarPopupBtn(btn_MEA_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 460, 390);

            rbtn_MEA_Pregunta.addActionListener(new ActionListener() { //Listener para el radiobutton
                @Override
                public void actionPerformed(ActionEvent e) {
                    flag_MEA = true;
                    ComprobarAritGeometrico();
                }
            });
            // - - - - - - Pregunta 02 Cuestionario 02 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MEG 
            rs = Resultado("obtenerpregunta", "2", "MEG");

            buttongroup.add(rbtn_MEG_Pregunta);
            rbtn_MEG_Pregunta.setText(rs.getString("titulo1"));
            AsignarPopupBtn(btn_MEG_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 460, 390);

            rbtn_MEG_Pregunta.addActionListener(new ActionListener() { //Listener para el radiobutton
                @Override
                public void actionPerformed(ActionEvent e) {
                    flag_MEA = false;
                    ComprobarAritGeometrico();
                }
            });
            // - - - - - - Pregunta 03 Cuestionario 02 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - TCA 
            rs = Resultado("obtenerpregunta", "2", "TCA");

            bod.minTCA = rs.getDouble("rangomin");
            bod.maxTCA = rs.getDouble("rangomax");
            bod.errorTCA = rs.getString("errormsg");
            lbl_TCA_TCG_titulo1.setText(rs.getString("titulo1"));//El titulo es el mismo para TCG
            lbl_TCA_TCG_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 04 Cuestionario 02 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - TCG 
            rs = Resultado("obtenerpregunta", "2", "TCG");

            bod.minTCG = rs.getDouble("rangomin");
            bod.maxTCG = rs.getDouble("rangomax");
            bod.errorTCG = rs.getString("errormsg");
            // - - - - - - Pregunta 05 Cuestionario 02 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PPA 
            rs = Resultado("obtenerpregunta", "2", "PPA");

            bod.minPPA = rs.getInt("rangomin");
            bod.maxPPA = rs.getInt("rangomax");
            bod.errorPPA = rs.getString("errormsg");
            lbl_PPA_PPG_titulo1.setText(rs.getString("titulo1"));
            lbl_PPA_PPG_titulo2.setText(rs.getString("titulo2"));
            AsignarPopupBtn(btn_PPA_PPG_ayuda1, rs.getString("ayuda"), rs.getString("ayudalink"), 390, 460); //Este serà el mensaje al escoger la primera opcion "Método Aritmético"
            btn_PPA_PPG_ayuda1.setVisible(false);
            // - - - - - - Pregunta 06 Cuestionario 02 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PPG 
            rs = Resultado("obtenerpregunta", "2", "PPG");

            bod.minPPG = rs.getInt("rangomin");
            bod.maxPPG = rs.getInt("rangomax");
            bod.errorPPG = rs.getString("errormsg");
            AsignarPopupBtn(btn_PPA_PPG_ayuda2, rs.getString("ayuda"), rs.getString("ayudalink"), 390, 450); //Este serà el mensaje al escoger la primera opcion "Método Geométrico"
            btn_PPA_PPG_ayuda2.setVisible(false);
            // - - - - - - Pregunta 07 Cuestionario 02 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NCR 
            rs = Resultado("obtenerpregunta", "2", "NCR");

            lbl_NCR_titulo1.setText(rs.getString("titulo1"));
            lbl_NCR_titulo2.setText(rs.getString("titulo2"));
            bod.errorNCR = rs.getString("errormsg");
            AsignarPopupBtn(btn_NCR_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 380);

            // - - - - - - - - - - - - - - - - - - - - -  - - - - - - - - - - - - - - - - - - - - - - - - - - - - Si existen datos traidos desde la BD:  

            if (bod.EditProyeccionPoblacional) {
                if (bod.getMEA() > 0) {
                    rbtn_MEA_Pregunta.setSelected(true);
                    flag_MEA = true;
                    ComprobarAritGeometrico();
                }
                if (bod.getMEG() > 0) {
                    rbtn_MEG_Pregunta.setSelected(true);
                    flag_MEA = false;
                    ComprobarAritGeometrico();
                }
                txt_NCR_Pregunta.setText(bod.getNCR());
            }

        } catch (Exception ex) {
            log.error("Error en InicializarComponentes " + ex.getMessage());
            ErrorGeneral(); // Ha ocurrido algun error en los datos o conexión, se debe revisar log.
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }

    private ResultSet Resultado(String consulta, String param1, String param2) throws Exception {

        String select = conf.ObtenerKey(consulta);

        switch (consulta) {
            case "obtenerseccion":
                select = String.format(select, param1); //Cuestionario 1, pregunta 2
                break;
            case "obtenerpregunta":
                select = String.format(select, param1, param2); //Cuestionario 1, pregunta 1,2,3,....
                break;
        }

        ResultSet result = db.SelectSql(select);
        if (result == null) {
            throw new Exception("Error al traer Resultado = null," + consulta + "," + param1 + "," + param2);
        }
        return result;
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

    private void AsignarTextEntero(JTextField jtxtf, int min, int max, JLabel jlbl, String ErrorMsg) {
        Ltexto = new Listener_Texto();
        Ltexto.AgregarAlertaTextoEntero(jtxtf, min, max, jlbl, ErrorMsg);
    }

    private boolean Guardar() {

        lbl_save_error.setText("");
        lbl_save_error2.setText("");

        if (!rbtn_MEA_Pregunta.isSelected() && !rbtn_MEG_Pregunta.isSelected()) {
            lbl_save_error2.setText("Punto 1, debe escoger un Método");
            return false;
        }

        if (rbtn_MEA_Pregunta.isSelected()) {
            bod.setMEA(1);
            bod.setMEG(0);

            if (!bod.setTCA(bod.CalcularTCA(), bod.minTCA, bod.maxTCA)) {
                lbl_TCA_TCG_error.setText(bod.errorTCA);
                lbl_save_error2.setText("Punto 2, Se deben corregir los datos anteriores");
                return false;
            }

            double temp = (int) Double.parseDouble(bod.CalcularPPA());
            if (!bod.setPPA(validar.DobleAcadena(temp), bod.minPPA, bod.maxPPA)) {
                lbl_PPA_PPG_error.setText(bod.errorPPA);
                lbl_save_error2.setText("Punto 2, Se deben corregir los datos anteriores");
                return false;
            }

            if (!bod.setNCR(bod.CalcularNCR(bod.getPPA()))) {
                lbl_save_error2.setText("Punto 3, Se deben corregir los datos anteriores");
                lbl_NCR_error.setText(bod.errorNCR);
                return false;
            }
        }

        if (rbtn_MEG_Pregunta.isSelected()) {
            bod.setMEA(0);
            bod.setMEG(1);

            if (!bod.setTCG(bod.CalcularTCG(), bod.minTCG, bod.maxTCG)) {
                lbl_TCA_TCG_error.setText(bod.errorTCG);
                lbl_save_error2.setText("Punto 2, Se deben corregir los datos anteriores");
                return false;
            }

            double temp = (int) Double.parseDouble(bod.CalcularPPG());
            if (!bod.setPPG(validar.DobleAcadena(temp), bod.minPPG, bod.maxPPG)) {
                lbl_PPA_PPG_error.setText(bod.errorPPG);
                lbl_save_error2.setText("Punto 2, Se deben corregir los datos anteriores");
                return false;
            }

            if (!bod.setNCR(bod.CalcularNCR(bod.getPPG()))) {
                lbl_save_error2.setText("Punto 3, Se deben corregir los datos anteriores");
                lbl_NCR_error.setText(bod.errorNCR);
                return false;
            }
        }

        bod.EditProyeccionPoblacional = true;

        if (!bod.GuardarUpdateBod()) {
            bod.EditProyeccionPoblacional = false;
            return false;
        }

        Main main = (Main) this.getTopLevelAncestor(); // Trata de ir  al contenedor principal 
        main.ComprobarCambio(3);//Comprobar cambios que afecten datos posteriores, si este guardar es de una edición.

        return true;
    }

    private void ComprobarAritGeometrico() { //Todo: try cacth

        String tcag = null;
        int ppag = 0;
        String ncr = null;
        lbl_TCA_TCG_error.setText("");
        lbl_PPA_PPG_error.setText("");

        try {
            if (flag_MEA) { //Se ha checkeado el metodo aritmético
                btn_MEG_ayuda.setVisible(false);
                btn_MEA_ayuda.setVisible(true);
                tcag = bod.CalcularTCA();
                ppag = (int) Double.parseDouble(bod.CalcularPPA());
                ncr = bod.CalcularNCR(ppag);

                if (tcag != null && ppag != 0) {
                    txt_TCA_TCG_Pregunta.setText(validar.DobleFormatoStringCeil(Double.parseDouble(tcag), "#.###"));//validar.CadenaFormatDecimal(tcag, "0.00")
                    txt_PPA_PPG_Pregunta.setText(validar.DobleAcadena((double) ppag));

                    if (!validar.EsDobleEntre(tcag, bod.minTCA, bod.maxTCA)) {
                        lbl_TCA_TCG_error.setText(bod.errorTCA);
                    }

                    if (!validar.EsEnteroEntre(validar.DobleAcadena((double) ppag), bod.minPPA, bod.maxPPA)) {
                        lbl_PPA_PPG_error.setText(bod.errorPPA);
                    } else {
                        btn_PPA_PPG_ayuda1.setVisible(true);
                        btn_PPA_PPG_ayuda2.setVisible(false);
                        if (ncr != null) {
                            txt_NCR_Pregunta.setText(ncr);
                        }
                    }
                    return;
                }
            } else {
                btn_MEG_ayuda.setVisible(true);
                btn_MEA_ayuda.setVisible(false);
                tcag = bod.CalcularTCG();
                ppag = (int) Double.parseDouble(bod.CalcularPPG());
                ncr = bod.CalcularNCR(ppag);

                if (tcag != null && ppag != 0) {
                    txt_TCA_TCG_Pregunta.setText(validar.DobleFormatoStringCeil(Double.parseDouble(tcag), "#.###"));//validar.CadenaFormatDecimal(tcag, "0.00")
                    txt_PPA_PPG_Pregunta.setText(validar.DobleAcadena((double) ppag)); //todo

                    if (!validar.EsDobleEntre(tcag, bod.minTCG, bod.maxTCG)) {
                        lbl_TCA_TCG_error.setText(bod.errorTCG);
                    }

                    if (!validar.EsEnteroEntre(validar.DobleAcadena((double) ppag), bod.minPPG, bod.maxPPG)) {
                        lbl_PPA_PPG_error.setText(bod.errorPPG);
                    } else {
                        btn_PPA_PPG_ayuda1.setVisible(false);
                        btn_PPA_PPG_ayuda2.setVisible(true);
                        if (ncr != null) {
                            txt_NCR_Pregunta.setText(ncr);
                        }
                    }
                    return;
                }
                lbl_TCA_TCG_error.setText("Error al calcular.");
                lbl_PPA_PPG_error.setText("Error al calcular.");
            }
        } catch (Exception ex) {
            log.error("Error en ComprobarAritGeometrico() " + ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jsp_ProyeccionPoblacional = new javax.swing.JScrollPane();
        jp_Componentes = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        lbl_titulo1_ProyeccionPoblacional = new javax.swing.JLabel();
        lbl_titulo2_ProyeccionPoblacional = new javax.swing.JLabel();
        btn_Guardar = new javax.swing.JButton();
        rbtn_MEA_Pregunta = new javax.swing.JRadioButton();
        rbtn_MEG_Pregunta = new javax.swing.JRadioButton();
        lbl_MEA_desc = new javax.swing.JLabel();
        lbl_NCR_titulo1 = new javax.swing.JLabel();
        txt_NCR_Pregunta = new javax.swing.JTextField();
        lbl_NCR_titulo2 = new javax.swing.JLabel();
        lbl_NCR_error = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        lbl_TCA_TCG_titulo1 = new javax.swing.JLabel();
        txt_TCA_TCG_Pregunta = new javax.swing.JTextField();
        lbl_TCA_TCG_titulo2 = new javax.swing.JLabel();
        lbl_TCA_TCG_error = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        lbl_PPA_PPG_titulo2 = new javax.swing.JLabel();
        txt_PPA_PPG_Pregunta = new javax.swing.JTextField();
        lbl_PPA_PPG_error = new javax.swing.JLabel();
        lbl_PPA_PPG_titulo1 = new javax.swing.JLabel();
        btn_close = new javax.swing.JButton();
        lbl_save_error = new javax.swing.JLabel();
        lbl_save_error2 = new javax.swing.JLabel();
        btn_MEA_ayuda = new javax.swing.JButton();
        btn_MEG_ayuda = new javax.swing.JButton();
        btn_PPA_PPG_ayuda1 = new javax.swing.JButton();
        btn_PPA_PPG_ayuda2 = new javax.swing.JButton();
        lbl_ = new javax.swing.JLabel();
        btn_NCR_ayuda = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1024, 768));

        jsp_ProyeccionPoblacional.setPreferredSize(new java.awt.Dimension(1024, 768));

        jp_Componentes.setPreferredSize(new java.awt.Dimension(1024, 768));
        jp_Componentes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jp_Componentes.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 1024, -1));

        lbl_titulo1_ProyeccionPoblacional.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_titulo1_ProyeccionPoblacional.setForeground(new java.awt.Color(51, 51, 51));
        lbl_titulo1_ProyeccionPoblacional.setText("Titulo");
        jp_Componentes.add(lbl_titulo1_ProyeccionPoblacional, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 300, 30));

        lbl_titulo2_ProyeccionPoblacional.setForeground(new java.awt.Color(0, 51, 102));
        lbl_titulo2_ProyeccionPoblacional.setText("Desc");
        jp_Componentes.add(lbl_titulo2_ProyeccionPoblacional, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, 600, 30));

        btn_Guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Button_Salva_Guardar_21.png"))); // NOI18N
        btn_Guardar.setText("cerrar");
        btn_Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GuardarActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_Guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 120, -1));

        rbtn_MEA_Pregunta.setText("Titulo");
        jp_Componentes.add(rbtn_MEA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 300, 30));

        rbtn_MEG_Pregunta.setText("Titulo");
        jp_Componentes.add(rbtn_MEG_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 300, 30));

        lbl_MEA_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_MEA_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_MEA_desc.setText("Desc");
        lbl_MEA_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_MEA_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 610, 30));

        lbl_NCR_titulo1.setText("Titulo");
        lbl_NCR_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NCR_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 300, 30));

        txt_NCR_Pregunta.setEditable(false);
        jp_Componentes.add(txt_NCR_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 320, 130, 25));

        lbl_NCR_titulo2.setText("Titulo");
        lbl_NCR_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_NCR_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 320, 80, 25));

        lbl_NCR_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_NCR_error.setText("...");
        jp_Componentes.add(lbl_NCR_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 310, 340, 35));
        jp_Componentes.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 1024, 10));
        jp_Componentes.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 360, 1024, -1));

        lbl_TCA_TCG_titulo1.setText("Titulo");
        lbl_TCA_TCG_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_TCA_TCG_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 300, 30));

        txt_TCA_TCG_Pregunta.setEditable(false);
        jp_Componentes.add(txt_TCA_TCG_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 190, 130, 25));

        lbl_TCA_TCG_titulo2.setText("Titulo");
        lbl_TCA_TCG_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_TCA_TCG_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 190, 80, 25));

        lbl_TCA_TCG_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_TCA_TCG_error.setText("...");
        jp_Componentes.add(lbl_TCA_TCG_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 190, 340, 35));
        jp_Componentes.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 1024, -1));

        lbl_PPA_PPG_titulo2.setText("Titulo");
        lbl_PPA_PPG_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PPA_PPG_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 250, 80, 25));

        txt_PPA_PPG_Pregunta.setEditable(false);
        jp_Componentes.add(txt_PPA_PPG_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 250, 130, 25));

        lbl_PPA_PPG_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PPA_PPG_error.setText("...");
        jp_Componentes.add(lbl_PPA_PPG_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 240, 340, 35));

        lbl_PPA_PPG_titulo1.setText("Titulo");
        lbl_PPA_PPG_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PPA_PPG_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 300, 30));

        btn_close.setText("Salir");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, 120, 30));

        lbl_save_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error.setText(" . . .");
        jp_Componentes.add(lbl_save_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 390, 400, 35));

        lbl_save_error2.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error2.setText(" . . .");
        jp_Componentes.add(lbl_save_error2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 440, 400, 35));

        btn_MEA_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MEA_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MEA_ayuda.setContentAreaFilled(false);
        btn_MEA_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MEA_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 100, 25, 25));

        btn_MEG_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_MEG_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_MEG_ayuda.setContentAreaFilled(false);
        btn_MEG_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_MEG_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 140, 25, 25));

        btn_PPA_PPG_ayuda1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_PPA_PPG_ayuda1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_PPA_PPG_ayuda1.setContentAreaFilled(false);
        btn_PPA_PPG_ayuda1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_PPA_PPG_ayuda1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 250, 25, 25));

        btn_PPA_PPG_ayuda2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_PPA_PPG_ayuda2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_PPA_PPG_ayuda2.setContentAreaFilled(false);
        btn_PPA_PPG_ayuda2.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_PPA_PPG_ayuda2, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 250, 25, 25));

        lbl_.setText("aqui hay 2 botones!");
        lbl_.setEnabled(false);
        jp_Componentes.add(lbl_, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 280, -1, -1));

        btn_NCR_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_NCR_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_NCR_ayuda.setContentAreaFilled(false);
        btn_NCR_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_NCR_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 320, 25, 25));

        jsp_ProyeccionPoblacional.setViewportView(jp_Componentes);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/cerrar.png"))); // NOI18N
        jButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton1.setContentAreaFilled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jsp_ProyeccionPoblacional, javax.swing.GroupLayout.DEFAULT_SIZE, 1008, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jsp_ProyeccionPoblacional, javax.swing.GroupLayout.PREFERRED_SIZE, 713, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GuardarActionPerformed
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
    }//GEN-LAST:event_btn_GuardarActionPerformed

    private void btn_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_closeActionPerformed
        bod.WinProyeccionPoblacional = false;
        this.dispose();
    }//GEN-LAST:event_btn_closeActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
         bod.WinProyeccionPoblacional = false;
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Guardar;
    private javax.swing.JButton btn_MEA_ayuda;
    private javax.swing.JButton btn_MEG_ayuda;
    private javax.swing.JButton btn_NCR_ayuda;
    private javax.swing.JButton btn_PPA_PPG_ayuda1;
    private javax.swing.JButton btn_PPA_PPG_ayuda2;
    private javax.swing.JButton btn_close;
    private javax.swing.JButton jButton1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPanel jp_Componentes;
    private javax.swing.JScrollPane jsp_ProyeccionPoblacional;
    private javax.swing.JLabel lbl_;
    private javax.swing.JLabel lbl_MEA_desc;
    private javax.swing.JLabel lbl_NCR_error;
    private javax.swing.JLabel lbl_NCR_titulo1;
    private javax.swing.JLabel lbl_NCR_titulo2;
    private javax.swing.JLabel lbl_PPA_PPG_error;
    private javax.swing.JLabel lbl_PPA_PPG_titulo1;
    private javax.swing.JLabel lbl_PPA_PPG_titulo2;
    private javax.swing.JLabel lbl_TCA_TCG_error;
    private javax.swing.JLabel lbl_TCA_TCG_titulo1;
    private javax.swing.JLabel lbl_TCA_TCG_titulo2;
    private javax.swing.JLabel lbl_save_error;
    private javax.swing.JLabel lbl_save_error2;
    private javax.swing.JLabel lbl_titulo1_ProyeccionPoblacional;
    private javax.swing.JLabel lbl_titulo2_ProyeccionPoblacional;
    private javax.swing.JRadioButton rbtn_MEA_Pregunta;
    private javax.swing.JRadioButton rbtn_MEG_Pregunta;
    private javax.swing.JTextField txt_NCR_Pregunta;
    private javax.swing.JTextField txt_PPA_PPG_Pregunta;
    private javax.swing.JTextField txt_TCA_TCG_Pregunta;
    // End of variables declaration//GEN-END:variables
}
