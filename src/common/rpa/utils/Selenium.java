/**
 * 
 */
package common.rpa.utils;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

/**
 * @author yeestevez
 *
 */
public class Selenium {
	private static Logger logger = Logger.getLogger(Selenium.class);
	
	/**
	 * Devuelve el driver con el que se manejara la pagina 
	 * @param paht direccion donde se encuentra el driver
	 * elemlplo C: \\ chromedriver.exe
	 * @return
	 */
	public WebDriver getDriverChrome(String paht) {
		
		System.setProperty("webdriver.chrome.marionette",paht);
		return  new ChromeDriver();
	}
	
	/**
	 * Abre el navegador con la url indicada
	 * @param url dirección de la pagina wep
	 * @param driver
	 */
	public void openBrowser(String url, WebDriver driver) {
		driver.get(url);
	}
	
	/**
	 * Devuelve el texto del campo
	 * @param driver
	 * @param xpathExpression 
	 * Ejempo   xpath de el campo de entrada de google =//*[@id="fakebox-input"]
	 * @return
	 */
	public String getText(WebDriver driver, String xpathExpression) {
		
		return driver.findElement(By.xpath(xpathExpression)).getText();
				
	}
	
	/**
	 *  Envia una cadena de texto al  campo de la pagina
	 * @param driver
	 * @param xpathExpression 
	 * Ejempo   xpath de el campo de entrada de google =//*[@id="fakebox-input"]
	 * @param text
	 */
	public void sendText(WebDriver driver, String xpathExpression,String text) {
		
		 driver.findElement(By.xpath(xpathExpression)).sendKeys(text);;
		
	}
	
	/**
	 * Se envian comandos de teclado
	 * Ejemplo  Keys.ENTER	para enviar un enter
	 * @param driver
	 * @param xpathExpression Ejempo   xpath de el campo de entrada de google =//*[@id="fakebox-input"]
	 * @param enter
	 */
	public void sendComand(WebDriver driver, String xpathExpression,Keys enter ) {
		
		 driver.findElement(By.xpath(xpathExpression)).sendKeys(enter);
				
	}
	
	/**
	 * Envia un click a campo
	 * @param driver
	 * @param xpathExpression
	 */
	public void sendClick(WebDriver driver, String xpathExpression ) {
		
		 driver.findElement(By.xpath(xpathExpression)).click();
				
	}
	
	/**
	 * Este metodo se unsa cuando se quiere subir un archivo a una pagina
	 * se envia al campo don se busca el archivo la direccion donde este se encuentra
	 * @param driver
	 * @param xpathExpression  Ejempo   xpath de el campo de entrada de google =//*[@id="fakebox-input"]
	 * @param pathFile
	 */
	public void sendFile (WebDriver driver, String xpathExpression , String pathFile) {
		WebElement addFile=  driver.findElement(By.xpath(xpathExpression));
		addFile.clear();
		addFile.sendKeys(pathFile);
	}
	
	/**
	 * Seleccion un dato de una lista desplegable
	 * @param driver
	 * @param xpathExpression xpathExpression  Ejempo   xpath de el campo de entrada de google =//*[@id="fakebox-input"]
	 * @param option
	 */
	public void selectOption(WebDriver driver, String xpathExpression, String option) {
		
		Select dropdown = new Select(driver.findElement(By.xpath(xpathExpression)));
		dropdown.selectByVisibleText(option);
	}
	
	/**
	 * Espera un determinado tiempo dado por la variabe time en milisegundos para que aparezcael elemento buscado
	 * @param driver
	 * @param xpathExpression
	 * @param time
	 * @return
	 */
	public WebDriver waitElement(WebDriver driver, int time ) {
		driver.manage().timeouts().implicitlyWait(time, TimeUnit.MILLISECONDS);
		return driver;
		
	}
	/**
	 * si encuenta una alerta la cierra
	 * @param driver
	 * @return
	 */
	public boolean existElement(WebElement  driver,String  xpathExpression) {
		try {
			driver.findElement(By.xpath(xpathExpression));
		} catch ( Exception e) {
			logger.error("No existe el elemento ");
			return false;
		}
		return true;
	}
	
}
