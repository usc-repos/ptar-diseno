package BO;

import Componentes.Configuracion;
import Componentes.Util;
import Componentes.Validaciones;
import DB.Dao;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class Bod implements Cloneable {

    private Field field;
    private Util util = new Util();
    public String Generalpath = "";//path de recursos del proyecto
    static Logger log = Logger.getLogger("BOD");
    private Validaciones valida = new Validaciones();
    private Configuracion conf = new Configuracion(); 
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));
    //-------------------------------------------------------------------------------------------------------------------- Datos de Entrada
    private int TFA; // INT, min 1950 max 2030 , Fecha 
    private double TML; // #,## 1, min 5 max 50 , Temperatura media de la localidad Cº 
    private int TCA; // INT, min 1 max 199000 , Población del censo más antiguo (Pci) hab. 
    private int TCR; // INT, min 1 max 199000 , Población del censo más reciente (Puc) hab. 
    private int TCI; // INT, min 1950 max 2030 , Año del censo (Tci) 
    private int TUC; // INT, min 1950 max 2030 , Año del censo (Tuc) 
    private int TDI; // INT, min 10 max 40 , Período de diseño años 
    private double TRE; // #,## 2, min 0.7 max 0.9 , Coeficiente de retorno (R) 
    private int TDP; // INT, min 1 max 400 , Dotación máxima de agua potable (D) L/hab*día
    //--------------------------------------------------------------------------------------------------------------------Proyeccion Poblacional 
    private int PAG; // INT, min max , Método Aritmético  
    private double PPT; // min 0.01 max 100000 , Tasa de crecimiento poblacional (r)  
    private int PPF; // INT, min 2 max 200000 , Población proyectada o futura (Pf ) hab  
    private String PCR; // min max , Nivel de complejidad según el RAS 
    //-------------------------------------------------------------------------------------------------------------------- Cálculo de caudales de diseño 
    private double CAR; // #,## 2, min 0,01 max 296 , Contribución de aguas residuales (C) L/s 
    private double KAR; // #,## 2, min 0.5 max 25500 , m³/día 
    private int QIF; // INT, min max , Extensión de la red de alcantarillado
    private double QEL; // #,## 2, min 0.1 max 500 , Longitud (L) Km <HTML><b>Extensión de la red de alcantarillado</b></HTML>
    private double QET; // #,## 2, min 0.01 max 1 , Tasa de infiltración (INF) L/s*km 
    private double QEC; // #,## 2, min 0.1 max 500 , Qinf L/s Caudal de infiltración (Qinf) método de acuerdo por longitud 1
    private double QEK; // #,## 2, min 0.01 max 35000 , m³/día Caudal de infiltración (Qinf) método de acuerdo por longitud 2
    private double QAA; // #,## 2, min 0.001 max 50000 , Área (A) ha <HTML><b>Extensión de la red de alcantarillado</b></HTML>
    private double QAI; // #,## 2, min 0.1 max 0.4 , Aporte por infiltración (INF) 
    private double QAC; // #,## 2, min 0.0001 max 20000 , Qinf L/s Caudal de infiltración (Qinf) acuerdo por área 1
    private double QAK; // #,## 2, min 0.00864 max 1750000 , m³/día Caudal de infiltración (Qinf) acuerdo por área 2
    private int QCD; // INT, min max , Si No 
    private double QCA; // #,## 2, min 0.001 max 50000 , Área (A) ha 
    private double QCM; // #,## 2, min 0.1 max 0.2 , Aporte máximo (CE) L/s*ha 
    private double QCC; // #,## 2, min 0.0001 max 10000 , Qce l/s Caudal por conexiones erradas(Qce) 1 (Área servida)
    private double QCK; // #,## 2, min 0.001 max 900000 , m³/día Caudal por conexiones erradas(Qce) 2 (Área servida)
    private int IOA; // INT, min max , Industrial <HTML><b>Otros aportes (industrial, comercial o institucional)</b></HTML>
    private int IRM; // INT, min max , Si No <HTML><b>¿Cuenta con registros de medición de caudales de origen industrial?</b></HTML>
    private double IAI; // INT, min 0 max 50000 , Área industrial ha 
    private double IPI; // #,## 2, min 0.4 max 1.5 , Aporte industrial L/s*ha 
    private double IQI; // #,## 2, min 0 max 75000 , Caudal industrial (Qi) l/s Caudal industrial (Qi) 1
    private double IKI; // #,## 2, min 0 max 6480000 , m³/d Caudal industrial (Qi) 2
    private double IQ2; // #,## 2, min 0 max 2000 , Caudal medio (QImed) l/s Caudal medio (QImed) 1
    private double IK2; // #,## 2, min 0 max 180000000 , m³/d Caudal medio (QImed) 2    
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
    private int FAP; // INT, min 2 max 3 , Pendiente n ⊿ 
    private double FAI; // #,## 1, min 18 max 27 , Ángulo de inclinación de la pendiente <HTML>&#945;</HTML> 
    private double FAB; // #,## 1, min 0.5 max 10 , Borde libre m 
    private double FLA; // #,### 1, min 0.1 max 20000000 , Área total de las lagunas Anaerobia + Facultativa m²
    //-------------------------------------------------------------------------------------------------------------------- Rejillas
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
    //-------------------------------------------------------------------------------------------------------------------- canaleta Parshall
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
    private double URV; // #,## 2, min 500 max 2000 // Volumen asumido por unidad de reactor (V) m³ 
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
    private double URQ; // #,## 1, min 60 max 80 // Eficiencia de  remoción de DBO5 del sistema (EDBO)  % 
    private double UEC; // #,## 1, min 2 max 200 // Estimación de la concentración de DQO en el efluente (S) mg/L 
    private double UEQ; // #,## 1, min 2 max 200 // Estimación de la concentración de DBO en el efluente (S) mg/L
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
    private double PEQ; // #,## 1, min 20 max 100,  Eficiencia de remoción de DBO del filtro percolador (EFP)
    private double PCD; // #,## 2, min 160 max 0.001,  Concentración de DQO en el efluente final del sistema de tratamiento Reactor UASB + Filtro precolador (Sfinal)
    private double PCB; // #,## 2, min 160 max 0.001,  Concentración de DBO en el efluente final del sistema de tratamiento Reactor UASB + Filtro precolador (Sfinal)
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
    private double MWT; // #,## 2, min 0.1 max 1000 , Ancho del tanque de aireación m
    private double MHT; // #,## 2, min 0.1 max 1000 , Longitud del tanque de aireación m
    private double MAM; // #,## 3, min 9800000 max 334000000 , Cálculo de la relación Alimento/Microorganismos (A/M) kgDBO / kgSSVTA 
    private double MRM; // #,## 2, min 0.1 max 0.6 , Cálculo de la recirculación mínima recomendada (r) 
    private double MEC; // #,## 1, min 0.08 max 1100000 , , Verificación de la producción de lodo ΔX a partir del V TA , edad del lodo y concentración de SSTA,  kg ST/d 
    private double MFS; // #,## 1, min 15 max 29 , Selección de la tasa de flujo superficial TFS m3/m2*d SEDIMENTADOR SECUNDARIO
    private double MSA; // #,## 1, min 0.06 max 3000000 , Área de sedimentación (As) m² 
    private double MMS; // #,## 1, min 1.4 max 550000000 , Masa de sólidos (M) KgSS/día , Verificación de la tasa de aplicación de sólidos
    private double MHS; // #,## 2, min 3.70 max 10 , Profundidad de sedimentación (Hss) m 
    private double MSD; // #,## 2, min 0.1 max 10000 , Diámetro de sedimentación secundaria m
    private double MAT; // #,## 2, min 0.14 max 6500000 , Área total requerida por la tecnología m² 
    //-------------------------------------------------------------------------------------------------------------------- Lodos Activados Modalidad Aireacion Extendida
    private double EDB; // #,## 1, min 0 max 70 , , Demanda Bioquímica de Oxígeno deseada en el efluente (DBOe),  mg/L TANQUE DE AIREACIÓN
    private double ESE; // #,## 1, min 0 max 70 , , Sólidos Suspendidos Totales deseado en efluente (Xe),  mg/L 
    private double ERD; // #,## 1, min 0.1 max 1.1 , Relación gDBO/gSST en el efluente 
    private double EST; // #,## 1, min 1500 max 4000 , , Sólidos suspendidos totales en el lodo (licor mixto) del tanque de aireación – SSTA (Xa),  mg/L 
    private double ESR; // #,## 1, min 7000 max 15000 , Sólidos del lodo recirculado (Xu) mg/L 
    private double ECC; // #,## 2, min 0.1 max 1 , Coeficiente de producción celular (Y) mgSSV/mgDBO 
    private double ETE; // #,## 3, min 0 max 0.3 , Tasa específica de respiración endógena (Kd) días-¹ 
    private double EEL; // #,## 1, min 3 max 15 , Edad del lodo (θc) días 
    private double ECE; // #,## 3, min 0.01 max 2 , , Cálculo del coeficiente de producción celular ajustado por la pérdida por respiración endógena (Yobs),  mgSSV/mgDBO 
    private double EDP; // #,## 2, min 0 max 77 , DBO particulada (Sp) mg/L Cálculo de la DBO soluble en el efluente y la eficiencia de remoción de DBO5
    private double EDS; // #,## 1, min 0 max 523 , DBO soluble (Se) mg/L 
    private double EES; // #,## 1, min 13 max 100 , Eficiencia de remoción de DBO soluble % 
    private double EED; // #,## 1, min 89 max 100 , Eficiencia global de remoción de DBO % 
    private double EPL; // #,## 1, min 0 max 13300000 , Cálculo de producción de lodo esperada - ∆X kg SV/d 
    private double ELT; // #,## 1, min 0 max 13300000 , , Cálculo de producción de lodo esperada - ∆X T ,  kg ST/d 
    private double EVL; // #,## 2, min 0 max 16600000 , Cálculo del volumen de lodo purgado por día (Vlodo) m³/d 
    private double EVT; // #,## 1, min 0.16 max 4100000 , VTA m³ Cálculo del volumen de lodo del tanque de aireación (VTA) y tiempo retención hidráulico (TRH)
    private double EVH; // #,## 1, min 1.15 max 4.4 , TRH horas 
    private double EPT; // #,## 2, min 2.00 max 7.00 , Profundidad del tanque de aireación (HTA) m Cálculo del área superficial del tanque de aireación (ATA)
    private double EAS; // #,## 1, min 0.08 max 590000 , Área superficial (ATA) m² 
    private double EWT; // #,## 2, min 0.1 max 1000 , Ancho del tanque de aireación m
    private double EHT; // #,## 2, min 0.1 max 1000 , Longitud del tanque de aireación m
    private double EAM; // #,## 3, min 9800000 max 334000000 , Cálculo de la relación Alimento/Microorganismos (A/M) kgDBO / kgSSVTA 
    private double ERM; // #,## 2, min 0.1 max 0.6 , Cálculo de la recirculación mínima recomendada (r) 
    private double EEC; // #,## 1, min 0.08 max 1100000 , , Verificación de la producción de lodo ΔX a partir del V TA , edad del lodo y concentración de SSTA,  kg ST/d 
    private double EFS; // #,## 1, min 15 max 29 , Selección de la tasa de flujo superficial TFS m3/m2*d SEDIMENTADOR SECUNDARIO
    private double ESA; // #,## 1, min 0.06 max 3000000 , Área de sedimentación (As) m² 
    private double EMS; // #,## 1, min 1.4 max 550000000 , Masa de sólidos (M) KgSS/día , Verificación de la tasa de aplicación de sólidos
    private double EHS; // #,## 2, min 3.70 max 10 , Profundidad de sedimentación (Hss) m 
    private double ESD; // #,## 2, min 0.1 max 10000 , Diámetro de sedimentación secundaria m
    private double EAT; // #,## 2, min 0.14 max 6500000 , Área total requerida por la tecnología m² 
    //-------------------------------------------------------------------------------------------------------------------- Lodos Activados Convencional UASB
    private double NDB; // #,## 1, min 0 max 70 , , Demanda Bioquímica de Oxígeno deseada en el efluente (DBOe),  mg/L TANQUE DE AIREACIÓN
    private double NSE; // #,## 1, min 0 max 70 , , Sólidos Suspendidos Totales deseado en efluente (Xe),  mg/L 
    private double NRD; // #,## 1, min 0.1 max 1.1 , Relación gDBO/gSST en el efluente 
    private double NST; // #,## 1, min 1500 max 4000 , , Sólidos suspendidos totales en el lodo (licor mixto) del tanque de aireación – SSTA (Xa),  mg/L 
    private double NSR; // #,## 1, min 7000 max 15000 , Sólidos del lodo recirculado (Xu) mg/L 
    private double NCC; // #,## 2, min 0.1 max 1 , Coeficiente de producción celular (Y) mgSSV/mgDBO 
    private double NTE; // #,## 3, min 0 max 0.3 , Tasa específica de respiración endógena (Kd) días-¹ 
    private double NEL; // #,## 1, min 3 max 15 , Edad del lodo (θc) días 
    private double NCE; // #,## 3, min 0.01 max 2 , , Cálculo del coeficiente de producción celular ajustado por la pérdida por respiración endógena (Yobs),  mgSSV/mgDBO 
    private double NDP; // #,## 2, min 0 max 77 , DBO particulada (Sp) mg/L Cálculo de la DBO soluble en el efluente y la eficiencia de remoción de DBO5
    private double NDS; // #,## 1, min 0 max 523 , DBO soluble (Se) mg/L 
    private double NES; // #,## 1, min 13 max 100 , Eficiencia de remoción de DBO soluble % 
    private double NED; // #,## 1, min 89 max 100 , Eficiencia global de remoción de DBO % 
    private double NPL; // #,## 1, min 0 max 13300000 , Cálculo de producción de lodo esperada - ∆X kg SV/d 
    private double NLT; // #,## 1, min 0 max 13300000 , , Cálculo de producción de lodo esperada - ∆X T ,  kg ST/d 
    private double NVL; // #,## 2, min 0 max 16600000 , Cálculo del volumen de lodo purgado por día (Vlodo) m³/d 
    private double NVT; // #,## 1, min 0.16 max 4100000 , VTA m³ Cálculo del volumen de lodo del tanque de aireación (VTA) y tiempo retención hidráulico (TRH)
    private double NVH; // #,## 1, min 1.15 max 4.4 , TRH horas 
    private double NPT; // #,## 2, min 2.00 max 7.00 , Profundidad del tanque de aireación (HTA) m Cálculo del área superficial del tanque de aireación (ATA)
    private double NAS; // #,## 1, min 0.08 max 590000 , Área superficial (ATA) m² 
    private double NWT; // #,## 2, min 0.1 max 1000 , Ancho del tanque de aireación m
    private double NHT; // #,## 2, min 0.1 max 1000 , Longitud del tanque de aireación m
    private double NAM; // #,## 3, min 9800000 max 334000000 , Cálculo de la relación Alimento/Microorganismos (A/M) kgDBO / kgSSVTA 
    private double NRM; // #,## 2, min 0.1 max 0.6 , Cálculo de la recirculación mínima recomendada (r) 
    private double NEC; // #,## 1, min 0.08 max 1100000 , , Verificación de la producción de lodo ΔX a partir del V TA , edad del lodo y concentración de SSTA,  kg ST/d 
    private double NFS; // #,## 1, min 15 max 29 , Selección de la tasa de flujo superficial TFS m3/m2*d SEDIMENTADOR SECUNDARIO
    private double NSA; // #,## 1, min 0.06 max 3000000 , Área de sedimentación (As) m² 
    private double NMS; // #,## 1, min 1.4 max 550000000 , Masa de sólidos (M) KgSS/día , Verificación de la tasa de aplicación de sólidos
    private double NHS; // #,## 2, min 3.70 max 10 , Profundidad de sedimentación (Hss) m 
    private double NSD; // #,## 2, min 0.1 max 10000 , Diámetro de sedimentación m 
    private double NAT; // #,## 2, min 0.14 max 6500000 , Área total requerida por la tecnología m² 
    //-------------------------------------------------------------------------------------------------------------------- Laguna Facultativa Secundaria UASB
    private double GTE; // #,## 1, min 0.1 max 100 , Tasa de evaporación (e) mm/Día 
    private double GCO; // #,## 1, min 0.1 max 10000000 , Carga Orgánica Volumétrica (COVLF) g/m³*Día 
    private double GCA; // #,## 1, min 1 max 2 , Altura Útil (hLF) m Cálculo de Área Superficial
    private double GAS; // #,## 1, min 0.1 max 10000000 , Área Superficial (ASLF) m² 
    private double GTR; // #,## 1, min 0.5 max 10 , Tiempo de retención hidráulico (TRLF) días 
    private double GED; // #,## 1, min 1 max 100 , Eficiencia de DBO5 estimada %LF 
    private double GUV; // #,## 1, min 0.1 max 10000000 , Volumen Útil (VLA) m³ 
    private double GSA; // #,## 1, min 0.1 max 10000000 , Ancho (aLF) m Relación Largo: Ancho
    private double GSL; // #,## 1, min 0.1 max 10000000 , Largo (LLF) m 
    private int GAP; // INT, min 2 max 3 , Pendiente n ⊿ 
    private double GAI; // #,## 1, min 18 max 27 , Ángulo de inclinación de la pendiente <HTML>&#945;</HTML> 
    private double GAB; // #,## 1, min 0.5 max 10 , Borde libre m 
    private double GUA; // #,### 1, min 0.1 max 20000000 , Área total del reactor UASB + Laguna Facultativa m²
    //--------------------------------------------------------------------------------------------------------------------  
    public boolean EditDatosDeEntrada = false; //Bandera True si La seccion de datos de entrada ha sido llenada con éxito. Igualmente si se carga desde bd 
    public boolean EditProyeccionPoblacional = false;
    public boolean EditCalculoCaudalesDiseno = false;
    public boolean EditCaractInicAguaResidual = false;
    public boolean EditRejillas = false;
    public boolean EditDesarenador = false;
    public boolean EditLagunaAnaerobia = false;
    public boolean EditLagunaFacultativaSec = false;
    public boolean EditLagunaFacultativaUASB = false;
    public boolean EditReactorUasb = false;
    public boolean EditFiltroPercolador = false;
    public boolean EditLodosActivadsConvenc = false;
    public boolean EditLodosActivadsConvencUASB = false;
    public boolean EditLodosActivadsAireacExt = false;
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
    public boolean WinLagunaFacultativaUASB = false;
    public boolean WinReactorUasb = false;
    public boolean WinFiltroPercolador = false;
    public boolean WinLodosActivadsConvenc = false;
    public boolean WinLodosActivadsConvencUASB = false;
    public boolean WinLodosActivadsAireacExt = false;
    public boolean WinResultado = false;
    //-----------------------------------------------------------
    public String pathProyecto; //ruta completa del proyecto cargado

    public Bod(String path) {
        Generalpath = path;
        pathProyecto = "";
        PCR = "";
    }

    @Override
    public Object clone() {
        Object obj = null;
        try {
            obj = super.clone();
        } catch (CloneNotSupportedException ex) {
            log.error("Error clone() " + ex.getMessage() + " " + ex.getCause());
        }
        return obj;
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
            log.error("Error setVarInt " + ex.getMessage() + " " + ex.getCause());
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
            log.error("Error setVarInt. " + ex.getMessage() + " " + ex.getCause());
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
            log.error("Error getVarInt. " + ex.getMessage() + " " + ex.getCause());
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
            log.error("Error setVarDob " + nombre + " " + ex.getMessage() + " " + ex.getCause());
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
            log.error("Error setVarDob. " + ex.getMessage() + " " + ex.getCause());
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
            double c = field.getDouble(this);
            return c;
        } catch (Exception ex) {
            log.error("Error getVarDob. " + ex.getMessage() + " " + ex.getCause());
        }
        return 0;
    }

    /**
     * Método que procede a asignar valor String a la variable por su 'nombre'
     * semejante en ésta clase
     *
     * @param var String con el valor de texto
     * @param nombre Nombre de la variable en ésta clase
     * @return True: si la conversion y asignación fueron posibles
     */
    public boolean setVarStr(String var, String nombre) {

        try {
            field = getClass().getDeclaredField(nombre);
            field.set(this, var);
            return true;
        } catch (Exception ex) {
            log.error("Error setVarStr. " + ex.getMessage() + " " + ex.getCause());
        }
        return false;
    }

    /**
     * Método que procede a devolver un String por su 'nombre' semejante en ésta
     * clase
     *
     * @param nombre Nombre de la variable en ésta clase
     * @return True: si la conversion y asignación fueron posibles
     */
    public String getVarStr(String nombre) {

        try {
            field = getClass().getDeclaredField(nombre);
            return field.get(this).toString();
        } catch (Exception ex) {
            log.error("Error getVarStr. " + ex.getMessage() + " " + ex.getCause());
        }
        return "";
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

    //--------------------------------------------------------------------------------------- 
    public boolean compruebaTucTci() { //Tuc debe ser mayor que Tci
        if (TUC > TCI && TUC < TFA) {
            return true;
        }
        return false;
    }

    public boolean VentanasAbiertas() {

        if (WinDatosDeEntrada) {
            return true;
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
        if (WinLagunaFacultativaUASB) {
            return true;
        }
        if (WinReactorUasb) {
            return true;
        }
        if (WinFiltroPercolador) {
            return true;
        }
        if (WinLodosActivadsConvenc) {
            return true;
        }
        if (WinLodosActivadsConvencUASB) {
            return true;
        }
        if (WinLodosActivadsAireacExt) {
            return true;
        }
        if (WinResultado) {
            return true;
        }
        return false;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Proyección poblacional 
    /**
     * *
     * Proyección poblacional: Calcula la Tasa de crecimiento poblacional (r)
     * por el método Aritmético
     *
     * @return
     */
    public double calcularPPT_A() {

        Map<String, Double> valores = new HashMap<>();//Diccionario clave/valor
        valores.put("TCR", (double) TCR);
        valores.put("TCA", (double) TCA);
        valores.put("TUC", (double) TUC);
        valores.put("TCI", (double) TCI);

        return CalcularGeneral_("metodoaritmeticotca", valores);
    }

    /**
     * *
     * Proyección poblacional: Calcula la Tasa de crecimiento poblacional (r)
     * por el método Geométrico
     *
     * @return
     */
    public double calcularPPT_G() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("TCR", (double) TCR);
        valores.put("TCA", (double) TCA);
        valores.put("TUC", (double) TUC);
        valores.put("TCI", (double) TCI);

        return CalcularGeneral_("metodogeometricotcg", valores);
    }

    public double calcularPPF_A() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("TCR", (double) TCR);
        valores.put("TCA", (double) TCA);
        valores.put("TUC", (double) TUC);
        valores.put("TCI", (double) TCI);
        valores.put("TDI", (double) TDI);
        valores.put("TF", (double) TFA);

        return CalcularGeneral_("metodogeometricoppa", valores);
    }

    public double calcularPPF_G() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("TCR", (double) TCR);
        valores.put("TCA", (double) TCA);
        valores.put("TUC", (double) TUC);
        valores.put("TCI", (double) TCI);
        valores.put("TDI", (double) TDI);
        valores.put("TF", (double) TFA);

        return CalcularGeneral_("metodogeometricoppg", valores);
    }

    public String calcularPCR() {

        if (CalcularOtros_("nivelcomplejidadras", "PCR", PPF) == 0) {
            return CalcularOtrosSt;
        }
        return "";
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Cálculo de caudales de diseño
    /**
     * Caudales de diseño: Calcula el Caudal de aguas residuales domésticas
     * (QARD)
     *
     * @return
     */
    public double calcularCAR() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("TRE", TRE);
        valores.put("TDP", (double) TDP);
        valores.put("pf", (double) PPF);

        return CalcularGeneral_("contribucionaguasresidualesls", valores);
    }

    public double calcularKAR() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("TRE", TRE);
        valores.put("TDP", (double) TDP);
        valores.put("pf", (double) PPF);

        return CalcularGeneral_("contribucionaguasresidualesm3", valores);
    }

    /**
     * Caudales de diseño: Calcula Extensión de la red de alcantarillado Qinf
     * L/S
     *
     * @return
     */
    public double calcularQEC() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("QEL", QEL);
        valores.put("QET", QET);

        return CalcularGeneral_("caudalinfiltracionqec", valores);
    }

    /**
     * Caudales de diseño: Calcula Extensión de la red de alcantarillado Qinf
     * m³/d
     *
     * @return
     */
    public double calcularQEK() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("QEL", QEL);
        valores.put("QET", QET);

        return CalcularGeneral_("caudalinfiltracionqek", valores);
    }

    /**
     * Caudales de diseño: Calcula el Área del alcantarillado influenciada por
     * infiltración Qinf L/S
     *
     * @return
     */
    public double calcularQAC() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("QAA", QAA);
        valores.put("QAI", QAI);

        return CalcularGeneral_("caudalinfiltracionqac", valores);
    }

    /**
     * Caudales de diseño: Calcula el Área del alcantarillado influenciada por
     * infiltración Qinf m³/d
     *
     * @return
     */
    public double calcularQAK() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("QAA", QAA);
        valores.put("QAI", QAI);

        return CalcularGeneral_("caudalinfiltracionqak", valores);
    }

    /**
     * Caudales de diseño: Calcula el Caudal por conexiones erradas (QCE) L/S
     *
     * @return
     */
    public double calcularQCC() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("QCA", QCA);
        valores.put("QCM", QCM);

        return CalcularGeneral_("caudalconexioneserradasqcc", valores);
    }

    /**
     * Caudales de diseño: Calcula el Caudal por conexiones erradas (QCE) m³/d
     *
     * @return
     */
    public double calcularQCK() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("QCA", QCA);
        valores.put("QCM", QCM);

        return CalcularGeneral_("caudalconexioneserradasqck", valores);
    }

    /**
     * Caudales de diseño: Calcula el Caudal industrial (QI) en L/s
     *
     * @return
     */
    public double calcularIQI() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("IAI", IAI);
        valores.put("IPI", IPI);

        return CalcularGeneral_("otrosaportesindustrialiqi", valores);
    }

    /**
     * Caudales de diseño: Calcula el Caudal industrial (QI) en m³/d
     *
     * @return
     */
    public double calcularIKI() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("IAI", IAI);
        valores.put("IPI", IPI);

        return CalcularGeneral_("otrosaportesindustrialiki", valores);
    }

    /**
     * Caudales de diseño: Calcula el Caudal Industrial medio (QImed) en L/s
     *
     * @return
     */
    public double calcularIK2() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("IQ2", IQ2);

        return CalcularGeneral_("otrosaportesindustrialik2", valores);
    }

    /**
     * Caudales de diseño: Calcula el Caudal comercial (Qc) en L/s
     *
     * @return
     */
    public double calcularCQC() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("CAC", CAC);
        valores.put("CPC", CPC);

        return CalcularGeneral_("otrosaportescomercialcqc", valores);
    }

    /**
     * Caudales de diseño: Calcula el Caudal comercial (Qc) en m³/d
     *
     * @return
     */
    public double calcularCKC() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("CAC", CAC);
        valores.put("CPC", CPC);

        return CalcularGeneral_("otrosaportescomercialckc", valores);
    }

    /**
     * Caudales de diseño: Calcula el Caudal institucional (QIN) en L/s
     *
     * @return
     */
    public double calcularYQI() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("YAI", YAI);
        valores.put("YPI", YPI);

        return CalcularGeneral_("otrosaportescomercialyqi", valores);
    }

    /**
     * Caudales de diseño: Calcula el Caudal institucional (QIN) en m³/d
     *
     * @return
     */
    public double calcularYKI() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("YAI", YAI);
        valores.put("YPI", YPI);

        return CalcularGeneral_("otrosaportescomercialyki", valores);
    }

    /**
     * Caudales de diseño: Q2C Caudal medio diario en
     *
     * @return
     */
    public double calcularQ2C() {

        double Qinf = 0;
        double Qce = 0;
        double Qi = 0;
        double Qc = 0;
        double Qin = 0;

        if (QIF == 0) {
            Qinf = QEC;
        }

        if (QIF == 1) {
            Qinf = QAC;
        }

        if (QCD == 1) {
            Qce = QCC;
        }

        if (IOA > 0) {
            if (IRM == 0) {
                Qi = IQI;
            }

            if (IRM == 1) {
                Qi = IQ2;
            }
        }

        if (COA > 0) {
            Qc = CQC;
        }

        if (YOA > 0) {
            Qin = YQI;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("CAR", CAR);
        valores.put("Qinf", Qinf);
        valores.put("Qce", Qce);
        valores.put("Qi", Qi);
        valores.put("Qc", Qc);
        valores.put("Qin", Qin);

        return CalcularGeneral_("caudalmediodiarioq2c", valores);
    }

    /**
     * Caudales de diseño:Calcula el Caudal medio diario en m³/día
     *
     * @return Q2C en m³/día
     */
    public double calcularQ2Cm3Dia() { //final

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", Q2C);
        return CalcularGeneral_("caudalmediodiarioq2ck", valores);
    }

    /**
     * *
     * Caudales de diseño: Calcula el Coeficiente de mayoración o variación
     * horaria (K)
     *
     * @return
     */
    public double calcularQ3M() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("pf", (double) PPF);

        return CalcularGeneral_("caudalmaximodiarioq3m", valores);
    }

    /**
     * Caudales de diseño: Calcula el Caudal máximo diario en L/s
     *
     * @return
     */
    public double calcularQ3C() {

        double Qinf = 0;
        double Qce = 0;
        double Qi = 0;
        double Qc = 0;
        double Qin = 0;

        if (QIF == 0) {
            Qinf = QEC;
        }

        if (QIF == 1) {
            Qinf = QAC;
        }

        if (QCD == 1) {
            Qce = QCC;
        }

        if (IOA > 0) {
            if (IRM == 0) {
                Qi = IQI;
            }

            if (IRM == 1) {
                Qi = IQ2;
            }
        }

        if (COA > 0) {
            Qc = CQC;
        }
        if (YOA > 0) {
            Qin = YQI;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("CAR", CAR);
        valores.put("Qinf", Qinf);
        valores.put("Qce", Qce);
        valores.put("Qi", Qi);
        valores.put("Qc", Qc);
        valores.put("Qin", Qin);
        valores.put("K", Q3M);

        return CalcularGeneral_("caudalmaximodiarioq3c", valores);
    }

    /**
     * Caudales de diseño: Calcula Q3C Caudal medio diario en m³/día
     *
     * @return Q3C en m³/día
     */
    public double calcularQ3Cm3Dia() { //usar este

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q3C", Q3C);
        return CalcularGeneral_("caudalmaximodiarioq3kc", valores);
    }

    /**
     * Caudales de diseño: Calcula el Caudal mínimo diario en L/s
     *
     * @return
     */
    public double calcularQ1C() {

        double Qinf = 0;
        double Qi = 0;
        double Qc = 0;
        double Qin = 0;
        double Qce = 0;

        if (QIF == 0) {
            Qinf = QEC;
        }

        if (QIF == 1) {
            Qinf = QAC;
        }

        if (QCD == 1) {
            Qce = QCC;
        }


        if (IOA > 0) {
            if (IRM == 0) {
                Qi = IQI;
            }

            if (IRM == 1) {
                Qi = IQ2;
            }
        }

        if (COA > 0) {
            Qc = CQC;
        }
        if (YOA > 0) {
            Qin = YQI;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("C", CAR);
        valores.put("Qinf", Qinf);
        valores.put("Qi", Qi);
        valores.put("Qc", Qc);
        valores.put("Qce", Qce);
        valores.put("Qin", Qin);
        valores.put("K3", Q1H);

        return CalcularGeneral_("caudalminimodiarioq1c", valores);
    }

    /**
     * Caudales de diseño: Calcula el Caudal mínimo diario en m³/día
     *
     * @return
     */
    public double calcularQ1Cm3Dia() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q1C", Q1C);

        return CalcularGeneral_("caudalminimodiarioq1k", valores);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Laguna Anaerobia
    /**
     * Laguna Anaerobia: (Carga Orgánica Volumétrica)
     *
     * @return
     */
    public double calcularLCO() {
        return CalcularOtros_("cargaorganicavolumetrica", "LCO", CAT);
    }

    /**
     * Laguna Anaerobia: Calcula la (Eficiencia de DBO5 estimada)
     *
     * @return
     */
    public double calcularLCE() {
        return CalcularOtros_("eficienciadbo5estimada", "LCE", CAT);
    }

    /**
     * Laguna Anaerobia: Calcula el (DBO5 en el efluente de la laguna Anaerobia)
     *
     * @return
     */
    public double calcularLAE() {

        double cabq = CAB != 0 ? CAB : CAQ; // Por regla si no existe CAB se usa CAQ

        Map<String, Double> valores = new HashMap<>();
        valores.put("CAB", cabq);
        valores.put("LCE", LCE);

        return CalcularGeneral_("dbo5efluentelagunanaerobia", valores);
    }

    /**
     * Laguna Anaerobia: Calcula el (Volumen Útil)
     *
     * @return
     */
    public double calcularLVU() {

        double q2c = calcularQ2Cm3Dia();
        double cabq = CAB != 0 ? CAB : CAQ;

        Map<String, Double> valores = new HashMap<>();
        valores.put("CAB", cabq);
        valores.put("Q2C", q2c);
        valores.put("LCO", LCO);

        return CalcularGeneral_("volumenutil", valores);
    }

    /**
     * Laguna Anaerobia: (Tiempo de retención hidráulico)
     *
     * @return
     */
    public double calcularLTH() { //Todo: estas funciones deben devolver dobles no strings

        double q2c = calcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("LVU", LVU);

        double x = CalcularGeneral_("tiemporetencionhidraulico", valores);

        if (x < 1) { //Regla: Si tiempo de retención hidráulico menor a (1) día, se ajustará dicho valor
            return 1;
        }
        return x;
    }

    /**
     * Laguna Anaerobia: (Volumen Útil Recalculado)
     *
     * @return
     */
    public double calcularLVR() {

        double q2c = calcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("LTH", LTH);

        return CalcularGeneral_("volumenutilrecalculado", valores);
    }

    /**
     * Laguna Anaerobia: (Área Superficial)
     *
     * @return
     */
    public double calcularLAS() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("LVR", LVR);
        valores.put("LAU", LAU);

        return CalcularGeneral_("areasuperficial", valores);
    }

    /**
     * Laguna Anaerobia: (Ancho (aLA)) para Relación Largo: Ancho 1:3,
     *
     * @return
     */
    public double calcularLAA_1_3() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAS", LAS);

        return CalcularGeneral_("ancho1_3", valores);
    }

    /**
     * Laguna Anaerobia: (Ancho (aLA)) para Relación Largo: Ancho 2:3,
     *
     * @return
     */
    public double calcularLAA_2_3() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAS", LAS);

        return CalcularGeneral_("ancho2_3", valores);
    }

    /**
     * Laguna Anaerobia: (Largo (l LA)) para Relación Largo: Ancho 1:3,
     *
     * @return
     */
    public double calcularLAL_1_3() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAA", LAA);

        return CalcularGeneral_("largo1_3", valores);
    }

    /**
     * Laguna Anaerobia: (Largo (l LA)) para Relación Largo: Ancho 2:3,
     *
     * @return
     */
    public double calcularLAL_2_3() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAA", LAA);

        return CalcularGeneral_("largo2_3", valores);
    }

    /**
     * Calcula LAI (Ángulo de inclinación de la pendiente)
     *
     * @return
     */
    public double calcularLAI() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAP", (double) LAP);
        valores.put("pi", Math.PI);

        return CalcularGeneral_("angulopendientelai", valores);
    }

    /**
     * Calcula LAB (Borde Libre)
     *
     * @return
     */
    public double calcularLAB() {

        return CalcularOtros_("bordelibrelab", "LAB", LAS);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Laguna Facultativa Secundaria: 
    /**
     * *
     * Laguna Facultativa Secundaria: (Carga Orgánica Volumétrica COVLF)
     *
     * @return
     */
    public double calcularFCO() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("CAT", CAT);

        return CalcularGeneral_("cargaorganicavolumetricalf", valores);
    }

    /**
     * *
     * Laguna Facultativa Secundaria: (Área Superficial (ASLF))
     *
     * @return
     */
    public double calcularFAS() {

        double q2c = calcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAE", LAE);
        valores.put("Q2C", q2c);
        valores.put("FCO", FCO);

        return CalcularGeneral_("areasuperficiallf", valores);
    }

    /**
     * *
     * Laguna Facultativa Secundaria: (Tiempo de retención hidráulico(TRLF))
     *
     * @return
     */
    public double calcularFTR() {

        double q2c = calcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("FAS", FAS);
        valores.put("FCA", FCA);
        valores.put("FTE", FTE);
        valores.put("Q2C", q2c);

        double x = CalcularGeneral_("tiemporetencionhidraulicolf", valores);

        if (x < 4) { //REGLA: el sistema debe redondear los valores que sean TRLF<4 días a 4 días. 
            return 4;
        }
        return x;
    }

    /**
     * *
     * Laguna Facultativa Secundaria: (Eficiencia de DBO5 estimada LF)
     *
     * @return
     */
    public double calcularFED() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAE", LAE);
        valores.put("CAT", CAT);
        valores.put("FTR", FTR);

        return CalcularGeneral_("eficienciadbo5estimadalf", valores);
    }

    /**
     * *
     * Laguna Facultativa Secundaria: (Volumen Útil)
     *
     * @return
     */
    public double calcularFUV() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("FCA", FCA);
        valores.put("FAS", FAS);

        return CalcularGeneral_("volumenutillf", valores);
    }

    /**
     * *
     * Laguna Facultativa Secundaria: (Relación Ancho LF)
     *
     * @return
     */
    public double calcularFSA() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("FAS", FAS);

        return CalcularGeneral_("relacionancholf", valores);
    }

    /**
     * *
     * Laguna Facultativa Secundaria: (Relación Largo LF)
     *
     * @return
     */
    public double calcularFSL() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("FSA", FSA);

        return CalcularGeneral_("relacionlargolf", valores);
    }

    /**
     * *
     * Laguna Facultativa Secundaria: Ángulo de inclinación de la pendiente)
     *
     * @return
     */
    public double calcularFAI() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("FAP", (double) FAP);
        valores.put("pi", Math.PI);

        return CalcularGeneral_("angulopendientefai", valores);
    }

    /**
     * *
     * Laguna Facultativa Secundaria: (Borde Libre)
     *
     * @return
     */
    public String calcularFAB() {

        CalcularOtrosSt = "";

        double n = CalcularOtros_("bordelibrefab", "FAB", FAS);
        if (n == 0) { //si retorna 0, esta devolviendo la cadena 'CalcularOtrosSt'
            return CalcularOtrosSt;
        }
        return n + "";
    }

    /**
     * *
     * Laguna Facultativa Secundaria: Sumatoria de áreas totales de las Lagunas)
     *
     * @return
     */
    public double calcularFLA() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAS", LAS);
        valores.put("FAS", FAS);

        return CalcularGeneral_("sumaareastotalagunas", valores);
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Laguna Facultativa Secundaria + UASB: 

    /**
     * *
     * Laguna Facultativa Secundaria + UASB: (Carga Orgánica Volumétrica COVLF),
     * Puede ser llamado desde el UI con parámetro
     *
     * @return
     */
    public double calcularGCO() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("CAT", CAT);

        return CalcularGeneral_("cargaorganicavolumetricalg", valores);
    }

    /**
     * *
     * Laguna Facultativa Secundaria + UASB: (Área Superficial (ASLF)), Puede
     * ser llamado desde el UI con parámetro
     *
     * @return
     */
    public double calcularGAS() {

        double q2c = calcularQ2Cm3Dia();
        double uecq = CAB != 0 ? UEQ : UEC;//por regla si no existe DBO de UASB se usa DQO de UASB

        Map<String, Double> valores = new HashMap<>();
        valores.put("UEC", uecq);
        valores.put("Q2C", q2c);
        valores.put("GCO", GCO);

        return CalcularGeneral_("areasuperficiallg", valores);
    }

    /**
     * *
     * Laguna Facultativa Secundaria + UASB: (Tiempo de retención
     * hidráulico(TRLF)), Puede ser llamado desde el UI con parámetro
     *
     * @return
     */
    public double calcularGTR() {

        double q2c = calcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("GAS", GAS);
        valores.put("GCA", GCA);
        valores.put("GTE", GTE);
        valores.put("Q2C", q2c);

        double x = CalcularGeneral_("tiemporetencionhidraulicolg", valores);

        if (x < 4) { //REGLA: el sistema debe redondear los valores que sean TRLF<4 días a 4 días. 
            return 4;
        }
        return x;
    }

    /**
     * *
     * Laguna Facultativa Secundaria + UASB: (Eficiencia de DBO5 estimada LF),
     * Puede ser llamado desde el UI con parámetro
     *
     * @return
     */
    public double calcularGED() {

        double uecq = CAB != 0 ? UEQ : UEC;

        Map<String, Double> valores = new HashMap<>();
        valores.put("UEC", uecq);
        valores.put("CAT", CAT);
        valores.put("GTR", GTR);

        return CalcularGeneral_("eficienciadbo5estimadalg", valores);
    }

    /**
     * *
     * Laguna Facultativa Secundaria + UASB: (Volumen Útil), Puede ser llamado
     * desde el UI con parámetro
     *
     * @return
     */
    public double calcularGUV() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("GCA", GCA);
        valores.put("GAS", GAS);

        return CalcularGeneral_("volumenutillg", valores);
    }

    /**
     * *
     * Laguna Facultativa Secundaria + UASB: (Relación Ancho LF), Puede ser
     * llamado desde el UI con parámetro
     *
     * @return
     */
    public double calcularGSA() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("GAS", GAS);

        return CalcularGeneral_("relacionancholg", valores);
    }

    /**
     * *
     * Laguna Facultativa Secundaria + UASB: (Relación Largo LF), Puede ser
     * llamado desde el UI con parámetro
     *
     * @return
     */
    public double calcularGSL() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("GSA", GSA);

        return CalcularGeneral_("relacionlargolg", valores);
    }

    /**
     * *
     * Laguna Facultativa Secundaria + UASB: Ángulo de inclinación de la
     * pendiente), Puede ser llamado desde el UI con parámetro
     *
     * @return
     */
    public double calcularGAI() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("GAP", (double) GAP);
        valores.put("pi", Math.PI);

        return CalcularGeneral_("angulopendientefag", valores);
    }

    /**
     * *
     * Laguna Facultativa Secundaria + UASB: (Borde Libre), Puede ser llamado
     * desde el UI con parámetro
     *
     * @return
     */
    public String calcularGAB() {

        CalcularOtrosSt = "";

        double n = CalcularOtros_("bordelibrefag", "GAB", GAS);
        if (n == 0) { //si retorna 0, esta devolviendo la cadena 'CalcularOtrosSt'
            return CalcularOtrosSt;
        }
        return n + "";
    }

    /**
     * *
     * Laguna Facultativa Secundaria + UASB: Área total del reactor UASB +
     * Laguna Facultativa
     *
     * @return
     */
    public double calcularGUA() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("GAS", GAS);
        valores.put("UAT", UAT);

        return CalcularGeneral_("sumaareastotaluasblagunafac", valores);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Anexos Lagunas: Anaerobia o Facultativa o Facultativa UASB
    /**
     * Lagunas (Anareobia o Facultativa) : Calcula Ancho fondo laf
     */
    public double calcularLAF(double laa, double lap, double lau) {

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAA", laa);
        valores.put("LAP", lap);
        valores.put("LAU", lau);

        return CalcularGeneral_("anchofondo", valores);
    }

    /**
     * *
     * Lagunas (Anareobia o Facultativa) : Calcula Largo fondo LLF
     */
    public double calcularLLF(double lal, double lap, double lau) {

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAL", lal);
        valores.put("LAP", lap);
        valores.put("LAU", lau);

        return CalcularGeneral_("largofondo", valores);
    }

    /**
     * *
     * Lagunas (Anareobia o Facultativa) : Calcula Ancho Total LAT
     *
     * @return
     */
    public double calcularLAT(double laa, double lap, double lau, double lab) {

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAA", laa);
        valores.put("LAP", lap);
        valores.put("LAU", lau);
        valores.put("LAB", lab);

        return CalcularGeneral_("anchototal", valores);
    }

    /**
     * *
     * Lagunas (Anareobia o Facultativa) : Calcula Largo Total LLT
     *
     * @return
     */
    public double calcularLLT(double lal, double lap, double lau, double lab) {

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAL", lal);
        valores.put("LAP", lap);
        valores.put("LAU", lau);
        valores.put("LAB", lab);

        return CalcularGeneral_("largototal", valores);
    }

    /**
     * Lagunas (Anareobia o Facultativa) : Calcula Lado (Y), Distancia
     *
     * @return
     */
    public double calcularLYX(double lap, double lau, double lab) {

        Map<String, Double> valores = new HashMap<>();
        valores.put("LAP", lap);
        valores.put("LAU", lau);
        valores.put("LAB", lab);

        return CalcularGeneral_("ladoyx", valores);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    /**
     * Calcula Q1C Caudal min diario en m(cúbicos)/dìa m3/día
     *
     * @return Q1C en m3/día
     */
    public double CalcularQ1Cm3Dia() { //Todo: Nuevo: Remplazar los otros por este, mas adelante cambiar nombres en la fórmula (Q2C)  //Nuevo usar este

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", Q1C);
        return CalcularGeneral_("caudalmediodiarioq2ck", valores);
    }

    /**
     * *
     * Calcula Q1C en metros cúbicos/seg.
     *
     * @return
     */
    public double calcularQ1Cm3Seg() { //usar este

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q1C", Q1C);
        return CalcularGeneral_("caudalminimodiariom3", valores);
    }

    /**
     * *
     * Calcula Q2C en metros cúbicos/seg.
     *
     * @return
     */
    public double calcularQ2Cm3Seg() { //usar este

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", (double) Q2C);

        return CalcularGeneral_("caudalmediodiariom3", valores);
    }

    /**
     * *
     * Calcula Q3C en metros cúbicos/seg.
     *
     * @return
     */
    public double calcularQ3Cm3Seg() { //usar este

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q3C", Q3C);
        return CalcularGeneral_("caudalmaximodiariom3", valores);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
//    /**
//     * *
//     * Calcula Q3C en metros cúbicos
//     *
//     * @return
//     */
//    public String calcularQ3C_m3() {
//
//        Map<String, Double> valores = new HashMap<>();
//        valores.put("Q3C", (double) Q3C);
//        return CalcularGeneral("caudalmaximodiariom3", valores);
//    }
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

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Rejillas
    /**
     * *
     * Rejillas: Calcula RAU Área útil entre las barras para el escurrimiento
     * (Au)
     */
    public double calcularRAU() {

        double q3c = calcularQ3Cm3Seg();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q3C", q3c); //En la formula Q3C se divide /mil
        valores.put("RPM", RPM);
        return CalcularGeneral_("areautilentrbarsescurrim", valores);
    }

    /**
     * Rejillas: Calcula RER Eficiencia de las rejillas en función del espesor
     * (t) y el espaciamiento entre barras (a)
     */
    public double calcularRER() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("REB", REB);
        valores.put("REL", (double) REL);
        return CalcularGeneral_("eficrejilfuncespsor", valores);
    }

    /**
     * Rejillas: Calcula RAT Área total de las rejillas (incluidas las barras)
     * (S)
     *
     * @return
     */
    public double calcularRAT() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("RAU", RAU);
        valores.put("RER", RER);
        return CalcularGeneral_("areatotrejillybarrs", valores);
    }

    /**
     * Rejillas: Calcula RAR Ancho de rejillas (b)
     *
     * @return
     */
    public double calcularRAR() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("RAT", RAT);
        valores.put("RPL", RPL);
        return CalcularGeneral_("anchorejillasb", valores);
    }

    /**
     * Rejillas: Calcula RCP Velocidad máxima (Vmax)
     *
     * @return
     */
    public double calcularRCP() {

        double q3c = calcularQ3Cm3Seg();

        Map<String, Double> valores = new HashMap<>();
        valores.put("RAR", RAR);
        valores.put("RPL", RPL);
        valores.put("RER", RER);
        valores.put("Q3C", q3c);
        return CalcularGeneral_("velocidadmaximarcp", valores);
    }

    /**
     * Rejillas: Calcula RCM Velocidad media (Vmed)
     *
     * @return
     */
    public double calcularRCM() {

        double q2c = calcularQ2Cm3Seg();

        Map<String, Double> valores = new HashMap<>();
        valores.put("RAR", RAR);
        valores.put("RPL", RPL);
        valores.put("RER", RER);
        valores.put("Q2C", q2c);
        return CalcularGeneral_("velocidadmediarcm", valores);
    }

    /**
     * Rejillas: Calcula RLC Longitud del canal (L)
     *
     * @return
     */
    public double calcularRLC() {

        double q3c = calcularQ3Cm3Seg();

        Map<String, Double> valores = new HashMap<>();
        valores.put("RAT", RAT);
        valores.put("Q3C", q3c);
        return CalcularGeneral_("longitudcanalrlc", valores);
    }

    /**
     * Rejillas: Calcula RVM Velocidad de aproximación Vo para el Caudal Máximo
     *
     * @return
     */
    public double calcularRVM() {

        double q3c = calcularQ3Cm3Seg();

        Map<String, Double> valores = new HashMap<>();
        valores.put("RAR", RAR);
        valores.put("RPL", RPL);
        valores.put("Q3C", q3c);
        return CalcularGeneral_("velaproxvocaudalmax", valores);
    }

    /**
     * Rejillas: Calcula RVN Velocidad de aproximación Vo para el caudal medio
     *
     * @return
     */
    public double calcularRVN() {

        double q2c = calcularQ2Cm3Seg();

        Map<String, Double> valores = new HashMap<>();
        valores.put("RAR", RAR);
        valores.put("RPL", RPL);
        valores.put("Q2C", q2c);
        return CalcularGeneral_("velaproxvocaudalmed", valores);
    }

    /**
     * Rejillas: Calcula RNB Número de barras (Nb) en la rejilla
     *
     * @return
     */
    public int calcularRNB() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("RAR", RAR);
        valores.put("REL", (double) REL);
        valores.put("REB", REB);
        
        return (int) Math.ceil(CalcularGeneral_("numerobarrasenrejilla", valores));//Se debe devolver redondeado por arriba
    }

    /**
     * Rejillas: Calcula RNE Número de espaciamientos (Ne) en la rejilla
     *
     * @return
     */
    public double calcularRNE() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("RNB", (double) RNB);
        return CalcularGeneral_("numeroespaciamrejilla", valores);
    }

    /**
     * Rejillas: Calcula Pérdida de carga en la rejilla limpia (hL)
     *
     * @return
     */
    public double calcularRPC() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("RCP", RCP);
        valores.put("RVM", RVM);

        return CalcularGeneral_("perdidcargrejillimpi", valores);
    }

    /**
     * Rejillas: Calcula Pérdida de carga en la rejilla 50% sucia (hL)
     *
     * @return
     */
    public double calcularRPS() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("RCP", RCP);
        valores.put("RVM", RVM);
        return CalcularGeneral_("perdidcargrejil50limp", valores);
    }

    /**
     * Rejillas: Calcula RPH Pérdida de carga calculada por la ecuación de
     * Kirshmer
     *
     * @return
     */
    public double calcularRPH() {

        double rib = Math.toRadians(RIB);

        Map<String, Double> valores = new HashMap<>();
        valores.put("RPB", RPB);
        valores.put("REB", REB);
        valores.put("REL", (double) REL);
        valores.put("RIB", rib);
        valores.put("RCP", RCP);

        return CalcularGeneral_("perdcargcalculeckirshmer", valores);
    }

    /**
     * Rejillas: (Dato complementario), calcula Hmax
     *
     * @return
     */
    public double calcularRHM() {


        Map<String, Double> valores = new HashMap<>();
        valores.put("RPS", RPS);
        valores.put("DP1", DP1);

        return CalcularGeneral_("hmaxrejilla", valores);
    }

    /**
     * Rejillas: (Dato complementario), calcula el lado x
     *
     * @return
     */
    public double calcularRXM() {

        double rhm = calcularRHM();
        Map<String, Double> valores = new HashMap<>();
        valores.put("RHM", rhm);
        valores.put("RIB", Math.toRadians(RIB));

        return CalcularGeneral_("medidaxrejilla", valores);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Canaleta Parshall 
    /**
     * Canaleta Parshall: Calcula número sugerido para Ancho nominal (W). Esta
     * funcion no esta en la base de datos, sin estandarizar! para ver mas
     * (buscar excel)
     *
     * @return:
     */
    public double calcularDAN() {

        return CalcularOtros_("dimensioncanaletaparshall", "DAN", Q3C); //Q3C en L/S
    }

    /**
     * Canaleta Parshall: Calcula Coeficiente K , para la Profundidad de la
     * lámina de agua (H) para Qmax, Qmed y Qmin. Recibe una cadena que contiene
     * los rangos de la tabla de ayuda para descomponerlos y obtener 'K' Y 'n'
     * al comparar el valor de DAN
     *
     * @return número relacionado al introducido en DAN
     */
    public String calcularDCNDCK() {

        try {
            ResultSet result = db.ResultadoSelect("obtenerecuaciongeneral", "profundidadlaminaguadcnk", null); //Trae de la bd la ecuacion
            String[] ecuacion = result.getString("ecuacion").split(",");

            for (int i = 0; i < ecuacion.length; i++) {
                String[] formula = ecuacion[i].split("\\|");
                if (formula[0].equals(DAN + "")) {
                    return ecuacion[i];
                }
            }
        } catch (Exception ex) {
            log.error("Error calcularDCNDCK:" + ex.toString());
        } finally {
            db.close();
        }
        return "";
    }

    /**
     * Canaleta Parshall: Calcula Hmax para la profundidad de la lámina de agua
     * (H) para Qmax
     *
     * @return
     */
    public double calcularDP1() {

        double q3c = calcularQ3Cm3Seg();

        Map<String, Double> valores = new HashMap<>();
        valores.put("DCK", DCK);
        valores.put("DCN", DCN);
        valores.put("Q3C", q3c);

        return CalcularGeneral_("profundlaminaaguahmax", valores);
    }

    /**
     * Canaleta Parshall: Calcula Hmed para la profundidad de la lámina de agua
     * (H) para Qmed
     *
     * @return
     */
    public double calcularDP2() {

        double q2c = calcularQ2Cm3Seg();

        Map<String, Double> valores = new HashMap<>();
        valores.put("DCK", DCK);
        valores.put("DCN", DCN);
        valores.put("Q2C", q2c);

        return CalcularGeneral_("profundlaminaaguahmed", valores);
    }

    /**
     * Canaleta Parshall: Calcula Hmin para la profundidad de la lámina de agua
     * (H) para Qmin para Qmed
     *
     * @return
     */
    public double calcularDP3() {

        double q1c = calcularQ1Cm3Seg();

        Map<String, Double> valores = new HashMap<>();
        valores.put("DCK", DCK);
        valores.put("DCN", DCN);
        valores.put("Q1C", q1c);

        return CalcularGeneral_("profundlaminaaguahmin", valores);
    }

    /**
     * Canaleta Parshall: Calcula Resalto (Z)
     *
     * @return
     */
    public double calcularDRZ() {

        double q1c = calcularQ1Cm3Seg();
        double q3c = calcularQ3Cm3Seg();

        Map<String, Double> valores = new HashMap<>();
        valores.put("DP3", DP3);
        valores.put("DP1", DP1);
        valores.put("Q3C", q3c);
        valores.put("Q1C", q1c);

        return CalcularGeneral_("resaltozdrz", valores);
    }

    /**
     * Canaleta Parshall: Recibe una cadena que contiene los rangos de la tabla
     * de ayuda para descomponer y obtener los numeros comparando la respectiva
     * fila con 'DAN'
     *
     * @return número relacionado al introducido en DAN
     */
    public String calcularDPABCDEFGKN() {

        try {
            ResultSet result = db.ResultadoSelect("obtenerecuaciongeneral", "dimensionsmedidparshall", null); //Trae de la bd la Tabla
            String[] ecuacion = result.getString("ecuacion").split(",");

            for (int i = 0; i < ecuacion.length; i++) {
                String[] formula = ecuacion[i].split("\\|");
                if (formula[1].equals(DAN + "")) {
                    return ecuacion[i];
                }
            }
        } catch (Exception ex) {
            log.error("Error calcularDPABCDEFGKN:" + ex.toString());
        } finally {
            db.close();
        }
        return "";
    }

    /**
     * Canaleta Parshall: Calcula la distancia al centro del pozo desde la
     * garganta
     *
     * @return
     */
    public double calcularDPApozo() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("DPA", DPA);

        return CalcularGeneral_("distanciagargantaapozo", valores);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Desarenador
    /**
     * Desarenador: Calcula la altura máxima de lámina de agua en el desarenador
     * (H)
     *
     * @return
     */
    public double calcularDAH() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("DP1", DP1);
        valores.put("DRZ", DRZ);

        return CalcularGeneral_("alturamaxlaminaguadesarena", valores);
    }

    /**
     * Desarenador: Calcula Ancho del desarenador (b) /mts
     *
     * @return
     */
    public double calcularDAB() {

        double q3c = calcularQ3Cm3Seg();

        Map<String, Double> valores = new HashMap<>();
        valores.put("DAH", DAH);
        valores.put("DVF", DVF);
        valores.put("Q3C", q3c);

        return CalcularGeneral_("anchodesarenadorb", valores);
    }

    /**
     * Desarenador: Calcula la Longitud del desarenador (L)
     *
     * @return
     */
    public double calcularDLD() {


        Map<String, Double> valores = new HashMap<>();
        valores.put("DAH", DAH);

        return CalcularGeneral_("longituddesarenadorl", valores);
    }

    /**
     * Desarenador: Calcula Área longitudinal del desarenador (A)
     *
     * @return
     */
    public double calcularDAL() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("DAB", DAB);
        valores.put("DLD", DLD);

        return CalcularGeneral_("arealongitudinldesarenador", valores);
    }

    /**
     * Desarenador: Calcula Estimación de material retenido (q) m3/día
     */
    public double calcularDEM() {

        double q2c = calcularQ2Cm3Seg();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);

        return CalcularGeneral_("estimamaterialretenidoq", valores);
    }

    /**
     * Desarenador: Calcula Área longitudinal del desarenador (A)
     *
     * @return
     */
    public double calcularDUP() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("DEM", DEM);
        valores.put("DFL", (double) DFL);
        valores.put("DAL", DAL);

        return CalcularGeneral_("profundutildepositinfarena", valores);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Reactor UASB 
    /**
     * Reactor UASB: Cálculo de carga afluente media de DQO (Lo)
     *
     * @return
     */
    public double calcularUDQ() {

        double q2c = calcularQ2Cm3Dia();
        double cabq = CAB != 0 ? CAB : CAQ; // Por regla si no existe CAB se usa CAQ

        Map<String, Double> valores = new HashMap<>();
        valores.put("CAQ", cabq);
        valores.put("Q2C", q2c);

        return CalcularGeneral_("cargafluentemediadqo", valores);
    }

    /**
     * Reactor UASB: Calcula UTH ( Tiempo de retención hidráulico (TRHUASB)),
     * Usa CAT la cual se evalua en 'CalcularOtros' para que devuelva una cadena
     * de rangos que permitiran comparar uth
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
    public String calcularUTH(double uth) {

        try {
            CalcularOtrosSt = "";

            if (CalcularOtros_("tiemporetencionhidraulicouth", "UTH", CAT) == 0) { //si retorna 0, esta devolviendo la cadena 'CalcularOtrosSt'

                String[] rango = CalcularOtrosSt.trim().split("-");

                if (valida.EsDobleEntre(uth, Double.parseDouble(rango[0]), Double.parseDouble(rango[1]))) {
                    return ""; //el número si se encuentra entre los rangos
                }
            }
        } catch (Exception ex) {
            log.error("Error en CalcularUTH " + ";" + ex.toString());
        }
        return CalcularOtrosSt.trim();
    }

    /**
     * Reactor UASB: Calcula la determinación del volumen total del reactor (V)
     *
     * @return
     */
    public double calcularUVR() {

        double q2c = calcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("UTH", UTH);
        valores.put("Q2C", q2c);

        return CalcularGeneral_("determinacvoltotalreactor", valores);
    }

    /**
     * Reactor UASB: Calcula la Adopción de número de unidades de reactores NR
     *
     * @return
     */
    public double calcularUNR() {         

        double urn = CalcularOtros_("numerounidadesreactores", "UNR", UVR);//Puede devolver un número u otra ecuación para ejecutar

        if (urn == 0) { //si retorna 0, esta devolviendo la variable pública 'CalcularOtrosSt'

            Map<String, Double> valores = new HashMap<>();
            valores.put("UVR", UVR);
            valores.put("URV", URV);

            return CalcularGeneral_(CalcularOtrosSt, valores);
        } 
        return urn;
    }

    /**
     * Reactor UASB: Calcula el volumen por unidad de reactor (Vr)
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
     * Reactor UASB: Calcula la Altura del reactor (H). Trae de la BD una
     * formula que es una taba separada por pipes y comas para compararla al
     * valor ingresado por el usuario y devolver a USH (Altura compartimiento de
     * sedimentación (hs)) Y UHD (Altura compartimiento de digestión (hd))
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
     * Reactor UASB: Área por unidad de reactor (Ar) devuelve el numero
     * redondeado .5 ó +1
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
     * Reactor UASB: Calcula el Ancho (ar) de Área por unidad de reactor (Ar)
     *
     * @return
     */
    public double calcularUAA() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UAR", UAR);

        return CalcularGeneral_("anchoaruaa", valores);
    }

    /**
     * Reactor UASB: Calcula la Longitud (Lr) de Área por unidad de reactor (Ar)
     *
     * @return
     */
    public double calcularUAL() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UAA", UAA);

        return CalcularGeneral_("longitudual", valores);
    }

    /**
     * Reactor UASB: Calcula Verificación del área total (AT) - UASB
     *
     * @return
     */
    public double calcularUAT() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UNR", (double) UNR);
        valores.put("UAL", UAL);
        valores.put("UAA", UAA);

        return CalcularGeneral_("retenchidraulicareatotal", valores);
    }

    /**
     * Reactor UASB: Calcula Verificación del Volumen total (VT) - UASB
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
     * Reactor UASB: Calcula Verificación de cargas aplicadas, Carga hidráulica
     * volumétrica (CHV)
     *
     * @return
     */
    public double calcularURH() {

        double q2c = calcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("UVT", UVT);

        return CalcularGeneral_("tiemporetenhidraulicuasb", valores);
    }

    /**
     * Reactor UASB: Calcula Verificación del Tiempo de retención hidráulico
     * (TRHUASB)
     *
     * @return
     */
    public double calcularUCH() {

        double q2c = calcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("UVT", UVT);

        return CalcularGeneral_("cargahidraulicvolumchv", valores);
    }

    /**
     * Reactor UASB: Calcula Verificación de Carga orgánica volumétrica (COV)
     *
     * @return
     */
    public double calcularUCO() {

        double q2c = calcularQ2Cm3Dia();
        double cabq = CAB != 0 ? CAB : CAQ; // Por regla si no existe CAB se usa CAQ

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("CAQ", cabq);
        valores.put("UVT", UVT);

        return CalcularGeneral_("cargaorganicavolumuasb", valores);
    }

    /**
     * Reactor UASB: Cálculo de Velocidad ascensional Va para caudal medio
     *
     * @return
     */
    public double calcularUVN() {

        double q2c = calcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("UAT", UAT);

        return CalcularGeneral_("velascensioncaudalmedio", valores);
    }

    /**
     * Reactor UASB: Cálculo de Velocidad ascensional Va para caudal máximo
     *
     * @return
     */
    public double calcularUVM() {

        double q3c = calcularQ3Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q3C", q3c);
        valores.put("UAT", UAT);

        return CalcularGeneral_("velascensioncaudalmaximo", valores);
    }

    /**
     * Reactor UASB: Cálculo de Sistema de distribución del afluente, Número de
     * tubos Nd
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
     * Reactor UASB: Cálculo de Eficiencia de remoción de DQO del sistema (EDQO)
     *
     * @return
     */
    public double calcularUER() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("URH", URH);

        return CalcularGeneral_("eficienciaremociondqouer", valores);
    }

    /**
     * Reactor UASB: Cálculo de Eficiencia de remoción de DBO5 del sistema
     * (EDBO)
     *
     * @return
     */
    public double calcularURQ() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("URH", URH);

        return CalcularGeneral_("eficienciaremociondqourq", valores);
    }

    /**
     * Reactor UASB: Cálculo de Estimación de la concentración de DQO en el
     * afluente (S)
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
     * Reactor UASB: Cálculo de Estimación de la concentración de DBO en el
     * afluente (S)
     *
     * @return
     */
    public double calcularUEQ() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("URQ", URQ);
        valores.put("CAB", CAB);

        return CalcularGeneral_("estimconcentrcdboafluente", valores);
    }

    /**
     * Reactor UASB: Cálculo de Producción lodo esperada (Plodo) UASB
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
     * Reactor UASB: Cálculo de Volumen de lodo (Vlodo) UASB
     *
     * @return
     */
    public double calcularUVL() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UPL", UPL);

        return CalcularGeneral_("volumenlodouvl", valores);
    }

    /**
     * Reactor UASB: Cálculo de Carga de DQO convertida en metano (DQO CH4 )
     *
     * @return
     */
    public double calcularUDM() {

        double q2c = calcularQ2Cm3Dia();
        // No se usa la regla si no existe CAB se usa CAQ      

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("UEC", UEC);
        valores.put("CAQ", CAQ);
        valores.put("USM", USM);

        return CalcularGeneral_("cargadqoconvertidametano", valores);
    }

    /**
     * Reactor UASB: Cálculo de Factor de corrección para la temperatura
     * operacional del reactor F(t)
     *
     * @return
     */
    public double calcularUCT() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("CAT", CAT);

        return CalcularGeneral_("factcorrecctempopreactor", valores);
    }

    /**
     * Reactor UASB: Cálculo de Producción volumétrica de metano QCH4
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
     * Reactor UASB: Cálculo de Producción volumétrica de biogás Qbiogas
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
     * Reactor UASB: Cálculo de Energía bruta producida (E) en kJ /d
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
     * Reactor UASB: Cálculo de Energía bruta producida (E) UEB en kWh/d
     *
     * @return
     */
    public double calcularUEBK() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UEB", UEB);

        return CalcularGeneral_("energiabrutaproducidauebk", valores);
    }

    /**
     * Reactor UASB: Cálculo de Potencial de electricidad disponible Kw
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
     * Reactor UASB: Cálculo de Potencial de electricidad disponible en HP
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

        double q2c = calcularQ2Cm3Dia();
        double uecq;

        if (CAB != 0) { // Por regla si no existe CAB se usa CAQ
            uecq = UEQ;
        } else {
            uecq = UEC;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("PCO", PCO);
        valores.put("Q2C", q2c);
        valores.put("UEC", uecq);

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

        double q2c = calcularQ2Cm3Dia();

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
//            if (!CalcularOtrosSt.trim().isEmpty()) {
//                util.Mensaje(CalcularOtrosSt, "ok");
//            }
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

        double q3c = calcularQ3Cm3Dia();

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
     * Filtro Precolador: Cálculo de Eficiencia de remoción de DQO del filtro
     * percolador (EFP)
     *
     * @return
     */
    public double calcularPER() {

        double q2c = calcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("UEC", UEC);
        valores.put("PVM", PVM);

        return CalcularGeneral_("eficienremociondqofiltrperc", valores);
    }

    /**
     * Filtro Precolador: Cálculo de Eficiencia de remoción de DBO del filtro
     * percolador (EFP)
     *
     * @return
     */
    public double calcularPEQ() {

        double q2c = calcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("UEQ", UEQ);
        valores.put("PVM", PVM);

        return CalcularGeneral_("eficienremociondbofiltrperc", valores);
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
        valores.put("PER", PER);

        return CalcularGeneral_("concntdqoeflntfinuasbfiltr", valores);
    }

    /**
     * Filtro Precolador: Cálculo de Concentración de DBO en el efluente final
     * del sistema de tratamiento Reactor UASB + Filtro percolador (Sfinal)
     *
     * @return
     */
    public double calcularPCB() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UEQ", UEQ);
        valores.put("PEQ", PEQ);

        return CalcularGeneral_("concntdboeflntfinuasbfiltr", valores);
    }

    /**
     * Filtro Precolador: Estimación de la producción de lodo (Plodo)
     *
     * @return
     */
    public double calcularPEL() {

        double q2c = calcularQ2Cm3Dia();
        double uecq, pcdb;

        if (CAB != 0) { // Por regla si no existe CAB se usa CAQ de cálculos de uasb
            uecq = UEQ;
            pcdb = PCB;
        } else {
            uecq = UEC;
            pcdb = PCD;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("UEC", uecq);
        valores.put("PCD", pcdb);

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

        double q2c = calcularQ2Cm3Dia();

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

        double q2c = calcularQ2Cm3Dia();

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

        double q2c = calcularQ2Cm3Dia();

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

        double caqb = CAB != 0 ? CAB : CAQ;

        Map<String, Double> valores = new HashMap<>();
        valores.put("MDS", MDS);
        valores.put("CAQB", caqb);

        return CalcularGeneral_("eficienciaremocdbosoluble", valores);
    }

    /**
     * Lodos Activados Convencional: Cálculo de la eficiencia global de remoción
     * de DBO
     *
     * @return
     */
    public double calcularMED() {

        double caqb = CAB != 0 ? CAB : CAQ;

        Map<String, Double> valores = new HashMap<>();
        valores.put("MDB", MDB);
        valores.put("CAQB", caqb);

        return CalcularGeneral_("eficienciaglobalremocdbo", valores);
    }

    /**
     * Lodos Activados Convencional: Cálculo de producción de lodo esperada - ∆X
     *
     * @return
     */
    public double calcularMPL() {

        double q2c = calcularQ2Cm3Dia();
        double caqb = CAB != 0 ? CAB : CAQ;

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("CAQB", caqb);
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

        double q2c = calcularQ2Cm3Dia();
        double caqb = CAB != 0 ? CAB : CAQ;

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("CAQB", caqb);
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

        double q2c = calcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("MVT", MVT);
        valores.put("Q2C", q2c);

        return CalcularGeneral_("tiempretenchidraulicmvh", valores);
    }

    /**
     * Lodos Activados Convencional: Cálculo del tiempo retención hidráulico
     * (TRH), Mostrar mensaje por rangos.
     *
     * @return : cadena con el mensaje proveniente de la fórmula
     */
    public String calcularMVHC() {

        double mvhc = calcularMVH();

        CalcularOtrosSt = "";

        if (CalcularOtros_("tiempretenchidraulicmvhc", "MVHC", mvhc) == 0) { //si retorna 0, esta devolviendo la cadena 'CalcularOtrosSt'           
            return CalcularOtrosSt;
        }
        return "";
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
     * Lodos Activados Convencional: Cálculo del Ancho del tanque de aireación
     *
     * @return
     */
    public double calcularMWT() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("MAS", MAS);

        return CalcularGeneral_("anchotanqueaireacionmwt", valores);
    }

    /**
     * Lodos Activados Convencional: Cálculo de la Longitud del tanque de
     * aireación
     *
     * @return
     */
    public double calcularMHT() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("MWT", MWT);

        return CalcularGeneral_("longitanqueaireacionmht", valores);
    }

    /**
     * Lodos Activados Convencional: Cálculo de la relación
     * Alimento/Microorganismos (A/M)
     *
     * @return
     */
    public double calcularMAM() {

        double q2c = calcularQ2Cm3Dia();

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

        double q2c = calcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("MFS", MFS);

        return CalcularGeneral_("areasedimentacionmsa", valores);
    }

    /**
     * Lodos Activados Convencional: Cálculo de Masa de sólidos (M)
     *
     * @return
     */
    public double calcularMMS() {

        double q2c = calcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("MRM", MRM);
        valores.put("MST", MST);

        return CalcularGeneral_("tasaaplicacionsolidosmms", valores);
    }

    /**
     * Lodos Activados Convencional: Cálculo de Diámetro Sedimentador Secundario
     *
     * @return
     */
    public double calcularMSD() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("pi", Math.PI);
        valores.put("MSA", MSA);

        return CalcularGeneral_("diametrosedimentadormsd", valores);
    }

    /**
     * Lodos Activados Convencional: Cálculo de Área total requerida por la
     * tecnología
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

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Lodos Activados Modalidad Aireación Extendida
    /**
     * Lodos Activados Aireación Extendida: Cálculo del coeficiente de
     * producción celular ajustado por la pérdida por respiración endógena
     * (Yobs)
     *
     * @return
     */
    public double calcularECE() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("ECC", ECC);
        valores.put("EEL", EEL);
        valores.put("ETE", ETE);

        return CalcularGeneral_("coeficiprodcelulrespirendoge", valores);
    }

    /**
     * Lodos Activados Aireación Extendida: Cálculo de la DBO soluble en el
     * efluente y la eficiencia de remoción de DBO5: DBO particulada (Sp)
     *
     * @return
     */
    public double calcularEDP() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("ESE", ESE);
        valores.put("ERD", ERD);

        return CalcularGeneral_("eficienremocdbo5particuladae", valores);
    }

    /**
     * Lodos Activados Aireación Extendida: Cálculo de la DBO soluble en el
     * efluente y la eficiencia de remoción de DBO5: DBO soluble (Se)
     *
     * @return
     */
    public double calcularEDS() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("EDB", EDB);
        valores.put("EDP", EDP);

        return CalcularGeneral_("dbosolublefluentesee", valores);
    }

    /**
     * Lodos Activados Aireación Extendida: Cálculo de la eficiencia de remoción
     * de DBO soluble
     *
     * @return
     */
    public double calcularEES() {

        double cabq = CAB != 0 ? CAB : CAQ; // Por regla si no existe CAB se usa CAQ

        Map<String, Double> valores = new HashMap<>();
        valores.put("EDS", EDS);
        valores.put("CAB", cabq);

        return CalcularGeneral_("eficienciaremocdbosolublee", valores);
    }

    /**
     * Lodos Activados Aireación Extendida: Cálculo de la eficiencia global de
     * remoción de DBO
     *
     * @return
     */
    public double calcularEED() {

        double cabq = CAB != 0 ? CAB : CAQ;

        Map<String, Double> valores = new HashMap<>();
        valores.put("EDB", EDB);
        valores.put("CAB", cabq);

        return CalcularGeneral_("eficienciaglobalremocdboe", valores);
    }

    /**
     * Lodos Activados Aireación Extendida: Cálculo de producción de lodo
     * esperada - ∆X
     *
     * @return
     */
    public double calcularEPL() {

        double q2c = calcularQ2Cm3Dia();

        double cabq = CAB != 0 ? CAB : CAQ;

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("CAB", cabq);
        valores.put("EDS", EDS);
        valores.put("ECE", ECE);

        return CalcularGeneral_("produccionlodoesperadaxe", valores);
    }

    /**
     * Lodos Activados Aireación Extendida: Cálculo de producción de lodo
     * esperada - ∆XT
     *
     * @return
     */
    public double calcularELT() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("EPL", EPL);
        valores.put("CVS", CVS);

        return CalcularGeneral_("producclodoesperadaxtmlte", valores);
    }

    /**
     * Lodos Activados Aireación Extendida: Cálculo del volumen de lodo purgado
     * por día (Vlodo)
     *
     * @return
     */
    public double calcularEVL() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("ELT", ELT);

        return CalcularGeneral_("volumenlodopurgadodiae", valores);
    }

    /**
     * Lodos Activados Aireación Extendida: Cálculo del volumen de lodo del
     * tanque de aireación (VTA)
     *
     * @return
     */
    public double calcularEVT() {

        double q2c = calcularQ2Cm3Dia();

        double cabq = CAB != 0 ? CAB : CAQ;

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("CAB", cabq);
        valores.put("ECC", ECC);
        valores.put("EDS", EDS);
        valores.put("EEL", EEL);
        valores.put("ETE", ETE);
        valores.put("EST", EST);
        valores.put("CVS", CVS);

        return CalcularGeneral_("vollodotanqueaireacionvtae", valores);
    }

    /**
     * Lodos Activados Aireación Extendida: Cálculo del tiempo retención
     * hidráulico (TRH)
     *
     * @return
     */
    public double calcularEVH() {

        double q2c = calcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("EVT", EVT);
        valores.put("Q2C", q2c);

        return CalcularGeneral_("tiempretenchidraulicmvhe", valores);
    }

    /**
     * Lodos Activados Aireación Extendida: Cálculo del tiempo retención
     * hidráulico (TRH), Eostrar mensaje por rangos.
     *
     * @return : cadena con el mensaje proveniente de la fórmula
     */
    public String calcularEVHC() {

        double mvhc = calcularEVH();
        CalcularOtrosSt = "";

        if (CalcularOtros_("tiempretenchidraulicmvhce", "EVHC", mvhc) == 0) { //si retorna 0, esta devolviendo la cadena 'CalcularOtrosSt'
            return CalcularOtrosSt;
        }
        return "";
    }

    /**
     * Lodos Activados Aireación Extendida: Cálculo del Área superficial (ATA)
     *
     * @return
     */
    public double calcularEAS() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("EVT", EVT);
        valores.put("EPT", EPT);

        return CalcularGeneral_("areasuperficialmase", valores);
    }

    /**
     * Lodos Activados Aireación Extendida: Cálculo del Ancho del tanque de
     * aireación
     *
     * @return
     */
    public double calcularEWT() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("EAS", EAS);

        return CalcularGeneral_("anchotanqueaireacionewt", valores);
    }

    /**
     * Lodos Activados Aireación Extendida: Cálculo de la Longitud del tanque de
     * aireación
     *
     * @return
     */
    public double calcularEHT() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("EWT", EWT);

        return CalcularGeneral_("longitanqueaireacioneht", valores);
    }

    /**
     * Lodos Activados Aireación Extendida: Cálculo de la relación
     * Alimento/Microorganismos (A/M)
     *
     * @return
     */
    public double calcularEAM() {

        double q2c = calcularQ2Cm3Dia();

        double cabq = CAB != 0 ? CAB : CAQ;

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("CAB", cabq);
        valores.put("EST", EST);
        valores.put("EVT", EVT);
        valores.put("EDS", EDS);
        valores.put("CVS", CVS);

        return CalcularGeneral_("relacnalimentmicrorganisme", valores);
    }

    /**
     * Lodos Activados Aireación Extendida: Cálculo de la recirculación mínima
     * recomendada (r)
     *
     * @return
     */
    public double calcularERM() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("EST", EST);
        valores.put("ESR", ESR);

        return CalcularGeneral_("recirculacminrecomendadae", valores);
    }

    /**
     * Lodos Activados Aireación Extendida: Verificación de la producción de
     * lodo ΔX a partir del volumen del tanque de aireación, edad del lodo y
     * concentración de SSTA
     *
     * @return
     */
    public double calcularEEC() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("EST", EST);
        valores.put("EVT", EVT);
        valores.put("EEL", EEL);

        return CalcularGeneral_("prodlodoxvoltanqaireacsstae", valores);
    }

    /**
     * Lodos Activados Aireación Extendida: Cálculo del Área de sedimentación
     * (As)
     *
     * @return
     */
    public double calcularESA() {

        double q2c = calcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("EFS", EFS);

        return CalcularGeneral_("areasedimentacionmsae", valores);
    }

    /**
     * Lodos Activados Aireación Extendida: Cálculo de Masa de sólidos (M)
     *
     * @return
     */
    public double calcularEMS() {

        double q2c = calcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("ERM", ERM);
        valores.put("EST", EST);

        return CalcularGeneral_("tasaaplicacionsolidosmmse", valores);
    }

    /**
     * Lodos Activados Aireación Extendida: Cálculo de Diámetro Sedimentador
     * Secundario
     *
     * @return
     */
    public double calcularESD() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("pi", Math.PI);
        valores.put("ESA", ESA);

        return CalcularGeneral_("diametrosedimentadoresd", valores);
    }

    /**
     * Lodos Activados Aireación Extendida: Cálculo de Área total requerida por
     * la tecnología
     *
     * @return
     */
    public double calcularEAT() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("EAS", EAS);
        valores.put("ESA", ESA);

        return CalcularGeneral_("areatotalrequeridamate", valores);
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - UASB + Lodos Activados Mod. Convencional

    /**
     * UASB + Lodos Activados Convencional: Cálculo del coeficiente de
     * producción celular ajustado por la pérdida por respiración endógena
     * (Yobs)
     *
     * @return
     */
    public double calcularNCE() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("NCC", NCC);
        valores.put("NEL", NEL);
        valores.put("NTE", NTE);

        return CalcularGeneral_("coeficiprodcelulrespirendogu", valores);
    }

    /**
     * UASB + Lodos Activados Convencional: Cálculo de la DBO soluble en el
     * efluente y la eficiencia de remoción de DBO5: DBO particulada (Sp)
     *
     * @return
     */
    public double calcularNDP() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("NSE", NSE);
        valores.put("NRD", NRD);

        return CalcularGeneral_("eficienremocdbo5particuladau", valores);
    }

    /**
     * UASB + Lodos Activados Convencional: Cálculo de la DBO soluble en el
     * efluente y la eficiencia de remoción de DBO5: DBO soluble (Se)
     *
     * @return
     */
    public double calcularNDS() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("NDB", NDB);
        valores.put("NDP", NDP);

        return CalcularGeneral_("dbosolublefluenteseu", valores);
    }

    /**
     * UASB + Lodos Activados Convencional: Cálculo de la eficiencia de remoción
     * de DBO soluble
     *
     * @return
     */
    public double calcularNES() {

        double uecq = 0;

        if (CAB != 0) {
            uecq = UEQ;
        } else {
            uecq = UEC;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("NDS", NDS);
        valores.put("UECQ", uecq);

        return CalcularGeneral_("eficienciaremocdbosolubleu", valores);
    }

    /**
     * UASB + Lodos Activados Convencional: Cálculo de la eficiencia global de
     * remoción de DBO
     *
     * @return
     */
    public double calcularNED() {

        double uecq = 0;

        if (CAB != 0) {
            uecq = UEQ;
        } else {
            uecq = UEC;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("NDB", NDB);
        valores.put("UECQ", uecq);

        return CalcularGeneral_("eficienciaglobalremocdbou", valores);
    }

    /**
     * UASB + Lodos Activados Convencional: Cálculo de producción de lodo
     * esperada - ∆X
     *
     * @return
     */
    public double calcularNPL() {

        double q2c = calcularQ2Cm3Dia();
        double uecq = 0;

        if (CAB != 0) {
            uecq = UEQ;
        } else {
            uecq = UEC;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("UECQ", uecq);
        valores.put("NDS", NDS);
        valores.put("NCE", NCE);

        return CalcularGeneral_("produccionlodoesperadaxu", valores);
    }

    /**
     * UASB + Lodos Activados Convencional: Cálculo de producción de lodo
     * esperada - ∆XT
     *
     * @return
     */
    public double calcularNLT() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("NPL", NPL);
        valores.put("CVS", CVS);

        return CalcularGeneral_("producclodoesperadaxtmltu", valores);
    }

    /**
     * UASB + Lodos Activados Convencional: Cálculo del volumen de lodo purgado
     * por día (Vlodo)
     *
     * @return
     */
    public double calcularNVL() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("NLT", NLT);

        return CalcularGeneral_("volumenlodopurgadodiau", valores);
    }

    /**
     * UASB + Lodos Activados Convencional: Cálculo del volumen de lodo del
     * tanque de aireación (VTA)
     *
     * @return
     */
    public double calcularNVT() {

        double q2c = calcularQ2Cm3Dia();
        double uecq = 0;

        if (CAB != 0) {
            uecq = UEQ;
        } else {
            uecq = UEC;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("UECQ", uecq);
        valores.put("NCC", NCC);
        valores.put("NDS", NDS);
        valores.put("NEL", NEL);
        valores.put("NTE", NTE);
        valores.put("NST", NST);
        valores.put("CVS", CVS);

        return CalcularGeneral_("vollodotanqueaireacionvtau", valores);
    }

    /**
     * UASB + Lodos Activados Convencional: Cálculo del tiempo retención
     * hidráulico (TRH)
     *
     * @return
     */
    public double calcularNVH() {

        double q2c = calcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("NVT", NVT);
        valores.put("Q2C", q2c);

        return CalcularGeneral_("tiempretenchidraulicmvhu", valores);
    }

    /**
     * UASB + Lodos Activados Convencional: Cálculo del tiempo retención
     * hidráulico (TRH), Nostrar mensaje por rangos.
     *
     * @return : cadena con el mensaje proveniente de la fórmula
     */
    public String calcularNVHC() {

        double mvhc = calcularNVH();
        CalcularOtrosSt = "";

        if (CalcularOtros_("tiempretenchidraulicmvhcu", "NVHC", mvhc) == 0) { //si retorna 0, esta devolviendo la variable pública 'CalcularOtrosSt'
            return CalcularOtrosSt;
        }
        return "";
    }

    /**
     * UASB + Lodos Activados Convencional: Cálculo del Área superficial (ATA)
     *
     * @return
     */
    public double calcularNAS() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("NVT", NVT);
        valores.put("NPT", NPT);

        return CalcularGeneral_("areasuperficialmasu", valores);
    }

    /**
     * UASB + Lodos Activados Convencional: Cálculo del Ancho del tanque de
     * aireación
     *
     * @return
     */
    public double calcularNWT() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("NAS", NAS);

        return CalcularGeneral_("anchotanqueaireacionnwt", valores);
    }

    /**
     * UASB + Lodos Activados Convencional: Cálculo de la Longitud del tanque de
     * aireación
     *
     * @return
     */
    public double calcularNHT() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("NWT", NWT);

        return CalcularGeneral_("longitanqueaireacionnht", valores);
    }

    /**
     * UASB + Lodos Activados Convencional: Cálculo de la relación
     * Alimento/Microorganismos (A/M)
     *
     * @return
     */
    public double calcularNAM() {

        double q2c = calcularQ2Cm3Dia();
        double uecq;

        if (CAB != 0) {
            uecq = UEQ;
        } else {
            uecq = UEC;
        }

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("UECQ", uecq);
        valores.put("NST", NST);
        valores.put("NVT", NVT);
        valores.put("NDS", NDS);
        valores.put("CVS", CVS);

        return CalcularGeneral_("relacnalimentmicrorganismu", valores);
    }

    /**
     * UASB + Lodos Activados Convencional: Verificación de la producción de
     * lodo ΔX a partir del volumen del tanque de aireación, edad del lodo y
     * concentración de SSTA
     *
     * @return
     */
    public double calcularNEC() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("NST", NST);
        valores.put("NVT", NVT);
        valores.put("NEL", NEL);

        return CalcularGeneral_("prodlodoxvoltanqaireacsstau", valores);
    }

    /**
     * UASB + Lodos Activados Convencional: Cálculo del Área de sedimentación
     * (As)
     *
     * @return
     */
    public double calcularNSA() {

        double q2c = calcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("NFS", NFS);

        return CalcularGeneral_("areasedimentacionmsau", valores);
    }

    /**
     * UASB + Lodos Activados Convencional: Cálculo de Masa de sólidos (M)
     *
     * @return
     */
    public double calcularNMS() {

        double q2c = calcularQ2Cm3Dia();

        Map<String, Double> valores = new HashMap<>();
        valores.put("Q2C", q2c);
        valores.put("NRM", NRM);
        valores.put("NST", NST);

        return CalcularGeneral_("tasaaplicacionsolidosmmsu", valores);
    }

    /**
     * UASB + Lodos Activados Convencional: Cálculo de Diámetro
     *
     * @return
     */
    public double calcularNSD() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("pi", Math.PI);
        valores.put("NSA", NSA);

        return CalcularGeneral_("diametrosedimentadornsd", valores);
    }

    /**
     * UASB + Lodos Activados Convencional: Cálculo de Área total requerida por
     * la tecnología
     *
     * @return
     */
    public double calcularNAT() {

        Map<String, Double> valores = new HashMap<>();
        valores.put("UAT", UAT);
        valores.put("NAS", NAS);
        valores.put("NSA", NSA);

        return CalcularGeneral_("areatotalrequeridamatu", valores);
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
            return 0;

        } catch (Exception ex) {
            log.error("Error en CalcularGeneral_(),ecuación:" + ecuacion + ";" + ex.toString());
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
            log.error("Error en CalcularOtros_(),ecuacion:" + ecuacion + ";x: " + x + "; " + ex.toString());
            return 0;
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }

    /**
     * Crea un archivo tipo .PTAR, que contiene los datos del proyecto; este
     * archivo lleva una codificación simple que busca que el archivo no sea
     * alterado manualmente.
     *
     * @param usarPath booleano que indica si se enviar la ruta del proyecto
     * cargada anteriormente para sobreescribir el archivo. True: usar ruta
     * cargada
     * @return
     */
    public boolean exportarProyecto(boolean usarPath) {
        String msg = "";

        String sql = "TFA;TML;TCA;TCR;TCI;TUC;TDI;TRE;TDP;PAG;PPT;PPF;PCR;CAR;KAR;QIF;QEL;QET;QEC;QEK;QAA;QAI;QAC;QAK;QCA;QCM;QCC;QCK;IOA;IRM;IAI;IPI;IQI;IKI;IQ2;IK2;COA;CAC;CPC;CQC;CKC;YOA;YAI;YPI;YQI;YKI;Q2C;Q2K;Q3M;Q3R;Q3C;Q3K;Q1H;Q1C;Q1K;QCD;CAT;CAB;CAQ;CAS;CAV;CVS;LCO;LCE;LAE;LVU;LTH;LVR;LAU;LAS;LLA;LAA;LAL;LAP;LAI;LAB;FTE;FCO;FCA;FAS;FTR;FED;FUV;FSA;FSL;FAP;FAI;FAB;FLA;RPL;RTR;REB;REL;RIB;RBI;RPM;RPN;RAU;RER;RAT;RAR;RCP;RCM;RLC;RVM;RVN;RNB;RNE;RPC;RPS;RPB;RPH;DAN;DCK;DCN;DP1;DP2;DP3;DRZ;DPA;DPB;DPC;DPD;DPE;DPF;DPG;DPK;DPN;DAH;DAB;DVF;DLD;DAL;DEM;DFL;DUP;UDQ;UTH;UVR;URV;UNR;UVU;UHR;UHS;UHD;UAR;UAA;UAL;UAT;UVT;URH;UCH;UCO;UVN;UVM;UAD;UNT;UER;URQ;UEC;UEQ;UCP;UPL;UVL;USM;UDM;UCT;UPM;UCM;UVB;UCB;UEB;URE;UPE;PCO;PVM;PPM;PAS;PH2;PH3;PFU;PFA;PFD;PER;PEQ;PCD;PCB;PEL;PVL;PSS;PSA;PPS;PNU;PAX;PDX;PAT;MCH;MAR;MPS;MTR;MGC;MGA;MGL;MDB;MSE;MRD;MST;MSR;MCC;MTE;MEL;MCE;MDP;MDS;MES;MED;MPL;MLT;MVL;MVT;MVH;MPT;MAS;MWT;MHT;MAM;MRM;MEC;MFS;MSA;MMS;MHS;MSD;MAT;EDB;ESE;ERD;EST;ESR;ECC;ETE;EEL;ECE;EDP;EDS;EES;EED;EPL;ELT;EVL;EVT;EVH;EPT;EAS;EWT;EHT;EAM;ERM;EEC;EFS;ESA;EMS;EHS;ESD;EAT;NDB;NSE;NRD;NST;NSR;NCC;NTE;NEL;NCE;NDP;NDS;NES;NED;NPL;NLT;NVL;NVT;NVH;NPT;NAS;NWT;NHT;NAM;NRM;NEC;NFS;NSA;NMS;NHS;NSD;NAT;GTE;GCO;GCA;GAS;GTR;GED;GUV;GSA;GSL;GAP;GAI;GAB;GUA;";
        sql += "EditDatosDeEntrada;EditProyeccionPoblacional;EditCalculoCaudalesDiseno;EditCaractInicAguaResidual;EditRejillas;EditDesarenador;EditLagunaAnaerobia;EditLagunaFacultativaSec;EditLagunaFacultativaUASB;EditReactorUasb;EditFiltroPercolador;EditLodosActivadsConvenc;EditLodosActivadsConvencUASB;EditLodosActivadsAireacExt;";
        try {
            String[] sqlsplit = sql.split(";"); //Se parte la cadena para obtener cada nombre

            for (String vars : sqlsplit) {

                field = getClass().getDeclaredField(vars); //Se le pasa el nombre de la variable 

                if (field.getType().toString().contains("double")) {//Por cada tipo de variable se le pasa el valor reconstruyendo la cadena con valores 
                    msg += vars + ":" + field.getDouble(this) + ";";
                } else if (field.getType().toString().contains("int")) {
                    msg += vars + ":" + field.getInt(this) + ";";
                } else if (field.getType().toString().contains("boolean")) {
                    msg += vars + ":" + field.getBoolean(this) + ";";
                } else if (field.getType().toString().contains("String")) { //Si es cadena se añaden comillas
                    msg += vars + ":\"" + field.get(this).toString() + "\";";
                }
            }

            if (!usarPath) {
                pathProyecto = "";
            }

            if (util.CrearArchivoPtar(util.encriptar(msg, "ptar"), pathProyecto)) { //Se pasa la cadena de variables y valores codificada para escribir el archivo

                if (!usarPath) {
                    util.Mensaje("Archivo creado Exitosamente", "ok");
                }

                pathProyecto = util.pathPtar;
                return true;
            } else {
                util.Mensaje("No se ha podido crear el archivo", "error");
                pathProyecto = "";
            }
        } catch (Exception ex) {
            pathProyecto = "";
            log.error("Error exportarProyecto(). " + ex.getMessage() + " " + ex.getCause());
            util.Mensaje("No se ha podido crear el archivo \n" + ex.getMessage() + " " + ex.getCause(), "error");
        }
        return false;
    }

    /**
     * Abre un archivo tipo .PTAR, que contiene los datos del proyecto y los
     * desencripta para cargarlos.
     *
     * @return
     */
    public boolean importarProyecto() {

        try {
            String msg = util.AbrirArchivoPtar();

            if (msg == null || msg.equals("error") || msg.isEmpty()) {
                util.Mensaje("No se ha podido cargar el proyecto", "error");
                pathProyecto = "";
                return false;
            }
            msg = util.desEncriptar(msg, "ptar");

            String[] varSplit;
            String[] sqlsplit = msg.split(";");

            for (String vars : sqlsplit) {
                varSplit = vars.split(":");
                field = getClass().getDeclaredField(varSplit[0]);

                if (varSplit[1].contains("\"")) { //Es una cadena
                    field.set(this, varSplit[1].replace("\"", ""));
                } else if (varSplit[1].contains("true")) { //Es un booleano v
                    field.setBoolean(this, true);
                } else if (varSplit[1].contains("false")) { //Es un booleano f
                    field.setBoolean(this, false);
                } else if (field.getType().toString().contains("int")) {
                    field.setInt(this, Integer.parseInt(varSplit[1]));
                } else {
                    field.setDouble(this, Double.parseDouble(varSplit[1]));
                }
            }
            pathProyecto = util.pathPtar;
            return true;

        } catch (Exception ex) {
            pathProyecto = "";
            log.error("Error importarProyecto(). " + ex.getMessage() + " " + ex.getCause());
            util.Mensaje("No se ha podido importar el Proyecto \n" + ex.getMessage(), "error");
        }
        return false;
    } 
}
