package com.game.server.world.object;

import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.geometry.Vector2;
import com.game.server.world.material.RectangleMaterial;
import com.game.server.world.object.base.GameObject;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
/**
 * @author dohnal
 */
public class Wall extends GameObject
{
	public Wall(Vector2 position)
	{
		setPosition(position);
		setMaterial(new RectangleMaterial(this, Color.gray, 50, 50));
	}

	@Override
	protected List<Behavior> getBehaviours()
	{
		return new ArrayList<>();
	}
}
