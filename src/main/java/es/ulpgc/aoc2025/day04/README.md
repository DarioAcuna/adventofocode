# Día 4: Printing Department

En el cuarto día del Advent of Code 2025, el problema se desarrolla en el departamento de impresión del Polo Norte. Para poder seguir avanzando por la base mientras los ascensores están fuera de servicio, los elfos necesitan atravesar una pared que conecta con la cafetería.

El problema consiste en analizar un diagrama formado por rollos de papel. Las carretillas elevadoras solo pueden acceder a ciertos rollos dependiendo de cuántos rollos tengan alrededor.

El diagrama está formado por una cuadrícula de caracteres:

* `@`: representa un rollo de papel.
* `.`: representa una posición vacía.

Ejemplo:

```text
..@@.@@@@.
@@@.@.@.@@
@@@@@.@.@@
@.@@@@..@.
@@.@@@@.@@
.@@@@@@@.@
.@.@.@.@@@
@.@@@.@@@@
.@@@@@@@@.
@.@.@@@.@.
```

Cada rollo puede tener hasta ocho posiciones adyacentes: arriba, abajo, izquierda, derecha y las cuatro diagonales.

---

## Parte 1

En la primera parte, una carretilla puede acceder a un rollo de papel si ese rollo tiene menos de cuatro rollos de papel en sus ocho posiciones adyacentes.

Por tanto, para cada posición del mapa hay que comprobar:

1. Si contiene un rollo de papel.
2. Cuántos rollos de papel tiene alrededor.
3. Si ese número es menor que `4`.

Solo se cuentan los rollos accesibles en el estado inicial del diagrama. En esta parte no se elimina ningún rollo.

En el ejemplo oficial hay `13` rollos accesibles.


---

## Parte 2

En la segunda parte, el proceso cambia. Ahora, cuando una carretilla puede acceder a un rollo de papel, ese rollo puede retirarse del mapa.

Al retirar un rollo, el número de vecinos de los rollos cercanos disminuye. Esto puede hacer que nuevos rollos pasen a ser accesibles. El proceso se repite hasta que ya no queda ningún rollo que cumpla la condición de accesibilidad.

La regla sigue siendo la misma:

```text
Un rollo es accesible si tiene menos de 4 rollos adyacentes.
```

La diferencia es que ahora el mapa cambia durante la ejecución.

El objetivo es contar cuántos rollos pueden eliminarse en total después de repetir el proceso tantas veces como sea posible.

En el ejemplo oficial se pueden retirar `43` rollos.


---

# Estructura del proyecto

La solución del Día 4 mantiene la misma estructura modular usada en los días anteriores:

```text
day04
├── Day04Main.java
├── common
│   ├── PaperRollDiagram.java
│   └── PaperRollDiagramParser.java
├── part1
│   └── Day04Part1Solver.java
└── part2
    ├── Day04Part2Solver.java
    └── RemovablePaperRollDiagram.java
```

La idea principal es separar:

* El punto de entrada del día.
* Las clases comunes del dominio.
* La solución específica de la parte 1.
* La solución específica de la parte 2.
* La simulación mutable necesaria para la retirada progresiva de rollos.

---

# Clases del paquete `day04.common`

El paquete `day04.common` contiene las clases compartidas por las dos partes del problema.

---

## `PaperRollDiagram`

El record `PaperRollDiagram` representa el diagrama inicial de rollos de papel.

Internamente almacena una lista de filas:

```java
public record PaperRollDiagram(List<String> rows)
```

Cada fila representa una línea del mapa y cada carácter indica si en esa posición hay un rollo de papel o una posición vacía.

Esta clase valida que el diagrama sea correcto:

* La lista de filas no puede ser `null`.
* La lista de filas no puede estar vacía.
* Ninguna fila puede ser `null`.
* Todas las filas deben tener la misma anchura.
* Solo se permiten los caracteres `.` y `@`.

Además, copia la lista de filas usando `List.copyOf`, evitando que el diagrama pueda modificarse desde fuera después de ser creado.

Sus métodos principales son:

```java
public int height()
```

Devuelve el número de filas del diagrama.

```java
public int width()
```

Devuelve el número de columnas del diagrama.

```java
public boolean hasPaperRollAt(int row, int column)
```

Indica si en una posición concreta hay un rollo de papel.

```java
public boolean contains(int row, int column)
```

Indica si una posición está dentro de los límites del diagrama.

Esta clase representa el estado inicial del problema de forma segura e inmutable.

---

## `PaperRollDiagramParser`

La clase `PaperRollDiagramParser` se encarga de transformar las líneas del input en un objeto `PaperRollDiagram`.

Su método principal es:

```java
public PaperRollDiagram parse(List<String> lines)
```

El parser recorre todas las líneas recibidas, ignora las líneas en blanco, elimina espacios innecesarios con `trim` y construye una lista de filas válidas.

Después, crea un objeto `PaperRollDiagram` con esas filas.

Su responsabilidad es únicamente interpretar el input. No calcula accesibilidad ni elimina rollos de papel.

Esto permite separar claramente el formato de entrada de la lógica del problema.

---

# Clases del paquete `day04.part1`

El paquete `day04.part1` contiene la solución específica de la primera parte.

---

## `Day04Part1Solver`

La clase `Day04Part1Solver` se encarga de resolver la primera parte del Día 4.

Implementa la interfaz común `PuzzleSolver`, por lo que define el método:

```java
public long solve(List<String> lines)
```

Su responsabilidad principal es contar cuántos rollos de papel son accesibles en el diagrama inicial.

Para ello, realiza los siguientes pasos:

1. Usa `PaperRollDiagramParser` para convertir el input en un `PaperRollDiagram`.
2. Recorre todas las posiciones del diagrama.
3. Comprueba si cada posición contiene un rollo de papel.
4. Cuenta cuántos rollos de papel hay en las ocho posiciones adyacentes.
5. Si el número de vecinos es menor que `4`, incrementa el contador.
6. Devuelve el total de rollos accesibles.

La constante:

```java
private static final int ACCESSIBLE_LIMIT = 4;
```

representa el límite de vecinos a partir del cual un rollo deja de ser accesible.

Los métodos privados ayudan a dividir la lógica:

* `isAccessiblePaperRoll`: comprueba si una posición contiene un rollo accesible.
* `adjacentPaperRollsOf`: cuenta los rollos adyacentes.
* `isPaperRollAt`: comprueba si una posición válida contiene un rollo.

Esta clase no modifica el diagrama. Solo analiza el estado inicial.

---

# Clases del paquete `day04.part2`

El paquete `day04.part2` contiene la solución específica de la segunda parte.

---

## `Day04Part2Solver`

La clase `Day04Part2Solver` se encarga de resolver la segunda parte del Día 4.

También implementa la interfaz `PuzzleSolver`.

Su método `solve` realiza los siguientes pasos:

1. Usa `PaperRollDiagramParser` para convertir el input en un `PaperRollDiagram`.
2. Crea un `RemovablePaperRollDiagram` a partir del diagrama inicial.
3. Llama a `removeAllAccessiblePaperRolls`.
4. Devuelve el número total de rollos retirados.

Esta clase actúa como coordinadora de la parte 2. No contiene directamente la simulación de retirada de rollos, sino que delega esa responsabilidad en `RemovablePaperRollDiagram`.

---

## `RemovablePaperRollDiagram`

La clase `RemovablePaperRollDiagram` representa una versión mutable del diagrama de rollos de papel.

A diferencia de `PaperRollDiagram`, que representa el estado inicial de forma inmutable, esta clase se usa para simular la retirada progresiva de rollos en la segunda parte.

Sus atributos principales son:

```java
private final boolean[][] paperRolls;
private final int[][] adjacentPaperRolls;
private final int height;
private final int width;
```

* `paperRolls` indica si en cada posición sigue existiendo un rollo de papel.
* `adjacentPaperRolls` guarda cuántos rollos vecinos tiene cada posición.
* `height` almacena la altura del diagrama.
* `width` almacena la anchura del diagrama.

El constructor recibe un `PaperRollDiagram`, copia su contenido y calcula inicialmente el número de vecinos de cada rollo.

Su método principal es:

```java
public long removeAllAccessiblePaperRolls()
```

Este método calcula cuántos rollos pueden retirarse en total.

El proceso funciona así:

1. Se buscan inicialmente todos los rollos accesibles.
2. Se guardan en una cola.
3. Mientras la cola no esté vacía, se extrae una posición.
4. Se comprueba que todavía haya un rollo en esa posición.
5. Se comprueba que el rollo siga siendo accesible.
6. Se elimina el rollo.
7. Se actualiza el número de vecinos de los rollos cercanos.
8. Se añaden a la cola los vecinos que ahora se hayan vuelto accesibles.
9. El proceso continúa hasta que no quedan posiciones accesibles.

El uso de una cola permite procesar los rollos accesibles de forma ordenada, evitando tener que recorrer todo el mapa desde cero después de cada eliminación.

La clase también define un record interno:

```java
private record Position(int row, int column) {
}
```

Este record representa una posición dentro del diagrama. Su uso mejora la claridad del código, porque permite trabajar con una posición como un único concepto en lugar de pasar siempre dos enteros separados.

También define una interfaz interna:

```java
private interface NeighbourAction {
    void apply(Position position);
}
```

Esta interfaz permite aplicar distintas acciones sobre los vecinos de una posición, reutilizando el método `forEachNeighbourOf`.

Por ejemplo, se usa para:

* Actualizar el número de vecinos cuando se elimina un rollo.
* Añadir a la cola los vecinos que se vuelven accesibles.

---

# Clase del paquete `day04`

El paquete `day04` contiene la clase principal del Día 4.

---

## `Day04Main`

La clase `Day04Main` es el punto de entrada para ejecutar la solución completa del Día 4.

Su responsabilidad principal no es resolver directamente el problema, sino preparar la ejecución.

El método `main` realiza los siguientes pasos:

1. Lee todas las líneas del archivo `src/main/resources/day04/input.txt`.
2. Crea una instancia de `Day04Part1Solver`.
3. Crea una instancia de `Day04Part2Solver`.
4. Ejecuta el método `solve` de ambos solvers.
5. Guarda los resultados.
6. Imprime los resultados por consola.

Esta clase utiliza la interfaz `PuzzleSolver` para referenciar ambos solvers:

```java
PuzzleSolver part1Solver = new Day04Part1Solver();
PuzzleSolver part2Solver = new Day04Part2Solver();
```

Gracias a esto, ambas partes se ejecutan de forma uniforme, aunque internamente tengan comportamientos distintos.

---

# Interfaz común del proyecto

Además de las clases específicas del Día 4, el proyecto utiliza la interfaz común `PuzzleSolver`, ubicada en el paquete `aoc2025.common`.

Esta interfaz define el contrato común para todos los solvers del Advent of Code:

```java
long solve(List<String> lines);
```

En el Día 4, tanto `Day04Part1Solver` como `Day04Part2Solver` implementan esta interfaz.

Esto permite mantener una estructura común para todos los días:

* Una clase principal que lee el input.
* Un solver para la parte 1.
* Un solver para la parte 2.
* Un método `solve` común para ejecutar cada solución.

---

# Fundamentos de diseño utilizados

En la solución del Día 4 se utilizan los siguientes fundamentos de diseño:

* Alta cohesión.
* Bajo acoplamiento.
* Modularidad.
* Código expresivo.
* Abstracción.

---

# Principios de diseño aplicados

En la solución del Día 4 se aplican los siguientes principios de diseño:

* Principio de Responsabilidad Única, SRP.
* Principio Abierto/Cerrado, OCP.
* Principio de Sustitución de Liskov, LSP.
* Principio de Segregación de Interfaces, ISP.
* Principio de Inversión de Dependencias, DIP.
* Principio DRY.
* Ley de Demeter.
* Principio YAGNI.

---

# Patrones de diseño aplicados

En la solución del Día 4 se utilizan los siguientes patrones de diseño:

* Iterator.
---

# Patrones no aplicados

En la solución del Día 4 no se aplican los siguientes patrones de diseño:

* Singleton.
* Factory Method.
* Adapter.
* Decorator.
* Observer.

---
