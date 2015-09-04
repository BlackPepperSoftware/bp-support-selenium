package uk.co.blackpepper.support.selenium;

import java.util.List;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
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
}
