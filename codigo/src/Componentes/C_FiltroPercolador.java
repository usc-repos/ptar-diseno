package Componentes;

import BO.Bod;
import DB.Dao;
import java.io.File;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;

public class C_FiltroPercolador {

    private Bod bod = new Bod("");
    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("C_ReactorUasb"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    public String error = "";//Retorna el error de esta clase al inicializar    
    public Map<String, Pregunta> map = new TreeMap<>();
    Pregunta pg;

    public C_FiltroPercolador(Bod bod) {
        this.bod = bod;
    }

    /**
     * trae desde al base de datos todos los elementos de rangos.
     */
    public boolean InicializarComponentes() {

        try {
            ResultSet rs;

            rs = db.ResultadoSelect("obtenerseccion", "11", null);

            error += rs.getString("Nombre") + ",\n "; //Se le coloca el nombre del objeto  
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PVM
            rs = db.ResultadoSelect("datospregunta", "PVM");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PVM", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PAS
            rs = db.ResultadoSelect("datospregunta", "PAS");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PAS", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PH2
            rs = db.ResultadoSelect("datospregunta", "PH2");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PH2", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PH3
            rs = db.ResultadoSelect("datospregunta", "PH3");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PH3", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PFA
            rs = db.ResultadoSelect("datospregunta", "PFA");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PFA", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PFD
            rs = db.ResultadoSelect("datospregunta", "PFD");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PFD", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PCD
            rs = db.ResultadoSelect("datospregunta", "PCD");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PCD", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PEL
            rs = db.ResultadoSelect("datospregunta", "PEL");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PEL", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PVL
            rs = db.ResultadoSelect("datospregunta", "PVL");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PVL", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PSA
            rs = db.ResultadoSelect("datospregunta", "PSA");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PSA", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PAX
            rs = db.ResultadoSelect("datospregunta", "PAX");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PAX", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PDX
            rs = db.ResultadoSelect("datospregunta", "PDX");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PDX", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PAT
            rs = db.ResultadoSelect("datospregunta", "PAT");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PAT", pg);
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
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PVM	   
            if (!bod.setVarDob(bod.calcularPVM(), "PVM", map.get("PVM").rmin, map.get("PVM").rmax)) {
                temp = map.get("PVM").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("PVM").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PAS	   
            if (!bod.setVarDob(bod.calcularPAS(), "PAS", map.get("PAS").rmin, map.get("PAS").rmax)) {
                temp = map.get("PAS").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("PAS").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PH2	   
            if (!bod.setVarDob(bod.calcularPH2(), "PH2", map.get("PH2").rmin, map.get("PH2").rmax)) {
                temp = map.get("PH2").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("PH2").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PH3	   
            if (!bod.setVarDob(bod.calcularPH3(), "PH3", map.get("PH3").rmin, map.get("PH3").rmax)) {
                temp = map.get("PH3").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("PH3").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PFA	   
            if (!bod.setVarDob(bod.calcularPFA(), "PFA", map.get("PFA").rmin, map.get("PFA").rmax)) {
                temp = map.get("PFA").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("PFA").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PFD	   
            if (!bod.setVarDob(bod.calcularPFD(), "PFD", map.get("PFD").rmin, map.get("PFD").rmax)) {
                temp = map.get("PFD").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("PFD").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PCD	   
            if (!bod.setVarDob(bod.calcularPCD(), "PCD", map.get("PCD").rmin, map.get("PCD").rmax)) {
                temp = map.get("PCD").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("PCD").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PEL	   
            if (!bod.setVarDob(bod.calcularPEL(), "PEL", map.get("PEL").rmin, map.get("PEL").rmax)) {
                temp = map.get("PEL").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("PEL").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PVL	   
            if (!bod.setVarDob(bod.calcularPVL(), "PVL", map.get("PVL").rmin, map.get("PVL").rmax)) {
                temp = map.get("PVL").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("PVL").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PSA	   
            if (!bod.setVarDob(bod.calcularPSA(), "PSA", map.get("PSA").rmin, map.get("PSA").rmax)) {
                temp = map.get("PSA").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("PSA").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PAX	   
            if (!bod.setVarDob(bod.calcularPAX(), "PAX", map.get("PAX").rmin, map.get("PAX").rmax)) {
                temp = map.get("PAX").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("PAX").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PDX	   
            if (!bod.setVarDob(bod.calcularPDX(), "PDX", map.get("PDX").rmin, map.get("PDX").rmax)) {
                temp = map.get("PDX").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("PDX").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PAT	   
            if (!bod.setVarDob(bod.calcularPAT(), "PAT", map.get("PAT").rmin, map.get("PAT").rmax)) {
                temp = map.get("PAT").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("PAT").desc;
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
