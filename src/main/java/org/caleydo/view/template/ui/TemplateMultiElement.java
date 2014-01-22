/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package org.caleydo.view.template.ui;

import org.caleydo.core.data.perspective.table.TablePerspective;
import org.caleydo.core.view.opengl.layout2.GLElementContainer;
import org.caleydo.core.view.opengl.layout2.layout.GLLayoutDatas;
import org.caleydo.core.view.opengl.layout2.layout.GLLayouts;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

/**
 *
 *
 * @author AUTHOR
 *
 */
public class TemplateMultiElement extends GLElementContainer {

	public TemplateMultiElement() {
		super(GLLayouts.flowHorizontal(5));
	}

	public Iterable<TablePerspective> getTablePerspectives() {
		return Iterables.filter(Iterables.transform(this, GLLayoutDatas.toLayoutData(TablePerspective.class, null)),
				Predicates.notNull());
	}
}
