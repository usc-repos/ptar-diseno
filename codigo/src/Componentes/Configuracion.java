package Componentes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

public class Configuracion {//Agregar loj4 !!!!!!!!!!!!!!!!!!!!!!!!!!!!

    ResourceBundle properties;
    static Logger log = Logger.getLogger("Main");

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
            InputStream input = new FileInputStream(System.getProperty("user.dir") + File.separator + "src" + File.separator + "Resources" + File.separator + "config.properties");

            Properties prop = new Properties();
            prop.load(input);

            if (true) {
                return prop.getProperty(key);
            }
            //Antigua implementación
            // ResourceBundle properties = ResourceBundle.getBundle("resources.config"); 
            // if (properties.containsKey(key)) {
            // return (properties.getObject(key).toString()); 
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
            key = System.getProperty("user.dir") + File.separator + "src" + File.separator + ObtenerKey(key);

            return key;

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }
}
