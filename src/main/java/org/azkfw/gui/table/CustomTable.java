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
package org.azkfw.gui.table;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * @since 1.0.0
 * @version 1.0.0 2014/10/07
 * @author Kawakicchi
 */
public class CustomTable extends JTable {

	/** serialVersionUID */
	private static final long serialVersionUID = 4384722077928655434L;

	public CustomTable(final DefaultTableModel model) {
		super(model);
	}

	protected TableCellRenderer genericRenderer = new GenericCellRenderer();
	protected TableCellEditor genericEditor = new GenericCellEditor();

	protected class GenericCellRenderer implements TableCellRenderer {

		/**
		 * レンダラコンポーネント取得
		 * 
		 * @param JTable 対象テーブル
		 * @param Object 表示する値
		 * @param boolean 選択されているかどうか
		 * @param boolean フォーカスが当たっているかどうか
		 * @param int 対象行番号
		 * @param int 対象カラム番号
		 * @return Component レンダラコンポーネント
		 */

		// getTableCellRendererComponent()に渡されてきたコンポーネントを、
		// このメソッドの戻り値として返す。
		// セルに置けれているSwingコンポーネントを、
		// そのままセルレンダラーのコンポーネントとして使うことになる。
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JComponent c = (JComponent) value;
			// テーブルの各行の高さを調整する。
			Dimension d = c.getPreferredSize();
			if (table.getRowHeight(row) < d.height) {

				table.setRowHeight(row, d.height);
			}
			return c;
		}
	}

	protected class GenericCellEditor extends AbstractCellEditor implements TableCellEditor {

		/** serialVersionUID */
		private static final long serialVersionUID = -4569629400750347234L;

		protected JComponent c = null;

		// このコンポーネントをインスタンス変数に保管しておき、
		// getCellEditorValue()が呼ばれるとき(セルの編集終了時)に
		// このコンポーネントを返す。
		// これにより、編集終了時に、コンポーネントが消失してしまうことを防ぐ。
		public Object getCellEditorValue() {
			return c;
		}

		/**
		 * レンダラコンポーネント取得
		 * 
		 * @param JTable 対象テーブル
		 * @param Object 表示する値
		 * @param boolean 選択されているかどうか
		 * @param boolean フォーカスが当たっているかどうか
		 * @param int 対象行番号
		 * @param int 対象カラム番号
		 * @return Component レンダラコンポーネント
		 */
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			c = (JComponent) value;
			return c;
		}
	}

	public TableCellRenderer getCellRenderer(int row, int col) {
		TableCellRenderer renderer;
		Object o = getValueAt(row, col);
		// 指定されたセルにSwingコンポーネントが置かれている場合には、
		// 汎用セルレンダラー[GenericCellRenderer]のオブジェクトを返す。
		// それ以外の場合には、そのセルに対応しているデフォルトのセルレンダラーを返す
		if (o instanceof JComponent) {
			renderer = genericRenderer;
		} else {
			renderer = super.getCellRenderer(row, col);
		}
		return renderer;
	}

	public TableCellEditor getCellEditor(int row, int col) {
		TableCellEditor editor;
		Object o = getValueAt(row, col);
		if (o instanceof JComponent) {
			editor = genericEditor;
		} else {
			editor = super.getCellEditor(row, col);
		}
		return editor;
	}

}
