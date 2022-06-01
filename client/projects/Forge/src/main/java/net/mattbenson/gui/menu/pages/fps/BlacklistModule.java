package net.mattbenson.gui.menu.pages.fps;

import net.mattbenson.gui.menu.pages.FPSPage;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;

public class BlacklistModule {
	private String name;
	private BlacklistType type;
	private Class<?> clazz;
	private EnumParticleTypes particle;
	private boolean enabled;
	
	public BlacklistModule(String name, BlacklistType type, Class<?> clazz) {
		this.name = name;
		this.type = type;
		this.clazz = clazz;
		
		switch(type) {
			case BLOCK:
				this.enabled = FPSPage.BLOCKS.contains(clazz);
				break;
				
			case ENTITY:
				this.enabled = FPSPage.ENTITIES.contains(clazz);
				break;
				
			case TILE_ENTITY:
				this.enabled = FPSPage.TILE_ENTITIES.contains(clazz);
				break;
				
			default:
				break;
		}
	}
	
	public BlacklistModule(String name, EnumParticleTypes particle) {
		this.name = name;
		this.type = BlacklistType.PARTICLE;
		this.particle = particle;
		this.enabled = FPSPage.PARTICLES.contains(particle);
	}
	
	public String getName() {
		return name;
	}
	
	public BlacklistType getType() {
		return type;
	}
	
	public Class<?> getClazz() {
		return clazz;
	}
	
	public EnumParticleTypes getParticle() {
		return particle;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		
		switch(type) {
			case BLOCK:
				if(clazz.getClass().isInstance(Block.class)) {
					Class<Block> block = (Class<Block>) clazz;
					
					if(enabled) {
						if(!FPSPage.BLOCKS.contains(block)) {
							FPSPage.BLOCKS.add(block);
						}
					} else if(FPSPage.BLOCKS.contains(block)) {
						FPSPage.BLOCKS.remove(block);
					}
				}
				
				break;
				
			case ENTITY:
				if(clazz.getClass().isInstance(Entity.class)) {
					Class<Entity> entity = (Class<Entity>) clazz;
					
					if(enabled) {
						if(!FPSPage.ENTITIES.contains(entity)) {
							FPSPage.ENTITIES.add(entity);
						}
					} else if(FPSPage.ENTITIES.contains(entity)) {
						FPSPage.ENTITIES.remove(entity);
					}
				}
				break;
				
			case TILE_ENTITY:
				if(clazz.getClass().isInstance(TileEntity.class)) {
					Class<TileEntity> tileEntity = (Class<TileEntity>) clazz;
					
					if(enabled) {
						if(!FPSPage.TILE_ENTITIES.contains(tileEntity)) {
							FPSPage.TILE_ENTITIES.add(tileEntity);
						}
					} else if(FPSPage.TILE_ENTITIES.contains(tileEntity)) {
						FPSPage.TILE_ENTITIES.remove(tileEntity);
					}
				}
				break;
				
			case PARTICLE:
				if(particle != null) {
					if(enabled) {
						if(!FPSPage.PARTICLES.contains(particle)) {
							FPSPage.PARTICLES.add(particle);
						}
					} else if(FPSPage.PARTICLES.contains(particle)) {
						FPSPage.PARTICLES.remove(particle);
					}
				}
				break;
				
			default:
				break;
		}
	}
}
