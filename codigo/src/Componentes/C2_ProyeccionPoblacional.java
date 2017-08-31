package Componentes;

import BO.Bod;
import DB.Dao;
import java.io.File;
import java.sql.ResultSet;
import org.apache.log4j.Logger;

public class C2_ProyeccionPoblacional {

    private Bod bod = new Bod("");
    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("C2_ProyeccionPoblacional"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.ObtenerKey("rutabasedatos") + File.separator + conf.ObtenerKey("nombrebasedatos"));
    private Validaciones validar = new Validaciones();
    public String error = "";//Retorna el error de esta clase al inicializar

    public C2_ProyeccionPoblacional(Bod bod) {
        this.bod = bod;
    }

    /**
     * trae desde al base de datos todos los elementos de rangos.
     */
    public boolean InicializarComponentes() {

        try {
            ResultSet rs;
            // - - - - - - Pregunta 3 - - - - - - -  - - - - - - - - - - - TCA
            rs = Resultado("obtenerpregunta", "2", "TCA");//Todo: ¿porque resutado se usa en vez de ResultadoSelect?

            bod.minTCA = rs.getDouble("rangomin");
            bod.maxTCA = rs.getDouble("rangomax");
            // - - - - - - Pregunta 3 - - - - - - -  - - - - - - - - - - - TCG
            rs = Resultado("obtenerpregunta", "2", "TCG");

            bod.minTCG = rs.getDouble("rangomin");
            bod.maxTCG = rs.getDouble("rangomax");
            // - - - - - - Pregunta 4 - - - - - - -  - - - - - - - - - - - PPA 
            rs = Resultado("obtenerpregunta", "2", "PPA");

            bod.minPPA = rs.getInt("rangomin");
            bod.maxPPA = rs.getInt("rangomax");
            // - - - - - - Pregunta 5 - - - - - - -  - - - - - - - - - - - PPG
            rs = Resultado("obtenerpregunta", "2", "PPG");

            bod.minPPG = rs.getInt("rangomin");
            bod.maxPPG = rs.getInt("rangomax");

            db.close(); //Se cierra la conexiòn

            if (Guardar()) {
                return true;
            }
            
        } catch (Exception ex) {
            error = ex.getMessage();
            log.error("Error en InicializarComponentes " + ex.getMessage());
            return false;
        } finally {
            db.close(); //Se cierra la conexiòn de guardar()        
        }
        return false;
    }

    private ResultSet Resultado(String consulta, String param1, String param2) throws Exception {

        String select = conf.ObtenerKey(consulta);

        switch (consulta) {
            case "obtenerseccion":
                select = String.format(select, param1); //Cuestionario 1, pregunta 2
                break;
            case "obtenerpregunta":
                select = String.format(select, param1, param2); //Cuestionario 1, pregunta 1,2,3,....
                break;
        }

        ResultSet result = db.SelectSql(select);
        if (result == null) {
            throw new Exception("Error al traer Resultado = null," + consulta + "," + param1 + "," + param2);
        }
        return result;
    }

    private boolean Guardar() {//Aquí unicamente se comprueban las fórmulas que tienen que ver con datos anteriores

        try {
            ResultSet rs;

            if (bod.getMEA() > 0) {

                if (!bod.setTCA(bod.CalcularTCA(), bod.minTCA, bod.maxTCA)) {
                    rs = db.ResultadoSelect("obtenernombretitulo", "TCA", null);
                    error = rs.getString("titulo1");
                    return false;
                }

                double temp = (int) Double.parseDouble(bod.CalcularPPA());
                if (!bod.setPPA(validar.DobleAcadena(temp), bod.minPPA, bod.maxPPA)) {
                    rs = db.ResultadoSelect("obtenernombretitulo", "PPA", null);
                    error = rs.getString("titulo1");
                    return false;
                }

                if (!bod.setNCR(bod.CalcularNCR(bod.getPPA()))) {
                    rs = db.ResultadoSelect("obtenernombretitulo", "NCR", null);
                    error = rs.getString("titulo1");
                    return false;
                }
            }

            if (bod.getMEG() > 0) {

                if (!bod.setTCG(bod.CalcularTCG(), bod.minTCG, bod.maxTCG)) {
                    rs = db.ResultadoSelect("obtenernombretitulo", "TCG", null);
                    error = rs.getString("titulo1");
                    return false;
                }

                double temp = (int) Double.parseDouble(bod.CalcularPPG());
                if (!bod.setPPG(validar.DobleAcadena(temp), bod.minPPG, bod.maxPPG)) {
                    rs = db.ResultadoSelect("obtenernombretitulo", "PPG", null);
                    error = rs.getString("titulo1");
                    return false;
                }

                if (!bod.setNCR(bod.CalcularNCR(bod.getPPG()))) {
                    rs = db.ResultadoSelect("obtenernombretitulo", "NCR", null);
                    error = rs.getString("titulo1");
                    return false;
                }
            }

            if (!bod.GuardarUpdateBod()) {
                bod.EditProyeccionPoblacional = false;
                return false;
            }
            return true;

        } catch (Exception ex) {
            error = ex.getMessage();
            log.error("Error en Guardar() " + ex.getMessage());
            return false;
        }
    }
}
