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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import org.azkfw.gui.validate.ValidationSupport;
import org.azkfw.util.StringUtility;

/**
 * このクラスは、設定ダイアログ画面で使用するフィールド情報を保持クラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/22
 * @author kawakicchi
 */
public class MultiConfigurationField extends ConfigurationField implements ValidationSupport {

	/** serialVersionUID */
	private static final long serialVersionUID = 2837802657740541747L;

	private static final int MARGIN = 4;
	private static final int SPACE = 8;

	private JLabel label;
	private int labelWidth;

	private List<Component> components;
	private boolean fixComponentWidth;

	private List<Component> lblSeparators;
	private int separatorWidth;

	public MultiConfigurationField(final String aLabel, final Component... aComponents) {
		this(aLabel, null, aComponents);
	}

	public MultiConfigurationField(final String aLabel, final String aSeparateString, final Component... aComponents) {
		super();

		label = new JLabel(aLabel);

		separatorWidth = 0;
		if (StringUtility.isNotEmpty(aSeparateString)) {
			lblSeparators = new ArrayList<Component>();
			for (int i = 1; i < aComponents.length; i++) {
				JLabel lbl = new JLabel(aSeparateString);
				lblSeparators.add(lbl);
				Dimension dm = lbl.getPreferredSize();
				separatorWidth = dm.width;
				lbl.setSize(dm);
			}
		}

		components = new ArrayList<Component>();
		for (int i = 0; i < aComponents.length; i++) {
			components.add(aComponents[i]);
		}

		fixComponentWidth = true;
		for (Component comp : components) {
			if (0 == comp.getWidth()) {
				fixComponentWidth = false;
				break;
			}
		}

		add(label);
		for (int i = 0; i < components.size(); i++) {
			if (null != lblSeparators && 0 != i) {
				add(lblSeparators.get(i - 1));
			}
			add(components.get(i));
		}

		labelWidth = getPreferredLabelWidth();

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent event) {
				int x = MARGIN;
				int y = MARGIN;
				int width = getWidth();
				int height = getHeight();

				label.setBounds(x, y, labelWidth, height - (MARGIN * 2));
				x += labelWidth + SPACE;

				if (fixComponentWidth) {
					for (int i = 0; i < components.size(); i++) {
						if (null != lblSeparators && 0 != i) {
							Component c = lblSeparators.get(i - 1);
							c.setLocation(x, y);
							x += c.getWidth();
						}
						Component c = components.get(i);
						c.setBounds(x, y, c.getWidth(), height - (MARGIN * 2));
						x += c.getWidth();
					}
				} else {
					int compWidth = 0;
					if (null == lblSeparators) {
						compWidth = (width - (x + MARGIN)) / components.size();
					} else {
						compWidth = (width - (x + MARGIN + separatorWidth * lblSeparators.size())) / components.size();
					}

					for (int i = 0; i < components.size(); i++) {
						if (null != lblSeparators && 0 != i) {
							Component c = lblSeparators.get(i - 1);
							c.setLocation(x, y);
							x += c.getWidth();
						}
						Component c = components.get(i);
						c.setBounds(x, y, compWidth, height - (MARGIN * 2));
						x += compWidth;
					}
				}
			}
		});
	}

	@Override
	public int getPreferredHeight() {
		return 32;
	}

	@Override
	public void setLabelWidth(final int aWidth) {
		labelWidth = aWidth;
	}

	@Override
	public int getPreferredLabelWidth() {
		Dimension dm = label.getPreferredSize();
		return dm.width;
	}

	@Override
	public boolean isValidate() {
		for (Component component : components) {
			if (null != component) {
				if (component instanceof ValidationSupport) {
					ValidationSupport support = (ValidationSupport) component;
					if (!support.isValidate()) {
						return false;
					}
				}
			}
		}
		return true;
	}
}
