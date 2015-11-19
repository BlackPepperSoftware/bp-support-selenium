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

import java.util.List;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import static java.util.Arrays.asList;

public class CompositeWebElement extends CompositeSearchContext implements WebElement {

	public CompositeWebElement(WebElement... elements) {
		this(asList(elements));
	}
	
	public CompositeWebElement(List<? extends WebElement> elements) {
		super(elements);
	}

	@Override
	public void click() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void submit() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void sendKeys(CharSequence... keysToSend) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getTagName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getAttribute(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSelected() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEnabled() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getText() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDisplayed() {
		boolean displayed = true;
		
		for (WebElement element : getChildren()) {
			displayed &= element.isDisplayed();
		}
		
		return displayed;
	}

	@Override
	public Point getLocation() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Dimension getSize() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getCssValue(String propertyName) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected List<? extends WebElement> getChildren() {
		return (List<? extends WebElement>) super.getChildren();
	}

	@Override
	public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
		throw new UnsupportedOperationException();
	}
}
