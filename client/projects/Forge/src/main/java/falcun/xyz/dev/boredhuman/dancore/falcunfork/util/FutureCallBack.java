package falcun.xyz.dev.boredhuman.dancore.falcunfork.util;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;

public class FutureCallBack<T> extends FutureTask<T> {

	Consumer<T> callback;

	public FutureCallBack(Callable<T> callable, Consumer<T> callback) {
		super(callable);
		this.callback = callback;
	}

	@Override
	protected void done() {
		try {
			T result = this.get();
			if (this.callback != null) {
				this.callback.accept(result);
			}
		} catch (Throwable err) {
			err.printStackTrace();
		}
		super.done();
	}
}