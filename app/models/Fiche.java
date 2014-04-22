/*********************************************************************************
 * 
 *   Copyright 2014 BOUSSEJRA Malik Olivier, HALDEBIQUE Geoffroy, ROYER Johan
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *   
 ********************************************************************************/

package models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import play.db.ebean.Model;

/**
 * Pour l'instant, les coordonnées GPS et les mailles utm 1x1 ne sont pas
 * utilisées.
 * @author malik
 *
 */
@SuppressWarnings("serial")
@Entity
public class Fiche extends Model {
	@Id
	public Long fiche_id;
	public String fiche_lieudit;
	public Calendar fiche_date_min;
	@NotNull
	public Calendar fiche_date;
	@Column(columnDefinition="TEXT")
	public String fiche_memo;
	@ManyToOne
	@NotNull
	public UTMS fiche_utm;
	@ManyToOne
	public Commune fiche_commune;
	public String fiche_gps_coordinates;
	public String fiche_utm1x1;
	@NotNull
	public Calendar fiche_date_soumission;
	
	public static Finder<Long,Fiche> find = new Finder<Long,Fiche>(Long.class, Fiche.class);
	
	public Fiche(Commune commune, String lieu_dit, UTMS utm, Calendar date_min, Calendar date, String memo) {
		fiche_commune=commune;
		fiche_lieudit=lieu_dit;
		fiche_utm=utm;
		fiche_date_min=date_min;
		fiche_date=date;
		fiche_memo=memo;
		fiche_date_soumission=Calendar.getInstance();
		fiche_id=Fiche.idSuivante();
	}
	/**
	 * renvoie les fonctions FicheHasMembre rattachées à la liste sélectionnée.
	 * @return
	 */
	public  List<FicheHasMembre> getFicheHasMembre(){
		List<FicheHasMembre> fhm=FicheHasMembre.find.where().eq("fiche", this).findList();
		return fhm;
	}
	@Override
	public String toString(){
		return fiche_id+"-"+fiche_utm;
	}
	
	/**
	 * Renvoie la fiche contenant le plus vieux témoignage de la base de données.
	 * @return
	 */
	public static Fiche getPlusVieuxTemoignage(){
		Observation o = Observation.find.where()
				.eq("observation_validee",Observation.VALIDEE)
			.setMaxRows(1).orderBy("observation_fiche.fiche_date").findUnique();
		return o.getFiche();
	}
	
	/**
	 * Renvoie la date max de la fiche sous la forme "dd/MM/yyyy".
	 * @return
	 */
	public String getDateInString(){
		SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
		return date_format.format(fiche_date.getTime());
	}
	
	/**
	 * Renvoie l'id suivante qui sera allouée.
	 * @return
	 */
	public static Long idSuivante(){
		Fiche f = find.where().setMaxRows(1).orderBy("fiche_id desc").findUnique();
		if(f==null)
			return 1L;
		else
			return f.fiche_id+1L;
	}
}
