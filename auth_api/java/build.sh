#!/bin/bash

set -ex

cd auth_api || exit 1
mvn package
