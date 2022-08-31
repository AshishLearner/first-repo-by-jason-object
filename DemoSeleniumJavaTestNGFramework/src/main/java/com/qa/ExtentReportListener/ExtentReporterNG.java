/*
 * @autor : MINDFIRE SOLUTIONS
 * 
 */
package com.qa.ExtentReportListener;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.xml.XmlSuite;

import com.qa.base.TestBase;
import com.qa.util.TestUtil;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.utils.ExtentUtil;

public class ExtentReporterNG extends TestBase implements IReporter {
	private ExtentReports extent;
	TestUtil testUtil;

	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
			String outputDirectory) {
		extent = new ExtentReports(outputDirectory + File.separator + "Extent_Report" +  File.separator
				+ "OrangeHRM_Extent.html", true);

		for (ISuite suite : suites) {
			Map<String, ISuiteResult> result = suite.getResults();
			for (ISuiteResult r : result.values()) {
				ITestContext context = r.getTestContext();
				try {
					buildTestNodes(context.getPassedTests(), LogStatus.PASS);
					buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
					//buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		extent.flush();
		extent.close();
	}

	private void buildTestNodes(IResultMap tests, LogStatus status) throws IOException {
		ExtentTest test;

		if (tests.size() > 0) {
			for (ITestResult result : tests.getAllResults()) {

				//Add browser and browser version info. This data is fetched from initialization() method in TestBase class
				extent.addSystemInfo("Browser", browserName1+" - "+browserVersion1);

				//Add the application environment info into the extent report
				extent.addSystemInfo("Application Environment: ", AppEnv);

				//Get the method name and store it in a string.
				String MethodName = result.getMethod().getMethodName();

				//Capitalize the first letter of the method name
				String capMethodName = MethodName.substring(0, 1).toUpperCase() + MethodName.substring(1);

				//Replace the method name from underscore and replace with space. This will make the method name look like a proper testcase name
				String SpacedMethodName = capMethodName.replace("_", " ");

				//Add the testcase id as given under @Test(description) in test class
				String FinalMethodName =  result.getMethod().getDescription()+" - "+SpacedMethodName;

				test = extent.startTest(FinalMethodName);

				test.setStartedTime(getTime(result.getStartMillis()));
				test.setEndedTime(getTime(result.getEndMillis()));

				for (String group : result.getMethod().getGroups()) { //This code will categories the test according to the group
					test.assignCategory(group);
				}

				for(String message:Reporter.getOutput(result)) { //This code picks the log from Reporter object
					if(message.endsWith(".png")) {
						test.log(LogStatus.INFO, test.addScreenCapture(message));
					}else {
						test.log(LogStatus.INFO, message);
					}
				}

				String message = "Test " + status.toString().toLowerCase() + "ed";

				if (result.getThrowable() != null) {
					test.log(status, result.getThrowable());

					message = result.getThrowable().getClass() +": "+ result.getThrowable().getMessage();

					//Check if the test case failed, skipped or passed and then attach the appropriate screenshot
					//using getAttribute() to the test case result in Extent Report
					if(result.getStatus()==result.FAILURE || result.getStatus()==result.SKIP) {
						String screenshotPath=(String) result.getAttribute("screenshotPath");
						test.log(status, test.addScreenCapture(screenshotPath));
					}
				} else {
					test.log(status, message);
				}

				extent.endTest(test);
			}
		}
	}

	private Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}
}