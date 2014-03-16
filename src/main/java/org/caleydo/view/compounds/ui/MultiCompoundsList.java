/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package org.caleydo.view.compounds.ui;

import gleem.linalg.Vec2f;

import java.util.ArrayList;
import java.util.List;

import org.caleydo.core.data.collection.EDimension;
import org.caleydo.core.util.collection.Pair.ComparablePair;
import org.caleydo.core.util.logging.Logger;
import org.caleydo.core.view.opengl.layout2.GLElement;
import org.caleydo.core.view.opengl.layout2.GLElementContainer;
import org.caleydo.core.view.opengl.layout2.GLSandBox;
import org.caleydo.core.view.opengl.layout2.basic.ScrollBar;
import org.caleydo.core.view.opengl.layout2.basic.ScrollingDecorator;
import org.caleydo.core.view.opengl.layout2.basic.ScrollingDecorator.IHasMinSize;
import org.caleydo.core.view.opengl.layout2.geom.Rect;
import org.caleydo.core.view.opengl.layout2.layout.GLLayouts;
import org.caleydo.core.view.opengl.layout2.layout.GLMinSizeProviders;
import org.caleydo.core.view.opengl.layout2.layout.GLPadding;
import org.caleydo.core.view.opengl.layout2.layout.GLSizeRestrictiveFlowLayout2;

/**
 * element of this view
 *
 * @author Hendrik Strobelt
 *
 */
public class MultiCompoundsList extends GLElementContainer implements IHasMinSize {
	private static final Logger log = Logger.create(MultiCompoundsList.class);


	protected ScrollingDecorator scrollingDecorator;

	protected ScrollableCompoundsList body;

//	 public MultiCompoundsElement(){
//
//	 }

	protected class ScrollableCompoundsList extends GLElementContainer{
		private  List<ComparablePair<String, String>> smilesList;

		public ScrollableCompoundsList(final List<ComparablePair<String, String>> smilesList) {
			super();
			this.smilesList = smilesList;
			setLayout(new GLSizeRestrictiveFlowLayout2(false, 5, GLPadding.ZERO));
			setMinSizeProvider(GLMinSizeProviders.createVerticalFlowMinSizeProvider(this,
					5, GLPadding.ZERO));

			for (ComparablePair<String, String> smile : this.smilesList) {
//				add(new CompoundsElement(smile,400,400));
				add(new CompoundsElement(smile.getSecond(), smile.getFirst()));
//				add(new CompoundsElement(smile.getSecond(), smile.getFirst(),300,300));
			}

		}



		@Override
		public void layout(int deltaTimeMs) {
			super.layout(deltaTimeMs);

			Rect clippingArea = scrollingDecorator.getClipingArea();

			for (GLElement child : this) {
				Rect bounds = child.getRectBounds();
				if (child.getVisibility() != EVisibility.NONE) {
					if (clippingArea.asRectangle2D().intersects(bounds.asRectangle2D())) {

						if (child.getVisibility() != EVisibility.PICKABLE) {
							child.setVisibility(EVisibility.PICKABLE);
							repaintAll();
						}
					} else {
						if (child.getVisibility() != EVisibility.HIDDEN) {
							child.setVisibility(EVisibility.HIDDEN);
							repaintAll();
						}
					}
				}
			}
		}

		@Override
		public Vec2f getMinSize() {
			// TODO Auto-generated method stub
			// Vec2f minSize = super.getMinSize();
			// minSize.setX(getSize().x());
			return super.getMinSize();
		}


	}



	//List<ComparablePair<String, String>> smiles
	public MultiCompoundsList(final List<ComparablePair<String, String>> smilesList) {
		super();
//		setLayout(new GLSizeRestrictiveFlowLayout2(false, 1, GLPadding.ZERO));
		setLayout(GLLayouts.LAYERS);
		setMinSizeProvider(GLMinSizeProviders.createLayeredMinSizeProvider(this));

		body = new ScrollableCompoundsList(smilesList);


		scrollingDecorator = new ScrollingDecorator(body, new ScrollBar(true),
				new ScrollBar(false), 8, EDimension.RECORD );
		scrollingDecorator.setMinSizeProvider(
//				new IHasMinSize() {
//			@Override
//			public Vec2f getMinSize() {
//				final float blobb = smilesList.size()*400;
//				return new Vec2f(1000,blobb);
//			}
//		}
		body		);

		add(scrollingDecorator);
		relayout();

	}

	@Override
	public Vec2f getMinSize() {
		return super.getMinSize();
	}


	public GLElement asGLElement() {
		return scrollingDecorator;
	}


	public static void main(String[] args) {
		// the tough one:
		ArrayList<ComparablePair<String, String> > list = new ArrayList<ComparablePair<String, String> >();
		list.add(new ComparablePair<String, String>("abc","COC1=C(C(=CC=C1)OC)OCCNCC2COC3=CC=CC=C3O2.Cl"));
		list.add(new ComparablePair<String, String>("abc","COC1=C(C(=CC=C1)OC)OCCNCC2COC3=CC=CC=C3O2.Cl"));
		list.add(new ComparablePair<String, String>("abc","COC1=C(C(=CC=C1)OC)OCCNCC2COC3=CC=CC=C3O2.Cl"));
		list.add(new ComparablePair<String, String>("abc","COC1=C(C(=CC=C1)OC)OCCNCC2COC3=CC=CC=C3O2.Cl"));
		list.add(new ComparablePair<String, String>("abc","COC1=C(C(=CC=C1)OC)OCCNCC2COC3=CC=CC=C3O2.Cl"));
		list.add(new ComparablePair<String, String>("abc","COC1=C(C(=CC=C1)OC)OCCNCC2COC3=CC=CC=C3O2.Cl"));

		GLSandBox.main(args, new MultiCompoundsList(list));

		// GLSandBox.main(args, new
		// CompoundsElement("CN2C(=O)N(C)C(=O)C1=C2N=CN1C"));
	}
}
