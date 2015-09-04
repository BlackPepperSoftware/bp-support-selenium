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
