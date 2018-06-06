package Modelo.DAO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import Controlador.Controlador;
import Controlador.Callback;

public class Log {

	private final String logsPath = "src/../Logs/";
	private Callback callback;
	private PrintWriter out;

   
	public Log() {
		callback= new Controlador();


	}
	
	public boolean guardarLogs(String log) {
		// TODO Auto-generated method stub
		
		
		
        boolean exito=false;
		File file = new File(logsPath);
		File file_logs = new File(logsPath+"log_"+obtenerFechaHoraActual().get(0)+".txt");
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

}
