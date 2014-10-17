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
import java.awt.Container;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * このクラスは、設定ダイアログ画面を実装する為の基底クラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/06
 * @author Kawakicchi
 */
public abstract class ConfigurationDialog extends JDialog {

	/** serialVersionUID */
	private static final long serialVersionUID = 4046832570158320828L;

	/** data object */
	private Object data;

	/** listener */
	private List<ConfigurationDialogListener> listeners;

	// Component
	/** client panel */
	private JPanel pnlClient;
	/** border */
	private JPanel pnlBorder;
	/** OK button */
	private JButton btnOk;
	/** Cancel button */
	private JButton btnCancel;

	/**
	 * コンストラクタ
	 * 
	 * @param aFrame Frame
	 * @param aData Data
	 */
	public ConfigurationDialog(final Frame aFrame, final Object aData) {
		super(aFrame);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLayout(null);
		setSize(400, 300);

		data = aData;
		listeners = new ArrayList<ConfigurationDialogListener>();

		Container container = getContentPane();

		pnlClient = new JPanel();
		pnlClient.setLayout(null);
		container.add(pnlClient);
		pnlBorder = new JPanel();
		pnlBorder.setBorder(new BevelBorder(BevelBorder.RAISED));
		container.add(pnlBorder);

		btnOk = new JButton("OK");
		container.add(btnOk);
		btnCancel = new JButton("キャンセル");
		container.add(btnCancel);

		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doClickButtonOk();
			}
		});
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doClickButtonCancel();
			}
		});
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent event) {
				doResizeDialog();
			}
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent event) {
				doClickButtonCancel();
			}
		});
	}

	@Override
	public final Component add(final Component comp) {
		return pnlClient.add(comp);
	}

	@Override
	public final int getComponentCount() {
		return pnlClient.getComponentCount();
	}

	@Override
	public final Component getComponent(final int index) {
		return pnlClient.getComponent(index);
	}

	protected final JPanel getClientPanel() {
		return pnlClient;
	}

	/**
	 * 設定ダイアログへリスナーを登録する。
	 * 
	 * @param listener リスナー
	 */
	public final void addConfigurationDialogListener(final ConfigurationDialogListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	/**
	 * 設定ダイアログからリスナーを削除する。
	 * 
	 * @param listener
	 */
	public final void removeConfigurationDialogListener(final ConfigurationDialogListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	/**
	 * クライアントサイズを設定する。
	 * <p>
	 * クライアントサイズを元にダイアログサイズを設定する。
	 * </p>
	 * 
	 * @param aWidth 横幅
	 * @param aHeight　縦幅
	 */
	public final void setClientPanelSize(final int aWidth, final int aHeight) {
		Insets insets = getInsets();
		setSize(aWidth - (insets.left + insets.left), aHeight - (insets.top + insets.bottom));
	}

	/**
	 * データを取得する。
	 * 
	 * @return データ
	 */
	protected final Object getData() {
		return data;
	}

	/**
	 * OKボタンが押下された時の処理。
	 * 
	 * @return <code>false</code>で処理を中断
	 */
	protected abstract boolean doClickOK();

	/**
	 * Cancelボタンが押下された時の処理。
	 * 
	 * @return <code>false</code>で処理を中断
	 */
	protected abstract boolean doClickCancel();

	private void doClickButtonOk() {
		if (doClickOK()) {
			ConfigurationDialogEvent event = new ConfigurationDialogEvent(this);
			synchronized (listeners) {
				for (ConfigurationDialogListener listener : listeners) {
					try {
						listener.configurationDialogOk(event, data);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
			setVisible(false);
			dispose();
		}
	}

	private void doClickButtonCancel() {
		if (doClickCancel()) {
			ConfigurationDialogEvent event = new ConfigurationDialogEvent(this);
			synchronized (listeners) {
				for (ConfigurationDialogListener listener : listeners) {
					try {
						listener.configurationDialogCancel(event, data);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
			setVisible(false);
			dispose();
		}
	}

	private void doResizeDialog() {
		Insets insets = getInsets();
		int width = getWidth() - (insets.left + insets.right);
		int height = getHeight() - (insets.top + insets.bottom);

		pnlClient.setBounds(0, 0, width, height - 50);
		pnlBorder.setBounds(-2, height - 50, width + 4, 3);
		btnOk.setBounds(width - (120 + 10) * 2, height - 40, 120, 32);
		btnCancel.setBounds(width - (120 + 10) * 1, height - 40, 120, 32);
	}
}
