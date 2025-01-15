package eu.tasgroup.gestione.businesscomponent.model;

import java.io.Serializable;
import java.util.Objects;

public class UserSkill implements Serializable{

	private static final long serialVersionUID = -988738152236513943L;
	
	private long id;
	private long id_competenze;
	private long id_utente;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getId_competenze() {
		return id_competenze;
	}
	public void setId_competenze(long id_competenze) {
		this.id_competenze = id_competenze;
	}
	public long getId_utente() {
		return id_utente;
	}
	public void setId_utente(long id_utente) {
		this.id_utente = id_utente;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id, id_competenze, id_utente);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserSkill other = (UserSkill) obj;
		return id == other.id && id_competenze == other.id_competenze && id_utente == other.id_utente;
	}
	@Override
	public String toString() {
		return "UserSkill [id=" + id + ", id_competenze=" + id_competenze + ", id_utente=" + id_utente + "]";
	}
	
	
	

}
