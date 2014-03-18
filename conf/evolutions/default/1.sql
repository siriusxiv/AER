# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table commune (
  ville_id                  MEDIUMINT(8) UNSIGNED auto_increment not null,
  ville_departement_departement_id INT(11),
  ville_slug                varchar(255),
  ville_nom                 VARCHAR(45),
  ville_nom_reel            VARCHAR(45),
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

create table departement (
  departement_id            INT(11) auto_increment not null,
  departement_code          VARCHAR(3),
  departement_nom           varchar(255),
  departement_nom_uppercase varchar(255),
  departement_slug          varchar(255),
  departement_nom_soundex   VARCHAR(20),
  constraint pk_departement primary key (departement_id))
;

create table droits (
  droits                    varchar(255) not null,
  constraint pk_droits primary key (droits))
;

create table fiche (
  fiche_id                  bigint auto_increment not null,
  fiche_lieudit             varchar(255),
  fiche_date_min            datetime,
  fiche_date                datetime,
  fiche_memo                TEXT,
  fiche_utm_utm             VARCHAR(4),
  fiche_commune_ville_id    MEDIUMINT(8) UNSIGNED,
  fiche_gps_coordinates     varchar(255),
  utm1x1                    varchar(255),
  vue                       tinyint(1) default 0,
  validee                   tinyint(1) default 0,
  constraint pk_fiche primary key (fiche_id))
;

create table utms (
  utm                       VARCHAR(4) not null,
  maille20x20               VARCHAR(4),
  maille50x50               VARCHAR(4),
  maille100x100             VARCHAR(4),
  constraint pk_utms primary key (utm))
;

alter table commune add constraint fk_commune_ville_departement_1 foreign key (ville_departement_departement_id) references departement (departement_id) on delete restrict on update restrict;
create index ix_commune_ville_departement_1 on commune (ville_departement_departement_id);
alter table fiche add constraint fk_fiche_fiche_utm_2 foreign key (fiche_utm_utm) references utms (utm) on delete restrict on update restrict;
create index ix_fiche_fiche_utm_2 on fiche (fiche_utm_utm);
alter table fiche add constraint fk_fiche_fiche_commune_3 foreign key (fiche_commune_ville_id) references commune (ville_id) on delete restrict on update restrict;
create index ix_fiche_fiche_commune_3 on fiche (fiche_commune_ville_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table commune;

drop table departement;

drop table droits;

drop table fiche;

drop table utms;

SET FOREIGN_KEY_CHECKS=1;

