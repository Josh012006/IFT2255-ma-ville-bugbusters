# Étape 1 : Utiliser une image officielle stable de Node.js
FROM node:22.12.0-slim

# Étape 2 : Définir le dossier de travail
WORKDIR /app

# Étape 3 : Copier les fichiers de dépendances
COPY package*.json ./

# Étape 4 : Installer les dépendances
RUN npm install

# Étape 5 : Copier le reste du code source
COPY . .

# Étape 6 : Exposer le port Vite (par défaut : 5173)
EXPOSE 5173

# Étape 7 : Démarrer le serveur Vite
CMD ["npm", "run", "dev"]
