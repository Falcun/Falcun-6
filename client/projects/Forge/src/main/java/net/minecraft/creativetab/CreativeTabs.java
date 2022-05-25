package net.minecraft.creativetab;

import java.util.List;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class CreativeTabs
{
    public static CreativeTabs[] creativeTabArray = new CreativeTabs[12];
    public static final CreativeTabs tabBlock = new CreativeTabs(0, "buildingBlocks")
    {
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return Item.getItemFromBlock(Blocks.brick_block);
        }
    };
    public static final CreativeTabs tabDecorations = new CreativeTabs(1, "decorations")
    {
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return Item.getItemFromBlock(Blocks.double_plant);
        }
        @SideOnly(Side.CLIENT)
        public int getIconItemDamage()
        {
            return BlockDoublePlant.EnumPlantType.PAEONIA.getMeta();
        }
    };
    public static final CreativeTabs tabRedstone = new CreativeTabs(2, "redstone")
    {
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return Items.redstone;
        }
    };
    public static final CreativeTabs tabTransport = new CreativeTabs(3, "transportation")
    {
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return Item.getItemFromBlock(Blocks.golden_rail);
        }
    };
    public static final CreativeTabs tabMisc = (new CreativeTabs(4, "misc")
    {
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return Items.lava_bucket;
        }
    }).setRelevantEnchantmentTypes(new EnumEnchantmentType[] {EnumEnchantmentType.ALL});
    public static final CreativeTabs tabAllSearch = (new CreativeTabs(5, "search")
    {
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return Items.compass;
        }
    }).setBackgroundImageName("item_search.png");
    public static final CreativeTabs tabFood = new CreativeTabs(6, "food")
    {
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return Items.apple;
        }
    };
    public static final CreativeTabs tabTools = (new CreativeTabs(7, "tools")
    {
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return Items.iron_axe;
        }
    }).setRelevantEnchantmentTypes(new EnumEnchantmentType[] {EnumEnchantmentType.DIGGER, EnumEnchantmentType.FISHING_ROD, EnumEnchantmentType.BREAKABLE});
    public static final CreativeTabs tabCombat = (new CreativeTabs(8, "combat")
    {
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return Items.golden_sword;
        }
    }).setRelevantEnchantmentTypes(new EnumEnchantmentType[] {EnumEnchantmentType.ARMOR, EnumEnchantmentType.ARMOR_FEET, EnumEnchantmentType.ARMOR_HEAD, EnumEnchantmentType.ARMOR_LEGS, EnumEnchantmentType.ARMOR_TORSO, EnumEnchantmentType.BOW, EnumEnchantmentType.WEAPON});
    public static final CreativeTabs tabBrewing = new CreativeTabs(9, "brewing")
    {
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return Items.potionitem;
        }
    };
    public static final CreativeTabs tabMaterials = new CreativeTabs(10, "materials")
    {
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return Items.stick;
        }
    };
    public static final CreativeTabs tabInventory = (new CreativeTabs(11, "inventory")
    {
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return Item.getItemFromBlock(Blocks.chest);
        }
    }).setBackgroundImageName("inventory.png").setNoScrollbar().setNoTitle();
    private final int tabIndex;
    private final String tabLabel;
    private String theTexture = "items.png";
    private boolean hasScrollbar = true;
    private boolean drawTitle = true;
    private EnumEnchantmentType[] enchantmentTypes;
    @SideOnly(Side.CLIENT)
    private ItemStack iconItemStack;

    public CreativeTabs(String label)
    {
        this(getNextID(), label);
    }

    public CreativeTabs(int index, String label)
    {
        if (index >= creativeTabArray.length)
        {
            CreativeTabs[] tmp = new CreativeTabs[index + 1];
            for (int x = 0; x < creativeTabArray.length; x++)
            {
                tmp[x] = creativeTabArray[x];
            }
            creativeTabArray = tmp;
        }
        this.tabIndex = index;
        this.tabLabel = label;
        creativeTabArray[index] = this;
    }

    @SideOnly(Side.CLIENT)
    public int getTabIndex()
    {
        return this.tabIndex;
    }

    public CreativeTabs setBackgroundImageName(String texture)
    {
        this.theTexture = texture;
        return this;
    }

    @SideOnly(Side.CLIENT)
    public String getTabLabel()
    {
        return this.tabLabel;
    }

    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel()
    {
        return "itemGroup." + this.getTabLabel();
    }

    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack()
    {
        if (this.iconItemStack == null)
        {
            this.iconItemStack = new ItemStack(this.getTabIconItem(), 1, this.getIconItemDamage());
        }

        return this.iconItemStack;
    }

    @SideOnly(Side.CLIENT)
    public abstract Item getTabIconItem();

    @SideOnly(Side.CLIENT)
    public int getIconItemDamage()
    {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public String getBackgroundImageName()
    {
        return this.theTexture;
    }

    @SideOnly(Side.CLIENT)
    public boolean drawInForegroundOfTab()
    {
        return this.drawTitle;
    }

    public CreativeTabs setNoTitle()
    {
        this.drawTitle = false;
        return this;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldHidePlayerInventory()
    {
        return this.hasScrollbar;
    }

    public CreativeTabs setNoScrollbar()
    {
        this.hasScrollbar = false;
        return this;
    }

    @SideOnly(Side.CLIENT)
    public int getTabColumn()
    {
        if (tabIndex > 11)
        {
            return ((tabIndex - 12) % 10) % 5;
        }
        return this.tabIndex % 6;
    }

    @SideOnly(Side.CLIENT)
    public boolean isTabInFirstRow()
    {
        if (tabIndex > 11)
        {
            return ((tabIndex - 12) % 10) < 5;
        }
        return this.tabIndex < 6;
    }

    @SideOnly(Side.CLIENT)
    public EnumEnchantmentType[] getRelevantEnchantmentTypes()
    {
        return this.enchantmentTypes;
    }

    public CreativeTabs setRelevantEnchantmentTypes(EnumEnchantmentType... types)
    {
        this.enchantmentTypes = types;
        return this;
    }

    @SideOnly(Side.CLIENT)
    public boolean hasRelevantEnchantmentType(EnumEnchantmentType enchantmentType)
    {
        if (this.enchantmentTypes == null)
        {
            return false;
        }
        else
        {
            for (EnumEnchantmentType enumenchantmenttype : this.enchantmentTypes)
            {
                if (enumenchantmenttype == enchantmentType)
                {
                    return true;
                }
            }

            return false;
        }
    }

    @SideOnly(Side.CLIENT)
    public void displayAllReleventItems(List<ItemStack> p_78018_1_)
    {
        for (Item item : Item.itemRegistry)
        {
            if (item == null)
            {
                continue;
            }
            for (CreativeTabs tab : item.getCreativeTabs())
            {
                if (tab == this)
                {
                    item.getSubItems(item, this, p_78018_1_);
                }
            }
        }

        if (this.getRelevantEnchantmentTypes() != null)
        {
            this.addEnchantmentBooksToList(p_78018_1_, this.getRelevantEnchantmentTypes());
        }
    }

    @SideOnly(Side.CLIENT)
    public void addEnchantmentBooksToList(List<ItemStack> itemList, EnumEnchantmentType... enchantmentType)
    {
        for (Enchantment enchantment : Enchantment.enchantmentsBookList)
        {
            if (enchantment != null && enchantment.type != null)
            {
                boolean flag = false;

                for (int i = 0; i < enchantmentType.length && !flag; ++i)
                {
                    if (enchantment.type == enchantmentType[i])
                    {
                        flag = true;
                    }
                }

                if (flag)
                {
                    itemList.add(Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, enchantment.getMaxLevel())));
                }
            }
        }
    }

    public int getTabPage()
    {
        if (tabIndex > 11)
        {
            return ((tabIndex - 12) / 10) + 1;
        }
        return 0;
    }

    public static int getNextID()
    {
        return creativeTabArray.length;
    }

    /**
     * Determines if the search bar should be shown for this tab.
     *
     * @return True to show the bar
     */
    public boolean hasSearchBar()
    {
        return tabIndex == CreativeTabs.tabAllSearch.tabIndex;
    }

    /**
     * Gets the width of the search bar of the creative tab, use this if your
     * creative tab name overflows together with a custom texture.
     *
     * @return The width of the search bar, 89 by default
     */
    public int getSearchbarWidth()
    {
        return 89;
    }
}