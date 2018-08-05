package Componentes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.apache.log4j.Logger;

public class Graficar {

    static Logger log = Logger.getLogger("Graficar");
    public String rutaimagen;
    public String tabla;
    int alto;
    int ancho;

    public Graficar(String rutaimg, int alt, int anch) {
        rutaimagen = rutaimg;
        ancho = anch;
        alto = alt;
    }

    public ImageIcon procesarImagen(String[][] datos) {

        try {
            String[] arr;
            tabla = "<html><style>table td{border: 1px solid #c8d2d9;border-collapse: collapse;} </style> <table cellspacing='0' cellpadding='0'>";

            for (String[] dato : datos) {

                if (dato[0].toUpperCase().contains("T")) {//la posición [0] define si se muestra el dato en esta tabla
                    arr = dato[1].split(";");
                    tabla += "<tr bgcolor=#a5c5d9><td>" + arr[0] + "</td> </tr> <tr><td>" + dato[2] + " " + arr[1] + "</td></tr>   ";
                }
            }
            tabla += "</table></html>";

            BufferedImage img = ImageIO.read(new File(rutaimagen));
            Graphics grf = img.getGraphics();
            grf.setColor(Color.darkGray);
            grf.setFont(grf.getFont().deriveFont(20f));
            String[] coo = null;
            for (String[] dato : datos) {

                if (dato[0].toUpperCase().contains("M")) {//El número 2 define si se muestra el dato acá
                    coo = dato[3].split(";"); //Puede traer más de 2 coordenadas
                    grf.drawString(dato[2], Integer.parseInt(coo[0]), Integer.parseInt(coo[1]));

                    if (coo.length > 3) {//más de 2 coordenadas (se necesita replicar la medida en otras coordenadas)
                        grf.drawString(dato[2], Integer.parseInt(coo[2]), Integer.parseInt(coo[3]));
                    }
                }
            }
            grf.dispose();
            return new ImageIcon(escalarImagen(img, ancho, alto));

        } catch (IOException | NumberFormatException ex) {
            log.error("Error procesarImagen() " + ex.getMessage());
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
