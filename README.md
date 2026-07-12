
# Simulador de MMU — Paginación y Reemplazo de Páginas
 
Proyecto del Parcial 2 de **Sistemas Operativos** — Universidad del Valle, Cali.
 
Simulador desarrollado en **Java 17 + JavaFX** que representa el funcionamiento de una Unidad de Administración de Memoria (MMU), dividido en dos implementaciones:
 
1. **Traductor de direcciones virtuales a físicas**, usando paginación de un nivel.
2. **Simulador de reemplazo de páginas**, comparando los algoritmos FIFO, LRU y OPT.
---
 
## Autor
 
- **Nombre:** Andrés Felipe Rengifo Rodríguez
- **Curso:** Sistemas Operativos
- **Video explicativo:** [](https://drive.google.com/drive/u/0/folders/1F1vcAFby70XUxEkbLPFQ8QgyJH3RlaEZ)
---
 
## Tecnologías utilizadas
 
- Java 17
- JavaFX 17.0.14 (Controls + FXML)
- Maven (con `javafx-maven-plugin` y `jlink` para el empaquetado)
- Docker (despliegue en contenedor)
---
 
## Estructura del proyecto
 
```
PARCIAL-2/
├── Dockerfile
├── pom.xml
└── src/main/
    ├── java/parcial2/
    │   ├── Controller/
    │   │   └── MainController.java
    │   ├── Model/
    │   │   ├── Implementacion1/
    │   │   │   ├── LogicaTraduccion.java
    │   │   │   ├── TablaPaginas.java
    │   │   │   └── ResultadoTraduccion.java
    │   │   └── Implementacion2/
    │   │       ├── FIFO.java
    │   │       ├── LRU.java
    │   │       ├── OPT.java
    │   │       ├── DatosSimulacion.java
    │   │       └── Resultados.java
    │   ├── HelloApplication.java
    │   └── Launcher.java
    └── resources/parcial2/
        └── MainView.fxml
```
 
El proyecto sigue una arquitectura **MVC**, con clases de responsabilidad única siguiendo el paradigma orientado a objetos.
 
---
 
## Implementación 1: Traductor de direcciones
 
Dada una **dirección virtual** y un **tamaño de página** (potencia de 2), el simulador calcula:
 
1. **Número de página** = `direccionVirtual / tamañoPágina` (división entera)
2. **Desplazamiento (offset)** = `direccionVirtual % tamañoPágina` (residuo)
3. **Consulta a la tabla de páginas** (`HashMap<Integer, Integer>`): si la página no existe, se genera un **fallo de página** y se le asigna automáticamente el siguiente marco físico libre.
4. **Dirección física** = `(marco * tamañoPágina) + desplazamiento`
Este cálculo no es una suma libre, sino una concatenación de bits: el desplazamiento nunca supera el tamaño de página, por lo que nunca se mezcla con los bits del marco.
 
---
 
## Implementación 2: Reemplazo de páginas
 
Dada una secuencia de páginas solicitadas y un número de marcos disponibles, se simulan tres algoritmos:
 
| Algoritmo | Estrategia |
|---|---|
| **FIFO** | Saca la página que lleva más tiempo en memoria (la primera que entró), usando una cola. |
| **LRU** | Saca la página que menos recientemente se usó, revisando el historial hacia atrás. |
| **OPT** | Saca la página que tardará más en volver a usarse (o que nunca vuelve a aparecer), mirando hacia adelante en la secuencia. Empates se resuelven con criterio FIFO. |
 
La interfaz permite elegir con un menú desplegable qué algoritmo graficar, mostrando una tabla con el estado de cada marco en cada instante de tiempo y marcando con "X" los fallos de página.
 
---
 
## Cómo ejecutar el proyecto
 
 
### Opción Con Docker
 
El proyecto incluye un `Dockerfile` que empaqueta la aplicación con `jlink` en una imagen autocontenida (Java + JavaFX embebidos).

1. Clona el repositorio en tu máquina local:
   ```bash
   git clone https://github.com/andres-rengifo-code/PARCIAL-2.git
   ```
2. Accede al directorio del proyecto:
   ```bash
   cd PARCIAL-2
   ```
 
**3. Construir la imagen:**
```bash
docker build -t simulador-mmu .
```
 
**4. Requisito para ver la interfaz gráfica:** como los contenedores no tienen pantalla propia, es necesario un servidor X en el sistema anfitrión:
- **Windows:** instalar [VcXsrv](https://sourceforge.net/projects/vcxsrv/), abrir XLaunch con "Multiple windows" → "Start no client" → marcar **"Disable access control"**.
- **Mac:** instalar [XQuartz](https://www.xquartz.org/) y habilitar conexiones de red en sus preferencias.
- **Linux:** ejecutar `xhost +local:docker`.

**5. Correr el contenedor:**
 
Windows:
```bash
docker run -e DISPLAY=host.docker.internal:0 simulador-mmu
```
 
Mac:
```bash
IP=$(ifconfig en0 | grep inet | awk '$1=="inet" {print $2}')
xhost + $IP
docker run -e DISPLAY=$IP:0 simulador-mmu
```
 
Linux:
```bash
docker run -e DISPLAY=$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix simulador-mmu
```
 
---
 
## Casos de prueba
 
**Caso 1 — Implementación 1**
- Dirección virtual: `1502`, tamaño de página: `1024`
- Resultado esperado: página `1`, offset `478`, marco `0` (fallo), dirección física `478`
**Caso 2 — Implementación 2**
- Secuencia: `7,0,1,2,0,3,0,4,2,3,0,3,2`, marcos: `3`
- Resultado: FIFO `10` fallos, LRU `9` fallos, OPT `7` fallos
