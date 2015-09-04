package uk.co.blackpepper.support.selenium.jqueryui;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static java.util.Arrays.asList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static uk.co.blackpepper.support.selenium.jqueryui.AutoComplete.clickSuggestion;
import static uk.co.blackpepper.support.selenium.jqueryui.AutoComplete.getSuggestions;
import static uk.co.blackpepper.support.selenium.jqueryui.AutoComplete.isVisible;

public class AutoCompleteTest {

	@Test
	public void isVisibleWithDisplayedSuggestionReturnsTrue() {
		WebElement suggestion = newSuggestion("x");
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElements(any(By.class))).thenReturn(asList(suggestion));
		
		assertThat(isVisible(driver), is(true));
	}

	@Test
	public void isVisibleWithUndisplayedSuggestionReturnsFalse() {
		WebElement suggestion = newUndisplayedElement();
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElements(any(By.class))).thenReturn(asList(suggestion));
		
		assertThat(isVisible(driver), is(false));
	}

	@Test
	public void isVisibleWithNoSuggestionsReturnsFalse() {
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElements(any(By.class))).thenReturn(Collections.<WebElement>emptyList());
		
		assertThat(isVisible(driver), is(false));
	}

	@Test
	public void getSuggestionsWithSuggestionReturnsText() {
		WebElement suggestion = newSuggestion("x");
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElements(any(By.class))).thenReturn(asList(suggestion));
		
		List<String> actual = getSuggestions(driver);
		
		assertThat(actual, contains("x"));
	}

	@Test
	public void getSuggestionsWithSuggestionsReturnsTexts() {
		WebElement suggestion1 = newSuggestion("x");
		WebElement suggestion2 = newSuggestion("y");
		WebElement suggestion3 = newSuggestion("z");
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElements(any(By.class))).thenReturn(asList(suggestion1, suggestion2, suggestion3));
		
		List<String> actual = getSuggestions(driver);
		
		assertThat(actual, contains("x", "y", "z"));
	}
	
	@Test
	public void clickSuggestionClicksSuggestion() {
		WebElement suggestion = newSuggestion("x");
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElements(any(By.class))).thenReturn(asList(suggestion));
		
		clickSuggestion(driver, "x");
		
		verify(suggestion).click();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void clickSuggestionWithUnknownSuggestionThrowsException() {
		WebElement suggestion = newSuggestion("x");
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElements(any(By.class))).thenReturn(asList(suggestion));
		
		clickSuggestion(driver, "y");
	}
	
	private static WebElement newUndisplayedElement() {
		WebElement suggestion = mock(WebElement.class);
		when(suggestion.isDisplayed()).thenReturn(false);
		return suggestion;
	}

	private static WebElement newSuggestion(String text) {
		WebElement element = mock(WebElement.class);
		when(element.isDisplayed()).thenReturn(true);
		when(element.getText()).thenReturn(text);
		return element;
	}
}
