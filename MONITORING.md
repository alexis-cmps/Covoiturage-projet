# 📊 Monitoring Covoiturage - Guide d'installation

## Prérequis
- Grafana déjà installé sur votre Mac
- Prometheus (sera installé via Homebrew)

## 🚀 Installation et Démarrage

### 1. Installer Prometheus
```bash
brew install prometheus
```

### 2. Démarrer Prometheus
```bash
cd /Users/samy/Projet-Vaadin/Covoiturage-projet
prometheus --config.file=prometheus.yml
```

Prometheus sera accessible sur : **http://localhost:9090**

### 3. Démarrer Grafana
```bash
# Démarrer Grafana comme service
brew services start grafana

# OU manuellement (pour voir les logs)
grafana-server --config=/usr/local/etc/grafana/grafana.ini
```

Grafana sera accessible sur : **http://localhost:3000**
- Login par défaut : `admin`
- Mot de passe par défaut : `admin`

### 4. Démarrer votre application Spring Boot
```bash
./mvnw spring-boot:run
```

L'application exposera les métriques sur : **http://localhost:8080/actuator/prometheus**

## 🔧 Configuration de Grafana

### Étape 1 : Ajouter Prometheus comme source de données
1. Connectez-vous à Grafana (http://localhost:3000)
2. Allez dans **☰ Menu** → **Connections** → **Data Sources**
3. Cliquez sur **Add data source**
4. Sélectionnez **Prometheus**
5. Configurez :
   - **URL** : `http://localhost:9090`
   - **Access** : Server (par défaut)
6. Cliquez sur **Save & Test**

### Étape 2 : Importer le dashboard
1. Dans Grafana, allez dans **☰ Menu** → **Dashboards**
2. Cliquez sur **New** → **Import**
3. Cliquez sur **Upload JSON file**
4. Sélectionnez le fichier `grafana-dashboard.json` du projet
5. Sélectionnez votre source de données Prometheus
6. Cliquez sur **Import**

## 📈 Métriques disponibles

| Métrique | Description | Type |
|----------|-------------|------|
| `trajets_created_total` | Nombre total de trajets créés | Counter |
| `reservations_created_total` | Nombre total de réservations | Counter |
| `users_registered_total` | Nombre total d'utilisateurs inscrits | Counter |
| `trajets_search_duration_seconds` | Temps de recherche de trajets | Timer |
| `http_server_requests_seconds` | Latence des endpoints HTTP | Timer |

## 🎯 Panneaux du Dashboard

1. **Trajets créés (total)** - Compteur total
2. **Réservations créées (total)** - Compteur total
3. **Utilisateurs inscrits (total)** - Compteur total
4. **Temps de réponse API moyen** - Graph des latences par endpoint
5. **Temps de recherche de trajets** - Performance des recherches
6. **Requêtes HTTP par minute** - Trafic par endpoint
7. **Activité étudiants** - Actions par heure
8. **Codes de réponse HTTP** - Distribution 2xx/4xx/5xx

## 🧪 Tester le monitoring

### 1. Vérifier que les métriques sont exposées
```bash
curl http://localhost:8080/actuator/prometheus
```

Vous devriez voir des métriques comme :
```
# HELP trajets_created_total Nombre total de trajets créés
# TYPE trajets_created_total counter
trajets_created_total 0.0

# HELP reservations_created_total Nombre total de réservations créées
# TYPE reservations_created_total counter
reservations_created_total 0.0
```

### 2. Générer de l'activité
- Créez quelques trajets via l'interface
- Faites des réservations
- Inscrivez de nouveaux utilisateurs
- Effectuez des recherches de trajets

### 3. Consulter le dashboard Grafana
Ouvrez http://localhost:3000 et observez les métriques en temps réel !

## 🔍 Vérification dans Prometheus

### Requêtes utiles dans Prometheus (http://localhost:9090)
```promql
# Nombre total de trajets créés
trajets_created_total

# Taux de création de trajets par minute
rate(trajets_created_total[1m]) * 60

# Temps moyen de recherche
rate(trajets_search_duration_seconds_sum[5m]) / rate(trajets_search_duration_seconds_count[5m])

# Latence moyenne API
rate(http_server_requests_seconds_sum[5m]) / rate(http_server_requests_seconds_count[5m])
```

## 🛠️ Commandes utiles

### Contrôle des services
```bash
# Démarrer Grafana
brew services start grafana

# Arrêter Grafana
brew services stop grafana

# Redémarrer Grafana
brew services restart grafana

# Vérifier le statut
brew services list

# Voir les logs Grafana
tail -f /usr/local/var/log/grafana/grafana.log
```

### Contrôle de Prometheus
```bash
# Démarrer Prometheus (en arrière-plan)
prometheus --config.file=prometheus.yml &

# Arrêter Prometheus
pkill prometheus

# Vérifier si Prometheus tourne
ps aux | grep prometheus
```

## 🐛 Dépannage

### Prometheus ne récupère pas les métriques
1. Vérifiez que l'application Spring Boot tourne sur le port 8080
2. Testez : `curl http://localhost:8080/actuator/prometheus`
3. Vérifiez les logs de Prometheus
4. Allez sur http://localhost:9090/targets pour voir l'état de la cible

### Grafana ne montre pas de données
1. Vérifiez que Prometheus récupère bien les métriques
2. Vérifiez la connexion à la source de données dans Grafana
3. Essayez une requête simple dans l'explorateur : `trajets_created_total`

### Port déjà utilisé
```bash
# Trouver le processus qui utilise le port
lsof -i :9090  # Pour Prometheus
lsof -i :3000  # Pour Grafana
lsof -i :8080  # Pour Spring Boot

# Tuer le processus
kill -9 <PID>
```

## 📚 Ressources

- [Documentation Prometheus](https://prometheus.io/docs/)
- [Documentation Grafana](https://grafana.com/docs/)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [Micrometer Metrics](https://micrometer.io/docs)

## ✅ Checklist de démarrage

- [ ] Prometheus installé (`brew install prometheus`)
- [ ] Grafana installé (déjà fait ✅)
- [ ] Prometheus démarré avec le fichier `prometheus.yml`
- [ ] Grafana démarré et accessible sur http://localhost:3000
- [ ] Application Spring Boot démarrée
- [ ] Source de données Prometheus ajoutée dans Grafana
- [ ] Dashboard importé dans Grafana
- [ ] Métriques visibles dans le dashboard

Votre monitoring est maintenant opérationnel ! 🎉
