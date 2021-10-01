package mod.wurmunlimited;

import com.wurmonline.math.TilePos;
import com.wurmonline.mesh.MeshIO;
import com.wurmonline.server.*;
import com.wurmonline.server.banks.Bank;
import com.wurmonline.server.banks.BankSlot;
import com.wurmonline.server.banks.Banks;
import com.wurmonline.server.creatures.*;
import com.wurmonline.server.economy.*;
import com.wurmonline.server.items.*;
import com.wurmonline.server.kingdom.Kingdoms;
import com.wurmonline.server.players.FakePlayerInfo;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.villages.NoSuchRoleException;
import com.wurmonline.server.villages.Village;
import com.wurmonline.server.villages.VillageStatus;
import com.wurmonline.server.villages.Villages;
import com.wurmonline.server.zones.Zones;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modsupport.actions.ActionEntryBuilder;
import org.jetbrains.annotations.NotNull;
import org.mockito.stubbing.Answer;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class WurmObjectsFactory {
    private static final List<Character> alphabet = IntStream.rangeClosed('a', 'z').mapToObj(c -> (char)c).collect(Collectors.toList());
    private static final List<Iterator<Character>> chars = new ArrayList<>();
    protected Map<Long, Creature> creatures = new HashMap<>(10);
    private final Map<Creature, FakeCommunicator> communicators = new HashMap<>(10);
    protected Map<Creature, FakeShop> shops = new HashMap<>(4);
    private final Map<Long, Item> items = new HashMap<>();
    private static WurmObjectsFactory current;
    private static int villageIds = 0;

    public enum AnimalTraitColours {
        brown(15),
        gold(16),
        black(17),
        white(18),
        ebony_black(23),
        piebald_pinto(24),
        blood_bay(25),
        skewbald_pinto(30),
        gold_buckskin(31),
        black_silver(32),
        appaloosa(33),
        chestnut(34);

        private final int bit;

        AnimalTraitColours(int bit) {
            this.bit = bit;
        }

        public int getNumber() {
            return bit;
        }

        public String getName() {
            return name().replace("_", " ");
        }
    }

    static {
        try {
            ItemTemplateCreator.initialiseItemTemplates();
            CreatureTemplateCreator.createCreatureTemplates();
            CreationEntryCreator.createCreationEntries();
            Constants.dbHost = ".";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public WurmObjectsFactory() throws Exception {
        Zones.resetStatic();
        Arrays.stream(Villages.getVillages()).forEach(village -> village.disband("upkeep"));
        WurmMail.resetStatic();
        Economy economy = mock(Economy.class);
        Field econ = Economy.class.getDeclaredField("economy");
        econ.setAccessible(true);
        econ.set(null, economy);
        assert Economy.getEconomy() == economy;
        when(economy.getShop(any(Creature.class))).thenAnswer(i -> {
            Creature creature = i.getArgument(0);
            FakeShop shop = shops.get(creature);

            if (shop == null && !creature.isPlayer()) {
                shop = FakeShop.createFakeTraderShop(((Creature)i.getArgument(0)).getWurmId());
                shops.put(creature, shop);
                return shop;
            }

            return shop;
        });
        when(economy.getShop(any(Creature.class), anyBoolean())).thenAnswer(i -> {
            Creature creature = i.getArgument(0);
            FakeShop shop = shops.get(creature);

            if (shop == null && !creature.isPlayer() && !(boolean)i.getArgument(1)) {
                shop = FakeShop.createFakeTraderShop(((Creature)i.getArgument(0)).getWurmId());
                shops.put(creature, shop);
                return shop;
            }

            return shop;
        });
        shops.put(null, FakeShop.createFakeShop());
        when(economy.getKingsShop()).thenAnswer(i -> shops.get(null));
        doAnswer((Answer<Void>) i -> {
            Item coin = i.getArgument(0);

            coin.setTradeWindow(null);
            coin.setOwner(-10L, false);
            coin.setLastOwnerId(-10L);
            coin.setZoneId(-10, true);
            coin.setParentId(-10L, true);
            coin.setRarity((byte)0);
            coin.setBanked(true);
            return null;
        }).when(economy).returnCoin(any(Item.class), anyString());
        when(economy.getCoinsFor(anyLong())).thenAnswer((Answer<Item[]>) i -> {
            long total = i.getArgument(0);

            Set<Item> set = new HashSet<>();

            while (total >= MonetaryConstants.COIN_SILVER) {
                set.add(createNewSilverCoin());
                total -= MonetaryConstants.COIN_SILVER;
            }

            while (total >= 100) {
                set.add(createNewCopperCoin());
                total -= 100;
            }

            while (total >= 20) {
                set.add(createNewItem(ItemList.coinIronTwenty));
                total -= 20;
            }

            while (total >= 5) {
                set.add(createNewItem(ItemList.coinIronFive));
                total -= 5;
            }

            while (total > 0) {
                set.add(createNewIronCoin());
                total -= 1;
            }

            return set.toArray(new Item[0]);
        });
        when(economy.getChangeFor(anyLong())).thenAnswer(i -> new Change(i.getArgument(0)));
        when(economy.createShop(anyLong(), anyLong())).thenAnswer(i -> {
            Creature creature = creatures.get((long)i.getArgument(0));
            if (creature.isPlayer()) {
                return null;
            }
            FakeShop shop = FakeShop.createFakeShop(creature, creatures.get((long)i.getArgument(1)));
            shops.put(creature, shop);
            return shop;
        });
        when(economy.getShops()).thenReturn(shops.values().toArray(new FakeShop[0]));
        Field economyShops = Economy.class.getDeclaredField("shops");
        economyShops.setAccessible(true);
        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(economyShops, economyShops.getModifiers() & ~Modifier.FINAL);
        economyShops.set(null, new HashMap<Long, Shop>() {
            @NotNull
            @Override
            public Collection<Shop> values() {
                return new ArrayList<>(shops.values());
            }
        });
        ReflectionUtil.<Map<Long, Bank>>getPrivateField(null, Banks.class.getDeclaredField("banks")).clear();
        Kingdoms.createBasicKingdoms();

        Server.caveMesh = mock(MeshIO.class);
        Server.surfaceMesh = mock(MeshIO.class);

        ActionEntryBuilder.init();

        current = this;
    }

    protected static String randomName(String baseName) {
        if (chars.isEmpty()) {
            chars.add(alphabet.iterator());
        }

        int idx = 0;
        Iterator<Character> toAdd = null;
        List<String> characters = new ArrayList<>();
        while (idx != chars.size()) {
            Iterator<Character> iterator = chars.get(idx++);
            if (iterator.hasNext()) {
                characters.add(iterator.next().toString());
            } else if (idx == chars.size()) {
                chars.add(alphabet.iterator());
            } else {
                chars.set(idx, alphabet.iterator());
            }
        }

        return baseName + String.join("", characters);
    }

    public void addCreature(Creature creature) {
        creatures.put(creature.getWurmId(), creature);
    }

    public static WurmObjectsFactory getCurrent() {
        return current;
    }

    public String[] getMessagesFor(Creature creature) {
        return communicators.get(creature).getMessages();
    }

    public void attachFakeCommunicator(Creature creature) {
        FakeCommunicator newCom = new FakeCommunicator(creature);
        creature.setCommunicator(newCom);
        communicators.put(creature, newCom);
    }

    public void addShop(Creature creature, FakeShop shop) {
        shops.put(creature, shop);
    }

    public Creature createNewCreature() {
        return createNewCreature(CreatureTemplateIds.HORSE_CID);
    }

    public Creature createNewCreature(int creatureTemplateId) {
        try {
            Creature creature = Creature.doNew(creatureTemplateId, 512, 512, 1, 1, randomName("Creature"), (byte)0);
            creatures.put(creature.getWurmId(), creature);
            creature.currentTile = Zones.getOrCreateTile(512, 512, true);
            creature.createPossessions();
            attachFakeCommunicator(creature);
            return creature;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Player createNewPlayer() {
        try {
            Player player = Player.doNewPlayer(CreatureTemplateIds.HUMAN_CID);
            player.setName(randomName("Player_"));
            player.setWurmId(WurmId.getNextPlayerId(), 512, 512, 1, 1);
            ServerPackageFactory.addPlayer(player);
            creatures.put(player.getWurmId(), player);
            player.currentTile = Zones.getOrCreateTile(512, 512, true);
            ReflectionUtil.setPrivateField(player, Creature.class.getDeclaredField("status"), new FakeCreatureStatus(player));
            player.getBody().createBodyParts();
            ReflectionUtil.setPrivateField(player, Player.class.getDeclaredField("saveFile"), new FakePlayerInfo(player.getWurmId(), player.getName()));
            player.createPossessions();
            attachFakeCommunicator(player);
            return player;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Creature createNewTrader() {
        Creature trader;
        try {
            trader = Creature.doNew(CreatureTemplateIds.SALESMAN_CID, 512, 517, 180.0f, 1, "Trader_" + randomName(""), (byte)0);
            creatures.put(trader.getWurmId(), trader);
            trader.currentTile = Zones.getOrCreateTile(512, 517, true);
            trader.createPossessions();
            attachFakeCommunicator(trader);

            shops.put(trader, FakeShop.createFakeTraderShop(trader.getWurmId()));
            return trader;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Creature createNewMerchant(Creature owner) {
        Creature merchant;
        try {
            merchant = Creature.doNew(CreatureTemplateIds.SALESMAN_CID, 512, 517, 180.0f, 1, "Merchant_" + randomName(""), (byte)0);
            creatures.put(merchant.getWurmId(), merchant);
            merchant.currentTile = Zones.getOrCreateTile(512, 517, true);
            merchant.createPossessions();
            attachFakeCommunicator(merchant);

            shops.put(merchant, FakeShop.createFakeShop(merchant, owner));
            return merchant;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Creature getCreature(long id) throws NoSuchCreatureException {
        if (!creatures.containsKey(id))
            throw new NoSuchCreatureException("");
        return creatures.get(id);
    }

    public Creature getCreature(String name) throws NoSuchCreatureException {
        Optional<Creature> creature = creatures.values().stream().filter(c -> c.getName().equals(name)).findAny();
        if (!creature.isPresent())
            throw new NoSuchCreatureException("");
        return creature.get();
    }

    public Collection<Creature> getAllCreatures() {
        return creatures.values();
    }

    public void removeCreature(Creature creature) {
        creatures.remove(creature.getWurmId());
    }

    public FakeShop getShop(Creature creature) {
        return shops.get(creature);
    }

    public FakeCommunicator getCommunicator(Creature creature) {
        return communicators.get(creature);
    }

    public Item createNewItem() {
        return createNewItem(ItemList.rake);
    }

    public Item createNewItem(int templateId) {
        Item item;
        try {
            item = ItemsPackageFactory.getTempItem(WurmId.getNextItemId());
            item.setTemplateId(templateId);
            item.setMaterial(ItemTemplateFactory.getInstance().getTemplate(templateId).getMaterial());
            item.setQualityLevel(1.0f);
            ReflectionUtil.setPrivateField(item, Item.class.getDeclaredField("weight"), ItemTemplateFactory.getInstance().getTemplate(templateId).getWeightGrams());
            item.setPosXYZRotation(512, 512, 1, 90);

            if (item.canHaveInscription()) {
                ReflectionUtil.setPrivateField(item, Item.class.getDeclaredField("inscription"), new InscriptionData(item.getWurmId(), "", "", 0));
            }

            items.put(item.getWurmId(), item);
            Items.putItem(item);
        } catch (NoSuchFieldException | NoSuchTemplateException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return item;
    }

    public Item createNewSilverCoin() {
        return createNewItem(ItemList.coinSilver);
    }

    public Item createNewCopperCoin() {
        return createNewItem(ItemList.coinCopper);
    }

    public Item createNewIronCoin() {
        return createNewItem(ItemList.coinIron);
    }

    /**
        @deprecated Use Economy.getInstance().getCoinsFor() instead.
     */
    @Deprecated
    public Iterable<Item> createManyCopperCoins(int numberOfItems) {
        return () -> new Iterator<Item>() {
            int number = numberOfItems;

            @Override
            public boolean hasNext() {
                return number > 0;
            }

            @Override
            public Item next() {
                --number;
                return createNewCopperCoin();
            }
        };
    }

    public Item createNoTradeItem() {
        Item item = createNewItem(ItemList.wagonerTent);
        assert item.isNoTrade();
        return item;
    }

    public Iterable<Item> createManyItems(int numberOfItems) {
        return createManyItems(numberOfItems, getIsMetalId());
    }

    public Iterable<Item> createManyItems(int numberOfItems, int templateId) {
        return () -> new Iterator<Item>() {
            int number = numberOfItems;

            @Override
            public boolean hasNext() {
                return number > 0;
            }

            @Override
            public Item next() {
                --number;
                return createNewItem(templateId);
            }
        };
    }

    public Item createMarketStallAtCreature(Creature creature) {
        Item item = createNewItem();
        item.setTemplateId(ItemList.marketStall);
        TilePos pos = creature.getTilePos();
        item.setPosXY(pos.x, pos.y);
        return item;
    }

    public void fillItemWith(Item item, int fluidId) {
        ItemTemplate fluid;
        try {
             fluid = ItemTemplateFactory.getInstance().getTemplate(fluidId);
        } catch (NoSuchTemplateException e) {
            throw new RuntimeException(e);
        }
        if (fluid != null && fluid.isLiquid()) {
            Item newFluid = createNewItem(fluidId);
            newFluid.setWeight(item.getFreeVolume(), true);
            item.insertItem(newFluid);
        }
    }

    public Item getItem(long id) {
        return items.get(id);
    }

    public void removeItem(long id) {
        if (!items.containsKey(id)) {
            System.out.println("Not in factory.items, expected?");
            return;
        }
        System.out.println("Decaying " + items.get(id).getName());
        items.remove(id);
    }

    public Village createVillageFor(Creature owner, Creature... villagers) {
        try {
            Village v = Villages.createVillage(owner.getTileX(), owner.getTileX() + 100, owner.getTileY(), owner.getTileY() + 100, 1, 1, "Village" + ++villageIds, owner, createNewItem(ItemList.settlementDeed).getWurmId(),
                    true, false, "", false, owner.currentKingdom, 5);
            // Some permissions don't apply if village was just created.
            setFinalField(v, Village.class.getDeclaredField("creationDate"), System.currentTimeMillis() - (120000L * 2));
            final Item token = ItemFactory.createItem(236, 99.0f, (float)((owner.getTileX() << 2) + 2), (float)((owner.getTileY() << 2) + 2), 180.0f, v.isOnSurface(), (byte)0, -10L, null);
            token.setData2(v.getId());
            v.setTokenId(token.getWurmId());
            v.resetRoles();
            for (Creature villager : villagers)
                v.addCitizen(villager, v.getRoleForStatus(VillageStatus.ROLE_CITIZEN));
            return v;
        } catch (IOException | NoSuchRoleException | NoSuchCreatureException | NoSuchPlayerException | NoSuchItemException | FailedException | NoSuchFieldException | NoSuchTemplateException e) {
            throw new RuntimeException(e);
        }
    }

    public Bank createBankFor(Creature player) {
        assert player.isPlayer();
        Village village = player.getCitizenVillage();
        if (village == null)
            village = createVillageFor(player);
        return createBankFor(player, village);
    }

    public Bank createBankFor(Creature player, Village village) {
        try {
            assert player.isPlayer();
            Objenesis ob = new ObjenesisStd();
            Bank newBank = ob.newInstance(Bank.class);
            newBank.startedMoving = -10L;
            newBank.currentVillage = -10;
            newBank.targetVillage = -10;
            newBank.open = false;
            setFinalField(newBank, Bank.class.getDeclaredField("owner"), player.getWurmId());
            setFinalField(newBank, Bank.class.getDeclaredField("size"), 5);
            setFinalField(newBank, Bank.class.getDeclaredField("currentVillage"), village.getId());
            setFinalField(newBank, Bank.class.getDeclaredField("lastPolled"), System.currentTimeMillis());
            setFinalField(newBank, Bank.class.getDeclaredField("id"), WurmId.getNextBankId());
            setFinalField(newBank, Bank.class.getDeclaredField("slots"), new BankSlot[5]);
            ReflectionUtil.callPrivateMethod(null, Banks.class.getDeclaredMethod("addBank", Bank.class), newBank);

            return newBank;
        } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteBank(Bank bank) {
        try {
            ReflectionUtil.<Map<Long, Bank>>getPrivateField(null, Banks.class.getDeclaredField("banks")).remove(bank.owner);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    // Templates
    public int getIsHollowId() {
        return ItemList.backPack;
    }

    public int getIsMetalId() {
        return ItemList.hatchet;
    }

    public int getIsWoodId() {
        return ItemList.log;
    }

    public int getIsMeatId() {
        return ItemList.cookedMeat;
    }

    public int getIsCoinId() {
        return ItemList.coinCopper;
    }

    public int getIsLockableId() {
        return ItemList.chestSmall;
    }

    public int getIsUnknownMaterial() {
        return ItemList.dough;
    }

    public int getIsDefaultMaterialId() {
        return ItemList.rope;
    }

    public void lockItem(Item item) {
        assert item.isLockable();

        Item lock = createNewItem(ItemList.padLockSmall);
        assert lock.isLock();
        items.put(lock.getWurmId(), lock);

        item.setLockId(lock.getWurmId());
        lock.setLocked(true);
    }

    public static void setFinalField(Object obj, Field field, Object value) {
        try {
            field.setAccessible(true);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(obj, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
