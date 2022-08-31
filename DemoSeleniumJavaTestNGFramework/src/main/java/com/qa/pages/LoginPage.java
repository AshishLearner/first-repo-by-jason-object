package com.qa.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.qa.base.TestBase;

public class LoginPage extends TestBase {

	//Page Factory - OR:
	@FindBy(xpath="//input[@id='txtUsername']")
	WebElement username; //Variable name

	@FindBy(xpath="//input[@id='txtPassword']")
	WebElement password;

	@FindBy(xpath="//input[@id='btnLogin']")
	WebElement loginBtn;

	@FindBy(xpath="//img[contains(@src,'logo.png')]")
	WebElement orangeHRMLogo;

	//Initializing the Page Factory/Objects:
	public LoginPage(){
		PageFactory.initElements(getdriver(), this); //"this" means current class object. All the above variables will be initialized with this driver
	}

	/**Actions/Methods on this page:**/
	
	public String validateLoginPageTitle(){ //this method will return a string(getTitle())
		return getdriver().getTitle();
	}

	public boolean validateOrangeHRMImage(){  //isDisplayed() method will return true/false. So, this method will return boolean
		return orangeHRMLogo.isDisplayed();
	}

	public DashboardPage login(String un, String pwd){ //return type of login() method is HomePage
		username.sendKeys(un);
		password.sendKeys(pwd);
		loginBtn.click();
		//JavascriptExecutor js = (JavascriptExecutor)driver;
		//js.executeScript("arguments[0].click();", loginBtn);

		return new DashboardPage(); //This login method should return Dashboard Page class object.
	}


}
