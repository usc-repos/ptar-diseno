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

public class LagunaAnaerobia extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("Laguna_Anaerobia"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    public Map<String, Pregunta> map = new TreeMap<>();
    private Bod bod = new Bod("");
    ButtonGroup btnGrupLLA = new ButtonGroup();
    private Listener_Popup Lpopup;
    boolean es_1_3 = false;//para guardar la Relación Largo: Ancho
    Util util = new Util();
    Pregunta pg;
    private boolean eSave = true;
    private boolean esGuia = false;

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
            // - - - - - - Dato 1 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q2C
            rs = db.ResultadoSelect("datospregunta", "Q2C");

            String[] titulo2 = rs.getString("titulo2").split("\\|"); //Q2C en el titulo 2 tiene 2 posibles textos

            lbl_Q2C_titulo1.setText(rs.getString("titulo1"));
            lbl_Q2C_titulo2.setText(titulo2[1].trim());
            txt_Q2C_Pregunta.setText(bod.getVarFormateada(bod.calcularQ2Cm3Dia(), rs.getInt("decimales"))); // Q2C en m³/dia
            // - - - - - - Dato 2 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Si CAB no fue establecido, se usa CAQ
            if (bod.getVarDob("CAB") != 0) {
                rs = db.ResultadoSelect("datospregunta", "CAB");
                txt_CABQ_Pregunta.setText(bod.getVarFormateada(bod.getVarDob("CAB"), rs.getInt("decimales")));
            } else {
                rs = db.ResultadoSelect("datospregunta", "CAQ");
                txt_CABQ_Pregunta.setText(bod.getVarFormateada(bod.getVarDob("CAQ"), rs.getInt("decimales")));
            }
            lbl_CABQ_titulo1.setText(rs.getString("titulo1"));
            lbl_CABQ_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Dato 3 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - CAT
            rs = db.ResultadoSelect("datospregunta", "CAT");

            lbl_CAT_titulo1.setText(rs.getString("titulo1"));
            lbl_CAT_titulo2.setText(rs.getString("titulo2"));
            txt_CAT_Pregunta.setText(bod.getVarFormateadaPorNombre("CAT", rs.getInt("decimales")));
            // - - - - - - Pregunta 4 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LCO
            rs = db.ResultadoSelect("datospregunta", "LCO");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("LCO", pg);

            lbl_LCO_titulo1.setText(pg.tit1);
            lbl_LCO_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_LCO_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 440, 490);
            // - - - - - - Pregunta 5 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LCE
            rs = db.ResultadoSelect("datospregunta", "LCE");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("LCE", pg);

            lbl_LCE_titulo1.setText(pg.tit1);
            lbl_LCE_titulo2.setText(rs.getString("titulo2"));
            lbl_LCE_desc.setText(pg.desc);
            // - - - - - - Pregunta 6 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LAE
            rs = db.ResultadoSelect("datospregunta", "LAE");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("LAE", pg);

            lbl_LAE_titulo1.setText(pg.tit1);
            lbl_LAE_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 7 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LVU
            rs = db.ResultadoSelect("datospregunta", "LVU");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("LVU", pg);

            lbl_LVU_titulo1.setText(pg.tit1);
            lbl_LVU_titulo2.setText(rs.getString("titulo2"));
            
            AsignarPopupBtn(btn_LVU_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 280, 260);
            // - - - - - - Pregunta 8 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LTH
            rs = db.ResultadoSelect("datospregunta", "LTH");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("LTH", pg);

            lbl_LTH_titulo1.setText(pg.tit1);
            lbl_LTH_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_LTH_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 480, 250);
            // - - - - - - Pregunta 9 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LVR
            rs = db.ResultadoSelect("datospregunta", "LVR");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("LVR", pg);

            lbl_LVR_titulo1.setText(pg.tit1);
            lbl_LVR_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 10 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LAU
            rs = db.ResultadoSelect("datospregunta", "LAU");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("LAU", pg);

            lbl_LAU_titulo1.setText(pg.tit1);
            lbl_LAU_titulo2.setText(rs.getString("titulo2"));
            txt_LAU_Pregunta.setText(rs.getString("valorpordefecto"));
            lbl_LAU_desc.setText(pg.desc);

            txt_LAU_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("LAU");
                }
            });

            AsignarPopupBtn(btn_LAU_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 490, 260);
            // - - - - - - Pregunta 11 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LAS
            rs = db.ResultadoSelect("datospregunta", "LAS");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("LAS", pg);

            lbl_LAS_titulo1.setText(pg.tit1);
            lbl_LAS_titulo2.setText(rs.getString("titulo2"));
            
            AsignarPopupBtn(btn_LAS_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 250, 160);
            // - - - - - - Pregunta 12 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LLA 
            rs = db.ResultadoSelect("datospregunta", "LLA");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("LLA", pg);

            rbtn_LLA_Pregunta1.setText(pg.tit1);
            rbtn_LLA_Pregunta2.setText(rs.getString("titulo2"));
            lbl_LLA_desc.setText(pg.desc);

            btnGrupLLA.add(rbtn_LLA_Pregunta1);
            btnGrupLLA.add(rbtn_LLA_Pregunta2);

            rbtn_LLA_Pregunta1.addActionListener(new ActionListener() { //Listener para el radiobutton
                @Override
                public void actionPerformed(ActionEvent e) {
                    ejecutarFunciones("LLA");
                }
            });

            rbtn_LLA_Pregunta2.addActionListener(new ActionListener() { //Listener para el radiobutton
                @Override
                public void actionPerformed(ActionEvent e) {
                    ejecutarFunciones("LLA");
                }
            });

            AsignarPopupBtn(btn_LLA_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 470, 120);
            // - - - - - - Pregunta 13 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LAA
            rs = db.ResultadoSelect("datospregunta", "LAA");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("LAA", pg);

            lbl_LAA_titulo1.setText(pg.tit1);
            lbl_LAA_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 14 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LAL
            rs = db.ResultadoSelect("datospregunta", "LAL");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("LAL", pg);

            lbl_LAL_titulo1.setText(pg.tit1);
            lbl_LAL_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 15 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LAP
            rs = db.ResultadoSelect("datospregunta", "LAP");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("LAP", pg);

            lbl_LAP_titulo1.setText(pg.tit1);
            lbl_LAP_titulo2.setText(rs.getString("titulo2"));
            txt_LAP_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_LAP_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("LAP");
                }
            });

            AsignarPopupBtn(btn_LAP_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 310, 160);
            // - - - - - - Pregunta 16 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LAI
            rs = db.ResultadoSelect("datospregunta", "LAI");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("LAI", pg);

            lbl_LAI_titulo1.setText(pg.tit1);
            lbl_LAI_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_LAI_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 250, 150);
            // - - - - - - Pregunta 17 Cuestionario 5 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - LAB
            rs = db.ResultadoSelect("datospregunta", "LAB");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("LAB", pg);

            lbl_LAB_titulo1.setText(pg.tit1);
            lbl_LAB_titulo2.setText(rs.getString("titulo2"));
            AsignarPopupBtn(btn_LAB_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 350, 220);

            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
            // Si Bod cargó datos de Laguna Anaerobia desde la BD, porque Ya estaba editada, se proceden a llenar casillas con datos
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -          
            if (bod.EditLagunaAnaerobia) {

                txt_LCO_Pregunta.setText(bod.getVarFormateadaPorNombre("LCO", map.get("LCO").deci));
                txt_LCE_Pregunta.setText(bod.getVarFormateadaPorNombre("LCE", map.get("LCE").deci));
                txt_LAE_Pregunta.setText(bod.getVarFormateadaPorNombre("LAE", map.get("LAE").deci));
                txt_LVU_Pregunta.setText(bod.getVarFormateadaPorNombre("LVU", map.get("LVU").deci));
                txt_LTH_Pregunta.setText(bod.getVarFormateadaPorNombre("LTH", map.get("LTH").deci));
                txt_LVR_Pregunta.setText(bod.getVarFormateadaPorNombre("LVR", map.get("LVR").deci));
                txt_LAU_Pregunta.setText(bod.getVarFormateadaPorNombre("LAU", map.get("LAU").deci));
                txt_LAS_Pregunta.setText(bod.getVarFormateadaPorNombre("LAS", map.get("LAS").deci));

                if (bod.getVarDob("LLA") > 0) { //1:3
                    rbtn_LLA_Pregunta1.setSelected(true);
                } else { //2:3
                    rbtn_LLA_Pregunta2.setSelected(true);
                }

                txt_LAA_Pregunta.setText(bod.getVarFormateadaPorNombre("LAA", map.get("LAA").deci));
                txt_LAL_Pregunta.setText(bod.getVarFormateadaPorNombre("LAL", map.get("LAL").deci));
                txt_LAP_Pregunta.setText(bod.getVarFormateadaPorNombre("LAP", map.get("LAP").deci));
                txt_LAI_Pregunta.setText(bod.getVarFormateadaPorNombre("LAI", map.get("LAI").deci));
                txt_LAB_Pregunta.setText(bod.getVarFormateadaPorNombre("LAB", map.get("LAB").deci));

            } else {// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -si no otras tareas de cálculos que se deben hacer al cargar
                ejecutarFunciones("LCO");
                ejecutarFunciones("LCE");
                ejecutarFunciones("LAE");
                ejecutarFunciones("LVU");
                ejecutarFunciones("LAU");
                ejecutarFunciones("LAP");
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
        lbl_LAA_error.setText("");
        lbl_LAB_error.setText("");
        lbl_LAE_error.setText("");
        lbl_LAI_error.setText("");
        lbl_LAL_error.setText("");
        lbl_LAP_error.setText("");
        lbl_LAS_error.setText("");
        lbl_LAU_error.setText("");
        lbl_LCE_error.setText("");
        lbl_LCO_error.setText("");
        lbl_LTH_error.setText("");
        lbl_LVR_error.setText("");
        lbl_LVU_error.setText("");
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

        String escoger = "Debe completar los campos de: \n";
        lbl_save_error.setText("");
        lbl_save_desc.setText("");

        if (!CalcularLCO()) {
            lbl_save_error.setText(map.get("LCO").erro);
            lbl_save_desc.setText(map.get("LCO").desc);
            lbl_save_titulo1.setText(map.get("LCO").tit1);
            return false;
        }

        if (!CalcularLCE()) {
            lbl_save_error.setText(map.get("LCE").erro);
            lbl_save_desc.setText(map.get("LCE").desc);
            lbl_save_titulo1.setText(map.get("LCE").tit1);
            return false;
        }

        if (!CalcularLAE()) {
            lbl_save_error.setText(map.get("LAE").erro);
            lbl_save_desc.setText(map.get("LAE").desc);
            lbl_save_titulo1.setText(map.get("LAE").tit1);
            return false;
        }

        if (!CalcularLVU()) {
            lbl_save_error.setText(map.get("LVU").erro);
            lbl_save_desc.setText(map.get("LVU").desc);
            lbl_save_titulo1.setText(map.get("LVU").tit1);
            return false;
        }

        if (!CalcularLTH()) {
            lbl_save_error.setText(map.get("LTH").erro);
            lbl_save_desc.setText(map.get("LTH").desc);
            lbl_save_titulo1.setText(map.get("LTH").tit1);
            return false;
        }

        if (!CalcularLVR()) {
            lbl_save_error.setText(map.get("LVR").erro);
            lbl_save_desc.setText(map.get("LVR").desc);
            lbl_save_titulo1.setText(map.get("LVR").tit1);
            return false;
        }

        if (!CalcularLAU()) {
            lbl_save_error.setText(map.get("LAU").erro);
            lbl_save_desc.setText(map.get("LAU").desc);
            lbl_save_titulo1.setText(map.get("LAU").tit1);
            return false;
        }

        if (!CalcularLAS()) {
            lbl_save_error.setText(map.get("LAS").erro);
            lbl_save_desc.setText(map.get("LAS").desc);
            lbl_save_titulo1.setText(map.get("LAS").tit1);
            return false;
        }

        if (!rbtn_LLA_Pregunta1.isSelected() && !rbtn_LLA_Pregunta2.isSelected()) {
            lbl_save_desc.setText(escoger + map.get("LLA").desc);
            return false;
        }

        if (!CalcularLLA()) {
            lbl_save_error.setText(map.get("LLA").erro);
            lbl_save_desc.setText(map.get("LLA").desc);
            lbl_save_titulo1.setText(map.get("LLA").tit1);
            return false;
        }

        if (!CalcularLAA()) {
            lbl_save_error.setText(map.get("LAA").erro);
            lbl_save_desc.setText(map.get("LAA").desc);
            lbl_save_titulo1.setText(map.get("LAA").tit1);
            return false;
        }

        if (!CalcularLAL()) {
            lbl_save_error.setText(map.get("LAL").erro);
            lbl_save_desc.setText(map.get("LAL").desc);
            lbl_save_titulo1.setText(map.get("LAL").tit1);
            return false;
        }

        if (!CalcularLAP()) {
            lbl_save_error.setText(map.get("LAP").erro);
            lbl_save_desc.setText(map.get("LAP").desc);
            lbl_save_titulo1.setText(map.get("LAP").tit1);
            return false;
        }

        if (!CalcularLAI()) {
            lbl_save_error.setText(map.get("LAI").erro);
            lbl_save_desc.setText(map.get("LAI").desc);
            lbl_save_titulo1.setText(map.get("LAI").tit1);
            return false;
        }

        if (!CalcularLAB()) {
            lbl_save_error.setText(map.get("LAB").erro);
            lbl_save_desc.setText(map.get("LAB").desc);
            lbl_save_titulo1.setText(map.get("LAB").tit1);
            return false;
        }

        bod.EditLagunaAnaerobia = true;
        Main main = (Main) this.getTopLevelAncestor(); // Trata de ir  al contenedor principal 
        main.cancela = false;
        main.vbod = this.bod;

        if (!main.ComprobarCambio(16, true)) {//Comprobar cambios que afecten datos posteriores, si este guardar es de una edición.
            if (!main.cancela) {
                bod.EditLagunaAnaerobia = false;
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

    //------------------------------------------------
    private boolean CalcularLCO() {

        try {
            lbl_LCO_error.setText("");
            double x = bod.calcularLCO();

            if (bod.setVarDob(x, "LCO", map.get("LCO").rmin, map.get("LCO").rmax)) {
                txt_LCO_Pregunta.setText(bod.getVarFormateadaPorNombre("LCO", map.get("LCO").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularLCO " + ex.getMessage());
        }
        lbl_LCO_error.setText(map.get("LCO").erro);
        return false;
    }

    private boolean CalcularLCE() {

        try {
            lbl_LCE_error.setText("");
            double x = bod.calcularLCE();

            if (bod.setVarDob(x, "LCE", map.get("LCE").rmin, map.get("LCE").rmax)) {
                txt_LCE_Pregunta.setText(bod.getVarFormateadaPorNombre("LCE", map.get("LCE").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularLCE " + ex.getMessage());
        }
        lbl_LCE_error.setText(map.get("LCE").erro);
        return false;
    }

    private boolean CalcularLAE() {

        try {
            lbl_LAE_error.setText("");
            double x = bod.calcularLAE();

            if (bod.setVarDob(x, "LAE", map.get("LAE").rmin, map.get("LAE").rmax)) {
                txt_LAE_Pregunta.setText(bod.getVarFormateadaPorNombre("LAE", map.get("LAE").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularLAE " + ex.getMessage());
        }
        lbl_LAE_error.setText(map.get("LAE").erro);
        return false;
    }

    private boolean CalcularLVU() {

        try {
            lbl_LVU_error.setText("");
            double x = bod.calcularLVU();

            if (bod.setVarDob(x, "LVU", map.get("LVU").rmin, map.get("LVU").rmax)) {
                txt_LVU_Pregunta.setText(bod.getVarFormateadaPorNombre("LVU", map.get("LVU").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularLVU " + ex.getMessage());
        }
        lbl_LVU_error.setText(map.get("LVU").erro);
        return false;
    }

    private boolean CalcularLTH() {

        try {
            lbl_LTH_error.setText("");
            double x = bod.calcularLTH();

            if (bod.setVarDob(x, "LTH", map.get("LTH").rmin, map.get("LTH").rmax)) {
                txt_LTH_Pregunta.setText(bod.getVarFormateadaPorNombre("LTH", map.get("LTH").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularLTH " + ex.getMessage());
        }
        lbl_LTH_error.setText(map.get("LTH").erro);
        return false;
    }

    private boolean CalcularLVR() {

        try {
            lbl_LVR_error.setText("");
            double x = bod.calcularLVR();

            if (bod.setVarDob(x, "LVR", map.get("LVR").rmin, map.get("LVR").rmax)) {
                txt_LVR_Pregunta.setText(bod.getVarFormateadaPorNombre("LVR", map.get("LVR").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularLVR " + ex.getMessage());
        }
        lbl_LVR_error.setText(map.get("LVR").erro);
        return false;
    }

    private boolean CalcularLAU() {

        try {
            lbl_LAU_error.setText("");

            if (bod.setVarDob(txt_LAU_Pregunta.getText(), "LAU", map.get("LAU").rmin, map.get("LAU").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularLAU " + ex.getMessage());
        }
        lbl_LAU_error.setText(map.get("LAU").erro);
        return false;
    }

    private boolean CalcularLAS() {

        try {
            lbl_LAS_error.setText("");
            double x = bod.calcularLAS();

            if (bod.setVarDob(x, "LAS", map.get("LAS").rmin, map.get("LAS").rmax)) {
                txt_LAS_Pregunta.setText(bod.getVarFormateadaPorNombre("LAS", map.get("LAS").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularLAS " + ex.getMessage());
        }
        lbl_LAS_error.setText(map.get("LAS").erro);
        return false;
    }

    private boolean CalcularLLA() {

        try {

            if (rbtn_LLA_Pregunta1.isSelected()) { // 1 = 1:3
                if (bod.setVarInt(1, "LLA", (int) 0, 1)) {
                    es_1_3 = true;
                    return true;
                }
            }
            if (rbtn_LLA_Pregunta2.isSelected()) { // 2 = 2:3
                if (bod.setVarInt(0, "LLA", (int) 0, 1)) {
                    es_1_3 = false;
                    return true;
                }
            }
        } catch (Exception ex) {
            log.error("Error: CalcularLLA " + ex.getMessage());
        }
        return false;
    }

    private boolean CalcularLAA() {

        try {
            if (!rbtn_LLA_Pregunta1.isSelected() && !rbtn_LLA_Pregunta2.isSelected()) {
                return false;
            }

            lbl_LAA_error.setText("");
            double x;

            if (es_1_3) {
                x = bod.calcularLAA_1_3();
            } else {
                x = bod.calcularLAA_2_3();
            }

            if (bod.setVarDob(x, "LAA", map.get("LAA").rmin, map.get("LAA").rmax)) {
                txt_LAA_Pregunta.setText(bod.getVarFormateadaPorNombre("LAA", map.get("LAA").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularLAA " + ex.getMessage());
        }
        lbl_LAA_error.setText(map.get("LAA").erro);
        return false;
    }

    private boolean CalcularLAL() {

        try {
            if (!rbtn_LLA_Pregunta1.isSelected() && !rbtn_LLA_Pregunta2.isSelected()) {
                return false;
            }

            lbl_LAL_error.setText("");
            double x;

            if (es_1_3) {
                x = bod.calcularLAL_1_3();
            } else {
                x = bod.calcularLAL_2_3();
            }

            if (bod.setVarDob(x, "LAL", map.get("LAL").rmin, map.get("LAL").rmax)) {
                txt_LAL_Pregunta.setText(bod.getVarFormateadaPorNombre("LAL", map.get("LAL").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularLAL " + ex.getMessage());
        }
        lbl_LAL_error.setText(map.get("LAL").erro);
        return false;
    }

    private boolean CalcularLAP() {

        try {
            lbl_LAP_error.setText("");

            if (bod.setVarInt(txt_LAP_Pregunta.getText(), "LAP", (int) map.get("LAP").rmin, (int) map.get("LAP").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularLAP " + ex.getMessage());
        }
        lbl_LAP_error.setText(map.get("LAP").erro);
        return false;
    }

    private boolean CalcularLAI() {

        try {
            lbl_LAI_error.setText("");
            double x = bod.calcularLAI();

            if (bod.setVarDob(x, "LAI", map.get("LAI").rmin, map.get("LAI").rmax)) {
                txt_LAI_Pregunta.setText(bod.getVarFormateadaPorNombre("LAI", map.get("LAI").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularLAI " + ex.getMessage());
        }
        lbl_LAI_error.setText(map.get("LAI").erro);
        return false;
    }

    private boolean CalcularLAB() {

        try {
            lbl_LAB_error.setText("");
            double x = bod.calcularLAB();

            if (bod.setVarDob(x, "LAB", map.get("LAB").rmin, map.get("LAB").rmax)) {
                txt_LAB_Pregunta.setText(bod.getVarFormateadaPorNombre("LAB", map.get("LAB").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularLAB " + ex.getMessage());
        }
        lbl_LAB_error.setText(map.get("LAB").erro);
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
            case "LCO":
                CalcularLCO();
                ejecutarFunciones("LVU");
                break;

            case "LCE":
                CalcularLCE();
                ejecutarFunciones("LAE");
                break;

            case "LAE":
                CalcularLAE();
                break;

            case "LVU":
                CalcularLVU();
                ejecutarFunciones("LTH");
                break;

            case "LTH":
                CalcularLTH();
                ejecutarFunciones("LVR");
                break;

            case "LVR":
                CalcularLVR();
                ejecutarFunciones("LAS");
                break;

            case "LAU":
                CalcularLAU();
                ejecutarFunciones("LAS");
                break;

            case "LAS":
                CalcularLAS();
                ejecutarFunciones("LAA");
                ejecutarFunciones("LAB");
                break;

            case "LLA":
                CalcularLLA();
                ejecutarFunciones("LAA");
                break;

            case "LAA":
                CalcularLAA();
                ejecutarFunciones("LAL");
                break;

            case "LAL":
                CalcularLAL();
                break;

            case "LAP":
                CalcularLAP();
                ejecutarFunciones("LAI");
                break;

            case "LAI":
                CalcularLAI();
                break;

            case "LAB":
                CalcularLAB();
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
        bod.WinLagunaAnaerobia = false;
        Main main = (Main) this.getTopLevelAncestor();
        main.bod.WinLagunaAnaerobia = false;
        if(esGuia) main.guiaUsuario2();
        this.dispose();
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
        lbl_CABQ_titulo1 = new javax.swing.JLabel();
        txt_CABQ_Pregunta = new javax.swing.JTextField();
        lbl_CABQ_titulo2 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        btn_close = new javax.swing.JButton();
        lbl_save_error = new javax.swing.JLabel();
        lbl_save_desc = new javax.swing.JLabel();
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
        lbl_save_titulo1 = new javax.swing.JLabel();
        btn_LVU_ayuda = new javax.swing.JButton();
        btn_LAS_ayuda = new javax.swing.JButton();
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

        lbl_CABQ_titulo1.setText("Titulo");
        lbl_CABQ_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CABQ_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 300, 30));

        txt_CABQ_Pregunta.setEditable(false);
        jp_Componentes.add(txt_CABQ_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 90, 130, 25));

        lbl_CABQ_titulo2.setText("Titulo");
        lbl_CABQ_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_CABQ_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 90, 80, 25));
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
        jp_Componentes.add(lbl_save_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 950, 500, 35));

        lbl_save_desc.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_desc.setText(" . . .");
        jp_Componentes.add(lbl_save_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 990, 610, 35));

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

        txt_LAU_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
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

        txt_LAP_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
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

        lbl_save_titulo1.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_titulo1.setText(" . . .");
        jp_Componentes.add(lbl_save_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 950, 300, 35));

        btn_LVU_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_LVU_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_LVU_ayuda.setContentAreaFilled(false);
        btn_LVU_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_LVU_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 370, 25, 25));

        btn_LAS_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_LAS_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_LAS_ayuda.setContentAreaFilled(false);
        btn_LAS_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_LAS_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 580, 25, 25));

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
            .addComponent(jsp_LagunaAnaerobia, javax.swing.GroupLayout.PREFERRED_SIZE, 1008, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_close2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(btn_close2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jsp_LagunaAnaerobia, javax.swing.GroupLayout.DEFAULT_SIZE, 1040, Short.MAX_VALUE)
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
    private javax.swing.JButton btn_LAB_ayuda;
    private javax.swing.JButton btn_LAI_ayuda;
    private javax.swing.JButton btn_LAP_ayuda;
    private javax.swing.JButton btn_LAS_ayuda;
    private javax.swing.JButton btn_LAU_ayuda;
    private javax.swing.JButton btn_LCO_ayuda;
    private javax.swing.JButton btn_LLA_ayuda;
    private javax.swing.JButton btn_LTH_ayuda;
    private javax.swing.JButton btn_LVU_ayuda;
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
    private javax.swing.JLabel lbl_CABQ_titulo1;
    private javax.swing.JLabel lbl_CABQ_titulo2;
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
    private javax.swing.JLabel lbl_save_desc;
    private javax.swing.JLabel lbl_save_error;
    private javax.swing.JLabel lbl_save_titulo1;
    private javax.swing.JLabel lbl_titulo1;
    private javax.swing.JLabel lbl_titulo2;
    private javax.swing.JRadioButton rbtn_LLA_Pregunta1;
    private javax.swing.JRadioButton rbtn_LLA_Pregunta2;
    private javax.swing.JTextField txt_CABQ_Pregunta;
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
