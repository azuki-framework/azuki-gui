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
package org.azkfw.gui.tree;

import java.io.File;
import java.util.List;

import javax.swing.JMenuItem;

/**
 * このインターフェースは、ファイルエクスプローラツリー用のリスナー機能を定義したインターフェースです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/14
 * @author kawakicchi
 */
public interface FileExplorerTreeListener {

	/**
	 * ファイル・ディレクトでダブルクリック・Enterキー押下時に呼び出される。
	 * 
	 * @param event イベント
	 * @param aFile ファイル
	 */
	public void fileExplorerTreeClickedFile(final FileExplorerTreeEvent event, final File aFile);

	/**
	 * ファイル・ディレクトリで右クリックした場合のコンテキストメニュー
	 * 
	 * @param event イベント
	 * @param aFile ファイル
	 * @return <code>null</code>の場合、コンテキストメニューにメニューは追加されない。
	 */
	public List<JMenuItem> fileExplorerTreeMenuFile(final FileExplorerTreeEvent event, final File aFile);

	/**
	 * ツリーの展開時にファイルごとに呼び出される。
	 * 
	 * @param event イベント
	 * @param aFile　ファイル
	 * @return <code>false</code>を指定した場合、ノードに追加されない。
	 */
	public boolean fileExplorerTreeAppendingFile(final FileExplorerTreeEvent event, final File aFile);

	public void fileExplorerTreeAppendedFile(final FileExplorerTreeEvent event, final File aFile);

}
