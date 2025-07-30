# Étape 1 : Utiliser l’image officielle de Node
FROM node:24.4.0

# Étape 2 : Définir le dossier de travail
WORKDIR /app

# Étape 3 : Copier les fichiers de dépendances
COPY package*.json ./

# Étape 4 : Installer proprement les dépendances
# Supprime d’abord node_modules si présent et installe clean
RUN rm -rf node_modules && npm install

# Étape 5 : Copier le reste du code source
COPY . .

# Étape 6 : Exposer le port Vite par défaut
EXPOSE 5173

# Étape 7 : Démarrer le serveur de dev Vite
CMD ["npm", "run", "dev"]
