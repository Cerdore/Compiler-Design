package entity;

import java.util.ArrayList;
import java.util.List;

public class NonTerminalSymbol {
	private String value; 
	private List<String> first; // 非终结符的first集合
	private List<String> follow;  // follow集合
	private List<String> typeList; 
	private List<NonTerminalSymbol> higherNTS;
	
	public NonTerminalSymbol(String value)
	{
		this.value = value;
		this.first = new ArrayList<String>();
		this.follow = new ArrayList<String>();
		this.typeList = new ArrayList<String>();
		this.higherNTS = new ArrayList<NonTerminalSymbol>();
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

	public List<String> getFollow() {
		return follow;
	}

	public void setFollow(List<String> follow) {
		this.follow = follow;
	}

	public List<String> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<String> typeList) {
		this.typeList = typeList;
	}

	public List<NonTerminalSymbol> getHigherNTS() {
		return higherNTS;
	}

	public void setHigherNTS(List<NonTerminalSymbol> higherNTS) {
		this.higherNTS = higherNTS;
	}
	
	
		
}
