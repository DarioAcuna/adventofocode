# Día 11: Reactor

En el undécimo día del Advent of Code 2025, el problema se desarrolla en la zona del reactor de la fábrica. Los elfos están intentando que un nuevo rack de servidores se comunique correctamente con el reactor, pero sospechan que el fallo depende de ciertas rutas de datos entre dispositivos.

El input del problema describe una red de dispositivos. Cada línea indica un dispositivo y la lista de dispositivos a los que envía sus salidas.

Ejemplo:

```text id="t2lejz"
aaa: you hhh
you: bbb ccc
bbb: ddd eee
ccc: ddd eee fff
ddd: ggg
eee: out
fff: out
ggg: out
hhh: ccc fff iii
iii: out
```

Una línea como:

```text id="5hjcka"
bbb: ddd eee
```

significa que el dispositivo `bbb` tiene dos salidas: una hacia `ddd` y otra hacia `eee`.

Los datos solo fluyen hacia delante, desde un dispositivo hacia sus salidas. No pueden retroceder.

---

## Parte 1

En la primera parte, el objetivo es contar cuántos caminos distintos existen desde el dispositivo `you` hasta el dispositivo `out`.

Cada camino representa una posible ruta que pueden seguir los datos por la red de dispositivos.

En el ejemplo oficial, los caminos desde `you` hasta `out` son:

```text id="x915yo"
you -> bbb -> ddd -> ggg -> out
you -> bbb -> eee -> out
you -> ccc -> ddd -> ggg -> out
you -> ccc -> eee -> out
you -> ccc -> fff -> out
```

En total, hay `5` caminos.

Con el input real, el resultado obtenido para la parte 1 fue:

```text id="q6yfzy"
791
```

---

## Parte 2

En la segunda parte, los elfos descubren que la ruta problemática debe pasar por dos dispositivos concretos:

* `dac`
* `fft`

Ahora hay que contar los caminos desde `svr` hasta `out`, pero solo se consideran válidos los caminos que visitan ambos dispositivos obligatorios, en cualquier orden.

Por tanto, un camino cuenta si:

1. Empieza en `svr`.
2. Termina en `out`.
3. Pasa por `dac`.
4. Pasa por `fft`.

Con el input real, el resultado obtenido para la parte 2 fue:

```text id="l94cxt"
520476725037672
```

---

# Estructura del proyecto

La solución del Día 11 mantiene la misma estructura modular usada en los días anteriores:

```text id="evuwxp"
day11
├── Day11Main.java
├── common
│   ├── DeviceNetwork.java
│   └── DeviceNetworkParser.java
├── part1
│   ├── Day11Part1Solver.java
│   └── DevicePathCounter.java
└── part2
    ├── Day11Part2Solver.java
    └── RequiredDevicePathCounter.java
```

La idea principal es separar:

* El punto de entrada del día.
* Las clases comunes del dominio.
* El parseo de la red de dispositivos.
* La lógica específica de la parte 1.
* La lógica específica de la parte 2.
* El conteo de caminos simples.
* El conteo de caminos con dispositivos obligatorios.

---

# Clases del paquete `day11.common`

El paquete `day11.common` contiene las clases compartidas por ambas partes.

---

## `DeviceNetwork`

El record `DeviceNetwork` representa la red dirigida de dispositivos.

Internamente almacena un mapa:

```java id="i7umug"
public record DeviceNetwork(Map<String, List<String>> outputsByDevice)
```

La clave del mapa es el nombre de un dispositivo, y el valor es la lista de dispositivos a los que puede enviar datos.

Por ejemplo:

```text id="ydf8mu"
bbb: ddd eee
```

se representa como una entrada donde `bbb` apunta a la lista `[ddd, eee]`.

Esta clase valida que el mapa no sea `null` y lo copia usando `Map.copyOf`, evitando modificaciones externas.

Su método principal es:

```java id="k6gemr"
public List<String> outputsOf(String device)
```

Este método devuelve las salidas de un dispositivo. Si el dispositivo no aparece en el mapa, devuelve una lista vacía.

---

## `DeviceNetworkParser`

La clase `DeviceNetworkParser` transforma las líneas del input en un objeto `DeviceNetwork`.

Su método principal es:

```java id="f97wnj"
public DeviceNetwork parse(List<String> lines)
```

El parser realiza los siguientes pasos:

1. Recorre las líneas del input.
2. Ignora líneas en blanco.
3. Divide cada línea por `:`.
4. Extrae el nombre del dispositivo.
5. Extrae la lista de salidas.
6. Construye un mapa de dispositivos a salidas.
7. Crea un `DeviceNetwork`.

También valida que cada línea tenga el formato correcto y que el nombre del dispositivo no esté vacío.

---

# Clases del paquete `day11.part1`

El paquete `day11.part1` contiene la solución específica de la primera parte.

---

## `Day11Part1Solver`

La clase `Day11Part1Solver` resuelve la primera parte del Día 11.

Implementa la interfaz común `PuzzleSolver`.

Su método `solve` realiza estos pasos:

1. Usa `DeviceNetworkParser` para convertir el input en un `DeviceNetwork`.
2. Usa `DevicePathCounter`.
3. Cuenta los caminos desde `you` hasta `out`.
4. Devuelve el resultado.

La clase define como dispositivo inicial:

```java id="fxi2j0"
private static final String START_DEVICE = "you";
```

---

## `DevicePathCounter`

La clase `DevicePathCounter` contiene la lógica para contar todos los caminos desde un dispositivo inicial hasta `out`.

Su método principal es:

```java id="v5h9ah"
public long countPathsFrom(String startDevice, DeviceNetwork network)
```

El algoritmo funciona de forma recursiva:

1. Si el dispositivo actual es `out`, devuelve `1`.
2. Si ya se conoce el número de caminos desde ese dispositivo, usa el valor guardado en memoria.
3. Si el dispositivo está siendo visitado, se detecta un ciclo y se lanza una excepción.
4. Recorre todas las salidas del dispositivo actual.
5. Suma los caminos que existen desde cada salida hasta `out`.
6. Guarda el resultado en una tabla de memoización.
7. Devuelve el total.

La memoización evita recalcular varias veces el número de caminos desde un mismo dispositivo.

---

# Clases del paquete `day11.part2`

El paquete `day11.part2` contiene la solución específica de la segunda parte.

---

## `Day11Part2Solver`

La clase `Day11Part2Solver` resuelve la segunda parte del Día 11.

También implementa la interfaz `PuzzleSolver`.

Su método `solve` realiza estos pasos:

1. Usa `DeviceNetworkParser` para convertir el input en un `DeviceNetwork`.
2. Define como inicio el dispositivo `svr`.
3. Define como obligatorios los dispositivos `dac` y `fft`.
4. Usa `RequiredDevicePathCounter`.
5. Cuenta los caminos desde `svr` hasta `out` que visitan ambos dispositivos.
6. Devuelve el resultado.

La clase define:

```java id="c7757s"
private static final String START_DEVICE = "svr";
```

y:

```java id="wlmv97"
private static final Set<String> REQUIRED_DEVICES = Set.of(
        "dac",
        "fft"
);
```

---

## `RequiredDevicePathCounter`

La clase `RequiredDevicePathCounter` contiene la lógica para contar caminos que deben visitar ciertos dispositivos obligatorios.

Su método principal es:

```java id="ghywjh"
public long countPathsFrom(
        String startDevice,
        DeviceNetwork network,
        Set<String> requiredDevices
)
```

A diferencia de la parte 1, aquí no basta con saber en qué dispositivo estamos. También hay que saber qué dispositivos obligatorios ya han sido visitados.

Para eso, la clase usa un estado de búsqueda formado por:

* El dispositivo actual.
* El conjunto de dispositivos obligatorios ya visitados.

Internamente define el record:

```java id="kucegz"
private record SearchState(String device, RequiredDeviceState requiredDeviceState)
```

También define el record:

```java id="c2po78"
private record RequiredDeviceState(Set<String> requiredDevices, Set<String> visitedRequiredDevices)
```

Este estado permite saber si un camino ya ha pasado por `dac` y `fft`.

El algoritmo funciona así:

1. Empieza en `svr`.
2. Actualiza el estado si el dispositivo actual es obligatorio.
3. Si llega a `out`, comprueba si ya se visitaron todos los dispositivos obligatorios.
4. Si se visitaron todos, cuenta el camino como válido.
5. Si no, el camino no cuenta.
6. Usa memoización para evitar recalcular estados repetidos.
7. Usa detección de ciclos para evitar recorridos infinitos.

---

# Clase del paquete `day11`

El paquete `day11` contiene la clase principal del Día 11.

---

## `Day11Main`

La clase `Day11Main` es el punto de entrada para ejecutar la solución completa del Día 11.

El método `main` realiza los siguientes pasos:

1. Lee todas las líneas del archivo `src/main/resources/day11/input.txt`.
2. Crea una instancia de `Day11Part1Solver`.
3. Crea una instancia de `Day11Part2Solver`.
4. Ejecuta el método `solve` de ambos solvers.
5. Guarda los resultados.
6. Imprime los resultados por consola.

Esta clase utiliza la interfaz `PuzzleSolver` para referenciar ambos solvers:

```java id="su45cj"
PuzzleSolver part1Solver = new Day11Part1Solver();
PuzzleSolver part2Solver = new Day11Part2Solver();
```

---

# Interfaz común del proyecto

El proyecto utiliza la interfaz común `PuzzleSolver`, ubicada en el paquete `aoc2025.common`.

Esta interfaz define el contrato común para todos los solvers del Advent of Code:

```java id="ph184p"
long solve(List<String> lines);
```

En el Día 11, tanto `Day11Part1Solver` como `Day11Part2Solver` implementan esta interfaz.

---

# Fundamentos de diseño utilizados

En la solución del Día 11 se utilizan los siguientes fundamentos de diseño:

* Alta cohesión.
* Bajo acoplamiento.
* Modularidad.
* Código expresivo.
* Abstracción.
* Encapsulación.
* Diseño por contrato.
* Inmutabilidad.
* Modelado de grafos dirigidos.
* Recursividad.
* Memoización.
* Detección de ciclos.
* Búsqueda con estado.

---

# Principios de diseño aplicados

En la solución del Día 11 se aplican los siguientes principios de diseño:

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

En la solución del Día 11 se utilizan los siguientes patrones de diseño:

* Iterator.
* Strategy.
* Command, aplicado parcialmente.

---

# Patrones no aplicados

En la solución del Día 11 no se aplican los siguientes patrones de diseño:

* Singleton.
* Factory Method.
* Adapter.
* Decorator.
* Observer.
* Template Method.
* State.

---

# Conclusión

La solución del Día 11 está organizada de forma clara y modular.

La primera parte cuenta todos los caminos desde `you` hasta `out`.

La segunda parte cuenta los caminos desde `svr` hasta `out`, pero solo aquellos que visitan tanto `dac` como `fft`.

El diseño separa correctamente el parseo, el modelo de la red de dispositivos, el conteo de caminos simples y el conteo de caminos con estado adicional. Esto permite que el código sea fácil de entender, probar, mantener y defender en una explicación oral.
