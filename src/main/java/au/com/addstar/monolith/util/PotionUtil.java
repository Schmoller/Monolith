/*
 * Copyright (c) 2020. AddstarMC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 *  and associated documentation files (the "Software"), to deal in the Software without restriction,
 *  including without limitation the rights to use, copy, modify, merge, publish, distribute,
 *  sublicense, and/or copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *
 */

package au.com.addstar.monolith.util;

/*
  Created for use for the Add5tar MC Minecraft server
  Created by benjamincharlton on 27/11/2016.
 */



import au.com.addstar.monolith.Monolith;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.invoke.MethodHandle;
import java.util.Objects;

/**
 * This class lets you get the potion type from a potion item stack, using the NBT tags introduced in Minecraft 1.9
 * <p>
 * Source: https://gist.github.com/forseth11/31b914badd376dbd38e2
 *
 * @author Michael Forseth
 */
public class PotionUtil {
    private PotionType type;
    private boolean strong, extended, linger, splash;

    /**
     * Construct a new potion of the given type.
     *
     * @param potionType The potion type
     */
    public PotionUtil(PotionType potionType) {
        this.type = potionType;
        this.strong = false;
        this.extended = false;
        this.linger = false;
        this.splash = false;
    }

    /**
     * Create a new potion of the given type and strength.
     *
     * @param type   The type of potion.
     * @param strong True if the potion is a strong potion
     */
    public PotionUtil(PotionType type, boolean strong) {
        this(type);
        if (type == null)
            throw new IllegalArgumentException("Type cannot be null");
        if (type != PotionType.WATER)
            throw new IllegalArgumentException("Water bottles cannot be strong!");
        this.strong = strong;
    }

    /**
     * This constructs an instance of PotionInfo.
     *
     * @param type     PotionType
     * @param strong   boolean
     * @param extended is exteneded
     * @param linger   will linger
     * @param splash   is splash
     */
    public PotionUtil(PotionType type, boolean strong, boolean extended, boolean linger, boolean splash) {
        this.type = type;
        this.strong = strong;
        this.extended = extended;
        this.linger = linger;
        this.splash = splash;
    }

    /**
     * This parses an ItemStack into an instance of PotionInfo.
     * This lets you get the potion type, if the potion is strong, if the potion is long,
     * if the potion is lingering, and if the potion is a splash potion.
     *
     * @param item create from this stack
     * @return PotionInfo. If it fails to parse, or the item argument is not a valid potion this will return null.
     */
    public static PotionUtil fromItemStack(ItemStack item) {
        if (item == null) {
            throw new IllegalArgumentException("item cannot be null");
        }

        if (item.getType() != Material.POTION &&
                item.getType() != Material.SPLASH_POTION &&
                item.getType() != Material.LINGERING_POTION) {
            throw new IllegalArgumentException("item is not a potion");
        }
        Class nmsItemStackClass = Crafty.findNmsClass("ItemStack");
        Class craftItemStackClass = Crafty.findCraftClass("inventory.CraftItemStack");
        Class nbtCompoundTagClass = Crafty.findNmsClass("NBTTagCompound");
        try {
            MethodHandle handle = Crafty.findStaticMethod(craftItemStackClass, "asNMSCopy", nmsItemStackClass, ItemStack.class);
            Object nmsStack = Objects.requireNonNull(handle).invokeWithArguments(item);
            MethodHandle tagHandle = Crafty.findMethod(nmsItemStackClass, "getTag", nbtCompoundTagClass);
            Object tagCompound = tagHandle.invoke(nmsStack);
            MethodHandle getString = Crafty.findMethod(nbtCompoundTagClass, "getString", String.class, String.class);
            if (tagCompound != null && ! ((String)getString.invoke(tagCompound,"Potion")).isEmpty()) {
                String tag = ((String)getString.invoke(tagCompound,"Potion")).replace("minecraft:", "");
                PotionType type = null;
                boolean strong = tag.contains("strong");
                boolean extended = tag.contains("long");
                switch (tag) {
                    case "fire_resistance":
                    case "long_fire_resistance":
                        type = PotionType.FIRE_RESISTANCE;
                        break;
                    case "harming":
                    case "strong_harming":
                        type = PotionType.INSTANT_DAMAGE;
                        break;
                    case "healing":
                    case "strong_healing":
                        type = PotionType.INSTANT_HEAL;
                        break;
                    case "invisibility":
                    case "long_invisibility":
                        type = PotionType.INVISIBILITY;
                        break;
                    case "leaping":
                    case "long_leaping":
                    case "strong_leaping":
                        type = PotionType.JUMP;
                        break;
                    case "luck":
                        type = PotionType.LUCK;
                        break;
                    case "night_vision":
                    case "long_night_vision":
                        type = PotionType.NIGHT_VISION;
                        break;
                    case "poison":
                    case "long_poison":
                    case "strong_poison":
                        type = PotionType.POISON;
                        break;
                    case "regeneration":
                    case "long_regeneration":
                    case "strong_regeneration":
                        type = PotionType.REGEN;
                        break;
                    case "slowness":
                    case "long_slowness":
                        type = PotionType.SLOWNESS;
                        break;
                    case "swiftness":
                    case "long_swiftness":
                    case "strong_swiftness":
                        type = PotionType.SPEED;
                        break;
                    case "strength":
                    case "long_strength":
                    case "strong_strength":
                        type = PotionType.STRENGTH;
                        break;
                    case "water_breathing":
                    case "long_water_breathing":
                        type = PotionType.WATER_BREATHING;
                        break;
                    case "water":
                        type = PotionType.WATER;
                        break;
                    case "weakness":
                    case "long_weakness":
                        type = PotionType.WEAKNESS;
                        break;
                    case "empty":
                        type = PotionType.EMPTY;
                        break;
                    case "mundane":
                        type = PotionType.MUNDANE;
                        break;
                    case "thick":
                        type = PotionType.THICK;
                        break;
                    case "awkward":
                        type = PotionType.AWKWARD;
                        break;
                    case "turtle_master":
                    case "long_turtle_master":
                    case "strong_turtle_master":
                        type = PotionType.TURTLE;
                        break;
                    case "slow_falling":
                    case "long_slow_falling":
                        type = PotionType.SLOW_FALLING;
                        break;
                    default:
                        return null;
                }

                return new PotionUtil(type, strong, extended,
                      item.getType() == Material.LINGERING_POTION,
                      item.getType() == Material.SPLASH_POTION);
            } else {
                return null;
            }
        } catch (Throwable t) {
            Monolith.getInstance().getLogger().warning(t.getMessage());
            return null;
        }
    }

    /**
     * Chain this to the constructor to make the potion a splash potion.
     *
     * @return The potion.
     */
    public PotionUtil splash() {
        setSplash(true);
        return this;
    }

    /**
     * Chain this to the constructor to extend the potion's duration.
     *
     * @return The potion.
     */
    public PotionUtil extend() {
        setHasExtendedDuration(true);
        return this;
    }

    /**
     * Chain this to the constructor to make potion a linger potion.
     *
     * @return The potion.
     */
    public PotionUtil linger() {
        setLinger(true);
        return this;
    }

    /**
     * Chain this to the constructor to make potion a strong potion.
     *
     * @return The potion.
     */
    public PotionUtil strong() {
        setStrong(true);
        return this;
    }

    /**
     * Applies the effects of this potion to the given {@link ItemStack}. The
     * ItemStack must be a potion.
     *
     * @param to The itemstack to apply to
     * @throws Exception if stack is null or not a potion
     */
    public void apply(ItemStack to) throws Exception {
        if (to == null) {
            throw new Exception("itemstack cannot be null");
        }
        if (to.getType() != Material.POTION) {
            throw new Exception("given itemstack is not a potion");
        }
        to = toItemStack(to.getAmount()).clone();
    }

    /**
     * This converts PotionInfo to an ItemStack
     * NOTICE: This does not allow a way to change the level of the potion. This will work for only default minecraft potions.
     *
     * @param amount how many
     * @return ItemStack of a potion. NULL if it fails.
     */
    public ItemStack toItemStack(int amount) {

        ItemStack item = new ItemStack(Material.POTION, amount);
        if (splash) {
            item = new ItemStack(Material.SPLASH_POTION, amount);
        } else if (linger) {
            item = new ItemStack(Material.LINGERING_POTION, amount);
        }
        try {
            Class nmsItemStackClass = Crafty.findNmsClass("ItemStack");
            Class craftItemStackClass = Crafty.findCraftClass("inventory.CraftItemStack");
            Class nbtCompoundTagClass = Crafty.findNmsClass("NBTTagCompound");
            MethodHandle handle = Crafty.findStaticMethod(craftItemStackClass, "asNMSCopy", nmsItemStackClass, ItemStack.class);
            Object nmsStack = null;
            if (handle != null) {
                nmsStack = handle.invokeWithArguments(item);
            }
            MethodHandle getTagHandle = Crafty.findMethod(nmsItemStackClass, "getTag", nbtCompoundTagClass);
            Object tagCompound = null;
            if (getTagHandle != null) {
                tagCompound = getTagHandle.invoke(nmsStack);
            }
            if (tagCompound == null) {
                tagCompound = nbtCompoundTagClass.getConstructor().newInstance();
            }
            String tag = "";
            switch (type) {
                case JUMP:
                    tag = "leaping";
                    break;
                case LUCK:
                    tag = "luck";
                    break;
                case REGEN:
                    tag = "regeneration";
                    break;
                case SPEED:
                    tag = "swiftness";
                    break;
                case FIRE_RESISTANCE:
                    tag = "fire_resistance";
                    break;
                case INSTANT_DAMAGE:
                    tag = "harming";
                    break;
                case INSTANT_HEAL:
                    tag = "healing";
                    break;
                case WATER_BREATHING:
                    tag = "water_breathing";
                    break;
                case SLOW_FALLING:
                    tag = "slow_falling";
                    break;
                case NIGHT_VISION:
                    tag = "night_vision";
                    break;
                case INVISIBILITY:
                    tag = "invisibility";
                    break;
                case WEAKNESS:
                    tag = "weakness";
                    break;
                case STRENGTH:
                    tag = "strength";
                    break;
                case SLOWNESS:
                    tag = "slowness";
                    break;
                case MUNDANE:
                    tag = "mundane";
                    break;
                case TURTLE:
                    tag = "turtle_master";
                    break;
                case POISON:
                    tag = "poison";
                    break;
                case AWKWARD:
                    tag = "awkward";
                    break;
                case WATER:
                    tag = "water";
                    break;
                case THICK:
                    tag = "thick";
                    break;
                case EMPTY:
                    tag = "empty";
                    break;
            }
            if (extended) {
                tag = "long_" + tag;
            } else if (strong) {
                tag = "strong_" + tag;
            }
            MethodHandle setString = Crafty.findMethod(tagCompound.getClass(), "setString", Void.class, String.class, String.class);
            MethodHandle setTagHandle = Crafty.findMethod(nmsItemStackClass, "setTag", Void.class, nbtCompoundTagClass);
            setString.invokeWithArguments(tagCompound, "Potion", "minecraft:" + tag);
            //todo
            setTagHandle.invokeWithArguments(nmsStack, tagCompound);
            MethodHandle asBukkitCopy = Crafty.findStaticMethod(craftItemStackClass, "asBukkitCopy", ItemStack.class, nmsItemStackClass);
            return ((ItemStack)asBukkitCopy.invokeWithArguments(nmsStack));
        } catch (Throwable t) {
            return null;
        }
    }

    /**
     * This gets the potion type
     *
     * @return PotionType
     */
    public PotionType getType() {
        return type;
    }

    /**
     * Sets the PotionType for this PotionInfo
     *
     * @param type type
     */
    public void setType(PotionType type) {
        this.type = type;
    }

    /**
     * A strong potion is a potion which is level II.
     *
     * @return boolean. True if the potion is strong.
     */
    public boolean isStrong() {
        return strong;
    }

    /**
     * This sets if the PotionInfo is strong.
     *
     * @param strong is strong
     */
    public void setStrong(boolean strong) {
        this.strong = strong;
    }

    /**
     * A long potion is an extended duration potion.
     *
     * @return boolen. True if the potion is the extended type.
     */
    public boolean isExtendedDuration() {
        return extended;
    }

    /**
     * This changes the extended value for PotionInfo.
     *
     * @param extended if extended
     */
    public void setHasExtendedDuration(boolean extended) {
        this.extended = extended;
    }

    /**
     * This lets you know if PotionInfo is a lingering potion.
     *
     * @return boolean. True if the potion is a lingering potion.
     */
    public boolean isLinger() {
        return linger;
    }

    /**
     * Set linger to true or false.
     *
     * @param linger will linger
     */
    public void setLinger(boolean linger) {
        this.linger = linger;
    }

    /**
     * Checks if a potion is a splash potion.
     *
     * @return boolean. True if the potion is a splash potion.
     */
    public boolean isSplash() {
        return splash;
    }

    /**
     * This sets this PotionInfo to a splash potion.
     *
     * @param splash is splash
     */
    public void setSplash(boolean splash) {
        this.splash = splash;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof PotionUtil) {
            PotionUtil test = (PotionUtil) object;
            return test.type.equals(type) && test.extended == extended && test.linger == linger && test.splash == splash;
        }
        return false;
    }

    /**
     * This will make this instance of PotionInfo act as if it was brewed with an ingredient.
     *
     * @param ingredient brew from this ingredient
     */
    public void brew(ItemStack ingredient) {
        if (ingredient != null) {
            if (ingredient.getType().equals(Material.NETHER_WART)) {
                if (type.equals(PotionType.WATER)) {
                    type = PotionType.AWKWARD;
                }
            } else if (ingredient.getType().equals(Material.GUNPOWDER)) {
                splash = true;
            } else if (ingredient.getType().equals(Material.GLOWSTONE_DUST)) {
                if (type.equals(PotionType.WATER)) {
                    type = PotionType.THICK;
                    if (!extended) {
                        strong = true;
                    }
                } else {
                    if (!extended) {
                        strong = true;
                    }
                }
            } else if (ingredient.getType().equals(Material.REDSTONE)) {
                if (type.equals(PotionType.WATER)) {
                    type = PotionType.MUNDANE;
                    extended = true;
                } else {
                    if (!strong) {
                        extended = true;
                    }
                }
            } else if (ingredient.getType().equals(Material.DRAGON_BREATH)) {
                if (splash) {
                    linger = true;
                }
            } else if (ingredient.getType().equals(Material.SPIDER_EYE)) {
                if (type.equals(PotionType.WATER)) {
                    type = PotionType.MUNDANE;
                } else if (type.equals(PotionType.AWKWARD)) {
                    type = PotionType.POISON;
                }
            } else if (ingredient.getType().equals(Material.BLAZE_POWDER)) {
                if (type.equals(PotionType.WATER)) {
                    type = PotionType.MUNDANE;
                } else if (type.equals(PotionType.AWKWARD)) {
                    type = PotionType.STRENGTH;
                }
            } else if (ingredient.getType().equals(Material.GHAST_TEAR)) {
                if (type.equals(PotionType.WATER)) {
                    type = PotionType.MUNDANE;
                } else if (type.equals(PotionType.AWKWARD)) {
                    type = PotionType.REGEN;
                }
            } else if (ingredient.getType().equals(Material.GLISTERING_MELON_SLICE)) {
                if (type.equals(PotionType.WATER)) {
                    type = PotionType.MUNDANE;
                } else if (type.equals(PotionType.AWKWARD)) {
                    type = PotionType.INSTANT_HEAL;
                }
            } else if (ingredient.getType().equals(Material.RABBIT_FOOT)) {
                if (type.equals(PotionType.WATER)) {
                    type = PotionType.MUNDANE;
                } else if (type.equals(PotionType.AWKWARD)) {
                    type = PotionType.JUMP;
                }
            } else if (ingredient.getType().equals(Material.SUGAR)) {
                if (type.equals(PotionType.WATER)) {
                    type = PotionType.MUNDANE;
                } else if (type.equals(PotionType.AWKWARD)) {
                    type = PotionType.SPEED;
                }
            } else if (ingredient.getType().equals(Material.MAGMA_CREAM)) {
                if (type.equals(PotionType.WATER)) {
                    type = PotionType.MUNDANE;
                } else if (type.equals(PotionType.AWKWARD)) {
                    type = PotionType.FIRE_RESISTANCE;
                }
            } else if (ingredient.getType().equals(Material.FERMENTED_SPIDER_EYE) && !splash && !linger) {
                if (type.equals(PotionType.WATER)) {
                    type = PotionType.WEAKNESS;
                } else if (type.equals(PotionType.NIGHT_VISION)) {
                    type = PotionType.INVISIBILITY;
                } else if (type.equals(PotionType.INSTANT_HEAL)) {
                    type = PotionType.INSTANT_DAMAGE;
                } else if (type.equals(PotionType.POISON)) {
                    type = PotionType.INSTANT_DAMAGE;
                } else if (type.equals(PotionType.JUMP) && !strong && !extended && !splash) {
                    type = PotionType.SLOWNESS;
                } else if (type.equals(PotionType.FIRE_RESISTANCE)) {
                    type = PotionType.SLOWNESS;
                } else if (type.equals(PotionType.SPEED)) {
                    type = PotionType.SLOWNESS;
                }
            } else if (ingredient.getType().equals(Material.GOLDEN_CARROT)) {
                if (type.equals(PotionType.AWKWARD)) {
                    type = PotionType.NIGHT_VISION;
                }
            } else if (ingredient.getType().equals(Material.PUFFERFISH)) {
                if (type.equals(PotionType.AWKWARD)) {
                    type = PotionType.WATER_BREATHING;
                }
            }
        }
    }

    @Override
    public String toString() {
        String potionName = this.type.toString().replace("_", " ");

        // Change the potion name to initial caps
        String potionDescription = potionName.substring(0, 1) + potionName.substring(1).toLowerCase();

        if (this.splash)
            potionDescription = "Splash " + potionDescription;

        if (this.linger)
            potionDescription = "Lingering " + potionDescription;

        if (this.strong)
            potionDescription += " II";

        if (this.extended)
            return potionDescription + " (extended)";
        else
            return potionDescription;
    }

    public enum PotionType {
        FIRE_RESISTANCE,
        INSTANT_DAMAGE,
        INSTANT_HEAL,
        INVISIBILITY,
        JUMP,
        LUCK,
        NIGHT_VISION,
        POISON,
        REGEN,
        SLOWNESS,
        SPEED,
        STRENGTH,
        WATER,
        WATER_BREATHING,
        WEAKNESS,
        SLOW_FALLING,
        TURTLE,
        EMPTY,
        MUNDANE,
        THICK,
        AWKWARD

    }
}
