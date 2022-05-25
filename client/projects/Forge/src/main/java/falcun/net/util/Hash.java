package falcun.net.util;

public class Hash {
	public Hash() {
	}

	public static long fnv1a(double... d0) {
		long l1 = 2166136261L;
		double[] var3 = d0;
		int var4 = d0.length;

		for(int var5 = 0; var5 < var4; ++var5) {
			double d1 = var3[var5];
			l1 = (l1 ^ Double.doubleToLongBits(d1)) * 16777619L;
		}

		return l1;
	}

	public static long fnv1a(long... l0) {
		long l1 = 2166136261L;
		long[] var3 = l0;
		int var4 = l0.length;

		for(int var5 = 0; var5 < var4; ++var5) {
			long l2 = var3[var5];
			l1 = (l1 ^ l2) * 16777619L;
		}

		return l1;
	}
}