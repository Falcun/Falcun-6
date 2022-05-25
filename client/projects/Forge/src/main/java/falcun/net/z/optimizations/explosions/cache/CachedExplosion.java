package falcun.net.z.optimizations.explosions.cache;

import falcun.net.Falcun;
import falcun.net.util.ChunkUtil;
import falcun.net.util.Hash;
import falcun.net.util.NumberConversions;
import falcun.net.z.optimizations.explosions.raycasting.BlockSearching;
import falcun.net.z.optimizations.explosions.raycasting.Ray;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.Position;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class CachedExplosion {

	protected static final double HEIGHT = 0.49f;
	protected final World world;
	protected Block blockAt;
	protected boolean isWatered;
	protected double posX;
	protected double posY;
	protected double posZ;
	public ExplosionCache cache;
	public Entity source;

	public CachedExplosion(World world, Entity entity, double d0, double d1, double d2) {
		this.world = world;
		this.source = entity;
		this.posX = d0;
		this.posY = d1;
		this.posZ = d2;
	}

	public void cache() {
		if (this.source == null) {
			this.cache = new ExplosionCache();
			return;
		}
		long locationHash = Hash.fnv1a(this.source.prevPosX, this.source.prevPosY, this.source.prevPosZ);
		double movedX = this.source.posX - this.source.prevPosX;
		double movedY = this.source.posY - this.source.prevPosY;
		double movedZ = this.source.posZ - this.source.prevPosZ;
		this.cache = this.world.explosionCache.get(locationHash);
		if (this.cache != null) {
			this.updateCache(movedX, movedY, movedZ);
		} else {
			this.createCache(locationHash, movedX, movedY, movedZ);
		}
		this.cache.setMomentum(movedX, movedY, movedZ);
	}

	private void updateCache(double movedX, double movedY, double movedZ) {
		ExplosionCache cache = this.cache;
		if (!cache.checkTick() || !ExplosionCache.checkId()) {
			if (!cache.checkTick() || ExplosionCache.getDistanceSq(cache) > 64.0) {
				if (!this.cache.checkTick()) {
					this.cache.explosionDensityCache.clear();
				}
				if (!cache.checkTick() || !cache.swinging) {
					cache.swingingExplosions = 7;
					{
						this.blockAt =  ChunkUtil.getBlock((int)this.posX, (int) this.posY, (int) this.posZ);
//						this.blockAt = this.chunkUtils.getBlock(this.posX, this.posY, this.posZ);
						cache.setWatered(this.blockAt.getMaterial().isLiquid());
					}
				}
				cache.highestIndex = -1;
				cache.lowestIndex = -1;
				cache.entitiesNearby = false;
				cache.affectedEntitiesLocation = 0L;
				cache.affectedEntities = null;
				cache.blockDensity = true;
				cache.explosions = 0;
			}
			cache.updateId();
		}
		++cache.explosions;
		if (movedX * movedX + movedY * movedY + movedZ * movedZ <= 64.0) {
			++cache.swingingExplosions;
		}
	}


	private void createCache(long locationHash, double movedX, double movedY, double movedZ) {
		ExplosionCache cache;
		this.cache = cache = new ExplosionCache();
		this.world.explosionCache.put(locationHash, this.cache);
		if (cache.swingingExplosions <= 1) {
			if (!Falcun.minecraft.theWorld.isRemote) {
//				EntityExplodeEvent event = new EntityExplodeEvent(this.source.getBukkitEntity(), location, list, 0.3f);
//				this.world.getServer().getPluginManager().callEvent(event);
				cache.blockBreaking = false;
			}
		}
		cache.setHashedLocation(locationHash);
		cache.setLocation(this.posX, this.posY, this.posZ);
		cache.updateId();

//		this.blockAt = this.chunkUtils.getBlock(this.posX, this.posY, this.posZ);
		this.blockAt = ChunkUtil.getBlock((int)this.posX, (int) this.posY, (int) this.posZ);
		cache.setWatered(this.blockAt.getMaterial().isLiquid());
	}


	protected Entity[] getEntities(Vec3 vec3d, double d0) {
		ExplosionCache cache = this.cache;
		if (!(ExplosionCache.getDistanceSq(cache) <= 64.0) || !cache.entitiesNearby) {
			return null;
		}
		long location = cache.hashedExplosionLocation;
		if (this.source == null) {
			location = Hash.fnv1a(new long[]{(int) this.posX, (int) this.posY, (int) this.posZ});
		}
		if (cache.explosions > 24 && cache.lastIndex == cache.lowestIndex) {
			cache.cacheEntities = false;
		}
		if (cache.cacheEntities && cache.affectedEntitiesLocation == location) {
			return cache.affectedEntities;
		}
		cache.affectedEntitiesLocation = location;
		double minX = this.posX - d0 - 1.0;
		double maxX = this.posX + d0 + 1.0;
		double minY = MathHelper.clamp(this.posY - d0 - 1.0, 0.0, 255.0);
		double maxY = MathHelper.clamp(this.posY + d0 + 1.0, 0.0, 255.0);
		double minZ = this.posZ - d0 - 1.0;
		double maxZ = this.posZ + d0 + 1.0;
		Entity[] array = this.world.getEntities(new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ));
		cache.highestIndex = -1;
		cache.lowestIndex = -1;
		cache.affectedEntities = array;
		return array;
	}

	protected int mergeExplosions() {
		int merged = 1;
		if (this.source != null && this.source.cachedEntity) {
			if (!this.cache.swinging && this.cache.swingingExplosions > 8) {
				return this.mergeTunnelling();
			}
			if (this.source.prevPosX == this.source.posX && this.source.prevPosY == this.source.posY && this.source.prevPosZ == this.source.posZ) {
				return this.mergeStationary();
			}
		}
		return merged;
	}


	private int mergeTunnelling() {
		int merged = this.source.potential;
		if (merged > 1) {
			((EntityTNTPrimed) this.source).canExplode = false;
			this.source.potential = 1;
		} else {
			int i2 = this.world.loadedEntityList.indexOf(this.source) + 1;
			while (i2 < this.world.loadedEntityList.size()) {
				Entity en = this.world.loadedEntityList.get(i2);
				if (!en.isDead && en instanceof EntityTNTPrimed && ((EntityTNTPrimed) en).fuse <= 1) {
					if (this.source.prevPosX == en.posX && this.source.prevPosY == en.posY && this.source.prevPosZ == en.posZ) {
						((EntityTNTPrimed) en).canExplode = false;
						merged += en.potential;
					}
				} else {
					return merged;
				}
				++i2;
			}
		}
		return merged;
	}

	private int mergeStationary() {
		if (this.blockAt == null) {
//			this.blockAt = this.chunkUtils.getBlock(this.posX, this.posY, this.posZ);
			this.blockAt = ChunkUtil.getBlock((int)this.posX, (int) this.posY, (int) this.posZ);
		}
//		Block underBlock = this.chunkUtils.getBlock(this.posX, this.posY - 1.0, this.posZ);
		Block underBlock = ChunkUtil.getBlock((int)this.posX, (int) this.posY - 1, (int) this.posZ);
		if (!this.cache.isWatered && (underBlock instanceof BlockFalling)) {
			return 1;
		}
		int merged = this.source.potential;
		if (merged > 1) {
			((EntityTNTPrimed) this.source).canExplode = false;
			this.source.potential = 1;
		} else {
			int i2 = this.world.loadedEntityList.indexOf(this.source) + 1;
			while (i2 < this.world.loadedEntityList.size()) {
				Entity en = this.world.loadedEntityList.get(i2);
				if (!en.isDead && en instanceof EntityTNTPrimed && ((EntityTNTPrimed) en).fuse <= 1) {
					if (this.source.checkEquality(en)) {
						((EntityTNTPrimed) en).canExplode = false;
						merged += en.potential;
					}
				} else {
					return merged;
				}
				++i2;
			}
		}
		return merged;
	}

	public abstract void affectEntities();


	public void handleBlocks() {
		ExplosionCache cache = this.cache;
		if (!(cache.isWatered || cache.swingingExplosions > 8 || !cache.blockBreaking)) {
			this.isWatered = cache.isWatered;
			BlockPos.MutableBlockPos blockposition = new BlockPos.MutableBlockPos(NumberConversions.floor(this.posX), NumberConversions.floor(this.posY), NumberConversions.floor(this.posZ));
			if (!this.isWatered) {
				double z;
				double y;
				double x;
				double distanceSq;
				Ray[] rays = BlockSearching.generalRays;
				if (this.source != null && cache.swingingExplosions == 0 && !cache.swinging && (distanceSq = (x = this.source.posX - this.source.prevPosX) * x + (y = this.source.posY - this.source.prevPosY) * y + (z = this.source.posZ - this.source.prevPosZ) * z) > 64.0) {
					rays = BlockSearching.nukingRays;
				}
				this.affectBlocks(blockposition, rays);
			}
		}
	}

	protected abstract void affectBlocks(BlockPos.MutableBlockPos var1, Ray[] var2);

	protected abstract float getBlockDensity(Vec3 var1, AxisAlignedBB var2);
}
