# Día 2: Gift Shop

En el segundo día del Advent of Code 2025, el problema se desarrolla en la tienda de regalos del Polo Norte. Al llegar allí, uno de los elfos pide ayuda para revisar una base de datos de productos, ya que se han introducido muchos IDs de producto inválidos.

El archivo de entrada contiene varios rangos de IDs. Cada rango indica el primer y el último ID que deben revisarse. Los rangos aparecen separados por comas y cada uno tiene el siguiente formato:

```text
11-22,95-115,998-1012
```

Cada rango contiene dos números separados por un guion:

* El primer número representa el ID inicial del rango.
* El segundo número representa el ID final del rango.

El objetivo del problema es encontrar todos los IDs inválidos dentro de esos rangos y sumar sus valores.

---

## Parte 1

En la primera parte, un ID se considera inválido si está formado por una secuencia de dígitos repetida exactamente dos veces.

Por ejemplo:

* `55` es inválido porque está formado por `5` repetido dos veces.
* `6464` es inválido porque está formado por `64` repetido dos veces.
* `123123` es inválido porque está formado por `123` repetido dos veces.

En cambio, un número como `101` no es inválido, ya que no está formado por dos mitades iguales.

La tarea consiste en revisar todos los rangos del input, encontrar los IDs que cumplen este patrón y sumar todos los IDs inválidos encontrados.

---

## Parte 2

En la segunda parte, la regla cambia. Ahora un ID se considera inválido si está formado por una secuencia de dígitos repetida al menos dos veces.

Esto amplía los casos válidos respecto a la primera parte.

Por ejemplo:

* `12341234` es inválido porque `1234` se repite dos veces.
* `123123123` es inválido porque `123` se repite tres veces.
* `1212121212` es inválido porque `12` se repite cinco veces.
* `1111111` es inválido porque `1` se repite siete veces.

Por tanto, en esta parte no basta con buscar números formados por dos mitades iguales. También hay que detectar números formados por un bloque más pequeño repetido varias veces.

La tarea consiste en encontrar todos los IDs inválidos según esta nueva regla y sumar sus valores.

---

# Clases del paquete `day02.common`

El paquete `day02.common` contiene las clases e interfaces comunes utilizadas por las dos partes del Día 2. Su objetivo es representar los rangos de IDs, interpretar el input y definir una abstracción común para las distintas formas de generar IDs inválidos.

---

## `IdRange`

El record `IdRange` representa un rango de IDs de producto.

Está formado por dos valores:

* `firstId`: primer ID incluido en el rango.
* `lastId`: último ID incluido en el rango.

Esta clase se encarga de modelar cada intervalo del input. Por ejemplo, el rango textual `95-115` se puede representar mediante un objeto `IdRange` cuyo `firstId` es `95` y cuyo `lastId` es `115`.

Además, valida que los datos del rango sean correctos:

* El primer ID no puede ser negativo.
* El último ID no puede ser negativo.
* El primer ID no puede ser mayor que el último.

También incluye el método `contains`, que permite comprobar si un ID concreto pertenece al rango.

```java
public boolean contains(long id) {
    return firstId <= id && id <= lastId;
}
```

Al estar definido como `record`, `IdRange` es inmutable. Una vez creado un rango, sus valores no cambian.

---

## `IdRangeParser`

La clase `IdRangeParser` se encarga de transformar el contenido del archivo de entrada en una lista de objetos `IdRange`.

El input del problema puede aparecer como una línea larga con rangos separados por comas. Esta clase une las líneas recibidas, separa los rangos por comas y convierte cada rango textual en un objeto del dominio.

Por ejemplo, a partir de este texto:

```text
11-22,95-115,998-1012
```

genera una lista con tres rangos:

* `IdRange(11, 22)`
* `IdRange(95, 115)`
* `IdRange(998, 1012)`

Sus responsabilidades principales son:

* Unir las líneas del input.
* Eliminar espacios innecesarios.
* Separar los rangos usando la coma.
* Ignorar fragmentos vacíos.
* Separar cada rango usando el guion.
* Convertir los límites a valores `long`.
* Crear objetos `IdRange`.

Esta clase separa la lógica de lectura e interpretación del input de la lógica de generación de IDs inválidos.

---

## `InvalidProductIdGenerator`

La interfaz `InvalidProductIdGenerator` define el contrato que deben cumplir las clases capaces de generar IDs de producto inválidos.

Contiene un único método:

```java
Set<Long> generate(List<IdRange> ranges);
```

Este método recibe una lista de rangos y devuelve un conjunto de IDs inválidos encontrados dentro de esos rangos.

El uso de `Set<Long>` evita duplicados. Esto es importante porque un mismo ID podría coincidir con más de una condición o aparecer relacionado con más de un rango, y solo debe sumarse una vez.

Esta interfaz permite que cada parte del problema tenga su propia estrategia de generación de IDs inválidos:

* En la parte 1, se generan IDs formados por un bloque repetido exactamente dos veces.
* En la parte 2, se generan IDs formados por un bloque repetido al menos dos veces.

---

# Clases del paquete `day02.part1`

El paquete `day02.part1` contiene las clases específicas para resolver la primera parte del problema.

---

## `Day02Part1Solver`

La clase `Day02Part1Solver` se encarga de resolver la primera parte del Día 2.

Implementa la interfaz `PuzzleSolver`, por lo que debe definir el método:

```java
long solve(List<String> lines);
```

Su responsabilidad principal es coordinar el proceso completo de resolución de la parte 1.

Para ello, realiza los siguientes pasos:

1. Usa `IdRangeParser` para convertir el input en una lista de rangos.
2. Usa `TwiceRepeatedProductIdGenerator` para generar los IDs inválidos.
3. Convierte el conjunto de IDs inválidos en un stream.
4. Suma todos los valores.
5. Devuelve el resultado final.

La clase no contiene directamente la lógica para construir los IDs inválidos. Esa responsabilidad se delega en `TwiceRepeatedProductIdGenerator`.

---

## `TwiceRepeatedProductIdGenerator`

La clase `TwiceRepeatedProductIdGenerator` genera los IDs inválidos de la primera parte.

Un ID es inválido en esta parte si está formado por un bloque de dígitos repetido exactamente dos veces.

Por ejemplo:

* `11`
* `99`
* `1010`
* `6464`
* `123123`

Esta clase genera posibles bloques numéricos y construye IDs duplicando esos bloques. Después comprueba si el ID generado está dentro de alguno de los rangos del input.

Sus responsabilidades principales son:

* Calcular el ID máximo de todos los rangos.
* Calcular cuántos dígitos tiene ese ID máximo.
* Generar bloques de distintas longitudes.
* Construir IDs repitiendo cada bloque dos veces.
* Comprobar si el ID generado está dentro de algún rango.
* Guardar los IDs inválidos en un `Set<Long>` para evitar duplicados.

Por ejemplo, si el bloque es `123`, el ID generado será:

```text
123123
```

Esta clase contiene la lógica específica de la regla de la parte 1.

---

# Clases del paquete `day02.part2`

El paquete `day02.part2` contiene las clases específicas para resolver la segunda parte del problema.

---

## `Day02Part2Solver`

La clase `Day02Part2Solver` se encarga de resolver la segunda parte del Día 2.

Al igual que la clase de la parte 1, implementa la interfaz `PuzzleSolver`.

Su responsabilidad principal es coordinar la resolución de la parte 2.

Para ello, realiza los siguientes pasos:

1. Usa `IdRangeParser` para convertir el input en una lista de rangos.
2. Usa `MultipleRepeatedProductIdGenerator` para generar los IDs inválidos.
3. Convierte el conjunto de IDs inválidos en un stream.
4. Suma todos los valores.
5. Devuelve el resultado final.

La diferencia principal con `Day02Part1Solver` es el generador que utiliza. En lugar de usar `TwiceRepeatedProductIdGenerator`, utiliza `MultipleRepeatedProductIdGenerator`.

---

## `MultipleRepeatedProductIdGenerator`

La clase `MultipleRepeatedProductIdGenerator` genera los IDs inválidos de la segunda parte.

En esta parte, un ID es inválido si está formado por un bloque de dígitos repetido al menos dos veces.

Por ejemplo:

* `11`
* `999`
* `1010`
* `123123123`
* `1212121212`
* `1111111`

Esta clase amplía la lógica de la primera parte, ya que no se limita a repetir un bloque dos veces. En su lugar, prueba distintas longitudes totales, distintas longitudes de bloque y distintas cantidades de repeticiones.

Sus responsabilidades principales son:

* Calcular el ID máximo de todos los rangos.
* Calcular el número máximo de dígitos necesario.
* Probar diferentes longitudes totales de ID.
* Probar diferentes longitudes de bloque.
* Comprobar que la longitud total sea divisible entre la longitud del bloque.
* Calcular cuántas veces debe repetirse el bloque.
* Construir el ID repitiendo el bloque.
* Comprobar si el ID generado está dentro de algún rango.
* Guardar los IDs inválidos en un `Set<Long>` para evitar duplicados.

Por ejemplo, si el bloque es `12` y se repite cinco veces, el ID generado será:

```text
1212121212
```

Esta clase contiene la lógica específica de la regla de la parte 2.

---

# Clase del paquete `day02`

El paquete `day02` contiene la clase principal del Día 2.

---

## `Day02Main`

La clase `Day02Main` es el punto de entrada para ejecutar la solución completa del Día 2.

Su responsabilidad principal no es resolver directamente el problema, sino preparar la ejecución de ambas partes.

El método `main` realiza los siguientes pasos:

1. Lee todas las líneas del archivo `src/main/resources/day02/input.txt`.
2. Crea una instancia de `Day02Part1Solver`.
3. Crea una instancia de `Day02Part2Solver`.
4. Ejecuta el método `solve` de cada solver.
5. Guarda los resultados de ambas partes.
6. Imprime los resultados por consola.

Esta clase utiliza la interfaz `PuzzleSolver` para referenciar ambos solvers:

```java
PuzzleSolver part1Solver = new Day02Part1Solver();
PuzzleSolver part2Solver = new Day02Part2Solver();
```

Gracias a esto, ambas partes pueden ejecutarse de forma uniforme, aunque internamente usen reglas distintas para encontrar IDs inválidos.

---

# Interfaz común del proyecto

Además de las clases específicas del Día 2, el proyecto utiliza la interfaz común `PuzzleSolver`, ubicada en el paquete `aoc2025.common`.

Esta interfaz define el contrato común para todos los solvers del Advent of Code:

```java
long solve(List<String> lines);
```

En el Día 2, tanto `Day02Part1Solver` como `Day02Part2Solver` implementan esta interfaz.

Esto permite que las soluciones de todos los días mantengan una estructura común:

* Una clase principal que lee el input.
* Un solver para la parte 1.
* Un solver para la parte 2.
* Un método `solve` común para ejecutar cada solución.

---

# Fundamentos de diseño utilizados

En la solución del Día 2 se utilizan los siguientes fundamentos de diseño:

* Alta cohesión.
* Bajo acoplamiento.
* Modularidad.
* Código expresivo.
* Abstracción.
* Encapsulación.
* Diseño por contrato.
* Inmutabilidad.

---

# Principios de diseño aplicados

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

* Iterator.
* Strategy.

---
## Patrones no aplicados

Aunque algunos patrones aparecen en los materiales teóricos, no todos son necesarios en esta solución.

No se aplica `Singleton`, porque no hay ninguna clase que necesite garantizar una única instancia global.

No se aplica `Factory Method`, porque la creación de objetos es simple y directa. Los solvers crean directamente sus dependencias mediante constructores.

No se aplica `Adapter`, porque no se está adaptando una librería externa ni una interfaz incompatible con el diseño del proyecto.

No se aplica `Decorator`, porque no se añaden responsabilidades dinámicamente a objetos existentes. Las clases tienen responsabilidades fijas y bien definidas.

No se aplica `Observer`, porque no hay objetos observadores suscritos a cambios de estado de otros objetos.

No se aplica `Template Method`, aunque las dos partes tienen una estructura parecida. Ambos solvers parsean el input, generan IDs inválidos y suman el resultado, pero no existe una clase abstracta común que defina el esqueleto del algoritmo.

Esta ausencia también es positiva, porque aplicar patrones sin necesidad haría el código más complejo sin aportar beneficios reales.

---
# Conclusión

La solución del Día 2 está organizada en clases con responsabilidades bien separadas.

El paquete `day02.common` contiene los elementos compartidos por ambas partes: los rangos de IDs, el parser del input y la interfaz común para generar IDs inválidos.

La parte 1 utiliza una estrategia centrada en detectar IDs formados por un bloque repetido exactamente dos veces.

La parte 2 utiliza otra estrategia más general, capaz de detectar IDs formados por un bloque repetido al menos dos veces.

La clase `Day02Main` actúa como punto de entrada y coordina la ejecución de ambas partes sin contener lógica específica del problema.

Gracias a esta estructura, el código es claro, modular, extensible y fácil de mantener.
