package UI;

import org.apache.log4j.Logger;
import Componentes.Configuracion;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Bienvenido1 extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("Bienvenido"); //Nombre que tomará en el log.  

    public Bienvenido1() {
        initComponents();
        InicializarComponentes();
    }

    /**
     * Inicializa todos los elementos (jlabels,jtextbox,etc.) con sus
     * respectivos valores y textos traidos desde al base de datos.
     */
    public void InicializarComponentes() {

        try {
            btn_close.setText("Cerrar");
            btn_siguiente.setText("Siguiente");
            lbl_NoShow.setText("No mostrar al inicio");
            String rutaimagen = conf.ObtenerRutaGeneral("rutaimagenes") + conf.separador + conf.ObtenerKey("imgbienvenido");
            BufferedImage img = ImageIO.read(new File(rutaimagen));
            ImageIcon iconLogo2 = new ImageIcon(escalarImagen(img, 964, 550)); //Esta relacion de aspecto se debe tomar en un programa de diseño o calcularse
            lbl_Bienvenido.setHorizontalAlignment(JLabel.CENTER);
            lbl_Bienvenido.setVerticalAlignment(JLabel.CENTER);
            lbl_Bienvenido.setIcon(iconLogo2);
            lbl_NoShow.setText("Versión 1.3.0");

            //todo, traer desde bd
            String txt = "<HTML><div style='text-align:center;'><b>PRESENTACIÓN</b></div><div style='text-align:center;'><br/>En los países en desarrollo aún existe baja cobertura en el tratamiento de las aguas residuales, lo que puede conllevar a problemas ambientales y de salud pública. <br/>En América Latina y el Caribe persisten retrasos en infraestructura, y aunque la inversión se ha incrementado en los últimos años, el atraso acumulado de varias <br/>décadas aún se mantiene (Noyola et al., 2013). En Colombia el panorama es similar, de acuerdo con la Superintendencia de Servicios Públicos Domiciliarios - <br/>SSPD (2013) aunque existen cerca de 562 sistemas construidos, en su mayoría el funcionamiento no es adecuado, siendo el sector rural el más afectado.<br/><br/>PTAR DISEÑO es una herramienta que surge como una opción de apoyo en la selección y predimensionamiento de tecnologías de tratamiento de aguas <br/>residuales domésticas para poblaciones entre 500 y 200.000 habitantes, lo que a su vez puede brindar soporte técnico durante los procesos de planeación <br/>y gestión de recursos a cargo de las instituciones o partes interesadas. PTAR DISEÑO también es una herramienta que puede apoyar los procesos de <br/>formación en el sector académico – investigativo, por su carácter de libre acceso y por contar con ayudas soportadas en la literatura científica. <br/><br/>PTAR DISEÑO está desarrollado para operar en los sistemas operativos Windows, Linux y MacOS, y es puesto a disposición de los interesados bajo la <br/>Licencia Pública General GPL v.2.0 de GNU (<a href=\"https://www.gnu.org/licenses/old-licenses/gpl-2.0.html\">https://www.gnu.org/licenses/old-licenses/gpl-2.0.html</a>). El software proviene de un proyecto NetBeans, desarrollado <br/>con lenguaje de programación Java, utiliza la base de datos SQLite y archivos XML para la configuración. Además cuenta con la documentación técnica necesaria <br/>para realizar cambios en la programación, estando disponible su código fuente en un repositorio público de Github (<a href=\"https://github.com/usc-repos/ptar-diseno\">https://github.com/usc-repos/ptar-diseno</a>).<br/><br/>La herramienta informática PTAR DISEÑO fue desarrollada por los grupos de investigación en Ingeniería Electrónica, Industrial y Ambiental – GIEIAM y <br/>COMBA I+D adscritos a la Facultad de Ingeniería de la Universidad Santiago de Cali - USC, bajo el liderazgo de los docentes Andrea Pérez Vidal <br/>(Ing. Sanitaria, MSc., PhD.), Jorge Antonio Silva Leal (Ing. Biotecnológico, MSc., PhD) y Diego Fernando Marin Lozano (Ing. Sistemas, Esp). <br/><br/>Se contó con el apoyo técnico de Alexander Alberto Colorado Solano (Ing. Sistemas) adscrito al programa de Maestría en Informática de la USC y Marcela <br/>Collazos Morales y Andrés Felipe Aya Mejía adscritos al programa de Bioingeniería, quienes desarrollaron su trabajo de grado en el marco del proyecto de <br/>investigación. Adicionalmente, la Unidad de Comunicaciones y el Departamento de Gestión Tecnológica de la USC contribuyeron con el diseño del logo y <br/>publicación del software en el repositorio institucional de proyectos.</div><br/><br/> <u>Referencia(s) Bibliográfica(s):</u><br/><em>Noyola, A.; Sagastume, J.; Güereca, L. (2013). Selección de tecnologías para el tratamiento de aguas residuales municipales. <br/>Disponible en: http://www.pronatura-sur.org/web/docs/Tecnologia_Aguas_Residuales.pdf [consultado el día julio 27 de 2016].<br/><br/>Superintendencia de Servicios Públicos Domiciliarios [SSPD]. (2013). Informe técnico sobre sistemas de tratamiento de aguas residuales en Colombia. <br/>Disponible en: http://www.superservicios.gov.co/content/download/4989/47298 [consultado el día 30 de junio de 2016)</em></HTML>";

            lbl_intro.setText(txt);
        } catch (Exception ex) {
            log.error("Error en InicializarComponentes " + ex.getMessage());
            ErrorGeneral(); // Ha ocurrido algun error en los datos o conexión, se debe revisar log.
        } finally {
        }
    }

    /**
     * Deshabilita todos los componentes y muestra una advertencia al usuario
     */
    private void ErrorGeneral() {

        Component[] comp = jp_Componentes.getComponents();//Deshabilitar todos los Componentes del Jpane
        for (Component component : comp) {
            component.setEnabled(false);
        }
        btn_close.setEnabled(true);
        this.dispose();//Esta ventana no es crítica, se puede omitir
    }

    public Image escalarImagen(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jsp_Bienvenido = new javax.swing.JScrollPane();
        jp_Componentes = new javax.swing.JPanel();
        tabPane = new javax.swing.JTabbedPane();
        tabPane1 = new javax.swing.JPanel();
        lbl_Bienvenido = new javax.swing.JLabel();
        btn_siguiente = new javax.swing.JButton();
        paneTab2 = new javax.swing.JPanel();
        lbl_intro = new javax.swing.JLabel();
        btn_close = new javax.swing.JButton();
        chbxNoshow = new javax.swing.JCheckBox();
        lbl_NoShow = new javax.swing.JLabel();
        lbl_NoShow1 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1024, 735));

        jsp_Bienvenido.setPreferredSize(new java.awt.Dimension(1024, 768));

        jp_Componentes.setBackground(new java.awt.Color(255, 255, 255));
        jp_Componentes.setPreferredSize(new java.awt.Dimension(1000, 700));
        jp_Componentes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabPane1.setBackground(new java.awt.Color(236, 243, 252));

        btn_siguiente.setText("...");
        btn_siguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_siguienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tabPane1Layout = new javax.swing.GroupLayout(tabPane1);
        tabPane1.setLayout(tabPane1Layout);
        tabPane1Layout.setHorizontalGroup(
            tabPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_Bienvenido, javax.swing.GroupLayout.DEFAULT_SIZE, 1005, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabPane1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_siguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69))
        );
        tabPane1Layout.setVerticalGroup(
            tabPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabPane1Layout.createSequentialGroup()
                .addComponent(lbl_Bienvenido, javax.swing.GroupLayout.PREFERRED_SIZE, 555, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_siguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(121, Short.MAX_VALUE))
        );

        tabPane.addTab("Inicio", tabPane1);

        paneTab2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_intro.setText(".");
        lbl_intro.setToolTipText("");
        paneTab2.add(lbl_intro, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, -1));

        btn_close.setText("Salir");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });
        paneTab2.add(btn_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 550, 120, 30));
        paneTab2.add(chbxNoshow, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 560, -1, -1));

        lbl_NoShow.setForeground(new java.awt.Color(153, 153, 153));
        lbl_NoShow.setText(".");
        lbl_NoShow.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        paneTab2.add(lbl_NoShow, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 580, 100, -1));

        lbl_NoShow1.setText("No volver a mostrar al inicio");
        lbl_NoShow1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        paneTab2.add(lbl_NoShow1, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 560, 220, -1));

        tabPane.addTab("Acerca de", paneTab2);

        jp_Componentes.add(tabPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1010, 740));
        tabPane.getAccessibleContext().setAccessibleName("Inicio");
        tabPane.getAccessibleContext().setAccessibleDescription("");

        jsp_Bienvenido.setViewportView(jp_Componentes);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jsp_Bienvenido, javax.swing.GroupLayout.PREFERRED_SIZE, 1008, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jsp_Bienvenido, javax.swing.GroupLayout.PREFERRED_SIZE, 739, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_closeActionPerformed
        Main main = (Main) this.getTopLevelAncestor(); // Trata de ir  al contenedor principal y activa o desactiva botones
        main.winbienvenido = false;

        if (chbxNoshow.isSelected()) {
            main.CargarBienvenido(true); // Deshabilitar esta ventana indefinidamente          
        }
        this.dispose();
    }//GEN-LAST:event_btn_closeActionPerformed

    private void btn_siguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_siguienteActionPerformed
        tabPane.setSelectedIndex(1);
    }//GEN-LAST:event_btn_siguienteActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_close;
    private javax.swing.JButton btn_siguiente;
    private javax.swing.JCheckBox chbxNoshow;
    private javax.swing.JPanel jp_Componentes;
    private javax.swing.JScrollPane jsp_Bienvenido;
    private javax.swing.JLabel lbl_Bienvenido;
    private javax.swing.JLabel lbl_NoShow;
    private javax.swing.JLabel lbl_NoShow1;
    private javax.swing.JLabel lbl_intro;
    private javax.swing.JPanel paneTab2;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JPanel tabPane1;
    // End of variables declaration//GEN-END:variables
}
