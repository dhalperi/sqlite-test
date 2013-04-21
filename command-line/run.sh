#!/bin/bash
echo "Create table"
time (sqlite3 test.db < create_table.sql)
rm -f test.db
echo
echo "Create table in one transaction"
time (sqlite3 test.db < create_table_trans.sql)
rm -f test.db
