package UI;

import BO.Bod;
import BO.Bod_Desarenador;
import BO.Bod_Lagunas;
import BO.Bod_MedParshall;
import BO.Bod_Rejillas;
import DB.Dao;
import java.io.File;
import Componentes.Util;
import javax.swing.JLabel;
import org.apache.log4j.Logger;
import Componentes.Validaciones;
import Componentes.Configuracion;
import Componentes.Listener_Popup;
import Componentes.Listener_Texto;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.ResultSet;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Resultado extends javax.swing.JInternalFrame {

    private Configuracion conf = new Configuracion();
    private static Logger log = Logger.getLogger("Resultado"); //Nombre que tomará en el log.
    private Dao db = new Dao(conf.ObtenerKey("rutabasedatos") + File.separator + conf.ObtenerKey("nombrebasedatos"));
    private Validaciones validar = new Validaciones();
    private Listener_Popup Lpopup;
    private Listener_Texto Ltexto;
    private Bod bod = new Bod("");
    private Bod_Lagunas bodla;
    private Bod_Rejillas bodre;
    private Bod_Desarenador bodda;
    private Bod_MedParshall bodmp;
    Util util = new Util();
    private String tecnologia;//Recibe que tecnología se debe cargar

    public Resultado(Bod bod, String tec) {
        this.bod = bod;
        initComponents();
        bod.WinResultado = true;
        this.tecnologia = tec;
        InicializarComponentes();
    }

    /**
     * Inicializa todos los elementos (jlabels,jtextbox,etc.) con sus
     * respectivos valores y textos traidos desde al base de datos.
     */
    public void InicializarComponentes() {

        ResultSet rs;
        try {

            btn_Guardar.setText("Exportar");
            btn_close.setText("Cerrar");
            //- - - - - - - - - - - - - - - - - - - Descripción de la sección- - - - - - - - - - - - - - - - - 
            rs = Resultado("obtenerseccion", "7", null);//TODO: DEJAR ESTOS NUMEROS "7" EN EL MAIN o usar letras para traer "LLA"

            lbl_Resultado_desc.setText(rs.getString("Nombre"));
            //- - - - - - - - - - - - - - - - - - - Titulo de la sección- - - - - - - - - - - - - - - - - - - -
            switch (tecnologia) {

                case "*PR": //Cargar Tratamiento Preliminar Rejillas // - - - - - - - - - - - - - - - - -  
                    rs = Resultado("obtenerpregunta", "7", "*PR");

                    lbl_Resultado_titulo1.setText(rs.getString("titulo1"));
                    lbl_Resultado_titulo2.setText(rs.getString("titulo2"));
                    AsignarPopup(lbl_Resultado_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 150);

                    bodre = new Bod_Rejillas();
                    lblImagen.setIcon(bodre.procesarImagen(bod.getRLCs(),bod.getRPS(),bod.getRPSs() , bod.getRIB() , bod.getRARs()+ "",bod.getRNB()+"",bod.getRNEs(),bod.getRPLs(),bod.getDP1()));
                    lblTabla.setText(bodre.tablaimagen);

                    //esta es una imagen de prueba
                    lblDerechos.setText(" © PTAR Diseño");
                    String R = bodre.rutaimagen.replace(".png", "_color.png");
                    BufferedImage imgR = ImageIO.read(new File(R));
                    ImageIcon iconLogoR = new ImageIcon(bodre.escalarImagen(imgR, 100, 82)); //Esta relacion de aspecto se debe tomar en un programa de diseño o calcularse
                    lblMiniatura.setIcon(iconLogoR);
                    
                    break;
 
                case "*PD": //Cargar Tratamiento Preliminar Desarenador // - - - - - - - - - - - - - - - - -  
                    rs = Resultado("obtenerpregunta", "7", "*PD");

                    lbl_Resultado_titulo1.setText(rs.getString("titulo1"));
                    lbl_Resultado_titulo2.setText(rs.getString("titulo2"));
                    AsignarPopup(lbl_Resultado_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 150);

                    bodda = new Bod_Desarenador(); 
                    lblImagen.setIcon(bodda.procesarImagen(bod.getDAHs() ,bod.getDABs(),bod.getDLDs(),bod.getDALs(),bod.getDUPs()));
                    lblTabla.setText(bodda.tablaimagen);                    

                    //esta es una imagen de prueba
                    lblDerechos.setText(" © PTAR Diseño");
                    String D = bodda.rutaimagen.replace(".png", "_color.png");
                    BufferedImage imgD = ImageIO.read(new File(D));
                    ImageIcon iconLogoD = new ImageIcon(bodda.escalarImagen(imgD, 100, 82)); //Esta relacion de aspecto se debe tomar en un programa de diseño o calcularse
                    lblMiniatura.setIcon(iconLogoD);
                    
                    break;
 
                case "*PP": //Cargar Tratamiento Preliminar Medidor Parshall // - - - - - - - - - - - - - - - - -  
                    rs = Resultado("obtenerpregunta", "7", "*PP");

                    lbl_Resultado_titulo1.setText(rs.getString("titulo1"));
                    lbl_Resultado_titulo2.setText(rs.getString("titulo2"));
                    AsignarPopup(lbl_Resultado_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 150);
                    

                    bodmp = new Bod_MedParshall(); 
                    lblImagen.setIcon(bodmp.procesarImagen(bod.getDPAs(),bod.getDPBs(),bod.getDPCs(),bod.getDPDs(),bod.getDPEs(),bod.getDPFs(),bod.getDPGs(),bod.getDPKs(),bod.getDPNs(),bod.getDP1s(),bod.getDANs()));
                    lblTabla.setText(bodmp.tablaimagen);                    

                    //esta es una imagen de prueba
                    lblDerechos.setText(" © PTAR Diseño");
                    String P = bodmp.rutaimagen.replace(".png", "_color.png");
                    BufferedImage imgP = ImageIO.read(new File(P));
                    ImageIcon iconLogoP = new ImageIcon(bodmp.escalarImagen(imgP, 100, 82)); //Esta relacion de aspecto se debe tomar en un programa de diseño o calcularse
                    lblMiniatura.setIcon(iconLogoP);

                     break;

                case "*LA": //Cargar Laguna Anaerobia
                    rs = Resultado("obtenerpregunta", "7", "*LA");

                    lbl_Resultado_titulo1.setText(rs.getString("titulo1"));
                    lbl_Resultado_titulo2.setText(rs.getString("titulo2"));
                    AsignarPopup(lbl_Resultado_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 150);
                    // - - - - - - - - - - - - - - - - - - - - -  - - - - - - - - - - - - - - - - - - - - - - - - - - - 

                    bodla = new Bod_Lagunas(bod.getLLA());
                    lblImagen.setIcon(bodla.procesarImagen(bod.getLAP(), bod.getLAB(), bod.getLAL(), bod.getLAU(), bod.getLAA(), bod.getLAI()));
                    lblTabla.setText(bodla.tablaimagen);

                    //esta es una imagen de prueba
                    lblDerechos.setText(" © PTAR Diseño");
                    String x = bodla.rutaimagen.replace(".png", "_color.png");
                    BufferedImage img1 = ImageIO.read(new File(x));
                    ImageIcon iconLogo1 = new ImageIcon(bodla.escalarImagen(img1, 100, 82)); //Esta relacion de aspecto se debe tomar en un programa de diseño o calcularse
                    lblMiniatura.setIcon(iconLogo1);

                    break;

                case "*LF": //Cargar Laguna Facultativa
                    rs = Resultado("obtenerpregunta", "7", "*LF");

                    lbl_Resultado_titulo1.setText(rs.getString("titulo1"));
                    lbl_Resultado_titulo2.setText(rs.getString("titulo2"));
                    AsignarPopup(lbl_Resultado_ayuda, rs.getString("ayuda"), rs.getString("ayudalink"), 400, 150);
                    // - - - - - - - - - - - - - - - - - - - - -  - - - - - - - - - - - - - -//Facultativa Usa las mismas imagenes y formaulas de Ananerobia

                    bodla = new Bod_Lagunas(1);// Facultativa solo es 1:3
                    lblImagen.setIcon(bodla.procesarImagen(bod.getFAP(), bod.getFAB(), bod.getFSL(), bod.getFCA(), bod.getFSA(), bod.getFAI()));
                    lblTabla.setText(bodla.tablaimagen);

                    //esta es una imagen de prueba
                    lblDerechos.setText(" © PTAR Diseño");
                    String y = bodla.rutaimagen.replace(".png", "_color.png");
                    BufferedImage img2 = ImageIO.read(new File(y));
                    ImageIcon iconLogo2 = new ImageIcon(bodla.escalarImagen(img2, 100, 82)); //Esta relacion de aspecto se debe tomar en un programa de diseño o calcularse
                    lblMiniatura.setIcon(iconLogo2);

                    break;
            }

        } catch (Exception ex) {
            log.error("Error en InicializarComponentes " + ex.getMessage());
            ErrorGeneral(); // Ha ocurrido algun error en los datos o conexión, se debe revisar log.
        } finally {
            db.close(); //Se cierra la conexiòn
        }
    }

    private ResultSet Resultado(String consulta, String param1, String param2) throws Exception {

        String select = conf.ObtenerKey(consulta);

        switch (consulta) {
            case "obtenerseccion":
                select = String.format(select, param1); //Cuestionario 1, pregunta 2
                break;
            case "obtenerpregunta":
                select = String.format(select, param1, param2); //Cuestionario 1, pregunta 1,2,3,....
                break;
        }

        ResultSet result = db.SelectSql(select);
        if (result == null) {
            throw new Exception("Error al traer Resultado = null," + consulta + "," + param1 + "," + param2);
        }
        return result;
    }

    /**
     * Deshabilita todos los componentes y muestra una advertencia al usuario
     */
    private void ErrorGeneral() {
        JOptionPane.showMessageDialog(null, "Ha ocurrido un error, revise el log de errores \n cierre la ventana", "Error al cargar componentes", JOptionPane.ERROR_MESSAGE);//Todo: 'intl'

        Component[] comp = jp_Resultado.getComponents();//Deshabilitar todos los Componentes del Jpane
        for (Component component : comp) {
            component.setEnabled(false);
        }
        btn_close.setEnabled(true);
    }

    private void AsignarPopup(JLabel lbl, String mensaje, String uri, int dx, int dy) {
        Lpopup = new Listener_Popup();
        Lpopup.AgregarMensajePopup(lbl, mensaje, uri, dx, dy);
    }

    /**
     * *
     * Imprime lo que hay dentro del Jpanel y lo exporta como PNG
     */
    private void imprimirImagen() {

        String ruta = util.CrearArchivo();
        if (ruta.equals("")) {
            return;
        }
        try {
            BufferedImage image = new BufferedImage(jp_Resultado.getWidth(), jp_Resultado.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            jp_Resultado.printAll(g);
            g.dispose();

            if (!util.obtenerExtension(ruta, ".png")) {
                ruta = ruta.trim() + ".png";
            }

            ImageIO.write(image, "png", new File(ruta));//ImageIO.write(image, "jpg", new File(ruta)); Baja calidad!

            util.Mensaje("Archivo guardado", "ok");
        } catch (IOException exp) {
            util.Mensaje("Error al crear archivo", "error");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jp_Resultado = new javax.swing.JPanel();
        lblImagen = new javax.swing.JLabel();
        lblTabla = new javax.swing.JLabel();
        lblMiniatura = new javax.swing.JLabel();
        lblDerechos = new javax.swing.JLabel();
        jp_Componentes_Resultado = new javax.swing.JPanel();
        btn_Guardar = new javax.swing.JButton();
        btn_close = new javax.swing.JButton();
        lbl_Resultado_titulo1 = new javax.swing.JLabel();
        lbl_Resultado_desc = new javax.swing.JLabel();
        lbl_Resultado_ayuda = new javax.swing.JLabel();
        lbl_Resultado_titulo2 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1024, 768));

        jp_Resultado.setBackground(new java.awt.Color(255, 255, 255));
        jp_Resultado.setPreferredSize(new java.awt.Dimension(1024, 680));

        lblImagen.setBackground(new java.awt.Color(204, 204, 255));
        lblImagen.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        lblTabla.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTabla.setText(" ");
        lblTabla.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblTabla.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jp_ResultadoLayout = new javax.swing.GroupLayout(jp_Resultado);
        jp_Resultado.setLayout(jp_ResultadoLayout);
        jp_ResultadoLayout.setHorizontalGroup(
            jp_ResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_ResultadoLayout.createSequentialGroup()
                .addGroup(jp_ResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMiniatura, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                    .addComponent(lblTabla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblDerechos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 889, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jp_ResultadoLayout.setVerticalGroup(
            jp_ResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_ResultadoLayout.createSequentialGroup()
                .addGroup(jp_ResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblImagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jp_ResultadoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblMiniatura, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTabla, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDerechos, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jp_Componentes_Resultado.setBackground(new java.awt.Color(255, 255, 255));

        btn_Guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Button_Exportar_21.png"))); // NOI18N
        btn_Guardar.setText("Exportar");
        btn_Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GuardarActionPerformed(evt);
            }
        });

        btn_close.setText("Salir");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });

        lbl_Resultado_titulo1.setText("Titulo");
        lbl_Resultado_titulo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_Resultado_desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_Resultado_desc.setForeground(new java.awt.Color(51, 51, 51));
        lbl_Resultado_desc.setText("Desc");
        lbl_Resultado_desc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_Resultado_ayuda.setBackground(new java.awt.Color(153, 195, 115));
        lbl_Resultado_ayuda.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_Resultado_ayuda.setForeground(new java.awt.Color(0, 102, 102));
        lbl_Resultado_ayuda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Resultado_ayuda.setText("?");
        lbl_Resultado_ayuda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 51), 2));
        lbl_Resultado_ayuda.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbl_Resultado_ayuda.setFocusable(false);
        lbl_Resultado_ayuda.setOpaque(true);
        lbl_Resultado_ayuda.setRequestFocusEnabled(false);

        lbl_Resultado_titulo2.setText("Titulo");
        lbl_Resultado_titulo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout jp_Componentes_ResultadoLayout = new javax.swing.GroupLayout(jp_Componentes_Resultado);
        jp_Componentes_Resultado.setLayout(jp_Componentes_ResultadoLayout);
        jp_Componentes_ResultadoLayout.setHorizontalGroup(
            jp_Componentes_ResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_Componentes_ResultadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jp_Componentes_ResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jp_Componentes_ResultadoLayout.createSequentialGroup()
                        .addComponent(btn_Guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_close, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lbl_Resultado_desc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jp_Componentes_ResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_Componentes_ResultadoLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lbl_Resultado_titulo2, javax.swing.GroupLayout.PREFERRED_SIZE, 511, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbl_Resultado_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jp_Componentes_ResultadoLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lbl_Resultado_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jp_Componentes_ResultadoLayout.setVerticalGroup(
            jp_Componentes_ResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_Componentes_ResultadoLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jp_Componentes_ResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_Resultado_desc)
                    .addComponent(lbl_Resultado_titulo1))
                .addGap(8, 8, 8)
                .addGroup(jp_Componentes_ResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_Componentes_ResultadoLayout.createSequentialGroup()
                        .addGroup(jp_Componentes_ResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_Guardar)
                            .addComponent(btn_close, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_Resultado_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 2, Short.MAX_VALUE))
                    .addGroup(jp_Componentes_ResultadoLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(lbl_Resultado_titulo2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jp_Resultado, javax.swing.GroupLayout.DEFAULT_SIZE, 1018, Short.MAX_VALUE)
            .addComponent(jp_Componentes_Resultado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jp_Resultado, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jp_Componentes_Resultado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_closeActionPerformed
        bod.WinResultado = false;
        this.dispose();
    }//GEN-LAST:event_btn_closeActionPerformed

    private void btn_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GuardarActionPerformed
        imprimirImagen();

    }//GEN-LAST:event_btn_GuardarActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Guardar;
    private javax.swing.JButton btn_close;
    private javax.swing.JPanel jp_Componentes_Resultado;
    private javax.swing.JPanel jp_Resultado;
    private javax.swing.JLabel lblDerechos;
    private javax.swing.JLabel lblImagen;
    private javax.swing.JLabel lblMiniatura;
    private javax.swing.JLabel lblTabla;
    private javax.swing.JLabel lbl_Resultado_ayuda;
    private javax.swing.JLabel lbl_Resultado_desc;
    private javax.swing.JLabel lbl_Resultado_titulo1;
    private javax.swing.JLabel lbl_Resultado_titulo2;
    // End of variables declaration//GEN-END:variables
}
