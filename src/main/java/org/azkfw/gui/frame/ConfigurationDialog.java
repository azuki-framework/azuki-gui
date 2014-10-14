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
package org.azkfw.gui.frame;

import java.awt.Component;
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
import javax.swing.JFrame;
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
	 * @param frame Frame
	 */
	public ConfigurationDialog(final JFrame frame) {
		super(frame);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLayout(null);
		setSize(400, 300);

		pnlClient = new JPanel();
		pnlClient.setLayout(null);
		getContentPane().add(pnlClient);
		pnlBorder = new JPanel();
		pnlBorder.setBorder(new BevelBorder(BevelBorder.RAISED));
		getContentPane().add(pnlBorder);

		btnOk = new JButton("OK");
		getContentPane().add(btnOk);
		btnCancel = new JButton("キャンセル");
		getContentPane().add(btnCancel);

		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doOk();
			}
		});
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doCancel();
			}
		});
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent event) {
				doResize();
			}
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent event) {
				doCancel();
			}
		});

		listeners = new ArrayList<ConfigurationDialogListener>();
	}

	@Override
	public final Component add(Component comp) {
		return pnlClient.add(comp);
	}

	public void addConfigurationDialogListener(final ConfigurationDialogListener listener) {
		listeners.add(listener);
	}

	public final void setClientSize(final int aWidth, final int aHeight) {
		Insets insets = getInsets();
		setSize(aWidth - (insets.left + insets.left), aHeight - (insets.top + insets.bottom));
	}

	protected boolean onClickOK() {
		return true;
	}

	protected boolean onClickCancel() {
		return true;
	}

	private void doOk() {
		if (onClickOK()) {
			ConfigurationDialogEvent event = new ConfigurationDialogEvent(this);
			for (ConfigurationDialogListener listener : listeners) {
				listener.configurationDialogOk(event);
			}
			setVisible(false);
			dispose();
		}
	}

	private void doCancel() {
		if (onClickCancel()) {
			ConfigurationDialogEvent event = new ConfigurationDialogEvent(this);
			for (ConfigurationDialogListener listener : listeners) {
				listener.configurationDialogCancel(event);
			}
			setVisible(false);
			dispose();
		}
	}

	private void doResize() {
		Insets insets = getInsets();
		int width = getWidth() - (insets.left + insets.right);
		int height = getHeight() - (insets.top + insets.bottom);

		pnlClient.setBounds(0, 0, width, height - 50);
		pnlBorder.setBounds(-2, height - 50, width + 4, 3);
		btnOk.setBounds(width - (120 + 10) * 2, height - 40, 120, 32);
		btnCancel.setBounds(width - (120 + 10) * 1, height - 40, 120, 32);
	}
}
