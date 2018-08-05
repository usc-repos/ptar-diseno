package Componentes;

import BO.Bod;
import DB.Dao;
import java.io.File;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;

public class C_Desarenador {

    private Bod bod = new Bod("");
    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("C_Desarenador"); 
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    public String error = "";//Retorna el error de esta clase al inicializar
    public Map<String, Pregunta> map = new TreeMap<>();
    Pregunta pg;

    public C_Desarenador(Bod bod) {
        this.bod = bod;
    }

    /**
     * trae desde al base de datos todos los elementos de rangos.
     */
    public boolean InicializarComponentes() {

        try {
            ResultSet rs;

            rs = db.ResultadoSelect("obtenerseccion", "9", null);

            error += rs.getString("Nombre") + ",\n "; //Se le coloca el nombre del objeto  
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DP1	   
            rs = db.ResultadoSelect("datospregunta", "DP1");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DP1", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DP2	   
            rs = db.ResultadoSelect("datospregunta", "DP2");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DP2", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DP3	   
            rs = db.ResultadoSelect("datospregunta", "DP3");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DP3", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DRZ	   
            rs = db.ResultadoSelect("datospregunta", "DRZ");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DRZ", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DAH	   
            rs = db.ResultadoSelect("datospregunta", "DAH");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DAH", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DAB	   
            rs = db.ResultadoSelect("datospregunta", "DAB");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DAB", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DLD	   
            rs = db.ResultadoSelect("datospregunta", "DLD");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DLD", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DAL	   
            rs = db.ResultadoSelect("datospregunta", "DAL");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DAL", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DEM	   
            rs = db.ResultadoSelect("datospregunta", "DEM");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DEM", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DUP	   
            rs = db.ResultadoSelect("datospregunta", "DUP");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("DUP", pg);
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  
            db.close(); //Se cierra la conexiòn

            if (Guardar()) {
                return true;
            }

        } catch (Exception ex) {
            error += ex.getMessage();
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
            String temp;
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DP1   
            if (!bod.setVarDob(bod.calcularDP1(), "DP1", map.get("DP1").rmin, map.get("DP1").rmax)) {
                temp = map.get("DP1").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("DP1").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DP2   
            if (!bod.setVarDob(bod.calcularDP2(), "DP2", map.get("DP2").rmin, map.get("DP2").rmax)) {
                temp = map.get("DP2").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("DP2").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DP3   
            if (!bod.setVarDob(bod.calcularDP3(), "DP3", map.get("DP3").rmin, map.get("DP3").rmax)) {
                temp = map.get("DP3").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("DP3").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DRZ   
            if (!bod.setVarDob(bod.calcularDRZ(), "DRZ", map.get("DRZ").rmin, map.get("DRZ").rmax)) {
                temp = map.get("DRZ").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("DRZ").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DAH   
            if (!bod.setVarDob(bod.calcularDAH(), "DAH", map.get("DAH").rmin, map.get("DAH").rmax)) {
                temp = map.get("DAH").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("DAH").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DAB   
            if (!bod.setVarDob(bod.calcularDAB(), "DAB", map.get("DAB").rmin, map.get("DAB").rmax)) {
                temp = map.get("DAB").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("DAB").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DLD   
            if (!bod.setVarDob(bod.calcularDLD(), "DLD", map.get("DLD").rmin, map.get("DLD").rmax)) {
                temp = map.get("DLD").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("DLD").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DAL   
            if (!bod.setVarDob(bod.calcularDAL(), "DAL", map.get("DAL").rmin, map.get("DAL").rmax)) {
                temp = map.get("DAL").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("DAL").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DEM   
            if (!bod.setVarDob(bod.calcularDEM(), "DEM", map.get("DEM").rmin, map.get("DEM").rmax)) {
                temp = map.get("DEM").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("DEM").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - DUP   
            if (!bod.setVarDob(bod.calcularDUP(), "DUP", map.get("DUP").rmin, map.get("DUP").rmax)) {
                temp = map.get("DUP").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("DUP").desc;
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
