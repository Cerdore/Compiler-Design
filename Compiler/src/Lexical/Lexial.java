package Lexical;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Lexial {
	private String code = ""; 	// 程序内容
	private int line = 1; 			// 代码在第几行
	private int p = 0;
	private List<String> symbol = new ArrayList<String>(); // 符号表
	private static final String abnormal = "@#$%^&*~";
	private static final String key[] = {"auto", "break", "case", "char", 
		"const", "continue", "default", "do", "double", "else", "enum",
		"extern", "float", "for", "goto", "if", "int", "long", "register",
		"return", "short", "signed", "static", "sizeof", "struct", "switch",
		"typeof", "union", "unsigned", "void", "volatile", "while"};
	
	private class tuple{
		private int lineNum;
		private String tokenName;
		private String tokenType;
		tuple(int LN, String TN, String TT)
		{
			this.lineNum = LN;
			this.tokenName = TN;
			this.tokenType = TT;
		}
		public void print()
		{
			System.out.println("< "+ lineNum + "," + tokenName + "," + tokenType + " >");
		}
	}
	public boolean normal(char ch)
	{
		for(int i = 0; i < abnormal.length(); i++)
		{
			if (ch == abnormal.charAt(i))
				return false;
		}
		return true;
	}
	
	public void delComment()
	{
		delComment1();
		delComment2();
	}
	/**
	 * 去除代码中的注释 // 类型
	 */
	public void delComment2(){
		int state = 0;
		int sI = 0, eI = 0;
		for(int i = 0; i < code.length(); i++)
		{
			if(state == 0)
			{
				if(code.charAt(i) == '/')
				{
					state = 1;
					sI = i;
				}
			}else if(state == 1){
				if(code.charAt(i) == '/')
					state = 2;
				else 
					state = 0;
			}else if(state == 2){
				if(code.charAt(i) == '\n' || i == code.length() - 2)
				{
					eI = i + 1;
					state = 3;
				}
			}else if(state == 3){
				code = code.substring(0, sI) + '\n' + code.substring(eI, code.length());
				i = 0;
				state = 0;
			}
		}
	}
	
	/**
	 * 去除代码中的注释 / *类型
	 */
	public void delComment1(){
		int state = 0;
		int startIndex = 0, endIndex = 0;
		
		for(int i = 0; i < code.length(); i++)
		{
			if(state == 0)
			{
				if(code.charAt(i) == '/')
				{
					state = 1;
					startIndex = i;
				}
			}else if(state == 1){
				if(code.charAt(i) == '*')
				{
					state = 2;
				}else{
					state = 0;
					startIndex = 0;
				}
			}else if(state == 2){
				if(code.charAt(i) == '*')
					state = 3;
				else if(i == code.length() - 2){
					System.out.println("注释错误，缺少结束出的*/");
					state = 0;
					return;
				}
			}else if(state == 3){
				if(code.charAt(i) == '/'){
					state = 4; // 4 是结束状态
					endIndex = i;
				}else if(i == code.length() - 2){
					System.out.println("注释错误，缺少结束出的*/");
					state = 0;
					return;
				}else if(code.charAt(i) == '*')
					;
				else{
					state = 2;
				}
			}else if(state == 4){
				code = code.substring(0, startIndex) + code.substring(endIndex + 1, code.length());
				state = 0;
			}
			i = 0;
		}
	}
	
	/**
	 * 读取文件中的代码
	 * @return
	 * @throws IOException
	 */
	public String scan(String fileName) throws IOException{
		File f = new File(fileName);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String str = "", tem;
		while((tem = br.readLine()) != null && tem != ""){
			str += tem;
			str += '\n';
		}
		this.code = str;
		return str;
		
	}
	
	/**
	 * 词法分析
	 */
	public void analysis()
	{
		int state = 0;
		String type = "";
		String name = "";
		char ch;
		for(int i = 0; i < code.length(); i++)
		{
			ch = code.charAt(i);
			if(ch == ' ')
				continue;
			if(Character.isAlphabetic(ch) || ch == '_') // _letter(letter | digit)
			{
				
			}
		}
	}
	
	
	public boolean strCheck(char ch)
	{
		String str = "\"\\% ";
		boolean flag = false;
		for(int i = 0; i < str.length(); i++)
		{
			if(ch == str.charAt(i))
				flag = true;
		}
		return flag;
	}
	

	public static void main(String[] args) throws IOException {
//		Lexial lx = new Lexial();
//		String code = lx.scan("test.txt");
//		lx.delComment();
//		System.out.println(lx.code);
		char ch = 'a';
		System.out.println(Character.isAlphabetic(ch));
	}

}
