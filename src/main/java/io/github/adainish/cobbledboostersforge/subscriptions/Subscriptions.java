package io.github.adainish.cobbledboostersforge.subscriptions;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import io.github.adainish.cobbledboostersforge.CobbledBoostersForge;
import io.github.adainish.cobbledboostersforge.data.Booster;
import io.github.adainish.cobbledboostersforge.enumerations.BoosterType;
import kotlin.Unit;

public class Subscriptions
{
    public Subscriptions()
    {
        subscribeToCaptured();
    }

    //spawn checker for IVS and Shiny
    public void subscribeToCaptured()
    {
        CobblemonEvents.POKEMON_CAPTURED.subscribe(Priority.NORMAL, event -> {

            //check if shiny booster active -> {boost shiny if check succeeds}
            if (CobbledBoostersForge.boosterStorage.boosterManager.activeBoosters.containsKey(BoosterType.Shiny.name())) {
                Booster booster = CobbledBoostersForge.boosterStorage.boosterManager.activeBoosters.get(BoosterType.Shiny.name());
                if (booster.expired()) {
                    return Unit.INSTANCE;
                }
                if (booster.shouldBoost(BoosterType.Shiny)) {
                    event.component1().setShiny(true);
                }
            }
            //check if ivs booster active -> {boost if ivs check succeeds}
            if (CobbledBoostersForge.boosterStorage.boosterManager.activeBoosters.containsKey(BoosterType.Ivs.name()))
            {
                Booster booster = CobbledBoostersForge.boosterStorage.boosterManager.activeBoosters.get(BoosterType.Ivs.name());
                if (booster.expired())
                    return Unit.INSTANCE;
                booster.boostPokemonIVS(event.component1());
            }
            return Unit.INSTANCE;
        });
    }
}
