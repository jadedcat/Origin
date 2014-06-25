package com.countrygamer.countrygamercore.base.client.render.models;

import net.minecraft.client.model.ModelRenderer;

public class ModelHelper {
	
	/**
	 * Create a new box in this model
	 * 
	 * @param modelInstance
	 * @param originX
	 * @param originY
	 * @param originZ
	 * @param offsetX
	 * @param offsetY
	 * @param offsetZ
	 * @param width
	 * @param height
	 * @param length
	 * @param rotationX
	 * @param rotationY
	 * @param rotationZ
	 * @param offsetU
	 * @param offsetV
	 * @param modelList
	 */
	public static void createModel(net.minecraft.client.model.ModelBase modelInstance,
			IModelBase modelInstance2, float originX, float originY, float originZ, float offsetX,
			float offsetY, float offsetZ, int width, int height, int length, float rotationX,
			float rotationY, float rotationZ, int offsetU, int offsetV) {
		ModelRenderer model = new ModelRenderer(modelInstance, offsetU, offsetV);
		// origin in reference to inter block coords
		model.setRotationPoint(originX, 8 - originY, originZ);
		// offsetX, offsetY, offsetZ, width, height, length
		model.addBox(offsetX, offsetY, offsetZ, width, height, length);
		model.setTextureSize(modelInstance.textureWidth, modelInstance.textureHeight);
		model.mirror = true;
		model.rotateAngleX = rotationX;
		model.rotateAngleY = rotationY;
		model.rotateAngleZ = rotationZ;
		
		modelInstance2.addModel(model);
	}
	
	public static ModelRenderer createArmorModel(ModelBipedBase modelInstance, String type,
			float offsetX, float offsetY, float offsetZ, int width, int height, int length,
			int offsetU, int offsetV) {
		ModelRenderer model = new ModelRenderer(modelInstance, offsetU, offsetV + 32);
		
		model.setRotationPoint(0, 0, 0);
		model.addBox(offsetX, offsetY, offsetZ, width, height, length);
		model.setTextureSize(modelInstance.textureWidth, modelInstance.textureHeight);
		model.mirror = true;
		model.rotateAngleX = 0;
		model.rotateAngleY = 0;
		model.rotateAngleZ = 0;
		
		if (type.equalsIgnoreCase("head")) {
			modelInstance.addModelToHead(model);
		}
		else if (type.equalsIgnoreCase("chest")) {
			modelInstance.addModelToChest(model);
		}
		else if (type.equalsIgnoreCase("right arm")) {
			modelInstance.addModelToRightArm(model);
		}
		else if (type.equalsIgnoreCase("left arm")) {
			modelInstance.addModelToLeftArm(model);
		}
		else if (type.equalsIgnoreCase("right leg")) {
			modelInstance.addModelToRightLeg(model);
		}
		else if (type.equalsIgnoreCase("left leg")) {
			modelInstance.addModelToLeftLeg(model);
		}
		else if (type.equalsIgnoreCase("right boot")) {
			modelInstance.addModelToRightBoot(model);
		}
		else if (type.equalsIgnoreCase("left boot")) {
			modelInstance.addModelToLeftBoot(model);
		}
		
		return model;
	}
	
	/**
	 * Meant for single line creation
	 * 
	 * @see createModel
	 * @param modelInstance
	 * @param originX
	 * @param originY
	 * @param originZ
	 * @param offsetX
	 * @param offsetY
	 * @param offsetZ
	 * @param width
	 * @param height
	 * @param length
	 * @param rotationX
	 * @param rotationY
	 * @param rotationZ
	 * @param textureWidth
	 * @param textureHeight
	 * @param offsetU
	 * @param offsetV
	 * @return
	 */
	public static net.minecraft.client.model.ModelBase createModelWithReturn(
			net.minecraft.client.model.ModelBase modelInstance, IModelBase modelInstance2,
			float originX, float originY, float originZ, float offsetX, float offsetY,
			float offsetZ, int width, int height, int length, float rotationX, float rotationY,
			float rotationZ, int textureWidth, int textureHeight, int offsetU, int offsetV) {
		ModelRenderer model = new ModelRenderer(modelInstance, offsetU, offsetV);
		// origin in reference to inter block coords
		model.setRotationPoint(originX, 8 - originY, originZ);
		// offsetX, offsetY, offsetZ, width, height, length
		model.addBox(offsetX, offsetY, offsetZ, width, height, length);
		model.setTextureSize(textureWidth, textureHeight);
		model.mirror = true;
		model.rotateAngleX = rotationX;
		model.rotateAngleY = rotationY;
		model.rotateAngleZ = rotationZ;
		
		modelInstance2.addModel(model);
		
		return modelInstance;
	}
	
}
