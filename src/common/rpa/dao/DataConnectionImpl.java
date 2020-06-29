/**
 * 
 */
package common.rpa.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import common.rpa.exception.PruebaBotException;
import common.rpa.model.Product;
import common.rpa.utils.Excel;
import common.rpa.utils.GetProperties;

/**
 * @author yeestevez
 *
 */
public class DataConnectionImpl implements DataConnection {
	private static final String PRODUCTOS = "Productos";
	private static final String PRODUCTO = "Producto";
	private static final String PROCESADO = "PROCESADO";
	private static final String PROCESANDO = "PROCESANDO";
	private static Logger logger = Logger.getLogger(DataConnectionImpl.class);
	private static String path = GetProperties.getInstance().getdataProperties("path_excel");

	/**
	 * Actualiza los datos en el campo consulta mercado libre
	 */
	@Override
	public void updateMercadoLibre(Product data,String state) {

		Excel.updateCell(path, PRODUCTOS, (int) data.getId(), 1, data.getPrice());
		Excel.updateCell(path, PRODUCTOS, (int) data.getId(), 3, state);
	}

	/**
	 * Consulta los productos para mercado libre
	 */
	@Override
	public Product consultMercadoLibre() {
		Product product = new Product();

		List<ArrayList<String>> data;
		try {
			data = Excel.readAll(path, PRODUCTOS);

			for (int i = 0; i < data.size(); i++) {
				if (data.get(i).size() > 3) {
					logger.info(data.get(i).get(0) + "   ESTADO " + data.get(i).get(3));
				}

				if (!data.get(i).get(0).contains(PRODUCTO) && data.get(i).get(0).trim().length() > 3 && !data.get(i).get(3).contains(PROCESADO) && !data.get(i).get(3).contains(PROCESANDO)) {
					product.setId(i);
					product.setName(data.get(i).get(0));
					return product;
				}
			}
		} catch (PruebaBotException e) {

			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * Actualiza los datos en el campo consulta amazon
	 */
	@Override
	public void updateAmazon(Product data,String state) {

		Excel.updateCell(path, PRODUCTOS, (int) data.getId(), 2, data.getData());
		Excel.updateCell(path, PRODUCTOS, (int) data.getId(), 4, state);

	}

	/**
	 * Consulta los productos para amazon
	 */

	@Override
	public Product consultAmazon() {
		Product product = new Product();

		List<ArrayList<String>> data;
		try {
			data = Excel.readAll(path, PRODUCTOS);

			for (int i = 0; i < data.size(); i++) {
				if (data.get(i).size() > 3) {
					logger.info("Producto: ");
					logger.info(data.get(i).get(0) + "   ESTADO " + data.get(i).get(4));
				}
				if (!data.get(i).get(0).contains(PRODUCTO) && data.get(i).get(0).trim().length() > 3 && !data.get(i).get(4).contains(PROCESADO) && !data.get(i).get(4).contains(PROCESANDO)) {
					product.setId(i);
					product.setName(data.get(i).get(0));
					return product;
				}
			}
		} catch (PruebaBotException e) {

			logger.error(e.getMessage());
		}
		return null;
	}

}
