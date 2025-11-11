<?php
// Iniciar sesión para usar $_SESSION
session_start();

// --- Manejo de cookies ---
if (isset($_POST['crear_cookie'])) {
    setcookie("mi_cookie", "Hola, soy una cookie", time() + 3600); // Dura 1 hora
    $mensaje_cookie = "Cookie creada. Recarga la página para verla en \$_COOKIE.";
}

// --- Manejo de sesión ---
if (isset($_POST['guardar_sesion'])) {
    $_SESSION['usuario'] = $_POST['usuario_sesion'] ?? 'Invitado';
}

// --- Variables de entorno ---
$_ENV['EJEMPLO_ENV'] = 'Variable de entorno de ejemplo';
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Ejemplo de Superglobales PHP</title>
</head>
<body>
    <h1>Demostración de variables superglobales en PHP</h1>

    <!-- FORMULARIOS -->
    <h2>Formulario con método GET</h2>
    <form action="superglobales_php.php" method="get">
        Nombre (GET): <input type="text" name="nombre_get">
        <input type="submit" value="Enviar por GET">
    </form>

    <h2>Formulario con método POST</h2>
    <form action="superglobales_php.php" method="post">
        Nombre (POST): <input type="text" name="nombre_post">
        <input type="submit" value="Enviar por POST">
    </form>

    <h2>Formulario con subida de archivo ($_FILES)</h2>
    <form action="superglobales_php.php" method="post" enctype="multipart/form-data">
        Archivo: <input type="file" name="archivo">
        <input type="submit" value="Subir archivo">
    </form>

    <h2>Cookie</h2>
    <form action="superglobales_php.php" method="post">
        <input type="hidden" name="crear_cookie" value="1">
        <input type="submit" value="Crear cookie">
    </form>
    <?php if (isset($mensaje_cookie)) echo "<p><strong>$mensaje_cookie</strong></p>"; ?>

    <h2>Sesión</h2>
    <form action="superglobales_php.php" method="post">
        Usuario: <input type="text" name="usuario_sesion">
        <input type="hidden" name="guardar_sesion" value="1">
        <input type="submit" value="Guardar en sesión">
    </form>

    <hr>

    <!-- RESULTADOS -->
    <h2>Resultados</h2>

    <h3>$_GET</h3>
    <pre><?php print_r($_GET); ?></pre>

    <h3>$_POST</h3>
    <pre><?php print_r($_POST); ?></pre>

    <h3>$_REQUEST</h3>
    <pre><?php print_r($_REQUEST); ?></pre>

    <h3>$_FILES</h3>
    <pre><?php print_r($_FILES); ?></pre>

    <h3>$_SERVER</h3>
    <pre><?php print_r($_SERVER); ?></pre>

    <h3>$_ENV</h3>
    <pre><?php print_r($_ENV); ?></pre>

    <h3>$_COOKIE</h3>
    <pre><?php print_r($_COOKIE); ?></pre>

    <h3>$_SESSION</h3>
    <pre><?php print_r($_SESSION); ?></pre>

</body>
</html>
