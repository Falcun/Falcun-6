package falcun.net.socket.events;

import com.google.gson.JsonObject;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class SocketEvent extends Event {

	public final JsonObject jsonObject;

	private SocketEvent(JsonObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public static abstract class In {
		private In() {
		}

		public static final class RequestClientInfo extends Auth {
			public RequestClientInfo(JsonObject jsonObject) {
				super(jsonObject);
			}
		}

		public static final class Verification extends Auth {
			public Verification(JsonObject jsonObject) {
				super(jsonObject);
			}
		}

		public static final class ChangeCosmetic extends Cosmetic {
			public ChangeCosmetic(JsonObject jsonObject) {
				super(jsonObject);
			}
		}

		public static final class UseCosmetic extends Cosmetic {
			public UseCosmetic(JsonObject jsonObject) {
				super(jsonObject);
			}
		}


	}

	public static final class Out {
		private Out() {
		}
	}

	public static abstract class Auth extends SocketEvent {

		private Auth(JsonObject jsonObject) {
			super(jsonObject);
		}
	}

	public static abstract class Cosmetic extends SocketEvent {
		public final String player;
		private Cosmetic(JsonObject jsonObject) {
			super(jsonObject);
			this.player = jsonObject.get("playerID").getAsString();
		}
	}


	public static abstract class Group extends SocketEvent {
		private Group(JsonObject jsonObject) {
			super(jsonObject);
		}
	}

}
