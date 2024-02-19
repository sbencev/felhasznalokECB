package hu.ruander.Control;

import java.time.LocalDate;

/* Készítsünk egytáblás adatbázist, melyben felhasználókat tudunk tárolni:
FELHASZNÁLÓ(UID, Felhasználónév, Jelszó, Regisztráció dátuma)
Oldjuk meg programból, hogy fel tudjunk tölteni, illetve tudjunk módosítani és törölni is az adatbázisban. */

public class Felhasznalo {

	// még hiányzik a konstruktor

	private Integer id;
	private String felhasznaloNev;
	private String jelszo;
	private LocalDate regisztracioDatuma;

	private int egyenleg;

	public Felhasznalo(Integer id, String felhasznaloNev, String jelszo, LocalDate regisztracioDatuma, int egyenleg) {

		setId(id);
		this.felhasznaloNev = felhasznaloNev;
		this.jelszo = jelszo;
		setRegisztracioDatuma(regisztracioDatuma);
		this.egyenleg = egyenleg;

	}

	public Felhasznalo(String felhasznaloNev, String jelszo, int egyenleg) {

		setFelhasznaloNev(felhasznaloNev);
		setJelszo(jelszo);
		setEgyenleg(egyenleg);

	}

	public int getEgyenleg() {
		return egyenleg;
	}

	public void setEgyenleg(int egyenleg) {
		if (egyenleg >= 0) {
			this.egyenleg = egyenleg;
		} else {
			throw new IllegalArgumentException("Az egyenleg nem lehet negativ szam!");
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {

		if (this.id == null) {

			this.id = id;

		} else {

			throw new IllegalStateException("Az ID értéke csak egyszer adható meg!");

		}

	}

	public String getFelhasznaloNev() {
		return felhasznaloNev;
	}

	public void setFelhasznaloNev(String felhasznaloNev) {

		if (felhasznaloNev != null && !felhasznaloNev.isBlank()) {

			this.felhasznaloNev = felhasznaloNev;

		} else {

			throw new IllegalArgumentException("A felhasználó azonosító nem lehet üres!");

		}

	}

	public String getJelszo() {
		return jelszo;
	}

	public void setJelszo(String jelszo) {

		if (JelszoMuveletek.erosJelszo(jelszo)) {

			this.jelszo = JelszoMuveletek.titkosit(jelszo);

		} else {

			throw new IllegalArgumentException(
					"A jelszó minimum 8 karakter kell legyen és tartalmaznia kell kisbetűt, nagybetűt, számjegyet! ");

		}

	}

	public LocalDate getRegisztracioDatuma() {
		return regisztracioDatuma;
	}

	public void setRegisztracioDatuma(LocalDate regisztracioDatuma) {

		if (this.regisztracioDatuma == null) {

			this.regisztracioDatuma = regisztracioDatuma;

		} else {

			throw new IllegalStateException("A regisztráció dátuma csak egyszer adható meg!");

		}
	}

}
