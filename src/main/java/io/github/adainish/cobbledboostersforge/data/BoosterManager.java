package io.github.adainish.cobbledboostersforge.data;

import io.github.adainish.cobbledboostersforge.enumerations.BoosterType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BoosterManager
{
    public HashMap<String, Booster> activeBoosters = new HashMap<>();

    public BoosterManager()
    {

    }

    public void clearExpiredBoosters()
    {
        List<String> expired = new ArrayList<>();
        activeBoosters.forEach((s, booster) -> {
            if (booster.expired()) {
                booster.sendExpiredBroadCast();
                expired.add(s);
            }
        });
        expired.forEach(s -> activeBoosters.remove(s));
    }

    public String getBoosterStatus(String type)
    {
        if (activeBoosters.containsKey(type))
            return "&4A Booster for this type is already active";
        else return "&aA Booster can be activated for this type";
    }

    public boolean startNewBooster(Booster booster) throws Exception
    {
        if (booster == null)
            throw new Exception("Error loading booster");

        if (activeBoosters.containsKey(booster.getStartedAt())) {
            throw new Exception("A Booster of this type is already active");
        }
        if (booster.getBoostPercentage() <= 0) {
            throw new Exception("Percentage was less than or equal to 0, please provide a positive number");
        }
        if (booster.getBoostPercentage() <= 0) {
            throw new Exception("The timer must be higher than 0 minutes");
        }
        booster.setStartedAt(System.currentTimeMillis());
        booster.sendActivatedMessage();
        activeBoosters.put(booster.getBoosterType(), booster);
        return true;
    }
    public boolean startNewBooster(BoosterType boosterType, double percentage, long timerMinutes) throws Exception
    {
        if (activeBoosters.containsKey(boosterType.name())) {
            throw new Exception("A Booster of this type is already active");
        }
        Booster booster = new Booster();
        if (percentage <= 0) {
            throw new Exception("Percentage was less than or equal to 0, please provide a positive number");
        }
        if (timerMinutes <= 0) {
            throw new Exception("The timer must be higher than 0 minutes");
        }
        booster.setBoosterType(boosterType.name());
        booster.setBoostPercentage(percentage);
        booster.setTimerMinutes(timerMinutes);
        booster.setStartedAt(System.currentTimeMillis());
        booster.sendActivatedMessage();
        activeBoosters.put(booster.getBoosterType(), booster);
        return true;
    }


}
