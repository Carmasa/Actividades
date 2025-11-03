<?php
require_once __DIR__ . '/Conexion.php';
require_once __DIR__ . '/Habitacion.php';
require_once __DIR__ . '/Huesped.php';
require_once __DIR__ . '/Reserva.php';
require_once __DIR__ . '/Mantenimiento.php';
require_once __DIR__ . '/Limpieza.php';

session_start();

$mensaje = '';
$mensaje_huesped = '';
if (isset($_POST['theme'])) {
    $theme = $_POST['theme'];
    setcookie('theme_preference', $theme, time() + (86400 * 30), "/"); // 30 días
    header('Location: ' . $_SERVER['HTTP_REFERER']);
    exit;
}

// Modificar la pestaana por defecto según el rol
if (isset($_SESSION['rol']) && strtolower($_SESSION['rol']) === 'admin') {
    $tab = $_GET['tab'] ?? 'registro-huesped';
} else {
    $tab = $_GET['tab'] ?? 'ver-reservas';
}

// Mensajes formularios
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (isset($_POST['action'])) {
        switch ($_POST['action']) {
            case 'crear_huesped':
                try {
                    $huesped = new Huesped();
                    $existe = $huesped->obtenerPorCorreo($_POST['correo']);
                    if ($existe) {
                        $mensaje_huesped = "El correo electrónico ya existe";
                    } else {
                        $huesped->crear($_POST['nombre'], $_POST['correo'], $_POST['dni']);
                        $mensaje_huesped = "Registro de huésped exitoso";
                    }
                    $tab = 'registro-huesped';
                } catch (Exception $e) {
                    $mensaje_huesped = "Error: " . $e->getMessage();
                }
                break;

            case 'crear_reserva':
                try {
                    $huesped = new Huesped();
                    $huespedData = $huesped->obtenerPorCorreo($_POST['correo_huesped']);
                    if (!$huespedData) {
                        $mensaje = "El huésped no existe";
                    } else {
                        $reserva = new Reserva();
                        $reserva->crearReserva(
                            $huespedData['id_huesped'],
                            $_POST['habitacion'],
                            $_POST['fecha_llegada'],
                            $_POST['fecha_salida']
                        );
                        $mensaje = "Reserva creada exitosamente";
                    }
                    $tab = 'reserva-habitacion';
                } catch (Exception $e) {
                    $mensaje = "Error: " . $e->getMessage();
                }
                break;

            case 'registrar_mantenimiento':
                try {
                    $mantenimiento = new Mantenimiento();
                    $mantenimiento->registrarTarea(
                        $_POST['habitacion_mant'],
                        $_POST['tarea'],
                        $_POST['fecha_inicio'],
                        $_POST['fecha_fin'],
                        $_POST['descripcion'] ?? ''
                    );
                    $mensaje = "Mantenimiento registrado exitosamente";
                    $tab = 'mantenimiento';
                } catch (Exception $e) {
                    $mensaje = "Error: " . $e->getMessage();
                }
                break;

            case 'actualizar_limpieza':
                try {
                    $limpieza = new Limpieza();
                    $limpieza->actualizarEstado(
                        $_POST['habitacion_limp'],
                        $_POST['estado_limpieza']
                    );
                    $mensaje = "Estado de limpieza actualizado";
                    $tab = 'limpieza';
                } catch (Exception $e) {
                    $mensaje = "Error: " . $e->getMessage();
                }
                break;

            case 'confirmar_reserva':
                try {
                    $reserva = new Reserva();
                    $reserva->confirmarReserva($_POST['id_reserva']);
                    $mensaje = "Reserva confirmada correctamente.";
                    $tab = 'ver-reservas';
                } catch (Exception $e) {
                    $mensaje = "Error al confirmar: " . $e->getMessage();
                }
                break;

            case 'eliminar_reserva':
                try {
                    $reserva = new Reserva();
                    $reserva->cancelarReserva($_POST['id_reserva']);
                    $mensaje = "Reserva eliminada correctamente.";
                    $tab = 'ver-reservas';
                } catch (Exception $e) {
                    $mensaje = "Error al eliminar: " . $e->getMessage();
                }
                break;

            case 'finalizar_mantenimiento':
                try {
                    $mantenimiento = new Mantenimiento();
                    $mantenimiento->finalizarTarea($_POST['id_mantenimiento']);
                    $mensaje = "Tarea de mantenimiento finalizada correctamente.";
                    $tab = 'ver-habitaciones';
                } catch (Exception $e) {
                    $mensaje = "Error al finalizar: " . $e->getMessage();
                }
                break;
        }
    }
}

// Para los selects de habitaciones
$habitacionService = new Habitacion();
$habitaciones = $habitacionService->listarTodas();
?>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema de Gestión Hotelera</title>
    <link rel="stylesheet" href="styles.css">
    <?php if (isset($_COOKIE['theme_preference']) && $_COOKIE['theme_preference'] === 'dark'): ?>
    <link rel="stylesheet" href="dark-theme.css">
    <?php endif; ?>
</head>
<body>
    <header class="main-header">
        <img src="img/encabezado.jpg" alt="Encabezado" class="header-img">
        <h1 class="header-title">Sistema de Gestión Hotelera</h1>
    </header>

    <div class="theme-selector">
        <form action="set_theme.php" method="POST">
            <label>Tema:</label>
            <select name="theme" onchange="this.form.submit()">
                <option value="light" <?php echo (!isset($_COOKIE['theme_preference']) || $_COOKIE['theme_preference'] === 'light') ? 'selected' : ''; ?>>Claro</option>
                <option value="dark" <?php echo (isset($_COOKIE['theme_preference']) && $_COOKIE['theme_preference'] === 'dark') ? 'selected' : ''; ?>>Oscuro</option>
            </select>
        </form>
    </div>

    <?php if(isset($_SESSION['usuario'])): ?>
    <div class="welcome-section">
        <p class="welcome-message">
            Bienvenido, <strong><?php echo htmlspecialchars($_SESSION['usuario']); ?></strong>
            <?php if(isset($_SESSION['rol']) && $_SESSION['rol'] === 'admin'): ?>
            <br><small>(Administrador)</small>
            <?php endif; ?>
        </p>
        <a href="Login.php?logout=1" class="logout-btn">Cerrar sesión</a>
    </div>
    <?php endif; ?>

    <div class="container">
        
        <!-- Navegacion -->
        <div class="nav-tabs">
<?php if (isset($_SESSION['rol']) && strtolower($_SESSION['rol']) === 'admin'): ?>
        <a href="index.php?tab=registro-huesped" class="<?= $tab === 'registro-huesped' ? 'active' : '' ?>">Registro de Huésped</a>
        <a href="index.php?tab=reserva-habitacion" class="<?= $tab === 'reserva-habitacion' ? 'active' : '' ?>">Reservar Habitación</a>
        <a href="index.php?tab=mantenimiento" class="<?= $tab === 'mantenimiento' ? 'active' : '' ?>">Mantenimiento</a>
        <a href="index.php?tab=limpieza" class="<?= $tab === 'limpieza' ? 'active' : '' ?>">Estado de Limpieza</a>
        <a href="index.php?tab=ver-reservas" class="<?= $tab === 'ver-reservas' ? 'active' : '' ?>">Ver Reservas</a>
        <a href="index.php?tab=ver-habitaciones" class="<?= $tab === 'ver-habitaciones' ? 'active' : '' ?>">Ver Habitaciones</a>
<?php else: ?>
        <a href="index.php?tab=ver-reservas" class="<?= $tab === 'ver-reservas' ? 'active' : '' ?>">Ver Mis Reservas</a>
<?php endif; ?>
    </div>

        <!-- Mensajes de respuesta -->
        <div id="message"></div>
        <div id="mensaje-huesped"></div>
        <?php if ($mensaje): ?>
            <div class="alert"><?= $mensaje ?></div>
        <?php endif; ?>
        <?php if ($mensaje_huesped): ?>
            <div class="alert"><?= $mensaje_huesped ?></div>
        <?php endif; ?>

        <!-- Registro de Huésped -->
        <div id="registro-huesped" class="form-section<?= $tab === 'registro-huesped' ? ' active' : '' ?>">
            <h2>Registro de Nuevo Huésped</h2>
            <form id="form-huesped" action="index.php?tab=registro-huesped" method="POST">
                <input type="hidden" name="action" value="crear_huesped">
                <div class="form-group">
                    <label for="nombre">Nombre completo:</label>
                    <input type="text" id="nombre" name="nombre" required>
                </div>
                <div class="form-group">
                    <label for="correo">Correo electrónico:</label>
                    <input type="email" id="correo" name="correo" required>
                </div>
                <div class="form-group">
                    <label for="dni">DNI:</label>
                    <input type="text" id="dni" name="dni" required pattern="[0-9]{8}[A-Z]">
                </div>
                <button type="submit">Registrar Huésped</button>
            </form>
        </div>

        <!--Reserva de Habitación -->
        <div id="reserva-habitacion" class="form-section<?= $tab === 'reserva-habitacion' ? ' active' : '' ?>">
            <h2>Reserva de Habitación</h2>
            <form id="form-reserva" action="index.php?tab=reserva-habitacion" method="POST">
                <input type="hidden" name="action" value="crear_reserva">
                <div class="form-group">
                    <label for="correo_huesped">Correo del huésped:</label>
                    <input type="email" id="correo_huesped" name="correo_huesped" required>
                </div>
                <div class="form-group">
                    <label for="habitacion">Número de habitación:</label>
                    <select id="habitacion" name="habitacion" required>
                        <?php foreach ($habitaciones as $hab): ?>
                            <?php if ($hab['disponibilidad'] === 'Disponible'): ?>
                                <option value="<?= $hab['id_habitacion'] ?>">
                                    Habitación <?= $hab['numero_habitacion'] ?> - <?= $hab['tipo'] ?> - <?= $hab['precio_base'] ?>€
                                </option>
                            <?php endif; ?>
                        <?php endforeach; ?>
                    </select>
                </div>
                <div class="form-group">
                    <label for="fecha_llegada">Fecha de llegada:</label>
                    <input type="date" id="fecha_llegada" name="fecha_llegada" required>
                </div>
                <div class="form-group">
                    <label for="fecha_salida">Fecha de salida:</label>
                    <input type="date" id="fecha_salida" name="fecha_salida" required>
                </div>
                <button type="submit">Crear Reserva</button>
            </form>
        </div>

        <!-- Mantenimiento -->
        <div id="mantenimiento" class="form-section<?= $tab === 'mantenimiento' ? ' active' : '' ?>">
            <h2>Registro de Mantenimiento</h2>
            <form id="form-mantenimiento" action="index.php?tab=mantenimiento" method="POST">
                <input type="hidden" name="action" value="registrar_mantenimiento">
                <div class="form-group">
                    <label for="habitacion_mant">Número de habitación:</label>
                    <select id="habitacion_mant" name="habitacion_mant" required>
                        <?php foreach ($habitaciones as $hab): ?>
                <option value="<?= $hab['id_habitacion'] ?>">
                    Habitación <?= $hab['numero_habitacion'] ?> - <?= $hab['tipo'] ?> - <?= $hab['precio_base'] ?>€
                </option>
            <?php endforeach; ?>
                    </select>
                </div>
                <div class="form-group">
                    <label for="tarea">Tarea de mantenimiento:</label>
                    <input type="text" id="tarea" name="tarea" required>
                </div>
                <div class="form-group">
                    <label for="fecha_inicio">Fecha de inicio:</label>
                    <input type="date" id="fecha_inicio" name="fecha_inicio" required>
                </div>
                <div class="form-group">
                    <label for="fecha_fin">Fecha de fin estimada:</label>
                    <input type="date" id="fecha_fin" name="fecha_fin" required>
                </div>
                <div class="form-group">
                    <label for="descripcion">Descripción:</label>
                    <input type="text" id="descripcion" name="descripcion">
                </div>
                <button type="submit">Registrar Mantenimiento</button>
            </form>
        </div>

        <!-- Estado de Limpieza -->
        <div id="limpieza" class="form-section<?= $tab === 'limpieza' ? ' active' : '' ?>">
            <h2>Estado de Limpieza de Habitaciones</h2>
            <form id="form-limpieza" action="index.php?tab=limpieza" method="POST">
                <input type="hidden" name="action" value="actualizar_limpieza">
                <div class="form-group">
                    <label for="habitacion_limp">Número de habitación:</label>
                    <select id="habitacion_limp" name="habitacion_limp" required>
                        <?php foreach ($habitaciones as $hab): ?>
                <option value="<?= $hab['id_habitacion'] ?>">
                    Habitación <?= $hab['numero_habitacion'] ?> - <?= $hab['tipo'] ?> - <?= $hab['precio_base'] ?>€
                </option>
            <?php endforeach; ?>
                    </select>
                </div>
                <div class="form-group">
                    <label for="estado_limpieza">Estado:</label>
                    <select id="estado_limpieza" name="estado_limpieza" required>
                        <option value="Limpia">Limpia</option>
                        <option value="Sucia">Sucia</option>
                        <option value="En Limpieza">En Limpieza</option>
                    </select>
                </div>
                <button type="submit">Actualizar Estado</button>
            </form>
        </div>

        <!-- Ver Reservas -->
        <div id="ver-reservas" class="form-section<?= $tab === 'ver-reservas' ? ' active' : '' ?>">
    <h2>Reservas Actuales</h2>
    <?php
    $reservaService = new Reserva();
    if (isset($_SESSION['rol']) && strtolower($_SESSION['rol']) === 'admin') {
        $reservas = $reservaService->listarTodas();
    } else {
        // Mostrar solo reservas del correo vinculado al usuario
        $correo = $_SESSION['email'] ?? null;
        $reservas = [];
        if ($correo) {
            $pdo = getPDO();
            $stmt = $pdo->prepare("SELECT r.*, h.numero_habitacion, hu.nombre as nombre_huesped 
                                   FROM reservas r 
                                   JOIN habitaciones h ON r.id_habitacion = h.id_habitacion 
                                   JOIN huespedes hu ON r.id_huesped = hu.id_huesped
                                   WHERE hu.correo = :correo
                                   ORDER BY r.fecha_llegada DESC");
            $stmt->execute([':correo' => $correo]);
            $reservas = $stmt->fetchAll(PDO::FETCH_ASSOC);
        }
    }
    if ($reservas):
    ?>
        <table>
            <tr>
                <th>Habitación</th>
                <th>Huésped</th>
                <th>Llegada</th>
                <th>Salida</th>
                <th>Estado</th>
                <th>Precio (€)</th>
                <th>Acción</th>
            </tr>
            <?php foreach ($reservas as $res): ?>
    <tr>
        <td><?= $res['numero_habitacion'] ?></td>
        <td><?= $res['nombre_huesped'] ?></td>
        <td><?= $res['fecha_llegada'] ?></td>
        <td><?= $res['fecha_salida'] ?></td>
        <td><?= $res['estado'] ?></td>
        <td><?= number_format($res['precio_total'], 2) ?> €</td>
        <td>
            <?php if ($res['estado'] === 'Reservada' && isset($_SESSION['rol']) && strtolower($_SESSION['rol']) === 'admin'): ?>
                <form action="index.php?tab=ver-reservas" method="POST" style="display:inline;">
                    <input type="hidden" name="action" value="confirmar_reserva">
                    <input type="hidden" name="id_reserva" value="<?= $res['id_reserva'] ?>">
                    <button type="submit">Confirmar</button>
                </form>
                <form action="index.php?tab=ver-reservas" method="POST" style="display:inline;">
                    <input type="hidden" name="action" value="eliminar_reserva">
                    <input type="hidden" name="id_reserva" value="<?= $res['id_reserva'] ?>">
                    <button type="submit" onclick="return confirm('¿Seguro que deseas eliminar la reserva?')">Eliminar</button>
                </form>
            <?php endif; ?>
        </td>
    </tr>
<?php endforeach; ?>
        </table>
    <?php else: ?>
        <p>No hay reservas registradas.</p>
    <?php endif; ?>
</div>

        <!-- Ver Habitaciones -->
        <div id="ver-habitaciones" class="form-section<?= $tab === 'ver-habitaciones' ? ' active' : '' ?>">
    <h2>Listado de Habitaciones</h2>
    <table>
        <tr>
            <th>Nº Habitación</th>
            <th>Tipo</th>
            <th>Estado de Limpieza</th>
            <th>Mantenimiento</th>
        </tr>
        <?php foreach ($habitaciones as $hab): ?>
            <tr>
                <td><?= $hab['numero_habitacion'] ?></td>
                <td><?= $hab['tipo'] ?></td>
                <td>
                    <?php
                    // Obtener estado de limpieza
                    $limpieza = new Limpieza();
                    $estadoLimpieza = $limpieza->obtenerEstado($hab['id_habitacion']);
                    echo $estadoLimpieza && isset($estadoLimpieza['estado_limpieza'])
    ? htmlspecialchars($estadoLimpieza['estado_limpieza'])
    : 'Desconocido';
                    ?>
                </td>
                <td>
                    <?php
                    // Comprobar si hay mantenimiento activo
                    $mantenimiento = new Mantenimiento();
                    $tareas = $mantenimiento->listarPorHabitacion($hab['id_habitacion']);
                    $mostrarBoton = false;
foreach ($tareas as $tarea) {
    if (isset($tarea['estado']) && $tarea['estado'] !== 'No requerido') {
        $mostrarBoton = true;
        break;
    }
}
if ($mostrarBoton) {
    // Mostrar botón para ver detalles
    echo '<form method="POST" action="index.php?tab=ver-habitaciones" style="display:inline;">
            <input type="hidden" name="action" value="ver_detalles_mantenimiento">
            <input type="hidden" name="habitacion_id" value="' . $hab['id_habitacion'] . '">
            <button type="submit">Ver detalles</button>
          </form>';
} else {
    echo "No requerido";
}
                    ?>
                </td>
            </tr>
            <?php
            // Mostrar detalles de mantenimiento si se ha solicitado
            if (
                isset($_POST['action'], $_POST['habitacion_id']) &&
                $_POST['action'] === 'ver_detalles_mantenimiento' &&
                $_POST['habitacion_id'] == $hab['id_habitacion'] &&
                $tab === 'ver-habitaciones'
            ) {
                echo '<tr><td colspan="4">';
                foreach ($tareas as $tarea) {
                    echo "<strong>Tarea:</strong> " . htmlspecialchars($tarea['tarea_mantenimiento']) . "<br>";
                    echo "<strong>Descripción:</strong> " . htmlspecialchars($tarea['descripcion_tarea']) . "<br>";
                    echo "<strong>Fecha inicio:</strong> " . htmlspecialchars($tarea['fecha_inicio']) . "<br>";
                    echo "<strong>Fecha fin:</strong> " . htmlspecialchars($tarea['fecha_fin']) . "<br>";
                    echo "<strong>Estado:</strong> " . (isset($tarea['estado']) ? htmlspecialchars($tarea['estado']) : 'Pendiente') . "<br>";
                    // Mostrar boton solo si "No requerido"
                    if (isset($tarea['estado']) && $tarea['estado'] !== 'No requerido') {
                        echo '<form method="POST" action="index.php?tab=ver-habitaciones" style="display:inline;">
                                <input type="hidden" name="action" value="finalizar_mantenimiento">
                                <input type="hidden" name="id_mantenimiento" value="' . $tarea['id_mantenimiento'] . '">
                                <button type="submit">Finalizar</button>
                              </form>';
                    }
                    echo "<hr>";
                }
                echo '</td></tr>';
            }
            ?>
        <?php endforeach; ?>
    </table>
</div>
    </div>

           
    <!-- Queda poner las redes -->
    <footer class="main-footer">
    <div>
        <strong>Contacto:</strong> info@hotel.com | Tel: 123 456 789
    </div>
    <div>
        <strong>Redes:</strong>
        <a href="#">Instagram</a> |
        <a href="#">Facebook</a> |
        <a href="#">Twitter</a>
    </div>
    <div>
        &copy; <?= date('Y') ?> Sistema de Gestión Hotelera
    </div>
</footer>
</body>
</html>