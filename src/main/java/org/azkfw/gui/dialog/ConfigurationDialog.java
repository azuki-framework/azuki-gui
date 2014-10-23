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
import java.awt.Dialog;
import java.awt.Dimension;
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
import javax.swing.border.SoftBevelBorder;

/**
 * このクラスは、設定ダイアログ画面を実装する為の基底クラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/06
 * @author Kawakicchi
 */
public abstract class ConfigurationDialog<DATA> extends JDialog {

	/** serialVersionUID */
	private static final long serialVersionUID = 4046832570158320828L;

	private static final int DEFAULT_BUTTON_MARGIN = 6;
	private static final int DEFAULT_BORDER_SIZE = 3;
	private static final int DEFAULT_BUTTON_WIDTH = 120;
	private static final int DEFAULT_BUTTON_HEIGHT = 24;

	/** data object */
	private DATA data;

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
	public ConfigurationDialog(final Frame aFrame, final DATA aData) {
		super(aFrame);
		init(aData);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param aDialog Dialog
	 * @param aData Data
	 */
	public ConfigurationDialog(final Dialog aDialog, final DATA aData) {
		super(aDialog);
		init(aData);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param aFrame Frame
	 * @param aModal Modal
	 * @param aData Data
	 */
	public ConfigurationDialog(final Frame aFrame, final boolean aModal, final DATA aData) {
		super(aFrame, aModal);
		init(aData);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param aDialog Dialog
	 * @param aModal Modal
	 * @param aData Data
	 */
	public ConfigurationDialog(final Dialog aDialog, final boolean aModal, final DATA aData) {
		super(aDialog, aModal);
		init(aData);
	}

	private void init(final DATA aData) {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLayout(null);
		setMinimumSize(new Dimension(480, 120));

		data = aData;
		listeners = new ArrayList<ConfigurationDialogListener>();

		Container container = getContentPane();

		pnlClient = new JPanel();
		pnlClient.setLayout(null);
		container.add(pnlClient);
		pnlBorder = new JPanel();
		pnlBorder.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
		container.add(pnlBorder);

		btnOk = new JButton("OK");
		btnOk.setSize(DEFAULT_BUTTON_WIDTH, DEFAULT_BUTTON_HEIGHT);
		container.add(btnOk);
		btnCancel = new JButton("キャンセル");
		btnCancel.setSize(DEFAULT_BUTTON_WIDTH, DEFAULT_BUTTON_HEIGHT);
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

			@Override
			public void windowOpened(final WindowEvent event) {
				setSize(getPreferredSize());
			}
		});
	}

	public Dimension getPreferredSize() {
		return new Dimension(getWidth(), getHeight());
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
	 * コンテナの中心に移動する。
	 * 
	 * @param aParent 親コンテナ
	 */
	public final void setLocationMiddle(final Container aParent) {
		int x = (aParent.getWidth() - getWidth()) / 2;
		int y = (aParent.getHeight() - getHeight()) / 2;
		setLocation(aParent.getX() + x, aParent.getY() + y);
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
		Insets insets = getClientInsets();
		setSize(aWidth + (insets.left + insets.left), aHeight + (insets.top + insets.bottom));
	}

	/**
	 * データを取得する。
	 * 
	 * @return データ
	 */
	public final DATA getData() {
		return data;
	}

	/**
	 * バリデーション処理。
	 * 
	 * @return <code>true</code>で成功
	 */
	protected abstract boolean isValidate();

	/**
	 * OKボタンが押下された時の処理。
	 * 
	 * @return <code>false</code>で処理を中断
	 */
	protected abstract boolean doClickOK(final DATA aData);

	/**
	 * Cancelボタンが押下された時の処理。
	 * 
	 * @return <code>false</code>で処理を中断
	 */
	protected abstract boolean doClickCancel();

	private void doClickButtonOk() {
		if (isValidate()) {
			if (doClickOK(data)) {
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

	public Insets getClientInsets() {
		Insets insets = super.getInsets();
		return new Insets(insets.top, insets.left, insets.bottom
				+ (DEFAULT_BUTTON_MARGIN + DEFAULT_BUTTON_HEIGHT + DEFAULT_BUTTON_MARGIN + DEFAULT_BORDER_SIZE), insets.right);
	}

	private void doResizeDialog() {
		Insets insets = getInsets();
		int width = getWidth() - (insets.left + insets.right);
		int height = getHeight() - (insets.top + insets.bottom);

		pnlClient.setBounds(0, 0, width, height - (DEFAULT_BUTTON_MARGIN + DEFAULT_BUTTON_HEIGHT + DEFAULT_BUTTON_MARGIN + DEFAULT_BORDER_SIZE));

		pnlBorder.setBounds(-2, height - (DEFAULT_BUTTON_MARGIN + DEFAULT_BUTTON_HEIGHT + DEFAULT_BUTTON_MARGIN + DEFAULT_BORDER_SIZE), width + 4,
				DEFAULT_BORDER_SIZE);

		btnOk.setLocation(width - (DEFAULT_BUTTON_WIDTH + DEFAULT_BUTTON_MARGIN) * 2, height - (DEFAULT_BUTTON_HEIGHT + DEFAULT_BUTTON_MARGIN));
		btnCancel.setLocation(width - (DEFAULT_BUTTON_WIDTH + DEFAULT_BUTTON_MARGIN) * 1, height - (DEFAULT_BUTTON_HEIGHT + DEFAULT_BUTTON_MARGIN));
	}
}
