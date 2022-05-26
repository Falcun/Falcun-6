package net.minecraft.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import falcun.net.Falcun;
import falcun.net.util.NumberConversions;
import falcun.net.z.optimizations.explosions.cache.CachedExplosion;
import falcun.net.z.optimizations.explosions.raycasting.Ray;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Explosion extends CachedExplosion {

	protected final LongOpenHashSet hashSet = new LongOpenHashSet(216);
	private final boolean isFlaming;
	private final boolean isSmoking;
	private final Random explosionRNG;
	private final World worldObj;
	private final double explosionX;
	private final double explosionY;
	private final double explosionZ;
	private final Entity exploder;
	private final float explosionSize;
	private final List<BlockPos> affectedBlockPositions;
	private final Map<EntityPlayer, Vec3> playerKnockbackMap;
	private final Vec3 position;

	@SideOnly(Side.CLIENT)
	public Explosion(World worldIn, Entity p_i45752_2_, double p_i45752_3_, double p_i45752_5_, double p_i45752_7_, float p_i45752_9_, List<BlockPos> p_i45752_10_) {
		this(worldIn, p_i45752_2_, p_i45752_3_, p_i45752_5_, p_i45752_7_, p_i45752_9_, false, true, p_i45752_10_);
	}

	@SideOnly(Side.CLIENT)
	public Explosion(World worldIn, Entity p_i45753_2_, double p_i45753_3_, double p_i45753_5_, double p_i45753_7_, float p_i45753_9_, boolean p_i45753_10_, boolean p_i45753_11_, List<BlockPos> p_i45753_12_) {
		this(worldIn, p_i45753_2_, p_i45753_3_, p_i45753_5_, p_i45753_7_, p_i45753_9_, p_i45753_10_, p_i45753_11_);
		this.affectedBlockPositions.addAll(p_i45753_12_);
	}

	public Explosion(World worldIn, Entity p_i45754_2_, double p_i45754_3_, double p_i45754_5_, double p_i45754_7_, float size, boolean p_i45754_10_, boolean p_i45754_11_) {
		super(worldIn, p_i45754_2_, p_i45754_3_, p_i45754_5_, p_i45754_7_);
		this.explosionRNG = new Random();
		this.affectedBlockPositions = Lists.<BlockPos>newArrayList();
		this.playerKnockbackMap = Maps.<EntityPlayer, Vec3>newHashMap();
		this.worldObj = worldIn;
		this.exploder = p_i45754_2_;
		this.explosionSize = size;
		this.explosionX = p_i45754_3_;
		this.explosionY = p_i45754_5_;
		this.explosionZ = p_i45754_7_;
		this.isFlaming = p_i45754_10_;
		this.isSmoking = p_i45754_11_;
		this.position = new Vec3(explosionX, explosionY, explosionZ);
	}


	public void a() {
		if (!(this.explosionSize < 0.1F) ) {
			this.cache();
			if (this.isSmoking) {
				this.handleBlocks();
			}
			this.affectEntities();

		}
	}

	public void doExplosionA() {
		Set<BlockPos> set = Sets.<BlockPos>newHashSet();
		int i = 16;

		for (int j = 0; j < 16; ++j) {
			for (int k = 0; k < 16; ++k) {
				for (int l = 0; l < 16; ++l) {
					if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
						double d0 = (double) ((float) j / 15.0F * 2.0F - 1.0F);
						double d1 = (double) ((float) k / 15.0F * 2.0F - 1.0F);
						double d2 = (double) ((float) l / 15.0F * 2.0F - 1.0F);
						double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
						d0 = d0 / d3;
						d1 = d1 / d3;
						d2 = d2 / d3;
						float f = this.explosionSize * (0.7F + this.worldObj.rand.nextFloat() * 0.6F);
						double d4 = this.explosionX;
						double d6 = this.explosionY;
						double d8 = this.explosionZ;

						for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
							BlockPos blockpos = new BlockPos(d4, d6, d8);
							IBlockState iblockstate = this.worldObj.getBlockState(blockpos);

							if (iblockstate.getBlock().getMaterial() != Material.air) {
								float f2 = this.exploder != null ? this.exploder.getExplosionResistance(this, this.worldObj, blockpos, iblockstate) : iblockstate.getBlock().getExplosionResistance(worldObj, blockpos, (Entity) null, this);
								f -= (f2 + 0.3F) * 0.3F;
							}

							if (f > 0.0F && (this.exploder == null || this.exploder.verifyExplosion(this, this.worldObj, blockpos, iblockstate, f))) {
								set.add(blockpos);
							}

							d4 += d0 * 0.30000001192092896D;
							d6 += d1 * 0.30000001192092896D;
							d8 += d2 * 0.30000001192092896D;
						}
					}
				}
			}
		}

		this.affectedBlockPositions.addAll(set);
		float f3 = this.explosionSize * 2.0F;
		int k1 = MathHelper.floor_double(this.explosionX - (double) f3 - 1.0D);
		int l1 = MathHelper.floor_double(this.explosionX + (double) f3 + 1.0D);
		int i2 = MathHelper.floor_double(this.explosionY - (double) f3 - 1.0D);
		int i1 = MathHelper.floor_double(this.explosionY + (double) f3 + 1.0D);
		int j2 = MathHelper.floor_double(this.explosionZ - (double) f3 - 1.0D);
		int j1 = MathHelper.floor_double(this.explosionZ + (double) f3 + 1.0D);
		List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB((double) k1, (double) i2, (double) j2, (double) l1, (double) i1, (double) j1));
		net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.worldObj, this, list, f3);
		Vec3 vec3 = new Vec3(this.explosionX, this.explosionY, this.explosionZ);

		for (int k2 = 0; k2 < list.size(); ++k2) {
			Entity entity = (Entity) list.get(k2);

			if (!entity.isImmuneToExplosions()) {
				double d12 = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / (double) f3;

				if (d12 <= 1.0D) {
					double d5 = entity.posX - this.explosionX;
					double d7 = entity.posY + (double) entity.getEyeHeight() - this.explosionY;
					double d9 = entity.posZ - this.explosionZ;
					double d13 = (double) MathHelper.sqrt_double(d5 * d5 + d7 * d7 + d9 * d9);

					if (d13 != 0.0D) {
						d5 = d5 / d13;
						d7 = d7 / d13;
						d9 = d9 / d13;
						double d14 = (double) this.worldObj.getBlockDensity(vec3, entity.getEntityBoundingBox());
						double d10 = (1.0D - d12) * d14;
						entity.attackEntityFrom(DamageSource.setExplosionSource(this), (float) ((int) ((d10 * d10 + d10) / 2.0D * 8.0D * (double) f3 + 1.0D)));
						double d11 = EnchantmentProtection.func_92092_a(entity, d10);
						entity.motionX += d5 * d11;
						entity.motionY += d7 * d11;
						entity.motionZ += d9 * d11;

						if (entity instanceof EntityPlayer && !((EntityPlayer) entity).capabilities.disableDamage) {
							this.playerKnockbackMap.put((EntityPlayer) entity, new Vec3(d5 * d10, d7 * d10, d9 * d10));
						}
					}
				}
			}
		}
	}

	public void doExplosionB(boolean spawnParticles) {
		this.worldObj.playSoundEffect(this.explosionX, this.explosionY, this.explosionZ, "random.explode", 4.0F, (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);

		if (this.explosionSize >= 2.0F && this.isSmoking) {
			this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D, new int[0]);
		} else {
			this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D, new int[0]);
		}

		if (this.isSmoking) {
			for (BlockPos blockpos : this.affectedBlockPositions) {
				Block block = this.worldObj.getBlockState(blockpos).getBlock();

				if (spawnParticles) {
					double d0 = (double) ((float) blockpos.getX() + this.worldObj.rand.nextFloat());
					double d1 = (double) ((float) blockpos.getY() + this.worldObj.rand.nextFloat());
					double d2 = (double) ((float) blockpos.getZ() + this.worldObj.rand.nextFloat());
					double d3 = d0 - this.explosionX;
					double d4 = d1 - this.explosionY;
					double d5 = d2 - this.explosionZ;
					double d6 = (double) MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
					d3 = d3 / d6;
					d4 = d4 / d6;
					d5 = d5 / d6;
					double d7 = 0.5D / (d6 / (double) this.explosionSize + 0.1D);
					d7 = d7 * (double) (this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3F);
					d3 = d3 * d7;
					d4 = d4 * d7;
					d5 = d5 * d7;
					this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (d0 + this.explosionX * 1.0D) / 2.0D, (d1 + this.explosionY * 1.0D) / 2.0D, (d2 + this.explosionZ * 1.0D) / 2.0D, d3, d4, d5, new int[0]);
					this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5, new int[0]);
				}

				if (block.getMaterial() != Material.air) {
					if (block.canDropFromExplosion(this)) {
						block.dropBlockAsItemWithChance(this.worldObj, blockpos, this.worldObj.getBlockState(blockpos), 1.0F / this.explosionSize, 0);
					}

					block.onBlockExploded(this.worldObj, blockpos, this);
				}
			}
		}

		if (this.isFlaming) {
			for (BlockPos blockpos1 : this.affectedBlockPositions) {
				if (this.worldObj.getBlockState(blockpos1).getBlock().getMaterial() == Material.air && this.worldObj.getBlockState(blockpos1.down()).getBlock().isFullBlock() && this.explosionRNG.nextInt(3) == 0) {
					this.worldObj.setBlockState(blockpos1, Blocks.fire.getDefaultState());
				}
			}
		}
	}

	public Map<EntityPlayer, Vec3> getPlayerKnockbackMap() {
		return this.playerKnockbackMap;
	}

	public EntityLivingBase getExplosivePlacedBy() {
		return this.exploder == null ? null : (this.exploder instanceof EntityTNTPrimed ? ((EntityTNTPrimed) this.exploder).getTntPlacedBy() : (this.exploder instanceof EntityLivingBase ? (EntityLivingBase) this.exploder : null));
	}

	public void func_180342_d() {
		this.affectedBlockPositions.clear();
	}

	public List<BlockPos> getAffectedBlockPositions() {
		return this.affectedBlockPositions;
	}

	public Vec3 getPosition() {
		return this.position;
	}

	@Override
	public void affectEntities() {
		this.affectEntities(this.mergeExplosions());
	}

	public void affectEntities(final int potential) {
		float f3 = this.explosionSize * 2.0F;
		final Vec3 vec3d = new Vec3(this.posX, this.posY, this.posZ);
		Entity[] list = this.getEntities(vec3d, f3);
		if (list != null && list.length != 0) {
			int indexLast = -1;
			int indexFirst = -1;
			int iterateFrom = 0;
			int iterateTo = list.length;
			if ( this.cache.highestIndex != -1 && !this.cache.hasMomentum) {
				iterateTo = Math.min(this.cache.highestIndex + 1, iterateTo);
				iterateFrom = this.cache.lowestIndex;
			}

			double[] impactCache = new double[6];
			boolean blockDensity = this.cache.blockDensity;


			for (int index = iterateFrom; index < iterateTo; ++index) {
				Entity entity = list[index];
				if (!entity.isDead) {
					if (entity.posY == impactCache[1] && entity.posX == impactCache[0] && entity.posZ == impactCache[2]) {
						entity.addVelocity(impactCache[3], impactCache[4], impactCache[5]);
						indexLast = index;
					} else {
						double x = entity.posX - this.posX;
						double y = entity.posY + 0.49000000953674316D - this.posY;
						double z = entity.posZ - this.posZ;
						double distanceSq = x * x + y * y + z * z;
						if (!(distanceSq > 64.0D) && distanceSq != 0.0D) {
							double density = 1.0D;
							if (blockDensity) {
								density = this.getBlockDensity(vec3d, entity.boundingBox);
								if (density == 0.0D) {
									continue;
								}
							}

							double distance = (float) Math.sqrt(distanceSq);
							double exposure = (1.0D - distance / (double) f3) * density;
//							if (!entity.cachedEntity) {
//								CraftEventFactory.entityDamage = this.source;
//								entity.forceExplosionKnockback = false;
//								boolean wasDamaged = entity.damageEntity(DamageSource.explosion(this), (float) ((int) ((exposure * exposure + exposure) / 2.0D * 8.0D * (double) f3 + 1.0D)));
//								CraftEventFactory.entityDamage = null;
//								if (!wasDamaged && !entity.forceExplosionKnockback) {
//									continue;
//								}
//							}

							exposure /= distance;
							impactCache[0] = entity.posX;
							impactCache[1] = entity.posY;
							impactCache[2] = entity.posZ;
							entity.addVelocity(impactCache[3] = x * exposure * (double) potential, impactCache[4] = y * exposure * (double) potential, impactCache[5] = z * exposure * (double) potential);

							indexLast = index;
							if (indexFirst == -1) {
								indexFirst = index;

							}
						}
					}
				}
			}

			this.cache.setIterationBounds(indexLast, indexFirst);
		} else {
			this.cache.entitiesNearby = false;
		}
	}

	@Override
	protected void affectBlocks(BlockPos.MutableBlockPos var1, Ray[] var2) {
		hashSet.clear();
		for (int i = 0; i < 1352; ++i) {
			Ray ray = var2[i];
			double d0 = ray.x;
			double d1 = ray.y;
			double d2 = ray.z;
			double stepX = this.posX;
			double stepY = this.posY;
			double stepZ = this.posZ;
			int prevX = -2147483648;
			int prevY = -2147483648;
			int prevZ = -2147483648;
			float resistance = 0.0F;
			BlockPos.MutableBlockPos mutableblockposition = new BlockPos.MutableBlockPos();
			Chunk chunk = null;

			for (float f = 1.12F * this.explosionSize; f > 0.0F; f -= 0.225001F) {
				int blockX = NumberConversions.floor(stepX);
				int blockY = NumberConversions.floor(stepY);
				int blockZ = NumberConversions.floor(stepZ);
				if (prevX == blockX && prevY == blockY && prevZ == blockZ) {
					f -= resistance;
				} else {
					int chunkX = blockX >> 4;
					int chunkZ = blockZ >> 4;
					if (chunk == null || chunk.xPosition != chunkX || chunk.zPosition != chunkZ) {
						chunk = this.world.getChunkFromChunkCoords(chunkX, chunkZ);
					}

					int blockId = 0;
					if (blockY > 0 && blockY < 256) {
						int chunkY = blockY >> 4;
						ExtendedBlockStorage section = chunk.getBlockStorageArray()[chunkY];
						if (section != null) {
							blockId = section.getData()[(blockY & 15) << 8 | (blockZ & 15) << 4 | blockX & 15];
						}
					}

					resistance = 0.0F;
					if (blockId != 0) {
						IBlockState iblockdata = Block.BLOCK_STATE_IDS.getByValue(blockId);
						if (iblockdata != null) {
							mutableblockposition.set(blockX, blockY, blockZ);
//							Block block = iblockdata.getBlock();
//								float blockResistance = block.durability / 5.0F;
							float blockResistance = 1 / 5.0F;
							resistance = (blockResistance + 0.3F) * 0.3F;
							f -= resistance;
//							if (f > 0.0F && (this.source == null || this.source.cachedEntity || this.source.a(this, this.world, mutableblockposition, iblockdata, f))) {
							if (f > 0.0F && (this.source == null || this.source.cachedEntity || this.source.verifyExplosion(this, this.world, mutableblockposition, iblockdata, f))) {
								long asLong = mutableblockposition.toLong();
								hashSet.add(asLong);
							}
						}
					}

					prevX = blockX;
					prevY = blockY;
					prevZ = blockZ;
				}

				stepX += d0;
				stepY += d1;
				stepZ += d2;
			}
		}
	}

	@Override
	protected synchronized float getBlockDensity(Vec3 vec3d, AxisAlignedBB aabb) {
		Integer hash = hashCode(this, aabb);
		Float blockDensity = this.cache.explosionDensityCache.get(hash);
		if (blockDensity == null) {
			blockDensity = this.world.a(vec3d, aabb);
			this.cache.explosionDensityCache.put(hash, blockDensity);
		}
		return blockDensity;

	}

	private static int hashCode(Explosion explosion, AxisAlignedBB aabb) {
		int result = 0;
		long temp = Double.doubleToLongBits(explosion.posX);
		result = 31 * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(explosion.posY);
		result = 31 * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(explosion.posZ);
		result = 31 * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(aabb.minX);
		result = 31 * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(aabb.minY);
		result = 31 * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(aabb.minZ);
		result = 31 * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(aabb.maxX);
		result = 31 * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(aabb.maxY);
		result = 31 * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(aabb.maxZ);
		result = 31 * result + (int) (temp ^ temp >>> 32);
		return result;
	}
}