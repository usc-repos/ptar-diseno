package Componentes;

import BO.Bod;
import DB.Dao;
import java.io.File;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;

public class C_ProyeccionPoblacional {

    private Bod bod = new Bod("");
    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("C_ProyeccionPoblacional"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    public String error = "";//Retorna el error de esta clase al inicializar
    public Map<String, Pregunta> map = new TreeMap<>();
    Pregunta pg;

    public C_ProyeccionPoblacional(Bod bod) {
        this.bod = bod;
    }

    /**
     * trae desde al base de datos todos los elementos de rangos.
     */
    public boolean InicializarComponentes() {

        try {
            ResultSet rs;

            rs = db.ResultadoSelect("obtenerseccion", "2", null);

            error += rs.getString("Nombre") + ",\n "; //Se le coloca el nombre del objeto 
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PPT	   
            rs = db.ResultadoSelect("datospregunta", "PPT");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PPT", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PPF	   
            rs = db.ResultadoSelect("datospregunta", "PPF");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PPF", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PCR	   
            rs = db.ResultadoSelect("datospregunta", "PCR");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("PCR", pg);

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

    private boolean Guardar() {//Aquí unicamente se comprueban las fórmulas que tienen que ver con datos anteriores

        try {
            String temp;
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PPT 
            if (bod.getVarInt("PAG") > 0) {

                if (!bod.setVarDob(bod.calcularPPT_A(), "PPT", map.get("PPT").rmin, map.get("PPT").rmax)) {
                    temp = map.get("PPT").tit1;
                    if (temp.length() < 4) {
                        temp += ".\n " + map.get("PPT").desc;
                    }
                    error += temp;
                    return false;
                }
            } else {
                if (!bod.setVarDob(bod.calcularPPT_G(), "PPT", map.get("PPT").rmin, map.get("PPT").rmax)) {
                    temp = map.get("PPT").tit1;
                    if (temp.length() < 4) {
                        temp += ".\n " + map.get("PPT").desc;
                    }
                    error += temp;
                    return false;
                }
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PPF 
            if (bod.getVarInt("PAG") > 0) {

                if (!bod.setVarInt((int) bod.calcularPPF_A(), "PPF",(int) map.get("PPF").rmin,(int) map.get("PPF").rmax)) {
                    temp = map.get("PPF").tit1;
                    if (temp.length() < 4) {
                        temp += ".\n " + map.get("PPF").desc;
                    }
                    error += temp;
                    return false;
                }
            } else {
                if (!bod.setVarDob((int) bod.calcularPPF_G(), "PPF",(int) map.get("PPF").rmin,(int) map.get("PPF").rmax)) {
                    temp = map.get("PPF").tit1;
                    if (temp.length() < 4) {
                        temp += ".\n " + map.get("PPF").desc;
                    }
                    error += temp;
                    return false;
                }
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - PCR 
            String x = bod.calcularPCR();

            if (x.isEmpty()) {
                temp = map.get("PCR").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("PCR").desc;
                }
                error += temp;
                return false;
            }

            if (bod.setVarStr(x, "PCR")) {
                return true;
            }

            return true;

        } catch (Exception ex) {
            error += ex.getMessage();
            log.error("Error en Guardar() " + ex.getMessage());
            return false;
        }
    }
}
