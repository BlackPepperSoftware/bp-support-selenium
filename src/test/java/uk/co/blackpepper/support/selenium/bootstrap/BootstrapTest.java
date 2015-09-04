package uk.co.blackpepper.support.selenium.bootstrap;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static java.util.Arrays.asList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static uk.co.blackpepper.support.selenium.bootstrap.Bootstrap.getDropdownMenuOptionLabels;

public class BootstrapTest {
	
	@Test
	public void getDropdownMenuOptionLabelsWhenEmptyOptionsReturnsEmptyList() {
		WebElement dropdownMenu = newDropdownMenuElement();
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElement(By.id("x"))).thenReturn(dropdownMenu);
		
		List<String> actual = getDropdownMenuOptionLabels(driver, "x");
		
		assertThat(actual, is(empty()));
	}
	
	@Test
	public void getDropdownMenuOptionLabelsWhenOptionReturnsLabels() {
		WebElement dropdownMenu = newDropdownMenuElement(newOption("y"));
		WebDriver driver = mock(WebDriver.class);
		when(driver.findElement(By.id("x"))).thenReturn(dropdownMenu);
		
		List<String> actual = getDropdownMenuOptionLabels(driver, "x");
		
		assertThat(actual, contains("y"));
	}

	private static WebElement newDropdownMenuElement(WebElement... options) {
		WebElement dropdownMenu = mock(WebElement.class);
		when(dropdownMenu.findElements(By.tagName("li"))).thenReturn(asList(options));
		return dropdownMenu;
	}

	private static WebElement newOption(String label) {
		WebElement option = mock(WebElement.class);
		when(option.getText()).thenReturn(label);
		return option;
	}
}
