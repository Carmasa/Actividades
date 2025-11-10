<?php
require_once __DIR__ . '/Conexion.php';
session_start();

$mensaje = '';

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $nombre = trim($_POST['nombre'] ?? '');
    $correo = trim($_POST['correo'] ?? '');
    $dni = trim($_POST['dni'] ?? '');
    $password = trim($_POST['password'] ?? '');
    $confirm_password = trim($_POST['confirm_password'] ?? '');

    // Validaciones
    if ($password !== $confirm_password) {
        $mensaje = "Las contraseñas no coinciden";
    } else {
        $pdo = getPDO();
        
        // Verificar si existe el correo o DNI
        $stmt = $pdo->prepare("SELECT COUNT(*) FROM huespedes WHERE correo = :correo OR dni = :dni");
        $stmt->execute([':correo' => $correo, ':dni' => $dni]);
        $existe = $stmt->fetchColumn();

        if ($existe) {
            $mensaje = "Ya existe un usuario con ese correo o DNI";
        } else {
            try {
                $stmt = $pdo->prepare("INSERT INTO huespedes (nombre, correo, dni, pass) VALUES (:nombre, :correo, :dni, :pass)");
                $stmt->execute([
                    ':nombre' => $nombre,
                    ':correo' => $correo,
                    ':dni' => $dni,
                    ':pass' => $password
                ]);
                
                header("Location: Login.php?registro=exitoso");
                exit;
            } catch (PDOException $e) {
                $mensaje = "Error al registrar: " . $e->getMessage();
            }
        }
    }
}
?>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="utf-8">
    <title>Registro de Usuario</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <header class="main-header">
        <img src="img/encabezado.jpg" alt="Encabezado" class="header-img">
        <h1 class="header-title">Sistema de Gestión Hotelera</h1>
    </header>

    <div class="container container-login">
        <div class="card card-login">
            <h3>Registro de Usuario</h3>
            <?php if ($mensaje): ?>
                <div class="error"><?php echo htmlspecialchars($mensaje); ?></div>
            <?php endif; ?>
            <form method="POST" action="registro.php">
                <label for="nombre">Nombre</label>
                <input type="text" id="nombre" name="nombre" required>
                
                <label for="correo">Correo Electrónico</label>
                <input type="email" id="correo" name="correo" required>
                
                <label for="dni">DNI</label>
                <input type="text" id="dni" name="dni" pattern="[0-9]{8}[A-Za-z]{1}" 
                title="Formato DNI: 8 números y 1 letra" required>
                
                <label for="password">Contraseña</label>
                <input type="password" id="password" name="password" required>
                
                <label for="confirm_password">Confirmar Contraseña</label>
                <input type="password" id="confirm_password" name="confirm_password" required>
                
                <button type="submit">Registrarse</button>
            </form>
            <div style="text-align: center; margin-top: 15px;">
                <a href="Login.php" class="back-btn">Volver al Login</a>
            </div>
        </div>
    </div>

    <footer class="main-footer">
        <div>
            <strong>Contacto:</strong> info@hotel.com | Tel: 123 456 789
        </div>
        <div>
            &copy; <?php echo date('Y'); ?> Sistema de Gestión Hotelera
        </div>
    </footer>
</body>
</html>