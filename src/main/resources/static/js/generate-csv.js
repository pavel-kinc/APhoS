
function generateCSV() {
    const searchParams = new URLSearchParams(window.location.search);
    let selects = document.getElementById("apertureSelects")
        .getElementsByTagName("select");
    let selectsArray = Array.from(selects).filter(select => !select.id.includes("ref"));
    let selectsRefArray = Array.from(selects).filter(select => select.id.includes("ref"));
    selectsArray.sort((a, b) => a.id - b.id);
    selectsRefArray.sort((a, b) => a.id - b.id);
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
    const refObjectId = searchParams.get("refId");
    let unwantedUsers = Array.from(unwantedSelect.options)
        .map(option => option.id.toString());
    const myHeaders = new Headers();
    let formData = new FormData();
    formData.append("objectId", objectId);
    formData.append("refObjectId", refObjectId);
    formData.append("unwantedUsers", unwantedUsers);
    formData.append("apertures", aperturesParam);
    formData.append("refApertures", aperturesRefParam);
    myHeaders.append('X-XSRF-TOKEN', Cookies.get('XSRF-TOKEN'));
    fetch('/object/download', {method: "POST", headers: myHeaders, body: formData})
        .then(response => response.blob())
        // TODO: name of the file
        .then(blob => download(blob, objectId + "_magnitudes.csv"));
}
