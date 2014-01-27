package CountryGamer_Core.Blocks.TileEnt.ClientRender;

import java.util.ArrayList;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBase extends net.minecraft.client.model.ModelBase {

	protected ArrayList<ModelRenderer> modelList = new ArrayList();

	public ModelBase(int textureWidth, int textureHeight) {
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;

	}

	public void createModel(ModelBase modelInstance,
			ArrayList<ModelRenderer> modelList, float originX, float originY,
			float originZ, float offsetX, float offsetY, float offsetZ,
			int width, int height, int length, float rotationX,
			float rotationY, float rotationZ, int textureWidth,
			int textureHeight, int offsetU, int offsetV) {
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

	public void render(Entity entity, float f, float f1, float f2, float f3,
			float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		this.renderModel(f5);
	}

	public void renderModel(float f5) {
		for (ModelRenderer model : this.modelList) {
			model.render(f5);
		}
	}

	public void setRotationAngles(float f, float f1, float f2, float f3,
			float f4, float f5) {
	}

}
