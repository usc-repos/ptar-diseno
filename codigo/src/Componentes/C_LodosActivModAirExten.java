package Componentes;

import BO.Bod;
import DB.Dao;
import java.io.File;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;

public class C_LodosActivModAirExten {

    private Bod bod = new Bod("");
    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("C_LodosActivModAirExten"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    public String error = "";//Retorna el error de esta clase al inicializar    
    public Map<String, Pregunta> map = new TreeMap<>();
    Pregunta pg;

    public C_LodosActivModAirExten(Bod bod) {
        this.bod = bod;
    }

    /**
     * trae desde al base de datos todos los elementos de rangos.
     */
    public boolean InicializarComponentes() {

        try {
            ResultSet rs;

            rs = db.ResultadoSelect("obtenerseccion", "13", null);

            error += rs.getString("Nombre") + ",\n "; //Se le coloca el nombre del objeto  
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EES
            rs = db.ResultadoSelect("datospregunta", "EES");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EES", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EED
            rs = db.ResultadoSelect("datospregunta", "EED");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EED", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EPL
            rs = db.ResultadoSelect("datospregunta", "EPL");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EPL", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ELT
            rs = db.ResultadoSelect("datospregunta", "ELT");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("ELT", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EVL
            rs = db.ResultadoSelect("datospregunta", "EVL");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EVL", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EVT
            rs = db.ResultadoSelect("datospregunta", "EVT");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EVT", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EVH
            rs = db.ResultadoSelect("datospregunta", "EVH");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EVH", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EAS
            rs = db.ResultadoSelect("datospregunta", "EAS");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EAS", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EAM
            rs = db.ResultadoSelect("datospregunta", "EAM");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EAM", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EEC
            rs = db.ResultadoSelect("datospregunta", "EEC");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EEC", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ESA
            rs = db.ResultadoSelect("datospregunta", "ESA");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("ESA", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EMS
            rs = db.ResultadoSelect("datospregunta", "EMS");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EMS", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EAT
            rs = db.ResultadoSelect("datospregunta", "EAT");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("EAT", pg);
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
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EES   
            if (!bod.setVarDob(bod.calcularEES(), "EES", map.get("EES").rmin, map.get("EES").rmax)) {
                temp = map.get("EES").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("EES").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EED   
            if (!bod.setVarDob(bod.calcularEED(), "EED", map.get("EED").rmin, map.get("EED").rmax)) {
                temp = map.get("EED").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("EED").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EPL   
            if (!bod.setVarDob(bod.calcularEPL(), "EPL", map.get("EPL").rmin, map.get("EPL").rmax)) {
                temp = map.get("EPL").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("EPL").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ELT   
            if (!bod.setVarDob(bod.calcularELT(), "ELT", map.get("ELT").rmin, map.get("ELT").rmax)) {
                temp = map.get("ELT").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("ELT").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EVL   
            if (!bod.setVarDob(bod.calcularEVL(), "EVL", map.get("EVL").rmin, map.get("EVL").rmax)) {
                temp = map.get("EVL").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("EVL").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EVT   
            if (!bod.setVarDob(bod.calcularEVT(), "EVT", map.get("EVT").rmin, map.get("EVT").rmax)) {
                temp = map.get("EVT").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("EVT").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EVH   
            if (!bod.setVarDob(bod.calcularEVH(), "EVH", map.get("EVH").rmin, map.get("EVH").rmax)) {
                temp = map.get("EVH").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("EVH").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EAS   
            if (!bod.setVarDob(bod.calcularEAS(), "EAS", map.get("EAS").rmin, map.get("EAS").rmax)) {
                temp = map.get("EAS").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("EAS").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EAM   
            if (!bod.setVarDob(bod.calcularEAM(), "EAM", map.get("EAM").rmin, map.get("EAM").rmax)) {
                temp = map.get("EAM").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("EAM").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EEC   
            if (!bod.setVarDob(bod.calcularEEC(), "EEC", map.get("EEC").rmin, map.get("EEC").rmax)) {
                temp = map.get("EEC").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("EEC").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ESA   
            if (!bod.setVarDob(bod.calcularESA(), "ESA", map.get("ESA").rmin, map.get("ESA").rmax)) {
                temp = map.get("ESA").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("ESA").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EMS   
            if (!bod.setVarDob(bod.calcularEMS(), "EMS", map.get("EMS").rmin, map.get("EMS").rmax)) {
                temp = map.get("EMS").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("EMS").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - EAT   
            if (!bod.setVarDob(bod.calcularEAT(), "EAT", map.get("EAT").rmin, map.get("EAT").rmax)) {
                temp = map.get("EAT").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("EAT").desc;
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
