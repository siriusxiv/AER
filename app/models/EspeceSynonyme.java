package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class EspeceSynonyme extends Model  {
	@Id
	public Integer synonyme_id;
	@NotNull
	public String synonyme_nom;
	@NotNull
	public boolean synonyme_origineAER;
	@NotNull
	@ManyToOne
	public Espece synonyme_espece;
	
	public static Finder<Integer,EspeceSynonyme> find = new Finder<Integer,EspeceSynonyme>(Integer.class, EspeceSynonyme.class);
	
	@Override
	public String toString(){
		return synonyme_espece+" alias "+synonyme_nom;
	}
}
