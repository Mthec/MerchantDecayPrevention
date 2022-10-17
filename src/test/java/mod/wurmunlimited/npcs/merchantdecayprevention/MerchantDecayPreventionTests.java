package mod.wurmunlimited.npcs.merchantdecayprevention;

import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.CreatureTemplate;
import com.wurmonline.server.creatures.CreatureTemplateIds;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import mod.wurmunlimited.WurmObjectsFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MerchantDecayPreventionTests {
    private WurmObjectsFactory factory;

    @BeforeEach
    void setUp() throws Throwable {
        factory = new WurmObjectsFactory();
    }

    @Test
    void testPollItemsOnNpcTradersPreventDecay() throws Throwable {
        MerchantDecayPreventionMod mod = new MerchantDecayPreventionMod();
        InvocationHandler handler = mod::pollOwned;
        Method method = mock(Method.class);
        when(method.invoke(any(Item.class), any())).thenAnswer((Answer<Boolean>)i -> {
            Item first = ((Item)i.getArgument(0)).getFirstContainedItem();
            if (first != null)
                first.setDamage(50);
            return true;
        });

        Creature merchant = factory.createNewMerchant(factory.createNewPlayer());
        merchant.getInventory().insertItem(factory.createNewItem(ItemList.pickAxe));
        Creature trader = factory.createNewTrader();
        trader.getInventory().insertItem(factory.createNewItem(ItemList.pickAxe));

        for (Creature creature : new Creature[] { merchant, trader }) {
            Object[] args = new Object[] { creature };
            assert creature.getInventory().getItems().size() == 1;
            assertEquals(false, handler.invoke(creature.getInventory(), method, args));
            verify(method, never()).invoke(creature.getInventory(), args);
            assertEquals(0, creature.getInventory().getFirstContainedItem().getDamage());
        }
    }

    @Test
    void testPollItemsOnCreatureDoesNotPreventDecay() throws Throwable {
        MerchantDecayPreventionMod mod = new MerchantDecayPreventionMod();
        InvocationHandler handler = mod::pollOwned;

        Creature horse = factory.createNewCreature(CreatureTemplate.HORSE_CID);
        horse.getInventory().insertItem(factory.createNewItem(ItemList.pickAxe));
        Creature player = factory.createNewPlayer();
        player.getInventory().insertItem(factory.createNewItem(ItemList.pickAxe));

        for (Creature creature : new Creature[] { horse, player }) {
            Method method = mock(Method.class);
            when(method.invoke(any(Item.class), any())).thenAnswer((Answer<Boolean>)i -> {
                Item first = ((Item)i.getArgument(0)).getFirstContainedItem();
                if (first != null)
                    first.setDamage(50);
                return false;
            });
            Object[] args = new Object[] { creature };
            assert creature.getInventory().getItems().size() == 1;
            assertEquals(false, handler.invoke(creature.getInventory(), method, args));
            verify(method, times(1)).invoke(creature.getInventory(), args);
            assertEquals(50, creature.getInventory().getFirstContainedItem().getDamage());
        }
    }

    @Test
    void testAllowCoolingTrue() throws Throwable {
        MerchantDecayPreventionMod mod = new MerchantDecayPreventionMod();
        InvocationHandler handler = mod::pollOwned;
        Method method = mock(Method.class);
        Properties properties = new Properties();
        properties.setProperty("allow_cooling", "true");
        mod.configure(properties);

        short startTemperature = 250;
        Creature merchant = factory.createNewMerchant(factory.createNewPlayer());
        merchant.getInventory().insertItem(factory.createNewItem(ItemList.pickAxe));
        merchant.getInventory().getFirstContainedItem().setTemperature(startTemperature);
        Creature trader = factory.createNewTrader();
        trader.getInventory().insertItem(factory.createNewItem(ItemList.pickAxe));
        trader.getInventory().getFirstContainedItem().setTemperature(startTemperature);

        for (Creature creature : new Creature[] { merchant, trader }) {
            Object[] args = new Object[] { creature };
            assert creature.getInventory().getItems().size() == 1;
            handler.invoke(creature.getInventory(), method, args);
            assertTrue(creature.getInventory().getFirstContainedItem().getTemperature() < startTemperature,
                    creature.getInventory().getFirstContainedItem().getTemperature() + " is not less than " + startTemperature);
        }
    }

    @Test
    void testAllowCoolingFalse() throws Throwable {
        MerchantDecayPreventionMod mod = new MerchantDecayPreventionMod();
        InvocationHandler handler = mod::pollOwned;
        Method method = mock(Method.class);
        Properties properties = new Properties();
        properties.setProperty("allow_cooling", "false");
        mod.configure(properties);

        short startTemperature = 250;
        Creature merchant = factory.createNewMerchant(factory.createNewPlayer());
        merchant.getInventory().insertItem(factory.createNewItem(ItemList.pickAxe));
        merchant.getInventory().getFirstContainedItem().setTemperature(startTemperature);
        Creature trader = factory.createNewTrader();
        trader.getInventory().insertItem(factory.createNewItem(ItemList.pickAxe));
        trader.getInventory().getFirstContainedItem().setTemperature(startTemperature);

        for (Creature creature : new Creature[] { merchant, trader }) {
            Object[] args = new Object[] { creature };
            assert creature.getInventory().getItems().size() == 1;
            handler.invoke(creature.getInventory(), method, args);
            assertEquals(startTemperature, creature.getInventory().getFirstContainedItem().getTemperature());
        }
    }

    @Test
    void testAllowCoolingFalseNonMerchant() throws Throwable {
        MerchantDecayPreventionMod mod = new MerchantDecayPreventionMod();
        InvocationHandler handler = mod::pollOwned;
        Method method = mock(Method.class);
        Properties properties = new Properties();
        properties.setProperty("allow_cooling", "false");
        mod.configure(properties);

        short startTemperature = 250;
        Creature notMerchant = factory.createNewPlayer();
        notMerchant.getInventory().insertItem(factory.createNewItem(ItemList.pickAxe));
        notMerchant.getInventory().getFirstContainedItem().setTemperature(startTemperature);
        Creature trader = factory.createNewTrader();
        trader.getInventory().insertItem(factory.createNewItem(ItemList.pickAxe));
        trader.getInventory().getFirstContainedItem().setTemperature(startTemperature);
        when(method.invoke(any(Item.class), any())).then((Answer<Void>)invocation -> {
            Item inventory = ((Creature)invocation.getArgument(1)).getInventory();
            for (Item item : inventory.getItems()) {
                item.setTemperature((short)(item.getTemperature() - 1));
            }
            return null;
        });

        for (Creature creature : new Creature[] { notMerchant }) {
            Object[] args = new Object[] { creature };
            assert creature.getInventory().getItems().size() == 1;
            handler.invoke(creature.getInventory(), method, args);
            assertTrue(creature.getInventory().getFirstContainedItem().getTemperature() < startTemperature,
                    creature.getInventory().getFirstContainedItem().getTemperature() + " is not less than " + startTemperature);
        }
    }

    @Test
    void testAllowCoolingPollTrue() throws Throwable {
        MerchantDecayPreventionMod mod = new MerchantDecayPreventionMod();
        InvocationHandler handler = mod::pollCoolingItems;
        Method method = mock(Method.class);
        Properties properties = new Properties();
        properties.setProperty("allow_cooling", "true");
        mod.configure(properties);

        short startTemperature = 250;
        Creature merchant = factory.createNewMerchant(factory.createNewPlayer());
        merchant.getInventory().insertItem(factory.createNewItem(ItemList.pickAxe));
        merchant.getInventory().getFirstContainedItem().setTemperature(startTemperature);
        Creature trader = factory.createNewTrader();
        trader.getInventory().insertItem(factory.createNewItem(ItemList.pickAxe));
        trader.getInventory().getFirstContainedItem().setTemperature(startTemperature);
        when(method.invoke(any(Item.class), any())).then((Answer<Void>)invocation -> {
            Item inventory = ((Creature)invocation.getArgument(1)).getInventory();
            for (Item item : inventory.getItems()) {
                item.setTemperature((short)(item.getTemperature() - 1));
            }
            return null;
        });

        for (Creature creature : new Creature[] { merchant, trader }) {
            Object[] args = new Object[] { creature, 10L };
            assert creature.getInventory().getItems().size() == 1;
            handler.invoke(creature.getInventory(), method, args);
            assertTrue(creature.getInventory().getFirstContainedItem().getTemperature() < startTemperature,
                    creature.getInventory().getFirstContainedItem().getTemperature() + " is not less than " + startTemperature);
        }
    }

    @Test
    void testAllowCoolingPollFalse() throws Throwable {
        MerchantDecayPreventionMod mod = new MerchantDecayPreventionMod();
        InvocationHandler handler = mod::pollCoolingItems;
        Method method = mock(Method.class);
        Properties properties = new Properties();
        properties.setProperty("allow_cooling", "false");
        mod.configure(properties);

        short startTemperature = 250;
        Creature merchant = factory.createNewMerchant(factory.createNewPlayer());
        merchant.getInventory().insertItem(factory.createNewItem(ItemList.pickAxe));
        merchant.getInventory().getFirstContainedItem().setTemperature(startTemperature);
        Creature trader = factory.createNewTrader();
        trader.getInventory().insertItem(factory.createNewItem(ItemList.pickAxe));
        trader.getInventory().getFirstContainedItem().setTemperature(startTemperature);
        when(method.invoke(any(Item.class), any())).then((Answer<Void>)invocation -> {
            Item inventory = ((Creature)invocation.getArgument(1)).getInventory();
            for (Item item : inventory.getItems()) {
                item.setTemperature((short)(item.getTemperature() - 1));
            }
            return null;
        });

        for (Creature creature : new Creature[] { merchant, trader }) {
            Object[] args = new Object[] { creature, 10L };
            assert creature.getInventory().getItems().size() == 1;
            handler.invoke(creature.getInventory(), method, args);
            assertEquals(startTemperature, creature.getInventory().getFirstContainedItem().getTemperature());
        }
    }

    @Test
    void testAllowCoolingPollFalseNonMerchant() throws Throwable {
        MerchantDecayPreventionMod mod = new MerchantDecayPreventionMod();
        InvocationHandler handler = mod::pollCoolingItems;
        Method method = mock(Method.class);
        Properties properties = new Properties();
        properties.setProperty("allow_cooling", "false");
        mod.configure(properties);

        short startTemperature = 250;
        Creature nonMerchant = factory.createNewPlayer();
        nonMerchant.getInventory().insertItem(factory.createNewItem(ItemList.pickAxe));
        nonMerchant.getInventory().getFirstContainedItem().setTemperature(startTemperature);
        Creature trader = factory.createNewTrader();
        trader.getInventory().insertItem(factory.createNewItem(ItemList.pickAxe));
        trader.getInventory().getFirstContainedItem().setTemperature(startTemperature);
        when(method.invoke(any(Item.class), any())).then((Answer<Void>)invocation -> {
            Item inventory = ((Creature)invocation.getArgument(1)).getInventory();
            for (Item item : inventory.getItems()) {
                item.setTemperature((short)(item.getTemperature() - 1));
            }
            return null;
        });

        for (Creature creature : new Creature[] { nonMerchant }) {
            Object[] args = new Object[] { creature, 10L };
            assert creature.getInventory().getItems().size() == 1;
            handler.invoke(creature.getInventory(), method, args);
            assertTrue(creature.getInventory().getFirstContainedItem().getTemperature() < startTemperature,
                    creature.getInventory().getFirstContainedItem().getTemperature() + " is not less than " + startTemperature);
        }
    }
}
