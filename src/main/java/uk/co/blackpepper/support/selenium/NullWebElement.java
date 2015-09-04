package uk.co.blackpepper.support.selenium;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

final class NullWebElement implements WebElement {
	
	private static final NullWebElement INSTANCE = new NullWebElement();
	
	private NullWebElement() {
		// singleton
	}
	
	public static NullWebElement get() {
		return INSTANCE;
	}

	@Override
	public void click() {
		// no-op
	}

	@Override
	public void submit() {
		// no-op
	}

	@Override
	public void sendKeys(CharSequence... keysToSend) {
		// no-op
	}

	@Override
	public void clear() {
		// no-op
	}

	@Override
	public String getTagName() {
		return null;
	}

	@Override
	public String getAttribute(String name) {
		return null;
	}

	@Override
	public boolean isSelected() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

	@Override
	public String getText() {
		return null;
	}

	@Override
	public List<WebElement> findElements(By by) {
		return null;
	}

	@Override
	public WebElement findElement(By by) {
		return null;
	}

	@Override
	public boolean isDisplayed() {
		return false;
	}

	@Override
	public Point getLocation() {
		return null;
	}

	@Override
	public Dimension getSize() {
		return null;
	}

	@Override
	public String getCssValue(String propertyName) {
		return null;
	}
}
