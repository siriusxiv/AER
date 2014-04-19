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

	public Groupe(String groupe_nom) {
		this.groupe_nom=groupe_nom;
	}

	public static List<Groupe> findAll(){
		return find.findList();
	}
	
	@Override
	public String toString(){
		return groupe_nom;
	}

	/**
	 * Renvoie la liste d'experts assigné à ce groupe.
	 * @return
	 */
	public List<MembreIsExpertOnGroupe> getExperts(){
		return MembreIsExpertOnGroupe.find.where().eq("groupe", this).findList();
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
	 * Renvoie tous les stades sexes pour le groupe donné.
	 * @return
	 */
	public List<StadeSexe> getStadesSexes(){
		List<StadeSexeHierarchieDansGroupe> sshdgs =
				StadeSexeHierarchieDansGroupe.find.where()
						.eq("groupe",this).orderBy("stade_sexe.stade_sexe_id").findList();
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
	
	/**
	 * Renvoie la liste des sous-groupes inclus dans ce groupe
	 * @return
	 */
	public List<SousGroupe> getSousGroupes(){
		return SousGroupe.find.where().eq("sous_groupe_groupe", this).findList();
	}

	/**
	 * Renvoie la liste des espèces dans ce groupe triées par systématique.
	 * @return
	 */
	public List<Espece> getEspecesInThisBySystematique(){
		List<SousGroupe> sgs = this.getSousGroupes();
		List<Espece> especes = new ArrayList<Espece>();
		for(SousGroupe sg : sgs){
			List<Espece> especeDansSG = Espece.find.where().eq("espece_sous_groupe", sg).findList();
			especes.addAll(especeDansSG);
		}
		Collections.sort(especes,new Espece());
		return especes;
	}
	/**
	 * Renvoie la liste des espèces dans ce groupe.
	 * @return
	 */
	public List<Espece> getEspecesInThis(){
		List<SousGroupe> sgs = this.getSousGroupes();
		List<Espece> especes = new ArrayList<Espece>();
		for(SousGroupe sg : sgs){
			List<Espece> especeDansSG = Espece.find.where().eq("espece_sous_groupe", sg).findList();
			especes.addAll(especeDansSG);
		}
		return especes;
	}
	
	/**
	 * Supprimer le groupe de la base de données et toutes ses références
	 * dans les autres tables
	 */
	public void supprimer() {
		List<SousGroupe> sous_groupes = SousGroupe.find.where().eq("sous_groupe_groupe", this).findList();
		for(SousGroupe sous_groupe : sous_groupes){
			sous_groupe.supprimer();
		}
		List<MembreIsExpertOnGroupe> mieogs = MembreIsExpertOnGroupe.find.where().eq("groupe", this).findList();
		for(MembreIsExpertOnGroupe mieog : mieogs){
			mieog.delete();
		}
		List<StadeSexeHierarchieDansGroupe> sshdsgs = StadeSexeHierarchieDansGroupe.find.where().eq("groupe", this).findList();
		for(StadeSexeHierarchieDansGroupe sshdsg : sshdsgs){
			sshdsg.delete();
		}
		List<DateCharniere> dates_charnieres = DateCharniere.find.where().eq("date_charniere_groupe", this).findList();
		for(DateCharniere date_charniere : dates_charnieres){
			date_charniere.delete();
		}
		this.delete();
	}
	
}
