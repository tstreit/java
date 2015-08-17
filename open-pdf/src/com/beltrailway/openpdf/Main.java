package com.beltrailway.openpdf;

import java.awt.Desktop;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SpringLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.awt.Toolkit;

public class Main extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	String pdfname;
	static Main frame = new Main();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
			        frame.addWindowListener(getWindowAdapter());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private static WindowAdapter getWindowAdapter() {
        return new WindowAdapter() {
            @Override
            public void windowIconified(WindowEvent we) {
                frame.setState(JFrame.NORMAL);
            }
        };
    }

	/**
	 * Create the frame.
	 */
	public Main() {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/com/beltrailway/openpdf/pdf.jpg")));
		setTitle("Car Department Forms");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 347, 424);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		JButton button = new JButton("East Receiving Yard Turnover Sheet");
		sl_contentPane.putConstraint(SpringLayout.EAST, button, -30, SpringLayout.EAST, contentPane);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pdfname = "//beltfileserver/CarShopPDF/EastReceivingTurnover.pdf";
				try {
					OpenPDF();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(button);
		
		JButton button_1 = new JButton("East Yard Turnover Sheet");
		sl_contentPane.putConstraint(SpringLayout.WEST, button_1, 30, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, button_1, -30, SpringLayout.EAST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, button, 0, SpringLayout.WEST, button_1);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, button, -6, SpringLayout.NORTH, button_1);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pdfname = "//beltfileserver/CarShopPDF/EastYardTurnover.pdf";
				try {
					OpenPDF();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(button_1);
		
		JButton button_2 = new JButton("Job Bid-Bump Form");
		sl_contentPane.putConstraint(SpringLayout.WEST, button_2, 30, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, button_2, -30, SpringLayout.EAST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, button_1, -6, SpringLayout.NORTH, button_2);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pdfname = "//beltfileserver/CarShopPDF/JobBid.pdf";
				try {
					OpenPDF();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(button_2);
		
		JButton button_3 = new JButton("Markman Air Brake Report");
		sl_contentPane.putConstraint(SpringLayout.WEST, button_3, 30, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, button_3, -30, SpringLayout.EAST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, button_2, -6, SpringLayout.NORTH, button_3);
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pdfname = "//beltfileserver/CarShopPDF/MarkermanAirBrake.pdf";
				try {
					OpenPDF();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(button_3);
		
		JButton button_4 = new JButton("Train Yard Productivity Report");
		sl_contentPane.putConstraint(SpringLayout.WEST, button_4, 30, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, button_4, -30, SpringLayout.EAST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, button_3, -6, SpringLayout.NORTH, button_4);
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pdfname = "//beltfileserver/CarShopPDF/TrainYardProductivity.pdf";
				try {
					OpenPDF();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(button_4);
		
		JButton button_5 = new JButton("Vacation - Personal Day Request Form");
		sl_contentPane.putConstraint(SpringLayout.WEST, button_5, 30, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, button_5, -30, SpringLayout.EAST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, button_4, -6, SpringLayout.NORTH, button_5);
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pdfname = "//beltfileserver/CarShopPDF/VacationRequest.pdf";
				try {
					OpenPDF();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(button_5);
		
		JButton button_6 = new JButton("Vehicle Checklist");
		sl_contentPane.putConstraint(SpringLayout.WEST, button_6, 30, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, button_6, -30, SpringLayout.EAST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, button_5, -6, SpringLayout.NORTH, button_6);
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pdfname = "//beltfileserver/CarShopPDF/VehicleChecklist.pdf";
				try {
					OpenPDF();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(button_6);
		
		JButton button_7 = new JButton("West Receiving Yard Turnover Sheet");
		sl_contentPane.putConstraint(SpringLayout.WEST, button_7, 30, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, button_7, -30, SpringLayout.EAST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, button_6, -6, SpringLayout.NORTH, button_7);
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pdfname = "//beltfileserver/CarShopPDF/WestReceivingTurnover.pdf";
				try {
					OpenPDF();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(button_7);
		
		JButton button_8 = new JButton("West Yard Turnover Sheet");
		sl_contentPane.putConstraint(SpringLayout.NORTH, button_8, 283, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, button_8, 30, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, button_8, -30, SpringLayout.EAST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, button_7, -6, SpringLayout.NORTH, button_8);
		button_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pdfname = "//beltfileserver/CarShopPDF/WestYardTurnover.pdf";
				try {
					OpenPDF();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(button_8);
		
		JButton btnNewButton = new JButton("Mechanical Refriderator Report");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pdfname = "//beltfileserver/CarShopPDF/MechRef.pdf";
				try {
					OpenPDF();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnNewButton, 6, SpringLayout.SOUTH, button_8);
		sl_contentPane.putConstraint(SpringLayout.WEST, btnNewButton, 0, SpringLayout.WEST, button);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnNewButton, 0, SpringLayout.EAST, button);
		contentPane.add(btnNewButton);
		
		JButton button_9 = new JButton("Yard Repair Sheet");
		sl_contentPane.putConstraint(SpringLayout.WEST, button_9, 30, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, button_9, -30, SpringLayout.EAST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, button_9, 35, SpringLayout.SOUTH, btnNewButton);
		button_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pdfname = "//beltfileserver/CarShopPDF/YardRepairSheet.pdf";
				try {
					OpenPDF();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(button_9);
	}

	public void OpenPDF() throws InterruptedException {
		try {

			File pdfFile = new File(pdfname);
			if (pdfFile.exists()) {

				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(pdfFile);
				} else {
					System.out.println("Awt Desktop is not supported!");
				}

			} else {
				System.out.println("File is not exists!");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}