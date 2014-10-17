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
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JLabel;

/**
 * @since 1.0.0
 * @version 1.0.0 2014/10/17
 * @author kawakita
 */
public abstract class BasicConfigurationDialog extends ConfigurationDialog {

	/** serialVersionUID */
	private static final long serialVersionUID = -4740454032721608488L;

	private int marge = 10;
	private int space = 6;
	private int labelWidth = 160;
	private int labelHeight = 24;

	/**
	 * コンストラクタ
	 * 
	 * @param aFrame 親フレーム
	 * @param aData データ
	 */
	public BasicConfigurationDialog(final Frame aFrame, final Object aData) {
		super(aFrame, aData);

		getClientPanel().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent event) {
				relayout();
			}
		});
	}

	protected void addConfig(final String aLabel, final Component aComponent) {
		JLabel label = new JLabel(aLabel);
		label.setSize(labelWidth, labelHeight);

		add(label);
		add(aComponent);
	}

	private void relayout() {
		int x = marge;
		int y = marge;

		int compoenntWidth = getClientPanel().getWidth() - (marge + labelWidth + marge);

		for (int i = 0; i < getComponentCount(); i += 2) {
			JLabel label = (JLabel) getComponent(i);
			Component component = getComponent(i + 1);

			int height = (0 != component.getHeight()) ? component.getHeight() : labelHeight;
			int width = (0 != component.getWidth()) ? component.getWidth() : compoenntWidth;

			label.setLocation(x, y);
			component.setBounds(x + labelWidth, y, width, height);

			y += space + Math.max(label.getHeight(), component.getHeight());
		}
	}
}
