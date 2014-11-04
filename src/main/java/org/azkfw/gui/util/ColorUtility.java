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
package org.azkfw.gui.util;

import java.awt.Color;

import org.azkfw.util.StringUtility;

/**
 * このクラスは、カラーの機能を集めたユーティリティクラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/11/04
 * @author kawakicchi
 */
public class ColorUtility {

	/**
	 * コンストラクタ
	 * <p>
	 * インスタンス生成を禁止
	 * </p>
	 */
	private ColorUtility() {

	}

	/**
	 * RGBカラーを文字列(形式："red, green, blue")へ変換する。
	 * 
	 * @param aColor カラー
	 * @return 文字列
	 */
	public static String rgbToString(final Color aColor) {
		String string = "";
		if (null != aColor) {
			string = String.format("%d, %d, %d", aColor.getRed(), aColor.getGreen(), aColor.getBlue());
		}
		return string;
	}

	/**
	 * 文字列（形式："red, green, blue"）をRGBカラーへ変換する。
	 * 
	 * @param aString 文字列
	 * @return カラー
	 */
	public static Color stringToRgb(final String aString) {
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
