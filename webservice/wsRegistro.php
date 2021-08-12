<?php
  $hostname_localhost = "localhost";
  $database_localhost = "id17410281_agenda_db";
  $username_localhost = "id17410281_cursoandroid";
  $pass_localhost = "_c>kpjdm0$5LiObF";

  $json = array();
  if (isset($_GET["nombre"]) && isset($_GET["telefono1"]) &&
  isset($_GET["direccion"])) {
    $nombre = $_GET['nombre'];
    $telefono1 = $_GET['telefono1'];
    $telefono2 = $_GET['telefono2'];
    $direccion = $_GET['direccion'];
    $notas = $_GET['notas'];
    $favorite = $_GET['favorite'];
    $idMovil = $_GET['idMovil'];

    $conexion = mysqli_connect(
      $hostname_localhost,
      $username_localhost,
      $pass_localhost,
      $database_localhost
    );

    $insert(
      "INSERT INTO contactos(
        _ID, nombre, telefono1, telefono2,direccion, notas, favorite , idMovil
      )
      VALUES(
        NULL,'{$nombre}','{$telefono1}',' {$telefono2}','{$direccion}','{$notas}',' {$favorite}','{$idMovil}'
      )"
    );

    $resultado_insert = mysqli_query($conexion,$insert);
    if ($resultado_insert) {
      $consulta = "SELECT * FROM contactos";
      $resultado = mysqli_query($conexion,$consulta);
      if ($registro = mysqli_fetch_array($resultado)) {
        $json['contacto'][]=$registro;
      }
      mysqli_close($conexion);
      echo json_encode($json);
    } else {
      $resulta["_ID"] = 0;
      $resulta["nombre"] ='No registrado';
      $resulta["telefono1"] ='No registrado';
      $resulta["telefono2"] ='No registrado';
      $resulta["direccion"] ='No registrado';
      $resulta["notas"] ='No registrado';
      $resulta["favorite"] = 0;
      $resulta["idMovil"] ='No registrado';
      $json['contactos'][]=$resulta;
      echo json_encode($json);
    }
  } else {
    $resulta["_ID"] = 0;
    $resulta["nombre"] ='WS no retorna';
    $resulta["telefono1"] ='WS no retorna';
    $resulta["telefono2"] ='WS no retorna';
    $resulta["direccion"] ='WS no retorna';
    $resulta["notas"] ='WS no retorna';
    $resulta["favorite"] = 0;
    $resulta["idMovil"] ='No retorna';
    $json['contactos'][]=$resulta;
    echo json_encode($json);
  }
?>