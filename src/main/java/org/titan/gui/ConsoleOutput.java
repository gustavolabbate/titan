package org.titan.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Generate Console for output 
 */
public class ConsoleOutput {

    /**
     * 
     */
    private JTextArea cPanel;
    private JScrollPane scroller;
    private JFrame frame;
    /**
     * @param args
     * @throws Exception 
     */

    public ConsoleOutput() 
    {

	frame = new JFrame();
	
	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	frame.setSize(
		(int) (dim.getWidth()-(dim.getWidth()*.30)),
		(int) (dim.getHeight()-(dim.getHeight()*.60)));
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setLocationRelativeTo(null);
	frame.setTitle("Console");
	cPanel = new JTextArea();
	cPanel.setAutoscrolls(true);
	scroller = new 	JScrollPane(cPanel,
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	frame.add(scroller, BorderLayout.CENTER);

    }

    public final JTextArea getcPanel() {
	return cPanel;
    }

    public final void setcPanel(JTextArea cPanel) {
	this.cPanel = cPanel;
    }

    public final JScrollPane getScroller() {
	return scroller;
    }

    public final void setScroller(JScrollPane scroller) {
	this.scroller = scroller;
    }

    public final JFrame getFrame() {
	return frame;
    }

    public final void setFrame(JFrame frame) {
	this.frame = frame;
    }

}
