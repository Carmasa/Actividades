<?php
require_once 'Conexion.php';
require_once 'Habitacion.php';
require_once 'Huesped.php';
require_once 'Reserva.php';
require_once 'Mantenimiento.php';
require_once 'Limpieza.php';

header('Content-Type: application/json');

function sendResponse($success, $message, $data = null) {
    echo json_encode([
        'success' => $success,
        'message' => $message,
        'data' => $data
    ]);
    exit;
}

// Clase para sustituir la seccion del index.php que procesa formularios
// no llego a implementarse
$action = $_REQUEST['action'] ?? '';

try {
    switch ($action) {
        case 'crear_huesped':
            if ($_SERVER['REQUEST_METHOD'] === 'POST') {
                $huesped = new Huesped();
                try {
                    $id = $huesped->crear(
                        $_POST['nombre'],
                        $_POST['correo'],
                        $_POST['dni']
                    );
                    sendResponse(true, "Huésped registrado con éxito", ['id' => $id]);
                } catch (PDOException $e) {
                    if ($e->getCode() == 23000) {
                        // Error de clave única duplicada
                        if (strpos($e->getMessage(), 'correo') !== false) {
                            sendResponse(false, "El correo electrónico ya existe");
                        } elseif (strpos($e->getMessage(), 'dni') !== false) {
                            sendResponse(false, "El DNI ya existe");
                        } else {
                            sendResponse(false, "El huésped ya existe");
                        }
                    } else {
                        sendResponse(false, "Error: " . $e->getMessage());
                    }
                }
            }
            break;

        case 'crear_reserva':
            if ($_SERVER['REQUEST_METHOD'] === 'POST') {
                $reserva = new Reserva();
                $huesped = new Huesped();
                
                // Obtener el huésped por correo
                $huespedData = $huesped->obtenerPorCorreo($_POST['correo_huesped']);
                if (!$huespedData) {
                    sendResponse(false, "Huésped no encontrado");
                }

                $id = $reserva->crearReserva(
                    $huespedData['id_huesped'],
                    $_POST['habitacion'],
                    $_POST['fecha_llegada'],
                    $_POST['fecha_salida']
                );
                
                if ($id) {
                    $reserva->confirmarReserva($id);
                    sendResponse(true, "Reserva creada y confirmada con éxito");
                } else {
                    sendResponse(false, "No se pudo crear la reserva");
                }
            }
            break;

        case 'registrar_mantenimiento':
            if ($_SERVER['REQUEST_METHOD'] === 'POST') {
                $mantenimiento = new Mantenimiento();
                $id = $mantenimiento->registrarTarea(
                    $_POST['habitacion_mant'],
                    $_POST['tarea'],
                    $_POST['fecha_inicio'],
                    $_POST['fecha_fin'],
                    $_POST['descripcion']
                );
                sendResponse(true, "Tarea de mantenimiento registrada con éxito");
            }
            break;

        case 'actualizar_limpieza':
            if ($_SERVER['REQUEST_METHOD'] === 'POST') {
                $limpieza = new Limpieza();
                $limpieza->actualizarEstado(
                    $_POST['habitacion_limp'],
                    $_POST['estado_limpieza']
                );
                sendResponse(true, "Estado de limpieza actualizado con éxito");
            }
            break;

        case 'obtener_habitaciones':
            $tipo = $_GET['tipo'] ?? 'Disponible';
            $pdo = getPDO();
            if ($tipo === 'Disponible') {
                $stmt = $pdo->prepare("SELECT * FROM habitaciones WHERE disponibilidad = 'Disponible'");
                $stmt->execute();
            } else {
                $stmt = $pdo->prepare("SELECT * FROM habitaciones WHERE tipo = :tipo AND disponibilidad = 'Disponible'");
                $stmt->execute([':tipo' => $tipo]);
            }
            $habitaciones = $stmt->fetchAll(PDO::FETCH_ASSOC);
            foreach ($habitaciones as $hab) {
                echo "<option value='{$hab['id_habitacion']}'>Habitación {$hab['numero_habitacion']} - {$hab['tipo']} - {$hab['precio_base']}€</option>";
            }
            exit;

        case 'listar_reservas':
            $reserva = new Reserva();
            $reservas = $reserva->listarTodas();
            if (empty($reservas)) {
                echo "<p>No hay reservas registradas.</p>";
                exit;
            }
            
            echo "<table>";
            echo "<tr><th>Habitación</th><th>Huésped</th><th>Llegada</th><th>Salida</th><th>Estado</th></tr>";
            foreach ($reservas as $res) {
                echo "<tr>";
                echo "<td>{$res['numero_habitacion']}</td>";
                echo "<td>{$res['nombre_huesped']}</td>";
                echo "<td>{$res['fecha_llegada']}</td>";
                echo "<td>{$res['fecha_salida']}</td>";
                echo "<td>{$res['estado']}</td>";
                echo "</tr>";
            }
            echo "</table>";
            exit;

        default:
            sendResponse(false, "Acción no válida");
    }
} catch (Exception $e) {
    sendResponse(false, "Error: " . $e->getMessage());
}
?>
<?php
$pdo->prepare("UPDATE habitaciones SET disponibilidad = 'No disponible' WHERE id_habitacion = :id")->execute([':id' => $id_habitacion]);
?>