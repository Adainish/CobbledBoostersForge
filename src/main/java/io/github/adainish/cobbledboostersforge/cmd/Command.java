package io.github.adainish.cobbledboostersforge.cmd;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.adainish.cobbledboostersforge.CobbledBoostersForge;
import io.github.adainish.cobbledboostersforge.data.Booster;
import io.github.adainish.cobbledboostersforge.data.Player;
import io.github.adainish.cobbledboostersforge.storage.BoosterStorage;
import io.github.adainish.cobbledboostersforge.storage.PlayerStorage;
import io.github.adainish.cobbledboostersforge.util.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;

public class Command
{
    public static LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("boosters")
                .executes(cc -> {
                    try {
                        Player player = PlayerStorage.getPlayer(cc.getSource().getPlayerOrException().getUUID());
                        if (player != null)
                            player.openBoostersMenu(cc.getSource().getPlayer());
                        else
                            cc.getSource().sendFailure(Component.literal(Util.formattedString("&cSomething went wrong while retrieving your player data...")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return 1;
                })
                .then(Commands.literal("givebooster")
                        .requires(commandSourceStack -> commandSourceStack.hasPermission(4))
                        .executes(cc -> {
                            Util.send(cc.getSource(), "&cPlease provide a valid booster type, percentage, and minutes");
                            return 1;
                        })
                        .then(Commands.argument("type", StringArgumentType.string())
                                .executes(cc -> {
                                    Util.send(cc.getSource(), "&cPlease provide a valid booster percentage, and minutes");
                                    return 1;
                                }) .then(Commands.argument("percent", DoubleArgumentType.doubleArg(1))
                                        .executes(cc -> {
                                            Util.send(cc.getSource(), "&cPlease provide a valid booster timer in minutes");
                                            return 1;
                                        }).then(Commands.argument("minutes", IntegerArgumentType.integer(1))
                                                .executes(cc -> {
                                                    String type = StringArgumentType.getString(cc, "type");
                                                    double percent = DoubleArgumentType.getDouble(cc, "percent");
                                                    int minutes = IntegerArgumentType.getInteger(cc, "minutes");
                                                    if (Util.isValidBoosterType(type))
                                                    {
                                                        try {
                                                            BoosterStorage boosterStorage = CobbledBoostersForge.boosterStorage;
                                                            if (boosterStorage.boosterManager.startNewBooster(Util.getBoosterTypeFromString(type), percent, minutes)) {
                                                                boosterStorage.save();
                                                                CobbledBoostersForge.boosterStorage = boosterStorage;
                                                            }
                                                        } catch (Exception e)
                                                        {
                                                            Util.send(cc.getSource(), e.getMessage());
                                                        }
                                                    }
                                                    return 1;
                                                }) .then(Commands.argument("player", EntityArgument.player())
                                                        .executes(cc -> {
                                                            String type = StringArgumentType.getString(cc, "type");
                                                            double percent = DoubleArgumentType.getDouble(cc, "percent");
                                                            int minutes = IntegerArgumentType.getInteger(cc, "minutes");
                                                            if (Util.isValidBoosterType(type))
                                                            {
                                                                try {
                                                                    Player player = PlayerStorage.getPlayer(EntityArgument.getPlayer(cc, "player").getUUID());
                                                                    if (player != null) {
                                                                        Booster booster = new Booster();
                                                                        booster.setBoosterType(type);
                                                                        booster.setBoostPercentage(percent);
                                                                        booster.setTimerMinutes(minutes);
                                                                        player.boosterList.add(booster);
                                                                        player.save();
                                                                    }
                                                                } catch (Exception e)
                                                                {
                                                                    Util.send(cc.getSource(), e.getMessage());
                                                                }
                                                            }
                                                            return 1;
                                                        })
                                                )
                                        )
                                )
                        )
                )
                ;
    }
}
