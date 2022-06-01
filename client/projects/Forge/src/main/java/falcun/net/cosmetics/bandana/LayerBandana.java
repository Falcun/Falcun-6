package falcun.net.cosmetics.bandana;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.TextureOffset;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.optifine.model.ModelSprite;

public class LayerBandana implements LayerRenderer<AbstractClientPlayer> {
	private RenderPlayer playerRenderer;

	public static float MIN_HEIGHT_OFFSET = 0;
	public static float MAX_HEIGHT_OFFSET = 8;

	private static boolean ANIMATED_BUN = false;
	private static int bandHeight = 2;
	private static int bandWidth = 1;
	private static float bunBandRotation = 0.6F;

	public LayerBandana(RenderPlayer playerRendererIn) {
		this.playerRenderer = playerRendererIn;

		playerRenderer.getMainModel().setTextureOffset("bandana.front_band", 0, 0);
		playerRenderer.getMainModel().setTextureOffset("bandana.right_band", 19, 19);
		playerRenderer.getMainModel().setTextureOffset("bandana.left_band", -5, 19);
		playerRenderer.getMainModel().setTextureOffset("bandana.back_band", 5, 19);
		playerRenderer.getMainModel().setTextureOffset("bandana.bun", 10, 13);
		playerRenderer.getMainModel().setTextureOffset("bun_right.bun_band_right", 15, 20);
		playerRenderer.getMainModel().setTextureOffset("bun_left.bun_band_left", 12, 20);
	}

	public static ModelRenderer getRenderer(RenderPlayer playerRenderer, float bandHeightOffset) {
		ModelRenderer bipedBandana = new ModelRenderer(playerRenderer.getMainModel(), "bandana");
		bipedBandana.setTextureSize(32, 16);

		//Band
		addBox(bipedBandana, "front_band", -5F, bandHeightOffset, -5F, 10, bandHeight, bandWidth);
		addBox(bipedBandana, "right_band", -5F, bandHeightOffset, -4F, bandWidth, bandHeight, 8);
		addBox(bipedBandana, "left_band", 4F, bandHeightOffset, -4F, bandWidth, bandHeight, 8);
		addBox(bipedBandana, "back_band", -5F, bandHeightOffset, 4F, 10, bandHeight, bandWidth);


		float bunBandLeftX = 3;
		float bunBandRightX = -4;
		float bunBandLeftZ = 5.02F;
		float bunBandRightZ = 5.02F;

		if (bandHeightOffset == -5) {
			bunBandLeftX = 1.5F;
			bunBandRightX = -2.5F;
			bunBandLeftZ = 4.7F;
			bunBandRightZ = 4.7F;
		} else if (bandHeightOffset == -7) {
			bunBandLeftX = 3;
			bunBandRightX = -4;
			bunBandLeftZ = 5.02F;
			bunBandRightZ = 5.02F;
		} else if (bandHeightOffset == -8) {
			bunBandLeftX = 3;
			bunBandRightX = -4;
			bunBandLeftZ = 5.2F;
			bunBandRightZ = 5.2F;
		}

		//Bun
		addBox(bipedBandana, "bun", -1F, bandHeightOffset, 5F, 2, bandHeight, 0.2F);

		ModelRenderer bunRight = new ModelRenderer(playerRenderer.getMainModel(), "bun_right");
		addBox(bunRight, "bun_band_right", bunBandRightX, bandHeightOffset + 2F, bunBandRightZ, 1, bandHeight + 1.5F, 0.15F);
		bunRight.rotateAngleZ = bunBandRotation;
		bunRight.setRotationPoint(-1F, 1F, 1F);
		bunRight.rotateAngleX = 0.2F;
		bipedBandana.addChild(bunRight);

		ModelRenderer bunLeft = new ModelRenderer(playerRenderer.getMainModel(), "bun_left");
		addBox(bunLeft, "bun_band_left", bunBandLeftX, bandHeightOffset + 2F, bunBandLeftZ, 1, bandHeight + 1.5F, 0.15F);
		bunLeft.rotateAngleZ = -bunBandRotation;

		bunLeft.setRotationPoint(1F, 1F, 1F);
		bunLeft.rotateAngleX = 0.2F;
		bipedBandana.addChild(bunLeft);

		bipedBandana.setRotationPoint(0.0F, 0.0F + playerRenderer.getMainModel().bipedHead.rotationPointY, 0.0F);
		return bipedBandana;
	}

	@Override
	public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
		if (entitylivingbaseIn.isInvisible()) {
			return;
		}

		ResourceLocation location = entitylivingbaseIn.getBandana();

		if (location == null) {
			return;
		}

//		BandanaPlayerHandler user = Falcun.getInstance().bandanaManager.getFromUUID(entitylivingbaseIn.getUniqueID().toString());
		BandanaPlayerHandler user = null;

		if (user == null) {
			return;
		}

		ModelRenderer bipedBandana = user.getRenderer(playerRenderer);

		if (bipedBandana == null) {
			return;
		}

		ItemStack stack = entitylivingbaseIn.getCurrentArmor(3);

		float scaling = 0.91F;

		if (stack != null) {
			scaling = 1.05F;
		}

		GlStateManager.pushMatrix();
		GlStateManager.scale(scaling, scaling, scaling);

		Minecraft.getMinecraft().getTextureManager().bindTexture(location);

		if (entitylivingbaseIn.isSneaking()) {
			GlStateManager.translate(0.0F, 0.25F, 0.0F);
		}

		bipedBandana.rotateAngleX = playerRenderer.getMainModel().bipedHead.rotateAngleX;
		bipedBandana.rotateAngleY = playerRenderer.getMainModel().bipedHead.rotateAngleY;
		bipedBandana.rotateAngleZ = playerRenderer.getMainModel().bipedHead.rotateAngleZ;

		render(bipedBandana, 0.0625F);

		GlStateManager.popMatrix();
	}

	private void render(ModelRenderer bipedBandana, float p_78785_1_) {
		if (!bipedBandana.isHidden && bipedBandana.showModel) {
			bipedBandana.checkResetDisplayList();

			if (!bipedBandana.getCompiled()) {
				compileDisplayList(bipedBandana, p_78785_1_);
			}

			int i = 0;

			if (bipedBandana.getTextureLocation() != null && !bipedBandana.renderGlobal.renderOverlayDamaged) {
				if (bipedBandana.renderGlobal.renderOverlayEyes) {
					return;
				}

				i = GlStateManager.getBoundTexture();
				Config.getTextureManager().bindTexture(bipedBandana.getTextureLocation());
			}

			if (bipedBandana.modelUpdater != null) {
				bipedBandana.modelUpdater.update();
			}

			boolean flag = bipedBandana.scaleX != 1.0F || bipedBandana.scaleY != 1.0F || bipedBandana.scaleZ != 1.0F;
			GlStateManager.translate(bipedBandana.offsetX, bipedBandana.offsetY, bipedBandana.offsetZ);

			if (bipedBandana.rotateAngleX == 0.0F && bipedBandana.rotateAngleY == 0.0F && bipedBandana.rotateAngleZ == 0.0F) {
				if (bipedBandana.rotationPointX == 0.0F && bipedBandana.rotationPointY == 0.0F && bipedBandana.rotationPointZ == 0.0F) {
					if (flag) {
						GlStateManager.scale(bipedBandana.scaleX, bipedBandana.scaleY, bipedBandana.scaleZ);
					}

					GlStateManager.callList(bipedBandana.getDisplayList());

					if (bipedBandana.childModels != null) {
						for (int l = 0; l < bipedBandana.childModels.size(); ++l) {
							ModelRenderer renderer = bipedBandana.childModels.get(l);
							render(renderer, p_78785_1_);
						}
					}

					if (flag) {
						GlStateManager.scale(1.0F / bipedBandana.scaleX, 1.0F / bipedBandana.scaleY, 1.0F / bipedBandana.scaleZ);
					}
				} else {
					GlStateManager.translate(bipedBandana.rotationPointX * p_78785_1_, bipedBandana.rotationPointY * p_78785_1_, bipedBandana.rotationPointZ * p_78785_1_);

					if (flag) {
						GlStateManager.scale(bipedBandana.scaleX, bipedBandana.scaleY, bipedBandana.scaleZ);
					}

					GlStateManager.callList(bipedBandana.getDisplayList());

					if (bipedBandana.childModels != null) {
						for (int k = 0; k < bipedBandana.childModels.size(); ++k) {
							ModelRenderer renderer = bipedBandana.childModels.get(k);
							render(renderer, p_78785_1_);
						}
					}

					if (flag) {
						GlStateManager.scale(1.0F / bipedBandana.scaleX, 1.0F / bipedBandana.scaleY, 1.0F / bipedBandana.scaleZ);
					}

					GlStateManager.translate(-bipedBandana.rotationPointX * p_78785_1_, -bipedBandana.rotationPointY * p_78785_1_, -bipedBandana.rotationPointZ * p_78785_1_);
				}
			} else {
				GlStateManager.pushMatrix();
				GlStateManager.translate(bipedBandana.rotationPointX * p_78785_1_, bipedBandana.rotationPointY * p_78785_1_, bipedBandana.rotationPointZ * p_78785_1_);

				if (bipedBandana.rotateAngleZ != 0.0F) {
					GlStateManager.rotate(bipedBandana.rotateAngleZ * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
				}

				if (bipedBandana.rotateAngleY != 0.0F) {
					GlStateManager.rotate(bipedBandana.rotateAngleY * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
				}

				if (bipedBandana.rotateAngleX != 0.0F) {
					GlStateManager.rotate(bipedBandana.rotateAngleX * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
				}

				if (flag) {
					GlStateManager.scale(bipedBandana.scaleX, bipedBandana.scaleY, bipedBandana.scaleZ);
				}

				GlStateManager.callList(bipedBandana.getDisplayList());

				if (bipedBandana.childModels != null) {
					for (int j = 0; j < bipedBandana.childModels.size(); ++j) {
						ModelRenderer renderer = bipedBandana.childModels.get(j);
						render(renderer, p_78785_1_);
					}
				}

				GlStateManager.popMatrix();
			}

			GlStateManager.translate(-bipedBandana.offsetX, -bipedBandana.offsetY, -bipedBandana.offsetZ);

			if (i != 0) {
				GlStateManager.bindTexture(i);
			}
		}
	}

	private void compileDisplayList(ModelRenderer bipedBandana, float scale) {
		if (bipedBandana.getDisplayList() == 0) {
			bipedBandana.displayList = GLAllocation.generateDisplayLists(1);
		}

		GL11.glNewList(bipedBandana.getDisplayList(), GL11.GL_COMPILE);
		WorldRenderer worldrenderer = Tessellator.getInstance().getWorldRenderer();

		for (int i = 0; i < bipedBandana.cubeList.size(); ++i) {
			ModelBox box = bipedBandana.cubeList.get(i);
			box.render(worldrenderer, scale);
		}

		for (int j = 0; j < bipedBandana.spriteList.size(); ++j) {
			ModelSprite modelsprite = (ModelSprite) bipedBandana.spriteList.get(j);
			modelsprite.render(Tessellator.getInstance(), scale);
		}

		GL11.glEndList();
		bipedBandana.compiled = true;
	}

	public static ModelRenderer addBox(ModelRenderer parent, String partName, float offX, float offY, float offZ, float width, float height, float depth) {
		partName = parent.boxName + "." + partName;
		TextureOffset textureoffset = parent.baseModel.getTextureOffset(partName);
		parent.setTextureOffset(textureoffset.textureOffsetX, textureoffset.textureOffsetY);
		ModelBoxBandana box = new ModelBoxBandana(parent, parent.textureOffsetX, parent.textureOffsetY, offX, offY, offZ, width, height, depth, 0.0F).setBoxName(partName);

		float tiltAmount = 0.5F;

		if (partName.equalsIgnoreCase("bandana.front_band")) {
		} else if (partName.equalsIgnoreCase("bandana.right_band")) {
		} else if (partName.equalsIgnoreCase("bandana.left_band")) {
		} else if (partName.equalsIgnoreCase("bun_right.bun_band_right")) {
			//animated bun in future?
		} else if (partName.equalsIgnoreCase("bun_left.bun_left")) {

		}

		box.reinit();

		parent.cubeList.add(box);
		return parent;
	}

	public boolean shouldCombineTextures() {
		return false;
	}

	public static boolean isValidOffset(float heightOffset) {
		if (heightOffset < MIN_HEIGHT_OFFSET) {
			return false;
		} else if (heightOffset > MAX_HEIGHT_OFFSET) {
			return false;
		}

		return true;
	}
}