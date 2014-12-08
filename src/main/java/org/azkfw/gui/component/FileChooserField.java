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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.azkfw.gui.event.FileChooserEvent;
import org.azkfw.gui.event.FileChooserListener;
import org.azkfw.gui.validate.ValidationSupport;
import org.azkfw.util.StringUtility;

/**
 * このクラスは、ファイル選択用の基底フィールドクラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/22
 * @author kawakicchi
 */
public class FileChooserField extends JPanel implements ValidationSupport {

	/** serialVersionUID */
	private static final long serialVersionUID = 8053290468852958097L;

	private JFileChooser fileChooser;
	private String approveButtonText;

	private boolean enableValidate;

	private JTextField text;
	private JButton button;

	private FileChooserEvent event;
	private List<FileChooserListener> listeners;

	private boolean required;

	/**
	 * コンストラクタ
	 */
	public FileChooserField(final String aApproveButtonText) {
		this(aApproveButtonText, null);
	}

	/**
	 * コンストラクタ
	 */
	public FileChooserField(final String aApproveButtonText, final File aFile) {
		event = new FileChooserEvent(this);
		listeners = new ArrayList<FileChooserListener>();

		required = true;

		approveButtonText = aApproveButtonText;
		enableValidate = true;

		text = new JTextField();
		button = new JButton(approveButtonText);

		setLayout(null);
		add(text);
		add(button);

		fileChooser = new JFileChooser();

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {
				doFileOpenDialog();
			}
		});
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent event) {
				int width = getWidth();
				int height = getHeight();
				Dimension dm = button.getPreferredSize();

				text.setBounds(0, 0, width - dm.width, height);
				button.setBounds(width - dm.width, 0, dm.width, height);
			}
		});
		text.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				doFileChooserChanged();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				doFileChooserChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				doFileChooserChanged();
			}
		});

		setFile(aFile);
	}

	public void addFileChooserListener(final FileChooserListener aListener) {
		synchronized (listeners) {
			listeners.add(aListener);
		}
	}

	public void removeFileChooserListener(final FileChooserListener aListener) {
		synchronized (listeners) {
			listeners.remove(aListener);
		}
	}

	private void doFileChooserChanged() {
		synchronized (listeners) {
			for (FileChooserListener listener : listeners) {
				listener.fileChooserChanged(event);
			}
		}
	}

	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		text.setEditable(enabled);
		button.setEnabled(enabled);
	}

	public void setEnableValidate(final boolean enable) {
		enableValidate = enable;
	}

	@Override
	public boolean isEnableValidate() {
		return enableValidate;
	}

	@Override
	public boolean isValidate() {
		if (required) {
			return (0 < text.getText().length());
		} else {
			return true;
		}
	}

	public void setText(final String aString) {
		text.setText(aString);
	}

	public String getText() {
		return text.getText();
	}

	public void setFile(final File aFile) {
		text.setText((null == aFile) ? "" : aFile.getAbsolutePath());
	}

	public File getFile() {
		if (StringUtility.isNotEmpty(text.getText())) {
			return new File(text.getText());
		} else {
			return null;
		}
	}

	public void setRequired(final boolean aRequired) {
		required = aRequired;
	}

	public boolean isRequired() {
		return required;
	}

	public void setDialogTitle(final String aTitle) {
		fileChooser.setDialogTitle(aTitle);
	}

	public void setApproveButtonText(final String aText) {
		approveButtonText = aText;
	}

	public void setFileSelectionMode(final int aMode) {
		fileChooser.setFileSelectionMode(aMode);
	}

	public void setCurrentDirectory(final File aFile) {
		fileChooser.setCurrentDirectory(aFile);
	}

	private void doFileOpenDialog() {
		fileChooser.setSelectedFile(new File(text.getText()));
		int selected = fileChooser.showDialog(this, approveButtonText);
		if (selected == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			text.setText(file.getAbsolutePath());
		} else if (selected == JFileChooser.CANCEL_OPTION) {
		} else if (selected == JFileChooser.ERROR_OPTION) {
		}
	}
}
