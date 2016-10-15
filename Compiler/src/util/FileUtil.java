package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
	public static List<String> myGrammarStringList = new ArrayList<String>();
	public static List<String> myTokenList = new ArrayList<String>();
	
	/*
	 * 从文中读取文法
	 */
	public static List<String> getGrammarFromFile()
	{
		try {
			BufferedReader br = new BufferedReader(new FileReader(".\\myGrammar1.txt"));
			String mGrammar;
			mGrammar = br.readLine();
			 
			while(mGrammar != null)
			{
				if(mGrammar.equals("\n"))
					continue;
				myGrammarStringList.add(mGrammar);
				mGrammar = br.readLine();
			}
			
			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myGrammarStringList;
	}
	
	/**
	 * 从文中读取token
	 */
	public static List<String> getTokenFromFile()
	{
		try {
			BufferedReader br = new BufferedReader(new FileReader(".\\token.txt"));
			
			String mToken = br.readLine();
			
			while(mToken != null)
			{
				myTokenList.add(mToken);
				mToken = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return myTokenList;
	}
	
	public static void main(String[] args) {
		List<String> t = FileUtil.getTokenFromFile();
		String s = null;
		for(int i = 0; i < t.size(); i++)
			System.out.println(t.get(i));
	}
}
