package Modelo.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Controlador.Controlador;
import Controlador.Callback;
/**
 * 
 * @author DESARROLLO1
 *
 */
public class Conexion {

     private Log log;
	private static Connection conexion = null;
	private Callback callback;

	/**
	 * Conexión con la Base de datos Access
	 */
	 private Conexion(String ruta,String usuario, String pass) {
	    try {
	    	conexion= null;
	     	callback= new Controlador();
	    	log= new Log();
	    	//System.out.println("PARAMETROS: "+ usuario+", "+ pass);
	        conexion = DriverManager.getConnection("jdbc:ucanaccess://"+ruta+"",
	        		usuario, pass);
	     
	        System.out.println("CONEXION REALIZADA CON EXITO: "+ usuario);
	        
		    callback.addLog("- "+log.obtenerFechaHoraActual().get(2)+"   - CONEXION REALIZADA CON EXITO: "+ usuario);
	        log.guardarLogs("- "+log.obtenerFechaHoraActual().get(2)+"   - CONEXION REALIZADA CON EXITO: "+ usuario);
	        
	    } catch (SQLException sql) {
	    //	callback.actualizarUsuarioContras(usuario, pass);
	   // 	callback.actualizarboton();
	        System.out.println("ERROR AL CONECTARSE A LA BASE DE DATOS "+ sql);
		    callback.addLog("- "+ log.obtenerFechaHoraActual().get(2)+"   ERROR AL CONECTARSE A LA BASE DE DATOS: "+ sql);
	        log.guardarLogs("- "+ log.obtenerFechaHoraActual().get(2)+"   ERROR AL CONECTARSE A LA BASE DE DATOS: "+ sql);
	    }

	 }

	
	/**
	  * Con este método obtenemos una única instancia de la conexión con nuestra BBDD
	  * @return
	  */
 	public static Connection getInstance(String ruta,String usuario, String pass){
 		
		if (conexion == null){
		    	
			new Conexion(ruta, usuario, pass);
			System.out.println("SE HA CREADO UNA NUEVA CONEXIÓN CON LA BBDD \n\n");
			
			
		}else {
			try {
				conexion.close();
				new Conexion(ruta, usuario, pass);
				System.out.println("SE HA CREADO UNA NUEVA CONEXIÓN CON LA BBDD \n\n");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return conexion;
		
	}
 	
 	
 
 	
	/*
	public static void main(String[] args) {
		Connection conexion= Conexion.getInstance();
	}
	*/
}
