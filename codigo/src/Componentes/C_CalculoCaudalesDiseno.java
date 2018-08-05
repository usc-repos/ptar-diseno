package Componentes;

import BO.Bod;
import DB.Dao;
import java.io.File;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;

public class C_CalculoCaudalesDiseno {

    private Bod bod = new Bod("");
    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("C3_CalculoCaudalesDiseno"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    public String error = "";//Retorna el error de esta clase al inicializar
    public Map<String, Pregunta> map = new TreeMap<>();
    Pregunta pg; 

    public C_CalculoCaudalesDiseno(Bod bod) {
        this.bod = bod;
    }

    /**
     * trae desde al base de datos todos los elementos de rangos.
     */
    public boolean InicializarComponentes() {

        try {
            ResultSet rs;

            rs = db.ResultadoSelect("obtenerseccion", "3", null);

            error += rs.getString("Nombre") + ",\n "; //Se le coloca el nombre del objeto 
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - CAR
            rs = db.ResultadoSelect("datospregunta", "CAR");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("CAR", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q2C
            rs = db.ResultadoSelect("datospregunta", "Q2C");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("Q2C", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q2K
            rs = db.ResultadoSelect("datospregunta", "Q2K");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("Q2K", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q3C
            rs = db.ResultadoSelect("datospregunta", "Q3C");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("Q3C", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q3K
            rs = db.ResultadoSelect("datospregunta", "Q3K");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("Q3K", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q1C
            rs = db.ResultadoSelect("datospregunta", "Q1C");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("Q1C", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q1K
            rs = db.ResultadoSelect("datospregunta", "Q1K");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("Q1K", pg);
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

    private boolean Guardar() {//Aqui unicamente se comprueban las fórmulas que tienen que ver con datos anteriores

        try {
            String temp;

            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - CAR  
            if (!bod.setVarDob(bod.calcularCAR(), "CAR", map.get("CAR").rmin, map.get("CAR").rmax)) {
                temp = map.get("CAR").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("CAR").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q2C  
            if (!bod.setVarDob(bod.calcularQ2C(), "Q2C", map.get("Q2C").rmin, map.get("Q2C").rmax)) {
                temp = map.get("Q2C").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("Q2C").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q2K  
            if (!bod.setVarDob(bod.calcularQ2Cm3Dia(), "Q2K", map.get("Q2K").rmin, map.get("Q2K").rmax)) {
                temp = map.get("Q2K").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("Q2K").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q3C  
            if (!bod.setVarDob(bod.calcularQ3C(), "Q3C", map.get("Q3C").rmin, map.get("Q3C").rmax)) {
                temp = map.get("Q3C").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("Q3C").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q3K  
            if (!bod.setVarDob(bod.calcularQ3Cm3Dia(), "Q3K", map.get("Q3K").rmin, map.get("Q3K").rmax)) {
                temp = map.get("Q3K").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("Q3K").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q1C  
            if (!bod.setVarDob(bod.calcularQ1C(), "Q1C", map.get("Q1C").rmin, map.get("Q1C").rmax)) {
                temp = map.get("Q1C").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("Q1C").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Q1K  
            if (!bod.setVarDob(bod.calcularQ1Cm3Dia(), "Q1K", map.get("Q1K").rmin, map.get("Q1K").rmax)) {
                temp = map.get("Q1K").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("Q1K").desc;
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
