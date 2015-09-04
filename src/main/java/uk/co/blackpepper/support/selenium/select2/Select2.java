package uk.co.blackpepper.support.selenium.select2;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import static org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf;

import static uk.co.blackpepper.support.selenium.WebElementUtils.escapeKeys;

public final class Select2 {

	private static final String SELECT2_DROPDOWN_SELECTOR = "#select2-drop";

	private static final String SELECT2_DROPDOWN_INPUT_SELECTOR = SELECT2_DROPDOWN_SELECTOR + " input.select2-input";

	private static final String SELECT2_SELECTOR = "#s2id_%s";

	private static final String CHOICE_SELECTOR = SELECT2_SELECTOR + " a.select2-choice";

	private static final String CHOICES_SELECTOR = SELECT2_SELECTOR + " li.select2-search-choice";

	private static final String CLOSE_CHOICES_SELECTOR = CHOICES_SELECTOR + " a.select2-search-choice-close";

	private static final String INPUT_SELECTOR = SELECT2_SELECTOR + " input.select2-input";

	private static final String ITEMS_SELECTOR = ".select2-result";

	private static final String HIGHLIGHTED_ITEM_SELECTOR = SELECT2_DROPDOWN_SELECTOR + " .select2-highlighted";

	private static final long CLEAR_TIME_OUT = 1;

	private static final long ITEM_TIME_OUT = 1;

	private Select2() {
		throw new AssertionError();
	}

	public static WebElement getInput(WebDriver driver, String id) {
		return driver.findElement(byInput(id));
	}

	public static List<String> getValues(WebDriver driver, String id) {
		List<WebElement> elements = driver.findElements(byChoices(id));

		return Lists.transform(elements, new Function<WebElement, String>() {
			@Override
			public String apply(WebElement element) {
				return element.getText();
			}
		});
	}

	public static List<Select2Suggestion> getSuggestions(WebDriver driver, String id) {
		List<WebElement> options = driver.findElements(byItems());

		if (options.isEmpty()) {
			WebElement field = driver.findElement(byChoice(id));
			field.click();
			options = driver.findElements(byItems());
		}

		return Lists.transform(options, new Function<WebElement, Select2Suggestion>() {
			@Override
			public Select2Suggestion apply(WebElement input) {
				return new Select2Suggestion(input);
			}
		});
	}

	public static List<String> getDropdownItemNames(WebDriver driver, String id) {
		WebElement field = driver.findElement(byChoice(id));
		field.click();

		return getItemNames(driver);
	}

	public static List<String> getItemNames(WebDriver driver) {
		List<WebElement> options = getItems(driver);

		return Lists.transform(options, new Function<WebElement, String>() {
			@Override
			public String apply(WebElement input) {
				return input.getText();
			}
		});
	}

	public static List<WebElement> getItems(WebDriver driver) {
		waitForItems(driver);
		List<WebElement> options = driver.findElements(byItems());

		return options;
	}

	public static void setValues(WebDriver driver, String id, Iterable<String> values) {
		clear(driver, id);
		addValues(driver, id, values);
	}

	public static void addValues(WebDriver driver, String id, Iterable<String> values) {
		for (String value : values) {
			addValue(driver, id, value);
		}
	}

	public static void addValue(WebDriver driver, String id, String value) {
		WebElement input = driver.findElement(byInput(id));

		input.sendKeys(escapeKeys(value));
		waitForItem(driver, value);
		clickItem(driver, value);
	}

	public static void addPartialValue(WebDriver driver, String id, String partialValue, String value) {
		WebElement input = driver.findElement(byInput(id));

		input.sendKeys(escapeKeys(partialValue));
		waitForItem(driver, value);
		clickItem(driver, value);
	}

	public static void setDropdownSearchText(WebDriver driver, String id, String searchText) {
		WebElement select2 = driver.findElement(byChoice(id));
		select2.click();

		WebElement input = driver.findElement(By.cssSelector(SELECT2_DROPDOWN_INPUT_SELECTOR));

		input.sendKeys(escapeKeys(searchText));
	}

	public static void setSearchText(WebDriver driver, String id, String partialValue) {
		WebElement input = driver.findElement(byInput(id));

		input.sendKeys(escapeKeys(partialValue));
		waitForItem(driver, partialValue);
	}

	public static void clear(WebDriver driver, String id) {
		List<WebElement> elements = driver.findElements(byCloseChoices(id));

		for (WebElement element : elements) {
			element.click();
			new WebDriverWait(driver, CLEAR_TIME_OUT).until(stalenessOf(element));
		}
	}

	public static void clickItem(WebDriver driver, String value) {
		waitForItem(driver, value);
		WebElement item = findItem(driver, value);

		if (item == null) {
			throw new IllegalArgumentException("Select2 item not found: " + value);
		}

		item.click();
	}

	public static Select2Suggestion getHighlightedSuggestion(WebDriver driver, String id) {
		waitForDropDown(driver);

		WebElement suggestion = getHighlightedItem(driver, id);

		return suggestion == null ? null : new Select2Suggestion(suggestion);
	}

	public static WebElement getHighlightedItem(WebDriver driver, String id) {
		waitForItems(driver);

		List<WebElement> webElements = driver.findElements(By.cssSelector(HIGHLIGHTED_ITEM_SELECTOR));

		return webElements.size() == 0 ? null : webElements.get(0);
	}

	public static boolean isSelectionLimitMessageVisible(WebDriver driver) {
		return !driver.findElements(By.cssSelector(".select2-selection-limit")).isEmpty();
	}

	public static Boolean isTooFewCharactersMessageVisible(WebDriver driver) {
		// TODO: Ensure that element text matches: "Please enter %d more character"
		return !driver.findElements(By.cssSelector(".select2-no-results")).isEmpty();
	}

	private static List<Select2Suggestion> getSuggestedItems(WebDriver driver) {
		List<Select2Suggestion> suggestions = new ArrayList<>();

		for (WebElement item : driver.findElements(byItems())) {
			suggestions.add(new Select2Suggestion(item));
		}

		return suggestions;
	}

	private static WebElement findItem(WebDriver driver, String value) {
		List<Select2Suggestion> suggestions = getSuggestedItems(driver);

		for (Select2Suggestion suggestion : suggestions) {
			if (value.trim().equals(suggestion.getText())) {
				return suggestion.getWebElement();
			}
		}

		return null;
	}

	private static boolean isItemVisible(WebDriver driver, String value) {
		return findItem(driver, value) != null;
	}

	private static void waitForItem(WebDriver driver, String value) {
		new WebDriverWait(driver, ITEM_TIME_OUT).until(itemIsVisible(driver, value));
	}

	private static Predicate<WebDriver> itemIsVisible(WebDriver driver, final String value) {
		return new Predicate<WebDriver>() {
			@Override
			public boolean apply(WebDriver driver) {
				return isItemVisible(driver, value);
			}

			@Override
			public String toString() {
				return String.format("Select2 item '%s' to be visible", value);
			}
		};
	}

	private static void waitForDropDown(WebDriver driver) {
		new WebDriverWait(driver, ITEM_TIME_OUT).until(dropDownIsVisible(driver));
	}

	private static Predicate<WebDriver> dropDownIsVisible(WebDriver driver) {
		return new Predicate<WebDriver>() {
			@Override
			public boolean apply(WebDriver driver) {
				return null != driver.findElement(By.cssSelector(SELECT2_DROPDOWN_SELECTOR));
			}

			@Override
			public String toString() {
				return "Select2 drop to be visible";
			}
		};
	}

	private static void waitForItems(WebDriver driver) {
		new WebDriverWait(driver, ITEM_TIME_OUT).until(itemsIsVisible(driver));
	}

	private static Predicate<WebDriver> itemsIsVisible(WebDriver driver) {
		return new Predicate<WebDriver>() {
			@Override
			public boolean apply(WebDriver driver) {
				WebElement result = driver.findElement(By.className("select2-result-label"));
				return result == null ? false : result.isDisplayed();
			}

			@Override
			public String toString() {
				return "Select2 items to be visible";
			}
		};
	}

	private static By byChoice(String id) {
		return By.cssSelector(String.format(CHOICE_SELECTOR, id));
	}

	private static By byChoices(String id) {
		return By.cssSelector(String.format(CHOICES_SELECTOR, id));
	}

	private static By byCloseChoices(String id) {
		return By.cssSelector(String.format(CLOSE_CHOICES_SELECTOR, id));
	}

	private static By byInput(String id) {
		return By.cssSelector(String.format(INPUT_SELECTOR, id));
	}

	private static By byItems() {
		return By.cssSelector(ITEMS_SELECTOR);
	}
}
