<?php
session_start();
include_once 'models/Conexion.php';
include_once 'models/Post.php';
include_once 'models/Etiqueta.php';

$database = new Database();
$db = $database->getConnection();
$post = new Post($db);
$etiqueta = new Etiqueta($db);

$request_uri = $_SERVER['REQUEST_URI'];
$path = parse_url($request_uri, PHP_URL_PATH);
$segments = explode('/', trim($path, '/'));

$tag_filter = isset($_GET['tag']) ? $_GET['tag'] : null;
$search_query = isset($_GET['search']) ? $_GET['search'] : null;

$posts = [];
if(isset($_SESSION['user_id'])) {
    $user_id_filter = null;
    if($_SESSION['role'] === 'user') {
        $user_id_filter = $_SESSION['user_id'];
    }
    
    if($tag_filter) {
        $posts = $etiqueta->obtenerPostsPorEtiqueta($tag_filter);
        if($user_id_filter) {
            $posts = array_filter($posts, function($p) use ($user_id_filter) {
                return $p['usuario_id'] == $user_id_filter;
            });
        }
    } else {
        $posts = $post->obtenerTodos(20, 0, $user_id_filter);
    }
}

?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Blog Personal</title>
    <link rel="stylesheet" href="css/styles.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <nav class="navbar">
        <a href="index.php" class="navbar-brand">Mi Blog</a>
        <div class="navbar-nav">
            <button id="theme-toggle" class="theme-toggle" title="Cambiar Tema">🌓</button>
            <?php if(isset($_SESSION['user_id'])): ?>
                <span>Hola, <?php echo htmlspecialchars($_SESSION['username']); ?></span>
                <a href="views/create_post.php" class="btn">Crear Post</a>
                <?php if($_SESSION['role'] === 'admin'): ?>
                    <a href="views/admin.php" class="nav-link">Admin</a>
                <?php endif; ?>
                <a href="controllers/logout.php" class="nav-link">Salir</a>
            <?php else: ?>
                <a href="views/Login.php" class="nav-link">Login</a>
                <a href="views/registro.php" class="btn">Registro</a>
            <?php endif; ?>
        </div>
    </nav>

    <div class="container">
        <?php if(!isset($_SESSION['user_id'])): ?>
            <div class="auth-container" style="max-width: 600px; text-align: center;">
                <h1>Bienvenido a Mi Blog</h1>
                <p style="margin: 2rem 0; font-size: 1.1rem; color: var(--text-muted);">
                    Para ver las publicaciones, por favor inicia sesión o regístrate.
                </p>
                <div style="display: flex; gap: 1rem; justify-content: center;">
                    <a href="views/Login.php" class="btn">Iniciar Sesión</a>
                    <a href="views/registro.php" class="btn">Registrarse</a>
                </div>
            </div>
        <?php else: ?>
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
                <?php if($tag_filter): ?>
                    <h2>Etiqueta: <?php echo htmlspecialchars($tag_filter); ?></h2>
                    <a href="index.php">Ver todos</a>
                <?php else: ?>
                    <h2><?php echo $_SESSION['role'] === 'user' ? "Mis Publicaciones" : "Publicaciones Recientes"; ?></h2>
                <?php endif; ?>

                <form action="index.php" method="get" style="display: flex; gap: 0.5rem;">
                    <input type="text" name="tag" placeholder="Buscar etiqueta..." value="<?php echo htmlspecialchars($tag_filter ?? ''); ?>">
                    <button type="submit" class="btn">Buscar</button>
                </form>
            </div>

            <div class="blog-grid">
                <?php foreach($posts as $p): ?>
                    <article class="blog-card">
                        <?php if($p['imagen']): ?>
                            <img src="<?php echo htmlspecialchars($p['imagen']); ?>" alt="<?php echo htmlspecialchars($p['titulo']); ?>" class="blog-image">
                        <?php else: ?>
                            <img src="https://via.placeholder.com/400x200?text=No+Image" alt="Placeholder" class="blog-image">
                        <?php endif; ?>
                        
                        <div class="blog-content">
                            <div class="blog-meta">
                                Por <?php echo htmlspecialchars($p['nombre_usuario']); ?> | <?php echo date('d M Y', strtotime($p['fecha_creacion'])); ?>
                            </div>
                            <a href="views/post_detail.php?slug=<?php echo htmlspecialchars($p['slug']); ?>" class="blog-title">
                                <?php echo htmlspecialchars($p['titulo']); ?>
                            </a>
                            <p class="blog-excerpt">
                                <?php echo substr(strip_tags($p['contenido']), 0, 100) . '...'; ?>
                            </p>
                            
                            <div class="tags">
                                <?php 
                                    $post_tags = $etiqueta->obtenerPorPost($p['id']);
                                    foreach($post_tags as $t): 
                                ?>
                                    <a href="index.php?tag=<?php echo urlencode($t['nombre']); ?>" class="tag">#<?php echo htmlspecialchars($t['nombre']); ?></a>
                                <?php endforeach; ?>
                            </div>

                            <?php if(isset($_SESSION['user_id']) && $_SESSION['user_id'] == $p['usuario_id']): ?>
                                <div style="margin-top: 1rem;">
                                    <a href="views/edit_post.php?id=<?php echo $p['id']; ?>" class="btn" style="padding: 0.25rem 0.5rem; font-size: 0.8rem;">Editar Post</a>
                                </div>
                            <?php endif; ?>
                        </div>
                    </article>
                <?php endforeach; ?>
            </div>
            
            <?php if(empty($posts)): ?>
                <p>No hay publicaciones aún.</p>
            <?php endif; ?>
        <?php endif; ?>
    </div>

    <script src="theme-toggle.js"></script>
</body>
</html>