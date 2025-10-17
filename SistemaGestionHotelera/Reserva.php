<?php
require_once __DIR__ . '/Conexion.php';

class Reserva
{
    private $pdo;

    public function __construct()
    {
        $this->pdo = getPDO();
    }

    // Comprueba si hay reservas confirmadas en un rango de fechas para no dar la misma habitacion
    private function hayReservaConfirmadaSolapada($id_habitacion, $fecha_llegada, $fecha_salida)
    {
        $sql = "SELECT COUNT(*) FROM reservas WHERE id_habitacion = :id_habitacion AND estado = 'Confirmada' AND NOT (fecha_salida <= :fecha_llegada OR fecha_llegada >= :fecha_salida)";
        $stmt = $this->pdo->prepare($sql);
        $stmt->execute([
            ':id_habitacion' => $id_habitacion,
            ':fecha_llegada' => $fecha_llegada,
            ':fecha_salida' => $fecha_salida
        ]);
        return $stmt->fetchColumn() > 0;
    }

    // Comprueba si hay tareas de mantenimiento para no asociar reservas
    private function hayMantenimientoActivo($id_habitacion, $fecha_llegada, $fecha_salida)
    {
        $sql = "SELECT COUNT(*) FROM mantenimiento WHERE id_habitacion = :id_habitacion AND NOT (fecha_fin < :fecha_llegada OR fecha_inicio > :fecha_salida)";
        $stmt = $this->pdo->prepare($sql);
        $stmt->execute([
            ':id_habitacion' => $id_habitacion,
            ':fecha_llegada' => $fecha_llegada,
            ':fecha_salida' => $fecha_salida
        ]);
        return $stmt->fetchColumn() > 0;
    }

    // Calcula precio_total noches * precio_base
    private function calcularPrecioTotal($id_habitacion, $fecha_llegada, $fecha_salida)
    {
        $sql = "SELECT precio_base FROM habitaciones WHERE id_habitacion = :id";
        $stmt = $this->pdo->prepare($sql);
        $stmt->execute([':id' => $id_habitacion]);
        $precio_base = $stmt->fetchColumn();

        $d1 = new DateTime($fecha_llegada);
        $d2 = new DateTime($fecha_salida);
        $noches = $d2->diff($d1)->days;
        if ($noches <= 0) {
            throw new Exception('La fecha de salida debe ser posterior a la fecha de llegada.');
        }
        return $noches * $precio_base;
    }

    // Crea una reserva en estado 'Pendiente'. No confirma ni bloquea la habitación hasta confirmar.
    public function crearReserva($id_huesped, $id_habitacion, $fecha_llegada, $fecha_salida)
    {
        // Evita la reserva si hay manenimiento asignado
        if ($this->hayMantenimientoActivo($id_habitacion, $fecha_llegada, $fecha_salida)) {
            throw new Exception('La habitación tiene una tarea de mantenimiento que se solapa con las fechas.');
        }

        if ($this->hayReservaSolapada($id_habitacion, $fecha_llegada, $fecha_salida)) {
            throw new Exception("La habitación ya está reservada en esas fechas.");
        }

        $precio_total = $this->calcularPrecioTotal($id_habitacion, $fecha_llegada, $fecha_salida);

        $sql = "INSERT INTO reservas (id_huesped, id_habitacion, fecha_llegada, fecha_salida, precio_total, estado) VALUES (:id_huesped, :id_habitacion, :fecha_llegada, :fecha_salida, :precio_total, 'Reservada')";
        $stmt = $this->pdo->prepare($sql);
        $stmt->execute([
            ':id_huesped' => $id_huesped,
            ':id_habitacion' => $id_habitacion,
            ':fecha_llegada' => $fecha_llegada,
            ':fecha_salida' => $fecha_salida,
            ':precio_total' => $precio_total
        ]);
        return $this->pdo->lastInsertId();
    }

    // Confirma una reserva (cambia de pendiente a confirmada)
    public function confirmarReserva($id_reserva)
    {
        // Obtener reserva
        $sql = "SELECT * FROM reservas WHERE id_reserva = :id";
        $stmt = $this->pdo->prepare($sql);
        $stmt->execute([':id' => $id_reserva]);
        $res = $stmt->fetch(PDO::FETCH_ASSOC);
        if (!$res) {
            throw new Exception('Reserva no encontrada');
        }

        $id_habitacion = $res['id_habitacion'];
        $fecha_llegada = $res['fecha_llegada'];
        $fecha_salida = $res['fecha_salida'];

        if ($this->hayMantenimientoActivo($id_habitacion, $fecha_llegada, $fecha_salida)) {
            throw new Exception('No se puede confirmar: mantenimiento activo en las fechas.');
        }

        if ($this->hayReservaConfirmadaSolapada($id_habitacion, $fecha_llegada, $fecha_salida)) {
            throw new Exception('No se puede confirmar: existe otra reserva confirmada solapada.');
        }

        $sql = "UPDATE reservas SET estado = 'Confirmada', fecha_reserva = NOW() WHERE id_reserva = :id";
        $stmt = $this->pdo->prepare($sql);
        $stmt->execute([':id' => $id_reserva]);
        return true;
    }

    public function cancelarReserva($id_reserva)
    {
        $sql = "UPDATE reservas SET estado = 'Cancelada' WHERE id_reserva = :id";
        $stmt = $this->pdo->prepare($sql);
        return $stmt->execute([':id' => $id_reserva]);
    }

    public function listarReservasPorHabitacion($id_habitacion)
    {
        $sql = "SELECT * FROM reservas WHERE id_habitacion = :id";
        $stmt = $this->pdo->prepare($sql);
        $stmt->execute([':id' => $id_habitacion]);
        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    }

    public function listarTodas()
    {
        $sql = "SELECT r.*, h.numero_habitacion, hu.nombre as nombre_huesped 
                FROM reservas r 
                JOIN habitaciones h ON r.id_habitacion = h.id_habitacion 
                JOIN huespedes hu ON r.id_huesped = hu.id_huesped 
                ORDER BY r.fecha_llegada DESC";
        return $this->pdo->query($sql)->fetchAll(PDO::FETCH_ASSOC);
    }

    private function hayReservaSolapada($id_habitacion, $fecha_llegada, $fecha_salida) {
        $sql = "SELECT COUNT(*) FROM reservas 
                WHERE id_habitacion = :id_habitacion 
                AND estado IN ('Reservada', 'Confirmada')
                AND (
                    (fecha_llegada <= :fecha_salida AND fecha_salida >= :fecha_llegada)
                )";
        $stmt = $this->pdo->prepare($sql);
        $stmt->execute([
            ':id_habitacion' => $id_habitacion,
            ':fecha_llegada' => $fecha_llegada,
            ':fecha_salida' => $fecha_salida
        ]);
        return $stmt->fetchColumn() > 0;
    }
}

?>