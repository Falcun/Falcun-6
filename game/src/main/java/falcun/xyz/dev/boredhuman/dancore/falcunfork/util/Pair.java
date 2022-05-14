package falcun.xyz.dev.boredhuman.dancore.falcunfork.util;

import java.util.Objects;

public final class Pair<A, B> {
	public A first;
	public B second;

	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}
		Pair<?, ?> pair = (Pair<?, ?>) o;
		return Objects.equals(this.first, pair.first) && Objects.equals(this.second, pair.second);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.first, this.second);
	}

	public static <A,B> Pair<A,B> of(A first, B second) {
		return new Pair<>(first, second);
	}
}