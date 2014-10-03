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
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
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

	private JTree tree;
	private JPanel pnlLeft;
	private JSplitPane splitPanel;
	private JButton btnOk;
	private JButton btnCancel;
	
	private TitlePanel pnlTitle;
	
	private JScrollPane pnlClient;

	private PreferenceData data;

	private class PreferenceData {
		private String id;
		private String title;

		private JComponent component;

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
			component = null;
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
		public PreferenceData(final String aId, final String aTitle, final JComponent aComponent) {
			id = aId;
			title = aTitle;
			component = aComponent;
			children = new ArrayList<PreferenceData>();
		}

		public void set(final String aTitle, final JComponent aComponent) {
			title = aTitle;
			component = aComponent;
		}

		public String getId() {
			return id;
		}

		public String getTitle() {
			return title;
		}

		public JComponent getComponent() {
			return component;
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

	private DefaultMutableTreeNode createNode(final PreferenceData aData) {
		// test
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

	private boolean addNode(final String aPath, final String aTitle, final JComponent aComponent) {
		String[] ids = aPath.split("/");
		return addNode(ids, aTitle, aComponent);
	}

	private boolean addNode(final String[] ids, final String aTitle, final JComponent aComponent) {
		for (String id : ids) {
			// System.out.println(id);
		}

		List<String> list = new ArrayList<String>();
		for (String id : ids) {
			list.add(id);
		}

		list.remove(0);

		addNode(aTitle, aComponent, data, list);
		return true;
	}

	private void addNode(final String aTitle, final JComponent aComponent, PreferenceData parent, List<String> ids) {
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
				target = new PreferenceData(id, aTitle, aComponent);
				parent.add(target);
			} else { // 最後でかつ作成済み
				if (null != target.getTitle() || null != target.getComponent()) {
					// err
					System.out.println("重複");
					return;
				}
				target.set(aTitle, aComponent);
			}
		} else { // 途中のツリー
			if (null == target) {
				target = new PreferenceData(id);
				parent.add(target);
			}
			ids.remove(0);
			addNode(aTitle, aComponent, target, ids);
		}
	}

	private void init() {
		setLayout(null);

		data = new PreferenceData("", "", null);
		addNode("/aaa/bbb/ccc", "CCC", new JButton("button"));
		addNode("/aaa/bbb", "BBB", null);
		addNode("/bbb", "BBB", new JLabel("Label"));
		addNode("/bbb/1", "BBB1", null);
		addNode("/bbb/2", "BBB2", null);

		DefaultMutableTreeNode root = createNode(data);

		tree = new JTree(root);
		tree.setRootVisible(false);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		JScrollPane treeScrollPanel = new JScrollPane();
		treeScrollPanel.setViewportView(tree);

		pnlTitle = new TitlePanel();
		pnlTitle.setLocation(0, 0);
		pnlClient = new JScrollPane();
		pnlClient.setBorder(new EmptyBorder(2, 2, 2, 2));
		pnlClient.setLocation(0,40);
		
		pnlLeft = new JPanel();
		pnlLeft.setLayout(null);
		pnlLeft.add(pnlTitle);
		pnlLeft.add(pnlClient);

		splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true);
		splitPanel.setLocation(0, 0);
		splitPanel.setDividerLocation(160);
		splitPanel.setDividerSize(6);
		btnOk = new JButton("OK");
		btnOk.setSize(160, 32);
		btnCancel = new JButton("キャンセル");
		btnCancel.setSize(160, 32);

		splitPanel.setLeftComponent(treeScrollPanel);
		splitPanel.setRightComponent(pnlLeft);

		add(splitPanel);
		add(btnOk);
		add(btnCancel);

		pnlLeft.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent event) {
				int width = pnlLeft.getWidth();
				int height = pnlLeft.getHeight();
				pnlTitle.setSize(width,40);
				pnlClient.setSize(width, height-40);
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
						if (null != dd.getComponent()) {
							pnlClient.add(dd.getComponent());
							dd.getComponent().setBounds(10, 10, 80, 80);
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
		});

		setSize(800, 400);
	}
	
	private void onClickOkButton() {
		
	}
	
	private void onClickCancelButton() {
		
	}

	private void doComponentResized() {
		Insets insets = getInsets();

		int width = getWidth() - (insets.left + insets.right);
		int height = getHeight() - (insets.top + insets.bottom);

		splitPanel.setSize(width, height - (5 + 32 + 10));
		btnOk.setLocation(width - (160 + 10) * 2, height - (32 + 10));
		btnCancel.setLocation(width - (160 + 10) * 1, height - (32 + 10));
	}

	private void doWindowOpened() {
		Container parent = getParent();
		if (null != parent) {
			int x = parent.getX() + (parent.getWidth() - getWidth()) / 2;
			int y = parent.getY() + (parent.getHeight() - getHeight()) / 2;
			setLocation(x, y);
		}
	}
	
	private class TitlePanel extends JPanel {
		
		private JLabel lblTitle;
		private JLabel lblBorder;
		
		public TitlePanel() {
			setLayout(null);
			
			lblTitle = new JLabel("");			
			lblTitle.setFont(new Font(Font.SERIF, Font.BOLD, 24));
			lblTitle.setLocation(10,0);
			
			lblBorder = new JLabel();
			lblBorder.setBorder( new BevelBorder(BevelBorder.LOWERED) );
						
			add(lblTitle);
			add(lblBorder);
			
			addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent event) {
					int width = getWidth();
					int height = getHeight();
					
					lblTitle.setSize(width-20,height-3);
					lblBorder.setBounds(-2, height-3 ,width+4, 3);
				}
			});
			
		}
		
		public void setTitle(final String aTitle) {
			lblTitle.setText(aTitle);
		}
		
	}
}
