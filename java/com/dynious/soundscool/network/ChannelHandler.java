package com.dynious.soundscool.network;

import com.dynious.soundscool.network.packet.CheckPresencePacket;
import com.dynious.soundscool.network.packet.IPacket;
import com.dynious.soundscool.network.packet.SoundPacket;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ChannelHandler extends FMLIndexedMessageToMessageCodec<IPacket>
{
    public enum Packets {
        SOUND,
        CHECKPRESENCE
    }

    public ChannelHandler() {
        addDiscriminator(Packets.SOUND.ordinal(), SoundPacket.class);
        addDiscriminator(Packets.CHECKPRESENCE.ordinal(), CheckPresencePacket.class);
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, IPacket packet, ByteBuf data) throws Exception {
        packet.writeBytes(data);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf data, IPacket packet) {
        packet.readBytes(data);
    }
}
