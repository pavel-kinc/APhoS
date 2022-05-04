/*
    Enable the file upload submit button.
 */

const inputElement = document.getElementById("fileSubmitter");
inputElement.addEventListener("change", enableButton, false);

function enableButton() {
    document.getElementById("submitButton").disabled = false;
}