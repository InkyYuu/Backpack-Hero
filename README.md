# Jeu Backpack Hero #

## Règles du jeu ##

Pour lancer le jeu, il suffit de lancer `GameController.java` depuis Eclipse. Une fois le jeu lancé, les règles sont reprises du jeu originel :  Vous jouez une souris qui s'aventure dans un donjon avec son fameux sac à dos et qui souhaite sortir, pour cela, il suffit de monter les étages en passant de pièce en pièce en affrontant les différents ennemis qui se trouveront sur votre chemin avec les différentes armes que vous stocker dans votre sac à dos !

**Bon jeu !**

## Les implémentations ##
##### Carte du niveau #####
- [x] Le joueur peut se déplacer au sein de l'étage du donjon sur lequel il se trouve actuellement.
- [x] Chaque étage contient des pièces vides et des événements (ennemis, coffres, marchand, guérisseur).
- [x] Il y a toujours une porte de sortie sur l'étage.
- [x] Les étages font 5 x 11 de cases.
- [x] Toutes les salles sont reliées entre elles.
- [x] Les étages et les événements sont générés aléatoirement.
- [x] On peut voir sur la carte les icônes des événements.
- [x] Certaines salles bloque le chemin, ce qui empêche au héro de traverser les étages sans obstacles


##### Combat #####
- [x] Le combat est en tour par tour le héro puis les ennemis.
- [x] Le héro peut utiliser les objets de son inventaire pour effectuer l'action de l'objet (attaque, défense, effets, ...).
- [x] Le héro peut utiliser "scratch" pour attaquer les ennemis sans objet nécessaire.
- [x] Les objets coûtent soit de l'énergie, de la mana ou de l'or.
- [x] Si tous les ennemis sont morts, alors le combat se fini.
- [x] Après le combat, le héro reçoit des objets, de l’expérience et de l'or.


##### Bestiaire #####
- [x] Il y a 6 types d'ennemis : Petits rats loups, rats-loups, petites abeilles, reine abeille, ombre vivante et grenouille magicienne.
- [x] Les ennemis annoncent ce qu'ils feront au prochain tour, de manière aléatoire.
- [x] Les ennemis peuvent attaquer, se protéger ou faire leurs autres actions.
- [x] Les ennemis peuvent subir des effets de combat.


##### Interface #####
- [x] Jeu entièrement jouable sans regarder la console d'Eclispe.
- [x] L'affichage avec Zen5 est interactif.
- [x] Modèle MVC respecté (GameModel, SimpleGameView, GameController).
- [x] Tous les effets sont affichés durant le combat, comme les points d'énergies, de protection ou les points de vie.


##### Hero #####
- [x] Le héro est affiché, possède 40 points de vie et 3 points d'énergie par tour.
- [x] Le joueur peut voir une carte de l'étage ou son sac.
- [x] Le héro peut subir des effets de combat.


##### Sac à Dos #####
- [x] Lorsqu'on gagne des niveaux d'expériences, on peut ajouter des cases dans notre sac à dos.
- [x] On plus de 20 objets, dont des armes de mêlées, armes à distance, armures, boucliers, objets magiques, manastones et de l'or.
- [x] Les objets interagissent.
- [x] On peut positionner chaque objet et leur faire effectuer une rotation de 90°.
- [x] On a implémenté des malédictions, qui sont infligées par des ennemis. Soit on refuse et on prend des dégâts, soit on accepte et on subit la malédiction qui ne pourra plus être retournée ni se débarrassé de la malédiction soit en allant au guérisseur ou en la détruisant durant un tour de combat.


##### Score #####
- [x] Le score est basé sur notre recette de score (basé sur le prix des équipements de l'inventaire, du nombre d'ennemis tués, du nombre d'étages parcourus, du nombres de niveaux d'expériences faits et du nombre de points de vie max).
- [x] Le jeu se termine à la fin du 3 ème étage. Si on arrive à la fin, la partie est considérée comme gagnée et cela impacte le score positivement.
- [x] Le score est écrit dans un fichier texte pour sauvegarder le score de la partie.

## L'organisation du code (UML) ##
*Les packages sont en minuscules tandis que les class / interface / enumerate commencent par une majuscule.*
&nbsp;
[![Schéma de l'organisation des classes et des packages](https://kellian.awooo.fr/other/backpack-hero-organisation.png)](https://kellian.awooo.fr)

> Création d'une classe pour chaque type item qui se distingue des autres :
On a trouvé plus simple de faire une classe pour chaque type item vu qu'il se distingue notamment au niveau des actions, des cractéristiques et pour pouvoir utiliser le polymorphisme. (Vu qu'ils implémentent tous l'interface `Items`)

## Les choix techniques ##
|Choix|Pourquoi ?|
|--------|--------|
|`Logiciel Eclipse`|Fortement recommendé pour le projet|
|`JavaSE-17`|Version récente et recommendé par défaut par Eclipse|
|`zen5`|Obligatoire dans le cadre de ce projet


## Les choix algorithmiques ##
|Choix|Pourquoi ?|
|--------|--------|
|`Format MVC`|Vu en cours et imposé pour ce projet|
|`Très peu d'utilisation d'instanceOf`|A part dans certains cas (2 ou 3) on utilise majoritairement le Polymorphisme|
|`Découpage du code en un maximum de fonction`|Obligatoire pour respecter au mieux la consigne des fonctions de moins de 20 lignes et simplifie la lecture du code|
|`Aucune classe abstraite`|Aucune utilité trouvée pour ce projet|
|`Utilisation de la récursivité`|Utilisation de la récursivité pour les fonctions de recheche `isValidPath` et `findMana`|

## Les problèmes rencontrés ##
*Classés par ordre décroissant en terme de difficulté*

* Organisation en format MVC : 
    > Nous avons compris le modèle MVC qu'à partir de la fin du rendu 1, ce qui nous a obligé à tout recommencer.

* Gestion du temps :
    > En vue de la charge de travail notamment avec les autres projets, ce projet-ci a tourné au ralentit pendant les deux premiers rendus. On a donc décidé à partir du rendu 3 de crée un Trello pour organisé au mieux le travail et la répartition des tâches.
    - ###### Trello ######
    [![Schéma de l'organisation des classes et des packages](https://kellian.awooo.fr/other/trello.png)](https://kellian.awooo.fr)
     - ###### Rendu 1 ######
    [![Schéma de l'organisation des classes et des packages](https://kellian.awooo.fr/other/rendu1.png)](https://kellian.awooo.fr)
    - ###### Rendu 3 ######
    [![Schéma de l'organisation des classes et des packages](https://kellian.awooo.fr/other/rendu3.png)](https://kellian.awooo.fr)
&nbsp;

* Difficulté avec la création de la map aléatoire (Helena) :
    > La difficulté a été de crée une map aléatoire fonctionnelle qui ne fait pas buguer le code.

* Difficulté avec les intéractions des items (Kellian) :
    > La difficulté a été d'enregistrer les interactions pour chaque item et de les faire fonctionner en fonction de si c'est `Passif`, `Each turn` ou `On use`.

&nbsp;
<p>Auteur : CHEVALIER Helena - BREDEAU Kellian</p>