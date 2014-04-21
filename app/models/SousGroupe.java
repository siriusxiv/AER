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

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import play.db.ebean.Model;

/**
 * SousGroupe est un ensemble d'inscte qui se "ressemblent".
 * Attention, cette discrimination n'a rien de scientifique,
 * elle a été faite par l'AER dans le but d'allouer un groupe
 * d'insectes à un expert.
 * Un Groupe est sous la juridiction d'un expert.
 * @author malik
 *
 */
@SuppressWarnings("serial")
@Entity
public class SousGroupe extends Model {
	@Id
	public Integer sous_groupe_id;
	@NotNull
	public String sous_groupe_nom;
	@NotNull
	@ManyToOne
	public Groupe sous_groupe_groupe;
	
	public static Finder<Integer,SousGroupe> find = new Finder<Integer,SousGroupe>(Integer.class, SousGroupe.class);

	public SousGroupe(String sous_groupe_nom, Groupe groupe) {
		this.sous_groupe_nom=sous_groupe_nom;
		this.sous_groupe_groupe=groupe;
	}

	public static List<SousGroupe> findAll(){
		return find.findList();
	}
	
	@Override
	public String toString(){
		return sous_groupe_nom;
	}
	
	/**
	 * Renvoie les ordres contenus dans ce sous-groupe
	 * @return
	 */
	public List<OrdreHasSousGroupe> getOrdres(){
		return OrdreHasSousGroupe.find.where().eq("sous_groupe", this).findList();
	}
	/**
	 * Renvoie les super-familles (dont l'ordre n'est pas dans ce sous-groupe)
	 * contenus dans ce sous-groupe
	 * @return
	 */
	public List<SuperFamilleHasSousGroupe> getSuperFamilles(){
		return SuperFamilleHasSousGroupe.find.where().eq("sous_groupe", this).findList();
	}
	/**
	 * Renvoie les familles (dont la super-famille n'est pas dans ce sous-groupe)
	 * contenus dans ce sous-groupe
	 * @return
	 */
	public List<FamilleHasSousGroupe> getFamilles(){
		return FamilleHasSousGroupe.find.where().eq("sous_groupe", this).findList();
	}
	/**
	 * Renvoie les sous-familles (dont la famille n'est pas dans ce sous-groupe)
	 * contenus dans ce sous-groupe
	 * @return
	 */
	public List<SousFamilleHasSousGroupe> getSousFamilles(){
		return SousFamilleHasSousGroupe.find.where().eq("sous_groupe", this).findList();
	}
	/**
	 * Renvoie les espèces (dont la sous-famille n'est pas dans ce sous-groupe)
	 * contenus dans ce sous-groupe
	 * @return
	 */
	public List<EspeceHasSousGroupe> getEspeces(){
		return EspeceHasSousGroupe.find.where().eq("sous_groupe", this).findList();
	}
	
	/**
	 * Renvoie la liste des sous-groupes sans groupe.
	 * @return
	 */
	public static List<SousGroupe> findSousGroupesSansSousGroupe(){
		return find.where().eq("sous_groupe_groupe", null).findList();
	}
	
	/**
	 * Trouve les espèces dans ce sous-groupe
	 * @return
	 */
	public List<Espece> getEspecesInThis(){
		return Espece.find.where().eq("espece_sous_groupe", this).orderBy("espece_systematique").findList();
	}
	/**
	 * Trouve les espèces dans ce sous-groupe dans l'ordre alpha
	 * @return
	 */
	public List<Espece> getEspecesInThisByAlpha(){
		return Espece.find.where().eq("espece_sous_groupe", this).orderBy("espece_nom").findList();
	}

	/**
	 * Supprimer le sous-groupe de la base de données et toutes ses références
	 * dans les autres tables
	 */
	public void supprimer() {
		List<EspeceHasSousGroupe> ehsgs = EspeceHasSousGroupe.find.where().eq("sous_groupe", this).findList();
		for(EspeceHasSousGroupe ehsg : ehsgs){
			ehsg.espece.espece_sous_groupe=null;
			ehsg.espece.save();
			ehsg.delete();
		}
		List<SousFamilleHasSousGroupe> sofhsgs = SousFamilleHasSousGroupe.find.where().eq("sous_groupe", this).findList();
		for(SousFamilleHasSousGroupe sofhsg : sofhsgs){
			for(Espece espece : sofhsg.sous_famille.getEspecesDansThis()){
				espece.espece_sous_groupe=null;
				espece.save();
			}
			sofhsg.delete();
		}
		List<FamilleHasSousGroupe> fhsgs = FamilleHasSousGroupe.find.where().eq("sous_groupe", this).findList();
		for(FamilleHasSousGroupe fhsg : fhsgs){
			for(Espece espece : fhsg.famille.getEspecesDansThis()){
				espece.espece_sous_groupe=null;
				espece.save();
			}
			fhsg.delete();
		}
		List<SuperFamilleHasSousGroupe> sufhsgs = SuperFamilleHasSousGroupe.find.where().eq("sous_groupe", this).findList();
		for(SuperFamilleHasSousGroupe sufhsg : sufhsgs){
			for(Espece espece : sufhsg.super_famille.getEspecesDansThis()){
				espece.espece_sous_groupe=null;
				espece.save();
			}
			sufhsg.delete();
		}
		List<OrdreHasSousGroupe> ohsgs = OrdreHasSousGroupe.find.where().eq("sous_groupe", this).findList();
		for(OrdreHasSousGroupe ohsg : ohsgs){
			for(Espece espece : ohsg.ordre.getEspecesDansThis()){
				espece.espece_sous_groupe=null;
				espece.save();
			}
			ohsg.delete();
		}
		this.delete();
	}
}
