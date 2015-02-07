package com.temportalist.origin.library.common.lib

import net.minecraft.block.state.IBlockState
import net.minecraftforge.common.property.IUnlistedProperty

/**
 *
 *
 * @author TheTemportalist 2/7/15
 */
object BlockProps {

	val STATE: IUnlistedProperty[IBlockState] = new PropertyState()

	class PropertyState extends IUnlistedProperty[IBlockState]() {
		override def getType: Class[IBlockState] = classOf[IBlockState]

		override def getName: String = "IBlockState"

		override def valueToString(v: IBlockState): String = {
			NameParser.getName(v, hasID = true, hasMeta = true)
		}

		override def isValid(v: IBlockState): Boolean = true
	}

}
