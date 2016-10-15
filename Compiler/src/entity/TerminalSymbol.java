package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 终结符
 * @author rui
 *
 */
public class TerminalSymbol {
	private String value; 
	private List<String> first; // 终结符的first集合
	
	public TerminalSymbol(String value)
	{
		this.value = value;
		this.first = new ArrayList<String>();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<String> getFirst() {
		return first;
	}

	public void setFirst(List<String> first) {
		this.first = first;
	}
	
	
}
