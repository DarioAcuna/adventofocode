## Día 1: Secret Entrance

En el primer día del Advent of Code 2025, el problema plantea una situación en la que los elfos han descubierto una entrada secreta a la base del Polo Norte. Para poder acceder, es necesario obtener la contraseña correcta de una caja fuerte con un dial circular.

El dial contiene los números del `0` al `99` y comienza apuntando al número `50`. El documento de entrada contiene una lista de rotaciones, una por línea. Cada rotación empieza con una letra:

* `L`: girar hacia la izquierda, es decir, hacia números menores.
* `R`: girar hacia la derecha, es decir, hacia números mayores.

Después de la letra aparece un número que indica cuántos clics debe girarse el dial. Como el dial es circular, al girar hacia la izquierda desde `0` se pasa a `99`, y al girar hacia la derecha desde `99` se vuelve a `0`.

### Parte 1

En la primera parte, el objetivo consiste en seguir todas las rotaciones del documento y contar cuántas veces el dial termina apuntando al número `0` después de completar una rotación.

Es importante destacar que solo se cuenta el estado final de cada instrucción, no los números por los que pasa el dial durante el giro. Por tanto, si una rotación pasa por el `0` pero termina en otro número, no se cuenta en esta parte.

La contraseña de la primera parte es el número total de rotaciones que dejan el dial apuntando a `0`.

### Parte 2

En la segunda parte cambia la forma de calcular la contraseña. Ahora no solo se cuentan las veces que el dial termina en `0`, sino todas las veces que cualquier clic individual hace que el dial apunte a `0`, incluso si ocurre durante una rotación.

Esto significa que, si una rotación es suficientemente larga, puede pasar por el `0` varias veces. Por ejemplo, una rotación de `R1000` desde el número `50` haría que el dial apuntara a `0` diez veces antes de volver a terminar en `50`.

Por tanto, en esta parte es necesario analizar cada rotación teniendo en cuenta todos los cruces por el número `0`, no únicamente la posición final del dial.

La contraseña de la segunda parte es el número total de veces que el dial apunta a `0` durante todos los clics realizados.


## Clases del paquete `common`

El paquete `common` contiene las clases comunes utilizadas por las dos partes del Día 1. Su objetivo es representar el dominio principal del problema: un dial circular, sus direcciones de giro, las rotaciones indicadas en el input y la conversión de texto a objetos del programa.

### `CircularDial`

La clase `CircularDial` representa el dial circular de la caja fuerte. Es la clase principal del dominio, ya que mantiene la posición actual del dial y contiene la lógica necesaria para girarlo.

El dial tiene un tamaño fijo de `100` posiciones, numeradas del `0` al `99`. Al ser circular, cuando se supera el límite superior o inferior, la posición vuelve a entrar dentro del rango válido usando aritmética modular.

Sus responsabilidades principales son:

* Guardar la posición actual del dial.
* Validar que la posición inicial esté entre `0` y `99`.
* Aplicar una rotación hacia la izquierda o hacia la derecha.
* Calcular la nueva posición del dial después de una rotación.
* Contar cuántas veces el dial apunta a `0` durante una rotación, necesario para resolver la segunda parte del problema.

Esta clase es mutable, ya que su atributo `position` cambia cada vez que se aplica una rotación.

### `Direction`

El enum `Direction` representa la dirección en la que puede girar el dial.

Tiene dos valores posibles:

* `LEFT`: representa un giro hacia la izquierda, es decir, hacia números menores.
* `RIGHT`: representa un giro hacia la derecha, es decir, hacia números mayores.

Su propósito es evitar trabajar directamente con caracteres como `'L'` o `'R'` dentro de la lógica del programa. De esta forma, el código es más expresivo y seguro, ya que las direcciones válidas quedan limitadas a los valores definidos en el enum.

### `Rotation`

El record `Rotation` representa una instrucción de rotación del dial.

Cada rotación está formada por dos datos:

* `direction`: la dirección del giro.
* `distance`: el número de clics que debe moverse el dial.

Esta clase se encarga de agrupar ambos valores en un único objeto, haciendo que el código sea más claro. En lugar de pasar por separado una dirección y una distancia, se trabaja con una rotación completa.

Además, valida que la dirección no sea `null` y que la distancia no sea negativa. Esto evita crear rotaciones inválidas y ayuda a mantener la consistencia del programa.

Al estar definida como `record`, es una clase inmutable: una vez creada una rotación, sus valores no cambian.

### `RotationParser`

La clase `RotationParser` se encarga de transformar cada línea del archivo de entrada en un objeto `Rotation`.

El input del problema contiene líneas con el formato:

```text
L68
R48
L5
```

Esta clase interpreta el primer carácter de cada línea como la dirección del giro y el resto de la cadena como la distancia. Por ejemplo, la línea `L68` se convierte en una rotación hacia la izquierda con distancia `68`.

Sus responsabilidades principales son:

* Validar que la línea no sea nula ni esté vacía.
* Leer el carácter inicial de la instrucción.
* Convertir `L` en `Direction.LEFT`.
* Convertir `R` en `Direction.RIGHT`.
* Convertir el resto de la línea en un número entero.
* Crear y devolver un objeto `Rotation`.

Esta clase separa la lógica de parseo de la lógica del dial. Gracias a eso, `CircularDial` no necesita saber cómo viene escrito el input, sino que solo trabaja con objetos `Rotation` ya construidos.

### Resumen general

En conjunto, estas clases separan claramente las responsabilidades del problema:

* `CircularDial` modela el estado y comportamiento del dial.
* `Direction` define los sentidos posibles de giro.
* `Rotation` representa una instrucción ya interpretada.
* `RotationParser` convierte el texto del input en objetos del dominio.

Esta separación hace que el código sea más claro, más fácil de probar y más mantenible, ya que cada clase tiene una responsabilidad concreta dentro de la solución.

## Clases de los paquetes `day01.part1` y `day01.part2`

Los paquetes `day01.part1` y `day01.part2` contienen las clases encargadas de resolver cada una de las dos partes del Día 1. Ambas clases implementan la interfaz `PuzzleSolver`, por lo que siguen una misma estructura: reciben una lista de líneas del input y devuelven el resultado numérico de la solución.

Estas clases no modelan directamente el dial ni las rotaciones, sino que coordinan el uso de las clases del paquete `day01.common`.

---

### `Day01Part1Solver`

La clase `Day01Part1Solver` se encarga de resolver la primera parte del problema.

Su responsabilidad principal es calcular cuántas veces el dial termina apuntando al número `0` después de completar una rotación.

Para ello, realiza los siguientes pasos:

1. Crea un `CircularDial` con la posición inicial `50`.
2. Recorre todas las líneas del input.
3. Usa `RotationParser` para convertir cada línea en un objeto `Rotation`.
4. Aplica la rotación al dial mediante el método `rotate`.
5. Comprueba si la posición final del dial es `0`.
6. Si el dial termina en `0`, incrementa el contador de la contraseña.

Esta clase representa la lógica específica de la primera parte: solo cuenta el `0` cuando aparece como posición final tras una rotación.

Por ejemplo, si durante una rotación el dial pasa por `0` pero termina en otro número, esa aparición no se cuenta en esta parte.

---

### `Day01Part2Solver`

La clase `Day01Part2Solver` se encarga de resolver la segunda parte del problema.

Su responsabilidad principal es calcular cuántas veces el dial apunta al número `0` durante todos los clics realizados, no solo al finalizar cada rotación.

Para ello, realiza los siguientes pasos:

1. Crea un `CircularDial` con la posición inicial `50`.
2. Recorre todas las líneas del input.
3. Usa `RotationParser` para convertir cada línea en un objeto `Rotation`.
4. Llama al método `countZeroClicksDuring` del dial.
5. Suma al resultado total el número de veces que esa rotación hace que el dial apunte a `0`.

A diferencia de la primera parte, esta clase no comprueba únicamente la posición final del dial. En su lugar, delega en `CircularDial` el cálculo de cuántas veces se cruza por el número `0` durante la rotación completa.

Esto permite resolver correctamente casos donde una rotación larga puede pasar varias veces por `0`.

---

### Diferencia principal entre ambas clases

La diferencia entre `Day01Part1Solver` y `Day01Part2Solver` está en qué se considera una aparición válida del número `0`.

En la primera parte, solo se cuenta si el dial termina en `0` después de ejecutar una instrucción completa.

En la segunda parte, se cuenta cada vez que un clic individual deja el dial apuntando a `0`, aunque la rotación termine en otra posición.

Por tanto:

* `Day01Part1Solver` cuenta posiciones finales.
* `Day01Part2Solver` cuenta cruces por `0` durante el movimiento completo.

---

## Clase del paquete `day01`

El paquete `day01` contiene la clase principal del Día 1. Esta clase actúa como punto de entrada para ejecutar la solución completa del ejercicio.

### `Day01Main`

La clase `Day01Main` es la encargada de iniciar la ejecución del Día 1.

Su responsabilidad principal no es resolver directamente el problema, sino coordinar los elementos necesarios para ejecutar ambas partes. Para ello, lee el archivo de entrada, crea los solvers correspondientes y muestra por consola los resultados obtenidos.

El método `main` realiza los siguientes pasos:

1. Lee todas las líneas del archivo `src/main/resources/day01/input.txt`.
2. Crea una instancia de `Day01Part1Solver`.
3. Crea una instancia de `Day01Part2Solver`.
4. Ejecuta el método `solve` de cada solver usando las líneas del input.
5. Guarda los resultados de la parte 1 y de la parte 2.
6. Imprime ambos resultados por consola.

Esta clase utiliza la interfaz `PuzzleSolver` para referenciar los solvers de ambas partes. Gracias a esto, el código no depende directamente del tipo concreto a la hora de ejecutar la solución, sino de una abstracción común.

```java
PuzzleSolver part1Solver = new Day01Part1Solver();
PuzzleSolver part2Solver = new Day01Part2Solver();
```

Esto permite que ambas partes sigan la misma estructura: reciben una lista de líneas y devuelven un resultado numérico.

### Propósito dentro del proyecto

`Day01Main` funciona como clase lanzadora del Día 1. Es decir, conecta el input real del problema con las clases que contienen la lógica de resolución.

Mientras que las clases `Day01Part1Solver` y `Day01Part2Solver` se encargan de resolver cada parte, `Day01Main` se encarga de preparar la ejecución y mostrar los resultados.

Por tanto, sus responsabilidades son:

* Localizar y leer el archivo de entrada.
* Instanciar los solvers de ambas partes.
* Ejecutar las soluciones.
* Mostrar los resultados finales.

### Resumen general

La clase `Day01Main` separa la ejecución del programa de la lógica del problema. No contiene reglas específicas sobre cómo se mueve el dial ni cómo se calcula la contraseña. Esa lógica queda delegada en los solvers y en las clases del paquete `day01.common`.

Gracias a esta separación, el código queda mejor organizado:

* `Day01Main` ejecuta el programa.
* `Day01Part1Solver` resuelve la primera parte.
* `Day01Part2Solver` resuelve la segunda parte.
* Las clases de `day01.common` modelan el dominio del problema.

Esta estructura hace que el proyecto sea más claro, modular y fácil de mantener.

## Interfaz común del proyecto

Además de las clases específicas del Día 1, el proyecto incluye una interfaz común ubicada en el paquete `aoc2025.common`. Esta interfaz se utiliza como base para todos los días del Advent of Code.

### `PuzzleSolver`

La interfaz `PuzzleSolver` define el contrato que deben cumplir todas las clases encargadas de resolver un puzzle.

Contiene un único método:

```java
long solve(List<String> lines);
```

Este método recibe como parámetro una lista de líneas de texto, que representa el contenido del archivo de entrada del problema, y devuelve un valor numérico de tipo `long` con el resultado de la solución.

Su propósito principal es unificar la forma en la que se resuelven los distintos ejercicios del proyecto. Gracias a esta interfaz, todos los solvers siguen la misma estructura, independientemente del día o de la parte que estén resolviendo.

Por ejemplo, en el Día 1, tanto `Day01Part1Solver` como `Day01Part2Solver` implementan esta interfaz. Esto permite tratarlos de forma común desde `Day01Main`, usando el tipo `PuzzleSolver` en lugar de depender directamente de las clases concretas.

```java
PuzzleSolver part1Solver = new Day01Part1Solver();
PuzzleSolver part2Solver = new Day01Part2Solver();
```

### Propósito dentro del proyecto

La interfaz `PuzzleSolver` actúa como una abstracción común para todos los resolutores del Advent of Code.

Sus responsabilidades son:

* Definir una estructura común para resolver puzzles.
* Separar la ejecución del programa de la implementación concreta de cada solución.
* Permitir que cada parte de cada día tenga su propia clase solver.
* Facilitar la reutilización de código en las clases principales de cada día.
* Hacer que el proyecto sea más escalable y organizado.

Gracias a esta interfaz, añadir nuevos días o nuevas partes resulta más sencillo, ya que basta con crear una nueva clase que implemente `PuzzleSolver` y defina su propia lógica dentro del método `solve`.

### Resumen general

`PuzzleSolver` no resuelve ningún problema por sí misma. Su función es definir un contrato común que todas las soluciones deben respetar.

Esto mejora la organización del proyecto porque permite que todos los días del Advent of Code sigan una misma estructura:

* Una clase principal que lee el input y ejecuta los solvers.
* Una clase solver para la parte 1.
* Una clase solver para la parte 2.
* Una interfaz común que garantiza que todos los solvers puedan ejecutarse de la misma manera.

En resumen, `PuzzleSolver` aporta abstracción, uniformidad y escalabilidad al proyecto.

## Fundamentos, principios y patrones de diseño aplicados

En la solución del Día 1 se aplican varios fundamentos y principios de diseño orientados a conseguir un código claro, modular, mantenible y fácil de extender. Aunque el problema es pequeño, la estructura elegida separa correctamente las responsabilidades entre las clases del dominio, los solvers de cada parte y la clase principal de ejecución.

---
## Fundamentos de diseño utilizados

En la solución del Día 1 se utilizan los siguientes fundamentos de diseño:

* Alta cohesión.
* Bajo acoplamiento.
* Modularidad.
* Código expresivo.
* Abstracción.


## Principios de diseño aplicados

En la solución del Día 1 se aplican los siguientes principios de diseño:

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

## Patrones de diseño aplicados
En la solución del Día 1 se utilizan los siguientes patrones de diseño:

* Iterator.
* Strategy.

## Patrones no aplicados

No se aplica `Singleton`, porque no hay ninguna clase que necesite garantizar una única instancia global.

No se aplica `Factory Method`, porque los objetos se crean directamente con sus constructores y no hay una lógica compleja de creación.

No se aplica `Adapter`, porque no se está adaptando una interfaz externa o incompatible.

No se aplica `Decorator`, porque no se añaden responsabilidades dinámicas a objetos existentes.

No se aplica `Observer`, porque no hay objetos suscritos a cambios de estado de otros objetos.

---


