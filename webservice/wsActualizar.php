<?php
  $hostname_localhost = "localhost";
  $database_localhost = "id17410281_agenda_db";
  $username_localhost = "id17410281_cursoandroid";
  $pass_localhost = "_c>kpjdm0$5LiObF";

  $json = array();
  if (isset($_GET["_ID"]) && isset($_GET["nombre"]) && isset($_GET["direccion"]) && isset($_GET["telefono1"])) {
    $_ID = $_GET['_ID'];
    $nombre = $_GET['nombre'];
    $telefono1 = $_GET['telefono1'];
    $telefono2 = $_GET['telefono2'];
    $direccion = $_GET['direccion'];
    $notas = $_GET['notas'];
    $favorite = $_GET['favorite'];
    $conexion = mysqli_connect($hostname_localhost, $username_localhost,
    $pass_localhost,$database_localhost);

    $update = (
      "UPDATE contactos SET nombre='{$nombre}',telefono1= '{$telefono1}',telefono2='{$telefono2}',direccion='{$direccion}',notas='{$notas}',favorite='{$favorite}' WHERE _ID = '{$_ID}'");

    $resultado_update = mysqli_query($conexion,$update);
  } else {
    $resulta["_ID"] = 0;
    $resulta["nombre"] ='WS no retorna';
    $resulta["telefono1"] ='WS no retorna';
    $resulta["direccion"] ='WS no retorna';
    $json['contactos'][]=$resulta;
    echo json_encode($json);
  }
?>