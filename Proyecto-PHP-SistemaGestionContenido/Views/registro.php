<?php
session_start();
include_once '../models/Conexion.php';
include_once '../models/Usuario.php';

$message = "";

if($_POST){
    $database = new Database();
    $db = $database->getConnection();
    $usuario = new Usuario($db);

    $usuario->nombre_usuario = $_POST['username'];
    $usuario->email = $_POST['email'];
    $usuario->contrasena = $_POST['password'];
    $usuario->rol = 'user'; 

    if($usuario->existeUsuario($usuario->nombre_usuario)){
        $message = "El nombre de usuario ya existe.";
    } elseif($usuario->existeEmail($usuario->email)){
        $message = "El correo electrónico ya está registrado.";
    } else {
        if($usuario->crear()){
            $message = "Usuario registrado exitosamente. <a href='Login.php'>Iniciar Sesión</a>";
        } else {
            $message = "No se pudo registrar el usuario.";
        }
    }
}
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro - Blog Personal</title>
    <link rel="stylesheet" href="../css/styles.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <div class="auth-container">
        <h2>Registro</h2>
        <?php if($message != ""): ?>
            <div class="alert"><?php echo $message; ?></div>
        <?php endif; ?>
        <form action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" method="post">
            <div class="form-group">
                <label for="username">Usuario:</label>
                <input type="text" name="username" id="username" required>
            </div>
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" name="email" id="email" required>
            </div>
            <div class="form-group">
                <label for="password">Contraseña:</label>
                <input type="password" name="password" id="password" required>
            </div>
            <button type="submit" class="btn">Registrarse</button>
        </form>
        <p>¿Ya tienes cuenta? <a href="Login.php">Inicia Sesión</a></p>
    </div>
    <script src="../theme-toggle.js"></script>
</body>
</html>