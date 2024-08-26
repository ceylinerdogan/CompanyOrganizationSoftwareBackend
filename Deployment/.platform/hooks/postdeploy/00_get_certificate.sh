#!/usr/bin/env bash

if ! grep -q letsencrypt </etc/nginx/nginx.conf; then
  sudo certbot -n -d delta1.eu-west-1.elasticbeanstalk.com --nginx --agree-tos --email cerdoga5@gmail.com
fi
