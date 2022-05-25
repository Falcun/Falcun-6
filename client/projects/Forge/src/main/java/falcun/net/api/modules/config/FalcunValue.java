package falcun.net.api.modules.config;

import java.util.function.Consumer;

public class FalcunValue<T> {
	private T value;
	private final T defaultValue;
	public transient Consumer<T> callBack;

	public FalcunValue(T value) {
		this(value, null);
	}

	public FalcunValue(T value, T defaultValue) {
		this.value = value;
		this.defaultValue = defaultValue;
	}

	public void setValue(T value) {
		this.value = value;
		if (this.callBack != null) {
			this.callBack.accept(value);
		}
	}

	public T getValue() {
		return this.value;
	}

	public void reset() {
		if (this.defaultValue != null) {
			this.value = this.defaultValue;
		}
	}
	public static <T> FalcunValue<T> of(T t) {
		return new FalcunValue<>(t);
	}
}