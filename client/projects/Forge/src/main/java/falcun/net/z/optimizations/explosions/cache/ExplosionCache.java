//package falcun.net.z.optimizations.explosions.cache;
//
//import falcun.net.Falcun;
//import it.unimi.dsi.fastutil.ints.Int2FloatLinkedOpenHashMap;
//import it.unimi.dsi.fastutil.ints.Int2FloatMap;
//import net.minecraft.entity.Entity;
//import net.minecraft.world.World;
//
//public class ExplosionCache {
//	private static double locX;
//	private static double locY;
//	private static double locZ;
//	private static int identification;
//	private static int lastId;
//	public final Int2FloatMap explosionDensityCache = new Int2FloatLinkedOpenHashMap();
//	public long hashedExplosionLocation;
//	public int lowestIndex = -1;
//	public int highestIndex = -1;
//	public int lastIndex = -1;
//	public boolean entitiesNearby = true;
//	public boolean isWatered = false;
//	public int swingingExplosions = 0;
//	public boolean blockBreaking = true;
//	public int explosions = 1;
//	public Entity[] affectedEntities;
//	public long affectedEntitiesLocation;
//	public boolean checkEntities = true;
//	public boolean cacheEntities = true;
//	public boolean hasMomentum = false;
//	public boolean swinging = false;
//	public double posX;
//	public double posY;
//	public double posZ;
//	public double momentum;
//	public boolean blockDensity = true;
//	private int tick;
//	private final World     world;
//
//	public ExplosionCache(World worldIn) {
//		this.world = worldIn;
//		++identification;
//	}
//
//	public static boolean checkId() {
//		return identification == lastId;
//	}
//
//	public static double getDistanceSq(ExplosionCache explosionCache) {
//		return (locX - explosionCache.posX) * (locX - explosionCache.posX) + (locY - explosionCache.posY) * (locY - explosionCache.posY) + (locZ - explosionCache.posZ) * (locZ - explosionCache.posZ);
//	}
//
//	public boolean checkTick() {
////		return this.tick == Falcun.minecraft.theWorld.currentTick;
//		return this.tick == this.world.currentTick;
//	}
//
//	public void updateId() {
////		if (Falcun.minecraft.theWorld == null) {
////			this.tick = 0;
////		} else
////			this.tick = Falcun.minecraft.theWorld.currentTick;
//		this.tick = this.world.currentTick;
//		lastId = identification;
//		locX = this.posX;
//		locY = this.posY;
//		locZ = this.posZ;
//	}
//
//	public void tick() {
//		this.tick = Falcun.minecraft.theWorld.currentTick;
//	}
//
//	public int getTick() {
//		return this.tick;
//	}
//
//	public void setHashedLocation(long location) {
//		this.hashedExplosionLocation = location;
//	}
//
//	public void setMomentum(double x, double y, double z) {
//		double momentumSq = x * x + y * y + z * z;
//		this.momentum = Math.sqrt(momentumSq);
//		this.hasMomentum = this.momentum != 0.0;
//		this.swinging = momentumSq < 64.0;
//	}
//
//	public void setLocation(double x, double y, double z) {
//		this.posX = x;
//		this.posY = y;
//		this.posZ = z;
//	}
//
//	public void setWatered(boolean watered) {
//		this.isWatered = watered;
//	}
//
//	public void setIterationBounds(int highest, int lowest) {
//		this.highestIndex = highest;
//		this.lastIndex = this.lowestIndex;
//		this.lowestIndex = lowest;
//	}
//}
