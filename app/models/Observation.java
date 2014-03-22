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

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import play.db.ebean.Model;

/**
 * Une observation contient 3 niveaux de validation :
 * observation_validee=0 -> Non validée (l'observation soit n'a pas encore été vue et dans ce cas,
 * 							on attend qu'un expert la voie, soit elle a déjà été vue et
 * 							dans ce cas l'expert l'a rejetée)
 * observation_validee=1 -> En suspend (il y a un truc qui cloche ou à vérifier avant de valider)
 * observation_validee=2 -> Validée
 * @author malik
 *
 */
@SuppressWarnings("serial")
@Entity
public class Observation extends Model {
	@Id
	public Long observation_id;
	@NotNull
	@ManyToOne
	public Espece observation_espece;
	@NotNull
	@ManyToOne
	public Fiche observation_fiche;
	public String observation_determinateur;
	@Column(columnDefinition="TEXT")
	public String observation_commentaires;
	@NotNull
	public boolean obervation_vue_par_expert;
	@NotNull
	@Column(columnDefinition="TINYINT")
	public Integer observation_validee;
	@NotNull
	public Calendar observation_date_derniere_modification;
	public Calendar observation_date_validation;
	
	public static Finder<Long,Observation> find = new Finder<Long,Observation>(Long.class, Observation.class);

	@Override
	public String toString(){
		return observation_espece+" "+observation_fiche;
	}
}
