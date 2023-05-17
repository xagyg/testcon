package roast.viewer;

import roast.*;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;

/**
* PENDING
*/
public class LogViewer extends JFrame {

DefaultTreeModel      model;
JSlider               levelJSlider = new JSlider(JSlider.HORIZONTAL,0,0,0);
CompoundBorder        treeCompoundBorder;
JButton               previousErrorButton = new JButton();
JButton               nextErrorButton = new JButton();
JMenuBar              viewerJMenuBar = new JMenuBar();
JMenu                 fileJMenu = new JMenu();
JMenuItem             loadJMenuItem = new JMenuItem();
JMenuItem             exitJMenuItem = new JMenuItem();
DynamicTree           tree = new DynamicTree();
int                   maxLevel = 0;

/**
* Construct a LogViewer object.
*/
public LogViewer() {
	try  {
		initialize();
	} catch (Exception e) {
		e.printStackTrace();
	}
}

/**
* Construct a LogViewer object and load <tt>logFile</tt>.
*
* @param logFile
*	the name of the serialized log file to read from
*/
public LogViewer(String logFile) 
{
	this();
	loadRequest(logFile);
}

private void initialize() throws Exception 
{
	addWindowListener(new WindowAdapter() {
		public void windowClosing( WindowEvent we )	{
			exitRequested(); } });

	addMenu();
	getContentPane().setLayout(new BorderLayout(5,5));
	setTitle("Roast Log Message Viewer");

	JScrollPane treeScrollPane = new JScrollPane(tree);
	treeScrollPane.setVerticalScrollBarPolicy(
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

	JPanel treeJPanel = new JPanel(new BorderLayout(5,5));
	treeJPanel.setBorder(new TitledBorder(
		BorderFactory.createEmptyBorder(5,5,5,5), "Log Messages:"));
	treeJPanel.setPreferredSize(new Dimension(512, 512));
	treeJPanel.add(treeScrollPane, BorderLayout.CENTER);

	JLabel levelJLabel = new JLabel("Level:");

	levelJSlider.setMajorTickSpacing(1);
	levelJSlider.setMinorTickSpacing(1);
	levelJSlider.setPaintLabels(true);
	levelJSlider.setSnapToTicks(true);
	levelJSlider.addChangeListener(new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			levelJSlider_stateChanged(e); }  });
	JPanel levelJPanel = new JPanel(new BorderLayout());
	levelJPanel.add(levelJSlider, BorderLayout.SOUTH);

	previousErrorButton.setText("Previous Error");
	previousErrorButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			previousErrorButton_actionPerformed(e); }  });
	nextErrorButton.setText("Next Error");
	nextErrorButton.setPreferredSize(previousErrorButton.
		getPreferredSize());
	nextErrorButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			nextErrorButton_actionPerformed(e); }  });

	JPanel controlJPanel = new JPanel();
	controlJPanel.setLayout(new BoxLayout(controlJPanel,BoxLayout.X_AXIS));
	controlJPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	controlJPanel.add(Box.createRigidArea(new Dimension(5,0)));
	controlJPanel.add(levelJPanel);
	controlJPanel.add(Box.createRigidArea(new Dimension(30,0)));
	//controlJPanel.add(Box.createHorizontalGlue());
	controlJPanel.add(previousErrorButton);
	controlJPanel.add(Box.createRigidArea(new Dimension(10,0)));
	controlJPanel.add(nextErrorButton);
	controlJPanel.add(Box.createRigidArea(new Dimension(10,0)));

	JPanel optionsJPanel = new JPanel(new GridLayout(1,1));
	optionsJPanel.add(controlJPanel);

	getContentPane().add(treeJPanel, BorderLayout.CENTER);
	getContentPane().add(optionsJPanel, BorderLayout.NORTH);
	pack();
	show();
}

private void addMenu() 
{
	loadJMenuItem.setText("Load...");
	loadJMenuItem.setActionCommand("Load");
	loadJMenuItem.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(ActionEvent e) {
			loadRequest(null); } });
	exitJMenuItem.setText("Exit");
	exitJMenuItem.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(ActionEvent e) {
			exitRequested(); } });
	fileJMenu.setText("File");
	fileJMenu.add(loadJMenuItem);
	fileJMenu.addSeparator();
	fileJMenu.add(exitJMenuItem);
	viewerJMenuBar.add(fileJMenu);
	this.setJMenuBar(viewerJMenuBar);
}

private void levelJSlider_stateChanged(ChangeEvent e) 
{
        if (!levelJSlider.getValueIsAdjusting()) {
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)
			tree.getModel().getRoot();
		DefaultMutableTreeNode currentNode;
		Enumeration treeEnum = rootNode.preorderEnumeration();
		int requestedLevel = levelJSlider.getValue();

		while (treeEnum.hasMoreElements()) {
			currentNode = (DefaultMutableTreeNode)
				treeEnum.nextElement();
			int currentLevel = currentNode.getLevel();
			if (currentLevel == requestedLevel) {
				tree.expandPath(new TreePath(
					currentNode.getPath()));
				
			} else if (currentLevel == requestedLevel+1) {
				tree.collapsePath(new TreePath(
					currentNode.getPath()));
				
			}
		}
		tree.setSelectionPath(new TreePath(
			rootNode.getPath()));
	}
}

private void previousErrorButton_actionPerformed(ActionEvent e)
{
	DefaultMutableTreeNode currentNode;
	TreePath selectedNodePath = tree.getSelectionPath();
	if (selectedNodePath == null) {
		currentNode = (DefaultMutableTreeNode)tree.
			getModel().getRoot();
	} else {
		currentNode = (DefaultMutableTreeNode)selectedNodePath.
			getLastPathComponent();
		currentNode = currentNode.getPreviousNode();
	}
	while (currentNode != null) {
		if (currentNode.getUserObject() instanceof FailureMessage) {
			tree.scrollPathToVisible(new TreePath(
				currentNode.getPath()));
			tree.setSelectionPath(new TreePath(
				currentNode.getPath()));
			return;
		}
		currentNode = currentNode.getPreviousNode();
	}
	JOptionPane.showMessageDialog(this, 
		"No more errors",
		"No more errors", JOptionPane.INFORMATION_MESSAGE);
}

private void nextErrorButton_actionPerformed(ActionEvent e)
{
	DefaultMutableTreeNode currentNode;
	TreePath selectedNodePath = tree.getSelectionPath();
	if (selectedNodePath == null) {
		currentNode = (DefaultMutableTreeNode)tree.
			getModel().getRoot();
	} else {
		currentNode = (DefaultMutableTreeNode)selectedNodePath.
			getLastPathComponent();
		currentNode = currentNode.getNextNode();
	}
	while (currentNode != null) {
		if (currentNode.getUserObject() instanceof FailureMessage) {
			tree.scrollPathToVisible(new TreePath(
				currentNode.getPath()));
			tree.setSelectionPath(new TreePath(
				currentNode.getPath()));
			return;
		}
		currentNode = currentNode.getNextNode();
	}
	JOptionPane.showMessageDialog(this, 
		"No more errors",
		"No more errors", JOptionPane.INFORMATION_MESSAGE);
}

private void loadRequest(String logFileName) 
{
	File logFile;
	if (logFileName == null) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(
			JFileChooser.FILES_AND_DIRECTORIES);
		chooser.addChoosableFileFilter(
			chooser.getAcceptAllFileFilter());
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			logFile = chooser.getSelectedFile();
		} else {
			return;
		}
	} else {
		logFile = new File(logFileName);
	}

	DefaultMutableTreeNode n = new DefaultMutableTreeNode();

	try {
		ObjectInputStream binaryIn = new ObjectInputStream(
			new ProgressMonitorInputStream(
			this, "Reading " + logFile,
			new FileInputStream(logFile)));
		try {
			while (true) {
				n = tree.addLogMessage(
					(LogMessage)binaryIn.readObject());
			}
		} catch (EOFException e) {
		} catch (InterruptedIOException i) {}
		binaryIn.close();
		tree.setLevelVisible(1);
		levelJSlider.setMaximum(tree.maxLevel);
		levelJSlider.updateUI();
	} catch (Exception e) {
	    e.printStackTrace();            /* bjl */
		JOptionPane.showMessageDialog(this, 
			"Cannot open file " + logFileName, 
			"Cannot Open File", JOptionPane.ERROR_MESSAGE);
	}
}

private void exitRequested() {
	System.exit(0);
}

public static void main(String[] args) {
	if (args.length == 0) {
		LogViewer viewer = new LogViewer();
	} else {
		LogViewer viewer = new LogViewer(args[0]);
	}
}

}
