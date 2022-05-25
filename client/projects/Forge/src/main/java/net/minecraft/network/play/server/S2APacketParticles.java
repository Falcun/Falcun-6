package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S2APacketParticles implements Packet<INetHandlerPlayClient>
{
    private EnumParticleTypes particleType;
    private float xCoord;
    private float yCoord;
    private float zCoord;
    private float xOffset;
    private float yOffset;
    private float zOffset;
    private float particleSpeed;
    private int particleCount;
    private boolean longDistance;
    private int[] particleArguments;

    public S2APacketParticles()
    {
    }

    public S2APacketParticles(EnumParticleTypes particleTypeIn, boolean longDistanceIn, float x, float y, float z, float xOffsetIn, float yOffset, float zOffset, float particleSpeedIn, int particleCountIn, int... particleArgumentsIn)
    {
        this.particleType = particleTypeIn;
        this.longDistance = longDistanceIn;
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        this.xOffset = xOffsetIn;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
        this.particleSpeed = particleSpeedIn;
        this.particleCount = particleCountIn;
        this.particleArguments = particleArgumentsIn;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.particleType = EnumParticleTypes.getParticleFromId(buf.readInt());

        if (this.particleType == null)
        {
            this.particleType = EnumParticleTypes.BARRIER;
        }

        this.longDistance = buf.readBoolean();
        this.xCoord = buf.readFloat();
        this.yCoord = buf.readFloat();
        this.zCoord = buf.readFloat();
        this.xOffset = buf.readFloat();
        this.yOffset = buf.readFloat();
        this.zOffset = buf.readFloat();
        this.particleSpeed = buf.readFloat();
        this.particleCount = buf.readInt();
        int i = this.particleType.getArgumentCount();
        this.particleArguments = new int[i];

        for (int j = 0; j < i; ++j)
        {
            this.particleArguments[j] = buf.readVarIntFromBuffer();
        }
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeInt(this.particleType.getParticleID());
        buf.writeBoolean(this.longDistance);
        buf.writeFloat(this.xCoord);
        buf.writeFloat(this.yCoord);
        buf.writeFloat(this.zCoord);
        buf.writeFloat(this.xOffset);
        buf.writeFloat(this.yOffset);
        buf.writeFloat(this.zOffset);
        buf.writeFloat(this.particleSpeed);
        buf.writeInt(this.particleCount);
        int i = this.particleType.getArgumentCount();

        for (int j = 0; j < i; ++j)
        {
            buf.writeVarIntToBuffer(this.particleArguments[j]);
        }
    }

    @SideOnly(Side.CLIENT)
    public EnumParticleTypes getParticleType()
    {
        return this.particleType;
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleParticles(this);
    }

    @SideOnly(Side.CLIENT)
    public boolean isLongDistance()
    {
        return this.longDistance;
    }

    @SideOnly(Side.CLIENT)
    public double getXCoordinate()
    {
        return (double)this.xCoord;
    }

    @SideOnly(Side.CLIENT)
    public double getYCoordinate()
    {
        return (double)this.yCoord;
    }

    @SideOnly(Side.CLIENT)
    public double getZCoordinate()
    {
        return (double)this.zCoord;
    }

    @SideOnly(Side.CLIENT)
    public float getXOffset()
    {
        return this.xOffset;
    }

    @SideOnly(Side.CLIENT)
    public float getYOffset()
    {
        return this.yOffset;
    }

    @SideOnly(Side.CLIENT)
    public float getZOffset()
    {
        return this.zOffset;
    }

    @SideOnly(Side.CLIENT)
    public float getParticleSpeed()
    {
        return this.particleSpeed;
    }

    @SideOnly(Side.CLIENT)
    public int getParticleCount()
    {
        return this.particleCount;
    }

    @SideOnly(Side.CLIENT)
    public int[] getParticleArgs()
    {
        return this.particleArguments;
    }
}