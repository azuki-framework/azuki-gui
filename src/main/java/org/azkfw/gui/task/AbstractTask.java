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
 * このクラスは、タスク機能を実装する為の基底クラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/07
 * @author Kawakicchi
 */
public abstract class AbstractTask implements Task {

	private List<TaskListener> listeners;
	private TaskEvent event;

	public AbstractTask() {
		event = new TaskEvent();
		listeners = new ArrayList<TaskListener>();
	}

	@Override
	public final void initialize() {
		doInitialize();
	}
	
	public String toString(){
		return getName();
	}

	@Override
	public final TaskResult execute() {
		TaskResult result = null;
		try {
			for (TaskListener listener : listeners) {
				listener.taskStarted(event);
			}
			result = doExecute();

			if (result.isResult()) {
				if (result.isCancel()) {
					for (TaskListener listener : listeners) {
						listener.taskCanceled(event);
					}				
				} else {
					for (TaskListener listener : listeners) {
						listener.taskSuccessed(event);
					}									
				}
			} else {
				for (TaskListener listener : listeners) {
					listener.taskFailed(event);
				}				
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			for (TaskListener listener : listeners) {
				listener.taskFailed(event);
			}						
		} finally {
			for (TaskListener listener : listeners) {
				listener.taskCompleted(event);
			}			
		}
		return result;
	}

	@Override
	public final void destory() {
		doDestory();
	}

	@Override
	public final void addTaskListener(final TaskListener listener) {
		listeners.add(listener);
	}

	protected abstract void doInitialize();

	protected abstract TaskResult doExecute();

	protected abstract void doDestory();
}
