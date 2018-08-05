package Componentes;

import BO.Bod;
import DB.Dao;
import java.io.File;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;

public class C_LagunaFacultativaSec {

    private Bod bod = new Bod("");
    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("C_LagunaFacultativaSec"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    public String error = "";//Retorna el error de esta clase al inicializar
    public Map<String, Pregunta> map = new TreeMap<>();
    Pregunta pg;

    public C_LagunaFacultativaSec(Bod bod) {
        this.bod = bod;
    }

    /**
     * trae desde al base de datos todos los elementos de rangos necesarios.
     */
    public boolean InicializarComponentes() {

        try {
            ResultSet rs;

            rs = db.ResultadoSelect("obtenerseccion", "6", null);

            error += rs.getString("Nombre") + ",\n "; //Se le coloca el nombre del objeto 
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - FCO 
            rs = db.ResultadoSelect("datospregunta", "FCO");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("FCO", pg);
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - FAS 
            rs = db.ResultadoSelect("datospregunta", "FAS");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("FAS", pg);
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - FTR 
            rs = db.ResultadoSelect("datospregunta", "FTR");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("FTR", pg);
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - FED 
            rs = db.ResultadoSelect("datospregunta", "FED");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("FED", pg);
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - FUV 
            rs = db.ResultadoSelect("datospregunta", "FUV");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("FUV", pg);
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - FSA 
            rs = db.ResultadoSelect("datospregunta", "FSA");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("FSA", pg);
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - FSL 
            rs = db.ResultadoSelect("datospregunta", "FSL");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("FSL", pg);
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - FAB 
            rs = db.ResultadoSelect("datospregunta", "FAB");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("FAB", pg);
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - FLA 
            rs = db.ResultadoSelect("datospregunta", "FLA");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("FLA", pg);
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
            db.close(); //Se cierra la conexiòn

            if (Guardar()) {
                return true;
            }

        } catch (Exception ex) {
            error += ex.getMessage();
            log.error("Error en InicializarComponentes " + ex.getMessage());
            return false;
        } finally {
            db.close();
        }
        return false;
    }

    private boolean Guardar() {//Aquí unicamente se comprueban las fórmulas que tienen que ver con datos anteriores

        try {
            String temp;
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - FCO
            if (!bod.setVarDob(bod.calcularFCO(), "FCO", map.get("FCO").rmin, map.get("FCO").rmax)) {
                temp = map.get("FCO").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("FCO").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - FAS
            if (!bod.setVarDob(bod.calcularFAS(), "FAS", map.get("FAS").rmin, map.get("FAS").rmax)) {
                temp = map.get("FAS").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("FAS").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - FTR
            if (!bod.setVarDob(bod.calcularFTR(), "FTR", map.get("FTR").rmin, map.get("FTR").rmax)) {
                temp = map.get("FTR").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("FTR").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - FED
            if (!bod.setVarDob(bod.calcularFED(), "FED", map.get("FED").rmin, map.get("FED").rmax)) {
                temp = map.get("FED").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("FED").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - FUV
            if (!bod.setVarDob(bod.calcularFUV(), "FUV", map.get("FUV").rmin, map.get("FUV").rmax)) {
                temp = map.get("FUV").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("FUV").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - FSA
            if (!bod.setVarDob(bod.calcularFSA(), "FSA", map.get("FSA").rmin, map.get("FSA").rmax)) {
                temp = map.get("FSA").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("FSA").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - FSL
            if (!bod.setVarDob(bod.calcularFSL(), "FSL", map.get("FSL").rmin, map.get("FSL").rmax)) {
                temp = map.get("FSL").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("FSL").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - FAB
            if (!bod.setVarDob(bod.calcularFAB(), "FAB", map.get("FAB").rmin, map.get("FAB").rmax)) {
                temp = map.get("FAB").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("FAB").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - FLA
            if (!bod.setVarDob(bod.calcularFLA(), "FLA", map.get("FLA").rmin, map.get("FLA").rmax)) {
                temp = map.get("FLA").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("FLA").desc;
                }
                error += temp;
                return false;
            }

            return true;

        } catch (Exception ex) {
            error += ex.getMessage();
            log.error("Error en Guardar() " + ex.getMessage());
            return false;
        }
    }
}
