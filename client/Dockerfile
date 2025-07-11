# Étape 1 : image officielle Node
FROM node:24.4.0

# Étape 2 : définir le dossier de travail
WORKDIR /app

# Étape 3 : copier les dépendances et installer
COPY package*.json ./
RUN npm install

# Étape 4 : copier le reste du code source
COPY . .

# Étape 5 : démarrer Vite (dev server)
CMD ["npm", "run", "dev"]