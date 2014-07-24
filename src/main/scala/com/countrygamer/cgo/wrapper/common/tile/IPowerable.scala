package com.countrygamer.cgo.wrapper.common.tile

import com.countrygamer.cgo.common.lib.RedstoneState
import net.minecraft.nbt.NBTTagCompound

/**
 * Used by TileEntities for handling usage of power
 *
 * @author CountryGamer
 */
trait IPowerable {

	var redstoneState: RedstoneState = RedstoneState.HIGH
	var isRecievingPower: Boolean = false

	def setRedstoneState(state: RedstoneState): Unit = {
		this.redstoneState = state

	}

	def getRedstoneState: RedstoneState = {
		this.redstoneState
	}

	/**
	 * Calls @see onPowerChanged() after setting of power
	 * @param isRecievingPower
	 */
	def setPowered(isRecievingPower: Boolean): Unit = {
		if (this.isRecievingPower != isRecievingPower) {
			this.isRecievingPower = isRecievingPower
			this.onPowerChanged
		}
	}

	/**
	 * Checks if self is powered.
	 *
	 * @param checkState
	 * If true, checks the redstone state and then decides
	 * If false, returns if self is recieving power
	 * @return
	 */
	def isPowered(checkState: Boolean): Boolean = {
		if (!checkState) {
			return this.isRecievingPower
		}
		else {
			if (this.redstoneState eq RedstoneState.IGNORE) {
				return true
			}
			else if (this.redstoneState eq RedstoneState.LOW) {
				return !this.isRecievingPower
			}
			else if (this.redstoneState eq RedstoneState.HIGH) {
				return this.isRecievingPower
			}
		}
		false
	}

	/**
	 * Called when the power is not what it was
	 */
	def onPowerChanged(): Unit = {}

	/**
	 * Checks if self is powered with regards to redstone state
	 * @return
	 */
	def canRun: Boolean = {
		this.isPowered(checkState = true)
	}

	/**
	 * Saves required data to passed compound
	 * @param tagCom
	 */
	def savePowerableNBT(tagCom: NBTTagCompound): Unit = {

		tagCom.setInteger("IPowerable_redstoneState",
			RedstoneState.getIntFromState(this.redstoneState))
		tagCom.setBoolean("IPowerable_isRecievingPower", this.isRecievingPower)

	}

	/**
	 * Reads required data from passed compound
	 * @param tagCom
	 */
	def readPowerableNBT(tagCom: NBTTagCompound): Unit = {

		this.redstoneState = RedstoneState
				.getStateFromInt(tagCom.getInteger("IPowerable_redstoneState"))
		this.isRecievingPower = tagCom.getBoolean("IPowerable_isRecievingPower")

	}

}
