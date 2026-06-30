# Día 6: Trash Compactor

En el sexto día del Advent of Code 2025, el problema se desarrolla dentro de un compactador de basura. Mientras esperas a que una familia de cefalópodos consiga abrir la puerta, ayudas a la más joven con sus deberes de matemáticas.

La hoja de ejercicios contiene varios problemas matemáticos colocados uno al lado de otro en una lista horizontal muy larga. Cada problema está formado por varios números y una operación al final.

Las operaciones posibles son:

* `+`: suma.
* `*`: multiplicación.

Ejemplo:

```text
123 328  51 64 
 45 64  387 23 
  6 98  215 314
*   +   *   +  
```

En la primera parte, los números de cada problema se leen horizontalmente por bloques. En la segunda parte, la lectura cambia y los números se interpretan por columnas, de derecha a izquierda.

---

## Parte 1

En la primera parte, cada problema se lee como un bloque vertical de números, separados de otros problemas por columnas completamente vacías.

A partir del ejemplo:

```text
123 328  51 64 
 45 64  387 23 
  6 98  215 314
*   +   *   +  
```

Se obtienen cuatro problemas:

```text
123 * 45 * 6 = 33210
328 + 64 + 98 = 490
51 * 387 * 215 = 4243455
64 + 23 + 314 = 401
```

El resultado final es la suma de todos los resultados individuales:

```text
33210 + 490 + 4243455 + 401 = 4277556
```

---

## Parte 2

En la segunda parte, los cefalópodos explican que su forma correcta de leer matemáticas es de derecha a izquierda y por columnas.

Cada número se encuentra en una columna. El dígito más significativo está arriba y el menos significativo abajo. Los problemas siguen separados por columnas completamente vacías, y la operación sigue apareciendo en la última fila del bloque.

Con el mismo ejemplo:

```text
123 328  51 64 
 45 64  387 23 
  6 98  215 314
*   +   *   +  
```

Los problemas se interpretan de otra forma:

```text
4 + 431 + 623 = 1058
175 * 581 * 32 = 3253600
8 + 248 + 369 = 625
356 * 24 * 1 = 8544
```

El resultado final es:

```text
1058 + 3253600 + 625 + 8544 = 3263827
```

---

# Estructura del proyecto

La solución del Día 6 mantiene la misma estructura modular usada en los días anteriores:

```text
day06
├── Day06Main.java
├── common
│   ├── MathOperation.java
│   ├── MathProblem.java
│   ├── MathWorksheet.java
│   └── MathWorksheetParser.java
├── part1
│   ├── Day06Part1Solver.java
│   └── HorizontalMathWorksheetProblemExtractor.java
└── part2
    ├── Day06Part2Solver.java
    └── VerticalMathWorksheetProblemExtractor.java
```

La idea principal es separar:

* El punto de entrada del día.
* Las clases comunes del dominio matemático.
* El parseo de la hoja.
* La extracción horizontal de problemas para la parte 1.
* La extracción vertical de problemas para la parte 2.
* La resolución específica de cada parte.

---

# Clases del paquete `day06.common`

El paquete `day06.common` contiene las clases comunes utilizadas por ambas partes.

---

## `MathOperation`

El enum `MathOperation` representa las operaciones matemáticas posibles de la hoja.

Tiene dos valores:

* `ADD`: suma.
* `MULTIPLY`: multiplicación.

Su método principal es:

```java
public long applyTo(long left, long right)
```

Este método aplica la operación entre dos valores.

También contiene el método:

```java
public static MathOperation fromSymbol(String symbol)
```

Este método convierte un símbolo textual, como `+` o `*`, en su correspondiente operación del enum.

---

## `MathProblem`

El record `MathProblem` representa un problema matemático individual.

Está formado por:

```java
public record MathProblem(List<Long> numbers, MathOperation operation)
```

* `numbers`: lista de números del problema.
* `operation`: operación que debe aplicarse entre esos números.

Esta clase valida que:

* La lista de números no sea `null`.
* La lista de números no esté vacía.
* La operación no sea `null`.

Además, copia la lista de números con `List.copyOf`, evitando modificaciones externas.

Su método principal es:

```java
public long solve()
```

Este método calcula el resultado del problema aplicando la operación a todos los números.

---

## `MathWorksheet`

El record `MathWorksheet` representa la hoja completa de ejercicios.

Internamente almacena una lista de filas:

```java
public record MathWorksheet(List<String> rows)
```

Esta clase valida que:

* La lista de filas no sea `null`.
* La hoja tenga al menos dos filas.
* Ninguna fila sea `null`.
* Todas las filas tengan la misma anchura.

Sus métodos principales son:

```java
public int height()
```

Devuelve la altura de la hoja.

```java
public int width()
```

Devuelve la anchura de la hoja.

```java
public int operationRowIndex()
```

Devuelve el índice de la fila donde están las operaciones.

```java
public boolean isBlankColumn(int column)
```

Comprueba si una columna está completamente vacía.

```java
public String textAt(int row, int startColumn, int endColumn)
```

Devuelve el texto de una fila entre dos columnas.

```java
public char characterAt(int row, int column)
```

Devuelve el carácter situado en una posición concreta.

---

## `MathWorksheetParser`

La clase `MathWorksheetParser` transforma las líneas del input en un objeto `MathWorksheet`.

Su método principal es:

```java
public MathWorksheet parse(List<String> lines)
```

El parser ignora líneas en blanco y normaliza la anchura de todas las filas. Esto es importante porque el problema depende de la posición de las columnas, y algunas líneas pueden perder espacios finales al copiar el input.

Para solucionarlo, el parser calcula la anchura máxima y rellena las filas más cortas con espacios a la derecha.

---

# Clases del paquete `day06.part1`

El paquete `day06.part1` contiene la solución específica de la primera parte.

---

## `Day06Part1Solver`

La clase `Day06Part1Solver` resuelve la primera parte del Día 6.

Implementa la interfaz común `PuzzleSolver`.

Su método `solve` realiza los siguientes pasos:

1. Usa `MathWorksheetParser` para convertir el input en un `MathWorksheet`.
2. Usa `HorizontalMathWorksheetProblemExtractor` para extraer los problemas.
3. Resuelve cada `MathProblem`.
4. Suma todos los resultados.
5. Devuelve el total.

---

## `HorizontalMathWorksheetProblemExtractor`

La clase `HorizontalMathWorksheetProblemExtractor` extrae los problemas de la hoja siguiendo la lectura de la parte 1.

Su responsabilidad es localizar los bloques de columnas que forman cada problema. Los problemas están separados por columnas completamente vacías.

Para cada bloque:

1. Extrae los números leyendo cada fila del bloque.
2. Extrae la operación desde la última fila.
3. Crea un objeto `MathProblem`.

Esta clase contiene la lógica específica de lectura horizontal del problema.

---

# Clases del paquete `day06.part2`

El paquete `day06.part2` contiene la solución específica de la segunda parte.

---

## `Day06Part2Solver`

La clase `Day06Part2Solver` resuelve la segunda parte del Día 6.

También implementa la interfaz `PuzzleSolver`.

Su método `solve` realiza los siguientes pasos:

1. Usa `MathWorksheetParser` para convertir el input en un `MathWorksheet`.
2. Usa `VerticalMathWorksheetProblemExtractor` para extraer los problemas.
3. Resuelve cada `MathProblem`.
4. Suma todos los resultados.
5. Devuelve el total.

---

## `VerticalMathWorksheetProblemExtractor`

La clase `VerticalMathWorksheetProblemExtractor` extrae los problemas siguiendo la lectura correcta de la parte 2.

A diferencia de la parte 1, los números no se leen por filas dentro del bloque, sino por columnas.

El extractor:

1. Localiza cada bloque de columnas separado por columnas vacías.
2. Recorre las columnas del bloque de derecha a izquierda.
3. En cada columna, lee los dígitos de arriba hacia abajo.
4. Convierte cada columna numérica en un número.
5. Extrae la operación desde la última fila.
6. Crea un objeto `MathProblem`.

Esta clase contiene la lógica específica de lectura vertical del problema.

---

# Clase del paquete `day06`

El paquete `day06` contiene la clase principal del Día 6.

---

## `Day06Main`

La clase `Day06Main` es el punto de entrada para ejecutar la solución completa del Día 6.

El método `main` realiza los siguientes pasos:

1. Lee todas las líneas del archivo `src/main/resources/day06/input.txt`.
2. Crea una instancia de `Day06Part1Solver`.
3. Crea una instancia de `Day06Part2Solver`.
4. Ejecuta el método `solve` de ambos solvers.
5. Guarda los resultados.
6. Imprime los resultados por consola.

Esta clase utiliza la interfaz `PuzzleSolver` para referenciar ambos solvers:

```java
PuzzleSolver part1Solver = new Day06Part1Solver();
PuzzleSolver part2Solver = new Day06Part2Solver();
```

---

# Interfaz común del proyecto

El proyecto utiliza la interfaz común `PuzzleSolver`, ubicada en el paquete `aoc2025.common`.

Esta interfaz define el contrato común para todos los solvers del Advent of Code:

```java
long solve(List<String> lines);
```

En el Día 6, tanto `Day06Part1Solver` como `Day06Part2Solver` implementan esta interfaz.

---

# Fundamentos de diseño utilizados

En la solución del Día 6 se utilizan los siguientes fundamentos de diseño:

* Alta cohesión.
* Bajo acoplamiento.
* Modularidad.
* Código expresivo.
* Abstracción.
* Encapsulación.
* Diseño por contrato.
* Inmutabilidad.
* Normalización de entrada.

---

# Principios de diseño aplicados

En la solución del Día 6 se aplican los siguientes principios de diseño:

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

En la solución del Día 6 se utilizan los siguientes patrones de diseño:

* Iterator.
* Factory Method, aplicado mediante `MathOperation.fromSymbol`.

---

# Patrones no aplicados

En la solución del Día 6 no se aplican los siguientes patrones de diseño:

* Singleton.
* Adapter.
* Decorator.
* Observer.

---
