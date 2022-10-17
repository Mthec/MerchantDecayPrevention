package mod.wurmunlimited.npcs.merchantdecayprevention;

import com.wurmonline.server.WurmCalendar;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.Configurable;
import org.gotti.wurmunlimited.modloader.interfaces.Initable;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

public class MerchantDecayPreventionMod implements WurmServerMod, Configurable, Initable {
    private boolean allowCooling = true;

    @Override
    public void configure(Properties properties) {
        String val = properties.getProperty("allow_cooling");
        allowCooling = val != null && val.equals("true");
    }

    @Override
    public void init() {
        HookManager manager = HookManager.getInstance();

        manager.registerHook("com.wurmonline.server.items.Item",
                "pollOwned",
                "(Lcom/wurmonline/server/creatures/Creature;)Z",
                () -> this::pollOwned);
        manager.registerHook("com.wurmonline.server.items.Item",
                "pollCoolingItems",
                "(Lcom/wurmonline/server/creatures/Creature;J)V",
                () -> this::pollCoolingItems);
    }

    Object pollOwned(Object o, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Creature creature = (Creature)args[0];
        Item item = (Item)o;
        if (creature.isNpcTrader()) {
            Method coolInventoryItem = Item.class.getDeclaredMethod("coolInventoryItem");
            coolInventoryItem.setAccessible(true);

            if (allowCooling) {
                coolInventoryItem.invoke(item);
            }

            for (Item it : item.getAllItems(true)) {
                if (allowCooling) {
                    coolInventoryItem.invoke(it);
                }

                it.setLastMaintained(WurmCalendar.currentTime);
            }

            item.setLastMaintained(WurmCalendar.currentTime);

            return false;
        } else {
            return method.invoke(o, args);
        }
    }

    Object pollCoolingItems(Object o, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Creature creature = (Creature)args[0];
        if (!creature.isNpcTrader() || allowCooling) {
            return method.invoke(o, args);
        }

        return null;
    }
}
