ARG REGISTRY_URL

FROM $REGISTRY_URL/msr-base-jdbc:11.1.0.0

ADD --chown=1724 . /opt/softwareag/IntegrationServer/packages/qdtContactManagement
