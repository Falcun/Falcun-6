package net.mattbenson.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.lwjgl.input.Keyboard;

public class ConfigValue {
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Boolean {
		String name();
		String description() default "";
		boolean visible() default true;
		boolean enabled() default true;
		boolean beta() default false;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Integer {
		String name();
		String description() default "";
		boolean visible() default true;
		boolean enabled() default true;
		
		int min();
		int max();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Float {
		String name();
		String description() default "";
		boolean visible() default true;
		boolean enabled() default true;
		
		float min();
		float max();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Double {
		String name();
		String description() default "";
		boolean visible() default true;
		boolean enabled() default true;
		
		double min();
		double max();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Color {
		String name();
		String description() default "";
		boolean visible() default true;
		boolean enabled() default true;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface List {
		String name();
		String description() default "";
		boolean visible() default true;
		boolean enabled() default true;
		
		String[] values();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Keybind {
		String name();
		String description() default "";
		boolean visible() default true;
		boolean enabled() default true;
		
		int keybind() default Keyboard.CHAR_NONE;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Text {
		String name();
		String description() default "";
		boolean visible() default true;
		boolean enabled() default true;
		
		String text() default "";
	}
}