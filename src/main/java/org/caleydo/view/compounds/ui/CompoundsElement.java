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
import org.caleydo.core.view.opengl.layout2.GLElement;
import org.caleydo.core.view.opengl.layout2.GLGraphics;
import org.caleydo.core.view.opengl.layout2.basic.ScrollingDecorator.IHasMinSize;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.renderer.AtomContainerRenderer;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
import org.openscience.cdk.renderer.generators.BasicBondGenerator;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator;
import org.openscience.cdk.renderer.generators.IGenerator;
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.templates.MoleculeFactory;

import com.jogamp.opengl.util.awt.TextureRenderer;

/**
 * element of this view
 *
 * @author Hendrik Strobelt
 *
 */
public class CompoundsElement extends GLElement implements IHasMinSize {

	private final String smile;
	private TextureRenderer textureRenderer;
	
	private List generators = new ArrayList();
	private String currentSmile = "";
	private AtomContainerRenderer actualRenderer =  null;
	private IMolecule actualMolecul = null;
	private int actualWidth = 1;
	private int actualHeight = 1;

	/**
	 * @param string
	 */
	@SuppressWarnings("unchecked")
	public CompoundsElement(String smile) {
		this.smile = smile;
		textureRenderer = new TextureRenderer(800, 800, false);
		Graphics2D graphics = textureRenderer.createGraphics();
		graphics.drawLine(10, 10, 300, 300);
		graphics.dispose();
		
		// init moleculeRendering steps
		// generators make the image elements
		generators.add(new BasicSceneGenerator());
		generators.add(new BasicBondGenerator());
		generators.add(new BasicAtomGenerator());
		// the renderer needs to have a toolkit-specific font manager
		actualRenderer = new AtomContainerRenderer(
				generators, new AWTFontManager());
		
		
	}

	@Override
	public Vec2f getMinSize() {
		return new Vec2f(100, 100);
	}

	@Override
	protected void renderImpl(GLGraphics g, float w, float h) {
		super.renderImpl(g, w, h);

		g.color(Color.WHITE).fillRect(0, 0, w, h);
		
		renderSmiles(smile,w,h);
		g.fillImage(textureRenderer.getTexture(), 0,0, w, h);
//		float size = (w>h)?h:w;
//		g.fillImage(textureRenderer.getTexture(), (w-size)/2, (h-size)/2, size, size);
		
		
	}
	
	private void renderSmiles(String smileString, float w, float h) {
		try{
			int WIDTH = (int) w;
			int HEIGHT = (int) h;
			

			// only update structure if smiles string changed
			if (!currentSmile.equals(smileString)){
				actualMolecul = MoleculeFactory.makeCyclobutane();

				Molecule molecule = (Molecule) new SmilesParser(
						DefaultChemObjectBuilder.getInstance())
						.parseSmiles(smileString);

				StructureDiagramGenerator sdg = new StructureDiagramGenerator();
				sdg.setMolecule(molecule);
				try {
					sdg.generateCoordinates();
				} catch (Exception ex) {

				}
				actualMolecul = sdg.getMolecule();
				
			}
			
			// only update texture if something changed
			if (!currentSmile.equals(smileString) || actualHeight!=HEIGHT || actualWidth!=WIDTH) {
				
				actualHeight= HEIGHT;
				actualWidth = WIDTH;
				currentSmile = smileString;
				
				// the draw area and the image should be the same size
				Rectangle drawArea = new Rectangle(WIDTH, HEIGHT);

				// center molecule
				actualRenderer.setup(actualMolecul, drawArea);

				// zoom to fit in rectangle..  
				Rectangle diagramBounds = actualRenderer.calculateDiagramBounds(actualMolecul);
				actualRenderer.setZoomToFit(WIDTH, HEIGHT, diagramBounds.width,
						diagramBounds.height);
				
				// and render it in a new texture
				textureRenderer = new TextureRenderer(WIDTH, HEIGHT, false);
				Graphics2D graphics = textureRenderer.createGraphics();
				graphics.setColor(java.awt.Color.WHITE);
		        graphics.fillRect(0, 0, WIDTH, HEIGHT);
				actualRenderer.paint(actualMolecul, new AWTDrawVisitor(graphics));
				graphics.dispose();

			}
			
			
			
			
			
		} catch (InvalidSmilesException ex) {
			// Logger.getLogger(ImageRenderer.class.getName()).log(Level.SEVERE,
			// null, ex);
		}
	      
	}

	
	
	
}
