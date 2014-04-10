# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table commune (
  ville_id                  MEDIUMINT(8) UNSIGNED auto_increment not null,
  ville_departement_departement_code VARCHAR(3),
  ville_slug                varchar(255),
  ville_nom                 VARCHAR(45) not null,
  ville_nom_reel            VARCHAR(45) not null,
  ville_nom_soundex         VARCHAR(20),
  ville_nom_metaphone       VARCHAR(22),
  ville_code_postal         varchar(255),
  ville_commune             VARCHAR(3),
  ville_code_commune        VARCHAR(5),
  ville_arrondissement      SMALLINT(3) UNSIGNED,
  ville_canton              VARCHAR(4),
  ville_amdi                SMALLINT(5) UNSIGNED,
  ville_population_2010     MEDIUMINT(11) UNSIGNED,
  ville_population_1999     MEDIUMINT(11) UNSIGNED,
  ville_population_2012     MEDIUMINT(10) UNSIGNED,
  ville_densite_2010        INT(11) UNSIGNED,
  ville_surface             MEDIUMINT(7) UNSIGNED,
  ville_longitude_deg       FLOAT,
  ville_latitude_deg        FLOAT,
  ville_longitude_grd       VARCHAR(9),
  ville_latitude_grd        VARCHAR(8),
  ville_longitude_dms       VARCHAR(9),
  ville_latitude_dms        VARCHAR(8),
  ville_zmin                MEDIUMINT(4),
  ville_zmax                MEDIUMINT(4),
  ville_population_2010_order_france INT(10) UNSIGNED,
  ville_densite_2010_order_france INT(10) UNSIGNED,
  ville_surface_order_france INT(10) UNSIGNED,
  constraint pk_commune primary key (ville_id))
;

create table confidentialite (
  confidentialite_id        integer auto_increment not null,
  confidentialite_intitule  varchar(255) not null,
  confidentialite_explication TEXT not null,
  constraint pk_confidentialite primary key (confidentialite_id))
;

create table date_charniere (
  date_charniere_id         integer auto_increment not null,
  date_charniere_date       datetime not null,
  date_charniere_groupe_groupe_id integer not null,
  constraint pk_date_charniere primary key (date_charniere_id))
;

create table departement (
  departement_code          VARCHAR(3) not null,
  departement_nom           varchar(255),
  departement_nom_uppercase varchar(255),
  departement_slug          varchar(255),
  departement_nom_soundex   VARCHAR(20),
  constraint pk_departement primary key (departement_code))
;

create table droits (
  droits_id                 integer auto_increment not null,
  droits_intitule           varchar(255) not null,
  constraint pk_droits primary key (droits_id))
;

create table espece (
  espece_id                 integer auto_increment not null,
  espece_nom                varchar(255) not null,
  espece_auteur             varchar(255) not null,
  espece_systematique       integer not null,
  espece_photo              varchar(255),
  espece_sous_groupe_sous_groupe_id integer,
  espece_sousfamille_sous_famille_id integer not null,
  espece_commentaires       TEXT,
  constraint pk_espece primary key (espece_id))
;

create table espece_has_sous_groupe (
  espece_has_sous_groupe_id integer auto_increment not null,
  espece_espece_id          integer not null,
  sous_groupe_sous_groupe_id integer not null,
  constraint pk_espece_has_sous_groupe primary key (espece_has_sous_groupe_id))
;

create table espece_synonyme (
  synonyme_id               integer auto_increment not null,
  synonyme_nom              varchar(255) not null,
  synonyme_origine_aer      tinyint(1) default 0 not null,
  synonyme_espece_espece_id integer not null,
  constraint pk_espece_synonyme primary key (synonyme_id))
;

create table famille (
  famille_id                integer auto_increment not null,
  famille_nom               varchar(255) not null,
  famille_super_famille_super_famille_id integer not null,
  constraint pk_famille primary key (famille_id))
;

create table famille_has_sous_groupe (
  famille_has_sous_groupe_id integer auto_increment not null,
  famille_famille_id        integer not null,
  sous_groupe_sous_groupe_id integer not null,
  constraint pk_famille_has_sous_groupe primary key (famille_has_sous_groupe_id))
;

create table fiche (
  fiche_id                  bigint auto_increment not null,
  fiche_lieudit             varchar(255),
  fiche_date_min            datetime,
  fiche_date                datetime not null,
  fiche_memo                TEXT,
  fiche_utm_utm             VARCHAR(4) not null,
  fiche_commune_ville_id    MEDIUMINT(8) UNSIGNED,
  fiche_gps_coordinates     varchar(255),
  fiche_utm1x1              varchar(255),
  fiche_date_soumission     datetime not null,
  constraint pk_fiche primary key (fiche_id))
;

create table fiche_has_membre (
  fiche_has_membre_id       integer auto_increment not null,
  membre_membre_id          integer not null,
  fiche_fiche_id            bigint not null,
  constraint pk_fiche_has_membre primary key (fiche_has_membre_id))
;

create table groupe (
  groupe_id                 integer auto_increment not null,
  groupe_nom                varchar(255) not null,
  constraint pk_groupe primary key (groupe_id))
;

create table groupe_de_membres (
  groupe_de_membres_id      integer auto_increment not null,
  groupe_de_membres_nom     varchar(255) not null,
  constraint pk_groupe_de_membres primary key (groupe_de_membres_id))
;

create table informations_complementaires (
  informations_complementaires_id bigint auto_increment not null,
  informations_complementaires_observation_observation_id bigint not null,
  informations_complementaires_nombre_de_specimens INT UNSIGNED,
  informations_complementaires_stade_sexe_stade_sexe_id integer,
  constraint pk_informations_complementaires primary key (informations_complementaires_id))
;

create table membre (
  membre_id                 integer auto_increment not null,
  membre_civilite           varchar(255),
  membre_nom                varchar(255) not null,
  membre_adresse            varchar(255),
  membre_adresse_complement varchar(255),
  membre_code_postal        varchar(255),
  membre_ville              varchar(255),
  membre_pays               varchar(255),
  membre_confidentialite_confidentialite_id integer not null,
  membre_abonne             tinyint(1) default 0 not null,
  membre_temoin_actif       tinyint(1) default 0 not null,
  membre_journais           integer,
  membre_moisnais           integer,
  membre_annenais           integer,
  membre_jourdece           integer,
  membre_moisdece           integer,
  membre_annedece           integer,
  membre_biographie         TEXT,
  membre_email              varchar(255),
  membre_mdp_hash           varchar(255),
  membre_sel                varchar(255),
  membre_droits_droits_id   integer not null,
  membre_inscription_acceptee tinyint(1) default 0 not null,
  membre_lien_de_validation_de_mail varchar(255),
  constraint pk_membre primary key (membre_id))
;

create table membre_has_groupe_de_membres (
  membre_has_groupe_de_membres_id integer auto_increment not null,
  membre_membre_id          integer not null,
  groupe_de_membres_groupe_de_membres_id integer not null,
  constraint pk_membre_has_groupe_de_membres primary key (membre_has_groupe_de_membres_id))
;

create table membre_is_expert_on_groupe (
  membre_is_expert_on_groupe_id integer auto_increment not null,
  membre_membre_id          integer not null,
  groupe_groupe_id          integer not null,
  constraint pk_membre_is_expert_on_groupe primary key (membre_is_expert_on_groupe_id))
;

create table observation (
  observation_id            bigint auto_increment not null,
  observation_espece_espece_id integer not null,
  observation_fiche_fiche_id bigint not null,
  observation_determinateur varchar(255),
  observation_commentaires  TEXT,
  observation_vue_par_expert tinyint(1) default 0 not null,
  observation_validee       TINYINT not null,
  observation_date_derniere_modification datetime not null,
  observation_date_validation datetime,
  constraint pk_observation primary key (observation_id))
;

create table ordre (
  ordre_id                  integer auto_increment not null,
  ordre_nom                 varchar(255) not null,
  constraint pk_ordre primary key (ordre_id))
;

create table ordre_has_sous_groupe (
  ordre_has_sous_groupe_id  integer auto_increment not null,
  ordre_ordre_id            integer not null,
  sous_groupe_sous_groupe_id integer not null,
  constraint pk_ordre_has_sous_groupe primary key (ordre_has_sous_groupe_id))
;

create table sous_famille (
  sous_famille_id           integer auto_increment not null,
  sous_famille_nom          varchar(255) not null,
  sous_famille_existe       tinyint(1) default 0 not null,
  sous_famille_famille_famille_id integer not null,
  constraint pk_sous_famille primary key (sous_famille_id))
;

create table sous_famille_has_sous_groupe (
  sous_famille_has_sous_groupe_id integer auto_increment not null,
  sous_famille_sous_famille_id integer not null,
  sous_groupe_sous_groupe_id integer not null,
  constraint pk_sous_famille_has_sous_groupe primary key (sous_famille_has_sous_groupe_id))
;

create table sous_groupe (
  sous_groupe_id            integer auto_increment not null,
  sous_groupe_nom           varchar(255) not null,
  sous_groupe_groupe_groupe_id integer not null,
  constraint pk_sous_groupe primary key (sous_groupe_id))
;

create table stade_sexe (
  stade_sexe_id             integer auto_increment not null,
  stade_sexe_intitule       varchar(255) not null,
  stade_sexe_explication    TEXT,
  constraint pk_stade_sexe primary key (stade_sexe_id))
;

create table stade_sexe_hierarchie_dans_groupe (
  stade_sexe_hierarchie_dans_groupe_id integer auto_increment not null,
  stade_sexe_stade_sexe_id  integer not null,
  groupe_groupe_id          integer not null,
  stade_sexe_pere_stade_sexe_id integer,
  position                  integer not null,
  constraint pk_stade_sexe_hierarchie_dans_groupe primary key (stade_sexe_hierarchie_dans_groupe_id))
;

create table super_famille (
  super_famille_id          integer auto_increment not null,
  super_famille_nom         varchar(255) not null,
  super_famille_existe      tinyint(1) default 0 not null,
  super_famille_ordre_ordre_id integer not null,
  constraint pk_super_famille primary key (super_famille_id))
;

create table super_famille_has_sous_groupe (
  super_famille_has_sous_groupe_id integer auto_increment not null,
  super_famille_super_famille_id integer not null,
  sous_groupe_sous_groupe_id integer not null,
  constraint pk_super_famille_has_sous_groupe primary key (super_famille_has_sous_groupe_id))
;

create table utms (
  utm                       VARCHAR(4) not null,
  maille20x20               VARCHAR(4),
  maille50x50               VARCHAR(4),
  maille100x100             VARCHAR(4),
  constraint pk_utms primary key (utm))
;

alter table commune add constraint fk_commune_ville_departement_1 foreign key (ville_departement_departement_code) references departement (departement_code) on delete restrict on update restrict;
create index ix_commune_ville_departement_1 on commune (ville_departement_departement_code);
alter table date_charniere add constraint fk_date_charniere_date_charniere_groupe_2 foreign key (date_charniere_groupe_groupe_id) references groupe (groupe_id) on delete restrict on update restrict;
create index ix_date_charniere_date_charniere_groupe_2 on date_charniere (date_charniere_groupe_groupe_id);
alter table espece add constraint fk_espece_espece_sous_groupe_3 foreign key (espece_sous_groupe_sous_groupe_id) references sous_groupe (sous_groupe_id) on delete restrict on update restrict;
create index ix_espece_espece_sous_groupe_3 on espece (espece_sous_groupe_sous_groupe_id);
alter table espece add constraint fk_espece_espece_sousfamille_4 foreign key (espece_sousfamille_sous_famille_id) references sous_famille (sous_famille_id) on delete restrict on update restrict;
create index ix_espece_espece_sousfamille_4 on espece (espece_sousfamille_sous_famille_id);
alter table espece_has_sous_groupe add constraint fk_espece_has_sous_groupe_espece_5 foreign key (espece_espece_id) references espece (espece_id) on delete restrict on update restrict;
create index ix_espece_has_sous_groupe_espece_5 on espece_has_sous_groupe (espece_espece_id);
alter table espece_has_sous_groupe add constraint fk_espece_has_sous_groupe_sous_groupe_6 foreign key (sous_groupe_sous_groupe_id) references sous_groupe (sous_groupe_id) on delete restrict on update restrict;
create index ix_espece_has_sous_groupe_sous_groupe_6 on espece_has_sous_groupe (sous_groupe_sous_groupe_id);
alter table espece_synonyme add constraint fk_espece_synonyme_synonyme_espece_7 foreign key (synonyme_espece_espece_id) references espece (espece_id) on delete restrict on update restrict;
create index ix_espece_synonyme_synonyme_espece_7 on espece_synonyme (synonyme_espece_espece_id);
alter table famille add constraint fk_famille_famille_super_famille_8 foreign key (famille_super_famille_super_famille_id) references super_famille (super_famille_id) on delete restrict on update restrict;
create index ix_famille_famille_super_famille_8 on famille (famille_super_famille_super_famille_id);
alter table famille_has_sous_groupe add constraint fk_famille_has_sous_groupe_famille_9 foreign key (famille_famille_id) references famille (famille_id) on delete restrict on update restrict;
create index ix_famille_has_sous_groupe_famille_9 on famille_has_sous_groupe (famille_famille_id);
alter table famille_has_sous_groupe add constraint fk_famille_has_sous_groupe_sous_groupe_10 foreign key (sous_groupe_sous_groupe_id) references sous_groupe (sous_groupe_id) on delete restrict on update restrict;
create index ix_famille_has_sous_groupe_sous_groupe_10 on famille_has_sous_groupe (sous_groupe_sous_groupe_id);
alter table fiche add constraint fk_fiche_fiche_utm_11 foreign key (fiche_utm_utm) references utms (utm) on delete restrict on update restrict;
create index ix_fiche_fiche_utm_11 on fiche (fiche_utm_utm);
alter table fiche add constraint fk_fiche_fiche_commune_12 foreign key (fiche_commune_ville_id) references commune (ville_id) on delete restrict on update restrict;
create index ix_fiche_fiche_commune_12 on fiche (fiche_commune_ville_id);
alter table fiche_has_membre add constraint fk_fiche_has_membre_membre_13 foreign key (membre_membre_id) references membre (membre_id) on delete restrict on update restrict;
create index ix_fiche_has_membre_membre_13 on fiche_has_membre (membre_membre_id);
alter table fiche_has_membre add constraint fk_fiche_has_membre_fiche_14 foreign key (fiche_fiche_id) references fiche (fiche_id) on delete restrict on update restrict;
create index ix_fiche_has_membre_fiche_14 on fiche_has_membre (fiche_fiche_id);
alter table informations_complementaires add constraint fk_informations_complementaires_informations_complementaires__15 foreign key (informations_complementaires_observation_observation_id) references observation (observation_id) on delete restrict on update restrict;
create index ix_informations_complementaires_informations_complementaires__15 on informations_complementaires (informations_complementaires_observation_observation_id);
alter table informations_complementaires add constraint fk_informations_complementaires_informations_complementaires__16 foreign key (informations_complementaires_stade_sexe_stade_sexe_id) references stade_sexe (stade_sexe_id) on delete restrict on update restrict;
create index ix_informations_complementaires_informations_complementaires__16 on informations_complementaires (informations_complementaires_stade_sexe_stade_sexe_id);
alter table membre add constraint fk_membre_membre_confidentialite_17 foreign key (membre_confidentialite_confidentialite_id) references confidentialite (confidentialite_id) on delete restrict on update restrict;
create index ix_membre_membre_confidentialite_17 on membre (membre_confidentialite_confidentialite_id);
alter table membre add constraint fk_membre_membre_droits_18 foreign key (membre_droits_droits_id) references droits (droits_id) on delete restrict on update restrict;
create index ix_membre_membre_droits_18 on membre (membre_droits_droits_id);
alter table membre_has_groupe_de_membres add constraint fk_membre_has_groupe_de_membres_membre_19 foreign key (membre_membre_id) references membre (membre_id) on delete restrict on update restrict;
create index ix_membre_has_groupe_de_membres_membre_19 on membre_has_groupe_de_membres (membre_membre_id);
alter table membre_has_groupe_de_membres add constraint fk_membre_has_groupe_de_membres_groupe_de_membres_20 foreign key (groupe_de_membres_groupe_de_membres_id) references groupe_de_membres (groupe_de_membres_id) on delete restrict on update restrict;
create index ix_membre_has_groupe_de_membres_groupe_de_membres_20 on membre_has_groupe_de_membres (groupe_de_membres_groupe_de_membres_id);
alter table membre_is_expert_on_groupe add constraint fk_membre_is_expert_on_groupe_membre_21 foreign key (membre_membre_id) references membre (membre_id) on delete restrict on update restrict;
create index ix_membre_is_expert_on_groupe_membre_21 on membre_is_expert_on_groupe (membre_membre_id);
alter table membre_is_expert_on_groupe add constraint fk_membre_is_expert_on_groupe_groupe_22 foreign key (groupe_groupe_id) references groupe (groupe_id) on delete restrict on update restrict;
create index ix_membre_is_expert_on_groupe_groupe_22 on membre_is_expert_on_groupe (groupe_groupe_id);
alter table observation add constraint fk_observation_observation_espece_23 foreign key (observation_espece_espece_id) references espece (espece_id) on delete restrict on update restrict;
create index ix_observation_observation_espece_23 on observation (observation_espece_espece_id);
alter table observation add constraint fk_observation_observation_fiche_24 foreign key (observation_fiche_fiche_id) references fiche (fiche_id) on delete restrict on update restrict;
create index ix_observation_observation_fiche_24 on observation (observation_fiche_fiche_id);
alter table ordre_has_sous_groupe add constraint fk_ordre_has_sous_groupe_ordre_25 foreign key (ordre_ordre_id) references ordre (ordre_id) on delete restrict on update restrict;
create index ix_ordre_has_sous_groupe_ordre_25 on ordre_has_sous_groupe (ordre_ordre_id);
alter table ordre_has_sous_groupe add constraint fk_ordre_has_sous_groupe_sous_groupe_26 foreign key (sous_groupe_sous_groupe_id) references sous_groupe (sous_groupe_id) on delete restrict on update restrict;
create index ix_ordre_has_sous_groupe_sous_groupe_26 on ordre_has_sous_groupe (sous_groupe_sous_groupe_id);
alter table sous_famille add constraint fk_sous_famille_sous_famille_famille_27 foreign key (sous_famille_famille_famille_id) references famille (famille_id) on delete restrict on update restrict;
create index ix_sous_famille_sous_famille_famille_27 on sous_famille (sous_famille_famille_famille_id);
alter table sous_famille_has_sous_groupe add constraint fk_sous_famille_has_sous_groupe_sous_famille_28 foreign key (sous_famille_sous_famille_id) references sous_famille (sous_famille_id) on delete restrict on update restrict;
create index ix_sous_famille_has_sous_groupe_sous_famille_28 on sous_famille_has_sous_groupe (sous_famille_sous_famille_id);
alter table sous_famille_has_sous_groupe add constraint fk_sous_famille_has_sous_groupe_sous_groupe_29 foreign key (sous_groupe_sous_groupe_id) references sous_groupe (sous_groupe_id) on delete restrict on update restrict;
create index ix_sous_famille_has_sous_groupe_sous_groupe_29 on sous_famille_has_sous_groupe (sous_groupe_sous_groupe_id);
alter table sous_groupe add constraint fk_sous_groupe_sous_groupe_groupe_30 foreign key (sous_groupe_groupe_groupe_id) references groupe (groupe_id) on delete restrict on update restrict;
create index ix_sous_groupe_sous_groupe_groupe_30 on sous_groupe (sous_groupe_groupe_groupe_id);
alter table stade_sexe_hierarchie_dans_groupe add constraint fk_stade_sexe_hierarchie_dans_groupe_stade_sexe_31 foreign key (stade_sexe_stade_sexe_id) references stade_sexe (stade_sexe_id) on delete restrict on update restrict;
create index ix_stade_sexe_hierarchie_dans_groupe_stade_sexe_31 on stade_sexe_hierarchie_dans_groupe (stade_sexe_stade_sexe_id);
alter table stade_sexe_hierarchie_dans_groupe add constraint fk_stade_sexe_hierarchie_dans_groupe_groupe_32 foreign key (groupe_groupe_id) references groupe (groupe_id) on delete restrict on update restrict;
create index ix_stade_sexe_hierarchie_dans_groupe_groupe_32 on stade_sexe_hierarchie_dans_groupe (groupe_groupe_id);
alter table stade_sexe_hierarchie_dans_groupe add constraint fk_stade_sexe_hierarchie_dans_groupe_stade_sexe_pere_33 foreign key (stade_sexe_pere_stade_sexe_id) references stade_sexe (stade_sexe_id) on delete restrict on update restrict;
create index ix_stade_sexe_hierarchie_dans_groupe_stade_sexe_pere_33 on stade_sexe_hierarchie_dans_groupe (stade_sexe_pere_stade_sexe_id);
alter table super_famille add constraint fk_super_famille_super_famille_ordre_34 foreign key (super_famille_ordre_ordre_id) references ordre (ordre_id) on delete restrict on update restrict;
create index ix_super_famille_super_famille_ordre_34 on super_famille (super_famille_ordre_ordre_id);
alter table super_famille_has_sous_groupe add constraint fk_super_famille_has_sous_groupe_super_famille_35 foreign key (super_famille_super_famille_id) references super_famille (super_famille_id) on delete restrict on update restrict;
create index ix_super_famille_has_sous_groupe_super_famille_35 on super_famille_has_sous_groupe (super_famille_super_famille_id);
alter table super_famille_has_sous_groupe add constraint fk_super_famille_has_sous_groupe_sous_groupe_36 foreign key (sous_groupe_sous_groupe_id) references sous_groupe (sous_groupe_id) on delete restrict on update restrict;
create index ix_super_famille_has_sous_groupe_sous_groupe_36 on super_famille_has_sous_groupe (sous_groupe_sous_groupe_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table commune;

drop table confidentialite;

drop table date_charniere;

drop table departement;

drop table droits;

drop table espece;

drop table espece_has_sous_groupe;

drop table espece_synonyme;

drop table famille;

drop table famille_has_sous_groupe;

drop table fiche;

drop table fiche_has_membre;

drop table groupe;

drop table groupe_de_membres;

drop table informations_complementaires;

drop table membre;

drop table membre_has_groupe_de_membres;

drop table membre_is_expert_on_groupe;

drop table observation;

drop table ordre;

drop table ordre_has_sous_groupe;

drop table sous_famille;

drop table sous_famille_has_sous_groupe;

drop table sous_groupe;

drop table stade_sexe;

drop table stade_sexe_hierarchie_dans_groupe;

drop table super_famille;

drop table super_famille_has_sous_groupe;

drop table utms;

SET FOREIGN_KEY_CHECKS=1;

