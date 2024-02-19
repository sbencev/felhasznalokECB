package hu.ruander.Entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import hu.ruander.Control.Felhasznalo;

public class ABKezelo implements IAdatbazisMuveletekFelhasznaloJDBC {

	private static Connection kapcsolat;
	private static PreparedStatement sqlUtasitas;

	public ABKezelo(String connectionString, String user, String jelszo) throws SQLException {

		csatlakozas(connectionString, user, jelszo);

	}

	public void csatlakozas(String connectionString, String user, String jelszo) throws SQLException {

		try {

			kapcsolat = DriverManager.getConnection(connectionString, user, jelszo);
		} catch (Exception e) {

			throw new SQLException("A csatlakozás sikertelen!" + e.getMessage());

		}

	}

	public void kapcsolatBontas() throws SQLException {

		try {

			kapcsolat.close();

		} catch (Exception e) {

			throw new SQLException("A kapcsolat bontása sikertelen!");

		}

	}

	// CRUD műveletek:
	// felhasznalo tábla oszlopai: ID, Felhasznalonev, Jelszo, RegisztracioDatuma

	// CRUD - c
	public void ujfelhasznalo(Felhasznalo felhasznalo) throws SQLException {

		try {

			sqlUtasitas = kapcsolat.prepareStatement(
					"INSERT INTO felhasznalo (felhasznalonev, Jelszo, Egyenleg) VALUES (?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			sqlUtasitas.setString(1, felhasznalo.getFelhasznaloNev());
			sqlUtasitas.setString(2, felhasznalo.getJelszo());
			sqlUtasitas.setInt(3, felhasznalo.getEgyenleg());

			// sql utasítás végrehajtása 3 módon történhet:
			// executeQuery() --> select esetén használatos, ResultSet típusban szolgáltatja
			// az eredményhalmazt
			// executeUpdate() --> insert/update/delete utasítást futtat és int eredményt
			// szolgáltat
			// execute() --> bármilyen utasítást futtat. logikai értékkel tér vissza, igaz
			// eredményt ad, ha ResultSet eredményünk van
			// és hamisat, ha int értéket kapunk vissz az adatbázisból.

			sqlUtasitas.executeUpdate();

			ResultSet res = sqlUtasitas.getGeneratedKeys();
			if (res.next()) {

				felhasznalo.setId(res.getInt(1));

			}

			sqlUtasitas.clearParameters();
			sqlUtasitas = kapcsolat.prepareStatement("SELECT * from felhasznalo WHERE ID=?");
			sqlUtasitas.setInt(1, felhasznalo.getId());

			res = sqlUtasitas.executeQuery();
			if (res.next()) {

				LocalDate datum = res.getTimestamp("RegisztracioDatuma").toLocalDateTime().toLocalDate();
				felhasznalo.setRegisztracioDatuma(datum);

			}
			sqlUtasitas.clearParameters();

		} catch (Exception e) {

			if (e.getMessage().toLowerCase().contains("duplicate")) {

				throw new SQLException("Létező felhasználónév!");

			} else {

				throw new SQLException("A felhasználó felvitele sikertelen! " + e.getMessage());

			}

		}

	}

	// CRUD U - update
	public void felhasznaloModositasa(Felhasznalo felhasznalo) throws SQLException {

		try {

			sqlUtasitas = kapcsolat
					.prepareStatement("UPDATE felhasznalo SET Felhasznalonev=?, Jelszo=?, Egyenleg=? WHERE ID=?");
			sqlUtasitas.setString(1, felhasznalo.getFelhasznaloNev());
			sqlUtasitas.setString(2, felhasznalo.getJelszo());
			sqlUtasitas.setInt(3, felhasznalo.getEgyenleg());
			sqlUtasitas.setInt(4, felhasznalo.getId());

			sqlUtasitas.executeUpdate();
			sqlUtasitas.clearParameters();

		} catch (Exception e) {

			throw new SQLException("A felhasználó módosítása sikertelen! " + e.getMessage());

		}

	}

	// CRUD - d
	public void felhasznaloTorlese(Felhasznalo felhasznalo) throws SQLException {

		try {

			sqlUtasitas = kapcsolat.prepareStatement("delete from felhasznalo where ID=?");
			sqlUtasitas.setInt(1, felhasznalo.getId());

			sqlUtasitas.executeUpdate();
			sqlUtasitas.clearParameters();

		} catch (Exception e) {

			throw new SQLException("A felhasználó törlése sikertelen! " + e.getMessage());

		}

	}

	// CRUD - R:
	public List<Felhasznalo> felhasznalokBeolvasasa() throws SQLException {

		try {

			List<Felhasznalo> felhasznalok = new ArrayList<Felhasznalo>();

			sqlUtasitas = kapcsolat.prepareStatement("SELECT * from felhasznalo");

			ResultSet res = sqlUtasitas.executeQuery();
			while (res.next()) {

				felhasznalok.add(new Felhasznalo(res.getInt("ID"), res.getString("Felhasznalonev"),
						res.getString("Jelszo"), res.getTimestamp("RegisztracioDatuma").toLocalDateTime().toLocalDate(),
						res.getInt("Egyenleg")));

			}

			res.close();
			return felhasznalok;

		} catch (Exception e) {

			throw new SQLException("Adatbázisból beolvasás sikertelen");

		}

	}

	// minden további adatbázisművelet ide kerülhet.

	public Felhasznalo belepes(String felhasznalonev, String jelszo) throws SQLException {

		try {

			sqlUtasitas = kapcsolat.prepareStatement("select * from felhasznalo where felhasznalonev=? and Jelszo=?");
			sqlUtasitas.setString(1, felhasznalonev);
			sqlUtasitas.setString(2, jelszo);

			ResultSet res = sqlUtasitas.executeQuery();
			if (res.next()) {

				Felhasznalo felhasznalo = new Felhasznalo(res.getInt("ID"), res.getString("Felhasznalonev"),
						res.getString("Jelszo"), res.getTimestamp("RegisztracioDatuma").toLocalDateTime().toLocalDate(),
						res.getInt("Egyenleg"));

				return felhasznalo;
			}

		} catch (Exception e) {

			throw new SQLException("Adatbázis hiba!");

		}

		return null;
	}

}
