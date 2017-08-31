package UI;

import BO.Bod;
import DB.Dao;
import java.io.File;
import Componentes.Util;
import org.apache.log4j.Logger;
import Componentes.Configuracion;
import Componentes.Listener_Popup;
import Componentes.Pregunta;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class CaracterAguaResidual extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("Características del agua residual"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.ObtenerKey("rutabasedatos") + File.separator + conf.ObtenerKey("nombrebasedatos"));
    public Map<String, Pregunta> map = new TreeMap<>();
    private Listener_Popup Lpopup;
    private Bod bod = new Bod("");
    Util util = new Util();
    String sCVS;
    Pregunta pg;

    public CaracterAguaResidual(Bod bod) {
        this.bod = bod;
        initComponents();
        bod.WinCaractInicAguaResidual = true;
        InicializarComponentes();
    }

    /**
     * Inicializa todos los elementos (jlabels,jtextbox,etc.) con sus
     * respectivos valores y textos traidos desde al base de datos.
     */
    public void InicializarComponentes() {

        ResultSet rs;
        bod.WinCaractInicAguaResidual = true;//Bandera La ventana se ha abierto

        try {
            btn_Guardar.setText("Guardar");
            btn_close.setText("Cerrar");
            // - - - - - - Cargar el titulo de la sección  
            rs = db.ResultadoSelect("obtenerseccion", "4");

            lbl_titulo1.setText(rs.getString("Nombre"));
            lbl_titulo2.setText(rs.getString("Mensaje"));
            // - - - - - - Pregunta 01 Cuestionario 04 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - CAT
            rs = db.ResultadoSelect("datospregunta", "CAT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("CAT", pg);

            lbl_CAT_titulo1.setText(pg.tit1);
            lbl_CAT_titulo2.setText(rs.getString("titulo2"));

            txt_CAT_Pregunta.addFocusListener(new FocusAdapter() { //Listener que se dispara cuando la casilla pierde focus
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("CAT");
                }
            });
            // - - - - - - Pregunta 02 Cuestionario 04 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - CAB
            rs = db.ResultadoSelect("datospregunta", "CAB");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("CAB", pg);

            lbl_CAB_titulo1.setText(pg.tit1);
            lbl_CAB_titulo2.setText(rs.getString("titulo2"));

            txt_CAB_Pregunta.addFocusListener(new FocusAdapter() {
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("CAB");
                }
            });

            AsignarPopupBtn(btn_CAB_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 380, 160);
            // - - - - - - Pregunta 03 Cuestionario 04 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - CAQ
            rs = db.ResultadoSelect("datospregunta", "CAQ");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("CAQ", pg);

            lbl_CAQ_titulo1.setText(pg.tit1);
            lbl_CAQ_titulo2.setText(rs.getString("titulo2"));

            txt_CAQ_Pregunta.addFocusListener(new FocusAdapter() {
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("CAQ");
                }
            });

            AsignarPopupBtn(btn_CAQ_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 380, 160);
            // - - - - - - Pregunta 04 Cuestionario 04 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - CAS
            rs = db.ResultadoSelect("datospregunta", "CAS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("CAS", pg);

            lbl_CAS_titulo1.setText(pg.tit1);
            lbl_CAS_titulo2.setText(rs.getString("titulo2"));

            txt_CAS_Pregunta.addFocusListener(new FocusAdapter() {
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("CAS");
                }
            });

            AsignarPopupBtn(btn_CAS_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 380, 160);
            // - - - - - - Pregunta 05 Cuestionario 04 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - CAV
            rs = db.ResultadoSelect("datospregunta", "CAV");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("CAV", pg);

            lbl_CAV_titulo1.setText(pg.tit1);
            lbl_CAV_titulo2.setText(rs.getString("titulo2"));

            txt_CAV_Pregunta.addFocusListener(new FocusAdapter() {
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("CAV");
                }
            });

            AsignarPopupBtn(btn_CAV_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 380, 160);
            // - - - - - - Pregunta 06 Cuestionario 04 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - CVS
            rs = db.ResultadoSelect("datospregunta", "CVS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("CVS", pg);

            lbl_CVS_titulo1.setText(pg.tit1);
            lbl_CVS_titulo2.setText(rs.getString("titulo2"));

            sCVS = rs.getString("valorpordefecto");//Guardar el valor por defecto
            txt_CVS_Pregunta.setText(sCVS);
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - Si ya han sido llenado correctamente estos datos se obtienen de Bod
            if (bod.EditCaractInicAguaResidual) {
                String temp = "";
                txt_CAT_Pregunta.setText(bod.getVarFormateadaPorNombre("CAT", map.get("CAT").deci)); //Trae la variable por nombre y la formatea por cantidad de decimales

                temp = bod.getVarFormateadaPorNombre("CAB", map.get("CAB").deci);

                if (!temp.equals("0") || !temp.equals("0.0")) {  //Si es cero 0, deja la casilla CAB vacía
                    txt_CAB_Pregunta.setText(temp);
                }

                txt_CAQ_Pregunta.setText(bod.getVarFormateadaPorNombre("CAQ", map.get("CAQ").deci));

                temp = bod.getVarFormateadaPorNombre("CAS", map.get("CAS").deci);

                if (!temp.equals("0") || !temp.equals("0.0")) {  //Si es cero 0, deja la casilla CAS vacía
                    txt_CAS_Pregunta.setText(temp);
                }

                temp = bod.getVarFormateadaPorNombre("CAV", map.get("CAV").deci);

                if (!temp.equals("0") || !temp.equals("0.0")) {  //Si es cero 0, deja la casilla CAV vacía
                    txt_CAV_Pregunta.setText(temp);
                }

                txt_CVS_Pregunta.setText(bod.getVarFormateadaPorNombre("CVS", map.get("CVS").deci));

            } else {
                // ejecutarFunciones("CAT");
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

    private boolean Guardar() {

        lbl_save_error.setText("");
        lbl_save_error2.setText("");

        if (!CalcularCAT()) {
            lbl_save_error.setText(map.get("CAT").erro);
            lbl_save_error2.setText(map.get("CAT").tit1 + " . " + map.get("CAT").desc);
            return false;
        }

        if (!CalcularCAB()) {
            lbl_save_error.setText(map.get("CAB").erro);
            lbl_save_error2.setText(map.get("CAB").tit1 + " . " + map.get("CAB").desc);
            return false;
        }

        if (!CalcularCAQ()) {
            lbl_save_error.setText(map.get("CAQ").erro);
            lbl_save_error2.setText(map.get("CAQ").tit1 + " . " + map.get("CAQ").desc);
            return false;
        }

        if (!CalcularCAS()) {
            lbl_save_error.setText(map.get("CAS").erro);
            lbl_save_error2.setText(map.get("CAS").tit1 + " . " + map.get("CAS").desc);
            return false;
        }

        if (!CalcularCAV()) {
            lbl_save_error.setText(map.get("CAV").erro);
            lbl_save_error2.setText(map.get("CAV").tit1 + " . " + map.get("CAV").desc);
            return false;
        }

        if (!CalcularCVS()) {
            lbl_save_error.setText(map.get("CVS").erro);
            lbl_save_error2.setText(map.get("CVS").tit1 + " . " + map.get("CVS").desc);
            return false;
        }

        bod.EditCaractInicAguaResidual = true;

        if (!bod.GuardarUpdateBod()) {
            bod.EditCaractInicAguaResidual = false;
            return false;
        }

        Main main = (Main) this.getTopLevelAncestor(); // Trata de ir  al contenedor principal 
        main.ComprobarCambio(4);//Comprobar cambios que afecten datos posteriores, si este guardar es de una edición.

        return true;
    }

    private boolean CalcularCAT() {

        try {
            lbl_CAT_error.setText("");

            if (bod.setVarDob(txt_CAT_Pregunta.getText(), "CAT", map.get("CAT").rmin, map.get("CAT").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularCAT " + ex.getMessage());
        }
        lbl_CAT_error.setText(map.get("CAT").erro);
        return false;
    }

    private boolean CalcularCAB() {

        try {
            lbl_CAB_error.setText("");

            if (txt_CAB_Pregunta.getText().trim().equals("")) { //No es obligatoria
                bod.setVarDob("0", "CAB", 0, 1);
                return true;
            }

            if (bod.setVarDob(txt_CAB_Pregunta.getText(), "CAB", map.get("CAB").rmin, map.get("CAB").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularCAB " + ex.getMessage());
        }
        lbl_CAB_error.setText(map.get("CAB").erro);
        return false;
    }

    private boolean CalcularCAQ() {

        try {
            lbl_CAQ_error.setText("");

            if (bod.setVarDob(txt_CAQ_Pregunta.getText(), "CAQ", map.get("CAQ").rmin, map.get("CAQ").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularCAQ " + ex.getMessage());
        }
        lbl_CAQ_error.setText(map.get("CAQ").erro);
        return false;
    }

    private boolean CalcularCAS() {

        try {
            lbl_CAS_error.setText("");

            if (txt_CAS_Pregunta.getText().trim().equals("")) { //No es obligatoria
                bod.setVarDob("0", "CAS", 0, 1);
                return true;
            }

            if (bod.setVarDob(txt_CAS_Pregunta.getText(), "CAS", map.get("CAS").rmin, map.get("CAS").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularCAS " + ex.getMessage());
        }
        lbl_CAS_error.setText(map.get("CAS").erro);
        return false;
    }

    private boolean CalcularCAV() {

        try {
            lbl_CAV_error.setText("");

            if (txt_CAV_Pregunta.getText().trim().equals("")) { //No es obligatoria
                bod.setVarDob("0", "CAV", 0, 1);
                return true;
            }

            if (bod.setVarDob(txt_CAV_Pregunta.getText(), "CAV", map.get("CAV").rmin, map.get("CAV").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularCAV " + ex.getMessage());
        }
        lbl_CAV_error.setText(map.get("CAV").erro);
        return false;
    }

    private boolean CalcularCVS() {

        try {
            lbl_CVS_error.setText("");
            txt_CVS_Pregunta.setText("");

            double x = bod.calcularCVS();

            if (x == 0) {
                bod.setVarDob(sCVS, "CVS", map.get("CVS").rmin, map.get("CVS").rmax); //Se coloca el valor por defecto
                txt_CVS_Pregunta.setText(sCVS);
                return true;
            }

            if (bod.setVarDob(x, "CVS", map.get("CVS").rmin, map.get("CVS").rmax)) {
                txt_CVS_Pregunta.setText(bod.getVarFormateadaPorNombre("CVS", map.get("CVS").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularCVS " + ex.getMessage());
        }
        lbl_CVS_error.setText(map.get("CVS").erro);
        return false;
    }

    /**
     * *
     * Ejecuta ciertas funciones por parametro de nombre, la funcion que se
     * ejecuta desencadena otras funciones y a su vez estas pueden hacer lo
     * mismo
     *
     * @param func : Nombre de la variable de la función
     */
    private void ejecutarFunciones(String func) {
        switch (func) {
            case "CAT":
                if (CalcularCAT()) {
                }
                break;
            case "CAB":
                if (CalcularCAB()) {
                }
                break;
            case "CAQ":
                if (CalcularCAQ()) {
                }
                break;
            case "CAS":
                if (CalcularCAS()) {
                    ejecutarFunciones("CVS");
                }
                break;
            case "CAV":
                if (CalcularCAV()) {
                    ejecutarFunciones("CVS");
                }
                break;
            case "CVS":
                CalcularCVS();
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jsp_CaractInicAguaResidual = new javax.swing.JScrollPane();
        jp_Componentes = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        lbl_titulo1 = new javax.swing.JLabel();
        lbl_titulo2 = new javax.swing.JLabel();
        btn_Guardar = new javax.swing.JButton();
        lbl_CAQ_titulo1 = new javax.swing.JLabel();
        txt_CAQ_Pregunta = new javax.swing.JTextField();
        lbl_CAQ_titulo2 = new javax.swing.JLabel();
        lbl_CAQ_error = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        lbl_CAT_titulo1 = new javax.swing.JLabel();
        txt_CAT_Pregunta = new javax.swing.JTextField();
        lbl_CAT_titulo2 = new javax.swing.JLabel();
        lbl_CAT_error = new javax.swing.JLabel();
        lbl_CAB_titulo2 = new javax.swing.JLabel();
        txt_CAB_Pregunta = new javax.swing.JTextField();
        lbl_CAB_error = new javax.swing.JLabel();
        lbl_CAB_titulo1 = new javax.swing.JLabel();
        btn_close = new javax.swing.JButton();
        lbl_save_error = new javax.swing.JLabel();
        lbl_save_error2 = new javax.swing.JLabel();
        txt_CAS_Pregunta = new javax.swing.JTextField();
        lbl_CAS_titulo2 = new javax.swing.JLabel();
        lbl_CAS_titulo1 = new javax.swing.JLabel();
        lbl_CAV_error = new javax.swing.JLabel();
        txt_CAV_Pregunta = new javax.swing.JTextField();
        lbl_CAV_titulo1 = new javax.swing.JLabel();
        lbl_CAS_error = new javax.swing.JLabel();
        lbl_CAV_titulo2 = new javax.swing.JLabel();
        btn_CAV_ayuda = new javax.swing.JButton();
        btn_CAB_ayuda = new javax.swing.JButton();
        btn_CAQ_ayuda = new javax.swing.JButton();
        btn_CAS_ayuda = new javax.swing.JButton();
        lbl_CVS_error = new javax.swing.JLabel();
        txt_CVS_Pregunta = new javax.swing.JTextField();
        lbl_CVS_titulo1 = new javax.swing.JLabel();
        lbl_CVS_titulo2 = new javax.swing.JLabel();
        btn_close2 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1024, 768));

        jsp_CaractInicAguaResidual.setPreferredSize(new java.awt.Dimension(1024, 768));

        jp_Componentes.setPreferredSize(new java.awt.Dimension(1024, 768));
        jp_Componentes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jp_Componentes.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 1024, -1));

        lbl_titulo1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_titulo1.setForeground(new java.awt.Color(51, 51, 51));
        lbl_titulo1.setText("Titulo");
        jp_Componentes.add(lbl_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 4, 300, 30));

        lbl_titulo2.setForeground(new java.awt.Color(0, 51, 102));
        lbl_titulo2.setText("Desc");
        jp_Componentes.add(lbl_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(346, 6, 620, 80));

        btn_Guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Button_Salva_Guardar_21.png"))); // NOI18N
        btn_Guardar.setText("cerrar");
        btn_Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GuardarActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_Guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 120, -1));

        lbl_CAQ_titulo1.setText("Titulo");
        lbl_CAQ_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAQ_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 300, 30));
        jp_Componentes.add(txt_CAQ_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 230, 130, 25));

        lbl_CAQ_titulo2.setText("Titulo");
        lbl_CAQ_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAQ_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 230, 80, 25));

        lbl_CAQ_error.setForeground(new java.awt.Color(153, 153, 255));
        lbl_CAQ_error.setText("...");
        jp_Componentes.add(lbl_CAQ_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 220, 340, 35));
        jp_Componentes.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 430, 1024, -1));

        lbl_CAT_titulo1.setText("Titulo");
        lbl_CAT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 300, 30));
        jp_Componentes.add(txt_CAT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 120, 130, 25));

        lbl_CAT_titulo2.setText("Titulo");
        lbl_CAT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 120, 80, 25));

        lbl_CAT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_CAT_error.setText("...");
        jp_Componentes.add(lbl_CAT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 110, 340, 35));

        lbl_CAB_titulo2.setText("Titulo");
        lbl_CAB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAB_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 180, 80, 25));
        jp_Componentes.add(txt_CAB_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 170, 130, 25));

        lbl_CAB_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_CAB_error.setText("...");
        jp_Componentes.add(lbl_CAB_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 170, 340, 35));

        lbl_CAB_titulo1.setText("Titulo");
        lbl_CAB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAB_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 300, 30));

        btn_close.setText("Salir");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 510, 120, 30));

        lbl_save_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error.setText(" . . .");
        jp_Componentes.add(lbl_save_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 460, 400, 35));

        lbl_save_error2.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error2.setText(" . . .");
        jp_Componentes.add(lbl_save_error2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 510, 400, 35));
        jp_Componentes.add(txt_CAS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 280, 130, 25));

        lbl_CAS_titulo2.setText("Titulo");
        lbl_CAS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 280, 80, 25));

        lbl_CAS_titulo1.setText("Titulo");
        lbl_CAS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 300, 30));

        lbl_CAV_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_CAV_error.setText("...");
        jp_Componentes.add(lbl_CAV_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 330, 340, 35));
        jp_Componentes.add(txt_CAV_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 330, 130, 25));

        lbl_CAV_titulo1.setText("Titulo");
        lbl_CAV_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAV_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 300, 30));

        lbl_CAS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_CAS_error.setText("...");
        jp_Componentes.add(lbl_CAS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 270, 340, 35));

        lbl_CAV_titulo2.setText("Titulo");
        lbl_CAV_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CAV_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 330, 80, 25));

        btn_CAV_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_CAV_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_CAV_ayuda.setContentAreaFilled(false);
        btn_CAV_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_CAV_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 330, 25, 25));

        btn_CAB_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_CAB_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_CAB_ayuda.setContentAreaFilled(false);
        btn_CAB_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_CAB_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 170, 25, 25));

        btn_CAQ_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_CAQ_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_CAQ_ayuda.setContentAreaFilled(false);
        btn_CAQ_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_CAQ_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 230, 25, 25));

        btn_CAS_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_CAS_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_CAS_ayuda.setContentAreaFilled(false);
        btn_CAS_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_CAS_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 280, 25, 25));

        lbl_CVS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_CVS_error.setText("...");
        jp_Componentes.add(lbl_CVS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 380, 340, 35));

        txt_CVS_Pregunta.setEditable(false);
        jp_Componentes.add(txt_CVS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 380, 130, 25));

        lbl_CVS_titulo1.setText("Titulo");
        lbl_CVS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CVS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 300, 30));

        lbl_CVS_titulo2.setText("Titulo");
        lbl_CVS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CVS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 380, 80, 25));

        jsp_CaractInicAguaResidual.setViewportView(jp_Componentes);

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_close2))
                    .addComponent(jsp_CaractInicAguaResidual, javax.swing.GroupLayout.DEFAULT_SIZE, 1008, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btn_close2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jsp_CaractInicAguaResidual, javax.swing.GroupLayout.PREFERRED_SIZE, 724, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        bod.WinCaractInicAguaResidual = false;
        this.dispose();
    }//GEN-LAST:event_btn_closeActionPerformed

    private void btn_close2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_close2ActionPerformed
        bod.WinCaractInicAguaResidual = false;
        this.dispose();
    }//GEN-LAST:event_btn_close2ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_CAB_ayuda;
    private javax.swing.JButton btn_CAQ_ayuda;
    private javax.swing.JButton btn_CAS_ayuda;
    private javax.swing.JButton btn_CAV_ayuda;
    private javax.swing.JButton btn_Guardar;
    private javax.swing.JButton btn_close;
    private javax.swing.JButton btn_close2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JPanel jp_Componentes;
    private javax.swing.JScrollPane jsp_CaractInicAguaResidual;
    private javax.swing.JLabel lbl_CAB_error;
    private javax.swing.JLabel lbl_CAB_titulo1;
    private javax.swing.JLabel lbl_CAB_titulo2;
    private javax.swing.JLabel lbl_CAQ_error;
    private javax.swing.JLabel lbl_CAQ_titulo1;
    private javax.swing.JLabel lbl_CAQ_titulo2;
    private javax.swing.JLabel lbl_CAS_error;
    private javax.swing.JLabel lbl_CAS_titulo1;
    private javax.swing.JLabel lbl_CAS_titulo2;
    private javax.swing.JLabel lbl_CAT_error;
    private javax.swing.JLabel lbl_CAT_titulo1;
    private javax.swing.JLabel lbl_CAT_titulo2;
    private javax.swing.JLabel lbl_CAV_error;
    private javax.swing.JLabel lbl_CAV_titulo1;
    private javax.swing.JLabel lbl_CAV_titulo2;
    private javax.swing.JLabel lbl_CVS_error;
    private javax.swing.JLabel lbl_CVS_titulo1;
    private javax.swing.JLabel lbl_CVS_titulo2;
    private javax.swing.JLabel lbl_save_error;
    private javax.swing.JLabel lbl_save_error2;
    private javax.swing.JLabel lbl_titulo1;
    private javax.swing.JLabel lbl_titulo2;
    private javax.swing.JTextField txt_CAB_Pregunta;
    private javax.swing.JTextField txt_CAQ_Pregunta;
    private javax.swing.JTextField txt_CAS_Pregunta;
    private javax.swing.JTextField txt_CAT_Pregunta;
    private javax.swing.JTextField txt_CAV_Pregunta;
    private javax.swing.JTextField txt_CVS_Pregunta;
    // End of variables declaration//GEN-END:variables
}
