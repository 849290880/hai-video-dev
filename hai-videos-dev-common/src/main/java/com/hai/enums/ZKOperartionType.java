package com.hai.enums;

public enum ZKOperartionType {
	
	ADD("1","添加bgm"),
	DEL("2","删除bgm");
	
	public final String value;
	public final String type;

	ZKOperartionType(String type,String value) {
		this.type = type;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public String getType() {
		return type;
	}
	
	public static String getValueByKey(String key) {
		for (ZKOperartionType type : ZKOperartionType.values()) {
			if(type.getType().equals(key)) {
				return type.getValue();
			}
		}
		return null;
	}
	
}
