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
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class ProyeccionPoblacional extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("ProyeccionPoblacional"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    public Map<String, Pregunta> map = new TreeMap<>();
    private Listener_Popup Lpopup;
    private Bod bod = new Bod("");
    boolean esPAG = false; //Guarda el estado del radiobutton
    Util util = new Util();
    Pregunta pg;
    private boolean eSave = true;
    private boolean esGuia = false;

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
            //- - - - - - - - - - - - - - - - - - - Titulo de la sección- - - -  
            rs = Resultado("obtenerseccion", "2", null);

            lbl_titulo1_ProyeccionPoblacional.setText(rs.getString("Nombre"));
            lbl_titulo2_ProyeccionPoblacional.setText(rs.getString("Mensaje"));
            // - - - - - - Pregunta 01 Cuestionario 02 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PAG
            rs = db.ResultadoSelect("datospregunta", "PAG");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PAG", pg);

            rbtn_PAG_Pregunta1.setText(pg.tit1);
            rbtn_PAG_Pregunta2.setText(rs.getString("titulo2"));
            lbl_PAG_desc.setText(pg.desc);

            buttongroup.add(rbtn_PAG_Pregunta1);
            buttongroup.add(rbtn_PAG_Pregunta2);

            rbtn_PAG_Pregunta1.addActionListener(new ActionListener() { //Listener para el radiobutton
                @Override
                public void actionPerformed(ActionEvent e) {
                    ejecutarFunciones("PAG");
                }
            });

            rbtn_PAG_Pregunta2.addActionListener(new ActionListener() { //Listener para el radiobutton
                @Override
                public void actionPerformed(ActionEvent e) {
                    ejecutarFunciones("PAG");
                }
            });

            AsignarPopupBtn(btn_PAG_ayuda2, rs.getString("ayuda"), rs.getString("ayudalink"), 520, 240);
            AsignarPopupBtn(btn_PAG_ayuda1, rs.getString("ayuda2"), rs.getString("ayudalink"), 480, 180);
            // - - - - - - Pregunta 03 Cuestionario 02 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PPT 
            rs = db.ResultadoSelect("datospregunta", "PPT");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PPT", pg);

            lbl_PPT_titulo1.setText(pg.tit1);
            lbl_PPT_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_PPF_ayuda1, rs.getString("ayuda"), rs.getString("ayudalink"), 440, 360);
            btn_PPF_ayuda1.setVisible(false);
            // - - - - - - Pregunta 06 Cuestionario 02 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PPF
            rs = db.ResultadoSelect("datospregunta", "PPF");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PPF", pg);

            lbl_PPF_titulo1.setText(pg.tit1);
            lbl_PPF_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_PPF_ayuda2, rs.getString("ayuda"), rs.getString("ayudalink"), 440, 360);
            btn_PPF_ayuda2.setVisible(false);
            // - - - - - - Pregunta 07 Cuestionario 02 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PCR 
            rs = db.ResultadoSelect("datospregunta", "PCR");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PCR", pg);

            lbl_PCR_titulo1.setText(pg.tit1);
            lbl_PCR_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_PCR_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 450, 390);

            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
            // Si Bod cargó datos de Proyeccion Poblacional desde la BD, porque Ya estaba editada, O se cargó un proyecto, se procede a llenar casillas con datos
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -          
            if (bod.EditProyeccionPoblacional) {

                if (bod.getVarInt("PAG") > 0) { //1 = Aritmético
                    esPAG = true;
                    rbtn_PAG_Pregunta1.setSelected(true);
                } else {
                    esPAG = false;
                    rbtn_PAG_Pregunta2.setSelected(true);
                }
                txt_PPT_Pregunta.setText(bod.getVarFormateadaPorNombre("PPT", map.get("PPT").deci));
                txt_PPF_Pregunta.setText(bod.getVarFormateadaPorNombre("PPF", map.get("PPF").deci));
                txt_PCR_Pregunta.setText(bod.getVarStr("PCR"));
            } else {
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
        lbl_PCR_error.setText("");
        lbl_PPF_error.setText("");
        lbl_PPT_error.setText("");
        lbl_save_error.setText("");
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

        String escoger = "Debe completar los campos de: \n";
        lbl_save_error.setText("");
        lbl_save_desc.setText("");

        if (!rbtn_PAG_Pregunta1.isSelected() && !rbtn_PAG_Pregunta2.isSelected()) {
            lbl_save_desc.setText(escoger + map.get("PAG").desc);
            return false;
        }

        if (!CalcularPAG()) {
            lbl_save_error.setText(map.get("PAG").erro);
            lbl_save_desc.setText(map.get("PAG").desc);
            lbl_save_titulo1.setText(map.get("PAG").tit1);
            return false;
        }

        if (!CalcularPPT()) {
            lbl_save_error.setText(map.get("PPT").erro);
            lbl_save_desc.setText(map.get("PPT").desc);
            lbl_save_titulo1.setText(map.get("PPT").tit1);
            return false;
        }

        if (!CalcularPPF()) {
            lbl_save_error.setText(map.get("PPF").erro);
            lbl_save_desc.setText(map.get("PPF").desc);
            lbl_save_titulo1.setText(map.get("PPF").tit1);
            return false;
        }

        if (!CalcularPCR()) {
            lbl_save_error.setText(map.get("PCR").erro);
            lbl_save_desc.setText(map.get("PCR").desc);
            lbl_save_titulo1.setText(map.get("PCR").tit1);
            return false;
        }

        bod.EditProyeccionPoblacional = true;
        Main main = (Main) this.getTopLevelAncestor(); // Trata de ir  al contenedor principal 
        main.cancela = false;
        main.vbod = this.bod;

        if (!main.ComprobarCambio(3, true)) {//Comprobar cambios que afecten datos posteriores, si este guardar es de una edición.
            if (!main.cancela) {
                bod.EditProyeccionPoblacional = false;
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

    private boolean CalcularPAG() {

        try {
            if (rbtn_PAG_Pregunta1.isSelected()) { //Aritmético
                if (bod.setVarInt(1, "PAG", (int) 0, 1)) {
                    btn_PPF_ayuda1.setVisible(true);
                    btn_PPF_ayuda2.setVisible(false);
                    esPAG = true;
                    return true;
                }
            }

            if (rbtn_PAG_Pregunta2.isSelected()) { //Geométrico 
                if (bod.setVarInt(0, "PAG", (int) 0, 1)) {
                    btn_PPF_ayuda1.setVisible(false);
                    btn_PPF_ayuda2.setVisible(true);
                    esPAG = false;
                    return true;
                }
            }
        } catch (Exception ex) {
            log.error("Error: CalcularPAG " + ex.getMessage());
        }
        return false;
    }

    private boolean CalcularPPT() {

        try {
            lbl_PPT_error.setText("");
            double x;

            if (esPAG) {
                x = bod.calcularPPT_A();
            } else {
                x = bod.calcularPPT_G();
            }

            txt_PPT_Pregunta.setText(bod.getVarFormateada(x, map.get("PPT").deci)); //Nueva modalidad ver càlculo asi sea mal hecho.

            if (bod.setVarDob(x, "PPT", map.get("PPT").rmin, map.get("PPT").rmax)) {
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularPPT " + ex.getMessage());
        }
        lbl_PPT_error.setText(map.get("PPT").erro);
        return false;
    }

    private boolean CalcularPPF() {

        try {
            lbl_PPF_error.setText("");
            double x;

            if (esPAG) {
                x = bod.calcularPPF_A();
            } else {
                x = bod.calcularPPF_G();
            }

            txt_PPF_Pregunta.setText(bod.getVarFormateada(x, map.get("PPF").deci));//Nueva modalidad ver càlculo asi sea mal hecho.

            if (bod.setVarInt((int) x, "PPF", (int) map.get("PPF").rmin, (int) map.get("PPF").rmax)) {
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularPPF " + ex.getMessage());
        }
        lbl_PPF_error.setText(map.get("PPF").erro);
        return false;
    }

    private boolean CalcularPCR() {

        try {
            lbl_PCR_error.setText("");
            String x = bod.calcularPCR();

            if (x.isEmpty()) {
                return false;
            }

            if (bod.setVarStr(x, "PCR")) {
                txt_PCR_Pregunta.setText(bod.getVarStr("PCR"));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularPCR " + ex.getMessage());
        }
        lbl_PCR_error.setText(map.get("PCR").erro);
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
            case "PAG":
                CalcularPAG();
                ejecutarFunciones("PPT");
                break;

            case "PPT":
                CalcularPPT();
                ejecutarFunciones("PPF");
                break;

            case "PPF":
                CalcularPPF();
                CalcularPCR();
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
        bod.WinProyeccionPoblacional = false;
        Main main = (Main) this.getTopLevelAncestor();
        main.bod.WinProyeccionPoblacional = false;
        if(esGuia) main.guiaUsuario2();
        this.dispose();
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
        rbtn_PAG_Pregunta1 = new javax.swing.JRadioButton();
        rbtn_PAG_Pregunta2 = new javax.swing.JRadioButton();
        lbl_PAG_desc = new javax.swing.JLabel();
        lbl_PCR_titulo1 = new javax.swing.JLabel();
        txt_PCR_Pregunta = new javax.swing.JTextField();
        lbl_PCR_titulo2 = new javax.swing.JLabel();
        lbl_PCR_error = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        lbl_PPT_titulo1 = new javax.swing.JLabel();
        txt_PPT_Pregunta = new javax.swing.JTextField();
        lbl_PPT_titulo2 = new javax.swing.JLabel();
        lbl_PPT_error = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        lbl_PPF_titulo2 = new javax.swing.JLabel();
        txt_PPF_Pregunta = new javax.swing.JTextField();
        lbl_PPF_error = new javax.swing.JLabel();
        lbl_PPF_titulo1 = new javax.swing.JLabel();
        btn_close = new javax.swing.JButton();
        lbl_save_error = new javax.swing.JLabel();
        lbl_save_desc = new javax.swing.JLabel();
        btn_PAG_ayuda1 = new javax.swing.JButton();
        btn_PAG_ayuda2 = new javax.swing.JButton();
        btn_PPF_ayuda1 = new javax.swing.JButton();
        btn_PPF_ayuda2 = new javax.swing.JButton();
        lbl_ = new javax.swing.JLabel();
        btn_PCR_ayuda = new javax.swing.JButton();
        lbl_save_titulo1 = new javax.swing.JLabel();
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

        rbtn_PAG_Pregunta1.setText("Titulo");
        jp_Componentes.add(rbtn_PAG_Pregunta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 300, 30));

        rbtn_PAG_Pregunta2.setText("Titulo");
        jp_Componentes.add(rbtn_PAG_Pregunta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 300, 30));

        lbl_PAG_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_PAG_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_PAG_desc.setText("Desc");
        lbl_PAG_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PAG_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 610, 30));

        lbl_PCR_titulo1.setText("Titulo");
        lbl_PCR_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PCR_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 300, 30));

        txt_PCR_Pregunta.setEditable(false);
        jp_Componentes.add(txt_PCR_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 320, 130, 25));

        lbl_PCR_titulo2.setText("Titulo");
        lbl_PCR_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PCR_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 320, 80, 25));

        lbl_PCR_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PCR_error.setText("...");
        jp_Componentes.add(lbl_PCR_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 310, 340, 35));
        jp_Componentes.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 1024, 10));
        jp_Componentes.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 360, 1024, -1));

        lbl_PPT_titulo1.setText("Titulo");
        lbl_PPT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PPT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 300, 30));

        txt_PPT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_PPT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 190, 130, 25));

        lbl_PPT_titulo2.setText("Titulo");
        lbl_PPT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PPT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 190, 80, 25));

        lbl_PPT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PPT_error.setText("...");
        jp_Componentes.add(lbl_PPT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 190, 340, 35));
        jp_Componentes.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 1024, -1));

        lbl_PPF_titulo2.setText("Titulo");
        lbl_PPF_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PPF_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 250, 80, 25));

        txt_PPF_Pregunta.setEditable(false);
        jp_Componentes.add(txt_PPF_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 250, 130, 25));

        lbl_PPF_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_PPF_error.setText("...");
        jp_Componentes.add(lbl_PPF_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 240, 340, 35));

        lbl_PPF_titulo1.setText("Titulo");
        lbl_PPF_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_PPF_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 300, 30));

        btn_close.setText("Salir");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, 120, 30));

        lbl_save_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error.setText(" . . .");
        jp_Componentes.add(lbl_save_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 390, 300, 35));

        lbl_save_desc.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_desc.setText(" . . .");
        jp_Componentes.add(lbl_save_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 440, 610, 35));

        btn_PAG_ayuda1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_PAG_ayuda1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_PAG_ayuda1.setContentAreaFilled(false);
        btn_PAG_ayuda1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_PAG_ayuda1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 100, 25, 25));

        btn_PAG_ayuda2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_PAG_ayuda2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_PAG_ayuda2.setContentAreaFilled(false);
        btn_PAG_ayuda2.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_PAG_ayuda2, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 140, 25, 25));

        btn_PPF_ayuda1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_PPF_ayuda1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_PPF_ayuda1.setContentAreaFilled(false);
        btn_PPF_ayuda1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_PPF_ayuda1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 250, 25, 25));

        btn_PPF_ayuda2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_PPF_ayuda2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_PPF_ayuda2.setContentAreaFilled(false);
        btn_PPF_ayuda2.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_PPF_ayuda2, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 250, 25, 25));

        lbl_.setText("aqui hay 2 botones!");
        lbl_.setEnabled(false);
        jp_Componentes.add(lbl_, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 280, -1, -1));

        btn_PCR_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_PCR_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_PCR_ayuda.setContentAreaFilled(false);
        btn_PCR_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_PCR_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 320, 25, 25));

        lbl_save_titulo1.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_titulo1.setText(" . . .");
        jp_Componentes.add(lbl_save_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 390, 300, 35));

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GuardarActionPerformed
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
    }//GEN-LAST:event_btn_GuardarActionPerformed

    private void btn_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_closeActionPerformed
        cerrar();
    }//GEN-LAST:event_btn_closeActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        cerrar();
    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Guardar;
    private javax.swing.JButton btn_PAG_ayuda1;
    private javax.swing.JButton btn_PAG_ayuda2;
    private javax.swing.JButton btn_PCR_ayuda;
    private javax.swing.JButton btn_PPF_ayuda1;
    private javax.swing.JButton btn_PPF_ayuda2;
    private javax.swing.JButton btn_close;
    private javax.swing.JButton jButton1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPanel jp_Componentes;
    private javax.swing.JScrollPane jsp_ProyeccionPoblacional;
    private javax.swing.JLabel lbl_;
    private javax.swing.JLabel lbl_PAG_desc;
    private javax.swing.JLabel lbl_PCR_error;
    private javax.swing.JLabel lbl_PCR_titulo1;
    private javax.swing.JLabel lbl_PCR_titulo2;
    private javax.swing.JLabel lbl_PPF_error;
    private javax.swing.JLabel lbl_PPF_titulo1;
    private javax.swing.JLabel lbl_PPF_titulo2;
    private javax.swing.JLabel lbl_PPT_error;
    private javax.swing.JLabel lbl_PPT_titulo1;
    private javax.swing.JLabel lbl_PPT_titulo2;
    private javax.swing.JLabel lbl_save_desc;
    private javax.swing.JLabel lbl_save_error;
    private javax.swing.JLabel lbl_save_titulo1;
    private javax.swing.JLabel lbl_titulo1_ProyeccionPoblacional;
    private javax.swing.JLabel lbl_titulo2_ProyeccionPoblacional;
    private javax.swing.JRadioButton rbtn_PAG_Pregunta1;
    private javax.swing.JRadioButton rbtn_PAG_Pregunta2;
    private javax.swing.JTextField txt_PCR_Pregunta;
    private javax.swing.JTextField txt_PPF_Pregunta;
    private javax.swing.JTextField txt_PPT_Pregunta;
    // End of variables declaration//GEN-END:variables
}
