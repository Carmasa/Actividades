<?php
session_start();
include_once '../models/Conexion.php';
include_once '../models/Usuario.php';

$database = new Database();
$db = $database->getConnection();
$usuario = new Usuario($db);

$message = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $nombre_usuario = $_POST['username'];
    $contrasena = $_POST['password'];

    if($usuario->login($nombre_usuario, $contrasena)){
        $_SESSION['user_id'] = $usuario->id;
        $_SESSION['username'] = $usuario->nombre_usuario;
        $_SESSION['role'] = $usuario->rol;
        
        header("Location: ../index.php");
        exit();
    } else {
        $message = "Credenciales incorrectas.";
    }
}
?>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Blog CMS</title>
    <link rel="stylesheet" href="../css/styles.css">
</head>
<body>
    <div class="auth-container">
        <h2>Iniciar Sesión</h2>
        <?php if($message != ""): ?>
            <div class="alert"><?php echo $message; ?></div>
        <?php endif; ?>
        <form action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" method="post">
            <div class="form-group">
                <label for="username">Usuario:</label>
                <input type="text" name="username" id="username" required>
            </div>
            <div class="form-group">
                <label for="password">Contraseña:</label>
                <input type="password" name="password" id="password" required>
            </div>
            <button type="submit" class="btn">Entrar</button>
        </form>
        <p>¿No tienes cuenta? <a href="registro.php">Regístrate</a></p>
    </div>
    <script src="../theme-toggle.js"></script>
</body>
</html>