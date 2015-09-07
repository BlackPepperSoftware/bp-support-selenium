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

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import static com.google.common.base.Preconditions.checkNotNull;

public class CompositeSearchContext implements SearchContext {
	
	private final List<? extends SearchContext> contexts;

	public CompositeSearchContext(List<? extends SearchContext> contexts) {
		this.contexts = checkNotNull(contexts, "contexts");
	}

	@Override
	public List<WebElement> findElements(By by) {
		List<WebElement> results = new ArrayList<>();
		
		for (SearchContext context : contexts) {
			results.addAll(context.findElements(by));
		}
		
		return results;
	}

	@Override
	public WebElement findElement(By by) {
		for (SearchContext context : contexts) {
			try {
				return context.findElement(by);
			}
			catch (NoSuchElementException exception) {
				// Try next element
			}
		}
		
		throw new NoSuchElementException(by.toString());
	}
	
	protected List<? extends SearchContext> getChildren() {
		return contexts;
	}
}
