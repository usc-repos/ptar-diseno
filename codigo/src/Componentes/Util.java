package Componentes;

import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.log4j.Logger;

public class Util {

    private static Logger log = Logger.getLogger("AgregarMensajePopup");
    public String pathPtar;

    //Abre una URI si es posible en el navegador por defecto.
    public static void AbrirUri(String uri) {

        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(new URI(uri));
            } catch (Exception ex) {
                log.error("Error AbrirUri() " + ex.getMessage());
                //todo: implementar ex  + Java no permite guiones en links (-)
            }
        }
    }

    /**
     * *
     * Abre la interfaz de 'guardar archivo' y devuelve la cadena con la ruta
     * seleccionada del archivo
     *
     * @param msg
     * @return
     */
    public String dialogoCrearArchivo() {
        try {
            JFileChooser fileChooser = new JFileChooser();
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

    /**
     * *
     * Devuelve true o false si la cadena tiene la extension dada
     *
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

    /**
     * *
     * Realiza una codificación tipo xor utilizando una llave, que devuelve un
     * arreglo de enteros (codificados) devueltos en una cadena
     *
     * @param msg cadena sin codificar
     * @param key llave para codificar
     * @return
     * @throws Exception
     */
    public static String encriptar(String msg, String key) throws Exception {

        String stout = "";
        try {
            for (int i = 0; i < msg.length(); i++) {
                int o = (Integer.valueOf(msg.charAt(i)) ^ Integer.valueOf(key.charAt(i % (key.length() - 1)))) + '0';
                stout += o + ",";
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return stout;
    }

    /**
     * *
     * Realiza una decodificación utilizando una llave, se devuelve una cadena
     *
     * @param msg cadena codificada
     * @param key llave para decodificar
     * @return
     * @throws Exception
     */
    public static String desEncriptar(String msg, String key) throws Exception {

        int x;
        String dencripMsg = "";
        try {
            String[] sarr = msg.split(",");
            int[] out = new int[sarr.length];

            for (int i = 0; i < out.length; i++) {
                x = Integer.valueOf(sarr[i]);
                dencripMsg += (char) ((x - 48) ^ (int) key.charAt(i % (key.length() - 1)));
            }

        } catch (Exception e) {
            throw new Exception(e);
        }
        return dencripMsg;
    }

    /**
     * Abre la interfaz de 'guardar archivo' para luego escribir la información
     * en un archivo de extensión PTAR
     *
     * @param msg
     * @param opcionalPath puede que este parametro llegue vacío si no se desea
     * enviar la ruta del proyecto
     * @return
     */
    public boolean CrearArchivoPtar(String msg, String opcionalPath) {//Todo: unir con CrearArchivoPtar()
        try {
            File file = null;

            if (opcionalPath.isEmpty()) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("todos los archivos *.PTAR", "ptar", "PTAR"));//filtro para ver solo archivos .ptar
                int seleccion = fileChooser.showSaveDialog(null);

                if (seleccion != JFileChooser.APPROVE_OPTION) {
                    return false;
                } else {//ha presionado el boton aceptar
                    file = fileChooser.getSelectedFile();
                    String path = file.getAbsolutePath();

                    if (!path.toUpperCase().endsWith(".PTAR")) { //Se comprueba la extensión, si no está se agrega
                        path += ".ptar";
                        file = new File(path);
                    }
                }
            } else {
                file = new File(opcionalPath);
            }

            Charset charset = Charset.forName("ISO-8859-1");
            try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), charset)) {
                writer.write(msg, 0, msg.length());
            }
            pathPtar = file.getAbsolutePath();
            return true;

        } catch (HeadlessException | IOException ex) {
            log.error("Error CrearArchivoPtar() " + ex.getMessage());
        }
        return false;
    }

    /**
     * Trata de abrir un archivo de extensón .PTAR y devuelve el dato leido
     *
     * @return : return "error" o "" si ha ocurrido un error o Null si se
     * cancela la apertura
     */
    public String AbrirArchivoPtar() {

        String str;
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("todos los archivos *.PTAR", "ptar", "PTAR"));//filtro para ver solo archivos .ptar
            File file = null;

            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();

                if (!file.canRead() || !file.exists() || !file.getName().toLowerCase().endsWith(".ptar")) {
                    return "error";
                }

                byte[] biteRead = Files.readAllBytes(file.toPath());
                str = new String(biteRead, "ISO-8859-1");
            } else {
                return null;
            }
            pathPtar = file.getAbsolutePath();
            return str;
        } catch (HeadlessException | IOException ex) {
            log.error("Error AbrirArchivo() " + ex.getMessage());
        }
        return "error";
    }
}
