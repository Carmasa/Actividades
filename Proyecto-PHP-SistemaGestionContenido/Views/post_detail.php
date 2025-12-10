<?php
session_start();
include_once '../models/Conexion.php';
include_once '../models/Post.php';
include_once '../models/Etiqueta.php';

$database = new Database();
$db = $database->getConnection();
$post = new Post($db);
$etiqueta = new Etiqueta($db);

$slug = isset($_GET['slug']) ? $_GET['slug'] : die('ERROR: Post no encontrado.');

$current_post = $post->obtenerPorSlug($slug);

if(!$current_post) {
    die('ERROR: Post no encontrado.');
}

$post_tags = $etiqueta->obtenerPorPost($current_post['id']);
?>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo htmlspecialchars($current_post['titulo']); ?> - Blog CMS</title>
    <link rel="stylesheet" href="../css/styles.css">
</head>
<body>
    <nav class="navbar">
        <a href="../index.php" class="navbar-brand">Mi Blog</a>
        <div class="navbar-nav">
            <button id="theme-toggle" class="theme-toggle" title="Cambiar Tema">🌓</button>
            <a href="../index.php" class="nav-link">Volver</a>
        </div>
    </nav>

    <div class="container">
        <article class="blog-card" style="max-width: 800px; margin: 0 auto;">
            <?php if($current_post['imagen']): ?>
                <img src="<?php echo htmlspecialchars('../' . $current_post['imagen']); ?>" alt="<?php echo htmlspecialchars($current_post['titulo']); ?>" class="blog-image" style="height: 400px;">
            <?php endif; ?>
            
            <div class="blog-content">
                <h1 class="blog-title" style="font-size: 2rem;"><?php echo htmlspecialchars($current_post['titulo']); ?></h1>
                
                <div class="blog-meta">
                    Por <?php echo htmlspecialchars($current_post['nombre_usuario']); ?> | <?php echo date('d M Y', strtotime($current_post['fecha_creacion'])); ?>
                </div>

                <div class="tags" style="margin-bottom: 2rem;">
                    <?php foreach($post_tags as $t): ?>
                        <span class="tag">#<?php echo htmlspecialchars($t['nombre']); ?></span>
                    <?php endforeach; ?>
                </div>

                <div class="blog-body" style="line-height: 1.8;">
                    <?php echo nl2br(htmlspecialchars($current_post['contenido'])); ?>
                </div>
            </div>
        </article>
    </div>
    <script src="../theme-toggle.js"></script>
</body>
</html>
