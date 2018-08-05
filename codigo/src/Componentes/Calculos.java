/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Componentes;

import DB.Dao;
import java.io.File;
import java.sql.ResultSet;
import java.util.Map;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.apache.log4j.Logger;
 
public class Calculos {
    
    static Logger log = Logger.getLogger("Calculos");
    private Configuracion conf = new Configuracion();
    private Validaciones valida = new Validaciones();
     private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    
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
    public String CalcularGeneral(String ecuacion, Map<String, Double> valores) { //todo: mover a utils

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

            ecuacion = valida.DobleFormatoStringCeil(exp.evaluate(), "#.##"); //todo: revisar porque 2
            valores.clear();
            return ecuacion;
        } catch (Exception ex) {
            log.error("Error en CalcularGeneral(),ecuación:" + ecuacion + ";" + ex.toString());
            return null;
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }
    
        /**
     * *
     * Recibe los parametros necesarios para traer de la BD una ecuacion, la
     * cual desgloza para evaluarla segun el parametro de entrada x, segun la
     * evaluación puede pasar a evaluar la formula obtenida o devolver
     * inmediatamente el resultado como cadena (ver ejemplo ej.)
     *
     * @param ecuacion : Nombre de la ecuacion en la bd
     * @param nombre : el nombre de la variable en la misma tabla de ecuaciones
     * @param x : valor a evaluar
     * @return
     */
    public String CalcularOtros(String ecuacion, String nombre, double x) {

        try {
            ResultSet result = db.ResultadoSelect("obtenerecuacion", ecuacion, nombre);
            ecuacion = "";
            String[] variables = result.getString("ecuacion").split(",");//Separa la ecuacion en subecuaciones por comas y las pasa a array ej. (50+C|0|10,20-C|1|30) --> ARRAY=(50+C|0|10)(20-C|1|30)
            for (int i = 0; i < variables.length; i++) {
                String nivel[] = variables[i].split("\\|"); //Separa la subecuaciones por pipe y se valida: x entre 0 y 10 , valida: x entre 1 y 30 , Si la primera validacion es true: ecuacion = 50+C

                if (valida.EsDobleEntre(valida.DobleAcadena(x), Double.parseDouble(nivel[1].trim()), Double.parseDouble(nivel[2].trim()))) {
                    ecuacion = nivel[0];
                    break;
                }
            }

            if (ecuacion.trim().contains("x")) { //La ecuacion contiene x, entonces se evalua, de lo contrario se devuelve resultado tal cual

                Expression e = new ExpressionBuilder(ecuacion) //ej. ecuacion = 50+C --> 50 + x
                        .variables("x")
                        .build()
                        .setVariable("x", x);

                ecuacion = valida.DobleAcadena(e.evaluate());
            }

            return ecuacion;
        } catch (Exception ex) {
            log.error("Error en CalcularOtros(),ecuacion:" + ecuacion + ";x: " + x + "; " + ex.toString());
            return null;
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }
}
