# Día 9: Movie Theater

En el noveno día del Advent of Code 2025, el problema se desarrolla en el cine de la base del Polo Norte. El suelo del cine está formado por una gran cuadrícula de baldosas. Algunas baldosas son rojas y los elfos quieren usarlas para formar rectángulos.

El input del problema contiene una lista de posiciones de baldosas rojas. Cada línea tiene el formato:

```text
x,y
```

Por ejemplo:

```text
7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3
```

Cada posición indica la coordenada de una baldosa roja dentro de la cuadrícula.

---

## Parte 1

En la primera parte, el objetivo es encontrar el rectángulo de mayor área posible usando dos baldosas rojas como esquinas opuestas.

Se puede elegir cualquier par de baldosas rojas. A partir de esas dos posiciones, se forma un rectángulo alineado con la cuadrícula.

El área se calcula teniendo en cuenta que las baldosas ocupan posiciones enteras. Por eso, si las coordenadas de dos esquinas son distintas, se suma `1` tanto al ancho como al alto:

```text
width = abs(x1 - x2) + 1
height = abs(y1 - y2) + 1
area = width * height
```

En el ejemplo oficial, el mayor rectángulo tiene área `50`.

Con el input real, el resultado obtenido para la parte 1 fue:

```text
4759930955
```

---

## Parte 2

En la segunda parte, aparece una restricción adicional: el rectángulo elegido solo puede incluir baldosas rojas o verdes.

Las baldosas rojas del input forman una frontera. Cada baldosa roja está conectada con la anterior y la siguiente mediante líneas rectas de baldosas verdes. Además, todas las baldosas que quedan dentro del bucle formado por esa frontera también son verdes.

Por tanto, la región válida está formada por:

* Las baldosas rojas.
* Las baldosas verdes de la frontera.
* Las baldosas verdes interiores.

El rectángulo sigue teniendo que usar dos baldosas rojas como esquinas opuestas, pero ahora todo el rectángulo debe estar contenido dentro de la región roja/verde.

En el ejemplo oficial, el mayor rectángulo válido tiene área `24`.

Con el input real, el resultado obtenido para la parte 2 fue:

```text
1525241870
```

---

# Estructura del proyecto

La solución del Día 9 mantiene la misma estructura modular usada en los días anteriores:

```text
day09
├── Day09Main.java
├── common
│   ├── RedTileMap.java
│   ├── RedTileMapParser.java
│   └── RedTilePosition.java
├── part1
│   ├── Day09Part1Solver.java
│   └── LargestRedCornerRectangleAnalyzer.java
└── part2
    ├── Day09Part2Solver.java
    ├── LargestRedGreenRectangleAnalyzer.java
    ├── RedGreenTileRegion.java
    └── TileRectangle.java
```

La idea principal es separar:

* El punto de entrada del día.
* Las clases comunes del dominio.
* El parseo de posiciones.
* La lógica específica de la parte 1.
* La lógica geométrica adicional de la parte 2.
* La representación de rectángulos y regiones válidas.

---

# Clases del paquete `day09.common`

El paquete `day09.common` contiene las clases compartidas por ambas partes.

---

## `RedTilePosition`

El record `RedTilePosition` representa la posición de una baldosa roja dentro de la cuadrícula.

Está formado por dos coordenadas:

```java
public record RedTilePosition(int x, int y)
```

Su método principal es:

```java
public long rectangleAreaWith(RedTilePosition other)
```

Este método calcula el área del rectángulo formado usando esta posición y otra posición como esquinas opuestas.

El cálculo utiliza coordenadas inclusivas, porque se cuentan las baldosas ocupadas por el rectángulo:

```text
width = abs(x1 - x2) + 1
height = abs(y1 - y2) + 1
area = width * height
```

---

## `RedTileMap`

El record `RedTileMap` representa el conjunto de baldosas rojas del input.

Internamente almacena una lista de posiciones:

```java
public record RedTileMap(List<RedTilePosition> redTiles)
```

Esta clase valida que:

* La lista de baldosas rojas no sea `null`.
* Haya al menos dos baldosas rojas.

Además, copia la lista usando `List.copyOf`, evitando modificaciones externas.

Sus métodos principales son:

```java
public int size()
```

Devuelve el número de baldosas rojas.

```java
public RedTilePosition redTileAt(int index)
```

Devuelve una baldosa roja por índice.

---

## `RedTileMapParser`

La clase `RedTileMapParser` transforma las líneas del input en un objeto `RedTileMap`.

Cada línea tiene el formato:

```text
x,y
```

El parser separa la línea por la coma, convierte las coordenadas a enteros y crea un objeto `RedTilePosition`.

Su responsabilidad es únicamente interpretar el formato de entrada. No calcula áreas ni valida rectángulos.

---

# Clases del paquete `day09.part1`

El paquete `day09.part1` contiene la solución específica de la primera parte.

---

## `Day09Part1Solver`

La clase `Day09Part1Solver` resuelve la primera parte del Día 9.

Implementa la interfaz común `PuzzleSolver`.

Su método `solve` realiza los siguientes pasos:

1. Usa `RedTileMapParser` para convertir el input en un `RedTileMap`.
2. Usa `LargestRedCornerRectangleAnalyzer`.
3. Calcula el área máxima usando cualquier par de baldosas rojas como esquinas opuestas.
4. Devuelve el resultado.

---

## `LargestRedCornerRectangleAnalyzer`

La clase `LargestRedCornerRectangleAnalyzer` contiene la lógica principal de la parte 1.

Su método principal es:

```java
public long largestRectangleAreaIn(RedTileMap map)
```

El algoritmo funciona así:

1. Recorre todos los pares posibles de baldosas rojas.
2. Para cada par, calcula el área del rectángulo formado.
3. Compara esa área con la mayor encontrada hasta el momento.
4. Devuelve el área máxima.

Esta solución es directa, porque en la parte 1 no importa qué baldosas quedan dentro del rectángulo. Solo importan las dos esquinas rojas.

---

# Clases del paquete `day09.part2`

El paquete `day09.part2` contiene la solución específica de la segunda parte.

---

## `Day09Part2Solver`

La clase `Day09Part2Solver` resuelve la segunda parte del Día 9.

También implementa la interfaz `PuzzleSolver`.

Su método `solve` realiza los siguientes pasos:

1. Usa `RedTileMapParser` para convertir el input en un `RedTileMap`.
2. Usa `LargestRedGreenRectangleAnalyzer`.
3. Calcula el mayor rectángulo válido dentro de la región roja/verde.
4. Devuelve el resultado.

---

## `LargestRedGreenRectangleAnalyzer`

La clase `LargestRedGreenRectangleAnalyzer` contiene la lógica principal de la parte 2.

Su método principal es:

```java
public long largestRectangleAreaIn(RedTileMap map)
```

El algoritmo funciona así:

1. Obtiene la lista de baldosas rojas.
2. Construye una región roja/verde usando `RedGreenTileRegion`.
3. Recorre todos los pares posibles de baldosas rojas.
4. Crea un `TileRectangle` entre cada par.
5. Calcula su área.
6. Si el área no supera la mejor encontrada, descarta ese rectángulo.
7. Si el área es mayor, comprueba si el rectángulo está contenido en la región válida.
8. Si es válido, actualiza el área máxima.

Esta clase añade la restricción de que el rectángulo completo debe estar formado solo por baldosas rojas o verdes.

---

## `TileRectangle`

El record `TileRectangle` representa un rectángulo dentro de la cuadrícula.

Está formado por:

```java
public record TileRectangle(int minX, int maxX, int minY, int maxY)
```

Esta clase valida que:

* `minX` no sea mayor que `maxX`.
* `minY` no sea mayor que `maxY`.

Su método estático principal es:

```java
public static TileRectangle between(RedTilePosition first, RedTilePosition second)
```

Crea un rectángulo a partir de dos posiciones.

Sus métodos principales son:

```java
public long area()
```

Calcula el área del rectángulo.

```java
public List<RedTilePosition> corners()
```

Devuelve las cuatro esquinas del rectángulo.

```java
public boolean hasStrictInteriorColumn(int x)
public boolean hasStrictInteriorRow(int y)
```

Comprueban si una columna o fila está estrictamente dentro del rectángulo.

```java
public boolean verticalRangeIntersectsInterior(int firstY, int secondY)
public boolean horizontalRangeIntersectsInterior(int firstX, int secondX)
```

Comprueban si un segmento vertical u horizontal atraviesa el interior del rectángulo.

---

## `RedGreenTileRegion`

La clase `RedGreenTileRegion` representa la región válida formada por las baldosas rojas y verdes.

Recibe como frontera la lista de baldosas rojas del input:

```java
public RedGreenTileRegion(List<RedTilePosition> boundary)
```

Esta clase valida que:

* La frontera no sea `null`.
* La frontera tenga al menos cuatro puntos.

Su método principal es:

```java
public boolean contains(TileRectangle rectangle)
```

Este método comprueba si un rectángulo está completamente dentro de la región roja/verde.

Para ello, verifica dos condiciones:

1. Todas las esquinas del rectángulo deben estar dentro de la región o sobre su frontera.
2. Ningún segmento de la frontera debe atravesar el interior del rectángulo.

La clase usa varios métodos auxiliares:

* `containsAllCornersOf`: comprueba las esquinas del rectángulo.
* `isOnBoundary`: comprueba si una posición está sobre la frontera.
* `isInside`: comprueba si una posición está dentro de la región.
* `hasBoundaryInside`: comprueba si la frontera atraviesa el interior del rectángulo.

Para saber si una posición está dentro de la región, se usa una técnica de intersección con un rayo horizontal.

---

# Clase del paquete `day09`

El paquete `day09` contiene la clase principal del Día 9.

---

## `Day09Main`

La clase `Day09Main` es el punto de entrada para ejecutar la solución completa del Día 9.

El método `main` realiza los siguientes pasos:

1. Lee todas las líneas del archivo `src/main/resources/day09/input.txt`.
2. Crea una instancia de `Day09Part1Solver`.
3. Crea una instancia de `Day09Part2Solver`.
4. Ejecuta el método `solve` de ambos solvers.
5. Guarda los resultados.
6. Imprime los resultados por consola.

Esta clase utiliza la interfaz `PuzzleSolver` para referenciar ambos solvers:

```java
PuzzleSolver part1Solver = new Day09Part1Solver();
PuzzleSolver part2Solver = new Day09Part2Solver();
```

---

# Interfaz común del proyecto

El proyecto utiliza la interfaz común `PuzzleSolver`, ubicada en el paquete `aoc2025.common`.

Esta interfaz define el contrato común para todos los solvers del Advent of Code:

```java
long solve(List<String> lines);
```

En el Día 9, tanto `Day09Part1Solver` como `Day09Part2Solver` implementan esta interfaz.

---

# Fundamentos de diseño utilizados

En la solución del Día 9 se utilizan los siguientes fundamentos de diseño:

* Alta cohesión.
* Bajo acoplamiento.
* Modularidad.
* Código expresivo.
* Abstracción.

---

# Principios de diseño aplicados

En la solución del Día 9 se aplican los siguientes principios de diseño:

* Principio de Responsabilidad Única, SRP.
* Principio Abierto/Cerrado, OCP.
* Principio de Sustitución de Liskov, LSP.
* Principio de Segregación de Interfaces, ISP.
* Principio de Inversión de Dependencias, DIP.
* Principio DRY.
* Ley de Demeter.
* Principio YAGNI.
* Principio de mínima sorpresa.

---

# Patrones de diseño aplicados

En la solución del Día 9 se utilizan los siguientes patrones de diseño:

* Iterator.

---

# Patrones no aplicados

En la solución del Día 9 no se aplican los siguientes patrones de diseño:

* Singleton.
* Factory Method.
* Adapter.
* Decorator.
* Observer.

---
