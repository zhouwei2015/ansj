package org.ansj.app.summary.pojo;

import java.util.List;

import org.ansj.app.keyword.Keyword;

/**
 * 摘要结构体封装
 * 
 */
public class Summary {

	/**
	 * 关键词
	 */
	private List<Keyword> keyWords = null;

	/**
	 * 摘要
	 */
	private String summary;

	public Summary(List<Keyword> keyWords, String summary) {
		this.keyWords = keyWords;
		this.summary = summary;
	}

	public List<Keyword> getKeyWords() {
		return keyWords;
	}

	public String getSummary() {
		return summary;
	}

}
