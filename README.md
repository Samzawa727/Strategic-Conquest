# 🎮 Strategic Conquest

## 📋 Description
Jeu de stratégie au tour par tour développé en Java avec LibGDX.

## 🏗️ Architecture du Projet

```
com.strategygame/
├── Main.java                        # Point d'entrée de l'application
├── StrategyGame.java                # Classe principale LibGDX
│
├── model/                           # Couche Modèle (Logique métier)
│   ├── TerrainType.java            # Enum : Types de terrain
│   ├── Tile.java                   # Classe : Case de la carte
│   ├── GameMap.java                # Classe : Carte de jeu
│   ├── ResourceManager.java        # Classe : Gestion des ressources
│   ├── Player.java                 # Classe : Joueur (humain/IA)
│   │
│   ├── units/                      # Package : Unités militaires
│   │   ├── Unit.java               # Classe abstraite
│   │   ├── Soldier.java            # Unité : Soldat
│   │   ├── Archer.java             # Unité : Archer
│   │   └── Cavalry.java            # Unité : Cavalier
│   │
│   └── buildings/                  # Package : Bâtiments
│       ├── Building.java           # Classe abstraite
│       ├── CommandCenter.java      # Bâtiment : Centre de commandement
│       ├── Barracks.java           # Bâtiment : Caserne
│       └── ResourceBuilding.java   # Bâtiment : Mine/Ferme/Scierie
│
├── controller/                     # Couche Contrôleur
│   └── GameController.java         # Contrôleur principal du jeu
│
└── screens/                        # Couche Vue (Interface)
    ├── MenuScreen.java             # Écran : Menu principal
    ├── InstructionsScreen.java     # Écran : Instructions
    └── GameScreen.java             # Écran : Jeu principal
```

## 🎨 Système de Couleurs et Lettres

### 🎖️ Unités (Cercles avec lettres)
**Joueur (Bleu) :**
- 🔵 **S** = Soldat (Bleu moyen)
- 🔵 **A** = Archer (Bleu clair)
- 🔵 **C** = Cavalier (Bleu foncé)

**IA (Rouge) :**
- 🔴 **S** = Soldat (Rouge moyen)
- 🔴 **A** = Archer (Rouge-orange)
- 🔴 **C** = Cavalier (Rouge foncé)

### 🏛️ Bâtiments (Carrés avec lettres)
- 🟨 **C** = Centre de commandement (Or foncé)
- 🟥 **E** = Camp d'Entraînement (Rouge foncé)
- ⬛ **M** = Mine (Gris)
- 🟩 **F** = Ferme (Vert olive)
- 🟫 **S** = Scierie (Marron bois)

### 🗺️ Terrains
- 🟢 Herbe (Vert)
- 🔵 Eau (Bleu)
- ⚫ Montagne (Gris)
- 🟢 Forêt (Vert foncé)
- 🟡 Désert (Jaune sable)

## 🤖 Intelligence Artificielle Avancée

L'IA possède un comportement stratégique complet en 4 phases :

### Phase 1 : Gestion Économique
- Construit un **Centre de Commandement** dès que possible (génère de l'or)
- Construit une **Caserne** pour produire des unités
- Développe des bâtiments de ressources (Mine, Ferme, Scierie) périodiquement

### Phase 2 : Production Militaire
- Crée des **Soldats** régulièrement (unités de base)
- Produit des **Archers** tous les 4 tours (unités à distance)
- Recrute des **Cavaliers** quand les ressources le permettent (unités d'élite)

### Phase 3 : Tactique Militaire
- Détecte les unités ennemies les plus proches
- Attaque si à portée
- Se déplace stratégiquement vers les cibles sinon

### Phase 4 : Attaque des Infrastructures
- Cible les bâtiments ennemis proches
- Détruit l'économie adverse

## 🎮 Contrôles

### 🖱️ Souris
- **Clic gauche** : Sélectionner une unité ou une case
- **Clic droit** : Déplacer l'unité sélectionnée / Attaquer

### ⌨️ Clavier
**Gestion générale :**
- **ESPACE** : Passer au tour suivant
- **ESC** : Retour au menu / Annuler le mode actuel
- **B** : Menu de construction de bâtiments (cycle)
- **U** : Menu de création d'unités (cycle)

**Raccourcis rapides - Bâtiments :**
- **1** : Construire Centre de Commandement
- **2** : Construire Camp d'Entraînement
- **3** : Construire Mine

**Raccourcis rapides - Unités :**
- **4** : Créer Soldat
- **5** : Créer Archer
- **6** : Créer Cavalier

## 📊 Système de Ressources

| Ressource | Utilisation | Production |
|-----------|-------------|------------|
| 💰 **Or** | Toutes les constructions/unités | Centre de Commandement (+20/tour) |
| 🪵 **Bois** | Bâtiments, Archers | Scierie (+15/tour) |
| 🪨 **Pierre** | Bâtiments principaux | Mine (+15/tour) |
| 🌾 **Nourriture** | Unités militaires | Ferme (+15/tour) |

**Ressources de départ :** 500 Or, 300 Bois, 200 Pierre, 400 Nourriture

## ⚔️ Unités Militaires

| Unité | PV | Attaque | Défense | Portée | Déplacement | Coût |
|-------|----|---------|---------|------------|-------------|------|
| **Soldat** | 100 | 15 | 12 | 1 | 3 | 50 Or, 30 Nourriture |
| **Archer** | 70 | 18 | 8 | 3 | 2 | 60 Or, 40 Bois, 25 Nourriture |
| **Cavalier** | 90 | 22 | 10 | 1 | 5 | 100 Or, 50 Nourriture |

### 💥 Système de Combat
**Formule de dégâts :** 
```
Dégâts = max(1, Attaque - Défense/2 + Random(0-4))
```

## 🏰 Bâtiments

| Bâtiment | PV | Construction | Production | Coût |
|----------|----|--------------|-----------|-|
| **Centre de Commandement** | 500 | 3 tours | +20 Or/tour | 200 Or, 150 Bois, 100 Pierre |
| **Camp d'Entraînement** | 300 | 2 tours | Débloque les unités | 120 Or, 80 Bois, 50 Pierre |
| **Mine** | 250 | 2 tours | +15 Pierre/tour | 100 Or, 50 Bois |
| **Ferme** | 250 | 2 tours | +15 Nourriture/tour | 70 Or, 40 Bois |
| **Scierie** | 250 | 2 tours | +15 Bois/tour | 80 Or, 30 Pierre |

## 🎯 Concepts POO Implémentés

### 1️⃣ Héritage
```java
Unit (classe abstraite)
  ├── Soldier
  ├── Archer
  └── Cavalry

Building (classe abstraite)
  ├── CommandCenter
  ├── Barracks
  └── ResourceBuilding
```

### 2️⃣ Polymorphisme
```java
// Méthode abstraite redéfinie dans chaque bâtiment
public abstract void executerAction();

// CommandCenter génère de l'or
// ResourceBuilding génère Pierre/Bois/Nourriture
// Barracks ne produit rien (débloque les unités)
```

### 3️⃣ Encapsulation
- Tous les attributs sont **private** ou **protected**
- Accès via **getters** et **setters**
- Méthodes internes **private** (trouverEmplacementLibre, deplacerVers...)

### 4️⃣ Classes Abstraites
- `Unit` : Définit le comportement commun des unités
- `Building` : Définit le comportement commun des bâtiments

### 5️⃣ Collections Java
```java
Map<String, Integer> ressources;           // ResourceManager
List<Unit> unites;                          // Player
List<Building> batiments;                   // Player
List<String> notifications;                 // GameController
Tile[][] tiles;                             // GameMap
```

## 🚀 Installation et Lancement

### Prérequis
- **Java 11+**
- **LibGDX** (via Gradle ou Maven)

### Dépendances LibGDX
```gradle
dependencies {
    implementation "com.badlogicgames.gdx:gdx:1.12.0"
    implementation "com.badlogicgames.gdx:gdx-backend-lwjgl3:1.12.0"
    implementation "com.badlogicgames.gdx:gdx-platform:1.12.0:natives-desktop"
}
```

### Compilation et Exécution
# Avec Gradle
./gradlew desktop:run (Linux)
gradlew.bat lwjgl3:run(Windows)

✅ **Collections Java**
- HashMap pour les ressources
- ArrayList pour unités et bâtiments
- Utilisation de streams et lambdas

## 🎮 Stratégies de Jeu

### Pour Gagner
1. **Construisez rapidement un Centre de Commandement** (génère de l'or)
2. **Développez votre économie** avec des bâtiments de ressources
3. **Créez une Caserne** pour produire des unités
4. **Équilibrez votre armée** : Soldats (défense), Archers (attaque à distance), Cavaliers (mobilité)
5. **Détruisez les unités ennemies** pour remporter la victoire

### Conseils Tactiques
- Les **Archers** peuvent attaquer de loin (portée 3)
- Les **Cavaliers** se déplacent rapidement (déplacement 5)
- Protégez vos bâtiments des attaques ennemies
- L'IA devient plus agressive avec le temps