package com.countrygamer.countrygamercore.Base.client;

import java.util.ArrayList;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * A base class for new Models. Makes new models easier to create boxes.
 * 
 * @author Country_Gamer
 * 
 */
public class ModelBase extends net.minecraft.client.model.ModelBase {
	
	public ArrayList<ModelRenderer> modelList = new ArrayList<ModelRenderer>();
	
	public ModelBase(int textureWidth, int textureHeight) {
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		
	}
	
	/**
	 * Create a new box in this model
	 * 
	 * @param modelInstance
	 * @param modelList
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
	 */
	public void createModel(ModelBase modelInstance, ArrayList<ModelRenderer> modelList,
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
		
		modelList.add(model);
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
	public ModelRenderer createModelWithReturn(ModelBase modelInstance, float originX,
			float originY, float originZ, float offsetX, float offsetY, float offsetZ, int width,
			int height, int length, float rotationX, float rotationY, float rotationZ,
			int textureWidth, int textureHeight, int offsetU, int offsetV) {
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
		
		return model;
	}
	
	/**
	 * Renders this model for an entity.
	 * 
	 * @see renderModel(float)
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		this.renderModel(f5);
	}
	
	/**
	 * Renders all boxes in this model
	 * 
	 * @param f5
	 */
	public void renderModel(float f5) {
		for (ModelRenderer model : this.modelList) {
			model.render(f5);
		}
	}
	
	/**
	 * Old rotation handler
	 * 
	 * @param f
	 * @param f1
	 * @param f2
	 * @param f3
	 * @param f4
	 * @param f5
	 */
	@Deprecated
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
	}
	
	/**
	 * New rotation handler. Rotates the specified model by the passed angles
	 * 
	 * @param model
	 * @param x
	 * @param y
	 * @param z
	 */
	protected void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
}
