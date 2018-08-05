package Componentes;

import BO.Bod;
import DB.Dao;
import java.io.File;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;

public class C_LodosActivadsModConv {

    private Bod bod = new Bod("");
    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("C_ReactorUasb"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    public String error = "";//Retorna el error de esta clase al inicializar    
    public Map<String, Pregunta> map = new TreeMap<>();
    Pregunta pg;

    public C_LodosActivadsModConv(Bod bod) {
        this.bod = bod;
    }

    /**
     * trae desde al base de datos todos los elementos de rangos.
     */
    public boolean InicializarComponentes() {

        try {
            ResultSet rs;

            rs = db.ResultadoSelect("obtenerseccion", "12", null);

            error += rs.getString("Nombre") + ",\n "; //Se le coloca el nombre del objeto 
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MAR
            rs = db.ResultadoSelect("datospregunta", "MAR");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MAR", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MTR
            rs = db.ResultadoSelect("datospregunta", "MTR");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MTR", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MGC
            rs = db.ResultadoSelect("datospregunta", "MGC");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MGC", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MGL
            rs = db.ResultadoSelect("datospregunta", "MGL");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MGL", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MES
            rs = db.ResultadoSelect("datospregunta", "MES");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MES", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MED
            rs = db.ResultadoSelect("datospregunta", "MED");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MED", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MPL
            rs = db.ResultadoSelect("datospregunta", "MPL");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MPL", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MLT
            rs = db.ResultadoSelect("datospregunta", "MLT");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MLT", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MVL
            rs = db.ResultadoSelect("datospregunta", "MVL");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MVL", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MVT
            rs = db.ResultadoSelect("datospregunta", "MVT");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MVT", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MVH
            rs = db.ResultadoSelect("datospregunta", "MVH");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MVH", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MAS
            rs = db.ResultadoSelect("datospregunta", "MAS");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MAS", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MAM
            rs = db.ResultadoSelect("datospregunta", "MAM");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MAM", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MEC
            rs = db.ResultadoSelect("datospregunta", "MEC");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MEC", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MSA
            rs = db.ResultadoSelect("datospregunta", "MSA");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MSA", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MMS
            rs = db.ResultadoSelect("datospregunta", "MMS");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MMS", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MAT
            rs = db.ResultadoSelect("datospregunta", "MAT");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("MAT", pg);
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
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MAR   
            if (!bod.setVarDob(bod.calcularMAR(), "MAR", map.get("MAR").rmin, map.get("MAR").rmax)) {
                temp = map.get("MAR").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("MAR").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MTR   
            if (!bod.setVarDob(bod.calcularMTR(), "MTR", map.get("MTR").rmin, map.get("MTR").rmax)) {
                temp = map.get("MTR").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("MTR").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MGC   
            if (!bod.setVarDob(bod.calcularMGC(), "MGC", map.get("MGC").rmin, map.get("MGC").rmax)) {
                temp = map.get("MGC").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("MGC").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MGL   
            if (!bod.setVarDob(bod.calcularMGL(), "MGL", map.get("MGL").rmin, map.get("MGL").rmax)) {
                temp = map.get("MGL").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("MGL").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MES   
            if (!bod.setVarDob(bod.calcularMES(), "MES", map.get("MES").rmin, map.get("MES").rmax)) {
                temp = map.get("MES").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("MES").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MED   
            if (!bod.setVarDob(bod.calcularMED(), "MED", map.get("MED").rmin, map.get("MED").rmax)) {
                temp = map.get("MED").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("MED").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MPL   
            if (!bod.setVarDob(bod.calcularMPL(), "MPL", map.get("MPL").rmin, map.get("MPL").rmax)) {
                temp = map.get("MPL").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("MPL").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MLT   
            if (!bod.setVarDob(bod.calcularMLT(), "MLT", map.get("MLT").rmin, map.get("MLT").rmax)) {
                temp = map.get("MLT").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("MLT").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MVL   
            if (!bod.setVarDob(bod.calcularMVL(), "MVL", map.get("MVL").rmin, map.get("MVL").rmax)) {
                temp = map.get("MVL").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("MVL").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MVT   
            if (!bod.setVarDob(bod.calcularMVT(), "MVT", map.get("MVT").rmin, map.get("MVT").rmax)) {
                temp = map.get("MVT").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("MVT").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MVH   
            if (!bod.setVarDob(bod.calcularMVH(), "MVH", map.get("MVH").rmin, map.get("MVH").rmax)) {
                temp = map.get("MVH").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("MVH").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MAS   
            if (!bod.setVarDob(bod.calcularMAS(), "MAS", map.get("MAS").rmin, map.get("MAS").rmax)) {
                temp = map.get("MAS").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("MAS").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MAM   
            if (!bod.setVarDob(bod.calcularMAM(), "MAM", map.get("MAM").rmin, map.get("MAM").rmax)) {
                temp = map.get("MAM").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("MAM").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MEC   
            if (!bod.setVarDob(bod.calcularMEC(), "MEC", map.get("MEC").rmin, map.get("MEC").rmax)) {
                temp = map.get("MEC").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("MEC").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MSA   
            if (!bod.setVarDob(bod.calcularMSA(), "MSA", map.get("MSA").rmin, map.get("MSA").rmax)) {
                temp = map.get("MSA").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("MSA").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MMS   
            if (!bod.setVarDob(bod.calcularMMS(), "MMS", map.get("MMS").rmin, map.get("MMS").rmax)) {
                temp = map.get("MMS").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("MMS").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - MAT   
            if (!bod.setVarDob(bod.calcularMAT(), "MAT", map.get("MAT").rmin, map.get("MAT").rmax)) {
                temp = map.get("MAT").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("MAT").desc;
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
