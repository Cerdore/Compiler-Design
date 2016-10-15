package Lexical;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Lexial {
	private String code = ""; 	// 程序内容
	private int line = 1; 			// 代码在第几行
	private List<String> symbol = new ArrayList<String>(); // 符号表
	private List<tuple> table = new ArrayList<tuple>();
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
			System.out.println(" "+ lineNum + "\t\t" + tokenName + "\t\t\t" + tokenType);
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
		return ;
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
		return ;
	}
	
	/**
	 * 去除代码中的注释 / *类型
	 */
	public void delComment1(){
		int state = 0;
		int startIndex = 0, endIndex = 0;
		char ch;
		for(int i = 0; i < code.length(); i++)
		{
			ch = code.charAt(i);
			if(state == 0){
				if(ch == '/'){
					state = 1;
					startIndex = i;
				}
				continue;
			}else if(state == 1){
				if(ch == '*')
					state = 2;
				else 
					state = 0;
				continue;
			}else if(state == 2){
				if(ch == '*')
					state = 3;
				else if(i == code.length() - 2){
					System.out.println("错误， 注释不完整 Eooro in line " + line);
					state = 0;
					return ;
				}
			}else if(state == 3){
				if(ch == '/'){
					state = 4;
					endIndex = i;
				}else if(i == code.length() - 1){
					System.out.println("错误， 注释不完整 Eooro in line " + line);
					state = 0;
					return ;
				}	
			}else if(state == 4){
				code = code.substring(0, startIndex) + code.substring(endIndex + 1 , code.length());
				state = 0;
				i = 0;
			}
		}
		return ;
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
		delComment();
		char ch;
		
		for(int i = 0; i < code.length(); i++)
		{
			int state = 0;
			String type = "";
			String word = "";
			ch = code.charAt(i);
			if(ch == ' ')
				continue;
			if(Character.isAlphabetic(ch) || ch == '_') // _letter(letter | digit)
			{
				while(Character.isAlphabetic(ch) || Character.isDigit(ch) ||
						ch == '_')
				{
					word += ch;
					ch = code.charAt(++i);
				}
				i--;
				for(int j = 0; j < word.length(); j++)
				{
					if(!normal(word.charAt(j)))
					{
						type = "#-6"; // 错误代码，标识符中有非法字符
						break;
					}else
						type = "ID"; 
				}
				
				for(int j = 0; j < key.length; j++)
				{
					//System.out.println(word + " "+ key[j]);
					if(word.equals(key[j]))
					{
						//System.out.println("**** " + word + " " + key[j] + " ****");
						type = key[j].toUpperCase();
						break;
					}
				}
				
				if(type == "ID")
				{
					symbol.add(word);
					//table.add(new tuple(line, word, type));
				}
			}else if(ch == '\"'){ // 字符串
				while(Character.isAlphabetic(ch) || strCheck(ch))
				{
					word += ch;
					if(state == 0)
					{
						if(ch == '\"')
							state = 1;
						
					}else if(state == 1){
						if(ch == '\"')
							state = 2;
					}
					ch = code.charAt(++i);
				}
				 
				if(state == 1)
				{
					type = "#-2"; // 错误代码，字符串格式错误
					state = 0;
				}else if(state == 2){
					state = 0;
					type = "STRING";
				}
				
				i--;
			}else if(Character.isDigit(ch)) {
				word += ch;
				while(Character.isDigit(ch) || ch == '.' || Character.isAlphabetic(ch))
				{
					if(state == 0)
						if(ch == '0')
							state = 1;
						else
							state = 2;
					else if(state == 1)
						if(ch == '.')
							state = 3;
						else
							state = 5;
					else if(state == 2)
						if(ch == '.')
							state = 3;
					
					ch = code.charAt(++i);
				}
				for(int j = 0; j < word.length(); j++)
					if(Character.isAlphabetic(word.charAt(j))){
						type = "#-7"; // 错误代码，数字和字母混合
						state = 0;
					}
				if(type != "#-7")
					if(state == 5)
						type = "#-7"; // 错误代码，数字和字母混合
					else{
						state = 0;
						int count = 0;
						for(int j = 0; j < word.length(); j++)
							if(word.charAt(j) == '.')
								count++;
						if(count == 0)
							type = "DIGIT";
						else if(count == 1)
							type = "FRACTION";
						else if(count > 1)
							type = "#-5"; // 错误代码，浮点数格式错误
					}
				i--;
			}else if(ch == '\'') { 		// 字符 
				while(Character.isAlphabetic(ch) || strCheck(ch))
				{
					word += ch;
					if(state == 0){
						if(ch == '\'')
							state = 1;
					}else if(state == 1){
						if(ch == '\\')
							state = 2;
						else if(Character.isAlphabetic(ch) || !normal(ch))
							state = 3;
					}else if(state == 2){
						if(ch == 'n' || ch == 't')
							state = 3;
					}else if(state == 3){
						if(state == '\'')
							state = 4;
					}
					ch = code.charAt(++i);
				}
				i--;
				if(state == 4)
					type = "CHARACER";
				else
					type = "#-4";  // 错误代码，字符格式错误
			}else if(ch == '<') {
				word += ch;
				ch = code.charAt(++i);
				
				if(ch == '='){
					word += ch;
					type = "<="; 	// '<='
				}else {
					type = "<";
					i--;
				}
			}else if(ch == '>'){
				word += ch;
				ch = code.charAt(++i);
				if(ch == '='){
					word += ch;
					type = ">=";
				}else{
					type = ">";
					i--;
				}
				//System.out.println(word + " " + type);
			}else if(ch == '!'){
				word += ch;
				ch = code.charAt(++i);
				
				if(ch == '='){
					word += ch;
					type = "!=";
				}else {
					type = "!";
					i--;
				}
			}else if(ch == '+'){
				word += ch;
				ch = code.charAt(++i);
				
				if(ch == '+'){
					word += ch;
					type = "++";
				}else{
					type = "+";
					i--;
				}
			}else if(ch == '-'){
				word += ch;
				ch = code.charAt(++i);
				if(ch == '-'){
					word += ch;
					type = "--";
				}else{
					type = "-";
					i--;
				}
			}else if(ch == '='){
				word += ch;
				ch = code.charAt(++i);
				if(ch == '='){
					word += ch;
					type = "==";
				}else{
					type = "=";
					i--;
				}
			}else if(ch == '&'){
				word += ch;
				ch = code.charAt(++i);
				if(ch == '&'){
					word += ch;
					type = "&&";
				}else {
					type = "&";
					i--;
				}
			}else if(ch == '|'){
				word += ch;
				ch = code.charAt(++i);
				if(ch == '|'){
					word += ch;
					type = "||";
				}else {
					type = "|";
					i--;
				}
			}else if(ch == '*'){
				word += ch;
				type = "*";
			}else if(ch == '/'){
				word += ch;
				type = "/";
			}else if(ch == ';'){
				word += ch;
				type = ";";
			}else if(ch == '('){
				word += ch;
				type = "(";
			}else if(ch == ')'){
				word += ch;
				type = ")";
			}else if(ch == '{'){
				word += ch;
				type = "{";
			}else if(ch == '}'){
				word += ch;
				type = "}";
			}else if(ch == ']'){
				word += ch;
				type = "]";
			}else if(ch == ','){
				word += ch;
				type = ",";
			}else if(ch == '\n'){
				type = "#-1";
			}// else if
			//System.out.println(word + " " + type);
			if(type == "#-1")
				line++;
			else if(type == "#-2")
				System.out.println("字符串 " + word + " 格式错误！ Error in line " + line);
			else if(type == "#-3")
				System.out.println("数字 " + word + " 格式错误！ Error in line " + line);
			else if(type == "#-4")
				System.out.println("字符 " + word + " 格式错误！ Error in line " + line);
			else if(type == "#-5")
				System.out.println("数字 " + word + " 不合法！ Error in line " + line);
			else if(type == "#-6")
				System.out.println("标识符 " + word + " 含有非法字符！ Error in line " + line);
			else if(type == "#-7")
				System.out.println("数字 " + word + " 包含字母！ Error in line " + line);
			else if(word != "" || type != "")
				table.add(new tuple(line, word, type));
		}// for
		return;
	}// analysis
	
	
	public boolean strCheck(char ch)
	{
		String str = "@#$%&*\\\'\" ";
		boolean flag = false;
		for(int i = 0; i < str.length(); i++)
		{
			if(ch == str.charAt(i))
				flag = true;
		}
		return flag;
	}
	
	public void printTable()
	{
		Iterator it = table.iterator();
		while(it.hasNext())
		{
			tuple tp = (tuple)it.next();
			tp.print();
		}
	}

	public static void main(String[] args) throws IOException {
		Lexial lx = new Lexial();
		String code = lx.scan("test.txt");
		lx.analysis();
		lx.printTable();
	}

}
