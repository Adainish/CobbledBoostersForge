package io.github.adainish.cobbledboostersforge.data;

import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.Pokemon;
import io.github.adainish.cobbledboostersforge.enumerations.BoosterType;
import io.github.adainish.cobbledboostersforge.util.RandomHelper;
import io.github.adainish.cobbledboostersforge.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Booster
{
    private String boosterType;
    private double boostPercentage = 0;
    private long timerMinutes = 0;
    private long startedAt;

    public Booster()
    {

    }

    public double getRandomChance() {
        return Math.floor(Math.random() * 100) + 1;
    }

    public boolean shouldBoost(BoosterType boosterType)
    {
        if (!this.getBoosterType().equalsIgnoreCase(boosterType.name())) {
            return false;
        }
        return getRandomChance() <= getBoostPercentage();
    }

    public long getLastsUntil()
    {
        return getStartedAt() + (TimeUnit.MINUTES.toMillis(getTimerMinutes()));
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

    public double getPercentage(int decimalPlaces, Pokemon pokemon) {
        int total = 0;

        for (Stats st:Stats.values()) {
            switch (st)
            {
                case ACCURACY, EVASION -> {
                    continue;
                }
            }
            total += pokemon.getIvs().get(st);
        }

        double percentage = (double)total / 186.0 * 100.0;
        return Math.floor(percentage * Math.pow(10.0, (double)decimalPlaces)) / Math.pow(10.0, (double)decimalPlaces);
    }

    public Stats getRandomStatType() {
        List<Stats> battleStatsTypes = new ArrayList<>(Arrays.asList(Stats.values()));
        battleStatsTypes.remove(Stats.ACCURACY);
        battleStatsTypes.remove(Stats.EVASION);
        return RandomHelper.getRandomElementFromCollection(battleStatsTypes);
    }

    public void boostPokemonIVS(Pokemon pokemon)
    {
        if (pokemon != null) {
            double buffPercent = boostPercentage;
            double pokemonPercent = getPercentage(1, pokemon);
            double leftOver = 100 - pokemonPercent;
            for (int i = 0; i < leftOver; i++) {
                if (i >= buffPercent)
                    break;
                Stats statsType = getRandomStatType();
                if (pokemon.getIvs().getOrDefault(statsType) >= 31)
                    continue;
                pokemon.getIvs().set(statsType, pokemon.getIvs().getOrDefault(statsType) + 1);
            }
        }
    }

    public void sendExpiredBroadCast()
    {
     Util.doBroadcast("&cThe %amount%x %type% has expired..."
             .replace("%amount%", String.valueOf(getBoostPercentage()))
             .replace("%type%", getBoosterType()));
    }

    public void sendActivatedMessage()
    {
        Util.doBroadcast("&aA %amount%x %type% Booster has started that lasts for %timer%"
                .replace("%amount%", String.valueOf(getBoostPercentage()))
                .replace("%type%", getBoosterType())
                .replace("%timer%", timeLeftInHoursMinutesFromString())
        );
    }

    public String getBoosterType() {
        return boosterType;
    }

    public void setBoosterType(String boosterType) {
        this.boosterType = boosterType;
    }

    public double getBoostPercentage() {
        return boostPercentage;
    }

    public void setBoostPercentage(double boostPercentage) {
        this.boostPercentage = boostPercentage;
    }

    public long getTimerMinutes() {
        return timerMinutes;
    }

    public void setTimerMinutes(long timerMinutes) {
        this.timerMinutes = timerMinutes;
    }

    public long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(long startedAt) {
        this.startedAt = startedAt;
    }
}
