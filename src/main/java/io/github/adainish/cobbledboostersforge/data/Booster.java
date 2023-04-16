package io.github.adainish.cobbledboostersforge.data;

import com.cobblemon.mod.common.pokemon.Pokemon;
import io.github.adainish.cobbledboostersforge.enumerations.BoosterType;
import io.github.adainish.cobbledboostersforge.util.Util;

import java.util.concurrent.TimeUnit;

public class Booster
{
    public BoosterType boosterType;
    public double boostPercentage = 0;
    public long timerMinutes = 0;
    public long startedAt;

    public Booster()
    {

    }

    public double getRandomChance() {
        return Math.floor(Math.random() * 100) + 1;
    }

    public boolean shouldBoost(BoosterType boosterType)
    {
        if (this.boosterType != boosterType)
            return false;
        return getRandomChance() <= boostPercentage;
    }

    public long getLastsUntil()
    {
        return startedAt + (TimeUnit.MINUTES.toMillis(timerMinutes));
    }

    public boolean expired()
    {
        return System.currentTimeMillis() >= getLastsUntil();
    }

    public long getTimeLeftInLong()
    {
        long currentTime = System.currentTimeMillis();
        return getLastsUntil() - currentTime;
    }

    public String timeLeftInHoursMinutesFromString() {
        long currentTime = System.currentTimeMillis();
        long cd = getLastsUntil() - currentTime;
        long hours = cd / Util.HOUR_IN_MILLIS;
        cd = cd - (hours * Util.HOUR_IN_MILLIS);
        long minutes = cd / Util.MINUTE_IN_MILLIS;
        cd = cd - (minutes * Util.MINUTE_IN_MILLIS);
        long seconds = cd / Util.SECOND_IN_MILLIS;
        return hours + " Hours " + minutes + " Minutes " + seconds + " Seconds";
    }

    public void boostPokemonIVS(Pokemon pokemon)
    {

    }

    public void sendExpiredBroadCast()
    {

    }

    public void sendActivatedMessage()
    {

    }
}
