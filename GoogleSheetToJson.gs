/**
 * Ejemplo de uso:
 * https://script.google.com/macros/s/.../exec?spreadsheetId=1jBtXZdoxIYJlEAnJ8YbQ3NbUmPrBFqgtSbmMHMIQMck&sheet=users
 */
function getData(spreadsheetId, sheet) {
  var rangeName = sheet+'!A1:Z';  
  var values = Sheets.Spreadsheets.Values.get(spreadsheetId, rangeName).values;
 
  if (!values) {
    return {error: 'No data found'}
  } else {
    
    var responseJson = [];
    
    for (var row = 1; row < values.length; row++) {
      var item ={};
      
      for(var column = 0; column < 26; column++){
        item[values[0][column]] = values[row][column]
      }
      responseJson.push(item);
    }
    return responseJson;
  }
}


function doGet(request) {
  if (request.parameter.spreadsheetId !== undefined && request.parameter.sheet !== undefined){
    return ContentService.createTextOutput(JSON.stringify(getData(request.parameter.spreadsheetId, request.parameter.sheet)));
    
  }else{
    return ContentService.createTextOutput(JSON.stringify({error:"Parameter spreadsheetId or sheet not found"}));
  }
}