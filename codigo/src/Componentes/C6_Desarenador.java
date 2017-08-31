package Componentes;

import BO.Bod;
import DB.Dao;
import java.io.File;
import java.sql.ResultSet;
import org.apache.log4j.Logger;

public class C6_Desarenador {

    private Bod bod = new Bod("");
    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("C6_Desarenador"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.ObtenerKey("rutabasedatos") + File.separator + conf.ObtenerKey("nombrebasedatos"));
    public String error = "";//Retorna el error de esta clase al inicializar
    //---------------------------- 
    public String errorDAN;
    public String errorDCK;
    public String errorDCN;
    public String errorDP1;
    public String errorDP2;
    public String errorDP3;
    public String errorDRZ;
    public String errorDAH;
    public String errorDAB;
    public String errorDVF;
    public String errorDLD;
    public String errorDAL;
    public String errorDEM;
    public String errorDFL;
    public String errorDUP;
    //------------------------------ 
    public double minDAN;
    public double maxDAN;
    public double minDCK;
    public double maxDCK;
    public double minDCN;
    public double maxDCN;
    public double minDP1;
    public double maxDP1;
    public double minDP2;
    public double maxDP2;
    public double minDP3;
    public double maxDP3;
    public double minDRZ;
    public double maxDRZ;
    public double minDAH;
    public double maxDAH;
    public double minDAB;
    public double maxDAB;
    public double minDVF;
    public double maxDVF;
    public double minDLD;
    public double maxDLD;
    public double minDAL;
    public double maxDAL;
    public double minDEM;
    public double maxDEM;
    public double minDUP;
    public double maxDUP;
    public int minDFL;
    public int maxDFL;
    
    public C6_Desarenador(Bod bod) {
        this.bod = bod;
    }

    /**
     * trae desde al base de datos todos los elementos de rangos.
     */
    public boolean InicializarComponentes() {

        try {
            ResultSet rs;
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DP1  
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DP1");

            minDP1 = rs.getDouble("rangomin");
            maxDP1 = rs.getDouble("rangomax");
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DP2  
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DP2");

            minDP2 = rs.getDouble("rangomin");
            maxDP2 = rs.getDouble("rangomax");
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DP3  
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DP3");

            minDP3 = rs.getDouble("rangomin");
            maxDP3 = rs.getDouble("rangomax");
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DRZ  
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DRZ");

            minDRZ = rs.getDouble("rangomin");
            maxDRZ = rs.getDouble("rangomax");
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DAH  
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DAH");

            minDAH = rs.getDouble("rangomin");
            maxDAH = rs.getDouble("rangomax");
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DAB  
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DAB");

            minDAB = rs.getDouble("rangomin");
            maxDAB = rs.getDouble("rangomax");
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DLD 
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DLD");

            minDLD = rs.getDouble("rangomin");
            maxDLD = rs.getDouble("rangomax");
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DAL 
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DAL");

            minDAL = rs.getDouble("rangomin");
            maxDAL = rs.getDouble("rangomax");
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DEM 
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DEM");

            minDEM = rs.getDouble("rangomin");
            maxDEM = rs.getDouble("rangomax");
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DUP 
            rs = db.ResultadoSelect("obtenerpregunta", "9", "DUP");

            minDUP = rs.getDouble("rangomin");
            maxDUP = rs.getDouble("rangomax");
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
     * Aquí unicamente se comprueban las fórmulas que tienen que ver con datos
     * anteriores
     *
     * @return true: si todo calculo y formula se hicieron sin problema
     */
    private boolean Guardar() {

        try {
            ResultSet rs;

            // - - - - - - Pregunta 4  - - - - - - - - - DP1 // Hmax. Profundidad de la lámina de agua (H) para Qmax, Qmed y Qmin.
            if (!bod.setDP1(bod.calcularDP1(0, 0), minDP1, maxDP1)) {
                rs = db.ResultadoSelect("obtenernombretitulo", "DP1", null);
                error = rs.getString("titulo1");
                return false;
            }
            // - - - - - - Pregunta 5  - - - - - - - - - DP2 // Hmed. Profundidad de la lámina de agua (H) para Qmax, Qmed y Qmin.
            if (!bod.setDP2(bod.calcularDP2(0, 0), minDP2, maxDP2)) {
                rs = db.ResultadoSelect("obtenernombretitulo", "DP2", null);
                error = rs.getString("titulo1");
                return false;
            }
            // - - - - - - Pregunta 6  - - - - - - - - - DP3 // Hmin. Profundidad de la lámina de agua (H) para Qmax, Qmed y Qmin.
            if (!bod.setDP3(bod.calcularDP3(0, 0), minDP3, maxDP3)) {
                rs = db.ResultadoSelect("obtenernombretitulo", "DP3", null);
                error = rs.getString("titulo1");
                return false;
            }
            // - - - - - - Pregunta 7  - - - - - - - - - DRZ //Resalto (Z). Profundidad de la lámina de agua (H) para Qmax, Qmed y Qmin.
            if (!bod.setDRZ(bod.calcularDRZ(0, 0), minDRZ, maxDRZ)) {
                rs = db.ResultadoSelect("obtenernombretitulo", "DRZ", null);
                error = rs.getString("titulo1");
                return false;
            }
            // - - - - - - Pregunta 17 - - - - - - - - - DAH // Altura máxima de lámina de agua en el desarenador (H)
            if (!bod.setDAH(bod.calcularDAH(0, 0), minDAH, maxDAH)) {
                rs = db.ResultadoSelect("obtenernombretitulo", "DAH", null);
                error = rs.getString("titulo1");
                return false;
            }
            // - - - - - - Pregunta 18 - - - - - - - - - DAB // Ancho del desarenador (b)
            if (!bod.setDAB(bod.calcularDAB(0, 0), minDAB, maxDAB)) {
                rs = db.ResultadoSelect("obtenernombretitulo", "DAB", null);
                error = rs.getString("titulo1");
                return false;
            }
            // - - - - - - Pregunta 20- - - - - - - - - DLD // Longitud del desarenador (L)
            if (!bod.setDLD(bod.calcularDLD(0), minDLD, maxDLD)) {
                rs = db.ResultadoSelect("obtenernombretitulo", "DLD", null);
                error = rs.getString("titulo1");
                return false;
            }
            // - - - - - - Pregunta 21 - - - - - - - - - DAL // Área longitudinal del desarenador (A)
            if (!bod.setDAL(bod.calcularDAL(0, 0), minDAL, maxDAL)) {
                rs = db.ResultadoSelect("obtenernombretitulo", "DAL", null);
                error = rs.getString("titulo1");
                return false;
            }
            // - - - - - - Pregunta 22 - - - - - - - - - DEM // Estimación de material retenido (q)
            if (!bod.setDEM(bod.calcularDEM(), minDEM, maxDEM)) {
                rs = db.ResultadoSelect("obtenernombretitulo", "DEM", null);
                error = rs.getString("titulo1");
                return false;
            }
            // - - - - - - Pregunta 24 - - - - - - - - - DUP // Profundidad útil depósito inferior de arena (p)
            if (!bod.setDUP(bod.calcularDUP(0, 0, 0), minDUP, maxDUP)) {
                rs = db.ResultadoSelect("obtenernombretitulo", "DUP", null);
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
