/**
 * 
 */
package common.rpa.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import common.rpa.exception.PruebaBotException;

/**
 * @author yeestevez
 *
 */
public class GetProperties {

	private static Logger logger = Logger.getLogger(GetProperties.class);
	private static Properties properties = new Properties();

	private static GetProperties instanceGetProperties = null;

	/**
	 * 
	 * @throws HolkStroreEception
	 * @throws IOException
	 */
	private GetProperties() throws PruebaBotException, IOException {

		File file = new File("resources\\properties");

		InputStream fileInputStream;

		if (file.exists()) {
			fileInputStream = new FileInputStream(file);
		} else {
			fileInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("properties");

			if (fileInputStream == null) {
				
				throw new PruebaBotException("No se encontro el archivo");

			}
		}

		properties.load(fileInputStream);
	}

	/**
	 * Este metodo devuelve el valor de la variable que recibe por parametro
	 * 
	 * @param name
	 * @return
	 */
	public String getdataProperties(String name) {

		return properties.getProperty(name);

	}

	public static GetProperties getInstance() {

		if (instanceGetProperties == null) {
			try {
				instanceGetProperties = new GetProperties();
			} catch (PruebaBotException e) {
				logger.error(e.getMessage());
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}

		return instanceGetProperties;
	}
}
