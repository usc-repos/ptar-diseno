package Componentes;

import BO.Bod;
import DB.Dao;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;

public class C_ReactorUasb {

    private Bod bod = new Bod("");
    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("C_ReactorUasb"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    public String error = "";//Retorna el error de esta clase al inicializar    
    public Map<String, Pregunta> map = new TreeMap<>();
    Pregunta pg;

    public C_ReactorUasb(Bod bod) {
        this.bod = bod;
    }

    /**
     * trae desde al base de datos todos los elementos de rangos.
     */
    public boolean InicializarComponentes() {

        try {
            ResultSet rs;

            rs = db.ResultadoSelect("obtenerseccion", "10", null);

            error += rs.getString("Nombre") + ",\n "; //Se le coloca el nombre del objeto 
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UDQ
            rs = db.ResultadoSelect("datospregunta", "UDQ");

            pg = new Pregunta(); //Objeto que será creado con datos, básicos de rangos y mensajes
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UDQ", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UVR	
            rs = db.ResultadoSelect("datospregunta", "UVR");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UVR", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UNR	
            rs = db.ResultadoSelect("datospregunta", "UNR");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UNR", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UVU	
            rs = db.ResultadoSelect("datospregunta", "UVU");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UVU", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UAR	
            rs = db.ResultadoSelect("datospregunta", "UAR");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UAR", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UAA	
            rs = db.ResultadoSelect("datospregunta", "UAA");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UAA", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UAL	
            rs = db.ResultadoSelect("datospregunta", "UAL");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UAL", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UAT	
            rs = db.ResultadoSelect("datospregunta", "UAT");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UAT", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UVT	
            rs = db.ResultadoSelect("datospregunta", "UVT");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UVT", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - URH	
            rs = db.ResultadoSelect("datospregunta", "URH");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("URH", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UCH	
            rs = db.ResultadoSelect("datospregunta", "UCH");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UCH", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UCO	
            rs = db.ResultadoSelect("datospregunta", "UCO");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UCO", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UVN	
            rs = db.ResultadoSelect("datospregunta", "UVN");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UVN", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UVM	
            rs = db.ResultadoSelect("datospregunta", "UVM");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UVM", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UNT
            rs = db.ResultadoSelect("datospregunta", "UNT");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UNT", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Si CAB no fue establecido = UER, si no URQ
            if (bod.getVarDob("CAB") != 0) {
                rs = db.ResultadoSelect("datospregunta", "URQ");

                pg = new Pregunta();
                pg.tit1 = rs.getString("titulo1");
                pg.rmin = rs.getDouble("rangomin");
                pg.rmax = rs.getDouble("rangomax");
                pg.erro = rs.getString("errormsg");
                pg.desc = rs.getString("descripcion");
                map.put("URQ", pg);
            } else {
                rs = db.ResultadoSelect("datospregunta", "UER");

                pg = new Pregunta();
                pg.tit1 = rs.getString("titulo1");
                pg.rmin = rs.getDouble("rangomin");
                pg.rmax = rs.getDouble("rangomax");
                pg.erro = rs.getString("errormsg");
                pg.desc = rs.getString("descripcion");
                map.put("UER", pg);
            }
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UEC	
            if (bod.getVarDob("CAB") != 0) {
                rs = db.ResultadoSelect("datospregunta", "UEQ");

                pg = new Pregunta();
                pg.tit1 = rs.getString("titulo1");
                pg.rmin = rs.getDouble("rangomin");
                pg.rmax = rs.getDouble("rangomax");
                pg.erro = rs.getString("errormsg");
                pg.desc = rs.getString("descripcion");
                map.put("UEQ", pg);
            } else {
                rs = db.ResultadoSelect("datospregunta", "UEC");

                pg = new Pregunta();
                pg.tit1 = rs.getString("titulo1");
                pg.rmin = rs.getDouble("rangomin");
                pg.rmax = rs.getDouble("rangomax");
                pg.erro = rs.getString("errormsg");
                pg.desc = rs.getString("descripcion");
                map.put("UEC", pg);
            }
             // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UPL	
            rs = db.ResultadoSelect("datospregunta", "UPL");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UPL", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UVL	
            rs = db.ResultadoSelect("datospregunta", "UVL");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UVL", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UDM	
            rs = db.ResultadoSelect("datospregunta", "UDM");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UDM", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UCT	
            rs = db.ResultadoSelect("datospregunta", "UCT");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UCT", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UPM	
            rs = db.ResultadoSelect("datospregunta", "UPM");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UPM", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UVB	
            rs = db.ResultadoSelect("datospregunta", "UVB");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UVB", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UEB	
            rs = db.ResultadoSelect("datospregunta", "UEB");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UEB", pg);
            // - - - - - - Pregunta - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UPE
            rs = db.ResultadoSelect("datospregunta", "UPE");

            pg = new Pregunta();
            pg.tit1 = rs.getString("titulo1");
            pg.rmin = rs.getDouble("rangomin");
            pg.rmax = rs.getDouble("rangomax");
            pg.erro = rs.getString("errormsg");
            pg.desc = rs.getString("descripcion");
            map.put("UPE", pg);
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
            db.close();
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
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UDQ	
            if (!bod.setVarDob(bod.calcularUDQ(), "UDQ", map.get("UDQ").rmin, map.get("UDQ").rmax)) {
                temp = map.get("UDQ").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("UDQ").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UVR
            if (!bod.setVarDob(bod.calcularUVR(), "UVR", map.get("UVR").rmin, map.get("UVR").rmax)) {
                temp = map.get("UVR").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("UVR").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UNR
            if (!bod.setVarDob(bod.calcularUNR(), "UNR", map.get("UNR").rmin, map.get("UNR").rmax)) {
                temp = map.get("UNR").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("UNR").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UVU
            if (!bod.setVarDob(bod.calcularUVU(), "UVU", map.get("UVU").rmin, map.get("UVU").rmax)) {
                temp = map.get("UVU").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("UVU").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UAR
            if (!bod.setVarDob(bod.calcularUAR(), "UAR", map.get("UAR").rmin, map.get("UAR").rmax)) {
                temp = map.get("UAR").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("UAR").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UAA
            if (!bod.setVarDob(bod.calcularUAA(), "UAA", map.get("UAA").rmin, map.get("UAA").rmax)) {
                temp = map.get("UAA").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("UAA").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UAL
            if (!bod.setVarDob(bod.calcularUAL(), "UAL", map.get("UAL").rmin, map.get("UAL").rmax)) {
                temp = map.get("UAL").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("UAL").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UAT
            if (!bod.setVarDob(bod.calcularUAT(), "UAT", map.get("UAT").rmin, map.get("UAT").rmax)) {
                temp = map.get("UAT").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("UAT").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UVT
            if (!bod.setVarDob(bod.calcularUVT(), "UVT", map.get("UVT").rmin, map.get("UVT").rmax)) {
                temp = map.get("UVT").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("UVT").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - URH
            if (!bod.setVarDob(bod.calcularURH(), "URH", map.get("URH").rmin, map.get("URH").rmax)) {
                temp = map.get("URH").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("URH").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UCH
            if (!bod.setVarDob(bod.calcularUCH(), "UCH", map.get("UCH").rmin, map.get("UCH").rmax)) {
                temp = map.get("UCH").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("UCH").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UCO
            if (!bod.setVarDob(bod.calcularUCO(), "UCO", map.get("UCO").rmin, map.get("UCO").rmax)) {
                temp = map.get("UCO").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("UCO").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UVN
            if (!bod.setVarDob(bod.calcularUVN(), "UVN", map.get("UVN").rmin, map.get("UVN").rmax)) {
                temp = map.get("UVN").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("UVN").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UVM
            if (!bod.setVarDob(bod.calcularUVM(), "UVM", map.get("UVM").rmin, map.get("UVM").rmax)) {
                temp = map.get("UVM").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("UVM").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UNT
            if (!bod.setVarDob(bod.calcularUNT(), "UNT", map.get("UNT").rmin, map.get("UNT").rmax)) {
                temp = map.get("UNT").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("UNT").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UERQ
            if (bod.getVarDob("CAB") != 0) {
                if (!bod.setVarDob(bod.calcularURQ(), "URQ", map.get("URQ").rmin, map.get("URQ").rmax)) {
                    temp = map.get("URQ").tit1;
                    if (temp.length() < 4) {
                        temp += ".\n " + map.get("URQ").desc;
                    }
                    error += temp;
                    return false;
                }
            } else {
                if (!bod.setVarDob(bod.calcularUER(), "UER", map.get("UER").rmin, map.get("UER").rmax)) {
                    temp = map.get("UER").tit1;
                    if (temp.length() < 4) {
                        temp += ".\n " + map.get("UER").desc;
                    }
                    error += temp;
                    return false;
                }
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UECQ
            if (bod.getVarDob("CAB") != 0) {
                if (!bod.setVarDob(bod.calcularUEQ(), "UEQ", map.get("UEQ").rmin, map.get("UEQ").rmax)) {
                    temp = map.get("UEQ").tit1;
                    if (temp.length() < 4) {
                        temp += ".\n " + map.get("UEQ").desc;
                    }
                    error += temp;
                    return false;
                }
            } else {
                if (!bod.setVarDob(bod.calcularUEC(), "UEC", map.get("UEC").rmin, map.get("UEC").rmax)) {
                    temp = map.get("UEC").tit1;
                    if (temp.length() < 4) {
                        temp += ".\n " + map.get("UEC").desc;
                    }
                    error += temp;
                    return false;
                }
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UPL
            if (!bod.setVarDob(bod.calcularUPL(), "UPL", map.get("UPL").rmin, map.get("UPL").rmax)) {
                temp = map.get("UPL").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("UPL").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UVL
            if (!bod.setVarDob(bod.calcularUVL(), "UVL", map.get("UVL").rmin, map.get("UVL").rmax)) {
                temp = map.get("UVL").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("UVL").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UDM
            if (!bod.setVarDob(bod.calcularUDM(), "UDM", map.get("UDM").rmin, map.get("UDM").rmax)) {
                temp = map.get("UDM").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("UDM").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UCT
            if (!bod.setVarDob(bod.calcularUCT(), "UCT", map.get("UCT").rmin, map.get("UCT").rmax)) {
                temp = map.get("UCT").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("UCT").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UPM
            if (!bod.setVarDob(bod.calcularUPM(), "UPM", map.get("UPM").rmin, map.get("UPM").rmax)) {
                temp = map.get("UPM").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("UPM").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UVB
            if (!bod.setVarDob(bod.calcularUVB(), "UVB", map.get("UVB").rmin, map.get("UVB").rmax)) {
                temp = map.get("UVB").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("UVB").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UEB
            if (!bod.setVarDob(bod.calcularUEB(), "UEB", map.get("UEB").rmin, map.get("UEB").rmax)) {
                temp = map.get("UEB").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("UEB").desc;
                }
                error += temp;
                return false;
            }
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UPE
            if (!bod.setVarDob(bod.calcularUPE(), "UPE", map.get("UPE").rmin, map.get("UPE").rmax)) {
                temp = map.get("UPE").tit1;
                if (temp.length() < 4) {
                    temp += ".\n " + map.get("UPE").desc;
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
