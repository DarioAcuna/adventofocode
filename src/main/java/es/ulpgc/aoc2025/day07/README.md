# Día 7: Laboratories

En el séptimo día del Advent of Code 2025, el problema se desarrolla en un laboratorio de teletransportación del Polo Norte. Al entrar en una plataforma de teletransporte, acabas atrapado en una sala sin puertas. Para reparar el teletransportador, necesitas analizar un colector de taquiones.

El input del problema es un diagrama del colector. En él aparecen distintos caracteres:

* `S`: posición inicial por donde entra el rayo de taquiones.
* `.`: espacio vacío por donde el rayo puede avanzar.
* `^`: divisor o splitter, que divide el rayo en dos caminos.

Los rayos de taquiones siempre se mueven hacia abajo. Si un rayo encuentra un splitter, el rayo se detiene y se generan dos nuevos rayos: uno desde la posición inmediata a la izquierda y otro desde la posición inmediata a la derecha.

Ejemplo:

```text id="d7-example-1"
.......S.......
...............
.......^.......
...............
......^.^......
...............
.....^.^.^.....
...............
....^.^...^....
...............
...^.^...^.^...
...............
..^...^.....^..
...............
.^.^.^.^.^...^.
...............
```

---

## Parte 1

En la primera parte, el objetivo es contar cuántas veces se divide el rayo de taquiones.

El rayo empieza en la posición marcada con `S` y avanza hacia abajo. Cuando encuentra un splitter `^`, se cuenta una división y el rayo se separa en dos nuevos rayos, uno hacia la columna izquierda y otro hacia la columna derecha.

Si varios rayos llegan a la misma posición, se consideran como un único rayo activo en esa columna para continuar el proceso.

En el ejemplo oficial, el rayo se divide un total de `21` veces.

Con el input real, el resultado obtenido para la parte 1 fue:

```text id="d7-part1-result"
1626
```

---

## Parte 2

En la segunda parte, el colector deja de interpretarse como un sistema clásico y pasa a ser un colector cuántico.

Ahora no se envían múltiples rayos, sino una única partícula de taquiones. Cada vez que la partícula llega a un splitter, el tiempo se divide en dos líneas temporales:

* En una línea temporal, la partícula va hacia la izquierda.
* En la otra línea temporal, la partícula va hacia la derecha.

El objetivo ya no es contar cuántos splitters se activan, sino cuántas líneas temporales existen al final de todos los posibles recorridos.

En el ejemplo oficial, la partícula acaba en `40` líneas temporales distintas.

Con el input real, el resultado obtenido para la parte 2 fue:

```text id="d7-part2-result"
48989920237096
```

---

# Estructura del proyecto

La solución del Día 7 mantiene la misma estructura modular usada en los días anteriores:

```text id="d7-structure"
day07
├── Day07Main.java
├── common
│   ├── Position.java
│   ├── TachyonManifold.java
│   └── TachyonManifoldParser.java
├── part1
│   ├── Day07Part1Solver.java
│   └── TachyonSplitCounter.java
└── part2
    ├── Day07Part2Solver.java
    └── QuantumTimelineCounter.java
```

La idea principal es separar:

* El punto de entrada del día.
* Las clases comunes del dominio.
* El parseo del diagrama.
* La lógica específica de la parte 1.
* La lógica específica de la parte 2.

---

# Clases del paquete `day07.common`

El paquete `day07.common` contiene las clases comunes utilizadas por ambas partes.

---

## `Position`

El record `Position` representa una posición dentro del diagrama del colector.

Está formado por dos valores:

```java id="d7-position-record"
public record Position(int row, int column)
```

* `row`: fila de la posición.
* `column`: columna de la posición.

Además, incluye métodos que permiten obtener posiciones cercanas:

```java id="d7-position-methods"
public Position below()
public Position left()
public Position right()
```

Estos métodos ayudan a expresar los movimientos del rayo de forma clara.

---

## `TachyonManifold`

El record `TachyonManifold` representa el diagrama completo del colector de taquiones.

Internamente almacena una lista de filas:

```java id="d7-manifold-record"
public record TachyonManifold(List<String> rows)
```

Esta clase valida que el diagrama sea correcto:

* La lista de filas no puede ser `null`.
* La lista de filas no puede estar vacía.
* Ninguna fila puede ser `null`.
* Todas las filas deben tener la misma anchura.
* Solo se permiten los caracteres `.`, `S` y `^`.
* Debe existir exactamente una posición inicial `S`.

Además, copia la lista de filas usando `List.copyOf`, evitando modificaciones externas.

Sus métodos principales son:

```java id="d7-manifold-methods"
public int height()
public int width()
public Position startPosition()
public boolean hasSplitterAt(Position position)
public boolean contains(Position position)
```

`startPosition` localiza la posición inicial `S`.

`hasSplitterAt` comprueba si en una posición concreta hay un splitter.

`contains` comprueba si una posición está dentro de los límites del diagrama.

---

## `TachyonManifoldParser`

La clase `TachyonManifoldParser` se encarga de transformar las líneas del input en un objeto `TachyonManifold`.

Su método principal es:

```java id="d7-parser"
public TachyonManifold parse(List<String> lines)
```

El parser ignora las líneas en blanco, elimina espacios innecesarios con `trim` y construye una lista de filas.

Después crea un `TachyonManifold`, delegando en esta clase la validación del diagrama.

---

# Clases del paquete `day07.part1`

El paquete `day07.part1` contiene la solución específica de la primera parte.

---

## `Day07Part1Solver`

La clase `Day07Part1Solver` resuelve la primera parte del Día 7.

Implementa la interfaz común `PuzzleSolver`.

Su método `solve` realiza los siguientes pasos:

1. Usa `TachyonManifoldParser` para convertir el input en un `TachyonManifold`.
2. Usa `TachyonSplitCounter` para contar cuántas veces se divide el rayo.
3. Devuelve el total de divisiones.

Esta clase actúa como coordinadora de la parte 1.

---

## `TachyonSplitCounter`

La clase `TachyonSplitCounter` contiene la lógica específica para contar divisiones clásicas del rayo de taquiones.

Su método principal es:

```java id="d7-split-counter"
public long countSplitsIn(TachyonManifold manifold)
```

El algoritmo funciona así:

1. Localiza la posición inicial `S`.
2. Guarda la columna inicial como columna activa.
3. Recorre el diagrama fila a fila hacia abajo.
4. Para cada columna activa, comprueba la posición correspondiente.
5. Si la posición contiene un splitter, incrementa el contador de divisiones.
6. Desde ese splitter, genera dos nuevas columnas activas: izquierda y derecha.
7. Si no hay splitter, el rayo continúa en la misma columna.
8. Repite el proceso hasta salir del diagrama.

La clase usa un `Set<Integer>` para almacenar las columnas activas. Esto evita duplicar rayos cuando varios caminos llegan a la misma columna en la misma fila.

---

# Clases del paquete `day07.part2`

El paquete `day07.part2` contiene la solución específica de la segunda parte.

---

## `Day07Part2Solver`

La clase `Day07Part2Solver` resuelve la segunda parte del Día 7.

También implementa la interfaz `PuzzleSolver`.

Su método `solve` realiza los siguientes pasos:

1. Usa `TachyonManifoldParser` para convertir el input en un `TachyonManifold`.
2. Usa `QuantumTimelineCounter` para contar las líneas temporales finales.
3. Devuelve el total de timelines.

Esta clase actúa como coordinadora de la parte 2.

---

## `QuantumTimelineCounter`

La clase `QuantumTimelineCounter` contiene la lógica específica de la interpretación cuántica del colector.

Su método principal es:

```java id="d7-quantum-counter"
public long countTimelinesIn(TachyonManifold manifold)
```

A diferencia de la parte 1, aquí no basta con saber qué columnas están activas. Hay que saber cuántas líneas temporales llegan a cada columna.

Para ello, la clase usa un mapa:

```java id="d7-map"
Map<Integer, Long> activeTimelinesByColumn
```

La clave representa la columna y el valor representa cuántas timelines llegan a esa columna.

El algoritmo funciona así:

1. Localiza la posición inicial `S`.
2. Crea un mapa con una única timeline en la columna inicial.
3. Recorre el diagrama fila a fila.
4. Para cada columna activa, obtiene cuántas timelines llegan a ella.
5. Si hay un splitter, esas timelines se duplican hacia izquierda y derecha.
6. Si no hay splitter, las timelines continúan hacia abajo.
7. Cuando varias timelines llegan a la misma columna, se suman.
8. Al final, se suman todas las timelines activas restantes.

Esta clase permite resolver la parte 2 sin enumerar cada camino individualmente, agrupando las timelines por columna.

---

# Clase del paquete `day07`

El paquete `day07` contiene la clase principal del Día 7.

---

## `Day07Main`

La clase `Day07Main` es el punto de entrada para ejecutar la solución completa del Día 7.

El método `main` realiza los siguientes pasos:

1. Lee todas las líneas del archivo `src/main/resources/day07/input.txt`.
2. Crea una instancia de `Day07Part1Solver`.
3. Crea una instancia de `Day07Part2Solver`.
4. Ejecuta el método `solve` de ambos solvers.
5. Guarda los resultados.
6. Imprime los resultados por consola.

Esta clase utiliza la interfaz `PuzzleSolver` para referenciar ambos solvers:

```java id="d7-main-solvers"
PuzzleSolver part1Solver = new Day07Part1Solver();
PuzzleSolver part2Solver = new Day07Part2Solver();
```

---

# Interfaz común del proyecto

El proyecto utiliza la interfaz común `PuzzleSolver`, ubicada en el paquete `aoc2025.common`.

Esta interfaz define el contrato común para todos los solvers del Advent of Code:

```java id="d7-puzzle-solver"
long solve(List<String> lines);
```

En el Día 7, tanto `Day07Part1Solver` como `Day07Part2Solver` implementan esta interfaz.

---

# Fundamentos de diseño utilizados

En la solución del Día 7 se utilizan los siguientes fundamentos de diseño:

* Alta cohesión.
* Bajo acoplamiento.
* Modularidad.
* Código expresivo.
* Abstracción.
* Encapsulación.
* Diseño por contrato.
* Inmutabilidad.
* Eficiencia algorítmica.
* Agrupación de estados.

---

# Principios de diseño aplicados

En la solución del Día 7 se aplican los siguientes principios de diseño:

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

En la solución del Día 7 se utilizan los siguientes patrones de diseño:

* Iterator.
* Strategy.
* Command, aplicado parcialmente.

---

# Patrones no aplicados

En la solución del Día 7 no se aplican los siguientes patrones de diseño:

* Singleton.
* Factory Method.
* Adapter.
* Decorator.
* Observer.
* Template Method.

---

# Conclusión

La solución del Día 7 está organizada de forma clara y modular.

La primera parte cuenta cuántas veces se divide el rayo de taquiones usando un conjunto de columnas activas.

La segunda parte calcula cuántas líneas temporales quedan activas usando un mapa que agrupa el número de timelines por columna.

El diseño separa correctamente el modelo del colector, el parseo del input, la lógica clásica de división y la lógica cuántica de timelines. Esto permite que el código sea fácil de entender, probar, mantener y defender en una explicación oral.
