package com.temportalist.origin.wrapper.common.enchant

import net.minecraft.enchantment.{Enchantment, EnumEnchantmentType}
import net.minecraft.util.ResourceLocation

/**
 *
 *
 * @author TheTemportalist
 */
class EnchantmentWrapper(name: String, weight: Int, reloc: ResourceLocation)
		extends Enchantment(EnchantmentHelper.getNewID, weight, EnumEnchantmentType.all) {

	// Default Constructor
	this.setName(name)

	// End Constructor

	// Other Constructors

	// End Constructors

}
