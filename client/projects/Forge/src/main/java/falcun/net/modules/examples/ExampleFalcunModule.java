package falcun.net.modules.examples;

import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.*;
import falcun.net.api.modules.inheritance.FalcunEventBusModule;
import falcun.net.modules.ModuleCategory;


@FalcunModuleInfo(fileName = "ExampleFalcunModule1", name = "Example Falcun Module1", description = "Example Falcun Module", version = "1.0.0", category = ModuleCategory.MISC)
public final class ExampleFalcunModule extends FalcunModule implements FalcunEventBusModule {

	@FalcunSetting("boolean")
	public static FalcunValue<Boolean> booleanSave = new FalcunValue<>(true);

	@FalcunSetting("numbers")
	@FalcunBounds(min = 0, max = 10)
	public FalcunValue<Integer> intSave = new FalcunValue<>(1);

	@FalcunSetting("string")
	public static FalcunValue<String> stringSave = new FalcunValue<>("Hello World");

	@FalcunSetting("enum")
	public static FalcunValue<TestEnum> enumSave = new FalcunValue<>(TestEnum.ONE);

	@FalcunConfigValue
	public int timesPlayed = 0;



	enum TestEnum {
		ONE, TWO, THREE
	}
}
