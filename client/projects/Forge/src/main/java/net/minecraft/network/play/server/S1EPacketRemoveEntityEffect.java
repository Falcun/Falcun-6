package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S1EPacketRemoveEntityEffect implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private int effectId;

    public S1EPacketRemoveEntityEffect()
    {
    }

    public S1EPacketRemoveEntityEffect(int entityIdIn, PotionEffect effect)
    {
        this.entityId = entityIdIn;
        this.effectId = effect.getPotionID();
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.entityId = buf.readVarIntFromBuffer();
        this.effectId = buf.readUnsignedByte();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeByte(this.effectId);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleRemoveEntityEffect(this);
    }

    @SideOnly(Side.CLIENT)
    public int getEntityId()
    {
        return this.entityId;
    }

    @SideOnly(Side.CLIENT)
    public int getEffectId()
    {
        return this.effectId;
    }
}