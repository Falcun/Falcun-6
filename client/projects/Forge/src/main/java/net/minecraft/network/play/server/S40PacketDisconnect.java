package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S40PacketDisconnect implements Packet<INetHandlerPlayClient>
{
    private IChatComponent reason;

    public S40PacketDisconnect()
    {
    }

    public S40PacketDisconnect(IChatComponent reasonIn)
    {
        this.reason = reasonIn;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.reason = buf.readChatComponent();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeChatComponent(this.reason);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleDisconnect(this);
    }

    @SideOnly(Side.CLIENT)
    public IChatComponent getReason()
    {
        return this.reason;
    }
}