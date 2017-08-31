package Componentes;

import BO.Bod;
import DB.Dao;
import java.io.File;
import java.sql.ResultSet;
import org.apache.log4j.Logger;

public class C8_LagunaFacultativaSec {

    private Bod bod = new Bod("");
    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("C6_LagunaFacultativaSec"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.ObtenerKey("rutabasedatos") + File.separator + conf.ObtenerKey("nombrebasedatos"));
    public String error = "";//Retorna el error de esta clase al inicializar

    public C8_LagunaFacultativaSec(Bod bod) {
        this.bod = bod;
    }

    /**
     * trae desde al base de datos todos los elementos de rangos necesarios.
     */
    public boolean InicializarComponentes() {

        try {
            ResultSet rs;

            rs = db.ResultadoSelect("obtenerpregunta", "6", "FCO");

            bod.minFCO = rs.getDouble("rangomin");
            bod.maxFCO = rs.getDouble("rangomax");
            // - - - - - - Pregunta 6 Cuestionario 6 - - - - - - - - - - - FCA
            rs = db.ResultadoSelect("obtenerpregunta", "6", "FCA");

            bod.minFCA = rs.getDouble("rangomin");
            bod.maxFCA = rs.getDouble("rangomax");
            // - - - - - - Pregunta 7 Cuestionario 6 - - - - - - - - - - - FAS
            rs = db.ResultadoSelect("obtenerpregunta", "6", "FAS");

            bod.minFAS = rs.getDouble("rangomin");
            bod.maxFAS = rs.getDouble("rangomax");
            // - - - - - - Pregunta 8 Cuestionario 6 - - - - - - - - - - - FTR
            rs = db.ResultadoSelect("obtenerpregunta", "6", "FTR");

            bod.minFTR = rs.getDouble("rangomin");
            bod.maxFTR = rs.getDouble("rangomax");
            // - - - - - - Pregunta 9 Cuestionario 6 - - - - - - - - - - - FED
            rs = db.ResultadoSelect("obtenerpregunta", "6", "FED");

            bod.minFED = rs.getDouble("rangomin");
            bod.maxFED = rs.getDouble("rangomax");
            // - - - - - - Pregunta 10 Cuestionario 6 - - - - - - - - - - - FUV
            rs = db.ResultadoSelect("obtenerpregunta", "6", "FUV");

            bod.minFUV = rs.getDouble("rangomin");
            bod.maxFUV = rs.getDouble("rangomax");
            // - - - - - - Pregunta 11 Cuestionario 6 - - - - - - - - - - - FSA
            rs = db.ResultadoSelect("obtenerpregunta", "6", "FSA");

            bod.minFSA = rs.getDouble("rangomin");
            bod.maxFSA = rs.getDouble("rangomax");
            // - - - - - - Pregunta 12 Cuestionario 6 - - - - - - - - - - - FSL
            rs = db.ResultadoSelect("obtenerpregunta", "6", "FSL");

            bod.minFSL = rs.getDouble("rangomin");
            bod.maxFSL = rs.getDouble("rangomax");
            // - - - - - - Pregunta 15 Cuestionario 6 - - - - - - - - - - - FAB
            rs = db.ResultadoSelect("obtenerpregunta", "6", "FAB");

            bod.minFAB = rs.getDouble("rangomin");
            bod.maxFAB = rs.getDouble("rangomax");
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

    private boolean Guardar() {//Aquí unicamente se comprueban las fórmulas que tienen que ver con datos anteriores

        try {
            ResultSet rs;

            if (!bod.setFCO(bod.CalcularFCO(), bod.minFCO, bod.maxFCO)) {// Carga Orgánica Volumétrica (COVLF) g/m³*Día 
                rs = db.ResultadoSelect("obtenernombretitulo", "FCO", null);
                error = rs.getString("titulo1");
                return false;
            }

            if (!bod.setFAS(bod.CalcularFAS(0), bod.minFAS, bod.maxFAS)) {// Área Superficial (ASLF) m²
                rs = db.ResultadoSelect("obtenernombretitulo", "FAS", null);
                error = rs.getString("titulo1");
                return false;
            }

            if (!bod.setFTR(bod.CalcularFTR(0, 0, 0), bod.minFTR, bod.maxFTR)) {// Tiempo de retención hidráulico (TRLF) días
                rs = db.ResultadoSelect("obtenernombretitulo", "FTR", null);
                error = rs.getString("titulo1");
                return false;
            }

            if (!bod.setFED(bod.CalcularFED(0), bod.minFED, bod.maxFED)) {// Eficiencia de DBO5 estimada %LF 
                rs = db.ResultadoSelect("obtenernombretitulo", "FED", null);
                error = rs.getString("titulo1");
                return false;
            }

            if (!bod.setFUV(bod.CalcularFUV(0), bod.minFUV, bod.maxFUV)) {// Volumen Útil (VLA) m³ 
                rs = db.ResultadoSelect("obtenernombretitulo", "FUV", null);
                error = rs.getString("titulo1");
                return false;
            }

            if (!bod.setFSA(bod.CalcularFSA(0), bod.minFSA, bod.maxFSA)) {// Ancho (aLF) m Relación Largo: Ancho 
                rs = db.ResultadoSelect("obtenernombretitulo", "FSA", null);
                error = rs.getString("titulo1");
                return false;
            }

            if (!bod.setFSL(bod.CalcularFSL(0), bod.minFSL, bod.maxFSL)) {// Ancho (aLF) m Relación Largo: Ancho 
                rs = db.ResultadoSelect("obtenernombretitulo", "FSL", null);
                error = rs.getString("titulo1");
                return false;
            }

            if (!bod.setFAB(bod.calcularFAB(0), bod.minFAB, bod.maxFAB)) {// Ángulo de inclinación de la pendiente α
                rs = db.ResultadoSelect("obtenernombretitulo", "FAB", null);
                error = rs.getString("titulo1");
                return false;
            }

            if (!bod.GuardarUpdateBod()) {
                bod.EditLagunaFacultativaSec = false;
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
