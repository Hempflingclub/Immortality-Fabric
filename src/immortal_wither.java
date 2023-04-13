// Made with Blockbench 4.6.5
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class immortal_wither extends EntityModel<Entity> {
	private final ModelPart head;
	private final ModelPart head_r1;
	private final ModelPart hair;
	private final ModelPart hair12_r1;
	private final ModelPart hair10_r1;
	private final ModelPart hair7_r1;
	private final ModelPart hair4_r1;
	private final ModelPart hair2_r1;
	private final ModelPart skin;
	private final ModelPart skin17_r1;
	private final ModelPart skin14_r1;
	private final ModelPart ribs;
	private final ModelPart L6_r1;
	private final ModelPart L5_r1;
	private final ModelPart L4_r1;
	private final ModelPart L3_r1;
	private final ModelPart L2_r1;
	private final ModelPart L1_r1;
	private final ModelPart R6_r1;
	private final ModelPart R5_r1;
	private final ModelPart R4_r1;
	private final ModelPart R3_r1;
	private final ModelPart R2_r1;
	private final ModelPart R1_r1;
	private final ModelPart spine;
	private final ModelPart spine5_r1;
	private final ModelPart spine4_r1;
	private final ModelPart spine3_r1;
	private final ModelPart spine2_r1;
	private final ModelPart Ripple3_r1;
	private final ModelPart bb_main;
	private final ModelPart heart_r1;
	private final ModelPart neck2_r1;
	private final ModelPart neck1_r1;
	public immortal_wither(ModelPart root) {
		this.head = root.getChild("head");
		this.ribs = root.getChild("ribs");
		this.spine = root.getChild("spine");
		this.bb_main = root.getChild("bb_main");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData head_r1 = head.addChild("head_r1", ModelPartBuilder.create().uv(0, 8).cuboid(-5.6788F, -5.6527F, -1.4718F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F))
		.uv(9, 11).cuboid(2.7212F, -5.6527F, -1.4718F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-1.9788F, -7.6527F, -1.9718F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.0F, 3.1416F, 0.0F));

		ModelPartData hair = head.addChild("hair", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData hair12_r1 = hair.addChild("hair12_r1", ModelPartBuilder.create().uv(12, 0).cuboid(-1.6212F, -7.2225F, -3.0097F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(0.1788F, -7.2225F, -3.0097F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, -0.1309F, 0.0F, 0.0F));

		ModelPartData hair10_r1 = hair.addChild("hair10_r1", ModelPartBuilder.create().uv(8, 16).cuboid(-2.9608F, -7.229F, 0.8282F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(0, 9).cuboid(-2.9608F, -7.229F, -0.4718F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(0, 7).cuboid(-2.9608F, -7.229F, -1.7718F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.0F, 3.1416F, -0.1309F));

		ModelPartData hair7_r1 = hair.addChild("hair7_r1", ModelPartBuilder.create().uv(17, 6).cuboid(-3.3194F, -7.0855F, -1.8282F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(12, 0).cuboid(-3.3194F, -7.0855F, -0.5282F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(16, 3).cuboid(-3.3194F, -7.0855F, 0.8718F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.0F, 0.0F, 0.1745F));

		ModelPartData hair4_r1 = hair.addChild("hair4_r1", ModelPartBuilder.create().uv(3, 13).cuboid(-6.7874F, -4.1828F, 0.1718F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(16, 19).cuboid(-6.7874F, -4.1828F, -1.3282F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.0F, 0.0F, 0.2182F));

		ModelPartData hair2_r1 = hair.addChild("hair2_r1", ModelPartBuilder.create().uv(7, 13).cuboid(-6.9225F, -3.8937F, -1.1718F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(18, 20).cuboid(-6.9225F, -3.8937F, 0.3282F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.0F, 3.1416F, -0.2618F));

		ModelPartData skin = head.addChild("skin", ModelPartBuilder.create().uv(18, 13).cuboid(3.2F, -13.0F, -2.0F, 2.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(16, 0).cuboid(-1.5F, -14.0F, -2.5F, 2.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(13, 8).cuboid(3.2F, -13.0F, -1.0F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(9, 7).cuboid(-1.5F, -14.0F, -1.5F, 0.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 1).cuboid(-5.2F, -13.0F, -2.0F, 0.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(11, 20).cuboid(-5.2F, -13.0F, -2.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData skin17_r1 = skin.addChild("skin17_r1", ModelPartBuilder.create().uv(0, 0).cuboid(2.0212F, -4.6527F, -0.4718F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F))
		.uv(20, 0).cuboid(2.0212F, -4.6527F, -0.4718F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F))
		.uv(20, 4).cuboid(3.7212F, -1.6527F, -1.4718F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(12, 3).cuboid(2.7212F, -2.6527F, -1.4718F, 2.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(22, 13).cuboid(-4.6788F, -2.6527F, -1.4718F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(16, 0).cuboid(-5.6788F, -1.6527F, -0.4718F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(9, 5).cuboid(-5.6788F, -2.6527F, -1.4718F, 0.0F, 1.0F, 3.0F, new Dilation(0.0F))
		.uv(20, 21).cuboid(-2.9788F, -3.6527F, -0.9718F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F))
		.uv(9, 8).cuboid(-1.9788F, -3.6527F, -1.9718F, 0.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(21, 14).cuboid(0.0212F, -3.6527F, -1.9718F, 2.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.0F, 3.1416F, 0.0F));

		ModelPartData skin14_r1 = skin.addChild("skin14_r1", ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, -0.5F, -1.0F, 0.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-2.2F, -12.5F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData ribs = modelPartData.addChild("ribs", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData L6_r1 = ribs.addChild("L6_r1", ModelPartBuilder.create().uv(12, 0).cuboid(1.4221F, 0.7513F, -0.6201F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.0F, -2.0508F, -0.3054F));

		ModelPartData L5_r1 = ribs.addChild("L5_r1", ModelPartBuilder.create().uv(13, 8).cuboid(1.3946F, -0.4856F, -0.873F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.0F, -2.0508F, -0.2182F));

		ModelPartData L4_r1 = ribs.addChild("L4_r1", ModelPartBuilder.create().uv(14, 17).cuboid(1.2353F, -1.6527F, -0.599F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.0F, -1.9635F, 0.0F));

		ModelPartData L3_r1 = ribs.addChild("L3_r1", ModelPartBuilder.create().uv(16, 1).cuboid(0.4799F, 0.8513F, -2.3221F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.0F, 2.6616F, -0.3054F));

		ModelPartData L2_r1 = ribs.addChild("L2_r1", ModelPartBuilder.create().uv(16, 4).cuboid(0.227F, -0.4856F, -2.2946F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.0F, 2.6616F, -0.2182F));

		ModelPartData L1_r1 = ribs.addChild("L1_r1", ModelPartBuilder.create().uv(19, 15).cuboid(0.3445F, -1.6527F, -2.1591F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.0F, 2.6616F, 0.0F));

		ModelPartData R6_r1 = ribs.addChild("R6_r1", ModelPartBuilder.create().uv(17, 7).cuboid(1.3874F, 0.7634F, -1.2592F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.0F, -1.0908F, 0.3054F));

		ModelPartData R5_r1 = ribs.addChild("R5_r1", ModelPartBuilder.create().uv(8, 17).cuboid(1.2698F, -0.4929F, -1.1254F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.0F, -1.0908F, 0.2182F));

		ModelPartData R4_r1 = ribs.addChild("R4_r1", ModelPartBuilder.create().uv(18, 10).cuboid(1.2364F, -1.6527F, -1.3379F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.0F, -1.1781F, 0.0F));

		ModelPartData R3_r1 = ribs.addChild("R3_r1", ModelPartBuilder.create().uv(18, 18).cuboid(0.4592F, 0.8634F, 0.1874F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.0F, 0.48F, 0.3054F));

		ModelPartData R2_r1 = ribs.addChild("R2_r1", ModelPartBuilder.create().uv(20, 0).cuboid(0.2254F, -0.4929F, 0.1698F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.0F, 0.48F, 0.2182F));

		ModelPartData R1_r1 = ribs.addChild("R1_r1", ModelPartBuilder.create().uv(20, 3).cuboid(0.3199F, -1.6527F, 0.1778F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.0F, 0.48F, 0.0F));

		ModelPartData spine = modelPartData.addChild("spine", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData spine5_r1 = spine.addChild("spine5_r1", ModelPartBuilder.create().uv(12, 17).cuboid(-0.4788F, 5.6077F, -4.2875F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.7418F, 3.1416F, 0.0F));

		ModelPartData spine4_r1 = spine.addChild("spine4_r1", ModelPartBuilder.create().uv(21, 6).cuboid(-0.4788F, 4.0926F, -3.2636F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.5672F, 3.1416F, 0.0F));

		ModelPartData spine3_r1 = spine.addChild("spine3_r1", ModelPartBuilder.create().uv(8, 20).cuboid(0.4149F, 3.0505F, -0.5743F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.0F, 3.0107F, -0.48F));

		ModelPartData spine2_r1 = spine.addChild("spine2_r1", ModelPartBuilder.create().uv(12, 20).cuboid(-1.3982F, 3.0318F, -0.5537F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.0F, -3.0107F, 0.48F));

		ModelPartData Ripple3_r1 = spine.addChild("Ripple3_r1", ModelPartBuilder.create().uv(18, 10).cuboid(0.0212F, 1.8473F, 0.0282F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(18, 17).cuboid(0.0212F, -0.1527F, 0.2282F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(7, 19).cuboid(0.0212F, -2.1527F, 0.0282F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(0, 14).cuboid(-0.4788F, -3.6527F, -0.4718F, 1.0F, 9.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.0F, 3.1416F, 0.0F));

		ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData heart_r1 = bb_main.addChild("heart_r1", ModelPartBuilder.create().uv(22, 10).cuboid(-0.4788F, -1.6527F, -2.1782F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.0F, 3.1416F, 0.0F));

		ModelPartData neck2_r1 = bb_main.addChild("neck2_r1", ModelPartBuilder.create().uv(4, 14).cuboid(0.6082F, -5.2875F, -0.3718F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.0F, 3.1416F, 0.9163F));

		ModelPartData neck1_r1 = bb_main.addChild("neck1_r1", ModelPartBuilder.create().uv(4, 20).cuboid(-1.5737F, -5.1145F, -0.3718F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.5212F, -10.3473F, -0.4718F, 0.0F, 3.1416F, -0.9163F));
		return TexturedModelData.of(modelData, 32, 32);
	}
	@Override
	public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		ribs.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		spine.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		bb_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}