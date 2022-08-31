package com.qa.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.qa.base.TestBase;

public class MarketplacePage extends TestBase {

	//Page Factory - OR:
	@FindBy(xpath="//label[text()='LDAP']")
	WebElement Label_LDAP;

	//Initializing the Page Objects:
	public MarketplacePage(){
		PageFactory.initElements(getdriver(), this);
	}

	//Actions/Methods on this page:
	public String verifyMarketplacePageURL(){
		return getdriver().getCurrentUrl();
	}

}
