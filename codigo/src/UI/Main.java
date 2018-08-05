package UI;

import BO.Bod;
import Componentes.C_ProyeccionPoblacional;
import Componentes.C_CalculoCaudalesDiseno;
import Componentes.C_Rejillas;
import Componentes.C_Desarenador;
import Componentes.C_FiltroPercolador;
import Componentes.C_LagunaAnaerobia;
import Componentes.C_LagunaFacultativaSec;
import Componentes.C_LagunaFacultativaSecUasb;
import Componentes.C_LodosActivModAirExten;
import Componentes.C_LodosActivadsModConv;
import Componentes.C_LodosActivadsModConvUASB;
import Componentes.C_ReactorUasb;
import Componentes.Configuracion;
import Componentes.Util;
import DB.Dao;
import java.awt.Desktop;
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
import javax.swing.ImageIcon;
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
    private LagunaFacultativaSecUASB Lagunafacultativasecuasb;
    private ReactorUASB ReactorUasb;
    private FiltroPercolador filtropercolador;
    private LodosActivadsModConv lodosactivadosmodconv;
    private LodosActivadsModConvUASB lodosactivadosmodconvUASB;
    private LodosActivModAirExten lodosactivadosairexten;
    private Resultado resultado;
    private Dao db;
    String stError;
    String mensajes = "";
    public String Generalpath = "";//path de recursos del proyecto
    public boolean cancela = false;//Indica si la respuesta proviene de cancelar un guardar()
    public String tecnologia = ""; //Indica què tecnologìa ha sido escogida en el menù para saber si se muestran ayudas al usuario, respecto de donde debe continuar
    boolean winbienvenido; //controla si Bienvenido está abierto
    boolean rejilladesarena = false; //indica si el guardado proviene de rejillas o desarenador y no debe comprobar cambios adelante
    ImageIcon icon;
    Util util;
    Bod bod;
    Bod vbod; //Usado temporalmente para guardar    

    public Main() { //Todo:, los proyectos deben ser exportables, ya que solo funcionan en la bd del equipo
        initComponents();
        IniciarLog();
        InicializarComponentes();
    }

    public void InicializarComponentes() {

        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent ev) {
                if (bod.pathProyecto.trim().equals("")) {
                    System.exit(0);
                } else {
                    int n = util.Mensaje("¿Desea cerrar el Trabajo existente?", "yesno");//todo, lenguaje
                    if (n == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
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
        vbod = new Bod(Generalpath);
        util = new Util();
        db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
        winbienvenido = false;
        CargarBienvenido(false);//parámetro de escritura
        stError = "Los cambios han generado un error de cálculo en \n _ \n Se deshabilitaran entradas relacionadas y se perderán datos posteriores. \n\n ¿Desea Guardar?";
        icon = new ImageIcon(conf.ObtenerRutaGeneral("rutaimagenes") + conf.separador + "logo.png");
        setIconImage(icon.getImage());
    }

    /**
     * Configura el log de eventos Errores, Advertencias, etc. En la carpeta
     * Resources està el archivo de configuración.
     */
    private void IniciarLog() {

        BasicConfigurator.configure();
        try {
            Generalpath = conf.pathreal + conf.separador + "src";
            InputStream input = new FileInputStream(Generalpath + conf.separador + "Resources" + conf.separador + "log4j.properties");
            Properties prop = new Properties();
            prop.load(input);
            PropertyConfigurator.configure(prop);

            File filelogs = new File(conf.pathreal + conf.separador + "logs");
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
            menuitem_export.setEnabled(true);//Botón exportar proyecto
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
                                menu_T1Lagunas.setEnabled(true);
                                menuitem_lagunaerobia.setEnabled(true);

                                menu_T2UasbLaguna.setEnabled(true);
                                menuitem_reactoruasbT2.setEnabled(true);
                                menu_T3UasbFiltro.setEnabled(true);
                                menuitem_reactoruasbT3.setEnabled(true);
                                menu_T4UasbLodos.setEnabled(true);
                                menuitem_reactoruasbT4.setEnabled(true);
                                menu_T5LodosAirExt.setEnabled(true);
                                menuitem_T5LodosAireacion.setEnabled(true);
                                menu_T6LodosModConv.setEnabled(true);
                                menuitem_T6LodosConvencional.setEnabled(true);
                                //------------------------------------------------------------T1. Lagunas
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
                                //------------------------------------------------------------T2. Uasb + Laguna
                                if (bod.EditReactorUasb) {
                                    menuitem_lagunafaculT2.setEnabled(true);
                                    menuItem_Res_Uasb.setEnabled(true);

                                    if (bod.EditLagunaFacultativaUASB) {
                                        menuItem_Res_LagunaFacultUasb.setEnabled(true);
                                    } else {
                                        menuItem_Res_LagunaFacultUasb.setEnabled(false);
                                    }
                                } else {
                                    menuitem_lagunafaculT2.setEnabled(false);
                                    menuItem_Res_Uasb.setEnabled(false);
                                    menuItem_Res_LagunaFacultUasb.setEnabled(false);
                                }
                                //------------------------------------------------------------T3. Uasb + Filtro
                                if (bod.EditReactorUasb) {
                                    menuitem_filtropercoT3.setEnabled(true);
                                    menuItem_Res_Uasb.setEnabled(true);

                                    if (bod.EditFiltroPercolador) {
                                        menuItem_Res_FiltroPerc.setEnabled(true);
                                    } else {
                                        menuItem_Res_FiltroPerc.setEnabled(false);
                                    }
                                } else {
                                    menuitem_filtropercoT3.setEnabled(false);
                                    menuItem_Res_Uasb.setEnabled(false);
                                    menuItem_Res_FiltroPerc.setEnabled(false);
                                }
                                //------------------------------------------------------------T4. UASB + Lodos Modalidad Convencional
                                if (bod.EditReactorUasb) {
                                    menuitem_lodsactivmodconvT4.setEnabled(true);
                                    menuItem_Res_Uasb.setEnabled(true);

                                    if (bod.EditLodosActivadsConvencUASB) {
                                        menuItem_Res_LodosActModConvUasb.setEnabled(true);
                                    } else {
                                        menuItem_Res_LodosActModConvUasb.setEnabled(false);
                                    }
                                } else {
                                    menuitem_lodsactivmodconvT4.setEnabled(false);
                                    menuItem_Res_LodosActModConvUasb.setEnabled(false);
                                }
                                //------------------------------------------------------------T5. Lodos Aireacion ext
                                if (bod.EditLodosActivadsAireacExt) {
                                    menuItem_Res_LodosActAirExt.setEnabled(true);
                                } else {
                                    menuItem_Res_LodosActAirExt.setEnabled(false);
                                }
                                //------------------------------------------------------------T6. Lodos Modalidad Convencional
                                if (bod.EditLodosActivadsConvenc) {
                                    menuItem_Res_LodosActModConv.setEnabled(true);
                                } else {
                                    menuItem_Res_LodosActModConv.setEnabled(false);
                                }
                                //------------------------------------------------------------
                            } else {
                                menuItem_Res_Desarenador.setEnabled(false);
                                menuItem_Res_Parshall.setEnabled(false);
                                menu_T1Lagunas.setEnabled(false);
                                menuitem_lagunaerobia.setEnabled(false);
                                menuitem_lagunafacul1.setEnabled(false);
                                menuItem_Res_LagunaAnaerobia.setEnabled(false);
                                menuItem_Res_LagunaFacultativa.setEnabled(false);

                                menu_T2UasbLaguna.setEnabled(false);
                                menuitem_reactoruasbT2.setEnabled(false);
                                menu_T3UasbFiltro.setEnabled(false);
                                menuitem_reactoruasbT3.setEnabled(false);
                                menu_T4UasbLodos.setEnabled(false);
                                menuitem_reactoruasbT4.setEnabled(false);
                                menu_T5LodosAirExt.setEnabled(false);
                                menu_T6LodosModConv.setEnabled(false);
                                menuitem_T5LodosAireacion.setEnabled(false);
                                menuitem_T6LodosConvencional.setEnabled(false);
                                menuitem_filtropercoT3.setEnabled(false);
                                menuItem_Res_Uasb.setEnabled(false);
                                menuItem_Res_FiltroPerc.setEnabled(false);
                                menuitem_lagunafaculT2.setEnabled(false);
                                menuItem_Res_Uasb.setEnabled(false);
                                menuItem_Res_LagunaFacultUasb.setEnabled(false);
                                menuitem_lodsactivmodconvT4.setEnabled(false);
                                menuItem_Res_LodosActModConvUasb.setEnabled(false);
                                menuItem_Res_LodosActAirExt.setEnabled(false);
                                menuItem_Res_LodosActModConv.setEnabled(false);
                            }
                        } else {
                            menuitem_desarenador.setEnabled(false);
                            menuItem_Res_Rejillas.setEnabled(false);
                            menuItem_Res_Desarenador.setEnabled(false);
                            menuItem_Res_Parshall.setEnabled(false);
                            menu_T1Lagunas.setEnabled(false);
                            menuitem_lagunaerobia.setEnabled(false);
                            menuitem_lagunafacul1.setEnabled(false);
                            menuItem_Res_LagunaAnaerobia.setEnabled(false);
                            menuItem_Res_LagunaFacultativa.setEnabled(false);

                            menu_T2UasbLaguna.setEnabled(false);
                            menuitem_reactoruasbT2.setEnabled(false);
                            menu_T3UasbFiltro.setEnabled(false);
                            menuitem_reactoruasbT3.setEnabled(false);
                            menu_T4UasbLodos.setEnabled(false);
                            menuitem_reactoruasbT4.setEnabled(false);
                            menu_T5LodosAirExt.setEnabled(false);
                            menu_T6LodosModConv.setEnabled(false);
                            menuitem_T5LodosAireacion.setEnabled(false);
                            menuitem_T6LodosConvencional.setEnabled(false);
                            menuitem_filtropercoT3.setEnabled(false);
                            menuItem_Res_Uasb.setEnabled(false);
                            menuItem_Res_FiltroPerc.setEnabled(false);
                            menuitem_lagunafaculT2.setEnabled(false);
                            menuItem_Res_Uasb.setEnabled(false);
                            menuItem_Res_LagunaFacultUasb.setEnabled(false);
                            menuitem_lodsactivmodconvT4.setEnabled(false);
                            menuItem_Res_LodosActModConvUasb.setEnabled(false);
                            menuItem_Res_LodosActAirExt.setEnabled(false);
                            menuItem_Res_LodosActModConv.setEnabled(false);
                        }
                    } else {
                        menu_Tecnologia0.setEnabled(false);
                        menuitem_rejillas.setEnabled(false);
                        menuitem_desarenador.setEnabled(false);
                        menuItem_Res_Rejillas.setEnabled(false);
                        menuItem_Res_Desarenador.setEnabled(false);
                        menuItem_Res_Parshall.setEnabled(false);
                        menu_T1Lagunas.setEnabled(false);
                        menuitem_lagunaerobia.setEnabled(false);
                        menuitem_lagunafacul1.setEnabled(false);
                        menuItem_Res_LagunaAnaerobia.setEnabled(false);
                        menuItem_Res_LagunaFacultativa.setEnabled(false);

                        menu_T2UasbLaguna.setEnabled(false);
                        menuitem_reactoruasbT2.setEnabled(false);
                        menu_T3UasbFiltro.setEnabled(false);
                        menuitem_reactoruasbT3.setEnabled(false);
                        menu_T4UasbLodos.setEnabled(false);
                        menuitem_reactoruasbT4.setEnabled(false);
                        menu_T5LodosAirExt.setEnabled(false);
                        menu_T6LodosModConv.setEnabled(false);
                        menuitem_T5LodosAireacion.setEnabled(false);
                        menuitem_T6LodosConvencional.setEnabled(false);
                        menuitem_filtropercoT3.setEnabled(false);
                        menuItem_Res_Uasb.setEnabled(false);
                        menuItem_Res_FiltroPerc.setEnabled(false);
                        menuitem_lagunafaculT2.setEnabled(false);
                        menuItem_Res_Uasb.setEnabled(false);
                        menuItem_Res_LagunaFacultUasb.setEnabled(false);
                        menuitem_lodsactivmodconvT4.setEnabled(false);
                        menuItem_Res_LodosActModConvUasb.setEnabled(false);
                        menuItem_Res_LodosActAirExt.setEnabled(false);
                        menuItem_Res_LodosActModConv.setEnabled(false);
                    }
                } else {
                    menu_Tecnologia0.setEnabled(false);
                    menuitem_rejillas.setEnabled(false);
                    menuitem_desarenador.setEnabled(false);
                    menuItem_Res_Rejillas.setEnabled(false);
                    menuItem_Res_Desarenador.setEnabled(false);
                    menuItem_Res_Parshall.setEnabled(false);
                    menuitem_CaractInicAguaResidual.setEnabled(false);
                    menu_T1Lagunas.setEnabled(false);
                    menuitem_lagunaerobia.setEnabled(false);
                    menuitem_lagunafacul1.setEnabled(false);
                    menuItem_Res_LagunaAnaerobia.setEnabled(false);
                    menuItem_Res_LagunaFacultativa.setEnabled(false);

                    menu_T2UasbLaguna.setEnabled(false);
                    menuitem_reactoruasbT2.setEnabled(false);
                    menu_T3UasbFiltro.setEnabled(false);
                    menuitem_reactoruasbT3.setEnabled(false);
                    menu_T4UasbLodos.setEnabled(false);
                    menuitem_reactoruasbT4.setEnabled(false);
                    menu_T5LodosAirExt.setEnabled(false);
                    menu_T6LodosModConv.setEnabled(false);
                    menuitem_T5LodosAireacion.setEnabled(false);
                    menuitem_T6LodosConvencional.setEnabled(false);
                    menuitem_filtropercoT3.setEnabled(false);
                    menuItem_Res_Uasb.setEnabled(false);
                    menuItem_Res_FiltroPerc.setEnabled(false);
                    menuitem_lagunafaculT2.setEnabled(false);
                    menuItem_Res_Uasb.setEnabled(false);
                    menuItem_Res_LagunaFacultUasb.setEnabled(false);
                    menuitem_lodsactivmodconvT4.setEnabled(false);
                    menuItem_Res_LodosActModConvUasb.setEnabled(false);
                    menuItem_Res_LodosActAirExt.setEnabled(false);
                    menuItem_Res_LodosActModConv.setEnabled(false);
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
                menu_T1Lagunas.setEnabled(false);
                menuitem_lagunaerobia.setEnabled(false);
                menuitem_lagunafacul1.setEnabled(false);
                menuItem_Res_LagunaAnaerobia.setEnabled(false);
                menuItem_Res_LagunaFacultativa.setEnabled(false);

                menu_T2UasbLaguna.setEnabled(false);
                menuitem_reactoruasbT2.setEnabled(false);
                menu_T3UasbFiltro.setEnabled(false);
                menuitem_reactoruasbT3.setEnabled(false);
                menu_T4UasbLodos.setEnabled(false);
                menuitem_reactoruasbT4.setEnabled(false);
                menu_T5LodosAirExt.setEnabled(false);
                menu_T6LodosModConv.setEnabled(false);
                menuitem_T5LodosAireacion.setEnabled(false);
                menuitem_T6LodosConvencional.setEnabled(false);
                menuitem_filtropercoT3.setEnabled(false);
                menuItem_Res_Uasb.setEnabled(false);
                menuItem_Res_FiltroPerc.setEnabled(false);
                menuitem_lagunafaculT2.setEnabled(false);
                menuItem_Res_Uasb.setEnabled(false);
                menuItem_Res_LagunaFacultUasb.setEnabled(false);
                menuitem_lodsactivmodconvT4.setEnabled(false);
                menuItem_Res_LodosActModConvUasb.setEnabled(false);
                menuItem_Res_LodosActAirExt.setEnabled(false);
                menuItem_Res_LodosActModConv.setEnabled(false);
            }
        } else {
            menuitem_export.setEnabled(false);//Botón exportar proyecto
            menu_Tecnologia0.setEnabled(false);
            menuitem_rejillas.setEnabled(false);
            menuitem_desarenador.setEnabled(false);
            menuItem_Res_Rejillas.setEnabled(false);
            menuItem_Res_Desarenador.setEnabled(false);
            menuItem_Res_Parshall.setEnabled(false);
            menuitem_ProyeccionPoblacional.setEnabled(false);
            menuitem_CalculoCaudalesDiseno.setEnabled(false);
            menuitem_CaractInicAguaResidual.setEnabled(false);
            menu_T1Lagunas.setEnabled(false);
            menuitem_lagunaerobia.setEnabled(false);
            menuitem_lagunafacul1.setEnabled(false);
            menuItem_Res_LagunaAnaerobia.setEnabled(false);
            menuItem_Res_LagunaFacultativa.setEnabled(false);

            menu_T2UasbLaguna.setEnabled(false);
            menuitem_reactoruasbT2.setEnabled(false);
            menu_T3UasbFiltro.setEnabled(false);
            menuitem_reactoruasbT3.setEnabled(false);
            menu_T4UasbLodos.setEnabled(false);
            menuitem_reactoruasbT4.setEnabled(false);
            menu_T5LodosAirExt.setEnabled(false);
            menu_T6LodosModConv.setEnabled(false);
            menuitem_T5LodosAireacion.setEnabled(false);
            menuitem_T6LodosConvencional.setEnabled(false);
            menuitem_filtropercoT3.setEnabled(false);
            menuItem_Res_Uasb.setEnabled(false);
            menuItem_Res_FiltroPerc.setEnabled(false);
            menuitem_lagunafaculT2.setEnabled(false);
            menuItem_Res_Uasb.setEnabled(false);
            menuItem_Res_LagunaFacultUasb.setEnabled(false);
            menuitem_lodsactivmodconvT4.setEnabled(false);
            menuItem_Res_LodosActModConvUasb.setEnabled(false);
            menuItem_Res_LodosActAirExt.setEnabled(false);
            menuItem_Res_LodosActModConv.setEnabled(false);
        }
    }

    private boolean CerrarVentanas() {

        if (!bod.pathProyecto.isEmpty()) {
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
    public boolean ComprobarCambio(int pos, boolean usarpath) {//Pendiente, (esta funcion se debe redefinir)

        boolean si = true;//Pendiente,en algunos casos se necesita retornar, pero el switch no deja, se fuerza con esta var

        switch (pos) {
            case 2:
                if (vbod.EditProyeccionPoblacional) {
                    C_ProyeccionPoblacional Cpp = new C_ProyeccionPoblacional(vbod);

                    if (!Cpp.InicializarComponentes()) { //Si la clase copia de la original del UI realiza los calculos con errores ...

                        int n = util.Mensaje(stError.replace("_", Cpp.error), "yesno"); // Mensaje para preguntar al usuario si desea continuar

                        if (n != JOptionPane.YES_OPTION) { // El usuario canceló el guardar
                            cancela = true;
                            return false;
                        }
                        vbod.EditProyeccionPoblacional = false; // Se deshabilitan los componenetes dependientes de sus datos
                        vbod.EditCalculoCaudalesDiseno = false;
                        vbod.EditCaractInicAguaResidual = false;
                        vbod.EditLagunaAnaerobia = false;
                        vbod.EditLagunaFacultativaSec = false;
                        vbod.EditRejillas = false;
                        vbod.EditDesarenador = false;
                        vbod.EditReactorUasb = false;
                        vbod.EditFiltroPercolador = false;
                        vbod.EditLodosActivadsConvenc = false;
                        vbod.EditLodosActivadsAireacExt = false;
                        vbod.EditLodosActivadsConvencUASB = false;
                        vbod.EditLagunaFacultativaUASB = false;
                        if (si) {
                            return vbod.exportarProyecto(usarpath);
                        }
                    } else { // Los càlculos no tuvieron error, paso al sigueinte que pueda depender de los datos
                        return ComprobarCambio(3, usarpath);
                    }
                } else {
                    return vbod.exportarProyecto(usarpath); //Si hacia adelante no esta habilitado nada, se guarda
                }
                break;
            case 3: // Caudales de Diseño (puede desactivar componentes)
                if (vbod.EditCalculoCaudalesDiseno) {
                    C_CalculoCaudalesDiseno Ccc = new C_CalculoCaudalesDiseno(vbod);

                    if (!Ccc.InicializarComponentes()) {
                        int n = util.Mensaje(stError.replace("_", Ccc.error), "yesno");

                        if (n != JOptionPane.YES_OPTION) {
                            cancela = true;
                            return false;
                        }
                        vbod.EditCalculoCaudalesDiseno = false;
                        vbod.EditCaractInicAguaResidual = false;
                        vbod.EditLagunaAnaerobia = false;
                        vbod.EditLagunaFacultativaSec = false;
                        vbod.EditRejillas = false;
                        vbod.EditDesarenador = false;
                        vbod.EditReactorUasb = false;
                        vbod.EditFiltroPercolador = false;
                        vbod.EditLodosActivadsConvenc = false;
                        vbod.EditLodosActivadsAireacExt = false;
                        vbod.EditLodosActivadsConvencUASB = false;
                        vbod.EditLagunaFacultativaUASB = false;
                        if (si) {
                            return vbod.exportarProyecto(usarpath);
                        }
                    } else {
                        return ComprobarCambio(4, usarpath);
                    }
                } else {
                    return vbod.exportarProyecto(usarpath);
                }
                break;

            case 4://Tratamiento preliminar Rejillas_ es obligatorio (puede desactivar componentes)
                if (vbod.EditRejillas) {
                    C_Rejillas Cre = new C_Rejillas(vbod);

                    if (!Cre.InicializarComponentes()) {
                        int n = util.Mensaje(stError.replace("_", Cre.error), "yesno");

                        if (n != JOptionPane.YES_OPTION) {
                            cancela = true;
                            return false;
                        }
                        vbod.EditRejillas = false;
                        vbod.EditLagunaAnaerobia = false;
                        vbod.EditLagunaFacultativaSec = false;
                        vbod.EditDesarenador = false;
                        vbod.EditReactorUasb = false;
                        vbod.EditFiltroPercolador = false;
                        vbod.EditLodosActivadsConvenc = false;
                        vbod.EditLodosActivadsAireacExt = false;
                        vbod.EditLodosActivadsConvencUASB = false;
                        vbod.EditLagunaFacultativaUASB = false;
                        if (si) {
                            return vbod.exportarProyecto(usarpath);
                        }
                    } else {
                        return ComprobarCambio(5, usarpath);
                    }
                } else {
                    return vbod.exportarProyecto(usarpath);
                }
                break;

            case 5://Tratamiento preliminar Desarenador es obligatorio
                if (vbod.EditDesarenador) {

                    C_Desarenador Cdes = new C_Desarenador(vbod);
                    if (!Cdes.InicializarComponentes()) {
                        int n = util.Mensaje(stError.replace("_", Cdes.error), "yesno");

                        if (n != JOptionPane.YES_OPTION) {
                            cancela = true;
                            return false;
                        }
                        vbod.EditDesarenador = false;
                        vbod.EditLagunaAnaerobia = false; //Esto se coloca aqui ya que se definio que T. preliminar es obligatorio
                        vbod.EditLagunaFacultativaSec = false;
                        vbod.EditReactorUasb = false;
                        vbod.EditFiltroPercolador = false;
                        vbod.EditLodosActivadsConvenc = false;
                        vbod.EditLodosActivadsAireacExt = false;
                        vbod.EditLodosActivadsConvencUASB = false;
                        vbod.EditLagunaFacultativaUASB = false;
                        if (si) {
                            return vbod.exportarProyecto(usarpath);
                        }
                    } else {
                        if (!rejilladesarena) {
                            return ComprobarCambio(6, usarpath);
                        } else {
                            rejilladesarena = false;
                            return true;
                        }
                    }
                } else {
                    return vbod.exportarProyecto(usarpath);
                }
                break;

            case 6: // Se trata de comprobar si alguna tecnologìa falla

                boolean tec001LagAna = ComprobarCambio(8, usarpath);
                boolean tec001LagFac = ComprobarCambio(9, usarpath);
                boolean tec234ReUasb = ComprobarCambio(10, usarpath);
                boolean tec002LagFaU = ComprobarCambio(11, usarpath);
                boolean tec003FilPer = ComprobarCambio(12, usarpath);
                boolean tec004LodCoU = ComprobarCambio(13, usarpath);
                boolean tec005LodExt = ComprobarCambio(14, usarpath);
                boolean tec006LodCon = ComprobarCambio(15, usarpath);

                if (!tec001LagAna || !tec001LagFac || !tec234ReUasb || !tec002LagFaU || !tec003FilPer || !tec004LodCoU || !tec005LodExt || !tec006LodCon) {

                    int n = util.Mensaje(stError.replace("_", mensajes), "yesno");

                    if (n != JOptionPane.YES_OPTION) {
                        cancela = true;
                        return false;
                    }
                } else {
                    return vbod.exportarProyecto(usarpath);
                }

                if (tec001LagAna) { //LAGUNAS

                    if (!tec001LagFac) {
                        vbod.EditLagunaFacultativaSec = false;
                    }
                } else {
                    vbod.EditLagunaAnaerobia = false;
                    vbod.EditLagunaFacultativaSec = false;
                }

                if (tec234ReUasb) { //UASB

                    if (!tec002LagFaU) {
                        vbod.EditLagunaFacultativaUASB = false;
                    }

                    if (!tec003FilPer) {
                        vbod.EditFiltroPercolador = false;
                    }

                    if (!tec004LodCoU) {
                        vbod.EditLodosActivadsConvencUASB = false;
                    }
                } else {
                    vbod.EditReactorUasb = false;
                    vbod.EditLagunaFacultativaUASB = false;
                    vbod.EditFiltroPercolador = false;
                    vbod.EditLodosActivadsConvencUASB = false;
                }

                if (!tec005LodExt) {
                    vbod.EditLodosActivadsAireacExt = false;
                }

                if (!tec006LodCon) {
                    vbod.EditLodosActivadsConvenc = false;
                }

                if (si) {
                    return vbod.exportarProyecto(usarpath);
                }

                break;

            case 7: // Se trata de comprobar si alguna tecnologia falla por parte del uasb

                boolean tec002LagFaUuasb = ComprobarCambio(11, usarpath);
                boolean tec003FilPeruasb = ComprobarCambio(12, usarpath);
                boolean tec004LodCoUuasb = ComprobarCambio(13, usarpath);

                if (!tec002LagFaUuasb || !tec003FilPeruasb || !tec004LodCoUuasb) {
                    int n = util.Mensaje(stError.replace("_", mensajes), "yesno");

                    if (n != JOptionPane.YES_OPTION) {
                        cancela = true;
                        return false;
                    }
                } else {
                    return vbod.exportarProyecto(usarpath);
                }

                if (!tec002LagFaUuasb) {
                    vbod.EditLagunaFacultativaUASB = false;
                }

                if (!tec003FilPeruasb) {
                    vbod.EditFiltroPercolador = false;
                }

                if (!tec004LodCoUuasb) {
                    vbod.EditLodosActivadsConvencUASB = false;
                }

                if (si) {
                    return vbod.exportarProyecto(usarpath);
                }

                break;

            case 16: // Se trata de comprobar la tecnologia Laguna Fac. falla por parte de la Tecnologìa Laguna An.

                boolean tec002LagFaSec = ComprobarCambio(11, usarpath);

                if (!tec002LagFaSec) {
                    int n = util.Mensaje(stError.replace("_", mensajes), "yesno");

                    if (n != JOptionPane.YES_OPTION) {
                        cancela = true;
                        return false;
                    }
                } else {
                    return vbod.exportarProyecto(usarpath);
                }

                if (!tec002LagFaSec) {
                    vbod.EditLagunaFacultativaUASB = false;
                }


                if (si) {
                    return vbod.exportarProyecto(usarpath);
                }

                break;

            case 8:
                if (vbod.EditLagunaAnaerobia) { //Tecnologia 1 Laguna Anaerobia

                    C_LagunaAnaerobia Clan = new C_LagunaAnaerobia(vbod);

                    if (!Clan.InicializarComponentes()) {
                        mensajes += Clan.error + " ";
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    if (si) {
                        return true;//Pendiente
                    }
                }
                break;

            case 9:
                if (vbod.EditLagunaFacultativaSec) { //Tecnologia 1 Laguna Facultativa Secundaria

                    C_LagunaFacultativaSec Claf = new C_LagunaFacultativaSec(vbod);

                    if (!Claf.InicializarComponentes()) {
                        mensajes += Claf.error + " ";
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    if (si) {
                        return true;
                    }
                }
                break;

            case 10:
                if (vbod.EditReactorUasb) { //Tecnologia 2,3,4 EditReactor Uasb

                    C_ReactorUasb Clua = new C_ReactorUasb(vbod);

                    if (!Clua.InicializarComponentes()) {
                        mensajes += Clua.error + " ";
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    if (si) {
                        return true;
                    }
                }
                break;

            case 11:
                if (vbod.EditLagunaFacultativaUASB) { //Tecnologia 2 Laguna Facultativa UASB

                    C_LagunaFacultativaSecUasb Cluf = new C_LagunaFacultativaSecUasb(vbod);

                    if (!Cluf.InicializarComponentes()) {
                        mensajes += Cluf.error + " ";
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    if (si) {
                        return true;
                    }
                }
                break;

            case 12:
                if (vbod.EditFiltroPercolador) { //Tecnologia 3 Filtro Percolador  

                    C_FiltroPercolador Clfp = new C_FiltroPercolador(vbod);

                    if (!Clfp.InicializarComponentes()) {
                        mensajes += Clfp.error + " ";
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    if (si) {
                        return true;
                    }
                }
                break;

            case 13:
                if (vbod.EditLodosActivadsConvencUASB) { //Tecnologia 4 Lodos Activados Modalidad Convencional UASB

                    C_LodosActivadsModConvUASB Cllau = new C_LodosActivadsModConvUASB(vbod);

                    if (!Cllau.InicializarComponentes()) {
                        mensajes += Cllau.error + " ";
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    if (si) {
                        return true;
                    }
                }
                break;

            case 14:
                if (vbod.EditLodosActivadsAireacExt) { //Tecnologia 5 Lodos Activados Aireaciòn Extendida  

                    C_LodosActivModAirExten Claex = new C_LodosActivModAirExten(vbod);

                    if (!Claex.InicializarComponentes()) {
                        mensajes += Claex.error + " ";
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    if (si) {
                        return true;
                    }
                }
                break;

            case 15:
                if (vbod.EditLodosActivadsConvenc) { //Tecnologia 6 Lodos Activados Modalidad Convencional  

                    C_LodosActivadsModConv Cllac = new C_LodosActivadsModConv(vbod);

                    if (!Cllac.InicializarComponentes()) {
                        mensajes += Cllac.error + " ";
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    if (si) {
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    /**
     * *
     * Se llama a este metodo desde las ventanas al haber un guardado sin
     * errores. Le indica al usuario donde debería continuar en las ventanas por
     * mensaje.
     */
    public void guiaUsuario() {

        String guardado = "<HTML>Guardado Exitosamente,<br/> Puede hacer click en <font color=#0066cc><b>'Cerrar'</b></font> y continuar diligenciando: ";
        String guardad2 = "<HTML>Guardado Exitosamente</HTML>";

        switch (tecnologia) {
            case "DATENT_DATOSDEENTRADA":
            case "DATENT_CARACAGUAINIC":
            case "DATENT_CALCULCAUDISE":
            case "DATENT_PROYECNPOBLAC":
                if (!bod.EditProyeccionPoblacional || !bod.EditCalculoCaudalesDiseno || !bod.EditCaractInicAguaResidual) {
                    guardado += "<font color=#ff3300><b> 'DATOS'</b></font></HTML>";
                    util.Mensaje(guardado, "ok");
                } else {
                    if (!bod.EditRejillas) {
                        guardado += "<font color=#ff3300><b> 'TECNOLOGIAS'</b></font></HTML>";
                        util.Mensaje(guardado, "ok");
                    }
                }
                break;
            case "TPREJILLA_T0":
                if (!bod.EditDesarenador) {
                    guardado += "<font color=#ff3300><b>'DESARENADOR'</b></font></HTML>";//Todo: traer mensajes de la bd
                    util.Mensaje(guardado, "ok");
                } else {
                    util.Mensaje(guardad2, "ok");
                }
                break;
            case "TPDESAREN_T0":
                if (!bod.EditLagunaAnaerobia || !bod.EditReactorUasb || !bod.EditLodosActivadsConvenc || !bod.EditLodosActivadsAireacExt) {
                    guardado += "<font color=#ff3300><b>'TECNOLOGÍAS DE TRATAMIENTO SECUNDARIO.'</b></font></HTML>";
                    util.Mensaje(guardado, "ok");
                } else {
                    util.Mensaje(guardad2, "ok");
                }
                break;
            case "LAGUNANRB_T1":
                if (!bod.EditLagunaFacultativaSec) {
                    guardado += "<font color=#ff3300><b>'LAGUNA FACULTATIVA.'</b></font></HTML>";
                    util.Mensaje(guardado, "ok");
                } else {
                    util.Mensaje(guardad2, "ok");
                }
                break;
            case "REACTUASB_T2":
                if (!bod.EditLagunaFacultativaUASB) {
                    guardado += "<font color=#ff3300><b>'LAGUNA FACULTATIVA.'</b></font></HTML>";
                    util.Mensaje(guardado, "ok");
                } else {
                    util.Mensaje(guardad2, "ok");
                }
                break;
            case "REACTUASB_T3":
                if (!bod.EditFiltroPercolador) {
                    guardado += "<font color=#ff3300><b>'FILTRO PERCOLADOR'</b></font></HTML>";
                    util.Mensaje(guardado, "ok");
                } else {
                    util.Mensaje(guardad2, "ok");
                }
                break;
            case "REACTUASB_T4":
                if (tecnologia.equals("REACTUASB_T4") && !bod.EditLodosActivadsConvencUASB) {
                    guardado += "<font color=#ff3300><b>'LODOS ACTIVADOS MODALIDAD CONVENCIONAL'</b></font></HTML>";
                    util.Mensaje(guardado, "ok");
                } else {
                    util.Mensaje(guardad2, "ok");
                }
                break;
            case "LODACTCNV_T4":
                util.Mensaje(guardad2, "ok");
                break;
            case "LODACTAEX_T5":
                util.Mensaje(guardad2, "ok");
                break;
            case "LODACTCNV_T6":
                util.Mensaje(guardad2, "ok");
                break;
        } 
    }

    /**
     * Se llama a este metodo desde las ventanas al haber un guardado sin
     * errores. Le indica al usuario donde debería continuar en las ventanas
     * visualmente.
     */
    public void guiaUsuario2() {

        if (tecnologia.equals("TPREJILLA_T0") && !bod.EditDesarenador) {
            menu_Tecnologias.doClick();
            menu_Tecnologia0.doClick();

        } else if (tecnologia.equals("DATENT_DATOSDEENTRADA") || tecnologia.equals("DATENT_CARACAGUAINIC") || tecnologia.equals("DATENT_CALCULCAUDISE") || tecnologia.equals("DATENT_PROYECNPOBLAC")) {

            if (!bod.EditProyeccionPoblacional || !bod.EditCalculoCaudalesDiseno || !bod.EditCaractInicAguaResidual) {
                menu_Datos.doClick();
            } else {
                if (!bod.EditRejillas) {
                    menu_Tecnologias.doClick();
                }
            }
        } else if (tecnologia.equals("TPDESAREN_T0")) {
            menu_Tecnologias.doClick();

        } else if (tecnologia.equals("LAGUNANRB_T1") && !bod.EditLagunaFacultativaSec) {
            menu_Tecnologias.doClick();
            menu_T1Lagunas.doClick();

        } else if (tecnologia.equals("REACTUASB_T2") && !bod.EditLagunaFacultativaUASB) {
            menu_Tecnologias.doClick();
            menu_T2UasbLaguna.doClick();

        } else if (tecnologia.equals("REACTUASB_T3") && !bod.EditFiltroPercolador) {
            menu_Tecnologias.doClick();
            menu_T3UasbFiltro.doClick();

        } else if (tecnologia.equals("REACTUASB_T4") && !bod.EditLodosActivadsConvencUASB) {
            menu_Tecnologias.doClick();
            menu_T4UasbLodos.doClick();
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
     * Crea una fila de tipo tabla HTML con los datos de la variable
     *
     * @param var fila HTML formateada
     * @return
     */
    private String fila(String var, int tipo) { //-1 Trae el título 1, -2 Trae el título 2, 
        try {
            ResultSet rs;

            if (tipo > 0) { //Es una fila especial para secciones
                rs = db.ResultadoSelect("obtenerseccion", tipo + "", null);
                var = "<tr bgcolor='#35556c'><td colspan='3'><b>" + rs.getString("Nombre") + "</b></td></tr>";
            } else if (tipo < 0) { //Fila de la que se necesita solo un titulo
                if (tipo == -1) {
                    rs = db.ResultadoSelect("datospregunta", var);
                    var = "<tr><td colspan='3'>" + rs.getString("titulo1") + "</td></tr>";
                } else if (tipo == -2) {
                    rs = db.ResultadoSelect("datospregunta", var);
                    var = "<tr><td colspan='3'>" + rs.getString("titulo2") + "</td></tr>";
                } else if (tipo == -3) {
                    rs = db.ResultadoSelect("datospregunta", var);
                    var = "<tr><td colspan='3'>" + rs.getString("descripcion") + "</td></tr>";
                } else { //Para obetener valores inmediatos, preferiblemente de cadenas
                    rs = db.ResultadoSelect("datospregunta", var);
                    var = "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getVarStr(var) + "</td><td>" + rs.getString("titulo2") + "</td></tr>";
                }
            } else {
                rs = db.ResultadoSelect("datospregunta", var);

                String titulo2 = rs.getString("titulo2");

                if (titulo2.contains("|")) {
                    String[] titulo = rs.getString("titulo2").split("\\|");
                    titulo2 = titulo[1];
                }

                var = "<tr><td>" + rs.getString("titulo1") + "</td><td>" + bod.getVarFormateadaPorNombre(var, rs.getInt("decimales")) + "</td><td>" + titulo2 + "</td></tr>";
            }
        } catch (Exception ex) {
            log.error("Error fila() " + ex.getMessage());
            var = "";
        } finally {
            db.close(); //Se cierra la conexiòn
        }
        return var;
    }

    /**
     * *
     * Carga los datos ya editados como tabla HTML de los principales datos de
     * entrada en los label de esta ventana principal. Para que el usuario los
     * pueda visualizar.
     */
    public void CargarDatosFondo() {

        if (!lbl_datosfondo2.isDisplayable()) {
            jdp_Main.add(lbl_datosfondo1);
            jdp_Main.add(lbl_datosfondo2);
        }

        String datos = "<HTML><style>table td {border: 1px solid #567288;border-collapse: collapse;} </style><table cellspacing='0' cellpadding='0'>"; //border='1' cellspacing='0'
        String datos2 = datos;
        ResultSet rs;

        if (bod.EditDatosDeEntrada) {
            datos += fila("", 1);
            datos += fila("TFA", 0);
            datos += fila("TML", 0);
            datos += fila("TCA", 0);
            datos += fila("TCI", 0);
            datos += fila("TCR", 0);
            datos += fila("TUC", 0);
            datos += fila("TDI", 0);
            datos += fila("TRE", 0);
            datos += fila("TDP", 0);
        }

        if (bod.EditProyeccionPoblacional) {
            datos += fila("", 2);

            if (bod.getVarInt("PAG") > 0) {
                datos += fila("PAG", -1);
            } else {
                datos += fila("PAG", -2);
            }
            datos += fila("PPT", 0);
            datos += fila("PPF", 0);
            datos += fila("PCR", -4);//ESTE SE QUITA??
        }

        if (bod.EditCalculoCaudalesDiseno) {
            datos += fila("", 3);
            datos += fila("CAR", 0);

            if (bod.getVarInt("QIF") == 0) { //0 = extension... 
                datos += fila("QIF", -1);
                datos += fila("QEL", 0);
                datos += fila("QET", 0);
                datos += fila("QEC", 0);
                datos += fila("QEK", 0);
            } else {                        // 1= area...
                datos += fila("QIF", -2);
                datos += fila("QAA", 0);
                datos += fila("QAI", 0);
                datos += fila("QAC", 0);
                datos += fila("QAK", 0);
            }

            if (bod.getVarInt("QCD") > 0) {
                datos += fila("QCD", -3);
                datos += fila("QCA", 0);
                datos += fila("QCM", 0);
                datos += fila("QCC", 0);
                datos += fila("QCK", 0);
            }

            if (bod.getVarInt("IOA") > 0) {
                datos += fila("IOA", -1);
                if (bod.getVarInt("IRM") > 0) {
                    datos += fila("IQ2", 0);
                    datos += fila("IK2", 0);

                } else {
                    datos += fila("IAI", 0);
                    datos += fila("IPI", 0);
                    datos += fila("IQI", 0);
                    datos += fila("IKI", 0);
                }
            }

            if (bod.getVarInt("COA") > 0) {
                datos2 += fila("COA", -1);
                datos2 += fila("CAC", 0);
                datos2 += fila("CPC", 0);
                datos2 += fila("CQC", 0);
                datos2 += fila("CKC", 0);
            }

            if (bod.getVarInt("COA") > 0) {
                datos2 += fila("YOA", -1);
                datos2 += fila("YAI", 0);
                datos2 += fila("YPI", 0);
                datos2 += fila("YQI", 0);
                datos2 += fila("YKI", 0);
            }
            datos2 += fila("Q2C", 0);
            datos2 += fila("Q2K", 0);
            datos2 += fila("Q3M", 0);
            datos2 += fila("Q3C", 0);
            datos2 += fila("Q3K", 0);
            datos2 += fila("Q1H", 0);
            datos2 += fila("Q1C", 0);
            datos2 += fila("Q1K", 0);
        }

        if (bod.EditCaractInicAguaResidual) {
            datos2 += fila("", 4);
            datos2 += fila("CAT", 0);
            datos2 += fila("CAB", 0);
            datos2 += fila("CAQ", 0);
            datos2 += fila("CAS", 0);
            datos2 += fila("CAV", 0);
            datos2 += fila("CVS", 0);
        }

        datos += "</table></HTML>";
        datos2 += "</table></HTML>";
        lbl_datosfondo1.setText(datos);
        lbl_datosfondo2.setText(datos2);
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

        if (bod.VentanasAbiertas()) {//Si existen otras ventanas abiertas no deja abrir mas
            return;
        }

        Bod vod = (Bod) bod.clone();//Se crea un clon vod para copiar los datos de 'bod' y no escribirlo directamente. En el metodo de guardado (si fue exitoso) se pasan los datos a bod
        DatosEntrada = null;
        DatosEntrada = new DatosEntrada(vod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) DatosEntrada.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(DatosEntrada);
        DatosEntrada.setLocation(1, 1);
        DatosEntrada.setVisible(true);

        bod.WinDatosDeEntrada = true;
        try {
            DatosEntrada.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirProyeccionPoblacional() {

        if (bod.VentanasAbiertas()) {
            return;
        }

        Bod vod = (Bod) bod.clone();
        Proyeccionpoblacional = null;
        Proyeccionpoblacional = new ProyeccionPoblacional(vod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) Proyeccionpoblacional.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(Proyeccionpoblacional);
        Proyeccionpoblacional.setLocation(1, 1);
        Proyeccionpoblacional.setVisible(true);
        bod.WinProyeccionPoblacional = true;
        try {
            Proyeccionpoblacional.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirCalculoCaudalesDiseno() {

        if (bod.VentanasAbiertas()) {
            return;
        }

        Bod vod = (Bod) bod.clone();

        Calculocaudalesdiseno = null;
        Calculocaudalesdiseno = new CalculoCaudalesDiseno(vod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) Calculocaudalesdiseno.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(Calculocaudalesdiseno);
        Calculocaudalesdiseno.setLocation(1, 1);
        Calculocaudalesdiseno.setVisible(true);

        bod.WinCalculoCaudalesDiseno = true;
        try {
            Calculocaudalesdiseno.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirCaractInicAguaResidual() {

        if (bod.VentanasAbiertas()) {
            return;
        }

        Bod vod = (Bod) bod.clone();
        Caractinicaguaresidual = null;
        Caractinicaguaresidual = new CaracterAguaResidual(vod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) Caractinicaguaresidual.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(Caractinicaguaresidual);
        Caractinicaguaresidual.setLocation(1, 1);
        Caractinicaguaresidual.setVisible(true);

        bod.WinCaractInicAguaResidual = true;
        try {
            Caractinicaguaresidual.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirUasb() {

        if (bod.VentanasAbiertas()) {
            return;
        }

        Bod vod = (Bod) bod.clone();
        ReactorUasb = null;
        ReactorUasb = new ReactorUASB(vod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) ReactorUasb.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(ReactorUasb);
        ReactorUasb.setLocation(1, 1);
        ReactorUasb.setVisible(true);

        bod.WinReactorUasb = true;
        try {
            ReactorUasb.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirLodosActivadosConvencional() {

        if (bod.VentanasAbiertas()) {
            return;
        }

        Bod vod = (Bod) bod.clone();
        lodosactivadosmodconv = null;
        lodosactivadosmodconv = new LodosActivadsModConv(vod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) lodosactivadosmodconv.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(lodosactivadosmodconv);
        lodosactivadosmodconv.setLocation(1, 1);
        lodosactivadosmodconv.setVisible(true);

        bod.WinLodosActivadsConvenc = true;
        try {
            lodosactivadosmodconv.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirLodosActivadosConvencionalUASB() {

        if (bod.VentanasAbiertas()) {
            return;
        }

        Bod vod = (Bod) bod.clone();
        lodosactivadosmodconvUASB = null;
        lodosactivadosmodconvUASB = new LodosActivadsModConvUASB(vod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) lodosactivadosmodconvUASB.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(lodosactivadosmodconvUASB);
        lodosactivadosmodconvUASB.setLocation(1, 1);
        lodosactivadosmodconvUASB.setVisible(true);

        bod.WinLodosActivadsConvencUASB = true;
        try {
            lodosactivadosmodconvUASB.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirLodosActivadosAireacionExtendida() {

        if (bod.VentanasAbiertas()) {
            return;
        }

        Bod vod = (Bod) bod.clone();
        lodosactivadosairexten = null;
        lodosactivadosairexten = new LodosActivModAirExten(vod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) lodosactivadosairexten.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(lodosactivadosairexten);
        lodosactivadosairexten.setLocation(1, 1);
        lodosactivadosairexten.setVisible(true);

        bod.WinLodosActivadsAireacExt = true;
        try {
            lodosactivadosairexten.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirFiltro() {

        if (bod.VentanasAbiertas()) {
            return;
        }

        Bod vod = (Bod) bod.clone();
        filtropercolador = null;
        filtropercolador = new FiltroPercolador(vod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) filtropercolador.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(filtropercolador);
        filtropercolador.setLocation(1, 1);
        filtropercolador.setVisible(true);

        bod.WinFiltroPercolador = true;
        try {
            filtropercolador.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirRejillas() {

        if (bod.VentanasAbiertas()) {
            return;
        }

        Bod vod = (Bod) bod.clone();
        rejillas = null;
        rejillas = new Rejillas(vod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) rejillas.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(rejillas);
        rejillas.setLocation(1, 1);
        rejillas.setVisible(true);

        bod.WinRejillas = true;
        try {
            rejillas.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirDesarenador() {

        if (bod.VentanasAbiertas()) {
            return;
        }

        Bod vod = (Bod) bod.clone();
        desarenador = null;
        desarenador = new Desarenador(vod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) desarenador.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(desarenador);
        desarenador.setLocation(1, 1);
        desarenador.setVisible(true);

        bod.WinDesarenador = true;
        try {
            desarenador.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirLagunaAnanerobia() {

        if (bod.VentanasAbiertas()) {
            return;
        }

        Bod vod = (Bod) bod.clone();
        Lagunaanaerobia = null;
        Lagunaanaerobia = new LagunaAnaerobia(vod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) Lagunaanaerobia.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(Lagunaanaerobia);
        Lagunaanaerobia.setLocation(1, 1);
        Lagunaanaerobia.setVisible(true);

        bod.WinLagunaAnaerobia = true;
        try {
            Lagunaanaerobia.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirLagunaFacultativaSecundaria() {

        if (bod.VentanasAbiertas()) {
            return;
        }

        Bod vod = (Bod) bod.clone();
        Lagunafacultativasec = null;
        Lagunafacultativasec = new LagunaFacultativaSec(vod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) Lagunafacultativasec.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(Lagunafacultativasec);
        Lagunafacultativasec.setLocation(1, 1);
        Lagunafacultativasec.setVisible(true);

        bod.WinLagunaFacultativaSec = true;
        try {
            Lagunafacultativasec.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirLagunaFacultativaSecundariaUasb() {

        if (bod.VentanasAbiertas()) {
            return;
        }

        Bod vod = (Bod) bod.clone();
        Lagunafacultativasecuasb = null;
        Lagunafacultativasecuasb = new LagunaFacultativaSecUASB(vod);

        BasicInternalFrameUI biu = (BasicInternalFrameUI) Lagunafacultativasecuasb.getUI(); //Quita bordes que permiten arrastrar la ventana.
        biu.setNorthPane(null);

        jdp_Main.add(Lagunafacultativasecuasb);
        Lagunafacultativasecuasb.setLocation(1, 1);
        Lagunafacultativasecuasb.setVisible(true);

        bod.WinLagunaFacultativaUASB = true;
        try {
            Lagunafacultativasecuasb.setMaximum(true);//Colocar el tamaño màximo relativo al su contenedor
        } catch (PropertyVetoException e) {
            //todo: implementar exepcion
        }
    }

    private void AbrirResultado(String tecnbd) {

        if (bod.VentanasAbiertas()) {
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
        menu_T1Lagunas = new javax.swing.JMenu();
        menuitem_lagunaerobia = new javax.swing.JMenuItem();
        menuitem_lagunafacul1 = new javax.swing.JMenuItem();
        Sep_1 = new javax.swing.JPopupMenu.Separator();
        menu_T2UasbLaguna = new javax.swing.JMenu();
        menuitem_reactoruasbT2 = new javax.swing.JMenuItem();
        menuitem_lagunafaculT2 = new javax.swing.JMenuItem();
        Sep_2 = new javax.swing.JPopupMenu.Separator();
        menu_T3UasbFiltro = new javax.swing.JMenu();
        menuitem_reactoruasbT3 = new javax.swing.JMenuItem();
        menuitem_filtropercoT3 = new javax.swing.JMenuItem();
        Sep_3 = new javax.swing.JPopupMenu.Separator();
        menu_T4UasbLodos = new javax.swing.JMenu();
        menuitem_reactoruasbT4 = new javax.swing.JMenuItem();
        menuitem_lodsactivmodconvT4 = new javax.swing.JMenuItem();
        Sep_4 = new javax.swing.JPopupMenu.Separator();
        menu_T5LodosAirExt = new javax.swing.JMenu();
        menuitem_T5LodosAireacion = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        menu_T6LodosModConv = new javax.swing.JMenu();
        menuitem_T6LodosConvencional = new javax.swing.JMenuItem();
        jMenu_separador3 = new javax.swing.JMenu();
        menu_Resultados = new javax.swing.JMenu();
        menuItem_Res_Rejillas = new javax.swing.JMenuItem();
        menuItem_Res_Desarenador = new javax.swing.JMenuItem();
        menuItem_Res_Parshall = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuItem_Res_LagunaAnaerobia = new javax.swing.JMenuItem();
        menuItem_Res_LagunaFacultativa = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        menuItem_Res_Uasb = new javax.swing.JMenuItem();
        menuItem_Res_LagunaFacultUasb = new javax.swing.JMenuItem();
        menuItem_Res_FiltroPerc = new javax.swing.JMenuItem();
        menuItem_Res_LodosActModConvUasb = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        menuItem_Res_LodosActAirExt = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        menuItem_Res_LodosActModConv = new javax.swing.JMenuItem();
        jMenu_separador4 = new javax.swing.JMenu();
        menu_Acerca = new javax.swing.JMenu();
        menuItem_Ayuda = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        menuItem_Acercade = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1024, 740));
        setResizable(false);

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
        menuitem_export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitem_exportActionPerformed(evt);
            }
        });
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

        menu_T1Lagunas.setText("Tecn. Laguna Anaerobia + Laguna Facultativa");
        menu_T1Lagunas.setEnabled(false);

        menuitem_lagunaerobia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_lagunas.png"))); // NOI18N
        menuitem_lagunaerobia.setText("Laguna Anaerobia");
        menuitem_lagunaerobia.setEnabled(false);
        menuitem_lagunaerobia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitem_lagunaerobiaActionPerformed(evt);
            }
        });
        menu_T1Lagunas.add(menuitem_lagunaerobia);

        menuitem_lagunafacul1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_lagunas.png"))); // NOI18N
        menuitem_lagunafacul1.setText("Laguna Facultativa");
        menuitem_lagunafacul1.setEnabled(false);
        menuitem_lagunafacul1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitem_lagunafacul1ActionPerformed(evt);
            }
        });
        menu_T1Lagunas.add(menuitem_lagunafacul1);

        menu_Tecnologias.add(menu_T1Lagunas);
        menu_Tecnologias.add(Sep_1);

        menu_T2UasbLaguna.setText("Tecn. Reactor UASB + Laguna Facultativa");
        menu_T2UasbLaguna.setEnabled(false);

        menuitem_reactoruasbT2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_uasb.png"))); // NOI18N
        menuitem_reactoruasbT2.setText("Reactor UASB");
        menuitem_reactoruasbT2.setEnabled(false);
        menuitem_reactoruasbT2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitem_reactoruasbT2ActionPerformed(evt);
            }
        });
        menu_T2UasbLaguna.add(menuitem_reactoruasbT2);

        menuitem_lagunafaculT2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_lagunas.png"))); // NOI18N
        menuitem_lagunafaculT2.setText("Laguna Facultativa");
        menuitem_lagunafaculT2.setEnabled(false);
        menuitem_lagunafaculT2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitem_lagunafaculT2ActionPerformed(evt);
            }
        });
        menu_T2UasbLaguna.add(menuitem_lagunafaculT2);

        menu_Tecnologias.add(menu_T2UasbLaguna);
        menu_Tecnologias.add(Sep_2);

        menu_T3UasbFiltro.setText("Tecn. Reactor UASB + Filtro Percolador");
        menu_T3UasbFiltro.setEnabled(false);

        menuitem_reactoruasbT3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_uasb.png"))); // NOI18N
        menuitem_reactoruasbT3.setText("Reactor UASB");
        menuitem_reactoruasbT3.setEnabled(false);
        menuitem_reactoruasbT3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitem_reactoruasbT3ActionPerformed(evt);
            }
        });
        menu_T3UasbFiltro.add(menuitem_reactoruasbT3);

        menuitem_filtropercoT3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_filtroperc.png"))); // NOI18N
        menuitem_filtropercoT3.setText("Filtro Percolador");
        menuitem_filtropercoT3.setEnabled(false);
        menuitem_filtropercoT3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitem_filtropercoT3ActionPerformed(evt);
            }
        });
        menu_T3UasbFiltro.add(menuitem_filtropercoT3);

        menu_Tecnologias.add(menu_T3UasbFiltro);
        menu_Tecnologias.add(Sep_3);

        menu_T4UasbLodos.setText("Tecn. Reactor UASB + Lodos Activados Modalidad convencional");
        menu_T4UasbLodos.setEnabled(false);

        menuitem_reactoruasbT4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_uasb.png"))); // NOI18N
        menuitem_reactoruasbT4.setText("Reactor UASB");
        menuitem_reactoruasbT4.setEnabled(false);
        menuitem_reactoruasbT4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitem_reactoruasbT4ActionPerformed(evt);
            }
        });
        menu_T4UasbLodos.add(menuitem_reactoruasbT4);

        menuitem_lodsactivmodconvT4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_lodos.png"))); // NOI18N
        menuitem_lodsactivmodconvT4.setText("Lodos Activados Modalidad convencional");
        menuitem_lodsactivmodconvT4.setEnabled(false);
        menuitem_lodsactivmodconvT4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitem_lodsactivmodconvT4ActionPerformed(evt);
            }
        });
        menu_T4UasbLodos.add(menuitem_lodsactivmodconvT4);

        menu_Tecnologias.add(menu_T4UasbLodos);
        menu_Tecnologias.add(Sep_4);

        menu_T5LodosAirExt.setText("Tecn. Lodos Activados Aireación Extendida");
        menu_T5LodosAirExt.setToolTipText("");
        menu_T5LodosAirExt.setEnabled(false);

        menuitem_T5LodosAireacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_lodos.png"))); // NOI18N
        menuitem_T5LodosAireacion.setText("Lodos Activados Aireación Extendida");
        menuitem_T5LodosAireacion.setToolTipText("");
        menuitem_T5LodosAireacion.setEnabled(false);
        menuitem_T5LodosAireacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitem_T5LodosAireacionActionPerformed(evt);
            }
        });
        menu_T5LodosAirExt.add(menuitem_T5LodosAireacion);

        menu_Tecnologias.add(menu_T5LodosAirExt);
        menu_Tecnologias.add(jSeparator3);

        menu_T6LodosModConv.setText("Tecn. Lodos Activados Modalidad convencional");
        menu_T6LodosModConv.setEnabled(false);

        menuitem_T6LodosConvencional.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_lodos.png"))); // NOI18N
        menuitem_T6LodosConvencional.setText("Lodos Activados Modalidad convencional");
        menuitem_T6LodosConvencional.setToolTipText("");
        menuitem_T6LodosConvencional.setEnabled(false);
        menuitem_T6LodosConvencional.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitem_T6LodosConvencionalActionPerformed(evt);
            }
        });
        menu_T6LodosModConv.add(menuitem_T6LodosConvencional);

        menu_Tecnologias.add(menu_T6LodosModConv);

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

        menuItem_Res_Uasb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_Res_Uasb.png"))); // NOI18N
        menuItem_Res_Uasb.setText("Reactor UASB");
        menuItem_Res_Uasb.setEnabled(false);
        menuItem_Res_Uasb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_Res_UasbActionPerformed(evt);
            }
        });
        menu_Resultados.add(menuItem_Res_Uasb);

        menuItem_Res_LagunaFacultUasb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_Res_lagunas.png"))); // NOI18N
        menuItem_Res_LagunaFacultUasb.setText("+ Laguna Facultativa");
        menuItem_Res_LagunaFacultUasb.setEnabled(false);
        menuItem_Res_LagunaFacultUasb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_Res_LagunaFacultUasbActionPerformed(evt);
            }
        });
        menu_Resultados.add(menuItem_Res_LagunaFacultUasb);

        menuItem_Res_FiltroPerc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_Res_FiltroPercolador.png"))); // NOI18N
        menuItem_Res_FiltroPerc.setText("+ Filtro Percolador");
        menuItem_Res_FiltroPerc.setEnabled(false);
        menuItem_Res_FiltroPerc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_Res_FiltroPercActionPerformed(evt);
            }
        });
        menu_Resultados.add(menuItem_Res_FiltroPerc);

        menuItem_Res_LodosActModConvUasb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_Res_Sedimentador.png"))); // NOI18N
        menuItem_Res_LodosActModConvUasb.setText("+ Lodos Activados Mod. Convencional");
        menuItem_Res_LodosActModConvUasb.setEnabled(false);
        menuItem_Res_LodosActModConvUasb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_Res_LodosActModConvUasbActionPerformed(evt);
            }
        });
        menu_Resultados.add(menuItem_Res_LodosActModConvUasb);
        menu_Resultados.add(jSeparator4);

        menuItem_Res_LodosActAirExt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_Res_Sedimentador.png"))); // NOI18N
        menuItem_Res_LodosActAirExt.setText("Lodos Activados Aireación Ext.");
        menuItem_Res_LodosActAirExt.setEnabled(false);
        menuItem_Res_LodosActAirExt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_Res_LodosActAirExtActionPerformed(evt);
            }
        });
        menu_Resultados.add(menuItem_Res_LodosActAirExt);
        menu_Resultados.add(jSeparator5);

        menuItem_Res_LodosActModConv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SubMenu_Res_Sedimentador.png"))); // NOI18N
        menuItem_Res_LodosActModConv.setText("Lodos Activados Mod. Convencional");
        menuItem_Res_LodosActModConv.setEnabled(false);
        menuItem_Res_LodosActModConv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_Res_LodosActModConvActionPerformed(evt);
            }
        });
        menu_Resultados.add(menuItem_Res_LodosActModConv);

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

        menuItem_Ayuda.setText("Ayuda");
        menuItem_Ayuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_AyudaActionPerformed(evt);
            }
        });
        menu_Acerca.add(menuItem_Ayuda);
        menu_Acerca.add(jSeparator7);

        menuItem_Acercade.setText("Acerca de...");
        menuItem_Acercade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_AcercadeActionPerformed(evt);
            }
        });
        menu_Acerca.add(menuItem_Acercade);

        menubar.add(menu_Acerca);

        setJMenuBar(menubar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jdp_Main, javax.swing.GroupLayout.PREFERRED_SIZE, 1024, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            if (bod.exportarProyecto(false)) {
                comprobarBotones();
                menuitem_DatosEntrada.setEnabled(true);
                util.Mensaje("Puede iniciar el ingreso de información en el menú 'DATOS'", "ok");
                menu_Datos.doClick();
            }
        }
    }//GEN-LAST:event_menuitem_newActionPerformed

    private void menuitem_openActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_openActionPerformed

        if (CerrarVentanas()) {

            if (bod.importarProyecto()) {
                comprobarBotones();
                menuitem_DatosEntrada.setEnabled(true);
                CargarDatosFondo();
                util.Mensaje("Proyecto cargado correctamente!", "ok");
            }
        }
    }//GEN-LAST:event_menuitem_openActionPerformed

    private void menuitem_ProyeccionPoblacionalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_ProyeccionPoblacionalActionPerformed
        tecnologia = "DATENT_PROYECNPOBLAC";
        AbrirProyeccionPoblacional();
    }//GEN-LAST:event_menuitem_ProyeccionPoblacionalActionPerformed

    private void menuitem_DatosEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_DatosEntradaActionPerformed
        tecnologia = "DATENT_DATOSDEENTRADA";
        AbrirDatosEntrada();
    }//GEN-LAST:event_menuitem_DatosEntradaActionPerformed

    private void menuitem_CalculoCaudalesDisenoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_CalculoCaudalesDisenoActionPerformed
        tecnologia = "DATENT_CALCULCAUDISE";
        AbrirCalculoCaudalesDiseno();
    }//GEN-LAST:event_menuitem_CalculoCaudalesDisenoActionPerformed

    private void menuitem_CaractInicAguaResidualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_CaractInicAguaResidualActionPerformed
        tecnologia = "DATENT_CARACAGUAINIC";
        AbrirCaractInicAguaResidual();
    }//GEN-LAST:event_menuitem_CaractInicAguaResidualActionPerformed

    private void menuitem_lagunaerobiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_lagunaerobiaActionPerformed
        tecnologia = "LAGUNANRB_T1";
        AbrirLagunaAnanerobia();
    }//GEN-LAST:event_menuitem_lagunaerobiaActionPerformed

    private void menuitem_lagunafacul1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_lagunafacul1ActionPerformed
        tecnologia = "LAGUNFACU_T1";
        AbrirLagunaFacultativaSecundaria();
    }//GEN-LAST:event_menuitem_lagunafacul1ActionPerformed

    private void menuItem_Res_LagunaAnaerobiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_Res_LagunaAnaerobiaActionPerformed
        AbrirResultado("*LA");//Abrir laguna anaerobia, resultado en imagen
    }//GEN-LAST:event_menuItem_Res_LagunaAnaerobiaActionPerformed

    private void menuItem_Res_LagunaFacultativaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_Res_LagunaFacultativaActionPerformed
        AbrirResultado("*LF");//Abrir laguna facultativa, resultado en imagen
    }//GEN-LAST:event_menuItem_Res_LagunaFacultativaActionPerformed

    private void menuitem_rejillasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_rejillasActionPerformed
        tecnologia = "TPREJILLA_T0";
        AbrirRejillas();
    }//GEN-LAST:event_menuitem_rejillasActionPerformed

    private void menuitem_desarenadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_desarenadorActionPerformed
        tecnologia = "TPDESAREN_T0";
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

    private void menuItem_AcercadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_AcercadeActionPerformed
        AbrirBienvenido();
    }//GEN-LAST:event_menuItem_AcercadeActionPerformed

    private void menuitem_reactoruasbT2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_reactoruasbT2ActionPerformed
        tecnologia = "REACTUASB_T2";
        AbrirUasb();
    }//GEN-LAST:event_menuitem_reactoruasbT2ActionPerformed

    private void menuitem_lagunafaculT2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_lagunafaculT2ActionPerformed
        tecnologia = "LAGUNFACU_T2";
        AbrirLagunaFacultativaSecundariaUasb();
    }//GEN-LAST:event_menuitem_lagunafaculT2ActionPerformed

    private void menuitem_reactoruasbT3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_reactoruasbT3ActionPerformed
        tecnologia = "REACTUASB_T3";
        AbrirUasb();
    }//GEN-LAST:event_menuitem_reactoruasbT3ActionPerformed

    private void menuitem_filtropercoT3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_filtropercoT3ActionPerformed
        tecnologia = "FILTPERCL_T3";
        AbrirFiltro();
    }//GEN-LAST:event_menuitem_filtropercoT3ActionPerformed

    private void menuitem_reactoruasbT4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_reactoruasbT4ActionPerformed
        tecnologia = "REACTUASB_T4";
        AbrirUasb();
    }//GEN-LAST:event_menuitem_reactoruasbT4ActionPerformed

    private void menuitem_lodsactivmodconvT4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_lodsactivmodconvT4ActionPerformed
        tecnologia = "LODACTCNV_T4";
        AbrirLodosActivadosConvencionalUASB();
    }//GEN-LAST:event_menuitem_lodsactivmodconvT4ActionPerformed

    private void menuitem_T5LodosAireacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_T5LodosAireacionActionPerformed
        tecnologia = "LODACTAEX_T5";
        AbrirLodosActivadosAireacionExtendida();
    }//GEN-LAST:event_menuitem_T5LodosAireacionActionPerformed

    private void menuitem_T6LodosConvencionalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_T6LodosConvencionalActionPerformed
        tecnologia = "LODACTCNV_T6";
        AbrirLodosActivadosConvencional();
    }//GEN-LAST:event_menuitem_T6LodosConvencionalActionPerformed

    private void menuItem_Res_UasbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_Res_UasbActionPerformed
        AbrirResultado("*RU");
    }//GEN-LAST:event_menuItem_Res_UasbActionPerformed

    private void menuItem_Res_LagunaFacultUasbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_Res_LagunaFacultUasbActionPerformed
        AbrirResultado("*UF");//Abrir laguna facultativa, resultado en imagen
    }//GEN-LAST:event_menuItem_Res_LagunaFacultUasbActionPerformed

    private void menuItem_Res_FiltroPercActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_Res_FiltroPercActionPerformed
        AbrirResultado("*FP");
    }//GEN-LAST:event_menuItem_Res_FiltroPercActionPerformed

    private void menuItem_Res_LodosActModConvUasbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_Res_LodosActModConvUasbActionPerformed
        AbrirResultado("*UL");
    }//GEN-LAST:event_menuItem_Res_LodosActModConvUasbActionPerformed

    private void menuItem_Res_LodosActAirExtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_Res_LodosActAirExtActionPerformed
        AbrirResultado("*LE");
    }//GEN-LAST:event_menuItem_Res_LodosActAirExtActionPerformed

    private void menuItem_Res_LodosActModConvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_Res_LodosActModConvActionPerformed
        AbrirResultado("*LC");
    }//GEN-LAST:event_menuItem_Res_LodosActModConvActionPerformed

    private void menuitem_exportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_exportActionPerformed
        bod.exportarProyecto(false);
    }//GEN-LAST:event_menuitem_exportActionPerformed

    private void menuItem_AyudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_AyudaActionPerformed
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File(Generalpath + conf.separador + "Resources" + conf.separador + "Ayuda.pdf");
                Desktop.getDesktop().open(myFile);

            } catch (Exception ex) {
                util.Mensaje("No se pudo abrir el archivo de ayuda en la carpeta 'Resources'", "ok");
            }
        } else {
            util.Mensaje("El sistema no soporta esta función en la carpeta 'Resources'", "ok");
        }
    }//GEN-LAST:event_menuItem_AyudaActionPerformed

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
            java.util.logging.Logger.getLogger(Main.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JMenu jMenu_separador1;
    private javax.swing.JMenu jMenu_separador2;
    private javax.swing.JMenu jMenu_separador3;
    private javax.swing.JMenu jMenu_separador4;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JDesktopPane jdp_Main;
    private javax.swing.JLabel lbl_datosfondo1;
    private javax.swing.JLabel lbl_datosfondo2;
    private javax.swing.JMenuItem menuItem_Acercade;
    private javax.swing.JMenuItem menuItem_Ayuda;
    private javax.swing.JMenuItem menuItem_Res_Desarenador;
    private javax.swing.JMenuItem menuItem_Res_FiltroPerc;
    private javax.swing.JMenuItem menuItem_Res_LagunaAnaerobia;
    private javax.swing.JMenuItem menuItem_Res_LagunaFacultUasb;
    private javax.swing.JMenuItem menuItem_Res_LagunaFacultativa;
    private javax.swing.JMenuItem menuItem_Res_LodosActAirExt;
    private javax.swing.JMenuItem menuItem_Res_LodosActModConv;
    private javax.swing.JMenuItem menuItem_Res_LodosActModConvUasb;
    private javax.swing.JMenuItem menuItem_Res_Parshall;
    private javax.swing.JMenuItem menuItem_Res_Rejillas;
    private javax.swing.JMenuItem menuItem_Res_Uasb;
    private javax.swing.JMenu menu_Acerca;
    private javax.swing.JMenu menu_Archivo;
    private javax.swing.JMenu menu_Datos;
    private javax.swing.JMenu menu_Resultados;
    private javax.swing.JMenu menu_T1Lagunas;
    private javax.swing.JMenu menu_T2UasbLaguna;
    private javax.swing.JMenu menu_T3UasbFiltro;
    private javax.swing.JMenu menu_T4UasbLodos;
    private javax.swing.JMenu menu_T5LodosAirExt;
    private javax.swing.JMenu menu_T6LodosModConv;
    private javax.swing.JMenu menu_Tecnologia0;
    private javax.swing.JMenu menu_Tecnologias;
    private javax.swing.JMenuBar menubar;
    private javax.swing.JMenuItem menuitem_CalculoCaudalesDiseno;
    private javax.swing.JMenuItem menuitem_CaractInicAguaResidual;
    private javax.swing.JMenuItem menuitem_DatosEntrada;
    private javax.swing.JMenuItem menuitem_ProyeccionPoblacional;
    private javax.swing.JMenuItem menuitem_T5LodosAireacion;
    private javax.swing.JMenuItem menuitem_T6LodosConvencional;
    private javax.swing.JMenuItem menuitem_desarenador;
    private javax.swing.JMenuItem menuitem_exit;
    private javax.swing.JMenuItem menuitem_export;
    private javax.swing.JMenuItem menuitem_filtropercoT3;
    private javax.swing.JMenuItem menuitem_lagunaerobia;
    private javax.swing.JMenuItem menuitem_lagunafacul1;
    private javax.swing.JMenuItem menuitem_lagunafaculT2;
    private javax.swing.JMenuItem menuitem_lodsactivmodconvT4;
    private javax.swing.JMenuItem menuitem_new;
    private javax.swing.JMenuItem menuitem_open;
    private javax.swing.JMenuItem menuitem_reactoruasbT2;
    private javax.swing.JMenuItem menuitem_reactoruasbT3;
    private javax.swing.JMenuItem menuitem_reactoruasbT4;
    private javax.swing.JMenuItem menuitem_rejillas;
    private javax.swing.JPopupMenu.Separator sep1;
    private javax.swing.JPopupMenu.Separator sep2;
    private javax.swing.JPopupMenu.Separator sep_1;
    private javax.swing.JPopupMenu.Separator sep_2;
    private javax.swing.JPopupMenu.Separator sep_3;
    // End of variables declaration//GEN-END:variables
}
