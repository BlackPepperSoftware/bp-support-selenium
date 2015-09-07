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

import org.junit.Test;
import org.mockito.InOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static com.google.common.collect.Lists.newArrayList;

import static uk.co.blackpepper.support.selenium.FormUtils.getControlValue;
import static uk.co.blackpepper.support.selenium.FormUtils.getOptionLabels;
import static uk.co.blackpepper.support.selenium.FormUtils.getOptionValues;
import static uk.co.blackpepper.support.selenium.FormUtils.getRadioValue;
import static uk.co.blackpepper.support.selenium.FormUtils.hasClass;
import static uk.co.blackpepper.support.selenium.FormUtils.hasError;
import static uk.co.blackpepper.support.selenium.FormUtils.hasFormGroupError;
import static uk.co.blackpepper.support.selenium.FormUtils.isEnabled;
import static uk.co.blackpepper.support.selenium.FormUtils.setCheckboxValue;
import static uk.co.blackpepper.support.selenium.FormUtils.setControlValue;
import static uk.co.blackpepper.support.selenium.FormUtils.setRadioValue;

public class FormUtilsTest {
	
	@Test
	public void getControlValueReturnsValue() {
		WebElement element = mock(WebElement.class);
		when(element.getAttribute("value")).thenReturn("x");
		SearchContext context = mock(SearchContext.class);
		when(context.findElement(By.id("y"))).thenReturn(element);
		
		String actual = getControlValue(context.findElement(By.id("y")));
		
		assertThat(actual, is("x"));
	}
	
	@Test
	public void setControlValueClearsAndSetsValue() {
		SearchContext context = mock(SearchContext.class);
		WebElement element = mock(WebElement.class);
		when(context.findElement(By.id("y"))).thenReturn(element);
		
		setControlValue(context.findElement(By.id("y")), "x");

		InOrder order = inOrder(element);
		order.verify(element).clear();
		order.verify(element).sendKeys("x");
	}
	
	@Test
	public void setCheckboxValueWhenUncheckedWithCheckedClicksCheckbox() {
		WebElement checkbox = newCheckbox();
		
		setCheckboxValue(checkbox, true);
		
		verify(checkbox).click();
	}
	
	@Test
	public void setCheckboxValueWhenUncheckedWithUncheckedDoesNotClickCheckbox() {
		WebElement checkbox = newCheckbox();
		
		setCheckboxValue(checkbox, false);
		
		verify(checkbox, never()).click();
	}
	
	@Test
	public void setCheckboxValueWhenCheckedWithCheckedDoesNotClickCheckbox() {
		WebElement checkbox = newSelectedCheckbox();
		
		setCheckboxValue(checkbox, true);
		
		verify(checkbox, never()).click();
	}
	
	@Test
	public void setCheckboxValueWhenCheckedWithUncheckedClicksCheckbox() {
		WebElement checkbox = newSelectedCheckbox();
		
		setCheckboxValue(checkbox, false);
		
		verify(checkbox).click();
	}

	@Test
	public void getRadioValueWithSelectedRadioReturnsValue() {
		WebElement radio = newSelectedRadioWithValue("x");
		
		String actual = getRadioValue(singletonList(radio));
		
		assertThat(actual, is("x"));
	}

	@Test
	public void getRadioValueWithSelectedRadiosReturnsValue() {
		WebElement radio1 = newRadioWithValue("x");
		WebElement radio2 = newSelectedRadioWithValue("y");
		
		String actual = getRadioValue(newArrayList(radio1, radio2));
		
		assertThat(actual, is("y"));
	}
	
	@Test
	public void getRadioValueWithUnselectedRadioReturnsEmptyString() {
		WebElement radio = newRadioWithValue("x");
		
		String actual = getRadioValue(singletonList(radio));
		
		assertThat(actual, isEmptyString());
	}
	
	@Test
	public void setRadioValueWithValueClicksRadio() {
		WebElement radio = newRadioWithValue("x");
		
		setRadioValue(singletonList(radio), "x");
		
		verify(radio).click();
	}
	
	@Test
	public void setRadioValueWithSecondValueClicksRadio() {
		WebElement radio1 = newRadioWithValue("x");
		WebElement radio2 = newRadioWithValue("y");
		
		setRadioValue(newArrayList(radio1, radio2), "y");
		
		verify(radio2).click();
	}

	@Test(expected = IllegalArgumentException.class)
	public void setRadioValueWithUnknownValueThrowsException() {
		WebElement radio = newRadioWithValue("x");
		
		setRadioValue(singletonList(radio), "y");
	}
	
	@Test
	public void getOptionValuesReturnsValues() {
		WebElement select = newSelect(newOption("x", ""), newOption("y", ""));
		
		List<String> actual = getOptionValues(select);
		
		assertThat(actual, contains("x", "y"));
	}

	@Test
	public void getOptionValuesWithEmptySelectReturnsEmptyList() {
		List<String> actual = getOptionValues(newSelect());
		
		assertThat(actual, is(empty()));
	}

	@Test
	public void getOptionLabelsReturnsLabels() {
		WebElement select = newSelect(newOption("", "x"), newOption("", "y"));
		
		List<String> actual = getOptionLabels(select);
		
		assertThat(actual, contains("x", "y"));
	}

	@Test
	public void getOptionLabelsWithEmptySelectReturnsEmptyList() {
		List<String> actual = getOptionLabels(newSelect());
		
		assertThat(actual, is(empty()));
	}

	@Test
	public void isEnabledWithoutDisabledReturnsTrue() {
		WebElement element = mock(WebElement.class);
		
		assertThat(isEnabled(element), is(true));
	}
	
	@Test
	public void isEnabledWithDisabledReturnsFalse() {
		WebElement element = mock(WebElement.class);
		when(element.getAttribute("disabled")).thenReturn("true");
		
		assertThat(isEnabled(element), is(false));
	}
	
	@Test
	public void hasFormGroupErrorWithErrorReturnsTrue() {
		WebElement formGroup = newElementWithClass("has-error");
		WebElement field = mock(WebElement.class);
		when(field.findElements(any(By.class))).thenReturn(singletonList(formGroup));
		
		assertThat(hasFormGroupError(field), is(true));
	}
	
	@Test
	public void hasFormGroupErrorWithoutErrorReturnsFalse() {
		WebElement formGroup = newElementWithClass("x");
		WebElement field = mock(WebElement.class);
		when(field.findElements(any(By.class))).thenReturn(singletonList(formGroup));
		
		assertThat(hasFormGroupError(field), is(false));
	}
	
	@Test
	public void hasErrorWithClassReturnsTrue() {
		assertThat(hasError(newElementWithClass("has-error")), is(true));
	}

	@Test
	public void hasErrorWithoutClassReturnsFalse() {
		assertThat(hasError(newElementWithClass("x")), is(false));
	}
	
	@Test
	public void hasClassWithExactClassReturnsTrue() {
		assertThat(hasClass(newElementWithClass("x"), "x"), is(true));
	}
	
	@Test
	public void hasClassWithContainedClassReturnsTrue() {
		assertThat(hasClass(newElementWithClass("x y z"), "y"), is(true));
	}

	@Test
	public void hasClassWithContainedClassAndWhitespaceReturnsTrue() {
		assertThat(hasClass(newElementWithClass("x  y  z"), "y"), is(true));
	}

	@Test
	public void hasClassWithoutClassReturnsFalse() {
		assertThat(hasClass(newElementWithClass("x"), "y"), is(false));
	}

	private static WebElement newElementWithClass(String cssClass) {
		WebElement element = mock(WebElement.class);
		when(element.getAttribute("class")).thenReturn(cssClass);
		return element;
	}
	
	private static WebElement newCheckbox() {
		return mock(WebElement.class);
	}
	
	private static WebElement newSelectedCheckbox() {
		WebElement checkbox = newCheckbox();
		when(checkbox.isSelected()).thenReturn(true);
		return checkbox;
	}

	private static WebElement newRadioWithValue(String value) {
		WebElement radio = mock(WebElement.class);
		when(radio.getAttribute("value")).thenReturn(value);
		return radio;
	}
	
	private static WebElement newSelectedRadioWithValue(String value) {
		WebElement radio = newRadioWithValue(value);
		when(radio.isSelected()).thenReturn(true);
		return radio;
	}
	
	private static WebElement newSelect(WebElement... options) {
		WebElement select = mock(WebElement.class);
		when(select.getTagName()).thenReturn("select");
		when(select.findElements(By.tagName("option"))).thenReturn(asList(options));
		return select;
	}
	
	private static WebElement newOption(String value, String label) {
		WebElement option = mock(WebElement.class);
		when(option.getAttribute("value")).thenReturn(value);
		when(option.getText()).thenReturn(label);
		return option;
	}
}
