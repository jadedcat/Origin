package com.countrygamer.core.Base.client.gui;

import java.util.List;

import com.countrygamer.countrygamercore.lib.RedstoneState;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiButtonRedstoneController {
	
	public GuiButtonRedstone ignore, low, high;
	
	public GuiButtonRedstoneController() {
	}
	
	/**
	 * Returns the button ID after adding the redstone buttons. int[] positions MUSt have a length
	 * of 6 integers (x, y) x3 (for each button).
	 * 
	 * @param buttonList
	 * @param buttonID
	 * @param positions
	 * @return
	 */
	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	public int registerButtons(List buttonList, int buttonID, int[] positions) {
		
		buttonID++;
		this.ignore = new GuiButtonRedstone(buttonID, positions[0], positions[1],
				RedstoneState.IGNORE);
		buttonList.add(this.ignore);
		buttonID++;
		this.low = new GuiButtonRedstone(buttonID, positions[2], positions[3],
				RedstoneState.LOW);
		buttonList.add(this.low);
		buttonID++;
		this.high = new GuiButtonRedstone(buttonID, positions[4], positions[5],
				RedstoneState.HIGH);
		buttonList.add(this.high);
		
		return buttonID;
	}
	
	public boolean buttonPressed(int id) {
		if (id == this.ignore.id) {
			this.ignore.isActive = !this.ignore.isActive;
			if (this.ignore.isActive) {
				this.low.isActive = false;
				this.high.isActive = false;
			}
			return true;
		}
		else if (id == this.low.id) {
			this.low.isActive = !this.low.isActive;
			if (this.low.isActive) {
				this.high.isActive = false;
				this.ignore.isActive = false;
			}
			return true;
		}
		else if (id == this.high.id) {
			this.high.isActive = !this.high.isActive;
			if (this.high.isActive) {
				this.ignore.isActive = false;
				this.low.isActive = false;
			}
			return true;
		}
		return false;
	}
	
	public RedstoneState getActiveState() {
		if (this.ignore.isActive)
			return RedstoneState.IGNORE;
		else if (this.low.isActive)
			return RedstoneState.LOW;
		else if (this.high.isActive)
			return RedstoneState.HIGH;
		else
			return null;
	}
	
}
