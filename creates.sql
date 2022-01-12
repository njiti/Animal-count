CREATE DATABASE wildlife_tracker;
\c wildlife_tracker;
CREATE TABLE animals(id SERIAL PRIMARY KEY,name varchar, endangered boolean, age varchar);
CREATE TABLE sightings(id SERIAL PRIMARY KEY, rangername varchar , location varchar, spotted timestamp, animalid int);
CREATE DATABASE wildlife_tracker_test WITH TEMPLATE wildlifetest;