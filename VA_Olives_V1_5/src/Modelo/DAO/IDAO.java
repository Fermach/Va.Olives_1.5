package Modelo.DAO;

import java.util.List;
import Modelo.DTO.ImagenFrutoPesada;
import at.jta.RegistryErrorException;

/**
 * 
 * @author DESARROLLO1
 *
 */
public interface IDAO {

	/**
	 * Leer archivos TXT
	 * @return
	 */
	List<ImagenFrutoPesada> listaImagenesPesadaRecibidas();
	
	/**
	 * Insertar datos en BBDD Access
	 * @param frutoPesada
	 * @param num_ticket
	 * @return
	 */
	boolean insertarImagenesRecibidasEnBBDD(ImagenFrutoPesada frutoPesada, String num_ticket);
	String obtenerUltimoNumeroTicketBBDD(String linea);

	
	/**
	 * Leer el estado de pesada del registro
	 * @param bascula
	 * @return
	 * @throws RegistryErrorException
	 */
	String obtenerValorClavePesada(String bascula) throws RegistryErrorException;
	
	/**
	 * Actualiza la clave del registro fruto pesada procesado
	 * @param linea
	 * @return
	 */
	boolean CambiarClaveEnRegistroLineaProcesada(int linea);
	
	/**
	 * Guardar los Logs en un archivo Txt
	 * 
	 * @param log
	 * @return
	 */
	boolean guardarLogs(String log) ;
	
	/**
	 * mover los archivos procesados de carpeta
	 * @param frutoPesada
	 * @return
	 */
	boolean moverTxtAprocesados(ImagenFrutoPesada frutoPesada);
	
	
}
