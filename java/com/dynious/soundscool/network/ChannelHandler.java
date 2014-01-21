package com.dynious.soundscool.network;

import com.dynious.soundscool.network.packet.IPacket;
import com.dynious.soundscool.network.packet.SoundChunkPacket;
import com.dynious.soundscool.network.packet.SoundUploadedPacket;
import com.dynious.soundscool.network.packet.client.*;
import com.dynious.soundscool.network.packet.server.*;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ChannelHandler extends FMLIndexedMessageToMessageCodec<IPacket>
{
    public enum Packets {
        SOUND_CHUNK,
        SOUND_UPLOADED,
        CHECK_PRESENCE,
        SOUND_NOT_FOUND,
        OPEN_GUI,
        GET_UPLOADED_SOUNDS,
        UPLOADED_SOUNDS,
        CLIENT_PLAY_SOUND,
        SERVER_PLAY_SOUND,
        SOUND_PLAYER_PLAY,
        SOUND_PLAYER_SELECT,
        REMOVE_SOUND,
        SOUND_RECEIVED,
        SOUND_REMOVED,
        STOP_SOUND
    }

    public ChannelHandler() {
        addDiscriminator(Packets.SOUND_CHUNK.ordinal(), SoundChunkPacket.class);
        addDiscriminator(Packets.SOUND_UPLOADED.ordinal(), SoundUploadedPacket.class);
        addDiscriminator(Packets.CHECK_PRESENCE.ordinal(), CheckPresencePacket.class);
        addDiscriminator(Packets.SOUND_NOT_FOUND.ordinal(), SoundNotFoundPacket.class);
        addDiscriminator(Packets.OPEN_GUI.ordinal(), OpenGUIPacket.class);
        addDiscriminator(Packets.GET_UPLOADED_SOUNDS.ordinal(), GetUploadedSoundsPacket.class);
        addDiscriminator(Packets.UPLOADED_SOUNDS.ordinal(), UploadedSoundsPacket.class);
        addDiscriminator(Packets.CLIENT_PLAY_SOUND.ordinal(), ClientPlaySoundPacket.class);
        addDiscriminator(Packets.SERVER_PLAY_SOUND.ordinal(), ServerPlaySoundPacket.class);
        addDiscriminator(Packets.SOUND_PLAYER_PLAY.ordinal(), SoundPlayerPlayPacket.class);
        addDiscriminator(Packets.SOUND_PLAYER_SELECT.ordinal(), SoundPlayerSelectPacket.class);
        addDiscriminator(Packets.REMOVE_SOUND.ordinal(), RemoveSoundPacket.class);
        addDiscriminator(Packets.SOUND_RECEIVED.ordinal(), SoundReceivedPacket.class);
        addDiscriminator(Packets.SOUND_REMOVED.ordinal(), SoundRemovedPacket.class);
        addDiscriminator(Packets.STOP_SOUND.ordinal(), StopSoundPacket.class);
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
