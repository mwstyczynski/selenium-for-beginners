package com.herokuapp.theinternet;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ExceptionsTests {

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
	}

	@Test(priority = 1, groups = { "exceptions", "hiddenElement" })
	private void notVisibleTest() {
		// Element is present on the page from the begining but not visible
		String testUrl = "http://the-internet.herokuapp.com/dynamic_loading/1";
		driver.navigate().to(testUrl);

		WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button"));
		startButton.click();

		String helloMessage = "Hello World!";
		WebElement hello = driver.findElement(By.id("finish"));

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOf(hello));

		String actualHelloMessage = hello.getText();
		System.out.println("Message is: " + actualHelloMessage);
		Assert.assertTrue(actualHelloMessage.equals(helloMessage), "Actual message is different than " + helloMessage);

		// now 'start' button is not visible
		// org.openqa.selenium.ElementNotInteractableException
		// startButton.click();
	}

	@Test(priority = 2, groups = { "exceptions" })
	private void timeOutTest() {
		String testUrl = "http://the-internet.herokuapp.com/dynamic_loading/1";
		driver.navigate().to(testUrl);

		WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button"));
		startButton.click();

		String helloMessage = "Hello World!";
		WebElement hello = driver.findElement(By.id("finish"));

		WebDriverWait wait = new WebDriverWait(driver, 3);

		try {
			wait.until(ExpectedConditions.visibilityOf(hello));
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception catched: " + e.getMessage());
			wait.until(ExpectedConditions.visibilityOf(hello));
			wait.until(ExpectedConditions.visibilityOf(hello));
			wait.until(ExpectedConditions.visibilityOf(hello));
		}

		String actualHelloMessage = hello.getText();
		System.out.println("Message is: " + actualHelloMessage);
		Assert.assertTrue(actualHelloMessage.equals(helloMessage), "Actual message is different than " + helloMessage);

		// now 'start' button is not visible
		// org.openqa.selenium.ElementNotInteractableException
		// startButton.click();
	}

	@Test(priority = 3, groups = { "exceptions", "hiddenElement" })
	private void noSuchElementTest() {
		// Element is not present on the page until button is clicked and element loaded
		String testUrl = "http://the-internet.herokuapp.com/dynamic_loading/2";
		driver.navigate().to(testUrl);

		WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button"));
		startButton.click();

//		here we need our wait BEFORE even trying to locate the element
//		it will not be wait.untill VISIBILITY but until.Appears
		WebDriverWait wait = new WebDriverWait(driver, 10);
//		WebElement hello=  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("finish")));

//		String helloMessage = "Hello World!";
//		String actualHelloMessage = hello.getText();
//		System.out.println("Message is: " + actualHelloMessage);
//		Assert.assertTrue(actualHelloMessage.equals(helloMessage), "Actual message is different than " + helloMessage);

//		This can also be achieved in one single line (much faster)
		Assert.assertTrue(
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("finish"), "Hello World!")),
				"Couldn't verify expected text");
	}

	@Test
	public void staleElementTest() {
		driver.get("http://the-internet.herokuapp.com/dynamic_controls");

//		WebElement located with xPath using 'text()' locate option
		WebElement removeButton = driver.findElement(By.xpath("//button[contains(text(), 'Remove')]"));
		WebElement checkbox = driver.findElement(By.id("checkbox"));

		WebDriverWait wait = new WebDriverWait(driver, 6);

		removeButton.click();

//		wait.until(ExpectedConditions.invisibilityOf(checkbox));
//		Assert.assertFalse(checkbox.isDisplayed());

//		Here (in that assertion above) we are trying to reach an element which is not present in the DOM anymore which will cause StaleElementReferenceException
//		Comment out wait.until and assertFalse and assert invisibilityOf straight in assertion because wait.until(ExpectedCondition...) returns a boolean which can be asserted as true of false

//		Assert.assertTrue(wait.until(ExpectedConditions.invisibilityOf(checkbox)), "Checkbox is still visible");

//		Another way of dealing with that Exception is to use stalenessOf assertion
		Assert.assertTrue(wait.until(ExpectedConditions.stalenessOf(checkbox)));

//		Now we click on "#add" button and wait for the #checkbox to be present again
		WebElement addButton = driver.findElement(By.xpath("//button[contains(text(), 'Add')]"));
		addButton.click();
		WebElement newCheckbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("checkbox")));
		Assert.assertTrue(newCheckbox.isDisplayed(), "Checkbox is not visible");
	}

	@Test(priority = 4)
	public void disabledElementTest() {
		driver.get("http://the-internet.herokuapp.com/dynamic_controls");
		WebElement enableButton = driver.findElement(By.xpath("//button[contains(text(), 'Enable')]"));
		WebElement inputField = driver.findElement(By.xpath("(//input)[2]"));
		enableButton.click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(inputField));
		String message = "Now it's clickable";
		inputField.sendKeys(message);
		
		Assert.assertEquals(inputField.getAttribute("value"), message);

		WebElement disableButton = driver.findElement(By.xpath("//button[contains(text(), 'Disable')]"));
		disableButton.click();

	}

	@AfterMethod(alwaysRun = true)
	private void tareDown() {
		driver.quit();
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
