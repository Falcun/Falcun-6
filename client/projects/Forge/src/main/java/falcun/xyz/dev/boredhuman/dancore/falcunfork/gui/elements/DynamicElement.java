package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements;

import java.util.function.Consumer;

public class DynamicElement<T extends DynamicElement<T>> extends BasicElement<T> {

	Consumer<BasicElement<?>> renderFunction;

	public DynamicElement(Consumer<BasicElement<?>> renderFunction) {
		this.renderFunction = renderFunction;
	}

	@Override
	public void render() {
		this.renderFunction.accept(this);
	}
}