/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package org.caleydo.view.compounds.ui;

import gleem.linalg.Vec2f;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.caleydo.core.data.collection.EDimension;
import org.caleydo.core.util.color.Color;
import org.caleydo.core.util.logging.Logger;
import org.caleydo.core.view.opengl.layout2.GLElement;
import org.caleydo.core.view.opengl.layout2.GLElementContainer;
import org.caleydo.core.view.opengl.layout2.GLGraphics;
import org.caleydo.core.view.opengl.layout2.GLSandBox;
import org.caleydo.core.view.opengl.layout2.IGLElementContext;
import org.caleydo.core.view.opengl.layout2.GLElement.EVisibility;
import org.caleydo.core.view.opengl.layout2.basic.ScrollBar;
import org.caleydo.core.view.opengl.layout2.basic.ScrollingDecorator;
import org.caleydo.core.view.opengl.layout2.basic.ScrollingDecorator.IHasMinSize;
import org.caleydo.core.view.opengl.layout2.geom.Rect;
import org.caleydo.core.view.opengl.layout2.layout.GLLayouts;
import org.caleydo.core.view.opengl.layout2.layout.GLMinSizeProviders;
import org.caleydo.core.view.opengl.layout2.layout.GLPadding;
import org.caleydo.core.view.opengl.layout2.layout.GLSizeRestrictiveFlowLayout;
import org.caleydo.core.view.opengl.layout2.layout.GLSizeRestrictiveFlowLayout2;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.renderer.AtomContainerRenderer;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
import org.openscience.cdk.renderer.generators.BasicBondGenerator;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator;
import org.openscience.cdk.renderer.generators.IGenerator;
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;
import org.openscience.cdk.smiles.SmilesParser;

import com.jogamp.opengl.util.awt.TextureRenderer;

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
		private  List<String> smilesList;

		public ScrollableCompoundsList(final List<String> smilesList) {
			super();
			this.smilesList = smilesList;
			setLayout(new GLSizeRestrictiveFlowLayout2(false, 5, GLPadding.ZERO));
			setMinSizeProvider(GLMinSizeProviders.createVerticalFlowMinSizeProvider(this,
					5, GLPadding.ZERO));
			
			for (String smile : this.smilesList) {
//				add(new CompoundsElement(smile,400,400));
				add(new CompoundsElement(smile));
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
	
	
	
	
	public MultiCompoundsList(final List<String> smilesList) {
		super();
//		setLayout(new GLSizeRestrictiveFlowLayout2(false, 1, GLPadding.ZERO));
		setLayout(GLLayouts.LAYERS);
		
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
		return new Vec2f(400,400);
	}
	

	public GLElement asGLElement() {
		return scrollingDecorator;
	}

	
	public static void main(String[] args) {
		// the tough one:
		ArrayList<String> list = new ArrayList<String>();
		list.add("COC1=C(C(=CC=C1)OC)OCCNCC2COC3=CC=CC=C3O2.Cl");
		list.add("COC1=C(C(=CC=C1)OC)OCCNCC2COC3=CC=CC=C3O2.Cl");
		list.add("COC1=C(C(=CC=C1)OC)OCCNCC2COC3=CC=CC=C3O2.Cl");
		list.add("COC1=C(C(=CC=C1)OC)OCCNCC2COC3=CC=CC=C3O2.Cl");
		list.add("COC1=C(C(=CC=C1)OC)OCCNCC2COC3=CC=CC=C3O2.Cl");
		list.add("COC1=C(C(=CC=C1)OC)OCCNCC2COC3=CC=CC=C3O2.Cl");
		list.add("COC1=C(C(=CC=C1)OC)OCCNCC2COC3=CC=CC=C3O2.Cl");
		
		GLSandBox.main(args, new MultiCompoundsList(list));

		// GLSandBox.main(args, new
		// CompoundsElement("CN2C(=O)N(C)C(=O)C1=C2N=CN1C"));
	}
}