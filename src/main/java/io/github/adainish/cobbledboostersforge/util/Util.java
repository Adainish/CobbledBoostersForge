package io.github.adainish.cobbledboostersforge.util;

import io.github.adainish.cobbledboostersforge.CobbledBoostersForge;
import io.github.adainish.cobbledboostersforge.enumerations.BoosterType;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Util
{

    public static String formattedString(String s) {
        return s.replaceAll("&", "§");
    }

    public static List<String> formattedArrayList(List<String> list) {

        List<String> formattedList = new ArrayList<>();
        for (String s : list) {
            formattedList.add(formattedString(s));
        }

        return formattedList;
    }

    public static void send(CommandSourceStack sender, String message) {
        sender.sendSystemMessage(Component.literal(((TextUtil.getMessagePrefix()).getString() + message).replaceAll("&([0-9a-fk-or])", "\u00a7$1")));
    }

    public static boolean isValidBoosterType(String st)
    {
        for (BoosterType bt: BoosterType.values()) {
            if (bt.name().equalsIgnoreCase(st))
                return true;
        }
        return false;
    }

    public static BoosterType getBoosterTypeFromString(String st) throws Exception
    {
        for (BoosterType bt:BoosterType.values()) {
            if (bt.name().equalsIgnoreCase(st))
                return bt;
        }
        throw new Exception("Invalid booster type");
    }
    public static ServerPlayer getPlayer(UUID uuid) {
        return CobbledBoostersForge.getServer().getPlayerList().getPlayer(uuid);
    }

    public static void send(UUID uuid, String message) {
        if (message == null)
            return;
        if (message.isEmpty())
            return;
        ServerPlayer player = getPlayer(uuid);
        if (player != null)
            player.sendSystemMessage(Component.literal(((TextUtil.getMessagePrefix()).getString() + message).replaceAll("&([0-9a-fk-or])", "\u00a7$1")));
    }

    public static void doBroadcast(String message) {
        CobbledBoostersForge.getServer().getPlayerList().getPlayers().forEach(serverPlayerEntity -> {
            serverPlayerEntity.sendSystemMessage(Component.literal( formattedString(TextUtil.getMessagePrefix().getString() + message)));
        });
    }

    public static final long DAY_IN_TICKS = 1728000;
    public static final long DAY_IN_MILLIS = 86400000;
    public static final long HOUR_IN_MILLIS = 3600000;
    public static final long MINUTE_IN_MILLIS = 60000;
    public static final long SECOND_IN_MILLIS = 1000;
    public static final long HALF_AN_HOUR_IN_MILLIS = 1800000;
}
