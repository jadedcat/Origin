package com.temportalist.origin.wrapper.common.enchant

import net.minecraft.enchantment.{Enchantment, EnumEnchantmentType}

/**
 *
 *
 * @author CountryGamer
 */
class EnchantmentWrapper(name: String, weight: Int)
		extends Enchantment(EnchantmentHelper.getNewID, weight, EnumEnchantmentType.all) {

	// Default Constructor
	this.setName(name)

	// End Constructor

	// Other Constructors

	// End Constructors

}
