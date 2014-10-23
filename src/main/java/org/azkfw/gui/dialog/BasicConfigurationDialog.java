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

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import org.azkfw.gui.validate.ValidationSupport;

/**
 * @since 1.0.0
 * @version 1.0.0 2014/10/17
 * @author kawakita
 */
public abstract class BasicConfigurationDialog<DATA> extends ConfigurationDialog<DATA> {

	/** serialVersionUID */
	private static final long serialVersionUID = -4740454032721608488L;

	private int margin = 12;

	/**
	 * コンストラクタ
	 * 
	 * @param aFrame 親フレーム
	 * @param aData データ
	 */
	public BasicConfigurationDialog(final Frame aFrame, final DATA aData) {
		super(aFrame, aData);
		init();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param aDialog 親ダイアログ
	 * @param aData データ
	 */
	public BasicConfigurationDialog(final Dialog aDialog, final DATA aData) {
		super(aDialog, aData);
		init();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param aFrame 親フレーム
	 * @param aModal モーダル
	 * @param aData データ
	 */
	public BasicConfigurationDialog(final Frame aFrame, final boolean aModal, final DATA aData) {
		super(aFrame, aData);
		init();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param aDialog 親ダイアログ
	 * @param aModal モーダル
	 * @param aData データ
	 */
	public BasicConfigurationDialog(final Dialog aDialog, final boolean aModal, final DATA aData) {
		super(aDialog, aData);
		init();
	}

	private void init() {
		getClientPanel().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent event) {
				relayout();
			}
		});
	}

	protected void addConfigurationField(final ConfigurationField aField) {
		add(aField);
	}

	/**
	 * バリデーション処理。
	 * 
	 * @return <code>true</code>で成功
	 */
	protected boolean isValidate() {
		for (int i = 0; i < getComponentCount(); i++) {
			ConfigurationField field = (ConfigurationField) getComponent(i);
			if (field instanceof ValidationSupport) {
				ValidationSupport support = (ValidationSupport) field;
				if (!support.isValidate()) {
					return false;
				}
			}
		}
		return true;
	}

	private void relayout() {
		int maxLabelWidth = 80;
		for (int i = 0; i < getComponentCount(); i++) {
			ConfigurationField field = (ConfigurationField) getComponent(i);
			maxLabelWidth = Math.max(field.getPreferredLabelWidth(), maxLabelWidth);
		}

		int x = margin;
		int y = margin;
		for (int i = 0; i < getComponentCount(); i++) {
			ConfigurationField field = (ConfigurationField) getComponent(i);
			field.setLabelWidth(maxLabelWidth);

			int height = field.getPreferredHeight();
			int width = getClientPanel().getWidth() - (margin * 2);

			field.setBounds(x, y, width, height);

			y += height;
		}
	}

	public Dimension getPreferredSize() {
		relayout();

		int clientWidth = 0;
		int clientHeight = 0;
		for (int i = 0; i < getComponentCount(); i++) {
			ConfigurationField field = (ConfigurationField) getComponent(i);
			clientWidth = Math.max(field.getPreferredWidth(), clientWidth);
			clientHeight += field.getPreferredHeight();
		}

		Insets insets = getClientInsets();
		clientWidth += insets.left + insets.right + (margin * 2);
		clientHeight += insets.top + insets.bottom + (margin * 2);
		return new Dimension(clientWidth, clientHeight);
	}

}
