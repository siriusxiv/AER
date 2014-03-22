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
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class StadeSexeHierarchieDansSousGroupe extends Model {
	@Id
	public Integer StadeSexeHierarchieDansSousGroupe_id;
	@NotNull
	@ManyToOne
	public StadeSexe stade_sexe;
	@NotNull
	@ManyToOne
	public SousGroupe sous_groupe;
	@ManyToOne
	public StadeSexe stade_sexe_pere;

	public static Finder<Integer,StadeSexeHierarchieDansSousGroupe> find = new Finder<Integer,StadeSexeHierarchieDansSousGroupe>(Integer.class, StadeSexeHierarchieDansSousGroupe.class);
}
