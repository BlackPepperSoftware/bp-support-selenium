package uk.co.blackpepper.support.selenium.jqueryui;

import java.util.Collections;
import java.util.Date;
import java.util.NoSuchElementException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static java.util.Arrays.asList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static uk.co.blackpepper.support.date.Dates.newDate;
import static uk.co.blackpepper.support.selenium.jqueryui.DatePicker.clickDayOfMonth;
import static uk.co.blackpepper.support.selenium.jqueryui.DatePicker.getDate;
import static uk.co.blackpepper.support.selenium.jqueryui.DatePicker.isVisible;
import static uk.co.blackpepper.support.selenium.jqueryui.DatePicker.nextMonthIsEnabled;

public class DatePickerTest {

	private ExpectedException thrown = ExpectedException.none();
	
	@Rule
	public ExpectedException getThrown() {
		return thrown;
	}
	
	@Test
	public void isVisibleWithDisplayedElementReturnsTrue() {
		WebElement element = newDisplayedElement();
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElements(any(By.class))).thenReturn(asList(element));
		
		assertThat(isVisible(driver), is(true));
	}

	@Test
	public void isVisibleWithUndisplayedElementReturnsFalse() {
		WebElement element = newUndisplayedElement();
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElements(any(By.class))).thenReturn(asList(element));
		
		assertThat(isVisible(driver), is(false));
	}

	@Test
	public void isVisibleWithNoElementsReturnsFalse() {
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElements(any(By.class))).thenReturn(Collections.<WebElement>emptyList());
		
		assertThat(isVisible(driver), is(false));
	}
	
	@Test
	public void clickDayOfMonthWithValidDayClicksDay() {
		WebDriver driver = mock(WebDriver.class);
		WebElement element = newDisplayedElement();
		when(driver.findElements(any(By.class))).thenReturn(asList(element));
		WebElement dayOfMonthElement = mock(WebElement.class);
		when(driver.findElement(any(By.class))).thenReturn(dayOfMonthElement);
		
		clickDayOfMonth(driver, 1);
		
		verify(dayOfMonthElement).click();
	}
	
	@Test
	public void clickDayOfMonthWithInvalidDayThrowsException() {
		WebDriver driver = mock(WebDriver.class);
		WebElement element = newDisplayedElement();
		when(driver.findElements(any(By.class))).thenReturn(asList(element));
		when(driver.findElement(any(By.class))).thenThrow(new NoSuchElementException());
		
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Day of month not found: -1");
		
		clickDayOfMonth(driver, -1);
	}
	
	@Test
	public void clickDayOfMonthWithUndisplayedDatePickerThrowsException() {
		WebElement element = newUndisplayedElement();
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElements(any(By.class))).thenReturn(asList(element));

		thrown.expect(TimeoutException.class);
		thrown.expectMessage("jQueryUI date-picker to be visible");
		
		clickDayOfMonth(driver, -1);
	}
	
	@Test
	public void getDateReturnsDate() {
		WebDriver driver = mock(WebDriver.class);
		WebElement element = newDisplayedElement();
		when(driver.findElements(any(By.class))).thenReturn(asList(element));
		WebElement yearElement = newElementWithText("2000");
		WebElement monthElement = newElementWithText("January");
		WebElement dayOfMonthElement = newElementWithText("1");
		when(driver.findElement(any(By.class))).thenReturn(yearElement)
			.thenReturn(monthElement)
			.thenReturn(dayOfMonthElement);
		
		Date actual = getDate(driver);
		
		assertThat(actual, is(newDate(2000, 1, 1)));
	}

	@Test
	public void getDateWithUndisplayedDatePickerThrowsException() {
		WebElement element = newUndisplayedElement();
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElements(any(By.class))).thenReturn(asList(element));

		thrown.expect(TimeoutException.class);
		thrown.expectMessage("jQueryUI date-picker to be visible");
		
		getDate(driver);
	}
	
	@Test
	public void nextMonthIsEnabledWithNextMonthEnabledReturnsTrue() {
		WebDriver driver = mock(WebDriver.class);
		WebElement element = newDisplayedElement();
		when(element.getAttribute(anyString())).thenReturn("");
		when(driver.findElements(any(By.class))).thenReturn(asList(element));
		when(driver.findElement(any(By.class))).thenReturn(element);
		
		assertThat(nextMonthIsEnabled(driver, ""), is(true));
	}
	
	@Test
	public void nextMonthIsEnabledWithNextMonthDisabledReturnsFalse() {
		WebDriver driver = mock(WebDriver.class);
		WebElement element = newDisplayedElement();
		when(element.getAttribute(anyString())).thenReturn("ui-state-disabled");
		when(driver.findElements(any(By.class))).thenReturn(asList(element));
		when(driver.findElement(any(By.class))).thenReturn(element);
		
		assertThat(nextMonthIsEnabled(driver, ""), is(false));
	}
	
	private static WebElement newDisplayedElement() {
		WebElement element = mock(WebElement.class);
		when(element.isDisplayed()).thenReturn(true);
		return element;
	}
	
	private static WebElement newUndisplayedElement() {
		WebElement element = mock(WebElement.class);
		when(element.isDisplayed()).thenReturn(false);
		return element;
	}

	private static WebElement newElementWithText(String text) {
		WebElement element = mock(WebElement.class);
		when(element.getText()).thenReturn(text);
		return element;
	}
}
