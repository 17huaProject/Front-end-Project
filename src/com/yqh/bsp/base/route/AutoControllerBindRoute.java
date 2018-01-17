package com.yqh.bsp.base.route;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.jfinal.config.Routes;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.yqh.bsp.base.annotation.Action;
import com.yqh.bsp.base.kit.ClassSearcher;

public class AutoControllerBindRoute extends Routes{
	 private boolean autoScan = true;

	    private List<Class<? extends Controller>> excludeClasses = Lists.newArrayList();

	    private boolean includeAllJarsInLib;

	    private List<String> includeJars = Lists.newArrayList();

	    protected final Logger logger = Logger.getLogger(getClass());

	    private String suffix = "Controller";

	    public AutoControllerBindRoute autoScan(boolean autoScan) {
	        this.autoScan = autoScan;
	        return this;
	    }

	    public AutoControllerBindRoute addExcludeClasses(Class<? extends Controller>... clazzes) {
	        if (clazzes != null) {
	            for (Class<? extends Controller> clazz : clazzes) {
	                excludeClasses.add(clazz);
	            }
	        }
	        return this;
	    }

	    public AutoControllerBindRoute addExcludeClasses(List<Class<? extends Controller>> clazzes) {
	        excludeClasses.addAll(clazzes);
	        return this;
	    }

	    public AutoControllerBindRoute addJars(String... jars) {
	        if (jars != null) {
	            for (String jar : jars) {
	                includeJars.add(jar);
	            }
	        }
	        return this;
	    }

	    @Override
	    @SuppressWarnings({ "rawtypes", "unchecked" })
	    public void config() {
	        List<Class<? extends Controller>> controllerClasses = ClassSearcher.of(Controller.class)
	                .includeAllJarsInLib(includeAllJarsInLib).libDir(null).injars(includeJars).search();
	        Action action = null;
	        for (Class controller : controllerClasses) {
	            if (excludeClasses.contains(controller)) {
	                continue;
	            }
	            action = (Action) controller.getAnnotation(Action.class);
	            if (action == null) {
	                if (!autoScan) {
	                    continue;
	                }
	                this.add(controllerKey(controller), controller);
	                logger.debug("routes.add(" + controllerKey(controller) + ", " + controller.getName() + ")");
	            } else if (StrKit.isBlank(action.viewPath())) {
	                this.add(action.controllerKey(), controller);
	                logger.debug("routes.add(" + action.controllerKey() + ", " + controller.getName() + ")");
	            } else {
	                this.add(action.controllerKey(), controller, action.viewPath());
	                logger.debug("routes.add(" + action.controllerKey() + ", " + controller + ","
	                        + action.viewPath() + ")");
	            }
	        }
	    }

	    private String controllerKey(Class<Controller> clazz) {
	        Preconditions.checkArgument(clazz.getSimpleName().endsWith(suffix),
	                clazz.getName()+" is not annotated with @ControllerBind and not end with " + suffix);
	        String controllerKey = "/" + StrKit.firstCharToLowerCase(clazz.getSimpleName());
	        controllerKey = controllerKey.substring(0, controllerKey.indexOf(suffix));
	        return controllerKey;
	    }

	    public AutoControllerBindRoute includeAllJarsInLib(boolean includeAllJarsInLib) {
	        this.includeAllJarsInLib = includeAllJarsInLib;
	        return this;
	    }

	    public AutoControllerBindRoute suffix(String suffix) {
	        this.suffix = suffix;
	        return this;
	    }

}
