package falcun.net.util;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class ConnectedServerUtil {

	private ConnectedServerUtil() {
	}

	private static boolean isLocal = false;
	private static String serverIP = "";

	public static boolean isLocal() {
		return isLocal;
	}

	public static String getServerIP(){
		return serverIP == null ? "" : serverIP;
	}


	public final static ConnectedServerUtil instance = new ConnectedServerUtil();

	public static void init() {
		MinecraftForge.EVENT_BUS.register(instance);
	}

	@SubscribeEvent
	public void ServerJoin(FMLNetworkEvent<?> e) {
		if (e instanceof FMLNetworkEvent.ClientConnectedToServerEvent) {
			serverJoin((FMLNetworkEvent.ClientConnectedToServerEvent) e);
		}
	}

	private void serverJoin(FMLNetworkEvent.ClientConnectedToServerEvent e) {
		if ((isLocal = e.isLocal)) {
			return;
		}


	}


}
