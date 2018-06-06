package Vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Controlador.Controlador;
import Modelo.DAO.DAO;

/**
 * 
 * @author DESARROLLO1
 *
 */
public class Vista extends JFrame {

	private static JPanel contentPane=  new JPanel();
	static PantallaPrincipalVista pantallaPrincipalVista= new PantallaPrincipalVista();
	
	
	public static PantallaPrincipalVista getPantallaPrincipalVista() {
		return pantallaPrincipalVista;
	}

	public static void setPantallaPrincipalVista(PantallaPrincipalVista pantallaPrincipalVista) {
		Vista.pantallaPrincipalVista = pantallaPrincipalVista;
	}

	/**
	 * Se lanza la App
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Vista frame = new Vista();
	
					new Controlador(frame);
				
					frame.setVisible(true);
					contentPane.add(pantallaPrincipalVista);
					
						
			
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Se crea la vista principal donde se instancian la demás vistas
	 */
	public Vista() {
	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0,0, 905, 705);
		setResizable(false);
		
		
		this.setTitle("Va.Olives - Caracterización del Fruto");
		
			
		ImageIcon img = new ImageIcon("src/res/imatec_img.jpeg");
		this.setIconImage(img.getImage());
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
	
		
		setContentPane(contentPane);
	}


	
	

}
