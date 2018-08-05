package DB;

import Componentes.Configuracion;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import org.apache.log4j.Logger;

public class Dao {

    Logger log = Logger.getLogger("Dao"); //Nombre que tomará en el log.
    private Configuracion conf = new Configuracion();
    private Connection conn = null;
    private Statement stmt = null;
    private String dbName;

    public Dao(String dbname) {
        this.dbName = dbname;
    }

    private Connection conectar() {

        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + dbName);
            if (c == null) {
                return null;
            }
            return c;
        } catch (Exception ex) {
            log.error("Error al conectar a " + dbName + "; " + ex.getMessage());
            return null;
        }
    }

    public ResultSet SelectSql(String sql) {

        try {

            if (conn == null || conn.isClosed()) {
                conn = conectar();
                if (conn == null) {
                    return null;
                }
            }

            stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(sql);
            //PRUEBAS //return null; // while (res.next())//{//   System.out.println (res.getString (1) + " " + res.getString (2) );//}//   if (!res.isBeforeFirst())return null;
            if (!res.isBeforeFirst()) {
                return null;
            }
            return res;
        } catch (SQLException ex) {
            log.error("Error al obtener datos ; " + ex.getMessage() + " " + ex.getSQLState());//todo name?
            return null;
        } catch (Exception ex) {
            log.error("Error al obtener datos ; " + ex.getMessage());//todo name?
            return null;
        } finally {
            //close();
        }
    }

    public Integer UpdateInsertSql(String sql) {
        int res;

        try {
            if (conn == null || conn.isClosed()) {
                conn = conectar();
                if (conn == null) {
                    return -99;
                }
            }

            stmt = conn.createStatement();
            res = stmt.executeUpdate(sql);
            return res;

        } catch (SQLException ex) {
            log.error("Error en UpdateSql ; " + ex.getMessage());
            return -99;
        } catch (Exception ex) {
            log.error("Error en UpdateSql ; " + ex.getMessage());
            return -99;
        } finally {
            // close();
        }
    }

    public ResultSet ResultadoSelect(String consulta, String param1, String param2) throws Exception {

        String select = conf.ObtenerKey(consulta);//Obtiene la cadena solicitada del archivo config

        if (param2 != null) {
            select = String.format(select, param1, param2); //Cuestionario 1, pregunta 1,2,3,....
        } else {

            if (param1 != null) {
                select = String.format(select, param1); //de la cadena solicitada, une el parametro necesario, ej.: Cuestionario 1, pregunta 2
            }
        }

        ResultSet result = SelectSql(select);

        if (result == null) {
            throw new Exception("Error al traer ResultadoSelect = null," + consulta + "," + param1 + "," + param2);
        }
        return result;
    }

    public ResultSet ResultadoSelect(String consulta, String param1) throws Exception {

        String select = conf.ObtenerKey(consulta);//Obtiene la cadena solicitada del archivo config

        if (param1 != null) {
            select = String.format(select, param1); //de la cadena solicitada, une el parametro necesario, ej.: Cuestionario 1, pregunta 2
        }

        ResultSet result = SelectSql(select);

        if (result == null) {
            throw new Exception("Error al traer ResultadoSelect = null," + consulta + "," + param1);
        }
        return result;
    }

    public int ResultadoUpdateInsert(String consulta, String param1, String param2) throws Exception { //Todo: quitar este

        String select = conf.ObtenerKey(consulta);//Obtiene la cadena solicitada del archivo config

        if (param2 != null) {
            select = String.format(select, param1, param2); //Cuestionario 1, pregunta 1,2,3,....
        } else {

            if (param1 != null) {
                select = String.format(select, param1); //de la cadena solicitada, une el parametro necesario, ej.: Cuestionario 1, pregunta 2
            }
        }

        int result = UpdateInsertSql(select);

        if (result == -99) {
            throw new Exception("Error al traer ResultadoUpdateInsert = null," + consulta + "," + param1 + "," + param2);
        }
        return result;
    }

    /**
     * Recibe el nombre de la consulta el cual busca en el archivo de
     * configuración, y une las parámetros del arreglo con la consulta
     *
     * @param consulta : Nombre de la consulta en el archivo de configuración
     * @param parametros : Arreglo de variables en String las cuales son los
     * parámetros de la consulta
     * @return: Retorna excepción en caso de error, si no un resultset
     * @throws Exception
     */
    public int ResultadoUpdateInsert(String consulta, String[] parametros) throws Exception {

        String select = conf.ObtenerKey(consulta);//Obtiene la cadena solicitada del archivo config
        select = String.format(select, (Object[]) parametros);
        int result = UpdateInsertSql(select);

        if (result == -99) {
            throw new Exception("Error al traer ResultadoUpdateInsert = null," + consulta + "String[]:" + parametros);
        }
        return result;
    }

    public void close() {
        try {
            if (stmt != null) { // Todo: bug? SQL error or missing database (Connection is closed)
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            if(!ex.getMessage().contains("missing "))
            log.error("Error al cerrar conexión; " + ex.getMessage());
        } catch (Exception ex) {
            if(!ex.getMessage().contains("missing "))
            log.error("Error al cerrar conexión; " + ex.getMessage());
        }
    }
}