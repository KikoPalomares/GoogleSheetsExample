/*
 * Ejemplo de datos a enviar: {"spreadsheet_id":"1jBtXZdoxIYJlEAnJ8YbQ3NbUmPrBFqgtSbmMHMIQMck", "sheet": "users", "row_position": 1}
 * https://script.google.com/macros/s/.../exec
 */
function doPost(request) {
  
  var jsonPost = JSON.parse(request.postData.contents);
  
  var spreadsheetId = jsonPost.spreadsheet_id;
  var sheetName = jsonPost.sheet;
  var row_position = jsonPost.row_position;
  
  var sheet= SpreadsheetApp.openById(spreadsheetId).getSheetByName(sheetName);
  sheet.deleteRow(row_position);
  
  return ContentService.createTextOutput(JSON.stringify({row_position:row_position}));
}
