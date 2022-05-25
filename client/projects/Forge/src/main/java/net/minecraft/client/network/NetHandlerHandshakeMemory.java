package net.minecraft.client.network;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.NetHandlerLoginServer;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NetHandlerHandshakeMemory implements INetHandlerHandshakeServer
{
    private final MinecraftServer mcServer;
    private final NetworkManager networkManager;

    public NetHandlerHandshakeMemory(MinecraftServer p_i45287_1_, NetworkManager p_i45287_2_)
    {
        this.mcServer = p_i45287_1_;
        this.networkManager = p_i45287_2_;
    }

    public void processHandshake(C00Handshake packetIn)
    {
        if (!net.minecraftforge.fml.common.FMLCommonHandler.instance().handleServerHandshake(packetIn, this.networkManager)) return;
        this.networkManager.setConnectionState(packetIn.getRequestedState());
        this.networkManager.setNetHandler(new NetHandlerLoginServer(this.mcServer, this.networkManager));
    }

    public void onDisconnect(IChatComponent reason)
    {
    }
}