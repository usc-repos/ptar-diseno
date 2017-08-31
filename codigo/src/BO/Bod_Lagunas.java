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
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.apache.log4j.Logger;

public class Bod_Lagunas {

    static Logger log = Logger.getLogger("Bod_Lagunas");
    private Configuracion conf = new Configuracion();
    public int relacion; //Relación de aspecto 1:3 2:3  LLA = (1:3 > 0)  LLA = (2:3 <= 0)
    private Validaciones validar = new Validaciones();
    public String rutaimagen;
    public String tablaimagen;
    private Dao db = new Dao(conf.ObtenerKey("rutabasedatos") + File.separator + conf.ObtenerKey("nombrebasedatos"));

    public Bod_Lagunas(int relacion) {
        this.relacion = relacion;
        InicializarComponentes();
    }

    /**
     * Inicializa elementos con sus respectivos valores y textos traidos desde
     * archivo de configuración.
     */
    public void InicializarComponentes() {

        if (relacion > 0) { //1:3
            rutaimagen = conf.ObtenerRutaGeneral("rutaimagenes") + File.separator + conf.ObtenerKey("imglanaerobia13");//Todo: ruta iamgenes en bod
        } else {
            rutaimagen = conf.ObtenerRutaGeneral("rutaimagenes") + File.separator + conf.ObtenerKey("imglanaerobia23");
        }
    }

    /**
     * *
     * Modifica la imagen y le sobreescribe las coordenadas con sus respectivos
     * valores
     *
     * @param _LAP Pendiente, Facultativa (FAP)
     * @param _LAB Borde libre, Facultativa (FAB)
     * @param _LAL Largo, Facultativa (FSL)
     * @param _LAU Altura Útil, Facultativa (FCA)
     * @param _LAA Ancho, Facultativa (FSA)
     * @param _LAI Ángulo de inclinación, Facultativa (FAI)
     * @return
     */
    public ImageIcon procesarImagen(int _LAP, double _LAB, double _LAL, double _LAU, double _LAA, double _LAI) {

        try {
            String lat = calcularLAT(_LAA, _LAP, _LAU, _LAB);//Ancho(alto) total 
            String llt = calcularLLT(_LAL, _LAP, _LAU, _LAB);//Largo total
            String laf = calcularLAF(_LAA, _LAP, _LAU); //Ancho fondo laf
            String llf = calcularLLF(_LAL, _LAP, _LAU);//Largo Fondo LLF        
            String lyx = calcularLYX(_LAP, _LAU, _LAB); //Lado del triángulo Y
            String lab = validar.DobleFormatoStringCeil(_LAB, "#.##"); //Borde Libre
            String lal = validar.DobleFormatoStringCeil(_LAL, "#.##");//Largo LAL 
            String lau = validar.DobleFormatoStringCeil(_LAU, "#.##");// Altura Útil
            String laa = validar.DobleFormatoStringCeil(_LAA, "#.##"); //Ancho Laguna LAA
            String lai = validar.DobleFormatoStringCeil(_LAI, "#.##"); // Ángulo de inclinación de la pendiente     

            String rel = relacion > 0 ? "1:3" : "2.3";
            tablaimagen = "<html><style>table td{border: 1px solid #c8d2d9;border-collapse: collapse;}   "
                    + "</style> <table cellspacing='0' cellpadding='0' >"
                    + "<tr bgcolor=#a5c5d9><td>Altura Total </td> </tr> <tr><td>aLF + n (hL + 2B)</td></tr> <tr><td>=&nbsp;" + lat + "m</td> </tr> "
                    + "<tr bgcolor=#a5c5d9><td>Longitud Total</td> </tr> <tr><td>lL + n (hL + 2B)</td> </tr> <tr><td>=&nbsp;" + llt + "m</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Longitud Fondo (L)</td> </tr> <tr><td>lL - n (hL)</td> </tr> <tr><td>=&nbsp;" + llf + "m</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Altura Fondo (A)</td> </tr> <tr><td>aL - n (hL)</td> </tr> <tr><td>=&nbsp;" + laf + "m</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Y (Y)</td> </tr> <tr><td>n (hL + B)</td> </tr> <tr><td>=&nbsp;" + lyx + "m</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Altura Laguna (aL)</td> </tr> <tr><td>=&nbsp;" + laa + "m</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Long. Laguna (lL)</td> </tr> <tr><td>=&nbsp;" + lal + "m</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Pendiente (n)</td> </tr> <tr><td>=&nbsp;" + _LAP + "m</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Ángulo Inclinac. (&alpha;)</td> </tr> <tr><td>=&nbsp;" + lai + "º</td> </tr>"
                    + "<tr bgcolor=#a5c5d9><td>Borde Libre (B)</td> </tr> <tr><td>=&nbsp;" + lab + "m</td> </tr> "
                    + "<tr bgcolor=#a5c5d9><td>Largo : Ancho</td> </tr> <tr><td>=&nbsp;" + rel + "</td> </tr></table></html>";


            BufferedImage img = ImageIO.read(new File(rutaimagen));
            Graphics g = img.getGraphics();
            g.setColor(Color.darkGray);
            g.setFont(g.getFont().deriveFont(20f));

            if (relacion > 0) { //1:3
                g.drawString("hL", 700, 960);//Todo: coordenadas quemadas
                g.drawString(lau, 780, 960);
                g.drawString("α", 355, 960);
                g.drawString(lai + "º", 380, 960);
                g.drawString("lL", 750, 800);
                g.drawString(lal, 750, 820);
                g.drawString("lTotal", 750, 700);
                g.drawString(llt, 750, 720);
                g.drawString("L", 750, 860);
                g.drawString(llf, 750, 880);
                g.drawString("B", 1300, 860);
                g.drawString(lab, 1300, 880);
                g.drawString("Y", 1300, 650);
                g.drawString(lyx, 1300, 670);
                g.drawString("aL", 7, 310);
                g.drawString(laa, 7, 330);
                g.drawString("A", 60, 310);
                g.drawString(laf, 60, 330);
                g.drawString("aTotal", 1440, 310);
                g.drawString(lat, 1440, 330);

                g.dispose();

                return new ImageIcon(escalarImagen(img, 868, 650));//868, 650));
            } else {//2:3
                g.drawString("hL", 680, 1040);//Todo: coordenadas quemadas
                g.drawString(lau, 720, 1040);
                g.drawString("α", 440, 1040);
                g.drawString(lai + "º", 465, 1040);
                g.drawString("lL", 680, 890);
                g.drawString(lal, 680, 910);
                g.drawString("lTotal", 680, 840);
                g.drawString(llt, 680, 860);
                g.drawString("L", 680, 940);
                g.drawString(llf, 680, 960);
                g.drawString("B", 1140, 920);
                g.drawString(lab, 1140, 940);
                g.drawString("Y", 1100, 480);
                g.drawString(lyx, 1100, 500);
                g.drawString("aL", 5, 385);
                g.drawString(laa, 5, 405);
                g.drawString("A", 87, 385);
                g.drawString(laf, 87, 405);
                g.drawString("aTotal", 1300, 385);
                g.drawString(lat, 1300, 405);

                g.dispose();

                return new ImageIcon(escalarImagen(img, 801, 660));//868, 650));
            }
        } catch (Exception ex) {
            log.error("Error en procesarImagen() " + ex.getMessage());
        }
        return null;
    }

    /**
     * Calcula Ancho fondo laf
     *
     * @param LAA Ancho
     * @param LAP
     * @param LAU
     * @return
     */
    public String calcularLAF(double LAA, double LAP, double LAU) {

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAA", LAA);
        valores.put("LAP", LAP);
        valores.put("LAU", LAU);

        return CalcularGeneral("anchofondo", valores);
    }

    /**
     * *
     * Calcula Largo fondo LLF
     *
     * @param LAL
     * @param LAP
     * @param LAU
     * @return
     */
    public String calcularLLF(double LAL, double LAP, double LAU) {

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAL", LAL);
        valores.put("LAP", LAP);
        valores.put("LAU", LAU);

        return CalcularGeneral("largofondo", valores);
    }

    /**
     * *
     * Calcula Ancho Total LAT
     *
     * @param LAA
     * @param LAP
     * @param LAU
     * @param LAB
     * @return
     */
    public String calcularLAT(double LAA, double LAP, double LAU, double LAB) {

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAA", LAA);
        valores.put("LAP", LAP);
        valores.put("LAU", LAU);
        valores.put("LAB", LAB);

        return CalcularGeneral("anchototal", valores);
    }

    /**
     * *
     * Calcula Largo Total LLT
     *
     * @param LAL
     * @param LAP
     * @param LAU
     * @param LAB
     * @return
     */
    public String calcularLLT(double LAL, double LAP, double LAU, double LAB) {

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAL", LAL);
        valores.put("LAP", LAP);
        valores.put("LAU", LAU);
        valores.put("LAB", LAB);

        return CalcularGeneral("largototal", valores);
    }

    /**
     * Calcula Lado (Y) del triangulo
     *
     * @param LAP
     * @param LAU
     * @param LAB
     * @return
     */
    public String calcularLYX(double LAP, double LAU, double LAB) {

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAP", LAP);
        valores.put("LAU", LAU);
        valores.put("LAB", LAB);

        return CalcularGeneral("ladoyx", valores);
    }

    /**
     * Recibe los parametros necesarios para traer de la BD una ecuacion y con
     * los parametros que llegan (que son las variables necesarias) evaluar la
     * ecuacion y dar un resultado devuelto en cadena Nota> devuelve la cadena
     * formateada a 2 decimales
     *
     * @param ecuacion Nombre de la formula en la BD
     * @param valores Nombres y valores usados (clave - valor)
     * @return
     */
    public String CalcularGeneral(String ecuacion, Map<String, Double> valores) {

        try {
            int i = 0;
            String[] nombres = new String[valores.size()];

            for (String key : valores.keySet()) {
                nombres[i] = key; //Entrega los nombres de las variables
                i++;
            }

            ResultSet result = db.ResultadoSelect("obtenerecuaciongeneral", ecuacion, null); //Trae de la bd la ecuacion
            ecuacion = result.getString("ecuacion");//Se reusa var ecuacion para almacenar el resultado

            Expression exp = new ExpressionBuilder(ecuacion)
                    .variables(nombres)
                    .build()
                    .setVariables(valores);

            ecuacion = validar.DobleFormatoStringCeil(exp.evaluate(), "#.##");
            valores.clear();
            return ecuacion;
        } catch (Exception ex) {
            log.error("Error en CalcularGeneral(),ecuación:" + ecuacion + ";" + ex.toString());
            return null;
        } finally {
            db.close(); //Se cierra la conexiòn
        }
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
