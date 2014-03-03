/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package org.caleydo.view.compounds.internal.serial;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.caleydo.core.serialize.ASerializedSingleTablePerspectiveBasedView;
import org.caleydo.core.view.ISingleTablePerspectiveBasedView;
import org.caleydo.view.compounds.internal.CompoundsView;

/**
 *
 * @author Hendrik Strobelt
 *
 */
@XmlRootElement
@XmlType
public class SerializedCompoundsView extends ASerializedSingleTablePerspectiveBasedView {

	/**
	 * Default constructor with default initialization
	 */
	public SerializedCompoundsView() {
	}

	public SerializedCompoundsView(ISingleTablePerspectiveBasedView view) {
		super(view);
	}

	@Override
	public String getViewType() {
		return CompoundsView.VIEW_TYPE;
	}
}
