oc create serviceaccount image-puller-sa
oc policy add-role-to-user system:image-puller system:serviceaccount:helloworld:image-puller-sa
oc create token image-puller-sa

oc create secret docker-registry ocp-regcred --docker-server=${OCP_CR_URL} --docker-username=${OCP_CR_USERNAME} --docker-password=$(oc create token image-puller-sa)


oc adm policy add-scc-to-user privileged -z msr-contact-management

oc apply -f ./resources/helm/msr-secrets.yaml
oc create secret generic sttlab-keystore --from-file=$HOME/tls/sttlab.eu.jks

helm upgrade --install contact-management webmethods/microservicesruntime -f ./resources/helm/msr-values.yaml
