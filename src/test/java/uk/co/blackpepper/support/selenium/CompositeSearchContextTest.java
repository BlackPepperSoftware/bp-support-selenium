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

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import static java.util.Arrays.asList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CompositeSearchContextTest {
	
	private static final List<WebElement> EMPTY_ELEMENTS = Collections.<WebElement>emptyList();

	private By by;
	
	private WebElement found;
	
	private WebElement found2;

	@Before
	public void setUp() {
		by = mock(By.class);
		found = mock(WebElement.class);
		found2 = mock(WebElement.class);
	}
	
	@Test
	public void findElementsWhenFirstChildFindsElementsReturnsElements() {
		SearchContext child1 = mock(SearchContext.class);
		when(child1.findElements(by)).thenReturn(asList(found));
		SearchContext child2 = mock(SearchContext.class);
		when(child2.findElements(by)).thenReturn(EMPTY_ELEMENTS);
		CompositeSearchContext element = new CompositeSearchContext(asList(child1, child2));
		
		List<WebElement> actual = element.findElements(by);
		
		assertThat(actual, contains(found));
	}
	
	@Test
	public void findElementsWhenSecondChildFindsElementsReturnsElements() {
		SearchContext child1 = mock(SearchContext.class);
		when(child1.findElements(by)).thenReturn(EMPTY_ELEMENTS);
		SearchContext child2 = mock(SearchContext.class);
		when(child2.findElements(by)).thenReturn(asList(found));
		CompositeSearchContext element = new CompositeSearchContext(asList(child1, child2));
		
		List<WebElement> actual = element.findElements(by);
		
		assertThat(actual, contains(found));
	}
	
	@Test
	public void findElementsWhenMultipleChildrenFindElementsReturnsAllElements() {
		SearchContext child1 = mock(SearchContext.class);
		when(child1.findElements(by)).thenReturn(asList(found));
		SearchContext child2 = mock(SearchContext.class);
		when(child2.findElements(by)).thenReturn(asList(found2));
		CompositeSearchContext element = new CompositeSearchContext(asList(child1, child2));
		
		List<WebElement> actual = element.findElements(by);
		
		assertThat(actual, contains(found, found2));
	}
	
	@Test
	public void findElementsWhenNoChildFindsElementsReturnsEmptyElements() {
		SearchContext child = mock(SearchContext.class);
		when(child.findElements(by)).thenReturn(EMPTY_ELEMENTS);
		CompositeSearchContext element = new CompositeSearchContext(asList(child));
		
		List<WebElement> actual = element.findElements(by);
		
		assertThat(actual, is(empty()));
	}
	
	@Test
	public void findElementsWhenNoChildrenReturnsEmptyElements() {
		CompositeSearchContext element = new CompositeSearchContext(EMPTY_ELEMENTS);
		
		List<WebElement> actual = element.findElements(by);
		
		assertThat(actual, is(empty()));
	}
	
	@Test
	public void findElementWhenFirstChildFindsElementReturnsElement() {
		SearchContext child1 = mock(SearchContext.class);
		when(child1.findElement(by)).thenReturn(found);
		SearchContext child2 = mock(SearchContext.class);
		when(child2.findElement(by)).thenThrow(new NoSuchElementException("x"));
		CompositeSearchContext element = new CompositeSearchContext(asList(child1, child2));
		
		WebElement actual = element.findElement(by);
		
		assertThat(actual, is(found));
	}
	
	@Test
	public void findElementWhenSecondChildFindsElementReturnsElement() {
		SearchContext child1 = mock(SearchContext.class);
		when(child1.findElement(by)).thenThrow(new NoSuchElementException("x"));
		SearchContext child2 = mock(SearchContext.class);
		when(child2.findElement(by)).thenReturn(found);
		CompositeSearchContext element = new CompositeSearchContext(asList(child1, child2));
		
		WebElement actual = element.findElement(by);
		
		assertThat(actual, is(found));
	}
	
	@Test(expected = NoSuchElementException.class)
	public void findElementWhenNoChildFindsElementThrowsException() {
		SearchContext child = mock(SearchContext.class);
		when(child.findElement(by)).thenThrow(new NoSuchElementException("x"));
		CompositeSearchContext element = new CompositeSearchContext(asList(child));
		
		element.findElement(by);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void findElementWhenNoChildrenThrowsException() {
		CompositeSearchContext element = new CompositeSearchContext(EMPTY_ELEMENTS);
		
		element.findElement(by);
	}
	
	@Test
	public void getChildrenReturnsChildren() {
		SearchContext child1 = mock(SearchContext.class);
		SearchContext child2 = mock(SearchContext.class);
		CompositeSearchContext element = new CompositeSearchContext(asList(child1, child2));
		
		List<? extends SearchContext> actual = element.getChildren();
		
		assertThat(actual, contains(child1, child2));
	}
}
