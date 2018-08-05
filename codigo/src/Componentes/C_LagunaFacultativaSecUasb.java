package Componentes;

import BO.Bod;
import DB.Dao;
import java.io.File;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;

public class C_LagunaFacultativaSecUasb {

    private Bod bod = new Bod("");
    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("C_LagunaFacultativaSecUasb"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    public String error = "";//Retorna el error de esta clase al inicializar
    public Map<String, Pregunta> map = new TreeMap<>();
    Pregunta pg;

    public C_LagunaFacultativaSecUasb(Bod bod) {
        this.bod = bod;
    }

    /**
     * trae desde al base de datos todos los elementos de rangos necesarios.
     */
    public boolean InicializarComponentes() {

        try {
            ResultSet rs;

            rs = db.ResultadoSelect("obtenerseccion", "15", null);

            error += rs.getString("Nombre") + ",\n "; //Se le coloca el nombre del objeto 
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - GCO 
            rs = db.ResultadoSelect("datospregunta", "GCO");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("GCO", pg);
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - GAS 
            rs = db.ResultadoSelect("datospregunta", "GAS");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("GAS", pg);
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - GTR 
            rs = db.ResultadoSelect("datospregunta", "GTR");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("GTR", pg);
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - GED 
            rs = db.ResultadoSelect("datospregunta", "GED");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("GED", pg);
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - GUV 
            rs = db.ResultadoSelect("datospregunta", "GUV");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("GUV", pg);
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - GSA 
            rs = db.ResultadoSelect("datospregunta", "GSA");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("GSA", pg);
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - GSL 
            rs = db.ResultadoSelect("datospregunta", "GSL");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("GSL", pg);
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - GAB 
            rs = db.ResultadoSelect("datospregunta", "GAB");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("GAB", pg);

            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - GUA 
            rs = db.ResultadoSelect("datospregunta", "GUA");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("GUA", pg);
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
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - GCO
            if (!bod.setVarDob(bod.calcularGCO(), "GCO", map.get("GCO").rmin, map.get("GCO").rmax)) {
                temp = map.get("GCO").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("GCO").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - GAS
            if (!bod.setVarDob(bod.calcularGAS(), "GAS", map.get("GAS").rmin, map.get("GAS").rmax)) {
                temp = map.get("GAS").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("GAS").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - GTR
            if (!bod.setVarDob(bod.calcularGTR(), "GTR", map.get("GTR").rmin, map.get("GTR").rmax)) {
                temp = map.get("GTR").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("GTR").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - GED
            if (!bod.setVarDob(bod.calcularGED(), "GED", map.get("GED").rmin, map.get("GED").rmax)) {
                temp = map.get("GED").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("GED").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - GUV
            if (!bod.setVarDob(bod.calcularGUV(), "GUV", map.get("GUV").rmin, map.get("GUV").rmax)) {
                temp = map.get("GUV").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("GUV").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - GSA
            if (!bod.setVarDob(bod.calcularGSA(), "GSA", map.get("GSA").rmin, map.get("GSA").rmax)) {
                temp = map.get("GSA").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("GSA").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - GSL
            if (!bod.setVarDob(bod.calcularGSL(), "GSL", map.get("GSL").rmin, map.get("GSL").rmax)) {
                temp = map.get("GSL").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("GSL").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - GAB
            if (!bod.setVarDob(bod.calcularGAB(), "GAB", map.get("GAB").rmin, map.get("GAB").rmax)) {
                temp = map.get("GAB").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("GAB").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - GUA
            if (!bod.setVarDob(bod.calcularGUA(), "GUA", map.get("GUA").rmin, map.get("GUA").rmax)) {
                temp = map.get("GUA").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("GUA").desc;
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
