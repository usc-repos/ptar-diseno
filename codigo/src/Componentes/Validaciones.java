package Componentes;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.apache.log4j.Logger;

public class Validaciones {

    private static Logger log = Logger.getLogger("Validaciones"); //Nombre que tomará en el log.
    DateFormat dtf = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Comprueba si una cadena es convertible en double entre rangos
     *
     * @param str cadena
     * @param min valor mínimo que puede tener
     * @param max valor máximo que puede tener
     * @return
     */
    public boolean EsDobleEntre(String str, double min, double max) {

        try {
            if (str.matches(".*[A-Za-z].*")) {
                return false;//Filtro de caracteres permitidos como sufijos literales Numéricos
            }
            double num = Double.parseDouble(str.trim());

            if (num >= min && num <= max) {
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Comprueba si un double está entre rangos
     *
     * @param in variable por probar
     * @param min valor mínimo que puede tener
     * @param max valor máximo que puede tener
     * @return
     */
    public boolean EsDobleEntre(double in, double min, double max) {

        try {
            if (in >= min && in <= max) {
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Comprueba si una cadena es convertible en entero entre rangos
     *
     * @param str cadena
     * @param min valor mínimo que puede tener
     * @param max valor máximo que puede tener
     * @return
     */
    public boolean EsEnteroEntre(String str, int min, int max) {

        try {
//            if (str != null && !str.isEmpty()) {
//                return false;
//            }

            if (str.matches(".*[A-Za-z].*")) {
                return false;//Filtro de caracteres permitidos como sufijos literales
            }
            int num = Integer.parseInt(str.trim());

            if (num >= min && num <= max) {
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Comprueba si un entero está entre rangos
     *
     * @param num entero
     * @param min valor mínimo que puede tener
     * @param max valor máximo que puede tener
     * @return
     */
    public boolean EsEnteroEntre(int num, int min, int max) {

        try {
            if (num >= min && num <= max) {
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * *
     * Comprueba si una cadena es convertible en doble, devuelve el número, caso
     * contrario devuelve 0
     *
     * @param str cadena con el posible número
     * @return número o cero (0)
     */
    public Double EsNumero(String str) {

        double d;

        try {
            d = Double.parseDouble(str);
        } catch (NumberFormatException ex) {
            return 0.0;
        } catch (Exception ex) {
            return 0.0;
        }
        return d;
    }

    /**
     * Retorna true si una fecha este entre dos fechas
     *
     * @param date: Fecha a comparar
     * @param min:Fecha mínima
     * @param max:Fecha màxima
     * @return
     */
    public boolean EsFechaEntre(Date dateArg, Date min, Date max) {

        try {
            if (min.compareTo(dateArg) != 1 && (max.compareTo(dateArg) != -1)) {
                return true; //0 = son iguales, -1 = dateArg es mayor, 1 si dateArg es menor  
            }
            return false;
        } catch (Exception ex) {
            log.error("Error EsFechaEntre " + ex.getMessage());
            return false;
        }
    }

    /**
     * Retorna 'true' si la fecha A es mayor que la B
     *
     * @return
     */
    public boolean EsDateMayor(Date a, Date b) {
        try {
            if (a.compareTo(b) == -1) {
                return true;
            }
        } catch (Exception ex) {
            log.error("Error EsDateMayor " + ex.getMessage());
            return false;
        }
        return false;
    }

    /**
     * Retorna 'true' si el año de la fecha A es mayor que la B
     *
     * @return
     */
    public boolean EsCalendarYearMayor(Calendar a, Calendar b) {
        try {
            if (a.get(Calendar.YEAR) > b.get(Calendar.YEAR)) {
                return true;
            }

        } catch (Exception ex) {
            log.error("Error EsCalendarYearMayor " + ex.getMessage());
            return false;
        }
        return false;
    }

    /**
     * Retorna una fecha actual formateada sin Horas,Mins
     *
     * @return fecha actual formateada o null si falla
     */
    public Date FechaActual() {
        try {
            Date today = new Date();
            Date date = dtf.parse(dtf.format(today));
            return date;
        } catch (ParseException ex) {
            return null;
        }
    }

    /**
     * Retorna una fecha formateada sin Horas,Mins
     *
     * @return fecha formateada o null si falla
     */
    public Date FormatFecha(Date date) {
        try {
            Date fecha = dtf.parse(dtf.format(date));
            return fecha;
        } catch (ParseException ex) {
            return null;
        }
    }

    /**
     * * Retorna una fecha de la cadena, parseada y formateada sin Horas,Mins
     *
     * @param sdate: cadena que se convertirá
     * @return fecha o null si falla
     */
    public Date CadenaAFecha(String sdate) {
        try {
            Date date = dtf.parse(sdate);
            return date;
        } catch (ParseException ex) {
            return null;
        }
    }

    /**
     * Formatea una cadena de contenido numerico y formatea sus decimales
     *
     * @param str : Numero que se quiere formatear ej. 32.4567
     * @param formato : Formato que se desea ej: "0.00"
     * @return : ej. 32.45
     */
    public String CadenaFormatDecimal(String str, String formato) {
        try {
            DecimalFormat decimalFormat = new DecimalFormat(formato);
            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            dfs.setDecimalSeparator('.');
            decimalFormat.setDecimalFormatSymbols(dfs);
            str = decimalFormat.format(Double.valueOf(str));
            return str;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Formatea una double y formatea sus decimales por aproximación
     *
     * @param num : Numero que se quiere formatear ej. 0.0467
     * @param formato : es el formato que se desea ej: "#.##" = "0.00"
     * @return : ej. 0.05 redondeado
     */
    public String DobleFormatoStringCeil(double num, String formato) {
        try {
            DecimalFormat df = new DecimalFormat(formato);
            df.setRoundingMode(RoundingMode.CEILING);
            return df.format(num).replace(",", ".");

        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Formatea un double sin redondedar
     *
     * @param num : Numero que se quiere formatear ej. 0.49987
     * @param formato : es el formato que se desea ej: "#.##" = "0.00"
     * @return : ej. 0.49 Sin redondeo
     */
    public String DobleFormatoStringFloor(double num, String formato) {
        try {
            DecimalFormat df = new DecimalFormat(formato);
            df.setRoundingMode(RoundingMode.FLOOR);
            return df.format(num).replace(",", ".");

        } catch (Exception ex) {
            return null;
        }
    }

    /***
     * redondeda un double acercando sus decimales a 0.5 por debajo ó 1 por arriba
     * ejemplo: 2.1 --> 2.5  , 2.6 --> 3
     * @param num
     * @return 
     */
    public double DobleRedondeo05(double num) {

        double x = (num - (int) (num));

        if (x >= 0 && x <= 0.5) {
            num = (int) num + 0.5;
        } else {
            num = (int) num + 1;
        }

        return num;

    }

    /**
     * Devuelve un entero año formato: YYYY
     *
     * @param cdate Calendar
     * @return entero, -1 en error
     */
    public int CalendarAnnio(Calendar cdate) {
        try {
            int year = cdate.get(Calendar.YEAR);
            return year;
        } catch (Exception ex) {
            return -1;
        }
    }

    /**
     * Convierte un Doble a Cadena para evitar la notación cientifica de Java Se
     * usa en algunas variables del BOD o el UI que pueden llegar a valer mas de
     * 6 digitos ej. 10000000 = "1E07"
     *
     * @param num numero double por convertir
     * @return cadena con formato numérico
     */
    public String DobleAcadena(Double num) { //
        return DobleFormatoStringCeil(num, "###.#################");
    }
}
