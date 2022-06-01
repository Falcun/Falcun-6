package net.mattbenson.events.types.render;

import net.mattbenson.events.Event;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;

//Code at RendererLivingEntity.java
public class RenderEntityEvent <T extends EntityLivingBase> extends Event {
	private T entity;
	private RendererLivingEntity<T> renderer;
	private double x;
	private double y;
	private double z;
	
	public RenderEntityEvent(T entity, RendererLivingEntity<T> renderer, double x, double y, double z) {
		this.entity = entity;
		this.renderer = renderer;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public T getEntity() {
		return entity;
	}
	
	public RendererLivingEntity<T> getRenderer() {
		return renderer;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
}
