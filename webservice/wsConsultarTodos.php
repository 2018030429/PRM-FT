<?php
  $hostname_localhost = "localhost";
  $database_localhost = "id17410281_agenda_db";
  $username_localhost = "id17410281_cursoandroid";
  $pass_localhost = "_c>kpjdm0$5LiObF";

  $json = array();
  $idMovil = $_GET['idMovil'];

  $conexion = mysqli_connect(
    $hostname_localhost,
    $username_localhost,
    $pass_localhost,
    $database_localhost
  );

  $consulta = ("SELECT * FROM contactos where idMovil='{$idMovil}' ORDER BY nombre");

  $resultado_consulta = mysqli_query($conexion,$consulta);

  while($registro=mysqli_fetch_array($resultado_consulta)) {
    $json['contactos'][]=$registro;
  }

  mysqli_close($conexion);
  echo json_encode($json);
?>