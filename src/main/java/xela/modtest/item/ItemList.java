package xela.modtest.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import xela.modtest.XelaModtest;

public class ItemList {
    public static final Item testItem = ItemHelper.register("test-item", Item::new, new Item.Properties());
    public static final Item friesItem = ItemHelper.register("fries", Item::new, new Item.Properties().food(new FoodProperties.Builder().saturationModifier(15).nutrition(10).build()));
    public static void initialize() {
        XelaModtest.LOGGER.info("Initializing Items");
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.OP_BLOCKS)
                .register((itemGroup) -> itemGroup.accept(testItem));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS)
                .register((itemGroup) -> itemGroup.accept(friesItem));

    }
}
