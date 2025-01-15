package eu.tasgroup.gestione.businesscomponent.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import eu.tasgroup.gestione.businesscomponent.enumerated.Fase;
import eu.tasgroup.gestione.businesscomponent.enumerated.StatoTask;

public class ProjectTask implements Serializable{
	private static final long serialVersionUID = 4442416040038390835L;

	private long id;
	private long id_progetto;
	private String nome_task;
	private String descrizione;
	private long id_dipendente;
	private StatoTask stato;
	private Date scadenza;
	private Fase fase;
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
	public String getNome_task() {
		return nome_task;
	}
	public void setNome_task(String nome_task) {
		this.nome_task = nome_task;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public long getId_dipendente() {
		return id_dipendente;
	}
	public void setId_dipendente(long id_dipendente) {
		this.id_dipendente = id_dipendente;
	}
	public StatoTask getStato() {
		return stato;
	}
	public void setStato(StatoTask stato) {
		this.stato = stato;
	}
	public Date getScadenza() {
		return scadenza;
	}
	public void setScadenza(Date scadenza) {
		this.scadenza = scadenza;
	}
	public Fase getFase() {
		return fase;
	}
	public void setFase(Fase fase) {
		this.fase = fase;
	}
	@Override
	public int hashCode() {
		return Objects.hash(descrizione, fase, id, id_dipendente, id_progetto, nome_task, scadenza, stato);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectTask other = (ProjectTask) obj;
		return Objects.equals(descrizione, other.descrizione) && fase == other.fase && id == other.id
				&& id_dipendente == other.id_dipendente && id_progetto == other.id_progetto
				&& Objects.equals(nome_task, other.nome_task) && Objects.equals(scadenza, other.scadenza)
				&& stato == other.stato;
	}
	@Override
	public String toString() {
		return "ProjectTask [id=" + id + ", id_progetto=" + id_progetto + ", nome_task=" + nome_task + ", descrizione="
				+ descrizione + ", id_dipendente=" + id_dipendente + ", stato=" + stato + ", scadenza=" + scadenza
				+ ", fase=" + fase + "]";
	}
	
	
	
}
