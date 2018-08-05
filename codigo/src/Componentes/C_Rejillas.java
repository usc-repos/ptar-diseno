package Componentes;

import BO.Bod;
import DB.Dao;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;

public class C_Rejillas {

    private Bod bod = new Bod("");
    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("C5_Rejillas"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    public String error = "";//Retorna el error de esta clase al inicializar
    public Map<String, Pregunta> map = new TreeMap<>();
    Pregunta pg;

    public C_Rejillas(Bod bod) {
        this.bod = bod;
    }

    /**
     * trae desde al base de datos todos los elementos de rangos.
     */
    public boolean InicializarComponentes() {

        try {
            ResultSet rs;

            rs = db.ResultadoSelect("obtenerseccion", "8", null);

            error += rs.getString("Nombre") + ",\n ";
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RAU	   
            rs = db.ResultadoSelect("datospregunta", "RAU");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RAU", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RAT	   
            rs = db.ResultadoSelect("datospregunta", "RAT");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RAT", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RAR	   
            rs = db.ResultadoSelect("datospregunta", "RAR");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RAR", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RCP	   
            rs = db.ResultadoSelect("datospregunta", "RCP");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RCP", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RCM	   
            rs = db.ResultadoSelect("datospregunta", "RCM");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RCM", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RLC	   
            rs = db.ResultadoSelect("datospregunta", "RLC");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RLC", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RVM	   
            rs = db.ResultadoSelect("datospregunta", "RVM");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RVM", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RVN	   
            rs = db.ResultadoSelect("datospregunta", "RVN");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RVN", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RNB	   
            rs = db.ResultadoSelect("datospregunta", "RNB");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RNB", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RNE	   
            rs = db.ResultadoSelect("datospregunta", "RNE");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RNE", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RPC	   
            rs = db.ResultadoSelect("datospregunta", "RPC");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RPC", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RPS	   
            rs = db.ResultadoSelect("datospregunta", "RPS");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RPS", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RPH	   
            rs = db.ResultadoSelect("datospregunta", "RPH");
            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("RPH", pg);
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
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RAU   
            if (!bod.setVarDob(bod.calcularRAU(), "RAU", map.get("RAU").rmin, map.get("RAU").rmax)) {
                temp = map.get("RAU").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("RAU").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RAT   
            if (!bod.setVarDob(bod.calcularRAT(), "RAT", map.get("RAT").rmin, map.get("RAT").rmax)) {
                temp = map.get("RAT").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("RAT").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RAR   
            if (!bod.setVarDob(bod.calcularRAR(), "RAR", map.get("RAR").rmin, map.get("RAR").rmax)) {
                temp = map.get("RAR").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("RAR").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RCP   
            if (!bod.setVarDob(bod.calcularRCP(), "RCP", map.get("RCP").rmin, map.get("RCP").rmax)) {
                temp = map.get("RCP").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("RCP").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RCM   
            if (!bod.setVarDob(bod.calcularRCM(), "RCM", map.get("RCM").rmin, map.get("RCM").rmax)) {
                temp = map.get("RCM").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("RCM").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RLC   
            if (!bod.setVarDob(bod.calcularRLC(), "RLC", map.get("RLC").rmin, map.get("RLC").rmax)) {
                temp = map.get("RLC").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("RLC").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RVM   
            if (!bod.setVarDob(bod.calcularRVM(), "RVM", map.get("RVM").rmin, map.get("RVM").rmax)) {
                temp = map.get("RVM").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("RVM").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RVN   
            if (!bod.setVarDob(bod.calcularRVN(), "RVN", map.get("RVN").rmin, map.get("RVN").rmax)) {
                temp = map.get("RVN").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("RVN").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RNB   
            if (!bod.setVarInt(bod.calcularRNB(), "RNB", (int) map.get("RNB").rmin, (int) map.get("RNB").rmax)) {
                temp = map.get("RNB").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("RNB").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RNE   
            if (!bod.setVarDob(bod.calcularRNE(), "RNE", map.get("RNE").rmin, map.get("RNE").rmax)) {
                temp = map.get("RNE").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("RNE").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RPC   
            if (!bod.setVarDob(bod.calcularRPC(), "RPC", map.get("RPC").rmin, map.get("RPC").rmax)) {
                temp = map.get("RPC").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("RPC").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RPS   
            if (!bod.setVarDob(bod.calcularRPS(), "RPS", map.get("RPS").rmin, map.get("RPS").rmax)) {
                temp = map.get("RPS").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("RPS").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - RPH   
            if (!bod.setVarDob(bod.calcularRPH(), "RPH", map.get("RPH").rmin, map.get("RPH").rmax)) {
                temp = map.get("RPH").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("RPH").desc;
                }
                error += temp;
                return false;
            }
            return true;

        } catch (Exception ex) {
            error = ex.getMessage();
            log.error("Error en Guardar() " + ex.getMessage());
            return false;
        }
    }
}