package io.github.adainish.cobbledboostersforge.data;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.button.linked.LinkType;
import ca.landonjw.gooeylibs2.api.button.linked.LinkedPageButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import io.github.adainish.cobbledboostersforge.CobbledBoostersForge;
import io.github.adainish.cobbledboostersforge.enumerations.BoosterType;
import io.github.adainish.cobbledboostersforge.storage.PlayerStorage;
import io.github.adainish.cobbledboostersforge.util.Util;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.*;

public class Player
{
    public UUID uuid;
    public List<Booster> boosterList;

    public Player(UUID uuid)
    {
        this.uuid = uuid;
        this.boosterList = new ArrayList<>();
    }


    public void save()
    {
        PlayerStorage.savePlayer(this);
    }

    public void updateCache()
    {
        CobbledBoostersForge.playerWrapper.playerCache.put(uuid, this);
    }

    public void openBoostersMenu(ServerPlayer player)
    {
        UIManager.openUIForcefully(player, mainPage());
    }

    //booster list by length and percentage of type
    public List<Booster> sortedBoostersOfTypeLength(BoosterType boosterType)
    {
        List<Booster> boosterList = new ArrayList<>();
        if (!this.boosterList.isEmpty())
        {
            for (Booster b:this.boosterList) {
                if (b.getBoosterType().equalsIgnoreCase(boosterType.name()))
                {
                    boosterList.add(b);
                }
            }
        }
        boosterList.sort(Comparator.comparing(Booster::getTimerMinutes).thenComparing(Booster::getBoostPercentage));
        return boosterList;
    }
    //boosters by length
    public List<Booster> sortedBoostersByLength()
    {
        List<Booster> boosterList = new ArrayList<>();
        if (!this.boosterList.isEmpty())
        {
            boosterList.addAll(this.boosterList);
        }
        boosterList.sort(Comparator.comparing(Booster::getBoosterType).thenComparing(Booster::getTimerMinutes).thenComparing(Booster::getBoostPercentage));
        return boosterList;
    }

    //boosters
    public List<Button> boosterButtonList()
    {
        List<Button> buttons = new ArrayList<>();
        for (Booster b:sortedBoostersByLength()) {
            GooeyButton button = GooeyButton.builder()
                    .title(Util.formattedString("&b%type% Booster".replace("%type%", b.getBoosterType())))
                    .lore(Util.formattedArrayList(Arrays.asList(
                            "%boosterstatus%".replace("%boosterstatus%", CobbledBoostersForge.boosterStorage.boosterManager.getBoosterStatus(b.getBoosterType())),
                            "&bBoosts by &e-> &6%percentage%".replace("%percentage%", String.valueOf(b.getBoostPercentage())),
                            "&bBoosts for &e-> &6%timer%".replace("%timer%", String.valueOf(b.getTimerMinutes()))
                    )))
                    .display(new ItemStack(Items.DIAMOND))
                    .onClick(buttonAction -> {
                        try {
                            if (CobbledBoostersForge.boosterStorage.boosterManager.startNewBooster(b))
                            {
                                boosterList.remove(b);
                                this.save();
                                UIManager.closeUI(buttonAction.getPlayer());
                            }
                        } catch (Exception e) {
                            Util.send(buttonAction.getPlayer().getUUID(), e.getMessage());
                        }

                    })
                    .build();
            buttons.add(button);
        }
        return buttons;
    }

    //sorted by type boosters
    public List<Button> boosterByTypeButtonList(BoosterType boosterType)
    {
        List<Button> buttons = new ArrayList<>();
        for (Booster b:sortedBoostersOfTypeLength(boosterType)) {
            GooeyButton button = GooeyButton.builder()
                    .title(Util.formattedString("&b%type% Booster".replace("%type%", b.getBoosterType())))
                    .lore(Util.formattedArrayList(Arrays.asList(
                            "%boosterstatus%".replace("%boosterstatus%", CobbledBoostersForge.boosterStorage.boosterManager.getBoosterStatus(b.getBoosterType())),
                            "&bBoosts by &e-> &6%percentage%".replace("%percentage%", String.valueOf(b.getBoostPercentage())),
                            "&bBoosts for &e-> &6%timer%".replace("%timer%", String.valueOf(b.getTimerMinutes()))
                    )))
                    .display(new ItemStack(Items.DIAMOND))
                    .onClick(buttonAction -> {
                        try {
                            if (CobbledBoostersForge.boosterStorage.boosterManager.startNewBooster(b))
                            {
                                boosterList.remove(b);
                                this.save();
                                UIManager.closeUI(buttonAction.getPlayer());
                            } else {
                                //send fail message
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    })
                    .build();
            buttons.add(button);
        }
        return buttons;
    }

    public GooeyButton filler() {
        return GooeyButton.builder()
                .display(new ItemStack(Items.GRAY_STAINED_GLASS_PANE))
                .build();
    }

    public LinkedPage mainPage()
    {
        ChestTemplate.Builder builder = ChestTemplate.builder(5);
        builder.fill(filler());
        PlaceholderButton placeHolderButton = new PlaceholderButton();
        LinkedPageButton previous = LinkedPageButton.builder()
                .display(new ItemStack(Items.SPECTRAL_ARROW))
                .title("Previous Page")
                .linkType(LinkType.Previous)
                .build();

        LinkedPageButton next = LinkedPageButton.builder()
                .display(new ItemStack(Items.SPECTRAL_ARROW))
                .title("Next Page")
                .linkType(LinkType.Next)
                .build();

        builder.set(0, 3, previous)
                .set(0, 5, next)
                .rectangle(1, 1, 3, 7, placeHolderButton);

        return PaginationHelper.createPagesFromPlaceholders(builder.build(), boosterButtonList(), LinkedPage.builder().template(builder.build()));
    }
}
