# BatiCuisine

## Description

BatiCuisine est une application de gestion de projets de construction.
Cette application permet de gérer les clients, les projets, les devis 
ainsi que les coûts estimés des projets. Elle est conçue pour faciliter le suivi des projets 
de construction pour des clients professionnels et particuliers.

## Fonctionnalités

- Gestion des clients : ajouter, rechercher et afficher les informations des clients.
- Gestion des projets : création, suivi, et gestion des projets.
- Gestion des devis : génération de devis avec un montant estimé, une date d'émission et une date de validité.
- Gestion des professionnels et des particuliers : support pour les deux types de clients.
- Génération et sauvegarde des devis pour chaque projet.

## Technologies utilisées

- **Java 8** : Langage de programmation principal.
- **Maven** : Outil de gestion de projet et de dépendances.
- **PostgreSQL** : Base de données relationnelle pour stocker les clients, projets et devis.
- **JDBC** : Interface pour la connexion à la base de données PostgreSQL.
- **IntelliJ IDEA** : Environnement de développement intégré (IDE).

## Prérequis

Avant de commencer, assurez-vous d'avoir les éléments suivants installés :

- [Java 8](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)
- [Maven](https://maven.apache.org/install.html)
- [PostgreSQL](https://www.postgresql.org/download/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/)

## Installation

### 1. Cloner le dépôt

Cloner ce dépôt Git sur votre machine locale en utilisant la commande suivante :

git clone https://github.com/Douaesb/BatiCuisine.git

### 2. Configurer la base de données
Créez une base de données PostgreSQL pour l'application :

CREATE DATABASE baticuisine;

Exécutez le script SQL situé dans le dossier /database pour créer les tables nécessaires.

Modifiez le fichier /database/DatabaseConnection pour configurer l'accès à la base de données :


jdbc.url=jdbc:postgresql://localhost:5432/baticuisine
jdbc.username=VotreNomUtilisateur
jdbc.password=VotreMotDePasse

### 3. Construire le projet
À partir de la racine du projet, utilisez Maven pour construire le projet et télécharger toutes les dépendances :

mvn clean install

### 4. Exécuter le projet

Vous pouvez générer un fichier JAR avec la commande suivante via Maven :

mvn clean package

Puis exécutez le fichier JAR généré :

java -jar target/BatiCuisine-1.0-SNAPSHOT.jar

## Utilisation

### 1. Lancement de l'application
Après avoir démarré l'application, vous serez invité à choisir parmi plusieurs options pour gérer les clients, les projets et les devis. Voici un exemple d'interaction :

--- Menu Principal ---
1. Gérer les Clients
2. Gérer les Projets
3. Gérer les Devis
4. Quitter

Choisissez une option : 1
### 2. Gestion des Clients
Vous pouvez rechercher un client existant ou ajouter un nouveau client :

Chercher un client existant : Entrez le nom du client et affichez ses informations.
Ajouter un nouveau client : Entrez les informations du nouveau client (nom, adresse, téléphone, professionnel ou non).
### 3. Gestion des Projets
Chaque projet est lié à un client. Lors de la création d'un projet, vous devrez attribuer un client à ce projet. Vous pouvez également afficher et mettre à jour les projets existants.

### 4. Gestion des Devis
Pour chaque projet, vous pouvez générer un devis avec les informations suivantes :

Montant estimé
Date d'émission
Date de validité
Vous pouvez aussi choisir d'accepter ou non un devis avant de l'enregistrer.

## Structure du Projet
Le projet est structuré selon une architecture MVC :

src/main/java/org/baticuisine/entities : Contient les entités principales (Client, Project, Quote).
src/main/java/org/baticuisine/serviceImpl : Implémentation des services (gestion des clients, des projets, des devis).
src/main/java/org/baticuisine/repositoryImpl : Implémentation des interfaces du repository pour la gestion des accès à la base de données.
src/main/java/org/baticuisine/presentation : Interface utilisateur pour interagir avec le système via la console.

## Contribution
Les contributions sont les bienvenues ! Pour contribuer :

Forkez ce dépôt.
Créez une branche avec votre fonctionnalité (git checkout -b nouvelle-fonctionnalité).
Committez vos modifications (git commit -m 'Ajout d'une nouvelle fonctionnalité').
Poussez sur la branche (git push origin nouvelle-fonctionnalité).
Créez une Pull Request.

## Auteur
Douae Sebti - Développeur Java