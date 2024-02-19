package hu.ruander.Entity;

import java.sql.SQLException;
import java.util.List;

import hu.ruander.Control.Felhasznalo;

public interface IAdatbazisMuveletekFelhasznaloJDBC {

	void ujfelhasznalo(Felhasznalo felhasznalo) throws SQLException;

	void felhasznaloModositasa(Felhasznalo felhasznalo) throws SQLException;

	void felhasznaloTorlese(Felhasznalo felhasznalo) throws SQLException;

	List<Felhasznalo> felhasznalokBeolvasasa() throws SQLException;

	Felhasznalo belepes(String felhasznalonev, String jelszo) throws SQLException;

}
