package Componentes;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.log4j.Logger;

public class Util {

    private static Logger log = Logger.getLogger("AgregarMensajePopup");

    //Abre una URI si es posible en el navegador por defecto.
    public static void AbrirUri(String uri) {

        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(new URI(uri));
            } catch (Exception ex) {
                log.error("Error AbrirUri() " + ex.getMessage());
                //todo: implementar ex  + Java no permite guines en link (-)
            }
        }
    }

    /**
     * Trata de abrir un archivo de extensón .PTAR y devuelve el dato leido
     *
     * @return : return "error" si ha ocurrido un error o Null si se cancela la
     * apertura
     */
    public String AbrirArchivoPtar() {

        BufferedReader buffread = null;
        String line;
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("todos los archivos *.PTAR", "ptar", "PTAR"));//filtro para ver solo archivos .ptar
            File file = null;

            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();

                if (!file.canRead() || !file.exists() || !file.getName().toLowerCase().endsWith(".ptar")) {
                    return "error";
                }
            } else {
                return null;
            }

            buffread = new BufferedReader(new FileReader(file));

            while ((line = buffread.readLine()) != null) {
                break;
            }

            //if(line.contains(""))//Todo: implementar codificacion

            return line;
        } catch (Exception ex) {
            log.error("Error AbrirArchivo() " + ex.getMessage());
        } finally {
            try {
                buffread.close();

            } catch (IOException ex) {
            }
        }
        return "error";
    }

    /**
     * *
     * Devuelve la cadena de un dialogo de crear archivo
     *
     * @param msg
     * @return
     */
    public String CrearArchivo() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            //fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("todos los archivos *.PTAR", "ptar", "PTAR"));//filtro para ver solo archivos .ptar
            int seleccion = fileChooser.showSaveDialog(null);
            File file = null;

            if (seleccion == JFileChooser.APPROVE_OPTION) {//ha presionado el boton aceptar
                file = fileChooser.getSelectedFile();
                String ruta = file.getAbsolutePath().trim();
                if (!ruta.equals("")) {
                    return ruta;
                }
            }
        } catch (Exception ex) {
            log.error("Error CrearArchivo() " + ex.getMessage());
        }
        return "";
    }

    public boolean CrearArchivoPtar(String msg) {//Todo: unir con CrearArchivoPtar()
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("todos los archivos *.PTAR", "ptar", "PTAR"));//filtro para ver solo archivos .ptar
            int seleccion = fileChooser.showSaveDialog(null);
            File file = null;

            if (seleccion == JFileChooser.APPROVE_OPTION) {//ha presionado el boton aceptar
                file = fileChooser.getSelectedFile();
                if (EscribirArchivo(msg, file)) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception ex) {
            log.error("Error CrearArchivoPtar() " + ex.getMessage());
        }
        return false;
    }

    public boolean EscribirArchivo(String msg, File file) { //TODO: mensaje de overwrite!
        try {
            String ruta = file.getAbsolutePath();
            
            if (!obtenerExtension(ruta, ".ptar")) {
                ruta = ruta + ".ptar";
            }

            FileWriter filewriter = new FileWriter(ruta);
            filewriter.write(msg);
            filewriter.close();
            return true;

        } catch (Exception ex) {
            log.error("Error EscribirArchivo() " + ex.getMessage());
        }
        return false;
    }

    public int Mensaje(String msg, String tipo) {

        int val = 0;

        switch (tipo) {
            case "ok":
                JOptionPane.showMessageDialog(null, msg, "Información", JOptionPane.PLAIN_MESSAGE);
                break;
            case "error":
                JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.WARNING_MESSAGE);
                break;
            case "yesno":
                val = JOptionPane.showConfirmDialog(null, msg, "Información!", JOptionPane.YES_NO_OPTION);
                break;
            default:
                JOptionPane.showMessageDialog(null, msg, "Información", JOptionPane.DEFAULT_OPTION);
                break;
        }
        return val;
    }

    /***
     * Devuelve true o false si la cadena tiene la extension dada
     * @param cadena
     * @param ext ".jpg"
     * @return 
     */
    public boolean obtenerExtension(String cadena, String ext) {

        if (!cadena.contains(".")) {
            return false;
        }

        String subs = cadena.substring(cadena.lastIndexOf("."), cadena.length()).toLowerCase();

        if (subs.toLowerCase().equals(ext)) {
            return true;
        }
        return false;
    }
}
