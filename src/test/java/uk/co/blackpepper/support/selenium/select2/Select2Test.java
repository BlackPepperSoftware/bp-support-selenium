package uk.co.blackpepper.support.selenium.select2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static java.util.Arrays.asList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class Select2Test {

	@Test
	public void getInputReturnsElement() {
		WebDriver driver = mock(WebDriver.class);
		WebElement element = mock(WebElement.class);
		when(driver.findElement(By.cssSelector("#s2id_x input.select2-input"))).thenReturn(element);
		
		WebElement actual = Select2.getInput(driver, "x");
		
		assertThat(actual, is(element));
	}
	
	@Test
	public void getValuesReturnsValues() {
		WebDriver driver = mock(WebDriver.class);
		WebElement choice1 = newChoice("x");
		WebElement choice2 = newChoice("y");
		when(driver.findElements(any(By.class))).thenReturn(asList(choice1, choice2));
	
		List<String> actual = Select2.getValues(driver, "z");
		
		assertThat(actual, contains("x", "y"));
	}

	@Test
	public void getValuesWhenNoChoicesReturnsEmptyList() {
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElements(any(By.class))).thenReturn(Collections.<WebElement>emptyList());

		List<String> actual = Select2.getValues(driver, "x");
		
		assertThat(actual, is(empty()));
	}

	@Test
	public void getSuggestionsReturnsSuggestions() {
		WebDriver driver = mock(WebDriver.class);
		WebElement element1 = newChoice("x");
		WebElement element2 = newChoice("y");
		when(driver.findElements(any(By.class))).thenReturn(asList(element1, element2));
	
		List<Select2Suggestion> actual = Select2.getSuggestions(driver, "z");
		
		assertThat(actual, contains(new Select2Suggestion(element1), new Select2Suggestion(element2)));
	}

	@Test
	public void getSuggestionsWhenNoChoicesReturnsEmptyList() {
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElements(any(By.class))).thenReturn(Collections.<WebElement>emptyList());
		when(driver.findElement(any(By.class))).thenReturn(mock(WebElement.class));

		List<Select2Suggestion> actual = Select2.getSuggestions(driver, "x");
		
		assertThat(actual, is(empty()));
	}

	@Test
	public void getDropdownItemNamesClicksField() {
		WebDriver driver = mock(WebDriver.class);
		WebElement field = mock(WebElement.class);
		when(driver.findElement(By.cssSelector("#s2id_x a.select2-choice"))).thenReturn(field);
		when(driver.findElements(any(By.class))).thenReturn(new ArrayList<WebElement>());
		expectResults(driver);
		
		Select2.getDropdownItemNames(driver, "x");
		
		verify(field).click();
	}

	@Test
	public void getDropdownItemNamesReturnsNames() {
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElement(By.cssSelector("#s2id_z a.select2-choice"))).thenReturn(mock(WebElement.class));
		WebElement choice1 = newChoice("x");
		WebElement choice2 = newChoice("y");
		when(driver.findElements(any(By.class))).thenReturn(asList(choice1, choice2));
		expectResults(driver);
		
		List<String> actual = Select2.getDropdownItemNames(driver, "z");
		
		assertThat(actual, contains("x", "y"));
	}

	@Test(expected = TimeoutException.class)
	public void getDropdownItemNamesWhenNoChoicesThrowsException() {
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElement(By.cssSelector("#s2id_x a.select2-choice"))).thenReturn(mock(WebElement.class));
		when(driver.findElement(By.className("select2-result-label"))).thenReturn(null);
		
		Select2.getDropdownItemNames(driver, "x");
	}

	@Test
	public void getItemNamesReturnsNames() {
		WebDriver driver = mock(WebDriver.class);
		WebElement element1 = newChoice("x");
		WebElement element2 = newChoice("y");
		when(driver.findElements(any(By.class))).thenReturn(asList(element1, element2));
		expectResults(driver);
		
		List<String> actual = Select2.getItemNames(driver);
		
		assertThat(actual, contains("x", "y"));
	}

	@Test(expected = TimeoutException.class)
	public void getItemNamesWhenNoChoicesReturnsEmptyList() {
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElement(By.className("select2-result-label"))).thenReturn(null);
		
		List<String> actual = Select2.getItemNames(driver);
		
		assertThat(actual, is(empty()));
	}

	@Test
	public void getItemsReturnsItems() {
		WebDriver driver = mock(WebDriver.class);
		WebElement element1 = newChoice("x");
		WebElement element2 = newChoice("y");
		when(driver.findElements(any(By.class))).thenReturn(asList(element1, element2));
		expectResults(driver);
		
		List<WebElement> actual = Select2.getItems(driver);
		
		assertThat(actual, contains(element1, element2));
	}
	
	@Test(expected = TimeoutException.class)
	public void getItemsWhenNoChoicesThrowsException() {
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElement(By.className("select2-result-label"))).thenReturn(null);
		
		Select2.getItems(driver);
	}
	
	@Test
	public void addValuesClicksChoice() {
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElement(By.cssSelector("#s2id_y input.select2-input"))).thenReturn(mock(WebElement.class));
		WebElement choice = newChoice("x");
		when(driver.findElements(By.cssSelector(".select2-result"))).thenReturn(asList(choice));
		
		Select2.addValues(driver, "y", asList("x"));
		
		verify(choice).click();
	}
	
	@Test
	public void addValueSendsKeys() {
		WebDriver driver = mock(WebDriver.class);
		WebElement input = mock(WebElement.class);
		when(driver.findElement(By.cssSelector("#s2id_y input.select2-input"))).thenReturn(input);
		WebElement choice = newChoice("x");
		when(driver.findElements(By.cssSelector(".select2-result"))).thenReturn(asList(choice));
		
		Select2.addValue(driver, "y", "x");
		
		verify(input).sendKeys("x");
	}

	@Test
	public void addValueEscapesKeys() {
		WebDriver driver = mock(WebDriver.class);
		WebElement input = mock(WebElement.class);
		when(driver.findElement(By.cssSelector("#s2id_y input.select2-input"))).thenReturn(input);
		WebElement choice = newChoice("(");
		when(driver.findElements(By.cssSelector(".select2-result"))).thenReturn(asList(choice));
		
		Select2.addValue(driver, "y", "(");
		
		verify(input).sendKeys(Keys.SHIFT + "9" + Keys.NULL);
	}

	@Test
	public void addValueClicksChoice() {
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElement(By.cssSelector("#s2id_y input.select2-input"))).thenReturn(mock(WebElement.class));
		WebElement choice = newChoice("x");
		when(driver.findElements(By.cssSelector(".select2-result"))).thenReturn(asList(choice));
		
		Select2.addValue(driver, "y", "x");
		
		verify(choice).click();
	}

	@Test
	public void addValueWithWhitespaceValueClicksChoice() {
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElement(By.cssSelector("#s2id_y input.select2-input"))).thenReturn(mock(WebElement.class));
		WebElement choice = newChoice("x");
		when(driver.findElements(By.cssSelector(".select2-result"))).thenReturn(asList(choice));

		Select2.addValue(driver, "y", " x ");

		verify(choice).click();
	}

	@Test
	public void addPartialValueSendsKeys() {
		WebDriver driver = mock(WebDriver.class);
		WebElement input = mock(WebElement.class);
		when(driver.findElement(By.cssSelector("#s2id_y input.select2-input"))).thenReturn(input);
		WebElement choice = newChoice("xx");
		when(driver.findElements(By.cssSelector(".select2-result"))).thenReturn(asList(choice));
		
		Select2.addPartialValue(driver, "y", "x", "xx");
		
		verify(input).sendKeys("x");
	}

	@Test
	public void addPartialValueEscapesKeys() {
		WebDriver driver = mock(WebDriver.class);
		WebElement input = mock(WebElement.class);
		when(driver.findElement(By.cssSelector("#s2id_y input.select2-input"))).thenReturn(input);
		WebElement choice = newChoice("((");
		when(driver.findElements(By.cssSelector(".select2-result"))).thenReturn(asList(choice));
		
		Select2.addPartialValue(driver, "y", "(", "((");
		
		verify(input).sendKeys(Keys.SHIFT + "9" + Keys.NULL);
	}
	
	@Test
	public void setDropdownSearchTextSendsKeys() {
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElement(By.cssSelector("#s2id_y a.select2-choice"))).thenReturn(mock(WebElement.class));
		WebElement input = mock(WebElement.class);
		when(driver.findElement(By.cssSelector("#select2-drop input.select2-input"))).thenReturn(input);
		
		Select2.setDropdownSearchText(driver, "y", "x");
		
		verify(input).sendKeys("x");
	}
	
	@Test
	public void setDropdownSearchTextEscapesKeys() {
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElement(By.cssSelector("#s2id_y a.select2-choice"))).thenReturn(mock(WebElement.class));
		WebElement input = mock(WebElement.class);
		when(driver.findElement(By.cssSelector("#select2-drop input.select2-input"))).thenReturn(input);
		
		Select2.setDropdownSearchText(driver, "y", "(");
		
		verify(input).sendKeys(Keys.SHIFT + "9" + Keys.NULL);
	}
	
	@Test
	public void setSearchTextSendsKeys() {
		WebDriver driver = mock(WebDriver.class);
		WebElement input = mock(WebElement.class);
		when(driver.findElement(By.cssSelector("#s2id_y input.select2-input"))).thenReturn(input);
		WebElement choice = newChoice("x");
		when(driver.findElements(By.cssSelector(".select2-result"))).thenReturn(asList(choice));
		
		Select2.setSearchText(driver, "y", "x");
		
		verify(input).sendKeys("x");
	}
	
	@Test
	public void setSearchTextEscapesKeys() {
		WebDriver driver = mock(WebDriver.class);
		WebElement input = mock(WebElement.class);
		when(driver.findElement(By.cssSelector("#s2id_y input.select2-input"))).thenReturn(input);
		WebElement choice = newChoice("(");
		when(driver.findElements(By.cssSelector(".select2-result"))).thenReturn(asList(choice));
		
		Select2.setSearchText(driver, "y", "(");
		
		verify(input).sendKeys(Keys.SHIFT + "9" + Keys.NULL);
	}
	
	@Test
	public void clearClicksChoice() {
		WebDriver driver = mock(WebDriver.class);
		WebElement choice = newChoice("x");
		when(choice.isEnabled()).thenThrow(new StaleElementReferenceException(""));
		when(driver.findElements(any(By.class))).thenReturn(asList(choice));
		
		Select2.clear(driver, "x");
		
		verify(choice).click();
	}

	@Test
	public void clickItemClicksChoice() {
		WebDriver driver = mock(WebDriver.class);
		WebElement choice = newChoice("x");
		when(driver.findElements(By.cssSelector(".select2-result"))).thenReturn(asList(choice));
		
		Select2.clickItem(driver, "x");
		
		verify(choice).click();
	}

	@Test
	public void clickItemWithWhitespaceChoiceClicksChoice() {
		WebDriver driver = mock(WebDriver.class);
		WebElement choice = newChoice("x");
		when(driver.findElements(By.cssSelector(".select2-result"))).thenReturn(asList(choice));
		
		Select2.clickItem(driver, " x ");
		
		verify(choice).click();
	}

	@Test(expected = TimeoutException.class)
	public void clickItemWithUnknownChoiceThrowsException() {
		WebDriver driver = mock(WebDriver.class);
		WebElement choice = newChoice("x");
		when(driver.findElements(By.cssSelector(".select2-result"))).thenReturn(asList(choice));
		
		Select2.clickItem(driver, "y");
	}

	@Test
	public void getHighlightedItemReturnsItem() {
		WebDriver driver = mock(WebDriver.class);
		WebElement choice = mock(WebElement.class);
		when(driver.findElements(By.cssSelector("#select2-drop .select2-highlighted"))).thenReturn(asList(choice));
		expectResults(driver);
		
		WebElement actual = Select2.getHighlightedItem(driver, "x");
		
		assertThat(actual, is(choice));
	}
	
	@Test(expected = TimeoutException.class)
	public void getHighlightedItemWithUnknownChoiceThrowsException() {
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElement(By.className("select2-result-label"))).thenReturn(null);
		
		WebElement actual = Select2.getHighlightedItem(driver, "x");
		
		assertThat(actual, is(nullValue()));
	}
	
	@Test
	public void getHighlightedSuggestionReturnsChoice() {
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElement(By.cssSelector("#select2-drop"))).thenReturn(mock(WebElement.class));
		WebElement choice = newChoice("y");
		when(driver.findElements(By.cssSelector("#select2-drop .select2-highlighted"))).thenReturn(asList(choice));
		expectResults(driver);
		
		Select2Suggestion actual = Select2.getHighlightedSuggestion(driver, "x");
		
		assertThat(actual, is(new Select2Suggestion(choice)));
	}

	@Test(expected = TimeoutException.class)
	public void getHighlightedSuggestionWhenNoChoicesThrowsException() {
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElement(By.className("select2-result-label"))).thenReturn(null);
		
		Select2Suggestion actual = Select2.getHighlightedSuggestion(driver, "x");
		
		assertThat(actual, is(nullValue()));
	}
	
	@Test
	public void isSelectionLimitMessageVisibleWhenVisibleReturnsTrue() {
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElements(By.cssSelector(".select2-selection-limit")))
			.thenReturn(asList(mock(WebElement.class)));
		
		assertThat(Select2.isSelectionLimitMessageVisible(driver), is(true));
	}
	
	@Test
	public void isSelectionLimitMessageVisibleWhenNotVisibleReturnsFalse() {
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElements(By.cssSelector(".select2-selection-limit")))
			.thenReturn(Collections.<WebElement>emptyList());
		
		assertThat(Select2.isSelectionLimitMessageVisible(driver), is(false));
	}

	@Test
	public void isTooFewCharactersMessageVisibleWhenVisibleReturnsTrue() {
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElements(By.cssSelector(".select2-no-results")))
			.thenReturn(asList(mock(WebElement.class)));
		
		assertThat(Select2.isTooFewCharactersMessageVisible(driver), is(true));
	}
	
	@Test
	public void isTooFewCharactersMessageVisibleWhenNotVisibleReturnsFalse() {
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElements(By.cssSelector(".select2-no-results")))
			.thenReturn(Collections.<WebElement>emptyList());
		
		assertThat(Select2.isTooFewCharactersMessageVisible(driver), is(false));
	}
	
	private static void expectResults(WebDriver driver) {
		WebElement result = newDisplayedElement();
		when(driver.findElement(By.className("select2-result-label"))).thenReturn(result);
	}
	
	private static WebElement newDisplayedElement() {
		WebElement element = mock(WebElement.class);
		when(element.isDisplayed()).thenReturn(true);
		return element;
	}
	
	private static WebElement newChoice() {
		WebElement element = newDisplayedElement();
		when(element.getAttribute("class")).thenReturn("");
		return element;
	}
	
	private static WebElement newChoice(String text) {
		WebElement element = newChoice();
		when(element.getText()).thenReturn(text);
		return element;
	}
}
