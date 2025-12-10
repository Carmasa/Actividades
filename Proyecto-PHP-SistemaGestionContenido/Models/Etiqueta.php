<?php
class Etiqueta {
    private $conn;
    private $table_name = "etiquetas";
    private $pivot_table = "post_etiquetas";

    public $id;
    public $nombre;

    public function __construct($db) {
        $this->conn = $db;
    }

    // Obtener o crear etiqueta
    public function obtenerOCrear($nombre) {
        $nombre = strtolower(trim($nombre));
        
        // Verificar si existe
        $query = "SELECT id FROM " . $this->table_name . " WHERE nombre = ? LIMIT 0,1";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $nombre);
        $stmt->execute();

        if($stmt->rowCount() > 0) {
            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            return $row['id'];
        }

        // Crear
        $query = "INSERT INTO " . $this->table_name . " SET nombre = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $nombre);
        if($stmt->execute()) {
            return $this->conn->lastInsertId();
        }
        return false;
    }

    // Asociar con publicación
    public function asociarConPost($post_id, $etiqueta_id) {
        $query = "INSERT IGNORE INTO " . $this->pivot_table . " SET post_id = ?, etiqueta_id = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $post_id);
        $stmt->bindParam(2, $etiqueta_id);
        return $stmt->execute();
    }

    // Obtener por publicación
    public function obtenerPorPost($post_id) {
        $query = "SELECT t.* FROM " . $this->table_name . " t 
                  JOIN " . $this->pivot_table . " pt ON t.id = pt.etiqueta_id 
                  WHERE pt.post_id = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $post_id);
        $stmt->execute();
        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    }
    
    // Obtener publicaciones por etiqueta
    public function obtenerPostsPorEtiqueta($nombre_etiqueta) {
        $query = "SELECT p.*, u.nombre_usuario FROM posts p 
                  JOIN post_etiquetas pt ON p.id = pt.post_id 
                  JOIN etiquetas t ON pt.etiqueta_id = t.id 
                  LEFT JOIN usuarios u ON p.usuario_id = u.id
                  WHERE t.nombre = ? ORDER BY p.fecha_creacion DESC";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $nombre_etiqueta);
        $stmt->execute();
        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    }
}
?>
