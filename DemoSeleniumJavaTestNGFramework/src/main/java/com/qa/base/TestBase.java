package com.qa.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.Reporter;
import org.testng.annotations.BeforeSuite;

import com.qa.util.TestUtil;
import com.qa.util.WebEventListener;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.Capabilities;

public class TestBase {

	//A static member is a member of a class that isn't associated with an instance of a class. Instead, the member belongs to the class itself.

	private static WebDriver driver; //Initialize webdriver with static variable. This is set tp private because I am not using this anywhere outside this class
	public static Properties prop; //Public class variable for properties. This is a global variable(public static).I can use inside TestBase.java class and all other child classes also
	public static EventFiringWebDriver e_driver;
	public static WebEventListener eventListener;
	public static String browserName1;
	public static String browserVersion1;
	private static String propertiesFileName;
	public static String AppEnv;
	//private static final String KEY = "appEnvironment";

	//This is the logger class. Generate the logs? : use Apache log4j API (log4j jar)
	public static Logger log = Logger.getLogger(TestBase.class);

	//Method for adding logs passed from test cases. This is for extentTest HTML report and log4j log files as well
	public static void reportLog(String message) {
		log.info("Message: " + message);
		Reporter.log(message);
	}

	//Method to take screenshot at any step you want
	public static void takeScreenshotAtAnyStep(String screenshotPath) {
		Reporter.log(screenshotPath);
	}

	//TestBase class public constructor.
	/*
	 * public TestBase(){ //Read the properties file try { prop = new Properties();
	 * //initializing the prop variable FileInputStream ip = new
	 * FileInputStream(System.getProperty("user.dir")+
	 * "//src//main//java//com//qa//config//config.properties"); prop.load(ip); }
	 * catch (FileNotFoundException e) { e.printStackTrace(); } catch (IOException
	 * e) { e.printStackTrace(); } }
	 */

	//To make driver thread safe. This will be required for parallel execution from testNG
	private static ThreadLocal<WebDriver> dr = new ThreadLocal<WebDriver>(); //Initialize thread local variable.

	//Getter method ThreadLocal variable dr. This is the driver instance, it will check which thread is calling this for parallel running.
	public static WebDriver getdriver() {
		return dr.get();
	}

	//Setter method. This will set the webdriver reference driverref
	public static void setDriver(WebDriver driverref) {
		dr.set(driverref); 
	}

	//For cleanup operation. This will remove any values stored by threadLocal variable. This will be used in afterclass method of each test.
	public static void unload() {
		dr.remove(); 
	}

	//Initialization method
	public static void initialization(String browserName, String env){

		//Read the properties file.Based on the parameter value provided for environment, the properties file will load
		try {
			if (env.trim().equalsIgnoreCase("Staging")) {
				propertiesFileName = "config_Staging.properties";
			} else if (env.trim().equalsIgnoreCase("Production")) {
				propertiesFileName = "config_Production.properties";
			}

			System.out.println("************"+env);
			prop = new Properties(); //initializing the prop variable
			FileInputStream ip = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//com//qa//config//"+propertiesFileName);
			prop.load(ip);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//String browserName = prop.getProperty("browser");

		if(browserName.trim().equalsIgnoreCase("chrome")){

			//System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"//chromedriver.exe");
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			setDriver(driver);
		}
		else if(browserName.trim().equalsIgnoreCase("firefox")){
			//System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"//geckodriver.exe");
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			setDriver(driver);
		}

		//To get Browser details:
		Capabilities browserCap = ((RemoteWebDriver) getdriver()).getCapabilities();
		browserName1 = browserCap.getBrowserName();
		browserVersion1 = browserCap.getVersion();

		//To get Application environment currently running
		
		//AppEnv = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter(KEY);
		AppEnv = prop.getProperty("environment");
		System.out.println(AppEnv);

		//Create EventFiringWebDriver class object
		e_driver = new EventFiringWebDriver(getdriver());
		// Now create object of EventListerHandler to register it with EventFiringWebDriver
		eventListener = new WebEventListener();
		//Register the eventListener class object with event firing webdriver object.
		e_driver.register(eventListener);
		//Assign this to the main driver
		//driver = e_driver;
		setDriver(e_driver);

		getdriver().manage().window().maximize();
		getdriver().manage().deleteAllCookies();
		getdriver().manage().timeouts().pageLoadTimeout(TestUtil.PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
		getdriver().manage().timeouts().implicitlyWait(TestUtil.IMPLICIT_WAIT, TimeUnit.SECONDS);

		//Open the url of the application
		getdriver().get(prop.getProperty("url"));

	}

}
