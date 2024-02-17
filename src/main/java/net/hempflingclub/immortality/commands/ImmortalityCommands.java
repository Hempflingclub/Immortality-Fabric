package net.hempflingclub.immortality.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.hempflingclub.immortality.Immortality;
import net.hempflingclub.immortality.util.ImmortalityData;
import net.hempflingclub.immortality.util.ImmortalityData.DataTypeBool;
import net.hempflingclub.immortality.util.ImmortalityData.DataTypeInt;
import net.hempflingclub.immortality.util.ImmortalityStatus;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

import java.util.Objects;

import static net.hempflingclub.immortality.util.ImmortalityStatus.getBool;
import static net.hempflingclub.immortality.util.ImmortalityStatus.getInt;

public final class ImmortalityCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal(Immortality.MOD_ID).requires((source) -> source.hasPermissionLevel(0))
                .executes((context -> {
                    if (context.getSource().isExecutedByPlayer()) {
                        PlayerEntity playerEntity = (context.getSource().getPlayer());
                        context.getSource().getServer().execute(() -> {
                            assert playerEntity != null;
                            playerEntity.sendMessage(Text.translatable("immortality.commands.use_auto_completed_commands"), false);
                        });
                    } else {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.playerOnly"), false);
                    }
                    return 1;
                }))
                .then(CommandManager.literal("stats").executes((context -> {
                    if (context.getSource().isExecutedByPlayer()) {
                        ServerPlayerEntity serverPlayerEntity = context.getSource().getPlayer();
                        context.getSource().getServer().execute(() -> {
                            //Only executable by a player (serverside)
                            if (!(serverPlayerEntity instanceof ServerPlayerEntity)) return;
                            //Declarations
                            int bonusHearts = getInt(serverPlayerEntity, DataTypeInt.BonusHearts);
                            int negativeHearts = getInt(serverPlayerEntity, DataTypeInt.TemporaryNegativeHearts);

                            int bonusArmor = getInt(serverPlayerEntity, DataTypeInt.BonusArmor);
                            int bonusArmorToughness = getInt(serverPlayerEntity, DataTypeInt.BonusArmorToughness);

                            int immortalDeaths = getInt(serverPlayerEntity, DataTypeInt.ImmortalDeaths);

                            boolean isDeltaImmortal = getBool(serverPlayerEntity, DataTypeBool.DeltaImmortality);
                            boolean isGammaImmortal = getBool(serverPlayerEntity, DataTypeBool.GammaImmortality);
                            boolean isBetaImmortal = getBool(serverPlayerEntity, DataTypeBool.BetaImmortality);
                            boolean isAlphaImmortal = getBool(serverPlayerEntity, DataTypeBool.AlphaImmortality);

                            boolean hasVoidHeart = getBool(serverPlayerEntity, DataTypeBool.VoidHeart);

                            if (bonusHearts != 0) context.getSource().sendFeedback(Text.translatable("immortality.commands.lifeElixirHearts", bonusHearts), false);
                            if (negativeHearts != 0) context.getSource().sendFeedback(Text.translatable(isGammaImmortal ? "immortality.commands.negativeHearts_Semi" : "immortality.commands.negativeHearts", negativeHearts), false);
                            if (bonusArmor != 0) context.getSource().sendFeedback(Text.translatable("immortality.commands.bonusArmor", bonusArmor), false);
                            if (bonusArmorToughness != 0) context.getSource().sendFeedback(Text.translatable("immortality.commands.bonusArmorT", bonusArmorToughness), false);
                            if (hasVoidHeart) context.getSource().sendFeedback(Text.translatable("immortality.commands.void_heart"), false);
                            if (isGammaImmortal) context.getSource().sendFeedback(Text.translatable("immortality.commands.semi_immortality"), false);
                            //Going through exclusive Immortality Types
                            if (isDeltaImmortal) context.getSource().sendFeedback(Text.translatable("immortality.commands.false_immortality"), false);
                                //context.getSource().sendFeedback(Text.translatable("immortality.commands.needed_successful_lifeElixir", ((20 - ImmortalityStatus.getBonusHearts(playerEntity)) / ImmortalityStatus.lifeElixirHealth)), false);
                            else if (isBetaImmortal) context.getSource().sendFeedback(Text.translatable("immortality.commands.immortality"), false);
                            else if (isAlphaImmortal) context.getSource().sendFeedback(Text.translatable("immortality.commands.trinity"), false);
                            else context.getSource().sendFeedback(Text.translatable("immortality.commands.not_immortal"), false);
                            //Immortal Deaths
                            if (immortalDeaths > 0) context.getSource().sendFeedback(Text.translatable("immortality.commands.prevented_deaths", immortalDeaths), false);
                        });
                    } else {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.playerOnly"), false);
                    }
                    return 1;
                })))
        );
    }
}
