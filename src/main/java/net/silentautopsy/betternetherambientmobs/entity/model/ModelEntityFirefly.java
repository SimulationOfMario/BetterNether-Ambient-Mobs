package net.silentautopsy.betternetherambientmobs.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.silentautopsy.betternetherambientmobs.entity.custom.EntityFirefly;
import org.jetbrains.annotations.NotNull;

public class ModelEntityFirefly extends AgeableListModel<EntityFirefly>
{
    private final ModelPart body;
    private final ModelPart glow;
    private final static String GLOW = "glow";

    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition modelPartData_BODY = modelPartData.addOrReplaceChild(
                PartNames.BODY,
                CubeListBuilder.create()
                               .texOffs(
                                       0,
                                       0
                               )
                               .addBox(
                                       0F,
                                       0F,
                                       0F,
                                       5,
                                       5,
                                       5
                               ),
                PartPose.offset(-2.5F, 18F, -2.5F)
        );

        modelPartData_BODY.addOrReplaceChild(
                PartNames.TAIL,
                CubeListBuilder.create()
                               .texOffs(0, 22)
                               .addBox(0F, 0F, 0F, 3F, 3F, 4F),
                PartPose.offset(1.0F, 5F, 0.5F)
        );

        modelPartData.addOrReplaceChild(
                GLOW,
                CubeListBuilder.create()
                               .texOffs(0, 10)
                               .addBox(0F, 0F, 0F, 5F, 5F, 5F, new CubeDeformation(0.2f)),

                PartPose.offset(-2.6F, 18.1F, -2.6F)
        );

        return LayerDefinition.create(modelData, 32, 64);
    }

    public ModelEntityFirefly(ModelPart root) {
        this.body = root.getChild(PartNames.BODY);
        // this.legs = this.body.getChild(EntityModelPartNames.TAIL);
        this.glow = root.getChild(GLOW);
    }

    @Override
    protected @NotNull Iterable<ModelPart> headParts() {
        return ImmutableList.of();
    }

    @Override
    protected @NotNull Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body);
    }

    @Override
    public void setupAnim(
            @NotNull EntityFirefly entity,
            float limbAngle,
            float limbDistance,
            float customAngle,
            float headYaw,
            float headPitch
    ) {
    }

    public ModelPart getGlowPart() {
        return this.glow;
    }
}