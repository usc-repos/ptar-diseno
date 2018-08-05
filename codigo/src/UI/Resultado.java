package UI;

import BO.Bod;
import DB.Dao;
import java.io.File;
import Componentes.Util;
import org.apache.log4j.Logger;
import Componentes.Configuracion;
import Componentes.Graficar;
import Componentes.Listener_Popup;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.ResultSet;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class Resultado extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("Resultado"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    private Listener_Popup Lpopup;
    private Graficar graficar;
    private Bod bod = new Bod("");
    Util util = new Util();
    private String tecnologia;//Recibe que tecnología se debe cargar

    public Resultado(Bod bod, String tec) {
        this.bod = bod;
        initComponents();
        bod.WinResultado = true;
        this.tecnologia = tec;
        InicializarComponentes();
    }

    /**
     * Inicializa todos los elementos (jlabels,jtextbox,etc.) con sus
     * respectivos valores y textos traidos desde al base de datos.
     */
    public void InicializarComponentes() {

        try {

            btn_Guardar.setText("Exportar");
            btn_close.setText("Cerrar");

            switch (tecnologia) {

                case "*PR": //Rejillas  
                    ImagenRejillas();
                    break;

                case "*PD": //Desarenador 
                    ImagenDesarenador();
                    break;

                case "*PP": //Medidor Parshall
                    ImagenMedidorParshall();
                    break;

                case "*LA": //Laguna Anaerobia
                    ImagenLagunaAnaerobia();
                    break;

                case "*LF": //Laguna Facultativa
                    ImagenLagunaFacultativa();
                    break;

                case "*RU": //Reactor UASB 
                    ImagenReactorUasb();
                    break;

                case "*UF": //Laguna Facultativa de UASB 
                    ImagenLagunaFacultativaUasb();
                    break;

                case "*FP": //Filtro Percolador 
                    ImagenFiltroPercolador();
                    break;

                case "*UL": //Lodos Activados modalidad Convencional de UASB                    
                    ImagenLodosActModConvUasb();
                    break;

                case "*LE": //Tecnología Lodos Activados Aireacion Extendida
                    ImagenLodosActAireacionExtend();
                    break;

                case "*LC": //Lodos Activados Modalidad Convencional
                    ImagenLodosActModConv();
                    break;
            }

        } catch (Exception ex) {
            log.error("Error en InicializarComponentes " + ex.getMessage());
            ErrorGeneral(); // Ha ocurrido algun error en los datos o conexión, se debe revisar log.
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }

    private void ImagenRejillas() {
        ResultSet rs;
        try {
            String rutaimagen;

            rs = db.ResultadoSelect("obtenerpreguntesp", "*PR", null);

            lbl_Resultado_titulo1.setText(rs.getString("titulo1"));
            lbl_Resultado_titulo2.setText(rs.getString("titulo2"));
            AsignarPopupBtn(btn_Resultado_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 150);

            rutaimagen = conf.ObtenerRutaGeneral("rutaimagenes") + conf.separador + conf.ObtenerKey("imgtratprelreji");
            String[][] data = new String[14][4];

            rs = db.ResultadoSelect("datospregunta", "RLC");//Longitud del canal (L) 
            data[0][0] = "T-M"; //T = mostrar en tabla, M = mostrar en la figura
            data[0][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");//Título; medida
            data[0][2] = bod.getVarFormateadaPorNombre("RLC", rs.getInt("decimales"));//Valor 
            data[0][3] = "418;526";//coordenada Ancho;Alto - si hay mas de un ';' = se necesita replicar la medida en otras coordenadas

            rs = db.ResultadoSelect("datospregunta", "RPS");//Pérdida de carga en la rejilla 50% sucia (hL)
            data[1][0] = "T-M";
            data[1][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[1][2] = bod.getVarFormateadaPorNombre("RPS", rs.getInt("decimales"));
            data[1][3] = "942;762";

            rs = db.ResultadoSelect("datospregunta", "RIB");// Inclinación de las barras (θ) 
            data[2][0] = "T+M";
            data[2][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[2][2] = bod.getVarFormateadaPorNombre("RIB", rs.getInt("decimales")) + rs.getString("titulo2");
            data[2][3] = "768;860";

            rs = db.ResultadoSelect("datospregunta", "RAR");//Ancho de rejillas (b)
            data[3][0] = "T+M";
            data[3][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[3][2] = bod.getVarFormateadaPorNombre("RAR", rs.getInt("decimales"));
            data[3][3] = "1317;283";

            data[4][0] = "T+M";//Hmax
            data[4][1] = "Hmax;m";
            data[4][2] = bod.getVarFormateada(bod.calcularRHM(), 2);
            data[4][3] = "600;826";

            rs = db.ResultadoSelect("datospregunta", "RAR");//medida x
            data[5][0] = " M";
            data[5][1] = ";m";
            data[5][2] = bod.getVarFormateada(bod.calcularRXM(), 2);
            data[5][3] = "768;526";

            //Dato por defecto  
            data[6][0] = "M";
            data[6][1] = ";m";
            data[6][2] = "0.1 m";
            data[6][3] = "1000;234";

            //Dato por defecto  
            data[7][0] = "M";
            data[7][1] = ";m";
            data[7][2] = "0.40 m";
            data[7][3] = "810;94";

            //Dato por defecto
            data[8][0] = "M";
            data[8][1] = ";m";
            data[8][2] = "Bypass";
            data[8][3] = "650;94";

            //Dato por defecto  
            data[9][0] = "M";
            data[9][1] = ";m";
            data[9][2] = "0.1 m";
            data[9][3] = "1300;88";

            //Dato por defecto  
            data[10][0] = "M";
            data[10][1] = ";m";
            data[10][2] = "0.3 m";
            data[10][3] = "1134;273";

            //Dato por defecto  
            data[11][0] = "M";
            data[11][1] = ";m";
            data[11][2] = "0.5 m";
            data[11][3] = "547;654";

            //Dato por defecto  
            data[12][0] = "M";
            data[12][1] = ";m";
            data[12][2] = "0.8 m";
            data[12][3] = "1000;526";

            //Regla para NB < 6 Se debe advertir al usuario.
            String msg ="; ";
            if (bod.getVarInt("RNB") <=2) { 
               msg ="<font color=red>Número de barras (Nb) ≤ 2 barras. Se recomienda verificar los valores de 'h' y 'a', o considerar otras opciones de sistemas compactos de tratamiento preliminar</font>; ";
            }
            data[13][0] = "T";
            data[13][1] = msg;
            data[13][2] = "";
            data[13][3] = "0;0";

            graficar = new Graficar(rutaimagen, 650, 868);
            lblImagen.setIcon(graficar.procesarImagen(data));
            lblTabla.setText(graficar.tabla);

            lblDerechos.setText(" © PTAR Diseño");
            String u = rutaimagen.replace(".png", "_color.png");
            BufferedImage imgu = ImageIO.read(new File(u));
            ImageIcon iconLogou = new ImageIcon(graficar.escalarImagen(imgu, 100, 82)); //Esta relacion de aspecto se debe tomar en un programa de diseño o calcularse
            lblMiniatura.setIcon(iconLogou);

        } catch (Exception ex) {
            log.error("Error en InicializarComponentes " + ex.getMessage());
            ErrorGeneral(); // Ha ocurrido algun error en los datos o conexión, se debe revisar log.
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }

    private void ImagenDesarenador() {
        ResultSet rs;
        try {
            String rutaimagen;

            rs = db.ResultadoSelect("obtenerpreguntesp", "*PD", null);

            lbl_Resultado_titulo1.setText(rs.getString("titulo1"));
            lbl_Resultado_titulo2.setText(rs.getString("titulo2"));
            AsignarPopupBtn(btn_Resultado_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 150);

            rutaimagen = conf.ObtenerRutaGeneral("rutaimagenes") + conf.separador + conf.ObtenerKey("imgtratpreldesa");
            String[][] data = new String[7][4];

            rs = db.ResultadoSelect("datospregunta", "DAH");//Altura máxima de lámina de agua en el desarenador (H)
            data[0][0] = "T-M"; //T = mostrar en tabla, M = mostrar en la figura
            data[0][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");//Título; medida
            data[0][2] = bod.getVarFormateadaPorNombre("DAH", rs.getInt("decimales"));//Valor 
            data[0][3] = "570;690";//coordenada Ancho;Alto - si hay mas de un ';' = se necesita replicar la medida en otras coordenadas

            rs = db.ResultadoSelect("datospregunta", "DAB");//Ancho del desarenador (b)
            data[1][0] = "T-M";
            data[1][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[1][2] = bod.getVarFormateadaPorNombre("DAB", rs.getInt("decimales"));
            data[1][3] = "568;328";

            rs = db.ResultadoSelect("datospregunta", "DLD");//Longitud del desarenador (L)
            data[2][0] = "T+M";
            data[2][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[2][2] = bod.getVarFormateadaPorNombre("DLD", rs.getInt("decimales"));
            data[2][3] = "690;480";

            rs = db.ResultadoSelect("datospregunta", "DUP");//Profundidad útil depósito inferior de arena (p)
            data[3][0] = "T+M";
            data[3][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[3][2] = bod.getVarFormateadaPorNombre("DUP", rs.getInt("decimales"));
            data[3][3] = "330;755";

            //Dato por defecto
            data[4][0] = "M";
            data[4][1] = ";m";
            data[4][2] = "0.5 m";
            data[4][3] = "480;600";

            //Dato por defecto
            data[5][0] = "M";
            data[5][1] = ";m";
            data[5][2] = "0.1 m";
            data[5][3] = "780;100";

            //Dato por defecto
            data[6][0] = "M";
            data[6][1] = ";m";
            data[6][2] = "0.15 m";
            data[6][3] = "780;222";

            graficar = new Graficar(rutaimagen, 650, 868);
            lblImagen.setIcon(graficar.procesarImagen(data));
            lblTabla.setText(graficar.tabla);

            lblDerechos.setText(" © PTAR Diseño");
            String u = rutaimagen.replace(".png", "_color.png");
            BufferedImage imgu = ImageIO.read(new File(u));
            ImageIcon iconLogou = new ImageIcon(graficar.escalarImagen(imgu, 100, 82)); //Esta relacion de aspecto se debe tomar en un programa de diseño o calcularse
            lblMiniatura.setIcon(iconLogou);

        } catch (Exception ex) {
            log.error("Error en InicializarComponentes " + ex.getMessage());
            ErrorGeneral(); // Ha ocurrido algun error en los datos o conexión, se debe revisar log.
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }

    private void ImagenMedidorParshall() {
        ResultSet rs;
        try {
            String rutaimagen;

            rs = db.ResultadoSelect("obtenerpreguntesp", "*PP", null);

            lbl_Resultado_titulo1.setText(rs.getString("titulo1"));
            lbl_Resultado_titulo2.setText(rs.getString("titulo2"));
            AsignarPopupBtn(btn_Resultado_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 150);

            rutaimagen = conf.ObtenerRutaGeneral("rutaimagenes") + conf.separador + conf.ObtenerKey("imgtratprelmepa");
            String[][] data = new String[13][4];

            rs = db.ResultadoSelect("datospregunta", "DPA");//Dimensión A
            data[0][0] = "T-M"; //T = mostrar en tabla, M = mostrar en la figura
            data[0][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");//Título; medida
            data[0][2] = bod.getVarFormateadaPorNombre("DPA", rs.getInt("decimales"));//Valor 
            data[0][3] = "430;510";//coordenada Ancho;Alto - si hay mas de un ';' = se necesita replicar la medida en otras coordenadas

            rs = db.ResultadoSelect("datospregunta", "DPB");//Dimensión B
            data[1][0] = "T-M";
            data[1][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[1][2] = bod.getVarFormateadaPorNombre("DPB", rs.getInt("decimales"));
            data[1][3] = "430;600";

            rs = db.ResultadoSelect("datospregunta", "DPC");//Dimensión C
            data[2][0] = "T+M";
            data[2][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[2][2] = bod.getVarFormateadaPorNombre("DPC", rs.getInt("decimales"));
            data[2][3] = "1200;310";

            rs = db.ResultadoSelect("datospregunta", "DPD");//Dimensión D
            data[3][0] = "T+M";
            data[3][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[3][2] = bod.getVarFormateadaPorNombre("DPD", rs.getInt("decimales"));
            data[3][3] = "140;310";

            rs = db.ResultadoSelect("datospregunta", "DPE"); //Dimensión E //todo: donde se ubica?
            data[4][0] = "T+";
            data[4][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[4][2] = bod.getVarFormateadaPorNombre("DPE", rs.getInt("decimales"));
            data[4][3] = "130;310";

            rs = db.ResultadoSelect("datospregunta", "DPF"); //Dimensión F
            data[5][0] = "T+M";
            data[5][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[5][2] = bod.getVarFormateadaPorNombre("DPF", rs.getInt("decimales"));
            data[5][3] = "750;600";

            rs = db.ResultadoSelect("datospregunta", "DPG"); //Dimensión G
            data[6][0] = "T+M";
            data[6][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[6][2] = bod.getVarFormateadaPorNombre("DPG", rs.getInt("decimales"));
            data[6][3] = "960;600";

            rs = db.ResultadoSelect("datospregunta", "DPK"); //Dimensión K
            data[7][0] = "T+M";
            data[7][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[7][2] = bod.getVarFormateadaPorNombre("DPK", rs.getInt("decimales"));
            data[7][3] = "1170;880";

            rs = db.ResultadoSelect("datospregunta", "DPN"); //Dimensión N
            data[8][0] = "T+M";
            data[8][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[8][2] = bod.getVarFormateadaPorNombre("DPN", rs.getInt("decimales"));
            data[8][3] = "540;920";

            rs = db.ResultadoSelect("datospregunta", "DAN"); //Ancho nominal (W) 
            data[9][0] = "T+M";
            data[9][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[9][2] = bod.getVarFormateadaPorNombre("DAN", rs.getInt("decimales"));
            data[9][3] = "1020;350";

            rs = db.ResultadoSelect("datospregunta", "DP1"); //Hmax  
            data[10][0] = "T+M";
            data[10][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[10][2] = bod.getVarFormateadaPorNombre("DP1", rs.getInt("decimales"));
            data[10][3] = "500;800";

            //Dato complementario
            data[11][0] = "M";
            data[11][1] = ";m";
            data[11][2] = bod.getVarFormateada(bod.calcularDPApozo(), 2);
            data[11][3] = "430;300";

            //Dato por defecto
            data[12][0] = "M";
            data[12][1] = ";m";
            data[12][2] = "0.5 m";
            data[12][3] = "400;680";

            graficar = new Graficar(rutaimagen, 650, 868);
            lblImagen.setIcon(graficar.procesarImagen(data));
            lblTabla.setText(graficar.tabla);

            lblDerechos.setText(" © PTAR Diseño");
            String u = rutaimagen.replace(".png", "_color.png");
            BufferedImage imgu = ImageIO.read(new File(u));
            ImageIcon iconLogou = new ImageIcon(graficar.escalarImagen(imgu, 100, 82)); //Esta relacion de aspecto se debe tomar en un programa de diseño o calcularse
            lblMiniatura.setIcon(iconLogou);

        } catch (Exception ex) {
            log.error("Error en InicializarComponentes " + ex.getMessage());
            ErrorGeneral(); // Ha ocurrido algun error en los datos o conexión, se debe revisar log.
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }

    private void ImagenLagunaAnaerobia() {

        ResultSet rs;
        try {
            String rutaimagen;

            rs = db.ResultadoSelect("obtenerpreguntesp", "*LA", null);

            lbl_Resultado_titulo1.setText(rs.getString("titulo1"));
            lbl_Resultado_titulo2.setText(rs.getString("titulo2"));
            AsignarPopupBtn(btn_Resultado_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 150);

            String[][] data = new String[10][4];

            if (bod.getVarInt("LLA") > 0) { //Carga la imagen de relación 1:3 
                rutaimagen = conf.ObtenerRutaGeneral("rutaimagenes") + conf.separador + conf.ObtenerKey("imglanaerobia13");

                rs = db.ResultadoSelect("datospregunta", "LAA");//Ancho
                data[0][0] = "T-M"; //T = mostrar en tabla, M = mostrar en la figura
                data[0][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");//Título; medida
                data[0][2] = bod.getVarFormateadaPorNombre("LAA", rs.getInt("decimales"));//Valor 
                data[0][3] = "715;720";//coordenada Ancho;Alto - si hay mas de un ';' = se necesita replicar la medida en otras coordenadas

                rs = db.ResultadoSelect("datospregunta", "LAL");//Largo 
                data[1][0] = "T-M";
                data[1][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
                data[1][2] = bod.getVarFormateadaPorNombre("LAL", rs.getInt("decimales"));
                data[1][3] = "130;287";

                //Lado Y
                data[2][0] = "M";
                data[2][1] = ";m";
                data[2][2] = bod.getVarFormateada(bod.calcularLYX(bod.getVarDob("LAP"), bod.getVarDob("LAU"), bod.getVarDob("LAB")), 2);// lap,  lau,  lab
                data[2][3] = "1200;615";

                rs = db.ResultadoSelect("datospregunta", "LAI");//Ángulo de inclinación de la pendiente
                data[3][0] = "T+M";
                data[3][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
                data[3][2] = bod.getVarFormateadaPorNombre("LAI", rs.getInt("decimales")) + rs.getString("titulo2");
                data[3][3] = "386;834";

                rs = db.ResultadoSelect("datospregunta", "LAU");
                data[4][0] = "T+M";
                data[4][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
                data[4][2] = bod.getVarFormateadaPorNombre("LAU", rs.getInt("decimales"));
                data[4][3] = "730;828";

                rs = db.ResultadoSelect("datospregunta", "LAB");//Borde libre
                data[5][0] = "T+M";
                data[5][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
                data[5][2] = bod.getVarFormateadaPorNombre("LAB", rs.getInt("decimales"));
                data[5][3] = "1200;790";

                //Largo del fondo
                data[6][0] = " M";
                data[6][1] = ";m";
                data[6][2] = bod.getVarFormateada(bod.calcularLLF(bod.getVarDob("LAL"), bod.getVarDob("LAP"), bod.getVarDob("LAU")), 2);//laa,  lap, lau
                data[6][3] = "390;280";

                //Ancho del fondo
                data[7][0] = " M";
                data[7][1] = ";m";
                data[7][2] = bod.getVarFormateada(bod.calcularLAF(bod.getVarDob("LAA"), bod.getVarDob("LAP"), bod.getVarDob("LAU")), 2);//laa,  lap, lau
                data[7][3] = "715;756";

                //Largo Total
                data[8][0] = " M";
                data[8][1] = ";m";
                data[8][2] = bod.getVarFormateada(bod.calcularLLT(bod.getVarDob("LAL"), bod.getVarDob("LAP"), bod.getVarDob("LAU"), bod.getVarDob("LAB")), 2);//lal, lap, lau, lab
                data[8][3] = "1313;274";

                //Ancho Total
                data[9][0] = " M";
                data[9][1] = ";m";
                data[9][2] = bod.getVarFormateada(bod.calcularLAT(bod.getVarDob("LAA"), bod.getVarDob("LAP"), bod.getVarDob("LAU"), bod.getVarDob("LAB")), 2);//laa, lap, lau, lab
                data[9][3] = "715;625";

            } else { //Carga la imagen de relación 2:3

                rutaimagen = conf.ObtenerRutaGeneral("rutaimagenes") + conf.separador + conf.ObtenerKey("imglanaerobia23");

                rs = db.ResultadoSelect("datospregunta", "LAA");//Ancho
                data[0][0] = "T-M"; //T = mostrar en tabla, M = mostrar en la figura
                data[0][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");//Título; medida
                data[0][2] = bod.getVarFormateadaPorNombre("LAA", rs.getInt("decimales"));//Valor 
                data[0][3] = "1247;322";//coordenada Ancho;Alto - si hay mas de un ';' = se necesita replicar la medida en otras coordenadas

                rs = db.ResultadoSelect("datospregunta", "LAL");//Largo 
                data[1][0] = "T-M";
                data[1][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
                data[1][2] = bod.getVarFormateadaPorNombre("LAL", rs.getInt("decimales"));
                data[1][3] = "690;802";

                //Lado Y
                data[2][0] = "M";
                data[2][1] = ";m";
                data[2][2] = bod.getVarFormateada(bod.calcularLYX(bod.getVarDob("LAP"), bod.getVarDob("LAU"), bod.getVarDob("LAB")), 2);// lap,  lau,  lab
                data[2][3] = "1038;380";

                rs = db.ResultadoSelect("datospregunta", "LAI");//Ángulo de inclinación de la pendiente
                data[3][0] = "T+M";
                data[3][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
                data[3][2] = bod.getVarFormateadaPorNombre("LAI", rs.getInt("decimales")) + rs.getString("titulo2");
                data[3][3] = "510;838";

                rs = db.ResultadoSelect("datospregunta", "LAU");//h
                data[4][0] = "T+M";
                data[4][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
                data[4][2] = bod.getVarFormateadaPorNombre("LAU", rs.getInt("decimales"));
                data[4][3] = "710;843";

                rs = db.ResultadoSelect("datospregunta", "LAB");//Borde libre
                data[5][0] = "T+M";
                data[5][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
                data[5][2] = bod.getVarFormateadaPorNombre("LAB", rs.getInt("decimales"));
                data[5][3] = "1092;800";

                //Largo del fondo
                data[6][0] = " M";
                data[6][1] = ";m";
                data[6][2] = bod.getVarFormateada(bod.calcularLLF(bod.getVarDob("LAL"), bod.getVarDob("LAP"), bod.getVarDob("LAU")), 2);//laa,  lap, lau
                data[6][3] = "532;313";

                //Ancho del fondo
                data[7][0] = " M";
                data[7][1] = ";m";
                data[7][2] = bod.getVarFormateada(bod.calcularLAF(bod.getVarDob("LAA"), bod.getVarDob("LAP"), bod.getVarDob("LAU")), 2);//laa,  lap, lau
                data[7][3] = "690;753";

                //Largo Total
                data[8][0] = " M";
                data[8][1] = ";m";
                data[8][2] = bod.getVarFormateada(bod.calcularLLT(bod.getVarDob("LAL"), bod.getVarDob("LAP"), bod.getVarDob("LAU"), bod.getVarDob("LAB")), 2);//lal, lap, lau, lab
                data[8][3] = "190;322";

                //Ancho Total
                data[9][0] = " M";
                data[9][1] = ";m";
                data[9][2] = bod.getVarFormateada(bod.calcularLAT(bod.getVarDob("LAA"), bod.getVarDob("LAP"), bod.getVarDob("LAU"), bod.getVarDob("LAB")), 2);//laa, lap, lau, lab
                data[9][3] = "690;660";
            }

            graficar = new Graficar(rutaimagen, 650, 868);
            lblImagen.setIcon(graficar.procesarImagen(data));
            lblTabla.setText(graficar.tabla);

            lblDerechos.setText(" © PTAR Diseño");
            String u = rutaimagen.replace(".png", "_color.png");
            BufferedImage imgu = ImageIO.read(new File(u));
            ImageIcon iconLogou = new ImageIcon(graficar.escalarImagen(imgu, 100, 82)); //Esta relacion de aspecto se debe tomar en un programa de diseño o calcularse
            lblMiniatura.setIcon(iconLogou);

        } catch (Exception ex) {
            log.error("Error en InicializarComponentes " + ex.getMessage());
            ErrorGeneral(); // Ha ocurrido algun error en los datos o conexión, se debe revisar log.
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }

    private void ImagenLagunaFacultativa() {

        ResultSet rs;
        try {
            String rutaimagen;

            rs = db.ResultadoSelect("obtenerpreguntesp", "*LF", null);

            lbl_Resultado_titulo1.setText(rs.getString("titulo1"));
            lbl_Resultado_titulo2.setText(rs.getString("titulo2"));
            AsignarPopupBtn(btn_Resultado_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 150);

            rutaimagen = conf.ObtenerRutaGeneral("rutaimagenes") + conf.separador + conf.ObtenerKey("imglanaerobia13");//Facultativa es 1:3  
            String[][] data = new String[11][4];

            rs = db.ResultadoSelect("datospregunta", "FSL");//Largo
            data[0][0] = "T-M"; //T = mostrar en tabla, M = mostrar en la figura
            data[0][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");//Título; medida
            data[0][2] = bod.getVarFormateadaPorNombre("FSL", rs.getInt("decimales"));//Valor 
            data[0][3] = "715;720";//coordenada Ancho;Alto - si hay mas de un ';' = se necesita replicar la medida en otras coordenadas

            rs = db.ResultadoSelect("datospregunta", "FSA");//Ancho
            //borrar double fsa = Double.parseDouble(bod.getVarFormateadaPorNombre("FSA", rs.getInt("decimales")));
            data[1][0] = "T-M";
            data[1][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[1][2] = bod.getVarFormateadaPorNombre("FSA", rs.getInt("decimales"));
            data[1][3] = "130;287";

            //Lado Y
            data[2][0] = "M";
            data[2][1] = ";m";
            data[2][2] = bod.getVarFormateada(bod.calcularLYX(bod.getVarDob("FAP"), bod.getVarDob("FCA"), bod.getVarDob("FAB")), 2);// lap,  lau,  lab
            data[2][3] = "1200;615";

            rs = db.ResultadoSelect("datospregunta", "FAI");//Ángulo de inclinación de la pendiente
            data[3][0] = "T+M";
            data[3][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[3][2] = bod.getVarFormateadaPorNombre("FAI", rs.getInt("decimales")) + rs.getString("titulo2");
            data[3][3] = "386;834";

            rs = db.ResultadoSelect("datospregunta", "FCA");
            data[4][0] = "T+M";
            data[4][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[4][2] = bod.getVarFormateadaPorNombre("FCA", rs.getInt("decimales"));
            data[4][3] = "730;828";

            rs = db.ResultadoSelect("datospregunta", "FAB");//Borde libre
            data[5][0] = "T+M";
            data[5][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[5][2] = bod.getVarFormateadaPorNombre("FAB", rs.getInt("decimales"));
            data[5][3] = "1200;790";

            //Largo del fondo
            data[6][0] = " M";
            data[6][1] = ";m";
            data[6][2] = bod.getVarFormateada(bod.calcularLLF(bod.getVarDob("FSL"), bod.getVarDob("FAP"), bod.getVarDob("FCA")), 2);//laa,  lap, lau
            data[6][3] = "715;756";

            //Ancho del fondo
            data[7][0] = " M";
            data[7][1] = ";m";
            data[7][2] = bod.getVarFormateada(bod.calcularLAF(bod.getVarDob("FSA"), bod.getVarDob("FAP"), bod.getVarDob("FCA")), 2);//laa,  lap, lau
            data[7][3] = "390;280";

            //Largo Total
            data[8][0] = " M";
            data[8][1] = ";m";
            data[8][2] = bod.getVarFormateada(bod.calcularLLT(bod.getVarDob("FSL"), bod.getVarDob("FAP"), bod.getVarDob("FCA"), bod.getVarDob("FAB")), 2);//lal, lap, lau, lab
            data[8][3] = "715;625";

            //Ancho Total
            data[9][0] = " M";
            data[9][1] = ";m";
            data[9][2] = bod.getVarFormateada(bod.calcularLAT(bod.getVarDob("FSA"), bod.getVarDob("FAP"), bod.getVarDob("FCA"), bod.getVarDob("FAB")), 2);//laa, lap, lau, lab
            data[9][3] = "1313;274";

            rs = db.ResultadoSelect("datospregunta", "FLA");//Área total de las lagunas
            data[10][0] = "T";
            data[10][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[10][2] = bod.getVarFormateadaPorNombre("FSA", rs.getInt("decimales"));
            data[10][3] = "000;000";

            graficar = new Graficar(rutaimagen, 650, 868);
            lblImagen.setIcon(graficar.procesarImagen(data));
            lblTabla.setText(graficar.tabla);

            lblDerechos.setText(" © PTAR Diseño");
            String u = rutaimagen.replace(".png", "_color.png");
            BufferedImage imgu = ImageIO.read(new File(u));
            ImageIcon iconLogou = new ImageIcon(graficar.escalarImagen(imgu, 100, 82)); //Esta relacion de aspecto se debe tomar en un programa de diseño o calcularse
            lblMiniatura.setIcon(iconLogou);

        } catch (Exception ex) {
            log.error("Error en InicializarComponentes " + ex.getMessage());
            ErrorGeneral(); // Ha ocurrido algun error en los datos o conexión, se debe revisar log.
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }

    private void ImagenReactorUasb() {
        ResultSet rs;
        try {
            String rutaimagen;

            rs = db.ResultadoSelect("obtenerpreguntesp", "*RU", null);

            lbl_Resultado_titulo1.setText(rs.getString("titulo1"));
            lbl_Resultado_titulo2.setText(rs.getString("titulo2"));
            AsignarPopupBtn(btn_Resultado_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 150);

            rutaimagen = conf.ObtenerRutaGeneral("rutaimagenes") + conf.separador + conf.ObtenerKey("imgreactoruasb");//Todo: ruta iamgenes en bod  
            String[][] data = new String[10][4];//10 Filas x 4 Cols

            rs = db.ResultadoSelect("datospregunta", "UHS");
            data[0][0] = "T-M"; //T = mostrar en tabla, M = mostrar en la figura
            data[0][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");//Título; medida
            data[0][2] = bod.getVarFormateadaPorNombre("URH", rs.getInt("decimales"));//Valor 
            data[0][3] = "345;590;1028;700";//coordenada Ancho;Alto - si hay mas de un ';' = se necesita replicar la medida en otras coordenadas

            rs = db.ResultadoSelect("datospregunta", "UHD");
            data[1][0] = "T-M";
            data[1][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[1][2] = bod.getVarFormateadaPorNombre("UHD", rs.getInt("decimales"));
            data[1][3] = "345;730";

            rs = db.ResultadoSelect("datospregunta", "UAA");
            data[2][0] = "T+M";
            data[2][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[2][2] = bod.getVarFormateadaPorNombre("UAA", rs.getInt("decimales"));
            data[2][3] = "645;50";

            rs = db.ResultadoSelect("datospregunta", "UAL");
            data[3][0] = "T+M";
            data[3][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[3][2] = bod.getVarFormateadaPorNombre("UAL", rs.getInt("decimales"));
            data[3][3] = "200;100";

            rs = db.ResultadoSelect("datospregunta", "UNT");
            data[4][0] = "T";//Solo tabla
            data[4][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[4][2] = bod.getVarFormateadaPorNombre("UNT", rs.getInt("decimales"));
            data[4][3] = "0;0";

            //Dato por defecto
            data[5][0] = "M";//Solo mapeado
            data[5][1] = ";m";
            data[5][2] = "50°";
            data[5][3] = "820;687";

            //Dato por defecto
            data[6][0] = "M";
            data[6][1] = ";m";
            data[6][2] = "1.2 m";
            data[6][3] = "750;718";

            //Dato por defecto
            data[7][0] = "M";
            data[7][1] = ";m";
            data[7][2] = "0.1 m";
            data[7][3] = "824;814";

            //Dato por defecto
            data[8][0] = "M";
            data[8][1] = ";m";
            data[8][2] = "0.7 m";
            data[8][3] = "1038;768";

            //Dato por defecto
            data[9][0] = "M";
            data[9][1] = ";m";
            data[9][2] = "0.25 m";
            data[9][3] = "890;460";

            graficar = new Graficar(rutaimagen, 650, 868);
            lblImagen.setIcon(graficar.procesarImagen(data));
            lblTabla.setText(graficar.tabla);

            lblDerechos.setText(" © PTAR Diseño");
            String u = rutaimagen.replace(".png", "_color.png");
            BufferedImage imgu = ImageIO.read(new File(u));
            ImageIcon iconLogou = new ImageIcon(graficar.escalarImagen(imgu, 100, 82)); //Esta relacion de aspecto se debe tomar en un programa de diseño o calcularse
            lblMiniatura.setIcon(iconLogou);

        } catch (Exception ex) {
            log.error("Error en InicializarComponentes " + ex.getMessage());
            ErrorGeneral(); // Ha ocurrido algun error en los datos o conexión, se debe revisar log.
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }

    private void ImagenLagunaFacultativaUasb() {

        ResultSet rs;
        try {
            String rutaimagen;

            rs = db.ResultadoSelect("obtenerpreguntesp", "*UF", null);

            lbl_Resultado_titulo1.setText(rs.getString("titulo1"));
            lbl_Resultado_titulo2.setText(rs.getString("titulo2"));
            AsignarPopupBtn(btn_Resultado_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 150);

            rutaimagen = conf.ObtenerRutaGeneral("rutaimagenes") + conf.separador + conf.ObtenerKey("imglanaerobia13");//Facultativa es 1:3  
            String[][] data = new String[11][4];

            rs = db.ResultadoSelect("datospregunta", "GSL");//Largo
            data[0][0] = "T-M"; //T = mostrar en tabla, M = mostrar en la figura
            data[0][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");//Título; medida
            data[0][2] = bod.getVarFormateadaPorNombre("GSL", rs.getInt("decimales"));//Valor 
            data[0][3] = "715;720";//coordenada Ancho;Alto - si hay mas de un ';' = se necesita replicar la medida en otras coordenadas

            rs = db.ResultadoSelect("datospregunta", "GSA");//Ancho 
            data[1][0] = "T-M";
            data[1][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[1][2] = bod.getVarFormateadaPorNombre("GSA", rs.getInt("decimales"));
            data[1][3] = "130;287";

            //Lado Y
            data[2][0] = "M";
            data[2][1] = ";m";
            data[2][2] = bod.getVarFormateada(bod.calcularLYX(bod.getVarDob("GAP"), bod.getVarDob("GCA"), bod.getVarDob("GAB")), 2);// lap,  lau,  lab
            data[2][3] = "1200;615";

            rs = db.ResultadoSelect("datospregunta", "GAI");//Ángulo de inclinación de la pendiente
            data[3][0] = "T+M";
            data[3][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[3][2] = bod.getVarFormateadaPorNombre("GAI", rs.getInt("decimales")) + rs.getString("titulo2");
            data[3][3] = "386;834";

            rs = db.ResultadoSelect("datospregunta", "GCA");
            data[4][0] = "T+M";
            data[4][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[4][2] = bod.getVarFormateadaPorNombre("GCA", rs.getInt("decimales"));
            data[4][3] = "730;828";

            rs = db.ResultadoSelect("datospregunta", "GAB");//Borde libre
            data[5][0] = "T+M";
            data[5][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[5][2] = bod.getVarFormateadaPorNombre("GAB", rs.getInt("decimales"));
            data[5][3] = "1200;790";

            //Largo del fondo
            data[6][0] = " M";
            data[6][1] = ";m";
            data[6][2] = bod.getVarFormateada(bod.calcularLLF(bod.getVarDob("GSL"), bod.getVarDob("GAP"), bod.getVarDob("GCA")), 2);//laa,  lap, lau
            data[6][3] = "715;756";

            //Ancho del fondo
            data[7][0] = " M";
            data[7][1] = ";m";
            data[7][2] = bod.getVarFormateada(bod.calcularLAF(bod.getVarDob("GSA"), bod.getVarDob("GAP"), bod.getVarDob("GCA")), 2);//laa,  lap, lau
            data[7][3] = "390;280";

            //Largo Total
            data[8][0] = " M";
            data[8][1] = ";m";
            data[8][2] = bod.getVarFormateada(bod.calcularLLT(bod.getVarDob("GSL"), bod.getVarDob("GAP"), bod.getVarDob("GCA"), bod.getVarDob("GAB")), 2);//lal, lap, lau, lab
            data[8][3] = "715;625";

            //Ancho Total
            data[9][0] = " M";
            data[9][1] = ";m";
            data[9][2] = bod.getVarFormateada(bod.calcularLAT(bod.getVarDob("GSA"), bod.getVarDob("GAP"), bod.getVarDob("GCA"), bod.getVarDob("GAB")), 2);//laa, lap, lau, lab
            data[9][3] = "1313;274";

            rs = db.ResultadoSelect("datospregunta", "GUA");//Área total de UASB + laguna
            data[10][0] = "T";
            data[10][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[10][2] = bod.getVarFormateadaPorNombre("GUA", rs.getInt("decimales"));
            data[10][3] = "000;000";

            graficar = new Graficar(rutaimagen, 650, 868);
            lblImagen.setIcon(graficar.procesarImagen(data));
            lblTabla.setText(graficar.tabla);

            lblDerechos.setText(" © PTAR Diseño");
            String u = rutaimagen.replace(".png", "_color.png");
            BufferedImage imgu = ImageIO.read(new File(u));
            ImageIcon iconLogou = new ImageIcon(graficar.escalarImagen(imgu, 100, 82)); //Esta relacion de aspecto se debe tomar en un programa de diseño o calcularse
            lblMiniatura.setIcon(iconLogou);

        } catch (Exception ex) {
            log.error("Error en InicializarComponentes " + ex.getMessage());
            ErrorGeneral(); // Ha ocurrido algun error en los datos o conexión, se debe revisar log.
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }

    private void ImagenFiltroPercolador() {
        ResultSet rs;
        try {
            String rutaimagen;

            rs = db.ResultadoSelect("obtenerpreguntesp", "*FP", null);

            lbl_Resultado_titulo1.setText(rs.getString("titulo1"));
            lbl_Resultado_titulo2.setText(rs.getString("titulo2"));
            AsignarPopupBtn(btn_Resultado_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 150);

            rutaimagen = conf.ObtenerRutaGeneral("rutaimagenes") + conf.separador + conf.ObtenerKey("imgfiltropercol");
            String[][] data = new String[6][4];

            rs = db.ResultadoSelect("datospregunta", "PFD");
            data[0][0] = "T-M"; //T = mostrar en tabla, M = mostrar en la figura
            data[0][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");//Título; medida
            data[0][2] = bod.getVarFormateadaPorNombre("PFD", rs.getInt("decimales"));//Valor 
            data[0][3] = "541;20;";//coordenada Ancho;Alto - si hay mas de un ';' = se necesita replicar la medida en otras coordenadas

            rs = db.ResultadoSelect("datospregunta", "PPM");
            data[1][0] = "T-M";
            data[1][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[1][2] = bod.getVarFormateadaPorNombre("PPM", rs.getInt("decimales"));
            data[1][3] = "1180;360";

            rs = db.ResultadoSelect("datospregunta", "PDX"); //------------------------sedimentador secundario
            data[2][0] = "T+M";
            data[2][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[2][2] = bod.getVarFormateadaPorNombre("PDX", rs.getInt("decimales"));
            data[2][3] = "730;570";

            rs = db.ResultadoSelect("datospregunta", "PPS");
            data[3][0] = "T+M";
            data[3][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[3][2] = bod.getVarFormateadaPorNombre("PPS", rs.getInt("decimales"));
            data[3][3] = "1200;768";

            rs = db.ResultadoSelect("obtenerseccionalias", "FILTPERCL", null);
            data[4][0] = "M";
            data[4][1] = ";";
            data[4][2] = rs.getString("Nombre"); //sólo para mostrar el nombre de cada diseño
            data[4][3] = "530;500";

            rs = db.ResultadoSelect("datospregunta", "PSS");
            data[5][0] = "M";
            data[5][1] = ";";
            data[5][2] = rs.getString("descripcion");//sólo para mostrar el nombre de cada diseño
            data[5][3] = "1000;930";

            graficar = new Graficar(rutaimagen, 650, 868);
            lblImagen.setIcon(graficar.procesarImagen(data));
            lblTabla.setText(graficar.tabla);

            lblDerechos.setText(" © PTAR Diseño");
            String u = rutaimagen.replace(".png", "_color.png");
            BufferedImage imgu = ImageIO.read(new File(u));
            ImageIcon iconLogou = new ImageIcon(graficar.escalarImagen(imgu, 100, 82)); //Esta relacion de aspecto se debe tomar en un programa de diseño o calcularse
            lblMiniatura.setIcon(iconLogou);

        } catch (Exception ex) {
            log.error("Error en InicializarComponentes " + ex.getMessage());
            ErrorGeneral(); // Ha ocurrido algun error en los datos o conexión, se debe revisar log.
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }

    private void ImagenLodosActModConvUasb() {
        ResultSet rs;
        try {
            String rutaimagen;

            rs = db.ResultadoSelect("obtenerpreguntesp", "*UL", null);

            lbl_Resultado_titulo1.setText(rs.getString("titulo1"));
            lbl_Resultado_titulo2.setText(rs.getString("titulo2"));
            AsignarPopupBtn(btn_Resultado_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 150);

            rutaimagen = conf.ObtenerRutaGeneral("rutaimagenes") + conf.separador + conf.ObtenerKey("imglodsmodconvu");
            String[][] data = new String[7][4];

            rs = db.ResultadoSelect("datospregunta", "NPT");//Tanque de aireación: Profundidad
            data[0][0] = "T-M"; //T = mostrar en tabla, M = mostrar en la figura
            data[0][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");//Título; medida
            data[0][2] = bod.getVarFormateadaPorNombre("NPT", rs.getInt("decimales"));//Valor 
            data[0][3] = "7;300;";//coordenada Ancho;Alto - si hay mas de un ';' = se necesita replicar la medida en otras coordenadas

            rs = db.ResultadoSelect("datospregunta", "NWT");//Tanque de aireación: Ancho
            data[1][0] = "T-M";
            data[1][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[1][2] = bod.getVarFormateadaPorNombre("NWT", rs.getInt("decimales"));
            data[1][3] = "1130;70";

            rs = db.ResultadoSelect("datospregunta", "NHT");//Tanque de aireación: Largo
            data[2][0] = "T+M";
            data[2][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[2][2] = bod.getVarFormateadaPorNombre("NHT", rs.getInt("decimales"));
            data[2][3] = "440;70";

            rs = db.ResultadoSelect("datospregunta", "NHS");//Sedimentador secundario: Profundidad de sedimentación
            data[3][0] = "T+M";
            data[3][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[3][2] = bod.getVarFormateadaPorNombre("NHS", rs.getInt("decimales"));
            data[3][3] = "1220;768";

            rs = db.ResultadoSelect("datospregunta", "NSD");//Sedimentador secundario: Diámetro
            data[4][0] = "T+M";
            data[4][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[4][2] = bod.getVarFormateadaPorNombre("NSD", rs.getInt("decimales"));
            data[4][3] = "770;570";

            rs = db.ResultadoSelect("datospregunta", "NDB");//Tanque de aireación: Título 
            data[5][0] = "M";
            data[5][1] = ";";
            data[5][2] = rs.getString("descripcion");
            data[5][3] = "530;500";

            rs = db.ResultadoSelect("datospregunta", "NFS");//Sedimentador secundario:  Título 
            data[6][0] = "M";
            data[6][1] = " ";
            data[6][2] = rs.getString("descripcion");
            data[6][3] = "1000;930";

            graficar = new Graficar(rutaimagen, 650, 868);
            lblImagen.setIcon(graficar.procesarImagen(data));
            lblTabla.setText(graficar.tabla);

            lblDerechos.setText(" © PTAR Diseño");
            String u = rutaimagen.replace(".png", "_color.png");
            BufferedImage imgu = ImageIO.read(new File(u));
            ImageIcon iconLogou = new ImageIcon(graficar.escalarImagen(imgu, 100, 82)); //Esta relacion de aspecto se debe tomar en un programa de diseño o calcularse
            lblMiniatura.setIcon(iconLogou);

        } catch (Exception ex) {
            log.error("Error en InicializarComponentes " + ex.getMessage());
            ErrorGeneral(); // Ha ocurrido algun error en los datos o conexión, se debe revisar log.
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }

    private void ImagenLodosActModConv() {
        ResultSet rs;
        try {
            String rutaimagen;

            rs = db.ResultadoSelect("obtenerpreguntesp", "*LC", null);

            lbl_Resultado_titulo1.setText(rs.getString("titulo1"));
            lbl_Resultado_titulo2.setText(rs.getString("titulo2"));
            AsignarPopupBtn(btn_Resultado_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 150);

            rutaimagen = conf.ObtenerRutaGeneral("rutaimagenes") + conf.separador + conf.ObtenerKey("imglodsamodconv");
            String[][] data = new String[10][4];

            rs = db.ResultadoSelect("datospregunta", "MDB");//Tanque de aireación: Título  
            data[0][0] = "T+M";
            data[0][1] = " ; ";
            data[0][2] = rs.getString("descripcion");
            data[0][3] = "1000;680";

            rs = db.ResultadoSelect("datospregunta", "MPT");//Tanque de aireación: Profundidad  
            data[1][0] = "T-M"; //T = mostrar en tabla, M = mostrar en la figura
            data[1][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");//Título; medida
            data[1][2] = bod.getVarFormateadaPorNombre("MPT", rs.getInt("decimales"));//Valor 
            data[1][3] = "200;520;";//coordenada Ancho;Alto - si hay mas de un ';' = se necesita replicar la medida en otras coordenadas

            rs = db.ResultadoSelect("datospregunta", "MWT");//Tanque de aireación: Ancho  
            data[2][0] = "T-M";
            data[2][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[2][2] = bod.getVarFormateadaPorNombre("MWT", rs.getInt("decimales"));
            data[2][3] = "1015;365";

            rs = db.ResultadoSelect("datospregunta", "MHT");//Tanque de aireación: Largo  
            data[3][0] = "T+M";
            data[3][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[3][2] = bod.getVarFormateadaPorNombre("MHT", rs.getInt("decimales"));
            data[3][3] = "530;365";

            rs = db.ResultadoSelect("datospregunta", "MCH");//Sedimentador Primario:  Título  
            data[4][0] = "T+M";
            data[4][1] = " ; "; //debe llevar espacios, si está vacío
            data[4][2] = rs.getString("descripcion");
            data[4][3] = "1125;316";

            rs = db.ResultadoSelect("datospregunta", "MPS");//Sedimentador Primario: Profundidad de sedimentación  
            data[5][0] = "T+M";
            data[5][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[5][2] = bod.getVarFormateadaPorNombre("MPS", rs.getInt("decimales"));
            data[5][3] = "1230;180";

            rs = db.ResultadoSelect("datospregunta", "MGC");//Sedimentador Primario: Diámetro   
            data[6][0] = "T+M";
            data[6][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[6][2] = bod.getVarFormateadaPorNombre("MGC", rs.getInt("decimales"));
            data[6][3] = "910;30";

            rs = db.ResultadoSelect("datospregunta", "MFS");//Sedimentador Secundario:  Título  
            data[7][0] = "T+M";
            data[7][1] = " ; ";
            data[7][2] = rs.getString("descripcion");
            data[7][3] = "680;960";

            rs = db.ResultadoSelect("datospregunta", "MHS");//Sedimentador Secundario: Profundidad de sedimentación   
            data[8][0] = "T+M";
            data[8][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[8][2] = bod.getVarFormateadaPorNombre("MHS", rs.getInt("decimales"));
            data[8][3] = "760;830";

            rs = db.ResultadoSelect("datospregunta", "MSD");//Sedimentador Secundario: Diámetro    
            data[9][0] = "T+M";
            data[9][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[9][2] = bod.getVarFormateadaPorNombre("MSD", rs.getInt("decimales"));
            data[9][3] = "425;680";

            graficar = new Graficar(rutaimagen, 650, 868);
            lblImagen.setIcon(graficar.procesarImagen(data));
            lblTabla.setText(graficar.tabla);

            lblDerechos.setText(" © PTAR Diseño");
            String u = rutaimagen.replace(".png", "_color.png");
            BufferedImage imgu = ImageIO.read(new File(u));
            ImageIcon iconLogou = new ImageIcon(graficar.escalarImagen(imgu, 100, 82)); //Esta relacion de aspecto se debe tomar en un programa de diseño o calcularse
            lblMiniatura.setIcon(iconLogou);

        } catch (Exception ex) {
            log.error("Error en InicializarComponentes " + ex.getMessage());
            ErrorGeneral(); // Ha ocurrido algun error en los datos o conexión, se debe revisar log.
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }

    private void ImagenLodosActAireacionExtend() {
        ResultSet rs;
        try {
            String rutaimagen;

            rs = db.ResultadoSelect("obtenerpreguntesp", "*LE", null);

            lbl_Resultado_titulo1.setText(rs.getString("titulo1"));
            lbl_Resultado_titulo2.setText(rs.getString("titulo2"));
            AsignarPopupBtn(btn_Resultado_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 150);

            rutaimagen = conf.ObtenerRutaGeneral("rutaimagenes") + conf.separador + conf.ObtenerKey("imglodactairext");
            String[][] data = new String[7][4];

            rs = db.ResultadoSelect("datospregunta", "EDB");//Tanque de aireación: Título  
            data[0][0] = "T+M";
            data[0][1] = " ; ";
            data[0][2] = rs.getString("descripcion");
            data[0][3] = "540;480";

            rs = db.ResultadoSelect("datospregunta", "EPT");//Tanque de aireación: Profundidad  
            data[1][0] = "T-M"; //T = mostrar en tabla, M = mostrar en la figura
            data[1][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");//Título; medida
            data[1][2] = bod.getVarFormateadaPorNombre("EPT", rs.getInt("decimales"));//Valor 
            data[1][3] = "20;280;";//coordenada Ancho;Alto - si hay mas de un ';' = se necesita replicar la medida en otras coordenadas

            rs = db.ResultadoSelect("datospregunta", "EWT");//Tanque de aireación: Ancho  
            data[2][0] = "T-M";
            data[2][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[2][2] = bod.getVarFormateadaPorNombre("EWT", rs.getInt("decimales"));
            data[2][3] = "1130;70";

            rs = db.ResultadoSelect("datospregunta", "EHT");//Tanque de aireación: Largo  
            data[3][0] = "T+M";
            data[3][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[3][2] = bod.getVarFormateadaPorNombre("EHT", rs.getInt("decimales"));
            data[3][3] = "460;70";

            rs = db.ResultadoSelect("datospregunta", "EFS");//Sedimentador Secundario:  Título  
            data[4][0] = "T+M";
            data[4][1] = " ; "; //debe llevar espacios, si está vacío
            data[4][2] = rs.getString("descripcion");
            data[4][3] = "1040;950";

            rs = db.ResultadoSelect("datospregunta", "EHS");//Sedimentador Secundario: Profundidad de sedimentación  
            data[5][0] = "T+M";
            data[5][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[5][2] = bod.getVarFormateadaPorNombre("EHS", rs.getInt("decimales"));
            data[5][3] = "1200;770";

            rs = db.ResultadoSelect("datospregunta", "ESD");//Sedimentador Secundario: Diámetro   
            data[6][0] = "T+M";
            data[6][1] = rs.getString("titulo1") + ";" + rs.getString("titulo2");
            data[6][2] = bod.getVarFormateadaPorNombre("ESD", rs.getInt("decimales"));
            data[6][3] = "730;560";

            graficar = new Graficar(rutaimagen, 650, 868);
            lblImagen.setIcon(graficar.procesarImagen(data));
            lblTabla.setText(graficar.tabla);

            lblDerechos.setText(" © PTAR Diseño");
            String u = rutaimagen.replace(".png", "_color.png");
            BufferedImage imgu = ImageIO.read(new File(u));
            ImageIcon iconLogou = new ImageIcon(graficar.escalarImagen(imgu, 100, 82)); //Esta relacion de aspecto se debe tomar en un programa de diseño o calcularse
            lblMiniatura.setIcon(iconLogou);

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

        Component[] comp = jp_Resultado.getComponents();//Deshabilitar todos los Componentes del Jpane
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

    /**
     * *
     * Imprime lo que hay dentro del Jpanel y lo exporta como PNG
     */
    private void imprimirImagen() {

        String ruta = util.dialogoCrearArchivo();
        if (ruta.equals("")) {
            return;
        }
        try {
            BufferedImage image = new BufferedImage(jp_Resultado.getWidth(), jp_Resultado.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            jp_Resultado.printAll(g);
            g.dispose();

            if (!util.obtenerExtension(ruta, ".png")) {
                ruta = ruta.trim() + ".png";
            }

            ImageIO.write(image, "png", new File(ruta));//ImageIO.write(image, "jpg", new File(ruta)); Baja calidad!

            util.Mensaje("Archivo guardado", "ok");
        } catch (IOException exp) {
            util.Mensaje("Error al crear archivo", "error");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jp_Resultado = new javax.swing.JPanel();
        lblImagen = new javax.swing.JLabel();
        lblTabla = new javax.swing.JLabel();
        lblMiniatura = new javax.swing.JLabel();
        lblDerechos = new javax.swing.JLabel();
        lbl_Resultado_titulo1 = new javax.swing.JLabel();
        lbl_Resultado_titulo2 = new javax.swing.JLabel();
        btn_Guardar = new javax.swing.JButton();
        btn_close = new javax.swing.JButton();
        btn_Resultado_ayuda = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1024, 700));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(1026, 1200));

        jp_Resultado.setBackground(new java.awt.Color(255, 255, 255));
        jp_Resultado.setPreferredSize(new java.awt.Dimension(1024, 800));
        jp_Resultado.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblImagen.setBackground(new java.awt.Color(255, 255, 255));
        lblImagen.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jp_Resultado.add(lblImagen, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 0, 889, 690));

        lblTabla.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTabla.setText(" ");
        lblTabla.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblTabla.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jp_Resultado.add(lblTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 119, 550));
        jp_Resultado.add(lblMiniatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 119, 112));
        jp_Resultado.add(lblDerechos, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 670, 119, 15));

        lbl_Resultado_titulo1.setText("Titulo");
        lbl_Resultado_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Resultado.add(lbl_Resultado_titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 690, 460, -1));

        lbl_Resultado_titulo2.setText("Titulo");
        lbl_Resultado_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_Resultado.add(lbl_Resultado_titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 710, 511, -1));

        jScrollPane1.setViewportView(jp_Resultado);

        btn_Guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Button_Exportar_21.png"))); // NOI18N
        btn_Guardar.setText("Exportar");
        btn_Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GuardarActionPerformed(evt);
            }
        });

        btn_close.setText("Salir");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });

        btn_Resultado_ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion.png"))); // NOI18N
        btn_Resultado_ayuda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_Resultado_ayuda.setContentAreaFilled(false);
        btn_Resultado_ayuda.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/interrogacion2.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1008, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Resultado_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(btn_Guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_close, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Resultado_ayuda, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_Guardar)
                        .addComponent(btn_close, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_closeActionPerformed
        bod.WinResultado = false;
        this.dispose();
    }//GEN-LAST:event_btn_closeActionPerformed

    private void btn_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GuardarActionPerformed
        imprimirImagen();

    }//GEN-LAST:event_btn_GuardarActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Guardar;
    private javax.swing.JButton btn_Resultado_ayuda;
    private javax.swing.JButton btn_close;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jp_Resultado;
    private javax.swing.JLabel lblDerechos;
    private javax.swing.JLabel lblImagen;
    private javax.swing.JLabel lblMiniatura;
    private javax.swing.JLabel lblTabla;
    private javax.swing.JLabel lbl_Resultado_titulo1;
    private javax.swing.JLabel lbl_Resultado_titulo2;
    // End of variables declaration//GEN-END:variables
}
