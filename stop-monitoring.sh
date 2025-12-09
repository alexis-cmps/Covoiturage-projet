#!/bin/bash

# Script d'arrêt du monitoring Covoiturage
# Usage: ./stop-monitoring.sh

echo "🛑 Arrêt du monitoring Covoiturage..."
echo ""

# Couleurs pour les messages
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# 1. Arrêter Prometheus
echo "📊 Arrêt de Prometheus..."
if pgrep -x "prometheus" > /dev/null; then
    pkill prometheus
    echo -e "${GREEN}✓ Prometheus arrêté${NC}"
else
    echo "  Prometheus n'était pas en cours d'exécution"
fi

# 2. Arrêter Grafana
echo "📈 Arrêt de Grafana..."
if brew services list | grep grafana | grep started > /dev/null; then
    brew services stop grafana > /dev/null 2>&1
    echo -e "${GREEN}✓ Grafana arrêté${NC}"
else
    echo "  Grafana n'était pas en cours d'exécution"
fi

echo ""
echo "✅ Monitoring arrêté"
echo "   (L'application Spring Boot continue de tourner si elle était démarrée)"
