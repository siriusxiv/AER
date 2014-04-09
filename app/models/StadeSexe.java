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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class StadeSexe extends Model{
	@Id
	public Integer stade_sexe_id;
	@NotNull
	public String stade_sexe_intitule;
	@Column(columnDefinition="TEXT")
	public String stade_sexe_explication;
	
	public static Finder<Integer,StadeSexe> find = new Finder<Integer,StadeSexe>(Integer.class, StadeSexe.class);

	public static List<StadeSexe> findAll(){
		return find.where().orderBy("stade_sexe_id").findList();
	}
	
	@Override
	public String toString(){
		return stade_sexe_intitule;
	}
	
	/**
	 * Renvoie tous les fils du stade sexe pour le groupe donné.
	 * @param groupe
	 * @return
	 */
	public List<StadeSexe> getStadeSexeFilsPourTelGroupe(Groupe groupe){
		List<StadeSexeHierarchieDansGroupe> sshdgs =
				StadeSexeHierarchieDansGroupe.find.where()
						.eq("stade_sexe_pere", this)
						.eq("groupe",groupe).orderBy("position").findList();
		List<StadeSexe> stadesexes = new ArrayList<StadeSexe>();
		for(StadeSexeHierarchieDansGroupe sshdg : sshdgs){
			stadesexes.add(sshdg.stade_sexe);
		}
		return stadesexes;
	}
}
