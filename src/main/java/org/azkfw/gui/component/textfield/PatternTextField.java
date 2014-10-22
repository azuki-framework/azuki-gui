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

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * このクラスは、パターン型のテキストフィールドを実装したクラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/22
 * @author kawakicchi
 */
public class PatternTextField extends ValidationTextField {

	/** serialVersionUID */
	private static final long serialVersionUID = -7190798779491694031L;

	/**
	 * コンストラクタ
	 */
	public PatternTextField() {
		this(null);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param value
	 */
	public PatternTextField(final Pattern value) {
		super();

		if (null != value) {
			setText(value.pattern());
		} else {
			setText("");
		}
	}

	/**
	 * 値を取得する。
	 * 
	 * @return 値
	 */
	public Pattern getValue() {
		Pattern value = null;
		try {
			if (0 < getText().length()) {
				value = Pattern.compile(getText());
			}
		} catch (PatternSyntaxException ex) {
		}
		return value;
	}

	@Override
	public boolean isValidate() {
		boolean validate = false;
		try {
			if (0 < getText().length()) {
				Pattern.compile(getText());
			}
			validate = true;
		} catch (PatternSyntaxException ex) {
		}
		return validate;
	}
}
