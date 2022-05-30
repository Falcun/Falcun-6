package falcun.net.api.primitive.doubles;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.Pair;

public class DoublePair extends Pair<Double, Double> {
	public DoublePair(Double first, Double second) {
		super(first, second);
	}
	public DoublePair(int first, Double second) {
		super((double) first, second);
	}

	public DoublePair(Double first, int second) {
		super(first, (double) second);
	}
	public DoublePair(double first, double second) {
		super(first, second);
	}

	public DoublePair(int first, double second) {
		super((double) first, second);
	}

	public DoublePair(int first, int second) {
		super((double) first, (double) second);
	}

	public DoublePair(Double first, double second) {
		super(first, second);
	}

	public DoublePair(double first, Double second) {
		super(first, second);
	}

}
