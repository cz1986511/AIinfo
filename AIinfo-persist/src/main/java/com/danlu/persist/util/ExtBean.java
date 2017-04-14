package com.danlu.persist.util;

import java.util.List;

/**
 * Ext返回结果
 * 
 * @author
 * @param <T>
 */
public class ExtBean<T> {

	// private T t;//
	private List<T> resultBean;
	private Integer count;// 总行数

	public ExtBean() {
	}

	public ExtBean(List<T> resultBean, Integer count) {
		this.resultBean = resultBean;
		this.count = count;
	}

	public List<T> getResultBean() {
		return resultBean;
	}

	public void setResultBean(List<T> resultBean) {
		this.resultBean = resultBean;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "ExtBean [resultBean=" + resultBean + ", count=" + count + "]";
	}

}
