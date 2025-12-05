<?php
include_once 'PalabraProhibida.php';

class Post {
    private $conn;
    private $table_name = "posts";

    public $id;
    public $usuario_id;
    public $titulo;
    public $slug;
    public $contenido;
    public $imagen;
    public $marcado;
    public $fecha_creacion;

    public function __construct($db) {
        $this->conn = $db;
    }

    // Crear publicación
    public function crear() {
        $query = "INSERT INTO " . $this->table_name . " SET usuario_id=:uid, titulo=:titulo, slug=:slug, contenido=:contenido, imagen=:imagen, marcado=:marcado";
        $stmt = $this->conn->prepare($query);

        $this->titulo = htmlspecialchars(strip_tags($this->titulo));
        $this->slug = $this->generarSlug($this->titulo);
        $this->contenido = htmlspecialchars(strip_tags($this->contenido));
        $this->imagen = htmlspecialchars(strip_tags($this->imagen));
        
        // Moderación
        $this->marcado = $this->contienePalabrasProhibidas($this->titulo . ' ' . $this->contenido) ? 1 : 0;

        $stmt->bindParam(":uid", $this->usuario_id);
        $stmt->bindParam(":titulo", $this->titulo);
        $stmt->bindParam(":slug", $this->slug);
        $stmt->bindParam(":contenido", $this->contenido);
        $stmt->bindParam(":imagen", $this->imagen);
        $stmt->bindParam(":marcado", $this->marcado);

        if($stmt->execute()) {
            $this->id = $this->conn->lastInsertId();
            return true;
        }
        return false;
    }
    
    // Actualizar publicación
    public function actualizar() {
        $query = "UPDATE " . $this->table_name . " SET titulo=:titulo, contenido=:contenido, imagen=:imagen, marcado=:marcado WHERE id=:id";
        $stmt = $this->conn->prepare($query);

        $this->titulo = htmlspecialchars(strip_tags($this->titulo));
        $this->contenido = htmlspecialchars(strip_tags($this->contenido));
        $this->imagen = htmlspecialchars(strip_tags($this->imagen));
        
        // Moderación
        $this->marcado = $this->contienePalabrasProhibidas($this->titulo . ' ' . $this->contenido) ? 1 : 0;

        $stmt->bindParam(":titulo", $this->titulo);
        $stmt->bindParam(":contenido", $this->contenido);
        $stmt->bindParam(":imagen", $this->imagen);
        $stmt->bindParam(":marcado", $this->marcado);
        $stmt->bindParam(":id", $this->id);

        if($stmt->execute()) {
            return true;
        }
        return false;
    }

    // Obtener todas las publicaciones (con filtro opcional de usuario)
    public function obtenerTodos($limite = 10, $offset = 0, $usuario_id = null) {
        $query = "SELECT p.*, u.nombre_usuario FROM " . $this->table_name . " p 
                  LEFT JOIN usuarios u ON p.usuario_id = u.id ";
        
        if($usuario_id) {
            $query .= " WHERE p.usuario_id = :uid ";
        }
        
        $query .= " ORDER BY p.fecha_creacion DESC LIMIT :limite OFFSET :offset";
        
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":limite", $limite, PDO::PARAM_INT);
        $stmt->bindParam(":offset", $offset, PDO::PARAM_INT);
        
        if($usuario_id) {
            $stmt->bindParam(":uid", $usuario_id, PDO::PARAM_INT);
        }
        
        $stmt->execute();
        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    }

    // Obtener por usuario
    public function obtenerPorUsuario($usuario_id) {
        $query = "SELECT * FROM " . $this->table_name . " WHERE usuario_id = ? ORDER BY fecha_creacion DESC";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $usuario_id);
        $stmt->execute();
        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    }
    
    // Obtener por ID
    public function obtenerPorId($id) {
        $query = "SELECT * FROM " . $this->table_name . " WHERE id = ? LIMIT 0,1";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $id);
        $stmt->execute();
        return $stmt->fetch(PDO::FETCH_ASSOC);
    }

    // Obtener por Slug
    public function obtenerPorSlug($slug) {
        $query = "SELECT p.*, u.nombre_usuario FROM " . $this->table_name . " p 
                  LEFT JOIN usuarios u ON p.usuario_id = u.id 
                  WHERE p.slug = ? LIMIT 0,1";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $slug);
        $stmt->execute();
        return $stmt->fetch(PDO::FETCH_ASSOC);
    }
    
    // Eliminar publicación
    public function eliminar($id) {
        $query = "DELETE FROM " . $this->table_name . " WHERE id = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $id);
        if($stmt->execute()) {
            return true;
        }
        return false;
    }

    // Generar Slug 
    private function generarSlug($titulo) {
        $slug = strtolower(trim(preg_replace('/[^A-Za-z0-9-]+/', '-', $titulo)));
        $query = "SELECT id FROM " . $this->table_name . " WHERE slug = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $slug);
        $stmt->execute();
        
        if($stmt->rowCount() > 0) {
            $slug .= '-' . time();
        }
        return $slug;
    }
    
    // Verificar palabras prohibidas
    private function contienePalabrasProhibidas($texto) {
        $palabraProhibida = new PalabraProhibida($this->conn);
        $palabras = $palabraProhibida->obtenerTodas();
        
        foreach($palabras as $palabra) {
            if(stripos($texto, $palabra) !== false) {
                return true;
            }
        }
        return false;
    }
}
?>