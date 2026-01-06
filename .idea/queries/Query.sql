-- Get complete schema dump
SELECT
    CONCAT(
            'Table: ', TABLE_NAME, '\n',
            GROUP_CONCAT(
                    CONCAT(COLUMN_NAME, ' ', COLUMN_TYPE,
                           IF(IS_NULLABLE='NO', ' NOT NULL', ''),
                           IF(COLUMN_KEY='PRI', ' PRIMARY KEY', ''),
                           IF(COLUMN_KEY='MUL', ' INDEX', ''),
                           IF(EXTRA!='', CONCAT(' ', EXTRA), ''))
                    SEPARATOR '\n'
            )
    ) AS table_structure
FROM
    INFORMATION_SCHEMA.COLUMNS
WHERE
    TABLE_SCHEMA = 'salesmanager'
GROUP BY
    TABLE_NAME;
