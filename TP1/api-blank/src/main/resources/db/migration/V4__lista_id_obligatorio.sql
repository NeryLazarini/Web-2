-- 1. Creamos una lista por defecto (solo si no existe ya una con ese nombre)
INSERT INTO listas (nombre) 
SELECT 'Sin clasificar' 
WHERE NOT EXISTS (SELECT 1 FROM listas WHERE nombre = 'Sin clasificar');

-- 2. Le asignamos esa lista a cualquier favorito viejo que haya quedado huérfano (lista_id nulo)
UPDATE favoritos 
SET lista_id = (SELECT id FROM listas WHERE nombre = 'Sin clasificar' LIMIT 1)
WHERE lista_id IS NULL;

-- 3. Ahora que no hay nulos, recién aplicamos la restricción fuerte
ALTER TABLE favoritos ALTER COLUMN lista_id SET NOT NULL;