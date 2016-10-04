'''
Created on Oct 4, 2016

@author: Trevor
'''

fname = 'html.txt'
fmode = 'w'
bufferVal = 256


with open(fname,fmode, bufferVal) as outf:
    topLabels = ['Time', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']
    hourLabels = ['6:00AM', '7:00AM', '8:00AM', '9:00AM', '10:00AM', '11:00AM', '12:00PM', '1:00PM', '2:00PM', '3:00PM', '4:00PM', '5:00PM', '6:00PM', '7:00PM', '8:00PM', '9:00PM', '10:00PM']
    index = 0
    outf.write('\t\t<table>\n')
    outf.write('\t\t\t<tbody>\n')
    outf.write('\t\t\t\t<tr>\n')
    for i in topLabels:
        string = '\t\t\t\t\t<td class=\"center\" colspan="5">'
        string += i
        string += '</td>\n'
        outf.write(string)
    
    outf.write('\t\t\t\t</tr>\n')
    
    for h in range(len(hourLabels)):
        for j in range(12):
            
            outf.write('\t\t\t\t<tr>\n')
            for k in range(7):
                    
                if (k == 0):
                    if(j == 0):
                        string1 = '\t\t\t\t\t<td class=\"center\" rowspan=\"12\" colspan=\"5\">'
                        string1 += hourLabels[h]
                        string1 += '</td>\n'
                        outf.write(string1)
                else:
                    for l in range(5):
                        num = (((k - 1) * 300) + (l * 2000) + (h*12) + (j))
                        string2 = '\t\t\t\t\t<td class="right" id=\"'
                        string2 += str(num)
                        string2 += '\">'
                        string2 += str(num)
                        string2 += '</td>\n'
                        outf.write(string2)
                    
            outf.write('\t\t\t\t</tr>\n')
    outf.write('\t\t\t</tbody>\n')
    outf.write('\t\t</table>\n')
    
    
    outf.flush()