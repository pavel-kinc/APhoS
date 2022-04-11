/*
    Script for generating the request to download the file.
*/


function generateCSV() {
    let addData = false;
    let textFileFormat = false;
    if (document.getElementById("radioCsvFull").checked) {
        addData = true;
    } else if (document.getElementById("radioText").checked) {
        textFileFormat = true;
    }
    const searchParams = new URLSearchParams(window.location.search);
    let selects = document.getElementById("apertureSelects")
        .getElementsByTagName("select");
    let selectsArray = Array.from(selects).filter(select => !select.id.includes("ref"));
    let selectsRefArray = Array.from(selects).filter(select => select.id.includes("ref"));
    selectsArray.sort((a, b) => a.id - b.id);
    selectsRefArray.sort((a, b) => a.id - b.id);
    // collecting apertures from the query params
    let aperturesParam = "";
    let aperturesRefParam = "";
    for (let i = 0; i < selectsArray.length; i++) {
        aperturesParam += selectsArray[i].value;
        if (i !== selectsArray.length - 1) {
            aperturesParam += ",";
        }
        aperturesRefParam += selectsRefArray[i].value;
        if (i !== selectsRefArray.length - 1) {
            aperturesRefParam += ",";
        }
    }
    const objectId = searchParams.get("id");
    const refObjectId = searchParams.get("ref-id");
    let unwantedUsers = Array.from(unwantedSelect.options)
        .map(option => option.id.toString());
    const myHeaders = new Headers();
    let formData = new FormData();
    formData.append("object-id", objectId);
    formData.append("ref-object-id", refObjectId);
    formData.append("unwanted-users", unwantedUsers);
    formData.append("apertures", aperturesParam);
    formData.append("ref-apertures", aperturesRefParam);
    formData.append("add-data", addData);
    formData.append("text-file-format", textFileFormat);
    myHeaders.append('X-XSRF-TOKEN', Cookies.get('XSRF-TOKEN'));
    fetch('/object/download', {method: "POST", headers: myHeaders, body: formData})
        .then(response => response.blob())
        .then(blob => download(blob, objectId + "_magnitudes." + (textFileFormat ? "txt" : "csv")));
}
