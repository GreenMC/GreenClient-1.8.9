package net.minecraft.client.settings;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.IntHashMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KeyBinding implements Comparable<KeyBinding> {

    private static final Set<String> categories = new HashSet<>();
    private static final List<KeyBinding> keyBindings = new ArrayList<>();
    private static final IntHashMap<KeyBinding> hash = new IntHashMap<>();

    private int keyCode, pressTime;
    private boolean pressed;

    private final String keyDescription, keyCategory;
    private final int keyCodeDefault;

    public KeyBinding(String description, int keyCode, String category) {
        this.keyDescription = description;
        this.keyCode = keyCode;
        this.keyCodeDefault = keyCode;
        this.keyCategory = category;

        keyBindings.add(this);
        hash.addKey(keyCode, this);
        categories.add(category);
    }

    public boolean isKeyDown() {
        return pressed;
    }

    public String getKeyCategory() {
        return keyCategory;
    }

    public boolean isPressed() {
        if (pressTime == 0) {
            return false;
        } else {
            --pressTime;
            return true;
        }
    }

    private void unpressKey() {
        pressTime = 0;
        pressed = false;
    }

    public String getKeyDescription() {
        return keyDescription;
    }

    public int getKeyCodeDefault() {
        return keyCodeDefault;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public int compareTo(KeyBinding keyBinding) {
        int i = I18n.format(this.keyCategory).compareTo(I18n.format(keyBinding.keyCategory));

        if (i == 0) {
            i = I18n.format(this.keyDescription).compareTo(I18n.format(keyBinding.keyDescription));
        }

        return i;
    }

    public static void onTick(int keyCode) {
        if (keyCode != 0) {
            KeyBinding keybinding = hash.lookup(keyCode);

            if (keybinding != null) {
                ++keybinding.pressTime;
            }
        }
    }

    public static void setKeyBindState(int keyCode, boolean pressed) {
        if (keyCode != 0) {
            KeyBinding keybinding = hash.lookup(keyCode);

            if (keybinding != null) {
                keybinding.pressed = pressed;
            }
        }
    }

    public static void unPressAllKeys() {
        for (KeyBinding keybinding : keyBindings) {
            keybinding.unpressKey();
        }
    }

    public static void resetKeyBindingArrayAndHash() {
        hash.clearMap();

        for (KeyBinding keybinding : keyBindings) {
            hash.addKey(keybinding.keyCode, keybinding);
        }
    }

    public static Set<String> getKeyBinds() {
        return categories;
    }
}