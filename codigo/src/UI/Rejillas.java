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

public class Rejillas extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("Rejillas"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.ObtenerKey("rutabasedatos") + File.separator + conf.ObtenerKey("nombrebasedatos"));
    private Validaciones validar = new Validaciones();
    private Bod bod = new Bod("");
    ButtonGroup btnGrupLLA = new ButtonGroup();
    private Listener_Popup Lpopup;
    private Listener_Texto Ltexto;
    Util util = new Util();
    ButtonGroup btngroupRBI = new ButtonGroup();
    private int Rango1RBI;
    private int Rango2RBI;
    private int Rango3RBI;
    private String Error_RBI; //Guarda la cadena original de error

    public Rejillas(Bod bod) {
        this.bod = bod;
        initComponents();
        bod.WinRejillas = true;
        InicializarComponentes();
    }

    /**
     * Inicializa todos los elementos (jlabels,jtextbox,etc.) con sus
     * respectivos valores y textos desde al base de datos.
     */
    public void InicializarComponentes() {

        ResultSet rs;
        bod.WinRejillas = true;//Bandera La ventana se ha abierto

        try {//Todo: implementar donde esta error
            btn_guardar.setText("Guardar");
            btn_close.setText("Cerrar");

            // - - - - - - Titulo de la sección            
            rs = db.ResultadoSelect("obtenerseccion", "8", null);

            lbl_titulo1.setText(rs.getString("Nombre")); //Título de este jpane
            lbl_titulo2.setText(rs.getString("Mensaje"));
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q2C
            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q2K"); //Aqui se obtiene solo el titulo 2 de Q2K em metroscubicos

            String titulo2 = rs.getString("titulo2").replace("d", "s");

            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q2C");

            lbl_Q2C_titulo1.setText(rs.getString("descripcion") + rs.getString("titulo1"));
            lbl_Q2C_titulo2.setText(titulo2);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q3C
            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q3C");

            lbl_Q3C_titulo1.setText(rs.getString("descripcion") + rs.getString("titulo1"));
            lbl_Q3C_titulo2.setText(titulo2);
            // - - - - - - Pregunta 1 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RPL
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RPL");

            AsignarPopupBtn(btn_Q2C_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 370, 150);//Mensaje ayuda del anterior

            bod.minRPL = rs.getDouble("rangomin");
            bod.maxRPL = rs.getDouble("rangomax");
            bod.errorRPL = rs.getString("errormsg");
            lbl_RPL_titulo1.setText(rs.getString("titulo1"));
            lbl_RPL_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_RPL_Pregunta, bod.minRPL, bod.maxRPL, lbl_RPL_error, bod.errorRPL);
            // - - - - - - Pregunta 2 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RTR
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RTR");

            lbl_RTR_desc.setText(rs.getString("descripcion"));
            String[] medidas = rs.getString("errormsg").split("\\|");

            for (String elem : medidas) {
                ddm_RTR_pregunta.addItem(elem);
            }
            ddm_RTR_pregunta.setSelectedIndex(Integer.parseInt(rs.getString("valorpordefecto")));

            ddm_RTR_pregunta.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    getItemMenu();
                }
            });

            lbl_RTR_titulo1.setText(rs.getString("titulo1"));
            lbl_RTR_titulo2.setText(rs.getString("titulo2"));
            AsignarPopupBtn(btn_RTR_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 360, 480);

            lbl_RNB_titulo3.setText(rs.getString("titulo2"));//Campo que usa datos de rtr
            // - - - - - - Pregunta 3 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - REB
            rs = db.ResultadoSelect("obtenerpregunta", "8", "REB");

            lbl_REB_titulo1.setText(rs.getString("titulo1"));
            lbl_REB_titulo2.setText(rs.getString("titulo2"));
            AsignarPopupBtn(btn_REB_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 420, 250);
            // - - - - - - Pregunta 4 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - REL
            rs = db.ResultadoSelect("obtenerpregunta", "8", "REL");

            bod.minREL = rs.getInt("rangomin");
            bod.maxREL = rs.getInt("rangomax");
            bod.errorREL = rs.getString("errormsg");
            lbl_REL_titulo1.setText(rs.getString("titulo1"));
            lbl_REL_titulo2.setText(rs.getString("titulo2"));
            txt_REL_Pregunta.setText(rs.getString("valorpordefecto"));
            AsignarTextEntero(txt_REL_Pregunta, bod.minREL, bod.maxREL, lbl_REL_error, bod.errorREL);
            AsignarPopupBtn(btn_REL_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 450, 450);

            lbl_RNE_titulo3.setText(rs.getString("titulo2")); //Este componente y otros usan resultados y cosas de REL
            // - - - - - - Pregunta 5 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RIB
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RIB");

            //bod.minRIB = rs.getInt("rangomin"); //Se colocan abajo cambioRBI
            //bod.maxRIB = rs.getInt("rangomax");//Se colocan abajo cambioRBI
            Error_RBI = rs.getString("errormsg");// bod.errorRIB  Se coloca en la funcion abajo cambioRBI

            lbl_RIB_titulo1.setText(rs.getString("titulo1"));
            lbl_RIB_titulo2.setText(rs.getString("titulo2"));

            //Tiene el listener aquí ya que posee 2 posibles rangos max y min
            txt_RIB_Pregunta.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    cambioRIB();
                }

                public void removeUpdate(DocumentEvent e) {
                    cambioRIB();
                }

                public void insertUpdate(DocumentEvent e) {
                    cambioRIB();
                }
            });
            // - - - - - - Pregunta 6 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RBI
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RBI");

            btngroupRBI.add(lbl_RBI_pregunta1);
            btngroupRBI.add(lbl_RBI_pregunta2);

            lbl_RBI_desc.setText(rs.getString("descripcion"));
            lbl_RBI_pregunta1.setText(rs.getString("titulo1"));
            lbl_RBI_pregunta2.setText(rs.getString("titulo2"));

            Rango1RBI = rs.getInt("rangomin");
            Rango2RBI = rs.getInt("valorpordefecto"); //Los valores que cambian los rango de R_I_B se toman de estos 3 campos            
            Rango3RBI = rs.getInt("rangomax");

            lbl_RBI_pregunta1.addActionListener(new ActionListener() { //Listener para el radiobutton
                @Override
                public void actionPerformed(ActionEvent e) {
                    cambioRBI(Rango1RBI, Rango2RBI);
                    txt_RIB_Pregunta.setEnabled(true);
                }
            });

            lbl_RBI_pregunta2.addActionListener(new ActionListener() { //Listener para el radiobutton
                @Override
                public void actionPerformed(ActionEvent e) {
                    cambioRBI(Rango2RBI, Rango3RBI);
                    txt_RIB_Pregunta.setEnabled(true);
                }
            });

            AsignarPopupBtn(btn_RBI_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 450, 250);
            // - - - - - - Pregunta 7 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RPM
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RPM");

            bod.minRPM = rs.getDouble("rangomin");
            bod.maxRPM = rs.getDouble("rangomax");

            bod.errorRPM = rs.getString("errormsg");
            lbl_RPM_titulo1.setText(rs.getString("titulo1"));
            lbl_RPM_titulo2.setText(rs.getString("titulo2"));
            lbl_RPM_desc.setText(rs.getString("descripcion"));
            txt_RPM_Pregunta.setText(rs.getString("valorpordefecto"));
            AsignarTextDoble(txt_RPM_Pregunta, bod.minRPM, bod.maxRPM, lbl_RPM_error, bod.errorRPM);
            AsignarPopupBtn(btn_RPM_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 300);
            // - - - - - - Pregunta 8 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RPN
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RPN");

            bod.minRPN = rs.getDouble("rangomin");
            bod.maxRPN = rs.getDouble("rangomax");
            bod.errorRPN = rs.getString("errormsg");
            lbl_RPN_titulo1.setText(rs.getString("titulo1"));
            lbl_RPN_titulo2.setText(rs.getString("titulo2"));
            txt_RPN_Pregunta.setText(rs.getString("valorpordefecto"));
            AsignarTextDoble(txt_RPN_Pregunta, bod.minRPN, bod.maxRPN, lbl_RPN_error, bod.errorRPN);
            // - - - - - - Pregunta 9 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RAU
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RAU");

            bod.minRAU = rs.getDouble("rangomin");
            bod.maxRAU = rs.getDouble("rangomax");
            bod.errorRAU = rs.getString("errormsg");
            lbl_RAU_titulo1.setText(rs.getString("titulo1"));
            lbl_RAU_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_RAU_Pregunta, bod.minRAU, bod.maxRAU, lbl_RAU_error, bod.errorRAU);
            AsignarPopupBtn(btn_RAU_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 360, 260);
            // - - - - - - Pregunta 10 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RER
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RER");

            bod.minRER = rs.getDouble("rangomin");
            bod.maxRER = rs.getDouble("rangomax");
            bod.errorRER = rs.getString("errormsg");
            lbl_RER_titulo1.setText(rs.getString("titulo1"));
            lbl_RER_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_RER_Pregunta, bod.minRER, bod.maxRER, lbl_RER_error, bod.errorRER);
            AsignarPopupBtn(btn_RER_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 360, 260);
            // - - - - - - Pregunta 11 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RAT
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RAT");

            bod.minRAT = rs.getDouble("rangomin");
            bod.maxRAT = rs.getDouble("rangomax");
            bod.errorRAT = rs.getString("errormsg");
            lbl_RAT_titulo1.setText(rs.getString("titulo1"));
            lbl_RAT_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_RAT_Pregunta, bod.minRAT, bod.maxRAT, lbl_RAT_error, bod.errorRAT);
            AsignarPopupBtn(btn_RAT_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 350, 280);
            // - - - - - - Pregunta 12 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RAR
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RAR");

            bod.minRAR = rs.getDouble("rangomin");
            bod.maxRAR = rs.getDouble("rangomax");
            bod.errorRAR = rs.getString("errormsg");
            lbl_RAR_titulo1.setText(rs.getString("titulo1"));
            lbl_RAR_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_RAR_Pregunta, bod.minRAR, bod.maxRAR, lbl_RAR_error, bod.errorRAR);
            AsignarPopupBtn(btn_RAR_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 360, 300);

            lbl_RCP_titulo4.setText(rs.getString("titulo2")); //Este componente y otros usan resultados y cosas de RAR
            lbl_RVM_titulo4.setText(rs.getString("titulo2")); //Este componente y otros usan resultados y cosas de RAR
            set_RAR_RCP_RVM("");//Los componentes que usan RAR se ponen en blanco
            // - - - - - - Pregunta 13 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RCP
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RCP");

            bod.minRCP = rs.getDouble("rangomin");
            bod.maxRCP = rs.getDouble("rangomax");
            bod.errorRCP = rs.getString("errormsg");
            lbl_RCP_titulo1.setText(rs.getString("titulo1"));
            lbl_RCP_titulo2.setText(rs.getString("titulo2"));
            lbl_RCP_desc.setText(rs.getString("descripcion"));
            AsignarTextDoble(txt_RCP_Pregunta, bod.minRCP, bod.maxRCP, lbl_RCP_error, bod.errorRCP);
            AsignarPopupBtn(btn_RCP_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 380, 420);
            // - - - - - - Pregunta 14 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RCM
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RCM");

            bod.minRCM = rs.getDouble("rangomin");
            bod.maxRCM = rs.getDouble("rangomax");
            bod.errorRCM = rs.getString("errormsg");
            lbl_RCM_titulo1.setText(rs.getString("titulo1"));
            lbl_RCM_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_RCM_Pregunta, bod.minRCM, bod.maxRCM, lbl_RCM_error, bod.errorRCM);
            // - - - - - - Pregunta 15 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RLC
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RLC");

            bod.minRLC = rs.getDouble("rangomin");
            bod.maxRLC = rs.getDouble("rangomax");
            bod.errorRLC = rs.getString("errormsg");
            lbl_RLC_titulo1.setText(rs.getString("titulo1"));
            lbl_RLC_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_RLC_Pregunta, bod.minRLC, bod.maxRLC, lbl_RLC_error, bod.errorRLC);
            AsignarPopupBtn(btn_RLC_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 360, 300);
            // - - - - - - Pregunta 16 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RVM
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RVM");

            bod.minRVM = rs.getDouble("rangomin");
            bod.maxRVM = rs.getDouble("rangomax");
            bod.errorRVM = rs.getString("errormsg");
            lbl_RVM_titulo1.setText(rs.getString("titulo1"));
            lbl_RVM_titulo2.setText(rs.getString("titulo2"));
            lbl_RVM_desc.setText(rs.getString("descripcion"));
            AsignarTextDoble(txt_RVM_Pregunta, bod.minRVM, bod.maxRVM, lbl_RVM_error, bod.errorRVM);
            AsignarPopupBtn(btn_RVM_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 360, 340);
            // - - - - - - Pregunta 17 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RVN
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RVN");

            bod.minRVN = rs.getDouble("rangomin");
            bod.maxRVN = rs.getDouble("rangomax");
            bod.errorRVN = rs.getString("errormsg");
            lbl_RVN_titulo1.setText(rs.getString("titulo1"));
            lbl_RVN_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_RVN_Pregunta, bod.minRVN, bod.maxRVN, lbl_RVN_error, bod.errorRVN);
            // - - - - - - Pregunta 18 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RNB
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RNB");

            bod.minRNB = rs.getInt("rangomin");
            bod.maxRNB = rs.getInt("rangomax");
            bod.errorRNB = rs.getString("errormsg");
            lbl_RNB_titulo1.setText(rs.getString("titulo1"));
            lbl_RNB_titulo2.setText(rs.getString("titulo2"));
            lbl_RNB_desc.setText(rs.getString("descripcion"));
            AsignarTextEntero(txt_RNB_Pregunta, bod.minRNB, bod.maxRNB, lbl_RNB_error, bod.errorRNB);
            AsignarPopupBtn(btn_RNB_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 360, 370);
            // - - - - - - Pregunta 19 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RNE
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RNE");

            bod.minRNE = rs.getDouble("rangomin");
            bod.maxRNE = rs.getDouble("rangomax");
            bod.errorRNE = rs.getString("errormsg");
            lbl_RNE_titulo1.setText(rs.getString("titulo1"));
            lbl_RNE_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_RNE_Pregunta, bod.minRNE, bod.maxRNE, lbl_RNE_error, bod.errorRNE);
            // - - - - - - Pregunta 20 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RPC
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RPC");

            bod.minRPC = rs.getDouble("rangomin");
            bod.maxRPC = rs.getDouble("rangomax");
            bod.errorRPC = rs.getString("errormsg");
            lbl_RPC_titulo1.setText(rs.getString("titulo1"));
            lbl_RPC_titulo2.setText(rs.getString("titulo2"));
            lbl_RPC_desc.setText(rs.getString("descripcion"));
            AsignarTextDoble(txt_RPC_Pregunta, bod.minRPC, bod.maxRPC, lbl_RPC_error, bod.errorRPC);
            AsignarPopupBtn(btn_RPC_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 450, 470);
            // - - - - - - Pregunta 21 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RPS
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RPS");

            bod.minRPS = rs.getDouble("rangomin");
            bod.maxRPS = rs.getDouble("rangomax");
            bod.errorRPS = rs.getString("errormsg");
            lbl_RPS_titulo1.setText(rs.getString("titulo1"));
            lbl_RPS_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_RPS_Pregunta, bod.minRPS, bod.maxRPS, lbl_RPS_error, bod.errorRPS);
            // - - - - - - Pregunta 22 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RPB
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RPB");

            bod.minRPB = rs.getDouble("rangomin");
            bod.maxRPB = rs.getDouble("rangomax");
            bod.errorRPB = rs.getString("errormsg");
            lbl_RPB_titulo1.setText(rs.getString("titulo1"));
            lbl_RPB_titulo2.setText(rs.getString("titulo2"));
            lbl_RPB_desc.setText(rs.getString("descripcion"));
            txt_RPB_Pregunta.setText(rs.getString("valorpordefecto"));
            AsignarTextDoble(txt_RPB_Pregunta, bod.minRPB, bod.maxRPB, lbl_RPB_error, bod.errorRPB);

            String imagen = "";
            if (!bod.Generalpath.equals("")) { //todo: comprobar slash en linux
                imagen = bod.Generalpath + "/Images/Barras.png";
                imagen = rs.getString("ayuda").replace("[imagen]", imagen).replace("\\", "/");
            }

            AsignarPopupBtn(btn_RPB_ayuda, imagen, rs.getString("ayudalink"), 360, 600);
            // - - - - - - Pregunta 23 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RPH
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RPH");

            bod.minRPH = rs.getDouble("rangomin");
            bod.maxRPH = rs.getDouble("rangomax");
            bod.errorRPH = rs.getString("errormsg");
            lbl_RPH_titulo1.setText(rs.getString("titulo1"));
            lbl_RPH_titulo2.setText(rs.getString("titulo2"));
            AsignarTextDoble(txt_RPH_Pregunta, bod.minRPH, bod.maxRPH, lbl_RPH_error, bod.errorRPH);

            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
            // Si Bod cargó datos de Laguna Anaerobia desde la BD, porque Ya estaba editada, se proceden a llenar casillas con datos
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -          
            if (bod.EditRejillas) {
                txt_RPL_Pregunta.setText(bod.getRPLs());
                ddm_RTR_pregunta.setSelectedIndex(bod.getRTR());
                txt_REB_Pregunta.setText(bod.getREBs());
                txt_REL_Pregunta.setText(bod.getREL() + "");//Valores bajos no necesita control #
                txt_RIB_Pregunta.setText(bod.getRIB() + "");//Valores bajos 

                if (bod.getRBI() > 0) {// 1 = mecanico
                    lbl_RBI_pregunta1.setSelected(true);
                    cambioRBI(Rango1RBI, Rango2RBI);
                } else {
                    lbl_RBI_pregunta2.setSelected(true);
                    cambioRBI(Rango2RBI, Rango3RBI);
                }
                txt_RIB_Pregunta.setEnabled(true);
                txt_RPM_Pregunta.setText(bod.getRPMs());
                txt_RPN_Pregunta.setText(bod.getRPNs());
                txt_RAU_Pregunta.setText(bod.getRAUs());
                txt_RER_Pregunta.setText(bod.getRERs());
                txt_RAT_Pregunta.setText(bod.getRATs());
                txt_RAR_Pregunta.setText(bod.getRARs());
                set_RAR_RCP_RVM(bod.getRARs());
                txt_RCP_Pregunta.setText(bod.getRCPs());
                txt_RCM_Pregunta.setText(bod.getRCMs());
                txt_RLC_Pregunta.setText(bod.getRLCs());
                txt_RVM_Pregunta.setText(bod.getRVMs());
                txt_RVN_Pregunta.setText(bod.getRVNs());
                txt_RNB_Pregunta.setText(bod.getRNB() + "");
                txt_RNE_Pregunta.setText(bod.getRNEs());
                txt_RPC_Pregunta.setText(bod.getRPCs());
                txt_RPS_Pregunta.setText(bod.getRPSs());
                txt_RPB_Pregunta.setText(bod.getRPBs());
                txt_RPH_Pregunta.setText(bod.getRPHs());

            } else {// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -si no otras tareas de cálculos que se deben hacer al cargar
                getItemMenu();
                calcularRAU();
                calcularRER();
                calcularRAT();
                calcularRAR();
                calcularRCP();
                calcularRCM();
                calcularRLC();
                calcularRVM();
                calcularRVN();
                calcularRNB();
                calcularRNE();
                calcularRPC();
                calcularRPS();
                calcularRPH();
            }

            txt_Q2C_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(bod.calcularQ2C_m3())), "#.####"));
            txt_Q3C_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(bod.calcularQ3C_m3())), "#.####"));

            //Se agrega listener 'especial' a campos no editables los cuales hacen parte de cálculos de una o varias fórmulas
            ListenerCampoTxt_Metodos(txt_REB_Pregunta, "REB");
            ListenerCampoTxt_Metodos(txt_RAR_Pregunta, "RAR");
            ListenerCampoTxt_Metodos(txt_RAT_Pregunta, "RAT");
            ListenerCampoTxt_Metodos(txt_RER_Pregunta, "RER");
            ListenerCampoTxt_Metodos(txt_RAU_Pregunta, "RAU");
            ListenerCampoTxt_Metodos(txt_RCP_Pregunta, "RCP");
            ListenerCampoTxt_Metodos(txt_RVM_Pregunta, "RVM");

            set_RNE();

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
    private boolean Guardar() { //Si se modifica esta clase se debe modificar su respectivo en 'Componentes' CX_

        lbl_save_error.setText("");
        lbl_save_error2.setText("");

        try {
            ResultSet rs;
            // - - - - - - Pregunta 1- - - - - -  - - - RPL Profundidad de la lámina de agua determinada por las unidades del conducto  o tuberías afluente (h)
            if (!bod.setRPL(txt_RPL_Pregunta.getText(), bod.minRPL, bod.maxRPL)) {//todo:hacer este cambio de traer errores de bod y bd
                rs = db.ResultadoSelect("obtenernombretitulo", "RPL", null);
                lbl_save_error2.setText("Punto 1, Error en " + rs.getString("titulo1"));
                lbl_save_error.setText(bod.errorRPL);
                return false;
            }
            // - - - - - - Pregunta 2- - - - - -  - - - RTR Seleccionar el tipo de rejilla
            if (!bod.setRTR(ddm_RTR_pregunta.getSelectedIndex() + "", 0, 100)) { //Esta validacion no está verificada, faltan datos o no es importante
                rs = db.ResultadoSelect("obtenernombretitulo", "RTR", null);
                lbl_save_error2.setText("Punto 2, Error en " + rs.getString("titulo1"));
                //lbl_save_error.setText(bod.errorRTR);
                return false;
            }
            // - - - - - - Pregunta 3- - - - - -  - - - REB Espesor de la barra (t) 
            if (!bod.setREB(txt_REB_Pregunta.getText() + "", 0, 100)) { //Esta validacion no está verificada, faltan datos o no es importante
                rs = db.ResultadoSelect("obtenernombretitulo", "REB", null);
                lbl_save_error2.setText("Punto 3, Error en " + rs.getString("titulo1"));
                //lbl_save_error.setText(bod.errorREB);
                return false;
            }
            // - - - - - - Pregunta 4- - - - - -  - - - REL Espaciamiento libre entre barras (a)           
            if (!bod.setREL(txt_REL_Pregunta.getText(), bod.minREL, bod.maxREL)) {
                rs = db.ResultadoSelect("obtenernombretitulo", "REL", null);
                lbl_save_error2.setText("Punto 4, Error en " + rs.getString("titulo1"));
                lbl_save_error.setText(bod.errorREL);
                return false;
            }
            // - - - - - - Pregunta 6- - - - - -  - - - RBI Tipo de limpieza
            if (!lbl_RBI_pregunta1.isSelected() && !lbl_RBI_pregunta2.isSelected()) {
                rs = db.ResultadoSelect("obtenernombretitulo", "RBI", null);
                lbl_save_error2.setText("Punto 6, Error en " + rs.getString("titulo1") + " " + rs.getString("titulo2"));
                lbl_save_error.setText(bod.errorREL);
                return false;
            }

            if (lbl_RBI_pregunta1.isSelected()) { //Manual = 1
                bod.setRBI(1);
            } else { //Mecánica = 0
                bod.setRBI(0);
            }
            // - - - - - - Pregunta 5- - - - - -  - - - RIB Inclinación de las barras (θ)
            if (!bod.setRIB(txt_RIB_Pregunta.getText(), bod.minRIB, bod.maxRIB)) {
                rs = db.ResultadoSelect("obtenernombretitulo", "RIB", null);
                lbl_save_error2.setText("Punto 5, Error en " + rs.getString("titulo1"));
                lbl_save_error.setText(bod.errorRIB);
                return false;
            }
            // - - - - - - Pregunta 7- - - - - -  - - - RPM Velocidad máxima (Vmax) 
            if (!bod.setRPM(txt_RPM_Pregunta.getText(), bod.minRPM, bod.maxRPM)) {
                rs = db.ResultadoSelect("obtenernombretitulo", "RPM", null);
                lbl_save_error2.setText("Punto 7, Error en " + rs.getString("titulo1"));
                lbl_save_error.setText(bod.errorRPM);
                return false;
            }
            // - - - - - - Pregunta 8- - - - - -  - - - RPN Velocidad mínima (Vmin) 
            if (!bod.setRPN(txt_RPN_Pregunta.getText(), bod.minRPN, bod.maxRPN)) {
                rs = db.ResultadoSelect("obtenernombretitulo", "RPN", null);
                lbl_save_error2.setText("Punto 8, Error en " + rs.getString("titulo1"));
                lbl_save_error.setText(bod.errorRPN);
                return false;
            }
            // - - - - - - Pregunta 9- - - - - -  - - - RAU  Área útil entre las barras para el escurrimiento (Au)
            if (!bod.setRAU(bod.calcularRAU(0), bod.minRAU, bod.maxRAU)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RAU", null);
                lbl_save_error2.setText("Punto 9, Error en " + rs.getString("titulo1"));
                lbl_save_error.setText(bod.errorRAU);
                return false;
            }
            // - - - - - - Pregunta 10- - - - - -  - - - RER Eficiencia de las rejillas en función del espesor (t) y el espaciamiento entre barras (a)
            if (!bod.setRER(bod.calcularRER(0, 0), bod.minRER, bod.maxRER)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RER", null);
                lbl_save_error2.setText("Punto 10, Error en " + rs.getString("titulo1"));
                lbl_save_error.setText(bod.errorRER);
                return false;
            }
            // - - - - - - Pregunta 11- - - - - -  - - - RAT Área total de las rejillas (incluidas las barras) (S) 
            if (!bod.setRAT(bod.calcularRAT(0, 0), bod.minRAT, bod.maxRAT)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RAT", null);
                lbl_save_error2.setText("Punto 11, Error en " + rs.getString("titulo1"));
                lbl_save_error.setText(bod.errorRAT);
                return false;
            }
            // - - - - - - Pregunta 12- - - - - -  - - - RAR Ancho de rejillas (b)
            if (!bod.setRAR(bod.calcularRAR(0, 0), bod.minRAR, bod.maxRAR)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RAR", null);
                lbl_save_error2.setText("Punto 12, Error en " + rs.getString("titulo1"));
                lbl_save_error.setText(bod.errorRAR);
                return false;
            }
            // - - - - - - Pregunta 13- - - - - -  - - - RCP Velocidad máxima (Vmax)
            if (!bod.setRCP(bod.calcularRCP(0, 0, 0), bod.minRCP, bod.maxRCP)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RCP", null);
                lbl_save_error2.setText("Punto 13, Error en " + rs.getString("titulo1"));
                lbl_save_error.setText(bod.errorRCP);
                return false;
            }
            // - - - - - - Pregunta 14- - - - - -  - - - RCM Velocidad media (Vmed)
            if (!bod.setRCM(bod.calcularRCM(0, 0, 0), bod.minRCM, bod.maxRCM)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RCM", null);
                lbl_save_error2.setText("Punto 14, Error en " + rs.getString("titulo1"));
                lbl_save_error.setText(bod.errorRCM);
                return false;
            }
            // - - - - - - Pregunta 15- - - - - -  - - - RLC Longitud del canal (L)
            if (!bod.setRLC(bod.calcularRLC(0), bod.minRLC, bod.maxRLC)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RLC", null);
                lbl_save_error2.setText("Punto 15, Error en " + rs.getString("titulo1"));
                lbl_save_error.setText(bod.errorRLC);
                return false;
            }
            // - - - - - - Pregunta 16- - - - - -  - - - RVM Velocidad de aproximación Vo para el Caudal Máximo
            if (!bod.setRVM(bod.calcularRVM(0, 0), bod.minRVM, bod.maxRVM)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RVM", null);
                lbl_save_error2.setText("Punto 16, Error en " + rs.getString("titulo1"));
                lbl_save_error.setText(bod.errorRVM);
                return false;
            }
            // - - - - - - Pregunta 17- - - - - -  - - - RVN Velocidad de aproximación Vo para el caudal medio
            if (!bod.setRVN(bod.calcularRVN(0, 0), bod.minRVN, bod.maxRVN)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RVN", null);
                lbl_save_error2.setText("Punto 17, Error en " + rs.getString("titulo1"));
                lbl_save_error.setText(bod.errorRVN);
                return false;
            }
            // - - - - - - Pregunta 18- - - - - -  - - - RNB Número de barras (Nb) en la rejilla 
            if (!bod.setRNB(bod.calcularRNB(0, 0, 0), bod.minRNB, bod.maxRNB)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RNB", null);
                lbl_save_error2.setText("Punto 18, Error en " + rs.getString("titulo1"));
                lbl_save_error.setText(bod.errorRNB);
                return false;
            }
            // - - - - - - Pregunta 19- - - - - -  - - - RNE Número de espaciamientos (Ne) en la rejilla 
            if (!bod.setRNE(bod.calcularRNE(0), bod.minRNE, bod.maxRNE)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RNE", null);
                lbl_save_error2.setText("Punto 19, Error en " + rs.getString("titulo1"));
                lbl_save_error.setText(bod.errorRNE);
                return false;
            }
            // - - - - - - Pregunta 20- - - - - -  - - - RPC Pérdida de carga en la rejilla 
            if (!bod.setRPC(bod.calcularRPC(0, 0), bod.minRPC, bod.maxRPC)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RPC", null);
                lbl_save_error2.setText("Punto 20, Error en " + rs.getString("titulo1"));
                lbl_save_error.setText(bod.errorRPC);
                return false;
            }
            // - - - - - - Pregunta 21- - - - - -  - - - RPS Pérdida de carga en la rejilla 50% sucia (hL)
            if (!bod.setRPS(bod.calcularRPS(0, 0), bod.minRPS, bod.maxRPS)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RPS", null);
                lbl_save_error2.setText("Punto 21, Error en " + rs.getString("titulo1"));
                lbl_save_error.setText(bod.errorRPS);
                return false;
            }
            // - - - - - - Pregunta 22- - - - - -  - - - RPB
            if (!bod.setRPB(txt_RPB_Pregunta.getText(), bod.minRPB, bod.maxRPB)) { //Factor que depende de la sección de las barras β 
                rs = db.ResultadoSelect("obtenernombretitulo", "RPB", null);
                lbl_save_error2.setText("Punto 22, Error en " + rs.getString("titulo1"));
                lbl_save_error.setText(bod.errorRPB);
                return false;
            }
            // - - - - - - Pregunta 23- - - - - -  - - - RPH Pérdida de carga calculada por la ecuación de Kirshmer hF
            if (!bod.setRPH(bod.calcularRPH(0, 0, 0, 0, 0), bod.minRPH, bod.maxRPH)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RPH", null);
                lbl_save_error2.setText("Punto 23, Error en " + rs.getString("titulo1"));
                lbl_save_error.setText(bod.errorRPH);
                return false;
            }

            bod.EditRejillas = true;

            if (!bod.GuardarUpdateBod()) {
                bod.EditRejillas = false;
                return false;
            }
//            Esto solo lo deben hacer quienes afecten datos de los que depndan otros y Rejillas no genera datos que afecten posteriores      
//            Main main = (Main) this.getTopLevelAncestor(); // Trata de ir  al contenedor principal 
//            main.ComprobarCambio(5);//Comprobar cambios que afecten datos posteriores, si este guardar es de una edición.

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

    /**
     * *
     * Agrega un listener general para multiples JTextField "No editables" en
     * los cuales un listener comun (keyreleased) No funciona
     *
     * @param jt recibe el JTextField al que se le agregará el listener
     */
    private void ListenerCampoTxt_Metodos(JTextField jtfield, final String RXX) {

        jtfield.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
            }

            public void removeUpdate(DocumentEvent e) {
            }

            public void insertUpdate(DocumentEvent e) {
                ejecutar_Metodos(RXX);
            }
        });
    }

    /**
     * Obtiene el item seleccionado del menú desplegable y lo parsea a la
     * casilla siguiente REB
     */
    public void getItemMenu() {
        String[] items = ddm_RTR_pregunta.getSelectedItem().toString().split(",");
        set_RTR_RNB_RNE(items[1]);//Se llenan otras celdas que usan los datos de RTR
        double item = Double.parseDouble(items[1].substring(0, 5).toLowerCase().replace("x", "").trim());
        txt_REB_Pregunta.setText(item + "");
    }

    public void cambioRBI(int min, int max) {
        bod.minRIB = min;
        bod.maxRIB = max;

        bod.errorRIB = Error_RBI.replace("min.", bod.minRIB + "");
        bod.errorRIB = bod.errorRIB.replace("max.", bod.maxRIB + "");

        if (txt_RIB_Pregunta.isEnabled()) {//cuando se cambia de radio no cambiaba texto
            cambioRIB();
        }
    }

    public void cambioRIB() { //viene del listener del textbox

        if (validar.EsEnteroEntre(txt_RIB_Pregunta.getText(), bod.minRIB, bod.maxRIB)) {
            lbl_RIB_error.setText("");
        } else {
            lbl_RIB_error.setText(bod.errorRIB);
        }
    }

    /**
     * Esto se hace para calcular automática y temporalmente RAU ya que debe
     * aparecer cuando el usuario ingrese RPM
     */
    private void calcularRAU() {

        String x = txt_RPM_Pregunta.getText().trim();
        if (validar.EsDobleEntre(x, bod.minRPM, bod.maxRPM)) {
            x = bod.calcularRAU(Double.parseDouble(x));
            txt_RAU_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.#####"));
        } else {
            txt_RAU_Pregunta.setText("");
        }
    }

    /**
     * Esto se hace para calcular automática y temporalmente RER ya que debe
     * aparecer cuando el usuario ingrese REB y REL
     */
    private void calcularRER() {

        String x = txt_REL_Pregunta.getText().trim(); //REB no se valida, REL si
        if (validar.EsDobleEntre(x, bod.minREL, bod.maxREL)) {
            x = bod.calcularRER(Double.parseDouble(txt_REB_Pregunta.getText().trim()), Double.parseDouble(x));
            txt_RER_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.###"));
        } else {
            txt_RER_Pregunta.setText("");
        }
    }

    /**
     * Esto se hace para calcular automática y temporalmente RAT ya que debe
     * aparecer cuando el usuario ingrese REB y REL
     */
    private void calcularRAT() {

        String x = txt_RAU_Pregunta.getText().trim();
        String y = txt_RER_Pregunta.getText().trim();
        if (validar.EsDobleEntre(x, bod.minRAU, bod.maxRAU)) {
            if (validar.EsDobleEntre(y, bod.minRER, bod.maxRER)) {
                x = bod.calcularRAT(Double.parseDouble(x), Double.parseDouble(y));
                txt_RAT_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.#####"));
                return;
            }
        }
        txt_RAT_Pregunta.setText("");
    }

    /**
     * Esto se hace para calcular automática y temporalmente RAR ya que debe
     * aparecer cuando el usuario ingrese REB y REL
     */
    private void calcularRAR() {

        String x = txt_RAT_Pregunta.getText().trim();
        String y = txt_RPL_Pregunta.getText().trim();
        if (validar.EsDobleEntre(x, bod.minRAT, bod.maxRAT)) {
            if (validar.EsDobleEntre(y, bod.minRPL, bod.maxRPL)) {
                x = bod.calcularRAR(Double.parseDouble(x), Double.parseDouble(y));
                set_RAR_RCP_RVM(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.####"));//mandar resultado a otros labels              
                return;
            }
        }
        txt_RAR_Pregunta.setText("");
    }

    /**
     * Se llenan RAR y algunos labels que usan el resultado de RAR
     *
     * @param rar
     */
    private void set_RAR_RCP_RVM(String rar) {
        txt_RAR_Pregunta.setText(rar);
        lbl_RCP_titulo3.setText(rar);
        lbl_RVM_titulo3.setText(rar);
    }

    private void calcularRCP() {
        String x = txt_RAR_Pregunta.getText().trim();
        String y = txt_RPL_Pregunta.getText().trim();
        String z = txt_RER_Pregunta.getText().trim();

        if (validar.EsDobleEntre(x, bod.minRAR, bod.maxRAR)) {
            if (validar.EsDobleEntre(y, bod.minRPL, bod.maxRPL)) {
                if (validar.EsDobleEntre(z, bod.minRER, bod.maxRER)) {
                    x = bod.calcularRCP(Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
                    txt_RCP_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.##"));//mandar resultado a otros labels              
                    return;
                }
            }
        }
        txt_RCP_Pregunta.setText("");
    }

    private void calcularRCM() {
        String x = txt_RAR_Pregunta.getText().trim();
        String y = txt_RPL_Pregunta.getText().trim();
        String z = txt_RER_Pregunta.getText().trim();

        if (validar.EsDobleEntre(x, bod.minRAR, bod.maxRAR)) {
            if (validar.EsDobleEntre(y, bod.minRPL, bod.maxRPL)) {
                if (validar.EsDobleEntre(z, bod.minRER, bod.maxRER)) {
                    x = bod.calcularRCM(Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
                    txt_RCM_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.##"));//mandar resultado a otros labels              
                    return;
                }
            }
        }
        txt_RCM_Pregunta.setText("");
    }

    /**
     * Esto se hace para calcular automática y temporalmente RLC ya que debe
     * aparecer cuando el usuario ingrese RAT
     */
    private void calcularRLC() {

        String x = txt_RAT_Pregunta.getText().trim();

        if (validar.EsDobleEntre(x, bod.minRAT, bod.maxRAT)) {
            x = bod.calcularRLC(Double.parseDouble(x));
            txt_RLC_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.##"));
        } else {
            txt_RLC_Pregunta.setText("");
        }
    }

    private void calcularRVM() {
        String x = txt_RAR_Pregunta.getText().trim();
        String y = txt_RPL_Pregunta.getText().trim();

        if (validar.EsDobleEntre(x, bod.minRAR, bod.maxRAR)) {
            if (validar.EsDobleEntre(y, bod.minRPL, bod.maxRPL)) {
                x = bod.calcularRVM(Double.parseDouble(x), Double.parseDouble(y));
                txt_RVM_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.##"));//mandar resultado a otros labels              
                return;
            }
        }
        txt_RVM_Pregunta.setText("");
    }

    private void calcularRVN() {
        String x = txt_RAR_Pregunta.getText().trim();
        String y = txt_RPL_Pregunta.getText().trim();

        if (validar.EsDobleEntre(x, bod.minRAR, bod.maxRAR)) {
            if (validar.EsDobleEntre(y, bod.minRPL, bod.maxRPL)) {
                x = bod.calcularRVN(Double.parseDouble(x), Double.parseDouble(y));
                txt_RVN_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.##"));//mandar resultado a otros labels              
                return;
            }
        }
        txt_RVN_Pregunta.setText("");
    }

    /**
     * Se llenan RNB 2 RNE 2 y algunos labels que usan el resultado de RTR
     *
     * @param rtr
     */
    private void set_RTR_RNB_RNE(String rtr) {
        txt_RNB_Pregunta2.setText(rtr);
    }

    private void calcularRNB() {
        String x = txt_RAR_Pregunta.getText().trim();
        String y = txt_REL_Pregunta.getText().trim();
        String z = txt_REB_Pregunta.getText().trim();

        if (validar.EsDobleEntre(x, bod.minRAR, bod.maxRAR)) {
            if (validar.EsDobleEntre(y, bod.minREL, bod.maxREL)) {  //REB No se valida              
                x = bod.calcularRNB(Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
                txt_RNB_Pregunta.setText(x);//Se debe parsear entero
                return;
            }
        }
        txt_RNB_Pregunta.setText("");
    }

    private void calcularRNE() {
        String x = txt_RNB_Pregunta.getText();

        if (validar.EsEnteroEntre(x, bod.minRNB, bod.maxRNB)) {
            x = bod.calcularRNE(Double.parseDouble(x));
            txt_RNE_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.##"));
            return;
        }
        txt_RNE_Pregunta.setText("");
    }

    /**
     * Se llenan la casilla junto a RNE con el resultado de REL
     *
     * @param rtr
     */
    private void set_RNE() {
        txt_RNE_Pregunta2.setText(txt_REL_Pregunta.getText());
    }

    private void calcularRPC() {
        String x = txt_RCP_Pregunta.getText().trim();
        String y = txt_RVM_Pregunta.getText().trim();

        if (validar.EsDobleEntre(x, bod.minRCP, bod.maxRCP)) {
            if (validar.EsDobleEntre(y, bod.minRVM, bod.maxRVM)) {
                x = bod.calcularRPC(Double.parseDouble(x), Double.parseDouble(y));
                txt_RPC_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.###"));
                return;
            }
        }
        txt_RPC_Pregunta.setText("");
    }

    private void calcularRPS() {
        String x = txt_RCP_Pregunta.getText().trim();
        String y = txt_RVM_Pregunta.getText().trim();

        if (validar.EsDobleEntre(x, bod.minRCP, bod.maxRCP)) {
            if (validar.EsDobleEntre(y, bod.minRVM, bod.maxRVM)) {
                x = bod.calcularRPS(Double.parseDouble(x), Double.parseDouble(y));
                txt_RPS_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.###"));
                return;
            }
        }
        txt_RPS_Pregunta.setText("");
    }

    private void calcularRPH() {//RPB,  REB,  REL,  RIB,  RCP
        String v = txt_RPB_Pregunta.getText();
        String w = txt_REB_Pregunta.getText();//no se valida
        String x = txt_REL_Pregunta.getText();
        String y = txt_RIB_Pregunta.getText();
        String z = txt_RCP_Pregunta.getText();

        if (validar.EsDobleEntre(v, bod.minRPB, bod.maxRPB)) {
            if (validar.EsDobleEntre(x, bod.minREL, bod.maxREL)) {
                if (validar.EsDobleEntre(y, bod.minRIB, bod.maxRIB)) {
                    if (validar.EsDobleEntre(z, bod.minRCP, bod.maxRCP)) {
                        x = bod.calcularRPH(Double.parseDouble(v), Double.parseDouble(w), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
                        txt_RPH_Pregunta.setText(validar.DobleFormatoStringCeil((Double.parseDouble(x)), "#.##"));
                        return;
                    }
                }
            }
        }
        txt_RPH_Pregunta.setText("");
    }

    /**
     * Ejecuta varios metodos que las casillas Inactivas deben ejecutar
     *
     * @param rxx ...Puede ser RAR REB , etc
     */
    public void ejecutar_Metodos(String rxx) {

        switch (rxx) {
            case "REB": // Calcular temporalmente a:
                calcularRER();
                calcularRNB();
                calcularRNE();
                calcularRPH();
                break;
            case "RAR": // Calcular temporalmente a:
                calcularRCP();
                calcularRCM();
                calcularRVM();
                calcularRVN();
                calcularRNB();
                calcularRNE();
                break;
            case "RAT": // Calcular temporalmente a:
                calcularRAR();
                calcularRLC();
                break;
            case "RER": // Calcular temporalmente a:
                calcularRAT();
                calcularRCP();
                calcularRCM();
                break;
            case "RAU": // Calcular temporalmente a:
                calcularRAT();
                break;
            case "RCP": // Calcular temporalmente a:
                calcularRPC();
                calcularRPS();
                calcularRPH();
                break;
            case "RVM": // Calcular temporalmente a:
                calcularRPC();
                calcularRPS();
                break;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jsp_Rejillas = new javax.swing.JScrollPane();
        jp_Componentes = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        lbl_titulo1 = new javax.swing.JLabel();
        btn_guardar = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        txt_Q2C_Pregunta = new javax.swing.JTextField();
        lbl_Q2C_titulo1 = new javax.swing.JLabel();
        lbl_Q2C_titulo2 = new javax.swing.JLabel();
        lbl_Q3C_titulo1 = new javax.swing.JLabel();
        txt_Q3C_Pregunta = new javax.swing.JTextField();
        lbl_Q3C_titulo2 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        btn_close = new javax.swing.JButton();
        lbl_save_error = new javax.swing.JLabel();
        lbl_save_error2 = new javax.swing.JLabel();
        lbl_RPL_error = new javax.swing.JLabel();
        lbl_RPL_titulo2 = new javax.swing.JLabel();
        txt_RPL_Pregunta = new javax.swing.JTextField();
        lbl_RPL_titulo1 = new javax.swing.JLabel();
        lbl_RTR_titulo2 = new javax.swing.JLabel();
        lbl_RTR_titulo1 = new javax.swing.JLabel();
        txt_RAR_Pregunta = new javax.swing.JTextField();
        lbl_RAR_titulo1 = new javax.swing.JLabel();
        lbl_RLC_error = new javax.swing.JLabel();
        lbl_RLC_titulo2 = new javax.swing.JLabel();
        lbl_RAR_error = new javax.swing.JLabel();
        txt_RLC_Pregunta = new javax.swing.JTextField();
        lbl_RLC_titulo1 = new javax.swing.JLabel();
        lbl_RAR_titulo2 = new javax.swing.JLabel();
        lbl_REL_titulo1 = new javax.swing.JLabel();
        lbl_REL_titulo2 = new javax.swing.JLabel();
        txt_REL_Pregunta = new javax.swing.JTextField();
        lbl_RIB_titulo1 = new javax.swing.JLabel();
        txt_RIB_Pregunta = new javax.swing.JTextField();
        lbl_RIB_titulo2 = new javax.swing.JLabel();
        lbl_REL_error = new javax.swing.JLabel();
        lbl_RIB_error = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        lbl_RPM_desc = new javax.swing.JLabel();
        lbl_RBI_pregunta1 = new javax.swing.JRadioButton();
        lbl_RBI_pregunta2 = new javax.swing.JRadioButton();
        lbl_RPM_titulo1 = new javax.swing.JLabel();
        lbl_RPM_error = new javax.swing.JLabel();
        lbl_RPM_titulo2 = new javax.swing.JLabel();
        txt_RPM_Pregunta = new javax.swing.JTextField();
        lbl_RPN_titulo1 = new javax.swing.JLabel();
        lbl_RPN_error = new javax.swing.JLabel();
        lbl_RPN_titulo2 = new javax.swing.JLabel();
        txt_RPN_Pregunta = new javax.swing.JTextField();
        lbl_RVM_titulo2 = new javax.swing.JLabel();
        lbl_RVM_error = new javax.swing.JLabel();
        lbl_RVM_titulo1 = new javax.swing.JLabel();
        txt_RVM_Pregunta = new javax.swing.JTextField();
        txt_RER_Pregunta = new javax.swing.JTextField();
        lbl_RAU_titulo1 = new javax.swing.JLabel();
        lbl_RER_error = new javax.swing.JLabel();
        lbl_RER_titulo2 = new javax.swing.JLabel();
        txt_RAU_Pregunta = new javax.swing.JTextField();
        lbl_RER_titulo1 = new javax.swing.JLabel();
        lbl_RAU_error = new javax.swing.JLabel();
        lbl_RAU_titulo2 = new javax.swing.JLabel();
        lbl_RAT_titulo2 = new javax.swing.JLabel();
        lbl_RAT_error = new javax.swing.JLabel();
        lbl_RAT_titulo1 = new javax.swing.JLabel();
        txt_RAT_Pregunta = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        ddm_RTR_pregunta = new javax.swing.JComboBox();
        lbl_REB_error = new javax.swing.JLabel();
        txt_REB_Pregunta = new javax.swing.JTextField();
        lbl_REB_titulo1 = new javax.swing.JLabel();
        lbl_REB_titulo2 = new javax.swing.JLabel();
        lbl_RBI_desc = new javax.swing.JLabel();
        lbl_RCP_desc = new javax.swing.JLabel();
        lbl_RCP_titulo4 = new javax.swing.JLabel();
        lbl_RCP_titulo3 = new javax.swing.JLabel();
        txt_RCP_Pregunta = new javax.swing.JTextField();
        lbl_RCM_titulo1 = new javax.swing.JLabel();
        txt_RCM_Pregunta = new javax.swing.JTextField();
        lbl_RCM_titulo2 = new javax.swing.JLabel();
        lbl_RCM_error = new javax.swing.JLabel();
        lbl_RCP_titulo2 = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        lbl_RCP_error = new javax.swing.JLabel();
        lbl_RCP_titulo1 = new javax.swing.JLabel();
        lbl_RVN_error = new javax.swing.JLabel();
        lbl_RVN_titulo2 = new javax.swing.JLabel();
        txt_RVN_Pregunta = new javax.swing.JTextField();
        lbl_RVN_titulo1 = new javax.swing.JLabel();
        lbl_RVM_desc = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        lbl_RVM_titulo3 = new javax.swing.JLabel();
        lbl_RNB_desc = new javax.swing.JLabel();
        lbl_RNE_titulo1 = new javax.swing.JLabel();
        txt_RNE_Pregunta = new javax.swing.JTextField();
        lbl_RNE_titulo2 = new javax.swing.JLabel();
        lbl_RNE_error = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        lbl_RNB_error = new javax.swing.JLabel();
        lbl_RNB_titulo2 = new javax.swing.JLabel();
        lbl_RNB_titulo1 = new javax.swing.JLabel();
        txt_RNB_Pregunta = new javax.swing.JTextField();
        jSeparator8 = new javax.swing.JSeparator();
        lbl_RPC_error = new javax.swing.JLabel();
        lbl_RPS_titulo2 = new javax.swing.JLabel();
        lbl_RPS_error = new javax.swing.JLabel();
        lbl_RPS_titulo1 = new javax.swing.JLabel();
        txt_RPS_Pregunta = new javax.swing.JTextField();
        lbl_RPC_desc = new javax.swing.JLabel();
        lbl_RPC_titulo2 = new javax.swing.JLabel();
        lbl_RPC_titulo1 = new javax.swing.JLabel();
        txt_RPC_Pregunta = new javax.swing.JTextField();
        lbl_RPH_titulo1 = new javax.swing.JLabel();
        txt_RPH_Pregunta = new javax.swing.JTextField();
        lbl_RPB_desc = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        lbl_RPB_error = new javax.swing.JLabel();
        lbl_RPH_titulo2 = new javax.swing.JLabel();
        lbl_RPH_error = new javax.swing.JLabel();
        lbl_RPB_titulo2 = new javax.swing.JLabel();
        txt_RPB_Pregunta = new javax.swing.JTextField();
        lbl_RPB_titulo1 = new javax.swing.JLabel();
        lbl_RTR_desc = new javax.swing.JLabel();
        lbl_RVM_titulo4 = new javax.swing.JLabel();
        txt_RNB_Pregunta2 = new javax.swing.JTextField();
        txt_RNE_Pregunta2 = new javax.swing.JTextField();
        lbl_RNB_titulo3 = new javax.swing.JLabel();
        lbl_RNE_titulo3 = new javax.swing.JLabel();
        btn_Q2C_ayuda = new javax.swing.JButton();
        lbl_titulo2 = new javax.swing.JLabel();
        btn_RAT_ayuda = new javax.swing.JButton();
        btn_RTR_ayuda = new javax.swing.JButton();
        btn_REB_ayuda = new javax.swing.JButton();
        btn_REL_ayuda = new javax.swing.JButton();
        btn_RBI_ayuda = new javax.swing.JButton();
        btn_RPM_ayuda = new javax.swing.JButton();
        btn_RER_ayuda = new javax.swing.JButton();
        btn_RAR_ayuda = new javax.swing.JButton();
        btn_RAU_ayuda = new javax.swing.JButton();
        btn_RVM_ayuda = new javax.swing.JButton();
        btn_RLC_ayuda = new javax.swing.JButton();
        btn_RNB_ayuda = new javax.swing.JButton();
        btn_RCP_ayuda = new javax.swing.JButton();
        btn_RPC_ayuda = new javax.swing.JButton();
        btn_RPB_ayuda = new javax.swing.JButton();
        btn_close2 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1024, 1524));

        jsp_Rejillas.setPreferredSize(new java.awt.Dimension(1024, 1400));

        jp_Componentes.setPreferredSize(new java.awt.Dimension(1024, 1650));
        jp_Componentes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jp_Componentes.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 38, 1061, -1));

        lbl_titulo1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_titulo1.setForeground(new java.awt.Color(51, 51, 51));
        lbl_titulo1.setText("Titulo");
        jp_Componentes.add(lbl_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 300, 30));

        btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Button_Salva_Guardar_21.png"))); // NOI18N
        btn_guardar.setText("save");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1450, 120, -1));
        jp_Componentes.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 127, 1061, -1));

        txt_Q2C_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q2C_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 50, 130, 25));

        lbl_Q2C_titulo1.setText("Titulo");
        lbl_Q2C_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2C_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 47, 300, 30));

        lbl_Q2C_titulo2.setText("Titulo");
        lbl_Q2C_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q2C_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 60, 80, 25));

        lbl_Q3C_titulo1.setText("Titulo");
        lbl_Q3C_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q3C_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 92, 300, 30));

        txt_Q3C_Pregunta.setEditable(false);
        jp_Componentes.add(txt_Q3C_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 90, 130, 25));

        lbl_Q3C_titulo2.setText("Titulo");
        lbl_Q3C_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_Q3C_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 90, 80, 25));
        jp_Componentes.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 412, 1071, 2));

        btn_close.setText("Salir");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });
        jp_Componentes.add(btn_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1490, 120, 30));

        lbl_save_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error.setText(" . . .");
        jp_Componentes.add(lbl_save_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 1450, 400, 35));

        lbl_save_error2.setForeground(new java.awt.Color(153, 0, 51));
        lbl_save_error2.setText(" . . .");
        jp_Componentes.add(lbl_save_error2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 1490, 400, 35));

        lbl_RPL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RPL_error.setText(" . . .");
        jp_Componentes.add(lbl_RPL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 130, 340, 35));

        lbl_RPL_titulo2.setText("Titulo");
        lbl_RPL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 140, 80, 25));

        txt_RPL_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_RPL_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_RPL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 140, 130, 25));

        lbl_RPL_titulo1.setText("Titulo");
        lbl_RPL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 300, 30));

        lbl_RTR_titulo2.setText("Titulo");
        lbl_RTR_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RTR_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 210, 80, 25));

        lbl_RTR_titulo1.setText("Titulo");
        lbl_RTR_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RTR_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 300, 30));

        txt_RAR_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RAR_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 680, 130, 25));

        lbl_RAR_titulo1.setText("Titulo");
        lbl_RAR_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RAR_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 670, 300, 30));

        lbl_RLC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RLC_error.setText(" . . .");
        jp_Componentes.add(lbl_RLC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 850, 340, 35));

        lbl_RLC_titulo2.setText("Titulo");
        lbl_RLC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RLC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 860, 80, 25));

        lbl_RAR_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RAR_error.setText(" . . .");
        jp_Componentes.add(lbl_RAR_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 670, 340, 35));

        txt_RLC_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RLC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 860, 130, 25));

        lbl_RLC_titulo1.setText("Titulo");
        lbl_RLC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RLC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 860, 300, 30));

        lbl_RAR_titulo2.setText("Titulo");
        lbl_RAR_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RAR_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 680, 80, 25));

        lbl_REL_titulo1.setText("Titulo");
        lbl_REL_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_REL_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 300, 30));

        lbl_REL_titulo2.setText("Titulo");
        lbl_REL_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_REL_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 290, 80, 25));

        txt_REL_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_REL_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_REL_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 290, 130, 25));

        lbl_RIB_titulo1.setText("Titulo");
        lbl_RIB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RIB_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 300, 30));

        txt_RIB_Pregunta.setEnabled(false);
        txt_RIB_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_RIB_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_RIB_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 330, 130, 25));

        lbl_RIB_titulo2.setText("Titulo");
        lbl_RIB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RIB_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 330, 80, 25));

        lbl_REL_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_REL_error.setText(" . . .");
        jp_Componentes.add(lbl_REL_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 280, 340, 35));

        lbl_RIB_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RIB_error.setText(" . . .");
        jp_Componentes.add(lbl_RIB_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 320, 340, 35));
        jp_Componentes.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 543, 1071, 2));

        lbl_RPM_desc.setText("Desc");
        lbl_RPM_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPM_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 420, 610, 30));

        lbl_RBI_pregunta1.setText("Titulo");
        jp_Componentes.add(lbl_RBI_pregunta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 370, 80, 25));

        lbl_RBI_pregunta2.setText("Titulo");
        jp_Componentes.add(lbl_RBI_pregunta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 370, 80, 25));

        lbl_RPM_titulo1.setText("Titulo");
        lbl_RPM_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPM_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 300, 30));

        lbl_RPM_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RPM_error.setText(" . . .");
        jp_Componentes.add(lbl_RPM_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 460, 340, 35));

        lbl_RPM_titulo2.setText("Titulo");
        lbl_RPM_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPM_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 460, 80, 25));

        txt_RPM_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_RPM_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_RPM_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 460, 130, 25));

        lbl_RPN_titulo1.setText("Titulo");
        lbl_RPN_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPN_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 500, 300, 30));

        lbl_RPN_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RPN_error.setText(" . . .");
        jp_Componentes.add(lbl_RPN_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 500, 340, 35));

        lbl_RPN_titulo2.setText("Titulo");
        lbl_RPN_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPN_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 500, 80, 25));
        jp_Componentes.add(txt_RPN_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 500, 130, 25));

        lbl_RVM_titulo2.setText("Titulo");
        lbl_RVM_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RVM_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 970, 80, 25));

        lbl_RVM_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RVM_error.setText(" . . .");
        jp_Componentes.add(lbl_RVM_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 960, 340, 35));

        lbl_RVM_titulo1.setText("Titulo");
        lbl_RVM_titulo1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lbl_RVM_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RVM_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 970, 300, 30));

        txt_RVM_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RVM_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 970, 130, 25));

        txt_RER_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RER_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 600, 130, 25));

        lbl_RAU_titulo1.setText("Titulo");
        lbl_RAU_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RAU_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 550, 300, 30));

        lbl_RER_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RER_error.setText(" . . .");
        jp_Componentes.add(lbl_RER_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 590, 340, 35));

        lbl_RER_titulo2.setText("Titulo");
        lbl_RER_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RER_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 600, 80, 25));

        txt_RAU_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RAU_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 560, 130, 25));

        lbl_RER_titulo1.setText("Titulo");
        lbl_RER_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RER_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 590, 300, 30));

        lbl_RAU_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RAU_error.setText(" . . .");
        jp_Componentes.add(lbl_RAU_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 550, 340, 35));

        lbl_RAU_titulo2.setText("Titulo");
        lbl_RAU_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RAU_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 560, 80, 25));

        lbl_RAT_titulo2.setText("Titulo");
        lbl_RAT_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RAT_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 640, 80, 25));

        lbl_RAT_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RAT_error.setText(" . . .");
        jp_Componentes.add(lbl_RAT_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 630, 340, 35));

        lbl_RAT_titulo1.setText("Titulo");
        lbl_RAT_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RAT_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 630, 300, 30));

        txt_RAT_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RAT_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 640, 130, 25));
        jp_Componentes.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1050, 1051, 2));

        jp_Componentes.add(ddm_RTR_pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 210, 130, 25));

        lbl_REB_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_REB_error.setText(" . . .");
        jp_Componentes.add(lbl_REB_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 240, 340, 35));

        txt_REB_Pregunta.setEditable(false);
        jp_Componentes.add(txt_REB_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 250, 130, 25));

        lbl_REB_titulo1.setText("Titulo");
        lbl_REB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_REB_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 300, 30));

        lbl_REB_titulo2.setText("Titulo");
        lbl_REB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_REB_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 250, 80, 25));

        lbl_RBI_desc.setText("Titulo");
        lbl_RBI_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RBI_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 300, 30));

        lbl_RCP_desc.setText("Desc");
        lbl_RCP_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RCP_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 730, 387, 30));

        lbl_RCP_titulo4.setText("Titulo");
        lbl_RCP_titulo4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RCP_titulo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 730, 80, 25));

        lbl_RCP_titulo3.setText("Titulo");
        lbl_RCP_titulo3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RCP_titulo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 730, 80, 25));

        txt_RCP_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RCP_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 780, 130, 25));

        lbl_RCM_titulo1.setText("Titulo");
        lbl_RCM_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RCM_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 820, 300, 30));

        txt_RCM_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RCM_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 820, 130, 25));

        lbl_RCM_titulo2.setText("Titulo");
        lbl_RCM_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RCM_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 820, 80, 25));

        lbl_RCM_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RCM_error.setText(" . . .");
        jp_Componentes.add(lbl_RCM_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 810, 340, 35));

        lbl_RCP_titulo2.setText("Titulo");
        lbl_RCP_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RCP_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 780, 80, 25));
        jp_Componentes.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 902, 1061, 2));

        lbl_RCP_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RCP_error.setText(" . . .");
        jp_Componentes.add(lbl_RCP_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 770, 340, 35));

        lbl_RCP_titulo1.setText("Titulo");
        lbl_RCP_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RCP_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 770, 300, 30));

        lbl_RVN_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RVN_error.setText(" . . .");
        jp_Componentes.add(lbl_RVN_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1000, 340, 35));

        lbl_RVN_titulo2.setText("Titulo");
        lbl_RVN_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RVN_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1010, 80, 25));

        txt_RVN_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RVN_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1010, 130, 25));

        lbl_RVN_titulo1.setText("Titulo");
        lbl_RVN_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RVN_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1010, 300, 30));

        lbl_RVM_desc.setText("Desc");
        lbl_RVM_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RVM_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 910, 386, 50));
        jp_Componentes.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 722, 1061, 2));

        lbl_RVM_titulo3.setText("Titulo");
        lbl_RVM_titulo3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RVM_titulo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 910, 80, 25));

        lbl_RNB_desc.setText("Desc");
        lbl_RNB_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RNB_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1060, 610, 30));

        lbl_RNE_titulo1.setText("Titulo");
        lbl_RNE_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RNE_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1130, 250, 30));

        txt_RNE_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RNE_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 1140, 130, 25));

        lbl_RNE_titulo2.setText("Titulo");
        lbl_RNE_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RNE_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 1140, 80, 25));

        lbl_RNE_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RNE_error.setText(" . . .");
        jp_Componentes.add(lbl_RNE_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 1130, 270, 35));
        jp_Componentes.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1180, 1061, 2));

        lbl_RNB_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RNB_error.setText(" . . .");
        jp_Componentes.add(lbl_RNB_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 1080, 270, 35));

        lbl_RNB_titulo2.setText("Titulo");
        lbl_RNB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RNB_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 1090, 80, 25));

        lbl_RNB_titulo1.setText("Titulo");
        lbl_RNB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RNB_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1090, 250, 30));

        txt_RNB_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RNB_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 1090, 130, 25));
        jp_Componentes.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1310, 1061, 2));

        lbl_RPC_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RPC_error.setText(" . . .");
        jp_Componentes.add(lbl_RPC_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1220, 340, 35));

        lbl_RPS_titulo2.setText("Titulo");
        lbl_RPS_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPS_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1270, 80, 25));

        lbl_RPS_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RPS_error.setText(" . . .");
        jp_Componentes.add(lbl_RPS_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1260, 340, 35));

        lbl_RPS_titulo1.setText("Titulo");
        lbl_RPS_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPS_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1270, 300, 30));

        txt_RPS_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RPS_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1270, 130, 25));

        lbl_RPC_desc.setText("Desc");
        lbl_RPC_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPC_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1190, 610, 30));

        lbl_RPC_titulo2.setText("Titulo");
        lbl_RPC_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPC_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1230, 80, 25));

        lbl_RPC_titulo1.setText("Titulo");
        lbl_RPC_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPC_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1230, 300, 30));

        txt_RPC_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RPC_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1230, 130, 25));

        lbl_RPH_titulo1.setText("Titulo");
        lbl_RPH_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPH_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1390, 300, 30));

        txt_RPH_Pregunta.setEditable(false);
        jp_Componentes.add(txt_RPH_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1390, 130, 25));

        lbl_RPB_desc.setText("Desc");
        lbl_RPB_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPB_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1320, 610, 30));
        jp_Componentes.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1430, 1061, 2));

        lbl_RPB_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RPB_error.setText(" . . .");
        jp_Componentes.add(lbl_RPB_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1350, 340, 35));

        lbl_RPH_titulo2.setText("Titulo");
        lbl_RPH_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPH_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1390, 80, 25));

        lbl_RPH_error.setForeground(new java.awt.Color(153, 0, 51));
        lbl_RPH_error.setText(" . . .");
        jp_Componentes.add(lbl_RPH_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 1390, 340, 35));

        lbl_RPB_titulo2.setText("Titulo");
        lbl_RPB_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPB_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 1350, 80, 25));

        txt_RPB_Pregunta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_RPB_PreguntaKeyReleased(evt);
            }
        });
        jp_Componentes.add(txt_RPB_Pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1350, 130, 25));

        lbl_RPB_titulo1.setText("Titulo");
        lbl_RPB_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RPB_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1350, 300, 30));

        lbl_RTR_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_RTR_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_RTR_desc.setText("Desc");
        lbl_RTR_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RTR_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 610, 30));

        lbl_RVM_titulo4.setText("Titulo");
        lbl_RVM_titulo4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RVM_titulo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 910, 80, 25));

        txt_RNB_Pregunta2.setEditable(false);
        jp_Componentes.add(txt_RNB_Pregunta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 1090, 130, 25));

        txt_RNE_Pregunta2.setEditable(false);
        jp_Componentes.add(txt_RNE_Pregunta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 1140, 130, 25));

        lbl_RNB_titulo3.setText("Titulo");
        lbl_RNB_titulo3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RNB_titulo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 1090, 80, 25));

        lbl_RNE_titulo3.setText("Titulo");
        lbl_RNE_titulo3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Componentes.add(lbl_RNE_titulo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 1140, 80, 25));

        btn_Q2C_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_Q2C_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_Q2C_ayuda.setContentAreaFilled(false);
        btn_Q2C_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_Q2C_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 50, 25, 25));

        lbl_titulo2.setForeground(new java.awt.Color(0, 51, 102));
        lbl_titulo2.setText("Desc");
        jp_Componentes.add(lbl_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 0, 600, 30));

        btn_RAT_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RAT_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RAT_ayuda.setContentAreaFilled(false);
        btn_RAT_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RAT_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 640, 25, 25));

        btn_RTR_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RTR_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RTR_ayuda.setContentAreaFilled(false);
        btn_RTR_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RTR_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 210, 25, 25));

        btn_REB_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_REB_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_REB_ayuda.setContentAreaFilled(false);
        btn_REB_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_REB_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 250, 25, 25));

        btn_REL_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_REL_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_REL_ayuda.setContentAreaFilled(false);
        btn_REL_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_REL_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 290, 25, 25));

        btn_RBI_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RBI_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RBI_ayuda.setContentAreaFilled(false);
        btn_RBI_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RBI_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 370, 25, 25));

        btn_RPM_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RPM_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RPM_ayuda.setContentAreaFilled(false);
        btn_RPM_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RPM_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 420, 25, 25));

        btn_RER_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RER_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RER_ayuda.setContentAreaFilled(false);
        btn_RER_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RER_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 600, 25, 25));

        btn_RAR_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RAR_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RAR_ayuda.setContentAreaFilled(false);
        btn_RAR_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RAR_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 680, 25, 25));

        btn_RAU_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RAU_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RAU_ayuda.setContentAreaFilled(false);
        btn_RAU_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RAU_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 560, 25, 25));

        btn_RVM_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RVM_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RVM_ayuda.setContentAreaFilled(false);
        btn_RVM_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RVM_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 910, 25, -1));

        btn_RLC_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RLC_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RLC_ayuda.setContentAreaFilled(false);
        btn_RLC_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RLC_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 860, 25, -1));

        btn_RNB_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RNB_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RNB_ayuda.setContentAreaFilled(false);
        btn_RNB_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RNB_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 1060, 25, -1));

        btn_RCP_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RCP_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RCP_ayuda.setContentAreaFilled(false);
        btn_RCP_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RCP_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 780, 25, -1));

        btn_RPC_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RPC_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RPC_ayuda.setContentAreaFilled(false);
        btn_RPC_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RPC_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 1190, 25, -1));

        btn_RPB_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_RPB_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_RPB_ayuda.setContentAreaFilled(false);
        btn_RPB_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N
        jp_Componentes.add(btn_RPB_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 1320, 25, -1));

        jsp_Rejillas.setViewportView(jp_Componentes);

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
            .addComponent(jsp_Rejillas, javax.swing.GroupLayout.DEFAULT_SIZE, 1008, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_close2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btn_close2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jsp_Rejillas, javax.swing.GroupLayout.DEFAULT_SIZE, 1700, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_closeActionPerformed
        bod.WinRejillas = false;
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

    private void txt_RPM_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_RPM_PreguntaKeyReleased
        // Calcular temporalmente a:
        calcularRAU();
    }//GEN-LAST:event_txt_RPM_PreguntaKeyReleased

    private void txt_REL_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_REL_PreguntaKeyReleased
        // Calcular temporalmente a:
        calcularRER();
        calcularRNB();
        calcularRNE();
        set_RNE();
        calcularRPH();
    }//GEN-LAST:event_txt_REL_PreguntaKeyReleased

    private void txt_RPL_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_RPL_PreguntaKeyReleased
        // Calcular temporalmente a:
        calcularRAR();
        calcularRCP();
        calcularRCM();
        calcularRVM();
        calcularRVN();
    }//GEN-LAST:event_txt_RPL_PreguntaKeyReleased

    private void txt_RPB_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_RPB_PreguntaKeyReleased
        // Calcular temporalmente a:
        calcularRPH();
    }//GEN-LAST:event_txt_RPB_PreguntaKeyReleased

    private void txt_RIB_PreguntaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_RIB_PreguntaKeyReleased
        // Calcular temporalmente a:
        calcularRPH();
    }//GEN-LAST:event_txt_RIB_PreguntaKeyReleased

    private void btn_close2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_close2ActionPerformed
        bod.WinRejillas = false;
        this.dispose();
    }//GEN-LAST:event_btn_close2ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Q2C_ayuda;
    private javax.swing.JButton btn_RAR_ayuda;
    private javax.swing.JButton btn_RAT_ayuda;
    private javax.swing.JButton btn_RAU_ayuda;
    private javax.swing.JButton btn_RBI_ayuda;
    private javax.swing.JButton btn_RCP_ayuda;
    private javax.swing.JButton btn_REB_ayuda;
    private javax.swing.JButton btn_REL_ayuda;
    private javax.swing.JButton btn_RER_ayuda;
    private javax.swing.JButton btn_RLC_ayuda;
    private javax.swing.JButton btn_RNB_ayuda;
    private javax.swing.JButton btn_RPB_ayuda;
    private javax.swing.JButton btn_RPC_ayuda;
    private javax.swing.JButton btn_RPM_ayuda;
    private javax.swing.JButton btn_RTR_ayuda;
    private javax.swing.JButton btn_RVM_ayuda;
    private javax.swing.JButton btn_close;
    private javax.swing.JButton btn_close2;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JComboBox ddm_RTR_pregunta;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JPanel jp_Componentes;
    private javax.swing.JScrollPane jsp_Rejillas;
    private javax.swing.JLabel lbl_Q2C_titulo1;
    private javax.swing.JLabel lbl_Q2C_titulo2;
    private javax.swing.JLabel lbl_Q3C_titulo1;
    private javax.swing.JLabel lbl_Q3C_titulo2;
    private javax.swing.JLabel lbl_RAR_error;
    private javax.swing.JLabel lbl_RAR_titulo1;
    private javax.swing.JLabel lbl_RAR_titulo2;
    private javax.swing.JLabel lbl_RAT_error;
    private javax.swing.JLabel lbl_RAT_titulo1;
    private javax.swing.JLabel lbl_RAT_titulo2;
    private javax.swing.JLabel lbl_RAU_error;
    private javax.swing.JLabel lbl_RAU_titulo1;
    private javax.swing.JLabel lbl_RAU_titulo2;
    private javax.swing.JLabel lbl_RBI_desc;
    private javax.swing.JRadioButton lbl_RBI_pregunta1;
    private javax.swing.JRadioButton lbl_RBI_pregunta2;
    private javax.swing.JLabel lbl_RCM_error;
    private javax.swing.JLabel lbl_RCM_titulo1;
    private javax.swing.JLabel lbl_RCM_titulo2;
    private javax.swing.JLabel lbl_RCP_desc;
    private javax.swing.JLabel lbl_RCP_error;
    private javax.swing.JLabel lbl_RCP_titulo1;
    private javax.swing.JLabel lbl_RCP_titulo2;
    private javax.swing.JLabel lbl_RCP_titulo3;
    private javax.swing.JLabel lbl_RCP_titulo4;
    private javax.swing.JLabel lbl_REB_error;
    private javax.swing.JLabel lbl_REB_titulo1;
    private javax.swing.JLabel lbl_REB_titulo2;
    private javax.swing.JLabel lbl_REL_error;
    private javax.swing.JLabel lbl_REL_titulo1;
    private javax.swing.JLabel lbl_REL_titulo2;
    private javax.swing.JLabel lbl_RER_error;
    private javax.swing.JLabel lbl_RER_titulo1;
    private javax.swing.JLabel lbl_RER_titulo2;
    private javax.swing.JLabel lbl_RIB_error;
    private javax.swing.JLabel lbl_RIB_titulo1;
    private javax.swing.JLabel lbl_RIB_titulo2;
    private javax.swing.JLabel lbl_RLC_error;
    private javax.swing.JLabel lbl_RLC_titulo1;
    private javax.swing.JLabel lbl_RLC_titulo2;
    private javax.swing.JLabel lbl_RNB_desc;
    private javax.swing.JLabel lbl_RNB_error;
    private javax.swing.JLabel lbl_RNB_titulo1;
    private javax.swing.JLabel lbl_RNB_titulo2;
    private javax.swing.JLabel lbl_RNB_titulo3;
    private javax.swing.JLabel lbl_RNE_error;
    private javax.swing.JLabel lbl_RNE_titulo1;
    private javax.swing.JLabel lbl_RNE_titulo2;
    private javax.swing.JLabel lbl_RNE_titulo3;
    private javax.swing.JLabel lbl_RPB_desc;
    private javax.swing.JLabel lbl_RPB_error;
    private javax.swing.JLabel lbl_RPB_titulo1;
    private javax.swing.JLabel lbl_RPB_titulo2;
    private javax.swing.JLabel lbl_RPC_desc;
    private javax.swing.JLabel lbl_RPC_error;
    private javax.swing.JLabel lbl_RPC_titulo1;
    private javax.swing.JLabel lbl_RPC_titulo2;
    private javax.swing.JLabel lbl_RPH_error;
    private javax.swing.JLabel lbl_RPH_titulo1;
    private javax.swing.JLabel lbl_RPH_titulo2;
    private javax.swing.JLabel lbl_RPL_error;
    private javax.swing.JLabel lbl_RPL_titulo1;
    private javax.swing.JLabel lbl_RPL_titulo2;
    private javax.swing.JLabel lbl_RPM_desc;
    private javax.swing.JLabel lbl_RPM_error;
    private javax.swing.JLabel lbl_RPM_titulo1;
    private javax.swing.JLabel lbl_RPM_titulo2;
    private javax.swing.JLabel lbl_RPN_error;
    private javax.swing.JLabel lbl_RPN_titulo1;
    private javax.swing.JLabel lbl_RPN_titulo2;
    private javax.swing.JLabel lbl_RPS_error;
    private javax.swing.JLabel lbl_RPS_titulo1;
    private javax.swing.JLabel lbl_RPS_titulo2;
    private javax.swing.JLabel lbl_RTR_desc;
    private javax.swing.JLabel lbl_RTR_titulo1;
    private javax.swing.JLabel lbl_RTR_titulo2;
    private javax.swing.JLabel lbl_RVM_desc;
    private javax.swing.JLabel lbl_RVM_error;
    private javax.swing.JLabel lbl_RVM_titulo1;
    private javax.swing.JLabel lbl_RVM_titulo2;
    private javax.swing.JLabel lbl_RVM_titulo3;
    private javax.swing.JLabel lbl_RVM_titulo4;
    private javax.swing.JLabel lbl_RVN_error;
    private javax.swing.JLabel lbl_RVN_titulo1;
    private javax.swing.JLabel lbl_RVN_titulo2;
    private javax.swing.JLabel lbl_save_error;
    private javax.swing.JLabel lbl_save_error2;
    private javax.swing.JLabel lbl_titulo1;
    private javax.swing.JLabel lbl_titulo2;
    private javax.swing.JTextField txt_Q2C_Pregunta;
    private javax.swing.JTextField txt_Q3C_Pregunta;
    private javax.swing.JTextField txt_RAR_Pregunta;
    private javax.swing.JTextField txt_RAT_Pregunta;
    private javax.swing.JTextField txt_RAU_Pregunta;
    private javax.swing.JTextField txt_RCM_Pregunta;
    private javax.swing.JTextField txt_RCP_Pregunta;
    private javax.swing.JTextField txt_REB_Pregunta;
    private javax.swing.JTextField txt_REL_Pregunta;
    private javax.swing.JTextField txt_RER_Pregunta;
    private javax.swing.JTextField txt_RIB_Pregunta;
    private javax.swing.JTextField txt_RLC_Pregunta;
    private javax.swing.JTextField txt_RNB_Pregunta;
    private javax.swing.JTextField txt_RNB_Pregunta2;
    private javax.swing.JTextField txt_RNE_Pregunta;
    private javax.swing.JTextField txt_RNE_Pregunta2;
    private javax.swing.JTextField txt_RPB_Pregunta;
    private javax.swing.JTextField txt_RPC_Pregunta;
    private javax.swing.JTextField txt_RPH_Pregunta;
    private javax.swing.JTextField txt_RPL_Pregunta;
    private javax.swing.JTextField txt_RPM_Pregunta;
    private javax.swing.JTextField txt_RPN_Pregunta;
    private javax.swing.JTextField txt_RPS_Pregunta;
    private javax.swing.JTextField txt_RVM_Pregunta;
    private javax.swing.JTextField txt_RVN_Pregunta;
    // End of variables declaration//GEN-END:variables
}
