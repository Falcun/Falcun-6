package net.mattbenson.modules.types.fpssettings.cruches;

import org.lwjgl.opengl.GLContext;

import com.github.lunatrius.schematica.handler.client.RenderTickHandler;

import java.net.URISyntaxException;
import java.net.URI;
import java.util.Set;
import java.util.List;
import net.minecraft.client.multiplayer.WorldClient;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraftforge.client.event.RenderLivingEvent;
import java.util.function.Function;
import org.lwjgl.opengl.GL15;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.scoreboard.Team;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.renderer.entity.RenderManager;
import net.mattbenson.Falcun;
import net.mattbenson.modules.types.fpssettings.FPSSettings;
import net.minecraft.client.Minecraft;

public class EntityCulling
{
    private static final Minecraft mc;
    private static final RenderManager renderManager;
    private static final ConcurrentHashMap<UUID, OcclusionQuery> queries;
    private static final boolean SUPPORT_NEW_GL;
    public static boolean shouldPerformCulling;
    private int destroyTimer;
    public static final EntityCulling INSTANCE = new EntityCulling();
    
    public static boolean canRenderName(final EntityLivingBase entity) {
        final EntityPlayerSP player = EntityCulling.mc.thePlayer;
        if (entity instanceof EntityPlayer && entity != player) {
            final Team otherEntityTeam = entity.getTeam();
            final Team playerTeam = player.getTeam();
            if (otherEntityTeam != null) {
                final Team.EnumVisible teamVisibilityRule = otherEntityTeam.getNameTagVisibility();
                switch (teamVisibilityRule) {
                    case NEVER: {
                        return false;
                    }
                    case HIDE_FOR_OTHER_TEAMS: {
                        return playerTeam == null || otherEntityTeam.isSameTeam(playerTeam);
                    }
                    case HIDE_FOR_OWN_TEAM: {
                        return playerTeam == null || !otherEntityTeam.isSameTeam(playerTeam);
                    }
                    default: {
                        return true;
                    }
                }
            }
        }
        return Minecraft.isGuiEnabled() && entity != EntityCulling.mc.getRenderManager().livingPlayer && (entity instanceof EntityArmorStand || !entity.isInvisibleToPlayer((EntityPlayer)player)) && entity.riddenByEntity == null;
    }
    
    public static void drawSelectionBoundingBox(final AxisAlignedBB b) {
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.colorMask(false, false, false, false);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(8, DefaultVertexFormats.POSITION);
        worldrenderer.pos(b.maxX, b.maxY, b.maxZ).endVertex();
        worldrenderer.pos(b.maxX, b.maxY, b.minZ).endVertex();
        worldrenderer.pos(b.minX, b.maxY, b.maxZ).endVertex();
        worldrenderer.pos(b.minX, b.maxY, b.minZ).endVertex();
        worldrenderer.pos(b.minX, b.minY, b.maxZ).endVertex();
        worldrenderer.pos(b.minX, b.minY, b.minZ).endVertex();
        worldrenderer.pos(b.minX, b.maxY, b.minZ).endVertex();
        worldrenderer.pos(b.minX, b.minY, b.minZ).endVertex();
        worldrenderer.pos(b.maxX, b.maxY, b.minZ).endVertex();
        worldrenderer.pos(b.maxX, b.minY, b.minZ).endVertex();
        worldrenderer.pos(b.maxX, b.maxY, b.maxZ).endVertex();
        worldrenderer.pos(b.maxX, b.minY, b.maxZ).endVertex();
        worldrenderer.pos(b.minX, b.maxY, b.maxZ).endVertex();
        worldrenderer.pos(b.minX, b.minY, b.maxZ).endVertex();
        worldrenderer.pos(b.minX, b.minY, b.maxZ).endVertex();
        worldrenderer.pos(b.maxX, b.minY, b.maxZ).endVertex();
        worldrenderer.pos(b.minX, b.minY, b.minZ).endVertex();
        worldrenderer.pos(b.maxX, b.minY, b.minZ).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.enableAlpha();
    }
    
    public static boolean renderItem(final Entity stack) {
        return EntityCulling.shouldPerformCulling && Falcun.getInstance().moduleManager.getModule(FPSSettings.class).ENTITY_CULLING && stack.worldObj == EntityCulling.mc.thePlayer.worldObj && checkEntity(stack);
    }
    
    private static int getQuery() {
        try {
            return GL15.glGenQueries();
        }
        catch (Throwable throwable) {
            System.out.println("Failed to run GL15.glGenQueries(). User's computer is likely too old to support OpenGL 1.5, Entity Culling has been force disabled.");
            Falcun.getInstance().moduleManager.getModule(FPSSettings.class).ENTITY_CULLING = false;
            return 0;
        }
    }
    
    private static boolean checkEntity(final Entity entity) {
        final OcclusionQuery query = EntityCulling.queries.computeIfAbsent(entity.getUniqueID(), OcclusionQuery::new);
        if (query.refresh) {
            query.nextQuery = getQuery();
            query.refresh = false;
            final int mode = EntityCulling.SUPPORT_NEW_GL ? 35887 : 35092;
            GL15.glBeginQuery(mode, query.nextQuery);
            drawSelectionBoundingBox(entity.getEntityBoundingBox().expand(0.2, 0.2, 0.2).offset(-Minecraft.getMinecraft().getRenderManager().renderPosX, -Minecraft.getMinecraft().getRenderManager().renderPosY, -Minecraft.getMinecraft().getRenderManager().renderPosZ));
            GL15.glEndQuery(mode);
        }
        return query.occluded;
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void shouldRenderEntity(final RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (!Falcun.getInstance().moduleManager.getModule(FPSSettings.class).ENTITY_CULLING || !EntityCulling.shouldPerformCulling) {
            return;
        }

        final EntityLivingBase entity = event.entity;
        final boolean armorstand = entity instanceof EntityArmorStand;
        if (entity == EntityCulling.mc.thePlayer || entity.worldObj != EntityCulling.mc.thePlayer.worldObj || (Falcun.getInstance().moduleManager.getModule(FPSSettings.class).DONT_CULL_ARMOR_STANDS_NAMETAGS && armorstand && ((EntityArmorStand)entity).func_181026_s()) || (entity.isInvisibleToPlayer((EntityPlayer)EntityCulling.mc.thePlayer) && !armorstand)) {
            return;
        }
        if (checkEntity((Entity)entity)) {
            event.setCanceled(true);
            if (!canRenderName(entity)) {
                return;
            }
            if ((Falcun.getInstance().moduleManager.getModule(FPSSettings.class).DONT_CULL_PLAYER_NAMETAGS && entity instanceof EntityPlayer) || (Falcun.getInstance().moduleManager.getModule(FPSSettings.class).DONT_CULL_ENTITY_NAMETAGS && !armorstand) || (Falcun.getInstance().moduleManager.getModule(FPSSettings.class).DONT_CULL_ARMOR_STANDS_NAMETAGS && armorstand)) {
                final double x = event.x;
                final double y = event.y;
                final double z = event.z;
                final RendererLivingEntity<EntityLivingBase> renderer = (RendererLivingEntity<EntityLivingBase>)event.renderer;
                renderer.renderName(entity, x, y, z);
            }
        }
    }
    
    @SubscribeEvent
    public void renderTickEvent(final TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !Falcun.getInstance().moduleManager.getModule(FPSSettings.class).ENTITY_CULLING) {
            return;
        }
        Minecraft.getMinecraft().addScheduledTask(this::check);
    }
    
    private void check() {
        long delay = 0L;
        switch (Falcun.getInstance().moduleManager.getModule(FPSSettings.class).ENTITY_CULLING_INTERVAL) {
            case 0: {
                delay = 50L;
                break;
            }
            case 1: {
                delay = 25L;
                break;
            }
            case 2: {
                delay = 10L;
                break;
            }
        }
        final long nanoTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
        for (final OcclusionQuery query : EntityCulling.queries.values()) {
            if (query.nextQuery != 0) {
                final long queryObject = GL15.glGetQueryObjecti(query.nextQuery, 34919);
                if (queryObject != 0L) {
                    query.occluded = (GL15.glGetQueryObjecti(query.nextQuery, 34918) == 0);
                    GL15.glDeleteQueries(query.nextQuery);
                    query.nextQuery = 0;
                }
            }
            if (query.nextQuery == 0 && nanoTime - query.executionTime > delay) {
                query.executionTime = nanoTime;
                query.refresh = true;
            }
        }
    }
    
    @SubscribeEvent
    public void tick(final TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !Falcun.getInstance().moduleManager.getModule(FPSSettings.class).ENTITY_CULLING || this.destroyTimer++ < 120) {
            return;
        }
        this.destroyTimer = 0;
        final WorldClient theWorld = EntityCulling.mc.theWorld;
        if (theWorld == null) {
            return;
        }
        final List<UUID> remove = new ArrayList<UUID>();
        final Set<UUID> loaded = new HashSet<UUID>();
        for (final Entity entity : theWorld.loadedEntityList) {
            loaded.add(entity.getUniqueID());
        }
        for (final OcclusionQuery value : EntityCulling.queries.values()) {
            if (loaded.contains(value.uuid)) {
                continue;
            }
            remove.add(value.uuid);
            if (value.nextQuery == 0) {
                continue;
            }
            GL15.glDeleteQueries(value.nextQuery);
        }
        for (final UUID uuid : remove) {
            EntityCulling.queries.remove(uuid);
        }
    }
    
    static {
        mc = Minecraft.getMinecraft();
        renderManager = EntityCulling.mc.getRenderManager();
        queries = new ConcurrentHashMap<UUID, OcclusionQuery>();
        SUPPORT_NEW_GL = GLContext.getCapabilities().OpenGL33;
        EntityCulling.shouldPerformCulling = false;
    }
    
    static class OcclusionQuery
    {
        private final UUID uuid;
        private int nextQuery;
        private boolean refresh;
        private boolean occluded;
        private long executionTime;
        
        public OcclusionQuery(final UUID uuid) {
            this.refresh = true;
            this.executionTime = 0L;
            this.uuid = uuid;
        }
    }
}