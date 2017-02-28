import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;

import ca.pfv.spmf.algorithms.associationrules.agrawal94_association_rules.AlgoAgrawalFaster94;
import ca.pfv.spmf.algorithms.frequentpatterns.apriori.AlgoApriori;
import ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth.AlgoFPGrowth;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemsets;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JRadioButton;

public class App {

	private JFrame frmWebContentMining;
	private Color white = new Color(255, 255, 255);
	private Color foreGr = new Color(51, 51, 51);
	private Color btnBorderColor = new Color(204, 204, 204);
	private Color btnHoverColor = new Color(230, 230, 230);
	private Color btnClickedColor = new Color(212, 212, 212);
	private JPanel dbPanel;
	private JScrollPane scrollPane;
	private Font defFont = new Font("Segoe UI", Font.PLAIN, 14);
	private Font smallerFont = new Font("Segoe UI", Font.PLAIN, 12);
	private JPanel filePanel;
	private JLabel lblJunkFileName;
	private JLabel lblCleanFileName;
	private JLabel lblCelanRowsFileName;
	private JLabel lblSPMFReadyFileName;
	private JTextField txtJunk;
	private JTextField txtCleanSortedTagsName;
	private JTextField txtCleanRowsFileName;
	private JTextField txtSPMFFileName;
	private JLabel lblJunkStatus;
	private JLabel lblCleanSortedStatus;
	private JLabel lblCleanRowsStatus;
	private JLabel lblSMPFFileStatus;
	private BSButton btnMine;

	private ArrayList<ArrayList<String>> cleanRows;
	private HashMap<String, String> uniqueHashTagsWithIds;
	private HashMap<String, String> uniqueHashTagsWithIdsReversed;

	private String cleanSortedTagsFile = "";
	private String junkFile = "";
	private String cleanRowsFile = "";
	private String inputSPMFFile = "";

	private boolean workerOneSucceeded = true;
	private boolean workerTwoSucceeded = true;
	private boolean fileCanBeShown = true;
	private JPanel miningPanel;
	private JLabel lblAprioriResultFile;
	private JTextField txtAprioriResultFile;
	private JLabel lblMinSupport;
	private JTextField txtAprioriMinSup;
	private JTextArea txtResultsOfMining;
	private JLabel lblAprioryProcessing;
	private JLabel lblDBLoading;
	@SuppressWarnings("rawtypes")
	private JList fileContentList;
	private JLabel lblChooseMethod;
	private JPanel rbtnPanel;
	private JRadioButton rdbtnApriori;
	private JRadioButton rdbtnFpGrowth;
	private JPanel pnlApriori;
	private JPanel pnlFPG;
	private JLabel lblMinSupFP;
	private JTextField txtFPMinSup;
	private JLabel lblFPMinConf;
	private JTextField txtFPMinConf;
	private JLabel lblFpGrowthResult;
	private JTextField txtFPGResultFile;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App window = new App();
					window.frmWebContentMining.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public App() {
		initialize();
		showDB();
	}

	@SuppressWarnings("rawtypes")
	private void initialize() {
		UIManager.put("OptionPane.background", white);
		UIManager.put("OptionPane.messageFont", defFont);
		UIManager.put("Panel.background", white);
		// UIManager.put("RadioButton.focus", btnHoverColor);
		// UIManager.put("RadioButton.highlight", btnHoverColor);
		// UIManager.put("RadioButton.interiorBackground", btnHoverColor);
		// UIManager.put("RadioButton.select", btnHoverColor);

		frmWebContentMining = new JFrame();
		frmWebContentMining.setBackground(Color.WHITE);
		frmWebContentMining.setTitle("Web Content Mining");
		frmWebContentMining.getContentPane().setForeground(foreGr);
		frmWebContentMining.getContentPane().setBackground(white);
		frmWebContentMining
				.setIconImage(Toolkit.getDefaultToolkit().getImage(App.class.getResource("/Imgs/Diamond-icon.png")));
		frmWebContentMining.setBounds(100, 100, 862, 509);
		frmWebContentMining.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 14, 74, 105, 49, 0, 520, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 20, 30, 14, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, Double.MIN_VALUE };
		frmWebContentMining.getContentPane().setLayout(gridBagLayout);

		BSButton btnShowDB = new BSButton("Show DB");
		btnShowDB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showScreen(ScreenType.DBScreen);
			}
		});
		btnShowDB.setFont(defFont);
		btnShowDB.setFocusPainted(false);
		btnShowDB.setForeground(foreGr);
		btnShowDB.setBorder(new LineBorder(btnBorderColor));
		btnShowDB.setBackground(white);
		btnShowDB.setHoverBackgroundColor(btnHoverColor);
		btnShowDB.setPressedBackgroundColor(btnClickedColor);

		GridBagConstraints gbc_btnShowDB = new GridBagConstraints();
		gbc_btnShowDB.fill = GridBagConstraints.BOTH;
		gbc_btnShowDB.insets = new Insets(0, 0, 5, 5);
		gbc_btnShowDB.gridx = 1;
		gbc_btnShowDB.gridy = 1;
		frmWebContentMining.getContentPane().add(btnShowDB, gbc_btnShowDB);

		BSButton btnStartCleaning = new BSButton("Show DB");
		btnStartCleaning.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showScreen(ScreenType.FileScreen);
			}
		});
		btnStartCleaning.setText("Data Cleaning");
		btnStartCleaning.setPressedBackgroundColor(new Color(212, 212, 212));
		btnStartCleaning.setHoverBackgroundColor(new Color(230, 230, 230));
		btnStartCleaning.setForeground(new Color(51, 51, 51));
		btnStartCleaning.setFont(defFont);
		btnStartCleaning.setFocusPainted(false);
		btnStartCleaning.setBorder(new LineBorder(btnBorderColor));
		btnStartCleaning.setBackground(white);

		GridBagConstraints gbc_btnStartCleaning = new GridBagConstraints();
		gbc_btnStartCleaning.fill = GridBagConstraints.BOTH;
		gbc_btnStartCleaning.insets = new Insets(0, 0, 5, 5);
		gbc_btnStartCleaning.gridx = 2;
		gbc_btnStartCleaning.gridy = 1;
		frmWebContentMining.getContentPane().add(btnStartCleaning, gbc_btnStartCleaning);

		btnMine = new BSButton("Mine");
		btnMine.setIcon(null);
		btnMine.setEnabled(false);
		btnMine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showScreen(ScreenType.MiningScreen);
			}
		});
		btnMine.setPressedBackgroundColor(new Color(212, 212, 212));
		btnMine.setHoverBackgroundColor(new Color(230, 230, 230));
		btnMine.setForeground(new Color(51, 51, 51));
		btnMine.setFont(defFont);
		btnMine.setFocusPainted(false);
		btnMine.setBorder(new LineBorder(btnBorderColor));
		btnMine.setBackground(white);
		GridBagConstraints gbc_btnMine = new GridBagConstraints();
		gbc_btnMine.fill = GridBagConstraints.BOTH;
		gbc_btnMine.insets = new Insets(0, 0, 5, 5);
		gbc_btnMine.gridx = 3;
		gbc_btnMine.gridy = 1;
		frmWebContentMining.getContentPane().add(btnMine, gbc_btnMine);

		dbPanel = new JPanel();
		dbPanel.setLayout(new BorderLayout());
		dbPanel.setForeground(foreGr);
		dbPanel.setBackground(white);
		dbPanel.setBorder(new LineBorder(new Color(204, 204, 204)));
		dbPanel.setBorder(new CompoundBorder(dbPanel.getBorder(), new EmptyBorder(6, 6, 6, 6)));
		GridBagConstraints gbc_dbPanel = new GridBagConstraints();
		gbc_dbPanel.gridheight = 13;
		gbc_dbPanel.gridwidth = 5;
		gbc_dbPanel.insets = new Insets(0, 0, 5, 5);
		gbc_dbPanel.fill = GridBagConstraints.BOTH;
		gbc_dbPanel.gridx = 1;
		gbc_dbPanel.gridy = 2;
		dbPanel.setVisible(true);

		filePanel = new JPanel();
		filePanel.setBorder(new LineBorder(new Color(204, 204, 204)));
		Border border = filePanel.getBorder();
		Border margin = new EmptyBorder(6, 6, 6, 6);
		filePanel.setBorder(new CompoundBorder(border, margin));
		filePanel.setForeground(foreGr);
		filePanel.setBackground(white);
		GridBagConstraints gbc_filePanel = new GridBagConstraints();
		gbc_filePanel.gridheight = 13;
		gbc_filePanel.gridwidth = 5;
		gbc_filePanel.insets = new Insets(0, 0, 5, 5);
		gbc_filePanel.fill = GridBagConstraints.BOTH;
		gbc_filePanel.gridx = 1;
		gbc_filePanel.gridy = 2;
		filePanel.setVisible(false);

		miningPanel = new JPanel();
		miningPanel.setBorder(new LineBorder(new Color(204, 204, 204)));
		miningPanel.setBorder(new CompoundBorder(miningPanel.getBorder(), new EmptyBorder(6, 6, 6, 6)));
		miningPanel.setForeground(foreGr);
		miningPanel.setBackground(white);
		GridBagConstraints gbc_miningPanel = new GridBagConstraints();
		gbc_miningPanel.gridheight = 13;
		gbc_miningPanel.gridwidth = 5;
		gbc_miningPanel.insets = new Insets(0, 0, 5, 5);
		gbc_miningPanel.fill = GridBagConstraints.BOTH;
		gbc_miningPanel.gridx = 1;
		gbc_miningPanel.gridy = 2;
		GridBagLayout gbl_miningPanel = new GridBagLayout();
		gbl_miningPanel.columnWidths = new int[] { 88, 151, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 37, 0, 0, 0, 0, 0, 0 };
		gbl_miningPanel.rowHeights = new int[] { 30, 30, 30, 30, 30, 0, 0, 0 };
		gbl_miningPanel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_miningPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0 };
		miningPanel.setLayout(gbl_miningPanel);

		lblChooseMethod = new JLabel("Choose Method:");
		lblChooseMethod.setFont(defFont);
		lblChooseMethod.setForeground(foreGr);
		GridBagConstraints gbc_lblChooseMethod = new GridBagConstraints();
		gbc_lblChooseMethod.fill = GridBagConstraints.VERTICAL;
		gbc_lblChooseMethod.anchor = GridBagConstraints.WEST;
		gbc_lblChooseMethod.insets = new Insets(0, 0, 5, 5);
		gbc_lblChooseMethod.gridx = 0;
		gbc_lblChooseMethod.gridy = 0;
		miningPanel.add(lblChooseMethod, gbc_lblChooseMethod);

		rbtnPanel = new JPanel();
		rbtnPanel.setBackground(Color.WHITE);
		GridBagConstraints gbc_rbtnPanel = new GridBagConstraints();
		gbc_rbtnPanel.insets = new Insets(0, 0, 5, 5);
		gbc_rbtnPanel.fill = GridBagConstraints.BOTH;
		gbc_rbtnPanel.gridx = 1;
		gbc_rbtnPanel.gridy = 0;
		miningPanel.add(rbtnPanel, gbc_rbtnPanel);
		GridBagLayout gbl_rbtnPanel = new GridBagLayout();
		gbl_rbtnPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_rbtnPanel.rowHeights = new int[] { 0, 0 };
		gbl_rbtnPanel.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_rbtnPanel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		rbtnPanel.setLayout(gbl_rbtnPanel);

		rdbtnApriori = new JRadioButton("Apriori");
		rdbtnApriori.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnApriori.isSelected()) {
					// rdbtnFpGrowth.setSelected(false);
					pnlFPG.setVisible(false);
					pnlApriori.setVisible(true);
				} else {
					// rdbtnFpGrowth.setSelected(true);
					pnlFPG.setVisible(true);
					pnlApriori.setVisible(false);
				}
			}
		});
		rdbtnApriori.setForeground(foreGr);
		rdbtnApriori.setSelected(true);
		rdbtnApriori.setBackground(Color.WHITE);
		rdbtnApriori.setFont(smallerFont);
		rdbtnApriori.setFocusPainted(false);
		rdbtnApriori.setIcon(new ImageIcon(App.class.getResource("/Imgs/r_off.png")));
		rdbtnApriori.setSelectedIcon(new ImageIcon(App.class.getResource("/Imgs/r_on.png")));
		rdbtnApriori.setRolloverIcon(new ImageIcon(App.class.getResource("/Imgs/r_off.png")));
		GridBagConstraints gbc_rdbtnApriori = new GridBagConstraints();
		gbc_rdbtnApriori.fill = GridBagConstraints.BOTH;
		gbc_rdbtnApriori.insets = new Insets(0, 0, 0, 5);
		gbc_rdbtnApriori.gridx = 0;
		gbc_rdbtnApriori.gridy = 0;
		rbtnPanel.add(rdbtnApriori, gbc_rdbtnApriori);

		rdbtnFpGrowth = new JRadioButton("FP Growth");
		rdbtnFpGrowth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtnFpGrowth.isSelected()) {
					// rdbtnApriori.setSelected(false);
					pnlFPG.setVisible(true);
					pnlApriori.setVisible(false);
				} else {
					// rdbtnApriori.setSelected(true);
					pnlFPG.setVisible(false);
					pnlApriori.setVisible(true);
				}
			}
		});
		rdbtnFpGrowth.setForeground(foreGr);
		rdbtnFpGrowth.setBackground(Color.WHITE);
		rdbtnFpGrowth.setFocusPainted(false);
		rdbtnFpGrowth.setFont(smallerFont);
		rdbtnFpGrowth.setIcon(new ImageIcon(App.class.getResource("/Imgs/r_off.png")));
		rdbtnFpGrowth.setSelectedIcon(new ImageIcon(App.class.getResource("/Imgs/r_on.png")));
		rdbtnFpGrowth.setRolloverIcon(new ImageIcon(App.class.getResource("/Imgs/r_off.png")));
		GridBagConstraints gbc_rdbtnFpGrowth = new GridBagConstraints();
		gbc_rdbtnFpGrowth.fill = GridBagConstraints.BOTH;
		gbc_rdbtnFpGrowth.gridx = 1;
		gbc_rdbtnFpGrowth.gridy = 0;
		rbtnPanel.add(rdbtnFpGrowth, gbc_rdbtnFpGrowth);

		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnApriori);
		group.add(rdbtnFpGrowth);

		pnlApriori = new JPanel();
		pnlApriori.setBackground(Color.WHITE);
		pnlApriori.setBorder(new LineBorder(new Color(204, 204, 204)));
		pnlApriori.setBorder(new CompoundBorder(pnlApriori.getBorder(), new EmptyBorder(6, 6, 6, 6)));
		GridBagConstraints gbc_pnlApriori = new GridBagConstraints();
		gbc_pnlApriori.gridheight = 3;
		gbc_pnlApriori.gridwidth = 8;
		gbc_pnlApriori.insets = new Insets(0, 0, 5, 5);
		gbc_pnlApriori.fill = GridBagConstraints.BOTH;
		gbc_pnlApriori.gridx = 1;
		gbc_pnlApriori.gridy = 1;
		miningPanel.add(pnlApriori, gbc_pnlApriori);
		GridBagLayout gbl_pnlApriori = new GridBagLayout();
		gbl_pnlApriori.columnWidths = new int[] { 0, 153, 0, 0, 0, 0 };
		gbl_pnlApriori.rowHeights = new int[] { 30, 30, 30, 0 };
		gbl_pnlApriori.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_pnlApriori.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		pnlApriori.setLayout(gbl_pnlApriori);

		lblMinSupport = new JLabel("Min Support: ");
		GridBagConstraints gbc_lblMinSupport = new GridBagConstraints();
		gbc_lblMinSupport.fill = GridBagConstraints.VERTICAL;
		gbc_lblMinSupport.anchor = GridBagConstraints.WEST;
		gbc_lblMinSupport.insets = new Insets(0, 0, 5, 5);
		gbc_lblMinSupport.gridx = 0;
		gbc_lblMinSupport.gridy = 0;
		pnlApriori.add(lblMinSupport, gbc_lblMinSupport);
		lblMinSupport.setFont(defFont);
		lblMinSupport.setForeground(foreGr);

		txtAprioriMinSup = new JTextField();
		GridBagConstraints gbc_txtMinSup = new GridBagConstraints();
		gbc_txtMinSup.insets = new Insets(0, 0, 5, 5);
		gbc_txtMinSup.fill = GridBagConstraints.BOTH;
		gbc_txtMinSup.gridx = 1;
		gbc_txtMinSup.gridy = 0;
		pnlApriori.add(txtAprioriMinSup, gbc_txtMinSup);
		txtAprioriMinSup.setText("0.4");
		txtAprioriMinSup.setForeground(foreGr);
		txtAprioriMinSup.setFont(smallerFont);
		txtAprioriMinSup.setBorder(new TextBubbleBorder(btnBorderColor, 1, 4, 0));
		txtAprioriMinSup.setColumns(10);

		lblAprioriResultFile = new JLabel("Apriori Result File: ");
		lblAprioriResultFile.setForeground(foreGr);
		GridBagConstraints gbc_lblAprioriResultFile = new GridBagConstraints();
		gbc_lblAprioriResultFile.anchor = GridBagConstraints.WEST;
		gbc_lblAprioriResultFile.fill = GridBagConstraints.VERTICAL;
		gbc_lblAprioriResultFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblAprioriResultFile.gridx = 0;
		gbc_lblAprioriResultFile.gridy = 1;
		pnlApriori.add(lblAprioriResultFile, gbc_lblAprioriResultFile);
		lblAprioriResultFile.setFont(defFont);

		txtAprioriResultFile = new JTextField();
		GridBagConstraints gbc_txtAprioriResultFile = new GridBagConstraints();
		gbc_txtAprioriResultFile.insets = new Insets(0, 0, 5, 5);
		gbc_txtAprioriResultFile.fill = GridBagConstraints.BOTH;
		gbc_txtAprioriResultFile.gridx = 1;
		gbc_txtAprioriResultFile.gridy = 1;
		pnlApriori.add(txtAprioriResultFile, gbc_txtAprioriResultFile);
		txtAprioriResultFile.setText("apriori_output_file.txt");
		txtAprioriResultFile.setForeground(foreGr);
		txtAprioriResultFile.setFont(smallerFont);
		txtAprioriResultFile.setBorder(new TextBubbleBorder(btnBorderColor, 1, 4, 0));
		txtAprioriResultFile.setColumns(10);
		miningPanel.setVisible(false);

		lblDBLoading = new JLabel(" Loading Tweets from DB");
		lblDBLoading.setVerticalAlignment(SwingConstants.TOP);
		lblDBLoading.setFont(defFont);
		lblDBLoading.setIcon(new ImageIcon(App.class.getResource("/Imgs/loading.gif")));
		dbPanel.add(lblDBLoading, BorderLayout.NORTH);
		// comment here
		frmWebContentMining.getContentPane().add(miningPanel, gbc_miningPanel);

		pnlFPG = new JPanel();
		pnlFPG.setBackground(Color.WHITE);
		pnlFPG.setBorder(new LineBorder(new Color(204, 204, 204)));
		pnlFPG.setBorder(new CompoundBorder(pnlFPG.getBorder(), new EmptyBorder(6, 6, 6, 6)));
		GridBagConstraints gbc_pnlFPG = new GridBagConstraints();
		gbc_pnlFPG.gridwidth = 8;
		gbc_pnlFPG.gridheight = 3;
		gbc_pnlFPG.insets = new Insets(0, 0, 5, 5);
		gbc_pnlFPG.fill = GridBagConstraints.BOTH;
		gbc_pnlFPG.gridx = 1;
		gbc_pnlFPG.gridy = 1;
		miningPanel.add(pnlFPG, gbc_pnlFPG);
		GridBagLayout gbl_pnlFPG = new GridBagLayout();
		gbl_pnlFPG.columnWidths = new int[] { 0, 151, 0 };
		gbl_pnlFPG.rowHeights = new int[] { 30, 30, 30, 0 };
		gbl_pnlFPG.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_pnlFPG.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		pnlFPG.setLayout(gbl_pnlFPG);

		lblMinSupFP = new JLabel("Min Support: ");
		lblMinSupFP.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblMinSupFP.setForeground(foreGr);
		GridBagConstraints gbc_lblMinSupFP = new GridBagConstraints();
		gbc_lblMinSupFP.fill = GridBagConstraints.VERTICAL;
		gbc_lblMinSupFP.insets = new Insets(0, 0, 5, 5);
		gbc_lblMinSupFP.anchor = GridBagConstraints.WEST;
		gbc_lblMinSupFP.gridx = 0;
		gbc_lblMinSupFP.gridy = 0;
		pnlFPG.add(lblMinSupFP, gbc_lblMinSupFP);

		txtFPMinSup = new JTextField();
		txtFPMinSup.setText("0.4");
		txtFPMinSup.setForeground(new Color(51, 51, 51));
		txtFPMinSup.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtFPMinSup.setColumns(10);
		txtFPMinSup.setBorder(new TextBubbleBorder(btnBorderColor, 1, 4, 0));
		GridBagConstraints gbc_txtFPMinSup = new GridBagConstraints();
		gbc_txtFPMinSup.insets = new Insets(0, 0, 5, 0);
		gbc_txtFPMinSup.fill = GridBagConstraints.BOTH;
		gbc_txtFPMinSup.gridx = 1;
		gbc_txtFPMinSup.gridy = 0;
		pnlFPG.add(txtFPMinSup, gbc_txtFPMinSup);

		lblFPMinConf = new JLabel("Min Confidence: ");
		lblFPMinConf.setForeground(new Color(51, 51, 51));
		lblFPMinConf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_lblFPMinConf = new GridBagConstraints();
		gbc_lblFPMinConf.fill = GridBagConstraints.VERTICAL;
		gbc_lblFPMinConf.anchor = GridBagConstraints.WEST;
		gbc_lblFPMinConf.insets = new Insets(0, 0, 5, 5);
		gbc_lblFPMinConf.gridx = 0;
		gbc_lblFPMinConf.gridy = 1;
		pnlFPG.add(lblFPMinConf, gbc_lblFPMinConf);

		txtFPMinConf = new JTextField();
		txtFPMinConf.setText("0.6");
		txtFPMinConf.setForeground(new Color(51, 51, 51));
		txtFPMinConf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtFPMinConf.setColumns(10);
		txtFPMinConf.setBorder(new TextBubbleBorder(btnBorderColor, 1, 4, 0));
		GridBagConstraints gbc_txtFPMinConf = new GridBagConstraints();
		gbc_txtFPMinConf.insets = new Insets(0, 0, 5, 0);
		gbc_txtFPMinConf.fill = GridBagConstraints.BOTH;
		gbc_txtFPMinConf.gridx = 1;
		gbc_txtFPMinConf.gridy = 1;
		pnlFPG.add(txtFPMinConf, gbc_txtFPMinConf);

		lblFpGrowthResult = new JLabel("FP Growth Result File: ");
		lblFpGrowthResult.setForeground(new Color(51, 51, 51));
		lblFpGrowthResult.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_lblFpGrowthResult = new GridBagConstraints();
		gbc_lblFpGrowthResult.fill = GridBagConstraints.VERTICAL;
		gbc_lblFpGrowthResult.anchor = GridBagConstraints.EAST;
		gbc_lblFpGrowthResult.insets = new Insets(0, 0, 0, 5);
		gbc_lblFpGrowthResult.gridx = 0;
		gbc_lblFpGrowthResult.gridy = 2;
		pnlFPG.add(lblFpGrowthResult, gbc_lblFpGrowthResult);

		txtFPGResultFile = new JTextField();
		txtFPGResultFile.setText("fp_growth_output_file.txt");
		txtFPGResultFile.setForeground(new Color(51, 51, 51));
		txtFPGResultFile.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtFPGResultFile.setColumns(10);
		txtFPGResultFile.setBorder(new TextBubbleBorder(btnBorderColor, 1, 4, 0));
		GridBagConstraints gbc_txtFPGResultFile = new GridBagConstraints();
		gbc_txtFPGResultFile.fill = GridBagConstraints.BOTH;
		gbc_txtFPGResultFile.gridx = 1;
		gbc_txtFPGResultFile.gridy = 2;
		pnlFPG.add(txtFPGResultFile, gbc_txtFPGResultFile);

		BSButton btnStartMining = new BSButton("Start Mining");
		btnStartMining.setText(" Start Mining ");
		btnStartMining.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					startTheMiningThings();
				} catch (IOException e1) {
					lblAprioryProcessing.setIcon(new ImageIcon(App.class.getResource("/Imgs/NO.png")));
					lblAprioryProcessing.setVisible(true);
					Alert("Error!", e1.getMessage());
					e1.printStackTrace();
				}
			}
		});

		btnStartMining.setPressedBackgroundColor(new Color(212, 212, 212));
		btnStartMining.setHoverBackgroundColor(new Color(230, 230, 230));
		btnStartMining.setForeground(new Color(51, 51, 51));
		btnStartMining.setFont(defFont);
		btnStartMining.setFocusPainted(false);
		btnStartMining.setBorder(new LineBorder(btnBorderColor));
		btnStartMining.setBackground(white);
		GridBagConstraints gbc_btnStartMining = new GridBagConstraints();
		gbc_btnStartMining.insets = new Insets(0, 0, 5, 5);
		gbc_btnStartMining.fill = GridBagConstraints.BOTH;
		gbc_btnStartMining.gridx = 1;
		gbc_btnStartMining.gridy = 4;
		miningPanel.add(btnStartMining, gbc_btnStartMining);

		lblAprioryProcessing = new JLabel("");
		lblAprioryProcessing.setBackground(Color.WHITE);
		lblAprioryProcessing.setIcon(new ImageIcon(App.class.getResource("/Imgs/loading.gif")));
		lblAprioryProcessing.setVisible(false);
		GridBagConstraints gbc_lblAprioryProcessing = new GridBagConstraints();
		gbc_lblAprioryProcessing.fill = GridBagConstraints.BOTH;
		gbc_lblAprioryProcessing.insets = new Insets(0, 0, 0, 0);
		gbc_lblAprioryProcessing.gridx = 2;
		gbc_lblAprioryProcessing.gridy = 4;
		miningPanel.add(lblAprioryProcessing, gbc_lblAprioryProcessing);

		txtResultsOfMining = new JTextArea();
		txtResultsOfMining.setFont(new Font("Bitstream Cyberbit", Font.PLAIN, 12));
		txtResultsOfMining.setBorder(BorderFactory.createCompoundBorder(txtResultsOfMining.getBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		JScrollPane jspTxtApriory = new JScrollPane(txtResultsOfMining);
		jspTxtApriory.setBorder(BorderFactory.createLineBorder(btnBorderColor));
		txtResultsOfMining.setText("Results...");
		GridBagConstraints gbc_txtResultsApriory = new GridBagConstraints();
		gbc_txtResultsApriory.gridheight = 2;
		gbc_txtResultsApriory.gridwidth = 17;
		gbc_txtResultsApriory.fill = GridBagConstraints.BOTH;
		gbc_txtResultsApriory.gridx = 1;
		gbc_txtResultsApriory.gridy = 6;
		miningPanel.add(jspTxtApriory, gbc_txtResultsApriory);
		// comment here
		frmWebContentMining.getContentPane().add(dbPanel, gbc_dbPanel);
		// comment here
		frmWebContentMining.getContentPane().add(filePanel, gbc_filePanel);

		GridBagLayout gbl_filePanel = new GridBagLayout();
		gbl_filePanel.columnWidths = new int[] { 196, 159, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_filePanel.rowHeights = new int[] { 30, 30, 30, 30, 30, 0, 0 };
		gbl_filePanel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				1.0, Double.MIN_VALUE };
		gbl_filePanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		filePanel.setLayout(gbl_filePanel);

		lblJunkFileName = new JLabel("Junk Tags File Name:");
		lblJunkFileName.setFont(defFont);
		GridBagConstraints gbc_lblJunkFileName = new GridBagConstraints();
		gbc_lblJunkFileName.fill = GridBagConstraints.VERTICAL;
		gbc_lblJunkFileName.anchor = GridBagConstraints.WEST;
		gbc_lblJunkFileName.insets = new Insets(0, 0, 5, 5);
		gbc_lblJunkFileName.gridx = 0;
		gbc_lblJunkFileName.gridy = 0;
		filePanel.add(lblJunkFileName, gbc_lblJunkFileName);

		txtJunk = new JTextField();
		txtJunk.setText("junk_tags.txt");
		txtJunk.setForeground(foreGr);
		txtJunk.setFont(smallerFont);
		txtJunk.setBorder(new TextBubbleBorder(btnBorderColor, 1, 4, 0));
		GridBagConstraints gbc_txtJunk = new GridBagConstraints();
		gbc_txtJunk.fill = GridBagConstraints.BOTH;
		gbc_txtJunk.insets = new Insets(0, 0, 5, 5);
		gbc_txtJunk.gridx = 1;
		gbc_txtJunk.gridy = 0;
		filePanel.add(txtJunk, gbc_txtJunk);
		txtJunk.setColumns(10);

		lblJunkStatus = new JLabel("");
		lblJunkStatus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String icon = ((JLabel) arg0.getSource()).getIcon().toString();
				if (icon.contains("OK")) {
					displayFileContents(FileType.Junk);
				}
			}
		});
		lblJunkStatus.setBackground(Color.WHITE);
		lblJunkStatus.setIcon(new ImageIcon(App.class.getResource("/Imgs/loading.gif")));
		lblJunkStatus.setVisible(false);
		GridBagConstraints gbc_lblJunkStatus = new GridBagConstraints();
		gbc_lblJunkStatus.insets = new Insets(0, 0, 0, 0);
		gbc_lblJunkStatus.gridx = 2;
		gbc_lblJunkStatus.gridy = 0;
		filePanel.add(lblJunkStatus, gbc_lblJunkStatus);

		lblCleanFileName = new JLabel("Clean Sorted Tags File Name:");
		lblCleanFileName.setFont(defFont);
		GridBagConstraints gbc_lblCleanFileName = new GridBagConstraints();
		gbc_lblCleanFileName.fill = GridBagConstraints.VERTICAL;
		gbc_lblCleanFileName.anchor = GridBagConstraints.WEST;
		gbc_lblCleanFileName.insets = new Insets(0, 0, 5, 5);
		gbc_lblCleanFileName.gridx = 0;
		gbc_lblCleanFileName.gridy = 1;
		filePanel.add(lblCleanFileName, gbc_lblCleanFileName);

		txtCleanSortedTagsName = new JTextField();
		txtCleanSortedTagsName.setText("clean_sorted_tags.txt");
		txtCleanSortedTagsName.setForeground(foreGr);
		txtCleanSortedTagsName.setFont(smallerFont);
		txtCleanSortedTagsName.setBorder(new TextBubbleBorder(btnBorderColor, 1, 4, 0));
		GridBagConstraints gbc_txtCleanSortedTagsName = new GridBagConstraints();
		gbc_txtCleanSortedTagsName.fill = GridBagConstraints.BOTH;
		gbc_txtCleanSortedTagsName.insets = new Insets(0, 0, 5, 5);
		gbc_txtCleanSortedTagsName.gridx = 1;
		gbc_txtCleanSortedTagsName.gridy = 1;
		filePanel.add(txtCleanSortedTagsName, gbc_txtCleanSortedTagsName);
		txtCleanSortedTagsName.setColumns(10);

		lblCleanSortedStatus = new JLabel("");
		lblCleanSortedStatus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String icon = ((JLabel) arg0.getSource()).getIcon().toString();
				if (icon.contains("OK")) {
					displayFileContents(FileType.CleanSorted);
				}
			}
		});
		lblCleanSortedStatus.setBackground(Color.WHITE);
		lblCleanSortedStatus.setIcon(new ImageIcon(App.class.getResource("/Imgs/loading.gif")));
		lblCleanSortedStatus.setVisible(false);
		GridBagConstraints gbc_lblCleanSortedStatus = new GridBagConstraints();
		gbc_lblCleanSortedStatus.insets = new Insets(0, 0, 0, 0);
		gbc_lblCleanSortedStatus.gridx = 2;
		gbc_lblCleanSortedStatus.gridy = 1;
		filePanel.add(lblCleanSortedStatus, gbc_lblCleanSortedStatus);

		lblCelanRowsFileName = new JLabel("Clean Rows File Name:");
		lblCelanRowsFileName.setFont(defFont);
		GridBagConstraints gbc_lblCelanRowsFileName = new GridBagConstraints();
		gbc_lblCelanRowsFileName.fill = GridBagConstraints.VERTICAL;
		gbc_lblCelanRowsFileName.insets = new Insets(0, 0, 5, 5);
		gbc_lblCelanRowsFileName.anchor = GridBagConstraints.WEST;
		gbc_lblCelanRowsFileName.gridx = 0;
		gbc_lblCelanRowsFileName.gridy = 2;
		filePanel.add(lblCelanRowsFileName, gbc_lblCelanRowsFileName);

		txtCleanRowsFileName = new JTextField();
		txtCleanRowsFileName.setText("clean_rows.txt");
		txtCleanRowsFileName.setForeground(foreGr);
		txtCleanRowsFileName.setFont(smallerFont);
		txtCleanRowsFileName.setBorder(new TextBubbleBorder(btnBorderColor, 1, 4, 0));
		GridBagConstraints gbc_txtCleanRowsFileName = new GridBagConstraints();
		gbc_txtCleanRowsFileName.fill = GridBagConstraints.BOTH;
		gbc_txtCleanRowsFileName.insets = new Insets(0, 0, 5, 5);
		gbc_txtCleanRowsFileName.gridx = 1;
		gbc_txtCleanRowsFileName.gridy = 2;
		filePanel.add(txtCleanRowsFileName, gbc_txtCleanRowsFileName);
		txtCleanRowsFileName.setColumns(10);

		lblCleanRowsStatus = new JLabel("");
		lblCleanRowsStatus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String icon = ((JLabel) e.getSource()).getIcon().toString();
				if (icon.contains("OK")) {
					displayFileContents(FileType.CleanRows);
				}
			}
		});
		lblCleanRowsStatus.setBackground(Color.WHITE);
		lblCleanRowsStatus.setIcon(new ImageIcon(App.class.getResource("/Imgs/loading.gif")));
		lblCleanRowsStatus.setVisible(false);
		GridBagConstraints gbc_lblCleanRowsStatus = new GridBagConstraints();
		gbc_lblCleanRowsStatus.insets = new Insets(0, 0, 0, 0);
		gbc_lblCleanRowsStatus.gridx = 2;
		gbc_lblCleanRowsStatus.gridy = 2;
		filePanel.add(lblCleanRowsStatus, gbc_lblCleanRowsStatus);

		lblSPMFReadyFileName = new JLabel("SPMF Ready File Name:");
		lblSPMFReadyFileName.setFont(defFont);
		GridBagConstraints gbc_lblSPMFReadyFileName = new GridBagConstraints();
		gbc_lblSPMFReadyFileName.insets = new Insets(0, 0, 5, 5);
		gbc_lblSPMFReadyFileName.fill = GridBagConstraints.VERTICAL;
		gbc_lblSPMFReadyFileName.anchor = GridBagConstraints.WEST;
		gbc_lblSPMFReadyFileName.gridx = 0;
		gbc_lblSPMFReadyFileName.gridy = 3;
		filePanel.add(lblSPMFReadyFileName, gbc_lblSPMFReadyFileName);

		txtSPMFFileName = new JTextField();
		txtSPMFFileName.setText("spmf_input_file.txt");
		txtSPMFFileName.setForeground(foreGr);
		txtSPMFFileName.setFont(smallerFont);
		txtSPMFFileName.setBorder(new TextBubbleBorder(btnBorderColor, 1, 4, 0));
		GridBagConstraints gbc_txtSPMFFileName = new GridBagConstraints();
		gbc_txtSPMFFileName.insets = new Insets(0, 0, 5, 5);
		gbc_txtSPMFFileName.fill = GridBagConstraints.BOTH;
		gbc_txtSPMFFileName.gridx = 1;
		gbc_txtSPMFFileName.gridy = 3;
		filePanel.add(txtSPMFFileName, gbc_txtSPMFFileName);
		txtSPMFFileName.setColumns(10);

		lblSMPFFileStatus = new JLabel("");
		lblSMPFFileStatus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String icon = ((JLabel) e.getSource()).getIcon().toString();
				if (icon.contains("OK")) {
					displayFileContents(FileType.SPMFReady);
				}
			}
		});
		lblSMPFFileStatus.setBackground(Color.WHITE);
		lblSMPFFileStatus.setIcon(new ImageIcon(App.class.getResource("/Imgs/loading.gif")));
		lblSMPFFileStatus.setVisible(false);
		GridBagConstraints gbc_lblSMPFFileStatus = new GridBagConstraints();
		gbc_lblSMPFFileStatus.insets = new Insets(0, 0, 0, 0);
		gbc_lblSMPFFileStatus.gridx = 2;
		gbc_lblSMPFFileStatus.gridy = 3;
		filePanel.add(lblSMPFFileStatus, gbc_lblSMPFFileStatus);

		BSButton btnCleanData = new BSButton("Show DB");
		btnCleanData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cleanDataAndSaveToFiles();
			}
		});

		btnCleanData.setText(" Clean Data ");
		btnCleanData.setPressedBackgroundColor(new Color(212, 212, 212));
		btnCleanData.setHoverBackgroundColor(new Color(230, 230, 230));
		btnCleanData.setForeground(new Color(51, 51, 51));
		btnCleanData.setFont(defFont);
		btnCleanData.setFocusPainted(false);
		btnCleanData.setBorder(new LineBorder(btnBorderColor));
		btnCleanData.setBackground(white);

		GridBagConstraints gbc_btnCleanData = new GridBagConstraints();
		gbc_btnCleanData.anchor = GridBagConstraints.WEST;
		gbc_btnCleanData.fill = GridBagConstraints.VERTICAL;
		gbc_btnCleanData.insets = new Insets(0, 0, 5, 5);
		gbc_btnCleanData.gridx = 1;
		gbc_btnCleanData.gridy = 4;
		filePanel.add(btnCleanData, gbc_btnCleanData);

		fileContentList = new JList();
		fileContentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fileContentList.setForeground(foreGr);
		fileContentList.setFont(new Font("Bitstream Cyberbit", Font.PLAIN, 12));
		JScrollPane jspFCList = new JScrollPane(fileContentList);
		jspFCList.setBorder(BorderFactory.createLineBorder(btnBorderColor));
		GridBagConstraints gbc_fileContentList = new GridBagConstraints();
		gbc_fileContentList.gridwidth = 13;
		gbc_fileContentList.insets = new Insets(0, 0, 0, 5);
		gbc_fileContentList.fill = GridBagConstraints.BOTH;
		gbc_fileContentList.gridx = 1;
		gbc_fileContentList.gridy = 5;
		filePanel.add(jspFCList, gbc_fileContentList);

	}

	protected void startTheMiningThings() throws IOException {
		lblAprioryProcessing.setIcon(new ImageIcon(App.class.getResource("/Imgs/loading.gif")));
		lblAprioryProcessing.setVisible(true);
		txtResultsOfMining.setText("");

		SwingWorker<Integer, Void> worker = new SwingWorker<Integer, Void>() {
			@Override
			public Integer doInBackground() throws IOException {
				if (rdbtnApriori.isSelected()) {
					// Apply Apriori
					String output = txtAprioriResultFile.getText().trim();
					if (output.isEmpty()) {
						Alert("Warning", "Please enter a result file name");
						return 3;
					}

					String input = inputSPMFFile;
					if (input.isEmpty()) {
						Alert("Warning", "Create SPMF file first!");
						return 4;
					}
					File inputFile = new File(input);
					if (!inputFile.exists()) {
						Alert("Error!", input + " file does not exists!");
						return 5;
					}

					input = java.net.URLDecoder.decode(inputFile.getAbsolutePath(), "UTF-8");

					String minSup = txtAprioriMinSup.getText().trim();
					if (minSup.isEmpty()) {
						Alert("Warning", "Please enter a value for min support");
						return 6;
					}

					double minsup = Double.parseDouble(minSup);
					if (minsup < 0 || minsup > 1) {
						Alert("Warning", "Min support value must be in [0,1] interval");
						return 7;
					}

					List<String> lines = Files.readAllLines(Paths.get(input), StandardCharsets.UTF_8);
					int recordCount = lines.size();

					AlgoApriori apriori = new AlgoApriori();
					apriori.runAlgorithm(minsup, input, output);
					txtResultsOfMining.setText("");
					apriori.printStats(txtResultsOfMining);

					ArrayList<ArrayList<AprioryRecord>> levels = FileManager.restoreFileBySPMFAprioryResults(output,
							uniqueHashTagsWithIdsReversed);
					
					if(levels.get(0).size() == 0) {
						txtResultsOfMining.append("\nNo result found... Retry with smaller min support value.");
					}

					int lineNo = 1;
					int levelLength = 0;
					for (ArrayList<AprioryRecord> level : levels) {
						if (level.get(0).getTags().size() > levelLength) {
							levelLength = level.get(0).getTags().size();
							txtResultsOfMining.append("\n");
							txtResultsOfMining.append(levelLength + " - ITEMSETS:");
							txtResultsOfMining.append("\n");
						}
						for (AprioryRecord record : level) {
							txtResultsOfMining
									.append(String.format("% 5d", lineNo) + ": " + record.toString(recordCount));
							txtResultsOfMining.append("\n");
							lineNo++;
						}
					}
				} else if (rdbtnFpGrowth.isSelected()) {
					// Apply FP Growth
					String output = txtFPGResultFile.getText().trim();
					if (output.isEmpty()) {
						Alert("Warning", "Please enter a result file name");
						return 3;
					}

					String input = inputSPMFFile;
					if (input.isEmpty()) {
						Alert("Warning", "Create SPMF file first!");
						return 4;
					}
					File inputFile = new File(input);
					if (!inputFile.exists()) {
						Alert("Error!", input + " file does not exists!");
						return 5;
					}

					input = java.net.URLDecoder.decode(inputFile.getAbsolutePath(), "UTF-8");

					String minSup = txtFPMinSup.getText().trim();
					if (minSup.isEmpty()) {
						Alert("Warning", "Please enter a value for min support");
						return 6;
					}

					double minsup = Double.parseDouble(minSup);
					if (minsup < 0 || minsup > 1) {
						Alert("Warning", "Min support value must be in [0,1] interval");
						return 7;
					}

					String minConf = txtFPMinConf.getText().trim();
					if (minConf.isEmpty()) {
						Alert("Warning", "Please enter a value for min support");
						return 6;
					}

					double minconf = Double.parseDouble(minConf);
					if (minconf < 0 || minconf > 1) {
						Alert("Warning", "Min confidence value must be in [0,1] interval");
						return 7;
					}

					AlgoFPGrowth fpgrowth = new AlgoFPGrowth();
					Itemsets patterns = fpgrowth.runAlgorithm(input, null, minsup);
					fpgrowth.printStats(txtResultsOfMining);
					txtResultsOfMining.append("\n");
					int databaseSize = fpgrowth.getDatabaseSize();

					AlgoAgrawalFaster94 algoAgrawal = new AlgoAgrawalFaster94();
					algoAgrawal.runAlgorithm(patterns, output, databaseSize, minconf);
					algoAgrawal.printStats(txtResultsOfMining);

					List<String> lines = Files.readAllLines(Paths.get(input), StandardCharsets.UTF_8);
					int recordCount = lines.size();

					ArrayList<ArrayList<AssociationRecord>> levels = FileManager.restoreFileBySPMFFPGAssocResults(output,
							uniqueHashTagsWithIdsReversed);
					
					txtResultsOfMining.append("\n");
					
					if(levels.get(0).size() == 0) {
						txtResultsOfMining.append("\nNo association rules found... Retry with smaller parameters.");
					}

					int lineNo = 1;
					int levelLength = 0;
					for (ArrayList<AssociationRecord> level : levels) {
						if (level.get(0).getAllTags().size() > levelLength) {
							levelLength = level.get(0).getAllTags().size();
							txtResultsOfMining.append("\n");
							txtResultsOfMining.append(levelLength + " - ASSOCIATION RULES:");
							txtResultsOfMining.append("\n");
						}
						for (AssociationRecord record : level) {
							txtResultsOfMining
									.append(String.format("% 5d", lineNo) + ": " + record.toString(recordCount));
							txtResultsOfMining.append("\n");
							lineNo++;
						}
					}
				}

				return 1;
			}

			@Override
			public void done() {
				lblAprioryProcessing.setIcon(new ImageIcon(App.class.getResource("/Imgs/OK.png")));
			}
		};

		worker.execute();
	}

	protected void displayFileContents(FileType fileType) {
		SwingWorker<Integer, Void> worker = new SwingWorker<Integer, Void>() {
			@SuppressWarnings("unchecked")
			@Override
			public Integer doInBackground() {
				if (!fileCanBeShown) {
					Alert("Please Wait!", "Still displaying the previous file...");
					return 2;
				}
				fileCanBeShown = false;

				String fileToBeRead = "";

				fileContentList.removeAll();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e2) {
				}

				switch (fileType) {
				case Junk:
					fileToBeRead = junkFile;
					break;
				case CleanSorted:
					fileToBeRead = cleanSortedTagsFile;
					break;
				case CleanRows:
					fileToBeRead = cleanRowsFile;
					break;
				case SPMFReady:
					fileToBeRead = inputSPMFFile;
					break;
				default:
					break;
				}

				if (fileToBeRead.isEmpty()) {
					Alert("Warning", "Please create files first!");
					return 4;
				}

				Charset charset = Charset.forName("UTF-8");
				BufferedReader br = null;
				try {
					br = new BufferedReader(new InputStreamReader(new FileInputStream(fileToBeRead), charset));
				} catch (FileNotFoundException e1) {
					Alert("Error!", "BR - FR: " + e1.getMessage());
				}

				try {
					List<String> lines = Files.readAllLines(Paths.get(fileToBeRead), StandardCharsets.UTF_8);
					DefaultListModel<String> listModel = new DefaultListModel<>();
					int lineNo = 1;
					for (String line : lines) {
						line = String.format("% 5d", lineNo) + ": " + line;
						listModel.addElement(line);
						lineNo++;
					}
					fileContentList.setModel(listModel);
				} catch (IOException e) {
					Alert("Error!", "BR Read: " + e.getMessage());
				}
				try {
					br.close();
				} catch (IOException e) {
					Alert("Error!", "BR Close: " + e.getMessage());
				}

				return 1;
			}

			@Override
			public void done() {
				fileCanBeShown = true;
			}
		};

		worker.execute();
	}

	protected void cleanDataAndSaveToFiles() {
		cleanSortedTagsFile = txtCleanSortedTagsName.getText().trim();
		junkFile = txtJunk.getText().trim();
		cleanRowsFile = txtCleanRowsFileName.getText().trim();
		inputSPMFFile = txtSPMFFileName.getText().trim();
		MySQLMgr mySQLMgr = new MySQLMgr();

		lblCleanRowsStatus.setIcon(new ImageIcon(App.class.getResource("/Imgs/loading.gif")));
		lblCleanSortedStatus.setIcon(new ImageIcon(App.class.getResource("/Imgs/loading.gif")));
		lblJunkStatus.setIcon(new ImageIcon(App.class.getResource("/Imgs/loading.gif")));

		lblCleanRowsStatus.setVisible(true);
		lblCleanSortedStatus.setVisible(true);
		lblJunkStatus.setVisible(true);

		workerOneSucceeded = true;
		workerTwoSucceeded = true;

		SwingWorker<Integer, Void> workerOne = new SwingWorker<Integer, Void>() {
			@Override
			public Integer doInBackground() {

				Connection conn = null;
				cleanRows = new ArrayList<ArrayList<String>>();
				uniqueHashTagsWithIds = new HashMap<String, String>();
				try {
					conn = mySQLMgr.connectToDB();
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
					Alert("Error!", "Worker 1 DB Connection: " + e.getMessage());
					workerOneSucceeded = false;
					// e.printStackTrace();
				}

				try {
					cleanRows = FileManager.cleanTags(conn, "SELECT * FROM tweets", cleanSortedTagsFile, cleanRowsFile,
							junkFile);
				} catch (Exception e) {
					Alert("Error!", "Worker 1 Tag Cleaner: " + e.toString());
					workerOneSucceeded = false;
					// e.printStackTrace();
				}

				try {
					ArrayList<HashMap<String, String>> results = FileManager.getFormattedList(cleanSortedTagsFile);
					uniqueHashTagsWithIds = results.get(0);
					uniqueHashTagsWithIdsReversed = results.get(1);
				} catch (IOException e1) {
					Alert("Error!", "Worker 1 Getting Formatted List: " + e1.getMessage());
					workerOneSucceeded = false;
					// e.printStackTrace();
				}

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					Alert("Error!", "Worker 1 Sleeper: " + e.getMessage());
					workerOneSucceeded = false;
					// e.printStackTrace();
				}

				return 1;
			}

			@Override
			public void done() {
				if (workerOneSucceeded) {
					lblCleanRowsStatus.setIcon(new ImageIcon(App.class.getResource("/Imgs/OK.png")));
					lblCleanSortedStatus.setIcon(new ImageIcon(App.class.getResource("/Imgs/OK.png")));
					lblJunkStatus.setIcon(new ImageIcon(App.class.getResource("/Imgs/OK.png")));
				} else {
					lblCleanRowsStatus.setIcon(new ImageIcon(App.class.getResource("/Imgs/NO.png")));
					lblCleanSortedStatus.setIcon(new ImageIcon(App.class.getResource("/Imgs/NO.png")));
					lblJunkStatus.setIcon(new ImageIcon(App.class.getResource("/Imgs/NO.png")));
				}

				lblSMPFFileStatus.setIcon(new ImageIcon(App.class.getResource("/Imgs/loading.gif")));
				lblSMPFFileStatus.setVisible(true);

				SwingWorker<Integer, Void> workerTwo = new SwingWorker<Integer, Void>() {
					@Override
					public Integer doInBackground() {

						try {
							FileManager.buildPreparedDataFile(uniqueHashTagsWithIds, cleanRows, inputSPMFFile);
						} catch (Exception e) {
							Alert("Error!", "Worker 2 SPMF File: " + e.toString());
							workerTwoSucceeded = false;
							// e.printStackTrace();
						}

						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							Alert("Error!", "Worker 2 Sleeper: " + e.getMessage());
							workerTwoSucceeded = false;
							// e.printStackTrace();
						}
						return 1;
					}

					@Override
					public void done() {
						if (workerTwoSucceeded) {
							lblSMPFFileStatus.setIcon(new ImageIcon(App.class.getResource("/Imgs/OK.png")));
							btnMine.setEnabled(true);
							mySQLMgr.closeConnection();
						} else {
							lblSMPFFileStatus.setIcon(new ImageIcon(App.class.getResource("/Imgs/NO.png")));
						}
					}
				};

				workerTwo.execute();

			}
		};

		workerOne.execute();

	}

	protected void showScreen(ScreenType screen) {
		switch (screen) {
		case DBScreen:
			dbPanel.setVisible(true);
			filePanel.setVisible(false);
			miningPanel.setVisible(false);

			/*
			 * lblSMPFFileStatus.setVisible(false);
			 * lblCleanRowsStatus.setVisible(false);
			 * lblCleanSortedStatus.setVisible(false);
			 * lblJunkStatus.setVisible(false);
			 */
			break;
		case FileScreen:
			dbPanel.setVisible(false);
			filePanel.setVisible(true);
			miningPanel.setVisible(false);
			break;
		case MiningScreen:
			dbPanel.setVisible(false);
			filePanel.setVisible(false);
			miningPanel.setVisible(true);
			break;
		default:
			break;
		}
	}

	protected void showDB() {

		SwingWorker<Integer, Void> worker = new SwingWorker<Integer, Void>() {
			@Override
			public Integer doInBackground() {
				ArrayList<String> columnNames = new ArrayList<>();
				ArrayList<Object> data = new ArrayList<>();

				try {
					MySQLMgr mySQLMgr = new MySQLMgr();
					Connection conn = mySQLMgr.connectToDB();

					Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery("SELECT * FROM tweets");

					ResultSetMetaData md = rs.getMetaData();
					int columns = md.getColumnCount();

					for (int i = 1; i <= columns; i++) {
						columnNames.add(md.getColumnName(i));
					}

					while (rs.next()) {
						ArrayList<Object> row = new ArrayList<>(columns);
						for (int i = 1; i <= columns; i++) {
							row.add(rs.getObject(i));
						}
						data.add(row);
					}

					st.close();
					rs.close();
					mySQLMgr.closeConnection();

					Vector<String> columnNamesVector = new Vector<>();
					Vector<Vector<Object>> dataVector = new Vector<Vector<Object>>();

					for (int i = 0; i < data.size(); i++) {
						Object obj = data.get(i);
						if (obj instanceof ArrayList<?>) {
							@SuppressWarnings("unchecked")
							ArrayList<Object> subArray = (ArrayList<Object>) obj;
							Vector<Object> subVector = new Vector<>();
							for (int j = 0; j < subArray.size(); j++) {
								if (j == 0) {
									subVector.add(i + 1);
								} else {
									subVector.add(subArray.get(j));
								}
							}
							dataVector.add(subVector);
						}

					}

					for (int i = 0; i < columnNames.size(); i++) {
						columnNamesVector.add(columnNames.get(i));
					}

					JTable dataTable = new JTable(dataVector, columnNamesVector);
					DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
					centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
					for (int x = 0; x < dataTable.getColumnCount(); x++) {
						dataTable.getColumnModel().getColumn(x).setCellRenderer(centerRenderer);
					}
					dataTable.getTableHeader().setBackground(white);
					dataTable.setBackground(white);
					dataTable.setForeground(foreGr);
					dataTable.setRowHeight(24);
					dataTable.setFont(defFont);
					dataTable.setGridColor(btnBorderColor);
					dataTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

					int smallestWidth = 100;

					dataTable.getColumnModel().getColumn(0).setMaxWidth(50);
					dataTable.getColumnModel().getColumn(0).setMinWidth(50);

					dataTable.getColumnModel().getColumn(1).setPreferredWidth(170);
					dataTable.getColumnModel().getColumn(1).setMaxWidth(170);
					dataTable.getColumnModel().getColumn(1).setMinWidth(170);

					dataTable.getColumnModel().getColumn(2).setPreferredWidth(170);
					dataTable.getColumnModel().getColumn(2).setMaxWidth(170);
					dataTable.getColumnModel().getColumn(2).setMinWidth(170);

					dataTable.getColumnModel().getColumn(5).setPreferredWidth(smallestWidth - 10);
					dataTable.getColumnModel().getColumn(5).setMaxWidth(smallestWidth - 10);
					dataTable.getColumnModel().getColumn(5).setMinWidth(smallestWidth - 10);

					dataTable.getColumnModel().getColumn(6).setPreferredWidth(smallestWidth);
					dataTable.getColumnModel().getColumn(6).setMaxWidth(smallestWidth);
					dataTable.getColumnModel().getColumn(6).setMinWidth(smallestWidth);

					dataTable.getColumnModel().getColumn(7).setMaxWidth(50);
					dataTable.getColumnModel().getColumn(7).setMinWidth(50);

					dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

					scrollPane = new JScrollPane(dataTable);
					scrollPane.setBorder(BorderFactory.createLineBorder(btnBorderColor));

				} catch (Exception ex) {
					Alert("Error!", ex.getMessage());
					System.out.println(ex.getStackTrace());
				}
				return 1;
			}

			@Override
			public void done() {
				dbPanel.removeAll();

				dbPanel.add(scrollPane, BorderLayout.CENTER);

				dbPanel.revalidate();
				dbPanel.repaint();
				frmWebContentMining.revalidate();
			}
		};

		worker.execute();

	}

	private void Alert(String title, String message) {
		JOptionPane.showOptionDialog(null, message, title, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
				new Object[] {}, null);
	}
}