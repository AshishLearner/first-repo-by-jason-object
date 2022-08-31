package com.qa.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.qa.base.TestBase;

public class TestUtil extends TestBase {

	//Static public variable to use anywhere.
	public static long PAGE_LOAD_TIMEOUT = 20;
	public static long IMPLICIT_WAIT = 20;

	public static String TESTDATA_SHEET_PATH = System.getProperty("user.dir")+"//src//main//java//com//qa//testdata//TestData.xls";

	static Workbook book;
	static Sheet sheet;
	static JavascriptExecutor js;

	public void switchToFrame() {
		getdriver().switchTo().frame("mainpanel");
	}

	public static Object[][] getTestData(String sheetName) throws InvalidFormatException {
		FileInputStream file = null;
		try {
			file = new FileInputStream(TESTDATA_SHEET_PATH);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			book = WorkbookFactory.create(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = book.getSheet(sheetName);
		Object[][] data = new Object[sheet.getLastRowNum()][sheet.getRow(0).getLastCellNum()];
		// System.out.println(sheet.getLastRowNum() + "--------" +
		// sheet.getRow(0).getLastCellNum());
		for (int i = 0; i < sheet.getLastRowNum(); i++) {
			for (int k = 0; k < sheet.getRow(0).getLastCellNum(); k++) {
				data[i][k] = sheet.getRow(i + 1).getCell(k).toString();
				// System.out.println(data[i][k]);
			}
		}
		return data;
	}

	public static void takeScreenshotAtEndOfTest() throws IOException {
		File scrFile = ((TakesScreenshot) getdriver()).getScreenshotAs(OutputType.FILE);
		String currentDir = System.getProperty("user.dir");
		FileUtils.copyFile(scrFile, new File(currentDir + "//screenshots//" + System.currentTimeMillis() + ".png"));
	}

	//Creating a method getScreenshot and passing two parameters 
	//driver and screenshotName
	public static String getScreenshot(WebDriver driver, String screenshotName) throws Exception {
		//below line is just to append the date format with the screenshot name to avoid duplicate names		
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		//after execution, you could see a folder "FailedTestsScreenshots" under src folder
		//String destination = System.getProperty("user.dir") + "//Screenshots//"+screenshotName+dateName+".png";
		String destination = System.getProperty("user.dir") + File.separator + "test-output" + File.separator + "Extent_Report" + File.separator + screenshotName+dateName+".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		
		//This is required for Jenkins
		String ImagePathForRemote = "./"+screenshotName+dateName+".png";
		
		//Returns the captured file path
		return ImagePathForRemote;
	}

	public static void runTimeInfo(String messageType, String message) throws InterruptedException {
		js = (JavascriptExecutor) getdriver();
		// Check for jQuery on the page, add it if need be
		js.executeScript("if (!window.jQuery) {"
				+ "var jquery = document.createElement('script'); jquery.type = 'text/javascript';"
				+ "jquery.src = 'https://ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js';"
				+ "document.getElementsByTagName('head')[0].appendChild(jquery);" + "}");
		Thread.sleep(5000);

		// Use jQuery to add jquery-growl to the page
		js.executeScript("$.getScript('https://the-internet.herokuapp.com/js/vendor/jquery.growl.js')");

		// Use jQuery to add jquery-growl styles to the page
		js.executeScript("$('head').append('<link rel=\"stylesheet\" "
				+ "href=\"https://the-internet.herokuapp.com/css/jquery.growl.css\" " + "type=\"text/css\" />');");
		Thread.sleep(5000);

		// jquery-growl w/ no frills
		js.executeScript("$.growl({ title: 'GET', message: '/' });");
		//'"+color+"'"
		if (messageType.equals("error")) {
			js.executeScript("$.growl.error({ title: 'ERROR', message: '"+message+"' });");
		}else if(messageType.equals("info")){
			js.executeScript("$.growl.notice({ title: 'Notice', message: 'your notice message goes here' });");
		}else if(messageType.equals("warning")){
			js.executeScript("$.growl.warning({ title: 'Warning!', message: 'your warning message goes here' });");
		}else
			System.out.println("no error message");
		// jquery-growl w/ colorized output
		//		js.executeScript("$.growl.error({ title: 'ERROR', message: 'your error message goes here' });");
		//		js.executeScript("$.growl.notice({ title: 'Notice', message: 'your notice message goes here' });");
		//		js.executeScript("$.growl.warning({ title: 'Warning!', message: 'your warning message goes here' });");
		Thread.sleep(5000);
	}

	public static void scrollPageToElement(WebElement wb){
		//Javascript command
		JavascriptExecutor js = (JavascriptExecutor)getdriver();
		js.executeScript("arguments[0].scrollIntoView();",wb);
	}


}
