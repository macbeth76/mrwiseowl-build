#!/bin/bash

# Mr Wise Owl Control Script
# Manage backend, frontend, and Docker services

set -e

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BACKEND_DIR="$PROJECT_DIR/backend"
FRONTEND_DIR="$PROJECT_DIR/frontend"
PID_DIR="$PROJECT_DIR/.pids"

mkdir -p "$PID_DIR"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Backend functions
backend_start() {
    log_info "Starting backend..."
    cd "$BACKEND_DIR"
    
    if [ -f "$PID_DIR/backend.pid" ]; then
        log_warn "Backend already running (PID: $(cat $PID_DIR/backend.pid))"
        return
    fi
    
    ./gradlew build -x test > /dev/null 2>&1
    nohup java -jar build/libs/mrwiseowl-0.0.1-SNAPSHOT.jar > "$PID_DIR/backend.log" 2>&1 &
    echo $! > "$PID_DIR/backend.pid"
    
    log_info "Backend starting... (PID: $!)"
    log_info "Waiting for backend to be ready..."
    sleep 8
    
    if curl -s http://localhost:8080/api/health > /dev/null; then
        log_info "Backend is ready! http://localhost:8080"
    else
        log_error "Backend failed to start. Check logs: tail -f $PID_DIR/backend.log"
    fi
}

backend_stop() {
    log_info "Stopping backend..."
    if [ -f "$PID_DIR/backend.pid" ]; then
        PID=$(cat "$PID_DIR/backend.pid")
        kill $PID 2>/dev/null || true
        rm "$PID_DIR/backend.pid"
        log_info "Backend stopped"
    else
        log_warn "Backend not running"
    fi
}

backend_restart() {
    backend_stop
    sleep 2
    backend_start
}

backend_logs() {
    if [ -f "$PID_DIR/backend.log" ]; then
        tail -f "$PID_DIR/backend.log"
    else
        log_error "No backend logs found"
    fi
}

backend_test() {
    log_info "Testing backend endpoints..."
    
    echo ""
    echo "Health Check:"
    curl -s http://localhost:8080/api/health | jq .
    
    echo ""
    echo "Towns Search:"
    curl -s "http://localhost:8080/api/towns/search?q=Asheville" | jq .
    
    echo ""
    echo "POIs:"
    curl -s "http://localhost:8080/api/pois?townId=1" | jq .
}

# Frontend functions
frontend_start() {
    log_info "Starting frontend..."
    cd "$FRONTEND_DIR"
    
    if [ -f "$PID_DIR/frontend.pid" ]; then
        log_warn "Frontend already running (PID: $(cat $PID_DIR/frontend.pid))"
        return
    fi
    
    npm install > /dev/null 2>&1
    nohup npm run dev > "$PID_DIR/frontend.log" 2>&1 &
    echo $! > "$PID_DIR/frontend.pid"
    
    log_info "Frontend starting... (PID: $!)"
    sleep 3
    log_info "Frontend ready! http://localhost:5173"
}

frontend_stop() {
    log_info "Stopping frontend..."
    if [ -f "$PID_DIR/frontend.pid" ]; then
        PID=$(cat "$PID_DIR/frontend.pid")
        kill $PID 2>/dev/null || true
        rm "$PID_DIR/frontend.pid"
        log_info "Frontend stopped"
    else
        log_warn "Frontend not running"
    fi
}

frontend_logs() {
    if [ -f "$PID_DIR/frontend.log" ]; then
        tail -f "$PID_DIR/frontend.log"
    else
        log_error "No frontend logs found"
    fi
}

# Docker functions
docker_up() {
    log_info "Starting Docker services..."
    docker-compose up -d
    log_info "Docker services started"
}

docker_down() {
    log_info "Stopping Docker services..."
    docker-compose down
    log_info "Docker services stopped"
}

docker_logs() {
    docker-compose logs -f
}

# Status
status() {
    echo ""
    echo "=== Mr Wise Owl Status ==="
    echo ""
    
    # Backend
    if [ -f "$PID_DIR/backend.pid" ]; then
        PID=$(cat "$PID_DIR/backend.pid")
        if ps -p $PID > /dev/null 2>&1; then
            echo -e "${GREEN}✓${NC} Backend running (PID: $PID) - http://localhost:8080"
        else
            echo -e "${RED}✗${NC} Backend not running (stale PID)"
            rm "$PID_DIR/backend.pid"
        fi
    else
        echo -e "${RED}✗${NC} Backend not running"
    fi
    
    # Frontend
    if [ -f "$PID_DIR/frontend.pid" ]; then
        PID=$(cat "$PID_DIR/frontend.pid")
        if ps -p $PID > /dev/null 2>&1; then
            echo -e "${GREEN}✓${NC} Frontend running (PID: $PID) - http://localhost:5173"
        else
            echo -e "${RED}✗${NC} Frontend not running (stale PID)"
            rm "$PID_DIR/frontend.pid"
        fi
    else
        echo -e "${RED}✗${NC} Frontend not running"
    fi
    
    # Docker
    if docker-compose ps 2>/dev/null | grep -q "Up"; then
        echo -e "${GREEN}✓${NC} Docker services running"
    else
        echo -e "${RED}✗${NC} Docker services not running"
    fi
    
    echo ""
}

# Start all
start_all() {
    backend_start
    sleep 2
    frontend_start
    status
}

# Stop all
stop_all() {
    frontend_stop
    backend_stop
    docker_down
    log_info "All services stopped"
}

# Main
case "$1" in
    # Backend
    backend:start)
        backend_start
        ;;
    backend:stop)
        backend_stop
        ;;
    backend:restart)
        backend_restart
        ;;
    backend:logs)
        backend_logs
        ;;
    backend:test)
        backend_test
        ;;
    
    # Frontend
    frontend:start)
        frontend_start
        ;;
    frontend:stop)
        frontend_stop
        ;;
    frontend:logs)
        frontend_logs
        ;;
    
    # Docker
    docker:up)
        docker_up
        ;;
    docker:down)
        docker_down
        ;;
    docker:logs)
        docker_logs
        ;;
    
    # All
    start)
        start_all
        ;;
    stop)
        stop_all
        ;;
    restart)
        stop_all
        sleep 2
        start_all
        ;;
    status)
        status
        ;;
    
    *)
        echo "Mr Wise Owl Control Script"
        echo ""
        echo "Usage: ./control.sh [command]"
        echo ""
        echo "Commands:"
        echo "  start              Start all services"
        echo "  stop               Stop all services"
        echo "  restart            Restart all services"
        echo "  status             Show service status"
        echo ""
        echo "Backend:"
        echo "  backend:start      Start backend"
        echo "  backend:stop       Stop backend"
        echo "  backend:restart    Restart backend"
        echo "  backend:logs       Show backend logs"
        echo "  backend:test       Test backend endpoints"
        echo ""
        echo "Frontend:"
        echo "  frontend:start     Start frontend"
        echo "  frontend:stop      Stop frontend"
        echo "  frontend:logs      Show frontend logs"
        echo ""
        echo "Docker:"
        echo "  docker:up          Start Docker services"
        echo "  docker:down        Stop Docker services"
        echo "  docker:logs        Show Docker logs"
        echo ""
        exit 1
        ;;
esac
