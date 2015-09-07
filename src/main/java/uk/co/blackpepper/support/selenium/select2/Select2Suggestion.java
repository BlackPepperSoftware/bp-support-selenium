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

import org.openqa.selenium.WebElement;

import static com.google.common.base.Preconditions.checkNotNull;

public class Select2Suggestion {
	
	private final WebElement element;
	
	public Select2Suggestion(WebElement element) {
		this.element = checkNotNull(element, "element");
	}
	
	public String getText() {
		String text = element.getText();
		
		if (isNew() && text.startsWith("New ")) {
			text = text.substring(4);
		}
		
		return text;
	}
	
	public WebElement getWebElement() {
		return element;
	}
	
	public boolean isNew() {
		return element.getAttribute("class").contains("x-select2-new");
	}
	
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Select2Suggestion)) {
			return false;
		}
		
		Select2Suggestion suggestion = (Select2Suggestion) object;
		
		// No point checking isNew, since it's derived from webElement
		return element == suggestion.getWebElement();
	}
	
	@Override
	public int hashCode() {
		return element.hashCode();
	}
}
