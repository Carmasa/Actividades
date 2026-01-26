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

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $post->usuario_id = $_SESSION['user_id'];
    $post->titulo = $_POST['title'];
    $post->contenido = $_POST['content'];
    
    $image_path = null;
    if(isset($_FILES['image']) && $_FILES['image']['error'] == 0) {
        $target_dir = "../uploads/";
        if(!is_dir($target_dir)){
            mkdir($target_dir, 0777, true);
        }
        $target_file = $target_dir . basename($_FILES["image"]["name"]);
        
        $check = getimagesize($_FILES["image"]["tmp_name"]);
        if($check !== false) {
            if(move_uploaded_file($_FILES["image"]["tmp_name"], $target_file)) {
                $image_path = "uploads/" . basename($_FILES["image"]["name"]);
            } else {
                $message = "Error al subir la imagen.";
            }
        } else {
            $message = "El archivo no es una imagen.";
        }
    }
    $post->imagen = $image_path;

    if($post->crear()) {
        if(!empty($_POST['tags'])) {
            $tags_input = explode(',', $_POST['tags']);
            foreach($tags_input as $tag_name) {
                $etiqueta_id = $etiqueta->obtenerOCrear($tag_name);
                $etiqueta->asociarConPost($post->id, $etiqueta_id);
            }
        }
        header("Location: ../index.php");
        exit();
    } else {
        $message = "Error al crear el post.";
    }
}
?>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crear Post - Blog CMS</title>
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
            <h2>Crear Nuevo Post</h2>
            <?php if($message != ""): ?>
                <div class="alert"><?php echo $message; ?></div>
            <?php endif; ?>
            
            <form action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="title">Título:</label>
                    <input type="text" name="title" id="title" required>
                </div>
                
                <div class="form-group">
                    <label for="image">Imagen Destacada:</label>
                    <input type="file" name="image" id="image" accept="image/*">
                </div>

                <div class="form-group">
                    <label for="tags">Etiquetas (separadas por coma):</label>
                    <input type="text" name="tags" id="tags" placeholder="tecnologia, vida, viajes">
                </div>

                <div class="form-group">
                    <label for="content">Contenido:</label>
                    <textarea name="content" id="content" rows="10" required></textarea>
                </div>

                <button type="submit" class="btn">Publicar</button>
            </form>
        </div>
    </div>
    <script src="../theme-toggle.js"></script>
</body>
</html>