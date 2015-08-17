/*
 * Main.java
 *
 * Created on Feb 5, 2009, 2:10:51 PM
 */

import java.awt.*;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.table.*;

import com.jcraft.jsch.*;

/**
 *
 * @author jzalumsky
 */
public class Main extends javax.swing.JFrame {

	private static final long serialVersionUID = -1261515838947114727L;

	// UI Variables declarations
	private javax.swing.JButton jButton1; // Add
	private javax.swing.JButton jButton2; // Delete
	private javax.swing.JButton jButton3; // Up
	private javax.swing.JButton jButton4; // Down
	private javax.swing.JButton jButton5; // Upload
	private javax.swing.JButton jButton6; // Exit
	private javax.swing.JButton jButton7; // New
	private javax.swing.JButton jButton8; // Open
	private javax.swing.JButton jButton9; // Save
	private javax.swing.JButton jButton10; // Import
	private javax.swing.JLabel jLabel1; // Current Slide Show is :
	private javax.swing.JMenu jMenu1; // File
	private javax.swing.JMenu jMenu2; // Help
	private javax.swing.JMenu jMenu3; // Import
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JMenuItem jMenuItem1; // About
	private javax.swing.JMenuItem jMenuItem2; // New
	private javax.swing.JMenuItem jMenuItem3; // Open
	private javax.swing.JMenuItem jMenuItem4; // Save
	private javax.swing.JMenuItem jMenuItem6; // Upload
	private javax.swing.JMenuItem jMenuItem7; // Exit
	private javax.swing.JMenuItem jMenuItem8; // Import PNGs
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTable jTable1;
	// End of UI variables declarations

	private String tableHeadings[] = { "File Name", "Thumbnail", "Delay" };
	private Object tableObjects[][] = null;
	private TableCellRenderer renderer = new JComponentTableCellRenderer();
	private TableColumnModel columnModel;
	private TableColumn column1;
	private String slideShowName = null;
	private String slideShowPath = null;
	private boolean dirty = false;
	private String showChooserPath = ".";
	private String pictureChooserPath = ".";
	boolean time;
	private int nextNumber = 0;

	protected int minSelectedRow;

	protected int maxSelectedRow;

	private static FileInputStream fileInputStream;

	private static FileOutputStream fileOutputStream;

	/** Creates new form Main */
	public Main() {
		initComponents();
		jTable1.setModel(new DefaultTableModel(tableObjects, tableHeadings));
		columnModel = jTable1.getColumnModel();
		column1 = columnModel.getColumn(1);
		column1.setCellRenderer(renderer);
		jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
	}

	public void Exit() {
		System.exit(0);
	}

	private void About() {
		About a = new About();
		a.setVisible(true);
	}

	private void importPngs() throws FileNotFoundException, IOException {
		if (slideShowName == null) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane
					.showMessageDialog(null,
							"Error: You must have a Slide Show Open before importing a files.");
		} // end-if slideShowName == null
		else {
			GetPictPath();
			JFileChooser fileChooser = new JFileChooser(pictureChooserPath);
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int status = fileChooser.showOpenDialog(null);
			if (status == JFileChooser.APPROVE_OPTION) {
				File folder = fileChooser.getSelectedFile();
				if (!folder.exists()) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null,
							"Error: Does Not Exist.");
				} // end-if !folder.exists()
				else {
					try {
						for (final File fileEntry : folder.listFiles()) {
							if (!fileEntry.isDirectory()) {
								String extension = Utils
										.getExtension(fileEntry);
								if (extension.equals(Utils.png)) {
									File dFile = new File(slideShowPath + "/"
											+ getNextName() + "." + extension);
									CopyFile(fileEntry, dFile);
									dirty = true;
									Object[] aRow = new Object[3];

									SetPictPath(fileEntry);
									aRow[0] = (Object) dFile.getName();
									aRow[1] = (new JLabel(
											new ImageIcon(new ImageIcon(
													fileEntry.toString())
													.getImage()
													.getScaledInstance(64, 64,
															Image.SCALE_SMOOTH))));
									if (time) {
										String inputValue = JOptionPane
												.showInputDialog("Delay:", "30");
										aRow[2] = new Integer(inputValue);
									} // end-if time
									else {
										aRow[2] = new Integer(30);
									} // end-if-else time
									DefaultTableModel tableModel = (DefaultTableModel) jTable1
											.getModel();
									tableModel.addRow(aRow);
								} // end-if extension.equals(Utils.png)
							} // end-if !fileEntry.isDirectory()

						} // end-for (final File fileEntry : folder.listFiles()
							// )
					} // end-try
					catch (IOException ex) {
						JOptionPane.showMessageDialog(null, "Error: " + ex);
					} // end-try-catch
				} // end-if-else !folder.exists()
			} // end-if status == JFileChooser.APPROVE_OPTION
		} // end-if-else // end-if slideShowName == null
	} // end-method importPngs

	private void Add() throws FileNotFoundException, IOException {
		if (slideShowName == null) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane
					.showMessageDialog(null,
							"Error: You must have a Slide Show Open before adding a file.");
		} else {
			GetPictPath();
			JFileChooser fileChooser = new JFileChooser(pictureChooserPath);
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.addChoosableFileFilter(new ImageFilter());
			fileChooser.setAccessory(new ImagePreview(fileChooser));
			int status = fileChooser.showOpenDialog(null);
			if (status == JFileChooser.APPROVE_OPTION) {
				try {
					File f = fileChooser.getSelectedFile();
					String extension = Utils.getExtension(f);
					File dFile = new File(slideShowPath + "/" + getNextName()
							+ "." + extension);
					CopyFile(f, dFile);
					dirty = true;
					Object[] aRow = new Object[3];

					SetPictPath(f);
					aRow[0] = (Object) dFile.getName();
					if (extension.equals(Utils.wmv)
							|| extension.equals(Utils.mov)
							|| extension.equals(Utils.avi)
							|| extension.equals(Utils.mpg)
							|| extension.equals(Utils.mpeg)) {
						aRow[1] = (new JLabel(new javax.swing.ImageIcon(
								getClass().getResource("video.png"))));
					} else {
						aRow[1] = (new JLabel(new ImageIcon(new ImageIcon(
								f.toString()).getImage().getScaledInstance(64,
								64, Image.SCALE_SMOOTH))));
					}
					if (time) {
						String inputValue = JOptionPane.showInputDialog(
								"Delay:", "30");
						aRow[2] = new Integer(inputValue);
					} else {
						aRow[2] = new Integer(30);
					}
					DefaultTableModel tableModel = (DefaultTableModel) jTable1
							.getModel();
					tableModel.addRow(aRow);
					// }
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(null, "Error: " + ex);
				}
			}
		}
	} // end-method add

	private String getNextName() {
		String nextNumberString = "";
		nextNumberString = nextNumberString + nextNumber;
		nextNumber += 1;
		while (nextNumberString.length() < 3) {
			nextNumberString = "0" + nextNumberString;
		} // end-while nextNumberString.length() < 3
		return "Slide" + nextNumberString;
	} // end-method getNextName

	public static void CopyFile(File in, File out) throws IOException {
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		try {
			fileInputStream = new FileInputStream(in);
			inChannel = fileInputStream.getChannel();
			fileOutputStream = new FileOutputStream(out);
			outChannel = fileOutputStream.getChannel();
			inChannel.transferTo(0, inChannel.size(), outChannel);
			inChannel.close();
			outChannel.close();
		} catch (IOException e) {
			throw e;
		} finally {
			if (inChannel != null) {
				inChannel.close();
			}
			if (outChannel != null) {
				outChannel.close();
			}
		}
	} // end_method CopyFile

	private void Delete() {
		DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
		if (jTable1.getSelectedRow() == -1) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null,
					"Error: You must select a slide to delete.");
		} else {
			dirty = true;
			int i = jTable1.getSelectedRow();
			String deleteFileName = (String) jTable1.getValueAt(i, 0);
			File df = new File(slideShowPath + "/" + deleteFileName);
			df.delete();
			tableModel.removeRow(i);
		}
	}

	private void Down() {
		DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
		int lastRow = tableModel.getRowCount() - 1;
		int row = jTable1.getSelectedRow();
		if (row == -1) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null,
					"Error: You have to select a slide to move.");
		} else {
			if (row == lastRow) {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(null,
						"Error: You tried to move the last slide down.");
			} else {
				dirty = true;
				tableModel.moveRow(row, row, row + 1);
				jTable1.setRowSelectionInterval(row + 1, row + 1);
			}
		}
	}

	public String ftpIt2() {
		int j = 0;
		Session session = null;
		JSch jsch = new JSch();
		try {
			session = jsch.getSession("root", "APPS.BELTRAILWAY.COM", 22);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword("B1gB1rd!2008");
			session.connect();

			Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp sftpChannel = (ChannelSftp) channel;
			sftpChannel.cd("/var/www/html/btv/current");
			sftpChannel.rm("*.*");

			sftpChannel.lcd(slideShowPath);
			for (int i = 0; i < jTable1.getRowCount(); i++) {
				String src = (String) jTable1.getValueAt(i, 0);
				// System.out.println(src);
				sftpChannel.put(slideShowPath + "/" + src, src);
				j = i + 1;
				sftpChannel.put(slideShowPath + "/Slides/Slide" + j + ".html",
						"Slide" + j + ".html");
			}
			sftpChannel.exit();
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (SftpException e) {
			e.printStackTrace();
		} finally {
			session.disconnect();
		}
		return "Done.";
	} // end-method ftpIt2

	public void MakeHtml(String pathName, String fileName, int delay,
			int slideNumber) {
		int s = slideNumber + 1;
		int n = s + 1;
		String t;

		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(pathName
					+ "/Slides/Slide" + s + ".html"));
			t = "<html>\n";
			bufferedWriter.write(t, 0, t.length());
			t = " <head>\n";
			bufferedWriter.write(t, 0, t.length());
			t = "  <meta http-equiv=\"Pragma\" content=\"no-cache\">\n";
			bufferedWriter.write(t, 0, t.length());
			t = "  <meta http-equiv=\"expires\" content=\"0\">\n";
			bufferedWriter.write(t, 0, t.length());
			t = "  <meta http-equiv=\"refresh\" content=\"";
			t = t + delay;
			t = t + ";url=http://apps.beltrailway.com/btv/current/Slide";
			t = t + n + ".html\">\n";
			bufferedWriter.write(t, 0, t.length());
			t = "  <title>The Belt Railway Company of Chicago</title>\n";
			bufferedWriter.write(t, 0, t.length());
			t = " </head>\n";
			bufferedWriter.write(t, 0, t.length());
			t = " <body  BGCOLOR=\"#000000\">\n";
			bufferedWriter.write(t, 0, t.length());
			t = "  <center>\n";
			bufferedWriter.write(t, 0, t.length());
			String extension = Utils.getExtension(new File(fileName));
			if (extension.equals(Utils.wmv) || extension.equals(Utils.mov)
					|| extension.equals(Utils.avi)
					|| extension.equals(Utils.mpg)
					|| extension.equals(Utils.mpeg)) {
				t = "</br></br></br></br></br></br></br></br>\n";
				bufferedWriter.write(t, 0, t.length());
				t = "   <embed width=\"100%\"  height=\"100%\" src=\""
						+ fileName + "\"></embed>\n";
				t = "   <embed width=\"75%\"  height=\"75%\" src=\"" + fileName
						+ "\"></embed>\n";
				// t = "   <embed width=\"50%\"  height=\"50%\" src=\"" +
				// fileName + "\"></embed>\n";
			} else {
				t = "   <img width=\"100%\" height=\"100%\" src=\"" + fileName
						+ "\">\n";
			}

			bufferedWriter.write(t, 0, t.length());
			t = "  </center>\n";
			bufferedWriter.write(t, 0, t.length());
			t = " </body>\n";
			bufferedWriter.write(t, 0, t.length());
			t = "</html>";
			bufferedWriter.write(t, 0, t.length());
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try { // Close the BufferedWriter
				if (bufferedWriter != null) {
					bufferedWriter.flush();
					bufferedWriter.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	} // end_method MakeHtml

	private void NewProject() {
		if (dirty == true) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null,
					"Error: Unsaved changes where made.");
		} else {
			GetShowPath();
			JFileChooser fileChooser = new JFileChooser(showChooserPath);
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int status = fileChooser.showSaveDialog(null);
			if (status == JFileChooser.APPROVE_OPTION) {
				File f = fileChooser.getSelectedFile();
				if (f.exists()) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null,
							"Error: Already exists.");
				} else {
					SetShowPath(f.getParent());
					@SuppressWarnings("unused")
					boolean results = f.mkdirs();
					File dataFile = new File(f.getPath() + "/data.txt");

					try {
						dataFile.createNewFile();
					} catch (IOException ex) {
						JOptionPane.showMessageDialog(null, "Error: " + ex);
					}
					slideShowName = f.getName();
					slideShowPath = f.toString();
					jLabel1.setText("Current Slide Show is : " + f.getName());
					jTable1.setModel(new DefaultTableModel(tableObjects,
							tableHeadings));
					columnModel = jTable1.getColumnModel();
					column1 = columnModel.getColumn(1);
					column1.setCellRenderer(renderer);
					jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
					jTable1.getColumnModel().getColumn(0).setResizable(false);
					jTable1.getColumnModel().getColumn(0)
							.setPreferredWidth(150);
					jTable1.getColumnModel().getColumn(1).setResizable(false);
					jTable1.getColumnModel().getColumn(1).setPreferredWidth(50);
					jTable1.getColumnModel().getColumn(2).setResizable(false);
					jTable1.getColumnModel().getColumn(2).setPreferredWidth(20);
					nextNumber = 1;
					dirty = false;
				}

			} else {
				Toolkit.getDefaultToolkit().beep();
			}
		}
	}

	private void OpenProject() {
		if (dirty == true) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null,
					"Error: Unsaved changes where made.");
		} else {
			GetShowPath();
			JFileChooser fileChooser = new JFileChooser(showChooserPath);
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int status = fileChooser.showOpenDialog(null);
			if (status == JFileChooser.APPROVE_OPTION) {
				File f = fileChooser.getSelectedFile();
				if (!f.exists()) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null,
							"Error: Does Not Exist.");
				} else {
					dirty = false;
					nextNumber = 1;
					SetShowPath(f.getParent());
					slideShowName = f.getName();
					slideShowPath = f.toString();
					jLabel1.setText("Current Slide Show is : " + f.getName());
					jTable1.setModel(new DefaultTableModel(tableObjects,
							tableHeadings));
					columnModel = jTable1.getColumnModel();
					column1 = columnModel.getColumn(1);
					column1.setCellRenderer(renderer);
					jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
					jTable1.getColumnModel().getColumn(0).setResizable(false);
					jTable1.getColumnModel().getColumn(0)
							.setPreferredWidth(150);
					jTable1.getColumnModel().getColumn(1).setResizable(false);
					jTable1.getColumnModel().getColumn(1).setPreferredWidth(50);
					jTable1.getColumnModel().getColumn(2).setResizable(false);
					jTable1.getColumnModel().getColumn(2).setPreferredWidth(20);
					File dataFile = new File(f.getPath() + "/data.txt");
					if (dataFile.exists()) {
						try {
							BufferedReader in = new BufferedReader(
									new FileReader(dataFile));
							String str;

							DefaultTableModel tableModel = (DefaultTableModel) jTable1
									.getModel();
							while ((str = in.readLine()) != null) {
								int i = str.indexOf('\t');
								String f1 = str.substring(0, i);
								Integer d1 = new Integer(str.substring(i + 1));
								Object aRow[] = new Object[3];
								File f2 = new File(f.toString(), f1);
								aRow[0] = (Object) f1;
								nextNumber = Utils.getFileNumber(f2);
								String extension = Utils.getExtension(f2);
								if (extension.equals(Utils.wmv)
										|| extension.equals(Utils.mov)
										|| extension.equals(Utils.avi)
										|| extension.equals(Utils.mpg)
										|| extension.equals(Utils.mpeg)) {
									aRow[1] = (new JLabel(
											new javax.swing.ImageIcon(
													getClass()
															.getResource(
																	"/slideshow/resources/video.png"))));

								} else {
									aRow[1] = (new JLabel(
											new ImageIcon(new ImageIcon(f2
													.toString()).getImage()
													.getScaledInstance(64, 64,
															Image.SCALE_SMOOTH))));
								}
								aRow[2] = d1;
								tableModel.addRow(aRow);
							}
							in.close();
						} catch (IOException ex) {
							JOptionPane.showMessageDialog(null, "Error: " + ex);
						}
					} else {
						try {
							dataFile.createNewFile();
						} catch (IOException ex) {
							JOptionPane.showMessageDialog(null, "Error: " + ex);
						}
					}

				}
			}
		}
	}

	private void GetPictPath() throws FileNotFoundException, IOException {
		File dataFile = new File("./PictPath.txt");
		if (dataFile.exists()) {
			try {
				BufferedReader pictPathReader = new BufferedReader(
						new FileReader(dataFile));
				pictureChooserPath = pictPathReader.readLine();
				pictPathReader.close();
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(null, "Error: " + ex);
			}
		} else {
			try {
				dataFile.createNewFile();
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(null, "Error: " + ex);
			}
		}

	} // end_method GetPictPath

	private void GetShowPath() {
		File showFile = new File("./ShowPath.txt");
		if (showFile.exists()) {
			try {
				BufferedReader showPathReader = new BufferedReader(
						new FileReader(showFile));
				showChooserPath = showPathReader.readLine();
				showPathReader.close();
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(null, "Error: " + ex);
			}
		} else {
			try {
				showFile.createNewFile();
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(null, "Error: " + ex);
			}
		}

	} // end_method GetShowPath

	private void SaveProject() {
		if (slideShowName == null) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null,
					"Error: You must have a Slide Show Open to Save.");
		} else {
			BufferedWriter bufferedWriter = null;
			try {
				File d = new File(slideShowPath + "/Slides");
				if (!d.exists()) {
					@SuppressWarnings("unused")
					boolean results = d.mkdirs();
				}

				bufferedWriter = new BufferedWriter(new FileWriter(
						slideShowPath + "/data.txt"));
				columnModel = jTable1.getColumnModel();
				for (int i = 0; i < jTable1.getRowCount(); i++) {
					// Start writing to the output stream
					String fileName = (String) jTable1.getValueAt(i, 0);
					int delay = new Integer(jTable1.getValueAt(i, 2).toString())
							.intValue();
					String str = fileName + "\t" + delay;
					bufferedWriter.write(str, 0, str.length());
					bufferedWriter.newLine();
					MakeHtml(slideShowPath, fileName, delay, i);
				}

			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				try { // Close the BufferedWriter
					if (bufferedWriter != null) {
						bufferedWriter.flush();
						bufferedWriter.close();
						dirty = false;
					}

				} catch (IOException ex) {
					ex.printStackTrace();
				}
				JOptionPane.showMessageDialog(null,
						"Your changes have been saved.");
			}
		}
	}

	private void SetPictPath(File f) {
		pictureChooserPath = f.getPath();
		BufferedWriter pictPathWriter = null;
		try {
			pictPathWriter = new BufferedWriter(
					new FileWriter("./PictPath.txt"));
			pictPathWriter.write(pictureChooserPath, 0,
					pictureChooserPath.length());
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try { // Close the BufferedWriter
				if (pictPathWriter != null) {
					pictPathWriter.flush();
					pictPathWriter.close();
				}

			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
	} // end_method SetPictPath

	private void SetShowPath(String f) {
		showChooserPath = f;
		BufferedWriter showPathWriter = null;
		try {
			showPathWriter = new BufferedWriter(
					new FileWriter("./ShowPath.txt"));
			showPathWriter.write(showChooserPath, 0, showChooserPath.length());
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try { // Close the BufferedWriter
				if (showPathWriter != null) {
					showPathWriter.flush();
					showPathWriter.close();
				}

			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
	} // end_method SetShowPath

	private void Up() {
		int row = jTable1.getSelectedRow();
		if (row == -1) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null,
					"Error: You have to select a slide to move.");
		} else {
			if (row == 0) {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(null,
						"Error: You tried to move the 1st slide up.");
			} else {
				dirty = true;
				DefaultTableModel tableModel = (DefaultTableModel) jTable1
						.getModel();
				tableModel.moveRow(row, row, row - 1);
				jTable1.setRowSelectionInterval(row - 1, row - 1);
			}
		}
	}

	private void Upload() {
		if (slideShowName == null) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null,
					"Error: You must have a Slide Show Open to Upload.");
		} else {
			if (dirty == true) {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(null,
						"Error: Unsaved changes where made.");
			} else {
				ftpIt2();
			}
		}
	}

	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		jTable1 = new javax.swing.JTable();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jButton3 = new javax.swing.JButton();
		jButton4 = new javax.swing.JButton();
		jButton5 = new javax.swing.JButton();
		jButton6 = new javax.swing.JButton();
		jButton7 = new javax.swing.JButton();
		jButton8 = new javax.swing.JButton();
		jButton9 = new javax.swing.JButton();
		jButton10 = new javax.swing.JButton();
		jMenuBar1 = new javax.swing.JMenuBar();
		jMenu1 = new javax.swing.JMenu();
		jMenuItem2 = new javax.swing.JMenuItem();
		jMenuItem3 = new javax.swing.JMenuItem();
		jMenuItem4 = new javax.swing.JMenuItem();
		jMenuItem6 = new javax.swing.JMenuItem();
		jMenuItem7 = new javax.swing.JMenuItem();
		jMenu2 = new javax.swing.JMenu();
		jMenuItem1 = new javax.swing.JMenuItem();
		jMenu3 = new javax.swing.JMenu();
		jMenuItem8 = new javax.swing.JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Slide Show Builder");
		ImageIcon appIcon = new ImageIcon("Projector.gif");
		setIconImage(appIcon.getImage());

		jTable1.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] { { null, null, null }, { null, null, null },
						{ null, null, null }, { null, null, null },
						{ null, null, null } }, new String[] { "File Name",
						"Thumbnail", "Delay" }) {
			/**
		 * 
		 */
			private static final long serialVersionUID = -7346758311993642709L;
			boolean[] canEdit = new boolean[] { false, false, true };

			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Class getColumnClass(int columnIndex) {
				return getValueAt(0, columnIndex).getClass();
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		jTable1.setRowHeight(75);
		jTable1.getTableHeader().setReorderingAllowed(false);
		jScrollPane1.setViewportView(jTable1);
		jTable1.getColumnModel().getColumn(0).setResizable(false);
		jTable1.getColumnModel().getColumn(1).setResizable(false);
		jTable1.getColumnModel().getColumn(2).setResizable(false);

		jButton1.setText("Add");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		jButton2.setText("Delete");
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});

		jButton3.setText("Up");
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton3ActionPerformed(evt);
			}
		});

		jButton4.setText("Down");
		jButton4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton4ActionPerformed(evt);
			}
		});

		jButton5.setText("Upload");
		jButton5.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton5ActionPerformed(evt);
			}
		});

		jButton6.setText("Exit");
		jButton6.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton6ActionPerformed(evt);
			}
		});

		jButton7.setText("New");
		jButton7.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton7ActionPerformed(evt);
			}
		});

		jButton8.setText("Open");
		jButton8.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton8ActionPerformed(evt);
			}
		});

		jButton9.setText("Save");
		jButton9.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton9ActionPerformed(evt);
			}
		});

		jButton10.setText("Import");
		jButton10.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				{
					try {
						importPngs();
					} catch (FileNotFoundException ex) {
						Logger.getLogger(Main.class.getName()).log(
								Level.SEVERE, null, ex);
					} catch (IOException ex) {
						Logger.getLogger(Main.class.getName()).log(
								Level.SEVERE, null, ex);
					}
				}
			}
		});

		jMenu1.setText("File");

		jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_N,
				java.awt.event.InputEvent.CTRL_MASK));
		jMenuItem2.setText("New");
		jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem2ActionPerformed(evt);
			}
		});
		jMenu1.add(jMenuItem2);

		jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_O,
				java.awt.event.InputEvent.CTRL_MASK));
		jMenuItem3.setText("Open");
		jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem3ActionPerformed(evt);
			}
		});
		jMenu1.add(jMenuItem3);

		jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_S,
				java.awt.event.InputEvent.CTRL_MASK));
		jMenuItem4.setText("Save");
		jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem4ActionPerformed(evt);
			}
		});
		jMenu1.add(jMenuItem4);

		jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_U,
				java.awt.event.InputEvent.CTRL_MASK));
		jMenuItem6.setText("Upload");
		jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem6ActionPerformed(evt);
			}
		});
		jMenu1.add(jMenuItem6);

		jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_X,
				java.awt.event.InputEvent.CTRL_MASK));
		jMenuItem7.setText("Exit");
		jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem7ActionPerformed(evt);
			}
		});
		jMenu1.add(jMenuItem7);

		jMenuBar1.add(jMenu1);

		jMenu2.setText("Help");

		jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_H,
				java.awt.event.InputEvent.CTRL_MASK));
		jMenuItem1.setText("About");
		jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem1ActionPerformed(evt);
			}
		});
		jMenu2.add(jMenuItem1);

		jMenuBar1.add(jMenu2);
		jMenu3.setText("Import");

		jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_I,
				java.awt.event.InputEvent.CTRL_MASK));
		jMenuItem8.setText("Import PNGs...");
		jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem8ActionPerformed(evt);
			}
		});
		jMenu3.add(jMenuItem8);
		jMenuBar1.add(jMenu3);
		setJMenuBar(jMenuBar1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						javax.swing.GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.TRAILING)
												.addComponent(
														jScrollPane1,
														javax.swing.GroupLayout.Alignment.LEADING,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														852, Short.MAX_VALUE)
												.addGroup(
														javax.swing.GroupLayout.Alignment.LEADING,
														layout.createSequentialGroup()
																.addComponent(
																		jButton1)
																// add
																.addGap(18, 18,
																		18)
																.addComponent(
																		jButton2)
																// delete
																.addGap(18, 18,
																		18)
																.addComponent(
																		jButton3)
																// up
																.addGap(18, 18,
																		18)
																.addComponent(
																		jButton4)
																// down
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																		378,
																		Short.MAX_VALUE)
																.addComponent(
																		jButton5)
																// upload
																.addGap(18, 18,
																		18)
																.addComponent(
																		jButton6)) // exit
												.addGroup(
														javax.swing.GroupLayout.Alignment.LEADING,
														layout.createSequentialGroup()
																.addComponent(
																		jButton7)
																// new
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		jButton8)
																// open
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		jButton9) // save
																		.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		jButton10)) // save
												.addComponent(
														jLabel1,
														javax.swing.GroupLayout.Alignment.LEADING,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														852, Short.MAX_VALUE))
								.addContainerGap()));

		layout.linkSize(javax.swing.SwingConstants.HORIZONTAL,
				new java.awt.Component[] { jButton1, jButton2, jButton3,
						jButton4, jButton5, jButton6, jButton7, jButton8,
						jButton9, jButton10 });

		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						javax.swing.GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(jButton7) // new
												.addComponent(jButton8) // open
												.addComponent(jButton9)
												.addComponent(jButton10))
								// save
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jLabel1,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										14,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jScrollPane1,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										590, Short.MAX_VALUE)
								.addGap(18, 18, 18)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(jButton1) // add
												.addComponent(jButton2) // delete
												.addComponent(jButton3) // up
												.addComponent(jButton4) // down
												.addComponent(jButton6) // exit
												.addComponent(jButton5)) // upload
								.addContainerGap()));

		layout.linkSize(javax.swing.SwingConstants.VERTICAL,
				new java.awt.Component[] { jButton1, jButton2, jButton3,
						jButton4, jButton5, jButton6, jButton7, jButton8,
						jButton9, jButton10 });

		pack();
	}// end-method initComponents

	private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) // About
	{
		About();
	}// end-method jMenuItem1ActionPerformed

	private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) // NewProject
	{
		NewProject();
	}// end-method jMenuItem2ActionPerformed

	private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) // OpenProject
	{
		OpenProject();
	}// end-method jMenuItem3ActionPerformed

	private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) // SaveProject
	{
		SaveProject();
	}// end-method jMenuItem4ActionPerformed

	private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) // Upload
	{
		Upload();
	}// end-method jMenuItem6ActionPerformed

	private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) // Exit
	{
		Exit();
	}// end-method jMenuItem7ActionPerformed

	private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) // importPngs
	{
		try {
			importPngs();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}// end-method jMenuItem8ActionPerformed

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) // Add
	{
		try {
			Add();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}// end-method jButton1ActionPerformed

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) // Delete
	{
		Delete();
	}// end-method jButton2ActionPerformed

	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) // Up
	{
		Up();
	}// end-method jButton3ActionPerformed

	private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) // Down
	{
		Down();
	}// end-method jButton4ActionPerformed

	private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) // Upload
	{
		Upload();
	}// end-method jButton5ActionPerformed

	private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) // Exit
	{
		Exit();
	}// end-method jButton6ActionPerformed

	private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) // NewProject
	{
		NewProject();
	}// end-method jButton7ActionPerformed

	private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) // OpenProject
	{
		OpenProject();
	}// end-method jButton8ActionPerformed

	private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) // SaveProject
	{
		SaveProject();
	}// end-method jButton9ActionPerformed

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {

			public void run() {
				new Main().setVisible(true);
			}
		});
	}

}
