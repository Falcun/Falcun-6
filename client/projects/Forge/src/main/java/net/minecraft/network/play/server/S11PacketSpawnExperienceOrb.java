package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S11PacketSpawnExperienceOrb implements Packet<INetHandlerPlayClient>
{
    private int entityID;
    private int posX;
    private int posY;
    private int posZ;
    private int xpValue;

    public S11PacketSpawnExperienceOrb()
    {
    }

    public S11PacketSpawnExperienceOrb(EntityXPOrb xpOrb)
    {
        this.entityID = xpOrb.getEntityId();
        this.posX = MathHelper.floor_double(xpOrb.posX * 32.0D);
        this.posY = MathHelper.floor_double(xpOrb.posY * 32.0D);
        this.posZ = MathHelper.floor_double(xpOrb.posZ * 32.0D);
        this.xpValue = xpOrb.getXpValue();
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.entityID = buf.readVarIntFromBuffer();
        this.posX = buf.readInt();
        this.posY = buf.readInt();
        this.posZ = buf.readInt();
        this.xpValue = buf.readShort();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.entityID);
        buf.writeInt(this.posX);
        buf.writeInt(this.posY);
        buf.writeInt(this.posZ);
        buf.writeShort(this.xpValue);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleSpawnExperienceOrb(this);
    }

    @SideOnly(Side.CLIENT)
    public int getEntityID()
    {
        return this.entityID;
    }

    @SideOnly(Side.CLIENT)
    public int getX()
    {
        return this.posX;
    }

    @SideOnly(Side.CLIENT)
    public int getY()
    {
        return this.posY;
    }

    @SideOnly(Side.CLIENT)
    public int getZ()
    {
        return this.posZ;
    }

    @SideOnly(Side.CLIENT)
    public int getXPValue()
    {
        return this.xpValue;
    }
}