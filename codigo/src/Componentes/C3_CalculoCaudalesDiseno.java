package Componentes;

import BO.Bod;
import DB.Dao;
import java.io.File;
import java.sql.ResultSet;
import org.apache.log4j.Logger;

public class C3_CalculoCaudalesDiseno {

    private Bod bod = new Bod("");
    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("C3_CalculoCaudalesDiseno"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.ObtenerKey("rutabasedatos") + File.separator + conf.ObtenerKey("nombrebasedatos"));
    public String error = "";//Retorna el error de esta clase al inicializar

    public C3_CalculoCaudalesDiseno(Bod bod) {
        this.bod = bod;
    }

    /**
     * trae desde al base de datos todos los elementos de rangos.
     */
    public boolean InicializarComponentes() {

        try {
            ResultSet rs;
            // - - - - - - Pregunta 1 - - - - - - - - - - - - - - - - - - - CAR
            rs = db.ResultadoSelect("obtenerpregunta", "3", "CAR");

            bod.minCAR = rs.getDouble("rangomin");
            bod.maxCAR = rs.getDouble("rangomax");
            // - - - - - - Pregunta 2 - - - - - - - - - - - - - - - - - - - KAR
            rs = db.ResultadoSelect("obtenerpregunta", "3", "KAR");

            bod.minKAR = rs.getDouble("rangomin");
            bod.maxKAR = rs.getDouble("rangomax");
            // - - - - - - Pregunta 19 - - - - - - - - - - - - - - - - - - QNC
            rs = db.ResultadoSelect("obtenerpregunta", "3", "QNC");

            bod.minQNC = rs.getDouble("rangomin");
            bod.maxQNC = rs.getDouble("rangomax");
            // - - - - - - Pregunta 20 - - - - - - - - - - - - - - - - - - QNK
            rs = db.ResultadoSelect("obtenerpregunta", "3", "QNK");

            bod.minQNK = rs.getDouble("rangomin");
            bod.maxQNK = rs.getDouble("rangomax");
            // - - - - - - Pregunta 45 - - - - - - - - - - - - - - - - - - Q2C
            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q2C");

            bod.minQ2C = rs.getDouble("rangomin");
            bod.maxQ2C = rs.getDouble("rangomax");
            // - - - - - - Pregunta 46 - - - - - - - - - - - - - - - - - - Q2K
            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q2K");

            bod.minQ2K = rs.getDouble("rangomin");
            bod.maxQ2K = rs.getDouble("rangomax");
            // - - - - - - Pregunta 47 - - - - - - - - - - - - - - - - - - Q3M
            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q3M");

            bod.minQ3M = rs.getDouble("rangomin");
            bod.maxQ3M = rs.getDouble("rangomax");
            bod.errorQ3M = rs.getString("errormsg");
            // - - - - - - Pregunta 49 - - - - - - - - - - - - - - - - - - Q3C
            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q3C");

            bod.minQ3C = rs.getDouble("rangomin");
            bod.maxQ3C = rs.getDouble("rangomax");
            // - - - - - - Pregunta 50 - - - - - - - - - - - - - - - - - - Q3K
            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q3K");

            bod.minQ3K = rs.getDouble("rangomin");
            bod.maxQ3K = rs.getDouble("rangomax");
            // - - - - - - Pregunta 52 - - - - - - - - - - - - - - - - - - Q1C
            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q1C");

            bod.minQ1C = rs.getDouble("rangomin");
            bod.maxQ1C = rs.getDouble("rangomax");
            // - - - - - - Pregunta 53 - - - - - - - - - - - - - - - - - - Q1K
            rs = db.ResultadoSelect("obtenerpregunta", "3", "Q1K");

            bod.minQ1K = rs.getDouble("rangomin");
            bod.maxQ1K = rs.getDouble("rangomax");
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

    private boolean Guardar() {//Aqui unicamente se comprueban las fórmulas que tienen que ver con datos anteriores

        try {
            ResultSet rs; //Todo: usar este sistema de traer nombre en UIs

            if (!bod.setCAR(bod.CalcularCAR(), bod.minCAR, bod.maxCAR)) {
                rs = db.ResultadoSelect("obtenernombretitulo", "CAR", null);
                error = rs.getString("titulo1");
                return false;
            }

            if (!bod.setKAR(bod.CalcularKAR(), bod.minKAR, bod.maxKAR)) {
                rs = db.ResultadoSelect("obtenernombretitulo", "CAR", null); //  este lleva el mismo del anterior CAR
                error = rs.getString("titulo1") + " m³/d";
                return false;
            }
            //- - - - - - - - - - - - - - - - - - - - - - - - INICIA Caudal por conexiones erradas  

            if (bod.getQCD() > 0) {

                if (bod.getQCE() > 0) {
                } else {

                    if (!bod.setQNC(bod.CalcularQNC(), bod.minQNC, bod.maxQNC)) {
                        rs = db.ResultadoSelect("obtenernombretitulo", "QNC", null);
                        error = rs.getString("titulo1");
                        return false;
                    }

                    if (!bod.setQNK(bod.CalcularQNK(), bod.minQNK, bod.maxQNK)) {
                        rs = db.ResultadoSelect("obtenernombretitulo", "QNC", null); //  este lleva el mismo del anterior QNC 
                        error = rs.getString("titulo1") + " m³/d";
                        return false;
                    }
                }
            }

            // - - - - - - - - - - - - - - - - - - - - - - - - INICIO Caudal medio diario 
            if (!bod.setQ2C(bod.CalcularQ2C(0, 0, 0, 0, 0, 0), bod.minQ2C, bod.maxQ2C)) {//Caudal medio diario  
                rs = db.ResultadoSelect("obtenernombretitulo", "Q2C", null);
                error = rs.getString("titulo1");
                return false;
            }

            if (!bod.setQ2K(bod.CalcularQ2K(0, 0, 0, 0, 0, 0), bod.minQ2K, bod.maxQ2K)) {//Caudal medio diario
                rs = db.ResultadoSelect("obtenernombretitulo", "Q2C", null); //  este lleva el mismo del anterior Q2C 
                error = rs.getString("titulo1") + " m³/d";
                return false;
            }

            if (!bod.setQ3M(bod.calcularQ3M(), bod.minQ3M, bod.maxQ3M)) { //Caudal máximo diario
                rs = db.ResultadoSelect("obtenernombretitulo", "Q3M", null);
                error = rs.getString("titulo1");
                return false;
            }

            if (!bod.setQ3C(bod.CalcularQ3C(0, 0, 0, 0, 0, 0, 0), bod.minQ3C, bod.maxQ3C)) {//Caudal máximo diario
                rs = db.ResultadoSelect("obtenernombretitulo", "Q3C", null);
                error = rs.getString("titulo1");
                return false;
            }

            if (!bod.setQ3K(bod.CalcularQ3K(0, 0, 0, 0, 0, 0, 0), bod.minQ3K, bod.maxQ3K)) {//Caudal máximo diario
                rs = db.ResultadoSelect("obtenernombretitulo", "Q3K", null);
                error = rs.getString("titulo1");
                return false;
            }

            if (!bod.setQ1C(bod.CalcularQ1C(0, 0, 0, 0, 0, 0), bod.minQ1C, bod.maxQ1C)) {//Caudal mínimo diario
                rs = db.ResultadoSelect("obtenernombretitulo", "Q1C", null);
                error = rs.getString("titulo1");
                return false;
            }

            if (!bod.setQ1K(bod.CalcularQ1K(0, 0, 0, 0, 0, 0), bod.minQ1K, bod.maxQ1K)) {//Caudal mínimo diario
                rs = db.ResultadoSelect("obtenernombretitulo", "Q1C", null);//  este lleva el mismo del anterior Q1C 
                error = rs.getString("titulo1") + " m³/d";
                return false;
            }

            if (!bod.GuardarUpdateBod()) {
                bod.EditCalculoCaudalesDiseno = false;
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
