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

import org.hamcrest.Matchers;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WebElementUtilsTest {
	
	@Test
	public void escapeKeysPreservesRegularKeys() {
		CharSequence[] actual = WebElementUtils.escapeKeys("x", "y");
		
		assertThat(actual, is(Matchers.<CharSequence>arrayContaining("x", "y")));
	}

	@Test
	public void escapeKeysEscapesOpenBracket() {
		CharSequence[] actual = WebElementUtils.escapeKeys("(");
		
		assertThat(actual, is(Matchers.<CharSequence>arrayContaining(Keys.SHIFT + "9" + Keys.NULL)));
	}
	
	@Test
	public void escapeKeysEscapesSubstring() {
		CharSequence[] actual = WebElementUtils.escapeKeys("x(y");
		
		assertThat(actual, is(Matchers.<CharSequence>arrayContaining("x" + Keys.SHIFT + "9" + Keys.NULL + "y")));
	}
	
	@Test
	public void scrollIntoViewScrollsElement() {
		JavascriptExecutor javascriptExecutor = mock(JavascriptExecutor.class);
		when(javascriptExecutor.executeScript("return document.documentElement.clientWidth")).thenReturn(0L);
		when(javascriptExecutor.executeScript("return document.documentElement.clientHeight")).thenReturn(1L);
		WebElement webElement = mock(WebElement.class);
		when(webElement.getLocation()).thenReturn(new Point(0, 2));
		when(webElement.getSize()).thenReturn(new Dimension(0, 3));
		
		WebElementUtils.scrollIntoView(javascriptExecutor, webElement);
		
		verify(javascriptExecutor).executeScript("window.scrollTo(0, 4)");
	}
}
