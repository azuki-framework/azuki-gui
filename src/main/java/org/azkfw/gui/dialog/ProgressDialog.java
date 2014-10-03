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

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * このクラスは、プログレスダイアログクラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/03
 * @author kawakicchi
 */
public class ProgressDialog extends JDialog {

	public static interface ProgressDialogListener {

		public void progressDialogCanceled(final ProgressDialog aDialog);

	}

	public static class ProcessDialogAdapter implements ProgressDialogListener {
		@Override
		public void progressDialogCanceled(final ProgressDialog aDialog) {

		}
	}

	/** serialVersionUID */
	private static final long serialVersionUID = 6631089209342946069L;

	private JLabel lblMessage;
	private JProgressBar progressBar;
	private JButton btnCancel;

	/**
	 * コンストラクタ
	 */
	public ProgressDialog() {
		super();
		init();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param owner オーナー
	 */
	public ProgressDialog(final Dialog owner) {
		super(owner);
		init();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param owner オーナー
	 * @param title タイトル
	 */
	public ProgressDialog(final Dialog owner, final String title) {
		super(owner, title);
		init();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param owner オーナー
	 * @param modal モーダル
	 */
	public ProgressDialog(final Dialog owner, final boolean modal) {
		super(owner, modal);
		init();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param owner オーナー
	 * @param title タイトル
	 * @param modal モーダル
	 */
	public ProgressDialog(final Dialog owner, final String title, final boolean modal) {
		super(owner, title, modal);
		init();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param owner オーナー
	 */
	public ProgressDialog(final Frame owner) {
		super(owner);
		init();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param owner オーナー
	 * @param title タイトル
	 */
	public ProgressDialog(final Frame owner, final String title) {
		super(owner, title);
		init();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param owner オーナー
	 * @param modal モーダル
	 */
	public ProgressDialog(final Frame owner, final boolean modal) {
		super(owner, modal);
		init();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param owner オーナー
	 * @param title タイトル
	 * @param modal モーダル
	 */
	public ProgressDialog(final Frame owner, final String title, final boolean modal) {
		super(owner, title, modal);
		init();
	}

	public void progress(final double percent) {
		progress(percent, null);
	}

	public void progress(final String message) {
		progress(-1, null);
	}

	public void progress(final double percent, final String message) {
		if (-1 != percent) {
			progressBar.setValue((int) percent);
		}
		if (null != message) {
			lblMessage.setText(message);
		}
	}

	private void init() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLayout(null);
		setResizable(true);

		lblMessage = new JLabel("");
		lblMessage.setLocation(10, 10);
		add(lblMessage);

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		add(progressBar);

		btnCancel = new JButton("キャンセル");
		btnCancel.setSize(100, 32);
		add(btnCancel);

		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				onClickCancalButton();
			}
		});

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent event) {
				doComponentResized();
			}
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(final WindowEvent event) {
				doWindowOpened();
			}
		});

	}

	private void onClickCancalButton() {
		btnCancel.setEnabled(false);
	}

	private void doComponentResized() {
		Insets insets = getInsets();

		int width = getWidth() - (insets.left + insets.right);
		int height = getHeight() - (insets.top + insets.bottom);

		lblMessage.setSize(width - 20, height - (10 + 4 + 24 + 10 + 32 + 10));
		progressBar.setLocation(10, height - (24 + 10 + btnCancel.getHeight() + 10));
		progressBar.setSize(width - 20, 24);
		btnCancel.setLocation(width - (btnCancel.getWidth() + 10), height - (btnCancel.getHeight() + 10));
	}
	
	private void doWindowOpened() {
		Insets insets = getInsets();

		int width = 400;
		int height = insets.top + 10 + 32 + 4 + 24 + 10 + 32 + 10 + insets.bottom;
		setSize(width, height);

		Container parent = getParent();
		if (null != parent) {
			int x = parent.getX() + (parent.getWidth() - width) / 2;
			int y = parent.getY() + (parent.getHeight() - height) / 2;
			setLocation(x, y);
		}
	}
}
