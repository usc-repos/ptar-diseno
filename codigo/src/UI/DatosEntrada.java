package UI;

import BO.Bod;
import DB.Dao;
import java.io.File;
import Componentes.Util;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.apache.log4j.Logger;
import Componentes.Configuracion;
import Componentes.Listener_Popup;
import Componentes.Listener_Texto;
import java.awt.Component;
import java.sql.ResultSet;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class DatosEntrada extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("DatosEntrada"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.ObtenerKey("rutabasedatos") + File.separator + conf.ObtenerKey("nombrebasedatos"));
    PopupMenuListener listener;
    private Bod bod = new Bod("");
    private Listener_Popup Lpopup;
    private Listener_Texto Ltexto;
    private String error_TUC = "";
    Util util = new Util();

    public DatosEntrada(Bod bod) {
        this.bod = bod;
        initComponents();
        bod.WinDatosDeEntrada = true;
        InicializarComponentes();
    }

    /**
     * Inicializa todos los elementos (jlabels,jtextbox,etc.) con sus
     * respectivos valores y textos desde al base de datos.
     */
    public void InicializarComponentes() {

        ResultSet rs;
        bod.WinDatosDeEntrada = true;//Bandera La ventana se ha abierto
        final int year = Calendar.getInstance().get(Calendar.YEAR);
        btn_guardar.setText("Guardar");
        btn_close.setText("Cerrar");

        try {//Todo: implementar posición de posible error
            //------------------------------------------------------------// Se activa con las acciones del Jcombobox al desplegar drop down, 
            //Inicialemnte coloca el item seleccionado en el año actual, mas abajo se coloca otra si existe
            listener = new PopupMenuListener() {
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

                    JComboBox box = (JComboBox) e.getSource();
                    if (box.getSelectedItem().toString().equals("") || box.getSelectedItem().toString().equals(year + "")) {
                        box.setSelectedItem(year + "");
                    }
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent e) {
                }
            };
            //-------------------------------------------------------------- Titulo de la sección            
            rs = db.ResultadoSelect("obtenerseccion", "1", null);

            lbl_titulo1.setText(rs.getString("Nombre")); //Título de este jpane
            lbl_titulo2.setText(rs.getString("Mensaje"));
            // - - - - - - Pregunta 03 Cuestionario 01 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - FCH 
            rs = db.ResultadoSelect("obtenerpregunta", "1", "FCH");

            bod.minFCH = rs.getInt("rangomin");
            bod.maxFCH = rs.getInt("rangomax");
            bod.errorFCH = rs.getString("errormsg");
            lbl_FCH_titulo1.setText(rs.getString("titulo1"));

            ddm_FCH.addItem(""); //Añade un campo vacio a la lista desplegable fecha

            if (bod.maxFCH <= year) {//por si se usa años despues
                bod.maxFCH += 30;
            }

            for (int annios = bod.minFCH; annios <= bod.maxFCH; annios++) {//Añade años a la lista desplegable fecha
                ddm_FCH.addItem(annios + "");
            }

            ddm_FCH.addPopupMenuListener(listener);
            AsignarPopupBtn(btn_FCH_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 390, 100);
            // - - - - - - Pregunta 02 Cuestionario 01 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - TML 
            rs = db.ResultadoSelect("obtenerpregunta", "1", "TML");

            bod.minTML = rs.getDouble("rangomin");
            bod.maxTML = rs.getDouble("rangomax");
            bod.errorTML = rs.getString("errormsg");
            lbl_TML_titulo1.setText(rs.getString("titulo1"));
            lbl_TML_titulo2.setText(rs.getString("titulo2"));
            AsignarPopupBtn(btn_TML_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 300, 110);
            AsignarTextDoble(txt_TML_Pregunta, bod.minTML, bod.maxTML, lbl_TML_error, bod.errorTML);
            // - - - - - - Pregunta 03 Cuestionario 01 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PCI 
            rs = db.ResultadoSelect("obtenerpregunta", "1", "PCI");

            bod.minPCI = rs.getInt("rangomin");
            bod.maxPCI = rs.getInt("rangomax");
            bod.errorPCI = rs.getString("errormsg");
            lbl_PCI_titulo1.setText(rs.getString("titulo1"));
            lbl_PCI_titulo2.setText(rs.getString("titulo2"));
            lbl_PCI_desc.setText(rs.getString("descripcion"));
            AsignarPopupBtn(btn_PCI_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 420, 110);
            AsignarTextEntero(txt_PCI_Pregunta, bod.minPCI, bod.maxPCI, lbl_PCI_error, bod.errorPCI);
            // - - - - - - Pregunta 04 Cuestionario 01 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PUC 
            rs = db.ResultadoSelect("obtenerpregunta", "1", "PUC");

            bod.minPUC = rs.getInt("rangomin");
            bod.maxPUC = rs.getInt("rangomax");
            bod.errorPUC = rs.getString("errormsg");
            lbl_PUC_titulo1.setText(rs.getString("titulo1"));
            lbl_PUC_titulo2.setText(rs.getString("titulo2"));
            AsignarTextEntero(txt_PUC_Pregunta, bod.minPUC, bod.maxPUC, lbl_PUC_error, bod.errorPUC);
            // - - - - - - Pregunta 05 Cuestionario 01 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - TCI 
            rs = db.ResultadoSelect("obtenerpregunta", "1", "TCI");

            bod.errorTCI = rs.getString("errormsg");
            lbl_TCI_titulo1.setText(rs.getString("titulo1"));

            ddm_TCI.addItem(""); //Añade un campo vacio a la lista desplegable fecha

            bod.minTCI = rs.getInt("rangomin");
            bod.maxTCI = rs.getInt("rangomax");

            if (bod.maxTCI <= year) {
                bod.maxTCI = bod.maxTCI + 30;
            }

            for (int annios = bod.minTCI; annios <= bod.maxTCI; annios++) {//Añade años a la lista desplegable fecha
                ddm_TCI.addItem(annios + "");
            }

            ddm_TCI.addPopupMenuListener(listener);
            // - - - - - - Pregunta 06 Cuestionario 01 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - TUC 
            rs = db.ResultadoSelect("obtenerpregunta", "1", "TUC");

            error_TUC = rs.getString("errormsg");
            bod.errorTUC = error_TUC;
            lbl_TUC_titulo1.setText(rs.getString("titulo1"));

            ddm_TUC.addItem(""); //Añade un campo vacio a la lista desplegable fecha

            bod.minTUC = rs.getInt("rangomin");
            bod.maxTUC = rs.getInt("rangomax");
            if (bod.maxTUC <= year) {
                bod.maxTUC = bod.maxTUC + 30;
            }

            for (int annios = bod.minTUC; annios <= bod.maxTUC; annios++) {//Añade años a la lista desplegable fecha
                ddm_TUC.addItem(annios + "");
            }

            ddm_TUC.addPopupMenuListener(listener);
            // - - - - - - Pregunta 07 Cuestionario 01 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PDI 
            rs = db.ResultadoSelect("obtenerpregunta", "1", "PDI");

            bod.minPDI = rs.getInt("rangomin");
            bod.maxPDI = rs.getInt("rangomax");
            bod.errorPDI = rs.getString("errormsg");
            lbl_PDI_titulo1.setText(rs.getString("titulo1"));
            lbl_PDI_titulo2.setText(rs.getString("titulo2"));
            txt_PDI_Pregunta.setText(rs.getString("valorpordefecto").trim());
            AsignarPopupBtn(btn_PDI_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 420, 300);
            AsignarTextEntero(txt_PDI_Pregunta, bod.minPDI, bod.maxPDI, lbl_PDI_error, bod.errorPDI);
            // - - - - - - Pregunta 08 Cuestionario 01 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - CRE 
            rs = db.ResultadoSelect("obtenerpregunta", "1", "CRE");

            bod.minCRE = rs.getDouble("rangomin");
            bod.maxCRE = rs.getDouble("rangomax");
            bod.errorCRE = rs.getString("errormsg");
            lbl_CRE_titulo1.setText(rs.getString("titulo1"));
            lbl_CRE_titulo2.setText(rs.getString("titulo2"));
            txt_CRE_Pregunta.setText(rs.getString("valorpordefecto").trim());
            AsignarPopupBtn(btn_CRE_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 430, 300);
            AsignarTextDoble(txt_CRE_Pregunta, bod.minCRE, bod.maxCRE, lbl_CRE_error, bod.errorCRE);
            // - - - - - - Pregunta 09 Cuestionario 01 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DMP 
            rs = db.ResultadoSelect("obtenerpregunta", "1", "DMP");

            bod.minDMP = rs.getInt("rangomin");
            bod.maxDMP = rs.getInt("rangomax");
            bod.errorDMP = rs.getString("errormsg");
            lbl_DMP_titulo1.setText(rs.getString("titulo1"));
            lbl_DMP_titulo2.setText(rs.getString("titulo2"));
            txt_DMP_Pregunta.setText(rs.getString("valorpordefecto").trim());
            AsignarPopupBtn(btn_DMP_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 500, 350);
            AsignarTextEntero(txt_DMP_Pregunta, bod.minDMP, bod.maxDMP, lbl_DMP_error, bod.errorDMP);

            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - Si ya han sido llenado correctamente estos datos se obtienen de Bod
            if (bod.EditDatosDeEntrada) {
                ddm_FCH.setSelectedItem(bod.getFCH() + "");
                txt_TML_Pregunta.setText(bod.getTML());
                txt_PCI_Pregunta.setText(bod.getPCI());
                txt_PUC_Pregunta.setText(bod.getPUC());
                ddm_TCI.setSelectedItem(bod.getTCI() + "");
                ddm_TUC.setSelectedItem(bod.getTUC() + "");
                txt_PDI_Pregunta.setText(bod.getPDI());
                txt_CRE_Pregunta.setText(bod.getCRE());
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

        if (!bod.setFCH(ddm_FCH.getSelectedItem() + "", bod.minFCH, bod.maxFCH)) {
            lbl_save_error.setText(bod.errorFCH);
            lbl_save_error2.setText("Punto 1, debe escoger una fecha");
            return false;
        }

        if (!bod.setTML(txt_TML_Pregunta.getText(), bod.minTML, bod.maxTML)) {
            lbl_save_error.setText(bod.errorTML);
            lbl_save_error2.setText("Punto 2");
            return false;
        }

        if (!bod.setPCI(txt_PCI_Pregunta.getText(), bod.minPCI, bod.maxPCI)) {
            lbl_save_error.setText(bod.errorPCI);
            lbl_save_error2.setText("Punto 3 Pci");
            return false;
        }

        if (!bod.setPUC(txt_PUC_Pregunta.getText(), bod.minPUC, bod.maxPUC)) {
            lbl_save_error.setText(bod.errorPUC);
            lbl_save_error2.setText("Punto 4 Puc");
            return false;
        }

        if (ddm_TCI.getSelectedItem().equals("") || ddm_TUC.getSelectedItem().equals("")) {
            lbl_save_error.setText(bod.errorTCI);
            lbl_save_error2.setText("Punto 5 al 6, debe escoger una fecha");
            return false;
        }

        if (!bod.setTCI(ddm_TCI.getSelectedItem() + "", bod.minTCI, bod.maxTCI)) {
            lbl_save_error.setText(bod.errorTCI);
            lbl_save_error2.setText("Punto 5 Tci, debe escoger una fecha");
            return false;
        }

        if (!bod.setTUC(ddm_TUC.getSelectedItem() + "", bod.minTUC, bod.maxTUC)) {
            lbl_save_error.setText(bod.errorTUC);
            lbl_save_error2.setText("Punto 6 Tuc, debe escoger una fecha");
            return false;
        }

        if (!bod.compruebaTucTci()) {
            lbl_save_error.setText(bod.errorTCI);
            lbl_save_error2.setText("Punto 1, el año debe ser mayor a TUC");
            return false;
        }

        if (!bod.setPDI(txt_PDI_Pregunta.getText(), bod.minPDI, bod.maxPDI)) {
            lbl_save_error.setText(bod.errorPDI);
            lbl_save_error2.setText("Punto 7");
            return false;
        }

        if (!bod.setCRE(txt_CRE_Pregunta.getText(), bod.minCRE, bod.maxCRE)) {
            lbl_save_error.setText(bod.errorCRE);
            lbl_save_error2.setText("Punto 8");
            return false;
        }

        if (!bod.setDMP(txt_DMP_Pregunta.getText(), bod.minDMP, bod.maxDMP)) {
            lbl_save_error.setText(bod.errorDMP);
            lbl_save_error2.setText("Punto 9");
            return false;
        }

        bod.EditDatosDeEntrada = true;

        if (!bod.GuardarUpdateBod()) {
            bod.EditDatosDeEntrada = false;
            return false;
        }

        Main main = (Main) this.getTopLevelAncestor(); // Trata de ir  al contenedor principal 
        main.ComprobarCambio(2);//Comprobar cambios que afecten datos posteriores, si este guardar es de una edición.

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

    private void compruebaTucTci() { //Tuc debe ser mayor que Tci

        try {
            lbl_TUC_error.setText("");
            int itci = 0, ituc = 0, ifch = 0;
            String fch = ddm_FCH.getSelectedItem().toString();
            String tci = ddm_TCI.getSelectedItem().toString();
            String tuc = ddm_TUC.getSelectedItem().toString();

            if (!tci.trim().equals("")) {
                itci = Integer.parseInt(tci);
            }
            if (!tuc.trim().equals("")) {
                ituc = Integer.parseInt(tuc);
            }
            if (!fch.trim().equals("")) {
                ifch = Integer.parseInt(fch);
            }

            if (ifch > 0 && ituc > 0 && (ituc >= ifch)) {
                lbl_TUC_error.setText(error_TUC);
                return;
            }

            if (ifch > 0 && itci > 0 && (itci >= ifch)) {
                lbl_TUC_error.setText(error_TUC);
                return;
            }

            if (ituc > 0 && itci > 0 && (itci >= ituc)) {
                lbl_TUC_error.setText(error_TUC);
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jsp_DatosEntrada = new javax.swing.JScrollPane();
        jp_Componentes = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        lbl_titulo1 = new javax.swing.JLabel();
        lbl_FCH_titulo1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        txt_TML_Pregunta = new javax.swing.JTextField();
        lbl_titulo2 = new javax.swing.JLabel();
        lbl_FCH_error = new javax.swing.JLabel();
        btn_guardar = new javax.swing.JButton();
        lbl_TML_titulo1 = new javax.swing.JLabel();
        lbl_TML_titulo2 = new javax.swing.JLabel();
        lbl_TML_error = new javax.swing.JLabel();
        lbl_PCI_desc = new javax.swing.JLabel();
        lbl_PCI_titulo1 = new javax.swing.JLabel();
        txt_PCI_Pregunta = new javax.swing.JTextField();
        lbl_PCI_titulo2 = new javax.swing.JLabel();
        lbl_PUC_titulo1 = new javax.swing.JLabel();
        txt_PUC_Pregunta = new javax.swing.JTextField();
        lbl_PUC_titulo2 = new javax.swing.JLabel();
        lbl_PCI_error = new javax.swing.JLabel();
        lbl_PUC_error = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        lbl_TCI_titulo1 = new javax.swing.JLabel();
        lbl_TUC_titulo1 = new javax.swing.JLabel();
        lbl_TCI_error = new javax.swing.JLabel();
        lbl_TUC_error = new javax.swing.JLabel();
        txt_PDI_Pregunta = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        lbl_PDI_titulo1 = new javax.swing.JLabel();
        lbl_PDI_titulo2 = new javax.swing.JLabel();
        lbl_CRE_error = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        lbl_CRE_titulo1 = new javax.swing.JLabel();
        txt_CRE_Pregunta = new javax.swing.JTextField();
        lbl_CRE_titulo2 = new javax.swing.JLabel();
        lbl_PDI_error = new javax.swing.JLabel();
        lbl_DMP_titulo1 = new javax.swing.JLabel();
        txt_DMP_Pregunta = new javax.swing.JTextField();
        lbl_DMP_titulo2 = new javax.swing.JLabel();
        lbl_DMP_error = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        btn_close = new javax.swing.JButton();
        lbl_save_error = new javax.swing.JLabel();
        lbl_save_error2 = new javax.swing.JLabel();
        ddm_FCH = new javax.swing.JComboBox();
        ddm_TCI = new javax.swing.JComboBox();
        ddm_TUC = new javax.swing.JComboBox();
        btn_PDI_ayuda = new javax.swing.JButton();
        btn_FCH_ayuda = new javax.swing.JButton();
        btn_TML_ayuda = new javax.swing.JButton();
        btn_PCI_ayuda = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JSeparator();
        btn_CRE_ayuda = new javax.swing.JButton();
        btn_DMP_ayuda = new javax.swing.JButton();
        btn_close2 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1024, 768));

        jsp_DatosEntrada.setPreferredSize(new java.awt.Dimension(1024, 768));

        jp_Componentes.setPreferredSize(new java.awt.Dimension(1024, 768));
        jp_Componentes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jp_Componentes.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 38, 1024, -1));

        lbl_titulo1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_titulo1.setForeground(new java.awt.Color(51, 51, 51));
        lbl_titulo1.setText("Titulo");
        jp_Componentes.add(lbl_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 300, 30));

        lbl_FCH_titulo1.setText("Titulo");
        lbl_FCH_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_FCH_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 300, 30));
        jp_Componentes.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 92, 1024, -1));
        jp_Componentes.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 152, 1024, -1));
        jp_Componentes.add(txt_TML_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 110, 130, 25));

        lbl_titulo2.setForeground(new java.awt.Color(0, 51, 102));
        lbl_titulo2.setText("Desc");
        jp_Componentes.add(lbl_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 4, 600, 30));

        lbl_FCH_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_FCH_error.setText(" . . .");
        jp_Componentes.add(lbl_FCH_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 50, 340, 35));

        btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Button_Salva_Guardar_21.png"))); // NOI18N
        btn_guardar.setText("save");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 590, 120, -1));

        lbl_TML_titulo1.setText("Titulo");
        lbl_TML_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_TML_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 300, 30));

        lbl_TML_titulo2.setText("Titulo");
        lbl_TML_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_TML_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 110, 80, 25));

        lbl_TML_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_TML_error.setText(" . . .");
        jp_Componentes.add(lbl_TML_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 100, 340, 35));

        lbl_PCI_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_PCI_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_PCI_desc.setText("Desc");
        lbl_PCI_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PCI_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 610, 30));

        lbl_PCI_titulo1.setText("Titulo");
        lbl_PCI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PCI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 220, 30));
        jp_Componentes.add(txt_PCI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 210, 130, 25));

        lbl_PCI_titulo2.setText("Titulo");
        lbl_PCI_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PCI_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 210, 60, 25));

        lbl_PUC_titulo1.setText("Titulo");
        lbl_PUC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PUC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 220, 30));
        jp_Componentes.add(txt_PUC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 300, 130, 25));

        lbl_PUC_titulo2.setText("Titulo");
        lbl_PUC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PUC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 300, 60, 25));

        lbl_PCI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PCI_error.setText(" . . .");
        jp_Componentes.add(lbl_PCI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 250, 340, 35));

        lbl_PUC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PUC_error.setText(" . . .");
        jp_Componentes.add(lbl_PUC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, 340, 35));
        jp_Componentes.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, 1024, 2));

        lbl_TCI_titulo1.setText("Titulo");
        lbl_TCI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_TCI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 210, 220, 30));

        lbl_TUC_titulo1.setText("Titulo");
        lbl_TUC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_TUC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 300, 220, 30));

        lbl_TCI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_TCI_error.setText(" . . .");
        jp_Componentes.add(lbl_TCI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 250, 340, 35));

        lbl_TUC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_TUC_error.setText(" . . .");
        jp_Componentes.add(lbl_TUC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 340, 340, 35));
        jp_Componentes.add(txt_PDI_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 400, 130, 25));
        jp_Componentes.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 440, 1024, -1));

        lbl_PDI_titulo1.setText("Titulo");
        lbl_PDI_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PDI_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 300, 30));

        lbl_PDI_titulo2.setText("Titulo");
        lbl_PDI_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PDI_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 400, 80, 25));

        lbl_CRE_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_CRE_error.setText(" . . .");
        jp_Componentes.add(lbl_CRE_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 460, 340, 35));
        jp_Componentes.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 500, 1024, -1));

        lbl_CRE_titulo1.setText("Titulo");
        lbl_CRE_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CRE_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 300, 30));
        jp_Componentes.add(txt_CRE_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 460, 130, 25));

        lbl_CRE_titulo2.setText("Titulo");
        lbl_CRE_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CRE_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 460, 80, 25));

        lbl_PDI_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PDI_error.setText(" . . .");
        jp_Componentes.add(lbl_PDI_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 400, 340, 35));

        lbl_DMP_titulo1.setText("Titulo");
        lbl_DMP_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DMP_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, 300, 30));
        jp_Componentes.add(txt_DMP_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 520, 130, 25));

        lbl_DMP_titulo2.setText("Titulo");
        lbl_DMP_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DMP_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 520, 80, 25));

        lbl_DMP_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DMP_error.setText(" . . .");
        jp_Componentes.add(lbl_DMP_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 520, 340, 35));
        jp_Componentes.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 562, 1024, -1));

        btn_close.setText("Salir");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 640, 120, 30));

        lbl_save_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error.setText(" . . .");
        jp_Componentes.add(lbl_save_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 590, 400, 35));

        lbl_save_error2.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error2.setText(" . . .");
        jp_Componentes.add(lbl_save_error2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 640, 400, 35));

        ddm_FCH.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ddm_FCHItemStateChanged(evt);
            }
        });
        jp_Componentes.add(ddm_FCH, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 50, 130, 30));
        ddm_FCH.getAccessibleContext().setAccessibleName("");

        ddm_TCI.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ddm_TCIItemStateChanged(evt);
            }
        });
        jp_Componentes.add(ddm_TCI, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 210, 130, 30));

        ddm_TUC.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ddm_TUCItemStateChanged(evt);
            }
        });
        jp_Componentes.add(ddm_TUC, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 300, 130, 30));

        btn_PDI_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_PDI_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_PDI_ayuda.setContentAreaFilled(false);
        btn_PDI_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_PDI_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 400, 25, 25));

        btn_FCH_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_FCH_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_FCH_ayuda.setContentAreaFilled(false);
        btn_FCH_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_FCH_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 50, 25, 25));

        btn_TML_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_TML_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_TML_ayuda.setContentAreaFilled(false);
        btn_TML_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_TML_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 110, 25, 25));

        btn_PCI_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_PCI_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_PCI_ayuda.setContentAreaFilled(false);
        btn_PCI_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_PCI_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 160, 25, 25));
        jp_Componentes.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 294, 580, 2));

        btn_CRE_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_CRE_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_CRE_ayuda.setContentAreaFilled(false);
        btn_CRE_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_CRE_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 460, 25, 25));

        btn_DMP_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_DMP_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_DMP_ayuda.setContentAreaFilled(false);
        btn_DMP_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_DMP_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 520, 25, 25));

        jsp_DatosEntrada.setViewportView(jp_Componentes);

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
            .addComponent(jsp_DatosEntrada, javax.swing.GroupLayout.DEFAULT_SIZE, 1008, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_close2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(btn_close2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jsp_DatosEntrada, javax.swing.GroupLayout.DEFAULT_SIZE, 706, Short.MAX_VALUE))
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
        bod.WinDatosDeEntrada = false;
        this.dispose();
    }//GEN-LAST:event_btn_closeActionPerformed

    private void btn_close2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_close2ActionPerformed
        bod.WinDatosDeEntrada = false;
        this.dispose();
    }//GEN-LAST:event_btn_close2ActionPerformed

    private void ddm_TCIItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ddm_TCIItemStateChanged
        compruebaTucTci();
    }//GEN-LAST:event_ddm_TCIItemStateChanged

    private void ddm_TUCItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ddm_TUCItemStateChanged
        compruebaTucTci();
    }//GEN-LAST:event_ddm_TUCItemStateChanged

    private void ddm_FCHItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ddm_FCHItemStateChanged
        compruebaTucTci();
    }//GEN-LAST:event_ddm_FCHItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_CRE_ayuda;
    private javax.swing.JButton btn_DMP_ayuda;
    private javax.swing.JButton btn_FCH_ayuda;
    private javax.swing.JButton btn_PCI_ayuda;
    private javax.swing.JButton btn_PDI_ayuda;
    private javax.swing.JButton btn_TML_ayuda;
    private javax.swing.JButton btn_close;
    private javax.swing.JButton btn_close2;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JComboBox ddm_FCH;
    private javax.swing.JComboBox ddm_TCI;
    private javax.swing.JComboBox ddm_TUC;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JPanel jp_Componentes;
    private javax.swing.JScrollPane jsp_DatosEntrada;
    private javax.swing.JLabel lbl_CRE_error;
    private javax.swing.JLabel lbl_CRE_titulo1;
    private javax.swing.JLabel lbl_CRE_titulo2;
    private javax.swing.JLabel lbl_DMP_error;
    private javax.swing.JLabel lbl_DMP_titulo1;
    private javax.swing.JLabel lbl_DMP_titulo2;
    private javax.swing.JLabel lbl_FCH_error;
    private javax.swing.JLabel lbl_FCH_titulo1;
    private javax.swing.JLabel lbl_PCI_desc;
    private javax.swing.JLabel lbl_PCI_error;
    private javax.swing.JLabel lbl_PCI_titulo1;
    private javax.swing.JLabel lbl_PCI_titulo2;
    private javax.swing.JLabel lbl_PDI_error;
    private javax.swing.JLabel lbl_PDI_titulo1;
    private javax.swing.JLabel lbl_PDI_titulo2;
    private javax.swing.JLabel lbl_PUC_error;
    private javax.swing.JLabel lbl_PUC_titulo1;
    private javax.swing.JLabel lbl_PUC_titulo2;
    private javax.swing.JLabel lbl_TCI_error;
    private javax.swing.JLabel lbl_TCI_titulo1;
    private javax.swing.JLabel lbl_TML_error;
    private javax.swing.JLabel lbl_TML_titulo1;
    private javax.swing.JLabel lbl_TML_titulo2;
    private javax.swing.JLabel lbl_TUC_error;
    private javax.swing.JLabel lbl_TUC_titulo1;
    private javax.swing.JLabel lbl_save_error;
    private javax.swing.JLabel lbl_save_error2;
    private javax.swing.JLabel lbl_titulo1;
    private javax.swing.JLabel lbl_titulo2;
    private javax.swing.JTextField txt_CRE_Pregunta;
    private javax.swing.JTextField txt_DMP_Pregunta;
    private javax.swing.JTextField txt_PCI_Pregunta;
    private javax.swing.JTextField txt_PDI_Pregunta;
    private javax.swing.JTextField txt_PUC_Pregunta;
    private javax.swing.JTextField txt_TML_Pregunta;
    // End of variables declaration//GEN-END:variables
}
