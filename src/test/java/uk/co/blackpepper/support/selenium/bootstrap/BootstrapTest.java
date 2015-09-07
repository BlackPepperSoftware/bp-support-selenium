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
