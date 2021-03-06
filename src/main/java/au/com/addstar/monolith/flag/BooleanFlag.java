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

package au.com.addstar.monolith.flag;

import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import au.com.addstar.monolith.Monolith;
import au.com.addstar.monolith.command.BadArgumentException;

public class BooleanFlag extends Flag<Boolean> {
    @Override
    public Boolean parse(Player sender, String[] args) throws IllegalArgumentException, BadArgumentException {
        if (args.length != 1)
            throw new IllegalArgumentException("<value>");

        if (!args[0].equalsIgnoreCase("true") && !args[0].equalsIgnoreCase("false"))
            throw new BadArgumentException(0, "Valid values are true, and false");

        return Boolean.parseBoolean(args[0]);
    }

    @Override
    public List<String> tabComplete(Player sender, String[] args) {
        if (args.length == 1)
            return Monolith.matchStrings(args[0], Arrays.asList("True", "False"));

        return null;
    }

    @Override
    public void save(ConfigurationSection section) {
        section.set("value", value);
    }

    @Override
    public void read(ConfigurationSection section) throws InvalidConfigurationException {
        value = section.getBoolean("value");
    }

    @Override
    public String getValueString() {
        return value ? "True" : "False";
    }

}
