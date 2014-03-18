# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table commune (
  ville_id                  MEDIUMINT(8) UNSIGNED not null,
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
  departement_id            INT(11) not null,
  departement_code          VARCHAR(3),
  departement_nom           varchar(255),
  departement_nom_uppercase varchar(255),
  departement_slug          varchar(255),
  departement_nom_soundex   VARCHAR(20),
  constraint pk_departement primary key (departement_id))
;

create table utms (
  utm                       VARCHAR(4) not null,
  maille20x20               VARCHAR(4),
  maille50x50               VARCHAR(4),
  maille100x100             VARCHAR(4),
  constraint pk_utms primary key (utm))
;

create sequence commune_seq;

create sequence departement_seq;

create sequence utms_seq;

alter table commune add constraint fk_commune_ville_departement_1 foreign key (ville_departement_departement_id) references departement (departement_id) on delete restrict on update restrict;
create index ix_commune_ville_departement_1 on commune (ville_departement_departement_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists commune;

drop table if exists departement;

drop table if exists utms;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists commune_seq;

drop sequence if exists departement_seq;

drop sequence if exists utms_seq;

