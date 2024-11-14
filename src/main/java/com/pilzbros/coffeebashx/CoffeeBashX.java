package com.pilzbros.coffeebashx;

import java.util.ArrayList;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CoffeeBashX extends JavaPlugin {
    public static Economy econ = null;

    public void onEnable() {
        if (!this.setupEconomy()) {
            this.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", this.getDescription().getName()));
            this.getServer().getPluginManager().disablePlugin(this);
        } else {
            this.crecipe();
            PluginDescriptionFile pdfFile = this.getDescription();
            this.getLogger().info(pdfFile.getName() + " " + pdfFile.getVersion() + " Has been enabled!");
        }
    }

    public void onDisable() {
        PluginDescriptionFile pdfFile = this.getDescription();
        this.getLogger().info(pdfFile.getName() + " " + pdfFile.getVersion() + " Has been Disabled!");
    }

    private void crecipe() {
        ItemStack mat = new ItemStack(Material.getMaterial(351));
        Short o = Short.valueOf((short)3);
        mat.setDurability(o);
        ItemStack coffee = new ItemStack(Material.POTION, 1);
        PotionEffect pp = new PotionEffect(PotionEffectType.SPEED, 600, 0);
        PotionEffect ppp = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 0);
        PotionEffect pppp = new PotionEffect(PotionEffectType.CONFUSION, 600, 0);
        PotionMeta p = (PotionMeta)coffee.getItemMeta();
        p.setDisplayName("§aCoffee");
        ArrayList<String> c = new ArrayList();
        c.add("§3Drink some Coffee!");
        p.setLore(c);
        coffee.setItemMeta(p);
        p.addCustomEffect(pppp, true);
        p.addCustomEffect(pp, true);
        p.addCustomEffect(ppp, true);
        coffee.setItemMeta(p);
        ShapedRecipe ccc = new ShapedRecipe(coffee);
        ccc.shape(new String[]{"   ", "BPS", "   "});
        ccc.setIngredient('B', Material.GLASS_BOTTLE);
        ccc.setIngredient('P', mat.getData());
        ccc.setIngredient('S', Material.SUGAR);
        Bukkit.getServer().addRecipe(ccc);
    }

    private boolean setupEconomy() {
        if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        } else {
            RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) {
                return false;
            } else {
                econ = (Economy)rsp.getProvider();
                return econ != null;
            }
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (cmd.getName().equalsIgnoreCase("CoffeeBashX")) {
                Double price = this.getConfig().getDouble("price");
                String sprice = price.toString();
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("buy")) {
                        if (player.hasPermission("cbx.buy")) {
                            if (econ.getBalance(player.getName()) < price) {
                                player.sendMessage("§b[CBX]§3You Can't afford coffee!");
                            } else {
                                EconomyResponse r = econ.withdrawPlayer(player.getName(), price);
                                if (r.transactionSuccess()) {
                                    ItemStack coffee = new ItemStack(Material.POTION, 1);
                                    PotionEffect pp = new PotionEffect(PotionEffectType.SPEED, 600, 0);
                                    PotionEffect ppp = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 0);
                                    PotionEffect pppp = new PotionEffect(PotionEffectType.CONFUSION, 600, 0);
                                    PotionMeta p = (PotionMeta)coffee.getItemMeta();
                                    p.setDisplayName("§aCoffee");
                                    ArrayList<String> c = new ArrayList();
                                    c.add("§3Drink some Coffee!");
                                    p.setLore(c);
                                    coffee.setItemMeta(p);
                                    p.addCustomEffect(pppp, true);
                                    p.addCustomEffect(pp, true);
                                    p.addCustomEffect(ppp, true);
                                    coffee.setItemMeta(p);
                                    player.getInventory().addItem(new ItemStack[]{coffee});
                                    player.sendMessage("§b[CBX]§3You bought some Coffee!");
                                    return true;
                                }

                                player.sendMessage("§b[CBX]§cYou can't get this Item at the moment!");
                            }
                        } else {
                            player.sendMessage("§b[CBX]§cYou do not have permission to do this!");
                        }
                    }

                    if (args[0].equalsIgnoreCase("help") && player.hasPermission("cbx.help.display")) {
                        player.sendMessage("§b[CBX]§3List of Commands:\n§3/cbx buy - Lets you buy the coffee potion for $" + sprice + "!");
                    }

                    if (args[0].equalsIgnoreCase("reload") && player.hasPermission("cbx.reload")) {
                        this.reloadConfig();
                    }
                }
            }
        } else {
            sender.sendMessage("player only plugin!");
        }

        return false;
    }
}