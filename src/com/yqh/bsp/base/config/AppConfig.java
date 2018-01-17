package com.yqh.bsp.base.config;

import java.util.concurrent.Executors;

import net.dreamlu.event.EventPlugin;
import net.dreamlu.event.EventThreadFactory;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.plugin.shiro.ShiroPlugin;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.ViewType;
import com.yqh.bsp.base.plugin.tablebind.AutoTableBindPlugin;
import com.yqh.bsp.base.plugin.tablebind.SimpleNameStyles;
import com.yqh.bsp.base.quartz.QuartzPlugin;
import com.yqh.bsp.base.route.AutoControllerBindRoute;
import com.yqh.bsp.common.init.DataInit;
import com.yqh.bsp.common.interceptor.SignAuthActionInterceptor;

public class AppConfig extends JFinalConfig {
	
	public final static String ENCRYPT_SLAT="Y16Q17H18";
	

	 /**
     * 供Shiro插件使用。
     */
    Routes routes;
	@Override
	public void configConstant(Constants me) {
		PropKit.use("app.properties");
		me.setEncoding(PropKit.get("config.encoding")); 
		me.setDevMode(PropKit.getBoolean("config.devMode", false));
		me.setViewType(ViewType.JSP);
	}

	@Override
	public void configRoute(Routes me) {
		AutoControllerBindRoute  autoRoutes = new AutoControllerBindRoute();
		me.add(autoRoutes);
		this.routes = me;
//		me.add("/users", UsersControllers.class);
	}

	@Override
	public void configPlugin(Plugins me) {
		final String URL =PropKit.use("db.properties").get("mysql.jdbcUrl");
		final String USERNAME = PropKit.use("db.properties").get("mysql.userName");
		final String PASSWORD =PropKit.use("db.properties").get("mysql.password");
		final Integer INITIALSIZE = PropKit.use("db.properties").getInt("db.initialSize");
		final Integer MIDIDLE = PropKit.use("db.properties").getInt("db.minIdle");
		final Integer MAXACTIVEE = PropKit.use("db.properties").getInt("db.maxActive");
		DruidPlugin druidPlugin = new DruidPlugin(URL,USERNAME,PASSWORD);
		druidPlugin.set(INITIALSIZE,MIDIDLE,MAXACTIVEE);
		druidPlugin.setFilters("stat,wall");
		me.add(druidPlugin);
		ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin("MAIN",druidPlugin);
		AutoTableBindPlugin autoTablePlugin = new AutoTableBindPlugin(druidPlugin,SimpleNameStyles.LOWER_UNDERLINE).autoScan(false);
		me.add(activeRecordPlugin);
		me.add(autoTablePlugin);
		
		me.add(new EhCachePlugin());
		
		ShiroPlugin shiroPlugin = new ShiroPlugin(this.routes);
//		shiroPlugin.setLoginUrl("/login.do");       //登陆url：未验证成功跳转
		shiroPlugin.setUnauthorizedUrl("/test.jsp");//授权url：未授权成功自动跳转
		me.add(shiroPlugin);
		
		// 初始化插件
		EventPlugin eventPlugin = new EventPlugin();
		// 开启全局异步
		eventPlugin.async();
		// 设置扫描jar包，默认不扫描
		// plugin.scanJar();
		// 设置监听器默认包，默认全扫描
		eventPlugin.scanPackage("com.yqh.bsp.common.event");
		eventPlugin.threadPool(Executors.newCachedThreadPool(new EventThreadFactory()));
		me.add(eventPlugin);
		
		QuartzPlugin quartzPlugin = new QuartzPlugin();
		me.add(quartzPlugin);
	}

	@Override
	public void configInterceptor(Interceptors me) {
//		me.addGlobalActionInterceptor(new SignAuthActionInterceptor());
//		me.add(new ShiroInterceptor());
	}

	@Override
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("ctx"));

	}
	
	@Override
	public void afterJFinalStart() {
		DataInit init = new DataInit();
		init.userIdInit();
		init.pidKeyInit();
		super.afterJFinalStart();
	}

}
