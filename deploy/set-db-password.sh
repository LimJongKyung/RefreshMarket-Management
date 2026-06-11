#!/usr/bin/env bash
set -euo pipefail

read -rsp "REFRESHMARKET database password: " db_password
printf '\n'
read -rsp "Confirm database password: " db_password_confirm
printf '\n'

if [[ "$db_password" != "$db_password_confirm" ]]; then
    echo "Passwords do not match." >&2
    exit 1
fi

sudo sed -i '/^DB_PASSWORD=/d' /opt/refreshmarket-management/management.env
printf 'DB_PASSWORD=%s\n' "$db_password" | sudo tee -a /opt/refreshmarket-management/management.env >/dev/null
unset db_password db_password_confirm

sudo chmod 600 /opt/refreshmarket-management/management.env
sudo systemctl restart management
sudo systemctl status management --no-pager
