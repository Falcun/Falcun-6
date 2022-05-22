package falcun.net.api.fps.config;

import falcun.net.fps.FPSCategory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FalcunFPSInfo {
	String name();

	String version();

	String description();

	String fileName();

	FPSCategory category() default FPSCategory.ALL;

}
