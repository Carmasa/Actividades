<?php
require_once __DIR__ . '/Conexion.php';

class Habitacion
{
    private $pdo;

    public function __construct()
    {
        $this->pdo = getPDO();
    }

    //Método para crear una nueva habitación (No se usa actualemtente)
    public function crear($numero, $tipo, $precio_base)
    {
        $sql = "INSERT INTO habitaciones (numero_habitacion, tipo, precio_base) VALUES (:numero, :tipo, :precio)";
        $stmt = $this->pdo->prepare($sql);
        $stmt->execute([':numero' => $numero, ':tipo' => $tipo, ':precio' => $precio_base]);
        return $this->pdo->lastInsertId();
    }

    public function obtenerPorId($id)
    {
        $sql = "SELECT * FROM habitaciones WHERE id_habitacion = :id";
        $stmt = $this->pdo->prepare($sql);
        $stmt->execute([':id' => $id]);
        return $stmt->fetch(PDO::FETCH_ASSOC);
    }

    public function obtenerPorNumero($numero)
    {
        return obtenerHabitacionPorNumero($numero);
    }

    public function listarTodas()
    {
        $sql = "SELECT * FROM habitaciones";
        return $this->pdo->query($sql)->fetchAll(PDO::FETCH_ASSOC);
    }
}

?>