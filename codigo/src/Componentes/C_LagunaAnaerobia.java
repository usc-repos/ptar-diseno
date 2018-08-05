package Componentes;

import BO.Bod;
import DB.Dao;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;

public class C_LagunaAnaerobia {

    private Bod bod = new Bod("");
    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("C4_CaractInicAguaResidual"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    public String error = "";//Retorna el error de esta clase al inicializar
    public Map<String, Pregunta> map = new TreeMap<>();
    Pregunta pg;

    public C_LagunaAnaerobia(Bod bod) {
        this.bod = bod;
    }

    /**
     * trae desde al base de datos todos los elementos de rangos.
     */
    public boolean InicializarComponentes() {

        try {
            ResultSet rs;

            rs = db.ResultadoSelect("obtenerseccion", "5", null);

            error += rs.getString("Nombre") + ",\n "; //Se le coloca el nombre del objeto 
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - LCO	   
            rs = db.ResultadoSelect("datospregunta", "LCO");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("LCO", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - LCE	   
            rs = db.ResultadoSelect("datospregunta", "LCE");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("LCE", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - LAE	   
            rs = db.ResultadoSelect("datospregunta", "LAE");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("LAE", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - LVU	   
            rs = db.ResultadoSelect("datospregunta", "LVU");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("LVU", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - LTH	   
            rs = db.ResultadoSelect("datospregunta", "LTH");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("LTH", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - LVR	   
            rs = db.ResultadoSelect("datospregunta", "LVR");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("LVR", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - LAS	   
            rs = db.ResultadoSelect("datospregunta", "LAS");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("LAS", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - LAA	   
            rs = db.ResultadoSelect("datospregunta", "LAA");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("LAA", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - LAL	   
            rs = db.ResultadoSelect("datospregunta", "LAL");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("LAL", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - LAB	   
            rs = db.ResultadoSelect("datospregunta", "LAB");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("LAB", pg);
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
     * Se comprueban las fórmulas que tienen que ver con datos anteriores, al
     * realizar un guardado en una ventana anterior que pueda afectar los datos
     * de ésta
     *
     * @return true: si todo calculo y formula se hicieron sin problema
     */
    private boolean Guardar() {

        try {
            String temp;

            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - LCO 
            if (!bod.setVarDob(bod.calcularLCO(), "LCO", map.get("LCO").rmin, map.get("LCO").rmax)) {
                temp = map.get("LCO").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("LCO").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - LCE 
            if (!bod.setVarDob(bod.calcularLCE(), "LCE", map.get("LCE").rmin, map.get("LCE").rmax)) {
                temp = map.get("LCE").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("LCE").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - LAE 
            if (!bod.setVarDob(bod.calcularLAE(), "LAE", map.get("LAE").rmin, map.get("LAE").rmax)) {
                temp = map.get("LAE").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("LAE").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - LVU 
            if (!bod.setVarDob(bod.calcularLVU(), "LVU", map.get("LVU").rmin, map.get("LVU").rmax)) {
                temp = map.get("LVU").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("LVU").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - LTH 
            if (!bod.setVarDob(bod.calcularLTH(), "LTH", map.get("LTH").rmin, map.get("LTH").rmax)) {
                temp = map.get("LTH").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("LTH").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - LVR 
            if (!bod.setVarDob(bod.calcularLVR(), "LVR", map.get("LVR").rmin, map.get("LVR").rmax)) {
                temp = map.get("LVR").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("LVR").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - LAS 
            if (!bod.setVarDob(bod.calcularLAS(), "LAS", map.get("LAS").rmin, map.get("LAS").rmax)) {
                temp = map.get("LAS").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("LAS").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - LAA 
            if (bod.getVarDob("LLA") > 0) {

                if (!bod.setVarDob(bod.calcularLAA_1_3(), "LAA", map.get("LAA").rmin, map.get("LAA").rmax)) {
                    temp = map.get("LAA").tit1;
                    if (temp.length() < 4) {
                        temp += ".\n " + map.get("LAA").desc;
                    }
                    error += temp;
                    return false;
                }
            } else {
                if (!bod.setVarDob(bod.calcularLAA_2_3(), "LAA", map.get("LAA").rmin, map.get("LAA").rmax)) {
                    temp = map.get("LAA").tit1;
                    if (temp.length() < 4) {
                        temp += ".\n " + map.get("LAA").desc;
                    }
                    error += temp;
                    return false;
                }
            }

            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - LAL 
            if (bod.getVarDob("LLA") > 0) {
                if (!bod.setVarDob(bod.calcularLAL_1_3(), "LAL", map.get("LAL").rmin, map.get("LAL").rmax)) {
                    temp = map.get("LAL").tit1;
                    if (temp.length() < 4) {
                        temp += ".\n " + map.get("LAL").desc;
                    }
                    error += temp;
                    return false;
                }
            } else {
                if (!bod.setVarDob(bod.calcularLAL_2_3(), "LAL", map.get("LAL").rmin, map.get("LAL").rmax)) {
                    temp = map.get("LAL").tit1;
                    if (temp.length() < 4) {
                        temp += ".\n " + map.get("LAL").desc;
                    }
                    error += temp;
                    return false;
                }
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - LAB 
            if (!bod.setVarDob(bod.calcularLAB(), "LAB", map.get("LAB").rmin, map.get("LAB").rmax)) {
                temp = map.get("LAB").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("LAB").desc;
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
