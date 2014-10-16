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
package org.azkfw.gui.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * このクラスは、ファイルエクスプローラ機能を実装したツリークラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/14
 * @author kawakicchi
 */
public class FileExplorerTree extends JTree {

	/** serialVersionUID */
	private static final long serialVersionUID = 7291484363688454423L;

	private FileExplorerTreeEvent listenerEvent;
	private List<FileExplorerTreeListener> listeners;

	/**
	 * コンストラクタ
	 */
	public FileExplorerTree() {
		listenerEvent = new FileExplorerTreeEvent(this);
		listeners = new ArrayList<FileExplorerTreeListener>();

		setRootVisible(false);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("ROOT");

		File[] roots = File.listRoots();
		for (File file : roots) {
			DefaultMutableTreeNode child = new FileExplorerDriverTreeNode(file.getAbsolutePath());
			root.add(child);
		}
		DefaultTreeModel model = new DefaultTreeModel(root);
		setModel(model);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent event) {
				if (SwingUtilities.isRightMouseButton(event)) {
					int x = event.getX();
					int y = event.getY();
					int row = getRowForLocation(x, y);
					if (-1 != row) {
						setSelectionRow(row);
						Object obj = getSelectionPath().getLastPathComponent();
						if (obj instanceof FileExplorerFileTreeNode) {
							FileExplorerFileTreeNode node = (FileExplorerFileTreeNode) obj;
							JPopupMenu menu = createPopupMenu(node, node.getFile());
							if (null != menu) {
								menu.show(event.getComponent(), x, y);
							}
						}
					}
				}
			}

			@Override
			public void mouseClicked(final MouseEvent event) {
				if (SwingUtilities.isLeftMouseButton(event) && event.getClickCount() == 2) {
					JTree tree = (JTree) event.getSource();
					TreePath path = tree.getPathForLocation(event.getX(), event.getY());
					if (null != path) {
						Object obj = path.getLastPathComponent();
						if (obj instanceof FileExplorerFileTreeNode) {
							FileExplorerFileTreeNode fileNode = (FileExplorerFileTreeNode) obj;
							doOpenFile(fileNode.getFile());
						}
					}
				}
			}
		});

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					JTree tree = (JTree) event.getSource();
					TreePath path = tree.getLeadSelectionPath();
					Object obj = path.getLastPathComponent();
					if (obj instanceof FileExplorerFileTreeNode) {
						FileExplorerFileTreeNode fileNode = (FileExplorerFileTreeNode) obj;
						doOpenFile(fileNode.getFile());
					}
					event.consume();
				}
			}
		});

		addTreeWillExpandListener(new TreeWillExpandListener() {
			@Override
			public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
				TreePath path = event.getPath();
				Object obj = path.getLastPathComponent();
				if (obj instanceof FileExplorerTreeNode) {
					FileExplorerTreeNode node = (FileExplorerTreeNode) obj;
					doOpenChild(node);
				}
			}

			@Override
			public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
			}
		});
	}

	public void addFileExplorerTreeListener(final FileExplorerTreeListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	public void removeFileExplorerTreeListener(final FileExplorerTreeListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	/**
	 * ディレクトリ・ファイルでダブルクリック、Enter時に呼び出される
	 * 
	 * @param aFile
	 */
	private void doOpenFile(final File aFile) {
		synchronized (listeners) {
			for (FileExplorerTreeListener listener : listeners) {
				try {
					listener.fileExplorerTreeClickedFile(listenerEvent, aFile);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	private JPopupMenu createPopupMenu(final FileExplorerFileTreeNode aNode, final File aFile) {
		JPopupMenu menu = new JPopupMenu();
		synchronized (listeners) {
			for (FileExplorerTreeListener listener : listeners) {
				try {
					List<JMenuItem> menuItems = listener.fileExplorerTreeMenuFile(listenerEvent, aFile);
					if (null != menuItems) {
						for (JMenuItem menuItem : menuItems) {
							menu.add(menuItem);
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		if (aFile.isDirectory()) {
			if (0 < menu.getComponentCount()) {
				menu.addSeparator();
			}

			ClassLoader cl = this.getClass().getClassLoader();
			Icon icon = new ImageIcon(cl.getResource("org/azkfw/gui/tree/FileExplorerTree_refresh.png"));
			JMenuItem menuRefresh = new JMenuItem("更新", icon);
			menu.add(menuRefresh);

			menuRefresh.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent event) {
					//System.out.println("menu : " + aNode.getName());

					File[] files = aFile.listFiles();
					refreshNode(aNode, files);
				}
			});
		}

		return menu;
	}

	private void refreshNode(final FileExplorerTreeNode aNode, final File[] aFiles) {
		List<File> lst = new ArrayList<File>();

		if (null != aFiles) {

			synchronized (listeners) {
				for (File f : aFiles) {
					if (!f.isHidden()) {
						boolean cancel = false;
						try {
							for (FileExplorerTreeListener listener : listeners) {
								cancel = listener.fileExplorerTreeAppendingFile(listenerEvent, f);
								if (!cancel) {
									break;
								}
							}
							if (cancel) {
								lst.add(f);
								for (FileExplorerTreeListener listener : listeners) {
									listener.fileExplorerTreeAppendedFile(listenerEvent, f);
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}

			Collections.sort(lst, new Comparator<File>() {
				public int compare(final File file1, final File file2) {
					if (file1.isFile() != file2.isFile()) {
						if (file1.isFile()) {
							return 1;
						} else {
							return -1;
						}
					} else {
						return file1.getName().compareTo(file2.getName());
					}
				}
			});
		} else {
			// ファイル無
		}

		aNode.refreshNode(lst);
	}

	private void doOpenChild(final FileExplorerTreeNode node) {
		if (!node.isOpened()) {
			File file = getFile(node);
			File[] files = file.listFiles();
			refreshNode(node, files);
		}
	}

	private File getFile(FileExplorerTreeNode node) {
		File file = null;
		List<String> paths = new ArrayList<String>();
		FileExplorerTreeNode target = node;
		paths.add(0, target.getName());
		while (true) {
			TreeNode tn = target.getParent();
			if (tn instanceof FileExplorerTreeNode) {
				target = (FileExplorerTreeNode) tn;
				paths.add(0, target.getName());
			} else {
				break;
			}
		}
		Path path = Paths.get("", paths.toArray(new String[0]));
		file = path.toFile();

		return file;
	}

	/**
	 * このクラスは、ファイルエクスプローラ用のツリーノードクラスです。
	 * 
	 * @since 1.0.0
	 * @version 1.0.0 2014/10/14
	 * @author kawakicchi
	 */
	private abstract class FileExplorerTreeNode extends DefaultMutableTreeNode {

		/** serialVersionUID */
		private static final long serialVersionUID = -3687831061073761255L;

		private boolean openFlag;

		public FileExplorerTreeNode(final String aName) {
			super(aName);
			openFlag = false;
		}

		public boolean isOpened() {
			return openFlag;
		}

		protected String getName() {
			return getUserObject().toString();
		}

		public void refreshNode(final List<File> aFiles) {
			openFlag = true;
			removeAllChildren();
			for (File f : aFiles) {
				DefaultMutableTreeNode child = new FileExplorerFileTreeNode(f);
				add(child);
			}

			((DefaultTreeModel) getModel()).reload(this);
		}
	}

	/**
	 * このクラスは、ドライブ用のツリーノードクラスです。
	 * 
	 * @since 1.0.0
	 * @version 1.0.0 2014/10/14
	 * @author kawakicchi
	 */
	private class FileExplorerDriverTreeNode extends FileExplorerTreeNode {

		/** serialVersionUID */
		private static final long serialVersionUID = -3171203904400080810L;

		/**
		 * コンストラクター
		 * 
		 * @param userObject 当ノードのユーザーオブジェクト
		 */
		public FileExplorerDriverTreeNode(final String aName) {
			super(aName);
		}

		@Override
		public boolean isLeaf() {
			return false;
		}
	}

	/**
	 * このクラスは、ファイル用のツリーノードクラスです。
	 * 
	 * @since 1.0.0
	 * @version 1.0.0 2014/10/14
	 * @author kawakicchi
	 */
	private class FileExplorerFileTreeNode extends FileExplorerTreeNode {

		/** serialVersionUID */
		private static final long serialVersionUID = 7460805214915893826L;

		/** file */
		private File file;

		/**
		 * コンストラクター
		 * 
		 * @param userObject 当ノードのユーザーオブジェクト
		 */
		public FileExplorerFileTreeNode(final File aFile) {
			super(aFile.getName());
			file = aFile;
		}

		@Override
		public boolean isLeaf() {
			return file.isFile();
		}

		public File getFile() {
			return file;
		}
	}
}
