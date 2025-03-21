# Projet de Résolution de Labyrinthe

Ce projet implémente un solveur de labyrinthe utilisant les algorithmes de recherche en profondeur (DFS) et en largeur (BFS). Il permet de visualiser et comparer les performances de ces deux algorithmes de manière interactive.

## Table des matières

1. [Vue d’ensemble](#vue-densemble)
2. [Fonctionnalités](#fonctionnalités)
3. [Prérequis](#prérequis)
4. [Installation](#installation)
5. [Utilisation](#utilisation)
    1. [Mode Graphique](#mode-graphique)
    2. [Mode Console](#mode-console)
6. [Format des fichiers de labyrinthe](#format-des-fichiers-de-labyrinthe)
7. [Statistiques affichées](#statistiques-affichées)
8. [Structure du projet](#structure-du-projet)
9. [Comparaison des algorithmes](#comparaison-des-algorithmes)
10. [Vidéo de présentation](#vidéo-de-présentation)
11. [Documentation](#documentation)
12. [Contribution](#contribution)
13. [Licence](#licence)
14. [Contributeurs](#contributeurs)




## 🎯 Fonctionnalités

- **Chargement de labyrinthe**
  - Depuis un fichier texte
  - Génération aléatoire avec paramètres personnalisables

- **Résolution de labyrinthe**
  - Algorithme DFS (Depth-First Search)
  - Algorithme BFS (Breadth-First Search)
  - Visualisation en temps réel
  - Comparaison simultanée des deux algorithmes

- **Interface graphique**
  - Affichage côte à côte des solutions
  - Contrôles d'animation (vitesse, pause, mode pas à pas)
  - Statistiques détaillées pour chaque algorithme
  - Comparaison des performances

## 🛠️ Prérequis

- Java JDK 11 ou supérieur
- Un éditeur de code (IntelliJ IDEA, Eclipse, VS Code, etc.)

## 📦 Installation

1. Clonez le repository :
```bash
git clone https://github.com/Sene88Ibrahima/Resolution-Labyrinthe.git
cd Resolution-Labyrinthe
```

2. Compilez le projet :
```bash
javac src/model/*.java src/algorithms/*.java src/utils/*.java src/ui/*.java src/Main.java
```

3. Lancez l'application :
```bash
java -cp src Main
```

Ou utilisez le fichier batch `lancer.bat` sous Windows :
```bash
lancer.bat
```

## 🎮 Utilisation

### Mode Graphique

1. Lancez l'application
2. Choisissez l'option 2 pour le mode graphique
3. Utilisez les boutons en haut de l'interface :
   - "Charger un labyrinthe" : pour ouvrir un fichier existant
   - "Générer un labyrinthe" : pour créer un labyrinthe aléatoire
   - "Résoudre (DFS et BFS)" : pour lancer la résolution
   - "Guide d'utilisation" : pour voir l'aide

4. Contrôles pendant la résolution :
   - Slider de vitesse : ajustez la vitesse d'animation
   - Bouton Pause : interrompre/reprendre la résolution
   - Mode Pas à Pas : avancer étape par étape

### Mode Console

1. Lancez l'application
2. Choisissez l'option 1 pour le mode console
3. Suivez les instructions pour :
   - Charger un labyrinthe depuis un fichier
   - Générer un labyrinthe aléatoire
   - Voir les résultats des deux algorithmes

## 📝 Format des fichiers de labyrinthe

Le labyrinthe est représenté par une matrice où :
- `#` représente un mur
- `=` représente un passage
- `S` représente le point de départ
- `E` représente la sortie
- `+` représente le chemin solution

Exemple :
```
#########
#S=====#
#=#=#=#=#
#=#=#=#=#
#=====#E#
#########
```

## 📊 Statistiques affichées

Pour chaque algorithme :
- Nombre d'étapes explorées
- Temps d'exécution
- Longueur du chemin trouvé
- Utilisation mémoire

Comparaison :
- Différence de performance entre DFS et BFS
- Pourcentage d'exploration du labyrinthe
- Analyse des chemins trouvés

## 🏗️ Structure du projet

```
src/
├── model/
│   ├── Cell.java       # Représentation d'une cellule
│   └── Maze.java       # Gestion du labyrinthe
├── algorithms/
│   ├── MazeSolver.java # Interface des algorithmes
│   ├── DFSSolver.java  # Implémentation DFS
│   └── BFSSolver.java  # Implémentation BFS
├── ui/
│   └── MazeGUI.java    # Interface graphique
├── utils/
│   └── MazeLoader.java # Chargement/génération
└── Main.java           # Point d'entrée
```

## 🔍 Comparaison des algorithmes

### DFS (Depth-First Search)
- Exploration en profondeur
- Utilise une pile (Stack)
- Avantages :
  - Utilise moins de mémoire
  - Rapide pour trouver une solution
- Inconvénients :
  - Ne garantit pas le chemin le plus court
  - Peut s'enfoncer profondément dans des impasses

### BFS (Breadth-First Search)
- Exploration en largeur
- Utilise une file (Queue)
- Avantages :
  - Trouve toujours le chemin le plus court
  - Plus efficace pour les labyrinthes simples
- Inconvénients :
  - Utilise plus de mémoire
  - Peut être plus lent pour trouver une solution

## 🎥 Vidéo de présentation

[Lien vers la vidéo YouTube de présentation](https://youtube.com/votre-video)

## 📚 Documentation

Pour plus de détails sur l'implémentation et les choix algorithmiques, consultez la documentation du code source.

## 🤝 Contribution

Les contributions sont les bienvenues ! N'hésitez pas à :
1. Fork le projet
2. Créer une branche pour votre fonctionnalité
3. Commiter vos changements
4. Pousser vers la branche
5. Ouvrir une Pull Request

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails. 

## 🤝 Contributeurs
- Magatte DIAWARA : magui245 
- Ibrahima SENE : Sene88Ibrahima
- Abdou Aziz SY : Abdou-Aziz-Sy
