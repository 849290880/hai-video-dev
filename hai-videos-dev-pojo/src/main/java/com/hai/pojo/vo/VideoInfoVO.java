package com.hai.pojo.vo;

public class VideoInfoVO<T> {
	
	/**
	 * 是否点赞的结果
	 */
	private boolean isClick;
	
	/**
	 * 是否关注别人
	 */
	private boolean isFollow;

	
	private T data;
	
	public boolean isClick() {
		return isClick;
	}

	public void setClick(boolean isClick) {
		this.isClick = isClick;
	}
	
	public VideoInfoVO<T> addIsClickResult(boolean result) {
		this.isClick=result;
		return this;
	}

	public boolean isFollow() {
		return isFollow;
	}

	public void setFollow(boolean isFollow) {
		this.isFollow = isFollow;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
}
