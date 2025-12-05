<?php
class PalabraProhibida {
    private $conn;
    private $table_name = "palabras_prohibidas";

    public $id;
    public $palabra;

    public function __construct($db) {
        $this->conn = $db;
    }

    // Obtener todas las palabras prohibidas (solo array de strings)
    public function obtenerTodas() {
        $query = "SELECT palabra FROM " . $this->table_name;
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt->fetchAll(PDO::FETCH_COLUMN);
    }

    // Obtener todas las palabras prohibidas con ID (para gestión)
    public function obtenerTodasConId() {
        $query = "SELECT * FROM " . $this->table_name . " ORDER BY palabra ASC";
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    }

    // Agregar palabra prohibida
    public function crear($palabra) {
        $query = "INSERT INTO " . $this->table_name . " SET palabra = :palabra";
        $stmt = $this->conn->prepare($query);
        
        $palabra = htmlspecialchars(strip_tags($palabra));
        $stmt->bindParam(":palabra", $palabra);

        if($stmt->execute()) {
            return true;
        }
        return false;
    }

    // Eliminar palabra prohibida
    public function eliminar($id) {
        $query = "DELETE FROM " . $this->table_name . " WHERE id = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $id);

        if($stmt->execute()) {
            return true;
        }
        return false;
    }
}
?>
