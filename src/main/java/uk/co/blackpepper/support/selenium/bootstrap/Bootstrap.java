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
