<?php
class Usuario {
    private $conn;
    private $table_name = "usuarios";

    public $id;
    public $nombre_usuario;
    public $email;
    public $contrasena;
    public $rol;
    public $fecha_creacion;

    public function __construct($db) {
        $this->conn = $db;
    }

    // Crear nuevo usuario
    public function crear() {
        $query = "INSERT INTO " . $this->table_name . " SET nombre_usuario=:nombre, email=:email, contrasena=:pass, rol=:rol";
        $stmt = $this->conn->prepare($query);

        $this->nombre_usuario = htmlspecialchars(strip_tags($this->nombre_usuario));
        $this->email = htmlspecialchars(strip_tags($this->email));
        $this->contrasena = htmlspecialchars(strip_tags($this->contrasena));
        $this->rol = htmlspecialchars(strip_tags($this->rol));

        // Hash de contraseña
        $password_hash = password_hash($this->contrasena, PASSWORD_BCRYPT);

        $stmt->bindParam(":nombre", $this->nombre_usuario);
        $stmt->bindParam(":email", $this->email);
        $stmt->bindParam(":pass", $password_hash);
        $stmt->bindParam(":rol", $this->rol);

        if($stmt->execute()) {
            return true;
        }
        return false;
    }

    // Iniciar sesión
    public function login($nombre_usuario, $contrasena) {
        $query = "SELECT id, nombre_usuario, contrasena, rol FROM " . $this->table_name . " WHERE nombre_usuario = ? LIMIT 0,1";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $nombre_usuario);
        $stmt->execute();

        if($stmt->rowCount() > 0) {
            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            if(password_verify($contrasena, $row['contrasena'])) {
                $this->id = $row['id'];
                $this->nombre_usuario = $row['nombre_usuario'];
                $this->rol = $row['rol'];
                return true;
            }
        }
        return false;
    }

    // Buscar por ID
    public function buscarPorId($id) {
        $query = "SELECT * FROM " . $this->table_name . " WHERE id = ? LIMIT 0,1";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $id);
        $stmt->execute();
        return $stmt->fetch(PDO::FETCH_ASSOC);
    }

    // Obtener todos los usuarios
    public function obtenerTodos() {
        $query = "SELECT * FROM " . $this->table_name . " ORDER BY fecha_creacion DESC";
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    }
    
    // Verificar si el usuario existe
    public function existeUsuario($nombre_usuario) {
        $query = "SELECT id FROM " . $this->table_name . " WHERE nombre_usuario = ? LIMIT 0,1";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $nombre_usuario);
        $stmt->execute();
        return $stmt->rowCount() > 0;
    }

    // Verificar si el email existe
    public function existeEmail($email) {
        $query = "SELECT id FROM " . $this->table_name . " WHERE email = ? LIMIT 0,1";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $email);
        $stmt->execute();
        return $stmt->rowCount() > 0;
    }
}
?>
