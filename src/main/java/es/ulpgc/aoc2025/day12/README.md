# Día 12: Christmas Tree Farm

En el duodécimo día del Advent of Code 2025, el problema se desarrolla en una gran granja subterránea de árboles de Navidad. Los elfos están decorando los árboles, pero necesitan comprobar si los regalos caben correctamente bajo cada árbol.

Los regalos tienen formas irregulares representadas en una cuadrícula bidimensional. Cada forma se describe usando:

* `#`: parte ocupada por el regalo.
* `.`: espacio vacío dentro del dibujo de la forma.

Además, el input contiene regiones rectangulares bajo los árboles, junto con la cantidad de regalos de cada forma que deben colocarse en cada región.

Ejemplo:

```text id="day12-example"
0:
###
##.
##.

1:
###
##.
.##

2:
.##
###
##.

3:
##.
###
##.

4:
###
#..
###

5:
###
.#.
###

4x4: 0 0 0 0 2 0
12x5: 1 0 1 0 2 2
12x5: 1 0 1 0 3 2
```

La primera sección define las formas de los regalos. La segunda sección define las regiones bajo los árboles y cuántos regalos de cada tipo deben caber en ellas.

Los regalos pueden rotarse y voltearse, pero deben colocarse alineados con la cuadrícula. No pueden solaparse entre sí.

---

## Parte 1

En la primera parte, el objetivo es contar cuántas regiones pueden contener todos los regalos indicados.

Para cada región, hay que comprobar si es posible colocar todos los regalos requeridos respetando estas condiciones:

1. Cada regalo debe colocarse dentro de la región.
2. Los regalos pueden rotarse.
3. Los regalos pueden voltearse.
4. Las partes `#` de dos regalos no pueden ocupar la misma celda.
5. Las partes `.` de una forma no bloquean espacio.
6. No se pueden apilar regalos.

En el ejemplo oficial hay tres regiones:

```text id="day12-regions"
4x4: 0 0 0 0 2 0
12x5: 1 0 1 0 2 2
12x5: 1 0 1 0 3 2
```

Las dos primeras regiones pueden contener todos sus regalos, pero la tercera no.

Por tanto, el resultado del ejemplo es:

```text id="day12-example-result"
2
```

Con el input real, el resultado obtenido para la parte 1 fue:

```text id="day12-part1-result"
544
```

---

## Parte 2

En el enunciado del Día 12, la segunda parte no introduce una nueva tarea computacional como en los días anteriores.

La historia continúa con la llegada de más elfos y estrellas para decorar los árboles, pero no se proporciona un nuevo cálculo, una nueva regla o un nuevo resultado que implementar.

Por eso, en el código del proyecto solo existe la solución de la parte 1:

```text id="day12-only-part1"
Day12Part1Solver
```

y la clase principal ejecuta únicamente esa parte.

---

# Estructura del proyecto

La solución del Día 12 mantiene una estructura modular similar a la usada en los días anteriores:

```text id="day12-structure"
day12
├── Day12Main.java
├── common
│   ├── ChristmasTreeFarmReport.java
│   ├── ChristmasTreeFarmReportParser.java
│   ├── GridCell.java
│   ├── PresentFootprint.java
│   ├── PresentShape.java
│   └── TreeRegionRequirement.java
└── part1
    ├── Day12Part1Solver.java
    └── PresentPackingAnalyzer.java
```

La idea principal es separar:

* El punto de entrada del día.
* Las clases comunes del dominio.
* El parseo del informe.
* La representación de formas y regiones.
* La generación de transformaciones de los regalos.
* La lógica específica de comprobación de empaquetado.

---

# Clases del paquete `day12.common`

El paquete `day12.common` contiene las clases compartidas del dominio del Día 12.

---

## `GridCell`

El record `GridCell` representa una celda dentro de una cuadrícula.

Está formado por:

```java id="day12-gridcell"
public record GridCell(int row, int column)
```

Se usa para representar las posiciones ocupadas por una forma de regalo.

---

## `PresentFootprint`

El record `PresentFootprint` representa la huella ocupada por una forma concreta de regalo.

Está formado por una lista de celdas:

```java id="day12-footprint"
public record PresentFootprint(List<GridCell> cells)
```

Cada celda representa una posición ocupada por un `#`.

Esta clase valida que:

* La lista de celdas no sea `null`.
* La lista no esté vacía.

Además, ordena las celdas por fila y columna para tener una representación estable.

Sus métodos principales son:

```java id="day12-footprint-methods"
public int area()
public int height()
public int width()
```

`area` devuelve el número de celdas ocupadas.

`height` devuelve la altura de la huella.

`width` devuelve la anchura de la huella.

---

## `PresentShape`

El record `PresentShape` representa una forma estándar de regalo.

Está formado por:

```java id="day12-shape"
public record PresentShape(int index, List<String> rows)
```

* `index`: índice de la forma.
* `rows`: representación visual de la forma con `#` y `.`.

Esta clase valida que:

* El índice no sea negativo.
* La lista de filas no sea `null`.
* La lista de filas no esté vacía.
* Todas las filas tengan la misma anchura.
* Solo se usen los caracteres `.` y `#`.

Su método:

```java id="day12-shape-area"
public int area()
```

calcula cuántas celdas ocupa realmente el regalo.

Su método más importante es:

```java id="day12-transformations"
public List<PresentFootprint> transformations()
```

Este método genera todas las transformaciones posibles de la forma:

* Rotaciones.
* Reflejos.
* Normalización de coordenadas.

Para evitar duplicados, usa un `LinkedHashSet`.

---

## `TreeRegionRequirement`

El record `TreeRegionRequirement` representa una región bajo un árbol y los regalos que deben caber en ella.

Está formado por:

```java id="day12-region"
public record TreeRegionRequirement(
        int width,
        int height,
        List<Integer> quantities
)
```

* `width`: anchura de la región.
* `height`: altura de la región.
* `quantities`: cantidad necesaria de cada forma de regalo.

Esta clase valida que:

* La anchura sea positiva.
* La altura sea positiva.
* La lista de cantidades no sea `null`.

Sus métodos principales son:

```java id="day12-region-methods"
public long area()
public int presentCount()
public long requiredArea(Map<Integer, PresentShape> shapesByIndex)
```

`area` calcula el área disponible.

`presentCount` calcula cuántos regalos se deben colocar en total.

`requiredArea` calcula cuánta área ocupan todos los regalos requeridos.

---

## `ChristmasTreeFarmReport`

El record `ChristmasTreeFarmReport` representa el informe completo del Día 12.

Está formado por:

```java id="day12-report"
public record ChristmasTreeFarmReport(
        Map<Integer, PresentShape> shapesByIndex,
        List<TreeRegionRequirement> regions
)
```

Contiene:

* Las formas de regalo indexadas por número.
* Las regiones que hay que comprobar.

Esta clase valida que:

* El mapa de formas no sea `null`.
* La lista de regiones no sea `null`.
* Exista al menos una forma.

Además, copia el mapa y la lista para evitar modificaciones externas.

Su método principal es:

```java id="day12-shape-by-index"
public PresentShape shapeByIndex(int index)
```

Este método devuelve una forma por índice y lanza una excepción si no existe.

---

## `ChristmasTreeFarmReportParser`

La clase `ChristmasTreeFarmReportParser` transforma las líneas del input en un `ChristmasTreeFarmReport`.

El input tiene dos tipos de información:

1. Definiciones de formas.
2. Requisitos de regiones.

El parser usa expresiones regulares para detectar:

```text id="day12-shape-regex"
0:
```

como cabecera de forma, y:

```text id="day12-region-regex"
12x5: 1 0 1 0 2 2
```

como línea de región.

Sus responsabilidades principales son:

1. Recorrer las líneas del input.
2. Ignorar líneas en blanco.
3. Detectar cabeceras de forma.
4. Leer las filas visuales de cada forma.
5. Detectar líneas de región.
6. Parsear anchura, altura y cantidades.
7. Construir un `ChristmasTreeFarmReport`.

---

# Clases del paquete `day12.part1`

El paquete `day12.part1` contiene la solución específica de la primera parte.

---

## `Day12Part1Solver`

La clase `Day12Part1Solver` resuelve la primera parte del Día 12.

Implementa la interfaz común `PuzzleSolver`.

Su método `solve` realiza estos pasos:

1. Usa `ChristmasTreeFarmReportParser` para convertir el input en un `ChristmasTreeFarmReport`.
2. Recorre todas las regiones del informe.
3. Usa `PresentPackingAnalyzer` para comprobar si los regalos caben en cada región.
4. Cuenta cuántas regiones son válidas.
5. Devuelve el total.

---

## `PresentPackingAnalyzer`

La clase `PresentPackingAnalyzer` contiene la lógica principal para comprobar si un conjunto de regalos puede caber en una región.

Su método principal es:

```java id="day12-canfit"
public boolean canFit(
        ChristmasTreeFarmReport report,
        TreeRegionRequirement region
)
```

El algoritmo combina varias estrategias:

1. Comprobación rápida por área.
2. Comprobación aproximada usando cajas envolventes.
3. Búsqueda exacta en regiones pequeñas.

Primero, si el área total requerida por los regalos supera el área de la región, devuelve `false`.

Después, intenta una comprobación rápida usando cajas envolventes.

Si la región es suficientemente pequeña, usa búsqueda exacta mediante máscaras de bits.

La constante:

```java id="day12-exact-limit"
private static final int EXACT_SEARCH_MAX_CELLS = 63;
```

indica el límite máximo de celdas para usar búsqueda exacta con una máscara `long`.

La búsqueda exacta funciona así:

1. Expande la lista de regalos según las cantidades requeridas.
2. Ordena los regalos para probar primero los que tienen menos colocaciones posibles.
3. Genera todas las colocaciones posibles de cada forma dentro de la región.
4. Representa cada colocación como una máscara de bits.
5. Usa backtracking para intentar colocar regalos sin solapamientos.
6. Guarda estados fallidos para no repetir búsquedas inútiles.

---

# Clase del paquete `day12`

El paquete `day12` contiene la clase principal del Día 12.

---

## `Day12Main`

La clase `Day12Main` es el punto de entrada para ejecutar la solución del Día 12.

El método `main` realiza los siguientes pasos:

1. Lee todas las líneas del archivo `src/main/resources/day12/input.txt`.
2. Crea una instancia de `Day12Part1Solver`.
3. Ejecuta el método `solve`.
4. Guarda el resultado.
5. Imprime el resultado por consola.

A diferencia de otros días, aquí solo se ejecuta la parte 1 porque la parte 2 no contiene un nuevo problema computacional.

---

# Interfaz común del proyecto

El proyecto utiliza la interfaz común `PuzzleSolver`, ubicada en el paquete `aoc2025.common`.

Esta interfaz define el contrato común para todos los solvers del Advent of Code:

```java id="day12-puzzle-solver"
long solve(List<String> lines);
```

En el Día 12, `Day12Part1Solver` implementa esta interfaz.

---

# Fundamentos de diseño utilizados

En la solución del Día 12 se utilizan los siguientes fundamentos de diseño:

* Alta cohesión.
* Bajo acoplamiento.
* Modularidad.
* Código expresivo.
* Abstracción.
* Encapsulación.
* Diseño por contrato.
* Inmutabilidad.
* Modelado de cuadrículas.
* Transformaciones geométricas.
* Backtracking.
* Memoización de estados fallidos.
* Representación mediante máscaras de bits.
* Optimización por poda.

---

# Principios de diseño aplicados

En la solución del Día 12 se aplican los siguientes principios de diseño:

* Principio de Responsabilidad Única, SRP.
* Principio Abierto/Cerrado, OCP.
* Principio de Sustitución de Liskov, LSP.
* Principio de Segregación de Interfaces, ISP.
* Principio de Inversión de Dependencias, DIP.
* Composición sobre herencia.
* Principio DRY.
* Ley de Demeter.
* Principio YAGNI.
* Principio de mínima sorpresa.

---

# Patrones de diseño aplicados

En la solución del Día 12 se utilizan los siguientes patrones de diseño:

* Iterator.
* Strategy.
* Command, aplicado parcialmente.

---

# Patrones no aplicados

En la solución del Día 12 no se aplican los siguientes patrones de diseño:

* Singleton.
* Factory Method.
* Adapter.
* Decorator.
* Observer.
* Template Method.
* State.

---

# Conclusión

La solución del Día 12 está organizada de forma clara y modular.

El problema consiste en comprobar si distintos conjuntos de regalos con formas irregulares pueden colocarse dentro de regiones rectangulares. Para ello, el código modela las formas, genera transformaciones mediante rotaciones y reflejos, calcula áreas y realiza una búsqueda exacta cuando la región es suficientemente pequeña.

El diseño separa correctamente el parseo, el modelo del informe, las formas de regalos, las regiones y la lógica de empaquetado. Esto permite que el código sea más fácil de entender, probar, mantener y defender en una explicación oral.
