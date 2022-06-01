package net.mattbenson.utils;


import net.minecraft.util.*;
import net.mattbenson.modules.types.factions.Patchcrumbs2;

public class PatchcrumbSoundTNT
{
    private long initTime;
    public double x;
    public double y;
    public double z;
    public AxisAlignedBB boundingBox;
    public PatchcrumbSource source;
    
    public PatchcrumbSoundTNT(final double x, final double y, final double z, final AxisAlignedBB boundingBox, final PatchcrumbSource source) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.boundingBox = boundingBox;
        this.initTime = System.currentTimeMillis();
        this.source = source;
    }
    
    public boolean expired() {
        return this.initTime + Patchcrumbs2.getInstance().timeout * 1000L < System.currentTimeMillis();
    }
}
