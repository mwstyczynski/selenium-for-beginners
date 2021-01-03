package com.herokuapp.theinternet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PositiveTests {

	@Test(priority = 1, enabled = true, groups = { "positiveTests", "smoke" })
	public void loginTest() {

		System.out.println("Starting login test.");

// 		Create Driver - instance of the browsers
		System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
		WebDriver driver = new ChromeDriver();

//		Open Test Page
		String home_page = "https://the-internet.herokuapp.com/login";

		sleepFor(500);
		driver.manage().window().maximize();
		driver.get(home_page);
		System.out.println("Page is opened.");

//		Enter User Name
		WebElement username = driver.findElement(By.id("username"));
		username.sendKeys("tomsmith");
//		driver.findElement(By.xpath("//input[@id='username']")).sendKeys("tomsmith");

//		Enter password
		WebElement password = driver.findElement(By.id("password"));
		password.sendKeys("SuperSecretPassword!");

//		Just to have a second or two to see what was inserted into loginPage
		sleepFor(2500);

//		Click login button
		driver.findElement(By.className("fa-sign-in")).click();

//		ASSERTIONS

//		assertEquals(actual, expected, "message if 0");			URL assertion
		String expectedUrl = "https://the-internet.herokuapp.com/secure";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl, "Actual URL is different than Expected UR");

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

//		Close browser
		driver.quit();

	}

	private void sleepFor(long m) {
		try {
			Thread.sleep(m);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
