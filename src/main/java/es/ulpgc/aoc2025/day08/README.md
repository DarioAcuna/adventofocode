# Día 8: Playground

En el octavo día del Advent of Code 2025, el problema se desarrolla en un enorme parque subterráneo donde los elfos están preparando una decoración navideña con cajas de conexión eléctrica suspendidas en el espacio.

Cada caja de conexión tiene una posición en 3D, representada mediante coordenadas `X,Y,Z`.

Ejemplo:

```text
162,817,812
57,618,57
906,360,560
592,479,940
352,342,300
466,668,158
542,29,236
431,825,988
739,650,466
52,470,668
216,146,977
819,987,18
117,168,530
805,96,715
346,949,466
970,615,88
941,993,340
862,61,35
984,92,344
425,690,689
```

Cada línea representa una caja de conexión.

El objetivo es conectar pares de cajas usando tiras de luces. Cuando dos cajas se conectan, pasan a formar parte del mismo circuito. Si una caja ya estaba conectada a otras, todos esos elementos quedan dentro del mismo circuito.

La distancia entre cajas se calcula en el espacio 3D. Para comparar distancias no es necesario calcular la raíz cuadrada; basta con usar la distancia cuadrada.

---

## Parte 1

En la primera parte, los elfos quieren conectar los `1000` pares de cajas más cercanos.

Después de realizar esas conexiones, hay que observar el tamaño de todos los circuitos formados.

El resultado se obtiene multiplicando los tamaños de los tres circuitos más grandes.

En el ejemplo oficial, después de hacer las `10` conexiones más cortas, los tres circuitos más grandes tienen tamaños:

```text
5, 4, 2
```

El producto es:

```text
5 * 4 * 2 = 40
```

Con el input real, el resultado obtenido para la parte 1 fue:

```text
83520
```

---

## Parte 2

En la segunda parte, los elfos descubren que no tienen suficientes cables para mantener tantos circuitos separados. Por tanto, necesitan seguir conectando cajas hasta que todas formen un único circuito.

El proceso sigue conectando los pares de cajas más cercanos que todavía no hayan sido considerados.

Cuando una conexión une dos circuitos distintos, esos circuitos se fusionan. Si una conexión une dos cajas que ya pertenecen al mismo circuito, no cambia nada.

El objetivo es detectar cuál es la conexión que por fin hace que todas las cajas formen un único circuito.

Una vez encontrada esa conexión final, se multiplican las coordenadas `X` de las dos cajas conectadas.

Con el input real, el resultado obtenido para la parte 2 fue:

```text
1131823407
```

---

# Estructura del proyecto

La solución del Día 8 mantiene la misma estructura modular usada en los días anteriores:

```text
day08
├── Day08Main.java
├── common
│   ├── CircuitDisjointSet.java
│   ├── JunctionBoxConnection.java
│   ├── JunctionBoxConnectionGenerator.java
│   ├── JunctionBoxMap.java
│   ├── JunctionBoxMapParser.java
│   └── JunctionBoxPosition.java
├── part1
│   ├── Day08Part1Solver.java
│   └── ShortestConnectionCircuitAnalyzer.java
└── part2
    ├── Day08Part2Solver.java
    └── FinalCircuitConnectionAnalyzer.java
```

La idea principal es separar:

* El punto de entrada del día.
* Las clases comunes del dominio.
* La representación de posiciones y conexiones.
* La generación de conexiones ordenadas por distancia.
* La estructura de circuitos conectados.
* La lógica específica de la parte 1.
* La lógica específica de la parte 2.

---

# Clases del paquete `day08.common`

El paquete `day08.common` contiene las clases compartidas por ambas partes.

---

## `JunctionBoxPosition`

El record `JunctionBoxPosition` representa la posición de una caja de conexión en el espacio 3D.

Está formado por tres coordenadas:

```java
public record JunctionBoxPosition(int x, int y, int z)
```

Su método principal es:

```java
public long squaredDistanceTo(JunctionBoxPosition other)
```

Este método calcula la distancia cuadrada entre dos posiciones.

Se usa distancia cuadrada porque para comparar qué conexión es más corta no hace falta calcular la raíz cuadrada. Esto evita operaciones innecesarias y mantiene el orden correcto entre distancias.

---

## `JunctionBoxMap`

El record `JunctionBoxMap` representa el conjunto completo de cajas de conexión.

Internamente almacena una lista de posiciones:

```java
public record JunctionBoxMap(List<JunctionBoxPosition> positions)
```

Esta clase valida que:

* La lista de posiciones no sea `null`.
* La lista no esté vacía.

Además, copia la lista mediante `List.copyOf`, evitando modificaciones externas.

Sus métodos principales son:

```java
public int size()
```

Devuelve el número de cajas.

```java
public JunctionBoxPosition positionAt(int index)
```

Devuelve la posición de una caja a partir de su índice.

---

## `JunctionBoxMapParser`

La clase `JunctionBoxMapParser` se encarga de transformar las líneas del input en un `JunctionBoxMap`.

Cada línea tiene el formato:

```text
X,Y,Z
```

El parser separa cada línea por comas, convierte las coordenadas a enteros y crea objetos `JunctionBoxPosition`.

Su responsabilidad es únicamente interpretar el formato de entrada.

---

## `JunctionBoxConnection`

El record `JunctionBoxConnection` representa una posible conexión entre dos cajas.

Está formado por:

```java
public record JunctionBoxConnection(
        int firstIndex,
        int secondIndex,
        long squaredDistance
)
```

* `firstIndex`: índice de la primera caja.
* `secondIndex`: índice de la segunda caja.
* `squaredDistance`: distancia cuadrada entre ambas cajas.

Implementa `Comparable<JunctionBoxConnection>`, lo que permite ordenar conexiones automáticamente.

El orden se basa en:

1. Distancia cuadrada.
2. Índice de la primera caja.
3. Índice de la segunda caja.

Esto hace que el orden sea estable y predecible.

---

## `JunctionBoxConnectionGenerator`

La clase `JunctionBoxConnectionGenerator` genera todas las posibles conexiones entre pares de cajas.

Su método principal es:

```java
public List<JunctionBoxConnection> sortedConnectionsOf(JunctionBoxMap map)
```

El algoritmo recorre todos los pares posibles de cajas, calcula la distancia cuadrada entre ellas y crea una conexión.

Después ordena todas las conexiones de menor a mayor distancia.

Esta clase permite que las dos partes del problema trabajen con la misma lista ordenada de conexiones.

---

## `CircuitDisjointSet`

La clase `CircuitDisjointSet` representa los circuitos conectados mediante una estructura Union-Find o Disjoint Set.

Cada caja empieza perteneciendo a su propio circuito. Cuando se conecta con otra caja de un circuito distinto, ambos circuitos se fusionan.

Sus atributos principales son:

```java
private final int[] parent;
private final int[] size;
private int componentCount;
```

* `parent`: indica el representante de cada componente.
* `size`: guarda el tamaño de cada circuito.
* `componentCount`: indica cuántos circuitos separados existen actualmente.

Sus métodos principales son:

```java
public boolean union(int first, int second)
```

Fusiona los circuitos de dos cajas si pertenecen a componentes distintas.

```java
public boolean isSingleComponent()
```

Indica si todas las cajas forman ya un único circuito.

```java
public List<Integer> componentSizes()
```

Devuelve los tamaños de los circuitos actuales.

Esta clase es clave para resolver el problema de forma eficiente.

---

# Clases del paquete `day08.part1`

El paquete `day08.part1` contiene la solución específica de la primera parte.

---

## `Day08Part1Solver`

La clase `Day08Part1Solver` resuelve la primera parte del Día 8.

Implementa la interfaz común `PuzzleSolver`.

Por defecto, usa la constante:

```java
private static final int CONNECTIONS_TO_MAKE = 1000;
```

También tiene un constructor alternativo que permite indicar otro número de conexiones. Esto es útil para probar el ejemplo oficial, donde se usan `10` conexiones.

Su método `solve` realiza estos pasos:

1. Usa `JunctionBoxMapParser` para convertir el input en un `JunctionBoxMap`.
2. Usa `ShortestConnectionCircuitAnalyzer`.
3. Calcula el producto de los tres circuitos más grandes después de hacer las conexiones indicadas.
4. Devuelve el resultado.

---

## `ShortestConnectionCircuitAnalyzer`

La clase `ShortestConnectionCircuitAnalyzer` contiene la lógica principal de la parte 1.

Su método principal es:

```java
public long productOfThreeLargestCircuitSizesAfter(
        JunctionBoxMap map,
        int connectionsToMake
)
```

El algoritmo funciona así:

1. Genera todas las conexiones posibles ordenadas por distancia.
2. Crea un `CircuitDisjointSet` con una componente por caja.
3. Aplica las primeras `connectionsToMake` conexiones.
4. Obtiene los tamaños de los circuitos resultantes.
5. Ordena esos tamaños de mayor a menor.
6. Toma los tres mayores.
7. Devuelve el producto.

---

# Clases del paquete `day08.part2`

El paquete `day08.part2` contiene la solución específica de la segunda parte.

---

## `Day08Part2Solver`

La clase `Day08Part2Solver` resuelve la segunda parte del Día 8.

También implementa la interfaz `PuzzleSolver`.

Su método `solve` realiza estos pasos:

1. Usa `JunctionBoxMapParser` para convertir el input en un `JunctionBoxMap`.
2. Usa `FinalCircuitConnectionAnalyzer`.
3. Calcula el producto de las coordenadas `X` de la conexión que une todas las cajas en un único circuito.
4. Devuelve el resultado.

---

## `FinalCircuitConnectionAnalyzer`

La clase `FinalCircuitConnectionAnalyzer` contiene la lógica específica de la parte 2.

Su método principal es:

```java
public long productOfXCoordinatesOfFinalConnection(JunctionBoxMap map)
```

El algoritmo funciona así:

1. Genera todas las conexiones posibles ordenadas por distancia.
2. Crea un `CircuitDisjointSet`.
3. Recorre las conexiones de menor a mayor distancia.
4. Intenta fusionar los circuitos de cada conexión.
5. Si la conexión une dos circuitos distintos, comprueba si ya solo queda un circuito.
6. Cuando todas las cajas quedan conectadas, obtiene las dos posiciones de la conexión final.
7. Multiplica sus coordenadas `X`.
8. Devuelve el resultado.

Si no se logra formar un único circuito, lanza una excepción.

---

# Clase del paquete `day08`

El paquete `day08` contiene la clase principal del Día 8.

---

## `Day08Main`

La clase `Day08Main` es el punto de entrada para ejecutar la solución completa del Día 8.

El método `main` realiza los siguientes pasos:

1. Lee todas las líneas del archivo `src/main/resources/day08/input.txt`.
2. Crea una instancia de `Day08Part1Solver`.
3. Crea una instancia de `Day08Part2Solver`.
4. Ejecuta el método `solve` de ambos solvers.
5. Guarda los resultados.
6. Imprime los resultados por consola.

Esta clase utiliza la interfaz `PuzzleSolver` para referenciar ambos solvers:

```java
PuzzleSolver part1Solver = new Day08Part1Solver();
PuzzleSolver part2Solver = new Day08Part2Solver();
```

---

# Interfaz común del proyecto

El proyecto utiliza la interfaz común `PuzzleSolver`, ubicada en el paquete `aoc2025.common`.

Esta interfaz define el contrato común para todos los solvers del Advent of Code:

```java
long solve(List<String> lines);
```

En el Día 8, tanto `Day08Part1Solver` como `Day08Part2Solver` implementan esta interfaz.

---

# Fundamentos de diseño utilizados

En la solución del Día 8 se utilizan los siguientes fundamentos de diseño:

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
* Ordenación de conexiones.

---

# Principios de diseño aplicados

En la solución del Día 8 se aplican los siguientes principios de diseño:

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

En la solución del Día 8 se utilizan los siguientes patrones de diseño:

* Iterator.
* Strategy.
* Command, aplicado parcialmente.

---

# Patrones no aplicados

En la solución del Día 8 no se aplican los siguientes patrones de diseño:

* Singleton.
* Factory Method.
* Adapter.
* Decorator.
* Observer.
* Template Method.

---

# Conclusión

La solución del Día 8 está organizada de forma clara y modular.

La primera parte conecta las `1000` conexiones más cortas y calcula el producto de los tres circuitos más grandes.

La segunda parte continúa conectando cajas hasta que todas forman un único circuito y devuelve el producto de las coordenadas `X` de la última conexión necesaria.

El diseño separa correctamente el parseo, el modelo de posiciones, la generación de conexiones, la estructura de circuitos y la lógica específica de cada parte. Esto permite que el código sea fácil de entender, probar, mantener y defender en una explicación oral.
