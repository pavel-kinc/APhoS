#!/bin/bash
#source:https://github.com/CESNET/perun/blob/master/perun-cli-python/generate.sh, Martin Kuba
GENERATOR_VERSION=6.2.1
if [ ! -f  "openapi-generator-cli-$GENERATOR_VERSION.jar" ] ; then
  wget https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/$GENERATOR_VERSION/openapi-generator-cli-$GENERATOR_VERSION.jar
fi

rm -rf aphos_openapi
# see https://openapi-generator.tech/docs/usage#generate
# and https://openapi-generator.tech/docs/generators/python-prior
java \
 --add-opens java.base/java.util=ALL-UNNAMED \
 --add-opens java.base/java.lang=ALL-UNNAMED \
 -DapiDocs=false \
 -DapiTests=false \
 -DmodelDocs=false \
 -DmodelTests=false \
 -jar openapi-generator-cli-$GENERATOR_VERSION.jar generate \
 --generator-name python-prior \
 --input-spec ../openapi.yml \
 --model-package model \
 --additional-properties=generateSourceCodeOnly=true,packageName=aphos_openapi