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

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Font;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * このクラスは、環境設定の機能を提供するダイアログクラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/03
 * @author kawakicchi
 */
public class PreferenceDialog extends JDialog {

	/** serialVersionUID */
	private static final long serialVersionUID = 4722800289396390658L;

	private static final int DEFAULT_TITLE_HEIGHT = 40;
	private static final int DEFAULT_BUTTON_WIDTH = 120;
	private static final int DEFAULT_BUTTON_HEIGHT = 24;

	private JTree tree;
	private JPanel pnlRight;
	private JSplitPane splitPanel;
	private JButton btnOk;
	private JButton btnCancel;

	private TitlePanel pnlTitle;

	private JPanel pnlClient;

	private PreferenceData data;

	private List<PreferenceDialogListener> listeners;

	/**
	 * コンストラクタ
	 */
	public PreferenceDialog() {
		super();
		init();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param owner オーナー
	 */
	public PreferenceDialog(final Dialog owner) {
		super(owner);
		init();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param owner オーナー
	 * @param title タイトル
	 */
	public PreferenceDialog(final Dialog owner, final String title) {
		super(owner, title);
		init();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param owner オーナー
	 * @param modal モーダル
	 */
	public PreferenceDialog(final Dialog owner, final boolean modal) {
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
	public PreferenceDialog(final Dialog owner, final String title, final boolean modal) {
		super(owner, title, modal);
		init();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param owner オーナー
	 */
	public PreferenceDialog(final Frame owner) {
		super(owner);
		init();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param owner オーナー
	 * @param title タイトル
	 */
	public PreferenceDialog(final Frame owner, final String title) {
		super(owner, title);
		init();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param owner オーナー
	 * @param modal モーダル
	 */
	public PreferenceDialog(final Frame owner, final boolean modal) {
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
	public PreferenceDialog(final Frame owner, final String title, final boolean modal) {
		super(owner, title, modal);
		init();
	}

	public final void addPreferenceDialogListener(final PreferenceDialogListener listener) {
		listeners.add(listener);
	}

	private DefaultMutableTreeNode createNode(final PreferenceData aData) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(aData);
		node(root, aData.getChildren());
		return root;
	}

	private void node(DefaultMutableTreeNode aParent, List<PreferenceData> aDatas) {
		if (null != aDatas) {
			for (PreferenceData data : aDatas) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(data);
				node(node, data.getChildren());
				aParent.add(node);
			}
		}
	}

	private boolean addNode(final String aPath, final String aTitle, final PreferenceClientPanel aClientPanel) {
		String[] ids = aPath.split("/");
		return addNode(ids, aTitle, aClientPanel);
	}

	private boolean addNode(final String[] ids, final String aTitle, final PreferenceClientPanel aClientPanel) {
		List<String> list = new ArrayList<String>();
		for (String id : ids) {
			list.add(id);
		}

		list.remove(0);

		addNode(aTitle, aClientPanel, data, list);
		return true;
	}

	private void addNode(final String aTitle, final PreferenceClientPanel aClientPanel, final PreferenceData parent, final List<String> ids) {
		String id = ids.get(0);

		PreferenceData target = null;
		for (PreferenceData pre : parent.getChildren()) {
			if (id.equals(pre.getId())) {
				target = pre;
				break;
			}
		}

		if (1 == ids.size()) {
			if (null == target) { // 最後でかつ未作成				
				target = new PreferenceData(id, aTitle, aClientPanel);
				parent.add(target);
			} else { // 最後でかつ作成済み
				if (null != target.getTitle() || null != target.getClientPanel()) {
					// err
					System.out.println("重複");
					return;
				}
				target.set(aTitle, aClientPanel);
			}
		} else { // 途中のツリー
			if (null == target) {
				target = new PreferenceData(id);
				parent.add(target);
			}
			ids.remove(0);
			addNode(aTitle, aClientPanel, target, ids);
		}
	}

	private void init() {
		setLayout(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		data = new PreferenceData("", "", null);
		listeners = new ArrayList<PreferenceDialogListener>();

		tree = new JTree();
		tree.setRootVisible(false);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		JScrollPane treeScrollPanel = new JScrollPane();
		treeScrollPanel.setViewportView(tree);

		pnlTitle = new TitlePanel();
		pnlTitle.setLocation(0, 0);

		pnlClient = new JPanel();
		pnlClient.setLayout(null);
		pnlClient.setLocation(0, 40);
		pnlClient.add(new TestPanel());

		pnlRight = new JPanel();
		pnlRight.setLayout(null);
		pnlRight.add(pnlTitle);
		pnlRight.add(pnlClient);

		splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		splitPanel.setLocation(0, 0);
		splitPanel.setDividerLocation(160);
		splitPanel.setDividerSize(6);

		btnOk = new JButton("OK");
		btnOk.setSize(DEFAULT_BUTTON_WIDTH, DEFAULT_BUTTON_HEIGHT);
		btnCancel = new JButton("キャンセル");
		btnCancel.setSize(DEFAULT_BUTTON_WIDTH, DEFAULT_BUTTON_HEIGHT);

		splitPanel.setLeftComponent(treeScrollPanel);
		splitPanel.setRightComponent(pnlRight);

		add(splitPanel);
		add(btnOk);
		add(btnCancel);

		pnlRight.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent event) {
				int width = pnlRight.getWidth();
				int height = pnlRight.getHeight();
				pnlTitle.setSize(width, DEFAULT_TITLE_HEIGHT);
				pnlClient.setSize(width, height - DEFAULT_TITLE_HEIGHT);

				if (0 < pnlClient.getComponentCount()) {
					System.out.println("change component");
					Component c = pnlClient.getComponent(0);
					c.setBounds(0, 0, width, height - DEFAULT_TITLE_HEIGHT);
				}
			}
		});
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				onClickOkButton();
			}
		});
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onClickCancelButton();
			}
		});
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				TreePath[] ps = e.getPaths();
				for (int i = 0; i < ps.length; i++) {
					boolean add = e.isAddedPath(i);

					TreePath tp = ps[i];
					DefaultMutableTreeNode aa = (DefaultMutableTreeNode) tp.getLastPathComponent();
					PreferenceData dd = (PreferenceData) aa.getUserObject();
					if (add) {
						pnlClient.removeAll();

						PreferenceClientPanel component = dd.getClientPanel();
						if (null != component) {
							pnlClient.add(component);
							component.setSize(pnlClient.getSize());
						}
						pnlClient.invalidate();
						pnlClient.validate();
						pnlClient.repaint();

						pnlTitle.setTitle(dd.getTitle());
					}
				}
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

			@Override
			public void windowClosing(final WindowEvent event) {
				onClickCancelButton();
			}
		});

		setSize(800, 400);
	}

	private void onClickOkButton() {

	}

	private void onClickCancelButton() {
		setVisible(false);
		dispose();
	}

	private void doComponentResized() {
		Insets insets = getInsets();

		int width = getWidth() - (insets.left + insets.right);
		int height = getHeight() - (insets.top + insets.bottom);

		splitPanel.setSize(width, height - (5 + DEFAULT_BUTTON_HEIGHT + 10));
		btnOk.setLocation(width - (DEFAULT_BUTTON_WIDTH + 10) * 2, height - (DEFAULT_BUTTON_HEIGHT + 10));
		btnCancel.setLocation(width - (DEFAULT_BUTTON_WIDTH + 10) * 1, height - (DEFAULT_BUTTON_HEIGHT + 10));
	}

	/**
	 * 
	 * @param aPath /aaa/bbb
	 * @param aTitle
	 * @param aPanel
	 */
	public void addPreference(final String aPath, final String aTitle, final PreferenceClientPanel aPanel) {
		addNode(aPath, aTitle, aPanel);
	}

	private void doWindowOpened() {
		DefaultMutableTreeNode root = createNode(data);
		DefaultTreeModel model = new DefaultTreeModel(root);
		tree.setModel(model);

		Container parent = getParent();
		if (null != parent) {
			int x = parent.getX() + (parent.getWidth() - getWidth()) / 2;
			int y = parent.getY() + (parent.getHeight() - getHeight()) / 2;
			setLocation(x, y);
		}
	}

	private class TestPanel extends PreferenceClientPanel {

		/** serialVersionUID */
		private static final long serialVersionUID = -8475543665402763379L;

		private JScrollPane scroll;
		private JPanel panel;
		private JLabel lblName;

		public TestPanel() {
			setLayout(null);
			setBackground(Color.pink);

			panel = new JPanel();
			panel.setLayout(null);
			panel.setBackground(Color.red);
			panel.setLocation(0, 0);
			panel.setSize(600, 600);

			lblName = new JLabel("AAA");
			lblName.setLocation(0, 0);
			lblName.setSize(200, 200);
			panel.add(lblName);

			scroll = new JScrollPane(panel);
			scroll.setLocation(0, 0);
			scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

			add(scroll);

			addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(final ComponentEvent event) {
					scroll.setSize(getSize());
				}
			});
		}
	}

	private class TitlePanel extends JPanel {

		/** serialVersionUID */
		private static final long serialVersionUID = 1839617953546971996L;

		private JLabel lblTitle;
		private JLabel lblBorder;

		public TitlePanel() {
			setLayout(null);

			lblTitle = new JLabel("");
			lblTitle.setFont(new Font(Font.SERIF, Font.BOLD, 24));
			lblTitle.setLocation(10, 0);

			lblBorder = new JLabel();
			lblBorder.setBorder(new BevelBorder(BevelBorder.LOWERED));

			add(lblTitle);
			add(lblBorder);

			addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent event) {
					int width = getWidth();
					int height = getHeight();

					lblTitle.setSize(width - 20, height - 3);
					lblBorder.setBounds(-2, height - 3, width + 4, 3);
				}
			});

		}

		public void setTitle(final String aTitle) {
			lblTitle.setText(aTitle);
		}

	}

	private class PreferenceData {
		private String id;
		private String title;

		private PreferenceClientPanel pnlClient;

		private List<PreferenceData> children;

		/**
		 * コンストラクタ
		 * <p>
		 * 仮作成の場合などに使用
		 * </p>
		 * 
		 * @param aId
		 */
		public PreferenceData(final String aId) {
			id = aId;
			title = null;
			pnlClient = null;
			children = new ArrayList<PreferenceData>();
		}

		/**
		 * コンストラクタ
		 * <p>
		 * 作成時に使用
		 * </p>
		 * 
		 * @param aId
		 * @param aTitle
		 * @param aComponent
		 */
		public PreferenceData(final String aId, final String aTitle, final PreferenceClientPanel aClientPanel) {
			id = aId;
			title = aTitle;
			pnlClient = aClientPanel;
			children = new ArrayList<PreferenceData>();
		}

		public void set(final String aTitle, final PreferenceClientPanel aClientPanel) {
			title = aTitle;
			pnlClient = aClientPanel;
		}

		public String getId() {
			return id;
		}

		public String getTitle() {
			return title;
		}

		public PreferenceClientPanel getClientPanel() {
			return pnlClient;
		}

		public List<PreferenceData> getChildren() {
			return children;
		}

		public void add(PreferenceData data) {
			children.add(data);
		}

		public String toString() {
			return title;
		}
	}
}
