/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package org.caleydo.view.compounds.internal;

import java.util.List;

import org.caleydo.core.util.collection.Pair.ComparablePair;
import org.caleydo.core.view.opengl.layout2.GLElement;
import org.caleydo.core.view.opengl.layout2.layout.GLLayoutDatas;
import org.caleydo.core.view.opengl.layout2.manage.GLElementFactoryContext;
import org.caleydo.core.view.opengl.layout2.manage.IGLElementFactory;
import org.caleydo.view.compounds.ui.CompoundsElement;

/**
 * @author Samuel Gratzl
 *
 */
public class CompoundsElementFactory implements IGLElementFactory {

	@Override
	public String getId() {
		return "smiles";
	}


	@Override
	public boolean apply(GLElementFactoryContext context) {
		return context.get("smiles", List.class, null) != null;
	}

	@Override
	public GLElement create(GLElementFactoryContext context) {

		@SuppressWarnings("unchecked")
		List<ComparablePair<String, String>> smiles = context.get("smiles", List.class,
				GLLayoutDatas.<List> throwInvalidException());
		return new CompoundsElement(smiles);
	}

}
