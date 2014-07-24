package com.countrygamer.cgo.wrapper.common.tile

import com.countrygamer.cgo.common.lib.ActivatedAction
import net.minecraft.nbt.NBTTagCompound

/**
 *
 *
 * @author CountryGamer
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
