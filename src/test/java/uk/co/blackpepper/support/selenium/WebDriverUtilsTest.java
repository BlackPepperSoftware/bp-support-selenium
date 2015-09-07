/*
 * Copyright 2014 Black Pepper Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.blackpepper.support.selenium;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static uk.co.blackpepper.support.selenium.WebDriverUtils.acceptAlert;
import static uk.co.blackpepper.support.selenium.WebDriverUtils.dismissAlert;
import static uk.co.blackpepper.support.selenium.WebDriverUtils.getText;
import static uk.co.blackpepper.support.selenium.WebDriverUtils.quietFindElement;
import static uk.co.blackpepper.support.selenium.WebDriverUtils.safeFindElement;
import static uk.co.blackpepper.support.selenium.WebDriverUtils.until;

public class WebDriverUtilsTest {

	@Test
	public void safeFindElementWhenElementFoundReturnsElement() {
		WebElement element = mock(WebElement.class);
		SearchContext context = mock(SearchContext.class);
		when(context.findElement(By.id("x"))).thenReturn(element);
		
		WebElement actual = safeFindElement(context, By.id("x"));
		
		assertThat(actual, is(element));
	}

	@Test
	public void safeFindElementWhenElementNotFoundReturnsNullElement() {
		SearchContext context = mock(SearchContext.class);
		when(context.findElement(By.id("x"))).thenThrow(new NoSuchElementException("x"));
		
		WebElement actual = safeFindElement(context, By.id("x"));
		
		assertThat(actual, is(instanceOf(NullWebElement.class)));
	}
	
	/**
	 * @deprecated Tests deprecated method.
	 */
	@Test
	@Deprecated
	public void quietFindElementWhenElementFoundReturnsElement() {
		WebElement element = mock(WebElement.class);
		SearchContext context = mock(SearchContext.class);
		when(context.findElement(By.id("x"))).thenReturn(element);
		
		WebElement actual = quietFindElement(context, By.id("x"));
		
		assertThat(actual, is(element));
	}

	/**
	 * @deprecated Tests deprecated method.
	 */
	@Test
	@Deprecated
	public void quietFindElementWhenElementNotFoundReturnsNull() {
		SearchContext context = mock(SearchContext.class);
		when(context.findElement(By.id("x"))).thenThrow(new NoSuchElementException("x"));
		
		WebElement actual = quietFindElement(context, By.id("x"));
		
		assertThat(actual, is(nullValue()));
	}
	
	@Test
	public void untilWithConditionSatisfiedReturnsTrue() {
		ExpectedCondition<Object> condition = mock(ExpectedCondition.class);
		WebDriverWait wait = mock(WebDriverWait.class);
		when(wait.until(condition)).thenReturn(new Object());
		
		boolean actual = until(wait, condition);
		
		assertThat(actual, is(true));
	}
	
	@Test
	public void untilWithConditionUnsatisfiedReturnsFalse() {
		ExpectedCondition<Object> condition = mock(ExpectedCondition.class);
		WebDriverWait wait = mock(WebDriverWait.class);
		when(wait.until(condition)).thenThrow(new TimeoutException());
		
		boolean actual = until(wait, condition);
		
		assertThat(actual, is(false));
	}
	
	@Test
	public void getTextWithNoElementsReturnsEmptyList() {
		List<String> actual = getText(Collections.<WebElement>emptySet());
		
		assertThat(actual, is(empty()));
	}
	
	@Test
	public void getTextWithElementReturnsText() {
		WebElement element = newElementWithText("x");
		
		List<String> actual = getText(singleton(element));
		
		assertThat(actual, contains("x"));
	}
	
	@Test
	public void getTextWithElementsReturnsText() {
		WebElement element1 = newElementWithText("x");
		WebElement element2 = newElementWithText("y");
		
		List<String> actual = getText(asList(element1, element2));
		
		assertThat(actual, contains("x", "y"));
	}
	
	@Test
	public void acceptAlertAcceptsAlert() {
		WebDriver driver = mock(WebDriver.class);
		Alert alert = mock(Alert.class);
		whenSwitchToAlert(driver).thenReturn(alert);
		
		acceptAlert(driver);
		
		verify(alert).accept();
	}
	
	@Test(expected = NoAlertPresentException.class)
	public void acceptAlertWhenNoAlertThrowsException() {
		WebDriver driver = mock(WebDriver.class);
		whenSwitchToAlert(driver).thenThrow(new NoAlertPresentException());
		
		acceptAlert(driver);
	}
	
	@Test(expected = IllegalStateException.class)
	public void acceptAlertWhenWebDriverExceptionThrowsException() {
		Alert alert = mock(Alert.class);
		doThrow(new WebDriverException()).when(alert).accept();
		WebDriver driver = mock(WebDriver.class);
		whenSwitchToAlert(driver).thenReturn(alert);

		acceptAlert(driver);
	}
	
	@Test
	public void dismissAlertDismissesAlert() {
		WebDriver driver = mock(WebDriver.class);
		Alert alert = mock(Alert.class);
		whenSwitchToAlert(driver).thenReturn(alert);
		
		dismissAlert(driver);
		
		verify(alert).dismiss();
	}
	
	@Test(expected = NoAlertPresentException.class)
	public void dismissAlertWhenNoAlertThrowsException() {
		WebDriver driver = mock(WebDriver.class);
		whenSwitchToAlert(driver).thenThrow(new NoAlertPresentException());
		
		dismissAlert(driver);
	}
	
	@Test(expected = IllegalStateException.class)
	public void dismissAlertWhenWebDriverExceptionThrowsException() {
		Alert alert = mock(Alert.class);
		doThrow(new WebDriverException()).when(alert).dismiss();
		WebDriver driver = mock(WebDriver.class);
		whenSwitchToAlert(driver).thenReturn(alert);

		dismissAlert(driver);
	}
	
	private static WebElement newElementWithText(String text) {
		WebElement element = mock(WebElement.class);
		when(element.getText()).thenReturn(text);
		return element;
	}

	private static OngoingStubbing<Alert> whenSwitchToAlert(WebDriver driver) {
		TargetLocator targetLocator = mock(TargetLocator.class);
		when(driver.switchTo()).thenReturn(targetLocator);
		return when(targetLocator.alert());
	}
}
