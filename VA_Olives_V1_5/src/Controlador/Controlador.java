package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import Modelo.DTO.ImagenFrutoPesada;
import Modelo.DAO.DAO;
import Controlador.Callback;
import Vista.PantallaPrincipalVista;
import Vista.Vista;
import at.jta.RegistryErrorException;

/**
 * 
 * @author DESARROLLO1
 *
 */
public class Controlador implements Callback, ActionListener{

	 private Vista vista;
	 private boolean ejecutar;
	 private DAO dao;
	 private List<ImagenFrutoPesada> listaImagenesFrutoPesada;
	 private int contador = 0;
	 private String mLog;
	 private boolean pesadas_iniciadas;
     private Callback callback;
     private FileWriter fileWriter;
     private int numeroBasculas;
	 private String[] estadosPesada;
     private boolean[] pesadas_ini;
     private String num_ticket;
	 
     /**
      * Esta clase inicializa la vista y controla los eventos que en ella se producen,
      * además se comunica con el DAO obtener los datos que sean necesarios
      * 
      * @param vista
      */
	 public Controlador(Vista vista) {
			this.vista = vista;
			this.dao= DAO.getInstance();
			numeroBasculas= DAO.getInstance().obtenerNumeroBascular();
			pesadas_ini= new boolean[9];
			pesadas_iniciadas=false; 
			estadosPesada= new String[9];
			activarEscuchador(this);
			listaImagenesFrutoPesada = dao.getInstance().listaImagenesPesadaRecibidas();
            mLog= " ";
			cargarTabla();
			actualizarUsuarioContras(dao.obtenerUsuarioContras().get(0), dao.obtenerUsuarioContras().get(1));
			iniciarLecturaPesadaYProcesarDatos();
			
		}
	 
	

	private void activarEscuchador(ActionListener escuchador) {
		vista.getPantallaPrincipalVista().getBtnLimpiarLogs().addActionListener(escuchador);
		vista.getPantallaPrincipalVista().getBtnGuardar().addActionListener(escuchador);
	}
  
	public Controlador(){
		 
	}
	 

	/**
	 * Este metodo se encarga iniciar un Hilo que se ejecuta de fondo 
	 * cada 6000 segundos y cuya labor es realizar todo el procesado de ticket 
	 * en caso de que se haya finalizado una pesada:
	 * 
	 * - Obtiene los valores de pesada del registro
	 * - Obtiene los ultimos registros de la BBDD
	 * - Inserta en la BBDD los datos correspondientes a la última imagen de fruto de pesada 
	 *   obtenida en cada linea 
	 * - Mueve los archivos de Imagen de Fruto de una carpeta a otra cuando estos de han 
	 *   insertado en la BBDD
	 * 
	 */
	private void iniciarLecturaPesadaYProcesarDatos() {
	    ejecutar=true;	
	
		for(int i=1; i<9; i++) {
			pesadas_ini[i]=false;
		}
         
       
		 new Thread(new Runnable() {
		        public void run() {
		            try {
		             while(true) {
		            	pesadas_iniciadas=false;
		            	System.out.println("-- HILO INICIADO --");
		            	
		            	//listaImagenesFrutoPesada = dao.getInstance().listaImagenesPesadaRecibidas();
		            	dao.addLogs("- "+dao.obtenerFechaHoraActual().get(2)+"   --HILO INICIADO --");
		            	dao.guardarLogs("- "+dao.obtenerFechaHoraActual().get(2)+"   --HILO INICIADO --");
		            	
		            	for(int i=1; i<= numeroBasculas; i++) {
		               
		            		estadosPesada[i] =dao.obtenerValorClavePesada("bascula"+i);
		            		
		            	}
		            	System.out.println("--VALORES DEL REGISTRO OBTENIDOS: "+ estadosPesada[1]+", "+estadosPesada[2]+", "+estadosPesada[3]+", "
		            	          +estadosPesada[4]+", "+estadosPesada[5] +", "+estadosPesada[6]+", "+estadosPesada[7]+", "+estadosPesada[8]+" --");
		            	dao.addLogs("- "+dao.obtenerFechaHoraActual().get(2)+"   --VALORES DEL REGISTRO OBTENIDOS: "+ estadosPesada[1]+", "+estadosPesada[2]+", "+estadosPesada[3]+", "
		            	            +estadosPesada[4]+", "+estadosPesada[5] +", "+estadosPesada[6]+", "+estadosPesada[7]+", "+estadosPesada[8]+" --");
                        dao.guardarLogs("- "+dao.obtenerFechaHoraActual().get(2)+"   --VALORES DEL REGISTRO OBTENIDOS: "+ estadosPesada[1]+", "+estadosPesada[2]+", "+estadosPesada[3]+", "
		            	          +estadosPesada[4]+", "+estadosPesada[5] +", "+estadosPesada[6]+", "+estadosPesada[7]+", "+estadosPesada[8]+" --");
		            	
                        
                       
		            	for(int i=1; i<=numeroBasculas; i++) {
		            	 System.out.println("--PROCESANDO LINEA "+i+"--");
		            	 //dao.addLogs("- "+dao.obtenerFechaHoraActual().get(2)+"   --PROCESANDO LINEA "+i+"--");
		            	 dao.guardarLogs("- "+dao.obtenerFechaHoraActual().get(2)+"   --PROCESANDO LINEA "+i+"--");
		            	 
		            	 if(estadosPesada[i].equals("2")) {
		            		   pesadas_ini[i]=true;
		            		   pesadas_iniciadas=true;
		            		   System.out.println("--PESANDO LINEA "+i+"--");      
		            		   dao.addLogs("- "+dao.obtenerFechaHoraActual().get(2)+"   --PESANDO LINEA "+i+"--\n\n");
                               dao.guardarLogs("- "+dao.obtenerFechaHoraActual().get(2)+"   --PESANDO LINEA "+i+"--\n\n");
                              
		            		}
   
		            	 else{
		            			// si se ha registrado un pesada 
		            		   if(pesadas_ini[i]==true) {
		            			   
		            				 pesadas_ini[i]=false;
		            				 //pesadas_iniciadas=false;  
		            				 Thread.sleep(2000);
		            			 
				            		   System.out.println("--PESADA FINALIZADA EN LINEA "+i+"--");
				            		   dao.addLogs("- "+dao.obtenerFechaHoraActual().get(2)+"    "+"--PESADA FINALIZADA EN LINEA "+i+"--");
				            		   System.out.println("--PROCESANDO TICKET--");  
				            		   dao.addLogs("- "+dao.obtenerFechaHoraActual().get(2)+"   - "+dao.obtenerFechaHoraActual().get(2)+"   --PROCESANDO TICKET--");
						               dao.guardarLogs("- "+dao.obtenerFechaHoraActual().get(2)+"   - "+dao.obtenerFechaHoraActual().get(2)+"   --PROCESANDO TICKET--");
				            		   
				            	       //CONSULTAR ULTIMO REGISTRO BBDD
				            		   //NUM TICKET
				            		    num_ticket=dao.obtenerUltimoNumeroTicketBBDD(""+i);

				            		   //INSERTAR DATOS DE ESA LINEA EN NUM TICKET
				            		   for(ImagenFrutoPesada imagenFrutoPesada: listaImagenesFrutoPesada) {
				            			   
				            			   if(imagenFrutoPesada.getLinea().equals(""+i)) {
				            				     
				            				  boolean exito=  dao.insertarImagenesRecibidasEnBBDD(imagenFrutoPesada, num_ticket);
				            				  if(exito) {
				            					  //MOVER TICKET DE RUTA DE TICKET RECIBIDOS A TICKET PROCESADOS
				            					  dao.moverTxtAprocesados(imagenFrutoPesada);
				            					  dao.guardarLogs("- "+dao.obtenerFechaHoraActual().get(2)+"   +++++ DATOS DEL TICKET PROCESADOS CORRECTAMENTE +++++");
				            					  dao.addLogs("- "+dao.obtenerFechaHoraActual().get(2)+"   +++++ DATOS DEL TICKET PROCESADOS CORRECTAMENTE +++++");
									              
				            					  
				            					  //escribir en el registro clave procesada
				            					  dao.CambiarClaveEnRegistroLineaProcesada(i);
				            					 

				            				  }else {
				            					   dao.guardarLogs("- "+dao.obtenerFechaHoraActual().get(2)+ "   - ERROR los datos del fruto obtenido no son validos");
				            					   dao.addLogs("- "+dao.obtenerFechaHoraActual().get(2)+ "   - ERROR los datos del fruto obtenido no son validos");
					            				 
					            			  }
				            				
				            			   }else {
				            				   dao.guardarLogs("- "+dao.obtenerFechaHoraActual().get(2)+ "   - ERROR los datos del Txt no se han obtenido correctamente! ");
				            				   dao.addLogs("- "+dao.obtenerFechaHoraActual().get(2)+ "   - ERROR los datos del TXT no se han obtenido correctamente! ");
				            				 

				            			   }
				            			  
				            		   }
				            		     

		            		   }
		            		   System.out.println("--SIN PESADA LINEA "+i+"--");  
				              // addLog("- "+dao.obtenerFechaHoraActual().get(2)+"   --SIN PESADA LINEA "+i+"--");
            				   dao.guardarLogs("- "+dao.obtenerFechaHoraActual().get(2)+"   --SIN PESADA LINEA "+i+"--");

		            		}	
		            	 
		              	
		            	}
		            	if(!pesadas_iniciadas) {
		            		 dao.addLogs("- "+dao.obtenerFechaHoraActual().get(2)+"   - NO HAY PESADAS INICIADAS EN ESTE MOMENTO");
		            		// pesadas_iniciadas=false;
		            		 }
		            	            	
		                Thread.sleep(6000);
	            		System.out.println("\n\n");  

		             }
		            } catch (InterruptedException e) {
		                // TODO Auto-generated catch block
		                e.printStackTrace();
		                dao.addLogs("- "+dao.obtenerFechaHoraActual().get(2)+"   - ERROR: \n"+ e.getStackTrace()  +"\n");
     				    dao.guardarLogs("- "+dao.obtenerFechaHoraActual().get(2)+"   - ERROR: \n"+ e.getStackTrace()  +"\n");


		            } catch (RegistryErrorException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						dao.addLogs("- "+dao.obtenerFechaHoraActual().get(2)+"   - ERROR: \n"+ e.getStackTrace()  +"\n");
                        dao.guardarLogs("- "+dao.obtenerFechaHoraActual().get(2)+"   - ERROR: \n"+ e.getStackTrace()  +"\n");
					}
		        }
		    }).start();
		
	}


	/**
	 * 	Este metodo carga la tabla de ticket recibidos en nuestra interfaz
	 */
	private void cargarTabla() {
	//lee los txt recibidos para actualizar la tabla

	 new Thread(new Runnable() {
        public void run() {
		     
		         while(true) {
		        	listaImagenesFrutoPesada= new ArrayList<>();
		        	vista.getPantallaPrincipalVista().getModelo().getDataVector().removeAllElements();
		 			listaImagenesFrutoPesada = dao.getInstance().listaImagenesPesadaRecibidas();

		 			if(listaImagenesFrutoPesada.isEmpty()) {
		 				vista.getPantallaPrincipalVista().getModelo().setRowCount(0);
		 				vista.getPantallaPrincipalVista().repaint();
		 				vista.getPantallaPrincipalVista().getTabla_datos().setModel(vista.getPantallaPrincipalVista().getModelo());
		 				System.out.println("- Lista Vacia");
		 				dao.addLogs("- "+dao.obtenerFechaHoraActual().get(2)+"   - Lista Vacia");
                        dao.guardarLogs("- "+dao.obtenerFechaHoraActual().get(2)+"   - Lista Vacia");
		 				
		 				
		 			}else {
		 			
		            String filas[] = new String[13] ;
		
                    for (ImagenFrutoPesada frutoPesada : listaImagenesFrutoPesada) {
 			
 		          	filas[0]= frutoPesada.getLinea();
 			        filas[1]= frutoPesada.getDato1();
 			        filas[2]= frutoPesada.getDato2();
 			        filas[3]= frutoPesada.getDato3();
 			        filas[4]= frutoPesada.getDato4();
 			        filas[5]= frutoPesada.getDato5();
 			        filas[6]= frutoPesada.getDato6();
 		         	filas[7]= frutoPesada.getDato7();
 			        filas[8]= frutoPesada.getDato8();
 			        filas[9]= frutoPesada.getDato9();
 			        filas[10]= frutoPesada.getDato10();
 			        filas[11]= frutoPesada.getDato11();
 		        	filas[12]= frutoPesada.getDato12();
 			
 			        vista.getPantallaPrincipalVista().getModelo().addRow(filas);
 			

 		             }
                    vista.getPantallaPrincipalVista().getTabla_datos().setModel(vista.getPantallaPrincipalVista().getModelo());
		 			}
         
 		            
 		           
 			       
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
		         }
		         
		      
				}
		        
		    }).start();
	 		
	}
	

    /**
     * Este método añade los Logs a nuestra interfaz y también los guarda 
     * en un archivo txt  
     */
	@Override
	public void addLog(String log) {
		// TODO Auto-generated method stub
		//this.dao= DAO.getInstance();	
		
		
		this.mLog +=""+log+"\n";
		if(mLog.length()>2700){
			mLog="";
			
		}
		
		vista.getPantallaPrincipalVista().getText_logs().setText(mLog);
		vista.getPantallaPrincipalVista().getText_logs().repaint();
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getActionCommand().equals("Limpiar Logs")) {
                			
			mLog="";
			vista.getPantallaPrincipalVista().getText_logs().setText(mLog);
			vista.getPantallaPrincipalVista().getText_logs().repaint();
		}
		if(e.getActionCommand().equals("guardar")) {
		//	vista.getPantallaPrincipalVista().getBtnGuardar().setEnabled(false);
			guardarUsuarioContraseña();
	
		}
		
	}
	
	
	public void actualizarUsuarioContras(String usuario,String cotraseña) {

		vista.getPantallaPrincipalVista().getPasswordField().setText(cotraseña);
		vista.getPantallaPrincipalVista().getTextField_nombre_usuario().setText(usuario);
		
	}
	
	public void guardarUsuarioContraseña() {
		String nombre_usuario= 	vista.getPantallaPrincipalVista().getTextField_nombre_usuario().getText().toString();
		String pass_usuario= vista.getPantallaPrincipalVista().getPasswordField().getText().toString();
		
		dao.guardarUsuarioYPass(nombre_usuario, pass_usuario);
	}



	@Override
	public void actualizarboton() {
		// TODO Auto-generated method stub
		vista.getPantallaPrincipalVista().getBtnGuardar().setEnabled(true);

	}




	


}
