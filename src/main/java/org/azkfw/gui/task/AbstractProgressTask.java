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
package org.azkfw.gui.task;

import java.util.ArrayList;
import java.util.List;

/**
 * このクラスは、プログレス機能を実装したタスク機能を定義する為の基底クラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/07
 * @author Kawakicchi
 */
public abstract class AbstractProgressTask extends AbstractTask implements TaskProgressSupport {

	private List<TaskProgressListener> listeners;
	
	private TaskProgressEvent event;

	public AbstractProgressTask() {
		super();
		event = new TaskProgressEvent(this);
		listeners = new ArrayList<TaskProgressListener>();
	}

	@Override
	public void addTaskProgressListener(final TaskProgressListener listener) {
		listeners.add(listener);
	}

	/**
	 * プログレスリスナーを呼び出す。
	 * @param percent 進捗率
	 * @param message メッセージ
	 * @return <code>false</code>の場合キャンセル
	 */
	protected final boolean callProgressListener(final double percent, final String message) {
		event.setPercent(percent);
		event.setMessage(message);
		for (TaskProgressListener listener : listeners) {
			listener.progress(event);
			if (event.isCancel()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * プログレスリスナーを呼び出す。
	 * @param percent 進捗率
	 * @return <code>false</code>の場合キャンセル
	 */
	protected final boolean callProgressListener(final double percent) {
		event.setPercent(percent);
		for (TaskProgressListener listener : listeners) {
			listener.progress(event);
			if (event.isCancel()) {
				return false;
			}
		}
		return true;
	}
}
