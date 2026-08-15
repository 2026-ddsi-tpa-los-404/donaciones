# Diagramas de Arquitectura — Módulo Donaciones

## Diagrama de Componentes

```mermaid
flowchart LR
 subgraph DONACIONES["&lt;&lt;app&gt;&gt; Módulo Donaciones"]
        FA["&lt;&lt;component&gt;&gt;<br>Fachada"]
        REPO["&lt;&lt;library&gt;&gt;<br>InMemoryDonaciones<br>Repository"]
        MAPPER["&lt;&lt;library&gt;&gt;<br>Donaciones<br>DataMapper"]
        MODEL["&lt;&lt;library&gt;&gt;<br>Modelo de Dominio<br>Donacion · Producto<br>Categoria · SubCategoria<br>Identificador"]
  end
    FA --> REPO & MAPPER
    REPO --> MODEL
    MAPPER --> MODEL
    CLIENT["&lt;&lt;client&gt;&gt;<br>Otros Módulos / Tests"] -- <br> --o FA
    FA -- FachadaDonadoresYEntidades --o DON_ENT["&lt;&lt;app&gt;&gt;<br>Módulo<br>DonadoresYEntidades"]
    FA -- FachadaLogistica --o LOG["&lt;&lt;app&gt;&gt;<br>Módulo<br>Logistica"]
```

## Diagrama de Despliegue

```mermaid
graph TD
    TEST["Suite de Tests<br>(JUnit + Mockito)"]

    subgraph MAQUINA["&lt;&lt;device&gt;&gt; PC"]
        subgraph APP["&lt;&lt;app&gt;&gt; Spring Boot"]
            DON["Módulo Donaciones"]
            DON_ENT["Módulo DonadoresYEntidades"]
            LOG["Módulo Logistica"]
            INC["Módulo Incentivos"]
            MEM[("Memoria RAM")]
        end
    end

    TEST --> APP
    DON --> DON_ENT
    DON --> LOG
    DON_ENT --> INC
    DON --> MEM
    DON_ENT --> MEM
```

## Diagrama de Secuencia — Entrega 4

El rol de este
módulo pasa por dos flujos de integración existentes que otros módulos consumen
o dependen de esta entrega en adelante:

### 1. Validación de producto al crear una necesidad

Donadores y Entidades corrobora contra Donaciones si el producto solicitado en
una necesidad existe, reutilizando el endpoint `GET /productos/{id}` ya
expuesto. No se agregó un endpoint nuevo porque el existente ya distingue
"producto válido" (200) de "producto inválido" (404) sin ambigüedad.

```mermaid
sequenceDiagram
    participant Bot as Bot Telegram / Cliente
    participant DYE as Donadores y Entidades
    participant DON as Donaciones
    participant LOG as Logística

    Bot->>DYE: Alta de necesidad
    DYE->>DON: GET /productos/{productoID}
    alt Producto válido
        DON-->>DYE: 200 ProductoDTO
        DYE->>LOG: Consulta de stock disponible del producto
        LOG-->>DYE: Stock disponible / insuficiente
        DYE-->>Bot: Necesidad creada (con o sin asignación al momento)
    else Producto inválido
        DON-->>DYE: 404 Not Found
        DYE-->>Bot: Error: producto inexistente
    end
```

### 2. Registro de donación y notificación a Logística

El flujo de alta de una donación no cambió del lado de Donaciones. Lo relevante
para esta entrega es que la llamada a `gestionarDonacion` conecta con cola de Workers de Logística:
Donaciones no interpreta ni depende del cuerpo de la respuesta de Logística, por
lo que el pasaje de Logística a un modelo de stock + cola de trabajo no requiere ningún cambio en este módulo.

```mermaid
sequenceDiagram
    participant Cliente
    participant DON as Donaciones
    participant LOG as Logística

    Cliente->>DON: POST /donaciones
    DON->>DON: Generar ID secuencial y persistir
    DON->>LOG: POST /depositos/{depositoID}/donacion
    Note over DON,LOG: Logística encola la asignación internamente (Actualizacion Entrega 4).<br/>Donaciones no usa el body de la respuesta.
    LOG-->>DON: Respuesta (ignorada)
    DON-->>Cliente: 201 DonacionDTO
```
