package BO;

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
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.apache.log4j.Logger;

public class Bod_Desarenador {

    static Logger log = Logger.getLogger("Bod_Desarenador");
    private Configuracion conf = new Configuracion();
    private Validaciones validar = new Validaciones();
    public String rutaimagen;
    public String tablaimagen;
    private Dao db = new Dao(conf.ObtenerKey("rutabasedatos") + File.separator + conf.ObtenerKey("nombrebasedatos"));

    public Bod_Desarenador() {
        InicializarComponentes();
    }

    /**
     * Inicializa elementos con sus respectivos valores y textos traidos desde
     * archivo de configuración.
     */
    public void InicializarComponentes() {
        rutaimagen = conf.ObtenerRutaGeneral("rutaimagenes") + File.separator + conf.ObtenerKey("imgtratpreldesa");//Todo: ruta iamgenes en bod         
    }

    /**
     * Modifica la imagen y le sobreescribe las coordenadas con sus respectivos
     * valores
     * @return
     */
    public ImageIcon procesarImagen(String _DAH  ,String _DAB,String _DLD,String _DAL,String _DUP) {

        try {

            String temp = "0.0";
            tablaimagen = "<html><style>table td{border: 1px solid #c8d2d9;border-collapse: collapse;}   "
                    + "</style> <table cellspacing='0' cellpadding='0' >"
                    + "<tr bgcolor=#a5c5d9><td>Altura máxima de lámina de agua en el desarenador (H)</td> </tr> <tr><td></td></tr> <tr><td>=&nbsp;" + _DAH + "m</td> </tr> "
                    + "<tr bgcolor=#a5c5d9><td>Ancho del desarenador (b)</td> </tr> <tr><td></td> </tr> <tr><td>=&nbsp;" + _DAB + "m</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Longitud del desarenador (L)</td> </tr> <tr><td></td> </tr> <tr><td>=&nbsp;" + _DLD + "m</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Área longitudinal del desarenador (A)</td> </tr> <tr><td></td> </tr> <tr><td>=&nbsp;" + _DAL + "m²</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Profundidad útil depósito inferior de arena (p)</td> </tr> <tr><td></td> </tr> <tr><td>=&nbsp;" + _DUP + "m</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>&nbsp;</td> </tr> <tr><td>&nbsp;"  + "</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>&nbsp;</td> </tr> <tr><td>&nbsp;"  + "</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>&nbsp;</td> </tr> <tr><td>&nbsp;"  + "</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>&nbsp;</td> </tr> <tr><td>&nbsp;"  + "</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>&nbsp;</td> </tr> <tr><td>&nbsp;"  + "</td> </tr> "
                    + "<tr bgcolor=#a5c5d9><td>&nbsp;</td> </tr> <tr><td>&nbsp;"  + "</td> </tr></table></html>";


            BufferedImage img = ImageIO.read(new File(rutaimagen));
            Graphics g = img.getGraphics();
            g.setColor(Color.darkGray);
            g.setFont(g.getFont().deriveFont(20f));

            g.drawString("H", 820, 765);//Todo: coordenadas quemadas
            g.drawString(_DAH + "m", 840, 765);             
            g.drawString("0.1m", 970, 55);//Medida quemada
            g.drawString("0.15m", 970, 165);//Medida quemada
            g.drawString("0.50m", 600, 640);//Medida quemada            
            g.drawString("b", 720, 275);
            g.drawString(_DAB + "m", 720, 290);
            g.drawString("L", 950, 480);
            g.drawString(_DLD+ "m", 970, 480);          
            g.drawString("P", 390, 827);
            g.drawString(_DUP+ "m", 420, 827);
            g.dispose();

            return new ImageIcon(escalarImagen(img, 868, 650));//868, 650));

        } catch (Exception ex) {
            log.error("Error en procesarImagen() " + ex.getMessage());
        }
        return null;
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
