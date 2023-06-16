package com.landsem.setting.entity;

public final class EcarTel {

	private String lable;
	public String tel;

	public EcarTel(String lable, String tel) {
		super();
		this.lable = lable;
		this.tel = tel;
	}

	@Override
	public String toString() {

		return lable;
	}

}
