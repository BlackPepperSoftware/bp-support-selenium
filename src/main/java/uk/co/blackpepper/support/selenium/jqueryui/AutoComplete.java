package uk.co.blackpepper.support.selenium.jqueryui;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;

public final class AutoComplete {
	
	private static final long ITEM_TIME_OUT = 1;

	private AutoComplete() {
		throw new AssertionError();
	}
	
	public static boolean isVisible(WebDriver driver) {
		try {
			waitForSuggestions(driver);
			return true;
		}
		catch (TimeoutException exception) {
			return false;
		}
	}
	
	public static List<String> getSuggestions(WebDriver driver) {
		waitForSuggestions(driver);
		
		List<String> suggestions = new ArrayList<>();
		
		for (WebElement element : driver.findElements(bySuggestions())) {
			suggestions.add(element.getText());
		}
		
		return suggestions;
	}
	
	public static void clickSuggestion(WebDriver driver, String suggestion) {
		waitForSuggestions(driver);
		
		findSuggestion(driver, suggestion).click();
	}

	private static WebElement findSuggestion(WebDriver driver, String suggestion) {
		for (WebElement element : driver.findElements(bySuggestions())) {
			if (suggestion.equals(element.getText())) {
				return element;
			}
		}
		
		throw new IllegalArgumentException("Suggestion not found: " + suggestion);
	}
	
	private static void waitForSuggestions(WebDriver driver) {
		new WebDriverWait(driver, ITEM_TIME_OUT).until(new Predicate<WebDriver>() {
			@Override
			public boolean apply(WebDriver driver) {
				for (WebElement element : driver.findElements(byAutoComplete())) {
					if (element.isDisplayed()) {
						return true;
					}
				}
				
				return false;
			}

			@Override
			public String toString() {
				return "jQueryUI auto-complete to be visible";
			}
		});
	}
	
	private static By byAutoComplete() {
		return By.cssSelector(".ui-autocomplete");
	}
	
	private static By bySuggestions() {
		return By.cssSelector(".ui-autocomplete li a");
	}
}
