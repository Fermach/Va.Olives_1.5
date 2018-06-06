package Modelo.DAO;

import java.io.File;
import java.io.IOException;

import com.healthmarketscience.jackcess.CryptCodecProvider;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;

import net.ucanaccess.jdbc.JackcessOpenerInterface;

/**
 * 
 * @author DESARROLLO1
 *
 */
public class CryptCodecOpener implements JackcessOpenerInterface{

	/**
	 * Clase para desencriptar BBDD encriptada con contraseña
	 */
	@Override
	public Database open(File arg0, String arg1) throws IOException {
		DatabaseBuilder dbBuilder = new DatabaseBuilder(arg0);
		dbBuilder.setAutoSync(false);
		dbBuilder.setCodecProvider(new CryptCodecProvider(arg1));
		dbBuilder.setReadOnly(false);
	
		return dbBuilder.open();
	}
	

}
