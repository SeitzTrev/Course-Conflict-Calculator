
			//cell formatting
			function tableLabels(el){
				el.style.textAlign = "center";
				el.style.border= "2px solid #444444";
				el.style.backgroundColor= "#BBBBBB";
				el.style.height= "25px";
				el.style.maxWidth= "80px";
				el.style.minWidth= "80px";
				//el.style.fontSize = "medium";

			}
			
			//cell formatting
			function cells(el){
// 				el.style.borderRight= "1px dotted #444444";
				el.style.textAlign= "center";
				el.style.minWidth= "14px";
			}
			
			//cell formatting
			function leftCells(el){
				el.style.borderRight= "2px solid #444444";
				el.style.textAlign= "center";
				el.style.minWidth= "13px";
			}
			
			//cell formatting
			function bottomCells(el){
				el.style.borderRight= "1px dotted #000000";
				el.style.borderBottom= "2px solid #000000";
				el.style.textAlign= "center";
				el.style.minWidth= "14px";
			}
			
			//cell formatting
			function bottomLeftCells(el){
// 				el.style.borderRight= "2px solid #000000";
				el.style.borderBottom= "2px solid #000000";
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
									td.style.height = "70";
									td.setAttribute("colspan", 5);
									td.setAttribute("rowspan", 12);
									td.style.height= "50px";
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
									if(k == 4){
										if(i == 11){
											bottomLeftCells(td);
										}else{
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