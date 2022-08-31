package com.qa.testcases;

import static org.testng.Assert.assertTrue;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.qa.base.TestBase;
import com.qa.pages.MarketplacePage;
import com.qa.pages.DashboardPage;
import com.qa.pages.LoginPage;
import com.qa.util.TestUtil;

public class MarketplacePageTest extends TestBase{

	LoginPage loginPage;
	DashboardPage dashboardPage;
	MarketplacePage marketplacePage;

	public MarketplacePageTest() {
		super();
	}

	//test cases should be separated -- independent with each other
	//before each test case -- launch the browser and login
	//@test -- execute test case. Test methods name should be separated by "_" so that it will be used by Extent report to display the name
	//@Test(priority=<Set the priority>, groups = {<URS-ID>}, description = "PQT-ID")
	//after each test case -- close the browser

	@Parameters({"browser", "appEnvironment"})
	@BeforeMethod(alwaysRun = true)
	public void setUp(String Browser, String Env) throws InterruptedException {
		reportLog("Initialize the browser");
		initialization(Browser, Env);
		loginPage = new LoginPage();
		marketplacePage = new MarketplacePage();
		reportLog("Login to OrangeHRM app");
		dashboardPage = loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
		reportLog("Open Marketplace page");
		dashboardPage.clickOnMarketplace();
	}

	@Test(priority=3, groups = {"URS-0003"}, description="PQT-0010")
	public void verify_Marketplace_Page_URL_Test(){
		reportLog("verify Marketplace Page URL is correct");
		String marketplacePageTitle = marketplacePage.verifyMarketplacePageURL();
		assertTrue(marketplacePageTitle.contains("index.php/marketPlace/ohrmAddons"),"Marketplace page URL is not correct.");
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
	}

}
