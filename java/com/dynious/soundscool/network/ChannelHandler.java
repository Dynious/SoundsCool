package com.dynious.soundscool.network;

import com.dynious.soundscool.network.packet.*;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ChannelHandler extends FMLIndexedMessageToMessageCodec<IPacket>
{
    public enum Packets {
        CLIENTSOUND,
        SERVERSOUND,
        CHECKPRESENCE,
        SOUNDNOTFOUND,
        OPENGUI,
        GETUPLOADEDSOUNDS,
        UPLOADEDSOUNDS
    }

    public ChannelHandler() {
        addDiscriminator(Packets.CLIENTSOUND.ordinal(), ClientSoundPacket.class);
        addDiscriminator(Packets.SERVERSOUND.ordinal(), ServerSoundPacket.class);
        addDiscriminator(Packets.CHECKPRESENCE.ordinal(), CheckPresencePacket.class);
        addDiscriminator(Packets.SOUNDNOTFOUND.ordinal(), SoundNotFoundPacket.class);
        addDiscriminator(Packets.OPENGUI.ordinal(), OpenGUIPacket.class);
        addDiscriminator(Packets.GETUPLOADEDSOUNDS.ordinal(), GetUploadedSoundsPacket.class);
        addDiscriminator(Packets.UPLOADEDSOUNDS.ordinal(), UploadedSoundsPacket.class);
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
