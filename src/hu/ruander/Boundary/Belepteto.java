package hu.ruander.Boundary;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import hu.ruander.Control.Felhasznalo;
import hu.ruander.Control.JelszoMuveletek;
import hu.ruander.Entity.ABKezelo;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class Belepteto extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtFelhasznalo;
	private JPasswordField passwordField;
	
	private Felhasznalo felhasznalo;
	private ABKezelo dbMotor;

	public Felhasznalo getFelhasznalo() {
		return felhasznalo;
	}
	

	/**
	 * Create the dialog.
	 */
	public Belepteto(ABKezelo dbMotor) {
		
		this.dbMotor = dbMotor;
		setModal(true);
		setTitle("Belépés");
		setBounds(100, 100, 450, 231);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Felhasználói azonosító:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(0, 0, 434, 36);
		contentPanel.add(lblNewLabel);
		
		txtFelhasznalo = new JTextField();
		txtFelhasznalo.setBounds(0, 37, 434, 36);
		contentPanel.add(txtFelhasznalo);
		txtFelhasznalo.setColumns(10);
		
		JLabel lblJelsz = new JLabel("Jelszó:");
		lblJelsz.setHorizontalAlignment(SwingConstants.CENTER);
		lblJelsz.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblJelsz.setBounds(0, 72, 434, 36);
		contentPanel.add(lblJelsz);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(0, 110, 434, 36);
		contentPanel.add(passwordField);
		
		JButton btnNewButton = new JButton("Belépés");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				belepesEllenorzes();
				
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnNewButton.setBounds(0, 146, 434, 42);
		contentPanel.add(btnNewButton);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
		}
	}

	protected void belepesEllenorzes() {
		
		try {
			
			felhasznalo = dbMotor.belepes(txtFelhasznalo.getText(), JelszoMuveletek.titkosit(new String(passwordField.getPassword())));
			
			if (felhasznalo == null) {
				
				JOptionPane.showMessageDialog(this, "Hibás felhasználónév és/vagy jelszó!","Hiba",JOptionPane.ERROR_MESSAGE);
				
			}
			else {
				
				dispose();
				
			}
			
		} catch (SQLException e) {
			
			JOptionPane.showMessageDialog(this, e.getMessage(), "Hiba", JOptionPane.ERROR_MESSAGE);
			
		}
		
		
	}
}
