package net.mattbenson.utils.legacy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.mattbenson.modules.types.render.Combo;
import net.minecraft.network.play.server.S19PacketEntityStatus;

public class ComboPacketHandler extends ChannelInboundHandlerAdapter {
	private final Combo comboDisplayMod;

	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof S19PacketEntityStatus) {
			this.comboDisplayMod.onEntityStatusPacket((S19PacketEntityStatus) msg);
		}

		super.channelRead(ctx, msg);
	}

	public ComboPacketHandler(Combo comboDisplayMod) {
		this.comboDisplayMod = comboDisplayMod;
	}
}