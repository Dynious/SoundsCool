package com.dynious.soundscool.command;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.helper.NetworkHelper;
import com.dynious.soundscool.lib.Commands;
import com.dynious.soundscool.network.packet.server.OpenGUIPacket;
import com.dynious.soundscool.network.packet.server.SoundNotFoundPacket;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class CommandSoundsCool extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return Commands.SOUNDS;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender commandSender)
    {
        return true;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List addTabCompletionOptions(ICommandSender commandSender, String[] args)
    {
        return null;
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] args)
    {
        if (commandSender instanceof EntityPlayer)
        {
            NetworkHelper.sendPacketToPlayer(new OpenGUIPacket(0), (EntityPlayer) commandSender);
        }
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return null;
    }

    @Override
    public int compareTo(Object obj)
    {
        if (obj instanceof ICommand)
        {
            return this.compareTo((ICommand) obj);
        }
        else
        {
            return 0;
        }
    }
}
