package Componentes;

import BO.Bod;
import DB.Dao;
import java.io.File;
import java.sql.ResultSet;
import org.apache.log4j.Logger;

public class C5_Rejillas {

    private Bod bod = new Bod("");
    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("C5_Rejillas"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.ObtenerKey("rutabasedatos") + File.separator + conf.ObtenerKey("nombrebasedatos"));
    public String error = "";//Retorna el error de esta clase al inicializar

    public C5_Rejillas(Bod bod) {
        this.bod = bod;
    }

    /**
     * trae desde al base de datos todos los elementos de rangos.
     */
    public boolean InicializarComponentes() {

        try {
            ResultSet rs;
            // - - - - - - Pregunta 4 Cuestionario 5 - - - - - - - - - - - LCOxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            rs = db.ResultadoSelect("obtenerpregunta", "5", "LCO"); //Todo: no es necesario 'AsignarTextDoble' y otros, verificar en transcurso

            bod.minLCO = rs.getDouble("rangomin");
            bod.maxLCO = rs.getDouble("rangomax");
            // - - - - - - Pregunta 5 Cuestionario 5 - - - - - - - - - - - LCExxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

            // - - - - - - Pregunta 9 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RAU
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RAU");

            bod.minRAU = rs.getDouble("rangomin");
            bod.maxRAU = rs.getDouble("rangomax");
            // - - - - - - Pregunta 11 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RAT
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RAT");

            bod.minRAT = rs.getDouble("rangomin");
            bod.maxRAT = rs.getDouble("rangomax");
            // - - - - - - Pregunta 12 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RAR
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RAR");

            bod.minRAR = rs.getDouble("rangomin");
            bod.maxRAR = rs.getDouble("rangomax");
            // - - - - - - Pregunta 13 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RCP
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RCP");

            bod.minRCP = rs.getDouble("rangomin");
            bod.maxRCP = rs.getDouble("rangomax");
            // - - - - - - Pregunta 14 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RCM
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RCM");

            bod.minRCM = rs.getDouble("rangomin");
            bod.maxRCM = rs.getDouble("rangomax");
            // - - - - - - Pregunta 15 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RLC
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RLC");

            bod.minRLC = rs.getDouble("rangomin");
            bod.maxRLC = rs.getDouble("rangomax");
            // - - - - - - Pregunta 16 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RVM
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RVM");

            bod.minRVM = rs.getDouble("rangomin");
            bod.maxRVM = rs.getDouble("rangomax");
            // - - - - - - Pregunta 17 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RVN
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RVN");

            bod.minRVN = rs.getDouble("rangomin");
            bod.maxRVN = rs.getDouble("rangomax");
            // - - - - - - Pregunta 18 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RNB
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RNB");

            bod.minRNB = rs.getInt("rangomin");
            bod.maxRNB = rs.getInt("rangomax");
            // - - - - - - Pregunta 19 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RNE
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RNE");

            bod.minRNE = rs.getDouble("rangomin");
            bod.maxRNE = rs.getDouble("rangomax");
            // - - - - - - Pregunta 20 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RPC
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RPC");

            bod.minRPC = rs.getDouble("rangomin");
            bod.maxRPC = rs.getDouble("rangomax");
            // - - - - - - Pregunta 21 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RPS
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RPS");

            bod.minRPS = rs.getDouble("rangomin");
            bod.maxRPS = rs.getDouble("rangomax");
            // - - - - - - Pregunta 23 Cuestionario 8 - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - RPH
            rs = db.ResultadoSelect("obtenerpregunta", "8", "RPH");

            bod.minRPH = rs.getDouble("rangomin");
            bod.maxRPH = rs.getDouble("rangomax");
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
            // - - - - - - Pregunta 9- - - - - -  - - - RAU  Área útil entre las barras para el escurrimiento (Au)
            if (!bod.setRAU(bod.calcularRAU(0), bod.minRAU, bod.maxRAU)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RAU", null);
                error = rs.getString("titulo1");
                return false;
            }
            // - - - - - - Pregunta 11- - - - - -  - - - RAT Área total de las rejillas (incluidas las barras) (S) 
            if (!bod.setRAT(bod.calcularRAT(0, 0), bod.minRAT, bod.maxRAT)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RAT", null);
                error = rs.getString("titulo1");
                return false;
            }
            // - - - - - - Pregunta 12- - - - - -  - - - RAR Ancho de rejillas (b)
            if (!bod.setRAR(bod.calcularRAR(0, 0), bod.minRAR, bod.maxRAR)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RAR", null);
                error = rs.getString("titulo1");
                return false;
            }
            // - - - - - - Pregunta 13- - - - - -  - - - RCP Velocidad máxima (Vmax)
            if (!bod.setRCP(bod.calcularRCP(0, 0, 0), bod.minRCP, bod.maxRCP)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RCP", null);
                error = rs.getString("titulo1");
                return false;
            }
            // - - - - - - Pregunta 14- - - - - -  - - - RCM Velocidad media (Vmed)
            if (!bod.setRCM(bod.calcularRCM(0, 0, 0), bod.minRCM, bod.maxRCM)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RCM", null);
                error = rs.getString("titulo1");
                return false;
            }
            // - - - - - - Pregunta 15- - - - - -  - - - RLC Longitud del canal (L)
            if (!bod.setRLC(bod.calcularRLC(0), bod.minRLC, bod.maxRLC)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RLC", null);
                error = rs.getString("titulo1");
                return false;
            }
            // - - - - - - Pregunta 16- - - - - -  - - - RVM Velocidad de aproximación Vo para el Caudal Máximo
            if (!bod.setRVM(bod.calcularRVM(0, 0), bod.minRVM, bod.maxRVM)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RVM", null);
                error = rs.getString("titulo1");
                return false;
            }
            // - - - - - - Pregunta 17- - - - - -  - - - RVN Velocidad de aproximación Vo para el caudal medio
            if (!bod.setRVN(bod.calcularRVN(0, 0), bod.minRVN, bod.maxRVN)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RVN", null);
                error = rs.getString("titulo1");
                return false;
            }
            // - - - - - - Pregunta 18- - - - - -  - - - RNB Número de barras (Nb) en la rejilla 
            if (!bod.setRNB(bod.calcularRNB(0, 0, 0), bod.minRNB, bod.maxRNB)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RNB", null);
                error = rs.getString("titulo1");
                return false;
            }
            // - - - - - - Pregunta 19- - - - - -  - - - RNE Número de espaciamientos (Ne) en la rejilla 
            if (!bod.setRNE(bod.calcularRNE(0), bod.minRNE, bod.maxRNE)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RNE", null);
                error = rs.getString("titulo1");
                return false;
            }
            // - - - - - - Pregunta 20- - - - - -  - - - RPC Pérdida de carga en la rejilla 
            if (!bod.setRPC(bod.calcularRPC(0, 0), bod.minRPC, bod.maxRPC)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RPC", null);
                error = rs.getString("titulo1");
                return false;
            }
            // - - - - - - Pregunta 21- - - - - -  - - - RPS Pérdida de carga en la rejilla 50% sucia (hL)
            if (!bod.setRPS(bod.calcularRPS(0, 0), bod.minRPS, bod.maxRPS)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RPS", null);
                error = rs.getString("titulo1");
                return false;
            }
            // - - - - - - Pregunta 23- - - - - -  - - - RPH Pérdida de carga calculada por la ecuación de Kirshmer hF
            if (!bod.setRPH(bod.calcularRPH(0, 0, 0, 0, 0), bod.minRPH, bod.maxRPH)) { //Parametros ya existen
                rs = db.ResultadoSelect("obtenernombretitulo", "RPH", null);
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