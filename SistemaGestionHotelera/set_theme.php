<?php
if (isset($_POST['theme'])) {
    $theme = $_POST['theme'];
    setcookie('theme_preference', $theme, time() + (86400 * 30), "/"); // 30 días
    header('Location: ' . $_SERVER['HTTP_REFERER']);
    exit;
}