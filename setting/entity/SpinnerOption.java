package com.landsem.setting.entity;

import java.io.Serializable;

public class SpinnerOption implements Serializable{

	private static final long serialVersionUID = 4274975266248032898L;
	public String lable;
	public String libName;
	public int duration;
	public boolean select;

	public SpinnerOption() {
		super();
	}

	public SpinnerOption(String lable, int duration) {
		super();
		this.lable = lable;
		this.duration = duration;
	}
	
	public SpinnerOption(String lable, String libName) {
		super();
		this.lable = lable;
		this.libName = libName;
	}

	@Override
	public String toString() {
		return null==lable?"":lable;
	}
	
}
