package uk.co.blackpepper.support.selenium.jqueryui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;

public final class DatePicker {

	private static final long ITEM_TIME_OUT = 1;

	private DatePicker() {
		throw new AssertionError();
	}
	
	public static boolean isVisible(WebDriver driver) {
		try {
			waitForDatePicker(driver);
			return true;
		}
		catch (TimeoutException exception) {
			return false;
		}
	}
	
	public static void clickDayOfMonth(WebDriver driver, int day) {
		waitForDatePicker(driver);
		
		WebElement dayButton;
		try {
			dayButton = driver.findElement(By.linkText(String.valueOf(day)));
		}
		catch (NoSuchElementException noSuchElementException) {
			throw new IllegalArgumentException(String.format("Day of month not found: %d", day));
		}
		
		dayButton.click();
	}
	
	public static Date getDate(WebDriver driver) {
		waitForDatePicker(driver);
		
		int year = getYear(driver);
		int month = getMonth(driver);
		int day = getDay(driver);
		
		return getDate(year, month, day);
	}

	public static boolean nextMonthIsEnabled(WebDriver driver, String elementId) {
		show(driver, elementId);
		
		waitForDatePicker(driver);
		
		WebElement nextMonth = driver.findElement(By.className("ui-datepicker-next"));
		
		return !nextMonth.getAttribute("class").contains("ui-state-disabled");
	}

	private static void show(WebDriver driver, String elementId) {
		driver.findElement(By.id(elementId)).click();
	}
	
	private static int getYear(WebDriver driver) {
		WebElement yearElement = driver.findElement(By.className("ui-datepicker-year"));
		
		return Integer.parseInt(yearElement.getText());
	}
	
	private static int getMonth(WebDriver driver) {
		WebElement monthElement = driver.findElement(By.className("ui-datepicker-month"));
		
		Date date = null;
		try {
			date = new SimpleDateFormat("MMMM").parse(monthElement.getText());
		}
		catch (ParseException parseException) {
			parseException.printStackTrace();
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		return calendar.get(Calendar.MONTH);
	}
	
	private static int getDay(WebDriver driver) {
		WebElement selectedDayElement = driver.findElement(By.className("ui-state-active"));
		
		return Integer.parseInt(selectedDayElement.getText());
	}
	
	private static void waitForDatePicker(WebDriver driver) {
		new WebDriverWait(driver, ITEM_TIME_OUT).until(new Predicate<WebDriver>() {
			@Override
			public boolean apply(WebDriver driver) {
				for (WebElement element : driver.findElements(By.id("ui-datepicker-div"))) {
					if (element.isDisplayed()) {
						return true;
					}
				}
				
				return false;
			}

			@Override
			public String toString() {
				return "jQueryUI date-picker to be visible";
			}
		});
	}
	
	private static Date getDate(int year, int month, int day) {
		Calendar calendar = new GregorianCalendar(year, month, day, 0, 0, 0);
		
		return calendar.getTime();
	}
}
