package com.qa.testcases;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.qa.ExtentReportListener.ExtentReporterNG;
import com.qa.base.TestBase;
import com.qa.pages.DashboardPage;
import com.qa.pages.LoginPage;
import com.qa.util.TestUtil;

public class LoginPageTest extends TestBase{
	LoginPage loginPage; //Login page object reference. It will be defined at class level to be used throughout the program.
	DashboardPage dashboardPage; //dashboardPage is DashboardPage reference variable or DashboardPage page object reference
	
	String sheetName = "LoginPage";
	
	//Create a constructor of LoginPageTest class
	public LoginPageTest(){
		super(); //super() keyword is used to call TestBase class constructor i.e. the super class/parent constructor
		/*
		 * The super keyword refers to superclass (parent) objects. It is used to call
		 * superclass methods, and to access the superclass constructor. The most common
		 * use of the super keyword is to eliminate the confusion between superclasses
		 * and subclasses that have methods with the same name.
		 */
	}

	//test cases should be separated -- independent with each other
	//before each test case -- launch the browser and login
	//@test -- execute test case. Test methods name should be separated by "_" so that it will be used by Extent report to display the name
	//@Test(priority=<Set the priority>, groups = {<URS-ID>}, description = "PQT-ID")
	//after each test case -- close the browser

	@Parameters({"browser", "appEnvironment"})
	@BeforeMethod(alwaysRun = true)
	public void setUp(String Browser, String Env){
		reportLog("Login Page class - Before method execution");
		initialization(Browser, Env);
		loginPage = new LoginPage(); //Create object of LoginPage() class. This is used to access all the functions/methods of LoginPage() class
	}

	@Test(priority=1, groups = {"URS-0001"}, description = "PQT-0001")
	public void login_Page_Title_Test(){
		reportLog("Validating the login page titie");
		String title = loginPage.validateLoginPageTitle(); //this will return title in string.
		Assert.assertEquals(title, "OrangeHRM"); //assert from testNG is used to verify or validate
	}

	@Test(priority=2, groups =  {"URS-0001"}, description="PQT-0002")
	public void orangeHRM_Logo_Image_Test(){
		reportLog("Validating OrangeHRM logo image");
		boolean flag = loginPage.validateOrangeHRMImage();
		Assert.assertTrue(flag);
	}

	@DataProvider
	public Object[][] getTestData() throws InvalidFormatException{
		Object data[][] = TestUtil.getTestData(sheetName);
		return data;
	}

	@Test(priority=3, groups =  {"URS-0001"}, description="PQT-0003", dataProvider="getTestData")
	public void login_Test(String userName, String password) throws Exception{
		reportLog("Login to OrangeHRM app");
		
		//this login() method is returning an object of HomePage class. So I stored this in dashboardPage class reference object
		reportLog("Login using username as: "+userName+" and password as: "+password);
		
		//Data Driven example for login function
		dashboardPage = loginPage.login(userName, password);
		
		//Fetching data from properties file
		//dashboardPage = loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
		
		reportLog("Validating login is successfull by checking dashboard page title");
		String dashboardPageTitle = dashboardPage.verifyDashboardPageTitle();
		Assert.assertEquals(dashboardPageTitle, "OrangeHRM","Dashboard page title not matched");
		
		//Sample for color coding the log line
		reportLog("<b><font color='Purple'> Verifying Login successful. Screenshot attached below:</font><b>");
		//Sample code on how to take screenshot at a particular step that will be added in Extent report.
		String screenshotPathAnyStep = TestUtil.getScreenshot(getdriver(), "Verify Login");
		takeScreenshotAtAnyStep(screenshotPathAnyStep);
	}


	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult result) throws Exception {
		reportLog("Take sceenshot in case of failue and close browser");
		//Check if the test case failed or was skipped and take screenshot
		if(result.getStatus()==result.FAILURE || result.getStatus()==result.SKIP) {
			String screenshotPath = TestUtil.getScreenshot(getdriver(), result.getName());
			result.setAttribute("screenshotPath", screenshotPath); //sets the value the variable/attribute screenshotPath as the path of the screenshot
		}
		getdriver().quit();
		unload();
	}
}
