#!/bin/sh

java -cp /opt/devel/hsqldb-2.3.0/hsqldb/lib/hsqldb.jar org.hsqldb.util.DatabaseManagerSwing --url jdbc:hsqldb:hsql://localhost/workdb
