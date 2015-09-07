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
package uk.co.blackpepper.support.selenium.jqueryui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.google.common.annotations.VisibleForTesting;

import static com.google.common.base.Preconditions.checkArgument;

public final class Slider {

	private Slider() {
		throw new AssertionError();
	}
	
	public static void dragHandle(WebDriver driver, WebElement slider, int percent) {
		WebElement sliderHandle = getSliderHandle(slider);
		
		dragSliderByAmount(driver, sliderHandle, getDragAmount(slider, percent));
	}
	
	private static void dragSliderByAmount(WebDriver driver, WebElement sliderHandle, int dragAmount) {
		(new Actions(driver)).dragAndDropBy(sliderHandle, dragAmount, 0).perform();
	}
	
	@VisibleForTesting
	static WebElement getSliderHandle(WebElement slider) {
		return slider.findElement(By.className("ui-slider-handle"));
	}
	
	@VisibleForTesting
	static int getDragAmount(WebElement slider, int percent) {
		checkArgument(percent > -101 && percent < 101, "percent must be between -100 and 100");
		double widthPercent = slider.getSize().getWidth() / 100.0;
		return new Double(widthPercent * percent).intValue();
	}
}
