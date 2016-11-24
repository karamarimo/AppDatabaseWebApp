package utility;
public class HtmlTag {
	public final String closingTag;
	public final String openingTag;
	
	public HtmlTag(String open, String close) {
		openingTag = open;
		closingTag = close;
	}
	
	public String whole() {
		return openingTag + closingTag;
	}

	@Override
	public String toString() {
		return openingTag + closingTag;
	}
	
}
