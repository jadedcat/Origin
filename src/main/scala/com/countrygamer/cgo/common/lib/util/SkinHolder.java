package com.countrygamer.cgo.common.lib.util;

import com.countrygamer.cgo.common.Origin;
import com.countrygamer.cgo.common.lib.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * @author maxpowa (permissions given)
 */
public class SkinHolder {

	private BufferedImage bufferedimage;
	private String username;
	private ResourceLocation resourceLocation;
	private DynamicTexture skin;

	public SkinHolder(String username) {
		this.username = username;
		this.resourceLocation = new ResourceLocation("skins/" + username);
	}

	public void prepareSkin() {
		this.skin = (DynamicTexture) Minecraft.getMinecraft().getTextureManager()
				.getTexture(this.resourceLocation);

		try {
			URL url = new URL(String.format("http://skins.minecraft.net/MinecraftSkins/%s.png",
					this.username));
			this.bufferedimage = ImageIO.read(url);
		} catch (Exception exception) {
			LogHelper.error(Origin.pluginName(), "Invalid " + this.username);
			this.bufferedimage = null;
			return;
		}

		if (this.skin == null) {
			this.skin = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
			Minecraft.getMinecraft().getTextureManager()
					.loadTexture(this.resourceLocation, this.skin);
		}

		bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(),
				this.skin.getTextureData(), 0, bufferedimage.getWidth());
		this.skin.updateDynamicTexture();
	}

	public ResourceLocation getSkin() {
		return this.resourceLocation;
	}

	public float getSkinWidth() {
		return this.bufferedimage.getWidth();
	}

	public float getSkinHeight() {
		return this.bufferedimage.getHeight();
	}

	public static void renderFace(int x, int y, float scale, SkinHolder skin) {
		GL11.glPushMatrix();
		GL11.glScalef(scale, scale, 1.0f);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glTranslatef(x / scale, y / scale, 1.0f);
		UtilRender.bindResource(skin.getSkin());
		Gui.func_146110_a(0, 0, 8.0F, 8.0F, 8, 8, skin.getSkinWidth(), skin.getSkinHeight());
		Gui.func_146110_a(0, 0, 40.0F, 8.0F, 8, 8, skin.getSkinWidth(), skin.getSkinHeight());
		GL11.glPopMatrix();
	}

}
