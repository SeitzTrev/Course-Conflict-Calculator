/* set up drag-and-drop event */
function handleDrop(e) {
  e.stopPropagation();
  e.preventDefault();
  var files = e.dataTransfer.files;
  fileHandler(files);
  /*
  var i,f;
  for (i = 0, f = files[i]; i != files.length; ++i) {
    var reader = new FileReader();
    var name = f.name;
    reader.onload = function(e) {
      var data = e.target.result;

       if binary string, read with type 'binary' 
      
	var workbook = XLSX.read(data, {type: 'binary'});
	*/
    
      
      
    //};
	
	
  //}
}

function handleDragover(e) {
	e.stopPropagation();
	e.preventDefault();
	e.dataTransfer.dropEffect = 'copy';
}

//Variables to drag and drop the files and allow 'drop' to detect these changes.
var drop = document.getElementById("drop");
drop.addEventListener('dragenter', handleDragover, false);
drop.addEventListener('dragover', handleDragover, false);
drop.addEventListener('drop', handleDrop, false);

function handleFile(e) {
  var files = e.target.files;
  /*
  var i,f;
  
  for (i = 0, f = files[i]; i != files.length; ++i) {
    var reader = new FileReader();
    var name = f.name;
    reader.onload = function(e) {
      var data = e.target.result;

      var workbook = XLSX.read(data, {type: 'binary'});

      main(workbook);
    };
    reader.readAsBinaryString(f);
  }
  */
  
  fileHandler(files);
}

//This will store the form temporarily so that we can update it each time the user plays around with the checkboxes.

function fileHandler(files)
{		
		var i,f;
		var reader;
		var name;
		//We want to process multiple files?
		for (i = 0, f = files[i]; i != files.length; ++i) 
		{
			reader = new FileReader();
			name = f.name;
			reader.onload = function(e) 
			{
				var data = e.target.result;

				/* if binary string, read with type 'binary' */
				var workbook = XLSX.read(data, {type: 'binary'});
				main(workbook);
			};
		
			reader.readAsBinaryString(f);
		}
}
var fileselect = document.getElementById("fileselect");
fileselect.addEventListener('change', handleFile, false);

function main(workbook){
  var courses = parseCourseData(workbook);
  var timeSegments = createTimeSegments(courses);
  populateTable(timeSegments);
}

//Read all course data from excel file
function parseCourseData(workbook){
    var sheetName = workbook.SheetNames[0];
    var sheet = workbook.Sheets[sheetName];
    //Turn sheet into array of Objects
    var data = XLSX.utils.sheet_to_json(sheet, {header:1}); // <-- this is an array of arrays that you can use
    
    //Put all parsed data into Course objects
    var courses = [];
    for (i = 1; i < data.length; i++){
        if ((data[i][11] == "LEC" || data[i][11] == "LAB") && data[i][12] != undefined){
            var crn = data[i][0];
            var courseNumber = data[i][1];
            var level = data[i][1];
            level = level[level.indexOf('-') + 1];
            level = Number(level) * 100;
            var courseTitle = data[i][2];
            var crossList = data[i][3];
            var type = data[i][11];
            var days = data[i][12];
            var startTime = data[i][13];
            var endTime = data[i][14];
            var room = data[i][15];
            var instructor = data[i][16];
          
            courses.push(new Course(crn, courseNumber, level, courseTitle, 
                    crossList, type, days, startTime, endTime, room, instructor));
        }
    }
    
    //Determine all cross listed courses
    for (var i = 0; i < courses.length; i++){
      for (var j = 0; j < courses.length; j++){
        if (courses[i] != courses[j] && courses[i].crossList != undefined && 
        courses[i].crossList == courses[j].crossList){
        
        courses[i].crossListCourses.push(courses[j]);
        courses.splice(j, 1);
        j--;
        console.log("crosslisted " + courses[i].courseNumber + " and " + courses[j].courseNumber);
        }
      }
    }
    
    return courses;
}

function createTimeSegments(courses){
  //Create all TimeSegments for a day column of the table
  //17 hours * 60 mins / 5 minute blocks = 192
  //5 course levels per day
  var timeSegments = {
    time: new Array(204)
  };
  var days = ["M", "T", "W", "R", "F"];
  
  for (var i = 0; i < timeSegments.time.length; i++){
    timeSegments.time[i] = {
      day: new Array(5)
    };
    for (var j = 0; j < days.length; j++){
      timeSegments.time[i].day[j] = {
        level: new Array(5)
      };
    }
  }
  var accumulativeConflicts = [];
  
  //Create each time segment according to time, day, and course level
  for (var time = 0; time < 204; time++){
    for (var i = 0; i < days.length; i++){
      for (var level = 100; level <= 500; level += 100){
        // console.log("time: " + time + ", day: " + days[i] + ", level: " + level);
        var dayCourses = getDayCourses(courses, days[i]);
        var levelDayCourses = getLevelCourses(dayCourses, level);
        var timeSegment = new TimeSegment(time, time + 1, level, days[i]);
        timeSegment.findConflictsAndCourses(levelDayCourses, accumulativeConflicts);
        if (timeSegment.conflicts.length > 0){
          console.log("time: " + time + " i: " + i + " level: " + level);
        }
        timeSegments.time[time].day[i].level[level/100 - 1] = timeSegment;
      }
    }
  }
  
  return timeSegments;
}

function populateTable(timeSegments){
  var levelColors = {
    // 100: "dd1",
    // 200: "dd1",
    // 300: "dd1",
    // 400: "dd1",
    // 500: "dd1"
    100: "#95D595",
    200: "#000064",
    300: "#FCB930",
    400: "#E50000",
    500: "#F5F611"
  };
  
  var levelIcons = {
    100: "images/ConflictIconLevel100.png",
    200: "images/ConflictIconLevel200.png",
    300: "images/ConflictIconLevel300.png",
    400: "images/ConflictIconLevel400.png",
    500: "images/ConflictIconLevel500.png"
  };
  
  for (var i = 0; i < timeSegments.time.length; i++){
    // console.log("i: " + i);
    for (var j = 0; j < timeSegments.time[0].day.length; j++){
      // console.log("j: " + j);
      for (var k = 0; k < timeSegments.time[0].day[0].level.length; k++){
        var timeSegment = timeSegments.time[i].day[j].level[k];
        var index = timeSegment.tableIndex();
        // console.log(index);
        var tableCell = document.getElementById(index);
        tableCell.timeSegment = timeSegment;
        var setTopBorder = false;
        var setBottomBorder = false;
        var setRightBorder = false;
        var setLeftBorder = false;
        var conflicts = timeSegment.conflicts;
        var nonConflictCourses = timeSegment.nonConflictCourses;
		
		/*
		var nConBox = document.getElmentById("nCon");
		var conBox  = document.getElementById("con");
		*/

		if (document.getElementById("nCon").checked && !document.getElementById("con").checked)
		{
			conflicts = [];
		}
		else if (!document.getElementById("nCon").checked && document.getElementById("con").checked)
		{
			nonConflictCourses = [];
		}
		
        if (conflicts.length > 0){
          // tableCell.style.backgroundImage = "url(" + levelIcons[timeSegment.level] + ")";
          tableCell.style.backgroundColor = "black";
          setRightBorder = true;
          setLeftBorder = true;
          tableCell.addEventListener("click", function(){displayCourseInfo(this);}, false);
          for (var l = 0; l < conflicts.length; l++){
            var courses = conflicts[l].getCourses();
            for (var m = 0; m < courses.length; m++){
              if (courses[m].timeAsIndex("start") == timeSegment.startTime){
                setTopBorder = true;
              }
              if (courses[m].timeAsIndex("end") == timeSegment.startTime){
                setBottomBorder = true;
              }
            }
          }
        }
        else if (nonConflictCourses.length > 0){
          tableCell.style.backgroundColor = levelColors[timeSegment.level];
          setRightBorder = true;
          setLeftBorder = true;
          tableCell.addEventListener("click", function(){displayCourseInfo(this);}, false);
          for (var l = 0; l < nonConflictCourses.length; l++){
            if (nonConflictCourses[l].timeAsIndex("start") == timeSegment.startTime){
              setTopBorder = true;
            }
            if (nonConflictCourses[l].timeAsIndex("end") == timeSegment.startTime){
              setBottomBorder = true;
            }
          }
        }
        
        var borderStyle = "thin solid black";
        
        //Set borders around courses
        if (setRightBorder){
          tableCell.style.borderRight = borderStyle;
        }
        if(setLeftBorder){
          tableCell.style.borderLeft = borderStyle;
        }
        if (setTopBorder){
          tableCell.style.borderTop = borderStyle;
        }
        if (setBottomBorder){
          tableCell.style.borderBottom = borderStyle;
        }
        
      }
    }
  }
}

function displayCourseInfo(tableCell){
  var coursesOrConflicts;
  var timeSegment = tableCell.timeSegment;
 
  if (timeSegment.conflicts.length > 0){
    coursesOrConflicts = timeSegment.conflicts;
  }
  else if (timeSegment.nonConflictCourses.length > 0){
    coursesOrConflicts = timeSegment.nonConflictCourses;
  }
  
  var courseData = "";
  
  for (var i = 0; i < coursesOrConflicts.length; i++){
    courseData += coursesOrConflicts[i].toString();
  }
  
  var textbox = document.getElementById("courseInfoText");
  textbox.value = courseData;
}


