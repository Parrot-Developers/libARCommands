#!/bin/sh 

python ./Xml/generateLibARCommands.py -debug-cmds yes -projects all || exit 1
python ../ARSDKBuildUtils/Utils/Python/ARSDK_PrebuildActions.py --lib libARCommands --root . || exit 1
