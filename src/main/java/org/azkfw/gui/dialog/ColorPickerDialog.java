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
package org.azkfw.gui.dialog;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.azkfw.gui.component.ColorPickerPanel;

/**
 * このクラスは、カラーピッカー機能を実装した設定ダイアログクラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/31
 * @author kawakicchi
 */
public class ColorPickerDialog extends ConfigurationDialog<Color> {

	/** serialVersionUID */
	private static final long serialVersionUID = 4311272267483599998L;

	/** Color picker panel */
	private ColorPickerPanel panel;

	/**
	 * コンストラクタ
	 * 
	 * @param aData Data
	 */
	public ColorPickerDialog(final Color aData) {
		super(aData);
		init(aData);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param aFrame Frame
	 * @param aData Data
	 */
	public ColorPickerDialog(final Frame aFrame, final Color aData) {
		super(aFrame, aData);
		init(aData);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param aDialog Dialog
	 * @param aData Data
	 */
	public ColorPickerDialog(final Dialog aDialog, final Color aData) {
		super(aDialog, aData);
		init(aData);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param aFrame Frame
	 * @param aModal Modal
	 * @param aData Data
	 */
	public ColorPickerDialog(final Frame aFrame, final boolean aModal, final Color aData) {
		super(aFrame, aModal, aData);
		init(aData);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param aDialog Dialog
	 * @param aModal Modal
	 * @param aData Data
	 */
	public ColorPickerDialog(final Dialog aDialog, final boolean aModal, final Color aData) {
		super(aDialog, aModal, aData);
		init(aData);
	}

	/**
	 * カラーを設定する。
	 * 
	 * @param aColor カラー
	 */
	public void setColor(final Color aColor) {
		panel.setColor(aColor);
	}

	/**
	 * カラーを取得する。
	 * 
	 * @return カラー
	 */
	public Color getColor() {
		return panel.getColor();
	}

	@Override
	protected boolean isValidate() {
		return true;
	}

	@Override
	protected boolean doClickOK(Color color) {
		color = panel.getColor();
		return true;
	}

	@Override
	protected boolean doClickCancel() {
		return true;
	}

	private void init(final Color aColor) {
		setTitle("カラー選択ダイアログ");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		panel = new ColorPickerPanel(aColor);
		panel.setLocation(0, 0);
		add(panel);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				Insets insets = getClientInsets();
				int width = getWidth() - (insets.left + insets.right);
				int height = getHeight() - (insets.top + insets.bottom);
				panel.setSize(width, height);
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(final WindowEvent event) {
				Insets insets = getClientInsets();
				int width = 400 + (insets.left + insets.right);
				int height = 400 + (insets.top + insets.bottom);
				setMinimumSize(new Dimension(width, height));
				setSize(width, height);
			}
		});
	}
}
