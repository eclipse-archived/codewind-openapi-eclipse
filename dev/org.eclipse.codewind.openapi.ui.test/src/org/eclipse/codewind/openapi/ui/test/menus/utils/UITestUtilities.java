/*******************************************************************************
 * Copyright (c) 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.codewind.openapi.ui.test.menus.utils;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Stack;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.hamcrest.Matcher;

public class UITestUtilities {

	public UITestUtilities() {
		// Empty
	}
	
	public static SWTBotView getProjectExplorerView() {
		SWTWorkbenchBot swtWorkbenchBot = new SWTWorkbenchBot();
		return swtWorkbenchBot.viewById("org.eclipse.ui.navigator.ProjectExplorer");
	}

	public static SWTBotView getPackageExplorerView() {
		SWTWorkbenchBot swtWorkbenchBot = new SWTWorkbenchBot();
		return swtWorkbenchBot.viewById("org.eclipse.jdt.ui.PackageExplorer");
	}

	/*
	 * Given the specific view, which can have multiple root nodes (eg. multiple projects in the project explorer), 
	 * and given the specific root node (rootTreeNode, which is uniquely named amongst other possible root nodes), find the
	 * first child node with the name treeItemText
	 * 
	 */
	public static SWTBotTreeItem findTreeItem(final String rootTreeNode, SWTBotView view, final String treeItemText) {
		SWTBotTreeItem treeItem = findTreeItemForRoot(rootTreeNode, view);
		SWTBotTreeItem targetTreeItem = findTargetNode(treeItem, treeItemText);
		return targetTreeItem;
	}
	
	public static SWTBotTreeItem findTreeItem(final String rootTreeNode, SWTBotView view, final Stack<String> pathSegments) {
		SWTBotTreeItem treeItem = findTreeItemForRoot(rootTreeNode, view);
		SWTBotTreeItem targetTreeItem = findTargetNode(treeItem, pathSegments);
		return targetTreeItem;
	}
	
	@SuppressWarnings("unchecked")
	private static SWTBotTreeItem findTreeItemForRoot(final String rootTreeNode, SWTBotView view) {
		Matcher<? extends Tree> matcher = WidgetMatcherFactory.allOf(WidgetMatcherFactory.widgetOfType(Tree.class));
		List<? extends Widget> widgets = view.bot().widgets(matcher, view.getWidget());
		SWTBotTree tree = null;
		SWTBotTreeItem treeItem = null;
		// Find the root node with the given name rootTreeNode
		for (Widget o : widgets) {
			if (o instanceof Tree) {
				SWTBotTree aTree = new SWTBotTree((Tree) o);
				SWTBotTreeItem aTreeItem = aTree.getTreeItem(rootTreeNode);
				if (aTreeItem.getText().equals(rootTreeNode)) {
					tree = aTree;
					treeItem = aTreeItem;
					break;
				}
			}
		}
		assertNotNull("Expected top level node " + rootTreeNode + " does not exist", tree);
		tree.expandNode(rootTreeNode, true);
		return treeItem;
	}
	
	private static SWTBotTreeItem findTargetNode(SWTBotTreeItem treeItem, String targetName) {
		SWTBotTreeItem[] items = treeItem.getItems();
		for (SWTBotTreeItem aTreeItem : items) {
			if (!aTreeItem.isEnabled()) {
				continue;
			}
			if (aTreeItem.getText().equals(targetName)) {
				return aTreeItem;
			} else {
				SWTBotTreeItem potential = findTargetNode(aTreeItem, targetName);
				if (potential != null) {
					return potential;
				}
			}
		}
		return null;
	}
	
	private static SWTBotTreeItem findTargetNode(SWTBotTreeItem treeItem, Stack<String> pathSegments) {
		SWTBotTreeItem[] items = treeItem.getItems();
		for (SWTBotTreeItem aTreeItem : items) {
			if (!aTreeItem.isEnabled()) {
				continue;
			}
			if (aTreeItem.getText().equals(pathSegments.peek())) {
				pathSegments.pop();
				if (pathSegments.isEmpty()) {
					return aTreeItem;
				} else {
					SWTBotTreeItem potential = findTargetNode(aTreeItem, pathSegments);
					if (potential != null) {
						return potential;
					}
				}
			} else {
				SWTBotTreeItem potential = findTargetNode(aTreeItem, pathSegments);
				if (potential != null) {
					return potential;
				}
			}
		}
		return null;
	}
}
