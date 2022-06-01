package net.mattbenson.modules;

public interface IModule {
	default void onEnable() {}
	default void onDisable() {}
}
