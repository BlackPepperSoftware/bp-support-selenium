package uk.co.blackpepper.support.selenium;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static uk.co.blackpepper.support.selenium.ExpectedConditions2.and;
import static uk.co.blackpepper.support.selenium.ExpectedConditions2.urlIs;
import static uk.co.blackpepper.support.selenium.ExpectedConditions2.visibilityOfElementLocated;

public class ExpectedConditions2Test {
	
	@Test
	public void visibilityOfElementLocatedWithDisplayedElementReturnsTrue() {
		WebElement element = newDisplayedElement();
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElement(By.id("x"))).thenReturn(element);
		
		Boolean actual = visibilityOfElementLocated(By.id("x")).apply(driver);
		
		assertThat(actual, is(true));
	}
	
	@Test
	public void visibilityOfElementLocatedWithNonDisplayedElementReturnsFalse() {
		WebElement element = newUndisplayedElement();
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElement(By.id("x"))).thenReturn(element);
		
		Boolean actual = visibilityOfElementLocated(By.id("x")).apply(driver);
		
		assertThat(actual, is(false));
	}
	
	@Test
	public void visibilityOfElementLocatedWithNonExistantElementReturnsFalse() {
		WebDriver driver = mock(WebDriver.class);
		doThrow(new NoSuchElementException(null)).when(driver).findElement(By.id("x"));
		
		Boolean actual = visibilityOfElementLocated(By.id("x")).apply(driver);
		
		assertThat(actual, is(false));
	}
	
	@Test
	public void urlIsApplyWithEqualCurrentUrlReturnsTrue() {
		WebDriver driver = mock(WebDriver.class);
		when(driver.getCurrentUrl()).thenReturn("x");
		
		Boolean actual = urlIs("x").apply(driver);
		
		assertThat(actual, is(true));
	}
	
	@Test
	public void urlIsApplyWithUnequalCurrentUrlReturnsFalse() {
		WebDriver driver = mock(WebDriver.class);
		when(driver.getCurrentUrl()).thenReturn("y");
		
		Boolean actual = urlIs("x").apply(driver);
		
		assertThat(actual, is(false));
	}
	
	@Test
	public void urlIsToStringReturnsMessage() {
		WebDriver driver = mock(WebDriver.class);
		when(driver.getCurrentUrl()).thenReturn("y");
		ExpectedCondition<Boolean> condition = urlIs("x");
		condition.apply(driver);
		
		assertThat(condition.toString(), is("URL to be \"x\". Current URL: \"y\""));
	}
	
	@Test
	public void andApplyWithTrueAndTrueReturnsTrue() {
		Boolean actual = and(constant(true), constant(true)).apply(mock(WebDriver.class));
		
		assertThat(actual, is(true));
	}

	@Test
	public void andApplyWithTrueAndFalseReturnsFalse() {
		Boolean actual = and(constant(true), constant(false)).apply(mock(WebDriver.class));
		
		assertThat(actual, is(false));
	}
	
	@Test
	public void andApplyWithFalseAndTrueReturnsFalse() {
		Boolean actual = and(constant(false), constant(true)).apply(mock(WebDriver.class));
		
		assertThat(actual, is(false));
	}
	
	@Test
	public void andApplyWithFalseAndFalseReturnsFalse() {
		Boolean actual = and(constant(false), constant(false)).apply(mock(WebDriver.class));
		
		assertThat(actual, is(false));
	}
	
	@Test
	public void andToStringReturnsMessage() {
		ExpectedCondition<Boolean> condition = and(string("x"), string("y"));
		
		assertEquals("x and y", condition.toString());
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
	
	private static ExpectedCondition<Boolean> string(String string) {
		ExpectedCondition<Boolean> condition = mock(ExpectedCondition.class);
		when(condition.toString()).thenReturn(string);
		return condition;
	}

	private static ExpectedCondition<Boolean> constant(boolean value) {
		ExpectedCondition<Boolean> condition = mock(ExpectedCondition.class);
		when(condition.apply(any(WebDriver.class))).thenReturn(value);
		return condition;
	}
}
