<?php
// Iniciar sesión para usar $_SESSION
session_start();

// Manejo de cookies
if (isset($_POST['crear_cookie'])) {
    setcookie("mi_cookie", "Hola, esto es una cookie", time() + 3600); 
    $mensaje_cookie = "Cookie creada. Recarga la página para verla en \$_COOKIE.";
}

// Manejo de sesión
if (isset($_POST['guardar_sesion'])) {
    $_SESSION['usuario'] = $_POST['usuario_sesion'] ?? 'Invitado';
    $mensaje_sesion = "Usuario guardado en sesión: " . htmlspecialchars($_SESSION['usuario']);
}

// Manejo de subida de archivos
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_FILES['archivo'])) {
    if ($_FILES['archivo']['error'] === UPLOAD_ERR_OK) {
        $nombre_archivo = htmlspecialchars($_FILES['archivo']['name']);
        $mensaje_archivo = "Archivo subido: $nombre_archivo";
    } else {
        $mensaje_archivo = "Error en la subida: " . $_FILES['archivo']['error'];
    }
}
?>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Conceptos sobre programación web en PHP</title>
</head>
<body>
    <h1>Demostración de variables superglobales en PHP</h1>

    <!-- FORMULARIOS -->
    <h2>Formulario con método GET</h2>
    <form action="<?php echo htmlspecialchars($_SERVER['PHP_SELF']); ?>" method="get">
        Nombre (GET): <input type="text" name="nombre_get" value="<?php echo isset($_GET['nombre_get']) ? htmlspecialchars($_GET['nombre_get']) : ''; ?>">
        <input type="submit" value="Enviar por GET">
    </form>

    <h2>Formulario con método POST</h2>
    <form action="<?php echo htmlspecialchars($_SERVER['PHP_SELF']); ?>" method="post">
        Nombre (POST): <input type="text" name="nombre_post" value="<?php echo isset($_POST['nombre_post']) ? htmlspecialchars($_POST['nombre_post']) : ''; ?>">
        <input type="submit" value="Enviar por POST">
    </form>

    <h2>Formulario con subida de archivo ($_FILES)</h2>

    <form action="<?php echo htmlspecialchars($_SERVER['PHP_SELF']); ?>" method="post" enctype="multipart/form-data">
        Archivo: <input type="file" name="archivo">
        <input type="submit" value="Subir archivo">
    </form>
    <?php if (isset($mensaje_archivo)) echo "<p>$mensaje_archivo</p>"; ?>

    <h2>Cookie</h2>
    <form action="<?php echo htmlspecialchars($_SERVER['PHP_SELF']); ?>" method="post">
        <input type="hidden" name="crear_cookie" value="1">
        <input type="submit" value="Crear cookie">
    </form>
    <?php if (isset($mensaje_cookie)) echo "<p>$mensaje_cookie</p>"; ?>

    <h2>Sesión</h2>
    <form action="<?php echo htmlspecialchars($_SERVER['PHP_SELF']); ?>" method="post">
        Usuario: <input type="text" name="usuario_sesion" value="<?php echo isset($_SESSION['usuario']) ? htmlspecialchars($_SESSION['usuario']) : ''; ?>">
        <input type="hidden" name="guardar_sesion" value="1">
        <input type="submit" value="Guardar en sesión">
    </form>
    <?php if (isset($mensaje_sesion)) echo "<p>$mensaje_sesion</p>"; ?>

    <hr>

    <!-- RESULTADOS -->
    <h2>Resultados</h2>

    <h3>$_GET</h3>
    <pre><?php print_r($_GET); ?></pre>

    <?php
    if (!empty($_GET["nombre_get"])) {
        echo 'Hola ' . htmlspecialchars($_GET["nombre_get"]) . '!';
    }
    ?>

    <h3>$_POST</h3>
    <pre><?php print_r($_POST); ?></pre>

    <?php
    if (!empty($_POST["nombre_post"])) {
        echo 'Hola ' . htmlspecialchars($_POST["nombre_post"]) . '!';
    }
    ?>

    <h3>$_REQUEST</h3>
    <pre><?php print_r($_REQUEST); ?></pre>

    <?php
    if (!empty($_REQUEST["nombre_get"]) || !empty($_REQUEST["nombre_post"])) {
        echo 'Hola (REQUEST): ' . htmlspecialchars($_REQUEST["nombre_get"] ?? $_REQUEST["nombre_post"]) . '!';
    }
    ?>

    <h3>$_FILES</h3>
    <pre><?php print_r($_FILES); ?></pre>

    <?php
    if (!empty($_FILES['archivo']) && $_FILES['archivo']['error'] === UPLOAD_ERR_OK) {
        echo "Archivo subido: " . htmlspecialchars($_FILES['archivo']['name']);
    }
    ?>

    <h3>$_SERVER</h3>
    <pre><?php print_r($_SERVER); ?></pre>

    <?php
    echo "Petición realizada con método: " . htmlspecialchars($_SERVER["REQUEST_METHOD"]);
    ?>

    <h3>$_ENV</h3>
    <pre><?php print_r($_ENV); ?></pre>

    <?php
    echo '¡Mi nombre de usuario es ' . $_ENV["USER"] . '!';
    ?>

    <?php
    echo '¡Mi nombre de usuario es ' . (getenv("USERNAME") ?: getenv("USER") ?: 'No definido') . '!';
    ?>

    <h3>$_COOKIE</h3>
    <pre><?php print_r($_COOKIE); ?></pre>

    <?php
    if (!empty($_COOKIE["mi_cookie"])) {
        echo "Cookie dice: " . htmlspecialchars($_COOKIE["mi_cookie"]);
    }
    ?>

    <h3>$_SESSION</h3>
    <pre><?php print_r($_SESSION); ?></pre>

    <?php
    if (!empty($_SESSION["usuario"])) {
        echo "Usuario en sesión: " . htmlspecialchars($_SESSION["usuario"]);
    }
    ?>

</body>
</html>