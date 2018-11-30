package com.hai.util;

import java.util.List;

public class PageResult<T> {
	
	//总页数
	private long pageTotal;
	
	//当前页数
	private int currentPage;
	
	//每页的记录数
	private long recordNumber;
	
	//当前页的数据
	private List<T> Object;


	public long getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(long pageTotal) {
		this.pageTotal = pageTotal;
	}

	public long getRecordNumber() {
		return recordNumber;
	}

	public void setRecordNumber(long recordNumber) {
		this.recordNumber = recordNumber;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}




	public List<T> getObject() {
		return Object;
	}

	public void setObject(List<T> object) {
		Object = object;
	}
}
