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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.azkfw.gui.dialog.ColorPickerDialog;
import org.azkfw.gui.dialog.ConfigurationDialogAdapter;
import org.azkfw.gui.dialog.ConfigurationDialogEvent;
import org.azkfw.gui.validate.ValidationSupport;
import org.azkfw.util.StringUtility;

/**
 * @since 1.0.0
 * @version 1.0.0 2014/10/31
 * @author kawakicchi
 */
public class ColorTextField extends JPanel implements ValidationSupport {

	/** serialVersionUID */
	private static final long serialVersionUID = 3622070142454877290L;

	/** テキストフィールド */
	private JTextField text;
	/** 選択ボタン */
	private JButton button;

	private boolean enableValidate;

	/**
	 * コンストラクタ
	 */
	public ColorTextField() {
		this(null);
	}

	public ColorTextField(final Color aColor) {
		enableValidate = true;

		Dimension dm = getMinimumSize();
		dm.width = 160;
		setMinimumSize(dm);
		setSize(dm);

		text = new JTextField();
		button = new JButton("選択");

		setLayout(null);

		add(text);
		add(button);

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {
				doColorPickerDialog();
			}
		});
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent event) {
				int width = getWidth();
				int height = getHeight();
				Dimension dm = button.getPreferredSize();

				text.setBounds(0, 0, width - dm.width, height);
				button.setBounds(width - dm.width, 0, dm.width, height);
			}
		});

		setColor(aColor);
	}

	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		text.setEditable(enabled);
		button.setEnabled(enabled);
	}

	public void setText(final String aText) {
		text.setText(aText);
	}

	public String getText() {
		return text.getText();
	}

	public void setColor(final Color aColor) {
		String string = rgbToString(aColor);
		text.setText(string);
	}

	public Color getColor() {
		Color color = stringToRgb(text.getText());
		return color;
	}

	public void setEnableValidate(final boolean enable) {
		enableValidate = enable;
	}

	@Override
	public boolean isEnableValidate() {
		return enableValidate;
	}

	@Override
	public boolean isValidate() {
		boolean result = false;
		String string = text.getText();
		if (StringUtility.isNotEmpty(string)) {
			String[] strs = string.split(",");
			if (3 == strs.length) {
				try {
					Integer.parseInt(strs[0].trim());
					Integer.parseInt(strs[1].trim());
					Integer.parseInt(strs[2].trim());
					return true;
				} catch (NumberFormatException ex) {
					//
				}
			}
		} else {
			return true;
		}
		return result;
	}

	private void doColorPickerDialog() {
		ColorPickerDialog dialog = new ColorPickerDialog(stringToRgb(text.getText()));

		dialog.addConfigurationDialogListener(new ConfigurationDialogAdapter() {
			@Override
			public void configurationDialogOk(final ConfigurationDialogEvent event, final Object data) {
				ColorPickerDialog dialog = (ColorPickerDialog) event.getDialog();
				text.setText(rgbToString(dialog.getColor()));
			}
		});

		dialog.setModal(true);
		dialog.setVisible(true);
	}

	private String rgbToString(final Color aColor) {
		String string = "";
		if (null != aColor) {
			string = String.format("%d, %d, %d", aColor.getRed(), aColor.getGreen(), aColor.getBlue());
		}
		return string;
	}

	private Color stringToRgb(final String aString) {
		Color color = null;
		if (StringUtility.isNotEmpty(aString)) {
			String[] strs = aString.split(",");
			if (3 == strs.length) {
				try {
					int r = Integer.parseInt(strs[0].trim());
					int g = Integer.parseInt(strs[1].trim());
					int b = Integer.parseInt(strs[2].trim());
					color = new Color(r, g, b);
				} catch (NumberFormatException ex) {
					//
				}
			}
		}
		return color;
	}
}
