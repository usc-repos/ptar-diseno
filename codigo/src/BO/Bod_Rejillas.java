package BO;

import Componentes.Calculos;
import Componentes.Configuracion;
import Componentes.Validaciones;
import DB.Dao;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.apache.log4j.Logger;

public class Bod_Rejillas {

    static Logger log = Logger.getLogger("Bod_Rejillas");
    private Configuracion conf = new Configuracion();
    private Validaciones validar = new Validaciones();
    public String rutaimagen;
    public String tablaimagen;
    private Dao db = new Dao(conf.ObtenerKey("rutabasedatos") + File.separator + conf.ObtenerKey("nombrebasedatos"));

    public Bod_Rejillas() {
        InicializarComponentes();
    }

    /**
     * Inicializa elementos con sus respectivos valores y textos traidos desde
     * archivo de configuración.
     */
    public void InicializarComponentes() {
        rutaimagen = conf.ObtenerRutaGeneral("rutaimagenes") + File.separator + conf.ObtenerKey("imgtratprelreji");//Todo: ruta iamgenes en bod         
    }

    /**
     * Modifica la imagen y le sobreescribe las coordenadas con sus respectivos
     * valores
     *
     * @param _RTR
     * @param _REB
     * @param _REL
     * @return
     */
    public ImageIcon procesarImagen(String _RLC, double RPS_, String _RPS, double RIB_, String _RAR, String _RNB, String _RNE, String _RPL, double DP1_) { //TODO: rps dejar 1

        try {  

            String temp = "0.0";
            String RHM = calcularRHM(RPS_, DP1_);
            String RXM = calcularRXM(Double.parseDouble(RHM), RIB_);

            //Todo: tìtulo entre <td> debe ser traido de BD
            tablaimagen = "<html><style>table td{border: 1px solid #c8d2d9;border-collapse: collapse;}   "
                    + "</style> <table cellspacing='0' cellpadding='0' >"
                    + "<tr bgcolor=#a5c5d9><td>Longitud canal (L)</td> </tr> <tr><td></td></tr> <tr><td>=&nbsp;" + _RLC + "m</td> </tr> "
                    + "<tr bgcolor=#a5c5d9><td>Inclinación de las barras (θ)</td> </tr> <tr><td>=&nbsp;" + RIB_ + "º</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Ancho de rejillas (b)</td> </tr>  <tr><td>=&nbsp;" + _RAR + "m</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Profundidad de la lámina de agua (h)</td> </tr> <tr><td> </td> </tr> <tr><td>=&nbsp;" + _RPL + "m</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Altura Max</td> </tr> <tr><td>Hmax(dearenador) + hL</td> </tr> <tr><td>=&nbsp;" + RHM + "m</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Medida x</td> </tr> <tr><td>Hmax(dearenador)+0.5/tan(θ)</td> </tr> <tr><td>=&nbsp;" + RXM + "m</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Pérdida de carga en la rejilla 50% sucia (hL)</td> </tr> <tr><td>=&nbsp;" + _RPS  + "m</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Número de barras</td> </tr> <tr><td>=&nbsp;" + _RNB + "</td> </tr> "
                    + "<tr bgcolor=#a5c5d9><td>Espaciamientos</td> </tr> <tr><td>=&nbsp;" + _RNE + " mm</td> </tr></table></html>";

            BufferedImage img = ImageIO.read(new File(rutaimagen));
            Graphics g = img.getGraphics();
            g.setColor(Color.darkGray);
            g.setFont(g.getFont().deriveFont(20f));

            g.drawString("L", 380, 570);//Todo: coordenadas quemadas
            g.drawString(_RLC, 400, 570);
            g.drawString("Bypass", 720, 100);//Medida quemada
            g.drawString("0.40m", 830, 100);
            g.drawString("0.1m", 1390, 170);//Medida quemada
            g.drawString("0.1m", 1050, 250);//Medida quemada
            g.drawString("0.3m", 1200, 300);//Medida quemada
            g.drawString("0.50m", 550, 730);//Medida quemada
            g.drawString("0.80m", 1030, 570);//Medida quemada
            g.drawString("hL", 870, 830);
            g.drawString(_RPS + "m", 920, 830);
            g.drawString("θ", 790, 940);
            g.drawString(RIB_ + "º", 810, 940);
            g.drawString("b", 1400, 305);
            g.drawString(_RAR, 1400, 335);
            g.drawString("h", 100, 835);
            g.drawString(_RPL + "m", 130, 835);
            g.drawString("x", 800, 570);
            g.drawString(RXM + "m", 820, 570);
            g.drawString("Hmax", 580, 890);
            g.drawString(RHM + "m", 580, 910);
            g.dispose();

            return new ImageIcon(escalarImagen(img, 868, 650)); 

        } catch (Exception ex) {
            log.error("Error en procesarImagen() " + ex.getMessage());
        }
        return null;
    }

    /**
     * @param RPS_
     * @param DP1_
     * @return
     */
    public String calcularRHM(double RPS_, double DP1_) {
 
 
        Map<String, Double> valores = new HashMap<>();
        valores.put("RPS", RPS_);
        valores.put("DP1", DP1_);

        Calculos cal = new Calculos();
        return cal.CalcularGeneral("hmaxrejilla", valores);
    }

    /**
     * @param RHM_
     * @param RIB_
     * @return
     */
    public String calcularRXM(double RHM_, double RIB_) {

        Map<String, Double> valores = new HashMap<>();
        valores.put("RHM", RHM_);
        valores.put("RIB", Math.toRadians(RIB_)); 

        Calculos cal = new Calculos();
        return cal.CalcularGeneral("medidaxrejilla", valores);
    }

    public Image escalarImagen(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
}
