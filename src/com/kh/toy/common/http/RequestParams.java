package com.kh.toy.common.http;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestParams {

	private Map<String,String> params = new HashMap<String,String>();
	
	private RequestParams(RequestParamsBuilder builder) {
		this.params = builder.params;
	}
	
	public static RequestParamsBuilder builder() {
		return new RequestParamsBuilder();
	}
	public static class RequestParamsBuilder {
		//순서보장하기위해 LinkedHashMap으로 만듬
		private Map<String,String> params = new LinkedHashMap<String, String>();
		
		public RequestParamsBuilder param(String name, String value) {
			params.put(name, value);
			return this;//자기자신 반환
		}
		
		public RequestParams build() {
			return new RequestParams(this); //builder 를 담아서 반환
			
		}
	}
	
	
	public Map<String , String> getParams(){
		return params;
	}
	
}

