package com.landsem.setting.entity;

import java.io.Serializable;
import java.util.Locale;

public class Language implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int resource;
	public String appLable;
	public Locale locale;

	public Language() {
		super();
	}

	public Language(String appLable, Locale locale) {
		super();
		this.appLable = appLable;
		this.locale = locale;
	}
	
	

	public Language(int resource, String appLable, Locale locale) {
		super();
		this.resource = resource;
		this.appLable = appLable;
		this.locale = locale;
	}

	@Override
	public String toString() {
		return null==appLable?"":appLable;
	}
	
}
