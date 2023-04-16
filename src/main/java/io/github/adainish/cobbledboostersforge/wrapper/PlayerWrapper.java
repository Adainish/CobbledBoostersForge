package io.github.adainish.cobbledboostersforge.wrapper;

import io.github.adainish.cobbledboostersforge.data.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerWrapper
{
    public HashMap<UUID, Player> playerCache = new HashMap<>();

    public PlayerWrapper()
    {

    }
}
