# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

INSERT INTO `departement` (`departement_code`, `departement_nom`, `departement_nom_uppercase`, `departement_slug`, `departement_nom_soundex`) VALUES
('17', 'Charente-Maritime', 'CHARENTE-MARITIME', 'charente-maritime', 'C6535635'),
('35', 'Ile-et-Vilaine', 'ILE-ET-VILAINE', 'ile-et-vilaine', 'I43145'),
('44', 'Loire-Atlantique', 'LOIRE-ATLANTIQUE', 'loire-atlantique', 'L634532'),
('49', 'Maine-et-Loire', 'MAINE-ET-LOIRE', 'maine-et-loire', 'M346'),
('53', 'Mayenne', 'MAYENNE', 'mayenne', 'M000'),
('56', 'Morbihan', 'MORBIHAN', 'morbihan', 'M615'),
('79', 'Deux-Sèvres', 'DEUX-SÈVRES', 'deux-sevres', 'D2162'),
('85', 'Vendée', 'VENDÉE', 'vendee', 'V530');

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

DELETE FROM departement;

SET FOREIGN_KEY_CHECKS=1;


