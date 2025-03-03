INSERT INTO bin (bin_code, zone, aisle, row_num, floor, amount)
SELECT
    CONCAT(z.zone, '-', LPAD(a.aisle, 2, '0'), '-', LPAD(r.row_num, 2, '0'), '-', LPAD(f.floor, 2, '0')) AS bin_code,
    z.zone, a.aisle, r.row_num, f.floor, 0
FROM
    (SELECT 'A' AS zone UNION ALL SELECT 'B' UNION ALL SELECT 'C' UNION ALL SELECT 'D' UNION ALL SELECT 'E' UNION ALL SELECT 'F') AS z
        CROSS JOIN
    (SELECT 1 AS aisle UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6) AS a
        CROSS JOIN
    (SELECT 1 AS row_num UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6) AS r
        CROSS JOIN
    (SELECT 1 AS floor UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6) AS f
WHERE NOT EXISTS (
    SELECT 1 FROM bin b WHERE b.bin_code = CONCAT(z.zone, '-', LPAD(a.aisle, 2, '0'), '-', LPAD(r.row_num, 2, '0'), '-', LPAD(f.floor, 2, '0'))
)
ORDER BY z.zone ASC, a.aisle ASC, r.row_num ASC, f.floor ASC;

