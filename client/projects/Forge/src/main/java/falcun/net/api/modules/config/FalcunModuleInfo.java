package falcun.net.api.modules.config;

import falcun.net.modules.ModuleCategory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FalcunModuleInfo {
	String name();

	String version();

	String description();

	String fileName();

	ModuleCategory category();
}
