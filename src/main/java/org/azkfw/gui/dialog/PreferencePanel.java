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

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * このクラスは、環境設定画面のクライアントパネルを実装する為の基底クラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/21
 * @author kawakicchi
 */
public abstract class PreferencePanel extends JPanel {

	/** serialVersionUID */
	private static final long serialVersionUID = -3152534264542817918L;

	/** Preference dialog */
	private PreferenceDialog dialog;

	private PreferencePanelEvent event;
	private List<PreferencePanelListener> listeners;

	/**
	 * コンストラクタ
	 */
	public PreferencePanel() {
		event = new PreferencePanelEvent(this);
		listeners = new ArrayList<PreferencePanelListener>();
	}

	public void addPreferencePanelListener(final PreferencePanelListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	public void removePreferencePanelListener(final PreferencePanelListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	final void setPreferenceDialog(final PreferenceDialog aDialog) {
		dialog = aDialog;
	}

	protected final PreferenceDialog getPreferenceDialog() {
		return dialog;
	}

	void load() {
		synchronized (listeners) {
			for (PreferencePanelListener listener : listeners) {
				try {
					listener.preferencePanelLoading(event);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		doLoad();
	}

	boolean isValidate() {
		return doValidate();
	}

	void store() {
		doStore();

		synchronized (listeners) {
			for (PreferencePanelListener listener : listeners) {
				try {
					listener.preferencePanelStored(event);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	protected abstract void doLoad();

	protected abstract boolean doValidate();

	protected abstract void doStore();
}
