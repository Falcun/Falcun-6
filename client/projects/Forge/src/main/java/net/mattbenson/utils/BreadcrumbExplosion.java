package net.mattbenson.utils;

import net.mattbenson.Falcun;
import net.mattbenson.modules.types.factions.Breadcrumbsmod;

public class BreadcrumbExplosion {
    public double posX;
    public double posY;
    public double posZ;
    public Timer tu;

    public BreadcrumbExplosion(double x, double y, double z) {
        posX = x;
        posY = y;
        posZ = z;
        tu = new Timer();
        tu.reset();
    }

    public boolean isDone() {
        if (tu.hasReached(Falcun.getInstance().moduleManager.getModule(Breadcrumbsmod.class).age * 1000))
            return true;
        return false;
    }
}