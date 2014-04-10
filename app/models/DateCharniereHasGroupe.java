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
public class DateCharniereHasGroupe extends Model {
	@Id
	public Integer DateCharniereHasGroupe_id;
	@NotNull
	@ManyToOne
	public DateCharniere date_charniere;
	@NotNull
	@ManyToOne
	public Groupe groupe;
	
	public static Finder<Integer,DateCharniereHasGroupe> find = new Finder<Integer,DateCharniereHasGroupe>(Integer.class, DateCharniereHasGroupe.class);

}
