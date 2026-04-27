package me.elix.durability;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class DurabilityPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    private void updateLore(ItemStack item) {
        if (item == null) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        // La correction de l'erreur est sur la ligne ci-dessous ( != 0 )
        if (item.getType().getMaxDurability() != 0) {
            int max = item.getType().getMaxDurability();
            int used = item.getDurability();
            int current = max - used;
            int percent = (int) ((current * 100.0) / max);

            List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();

            lore.removeIf(line -> line.startsWith("Durability:"));

            lore.add("Durability: " + current + " / " + max + " (" + percent + "%)");

            meta.setLore(lore);
            item.setItemMeta(meta);
        }
    }

    @EventHandler
    public void onDamage(PlayerItemDamageEvent e) {
        updateLore(e.getItem());
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() != null) {
            updateLore(e.getCurrentItem());
        }
    }
}
