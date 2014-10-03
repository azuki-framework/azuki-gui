package org.azkfw.gui;

import javax.swing.JFrame;

import org.azkfw.gui.dialog.PreferenceDialog;
import org.azkfw.gui.dialog.ProgressDialog;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

    	JFrame frm = new JFrame();
    	frm.setVisible(true);
    	frm.setBounds(200, 50, 800, 600);
    	frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	
    	//ProgressDialog dialog = new ProgressDialog(frm,true);
    	//dialog.setVisible(true);
    	
    	
    	PreferenceDialog dialog = new PreferenceDialog(frm, true);
    	dialog.setVisible(true);
    }
}
