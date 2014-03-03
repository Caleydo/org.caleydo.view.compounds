/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package org.caleydo.view.compounds.ui;

import gleem.linalg.Vec2f;

import org.caleydo.core.util.color.Color;
import org.caleydo.core.view.opengl.layout2.GLElement;
import org.caleydo.core.view.opengl.layout2.GLGraphics;
import org.caleydo.core.view.opengl.layout2.basic.ScrollingDecorator.IHasMinSize;

/**
 * element of this view
 *
 * @author Hendrik Strobelt
 *
 */
public class CompoundsElement extends GLElement implements IHasMinSize {

	private final String smile;

	/**
	 * @param string
	 */
	public CompoundsElement(String smile) {
		this.smile = smile;
	}

	@Override
	public Vec2f getMinSize() {
		return new Vec2f(100, 100);
	}

	@Override
	protected void renderImpl(GLGraphics g, float w, float h) {
		super.renderImpl(g, w, h);

		g.color(Color.RED).fillRect(0, 0, w, h);
		g.drawText("Smile String: " + smile, 0, 0, w, 20);
	}
}
