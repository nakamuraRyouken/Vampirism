package de.teamlapen.vampirism.world.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import de.teamlapen.vampirism.util.REFERENCE;
import de.teamlapen.vampirism.util.VampireBookManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import java.util.Random;

/**
 * Loot function that adds a random vampire text to a book stack
 */
public class AddBookNbt extends LootFunction {
    protected AddBookNbt(LootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
        VampireBookManager.getInstance().applyRandomBook(stack, rand);
        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<AddBookNbt> {

        protected Serializer() {
            super(new ResourceLocation(REFERENCE.MODID, "add_book_nbt"), AddBookNbt.class);
        }

        @Override
        public AddBookNbt deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn) {
            return new AddBookNbt(conditionsIn);
        }

        @Override
        public void serialize(JsonObject object, AddBookNbt functionClazz, JsonSerializationContext serializationContext) {

        }
    }
}
