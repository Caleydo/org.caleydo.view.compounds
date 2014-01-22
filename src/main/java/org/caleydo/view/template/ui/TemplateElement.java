/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package org.caleydo.view.template.ui;

import gleem.linalg.Vec2f;

import org.caleydo.core.data.perspective.table.TablePerspective;
import org.caleydo.core.view.opengl.layout2.renderer.GLRenderers;
import org.caleydo.core.view.opengl.layout2.view.ASingleTablePerspectiveElement;
import org.caleydo.view.template.internal.Resources;

/**
 * element of this view holding a {@link TablePerspective}
 *
 * @author AUTHOR
 *
 */
public class TemplateElement extends ASingleTablePerspectiveElement {
	public TemplateElement(TablePerspective tablePerspective) {
		super(tablePerspective);
		setRenderer(GLRenderers.fillImage(Resources.ICON));
	}

	@Override
	public Vec2f getMinSize() {
		return new Vec2f(100, 100);
	}

}
