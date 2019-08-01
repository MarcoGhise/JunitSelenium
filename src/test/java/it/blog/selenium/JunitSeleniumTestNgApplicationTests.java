package it.blog.selenium;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
public class JunitSeleniumTestNgApplicationTests {

	public static Properties prop = null;

	public static class MyHtmlUnitDriver extends HtmlUnitDriver {
		public MyHtmlUnitDriver(boolean enableJavascript) {
			super(enableJavascript);
		}

		public WebClient modifyWebClient(WebClient client) {
			client.setRefreshHandler(new ThreadedRefreshHandler());
			return client;
		}
	}

	private static Properties readPropertiesFile() {
		try (InputStream input = JunitSeleniumApplicationTests.class.getClassLoader()
				.getResourceAsStream("application.properties")) {

			Properties prop = new Properties();
			prop.load(input);
			return prop;
			
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		
	}

	@BeforeClass
	public static void setUp() throws IOException {
		prop = readPropertiesFile();
	}

	@Test
	@Ignore
	public void testHtmlJs() {

		DesiredCapabilities capabilities = DesiredCapabilities.htmlUnit();
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

		MyHtmlUnitDriver driver = new MyHtmlUnitDriver(true);

		driver.get(prop.getProperty("HtmlUnitDriver.url"));

		WebElement htmlTag = driver.findElement(By.id("para"));

		System.out.println(htmlTag.getText());

		assertEquals(htmlTag.getText(), "Js Test");

		driver.quit();
	}

	@Test
	public void testPhatomJs() throws InterruptedException {
		String os = System.getProperty("os.name").toLowerCase();

		if (os.contains("windows"))
			System.setProperty("phantomjs.binary.path", "driver/phantomjs.exe");
		else
			System.setProperty("phantomjs.binary.path", "driver/phantomjs");

		DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Authorization",
				"Basic " + prop.getProperty("PhantonJsDriver.credentials"));

		WebDriver driver = new PhantomJSDriver(capabilities);
		
		driver.get(prop.getProperty("PhantonJsDriver.url"));

		// Thread.sleep(3000);
		assertEquals(driver.getTitle(), prop.getProperty("assertTitle"));

		driver.quit();

	}
}
