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

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.azkfw.gui.frame.TaskConfigurationDialog;
import org.azkfw.gui.frame.TaskConfigurationSupport;

/**
 * @since 1.0.0
 * @version 1.0.0 2014/10/06
 * @author Kawakicchi
 */
public class TimerTask extends AbstractProgressTask implements TaskConfigurationSupport {

	private long time;

	/**
	 * タイマー時間(s)
	 * 
	 * @param aTime
	 */
	public void setTime(final int aTime) {
		time = aTime * 1000;
	}

	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void doInitialize() {
	}

	@Override
	public TaskResult doExecute() {
		int cnt = 0;
		while (cnt < time) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			cnt += 100;
			callProgressListener((double) cnt / (double) time * 100.f, String.format("%.2f秒経過", (double) cnt / 1000.f));
		}
		callProgressListener(100.f, "完了");
		return TaskResult.success();
	}

	@Override
	public void doDestory() {

	}

	@Override
	public TaskConfigurationDialog createConfigurationDialog(final JFrame frame) {
		return new TimerTaskConfigurationDialog(frame, this);
	}

	private class TimerTaskConfigurationDialog extends TaskConfigurationDialog {

		/** serialVersionUID */
		private static final long serialVersionUID = -7351679085781831298L;

		private TimerTask action;
		private JLabel lblTime;
		private JTextField txtTime;

		/**
		 * コンストラクタ
		 * 
		 * @param frame
		 */
		public TimerTaskConfigurationDialog(final JFrame frame, final TimerTask aAction) {
			super(frame);

			action = aAction;

			lblTime = new JLabel("Time(sec)");
			lblTime.setLocation(10, 10);
			lblTime.setSize(120, 32);
			add(lblTime);

			txtTime = new JTextField("10");
			txtTime.setLocation(140, 10);
			txtTime.setSize(80, 32);
			add(txtTime);

			setClientSize(300, 300);
		}

		protected boolean onClickOK() {
			int time = 0;
			try {
				time = Integer.parseInt(txtTime.getText());
			} catch (Exception ex) {

			}
			if (0 >= time) {
				return false;
			}

			action.setTime(time);
			return true;
		}

	}
}
