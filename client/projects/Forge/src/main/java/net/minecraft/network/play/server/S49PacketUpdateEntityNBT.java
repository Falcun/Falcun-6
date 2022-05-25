package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S49PacketUpdateEntityNBT implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private NBTTagCompound tagCompound;

    public S49PacketUpdateEntityNBT()
    {
    }

    public S49PacketUpdateEntityNBT(int entityIdIn, NBTTagCompound tagCompoundIn)
    {
        this.entityId = entityIdIn;
        this.tagCompound = tagCompoundIn;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.entityId = buf.readVarIntFromBuffer();
        this.tagCompound = buf.readNBTTagCompoundFromBuffer();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeNBTTagCompoundToBuffer(this.tagCompound);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEntityNBT(this);
    }

    @SideOnly(Side.CLIENT)
    public NBTTagCompound getTagCompound()
    {
        return this.tagCompound;
    }

    @SideOnly(Side.CLIENT)
    public Entity getEntity(World worldIn)
    {
        return worldIn.getEntityByID(this.entityId);
    }
}