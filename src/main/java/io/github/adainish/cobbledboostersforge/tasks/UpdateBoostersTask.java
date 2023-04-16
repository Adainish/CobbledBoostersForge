package io.github.adainish.cobbledboostersforge.tasks;

import io.github.adainish.cobbledboostersforge.CobbledBoostersForge;
import io.github.adainish.cobbledboostersforge.storage.BoosterStorage;

public class UpdateBoostersTask implements Runnable {
    @Override
    public void run() {
        if (CobbledBoostersForge.boosterStorage != null) {
            if (CobbledBoostersForge.boosterStorage.boosterManager != null) {
                BoosterStorage storage = CobbledBoostersForge.boosterStorage;
                storage.boosterManager.clearExpiredBoosters();
                storage.save();
                CobbledBoostersForge.boosterStorage = storage;
            }
        }
    }
}
