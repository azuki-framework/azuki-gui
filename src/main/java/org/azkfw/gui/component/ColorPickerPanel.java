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
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;

/**
 * このクラスは、カラーピッカー機能を実装したパネルクラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/31
 * @author kawakicchi
 */
public class ColorPickerPanel extends JPanel {

	/** serialVersionUID */
	private static final long serialVersionUID = -5305794517812285112L;

	/** Hue - 色合い */
	private float hue = 0.f;
	/** Saturation - 彩度 */
	private float saturation = 1.f;
	/** Brightness - 明度 */
	private float brightness = 1.f;

	/**
	 * コンストラクタ
	 */
	public ColorPickerPanel() {
		this(null);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param aColor カラー
	 */
	public ColorPickerPanel(final Color aColor) {
		setBackground(Color.white);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				doMouse(e);
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				doMouse(e);
			}
		});
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent event) {

			}
		});

		if (null != aColor) {
			setColor(aColor);
		}
	}

	/**
	 * カラーを設定する。
	 * 
	 * @param aColor カラー
	 */
	public void setColor(final Color aColor) {
		if (null != aColor) {
			float[] hsb = new float[3];
			Color.RGBtoHSB(aColor.getRed(), aColor.getGreen(), aColor.getBlue(), hsb);

			hue = hsb[0];
			saturation = hsb[1];
			brightness = hsb[2];
		}
	}

	/**
	 * カラーを設定する。
	 * 
	 * @return カラー
	 */
	public Color getColor() {
		Color color = Color.getHSBColor(hue, saturation, brightness);
		return color;
	}

	@Override
	public void paintComponent(Graphics aGraphics) {
		Graphics2D g = (Graphics2D) aGraphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int width = getWidth();
		int height = getHeight();
		int arcSize = Math.min(width, height); // H直径
		int douSize = arcSize / 8; // H幅
		int arcX = (width - arcSize) / 2; // H左上X
		int arcY = (height - arcSize) / 2; // H左上Y

		Color back = getBackground();
		g.setColor(back);
		g.fillRect(0, 0, width, height);

		// Draw H
		for (int i = 0; i < 360; i++) {
			Color color = Color.getHSBColor((float) i / 360.f, 1.f, 1.f);
			g.setColor(color);
			g.fillArc(arcX, arcY, arcSize, arcSize, i, 2);
		}
		g.setColor(back);
		g.fillArc(arcX + douSize, arcY + douSize, (arcSize - douSize * 2), (arcSize - douSize * 2), 0, 360);
		//g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		g.setColor(Color.gray);
		g.drawArc(arcX, arcY, arcSize, arcSize, 0, 360);
		g.drawArc(arcX + douSize, arcY + douSize, (arcSize - douSize * 2), (arcSize - douSize * 2), 0, 360);

		// Draw SB
		int x = arcX + douSize * 2;
		int y = arcY + douSize * 2;
		width = arcSize - (douSize * 2 * 2);
		height = arcSize - (douSize * 2 * 2);
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				Color color = Color.getHSBColor(hue, 1.f - ((float) col / (float) width), 1.f - ((float) row / (float) height));
				g.setColor(color);
				g.fillRect(x + col, y + row, 1, 1);
			}
		}
		g.setColor(Color.gray);
		g.drawRect(x, y, width, height);

		int sizePoint = douSize / 5 * 3;
		// Draw point H
		double rnd = (hue * 360.f) * Math.PI / 180.0;
		x = (int) (arcX + (arcSize / 2) + ((arcSize / 2 - douSize / 2) * Math.cos(rnd)));
		y = (int) (arcY + (arcSize / 2) - ((arcSize / 2 - douSize / 2) * Math.sin(rnd)));
		g.setColor(Color.gray);
		g.fillArc(x - (sizePoint / 2), y - (sizePoint / 2), sizePoint, sizePoint, 0, 360);
		g.setColor(Color.getHSBColor(hue, 1.f, 1.f));
		g.fillArc(x - (sizePoint / 2 - 2), y - (sizePoint / 2 - 2), sizePoint - 4, sizePoint - 4, 0, 360);

		// Draw point SB
		x = (int) (arcX + douSize * 2 + (1.f - saturation) * (arcSize - (douSize * 2 * 2)));
		y = (int) (arcY + douSize * 2 + (1.f - brightness) * (arcSize - (douSize * 2 * 2)));
		g.setColor(Color.gray);
		g.fillArc(x - (sizePoint / 2), y - (sizePoint / 2), sizePoint, sizePoint, 0, 360);
		g.setColor(Color.getHSBColor(hue, saturation, brightness));
		g.fillArc(x - (sizePoint / 2 - 2), y - (sizePoint / 2 - 2), sizePoint - 4, sizePoint - 4, 0, 360);

	}

	private void doMouse(final MouseEvent event) {
		int width = getWidth();
		int height = getHeight();
		int arcSize = Math.min(width, height); // H直径
		int douSize = arcSize / 8; // H幅
		int radius = arcSize / 4 * 3 / 2;
		int arcX = (width - arcSize) / 2; // H左上X
		int arcY = (height - arcSize) / 2; // H左上Y

		Point point = event.getPoint();

		int x = (point.x - (width / 2));
		int y = (height / 2) - point.y;
		double distance = Math.sqrt(x * x + y * y);

		int sbX = arcX + douSize * 2;
		int sbY = arcY + douSize * 2;
		int sbWidth = arcSize - (douSize * 2 * 2);
		int sbHeight = arcSize - (douSize * 2 * 2);

		if (distance >= radius && distance < radius + douSize) {
			double radian = Math.atan2(y, x);
			double angle = radian * 180.0 / Math.PI;
			hue = (float) angle / 360.f;

			repaint();
		} else if (point.x >= sbX && point.x < sbX + sbWidth && point.y >= sbY && point.y < sbY + sbHeight) {
			saturation = 1.f - (float) (point.x - sbX) / (float) sbWidth;
			brightness = 1.f - (float) (point.y - sbY) / (float) sbHeight;

			repaint();
		}
	}
}
