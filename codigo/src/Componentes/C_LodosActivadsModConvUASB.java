package Componentes;

import BO.Bod;
import DB.Dao;
import java.io.File;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;

public class C_LodosActivadsModConvUASB {

    private Bod bod = new Bod("");
    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("C_ReactorUasb"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    public String error = "";//Retorna el error de esta clase al inicializar    
    public Map<String, Pregunta> map = new TreeMap<>();
    Pregunta pg;

    public C_LodosActivadsModConvUASB(Bod bod) {
        this.bod = bod;
    }

    /**
     * trae desde al base de datos todos los elementos de rangos.
     */
    public boolean InicializarComponentes() {

        try {
            ResultSet rs;

            rs = db.ResultadoSelect("obtenerseccion", "14", null);

            error += rs.getString("Nombre") + ",\n "; //Se le coloca el nombre del objeto 
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NES
            rs = db.ResultadoSelect("datospregunta", "NES");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NES", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NED
            rs = db.ResultadoSelect("datospregunta", "NED");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NED", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NPL
            rs = db.ResultadoSelect("datospregunta", "NPL");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NPL", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NLT
            rs = db.ResultadoSelect("datospregunta", "NLT");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NLT", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NVL
            rs = db.ResultadoSelect("datospregunta", "NVL");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NVL", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NVT
            rs = db.ResultadoSelect("datospregunta", "NVT");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NVT", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NVH
            rs = db.ResultadoSelect("datospregunta", "NVH");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NVH", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NAS
            rs = db.ResultadoSelect("datospregunta", "NAS");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NAS", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NAM
            rs = db.ResultadoSelect("datospregunta", "NAM");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NAM", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NEC
            rs = db.ResultadoSelect("datospregunta", "NEC");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NEC", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NSA
            rs = db.ResultadoSelect("datospregunta", "NSA");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NSA", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NMS
            rs = db.ResultadoSelect("datospregunta", "NMS");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NMS", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NAT
            rs = db.ResultadoSelect("datospregunta", "NAT");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("NAT", pg);
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
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NES   
            if (!bod.setVarDob(bod.calcularNES(), "NES", map.get("NES").rmin, map.get("NES").rmax)) {
                temp = map.get("NES").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("NES").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NED   
            if (!bod.setVarDob(bod.calcularNED(), "NED", map.get("NED").rmin, map.get("NED").rmax)) {
                temp = map.get("NED").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("NED").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NPL   
            if (!bod.setVarDob(bod.calcularNPL(), "NPL", map.get("NPL").rmin, map.get("NPL").rmax)) {
                temp = map.get("NPL").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("NPL").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NLT   
            if (!bod.setVarDob(bod.calcularNLT(), "NLT", map.get("NLT").rmin, map.get("NLT").rmax)) {
                temp = map.get("NLT").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("NLT").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NVL   
            if (!bod.setVarDob(bod.calcularNVL(), "NVL", map.get("NVL").rmin, map.get("NVL").rmax)) {
                temp = map.get("NVL").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("NVL").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NVT   
            if (!bod.setVarDob(bod.calcularNVT(), "NVT", map.get("NVT").rmin, map.get("NVT").rmax)) {
                temp = map.get("NVT").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("NVT").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NVH   
            if (!bod.setVarDob(bod.calcularNVH(), "NVH", map.get("NVH").rmin, map.get("NVH").rmax)) {
                temp = map.get("NVH").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("NVH").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NAS   
            if (!bod.setVarDob(bod.calcularNAS(), "NAS", map.get("NAS").rmin, map.get("NAS").rmax)) {
                temp = map.get("NAS").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("NAS").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NAM   
            if (!bod.setVarDob(bod.calcularNAM(), "NAM", map.get("NAM").rmin, map.get("NAM").rmax)) {
                temp = map.get("NAM").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("NAM").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NEC   
            if (!bod.setVarDob(bod.calcularNEC(), "NEC", map.get("NEC").rmin, map.get("NEC").rmax)) {
                temp = map.get("NEC").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("NEC").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NSA   
            if (!bod.setVarDob(bod.calcularNSA(), "NSA", map.get("NSA").rmin, map.get("NSA").rmax)) {
                temp = map.get("NSA").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("NSA").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NMS   
            if (!bod.setVarDob(bod.calcularNMS(), "NMS", map.get("NMS").rmin, map.get("NMS").rmax)) {
                temp = map.get("NMS").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("NMS").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NAT   
            if (!bod.setVarDob(bod.calcularNAT(), "NAT", map.get("NAT").rmin, map.get("NAT").rmax)) {
                temp = map.get("NAT").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("NAT").desc;
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
