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
 * このクラスは、タスク管理を行なうマネージャークラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/07
 * @author Kawakicchi
 */
public class TaskManager implements Runnable {

	/** Instance */
	private static final TaskManager INSTANCE = new TaskManager();

	/** TaskManager listener */
	private List<TaskManagerListener> listeners;

	private Thread thread;
	private List<TaskThread> tasks;

	private int runningCount;
	private int maxRunning;
	private boolean cancel;

	private TaskManager() {
		listeners = new ArrayList<TaskManagerListener>();
		tasks = new ArrayList<TaskThread>();
		runningCount = 0;
		maxRunning = 2;
	}

	public static TaskManager getInstance() {
		return INSTANCE;
	}

	public void addTaskManagerListener(final TaskManagerListener listener) {
		listeners.add(listener);
	}

	public void start() {
		thread = new Thread(this);
		thread.start();
	}

	public void queue(final Task task) {
		synchronized (tasks) {
			TaskThread tt = new TaskThread(task);
			tt.setListener(new TaskThreadListener() {
				@Override
				public void taskThreadStoped(final TaskThread taskThread) {
					runningCount--;
				}
				@Override
				public void taskThreadStarted(final TaskThread taskThread) {
				}
			});
			tasks.add(tt);
		}
	}

	public void stop() {
		synchronized (tasks) {

			cancel = true;
		}
	}

	@Override
	public void run() {
		for (TaskManagerListener listener : listeners) {
			listener.taskManagerStarted();
		}

		while (!cancel) {
			if (runningCount < maxRunning) {
				for (TaskThread task : tasks) {
					if (TaskStatus.Wait == task.getStatus()) {
						runningCount++;
						task.start();
						break;
					}
				}
			}

			try {
				Thread.sleep(1000);
			} catch (Exception ex) {
				break;
			}
		}

		for (TaskManagerListener listener : listeners) {
			listener.taskManagerStopping();
		}

		while (isRunningTask()) {
			try {
				Thread.sleep(500);
			} catch (Exception ex) {
				break;
			}
		}

		for (TaskManagerListener listener : listeners) {
			listener.taskManagerStoped();
		}
	}

	public boolean isRunningTask() {
		return (0 < runningCount);
	}

	private enum TaskStatus {
		Wait(), Starting(), Started(), Canceled(), Stoped();

		/**
		 * 
		 */
		private TaskStatus() {
		};
	}

	private interface TaskThreadListener {
		public void taskThreadStarted(final TaskThread taskThread);

		public void taskThreadStoped(final TaskThread taskThread);
	}

	/**
	 * @since 1.0.0
	 * @version 1.0.0 2014/10/07
	 * @author Kawakicchi
	 */
	private class TaskThread implements Runnable {

		private Thread thread;
		private Task task;
		private TaskThreadListener listener;
		private TaskStatus status;

		public TaskThread(final Task aTask) {
			task = aTask;
			status = TaskStatus.Wait;
		}

		public void setListener(final TaskThreadListener aListener) {
			listener = aListener;
		}

		public TaskStatus getStatus() {
			return status;
		}

		public void start() {
			status = TaskStatus.Starting;
			thread = new Thread(this);
			thread.start();
		}

		@Override
		public void run() {
			status = TaskStatus.Started;
			listener.taskThreadStarted(this);

			task.execute();

			status = TaskStatus.Stoped;
			listener.taskThreadStoped(this);
		}
	}
}
