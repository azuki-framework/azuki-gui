/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.azkfw.gui.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * このクラスは、イメージの表示を行うパネルクラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/16
 * @author kawakita
 */
public class ImagePanel extends JPanel {

	/** serialVersionUID */
	private static final long serialVersionUID = -7505623591121959405L;

	/** image */
	private Image image;
	/** image width (pixel) */
	private int imageWidth;
	/** image height (pixel) */
	private int imageHeight;

	/** cash image */
	private Image cashImage;
	/** cash image width (pixel) */
	private int cashImageWidth;
	/** cash image height (pixel) */
	private int cashImageHeight;

	/** background image */
	private Image backgroundImage;

	/**
	 * コンストラクタ
	 * 
	 * @param aImage イメージ
	 */
	public ImagePanel() {
		image = null;
		imageWidth = -1;
		imageHeight = -1;

		cashImage = null;
		cashImageWidth = -1;
		cashImageHeight = -1;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param aImage イメージ
	 */
	public ImagePanel(final Image aImage) {
		setImage(aImage);
	}

	/**
	 * イメージを設定する。
	 * 
	 * @param aImage イメージ
	 */
	public void setImage(final Image aImage) {
		image = aImage;
		imageWidth = image.getWidth(this);
		imageHeight = image.getHeight(this);

		cashImage = null;
		cashImageWidth = -1;
		cashImageHeight = -1;

		repaint();
	}

	@Override
	public void paint(final Graphics g) {
		if (null != image) {
			int width = getWidth();
			int height = getHeight();

			double perX = (double) imageWidth / (double) width;
			double perY = (double) imageHeight / (double) height;
			double per = Math.max(perX, perY);

			int sWidth = (int) (imageWidth / per);
			int sHeight = (int) (imageHeight / per);

			if (sWidth != cashImageWidth || sHeight != cashImageHeight) {
				cashImageWidth = sWidth;
				cashImageHeight = sHeight;
				cashImage = image.getScaledInstance(sWidth, sHeight, Image.SCALE_SMOOTH);

				backgroundImage = createBackgroundImage(cashImageWidth, cashImageHeight, 12);
			}

			g.setColor(Color.black);
			g.fillRect(0, 0, width, height);

			int x = (width - cashImageWidth) / 2;
			int y = (height - cashImageHeight) / 2;

			g.drawImage(backgroundImage, x, y, this);
			g.drawImage(cashImage, x, y, this);
		}
	}

	private Image createBackgroundImage(final int width, final int height, final int size) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();

		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);

		g.setColor(new Color(200, 200, 200, 255));
		for (int row = 0; row < cashImageHeight / size + 1; row++) {
			for (int col = 0; col < cashImageWidth / size + 1; col++) {
				if ((col % 2) == (row % 2)) {
					g.fillRect(col * size, row * size, size, size);
				}
			}
		}
		return image;
	}
}
