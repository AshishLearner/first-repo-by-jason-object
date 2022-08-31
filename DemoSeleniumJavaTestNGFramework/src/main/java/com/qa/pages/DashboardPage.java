package com.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.qa.base.TestBase;

public class DashboardPage extends TestBase {

	//Page Factory - OR:
	/*
	 * @FindBy(xpath="//a[@id='welcome' and text()='Welcome Paul']") WebElement
	 * userNameLabel;
	 */

	@FindBy(xpath="//input[@id='MP_link']")
	WebElement link_marketplace;

	@FindBy(xpath="//legend[text()='Employee Distribution by Subunit']//ancestor::fieldset")
	WebElement EmployeeDistributionSubunit_Panel;

	@FindBy(xpath="//legend[text()='Legend']//ancestor::fieldset")
	WebElement Legend_Panel;

	@FindBy(xpath="//legend[text()='Pending Leave Requests']//ancestor::fieldset")
	WebElement PendingLeaveRequests_Panel;


	//Initializing the Page Objects:
	public DashboardPage(){
		PageFactory.initElements(getdriver(), this);
	}

	//Actions/Methods on this page:
	public String verifyDashboardPageTitle(){
		return getdriver().getTitle();
	}

	public boolean verifyCorrectUserNameByName(String name){
		//return userNameLabel.isDisplayed();
		return getdriver().findElement(By.xpath("//a[@id='welcome' and text()='"+name+"']")).isDisplayed();
	}

	public MarketplacePage clickOnMarketplace(){
		link_marketplace.click();

		return new MarketplacePage();
	}

	public boolean validateEmployeeDistributionSubunitPanel(){
		return EmployeeDistributionSubunit_Panel.isDisplayed();
	}

	public boolean validateLegendPanel(){
		return Legend_Panel.isDisplayed();
	}

	public boolean validatePendingLeaveRequestsPanel(){
		return PendingLeaveRequests_Panel.isDisplayed();
	}
}
