# 🎓 Gestión EduOdoo - Sistema de Gestión Académica

Este proyecto es un sistema integral para la gestión de academias desarrollado en **Odoo 19**, diseñado para centralizar la administración de profesores, alumnos, cursos y sesiones formativas.

## 🚀 Funcionalidades Principales
- **Gestión Académica**: Control total de inscripciones, cursos y planificación.
- **Vistas Avanzadas**: Interfaz moderna con vistas **Kanban** y **Calendario** para un seguimiento visual.
- **Control de Ocupación**: Cálculo dinámico de plazas y visualización mediante barras de progreso.
- **Seguridad**: Sistema de permisos (ACLs) configurado para usuarios estándar y administradores.

## 🛠️ Estructura del Proyecto
El sistema se compone de varios módulos personalizados:
- `profesor`: Gestión de docentes y titulaciones.
- `alumno`: Registro de estudiantes y su historial académico.
- `curso`: Catálogo de enseñanza por niveles.
- `sesion`: Planificación horaria y control de asistencia.
- `matricula`: Flujo de inscripciones y estados de pago.
- `clases`: Asignación de grupos y horarios específicos.
- `menu_eduodoo`: Interfaz centralizada que unifica toda la plataforma.

---

## ⚖️ Resolución de Restricciones y Lógica de Negocio

A continuación, se detalla cómo se han resuelto técnicamente las restricciones solicitadas:

### 📊 Campos Computados (`@api.depends`)
- **Porcentaje de Ocupación**: Implementado en el modelo `sesion.sesion`. Calcula el ratio entre alumnos matriculados (en estado confirmado/pagado) y el número de asientos totales.
- **Color de Sesión**: Un campo calculado que devuelve un identificador numérico basado en la ocupación (Verde < 50%, Amarillo > 50%, Naranja > 80%, Rojo >= 100%). Este campo alimenta la lógica visual de las ProgressBar.
- **Fecha de Fin**: Implementado para la vista Calendario, calculando automáticamente el final de la sesión sumando la `duración` a la `fecha_inicio`.
- **Nombres Representativos**: Se ha usado `_compute_display_name` para que los profesores y alumnos se muestren con su nombre completo en lugar de códigos técnicos.

### 🛡️ Validaciones y Restricciones (`@api.constrains`)
- **Conflicto de Horario del Profesor**: El sistema realiza una búsqueda en la base de datos antes de guardar una sesión para asegurar que el profesor asignado no tenga otra clase programada que solape en el mismo rango de tiempo.
- **Límite de Capacidad**: Una restricción impide guardar cambios si el número de alumnos inscritos supera el total de asientos definidos para la sesión, lanzando un mensaje de error preventivo.
- **Email Único**: Restricción SQL para garantizar que no existan duplicados en el registro de alumnos por correo electrónico.

### 🔄 Flujo de Estados
- **Matrículas**: Implementa un ciclo de vida robusto: `Borrador` → `Confirmada` → `Pagada`. La lógica de ocupación solo contabiliza a los alumnos que han superado el estado de borrador, asegurando que las estadísticas de las sesiones sean reales.
