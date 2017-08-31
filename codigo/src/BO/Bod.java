package BO;

import Componentes.Configuracion;
import Componentes.Util;
import Componentes.Validaciones;
import DB.Dao;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import java.io.File;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class Bod {

    private Field field;
    private Util util = new Util();
    public String Generalpath = "";//path de recursos del proyecto
    static Logger log = Logger.getLogger("BOD");
    private Validaciones valida = new Validaciones();
    private Configuracion conf = new Configuracion();
    //private DateFormat sqlidateformat = new SimpleDateFormat("yyyy-MM-dd"); //Formato de fechas para Sqlite;
    private Dao db = new Dao(conf.ObtenerKey("rutabasedatos") + File.separator + conf.ObtenerKey("nombrebasedatos"));
    //-------------------------------------------------------------------------------------------------------------------- Datos de Entrada
    private int FCH; // INT, min 1950 max 2030 , Fecha 
    private double TML; // #,## 1, min 5 max 50 , Temperatura media de la localidad Cº 
    private int PCI; // INT, min 1 max 199000 , Población del censo más antiguo (Pci) hab. 
    private int PUC; // INT, min 1 max 199000 , Población del censo más reciente (Puc) hab. 
    private int TCI; // INT, min 1950 max 2030 , Año del censo (Tci) 
    private int TUC; // INT, min 1950 max 2030 , Año del censo (Tuc) 
    private int PDI; // INT, min 10 max 40 , Período de diseño años 
    private double CRE; // #,## 2, min 0.7 max 0.9 , Coeficiente de retorno (R) 
    private int DMP; // INT, min 1 max 400 , Dotación máxima de agua potable (D) L/hab*día
    //--------------------------------------------------------------------------------------------------------------------ProyeccionPoblacional
    private int MEA; // INT, min max , Método Aritmético 
    private int MEG; // INT, min max , Método Geométrico 
    private double TCA; // min 0.01 max 100000 , Tasa de crecimiento poblacional (r) 
    private double TCG; // min 0.000001 max 3 , Tasa de crecimiento poblacional (r) 
    private int PPA; // INT, min 2 max 200000 , Población proyectada o futura (Pf ) hab 
    private int PPG; // INT, min 2 max 200000 , Población proyectada o futura (Pf ) hab 
    private String NCR; // min max , Nivel de complejidad según el RAS 
    //-------------------------------------------------------------------------------------------------------------------- Cálculo de caudales de diseño 
    private double CAR; // #,## 2, min 0,01 max 296 , Contribución de aguas residuales (C) L/s 
    private double KAR; // #,## 2, min 0.5 max 25500 , m³/día 
    private int QIF; // INT, min max , Extensión de la red de alcantarillado
    private int QIA; // INT, min max , Área servida 
    private double QEL; // #,## 2, min 0.1 max 500 , Longitud (L) Km <HTML><b>Extensión de la red de alcantarillado</b></HTML>
    private double QET; // #,## 2, min 0.01 max 1 , Tasa de infiltración (INF) L/s*km 
    private double QEC; // #,## 2, min 0.1 max 500 , Qinf L/s Caudal de infiltración (Qinf) método de acuerdo por longitud 1
    private double QEK; // #,## 2, min 0.01 max 35000 , m³/día Caudal de infiltración (Qinf) método de acuerdo por longitud 2
    private double QAA; // #,## 2, min 0.001 max 50000 , Área (A) ha <HTML><b>Extensión de la red de alcantarillado</b></HTML>
    private double QAI; // #,## 2, min 0.1 max 0.4 , Aporte por infiltración (INF) 
    private double QAC; // #,## 2, min 0.0001 max 20000 , Qinf L/s Caudal de infiltración (Qinf) acuerdo por área 1
    private double QAK; // #,## 2, min 0.00864 max 1750000 , m³/día Caudal de infiltración (Qinf) acuerdo por área 2
    private int QCE; // INT, min max , Cálculo por Área servida <HTML><b>Caudal por conexiones erradas (QCE)</b></HTML>
    private double QCA; // #,## 2, min 0.001 max 50000 , Área (A) ha 
    private double QCM; // #,## 2, min 0.1 max 0.2 , Aporte máximo (CE) L/s*ha 
    private double QCC; // #,## 2, min 0.0001 max 10000 , Qce l/s Caudal por conexiones erradas(Qce) 1 (Área servida)
    private double QCK; // #,## 2, min 0.001 max 900000 , m³/día Caudal por conexiones erradas(Qce) 2 (Área servida)
    private int QNL; // INT, min max , Cálculo por Nivel de complejidad 
    private double QNC; // #,## 2, min 0.0001 max 0.15 , Qce l/s Caudal por conexiones erradas(Qce) 1 (Nivel de complejidad)
    private double QNK; // #,## 2, min 0.00864 max 13 , m³/día Caudal por conexiones erradas(Qce) 2 (Nivel de complejidad)
    private int IOA; // INT, min max , Industrial <HTML><b>Otros aportes (industrial, comercial o institucional)</b></HTML>
    private int IRM; // INT, min max , Si No <HTML><b>¿Cuenta con registros de medición de caudales de origen industrial?</b></HTML>
    private int IAI; // INT, min 0 max 50000 , Área industrial ha 
    private double IPI; // #,## 2, min 0.4 max 1.5 , Aporte industrial L/s*ha 
    private double IQI; // #,## 2, min 0 max 75000 , Caudal industrial (Qi) l/s Caudal industrial (Qi) 1
    private double IKI; // #,## 2, min 0 max 6480000 , m³/d Caudal industrial (Qi) 2
    private double IQ2; // #,## 2, min 0 max 2000 , Caudal medio (QImed) l/s Caudal medio (QImed) 1
    private double IK2; // #,## 2, min 0 max 180000000 , m³/d Caudal medio (QImed) 2
    private double IQ3; // #,## 2 min 0 max 2000 , Caudal máximo (QImax) l/s Caudal máximo (QImax) 1
    private double IK3; // #,## 2 min 0 max 180000000 , m³/d Caudal máximo (QImax) 2
    private double ICM; // #,## 2, min 0.1 max 5 , Coeficiente de mayoración 
    private double IQ1; // #,## 2, min 0 max 2000 , Caudal mínimo (QImin) l/s Caudal mínimo (QImin) 1
    private double IK1; // #,## 2, min 0 max 180000000 , m³/d Caudal mínimo (QImin) 2
    private double ICC; // #,## 2, min 0.1 max 5 , Coeficiente de consumo mínimo 
    private int COA; // INT, min max , Comercial 
    private double CAC; // #,## 2, min 0 max 50000 , Área comercial ha 
    private double CPC; // #,## 2, min 0.4 max 0.5 , Aporte comercial L/s*ha 
    private double CQC; // #,## 2 min 0 max 25000 , Caudal comercial (Qc) l/s Caudal comercial (Qc) 1
    private double CKC; // #,## 2 min 0 max 2160000 , m³/d Caudal comercial (Qc) 2
    private int YOA; // INT, min max , Institucional 
    private double YAI; // #,## 2 min 0 max 50000 , Área institucional ha 
    private double YPI; // #,## 2, min 0.4 max 0.5 , Aporte institucional L/s*ha 
    private double YQI; // #,## 2, min 0 max 25000 , Caudal institucional (Qin) l/s Caudal institucional (Qin) 1
    private double YKI; // #,## 2, min 0 max 2160000 , m³/d Caudal institucional (Qin) 2
    private double Q2C; // #,## 2, min 0.01 max 1000000 , Qmed l/s Caudal medio diario 
    private double Q2K; // #,## 2, min 0.5 max 87000000 , m³/d Caudal medio diario 2
    private double Q3M; // #,## 2, min 0.1 max 5 , Coeficiente de mayoración o variación horaria(K)  
    private int Q3R; // INT, min max , ¿Desea recalcular K a partir de la Ecuación de Harmon? 
    private double Q3C; // #,## 2, min 0.01 max 1000000 , Qmax l/s Caudal máximo diario 
    private double Q3K; // #,## 2, min 0.5 max 87000000 , m³/d Caudal máximo diario 2
    private double Q1H; // #,## 2, min 0.1 max 5 , Coeficiente de horario de menor consumo (K3)  
    private double Q1C; // #,## 2, min 0 max 1000000 , Qmin l/s Caudal mínimo diario 1
    private double Q1K; // #,## 2, min 0 max 87000000 , m³/d Caudal mínimo diario 2
    private int ICO; // INT, min max , Editar los coeficientes de Mayoración o Consumo Mínimo Deberia ir posicion 30
    private int QCD; // INT, min max , Si No 
    //-------------------------------------------------------------------------------------------------------------------- Características iniciales del agua residual
    private double CAT; // #,## 2, min 5 max 50, Temperatura °C 
    private double CAB; // #,## 2, min 50 max 1000, Demanda Biológica de Oxígeno (DBO5) mg/l o g/m³ 
    private double CAQ; // #,## 2, min 5 max 1000, Demanda Bioquímica de Oxígeno (DQO) mg/l o g/m³ 
    private double CAS; // #,## 2, min 0 max 1000, Sólidos Suspendidos Totales (SST) mg/L 
    private double CAV; // #,## 2, min 5 max 500, Sólidos Suspendidos Volátiles (SSV) mg/L	 	
    private double CVS; // #,## 2, min 0.1 max 1.1, Relación SSV/SST en el afluente	 		 	
    //-------------------------------------------------------------------------------------------------------------------- LagunaAnaerobia
    private double LCO; // #,## 1, min 100 max 350 , Carga Orgánica Volumétrica (COV LA) g/m³*Día 
    private double LCE; // #,## 1, min 30 max 80 , Eficiencia de DBO5 estimada %LA Calculo de Eficiencia y Efluente de DBO5
    private double LAE; // #,## 1, min 5 max 1000 , DBO5 en el efluente de la laguna Anaerobia (SLA) mg/L 
    private double LVU; // #,## 1, min 0.1 max 10000000 , Volumen Útil (VLA) m³ 
    private double LTH; // #,## 1, min 0.5 max 10 , Tiempo de retención hidráulico (TRLA) días 
    private double LVR; // #,## 1, min 0.1 max 10000000 , Volumen Útil Recalculado (VLA) m³ 
    private double LAU; // #,## 1, min 2.0 max 5.0 , Altura Útil (hLA) m Calculo de Área Superficial 
    private double LAS; // #,## 1, min 0.1 max 10000000 , Área Superficial m² 
    private int LLA; // INT, min max , 1:3 2:3 Relación Largo: Ancho
    private double LAA; // #,## 1, min 0.1 max 10000000 , Ancho (aLA) m 
    private double LAL; // #,## 1, min 0.1 max 10000000 , Largo (LLA) m 
    private int LAP; // INT, min 2 max 3 , Pendiente n <HTML><div>&#8895;</div></HTML> 
    private double LAI; // #,## 1, min 18 max 27 , Ángulo de inclinación de la pendiente <HTML>&#945;</HTML> 
    private double LAB; // #,## 1, min 0.5 max 10 , Borde libre m 
    //-------------------------------------------------------------------------------------------------------------------- Laguna Facultativa Secundaria
    private double FTE; // #,## 1, min 0.1 max 100 , Tasa de evaporación (e) mm/Día 
    private double FCO; // #,## 1, min 0.1 max 10000000 , Carga Orgánica Volumétrica (COVLF) g/m³*Día 
    private double FCA; // #,## 1, min 1 max 2 , Altura Útil (hLF) m Cálculo de Área Superficial
    private double FAS; // #,## 1, min 0.1 max 10000000 , Área Superficial (ASLF) m² 
    private double FTR; // #,## 1, min 0.5 max 10 , Tiempo de retención hidráulico (TRLF) días 
    private double FED; // #,## 1, min 1 max 100 , Eficiencia de DBO5 estimada %LF 
    private double FUV; // #,## 1, min 0.1 max 10000000 , Volumen Útil (VLA) m³ 
    private double FSA; // #,## 1, min 0.1 max 10000000 , Ancho (aLF) m Relación Largo: Ancho
    private double FSL; // #,## 1, min 0.1 max 10000000 , Largo (LLF) m 
    private int FAP; // INT, min 2 max 3 , Pendiente n <HTML><div>&#8895;</div></HTML> 
    private double FAI; // #,## 1, min 18 max 27 , Ángulo de inclinación de la pendiente <HTML>&#945;</HTML> 
    private double FAB; // #,## 1, min 0.5 max 10 , Borde libre m 
    //--------------------------------------------------------------------------------------------------------------------
    private double RPL; // #,## 2, min 0.05 max 2 , Profundidad de la lámina de agua determinada por las unidades del conducto o tuberías afluente (h). m 
    private int RTR; // INT, min max , Seleccione el tipo de rejilla mm Seleccionar el tipo de rejilla.
    private double REB; // #,## 2, min max , Espesor de la barra (t) mm 
    private int REL; // INT, min 3 max 100 , Espaciamiento libre entre barras (a) mm 
    private int RIB; // INT, min max , Inclinación de las barras (θ) ° 
    private int RBI; // INT, min 45 max 90 , Manual Mecánica Tipo de limpieza
    private double RPM; // #,## 2, min 0.5 max 1.5 , Velocidad máxima (Vmax) m/s Velocidad de paso entre barras (V)
    private double RPN; // #,## 2, min 0.5 max 1.5 , Velocidad mínima (Vmin) m/s 
    private double RAU; // #,## 2, min 0.00001 max 200 , Área útil entre las barras para el escurrimiento (Au) m2 
    private double RER; // #,## 2, min 0.4 max 1 , Eficiencia (E) Eficiencia de las rejillas en función del espesor (t) y el espaciamiento entre barras (a)
    private double RAT; // #,## 5, min 0.00001 max 200 , Área total de las rejillas (incluidas las barras) (S) m2 
    private double RAR; // #,## 4, min 0.0002 max 100 , Ancho de rejillas (b) m 
    private double RCP; // #,## 2, min 0.5 max 2 , Velocidad máxima. (Vmax) m/s Chequeo de velocidades de paso entre barras para un ancho (b)
    private double RCM; // #,## 2, min 0.5 max 2 , Velocidad media (Vmed) m/s 
    private double RLC; // #,## 2, min 1 max 900 , Longitud del canal (L) m 
    private double RVM; // #,## 2, min 1 max 10000000 , Velocidad aproxim. Vo para Caudal Máx. m/s 
    private double RVN; // #,## 2, min 1 max 1000000 , Velocidad aproxim. Vo para Caudal Med. m/s 
    private int RNB; // INT, min 0.1 max 10000000 , Nb barras de Número de barras (Nb) y espaciamientos (Ne) en la rejilla 
    private double RNE; // #,## 2, min 0.1 max 10000000 , Ne espaciamientos de 
    private double RPC; // #,## 2, min 1 max 10000000 , Pérdida de carga en la rejilla limpia (hL) m Pérdida de carga en la rejilla 
    private double RPS; // #,## 2, min 1 max 10000000 , Pérdida de carga en la rejilla 50% sucia (hL) m 
    private double RPB; // #,## 2, min 0.72 max 2.42 , β Pérdida de carga calculada por la ecuación de Kirshmer
    private double RPH; // #,## 2, min 0.001 max 10000000 , hF m 
    //--------------------------------------------------------------------------------------------------------------------
    private double DAN; // #,## 1, min 7.6 max 244 , Ancho nominal (W) cm Dimensionamiento de canaleta Parshall
    private double DCK; // #,## 2, min 0.176 max 6.101 , Coeficiente K m Profundidad de la lámina de agua (H) para Qmax, Qmed y Qmin.
    private double DCN; // #,## 3, min 1.547 max 1.606 , Coeficiente n n 
    private double DP1; // #,## 1, min 1 max 1000000 , Hmax m 
    private double DP2; // #,## 1, min 1 max 1000000 , Hmed m 
    private double DP3; // #,## 1, min 1 max 1000000 , Hmin m 
    private double DRZ; // #,## 1, min 1 max 1000000 , Resalto (Z) m 
    private double DPA; // #,## 1, min 46.6 max 244 , A cm Dimensiones del medidor Parshall
    private double DPB; // #,## 1, min 45.7 max 239 , B cm 
    private double DPC; // #,## 1, min 17.8 max 274.5 , C cm 
    private double DPD; // #,## 1, min 25.9 max 340 , D cm 
    private double DPE; // #,## 1, min 45.7 max 91.5 , E cm 
    private double DPF; // #,## 1, min 15.2 max 61 , F cm 
    private double DPG; // #,## 1, min 30.5 max 91.5 , G cm 
    private double DPK; // #,## 1, min 2.5 max 7.6 , K cm 
    private double DPN; // #,## 1, min 5.7 max 22.9 , N cm 
    private double DAH; // #,## 2, min 1 max 1000000 , Altura máxima de lámina de agua en el desarenador (H) m Dimensionamiento del desarenador
    private double DAB; // #,## 2, min 1 max 1000000 , Ancho del desarenador (b) m 
    private double DVF; // #,## 2, min 0.1 max 0.5 , Velocidad del flujo (V) m/s 
    private double DLD; // #,## 2, min 1 max 1000000 , Longitud del desarenador (L) m 
    private double DAL; // #,## 2, min 1 max 1000000 , Área longitudinal del desarenador (A) m² 
    private double DEM; // #,## 2, min 1 max 1000000 , Estimación de material retenido (q) m³/día 
    private int DFL; // INT, min 1 max 45 , Frecuencia de limpieza (t) días 
    private double DUP; // #,## 2, min 0.5 max 1000000 , Profundidad útil depósito inferior de arena (p) m
    //-------------------------------------------------------------------------------------------------------------------- Reactor UASB
    private double UDQ; // #,## 2, min 0.00005 max 1000000 // Cálculo de carga afluente media de DQO (Lo) Kg DQO/día 
    private double UTH; // #,## 1, min 6 max 12 // Tiempo de retención hidráulico (TRHUASB) horas 
    private double UVR; // #,## 0, min 1 max 500000 // Determinación del volumen total del reactor (V) m³ 
    private int UNR; // INT, min 2 max 100 // Adopción de número de unidades de reactores NR 
    private double UVU; // #,## 1, min 0.01 max 250000 // Volumen por unidad de reactor (Vr) m³ 
    private double UHR; // #,## 1, min 3 max 6 // Altura del reactor (H) m 
    private double UHS; // #,## 1, min 1 max 3 // Altura compartimiento de sedimentación (hs) m 
    private double UHD; // #,## 1, min 2 max 4 // Altura compartimiento de digestión (hd) m 
    private double UAR; // #,## 1, min 1 max 84000 // Área por unidad de reactor (Ar) m² 
    private double UAA; // #,## 1, min 0.7 max 205 // Ancho (ar) m 
    private double UAL; // #,## 1, min 1.4 max 500 // Longitud (Lr) m 
    private double UAT; // #,## 1, min 1 max 9000000 // Área total (AT) m² Verificación de área total, volumen y tiempo de retención hidráulico
    private double UVT; // #,## 1, min 1 max 50000000 // Volumen total (VT) m³ 
    private double URH; // #,## 1, min 4 max 14 // Tiempo de retención hidráulico (TRHUASB) horas 
    private double UCH; // #,## 2, min 0.01 max 0.02 // Carga hidráulica volumétrica (CHV) m³/( m³*d) Verificación de cargas aplicadas
    private double UCO; // #,## 2, min 0.00005 max 0.02 // Carga orgánica volumétrica (COV) (Kg DQO)/( m³* d) 
    private double UVN; // #,## 2, min 0.1 max 0.8 // Va para Qmed m/h Cálculo de Velocidad ascensional Va para caudal medio y máximo 
    private double UVM; // #,## 2, min 0.1 max 1.6 // Va para Qmax m/h 
    private double UAD; // #,## 2, min 1.5 max 3 // a) Área de influencia por tubo distribuidor (Ad) m² Sistema de distribución del afluente
    private double UNT; // #,## 0, min 1 max 3000000 // b.) Número de tubos Nd m² 
    private double UER; // #,## 1, min 60 max 80 // Eficiencia de remoción de DQO del sistema (EDQO) % 
    private double UEC; // #,## 1, min 2 max 200 // Estimación de la concentración de DQO en el efluente (S) mg/L 
    private double USA; // #,## 1, min 2.5 max 3.5 // Ancho (as) M Compartimiento de sedimentación
    private double USC; // #,## 1, min 1 max 100 // Número de compartimientos ns 
    private double UVA; // #,## 2, min 0.1 max 1.6 // Verificación de velocidad ascensional en el compartimiento de sedimentación Vsed m/h 
    private double UCP; // #,## 2, min 0.1 max 0.2 // Coeficiente de producción de sólidos (Y) 
    private double UPL; // #,## 2, min 0.000005 max 200000 // Producción lodo esperada (Plodo) Kg/SSTdía 
    private double UVL; // #,## 2, min 0.0000001 max 5000 // Volumen de lodo (Vlodo) m³/día 
    private double USM; // #,## 3, min 0.05 max 0.3 // Coeficiente de producción de sólidos en términos de DQO (Yobs) KgSSVlodo/KgDQO Producción de metano
    private double UDM; // #,## 2, min 0.00002 max 500000 // Carga de DQO convertida en metano (DQO CH4 ) Kg DQO/ d 
    private double UCT; // #,## 2, min 2.9 max 2.5 // Factor de corrección para la temperatura operacional del reactor F(t) (Kg DQO/m³) 
    private double UPM; // #,## 2, min 0.000009 max 207000 // Producción volumétrica de metano QCH4 (m3/d) 
    private double UCM; // #,## 2, min 60 max 80 // Concentración de metano (CCH4) % 
    private double UVB; // #,## 2, min 0.000016 max 260000 // Producción volumétrica de biogás Qbiogas m³/d 
    private double UCB; // #,## 2, min 20000 max 50000 // Poder calorífico del biogás (Pc) kJ/m³ Estimación de energía generada 
    private double UEB; // #,## 2, min 0.33 max 13000000000 // Energía bruta producida (E ) kJ /d 
    private double URE; // #,## 1, min 10 max 80 // Rendimiento global de transformación de energía eléctrica (R%) % 
    private double UPE; // #,## 2, min 278 max 18000 // Potencial de electricidad disponible kw 
    //-------------------------------------------------------------------------------------------------------------------- Filtro Percolador
    private double PCO; // #,## 2, min 0.5 max 1,  Selección de la Carga Orgánica Volumétrica (Cv)
    private double PVM; // #,## 2, min 0.01 max 10000000,  Cálculo del volumen del medio de soporte (V)
    private double PPM; // #,## 1, min 2 max 3,  Profundidad del medio de soporte (H)
    private double PAS; // #,## 2, min 0.01 max 10000000,  
    private double PH2; // #,## 1, min 0.01 max 100,  Para Qmed
    private double PH3; // #,## 1, min 0.01 max 100,  Para Q max
    private int PFU; //   INT,  min 1 max 3,  Número de unidades o filtros (n)
    private double PFA; // #,## 1, min 0.001 max 10000000,  Área por filtro (a)
    private double PFD; // #,## 1, min 0.05 max 16000,  Diámetro por filtro (D)
    private double PER; // #,## 1, min 20 max 100,  Eficiencia de remoción de DQO del filtro percolador (EFP)
    private double PCD; // #,## 2, min 160 max 0.001,  Concentración de DQO en el efluente final del sistema de tratamiento Reactor UASB + Filtro precolador (Sfinal)
    private double PEL; // #,## 2, min 0.01 max 160000,   Estimación de la producción de lodo (Plodo)
    private double PVL; // #,## 2, min 0.01 max 20000,  Producción volumétrica de lodo (Vlodo)
    private int PSS; // #,## 0, min 14 max 30,  Tasa de escurrimiento superficial (qs)
    private double PSA; // #,## 2, min 0.001 max 40000,  Área del sedimentador (As)
    private double PPS; // #,## 1, min 2.5 max 4.5,  Profundidad del sedimentador (Hsed)
    private int PNU; //   INT,  min 1 max 3,  Número de unidades de sedimentación (n)
    private double PAX; // #,## 1, min 0.01 max 10000,  Área por sedimentador (as)
    private double PDX; // #,## 1, min 0.01 max 200,  Diámetro por sedimentador (D)
    private double PAT; // #,## 2, min 2 max 10000000,  Estimación de área útil requerida para la técnología uerida para la tecnología Reactor UASB + Filtro percolador f
    //-------------------------------------------------------------------------------------------------------------------- Lodos Activados Convencional
    private double MCH; // #,## 1, min 30.00 max 120 , Carga hidráulica superficial (OR) m³/m²*día SEDIMENTADOR PRIMARIO
    private double MAR; // #,## 1, min 0.01 max 2880000 , Cálculo del área requerida (Asp) m² 
    private double MPS; // #,## 1, min 2.50 max 5.50 , Profundidad del sedimentador (HSP) m 
    private double MTR; // #,## 1, min 1 max 3 , Verificación del tiempo de retención hidráulico (TRHSP) horas 
    private double MGC; // #,## 1, min 1 max 1000 , Diámetro (D) m Configuración geométrica del sedimentador primario
    private double MGA; // #,## 1, min 3 max 24 , Ancho m 
    private double MGL; // #,## 1, min 1 max 1000 , Longitud m 
    private double MDB; // #,## 1, min 0 max 70 , , Demanda Bioquímica de Oxígeno deseada en el efluente (DBOe),  mg/L TANQUE DE AIREACIÓN
    private double MSE; // #,## 1, min 0 max 70 , , Sólidos Suspendidos Totales deseado en efluente (Xe),  mg/L 
    private double MRD; // #,## 1, min 0.1 max 1.1 , Relación gDBO/gSST en el efluente 
    private double MST; // #,## 1, min 1500 max 4000 , , Sólidos suspendidos totales en el lodo (licor mixto) del tanque de aireación – SSTA (Xa),  mg/L 
    private double MSR; // #,## 1, min 7000 max 15000 , Sólidos del lodo recirculado (Xu) mg/L 
    private double MCC; // #,## 2, min 0.1 max 1 , Coeficiente de producción celular (Y) mgSSV/mgDBO 
    private double MTE; // #,## 3, min 0 max 0.3 , Tasa específica de respiración endógena (Kd) días-¹ 
    private double MEL; // #,## 1, min 3 max 15 , Edad del lodo (θc) días 
    private double MCE; // #,## 3, min 0.01 max 2 , , Cálculo del coeficiente de producción celular ajustado por la pérdida por respiración endógena (Yobs),  mgSSV/mgDBO 
    private double MDP; // #,## 2, min 0 max 77 , DBO particulada (Sp) mg/L Cálculo de la DBO soluble en el efluente y la eficiencia de remoción de DBO5
    private double MDS; // #,## 1, min 0 max 523 , DBO soluble (Se) mg/L 
    private double MES; // #,## 1, min 13 max 100 , Eficiencia de remoción de DBO soluble % 
    private double MED; // #,## 1, min 89 max 100 , Eficiencia global de remoción de DBO % 
    private double MPL; // #,## 1, min 0 max 13300000 , Cálculo de producción de lodo esperada - ∆X kg SV/d 
    private double MLT; // #,## 1, min 0 max 13300000 , , Cálculo de producción de lodo esperada - ∆X T ,  kg ST/d 
    private double MVL; // #,## 2, min 0 max 16600000 , Cálculo del volumen de lodo purgado por día (Vlodo) m³/d 
    private double MVT; // #,## 1, min 0.16 max 4100000 , VTA m³ Cálculo del volumen de lodo del tanque de aireación (VTA) y tiempo retención hidráulico (TRH)
    private double MVH; // #,## 1, min 1.15 max 4.4 , TRH horas 
    private double MPT; // #,## 2, min 2.00 max 7.00 , Profundidad del tanque de aireación (HTA) m Cálculo del área superficial del tanque de aireación (ATA)
    private double MAS; // #,## 1, min 0.08 max 590000 , Área superficial (ATA) m² 
    private double MAM; // #,## 3, min 9800000 max 334000000 , Cálculo de la relación Alimento/Microorganismos (A/M) kgDBO / kgSSVTA 
    private double MRM; // #,## 2, min 0.1 max 0.6 , Cálculo de la recirculación mínima recomendada (r) 
    private double MEC; // #,## 1, min 0.08 max 1100000 , , Verificación de la producción de lodo ΔX a partir del V TA , edad del lodo y concentración de SSTA,  kg ST/d 
    private double MFS; // #,## 1, min 15 max 29 , Selección de la tasa de flujo superficial TFS m3/m2*d SEDIMENTADOR SECUNDARIO
    private double MSA; // #,## 1, min 0.06 max 3000000 , Área de sedimentación (As) m² 
    private double MMS; // #,## 1, min 1.4 max 550000000 , Masa de sólidos (M) KgSS/día , Verificación de la tasa de aplicación de sólidos
    private double MHS; // #,## 2, min 3.70 max 10 , Profundidad de sedimentación (Hss) m 
    private double MAT; // #,## 2, min 0.14 max 6500000 , Área total requerida por la tecnología m² 
    //-------------------------------------------------------------------------------------------------------------------- 
    //public boolean CAQB = false; //Usada en Lodos Activados Convencional, para saber si se usa CAQ ó CAB por defecto se usa CAQ 
    public String errorFCH;
    public String errorTML;
    public String errorPCI;
    public String errorPUC;
    public String errorTCI;
    public String errorTUC;
    public String errorPDI;
    public String errorCRE;
    public String errorDMP;
    public String errorTCA;
    public String errorTCG;
    public String errorPPA;
    public String errorPPG;
    public String errorNCR;
    public String errorCAR;
    public String errorKAR;
    public String errorQEL;
    public String errorQET;
    public String errorQEC;
    public String errorQEK;
    public String errorQAA;
    public String errorQAI;
    public String errorQAC;
    public String errorQAK;
    public String errorQCD;
    public String errorQCA;
    public String errorQCM;
    public String errorQCC;
    public String errorQCK;
    public String errorQNC;
    public String errorQNK;
    public String errorIAI;
    public String errorIPI;
    public String errorIQI;
    public String errorIKI;
    public String errorIQ2;
    public String errorIK2;
    public String errorIQ3;
    public String errorIK3;
    public String errorICM;
    public String errorIQ1;
    public String errorIK1;
    public String errorICC;
    public String errorCAC;
    public String errorCPC;
    public String errorCQC;
    public String errorCKC;
    public String errorYAI;
    public String errorYPI;
    public String errorYQI;
    public String errorYKI;
    public String errorQ2C;
    public String errorQ2K;
    public String errorQ3M;
    public String errorQ3R;
    public String errorQ3C;
    public String errorQ3K;
    public String errorQ1H;
    public String errorQ1C;
    public String errorQ1K;
    public String errorCAT;
    public String errorCAB;
    public String errorCAQ;
    public String errorCAS;
    public String errorCAV;
    public String errorCVS;
    public String errorLCO;
    public String errorLCE;
    public String errorLAE;
    public String errorLVU;
    public String errorLTH;
    public String errorLVR;
    public String errorLAU;
    public String errorLAS;
    public String errorLAA;
    public String errorLAL;
    public String errorFTE;
    public String errorFCO;
    public String errorFCA;
    public String errorFAS;
    public String errorFTR;
    public String errorFED;
    public String errorFUV;
    public String errorFSA;
    public String errorFSL;
    public String errorLAP;
    public String errorLAI;
    public String errorLAB;
    public String errorFAP;
    public String errorFAI;
    public String errorFAB;
    //----------------------------------------------------------- REJILLAS
    public String errorRPL;
//public String errorRTR; 
//public String errorREB;
    public String errorREL;
    public String errorRIB;
//public String errorRBI; 
    public String errorRPM;
    public String errorRPN;
    public String errorRAU;
    public String errorRER;
    public String errorRAT;
    public String errorRAR;
    public String errorRCP;
    public String errorRCM;
    public String errorRLC;
    public String errorRVM;
    public String errorRVN;
    public String errorRNB;
    public String errorRNE;
    public String errorRPC;
    public String errorRPS;
    public String errorRPB;
    public String errorRPH;
    //----------------------------------------------------------- DESARENADOR
    public int minFCH;
    public int maxFCH;
    public double minTML;
    public double maxTML;
    public int minPCI;
    public int maxPCI;
    public int minPUC;
    public int maxPUC;
    public int minTCI;
    public int maxTCI;
    public int minTUC;
    public int maxTUC;
    public int minPDI;
    public int maxPDI;
    public double minCRE;
    public double maxCRE;
    public int minDMP;
    public int maxDMP;
    public double minTCA;
    public double maxTCA;
    public double minTCG;
    public double maxTCG;
    public int minPPA;
    public int maxPPA;
    public int minPPG;
    public int maxPPG;
    public double maxCAR;
    public double minCAR;
    public double maxKAR;
    public double minKAR;
    public double maxQEL;
    public double minQEL;
    public double maxQET;
    public double minQET;
    public double maxQEC;
    public double minQEC;
    public double maxQEK;
    public double minQEK;
    public double maxQAA;
    public double minQAA;
    public double maxQAI;
    public double minQAI;
    public double maxQAC;
    public double minQAC;
    public double maxQAK;
    public double minQAK;
    public double maxQCA;
    public double minQCA;
    public double maxQCM;
    public double minQCM;
    public double maxQCC;
    public double minQCC;
    public double maxQCK;
    public double minQCK;
    public double maxQNC;
    public double minQNC;
    public double maxQNK;
    public double minQNK;
    public int maxIAI;
    public int minIAI;
    public double maxIPI;
    public double minIPI;
    public double maxIQI;
    public double minIQI;
    public double maxIKI;
    public double minIKI;
    public double maxIQ2;
    public double minIQ2;
    public double maxIK2;
    public double minIK2;
    public double maxIQ3;
    public double minIQ3;
    public double maxIK3;
    public double minIK3;
    public double maxICM;
    public double minICM;
    public double maxIQ1;
    public double minIQ1;
    public double maxIK1;
    public double minIK1;
    public double maxICC;
    public double minICC;
    public double maxCAC;
    public double minCAC;
    public double maxCPC;
    public double minCPC;
    public double maxCQC;
    public double minCQC;
    public double maxCKC;
    public double minCKC;
    public double maxYAI;
    public double minYAI;
    public double maxYPI;
    public double minYPI;
    public double maxYQI;
    public double minYQI;
    public double maxYKI;
    public double minYKI;
    public double maxQ2C;
    public double minQ2C;
    public double maxQ2K;
    public double minQ2K;
    public double maxQ3M;
    public double minQ3M;
    public double maxQ3C;
    public double minQ3C;
    public double maxQ3K;
    public double minQ3K;
    public double maxQ1H;
    public double minQ1H;
    public double maxQ1C;
    public double minQ1C;
    public double maxQ1K;
    public double minQ1K;
    public double maxCAT;
    public double minCAT;
    public double maxCAB;
    public double minCAB;
    public double maxCAQ;
    public double minCAQ;
    public double maxCAS;
    public double minCAS;
    public double maxCAV;
    public double minCAV;
    public double maxCVS;
    public double minCVS;
    public double minLCO;
    public double maxLCO;
    public double minLCE;
    public double maxLCE;
    public double minLAE;
    public double maxLAE;
    public double minLVU;
    public double maxLVU;
    public double minLTH;
    public double maxLTH;
    public double minLVR;
    public double maxLVR;
    public double minLAU;
    public double maxLAU;
    public double minLAS;
    public double maxLAS;
    public double minLAA;
    public double maxLAA;
    public double minLAL;
    public double maxLAL;
    public double minFTE;
    public double maxFTE;
    public double minFCO;
    public double maxFCO;
    public double minFCA;
    public double maxFCA;
    public double minFAS;
    public double maxFAS;
    public double minFTR;
    public double maxFTR;
    public double minFED;
    public double maxFED;
    public double minFUV;
    public double maxFUV;
    public double minFSA;
    public double maxFSA;
    public double minFSL;
    public double maxFSL;
    public int minLAP;
    public int maxLAP;
    public double minLAI;
    public double maxLAI;
    public double minLAB;
    public double maxLAB;
    public int minFAP;
    public int maxFAP;
    public double minFAI;
    public double maxFAI;
    public double minFAB;
    public double maxFAB;
    //----------------------------------------------------------REJILLAS 
    public double minRPL;
    public double maxRPL;
    public int minREL;
    public int maxREL;
    public int minRIB;
    public int maxRIB;
    public double minRPM;
    public double maxRPM;
    public double minRPN;
    public double maxRPN;
    public double minRAU;
    public double maxRAU;
    public double minRER;
    public double maxRER;
    public double minRAT;
    public double maxRAT;
    public double minRAR;
    public double maxRAR;
    public double minRCP;
    public double maxRCP;
    public double minRCM;
    public double maxRCM;
    public double minRLC;
    public double maxRLC;
    public double minRVM;
    public double maxRVM;
    public double minRVN;
    public double maxRVN;
    public int minRNB;
    public int maxRNB;
    public double minRNE;
    public double maxRNE;
    public double minRPC;
    public double maxRPC;
    public double minRPS;
    public double maxRPS;
    public double minRPB;
    public double maxRPB;
    public double minRPH;
    public double maxRPH;
    //-----------------------------------------------------------
    public boolean EditDatosDeEntrada = false; //Bandera True si La seccion de datos de entrada ha sido llenada con éxito. Igualmente si se carga desde bd 
    public boolean EditProyeccionPoblacional = false;
    public boolean EditCalculoCaudalesDiseno = false;
    public boolean EditCaractInicAguaResidual = false;
    public boolean EditRejillas = false;
    public boolean EditDesarenador = false;
    public boolean EditLagunaAnaerobia = false;
    public boolean EditLagunaFacultativaSec = false;
    public boolean EditReactorUasb = false;
    public boolean EditFiltroPercolador = false;
    public boolean EditLodosActivados = false;
    //public boolean EditResultado = false;
    //----------------------- = false;----------------------------- //Bandera que evita la duplicación de la ventana
    public boolean WinDatosDeEntrada = false;
    public boolean WinProyeccionPoblacional = false;
    public boolean WinCalculoCaudalesDiseno = false;
    public boolean WinCaractInicAguaResidual = false;
    public boolean WinRejillas = false;
    public boolean WinDesarenador = false;
    public boolean WinLagunaAnaerobia = false;
    public boolean WinLagunaFacultativaSec = false;
    public boolean WinReactorUasb = false;
    public boolean WinFiltroPercolador = false;
    public boolean WinLodosActivados = false;
    public boolean WinResultado = false;
    //-----------------------------------------------------------
    public int idproyecto = 0; //Contiene un nùmero si existe un proyecto cargado
    //-----------------------------------------------------------

    public Bod(String path) {
        Generalpath = path;
    }

    /**
     * Mètodo que comprueba si la variable 'var' es convertible a int. Si es así
     * se procede a asignarla a su 'nombre' semejante en ésta clase
     *
     * @param var Cadena con el valor int
     * @param nombre Nombre de la variable en ésta clase
     * @param min Valor mínimo permitido
     * @param max Valor máximo permitido
     * @return True: si la conversion y asignación fueron posibles
     */
    public boolean setVarInt(String var, String nombre, int min, int max) {

        try {
            if (valida.EsEnteroEntre(var, min, max)) {
                field = getClass().getDeclaredField(nombre);
                field.setInt(this, Integer.parseInt(var.trim()));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error setIntVar. " + ex.getMessage() + " " + ex.getCause());
        }
        return false;
    }

    /**
     * Método que comprueba si la variable 'var' entera está entre rangos. Si es
     * así se procede a asignarla a su 'nombre' semejante en ésta clase
     *
     * @param var Cadena con el valor int
     * @param nombre Nombre de la variable en ésta clase
     * @param min Valor mínimo permitido
     * @param max Valor máximo permitido
     * @return True: si la conversion y asignación fueron posibles
     */
    public boolean setVarInt(int var, String nombre, int min, int max) {

        try {
            if (valida.EsEnteroEntre(var, min, max)) {
                field = getClass().getDeclaredField(nombre);
                field.setInt(this, var);
                return true;
            }
        } catch (Exception ex) {
            log.error("Error setIntVar. " + ex.getMessage() + " " + ex.getCause());
        }
        return false;
    }

    /**
     * Método que devuelve el valor entero de la variable si existe el nombre
     * recibido 'nombre'
     *
     * @param nombre Cadena con el nombre de la variable en ésta clase
     * @return entero: si existe tal nombre
     */
    public int getVarInt(String nombre) {

        try {
            field = getClass().getDeclaredField(nombre);
            return field.getInt(this);
        } catch (Exception ex) {
            log.error("Error getIntVar. " + ex.getMessage() + " " + ex.getCause());
        }
        return 0;
    }

    /**
     * Método que comprueba si la variable 'var' es convertible a double. Si es
     * así se procede a asignarla a su 'nombre' semejante en ésta clase
     *
     * @param var Cadena con el valor
     * @param nombre Nombre de la variable en ésta clase
     * @param min Valor mínimo permitido
     * @param max Valor máximo permitido
     * @return True: si la conversion y asignación fueron posibles
     */
    public boolean setVarDob(String var, String nombre, double min, double max) {

        try {
            if (valida.EsDobleEntre(var, min, max)) {
                field = getClass().getDeclaredField(nombre);
                field.setDouble(this, Double.parseDouble(var.trim()));
                return true;
            }
        } catch (Exception ex) {
            log.error("Error setDubVar. " + nombre + " " + ex.getMessage() + " " + ex.getCause());
        }
        return false;
    }

    /**
     * Método que comprueba si la variable 'var' está entre rangos. Si es así se
     * procede a asignarla a su 'nombre' semejante en ésta clase
     *
     * @param var double con el valor
     * @param nombre Nombre de la variable en ésta clase
     * @param min Valor mínimo permitido
     * @param max Valor máximo permitido
     * @return True: si la conversion y asignación fueron posibles
     */
    public boolean setVarDob(double var, String nombre, double min, double max) {

        try {
            if (valida.EsDobleEntre(var, min, max)) {
                field = getClass().getDeclaredField(nombre);
                field.setDouble(this, var);
                return true;
            }
        } catch (Exception ex) {
            log.error("Error setDubVar. " + ex.getMessage() + " " + ex.getCause());
        }
        return false;
    }

    /**
     * Método que devuelve el valor doble de la variable si existe el nombre
     * recibido 'nombre'
     *
     * @param nombre Cadena con el nombre de la variable en ésta clase
     * @return doble: si existe tal nombre
     */
    public double getVarDob(String nombre) {

        try {
            field = getClass().getDeclaredField(nombre);
            return field.getDouble(this);
        } catch (Exception ex) {
            log.error("Error getDobVar. " + ex.getMessage() + " " + ex.getCause());
        }
        return 0;
    }

    /**
     * Método que devuelve en cadena formateada #.## el valor doble de la
     * variable si existe el nombre recibido 'nombre' y la cantidad de decimales
     * 'decimales'
     *
     * @param nombre Cadena con el nombre de la variable en ésta clase
     * @return doble: si existe tal nombre
     */
    public String getVarFormateadaPorNombre(String nombre, int decimales) {
        String sharp = "#.#";
        --decimales;
        while (decimales > 0) {
            sharp += "#";
            decimales--;
        }
        return valida.DobleFormatoStringCeil(getVarDob(nombre), sharp);
    }

    /**
     * Método que devuelve en cadena formateada #.## el valor doble de la
     * variable
     *
     * @param num variable por formatear
     * @return variable formateada #.####
     */
    public String getVarFormateada(double num, int decimales) {
        String sharp = "#.#";
        --decimales;
        while (decimales > 0) {
            sharp += "#";
            decimales--;
        }
        return valida.DobleFormatoStringCeil(num, sharp);
    }
    //--------------------------------------------------------

    public boolean setFCH(String sFCH, int min, int max) {

        if (valida.EsEnteroEntre(sFCH, min, max)) {
            this.FCH = Integer.parseInt(sFCH.trim());
            return true;
        }
        return false;
    }

    public boolean setTML(String sTML, double min, double max) {

        if (valida.EsDobleEntre(sTML, min, max)) {

            this.TML = Double.parseDouble(sTML.trim()); //valor.trim( //Se realiza el parseo directamente ya que la primera funcion lo comprobó
            return true;
        }
        return false;
    }

    public boolean setPCI(String sPCI, int min, int max) {

        if (valida.EsEnteroEntre(sPCI, min, max)) {
            this.PCI = Integer.parseInt(sPCI.trim());
            return true;
        }
        return false;
    }

    public boolean setPUC(String sPUC, int min, int max) {

        if (valida.EsEnteroEntre(sPUC, min, max)) {
            this.PUC = Integer.parseInt(sPUC.trim());
            return true;
        }
        return false;
    }

    public boolean setTCI(String sTCI, int min, int max) {

        if (valida.EsEnteroEntre(sTCI, min, max)) {
            this.TCI = Integer.parseInt(sTCI.trim());
            return true;
        }
        return false;
    }

    public boolean setTUC(String sTUC, int min, int max) {

        if (valida.EsEnteroEntre(sTUC, min, max)) {
            this.TUC = Integer.parseInt(sTUC.trim());
            return true;
        }
        return false;
    }

    public boolean setPDI(String sPDI, int min, int max) {

        if (valida.EsEnteroEntre(sPDI, min, max)) {
            this.PDI = Integer.parseInt(sPDI.trim());
            return true;
        }
        return false;
    }

    public boolean setCRE(String sCRE, double min, double max) {

        if (valida.EsDobleEntre(sCRE, min, max)) {
            this.CRE = Double.parseDouble(sCRE.trim());
            return true;
        }
        return false;
    }

    public boolean setDMP(String sDMP, int min, int max) {

        if (valida.EsEnteroEntre(sDMP, min, max)) {
            this.DMP = Integer.parseInt(sDMP.trim());
            return true;
        }
        return false;
    }

    public boolean setProyecto(String idProyecto, int min, int max) {

        if (valida.EsEnteroEntre(idProyecto, min, max)) {
            this.idproyecto = Integer.parseInt(idProyecto.trim());
            return true;
        }
        return false;
    }

    public void setMEA(int MEA) {
        this.MEA = MEA;
    }

    public void setMEG(int MEG) {
        this.MEG = MEG;
    }

    public boolean setTCA(String sTCA, double min, double max) {

        if (valida.EsDobleEntre(sTCA, minTCA, maxTCA)) {
            //String valor = valida.DobleFormatString(Double.parseDouble(sTCA), "#.##");
            // if (valor != null && !valor.isEmpty()) {
            this.TCA = Double.parseDouble(sTCA.trim());//valor.trim());
            return true;
        }
        return false;
    }

    public boolean setTCG(String sTCG, double min, double max) {

        if (valida.EsDobleEntre(sTCG, min, max)) {
            //String valor = valida.DobleFormatString(Double.parseDouble(sTCG), "#.######");
            //if (valor != null && !valor.isEmpty()) {
            this.TCG = Double.parseDouble(sTCG.trim()); //Se realiza el parseo directamente ya que la primera funcion lo comprobó
            return true;
        }
        return false;
    }

    public boolean setPPA(String sPPA, int min, int max) {

        if (valida.EsEnteroEntre(sPPA, minPPA, maxPPA)) {

            this.PPA = Integer.parseInt(sPPA);
            return true;
        }
        return false;
    }

    public boolean setPPG(String sPPG, int min, int max) {

        if (valida.EsEnteroEntre(sPPG, minPPG, maxPPG)) {

            this.PPG = Integer.parseInt(sPPG);
            return true;
        }
        return false;
    }

    public boolean setNCR(String sNCR) {

        if (sNCR != null) {
            this.NCR = sNCR;
            return true;
        }
        return false;
    }

    public boolean setCAR(String sCAR, double min, double max) {

        if (valida.EsDobleEntre(sCAR, min, max)) {
            this.CAR = Double.parseDouble(sCAR.trim());
            return true;
        }
        return false;
    }

    public boolean setKAR(String sKAR, double min, double max) {

        if (valida.EsDobleEntre(sKAR, min, max)) {
            this.KAR = Double.parseDouble(sKAR.trim());
            return true;
        }
        return false;
    }

    public void setQIF(int QIF) {
        this.QIF = QIF;
    }

    public void setQIA(int QIA) {
        this.QIA = QIA;
    }

    public boolean setQEL(String sQEL, double min, double max) {

        if (valida.EsDobleEntre(sQEL, min, max)) {
            this.QEL = Double.parseDouble(sQEL.trim());
            return true;
        }
        return false;
    }

    public boolean setQET(String sQET, double min, double max) {
        if (valida.EsDobleEntre(sQET, min, max)) {
            this.QET = Double.parseDouble(sQET.trim());
            return true;
        }
        return false;
    }

    public boolean setQEC(String sQEC, double min, double max) {
        if (valida.EsDobleEntre(sQEC, min, max)) {
            this.QEC = Double.parseDouble(sQEC.trim());
            return true;
        }
        return false;
    }

    public boolean setQEK(String sQEK, double min, double max) {
        if (valida.EsDobleEntre(sQEK, min, max)) {
            this.QEK = Double.parseDouble(sQEK.trim());
            return true;
        }
        return false;
    }

    public boolean setQAA(String sQAA, double min, double max) {
        if (valida.EsDobleEntre(sQAA, min, max)) {
            this.QAA = Double.parseDouble(sQAA.trim());
            return true;
        }
        return false;
    }

    public boolean setQAI(String sQAI, double min, double max) {
        if (valida.EsDobleEntre(sQAI, min, max)) {
            this.QAI = Double.parseDouble(sQAI.trim());
            return true;
        }
        return false;
    }

    public boolean setQAC(String sQAC, double min, double max) {
        if (valida.EsDobleEntre(sQAC, min, max)) {
            this.QAC = Double.parseDouble(sQAC.trim());
            return true;
        }
        return false;
    }

    public boolean setQAK(String sQAK, double min, double max) {
        if (valida.EsDobleEntre(sQAK, min, max)) {
            this.QAK = Double.parseDouble(sQAK.trim());
            return true;
        }
        return false;
    }

    public void setQCE(int QCE) {
        this.QCE = QCE;
    }

    public void setQCD(int QCD) {
        this.QCD = QCD;
    }

    public boolean setQCA(String sQCA, double min, double max) {
        if (valida.EsDobleEntre(sQCA, min, max)) {
            this.QCA = Double.parseDouble(sQCA.trim());
            return true;
        }
        return false;
    }

    public boolean setQCM(String sQCM, double min, double max) {
        if (valida.EsDobleEntre(sQCM, min, max)) {
            this.QCM = Double.parseDouble(sQCM.trim());
            return true;
        }
        return false;
    }

    public boolean setQCC(String sQCC, double min, double max) {
        if (valida.EsDobleEntre(sQCC, min, max)) {
            this.QCC = Double.parseDouble(sQCC.trim());
            return true;
        }
        return false;
    }

    public boolean setQCK(String sQCK, double min, double max) {
        if (valida.EsDobleEntre(sQCK, min, max)) {
            this.QCK = Double.parseDouble(sQCK.trim());
            return true;
        }
        return false;
    }

    public void setQNL(int QNL) {
        this.QNL = QNL;
    }

    public boolean setQNC(String sQNC, double min, double max) {
        if (valida.EsDobleEntre(sQNC, min, max)) {
            this.QNC = Double.parseDouble(sQNC.trim());
            return true;
        }
        return false;
    }

    public boolean setQNK(String sQNK, double min, double max) {
        if (valida.EsDobleEntre(sQNK, min, max)) {
            this.QNK = Double.parseDouble(sQNK.trim());
            return true;
        }
        return false;
    }

    public void setIOA(int IOA) {
        this.IOA = IOA;
    }

    public void setIRM(int IRM) {
        this.IRM = IRM;
    }

    public boolean setIAI(String sIAI, int min, int max) {

        if (valida.EsEnteroEntre(sIAI, min, max)) {
            this.IAI = Integer.parseInt(sIAI.trim());
            return true;
        }
        return false;
    }

    public boolean setIPI(String sIPI, double min, double max) {

        if (valida.EsDobleEntre(sIPI, min, max)) {
            this.IPI = Double.parseDouble(sIPI.trim());
            return true;
        }
        return false;
    }

    public boolean setIQI(String sIQI, double min, double max) {

        if (valida.EsDobleEntre(sIQI, min, max)) {
            this.IQI = Double.parseDouble(sIQI.trim());
            return true;
        }
        return false;
    }

    public boolean setIKI(String sIKI, double min, double max) {

        if (valida.EsDobleEntre(sIKI, min, max)) {
            this.IKI = Double.parseDouble(sIKI.trim());
            return true;
        }
        return false;
    }

    public boolean setIQ2(String sIQ2, double min, double max) {

        if (valida.EsDobleEntre(sIQ2, min, max)) {
            this.IQ2 = Double.parseDouble(sIQ2.trim());
            return true;
        }
        return false;
    }

    public boolean setIK2(String sIK2, double min, double max) {

        if (valida.EsDobleEntre(sIK2, min, max)) {
            this.IK2 = Double.parseDouble(sIK2.trim());
            return true;
        }
        return false;
    }

    public void setICO(int ICO) {
        this.ICO = ICO;
    }

    public boolean setIQ3(String sIQ3, double min, double max) {

        if (valida.EsDobleEntre(sIQ3, min, max)) {
            this.IQ3 = Double.parseDouble(sIQ3.trim());
            return true;
        }
        return false;
    }

    public boolean setIK3(String sIK3, double min, double max) {

        if (valida.EsDobleEntre(sIK3, min, max)) {
            this.IK3 = Double.parseDouble(sIK3.trim());
            return true;
        }
        return false;
    }

    public boolean setICM(String sICM, double min, double max) {

        if (valida.EsDobleEntre(sICM, min, max)) {
            this.ICM = Double.parseDouble(sICM.trim());
            return true;
        }
        return false;
    }

    public boolean setIQ1(String sIQ1, double min, double max) {

        if (valida.EsDobleEntre(sIQ1, min, max)) {
            this.IQ1 = Double.parseDouble(sIQ1.trim());
            return true;
        }
        return false;
    }

    public boolean setIK1(String sIK1, double min, double max) {

        if (valida.EsDobleEntre(sIK1, min, max)) {
            this.IK1 = Double.parseDouble(sIK1.trim());
            return true;
        }
        return false;
    }

    public boolean setICC(String sICC, double min, double max) {

        if (valida.EsDobleEntre(sICC, min, max)) {
            this.ICC = Double.parseDouble(sICC.trim());
            return true;
        }
        return false;
    }

    public void setCOA(int COA) {
        this.COA = COA;
    }

    public boolean setCAC(String sCAC, double min, double max) {

        if (valida.EsDobleEntre(sCAC, min, max)) {
            this.CAC = Double.parseDouble(sCAC.trim());
            return true;
        }
        return false;
    }

    public boolean setCPC(String sCPC, double min, double max) {

        if (valida.EsDobleEntre(sCPC, min, max)) {
            this.CPC = Double.parseDouble(sCPC.trim());
            return true;
        }
        return false;
    }

    public boolean setCQC(String sCQC, double min, double max) {

        if (valida.EsDobleEntre(sCQC, min, max)) {
            this.CQC = Double.parseDouble(sCQC.trim());
            return true;
        }
        return false;
    }

    public boolean setCKC(String sCKC, double min, double max) {

        if (valida.EsDobleEntre(sCKC, min, max)) {
            this.CKC = Double.parseDouble(sCKC.trim());
            return true;
        }
        return false;
    }

    public void setYOA(int YOA) {
        this.YOA = YOA;
    }

    public boolean setYAI(String sYAI, double min, double max) {

        if (valida.EsDobleEntre(sYAI, min, max)) {
            this.YAI = Double.parseDouble(sYAI.trim());
            return true;
        }
        return false;
    }

    public boolean setYPI(String sYPI, double min, double max) {

        if (valida.EsDobleEntre(sYPI, min, max)) {
            this.YPI = Double.parseDouble(sYPI.trim());
            return true;
        }
        return false;
    }

    public boolean setYQI(String sYQI, double min, double max) {

        if (valida.EsDobleEntre(sYQI, min, max)) {
            this.YQI = Double.parseDouble(sYQI.trim());
            return true;
        }
        return false;
    }

    public boolean setYKI(String sYKI, double min, double max) {

        if (valida.EsDobleEntre(sYKI, min, max)) {
            this.YKI = Double.parseDouble(sYKI.trim());
            return true;
        }
        return false;
    }

    public boolean setQ2C(String sQ2C, double min, double max) {

        if (valida.EsDobleEntre(sQ2C, min, max)) {
            this.Q2C = Double.parseDouble(sQ2C.trim());
            return true;
        }
        return false;
    }

    public boolean setQ2K(String sQ2K, double min, double max) {

        if (valida.EsDobleEntre(sQ2K, min, max)) {
            this.Q2K = Double.parseDouble(sQ2K.trim());
            return true;
        }
        return false;
    }

    public boolean setQ3M(String sQ3M, double min, double max) {

        if (valida.EsDobleEntre(sQ3M, min, max)) {
            this.Q3M = Double.parseDouble(sQ3M.trim());
            return true;
        }
        return false;
    }

    public void setQ3R(int Q3R) {
        this.Q3R = Q3R;
    }

    public boolean setQ3C(String sQ3C, double min, double max) {

        if (valida.EsDobleEntre(sQ3C, min, max)) {
            this.Q3C = Double.parseDouble(sQ3C.trim());
            return true;
        }
        return false;
    }

    public boolean setQ3K(String sQ3K, double min, double max) {

        if (valida.EsDobleEntre(sQ3K, min, max)) {
            this.Q3K = Double.parseDouble(sQ3K.trim());
            return true;
        }
        return false;
    }

    public boolean setQ1H(String sQ1H, double min, double max) {

        if (valida.EsDobleEntre(sQ1H, min, max)) {
            this.Q1H = Double.parseDouble(sQ1H.trim());
            return true;
        }
        return false;
    }

    public boolean setQ1C(String sQ1C, double min, double max) {

        if (valida.EsDobleEntre(sQ1C, min, max)) {
            this.Q1C = Double.parseDouble(sQ1C.trim());
            return true;
        }
        return false;
    }

    public boolean setQ1K(String sQ1K, double min, double max) {

        if (valida.EsDobleEntre(sQ1K, min, max)) {
            this.Q1K = Double.parseDouble(sQ1K.trim());
            return true;
        }
        return false;
    }

    public boolean setCAT(String sCAT, double min, double max) {

        if (valida.EsDobleEntre(sCAT, min, max)) {
            this.CAT = Double.parseDouble(sCAT.trim());
            return true;
        }
        return false;
    }

    public boolean setCAB(String sCAB, double min, double max) {

        if (valida.EsDobleEntre(sCAB, min, max)) {
            this.CAB = Double.parseDouble(sCAB.trim());
            return true;
        }
        return false;
    }

    public boolean setCAQ(String sCAQ, double min, double max) {

        if (valida.EsDobleEntre(sCAQ, min, max)) {
            this.CAQ = Double.parseDouble(sCAQ.trim());
            return true;
        }
        return false;
    }

    public void setCAQ(double sCAQ) {//Caso especial para variables opcionales
        this.CAQ = sCAQ;
    }

    public boolean setCAS(String sCAS, double min, double max) {

        if (valida.EsDobleEntre(sCAS, min, max)) {
            this.CAS = Double.parseDouble(sCAS.trim());
            return true;
        }
        return false;
    }

    public void setCAS(double sCAS) { //Caso especial para variables opcionales 
        this.CAS = sCAS;
    }

    public boolean setCAV(String sCAV, double min, double max) {

        if (valida.EsDobleEntre(sCAV, min, max)) {
            this.CAV = Double.parseDouble(sCAV.trim());
            return true;
        }
        return false;
    }

    public void setCAV(double sCAV) { //CAVo especial para variables opcionales 
        this.CAV = sCAV;
    }

    public boolean setCVS(String sCVS, double min, double max) {

        if (valida.EsDobleEntre(sCVS, min, max)) {
            this.CVS = Double.parseDouble(sCVS.trim());
            return true;
        }
        return false;
    }

    public void setCVS(double sCVS) { //CVS  
        this.CVS = sCVS;
    }

    public boolean setLCO(String sLCO, double min, double max) {
        if (valida.EsDobleEntre(sLCO, min, max)) {
            this.LCO = Double.parseDouble(sLCO.trim());
            return true;
        }
        return false;
    }

    public boolean setLCE(String sLCE, double min, double max) {
        if (valida.EsDobleEntre(sLCE, min, max)) {
            this.LCE = Double.parseDouble(sLCE.trim());
            return true;
        }
        return false;
    }

    public boolean setLAE(String sLAE, double min, double max) {
        if (valida.EsDobleEntre(sLAE, min, max)) {
            this.LAE = Double.parseDouble(sLAE.trim());
            return true;
        }
        return false;
    }

    public boolean setLVU(String sLVU, double min, double max) {
        if (valida.EsDobleEntre(sLVU, min, max)) {
            this.LVU = Double.parseDouble(sLVU.trim());
            return true;
        }
        return false;
    }

    public boolean setLTH(String sLTH, double min, double max) {
        if (valida.EsDobleEntre(sLTH, min, max)) {
            this.LTH = Double.parseDouble(sLTH.trim());
            return true;
        }
        return false;
    }

    public boolean setLVR(String sLVR, double min, double max) {
        if (valida.EsDobleEntre(sLVR, min, max)) {
            this.LVR = Double.parseDouble(sLVR.trim());
            return true;
        }
        return false;
    }

    public boolean setLAU(String sLAU, double min, double max) {
        if (valida.EsDobleEntre(sLAU, min, max)) {
            this.LAU = Double.parseDouble(sLAU.trim());
            return true;
        }
        return false;
    }

    public boolean setLAS(String sLAS, double min, double max) {
        if (valida.EsDobleEntre(sLAS, min, max)) {
            this.LAS = Double.parseDouble(sLAS.trim());
            return true;
        }
        return false;
    }

    public void setLLA(int LLA) {
        this.LLA = LLA;
    }

    public boolean setLAA(String sLAA, double min, double max) {
        if (valida.EsDobleEntre(sLAA, min, max)) {
            this.LAA = Double.parseDouble(sLAA.trim());
            return true;
        }
        return false;
    }

    public boolean setLAL(String sLAL, double min, double max) {
        if (valida.EsDobleEntre(sLAL, min, max)) {
            this.LAL = Double.parseDouble(sLAL.trim());
            return true;
        }
        return false;
    }

    public boolean setLAP(String sLAP, int min, int max) {

        if (valida.EsEnteroEntre(sLAP, min, max)) {
            this.LAP = Integer.parseInt(sLAP.trim());
            return true;
        }
        return false;
    }

    public boolean setLAI(String sLAI, double min, double max) {
        if (valida.EsDobleEntre(sLAI, min, max)) {
            this.LAI = Double.parseDouble(sLAI.trim());
            return true;
        }
        return false;
    }

    public boolean setLAB(String sLAB, double min, double max) {
        if (valida.EsDobleEntre(sLAB, min, max)) {
            this.LAB = Double.parseDouble(sLAB.trim());
            return true;
        }
        return false;
    }

    public boolean setFAP(String sFAP, int min, int max) {

        if (valida.EsEnteroEntre(sFAP, min, max)) {
            this.FAP = Integer.parseInt(sFAP.trim());
            return true;
        }
        return false;
    }

    public boolean setFAI(String sFAI, double min, double max) {
        if (valida.EsDobleEntre(sFAI, min, max)) {
            this.FAI = Double.parseDouble(sFAI.trim());
            return true;
        }
        return false;
    }

    public boolean setFAB(String sFAB, double min, double max) {
        if (valida.EsDobleEntre(sFAB, min, max)) {
            this.FAB = Double.parseDouble(sFAB.trim());
            return true;
        }
        return false;
    }

    public boolean setFTE(String sFTE, double min, double max) {
        if (valida.EsDobleEntre(sFTE, min, max)) {
            this.FTE = Double.parseDouble(sFTE.trim());
            return true;
        }
        return false;
    }

    public boolean setFCO(String sFCO, double min, double max) {
        if (valida.EsDobleEntre(sFCO, min, max)) {
            this.FCO = Double.parseDouble(sFCO.trim());
            return true;
        }
        return false;
    }

    public boolean setFCA(String sFCA, double min, double max) {
        if (valida.EsDobleEntre(sFCA, min, max)) {
            this.FCA = Double.parseDouble(sFCA.trim());
            return true;
        }
        return false;
    }

    public boolean setFAS(String sFAS, double min, double max) {
        if (valida.EsDobleEntre(sFAS, min, max)) {
            this.FAS = Double.parseDouble(sFAS.trim());
            return true;
        }
        return false;
    }

    public boolean setFTR(String sFTR, double min, double max) {
        if (valida.EsDobleEntre(sFTR, min, max)) {
            this.FTR = Double.parseDouble(sFTR.trim());
            return true;
        }
        return false;
    }

    public boolean setFED(String sFED, double min, double max) {
        if (valida.EsDobleEntre(sFED, min, max)) {
            this.FED = Double.parseDouble(sFED.trim());
            return true;
        }
        return false;
    }

    public boolean setFUV(String sFUV, double min, double max) {
        if (valida.EsDobleEntre(sFUV, min, max)) {
            this.FUV = Double.parseDouble(sFUV.trim());
            return true;
        }
        return false;
    }

    public boolean setFSA(String sFSA, double min, double max) {
        if (valida.EsDobleEntre(sFSA, min, max)) {
            this.FSA = Double.parseDouble(sFSA.trim());
            return true;
        }
        return false;
    }

    public boolean setFSL(String sFSL, double min, double max) {
        if (valida.EsDobleEntre(sFSL, min, max)) {
            this.FSL = Double.parseDouble(sFSL.trim());
            return true;
        }
        return false;
    }

    public boolean setRPL(String sRPL, double min, double max) {
        if (valida.EsDobleEntre(sRPL, min, max)) {
            this.RPL = Double.parseDouble(sRPL.trim());
            return true;
        }
        return false;
    }

    public boolean setRTR(String sRTR, int min, int max) {
        if (valida.EsEnteroEntre(sRTR, min, max)) {
            this.RTR = Integer.parseInt(sRTR.trim());
            return true;
        }
        return false;
    }

    public boolean setREB(String sREB, int min, int max) {
        if (valida.EsDobleEntre(sREB, min, max)) {
            this.REB = Double.parseDouble(sREB.trim());
            return true;
        }
        return false;
    }

    public boolean setREL(String sREL, int min, int max) {
        if (valida.EsEnteroEntre(sREL, min, max)) {
            this.REL = Integer.parseInt(sREL.trim());
            return true;
        }
        return false;
    }

    public boolean setRIB(String sRIB, int min, int max) {
        if (valida.EsEnteroEntre(sRIB, min, max)) {
            this.RIB = Integer.parseInt(sRIB.trim());
            return true;
        }
        return false;
    }

    public void setRBI(int RBI) {
        this.RBI = RBI;
    }

    public boolean setRPM(String sRPM, double min, double max) {
        if (valida.EsDobleEntre(sRPM, min, max)) {
            this.RPM = Double.parseDouble(sRPM.trim());
            return true;
        }
        return false;
    }

    public boolean setRPN(String sRPN, double min, double max) {
        if (valida.EsDobleEntre(sRPN, min, max)) {
            this.RPN = Double.parseDouble(sRPN.trim());
            return true;
        }
        return false;
    }

    public boolean setRAU(String sRAU, double min, double max) {
        if (valida.EsDobleEntre(sRAU, min, max)) {
            this.RAU = Double.parseDouble(sRAU.trim());
            return true;
        }
        return false;
    }

    public boolean setRER(String sRER, double min, double max) {
        if (valida.EsDobleEntre(sRER, min, max)) {
            this.RER = Double.parseDouble(sRER.trim());
            return true;
        }
        return false;
    }

    public boolean setRAT(String sRAT, double min, double max) {
        if (valida.EsDobleEntre(sRAT, min, max)) {
            this.RAT = Double.parseDouble(sRAT.trim());
            return true;
        }
        return false;
    }

    public boolean setRAR(String sRAR, double min, double max) {
        if (valida.EsDobleEntre(sRAR, min, max)) {
            this.RAR = Double.parseDouble(sRAR.trim());
            return true;
        }
        return false;
    }

    public boolean setRCP(String sRCP, double min, double max) {
        if (valida.EsDobleEntre(sRCP, min, max)) {
            this.RCP = Double.parseDouble(sRCP.trim());
            return true;
        }
        return false;
    }

    public boolean setRCM(String sRCM, double min, double max) {
        if (valida.EsDobleEntre(sRCM, min, max)) {
            this.RCM = Double.parseDouble(sRCM.trim());
            return true;
        }
        return false;
    }

    public boolean setRLC(String sRLC, double min, double max) {
        if (valida.EsDobleEntre(sRLC, min, max)) {
            this.RLC = Double.parseDouble(sRLC.trim());
            return true;
        }
        return false;
    }

    public boolean setRVM(String sRVM, double min, double max) {
        if (valida.EsDobleEntre(sRVM, min, max)) {
            this.RVM = Double.parseDouble(sRVM.trim());
            return true;
        }
        return false;
    }

    public boolean setRVN(String sRVN, double min, double max) {
        if (valida.EsDobleEntre(sRVN, min, max)) {
            this.RVN = Double.parseDouble(sRVN.trim());
            return true;
        }
        return false;
    }

    public boolean setRNB(String sRNB, int min, int max) {
        if (valida.EsEnteroEntre(sRNB, min, max)) {
            this.RNB = Integer.parseInt(sRNB.trim());
            return true;
        }
        return false;
    }

    public boolean setRNE(String sRNE, double min, double max) {
        if (valida.EsDobleEntre(sRNE, min, max)) {
            this.RNE = Double.parseDouble(sRNE.trim());
            return true;
        }
        return false;
    }

    public boolean setRPC(String sRPC, double min, double max) {
        if (valida.EsDobleEntre(sRPC, min, max)) {
            this.RPC = Double.parseDouble(sRPC.trim());
            return true;
        }
        return false;
    }

    public boolean setRPS(String sRPS, double min, double max) {
        if (valida.EsDobleEntre(sRPS, min, max)) {
            this.RPS = Double.parseDouble(sRPS.trim());
            return true;
        }
        return false;
    }

    public boolean setRPB(String sRPB, double min, double max) {
        if (valida.EsDobleEntre(sRPB, min, max)) {
            this.RPB = Double.parseDouble(sRPB.trim());
            return true;
        }
        return false;
    }

    public boolean setRPH(String sRPH, double min, double max) {
        if (valida.EsDobleEntre(sRPH, min, max)) {
            this.RPH = Double.parseDouble(sRPH.trim());
            return true;
        }
        return false;
    }

    public boolean setDAN(String sDAN, double min, double max) {
        if (valida.EsDobleEntre(sDAN, min, max)) {
            this.DAN = Double.parseDouble(sDAN.trim());
            return true;
        }
        return false;
    }

    public boolean setDCK(String sDCK, double min, double max) {
        if (valida.EsDobleEntre(sDCK, min, max)) {
            this.DCK = Double.parseDouble(sDCK.trim());
            return true;
        }
        return false;
    }

    public boolean setDCN(String sDCN, double min, double max) {
        if (valida.EsDobleEntre(sDCN, min, max)) {
            this.DCN = Double.parseDouble(sDCN.trim());
            return true;
        }
        return false;
    }

    public boolean setDP1(String sDP1, double min, double max) {
        if (valida.EsDobleEntre(sDP1, min, max)) {
            this.DP1 = Double.parseDouble(sDP1.trim());
            return true;
        }
        return false;
    }

    public boolean setDP2(String sDP2, double min, double max) {
        if (valida.EsDobleEntre(sDP2, min, max)) {
            this.DP2 = Double.parseDouble(sDP2.trim());
            return true;
        }
        return false;
    }

    public boolean setDP3(String sDP3, double min, double max) {
        if (valida.EsDobleEntre(sDP3, min, max)) {
            this.DP3 = Double.parseDouble(sDP3.trim());
            return true;
        }
        return false;
    }

    public boolean setDRZ(String sDRZ, double min, double max) {
        if (valida.EsDobleEntre(sDRZ, min, max)) {
            this.DRZ = Double.parseDouble(sDRZ.trim());
            return true;
        }
        return false;
    }

    public void setDPA(double DPA) {
        this.DPA = DPA;
    }

    public void setDPB(double DPB) {
        this.DPB = DPB;
    }

    public void setDPC(double DPC) {
        this.DPC = DPC;
    }

    public void setDPD(double DPD) {
        this.DPD = DPD;
    }

    public void setDPE(double DPE) {
        this.DPE = DPE;
    }

    public void setDPF(double DPF) {
        this.DPF = DPF;
    }

    public void setDPG(double DPG) {
        this.DPG = DPG;
    }

    public void setDPK(double DPK) {
        this.DPK = DPK;
    }

    public void setDPN(double DPN) {
        this.DPN = DPN;
    }

    public boolean setDAH(String sDAH, double min, double max) {
        if (valida.EsDobleEntre(sDAH, min, max)) {
            this.DAH = Double.parseDouble(sDAH.trim());
            return true;
        }
        return false;
    }

    public boolean setDAB(String sDAB, double min, double max) {
        if (valida.EsDobleEntre(sDAB, min, max)) {
            this.DAB = Double.parseDouble(sDAB.trim());
            return true;
        }
        return false;
    }

    public boolean setDVF(String sDVF, double min, double max) {
        if (valida.EsDobleEntre(sDVF, min, max)) {
            this.DVF = Double.parseDouble(sDVF.trim());
            return true;
        }
        return false;
    }

    public boolean setDLD(String sDLD, double min, double max) {
        if (valida.EsDobleEntre(sDLD, min, max)) {
            this.DLD = Double.parseDouble(sDLD.trim());
            return true;
        }
        return false;
    }

    public boolean setDAL(String sDAL, double min, double max) {
        if (valida.EsDobleEntre(sDAL, min, max)) {
            this.DAL = Double.parseDouble(sDAL.trim());
            return true;
        }
        return false;
    }

    public boolean setDEM(String sDEM, double min, double max) {
        if (valida.EsDobleEntre(sDEM, min, max)) {
            this.DEM = Double.parseDouble(sDEM.trim());
            return true;
        }
        return false;
    }

    public boolean setDFL(String sDFL, int min, int max) {
        if (valida.EsEnteroEntre(sDFL, min, max)) {
            this.DFL = Integer.parseInt(sDFL.trim());
            return true;
        }
        return false;
    }

    public boolean setDUP(String sDUP, double min, double max) {
        if (valida.EsDobleEntre(sDUP, min, max)) {
            this.DUP = Double.parseDouble(sDUP.trim());
            return true;
        }
        return false;
    }
    //--------------------------------------------------------------------------------------- GETTERS

    public boolean compruebaTucTci() { //Tuc debe ser mayor que Tci
        if (TUC > TCI && TUC < FCH) {
            return true;
        }
        return false;
    }

    public int getFCH() {
        return this.FCH;
    }

    public String getTML() {
        return valida.DobleFormatoStringCeil(TML, "#.#");
    }

    public String getPCI() {
        return PCI + "";
    }

    public String getPUC() {
        return PUC + "";
    }

    public int getTCI() {
        return this.TCI;
    }

    public int getTUC() {
        return this.TUC;
    }

    public String getPDI() {
        return PDI + "";
    }

    public String getCRE() {
        return valida.DobleFormatoStringCeil(CRE, "#.##");
    }

    public String getDMP() {
        return DMP + "";
    }

    public int getMEG() {
        return MEG;
    }

    public int getMEA() {
        return MEA;
    }

    public String getTCA() { //Por regla se devuelven 3 decimales (ver anexos), aunque el numero original puede tener cualquier cantidad 
        return valida.DobleFormatoStringCeil(TCA, "#.###");
    }

    public String getTCG() {
        return valida.DobleFormatoStringCeil(TCG, "#.###");
    }

    public int getPPA() {
        return PPA;
    }

    public int getPPG() {
        return PPG;
    }

    public String getNCR() {
        return NCR;
    }

    public String getCAR() {
        return valida.DobleFormatoStringCeil(CAR, "#.##");
    }

    public String getKAR() {
        return valida.DobleFormatoStringCeil(KAR, "#.##");
    }

    public int getQIF() {
        return QIF;
    }

    public int getQIA() {
        return QIA;
    }

    public String getQEL() {
        return valida.DobleFormatoStringCeil(QEL, "#.##");
    }

    public String getQET() {
        return valida.DobleFormatoStringCeil(QET, "#.##");
    }

    public String getQEC() {
        return valida.DobleFormatoStringCeil(QEC, "#.##");
    }

    public String getQEK() {
        return valida.DobleFormatoStringCeil(QEK, "#.##");
    }

    public String getQAA() {
        return valida.DobleFormatoStringCeil(QAA, "#.##");
    }

    public String getQAI() {
        return valida.DobleFormatoStringCeil(QAI, "#.##");
    }

    public String getQAC() {
        return valida.DobleFormatoStringCeil(QAC, "#.##");
    }

    public String getQAK() {
        return valida.DobleFormatoStringCeil(QAK, "#.##");
    }

    public int getQCE() {
        return QCE;
    }

    public int getQCD() {
        return QCD;
    }

    public String getQCA() {
        return valida.DobleFormatoStringCeil(QCA, "#.##");
    }

    public String getQCM() {
        return valida.DobleFormatoStringCeil(QCM, "#.##");
    }

    public String getQCC() {
        return valida.DobleFormatoStringCeil(QCC, "#.##");
    }

    public String getQCK() {
        return valida.DobleFormatoStringCeil(QCK, "#.##");
    }

    public int getQNL() {
        return QNL;
    }

    public String getQNC() {
        return valida.DobleFormatoStringCeil(QNC, "#.##");
    }

    public String getQNK() {
        return valida.DobleFormatoStringCeil(QNK, "#.##");
    }

    public int getIOA() {
        return IOA;
    }

    public int getIRM() {
        return IRM;
    }

    public String getIAI() {
        return IAI + "";
    }

    public String getIPI() {
        return valida.DobleFormatoStringCeil(IPI, "#.##");
    }

    public String getIQI() {
        return valida.DobleFormatoStringCeil(IQI, "#.##");
    }

    public String getIKI() {
        return valida.DobleFormatoStringCeil(IKI, "#.##");
    }

    public String getIQ2() {
        return valida.DobleFormatoStringCeil(IQ2, "#.##");
    }

    public String getIK2() {
        return valida.DobleFormatoStringCeil(IK2, "#.##");
    }

    public int getICO() {
        return ICO;
    }

    public String getIQ3() {
        return valida.DobleFormatoStringCeil(IQ3, "#.##");
    }

    public String getIK3() {
        return valida.DobleFormatoStringCeil(IK3, "#.##");
    }

    public String getICM() {
        return valida.DobleFormatoStringCeil(ICM, "#.##");
    }

    public String getIQ1() {
        return valida.DobleFormatoStringCeil(IQ1, "#.##");
    }

    public String getIK1() {
        return valida.DobleFormatoStringCeil(IK1, "#.##");
    }

    public String getICC() {
        return valida.DobleFormatoStringCeil(ICC, "#.##");
    }

    public int getCOA() {
        return COA;
    }

    public String getCAC() {
        return valida.DobleFormatoStringCeil(CAC, "#.##");
    }

    public String getCPC() {
        return valida.DobleFormatoStringCeil(CPC, "#.##");
    }

    public String getCQC() {
        return valida.DobleFormatoStringCeil(CQC, "#.##");
    }

    public String getCKC() {
        return valida.DobleFormatoStringCeil(CKC, "#.##");
    }

    public int getYOA() {
        return YOA;
    }

    public String getYAI() {
        return valida.DobleFormatoStringCeil(YAI, "#.##");
    }

    public String getYPI() {
        return valida.DobleFormatoStringCeil(YPI, "#.##");
    }

    public String getYQI() {
        return valida.DobleFormatoStringCeil(YQI, "#.##");
    }

    public String getYKI() {
        return valida.DobleFormatoStringCeil(YKI, "#.##");
    }

    public String getQ2C() {
        return valida.DobleFormatoStringCeil(Q2C, "#.##");
    }

    public String getQ2K() {
        return valida.DobleFormatoStringCeil(Q2K, "#.##");
    }

    public String getQ3M() {
        return valida.DobleFormatoStringCeil(Q3M, "#.##");
    }

    public int getQ3R() {
        return Q3R;
    }

    public String getQ3Cs() {
        return valida.DobleFormatoStringCeil(Q3C, "#.##");
    }

    public double getQ3C() {
        return Q3C;
    }//-------

    public String getQ3K() {
        return valida.DobleFormatoStringCeil(Q3K, "#.##");
    }

    public String getQ1H() {
        return valida.DobleFormatoStringCeil(Q1H, "#.##");
    }

    public String getQ1C() {
        return valida.DobleFormatoStringCeil(Q1C, "#.##");
    }

    public String getQ1K() {
        return valida.DobleFormatoStringCeil(Q1K, "#.##");
    }

    public String getCAT() {
        return valida.DobleFormatoStringCeil(CAT, "#.#");
    }

    public String getCAB() {
        return valida.DobleFormatoStringCeil(CAB, "#.##");
    }

    public String getCAQ() {
        return valida.DobleFormatoStringCeil(CAQ, "#.##");
    }

    public String getCAS() {
        return valida.DobleFormatoStringCeil(CAS, "#.##");
    }

    public String getCAV() {
        return valida.DobleFormatoStringCeil(CAV, "#.##");
    }

    public String getCVS() {
        return valida.DobleFormatoStringCeil(CVS, "#.##");
    }

    public String getLCO() {
        return valida.DobleFormatoStringCeil(LCO, "#.#");
    }

    public String getLCE() {
        return valida.DobleFormatoStringCeil(LCE, "#.#");
    }

    public String getLAE() {
        return valida.DobleFormatoStringCeil(LAE, "#.#");
    }

    public String getLVU() {
        return valida.DobleFormatoStringCeil(LVU, "#.#");
    }

    public String getLTH() {
        return valida.DobleFormatoStringCeil(LTH, "#.#");
    }

    public String getLVR() {
        return valida.DobleFormatoStringCeil(LVR, "#.#");
    }

    public String getLAUs() {
        return valida.DobleFormatoStringCeil(LAU, "#.#");
    }

    public double getLAU() {
        return LAU;
    }

    public String getLAS() {
        return valida.DobleFormatoStringCeil(LAS, "#.#");
    }

    public int getLLA() {
        return LLA;
    }

    public String getLAAs() {
        return valida.DobleFormatoStringCeil(LAA, "#.#");
    }

    public double getLAA() {
        return LAA;
    }

    public String getLALs() {
        return valida.DobleFormatoStringCeil(LAL, "#.#");
    }

    public double getLAL() {
        return LAL;
    }

    public int getLAP() {
        return LAP;
    }

    public String getLAIs() {
        return valida.DobleFormatoStringCeil(LAI, "#.#");
    }

    public double getLAI() {
        return LAI;
    }

    public String getLABs() {
        return valida.DobleFormatoStringCeil(LAB, "#.#");
    }

    public double getLAB() {
        return LAB;
    }

    public int getFAP() {
        return FAP;
    }

    public String getFAIs() {
        return valida.DobleFormatoStringCeil(FAI, "#.#");
    }

    public double getFAI() {
        return FAI;
    }

    public String getFABs() {
        return valida.DobleFormatoStringCeil(FAB, "#.#");
    }

    public double getFAB() {
        return FAB;
    }

    public String getFTE() {
        return valida.DobleFormatoStringCeil(FTE, "#.#");
    }

    public String getFCO() {
        return valida.DobleFormatoStringCeil(FCO, "#.#");
    }

    public String getFCAs() {
        return valida.DobleFormatoStringCeil(FCA, "#.#");
    }

    public double getFCA() {
        return FCA;
    }

    public String getFAS() {
        return valida.DobleFormatoStringCeil(FAS, "#.#");
    }

    public String getFTR() {
        return valida.DobleFormatoStringCeil(FTR, "#.#");
    }

    public String getFED() {
        return valida.DobleFormatoStringCeil(FED, "#.#");
    }

    public String getFUV() {
        return valida.DobleFormatoStringCeil(FUV, "#.#");
    }

    public String getFSAs() {
        return valida.DobleFormatoStringCeil(FSA, "#.#");
    }

    public double getFSA() {
        return FSA;
    }

    public String getFSLs() {
        return valida.DobleFormatoStringCeil(FSL, "#.#");
    }

    public double getFSL() {
        return FSL;
    }

    public String getRPLs() {
        return valida.DobleFormatoStringCeil(RPL, "#.##");
    }

    public double getRPL() {
        return RPL;
    }

    public int getRTR() {
        return RTR;
    }

    public String getREBs() {
        return valida.DobleFormatoStringCeil(REB, "#.##");
    }

    public double getREB() {
        return REB;
    }

    public int getREL() {
        return REL;
    }

    public int getRIB() {
        return RIB;
    }

    public int getRBI() {
        return RBI;
    }

    public String getRPMs() {
        return valida.DobleFormatoStringCeil(RPM, "#.#");
    }

    public double getRPM() {
        return RPM;
    }

    public String getRPNs() {
        return valida.DobleFormatoStringCeil(RPN, "#.#");
    }

    public double getRPN() {
        return RPN;
    }

    public String getRAUs() {
        return valida.DobleFormatoStringCeil(RAU, "#.#####");
    }

    public double getRAU() {
        return RAU;
    }

    public String getRERs() {
        return valida.DobleFormatoStringCeil(RER, "#.###");
    }

    public double getRER() {
        return RER;
    }

    public String getRATs() {
        return valida.DobleFormatoStringCeil(RAT, "#.#####");
    }

    public double getRAT() {
        return RAT;
    }

    public String getRARs() {
        return valida.DobleFormatoStringCeil(RAR, "#.####");
    }

    public double getRAR() {
        return RAR;
    }

    public String getRCPs() {
        return valida.DobleFormatoStringCeil(RCP, "#.##");
    }

    public double getRCP() {
        return RCP;
    }

    public String getRCMs() {
        return valida.DobleFormatoStringCeil(RCM, "#.##");
    }

    public double getRCM() {
        return RCM;
    }

    public String getRLCs() {
        return valida.DobleFormatoStringCeil(RLC, "#.##");
    }

    public double getRLC() {
        return RLC;
    }

    public String getRVMs() {
        return valida.DobleFormatoStringCeil(RVM, "#.##");
    }

    public double getRVM() {
        return RVM;
    }

    public String getRVNs() {
        return valida.DobleFormatoStringCeil(RVN, "#.##");
    }

    public double getRVN() {
        return RVN;
    }

    public int getRNB() {
        return RTR;
    }

    public String getRNEs() {
        return valida.DobleFormatoStringCeil(RNE, "#.#");
    }

    public double getRNE() {
        return RNE;
    }

    public String getRPCs() {
        return valida.DobleFormatoStringCeil(RPC, "#.###");
    }

    public double getRPC() {
        return RPC;
    }

    public String getRPSs() {
        return valida.DobleFormatoStringCeil(RPS, "#.###");
    }

    public double getRPS() {
        return RPS;
    }

    public String getRPBs() {
        return valida.DobleFormatoStringCeil(RPB, "#.###");
    }

    public double getRPB() {
        return RPB;
    }

    public String getRPHs() {
        return valida.DobleFormatoStringCeil(RPH, "#.###");
    }

    public double getRPH() {
        return RPH;
    }

    public String getDANs() {
        return valida.DobleFormatoStringCeil(DAN, "#.#");
    }

    public double getDAN() {
        return DAN;
    }

    public String getDCKs() {
        return valida.DobleFormatoStringCeil(DCK, "#.###");
    }

    public double getDCK() {
        return DCK;
    }

    public String getDCNs() {
        return valida.DobleFormatoStringCeil(DCN, "#.###");
    }

    public double getDCN() {
        return DCN;
    }

    public String getDP1s() {
        return valida.DobleFormatoStringCeil(DP1, "#.#");
    }

    public double getDP1() {
        return DP1;
    }

    public String getDP2s() {
        return valida.DobleFormatoStringCeil(DP2, "#.#");
    }

    public double getDP2() {
        return DP2;
    }

    public String getDP3s() {
        return valida.DobleFormatoStringCeil(DP3, "#.#");
    }

    public double getDP3() {
        return DP3;
    }

    public String getDRZs() {
        return valida.DobleFormatoStringCeil(DRZ, "#.##");
    }

    public double getDRZ() {
        return DRZ;
    }

    public String getDPAs() {
        return valida.DobleFormatoStringCeil(DPA, "#.#");
    }

    public double getDPA() {
        return DPA;
    }

    public String getDPBs() {
        return valida.DobleFormatoStringCeil(DPB, "#.#");
    }

    public double getDPB() {
        return DPB;
    }

    public String getDPCs() {
        return valida.DobleFormatoStringCeil(DPC, "#.#");
    }

    public double getDPC() {
        return DPC;
    }

    public String getDPDs() {
        return valida.DobleFormatoStringCeil(DPD, "#.#");
    }

    public double getDPD() {
        return DPD;
    }

    public String getDPEs() {
        return valida.DobleFormatoStringCeil(DPE, "#.#");
    }

    public double getDPE() {
        return DPE;
    }

    public String getDPFs() {
        return valida.DobleFormatoStringCeil(DPF, "#.#");
    }

    public double getDPF() {
        return DPF;
    }

    public String getDPGs() {
        return valida.DobleFormatoStringCeil(DPG, "#.#");
    }

    public double getDPG() {
        return DPG;
    }

    public String getDPKs() {
        return valida.DobleFormatoStringCeil(DPK, "#.#");
    }

    public double getDPK() {
        return DPK;
    }

    public String getDPNs() {
        return valida.DobleFormatoStringCeil(DPN, "#.#");
    }

    public double getDPN() {
        return DPN;
    }

    public String getDAHs() {
        return valida.DobleFormatoStringCeil(DAH, "#.##");
    }

    public double getDAH() {
        return DAH;
    }

    public String getDABs() {
        return valida.DobleFormatoStringCeil(DAB, "#.##");
    }

    public double getDAB() {
        return DAB;
    }

    public String getDVFs() {
        return valida.DobleFormatoStringCeil(DVF, "#.##");
    }

    public double getDVF() {
        return DVF;
    }

    public String getDLDs() {
        return valida.DobleFormatoStringCeil(DLD, "#.##");
    }

    public double getDLD() {
        return DLD;
    }

    public String getDALs() {
        return valida.DobleFormatoStringCeil(DAL, "#.##");
    }

    public double getDAL() {
        return DAL;
    }

    public String getDEMs() {
        return valida.DobleFormatoStringCeil(DEM, "#.##");
    }

    public double getDEM() {
        return DEM;
    }

    public int getDFL() {
        return DFL;
    }

    public String getDUPs() {
        return valida.DobleFormatoStringCeil(DUP, "#.##");
    }

    public double getDUP() {
        return DUP;
    }
    //---------------------------------------------------------------------------------------------------------- METHODS

    public boolean VentanasAbiertas() {

        if (WinDatosDeEntrada) {
            return true; //Bandera que evita la duplicación de la ventana
        }
        if (WinProyeccionPoblacional) {
            return true;
        }
        if (WinCalculoCaudalesDiseno) {
            return true;
        }
        if (WinCaractInicAguaResidual) {
            return true;
        }
        if (WinRejillas) {
            return true;
        }
        if (WinDesarenador) {
            return true;
        }
        if (WinLagunaAnaerobia) {
            return true;
        }
        if (WinLagunaFacultativaSec) {
            return true;
        }
        if (WinResultado) {
            return true;
        }
        return false;
    }

    /**
     * Trata de Guardar los datos privados a la base de datos, debe existir con
     * anterioridad una sesion insertada. El orden de insercion esta en el
     * archivo config
     *
     * @return
     */
    public boolean GuardarUpdateBod() {
        int res = 0;

        try {
            //String f_FCH = sqlidateformat.format(FCH);

            String[] sql = new String[]{
                FCH + "",
                TML + "",
                PCI + "",
                PUC + "",
                TCI + "",
                TUC + "",
                PDI + "",
                CRE + "",
                DMP + "",
                MEA + "",
                MEG + "",
                TCA + "",
                TCG + "",
                PPA + "",
                PPG + "",
                NCR,
                CAR + "",
                KAR + "",
                QIF + "",
                QIA + "",
                QEL + "",
                QET + "",
                QEC + "",
                QEK + "",
                QAA + "",
                QAI + "",
                QAC + "",
                valida.DobleAcadena(QAK),
                QCD + "",
                QCE + "",
                QNL + "",
                QCA + "",
                QCM + "",
                QCC + "",
                QCK + "",
                QNC + "",
                QNK + "",
                IOA + "",
                IRM + "",
                IAI + "",
                IPI + "",
                IQI + "",
                valida.DobleAcadena(IKI),
                IQ2 + "",
                valida.DobleAcadena(IK2),
                ICO + "",
                IQ3 + "",
                valida.DobleAcadena(IK3),
                ICM + "",
                IQ1 + "",
                valida.DobleAcadena(IK1),
                ICC + "",
                COA + "",
                CAC + "",
                CPC + "",
                CQC + "",
                valida.DobleAcadena(CKC),
                YOA + "",
                YAI + "",
                YPI + "",
                YQI + "",
                valida.DobleAcadena(YKI),
                valida.DobleAcadena(Q2C),
                valida.DobleAcadena(Q2K),
                Q3M + "",
                Q3R + "",
                valida.DobleAcadena(Q3C),
                valida.DobleAcadena(Q3K),
                Q1H + "",
                valida.DobleAcadena(Q1C),
                valida.DobleAcadena(Q1K),
                CAT + "",
                CAB + "",
                CAQ + "",
                CAS + "",
                CAV + "",
                CVS + "",
                LCO + "",
                LCE + "",
                LAE + "",
                valida.DobleAcadena(LVU),
                LTH + "",
                valida.DobleAcadena(LVR),
                LAU + "",
                valida.DobleAcadena(LAS),
                LLA + "",
                valida.DobleAcadena(LAA),
                valida.DobleAcadena(LAL),
                FTE + "",
                valida.DobleAcadena(FCO),
                FCA + "",
                valida.DobleAcadena(FAS),
                FTR + "",
                FED + "",
                valida.DobleAcadena(FUV),
                valida.DobleAcadena(FSA),
                valida.DobleAcadena(FSL),
                LAP + "",
                LAI + "",
                LAB + "",
                FAP + "",
                FAI + "",
                FAB + "",
                RPL + "",
                RTR + "",
                REB + "",
                REL + "",
                RIB + "",
                RBI + "",
                RPM + "",
                RPN + "",
                valida.DobleAcadena(RAU),
                RER + "",
                valida.DobleAcadena(RAT),
                valida.DobleAcadena(RAR),
                RCP + "",
                RCM + "",
                RLC + "",
                RVM + "",
                RVN + "",
                RNB + "",
                RNE + "",
                valida.DobleAcadena(RPC),
                valida.DobleAcadena(RPS),
                RPB + "",
                valida.DobleAcadena(RPH),
                DAN + "",
                DCK + "",
                DCN + "",
                valida.DobleAcadena(DP1),
                valida.DobleAcadena(DP2),
                valida.DobleAcadena(DP3),
                valida.DobleAcadena(DRZ),
                DPA + "",
                DPB + "",
                DPC + "",
                DPD + "",
                DPE + "",
                DPF + "",
                DPG + "",
                DPK + "",
                DPN + "",
                DAH + "",
                valida.DobleAcadena(DAB),
                DVF + "",
                DLD + "",
                valida.DobleAcadena(DAL),
                valida.DobleAcadena(DEM),
                DFL + "",
                valida.DobleAcadena(DUP),
                ((EditDatosDeEntrada) ? 1 : 0) + "", ((EditProyeccionPoblacional) ? 1 : 0) + "", ((EditCalculoCaudalesDiseno) ? 1 : 0) + "", ((EditCaractInicAguaResidual) ? 1 : 0) + "", ((EditLagunaAnaerobia) ? 1 : 0) + "", ((EditLagunaFacultativaSec) ? 1 : 0) + "", ((EditRejillas) ? 1 : 0) + "", ((EditDesarenador) ? 1 : 0) + "", valida.DobleAcadena((double) idproyecto)};

            res = db.ResultadoUpdateInsert("actualizarProyecto", sql); //trata de actualizar los datos de un proyecto en la bd.

            if (res == 1) { //1 = hubo update
                return true;
            }
        } catch (Exception ex) {
            util.Mensaje("No se pudieron Guardar los datos!", "error");
            log.error("Error en GuardarBod() " + ex.getMessage() + "res:" + res + " id: " + idproyecto);
        } finally {
            db.close(); //Se cierra la conexiòn
        }
        return false;
    }

    /**
     * Trata de insertar un nuevo proyecto en la bd.Obteniendo un id insertado
     *
     * @return : true o false segun el resultado.
     */
    public boolean CrearProyecto() {
        int id = 0, res = 0;

        try {
            res = db.ResultadoUpdateInsert("insertarProyecto", null, null); //trata de insertar un nuevo proyecto en la bd. 

            if (res == 1) { //1 = hubo insert
                ResultSet result = db.ResultadoSelect("ultimoinsertProyecto", null, null); //Trae el ultimo regiistro insertado para llenar el archivo
                id = Integer.parseInt(result.getString("id"));

                if (id > 0) {
                    if (util.CrearArchivoPtar(valida.DobleAcadena((double) id))) {
                        util.Mensaje("Archivo creado Exitosamente", "ok");
                        idproyecto = id;
                        return true;
                    }
                }
            }
            util.Mensaje("No se pudo crear el Archivo proyecto", "error");

        } catch (Exception ex) {
            util.Mensaje("No se pudo crear el Archivo proyecto", "error");
            log.error("Error en crearProyecto() " + ex.getMessage() + "res:" + res + " id: " + id);
        } finally {
            db.close(); //Se cierra la conexiòn
        }
        return false;
    }

    /**
     * *
     * Carga de un proyecto de un archivo .ptar se trae las variables y las
     * llena
     *
     * @param id
     * @return
     */
    public boolean CargarProyecto(int id) {

        /*Nota: 
         * hacen falta las flag, pero no se colocan ya que se actualizan en true al cargar datos
         */
        ResultSet res;

        try {
            res = db.ResultadoSelect("obtenerproyecto", valida.DobleAcadena((double) id), null); //trata de insertar un nuevo proyecto en la bd. 

            if (res.getString("FCH") == null) {//Es un proyecto vacío ...Todo: por ver
                idproyecto = id;
                return true;
            }

            FCH = res.getInt("FCH");
            TML = res.getDouble("TML");
            PCI = res.getInt("PCI");
            PUC = res.getInt("PUC");
            TCI = res.getInt("TCI");
            TUC = res.getInt("TUC");
            PDI = res.getInt("PDI");
            CRE = res.getDouble("CRE");
            DMP = res.getInt("DMP");
            MEA = res.getInt("MEA"); //Variables Proyección poblacional
            MEG = res.getInt("MEG");
            TCA = res.getDouble("TCA");
            TCG = res.getDouble("TCG");
            PPA = res.getInt("PPA");
            PPG = res.getInt("PPG");
            NCR = res.getString("NCR");
            CAR = res.getDouble("CAR");
            KAR = res.getDouble("KAR"); //Variables Cálculo de caudales de diseño
            QIF = res.getInt("QIF");
            QIA = res.getInt("QIA");
            QEL = res.getDouble("QEL");
            QET = res.getDouble("QET");
            QEC = res.getDouble("QEC");
            QEK = res.getDouble("QEK");
            QAA = res.getDouble("QAA");
            QAI = res.getDouble("QAI");
            QAC = res.getDouble("QAC");
            QAK = res.getDouble("QAK");
            QCD = res.getInt("QCD");
            QCE = res.getInt("QCE");
            QNL = res.getInt("QNL");
            QCA = res.getDouble("QCA");
            QCM = res.getDouble("QCM");
            QCC = res.getDouble("QCC");
            QCK = res.getDouble("QCK");
            QNC = res.getDouble("QNC");
            QNK = res.getDouble("QNK");
            IOA = res.getInt("IOA");
            IRM = res.getInt("IRM");
            IAI = res.getInt("IAI");
            IPI = res.getDouble("IPI");
            IQI = res.getDouble("IQI");
            IKI = res.getDouble("IKI");
            IQ2 = res.getDouble("IQ2");
            IK2 = res.getDouble("IK2");
            ICO = res.getInt("ICO");
            IQ3 = res.getDouble("IQ3");
            IK3 = res.getDouble("IK3");
            ICM = res.getDouble("ICM");
            IQ1 = res.getDouble("IQ1");
            IK1 = res.getDouble("IK1");
            ICC = res.getDouble("ICC");
            COA = res.getInt("COA");
            CAC = res.getDouble("CAC");
            CPC = res.getDouble("CPC");
            CQC = res.getDouble("CQC");
            CKC = res.getDouble("CKC");
            YOA = res.getInt("YOA");
            YAI = res.getDouble("YAI");
            YPI = res.getDouble("YPI");
            YQI = res.getDouble("YQI");
            YKI = res.getDouble("YKI");
            Q2C = res.getDouble("Q2C");
            Q2K = res.getDouble("Q2K");
            Q3M = res.getDouble("Q3M");
            Q3R = res.getInt("Q3R");
            Q3C = res.getDouble("Q3C");
            Q3K = res.getDouble("Q3K");
            Q1H = res.getDouble("Q1H");
            Q1C = res.getDouble("Q1C");
            Q1K = res.getDouble("Q1K");
            CAT = res.getDouble("CAT"); //Variables Características iniciales del agua residual
            CAB = res.getDouble("CAB");
            CAQ = res.getDouble("CAQ");
            CAS = res.getDouble("CAS");
            CAV = res.getDouble("CAV");
            CVS = res.getDouble("CVS");
            LCO = res.getDouble("LCO");//laguna anaerobia
            LCE = res.getDouble("LCE");
            LAE = res.getDouble("LAE");
            LVU = res.getDouble("LVU");
            LTH = res.getDouble("LTH");
            LVR = res.getDouble("LVR");
            LAU = res.getDouble("LAU");
            LAS = res.getDouble("LAS");
            LLA = res.getInt("LLA");
            LAA = res.getDouble("LAA");
            LAL = res.getDouble("LAL");
            LAP = res.getInt("LAP");
            LAI = res.getDouble("LAI");
            LAB = res.getDouble("LAB");
            FTE = res.getDouble("FTE"); //laguna Facultativa sec
            FCO = res.getDouble("FCO");
            FCA = res.getDouble("FCA");
            FAS = res.getDouble("FAS");
            FTR = res.getDouble("FTR");
            FED = res.getDouble("FED");
            FUV = res.getDouble("FUV");
            FSA = res.getDouble("FSA");
            FSL = res.getDouble("FSL");
            FAP = res.getInt("LAP");
            FAI = res.getDouble("LAI");
            FAB = res.getDouble("LAB");
            RPL = res.getDouble("RPL");//Rejillas
            RTR = res.getInt("RTR");
            REB = res.getDouble("REB");
            REL = res.getInt("REL");
            RIB = res.getInt("RIB");
            RBI = res.getInt("RBI");
            RPM = res.getDouble("RPM");
            RPN = res.getDouble("RPN");
            RAU = res.getDouble("RAU");
            RER = res.getDouble("RER");
            RAT = res.getDouble("RAT");
            RAR = res.getDouble("RAR");
            RCP = res.getDouble("RCP");
            RCM = res.getDouble("RCM");
            RLC = res.getDouble("RLC");
            RVM = res.getDouble("RVM");
            RVN = res.getDouble("RVN");
            RNB = res.getInt("RNB");
            RNE = res.getDouble("RNE");
            RPC = res.getDouble("RPC");
            RPS = res.getDouble("RPS");
            RPB = res.getDouble("RPB");
            RPH = res.getDouble("RPH");
            DAN = res.getDouble("DAN");//Desarenador
            DCK = res.getDouble("DCK");
            DCN = res.getDouble("DCN");
            DP1 = res.getDouble("DP1");
            DP2 = res.getDouble("DP2");
            DP3 = res.getDouble("DP3");
            DRZ = res.getDouble("DRZ");
            DPA = res.getDouble("DPA");
            DPB = res.getDouble("DPB");
            DPC = res.getDouble("DPC");
            DPD = res.getDouble("DPD");
            DPE = res.getDouble("DPE");
            DPF = res.getDouble("DPF");
            DPG = res.getDouble("DPG");
            DPK = res.getDouble("DPK");
            DPN = res.getDouble("DPN");
            DAH = res.getDouble("DAH");
            DAB = res.getDouble("DAB");
            DVF = res.getDouble("DVF");
            DLD = res.getDouble("DLD");
            DAL = res.getDouble("DAL");
            DEM = res.getDouble("DEM");
            DFL = res.getInt("DFL");
            DUP = res.getDouble("DUP");
            EditDatosDeEntrada = res.getBoolean("EditDatosDeEntrada");
            EditProyeccionPoblacional = res.getBoolean("EditProyeccionPoblacional");
            EditCalculoCaudalesDiseno = res.getBoolean("EditCalculoCaudalesDiseno");
            EditCaractInicAguaResidual = res.getBoolean("EditCaractInicAguaResidual");
            EditLagunaAnaerobia = res.getBoolean("EditLagunaAnaerobia");
            EditLagunaFacultativaSec = res.getBoolean("EditLagunaFacultativaSec");
            EditRejillas = res.getBoolean("EditRejillas");
            EditDesarenador = res.getBoolean("EditDesarenador");
            idproyecto = id;
            return true;
        } catch (Exception ex) {
            util.Mensaje("No se pudo Obtener proyecto", "error");
            log.error("Error en CargarProyecto() " + ex.getMessage() + " id: " + id);
        } finally {
            db.close(); //Se cierra la conexiòn
        }
        return false;
    }

    public String CalcularTCA() {

        Map<String, Double> valores = new HashMap<>();//Diccionario clave/valor
        valores.put("PUC", (double) PUC);
        valores.put("PCI", (double) PCI);
        valores.put("TUC", (double) TUC);
        valores.put("TCI", (double) TCI);

        return CalcularGeneral("metodoaritmeticotca", valores);
    }

    public String CalcularTCG() { //Todo: unificar ecuaciones

        Map<String, Double> valores = new HashMap<>();//Diccionario clave/valor
        valores.put("PUC", (double) PUC);
        valores.put("PCI", (double) PCI);
        valores.put("TUC", (double) TUC);
        valores.put("TCI", (double) TCI);

        return CalcularGeneral("metodogeometricotcg", valores);
    }

    public String CalcularPPA() {

        int TF = getFCH();
        String ecuacion = null;

        Map<String, Double> valores = new HashMap<>();
        valores.put("PUC", (double) PUC);
        valores.put("PCI", (double) PCI);
        valores.put("TUC", (double) TUC);
        valores.put("TCI", (double) TCI);
        valores.put("PDI", (double) PDI);
        valores.put("TF", (double) TF);

        return CalcularGeneral("metodogeometricoppa", valores);
    }

    public String CalcularPPG() {

        int TF = getFCH();
        String ecuacion = null;

        Map<String, Double> valores = new HashMap<>();
        valores.put("PUC", (double) PUC);
        valores.put("PCI", (double) PCI);
        valores.put("TUC", (double) TUC);
        valores.put("TCI", (double) TCI);
        valores.put("PDI", (double) PDI);
        valores.put("TF", (double) TF);

        return CalcularGeneral("metodogeometricoppg", valores);
    }

    public String CalcularNCR(double ppag) {

        return CalcularOtros("nivelcomplejidadras", "NCR", ppag);
    }

    public String CalcularCAR() {

        double pf;

        if (MEA > 0) {
            pf = PPA;
        } else {
            pf = PPG;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("CRE", (double) CRE);
        valores.put("DMP", (double) DMP);
        valores.put("pf", (double) pf);

        return CalcularGeneral("contribucionaguasresidualesls", valores);
    }

    public String CalcularKAR() {

        double pf;

        if (MEA > 0) {
            pf = PPA;
        } else {
            pf = PPG;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("CRE", (double) CRE);
        valores.put("DMP", (double) DMP);
        valores.put("pf", (double) pf);

        return CalcularGeneral("contribucionaguasresidualesm3", valores);
    }

    /**
     * *+ Calcula QEC y los parámetros se usan desde la clase IU, si son ceros
     * se calcula el resultado con datos de la propia clase.
     *
     * @param qel si cero => this.QEL
     * @param qet si cero => this.QET
     * @return
     */
    public String CalcularQEC(double qel, double qet) {

        if (qel == 0 || qet == 0) {
            qel = QEL;
            qet = QET;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("QEL", (double) qel);
        valores.put("QET", (double) qet);

        return CalcularGeneral("caudalinfiltracionqec", valores);
    }

    /**
     * *+ Calcula QEK y los parámetros se usan desde la clase IU, si son ceros
     * se calcula el resultado con datos de la propia clase.
     *
     * @param qel si cero => this.QEL
     * @param qet si cero => this.QET
     * @return
     */
    public String CalcularQEK(double qel, double qet) {

        if (qel == 0 || qet == 0) {
            qel = QEL;
            qet = QET;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("QEL", (double) qel);
        valores.put("QET", (double) qet);

        return CalcularGeneral("caudalinfiltracionqek", valores);
    }

    /**
     * *+ Calcula QAC y los parámetros se usan desde la clase IU, si son ceros
     * se calcula el resultado con datos de la propia clase.
     *
     * @param qaa si cero => this.QAA
     * @param qai si cero => this.QAI
     * @return
     */
    public String CalcularQAC(double qaa, double qai) {

        if (qaa == 0 || qai == 0) {
            qaa = QAA;
            qai = QAI;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("QAA", (double) qaa);
        valores.put("QAI", (double) qai);

        return CalcularGeneral("caudalinfiltracionqac", valores);
    }

    /**
     * Calcula QAK y los parámetros se usan desde la clase IU, si son ceros se
     * calcula el resultado con datos de la propia clase.
     *
     * @param qaa si cero => this.QAA
     * @param qai si cero => this.QAI
     * @return
     */
    public String CalcularQAK(double qaa, double qai) {

        if (qaa == 0 || qai == 0) {
            qaa = QAA;
            qai = QAI;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("QAA", (double) qaa);
        valores.put("QAI", (double) qai);

        return CalcularGeneral("caudalinfiltracionqak", valores);
    }

    /**
     * Calcula QAK y los parámetros se usan desde la clase IU, si son ceros se
     * calcula el resultado con datos de la propia clase.
     *
     * @param qaa si cero => this.QAA
     * @param qai si cero => this.QAI
     * @return
     */
    public String CalcularQCC(double qca, double qcm) {

        if (qca == 0 || qcm == 0) {
            qca = QCA;
            qcm = QCM;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("QCA", (double) qca);
        valores.put("QCM", (double) qcm);

        return CalcularGeneral("caudalconexioneserradasqcc", valores);
    }

    public String CalcularQCK(double qca, double qcm) {

        if (qca == 0 || qcm == 0) {
            qca = QCA;
            qcm = QCM;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("QCA", (double) qca);
        valores.put("QCM", (double) qcm);

        return CalcularGeneral("caudalconexioneserradasqck", valores);
    }

    public String CalcularQNC() {

        double pf;

        if (MEA > 0) {
            pf = PPA;
        } else {
            pf = PPG;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("pf", (double) pf);

        return CalcularGeneral("caudalconexioneserradasqnc", valores);
    }

    public String CalcularQNK() {

        double pf;

        if (MEA > 0) {
            pf = PPA;
        } else {
            pf = PPG;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("pf", (double) pf);

        return CalcularGeneral("caudalconexioneserradasqnk", valores);
    }

    /**
     * Calcula IQI y los parámetros se usan desde la clase IU, si son ceros se
     * calcula el resultado con datos de la propia clase.
     *
     * @param iai si cero => this.IAI
     * @param ipi si cero => this.IPI
     * @return
     */
    public String CalcularIQI(double iai, double ipi) {

        if (iai == 0 && ipi == 0) {
            iai = IAI;
            ipi = IPI;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("IAI", (double) iai);
        valores.put("IPI", (double) ipi);

        return CalcularGeneral("otrosaportesindustrialiqi", valores);
    }

    /**
     * Calcula IKI y los parámetros se usan desde la clase IU, si son ceros se
     * calcula el resultado con datos de la propia clase.
     *
     * @param iai si cero => this.IAI
     * @param ipi si cero => this.IPI
     * @return
     */
    public String CalcularIKI(double iai, double ipi) {

        if (iai == 0 && ipi == 0) {
            iai = IAI;
            ipi = IPI;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("IAI", (double) iai);
        valores.put("IPI", (double) ipi);

        return CalcularGeneral("otrosaportesindustrialiki", valores);
    }

    /**
     * Calcula IK2 y los parámetros se usan desde la clase IU, si son ceros se
     * calcula el resultado con datos de la propia clase.
     *
     * @param iq2 si cero => this.iq2
     * @return
     */
    public String CalcularIK2(double iq2) {

        if (iq2 == 0) {
            iq2 = IQ2;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("IQ2", (double) iq2);

        return CalcularGeneral("otrosaportesindustrialik2", valores);
    }

    /**
     * Calcula IK3 y los parámetros se usan desde la clase IU, si son ceros se
     * calcula el resultado con datos de la propia clase.
     *
     * @param icm si cero => this.ICM
     * @param iq3 si cero => this.IQ3
     * @return
     */
    public String CalcularIK3(double icm, double iq3) {

        if (icm == 0 && iq3 == 0) {
            icm = ICM;
            iq3 = IQ3;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("ICM", (double) icm);
        valores.put("IQ3", (double) iq3);

        return CalcularGeneral("otrosaportesindustrialik3", valores);
    }

    /**
     * Calcula IK1 y los parámetros se usan desde la clase IU, si son ceros se
     * calcula el resultado con datos de la propia clase.
     *
     * @param icc si cero => this.ICC
     * @param iq1 si cero => this.IQ1
     * @return
     */
    public String CalcularIK1(double icc, double iq1) {

        if (icc == 0 && iq1 == 0) {
            icc = ICC;
            iq1 = IQ1;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("ICC", (double) icc);
        valores.put("IQ1", (double) iq1);

        return CalcularGeneral("otrosaportesindustrialik1", valores);
    }

    /**
     * Calcula CQC y los parámetros se usan desde la clase IU, si son ceros se
     * calcula el resultado con datos de la propia clase.
     *
     * @param cac si cero => this.CAC
     * @param cpc si cero => this.CPC
     * @return
     */
    public String CalcularCQC(double cac, double cpc) {

        if (cac == 0 && cpc == 0) {
            cac = CAC;
            cpc = CPC;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("CAC", (double) cac);
        valores.put("CPC", (double) cpc);

        return CalcularGeneral("otrosaportescomercialcqc", valores);
    }

    /**
     * Calcula CKC y los parámetros se usan desde la clase IU, si son ceros se
     * calcula el resultado con datos de la propia clase.
     *
     * @param cac si cero => this.CAC
     * @param cpc si cero => this.CPC
     * @return
     */
    public String CalcularCKC(double cac, double cpc) {

        if (cac == 0 && cpc == 0) {
            cac = CAC;
            cpc = CPC;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("CAC", (double) cac);
        valores.put("CPC", (double) cpc);

        return CalcularGeneral("otrosaportescomercialckc", valores);
    }

    /**
     * Calcula YQI y los parámetros se usan desde la clase IU, si son ceros se
     * calcula el resultado con datos de la propia clase.
     *
     * @param yai si cero => this.YAI
     * @param ypi si cero => this.YPI
     * @return
     */
    public String CalcularYQI(double yai, double ypi) {

        if (yai == 0 && ypi == 0) {
            yai = YAI;
            ypi = YPI;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("YAI", (double) yai);
        valores.put("YPI", (double) ypi);

        return CalcularGeneral("otrosaportescomercialyqi", valores);
    }

    /**
     * Calcula YKI y los parámetros se usan desde la clase IU, si son ceros se
     * calcula el resultado con datos de la propia clase.
     *
     * @param yai si cero => this.YAI
     * @param ypi si cero => this.YPI
     * @return
     */
    public String CalcularYKI(double yai, double ypi) {

        if (yai == 0 && ypi == 0) {
            yai = YAI;
            ypi = YPI;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("YAI", (double) yai);
        valores.put("YPI", (double) ypi);

        return CalcularGeneral("otrosaportescomercialyki", valores);
    }

    /**
     * Esta funcion se puede llamar desde el UI, representa: a Q2C Caudal medio
     * diario = C + Qinf + Qce + Qi + Qc + Qin Si sus parámetros son cero
     * entonces esta siendo calculada por Bod. Las reglas de quien este
     * seleccionado, se dan en el UI,cuando se usa desde el UI al guardar
     *
     * @return
     */
    public String CalcularQ2C(double C, double Qinf, double Qce, double Qi, double Qc, double Qin) {

        if (C == 0 && Qinf == 0 && Qce == 0 && Qi == 0 && Qc == 0 && Qin == 0) {

            C = CAR;

            if (QIF > 0) {
                Qinf = QEC;
            }
            if (QIA > 0) {
                Qinf = QAC;
            }

            if (QCD > 0) {
                if (QCE > 0) {
                    Qce = QCC;
                }
                if (QNL > 0) {
                    Qce = QNC;
                }
            }

            if (IOA > 0 && IRM < 1) {
                Qi = IQI;
            }
            if (IOA > 0 && IRM > 0) {
                Qi = IQ2;
            }

            if (COA > 0) {
                Qc = CQC;
            }
            if (YOA > 0) {
                Qin = YQI;
            }
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("C", C);
        valores.put("Qinf", Qinf);
        valores.put("Qce", Qce);
        valores.put("Qi", Qi);
        valores.put("Qc", Qc);
        valores.put("Qin", Qin);

        return CalcularGeneral("caudalmediodiarioq2c", valores);
    }

    /**
     * Esta funcion se puede llamar desde el UI, representa: a Q2K Caudal medio
     * diario = Q2K * 86400/1000 Si sus parámetros son cero entonces esta siendo
     * calculada por Bod. Las reglas de quien este seleccionado, se dan en el
     * UI,cuando se usa desde el UI al guardar
     *
     * @return
     */
    public String CalcularQ2K(double C, double Qinf, double Qce, double Qi, double Qc, double Qin) { //Todo , las funciones complementarias (Q2C) pueden reducir estas lineas aunque no se respetarian reglas 

        if (C == 0 && Qinf == 0 && Qce == 0 && Qi == 0 && Qc == 0 && Qin == 0) {

            C = CAR;

            if (QIF > 0) {
                Qinf = QEC;
            }
            if (QIA > 0) {
                Qinf = QAC;
            }

            if (QCD > 0) {
                if (QCE > 0) {
                    Qce = QCC;
                }
                if (QNL > 0) {
                    Qce = QNC;
                }
            }

            if (IOA > 0 && IRM < 1) {
                Qi = IQI;
            }
            if (IOA > 0 && IRM > 0) {
                Qi = IQ2;
            }

            if (COA > 0) {
                Qc = CQC;
            }
            if (YOA > 0) {
                Qin = YQI;
            }
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("C", C);
        valores.put("Qinf", Qinf);
        valores.put("Qce", Qce);
        valores.put("Qi", Qi);
        valores.put("Qc", Qc);
        valores.put("Qin", Qin);

        return CalcularGeneral("caudalmediodiarioq2k", valores);
    }

    /**
     * Esta funcion se puede llamar desde el UI, representa: a Q2K Caudal medio
     * diario em m(cubicos)/dìa
     *
     * @return
     */
    public String CalcularQ2K(double q2c) {

        if (q2c == 0) {
            q2c = Q2C;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        return CalcularGeneral("caudalmediodiarioq2ck", valores);
    }

    public String calcularQ3M() { //Se calcula solo al guardar, no mientras el user este editando

        double pf;

        if (MEA > 0) {
            pf = PPA;
        } else {
            pf = PPG;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("pf", pf);

        return CalcularGeneral("caudalmaximodiarioq3m", valores);
    }

    /**
     * Esta funcion se puede llamar desde el UI, representa: a Q3C Caudal máximo
     * diario . Si sus parámetros son cero entonces esta siendo calculada por
     * Bod. Las reglas de quien este seleccionado, se dan en el UI,cuando se usa
     * desde el UI al guardar
     *
     * @return
     */
    public String CalcularQ3C(double C, double K, double Qinf, double Qce, double Qi, double Qc, double Qin) {

        if (C == 0 && K == 0 && Qinf == 0 && Qce == 0 && Qi == 0 && Qc == 0 && Qin == 0) {

            C = CAR;

            if (QIF > 0) {
                Qinf = QEC;
            }
            if (QIA > 0) {
                Qinf = QAC;
            }

            if (QCD > 0) {
                if (QCE > 0) {
                    Qce = QCC;
                }
                if (QNL > 0) {
                    Qce = QNC;
                }
            }

            if (IOA > 0 && IRM < 1) {
                Qi = IQI;
            }
            if (IOA > 0 && IRM > 0) {
                Qi = IQ3;
            }

            if (COA > 0) {
                Qc = CQC;
            }
            if (YOA > 0) {
                Qin = YQI;
            }

            K = Q3M;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("C", C);
        valores.put("Qinf", Qinf);
        valores.put("Qce", Qce);
        valores.put("Qi", Qi);
        valores.put("Qc", Qc);
        valores.put("Qin", Qin);
        valores.put("K", K);

        return CalcularGeneral("caudalmaximodiarioq3c", valores);
    }

    /**
     * Esta funcion se puede llamar desde el UI, representa: a Q3K Caudal máximo
     * diario . Si sus parámetros son cero entonces esta siendo calculada por
     * Bod. Las reglas de quien este seleccionado, se dan en el UI,cuando se usa
     * desde el UI al guardar
     *
     * @return
     */
    public String CalcularQ3K(double C, double K, double Qinf, double Qce, double Qi, double Qc, double Qin) {

        if (C == 0 && K == 0 && Qinf == 0 && Qce == 0 && Qi == 0 && Qc == 0 && Qin == 0) {

            C = CAR;

            if (QIF > 0) {
                Qinf = QEC;
            }
            if (QIA > 0) {
                Qinf = QAC;
            }

            if (QCD > 0) {
                if (QCE > 0) {
                    Qce = QCC;
                }
                if (QNL > 0) {
                    Qce = QNC;
                }
            }

            if (IOA > 0 && IRM < 1) {
                Qi = IQI;
            }
            if (IOA > 0 && IRM > 0) {
                Qi = IQ3;
            }
            if (COA > 0) {
                Qc = CQC;
            }
            if (YOA > 0) {
                Qin = YQI;
            }
            K = Q3M;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("C", C);
        valores.put("Qinf", Qinf);
        valores.put("Qce", Qce);
        valores.put("Qi", Qi);
        valores.put("Qc", Qc);
        valores.put("Qin", Qin);
        valores.put("K", K);

        return CalcularGeneral("caudalmaximodiarioq3k", valores);
    }

    /**
     * Esta funcion se puede llamar desde el UI, representa: a Q1C Caudal mínimo
     * diario. Si sus parámetros son cero entonces esta siendocalculada por Bod.
     * Las reglas de quien este seleccionado, se dan en el UI,cuando se usa
     * desde el UI al guardar
     *
     * @return
     */
    public String CalcularQ1C(double C, double K3, double Qinf, double Qi, double Qc, double Qin) {

        if (C == 0 && K3 == 0 && Qinf == 0 && Qi == 0 && Qc == 0 && Qin == 0) {

            C = CAR;

            if (QIF > 0) {
                Qinf = QEC;
            }
            if (QIA > 0) {
                Qinf = QAC;
            }
            if (IOA > 0 && IRM < 1) {
                Qi = IQI;
            }
            if (IOA > 0 && IRM > 0) {
                Qi = IQ1;
            }
            if (COA > 0) {
                Qc = CQC;
            }
            if (YOA > 0) {
                Qin = YQI;
            }
            K3 = Q1H;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("C", C);
        valores.put("Qinf", Qinf);
        valores.put("Qi", Qi);
        valores.put("Qc", Qc);
        valores.put("Qin", Qin);
        valores.put("K3", K3);

        return CalcularGeneral("caudalminimodiarioq1c", valores);
    }

    /**
     * Esta funcion se puede llamar desde el UI, representa: a Q1K Caudal mínimo
     * diario. Si sus parámetros son cero entonces esta siendocalculada por Bod.
     * Las reglas de quien este seleccionado, se dan en el UI,cuando se usa
     * desde el UI al guardar
     *
     * @return
     */
    public String CalcularQ1K(double C, double K3, double Qinf, double Qi, double Qc, double Qin) {

        if (C == 0 && K3 == 0 && Qinf == 0 && Qi == 0 && Qc == 0 && Qin == 0) {

            C = CAR;

            if (QIF > 0) {
                Qinf = QEC;
            }
            if (QIA > 0) {
                Qinf = QAC;
            }
            if (IOA > 0 && IRM < 1) {
                Qi = IQI;
            }
            if (IOA > 0 && IRM > 0) {
                Qi = IQ1;
            }
            if (COA > 0) {
                Qc = CQC;
            }
            if (YOA > 0) {
                Qin = YQI;
            }
            K3 = Q1H;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("C", C);
        valores.put("Qinf", Qinf);

        valores.put("Qi", Qi);
        valores.put("Qc", Qc);
        valores.put("Qin", Qin);
        valores.put("K3", K3);

        return CalcularGeneral("caudalminimodiarioq1k", valores);
    }

    /**
     * *
     * Calcula LCO (Carga Orgánica Volumétrica) Puede ser llamado desde el UI
     * con parámetro
     *
     * @param cat : temperatura CAT
     * @return
     */
    public String CalcularLCO(double cat) {

        if (cat < 1) {
            cat = CAT;
        }
        return CalcularOtros("cargaorganicavolumetrica", "LCO", cat);
    }

    /**
     * *
     * Calcula LCE (Eficiencia de DBO5 estimada) Puede ser llamado desde el UI
     * con parámetro
     *
     * @param cat : temperatura CAT
     * @return
     */
    public String CalcularLCE(double cat) {

        if (cat < 1) {
            cat = CAT;
        }
        return CalcularOtros("eficienciadbo5estimada", "LCE", cat);
    }

    /**
     * *
     * Calcula LAE (DBO5 en el efluente de la laguna Anaerobia) Puede ser
     * llamado desde el UI con parámetro
     *
     * @param lce : LCE
     * @return
     */
    public String CalcularLAE(double lce) {

        if (lce < 1) {
            lce = LCE;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("CAB", CAB);
        valores.put("LCE", lce);

        return CalcularGeneral("dbo5efluentelagunanaerobia", valores);
    }

    /**
     * *
     * Calcula LVU (Volumen Útil) Puede ser llamado desde el UI con parámetro
     *
     * @param lco : LCO
     * @return
     */
    public String CalcularLVU(double lco) { //Todo: estas funciones deben devolver dobles no strings

        if (lco < 1) {
            lco = LCO;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("CAB", CAB);
        valores.put("Q2C", Q2C);
        valores.put("LCO", lco);

        return CalcularGeneral("volumenutil", valores);
    }

    /**
     * *
     * Calcula LTH (Tiempo de retención hidráulico) Puede ser llamado desde el
     * UI con parámetro
     *
     * @param lvu : LVU
     * @return
     */
    public String CalcularLTH(double lvu) { //Todo: estas funciones deben devolver dobles no strings

        if (lvu < 1) {
            lvu = LVU;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", Q2C);
        valores.put("LVU", lvu);

        String x = CalcularGeneral("tiemporetencionhidraulico", valores);

        if (Double.parseDouble(x) < 1) { //Regla: Si tiempo de retención hidráulico menor a (1) día, se ajustará dicho valor
            return "1";
        }
        return x;
    }

    /**
     * *
     * Calcula LVR (Volumen Útil Recalculado) Puede ser llamado desde el UI con
     * parámetro
     *
     * @param lth : LTH
     * @return
     */
    public String CalcularLVR(double lth) {

        if (lth <= 0) {
            lth = LTH;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", Q2C);
        valores.put("LTH", lth);

        return CalcularGeneral("volumenutilrecalculado", valores);
    }

    /**
     * *
     * Calcula LAS (Área Superficial) Puede ser llamado desde el UI con
     * parámetros
     *
     * @param lau : LAU
     * @param lvr : LVR
     * @return
     */
    public String CalcularLAS(double lau, double lvr) {

        if (lau <= 0) {
            lau = LAU;
        }

        if (lvr <= 0) {
            lvr = LVR;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("LVR", lvr);
        valores.put("LAU", lau);

        return CalcularGeneral("areasuperficial", valores);
    }

    /**
     * *
     * Calcula LAA (Ancho (aLA)) para Relación Largo: Ancho 1:3, Puede ser
     * llamado desde el UI con parámetro
     *
     * @param las : LAS
     * @return
     */
    public String CalcularLAA_1_3(double las) {

        if (las <= 0) {
            las = LAS;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAS", las);

        return CalcularGeneral("ancho1_3", valores);
    }

    /**
     * *
     * Calcula LAA (Ancho (aLA)) para Relación Largo: Ancho 2:3, Puede ser
     * llamado desde el UI con parámetro
     *
     * @param las : LAS
     * @return
     */
    public String CalcularLAA_2_3(double las) {

        if (las <= 0) {
            las = LAS;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAS", las);

        return CalcularGeneral("ancho2_3", valores);
    }

    /**
     * *
     * Calcula LAL (Largo (l LA)) para Relación Largo: Ancho 1:3, Puede ser
     * llamado desde el UI con parámetro
     *
     * @param laa : LAA
     * @return
     */
    public String CalcularLAL_1_3(double laa) {

        if (laa <= 0) {
            laa = LAA;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAA", laa);

        return CalcularGeneral("largo1_3", valores);
    }

    /**
     * *
     * Calcula LAL (Largo (l LA)) para Relación Largo: Ancho 2:3, Puede ser
     * llamado desde el UI con parámetro
     *
     * @param laa : LAA
     * @return
     */
    public String CalcularLAL_2_3(double laa) {

        if (laa <= 0) {
            laa = LAA;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAA", laa);

        return CalcularGeneral("largo2_3", valores);
    }

    /**
     * *
     * Calcula FCO (Carga Orgánica Volumétrica COVLF), Puede ser llamado desde
     * el UI con parámetro
     *
     * @return
     */
    public String CalcularFCO() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("CAT", CAT);

        return CalcularGeneral("cargaorganicavolumetricalf", valores);
    }

    /**
     * *
     * Calcula FAS (Área Superficial (ASLF)), Puede ser llamado desde el UI con
     * parámetro
     *
     * @param fco : FCO
     * @return
     */
    public String CalcularFAS(double fco) {

        if (fco <= 0) {
            fco = FCO;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAE", LAE);
        valores.put("Q2C", Q2C);
        valores.put("FCO", fco);

        return CalcularGeneral("areasuperficiallf", valores);
    }

    /**
     * *
     * Calcula FTR (Tiempo de retención hidráulico(TRLF)), Puede ser llamado
     * desde el UI con parámetro
     *
     * @param fas : FAS
     * @param fca : FCA
     * @param fte : FTE
     * @return
     */
    public String CalcularFTR(double fas, double fca, double fte) {

        if (fas <= 0) {
            fas = FAS;
        }

        if (fca <= 0) {
            fca = FCA;
        }

        if (fte <= 0) {
            fte = FTE;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("FAS", fas);
        valores.put("FCA", fca);
        valores.put("FTE", fte);
        valores.put("Q2C", Q2C);

        String x = CalcularGeneral("tiemporetencionhidraulicolf", valores);

        if (Double.parseDouble(x) < 4) { //REGLA: el sistema debe redondear los valores que sean TRLF<4 días a 4 días. 
            return "4";
        }
        return x;
    }

    /**
     * *
     * Calcula FED (Eficiencia de DBO5 estimada LF), Puede ser llamado desde el
     * UI con parámetro
     *
     * @param ftr : FTR
     * @return
     */
    public String CalcularFED(double ftr) {

        if (ftr <= 0) {
            ftr = FTR;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAE", LAE);
        valores.put("CAT", CAT);
        valores.put("FTR", ftr);

        return CalcularGeneral("eficienciadbo5estimadalf", valores);
    }

    /**
     * *
     * Calcula FUV (Volumen Útil), Puede ser llamado desde el UI con parámetro
     *
     * @param fco : FCO
     * @return
     */
    public String CalcularFUV(double fco) {

        if (fco <= 0) {
            fco = FTR;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAE", LAE);
        valores.put("Q2C", Q2C);
        valores.put("FCO", fco);

        return CalcularGeneral("volumenutillf", valores);
    }

    /**
     * *
     * Calcula FSA (Relación Ancho LF), Puede ser llamado desde el UI con
     * parámetro
     *
     * @param fas : FAS
     * @return
     */
    public String CalcularFSA(double fas) {

        if (fas <= 0) {
            fas = FAS;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("FAS", fas);

        return CalcularGeneral("relacionancholf", valores);
    }

    /**
     * *
     * Calcula FSL (Relación Largo LF), Puede ser llamado desde el UI con
     * parámetro
     *
     * @param fsa : FSA
     * @return
     */
    public String CalcularFSL(double fsa) {

        if (fsa <= 0) {
            fsa = FSA;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("FSA", fsa);

        return CalcularGeneral("relacionlargolf", valores);
    }

    /**
     * *
     * Calcula LAI (Ángulo de inclinación de la pendiente), Puede ser llamado
     * desde el UI con parámetro
     *
     * @param lap : LAP
     * @return
     */
    public String calcularLAI(int lap) {

        if (lap <= 0) {
            lap = LAP;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAP", (double) lap);
        valores.put("pi", Math.PI);

        return CalcularGeneral("angulopendientelai", valores);
    }

    /**
     * *
     * Calcula LAB (Borde Libre), Puede ser llamado desde el UI con parámetro
     *
     * @param las : LAS
     * @return
     */
    public String calcularLAB(double las) {

        if (las <= 0) {
            las = LAS;
        }
        return CalcularOtros("bordelibrelab", "LAB", las);
    }

    /**
     * *
     * Calcula FAI (Ángulo de inclinación de la pendiente), Puede ser llamado
     * desde el UI con parámetro
     *
     * @param fap : FAP
     * @return
     */
    public String calcularFAI(int fap) {

        if (fap <= 0) {
            fap = FAP;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("FAP", (double) fap);
        valores.put("pi", Math.PI);

        return CalcularGeneral("angulopendientefai", valores);
    }

    /**
     * *
     * Calcula FAB (Borde Libre), Puede ser llamado desde el UI con parámetro
     *
     * @param fas : FAS
     * @return
     */
    public String calcularFAB(double fas) {

        if (fas <= 0) {
            fas = FAS;
        }
        return CalcularOtros("bordelibrefab", "FAB", fas);
    }

    /**
     * *
     * Calcula Q1C para REJILLAS pues es dado en metros cúbicos
     *
     * @return
     */
    public String calcularQ1C_m3() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q1C", (double) Q1C);
        return CalcularGeneral("caudalminimodiariom3", valores);
    }

    /**
     * *
     * Calcula Q2C en metros cúbicos
     *
     * @return
     */
    public String calcularQ2C_m3() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", (double) Q2C);

        return CalcularGeneral("caudalmediodiariom3", valores);
    }

    /**
     * *
     * Calcula Q2C en metros cúbicos
     *
     * @return
     */
    public double calcularQ2Cm3() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", (double) Q2C);

        return CalcularGeneral_("caudalmediodiariom3", valores);
    }

    /**
     * Calcula Q2C Caudal medio diario en m(cubicos)/dìa m3/día
     *
     * @return Q2C en m3/día
     */
    public double CalcularQ2Cm3Dia() { //Anterior Q2K

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", Q2C);
        return CalcularGeneral_("caudalmediodiarioq2ck", valores);
    }

    /**
     * Calcula Q3C Caudal medio diario en m(cubicos)/dìa m3/día
     *
     * @return Q3C en m3/día
     */
    public double CalcularQ3Cm3Dia() { //Anterior Q3K

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q3C", Q3C);
        return CalcularGeneral_("caudalmaximodiarioq3kc", valores);
    }

    /**
     * *
     * Calcula Q3C en metros cúbicos
     *
     * @return
     */
    public double calcularQ3Cm3() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q3C", (double) Q3C);
        return CalcularGeneral_("caudalmaximodiariom3", valores);
    }

    /**
     * *
     * Calcula Q3C en metros cúbicos
     *
     * @return
     */
    public String calcularQ3C_m3() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q3C", (double) Q3C);
        return CalcularGeneral("caudalmaximodiariom3", valores);
    }

    /**
     * Características del agua residual: Cálculo de la Relación SSV/SST en el
     * afluente
     *
     * @return
     */
    public double calcularCVS() {

        if (CAS == 0 || CAV == 0) {
            return 0;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("CAS", CAS);
        valores.put("CAV", CAV);

        return CalcularGeneral_("relacionssvsstafluente", valores);
    }

    /**
     * *
     * Calcula RAU Área útil entre las barras para el escurrimiento (Au)
     *
     ** @param rpm : RPM del UI
     * @return
     */
    public String calcularRAU(double rpm) {

        if (rpm <= 0) {
            rpm = RPM;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q3C", (double) Q3C); //En la formula Q3C se divide /mil
        valores.put("RPM", (double) rpm);
        return CalcularGeneral("caudalmaximodiariom3", valores);
    }

    /**
     * *
     * Calcula RER Eficiencia de las rejillas en función del espesor (t) y el
     * espaciamiento entre barras (a)
     *
     * @param reb : REB del UI
     * @param rel : REL del UI
     * @return
     */
    public String calcularRER(double reb, double rel) {

        if (reb <= 0) {
            reb = REB;
        }
        if (rel <= 0) {
            rel = REL;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("REB", (double) reb);
        valores.put("REL", (double) rel);
        return CalcularGeneral("eficrejilfuncespsor", valores);
    }

    /**
     * *
     * Calcula RAT Área total de las rejillas (incluidas las barras) (S)
     *
     * @param rau : RAU del UI
     * @param rer : RER del UI
     * @return
     */
    public String calcularRAT(double rau, double rer) {

        if (rau <= 0) {
            rau = RAU;
        }
        if (rer <= 0) {
            rer = RER;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("RAU", (double) rau);
        valores.put("RER", (double) rer);
        return CalcularGeneral("areatotrejillybarrs", valores);
    }

    /**
     * *
     * Calcula RAR Ancho de rejillas (b)
     *
     * @param rat : RAT del UI
     * @param rpl : RPL del UI
     * @return
     */
    public String calcularRAR(double rat, double rpl) {

        if (rat <= 0) {
            rat = RAT;
        }
        if (rpl <= 0) {
            rpl = RPL;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("RAT", (double) rat);
        valores.put("RPL", (double) rpl);
        return CalcularGeneral("anchorejillasb", valores);
    }

    /**
     * * Calcula RCP Velocidad máxima (Vmax)
     *
     * @param rar : RAR desde UI
     * @param rpl : RPL desde UI
     * @param rer : RER desde UI
     * @return
     */
    public String calcularRCP(double rar, double rpl, double rer) {

        if (rar <= 0) {
            rar = RAR;
        }

        if (rpl <= 0) {
            rpl = RPL;
        }

        if (rer <= 0) {
            rer = RER;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("RAR", (double) rar);
        valores.put("RPL", (double) rpl);
        valores.put("RER", (double) rer);
        valores.put("Q3C", (double) Q3C);
        return CalcularGeneral("velocidadmaximarcp", valores);
    }

    /**
     * * Calcula RCM Velocidad media (Vmed)
     *
     * @param rar : RAR desde UI
     * @param rpl : RPL desde UI
     * @param rer : RER desde UI
     * @return
     */
    public String calcularRCM(double rar, double rpl, double rer) {

        if (rar <= 0) {
            rar = RAR;
        }

        if (rpl <= 0) {
            rpl = RPL;
        }

        if (rer <= 0) {
            rer = RER;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("RAR", (double) rar);
        valores.put("RPL", (double) rpl);
        valores.put("RER", (double) rer);
        valores.put("Q2C", (double) Q2C);
        return CalcularGeneral("velocidadmediarcm", valores);
    }

    /**
     * *
     * Calcula RLC Longitud del canal (L)
     *
     * @param rat : RAT del UI
     * @return
     */
    public String calcularRLC(double rat) {

        if (rat <= 0) {
            rat = RAT;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("RAT", (double) rat);
        valores.put("Q3C", (double) Q3C);
        return CalcularGeneral("longitudcanalrlc", valores);
    }

    /**
     * * Calcula RVM Velocidad de aproximación Vo para el Caudal Máximo
     *
     * @param rar : RAR desde UI
     * @param rpl : RPL desde UI
     * @return
     */
    public String calcularRVM(double rar, double rpl) {

        if (rar <= 0) {
            rar = RAR;
        }

        if (rpl <= 0) {
            rpl = RPL;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("RAR", (double) rar);
        valores.put("RPL", (double) rpl);
        valores.put("Q3C", (double) Q3C);
        return CalcularGeneral("velaproxvocaudalmax", valores);
    }

    /**
     * Calcula RVN Velocidad de aproximación Vo para el caudal medio
     *
     * @param rar : RAR desde UI
     * @param rpl : RPL desde UI
     * @return
     */
    public String calcularRVN(double rar, double rpl) {

        if (rar <= 0) {
            rar = RAR;
        }

        if (rpl <= 0) {
            rpl = RPL;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("RAR", (double) rar);
        valores.put("RPL", (double) rpl);
        valores.put("Q2C", (double) Q2C);
        return CalcularGeneral("velaproxvocaudalmed", valores);
    }

    /**
     * Calcula RNB Número de barras (Nb) en la rejilla
     *
     * @param rar : RAR desde UI
     * @param rel : REL desde UI
     * @param reb : REB desde UI
     * @return
     */
    public String calcularRNB(double rar, double rel, double reb) {

        if (rar <= 0) {
            rar = RAR;
        }

        if (rel <= 0) {
            rel = REL;
        }

        if (reb <= 0) {
            reb = REB;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("RAR", (double) rar);
        valores.put("REL", (double) rel);
        valores.put("REB", (double) reb);

        String x = CalcularGeneral("numerobarrasenrejilla", valores);

        if (x.contains(".")) {
            return (int) (Double.parseDouble(x)) + "";
        }
        return x;
    }

    /**
     * Calcula RNE Número de espaciamientos (Ne) en la rejilla
     *
     * @param rnb : RNB desde UI
     * @return
     */
    public String calcularRNE(double rnb) {

        if (rnb <= 0) {
            rnb = RNB;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("RNB", (double) rnb);
        return CalcularGeneral("numeroespaciamrejilla", valores);
    }

    /**
     * Calcula Pérdida de carga en la rejilla limpia (hL)
     *
     * @param rcp : RCP desde UI
     * @param rvm : RVM desde UI
     * @return
     */
    public String calcularRPC(double rcp, double rvm) {

        if (rcp <= 0) {
            rcp = RCP;
        }

        if (rvm <= 0) {
            rvm = RVM;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("RCP", (double) rcp);
        valores.put("RVM", (double) rvm);
        return CalcularGeneral("perdidcargrejillimpi", valores);
    }

    /**
     * Calcula Pérdida de carga en la rejilla 50% sucia (hL)
     *
     * @param rcp : RCP desde UI
     * @param rvm : RVM desde UI
     * @return
     */
    public String calcularRPS(double rcp, double rvm) {

        if (rcp <= 0) {
            rcp = RCP;
        }

        if (rvm <= 0) {
            rvm = RVM;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("RCP", (double) rcp);
        valores.put("RVM", (double) rvm);
        return CalcularGeneral("perdidcargrejil50limp", valores);
    }

    /**
     * Calcula RPH Pérdida de carga calculada por la ecuación de Kirshmer
     *
     * @param rpb : RPB desde UI
     * @param reb : REB desde UI
     * @param rel : REL desde UI
     * @param rib : RIB desde UI
     * @param rcp : RCP desde UI
     * @return
     */
    public String calcularRPH(double rpb, double reb, double rel, double rib, double rcp) {

        if (rpb <= 0) {
            rpb = RPB;
        }

        if (reb <= 0) {
            reb = REB;
        }

        if (rel <= 0) {
            rel = REL;
        }

        if (rib <= 0) {
            rib = RIB;
        }

        if (rcp <= 0) {
            rcp = RCP;
        }

        rib = Math.toRadians(rib);

        Map<String, Double> valores = new HashMap<>();
        valores.put("RPB", (double) rpb);
        valores.put("REB", (double) reb);
        valores.put("REL", (double) rel);
        valores.put("RIB", (double) rib);
        valores.put("RCP", (double) rcp);

        return CalcularGeneral("perdcargcalculeckirshmer", valores);
    }

    /**
     * Calcula número sugerido para Ancho nominal (W). Esta funcion no esta en
     * la base de datos, sin estandarizar! para ver mas (buscar excel)
     *
     * @param listaTabla : Cadena que contiene la configuracion de numeros de la
     * tabla relacionada
     * @param entradaCm : es la entreda del usuario
     *
     * @return: "vacio;mensaje" error: "mensage;mensage"
     */
    public String calcularDAN(String listaTabla, double entradaCm) {

        String[] nums = listaTabla.split("\\|");
        String msg = "";
        double entradaQmax = Q3C; //Debe ser en L/s  
        double minQmax = Double.parseDouble((nums[0].split(";"))[2]);
        double maxQmax = Double.parseDouble((nums[nums.length - 1].split(";"))[2]);
        double pulgada = 2.54;
        double sugerido = 0;
        double cercano = 0;
        int x = nums.length;

        if (!valida.EsDobleEntre(entradaQmax + "", minQmax, maxQmax)) { //Qmax entrada está entre extremos Qmax definidos
            return "Qmax fuera de rango; ; ";// 0k = "vacio;mensaje" error: "mensage;mensage sugerido..."
        }

        for (int i = 0; i < x; i++) {

            if (i == (x - 1)) {
                break;
            }

            String[] a = nums[i].split(";"); //ej. Primero [3] [7.6] [53.8] 
            String[] b = nums[i + 1].split(";");//ej. Segundo [6] [15.2] [110.4] 

            double infC = Double.parseDouble(a[1]);
            double supC = Double.parseDouble(b[1]);
            double infQ = Double.parseDouble(a[2]);
            double supQ = Double.parseDouble(b[2]);

            if (valida.EsDobleEntre(entradaQmax + "", infQ, supQ)) { //Qmax entrada está entre algun 'Qmax definidos'
                //donde esta Qmax entrada , alli debe estar Cm ingresados, si no error
                if (valida.EsDobleEntre(entradaCm + "", infC, supC)) { //Está entre cm, entonces se calcula el numero ideal

                    //El numero esta entre los cm correctos, se procede a hallar a cual extremo esta mas cercano
                    if (Math.abs(infC - entradaCm) > Math.abs(supC - entradaCm)) {
                        cercano = supC;
                    } else {
                        cercano = infC;
                    }

                    int infN = Integer.parseInt(a[0]);
                    int supN = Integer.parseInt(b[0]);
                    int difN = supN - infN;

                    double infQ_temp = infQ;

                    try {
                        for (int j = 0; j < difN; j++) {
                            double infQ_temp2 = infQ_temp + ((supQ - infQ) / difN);

                            if (valida.EsDobleEntre(entradaQmax + "", infQ_temp, infQ_temp2)) { //El Qmax entrada esta entre los numeros que pueden existir entre infQ y supQ
                                sugerido = pulgada * (infN + j); //esto no es exacto
                                break;
                            }
                            infQ_temp = infQ_temp2;
                        }
                    } catch (Exception ex) {
                        return " ; ;" + cercano; //No se puedo calcular el sugerido, no es importante.
                    }
                } else {
                    return "Número ingresado (cm) fuera de rango a Qmax (ver ayuda); ; ";
                }
            }
        }
        if (sugerido > 0) {
            return " ; sugerido: " + sugerido + ";" + cercano; // 0k = "vacio;mensaje sugerido" error: "mensage;mensage sugerido..."
        }

        return " ; ;" + cercano;
    }

    /**
     * Calcula Coeficiente K , para la Profundidad de la lámina de agua (H) para
     * Qmax, Qmed y Qmin. Recibe una cadena que contiene los rangos de la tabla
     * de ayuda para descomponerlos y obtener el numero relacionado al
     * introducido en DAN
     *
     * @return número relacionado al introducido en DAN
     */
    public String calcularDCKDCN(String listaTablaDck, double numeroDAN) {

        String[] nums = listaTablaDck.split("\\|");

        numeroDAN = numeroDAN / 100;//DAN en mts acá

        for (int i = 0; i < nums.length - 1; i++) { //(-1 para no llegar al limite y desbordar). Se sacan de la tabla dos filas ej. fila 1 y fila 2 y se comapara si numeroDAN esta entre.
            String[] a = nums[i].split(";");
            String[] b = nums[i + 1].split(";");

            if (numeroDAN >= Double.parseDouble(a[1]) && numeroDAN <= Double.parseDouble(b[1])) {
                return a[3] + ";" + a[2]; //Ver la tabla
            }
        }
        return "";
    }

    /**
     * Valcula Hmax para la profundidad de la lámina de agua (H) para Qmax
     *
     * @param dck : DCK desde UI
     * @param dcn : DCN desde UI
     * @return
     */
    public String calcularDP1(double dck, double dcn) {

        if (dck <= 0) {
            dck = DCK;
        }

        if (dcn <= 0) {
            dcn = DCN;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("DCK", dck);
        valores.put("DCN", dcn);
        valores.put("Q3C", Q3C);//En la fórmula es convertido a m³/s

        return CalcularGeneral("profundlaminaaguahmax", valores);
    }

    /**
     * Valcula Hmed para la profundidad de la lámina de agua (H) para Qmed
     *
     * @param dck : DCK desde UI
     * @param dcn : DCN desde UI
     * @return
     */
    public String calcularDP2(double dck, double dcn) {

        if (dck <= 0) {
            dck = DCK;
        }

        if (dcn <= 0) {
            dcn = DCN;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("DCK", dck);
        valores.put("DCN", dcn);
        valores.put("Q2C", Q2C);//En la fórmula es convertido a m³/s
        return CalcularGeneral("profundlaminaaguahmed", valores);
    }

    /**
     * Calcula Hmin para la profundidad de la lámina de agua (H) para Qmin
     *
     * @param dck : DCK desde UI
     * @param dcn : DCN desde UI
     * @return
     */
    public String calcularDP3(double dck, double dcn) {

        if (dck <= 0) {
            dck = DCK;
        }

        if (dcn <= 0) {
            dcn = DCN;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("DCK", dck);
        valores.put("DCN", dcn);
        valores.put("Q1C", Q1C);//En la fórmula es convertido a m³/s
        return CalcularGeneral("profundlaminaaguahmin", valores);
    }

    /**
     * Resalto (Z)
     *
     * @param dp3 : DP3 desde UI
     * @param dp1 : DP1 desde UI
     * @return
     */
    public String calcularDRZ(double dp3, double dp1) {

        if (dp3 <= 0) {
            dp3 = DP3;
        }

        if (dp1 <= 0) {
            dp1 = DP1;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("DP3", dp3);
        valores.put("DP1", dp1);
        valores.put("Q3C", Q3C);//En la fórmula es convertido a m³/s
        valores.put("Q1C", Q1C);//En la fórmula es convertido a m³/s

        String H = CalcularGeneral("resaltozdrz", valores);
        return H;
    }

    /**
     * Calcula Dimensiones del medidor Parshall . Recibe una cadena que contiene
     * los rangos de la tabla de ayuda para descomponer y obtener los numeros
     * relacionado al introducido en DAN. Ej: si introducen uno cercano a 7.6,
     * DPA= 46.6, DPB= 45.7...etc
     *
     * @return número relacionado al introducido en DAN
     */
    public String calcularDPABCDEFGKN(String listaTablaDck, double numeroDAN) {

        try {
            String[] nums = listaTablaDck.split("\\|");

            for (int i = 0; i < nums.length; i++) {
                String[] a = nums[i].split(";");

                if (Double.parseDouble(a[1]) == (numeroDAN)) { //Ojo esta en cm
                    return a[2] + ";" + a[3] + ";" + a[4] + ";" + a[5] + ";" + a[6] + ";" + a[7] + ";" + a[8] + ";" + a[9] + ";" + a[10];
                }
            }
        } catch (Exception ex) {
            return "";
        }
        return "";
    }

    /**
     * Calcula la altura máxima de lámina de agua en el desarenador (H)
     *
     * @param dp1 : DP1 desde UI
     * @param drz : DRZ desde UI
     * @return
     */
    public String calcularDAH(double dp1, double drz) {

        if (dp1 <= 0) {
            dp1 = DP1;
        }

        if (drz <= 0) {
            drz = DRZ;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("DP1", dp1);
        valores.put("DRZ", drz);

        return CalcularGeneral("alturamaxlaminaguadesarena", valores);
    }

    /**
     * Calcula Ancho del desarenador (b) /mts
     *
     * @param dah : DAH desde UI
     * @param dvf : DVF desde UI
     * @return
     */
    public String calcularDAB(double dah, double dvf) {

        if (dah <= 0) {
            dah = DAH;
        }

        if (dvf <= 0) {
            dvf = DVF;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("DAH", dah);
        valores.put("DVF", dvf);
        valores.put("Q3C", Q3C);//En la fórmula es convertido a m³/s

        return CalcularGeneral("anchodesarenadorb", valores);
    }

    /**
     * Calcula la Longitud del desarenador (L)
     *
     * @param dah : DAH desde UI
     * @return
     */
    public String calcularDLD(double dah) {

        if (dah <= 0) {
            dah = DAH;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("DAH", dah);

        return CalcularGeneral("longituddesarenadorl", valores);
    }

    /**
     * Calcula Área longitudinal del desarenador (A)
     *
     * @param dab : DAB desde UI
     * @param dld : DLD desde UI
     * @return
     */
    public String calcularDAL(double dab, double dld) {

        if (dab <= 0) {
            dab = DAB;
        }

        if (dld <= 0) {
            dld = DLD;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("DAB", dab);
        valores.put("DLD", dld);

        return CalcularGeneral("arealongitudinldesarenador", valores);
    }

    /**
     * Calcula Estimación de material retenido (q) m3/día
     */
    public String calcularDEM() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", Q2C);//En la fórmula es convertido a m³/s

        return CalcularGeneral("estimamaterialretenidoq", valores);
    }

    /**
     * Calcula Área longitudinal del desarenador (A)
     *
     * @param dem : DEM desde UI
     * @param dfl : DFL desde UI
     * @param dal : DAL desde UI
     * @return
     */
    public String calcularDUP(double dem, double dfl, double dal) {

        if (dem <= 0) {
            dem = DEM;
        }

        if (dfl <= 0) {
            dfl = DFL;
        }

        if (dal <= 0) {
            dal = DAL;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("DEM", dem);
        valores.put("DFL", dfl);
        valores.put("DAL", dal);

        String h = CalcularGeneral("profundutildepositinfarena", valores);
        return h;
    }

    /**
     * Cálculo de carga afluente media de DQO (Lo)
     *
     * @return
     */
    public double calcularUDQ() {

        double q2c = CalcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("CAQ", CAQ);
        valores.put("Q2C", q2c);

        return CalcularGeneral_("cargafluentemediadqo", valores);
    }

    /**
     * Calcula UTH ( Tiempo de retención hidráulico (TRHUASB)), Usa CAT la cual
     * se evalua en 'CalcularOtros' para que devuelva una cadena de rangos que
     * permitiran comparar uth
     *
     * ejemplo: se compara CAT = 25 contra los rangos de la formula
     * (10-12|15|17,8-9.9|18|21,....etc) en el metodo 'CalcularOtros_' luego se
     * traen en la cadena 'CalcularOtrosSt' los rangos (7-7.9) luego en ésta
     * función se compara 'uth' (uth=9) contra los rangos traidos ), luego se
     * devuelve falso ya que 9 no esta en el rango
     *
     * @param uth Valor de usuario o automático
     * @return
     */
    public boolean CalcularUTH(double uth) {

        try {
            CalcularOtrosSt = "";

            if (CalcularOtros_("tiemporetencionhidraulicouth", "UTH", CAT) == 0) { //si retorna 0, esta devolviendo la cadena 'CalcularOtrosSt'

                String[] rango = CalcularOtrosSt.trim().split("-");

                if (valida.EsDobleEntre(uth, Double.parseDouble(rango[0]), Double.parseDouble(rango[1]))) {
                    return true; //el número si se encuentra entre los rangos
                }
            }
        } catch (Exception ex) {
            log.error("Error en CalcularUTH " + ";" + ex.toString());
        }
        return false;
    }

    /**
     * Calcula la determinación del volumen total del reactor (V)
     *
     * @return
     */
    public double calcularUVR() {

        double q2c = CalcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("UTH", UTH);
        valores.put("Q2C", q2c);

        return CalcularGeneral_("determinacvoltotalreactor", valores);
    }

    public double calcularUNR() {

        return CalcularOtros_("numerounidadesreactores", "UNR", UVR);//Se compara UVR
    }

    /**
     * Calcula el volumen por unidad de reactor (Vr)
     *
     * @return
     */
    public double calcularUVU() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UVR", UVR);
        valores.put("UNR", (double) UNR);

        return CalcularGeneral_("volumenunidadreactor", valores);
    }

    /**
     * Calcula la Altura del reactor (H). Trae de la BD una formula que es una
     * taba separada por pipes y comas para compararla al valor ingresado por el
     * usuario y devolver a USH (Altura compartimiento de sedimentación (hs)) Y
     * UHD (Altura compartimiento de digestión (hd))
     *
     * @return
     */
    public String calcularUHR(double uhr) {

        try {
            String suhr = valida.DobleFormatoStringFloor(uhr, "#.#");//Se formatea para coincidir con los numeros de la tabla ej. 4.5623 --> 4.5
            ResultSet result = db.ResultadoSelect("obtenerecuaciongeneral", "alturareactorh", null); //Trae de la bd la ecuacion
            String[] ecuacion = result.getString("ecuacion").split(",");

            for (int i = 0; i < ecuacion.length; i++) {
                String[] formula = ecuacion[i].split("\\|");
                if (formula[0].equals(suhr)) {
                    return ecuacion[i];
                }
            }
        } catch (Exception ex) {
            log.error("Error calcularUHR:" + ex.toString());
        } finally {
            db.close(); //Se cierra la conexiòn
        }
        return "";
    }

    /**
     * Área por unidad de reactor (Ar) devuelve el numero redondeado .5 ó +1
     *
     * @return
     */
    public double calcularUAR() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UVU", UVU);
        valores.put("UHR", UHR);

        double x = CalcularGeneral_("areaunidadreactor", valores);
        return valida.DobleRedondeo05(x);
    }

    /**
     * Calcula el Ancho (ar) de Área por unidad de reactor (Ar)
     *
     * @return
     */
    public double calcularUAA() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UAR", UAR);

        return CalcularGeneral_("anchoaruaa", valores);
    }

    /**
     * Calcula la Longitud (Lr) de Área por unidad de reactor (Ar)
     *
     * @return
     */
    public double calcularUAL() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UAA", UAA);

        return CalcularGeneral_("longitudual", valores);
    }

    /**
     * Calcula Verificación del área total (AT) - UASB
     *
     * @return
     */
    public double calcularUAT() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UNR", (double) UNR);
        valores.put("UAR", UAR);

        return CalcularGeneral_("retenchidraulicareatotal", valores);
    }

    /**
     * Calcula Verificación del Volumen total (VT) - UASB
     *
     * @return
     */
    public double calcularUVT() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UAT", UAT);
        valores.put("UHR", UHR);

        return CalcularGeneral_("retenchidraulicvoltotal", valores);
    }

    /**
     * Calcula Verificación de cargas aplicadas, Carga hidráulica volumétrica
     * (CHV)
     *
     * @return
     */
    public double calcularURH() {

        double q2c = CalcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("UVT", UVT);

        return CalcularGeneral_("tiemporetenhidraulicuasb", valores);
    }

    /**
     * Calcula Verificación del Tiempo de retención hidráulico (TRHUASB)
     *
     * @return
     */
    public double calcularUCH() {

        double q2c = CalcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("UVT", UVT);

        return CalcularGeneral_("cargahidraulicvolumchv", valores);
    }

    /**
     * Calcula Verificación de Carga orgánica volumétrica (COV)
     *
     * @return
     */
    public double calcularUCO() {

        double q2c = CalcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("CAQ", CAQ);
        valores.put("UVT", UVT);

        return CalcularGeneral_("cargaorganicavolumuasb", valores);
    }

    /**
     * Cálculo de Velocidad ascensional Va para caudal medio
     *
     * @return
     */
    public double calcularUVN() {

        double q2c = CalcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("UAT", UAT);

        return CalcularGeneral_("velascensioncaudalmedio", valores);
    }

    /**
     * Cálculo de Velocidad ascensional Va para caudal máximo
     *
     * @return
     */
    public double calcularUVM() {

        double q3c = CalcularQ3Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q3C", q3c);
        valores.put("UAT", UAT);

        return CalcularGeneral_("velascensioncaudalmaximo", valores);
    }

    /**
     * Cálculo de Sistema de distribución del afluente, Número de tubos Nd
     *
     * @return
     */
    public double calcularUNT() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UAD", UAD);
        valores.put("UAT", UAT);

        return CalcularGeneral_("distribafluentnumtubos", valores);
    }

    /**
     * Cálculo de Eficiencia de remoción de DQO del sistema (EDQO)
     *
     * @return
     */
    public double calcularUER() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("URH", URH);

        return CalcularGeneral_("eficienciaremociondqouer", valores);
    }

    /**
     * Cálculo de Estimación de la concentración de DQO en el afluente (S)
     *
     * @return
     */
    public double calcularUEC() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UER", UER);
        valores.put("CAQ", CAQ);

        return CalcularGeneral_("estimconcentrcdqoafluente", valores);
    }

    /**
     * Cálculo de Compartimiento de sedimentación, Número de compartimientos ns
     *
     * @return
     */
    public double calcularUSC() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UAL", UAL);
        valores.put("USA", USA);

        double usc = CalcularGeneral_("sedimentnumcompartimients", valores);
        if (usc != 0 && usc < 1) {
            usc = 1;
        }
        return usc;
    }

    /**
     * Cálculo de Verificación de velocidad ascensional en el compartimiento de
     * sedimentación Vsed
     *
     * @return
     */
    public double calcularUVA() {

        double q2c = CalcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("USC", USC);
        valores.put("USA", USA);
        valores.put("UAL", UAL);

        return CalcularGeneral_("velascencompartimsediment", valores);
    }

    /**
     * Cálculo de Producción lodo esperada (Plodo) UASB
     *
     * @return
     */
    public double calcularUPL() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UCP", UCP);
        valores.put("UDQ", UDQ);

        return CalcularGeneral_("produccionlodoesperadaupl", valores);
    }

    /**
     * Cálculo de Volumen de lodo (Vlodo) UASB
     *
     * @return
     */
    public double calcularUVL() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UPL", UPL);

        return CalcularGeneral_("volumenlodouvl", valores);
    }

    /**
     * Cálculo de Carga de DQO convertida en metano (DQO CH4 )
     *
     * @return
     */
    public double calcularUDM() {

        double q2c = CalcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("UEC", UEC);
        valores.put("CAQ", CAQ);
        valores.put("USM", USM);

        return CalcularGeneral_("cargadqoconvertidametano", valores);
    }

    /**
     * Cálculo de Factor de corrección para la temperatura operacional del
     * reactor F(t)
     *
     * @return
     */
    public double calcularUCT() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("CAT", CAT);

        return CalcularGeneral_("factcorrecctempopreactor", valores);
    }

    /**
     * Cálculo de Producción volumétrica de metano QCH4
     *
     * @return
     */
    public double calcularUPM() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UDM", UDM);
        valores.put("UCT", UCT);

        return CalcularGeneral_("prodvolummetanoqch4", valores);
    }

    /**
     * Cálculo de Producción volumétrica de biogás Qbiogas
     *
     * @return
     */
    public double calcularUVB() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UPM", UPM);
        valores.put("UCM", UCM);

        return CalcularGeneral_("prodvolumetricaqbiogas", valores);
    }

    /**
     * Cálculo de Energía bruta producida (E) en kJ /d
     *
     * @return
     */
    public double calcularUEB() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UVB", UVB);
        valores.put("UCB", UCB);

        return CalcularGeneral_("energiabrutaproducida", valores);
    }

    /**
     * Cálculo de Energía bruta producida (E) UEB en kWh/d
     *
     * @return
     */
    public double calcularUEBK() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UEB", UEB);

        return CalcularGeneral_("energiabrutaproducidauebk", valores);
    }

    /**
     * Cálculo de Potencial de electricidad disponible Kw
     *
     * @return
     */
    public double calcularUPE() {

        double ueb = calcularUEBK();

        Map<String, Double> valores = new HashMap<>();
        valores.put("URE", URE);
        valores.put("UEB", ueb);

        return CalcularGeneral_("potencelectricdisponible", valores);
    }

    /**
     * Cálculo de Potencial de electricidad disponible en HP
     *
     * @return
     */
    public double calcularUPEK() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UPE", UPE);

        return CalcularGeneral_("potencelectricdisponiblek", valores);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Filtro Precolador
    /**
     * Filtro Precolador: Cálculo del volumen del medio de soporte (V)
     *
     * @return
     */
    public double calcularPVM() {

        double q2c = CalcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("PCO", PCO);
        valores.put("Q2C", q2c);
        valores.put("UEC", UEC);

        return CalcularGeneral_("volumenmediosoportepvm", valores);
    }

    /**
     * Filtro Precolador: Cálculo del área superficial (A)
     *
     * @return
     */
    public double calcularPAS() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("PVM", PVM);
        valores.put("PPM", PPM);

        return CalcularGeneral_("areasuperficialpas", valores);
    }

    /**
     * Filtro Precolador: Cálculo de Verificación de la tasa hidráulica
     * superficial (qs) Para Qmed
     *
     * @return
     */
    public double calcularPH2() {

        double q2c = CalcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("PAS", PAS);
        valores.put("Q2C", q2c);

        return CalcularGeneral_("tasahidraulicsuperfqsqmed", valores);
    }

    /**
     * Filtro Precolador: Cálculo de Verificación de la tasa hidráulica
     * superficial (qs) Para Qmed
     *
     * @return
     */
    public String calcularPH2C() {

        double ph2c = calcularPH2();
        CalcularOtrosSt = "";

        if (CalcularOtros_("tasahidraulicsuperfqsqmed2", "PH2C", ph2c) == 0) { //si retorna 0, esta devolviendo la cadena 'CalcularOtrosSt'
            return CalcularOtrosSt;
        }
        return "";
    }

    /**
     * Filtro Precolador: Cálculo de Verificación de la tasa hidráulica
     * superficial (qs) Para Qmax
     *
     * @return
     */
    public double calcularPH3() {

        double q3c = CalcularQ3Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("PAS", PAS);
        valores.put("Q3C", q3c);

        return CalcularGeneral_("tasahidraulicsuperfqsqmax", valores);
    }

    /**
     * Filtro Precolador: Cálculo de Área por filtro (a)
     *
     * @return
     */
    public double calcularPFA() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("PAS", PAS);
        valores.put("PFU", (double) PFU);

        return CalcularGeneral_("areaporfiltropfa", valores);
    }

    /**
     * Filtro Precolador: Cálculo de Diámetro por filtro (D)
     *
     * @return
     */
    public double calcularPFD() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("PFA", PFA);
        valores.put("pi", Math.PI);

        return CalcularGeneral_("diametroporfiltro", valores);
    }

    /**
     * Filtro Precolador: Cálculo de Concentración de DQO en el efluente final
     * del sistema de tratamiento Reactor UASB + Filtro percolador (Sfinal)
     *
     * @return
     */
    public double calcularPCD() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UEC", UEC);
        valores.put("PER", UEC);

        return CalcularGeneral_("concntdqoeflntfinuasbfiltr", valores);
    }

    /**
     * Filtro Precolador: Estimación de la producción de lodo (Plodo)
     *
     * @return
     */
    public double calcularPEL() {

        double q2c = CalcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("UEC", UEC);
        valores.put("PCD", PCD);

        return CalcularGeneral_("estimacionproducclodopel", valores);
    }

    /**
     * Filtro Precolador: Cálculo de la Producción volumétrica de lodo (Vlodo)
     *
     * @return
     */
    public double calcularPVL() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("PEL", PEL);

        return CalcularGeneral_("produccionvolumetricalodo", valores);
    }

    /**
     * Filtro Precolador: Cálculo de Área del sedimentador (As)
     *
     * @return
     */
    public double calcularPSA() {

        double q2c = CalcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("PSS", (double) PSS);

        return CalcularGeneral_("areasedimentadorpsa", valores);
    }

    /**
     * Filtro Precolador: Cálculo de Profundidad del sedimentador (Hsed)
     *
     * @return
     */
    public double calcularPPS() {

        return CalcularOtros_("profundidadsedimentador", "PPS", PSS);
    }

    /**
     * Filtro Precolador: Cálculo del Área por sedimentador (as)
     *
     * @return
     */
    public double calcularPAX() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("PSA", PSA);
        valores.put("PNU", (double) PNU);

        return CalcularGeneral_("areasedimentadorpax", valores);
    }

    /**
     * Filtro Precolador: Cálculo de Diámetro por sedimentador (D)
     *
     * @return
     */
    public double calcularPDX() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("PAX", PAX);
        valores.put("pi", Math.PI);

        return CalcularGeneral_("diametroporsedimentador", valores);
    }

    /**
     * Filtro Precolador: Estimación de área útil requerida para la tecnología
     * Reactor UASB + Filtro percolador
     *
     * @return
     */
    public double calcularPAT() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UAT", UAT);
        valores.put("PSA", PSA);
        valores.put("PAS", PAS);

        return CalcularGeneral_("areautilreqtecnouasbfiltr", valores);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Lodos Activados Convencional
    /**
     * Lodos Activados Convencional: Cálculo del área requerida (Asp)
     *
     * @return
     */
    public double calcularMAR() {

        double q2c = CalcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("MCH", MCH);

        return CalcularGeneral_("calculoarearequeridamar", valores);
    }

    /**
     * Lodos Activados Convencional: Verificación del tiempo de retención
     * hidráulico (TRHSP)
     *
     * @return
     */
    public double calcularMTR() {

        double q2c = CalcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("MPS", MPS);
        valores.put("MAR", MAR);

        return CalcularGeneral_("tiemporetenhidraulictrhsp", valores);
    }

    /**
     * Lodos Activados Convencional: Configuración geométrica del sedimentador
     * primario, Circular
     *
     * @return
     */
    public double calcularMGC() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("MAR", MAR);
        valores.put("pi", Math.PI);

        return CalcularGeneral_("configeomsedimpricircular", valores);
    }

    /**
     * Lodos Activados Convencional: Configuración geométrica del sedimentador
     * primario, Rectangular - Longitud
     *
     * @return
     */
    public double calcularMGL() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("MAR", MAR);
        valores.put("MGA", MGA);

        return CalcularGeneral_("configeomsedimprirectanlong", valores);
    }

    /**
     * Lodos Activados Convencional: Cálculo del coeficiente de producción
     * celular ajustado por la pérdida por respiración endógena (Yobs)
     *
     * @return
     */
    public double calcularMCE() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("MCC", MCC);
        valores.put("MEL", MEL);
        valores.put("MTE", MTE);

        return CalcularGeneral_("coeficiprodcelulrespirendog", valores);
    }

    /**
     * Lodos Activados Convencional: Cálculo de la DBO soluble en el efluente y
     * la eficiencia de remoción de DBO5: DBO particulada (Sp)
     *
     * @return
     */
    public double calcularMDP() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("MSE", MSE);
        valores.put("MRD", MRD);

        return CalcularGeneral_("eficienremocdbo5particulada", valores);
    }

    /**
     * Lodos Activados Convencional: Cálculo de la DBO soluble en el efluente y
     * la eficiencia de remoción de DBO5: DBO soluble (Se)
     *
     * @return
     */
    public double calcularMDS() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("MDB", MDB);
        valores.put("MDP", MDP);

        return CalcularGeneral_("dbosolublefluentese", valores);
    }

    /**
     * Lodos Activados Convencional: Cálculo de la eficiencia de remoción de DBO
     * soluble
     *
     * @return
     */
    public double calcularMES() {

        double cabq = CAB != 0 ? CAB : CAQ; // Por regla si no existe CAB se usa CAQ

        Map<String, Double> valores = new HashMap<>();
        valores.put("MDS", MDS);
        valores.put("CAB", cabq);

        return CalcularGeneral_("eficienciaremocdbosoluble", valores);
    }

    /**
     * Lodos Activados Convencional: Cálculo de la eficiencia global de remoción
     * de DBO
     *
     * @return
     */
    public double calcularMED() {

        double cabq = CAB != 0 ? CAB : CAQ;

        Map<String, Double> valores = new HashMap<>();
        valores.put("MDB", MDB);
        valores.put("CAB", cabq);

        return CalcularGeneral_("eficienciaglobalremocdbo", valores);
    }

    /**
     * Lodos Activados Convencional: Cálculo de producción de lodo esperada - ∆X
     *
     * @return
     */
    public double calcularMPL() {

        double q2c = CalcularQ2Cm3Dia();

        double cabq = CAB != 0 ? CAB : CAQ;

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("CAB", cabq);
        valores.put("MDS", MDS);
        valores.put("MCE", MCE);

        return CalcularGeneral_("produccionlodoesperadax", valores);
    }

    /**
     * Lodos Activados Convencional: Cálculo de producción de lodo esperada -
     * ∆XT
     *
     * @return
     */
    public double calcularMLT() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("MPL", MPL);
        valores.put("CVS", CVS);

        return CalcularGeneral_("producclodoesperadaxtmlt", valores);
    }

    /**
     * Lodos Activados Convencional: Cálculo del volumen de lodo purgado por día
     * (Vlodo)
     *
     * @return
     */
    public double calcularMVL() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("MLT", MLT);

        return CalcularGeneral_("volumenlodopurgadodia", valores);
    }

    /**
     * Lodos Activados Convencional: Cálculo del volumen de lodo del tanque de
     * aireación (VTA)
     *
     * @return
     */
    public double calcularMVT() {

        double q2c = CalcularQ2Cm3Dia();

        double cabq = CAB != 0 ? CAB : CAQ;

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("CAB", cabq);
        valores.put("MCC", MCC);
        valores.put("MDS", MDS);
        valores.put("MEL", MEL);
        valores.put("MTE", MTE);
        valores.put("MST", MST);
        valores.put("CVS", CVS);

        return CalcularGeneral_("vollodotanqueaireacionvta", valores);
    }

    /**
     * Lodos Activados Convencional: Cálculo del tiempo retención hidráulico
     * (TRH)
     *
     * @return
     */
    public double calcularMVH() {

        double q2c = CalcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("MVT", MVT);
        valores.put("Q2C", q2c);

        return CalcularGeneral_("tiempretenchidraulicmvh", valores);
    }

    /**
     * Lodos Activados Convencional: Cálculo del Área superficial (ATA)
     *
     * @return
     */
    public double calcularMAS() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("MVT", MVT);
        valores.put("MPT", MPT);

        return CalcularGeneral_("areasuperficialmas", valores);
    }

    /**
     * Lodos Activados Convencional: Cálculo de la relación
     * Alimento/Microorganismos (A/M)
     *
     * @return
     */
    public double calcularMAM() {

        double q2c = CalcularQ2Cm3Dia();

        double cabq = CAB != 0 ? CAB : CAQ;

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("CAB", cabq);
        valores.put("MST", MST);
        valores.put("MVT", MVT);
        valores.put("MDS", MDS);
        valores.put("CVS", CVS);

        return CalcularGeneral_("relacnalimentmicrorganism", valores);
    }

    /**
     * Lodos Activados Convencional: Cálculo de la recirculación mínima
     * recomendada (r)
     *
     * @return
     */
    public double calcularMRM() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("MST", MST);
        valores.put("MSR", MSR);

        return CalcularGeneral_("recirculacminrecomendada", valores);
    }

    /**
     * Lodos Activados Convencional: Verificación de la producción de lodo ΔX a
     * partir del volumen del tanque de aireación, edad del lodo y concentración
     * de SSTA
     *
     * @return
     */
    public double calcularMEC() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("MST", MST);
        valores.put("MVT", MVT);
        valores.put("MEL", MEL);

        return CalcularGeneral_("prodlodoxvoltanqaireacssta", valores);
    }

    /**
     * Lodos Activados Convencional: Cálculo del Área de sedimentación (As)
     *
     * @return
     */
    public double calcularMSA() {

        double q2c = CalcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("MFS", MFS);

        return CalcularGeneral_("areasedimentacionmsa", valores);
    }
    
            /**
     *  Lodos Activados Convencional: Cálculo de Masa de sólidos (M)
     *
     * @return
     */
    public double calcularMMS() {

        double q2c = CalcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("MRM", MRM);
        valores.put("MST", MST);

        return CalcularGeneral_("tasaaplicacionsolidosmms", valores);
    }

            /**
     *  Lodos Activados Convencional: Cálculo de Área total requerida por la tecnología
     *
     * @return
     */
    public double calcularMAT() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("MAR", MAR); 
        valores.put("MAS", MAS);
        valores.put("MSA", MSA);

        return CalcularGeneral_("areatotalrequeridamat", valores);
    }
    
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    /**
     * Recibe los parametros necesarios para traer de la BD una ecuacion y con
     * los parametros que llegan (que son las variables necesarias) evaluar la
     * ecuacion y dar un resultado devuelto en cadena
     *
     * @param ecuacion Nombre de la formula en la BD
     * @param valores Nombres y valores usados (clave - valor)
     * @return
     */
    public String CalcularGeneral(String ecuacion, Map<String, Double> valores) {

        try {
            int i = 0;
            String[] nombres = new String[valores.size()];

            for (String key : valores.keySet()) {
                nombres[i] = key; //Entrega los nombres de las variables
                i++;
            }

            ResultSet result = db.ResultadoSelect("obtenerecuaciongeneral", ecuacion, null); //Trae de la bd la ecuacion
            ecuacion = result.getString("ecuacion");//Se reusa var ecuacion para almacenar el resultado

            Expression exp = new ExpressionBuilder(ecuacion)
                    .variables(nombres)
                    .build()
                    .setVariables(valores);

            ecuacion = valida.DobleAcadena(exp.evaluate());
            valores.clear();
            return ecuacion;
        } catch (Exception ex) {
            log.error("Error en CalcularGeneral(),ecuación:" + ecuacion + ";" + ex.toString());
            return null;
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }

    /**
     * *
     * Recibe los parametros necesarios para traer de la BD una ecuacion, la
     * cual desgloza para evaluarla segun el parametro de entrada x, segun la
     * evaluación puede pasar a evaluar la formula obtenida o devolver
     * inmediatamente el resultado como cadena (ver ejemplo ej.)
     *
     * @param ecuacion : Nombre de la ecuacion en la bd
     * @param nombre : el nombre de la variable en la misma tabla de ecuaciones
     * @param x : valor a evaluar
     * @return
     */
    public String CalcularOtros(String ecuacion, String nombre, double x) {

        try {
            ResultSet result = db.ResultadoSelect("obtenerecuacion", ecuacion, nombre);
            ecuacion = "";
            String[] variables = result.getString("ecuacion").split(",");//Separa la ecuacion en subecuaciones por comas y las pasa a array ej. (50+C|0|10,20-C|1|30) --> ARRAY=(50+C|0|10)(20-C|1|30)
            for (int i = 0; i < variables.length; i++) {
                String nivel[] = variables[i].split("\\|"); //Separa la subecuaciones por pipe y se valida: x entre 0 y 10 , valida: x entre 1 y 30 , Si la primera validacion es true: ecuacion = 50+C

                if (valida.EsDobleEntre(valida.DobleAcadena(x), Double.parseDouble(nivel[1].trim()), Double.parseDouble(nivel[2].trim()))) { //Se evalua si el número de entrada está entre los rangos de la fórmula
                    ecuacion = nivel[0];
                    break;
                }
            }

            if (ecuacion.trim().contains("x")) { //La ecuacion contiene x, entonces se evalua, de lo contrario se devuelve resultado tal cual

                Expression e = new ExpressionBuilder(ecuacion) //ej. ecuacion = 50+C --> 50 + x
                        .variables("x")
                        .build()
                        .setVariable("x", x);

                ecuacion = valida.DobleAcadena(e.evaluate());
            }

            return ecuacion;
        } catch (Exception ex) {
            log.error("Error en CalcularOtros(),ecuacion:" + ecuacion + ";x: " + x + "; " + ex.toString());
            return null;
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }

    /**
     * Recibe los parametros necesarios para traer de la BD una ecuacion y con
     * los parametros que llegan (que son las variables necesarias) evaluar la
     * ecuacion y dar un resultado devuelto en cadena
     *
     * @param ecuacion Nombre de la formula en la BD
     * @param valores Nombres y valores usados (clave - valor)
     * @return
     */
    public double CalcularGeneral_(String ecuacion, Map<String, Double> valores) {

        try {
            int i = 0;
            String[] nombres = new String[valores.size()];
            double ec;
            for (String key : valores.keySet()) {
                nombres[i] = key; //Entrega los nombres de las variables
                i++;
            }

            ResultSet result = db.ResultadoSelect("obtenerecuaciongeneral", ecuacion, null); //Trae de la bd la ecuacion
            ecuacion = result.getString("ecuacion");//Se reusa var ecuacion para almacenar el resultado

            Expression exp = new ExpressionBuilder(ecuacion)
                    .variables(nombres)
                    .build()
                    .setVariables(valores);

            ec = exp.evaluate();
            valores.clear();
            return ec;
        } catch (ArithmeticException esx) {
            //log.error("Error en CalcularGeneral(),ecuación:" + ecuacion + ";" + esx.toString());
            return 0;

        } catch (Exception ex) {
            log.error("Error en CalcularGeneral(),ecuación:" + ecuacion + ";" + ex.toString());
            return 0;
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }
    //
    public String CalcularOtrosSt;

    /**
     * *
     * Recibe el nombre de la ecuacion o fórmula como parámetro, luego la
     * desgloza para separar términos y de lo obtenido devolver un número, si el
     * resultado no es un número y contiene 'x' se evalua de nuevo como fórmula;
     * pero si no contiene 'x' se pasa a una cadena global que puede ser usada
     * como resultado para hacer otras cosas. (ver * ejemplo ej.)
     *
     * @param ecuacion : Nombre de la ecuacion en la bd
     * @param nombre : el nombre de la variable en la misma tabla de ecuaciones
     * @param x : valor a evaluar
     * @return : devuleve cero si esta usando 'CalcularOtrosSt'
     */
    public double CalcularOtros_(String ecuacion, String nombre, double x) {

        try {
            ResultSet result = db.ResultadoSelect("obtenerecuacion", ecuacion, nombre);
            ecuacion = "";
            String[] variables = result.getString("ecuacion").split(",");//Separa la ecuacion en subecuaciones por comas y las pasa a array ej. (50+C|0|10,20-C|1|30) --> ARRAY=(50+C|0|10)(20-C|1|30)
            double ec = 0;

            for (int i = 0; i < variables.length; i++) {
                String nivel[] = variables[i].split("\\|"); //Separa la subecuaciones por pipe y se valida: x entre 0 y 10 , valida: x entre 1 y 30 , Si la primera validacion es true: ecuacion = 50+C

                if (valida.EsDobleEntre(valida.DobleAcadena(x), Double.parseDouble(nivel[1].trim()), Double.parseDouble(nivel[2].trim()))) { //Se evalua si el número de entrada está entre los rangos de la fórmula

                    double c = valida.EsNumero(nivel[0]);
                    if (c == 0.0) {
                        ecuacion = nivel[0]; // se detecta una cadena se debe proseguir mas abajo con x
                    } else {
                        return c; //Se obtuvo un resultado inmediato, salir
                    }
                    break;
                }
            }

            if (ecuacion.trim().contains("x")) { //La ecuacion contiene x, entonces se evalua, de lo contrario se devuelve resultado tal cual

                Expression e = new ExpressionBuilder(ecuacion) //ej. ecuacion = 50+C --> 50 + x
                        .variables("x")
                        .build()
                        .setVariable("x", x);

                ec = e.evaluate();
                CalcularOtrosSt = "";
            } else { //No hay x, el resultado es una cadena, se retorna cero y se debe usar la cadena CalcularOtrosSt
                CalcularOtrosSt = ecuacion;
                return 0;
            }
            return ec;
        } catch (Exception ex) {
            log.error("Error en CalcularOtros(),ecuacion:" + ecuacion + ";x: " + x + "; " + ex.toString());
            return 0;
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }

    public void setNullsDesarenador() {
        DAN = 0;
        DCK = 0;
        DCN = 0;
        DP1 = 0;
        DP2 = 0;
        DP3 = 0;
        DRZ = 0;
        DPA = 0;
        DPB = 0;
        DPC = 0;
        DPD = 0;
        DPE = 0;
        DPF = 0;
        DPG = 0;
        DPK = 0;
        DPN = 0;
        DAH = 0;
        DAB = 0;
        DVF = 0;
        DLD = 0;
        DAL = 0;
        DEM = 0;
        DFL = 0;
        DUP = 0;
    }
}
