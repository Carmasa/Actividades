<?php
session_start();
if(!isset($_SESSION['user_id']) || $_SESSION['role'] !== 'admin') {
    header("Location: Login.php");
    exit();
}

include_once '../models/Conexion.php';
include_once '../models/Usuario.php';
include_once '../models/Post.php';
include_once '../models/PalabraProhibida.php';

$database = new Database();
$db = $database->getConnection();
$usuario = new Usuario($db);
$post = new Post($db);
$palabraProhibida = new PalabraProhibida($db);

if(isset($_POST['delete_post_id'])) {
    $post->eliminar($_POST['delete_post_id']);
    $message = "Post eliminado correctamente.";
}

if(isset($_POST['add_word'])) {
    if(!empty($_POST['word'])) {
        if($palabraProhibida->crear($_POST['word'])) {
            $message = "Palabra prohibida agregada.";
        } else {
            $message = "Error al agregar palabra.";
        }
    }
}

if(isset($_POST['delete_word_id'])) {
    if($palabraProhibida->eliminar($_POST['delete_word_id'])) {
        $message = "Palabra prohibida eliminada.";
    } else {
        $message = "Error al eliminar palabra.";
    }
}

$usuarios = $usuario->obtenerTodos();
$palabras_prohibidas = $palabraProhibida->obtenerTodasConId();
?>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Blog CMS</title>
    <link rel="stylesheet" href="../css/styles.css">
</head>
<body>
    <nav class="navbar">
        <a href="../index.php" class="navbar-brand">Admin Panel</a>
        <div class="navbar-nav">
            <a href="../index.php" class="nav-link">Ver Sitio</a>
            <a href="../controllers/logout.php" class="nav-link">Salir</a>
        </div>
    </nav>

    <div class="container">
        <h1>Gestión de Usuarios y Blogs</h1>
        <?php if(isset($message)): ?>
            <div class="alert"><?php echo $message; ?></div>
        <?php endif; ?>

        <?php foreach($usuarios as $u): ?>
            <div class="user-section">
                <h3><?php echo htmlspecialchars($u['nombre_usuario']); ?> <small>(<?php echo htmlspecialchars($u['email']); ?>)</small></h3>
                <p>Rol: <?php echo htmlspecialchars($u['rol']); ?></p>
                
                <details>
                    <summary style="cursor: pointer; color: var(--primary-color);">Ver Publicaciones</summary>
                    <div class="post-list">
                        <?php 
                            $user_posts = $post->obtenerPorUsuario($u['id']);
                            if(empty($user_posts)):
                        ?>
                            <p>Este usuario no tiene publicaciones.</p>
                        <?php else: ?>
                            <?php foreach($user_posts as $p): ?>
                                <div class="post-item <?php echo $p['marcado'] ? 'flagged' : ''; ?>">
                                    <div>
                                        <a href="post_detail.php?slug=<?php echo htmlspecialchars($p['slug']); ?>">
                                            <?php echo htmlspecialchars($p['titulo']); ?>
                                        </a>
                                        <?php if($p['marcado']): ?>
                                            <span style="color: red; font-size: 0.8rem; font-weight: bold;">[MARCADO]</span>
                                        <?php endif; ?>
                                    </div>
                                    <form method="post" onsubmit="return confirm('¿Estás seguro de eliminar este post?');" style="display:inline;">
                                        <input type="hidden" name="delete_post_id" value="<?php echo $p['id']; ?>">
                                        <button type="submit" class="btn btn-danger" style="padding: 0.25rem 0.5rem; font-size: 0.8rem;">Eliminar</button>
                                    </form>
                                </div>
                            <?php endforeach; ?>
                        <?php endif; ?>
                    </div>
                </details>
            </div>
        <?php endforeach; ?>

        <div class="user-section" style="margin-top: 3rem;">
            <h2>Gestión de Palabras Prohibidas</h2>
            <form method="post" style="display: flex; gap: 1rem; margin-bottom: 1rem;">
                <input type="text" name="word" placeholder="Nueva palabra prohibida" required style="flex: 1;">
                <button type="submit" name="add_word" class="btn">Agregar</button>
            </form>

            <div style="display: flex; flex-wrap: wrap; gap: 0.5rem;">
                <?php foreach($palabras_prohibidas as $p): ?>
                    <div class="tag" style="display: flex; align-items: center; gap: 0.5rem; background-color: #fee2e2; color: #991b1b;">
                        <?php echo htmlspecialchars($p['palabra']); ?>
                        <form method="post" style="display: inline;">
                            <input type="hidden" name="delete_word_id" value="<?php echo $p['id']; ?>">
                            <button type="submit" style="background: none; border: none; cursor: pointer; color: #991b1b; font-weight: bold;">&times;</button>
                        </form>
                    </div>
                <?php endforeach; ?>
            </div>
        </div>
    </div>
    <script src="../theme-toggle.js"></script>
</body>
</html>