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


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import play.db.ebean.Model;

import java.util.*;

@SuppressWarnings("serial")
@Entity
public class Groupe extends Model {
	@Id
	public Integer groupe_id;
	@NotNull
	public String groupe_nom;
	
	public static Finder<Integer,Groupe> find = new Finder<Integer,Groupe>(Integer.class, Groupe.class);

	public static List<Groupe> findAll(){
		return find.all();
	}
	
	@Override
	public String toString(){
		return groupe_nom;
	}

	/**
	 * Renvoie l'expert assigné à ce groupe.
	 * S'il n'y a pas d'expert assigné à ce groupe, alors renvoie null.
	 * @return
	 */
	public Membre getExpert(){
		MembreIsExpertOnGroupe mieog = MembreIsExpertOnGroupe.find.where().eq("groupe", this).findUnique();
		if(mieog!=null)
			return mieog.membre;
		else
			return null;
	}
	
	/**
	 * Renvoie tous les stades sexes pères (c'est-à-dire les premiers choix qui apparaissent)
	 * pour le groupe donné.
	 * @return
	 */
	public List<StadeSexe> getStadeSexePeres(){
		List<StadeSexeHierarchieDansGroupe> sshdgs =
				StadeSexeHierarchieDansGroupe.find.where()
						.eq("stade_sexe_pere", null)
						.eq("groupe",this).orderBy("position").findList();
		List<StadeSexe> stadesexes = new ArrayList<StadeSexe>();
		for(StadeSexeHierarchieDansGroupe sshdg : sshdgs){
			stadesexes.add(sshdg.stade_sexe);
		}
		return stadesexes;
	}

	/**
	 * Renvoie la liste des dates charnières du groupe triées par ordre
	 * chronologiquement croissant.
	 * @return
	 */
	public List<DateCharniere> getDatesCharnieres(){
		return DateCharniere.find.where().eq("date_charniere_groupe", this)
				.orderBy("date_charniere_date").findList();
	}
}
