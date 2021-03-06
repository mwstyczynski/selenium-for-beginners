package com.herokuapp.theinternet;

import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;

public class NegativeTests {

	@Test(priority = 1, enabled = true, groups = { "negativeTests", "smoke" })
	public void negativeUsernameTest() throws Throwable {

		System.out.println("Starting incorrect username test.");
		System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver.exe");
		WebDriver driver = new FirefoxDriver();

		String loginUrl = "https://the-internet.herokuapp.com/login";
		driver.manage().window().maximize();
		driver.navigate().to(loginUrl);
		System.out.println("Page is opened.");

		String login = "whatever wrong";
		String pass = "SuperSecretPassword!";

		WebElement loginField = driver.findElement(By.xpath("//input[@name='username']"));
		loginField.sendKeys(login);

		WebElement passwordField = driver.findElement(By.xpath("//input[@type='password']"));
		passwordField.sendKeys(pass);

		WebElement loginButton = driver.findElement(By.xpath("//i[@class='fa fa-2x fa-sign-in']"));
		loginButton.click();

//		assert that URL is still same		
		String expectedUrlAfterFailedLogin = loginUrl;
		String actualUrlAfterFailedLogin = driver.getCurrentUrl();
		Assert.assertEquals(actualUrlAfterFailedLogin, expectedUrlAfterFailedLogin, "Page is not login page");

//		assert that warning is showing
		WebElement loginWarning = driver.findElement(By.id("flash"));
		String loginWarningText = loginWarning.getText();
		String warning = "Your username is invalid!";
		Assert.assertTrue(loginWarningText.contains(warning),
				"Login warning was not shown. \nActual: " + loginWarningText + "\nExpected: " + warning);

		driver.quit();

	}

	@Test(priority = 2, enabled = true, groups = { "negativeTests" })
	public void negativePasswordTest() throws Throwable {

		System.out.println("Starting incorrect username test.");
		System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
		WebDriver driver = new ChromeDriver();

		String loginUrl = "https://the-internet.herokuapp.com/login";
		driver.manage().window().maximize();
		driver.navigate().to(loginUrl);
		System.out.println("Page is opened.");

		String login = "tomsmith";
		String pass = "SuperWRONGPassword!";

		WebElement loginField = driver.findElement(By.xpath("//input[@name='username']"));
		loginField.sendKeys(login);

		WebElement passwordField = driver.findElement(By.xpath("//input[@type='password']"));
		passwordField.sendKeys(pass);

		WebElement loginButton = driver.findElement(By.xpath("//i[@class='fa fa-2x fa-sign-in']"));
		loginButton.click();

//		assert that URL is still same		
		String expectedUrlAfterFailedLogin = loginUrl;
		String actualUrlAfterFailedLogin = driver.getCurrentUrl();
		Assert.assertEquals(actualUrlAfterFailedLogin, expectedUrlAfterFailedLogin, "Page is not login page");

//		assert that warning is showing
		WebElement loginWarning = driver.findElement(By.id("flash"));
		String loginWarningText = loginWarning.getText();
		String warning = "Your password is invalid!";
		Assert.assertTrue(loginWarningText.contains(warning),
				"Login warning was not shown. \nActual: " + loginWarningText + "\nExpected: " + warning);

		driver.quit();

	}

}
