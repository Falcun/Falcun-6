package net.mattbenson.gui.framework;

public interface MenuComponentImpl {
	default void onPreSort() {}
	default void onRender() {}
	default boolean onExitGui(int key) { return false; }
	default void onKeyDown(char character, int key) {}
	default void onMouseClick(int key) {}
	default void onMouseScroll(int scroll) {}
	default void onMouseClickMove(int key) {}
	default void onInitColors() {}
}
