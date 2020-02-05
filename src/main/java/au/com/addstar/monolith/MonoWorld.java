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

package au.com.addstar.monolith;

import java.util.WeakHashMap;

import org.bukkit.World;
import au.com.addstar.monolith.chat.Title;

public class MonoWorld
{
	private static WeakHashMap<World, MonoWorld> mWorlds = new WeakHashMap<>();
	
	public static MonoWorld getWorld(World world)
	{
		MonoWorld mworld = mWorlds.get(world);
		if(mworld == null)
		{
			mworld = new MonoWorld(world);
			mWorlds.put(world, mworld);
		}
		
		return mworld;
	}
	
	private World mWorld;
	
	private MonoWorld(World world)
	{
		mWorld = world;
	}

	@Deprecated
	public void showTitle(Title title)
	{
		title.show(mWorld.getPlayers());
	}
}
