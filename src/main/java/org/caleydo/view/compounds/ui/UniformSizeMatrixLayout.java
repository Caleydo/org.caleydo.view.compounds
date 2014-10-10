/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package org.caleydo.view.compounds.ui;

import gleem.linalg.Vec2f;

import java.util.List;

import org.caleydo.core.view.opengl.layout2.layout.IGLLayout2;
import org.caleydo.core.view.opengl.layout2.layout.IGLLayoutElement;

/**
 * Fills a matrix of elements with uniform size.
 *
 * @author Christian
 *
 */
public class UniformSizeMatrixLayout implements IGLLayout2 {

	protected Vec2f minCellSize;
	protected float cellSpacing;

	public UniformSizeMatrixLayout(Vec2f minCellSize, float cellSpacing) {
		this.minCellSize = minCellSize;
		this.cellSpacing = cellSpacing;
	}

	@Override
	public boolean doLayout(List<? extends IGLLayoutElement> children, float w, float h, IGLLayoutElement parent,
			int deltaTimeMs) {
		int numColumns = (int) Math.max(1, Math.floor((w + cellSpacing) / (minCellSize.x() + cellSpacing)));
		if (children.size() < numColumns)
			numColumns = children.size();
		float childWidth = (w - (numColumns - 1) * cellSpacing) / numColumns;

		int numRows = (int) Math.max(1, Math.ceil((float) children.size() / (float) numColumns));
		float childHeight = (h - (numRows - 1) * cellSpacing) / numRows;

		int childIndex = 0;
		float posY = 0;
		for (int i = 0; i < numRows; i++) {
			float posX = 0;
			for (int j = 0; j < numColumns; j++) {
				if (childIndex >= children.size())
					return false;
				IGLLayoutElement child = children.get(childIndex);
				child.setLocation(posX, posY);
				child.setSize(childWidth, childHeight);

				posX += childWidth + cellSpacing;
				childIndex++;
			}
			posY += childHeight + cellSpacing;
		}

		return false;
	}

	/**
	 * @return the minCellSize, see {@link #minCellSize}
	 */
	public Vec2f getMinCellSize() {
		return minCellSize;
	}

}
