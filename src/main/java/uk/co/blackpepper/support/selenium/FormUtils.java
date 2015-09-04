package uk.co.blackpepper.support.selenium;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public final class FormUtils {
	
	private FormUtils() {
		throw new AssertionError();
	}
	
	public static String getControlValue(WebElement control) {
		return control.getAttribute("value");
	}
	
	public static void setControlValue(WebElement control, String value) {
		control.clear();
		control.sendKeys(value);
	}

	public static void setCheckboxValue(WebElement checkbox, boolean checked) {
		if (checkbox.isSelected() != checked) {
			checkbox.click();
		}
	}
	
	public static String getRadioValue(List<WebElement> radios) {
		for (WebElement radio : radios) {
			if (radio.isSelected()) {
				return radio.getAttribute("value");
			}
		}
		
		return "";
	}
	
	public static void setRadioValue(List<WebElement> radios, String value) {
		for (WebElement radio : radios) {
			if (value.equals(radio.getAttribute("value"))) {
				radio.click();
				return;
			}
		}
		
		throw new IllegalArgumentException(String.format("Unknown radio value: %s", value));
	}
	
	public static List<String> getOptionValues(WebElement element) {
		List<WebElement> options = new Select(element).getOptions();
		
		return Lists.transform(options, new Function<WebElement, String>() {
			@Override
			public String apply(WebElement option) {
				return option.getAttribute("value");
			}
		});
	}
	
	public static List<String> getOptionLabels(WebElement element) {
		List<WebElement> options = new Select(element).getOptions();
		
		return Lists.transform(options, new Function<WebElement, String>() {
			@Override
			public String apply(WebElement option) {
				return option.getText();
			}
		});
	}
	
	public static boolean isEnabled(WebElement element) {
		String disabled = element.getAttribute("disabled");
		return !"true".equals(disabled);
	}
	
	public static boolean hasFormGroupError(WebElement element) {
		List<WebElement> formGroups = element.findElements(byFormGroup());
		
		if (formGroups.isEmpty()) {
			return false;
		}
		
		return hasError(formGroups.iterator().next());
	}
	
	public static boolean hasError(WebElement element) {
		return hasClass(element, "has-error");
	}
	
	public static boolean hasClass(WebElement element, String className) {
		String cssClass = element.getAttribute("class");
		Iterable<String> classes = Splitter.on(" ").split(cssClass);
		
		return Iterables.contains(classes, className);
	}

	private static By byFormGroup() {
		return By.xpath("ancestor::*[contains(concat(' ', @class, ' '), ' form-group ')]");
	}
}
