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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class WebDriverUtils {
	
	private WebDriverUtils() {
		throw new AssertionError();
	}

	public static WebElement safeFindElement(SearchContext context, By by) {
		WebElement element = quietFindElement(context, by);
		
		if (element == null) {
			element = NullWebElement.get();
		}
		
		return element;
	}
	
	/**
	 * @deprecated Use {@link SearchContext#findElements(By)} with {@link List#isEmpty()} instead.
	 */
	@Deprecated
	public static WebElement quietFindElement(SearchContext context, By by) {
		try {
			return context.findElement(by);
		}
		catch (NoSuchElementException exception) {
			return null;
		}
	}

	public static boolean until(WebDriverWait wait, ExpectedCondition<?> condition) {
		try {
			wait.until(condition);
			return true;
		}
		catch (TimeoutException exception) {
			return false;
		}
	}

	public static List<String> getText(Collection<WebElement> elements) {
		List<String> texts = new ArrayList<>();
		
		for (WebElement element : elements) {
			texts.add(element.getText());
		}
		
		return texts;
	}
	
	public static void acceptAlert(WebDriver driver) {
		// The loop here is to work around the intermittent issue described by:
		// https://code.google.com/p/selenium/issues/detail?id=3544
		int tries = 4;
		while (tries > 0) {
			try {
				driver.switchTo().alert().accept();
				return;
			}
			catch (NoAlertPresentException exception) {
				throw exception;
			}
			catch (WebDriverException exception) {
				tries--;
				pause(250L);
			}
		}
		
		throw new IllegalStateException("Unable to accept alert");
	}
	
	public static void dismissAlert(WebDriver driver) {
		// The loop here is to work around the intermittent issue described by:
		// https://code.google.com/p/selenium/issues/detail?id=3544
		int tries = 4;
		while (tries > 0) {
			try {
				driver.switchTo().alert().dismiss();
				return;
			}
			catch (NoAlertPresentException exception) {
				throw exception;
			}
			catch (WebDriverException exception) {
				tries--;
				pause(250L);
			}
		}
		
		throw new IllegalStateException("Unable to dismiss alert");
	}
	
	/**
	 * @deprecated Use {@link Thread#sleep(long)} instead.
	 */
	@Deprecated
	public static void pause(long millis) {
		long endTime = System.currentTimeMillis();
		long sleepTime = millis;
		while (sleepTime > 0) {
			try {
				Thread.sleep(sleepTime);
			}
			catch (InterruptedException exception) {
				// Not a lot we can do really
			}
			sleepTime = endTime - System.currentTimeMillis();
		}
	}
}
