package com.countrygamer.countrygamercore.common.item;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import com.countrygamer.countrygamercore.base.client.render.models.ModelBipedBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemArmorModelBase extends ItemArmorBase {
	
	public ItemArmorModelBase(String modid, String name, ArmorMaterial mat, int renderIndex,
			String type) {
		super(modid, name, mat, renderIndex, type);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack,
			int armorSlot) {
		if (itemStack != null && itemStack.getItem() instanceof ItemArmor) {
			int type = ((ItemArmor) itemStack.getItem()).armorType;
			
			Constructor<? extends ModelBipedBase> ctor = null;
			try {
				ctor = this.getArmorModelClass().getConstructor(Float.class);
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}
			
			if (ctor != null) {
				ModelBipedBase model = null;
				
				float scale = type == 2 ? 0.5F : 1.0F;
				try {
					model = ctor.newInstance(scale);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				
				if (model != null) {
					model.checkArmorModel(entityLiving, armorSlot);
					return model;
				}
				
			}
			
		}
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	protected abstract Class<? extends ModelBipedBase> getArmorModelClass();
	
}
