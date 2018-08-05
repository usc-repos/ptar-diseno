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
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class Desarenador extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("Desarenador"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    public Map<String, Pregunta> map = new TreeMap<>();
    private Bod bod = new Bod("");
    private Listener_Popup Lpopup;
    Util util = new Util();
    Pregunta pg;
    private boolean eSave = true;
    private boolean esGuia = false;

    public Desarenador(Bod bod) {
        this.bod = bod;
        initComponents();
        InicializarComponentes();
    }

    /**
     * Inicializa todos los elementos (jlabels,jtextbox,etc.) con sus
     * respectivos valores y textos desde al base de datos.
     */
    public void InicializarComponentes() {

        ResultSet rs;
        bod.WinDesarenador = true;//Bandera La ventana se ha abierto

        try {
            btn_guardar.setText("Guardar");
            btn_close.setText("Cerrar");

            // - - - - - - Titulo de la sección            
            rs = db.ResultadoSelect("obtenerseccion", "9", null);

            lbl_titulo1.setText(rs.getString("Nombre"));
            lbl_titulo2.setText(rs.getString("Mensaje"));

            // - - - - - - Dato 1 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q3C  (en m³/dia)
            rs = db.ResultadoSelect("datospregunta", "Q3C");

            String[] titulo3 = rs.getString("titulo2").split("\\|"); //Q3C en el titulo 2 tiene 2 posibles textos

            lbl_Q3C_titulo1.setText(rs.getString("titulo1"));
            lbl_Q3C_titulo2.setText(titulo3[2].trim());
            txt_Q3C_Pregunta.setText(bod.getVarFormateada(bod.calcularQ3Cm3Seg(), rs.getInt("decimales")));
            // - - - - - - Dato 2 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q1C (en m³/dia)
            rs = db.ResultadoSelect("datospregunta", "Q1C");

            String[] titulo1 = rs.getString("titulo2").split("\\|");

            lbl_Q1C_titulo1.setText(rs.getString("titulo1"));
            lbl_Q1C_titulo2.setText(titulo3[2].trim());
            txt_Q1C_Pregunta.setText(bod.getVarFormateada(bod.calcularQ1Cm3Seg(), rs.getInt("decimales")));
            // - - - - - - Dato 3 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q2C (en m³/dia)
            rs = db.ResultadoSelect("datospregunta", "Q2C");

            String[] titulo2 = rs.getString("titulo2").split("\\|");

            lbl_Q2C_titulo1.setText(rs.getString("titulo1"));
            lbl_Q2C_titulo2.setText(titulo2[2].trim());
            txt_Q2C_Pregunta.setText(bod.getVarFormateada(bod.calcularQ2Cm3Seg(), rs.getInt("decimales")));
            // - - - - - - Pregunta 01 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DAN 
            rs = db.ResultadoSelect("datospregunta", "DAN");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DAN", pg);

            lbl_DAN_titulo1.setText(pg.tit1);
            lbl_DAN_titulo2.setText(rs.getString("titulo2"));
            lbl_DAN_desc.setText(pg.desc);

            AsignarPopupBtn(btn_DAN_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 280, 480);

            //El valor por defecto que se coloca en ("if (bod.EditDesarenador)..."),se aprovecha para obtenerlo acá. En la ecuación están los rangos del ddl
            rs = db.ResultadoSelect("obtenerecuacion", "dimensioncanaletaparshall", "DAN");

            String[] equ = rs.getString("ecuacion").split(",");
            double x;

            if (bod.EditDesarenador) {//Se ubica la posicion (no valor) del menú si es un proyecto cargado
                x = bod.getVarDob("DAN");
            } else {//Se ubica la posicion del menú si no hay datos (posición sugerida)
                x = bod.calcularDAN();
            }

            int pos = 0, cont = 0;

            for (String elem : equ) {

                String[] cms = elem.split("\\|");
                ddm_DAN_pregunta.addItem(cms[0]);//Agregar items al menú

                if (x == Double.parseDouble(cms[0])) {
                    pos = cont; //Se detecta la posición recomendada, más abajo se coloca
                }
                cont++;
            }

            ddm_DAN_pregunta.addActionListener(new ActionListener() { //Listener del menú
                @Override
                public void actionPerformed(ActionEvent e) {
                    ejecutarFunciones("DAN");
                }
            });

            // - - - - - - Pregunta 02 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DCN                                  
            rs = db.ResultadoSelect("datospregunta", "DCN");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DCN", pg);

            lbl_DCN_titulo1.setText(pg.tit1);
            lbl_DCN_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 03 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DCK  
            rs = db.ResultadoSelect("datospregunta", "DCK");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DCK", pg);

            lbl_DCK_titulo1.setText(pg.tit1);
            lbl_DCK_titulo2.setText(rs.getString("titulo2"));
            lbl_DCK_desc.setText(pg.desc);

            AsignarPopupBtn(btn_DCK_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 380, 490);
            // - - - - - - Pregunta 04 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DP1  
            rs = db.ResultadoSelect("datospregunta", "DP1");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DP1", pg);

            lbl_DP1_titulo1.setText(pg.tit1);
            lbl_DP1_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_DP1_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 490, 500);
            // - - - - - - Pregunta 05 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DP2  

            rs = db.ResultadoSelect("datospregunta", "DP2");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DP2", pg);

            lbl_DP2_titulo1.setText(pg.tit1);
            lbl_DP2_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 06 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DP3  

            rs = db.ResultadoSelect("datospregunta", "DP3");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DP3", pg);

            lbl_DP3_titulo1.setText(pg.tit1);
            lbl_DP3_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta 07 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DRZ  
            rs = db.ResultadoSelect("datospregunta", "DRZ");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DRZ", pg);

            lbl_DRZ_titulo1.setText(pg.tit1);
            lbl_DRZ_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_DRZ_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 380, 320);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DPA  
            rs = db.ResultadoSelect("datospregunta", "DPA");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DPA", pg);

            lbl_DPA_titulo1.setText(pg.tit1);
            lbl_DPA_titulo2.setText(rs.getString("titulo2"));
            lbl_DPA_desc.setText(pg.desc);

            AsignarPopupBtn(btn_DPA_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 490, 500);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DPB 
            rs = db.ResultadoSelect("datospregunta", "DPB");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DPB", pg);

            lbl_DPB_titulo1.setText(pg.tit1);
            lbl_DPB_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DPC 
            rs = db.ResultadoSelect("datospregunta", "DPC");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DPC", pg);

            lbl_DPC_titulo1.setText(pg.tit1);
            lbl_DPC_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DPD 
            rs = db.ResultadoSelect("datospregunta", "DPD");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DPD", pg);

            lbl_DPD_titulo1.setText(pg.tit1);
            lbl_DPD_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DPE 
            rs = db.ResultadoSelect("datospregunta", "DPE");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DPE", pg);

            lbl_DPE_titulo1.setText(pg.tit1);
            lbl_DPE_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DPF 
            rs = db.ResultadoSelect("datospregunta", "DPF");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DPF", pg);

            lbl_DPF_titulo1.setText(pg.tit1);
            lbl_DPF_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DPG 
            rs = db.ResultadoSelect("datospregunta", "DPG");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DPG", pg);

            lbl_DPG_titulo1.setText(pg.tit1);
            lbl_DPG_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DPK 
            rs = db.ResultadoSelect("datospregunta", "DPK");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DPK", pg);

            lbl_DPK_titulo1.setText(pg.tit1);
            lbl_DPK_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DPN 
            rs = db.ResultadoSelect("datospregunta", "DPN");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DPN", pg);

            lbl_DPN_titulo1.setText(pg.tit1);
            lbl_DPN_titulo2.setText(rs.getString("titulo2"));
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DAH  
            rs = db.ResultadoSelect("datospregunta", "DAH");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DAH", pg);

            lbl_DAH_titulo1.setText(pg.tit1);
            lbl_DAH_titulo2.setText(rs.getString("titulo2"));
            lbl_DAH_desc.setText(pg.desc);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DAB  
            rs = db.ResultadoSelect("datospregunta", "DAB");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DAB", pg);

            lbl_DAB_titulo1.setText(pg.tit1);
            lbl_DAB_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_DAB_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 440, 300);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DVF 
            rs = db.ResultadoSelect("datospregunta", "DVF");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DVF", pg);

            lbl_DVF_titulo1.setText(pg.tit1);
            lbl_DVF_titulo2.setText(rs.getString("titulo2"));
            txt_DVF_Pregunta.setText(rs.getString("valorpordefecto"));

            txt_DVF_Pregunta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("DVF");
                }
            });

            AsignarPopupBtn(btn_DVF_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 410, 210);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DLD 
            rs = db.ResultadoSelect("datospregunta", "DLD");

            pg = new Pregunta();
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DLD", pg);

            lbl_DLD_titulo1.setText(pg.tit1);
            lbl_DLD_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_DLD_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 410, 210);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DAL 

            rs = db.ResultadoSelect("datospregunta", "DAL");

            pg = new Pregunta(); //Objeto que será creado con datos, básicos de rangos y mensajes
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DAL", pg);

            lbl_DAL_titulo1.setText(pg.tit1);
            lbl_DAL_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_DAL_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 390, 250);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DEM 
            rs = db.ResultadoSelect("datospregunta", "DEM");

            pg = new Pregunta(); //Objeto que será creado con datos, básicos de rangos y mensajes
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DEM", pg);

            lbl_DEM_titulo1.setText(pg.tit1);
            lbl_DEM_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_DEM_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 390, 270);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DFL 
            rs = db.ResultadoSelect("datospregunta", "DFL");

            pg = new Pregunta(); //Objeto que será creado con datos, básicos de rangos y mensajes
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DFL", pg);

            lbl_DFL_titulo1.setText(pg.tit1);
            lbl_DFL_titulo2.setText(rs.getString("titulo2"));

            txt_DFL_Pregunta.addFocusListener(new FocusAdapter() {
                public void focusLost(FocusEvent e) {
                    ejecutarFunciones("DFL");
                }
            });

            txt_DFL_Pregunta.setText(rs.getString("valorpordefecto"));
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DUP 
            rs = db.ResultadoSelect("datospregunta", "DUP");

            pg = new Pregunta(); //Objeto que será creado con datos, básicos de rangos y mensajes
            pg.deci = rs.getInt("decimales");
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DUP", pg);

            lbl_DUP_titulo1.setText(pg.tit1);
            lbl_DUP_titulo2.setText(rs.getString("titulo2"));

            AsignarPopupBtn(btn_DUP_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 390, 340);

            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
            // Si Bod cargó datos de Laguna Anaerobia desde la BD, porque Ya estaba editada, se proceden a llenar casillas con datos
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -          
            if (bod.EditDesarenador) {
                ddm_DAN_pregunta.setSelectedIndex(pos);
                txt_DCN_Pregunta.setText(bod.getVarFormateadaPorNombre("DCN", map.get("DCN").deci));
                txt_DCK_Pregunta.setText(bod.getVarFormateadaPorNombre("DCK", map.get("DCK").deci));
                txt_DP1_Pregunta.setText(bod.getVarFormateadaPorNombre("DP1", map.get("DP1").deci));
                txt_DP2_Pregunta.setText(bod.getVarFormateadaPorNombre("DP2", map.get("DP2").deci));
                txt_DP3_Pregunta.setText(bod.getVarFormateadaPorNombre("DP3", map.get("DP3").deci));
                txt_DRZ_Pregunta.setText(bod.getVarFormateadaPorNombre("DRZ", map.get("DRZ").deci));
                txt_DPA_Pregunta.setText(bod.getVarFormateadaPorNombre("DPA", map.get("DPA").deci));
                txt_DPB_Pregunta.setText(bod.getVarFormateadaPorNombre("DPB", map.get("DPB").deci));
                txt_DPC_Pregunta.setText(bod.getVarFormateadaPorNombre("DPC", map.get("DPC").deci));
                txt_DPD_Pregunta.setText(bod.getVarFormateadaPorNombre("DPD", map.get("DPD").deci));
                txt_DPE_Pregunta.setText(bod.getVarFormateadaPorNombre("DPE", map.get("DPE").deci));
                txt_DPF_Pregunta.setText(bod.getVarFormateadaPorNombre("DPF", map.get("DPF").deci));
                txt_DPG_Pregunta.setText(bod.getVarFormateadaPorNombre("DPG", map.get("DPG").deci));
                txt_DPK_Pregunta.setText(bod.getVarFormateadaPorNombre("DPK", map.get("DPK").deci));
                txt_DPN_Pregunta.setText(bod.getVarFormateadaPorNombre("DPN", map.get("DPN").deci));
                txt_DAH_Pregunta.setText(bod.getVarFormateadaPorNombre("DAH", map.get("DAH").deci));
                txt_DAB_Pregunta.setText(bod.getVarFormateadaPorNombre("DAB", map.get("DAB").deci));
                txt_DVF_Pregunta.setText(bod.getVarFormateadaPorNombre("DVF", map.get("DVF").deci));
                txt_DLD_Pregunta.setText(bod.getVarFormateadaPorNombre("DLD", map.get("DLD").deci));
                txt_DAL_Pregunta.setText(bod.getVarFormateadaPorNombre("DAL", map.get("DAL").deci));
                txt_DEM_Pregunta.setText(bod.getVarFormateadaPorNombre("DEM", map.get("DEM").deci));
                txt_DFL_Pregunta.setText(bod.getVarFormateadaPorNombre("DFL", map.get("DFL").deci));
                txt_DUP_Pregunta.setText(bod.getVarFormateadaPorNombre("DUP", map.get("DUP").deci));
            } else {// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -si no, otras tareas de cálculos que se deben hacer al cargar              
                ddm_DAN_pregunta.setSelectedIndex(pos);
                ejecutarFunciones("DAN");
                ejecutarFunciones("DP1");
                ejecutarFunciones("DVF");
                ejecutarFunciones("DEM");
                ejecutarFunciones("DFL");

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
        lbl_DAB_error.setText("");
        lbl_DAH_error.setText("");
        lbl_DAL_error.setText("");
        lbl_DAN_error.setText("");
        lbl_DCK_error.setText("");
        lbl_DCN_error.setText("");
        //lbl_DEM_error.setText("");
        lbl_DFL_error.setText("");
        lbl_DLD_error.setText("");
        lbl_DP1_error.setText("");
        lbl_DP2_error.setText("");
        lbl_DP3_error.setText("");
        lbl_DPAN_error.setText("");
        lbl_DRZ_error.setText("");
        lbl_DUP_error.setText("");
        lbl_DVF_error.setText("");
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
            lbl_save_desc.setText("");

            if (!CalcularDAN()) {
                lbl_save_error.setText(map.get("DAN").erro);
                lbl_save_desc.setText(map.get("DAN").desc);
                lbl_save_error.setText(map.get("DAN").tit1);
                return false;
            }

            if (!CalcularDCNDCK()) {
                lbl_save_error.setText(map.get("DCN").erro);
                lbl_save_desc.setText(map.get("DCN").desc);
                lbl_save_titulo1.setText(map.get("DCN").tit1);
                return false;
            }

            if (!CalcularDP1()) {
                lbl_save_error.setText(map.get("DP1").erro);
                lbl_save_desc.setText(map.get("DP1").desc);
                lbl_save_titulo1.setText(map.get("DP1").tit1);
                return false;
            }

            if (!CalcularDP2()) {
                lbl_save_error.setText(map.get("DP2").erro);
                lbl_save_desc.setText(map.get("DP2").desc);
                lbl_save_titulo1.setText(map.get("DP2").tit1);
                return false;
            }

            if (!CalcularDP3()) {
                lbl_save_error.setText(map.get("DP3").erro);
                lbl_save_desc.setText(map.get("DP3").desc);
                lbl_save_titulo1.setText(map.get("DP3").tit1);
                return false;
            }

            if (!CalcularDRZ()) {
                lbl_save_error.setText(map.get("DRZ").erro);
                lbl_save_desc.setText(map.get("DRZ").desc);
                lbl_save_titulo1.setText(map.get("DRZ").tit1);
                return false;
            }

            if (!CalcularDPABCDEFGKN()) {//DPA, DPB, DPC, DPD, DPE, DPF, DPG, DPK, DPN 
                lbl_save_error.setText(map.get("DPA").erro);
                lbl_save_desc.setText(map.get("DPA").desc);
                lbl_save_titulo1.setText(map.get("DPA").tit1);
                return false;
            }

            if (!CalcularDAH()) {
                lbl_save_error.setText(map.get("DAH").erro);
                lbl_save_desc.setText(map.get("DAH").desc);
                lbl_save_titulo1.setText(map.get("DAH").tit1);
                return false;
            }

            if (!CalcularDVF()) {
                lbl_save_error.setText(map.get("DVF").erro);
                lbl_save_desc.setText(map.get("DVF").desc);
                lbl_save_titulo1.setText(map.get("DVF").tit1);
                return false;
            }

            if (!CalcularDAB()) {
                lbl_save_error.setText(map.get("DAB").erro);
                lbl_save_desc.setText(map.get("DAB").desc);
                lbl_save_titulo1.setText(map.get("DAB").tit1);
                return false;
            }

            if (!CalcularDLD()) {
                lbl_save_error.setText(map.get("DLD").erro);
                lbl_save_desc.setText(map.get("DLD").desc);
                lbl_save_titulo1.setText(map.get("DLD").tit1);
                return false;
            }

            if (!CalcularDAL()) {
                lbl_save_error.setText(map.get("DAL").erro);
                lbl_save_desc.setText(map.get("DAL").desc);
                lbl_save_titulo1.setText(map.get("DAL").tit1);
                return false;
            }

            if (!CalcularDEM()) {
                lbl_save_error.setText(map.get("DEM").erro);
                lbl_save_desc.setText(map.get("DEM").desc);
                lbl_save_titulo1.setText(map.get("DEM").tit1);
                return false;
            }

            if (!CalcularDFL()) {
                lbl_save_error.setText(map.get("DFL").erro);
                lbl_save_desc.setText(map.get("DFL").desc);
                lbl_save_titulo1.setText(map.get("DFL").tit1);
                return false;
            }

            if (!CalcularDUP()) {
                lbl_save_error.setText(map.get("DUP").erro);
                lbl_save_desc.setText(map.get("DUP").desc);
                lbl_save_titulo1.setText(map.get("DUP").tit1);
                return false;
            }

            bod.EditDesarenador = true;
            Main main = (Main) this.getTopLevelAncestor(); // Trata de ir  al contenedor principal 
            main.cancela = false;
            main.rejilladesarena = true;
            main.vbod = this.bod;

            if (!main.ComprobarCambio(6, true)) {//Comprobar cambios que afecten datos posteriores, si este guardar es de una edición.

                if (!main.cancela) {
                    bod.EditDesarenador = false;
                }
                main.rejilladesarena = false;
                main.cancela = false;
                main.vbod = null;
                return false;
            }

            if (bod.getVarInt("RNB") <= 1) {//Todo: de la bd
                util.Mensaje("Debido al bajo caudal, se recomienda considerar otros \n sistemas compactos de tratamiento preliminar", "ok");
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

    private boolean CalcularDAN() {

        try {
            lbl_DAN_error.setText("");

            String x = ddm_DAN_pregunta.getSelectedItem().toString();

            if (bod.setVarDob(x, "DAN", map.get("DAN").rmin, map.get("DAN").rmax)) {
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularDAN " + ex.getMessage());
        }
        lbl_DAN_error.setText(map.get("DAN").erro);
        return false;
    }

    private boolean CalcularDCNDCK() {

        try {
            lbl_DCK_error.setText("");
            lbl_DCN_error.setText("");
            txt_DCK_Pregunta.setText("");
            txt_DCN_Pregunta.setText("");

            String[] x = bod.calcularDCNDCK().split("\\|");

            if (bod.setVarDob(x[1], "DCN", map.get("DCN").rmin, map.get("DCN").rmax)) {
                txt_DCN_Pregunta.setText(x[1]);

                if (bod.setVarDob(x[2], "DCK", map.get("DCK").rmin, map.get("DCK").rmax)) {
                    txt_DCK_Pregunta.setText(x[2]);
                    return true;
                } else {
                    lbl_DCK_error.setText(map.get("DCK").erro);
                }
            } else {
                lbl_DCN_error.setText(map.get("DCN").erro);
            }

        } catch (Exception ex) {
            log.error("Error en calcularDCK " + ex.getMessage());
            lbl_DCK_error.setText("Error de calculo");
            lbl_DCN_error.setText("Error de calculo");
        }
        return false;
    }

    private boolean CalcularDP1() {

        try {
            lbl_DP1_error.setText("");
            double x = bod.calcularDP1();

            if (bod.setVarDob(x, "DP1", map.get("DP1").rmin, map.get("DP1").rmax)) {
                txt_DP1_Pregunta.setText(bod.getVarFormateadaPorNombre("DP1", map.get("DP1").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularDP1 " + ex.getMessage());
        }
        lbl_DP1_error.setText(map.get("DP1").erro);
        return false;
    }

    private boolean CalcularDP2() {

        try {
            lbl_DP2_error.setText("");
            double x = bod.calcularDP2();

            if (bod.setVarDob(x, "DP2", map.get("DP2").rmin, map.get("DP2").rmax)) {
                txt_DP2_Pregunta.setText(bod.getVarFormateadaPorNombre("DP2", map.get("DP2").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularDP2 " + ex.getMessage());
        }
        lbl_DP2_error.setText(map.get("DP2").erro);
        return false;
    }

    private boolean CalcularDP3() {

        try {
            lbl_DP3_error.setText("");
            double x = bod.calcularDP3();

            if (bod.setVarDob(x, "DP3", map.get("DP3").rmin, map.get("DP3").rmax)) {
                txt_DP3_Pregunta.setText(bod.getVarFormateadaPorNombre("DP3", map.get("DP3").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularDP3 " + ex.getMessage());
        }
        lbl_DP3_error.setText(map.get("DP3").erro);
        return false;
    }

    private boolean CalcularDRZ() {

        try {
            lbl_DRZ_error.setText("");
            double x = Math.abs(bod.calcularDRZ());

            if (bod.setVarDob(x, "DRZ", map.get("DRZ").rmin, map.get("DRZ").rmax)) {
                txt_DRZ_Pregunta.setText(bod.getVarFormateadaPorNombre("DRZ", map.get("DRZ").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularDRZ " + ex.getMessage());
        }
        lbl_DRZ_error.setText(map.get("DRZ").erro);
        return false;
    }

    /**
     * Calcular DPA,DPB,DPC,DPD,DPE,DPF,DPG,DPK,DPN
     */
    private boolean CalcularDPABCDEFGKN() {

        try {
            lbl_DPAN_error.setText("");
            lbl_DPAN_error.setText("");

            String[] x = bod.calcularDPABCDEFGKN().split("\\|");

            txt_DPA_Pregunta.setText("");
            if (bod.setVarDob(x[2], "DPA", map.get("DPA").rmin, map.get("DPA").rmax)) {
                txt_DPA_Pregunta.setText(x[2]);
            } else {
                return false;
            }
            txt_DPB_Pregunta.setText("");
            if (bod.setVarDob(x[3], "DPB", map.get("DPB").rmin, map.get("DPB").rmax)) {
                txt_DPB_Pregunta.setText(x[3]);
            } else {
                return false;
            }
            txt_DPC_Pregunta.setText("");
            if (bod.setVarDob(x[4], "DPC", map.get("DPC").rmin, map.get("DPC").rmax)) {
                txt_DPC_Pregunta.setText(x[4]);
            } else {
                return false;
            }
            txt_DPD_Pregunta.setText("");
            if (bod.setVarDob(x[5], "DPD", map.get("DPD").rmin, map.get("DPD").rmax)) {
                txt_DPD_Pregunta.setText(x[5]);
            } else {
                return false;
            }
            txt_DPE_Pregunta.setText("");
            if (bod.setVarDob(x[6], "DPE", map.get("DPE").rmin, map.get("DPE").rmax)) {
                txt_DPE_Pregunta.setText(x[6]);
            } else {
                return false;
            }
            txt_DPF_Pregunta.setText("");
            if (bod.setVarDob(x[7], "DPF", map.get("DPF").rmin, map.get("DPF").rmax)) {
                txt_DPF_Pregunta.setText(x[7]);
            } else {
                return false;
            }
            txt_DPG_Pregunta.setText("");
            if (bod.setVarDob(x[8], "DPG", map.get("DPG").rmin, map.get("DPG").rmax)) {
                txt_DPG_Pregunta.setText(x[8]);
            } else {
                return false;
            }
            txt_DPK_Pregunta.setText("");
            if (bod.setVarDob(x[9], "DPK", map.get("DPK").rmin, map.get("DPK").rmax)) {
                txt_DPK_Pregunta.setText(x[9]);
            } else {
                return false;
            }
            txt_DPN_Pregunta.setText("");
            if (bod.setVarDob(x[10], "DPN", map.get("DPN").rmin, map.get("DPN").rmax)) {
                txt_DPN_Pregunta.setText(x[10]);
            } else {
                return false;
            }

            return true;
        } catch (Exception ex) {
            log.error("Error en calcularDCK " + ex.getMessage());
        }
        lbl_DPAN_error.setText(map.get("DPA").erro);
        return false;
    }

    private boolean CalcularDAH() {

        try {
            lbl_DAH_error.setText("");
            double x = bod.calcularDAH();

            if (bod.setVarDob(x, "DAH", map.get("DAH").rmin, map.get("DAH").rmax)) {
                txt_DAH_Pregunta.setText(bod.getVarFormateadaPorNombre("DAH", map.get("DAH").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularDAH " + ex.getMessage());
        }
        lbl_DAH_error.setText(map.get("DAH").erro);
        return false;
    }

    private boolean CalcularDVF() {

        try {
            lbl_DVF_error.setText("");

            if (bod.setVarDob(txt_DVF_Pregunta.getText(), "DVF", map.get("DVF").rmin, map.get("DVF").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularDVF " + ex.getMessage());
        }
        lbl_DVF_error.setText(map.get("DVF").erro);
        return false;
    }

    private boolean CalcularDAB() {

        try {
            lbl_DAB_error.setText("");
            double x = bod.calcularDAB();

            if (bod.setVarDob(x, "DAB", map.get("DAB").rmin, map.get("DAB").rmax)) {
                txt_DAB_Pregunta.setText(bod.getVarFormateadaPorNombre("DAB", map.get("DAB").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularDAB " + ex.getMessage());
        }
        lbl_DAB_error.setText(map.get("DAB").erro);
        return false;
    }

    private boolean CalcularDLD() {

        try {
            lbl_DLD_error.setText("");
            double x = bod.calcularDLD();

            if (bod.setVarDob(x, "DLD", map.get("DLD").rmin, map.get("DLD").rmax)) {
                txt_DLD_Pregunta.setText(bod.getVarFormateadaPorNombre("DLD", map.get("DLD").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularDLD " + ex.getMessage());
        }
        lbl_DLD_error.setText(map.get("DLD").erro);
        return false;
    }

    private boolean CalcularDAL() {

        try {
            lbl_DAL_error.setText("");
            double x = bod.calcularDAL();

            if (bod.setVarDob(x, "DAL", map.get("DAL").rmin, map.get("DAL").rmax)) {
                txt_DAL_Pregunta.setText(bod.getVarFormateadaPorNombre("DAL", map.get("DAL").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularDAL " + ex.getMessage());
        }
        lbl_DAL_error.setText(map.get("DAL").erro);
        return false;
    }

    private boolean CalcularDEM() {

        try {
            lbl_DEM_error.setText("");
            double x = bod.calcularDEM();

            if (bod.setVarDob(x, "DEM", map.get("DEM").rmin, map.get("DEM").rmax)) {
                txt_DEM_Pregunta.setText(bod.getVarFormateadaPorNombre("DEM", map.get("DEM").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularDEM " + ex.getMessage());
        }
        lbl_DEM_error.setText(map.get("DEM").erro);
        return false;
    }

    private boolean CalcularDFL() {

        try {
            lbl_DFL_error.setText("");

            if (bod.setVarInt(txt_DFL_Pregunta.getText(), "DFL", (int) map.get("DFL").rmin, (int) map.get("DFL").rmax)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error: CalcularDFL " + ex.getMessage());
        }
        lbl_DFL_error.setText(map.get("DFL").erro);
        return false;
    }

    private boolean CalcularDUP() {

        try {
            lbl_DUP_error.setText("");
            double x = bod.calcularDUP();

            if (bod.setVarDob(x, "DUP", map.get("DUP").rmin, map.get("DUP").rmax)) {
                txt_DUP_Pregunta.setText(bod.getVarFormateadaPorNombre("DUP", map.get("DUP").deci));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error: CalcularDUP " + ex.getMessage());
        }
        lbl_DUP_error.setText(map.get("DUP").erro);
        return false;
    }

    /**
     * Ejecuta varios metodos que las casillas Inactivas deben ejecutar
     *
     * @param dxx ...Puede ser DAN DEM , etc
     */
    public void ejecutarFunciones(String dxx) {
        eSave = false;
        switch (dxx) {
            case "DAN":
                CalcularDAN();
                ejecutarFunciones("DCN");
                ejecutarFunciones("DPA");
                break;

            case "DCN":
                CalcularDCNDCK();
                ejecutarFunciones("DP1");
                break;

            case "DP1":
                CalcularDP1();
                CalcularDP2();
                CalcularDP3();
                ejecutarFunciones("DRZ");
                break;

            case "DRZ":
                CalcularDRZ();
                ejecutarFunciones("DAH");
                break;

            case "DPA":
                CalcularDPABCDEFGKN();
                break;

            case "DAH":
                CalcularDAH();
                ejecutarFunciones("DAB");
                ejecutarFunciones("DLD");
                break;

            case "DVF":
                CalcularDVF();
                ejecutarFunciones("DAB");
                break;

            case "DAB":
                CalcularDAB();
                ejecutarFunciones("DAL");
                break;

            case "DLD":
                CalcularDLD();
                ejecutarFunciones("DAL");
                break;

            case "DAL":
                CalcularDAL();
                ejecutarFunciones("DUP");
                break;

            case "DEM":
                CalcularDEM();
                ejecutarFunciones("DUP");
                break;

            case "DFL":
                CalcularDFL();
                ejecutarFunciones("DUP");
                break;

            case "DUP":
                CalcularDUP();
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
        bod.WinDesarenador = false;
        Main main = (Main) this.getTopLevelAncestor();
        main.bod.WinDesarenador = false;
        if(esGuia) main.guiaUsuario2();
        this.dispose();
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
        lbl_save_desc = new javax.swing.JLabel();
        lbl_DAN_error = new javax.swing.JLabel();
        lbl_DAN_titulo2 = new javax.swing.JLabel();
        lbl_DAN_titulo1 = new javax.swing.JLabel();
        txt_DPB_Pregunta = new javax.swing.JTextField();
        lbl_DPB_titulo1 = new javax.swing.JLabel();
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
        txt_Q3C_Pregunta = new javax.swing.JTextField();
        lbl_Q3C_titulo2 = new javax.swing.JLabel();
        lbl_DPAN_error = new javax.swing.JLabel();
        btn_DP1_ayuda = new javax.swing.JButton();
        btn_DAN_ayuda = new javax.swing.JButton();
        btn_DRZ_ayuda = new javax.swing.JButton();
        btn_DCK_ayuda = new javax.swing.JButton();
        btn_DPA_ayuda = new javax.swing.JButton();
        btn_DAB_ayuda = new javax.swing.JButton();
        btn_DVF_ayuda = new javax.swing.JButton();
        btn_DLD_ayuda = new javax.swing.JButton();
        btn_DAL_ayuda = new javax.swing.JButton();
        btn_DEM_ayuda = new javax.swing.JButton();
        btn_DUP_ayuda = new javax.swing.JButton();
        ddm_DAN_pregunta = new javax.swing.JComboBox();
        lbl_save_titulo1 = new javax.swing.JLabel();
        btn_close2 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1024, 1500));

        jsp_Desarenador.setPreferredSize(new java.awt.Dimension(1024, 1200));

        jp_Componentes.setPreferredSize(new java.awt.Dimension(1024, 1400));
        jp_Componentes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jp_Componentes.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 42, 1061, 2));

        lbl_titulo1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_titulo1.setForeground(new java.awt.Color(51, 51, 51));
        lbl_titulo1.setText("Titulo");
        jp_Componentes.add(lbl_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 300, 30));

        lbl_titulo2.setForeground(new java.awt.Color(0, 51, 102));
        lbl_titulo2.setText("Desc");
        jp_Componentes.add(lbl_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, 600, 30));

        btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Button_Salva_Guardar_21.png"))); // NOI18N
        btn_guardar.setText("save");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1180, 120, -1));
        jp_Componentes.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 1071, 2));

        lbl_Q3C_titulo1.setText("Titulo");
        lbl_Q3C_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q3C_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 300, 30));

        lbl_Q1C_titulo1.setText("Titulo");
        lbl_Q1C_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q1C_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 300, 30));

        txt_Q1C_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q1C_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 100, 130, 25));

        lbl_Q1C_titulo2.setText("Titulo");
        lbl_Q1C_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q1C_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 100, 80, 25));

        btn_close.setText("Salir");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1220, 120, 30));

        lbl_save_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error.setText(" . . .");
        jp_Componentes.add(lbl_save_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 1180, 500, 35));

        lbl_save_desc.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_desc.setText(" . . .");
        jp_Componentes.add(lbl_save_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 1220, 610, 35));

        lbl_DAN_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DAN_error.setText(" . . .");
        jp_Componentes.add(lbl_DAN_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 230, 340, 35));

        lbl_DAN_titulo2.setText("Titulo");
        lbl_DAN_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DAN_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 230, 80, 25));

        lbl_DAN_titulo1.setText("Titulo");
        lbl_DAN_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DAN_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 300, 30));

        txt_DPB_Pregunta.setEditable(false);
        jp_Componentes.add(txt_DPB_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 650, 130, 25));

        lbl_DPB_titulo1.setText("Titulo");
        lbl_DPB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DPB_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 650, 80, 25));

        lbl_DPB_titulo2.setText("Titulo");
        lbl_DPB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DPB_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 650, 80, 25));
        jp_Componentes.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 1071, -1));

        lbl_DAN_desc.setText("Desc");
        lbl_DAN_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DAN_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 610, 30));

        lbl_DCN_titulo1.setText("Titulo");
        lbl_DCN_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DCN_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 300, 30));

        lbl_DCN_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DCN_error.setText(" . . .");
        jp_Componentes.add(lbl_DCN_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 340, 340, 35));

        lbl_DCN_titulo2.setText("Titulo");
        lbl_DCN_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DCN_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 350, 80, 25));

        txt_DCN_Pregunta.setEditable(false);
        jp_Componentes.add(txt_DCN_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 350, 130, 25));

        lbl_DP1_titulo1.setText("Titulo");
        lbl_DP1_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DP1_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 300, 30));

        lbl_DP1_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DP1_error.setText(" . . .");
        jp_Componentes.add(lbl_DP1_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 380, 340, 35));

        lbl_DP1_titulo2.setText("Titulo");
        lbl_DP1_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DP1_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 390, 80, 25));

        txt_DP1_Pregunta.setEditable(false);
        jp_Componentes.add(txt_DP1_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 390, 130, 25));

        lbl_DAH_titulo2.setText("Titulo");
        lbl_DAH_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DAH_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 830, 80, 25));

        lbl_DAH_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DAH_error.setText(" . . .");
        jp_Componentes.add(lbl_DAH_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 820, 340, 35));

        lbl_DAH_titulo1.setText("Titulo");
        lbl_DAH_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DAH_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 830, 300, 30));

        txt_DAH_Pregunta.setEditable(false);
        jp_Componentes.add(txt_DAH_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 830, 130, 25));

        lbl_DPA_titulo2.setText("Titulo");
        lbl_DPA_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DPA_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 610, 80, 25));

        lbl_DPA_titulo1.setText("Titulo");
        lbl_DPA_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DPA_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 610, 80, 25));

        txt_DPA_Pregunta.setEditable(false);
        jp_Componentes.add(txt_DPA_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 610, 130, 25));

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
        jp_Componentes.add(lbl_REB_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(1041, 300, 20, 20));

        lbl_DCK_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DCK_error.setText(" . . .");
        jp_Componentes.add(lbl_DCK_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 300, 340, 35));

        txt_DCK_Pregunta.setEditable(false);
        jp_Componentes.add(txt_DCK_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 310, 130, 25));

        lbl_DCK_titulo1.setText("Titulo");
        lbl_DCK_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DCK_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 300, 30));

        lbl_DCK_titulo2.setText("Titulo");
        lbl_DCK_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DCK_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 310, 80, 25));

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
        jp_Componentes.add(lbl_RAT_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(1041, 627, 20, 20));

        lbl_DPA_desc.setText("Desc");
        lbl_DPA_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DPA_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 560, 610, 30));
        jp_Componentes.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 767, 1061, 2));

        lbl_DAB_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DAB_error.setText(" . . .");
        jp_Componentes.add(lbl_DAB_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 860, 340, 35));

        lbl_DAB_titulo2.setText("Titulo");
        lbl_DAB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DAB_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 870, 80, 25));

        txt_DAB_Pregunta.setEditable(false);
        jp_Componentes.add(txt_DAB_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 870, 130, 25));

        lbl_DAB_titulo1.setText("Titulo");
        lbl_DAB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DAB_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 870, 300, 30));
        jp_Componentes.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1160, 1061, -1));

        lbl_Q2C_titulo1.setText("Titulo");
        lbl_Q2C_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2C_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 300, 30));

        txt_Q2C_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q2C_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 140, 130, 25));

        lbl_Q2C_titulo2.setText("Titulo");
        lbl_Q2C_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2C_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 140, 80, 25));

        lbl_DCK_desc.setText("Desc");
        lbl_DCK_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DCK_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 610, 30));

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
        jp_Componentes.add(lbl_REB_ayuda1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1041, 421, 20, 20));

        txt_DRZ_Pregunta.setEditable(false);
        jp_Componentes.add(txt_DRZ_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 510, 130, 25));

        lbl_DRZ_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DRZ_error.setText(" . . .");
        jp_Componentes.add(lbl_DRZ_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 500, 340, 35));

        lbl_DRZ_titulo2.setText("Titulo");
        lbl_DRZ_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DRZ_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 510, 80, 25));

        txt_DP3_Pregunta.setEditable(false);
        jp_Componentes.add(txt_DP3_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 470, 130, 25));

        lbl_DRZ_titulo1.setText("Titulo");
        lbl_DRZ_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DRZ_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 510, 300, 30));

        lbl_DP3_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DP3_error.setText(" . . .");
        jp_Componentes.add(lbl_DP3_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 460, 340, 35));

        lbl_DP3_titulo2.setText("Titulo");
        lbl_DP3_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DP3_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 470, 80, 25));

        lbl_DP3_titulo1.setText("Titulo");
        lbl_DP3_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DP3_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 470, 300, 30));

        lbl_DP2_titulo2.setText("Titulo");
        lbl_DP2_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DP2_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 430, 80, 25));

        lbl_DP2_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DP2_error.setText(" . . .");
        jp_Componentes.add(lbl_DP2_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 420, 340, 35));

        lbl_DP2_titulo1.setText("Titulo");
        lbl_DP2_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DP2_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 300, 30));

        txt_DP2_Pregunta.setEditable(false);
        jp_Componentes.add(txt_DP2_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 430, 130, 25));

        txt_DPC_Pregunta.setEditable(false);
        jp_Componentes.add(txt_DPC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 690, 130, 25));

        lbl_DPC_titulo2.setText("Titulo");
        lbl_DPC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DPC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 690, 80, 25));

        lbl_DPC_titulo1.setText("Titulo");
        lbl_DPC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DPC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 690, 80, 25));

        lbl_DPD_titulo2.setText("Titulo");
        lbl_DPD_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DPD_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 610, 80, 25));

        txt_DPD_Pregunta.setEditable(false);
        jp_Componentes.add(txt_DPD_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 610, 130, 25));

        lbl_DPD_titulo1.setText("Titulo");
        lbl_DPD_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DPD_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 610, 80, 25));

        lbl_DPF_titulo2.setText("Titulo");
        lbl_DPF_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DPF_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 690, 80, 25));

        txt_DPF_Pregunta.setEditable(false);
        jp_Componentes.add(txt_DPF_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 690, 130, 25));

        lbl_DPF_titulo1.setText("Titulo");
        lbl_DPF_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DPF_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 690, 80, 25));

        txt_DPE_Pregunta.setEditable(false);
        jp_Componentes.add(txt_DPE_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 650, 130, 25));

        lbl_DPE_titulo2.setText("Titulo");
        lbl_DPE_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DPE_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 650, 80, 25));

        lbl_DPE_titulo1.setText("Titulo");
        lbl_DPE_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DPE_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 650, 80, 25));

        lbl_DPK_titulo2.setText("Titulo");
        lbl_DPK_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DPK_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 650, 80, 25));

        lbl_DPK_titulo1.setText("Titulo");
        lbl_DPK_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DPK_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 650, 80, 25));

        lbl_DPN_titulo1.setText("Titulo");
        lbl_DPN_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DPN_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 690, 80, 25));

        txt_DPK_Pregunta.setEditable(false);
        jp_Componentes.add(txt_DPK_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 650, 130, 25));

        lbl_DPN_titulo2.setText("Titulo");
        lbl_DPN_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DPN_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 690, 80, 25));

        txt_DPN_Pregunta.setEditable(false);
        jp_Componentes.add(txt_DPN_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 690, 130, 25));

        txt_DPG_Pregunta.setEditable(false);
        jp_Componentes.add(txt_DPG_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 610, 130, 25));

        lbl_DPG_titulo2.setText("Titulo");
        lbl_DPG_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DPG_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 610, 80, 25));

        lbl_DPG_titulo1.setText("Titulo");
        lbl_DPG_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DPG_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 610, 80, 25));

        lbl_DAH_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_DAH_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_DAH_desc.setText("Desc");
        lbl_DAH_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DAH_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 780, 610, 30));

        lbl_DVF_titulo2.setText("Titulo");
        lbl_DVF_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DVF_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 910, 80, 25));

        lbl_DVF_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DVF_error.setText(" . . .");
        jp_Componentes.add(lbl_DVF_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 900, 340, 35));

        lbl_DVF_titulo1.setText("Titulo");
        lbl_DVF_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DVF_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 910, 300, 30));

        txt_DVF_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_DVF_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 910, 130, 25));

        lbl_DLD_titulo1.setText("Titulo");
        lbl_DLD_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DLD_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 950, 300, 30));

        txt_DLD_Pregunta.setEditable(false);
        jp_Componentes.add(txt_DLD_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 950, 130, 25));

        lbl_DLD_titulo2.setText("Titulo");
        lbl_DLD_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DLD_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 950, 80, 25));

        lbl_DLD_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DLD_error.setText(" . . .");
        jp_Componentes.add(lbl_DLD_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 940, 340, 35));

        lbl_DAL_titulo2.setText("Titulo");
        lbl_DAL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DAL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 990, 80, 25));

        lbl_DAL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DAL_error.setText(" . . .");
        jp_Componentes.add(lbl_DAL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 980, 340, 35));

        lbl_DAL_titulo1.setText("Titulo");
        lbl_DAL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DAL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 990, 300, 30));

        txt_DAL_Pregunta.setEditable(false);
        jp_Componentes.add(txt_DAL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 990, 130, 25));

        lbl_DFL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DFL_error.setText(" . . .");
        jp_Componentes.add(lbl_DFL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1060, 340, 35));

        lbl_DFL_titulo1.setText("Titulo");
        lbl_DFL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DFL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1070, 300, 30));

        lbl_DFL_titulo2.setText("Titulo");
        lbl_DFL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DFL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1070, 80, 25));

        txt_DUP_Pregunta.setEditable(false);
        jp_Componentes.add(txt_DUP_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1110, 130, 25));

        lbl_DUP_titulo1.setText("Titulo");
        lbl_DUP_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DUP_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1110, 300, 30));

        txt_DFL_Pregunta.setBackground(new java.awt.Color(230, 241, 255));
        jp_Componentes.add(txt_DFL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1070, 130, 25));

        lbl_DEM_titulo1.setText("Titulo");
        lbl_DEM_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DEM_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1030, 300, 30));

        txt_DEM_Pregunta.setEditable(false);
        jp_Componentes.add(txt_DEM_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1030, 130, 25));

        lbl_DUP_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DUP_error.setText(" . . .");
        jp_Componentes.add(lbl_DUP_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1100, 340, 35));

        lbl_DUP_titulo2.setText("Titulo");
        lbl_DUP_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DUP_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1110, 80, 25));

        lbl_DEM_titulo2.setText("Titulo");
        lbl_DEM_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_DEM_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1030, 80, 25));

        lbl_DEM_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DEM_error.setText(" . . .");
        jp_Componentes.add(lbl_DEM_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1020, 340, 35));

        txt_Q3C_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q3C_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 60, 130, 25));

        lbl_Q3C_titulo2.setText("Titulo");
        lbl_Q3C_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q3C_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 60, 80, 25));

        lbl_DPAN_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_DPAN_error.setText(" . . .");
        jp_Componentes.add(lbl_DPAN_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 720, 474, 35));

        btn_DP1_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_DP1_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_DP1_ayuda.setContentAreaFilled(false);
        btn_DP1_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_DP1_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 390, 30, 25));

        btn_DAN_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_DAN_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_DAN_ayuda.setContentAreaFilled(false);
        btn_DAN_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_DAN_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 190, 25, 25));

        btn_DRZ_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_DRZ_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_DRZ_ayuda.setContentAreaFilled(false);
        btn_DRZ_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_DRZ_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 510, 25, 25));

        btn_DCK_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_DCK_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_DCK_ayuda.setContentAreaFilled(false);
        btn_DCK_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_DCK_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 270, 25, 25));

        btn_DPA_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_DPA_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_DPA_ayuda.setContentAreaFilled(false);
        btn_DPA_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_DPA_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 560, 25, 25));

        btn_DAB_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_DAB_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_DAB_ayuda.setContentAreaFilled(false);
        btn_DAB_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_DAB_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 870, 25, 25));

        btn_DVF_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_DVF_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_DVF_ayuda.setContentAreaFilled(false);
        btn_DVF_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_DVF_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 910, 25, 25));

        btn_DLD_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_DLD_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_DLD_ayuda.setContentAreaFilled(false);
        btn_DLD_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_DLD_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 950, 25, 25));

        btn_DAL_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_DAL_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_DAL_ayuda.setContentAreaFilled(false);
        btn_DAL_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_DAL_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 990, 25, 25));

        btn_DEM_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_DEM_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_DEM_ayuda.setContentAreaFilled(false);
        btn_DEM_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_DEM_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1030, 25, 25));

        btn_DUP_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_DUP_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_DUP_ayuda.setContentAreaFilled(false);
        btn_DUP_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_DUP_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 1110, 25, 25));

        jp_Componentes.add(ddm_DAN_pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 230, 130, 25));

        lbl_save_titulo1.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_titulo1.setText(" . . .");
        jp_Componentes.add(lbl_save_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 1180, 300, 35));

        jsp_Desarenador.setViewportView(jp_Componentes);

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
            .addComponent(jsp_Desarenador, javax.swing.GroupLayout.DEFAULT_SIZE, 1008, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_close2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btn_close2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jsp_Desarenador, javax.swing.GroupLayout.DEFAULT_SIZE, 1427, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_closeActionPerformed
        cerrar();
    }//GEN-LAST:event_btn_closeActionPerformed

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

    private void btn_close2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_close2ActionPerformed
        cerrar();
    }//GEN-LAST:event_btn_close2ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_DAB_ayuda;
    private javax.swing.JButton btn_DAL_ayuda;
    private javax.swing.JButton btn_DAN_ayuda;
    private javax.swing.JButton btn_DCK_ayuda;
    private javax.swing.JButton btn_DEM_ayuda;
    private javax.swing.JButton btn_DLD_ayuda;
    private javax.swing.JButton btn_DP1_ayuda;
    private javax.swing.JButton btn_DPA_ayuda;
    private javax.swing.JButton btn_DRZ_ayuda;
    private javax.swing.JButton btn_DUP_ayuda;
    private javax.swing.JButton btn_DVF_ayuda;
    private javax.swing.JButton btn_close;
    private javax.swing.JButton btn_close2;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JComboBox ddm_DAN_pregunta;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JPanel jp_Componentes;
    private javax.swing.JScrollPane jsp_Desarenador;
    private javax.swing.JLabel lbl_DAB_error;
    private javax.swing.JLabel lbl_DAB_titulo1;
    private javax.swing.JLabel lbl_DAB_titulo2;
    private javax.swing.JLabel lbl_DAH_desc;
    private javax.swing.JLabel lbl_DAH_error;
    private javax.swing.JLabel lbl_DAH_titulo1;
    private javax.swing.JLabel lbl_DAH_titulo2;
    private javax.swing.JLabel lbl_DAL_error;
    private javax.swing.JLabel lbl_DAL_titulo1;
    private javax.swing.JLabel lbl_DAL_titulo2;
    private javax.swing.JLabel lbl_DAN_desc;
    private javax.swing.JLabel lbl_DAN_error;
    private javax.swing.JLabel lbl_DAN_titulo1;
    private javax.swing.JLabel lbl_DAN_titulo2;
    private javax.swing.JLabel lbl_DCK_desc;
    private javax.swing.JLabel lbl_DCK_error;
    private javax.swing.JLabel lbl_DCK_titulo1;
    private javax.swing.JLabel lbl_DCK_titulo2;
    private javax.swing.JLabel lbl_DCN_error;
    private javax.swing.JLabel lbl_DCN_titulo1;
    private javax.swing.JLabel lbl_DCN_titulo2;
    private javax.swing.JLabel lbl_DEM_error;
    private javax.swing.JLabel lbl_DEM_titulo1;
    private javax.swing.JLabel lbl_DEM_titulo2;
    private javax.swing.JLabel lbl_DFL_error;
    private javax.swing.JLabel lbl_DFL_titulo1;
    private javax.swing.JLabel lbl_DFL_titulo2;
    private javax.swing.JLabel lbl_DLD_error;
    private javax.swing.JLabel lbl_DLD_titulo1;
    private javax.swing.JLabel lbl_DLD_titulo2;
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
    private javax.swing.JLabel lbl_DRZ_error;
    private javax.swing.JLabel lbl_DRZ_titulo1;
    private javax.swing.JLabel lbl_DRZ_titulo2;
    private javax.swing.JLabel lbl_DUP_error;
    private javax.swing.JLabel lbl_DUP_titulo1;
    private javax.swing.JLabel lbl_DUP_titulo2;
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
    private javax.swing.JLabel lbl_REB_ayuda;
    private javax.swing.JLabel lbl_REB_ayuda1;
    private javax.swing.JLabel lbl_save_desc;
    private javax.swing.JLabel lbl_save_error;
    private javax.swing.JLabel lbl_save_titulo1;
    private javax.swing.JLabel lbl_titulo1;
    private javax.swing.JLabel lbl_titulo2;
    private javax.swing.JTextField txt_DAB_Pregunta;
    private javax.swing.JTextField txt_DAH_Pregunta;
    private javax.swing.JTextField txt_DAL_Pregunta;
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
