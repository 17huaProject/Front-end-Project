package com.yqh.bsp.base.config;

import com.jfinal.core.JFinal;

public class runserver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		JFinal.start("WebRoot", 88, "/yqhbsp", 5);
		
//		AppConfig config = new AppConfig();
//		Reflect.on("com.jfinal.core.Config").call("configJFinal",config);			
		
//		AppConfig globalconfig = new AppConfig();
//		Reflect.on("com.jfinal.core.Config").call("configJFinal",globalconfig);
//		

	}

}
