<p align="center">
  <a href="https://github.com/IFT-2255/ift2255-ma-ville-bugbusters/actions/workflows/test.yml">
    <img src="https://github.com/IFT-2255/ift2255-ma-ville-bugbusters/actions/workflows/test.yml/badge.svg" alt="Compiler et tester" />
  </a>
  <a href="https://github.com/IFT-2255/ift2255-ma-ville-bugbusters/actions/workflows/docker.yml">
    <img src="https://github.com/IFT-2255/ift2255-ma-ville-bugbusters/actions/workflows/docker.yml/badge.svg" alt="Update Docker images" />
  </a>
</p>

# MaVille Application


## Aperçu
<img alt="start of application" src="/public/showcase.png">
<table>
  <tr>
    <td>
      <img alt="aperçu petit écran" src="/public/showcase1.png">
    </td>
    <td>
      <img alt="aperçu ordinateur" src="/public/showcase2.png">
    </td>
  </tr>
</table>

---

## A propos du projet

Il s'agit d'un projet réalisé par le groupe **Bugbusters** dans le cadre du cours de Génie logiciel. **MaVille** est une application construite pour permettre une meilleure gestion des projets de la ville de Montréal. Elle
propose trois profils d'utilisateur : le résident, le prestataire et l'agent de la STPM qui a un profil unique non nominatif.
L'application propose les fonctionnalités suivantes : 

- Pour le résident : 
    - Le signalement de problème dans son quartier.
    - La possibilité de voir les projets en cours et veux à venir dans la ville de Montréal.
    - La possibilité de filtrer les projets par quartier, par type de travaux et par priorité pour une acquisition plus efficace de l'information.
    - La possibilité de s'abonner à des quartiers ou des rues particulières de la ville de Montréal.
    - La réception en temps réel de notification pour la création ou les mises à jour concernant tout projet impactant les rues ou les quartiers auxquels il est abonné.

- Pour le prestataire : 
    - La visualisation des fiches problèmes créées par les agents de la STPM à partir des signalements des résidents.
    - La soumission d'une candidature pour une fiche problème.
    - Le suivi en temps réel de la décision de l'agent de la STPM par rapport à la candidature.
    - L'accès et la visualisation de tous les projets à sa charge.
    - La possibilité de modifier ses projets.
    - La possibilité de s'abonner à des quartiers ou des types de problèmes pour les signalements des résidents.
    - La réception en temps réel de notification pour toute nouvelle fiche problème concernant les type de problèmes et les quartiers auxquels il est abonné.

- Pour l'agent de la STPM : 
    - La possibilité de voir en temps réel les signalements des résidents de la ville de Montréal.
    - La capacité d'attribuer une priorité à un signalement pour créer ainsi une fiche problème.
    - La capacité de lier un signalement à une fiche problème déjà existante.
    - La possibilité de voir en temps réel les candidatures des prestataires.
    - La possibilité d'accepter une candidature, créant ainsi un nouveau projet.
    - La possibilité de refuser une candidature en donnant une raison de refus.
    - La réception en temps réel de notifications pour tout nouvea signalement ou toute nouvelle candidature.

---

## Structure du projet

- `README.md`            -> Ce fichier ;)
- `LICENSE`              -> Le fichier de License du répertoire (license MIT).
- `docker-compose.yml`   -> Le fichier Docker pour définir et exécuter les deux conteneurs de l'application.
- `.gitignore`           -> Spécifie quels fichiers git doit ignorer.
- `backend/pom.xml`      -> La configuration Maven pour le backend Java.
- `backend/Dockerfile`   -> La configuration su conteneur docker pour le backend.
- `backend/src/main`     -> Le code pour l'application.
- `backend/src/test`     -> Le code pour les tests.
- `client/package.json`  -> La configuration npm pour le client en React.js.
- `client/Dockerfile`    -> La configuration su conteneur docker pour le client.
- `client/src`           -> Le code source pour l'interface graphique en React.
- `rapport/`             -> Le rapport technique du projet.
- `visual_paradigm/`     -> Tous les fichiers visual paradigm.
- `public`               -> Le dossier de fichiers statiques pour le README.
- `.github/workflows`    -> Les github actions pour le CI.

---

## Installation et Lancement

1. Premièrement, vous devez **cloner le répertoire** en utilisant git (voir le bouton Code en haut) et ensuite
vous déplacer dans le dossier du projet :
  ```bash
    git clone https://github.com/IFT-2255/ift2255-ma-ville-bugbusters.git
    cd ift2255-ma-ville-bugbusters
  ```

2. Deuxièmement, précisez l'url de l'api de votre backend `VITE_API_URL` dans un fichier `.env` à l'intérieur de `ift2255-ma-ville-bugbusters/client`.

3. Finalement, placez l'url de votre base de données `MONGO_URI` et le port `PORT` (au besoin) dans un fichier `.env` à l'intérieur de `ift2255-ma-ville-bugbusters/backend`.

**NB**: Lorsque vous lancerez l'application, pour avoir des informations de base dans la base de données, il vous faudra 
décommenter les lignes 54, 55 et 56 du fichier `ift2255-ma-ville-bugbusters/backend/src/main/java/ca/udem/maville/server/Server.java`.
Ensuite lorsque vous lancerez l'application, la base de données sera remplie une fois avec les informations utilisateurs.
A votre deuxième lancement, veuillez commenter ces trois lignes à nouveau.

Une fois tout cela fait, vous disposez de deux options pour lancer l'application : 

### Première option : Utiliser Docker

1. Démarrer le Docker Desktop.

2. Lancer les conteneurs.
  ```bash
    docker-compose up
  ```

3. Une fois le lancement fini, vous pouvez accéder à l'application à l'adresse http://localhost:5173/ . Bonne exploration !



### Deuxième option : Sans Docker

**Il est nécessaire d'avoir Maven, Java 21 et Node.js version >=22.12.0 installés sur votre machine.**
Ci-dessous les instructions pour installer, lancer et tester l'application sans docker. Cela se fait en deux parties : 

#### Le backend

1. Se déplacer dans le dossier backend. Ouvrir le dossier root dans un premier terminal et exécuter :
  ```bash
    cd backend
  ```

2. Pour installer et lancer le server backend.
  ```bash
    mvn clean package
    java -jar target/maVille-3.0-SNAPSHOT-jar-with-dependencies.jar
  ```
  Le serveur démarre alors.


#### Le client

1. Se déplacer dans le dossier client. Ouvrir le dossier root dans un deuxième terminal et exécuter : 
  ```bash
    cd client
  ```

2. Pour installer et lancer l'interface graphique.
  ```bash
    npm install
    npm run dev
  ```

3. Vous pouvez accéder à l'application à l'adresse http://localhost:5173/ . Bonne exploration !

---

## Tests

Il est possible de tester le serveur Java : 

1. Se déplacer dans le dossier backend. Depuis le terminal dans le dossier root, exécuter :
  ```bash
    cd backend
  ```

2. Pour lancer les tests JUnit
  ```bash
    mvn test
  ```

---

## Auteurs (membres du groupe Bugbusters)

- **Cyreanne Candy Andrianefa**

Github : [cyreanne0](https://github.com/cyreanne0)

- **Zachary Bourgeois**

GitHub : [zacmatsteal](https://github.com/zacmatsteal)

- **Roxanne Cabedoce**

Github : [cabedocer](https://github.com/cabedocer)

- **Lallia Diakite**

GitHub : [Lalllou](https://github.com/Lalllou)

- **Josué Mongan**

GitHub : [Josh012006](https://github.com/Josh012006)

---

## License et Droits d'auteurs

Ce projet est sous la license MIT.

Les informations par rapport aux images utilisées pour l'interface (client/public) sont :

- back1.jpg - By TheoFbe - Content License pixabay
- resident.png - By IconBaandar - Flaticon
- prestataire.png - By SmashIcons - Flaticon
- stpm.png - By surang - Flaticon
- tab1.png - <a href="https://www.flaticon.com/authors/omoonstd" title="O.moonstd"> O.moonstd </a>
- tab2.png - <a href="https://www.flaticon.com/authors/omoonstd" title="O.moonstd"> O.moonstd </a>
- profile.png - <a href="https://www.flaticon.com/free-icons/user" title="user icons">User icons created by Becris - Flaticon</a>
- notif.png - <a href="https://www.flaticon.com/free-icons/notification" title="notification icons">Notification icons created by Freepik - Flaticon</a>
- candidature.png - <a href="https://www.flaticon.com/free-icons/poll" title="poll icons">Poll icons created by Freepik - Flaticon</a>
- signalement.png - <a href="https://www.flaticon.com/free-icons/warning" title="warning icons">Warning icons created by Smashicons - Flaticon</a>
- signalement1.png - <a href="https://www.flaticon.com/free-icons/under-construction" title="under construction icons">Under construction icons created by Aranagraphics - Flaticon</a>
- probleme.png - <a href="https://www.flaticon.com/free-icons/problem" title="problem icons">Problem icons created by Design View - Flaticon</a>
- projet.png - <a href="https://www.flaticon.com/free-icons/construction" title="construction icons">Construction icons created by Pause08 - Flaticon</a>
- udem.png - https://www.sium.umontreal.ca/impression-couleur.html
- udem_logo.png - https://www.sium.umontreal.ca/impression-couleur.html
- arrow.png - <a href="https://www.flaticon.com/free-icons/back-arrow" title="back arrow icons">Back arrow icons created by Ilham Fitrotul Hayat - Flaticon</a>
- validate_icon.png - Icons made by <a href="https://www.flaticon.com/authors/meaicon" title="meaicon"> meaicon </a>
- close_icon.png - Icons made by <a href="https://www.flaticon.com/authors/bingge-liu" title="Bingge Liu"> Bingge Liu </a>
- new.png - <a href="https://www.flaticon.com/free-icons/survey" title="survey icons">Survey icons created by Flat Icons - Flaticon</a>
