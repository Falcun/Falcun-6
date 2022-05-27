package falcun.net.modules.factions;

import falcun.net.Falcun;
import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.*;
import falcun.net.api.modules.inheritance.FalcunEventBusModule;
import falcun.net.modules.ModuleCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@FalcunModuleInfo(fileName = "Patchcrumbs", name = "Patchcrumbs", description = "Shows you where to patch", version = "1.0.0", category = ModuleCategory.FACS)
public class Patchcrumbs extends FalcunModule implements FalcunEventBusModule {

    @FalcunSetting("Timeout (sec)")
    @FalcunBounds(min = 1, max = 60)
    FalcunValue<Integer> time = new FalcunValue<>(2);

    @FalcunSetting("Width")
    @FalcunBounds(min = 0.1F, max = 5.0F)
    FalcunValue<Float> width = new FalcunValue<>(1F);

    @FalcunSetting("Distance")
    @FalcunBounds(min = 50, max = 1000)
    FalcunValue<Integer> distance = new FalcunValue<>(200);
    enum Directions{
        NORTH_SOUTH, EAST_WEST, BOTH
    }

    @FalcunSetting("Side")
    FalcunValue<Directions> mode = new FalcunValue<>(Directions.BOTH);

    @FalcunSetting("Render Tag")
    FalcunValue<Boolean> renderTag = new FalcunValue<>(true);

    @FalcunSetting("Tracers")
    FalcunValue<Boolean> tracers = new FalcunValue<>(true);

    @FalcunSetting("STill Merge TNT")
    FalcunValue<Boolean> mergeTNT = new FalcunValue<>(false);

    @FalcunSetting("ABC")
    FalcunValue<Boolean> abc = new FalcunValue<>(true);

    @FalcunSetting("Minimap Display")
    FalcunValue<Boolean> minimap = new FalcunValue<>(true);

    @FalcunSetting("Tracer Color")
    FalcunValue<FalcunColor> tracerColor = new FalcunValue<>(new FalcunColor(Color.RED.getRGB()));

    @FalcunSetting("Patch Line Color")
    FalcunValue<FalcunColor> patchColor = new FalcunValue<>(new FalcunColor(Color.ORANGE.getRGB()));

    @FalcunSetting("Shot Color")
    FalcunValue<FalcunColor> shotColor = new FalcunValue<>(new FalcunColor(Color.RED.getRGB()));

    @FalcunSetting("Text Color")
    FalcunValue<FalcunColor> textColor = new FalcunValue<>(new FalcunColor(Color.WHITE.getRGB()));

    @FalcunSetting("Shout TNT Coordinates Key")
    FalcunValue<FalcunKeyBind> keyBind2 = new FalcunValue<>(new FalcunKeyBind());

    @FalcunSetting("Font Color")
    FalcunValue<FalcunColor> fontColor = new FalcunValue<>(new FalcunColor(Color.WHITE.getRGB()));

    @FalcunSetting("Background")
    FalcunValue<Boolean> background = new FalcunValue<>(true);

    @FalcunSetting("Background Color")
    FalcunValue<FalcunColor> backgroundColor = new FalcunValue<>(new FalcunColor(new Color(0,0,0,150).getRGB()));

    @FalcunSetting("Chat Format")
    FalcunValue<String> chatFormat = new FalcunValue<>("X: {x}, Y: {y}, Z: {z}");

    enum wallCoord{
    }

    private transient ArrayList<WallCoord> wallCoords = new ArrayList<WallCoord>();
    public transient PatchCrumb currentCrumb = null;
    private transient PatchCrumb lastCrumb;

    private transient int currentCoordX;
    private transient int currentCoordZ;
    private transient double highestY;
    private transient TNTCrumb currentCrumbEntity;
    private transient TNTCrumb lastCrumb1;

    private transient long nextAllowed = 0L;
    private transient int timeout = 2;

//    private HUDElement hud;

    public class WallCoord {
        public int x;
        public int z;
        public int y;
        public double firstVelocityX;
        public double firstVelocityZ;
        public double firstYLevel;
        public long expiresAt;
        public AxisAlignedBB boundingBOX;
        public HashMap<UUID, TNTSpot> tntSpots;

        public WallCoord(final EntityTNTPrimed entityTNTPrimed) {
            this.x = MathHelper.floor_double(entityTNTPrimed.posX);
            this.z = MathHelper.floor_double(entityTNTPrimed.posZ);
            this.y = MathHelper.floor_double(entityTNTPrimed.posY);
            this.firstYLevel = entityTNTPrimed.posY;
            (this.tntSpots = new HashMap<UUID, TNTSpot>()).put(entityTNTPrimed.getUniqueID(),
                    new TNTSpot(entityTNTPrimed));
            this.expiresAt = System.currentTimeMillis() + 4500L;
            this.boundingBOX = entityTNTPrimed.getEntityBoundingBox();
            this.firstVelocityX = entityTNTPrimed.prevPosX - entityTNTPrimed.posX;
            this.firstVelocityZ = entityTNTPrimed.prevPosZ - entityTNTPrimed.posZ;
        }
        public class TNTSpot {
            public double x;
            public double y;
            public double z;
            public long fuseExpires;

            public TNTSpot(final EntityTNTPrimed tntPrimed) {
                this.x = tntPrimed.posX;
                this.y = tntPrimed.posY;
                this.z = tntPrimed.posZ;
                this.fuseExpires = System.currentTimeMillis() + tntPrimed.fuse * 50L;
            }
        }
        public void addTNT(final EntityTNTPrimed tnt) {
            if (this.tntSpots.containsKey(tnt.getUniqueID())) {
                return;
            }
            this.tntSpots.put(tnt.getUniqueID(), new TNTSpot(tnt));
        }

        public boolean testTNT(final EntityTNTPrimed tnt) {
            if (MathHelper.floor_double(tnt.posX) == this.x && MathHelper.floor_double(tnt.posZ) == this.z) {
                this.addTNT(tnt);
                return true;
            }
            return false;
        }
    }


    public class PatchCrumb {
        public int posX;
        public int posY;
        public int posZ;
        public Direction direction;
        public long expiresAt;
        public AxisAlignedBB boundingBOX;

        public PatchCrumb(final int x, final int y, final int z, final Direction direction, final AxisAlignedBB bb) {
            this.posX = x;
            this.posY = y;
            this.posZ = z;
            this.direction = direction;
            this.expiresAt = System.currentTimeMillis() + time.getValue() * 1000L;
            this.boundingBOX = bb;
        }

        public PatchCrumb(final int x, final int y, final int z) {
            this.posX = x;
            this.posY = y;
            this.posZ = z;
            this.direction = Direction.NORTHSOUTH;
            this.expiresAt = System.currentTimeMillis() + time.getValue() * 1000L;
            this.boundingBOX = new AxisAlignedBB(x + 0.5, y + 0.5, z + 0.5, x - 0.5, y - 0.5, z - 0.5);
        }
    }
    public class TNTCrumb {
        public int x;
        public int y;
        public int z;
        public long expireTime;
        public long expireChecks;
        public double posX;
        public double posZ;
        public double posY;
        public double lastX;
        public double lastZ;
        public int amount;
        public AxisAlignedBB boundingBOX;
        public Entity entity;

        public TNTCrumb(final EntityTNTPrimed tnt, final int timeout) {
            this.amount = 0;
            this.entity = (Entity) tnt;
            this.x = (int) Math.ceil(tnt.posX);
            this.y = (int) Math.ceil(tnt.posY) + 1;
            this.z = (int) Math.ceil(tnt.posZ);
            this.posX = tnt.posX;
            this.posZ = tnt.posZ;
            this.posY = tnt.posY;
            this.lastX = tnt.prevPosX;
            this.lastZ = tnt.prevPosZ;
            this.boundingBOX = tnt.getEntityBoundingBox();
            this.expireTime = System.currentTimeMillis() + timeout * 1000L;
            this.expireChecks = System.currentTimeMillis() + tnt.fuse * 50L;
        }

        public TNTCrumb(final int xx, final double yy, final int zz, final int timeout) {
            this.amount = 0;
            this.x = (int) Math.ceil(xx);
            this.y = (int) Math.ceil(yy) + 1;
            this.z = (int) Math.ceil(zz);
            this.posX = xx;
            this.posZ = zz;
            this.posY = yy;
            this.lastX = -1.0;
            this.lastZ = -1.0;
            this.boundingBOX = new AxisAlignedBB(xx + 0.5, yy + 0.5, zz + 0.5, xx - 0.5, yy - 0.5, zz - 0.5);
            this.expireTime = System.currentTimeMillis() + timeout * 1000L;
        }

        public TNTCrumb(final int xx, final double yy, final int zz, final int timeout, final AxisAlignedBB box) {
            this.amount = 0;
            this.x = (int) Math.ceil(xx);
            this.y = (int) Math.ceil(yy) + 1;
            this.z = (int) Math.ceil(zz);
            this.posX = xx;
            this.posZ = zz;
            this.posY = yy;
            this.lastX = -1.0;
            this.lastZ = -1.0;
            this.boundingBOX = box;
            this.expireTime = System.currentTimeMillis() + timeout * 1000L;
        }

        public void increaseAmount() {
            ++this.amount;
        }
    }

    public enum Direction {
        NORTHSOUTH, EASTWEST;
    }

}
