package falcun.net.api.collections;

import java.util.ArrayList;
import java.util.function.Consumer;

public class HookedArrayList<T> extends ArrayList<T> {

	Consumer<T> callback;

	public HookedArrayList(Consumer<T> callback) {
		this.callback = callback;
	}

	@Override
	public boolean add(T t) {
		this.callback.accept(t);
		return true;
	}

	public static <T> HookedArrayList<T> makeHook(Consumer<T> callback) {
		return new HookedArrayList<>(callback);
	}
}
