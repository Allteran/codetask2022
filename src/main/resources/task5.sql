-- 1
SELEC * FROM CUSTOMERS JOIN ORDERS ON CUSTOMERS.ID = ORDERS.CustomerID;

-- 2
UPDATE CUSTOMERS
SET Type='Corn'
WHERE ID = 2;

-- 3
SELECT
    Country,
    SUM (CASE
             WHEN Country='UK' THEN 1,
             ELSE 0
        END)
FROM CUSTOMERS;