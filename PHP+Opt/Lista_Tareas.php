<?php
session_start();

$archivo_xml = 'tareas.xml';

// Si el archivo no existe o está vacío, créalo con contenido válido
if (!file_exists($archivo_xml) || filesize($archivo_xml) === 0) {
    file_put_contents($archivo_xml, '<?xml version="1.0" encoding="UTF-8"?><tareas></tareas>');
}

// Función para cargar tareas desde XML
function cargar_desde_xml($archivo_xml) {
    $xml = simplexml_load_file($archivo_xml);
    $tareas = [];
    $completadas = [];
    if ($xml !== false) {
        if (isset($xml->pendiente)) {
            foreach ($xml->pendiente as $t) {
                $tareas[] = (string)$t;
            }
        }
        if (isset($xml->completada)) {
            foreach ($xml->completada as $c) {
                $completadas[] = (string)$c;
            }
        }
    }
    $_SESSION['tareas'] = $tareas;
    $_SESSION['completadas'] = $completadas;
    return [$tareas, $completadas];
}

// Función para guardar tareas en XML (con formato bonito)
function guardar_xml($tareas, $completadas, $archivo_xml) {
    $xml = new SimpleXMLElement('<?xml version="1.0" encoding="UTF-8"?><tareas></tareas>');
    foreach ($tareas as $t) {
        $xml->addChild('pendiente', $t);
    }
    foreach ($completadas as $c) {
        $xml->addChild('completada', $c);
    }
    $dom = dom_import_simplexml($xml)->ownerDocument;
    $dom->formatOutput = true;
    $dom->save($archivo_xml);
}

// Cargar tareas al inicio
list($tareas, $completadas) = cargar_desde_xml($archivo_xml);

// Agregar nueva tarea
if ($_SERVER['REQUEST_METHOD'] === 'POST' && !empty($_POST['nueva_tarea'])) {
    $tarea = trim($_POST['nueva_tarea']);
    if ($tarea !== '') {
        $tareas[] = htmlspecialchars($tarea);
        guardar_xml($tareas, $completadas, $archivo_xml);
        header("Location: Lista_Tareas.php");
        exit;
    }
}

// Marcar tarea como completada
if (isset($_POST['completar'])) {
    $contenido = intval($_POST['completar']);
    if (isset($tareas[$contenido])) {
        $completadas[] = $tareas[$contenido];
        unset($tareas[$contenido]);
        $tareas = array_values($tareas); 
        guardar_xml($tareas, $completadas, $archivo_xml);
        header("Location: Lista_Tareas.php");
        exit;
    }
}

// Eliminar tarea pendiente
if (isset($_POST['eliminar'])) {
    $contenido = intval($_POST['eliminar']);
    if (isset($tareas[$contenido])) {
        unset($tareas[$contenido]);
        $tareas = array_values($tareas); 
        guardar_xml($tareas, $completadas, $archivo_xml);
        header("Location: Lista_Tareas.php");
        exit;
    }
}

// Eliminar tarea completada
if (isset($_POST['eliminar_completada'])) {
    $contenido = intval($_POST['eliminar_completada']);
    if (isset($completadas[$contenido])) {
        unset($completadas[$contenido]);
        $completadas = array_values($completadas); 
        guardar_xml($tareas, $completadas, $archivo_xml);
        header("Location: Lista_Tareas.php");
        exit;
    }
}

// Limpiar listas
if (isset($_POST['limpiar'])) {
    $tareas = [];
    $completadas = [];
    guardar_xml($tareas, $completadas, $archivo_xml);
    header("Location: Lista_Tareas.php");
    exit;
}

// Actualiza la sesión antes de mostrar
$_SESSION['tareas'] = $tareas;
$_SESSION['completadas'] = $completadas;
?>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Lista de tareas</title>
    <link rel="stylesheet" href="Lista_Tareas.css">
</head>
<body>
    <div class="contenedor-tareas">
        <div class="titulo-principal">LISTA DE TAREAS</div>
        <div class="listas">
            <div>
                <h2>Tareas pendientes:</h2>
                <ul>
                    <?php foreach ($_SESSION['tareas'] as $i => $tarea): ?>
                        <li>
                            <span class="tarea-texto"><?php echo $tarea; ?></span>
                            <form class="inline" method="post">
                                <button type="submit" name="completar" value="<?php echo $i; ?>">✔</button>
                            </form>
                            <form class="inline" method="post">
                                <button type="submit" name="eliminar" value="<?php echo $i; ?>">🗑</button>
                            </form>
                        </li>
                    <?php endforeach; ?>
                </ul>
                <form method="post" class="form-agregar-tarea">
                    <input type="text" name="nueva_tarea" placeholder="Nueva tarea" required>
                    <button class="boton" type="submit">Agregar tarea</button>
                </form>
            </div>
            <div>
                <h2>Tareas completadas</h2>
                <ul>
                    <?php foreach ($_SESSION['completadas'] as $i => $tarea): ?>
                        <li>
                            <span class="tarea-texto"><?php echo $tarea; ?></span>
                            <form class="inline" method="post">
                                <button type="submit" name="eliminar_completada" value="<?php echo $i; ?>">🗑</button>
                            </form>
                        </li>
                    <?php endforeach; ?>
                </ul>
            </div>
        </div>
        <form method="post">
            <button type="submit" name="limpiar" value="1" class="limpiar-btn">Limpiar lista</button>
        </form>
    </div>
</body>
</html>