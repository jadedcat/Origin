package com.temportalist.origin.library.common.lib

import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import net.minecraftforge.common.property.IUnlistedProperty

/**
 *
 *
 * @author TheTemportalist 2/7/15
 */
object BlockProps {

	val STATE: IUnlistedProperty[IBlockState] = new PropertyState()

	val ITEMSTACK: IUnlistedProperty[ItemStack] = new IUnlistedProperty[ItemStack] {
		override def getType: Class[ItemStack] = classOf[ItemStack]

		override def getName: String = "ItemStack"

		override def valueToString(value: ItemStack): String = value.getDisplayName

		override def isValid(value: ItemStack): Boolean = true
	}

	class PropertyState extends IUnlistedProperty[IBlockState]() {
		override def getType: Class[IBlockState] = classOf[IBlockState]

		override def getName: String = "IBlockState"

		override def valueToString(v: IBlockState): String = {
			NameParser.getName(v, hasID = true, hasMeta = true)
		}

		override def isValid(v: IBlockState): Boolean = true
	}

}
