<?php
	session_start();
	if(!$_SESSION['loggedIn'])
	{
		header('Location: 3CverifyUser.php');
		exit;
	}
	?>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8"/>
		<title>Course Conflict Calculator<br>*3C*</title>
		<link rel = "stylesheet" href = "3CWebUtility.css" type = "text/css" title = "float layout style">
		<script type="text/javascript" src="3CWebUtility.js"></script>
		<script lang="javascript" src="js-xlsx-master/dist/xlsx.core.min.js"></script>
	</head>
	<body>
		<div>
			<header class="banner">
				<section class="logo">Course Conflict Calculator Home</section><br/>
				<nav>
					<ul>
						<li><a href="3c_homepage.html">Home</a></li>
						<li><a href="3CWebUtility.php">3C Calculator</a></li>
						<!--<li><a href="http://www.ipfw.edu">IPFW</a></li>-->
					</ul>
				</nav>
			</header>
		</div>
		<div class = "content">
		
			<!--area to select or drop excel file-->
			<div class = "fileForm">
				<input class = "fileButton" type="file" id = "fileselect" style = "background-color: white;">
				<input class = "fileTextbox" type = "text" id = "drop" disabled>
			</div>
			
			<!--insert table here-->
			<div class = "table" id = "conCell"></div>
			
			<!--Text area to display conficts as text and other toggles-->
			<div class = "info">
				<div class = "toggles">
					<label class = "checkbox"><input type = "checkbox" id = "nCon" onclick="updatePage()" checked/> Non Conflicts </label>
					<label class = "checkbox"><input type = "checkbox" id = "con" onclick="updatePage()" checked/> Conflicts </label>
				</div>
				<div class = "conflicts">
					<textarea id = "courseInfoText" disabled>
					</textarea>
				</div>
				<div class = "legend">
					<h3>Directions:</h3><b>
					<p>Load a CSV file by either dragging into the empty box at the top of the screen,
					or select the path using the button "Browse".<p>
					<p>Click on the squares in the picture to display conflict information.</p>
					<p>Click on the checkboxes at the top to display only conflicts, only non-conflicts, both or neither.</p>
					<p>Conflict Key:</p>
					<p>Conflicts: Black, Level 100 Courses: Green, Level 200 Courses: Blue, Level 300 Courses: Orange, Level 400 Courses: Red,
					Level 500 Courses: Yellow</p></b>
				</div>
				
			</div>
			<div id = "courseEdit" class = "courseEdit">
			</div>
		</div>
		
		<script src="Course.js"></script>
		<script src="Conflict.js"></script>
		<script src="TimeSegment.js"></script>
		<script src="ParseExcel.js"></script>
		<script src="EditCourses.js"></script>
		
	</body>
	<script>
		tableCreate();
//  		mockData();

	function updatePage()
	{
		updater();
	}

	function logout()
	{
		<?php 
		session_unset();
		session_destroy();
		?>
	}
	</script>
</html>