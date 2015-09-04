package uk.co.blackpepper.support.selenium.bootstrap;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public final class Bootstrap {
	
	private Bootstrap() {
		throw new AssertionError();
	}
	
	public static List<String> getDropdownMenuOptionLabels(WebDriver driver, String dropdownMenuId) {
		List<WebElement> elements = driver.findElement(By.id(dropdownMenuId)).findElements(By.tagName("li"));
		List<String> options = new ArrayList<>();
		
		for (WebElement element : elements) {
			options.add(element.getText());
		}
		
		return options;
	}
}
