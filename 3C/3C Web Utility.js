
			//cell formatting
			function tableLabels(el){
				el.style.textAlign = "center";
				el.style.border= "2px solid black";
				el.style.backgroundColor= "#BBBBBB";
				el.style.height= "25px";
				el.style.maxWidth= "70px";
				el.style.minWidth= "70px";
				//el.style.fontSize = "medium";

			}
			
			//cell formatting
			function cells(el){
// 				el.style.borderRight= "1px dotted #444444";
				el.style.textAlign= "center";
				el.style.minWidth= "12px";
				el.style.maxWidth= "12px";
			}
			
			//cell formatting
			function leftCells(el){
				el.style.borderRight= "2px solid black";
				el.style.textAlign= "center";
				el.style.minWidth= "13px";
			}
			
			//cell formatting
			function bottomCells(el){
// 				el.style.borderRight= "1px dotted #000000";
				el.style.borderBottom= "2px solid black";
				el.style.textAlign= "center";
				el.style.minWidth= "14px";
			}
			
			//cell formatting
			function bottomLeftCells(el){
				el.style.borderRight= "2px solid black";
				el.style.borderBottom= "2px solid black";
				el.style.textAlign= "center";
				el.style.minWidth= "13px";
			}
			
			//create the table for manipulation
			//
			function tableCreate() {
				var columnLabels = ['Time', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday']
				var hourLabels = ['6:00 AM', '7:00 AM', '8:00 AM', '9:00 AM', '10:00 AM', '11:00 AM', '12:00 PM', '1:00 PM', '2:00 PM', '3:00 PM', '4:00 PM', '5:00 PM', '6:00 PM', '7:00 PM', '8:00 PM', '9:00 PM', '10:00 PM']
				var body = document.getElementsByTagName('body')[0];
				var tbl = document.createElement('table');
				
				tbl.style.maxWidth = "500px";
				tbl.style.backgroundColor = "white"
				tbl.style.borderSpacing = "0";
				tbl.style.textAlign = "center"
				tbl.style.marginLeft = "auto";
				tbl.style.marginRight = "auto";
				tbl.style.width = "95%";
				tbl.style.minWidth = "500px";
				
				var tbdy = document.createElement('tbody');
				var trow = document.createElement('tr');
				tbdy.appendChild(trow);
				
				columnLabels.forEach(function(label){
					var tdata = document.createElement('td');
					var l = document.createTextNode(label);
					tableLabels(tdata);
					tdata.setAttribute("colspan", 5);
					tdata.appendChild(l);
					trow.appendChild(tdata);
				});
				var index = 0;
				hourLabels.forEach(function(label){
					for (var i = 0; i < 12; i++){
						var tr = document.createElement('tr');
						tbdy.appendChild(tr);
						
						for (var j = 0; j < 6; j++){
							if(j == 0){
								if(i == 0){
									var td = document.createElement('td');
									var l = document.createTextNode(label);
									tableLabels(td);
									td.style.backgroundColor= "#EEEEEE";
									td.style.height = "25px";
									td.setAttribute("colspan", 5);
									td.setAttribute("rowspan", 12);
									td.appendChild(l);
									tr.appendChild(td);
								}
							}else{
								for (var k = 0; k < 5; k++){
									var td = document.createElement('td');
									var ID = (((j - 1) * 300) + (k * 2000) + (index * 12) + i);
	//Comment out the next line to remove the ID labels from the cells
									//td.appendChild(document.createTextNode(ID));
									tr.appendChild(td);
									td.id= ID;
									if(k == 4)
									{
										if(i == 11)
										{
											bottomLeftCells(td);
										}
										else
										{
											leftCells(td);
										}
									}else{
										if(i == 11){
											bottomCells(td);
										}else{
											cells(td);
										}
									}
								}
							}
						}
					}
					index++;
				});
				tbl.appendChild(tbdy);
				body.appendChild(tbl);
				document.getElementById("conCell").appendChild(tbl);
			}
			
			
			//edit this to add remove more mock data for the picture
			function mockData(){
				var imgSize = "100px 35px";
				document.getElementById(10).style.backgroundColor = "#95D595";
				document.getElementById(2010).style.backgroundColor = "#000064";
				document.getElementById(4010).style.backgroundColor = "#FCB930";
				document.getElementById(6010).style.backgroundColor = "#E50000";
				document.getElementById(8010).style.backgroundColor = "#F5F611";
				document.getElementById(11).style.backgroundImage = "url('images/ConflictIconLevel100.png')";
				document.getElementById(11).style.backgroundSize = imgSize;
				document.getElementById(12).style.backgroundImage = "url('images/ConflictIconLevel100.png')";
				document.getElementById(12).style.backgroundSize = imgSize;
				document.getElementById(13).style.backgroundImage = "url('images/ConflictIconLevel100.png')";
				document.getElementById(13).style.backgroundSize = imgSize;
				document.getElementById(14).style.backgroundImage = "url('images/ConflictIconLevel100.png')";
				document.getElementById(14).style.backgroundSize = imgSize;
				document.getElementById(15).style.backgroundImage = "url('images/ConflictIconLevel100.png')";
				document.getElementById(15).style.backgroundSize = imgSize;
				document.getElementById(16).style.backgroundImage = "url('images/ConflictIconLevel100.png')";
				document.getElementById(16).style.backgroundSize = imgSize;
				document.getElementById(17).style.backgroundImage = "url('images/ConflictIconLevel100.png')";
				document.getElementById(17).style.backgroundSize = imgSize;
				
				document.getElementById(2011).style.backgroundImage = "url('images/ConflictIconLevel200.png')";
				document.getElementById(2011).style.backgroundSize = imgSize;
				document.getElementById(4011).style.backgroundImage = "url('images/ConflictIconLevel300.png')";
				document.getElementById(4011).style.backgroundSize = imgSize;
				document.getElementById(6011).style.backgroundImage = "url('images/ConflictIconLevel400.png')";
				document.getElementById(6011).style.backgroundSize = imgSize;
				document.getElementById(8011).style.backgroundImage = "url('images/ConflictIconLevel500.png')";
				document.getElementById(8011).style.backgroundSize = imgSize;
				
			}
			
function updater()
{
//Load the table of time elements.
	var nCon = document.getElementById("nCon");
	var con = document.getElementById("con");
	var timeSegments = giveMeTime();
	var table = document.getElementById("conCell").childNodes[0];
	var cell;
	var index;
	
	var opCode;
	
	var levelColors = 
	{
		// 100: "dd1",
		// 200: "dd1",
		// 300: "dd1",
		// 400: "dd1",
		// 500: "dd1"
		100: "green",
		200: "blue",
		300: "yellow",
		400: "purple",
		500: "orange"
		//There are a total of 204 time segments, in which each time segment contains an array of days, levels, and within the levels
		//they contain the conflicts.
		//there are 204 time segments.
	}
	for (var i = 0; i < timeSegments.time.length; i++)
	{
    // console.log("i: " + i);
		for (var j = 0; j < timeSegments.time[0].day.length; j++)
		{
		// console.log("j: " + j);
			for (var k = 0; k < timeSegments.time[0].day[0].level.length; k++)
			{
				var setTopBorder = false;
				var setBottomBorder = false;
				var setLeftBorder = false;
				var setRightBorder = false;
				var timeSegment = timeSegments.time[i].day[j].level[k];
				var index = timeSegment.tableIndex();
				// console.log(index);
				var tableCell = document.getElementById(index);
				opCode = getOpCode(timeSegment);
				var topbottomBorders = drawBorders(timeSegment, opCode);
				var currentTop = tableCell.style.borderTop;
				var currentBottom = tableCell.style.borderBottom;
				var currentLeft = tableCell.style.borderLeft;
				var currentRight = tableCell.style.borderRight;
				var isColumn = false;
				var isFloor = false;
				var borderStyle = "thin solid black";
				setTopBorder = topbottomBorders[0];
				setBottomBorder = topbottomBorders[1];
				//If the current borders are table borders, keep them that way.
				//Issue with this is if the borders overlap the column.  How to solve?!?
				if(index >= 8000)
				{
					isColumn = true;
				}
				if(currentBottom == "2px solid black")
				{
					isFloor = true;
				}
				
			
				if(!nCon.checked && !con.checked)
				{
					tableCell.style.backgroundColor = "white";
					setBottomBorder = false;
					setTopBorder = false;
					
					
				}
				//In the case that the user only wants to see non conflict classes:
				else if(nCon.checked && !con.checked)
				{
					if(timeSegment.nonConflictCourses.length > 0)
					{
						tableCell.style.backgroundColor = levelColors[timeSegment.level];
						 setRightBorder = true;
						 setLeftBorder = true;
					}
					else
					{
						tableCell.style.backgroundColor = "white";
						setBottomBorder = false;
						setTopBorder = false;
					}
				}
				else if (!nCon.checked && con.checked)
				{
					if(timeSegment.conflicts.length > 0)
					{
						 tableCell.style.backgroundColor = "red";
						 setRightBorder = true;
						 setLeftBorder = true;
					}
					else
					{
						tableCell.style.backgroundColor = "white";
						setBottomBorder = false;
						setTopBorder = false;
					}
				}
				else if (nCon.checked && con.checked)
				{
					if (timeSegment.nonConflictCourses.length > 0)
					{
						tableCell.style.backgroundColor = levelColors[timeSegment.level];
						setRightBorder = true;
						setLeftBorder = true;
					}
					if (timeSegment.conflicts.length > 0)
					{
						tableCell.style.backgroundColor = "red";
						setRightBorder = true;
						setLeftBorder = true;
					}
					
				}
				
				
				
				if (setRightBorder)
				{
				  tableCell.style.borderRight = borderStyle;
				}
				else if (isColumn)
				{
					tableCell.style.borderRight = "2px solid black";
				}
				else
				{
					tableCell.style.borderRight = "";
				}
				if(setLeftBorder)
				{
				  tableCell.style.borderLeft = borderStyle;
				}
				else
				{
					tableCell.style.borderLeft = "";
				}
				if (setTopBorder)
				{
				  tableCell.style.borderTop = borderStyle;
				}
				else
				{
					tableCell.style.borderTop = "";
				}
				if (setBottomBorder)
				{
				  tableCell.style.borderBottom = borderStyle;
				}
				else if(isFloor)
				{
					tableCell.style.borderBottom = "2px solid black";
				}
				else
				{
					tableCell.style.borderBottom = "";
				}
			}
		}
		
	}
}


function drawBorders(timeSeg, opCode)
{
	var conflictsOrNot;
	var arrayList = [false, false];
	if (opCode == -1)
	{
	
	}
	else
	{
		if (opCode == 0)
		{
			 
			   for (var l = 0; l < timeSeg.conflicts.length; l++)
			   {
					var courses = timeSeg.conflicts[l].getCourses();
					for (var m = 0; m < courses.length; m++)
					{
						  if (courses[m].timeAsIndex("start") == timeSeg.startTime){
							arrayList[0] = true;
						  }
						  if (courses[m].timeAsIndex("end") == timeSeg.startTime){
							arrayList[1] = true;
						  }
					}
			   }
		}
		else if (opCode == 1)
		{
			for (var l = 0; l < timeSeg.nonConflictCourses.length; l++)
			{
				if (timeSeg.nonConflictCourses[l].timeAsIndex("start") == timeSeg.startTime){
				  arrayList[0] = true;
				}
				if (timeSeg.nonConflictCourses[l].timeAsIndex("end") == timeSeg.startTime){
				  arrayList[1] = true;
				}
			}
		}
			
	}
	return arrayList;
}

function getOpCode(timeSeg)
{
	var opCode;
	if (timeSeg.conflicts.length > 0)
	{
		opCode = 0;
	}
	else if (timeSeg.nonConflictCourses.length > 0)
	{
		opCode = 1;
	}
	else
	{
		opCode = -1;
	}
	
	return opCode;
}