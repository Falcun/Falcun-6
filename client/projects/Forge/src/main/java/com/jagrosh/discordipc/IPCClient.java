package com.jagrosh.discordipc;

import org.slf4j.LoggerFactory;
import java.lang.management.ManagementFactory;
import org.json.JSONException;
import com.jagrosh.discordipc.entities.User;
import java.io.IOException;
import com.jagrosh.discordipc.entities.pipe.PipeStatus;
import org.json.JSONObject;
import com.jagrosh.discordipc.entities.Packet;
import com.jagrosh.discordipc.entities.Packet.OpCode;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import com.jagrosh.discordipc.entities.DiscordBuild;
import com.jagrosh.discordipc.entities.pipe.Pipe;
import com.jagrosh.discordipc.entities.Callback;
import java.util.HashMap;
import org.slf4j.Logger;
import java.io.Closeable;

public final class IPCClient implements Closeable
{
	private static final Logger LOGGER;
	private final long clientId;
	private final HashMap<String, Callback> callbacks;
	private volatile Pipe pipe;
	private IPCListener listener;
	private Thread readThread;

	public IPCClient(final long clientId) {
		this.callbacks = new HashMap<String, Callback>();
		this.listener = null;
		this.readThread = null;
		this.clientId = clientId;
	}

	public void setListener(final IPCListener listener) {
		this.listener = listener;
		if (this.pipe != null) {
			this.pipe.setListener(listener);
		}
	}

	public void connect(final DiscordBuild... preferredOrder) throws NoDiscordClientException {
		this.checkConnected(false);
		this.callbacks.clear();
		this.pipe = null;
		this.pipe = Pipe.openPipe(this, this.clientId, this.callbacks, preferredOrder);
		IPCClient.LOGGER.debug("Client is now connected and ready!");
		if (this.listener != null) {
			this.listener.onReady(this);
		}
		this.startReading();
	}

	public void sendRichPresence(final RichPresence presence) {
		this.sendRichPresence(presence, null);
	}

	public void sendRichPresence(final RichPresence presence, final Callback callback) {
		this.checkConnected(true);
		IPCClient.LOGGER.debug("Sending RichPresence to discord: " + ((presence == null) ? null : presence.toJson().toString()));
		this.pipe.send(Packet.OpCode.FRAME, new JSONObject().put("cmd", "SET_ACTIVITY").put("args", new JSONObject().put("pid", getPID()).put("activity", (presence == null) ? null : presence.toJson())), callback);
	}

	public void subscribe(final Event sub) {
		this.subscribe(sub, null);
	}

	public void subscribe(final Event sub, final Callback callback) {
		this.checkConnected(true);
		if (!sub.isSubscribable()) {
			throw new IllegalStateException("Cannot subscribe to " + sub + " event!");
		}
		IPCClient.LOGGER.debug(String.format("Subscribing to Event: %s", sub.name()));
		this.pipe.send(Packet.OpCode.FRAME, new JSONObject().put("cmd", "SUBSCRIBE").put("evt", sub.name()), callback);
	}

	public PipeStatus getStatus() {
		if (this.pipe == null) {
			return PipeStatus.UNINITIALIZED;
		}
		return this.pipe.getStatus();
	}

	@Override
	public void close() {
		this.checkConnected(true);
		try {
			this.pipe.close();
		}
		catch (IOException e) {
			IPCClient.LOGGER.debug("Failed to close pipe", e);
		}
	}

	public DiscordBuild getDiscordBuild() {
		if (this.pipe == null) {
			return null;
		}
		return this.pipe.getDiscordBuild();
	}

	private void checkConnected(final boolean connected) {
		if (connected && this.getStatus() != PipeStatus.CONNECTED) {
			throw new IllegalStateException(String.format("IPCClient (ID: %d) is not connected!", this.clientId));
		}
		if (!connected && this.getStatus() == PipeStatus.CONNECTED) {
			throw new IllegalStateException(String.format("IPCClient (ID: %d) is already connected!", this.clientId));
		}
	}

	private void startReading()
	{
		readThread = new Thread() {
			public void run() {
				try
				{
					Packet p;
					while((p = pipe.read()).getOp() != OpCode.CLOSE)
					{
						JSONObject json = p.getJson();
						Event event = Event.of(json.optString("evt", null));
						String nonce = json.optString("nonce", null);
						switch(event)
						{
						case NULL:
							if(nonce != null && callbacks.containsKey(nonce))
								callbacks.remove(nonce).succeed(p);
							break;

						case ERROR:
							if(nonce != null && callbacks.containsKey(nonce))
								callbacks.remove(nonce).fail(json.getJSONObject("data").optString("message", null));
							break;

						case ACTIVITY_JOIN:
							LOGGER.debug("Reading thread received a 'join' event.");
							break;

						case ACTIVITY_SPECTATE:
							LOGGER.debug("Reading thread received a 'spectate' event.");
							break;

						case ACTIVITY_JOIN_REQUEST:
							LOGGER.debug("Reading thread received a 'join request' event.");
							break;

						case UNKNOWN:
							LOGGER.debug("Reading thread encountered an event with an unknown type: " +
									json.getString("evt"));
							break;
						}
						if(listener != null && json.has("cmd") && json.getString("cmd").equals("DISPATCH"))
						{
							try
							{
								JSONObject data = json.getJSONObject("data");
								switch(Event.of(json.getString("evt")))
								{
								case ACTIVITY_JOIN:
									//listener.onActivityJoin(this, data.getString("secret"));
									break;

								case ACTIVITY_SPECTATE:
									//listener.onActivitySpectate(this, data.getString("secret"));
									break;

								case ACTIVITY_JOIN_REQUEST:
									JSONObject u = data.getJSONObject("user");
									User user = new User(
											u.getString("username"),
											u.getString("discriminator"),
											Long.parseLong(u.getString("id")),
											u.optString("avatar", null)
											);
									//listener.onActivityJoinRequest(this, data.optString("secret", null), user);
									break;
								}
							}
							catch(Exception e)
							{
								LOGGER.error("Exception when handling event: ", e);
							}
						}
					}
					pipe.setStatus(PipeStatus.DISCONNECTED);
//					if(listener != null)
//						//listener.onClose(this, p.getJson());
				}
				catch(Exception ex)
				{
					if(ex instanceof IOException)
						LOGGER.error("Reading thread encountered an IOException", ex);
					else
						LOGGER.error("Reading thread encountered an JSONException", ex);

					pipe.setStatus(PipeStatus.DISCONNECTED);
//					if(listener != null)
//						//listener.onDisconnect(this, ex);
				}
			}
		};
		
		LOGGER.debug("Starting IPCClient reading thread!");
		readThread.start();
	}

	private static int getPID() {
		final String pr = ManagementFactory.getRuntimeMXBean().getName();
		return Integer.parseInt(pr.substring(0, pr.indexOf(64)));
	}

	static {
		LOGGER = LoggerFactory.getLogger(IPCClient.class);
	}

	public enum Event
	{
		NULL(false), 
		READY(false), 
		ERROR(false), 
		ACTIVITY_JOIN(true), 
		ACTIVITY_SPECTATE(true), 
		ACTIVITY_JOIN_REQUEST(true), 
		UNKNOWN(false);

		private final boolean subscribable;

		private Event(final boolean subscribable) {
			this.subscribable = subscribable;
		}

		public boolean isSubscribable() {
			return this.subscribable;
		}

		static Event of(final String str) {
			if (str == null) {
				return Event.NULL;
			}
			for (final Event s : values()) {
				if (s != Event.UNKNOWN && s.name().equalsIgnoreCase(str)) {
					return s;
				}
			}
			return Event.UNKNOWN;
		}
	}
}
