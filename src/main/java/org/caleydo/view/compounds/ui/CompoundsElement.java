/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package org.caleydo.view.compounds.ui;

import gleem.linalg.Vec2f;

import org.caleydo.core.view.opengl.layout2.GLElement;
import org.caleydo.core.view.opengl.layout2.basic.ScrollingDecorator.IHasMinSize;

/**
 * element of this view
 *
 * @author Hendrik Strobelt
 *
 */
public class CompoundsElement extends GLElement implements IHasMinSize {

	@Override
	public Vec2f getMinSize() {
		return new Vec2f(100, 100);
	}

}
