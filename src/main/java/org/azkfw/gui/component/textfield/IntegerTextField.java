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

/**
 * このクラスは、Integer型のテキストフィールドを実装したクラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/21
 * @author kawakicchi
 */
public class IntegerTextField extends NumberTextField {

	/** serialVersionUID */
	private static final long serialVersionUID = -5487423297209440991L;

	private static final Pattern PATTERN = Pattern.compile("^(|-{0,1}[0-9]+)$");

	/**
	 * コンストラクタ
	 */
	public IntegerTextField() {
		this(null);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param value 値
	 */
	public IntegerTextField(final Integer value) {
		super();
		if (null != value) {
			setText(Integer.toString(value));
		} else {
			setText("");
		}
	}

	/**
	 * 値を取得する。
	 * 
	 * @return 値
	 */
	public Integer getValue() {
		Integer value = null;
		try {
			value = Integer.parseInt(getText());
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
		return value;
	}

	@Override
	public boolean isValidate() {
		return PATTERN.matcher(getText()).matches();
	}
}
