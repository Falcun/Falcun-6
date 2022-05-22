package falcun.net.api.fps.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FalcunFPSSetting {
	String name();

	String description();

	boolean isDefault() default false;

	boolean isHidden() default false;
}
