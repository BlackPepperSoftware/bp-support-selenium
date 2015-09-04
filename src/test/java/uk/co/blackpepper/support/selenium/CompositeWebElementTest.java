package uk.co.blackpepper.support.selenium;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.WebElement;

import static java.util.Arrays.asList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CompositeWebElementTest {
	
	@Test
	public void isDisplayedWhenChildrenDisplayedReturnsTrue() {
		CompositeWebElement element = new CompositeWebElement(newDisplayedElement(), newDisplayedElement());
		
		assertThat(element.isDisplayed(), is(true));
	}
	
	@Test
	public void isDisplayedWhenFirstChildIsUndisplayedReturnsFalse() {
		CompositeWebElement element = new CompositeWebElement(newUndisplayedElement(), newDisplayedElement());
		
		assertThat(element.isDisplayed(), is(false));
	}

	@Test
	public void isDisplayedWhenSecondChildIsUndisplayedReturnsFalse() {
		CompositeWebElement element = new CompositeWebElement(newDisplayedElement(), newUndisplayedElement());
		
		assertThat(element.isDisplayed(), is(false));
	}

	@Test
	public void isDisplayedWhenChildrenUndisplayedReturnsFalse() {
		CompositeWebElement element = new CompositeWebElement(newUndisplayedElement(), newUndisplayedElement());
		
		assertThat(element.isDisplayed(), is(false));
	}
	
	@Test
	public void isDisplayedWhenNoChildrenReturnsTrue() {
		CompositeWebElement element = new CompositeWebElement();
		
		assertThat(element.isDisplayed(), is(true));
	}
	
	@Test
	public void getChildrenReturnsChildren() {
		WebElement child1 = mock(WebElement.class);
		WebElement child2 = mock(WebElement.class);
		CompositeWebElement element = new CompositeWebElement(asList(child1, child2));
		
		List<? extends WebElement> actual = element.getChildren();
		
		assertThat(actual, contains(child1, child2));
	}

	private static WebElement newDisplayedElement() {
		WebElement element = mock(WebElement.class);
		when(element.isDisplayed()).thenReturn(true);
		return element;
	}
	
	private static WebElement newUndisplayedElement() {
		WebElement element = mock(WebElement.class);
		when(element.isDisplayed()).thenReturn(false);
		return element;
	}
}
