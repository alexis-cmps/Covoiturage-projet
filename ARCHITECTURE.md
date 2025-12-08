# Architecture du Projet Covoiturage Étudiant

## 📊 Vue d'ensemble de l'architecture

Votre projet suit maintenant une **architecture en couches professionnelle** conforme aux bonnes pratiques du développement Spring Boot.

---

## 🏗️ Structure des couches

### 1. **Couche Domaine (Entity) - Domain Layer**
📁 `com.ecole.covoiturage.entity`

Les entités JPA représentent le modèle de données :
- **Student** : Représente un étudiant (conducteur ou passager)
- **Trajet** : Représente un trajet de covoiturage
- **Reservation** : Représente une réservation d'un passager sur un trajet

✅ **Bonnes pratiques appliquées :**
- Utilisation de JPA/Hibernate pour la persistance
- Relations bidirectionnelles (@OneToMany, @ManyToOne)
- Annotations Lombok pour réduire le boilerplate

---

### 2. **Couche DTO (Data Transfer Object)**
📁 `com.ecole.covoiturage.dto`

Les DTOs permettent de **séparer le modèle de données interne du modèle exposé** :
- **StudentDTO** : Exposition publique d'un étudiant (sans mot de passe)
- **StudentRegistrationDTO** : Données nécessaires pour l'inscription
- **LoginRequestDTO** : Données de connexion
- **TrajetDTO** : Trajet avec informations du conducteur
- **ReservationDTO** : Réservation complète

✅ **Avantages :**
- Sécurité : le mot de passe n'est jamais exposé dans les réponses
- Validation : annotations Jakarta Validation (@NotBlank, @Email, etc.)
- Flexibilité : permet d'avoir différentes représentations selon le contexte

---

### 3. **Couche Mapper**
📁 `com.ecole.covoiturage.mapper`

Les mappers convertissent **Entity ↔ DTO** :
- **StudentMapper** : Conversion Student ↔ StudentDTO
- **TrajetMapper** : Conversion Trajet ↔ TrajetDTO
- **ReservationMapper** : Conversion Reservation ↔ ReservationDTO

✅ **Pattern utilisé :** 
- Séparation des responsabilités
- Réutilisabilité du code
- Facilite les tests unitaires

---

### 4. **Couche Repository (DAO - Data Access Object)**
📁 `com.ecole.covoiturage.repository`

Les repositories gèrent **l'accès aux données** :
- **StudentRepository** : CRUD + recherche par email
- **TrajetRepository** : CRUD + recherche par départ/destination + recherche par conducteur
- **ReservationRepository** : CRUD pour les réservations

✅ **Pattern DAO avec Spring Data JPA :**
- Interface étendant JpaRepository
- Méthodes de requête dérivées du nom
- Pas besoin d'implémenter le code SQL

---

### 5. **Couche Service (Application/Business Logic)**
📁 `com.ecole.covoiturage.service`

Les services contiennent **la logique métier** :
- **StudentService** : 
  - Inscription avec cryptage du mot de passe (BCrypt)
  - Authentification sécurisée
  - Gestion des étudiants
  
- **TrajetService** : 
  - CRUD des trajets
  - Recherche de trajets par critères
  
- **ReservationService** : 
  - Gestion des réservations

✅ **Bonnes pratiques :**
- Logique métier centralisée
- Validation des règles métier
- Transactions gérées automatiquement (@Transactional)

---

### 6. **Couche Controller (REST API)**
📁 `com.ecole.covoiturage.controller`

Les contrôleurs exposent des **API REST** :

#### **AuthController** (`/api/auth`)
- `POST /api/auth/register` : Inscription
- `POST /api/auth/login` : Connexion
- `POST /api/auth/logout` : Déconnexion

#### **TrajetController** (`/api/trajets`)
- `GET /api/trajets` : Liste tous les trajets
- `GET /api/trajets/{id}` : Détails d'un trajet
- `POST /api/trajets` : Créer un trajet
- `PUT /api/trajets/{id}` : Modifier un trajet
- `DELETE /api/trajets/{id}` : Supprimer un trajet
- `GET /api/trajets/search?depart=X&destination=Y` : Recherche

✅ **REST Best Practices :**
- Codes HTTP appropriés (200, 201, 400, 401, 404)
- Validation des DTOs avec @Valid
- Gestion des erreurs avec messages clairs
- Sécurisation des endpoints

---

### 7. **Couche Présentation (UI Vaadin)**
📁 `com.ecole.covoiturage.ui`

Interface utilisateur avec Vaadin :
- **LoginView** : Page de connexion professionnelle
- **RegisterView** : Page d'inscription
- **HomeView** : Page d'accueil avec cartes features
- **MainLayout** : Layout principal avec navigation + déconnexion
- **TrajetView** : Proposer un trajet
- **StudentView** : Gestion des profils

✅ **Design clean et professionnel :**
- Aucun emoji, interface épurée
- Couleurs cohérentes (gradient violet)
- Composants Material Design (Vaadin)

---

### 8. **Couche Configuration et Sécurité**
📁 `com.ecole.covoiturage.config`
📁 `com.ecole.covoiturage.security`

#### **SecurityConfig**
- Configuration Spring Security
- Protection des endpoints
- Autorisation des ressources publiques (login, register, API auth)
- CSRF configuré pour les API REST

#### **SecurityUtils**
- Gestion de la session utilisateur
- Authentification/Déconnexion
- Récupération de l'utilisateur connecté

#### **AppConfig**
- Configuration du ModelMapper
- Beans applicatifs

✅ **Sécurité :**
- Mots de passe cryptés avec BCrypt
- Spring Security pour l'authentification
- Protection CSRF
- Session management

---

## 📐 Architecture C4 - Correspondance avec vos consignes

### ✅ Diagramme Contexte
- **Étudiants** → Utilisateurs du système
- **Plateforme Covoiturage** → Application Spring Boot + Vaadin
- **Base de données** → H2 (dev) / PostgreSQL (prod possible)
- **Service Mail** → À implémenter (Spring Mail)

### ✅ Diagramme Containers
- **Frontend Web** : Vaadin (UI)
- **Backend API** : Spring Boot (Controllers REST)
- **Base de données** : H2/PostgreSQL
- **Service notifications** : À implémenter

### ✅ Diagramme Composants (Backend)
- **Controllers** : AuthController, TrajetController
- **Services** : StudentService, TrajetService, ReservationService
- **Repositories** : StudentRepository, TrajetRepository, ReservationRepository
- **Mappers** : StudentMapper, TrajetMapper, ReservationMapper

### ✅ Diagramme Code
Exemples de classes : Student, Trajet, Reservation avec relations JPA

---

## 🎯 Conception Multicouche

### ✅ Domaine
- **Entités** : Student, Trajet, Reservation
- **Règles de matching** : Recherche par départ/destination

### ✅ Application (Cas d'usage)
- Proposer un trajet : `TrajetController.createTrajet()`
- Rechercher un trajet : `TrajetController.searchTrajets()`
- S'inscrire : `AuthController.register()`
- Se connecter : `AuthController.login()`

### ✅ Infrastructure
- **DB** : JPA/Hibernate + H2
- **API REST** : Spring Web (@RestController)
- **Validation** : Jakarta Validation
- **Sécurité** : Spring Security + BCrypt

---

## 🚀 Points forts de l'architecture actuelle

✅ **Séparation des préoccupations** : Chaque couche a sa responsabilité
✅ **DTO Pattern** : Protection des données sensibles
✅ **Mapper Pattern** : Conversion Entity ↔ DTO propre
✅ **DAO Pattern** : Accès aux données via repositories
✅ **REST API** : Endpoints bien structurés
✅ **Sécurité** : Spring Security + cryptage BCrypt
✅ **Validation** : DTOs validés avec Jakarta Validation
✅ **Clean Code** : Pas d'emojis, code professionnel
✅ **Lombok** : Réduction du boilerplate
✅ **Spring Boot Best Practices** : Injection de dépendances, auto-configuration

---

## 📝 Améliorations possibles (optionnelles)

🔹 **Service de notifications** : Spring Mail pour envoyer des emails
🔹 **JWT Authentication** : Pour les appels API (remplacer la session)
🔹 **Exception Handling** : @ControllerAdvice pour centraliser la gestion des erreurs
🔹 **Pagination** : Pour les listes de trajets (Pageable)
🔹 **Cache** : Spring Cache pour optimiser les performances
🔹 **Tests** : Tests unitaires et d'intégration (JUnit, Mockito)
🔹 **Documentation API** : Swagger/OpenAPI
🔹 **Docker** : Conteneurisation de l'application

---

## 🎓 Conclusion

Votre architecture suit **parfaitement les bonnes pratiques** d'une application Spring Boot professionnelle :

- ✅ Architecture en couches (Domain, Application, Infrastructure)
- ✅ Pattern DAO avec Spring Data JPA
- ✅ Pattern DTO pour la sécurité et la flexibilité
- ✅ Pattern Mapper pour la conversion
- ✅ REST API bien structurée
- ✅ Sécurité avec Spring Security
- ✅ UI professionnelle sans emojis
- ✅ Code clean et maintenable

**Votre projet est prêt pour une présentation ou une évaluation académique !** 🎉

