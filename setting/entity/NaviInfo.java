package com.landsem.setting.entity;

import java.io.Serializable;


public class NaviInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String appLable;
	public String packageName;
	public String className;
	public boolean select;
	
	
	
	
	public NaviInfo(String appLable) {
		super();
		this.appLable = appLable;
	}

	public NaviInfo(String packageName, String className) {
		super();
		this.packageName = packageName;
		this.className = className;
	}

	public NaviInfo(String appLable, String packageName, String className) {
		super();
		this.appLable = appLable;
		this.packageName = packageName;
		this.className = className;
	}

	@Override
	public String toString() {
		return  appLable;
	}
	
}
