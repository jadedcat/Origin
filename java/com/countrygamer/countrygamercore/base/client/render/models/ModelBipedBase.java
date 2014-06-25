package com.countrygamer.countrygamercore.base.client.render.models;

import java.util.ArrayList;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class ModelBipedBase extends ModelBiped implements IModelBase {
	
	private ArrayList<ModelRenderer> legModels = new ArrayList<ModelRenderer>();
	private ArrayList<ModelRenderer> bootModels = new ArrayList<ModelRenderer>();
	
	public ModelBipedBase(Float scale) {
		super(scale, 0, 64, 96);
		
	}
	
	@Override
	public void addModel(ModelRenderer model) {
	}
	
	public void addModelToHead(ModelRenderer model) {
		this.bipedHead.addChild(model);
	}
	
	public void addModelToChest(ModelRenderer model) {
		this.bipedBody.addChild(model);
	}
	
	public void addModelToRightArm(ModelRenderer model) {
		this.bipedRightArm.addChild(model);
	}
	
	public void addModelToLeftArm(ModelRenderer model) {
		this.bipedLeftArm.addChild(model);
	}
	
	public void addModelToRightLeg(ModelRenderer model) {
		this.bipedRightLeg.addChild(model);
		this.legModels.add(model);
	}
	
	public void addModelToLeftLeg(ModelRenderer model) {
		this.bipedLeftLeg.addChild(model);
		this.legModels.add(model);
	}
	
	public void addModelToRightBoot(ModelRenderer model) {
		this.bipedRightLeg.addChild(model);
		this.bootModels.add(model);
	}
	
	public void addModelToLeftBoot(ModelRenderer model) {
		this.bipedLeftLeg.addChild(model);
		this.bootModels.add(model);
	}
	
	public void checkArmorModel(EntityLivingBase entityLivingBase, int armorSlot) {
		this.isSneak = entityLivingBase.isSneaking();
		this.isRiding = entityLivingBase.isRiding();
		this.isChild = entityLivingBase.isChild();
		this.heldItemRight = entityLivingBase.getEquipmentInSlot(0) != null ? 1 : 0;
		if (entityLivingBase instanceof EntityPlayer) {
			this.aimedBow = ((EntityPlayer) entityLivingBase).getItemInUseDuration() > 2;
		}
		
		this.bipedHead.showModel = false;
		this.bipedBody.showModel = false;
		this.bipedRightArm.showModel = false;
		this.bipedLeftArm.showModel = false;
		this.bipedRightLeg.showModel = false;
		this.bipedLeftLeg.showModel = false;
		for (int i = 0; i < this.legModels.size(); i++)
			this.legModels.get(i).showModel = false;
		for (int i = 0; i < this.bootModels.size(); i++)
			this.bootModels.get(i).showModel = false;
		
		if (armorSlot == 0) {
			this.bipedHead.showModel = true;
		}
		else if (armorSlot == 1) {
			this.bipedBody.showModel = true;
			this.bipedRightArm.showModel = true;
			this.bipedLeftArm.showModel = true;
		}
		else if (armorSlot == 2 || armorSlot == 3) {
			this.bipedRightLeg.showModel = true;
			this.bipedLeftLeg.showModel = true;
			
			if (armorSlot == 2) {
				for (int i = 0; i < this.legModels.size(); i++)
					this.legModels.get(i).showModel = true;
			}
			else if (armorSlot == 3) {
				for (int i = 0; i < this.bootModels.size(); i++)
					this.bootModels.get(i).showModel = true;
			}
		}
		
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}
	
	@Override
	public void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
}
