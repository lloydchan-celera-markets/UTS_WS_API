#!/bin/bash

find . -name *.jar -exec cp -p {} $CMBOS/lib \;
cp -p celera_cmbos/cfg/* $CMBOS/cfg

