package thedarkcolour.futuremc.world.functions;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootFunction;
import net.minecraft.world.storage.loot.LootParameter;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraft.world.storage.loot.functions.ILootFunction;

import java.util.Set;

public class CopyState extends LootFunction {
    private final Block block;
    private final Set<IProperty<?>> properties;

    public CopyState(ILootCondition[] conditionsIn, Block block, Set<IProperty<?>> properties) {
        super(conditionsIn);
        this.block = block;
        this.properties = properties;
    }

    @Override
    public Set<LootParameter<?>> getRequiredParameters() {
        return ImmutableSet.of(LootParameters.BLOCK_STATE);
    }

    @Override
    protected ItemStack doApply(ItemStack stack, LootContext context) {
        BlockState state = context.get(LootParameters.BLOCK_STATE);
        if (state != null) {
            CompoundNBT tag = stack.getOrCreateTag();
            CompoundNBT blockStateTag;
            if (tag.contains("BlockStateTag", 10)) {
                blockStateTag = tag.getCompound("BlockStateTag");
            } else {
                blockStateTag = new CompoundNBT();
                tag.put("BlockStateTag", blockStateTag);
            }

            properties.stream().filter(state::has).forEach(property -> blockStateTag.putString(property.getName(), valueName(state, property)));
        }

        return stack;
    }

    private static <T extends Comparable<T>> String valueName(BlockState blockState_1, IProperty<T> property) {
        T value = blockState_1.get(property);
        return property.getName(value);
    }

    public static class Serializer extends LootFunction.Serializer<CopyState> {
        public Serializer() {
            super(new ResourceLocation("minecraft:copy_state"), CopyState.class);
            System.out.println("Added serializer for CopyState");
        }

        @Override
        public CopyState deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn) {
            ResourceLocation resourceLocation = new ResourceLocation(JSONUtils.getString(object, "block"));
            Block block = Registry.BLOCK.getValue(resourceLocation).orElseThrow(() -> new IllegalArgumentException("Can't find block " + resourceLocation));
            StateContainer<Block, BlockState> container = block.getStateContainer();
            Set<IProperty<?>> set = Sets.newHashSet();
            JsonArray array = JSONUtils.getJsonArray(object, "properties", null);
            if (array != null) {
                array.forEach((jsonElement_1) -> set.add(container.getProperty(JSONUtils.getString(jsonElement_1, "property"))));
            }

            return new CopyState(conditionsIn, block, set);
        }
    }

    public static class Builder extends LootFunction.Builder<CopyState.Builder> {
        private final Block block;
        private final Set<IProperty<?>> properties;

        public Builder(Block block) {
            this.block = block;
            this.properties = Sets.newHashSet();
        }

        @Override
        public ILootFunction build() {
            return new CopyState(getConditions(), block, properties);
        }

        @Override
        protected Builder doCast() {
            return this;
        }
    }
}