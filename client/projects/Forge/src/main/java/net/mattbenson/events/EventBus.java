package net.mattbenson.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.mattbenson.Falcun;

public class EventBus {
	public static final boolean DEBUG = false;
	
	private Map<Object, List<Method>> registry;
	
	public EventBus() {
		registry = new LinkedHashMap<>();
	}
	
	public void register(Object clazz) {
		Method[] methods = clazz.getClass().getDeclaredMethods();
		
		List<Method> registered = new ArrayList<>();
		
		for(Method method : methods) {
			if(method.isAnnotationPresent(SubscribeEvent.class) && method.getParameterCount() == 1) {
				if(!method.isAccessible()) {
					method.setAccessible(true);
				}
				
				Class<?>[] parameters = method.getParameterTypes();
				
				if(Event.class.isAssignableFrom(parameters[0])) {
					registered.add(method);
				} else if(DEBUG) {
					debug("Tried subscribing to a event in class " + clazz.getClass().getName() + ", but the paramater " + parameters[0].getName() + " in method " + method.getName() + " isnt a subscribable event.");
				}
			}
		}
		
		if(!registered.isEmpty()) {
			registry.put(clazz, registered);
			
			if(DEBUG) {
				debug("Successfully subscribed to " + registered.size() + " event in class " + clazz.getClass().getName() + ".");
			}
		} else if(DEBUG) {
			debug("Registered class " + clazz.getClass().getName() + ", but contained no methods that subscribes to any events.");
		}
	}
	
	public void unregister(Object clazz) {
		registry.remove(clazz);
	}
	
	public boolean post(Event event) {
		synchronized(registry) {
			Map<Object, List<Method>> snapshot = new LinkedHashMap<>(registry);
			
			for(Object key : snapshot.keySet()) {
				List<Method> value = snapshot.get(key);
				
				for(Method method : value) {
					Class<?>[] parameters = method.getParameterTypes();
					
					if(parameters[0] == event.getClass()) {
						try {
							method.invoke(key, event);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace();
							debug("Failed supplying subscribed event " + event.getClass().getName() + " to " + key.getClass().getName() + "." + method.getName() + ".");
						}
					}
				}
			}
			
			return !event.isCancelled();
		}
	}
	
	private void debug(String string) {
		Falcun.getInstance().log.debug("[EventBus] " + string);
	}

	public boolean isRegistered(Object clazz) {
		return registry.containsKey(clazz);
	}
}
