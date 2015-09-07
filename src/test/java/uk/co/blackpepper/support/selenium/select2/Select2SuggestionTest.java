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
package uk.co.blackpepper.support.selenium.select2;

import org.junit.Test;
import org.openqa.selenium.WebElement;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Select2SuggestionTest {

	@Test(expected = NullPointerException.class)
	public void constructorWithNullElementThrowsException() {
		new Select2Suggestion(null);
	}
	
	@Test
	public void isNewWhenNewReturnsTrue() {
		Select2Suggestion suggestion = new Select2Suggestion(newNewSuggestion());
		
		assertThat(suggestion.isNew(), is(true));
	}

	@Test
	public void isNewWhenExistingReturnsFalse() {
		Select2Suggestion suggestion = new Select2Suggestion(newSuggestion());
		
		assertThat(suggestion.isNew(), is(false));
	}
	
	@Test
	public void getTextReturnsText() {
		Select2Suggestion suggestion = new Select2Suggestion(newSuggestion("x"));
		
		assertThat(suggestion.getText(), is("x"));
	}
	
	@Test
	public void getTextWhenNewReturnsText() {
		Select2Suggestion suggestion = new Select2Suggestion(newNewSuggestion("New x"));
		
		assertThat(suggestion.getText(), is("x"));
	}
	
	@Test
	public void equalsWithEqualReturnsTrue() {
		WebElement element = newSuggestion();
		Select2Suggestion suggestion1 = new Select2Suggestion(element);
		Select2Suggestion suggestion2 = new Select2Suggestion(element);
		
		assertThat(suggestion1.equals(suggestion2), is(true));
	}
	
	@Test
	public void equalsWithDifferentElementReturnsFalse() {
		Select2Suggestion suggestion1 = new Select2Suggestion(newSuggestion());
		Select2Suggestion suggestion2 = new Select2Suggestion(newSuggestion());
		
		assertThat(suggestion1.equals(suggestion2), is(false));
	}

	private static WebElement newSuggestion() {
		WebElement element = mock(WebElement.class);
		when(element.getAttribute("class")).thenReturn("");
		return element;
	}

	private static WebElement newSuggestion(String text) {
		WebElement element = newSuggestion();
		when(element.getText()).thenReturn(text);
		return element;
	}
	
	private static WebElement newNewSuggestion() {
		WebElement element = mock(WebElement.class);
		when(element.getAttribute("class")).thenReturn("x-select2-new");
		return element;
	}

	private static WebElement newNewSuggestion(String text) {
		WebElement element = newNewSuggestion();
		when(element.getText()).thenReturn(text);
		return element;
	}
}
