package falcun.net.z.optimizations.explosions.raycasting;

public class Ray {
	public final double x;
	public final double y;
	public final double z;

	public Ray(double x, double y, double z) {
		this.x = x * (double)0.3f;
		this.y = y * (double)0.3f;
		this.z = z * (double)0.3f;
	}
}
