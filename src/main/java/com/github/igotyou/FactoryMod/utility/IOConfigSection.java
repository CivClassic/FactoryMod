package com.github.igotyou.FactoryMod.utility;

import com.github.igotyou.FactoryMod.FactoryMod;
import com.github.igotyou.FactoryMod.FactoryModPlayerSettings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import vg.civcraft.mc.civmodcore.inventory.items.ItemUtils;
import vg.civcraft.mc.civmodcore.inventorygui.Clickable;
import vg.civcraft.mc.civmodcore.inventorygui.ClickableInventory;
import vg.civcraft.mc.civmodcore.inventorygui.LClickable;
import vg.civcraft.mc.civmodcore.inventorygui.components.StaticDisplaySection;

import java.util.UUID;

/**
 * @author caucow
 */
public class IOConfigSection extends StaticDisplaySection {

	private UUID viewerId;
	private final IOSelector ioSelector;
	private final Material centerDisplay;
	private final BlockFace front;
	private final Block centerBlock;
	private FactoryModPlayerSettings.IoConfigDirectionMode ioDirectionMode;

	public IOConfigSection(Player viewer, IOSelector ioSelector, Material centerDisplay, Block centerBlock, BlockFace front) {
		super(9);
		this.viewerId = viewer.getUniqueId();
		this.ioSelector = ioSelector;
		this.centerDisplay = centerDisplay;
		this.centerBlock = centerBlock;
		this.front = front;

		rebuild();
	}

	private Clickable getCycleButton(BlockFace dir) {
		if (!ioSelector.hasInputs() && !ioSelector.hasOutputs()) {
			// TODO short/optimized default UI (?)
		}
		Block relativeBlock = centerBlock.getRelative(dir);
		Material type = relativeBlock.getType();
		DirectionMask.Direction relativeDir = DirectionMask.Direction.getDirection(front, dir);
		IOSelector.IOState dirState = ioSelector.getState(relativeDir);
		boolean hasChest = type != Material.CHEST && type != Material.TRAPPED_CHEST;
		ItemStack display;
		if (hasChest) {
			display = new ItemStack(Material.BARRIER);
			ItemUtils.addComponentLore(display, Component
					.text("<no chest>")
					.style(Style.style(TextDecoration.BOLD))
					.color(TextColor.color(255, 0, 0)));
		} else {
			display = dirState.getUIVisual();
		}
		if (ioDirectionMode != null) {
			for (String descLine : ioDirectionMode.fullDescription) {
				ItemUtils.addComponentLore(display, Component
						.text(descLine)
						.color(TextColor.color(255, 255, 192)));
			}
		}
		ItemUtils.setComponentDisplayName(display,
				Component.text("\u00a7r")
						.append(Component.text(dir.name()).color(TextColor.color(192, 192, 192)))
						.append(Component.text(": "))
						.append(Component.text(dirState.name()).color(TextColor.color(dirState.color)))
						.asComponent());
		ItemUtils.addComponentLore(display,
				Component.text("Click to cycle IO mode").color(TextColor.color(192, 192, 192)));
		return new Clickable(display) {
			private ClickableInventory inventory;
			private int slot;

			@Override
			protected void clicked(Player player) {
				cycleIoState(relativeDir, false);
			}

			@Override
			protected void onRightClick(Player p) {
				cycleIoState(relativeDir, true);
			}

			@Override
			protected void onDoubleClick(Player p) { } // nop

			@Override
			public void addedToInventory(ClickableInventory inv, int slot) {
				this.inventory = inv;
				this.slot = slot;
			}

			private void cycleIoState(DirectionMask.Direction dir, boolean backwards) {
				IOSelector.IOState newState = ioSelector.cycleDirection(dir, backwards);
				ItemStack cstack = getItemStack();
				cstack.setType(newState.getUIVisual().getType());
				ItemUtils.setComponentDisplayName(cstack,
						Component.text("\u00a7r")
								.append(Component.text(dir.name()).color(TextColor.color(192, 192, 192)))
								.append(Component.text(": "))
								.append(Component.text(newState.name()).color(TextColor.color(newState.color)))
								.asComponent());
				if (inventory != null && inventory.getSlot(slot) == this) {
					inventory.setSlot(this, slot);
				}
			}
		};
	}

	private Clickable getCycleButton(DirectionMask.Direction dir) {
		if (!ioSelector.hasInputs() && !ioSelector.hasOutputs()) {
			// TODO short/optimized default UI (?)
		}
		BlockFace relativeDir = dir.getBlockFacing(front);
		Block relativeBlock = centerBlock.getRelative(relativeDir);
		Material type = relativeBlock.getType();
		IOSelector.IOState dirState = ioSelector.getState(dir);
		boolean hasChest = type != Material.CHEST && type != Material.TRAPPED_CHEST;
		ItemStack display;
		if (hasChest) {
			display = new ItemStack(Material.BARRIER);
			ItemUtils.addComponentLore(display, Component
					.text("<no chest>")
					.style(Style.style(TextDecoration.BOLD))
					.color(TextColor.color(255, 0, 0)));
		} else {
			display = dirState.getUIVisual();
		}
		if (ioDirectionMode != null) {
			for (String descLine : ioDirectionMode.fullDescription) {
				ItemUtils.addComponentLore(display, Component
						.text(descLine)
						.color(TextColor.color(255, 255, 192)));
			}
		}
		ItemUtils.setComponentDisplayName(display,
				Component.text("\u00a7r")
						.append(Component.text(dir.name()).color(TextColor.color(192, 192, 192)))
						.append(Component.text(": "))
						.append(Component.text(dirState.name()).color(TextColor.color(dirState.color)))
						.asComponent());
		ItemUtils.addComponentLore(display,
				Component.text("Click to cycle IO mode").color(TextColor.color(192, 192, 192)));

		return new Clickable(display) {
			private ClickableInventory inventory;
			private int slot;

			@Override
			protected void clicked(Player player) {
				cycleIoState(dir, false);
			}

			@Override
			protected void onRightClick(Player p) {
				cycleIoState(dir, true);
			}

			@Override
			protected void onDoubleClick(Player p) { } // nop

			@Override
			public void addedToInventory(ClickableInventory inv, int slot) {
				this.inventory = inv;
				this.slot = slot;
			}

			private void cycleIoState(DirectionMask.Direction dir, boolean backwards) {
				IOSelector.IOState newState = ioSelector.cycleDirection(dir, backwards);
				ItemStack cstack = getItemStack();
				cstack.setType(newState.getUIVisual().getType());
				ItemUtils.setComponentDisplayName(cstack,
						Component.text("\u00a7r")
								.append(Component.text(dir.name()).color(TextColor.color(192, 192, 192)))
								.append(Component.text(": "))
								.append(Component.text(newState.name()).color(TextColor.color(newState.color)))
								.asComponent());
				if (inventory != null && inventory.getSlot(slot) == this) {
					inventory.setSlot(this, slot);
				}
			}
		};
	}

	@Override
	protected void rebuild() {
		ioDirectionMode = FactoryMod.getInstance().getManager().getPlayerSettings().getIoDirectionMode(viewerId);
		switch (ioDirectionMode) {
			case VISUAL_RELATIVE: {
				set(getCycleButton(DirectionMask.Direction.TOP), 1);
				set(getCycleButton(DirectionMask.Direction.FRONT), 2);
				set(getCycleButton(DirectionMask.Direction.LEFT), 3);
				set(getCycleButton(DirectionMask.Direction.RIGHT), 5);
				set(getCycleButton(DirectionMask.Direction.BOTTOM), 7);
				set(getCycleButton(DirectionMask.Direction.BACK), 8);
				break;
			}
			case CARDINAL: {
				set(getCycleButton(BlockFace.NORTH), 1);
				set(getCycleButton(BlockFace.UP), 2);
				set(getCycleButton(BlockFace.WEST), 3);
				set(getCycleButton(BlockFace.EAST), 5);
				set(getCycleButton(BlockFace.SOUTH), 7);
				set(getCycleButton(BlockFace.DOWN), 8);
				break;
			}
		}
		set(new LClickable(centerDisplay, p -> {}), 4);
	}
}