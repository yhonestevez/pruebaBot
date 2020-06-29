/**
 * Para que este programa se ejcute el excel en el campo Estado mercado libre y   Estado Amazon no puede estar vacio
 * Cuabdo el bot consulta el producto actualiza el estado a PROCESANDO Y si puede culminar el procespo correctamente actualiza el estado a PROCESADO
 * Inicialmente el estado se encuentra en PENDIENTE, si el bot ya consulto el caso no lo volvera hace a menos que vaya al archivo Lista de Productos.xlsx
 * y  cambie el estado a  PENDIENTE. Esta es la ruta del archivo: resources\\Lista de Productos.xlsx.
 * 
 * Para el bot de la pagina de la UEFA el correo y la contraseña se encuentran el el archivo properties en la rura: resources\\properties
 * 
 * Se desarrollo con el driver de selenium con version del comntrolador para goole 83.0.4103.116 (Build oficial) (64 bits)
 */
package common.rpa.controller;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import common.rpa.utils.Mail;
import net.bytebuddy.asm.Advice.Exit;

/**
 * @author yeestevez
 *
 */
public class Main {
	private static Logger logger = Logger.getLogger(Main.class);

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		String log4jConfPath = "resources\\log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);

		
		int bot = 0;
		while (true) {
			String nBot = JOptionPane.showInputDialog(
					"1: Bot Mercado libre \n 2: Bot amazon \n 3: Bot pagina UEFA \n Ingrese cualquier otro numero para cerrar el programa \n Por favor ingrese el numero correspondiente");
			try {
				bot = Integer.parseInt(nBot);
				if(bot>3 || bot==0) {
					System.exit(0);
				}
				if (bot > 0 && bot < 4) {
					break;
				} 
				
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, " ingrese un dato valido");
			}
		}
		Bots bots = new Bots();
		switch (bot) {
		case 1:
			bots.botMercadoLibre();
			break;
		case 2:
			bots.botAmazon();
			break;
		case 3:
			String mail = JOptionPane.showInputDialog(
					"Por favo ingres un correo a donde \n se enviara los resultados de la ultima fecha \n de su equipo favorito Man. United");
			bots.botUefa(mail);
			break;
		default:

			break;
		}

	}

}
