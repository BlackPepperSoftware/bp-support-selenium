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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SliderTest {
	
	private ExpectedException thrown = ExpectedException.none();
	
	@Rule
	public ExpectedException getThrown() {
		return thrown;
	}

	@Test
	public void getSliderHandlerReturnsHandle() {
		WebElement sliderElement = mock(WebElement.class);
		WebElement handleElement = mock(WebElement.class);
		when(sliderElement.findElement(any(By.class))).thenReturn(handleElement);
		
		WebElement actual = Slider.getSliderHandle(sliderElement);
		
		assertThat(actual, is(handleElement));
	}
	
	@Test
	public void getDragAmountWithPercentReturnsDragAmount() {
		WebElement sliderElement = mock(WebElement.class);
		when(sliderElement.getSize()).thenReturn(new Dimension(100, 0));
		
		int actual = Slider.getDragAmount(sliderElement, 10);
		
		assertThat(actual, is(10));
	}
	
	@Test
	public void getDragAmountWithUnevenWidthReturnsDragAmount() {
		WebElement sliderElement = mock(WebElement.class);
		when(sliderElement.getSize()).thenReturn(new Dimension(999, 0));
		
		int actual = Slider.getDragAmount(sliderElement, 10);
		
		assertThat(actual, is(99));
	}
	
	@Test
	public void getDragAmountWithPercentTooSmallThrowsException() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("percent must be between -100 and 100");
		
		Slider.getDragAmount(mock(WebElement.class), -101);
	}
	
	@Test
	public void getDragAmountWithPercentTooLargeThrowsException() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("percent must be between -100 and 100");
		
		Slider.getDragAmount(mock(WebElement.class), 101);
	}
}
