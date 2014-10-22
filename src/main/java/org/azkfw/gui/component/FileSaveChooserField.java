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

import java.io.File;

import javax.swing.JFileChooser;

/**
 * このクラスは、ファイル保存用のフィールドクラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/22
 * @author kawakicchi
 */
public class FileSaveChooserField extends FileChooserField {

	/** serialVersionUID */
	private static final long serialVersionUID = -7142336266736643836L;

	public FileSaveChooserField() {
		this(null);
	}

	public FileSaveChooserField(final File aFile) {
		super("保存", aFile);
		setFileSelectionMode(JFileChooser.FILES_ONLY);
	}
}
