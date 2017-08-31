package UI;

import BO.Bod;
import Componentes.C2_ProyeccionPoblacional;
import Componentes.C3_CalculoCaudalesDiseno;
import Componentes.C5_Rejillas;
import Componentes.C6_Desarenador;
import Componentes.C7_LagunaAnaerobia;
import Componentes.C8_LagunaFacultativaSec;
import Componentes.Configuracion;
import Componentes.Util;
import Componentes.Validaciones;
import DB.Dao;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.plaf.basic.BasicInternalFrameUI;

public class Main extends javax.swing.JFrame {

    private Configuracion conf = new Configuracion();
    static Logger log = Logger.getLogger("Main"); //Nombre que tomará en el log. Tambièn puede ser: Main.class.getName()
    private Bienvenido bienvenido;
    private DatosEntrada DatosEntrada;
    private ProyeccionPoblacional Proyeccionpoblacional;
    private CalculoCaudalesDiseno Calculocaudalesdiseno;
    private CaracterAguaResidual Caractinicaguaresidual;
    private Rejillas rejillas;
    private Desarenador desarenador;
    private LagunaAnaerobia Lagunaanaerobia;
    private LagunaFacultativaSec Lagunafacultativasec;
    private ReactorUASB ReactorUasb;
    private FiltroPercolador filtropercolador;
    private LodosActivadsModConv lodosactivados;
    private Resultado resultado;
    private Dao db;
    public String Generalpath = "";//path de recursos del proyecto
    boolean winbienvenido; //controla si Bienvenido está abierto
    Util util;
    Bod bod;

    public Main() { //Todo:, los proyectos deben ser exportables, ya que solo funcionan en la bd del equipo
        initComponents();
        IniciarLog();
        InicializarComponentes();
    }

    public void InicializarComponentes() {//Todo: mensajes desde la bd

        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                int n = util.Mensaje("¿Desea cerrar el Trabajo existente?", "yesno");
                if (n == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        menu_Archivo.setPreferredSize((new Dimension(75, 65)));
        menu_Archivo.setHorizontalTextPosition(SwingConstants.CENTER);
        menu_Archivo.setVerticalTextPosition(SwingConstants.BOTTOM);

        menu_Datos.setPreferredSize((new Dimension(75, 65)));
        menu_Datos.setHorizontalTextPosition(SwingConstants.CENTER);
        menu_Datos.setVerticalTextPosition(SwingConstants.BOTTOM);

        menu_Tecnologias.setPreferredSize((new Dimension(75, 65)));
        menu_Tecnologias.setHorizontalTextPosition(SwingConstants.CENTER);
        menu_Tecnologias.setVerticalTextPosition(SwingConstants.BOTTOM);

        menu_Resultados.setPreferredSize((new Dimension(75, 65)));
        menu_Resultados.setHorizontalTextPosition(SwingConstants.CENTER);
        menu_Resultados.setVerticalTextPosition(SwingConstants.BOTTOM);

        menu_Acerca.setPreferredSize((new Dimension(75, 65)));
        menu_Acerca.setHorizontalTextPosition(SwingConstants.CENTER);
        menu_Acerca.setVerticalTextPosition(SwingConstants.BOTTOM);

        bod = new Bod(Generalpath);
        util = new Util();
        db = new Dao(conf.ObtenerKey("rutabasedatos") + File.separator + conf.ObtenerKey("nombrebasedatos"));
        winbienvenido = false;
        CargarBienvenido(false);//parámetro de escritura
        //todo: try
    }

    /**
     * Configura el log de eventos Errores, Advertencias, etc. En la carpeta
     * Resources està el archivo de configuración.
     */
    private void IniciarLog() {

        BasicConfigurator.configure();
        try {
            Generalpath = System.getProperty("user.dir") + File.separator + "src";
            InputStream input = new FileInputStream(Generalpath + File.separator + "Resources" + File.separator + "log4j.properties");
            Properties prop = new Properties();
            prop.load(input);
            PropertyConfigurator.configure(prop);

            File filelogs = new File(System.getProperty("user.dir") + File.separator + "logs");
            if (!filelogs.exists()) {
                filelogs.mkdirs();//Trata de crear la carpeta de logs
            }
        } catch (Exception ex) {
            //todo: crear una advertencia visual para indicar que el log no cargó / o tambièn cargarlo por default de otra forma
        }
    }

    /**
     * *
     * Comprueba el orden en que deben ser habilitados los botones segun si la
     * edicion de datos de la sección anterior ha sido completada
     */
    public void comprobarBotones() { //Todo quitar todos los else

        if (bod.EditDatosDeEntrada) {

            menuitem_ProyeccionPoblacional.setEnabled(true);

            if (bod.EditProyeccionPoblacional) {

                menuitem_CalculoCaudalesDiseno.setEnabled(true);

                if (bod.EditCalculoCaudalesDiseno) {

                    menuitem_CaractInicAguaResidual.setEnabled(true);

                    if (bod.EditCaractInicAguaResidual) {
                        menu_Tecnologia0.setEnabled(true);
                        menuitem_rejillas.setEnabled(true);

                        if (bod.EditRejillas) {
                            menuitem_desarenador.setEnabled(true);
                            menuItem_Res_Rejillas.setEnabled(true);

                            if (bod.EditDesarenador) {
                                menuItem_Res_Desarenador.setEnabled(true);
                                menuItem_Res_Parshall.setEnabled(true);
                                menu_Tecnologia1.setEnabled(true);
                                menuitem_lagunaerobia.setEnabled(true);

                                if (bod.EditLagunaAnaerobia) {
                                    menuitem_lagunafacul1.setEnabled(true);
                                    menuItem_Res_LagunaAnaerobia.setEnabled(true);

                                    if (bod.EditLagunaFacultativaSec) {
                                        menuItem_Res_LagunaFacultativa.setEnabled(true);
                                    } else {
                                        menuItem_Res_LagunaFacultativa.setEnabled(false);
                                    }
                                } else {
                                    menuitem_lagunafacul1.setEnabled(false);
                                    menuItem_Res_LagunaAnaerobia.setEnabled(false);
                                    menuItem_Res_LagunaFacultativa.setEnabled(false);
                                }
                            } else {
                                menuItem_Res_Desarenador.setEnabled(false);
                                menuItem_Res_Parshall.setEnabled(false);
                                menu_Tecnologia1.setEnabled(false);
                                menuitem_lagunaerobia.setEnabled(false);
                                menuitem_lagunafacul1.setEnabled(false);
                                menuItem_Res_LagunaAnaerobia.setEnabled(false);
                                menuItem_Res_LagunaFacultativa.setEnabled(false);
                            }
                        } else {
                            menuitem_desarenador.setEnabled(false);
                            menuItem_Res_Rejillas.setEnabled(false);
                            menuItem_Res_Desarenador.setEnabled(false);
                            menuItem_Res_Parshall.setEnabled(false);
                            menu_Tecnologia1.setEnabled(false);
                            menuitem_lagunaerobia.setEnabled(false);
                            menuitem_lagunafacul1.setEnabled(false);
                            menuItem_Res_LagunaAnaerobia.setEnabled(false);
                            menuItem_Res_LagunaFacultativa.setEnabled(false);
                        }
                    } else {
                        menu_Tecnologia0.setEnabled(false);
                        menuitem_rejillas.setEnabled(false);
                        menuitem_desarenador.setEnabled(false);
                        menuItem_Res_Rejillas.setEnabled(false);
                        menuItem_Res_Desarenador.setEnabled(false);
                        menuItem_Res_Parshall.setEnabled(false);
                        menu_Tecnologia1.setEnabled(false);
                        menuitem_lagunaerobia.setEnabled(false);
                        menuitem_lagunafacul1.setEnabled(false);
                        menuItem_Res_LagunaAnaerobia.setEnabled(false);
                        menuItem_Res_LagunaFacultativa.setEnabled(false);
                    }
                } else {
                    menu_Tecnologia0.setEnabled(false);
                    menuitem_rejillas.setEnabled(false);
                    menuitem_desarenador.setEnabled(false);
                    menuItem_Res_Rejillas.setEnabled(false);
                    menuItem_Res_Desarenador.setEnabled(false);
                    menuItem_Res_Parshall.setEnabled(false);
                    menuitem_CaractInicAguaResidual.setEnabled(false);
                    menu_Tecnologia1.setEnabled(false);
                    menuitem_lagunaerobia.setEnabled(false);
                    menuitem_lagunafacul1.setEnabled(false);
                    menuItem_Res_LagunaAnaerobia.setEnabled(false);
                    menuItem_Res_LagunaFacultativa.setEnabled(false);
                }
            } else {
                menu_Tecnologia0.setEnabled(false);
                menuitem_rejillas.setEnabled(false);
                menuitem_desarenador.setEnabled(false);
                menuItem_Res_Rejillas.setEnabled(false);
                menuItem_Res_Desarenador.setEnabled(false);
                menuItem_Res_Parshall.setEnabled(false);
                menuitem_CalculoCaudalesDiseno.setEnabled(false);
                menuitem_CaractInicAguaResidual.setEnabled(false);
                menu_Tecnologia1.setEnabled(false);
                menuitem_lagunaerobia.setEnabled(false);
                menuitem_lagunafacul1.setEnabled(false);
                menuItem_Res_LagunaAnaerobia.setEnabled(false);
                menuItem_Res_LagunaFacultativa.setEnabled(false);
            }
        } else {
            menu_Tecnologia0.setEnabled(false);
            menuitem_rejillas.setEnabled(false);
            menuitem_desarenador.setEnabled(false);
            menuItem_Res_Rejillas.setEnabled(false);
            menuItem_Res_Desarenador.setEnabled(false);
            menuItem_Res_Parshall.setEnabled(false);
            menuitem_ProyeccionPoblacional.setEnabled(false);
            menuitem_CalculoCaudalesDiseno.setEnabled(false);
            menuitem_CaractInicAguaResidual.setEnabled(false);
            menu_Tecnologia1.setEnabled(false);
            menuitem_lagunaerobia.setEnabled(false);
            menuitem_lagunafacul1.setEnabled(false);
            menuItem_Res_LagunaAnaerobia.setEnabled(false);
            menuItem_Res_LagunaFacultativa.setEnabled(false);
        }
    }

    private boolean CerrarVentanas() {

        if (bod.idproyecto > 0) {
            int n = util.Mensaje("¿Desea cerrar el Trabajo Existente?", "yesno");
            if (n != JOptionPane.YES_OPTION) {
                return false;
            }
        }
        jdp_Main.removeAll();//Cerrar las ventanas        
        this.bod = null;//Borra toda instancia para dejar la clase en blanco y borrar variables
        revalidate();
        repaint();
        this.bod = new Bod(Generalpath);
        menuitem_DatosEntrada.setEnabled(false);
        comprobarBotones();
        winbienvenido = false;
        return true;
    }

    /**
     * *
     * Comprueba datos afectados al hacer cambios en datos antiguos, ya que
     * algunos datos dependen de otros. Si el cambio es autorizado hace un
     * update del proyecto. Caso contrario advierte y desactiva pantallas ya
     * editadas
     *
     * @param pos desde donde debe comenzar a probar (orden de llenado de datos
     * por pantalla)
     */
    public void ComprobarCambio(int pos) {

        switch (pos) {

            case 2:
                if (bod.EditProyeccionPoblacional) {

                    C2_ProyeccionPoblacional C2pp = new C2_ProyeccionPoblacional(bod);
                    if (!C2pp.InicializarComponentes()) {
                        bod.EditCalculoCaudalesDiseno = false;//3
                        bod.EditCaractInicAguaResidual = false;//4
                        bod.EditLagunaAnaerobia = false;//5
                        bod.EditLagunaFacultativaSec = false;//6
                        bod.GuardarUpdateBod();

                        util.Mensaje("Error por corregir en Proyeccion Poblacional, \n" + C2pp.error, "error");
                    } else {//Todo bien con esta etapa, voy a la siguiente etapa
                        ComprobarCambio(3);
                    }
                }
                break;
            case 3:
                if (bod.EditCalculoCaudalesDiseno) {

                    C3_CalculoCaudalesDiseno C3pp = new C3_CalculoCaudalesDiseno(bod);
                    if (!C3pp.InicializarComponentes()) {
                        bod.EditCaractInicAguaResidual = false;//4
                        bod.EditLagunaAnaerobia = false;//5
                        bod.EditLagunaFacultativaSec = false;//6
                        bod.GuardarUpdateBod();

                        util.Mensaje("Error por corregir en Calculo de Caudales de Diseno, \n " + C3pp.error, "error");
                    } else {
                        ComprobarCambio(4);
                    }
                }
                break;

            case 4:
                if (bod.EditRejillas) {

                    C5_Rejillas C5pp = new C5_Rejillas(bod);
                    if (!C5pp.InicializarComponentes()) {
                        bod.EditDesarenador = false;//4
                        bod.EditLagunaAnaerobia = false;//5
                        bod.EditLagunaFacultativaSec = false;//6
                        bod.GuardarUpdateBod();

                        util.Mensaje("Error por corregir en Rejillas, \n " + C5pp.error, "error");
                    } else {
                        ComprobarCambio(5);
                    }
                }
                break;

            case 5:
                if (bod.EditDesarenador) { //El desarenador usa datos anteriores por esto se debe recalcular

                    C6_Desarenador C6pp = new C6_Desarenador(bod);
                    if (!C6pp.InicializarComponentes()) {
                        bod.EditLagunaAnaerobia = false;//5
                        bod.EditLagunaFacultativaSec = false;//6
                        bod.GuardarUpdateBod();

                        util.Mensaje("Error por corregir en Desarenador, \n " + C6pp.error, "error");
                    } else {
                        ComprobarCambio(6);
                    }
                }
                break;

            case 6:
                if (bod.EditLagunaAnaerobia) {

                    C7_LagunaAnaerobia C5pp = new C7_LagunaAnaerobia(bod);
                    if (!C5pp.InicializarComponentes()) {
                        bod.EditLagunaFacultativaSec = false;//6
                        bod.GuardarUpdateBod();

                        util.Mensaje("Error por corregir en Laguna Anaerobia, \n " + C5pp.error, "error");
                    } else {
                        ComprobarCambio(7); //Facultativa no depende(?) de Anaerobia, pero este llamado es el que viene en cadena dede (*3)
                    }
                }
                break;

            case 7:
                if (bod.EditLagunaFacultativaSec) {

                    C8_LagunaFacultativaSec C6pp = new C8_LagunaFacultativaSec(bod);
                    if (!C6pp.InicializarComponentes()) {
                        bod.GuardarUpdateBod();

                        util.Mensaje("Error por corregir en Laguna Facultativa Secundaria, \n " + C6pp.error, "error");
                    }
                }
                break;
        }
    }

    /**
     * Carga los datos de la pantalla de bienvenida, si el usuario cancela hace
     * un update en la tabla de cuestionarios.
     *
     * @param update : true hace update , false abre la ventana si esta activa
     * en bd
     */
    public void CargarBienvenido(boolean update) {//Todo, cambiar esto para traer toda la tabla y traer del bod valores autom..

        try {
            ResultSet rs;

            if (update) {
                int res = 0;
                String[] sql = new String[]{"0"};
                res = db.ResultadoUpdateInsert("actualizarbienvenido", sql); //trata de actualizar para que la pantalla de actualizar bienvenido no aparezca de nuevo.
            } else { //Abrir la ventana
                rs = db.ResultadoSelect("obtenerseccionalias", "INTRODUCN", null);

                int val = Integer.parseInt(rs.getString("Mensaje"));//Obtiene el valor 0 o 1 , si el usuario ha decidido no ver mas esta pantalla

                if (val > 0) {
                    AbrirBienvenido();
                }
            }
        } catch (Exception ex) {
            log.error("Error en CargarBienvenido() " + ex.getMessage());
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }

    /**
     * *
     * Carga los datos ya editados en el label de esta ventana principal. Para
     * que el usuario los pueda visualizar.
     */
    public void CargarDatosFondo() {

        if (!lbl_datosfondo2.isDisplayable()) {
            jdp_Main.add(lbl_datosfondo1);
            jdp_Main.add(lbl_datosfondo2);
        }

        if (bod.EditDatosDeEntrada) {

            try {
                String datos = "<HTML><style>table td {border: 1px solid #567288;border-collapse: collapse;} </style><table cellspacing='0' cellpadding='0'>"; //border='1' cellspacing='0'
                String datos2 = datos;
                ResultSet rs;

                rs = db.ResultadoSelect("obtenerseccion", "1", null);
                datos += "<tr bgcolor='#35556c'><td colspan='3'><b>" + rs.getString("Nombre") + "</b></td></tr>";
                rs = db.ResultadoSelect("obtenerpregunta", "1", "FCH");
                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getFCH() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                rs = db.ResultadoSelect("obtenerpregunta", "1", "TML");
                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getTML() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                rs = db.ResultadoSelect("obtenerpregunta", "1", "PCI");
                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getPCI() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                rs = db.ResultadoSelect("obtenerpregunta", "1", "TCI");
                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getTCI() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                rs = db.ResultadoSelect("obtenerpregunta", "1", "PUC");
                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getPUC() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                rs = db.ResultadoSelect("obtenerpregunta", "1", "TUC");
                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getTUC() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                rs = db.ResultadoSelect("obtenerpregunta", "1", "PDI");
                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getPDI() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                rs = db.ResultadoSelect("obtenerpregunta", "1", "CRE");
                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getCRE() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                rs = db.ResultadoSelect("obtenerpregunta", "1", "DMP");
                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getDMP() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";

                if (bod.EditProyeccionPoblacional) {

                    rs = db.ResultadoSelect("obtenerseccion", "2", null);
                    datos += "<tr bgcolor='#35556c'><td colspan='3'><b>" + rs.getString("Nombre") + "</b></td></tr>";

                    if (bod.getMEA() > 0) {
                        rs = db.ResultadoSelect("obtenerpregunta", "2", "MEA");
                        datos += "<tr><td>" + rs.getString("titulo1") + "</td><td></td><td>" + rs.getString("titulo2") + "</td></tr>";
                        rs = db.ResultadoSelect("obtenerpregunta", "2", "TCA");
                        datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getTCA() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                        rs = db.ResultadoSelect("obtenerpregunta", "2", "PPA");
                        datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getPPA() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                    } else {
                        rs = db.ResultadoSelect("obtenerpregunta", "2", "MEG");
                        datos += "<tr><td>" + rs.getString("titulo1") + "</td><td></td><td>" + rs.getString("titulo2") + "</td></tr>";
                        rs = db.ResultadoSelect("obtenerpregunta", "2", "TCG");
                        datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getTCG() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                        rs = db.ResultadoSelect("obtenerpregunta", "2", "PPG");
                        datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getPPG() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                    }
                    rs = db.ResultadoSelect("obtenerpregunta", "2", "NCR");
                    datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getNCR() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";

                    if (bod.EditCalculoCaudalesDiseno) {
                        rs = db.ResultadoSelect("obtenerseccion", "3", null);
                        datos += "<tr bgcolor='#35556c'><td colspan='3'><b>" + rs.getString("Nombre") + "</b></td></tr>";
                        rs = db.ResultadoSelect("obtenerpregunta", "3", "CAR");
                        datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getCAR() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                        rs = db.ResultadoSelect("obtenerpregunta", "3", "KAR");
                        datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getKAR() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                        if (bod.getQIF() > 0) {
                            rs = db.ResultadoSelect("obtenerpregunta", "3", "QIF");
                            datos += "<tr><td>" + rs.getString("titulo1") + "</td><td></td><td>" + rs.getString("titulo2") + "</td></tr>";
                            rs = db.ResultadoSelect("obtenerpregunta", "3", "QEL");
                            datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getQEL() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                            rs = db.ResultadoSelect("obtenerpregunta", "3", "QET");
                            datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getQET() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                            rs = db.ResultadoSelect("obtenerpregunta", "3", "QEC");
                            datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getQEC() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                            rs = db.ResultadoSelect("obtenerpregunta", "3", "QEK");
                            datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getQEK() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                        } else { //QUIA
                            rs = db.ResultadoSelect("obtenerpregunta", "3", "QIA");
                            datos += "<tr><td>" + rs.getString("titulo1") + "</td><td></td><td>" + rs.getString("titulo2") + "</td></tr>";
                            rs = db.ResultadoSelect("obtenerpregunta", "3", "QAA");
                            datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getQAA() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                            rs = db.ResultadoSelect("obtenerpregunta", "3", "QAI");
                            datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getQAI() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                            rs = db.ResultadoSelect("obtenerpregunta", "3", "QAC");
                            datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getQAC() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                            rs = db.ResultadoSelect("obtenerpregunta", "3", "QAK");
                            datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getQAK() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                        }
                        if (bod.getQCD() > 0) {
                            rs = db.ResultadoSelect("obtenerpregunta", "3", "QCA");
                            datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getQCA() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                            rs = db.ResultadoSelect("obtenerpregunta", "3", "QCM");
                            datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getQCM() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";

                            if (bod.getQCE() > 0) {
                                rs = db.ResultadoSelect("obtenerpregunta", "3", "QCE");
                                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td></td><td>" + rs.getString("titulo2") + "</td></tr>";
                                rs = db.ResultadoSelect("obtenerpregunta", "3", "QCC");
                                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getQCC() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                                rs = db.ResultadoSelect("obtenerpregunta", "3", "QCK");
                                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getQCK() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                            } else { //QNL
                                rs = db.ResultadoSelect("obtenerpregunta", "3", "QNL");
                                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td></td><td>" + rs.getString("titulo2") + "</td></tr>";
                                rs = db.ResultadoSelect("obtenerpregunta", "3", "QNC");
                                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getQNC() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                                rs = db.ResultadoSelect("obtenerpregunta", "3", "QNK");
                                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getQNK() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                            }
                        }
                        if (bod.getIOA() > 0) {
                            rs = db.ResultadoSelect("obtenerpregunta", "3", "IOA");
                            datos += "<tr><td>" + rs.getString("titulo1") + "</td><td></td><td>" + rs.getString("titulo2") + "</td></tr>";

                            if (bod.getIRM() > 0) {
                                rs = db.ResultadoSelect("obtenerpregunta", "3", "IQ2");
                                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getIQ2() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                                rs = db.ResultadoSelect("obtenerpregunta", "3", "IK2");
                                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getIK2() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                                rs = db.ResultadoSelect("obtenerpregunta", "3", "IQ3");
                                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getIQ3() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                                rs = db.ResultadoSelect("obtenerpregunta", "3", "IK3");
                                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getIK3() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                                rs = db.ResultadoSelect("obtenerpregunta", "3", "ICM");
                                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getICM() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                                rs = db.ResultadoSelect("obtenerpregunta", "3", "IQ1");
                                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getIQ1() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                                rs = db.ResultadoSelect("obtenerpregunta", "3", "IK1");
                                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getIK1() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                                rs = db.ResultadoSelect("obtenerpregunta", "3", "ICC");
                                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getICC() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                            } else {
                                rs = db.ResultadoSelect("obtenerpregunta", "3", "IAI");
                                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getIAI() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                                rs = db.ResultadoSelect("obtenerpregunta", "3", "IPI");
                                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getIPI() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                                rs = db.ResultadoSelect("obtenerpregunta", "3", "IQI");
                                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getIQI() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                                rs = db.ResultadoSelect("obtenerpregunta", "3", "IKI");
                                datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getIKI() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                            }
                        }
                        if (bod.getCOA() > 0) {
                            rs = db.ResultadoSelect("obtenerpregunta", "3", "COA");
                            datos += "<tr><td>" + rs.getString("titulo1") + "</td><td></td><td>" + rs.getString("titulo2") + "</td></tr>";
                            rs = db.ResultadoSelect("obtenerpregunta", "3", "CAC");
                            datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getCAC() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                            rs = db.ResultadoSelect("obtenerpregunta", "3", "CPC");
                            datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getCPC() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                            rs = db.ResultadoSelect("obtenerpregunta", "3", "CQC");
                            datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getCQC() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                            rs = db.ResultadoSelect("obtenerpregunta", "3", "CKC");
                            datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getCKC() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                        }
                        if (bod.getYOA() > 0) {
                            rs = db.ResultadoSelect("obtenerpregunta", "3", "YOA");
                            datos += "<tr><td>" + rs.getString("titulo1") + "</td><td></td><td>" + rs.getString("titulo2") + "</td></tr>";
                            rs = db.ResultadoSelect("obtenerpregunta", "3", "YAI");
                            datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getYAI() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                            rs = db.ResultadoSelect("obtenerpregunta", "3", "YPI");
                            datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getYPI() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                            rs = db.ResultadoSelect("obtenerpregunta", "3", "YQI");
                            datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getYQI() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                            rs = db.ResultadoSelect("obtenerpregunta", "3", "YKI");
                            datos += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getYKI() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                        }
                        rs = db.ResultadoSelect("obtenerpregunta", "3", "Q2C");
                        datos2 += "<tr><td>" + rs.getString("descripcion") + "</td><td></td><td></td></tr>"; //especial                        
                        datos2 += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getQ2C() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                        rs = db.ResultadoSelect("obtenerpregunta", "3", "Q2K");
                        datos2 += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getQ2K() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";

                        rs = db.ResultadoSelect("obtenerpregunta", "3", "Q3M");
                        datos2 += "<tr><td>" + rs.getString("descripcion") + "</td><td></td><td></td></tr>"; //especial
                        datos2 += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getQ3M() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                        rs = db.ResultadoSelect("obtenerpregunta", "3", "Q3C");
                        datos2 += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getQ3C() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                        rs = db.ResultadoSelect("obtenerpregunta", "3", "Q3K");
                        datos2 += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getQ3K() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";

                        rs = db.ResultadoSelect("obtenerpregunta", "3", "Q1H");
                        datos2 += "<tr><td>" + rs.getString("descripcion") + "</td><td></td><td></td></tr>"; //especial
                        datos2 += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getQ1H() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                        rs = db.ResultadoSelect("obtenerpregunta", "3", "Q1C");
                        datos2 += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getQ1C() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                        rs = db.ResultadoSelect("obtenerpregunta", "3", "Q1K");
                        datos2 += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getQ1K() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";

                        if (bod.EditCaractInicAguaResidual) {
                            rs = db.ResultadoSelect("obtenerseccion", "4", null);
                            datos2 += "<tr bgcolor='#35556c'><td colspan='3'><b>" + rs.getString("Nombre") + "</b></td></tr>";
                            rs = db.ResultadoSelect("obtenerpregunta", "4", "CAT");
                            datos2 += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getCAT() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                            rs = db.ResultadoSelect("obtenerpregunta", "4", "CAB");
                            datos2 += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getCAB() + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                            rs = db.ResultadoSelect("obtenerpregunta", "4", "CAQ");
                            datos2 += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + ((bod.getCAQ().equals("0")) ? bod.getCAQ() : " ") + "</td><td>" + rs.getString("titulo2") + "</td></tr>";//especial
                            rs = db.ResultadoSelect("obtenerpregunta", "4", "CAS");
                            datos2 += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + ((bod.getCAS().equals("0")) ? bod.getCAS() : " ") + "</td><td>" + rs.getString("titulo2") + "</td></tr>";//especial
                            rs = db.ResultadoSelect("obtenerpregunta", "4", "CAV");
                            datos2 += "<tr><td>" + rs.getString("titulo1") + "</td><td>" + ((bod.getCAV().equals("0")) ? bod.getCAV() : " ") + "</td><td>" + rs.getString("titulo2") + "</td></tr>";//especial
                            ///TODO Aqui falta CVS
                        }
                    }
                }
                datos += "</table></HTML>";
                datos2 += "</table></HTML>";
                lbl_datosfondo1.setText(datos);
                lbl_datosfondo2.setText(datos2);
            } catch (Exception ex) {
                log.error("Error en CargarDatosFondo() " + ex.getMessage());
                lbl_datosfondo1.setText("error");
                lbl_datosfondo2.setText("error");
            } finally {
                db.close(); //Se cierra la conexiòn
            }
        } else {
            lbl_datosfondo1.setText("");
            lbl_datosfondo2.setText("");
        }
    }

    private void habilitarExportar() {
    }

    private void AbrirBienvenido() {

        if (winbienvenido) {//Bandera que evita la duplicación de la ventana
            return;
        }

        bienvenido = null;
        bienvenido = new Bienvenido();

        BasicInternalFrameUI biu = (BasicInternalFrameUI) bienvenido.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(bienvenido);
        bienvenido.setLocation(1, 1);
        bienvenido.setVisible(true);
        winbienvenido = true;
        try {
            bienvenido.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirDatosEntrada() {

        if (bod.VentanasAbiertas()) { //Si existen otras ventanas abiertas no deja abrir mas
            return;
        }

        if (bod.WinDatosDeEntrada) {//Bandera que evita la duplicación de la ventana
            return;
        }

        DatosEntrada = null;
        DatosEntrada = new DatosEntrada(bod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) DatosEntrada.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(DatosEntrada);
        DatosEntrada.setLocation(1, 1);
        DatosEntrada.setVisible(true);

        try {
            DatosEntrada.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirProyeccionPoblacional() {

        if (bod.VentanasAbiertas()) { //Si existen otras ventanas abiertas no deja abrir mas
            return;
        }

        if (bod.WinProyeccionPoblacional) {//Bandera que evita la duplicación de la ventana. True = abierta
            return;
        }

        Proyeccionpoblacional = null;
        Proyeccionpoblacional = new ProyeccionPoblacional(bod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) Proyeccionpoblacional.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(Proyeccionpoblacional);
        Proyeccionpoblacional.setLocation(1, 1);
        Proyeccionpoblacional.setVisible(true);

        try {
            Proyeccionpoblacional.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirCalculoCaudalesDiseno() {

        if (bod.VentanasAbiertas()) { //Si existen otras ventanas abiertas no deja abrir mas
            return;
        }

        if (bod.WinCalculoCaudalesDiseno) {//Bandera que evita la duplicación de la ventana
            return;
        }

        Calculocaudalesdiseno = null;
        Calculocaudalesdiseno = new CalculoCaudalesDiseno(bod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) Calculocaudalesdiseno.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(Calculocaudalesdiseno);
        Calculocaudalesdiseno.setLocation(1, 1);
        Calculocaudalesdiseno.setVisible(true);

        try {
            Calculocaudalesdiseno.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirCaractInicAguaResidual() {

        if (bod.VentanasAbiertas()) { //Si existen otras ventanas abiertas no deja abrir mas
            return;
        }

        if (bod.WinCaractInicAguaResidual) {//Bandera que evita la duplicación de la ventana //Todo esto esta reiterativo?
            return;
        }

        Caractinicaguaresidual = null;
        Caractinicaguaresidual = new CaracterAguaResidual(bod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) Caractinicaguaresidual.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(Caractinicaguaresidual);
        Caractinicaguaresidual.setLocation(1, 1);
        Caractinicaguaresidual.setVisible(true);

        try {
            Caractinicaguaresidual.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirUasb() {

        if (bod.VentanasAbiertas()) { //Si existen otras ventanas abiertas no deja abrir mas
            return;
        }

        ReactorUasb = null;
        ReactorUasb = new ReactorUASB(bod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) ReactorUasb.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(ReactorUasb);
        ReactorUasb.setLocation(1, 1);
        ReactorUasb.setVisible(true);

        try {
            ReactorUasb.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirLodosActivadosConvencional() {

        if (bod.VentanasAbiertas()) { //Si existen otras ventanas abiertas no deja abrir mas
            return;
        }

        lodosactivados = null;
        lodosactivados = new LodosActivadsModConv(bod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) lodosactivados.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(lodosactivados);
        lodosactivados.setLocation(1, 1);
        lodosactivados.setVisible(true);

        try {
            lodosactivados.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirFiltro() {

        if (bod.VentanasAbiertas()) { //Si existen otras ventanas abiertas no deja abrir mas
            return;
        }

        filtropercolador = null;
        filtropercolador = new FiltroPercolador(bod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) filtropercolador.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(filtropercolador);
        filtropercolador.setLocation(1, 1);
        filtropercolador.setVisible(true);

        try {
            filtropercolador.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirRejillas() {

        if (bod.VentanasAbiertas()) { //Si existen otras ventanas abiertas no deja abrir mas
            return;
        }

        rejillas = null;
        rejillas = new Rejillas(bod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) rejillas.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(rejillas);
        rejillas.setLocation(1, 1);
        rejillas.setVisible(true);

        try {
            rejillas.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirDesarenador() {

        if (bod.VentanasAbiertas()) { //Si existen otras ventanas abiertas no deja abrir mas
            return;
        }

        desarenador = null;
        desarenador = new Desarenador(bod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) desarenador.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(desarenador);
        desarenador.setLocation(1, 1);
        desarenador.setVisible(true);

        try {
            desarenador.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirLagunaAnanerobia() {

        if (bod.VentanasAbiertas()) { //Si existen otras ventanas abiertas no deja abrir mas
            return;
        }

        if (bod.WinLagunaAnaerobia) {//Bandera que evita la duplicación de la ventana
            return;
        }

        Lagunaanaerobia = null;
        Lagunaanaerobia = new LagunaAnaerobia(bod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) Lagunaanaerobia.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(Lagunaanaerobia);
        Lagunaanaerobia.setLocation(1, 1);
        Lagunaanaerobia.setVisible(true);

        try {
            Lagunaanaerobia.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirLagunaFacultativaSecundaria() {

        if (bod.VentanasAbiertas()) { //Si existen otras ventanas abiertas no deja abrir mas
            return;
        }

        if (bod.WinLagunaAnaerobia) {//Bandera que evita la duplicación de la ventana
            return;
        }

        Lagunafacultativasec = null;
        Lagunafacultativasec = new LagunaFacultativaSec(bod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) Lagunafacultativasec.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(Lagunafacultativasec);
        Lagunafacultativasec.setLocation(1, 1);
        Lagunafacultativasec.setVisible(true);

        try {
            Lagunafacultativasec.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirResultado(String tecnbd) {

        if (bod.VentanasAbiertas()) { //Si existen otras ventanas abiertas no deja abrir mas
            return;
        }

        if (bod.WinResultado) {//Bandera que evita la duplicación de la ventana. True = abierta
            return;
        }

        resultado = null;
        resultado = new Resultado(bod, tecnbd);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) resultado.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(resultado);
        resultado.setLocation(1, 1);
        resultado.setVisible(true);

        try {
            resultado.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jdp_Main = new javax.swing.JDesktopPane();
        lbl_datosfondo2 = new javax.swing.JLabel();
        lbl_datosfondo1 = new javax.swing.JLabel();
        menubar = new javax.swing.JMenuBar();
        menu_Archivo = new javax.swing.JMenu();
        menuitem_new = new javax.swing.JMenuItem();
        sep1 = new javax.swing.JPopupMenu.Separator();
        menuitem_open = new javax.swing.JMenuItem();
        sep2 = new javax.swing.JPopupMenu.Separator();
        menuitem_export = new javax.swing.JMenuItem();
        menuitem_exit = new javax.swing.JMenuItem();
        jMenu_separador1 = new javax.swing.JMenu();
        menu_Datos = new javax.swing.JMenu();
        menuitem_DatosEntrada = new javax.swing.JMenuItem();
        sep_1 = new javax.swing.JPopupMenu.Separator();
        menuitem_ProyeccionPoblacional = new javax.swing.JMenuItem();
        sep_2 = new javax.swing.JPopupMenu.Separator();
        menuitem_CalculoCaudalesDiseno = new javax.swing.JMenuItem();
        sep_3 = new javax.swing.JPopupMenu.Separator();
        menuitem_CaractInicAguaResidual = new javax.swing.JMenuItem();
        jMenu_separador2 = new javax.swing.JMenu();
        menu_Tecnologias = new javax.swing.JMenu();
        menu_Tecnologia0 = new javax.swing.JMenu();
        menuitem_rejillas = new javax.swing.JMenuItem();
        menuitem_desarenador = new javax.swing.JMenuItem();
        Sep_0 = new javax.swing.JPopupMenu.Separator();
        menu_Tecnologia1 = new javax.swing.JMenu();
        menuitem_lagunaerobia = new javax.swing.JMenuItem();
        menuitem_lagunafacul1 = new javax.swing.JMenuItem();
        Sep_1 = new javax.swing.JPopupMenu.Separator();
        menu_Tecnologia2 = new javax.swing.JMenu();
        menuitem_reactoruasb = new javax.swing.JMenuItem();
        menuitem_lagunafacul2 = new javax.swing.JMenuItem();
        Sep_2 = new javax.swing.JPopupMenu.Separator();
        menu_Tecnologia3 = new javax.swing.JMenu();
        Sep_3 = new javax.swing.JPopupMenu.Separator();
        menu_Tecnologia4 = new javax.swing.JMenu();
        Sep_4 = new javax.swing.JPopupMenu.Separator();
        menu_Tecnologia5 = new javax.swing.JMenu();
        jMenu_separador3 = new javax.swing.JMenu();
        menu_Resultados = new javax.swing.JMenu();
        menuItem_Res_Rejillas = new javax.swing.JMenuItem();
        menuItem_Res_Desarenador = new javax.swing.JMenuItem();
        menuItem_Res_Parshall = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuItem_Res_LagunaAnaerobia = new javax.swing.JMenuItem();
        menuItem_Res_LagunaFacultativa = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenu_separador4 = new javax.swing.JMenu();
        menu_Acerca = new javax.swing.JMenu();
        jMenuItem12 = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        jMenuItem13 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jdp_Main.setBackground(new java.awt.Color(135, 135, 169));
        jdp_Main.setForeground(new java.awt.Color(255, 255, 255));

        lbl_datosfondo2.setForeground(new java.awt.Color(255, 255, 255));
        lbl_datosfondo2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lbl_datosfondo2.setBounds(522, 10, 490, 740);
        jdp_Main.add(lbl_datosfondo2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        lbl_datosfondo1.setForeground(new java.awt.Color(255, 255, 255));
        lbl_datosfondo1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lbl_datosfondo1.setBounds(10, 10, 490, 740);
        jdp_Main.add(lbl_datosfondo1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        menubar.setBackground(new java.awt.Color(255, 255, 255));
        menubar.setToolTipText("Datos");
        menubar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        menu_Archivo.setBackground(new java.awt.Color(255, 255, 255));
        menu_Archivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu_archivos.png"))); // NOI18N
        menu_Archivo.setText("Archivos");
        menu_Archivo.setToolTipText("");

        menuitem_new.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_nuevo.png"))); // NOI18N
        menuitem_new.setText("Nuevo Proyecto");
        menuitem_new.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitem_newActionPerformed(evt);
            }
        });
        menu_Archivo.add(menuitem_new);
        menu_Archivo.add(sep1);

        menuitem_open.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_abrir.png"))); // NOI18N
        menuitem_open.setText("Abrir Proyecto");
        menuitem_open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitem_openActionPerformed(evt);
            }
        });
        menu_Archivo.add(menuitem_open);
        menu_Archivo.add(sep2);

        menuitem_export.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_exportar.png"))); // NOI18N
        menuitem_export.setText("Exportar Proyecto");
        menuitem_export.setEnabled(false);
        menu_Archivo.add(menuitem_export);

        menuitem_exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu_salir.png"))); // NOI18N
        menuitem_exit.setText("Salir");
        menuitem_exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitem_exitActionPerformed(evt);
            }
        });
        menu_Archivo.add(menuitem_exit);

        menubar.add(menu_Archivo);

        jMenu_separador1.setBackground(new java.awt.Color(255, 255, 255));
        jMenu_separador1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu_separador.png"))); // NOI18N
        jMenu_separador1.setToolTipText("");
        jMenu_separador1.setContentAreaFilled(false);
        jMenu_separador1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jMenu_separador1.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu_separador.png"))); // NOI18N
        jMenu_separador1.setEnabled(false);
        jMenu_separador1.setFocusable(false);
        jMenu_separador1.setRequestFocusEnabled(false);
        jMenu_separador1.setRolloverEnabled(false);
        jMenu_separador1.setVerifyInputWhenFocusTarget(false);
        menubar.add(jMenu_separador1);

        menu_Datos.setBackground(new java.awt.Color(255, 255, 255));
        menu_Datos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu_datos.png"))); // NOI18N
        menu_Datos.setText("Datos");
        menu_Datos.setToolTipText("");

        menuitem_DatosEntrada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_datos.png"))); // NOI18N
        menuitem_DatosEntrada.setText("Datos de Entrada");
        menuitem_DatosEntrada.setEnabled(false);
        menuitem_DatosEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitem_DatosEntradaActionPerformed(evt);
            }
        });
        menu_Datos.add(menuitem_DatosEntrada);
        menu_Datos.add(sep_1);

        menuitem_ProyeccionPoblacional.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_proypob.png"))); // NOI18N
        menuitem_ProyeccionPoblacional.setText("Proyección poblacional");
        menuitem_ProyeccionPoblacional.setEnabled(false);
        menuitem_ProyeccionPoblacional.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitem_ProyeccionPoblacionalActionPerformed(evt);
            }
        });
        menu_Datos.add(menuitem_ProyeccionPoblacional);
        menu_Datos.add(sep_2);

        menuitem_CalculoCaudalesDiseno.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_calccaudal.png"))); // NOI18N
        menuitem_CalculoCaudalesDiseno.setText("Cálculo de caudales de  diseño");
        menuitem_CalculoCaudalesDiseno.setEnabled(false);
        menuitem_CalculoCaudalesDiseno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitem_CalculoCaudalesDisenoActionPerformed(evt);
            }
        });
        menu_Datos.add(menuitem_CalculoCaudalesDiseno);
        menu_Datos.add(sep_3);

        menuitem_CaractInicAguaResidual.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_caractaguainic.png"))); // NOI18N
        menuitem_CaractInicAguaResidual.setText("Características  iniciales del agua  residual");
        menuitem_CaractInicAguaResidual.setEnabled(false);
        menuitem_CaractInicAguaResidual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitem_CaractInicAguaResidualActionPerformed(evt);
            }
        });
        menu_Datos.add(menuitem_CaractInicAguaResidual);

        menubar.add(menu_Datos);

        jMenu_separador2.setBackground(new java.awt.Color(255, 255, 255));
        jMenu_separador2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu_separador.png"))); // NOI18N
        jMenu_separador2.setToolTipText("");
        jMenu_separador2.setContentAreaFilled(false);
        jMenu_separador2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jMenu_separador2.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu_separador.png"))); // NOI18N
        jMenu_separador2.setEnabled(false);
        jMenu_separador2.setFocusable(false);
        jMenu_separador2.setRequestFocusEnabled(false);
        jMenu_separador2.setRolloverEnabled(false);
        jMenu_separador2.setVerifyInputWhenFocusTarget(false);
        menubar.add(jMenu_separador2);

        menu_Tecnologias.setBackground(new java.awt.Color(255, 255, 255));
        menu_Tecnologias.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu_tecnologias.png"))); // NOI18N
        menu_Tecnologias.setText("Tecnologías");
        menu_Tecnologias.setToolTipText("");

        menu_Tecnologia0.setText("Rejillas + Desarenador...");
        menu_Tecnologia0.setEnabled(false);

        menuitem_rejillas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_rejilla.png"))); // NOI18N
        menuitem_rejillas.setText("Rejillas");
        menuitem_rejillas.setEnabled(false);
        menuitem_rejillas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitem_rejillasActionPerformed(evt);
            }
        });
        menu_Tecnologia0.add(menuitem_rejillas);

        menuitem_desarenador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_desarenador.png"))); // NOI18N
        menuitem_desarenador.setText("<html>Desarenador <br/>+ Canaleta Parshall</html>");
        menuitem_desarenador.setEnabled(false);
        menuitem_desarenador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitem_desarenadorActionPerformed(evt);
            }
        });
        menu_Tecnologia0.add(menuitem_desarenador);

        menu_Tecnologias.add(menu_Tecnologia0);
        menu_Tecnologias.add(Sep_0);

        menu_Tecnologia1.setText("Laguna Anaerobia + Laguna Facultativa");
        menu_Tecnologia1.setEnabled(false);

        menuitem_lagunaerobia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_lagunas.png"))); // NOI18N
        menuitem_lagunaerobia.setText("Laguna Anaerobia");
        menuitem_lagunaerobia.setEnabled(false);
        menuitem_lagunaerobia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitem_lagunaerobiaActionPerformed(evt);
            }
        });
        menu_Tecnologia1.add(menuitem_lagunaerobia);

        menuitem_lagunafacul1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_lagunas.png"))); // NOI18N
        menuitem_lagunafacul1.setText("Laguna Facultativa");
        menuitem_lagunafacul1.setEnabled(false);
        menuitem_lagunafacul1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitem_lagunafacul1ActionPerformed(evt);
            }
        });
        menu_Tecnologia1.add(menuitem_lagunafacul1);

        menu_Tecnologias.add(menu_Tecnologia1);
        menu_Tecnologias.add(Sep_1);

        menu_Tecnologia2.setText("Reactor UASB + Laguna Facultativa");
        menu_Tecnologia2.setEnabled(false);

        menuitem_reactoruasb.setText("Reactor UASB");
        menuitem_reactoruasb.setEnabled(false);
        menu_Tecnologia2.add(menuitem_reactoruasb);

        menuitem_lagunafacul2.setText("Laguna Facultativa");
        menuitem_lagunafacul2.setEnabled(false);
        menu_Tecnologia2.add(menuitem_lagunafacul2);

        menu_Tecnologias.add(menu_Tecnologia2);
        menu_Tecnologias.add(Sep_2);

        menu_Tecnologia3.setText("Reactor UASB + Filtro Percolador");
        menu_Tecnologia3.setEnabled(false);
        menu_Tecnologias.add(menu_Tecnologia3);
        menu_Tecnologias.add(Sep_3);

        menu_Tecnologia4.setText("Reactor UASB + Lodos Activados Modalidad convencional");
        menu_Tecnologia4.setEnabled(false);
        menu_Tecnologias.add(menu_Tecnologia4);
        menu_Tecnologias.add(Sep_4);

        menu_Tecnologia5.setText("Lodos Activados, Aireación Extendida");
        menu_Tecnologia5.setEnabled(false);
        menu_Tecnologias.add(menu_Tecnologia5);

        menubar.add(menu_Tecnologias);

        jMenu_separador3.setBackground(new java.awt.Color(255, 255, 255));
        jMenu_separador3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu_separador.png"))); // NOI18N
        jMenu_separador3.setToolTipText("");
        jMenu_separador3.setContentAreaFilled(false);
        jMenu_separador3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jMenu_separador3.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu_separador.png"))); // NOI18N
        jMenu_separador3.setEnabled(false);
        jMenu_separador3.setFocusable(false);
        jMenu_separador3.setRequestFocusEnabled(false);
        jMenu_separador3.setRolloverEnabled(false);
        jMenu_separador3.setVerifyInputWhenFocusTarget(false);
        menubar.add(jMenu_separador3);

        menu_Resultados.setBackground(new java.awt.Color(255, 255, 255));
        menu_Resultados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu_Resultados.png"))); // NOI18N
        menu_Resultados.setText("Resultados");

        menuItem_Res_Rejillas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_Res_Rejillas.png"))); // NOI18N
        menuItem_Res_Rejillas.setText("Rejillas");
        menuItem_Res_Rejillas.setEnabled(false);
        menuItem_Res_Rejillas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_Res_RejillasActionPerformed(evt);
            }
        });
        menu_Resultados.add(menuItem_Res_Rejillas);

        menuItem_Res_Desarenador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_Res_Desarenador.png"))); // NOI18N
        menuItem_Res_Desarenador.setText("Desarenador");
        menuItem_Res_Desarenador.setEnabled(false);
        menuItem_Res_Desarenador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_Res_DesarenadorActionPerformed(evt);
            }
        });
        menu_Resultados.add(menuItem_Res_Desarenador);

        menuItem_Res_Parshall.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_Res_Parshall.png"))); // NOI18N
        menuItem_Res_Parshall.setText("Medidor Parshall");
        menuItem_Res_Parshall.setEnabled(false);
        menuItem_Res_Parshall.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_Res_ParshallActionPerformed(evt);
            }
        });
        menu_Resultados.add(menuItem_Res_Parshall);
        menu_Resultados.add(jSeparator1);

        menuItem_Res_LagunaAnaerobia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_Res_lagunas.png"))); // NOI18N
        menuItem_Res_LagunaAnaerobia.setText("Laguna Anaerobia");
        menuItem_Res_LagunaAnaerobia.setEnabled(false);
        menuItem_Res_LagunaAnaerobia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_Res_LagunaAnaerobiaActionPerformed(evt);
            }
        });
        menu_Resultados.add(menuItem_Res_LagunaAnaerobia);

        menuItem_Res_LagunaFacultativa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_Res_lagunas.png"))); // NOI18N
        menuItem_Res_LagunaFacultativa.setText("Laguna Facultativa");
        menuItem_Res_LagunaFacultativa.setEnabled(false);
        menuItem_Res_LagunaFacultativa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_Res_LagunaFacultativaActionPerformed(evt);
            }
        });
        menu_Resultados.add(menuItem_Res_LagunaFacultativa);
        menu_Resultados.add(jSeparator2);

        menubar.add(menu_Resultados);

        jMenu_separador4.setBackground(new java.awt.Color(255, 255, 255));
        jMenu_separador4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu_separador.png"))); // NOI18N
        jMenu_separador4.setToolTipText("");
        jMenu_separador4.setContentAreaFilled(false);
        jMenu_separador4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jMenu_separador4.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu_separador.png"))); // NOI18N
        jMenu_separador4.setEnabled(false);
        jMenu_separador4.setFocusable(false);
        jMenu_separador4.setRequestFocusEnabled(false);
        jMenu_separador4.setRolloverEnabled(false);
        jMenu_separador4.setVerifyInputWhenFocusTarget(false);
        menubar.add(jMenu_separador4);

        menu_Acerca.setBackground(new java.awt.Color(255, 255, 255));
        menu_Acerca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu_ayuda.png"))); // NOI18N
        menu_Acerca.setText("Acerca de");

        jMenuItem12.setText("Ayuda");
        menu_Acerca.add(jMenuItem12);
        menu_Acerca.add(jSeparator7);

        jMenuItem13.setText("Acerca de...");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        menu_Acerca.add(jMenuItem13);

        menubar.add(menu_Acerca);

        setJMenuBar(menubar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jdp_Main, javax.swing.GroupLayout.DEFAULT_SIZE, 1024, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jdp_Main, javax.swing.GroupLayout.DEFAULT_SIZE, 766, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void menuitem_newActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_newActionPerformed
        // Trata de crear un archivo si la sesion tiene abierto un proyecto pregunta
        if (CerrarVentanas()) {
            if (bod.CrearProyecto()) {
                comprobarBotones();
                menuitem_DatosEntrada.setEnabled(true);
            }
        }
    }//GEN-LAST:event_menuitem_newActionPerformed

    private void menuitem_openActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_openActionPerformed

        if (CerrarVentanas()) {

            String text = util.AbrirArchivoPtar();

            if (text == null) {
                return;
            }

            if (!text.equals("error")) {

                Validaciones validas = new Validaciones();
                if (validas.EsEnteroEntre(text, 1, 99999999)) {

                    int id = Integer.parseInt(text.trim());

                    if (bod.CargarProyecto(id)) {
                        comprobarBotones();
                        menuitem_DatosEntrada.setEnabled(true);
                        menuitem_export.setEnabled(true);
                        CargarDatosFondo();
                        util.Mensaje("Proyecto cargado correctamente!", "ok");
                        return;
                    }
                }
            } else {
                menuitem_export.setEnabled(false);
                util.Mensaje("No se pudo abrir el archivo", "error");
            }
        } else {
            return;
        }
    }//GEN-LAST:event_menuitem_openActionPerformed

    private void menuitem_ProyeccionPoblacionalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_ProyeccionPoblacionalActionPerformed
        AbrirProyeccionPoblacional();
    }//GEN-LAST:event_menuitem_ProyeccionPoblacionalActionPerformed

    private void menuitem_DatosEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_DatosEntradaActionPerformed
        AbrirDatosEntrada();
    }//GEN-LAST:event_menuitem_DatosEntradaActionPerformed

    private void menuitem_CalculoCaudalesDisenoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_CalculoCaudalesDisenoActionPerformed
        AbrirCalculoCaudalesDiseno();
    }//GEN-LAST:event_menuitem_CalculoCaudalesDisenoActionPerformed

    private void menuitem_CaractInicAguaResidualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_CaractInicAguaResidualActionPerformed
        AbrirCaractInicAguaResidual();
    }//GEN-LAST:event_menuitem_CaractInicAguaResidualActionPerformed

    private void menuitem_lagunaerobiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_lagunaerobiaActionPerformed
        AbrirLagunaAnanerobia();
    }//GEN-LAST:event_menuitem_lagunaerobiaActionPerformed

    private void menuitem_lagunafacul1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_lagunafacul1ActionPerformed
        AbrirLagunaFacultativaSecundaria();
    }//GEN-LAST:event_menuitem_lagunafacul1ActionPerformed

    private void menuItem_Res_LagunaAnaerobiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_Res_LagunaAnaerobiaActionPerformed
        AbrirResultado("*LA");//Abrir laguna anaerobia, resultado en imagen
    }//GEN-LAST:event_menuItem_Res_LagunaAnaerobiaActionPerformed

    private void menuItem_Res_LagunaFacultativaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_Res_LagunaFacultativaActionPerformed
        AbrirResultado("*LF");//Abrir laguna facultativa, resultado en imagen
    }//GEN-LAST:event_menuItem_Res_LagunaFacultativaActionPerformed

    private void menuitem_rejillasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_rejillasActionPerformed
        // TODO add your handling code here:
        AbrirRejillas();
    }//GEN-LAST:event_menuitem_rejillasActionPerformed

    private void menuitem_desarenadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_desarenadorActionPerformed
        // TODO add your handling code here:
        AbrirDesarenador();
    }//GEN-LAST:event_menuitem_desarenadorActionPerformed

    private void menuItem_Res_RejillasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_Res_RejillasActionPerformed
        // TODO add your handling code here:
        AbrirResultado("*PR");//Abrir rejillas, resultado en imagen
    }//GEN-LAST:event_menuItem_Res_RejillasActionPerformed

    private void menuItem_Res_DesarenadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_Res_DesarenadorActionPerformed
        // TODO add your handling code here:
        AbrirResultado("*PD");//Abrir desarenador, resultado en imagen
    }//GEN-LAST:event_menuItem_Res_DesarenadorActionPerformed

    private void menuItem_Res_ParshallActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_Res_ParshallActionPerformed
        // TODO add your handling code here:
        AbrirResultado("*PP");//Abrir dMedidor Parshall, resultado en imagen
    }//GEN-LAST:event_menuItem_Res_ParshallActionPerformed

    private void menuitem_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_exitActionPerformed
        // TODO add your handling code here:
        int n = util.Mensaje("¿Desea cerrar el Trabajo Existente?", "yesno");
        if (n == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_menuitem_exitActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        AbrirBienvenido();
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu.Separator Sep_0;
    private javax.swing.JPopupMenu.Separator Sep_1;
    private javax.swing.JPopupMenu.Separator Sep_2;
    private javax.swing.JPopupMenu.Separator Sep_3;
    private javax.swing.JPopupMenu.Separator Sep_4;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenu jMenu_separador1;
    private javax.swing.JMenu jMenu_separador2;
    private javax.swing.JMenu jMenu_separador3;
    private javax.swing.JMenu jMenu_separador4;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JDesktopPane jdp_Main;
    private javax.swing.JLabel lbl_datosfondo1;
    private javax.swing.JLabel lbl_datosfondo2;
    private javax.swing.JMenuItem menuItem_Res_Desarenador;
    private javax.swing.JMenuItem menuItem_Res_LagunaAnaerobia;
    private javax.swing.JMenuItem menuItem_Res_LagunaFacultativa;
    private javax.swing.JMenuItem menuItem_Res_Parshall;
    private javax.swing.JMenuItem menuItem_Res_Rejillas;
    private javax.swing.JMenu menu_Acerca;
    private javax.swing.JMenu menu_Archivo;
    private javax.swing.JMenu menu_Datos;
    private javax.swing.JMenu menu_Resultados;
    private javax.swing.JMenu menu_Tecnologia0;
    private javax.swing.JMenu menu_Tecnologia1;
    private javax.swing.JMenu menu_Tecnologia2;
    private javax.swing.JMenu menu_Tecnologia3;
    private javax.swing.JMenu menu_Tecnologia4;
    private javax.swing.JMenu menu_Tecnologia5;
    private javax.swing.JMenu menu_Tecnologias;
    private javax.swing.JMenuBar menubar;
    private javax.swing.JMenuItem menuitem_CalculoCaudalesDiseno;
    private javax.swing.JMenuItem menuitem_CaractInicAguaResidual;
    private javax.swing.JMenuItem menuitem_DatosEntrada;
    private javax.swing.JMenuItem menuitem_ProyeccionPoblacional;
    private javax.swing.JMenuItem menuitem_desarenador;
    private javax.swing.JMenuItem menuitem_exit;
    private javax.swing.JMenuItem menuitem_export;
    private javax.swing.JMenuItem menuitem_lagunaerobia;
    private javax.swing.JMenuItem menuitem_lagunafacul1;
    private javax.swing.JMenuItem menuitem_lagunafacul2;
    private javax.swing.JMenuItem menuitem_new;
    private javax.swing.JMenuItem menuitem_open;
    private javax.swing.JMenuItem menuitem_reactoruasb;
    private javax.swing.JMenuItem menuitem_rejillas;
    private javax.swing.JPopupMenu.Separator sep1;
    private javax.swing.JPopupMenu.Separator sep2;
    private javax.swing.JPopupMenu.Separator sep_1;
    private javax.swing.JPopupMenu.Separator sep_2;
    private javax.swing.JPopupMenu.Separator sep_3;
    // End of variables declaration//GEN-END:variables
}
