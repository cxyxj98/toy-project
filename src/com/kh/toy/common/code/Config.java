package com.kh.toy.common.code;

public enum Config {
	
	//DOMAIN("https://pclass.ga"),
	
	DOMAIN("https://localhost:9090"),
	COMPANY_EMAIL("cxyxj98@gmail.com"),
	SMTP_AUTHENTICATION_ID("cxyxj98@gmail.com"),
	SMTP_AUTHENTICATION_PASSWORD("ehdwls1219!"),
	
	UPLOAD_PATH("C:\\CODE\\upload\\");
	
	public final String DESC;
	
	Config(String desc){
		this.DESC = desc;
	}
}
