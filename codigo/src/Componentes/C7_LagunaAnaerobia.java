package Componentes;

import BO.Bod;
import DB.Dao;
import java.io.File;
import java.sql.ResultSet;
import org.apache.log4j.Logger;

public class C7_LagunaAnaerobia {

    private Bod bod = new Bod("");
    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("C4_CaractInicAguaResidual"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.ObtenerKey("rutabasedatos") + File.separator + conf.ObtenerKey("nombrebasedatos"));
    public String error = "";//Retorna el error de esta clase al inicializar

    public C7_LagunaAnaerobia(Bod bod) {
        this.bod = bod;
    }

    /**
     * trae desde al base de datos todos los elementos de rangos.
     */
    public boolean InicializarComponentes() {

        try {
            ResultSet rs;
            // - - - - - - Pregunta 4 Cuestionario 5 - - - - - - - - - - - LCO
            rs = db.ResultadoSelect("obtenerpregunta", "5", "LCO"); //Todo: no es necesario 'AsignarTextDoble' y otros, verificar en transcurso

            bod.minLCO = rs.getDouble("rangomin");
            bod.maxLCO = rs.getDouble("rangomax");
            // - - - - - - Pregunta 5 Cuestionario 5 - - - - - - - - - - - LCE
            rs = db.ResultadoSelect("obtenerpregunta", "5", "LCE");

            bod.minLCE = rs.getDouble("rangomin");
            bod.maxLCE = rs.getDouble("rangomax");
            // - - - - - - Pregunta 6 Cuestionario 5 - - - - - - - - - - - LAE
            rs = db.ResultadoSelect("obtenerpregunta", "5", "LAE");

            bod.minLAE = rs.getDouble("rangomin");
            bod.maxLAE = rs.getDouble("rangomax");
            // - - - - - - Pregunta 7 Cuestionario 5 - - - - - - - - - - - LVU
            rs = db.ResultadoSelect("obtenerpregunta", "5", "LVU");

            bod.minLVU = rs.getDouble("rangomin");
            bod.maxLVU = rs.getDouble("rangomax");
            // - - - - - - Pregunta 8 Cuestionario 5 - - - - - - - - - - - LTH
            rs = db.ResultadoSelect("obtenerpregunta", "5", "LTH");

            bod.minLTH = rs.getDouble("rangomin");
            bod.maxLTH = rs.getDouble("rangomax");
            // - - - - - - Pregunta 9 Cuestionario 5 - - - - - - - - - - - LVR
            rs = db.ResultadoSelect("obtenerpregunta", "5", "LVR");

            bod.minLVR = rs.getDouble("rangomin");
            bod.maxLVR = rs.getDouble("rangomax");
            // - - - - - - Pregunta 11 Cuestionario 5 - - - - - - - - - - - LAS
            rs = db.ResultadoSelect("obtenerpregunta", "5", "LAS");

            bod.minLAS = rs.getDouble("rangomin");
            bod.maxLAS = rs.getDouble("rangomax");
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  
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

    /**
     * Aquí unicamente se comprueban las fórmulas que tienen que ver con datos anteriores
     * @return true: si todo calculo y formula se hicieron sin problema
     */
    private boolean Guardar() {

        try {
            ResultSet rs;

            if (!bod.setLCO(bod.CalcularLCO(0), bod.minLCO, bod.maxLCO)) {// Carga Orgánica Volumétrica (COV LA) g/m³*Día 
                rs = db.ResultadoSelect("obtenernombretitulo", "LCO", null);
                error = rs.getString("titulo1");
                return false;
            }

            if (!bod.setLCE(bod.CalcularLCE(0), bod.minLCE, bod.maxLCE)) {// Eficiencia de DBO5 estimada %LA Calculo de Eficiencia y Efluente de DBO5
                rs = db.ResultadoSelect("obtenernombretitulo", "LCE", null);
                error = rs.getString("titulo1");
                return false;
            }

            if (!bod.setLAE(bod.CalcularLAE(0), bod.minLAE, bod.maxLAE)) {// DBO5 en el efluente de la laguna Anaerobia (SLA) mg/L  
                rs = db.ResultadoSelect("obtenernombretitulo", "LAE", null);
                error = rs.getString("titulo1");
                return false;
            }

            if (!bod.setLVU(bod.CalcularLVU(0), bod.minLVU, bod.maxLVU)) {// Volumen Útil (VLA) m³ 
                rs = db.ResultadoSelect("obtenernombretitulo", "LVU", null);
                error = rs.getString("titulo1");
                return false;
            }

            if (!bod.setLTH(bod.CalcularLTH(0), bod.minLTH, bod.maxLTH)) {// Tiempo de retención hidráulico (TRLA) días 
                rs = db.ResultadoSelect("obtenernombretitulo", "LTH", null);
                error = rs.getString("titulo1");
                return false;
            }

            if (!bod.setLVR(bod.CalcularLVR(0), bod.minLVR, bod.maxLVR)) {// Volumen Útil Recalculado (VLA) m³ 
                rs = db.ResultadoSelect("obtenernombretitulo", "LVR", null);
                error = rs.getString("titulo1");
                return false;
            }

            if (!bod.setLAS(bod.CalcularLAS(0, 0), bod.minLAS, bod.maxLAS)) {// Área Superficial m²
                rs = db.ResultadoSelect("obtenernombretitulo", "LAS", null);
                error = rs.getString("titulo1");
                return false;
            }

            if (!bod.GuardarUpdateBod()) {
                bod.EditLagunaAnaerobia = false;
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
