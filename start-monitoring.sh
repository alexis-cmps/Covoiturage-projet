#!/bin/bash

# Script de démarrage du monitoring Covoiturage
# Usage: ./start-monitoring.sh

echo "🚀 Démarrage du monitoring Covoiturage..."
echo ""

# Couleurs pour les messages
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# 1. Vérifier Prometheus
echo "📊 Vérification de Prometheus..."
if pgrep -x "prometheus" > /dev/null; then
    echo -e "${GREEN}✓ Prometheus déjà en cours d'exécution${NC}"
else
    echo -e "${YELLOW}→ Démarrage de Prometheus...${NC}"
    prometheus --config.file=prometheus.yml > /dev/null 2>&1 &
    sleep 2
    if pgrep -x "prometheus" > /dev/null; then
        echo -e "${GREEN}✓ Prometheus démarré avec succès${NC}"
    else
        echo -e "${RED}✗ Échec du démarrage de Prometheus${NC}"
        exit 1
    fi
fi
echo "  URL: http://localhost:9090"
echo ""

# 2. Vérifier Grafana
echo "📈 Vérification de Grafana..."
if brew services list | grep grafana | grep started > /dev/null; then
    echo -e "${GREEN}✓ Grafana déjà en cours d'exécution${NC}"
else
    echo -e "${YELLOW}→ Démarrage de Grafana...${NC}"
    brew services start grafana > /dev/null 2>&1
    sleep 3
    echo -e "${GREEN}✓ Grafana démarré avec succès${NC}"
fi
echo "  URL: http://localhost:3000 (admin/admin)"
echo ""

# 3. Vérifier l'application Spring Boot
echo "🌱 Vérification de l'application Spring Boot..."
if lsof -i :8080 > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Application déjà en cours d'exécution${NC}"
    echo "  URL: http://localhost:8080"
    echo ""
    
    # Tester les métriques
    echo "🔍 Test des métriques..."
    sleep 2
    if curl -s http://localhost:8080/actuator/prometheus | grep -q "trajets_created_total"; then
        echo -e "${GREEN}✓ Métriques personnalisées détectées${NC}"
        echo ""
        echo "📊 Métriques disponibles:"
        curl -s http://localhost:8080/actuator/prometheus | grep "^# HELP trajets" | sed 's/# HELP /  - /'
        curl -s http://localhost:8080/actuator/prometheus | grep "^# HELP reservations" | sed 's/# HELP /  - /'
        curl -s http://localhost:8080/actuator/prometheus | grep "^# HELP users" | sed 's/# HELP /  - /'
    else
        echo -e "${YELLOW}⚠ Métriques personnalisées non détectées${NC}"
        echo "  Vérifiez que l'application a bien les dépendances Micrometer"
    fi
else
    echo -e "${YELLOW}⚠ L'application Spring Boot n'est pas démarrée${NC}"
    echo "  Démarrez-la avec: ./mvnw spring-boot:run"
fi

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "✅ Monitoring opérationnel !"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "📌 URLs importantes:"
echo "  • Prometheus:     http://localhost:9090"
echo "  • Grafana:        http://localhost:3000 (admin/admin)"
echo "  • Application:    http://localhost:8080"
echo "  • Métriques:      http://localhost:8080/actuator/prometheus"
echo ""
echo "📖 Prochaines étapes:"
echo "  1. Ouvrez Grafana: http://localhost:3000"
echo "  2. Ajoutez Prometheus comme source de données (URL: http://localhost:9090)"
echo "  3. Importez le dashboard: grafana-dashboard.json"
echo "  4. Consultez MONITORING.md pour plus d'informations"
echo ""
