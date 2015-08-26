package org.ansj.demo;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.FilterModifWord;

public class UserDefineLibraryNatureDemo {
	public static void main(String[] args) {
		//增加词汇
	/*	UserDefineLibrary.insertWord("泰迪犬", "名称", 1000);
		UserDefineLibrary.insertWord("比熊犬", "名称", 1000);*/ 
		UserDefineLibrary.insertWord("牌子得疫苗", "问题", 1000);
		
		List<Term> parse = ToAnalysis.parse("泰迪狗狗美容应该注意什么呢？") ;
		
		System.out.println(parse);
		
		//自定义词性优先
		parse = FilterModifWord.modifResult(parse) ;
		
		System.out.println(parse);
	}
}
