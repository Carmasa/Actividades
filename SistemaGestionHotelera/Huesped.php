<?php
require_once __DIR__ . '/Conexion.php';

class Huesped
{
    private $pdo;

    public function __construct()
    {
        $this->pdo = getPDO();
    }

    public function crear($nombre, $correo, $dni)
    {
        $sql = "INSERT INTO huespedes (nombre, correo, dni) VALUES (:nombre, :correo, :dni)";
        $stmt = $this->pdo->prepare($sql);
        $stmt->execute([':nombre' => $nombre, ':correo' => $correo, ':dni' => $dni]);
        return $this->pdo->lastInsertId();
    }

    public function obtenerPorId($id)
    {
        $sql = "SELECT * FROM huespedes WHERE id_huesped = :id";
        $stmt = $this->pdo->prepare($sql);
        $stmt->execute([':id' => $id]);
        return $stmt->fetch(PDO::FETCH_ASSOC);
    }

    public function obtenerPorCorreo($correo)
    {
        return obtenerHuespedPorCorreo($correo);
    }
}

?>
