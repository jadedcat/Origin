package com.countrygamer.countrygamercore.Base.Plugin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;

public class PluginEnchantment extends Enchantment {
	
	public PluginEnchantment(String name, int weight) {
		super(getNewID(), weight, EnumEnchantmentType.all);
		this.setName(name);
		System.out.println("Registered " + this.getName() + " to id " + this.effectId);
	}
	
	public static final int getNewID() {
		int i;
		for (i = 0; i < Enchantment.enchantmentsList.length; i++) {
			if (Enchantment.enchantmentsList[i] == null)
				break;
		}
		return i;
	}
	
}
