package eu.tasgroup.gestione.businesscomponent.model;

import java.io.Serializable;
import java.util.Objects;

public class Payment implements Serializable{


	private static final long serialVersionUID = 2636460469019243963L;

	private long id;
	private long id_progetto;
	private double cifra;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getId_progetto() {
		return id_progetto;
	}
	public void setId_progetto(long id_progetto) {
		this.id_progetto = id_progetto;
	}
	public double getCifra() {
		return cifra;
	}
	public void setCifra(double cifra) {
		this.cifra = cifra;
	}
	@Override
	public int hashCode() {
		return Objects.hash(cifra, id, id_progetto);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Payment other = (Payment) obj;
		return Double.doubleToLongBits(cifra) == Double.doubleToLongBits(other.cifra) && id == other.id
				&& id_progetto == other.id_progetto;
	}
	@Override
	public String toString() {
		return "Payment [id=" + id + ", id_progetto=" + id_progetto + ", cifra=" + cifra + "]";
	}
	
	
}
