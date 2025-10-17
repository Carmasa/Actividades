<?php
require_once __DIR__ . '/Conexion.php';

class Limpieza
{
    private $pdo;

    public function __construct()
    {
        $this->pdo = getPDO();
    }

    public function establecerEstado($id_habitacion, $estado)
    {
        // Intentamos actualizar si existe
        $sql = "SELECT COUNT(*) FROM limpieza WHERE id_habitacion = :id";
        $stmt = $this->pdo->prepare($sql);
        $stmt->execute([':id' => $id_habitacion]);
        $exists = $stmt->fetchColumn() > 0;

        if ($exists) {
            $sql = "UPDATE limpieza SET estado_limpieza = :estado WHERE id_habitacion = :id";
            $stmt = $this->pdo->prepare($sql);
            return $stmt->execute([':estado' => $estado, ':id' => $id_habitacion]);
        } else {
            $sql = "INSERT INTO limpieza (id_habitacion, estado_limpieza) VALUES (:id, :estado)";
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute([':id' => $id_habitacion, ':estado' => $estado]);
            return $this->pdo->lastInsertId();
        }
    }
public function actualizarEstado($idHabitacion, $estadoLimpieza) {
    $conn = getPDO();
    $stmt = $conn->prepare("UPDATE limpieza SET estado_limpieza = ? WHERE id_habitacion = ?");
    $stmt->execute([$estadoLimpieza, $idHabitacion]);
}
    public function obtenerEstado($id_habitacion)
    {
        $sql = "SELECT estado_limpieza FROM limpieza WHERE id_habitacion = :id";
        $stmt = $this->pdo->prepare($sql);
        $stmt->execute([':id' => $id_habitacion]);
        return $stmt->fetch(PDO::FETCH_ASSOC); 
    }
}


?>
