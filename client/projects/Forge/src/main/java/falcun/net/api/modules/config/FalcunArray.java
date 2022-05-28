package falcun.net.api.modules.config;

import java.io.Serializable;
import java.util.Objects;

public class FalcunArray<T> implements Serializable {

	private transient final T[] values;
	public T currentValue;


	public FalcunArray(T defaultValue, T... values) {
		this.currentValue = defaultValue;
		this.values = values;
	}


	private int getCurrentIndex() {
		for (int i = 0; i < values.length; i++) {
			if (Objects.equals(currentValue, values[i])) {
				return i;
			}
		}
		return 0;
	}

	public T getFirst() {
		return values[0];
	}

	public T getLast() {
		return values[values.length - 1];
	}

	public T getCurrent() {
		return this.currentValue;
	}

	public T getNext() {
		int i = getCurrentIndex();
		if (i >= values.length - 1) {
			return values[0];
		}
		return values[i];
	}

	public T getPrev() {
		int i = getCurrentIndex();
		if (i <= 0) {
			return values[values.length - 1];
		}
		return values[i];
	}

	public T setAndGetNext() {
		return this.currentValue = getNext();
	}

	public T setAndGetPrev() {
		return this.currentValue = getPrev();
	}

	public T set(T newValue) {
		return this.currentValue = newValue;
	}


}
