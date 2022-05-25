package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S35PacketUpdateTileEntity implements Packet<INetHandlerPlayClient>
{
    private BlockPos blockPos;
    private int metadata;
    private NBTTagCompound nbt;

    public S35PacketUpdateTileEntity()
    {
    }

    public S35PacketUpdateTileEntity(BlockPos blockPosIn, int metadataIn, NBTTagCompound nbtIn)
    {
        this.blockPos = blockPosIn;
        this.metadata = metadataIn;
        this.nbt = nbtIn;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.blockPos = buf.readBlockPos();
        this.metadata = buf.readUnsignedByte();
        this.nbt = buf.readNBTTagCompoundFromBuffer();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeBlockPos(this.blockPos);
        buf.writeByte((byte)this.metadata);
        buf.writeNBTTagCompoundToBuffer(this.nbt);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleUpdateTileEntity(this);
    }

    @SideOnly(Side.CLIENT)
    public BlockPos getPos()
    {
        return this.blockPos;
    }

    @SideOnly(Side.CLIENT)
    public int getTileEntityType()
    {
        return this.metadata;
    }

    @SideOnly(Side.CLIENT)
    public NBTTagCompound getNbtCompound()
    {
        return this.nbt;
    }
}