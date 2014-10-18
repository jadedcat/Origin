package com.temportalist.origin.wrapper.common.tile

import com.temportalist.origin.library.common.lib.enums.ActivatedAction
import net.minecraft.nbt.NBTTagCompound

/**
 *
 *
 * @author TheTemportalist
 */
trait IAction {

	var action: ActivatedAction = ActivatedAction.PULSE

	def setAction(action: ActivatedAction): Unit = {
		this.action = action
	}

	def getAction: ActivatedAction = {
		this.action
	}

	def saveActionNBT(tagCom: NBTTagCompound): Unit = {

		tagCom.setInteger("IAction_activatedActionID", ActivatedAction.getInt(this.action))

	}

	def readActionNBT(tagCom: NBTTagCompound): Unit = {

		this.action = ActivatedAction.getState(tagCom.getInteger("IAction_activatedAction"))

	}

}
