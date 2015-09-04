package uk.co.blackpepper.support.selenium;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.annotations.VisibleForTesting;

public final class WebElementUtils {

	private WebElementUtils() {
		throw new AssertionError();
	}
	
	/**
	 * Escapes characters incorrectly handled by {@code WebElement.sendKeys} to workaround Selenium issue #1723.
	 * 
	 * @see WebElement#sendKeys(CharSequence...)
	 * @see <a href="https://code.google.com/p/selenium/issues/detail?id=1723">Issue #1723</a>
	 */
	public static CharSequence[] escapeKeys(CharSequence... keys) {
		CharSequence[] escapedKeys = new CharSequence[keys.length];
		
		for (int index = 0; index < keys.length; index++) {
			escapedKeys[index] = escapeKeys(keys[index]);
		}
		
		return escapedKeys;
	}
	
	public static void scrollIntoView(WebDriver driver, WebElement element) {
		scrollIntoView(((JavascriptExecutor) driver), element);
	}
	
	@VisibleForTesting
	static void scrollIntoView(JavascriptExecutor driver, WebElement element) {
		// Based on from http://www.anggrianto.com/blog/scroll-element-view-webdriver/
		// The above scrolls to the top, taking into consideration the viewport size only scrolls enough
		// to make the element visible.
		int y = element.getLocation().getY() + element.getSize().getHeight();
		int viewportHeight = getBrowserViewportSize(driver).getHeight();
		
		driver.executeScript(String.format("window.scrollTo(0, %d)", y - viewportHeight));
	}
	
	private static CharSequence escapeKeys(CharSequence keys) {
		return keys.toString()
			.replace("(", Keys.chord(Keys.SHIFT, "9"));
	}

	private static Dimension getBrowserViewportSize(JavascriptExecutor driver) {
		// Based on https://groups.google.com/d/msg/selenium-users/3-IehHyOZ8w/6VrDf_5HY_0J
		Long width = (Long) driver.executeScript("return document.documentElement.clientWidth");
		Long height = (Long) driver.executeScript("return document.documentElement.clientHeight");
		
		return new Dimension(width.intValue(), height.intValue());
	}
}
