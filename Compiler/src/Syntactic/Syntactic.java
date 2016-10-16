package Syntactic;

import java.util.ArrayList;
import java.util.List;

import entity.Grammar;
import entity.NonTerminalSymbol;
import entity.TerminalSymbol;
import entity.Token;
import util.MyUtil;

public class Syntactic {
	
	public static List<Token> tokenList = new ArrayList<Token>();
	
	/**
	 *  获取文法中的非终结符， 保存到nonTerminalSymbolMap
	 */
	private void splitNonTerminalSymbol() {
		for(int i = 0; i < MyUtil.grammarList.size(); i++)
		{
			String singleGrammar = MyUtil.grammarList.get(i).getmGrammar();
			String[] part = singleGrammar.split("->");
			if(part.length == 2)
			{
				MyUtil.grammarList.get(i).setLeftPart(part[0]);
				String[] right = part[1].split(" ");
				MyUtil.grammarList.get(i).setRightPart(MyUtil.toList(right));
				if(!MyUtil.nonTerminalSymbolMap.containsKey(part[0]))
					MyUtil.nonTerminalSymbolMap.put(part[0], new NonTerminalSymbol(part[0]));
			}
		}
		
		System.out.println("非终结符： ");
		for(String string : MyUtil.nonTerminalSymbolMap.keySet())
			System.out.println(string + "\t");
		System.out.println();
	}
	
	/**
	 *  分离文法中的终结符， 保存到terminalSymbolMap
	 */
	private void splitTerminalSymbol()
	{
		for(int i = 0; i < MyUtil.grammarList.size(); i++)
		{
			List<String> rightPart = new ArrayList<String>();
			MyUtil.combineList(rightPart, MyUtil.grammarList.get(i).getRightPart());
			for(String keyString : MyUtil.nonTerminalSymbolMap.keySet())
			{
				if(rightPart.contains(keyString))
					rightPart.remove(keyString);
			}
			for(int k = 0; k < rightPart.size(); k++)
			{
				String mterminal = rightPart.get(k);
				if(!MyUtil.terminalSymbolMap.containsKey(mterminal))
					MyUtil.terminalSymbolMap.put(mterminal, new TerminalSymbol(mterminal));
			}
		}
		System.out.println("终结符： ");
		for(String string : MyUtil.terminalSymbolMap.keySet()) {
			System.out.println(string + "\t");
		}
		System.out.println();
	}
	
	
	
	/**
	 * 将文件中的文法分解为多个子文法
	 */
	private void getGrammarList(List<String> list)
	{
		for(int i = 0; i < list.size(); i++)
		{
			String replaceString = list.get(i).replace('|', '@');
			
			if(replaceString.indexOf("@") != -1) {
				String[] arrStrings = replaceString.split("@");
				MyUtil.grammarList.add(new Grammar(arrStrings[0]));
				String[] expLeft = arrStrings[0].split("->");
				
				for(int j = 1; j < arrStrings.length; j++) {
					MyUtil.grammarList.add(new Grammar(expLeft[0] + "->" + arrStrings[j]));
				}
			} else {
				MyUtil.grammarList.add(new Grammar(replaceString));
			}
		}
		System.out.println("文法表达式：");
		for(int k = 0; k < MyUtil.grammarList.size(); k++)
		{
			System.out.println(MyUtil.grammarList.get(k).getmGrammar());
		}
		System.out.println();
	}
	
}
