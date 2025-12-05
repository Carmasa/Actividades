<?php
session_start();
if(!isset($_SESSION['user_id'])) {
    header("Location: Login.php");
    exit();
}

include_once '../models/Conexion.php';
include_once '../models/Post.php';
include_once '../models/Etiqueta.php';

$database = new Database();
$db = $database->getConnection();
$post = new Post($db);
$etiqueta = new Etiqueta($db);

$message = "";
$post_id = isset($_GET['id']) ? $_GET['id'] : null;

if(!$post_id) {
    header("Location: ../index.php");
    exit();
}

$current_post = $post->obtenerPorId($post_id);

// Check ownership
if($current_post['usuario_id'] != $_SESSION['user_id'] && $_SESSION['role'] !== 'admin') {
    die("No tienes permiso para editar este post.");
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $post->id = $post_id;
    $post->titulo = $_POST['title'];
    $post->contenido = $_POST['content'];
    $post->imagen = $current_post['imagen']; 
    
    // Handle Image Upload
    if(isset($_FILES['image']) && $_FILES['image']['error'] == 0) {
        $target_dir = "../uploads/";
        if(!is_dir($target_dir)){
            mkdir($target_dir, 0777, true);
        }
        $target_file = $target_dir . basename($_FILES["image"]["name"]);
        
        $check = getimagesize($_FILES["image"]["tmp_name"]);
        if($check !== false) {
            if(move_uploaded_file($_FILES["image"]["tmp_name"], $target_file)) {
                $post->imagen = "uploads/" . basename($_FILES["image"]["name"]);
            } else {
                $message = "Error al subir la imagen.";
            }
        } else {
            $message = "El archivo no es una imagen.";
        }
    }

    if($post->actualizar()) {
        if(!empty($_POST['tags'])) {
             $tags_input = explode(',', $_POST['tags']);
             foreach($tags_input as $tag_name) {
                 $etiqueta_id = $etiqueta->obtenerOCrear($tag_name);
                 $etiqueta->asociarConPost($post->id, $etiqueta_id);
             }
        }
        
        header("Location: post_detail.php?slug=" . $current_post['slug']); 
        exit();
    } else {
        $message = "Error al actualizar el post.";
    }
}
?>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Post - Blog CMS</title>
    <link rel="stylesheet" href="../css/styles.css">
</head>
<body>
    <nav class="navbar">
        <a href="../index.php" class="navbar-brand">Mi Blog</a>
        <div class="navbar-nav">
            <a href="../index.php" class="nav-link">Volver</a>
        </div>
    </nav>

    <div class="container">
        <div class="auth-container" style="max-width: 800px;">
            <h2>Editar Post</h2>
            <?php if($message != ""): ?>
                <div class="alert"><?php echo $message; ?></div>
            <?php endif; ?>
            
            <form action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]) . "?id=" . $post_id; ?>" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="title">Título:</label>
                    <input type="text" name="title" id="title" value="<?php echo htmlspecialchars($current_post['titulo']); ?>" required>
                </div>
                
                <div class="form-group">
                    <label for="image">Imagen Destacada (Dejar vacío para mantener la actual):</label>
                    <?php if($current_post['imagen']): ?>
                        <div style="margin-bottom: 0.5rem;">
                            <img src="../<?php echo htmlspecialchars($current_post['imagen']); ?>" alt="Current" style="height: 100px;">
                        </div>
                    <?php endif; ?>
                    <input type="file" name="image" id="image" accept="image/*">
                </div>

                <div class="form-group">
                    <label for="tags">Etiquetas (Añadir nuevas):</label>
                    <input type="text" name="tags" id="tags" placeholder="tecnologia, vida, viajes">
                </div>

                <div class="form-group">
                    <label for="content">Contenido:</label>
                    <textarea name="content" id="content" rows="10" required><?php echo htmlspecialchars($current_post['contenido']); ?></textarea>
                </div>

                <button type="submit" class="btn">Actualizar</button>
            </form>
        </div>
    </div>
    <script src="../theme-toggle.js"></script>
</body>
</html>
