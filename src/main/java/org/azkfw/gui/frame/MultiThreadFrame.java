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

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.azkfw.gui.table.TaskTable;
import org.azkfw.gui.table.TaskTableModel;
import org.azkfw.gui.task.Task;
import org.azkfw.gui.task.TaskManager;
import org.azkfw.gui.task.TaskManagerAdapter;
import org.azkfw.gui.task.TaskProgressEvent;
import org.azkfw.gui.task.TaskProgressListener;
import org.azkfw.gui.task.TaskProgressSupport;
import org.azkfw.gui.task.TimerTask;
import org.azkfw.util.UUIDUtility;

/**
 * @since 1.0.0
 * @version 1.0.0 2014/10/06
 * @author Kawakicchi
 */
public class MultiThreadFrame extends JFrame {

	/** serialVersionUID */
	private static final long serialVersionUID = -518870205501922012L;

	public static void main(final String[] args) {
		MultiThreadFrame frame = new MultiThreadFrame();
		frame.setVisible(true);
	}

	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenu menuTask;

	private JMenuItem menuFileExist;

	private JScrollPane scrollAction;
	private TaskTable tblTask;
	private TaskTableModel tblMode;

	private Map<String, TaskData> taskDatas;

	/**
	 * コンストラクタ
	 */
	public MultiThreadFrame() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLayout(null);

		menuBar = new JMenuBar();
		menuFile = new JMenu("ファイル");
		menuFileExist = new JMenuItem("終了");
		menuFile.add(menuFileExist);
		menuBar.add(menuFile);
		menuTask = new JMenu("タスク");
		menuBar.add(menuTask);
		setJMenuBar(menuBar);

		taskDatas = new HashMap<String, MultiThreadFrame.TaskData>();
		addTaskMenu("Action1", TimerTask.class);
		addTaskMenu("Action2", TimerTask.class);
		addTaskMenu("Action3", TimerTask.class);

		tblMode = new TaskTableModel();

		tblTask = new TaskTable(tblMode);
		scrollAction = new JScrollPane(tblTask);
		add(scrollAction);

		TaskManager.getInstance().start();
		TaskManager.getInstance().addTaskManagerListener(new TaskManagerAdapter() {
			@Override
			public void taskManagerStoped() {
				doExit();
			}
		});

		// menu event
		menuFileExist.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doWindowClosing();
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent event) {
				doWindowClosing();
			}
		});
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent event) {
				Insets insets = getInsets();
				int width = getWidth() - (insets.left + insets.right);
				int height = getHeight() - (insets.top + insets.bottom);
				scrollAction.setBounds(0, 0, width, height);
			}
		});
		setSize(800, 600);
	}

	protected final void addTaskMenu(final String aName, final Class<? extends Task> aTask) {
		String taskId = UUIDUtility.generateToShortString();

		TaskData data = new TaskData(taskId, aName, aTask);
		taskDatas.put(data.getTaskId(), data);

		TaskMenuItem menuItem = new TaskMenuItem(data.getTaskId(), data.getName());
		menuTask.add(menuItem);

		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() instanceof TaskMenuItem) {
					TaskMenuItem menu = (TaskMenuItem) e.getSource();
					onClickMenuTask(menu.getTaskId());
				}
			}
		});
	}

	private void doWindowClosing() {
		if (TaskManager.getInstance().isRunningTask()) {
			int option = JOptionPane.showConfirmDialog(this, "<html>実行中のタスクがあります。<br/>タスクをキャンセルして終了しますか？<html>", "", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.YES_OPTION) {
			} else if (option == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}
		TaskManager.getInstance().stop();
	}

	/**
	 * システムを終了する。
	 */
	private void doExit() {
		System.out.println("System exit.");
		setVisible(false);
		dispose();
		System.exit(1);
	}

	private void onClickMenuTask(final String aTaskId) {
		TaskData data = taskDatas.get(aTaskId);
		if (null != data) {
			Task task = null;
			try {
				Class<? extends Task> taskClass = data.getTaskClass();
				if (null != taskClass) {
					Object obj = taskClass.newInstance();
					if (obj instanceof Task) {
						task = (Task) obj;
					}
				}
				if (null == task) {
					return;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				return;
			}

			// 設定画面をサポートしているか
			if (task instanceof TaskConfigurationSupport) {
				TaskConfigurationDialog dlg = ((TaskConfigurationSupport) task).createConfigurationDialog(this);
				if (null != dlg) {
					dlg.setTask(task);
					dlg.addConfigurationDialogListener(new ConfigurationDialogAdapter() {
						@Override
						public void configurationDialogOk(final ConfigurationDialogEvent event) {
							TaskConfigurationDialog dlg = (TaskConfigurationDialog) event.getDialog();
							doExecuteTask(dlg.getTask());
						}
					});
					dlg.setVisible(true);

				} else {
					doExecuteTask(task);
				}
			} else {
				doExecuteTask(task);
			}
		}
	}

	private void doExecuteTask(final Task task) {
		tblMode.addTask(task);

		if (task instanceof TaskProgressSupport) {
			((TaskProgressSupport) task).addTaskProgressListener(new TaskProgressListener() {
				@Override
				public void progress(final TaskProgressEvent event) {
					if (tblMode.updateTask(event.getTask(), event.getPercent(), event.getMessage())) {
						tblTask.repaint();
					}
				}
			});
		}

		TaskManager.getInstance().queue(task);
	}

	/**
	 * このクラスは、タスク情報を保持するクラスです。
	 * 
	 * @since 1.0.0
	 * @version 1.0.0 2014/10/09
	 * @author Kawakicchi
	 */
	private final class TaskData {

		/** task id */
		private String taskId;
		private String name;
		private Class<? extends Task> taskClass;

		public TaskData(final String aTaskId, final String aName, final Class<? extends Task> aClass) {
			taskId = aTaskId;
			name = aName;
			taskClass = aClass;
		}

		public String getTaskId() {
			return taskId;
		}

		public String getName() {
			return name;
		}

		public Class<? extends Task> getTaskClass() {
			return taskClass;
		}
	}

	/**
	 * このクラスは、タスクメニューアイテムクラスです。
	 * 
	 * @since 1.0.0
	 * @version 1.0.0 2014/10/09
	 * @author Kawakicchi
	 */
	private final class TaskMenuItem extends JMenuItem {

		/** serialVersionUID */
		private static final long serialVersionUID = -950719903614204268L;

		/** task id */
		private String taskId;

		/**
		 * コンテキスト
		 * 
		 * @param aId タスクID
		 * @param aName メニュー名
		 */
		public TaskMenuItem(final String aTaskId, final String aName) {
			super(aName);
			taskId = aTaskId;
		}

		/**
		 * タスクIDを取得する。
		 * 
		 * @return タスクID
		 */
		public String getTaskId() {
			return taskId;
		}
	}
}
