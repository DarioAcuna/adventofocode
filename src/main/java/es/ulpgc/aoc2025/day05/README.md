# Día 5: Cafeteria

En el quinto día del Advent of Code 2025, el problema se desarrolla en la cafetería del Polo Norte. Tras atravesar la pared del departamento de impresión, los elfos descubren que en la cocina tienen un problema con su nuevo sistema de inventario.

El sistema trabaja con IDs de ingredientes. Algunos IDs se consideran frescos porque pertenecen a ciertos rangos, mientras que otros están disponibles en el inventario y hay que comprobar si son frescos o no.

El archivo de entrada tiene dos secciones:

1. Una lista de rangos de IDs frescos.
2. Una línea en blanco.
3. Una lista de IDs de ingredientes disponibles.

Ejemplo:

```text id="k2zwfn"
3-5
10-14
16-20
12-18

1
5
8
11
17
32
```

Los rangos son inclusivos. Por ejemplo, el rango `3-5` incluye los IDs `3`, `4` y `5`.

Los rangos también pueden solaparse. Un ingrediente se considera fresco si pertenece a cualquiera de los rangos.

---

## Parte 1

En la primera parte, el objetivo es contar cuántos de los IDs disponibles son frescos.

Para ello, hay que comprobar cada ID de la segunda sección del input y verificar si pertenece a alguno de los rangos frescos de la primera sección.

En el ejemplo:

* `1` no pertenece a ningún rango, por tanto está estropeado.
* `5` pertenece al rango `3-5`, por tanto está fresco.
* `8` no pertenece a ningún rango, por tanto está estropeado.
* `11` pertenece al rango `10-14`, por tanto está fresco.
* `17` pertenece a los rangos `16-20` y `12-18`, por tanto está fresco.
* `32` no pertenece a ningún rango, por tanto está estropeado.

En total, hay `3` IDs disponibles frescos.

Con el input real, el resultado obtenido para la parte 1 fue:

```text id="r2epdi"
563
```

---

## Parte 2

En la segunda parte, la lista de IDs disponibles deja de ser relevante.

Ahora el objetivo es contar cuántos IDs distintos son considerados frescos por la unión de todos los rangos frescos.

Por ejemplo, con estos rangos:

```text id="awipnj"
3-5
10-14
16-20
12-18
```

Los IDs considerados frescos son:

```text id="zkyjw5"
3, 4, 5, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20
```

Aunque los rangos `10-14`, `16-20` y `12-18` se solapan, los IDs repetidos solo deben contarse una vez.

En el ejemplo hay `14` IDs frescos distintos.

Con el input real, el resultado obtenido para la parte 2 fue:

```text id="ianog7"
338693411431456
```

---

# Estructura del proyecto

La solución del Día 5 mantiene la misma estructura modular usada en los días anteriores:

```text id="clv7rh"
day05
├── Day05Main.java
├── common
│   ├── FreshIngredientRanges.java
│   ├── IngredientIdRange.java
│   ├── InventoryDatabase.java
│   └── InventoryDatabaseParser.java
├── part1
│   └── Day05Part1Solver.java
└── part2
    └── Day05Part2Solver.java
```

La idea principal es separar:

* El punto de entrada del día.
* Las clases comunes del dominio.
* El parseo de la base de datos.
* La solución específica de la parte 1.
* La solución específica de la parte 2.
* La lógica de unión y consulta de rangos frescos.

---

# Clases del paquete `day05.common`

El paquete `day05.common` contiene las clases compartidas por las dos partes del problema.

---

## `IngredientIdRange`

El record `IngredientIdRange` representa un rango inclusivo de IDs de ingredientes.

Está formado por dos valores:

```java id="ao0oar"
public record IngredientIdRange(long firstId, long lastId)
```

* `firstId`: primer ID incluido en el rango.
* `lastId`: último ID incluido en el rango.

Por ejemplo, el rango textual `10-14` se representa como:

```text id="mpf1sl"
IngredientIdRange(10, 14)
```

Esta clase valida que el rango sea correcto:

* El primer ID no puede ser negativo.
* El último ID no puede ser negativo.
* El primer ID no puede ser mayor que el último.

Sus métodos principales son:

```java id="4iu0xs"
public boolean contains(long ingredientId)
```

Comprueba si un ID pertenece al rango.

```java id="n7mbf6"
public boolean overlapsOrTouches(IngredientIdRange other)
```

Comprueba si dos rangos se solapan o son consecutivos.

```java id="k5s325"
public IngredientIdRange mergeWith(IngredientIdRange other)
```

Fusiona dos rangos que se solapan o se tocan.

Esta clase es fundamental porque permite trabajar con rangos como objetos del dominio en lugar de manejar pares de números sueltos.

---

## `FreshIngredientRanges`

El record `FreshIngredientRanges` representa el conjunto de rangos que consideran frescos a los ingredientes.

Su responsabilidad principal es recibir una lista de rangos y fusionar aquellos que se solapan o se tocan. De esta forma, la clase trabaja internamente con una lista de rangos simplificada y ordenada.

Por ejemplo, estos rangos:

```text id="i0u8kc"
10-14
12-18
16-20
```

pueden fusionarse en:

```text id="xmef55"
10-20
```

porque se solapan entre sí.

Sus métodos principales son:

```java id="kp7fcl"
public boolean contains(long ingredientId)
```

Comprueba si un ID pertenece a alguno de los rangos frescos.

Para hacerlo de forma eficiente, usa búsqueda binaria sobre los rangos fusionados.

```java id="knuo2n"
public long totalFreshIngredientIds()
```

Calcula cuántos IDs distintos son considerados frescos por la unión de todos los rangos.

Esto se consigue sumando el tamaño de cada rango fusionado:

```text id="bf6sk0"
lastId - firstId + 1
```

Esta clase es especialmente importante en la parte 2, porque permite contar una cantidad enorme de IDs sin tener que generarlos uno por uno.

---

## `InventoryDatabase`

El record `InventoryDatabase` representa la base de datos completa del inventario.

Está formado por dos partes:

```java id="uz487v"
public record InventoryDatabase(
        List<IngredientIdRange> freshRanges,
        List<Long> availableIngredientIds
)
```

* `freshRanges`: lista de rangos de IDs frescos.
* `availableIngredientIds`: lista de IDs disponibles en el inventario.

Esta clase copia ambas listas con `List.copyOf`, evitando que puedan modificarse desde fuera después de crear la base de datos.

Su propósito es agrupar en un único objeto las dos secciones del input.

---

## `InventoryDatabaseParser`

La clase `InventoryDatabaseParser` se encarga de transformar las líneas del archivo de entrada en un objeto `InventoryDatabase`.

El input tiene dos secciones separadas por una línea en blanco:

```text id="nlbcme"
3-5
10-14

1
5
8
```

La primera sección contiene rangos frescos.

La segunda sección contiene IDs disponibles.

El parser utiliza una variable booleana para saber si todavía está leyendo rangos o si ya ha pasado a leer IDs disponibles.

Sus responsabilidades principales son:

* Recorrer las líneas del input.
* Detectar la línea en blanco que separa ambas secciones.
* Parsear los rangos de la primera sección.
* Parsear los IDs disponibles de la segunda sección.
* Crear un objeto `InventoryDatabase`.

También incluye un método privado `parseRange`, encargado de convertir una línea como `3-5` en un objeto `IngredientIdRange`.

Esta clase separa el formato del input de la lógica de resolución del problema.

---

# Clases del paquete `day05.part1`

El paquete `day05.part1` contiene la solución específica de la primera parte.

---

## `Day05Part1Solver`

La clase `Day05Part1Solver` se encarga de resolver la primera parte del Día 5.

Implementa la interfaz común `PuzzleSolver`, por lo que define el método:

```java id="cvc85i"
public long solve(List<String> lines)
```

Su responsabilidad principal es contar cuántos IDs disponibles son frescos.

Para ello, realiza los siguientes pasos:

1. Usa `InventoryDatabaseParser` para convertir el input en un `InventoryDatabase`.
2. Crea un objeto `FreshIngredientRanges` a partir de los rangos frescos.
3. Recorre todos los IDs disponibles.
4. Comprueba si cada ID está contenido en los rangos frescos.
5. Incrementa un contador por cada ID fresco.
6. Devuelve el total.

Esta clase no decide cómo se fusionan los rangos ni cómo se realiza la búsqueda. Esa lógica se delega en `FreshIngredientRanges`.

---

# Clases del paquete `day05.part2`

El paquete `day05.part2` contiene la solución específica de la segunda parte.

---

## `Day05Part2Solver`

La clase `Day05Part2Solver` se encarga de resolver la segunda parte del Día 5.

También implementa la interfaz `PuzzleSolver`.

Su responsabilidad principal es calcular cuántos IDs distintos son considerados frescos por los rangos de la primera sección del input.

Para ello, realiza los siguientes pasos:

1. Usa `InventoryDatabaseParser` para convertir el input en un `InventoryDatabase`.
2. Crea un objeto `FreshIngredientRanges` a partir de los rangos frescos.
3. Llama al método `totalFreshIngredientIds`.
4. Devuelve el resultado.

A diferencia de la parte 1, esta clase no utiliza los IDs disponibles de la segunda sección, porque en la parte 2 solo importan los rangos frescos.

La lógica de fusionar rangos y contar IDs únicos está encapsulada en `FreshIngredientRanges`.

---

# Clase del paquete `day05`

El paquete `day05` contiene la clase principal del Día 5.

---

## `Day05Main`

La clase `Day05Main` es el punto de entrada para ejecutar la solución completa del Día 5.

Su responsabilidad principal no es resolver directamente el problema, sino preparar la ejecución.

El método `main` realiza los siguientes pasos:

1. Lee todas las líneas del archivo `src/main/resources/day05/input.txt`.
2. Crea una instancia de `Day05Part1Solver`.
3. Crea una instancia de `Day05Part2Solver`.
4. Ejecuta el método `solve` de ambos solvers.
5. Guarda los resultados.
6. Imprime los resultados por consola.

Esta clase utiliza la interfaz `PuzzleSolver` para referenciar ambos solvers:

```java id="8rvivi"
PuzzleSolver part1Solver = new Day05Part1Solver();
PuzzleSolver part2Solver = new Day05Part2Solver();
```

Gracias a esto, ambas partes se ejecutan de forma uniforme, aunque internamente tengan comportamientos distintos.

---

# Interfaz común del proyecto

Además de las clases específicas del Día 5, el proyecto utiliza la interfaz común `PuzzleSolver`, ubicada en el paquete `aoc2025.common`.

Esta interfaz define el contrato común para todos los solvers del Advent of Code:

```java id="2g9ryu"
long solve(List<String> lines);
```

En el Día 5, tanto `Day05Part1Solver` como `Day05Part2Solver` implementan esta interfaz.

Esto permite mantener una estructura común para todos los días:

* Una clase principal que lee el input.
* Un solver para la parte 1.
* Un solver para la parte 2.
* Un método `solve` común para ejecutar cada solución.

---

# Fundamentos de diseño utilizados

En la solución del Día 5 se utilizan los siguientes fundamentos de diseño:

* Alta cohesión.
* Bajo acoplamiento.
* Modularidad.
* Código expresivo.
* Abstracción.

---

# Principios de diseño aplicados

En la solución del Día 5 se aplican los siguientes principios de diseño:

* Principio de Responsabilidad Única, SRP.
* Principio Abierto/Cerrado, OCP.
* Principio de Sustitución de Liskov, LSP.
* Principio de Segregación de Interfaces, ISP.
* Principio de Inversión de Dependencias, DIP.
* Composición sobre herencia.
* Principio DRY.
* Ley de Demeter.
* Principio YAGNI.

---

# Patrones de diseño aplicados

En la solución del Día 5 se utilizan los siguientes patrones de diseño:

* Iterator.
  
---

# Patrones no aplicados

En la solución del Día 5 no se aplican los siguientes patrones de diseño:

* Singleton.
* Factory Method.
* Adapter.
* Decorator.
* Observer.

---
