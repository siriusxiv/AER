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
	 * Renvoie la liste des espèces dans ce groupe par ordre alphabétique.
	 * @return
	 */
	public List<Espece> getEspecesInThisByAlpha(){
		List<SousGroupe> sgs = this.getSousGroupes();
		List<Espece> especes = new ArrayList<Espece>();
		for(SousGroupe sg : sgs){
			List<Espece> especeDansSG = Espece.find.where().eq("espece_sous_groupe", sg).findList();
			especes.addAll(especeDansSG);
		}
		Collections.sort(especes, new Comparator<Espece>(){
			@Override
			public int compare(Espece arg0, Espece arg1) {
				return arg0.espece_nom.compareToIgnoreCase(arg1.espece_nom);
			}
		});
		return especes;
	}
	
	/**
	 * Renvoie la liste des sous-familles dans ce groupe triées par ordre alpha.
	 * @return
	 */
	public List<SousFamilleHasSousGroupe> getSousFamilles(){
		List<SousGroupe> sgs = this.getSousGroupes();
		List<SousFamilleHasSousGroupe> sfhsgs = new ArrayList<SousFamilleHasSousGroupe>();
		for(SousGroupe sg : sgs){
			sfhsgs.addAll(sg.getSousFamilles());
		}
		Collections.sort(sfhsgs,new Comparator<SousFamilleHasSousGroupe>(){
			@Override
			public int compare(SousFamilleHasSousGroupe arg0, SousFamilleHasSousGroupe arg1) {
				return arg0.sous_famille.sous_famille_nom.compareTo(arg1.sous_famille.sous_famille_nom);
			}
		});
		return sfhsgs;
	}
	/**
	 * Renvoie la liste des familles dans ce groupe triées par ordre alpha.
	 * @return
	 */
	public List<FamilleHasSousGroupe> getFamilles(){
		List<SousGroupe> sgs = this.getSousGroupes();
		List<FamilleHasSousGroupe> fhsgs = new ArrayList<FamilleHasSousGroupe>();
		for(SousGroupe sg : sgs){
			fhsgs.addAll(sg.getFamilles());
		}
		Collections.sort(fhsgs,new Comparator<FamilleHasSousGroupe>(){
			@Override
			public int compare(FamilleHasSousGroupe arg0, FamilleHasSousGroupe arg1) {
				return arg0.famille.famille_nom.compareTo(arg1.famille.famille_nom);
			}
		});
		return fhsgs;
	}
	/**
	 * Renvoie la liste des super-familles dans ce groupe triées par ordre alpha.
	 * @return
	 */
	public List<SuperFamilleHasSousGroupe> getSuperFamilles(){
		List<SousGroupe> sgs = this.getSousGroupes();
		List<SuperFamilleHasSousGroupe> sfhsgs = new ArrayList<SuperFamilleHasSousGroupe>();
		for(SousGroupe sg : sgs){
			sfhsgs.addAll(sg.getSuperFamilles());
		}
		Collections.sort(sfhsgs,new Comparator<SuperFamilleHasSousGroupe>(){
			@Override
			public int compare(SuperFamilleHasSousGroupe arg0, SuperFamilleHasSousGroupe arg1) {
				return arg0.super_famille.super_famille_nom.compareTo(arg1.super_famille.super_famille_nom);
			}
		});
		return sfhsgs;
	}
	/**
	 * Renvoie la liste des ordres dans ce groupe triés par ordre alpha.
	 * @return
	 */
	public List<OrdreHasSousGroupe> getOrdres(){
		List<SousGroupe> sgs = this.getSousGroupes();
		List<OrdreHasSousGroupe> ohsgs = new ArrayList<OrdreHasSousGroupe>();
		for(SousGroupe sg : sgs){
			ohsgs.addAll(sg.getOrdres());
		}
		Collections.sort(ohsgs,new Comparator<OrdreHasSousGroupe>(){
			@Override
			public int compare(OrdreHasSousGroupe arg0, OrdreHasSousGroupe arg1) {
				return arg0.ordre.ordre_nom.compareTo(arg1.ordre.ordre_nom);
			}
		});
		return ohsgs;
	}
	
	/**
	* Renvoie la liste complète des ordres contenus dans le groupe
	* @return
	*/
	public List<Ordre> getAllOrdres(){
		List<SousGroupe> sgs = this.getSousGroupes();
		List<Ordre> ordres = new ArrayList<Ordre>();
		for(SousGroupe sg : sgs){
			List<OrdreHasSousGroupe> ohsgs = sg.getOrdres();
			for(OrdreHasSousGroupe ohsg : ohsgs) {
				ordres.add(ohsg.ordre);
			}
		}
		return ordres;
	}
	
	/**
	* Renvoie la liste complète des super-familles contenues dans le groupe
	* @return
	*/
	public List<SuperFamille> getAllSuperFamilles(){
		List<SousGroupe> sgs = this.getSousGroupes();
		List<Ordre> ordres = this.getAllOrdres();
		List<SuperFamille> superfams = new ArrayList<SuperFamille>();
		for(Ordre ordre : ordres){
			List<SuperFamille> supFamsDsOrdres = SuperFamille.find.where().eq("super_famille_ordre",ordre).eq("super_famille_existe",true).findList();
			superfams.addAll(supFamsDsOrdres);}
		for(SousGroupe sg : sgs){
			List<SuperFamilleHasSousGroupe> sfhsgs = sg.getSuperFamilles();
			for(SuperFamilleHasSousGroupe sfhsg : sfhsgs){
				superfams.add(sfhsg.super_famille);
			}
		}
		return superfams;
	}
	
	/**
	* Renvoie la liste complète les familles contenues dans le groupes
	* @return
	*/
	public List<Famille> getAllFamilles(){
		List<SousGroupe> sgs = this.getSousGroupes();
		List<Ordre> ordres = this.getAllOrdres();
		List<SuperFamille> superfams = new ArrayList<SuperFamille>();
		for(SousGroupe sg : sgs){
			List<SuperFamilleHasSousGroupe> sfhsgs = sg.getSuperFamilles();
			for(SuperFamilleHasSousGroupe sfhsg : sfhsgs){
				superfams.add(sfhsg.super_famille);
			}
		}
		List<Famille> fams = new ArrayList<Famille>();
		for(SuperFamille superfam : superfams){
			List<Famille> famsDsSuperFams = Famille.find.where().eq("famille_super_famille",superfam).findList();
			fams.addAll(famsDsSuperFams);}
		for(Ordre ordre : ordres){
			List<Famille> famsDsOrdres = Famille.find.where().eq("famille_super_famille.super_famille_ordre",ordre).findList();
			fams.addAll(famsDsOrdres);}
		for(SousGroupe sg : sgs){
			List<FamilleHasSousGroupe> fhsgs = sg.getFamilles();
			for(FamilleHasSousGroupe fhsg : fhsgs){
				fams.add(fhsg.famille);
			}
		}
		return fams;
	}
			
	
	/** 
	* Renvoie la liste complète des sous-familles contenues dans le groupe
	* @return
	 */
	 public List<SousFamille> getAllSousFamilles(){
	 	 List<SousGroupe> sgs = this.getSousGroupes();
	 	 List<Famille> fams = this.getAllFamilles();
	 	 List<SousFamille> sousfams = new ArrayList<SousFamille>();
	 	 for(Famille fam : fams){
	 	 	 List<SousFamille> ssFamsDsFams = SousFamille.find.where().eq("sous_famille_famille",fam).eq("sous_famille_existe",true).findList();
	 	 	sousfams.addAll(ssFamsDsFams);}
	 	 for(SousGroupe sg : sgs){
	 	 	List<SousFamilleHasSousGroupe> ssfhsgs = sg.getSousFamilles();
	 	 	for(SousFamilleHasSousGroupe ssfhsg : ssfhsgs){
	 	 		sousfams.add(ssfhsg.sous_famille);
	 	 	}
	 	 }
	 	 return sousfams;
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
