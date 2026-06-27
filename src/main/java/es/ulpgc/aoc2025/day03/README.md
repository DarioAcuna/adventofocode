# Advent of Code 2025 - Día 3: Lobby

Este documento recoge la explicación del **Día 3** del Advent of Code 2025: **Lobby**.

El problema plantea que los ascensores y las escaleras mecánicas del vestíbulo están sin funcionar. Para alimentar las escaleras mecánicas, se dispone de varios bancos de baterías. Cada banco aparece en el input como una línea de dígitos, y cada dígito representa la potencia de una batería, con valores entre `1` y `9`.

El voltaje producido por un banco se calcula formando un número con las baterías seleccionadas. Las baterías no pueden reordenarse: el número debe respetar el orden en el que aparecen los dígitos en la línea.

---

## Resumen del ejercicio

Cada línea del input representa un banco de baterías.

Ejemplo:

```text
987654321111111
811111111111119
234234234234278
818181911112111
```

Dentro de cada banco hay que seleccionar una cantidad fija de baterías. El resultado de cada banco es el número más grande que se puede formar manteniendo el orden original de los dígitos seleccionados.

---

## Parte 1

En la primera parte hay que encender exactamente **2 baterías** de cada banco.

El objetivo es obtener el mayor número posible de dos dígitos por cada línea.

Por ejemplo, en el banco:

```text
987654321111111
```

la mejor elección es `98`, porque son los dos primeros dígitos y forman el mayor número posible respetando el orden.

Con el ejemplo oficial, los mayores voltajes son:

```text
98
89
78
92
```

La suma total del ejemplo es:

```text
357
```

---

## Parte 2

En la segunda parte hay que encender exactamente **12 baterías** de cada banco.

El objetivo sigue siendo formar el mayor número posible manteniendo el orden original, pero ahora el número resultante tiene 12 dígitos.

Por ejemplo, en el banco:

```text
987654321111111
```

el mayor voltaje posible es:

```text
987654321111
```

Con el ejemplo oficial, los mayores voltajes son:

```text
987654321111
811111111119
434234234278
888911112111
```

La suma total del ejemplo es:

```text
3121910778619
```

---

# Estructura del proyecto

La solución del Día 3 mantiene la misma estructura modular usada en los días anteriores:

```text
day03
├── Day03Main.java
├── common
│   ├── BatteryBank.java
│   ├── BatteryBankParser.java
│   └── MaximumJoltageCalculator.java
├── part1
│   └── Day03Part1Solver.java
└── part2
    └── Day03Part2Solver.java
```

El objetivo de esta organización es separar claramente:

- El punto de entrada del día.
- Las clases comunes del dominio.
- La solución específica de la parte 1.
- La solución específica de la parte 2.

La lógica de cálculo del mayor voltaje se coloca en `common` porque sirve para ambas partes. La diferencia entre las dos partes no está en el algoritmo, sino en la cantidad de baterías que deben encenderse.

```text
Parte 1 -> elegir 2 baterías
Parte 2 -> elegir 12 baterías
```

---

# Clases del paquete `day03.common`

El paquete `day03.common` contiene las clases compartidas por las dos partes del problema. Estas clases representan el dominio principal del Día 3: los bancos de baterías, el parseo del input y el cálculo del mayor voltaje posible.

---

## `BatteryBank`

El record `BatteryBank` representa un banco de baterías.

Cada objeto contiene una cadena de texto con los dígitos de las baterías disponibles en ese banco.

Por ejemplo:

```text
987654321111111
```

representa un banco con quince baterías.

Esta clase valida que el banco sea correcto antes de permitir su creación. Las reglas son:

- La cadena de baterías no puede ser `null`.
- El banco debe contener al menos dos baterías.
- Solo se permiten dígitos entre `1` y `9`.

Esto evita que el programa trabaje con bancos inválidos, como cadenas vacías, valores nulos o baterías con dígito `0`.

Al estar definido como `record`, `BatteryBank` es inmutable. Una vez creado, el contenido del banco no cambia.

---

## `BatteryBankParser`

La clase `BatteryBankParser` se encarga de transformar las líneas del input en objetos `BatteryBank`.

Cada línea no vacía del archivo representa un banco de baterías. El parser recorre todas las líneas, ignora las líneas en blanco y crea un objeto `BatteryBank` con el contenido de cada línea.

Sus responsabilidades principales son:

- Recorrer las líneas del input.
- Ignorar líneas vacías.
- Eliminar espacios innecesarios con `trim()`.
- Crear objetos `BatteryBank`.
- Devolver la lista de bancos parseados.

Esta clase separa la lógica de interpretación del input de la lógica de cálculo del voltaje.

---

## `MaximumJoltageCalculator`

La clase `MaximumJoltageCalculator` calcula el mayor voltaje posible para un banco de baterías, seleccionando una cantidad concreta de baterías.

Su método principal es:

```java
long calculate(BatteryBank bank, int batteriesToTurnOn);
```

Este método recibe:

- Un `BatteryBank`, que contiene la secuencia de baterías.
- Un entero `batteriesToTurnOn`, que indica cuántas baterías deben seleccionarse.

La misma clase sirve para resolver ambas partes:

```text
Parte 1 -> batteriesToTurnOn = 2
Parte 2 -> batteriesToTurnOn = 12
```

Antes de calcular el resultado, valida que la cantidad de baterías a encender sea correcta:

- Debe ser mayor que cero.
- No puede ser mayor que el número de baterías disponibles en el banco.

Internamente utiliza el método privado `maximumSubsequence`, que busca la mayor subsecuencia posible de una longitud concreta.

La idea del algoritmo es recorrer los dígitos de izquierda a derecha y eliminar dígitos anteriores cuando aparece un dígito mayor y todavía se pueden descartar baterías. De esta forma se obtiene el número más grande posible sin romper el orden original.

Por ejemplo, para:

```text
818181911112111
```

seleccionando 12 baterías, el resultado máximo es:

```text
888911112111
```

Esta clase contiene la lógica algorítmica común de ambas partes.

---

# Clases de los paquetes `day03.part1` y `day03.part2`

Los paquetes `day03.part1` y `day03.part2` contienen las clases encargadas de resolver cada parte del Día 3.

Ambas implementan la interfaz común `PuzzleSolver`, por lo que siguen la misma estructura: reciben una lista de líneas del input y devuelven el resultado numérico de la solución.

---

## `Day03Part1Solver`

La clase `Day03Part1Solver` resuelve la primera parte del problema.

Su responsabilidad principal es calcular la suma total de los mayores voltajes posibles cuando se encienden exactamente 2 baterías de cada banco.

Para ello, realiza los siguientes pasos:

1. Usa `BatteryBankParser` para convertir el input en una lista de bancos.
2. Inicializa el acumulador `totalJoltage` a cero.
3. Recorre todos los bancos.
4. Para cada banco, llama a `MaximumJoltageCalculator` con `BATTERIES_TO_TURN_ON = 2`.
5. Suma el resultado de cada banco.
6. Devuelve el total final.

La constante:

```java
private static final int BATTERIES_TO_TURN_ON = 2;
```

expresa claramente la regla específica de la parte 1.

---

## `Day03Part2Solver`

La clase `Day03Part2Solver` resuelve la segunda parte del problema.

Su responsabilidad principal es calcular la suma total de los mayores voltajes posibles cuando se encienden exactamente 12 baterías de cada banco.

Su estructura es prácticamente igual a la de la parte 1, pero cambia la constante que indica cuántas baterías deben encenderse:

```java
private static final int BATTERIES_TO_TURN_ON = 12;
```

Para resolver la parte 2, realiza los siguientes pasos:

1. Usa `BatteryBankParser` para convertir el input en una lista de bancos.
2. Inicializa el acumulador `totalJoltage` a cero.
3. Recorre todos los bancos.
4. Para cada banco, llama a `MaximumJoltageCalculator` con `BATTERIES_TO_TURN_ON = 12`.
5. Suma el resultado de cada banco.
6. Devuelve el total final.

La diferencia entre ambas partes queda aislada en una constante, mientras que la lógica de cálculo se reutiliza.

---

# Clase del paquete `day03`

El paquete `day03` contiene la clase principal del Día 3.

---

## `Day03Main`

La clase `Day03Main` es el punto de entrada para ejecutar la solución completa del Día 3.

Su responsabilidad principal no es resolver directamente el problema, sino coordinar la ejecución de ambas partes.

El método `main` realiza los siguientes pasos:

1. Lee todas las líneas del archivo `src/main/resources/day03/input.txt`.
2. Crea una instancia de `Day03Part1Solver`.
3. Crea una instancia de `Day03Part2Solver`.
4. Ejecuta el método `solve` de cada solver.
5. Guarda los resultados de ambas partes.
6. Imprime los resultados por consola.

Esta clase usa la interfaz `PuzzleSolver` para referenciar ambos solvers:

```java
PuzzleSolver part1Solver = new Day03Part1Solver();
PuzzleSolver part2Solver = new Day03Part2Solver();
```

Gracias a esto, ambas partes pueden ejecutarse de forma uniforme, aunque cada una use una cantidad distinta de baterías.

---

# Interfaz común del proyecto

Además de las clases específicas del Día 3, el proyecto utiliza la interfaz común `PuzzleSolver`, ubicada en el paquete `aoc2025.common`.

Esta interfaz define el contrato común para todos los solvers del Advent of Code:

```java
long solve(List<String> lines);
```

En el Día 3, tanto `Day03Part1Solver` como `Day03Part2Solver` implementan esta interfaz.

Esto permite que las soluciones de todos los días mantengan una estructura común:

- Una clase principal que lee el input.
- Un solver para la parte 1.
- Un solver para la parte 2.
- Un método `solve` común para ejecutar cada solución.

\newpage

# Fundamentos de diseño utilizados

En la solución del Día 3 se utilizan los siguientes fundamentos de diseño:

- Alta cohesión.
- Bajo acoplamiento.
- Modularidad.
- Código expresivo.
- Abstracción.
- Encapsulación.
- Diseño por contrato.
- Inmutabilidad.
- Reutilización de lógica común.

---

# Principios de diseño aplicados

En la solución del Día 3 se aplican los siguientes principios de diseño:

- Principio de Responsabilidad Única, SRP.
- Principio Abierto/Cerrado, OCP.
- Principio de Sustitución de Liskov, LSP.
- Principio de Segregación de Interfaces, ISP.
- Principio de Inversión de Dependencias, DIP.
- Composición sobre herencia.
- Principio DRY.
- Ley de Demeter.
- Principio YAGNI.
- Principio de mínima sorpresa.
- Principio de mínimo compromiso.

---

# Patrones de diseño aplicados

En la solución del Día 3 se utilizan los siguientes patrones de diseño:

- Iterator.
- Strategy.
- Command, aplicado parcialmente.

---

# Patrones no aplicados

Aunque algunos patrones aparecen en los materiales teóricos, no todos son necesarios en esta solución.

- No se aplica `Singleton`.
- No se aplica `Factory Method`.
- No se aplica `Adapter`.
- No se aplica `Decorator`.
- No se aplica `Observer`.
- No se aplica `Template Method` como patrón formal.

---

# Conclusión

La solución del Día 3 está organizada de forma modular. El dominio común se concentra en el paquete `day03.common`, mientras que cada parte tiene su propio solver.

El algoritmo principal está centralizado en `MaximumJoltageCalculator`, lo que permite reutilizar la misma lógica para la parte 1 y la parte 2 cambiando únicamente el número de baterías que deben encenderse.
