package com.herokuapp.theinternet;

//import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class LoginTestsCombined {

	private WebDriver driver;

	@Parameters({ "browser" })
	@BeforeMethod(alwaysRun = true)
	private void setUp(@Optional String browser) {

		// Create Driver - instance of the browsers
		// Input Parameter is marked as @Optional as we set default as Chromedriver
		switch (browser) {
		case "chrome":
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
			driver = new ChromeDriver();
			break;
		case "firefox":
			System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver.exe");
			driver = new FirefoxDriver();
			break;
		default:
			System.out.println("Do not know how to start " + browser + ". Starting chrome");
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
			driver = new ChromeDriver();
			break;
		}

		driver.manage().window().maximize();
		// 	implicit wait added to setUp method
		//	never mix implicit and explicit waits, which is why this one is commented out
//		driver.manage().timeouts().implicitlyWait(7, TimeUnit.SECONDS);
	}

	@AfterMethod(alwaysRun = true)
	private void tareDown() {
		driver.quit();
	}

	@Test(priority = 2, enabled = true, groups = { "positiveTests", "smoke" })
	public void positiveLoginTest() {
		System.out.println("Starting login test.");

//		Open Test Page
		String home_page = "https://the-internet.herokuapp.com/login";
		driver.get(home_page);
		System.out.println("Page is opened.");

//		Enter User Name
		WebElement username = driver.findElement(By.id("username"));
		username.sendKeys("tomsmith");

//		Enter password
		WebElement password = driver.findElement(By.id("password"));
		password.sendKeys("SuperSecretPassword!");
		
//		WebDriverWait wait = new WebDriverWait(driver, 10);
//		Click login button

		driver.findElement(By.className("fa-sign-in")).click();

//		ASSERTIONS
//		assertEquals(actual, expected, "message if 0");			URL assertion
		String expectedUrl = "https://the-internet.herokuapp.com/secure";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl, "Actual URL is different than expected URL");

//		assertTrue(WebElement.isDisplayed()); 					Logout button visible
		WebElement logoutButton = driver.findElement(By.xpath("//a[@class='button secondary radius']"));
		Assert.assertTrue(logoutButton.isDisplayed());

//		Message visible
		WebElement loginSuccesfullMessage = driver.findElement(By.xpath("//div[@id='flash']"));
		String actualMessage = loginSuccesfullMessage.getText();
		String expectedMessage = "You logged into a secure area!";
//		Assert.assertEquals(actualMessage, expectedMessage, "Not same");
		Assert.assertTrue(actualMessage.contains(expectedMessage),
				"Login success message does not contain expected message");
	}

	@Parameters({ "username", "password", "expectedMessage" })
	@Test(priority = 1, enabled = true, groups = { "negativeTests", "smoke" })
	public void negativeLoginTest(String username, String password, String expectedErrorMessage) throws Throwable {
		// it's negativeLoginTest now as we only have one method with parameters for
		// both user-name and password
		System.out.println("Starting incorrect login credentials test.");

		String loginUrl = "https://the-internet.herokuapp.com/login";
		driver.navigate().to(loginUrl);
		System.out.println("Page is opened.");

//		HARDCODED LOGIN & PASS NOT USED ANYMORE
// 		@Parameters in TestSuites are taking care of that which is how we can access those variables from XML file level
//		String login = "whatever wrong";
//		String pass = "SuperSecretPassword!";

		WebElement loginField = driver.findElement(By.xpath("//input[@name='username']"));
		loginField.sendKeys(username);

		WebElement passwordField = driver.findElement(By.xpath("//input[@type='password']"));
		passwordField.sendKeys(password);

		WebElement loginButton = driver.findElement(By.xpath("//i[@class='fa fa-2x fa-sign-in']"));
		loginButton.click();

//		assert that URL is still same		
		String expectedUrlAfterFailedLogin = loginUrl;
		String actualUrlAfterFailedLogin = driver.getCurrentUrl();
		Assert.assertEquals(actualUrlAfterFailedLogin, expectedUrlAfterFailedLogin, "Page is not login page");

//		assert that warning is showing
		WebElement loginWarning = driver.findElement(By.id("flash"));
		String loginWarningText = loginWarning.getText();
		Assert.assertTrue(loginWarningText.contains(expectedErrorMessage),
				"Login warning was not shown. \nActual: " + loginWarningText + "\nExpected: " + expectedErrorMessage);
	}

//	private void sleepFor(long m) {
//		try {
//			Thread.sleep(m);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
