#!/bin/bash

WD=$(pwd)
EXEC=SocialSim

java -cp "${WD}/GraphStreamLib/gs-core-1.3/gs-core-1.3.jar":"${WD}/GraphStreamLib/gs-ui-1.3/gs-ui-1.3.jar":"${WD}" $EXEC $*
