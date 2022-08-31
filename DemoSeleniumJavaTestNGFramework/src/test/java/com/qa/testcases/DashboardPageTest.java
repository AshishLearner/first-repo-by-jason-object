package com.qa.testcases;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.qa.base.TestBase;
import com.qa.pages.MarketplacePage;
import com.qa.pages.DashboardPage;
import com.qa.pages.LoginPage;
import com.qa.util.TestUtil;

public class DashboardPageTest extends TestBase{
	LoginPage loginPage; //Login page object reference
	DashboardPage dashboardPage; //Dashboard page object reference
	MarketplacePage marketplacePage;  //MarketplacePage page object reference

	public DashboardPageTest(){
		super();
	}

	//test cases should be separated -- independent with each other
	//before each test case -- launch the browser and login
	//@test -- execute test case. Test methods name should be separated by "_" so that it will be used by Extent report to display the name
	//@Test(priority=<Set the priority>, groups = {<URS-ID>}, description = "PQT-ID")
	//after each test case -- close the browser

	@Parameters({"browser", "appEnvironment"})
	@BeforeMethod(alwaysRun = true)
	public void setUp(String Browser, String Env) {
		reportLog("Home Page class - Before method execution");
		initialization(Browser, Env);
		loginPage = new LoginPage(); //initialize loginPage reference. Using loginPage object reference I can call all login methods written inside the LoginPage() class.
		marketplacePage = new MarketplacePage();  //initialize documentsPage reference.
		dashboardPage = loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
	}

	@Test(priority=1, groups = {"URS-0002"}, description = "PQT-0004")
	public void verify_DashboardPage_Page_Title_Test(){
		reportLog("Validating dashboard page title");
		String dashboardPageTitle = dashboardPage.verifyDashboardPageTitle();
		Assert.assertEquals(dashboardPageTitle, "OrangeHRM","Dashboard page title not matched");
	}

	@Test(priority=2, groups = {"URS-0002"}, description = "PQT-0005")
	public void verify_UserName_Test(){
		reportLog("Validating user name displayed in dashboard page");
		Assert.assertTrue(dashboardPage.verifyCorrectUserNameByName(prop.getProperty("dashboardPageUserName")));
	}

	@Test(priority=3, groups = {"URS-0002"}, description = "PQT-0006")
	public void verify_Employee_Distribution_Subunit_Panel_Test(){
		reportLog("Validating Employee Distribution Subunit Panel presence");
		boolean flag = dashboardPage.validateEmployeeDistributionSubunitPanel();
		Assert.assertTrue(flag);
	}
	
	@Test(priority=3, groups = {"URS-0002"}, description = "PQT-0007")
	public void verify_Legend_Panel_Test(){
		reportLog("Validating Legend Panel presence");
		boolean flag = dashboardPage.validateLegendPanel();
		Assert.assertTrue(flag);
	}
	
	@Test(priority=3, groups = {"URS-0002"}, description = "PQT-0008")
	public void verify_Pending_Leave_Requests_Panel_Test(){
		reportLog("Validating Pending Leave Request Panel presence");
		boolean flag = dashboardPage.validatePendingLeaveRequestsPanel();
		Assert.assertTrue(flag);
	}
	
	@Test(priority=3, groups = {"URS-0002"}, description = "PQT-0009")
	public void verify_Marketplace_Link_Test(){
		reportLog("Click open Marketplace link");
		marketplacePage = dashboardPage.clickOnMarketplace();
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
