package eu.tasgroup.gestione.businesscomponent.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Timesheet implements Serializable {
	private static final long serialVersionUID = 7654336974232692517L;

	private long id;
	private long id_dipendente;
	private long id_progetto;
	private long id_task;
	private double ore_lavorate;
	private Date data;
	private boolean approvato;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getId_dipendente() {
		return id_dipendente;
	}
	public void setId_dipendente(long id_dipendente) {
		this.id_dipendente = id_dipendente;
	}
	public long getId_progetto() {
		return id_progetto;
	}
	public void setId_progetto(long id_progetto) {
		this.id_progetto = id_progetto;
	}
	public long getId_task() {
		return id_task;
	}
	public void setId_task(long id_task) {
		this.id_task = id_task;
	}
	public double getOre_lavorate() {
		return ore_lavorate;
	}
	public void setOre_lavorate(double ore_lavorate) {
		this.ore_lavorate = ore_lavorate;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public boolean isApprovato() {
		return approvato;
	}
	public void setApprovato(boolean approvato) {
		this.approvato = approvato;
	}
	@Override
	public int hashCode() {
		return Objects.hash(approvato, data, id, id_dipendente, id_progetto, id_task, ore_lavorate);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Timesheet other = (Timesheet) obj;
		return approvato == other.approvato && Objects.equals(data, other.data) && id == other.id
				&& id_dipendente == other.id_dipendente && id_progetto == other.id_progetto && id_task == other.id_task
				&& Double.doubleToLongBits(ore_lavorate) == Double.doubleToLongBits(other.ore_lavorate);
	}
	@Override
	public String toString() {
		return "Timesheet [id=" + id + ", id_dipendente=" + id_dipendente + ", id_progetto=" + id_progetto
				+ ", id_task=" + id_task + ", ore_lavorate=" + ore_lavorate + ", data=" + data + ", approvato="
				+ approvato + "]";
	}
}
