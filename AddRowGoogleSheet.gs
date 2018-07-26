/*
 * Ejemplo de datos a enviar: {"spreadsheet_id":"1jBtXZdoxIYJlEAnJ8YbQ3NbUmPrBFqgtSbmMHMIQMck", "sheet": "users", "rows":[["4", "Juan", "juan@gmail.com"], ["5", "Maria", "maria@gmail.com"]]}
 * https://script.google.com/macros/s/.../exec
 */
function doPost(request) {
  
  var jsonPost = JSON.parse(request.postData.contents);
  
  var spreadsheetId = jsonPost.spreadsheet_id;
  var sheetName = jsonPost.sheet;
  var rows = jsonPost.rows;
  
  var sheet= SpreadsheetApp.openById(spreadsheetId).getSheetByName(sheetName);
  for(var i = 0; i < jsonPost.rows.length; i++){
    sheet.appendRow(jsonPost.rows[i]);
  }

  return ContentService.createTextOutput(JSON.stringify({sheet:sheetName, rows: rows}));
}
