<?php
$host = 'localhost';
$db   = 'GestionHotel'; 
$user = 'root';
$pass = ''; 
$charset = 'utf8mb4'; 

$dsn = "mysql:host=$host;dbname=$db;charset=$charset";

// Opciones adicionales para PDO
$opciones = [
    // Lanza una excepción en caso de errores (modo más seguro)
    PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,

];

try {
    // El objeto $pdo ahora representa la conexión a la base de datos

    $pdo = new PDO($dsn, $user, $pass, $opciones);

} catch (PDOException $e) {
    die("Error de conexión a la base de datos: " . $e->getMessage());
}

// Devuelve la conexión PDO. Usar require_once 'Conexion.php' y luego getPDO().
function getPDO()
{
    global $pdo;
    return $pdo;
}

function crearHuesped($nombre, $correo, $dni)
{
    $pdo = getPDO();
    $sql = "INSERT INTO huespedes (nombre, correo, dni) VALUES (:nombre, :correo, :dni)";
    $stmt = $pdo->prepare($sql);
    $stmt->execute([':nombre' => $nombre, ':correo' => $correo, ':dni' => $dni]);
    return $pdo->lastInsertId();
}

function obtenerHuespedPorCorreo($correo)
{
    $pdo = getPDO();
    $sql = "SELECT * FROM huespedes WHERE correo = :correo";
    $stmt = $pdo->prepare($sql);
    $stmt->execute([':correo' => $correo]);
    return $stmt->fetch(PDO::FETCH_ASSOC);
}

function obtenerHabitacionPorNumero($numero)
{
    $pdo = getPDO();
    $sql = "SELECT * FROM habitaciones WHERE numero_habitacion = :numero";
    $stmt = $pdo->prepare($sql);
    $stmt->execute([':numero' => $numero]);
    return $stmt->fetch(PDO::FETCH_ASSOC);
}


?>