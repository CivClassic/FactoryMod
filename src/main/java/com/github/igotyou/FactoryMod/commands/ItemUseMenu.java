package com.github.igotyou.FactoryMod.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.igotyou.FactoryMod.utility.ItemUseGUI;

import vg.civcraft.mc.civmodcore.command.CivCommand;
import vg.civcraft.mc.civmodcore.command.StandaloneCommand;

@CivCommand(id = "item")
public class ItemUseMenu extends StandaloneCommand {
	@Override
	public boolean execute(CommandSender sender, String[] args) {
		Player p = (Player) sender;
		if (args.length == 0 || args[0].length() == 0) {
			ItemUseGUI gui = new ItemUseGUI((Player) sender);
			gui.showItemOverview(p.getInventory().getItemInMainHand());
		} else {
			String concat = String.join(" ", args);
			Material mat = Material.getMaterial(concat);
			if (mat == null) {
				p.sendMessage(ChatColor.RED + "The item " + concat + " does not exist");
				return true;
			}
			ItemUseGUI gui = new ItemUseGUI((Player) sender);
			gui.showItemOverview(new ItemStack(mat));
		}
		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		return doTabComplete(args[0], Arrays.asList(Material.values()), Material::name, false);
	}
}
