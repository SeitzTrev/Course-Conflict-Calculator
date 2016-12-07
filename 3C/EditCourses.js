var hours = [];
for (var i = 1; i < 13; i++){
    if (i < 10){
        hours.push("0" + i + "");
    } else {
        hours.push(i + "");
    }
}

var minutes = [];
minutes.push("00");
minutes.push("05");
for (var i = 10; i < 60; i += 5){
    minutes.push(i + "");
}

function createCourseEditTable(courses){
    var editCourseHeader = document.createElement("h2");
    editCourseHeader.appendChild(document.createTextNode("Edit Courses"));
    editCourseHeader.id = "editCourseHeader";
    editCourseHeader.style.textAlign = "left";
    
    var editCourseDisplayTableButton = document.createElement("input");
    editCourseDisplayTableButton.id = "editCourseDisplayButton";
    editCourseDisplayTableButton.type = "submit";
    editCourseDisplayTableButton.innerHTML = "Update courses";
    editCourseDisplayTableButton.addEventListener("click", editCourseDisplayTable(courses));
    
    var courseEditDiv = document.getElementById("courseEdit");
    while (courseEditDiv.firstChild){
        courseEditDiv.removeChild(courseEditDiv.firstChild);
    }
    
    courseEditDiv.appendChild(editCourseHeader);
    
    var table = document.createElement("table");
    table.classList.add("courseEditTable");
    table.id = "courseEditTable";
    
    var tableHead = document.createElement("thead");
    tableHead.classList.add("courseEditHead");
    
    var headers = ["Name", "Start Time", "End Time", "Days"];
    var headerSizes = ["322", "165", "165", "142"];
    
    for (var i = 0; i < headers.length; i++){
        var th = document.createElement("th");
        th.classList.add("courseEditRow");
        th.classList.add("courseEditCell");
        th.width = headerSizes[i];
        var textNode = document.createTextNode(headers[i]);
        th.appendChild(textNode);
        tableHead.appendChild(th);
    }
    
    table.appendChild(tableHead);
    
    var tableBody = document.createElement("tbody");
    tableBody.classList.add("courseEditBody");
    
    for (var i = 0; i < courses.length; i++){
        var tr = document.createElement("tr");
        tr.classList.add("courseEditRow");
        var courseNumber = courses[i].courseNumber;
        tr.id = courseNumber;
        
        var courseNametd = document.createElement("td");
        courseNametd.classList.add("courseEditCell");
        var courseNametext = document.createTextNode(courses[i].courseNumber + " " + courses[i].courseTitle);
        courseNametd.appendChild(courseNametext);
        tr.appendChild(courseNametd);
        
        trAppendTimeSelect(tr, courses[i].startTime);
        
        trAppendTimeSelect(tr, courses[i].endTime);
        
        var daystd = document.createElement("td");
        daystd.classList.add("daystd");
        var days = ["M", "T", "W", "R", "F"];
        
        for (var j = 0; j < days.length; j++){
            var checkTextNode = document.createTextNode(days[j]);
            daystd.appendChild(checkTextNode);
            
            var checkbox = document.createElement("input");
            checkbox.type = "checkbox";
            checkbox.value = days[j];
            
            
            if (courses[i].days.includes(days[j])){
                checkbox.checked = true;
            }
            daystd.appendChild(checkbox);
        }
        tr.appendChild(daystd);
        
        tableBody.appendChild(tr);
    }
    
    table.appendChild(tableBody);
    courseEditDiv.appendChild(table);
    courseEditDiv.appendChild(editCourseDisplayTableButton);
    
}

function trAppendTimeSelect(tr, time){
    var td = document.createElement("td");
    td.classList.add("courseEditCell");
    
    var hoursMinutes = time.split(":");
    
    var amOrPm = time.substring(time.length - 2);
    
    hoursMinutes[1] = hoursMinutes[1].substring(0, hoursMinutes[1].length - 2);
    
    var hourSelect = document.createElement("select");
    
    for (var i = 0; i < hours.length; i++){
        var option = document.createElement("option");
        option.text = hours[i];
        hourSelect.add(option);
    }
    
    hourSelect.selectedIndex = hours.indexOf(hoursMinutes[0]);
    
    td.appendChild(hourSelect);
    
    var colonTextNode = document.createTextNode(":");
    
    td.appendChild(colonTextNode);
    
    var minuteSelect = document.createElement("select");
    for (var i = 0; i < minutes.length; i++){
        var option = document.createElement("option");
        option.text = minutes[i];
        minuteSelect.add(option);
    }
    
    minuteSelect.selectedIndex = minutes.indexOf(hoursMinutes[1]);
    
    td.appendChild(minuteSelect);

    var amOrPmSelect = document.createElement("select");
    var amOption = document.createElement("option");
    amOption.text = "AM";
    amOrPmSelect.add(amOption);
    var pmOption = document.createElement("option");
    pmOption.text = "PM";
    amOrPmSelect.add(pmOption);
    
    if (amOrPm == "AM"){
        amOrPmSelect.selectedIndex = 0;
    } else {
        amOrPmSelect.selectedIndex = 1;
    }
    
    td.appendChild(amOrPmSelect);
    
    tr.appendChild(td);
}

function editCourseDisplayTable(courses){
    return function(){
        for (var i = 0; i < courses.length; i++){
            var tr = document.getElementById(courses[i].courseNumber);
            
            var startTimeElements = tr.childNodes[1].childNodes;
            var startTime = combineTimeElements(startTimeElements);
            
            var endTimeElements = tr.childNodes[2].childNodes;
            var endTime = combineTimeElements(endTimeElements);
            
            var dayCheckboxes = tr.childNodes[3].childNodes;
            var days = "";
            for (var j = 0; j < dayCheckboxes.length; j++){
                if (dayCheckboxes[j].checked){
                    days += dayCheckboxes[j].value;
                }
            }
            
            courses[i] = new Course(courses[i].crn, courses[i].courseNumber, courses[i].level, courses[i].courseTitle, 
                                    courses[i].crossList, courses[i].type, days, startTime, endTime,
                                    courses[i].room, courses[i].instructor);
        }
        populateTable(courses);
    };
}

function combineTimeElements(timeElements){
    var time = "";
    for (var i = 0; i < timeElements.length; i++){
        if (timeElements[i].tagName == "SELECT"){
            var select = timeElements[i];
            time += select[select.selectedIndex].text;
        } else {
            time += timeElements[i].data;
        }
    }
    return time;
}