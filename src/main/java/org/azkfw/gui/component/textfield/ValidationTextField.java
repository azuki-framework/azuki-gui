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
package org.azkfw.gui.component.textfield;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

import org.azkfw.gui.validate.ValidationSupport;

/**
 * このクラスは、バリデーション機能を定義する為のテキストフィールドクラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/21
 * @author kawakicchi
 */
public abstract class ValidationTextField extends JTextField implements ValidationSupport {

	/** serialVersionUID */
	private static final long serialVersionUID = -5301369499632815079L;

	// requisite

	private boolean enableValidate;

	/**
	 * コンストラクタ
	 */
	public ValidationTextField() {
		enableValidate = true;
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent event) {
				if (isEnableValidate()) {
					doValidate(isValidate());
				}
			}
		});
	}

	@Override
	public void setText(final String text) {
		super.setText(text);
		if (isEnableValidate()) {
			doValidate(isValidate());
		}
	}

	/**
	 * バリデーション結果を受けての処理
	 * 
	 * @param validate バリデーション結果
	 */
	protected void doValidate(final boolean validate) {
		if (validate) {
			setBackground(Color.white);
		} else {
			setBackground(new Color(255, 220, 255));
		}
	}

	@Override
	public boolean isEnableValidate() {
		return enableValidate;
	}

	public void setEnableValidate(final boolean enable) {
		enableValidate = enable;
	}
}
