package hu.ruander.Boundary;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Font;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import hu.ruander.Control.Felhasznalo;
import hu.ruander.Entity.ABKezelo;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;

public class FoprogramFelhasznaloJDBC {

	private JFrame frmFoablak;
	private JTable tblFelhasznalo;

	private DefaultTableModel model;
	private List<Felhasznalo> felhasznalok;

	private ABKezelo dbMotor;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FoprogramFelhasznaloJDBC window = new FoprogramFelhasznaloJDBC();
					window.frmFoablak.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FoprogramFelhasznaloJDBC() {
		initialize();

		try {

			String connectionString = "jdbc:mysql://localhost:3306/felhasznaloDB?autoReconnect=true&useSSL=false";
			dbMotor = new ABKezelo(connectionString, "root", "Attpzk78@");

			Belepteto belepesAblak = new Belepteto(dbMotor);
			belepesAblak.setVisible(true);

			if (belepesAblak.getFelhasznalo() == null) {

				System.exit(0);

			}

			frmFoablak.setTitle(frmFoablak.getTitle() + " - " + belepesAblak.getFelhasznalo().getFelhasznaloNev());

			felhasznalok = dbMotor.felhasznalokBeolvasasa();
			listaTablazatbaToltes();

		} catch (SQLException e) {

			JOptionPane.showMessageDialog(frmFoablak, e.getMessage() + "\n A program bezárul", "DB hiba",
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);

		}

	}

	private void listaTablazatbaToltes() {

		for (Felhasznalo felhasznalo : felhasznalok) {

			Object[] adatok = new Object[] {

					felhasznalo.getId(), felhasznalo.getFelhasznaloNev(), felhasznalo.getRegisztracioDatuma(),
					felhasznalo.getEgyenleg() };
			model.insertRow(tblFelhasznalo.getRowCount(), adatok);

		}

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFoablak = new JFrame();
		frmFoablak.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

				Object[] opciok = { "Igen", "Nem" };
				if (JOptionPane.showOptionDialog(frmFoablak, "Biztosan ki akar lépni a programból?", "Kilépés",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciok,
						opciok[1]) == JOptionPane.YES_OPTION) {

					try {

						dbMotor.kapcsolatBontas();
						System.exit(0);

					} catch (SQLException e2) {

						JOptionPane.showMessageDialog(frmFoablak, e2.getMessage(), "DB hiba",
								JOptionPane.ERROR_MESSAGE);
						System.exit(1);

					}

				}

			}
		});
		frmFoablak.setTitle("Felhasználói adattárolás");
		frmFoablak.setBounds(100, 100, 600, 500);
		frmFoablak.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmFoablak.getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 564, 264);
		frmFoablak.getContentPane().add(scrollPane);

		tblFelhasznalo = new JTable();
		tblFelhasznalo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblFelhasznalo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		scrollPane.setViewportView(tblFelhasznalo);

		felhasznalok = new ArrayList<Felhasznalo>();
		String[] oszlopnevek = { "ID", "Felhasználó", "Regisztráció", "Egyenleg" };
		model = new DefaultTableModel(null, oszlopnevek);
		tblFelhasznalo.setModel(model);

		DefaultTableCellRenderer crenderer = new DefaultTableCellRenderer();
		crenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < model.getColumnCount(); i++) {

			tblFelhasznalo.getColumnModel().getColumn(i).setCellRenderer(crenderer);

		}

		JButton btnFelvitel = new JButton("Új felhasználó");
		btnFelvitel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				adatFelvitel();

			}

		});
		btnFelvitel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnFelvitel.setBounds(10, 289, 388, 49);
		frmFoablak.getContentPane().add(btnFelvitel);

		JButton btnmodosit = new JButton("Felhasználó módosítása");
		btnmodosit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				adatmodosit();

			}

		});
		btnmodosit.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnmodosit.setBounds(10, 349, 388, 49);
		frmFoablak.getContentPane().add(btnmodosit);

		JButton btnTorles = new JButton("Törlés");
		btnTorles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				adatTorles();

			}

		});
		btnTorles.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnTorles.setBounds(10, 412, 388, 49);
		frmFoablak.getContentPane().add(btnTorles);

		JButton btnKilepes = new JButton("Kilépés");
		btnKilepes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				frmFoablak.dispatchEvent(new WindowEvent(frmFoablak, WindowEvent.WINDOW_CLOSING));

			}
		});
		btnKilepes.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnKilepes.setBounds(485, 390, 89, 60);
		frmFoablak.getContentPane().add(btnKilepes);
	}

	private void adatFelvitel() {

		FelvitelmodositAblak felugroAblak = new FelvitelmodositAblak(dbMotor);
		felugroAblak.setVisible(true);

		if (felugroAblak.isDialogResult()) {

			Felhasznalo ujFelhasznalo = felugroAblak.getFelhasznalo();
			Object[] adatok = new Object[] { ujFelhasznalo.getId(), ujFelhasznalo.getFelhasznaloNev(),
					ujFelhasznalo.getRegisztracioDatuma(), ujFelhasznalo.getEgyenleg() };
			model.insertRow(tblFelhasznalo.getRowCount(), adatok);
			felhasznalok.add(ujFelhasznalo);

		}

	}

	private void adatmodosit() {

		if (tblFelhasznalo.getSelectedRow() != -1) {

			FelvitelmodositAblak felugroAblak = new FelvitelmodositAblak(
					felhasznalok.get(tblFelhasznalo.getSelectedRow()), dbMotor);
			felugroAblak.setVisible(true);

			if (felugroAblak.isDialogResult()) {

				model.setValueAt(felugroAblak.getFelhasznalo().getFelhasznaloNev(), tblFelhasznalo.getSelectedRow(), 1);
				model.setValueAt(felugroAblak.getFelhasznalo().getEgyenleg(), tblFelhasznalo.getSelectedRow(), 3);

			}

		} else {

			JOptionPane.showMessageDialog(frmFoablak, "Nincs kiválasztott elem!", "Figyelmeztetés",
					JOptionPane.WARNING_MESSAGE);

		}

	}

	private void adatTorles() {

		if (tblFelhasznalo.getSelectedRow() != -1 && JOptionPane.showOptionDialog(frmFoablak,
				"Biztosan törli a felhasználót?", "Törlés", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, null, null) == JOptionPane.YES_OPTION) {

			try {
				dbMotor.felhasznaloTorlese(felhasznalok.get(tblFelhasznalo.getSelectedRow()));
				felhasznalok.remove(tblFelhasznalo.getSelectedRow());
				model.removeRow(tblFelhasznalo.getSelectedRow());
			} catch (SQLException e) {

				JOptionPane.showMessageDialog(frmFoablak, e.getMessage(), "DB hiba", JOptionPane.ERROR_MESSAGE);
			}

		} else if (tblFelhasznalo.getSelectedRow() == -1) {

			JOptionPane.showMessageDialog(frmFoablak, "Nincs kiválasztott elem!", "Figyelmeztetés",
					JOptionPane.WARNING_MESSAGE);

		}

	}

}
