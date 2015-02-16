package com.zl.entities;

public class WebContentEntity {
	final private String str;
	final private int depth;
	
	public WebContentEntity(String str, int depth) {
		this.str = str;
		this.depth = depth;
	}
	
	public String getStr() {
		return str;
	}

	public int getDepth() {
		return depth;
	}
}
