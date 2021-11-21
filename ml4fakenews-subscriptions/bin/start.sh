#!/bin/bash

# CONFIGURE AND START DATADOG AGENT IF API KEY PROVIDED
if [ -n "$DD_API_KEY" ]; then
  DD_INSTALL_ONLY=true DD_AGENT_MAJOR_VERSION=7 DD_SITE="datadoghq.com" \
    bash -c "$(curl -L https://s3.amazonaws.com/dd-agent/scripts/install_script.sh)"

  echo "logs_enabled: true" >> /etc/datadog-agent/datadog.yaml

  mkdir /etc/datadog-agent/conf.d/java.d
  cat <<EOM > /etc/datadog-agent/conf.d/java.d/conf.yaml
#Log section
logs:
  - type: file
    path: "/logs/app.log"
    service: subscription-service
    source: java
    sourcecategory: sourcecode
EOM

  service datadog-agent start
fi

java -jar /app.jar