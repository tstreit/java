/*
 * SystemInfo.java
 *
 * Created on Feb 25, 2009, 11:38:44 AM
 */

import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;
import javax.swing.ImageIcon;

/**
 *
 * @author jzalumsky
 */
public class SystemInfo extends javax.swing.JFrame {

private static final long serialVersionUID = 3163219865543732202L;

/** Creates new form SystemInfo */
public SystemInfo()
	{
	initComponents();
	Properties pr = System.getProperties();
	TreeSet<Object> propKeys = new TreeSet<Object>(pr.keySet());  // TreeSet sorts keys
	for (Iterator<Object> it = propKeys.iterator(); it.hasNext();) 
		{
		String key = (String) it.next();
		jTextArea1.append("" + key + "=" + pr.get(key) + "\n");
		} // end-for Iterator it = propKeys.iterator(); it.hasNext();
	} // end-constructor SystemInfo

ImageIcon appIcon = new ImageIcon(getClass().getResource("Projector.gif"));



private void initComponents()
	{

	jButton1 = new javax.swing.JButton();
	jScrollPane1 = new javax.swing.JScrollPane();
	jTextArea1 = new javax.swing.JTextArea();

	setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	setTitle("Slide Show Builder - System Information");
	setIconImage(appIcon.getImage());

	jButton1.setText("OK");
	jButton1.addActionListener(new java.awt.event.ActionListener() {
	public void actionPerformed(java.awt.event.ActionEvent evt)
		{
		jButton1ActionPerformed(evt);
		}
	});

	jTextArea1.setColumns(20);
	jTextArea1.setEditable(false);
	jTextArea1.setRows(5);
	jScrollPane1.setViewportView(jTextArea1);

	javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
	getContentPane().setLayout(layout);
	layout.setHorizontalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
							.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE)
							.addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
							.addContainerGap())
			);
	layout.setVerticalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
					.addContainerGap()
					.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
					.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(jButton1)
					.addContainerGap())
			);

	pack();
	}// end-method initComponents

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) 
	{
	this.dispose();
	}// end-method jButton1ActionPerformed

// Variables declaration 
private javax.swing.JButton jButton1;          // OK
private javax.swing.JScrollPane jScrollPane1;
private javax.swing.JTextArea jTextArea1;
// End of variables declaration
}
