<?php
require_once __DIR__ . '/Conexion.php';
session_start();

if (isset($_GET['logout'])) {
    session_unset();
    session_destroy();
    header('Location: Login.php');
    exit;
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $usuarioIngresado = trim($_POST['usuario'] ?? '');
    $passwordIngresada = $_POST['password'] ?? '';

    $pdo = getPDO();
    $userRow = null;

    $sql = "SELECT id_huesped, nombre, correo, pass FROM huespedes WHERE nombre = :usuario LIMIT 1";
    try {
        $stmt = $pdo->prepare($sql);
        $stmt->execute([':usuario' => $usuarioIngresado]);
        $userRow = $stmt->fetch(PDO::FETCH_ASSOC);
    } catch (PDOException $e) {
        $userRow = null;
    }

    if (!$userRow) {
        $mensajeError = "Usuario no encontrado.";
    } else {
        $stored = $userRow['pass'] ?? null;
        $correoUsuario = $userRow['correo'] ?? null;

        $ok = false;
        if ($stored !== null) {
            if (strpos($stored, '$2y$') === 0 || strpos($stored, '$2a$') === 0) {
                $ok = password_verify($passwordIngresada, $stored);
            } else {
                $ok = hash_equals((string)$stored, (string)$passwordIngresada);
            }
        }

        if ($ok) {
            $_SESSION['autenticado'] = true;
            $_SESSION['usuario'] = $usuarioIngresado;
            $_SESSION['rol'] = ($usuarioIngresado === 'admin') ? 'admin' : 'user';
            if ($correoUsuario) $_SESSION['email'] = $correoUsuario;
            header("Location: index.php");
            exit;
        } else {
            $mensajeError = "Credenciales incorrectas. Intente de nuevo.";
        }
    }
}
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="utf-8">
    <title>Iniciar Sesión</title>
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
            <h3>Login de Usuario</h3>
            <?php if (isset($mensajeError)): ?>
                <div class="error"><?php echo htmlspecialchars($mensajeError); ?></div>
            <?php endif; ?>
            <form method="POST" action="Login.php">
                <label for="usuario">Usuario</label>
                <input type="text" id="usuario" name="usuario" required autofocus>
                <label for="password">Contraseña</label>
                <input type="password" id="password" name="password" required>
                <button type="submit">Entrar</button>
            </form>
            <div style="text-align: center; margin-top: 15px;">
                <a href="registro.php" class="register-btn">Registrarse</a>
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