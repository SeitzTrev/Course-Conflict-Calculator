<?php
	session_start();

	//Use whatever database name you want here, just import the sql table i provided into xampp or cloud9.
	$cccdb = 'CCC_DB';
	$conn = requireOnce($cccdb);

	if(isset($_POST['user']) and isset($_POST['pass']))
	{
		$user = $_POST['user'];
		$pass = $_POST['pass'];
		
		$passwordQuery = "SELECT * FROM ccc_userdb WHERE username = '$user' and password = '$pass'";

		$result = $conn->query($passwordQuery);
		
		if($result == false)
		{
			$error = true;
		}
		else
		{
			//If there is exactly 1 row, user is authenticated.
			$count = mysqli_num_rows($result);
		
			if($count == 1)
			{
				$_SESSION['loggedIn'] = true;
				header('Location: 3CWebUtility.php');
			}
			else
			{
				$error = true;
			}
		}
	}

	function requireOnce($database)
	{
		$servername = "localhost";
		$username = "root";
		$password = "";
		
		// Create connection
		$conn = new mysqli($servername, $username, $password, $database);

		// Check connection
		if ($conn->connect_error)
		{
			die("Connection failed: " .$conn->connect_error);
		}
		
		return $conn;
	}
?> 


<!DOCTYPE html>
<html>
<head>
	<title>Course Conflict Calculator</title>
	<link rel="stylesheet" href="3c_css.css" type="text/css" title="3c_css">
</head>
<body>
	<div>
	<header class="banner">
			<section class="logo">Course Conflict Calculator</section><br/>
	</header>
	</div>
	<div id="centerpage">
		<article>
		<h3 style="text-align:center">Welcome to 3C!</h3>
		<p style="text-align:center;">Please type in the username and password provided by your department chair.</p>
		<?php if (isset($error)) echo "<div style='color:red;text-align:center;'>Invalid username or password.  Please try again.</div></br>"?>
		
		<form action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" method="post">
        <table style="margin-left:auto;margin-right:auto;padding-bottom:50px;">
			<tr>
				<td>Username:</td>
				<td><input name="user" type="text"></td>
			</tr>
			<tr>
			<td>Password:</td>
			<td><input name="pass" type="password"></td>
        </tr>
        <tr>
          <td></td>
          <td><input type="submit" value="Log In"></td>
        </tr>
      </table>      
    </form>
	</div>
	<br/>
</body>
</html>