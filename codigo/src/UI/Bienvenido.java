package UI;

import org.apache.log4j.Logger;
import Componentes.Configuracion;
import DB.Dao;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.ResultSet;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Bienvenido extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("Bienvenido"); //Nombre que tomará en el log.   
    private Dao db = new Dao(conf.pathreal + conf.separador + conf.ObtenerKey("rutabasedatos") + conf.separador + conf.ObtenerKey("nombrebasedatos"));

    public Bienvenido() {
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
 
            ResultSet rs;
            rs = db.ResultadoSelect("obtenermensajealias", "Bienvenido1");
            String txt = rs.getString("Mensaje");            
            String typ = rs.getString("Tipo");
            txt_Intro.setContentType(typ);
            txt_Intro.setText(txt);
            
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
        btn_close = new javax.swing.JButton();
        chbxNoshow = new javax.swing.JCheckBox();
        lbl_NoShow = new javax.swing.JLabel();
        lbl_NoShow1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt_Intro = new javax.swing.JTextPane();

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

        txt_Intro.setEditable(false);
        txt_Intro.setBackground(new java.awt.Color(240, 240, 240));
        txt_Intro.setBorder(null);
        jScrollPane1.setViewportView(txt_Intro);

        paneTab2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 610));

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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jp_Componentes;
    private javax.swing.JScrollPane jsp_Bienvenido;
    private javax.swing.JLabel lbl_Bienvenido;
    private javax.swing.JLabel lbl_NoShow;
    private javax.swing.JLabel lbl_NoShow1;
    private javax.swing.JPanel paneTab2;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JPanel tabPane1;
    private javax.swing.JTextPane txt_Intro;
    // End of variables declaration//GEN-END:variables
}
