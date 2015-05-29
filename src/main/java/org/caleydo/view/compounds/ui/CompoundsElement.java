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
import org.caleydo.core.view.opengl.layout2.GLGraphics;
import org.caleydo.core.view.opengl.layout2.GLSandBox;
import org.caleydo.core.view.opengl.layout2.IGLElementContext;
import org.caleydo.core.view.opengl.layout2.PickableGLElement;
import org.caleydo.core.view.opengl.layout2.basic.ScrollingDecorator.IHasMinSize;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.renderer.AtomContainerRenderer;
import org.openscience.cdk.renderer.color.IAtomColorer;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator;
import org.openscience.cdk.renderer.generators.IGenerator;
import org.openscience.cdk.renderer.generators.IGeneratorParameter;
import org.openscience.cdk.renderer.generators.RingGenerator;
import org.openscience.cdk.renderer.generators.parameter.AbstractGeneratorParameter;
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;
import org.openscience.cdk.smiles.SmilesParser;

import com.jogamp.opengl.util.awt.TextureRenderer;

/**
 * element of this view
 *
 * @author Hendrik Strobelt
 *
 */
public class CompoundsElement extends PickableGLElement implements IHasMinSize {
	private static final Logger log = Logger.create(CompoundsElement.class);

	private final String smile;
	private final IAtomContainer molecul;
	private final AtomContainerRenderer renderer;

	private TextureRenderer textureRenderer;

	private boolean subComponentWarning = false;

	private String name;

	private static class MoleluleConversionFeedback {
		public boolean isSubComponent = false;

	}

	public CompoundsElement(String smile, String name, float sizeX, float sizeY) {
		this(smile, name);
		setSize(sizeX, sizeY);
	}

	public CompoundsElement(String smile) {
		this(smile, null);
	}

	private static class IndividualColors implements IAtomColorer {

		private static final java.awt.Color nColor = new java.awt.Color(43, 140, 190);
		private static final java.awt.Color oColor = new java.awt.Color(227, 74, 51);
		private static final java.awt.Color clColor = new java.awt.Color(116, 196, 118);

		public IndividualColors() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public java.awt.Color getAtomColor(IAtom atom) {
			// System.out.println(atom.getAtomTypeName());
			if (atom.getAtomTypeName().startsWith("N."))
				return nColor;
			else if (atom.getAtomTypeName().startsWith("O."))
				return oColor;
			else if (atom.getAtomTypeName().startsWith("Cl"))
				return clColor;

			return java.awt.Color.DARK_GRAY;
		}

		@Override
		public java.awt.Color getAtomColor(IAtom atom, java.awt.Color defaultColor) {
			// TODO Auto-generated method stub
			return getAtomColor(atom);
		}

	}

	public static class AtomColor extends AbstractGeneratorParameter<Color> {
		/**
		 * Returns the default value.
		 *
		 * @return {@link Color}.BLACK
		 */
		@Override
		public Color getDefault() {
			return Color.BLACK;
		}
	}

	/**
	 * @param string
	 */
	public CompoundsElement(String smile, String name) {
		this.smile = smile;
		this.name = name;

		// init moleculeRendering steps
		// generators make the image elements
		List<IGenerator<IAtomContainer>> generators = new ArrayList<>();
		generators.add(new BasicSceneGenerator());
		// generators.add(new BasicBondGenerator());
		generators.add(new RingGenerator());
		BasicAtomGenerator basicAtomGenerator = new BasicAtomGenerator();
		((IGeneratorParameter<IAtomColorer>) basicAtomGenerator.getParameters().get(1))
				.setValue(new IndividualColors());
		;

		generators.add(basicAtomGenerator);
		// the renderer needs to have a toolkit-specific font manager
		renderer = new AtomContainerRenderer(generators, new AWTFontManager());

		// when rendering the molecule add a warning, when only the largest
		// subcomponent is rendered.
		MoleluleConversionFeedback moleluleConversionFeedback = new MoleluleConversionFeedback();
		this.molecul = toMolecul(smile, moleluleConversionFeedback);
		subComponentWarning = moleluleConversionFeedback.isSubComponent;
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

	private static IAtomContainer toMolecul(String smile) {
		return toMolecul(smile, null);
	}

	/**
	 * @param smile2
	 * @return
	 */
	private static IAtomContainer toMolecul(String smile, MoleluleConversionFeedback feedback) {
		IAtomContainer molecule;
		try {
			SmilesParser smilesParser = new SmilesParser(DefaultChemObjectBuilder.getInstance());
			smilesParser.setPreservingAromaticity(true);
			// System.out.println(smilesParser.isPreservingAromaticity());
			molecule = smilesParser.parseSmiles(smile);

			StructureDiagramGenerator sdg = new StructureDiagramGenerator();
			sdg.setMolecule(molecule);
			try {
				if (ConnectivityChecker.isConnected(molecule)) {
					sdg.generateCoordinates();
					if (feedback != null) {
						feedback.isSubComponent = false;
					}
				} else {
					// if the smiles contains unconnected components -- show
					// only the largest connected component
					IAtomContainerSet partitionIntoMolecules = ConnectivityChecker.partitionIntoMolecules(molecule);
					int containerCount = partitionIntoMolecules.getAtomContainerCount();
					int maxAtomCount = 0;
					IAtomContainer largestMolecule = null;

					for (int cc = 0; cc < containerCount; cc++) {
						IAtomContainer actualMol = partitionIntoMolecules.getAtomContainer(cc);
						int molsize = actualMol.getAtomCount();
						if (molsize > maxAtomCount) {
							maxAtomCount = molsize;
							largestMolecule = actualMol;
						}
					}
					molecule = largestMolecule;
					sdg.setMolecule(molecule);
					sdg.generateCoordinates();

					if (feedback != null) {
						feedback.isSubComponent = true;
					}

				}
			} catch (Exception ex) {
				log.warn("can't generate coordinates for " + smile, ex);
				// System.out.println("COORD: " + ex);
				System.exit(1);
			}
			return sdg.getMolecule();
		} catch (InvalidSmilesException e) {
			log.error("invalid smile string: " + smile, e);
		}
		return null;
	}

	@Override
	public Vec2f getMinSize() {
		return new Vec2f(300, 165);
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

		g.fillImage(textureRenderer.getTexture(), 0, 20, w, h - 20);

		if (this.name != null) {
			g.drawText(this.name, 20, 2, 200, 15);
		}

		if (subComponentWarning) {

			g.textColor(Color.RED);
			g.drawText("disconnected structure:\nshow largest subcomponent", 20, 17, 200, 30);
			g.textColor(Color.BLACK);
		}

		// float size = (w>h)?h:w;
		// g.fillImage(textureRenderer.getTexture(), (w-size)/2, (h-size)/2,
		// size, size);

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
		GLSandBox.main(args, new CompoundsElement("Clc3ccc(COC(CN1C=CN=C1)c2ccc(Cl)cc2Cl)c(Cl)c3"));
		// new CompoundsElement("ClC1CCC(C(C1)Cl)COC(C2CCC(CC2Cl)Cl)CN3C=CN=C3"));
		// new CompoundsElement("CN2C(=O)N(C)C(=O)C1=C2N=CN1CCl"));
		// new CompoundsElement(Lists.newArrayList(Pair.<String, String> make("CompoundName",
		// "CN2C(=O)N(C)C(=O)C1=C2N=CN1C"))));
	}

	/**
	 * @return the name, see {@link #name}
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the smile, see {@link #smile}
	 */
	public String getSmile() {
		return smile;
	}
}
