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

import javax.swing.JLabel;

import org.azkfw.gui.validate.ValidationSupport;

/**
 * このクラスは、設定ダイアログ画面で使用するフィールド情報を保持クラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/22
 * @author kawakicchi
 */
public class BasicConfigurationField extends ConfigurationField implements ValidationSupport {

	/** serialVersionUID */
	private static final long serialVersionUID = -7555708497329910187L;

	private static final int MARGIN = 4;
	private static final int SPACE = 8;

	private JLabel label;
	private int labelWidth;

	private Component component;
	private boolean fixComponentWidth;

	public BasicConfigurationField(final String aLabel, final Component aComponent) {
		super();

		label = new JLabel(aLabel);

		component = aComponent;

		fixComponentWidth = (0 != component.getWidth());

		add(label);
		add(component);

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
					component.setBounds(x, y, component.getWidth(), height - (MARGIN * 2));
				} else {
					component.setBounds(x, y, width - (x + MARGIN), height - (MARGIN * 2));
				}
			}
		});
	}

	@Override
	public void setLabelWidth(final int aWidth) {
		labelWidth = aWidth;
	}

	@Override
	public int getPreferredWidth() {
		int width = 0;
		if (fixComponentWidth) {
			width = MARGIN + labelWidth + SPACE + component.getWidth() + MARGIN;
		}
		return width;
	}

	@Override
	public int getPreferredHeight() {
		return 32;
	}

	@Override
	public int getPreferredLabelWidth() {
		Dimension dm = label.getPreferredSize();
		return dm.width;
	}

	@Override
	public boolean isValidate() {
		if (null != component) {
			if (component instanceof ValidationSupport) {
				ValidationSupport support = (ValidationSupport) component;
				if (!support.isValidate()) {
					return false;
				}
			}
		}
		return true;
	}
}
