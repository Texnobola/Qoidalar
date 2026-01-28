package net.mcreator.qoidalar.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.HumanoidModel;

import net.mcreator.qoidalar.entity.RaisEntity;

public class RaisRenderer extends HumanoidMobRenderer<RaisEntity, HumanoidModel<RaisEntity>> {
	public RaisRenderer(EntityRendererProvider.Context context) {
		super(context, new HumanoidModel<RaisEntity>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
		this.addLayer(new HumanoidArmorLayer(this, new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(RaisEntity entity) {
		return new ResourceLocation("qoidalar:textures/entities/2021_07_31_shavkat-mirziyoyev-18540236.png");
	}
}