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

import org.caleydo.core.util.color.Color;
import org.caleydo.core.util.logging.Logger;
import org.caleydo.core.view.opengl.layout2.GLElement;
import org.caleydo.core.view.opengl.layout2.GLGraphics;
import org.caleydo.core.view.opengl.layout2.GLSandBox;
import org.caleydo.core.view.opengl.layout2.IGLElementContext;
import org.caleydo.core.view.opengl.layout2.basic.ScrollingDecorator.IHasMinSize;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
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
public class CompoundsElement extends GLElement implements IHasMinSize {
	private static final Logger log = Logger.create(CompoundsElement.class);

	private final String smile;
	private final IAtomContainer molecul;
	private final AtomContainerRenderer renderer;

	private TextureRenderer textureRenderer;

	/**
	 * @param string
	 */
	public CompoundsElement(String smile) {
		this.smile = smile;

		// init moleculeRendering steps
		// generators make the image elements
		List<IGenerator<IAtomContainer>> generators = new ArrayList<>();
		generators.add(new BasicSceneGenerator());
		generators.add(new BasicBondGenerator());
		generators.add(new BasicAtomGenerator());
		// the renderer needs to have a toolkit-specific font manager
		renderer = new AtomContainerRenderer(generators, new AWTFontManager());

		this.molecul = toMolecul(smile);
	}

	@Override
	protected void init(IGLElementContext context) {
		textureRenderer = new TextureRenderer(800, 800, false);
		// Graphics2D graphics = textureRenderer.createGraphics();
		// graphics.drawLine(10, 10, 300, 300);
		// graphics.dispose();
		super.init(context);
	}

	@Override
	protected void takeDown() {
		if (this.textureRenderer != null)
			textureRenderer.dispose();
		textureRenderer = null;
		super.takeDown();
	}

	/**
	 * @param smile2
	 * @return
	 */
	private static IAtomContainer toMolecul(String smile) {
		IAtomContainer molecule;
		try {
			molecule = new SmilesParser(DefaultChemObjectBuilder.getInstance()).parseSmiles(smile);

			StructureDiagramGenerator sdg = new StructureDiagramGenerator();
			sdg.setMolecule(molecule);
			try {
				sdg.generateCoordinates();
			} catch (Exception ex) {
				log.warn("can't generate coordinates for " + smile, ex);
			}
			return sdg.getMolecule();
		} catch (InvalidSmilesException e) {
			log.error("invalid smile string: " + smile, e);
		}
		return null;
	}

	@Override
	public Vec2f getMinSize() {
		return new Vec2f(100, 100);
	}

	@Override
	protected void layoutImpl(int deltaTimeMs) {
		// we have a new size
		renderSmiles((int) getSize().x(), (int) getSize().y());
		super.layoutImpl(deltaTimeMs);
	}

	@Override
	protected void renderImpl(GLGraphics g, float w, float h) {
		super.renderImpl(g, w, h);

		g.color(Color.WHITE).fillRect(0, 0, w, h);

		g.fillImage(textureRenderer.getTexture(), 0,0, w, h);
//		float size = (w>h)?h:w;
//		g.fillImage(textureRenderer.getTexture(), (w-size)/2, (h-size)/2, size, size);


	}

	private void renderSmiles(int width, int height) {
		// the draw area and the image should be the same size
		Rectangle drawArea = new Rectangle(width, height);

		// center molecule
		renderer.setup(molecul, drawArea);

		// zoom to fit in rectangle..
		Rectangle diagramBounds = renderer.calculateDiagramBounds(molecul);
		renderer.setZoomToFit(width, height, diagramBounds.width, diagramBounds.height);

		// and render it in a new texture
		textureRenderer.setSize(width, height);
		Graphics2D graphics = textureRenderer.createGraphics();
		graphics.setColor(java.awt.Color.WHITE);
		graphics.fillRect(0, 0, width, height);
		renderer.paint(molecul, new AWTDrawVisitor(graphics));
		graphics.dispose();
	}

	public static void main(String[] args) {
		GLSandBox.main(args, new CompoundsElement("CN2C(=O)N(C)C(=O)C1=C2N=CN1C"));
	}
}
