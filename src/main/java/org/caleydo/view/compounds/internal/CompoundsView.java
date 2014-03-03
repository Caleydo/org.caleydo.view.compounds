/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package org.caleydo.view.compounds.internal;

import org.caleydo.core.data.datadomain.DataSupportDefinitions;
import org.caleydo.core.data.datadomain.IDataSupportDefinition;
import org.caleydo.core.data.perspective.table.TablePerspective;
import org.caleydo.core.serialize.ASerializedView;
import org.caleydo.core.util.logging.Logger;
import org.caleydo.core.view.opengl.canvas.IGLCanvas;
import org.caleydo.core.view.opengl.layout2.GLElementDecorator;
import org.caleydo.core.view.opengl.layout2.view.ASingleTablePerspectiveElementView;
import org.caleydo.view.compounds.internal.serial.SerializedCompoundsView;
import org.caleydo.view.compounds.ui.CompoundsElement;

/**
 *
 * @author Hendrik Strobelt
 *
 */
public class CompoundsView extends ASingleTablePerspectiveElementView {
	public static final String VIEW_TYPE = "org.caleydo.view.compounds";
	public static final String VIEW_NAME = "Compounds";

	private static final Logger log = Logger.create(CompoundsView.class);

	public CompoundsView(IGLCanvas glCanvas) {
		super(glCanvas, VIEW_TYPE, VIEW_NAME);
	}

	@Override
	public ASerializedView getSerializableRepresentation() {
		return new SerializedCompoundsView(this);
	}

	@Override
	public IDataSupportDefinition getDataSupportDefinition() {
		return DataSupportDefinitions.tableBased;
	}

	@Override
	protected void applyTablePerspective(GLElementDecorator root, TablePerspective tablePerspective) {
		if (tablePerspective == null)
			root.setContent(null);
		else
			root.setContent(new CompoundsElement(tablePerspective));
	}
}
