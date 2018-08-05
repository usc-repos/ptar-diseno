package Componentes;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

public class Configuracion {

    public String separador = "/";
    public String pathreal = "";
    ResourceBundle properties;
    static Logger log = Logger.getLogger("Main");

    public Configuracion() {

        if (System.getProperty("user.dir").contains("\\")) { //El constructor define el separador de rutas
            separador = "\\";
            pathreal = System.getProperty("user.dir");//Windows
        } else {
            pathreal = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();//Linux
            pathreal = pathreal.substring(0, pathreal.lastIndexOf("/"));
        }
    }

    /**
     * *
     * Recibe una peticion en cadena, la cual busca en el archivo de
     * configuraciones y devuelve la cadena encontrada
     *
     * @param key cadena por encontrar
     * @return cadena encontrada
     */
    public String ObtenerKey(String key) {

        try {
            InputStream input = new FileInputStream(pathreal + separador + "src" + separador + "Resources" + separador + "config.properties");

            Properties prop = new Properties();
            prop.load(input);

            if (true) {
                return prop.getProperty(key);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    /**
     * *
     * Recibe una peticion en cadena, la cual busca en el archivo de
     * configuraciones y devuelve la cadena encontrada pero concatenada con la
     * ruta general de los archivos
     *
     * @param key cadena por encontrar
     * @return cadena encontrada
     */
    public String ObtenerRutaGeneral(String key) {

        try {
            key = pathreal + separador + "src" + separador + ObtenerKey(key);

            return key;

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }
}
