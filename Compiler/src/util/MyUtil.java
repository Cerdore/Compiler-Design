package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import entity.Grammar;
import entity.NonTerminalSymbol;
import entity.TerminalSymbol;

public class MyUtil {
	public static HashMap<String, TerminalSymbol> terminalSymbolMap = new HashMap<String, TerminalSymbol>();
	public static HashMap<String, NonTerminalSymbol> nonTerminalSymbolMap = new HashMap<String, NonTerminalSymbol>();
	public static HashMap<String, HashMap<String, List<String>>> analysisTable = new HashMap<String, HashMap<String, List<String>>>();
	public static List<Grammar> grammarList = new ArrayList<Grammar>();
	public static String ERROR = "error";
	public static String SYN = "synch";
	public static boolean whetherChanged = true;
	
	/**
	 * 求非终结符的First集
	 */
	public static List<String> FIRST(NonTerminalSymbol nts)
	{
		List<Grammar> expList = getMyExp(nts.getValue());
		List<String> mFirst = new ArrayList<String>();
		List<String> right = null;
		
		mFirst = initFirst(expList, mFirst);
		
		for(int i = 0; i < expList.size(); i++)
		{
			right = expList.get(i).getRightPart();
			if(right.size() > 0){
				if(nonTerminalSymbolMap.containsKey(right.get(0))) {
					combineList(mFirst, FIRST(new NonTerminalSymbol(right.get(0))));
					int j = 0; 
					while(j < right.size() - 1 && isEmptyExp(right.get(i))) {
						combineList(mFirst, FIRST(new NonTerminalSymbol(right.get(j + 1))));
						j++;
					}
				}
			}
		}
		if(mFirst.contains("$"))
			mFirst.remove("$");
		combineList(nts.getFirst(), mFirst);
		return mFirst;
	}
	
	/**
	 * 求一个文法串的FIRST集
	 */
	public static List<String> getNTSStringFirst(List<String> right) 
	{
		List<String> mFirst = new ArrayList<String>();
		
		if(right.size() > 0) {
			String curString = right.get(0);
			if(terminalSymbolMap.get(curString) != null)
				combineList(mFirst, terminalSymbolMap.get(right.get(0)).getFirst());
			else {
				combineList(mFirst, FIRST(new NonTerminalSymbol(curString)));
			}
		}
		
		if(right.size() > 0) {
			if(nonTerminalSymbolMap.get(right.get(0)) != null) {
				if(right.size() == 1) {
					combineList(mFirst, nonTerminalSymbolMap.get(right.get(0)).getFirst());
				} else if (right.size() > 0) {
					int j = 0;
					while(j < right.size() - 1 && isEmptyExp(right.get(j))) {
						String tString = right.get(j + 1);
						if(nonTerminalSymbolMap.get(tString) != null) {
							combineList(mFirst, FIRST(new NonTerminalSymbol(tString)));
						} else {
							combineList(mFirst, terminalSymbolMap.get(tString).getFirst());
						}
						j++;
					}
				}
			}
		}
		return mFirst;
	}
	
	/**
	 *  判断一个文法符号是否能通过N步推到出$
	 */
	public static boolean isEmptyExp(String nts)
	{
		if(nonTerminalSymbolMap.get(nts) != null)
		{
			List<Grammar> expList = getMyExp(nts);
			for(int i = 0; i < expList.size(); i++)
			{
				Grammar mGrammar = expList.get(i);
				if(mGrammar.getmGrammar().equals(nts + "->$"))
					return true;
				else {
					boolean flag = false;
					List<String> right = mGrammar.getRightPart();
					for(int j = 0; j < right.size(); j++) {
						if(!isEmptyExp(right.get(i))) {
							flag = true;
							break;
						}
					}
					if(flag = false) {
						return true;
					}
					flag = false;
				}
			}
			return false;
		}else {
			return false;
		}
	}
	
	/**
	 * 将两个List合并 
	 */
	public static List<String> combineList(List<String> mfirst1,List<String> mfirst2)
	{
		for(int i = 0; i < mfirst2.size(); i++)
		{
			if(!mfirst1.contains(mfirst2.get(i)))
				mfirst1.add(mfirst2.get(i));
		}
		return mfirst1;
	}
	
	/**
	 *  初始化First集合
	 */
	public static List<String> initFirst(List<Grammar> expList, List<String> mFirst)
	{
		List<String> right = null;
		for(int i = 0; i < expList.size(); i++)
		{
			right = expList.get(i).getRightPart();
			if(right.size() > 0)
				if(terminalSymbolMap.containsKey(right.get(0)))
					mFirst.add(right.get(0));
		}
		return mFirst;
	}
	
	
	/**
	 * 获取指定非终结符开头的文法表达式
	 */
	public static List<Grammar> getMyExp(String nts)
	{
		List<Grammar> myExp = new ArrayList<Grammar>();
		for(int i = 0; i < grammarList.size(); i++)
		{
			Grammar mGrammar = grammarList.get(i);
			if(nts.equals(mGrammar.getLeftPart()))
				myExp.add(mGrammar);
		}
		return myExp;
	}
	/**
	 *  求一个文法的SELECT集
	 */
	public static List<String> SELECT(Grammar g)
	{
		List<String> mselect = new ArrayList<String>();
		if (g.getRightPart().size() > 0) {
			if(g.getRightPart().get(0).equals("$")) {
				mselect = nonTerminalSymbolMap.get(g.getLeftPart()).getFollow();
				return mselect;
			} else {
				mselect = getNTSStringFirst(g.getRightPart());
				boolean isNull = true;
				for (int i = 0; i < g.getRightPart().size(); i++) {
					if(!isEmptyExp(g.getRightPart().get(i))) {
						isNull = false;
						break;
					}
				}
				if(isNull) {
					combineList(mselect, nonTerminalSymbolMap.get(g.getLeftPart()).getFollow());
				}
				return mselect;
			}
		}
		return null;
	}
	
	/**
	 *  将两个List合并
	 */
	public static List<NonTerminalSymbol> combine(List<NonTerminalSymbol> list1, List<NonTerminalSymbol> list2 )
	{
		for(int i = 0; i < list2.size(); i++)
		{
			if(!list1.contains(list2.get(i)))
				list1.add(list2.get(i));
		}
		return list1;
	}
	
	/**
	 * 构造预测分析表
	 */
	public static void getAnalysisTable() {
		List<String> synString = new ArrayList<String>();
		synString.add(SYN);
		
		for(int i = 0; i < grammarList.size(); i++)
		{
			Grammar grammar = grammarList.get(i);
			if(analysisTable.get(grammar.getLeftPart()) != null) {
				for(String sel : grammar.getSelect()) {
					analysisTable.get(grammar.getLeftPart()).put(sel, grammar.getRightPart());
				}
 			} else {
 				HashMap<String, List<String>> inMap = new HashMap<String, List<String>>();
 				for(String sel : grammar.getSelect())
 					inMap.put(sel, grammar.getSelect());
 				analysisTable.put(grammar.getLeftPart(), inMap);
 			}
			NonTerminalSymbol nts = nonTerminalSymbolMap.get(grammar.getLeftPart());
			List<String> msynList = nts.getTypeList();
			for (int j = 0; j < msynList.size(); j++)
				analysisTable.get(grammar.getLeftPart()).put(msynList.get(j), synString);
		}
	}
	
	
	/**
	 * LL(1) 语法分析控制程序， 输出分析数
	 */
	public static void mParser(List<String> sentence)
	{
		Stack<String> stack = new Stack<String>();
		int index = 0;
		String curCharacter = null;
		stack.push("#");
		stack.push("program");
		
		while(!stack.peek().equals("#")) {
			if(index < sentence.size())
				curCharacter = sentence.get(index);
			if(terminalSymbolMap.containsKey(stack.peek()) || stack.peek().equals("#"))
			{
				if(stack.peek().equals(curCharacter)) {
					if(!stack.peek().equals("#")) {
						stack.pop();
						index++;
					}
				}else {
					System.out.println("当前栈顶符号" + stack.pop() + "与" + curCharacter + "不匹配");
				}
			} else {
				List<String> exp = analysisTable.get(stack.peek()).get(curCharacter);
				if(exp != null) {
					if(exp.get(0).equals(SYN)) {
						System.out.println("遇到SYNCH， 从栈顶弹出非终结符" + stack.pop());
					} else {
						System.out.println(stack.peek() + "->" + ListToString(exp));
						stack.pop();
						
						for(int j = exp.size() - 1; j > -1; j--) {
							if(!exp.get(j).equals("$") && !exp.get(j).equals(SYN))
								stack.push(exp.get(j));
						}
					}
				} else {
					if(index < sentence.size() - 1){
						System.out.println("忽略" + curCharacter);
						index++;
					} else {
						System.out.println("语法错误， 缺少'}' ");
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 判断一个文法是否为LL1文法
	 */
	public static boolean isLL1()
	{
		for(String keyString : nonTerminalSymbolMap.keySet()) {
			List<Grammar> glist = getMyExp(keyString);
			if(glist.size() > 1) {
				for(int i = 0; i < glist.size(); i++) {
					for(int j = 1; j < glist.size(); j++) {
						if(i != 1) {
							if(glist.get(i).getSelect().equals(glist.get(j).getSelect())) {
								System.out.println(keyString + "为左部的select集相交 ");
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * 计算同步记号集
	 */
	public static void SYNCH(NonTerminalSymbol nts) 
	{
		combineList(nts.getTypeList(), nts.getFollow());
		combineList(nts.getTypeList(), nts.getFirst());
		List<NonTerminalSymbol> higher = nts.getHigherNTS();
		for (int j = 0; j < higher.size(); j++) 
			combineList(nts.getTypeList(), higher.get(j).getFirst());
		List<String> msynList = nts.getTypeList();
		
		for(int i = 0; i < grammarList.size(); i++)
		{
			Grammar grammar = grammarList.get(i);
			if(grammar.getLeftPart().equals(nts.getValue())) {
				List<String> mselect = grammar.getSelect();
				for(int j = 0; j < mselect.size(); j++)
				{
					if(msynList.contains(mselect.get(j)))
						msynList.remove(mselect.get(j));
				}
			}
		}
	}
	
	
	/**
	 * 将数组转换成ArrayList<String>
	 */
	public static List<String> toList(String[] str)
	{
		List<String> myList = new ArrayList<String>();
		for(String s : str)
			myList.add(s);
		return myList;
	}
	
	/**
	 *  将List合并成为字符串
	 */
	public static String ListToString(List<String> strings) {
		String mystr = "";
		for(int i = 0; i < strings.size(); i++)
			mystr += strings.get(i) + " ";
		return mystr;
	}
	
	
	/**
	 *  判断两个终结符那个是跟高层的结构
	 * 
	 */
	public static boolean isHigher(NonTerminalSymbol nts1, NonTerminalSymbol nts2)
	{
		List<Grammar> mexp = getMyExp(nts1.getValue());
		for (int i = 0; i < mexp.size(); i++) {
			List<String> mright = mexp.get(i).getRightPart();
			for(int j = 0; j < mright.size(); j++)
				if(mright.get(j).equals(nts2.getValue()))
					return true;
		}
		return false;
	}
	
	/**
	 *  计算非终结符的高层结构集
	 */
	public static void calHigherGroup()
	{
		for(String s : nonTerminalSymbolMap.keySet()) {
			NonTerminalSymbol sNonTerminalSymbol = nonTerminalSymbolMap.get(s);
			for(String r : nonTerminalSymbolMap.keySet()) {
				NonTerminalSymbol rNonTerminalSymbol = nonTerminalSymbolMap.get(r);
				if(isHigher(sNonTerminalSymbol, rNonTerminalSymbol)){
					if (!rNonTerminalSymbol.getHigherNTS().contains(sNonTerminalSymbol)) {
						rNonTerminalSymbol.getHigherNTS().add(sNonTerminalSymbol);
						combine(rNonTerminalSymbol.getHigherNTS(), sNonTerminalSymbol.getHigherNTS());
						whetherChanged = true;
					}
				}
			}
		}
	}
	
	
	
	/**
	 * 求终结符的FOLLOW集
	 */
	public static void FOLLOW()
	{
		for(int i = 0; i < grammarList.size(); i++)
		{
			String left = grammarList.get(i).getLeftPart();
			List<String> right = grammarList.get(i).getRightPart();
			
			for(int j = 0; j < right.size(); j++)
			{
				String cur = right.get(j);
				if(terminalSymbolMap.containsKey(cur))
					continue;
				
				if(j < right.size() - 1)
				{
					List<String> nList = new ArrayList<String>();
					for(int m = j + 1; m < right.size(); m++)
						nList.add(right.get(m));
					List<String> mfirst = getNTSStringFirst(nList);
					for(String str : mfirst) {
						if(!nonTerminalSymbolMap.get(cur).getFollow().contains(str)) {
							combineList(nonTerminalSymbolMap.get(cur).getFollow(), mfirst);
							whetherChanged = true;
							break;
						}
					}
					
					boolean isNull = true;
					for(int k = 0; k < nList.size(); k++) {
						if(!isEmptyExp(nList.get(k))) {
							isNull = false;
							break;
						}
					}
					
					if(isNull == true) {
						for (String str : nonTerminalSymbolMap.get(left).getFollow()) {
							if(!nonTerminalSymbolMap.get(cur).getFollow().contains(str)) {
								combineList(nonTerminalSymbolMap.get(cur).getFollow(), nonTerminalSymbolMap.get(left).getFollow());
								whetherChanged = true;
								break;
							}
						}
					}
				}else if(j == right.size() - 1) {
					List<String> mfollow = nonTerminalSymbolMap.get(left).getFollow();
					for(String str : mfollow) {
						if(!nonTerminalSymbolMap.get(cur).getFollow().contains(str)) {
							combineList(nonTerminalSymbolMap.get(cur).getFollow(), mfollow);
							whetherChanged = true;
							break;
						}
					}
				} 
			}
		}
	}
}
