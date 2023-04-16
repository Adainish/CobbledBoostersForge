package io.github.adainish.cobbledboostersforge.listener;

import io.github.adainish.cobbledboostersforge.data.Player;
import io.github.adainish.cobbledboostersforge.storage.PlayerStorage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerListener
{
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() == null) {
            return;
        }

        Player player = PlayerStorage.getPlayer(event.getEntity().getUUID());
        if (player == null) {
            PlayerStorage.makePlayer((ServerPlayer) event.getEntity());
            player = PlayerStorage.getPlayer(event.getEntity().getUUID());
        }

        if (player != null) {
            player.updateCache();
        }
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() != null) {
            Player player = PlayerStorage.getPlayer(event.getEntity().getUUID());
            if (player != null) {
                player.save();
            }
        }
    }
}
