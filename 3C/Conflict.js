function Conflict(course1, course2, day, startTime, endTime){
    this.course1 = course1;
    this.course2 = course2;
    this.day = day;
    this.startTime = startTime;
    this.endTime = endTime;
    
    this.containsCourse = function(course){
        if (JSON.stringify(course) === JSON.stringify(course1) || JSON.stringify(course) === JSON.stringify(course2)){
            return true;
        }
        return false;
    }
    
    this.toString = function(){
        return "Conflict: Day: " + this.day + "\n" + course1.toString() + "\n" + course2.toString();
    }
    
    this.getCourses = function(){
        return new Array(course1, course2);
    }
}