/**
 * 
 */
package common.rpa.exception;

/**
 * @author yeestevez
 *
 */
@SuppressWarnings("serial")
public class PruebaBotException extends Exception{
	
	public PruebaBotException() {
		
	}
	/**
	 * Este metodo recibe como parametro el texto que se desea mostrar
	 * Cuando sucede un error
	 * @param message
	 */
	public PruebaBotException (String message) {
		
		super(message);
	}

}
