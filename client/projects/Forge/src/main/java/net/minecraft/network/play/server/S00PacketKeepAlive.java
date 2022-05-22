package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S00PacketKeepAlive implements Packet<INetHandlerPlayClient>
{
    private int id;

    public S00PacketKeepAlive()
    {
    }

    public S00PacketKeepAlive(int idIn)
    {
        this.id = idIn;
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleKeepAlive(this);
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.id = buf.readVarIntFromBuffer();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.id);
    }

    @SideOnly(Side.CLIENT)
    public int func_149134_c()
    {
        return this.id;
    }
}