package me.stevemmmmm.server.game.enchants;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;

import me.stevemmmmm.server.game.enchants.templates.EventTemplate;
import me.stevemmmmm.server.game.managers.DamageManager;
import me.stevemmmmm.server.game.utils.LoreBuilder;

public class BeatTheSpammers extends CustomEnchant {
    private EnchantProperty<Float> damageAmount = new EnchantProperty<>(.10f, .25f, .40f);
    private DamageManager manager;

    public BeatTheSpammers(DamageManager manager, EventTemplate... templates) {
        super(templates);

        this.manager = manager;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        runEventTemplates(this, event.getDamager(), event.getEntity(), PlayerInventory::getItemInMainHand,
                level -> executeEnchant((Player) event.getDamager(), level, event));
    }

    public void executeEnchant(Player player, int level, EntityDamageByEntityEvent event) {
        // if (((Player) args[0]).getInventory().getItemInHand().getType() ==
        // Material.BOW) {
        // manager.addDamage(((EntityDamageByEntityEvent) args[1]),
        // damageAmount.getValueAtLevel(level), CalculationMode.ADDITIVE);

    }

    @Override
    public String getName() {
        return "Beat the Spammers";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Beatthespammers";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("+10%", "+25%", "+40%").write("Deal ")
                .writeVariable(ChatColor.RED, 0, level).write(" damage vs. players").next().write("holding a bow")
                .build();
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        return EnchantGroup.A;
    }

    @Override
    public boolean isRareEnchant() {
        return false;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.GOLDEN_SWORD };
    }
}
