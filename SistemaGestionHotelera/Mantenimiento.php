<?php
require_once __DIR__ . '/Conexion.php';

class Mantenimiento
{
    private $pdo;

    public function __construct()
    {
        $this->pdo = getPDO();
    }

    public function registrarTarea($id_habitacion, $tarea_mantenimiento, $fecha_inicio, $fecha_fin, $descripcion = null)
    {
        $sql = "INSERT INTO mantenimiento (id_habitacion, tarea_mantenimiento, fecha_inicio, fecha_fin, descripcion_tarea) VALUES (:id_habitacion, :tarea, :inicio, :fin, :descripcion)";
        $stmt = $this->pdo->prepare($sql);
        $stmt->execute([
            ':id_habitacion' => $id_habitacion,
            ':tarea' => $tarea_mantenimiento,
            ':inicio' => $fecha_inicio,
            ':fin' => $fecha_fin,
            ':descripcion' => $descripcion
        ]);
        return $this->pdo->lastInsertId();
    }

    public function listarPorHabitacion($id_habitacion)
    {
        $sql = "SELECT * FROM mantenimiento WHERE id_habitacion = :id";
        $stmt = $this->pdo->prepare($sql);
        $stmt->execute([':id' => $id_habitacion]);
        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    }

    public function hayMantenimientoActivo($id_habitacion, $fecha_llegada, $fecha_salida)
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

    public function finalizarTarea($id_mantenimiento) {
        $sql = "UPDATE mantenimiento SET estado = 'No requerido' WHERE id_mantenimiento = :id";
        $stmt = $this->pdo->prepare($sql);
        $stmt->execute([':id' => $id_mantenimiento]);
    }
}

?>
