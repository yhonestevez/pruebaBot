/**
 * 
 */
package common.rpa.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import common.rpa.dao.DataConnectionImpl;
import common.rpa.model.Product;
import common.rpa.utils.Mail;
import common.rpa.utils.Selenium;

/**
 * @author yeestevez
 *
 */
public class Bots {
	private static Logger logger = Logger.getLogger(Bots.class);
	private static final String PROCESADO = "PROCESADO";
	public static final DataConnectionImpl dataConnectionImpl = new DataConnectionImpl();
	public static final Selenium selenium = new Selenium();
	public String state = "PROCESANDO";
	public static final WebDriver driver = selenium.getDriverChrome("chromedriver.exe");

	/**
	 * Este metodo se utiliza para ingresar a la pagina de mercado libre y consultar
	 * los productos
	 */
	public void botMercadoLibre() {

		Product product = dataConnectionImpl.consultMercadoLibre();
		while (product != null) {

			logger.info("Nonbre del producto " + product.getName());
			String keyboard = product.getName();

			selenium.openBrowser("https://www.mercadolibre.com.co/", driver);
			selenium.sendText(driver, "/html/body/header/div/form/input", keyboard);
			selenium.sendClick(driver, "/html/body/header/div/form/button/div");
			selenium.waitElement(driver, 5000);
			List<WebElement> keyboards = driver.findElements(By.xpath("/html/body/main/div[2]/div/section/ol/li"));
			logger.info(keyboards.size());
			selenium.waitElement(driver, 1);
			String xpathProduct = "div/div[2]/div/h2/a/span";
			String xpathPrice = "div/div[2]/div/div[1]/div/span[2]";
			for (WebElement webElement : keyboards) {

				if (!selenium.existElement(webElement, xpathProduct)) {
					continue;
				}
				logger.info(webElement.findElement(By.xpath(xpathProduct)).getText());
				if (webElement.findElement(By.xpath(xpathProduct)).getText().toLowerCase()
						.contains(keyboard.toLowerCase())) {
					logger.info(webElement.findElement(By.xpath(xpathProduct)).getText());

					if (selenium.existElement(webElement, xpathPrice)) {

						logger.info(webElement.findElement(By.xpath(xpathPrice)).getText());
						product.setPrice(webElement.findElement(By.xpath(xpathPrice)).getText());
					}
					state = PROCESADO;
					break;
				}

			}
			dataConnectionImpl.updateMercadoLibre(product, state);
			product = dataConnectionImpl.consultMercadoLibre();
		}
		driver.close();
		driver.quit();
		
	}

	/**
	 * Este metodo se utiliza para ingresar a la pagina de amazon y consultar los
	 * productos
	 */
	public void botAmazon() throws InterruptedException {

		Product product = dataConnectionImpl.consultAmazon();
		while (product != null) {

			logger.info("Nonbre del producto " + product.getName());
			String keyboard = product.getName();

			selenium.openBrowser(" https://www.amazon.com/", driver);
			selenium.waitElement(driver, 5000);
			Thread.sleep(1000);
			selenium.sendText(driver, "/html/body/div[1]/header/div/div[1]/div[3]/div/form/div[3]/div[1]/input",
					keyboard);
			Thread.sleep(1000);
			selenium.sendClick(driver, "/html/body/div[1]/header/div/div[1]/div[3]/div/form/div[2]/div/input");// Buscar
			Thread.sleep(1000);
			selenium.sendClick(driver, "/html/body/div[1]/header/div/div[1]/div[3]/div/form/div[2]/div/input");// Buscar
			List<WebElement> keyboards = driver
					.findElements(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div/span[3]/div[2]/div"));
			logger.info(keyboards.size());
			selenium.waitElement(driver, 1);
			String xpathProduct = "div/span/div/div/div/div/div[2]/h2/a/span";
			String xpathPrice = "/div/span/div/div/div/div/div[4]/div/div";
			for (WebElement webElement : keyboards) {
				if (!selenium.existElement(webElement, xpathProduct)) {
					continue;
				}
				logger.info(" producto leido " + webElement.findElement(By.xpath(xpathProduct)).getText());
				if (webElement.findElement(By.xpath(xpathProduct)).getText().toLowerCase()
						.contains(keyboard.toLowerCase())) {
					logger.info(webElement.findElement(By.xpath(xpathProduct)).getText());
					logger.info(webElement.findElement(By.xpath(xpathPrice)).getText());
					product.setPrice(webElement.findElement(By.xpath(xpathPrice)).getText());
					state = PROCESADO;
					break;
				}

			}
			dataConnectionImpl.updateAmazon(product, state);
			product = dataConnectionImpl.consultAmazon();
		}
		driver.close();
		driver.quit();
		
	}

	/**
	 * Este metodo se usa para ingresar a la pagina es.uefa.com y buscar los
	 * resultados de Man. United" en la UEFA Europa League
	 * 
	 * @throws InterruptedException
	 */
	public void botUefa(String receivingMail) throws InterruptedException {

		selenium.openBrowser(" https:es.uefa.com", driver);
		selenium.waitElement(driver, 5000);
		selenium.sendClick(driver, "/html/body/div[4]/div[2]/div/div[1]/nav/div/ul/li[2]/a");
		selenium.sendClick(driver, "/html/body/div[4]/div[2]/div/div[1]/nav[2]/div[4]/div/ul/li[3]/a");
		List<WebElement> teamNames = driver.findElements(
				By.xpath("/html/body/div[5]/div[2]/div/div[2]/section[1]/div/div/div/div[3]/div/div[2]/div/div"));

		selenium.waitElement(driver, 1);
		for (WebElement teamName : teamNames) {
			logger.info("Equipo  " + teamName.findElement(By.xpath("a/div[2]/div[3]/div/div/span")).getText() + " VS "
					+ teamName.findElement(By.xpath("a/div[2]/div[5]/div/div/span")).getText());
			logger.info("Resultado  " + teamName.findElement(By.xpath("a/div[2]/span[1]")).getText());
			if (teamName.findElement(By.xpath("a/div[2]/div[3]/div/div/span")).getText().contains("Man. United")
					|| teamName.findElement(By.xpath("a/div[2]/div[5]/div/div/span")).getText()
							.contains("Man. United")) {
				Mail mail = new Mail();

				String message = "En el partido "
						+ teamName.findElement(By.xpath("a/div[2]/div[3]/div/div/span")).getText() + " VS "
						+ teamName.findElement(By.xpath("a/div[2]/div[5]/div/div/span")).getText()
						+ teamName.findElement(By.xpath("a/div[2]/span[1]")).getText();
				mail.sendMail("Resultados de mi equipo favorito", message, receivingMail);
				break;
			}

			
		}
		driver.close();
		driver.quit();
	}
}
