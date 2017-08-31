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

public class Bod_MedParshall {

    static Logger log = Logger.getLogger("Bod_MedParshall");
    private Configuracion conf = new Configuracion();
    //private Validaciones validar = new Validaciones();
    public String rutaimagen;
    public String tablaimagen;
    private Dao db = new Dao(conf.ObtenerKey("rutabasedatos") + File.separator + conf.ObtenerKey("nombrebasedatos"));

    public Bod_MedParshall() {
        InicializarComponentes();
    }

    /**
     * Inicializa elementos con sus respectivos valores y textos traidos desde
     * archivo de configuración.
     */
    public void InicializarComponentes() {
        rutaimagen = conf.ObtenerRutaGeneral("rutaimagenes") + File.separator + conf.ObtenerKey("imgtratprelmepa");//Todo: ruta iamgenes en bod         
    }

    /**
     * Modifica la imagen y le sobreescribe las coordenadas con sus respectivos
     * valores
     *
     * @return
     */
    public ImageIcon procesarImagen(String _DPA, String _DPB, String _DPC, String _DPD, String _DPE, String _DPF, String _DPG, String _DPK, String _DPN,String _DP1,String _DAN) {

        try {

            String _DPApozo = calcularDPApozo(Double.parseDouble(_DPA));
            String temp = "0.0";
            tablaimagen = "<html><style>table td{border: 1px solid #c8d2d9;border-collapse: collapse;}   "
                    + "</style> <table cellspacing='0' cellpadding='0' >"
                    + "<tr bgcolor=#a5c5d9><td>Dimension A</td> </tr>  <tr><td>=&nbsp;" + _DPA + "cm</td> </tr> "
                    + "<tr bgcolor=#a5c5d9><td>Dimension A pozo medidor Parshall</td> </tr>  <tr><td>=&nbsp;" + _DPApozo + "cm</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Dimension B</td>  <tr><td>=&nbsp;" + _DPB + "cm</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Dimension C</td>  <tr><td>=&nbsp;" + _DPC + "cm</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Dimension D</td>  <tr><td>=&nbsp;" + _DPD + "cm</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Dimension E</td>  <tr><td>=&nbsp;" + _DPE + "cm</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Dimension F</td>  <tr><td>=&nbsp;" + _DPF + "cm</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Dimension G</td>  <tr><td>=&nbsp;" + _DPG + "cm</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Dimension K</td>  <tr><td>=&nbsp;" + _DPK + "cm</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Dimension N</td>  <tr><td>=&nbsp;" + _DPN + "cm</td> </tr>"
                     + "<tr bgcolor=#a5c5d9><td>Ancho nominal (W)</td> </tr> <tr><td></td> </tr> <tr><td>=&nbsp;" + _DAN + "cm</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Profundidad de la lámina de agua Hmax</td> </tr> <tr><td>=&nbsp;" + _DP1 + "m</td> </tr></table></html>";


            BufferedImage img = ImageIO.read(new File(rutaimagen));
            Graphics g = img.getGraphics();
            g.setColor(Color.darkGray);
            g.setFont(g.getFont().deriveFont(20f));

            g.drawString("A", 410, 510);//Todo: coordenadas quemadas
            g.drawString(_DPA + "cm", 430, 510);
            g.drawString("A'", 410, 300);
            g.drawString(_DPApozo + "cm", 430, 300);
            g.drawString("B", 420, 610);
            g.drawString(_DPB + "cm", 440, 610);
            g.drawString("C", 1170, 310);
            g.drawString(_DPB + "cm", 1190, 310);
            g.drawString("D", 110, 310);
            g.drawString(_DPD + "cm", 130, 310);
            //g.drawString("E", 110, 310);
            //g.drawString(_DPE + "cm", 130, 310);
            g.drawString("F", 730, 610);
            g.drawString(_DPF + "cm", 750, 610);
            g.drawString("G", 940, 610);
            g.drawString(_DPG + "cm", 960, 610);
            g.drawString("K", 1150, 900);
            g.drawString(_DPK + "cm", 1170, 900);
            g.drawString("N", 470, 920);
            g.drawString(_DPN + "cm", 490, 920);
             g.drawString("W", 1000, 320);
            g.drawString(_DAN + "cm", 1000, 350);
            g.drawString("Hmax", 480, 820);
            g.drawString(_DP1 + "m", 480, 850);
            g.dispose();

            return new ImageIcon(escalarImagen(img, 868, 650));//868, 650));

        } catch (Exception ex) {
            log.error("Error en procesarImagen() " + ex.getMessage());
        }
        return null;
    }

    /**
     * Calcula la distancia al centro del pozo desde la garganta
     *
     * @param DPA Dimension A del medidor Parshall
     * @return
     */
    public String calcularDPApozo(double DPA) {

        Map<String, Double> valores = new HashMap<>();
        valores.put("DPA", DPA);

        Calculos cal = new Calculos();
        return cal.CalcularGeneral("distanciagargantaapozo", valores);
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
