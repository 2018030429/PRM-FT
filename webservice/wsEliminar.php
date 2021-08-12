<?php
  $hostname_localhost = "localhost";
  $database_localhost = "id17410281_agenda_db";
  $username_localhost = "id17410281_cursoandroid";
  $pass_localhost = "_c>kpjdm0$5LiObF";

  $json = array();

  if(isset($_GET["_ID"])) {
    $_ID = $_GET['_ID']; 
    $conexion = mysqli_connect(
      $hostname_localhost,
      $username_localhost,
      $pass_localhost,
      $database_localhost
    );

    $delete = ("DELETE FROM contactos WHERE _ID ='{$_ID}'");
    $resultado_delete = mysqli_query($conexion,$delete);
  } else {
    $resulta["_ID"] = 0;
    $json['contactos'][]=$resulta;
    echo json_encode($json);
  }
?>