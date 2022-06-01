package falcun.net.socket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import falcun.net.socket.events.SocketEvent;
import falcun.net.util.HWIDUtil;
import io.socket.client.IO;
import io.socket.client.Socket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import java.util.Arrays;
import java.util.UUID;

public final class SocketHandler {

	private final Gson gson = new Gson();
	private Socket socket;
	public static final SocketHandler socketHandler = new SocketHandler();

	public static void init() {
	}


	private SocketHandler() {
		try {
			IO.Options options = new IO.Options();
			options.forceNew = true;
			options.transports = new String[]{"websocket"};
//			this.socket = IO.socket("https://websockets.orbitclient.com/", options);
//			if (TweakManager.deObfEnv) {
//				this.socket = IO.socket("https://websockets.orbitclient.com/", options);
//			} else {
			this.socket = IO.socket("http://127.0.0.1:3000", options);
//			}
			this.socket.on("connect_error", objectArray -> System.out.println("Connect Error: " + Arrays.toString(objectArray))).on("connect", objectArray -> {
				System.out.println("Socket connected to the Orbit Server.");
				this.sendClientInfo();
				this.send("isVerified", new JsonObject());
				this.send("getFeaturedServers", new JsonObject());
			}).on("event", objectArray -> {
				String string = (String) objectArray[0];
				JsonObject jsonObject = this.gson.fromJson(string, JsonObject.class);
				String string2 = jsonObject.get("name").getAsString().toLowerCase();
				JsonObject jsonObject2 = jsonObject.get("values").getAsJsonObject();
				postEvent(string2, jsonObject2);
			}).on("disconnect", objectArray -> System.out.println("Socket disconnected: " + Arrays.toString(objectArray)));
		} catch (Exception exception) {
			exception.printStackTrace();
			System.out.println("Couldn't connect to the socket.");
		}
	}

	private static void post(SocketEvent event) {
		MinecraftForge.EVENT_BUS.post(event);
	}


	private void postEvent(String name, JsonObject jsonObject) {
		int len = name.length();
		if (len > 0 && len < 6) {
			switch (name) {
				default:
					break;
			}
		} else if (len > 5 && len < 10) {
			switch (name) {
				case "rclnti":
					post(new SocketEvent.In.RequestClientInfo(jsonObject));
					break;
				default:
					break;
			}
		} else if (len > 9 && len < 16) {
			switch (name) {
				default:
					break;
			}
		} else if (len > 16) {
			switch (name) {
				default:
					break;
			}
		}
	}

	public void sendClientInfo() {
		Minecraft minecraft = Minecraft.getMinecraft();
		UUID uUID = minecraft.getSession().getProfile().getId();
		String string = minecraft.getSession().getUsername();
		String string2 = HWIDUtil.getHWID();
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("UUID", uUID.toString());
		jsonObject.addProperty("IGN", string);
//		jsonObject.addProperty("PURCHASE_ID", OrbitClient.getInstance().getPurchaseID());
		jsonObject.addProperty("HWID", string2);
		this.send("clnti", jsonObject);
	}

	public void send(String string, JsonObject jsonObject) {
		try {
			JsonObject jsonObject2 = new JsonObject();
			jsonObject2.addProperty("name", string);
			jsonObject2.add("values", jsonObject);
			this.socket.emit("event", this.gson.toJson(jsonObject2));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public boolean isConnected() {
		return this.socket.connected();
	}

	public void start() {
		if (this.socket.connected()) {
			return;
		}
		this.socket.open();
	}

	public void reconnect() {
		if (this.socket.connected()) {
			this.socket.disconnect();
		}
		this.socket.connect();
	}
}
