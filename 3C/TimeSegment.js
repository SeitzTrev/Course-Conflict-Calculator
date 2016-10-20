function TimeSegment(startTime, endTime, level, day){
  this.startTime = startTime;
  this.endTime = endTime;
  this.level = level;
  this.day = day;
  this.conflicts = [];
  this.nonConflictCourses = [];
  
  //Finds all conflicts and nonconflicts and adds them to
  //conflictCourses and nonConflictCourses
  this.findConflictsAndCourses = function (courses, accumulativeConflicts){
    for (var i = 0; i < courses.length; i++){
      
      /*Add course to nonConflictCourses if any part of the course
      overlaps with this TimeSegment's time boundaries*/
      if (this.courseInTimeBoundaries(courses[i], this.startTime, this.endTime)){
        
        var courseIsCrossListed = false;
        
        /*Add course to nonConflictCourses as long as course is not
        cross listed with a course currently in nonConflictCourses*/
        for (var j = 0; j < courses[i].crossListCourses.length; j++){
            for (var k = 0; k < this.nonConflictCourses.length; k++){
                if (!courseIsCrossListed && JSON.stringify(this.nonConflictCourses[k]) === JSON.stringify(courses[i])){
                    courseIsCrossListed = true;
                    break;
                }
            }
        }
        
        if (!courseIsCrossListed){
            this.nonConflictCourses.push(courses[i]);
        }
        
        //Compare with all other courses to examine for conflicts
        for (var j = 0; j < courses.length; j++){
          
          //Check for later starting course being in the time boundaries
          if (this.courseInTimeBoundaries(courses[j])){
              //Check for courses having overlapping times
              if (i != j && ((courses[i].startTime >= courses[j].startTime && courses[i].startTime <= courses[j].endTime) ||
              (courses[i].startTime <= courses[j].startTime && courses[i].endTime >= courses[j].startTime) ||
              (courses[i].startTime <= courses[j].startTime && courses[i].endTime >= courses[j].endTime) ||
              (courses[i].startTime >= courses[j].startTime && courses[i].endTime <= courses[j].endTime))){
                  
                  //Create conflict with earlier starting course first
                  var conflict;
                  if (earlierStartingCourse(courses[i], courses[j]) == courses[i]){
                      conflict = new Conflict(courses[i], courses[j], this.day, this.startTime, this.endTime);
                  }
                  else{
                      conflict = new Conflict(courses[j], courses[i], this.day, this.startTime, this.endTime);
                  }
                  
                  //Check if conflict is a duplicate 
                  var duplicate = false;
                  for (var k = 0; k < accumulativeConflicts.length; k++){
                    var conflict1Str = JSON.stringify(accumulativeConflicts[k]);
                    var conflict2Str = JSON.stringify(conflict);
                      if (conflict1Str === conflict2Str){
                          duplicate = true;
                          break;
                      }
                  }
                  
                  if (!duplicate){
                      console.log("Found conflict:\n" + conflict.toString());
                    this.conflicts.push(conflict);
                    accumulativeConflicts.push(conflict);
                  }
              }
          }
        }
      }
    }
    
    //Remove all conflicted courses from nonConflictCourses
    for (var i = 0; i < this.conflicts.length; i++){
      for (var j = 0; j < this.nonConflictCourses.length; j++){
        if (this.conflicts[i].containsCourse(this.nonConflictCourses[j])){
          this.nonConflictCourses.splice(j, 1);
          j--;
        }
      }
    }
  };
  
  //Checks if given course is in TimeSegment time boundaries
  this.courseInTimeBoundaries = function (course){
    var courseStartTime = course.timeAsIndex("start");
    var courseEndTime = course.timeAsIndex("end");
    if (((courseStartTime < this.endTime && courseEndTime >= this.startTime) ||
          (courseEndTime < this.endTime && courseEndTime >= this.startTime) ||
          (courseStartTime <= this.startTime && courseEndTime >= this.endTime))){
              return true;
          } else{
              return false;
          }
  }
  
  this.tableIndex = function(){
    var index = startTime;
    var days = ["M", "T", "W", "R", "F"];
    var levels = [100, 200, 300, 400, 500];
    index += (300 * days.indexOf(day));
    index += (2000 * levels.indexOf(level));
    return index;
  }
}



