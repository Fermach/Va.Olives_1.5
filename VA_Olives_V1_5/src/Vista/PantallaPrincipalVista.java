package Vista;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;

import Controlador.Callback;
import Modelo.DAO.DAO;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

/**
 * 
 * @author DESARROLLO1
 *
 */
public class PantallaPrincipalVista extends JPanel {
	public JTable tabla_datos;
	private JTextPane Text_logs;
	private DAO dao;
	private DefaultTableModel modelo;
	private JScrollPane scrollPane ;
	private JButton btnLimpiarLogs;
	private JButton btnGuardar;
	

	private JScrollPane scrollPane_1;
	private JTextField textField_nombre_usuario;
	private JPasswordField passwordField;

	
	/**
	 * Se crea el panel con la pantalla
	 *  de log y la tabla de Txt leidos
	 */
	public PantallaPrincipalVista() {
		setBackground(new Color(143, 188, 143));
		
		inicializar();
	}
		
	/**
	 * Se inicializa la vista
	 */
	public void inicializar() {
		Font font = new Font("Courier", Font.BOLD,12);
		//setLayout(null);
	    Text_logs = new JTextPane();
	    Text_logs.setEditable(false);
	  
		JPanel panel = new JPanel();
		panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel.setBounds(10, 11, 879, 635);
		
		
		JLabel lblListadoDeImagenes = new JLabel("Listado de imagenes de pesdada recibidas:");
		lblListadoDeImagenes.setFont(new Font("Monospaced", Font.BOLD, 16));
		String[] titulos ={"LINEA", "CLOR1", "CLOR2", "CLOR3", "CLOR4","CLOR5", "AV.CLOR", "CLD1",
				 "CLD2", "CLD3", "CLD4","CLD5","AV.CLD"};
		String filas[] = new String[14] ;
		modelo= new DefaultTableModel(null, titulos);
		
		
		scrollPane = new JScrollPane();
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setAutoscrolls(false);
		/*scrollPane_1.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				// TODO Auto-generated method stub
		        e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
				
			}
		});*/
		
		DefaultCaret caret = (DefaultCaret)Text_logs.getCaret();
		caret.setUpdatePolicy(DefaultCaret.UPDATE_WHEN_ON_EDT);
		
		JLabel lblLogsDeProcesado = new JLabel("Logs de procesado de Tickets:");
		lblLogsDeProcesado.setFont(new Font("Monospaced", Font.BOLD, 16));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		JLabel lblConfiguracinDeLa = new JLabel("Configuraci\u00F3n BBDD:");
		lblConfiguracinDeLa.setFont(new Font("Monospaced", Font.BOLD, 12));
		
		JLabel lblNombreUsuario = new JLabel("Nombre Usuario:");
		lblNombreUsuario.setFont(new Font("Monospaced", Font.PLAIN, 12));
		
		JLabel lblContrasea = new JLabel("Contrase\u00F1a:");
		lblContrasea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		
		textField_nombre_usuario = new JTextField();
		textField_nombre_usuario.setColumns(10);
		
		btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnGuardar.setForeground(new Color(0, 51, 0));
		btnGuardar.setBackground(new Color(204, 204, 204));
		btnGuardar.setActionCommand("guardar");
		
		passwordField = new JPasswordField();
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(lblConfiguracinDeLa, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
							.addGap(29))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(lblNombreUsuario, GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(textField_nombre_usuario, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(22, Short.MAX_VALUE))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(lblContrasea, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
							.addGap(73))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(61)
							.addComponent(btnGuardar, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(24, Short.MAX_VALUE))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(passwordField)
							.addGap(24))))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addComponent(lblConfiguracinDeLa, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblNombreUsuario, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField_nombre_usuario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(14)
					.addComponent(lblContrasea)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
					.addComponent(btnGuardar)
					.addContainerGap())
		);
		panel_2.setLayout(gl_panel_2);
		
		btnLimpiarLogs = new JButton("Limpiar Logs");
		btnLimpiarLogs.setForeground(new Color(0, 51, 0));
		btnLimpiarLogs.setBackground(new Color(204, 204, 204));
		btnLimpiarLogs.setActionCommand("Limpiar Logs");
		btnLimpiarLogs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnLimpiarLogs, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(20, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnLimpiarLogs, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		
		scrollPane_1.setViewportView(Text_logs);
		
	
		tabla_datos = new JTable();
		scrollPane.setViewportView(tabla_datos);
		tabla_datos.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {"LINEA", "CLOR1", "CLOR2", "CLOR3", "CLOR4","CLOR5", "AV.CLOR", "CLD1",
					 "CLD2", "CLD3", "CLD4","CLD5","AV.CLD"}
		));
		tabla_datos.setEnabled(false);
		add(panel);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(20)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(lblListadoDeImagenes)
						.addComponent(lblLogsDeProcesado, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 547, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
								.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(scrollPane))
					.addContainerGap(24, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(11)
					.addComponent(lblListadoDeImagenes)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 226, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(lblLogsDeProcesado)
					.addGap(6)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
							.addGap(5)
							.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE))
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 291, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(12, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);

	}

	public JButton getBtnLimpiarLogs() {
		return btnLimpiarLogs;
	}

	public void setBtnLimpiarLogs(JButton btnNewButton) {
		this.btnLimpiarLogs = btnNewButton;
	}

	public JPasswordField getPasswordField() {
		return passwordField;
	}

	public void setPasswordField(JPasswordField passwordField) {
		this.passwordField = passwordField;
	}
	
	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	public JScrollPane getScrollPane_1() {
		return scrollPane_1;
	}

	public void setScrollPane_1(JScrollPane scrollPane_1) {
		this.scrollPane_1 = scrollPane_1;
	}

	public JButton getBtnGuardar() {
		return btnGuardar;
	}

	public void setBtnGuardar(JButton btnGuardar) {
		this.btnGuardar = btnGuardar;
	}

	public JTextField getTextField_nombre_usuario() {
		return textField_nombre_usuario;
	}

	public void setTextField_nombre_usuario(JTextField textField_nombre_usuario) {
		this.textField_nombre_usuario = textField_nombre_usuario;
	}

	public JTextPane getText_logs() {
		return Text_logs;
	}

	public void setText_logs(JTextPane text_logs) {
		Text_logs = text_logs;
	}

	public DAO getDao() {
		return dao;
	}

	public void setDao(DAO dao) {
		this.dao = dao;
	}

	public DefaultTableModel getModelo() {
		return modelo;
	}

	public void setModelo(DefaultTableModel modelo) {
		this.modelo = modelo;
	}
	


	public JTable getTabla_datos() {
		return tabla_datos;
	}

	public void setTabla_datos(JTable tabla_datos) {
		this.tabla_datos = tabla_datos;
	}
}
