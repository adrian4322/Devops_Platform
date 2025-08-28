# DevOps Platform

Un monorepo cu două componente principale: Frontend și Backend. Acest ghid explică arhitectura, instalarea, rularea locală, testarea, CI/CD și bune practici.

## Specific pentru acest repo
- Frontend: React 18 + Vite + TypeScript + TailwindCSS, în `Frontend/frontend-devops/`
- Backend: Spring Boot 3.5 (Java 21), Maven Wrapper, în `Backend/`
- Bază de date: MySQL (local), configurată în `Backend/src/main/resources/application.properties`

Instrucțiuni rapide:
- Frontend
  - Locație: `Frontend/frontend-devops/`
  - Instalare: `npm install`
  - Rulare dev: `npm run dev` (implicit http://localhost:5173)
  - Build: `npm run build` și previzualizare: `npm run preview`
  - Env: setați `VITE_API_BASE_URL` în `.env` (ex.: `http://localhost:8080`)
- Backend
  - Locație: `Backend/`
  - Cerințe: Java 21, Maven Wrapper inclus
  - Rulare dev: `mvnw.cmd spring-boot:run` (Windows) sau `./mvnw spring-boot:run` (Unix)
  - Build: `mvnw.cmd clean package -DskipTests`
  - Port implicit: `8080`
  - DB (exemplu curent): `jdbc:mysql://localhost:3306/baza_date`, user: `root`, pass: `admin` (modificați în `application.properties`)

## Cuprins
- [Prezentare](#prezentare)
- [Arhitectură](#arhitectură)
- [Stack tehnic](#stack-tehnic)
- [Cerințe prealabile](#cerințe-prealabile)
- [Configurare mediu](#configurare-mediu)
- [Instalare rapidă](#instalare-rapidă)
- [Rulare locală](#rulare-locală)
  - [Frontend](#frontend)
  - [Backend](#backend)
- [Scripturi utile](#scripturi-utile)
- [Build & Release](#build--release)
- [Testare & Calitate cod](#testare--calitate-cod)
- [CI/CD](#cicd)
- [Documentație API](#documentație-api)
- [Structura proiectului](#structura-proiectului)
- [Depanare (FAQ)](#depanare-faq)
- [Securitate](#securitate)
- [Contribuții](#contribuții)
- [Licență](#licență)

## Prezentare
Platformă destinată automatizării și orchestrării proceselor DevOps, cu UI modern (Frontend) și servicii scalabile (Backend).

## Arhitectură
- Frontend: aplicație web single-page (SPA), comunică cu Backend prin API REST/GraphQL.
- Backend: servicii API stateless, cu persistență de date și integrare cu servicii externe.
- Observabilitate: logs, metrics, tracing (ex. OpenTelemetry).
- CI/CD: pipeline pentru build, test, security scan și deploy.

Diagrama logică (high-level):
```
[User] -> [Frontend (SPA)] -> [API Gateway/Backend] -> [DB/Services]
```

## Stack tehnic
Specific acestui repo:
- Frontend: React 18 + TypeScript + Vite + TailwindCSS
- Backend: Spring Boot 3.5 (Java 21), Maven Wrapper
- Bază de date: MySQL
- Infrastructură: opțional Docker/Kubernetes
- CI/CD: recomandat GitHub Actions

## Cerințe prealabile
- Git
- Java 21 (JDK)
- Node.js LTS (ex. 18+)
- npm (sau yarn/pnpm)
- Docker & Docker Compose (opțional)

## Configurare mediu
Creează fișiere `.env` pentru Frontend și Backend pe baza șabloanelor.
- `frontend/.env.example` -> `frontend/.env`
- `backend/.env.example` -> `backend/.env`

Variabile tipice:
- FRONTEND: `VITE_API_BASE_URL` sau `NEXT_PUBLIC_API_URL`
- BACKEND: `PORT`, `DATABASE_URL`, `JWT_SECRET`, `LOG_LEVEL`

## Instalare rapidă
Recomandat să rulezi instalarea în fiecare componentă:

- Frontend (`Frontend/frontend-devops/`):
  ```bash
  npm install
  ```
- Backend (`Backend/`):
  ```bash
  # Windows
  .\\mvnw.cmd -v
  # sau rulare directă la pasul de build/rulare, nu e necesară instalare separată
  ```

Notă: Poți folosi Docker pentru DB MySQL, dacă dorești.

## Rulare locală
### Frontend
În `Frontend/frontend-devops/`:
```bash
npm run dev
```
Implicit: http://localhost:5173

Build producție:
```bash
pnpm build && pnpm preview
```

### Backend
În `Backend/` (Java 21 necesar):
```bash
# Windows
mvnw.cmd spring-boot:run
# Linux/Mac
./mvnw spring-boot:run
```
Implicit: http://localhost:8080

## Ghid de folosire a aplicației
Urmărește pașii de mai jos pentru a porni aplicația local și a o accesa din browser.

1) Configurare baza de date (MySQL)
   - Creează baza de date și credențialele sau actualizează `application.properties`:
     ```sql
     CREATE DATABASE baza_date CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
     -- Asigură-te că userul și parola corespund cu cele din application.properties (ex.: root/admin)
     ```
   - Opțional, pornește rapid MySQL cu Docker:
     ```bash
     docker run -d --name devops-mysql -p 3306:3306 \
       -e MYSQL_ROOT_PASSWORD=admin -e MYSQL_DATABASE=baza_date mysql:8
     ```

2) Configurează Backend-ul
   - Verifică `Backend/src/main/resources/application.properties`:
     - `spring.datasource.url=jdbc:mysql://localhost:3306/baza_date`
     - `spring.datasource.username=root`
     - `spring.datasource.password=admin`
   - Ajustează valorile conform mediului tău (user, parolă, host/port DB).

3) Pornește Backend-ul
   - În `Backend/`:
     ```bash
     # Windows
     mvnw.cmd spring-boot:run
     # Linux/Mac
     ./mvnw spring-boot:run
     ```
   - Așteaptă mesajul din consolă care indică faptul că aplicația a pornit (port implicit 8080).

4) Configurează Frontend-ul
   - În `Frontend/frontend-devops/`, creează `.env` pe baza exemplului de mai jos:
     ```env
     VITE_API_BASE_URL=http://localhost:8080
     ```
   - Instalează dependențe (o singură dată): `npm install`

5) Pornește Frontend-ul
   - În `Frontend/frontend-devops/`:
     ```bash
     npm run dev
     ```
   - Deschide aplicația în browser: http://localhost:5173

6) Verificări rapide
   - Backend: consola arată „Started ...” fără erori de conexiune la DB.
   - Frontend: pagina se încarcă, iar apelurile către API folosesc `VITE_API_BASE_URL`.
   - Dacă apar erori CORS, setează origin-ul `http://localhost:5173` în configurarea de securitate a Backend-ului.

7) Build pentru producție (opțional)
   - Frontend: `npm run build` și `npm run preview` pentru a testa build-ul local.
   - Backend: `mvnw.cmd clean package -DskipTests` sau `./mvnw clean package -DskipTests`.

## API Quick Reference
Base URL Backend: `http://localhost:8080`

- Clusters (`/api/clusters`):
  - GET `/api/clusters` — listează toate clusterele
  - GET `/api/clusters/Id/{Id}` — cluster după ID
  - GET `/api/clusters/name/{name}` — cluster după nume
  - POST `/api/clusters/create` — creează cluster (body: `ClusterDto`)
- Nodes (`/api/nodes`):
  - GET `/api/nodes` — listează noduri
  - GET `/api/nodes/{id}` — nod după ID
  - GET `/api/nodes/name/{name}` — nod după nume
- Pods (`/api/pod`):
  - GET `/api/pod` — listează pod-uri
  - GET `/api/pod/namespace/{namespace}` — pod-uri dintr-un namespace
  - GET `/api/pod/name/{name}` — pod după nume

Exemple rapide:
```bash
curl http://localhost:8080/api/clusters
curl http://localhost:8080/api/nodes/1
curl http://localhost:8080/api/pod/namespace/default
```

Note:
- CORS este permis pe `@CrossOrigin("*")` pentru aceste rute.
- În Frontend setează `VITE_API_BASE_URL=http://localhost:8080`.

## WebSocket (STOMP)
- Endpoint WS: `http://localhost:8080/ws` 
- Prefix aplicație: `/app`
- Broker topics: `/topic`

## Scripturi utile
Actualizează `package.json`/`pyproject.toml` după caz.
- `dev`: pornește aplicația în mod dezvoltare.
- `build`: build de producție.
- `test`: rulează testele unitare/integrate.
- `lint` / `format`: verifică și formatează codul.
- `start`: pornește serviciul în producție.

## Build & Release
- Versionare: SemVer (MAJOR.MINOR.PATCH)
- Artefacte: imagini Docker pentru Frontend și Backend
- Registry: GitHub Container Registry / Docker Hub
- Release notes: generate din PR-uri/Conventional Commits

Exemplu build Docker:
```bash
# Frontend
docker build -t your-org/frontend:TAG ./frontend
# Backend
docker build -t your-org/backend:TAG ./backend
```

## Testare & Calitate cod
Sugestii:
- Frontend: Lint (ESLint), format (Prettier). Exemple: `npm run lint`.
- Backend: Teste cu Maven/JUnit. Exemple: `mvnw.cmd test` sau `./mvnw test`.
- Securitate: `npm audit`, scan imagini Docker, SAST (ex. Semgrep).

## CI/CD
- Pipeline tipic:
  1) Install deps
  2) Lint + Test
  3) Build
  4) Security scan
  5) Publish artefacte (imagini Docker)
  6) Deploy (staging/prod)

Folosește GitHub Actions (ex. `.github/workflows/ci.yml`).


## Structura proiectului
Structura actuală (relevantă):
```
root/
├─ Frontend/
│  └─ frontend-devops/
│     ├─ src/
│     ├─ public/
│     └─ package.json
├─ Backend/
│  ├─ src/
│  ├─ pom.xml
│  ├─ mvnw / mvnw.cmd
│  └─ src/main/resources/application.properties
└─ README.md
```

## Depanare (FAQ)
- Port ocupat: schimbă `PORT` în `.env` sau în scripturile de start.
- CORS: setează origin-urile permise în Backend.
- Variabile lipsă: verifică `.env` și `process.env`/`os.environ`.
- DB inaccesibilă: verifică `DATABASE_URL`, rețele Docker, migrații.

## Securitate
- Nu comita secretele. Folosește GitHub Secrets/Vault.
- Rulează scanări regulate (deps, imagini Docker).
- Activează branch protection + code review obligatoriu.

## Contribuții
- Fork, feature branch, PR cu descriere clară și checklist.
- Respectă convențiile de commit (Conventional Commits).
- Adaugă/actualizează testele relevante.

## Licență
Specificați licența (ex. MIT, Apache-2.0) în `LICENSE`.
