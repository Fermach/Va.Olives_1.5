package Modelo.DAO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import Controlador.Controlador;
import Controlador.Callback;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import Modelo.DTO.ImagenFrutoPesada;
import Vista.PantallaPrincipalVista;
import at.jta.Key;
import at.jta.RegistryErrorException;
import at.jta.Regor;

/**
 * Data Object Access
 * @author DESARROLLO1
 *
 */
public class DAO implements IDAO {
	
	private static DAO INSTANCE= null;
	private String[] ficheros;
	private File dir;
	private String[] listaDatos;
	private Regor reg = null;
	private Key key = null;
	private String fileName;
	private Encriptador encriptador;
	private PrintWriter out = null;
	private Callback callback;
	private ArrayList<String> datosConfiguracion;
	private List<ImagenFrutoPesada> listaImagenesLeidas;
	
	//RUTAS
	private String DirectorioDatosRecibidos;
	private final String PathFicheroDatos= "src/../config.txt";
	private final String DirectorioDatosProcesados= "src/../Registros Procesados/";
	private final String RegistroPath = "Software\\VB and VBA Program Settings\\pesaje\\";
	private final String logsPath = "src/../Logs/";
	private final String SBK = "Software\\MySoftware";
	
	//ACCESS
	private Connection conex;
	private Statement state;
	private PreparedStatement pre;
	
	/**
	 * Método que retorna una sola instancia de la clase DAO
	 * @return
	 */
	public static DAO getInstance() {
		 if (INSTANCE == null) {
			
	            INSTANCE= new DAO();
	      }
		 
		 return INSTANCE;
	}
	
	private DAO()  {
	    
		datosConfiguracion= obtenerDatosArranque();	
	    
		encriptador= new Encriptador();
	 
		
		//obtenerDatosArranque();
		DirectorioDatosRecibidos= datosConfiguracion.get(0);
      
		
		dir = new File(DirectorioDatosRecibidos);
		
		conex= Conexion.getInstance(datosConfiguracion.get(1),datosConfiguracion.get(3), encriptador.desenciptarCadena(datosConfiguracion.get(4)));
		out=null;
		
		//nombre del archivo donde se van a guardar los logs de esta instancia con la fecha y hora actuales
		fileName="log_"+obtenerFechaHoraActual().get(0)+".txt";
		listaImagenesLeidas= new ArrayList<>();
		callback= new Controlador();
		
		
	}
	
	/**
	 * Se obtienen los datos del fichero de configuración
	 * @return
	 */
	private ArrayList<String> obtenerDatosArranque(){
		ArrayList<String>  datos= new ArrayList<>();
		
		String cadena;
	    FileReader f = null;
		try {
			f = new FileReader(PathFicheroDatos);
			
			BufferedReader b = new BufferedReader(f);
		      try {
		    	  int e=0;
				while((cadena = b.readLine())!=null) {
					  int in = cadena.indexOf("'");
					  int end = cadena.lastIndexOf("'");
					  
					  String dato;
					  if (in != -1) 
					  {
					      dato= cadena.substring(in+1 , end); 
					      datos.add(e,dato);
					      e++;
					  }
					  
					  in= cadena.indexOf("#");
					  end= cadena.lastIndexOf("#");
					  System.out.println(in+", "+end);
				      if(in!=-1) {
				    	  dato= cadena.substring((in+1) , end); 
					      datos.add(e,dato);
					      e++;
				      }
					  
				  }
			    b.close();
			    System.out.println("- Datos de configuración leidos correctamente: "+datos.toString());
			    guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - Datos de configuración leidos correctamente ");
			    
			} catch (IOException e) {
				System.out.println("- ERROR No se pudo leer correctamente fichero de configuración");
			    guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - ERROR No se pudo leer correctamente el fichero de configuración");
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			
			System.out.println("- ERROR No se pudo acceder al fichero de configuración: "+PathFicheroDatos);
		    guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - ERROR No se pudo acceder al fichero de configuración: "+ PathFicheroDatos);
		    e.printStackTrace();

		}
	     
	    
		
		return datos;
	}  
	
	/**
	 * Este método hace un PING a la conexión, si los datos son validos y conecta,
	 * sobreescribe el archivo de configuración, con los datos de la conexion,
	 * si no conecta muestra un mensaje de ERROR
	 * @param usuario
	 * @param pass
	 */
	public void guardarUsuarioYPass(String usuario, String pass) {
		
		
		//datosConfiguracion= obtenerDatosArranque();
		if(Conexion.getInstance(datosConfiguracion.get(1), usuario, pass)==null){
			 System.out.println("ERROR AL CONECTARSE A LA BASE DE DATOS ");
			    callback.addLog("- "+ obtenerFechaHoraActual().get(2)+"   - ERROR AL CONECTARSE A LA BASE DE DATOS ");
		        guardarLogs("- "+ obtenerFechaHoraActual().get(2)+"   - ERROR AL CONECTARSE A LA BASE DE DATOS ");
		}else {
			 System.out.println("CONEXION REALIZADA CON ÉXITO!!");
			    callback.addLog("- "+ obtenerFechaHoraActual().get(2)+"  - CONEXION REALIZADA CON ÉXITO!! ");
		        guardarLogs("- "+ obtenerFechaHoraActual().get(2)+"  - CONEXION REALIZADA CON ÉXITO!! ");
		//guardar datos
         ArrayList<String>  datos= new ArrayList<>();
		
		String cadena;
	    FileReader f = null;
		boolean usuarioinsertado=false;
		
			try {
			
				f = new FileReader(PathFicheroDatos);
				BufferedReader b = new BufferedReader(f);

				try {
					while((cadena = b.readLine())!=null) {
						  int in = cadena.indexOf("#");
						  int end = cadena.lastIndexOf("#");
						  
						  if (in != -1)  {
							  
							  if(!usuarioinsertado) {
						       cadena= cadena.replace(cadena.substring(in+1, end), usuario); 
						       usuarioinsertado=true;
							  }
							  else {
							   cadena= cadena.replace(cadena.substring(in+1, end), encriptador.encriptarCadena(pass)); 
  
							  }
						  }
					      datos.add(cadena);
						
					
                      }
					
					BufferedWriter outputWriter = new BufferedWriter(new FileWriter(PathFicheroDatos,false));
					  for (String linea: datos ) {
					    outputWriter.write(linea);
					    outputWriter.newLine();
					  }
					  outputWriter.flush();  
					  outputWriter.close(); 
					  
					  
					  datosConfiguracion= obtenerDatosArranque();
					    
					    System.out.println("Datos de arranque 2: "+datosConfiguracion.toString());
					    conex= Conexion.getInstance(datosConfiguracion.get(1),datosConfiguracion.get(3), encriptador.desenciptarCadena(datosConfiguracion.get(4)));

				} catch (IOException e) {
					// TODO Auto-generated catch block
					//callback.actualizarUsuarioContras(obtenerUsuarioContras().get(0),obtenerUsuarioContras().get(1));
                    callback.actualizarboton();
					e.printStackTrace();
				    guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - ERROR al gurdar los datos usuario/contraseña: "+ e.getMessage());

				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				//callback.actualizarUsuarioContras(obtenerUsuarioContras().get(0),obtenerUsuarioContras().get(1));
                callback.actualizarboton();
				e.printStackTrace();
			    guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - ERROR al gurdar los datos usuario/contraseña: "+ e.getMessage());

			}
			
		}
		//	callback.actualizarUsuarioContras(datosConfiguracion.get(3),datosConfiguracion.get(4));
        //    callback.actualizarboton();
      
				
		
	}
	/**
	 * 	Se obtiene el numero de basculas de la planta
	 * @return
	 */
	public int obtenerNumeroBascular() {
		
		return Integer.parseInt(datosConfiguracion.get(2));
		
	}
	
	/**
	 * Se obtienen los datos de accedo a nuestra BBDD
	 * @return
	 */
	public ArrayList<String> obtenerUsuarioContras(){
		ArrayList<String> lista = new ArrayList<>();
		
		System.out.println(datosConfiguracion.get(3));
		lista.add(0,datosConfiguracion.get(3));
		lista.add(1,encriptador.desenciptarCadena(datosConfiguracion.get(4)));

		return lista;
	}
	
	public void addLogs(String log) {
		callback.addLog(log);
	}
	
	/**
	 * Este método nos devuelve la fecha y la hora actuales 
	 * @return
	 */
	public ArrayList<String> obtenerFechaHoraActual() {
		String fecha;
		String hora;
		String hora_2;
		
		ArrayList<String> fecha_hora= new ArrayList<>(3) ;
		DateTimeFormatter dtf_fecha = DateTimeFormatter.ofPattern("yyyyMMdd");
		DateTimeFormatter dtf_hora = DateTimeFormatter.ofPattern("HHmmss");
		DateTimeFormatter dtf_hora_2 = DateTimeFormatter.ofPattern("HH:mm:ss");

		LocalDate localDate = LocalDate.now();
		LocalTime localTime= LocalTime.now();
		
		
		//System.out.println("FECHA/HORA ACTUAL: "+dtf_fecha.format(localDate)+", "+dtf_hora.format(localTime)); 
	    fecha= ""+dtf_fecha.format(localDate);
	    hora=""+dtf_hora.format(localTime);
	    hora_2=""+dtf_hora_2.format(localTime);
		
	    fecha_hora.add(0,fecha);
	    fecha_hora.add(1,hora);
	    fecha_hora.add(2,hora_2);
		
		return fecha_hora;
	}

	
	@Override
	public List<ImagenFrutoPesada> listaImagenesPesadaRecibidas() {
		// TODO Auto-generated method stub
		listaImagenesLeidas= new ArrayList<>();
     
	    System.out.println("\n\nLEYENDO FICHEROS TXT...");
	    callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   -LEYENDO FICHEROS TXT...");
	    guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   -LEYENDO FICHEROS TXT...");
	    String[] ficheros = dir.list();
		if(ficheros==null) {
			System.out.println("No existen ficheros de datos Recibidos en este momento!");
			callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   - ERROR No existen ficheros de datos Recibidos en este momento!");
		    guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - ERROR No existen ficheros de datos Recibidos en este momento!");

		}
		else {
			for(int i =0;i<ficheros.length;i++) {
				System.out.println("- Nombre del fichero: "+ficheros[i]);
				//callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   - Nombre del fichero: "+ficheros[i]);
				guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - Nombre del fichero: "+ficheros[i]);

				try {
					listaImagenesLeidas.add(leerTXTimagen(ficheros[i]));
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("- No fue posible leer el fichero "+ficheros[i]+" correctamente");
					callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   - No fue posible leer el fichero "+ficheros[i]+" correctamente");
			    	guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - No fue posible leer el fichero "+ficheros[i]+" correctamente");
				}
				
			}
			
		}
		System.out.println(listaImagenesLeidas.size());
		//callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   "+listaImagenesLeidas.size());
		guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   "+listaImagenesLeidas.size());
		
		return listaImagenesLeidas;
	}

	@Override
	public boolean insertarImagenesRecibidasEnBBDD(ImagenFrutoPesada frutoPesada, String num_ticket) {
		// TODO Auto-generated method stub
		boolean exito = false;
		try {
			if(conex.isClosed()) {
				conex= Conexion.getInstance(datosConfiguracion.get(1),datosConfiguracion.get(3), encriptador.desenciptarCadena(datosConfiguracion.get(4)));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		 System.out.println("++++ Insertando imagen: "+ frutoPesada.toString() +" ++++ \n");
			callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   ++++ Insertando imagen: "+ frutoPesada.toString() +"  ++++ \n");
			guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   ++++ Insertando imagen: "+ frutoPesada.toString() +" ++++ \n");

		final String sql="UPDATE tickets"
				+ " SET dato3= ? , dato4= ?, dato5= ?, dato6= ?"
				+ " WHERE numticket= ?";
		
		String dato3= ""+frutoPesada.getDato1()+", " +frutoPesada.getDato2()+", "+frutoPesada.getDato3()+", "+frutoPesada.getDato4()+", "+frutoPesada.getDato5();
		String dato4= ""+frutoPesada.getDato6();
		String dato5= ""+frutoPesada.getDato7()+", "+ frutoPesada.getDato8()+", "+frutoPesada.getDato9()+", "+frutoPesada.getDato10()+", "+frutoPesada.getDato11();
		String dato6= ""+frutoPesada.getDato12();

			try {
				pre = conex.prepareStatement(sql);
				pre.setString(1, dato3);
				pre.setString(2, dato4);
				pre.setString(3, dato5);
				pre.setString(4, dato6);
				pre.setString(5, num_ticket);
				
				int resultado = pre.executeUpdate();
				if (resultado != 0){
					exito = true;
				    System.out.println("++++ Imagen de Fruto añadida correctamente a la BBDD! ++++ \n"
				    		+ "++++ "+ frutoPesada.toString()+" ++++");
					callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   ++++ Imagen de Fruto añadida correctamente a la BBDD! ++++ \n"
				    		+ "++++ "+ frutoPesada.toString()+" ++++");
					guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   ++++ Imagen de Fruto añadida correctamente a la BBDD! ++++ \n"
				    		+ "++++ "+ frutoPesada.toString()+" ++++");

				}
			} catch (SQLException e) {
				
				System.out.println("- ERROR en la insercción de datos de la BBDD!");
				callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   - ERROR en la insercción de datos de la BBDD!");
				guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - ERROR en la insercción de datos de la BBDD!");
			}
		
		return exito;
			
	}
	
	@Override
	public String obtenerUltimoNumeroTicketBBDD(String linea) {
		// TODO Auto-generated method stub
		String num_ticket ="SIN RESULTADO";
		try {
			if(conex.isClosed()) {
				conex= Conexion.getInstance(datosConfiguracion.get(1),datosConfiguracion.get(3), encriptador.desenciptarCadena(datosConfiguracion.get(4)));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		final String sql="SELECT MAX(numticket) "
				+ "FROM tickets "
				+ "WHERE basculaentrada=  "+ linea;
		
	    try {
			state= conex.createStatement();
		    ResultSet resultSet= state.executeQuery(sql);
		    
		    while(resultSet.next()) {
		    	num_ticket=""+resultSet.getString(1);
		    	System.out.println("++++ Número de Ticket: "+num_ticket+" ++++ ");
		    	callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   ++++ Número de Ticket: "+num_ticket+" ++++ ");
		    	guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   ++++ Número de Ticket: "+num_ticket+" ++++ ");
		    }

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
    		callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   - ERROR: \n"+ e.getStackTrace()  +"\n");
    		guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - ERROR: \n"+ e.getStackTrace()  +"\n");

		}
	    
	  
		return num_ticket;
	}

	
	/**
	 * Este método lee la ruta donde se guardan los tickets y devuelve un objeto de tipo ImagenFrutoPesada
	 * con los datos en ese ticket
	 * @param archivo
	 * @return
	 * @throws IOException
	 */
	private ImagenFrutoPesada leerTXTimagen(String archivo) throws IOException {
		ImagenFrutoPesada imagenFrutoPesada= new ImagenFrutoPesada();  
		
		String cadena;
	      FileReader f = new FileReader(DirectorioDatosRecibidos+"/"+archivo);
	      BufferedReader b = new BufferedReader(f);
	      while((cadena = b.readLine())!=null) {
	          System.out.println("     Cadena de datos leida en "+archivo+": "+cadena);
	         // callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   - Cadena de datos leida en "+archivo+": "+cadena);
	          guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - Cadena de datos leida en "+archivo+": "+cadena);
	          listaDatos= cadena.split(",");
	          System.out.println("     Cantidad de datos leidos en documento "+archivo+"= "+listaDatos.length);
	          //callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   - Cantidad de datos leidos en documento "+archivo+"= "+listaDatos.length);
	          guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - Cantidad de datos leidos en documento "+archivo+"= "+listaDatos.length);
	          if(listaDatos.length==12) {
	        	  
	        	  imagenFrutoPesada= new ImagenFrutoPesada(archivo.substring(1,2),archivo.substring(2,6),listaDatos[0],listaDatos[1],listaDatos[2],listaDatos[3],listaDatos[4],listaDatos[5],
	        			  listaDatos[6],listaDatos[7],listaDatos[8],listaDatos[9],listaDatos[10],listaDatos[11]);
	        	  
	          }else {
	        	  System.out.println("Archivo Incompleto, faltan datos en el documento recibido");
		          callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   - Archivo Incompleto, faltan datos en el documento recibido "+ archivo+": "+cadena);

		          guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - Archivo Incompleto, faltan datos en el documento recibido");
	        	  return null;
	          }
	          
	      }
	      b.close();
	      
	      return imagenFrutoPesada;
	}

	@Override
	public String obtenerValorClavePesada(String bascula) throws RegistryErrorException {
	    
		String estadopesada="SIN ESTADO";
		reg= new Regor();
		
		
		if(reg.openKey(Regor.HKEY_CURRENT_USER, RegistroPath) !=null) {
			 key = reg.openKey(Regor.HKEY_CURRENT_USER, RegistroPath+bascula);
			 if(key!=null) {
				 if(reg.readValueAsString(key, "estadopesada")!=null) {
				   estadopesada= reg.readValueAsString(key, "estadopesada"); 
				 }else {
					 System.out.println("- ERROR el valor de la clave estadopesada de "+bascula+" es nulo");
				        callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   - ERROR el valor de la clave estadopesada de "+bascula+" es nulo");
				        guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - ERROR el valor de la clave estadopesada de "+bascula+" es nulo");

				 }
			 }else {
				System.out.println("- ERROR en la conexión con el registro (CLAVE) en "+bascula);
		        callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   - ERROR en la conexión con el registro (CLAVE) en "+bascula);
		        guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - ERROR en la conexión con el registro (CLAVE) en "+bascula);

			 }
			
		}else {
			System.out.println("- ERROR en la conexión con el registro (RUTA) en "+bascula);
	        callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   - ERROR en la conexión con el registro (RUTA) en "+bascula);
	        guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - ERROR en la conexión con el registro (RUTA) en "+bascula);

		}

		return estadopesada;

	}

	@Override
	public boolean guardarLogs(String log) {
		// TODO Auto-generated method stub
        boolean exito=false;
		File file = new File(logsPath);
		File file_logs = new File(logsPath+fileName);
		// if file doesn't exists, then create it
		if (!file.exists()) {
			file.mkdirs();
			
			
		}    
		
		if(!file_logs.exists()) {
			  try {
				file_logs.createNewFile();
			  } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			  }
		}
		try {
			    
				out = new PrintWriter(new BufferedWriter(new FileWriter(file_logs, true)));
				out.println(log);
				exito=true;
				//fileWriter.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	    		callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   - ERROR: \n"+ e.getStackTrace()  +"\n");
	    		guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - ERROR: \n"+ e.getStackTrace()  +"\n");

				exito=false;
			}finally {
				 if(out != null){
				       out.close();
				 }
			}
		
		return exito;
	}

	@Override
	public boolean moverTxtAprocesados(ImagenFrutoPesada frutoPesada) {
		// TODO Auto-generated method stub
		  boolean exito=false;
		  File mfile = new File(DirectorioDatosProcesados);
		  String name_file= "L"+frutoPesada.getLinea()+frutoPesada.getNum_ticket()+
				  "_"+obtenerFechaHoraActual().get(0)+obtenerFechaHoraActual().get(1)+".txt";
		  File mfile_path = new File(DirectorioDatosProcesados+name_file);
		  File file_logs = new File(logsPath+fileName);
			// if file doesn't exists, then create it
		  if (!mfile.exists()) {
				mfile.mkdirs();
				
				
			}
		  
		  if (!mfile_path.exists()) {
				try {
					mfile_path.createNewFile();
					System.out.println("- "+obtenerFechaHoraActual().get(2)+"   ++++ Creando archivo en  "+mfile.getPath()+" ++++");
			        callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   ++++ Creando archivo en  "+mfile.getPath()+" ++++");
			        guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   ++++ Creando archivo en  "+mfile.getPath()+" ++++");
				} catch (IOException e) {
					
					e.printStackTrace();
					callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   - ERROR: \n"+ e.getStackTrace() +"\n");
					guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - ERROR: \n"+ e.getStackTrace() +"\n");
				}
			}
		  try {
				String datosProcesados= ""+frutoPesada.getDato1()+", "+frutoPesada.getDato2()+", "+frutoPesada.getDato3()+", "+frutoPesada.getDato4()+", "+frutoPesada.getDato5()+", "+frutoPesada.getDato6()+", "+frutoPesada.getDato7()+", "
                      +frutoPesada.getDato8()+", "+frutoPesada.getDato9()+", "+frutoPesada.getDato10()+", "+frutoPesada.getDato11()+", "+frutoPesada.getDato12();
				
				
		        System.out.println("++++ Datos Procesados: "+ datosProcesados+" ++++" );
		        callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   ++++ Datos Procesados: "+ datosProcesados+" ++++" );
		        guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   ++++ Datos Procesados: "+ datosProcesados+" ++++");
		        
		        PrintStream mOut = new PrintStream(new FileOutputStream(mfile_path.getPath()));
		        mOut.print(datosProcesados);
				
				
				System.out.println("++++ Guardando los datos del fruto procesado en "+mfile_path.getPath()+" ++++");
		        callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   ++++ Guardando los datos del fruto procesado en "+ mfile_path.getPath()+" ++++");
		        guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   ++++ Guardando los datos del fruto procesado en "+ mfile_path.getPath()+" ++++");
		        
		        String nombreArchivo_Eliminar="0"+frutoPesada.getLinea()+frutoPesada.getNum_ticket()+".txt";
				File file2= new File(DirectorioDatosRecibidos+"/"+nombreArchivo_Eliminar);
			
				if(eliminarArchivo(file2)) {
					exito=true;

				}else {
					
					eliminarArchivo(mfile);
				}
				
				
				//fileWriter.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   - ERROR: \n"+ e.getStackTrace()  +"\n");
				guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - ERROR: \n"+ e.getStackTrace()  +"\n");
				exito=false;
			}finally {
				 if(out != null){
				       out.close();
				 }
			}
		
		return exito;
		  
			
		
	}

	/**
	 * Este método elimina un achivo en la ruta que le pasamos commo parámetro
	 * @param archivo
	 * @return
	 */
	private boolean eliminarArchivo(File archivo) {
		// TODO Auto-generated method stub
		boolean exito= false;
	
         try{
        	
    		if(archivo.delete()){
    			System.out.println("++++ Eliminando los datos del fruto procesado en "+ archivo.getPath()+" ++++");
    			callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   ++++ Eliminando los datos del fruto procesado en "+ archivo.getPath()+" ++++");
    			guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   ++++ Eliminando los datos del fruto procesado en "+ archivo.getPath()+" ++++");
    			exito=true;
    		}else{
    		    System.out.println("- ERROR No fue posible eliminar el archivo procesado: "+ archivo.getName());
    		    callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   - ERROR No fue posible eliminar el archivo procesado: "+ archivo.getName());
    		    guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - ERROR No fue posible eliminar el archivo procesado: "+ archivo.getName());
    		}
    	   
    	}catch(Exception e){
    		
    		e.printStackTrace();
    		callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   - ERROR: \n"+ e.getStackTrace()  +"\n");
    		guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - ERROR: \n"+ e.getStackTrace()  +"\n");
    	}
		
		
		return exito;
	}

	@Override
	public boolean CambiarClaveEnRegistroLineaProcesada(int linea) {

		try {
			reg= new Regor();
		} catch (RegistryErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - ERROR en la conexión con el registro: "+e.getMessage());

		}
		
		
		try {
			if(reg.openKey(Regor.HKEY_CURRENT_USER, RegistroPath) !=null) {
				 key = reg.openKey(Regor.HKEY_CURRENT_USER, RegistroPath+"bascula"+linea);
				 if(key!=null) {
					 if(reg.readValueAsString(key, "fruto_pesada_procesado")!=null) {
					    reg.setValue(key, "fruto_pesada_procesado", "true"); 
					    
					    
					    
					 }else {
						 reg.createKey(key, "fruto_pesada_procesado");
						 reg.setValue(key, "fruto_pesada_procesado", "true"); 

						 
						 System.out.println("- Se ha actualizado el valor de la clave fruto_pesada_procesado a true");
					     callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   - Se ha actualizado el valor de la clave fruto_pesada_procesado a true");
					     guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - Se ha actualizado el valor de la clave fruto_pesada_procesado a true");

					 }
				 }else {
					System.out.println("- ERROR en la conexión con el registro (CLAVE) en fruto_pesada_procesado en linea "+linea );
			        callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   - ERROR en la conexión con el registro (CLAVE) en fruto_pesada_procesado en linea "+linea);
			        guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - ERROR en la conexión con el registro (CLAVE) en fruto_pesada_procesado en linea "+linea);

				 }
				
			}else {
				System.out.println("- ERROR en la conexión con el registro (RUTA) en "+ RegistroPath);
			    callback.addLog("- "+obtenerFechaHoraActual().get(2)+"   - ERROR en la conexión con el registro (RUTA) en "+RegistroPath);
			    guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - ERROR en la conexión con el registro (RUTA) en "+RegistroPath);

			}
		} catch (RegistryErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    guardarLogs("- "+obtenerFechaHoraActual().get(2)+"   - ERROR en la conexión con el registro: "+e.getMessage());

		}
		
		
		return false;
	}

	
	
	/*
	public static void main(String[] args) throws RegistryErrorException {
		System.out.println(DAO.getInstance().obtenerValorClavePesada("bascula2"));
	}
    */
	
	
}
