/***********************************************
This file is part of the Calao project (https://github.com/Neonunux/calao/wiki).

Calao is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Calao is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Calao.  If not, see <http://www.gnu.org/licenses/>.

 **********************************************/
package calao;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextArea;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Class RoundedButton.
 *
 * @author Neonunux
 */
public class RoundedButton extends JButton {

	private static final Logger logger = LogManager
			.getLogger(RoundedButton.class.getName());

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8458705986423151858L;

	/** The b label. */
	private String bLabel;

	/** The app bundle. */
	private ResourceBundle appBundle;

	/** The img. */
	private Image img = null;

	/** The end color. */
	private Color endColor = Color.decode("0x4D5D8F");

	/** The font size. */
	private int fontSize = 20;

	/** The text off y. */
	private int textOffX = 0, textOffY = 0;

	/** The img w. */
	private int imgW = -1;

	/** The img h. */
	private int imgH = -1;

	private boolean border = false;

	private boolean gradientPaint = false;

	private int borderWidth = 3;

	/**
	 * Instantiates a new rounded button.
	 *
	 * @param label
	 *            the label
	 * @param b
	 *            the b
	 */
	public RoundedButton(String label, ResourceBundle b) {
		super(label);
		bLabel = label;
		appBundle = b;
	}

	/**
	 * Instantiates a new rounded button.
	 *
	 * @param label
	 *            the label
	 * @param b
	 *            the b
	 * @param eC
	 *            the e c
	 */
	public RoundedButton(String label, ResourceBundle b, Color eC) {
		super(label);
		bLabel = label;
		appBundle = b;
		endColor = eC;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.AbstractButton#setLabel(java.lang.String)
	 */
	public void setLabel(String label) {
		bLabel = label;
		repaint();
	}

	/**
	 * Sets the res bundle.
	 *
	 * @param b
	 *            the new res bundle
	 */
	public void setResBundle(ResourceBundle b) {
		appBundle = b;
		repaint();
	}

	/**
	 * Sets the font size.
	 *
	 * @param size
	 *            the new font size
	 */
	public void setFontSize(int size) {
		fontSize = size;
	}

	/**
	 * Sets the text offsets.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void setTextOffsets(int x, int y) {
		textOffX = x;
		textOffY = y;
		// repaint();
	}

	/**
	 * Sets the button image.
	 *
	 * @param i
	 *            the new button image
	 */
	public void setButtonImage(Image i) {
		img = i;
	}

	/**
	 * Sets the imag size.
	 *
	 * @param w
	 *            the w
	 * @param h
	 *            the h
	 */
	public void setImageSize(int w, int h) {
		imgW = w;
		imgH = h;
		repaint();
	}

	public boolean hasBorder() {
		return border;
	}

	public void setBorder(boolean border) {
		this.border = border;
	}

	public boolean isGradientPainted() {
		return gradientPaint;
	}

	public void setGradientPaint(boolean gradientPaint) {
		this.gradientPaint = gradientPaint;
	}

	// Draw the button
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Color bgColor;
		Color tmpColor = endColor;
		int bWidth = getBorderWidth();

		if (this.isEnabled() == false) {
			g.setColor(Color.decode("0x666666"));
			g.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
			if (this.hasBorder()) {
				g.setColor(Color.decode("0xBBBBBB"));
				g.fillRoundRect(getBorderWidth(), getBorderWidth(),
						getSize().width - 2 * bWidth, getSize().height - 2
								* bWidth, 27, 27);
			}
			g.setColor(Color.decode("0x888888"));
		} else {
			if (this.hasBorder()) {
				g.setColor(Color.decode("0x5F8DD3"));
				g.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
			}

			if (getModel().isArmed()) // is button being clicked ?
				bgColor = Color.decode("0x869EBA");
			else if (getModel().isRollover()) // rollover effect
			{
				bgColor = Color.decode("0xB8D8FF");
				tmpColor = Color.decode("0x667BBD");
			} else {
				bgColor = getBackground(); // normal state
			}

			((Graphics2D) g).setPaint(bgColor);

			if (this.isGradientPainted()) {
				// gradient fill:
				GradientPaint vertGrad = new GradientPaint(0, 0, bgColor, 0,
						getHeight(), tmpColor);
				((Graphics2D) g).setPaint(vertGrad);
			}
			g.fillRoundRect(getBorderWidth(), getBorderWidth(), getSize().width
					- 2 * bWidth, getSize().height - 2 * bWidth, 27, 27);
			g.setColor(Color.black);
		}

		int textWidth = 0;
		int vOffset = getHeight() / 2;
		int hOffset = getWidth() / 2;
		int OffsetContentPositionImage = 50;
		int OffsetContentPositionText = 50;
		/*
		 * lecture de notes // rythmes // partition // statistiques // Exercices
		 * // Ear Training
		 */

		if (bLabel == "RBL_INLINE") {
			String title = appBundle.getString("_menuNotereading");
			setContentButton(g, "lecture.png", OffsetContentPositionImage,
					OffsetContentPositionText, title);
		} else if (bLabel == "RBL_RHYTHM") {
			String title = appBundle.getString("_menuRythmreading");
			setContentButton(g, "rythm.png", OffsetContentPositionImage,
					OffsetContentPositionText, title);
		} else if (bLabel == "RBL_SCORE") {
			String title = appBundle.getString("_menuScorereading");
			setContentButton(g, "score.png", OffsetContentPositionImage,
					OffsetContentPositionText, title);
		} else if (bLabel == "RBL_NOTES") {
			String titlep1 = appBundle.getString("_menuClef");
			String titlep2 = appBundle.getString("_menuNotes");
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 12));
			FontMetrics fM = g.getFontMetrics();
			textWidth = fM.stringWidth(titlep1) / 2;
			g.drawString(titlep1, 15 + 25 - textWidth, vOffset - 10);
			fM = g.getFontMetrics();
			textWidth = fM.stringWidth("&") / 2;
			g.drawString("&", 15 + 25 - textWidth, vOffset + 5);
			fM = g.getFontMetrics();
			textWidth = fM.stringWidth(titlep2) / 2;
			g.drawString(titlep2, 15 + 25 - textWidth, vOffset + 20);
			// g.fillRoundRect(80, vOffset - 25, 70, 50, 15, 15); // 22
			g.setFont(getFont().deriveFont(27f));
			String ss = "" + (char) 0xA9 + (char) 0xA9 + (char) 0xA9
					+ (char) 0xA9; // staff symbol
			String sm = "" + (char) 0xF4;
			g.setColor(Color.black);
			g.drawString(ss, 87, vOffset + 11);
			g.setFont(getFont().deriveFont(35f));
			g.drawString("G", 90, vOffset + 12);
			g.drawString(sm, 110, vOffset + 18);
			g.drawString(sm, 130, vOffset + 11);
		} else if (bLabel == "RBL_STATS") {
			String title = appBundle.getString("_menuStatistics");
			setContentButton(g, "statistics.png", OffsetContentPositionImage,
					OffsetContentPositionText, title);
		} else if (bLabel == "RBL_LESSONS") {
			String title = appBundle.getString("_menuExercises");
			setContentButton(g, "lessons.png", OffsetContentPositionImage,
					OffsetContentPositionText, title);
		} else if (bLabel == "RBL_EARTRAIN") {
			String title = appBundle.getString("_menuEarTraining");
			setContentButton(g, "eartrain.png", OffsetContentPositionImage,
					OffsetContentPositionText, title);
		} else {
			FontMetrics fM1 = g.getFontMetrics(this.getFont());
			textWidth = fM1.stringWidth(bLabel);
			g.drawString(bLabel,
					textOffX + ((getSize().width - textWidth) / 2),
					textOffY + 25);
		}

		if (img != null) {
			if (imgW != -1)
				g.drawImage(img, (getWidth() - imgW) / 2,
						(getHeight() - imgH) / 2, imgW, imgH, this);
			else
				g.drawImage(img, (getWidth() - img.getWidth(null)) / 2,
						(getHeight() - img.getHeight(null)) / 2, null);
		}
	}

	/**
	 * @param g
	 * @param ImageName
	 * @param OffsetContentPositionImage
	 * @param OffsetContentPositionText
	 * @param bundleStringName
	 */
	private void setContentButton(Graphics g, String ImageName,
			int OffsetContentPositionImage, int OffsetContentPositionText,
			String bundleStringName) {
		int textWidth;
		BufferedImage image;
		try {
			image = ImageIO.read(getClass().getResource(ImageName));
			g.drawImage(image, (getSize().width - image.getWidth()) / 2,
					OffsetContentPositionImage, null);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String titleContent = bundleStringName;

		Font serifFont = new Font("Arial", Font.BOLD, fontSize);

		AttributedString as = new AttributedString(titleContent);
		as.addAttribute(TextAttribute.FONT, serifFont);
		as.addAttribute(TextAttribute.FOREGROUND, Color.red);

		textWidth = (int)Math.floor( getWidthOfAttributedString((Graphics2D) g, as) + 0.5d);

		g.drawString(as.getIterator(), (getSize().width - textWidth) / 2,
				OffsetContentPositionText);
	}
	
	double getWidthOfAttributedString(Graphics2D graphics2D, AttributedString attributedString) {
	    AttributedCharacterIterator characterIterator = attributedString.getIterator();
	    FontRenderContext fontRenderContext = graphics2D.getFontRenderContext();
	    LineBreakMeasurer lbm = new LineBreakMeasurer(characterIterator, fontRenderContext);
	    TextLayout textLayout = lbm.nextLayout(Integer.MAX_VALUE);
	    return textLayout.getBounds().getWidth();
	}
	
	public int getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
	}

}
