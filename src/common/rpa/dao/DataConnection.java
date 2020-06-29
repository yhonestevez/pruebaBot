/**
 * 
 */
package common.rpa.dao;

import common.rpa.model.Product;

// TODO: Auto-generated Javadoc
/**
 * The Interface DataConnection.
 *
 * @author yeestevez
 */
public interface DataConnection {

	/**
	 * Update.
	 *
	 * @param data the data
	 */
	public void updateMercadoLibre(Product data,String state);
	
	/**
	 * Consult.
	 *
	 * @return the product
	 */
	public Product  consultMercadoLibre();
	
	/**
	 * Update amazon.
	 *
	 * @param data the data
	 */
	public void updateAmazon(Product data,String state);
	
	/**
	 * Consult amazon.
	 *
	 * @return the product
	 */
	public Product  consultAmazon();
	
}
