//Prototype for courses
function Course(crn, courseNumber, level, courseTitle, crossList, 
        type, days, startTime, endTime, room, instructor){
    this.crn = crn;
    this.courseNumber = courseNumber;
    this.level = level;
    this.courseTitle = courseTitle;
    this.crossList = crossList;
    this.type = type;
    this.days = days;
    this.startTime = startTime;
    this.endTime = endTime;
    this.instructor = instructor;
    this.crossListCourses = [];
    
    //Calculate table index using time
    this.timeAsIndex = function(startOrEnd){
        var time;
        if (startOrEnd.toLowerCase() == "start".toLowerCase()){
            time = startTime;
        }
        else if (startOrEnd.toLowerCase() == "end".toLowerCase()){
            time = endTime;
        }
        
        var hour = Number(time.substring(0, time.indexOf(':')));
        
        if (time.toLowerCase().indexOf("p") != -1){
        	if (hour != 12){
            	hour += 12;
        	}
        }
        
        var minuteIndex = time.indexOf(':') + 1;
        var str = time.substring(minuteIndex, minuteIndex + 2);
        var minutes = Number(str);
        var index = (hour - 6) * 12 + minutes / 5;
        return index;
    }
    
    this.toString = function(){
        return this.courseNumber + " " + this.courseTitle + "\n" + this.startTime + "-" + this.endTime + "\n" + this.days;
    }
}

function getDayCourses(courses, day){
  var coursesDay = [];
  for (var i = 0; i < courses.length; i++){
    if (courses[i].days.includes(day)){
      coursesDay.push(courses[i]);
    }
  }
  return coursesDay;
}

function getLevelCourses(courses, level){
    var coursesLevel = [];
    for (var i = 0; i < courses.length; i++){
        if (courses[i].level == level){
            coursesLevel.push(courses[i]);
        }
    }
    return coursesLevel;
}

function earlierStartingCourse(course1, course2){
    var course1StartTime = course1.timeAsIndex("start");
    var course1EndTime = course1.timeAsIndex("end");
    var course2StartTime = course2.timeAsIndex("start");
    var course2EndTime = course2.timeAsIndex("end");
    
    if (course1StartTime == course2StartTime && 
        course1EndTime == course2EndTime){
        if (course1.crn > course2.crn){
            return course1;
        }
        else{
            return course2;
        }
    }
    
    if (course1StartTime <= course2StartTime){
        return course1;
    }
    else{
        return course2;
    }
}