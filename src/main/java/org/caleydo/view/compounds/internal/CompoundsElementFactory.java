/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package org.caleydo.view.compounds.internal;

import org.caleydo.core.view.opengl.layout2.GLElement;
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
		return "smile";
	}


	@Override
	public boolean apply(GLElementFactoryContext context) {
		return context.get("smile", String.class, null) != null;
	}

	@Override
	public GLElement create(GLElementFactoryContext context) {
		return new CompoundsElement();
	}

}
